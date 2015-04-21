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

import edu.monash.merc.common.name.*;
import edu.monash.merc.dto.*;
import org.apache.commons.lang.StringUtils;
import org.xml.sax.Attributes;
import org.xml.sax.SAXException;
import org.xml.sax.helpers.DefaultHandler;

import java.util.ArrayList;
import java.util.List;

/**
 * @author Simon Yu
 *         <p/>
 *         Email: xiaoming.yu@monash.edu
 * @version 1.0
 * @since 1.0
 *        <p/>
 *        Date: 24/07/13 12:31 PM
 */
public class NXHandler extends DefaultHandler {

    private List<NXEntryBean> nxEntryBeans = new ArrayList<NXEntryBean>();

    private NXEntryBean nxEntryBean;

    private AccessionBean identifiedAccession;

    private GeneBean geneBean;

    //accessions and dbsource in the identifiers
    private List<DbSourceAcEntryBean> dbAcEntryBeans;

    private NXPeMsAntiEntryBean peMsAntiEntryBean;

    //all pe ms ann evidences for a xrefs
    List<PEEvidenceBean> peMsAnnEvidenceBeans;

    //individual pe ms ann evidence in the xrefs/xref if any
    private PEEvidenceBean peMsAnnEvidenceBean;

    //individual pe anti ann evidence in the xrefs/xref if any
    private PEEvidenceBean peAntiAnnEvidenceBean;

    // xref three attributes 1. database, 2. category, 3. accession
    private String xrefDatabase;

    private String xrefCategory;

    private String xrefAccession;

    private int peAntiAnnCounter = 0;

    private String peAntiURL;

    private boolean peMsAnnEvExisted = false;

    private boolean peAntiAnnEvExisted = false;

    private boolean mainProteinDesc = false;

    private boolean mainGeneSymbol = false;

    private XMLStack pathStack = new XMLStack();

    private StringBuilder stringBuilder = new StringBuilder();

    public List<NXEntryBean> getNxEntryBeans() {
        return nxEntryBeans;
    }

