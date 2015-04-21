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

package edu.monash.merc.system.parser.xml;

import edu.monash.merc.common.name.ColorType;
import edu.monash.merc.common.name.DataType;
import edu.monash.merc.common.name.DbAcType;
import edu.monash.merc.common.name.TLLevel;
import edu.monash.merc.dto.*;
import edu.monash.merc.dto.hpa.HPAEntryBean;
import edu.monash.merc.exception.DMXMLParserException;
import org.apache.commons.lang.StringUtils;
import org.apache.log4j.Logger;
import org.codehaus.stax2.XMLEventReader2;
import org.codehaus.stax2.XMLInputFactory2;

import javax.xml.namespace.QName;
import javax.xml.stream.events.*;
import java.io.FileInputStream;
import java.util.ArrayList;
import java.util.List;

/**
 * HPAWSXmlParser class parses the hpa xml
 *
 * @author Simon Yu
 *         <p/>
 *         Email: xiaoming.yu@monash.edu
 * @version 1.0
 * @since 1.0
 *        <p/>
 *        Date: 11/10/12 12:13 PM
 */
public class HPAWSXmlParser {

    private XMLInputFactory2 xmlif2;

    private static String ELE_ENTRY = "entry";

    private static String ATTR_VERSION = "version";

    private static String ATTR_URL = "url";

    private static String ELE_NAME = "name";

    private static String ELE_IDENTIFIER = "identifier";

    private static String ATTR_ID = "id";

    private static String ELE_XREF = "xref";

    private static String ATTR_DB = "db";

    private static String ELE_TISSUE_EXPRESSION = "tissueExpression";

    private static String ATTR_TYPE = "type";

    private static String ELE_VERIFICATION = "verification";

    private static String ELE_DATA = "data";

    private static String ELE_TISSUE = "tissue";

    private static String ATTR_STATUS = "status";

    private static String ELE_CELLTYPE = "cellType";

    private static String ELE_LEVEL = "level";

    private static String TISSUE_STATUS_NORMAL = "normal";

    private static String LEVEL_NONE = "none";

    private static String LEVEL_NEGATIVE = "negative";

    private static String VALIDATION_SUPPORTIVE = "supportive";

    private static String VALIDATION_UNCERTAIN = "uncertain";

    private static String VALIDATION_NON_SUPPORTIVE = "non-supportive";

    private static String RELIABILITY_HIGH = "high";

    private static String RELIABILITY_MEDIUM = "medium";

    private static String RELIABILITY_LOW = "low";

    private static String RELIABILITY_VERY_LOW = "very low";

    private static String NONE_EVIDENCE_DATA_TXT = "There is no tissue expression data for this gene";

    private static String ELE_ANTIBODY = "antibody";

    private static final Logger logger = Logger.getLogger(HPAWSXmlParser.class.getName());

