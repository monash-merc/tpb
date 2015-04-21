/*
 * Copyright (c) 2011-2013, Monash e-Research Centre
 * (Monash University, Australia)
 * All rights reserved.
 *
 * Redistribution and use in source and binary forms, with or without
 * modification, are permitted provided that the following conditions are met:
 * 	* Redistributions of source code must retain the above copyright
 * 	  notice, this list of conditions and the following disclaimer.
 * 	* Redistributions in binary form must reproduce the above copyright
 * 	  notice, this list of conditions and the following disclaimer in the
 * 	  documentation and/or other materials provided with the distribution.
 * 	* Neither the name of the Monash University nor the names of its
 * 	  contributors may be used to endorse or promote products derived from
 * 	  this software without specific prior written permission.
 *
 * This program is distributed in the hope that it will be useful, but WITHOUT ANY WARRANTY;
 * without even the implied warranty of MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.
 * See the GNU Affero General Public License for more details.
 *
 * THIS SOFTWARE IS PROVIDED BY THE COPYRIGHT HOLDERS AND CONTRIBUTORS "AS IS" AND ANY
 * EXPRESS OR IMPLIED WARRANTIES, INCLUDING, BUT NOT LIMITED TO, THE IMPLIED
 * WARRANTIES OF MERCHANTABILITY AND FITNESS FOR A PARTICULAR PURPOSE ARE
 * DISCLAIMED. IN NO EVENT SHALL THE COPYRIGHT HOLDER OR CONTRIBUTORS BE LIABLE FOR ANY
 * DIRECT, INDIRECT, INCIDENTAL, SPECIAL, EXEMPLARY, OR CONSEQUENTIAL DAMAGES
 * (INCLUDING, BUT NOT LIMITED TO, PROCUREMENT OF SUBSTITUTE GOODS OR SERVICES;
 * LOSS OF USE, DATA, OR PROFITS; OR BUSINESS INTERRUPTION) HOWEVER CAUSED AND
 * ON ANY THEORY OF LIABILITY, WHETHER IN CONTRACT, STRICT LIABILITY, OR TORT
 * (INCLUDING NEGLIGENCE OR OTHERWISE) ARISING IN ANY WAY OUT OF THE USE OF THIS
 * SOFTWARE, EVEN IF ADVISED OF THE POSSIBILITY OF SUCH DAMAGE.
 *
 * You should have received a copy of the GNU Affero General Public License along with
 * this program. If not, see <http://www.gnu.org/licenses/>.
 */

$("a.tl_ev_sum").live('click', function (event) {
    event.preventDefault();
    var hrefLink = $(this);
    var tlTypeLevel = hrefLink.attr("id");
    var viewUrl = hrefLink.attr('href');
    removeHighLightColor();
    var tlColorTd = hrefLink.parent();
    highLightTLColor(tlColorTd);
    TLEvSummary(viewUrl);
})

//highlight the traffic light color if it's clicked
function highLightTLColor(tlColorTd) {
    tlColorTd.attr("style", "border:solid 1px #0033ff; background:rgba(1, 149, 194, 0.2);")
    tlColorTd.attr('class', 'tl_hl_color');
}

//remove the previous clicked highlight color
function removeHighLightColor() {
    var highlightColorTd = $("td.tl_hl_color");
    if (highlightColorTd != null && highlightColorTd != 'undefined') {
        highlightColorTd.removeAttr('style');
        highlightColorTd.removeAttr('class');
    }
}

function TLEvSummary(linkUrl) {
    $.ajax({
            type:"get",
            url:linkUrl,
            cache:false,
            contentType:'application/json; charset=utf-8',
            dataType:'json',
            success:function (respdata) {
                var ok = respdata.success;
                if (ok == 'true') {
                    TLEvSummarySuccess(respdata);
                } else {
                    showErrorDialog(respdata.message);
                }
            },
            error:function (request, exception) {
                var errormsg = getErrorMsg(request, exception);
                showErrorDialog(errormsg);
            }
        }
    )
}

function TLEvSummarySuccess(respData) {
    //get the Traffic Light Evidence Summary Header
    var tlEvSummary = respData.tlEvidenceSummary;
    var summaryHeader = tlEvSummary.tlEvidenceSummaryHeader;
    //get the type name
    var type = summaryHeader.tpbDataType;
    if (type != null) {
        type = type.toLowerCase();
    }

    var tlEvSummaryDiv = $('.tl_ev_summary_div');
    tlEvSummaryDiv.html();
    tlEvSummaryDiv.attr('id', (type + '_sum'));
    tlEvSummaryDiv.show();
    //create data type gene's evidence summary panel
    var tlSumPanel = "<div class='evidnece_header'>" +
        "<div class='tl_evidence_title'>" + summaryHeader.geneName + " - " + summaryHeader.typeDisplayName + " (" + summaryHeader.typeShortName + ")</div>" +
        "<table class='tl_sum_tab'>" +
        "<thead>" +
        "<tr>" +
        "<th width='12%'>DataSource</th>" +
        "<th width='10%'>Gene Symbol</th>" +
        "<th width='13%'>Accession</th>" +
        "<th width='18%'>Type</th>" +
        "<th width='5%'>Level</th>" +
        "<th width='37%'>Evidence</th>" +
        "<th width='5%'>Details</th>" +
        "</tr>" +
        "</thead>" +
        "</table>" +
        "</div>";
    var tlEvidences = tlEvSummary.tlEvidences;
    if (tlEvidences != null) {
        $.each(tlEvidences, function (key, tls) {
            var isLastLevel = tls.lastLevel;

            var tlColorLevel = tls.colorLevel;
            var colorClass = 'tl1_color';
            if (tlColorLevel == '4') {
                colorClass = 'tl4_color';
            }
            if (tlColorLevel == '3') {
                colorClass = 'tl3_color';
            }
            if (tlColorLevel == '2') {
                colorClass = 'tl2_color';
            }
            if (tlColorLevel == '1') {
                colorClass = 'tl1_color';
            }
            var dbSrcId = tls.dbSource;
            var tpbType = tls.tpbDataType;
            var identifiedId = dbSrcId + "_" + tpbType;

            var tlLevel = tls.tlLevel;
            var typeLevel = "tl_l" + tlLevel;
            var dentClss = 'tlevel_1';

            if (tlLevel == 2) {
                dentClss = 'tlevel_2';
            }
            if (tlLevel == 3) {
                dentClss = 'tlevel_3';
            }
            if (tlLevel == 4) {
                dentClss = 'tlevel_4';
            }

            tlSumPanel += "<div class='ev_separator'></div><div class='" + identifiedId + "' id= '" + typeLevel + "' >" +
                "<table class='tl_sum_tab'>" +
                "<tbody>" +
                "<tr class='tr_normal' onMouseOver=\"this.className='tr_highlight'\" onMouseOut=\"this.className='tr_normal'\">" +
                "<td width='12%'>" + tls.dbSource + "</td>" +
                "<td width='10%'>" + tls.geneName + "</td>" +
                "<td width='13%'>" + tls.ensemblId + "</td>" +
                "<td width='18%'><div class='" + dentClss + "'>" + tls.typeShortName + "</div></td>" +
                "<td width='5%'>" +
                "<div class='" + colorClass + "'>&nbsp;</div>" +
                "</td>" +
                "<td width='37%'>Summary</td>" +
                "<td width='5%'>" +
                "<div class='tl_link'>";
            if (isLastLevel) {
                tlSumPanel += "<a class='view_evidence' id='" + identifiedId + "' href='viewevidences.jspx?tpbDataType=" + tls.tpbDataType + "&geneId=" + tls.geneId + "' >";
            } else {
                tlSumPanel += "<a class='view_src_gene_sum'  id='" + identifiedId + "' href='srcgsum.jspx?dbSource=" + tls.dbSource + "&tpbDataType=" + tls.tpbDataType + "&geneId=" + tls.geneId + "'>";
            }
            tlSumPanel += "<span class='span_expand'>&nbsp;</span></a></div>" +
                "</td>" +
                "</tr>" +
                "</tbody>" +
                "</table>" +
                "<div class='" + identifiedId + "_All_Subs'></div>" +
                "</div>";
        });
    } else {
        tlSumPanel += "<div class='none_evidence'>The evidence is not available</div>"
    }
    tlEvSummaryDiv.html(tlSumPanel);
}

function showErrorDialog(errorMsg) {
    var htmlMessage = "<div class='resp_error_msg'>" + errorMsg + "</div> ";
    $('.tl_ev_summary_div').mPopup({
        title:"Error",
        content:htmlMessage,
        close_on_body_click:true,
        width:400
    });
    return false;
}

function getErrorMsg(request, exception) {
    var errormsg = '';
    if (request.status === 0) {
        errormsg = 'Failed to connect to the server';
    } else if (request.status == 404) {
        errormsg = 'The requested page not found';
    } else if (request.status == 500) {
        errormsg = 'The internal server error';
    } else if (exception === 'parsererror') {
        errormsg = 'The requested JSON parse failed';
    } else if (exception === 'timeout') {
        errormsg = 'Connection time out';
    } else if (exception === 'abort') {
        errormsg = 'The request aborted';
    } else {
        errormsg = 'Failed to call the service. ' + request.responseText;
    }
    return errormsg;
}


//view type evidence summary by source
$("a.view_src_gene_sum").live('click', function (event) {
    event.preventDefault();
    var hrefLink = $(this);
    var linkUrl = hrefLink.attr('href');
    var linkId = hrefLink.attr('id');
    if (requestForCollapse(hrefLink)) {
        collapseSubs(linkId);
        //reset it as expand
        changeLinkAsExpand(hrefLink);
    } else {
        srcGeneEvSummary(linkUrl, linkId);
        //change link as collapse status
        changeLinkAsCollapse(hrefLink);
    }
})