    public void startElement(String uri, String localName, String qName, Attributes attributes) throws SAXException {
        pathStack.push(qName);
        //Reset the StringBuilder
        stringBuilder.setLength(0);

        //get the current from the stack
        String currentPath = pathStack.currentPath();

        //start - nextprotExport/proteins/protein
        if (currentPath.equalsIgnoreCase(NXFields.PATH_PROTEIN)) {
            String nxAc = attributes.getValue(NXFields.ATTR_PROTEIN_UNIQUE_NAME);
            //create NextProt entry bean
            nxEntryBean = new NXEntryBean();
            //set the db source name
            nxEntryBean.setDbSourceName(DbAcType.NextProt.type());

            //create a AccessionBean
            identifiedAccession = new AccessionBean();
            identifiedAccession.setAccession(nxAc);
            identifiedAccession.setAcType(DbAcType.NextProt.type());
            nxEntryBean.setIdentifiedAccessionBean(identifiedAccession);

            //create geneBean
            geneBean = new GeneBean();
            geneBean.setDisplayName(NameType.UNKNOWN.cn());
            geneBean.setChromosome(ChromType.UNKNOWN.chm());
            //set the GeneBean
            nxEntryBean.setGeneBean(geneBean);

            //create DbSourceAcEntryBean list for storing the  each DbSourceAcEntryBean
            dbAcEntryBeans = new ArrayList<DbSourceAcEntryBean>();
            //add the nextport access and nextport dbsource
            DbSourceAcEntryBean nxDbSourceAcEntryBean = createDbAcEntryBean(DbAcType.NextProt.type(), nxAc, DbAcType.NextProt.type());
            dbAcEntryBeans.add(nxDbSourceAcEntryBean);
        }

        //start -- nextprotExport/proteins/protein/proteinExistence
        if (currentPath.equalsIgnoreCase(NXFields.PATH_PROTEIN_EXISTENCE)) {
            String evidence = attributes.getValue(NXFields.ATTR_VALUE);
            if (StringUtils.isNotBlank(evidence)) {
                String nxAc = nxEntryBean.getIdentifiedAccessionBean().getAccession();
                //Create PE TE OTH CUR evidence
                NXPeTeOthEntryBean nxPeTeOthEntryBean = createPeTeOthEvidencesBean(evidence, nxAc);
                //add the NXPeTeOthEntryBean
                nxEntryBean.setNxPeTeOthEntryBean(nxPeTeOthEntryBean);
            }
        }


        //start - protein/proteinNames/entityName Element
        if (currentPath.equalsIgnoreCase(NXFields.PATH_PROTEIN_DESC_MAIN_ENTITY)) {
            mainProteinDesc = Boolean.valueOf(attributes.getValue(NXFields.ATTR_MAIN_NAME)).booleanValue();
        }

        //start - nextprotExport/proteins/protein/chromosomalLocations/chromosomalLocation
        if (currentPath.equalsIgnoreCase(NXFields.PATH_CHROM_LOCATION)) {
            String chrom = attributes.getValue(NXFields.ATTR_CHROMOSOME);
            String band = attributes.getValue(NXFields.ATTR_BAND);
            String strand = attributes.getValue(NXFields.ATTR_STRAND);
            String ensgAc = attributes.getValue(NXFields.ATTR_ACCESSION);
            if (StringUtils.isNotBlank(chrom)) {
                nxEntryBean.getGeneBean().setChromosome(chrom);
            }
            if (StringUtils.isNotBlank(band)) {
                nxEntryBean.getGeneBean().setBand(band);
            }
            if (StringUtils.isNotBlank(strand)) {
                nxEntryBean.getGeneBean().setStrand(strand);
            }
            if (StringUtils.isNotBlank(ensgAc)) {
                nxEntryBean.getGeneBean().setEnsgAccession(ensgAc);
            }
        }

        //start - nextprotExport/proteins/protein/geneNames/entityName
        if (currentPath.equalsIgnoreCase(NXFields.PATH_GENE_SYMBOL_MAIN_ENTITY)) {
            mainGeneSymbol = Boolean.valueOf(attributes.getValue(NXFields.ATTR_MAIN_NAME)).booleanValue();
        }

        //start - nextprotExport/proteins/protein/identifiers
        if (currentPath.equalsIgnoreCase(NXFields.PATH_IDENTIFIERS)) {
            //do nothing,
        }

        //start -- nextprotExport/proteins/protein/identifiers/identifier
        if (currentPath.equalsIgnoreCase(NXFields.PATH_IDENTIFIER)) {
            String type = attributes.getValue(NXFields.ATTR_TYPE);
            String acValue = attributes.getValue(NXFields.ATTR_NAME);
            String dbName = attributes.getValue(NXFields.ATTR_DATABASE);

            //create DbSourceAcEntryBean for individual identifier
            DbSourceAcEntryBean dbSourceAcEntryBean = createDbAcEntryBean(type, acValue, dbName);
            //add this DbSourceAcEntryBean into list
            if (dbSourceAcEntryBean != null) {
                dbAcEntryBeans.add(dbSourceAcEntryBean);
            }
        }

        //start -- nextprotExport/proteins/protein/xrefs
        if (currentPath.equalsIgnoreCase(NXFields.PATH_XREFS)) {
            //create a pe ms an pe anti entry bean
            peMsAntiEntryBean = new NXPeMsAntiEntryBean();

            //create pe ms ann evidence bean list, to store all pe ms ann evidences under the xrefs/xref
            peMsAnnEvidenceBeans = new ArrayList<PEEvidenceBean>();

            //create a pe anti ann evidence empty object
            peAntiAnnEvidenceBean = new PEEvidenceBean();

            //reset peAntiAnnCounter
            peAntiAnnCounter = 0;
        }


        //start -- nextprotExport/proteins/protein/xrefs/xref
        if (currentPath.equalsIgnoreCase(NXFields.PATH_XREF)) {
            String database = attributes.getValue(NXFields.ATTR_XREF_DATABASE);
            String category = attributes.getValue(NXFields.ATTR_XREF_CATEGORY);
            String accession = attributes.getValue(NXFields.ATTR_XREF_ACCESSION);
            this.xrefDatabase = database;
            this.xrefCategory = category;
            this.xrefAccession = accession;

            int colorLevel = findPeMsAnnColorLevel(database, category);
            if (colorLevel != -1) {
                //create a pe ms ann evidence object
                peMsAnnEvidenceBean = new PEEvidenceBean();
                peMsAnnEvidenceBean.setColorLevel(colorLevel);
                String peMsAnnEv = xrefDatabase + " - " + xrefAccession;
                if (StringUtils.isNotBlank(peMsAnnEv)) {
                    peMsAnnEvidenceBean.setEvidenceValue(peMsAnnEv);
                }
                //create a TPBDataTypeBean
                TPBDataTypeBean tpbDataTypeBean = new TPBDataTypeBean();
                //set the data type
                tpbDataTypeBean.setDataType(DataType.PE_MS_ANN.type());
                //set the traffic lights level to 3
                tpbDataTypeBean.setLevel(TLLevel.TL3.level());
                peMsAnnEvidenceBean.setTpbDataTypeBean(tpbDataTypeBean);
                peMsAnnEvExisted = true;
            }
            //count pe anti ann
            countPeAntiEvidences(database, category, accession);
        }
    }