    public List<HPAEntryBean> parseHPAXml(String fileName, XMLInputFactory2 factory2) {
        xmlif2 = factory2;
        logger.info("Starting to parse " + fileName);
        List<HPAEntryBean> hpaEntryBeans = new ArrayList<HPAEntryBean>();
        XMLEventReader2 xmlEventReader = null;
        try {
            xmlEventReader = (XMLEventReader2) xmlif2.createXMLEventReader(new FileInputStream(fileName));

            QName entryQN = new QName(ELE_ENTRY);
            QName versionQN = new QName(ATTR_VERSION);
            QName urlQN = new QName(ATTR_URL);
            QName nameQN = new QName(ELE_NAME);
            QName identiferQN = new QName(ELE_IDENTIFIER);
            QName idQN = new QName(ATTR_ID);
            QName xrefQN = new QName(ELE_XREF);
            QName dbQN = new QName(ATTR_DB);
            QName tissueExpQN = new QName(ELE_TISSUE_EXPRESSION);
            QName typeQN = new QName(ATTR_TYPE);
            QName verificationQN = new QName(ELE_VERIFICATION);
            QName dataQN = new QName(ELE_DATA);
            QName tissueQN = new QName(ELE_TISSUE);
            QName statusQN = new QName(ATTR_STATUS);
            QName cellTypeQN = new QName(ELE_CELLTYPE);
            QName levelQN = new QName(ELE_LEVEL);
            QName antibodyQN = new QName(ELE_ANTIBODY);

            String version = null;
            String url = null;

            String geneName = null;
            String geneAccession = null;
            String dbNameForIdentifier = null;

            String xrefAc = null;
            String xrefDb = null;
            boolean tissueExpressionPresent = false;

            boolean antibodyPresent = false;

            String tissueStatus = null;
            String tissue = null;
            String cellType = null;
            String levelType = null;
            String level = null;

            String verificationType = null;
            String verification = null;

            HPAEntryBean hpaEntryBean = null;

            GeneBean geneBean = null;

            List<DbSourceAcEntryBean> dbSourceAcEntryBeans = new ArrayList<DbSourceAcEntryBean>();

            List<PEEvidenceBean> peAntiIHCNormEvidenceBeans = new ArrayList<PEEvidenceBean>();

            PEEvidenceBean antiIHCNormEvidenceBean = null;

            AccessionBean identifiedAcBean = null;

            while (xmlEventReader.hasNextEvent()) {
                //eventType = reader.next();
                XMLEvent event = xmlEventReader.nextEvent();
                if (event.isStartElement()) {
                    StartElement element = event.asStartElement();

                    //hpa entry
                    if (element.getName().equals(entryQN)) {
                        //start to create a hpaEntryBean
                        hpaEntryBean = new HPAEntryBean();
                        //create a GeneBean
                        geneBean = new GeneBean();
                        //create a list of DbSourceAcEntryBean to store all DbSource and Ac
                        dbSourceAcEntryBeans = new ArrayList<DbSourceAcEntryBean>();
                        //create a list of PEEvidenceBean to store all antibody evidencs for the current gene
                        peAntiIHCNormEvidenceBeans = new ArrayList<PEEvidenceBean>();

                        //get the version attribute
                        Attribute versionAttr = element.getAttributeByName(versionQN);
                        if (versionAttr != null) {
                            version = versionAttr.getValue();
                        }
                        //get the url attribute
                        Attribute urlAttr = element.getAttributeByName(urlQN);
                        if (urlAttr != null) {
                            url = urlAttr.getValue();
                        }
                    }

                    //parse the gene name in the name element
                    if (element.getName().equals(nameQN)) {
                        if (xmlEventReader.peek().isCharacters()) {
                            event = xmlEventReader.nextEvent();
                            Characters geneCharacters = event.asCharacters();
                            if (geneCharacters != null) {
                                geneName = geneCharacters.getData();
                            }
                        }
                    }

                    //parse the ensg accession and db in the identifier element
                    if (element.getName().equals(identiferQN)) {
                        Attribute idAttr = element.getAttributeByName(idQN);
                        if (idAttr != null) {
                            geneAccession = idAttr.getValue();
                        }
                        Attribute dbAttr = element.getAttributeByName(dbQN);
                        if (dbAttr != null) {
                            dbNameForIdentifier = dbAttr.getValue();
                        }
                    }

                    //parse all db and accession pair in xref element
                    if (element.getName().equals(xrefQN)) {
                        Attribute idAttr = element.getAttributeByName(idQN);
                        if (idAttr != null) {
                            xrefAc = idAttr.getValue();
                        }
                        Attribute dbAttr = element.getAttributeByName(dbQN);
                        if (dbAttr != null) {
                            xrefDb = dbAttr.getValue();
                        }
                    }


                    //parse tissueExpression
                    if (element.getName().equals(tissueExpQN)) {
                        //we only focus on the tissueExpression element in the path /entry/tissueExpression
                        if (!antibodyPresent) {
                            //set the tissueExpression present flag into true;
                            tissueExpressionPresent = true;
                            //create a list of PEEvidenceBean to store the PEEvidence for antibody
                            peAntiIHCNormEvidenceBeans = new ArrayList<PEEvidenceBean>();
                        }
                    }

                    //parse the verification element to get reliability or validation value
                    if (element.getName().equals(verificationQN)) {
                        //we only focus on the verification element in the path /entry/tissueExpression/verification
                        if (!antibodyPresent && tissueExpressionPresent) {
                            Attribute verifAttr = element.getAttributeByName(typeQN);
                            if (verifAttr != null) {
                                verificationType = element.getAttributeByName(typeQN).getValue();
                            }
                            if (xmlEventReader.peek().isCharacters()) {
                                event = xmlEventReader.nextEvent();
                                verification = event.asCharacters().getData();
                            }
                        }
                    }
                    //start of the data element
                    if (element.getName().equals(dataQN)) {
                        //we only focus on the data element in the path /entry/tissueExpression/data
                        if (!antibodyPresent && tissueExpressionPresent) {
                            antiIHCNormEvidenceBean = new PEEvidenceBean();
                            TPBDataTypeBean dataTypeBean = createTPBDataTypeBeanForPEANTIIHCNORM();
                            antiIHCNormEvidenceBean.setTpbDataTypeBean(dataTypeBean);
                        }
                    }

                    //start of tissue
                    if (element.getName().equals(tissueQN)) {
                        //we only focus on the tissue element in the path /entry/tissueExpression/data/tissue
                        if (!antibodyPresent && tissueExpressionPresent) {
                            Attribute tissueStatusAttr = element.getAttributeByName(statusQN);
                            if (tissueStatusAttr != null) {
                                tissueStatus = tissueStatusAttr.getValue();
                            }
                            if (xmlEventReader.peek().isCharacters()) {
                                event = xmlEventReader.nextEvent();
                                tissue = event.asCharacters().getData();
                            }
                        }
                    }

                    //start of cellType
                    if (element.getName().equals(cellTypeQN)) {
                        //we only focus on the cellType element in the path /entry/tissueExpression/data/cellType
                        if (!antibodyPresent && tissueExpressionPresent) {
                            if (xmlEventReader.peek().isCharacters()) {
                                event = xmlEventReader.nextEvent();
                                cellType = event.asCharacters().getData();
                            }
                        }
                    }

                    //start of level
                    if (element.getName().equals(levelQN)) {
                        //we only focus on the level element in the path /entry/tissueExpression/data/level
                        if (!antibodyPresent && tissueExpressionPresent) {
                            Attribute typeAttr = element.getAttributeByName(typeQN);
                            if (typeAttr != null) {
                                levelType = typeAttr.getValue();
                            }
                            if (xmlEventReader.peek().isCharacters()) {
                                event = xmlEventReader.nextEvent();
                                level = event.asCharacters().getData();
                            }
                        }
                    }
                    //start of antibody element
                    if (element.getName().equals(antibodyQN)) {
                        //we have to setup antibodyPresent flag as true
                        antibodyPresent = true;
                    }
                }

                //End of element
                if (event.isEndElement()) {
                    EndElement endElement = event.asEndElement();
                    //hpa entry end
                    if (endElement.getName().equals(entryQN)) {
                        //set hpa version
                        hpaEntryBean.setHpaVersion(version);
                        //hpaEntryBean set gene bean
                        hpaEntryBean.setGeneBean(geneBean);
                        //create the primary dbsource bean
                        DBSourceBean primaryDbSourceBean = createPrimaryDBSourceBeanForHPA();

                        //set the primary DBSourceBean
                        hpaEntryBean.setPrimaryDbSourceBean(primaryDbSourceBean);

                        //set the identified accesion bean
                        hpaEntryBean.setIdentifiedAccessionBean(identifiedAcBean);

                        //set DbSourceAcEntryBean list
                        hpaEntryBean.setDbSourceAcEntryBeans(dbSourceAcEntryBeans);

                        //set all the PeAntiIHCBody evidences
                        if (peAntiIHCNormEvidenceBeans.size() == 0) {
                            peAntiIHCNormEvidenceBeans.add(createNonePEEvidence(url));
                        }

                        hpaEntryBean.setPeAntiIHCNormEvidencesBeans(peAntiIHCNormEvidenceBeans);

                        //add the current hpa entry bean into list
                        hpaEntryBeans.add(hpaEntryBean);
                        //reset version and url
                        version = null;
                        url = null;
                        identifiedAcBean = null;
                    }

                    //end of gene name, populate the gene name
                    if (endElement.getName().equals(nameQN)) {
                        //set gene name
                        geneBean.setDisplayName(geneName);
                    }

                    //end of identifier, populating for gene accession, db and accessions if any
                    if (endElement.getName().equals(identiferQN)) {
                        //set the gene accession
                        geneBean.setEnsgAccession(geneAccession);

                        identifiedAcBean = createIdentifiedAcBean(geneAccession, dbNameForIdentifier);
                        //create a DbSourceAcEntryBean based on the identifier element
                        DbSourceAcEntryBean dbSourceAcEntryBean = createDbSourceAcEntry(dbNameForIdentifier, geneAccession);
                        //add this DbSourceAcEntryBean into list
                        dbSourceAcEntryBeans.add(dbSourceAcEntryBean);
                    }

                    //end of xref element.  populate for db and accessions if any
                    if (endElement.getName().equals(xrefQN)) {
                        //create a DbSourceAcEntryBean based on the xref element
                        DbSourceAcEntryBean dbSourceAcEntryBean = createDbSourceAcEntry(xrefDb, xrefAc);
                        //add this DbSoureAcEntryBean into list
                        dbSourceAcEntryBeans.add(dbSourceAcEntryBean);
                        //set rest of db and accession values
                        xrefDb = null;
                        xrefAc = null;
                    }
                    //end of the tissueExpression
                    if (endElement.getName().equals(tissueExpQN)) {
                        //we only focus on the tissueExpression element in the path /entry/tissueExpression
                        if (!antibodyPresent) {
                            //the tissueExpression is end. we have to reset tissueExpressionPresent,
                            //verificationType and verification values under the tissueExpression element level
                            //reset tissueExpression present flag into false
                            tissueExpressionPresent = false;
                            //reset verification type
                            verificationType = null;
                            //reset verification value
                            verification = null;
                        }
                    }

                    //end of data element
                    if (endElement.getName().equals(dataQN)) {
                        //we only focus on the data element in the path /entry/tissueExpression/data
                        if (!antibodyPresent && tissueExpressionPresent) {
                            //we only consider the tissue status is normal one
                            if (StringUtils.endsWithIgnoreCase(tissueStatus, TISSUE_STATUS_NORMAL)) {
                                setAntiEvidence(antiIHCNormEvidenceBean, url, verification, tissue, cellType, level, levelType);
                                //add anti evidence
                                peAntiIHCNormEvidenceBeans.add(antiIHCNormEvidenceBean);
                            }
                            //the data element is end. we have to reset the tissueStatus, tissue, cellType and level values under the data element level
                            tissueStatus = null;
                            tissue = null;
                            cellType = null;
                            level = null;
                            levelType = null;
                        }
                    }
                    //end of antibody
                    if (endElement.getName().equals(antibodyQN)) {
                        //we have to reset antibodyPresent flag as false
                        antibodyPresent = false;
                    }
                }
                //End of XML document
                if (event.isEndDocument()) {
                    // finished to parse the whole document;
                    break;
                }
            }
        } catch (Exception ex) {
            logger.error(ex);
            throw new DMXMLParserException(ex);
        } finally {
            if (xmlEventReader != null) {
                try {
                    xmlEventReader.close();
                } catch (Exception e) {
                    //ignore whatever caught.
                }
            }
        }
        return hpaEntryBeans;
    }

