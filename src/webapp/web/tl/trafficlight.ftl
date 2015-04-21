<#include  "../template/ftl_header.ftl" />
<!DOCTYPE html PUBLIC "-//W3C//DTD XHTML 1.0 Transitional//EN"
        "http://www.w3.org/TR/xhtml1/DTD/xhtml1-transitional.dtd">
<html xmlns="http://www.w3.org/1999/xhtml">
<head>
    <title>TPB Traffic Light</title>
<#include "../template/header_tl_jquery.ftl" />
    <!--[if lte IE 8]>
    <style type="text/css" media="screen">
        .tl_type_title {
            writing-mode: tb-lr;
        }

        .gene_name {
            writing-mode: tb-lr;
            margin-left: -35px;
        }
    </style>
    <![endif]-->
    <!--  Fade out the progress bar if completely loaded the traffic light -->
    <script type="text/javascript">
        $(document).ready(function () {
            $('.progress_bar').fadeOut();
        });
    </script>
</head>
<body>
<div class="blank_separator"></div>
<#include "../template/navigation.ftl" />
<div class="display_main_container">
<!-- sub-nav bar -->
<@s.if test="%{#session.authentication_flag =='authenticated'}">
<div class="subnav_section">
    <#include "../template/user_nav.ftl"/>
</div>
</@s.if>
<!-- end of sub-nav bar -->
<div class="display_middle_div">
<div class="blank_separator"></div>
<div class="display_error_div">
<#include "../template/action_errors.ftl" />
</div>
<!-- page title -->
<div class="page_title">Traffic Light</div>
<!-- inner display area -->
<div class="display_inner_div">
<#include "tlconditions.ftl" />
<@s.if test="%{tlPagination != null}">
    <@s.if test="%{tlPagination.pageResults.size() > 0}">
    <div class="tl_main_panel">
    <div class="tl_data_type">
        <table class="tl_data_type_tab">
            <thead>
            <tr>
                <th>
                    <div class="tl_type_title">
