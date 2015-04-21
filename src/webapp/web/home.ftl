<#include  "template/ftl_header.ftl" />
<!DOCTYPE html PUBLIC "-//W3C//DTD XHTML 1.0 Transitional//EN"
        "http://www.w3.org/TR/xhtml1/DTD/xhtml1-transitional.dtd">
<html xmlns="http://www.w3.org/1999/xhtml">
<head>
    <title>TPB Home</title>
<#include "template/header_jquery.ftl" />
    <script type="text/javascript">
        $(document).ready(function () {
            var rssFeedDiv = $('#tpb_rssfeed');
            if (rssFeedDiv != null) {
                loadRssFeeds(rssFeedDiv);
            }
        });
    </script>
</head>
<body>
<div class="blank_separator"></div>
<#include "template/navigation.ftl" />
<div class="display_main_container">
<@s.if test="%{#session.authentication_flag =='authenticated'}">
    <!-- sub-nav bar -->
    <div class="subnav_section">
        <#include "template/user_nav.ftl"/>
    </div>
    <!-- end of sub-nav bar -->
</@s.if>
    <div class="display_middle_div">
        <div class="blank_separator"></div>
        <div class="blank_separator"></div>
        <div class="page_title">Home</div>
        <div class="display_inner_div">
            <div class="inner_left">
                <p>
                    Welcome to The Proteome Browser (TPB). This web portal brings together data and information about
                    human proteins from a number of sources and presents them in a gene- and chromosome-centric,
                    interactive format.
                </p>

                <p>
                    Initially established to support the Human Proteome Organisation's (HUPO) Chromosome-centric Human
                    Proteome Project (C-HPP), this resource is currently in phase 2 of development; hence several
                    aspects are still in a draft version to demonstrate its potential and functionality. To view the
                    interactive prototype report, select a chromosome from the drop down menu in the top right corner or
                    below.
                </p>

                <div class="home_tl_explore">
                <#include "tl/simple_cond.ftl" />
                </div>
                <div style="clear:both"></div>
                <p>
                    TPB is an initiative of the Australia/New Zealand Chromosome 7 consortium of the <a
                        href="http://www.c-hpp.org" target="_blank">Chromosome-centric
                    Human Proteome Project (C-HPP)</a>.
                </p>

                <p>
                    TPB is currently being developed by the Monash University e-Research Centre <a
                        href="http://www.monash.edu/eresearch">(MeRC)</a>. The initial phase of development was funded
                    by the <a href="http://www.ands.org.au">Australian National Data Service (ANDS)</a>.
                </p>

                <p>
                    An important concept of this resource is that it is developed in close collaboration with the global
                    proteomics community. We therefore encourage your involvement and input. For more information or to
                    keep up to date with developments please check the <a target="_blank"
                                                                          href=http://www.ozhupohpp7.com/home>Project
                    Wiki</a> or join the
                    <a href="https://groups.google.com/forum/?hl=en&fromgroups#%21forum/theproteomebrowserproject"
                       target="_blank">discussion group</a>. To provide suggestions/feedback please <a
                        href="${base}/site/contactusinfo.jspx">contact us</a>.
                </p>

                <div class="blank_separator"></div>

                <br/>
                <br/>
                <br/>
                <br/>
                <br/>
                <br/>

            </div>
            <div class="inner_right">
            <#include "template/rss_feeds.ftl" />
            </div>
            <div style="clear: both;"/>
        </div>
    </div>
</div>
<#include "template/footer.ftl"/>
</body>
</html>
