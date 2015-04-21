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

package edu.monash.merc.system.parser.gpm;

import edu.monash.merc.common.name.ColorType;
import edu.monash.merc.common.name.DataType;
import edu.monash.merc.common.name.DbAcType;
import edu.monash.merc.common.name.TLLevel;
import edu.monash.merc.dto.*;
import edu.monash.merc.dto.gpm.GPMEntryBean;
import edu.monash.merc.exception.DMXMLParserException;
import org.apache.commons.lang.StringUtils;
import org.apache.log4j.Logger;
import org.jaxen.jdom.JDOMXPath;
import org.jdom.Attribute;
import org.jdom.Document;
import org.jdom.Element;
import org.jdom.input.SAXBuilder;

import java.io.InputStream;
import java.util.ArrayList;
import java.util.List;

/**
 * GPMXMLParser class which parses the GPM xml data to extract all data which which provides the evidences of the genes
 *
 * @author Simon Yu
 *         <p/>
 *         Email: xiaoming.yu@monash.edu
 * @version 1.0
 * @see edu.monash.merc.system.parser.xml.GPMWSXmlParser
 * @since 1.0
 *        <p/>
 *        Date: 30/07/12 3:48 PM
 * @deprecated Replaced edu.monash.merc.system.parser.xml.GPMWSXmlParser
 */
public class GPMXMLParser {

    private static String GPM_PROTEIN_XPATH = "//gpmdbsummary/protein";

    private static String ELE_GPM_PROTEIN = "protein";

    private static String ATTR_GPM_ENSP = "ensp";

    private static String ATTR_GPM_ENSG = "ensg";

    private static String ELE_GPM_IDENTIFICATION = "identification";

    private static String ATTR_GPM_IDEN_BESTE = "beste";

    private static String ATTR_GPM_IDEN_SAMPLES = "samples";


    private SAXBuilder parser = new SAXBuilder();

    private Logger logger = Logger.getLogger(this.getClass().getName());

