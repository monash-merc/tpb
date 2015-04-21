/*
 * Copyright (c) 2011-2013, Monash e-Research Centre
 * (Monash University, Australia)
 * All rights reserved.
 *
 * Redistribution and use in source and binary forms, with or without
 * modification, are permitted provided that the following conditions are met:
 * 	* Redistributions of source code must retain the above copyright
 * 	  notice, this list of conditions and the following disclaimer.
 * 	* Redistributions in binary form must reproduce the above copyright
 * 	  notice, this list of conditions and the following disclaimer in the
 * 	  documentation and/or other materials provided with the distribution.
 * 	* Neither the name of the Monash University nor the names of its
 * 	  contributors may be used to endorse or promote products derived from
 * 	  this software without specific prior written permission.
 *
 * This program is distributed in the hope that it will be useful, but WITHOUT ANY WARRANTY;
 * without even the implied warranty of MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.
 * See the GNU Affero General Public License for more details.
 *
 * THIS SOFTWARE IS PROVIDED BY THE COPYRIGHT HOLDERS AND CONTRIBUTORS "AS IS" AND ANY
 * EXPRESS OR IMPLIED WARRANTIES, INCLUDING, BUT NOT LIMITED TO, THE IMPLIED
 * WARRANTIES OF MERCHANTABILITY AND FITNESS FOR A PARTICULAR PURPOSE ARE
 * DISCLAIMED. IN NO EVENT SHALL THE COPYRIGHT HOLDER OR CONTRIBUTORS BE LIABLE FOR ANY
 * DIRECT, INDIRECT, INCIDENTAL, SPECIAL, EXEMPLARY, OR CONSEQUENTIAL DAMAGES
 * (INCLUDING, BUT NOT LIMITED TO, PROCUREMENT OF SUBSTITUTE GOODS OR SERVICES;
 * LOSS OF USE, DATA, OR PROFITS; OR BUSINESS INTERRUPTION) HOWEVER CAUSED AND
 * ON ANY THEORY OF LIABILITY, WHETHER IN CONTRACT, STRICT LIABILITY, OR TORT
 * (INCLUDING NEGLIGENCE OR OTHERWISE) ARISING IN ANY WAY OUT OF THE USE OF THIS
 * SOFTWARE, EVEN IF ADVISED OF THE POSSIBILITY OF SUCH DAMAGE.
 *
 * You should have received a copy of the GNU Affero General Public License along with
 * this program. If not, see <http://www.gnu.org/licenses/>.
 */

package edu.monash.merc.system.parser.nextprot;

import edu.monash.merc.common.name.ColorType;
import edu.monash.merc.common.name.DataType;
import edu.monash.merc.common.name.DbAcType;
import edu.monash.merc.common.name.TLLevel;
import edu.monash.merc.dto.*;
import edu.monash.merc.exception.DMXMLParserException;
import org.apache.commons.lang.StringUtils;
import org.apache.log4j.Logger;
import org.jaxen.jdom.JDOMXPath;
import org.jdom.Attribute;
import org.jdom.Document;
import org.jdom.Element;
import org.jdom.input.SAXBuilder;

import java.io.File;
import java.io.FileInputStream;
import java.io.InputStream;
import java.util.ArrayList;
import java.util.List;

@Deprecated
/**
 * NxXMLParser class which parses the nextprot xml file. using this parser is not recommended, please use NxXMLSAXParser instead
 *
 * @author Simon Yu
 *         <p/>
 *         Email: xiaoming.yu@monash.edu
 * @version 1.0
 * @since 1.0
 *        <p/>
 *        Date: 24/02/12 1:03 PM
 */
public class NxXMLParser {

    private static String PROTEIN_PATH = "//proteins/protein";

    private static String ATTR_PROTEIN_NEXTPROT_AC = "uniqueName";

    private static String ELE_PROTEIN_EXISTENCE = "proteinExistence";

    private static String ATTR_PROTEIN_EXISTENCE_VALUE = "value";

    private static String ELE_NEXTPROT_PROTEIN_NAMES = "proteinNames";

    private static String ELE_NEXTPROT_GENE_NAMES = "geneNames";

    private static String ELE_NEXTPROT_CHROMOSOMAL_LOCATIONS = "chromosomalLocations";

    private static String ELE_NEXTPROT_CHROMOSOMAL_LOCATION = "chromosomalLocation";

    private static String ATTR_NEXTPROT_CHROMOSOME = "chromosome";

    private static String ATTR_NEXTPROT_CHROMOSOMAL_BAND = "band";

    private static String ATTR_NEXTPROT_CHROMOSOMAL_STRAND = "strand";

    private static String ATTR_NEXTPROT_CHROMOSOMAL_ACCESSION = "accession";

    private static String ELE_NEXTPROT_IDENTIFIERS = "identifiers";

    private static String ELE_NEXTPROT_IDENTIFIER = "identifier";