    @Override
    public void endElement(String uri, String localName, String qName) throws SAXException {
        //get current path from the stack
        String currentPath = pathStack.currentPath();

        //end - nextprotExport/proteins/protein
        if (currentPath.equalsIgnoreCase(NXFields.PATH_PROTEIN)) {
            nxEntryBeans.add(nxEntryBean);
        }

        //end - protein/proteinNames/entityName/value end
        if (currentPath.equalsIgnoreCase(NXFields.PATH_PROTEIN_DESC)) {
            String desc = stringBuilder.toString();
            identifiedAccession.setDescription(desc);
            mainProteinDesc = false;
        }

        //end - nextprotExport/proteins/protein/geneNames/entityName/value
        if (currentPath.equalsIgnoreCase(NXFields.PATH_GENE_SYMBOL)) {
            String geneSymbol = stringBuilder.toString();
            nxEntryBean.getGeneBean().setDisplayName(geneSymbol);
            mainGeneSymbol = false;
        }

        //end - nextprotExport/proteins/protein/identifiers
        if (currentPath.equalsIgnoreCase(NXFields.PATH_IDENTIFIERS)) {
            //Set the DbSourceAcEntryBean list
            nxEntryBean.setDbSourceAcEntryBeans(dbAcEntryBeans);
        }

        //process pe ms ann and pe anti ann evidence hyperlink.
        //end of nextprotExport/proteins/protein/xrefs/xref/url
        if (currentPath.equalsIgnoreCase(NXFields.PATH_XREF_URL)) {
            String xrefUrl = stringBuilder.toString();
            xrefUrl = StringUtils.trim(xrefUrl);
            if (peMsAnnEvExisted) {
                peMsAnnEvidenceBean.setHyperlink(xrefUrl);
            }
            //find peAntiUrl
            findPeAnTiAnnURL(xrefDatabase, xrefCategory, xrefAccession, xrefUrl);
            //reset the database, category and accession
            xrefDatabase = null;
            xrefCategory = null;
            xrefAccession = null;
        }

        //process individual pe ms ann evidence bean
        //end -- nextprotExport/proteins/protein/xrefs/xref
        if (currentPath.equalsIgnoreCase(NXFields.PATH_XREF)) {
            //add the pe ms ann evidence bean into list
            if (peMsAnnEvExisted) {
                peMsAnnEvidenceBeans.add(peMsAnnEvidenceBean);
                peMsAnnEvExisted = false;
            }
        }

        //end -- nextprotExport/proteins/protein/xrefs
        if (currentPath.equalsIgnoreCase(NXFields.PATH_XREFS)) {
            //add the pe ms ann evidence bean list (peMsAnnEvidenceBeans)into NXPeMsAntiEntryBean
            peMsAntiEntryBean.setPeMsAnnEvidenceBeans(peMsAnnEvidenceBeans);

            //create pe anti ann evidence if it exists
            if (peAntiAnnEvExisted) {

                if (peAntiAnnCounter == 0) {
                    peAntiAnnEvidenceBean.setColorLevel(ColorType.BLACK.color());
                }

                if (peAntiAnnCounter == 1) {
                    peAntiAnnEvidenceBean.setColorLevel(ColorType.RED.color());
                }

                if (peAntiAnnCounter > 1) {
                    peAntiAnnEvidenceBean.setColorLevel(ColorType.YELLOW.color());
                }

                peAntiAnnEvidenceBean.setEvidenceValue(peAntiAnnCounter + " " + NXConts.XREF_HPA_ANTIBODY_DESC);
                peAntiAnnEvidenceBean.setHyperlink(peAntiURL);
                //create a TPBDataTypeBean
                TPBDataTypeBean tpbDataTypeBean = new TPBDataTypeBean();
                //set the data type
                tpbDataTypeBean.setDataType(DataType.PE_ANTI_ANN.type());
                //set the traffic lights level to 3
                tpbDataTypeBean.setLevel(TLLevel.TL3.level());
                peAntiAnnEvidenceBean.setTpbDataTypeBean(tpbDataTypeBean);
                //set the pe anti ann object
                peMsAntiEntryBean.setPeAntiAnnEvidenceBean(peAntiAnnEvidenceBean);
                //dpn't forget to rest pe anti ann evidence existed flag to false.
                peAntiAnnEvExisted = false;
                //reset peAntiURL to null.
                peAntiURL = null;
            }

            //and
            nxEntryBean.setNxPeMsAntiEntryBean(peMsAntiEntryBean);
        }

        //finally pop the current path as it's end of current element.
        pathStack.pop();
    }