    //create a none PEEvidence
    private PEEvidenceBean createNonePEEvidence(String url) {
        PEEvidenceBean antiIHCEvidenceBean = new PEEvidenceBean();
        TPBDataTypeBean dataTypeBean = new TPBDataTypeBean();
        dataTypeBean.setDataType(DataType.PE_ANTI_IHC_NORM.type());
        dataTypeBean.setLevel(TLLevel.TL4.level());
        antiIHCEvidenceBean.setTpbDataTypeBean(dataTypeBean);
        antiIHCEvidenceBean.setColorLevel(ColorType.BLACK.color());
        antiIHCEvidenceBean.setEvidenceValue(NONE_EVIDENCE_DATA_TXT);
        antiIHCEvidenceBean.setHyperlink(url);
        return antiIHCEvidenceBean;
    }

    //set the PEEvidenceBean properties
    private void setAntiEvidence(PEEvidenceBean antiEvidenceBean, String urlBase, String reliability, String tissue, String cellType, String level, String levelType) {
        if (StringUtils.equalsIgnoreCase(level, LEVEL_NEGATIVE) || StringUtils.equalsIgnoreCase(level, LEVEL_NONE)) {
            antiEvidenceBean.setColorLevel(ColorType.BLACK.color());
        } else {
            if (StringUtils.equalsIgnoreCase(reliability, VALIDATION_SUPPORTIVE)) {
                antiEvidenceBean.setColorLevel(ColorType.YELLOW.color());
            }
            if (StringUtils.equalsIgnoreCase(reliability, VALIDATION_UNCERTAIN)) {
                antiEvidenceBean.setColorLevel(ColorType.RED.color());
            }
            if (StringUtils.equalsIgnoreCase(reliability, VALIDATION_NON_SUPPORTIVE)) {
                antiEvidenceBean.setColorLevel(ColorType.RED.color());
            }
            if (StringUtils.equalsIgnoreCase(reliability, RELIABILITY_HIGH)) {
                antiEvidenceBean.setColorLevel(ColorType.GREEN.color());
            }
            if (StringUtils.equalsIgnoreCase(reliability, RELIABILITY_MEDIUM)) {
                antiEvidenceBean.setColorLevel(ColorType.YELLOW.color());
            }
            if (StringUtils.equalsIgnoreCase(reliability, RELIABILITY_LOW)) {
                antiEvidenceBean.setColorLevel(ColorType.RED.color());
            }
            if (StringUtils.equalsIgnoreCase(reliability, RELIABILITY_VERY_LOW)) {
                antiEvidenceBean.setColorLevel(ColorType.RED.color());
            }
        }
        antiEvidenceBean.setHyperlink(urlBase + "/" + TISSUE_STATUS_NORMAL + "/" + tissue);
        antiEvidenceBean.setEvidenceValue("Observed with " + level + " " + levelType + " in normal " + tissue + ", " + cellType);
    }