    private static String ATTR_NEXTPROT_IDENTIFIER_TYPE = "type";

    private static String ATTR_NEXTPROT_IDENTIFIER_NAME = "name";

    private static String ATTR_NEXTPROT_IDENTIFIER_DATABASE = "database";

    private static String ELE_ENTITY_NAME = "entityName";

    private static String ATTR_ENTITY_NAME_MAIN = "isMain";

    private static String ELE_ENTITY_NAME_VALUE = "value";


    private static String ELE_ANNOTATIONS = "annotations";

    private static String ELE_ANNOTATION_LIST = "annotationList";

    private static String ATTR_CATEGORY = "category";

    private static String ELE_ANNOTATION = "annotation";

    private static String ATTR_UNIQUE_NAME = "uniqueName";

    private static String ATTR_QUALITY_QUALIFIER = "qualityQualifier";

    private static String QUALITY_QUALIFIER_GOLD = "GOLD";

    private static String QUALITY_QUALIFIER_SILVER = "SILVER";

    private static String QUALITY_QUALIFIER_BRONZE = "BRONZE";

    private static String ELE_CV_TERM = "cvTerm";

    private static String ATTR_ACCESSION = "accession";

    private static String ELE_CV_NAME = "cvName";

    //annotation description element
    private static String ELE_DESCRIPTION = "description";

    private static String ELE_VARIANT = "variant";

    private static String ATTR_ORIGINAL = "original";

    private static String ATTR_VARIATION = "variation";

    private static String ELE_PROPERTIES = "properties";

    private static String ELE_PROPERTY = "property";

    private static String ATTR_PROPERTY_AC = "accession";

    private static String ATTR_PROPERTY_NAME = "propertyName";

    private static String ATTR_PROPERTY_VALUE = "value";

    private static String ELE_EXP_EVIDENCES = "experimentalEvidences";

    private static String ATTR_EXP_EVI_METHOD = "method";

    private static String ATTR_EXP_EVI_RESULT = "result";

    private static String ELE_EVIDENCES = "evidences";

    private static String ELE_EVIDENCE = "evidence";

    private static String ATTR_IS_NEGATIVE = "isNegative";

    private static String ATTR_QUALIFIER_TYPE = "qualifierType";

    private static String ATTR_RESOURCE_REF = "resourceRef";

    private static String ATTR_RESOURCE_ASSOC_TYPE = "resourceAssocType";

    private static String ELE_ISO_FORM_SPECIFICITY = "isoformSpecificity";

    private static String ELE_ISO_FORM_ANNOT = "isoformAnnot";

    private static String ELE_POSITIONS = "positions";

    private static String ATTR_ISO_FORM_REF = "isoformRef";

    private static String ELE_POSITION = "position";

    private static String ATTR_POSITION_FIRST = "first";

    private static String ATTR_POSITION_FIRST_STATUS = "firstStatus";

    private static String ATTR_POSITION_LAST = "last";

    private static String ATTR_POSITION_LAST_STATUS = "lastStatus";

    //XRef Elements
    private static String ELE_XREFS = "xrefs";

    private static String ELE_XREF = "xref";

    private static String ATTR_XREF_DATABASE = "database";

    private static String ATTR_XREF_CATEGORY = "category";

    private static String ATTR_XREF_ACCESSION = "accession";

    private static String ATTR_XREF_ID = "id";

    private static String ELE_XREF_URL = "url";


    private SAXBuilder parser = new SAXBuilder();

    private Logger logger = Logger.getLogger(this.getClass().getName());

