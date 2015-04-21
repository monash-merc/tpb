<?xml version="1.0" encoding="utf-8"?>
<registryObjects xmlns="http://ands.org.au/standards/rif-cs/registryObjects"
                 xmlns:xsi="http://www.w3.org/2001/XMLSchema-instance"
                 xsi:schemaLocation="http://ands.org.au/standards/rif-cs/registryObjects http://services.ands.org.au/documentation/rifcs/1.2.0/schema/registryObjects.xsd">
    <registryObject group="${TLDatasetGroupName}">
        <key>${TLDatasetIdentifierKey}</key>
        <originatingSource>${TLDatasetOriginatingSrc}</originatingSource>
        <collection type="dataset">
            <identifier type="local">${TLDatasetLocalKey}</identifier>
            <name type="primary">
                <namePart type="">
                    The Proteome Browser (TPB) - Protein Evidence dataset human chromosome ${NameChrom} - ${NameDbSource}
                </namePart>
            </name>
            <location>
                <address>
                    <electronic type="url">
                        <value>${TLDatasetURL}</value>
                    </electronic>
                </address>
            </location>
            <relatedObject>
                <key>MONe3aeaa1a-77de-4f69-a41c-af6f9710ee45</key>
                <relation type="isManagedBy"/>
            </relatedObject>
            <relatedObject>
                <key>MONebcb3d9b-324f-47d5-9591-a6b1414823f2</key>
                <relation type="isProducedBy"/>
            </relatedObject>
            <description type="full" xml:lang="en">
                &lt;p&gt;This dataset contains all currently published protein existence (evidence) for human
                chromosome ${DescChrom} sourced through ${DescDbSource} of the Proteome
                Browser (TPB). The dataset is displayed as a colour-coded "traffic light" report, which
                signifies the quality of the underlying data for each gene and data type, where the X axis
                relates to chromosome ordered genes (i.e. proteins), and the Y axis to a hierarchy of Protein
                Existence data types.
                &lt;/p&gt;
                &lt;p&gt;
                TPB, the tool used to compile this dataset is an open-source interactive web browser, used
                as a portal for integrating and analysing data; drawing the data from numerous sources with
                the aim of mapping to and characterising the human genome.
                &lt;/p&gt;
            </description>
            <description type="rights" xml:lang="en">
                &lt;p&gt;http://www.proteomebrowser.org/tpb/site/termsofuse.jspx&lt;/p&gt;
            </description>
            <subject type="anzsrc-for">060109</subject>
            <subject type="anzsrc-for">110106</subject>
        </collection>
    </registryObject>
</registryObjects>