function collapseSubs(parentId) {
    var tobeRemovedDiv = $('div.' + parentId + '_All_Subs');
    if (tobeRemovedDiv != null) {
        tobeRemovedDiv.html('');
    }
}

function requestForCollapse(hrefLink) {
    var spanEL = hrefLink.find('span');
    var clss = spanEL.attr('class');
    if (clss == 'span_collapse') {
        return true;
    } else {
        return false;
    }
}

function changeLinkAsCollapse(hrefLink) {
    //change link as collapse status
    var spanEL = hrefLink.find('span');
    spanEL.attr('class', 'span_collapse');
}

function changeLinkAsExpand(hrefLink) {
    var spanEL = hrefLink.find('span');
    spanEL.attr('class', 'span_expand');
}

function srcGeneEvSummary(viewUrl, linkId) {
    $.ajax({
            type:"get",
            url:viewUrl,
            cache:false,
            contentType:'application/json; charset=utf-8',
            dataType:'json',
            success:function (respdata) {
                var ok = respdata.success;
                if (ok == 'true') {
                    srcGeneEvSummarySuccess(respdata, linkId);
                } else {
                    showErrorDialog(respdata.message);
                }
            },
            error:function (request, exception) {
                var errormsg = getErrorMsg(request, exception);
                showErrorDialog(errormsg);
            }
        }
    )
}

function srcGeneEvSummarySuccess(respData, parentDivId) {
    var parentDiv = $('div.' + parentDivId + '_All_Subs');
    var srcGeneEvSummary = respData.tlEvidenceSummary;

    var srcGeneEvidences = srcGeneEvSummary.tlEvidences;
    var srcGeneSumPanel = "";
    if (srcGeneEvidences != null) {
        $.each(srcGeneEvidences, function (key, tls) {
            var tlColorLevel = tls.colorLevel;
            var colorClass = 'tl1_color';
            if (tlColorLevel == '4') {
                colorClass = 'tl4_color';
            }
            if (tlColorLevel == '3') {
                colorClass = 'tl3_color';
            }
            if (tlColorLevel == '2') {
                colorClass = 'tl2_color';
            }
            if (tlColorLevel == '1') {
                colorClass = 'tl1_color';
            }

            var isLastLevel = tls.lastLevel;
            var dbs = tls.dbSource;
            var dataType = tls.tpbDataType;
            var identifiedId = dbs + "_" + dataType;

            var tlLevel = tls.tlLevel;
            var dentClss = 'tlevel_1';

            if (tlLevel == 2) {
                dentClss = 'tlevel_2';
            }
            if (tlLevel == 3) {
                dentClss = 'tlevel_3';
            }
            if (tlLevel == 4) {
                dentClss = 'tlevel_4';
            }

            var exitSrcGeneSumPanel = $('div.' + identifiedId);
            if (exitSrcGeneSumPanel != null) {
                exitSrcGeneSumPanel.remove();
            }
            //srcGeneSumPanel += "<div class='blank_separator'></div>";
            srcGeneSumPanel += "<div class='" + identifiedId + "' >" +
                "<table class='tl_sum_tab'>" +
                "<tbody>" +
                "<tr class='tr_normal' onMouseOver=\"this.className='tr_highlight'\" onMouseOut=\"this.className='tr_normal'\">" +
                "<td width='12%'>" + tls.dbSource + "</td>" +
                "<td width='10%'>" + tls.geneName + "</td>" +
                "<td width='13%'>" + tls.ensemblId + "</td>" +
                "<td width='18%'><div class='" + dentClss + "'>" + tls.typeShortName + "</div></td>" +
                "<td width='5%'>" +
                "<div class='" + colorClass + "'>&nbsp;</div>" +
                "</td>" +
                "<td width='37%'>Summary</td>" +
                "<td width='5%'>" +
                "<div class='tl_link'>";
            if (isLastLevel) {
                srcGeneSumPanel += "<a class='view_evidence' id='" + identifiedId + "' href='viewevidences.jspx?tpbDataType=" + tls.tpbDataType + "&geneId=" + tls.geneId + "' >";

            } else {
                srcGeneSumPanel += "<a class='view_src_gene_sum'  id='" + identifiedId + "' href='srcgsum.jspx?dbSource=" + tls.dbSource + "&tpbDataType=" + tls.tpbDataType + "&geneId=" + tls.geneId + "'>";

            }
            srcGeneSumPanel += "<span class='span_expand'>&nbsp;</span></a></div>" +
                "</td>" +
                "</tr>" +
                "</tbody>" +
                "</table>" +
                "<div class='" + identifiedId + "_All_Subs'></div>" +
                "</div>";
        });

    } else {
        srcGeneSumPanel += "<div class='none_evidence'>The evidence is not available</div>"
    }
    parentDiv.html(srcGeneSumPanel);
}


$("a.view_evidence").live('click', function (event) {
    event.preventDefault();
    var hrefLink = $(this);
    var viewUrl = hrefLink.attr('href');
    var linkId = hrefLink.attr('id');
    if (requestForCollapse(hrefLink)) {
        collapseSubs(linkId);
        //reset it as expand
        changeLinkAsExpand(hrefLink);
    } else {
        allEvidences(viewUrl, linkId);
        changeLinkAsCollapse(hrefLink);
    }
})

function allEvidences(viewUrl, parentId) {
    $.ajax({
            type:"get",
            url:viewUrl,
            cache:false,
            contentType:'application/json; charset=utf-8',
            dataType:'json',
            success:function (respdata) {
                var ok = respdata.success;
                if (ok == 'true') {
                    allEvidencesSuccess(respdata, parentId);
                } else {
                    showErrorDialog(respdata.message);
                }
            },
            error:function (request, exception) {
                var errormsg = getErrorMsg(request, exception);
                showErrorDialog(errormsg);
            }
        }
    )
}

function allEvidencesSuccess(respData, parentDivId) {
    var parentDiv = $('div.' + parentDivId + '_All_Subs');
    if (parentDiv != null) {
        parentDiv.html();
    }
    var tlEvSummary = respData.tlEvidenceSummary;
    var primaryDataType = tlEvSummary.primaryDataType;

    var tlEvidences = tlEvSummary.tlEvidences;
    var allEvPanel = '';
    if (tlEvidences != null) {

        allEvPanel += "<table class='tl_sum_tab'>" +
            "<tbody>";
        $.each(tlEvidences, function (key, tlEv) {
            var tlColorLevel = tlEv.colorLevel;
            var colorClass = 'tl1_color';
            if (tlColorLevel == '4') {
                colorClass = 'tl4_color';
            }
            if (tlColorLevel == '3') {
                colorClass = 'tl3_color';
            }
            if (tlColorLevel == '2') {
                colorClass = 'tl2_color';
            }
            if (tlColorLevel == '1') {
                colorClass = 'tl1_color';
            }
            //dent for type
            var tlLevel = tlEv.tlLevel;
            var dentClss = 'tlevel_1';

            if (tlLevel == 2) {
                dentClss = 'tlevel_2';
            }
            if (tlLevel == 3) {
                dentClss = 'tlevel_3';
            }
            if (tlLevel == 4) {
                dentClss = 'tlevel_4';
            }

            allEvPanel += "<tr class='tr_normal' onMouseOver=\"this.className='tr_highlight'\" onMouseOut=\"this.className='tr_normal'\">";
            allEvPanel += "<td width='12%'>" + tlEv.dbSource + "</th>";
            allEvPanel += "<td width='10%'>" + tlEv.geneName + "</td>";
            allEvPanel += "<td width='13%'>" + tlEv.identifiedAccession + "</td>";
            allEvPanel += "<td width='18%'><div class='" + dentClss + "'>" + tlEv.typeShortName + "</div></td>";
            allEvPanel += "<td width='5%'>";
            allEvPanel += "<div class='" + colorClass + "'>&nbsp;</div>";
            allEvPanel += "</td>";
            var ev = "";
            if (primaryDataType == "PE") {
                ev = tlEv.evidence;
            } else if (primaryDataType == "PTM") {
                var pos = tlEv.pos;
                var obs = tlEv.evidence;

                if (pos != 0 && obs != "0") {
                    ev = "Observed " + obs + " times at position " + pos;
                } else {
                    ev = "No sites observed 5 or more times";
                }
            } else {
                var dataType = tlEv.tpbDataType;
                if (dataType == "TE_MA_PROP") {
                    var tissueName = tlEv.tissue;
                    var expValue = tlEv.expression;
                    var uniqueGene = tlEv.uniqueGene;
                    ev = "Probe ID: " + tlEv.identifiedAccession + " detected in " + expValue + " samples of " + tissueName;
                    if (!uniqueGene) {
                        ev += ". NB Probe not unique to gene."
                    }
                } else {
                    ev = tlEv.evidence;
                }

            }
            allEvPanel += "<td width='37%'>" + ev + "</td>";
            allEvPanel += "<td width='5%'>";
            allEvPanel += "<div class='tl_link'><a href='" + tlEv.hyperLink + "' target='_blank' class='evidence_source'><span class='span_grey'>&nbsp;</span></a></div>";
            allEvPanel += "</td>";
            allEvPanel += "</tr>";
        });
        allEvPanel += "</tbody></table>";
    } else {
        allEvPanel += "<div class='none_evidence'>The evidence is not available</div>";
    }
    parentDiv.html(allEvPanel);
}