    @SuppressWarnings("unchecked")
    public List<NXEntryBean> parseNextProtXML(InputStream xmlStream) {
        //for each protein, parsed by protein path
        JDOMXPath proteinPath = null;
        Document xmlDoc = null;
        List<Element> proteinElements = new ArrayList<Element>();

        //build the protein path
        try {
            proteinPath = new JDOMXPath(PROTEIN_PATH);
        } catch (Exception ex) {
            logger.error(ex);
            throw new DMXMLParserException(ex);
        }

        //create an empty NXEntryBean to store the parsed nextprot data
        List<NXEntryBean> nxEntryBeanList = new ArrayList<NXEntryBean>();

        try {
            //long startTime = System.currentTimeMillis();

            xmlDoc = this.parser.build(xmlStream);
            proteinElements = proteinPath.selectNodes(xmlDoc);

            System.out.println("NextProt total proteins size: " + proteinElements.size());
            //for each protein
            for (Element protein : proteinElements) {
                NXEntryBean nxEntryBean = new NXEntryBean();

                //get next ac id
                Attribute nextProtAc = protein.getAttribute(ATTR_PROTEIN_NEXTPROT_AC);
                if (nextProtAc != null) {
                    String nxAc = nextProtAc.getValue();
                    // System.out.println("====== nextprot accession : " + nxAc);
                    //set the nextprot accession
                    //create accession and db source for nextprot
                    AccessionBean nextprotAc = new AccessionBean();
                    nextprotAc.setAccession(nxAc);
                    nextprotAc.setAcType(DbAcType.NextProt.type());
                    nxEntryBean.setIdentifiedAccessionBean(nextprotAc);
                }
                //parse the gene information
                GeneBean geneBean = parseGene(protein);

                nxEntryBean.setGeneBean(geneBean);
                //set the db source name
                nxEntryBean.setDbSourceName(DbAcType.NextProt.type());

                //NextProt Accession

                AccessionBean accessionBean = nxEntryBean.getIdentifiedAccessionBean();
                String nxAccession = accessionBean.getAccession();

                //parse the dbsource and accession list
                List<DbSourceAcEntryBean> nxDbSourceAcEntryBeanList = parseDbSourceAc(protein, nxAccession);
                //set the dbsource and ac
                nxEntryBean.setDbSourceAcEntryBeans(nxDbSourceAcEntryBeanList);

                //parse the proteinExistence evidence - PEEvidence
                PEEvidenceBean peEvidenceBean = parsePEOthCurEvidence(protein, nxAccession);
               // nxEntryBean.setPeOthCurEvidenceBean(peEvidenceBean);


                // this parts only for prototyping purpose
                //parse the annotations
//                List<NXAnnEntryBean> nxAnnEntryBeans = parseAnnotationList(protein);
//
//                //set the nextprot annotations for each nextprot entry;
//                if (nxAnnEntryBeans.size() > 0) {
//                    nxEntryBean.setNxAnnEntryBeanList(nxAnnEntryBeans);
//                }
                //add the NXEntryBean into list
                nxEntryBeanList.add(nxEntryBean);

                //END

                //XRefs
                NXPeMsAntiEntryBean nxPeMsAntiEntryBean = parseNXPeMSAntiAnn(protein);
                if (nxPeMsAntiEntryBean != null) {
                    nxEntryBean.setNxPeMsAntiEntryBean(nxPeMsAntiEntryBean);
                }

            }


            long endTime = System.currentTimeMillis();
            //System.out.println("=====> NextProt total time: " + (endTime - startTime) / 1000 + "seconds");

        } catch (Exception ex) {
            logger.error(ex);
            throw new DMXMLParserException(ex);
        } finally {
            try {
                if (xmlStream != null) {
                    xmlStream.close();
                }
            } catch (Exception fex) {
                //ignore
            }
        }
        return nxEntryBeanList;
    }