    //create DbSourceAcEntryBean
    private DbSourceAcEntryBean createDbSourceAcEntry(String dbName, String accession) {
        DbSourceAcEntryBean dbSourceAcEntryBean = new DbSourceAcEntryBean();
        if (StringUtils.isNotBlank(dbName) && StringUtils.isNotBlank(accession)) {
            AccessionBean accessionBean = new AccessionBean();
            accessionBean.setAccession(accession);
            if (StringUtils.containsIgnoreCase(dbName, DbAcType.Ensembl.type())) {
                accessionBean.setAcType(DbAcType.Gene.type());
            }
            if (StringUtils.containsIgnoreCase(dbName, DbAcType.SwissProt.type())) {
                accessionBean.setAcType(DbAcType.SwissProt.type());
            }

            DBSourceBean dbSourceBean = new DBSourceBean();

            if (StringUtils.containsIgnoreCase(dbName, DbAcType.Ensembl.type())) {
                dbSourceBean.setDbName(DbAcType.Ensembl.type());
            }
            if (StringUtils.containsIgnoreCase(dbName, DbAcType.SwissProt.type())) {
                dbSourceBean.setDbName(DbAcType.SwissProt.type());
            }
            dbSourceAcEntryBean.setAccessionBean(accessionBean);
            dbSourceAcEntryBean.setDbSourceBean(dbSourceBean);
            return dbSourceAcEntryBean;
        }
        return null;
    }

