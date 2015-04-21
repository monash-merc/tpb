<#include  "template/ftl_header.ftl" />
<!DOCTYPE html PUBLIC "-//W3C//DTD XHTML 1.0 Transitional//EN"
        "http://www.w3.org/TR/xhtml1/DTD/xhtml1-transitional.dtd">
<html xmlns="http://www.w3.org/1999/xhtml">
<head>
    <title>Server Internal Error</title>
<#include "template/header.ftl" />
    <script>
        function jump() {
            var protocol = window.location.protocol;
            var host = window.location.host;
            var pathArray = window.location.pathname.split('/');
            location.href = protocol + "//" + host + "/" + pathArray[1] + "/home.jspx";
        }
    </script>
</head>
<body onload="setTimeout('jump()', 5000)">
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
        <div class="blank_separator"></div>
        <div class="display_inner_div">
            <br/>
            <br/>
            <br/>
            <div class="redirect_div">
                <div class="redirect_inner_div">
                    <br/>
                <span class="redirect_title"><img src="${base}/images/stop.png" border="0" align="middle"/> <b>Server Internal Error.</b></span>
                    <br/>
                    <br/>
                    <span class="redirect_msg">After a few seconds, the page will redirect to the home page ...</span>
                    <br/>
                    <br/>
                <span class="redirect_hints">
                    Problems with the redirect? Please use this <a href='${base}/home.jspx'>direct link</a>.
                </span>
                    <br/>
                    <br/>
                    <div style="clear:both"></div>
                </div>
            </div>

            <br/>
            <br/>
            <br/>
            <br/>
            <br/>
            <br/>

        </div>
    </div>
</div>
<#include "template/footer.ftl"/>
</body>
</html>