    @SuppressWarnings("unchecked")
    private GeneBean parseGene(Element protein) {
        //create gene associated with this nextprot ac
        GeneBean geneBean = new GeneBean();
        //get the gene name
        Element genNamesEle = protein.getChild(ELE_NEXTPROT_GENE_NAMES);
        if (genNamesEle == null) {
            //set the gene name
            geneBean.setDisplayName(NXConts.UNKNOWN);
        } else {
            List<Element> geneNameEntryEles = new ArrayList<Element>();
            geneNameEntryEles = genNamesEle.getChildren(ELE_ENTITY_NAME);
            for (Element gnEntry : geneNameEntryEles) {
                if (gnEntry != null) {
                    Attribute isMainAttr = gnEntry.getAttribute(ATTR_ENTITY_NAME_MAIN);
                    if (isMainAttr != null) {
                        String isMainAttrValue = isMainAttr.getValue();
                        if (StringUtils.equalsIgnoreCase(isMainAttrValue, "true")) {
                            Element geneNameValueEl = gnEntry.getChild(ELE_ENTITY_NAME_VALUE);
                            if (geneNameValueEl != null) {
                                String geneDisplayName = geneNameValueEl.getText();
                                geneBean.setDisplayName(geneDisplayName);
                            }
                        }
                    }
                }
            }
        }

        //get the gene description
        Element proteinNamesEle = protein.getChild(ELE_NEXTPROT_PROTEIN_NAMES);
        if (proteinNamesEle != null) {

            List<Element> protNameEntryEles = new ArrayList<Element>();

            protNameEntryEles = proteinNamesEle.getChildren(ELE_ENTITY_NAME);
            for (Element protEntry : protNameEntryEles) {
                if (protEntry != null) {
                    Attribute isMainAttr = protEntry.getAttribute(ATTR_ENTITY_NAME_MAIN);
                    if (isMainAttr != null) {
                        String isMainAttrValue = isMainAttr.getValue();
                        if (StringUtils.equalsIgnoreCase(isMainAttrValue, "true")) {
                            Element proteinNameValueEl = protEntry.getChild(ELE_ENTITY_NAME_VALUE);
                            if (proteinNameValueEl != null) {
                                String preteinDesc = proteinNameValueEl.getText();
                                //System.out.println(" protein display name : " + preteinDesc);
                                //set the gene description
                                geneBean.setDescription(preteinDesc);
                            }
                        }
                    }
                }

            }
        }

        //get the chromosome name, gene band and strand values
        Element chromosomalLocationsEle = protein.getChild(ELE_NEXTPROT_CHROMOSOMAL_LOCATIONS);
        List<Element> chlocationElements = chromosomalLocationsEle.getChildren(ELE_NEXTPROT_CHROMOSOMAL_LOCATION);
//
        for (Element element : chlocationElements) {
            Attribute chromosome = element.getAttribute(ATTR_NEXTPROT_CHROMOSOME);
            if (chromosome != null) {
                String chromosomeValue = chromosome.getValue();
                //set the chromosome name
                geneBean.setChromosome(chromosomeValue);
            }
            Attribute band = element.getAttribute(ATTR_NEXTPROT_CHROMOSOMAL_BAND);
            if (band != null) {
                String bandValue = band.getValue();
                //set the band value
                geneBean.setBand(bandValue);
            }

            Attribute strand = element.getAttribute(ATTR_NEXTPROT_CHROMOSOMAL_STRAND);
            if (strand != null) {
                String strandValue = strand.getValue();
                //set the strand value
                geneBean.setStrand(strandValue);
            }

            Attribute accession = element.getAttribute(ATTR_NEXTPROT_CHROMOSOMAL_ACCESSION);
            if (accession != null) {
                String ensgAcValue = accession.getValue();
                //set the ensembl ensg accession value
                geneBean.setEnsgAccession(ensgAcValue);
            }
        }
        return geneBean;
    }
    @SuppressWarnings("unchecked")
    private List<DbSourceAcEntryBean> parseDbSourceAc(Element protein, String nxAc) {
        List<DbSourceAcEntryBean> nxDbSourceAcEntryBeanList = new ArrayList<DbSourceAcEntryBean>();
        Element identifiersEle = protein.getChild(ELE_NEXTPROT_IDENTIFIERS);
        List<Element> identifierElements = identifiersEle.getChildren(ELE_NEXTPROT_IDENTIFIER);
        for (Element element : identifierElements) {
            DbSourceAcEntryBean nxDbSourceAcEntryBean = new DbSourceAcEntryBean();
            DBSourceBean dbSourceBean = new DBSourceBean();
            AccessionBean accessionBean = new AccessionBean();

            Attribute type = element.getAttribute(ATTR_NEXTPROT_IDENTIFIER_TYPE);
            if (type != null) {
                String typeValue = type.getValue();
                //set accession type
                accessionBean.setAcType(typeValue);
            }

            Attribute name = element.getAttribute(ATTR_NEXTPROT_IDENTIFIER_NAME);
            if (type != null) {
                String nameValue = name.getValue();
                //set accession
                accessionBean.setAccession(nameValue);
            }

            Attribute database = element.getAttribute(ATTR_NEXTPROT_IDENTIFIER_DATABASE);
            if (database != null) {
                String databaseValue = database.getValue();
                dbSourceBean.setDbName(databaseValue);
            } else {
                dbSourceBean.setDbName(DbAcType.Unknown.type());
            }
            //set the accession bean
            nxDbSourceAcEntryBean.setAccessionBean(accessionBean);
            //set the dbsource bean
            nxDbSourceAcEntryBean.setDbSourceBean(dbSourceBean);
            //add the NXDbSourceActionEntryBean into list
            nxDbSourceAcEntryBeanList.add(nxDbSourceAcEntryBean);
        }
        //create accession and db source for nextprot
        AccessionBean nextprotAc = new AccessionBean();
        nextprotAc.setAccession(nxAc);
        nextprotAc.setAcType(DbAcType.NextProt.type());

        DBSourceBean nxDbs = new DBSourceBean();
        nxDbs.setDbName(DbAcType.NextProt.type());
        nxDbs.setPrimaryEvidences(true);

        DbSourceAcEntryBean nxDbSAcEntryBean = new DbSourceAcEntryBean();
        nxDbSAcEntryBean.setAccessionBean(nextprotAc);
        nxDbSAcEntryBean.setDbSourceBean(nxDbs);

        nxDbSourceAcEntryBeanList.add(nxDbSAcEntryBean);

        return nxDbSourceAcEntryBeanList;
    }

