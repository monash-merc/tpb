<#include  "../template/ftl_header.ftl" />
<!DOCTYPE html PUBLIC "-//W3C//DTD XHTML 1.0 Transitional//EN"
        "http://www.w3.org/TR/xhtml1/DTD/xhtml1-transitional.dtd">
<html xmlns="http://www.w3.org/1999/xhtml" xmlns="http://www.w3.org/1999/html">
<head>
    <title>Contributors</title>
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
        <div class="page_title">Contributors</div>
        <div class="display_inner_div">
            <p>
                In addition to the development team (see <a
                    href="http://hpbdev.its.monash.edu.au/tpb/site/aboutus.jspx">About us</a>), we would like to
                acknowledge the following people for their assistance in developing and providing access to data for
                TPB:
            <ul>
                <li>Bill Hancock, for on-going support, introductions to key international data
                    sources and stakeholders, and providing direct communication with HUPO.
                </li>


                <li>Amos Bairoch and the neXtProt team for access to the wealth of information in the
                    neXtProt database, particularly Pascale Gaudet and Lydie Lane for technical assistance.
                </li>


                <li>Ron Beavis for access to GPM and assistance translating the data.</li>

                <li>Mathias Uhlen and Emma Lundberg for access to The Human Protein Atlas.</li>


                <li>Young-Ki Paik and Seul-Ki Jeong for an initial traffic light prototype and ongoing collaboration.
                </li>

                <li>Marc Wilkins and Natalie Twine from UNSW for guidance and advice, particularly with reference to
                    transcript data integration
                </li>

                <li>Attendees at TPB workshops held at Lorne Proteomics Symposium (Lorne, Victoria, Feb 2012 and 2013)
                    and 3rd Australian & New Zealand Human Proteome Project Workshop (Macquarie University, NSW, March
                    2012).
                </li>
            </ul>
            </p>
            <div class="blank_separator"></div>

            <br/>
            <br/>
            <br/>
            <br/>
            <br/>
            <br/>
        </div>
    </div>
</div>
<#include "../template/footer.ftl"/>
</body>
</html>