    @Override
    public void characters(char ch[], int start, int length) throws SAXException {
        //super the super-class
        super.characters(ch, start, length);
        //using a stringbuilder to get all characters during startElement and endElement processing
        stringBuilder.append(ch, start, length);
    }

    private NXPeTeOthEntryBean createPeTeOthEvidencesBean(String evidence, String nxAccession) {
        if (StringUtils.isNotBlank(evidence)) {
            //create NXPeTeOthEntryBean to store peOthCur Evidence and teOthCur Evidence
            NXPeTeOthEntryBean nxPeTeOthEntryBean = new NXPeTeOthEntryBean();

            //nextprot pe oth cur evidence
            PEEvidenceBean nxPeOthEvidenceBean = new PEEvidenceBean();

            //If it's protein level, we set the evidence level as green level, else we set it as black for pe
            if (StringUtils.containsIgnoreCase(evidence, NXFields.EV_PROTEIN_LEVEL)) {
                nxPeOthEvidenceBean.setColorLevel(ColorType.GREEN.color());
            } else {
                nxPeOthEvidenceBean.setColorLevel(ColorType.BLACK.color());
            }

            //create a TPBDataTypeBean for PE Oth Cur
            TPBDataTypeBean peOthCurTDT = new TPBDataTypeBean();
            //set the data type
            peOthCurTDT.setDataType(DataType.PE_OTH_CUR.type());
            //set the traffic lights level to 3
            peOthCurTDT.setLevel(TLLevel.TL3.level());
            nxPeOthEvidenceBean.setTpbDataTypeBean(peOthCurTDT);

            nxPeOthEvidenceBean.setEvidenceValue(evidence);
            nxPeOthEvidenceBean.setHyperlink(NXFields.NX_BASE_URL + nxAccession);


            //Nextprot te oth cur evidence
            TEEvidenceBean nxTeOthEvidenceBean = new TEEvidenceBean();

            if (StringUtils.containsIgnoreCase(evidence, NXFields.EV_PROTEIN_LEVEL) || StringUtils.containsIgnoreCase(evidence, NXFields.EV_TRANSCRIPT_LEVEL)) {
                nxTeOthEvidenceBean.setColorLevel(ColorType.GREEN.color());
            } else {
                nxTeOthEvidenceBean.setColorLevel(ColorType.BLACK.color());
            }

            //create a TPBDataTypeBean for TE Oth Cur
            TPBDataTypeBean teOthCurTDT = new TPBDataTypeBean();
            //set the data type
            teOthCurTDT.setDataType(DataType.TE_OTH_CUR.type());
            //set the traffic lights level to 3
            teOthCurTDT.setLevel(TLLevel.TL3.level());
            nxTeOthEvidenceBean.setTpbDataTypeBean(teOthCurTDT);
            nxTeOthEvidenceBean.setEvidenceValue(evidence);
            nxTeOthEvidenceBean.setHyperlink(NXFields.NX_BASE_URL + nxAccession);
            //add these two evidence beans
            nxPeTeOthEntryBean.setNxPeOthEvidenceBean(nxPeOthEvidenceBean);
            nxPeTeOthEntryBean.setNxTeOthEvidenceBean(nxTeOthEvidenceBean);
            return nxPeTeOthEntryBean;
        }
        return null;
    }