    private PEEvidenceBean parsePEOthCurEvidence(Element protein, String nxAc) {
        Element proteinExistenceEle = protein.getChild(ELE_PROTEIN_EXISTENCE);
        if (proteinExistenceEle != null) {
            Attribute proteinExistenceAttr = proteinExistenceEle.getAttribute(ATTR_PROTEIN_EXISTENCE_VALUE);
            if (proteinExistenceAttr != null) {
                PEEvidenceBean peOthCurEvidenceBean = new PEEvidenceBean();
                String proteinExistence = proteinExistenceAttr.getValue();
                //create a TPBDataTypeBean
                TPBDataTypeBean hpbDataTypeBean = new TPBDataTypeBean();
                //set the data type
                hpbDataTypeBean.setDataType(DataType.PE_OTH_CUR.type());
                //set the traffic lights level to 3
                hpbDataTypeBean.setLevel(TLLevel.TL3.level());
                //There is no color level 3
                //If it's protein level, then set color level into 4
                if (StringUtils.containsIgnoreCase(proteinExistence, NXConts.PE_PROTEIN_LEVEL)) {
                    peOthCurEvidenceBean.setColorLevel(ColorType.GREEN.color());
                } else if (StringUtils.containsIgnoreCase(proteinExistence, NXConts.PE_TRANSCRIPT_LEVEL)) {
                    //if it's transcript level, then set color level into 2 (original as 3)
                    //        : change the Color Level to Red
                    peOthCurEvidenceBean.setColorLevel(ColorType.RED.color());
                    //if it's homology or predicted or uncertian. or others, then set color level into 1
                    //rest of other value. then set color level into 1
                } else {
                    peOthCurEvidenceBean.setColorLevel(ColorType.BLACK.color());
                }
                peOthCurEvidenceBean.setTpbDataTypeBean(hpbDataTypeBean);
                peOthCurEvidenceBean.setEvidenceValue(proteinExistence);
                peOthCurEvidenceBean.setHyperlink(NXConts.PE_OTH_CUR_NX_BASE_URL + nxAc);
                return peOthCurEvidenceBean;
            }
        }
        return null;
    }