//view evidence source
$("a.evidence_source").live('click', function (event) {
    event.preventDefault();
    var hrefLink = $(this);
    var viewUrl = hrefLink.attr('href');

    //open new tab
    window.open(viewUrl, '_blank');
    //change out link background
    changeOutLinkBackground(hrefLink);
})

function changeOutLinkBackground(hrefLink) {
    var foundSpan = $('a span#src_span');
    if (foundSpan != 'undefined') {
        foundSpan.attr('id', '');
        foundSpan.attr('class', 'span_grey');
    }
    //set the background for current span
    var spanEL = hrefLink.find('span');
    spanEL.attr('class', 'span_blue');
    spanEL.attr('id', 'src_span');
}

$("div.popup_msg").live('click', function (event) {
    event.preventDefault();
    var popUpDiv = $(this);
    var type = popUpDiv.attr("id");
    var title = "Popup";
    var htmlMessage = "<div class='message'><br/>Popup Message<br/><br/></div> ";

    //not get the type, don't show message
    if ((type == null) || (type == 'undefined')) {
        return;
    }

    if (type == 'tl_pe_type') {
        title = "Protein Expression";
        htmlMessage = "<div class='message'>" +
            "<div class='md_title'>Protein expression</div>" +
            "<div class='md_desc'>Evidence for protein expression from MS- or antibody-based methods, or curated annotation.  It is a summary of numerous underlying data types</div>" +
            "<div class='md_field_row'>" +
            "<div class='md_field_l4_title'>Green</div>" +
            "<div class='md_field_value'>Strong evidence for expression at the protein level</div>" +
            "</div>" +
            "<div class='md_field_row'>" +
            "<div class='md_field_l3_title'>Yellow</div>" +
            "<div class='md_field_value'>Probable expression at the protein level</div>" +
            "</div>" +
            "<div class='md_field_row'>" +
            "<div class='md_field_l2_title'>Red</div>" +
            "<div class='md_field_value'>Dubious evidence for protein level expression, or strong evidence of transcript expression</div>" +
            "</div>" +
            "<div class='md_field_row'>" +
            "<div class='md_field_l1_title'>Black</div>" +
            "<div class='md_field_value'>No evidence for protein expression is available</div>" +
            "</div>" +
            "</div>";
    }
    if (type == 'tl_pe_ms_type') {
        title = "Protein Expression by Mass Spectrometry";
        htmlMessage = "<div class='message'>" +
            "<div class='md_title'>Protein expression by mass spectrometry</div>" +
            "<div class='md_desc'>Direct MS-based evidence for protein expression.  It is a summary of several underlying data types</div>" +
            "<div class='md_field_row'>" +
            "<div class='md_field_l4_title'>Green</div>" +
            "<div class='md_field_value'>Strong evidence for expression at the protein level</div>" +
            "</div>" +
            "<div class='md_field_row'>" +
            "<div class='md_field_l3_title'>Yellow</div>" +
            "<div class='md_field_value'>Probable expression at the protein level</div>" +
            "</div>" +
            "<div class='md_field_row'>" +
            "<div class='md_field_l2_title'>Red</div>" +
            "<div class='md_field_value'>Dubious evidence for protein level expression</div>" +
            "</div>" +
            "<div class='md_field_row'>" +
            "<div class='md_field_l1_title'>Black</div>" +
            "<div class='md_field_value'>No evidence for protein expression is available</div>" +
            "</div>" +
            "</div>";
    }
    if (type == 'tl_pe_ms_ann_type') {
        title = "Annotation of protein expression by Mass Spectrometry"
        htmlMessage = "<div class='message'>" +
            "<div class='md_title'>Annotation of protein expression by Mass Spectrometry</div>" +
            "<div class='md_desc'>Annotated, indirect evidence for MS-based detection of protein expression</div>" +
            "<div class='md_field_row'>" +
            "<div class='md_field_l4_title'>Green</div>" +
            "<div class='md_field_value'>Not applicable</div>" +
            "</div>" +
            "<div class='md_field_row'>" +
            "<div class='md_field_l3_title'>Yellow</div>" +
            "<div class='md_field_value'>Annotation of detection in Peptide Atlas</div>" +
            "</div>" +
            "<div class='md_field_row'>" +
            "<div class='md_field_l2_title'>Red</div>" +
            "<div class='md_field_value'>Annotation of detection in PRIDE</div>" +
            "</div>" +
            "<div class='md_field_row'>" +
            "<div class='md_field_l1_title'>Black</div>" +
            "<div class='md_field_value'>No annotation of MS evidence is available</div>" +
            "</div>" +
            "</div>";
    }
    if (type == 'tl_pe_ms_prob_type') {
        title = "Probability-based MS detection of protein expression"
        htmlMessage = "<div class='message'>" +
            "<div class='md_title'>Probability-based MS detection of protein expression</div>" +
            "<div class='md_desc'>Evidence for protein expression by MS, based upon the highest probability in a single analysis</div>" +
            "<div class='md_field_row'>" +
            "<div class='md_field_l4_title'>Green</div>" +
            "<div class='md_field_value'>log(e)&lt-10 (GPM)</div>" +
            "</div>" +
            "<div class='md_field_row'>" +
            "<div class='md_field_l3_title'>Yellow</div>" +
            "<div class='md_field_value'>-10&ltlog(e)&lt-3 (GPM)</div>" +
            "</div>" +
            "<div class='md_field_row'>" +
            "<div class='md_field_l2_title'>Red</div>" +
            "<div class='md_field_value'>-3&ltlog(e)&lt-1 (GPM)</div>" +
            "</div>" +
            "<div class='md_field_row'>" +
            "<div class='md_field_l1_title'>Black</div>" +
            "<div class='md_field_value'>-1&ltlog(e) (GPM) or no probability-based MS evidence is available</div>" +
            "</div>" +
            "</div>";
    }
    if (type == 'tl_pe_ms_sam_type') {
        title = "Frequency of MS detection";
        htmlMessage = "<div class='message'>" +
            "<div class='md_title'>Frequency of MS detection</div>" +
            "<div class='md_desc'>Repeated detection of protein expression by MS</div>" +
            "<div class='md_field_row'>" +
            "<div class='md_field_l4_title'>Green</div>" +
            "<div class='md_field_value'>Detected in 100 or more samples (GPM)</div>" +
            "</div>" +
            "<div class='md_field_row'>" +
            "<div class='md_field_l3_title'>Yellow</div>" +
            "<div class='md_field_value'>Detected in 20 to 100 samples (GPM)</div>" +
            "</div>" +
            "<div class='md_field_row'>" +
            "<div class='md_field_l2_title'>Red</div>" +
            "<div class='md_field_value'>Detected in under 20 samples</div>" +
            "</div>" +
            "<div class='md_field_row'>" +
            "<div class='md_field_l1_title'>Black</div>" +
            "<div class='md_field_value'>Not detected in any samples</div>" +
            "</div>" +
            "</div>";
    }
    if (type == 'tl_pe_anti_type') {
        title = "Protein expression by antibody technologies";
        htmlMessage = "<div class='message'>" +
            "<div class='md_title'>Protein expression by antibody technologies</div>" +
            "<div class='md_desc'>Antibody-based evidence for protein expression.  It is a summary of several underlying data types</div>" +
            "<div class='md_field_row'>" +
            "<div class='md_field_l4_title'>Green</div>" +
            "<div class='md_field_value'>Strong evidence for expression at the protein level</div>" +
            "</div>" +
            "<div class='md_field_row'>" +
            "<div class='md_field_l3_title'>Yellow</div>" +
            "<div class='md_field_value'>Probable expression at the protein level</div>" +
            "</div>" +
            "<div class='md_field_row'>" +
            "<div class='md_field_l2_title'>Red</div>" +
            "<div class='md_field_value'>Dubious evidence for protein level expression</div>" +
            "</div>" +
            "<div class='md_field_row'>" +
            "<div class='md_field_l1_title'>Black</div>" +
            "<div class='md_field_value'>No evidence for protein expression is available</div>" +
            "</div>" +
            "</div>";
    }
    if (type == 'tl_pe_anti_ann_type') {
        title = "Annotation of antibodies";
        htmlMessage = "<div class='message'>" +
            "<div class='md_title'>Annotation of antibodies</div>" +
            "<div class='md_desc'>Annotated availability of antibodies in Human Protein Atlas</div>" +
            "<div class='md_field_row'>" +
            "<div class='md_field_l4_title'>Green</div>" +
            "<div class='md_field_value'>Not applicable</div>" +
            "</div>" +
            "<div class='md_field_row'>" +
            "<div class='md_field_l3_title'>Yellow</div>" +
            "<div class='md_field_value'>Multiple antibodies are available</div>" +
            "</div>" +
            "<div class='md_field_row'>" +
            "<div class='md_field_l2_title'>Red</div>" +
            "<div class='md_field_value'>A single antibody is available</div>" +
            "</div>" +
            "<div class='md_field_row'>" +
            "<div class='md_field_l1_title'>Black</div>" +
            "<div class='md_field_value'>No antibodies are available</div>" +
            "</div>" +
            "</div>";
    }

    if (type == 'tl_pe_anti_ihc_type') {
        title = "Immunohistochemical detection of protein expression";
        htmlMessage = "<div class='message'>" +
            "<div class='md_title'>Immunohistochemical detection of protein expression</div>" +
            "<div class='md_desc'>Immunohistochemical evidence for protein expression (from Human Protein Atlas)</div>" +
            "<div class='md_field_row'>" +
            "<div class='md_field_l4_title'>Green</div>" +
            "<div class='md_field_value'>Strong evidence for protein expression by IHC. Eg, a high <a href='http://www.proteinatlas.org/about/quality+scoring#re' target='_blank'>Reliability score</a> from HPA.</div>" +
            "<div class='md_field_row'>" +
            "<div class='md_field_l3_title'>Yellow</div>" +
            "<div class='md_field_value'>Probable evidence for protein expression by IHC. Eg, a medium <a href='http://www.proteinatlas.org/about/quality+scoring#re' target='_blank'>Reliability score</a>, or Supportive <a href='http://www.proteinatlas.org/about/quality+scoring#va' target='_blank'>Validation score</a> according to HPA.</div>" +
            "</div>" +
            "<div class='md_field_row'>" +
            "<div class='md_field_l2_title'>Red</div>" +
            "<div class='md_field_value'>Poor or dubious evidence for protein expression by IHC. Eg, a Low or Very Low <a href='http://www.proteinatlas.org/about/quality+scoring#re' target='_blank'>Reliability score</a> or Uncertain or Non-Supportive <a href='http://www.proteinatlas.org/about/quality+scoring#va' target='_blank'>Validation score</a> (according to HPA).</div>" +
            "</div>" +
            "<div class='md_field_row'>" +
            "<div class='md_field_l1_title'>Black</div>" +
            "<div class='md_field_value'>No antibodies are available, or no detected staining</div>" +
            "</div>" +
            "</div>";
    }

    if (type == 'tl_pe_anti_ihc_norm_type') {
        title = "Immunohistochemical detection in normal tissues";
        htmlMessage = "<div class='message'>" +
            "<div class='md_title'>Immunohistochemical detection in normal tissues</div>" +
            "<div class='md_desc'>Immunohistochemical evidence for protein expression in normal tissues (from Human Protein Atlas).</div>" +
            "<div class='md_field_row'>" +
            "<div class='md_field_l4_title'>Green</div>" +
            "<div class='md_field_value'>High Reliability score (according to HPA).</div>" +
            "</div>" +
            "<div class='md_field_row'>" +
            "<div class='md_field_l3_title'>Yellow</div>" +
            "<div class='md_field_value'>Medium Reliability score, or supportive Validation score (according to HPA).</div>" +
            "</div>" +
            "<div class='md_field_row'>" +
            "<div class='md_field_l2_title'>Red</div>" +
            "<div class='md_field_value'>Low or very low Reliability score, or uncertain or non-supportive Validation score (according to HPA).</div>" +
            "</div>" +
            "<div class='md_field_row'>" +
            "<div class='md_field_l1_title'>Black</div>" +
            "<div class='md_field_value'>No antibodies are available, or no detected staining<br/><br/></div>" +
            "</div>" +
            "<div/>" +
            "<div class='md_desc'>More information about <a href='http://www.proteinatlas.org/about/quality+scoring' target='_blank'>HPA Reliability and Validation scores</a>.</div>"
        "</div>";
    }

    if (type == 'tl_pe_oth_type') {
        title = "Other evidence for protein expression";
        htmlMessage = "<div class='message'>" +
            "<div class='md_title'>Other evidence for protein expression</div>" +
            "<div class='md_desc'>Any non MS- or antibody-based evidence for protein expression</div>" +
            "<div class='md_field_row'>" +
            "<div class='md_field_l4_title'>Green</div>" +
            "<div class='md_field_value'>Strong evidence for expression at the protein level</div>" +
            "</div>" +
            "<div class='md_field_row'>" +
            "<div class='md_field_l3_title'>Yellow</div>" +
            "<div class='md_field_value'>Probable expression at the protein level</div>" +
            "</div>" +
            "<div class='md_field_row'>" +
            "<div class='md_field_l2_title'>Red</div>" +
            "<div class='md_field_value'>Dubious evidence for protein level expression</div>" +
            "</div>" +
            "<div class='md_field_row'>" +
            "<div class='md_field_l1_title'>Black</div>" +
            "<div class='md_field_value'>No evidence for protein expression is available</div>" +
            "</div>" +
            "</div>";
    }
    if (type == 'tl_pe_oth_cur_type') {
        title = "Curated annotation of protein expression";
        htmlMessage = "<div class='message'>" +
            "<div class='md_title'>Curated annotation of protein expression</div>" +
            "<div class='md_desc'></div>" +
            "<div class='md_field_row'>" +
            "<div class='md_field_l4_title'>Green</div>" +
            "<div class='md_field_value'>Direct annotation of protein expression</div>" +
            "</div>" +
            "<div class='md_field_row'>" +
            "<div class='md_field_l3_title'>Yellow</div>" +
            "<div class='md_field_value'>Not applicable</div>" +
            "</div>" +
            "<div class='md_field_row'>" +
            "<div class='md_field_l2_title'>Red</div>" +
            "<div class='md_field_value'>Not applicable</div>" +
            "</div>" +
            "<div class='md_field_row'>" +
            "<div class='md_field_l1_title'>Black</div>" +
            "<div class='md_field_value'>No evidence for protein expression is available</div>" +
            "</div>" +
            "</div>";
    }

    if (type == 'tl_ptm_type') {
        title = "Post-translational Modifications";
        htmlMessage = "<div class='message'>" +
            "<div class='md_title'>Post-translational Modification</div>" +
            "<div class='md_desc'>Evidence for post-translational modification of at least one gene product</div>" +
            "<div class='md_field_row'>" +
            "<div class='md_field_l4_title'>Green</div>" +
            "<div class='md_field_value'>A protein product is post-translationally modified</div>" +
            "</div>" +
            "<div class='md_field_row'>" +
            "<div class='md_field_l3_title'>Yellow</div>" +
            "<div class='md_field_value'>A protein product is probably post-translationally modified</div>" +
            "</div>" +
            "<div class='md_field_row'>" +
            "<div class='md_field_l2_title'>Red</div>" +
            "<div class='md_field_value'>Poor/weak evidence for post-translationally modification of a protein product</div>" +
            "</div>" +
            "<div class='md_field_row'>" +
            "<div class='md_field_l1_title'>Black</div>" +
            "<div class='md_field_value'>No evidence for post-translational modification is available</div>" +
            "</div>" +
            "</div>";
    }

    if (type == 'tl_ptm_phs_type') {
        title = "Phosphorylation";
        htmlMessage = "<div class='message'>" +
            "<div class='md_title'>Phosphorylation</div>" +
            "<div class='md_desc'>Evidence for phosphorylation of at least one gene product</div>" +
            "<div class='md_field_row'>" +
            "<div class='md_field_l4_title'>Green</div>" +
            "<div class='md_field_value'>A protein product is phosphorylated</div>" +
            "</div>" +
            "<div class='md_field_row'>" +
            "<div class='md_field_l3_title'>Yellow</div>" +
            "<div class='md_field_value'>A protein product is probably phosphorylated</div>" +
            "</div>" +
            "<div class='md_field_row'>" +
            "<div class='md_field_l2_title'>Red</div>" +
            "<div class='md_field_value'>Poor/weak evidence for phosphorylation of a protein product</div>" +
            "</div>" +
            "<div class='md_field_row'>" +
            "<div class='md_field_l1_title'>Black</div>" +
            "<div class='md_field_value'>No evidence for phosphorylation is available</div>" +
            "</div>" +
            "</div>";
    }

    if (type == 'tl_ptm_phs_ser_type') {
        title = "Phosphoserine";
        htmlMessage = "<div class='message'>" +
            "<div class='md_title'>Phosphoserine</div>" +
            "<div class='md_desc'>Evidence for phosphorylation on a serine residue of at least one gene product</div>" +
            "<div class='md_field_row'>" +
            "<div class='md_field_l4_title'>Green</div>" +
            "<div class='md_field_value'>A protein product is phosphorylated on a serine residue</div>" +
            "</div>" +
            "<div class='md_field_row'>" +
            "<div class='md_field_l3_title'>Yellow</div>" +
            "<div class='md_field_value'>A protein product is probably phosphorylated on a serine residue</div>" +
            "</div>" +
            "<div class='md_field_row'>" +
            "<div class='md_field_l2_title'>Red</div>" +
            "<div class='md_field_value'>Poor/weak evidence for serine phosphorylation of a protein product</div>" +
            "</div>" +
            "<div class='md_field_row'>" +
            "<div class='md_field_l1_title'>Black</div>" +
            "<div class='md_field_value'>No evidence for serine phosphorylation is available</div>" +
            "</div>" +
            "</div>";
    }

    if (type == 'tl_ptm_phs_thr_type') {
        title = "Phosphothreonine";
        htmlMessage = "<div class='message'>" +
            "<div class='md_title'>Phosphothreonine</div>" +
            "<div class='md_desc'>Evidence for phosphorylation on a threonine residue of at least one gene product</div>" +
            "<div class='md_field_row'>" +
            "<div class='md_field_l4_title'>Green</div>" +
            "<div class='md_field_value'>A protein product is phosphorylated on a threonine residue</div>" +
            "</div>" +
            "<div class='md_field_row'>" +
            "<div class='md_field_l3_title'>Yellow</div>" +
            "<div class='md_field_value'>A protein product is probably phosphorylated on a threonine residue</div>" +
            "</div>" +
            "<div class='md_field_row'>" +
            "<div class='md_field_l2_title'>Red</div>" +
            "<div class='md_field_value'>Poor/weak evidence for threonine phosphorylation of a protein product</div>" +
            "</div>" +
            "<div class='md_field_row'>" +
            "<div class='md_field_l1_title'>Black</div>" +
            "<div class='md_field_value'>No evidence for threonine phosphorylation is available</div>" +
            "</div>" +
            "</div>";
    }

    if (type == 'tl_ptm_phs_tyr_type') {
        title = "Phosphotyrosine";
        htmlMessage = "<div class='message'>" +
            "<div class='md_title'>Phosphotyrosine</div>" +
            "<div class='md_desc'>Evidence for phosphorylation on a tyrosine residue of at least one gene product</div>" +
            "<div class='md_field_row'>" +
            "<div class='md_field_l4_title'>Green</div>" +
            "<div class='md_field_value'>A protein product is phosphorylated on a tyrosine residue</div>" +
            "</div>" +
            "<div class='md_field_row'>" +
            "<div class='md_field_l3_title'>Yellow</div>" +
            "<div class='md_field_value'>A protein product is probably phosphorylated on a tyrosine residue</div>" +
            "</div>" +
            "<div class='md_field_row'>" +
            "<div class='md_field_l2_title'>Red</div>" +
            "<div class='md_field_value'>Poor/weak evidence for tyrosine phosphorylation of a protein product</div>" +
            "</div>" +
            "<div class='md_field_row'>" +
            "<div class='md_field_l1_title'>Black</div>" +
            "<div class='md_field_value'>No evidence for tyrosine phosphorylation is available</div>" +
            "</div>" +
            "</div>";
    }


    if (type == 'tl_ptm_ace_type') {
        title = "Acetylation";
        htmlMessage = "<div class='message'>" +
            "<div class='md_title'>Acetylation</div>" +
            "<div class='md_desc'>Evidence for acetylation of at least one gene product</div>" +
            "<div class='md_field_row'>" +
            "<div class='md_field_l4_title'>Green</div>" +
            "<div class='md_field_value'>A protein product is acetylated</div>" +
            "</div>" +
            "<div class='md_field_row'>" +
            "<div class='md_field_l3_title'>Yellow</div>" +
            "<div class='md_field_value'>A protein product is probably acetylated</div>" +
            "</div>" +
            "<div class='md_field_row'>" +
            "<div class='md_field_l2_title'>Red</div>" +
            "<div class='md_field_value'>Poor/weak evidence for acetylation of a protein product</div>" +
            "</div>" +
            "<div class='md_field_row'>" +
            "<div class='md_field_l1_title'>Black</div>" +
            "<div class='md_field_value'>No evidence for acetylylation is available</div>" +
            "</div>" +
            "</div>";
    }

    if (type == 'tl_ptm_ace_lys_type') {
        title = "Lysine Acetylation";
        htmlMessage = "<div class='message'>" +
            "<div class='md_title'>Lysine Acetylation</div>" +
            "<div class='md_desc'>Evidence for acetylation on a lysine residue of at least one gene product</div>" +
            "<div class='md_field_row'>" +
            "<div class='md_field_l4_title'>Green</div>" +
            "<div class='md_field_value'>A protein product is acetylated on a lysine residue</div>" +
            "</div>" +
            "<div class='md_field_row'>" +
            "<div class='md_field_l3_title'>Yellow</div>" +
            "<div class='md_field_value'>A protein product is probably acetylated on a lysine residue</div>" +
            "</div>" +
            "<div class='md_field_row'>" +
            "<div class='md_field_l2_title'>Red</div>" +
            "<div class='md_field_value'>Poor/weak evidence for lysine acetylation of a protein product</div>" +
            "</div>" +
            "<div class='md_field_row'>" +
            "<div class='md_field_l1_title'>Black</div>" +
            "<div class='md_field_value'>No evidence for lysine acetylation is available</div>" +
            "</div>" +
            "</div>";
    }

    if (type == 'tl_ptm_ace_nta_type') {
        title = "N-terminal Acetylation";
        htmlMessage = "<div class='message'>" +
            "<div class='md_title'>N-terminal Acetylation</div>" +
            "<div class='md_desc'>Evidence for acetylation on the N-terminus of at least one gene product</div>" +
            "<div class='md_field_row'>" +
            "<div class='md_field_l4_title'>Green</div>" +
            "<div class='md_field_value'>A protein product is acetylated on the N-terminus</div>" +
            "</div>" +
            "<div class='md_field_row'>" +
            "<div class='md_field_l3_title'>Yellow</div>" +
            "<div class='md_field_value'>A protein product is probably acetylated on the N-terminus</div>" +
            "</div>" +
            "<div class='md_field_row'>" +
            "<div class='md_field_l2_title'>Red</div>" +
            "<div class='md_field_value'>Poor/weak evidence for N-terminal acetylation of a protein product</div>" +
            "</div>" +
            "<div class='md_field_row'>" +
            "<div class='md_field_l1_title'>Black</div>" +
            "<div class='md_field_value'>No evidence for N-terminal acetylation is available</div>" +
            "</div>" +
            "</div>";
    }

    if (type == 'tl_te_type') {
        title = "Transcript Expression";
        htmlMessage = "<div class='message'>" +
            "<div class='md_title'>Transcript Expression</div>" +
            "<div class='md_desc'>Evidence for biological expression of a RNA transcript</div>" +
            "<div class='md_field_row'>" +
            "<div class='md_field_l4_title'>Green</div>" +
            "<div class='md_field_value'>Strong evidence for expression of a RNA transcript specifically from this gene</div>" +
            "</div>" +
            "<div class='md_field_row'>" +
            "<div class='md_field_l3_title'>Yellow</div>" +
            "<div class='md_field_value'>Reasonably consistent evidence for RNA transcript expression, though not necessarily unique to this gene</div>" +
            "</div>" +
            "<div class='md_field_row'>" +
            "<div class='md_field_l2_title'>Red</div>" +
            "<div class='md_field_value'>Poor or inconsistent evidence for RNA transcript expression from this gene</div>" +
            "</div>" +
            "<div class='md_field_row'>" +
            "<div class='md_field_l1_title'>Black</div>" +
            "<div class='md_field_value'>No reasonable evidence for RNA expression from this gene is available</div>" +
            "</div>" +
            "</div>";
    }

    if (type == 'tl_te_ma_type') {
        title = "MicroArray Transcript Expression";
        htmlMessage = "<div class='message'>" +
            "<div class='md_title'>MicroArray Transcript Expression</div>" +
            "<div class='md_desc'>Evidence for RNA transcript expression based on microarray technology.</div>" +
            "<div class='md_field_row'>" +
            "<div class='md_field_l4_title'>Green</div>" +
            "<div class='md_field_value'>Strong evidence for consistent expression of a RNA transcript specifically from this gene</div>" +
            "</div>" +
            "<div class='md_field_row'>" +
            "<div class='md_field_l3_title'>Yellow</div>" +
            "<div class='md_field_value'>Reasonably consistent evidence for RNA transcript expression, though not necessarily unique to this gene</div>" +
            "</div>" +
            "<div class='md_field_row'>" +
            "<div class='md_field_l2_title'>Red</div>" +
            "<div class='md_field_value'>Poor or inconsistent evidence (at best) for transcript expression based on microarray data </div>" +
            "</div>" +
            "<div class='md_field_row'>" +
            "<div class='md_field_l1_title'>Black</div>" +
            "<div class='md_field_value'>No evidence for transcript expression is available based on microarray data</div>" +
            "</div>" +
            "</div>";
    }

    if (type == 'tl_te_ma_prop_type') {
        title = "MicroArray Sample Proportion";
        htmlMessage = "<div class='message'>" +
            "<div class='md_title'>MicroArray Sample Proportion</div>" +
            "<div class='md_desc'>Microarray-based, tissue-specific evidence for transcript expression from the Gene Expression Barcode.  Based on the proportion of samples from a tissue that demonstrate detection of RNA expression</div>" +
            "<div class='md_field_row'>" +
            "<div class='md_field_l4_title'>Green</div>" +
            "<div class='md_field_value'>Detection of a probe in over 80% of samples from a particular tissue.  The probe must be unique to the gene.</div>" +
            "</div>" +
            "<div class='md_field_row'>" +
            "<div class='md_field_l3_title'>Yellow</div>" +
            "<div class='md_field_value'>Detection of a RNA probe in at least 50% of samples from a particular tissue</div>" +
            "</div>" +
            "<div class='md_field_row'>" +
            "<div class='md_field_l2_title'>Red</div>" +
            "<div class='md_field_value'>Detection of a RNA probe in at least one sample from a particular tissue. </div>" +
            "</div>" +
            "<div class='md_field_row'>" +
            "<div class='md_field_l1_title'>Black</div>" +
            "<div class='md_field_value'>No evidence for RNA expression is available<br/><br/></div>" +
            "</div>" +
            "<div class='md_desc'>More information about <a href='http://barcode.luhs.org' target='_blank'>Gene Expression Barcode</a>.</div>" +
            "</div>";
    }
    if (type == 'tl_te_oth_type') {
        title = "Other Transcript Expression";
        htmlMessage = "<div class='message'>" +
            "<div class='md_title'>Other Transcript Expression</div>" +
            "<div class='md_desc'>Evidence for RNA transcript expression from other sources.</div>" +
            "<div class='md_field_row'>" +
            "<div class='md_field_l4_title'>Green</div>" +
            "<div class='md_field_value'>Strong evidence for expression of a RNA transcript specifically from this gene</div>" +
            "</div>" +
            "<div class='md_field_row'>" +
            "<div class='md_field_l3_title'>Yellow</div>" +
            "<div class='md_field_value'>Not applicable</div>" +
            "</div>" +
            "<div class='md_field_row'>" +
            "<div class='md_field_l2_title'>Red</div>" +
            "<div class='md_field_value'>Not applicable</div>" +
            "</div>" +
            "<div class='md_field_row'>" +
            "<div class='md_field_l1_title'>Black</div>" +
            "<div class='md_field_value'>No evidence for transcript expression is available</div>" +
            "</div>" +
            "</div>";
    }

    if (type == 'tl_te_oth_cur_type') {
        title = "Curated annotation of Transcript Expression";
        htmlMessage = "<div class='message'>" +
            "<div class='md_title'>Curated annotation of Transcript Expression</div>" +
            "<div class='md_desc'>Curated annotation of transcript expression.  Based on annotation from neXtProt.</div>" +
            "<div class='md_field_row'>" +
            "<div class='md_field_l4_title'>Green</div>" +
            "<div class='md_field_value'>Annotation of transcript or protein expression evidence.</div>" +
            "</div>" +
            "<div class='md_field_row'>" +
            "<div class='md_field_l3_title'>Yellow</div>" +
            "<div class='md_field_value'>Not applicable.</div>" +
            "</div>" +
            "<div class='md_field_row'>" +
            "<div class='md_field_l2_title'>Red</div>" +
            "<div class='md_field_value'>Not applicable.</div>" +
            "</div>" +
            "<div class='md_field_row'>" +
            "<div class='md_field_l1_title'>Black</div>" +
            "<div class='md_field_value'>No evidence for RNA expression is available.</div>" +
            "</div>" +
            "</div>";
    }

    //if title is a default value, we just return. not show the popup
    if (title == 'Popup') {
        return;
    }
    showPopupMessage(popUpDiv, title, htmlMessage);
})

