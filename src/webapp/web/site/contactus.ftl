<#include  "../template/ftl_header.ftl" />
<!DOCTYPE html PUBLIC "-//W3C//DTD XHTML 1.0 Transitional//EN"
        "http://www.w3.org/TR/xhtml1/DTD/xhtml1-transitional.dtd">
<html xmlns="http://www.w3.org/1999/xhtml">
<head>
    <title>Contact Us</title>
<#include "../template/header_jquery.ftl" />
    <script type="text/javascript">
        function refresh() {
            document.getElementById("imagevalue").src = '${base}/security/securityCode.jspx?now=' + new Date();
        }
    </script>
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
        <div style="clear: both;"></div>
        <div class="page_title">Contact Us</div>
        <div class="display_inner_div">
        <@s.if test="%{successActMsg != null}">
            <div class="blank_separator"></div>
            <#include "../template/success_act_msg.ftl" />
        </@s.if>

            <p>
                TPB welcomes your suggestions or comments. Using the form below, you can share your suggestions or
                contact our researchers or development team. If you have a question about TPB, we may have answered it on
                our <a href="${base}/site/faq.jspx">FAQ</a> page.
            </p>

            <p>
                Also don’t forget to check the <a target="_blank" href="http://www.ozhupohpp7.com">wiki</a> for project
                updates.
            </p>

            <div class="p_title_div">Contact Us</div>
        <@s.form action="contactus.jspx" namespace="/site" method="post">
            <div class="form_data_div">
                <div class="blank_separator"></div>

                <div class="field_label_div">
                    Your Name:
                </div>
                <div class="field_value_div">
                    <@s.textfield name="contactName" cssClass="input_field_normal"/>  * (user name required)
                </div>
                <div class="field_label_div">
                    Your Email
                </div>
                <div class="field_value_div">
                    <@s.textfield name="contactEmail" cssClass="input_field_normal"/>  * (user email required)
                </div>
                <div class="field_label_div">
                    Your Phone
                </div>
                <div class="field_value_div">
                    <@s.textfield name="contactPhone" cssClass="input_field_normal"/>  * (user phone optional)
                </div>

                <div class="field_label_div">
                    Subject:
                </div>
                <div class="field_value_div">
                    <@s.textfield name="subject" cssClass="input_field_normal"/>  * (subject required)
                </div>

                <div class="field_label_div">
                    Messages:
                </div>
                <div class="field_value_div">
                    <@s.textarea  name="message" cols="70" rows="8" cssClass="input_textarea" /> * (message
                    required)
                </div>

                <div class="field_label_div">Word Verification:</div>
                <div class="field_value_div">
                    <@s.textfield name="securityCode" cssClass="input_field_normal" />  *
                    (<@s.text name="security.code.spec" />
                    )
                </div>
                <div class="blank_separator"></div>
                <div class="field_label_div">&nbsp;</div>
                <div class="security_code">
                    <img src="${base}/security/securityCode.jspx?now=new Date()" border="0" id="imagevalue"
                         name="imagevalue"/> &nbsp;
                    <a href="#" onclick="refresh()">
                        <img src="${base}/images/refresh.png" class="security_code_img"/>
                        can't read this?
                    </a>
                </div>

                <div style="clear:both"></div>
            </div>
            <div class="form_submit_div">
                <div class="field_label_div">&nbsp;</div>
                <div class="field_value_div">
                    <@s.submit value="Submit" cssClass="input_button" />
                    &nbsp; <@s.reset value="Clear" cssClass="input_button" />
                </div>
            </div>
        </@s.form>
            <div class="blank_separator"></div>
            <br/>
        </div>
    </div>
</div>
<#include "../template/footer.ftl"/>
</body>
</html>