    private TPBDataTypeBean createTPBDataTypeBeanForPEANTIIHCNORM() {
        TPBDataTypeBean dataTypeBean = new TPBDataTypeBean();
        dataTypeBean.setDataType(DataType.PE_ANTI_IHC_NORM.type());
        dataTypeBean.setLevel(TLLevel.TL4.level());
        return dataTypeBean;
    }

    private AccessionBean createIdentifiedAcBean(String accession, String actype) {
        AccessionBean accessionBean = new AccessionBean();
        accessionBean.setAccession(accession);
        if (StringUtils.containsIgnoreCase(actype, DbAcType.Ensembl.type())) {
            accessionBean.setAcType(DbAcType.Gene.type());
        }
        return accessionBean;
    }

    private DBSourceBean createPrimaryDBSourceBeanForHPA() {
        DBSourceBean pDbSourceBean = new DBSourceBean();
        pDbSourceBean.setDbName(DbAcType.HPA.type());
        pDbSourceBean.setPrimaryEvidences(true);
        return pDbSourceBean;
    }

    public static void main(String[] art) throws Exception {
        HPAWSXmlParser parser = new HPAWSXmlParser();
        String hpaFileName = "./testData/hpa_chromosome_7.xml";
        hpaFileName = "/opt/tpb/db_download/small_hpa.xml";
        long startTime = System.currentTimeMillis();
        List<HPAEntryBean> hpaEntryBeans = parser.parseHPAXml(hpaFileName, WSXmlInputFactory.getInputFactoryConfiguredForXmlConformance());
        long endTime = System.currentTimeMillis();

        int i = 0;
        for (HPAEntryBean hpaEntryBean : hpaEntryBeans) {
            GeneBean geneBean = hpaEntryBean.getGeneBean();
            String version = hpaEntryBean.getHpaVersion();

            String engsAc = geneBean.getEnsgAccession();


                System.out.println("=========== version: " + version + " - gene name: " + geneBean.getDisplayName() + " accession : " + geneBean.getEnsgAccession());
                DBSourceBean primaryDbSource = hpaEntryBean.getPrimaryDbSourceBean();
                System.out.println("=========== dbsource name: " + primaryDbSource.getDbName());
                List<DbSourceAcEntryBean> dbSourceAcEntryBeans = hpaEntryBean.getDbSourceAcEntryBeans();
                for (DbSourceAcEntryBean dbSourceAcEntryBean : dbSourceAcEntryBeans) {
                    DBSourceBean dbSourceBean = dbSourceAcEntryBean.getDbSourceBean();
                    AccessionBean accessionBean = dbSourceAcEntryBean.getAccessionBean();
                    System.out.println("========= ac : " + accessionBean.getAccession() + " db: " + dbSourceBean.getDbName());
                }
                List<PEEvidenceBean> antiEvidenceBeans = hpaEntryBean.getPeAntiIHCNormEvidencesBeans();
                if (antiEvidenceBeans != null) {
                    System.out.println("====== total evidences size : " + antiEvidenceBeans.size());
                    for (PEEvidenceBean peEvidenceBean : antiEvidenceBeans) {
                        System.out.println("========= anti evidence: color - level: " + peEvidenceBean.getColorLevel() + " evidence : " + peEvidenceBean.getEvidenceValue() + " - url : " + peEvidenceBean.getHyperlink());
                    }
                } else {
                    System.out.println("There is no evidence for this gene.");
                }

            System.out.println("");

        }
        System.out.println("===== total entry size : " + hpaEntryBeans.size());
        System.out.println("===== total processing time : " + (endTime - startTime) / 1000 + " seconds.");
    }
}