function showPopupMessage(el, popUpTitle, htmlMessage) {
    $(el).mDialog({
        title:popUpTitle,
        content:htmlMessage,
        close_on_body_click:true,
        width:420
    });
    return false;
}

//expand and collapse function
$("div.expand").live('click', function (event) {
    var expand_collapse = $(this);
    var state = expand_collapse.attr('title');
    var idName = expand_collapse.attr('id');
    expand_collapse.attr('class', 'collapse');
    expand_collapse.attr('title', 'collapse');
    expandSubTypes(idName);
})

$("div.collapse").live('click', function (event) {
    var expand_collapse = $(this);
    var state = expand_collapse.attr('title');
    var idName = expand_collapse.attr('id');
    expand_collapse.attr('class', 'expand');
    expand_collapse.attr('title', 'expand');
    collapseSubTypes(idName);
})

$("div.expandall").live('click', function (event) {
    $('tr.pe_ms').each(function () {
        $(this).show();
    });

    $('tr.pe_anti').each(function () {
        $(this).show();
    });

    $('tr.pe_oth').each(function () {
        $(this).show();
    });
    $('tr.pe_ms_ann').each(function () {
        $(this).show();
    });
    $('tr.pe_ms_prob').each(function () {
        $(this).show();
    });
    $('tr.pe_ms_sam').each(function () {
        $(this).show();
    });
    $('tr.pe_anti_ann').each(function () {
        $(this).show();
    });
    $('tr.pe_anti_ihc').each(function () {
        $(this).show();
    });
    $('tr.pe_anti_ihc_norm').each(function () {
        $(this).show();
    });
    $('tr.pe_oth_cur').each(function () {
        $(this).show();
    });
    $('tr.ptm_phs').each(function () {
        $(this).show();
    });
    $('tr.ptm_phs_ser').each(function () {
        $(this).show();
    });
    $('tr.ptm_phs_thr').each(function () {
        $(this).show();
    });
    $('tr.ptm_phs_tyr').each(function () {
        $(this).show();
    });
    $('tr.ptm_ace').each(function () {
        $(this).show();
    });
    $('tr.ptm_ace_lys').each(function () {
        $(this).show();
    });
    $('tr.ptm_ace_nta').each(function () {
        $(this).show();
    });
    $('tr.te_ma').each(function () {
        $(this).show();
    });
    $('tr.te_ma_prop').each(function () {
        $(this).show();
    });
    $('tr.te_oth').each(function () {
        $(this).show();
    });
    $('tr.te_oth_cur').each(function () {
        $(this).show();
    });
    changeExpandCollapseClass('pe', 'collapse', "collapse");
    changeExpandCollapseClass('pe_ms', 'collapse', "collapse");
    changeExpandCollapseClass('pe_anti', 'collapse', "collapse");
    changeExpandCollapseClass('pe_anti_ihc', 'collapse', "collapse");
    changeExpandCollapseClass('pe_oth', 'collapse', "collapse");
    changeExpandCollapseClass('ptm', 'collapse', "collapse");
    changeExpandCollapseClass('ptm_phs', 'collapse', "collapse");
    changeExpandCollapseClass('ptm_ace', 'collapse', "collapse");
    changeExpandCollapseClass('te', 'collapse', "collapse");
    changeExpandCollapseClass('te_ma', 'collapse', "collapse");
    changeExpandCollapseClass('te_oth', 'collapse', "collapse");
})

