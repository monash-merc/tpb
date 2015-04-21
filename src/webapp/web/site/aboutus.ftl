<#include  "../template/ftl_header.ftl" />
<!DOCTYPE html PUBLIC "-//W3C//DTD XHTML 1.0 Transitional//EN"
        "http://www.w3.org/TR/xhtml1/DTD/xhtml1-transitional.dtd">
<html xmlns="http://www.w3.org/1999/xhtml" xmlns="http://www.w3.org/1999/html">
<head>
    <title>About Us</title>
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
        <div class="blank_separator"></div>
        <div class="page_title">About Us</div>
        <div class="display_inner_div">
            <p>
                The Proteome Browser (TPB) is a chromosome-centric portal to the human proteome
                offering its users an intuitive interface to interrogate and integrate
                existing and emerging proteomics data.
            </p>

            <p>
                It represents the combined work and dedication of many individuals and
                groups - primarily a team of Australian proteomics researchers and
                the Monash University eResearch Centre (MeRC).
            </p>

            <p>
                The project will evolve in keeping with the needs of the proteomic community.
                TPB would not have been possible without the financial support from the
                Australian National Data Service (ANDS). ANDS is supported by the Australian
                Government through the National Collaborative Research Infrastructure Strategy
                Program and the Education Investment Fund (EIF) Super Science Initiative.
            </p>

            <p>
                The following individuals have all played a major role in the creation
                and ongoing development of TPB:
            </p>

            <div width="100%" style="text-align: left">
                <div style="display: inline;margin: 5px 10px 0pt 0pt; float: left;">
                    <p>
                        <b>C-HPP Executive </b> <br/>
                        Prof. William Hancock <br/>
                        Prof. Young-Ki Paik
                    </p>

                    <p>
                        <b>Australia/New Zealand Chromosome 7 consortium </b> <br/>
                        Prof. Mark Baker <br/>
                        Dr. Bill Jordan
                    </p>

                    <p>
                        <b>Monash University </b> <br/>
                        Prof. Ian Smith <br/>
                        Prof. Edouard Nice <br/>
                        Mr. Robert Goode
                    </p>

                    <p>
                        <b>Monash eResearch Centre </b> <br/>
                        Mr. Anthony Beitz <br/>
                        Mrs. Anitha Kannan <br/>
                        Mr. Simon Yu <br/>
                        Mrs. Kim Linton <br/>
                        Mrs. Kathryn Unsworth<br/>
                        Mr. Sebastian Barney
                    </p>

                    <p>
                        <b>Australian National Data Service </b> <br/>
                        Dr. Jeff Christiansen
                    </p>
                </div>

                <div style="display: inline; margin-top: 5px; margin-right: auto; margin-bottom: 0px; text-align: left; float: right;">
                    <img src="${base}/images/AboutUs_Team2.gif" border="0" alt="Team"/>
                </div>
                <div class="blank_separator"></div>
            </div>
            <div style="clear: both;"></div>
            <div class="blank_separator"></div>
        </div>
        <div class="blank_separator"></div>
    </div>
</div>
<#include "../template/footer.ftl"/>
</body>
</html>