    @SuppressWarnings("unchecked")
    public List<GPMEntryBean> parseGPMXML(InputStream xmlStream) {
        //for each protein, parsed by protein path
        JDOMXPath proteinPath = null;
        Document xmlDoc = null;
        List<Element> proteinElements = new ArrayList<Element>();
        //build the protein path
        try {
            proteinPath = new JDOMXPath(GPM_PROTEIN_XPATH);
        } catch (Exception ex) {
            logger.error(ex);
            throw new DMXMLParserException(ex);
        }

        List<GPMEntryBean> gpmEntryBeans = new ArrayList<GPMEntryBean>();
        try {
            xmlDoc = this.parser.build(xmlStream);
            proteinElements = proteinPath.selectNodes(xmlDoc);

            //for each protein
            for (Element protein : proteinElements) {

                GPMEntryBean gpmEntryBean = new GPMEntryBean();
                //create gpm dbsource
                DBSourceBean gpmDbSourceBean = new DBSourceBean();
                gpmDbSourceBean.setDbName(DbAcType.GPM.type());
                gpmDbSourceBean.setPrimaryEvidences(true);
                //set the gpm dbsource
                gpmEntryBean.setPrimaryDbSourceBean(gpmDbSourceBean);


                //get ensp accession
                Attribute enspAttr = protein.getAttribute(ATTR_GPM_ENSP);
                String enspAc = null;
                if (enspAttr != null) {
                    enspAc = enspAttr.getValue();
                }

                //get ensg accession
                Attribute ensgAttr = protein.getAttribute(ATTR_GPM_ENSG);
                String ensgAc = null;
                if (ensgAttr != null) {
                    ensgAc = ensgAttr.getValue();
                }

                //create a gene bean
                if (StringUtils.isNotBlank(ensgAc)) {
                    GeneBean geneBean = new GeneBean();
                    geneBean.setEnsgAccession(ensgAc);
                    gpmEntryBean.setGeneBean(geneBean);
                }

                //create an identified accession bean
                AccessionBean identAccessionBean = parseIdentifiedAccessionBean(enspAc);
                gpmEntryBean.setIdentifiedAccessionBean(identAccessionBean);
                //create dbsource and accession entry bean
                List<DbSourceAcEntryBean> dbSourceAcEntryBeanList = parseDBSourceAcEntryBeans(enspAc, ensgAc);
                gpmEntryBean.setDbSourceAcEntryBeans(dbSourceAcEntryBeanList);

                //get the identification element
                Element identEle = protein.getChild(ELE_GPM_IDENTIFICATION);
                //parse pe ms prob evidence
                PEEvidenceBean peMsProbEvidence = parseMsProbEvidence(identEle, enspAc);
                //parse pe ms samples evidence
                PEEvidenceBean peMsSamplesEvidence = parseMsSamplesEvidence(identEle, enspAc);
                if (peMsProbEvidence != null) {
                    gpmEntryBean.setPeMsProbEvidenceBean(peMsProbEvidence);
                }
                if (peMsSamplesEvidence != null) {
                    gpmEntryBean.setPeMsSamplesEvidenceBean(peMsSamplesEvidence);
                }

                //if gene is not null for this gpm entry, then we add it to the list
                GeneBean geneBean = gpmEntryBean.getGeneBean();
                if (geneBean != null) {
                    gpmEntryBeans.add(gpmEntryBean);
                }
            }
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
        return gpmEntryBeans;
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

    private PEEvidenceBean parseMsProbEvidence(Element identEle, String enspAc) {
        if (identEle != null) {
            Attribute besteAttr = identEle.getAttribute(ATTR_GPM_IDEN_BESTE);
            if (besteAttr != null) {
                PEEvidenceBean peMsProbEvidence = new PEEvidenceBean();
                //create a TPBDataTypeBean
                TPBDataTypeBean tpbDataTypeBean = new TPBDataTypeBean();
                //set the data type
                tpbDataTypeBean.setDataType(DataType.PE_MS_PROB.type());
                //set the traffic lights level to 3
                tpbDataTypeBean.setLevel(TLLevel.TL3.level());
                peMsProbEvidence.setTpbDataTypeBean(tpbDataTypeBean);

                String beste = besteAttr.getValue();
                peMsProbEvidence.setEvidenceValue(GPMConts.PE_MS_PROB_EVIDENCE_TEXT + beste);
                peMsProbEvidence.setHyperlink(GPMConts.GPM_HYPERLINK_PROB_SAM_BASE + enspAc + GPMConts.GPM_HYPERLINK_PROB_SAM_BASE_EXT);
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
        }
        return null;
    }

    private PEEvidenceBean parseMsSamplesEvidence(Element identEle, String enspAc) {
        if (identEle != null) {
            Attribute samplesAttr = identEle.getAttribute(ATTR_GPM_IDEN_SAMPLES);
            if (samplesAttr != null) {
                String samples = samplesAttr.getValue();

                PEEvidenceBean peMsSamplesEvidence = new PEEvidenceBean();
                //create a TPBDataTypeBean
                TPBDataTypeBean tpbDataTypeBean = new TPBDataTypeBean();
                //set the data type
                tpbDataTypeBean.setDataType(DataType.PE_MS_SAM.type());
                //set the traffic lights level to 3
                tpbDataTypeBean.setLevel(TLLevel.TL3.level());
                peMsSamplesEvidence.setTpbDataTypeBean(tpbDataTypeBean);

                peMsSamplesEvidence.setEvidenceValue(GPMConts.PE_MS_SAMPLE_EVIDENCE_TEXT_PART1 + samples + GPMConts.PE_MS_SAMPLE_EVIDENCE_TEXT_PART2);
                peMsSamplesEvidence.setHyperlink(GPMConts.GPM_HYPERLINK_PROB_SAM_BASE + enspAc + GPMConts.GPM_HYPERLINK_PROB_SAM_BASE_EXT);
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
        }
        return null;
    }

}
