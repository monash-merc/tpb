<#include  "../template/ftl_header.ftl" />
<!DOCTYPE html PUBLIC "-//W3C//DTD XHTML 1.0 Transitional//EN"
        "http://www.w3.org/TR/xhtml1/DTD/xhtml1-transitional.dtd">
<html xmlns="http://www.w3.org/1999/xhtml">
<head>
    <title>Documentations</title>
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
        <div class="page_title">Documentation</div>
        <div class="display_inner_div">
            <p>
                We’re currently working on the full documentation and user guide. In the meantime,
                please view the interactive chromosome reports by selecting a chromosome in the drop-down menu in the
                top right corner or using the search panel on the <a href="../home.jspx">home page</a>. Also check the
                <a href="../site/faq.jspx">FAQs </a> for information about common tasks and functionality.
            </p>

            <p>
                The primary report is a matrix of chromosome-ordered genes and expandable data-types. The level of
                evidence for each gene/data-type combination is signified by a traffic light colour system, with green
                being highly reliable evidence, yellow representing reasonable evidence, red demonstrating some evidence
                is available or black suggesting there is no available evidence. Details for each data-type can be
                viewed in a pop-up by selecting the data-type label. The data-types are hierarchical and thus can be
                expanded to display underlying data-types.
            </p>

            <p>
                Selection of any individual traffic light will launch the secondary report that provides details of the
                individual pieces of evidence in a source dependent manner.
                This is also hierarchical and provides links out to the original data source from which the evidence was
                sourced.
            </p>

            <p>
                A summary of the traffic light report is available by selecting the "view summary report" link directly
                below the primary traffic light report. This a tabulated overview of the data in the traffic light
                (including any applied filters), showing the number of genes in each evidence category for each data
                type. Selecting a row in the summary report will update the pie chart on the right.
            </p>

            <p>
                Preliminary documentation regarding the data types implemented in TPB and the mappings from source
                databases is available <a href="${base}/site/dtmapping.jspx">here</a>
            </p>

            <div class="doc_sub">
                <ul id="toc">
                    <!--<li>User Guide: download PDF format <a href="${base}/site/ddoc.jspx?fname=UserGuide.pdf">PDF</a>
                    </li>-->
                    <li><a href="${base}/site/dtmapping.jspx">Data Types and Mapping</a> <!--or download PDF format <a
                            href="${base}/site/ddoc.jspx?fname=DataTypesAndMappings.pdf">PDF</a>--></li>
                </ul>
            </div>

            <p>
                TPB is being developed to assist in the chromosome-centric Human Proteome Project (C-HPP). For more
                information about this global initiative, please visit the <a
                    href="http://www.c-hpp.org" target="_blank">C-HPP webpage</a>.
            </p>

            <p>
                We recommend using Google Chrome, Firefox and Internet Explorer IE8.0+.
            </p>


            <div class="blank_separator"></div>
        </div>
    </div>
</div>
<#include "../template/footer.ftl"/>
</body>
</html>