    @SuppressWarnings("unchecked")
    private NXPeMsAntiEntryBean parseNXPeMSAntiAnn(Element protein) {

        NXPeMsAntiEntryBean peMsAntiEntryBean = new NXPeMsAntiEntryBean();

        List<PEEvidenceBean> peMsEvidenceBeans = new ArrayList<PEEvidenceBean>();
        Element xrefsEle = protein.getChild(ELE_XREFS);
        if (xrefsEle != null) {

            //PE MS ANTI ANN url
            String peMsAntiURL = null;

            //PE MS ANTI
            int peMsAntiCounter = 0;
            List<Element> xrefElements = xrefsEle.getChildren(ELE_XREF);
            for (Element xrefEle : xrefElements) {
                //create a pe ms evidence object
                PEEvidenceBean peMsAnnEvidenceBean = new PEEvidenceBean();

                String xrefDatabase = null;
                String xrefCategory = null;
                String xrefAccession = null;
                String xrefId = null;
                String xrefUrl = null;
                //database
                Attribute xrefDbAtt = xrefEle.getAttribute(ATTR_XREF_DATABASE);
                if (xrefDbAtt != null) {
                    xrefDatabase = xrefDbAtt.getValue();

                }

                //category
                Attribute xrefCategAtt = xrefEle.getAttribute(ATTR_XREF_CATEGORY);
                if (xrefCategAtt != null) {
                    xrefCategory = xrefCategAtt.getValue();

                }

                //accession
                Attribute xrefAccessionAtt = xrefEle.getAttribute(ATTR_XREF_ACCESSION);
                if (xrefAccessionAtt != null) {
                    xrefAccession = xrefAccessionAtt.getValue();
                }

                //id
                Attribute xrefIdAtt = xrefEle.getAttribute(ATTR_XREF_ID);
                if (xrefIdAtt != null) {
                    xrefId = xrefIdAtt.getValue();
                }

                //url

                Element xrefUrlEle = xrefEle.getChild(ELE_XREF_URL);
                if (xrefEle != null) {
                    xrefUrl = xrefUrlEle.getTextNormalize();
                }


                if (StringUtils.isNotBlank(xrefDatabase) && (StringUtils.isNotBlank(xrefCategory))) {
                    //if category = Proteomic database and database = PRIDE
                    if (StringUtils.equalsIgnoreCase(xrefDatabase, NXConts.XREF_DB_PRIDE) && StringUtils.equalsIgnoreCase(xrefCategory, NXConts.XREF_CA_PROTEOMIC_DATABASES)) {
                        //set the color red
                        peMsAnnEvidenceBean.setColorLevel(ColorType.RED.color());
                        //set the link value
                        if (StringUtils.isNotBlank(xrefAccession)) {
                            peMsAnnEvidenceBean.setHyperlink(xrefUrl);
                        }
                        //added the evidence
                        String evidence = null;
                        evidence = xrefDatabase;
                        if (StringUtils.isNotBlank(xrefAccession)) {
                            evidence += " - " + xrefAccession;
                        }
                        if (StringUtils.isNotBlank(evidence)) {
                            peMsAnnEvidenceBean.setEvidenceValue(evidence);
                        }

                        //create a TPBDataTypeBean
                        TPBDataTypeBean tpbDataTypeBean = new TPBDataTypeBean();
                        //set the data type
                        tpbDataTypeBean.setDataType(DataType.PE_MS_ANN.type());
                        //set the traffic lights level to 3
                        tpbDataTypeBean.setLevel(TLLevel.TL3.level());
                        peMsAnnEvidenceBean.setTpbDataTypeBean(tpbDataTypeBean);
                        //add the PE MS Annotation for PRIDE
                        peMsEvidenceBeans.add(peMsAnnEvidenceBean);
                    }
                    //if category = Proteomic database and database = PeptideAtlas
                    if (StringUtils.equalsIgnoreCase(xrefDatabase, NXConts.XREF_DB_PEPTIDE_ATLAS) && StringUtils.equalsIgnoreCase(xrefCategory, NXConts.XREF_CA_PROTEOMIC_DATABASES)) {
                        //set the color yellow
                        peMsAnnEvidenceBean.setColorLevel(ColorType.YELLOW.color());
                        if (StringUtils.isNotBlank(xrefAccession)) {
                            peMsAnnEvidenceBean.setHyperlink(xrefUrl);
                        }
                        //added the evidence
                        String evidence = null;
                        evidence = xrefDatabase;
                        if (StringUtils.isNotBlank(xrefAccession)) {
                            evidence += " - " + xrefAccession;
                        }
                        if (StringUtils.isNotBlank(evidence)) {
                            peMsAnnEvidenceBean.setEvidenceValue(evidence);
                        }
                        //create a TPBDataTypeBean
                        TPBDataTypeBean tpbDataTypeBean = new TPBDataTypeBean();
                        //set the data type
                        tpbDataTypeBean.setDataType(DataType.PE_MS_ANN.type());
                        //set the traffic lights level to 3
                        tpbDataTypeBean.setLevel(TLLevel.TL3.level());
                        peMsAnnEvidenceBean.setTpbDataTypeBean(tpbDataTypeBean);
                        //add pe ms annotation for PeptideAtlas
                        peMsEvidenceBeans.add(peMsAnnEvidenceBean);
                    }
                }

                //find the PE ANTI ANN
                if (StringUtils.isNotBlank(xrefDatabase) && StringUtils.isNotBlank(xrefCategory) && StringUtils.isNotBlank(xrefAccession)) {
                    if (StringUtils.equalsIgnoreCase(xrefDatabase, NXConts.XREF_DB_HPA) && StringUtils.equalsIgnoreCase(xrefCategory, NXConts.XREF_CA_ANTIBODY_DATABASES)) {
                        //  System.out.println("===================> PE ANTI Ann:  Accession: " + xrefAccession);
                        if (StringUtils.startsWithIgnoreCase(xrefAccession, NXConts.XREF_AC_PREFIX_ENSG)) {
                            if (StringUtils.isNotBlank(xrefUrl)) {
                                peMsAntiURL = StringUtils.removeEndIgnoreCase(xrefUrl, NXConts.XREF_AC_ENSG_URL_END_PART);
                                // System.out.println("================== URL: " + peMsAntiURL);
                            }
                        }

                        if (StringUtils.startsWithIgnoreCase(xrefAccession, NXConts.XREF_AC_PREFIX_HPA) || StringUtils.startsWithIgnoreCase(xrefAccession, NXConts.XREF_AC_PREFIX_CAB)) {
                            if (StringUtils.isBlank(peMsAntiURL)) {
                                peMsAntiURL = xrefUrl;
                            }
                            peMsAntiCounter++;
                        }
                    }
                }

            }

            //add the PE MS Ann Evidences if Any
            peMsAntiEntryBean.setPeMsAnnEvidenceBeans(peMsEvidenceBeans);

            //PE ANTI ANN
            if (peMsAntiCounter > 0) {
                //create a pe anti evidence object
                PEEvidenceBean peAntiAnnEvidenceBean = new PEEvidenceBean();
                if (peMsAntiCounter == 1) {
                    peAntiAnnEvidenceBean.setColorLevel(ColorType.RED.color());
                }
                if (peMsAntiCounter > 1) {
                    peAntiAnnEvidenceBean.setColorLevel(ColorType.YELLOW.color());
                }
                peAntiAnnEvidenceBean.setEvidenceValue(peMsAntiCounter + " " + NXConts.XREF_HPA_ANTIBODY_DESC);
                if (StringUtils.isNotBlank(peMsAntiURL)) {
                    peAntiAnnEvidenceBean.setHyperlink(peMsAntiURL);
                }
                //create a TPBDataTypeBean
                TPBDataTypeBean tpbDataTypeBean = new TPBDataTypeBean();
                //set the data type
                tpbDataTypeBean.setDataType(DataType.PE_ANTI_ANN.type());
                //set the traffic lights level to 3
                tpbDataTypeBean.setLevel(TLLevel.TL3.level());
                peAntiAnnEvidenceBean.setTpbDataTypeBean(tpbDataTypeBean);
                //set the pe anti ann object
                peMsAntiEntryBean.setPeAntiAnnEvidenceBean(peAntiAnnEvidenceBean);
            }
        }
        return peMsAntiEntryBean;
    }

