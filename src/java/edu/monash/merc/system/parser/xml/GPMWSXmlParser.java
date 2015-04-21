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
import edu.monash.merc.dto.gpm.GPMEntryBean;
import edu.monash.merc.exception.DMXMLParserException;
import org.apache.commons.lang.StringUtils;
import org.apache.log4j.Logger;
import org.codehaus.stax2.XMLEventReader2;
import org.codehaus.stax2.XMLInputFactory2;

import javax.xml.namespace.QName;
import javax.xml.stream.events.Attribute;
import javax.xml.stream.events.EndElement;
import javax.xml.stream.events.StartElement;
import javax.xml.stream.events.XMLEvent;
import java.io.FileInputStream;
import java.util.ArrayList;
import java.util.List;

/**
 * GPMWSXmlParser class parses the gpm xml
 *
 * @author Simon Yu
 *         <p/>
 *         Email: xiaoming.yu@monash.edu
 * @version 1.0
 * @since 1.0
 *        <p/>
 *        Date: 12/12/12 4:34 PM
 */
public class GPMWSXmlParser {

    static String PE_MS_PROB_EVIDENCE_TEXT = "GPMDB best probability (log(e)) = ";

    static String PE_MS_SAMPLE_EVIDENCE_TEXT_PART1 = "Observed in ";

    static String PE_MS_SAMPLE_EVIDENCE_TEXT_PART2 = " samples in GPMDB";

    static String GPM_HYPERLINK_PROB_SAM_BASE = "http://gpmdb.thegpm.org/thegpm-cgi/dblist_label.pl?label=";

    static String GPM_HYPERLINK_PROB_SAM_BASE_EXT = "&proex=-1";

    private static String ELE_GPM_PROTEIN = "protein";

    private static String ATTR_GPM_ENSP = "ensp";

    private static String ATTR_GPM_ENSG = "ensg";

    private static String ELE_GPM_IDENTIFICATION = "identification";

    private static String ATTR_GPM_IDEN_BESTE = "beste";

    private static String ATTR_GPM_IDEN_SAMPLES = "samples";

    private XMLInputFactory2 xmlif2;

    private static final Logger logger = Logger.getLogger(GPMWSXmlParser.class.getName());

