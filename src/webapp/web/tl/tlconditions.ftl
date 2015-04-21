<div class="tl_control_div">
<@s.form action="trafficlight.jspx" namespace="/tl" method="post" id='tl_search_form'>
<div class="tl_control_left">
    <div class="filter_field_row">
        <div class="base_conds">
            Chromosome: &nbsp; <@s.select name ="tlSearchBean.selectedChromType" headerKey="tlSearchBean.selectedChromType" id="selected_chrom" list ="chromTypes"  cssClass="select_norm"/>
            &nbsp;&nbsp;
            &nbsp;Data Sources: <@s.checkbox name="tlSearchBean.nxDbSelected" id="selected_dbsource" cssClass="check_box_norm" /> neXtProt&nbsp;&nbsp;
            <@s.checkbox name="tlSearchBean.gpmDbSelected" id="selected_dbsource" cssClass="check_box_norm" /> GPM &nbsp;&nbsp;
            <@s.checkbox name="tlSearchBean.hpaDbSelected" id="selected_dbsource" cssClass="check_box_norm" /> HPA &nbsp;&nbsp; &nbsp;
            <@s.checkbox name="tlSearchBean.bcDbSelected" id="selected_dbsource" cssClass="check_box_norm" /> Barcode &nbsp;&nbsp;
            Version: <@s.select name = "tlSearchBean.selectedVersion" id="selected_version" headerKey="tlSearchBean.selectedVersion" list="tpbVersions" cssClass="select_norm2"/>
        </div>
    </div>
    <div style="clear: both;"></div>
    <div class="filter_separator"></div>
    <@s.if test="%{tlSearchBean.advancedMode == true}">
    <div class="advanced_filter_mode">
    </@s.if>
    <@s.else>
    <div class="advanced_filter_mode" style="display: none;">
    </@s.else>
        <div class="filter_field_row">
            <div class="filter_field_title">TPB Data Type</div>
            <div class="filter_field_section">
                <div class="advancedMode"><@s.hidden name="tlSearchBean.advancedMode" id="inAdvancedMode" /></div>
                <div>
                    The Evidence Level Of Data
                    Type: &nbsp; <@s.select name = "dataType" id="data_type_selected" headerKey="-1" headerValue ="-- select a data type --" list="tpbDataTypes" cssClass="select_norm2"/>
                </div>
                <div class="blank_separator"></div>
                <table class="type_evidence_level_tab" id="type_evidence_level_tab">
                    <tbody>
                        <@s.iterator status="eLevelStat" value="tlSearchBean.tlTypeEvLevelFilters" id="evLevel" >
                        <tr>
                            <td width="160px">
                                <@s.hidden name="tlSearchBean.tlTypeEvLevelFilters[%{#eLevelStat.index}].dataTypeDisplayName"  value="%{#evLevel.dataTypeDisplayName}" id="dataTypeDisplayName"/>
                                <@s.hidden name="tlSearchBean.tlTypeEvLevelFilters[%{#eLevelStat.index}].dataType"  value="%{#evLevel.dataType}" id="selectedDataType"/>
                                <@s.property  value="#evLevel.dataTypeDisplayName" />
                            </td>
                            <td>
                                <div class='tl4_level'>&nbsp;</div>
                            </td>
                            <td><@s.checkbox name="tlSearchBean.tlTypeEvLevelFilters[%{#eLevelStat.index}].typeEvLevel4" id="typeEvLevel4" cssClass="check_box_norm"/></td>
                            <td width='30px' align='right'>
                                <div class='tl3_level'>&nbsp;</div>
                            </td>
                            <td><@s.checkbox name="tlSearchBean.tlTypeEvLevelFilters[%{#eLevelStat.index}].typeEvLevel3" id="typeEvLevel3" cssClass="check_box_norm"/></td>
                            <td width='30px' align='right'>
                                <div class='tl2_level'>&nbsp;</div>
                            </td>
                            <td><@s.checkbox name="tlSearchBean.tlTypeEvLevelFilters[%{#eLevelStat.index}].typeEvLevel2" id="typeEvLevel2" cssClass="check_box_norm"/></td>
                            <td width='30px' align='right'>
                                <div class='tl1_level'>&nbsp;</div>
                            </td>
                            <td><@s.checkbox name="tlSearchBean.tlTypeEvLevelFilters[%{#eLevelStat.index}].typeEvLevel1" id="typeEvLevel1" cssClass="check_box_norm"/></td>
                            <td width="50px" align="right">
                                <div class="cancel_type_ev_level">&nbsp;</div>
                            </td>
                        </tr>
                        </@s.iterator>
                    </tbody>
                </table>
            </div>
        </div>
        <!-- //End of Type Level Filter -->
        <div style="clear: both;"></div>
        <div class="filter_separator"></div>
        <div class="filter_field_row">
            <div class="filter_field_title">Chromosome Region</div>
            <div class="filter_field_section">From: &nbsp; <@s.textfield name="tlSearchBean.regionFrom" id="tlSearchBeanRegionFrom" cssClass="numberinput" /> &nbsp; To:
                &nbsp; <@s.textfield name="tlSearchBean.regionTo" id="tlSearchBeanRegionTo" cssClass="numberinput"/>
                <div class="comments">
                    (The nucleotide base index (in chromosome space) start and end indexes)
                </div>
            </div>
        </div>
        <!-- End of Chromosome Region -->
        <div style="clear: both;"></div>
        <div class="filter_separator"></div>
        <div class="filter_field_row">
            <div class="filter_field_title">Gene Symbols / Accessions</div>
            <div class="filter_field_section">
                <@s.select name ="tlSearchBean.selectedGeneValueType" id="tlSearchBeanSelectedGeneValueType" list ="geneValueTypes"  cssClass="select_norm"/>
                <div class="blank_separator"></div>
                <@s.textarea  name="tlSearchBean.geneTypeValues" id="tlSearchBeanGeneTypeValues" cols="120" rows="5" cssClass="input_textarea" />
                &nbsp; &nbsp;
                <div class="comments">
                    (A list of gene <b>symbols</b> or <b>accessions</b> separated by <b>Comma</b> or <b>Space</b> or <b>Semicolon</b> or <b>Tab</b> or <b>NewLine</b>)
                </div>
            </div>
        </div>
        <!-- End of Gene Symbol -->
        <div style="clear: both;"></div>
    </div> <!-- //End of Advanced Filter Mode-->

    <!-- Explore Sumbit Section -->
    <div class="tl_explore_div">
        <div class="filter_options_div">
            <@s.if test="%{tlSearchBean.advancedMode == true}">
                <a href="#" class="filter_option_href" id="less_options">less options</a>
            </@s.if>
            <@s.else>
                <a href="#" class="filter_option_href" id="more_options">more options</a>
            </@s.else>
        </div>
        <input type="submit" name="explore" value="Explore" class="input_button"/>
    </div><!--End of Explore Submit Section -->
    <!-- Progress Bar -->
    <div class='progress_bar'>
        Loading the traffic light, please wait ... <img src="${base}/images/wait_loader.gif" class="load_img"/>
    </div>
    <div style="clear: both;"></div>

</div><!-- End of Control Left Section -->
</@s.form>
</div>
<div class="blank_separator"></div>