<!--                        <div class="expandall" title="Expand all data types"><img src="${base}/images/expand.png"/><a href="#">Expand All</a></div>-->
<!--                        <div class="collapseall" title="Collapse all data types"><img src="${base}/images/collapse.png"/><a href="#">Collapse All</a></div>-->
                    </div>
                </th>
                <th width="22px">&nbsp;</th>
            </tr>
            </thead>

            <tbody>
            <tr class="pe">
                <td>
                    <div class="popup_msg" id="tl_pe_type" title="Click to View The PE Definition">PE
                        <div class='type_info'>&nbsp;&nbsp;&nbsp;</div>
                    </div>
                </td>
                <td align="center" width="22px">
                    <div class="expand" id="pe" title="expand"></div>
                </td>
            </tr>
            <tr class="pe_ms">
                <td>
                    <div class="popup_msg" id="tl_pe_ms_type" title="Click to View The PE MS Definition">PE MS
                        <div class='type_info'>&nbsp;&nbsp;&nbsp;</div>
                    </div>
                </td>
                <td align="center" width="22px">
                    <div class="expand" id="pe_ms" title="expand"></div>
                </td>
            </tr>
            <tr class="pe_ms_ann">
                <td>

                    <div class="popup_msg" id="tl_pe_ms_ann_type" title="Click to View The PE MS ANN Definition">PE MS ANN
                        <div class='type_info'>&nbsp;&nbsp;&nbsp;</div>
                    </div>
                </td>
                <td align="center" width="22px"></td>
            </tr>
            <tr class="pe_ms_prob">
                <td>
                    <div class="popup_msg" id="tl_pe_ms_prob_type" title="Click to View The PE MS PROB Definition">PE MS PROB
                        <div class='type_info'>&nbsp;&nbsp;&nbsp;</div>
                    </div>
                </td>
                <td align="center" width="22px"></td>
            </tr>
            <tr class="pe_ms_sam">
                <td>
                    <div class="popup_msg" id="tl_pe_ms_sam_type" title="Click to View The PE MS SAM Definition">PE MS SAM
                        <div class='type_info'>&nbsp;&nbsp;&nbsp;</div>
                    </div>
                </td>
                <td align="center" width="22px"></td>
            </tr>
            <tr class="pe_anti">
                <td>
                    <div class="popup_msg" id="tl_pe_anti_type" title="Click to View The PE ANTI Definition">PE ANTI
                        <div class='type_info'>&nbsp;&nbsp;&nbsp;</div>
                    </div>
                </td>
                <td align="center" width="22px">
                    <div class="expand" id="pe_anti" title="expand"></div>
                </td>
            </tr>
            <tr class="pe_anti_ann">
                <td>
                    <div class="popup_msg" id="tl_pe_anti_ann_type" title="Click to View The PE ANTI ANN Definition">PE ANTI ANN
                        <div class='type_info'>&nbsp;&nbsp;&nbsp;</div>
                    </div>
                </td>
                <td align="center" width="22px"></td>
            </tr>
            <tr class="pe_anti_ihc">
                <td>
                    <div class="popup_msg" id="tl_pe_anti_ihc_type" title="Click to View The PE ANTI IHC Definition">PE ANTI IHC
                        <div class='type_info'>&nbsp;&nbsp;&nbsp;</div>
                    </div>
                </td>
                <td align="center" width="22px">
                    <div class="expand" id="pe_anti_ihc" title="expand"></div>
                </td>
            </tr>
            <tr class="pe_anti_ihc_norm">
                <td>
                    <div class="popup_msg" id="tl_pe_anti_ihc_norm_type" title="Click to View The PE ANTI IHC NORM Definition">PE ANTI IHC NORM
                        <div class='type_info'>&nbsp;&nbsp;&nbsp;</div>
                    </div>
                </td>
                <td align="center" width="22px"></td>
            </tr>
            <tr class="pe_oth">
                <td>
                    <div class="popup_msg" id="tl_pe_oth_type" title="Click to View The PE OTH Definition">PE OTH
                        <div class='type_info'>&nbsp;&nbsp;&nbsp;</div>
                    </div>
                </td>
                <td align="center" width="22px">
                    <div class="expand" id="pe_oth" title="expand"></div>
                </td>
            </tr>
            <tr class="pe_oth_cur">
                <td>
                    <div class="popup_msg" id="tl_pe_oth_cur_type" title="Click to View The PE OTH CUR Definition">PE OTH CUR
                        <div class='type_info'>&nbsp;&nbsp;&nbsp;</div>
                    </div>
                </td>
                <td align="center" width="22px"></td>
            </tr>
            <tr>
                <td colspan="2" style="background-color: #FFFFFF"></td>
            </tr>

            <tr class="ptm">
                <td>
                    <div class="popup_msg" id="tl_ptm_type" title="Click to View The PTM Definition">PTM
                        <div class='type_info'>&nbsp;&nbsp;&nbsp;</div>
                    </div>
                </td>
                <td align="center" width="22px">
                    <div class="expand" id="ptm" title="expand"></div>
                </td>
            </tr>
            <tr class="ptm_phs">
                <td>
                    <div class="popup_msg" id="tl_ptm_phs_type" title="Click to View The PTM PHS Definition">PTM PHS
                        <div class='type_info'>&nbsp;&nbsp;&nbsp;</div>
                    </div>
                </td>
                <td align="center" width="22px">
                    <div class="expand" id="ptm_phs" title="expand"></div>
                </td>
            </tr>
            <tr class="ptm_phs_ser">
                <td>
                    <div class="popup_msg" id="tl_ptm_phs_ser_type" title="Click to View The PTM PHS SER Definition">PTM PHS SER
                        <div class='type_info'>&nbsp;&nbsp;&nbsp;</div>
                    </div>
                </td>
                <td align="center" width="22px"></td>
            </tr>
            <tr class="ptm_phs_thr">
                <td>
                    <div class="popup_msg" id="tl_ptm_phs_thr_type" title="Click to View The PTM PHS THR Definition">PTM PHS THR
                        <div class='type_info'>&nbsp;&nbsp;&nbsp;</div>
                    </div>
                </td>
                <td align="center" width="22px"></td>
            </tr>
            <tr class="ptm_phs_tyr">
                <td>
                    <div class="popup_msg" id="tl_ptm_phs_tyr_type" title="Click to View The PTM PHS TYR Definition">PTM PHS TYR
                        <div class='type_info'>&nbsp;&nbsp;&nbsp;</div>
                    </div>
                </td>
                <td align="center" width="22px"></td>
            </tr>

            <tr class="ptm_ace">
                <td>
                    <div class="popup_msg" id="tl_ptm_ace_type" title="Click to View The PTM ACE Definition">PTM ACE
                        <div class='type_info'>&nbsp;&nbsp;&nbsp;</div>
                    </div>
                </td>
                <td align="center" width="22px">
                    <div class="expand" id="ptm_ace" title="expand"></div>
                </td>
            </tr>
            <tr class="ptm_ace_lys">
                <td>
                    <div class="popup_msg" id="tl_ptm_ace_lys_type" title="Click to View The PTM ACE LYS Definition">PTM ACE LYS
                        <div class='type_info'>&nbsp;&nbsp;&nbsp;</div>
                    </div>
                </td>
                <td align="center" width="22px"></td>
            </tr>
            <tr class="ptm_ace_nta">
                <td>
                    <div class="popup_msg" id="tl_ptm_ace_nta_type" title="Click to View The PTM PHS THR Definition">PTM ACE NTA
                        <div class='type_info'>&nbsp;&nbsp;&nbsp;</div>
                    </div>
                </td>
                <td align="center" width="22px"></td>
            </tr>

            <tr>
                <td colspan="2" style="background-color: #FFFFFF"></td>
            </tr>

            <tr class="te">
                <td>
                    <div class="popup_msg" id="tl_te_type" title="Click to View The TE Definition">TE
                        <div class='type_info'>&nbsp;&nbsp;&nbsp;</div>
                    </div>
                </td>
                <td align="center" width="22px">
                    <div class="expand" id="te" title="expand"></div>
                </td>
            </tr>
            <tr class="te_ma">
                <td>
                    <div class="popup_msg" id="tl_te_ma_type" title="Click to View The TE MA Definition">TE MA
                        <div class='type_info'>&nbsp;&nbsp;&nbsp;</div>
                    </div>
                </td>
                <td align="center" width="22px">
                    <div class="expand" id="te_ma" title="expand"></div>
                </td>
            </tr>
            <tr class="te_ma_prop">
                <td>
                    <div class="popup_msg" id="tl_te_ma_prop_type" title="Click to View The TE MA PROP Definition">TE MA PROP
                        <div class='type_info'>&nbsp;&nbsp;&nbsp;</div>
                    </div>
                </td>
                <td align="center" width="22px"></td>
            </tr>

            <tr class="te_oth">
                <td>
                    <div class="popup_msg" id="tl_te_oth_type" title="Click to View The TE OTH Definition">TE OTH
                        <div class='type_info'>&nbsp;&nbsp;&nbsp;</div>
                    </div>
                </td>
                <td align="center" width="22px">
                    <div class="expand" id="te_oth" title="expand"></div>
                </td>
            </tr>
            <tr class="te_oth_cur">
                <td>
                    <div class="popup_msg" id="tl_te_oth_cur_type" title="Click to View The TE OTH CUR Definition">TE OTH CUR
                        <div class='type_info'>&nbsp;&nbsp;&nbsp;</div>
                    </div>
                </td>
                <td align="center" width="22px"></td>
            </tr>

            </tbody>
        </table>
    </div>
    <div class='traffic_light'>
    <table class="tl_tab">
    <!-- gene symbol -->
    <thead>
    <tr>
        <@s.iterator status="tl_gene_stat" value="tlPagination.pageResults" id="tlgenes" >
            <th>
                <@s.if test="%{#tlgenes.tlGene.displayName != null}">
                    <div class="gene_name">${tlgenes.tlGene.displayName}</div>
                </@s.if>
                <@s.else>
                    <div class="gene_name">Unknown</div>
                </@s.else>
            </th>
        </@s.iterator>
    </tr>
    </thead>
    <tbody>
    <!-- PE Evidence -->
    <tr class="pe">
        <@s.iterator status="tlStat" value="tlPagination.pageResults" id="tlResult" >
            <td>
                <a href="${base}/tl/tlevsum.jspx?tpbDataType=PE&tlGeneId=${tlResult.tlGene.id?c}" class="tl_ev_sum">
                    <@s.if test="%{#tlResult.tlPEColor.colorLevel == 4}">
                        <div class='tl4_color'>&nbsp;</div>
                    </@s.if>
                    <@s.elseif test="%{#tlResult.tlPEColor.colorLevel == 3}">
                        <div class='tl3_color'>&nbsp;</div>
                    </@s.elseif>
                    <@s.elseif test="%{#tlResult.tlPEColor.colorLevel == 2}">
                        <div class='tl2_color'>&nbsp;</div>
                    </@s.elseif>
                    <@s.else>
                        <div class='tl1_color'>&nbsp;</div>
                    </@s.else>
                </a>
            </td>
        </@s.iterator>
    </tr>
    <!-- PE MS Evidence -->
    <tr class="pe_ms">
        <@s.iterator status="tlStat" value="tlPagination.pageResults" id="tlResult" >
            <td>
                <a href="${base}/tl/tlevsum.jspx?tpbDataType=PE_MS&tlGeneId=${tlResult.tlGene.id?c}" class="tl_ev_sum">
                    <@s.if test="%{#tlResult.tlPEMSColor.colorLevel == 4}">
                        <div class='tl4_color'>&nbsp;</div>
                    </@s.if>
                    <@s.elseif test="%{#tlResult.tlPEMSColor.colorLevel == 3}">
                        <div class='tl3_color'>&nbsp;</div>
                    </@s.elseif>
                    <@s.elseif test="%{#tlResult.tlPEMSColor.colorLevel == 2}">
                        <div class='tl2_color'>&nbsp;</div>
                    </@s.elseif>
                    <@s.else>
                        <div class='tl1_color'>&nbsp;</div>
                    </@s.else>
                </a>
            </td>
        </@s.iterator>
    </tr>

    <!-- PE MS ANN Evidence -->
    <tr class="pe_ms_ann">
        <@s.iterator status="tlStat" value="tlPagination.pageResults" id="tlResult" >
            <td>
                <a href="${base}/tl/tlevsum.jspx?tpbDataType=PE_MS_ANN&tlGeneId=${tlResult.tlGene.id?c}" class="tl_ev_sum">
                    <@s.if test="%{#tlResult.tlPEMSANNColor.colorLevel == 4}">
                        <div class='tl4_color'>&nbsp;</div>
                    </@s.if>
                    <@s.elseif test="%{#tlResult.tlPEMSANNColor.colorLevel == 3}">
                        <div class='tl3_color'>&nbsp;</div>
                    </@s.elseif>
                    <@s.elseif test="%{#tlResult.tlPEMSANNColor.colorLevel == 2}">
                        <div class='tl2_color'>&nbsp;</div>
                    </@s.elseif>
                    <@s.else>
                        <div class='tl1_color'>&nbsp;</div>
                    </@s.else>
                </a>
            </td>
        </@s.iterator>
    </tr>

    <!-- PE MS Prob Evidence -->
    <tr class="pe_ms_prob">
        <@s.iterator status="tlStat" value="tlPagination.pageResults" id="tlResult" >
            <td>
                <a href="${base}/tl/tlevsum.jspx?tpbDataType=PE_MS_PROB&tlGeneId=${tlResult.tlGene.id?c}" class="tl_ev_sum">
                    <@s.if test="%{#tlResult.tlPEMSPROBColor.colorLevel == 4}">
                        <div class='tl4_color'>&nbsp;</div>
                    </@s.if>
                    <@s.elseif test="%{#tlResult.tlPEMSPROBColor.colorLevel == 3}">
                        <div class='tl3_color'>&nbsp;</div>
                    </@s.elseif>
                    <@s.elseif test="%{#tlResult.tlPEMSPROBColor.colorLevel == 2}">
                        <div class='tl2_color'>&nbsp;</div>
                    </@s.elseif>
                    <@s.else>
                        <div class='tl1_color'>&nbsp;</div>
                    </@s.else>
                </a>
            </td>
        </@s.iterator>
    </tr>

    <!-- PE MS Samples Evidence -->
    <tr class="pe_ms_sam">
        <@s.iterator status="tlStat" value="tlPagination.pageResults" id="tlResult" >
            <td>
                <a href="${base}/tl/tlevsum.jspx?tpbDataType=PE_MS_SAM&tlGeneId=${tlResult.tlGene.id?c}" class="tl_ev_sum">
                    <@s.if test="%{#tlResult.tlPEMSSAMColor.colorLevel == 4}">
                        <div class='tl4_color'>&nbsp;</div>
                    </@s.if>
                    <@s.elseif test="%{#tlResult.tlPEMSSAMColor.colorLevel == 3}">
                        <div class='tl3_color'>&nbsp;</div>
                    </@s.elseif>
                    <@s.elseif test="%{#tlResult.tlPEMSSAMColor.colorLevel == 2}">
                        <div class='tl2_color'>&nbsp;</div>
                    </@s.elseif>
                    <@s.else>
                        <div class='tl1_color'>&nbsp;</div>
                    </@s.else>
                </a>
            </td>
        </@s.iterator>
    </tr>

    <!-- PE ANTI Evidence -->
    <tr class="pe_anti">
        <@s.iterator status="tlStat" value="tlPagination.pageResults" id="tlResult" >
            <td>
                <a href="${base}/tl/tlevsum.jspx?tpbDataType=PE_ANTI&tlGeneId=${tlResult.tlGene.id?c}" class="tl_ev_sum">
                    <@s.if test="%{#tlResult.tlPEANTIColor.colorLevel == 4}">
                        <div class='tl4_color'>&nbsp;</div>
                    </@s.if>
                    <@s.elseif test="%{#tlResult.tlPEANTIColor.colorLevel == 3}">
                        <div class='tl3_color'>&nbsp;</div>
                    </@s.elseif>
                    <@s.elseif test="%{#tlResult.tlPEANTIColor.colorLevel == 2}">
                        <div class='tl2_color'>&nbsp;</div>
                    </@s.elseif>
                    <@s.else>
                        <div class='tl1_color'>&nbsp;</div>
                    </@s.else>
                </a>
            </td>
        </@s.iterator>
    </tr>

    <!-- PE ANTI ANN Evidence -->
    <tr class="pe_anti_ann">
        <@s.iterator status="tlStat" value="tlPagination.pageResults" id="tlResult" >
            <td>
                <a href="${base}/tl/tlevsum.jspx?tpbDataType=PE_ANTI_ANN&tlGeneId=${tlResult.tlGene.id?c}" class="tl_ev_sum">
                    <@s.if test="%{#tlResult.tlPEANTIANNColor.colorLevel == 4}">
                        <div class='tl4_color'>&nbsp;</div>
                    </@s.if>
                    <@s.elseif test="%{#tlResult.tlPEANTIANNColor.colorLevel == 3}">
                        <div class='tl3_color'>&nbsp;</div>
                    </@s.elseif>
                    <@s.elseif test="%{#tlResult.tlPEANTIANNColor.colorLevel == 2}">
                        <div class='tl2_color'>&nbsp;</div>
                    </@s.elseif>
                    <@s.else>
                        <div class='tl1_color'>&nbsp;</div>
                    </@s.else>
                </a>
            </td>
        </@s.iterator>
    </tr>

    <!-- PE ANTI IHC Evidence -->
    <tr class="pe_anti_ihc">
        <@s.iterator status="tlStat" value="tlPagination.pageResults" id="tlResult" >
            <td>
                <a href="${base}/tl/tlevsum.jspx?tpbDataType=PE_ANTI_IHC&tlGeneId=${tlResult.tlGene.id?c}" class="tl_ev_sum">
                    <@s.if test="%{#tlResult.tlPEANTIIHCColor.colorLevel == 4}">
                        <div class='tl4_color'>&nbsp;</div>
                    </@s.if>
                    <@s.elseif test="%{#tlResult.tlPEANTIIHCColor.colorLevel == 3}">
                        <div class='tl3_color'>&nbsp;</div>
                    </@s.elseif>
                    <@s.elseif test="%{#tlResult.tlPEANTIIHCColor.colorLevel == 2}">
                        <div class='tl2_color'>&nbsp;</div>
                    </@s.elseif>
                    <@s.else>
                        <div class='tl1_color'>&nbsp;</div>
                    </@s.else>
                </a>
            </td>
        </@s.iterator>
    </tr>

    <!-- PE ANTI IHC NORM Evidence -->
    <tr class="pe_anti_ihc_norm">
        <@s.iterator status="tlStat" value="tlPagination.pageResults" id="tlResult" >
            <td>
                <a href="${base}/tl/tlevsum.jspx?tpbDataType=PE_ANTI_IHC_NORM&tlGeneId=${tlResult.tlGene.id?c}" class="tl_ev_sum">
                    <@s.if test="%{#tlResult.tlPEANTIIHCNORMColor.colorLevel == 4}">
                        <div class='tl4_color'>&nbsp;</div>
                    </@s.if>
                    <@s.elseif test="%{#tlResult.tlPEANTIIHCNORMColor.colorLevel == 3}">
                        <div class='tl3_color'>&nbsp;</div>
                    </@s.elseif>
                    <@s.elseif test="%{#tlResult.tlPEANTIIHCNORMColor.colorLevel == 2}">
                        <div class='tl2_color'>&nbsp;</div>
                    </@s.elseif>
                    <@s.else>
                        <div class='tl1_color'>&nbsp;</div>
                    </@s.else>
                </a>
            </td>
        </@s.iterator>
    </tr>

    <!-- PE OTH Evidence -->
    <tr class="pe_oth">
        <@s.iterator status="tlStat" value="tlPagination.pageResults" id="tlResult" >
            <td>
                <a href="${base}/tl/tlevsum.jspx?tpbDataType=PE_OTH&tlGeneId=${tlResult.tlGene.id?c}" class="tl_ev_sum">
                    <@s.if test="%{#tlResult.tlPEOTHColor.colorLevel == 4}">
                        <div class='tl4_color'>&nbsp;</div>
                    </@s.if>
                    <@s.elseif test="%{#tlResult.tlPEOTHColor.colorLevel == 3}">
                        <div class='tl3_color'>&nbsp;</div>
                    </@s.elseif>
                    <@s.elseif test="%{#tlResult.tlPEOTHColor.colorLevel == 2}">
                        <div class='tl2_color'>&nbsp;</div>
                    </@s.elseif>
                    <@s.else>
                        <div class='tl1_color'>&nbsp;</div>
                    </@s.else>
                </a>
            </td>
        </@s.iterator>
    </tr>

    <!-- PE OTH CUR Evidence -->
    <tr class="pe_oth_cur">
        <@s.iterator status="tlStat" value="tlPagination.pageResults" id="tlResult" >
            <td>
                <a href="${base}/tl/tlevsum.jspx?tpbDataType=PE_OTH_CUR&tlGeneId=${tlResult.tlGene.id?c}" class="tl_ev_sum">
                    <@s.if test="%{#tlResult.tlPEOTHCURColor.colorLevel == 4}">
                        <div class='tl4_color'>&nbsp;</div>
                    </@s.if>
                    <@s.elseif test="%{#tlResult.tlPEOTHCURColor.colorLevel == 3}">
                        <div class='tl3_color'>&nbsp;</div>
                    </@s.elseif>
                    <@s.elseif test="%{#tlResult.tlPEOTHCURColor.colorLevel == 2}">
                        <div class='tl2_color'>&nbsp;</div>
                    </@s.elseif>
                    <@s.else>
                        <div class='tl1_color'>&nbsp;</div>
                    </@s.else>
                </a>
            </td>
        </@s.iterator>
    </tr>
    <tr>
        <td colspan=${tlPagination.totalRecords?c} style="background-color: #ffffff;"></td>
    </tr>
    <!-- PTM Evidence -->
    <tr class="ptm">
        <@s.iterator status="tlStat" value="tlPagination.pageResults" id="tlResult" >
            <td>
                <a href="${base}/tl/tlevsum.jspx?tpbDataType=PTM&tlGeneId=${tlResult.tlGene.id?c}" class="tl_ev_sum">
                    <@s.if test="%{#tlResult.tlPTMColor.colorLevel == 4}">
                        <div class='tl4_color'>&nbsp;</div>
                    </@s.if>
                    <@s.elseif test="%{#tlResult.tlPTMColor.colorLevel == 3}">
                        <div class='tl3_color'>&nbsp;</div>
                    </@s.elseif>
                    <@s.elseif test="%{#tlResult.tlPTMColor.colorLevel == 2}">
                        <div class='tl2_color'>&nbsp;</div>
                    </@s.elseif>
                    <@s.else>
                        <div class='tl1_color'>&nbsp;</div>
                    </@s.else>
                </a>
            </td>
        </@s.iterator>
    </tr>


    <!-- PTM PHS -->
    <tr class="ptm_phs">
        <@s.iterator status="tlStat" value="tlPagination.pageResults" id="tlResult" >
            <td>
                <a href="${base}/tl/tlevsum.jspx?tpbDataType=PTM_PHS&tlGeneId=${tlResult.tlGene.id?c}" class="tl_ev_sum">
                    <@s.if test="%{#tlResult.tlPTMPHSColor.colorLevel == 4}">
                        <div class='tl4_color'>&nbsp;</div>
                    </@s.if>
                    <@s.elseif test="%{#tlResult.tlPTMPHSColor.colorLevel == 3}">
                        <div class='tl3_color'>&nbsp;</div>
                    </@s.elseif>
                    <@s.elseif test="%{#tlResult.tlPTMPHSColor.colorLevel == 2}">
                        <div class='tl2_color'>&nbsp;</div>
                    </@s.elseif>
                    <@s.else>
                        <div class='tl1_color'>&nbsp;</div>
                    </@s.else>
                </a>
            </td>
        </@s.iterator>
    </tr>

    <!-- PTM PHS SER-->
    <tr class="ptm_phs_ser">
        <@s.iterator status="tlStat" value="tlPagination.pageResults" id="tlResult" >
            <td>
                <a href="${base}/tl/tlevsum.jspx?tpbDataType=PTM_PHS_SER&tlGeneId=${tlResult.tlGene.id?c}" class="tl_ev_sum">
                    <@s.if test="%{#tlResult.tlPTMPHSSERColor.colorLevel == 4}">
                        <div class='tl4_color'>&nbsp;</div>
                    </@s.if>
                    <@s.elseif test="%{#tlResult.tlPTMPHSSERColor.colorLevel == 3}">
                        <div class='tl3_color'>&nbsp;</div>
                    </@s.elseif>
                    <@s.elseif test="%{#tlResult.tlPTMPHSSERColor.colorLevel == 2}">
                        <div class='tl2_color'>&nbsp;</div>
                    </@s.elseif>
                    <@s.else>
                        <div class='tl1_color'>&nbsp;</div>
                    </@s.else>
                </a>
            </td>
        </@s.iterator>
    </tr>

    <!-- PTM PHS THR-->
    <tr class="ptm_phs_thr">
        <@s.iterator status="tlStat" value="tlPagination.pageResults" id="tlResult" >
            <td>
                <a href="${base}/tl/tlevsum.jspx?tpbDataType=PTM_PHS_THR&tlGeneId=${tlResult.tlGene.id?c}" class="tl_ev_sum">
                    <@s.if test="%{#tlResult.tlPTMPHSTHRColor.colorLevel == 4}">
                        <div class='tl4_color'>&nbsp;</div>
                    </@s.if>
                    <@s.elseif test="%{#tlResult.tlPTMPHSTHRColor.colorLevel == 3}">
                        <div class='tl3_color'>&nbsp;</div>
                    </@s.elseif>
                    <@s.elseif test="%{#tlResult.tlPTMPHSTHRColor.colorLevel == 2}">
                        <div class='tl2_color'>&nbsp;</div>
                    </@s.elseif>
                    <@s.else>
                        <div class='tl1_color'>&nbsp;</div>
                    </@s.else>
                </a>
            </td>
        </@s.iterator>
    </tr>
    <!-- PTM PHS TYR-->
    <tr class="ptm_phs_tyr">
        <@s.iterator status="tlStat" value="tlPagination.pageResults" id="tlResult" >
            <td>
                <a href="${base}/tl/tlevsum.jspx?tpbDataType=PTM_PHS_TYR&tlGeneId=${tlResult.tlGene.id?c}" class="tl_ev_sum">
                    <@s.if test="%{#tlResult.tlPTMPHSTYRColor.colorLevel == 4}">
                        <div class='tl4_color'>&nbsp;</div>
                    </@s.if>
                    <@s.elseif test="%{#tlResult.tlPTMPHSTYRColor.colorLevel == 3}">
                        <div class='tl3_color'>&nbsp;</div>
                    </@s.elseif>
                    <@s.elseif test="%{#tlResult.tlPTMPHSTYRColor.colorLevel == 2}">
                        <div class='tl2_color'>&nbsp;</div>
                    </@s.elseif>
                    <@s.else>
                        <div class='tl1_color'>&nbsp;</div>
                    </@s.else>
                </a>
            </td>
        </@s.iterator>
    </tr>

    <!-- PTM ACE -->
    <tr class="ptm_ace">
        <@s.iterator status="tlStat" value="tlPagination.pageResults" id="tlResult" >
            <td>
                <a href="${base}/tl/tlevsum.jspx?tpbDataType=PTM_ACE&tlGeneId=${tlResult.tlGene.id?c}" class="tl_ev_sum">
                    <@s.if test="%{#tlResult.tlPTMACEColor.colorLevel == 4}">
                        <div class='tl4_color'>&nbsp;</div>
                    </@s.if>
                    <@s.elseif test="%{#tlResult.tlPTMACEColor.colorLevel == 3}">
                        <div class='tl3_color'>&nbsp;</div>
                    </@s.elseif>
                    <@s.elseif test="%{#tlResult.tlPTMACEColor.colorLevel == 2}">
                        <div class='tl2_color'>&nbsp;</div>
                    </@s.elseif>
                    <@s.else>
                        <div class='tl1_color'>&nbsp;</div>
                    </@s.else>
                </a>
            </td>
        </@s.iterator>
    </tr>

    <!-- PTM ACE LYS-->
    <tr class="ptm_ace_lys">
        <@s.iterator status="tlStat" value="tlPagination.pageResults" id="tlResult" >
            <td>
                <a href="${base}/tl/tlevsum.jspx?tpbDataType=PTM_ACE_LYS&tlGeneId=${tlResult.tlGene.id?c}" class="tl_ev_sum">
                    <@s.if test="%{#tlResult.tlPTMACELYSColor.colorLevel == 4}">
                        <div class='tl4_color'>&nbsp;</div>
                    </@s.if>
                    <@s.elseif test="%{#tlResult.tlPTMACELYSColor.colorLevel == 3}">
                        <div class='tl3_color'>&nbsp;</div>
                    </@s.elseif>
                    <@s.elseif test="%{#tlResult.tlPTMACELYSColor.colorLevel == 2}">
                        <div class='tl2_color'>&nbsp;</div>
                    </@s.elseif>
                    <@s.else>
                        <div class='tl1_color'>&nbsp;</div>
                    </@s.else>
                </a>
            </td>
        </@s.iterator>
    </tr>

    <!-- PTM ACE NTA-->
    <tr class="ptm_ace_nta">
        <@s.iterator status="tlStat" value="tlPagination.pageResults" id="tlResult" >
            <td>
                <a href="${base}/tl/tlevsum.jspx?tpbDataType=PTM_ACE_NTA&tlGeneId=${tlResult.tlGene.id?c}" class="tl_ev_sum">
                    <@s.if test="%{#tlResult.tlPTMACENTAColor.colorLevel == 4}">
                        <div class='tl4_color'>&nbsp;</div>
                    </@s.if>
                    <@s.elseif test="%{#tlResult.tlPTMACENTAColor.colorLevel == 3}">
                        <div class='tl3_color'>&nbsp;</div>
                    </@s.elseif>
                    <@s.elseif test="%{#tlResult.tlPTMACENTAColor.colorLevel == 2}">
                        <div class='tl2_color'>&nbsp;</div>
                    </@s.elseif>
                    <@s.else>
                        <div class='tl1_color'>&nbsp;</div>
                    </@s.else>
                </a>
            </td>
        </@s.iterator>
    </tr>

    <tr>
        <td colspan='${tlPagination.totalRecords?c}' style='background-color: #ffffff;'></td>
    </tr>

    <!-- TE Evidence -->
    <tr class="te">
        <@s.iterator status="tlStat" value="tlPagination.pageResults" id="tlResult" >
            <td>
                <a href="${base}/tl/tlevsum.jspx?tpbDataType=TE&tlGeneId=${tlResult.tlGene.id?c}" class="tl_ev_sum">
                    <@s.if test="%{#tlResult.tlTEColor.colorLevel == 4}">
                        <div class='tl4_color'>&nbsp;</div>
                    </@s.if>
                    <@s.elseif test="%{#tlResult.tlTEColor.colorLevel == 3}">
                        <div class='tl3_color'>&nbsp;</div>
                    </@s.elseif>
                    <@s.elseif test="%{#tlResult.tlTEColor.colorLevel == 2}">
                        <div class='tl2_color'>&nbsp;</div>
                    </@s.elseif>
                    <@s.else>
                        <div class='tl1_color'>&nbsp;</div>
                    </@s.else>
                </a>
            </td>
        </@s.iterator>
    </tr>

    <!-- TE MA Evidence -->
    <tr class="te_ma">
        <@s.iterator status="tlStat" value="tlPagination.pageResults" id="tlResult" >
            <td>
                <a href="${base}/tl/tlevsum.jspx?tpbDataType=TE_MA&tlGeneId=${tlResult.tlGene.id?c}" class="tl_ev_sum">
                    <@s.if test="%{#tlResult.tlTEMAColor.colorLevel == 4}">
                        <div class='tl4_color'>&nbsp;</div>
                    </@s.if>
                    <@s.elseif test="%{#tlResult.tlTEMAColor.colorLevel == 3}">
                        <div class='tl3_color'>&nbsp;</div>
                    </@s.elseif>
                    <@s.elseif test="%{#tlResult.tlTEMAColor.colorLevel == 2}">
                        <div class='tl2_color'>&nbsp;</div>
                    </@s.elseif>
                    <@s.else>
                        <div class='tl1_color'>&nbsp;</div>
                    </@s.else>
                </a>
            </td>
        </@s.iterator>
    </tr>

    <!-- TE MA PROP Evidence -->
    <tr class="te_ma_prop">
        <@s.iterator status="tlStat" value="tlPagination.pageResults" id="tlResult" >
            <td>
                <a href="${base}/tl/tlevsum.jspx?tpbDataType=TE_MA_PROP&tlGeneId=${tlResult.tlGene.id?c}" class="tl_ev_sum">
                    <@s.if test="%{#tlResult.tlTEMAPROPColor.colorLevel == 4}">
                        <div class='tl4_color'>&nbsp;</div>
                    </@s.if>
                    <@s.elseif test="%{#tlResult.tlTEMAPROPColor.colorLevel == 3}">
                        <div class='tl3_color'>&nbsp;</div>
                    </@s.elseif>
                    <@s.elseif test="%{#tlResult.tlTEMAPROPColor.colorLevel == 2}">
                        <div class='tl2_color'>&nbsp;</div>
                    </@s.elseif>
                    <@s.else>
                        <div class='tl1_color'>&nbsp;</div>
                    </@s.else>
                </a>
            </td>
        </@s.iterator>
    </tr>

    <!-- TE OTH Evidence -->
    <tr class="te_oth">
        <@s.iterator status="tlStat" value="tlPagination.pageResults" id="tlResult" >
            <td>
                <a href="${base}/tl/tlevsum.jspx?tpbDataType=TE_OTH&tlGeneId=${tlResult.tlGene.id?c}" class="tl_ev_sum">
                    <@s.if test="%{#tlResult.tlTEOTHColor.colorLevel == 4}">
                        <div class='tl4_color'>&nbsp;</div>
                    </@s.if>
                    <@s.elseif test="%{#tlResult.tlTEOTHColor.colorLevel == 3}">
                        <div class='tl3_color'>&nbsp;</div>
                    </@s.elseif>
                    <@s.elseif test="%{#tlResult.tlTEOTHColor.colorLevel == 2}">
                        <div class='tl2_color'>&nbsp;</div>
                    </@s.elseif>
                    <@s.else>
                        <div class='tl1_color'>&nbsp;</div>
                    </@s.else>
                </a>
            </td>
        </@s.iterator>
    </tr>

    <!-- TE OTH CUR Evidence -->
    <tr class="te_oth_cur">
        <@s.iterator status="tlStat" value="tlPagination.pageResults" id="tlResult" >
            <td>
                <a href="${base}/tl/tlevsum.jspx?tpbDataType=TE_OTH_CUR&tlGeneId=${tlResult.tlGene.id?c}" class="tl_ev_sum">
                    <@s.if test="%{#tlResult.tlTEOTHCURColor.colorLevel == 4}">
                        <div class='tl4_color'>&nbsp;</div>
                    </@s.if>
                    <@s.elseif test="%{#tlResult.tlTEOTHCURColor.colorLevel == 3}">
                        <div class='tl3_color'>&nbsp;</div>
                    </@s.elseif>
                    <@s.elseif test="%{#tlResult.tlTEOTHCURColor.colorLevel == 2}">
                        <div class='tl2_color'>&nbsp;</div>
                    </@s.elseif>
                    <@s.else>
                        <div class='tl1_color'>&nbsp;</div>
                    </@s.else>
                </a>
            </td>
        </@s.iterator>
    </tr>
    </tbody>
    </table>
    </div>
    </div>

    <div class="tl_counter_div">
        <div class="tl_counter_title">A total of <font color="green"> ${tlPagination.totalRecords?c} </font> gene(s) in the traffic light</div>
        <div class="tl_sum_report">
            <a href="#" class="view_sum_report" title="view the summary report">view summary report</a> &nbsp; &nbsp;
            <a href="${base}/tl/exportTl.jspx"  title="export the traffic lights as a CSV file">export traffic lights <img src="${base}/images/download.png"/></a> &nbsp; &nbsp;

        </div>
    </div>
    <div class="tl_sum_report_div"></div>
    <div class="tl_ev_summary_div"></div>
    </@s.if>
    <@s.else>
    <br/>

    <div class="none_tl_results_div">
        The traffic light is not currently available based on the filter condtions. Try using other conditions.
    </div>
    <br/>
    <br/>
    <br/>
    <br/>
    <br/>
    <br/>
    <br/>
    <br/>
    <br/>
    <br/>
    </@s.else>
</@s.if>
<@s.else>
<br/>
<br/>
<br/>
<br/>
<br/>
<br/>
<br/>
<br/>
<br/>
<br/>
<br/>
<br/>
<br/>
</@s.else>
</div>
</div>
</div>
<#include "../template/footer.ftl"/>
</body>
</html>