    public List<GPMEntryBean> parseGPMXml(String fileName, XMLInputFactory2 factory2) {

        xmlif2 = factory2;
        logger.info("Starting to parse " + fileName);

        XMLEventReader2 xmlEventReader = null;
        List<GPMEntryBean> gpmEntryBeans = new ArrayList<GPMEntryBean>();
        try {
            xmlEventReader = (XMLEventReader2) xmlif2.createXMLEventReader(new FileInputStream(fileName));

            QName proteinQN = new QName(ELE_GPM_PROTEIN);
            QName enspQN = new QName(ATTR_GPM_ENSP);
            QName ensgQN = new QName(ATTR_GPM_ENSG);
            QName identiQN = new QName(ELE_GPM_IDENTIFICATION);
            QName besteQN = new QName(ATTR_GPM_IDEN_BESTE);
            QName samplesQN = new QName(ATTR_GPM_IDEN_SAMPLES);
            String ensg = null;
            String ensp = null;
            String beste = null;
            String samples = null;

            GPMEntryBean gpmEntryBean = null;

            while (xmlEventReader.hasNextEvent()) {
                //eventType = reader.next();
                XMLEvent event = xmlEventReader.nextEvent();
                if (event.isStartElement()) {
                    StartElement element = event.asStartElement();

                    //protein entry
                    if (element.getName().equals(proteinQN)) {
                        //get the version attribute
                        Attribute enspAttr = element.getAttributeByName(enspQN);
                        Attribute ensgAttr = element.getAttributeByName(ensgQN);
                        if (enspAttr != null) {
                            ensp = enspAttr.getValue();
                        }
                        if (ensgAttr != null) {
                            ensg = ensgAttr.getValue();
                        }
                        //create gpn entry bean
                        gpmEntryBean = new GPMEntryBean();
                        //create gpm dbsource
                        DBSourceBean gpmDbSourceBean = new DBSourceBean();
                        gpmDbSourceBean.setDbName(DbAcType.GPM.type());
                        gpmDbSourceBean.setPrimaryEvidences(true);
                        //set the gpm dbsource
                        gpmEntryBean.setPrimaryDbSourceBean(gpmDbSourceBean);

                        //create a gene bean
                        if (StringUtils.isNotBlank(ensg)) {
                            GeneBean geneBean = new GeneBean();
                            geneBean.setEnsgAccession(ensg);
                            gpmEntryBean.setGeneBean(geneBean);
                        }


                        //create an identified accession bean
                        AccessionBean identAccessionBean = parseIdentifiedAccessionBean(ensp);
                        gpmEntryBean.setIdentifiedAccessionBean(identAccessionBean);
                        //create dbsource and accession entry bean
                        List<DbSourceAcEntryBean> dbSourceAcEntryBeanList = parseDBSourceAcEntryBeans(ensp, ensg);
                        gpmEntryBean.setDbSourceAcEntryBeans(dbSourceAcEntryBeanList);

                    }

                    //protein entry
                    if (element.getName().equals(identiQN)) {
                        Attribute besteAttr = element.getAttributeByName(besteQN);
                        Attribute samplesAttr = element.getAttributeByName(samplesQN);
                        if (besteAttr != null) {
                            beste = besteAttr.getValue();
                        }
                        if (samplesAttr != null) {
                            samples = samplesAttr.getValue();
                        }

                        //create pe ms prob evidence based on beste and ensp accesion
                        PEEvidenceBean peMsProbEvidence = createMsProbEvidence(beste, ensp);
                        //parse pe ms samples evidence
                        PEEvidenceBean peMsSamplesEvidence = createMsSamplesEvidence(samples, ensp);

                        if (peMsProbEvidence != null) {
                            gpmEntryBean.setPeMsProbEvidenceBean(peMsProbEvidence);
                        }
                        if (peMsSamplesEvidence != null) {
                            gpmEntryBean.setPeMsSamplesEvidenceBean(peMsSamplesEvidence);
                        }
                    }
                }
                //End of element
                if (event.isEndElement()) {
                    EndElement endElement = event.asEndElement();
                    //hpa entry end
                    if (endElement.getName().equals(proteinQN)) {
                        //if gene is not null, the  accession is not null and get some evidences for this gpm entry, then we add it to the list
                        GeneBean geneBean = gpmEntryBean.getGeneBean();
                        AccessionBean identifiedAcBean = gpmEntryBean.getIdentifiedAccessionBean();
                        PEEvidenceBean peMsProbEvidence = gpmEntryBean.getPeMsProbEvidenceBean();
                        PEEvidenceBean peMsSampEvidence = gpmEntryBean.getPeMsProbEvidenceBean();
                        if (geneBean != null && identifiedAcBean != null) {
                            if (peMsProbEvidence != null || peMsSampEvidence != null) {
                                gpmEntryBeans.add(gpmEntryBean);
                            }
                        }
                    }
                }
            }
            return gpmEntryBeans;
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

    }

    private AccessionBean parseIdentifiedAccessionBean(String enspAc) {
        if (StringUtils.isNotBlank(enspAc)) {
            AccessionBean accessionBean = new AccessionBean();
            accessionBean.setAccession(enspAc);
            accessionBean.setAcType(DbAcType.Protein.type());
            return accessionBean;
        }
        return null;
    }

    private List<DbSourceAcEntryBean> parseDBSourceAcEntryBeans(String enspAc, String ensgAc) {
        List<DbSourceAcEntryBean> dbSourceAcEntryBeans = new ArrayList<DbSourceAcEntryBean>();
        DbSourceAcEntryBean dbSourceAcEntryBeanEnsp = generateDbSourceAcEntry(enspAc, DbAcType.Protein);
        if (dbSourceAcEntryBeanEnsp != null) {
            dbSourceAcEntryBeans.add(dbSourceAcEntryBeanEnsp);
        }
        DbSourceAcEntryBean dbSourceAcEntryBeanEnsg = generateDbSourceAcEntry(ensgAc, DbAcType.Gene);
        if (dbSourceAcEntryBeanEnsg != null) {
            dbSourceAcEntryBeans.add(dbSourceAcEntryBeanEnsg);
        }
        return dbSourceAcEntryBeans;
    }

    private DbSourceAcEntryBean generateDbSourceAcEntry(String accession, DbAcType dbAcType) {
        if (StringUtils.isNotBlank(accession)) {
            DbSourceAcEntryBean dbSourceAcEntryBean = new DbSourceAcEntryBean();
            AccessionBean accessionBean = new AccessionBean();
            accessionBean.setAccession(accession);
            if (dbAcType.equals(DbAcType.Gene)) {
                accessionBean.setAcType(DbAcType.Gene.type());
            }
            if (dbAcType.equals(DbAcType.Protein)) {
                accessionBean.setAcType(DbAcType.Protein.type());
            }
            DBSourceBean dbSourceBean = new DBSourceBean();
            dbSourceBean.setDbName(DbAcType.Ensembl.type());

            dbSourceAcEntryBean.setAccessionBean(accessionBean);
            dbSourceAcEntryBean.setDbSourceBean(dbSourceBean);
            return dbSourceAcEntryBean;
        }
        return null;
    }

    private PEEvidenceBean createMsProbEvidence(String beste, String enspAc) {

        if (StringUtils.isNotBlank(beste) && StringUtils.isNotBlank(enspAc)) {
            PEEvidenceBean peMsProbEvidence = new PEEvidenceBean();
            //create a TPBDataTypeBean
            TPBDataTypeBean tpbDataTypeBean = new TPBDataTypeBean();
            //set the data type
            tpbDataTypeBean.setDataType(DataType.PE_MS_PROB.type());
            //set the traffic lights level to 3
            tpbDataTypeBean.setLevel(TLLevel.TL3.level());
            peMsProbEvidence.setTpbDataTypeBean(tpbDataTypeBean);
            peMsProbEvidence.setEvidenceValue(PE_MS_PROB_EVIDENCE_TEXT + beste);
            peMsProbEvidence.setHyperlink(GPM_HYPERLINK_PROB_SAM_BASE + enspAc + GPM_HYPERLINK_PROB_SAM_BASE_EXT);
            double besteValue = Double.valueOf(beste).doubleValue();

            if (besteValue <= -10) {
                peMsProbEvidence.setColorLevel(ColorType.GREEN.color());
            }
            if (besteValue > -10 && besteValue <= -3) {
                peMsProbEvidence.setColorLevel(ColorType.YELLOW.color());
            }
            if (besteValue > -3 && besteValue < 0) {
                peMsProbEvidence.setColorLevel(ColorType.RED.color());
            }
            if (besteValue >= 0) {
                peMsProbEvidence.setColorLevel(ColorType.BLACK.color());
            }
            return peMsProbEvidence;
        }

        return null;
    }

    private PEEvidenceBean createMsSamplesEvidence(String samples, String enspAc) {

        if (StringUtils.isNotBlank(samples) && StringUtils.isNotBlank(enspAc)) {

            PEEvidenceBean peMsSamplesEvidence = new PEEvidenceBean();
            //create a TPBDataTypeBean
            TPBDataTypeBean tpbDataTypeBean = new TPBDataTypeBean();
            //set the data type
            tpbDataTypeBean.setDataType(DataType.PE_MS_SAM.type());
            //set the traffic lights level to 3
            tpbDataTypeBean.setLevel(TLLevel.TL3.level());
            peMsSamplesEvidence.setTpbDataTypeBean(tpbDataTypeBean);

            peMsSamplesEvidence.setEvidenceValue(PE_MS_SAMPLE_EVIDENCE_TEXT_PART1 + samples + PE_MS_SAMPLE_EVIDENCE_TEXT_PART2);
            peMsSamplesEvidence.setHyperlink(GPM_HYPERLINK_PROB_SAM_BASE + enspAc + GPM_HYPERLINK_PROB_SAM_BASE_EXT);
            int samplesValue = Integer.valueOf(samples).intValue();

            if (samplesValue >= 100) {
                peMsSamplesEvidence.setColorLevel(ColorType.GREEN.color());
            }
            if (samplesValue >= 20 && samplesValue < 100) {
                peMsSamplesEvidence.setColorLevel(ColorType.YELLOW.color());
            }
            if (samplesValue > 0 && samplesValue < 20) {
                peMsSamplesEvidence.setColorLevel(ColorType.RED.color());
            }
            if (samplesValue <= 0) {
                peMsSamplesEvidence.setColorLevel(ColorType.BLACK.color());
            }
            return peMsSamplesEvidence;
        }

        return null;
    }

    public static void main(String[] art) throws Exception {
        GPMWSXmlParser parser = new GPMWSXmlParser();
        String gpmFileName = "./testData/2012.9.23.gpm2tpb.xml";
        List<GPMEntryBean> gpmEntryBeans = parser.parseGPMXml(gpmFileName, WSXmlInputFactory.getInputFactoryConfiguredForXmlConformance());

        for (GPMEntryBean gpmEntryBean : gpmEntryBeans) {
            System.out.println("Db name: " + gpmEntryBean.getPrimaryDbSourceBean().getDbName() + " - ensg: " + gpmEntryBean.getGeneBean().getEnsgAccession());
            PEEvidenceBean pemsProbEvidence = gpmEntryBean.getPeMsProbEvidenceBean();
            System.out.println("PE MS Prob evidence: " + pemsProbEvidence.getEvidenceValue() + " color level: " + pemsProbEvidence.getColorLevel());
            PEEvidenceBean pemsSamplesEvidence = gpmEntryBean.getPeMsSamplesEvidenceBean();
            System.out.println("PE MS Samples Evidence: " + pemsSamplesEvidence.getEvidenceValue() + " color level: " + pemsSamplesEvidence.getColorLevel());
        }
        System.out.println("================== total size: " + gpmEntryBeans.size());
    }
}