    //parse annotation list
    @SuppressWarnings("unchecked")
    private List<NXAnnEntryBean> parseAnnotationList(Element protein) {

        List<NXAnnEntryBean> nxAnnEntryBeanList = new ArrayList<NXAnnEntryBean>();

        Element annotationsEle = protein.getChild(ELE_ANNOTATIONS);
        if (annotationsEle != null) {
            List<Element> annotationListsEles = annotationsEle.getChildren(ELE_ANNOTATION_LIST);
            for (Element annListEle : annotationListsEles) {

                //get the annotation category value
                Attribute categoryAttr = annListEle.getAttribute(ATTR_CATEGORY);
                String annotationCategoryValue = null;
                if (categoryAttr != null) {
                    annotationCategoryValue = categoryAttr.getValue();
                }

                //get all annotation elements
                List<Element> annos = annListEle.getChildren(ELE_ANNOTATION);

                //for each annotation element
                for (Element ann : annos) {

                    NXAnnEntryBean nxAnnEntryBean = new NXAnnEntryBean();
                    nxAnnEntryBean.setCategory(annotationCategoryValue);

                    //qualityQualifier attribute value
                    Attribute qualityQAttr = ann.getAttribute(ATTR_QUALITY_QUALIFIER);
                    if (qualityQAttr != null) {
                        String qualityAttrValue = qualityQAttr.getValue();
                        nxAnnEntryBean.setQualityQualifier(qualityAttrValue);
                    }
                    //uniqueName attribute value
                    Attribute uniqueNameAttr = ann.getAttribute(ATTR_UNIQUE_NAME);
                    if (uniqueNameAttr != null) {
                        String uniqueNameValue = qualityQAttr.getValue();
                        nxAnnEntryBean.setUniqueName(uniqueNameValue);
                    }

                    //cvTerm element
                    Element cvTermEle = ann.getChild(ELE_CV_TERM);
                    if (cvTermEle != null) {
                        //cvTerm accession value
                        Attribute cvTermAcAttr = cvTermEle.getAttribute(ATTR_ACCESSION);
                        if (cvTermAcAttr != null) {
                            String cvTermAc = cvTermAcAttr.getValue();
                            nxAnnEntryBean.setCvTermAccession(cvTermAc);
                        }
                        //get cvName if any
                        Element cvNameEle = cvTermEle.getChild(ELE_CV_NAME);
                        if (cvNameEle != null) {
                            String cvName = cvNameEle.getText();
                            nxAnnEntryBean.setCvName(cvName);
                        }
                    }
                    //Description element
                    Element descEle = ann.getChild(ELE_DESCRIPTION);
                    if (descEle != null) {
                        String desc = descEle.getTextTrim();
                        nxAnnEntryBean.setDescription(desc);
                    }

                    try {
                        List<NXAnnEvidenceBean> nxAnnEvidenceBeans = new ArrayList<NXAnnEvidenceBean>();
                        //Evidence Elements
                        Element evidencesEle = ann.getChild(ELE_EVIDENCES);
                        if (evidencesEle != null) {
                            List<Element> evidenceList = evidencesEle.getChildren(ELE_EVIDENCE);
                            for (Element evEle : evidenceList) {
                                NXAnnEvidenceBean nxAnnEvidenceBean = new NXAnnEvidenceBean();
                                //isNegative Attribute
                                Attribute isNegAttr = evEle.getAttribute(ATTR_IS_NEGATIVE);
                                if (isNegAttr != null) {
                                    boolean isNegAttrValue = isNegAttr.getBooleanValue();
                                    nxAnnEvidenceBean.setNegative(isNegAttrValue);
                                }
                                //qualifierType attribute
                                Attribute qualifierTypeAttr = evEle.getAttribute(ATTR_QUALIFIER_TYPE);
                                if (qualifierTypeAttr != null) {
                                    String qualifierType = qualifierTypeAttr.getValue();
                                    nxAnnEvidenceBean.setQualifierType(qualifierType);
                                }
                                //resourceAssocType Attribute
                                Attribute resourceAssocTypeAttr = evEle.getAttribute(ATTR_RESOURCE_ASSOC_TYPE);
                                if (resourceAssocTypeAttr != null) {
                                    String resAssocType = resourceAssocTypeAttr.getValue();
                                    nxAnnEvidenceBean.setResourceAssocType(resAssocType);
                                }
                                //resourceRef attribute
                                Attribute resourceRefAttr = evEle.getAttribute(ATTR_RESOURCE_REF);
                                if (resourceRefAttr != null) {
                                    int resourceRef = resourceRefAttr.getIntValue();
                                    nxAnnEvidenceBean.setResourceRef(resourceRef);
                                }
                                nxAnnEvidenceBeans.add(nxAnnEvidenceBean);
                            }
                            //set all evidence for this annotation
                            if (evidenceList.size() > 0) {
                                nxAnnEntryBean.setNxAnnEvidenceBeans(nxAnnEvidenceBeans);
                            }
                        }

                        List<NXIsoFormAnnBean> nxisoFormAnnBeans = new ArrayList<NXIsoFormAnnBean>();
                        Element isoFormSpecEle = ann.getChild(ELE_ISO_FORM_SPECIFICITY);
                        if (isoFormSpecEle != null) {
                            List<Element> isoFormAnnots = isoFormSpecEle.getChildren(ELE_ISO_FORM_ANNOT);

                            for (Element isoformAnn : isoFormAnnots) {
                                NXIsoFormAnnBean nxisoFormAnnBean = new NXIsoFormAnnBean();
                                //isoFormRef attribute
                                Attribute isoFormRefAttr = isoformAnn.getAttribute(ATTR_ISO_FORM_REF);
                                if (isoFormRefAttr != null) {
                                    String isoFormRef = isoFormRefAttr.getValue();
                                    nxisoFormAnnBean.setIsoFormRef(isoFormRef);
                                }
                                // Positions
                                Element positionsEle = isoformAnn.getChild(ELE_POSITIONS);
                                if (positionsEle != null) {
                                    Element positionEle = positionsEle.getChild(ELE_POSITION);
                                    if (positionEle != null) {
                                        //first position
                                        Attribute firstAttr = positionEle.getAttribute(ATTR_POSITION_FIRST);
                                        if (firstAttr != null) {
                                            int first = firstAttr.getIntValue();
                                            nxisoFormAnnBean.setFirstPosition(first);
                                        }
                                        //first status
                                        Attribute firstStatusAttr = positionEle.getAttribute(ATTR_POSITION_FIRST_STATUS);
                                        if (firstStatusAttr != null) {
                                            String firstStatus = firstStatusAttr.getValue();
                                            nxisoFormAnnBean.setFirstStatus(firstStatus);
                                        }
                                        //last position
                                        Attribute lastAttr = positionEle.getAttribute(ATTR_POSITION_LAST);
                                        if (lastAttr != null) {
                                            int last = lastAttr.getIntValue();
                                            nxisoFormAnnBean.setLastPosition(last);
                                        }
                                        //last status
                                        Attribute lastStatusAttr = positionEle.getAttribute(ATTR_POSITION_LAST_STATUS);
                                        if (lastStatusAttr != null) {
                                            String lastStatus = lastStatusAttr.getValue();
                                            nxisoFormAnnBean.setLastStatus(lastStatus);
                                        }
                                    }
                                }
                                nxisoFormAnnBeans.add(nxisoFormAnnBean);
                            }
                            //set all isoformSpecificity for this annotation
                            if (nxisoFormAnnBeans.size() > 0) {
                                nxAnnEntryBean.setNxisoFormAnnBeans(nxisoFormAnnBeans);
                            }
                        }
                    } catch (Exception ex) {
                        throw new DMXMLParserException(ex);
                    }

                    //add all annotations for this protein
                    nxAnnEntryBeanList.add(nxAnnEntryBean);
                }
            }
        }
        return nxAnnEntryBeanList;
    }