    private DbSourceAcEntryBean createDbAcEntryBean(String acType, String accession, String dbName) {
        if (StringUtils.isNotBlank(acType) && StringUtils.isNotBlank(accession)) {
            DbSourceAcEntryBean nxDbSourceAcEntryBean = new DbSourceAcEntryBean();
            DBSourceBean dbSourceBean = new DBSourceBean();
            AccessionBean accessionBean = new AccessionBean();
            //set accession type
            accessionBean.setAcType(acType);
            //set accession
            accessionBean.setAccession(accession);

            //set for dbsource
            if (StringUtils.isNotBlank(dbName)) {
                dbSourceBean.setDbName(dbName);
            } else {
                dbSourceBean.setDbName(DbAcType.Unknown.type());
            }
            //set the accession bean
            nxDbSourceAcEntryBean.setAccessionBean(accessionBean);
            //set the dbsource bean
            nxDbSourceAcEntryBean.setDbSourceBean(dbSourceBean);
            return nxDbSourceAcEntryBean;
        }
        return null;
    }

    //find the pe ms ann color level
    private int findPeMsAnnColorLevel(String database, String category) {
        if (StringUtils.isNotBlank(database) && (StringUtils.isNotBlank(category))) {
            if (StringUtils.equalsIgnoreCase(database, NXFields.XREF_DB_PRIDE) && StringUtils.equalsIgnoreCase(category, NXFields.XREF_CA_PROTEOMIC_DATABASES)) {
                return ColorType.RED.color();
            }
            if (StringUtils.equalsIgnoreCase(database, NXFields.XREF_DB_PEPTIDE_ATLAS) && StringUtils.equalsIgnoreCase(category, NXFields.XREF_CA_PROTEOMIC_DATABASES)) {
                return ColorType.YELLOW.color();
            }
        }
        // negative 1 means no pe ms ann evidence
        return -1;
    }

    //count the pe anti ann evidence
    private void countPeAntiEvidences(String database, String category, String accession) {
        if (StringUtils.isNotBlank(database) && StringUtils.isNotBlank(category) && StringUtils.isNotBlank(accession)) {
            if (StringUtils.equalsIgnoreCase(database, NXFields.XREF_DB_HPA) && StringUtils.equalsIgnoreCase(category, NXFields.XREF_CA_ANTIBODY_DATABASES)) {
                //ony hpa and cab accession will be taken into account
                if (StringUtils.startsWithIgnoreCase(accession, NXFields.XREF_AC_PREFIX_HPA) || StringUtils.startsWithIgnoreCase(accession, NXFields.XREF_AC_PREFIX_CAB)) {
                    peAntiAnnCounter++;
                }
                peAntiAnnEvExisted = true;
            }
        }
    }

    //find pe anti ann hyperlink url
    private void findPeAnTiAnnURL(String xdatabase, String xcategory, String xaccession, String xrefUrl) {

        if (StringUtils.isNotBlank(xdatabase) && StringUtils.isNotBlank(xcategory) && StringUtils.isNotBlank(xaccession)) {
            if (StringUtils.equalsIgnoreCase(xdatabase, NXFields.XREF_DB_HPA) && StringUtils.equalsIgnoreCase(xcategory, NXFields.XREF_CA_ANTIBODY_DATABASES)) {
                //using the ENSG accession as perfer hyperlink
                if (StringUtils.startsWithIgnoreCase(xaccession, NXConts.XREF_AC_PREFIX_ENSG)) {
                    if (StringUtils.isNotBlank(xrefUrl)) {
                        peAntiURL = StringUtils.removeEndIgnoreCase(xrefUrl, NXFields.XREF_AC_ENSG_URL_END_PART);
                    }
                }
                //if no hyperlink for ensg then we have to use others
                if (StringUtils.startsWithIgnoreCase(xrefAccession, NXFields.XREF_AC_PREFIX_HPA) || StringUtils.startsWithIgnoreCase(xrefAccession, NXFields.XREF_AC_PREFIX_CAB)) {
                    if (StringUtils.isBlank(peAntiURL)) {
                        peAntiURL = xrefUrl;
                    }
                }
            }
        }
    }
}
