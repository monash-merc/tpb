<#include  "../template/ftl_header.ftl" />
<!DOCTYPE html PUBLIC "-//W3C//DTD XHTML 1.0 Transitional//EN"
        "http://www.w3.org/TR/xhtml1/DTD/xhtml1-transitional.dtd">
<html xmlns="http://www.w3.org/1999/xhtml">
<head>
    <title>Data Sources</title>
<#include "../template/header.ftl" />
</head>
<body>
<div class="blank_separator"></div>
<#include "../template/navigation.ftl" />
<div class="display_main_container">
<@s.if test="%{#session.authentication_flag =='authenticated'}">
    <!-- sub-nav bar -->
    <div class="subnav_section">
        <#include "../template/user_nav.ftl"/>
    </div>
    <!-- end of sub-nav bar -->
</@s.if>
    <div class="display_middle_div">
        <div class="blank_separator"></div>
        <div class="display_error_div">
        <#include "../template/action_errors.ftl" />
        </div>
        <div class="page_title">Sources</div>
        <div class="display_inner_div">

            <div class="page_desc">
                <p>
                    Currently we are utilising the following data sources in TPB.
                    We are very grateful to all of them for providing access to
                    their data and assistance with implementing them in TPB.
                </p>
            </div>
            <div class="dsversion_select">
            <@s.form action="sources.jspx" namespace="/site" method="post">
                Please select a chromosome type to view the latest version of sources: &nbsp;  <@s.select name ="selectedChrom"  headerKey="selectedChrom" list ="chromTypes"  cssClass="select_norm"/>
                <input type="submit" name="view" value="View" class="input_button"/>
            </@s.form>
            </div>

        <@s.if test="%{dbVersionBeans != null && dbVersionBeans.size() >0 }">
            <br/>

            <div class="dsv_title">The Sources of Chromosome ${selectedChrom}:</div>
            <div class="ds_version">
                <table class="dsv_tab">
                    <@s.iterator status="dsv_stat" value="dbVersionBeans" id="dsv" >
                        <tr>
                            <td width="45%">

                                <@s.if test="%{#dsv.dbSource =='GPM'}">
                                    <a target="_blank" href="http://gpmdb.thegpm.org">
                                        <img style="width:150px" src="${base}/images/logo/gpm_db.png"/> PE
                                    </a>
                                </@s.if>

                                <@s.if test="%{#dsv.dbSource =='GPMpSYT'}">
                                    <a target="_blank" href="http://gpmdb.thegpm.org">
                                        <img style="width:150px" src="${base}/images/logo/gpm_db.png"/> PTM pSYT
                                    </a>
                                </@s.if>

                                <@s.if test="%{#dsv.dbSource =='GPMLYS'}">
                                    <a target="_blank" href="http://gpmdb.thegpm.org">
                                        <img style="width:150px" src="${base}/images/logo/gpm_db.png"/> PTM LYS
                                    </a>
                                </@s.if>

                                <@s.if test="%{#dsv.dbSource =='GPMNTA'}">
                                    <a target="_blank" href="http://gpmdb.thegpm.org">
                                        <img style="width:150px" src="${base}/images/logo/gpm_db.png"/> PTM NTA
                                    </a>
                                </@s.if>

                                <@s.if test="%{#dsv.dbSource =='HPA'}">
                                    <a target="_blank" href="http://www.proteinatlas.org">
                                        <img style="width:200px" src="${base}/images/logo/logo_text_small.gif"/>
                                        <img src="${base}/images/logo/logo_hex_anim_small.gif"/>
                                    </a>
                                </@s.if>
                                <@s.if test="%{#dsv.dbSource =='NextProt'}">
                                    <a target="_blank" href="http://www.nextprot.org">
                                        <img style="width:200px" src="${base}/images/logo/np.png" alt="neXtprot"/>
                                    </a>
                                </@s.if>
                                <@s.if test="%{#dsv.dbSource =='BarcodeHGU133a'}">
                                    <a target="_blank" href="http://barcode.luhs.org">
                                        <img style="width:200px" src="${base}/images/logo/barcode_250.png" alt="Barcode HGU133a"/> HGU133a
                                    </a>
                                </@s.if>
                                <@s.if test="%{#dsv.dbSource =='BarcodeHGU133plus2'}">
                                    <a target="_blank" href="http://barcode.luhs.org">
                                        <img style="width:200px" src="${base}/images/logo/barcode_250.png" alt="Barcode HGU133plus2"/> HGU133plus2
                                    </a>
                                </@s.if>
                            </td>
                            <td width="20%">Version No. ${dsv.versionNo?c}</td>
                            <td class="35%">Imported Time: ${dsv.createdTime}</td>
                        </tr>
                    </@s.iterator>
                </table>
            </div>
        </@s.if>
        <@s.else>
            <br/>

            <div class="dsv_title">The Sources of Chromosome ${selectedChrom}:</div>
            <div class="none_ds_version">
                Not Available Now.
            </div>
        </@s.else>
            <br/>

            <p>
                We soon hope to include data from the following data sources:
            </p>

            <p>
                <a target="_blank" href="http://www.peptideatlas.org">
                    <img style="width:200px" src="${base}/images/logo/PeptideAtlas_Logo.png"/>
                </a>
            </p>

            <p>
                If you manage other data sources that you believe should be included in TPB, or would like to
                suggest other data sources, please <a href="${base}/site/contactusinfo.jspx">contact
                us</a>.
            </p>

            <div class="blank_separator"></div>
            <br/>
        </div>
    </div>
</div>
<#include "../template/footer.ftl"/>
</body>
</html>