    public static void main(String[] args) throws Exception {
        String filename = "./testData/nextprot_chromosome_7.xml";

        FileInputStream fileInputStream = new FileInputStream(new File(filename));
        NxXMLParser parser = new NxXMLParser();
        List<NXEntryBean> nxEntryBeans = parser.parseNextProtXML(fileInputStream);

        System.out.println("======== total size of nextprot entry: " + nxEntryBeans.size());
//
//       // String ftpUrl = "ftp://ftp.nextprot.org/pub/current_release/xml/nextprot_chromosome_7.xml.gz";
//        String filename = "./testData/nextprot_chromosome_7.xml.gz";
//        try {
//            GZIPInputStream gzipInputStream = null;
//            FileInputStream fileInputStream = null;
//            gzipInputStream = new GZIPInputStream(new FileInputStream(new File(filename)));
//            System.out.println("Opening the output file............:opened");
//            String outFilename = "chromosome_7.xml";
//            OutputStream out = new FileOutputStream(outFilename);
//            System.out.println("Transferring bytes from the compressed file to the output file........:Transfer successful ");
//            byte[] buf = new byte[1024];
//            //size can be changed according to programmer 's need.
//            int len;
//            while ((len = gzipInputStream.read(buf)) > 0) {
//                out.write(buf, 0, len);
//            }
//            System.out.println("The file and stream is ......closing..........:closed");
//            gzipInputStream.close();
//            out.close();
//        } catch (IOException e) {
//            System.out.println("Exception has been thrown" + e);
//        }
    }


}