$("div.collapseall").live('click', function (event) {
    collapseSubTypes("pe");
    collapseSubTypes("ptm");
    collapseSubTypes("te");
})

function changeExpandCollapseClass(idName, className, titleVale) {
    var clickDiv = $('div#' + idName);
    clickDiv.attr('class', className);
    clickDiv.attr('title', titleVale);
}

function expandSubTypes(idName) {
    if (idName == 'pe') {
        $('tr.pe_ms').each(function () {
            $(this).show();
        });

        $('tr.pe_anti').each(function () {
            $(this).show();
        });

        $('tr.pe_oth').each(function () {
            $(this).show();
        });
    }
    if (idName == 'pe_ms') {
        $('tr.pe_ms_ann').each(function () {
            $(this).show();
        });
        $('tr.pe_ms_prob').each(function () {
            $(this).show();
        });
        $('tr.pe_ms_sam').each(function () {
            $(this).show();
        });
    }

    if (idName == 'pe_anti') {
        $('tr.pe_anti_ann').each(function () {
            $(this).show();
        });
        $('tr.pe_anti_ihc').each(function () {
            $(this).show();
        });
    }

    if (idName == 'pe_anti_ihc') {
        $('tr.pe_anti_ihc_norm').each(function () {
            $(this).show();
        });
    }

    if (idName == 'pe_oth') {
        $('tr.pe_oth_cur').each(function () {
            $(this).show();
        });
    }

    if (idName == 'ptm') {
        $('tr.ptm_phs').each(function () {
            $(this).show();
        });
        $('tr.ptm_ace').each(function () {
            $(this).show();
        });
    }

    if (idName == 'ptm_phs') {
        $('tr.ptm_phs_ser').each(function () {
            $(this).show();
        });

        $('tr.ptm_phs_thr').each(function () {
            $(this).show();
        });

        $('tr.ptm_phs_tyr').each(function () {
            $(this).show();
        });
    }

    if (idName == 'ptm_ace') {
        $('tr.ptm_ace_lys').each(function () {
            $(this).show();
        });

        $('tr.ptm_ace_nta').each(function () {
            $(this).show();
        });
    }

    if (idName == 'te') {
        $('tr.te_ma').each(function () {
            $(this).show();
        });
        $('tr.te_oth').each(function () {
            $(this).show();
        });
    }

    if (idName == 'te_ma') {
        $('tr.te_ma_prop').each(function () {
            $(this).show();
        });
    }

    if (idName == 'te_oth') {
        $('tr.te_oth_cur').each(function () {
            $(this).show();
        });
    }

}

