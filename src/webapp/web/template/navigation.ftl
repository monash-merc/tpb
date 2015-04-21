<div class="nav_out_section">
    <div class="nav_middle_section">
        <div class="nav_inner_top">
            <div class="logo_div">
                <img src="${base}/images/logo/tpb_logo50.png" alt=""/>
            </div>
            <div class="chrom_exp_div">
            <@s.form action="trafficlight.jspx" namespace="/tl" method="post">
                <@s.select name ="tlSearchBean.selectedChromType"  headerKey="tlSearchBean.selectedChromType" list ="chromTypes"  cssClass="select_norm"/>
                <input type="submit" name="explore" value="Explore" class="input_button" />
            </@s.form>
            </div>
            <div style="clear:both"></div>
        </div>

        <div class="nav_inner_bottom">
            <div class="left_main_nav">
                <a href="${base}/home.jspx">Home</a>
                <a href="${base}/site/docs.jspx">Documentation</a>
                <a href="${base}/site/faq.jspx">FAQ</a>
                <a href="${base}/site/sources.jspx">Sources</a>
                <a href="${base}/site/contribute.jspx">Contributors</a>
                <a href="${base}/site/aboutus.jspx">About Us</a>
                <a href="${base}/site/contactusinfo.jspx">Contact Us</a>
            </div>
            <div class="right_main_nav">
                <!-- a href="#">Sign In</a> <a href="#">Join Now</a -->
            </div>
            <div style="clear:both"></div>
        </div>

    </div>
    <div style="clear:both"></div>
</div>

<div style="clear:both"></div>