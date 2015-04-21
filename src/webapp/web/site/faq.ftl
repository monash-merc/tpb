<#include  "../template/ftl_header.ftl" />
<!DOCTYPE html PUBLIC "-//W3C//DTD XHTML 1.0 Transitional//EN"
        "http://www.w3.org/TR/xhtml1/DTD/xhtml1-transitional.dtd">
<html xmlns="http://www.w3.org/1999/xhtml">
<head>
    <title>FAQ</title>
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
<div class="page_title">FAQ</div>
<div class="display_inner_div">
<div class="sub_b_title">
    Table of contents
</div>
<ul id="toc">
    <li><a href="#section1">Overview/Introduction</a></li>
    <li><a href="#section2">How do I view a chromosome report?</a></li>
    <li>
        <a href="#section3">How do I filter the chromosome report:</a>
        <ul id="toc_small">
            <li><a href="#section3.1">For missing proteins?</a></li>
            <li><a href="#section3.2">For specific levels of evidence?</a></li>
            <li><a href="#section3.3">For a list of specific genes?</a></li>
            <li><a href="#section3.4">For a chromosome region?</a></li>
        </ul>
    </li>
    <li><a href="#section4">How can I get a summary of all the traffic light evidence?</a></li>
    <li><a href="#section5">What data sources are used to generate the report and how often are they
        updated?</a></li>
    <li><a href="#section6">What do each of the data types and colours refer to in the traffic light
        report?</a></li>
    <li><a href="#section7">How are the colour levels determined for parent data types?</a></li>
    <li><a href="#section8">Why are some tracks not visible when I expand the traffic light display?</a>
    </li>
    <li><a href="#section9">Why are some genes named Unknown?</a></li>
    <li><a href="#section10">Can I download the traffic light data?</a></li>
</ul>
<div class="sub_b_title" id="section1">
    Overview/Introduction
</div>
<div class="paragraph_div">
    This page contains answers to several frequently asked questions regarding usage of The Proteome
    Browser. The user guide includes detailed information about the system, including descriptions of
    the display, functionality, parsing rules, etc.
</div>
<div class="sub_b_title" id="section2">
    How do I view a chromosome report?
</div>
<div class="paragraph_div">
    From the home page, select the chromosome of interest in the drop down list and select Explore.
    The maximal list of data sources and the most recent version is selected to be the default. On the
    traffic light report additional filters may be applied by selecting the “more options” link near the
    Explore button. These filters include: specific chromosome regions, specific genes or genes with
    specific levels of evidence.
</div>
<div class="sub_b_title" id="section3">
    How do I filter the chromosome report:
</div>
<div class="sub_norm_title" id="section3.1">
    For missing proteins?
</div>
<div class="paragraph_div">
    View the traffic light report for a chromosome and view the advanced filter options by selecting
    “more options” near the Explore button. Select the PE (Protein Expression) data type from the TPB data
    type
    drop down, select the black and red data level check boxes and select Explore. Note that while red
    traffic
    lights show the presence of some data, it is far from definitive and therefore should be considered in
    the missing proteins.
</div>

<div class="sub_norm_title" id="section3.2">
    For specific levels of evidence?
</div>
<div class="paragraph_div">
    Similar to searching for missing proteins, any combination of data type and colour level may be used
    as criteria to filter the traffic light report. Under “more options” on the traffic light report,
    numerous
    TPB data types and any combination of colour levels for each data type may be used as the filter
    criteria. Note that the criteria are analysed as an OR within a data type and as an AND between
    data types, for example, if the colours green and yellow are selected for PE MS and the colour
    black is selected for PE ANTI, then the criteria will filter genes that have either green OR yellow
    Mass Spectrometry evidence AND black Antibody evidence. If all or no colour levels are selected
    for a data type, that filter criteria is ignored. Data types may be removed from the filter criteria by
    selecting the small red x to the right of the colour criteria.
</div>

<div class="sub_norm_title" id="section3.3">
    For a list of specific genes?
</div>
<div class="paragraph_div">
    A list of gene symbols or accessions may be typed or pasted into the Gene List box, by ensuring
    the drop box is appropriately selected. Note that currently the system does not look for gene
    synonyms; therefore the symbols used must correspond to the displayed gene name. Also, currently
    the system only recognises Ensembl gene accessions (i.e. starting with ENSG). If a single entry is
    provided in the Gene List it is treated as a (pre and post) wild card. For example, filtering for a gene
    symbol of “TMEM” will return all genes with TMEM in the name. This can be useful to find members
    of gene families.
</div>

<div class="sub_norm_title" id="section3.4">
    For a chromosome region?
</div>
<div class="paragraph_div">
    By inserting start and end base pair indexes into the relevant boxes, specific regions of the
    chromosome can be filtered. These indexes are inclusive; meaning an index of 750000 will pick up a
    gene that spans the 750000th base pair. Leaving the start or end index as 0 will filter from the start
    or
    end of the chromosome respectively.
</div>
<div class="sub_b_title" id="section4">
    How can I get a summary of all the traffic light evidence?