function collapseSubTypes(idName) {
    if (idName == 'pe') {
        $('tr.pe_ms').each(function () {
            $(this).hide();
        });

        $('tr.pe_ms_ann').each(function () {
            $(this).hide();
        });

        $('tr.pe_ms_prob').each(function () {
            $(this).hide();
        });

        $('tr.pe_ms_sam').each(function () {
            $(this).hide();
        });

        $('tr.pe_anti').each(function () {
            $(this).hide();
        });

        $('tr.pe_anti_ann').each(function () {
            $(this).hide();
        });

        $('tr.pe_anti_ihc').each(function () {
            $(this).hide();
        });


        $('tr.pe_anti_ihc_norm').each(function () {
            $(this).hide();
        });

        $('tr.pe_oth').each(function () {
            $(this).hide();
        });


        $('tr.pe_oth_cur').each(function () {
            $(this).hide();
        });
        //change the none-end tree node as expandable class attribute
        changeExpandCollapseClass('pe', 'expand', "expand");
        changeExpandCollapseClass('pe_ms', 'expand', "expand");
        changeExpandCollapseClass('pe_anti', 'expand', "expand");
        changeExpandCollapseClass('pe_anti_ihc', 'expand', "expand");
        changeExpandCollapseClass('pe_oth', 'expand', "expand");
    }
    if (idName == 'pe_ms') {
        $('tr.pe_ms_ann').each(function () {
            $(this).hide();
        });
        $('tr.pe_ms_prob').each(function () {
            $(this).hide();
        });
        $('tr.pe_ms_sam').each(function () {
            $(this).hide();
        });
    }

    if (idName == 'pe_anti') {
        $('tr.pe_anti_ann').each(function () {
            $(this).hide();
        });
        $('tr.pe_anti_ihc').each(function () {
            $(this).hide();
        });

        $('tr.pe_anti_ihc_norm').each(function () {
            $(this).hide();
        });
        //change the anti ihc tree node as expandable class attribute
        changeExpandCollapseClass('pe_anti_ihc', 'expand', "expand");
    }

    if (idName == 'pe_anti_ihc') {
        $('tr.pe_anti_ihc_norm').each(function () {
            $(this).hide();
        });
        changeExpandCollapseClass('pe_anti_ihc', 'expand', "expand");
    }

    if (idName == 'pe_oth') {
        $('tr.pe_oth_cur').each(function () {
            $(this).hide();
        });
    }

    if (idName == 'ptm') {
        $('tr.ptm_phs').each(function () {
            $(this).hide();
        });

        $('tr.ptm_phs_ser').each(function () {
            $(this).hide();
        });

        $('tr.ptm_phs_thr').each(function () {
            $(this).hide();
        });

        $('tr.ptm_phs_tyr').each(function () {
            $(this).hide();
        });

        $('tr.ptm_ace').each(function () {
            $(this).hide();
        });

        $('tr.ptm_ace_lys').each(function () {
            $(this).hide();
        });

        $('tr.ptm_ace_nta').each(function () {
            $(this).hide();
        });


        changeExpandCollapseClass('ptm', 'expand', "expand");
        changeExpandCollapseClass('ptm_phs', 'expand', "expand");
        changeExpandCollapseClass('ptm_ace', 'expand', "expand");
    }

    if (idName == 'ptm_phs') {
        $('tr.ptm_phs_ser').each(function () {
            $(this).hide();
        });

        $('tr.ptm_phs_thr').each(function () {
            $(this).hide();
        });

        $('tr.ptm_phs_tyr').each(function () {
            $(this).hide();
        });
        changeExpandCollapseClass('ptm_phs', 'expand', "expand");
    }

    if (idName == 'ptm_ace') {
        $('tr.ptm_ace_lys').each(function () {
            $(this).hide();
        });

        $('tr.ptm_ace_nta').each(function () {
            $(this).hide();
        });

        changeExpandCollapseClass('ptm_ace', 'expand', "expand");
    }


    if (idName == 'te') {
        $('tr.te_ma').each(function () {
            $(this).hide();
        });

        $('tr.te_ma_prop').each(function () {
            $(this).hide();
        });

        $('tr.te_oth').each(function () {
            $(this).hide();
        });

        $('tr.te_oth_cur').each(function () {
            $(this).hide();
        });
        changeExpandCollapseClass('te', 'expand', "expand");
        changeExpandCollapseClass('te_ma', 'expand', "expand");
        changeExpandCollapseClass('te_oth', 'expand', "expand");
    }

    if (idName == 'te_ma') {
        $('tr.te_ma_prop').each(function () {
            $(this).hide();
        });
        changeExpandCollapseClass('te_ma', 'expand', "expand");
    }
    if (idName == 'te_oth') {
        $('tr.te_oth_cur').each(function () {
            $(this).hide();
        });
        changeExpandCollapseClass('te_oth', 'expand', "expand");
    }

    //check if it's visible or not. if not.just reset the sum panel
    checkTLSumVisible();
}

function checkTLSumVisible() {
    var tlSumPanel = $('.tl_ev_summary_div');
    var evSumId = tlSumPanel.attr('id');

    if (evSumId != null && evSumId != 'undefined') {
        var trTypeId = evSumId.substr(0, evSumId.indexOf('_sum'));
        var tlTr = $('tr.' + trTypeId);
        if (tlTr != null) {
            if (tlTr.is(':visible')) {
                //do nothing
            } else {
                //remove the highlight color
                removeHighLightColor();
                //reset TrafficLight evidence summary div
                resetTLEvSumDiv();
            }
        }
    }
}

function resetTLEvSumDiv() {
    var tlEvSumDiv = $('.tl_ev_summary_div');
    tlEvSumDiv.removeAttr('id');
    tlEvSumDiv.html('');
    tlEvSumDiv.hide();
}

$('input:checkbox#selected_dbsource').live('change', function (event) {
    changeTPBVersion();
});
$('#selected_chrom').live('change', function (event) {
    changeTPBVersion();
});

function changeTPBVersion() {
    var nxdbs = $("input[name='tlSearchBean.nxDbSelected']").is(':checked');
    var gpmdbs = $("input[name='tlSearchBean.gpmDbSelected']").is(':checked');
    var hpadbs = $("input[name='tlSearchBean.hpaDbSelected']").is(':checked');
    var bcdbs = $("input[name='tlSearchBean.bcDbSelected']").is(':checked');
    var chromType = $('#selected_chrom').val();
    $.ajax({
        type:"get",
        url:"findTpbVersions.jspx?tlSearchBean.selectedChromType=" + chromType + "&tlSearchBean.nxDbSelected=" + nxdbs + "&tlSearchBean.gpmDbSelected=" + gpmdbs + "&tlSearchBean.hpaDbSelected=" + hpadbs + "&tlSearchBean.bcDbSelected=" + bcdbs,
        cache:false,
        contentType:"application/json; charset=utf-8",
        dataType:"json",
        success:function (respData) {
            //reset the version values
            $("#selected_version").get(0).options.length = 0;
            $.each(respData, function (key, value) {
                $('#selected_version').get(0).options[$('#selected_version').get(0).options.length] = new Option(value, key);
            })
        },
        error:function () {
        }
    })
}

$("#data_type_selected").live('change', function () {
    var selectedTypeIndexValue = $('#data_type_selected').val();
    var selectedTypeText = $('#data_type_selected option:selected').text();
    //alert("selectedTypeText: " + selectedTypeText);
    var tabRowIndex = $("#type_evidence_level_tab > tbody > tr").length;
    var alreadyAddedRow = $("input=[id=selectedDataType][value=" + selectedTypeIndexValue + "]").val();
    if (alreadyAddedRow != null) {
        return;
    }

    if (selectedTypeIndexValue != '-1') {
        $('#type_evidence_level_tab > tbody:last').append("<tr>" +
            "<td width='160px'><input type='hidden' name='tlSearchBean.tlTypeEvLevelFilters[" + tabRowIndex + "].dataTypeDisplayName' id='dataTypeDisplayName' value='" + selectedTypeText + "' /><input type='hidden' name='tlSearchBean.tlTypeEvLevelFilters[" + tabRowIndex + "].dataType' id='selectedDataType' value='" + selectedTypeIndexValue + "' />" + selectedTypeText + "</td>" +
            "<td><div class='tl4_level'>&nbsp;</div></td>" +
            "<td><input type='checkbox' name='tlSearchBean.tlTypeEvLevelFilters[" + tabRowIndex + "].typeEvLevel4' value='true' id='typeEvLevel4' class='check_box_norm'/></td>" +
            "<td width='30px' align='right'><div class='tl3_level'>&nbsp;</div></td>" +
            "<td><input type='checkbox' name='tlSearchBean.tlTypeEvLevelFilters[" + tabRowIndex + "].typeEvLevel3' value='true' id='typeEvLevel3' class='check_box_norm'/></td>" +
            "<td width='30px' align='right'><div class='tl2_level'>&nbsp;</div></td>" +
            "<td><input type='checkbox' name='tlSearchBean.tlTypeEvLevelFilters[" + tabRowIndex + "].typeEvLevel2' value='true' id='typeEvLevel2' class='check_box_norm'/></td>" +
            "<td width='30px' align='right'><div class='tl1_level'>&nbsp;</div></td>" +
            "<td><input type='checkbox' name='tlSearchBean.tlTypeEvLevelFilters[" + tabRowIndex + "].typeEvLevel1' value='true' id='typeEvLevel1' class='check_box_norm'/></td>" +
            "<td width='50px' align='right'><div class='cancel_type_ev_level'>&nbsp;</div></td>" +
            "</tr>");
    }
})

$('.cancel_type_ev_level').live('click', function (event) {
    event.preventDefault();
    var trRowId = $(this).closest('tr');
    var trId = trRowId.attr('id');
    trRowId.remove();
    resortDataTypeLevelTabIndexes();
})

function resortDataTypeLevelTabIndexes() {
    var index = 0;
    $("#type_evidence_level_tab > tbody > tr").each(function () {
        //type display name
        var dataTypeDisplayName = $(this).find('#dataTypeDisplayName');
        dataTypeDisplayName.attr('name', 'tlSearchBean.tlTypeEvLevelFilters[' + index + '].dataTypeDisplayName');
        //type name
        var selectedDataType = $(this).find('#selectedDataType');
        selectedDataType.attr('name', 'tlSearchBean.tlTypeEvLevelFilters[' + index + '].dataType');
        //level 4
        var typeEvlevel4 = $(this).find('#typeEvLevel4');
        typeEvlevel4.attr('name', 'tlSearchBean.tlTypeEvLevelFilters[' + index + '].typeEvLevel4');
        //struts2 hidden check box
        var _typeEvLevel4 = $(this).find('#__checkbox_typeEvLevel4');
        if (_typeEvLevel4 != null && _typeEvLevel4 != 'undefined') {
            _typeEvLevel4.attr('name', '__checkbox_tlSearchBean.tlTypeEvLevelFilters[' + index + '].typeEvLevel4');
        }
        //level 3
        var typeEvlevel3 = $(this).find('#typeEvLevel3');
        typeEvlevel3.attr('name', 'tlSearchBean.tlTypeEvLevelFilters[' + index + '].typeEvLevel3');
        //struts2 hidden check box
        var _typeEvLevel3 = $(this).find('#__checkbox_typeEvLevel3');
        if (_typeEvLevel3 != null && _typeEvLevel3 != 'undefined') {
            _typeEvLevel3.attr('name', '__checkbox_tlSearchBean.tlTypeEvLevelFilters[' + index + '].typeEvLevel3');
        }
        //level2
        var typeEvlevel2 = $(this).find('#typeEvLevel2');
        typeEvlevel2.attr('name', 'tlSearchBean.tlTypeEvLevelFilters[' + index + '].typeEvLevel2');
        //struts2 hidden check box
        var _typeEvLevel2 = $(this).find('#__checkbox_typeEvLevel2');
        if (_typeEvLevel2 != null && _typeEvLevel2 != 'undefined') {
            _typeEvLevel2.attr('name', '__checkbox_tlSearchBean.tlTypeEvLevelFilters[' + index + '].typeEvLevel2');
        }
        //level1
        var typeEvlevel1 = $(this).find('#typeEvLevel1');
        typeEvlevel1.attr('name', 'tlSearchBean.tlTypeEvLevelFilters[' + index + '].typeEvLevel1');
        //struts2 hidden check box
        var _typeEvLevel1 = $(this).find('#__checkbox_typeEvLevel1');
        if (_typeEvLevel1 != null && _typeEvLevel1 != 'undefined') {
            _typeEvLevel1.attr('name', '__checkbox_tlSearchBean.tlTypeEvLevelFilters[' + index + '].typeEvLevel1');
        }
        //increase the index number
        index++;
    });
}