</div>
<div class="paragraph_div">
    On the right hand side, directly below the traffic light report is a link to "view summary report".
    Selecting this will open the summary report pane, that displays a table summarizing the number of genes
    with each level of evidence for each data type. A pie chart is provided to visually represent the
    proportion of genes with each evidence level for the highlighted data type. Selecting any row in the
    table will update the pie chart to the selected data type.
</div>
<div class="sub_b_title" id="section5">
    What data sources are used to generate the report and how often are they updated?
</div>
<div class="paragraph_div">
    A full list of current data sources is available on the <a href="${base}/site/sources.jspx">Data
    Sources</a> tab. Currently, data from neXtProt,
    GPM, Human Protein Atlas and the Gene Expression Barcode are used to compile the evidence. Each source
    is updated as soon as
    a new version is detected by the system and the most recent version and import date for each data
    source is viewable on the <a href="${base}/site/sources.jspx">Data Sources</a> tab. Additionally the
    different versions from each database
    can be viewed on the traffic light report page by selecting a single data source of interest and
    viewing the versions available in the drop box. Note that for combinations of data sources, a version
    of the traffic light is created each time one or more of the data sources is updated, thus if multiple
    data sources are selected, there will be versions corresponding to the versions of each selected data
    source.
</div>

<div class="sub_b_title" id="section6">
    What do each of the data types and colours refer to in the traffic light report?
</div>
<div class="paragraph_div">
    A pop-up with a description of the data type and a brief definition of the colour coding is available by
    selecting the data type name on the traffic light report. For detailed information of the parsing rules
    from the data sources to TPB data types, please refer to the <a href="${base}/site/dtmapping.jspx">data
    types and mapping information</a> on the <a href="${base}/site/docs.jspx">Documentation</a> tab.
</div>

<div class="sub_b_title" id="section7">
    How are the colour levels determined for parent data types?
</div>
<div class="paragraph_div">
    Currently all data types inherit the highest level evidence from child data types. For example if a
    gene has red Mass spectrometry evidence (PE MS) and green Antibody evidence (PE ANTI), the
    Protein Expression traffic light (PE) will be green.
</div>
<div class="sub_b_title" id="section8">
    Why are some tracks not visible when I expand the traffic light display?
</div>
<div class="paragraph_div">
    Due to the size of the database and the large number of genes in some queries, the tracks may
    take some time to load. A progress wheel shows when data is still being loaded. If some tracks do
    not appear, please wait and they will render. If they don’t appear after a reasonable time (up to
    a minute), try collapsing and reopening any sub tracks. Do not refresh the screen, except as a last
    resort, as this will restart the loading.
</div>
<div class="sub_b_title" id="section9">
    Why some genes are named “Unknown”?
</div>
<div class="paragraph_div">
    <p>Some evidence from the data sources do not cross reference a known gene or don’t include a
        gene name. At this stage full sequence comparison is not performed to identify identical gene
        sequences and therefore these data are assigned to an unknown gene and placed at the end of the
        chromosome if the chromosome is known but no gene location is available. Genes that aren't assigned
        to a chromosome are placed in a "chromosome Other" until an assignment can be made.</p>

    <p>These data are often compiled into known genes (with correct locations)
        with updates of the underlying data sources. We will endeavour to assign a sensible gene name to
        these entries in the near future, probably based on an accession or name from the data source that
        generated the data in TPB.</p>
</div>
<div class="sub_b_title" id="section10">
    Can I download the traffic light data?
</div>
<div class="paragraph_div">
    <p>
        Yes. The data representing the traffic light can be downloaded from the "export traffic lights"
        link below the traffic light. This will provide a csv file that contains all genes visible in the
        traffic light with Ensembl gene accessions and the colour level for each data type. Additionally,
        the file contains a header section describing any filters applied and the version of the traffic
        light.
    </p>

    <p>
        To view this file in Excel, open the document, select column A and select "Text to Columns..." from
        the data menu. Select "Delimited" on the first page of the wizard. Select "Tab" and ensure that
        double quotes are used as the text qualifier on the second page. On the final page, select "Text"
        as the format for column A to ensure gene names are not converted to dates.</p>

    <p>
        The data may also be viewed as the traffic lights by selecting all the data cells (those with values
        1-4) and using conditional formatting from the format menu. If there is at least one 1 and one 4 in
        the data matrix, Excel's in-built "4 Traffic Lights" icon set will correctly apply icons to each
        cell.
    </p>

    <p>
        Note that the layout is transposed compared to the original traffic light. This can be altered in Excel by
        selecting all the data (including gene names and accessions and the column titles), copying it and
        using "Paste Special" with "Transpose" selected.
    </p>
</div>

<br/>

<div class="paragraph_div">
    <p>
        If you have a question, please click <a href="${base}/site/contactusinfo.jspx">Contact Us</a>.
    </p>
</div>

<br/>
</div>
</div>
</div>
<#include "../template/footer.ftl"/>
</body>
</html>