$("a.filter_option_href").live('click', function (event) {
    event.preventDefault();

    var filterOptLink = $(this);
    var idval = filterOptLink.attr('id');
    //check the id value
    if (idval == 'more_options') {
        showAdvancedMode(filterOptLink);
    } else {
        hideAdvancedMode(filterOptLink);
    }
})

function showAdvancedMode(filterOptLink) {
    //change the id value and text
    filterOptLink.attr('id', 'less_options')
    filterOptLink.text("less options")

    var advanceModeDiv = $('div.advanced_filter_mode');
    advanceModeDiv.show();
    setInAdancedModeValue('true');
}

function hideAdvancedMode(filterOptLink) {
    //change the id value and text
    filterOptLink.attr('id', 'more_options')
    filterOptLink.text("more options")

    var advanceModeDiv = $('div.advanced_filter_mode');
    advanceModeDiv.hide();
    setInAdancedModeValue('false');
    resetAllAdvancedModeValues();
}

function setInAdancedModeValue(value) {
    var inputAdvancedMode = $("input[name='inAdvancedMode']");
    inputAdvancedMode.attr('value', value);
}

function resetAllAdvancedModeValues() {
    //remove all type level
    var tabTbody = $("#type_evidence_level_tab > tbody");
    tabTbody.empty();

    //reset the data type select option as default
    $("#data_type_selected option[value='-1']").attr('selected', 'selected');

    //reset the region from value
    var inputRegionFrom = $("input[name='tlSearchBean.regionFrom']");
    inputRegionFrom.attr('value', '0');

    //reset the region to value
    var inputRegionTo = $("input[name='tlSearchBean.regionTo']");
    inputRegionTo.attr('value', '0');

    //reset gene list values
    var inputGeneList = $("textarea[name='tlSearchBean.geneTypeValues']");
    inputGeneList.val('');

    //reset gene list type as default for gene symbol
    $("#tlSearchBeanSelectedGeneValueType option[value='symbol']").attr('selected', 'selected');
}

//numberic only
$(".numberinput").live('keypress', function (event) {
    // Backspace, tab, enter, end, home, left, right
    // We don't support the del key in Opera because del == . == 46.
    var controlKeys = [8, 9, 13, 35, 36, 37, 39];
    // IE doesn't support indexOf
    var isControlKey = controlKeys.join(",").match(new RegExp(event.which));
    // Some browsers just don't raise events for control keys. Easy.
    // e.g. Safari backspace.
    if (!event.which || // Control keys in most browsers. e.g. Firefox tab is 0
        (49 <= event.which && event.which <= 57) || // Always 1 through 9
        (48 == event.which && $(this).attr("value")) || // No 0 first digit
        isControlKey) { // Opera assigns values for control keys.
        return;
    } else {
        event.preventDefault();
    }
});


$(".view_sum_report").live('click', function (event) {
    event.preventDefault();

    var options = {
        // beforeSubmit:  showRequest,
        url:'sumReport.jspx', // override for form's 'action' attribute
        type:'post', // 'get' or 'post', override for form's 'method' attribute
        dataType:'json', // 'xml', 'script', or 'json' (expected server response type)
        success:processReport // post-submit callback
    };

    // inside event callbacks 'this' is the DOM element so we first
    // wrap it in a jQuery object and then invoke ajaxSubmit
    $('#tl_search_form').ajaxSubmit(options);
    // !!! Important !!!
    // always return false to prevent standard browser submit and page navigation
    return false;
});

// pre-submit callback
function showRequest(formData, jqForm, options) {
    // formData is an array; here we use $.param to convert it to a string to display it
    // but the form plugin does this for you automatically when it submits the data
    var queryString = $.param(formData);

    // jqForm is a jQuery object encapsulating the form element.  To access the
    // DOM element for the form do this:
    // var formElement = jqForm[0];

    alert('About to submit: \n\n' + queryString);

    // here we could return false to prevent the form from being submitted;
    // returning anything other than false will allow the form submit to continue
    return true;
}

// post-submit callback
function processReport(data) {
    // for normal html responses, the first argument to the success callback
    // is the XMLHttpRequest object's responseText property

    // if the ajaxSubmit method was passed an Options Object with the dataType
    // property set to 'xml' then the first argument to the success callback
    // is the XMLHttpRequest object's responseXML property

    // if the ajaxSubmit method was passed an Options Object with the dataType
    // property set to 'json' then the first argument to the success callback
    // is the json data object returned by the server
    var tlSumReport = $('.tl_sum_report_div');
    var success = data.succeed;
    var reportHtml = '';
    if (success) {
        reportHtml = createReportPanel(data);
    } else {
        reportHtml = "<div class='sum_report_error'>" + data.msg + "</div>";
    }
    tlSumReport.html(reportHtml);
    tlSumReport.show();
}

function createReportPanel(data) {
    var reportPanel = "<div class='report_title'>The Traffic Light Summary Report For Chromosome " + data.chromosome + "<div class='report_close' title='Close Summary Report'></div></div>";
    reportPanel += "<div class='sum_report_tab_div'>";

    var typeSumReporters = data.tlTypeSumReporters;
    var firstchartParam = data.firstChartParam;
    if (firstchartParam != null) {
        firstchartParam = "../chart/sumchart.jspx?" + firstchartParam;
    }

    if (typeSumReporters != null) {
        reportPanel += "<table class='tl_sum_report_tab'>";
        reportPanel += "<tr><th width='30%'>TPB Data Type</th><th width='15%'><div class='tl4_color'></div></th><th width='15%'><div class='tl3_color'></th><th width='15%'><div class='tl2_color'></th><th width='15%'><div class='tl1_color'></th><th width='5%'>&nbsp;</th></tr>";
        $.each(typeSumReporters, function (key, typeSumRep) {
            if (key == 0) {
                reportPanel += "<tr class='sum_highlight' id='report_tab_row' title='view pie chart diagram'>";
            } else {
                reportPanel += "<tr class='sum_normal' id='report_tab_row' title='view pie chart diagram'>";
            }
            reportPanel += "<td>" + typeSumRep.tpbDataType + "</td>";
            reportPanel += "<td>" + typeSumRep.level4Num + "</td>";
            reportPanel += "<td>" + typeSumRep.level3Num + "</td>";
            reportPanel += "<td>" + typeSumRep.level2Num + "</td>";
            reportPanel += "<td>" + typeSumRep.level1Num + "</td>";
            reportPanel += "<td align='right'>";
            if (key == 0) {
                reportPanel += "<div class='color_pie_chart' id='dt=" + typeSumRep.tpbDataType + "&l4=" + typeSumRep.level4Num + "&l3=" + typeSumRep.level3Num + "&l2=" + typeSumRep.level2Num + "&l1=" + typeSumRep.level1Num + "' >&nbsp;</div>";
            } else {
                reportPanel += "<div class='pie_chart' id='dt=" + typeSumRep.tpbDataType + "&l4=" + typeSumRep.level4Num + "&l3=" + typeSumRep.level3Num + "&l2=" + typeSumRep.level2Num + "&l1=" + typeSumRep.level1Num + "' >&nbsp;</div>";
            }
            reportPanel += "</td>";
            reportPanel += "</tr>";
        });
        reportPanel += "</table>";
        reportPanel +="<div class='blank_separator'></div>";

        reportPanel += "<div align='right'><a href='../tl/exportSR.jspx' title='export the traffic light summary report as a csv file'>export the summary report&nbsp; <img src='../images/download.png'/></a></div>"
    } else {
        reportPanel += "<div>The traffic light summary not available</div>"
    }
    reportPanel += "</div>";
    reportPanel += "<div class='sum_report_char_div'><img src='" + firstchartParam + "' border='0'/></div>";
    reportPanel += "<div style='clear: both;' />";
    return reportPanel;
}

$("div.report_close").live('click', function (event) {
    var tlSumReport = $('.tl_sum_report_div');
    if (tlSumReport != null) {
        tlSumReport.empty();
    }
    tlSumReport.hide();
})

$('.tl_sum_report_tab tr#report_tab_row').live('click', function (event) {
    event.preventDefault();
    var prev_hl_tr = $('.sum_highlight');
    prev_hl_tr.attr('class', 'sum_normal');
    $(this).attr('class', 'sum_highlight');

    //find previous view chart div
    var prevViewChartDiv = $('.color_pie_chart');
    prevViewChartDiv.attr('class', 'pie_chart');
    var pieChartDiv = $(this).find('div');
    pieChartDiv.attr('class', 'color_pie_chart');

    var chartId = pieChartDiv.attr('id');
    var chartUrl = "../chart/sumchart.jspx?" + chartId;
    var reportChartDiv = $('.sum_report_char_div');
    reportChartDiv.empty();
    var charImg = "<img src='" + chartUrl + "' border ='0'  />";
    reportChartDiv.html(charImg);
    return;
});