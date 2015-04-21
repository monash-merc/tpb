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

package edu.monash.merc.struts2.action;

import au.com.bytecode.opencsv.CSVWriter;
import edu.monash.merc.common.name.ChromType;
import edu.monash.merc.common.name.DataType;
import edu.monash.merc.common.name.DbAcType;
import edu.monash.merc.common.page.Pagination;
import edu.monash.merc.common.sql.OrderBy;
import edu.monash.merc.common.sql.OrderCondition;
import edu.monash.merc.common.sql.SqlCondition;
import edu.monash.merc.domain.TLColor;
import edu.monash.merc.domain.TLGene;
import edu.monash.merc.domain.TPBVersion;
import edu.monash.merc.domain.TrafficLight;
import edu.monash.merc.dto.TLEvidenceResponse;
import edu.monash.merc.dto.TLEvidenceSummary;
import edu.monash.merc.dto.TLSumReporter;
import edu.monash.merc.dto.TLTypeSumReporter;
import edu.monash.merc.dto.tl.TLSearchBean;
import edu.monash.merc.dto.tl.TLTypeEvLevelFilter;
import edu.monash.merc.exception.DMException;
import edu.monash.merc.system.config.SystemPropConts;
import edu.monash.merc.util.DMUtil;
import org.apache.commons.lang.StringUtils;
import org.apache.log4j.Logger;
import org.springframework.context.annotation.Scope;
import org.springframework.stereotype.Controller;

import javax.annotation.PostConstruct;
import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.InputStream;
import java.io.OutputStreamWriter;
import java.util.*;

/**
 * TrafficLightAction action class which handles the all traffic light action requests
 *
 * @author Simon Yu
 *         <p/>
 *         Email: xiaoming.yu@monash.edu
 * @version 1.0
 * @since 1.0
 *        <p/>
 *        Date: 30/05/12 12:56 PM
 */
@Scope("prototype")
@Controller("tl.tlAction")
public class TrafficLightAction extends BaseAction {

    /**
     * pagination of traffic light
     */
    private Pagination<TrafficLight> tlPagination;

    /**
     * traffic light gene id
     */
    private long tlGeneId;

    /**
     * gene id
     */
    private long geneId;

    /**
     * tpb data type
     */
    private String tpbDataType;

    /**
     * datasource
     */
    private String dbSource;

    /**
     * traffic light evidence json response
     */
    private TLEvidenceResponse tlEvidenceResponse;

    /**
     * Logging object
     */
    private Logger logger = Logger.getLogger(this.getClass().getName());

    /**
     * tpb data type map
     */
    private HashMap<String, String> tpbDataTypes = new LinkedHashMap<String, String>();

    /**
     * gene symbol or accession type
     */
    private HashMap<String, String> geneValueTypes = new LinkedHashMap<String, String>();

    /**
     * json response success flag
     */
    private static String SUCCESS_RESPONSE = "true";

    /**
     * json response failure flag
     */
    private static String FAILED_RESPONSE = "false";

    /**
     * shortcut parameter name for the chomosome
     */
    private String chm;

    /**
     * shortcut parameter name for the datasource combination
     */
    private int tt;

    /**
     * shortcut parameter name for the version of the traffic light
     */
    private long vid;

    /**
     * traffic light summary reporter json response object
     */
    private TLSumReporter tlSumReporter;

    // accessions csv file export
    private String contentType;

    /**
     * accessions cvs file inputstream
     */
    private InputStream csvInputStream;

    /**
     * accessions csv file content disposition
     */
    private String contentDisposition;

    /**
     * accessions csv file exporting buffer size
     */
    private int bufferSize;

    /**
     * session key for the filter conditions
     */
    private static String SESS_FILTER_CONDS = "sess_filter_conds";

    /**
     * PostConstruct method which is called after action instantialized
     */
    @PostConstruct
    public void initTL() {
        tpbDataTypes.put("PE", "PE");
        tpbDataTypes.put("PE_MS", "PE MS");
        tpbDataTypes.put("PE_MS_ANN", "PE MS ANN");
        tpbDataTypes.put("PE_MS_PROB", "PE MS PROB");
        tpbDataTypes.put("PE_MS_SAM", "PE MS SAM");
        tpbDataTypes.put("PE_ANTI", "PE ANTI");
        tpbDataTypes.put("PE_ANTI_ANN", "PE ANTI ANN");
        tpbDataTypes.put("PE_ANTI_IHC", "PE ANTI IHC");
        tpbDataTypes.put("PE_ANTI_IHC_NORM", "PE ANTI IHC NORM");
        tpbDataTypes.put("PE_OTH", "PE OTH");
        tpbDataTypes.put("PE_OTH_CUR", "PE OTH CUR");
        tpbDataTypes.put("PTM", "PTM");
        tpbDataTypes.put("PTM_PHS", "PTM PHS");
        tpbDataTypes.put("PTM_PHS_SER", "PTM PHS SER");
        tpbDataTypes.put("PTM_PHS_THR", "PTM PHS THR");
        tpbDataTypes.put("PTM_PHS_TYR", "PTM PHS TYR");
        tpbDataTypes.put("PTM_ACE", "PTM ACE");
        tpbDataTypes.put("PTM_ACE_LYS", "PTM ACE LYS");
        tpbDataTypes.put("PTM_ACE_NTA", "PTM ACE NTA");
        tpbDataTypes.put("TE", "TE");
        tpbDataTypes.put("TE_MA", "TE MA");
        tpbDataTypes.put("TE_MA_PROP", "TE MA PROP");
        tpbDataTypes.put("TE_OTH", "TE OTH");
        tpbDataTypes.put("TE_OTH_CUR", "TE OTH CUR");
        //gene value type
        geneValueTypes.put("symbol", "gene symbol");
        geneValueTypes.put("accession", "gene accession");
    }

    /**
     * view the traffic light based on the selected filter conditions
     *
     * @return SUCCESS if there is no error
     */
    public String trafficLight() {
        try {
            //check errors for any shortcut parameters if any
            if (checkShortcutParamsErrors()) {
                return INPUT;
            }
            //check input errors
            if (hasInputErrors()) {
                return INPUT;
            }
            //preparing for traffic light condtions
            String selectedChromType = tlSearchBean.getSelectedChromType();
            ChromType chromType = ChromType.fromType(selectedChromType);
            int combinatedDBSToken = tlSearchBean.getCombinatedToken();
            //post-process the tpb versions
            processTPBVersions(combinatedDBSToken, selectedChromType);

            //if no version provided, we just get the latest version for the current dbsource combination
            long selectedVersion = tlSearchBean.getSelectedVersion();
            if (selectedVersion == -1 || selectedVersion == 0) {
                TPBVersion tpbVersion = this.dmSystemService.getCurrentVersion(chromType, combinatedDBSToken);
                if (tpbVersion != null) {
                    selectedVersion = tpbVersion.getId();
                    tlSearchBean.setSelectedVersion(selectedVersion);
                }
            }

            //added the traffic light order by the gene's start position.
            OrderCondition orderCon = SqlCondition.orderCon().add(OrderBy.asc("startPosition"));
            OrderBy[] orderBys = orderCon.orderBy();

            tlPagination = this.dmSystemService.getVersionTrafficLight(tlSearchBean, 1, -1, orderBys);

            //save the search condition in the session for exporting the gene accessions as a csv file
            storeInSession(SESS_FILTER_CONDS, tlSearchBean);
        } catch (Exception ex) {
            logger.error(ex);
            addActionError(getText("tl.failed.to.display.the.traffic.light"));
            return ERROR;
        }
        return SUCCESS;
    }

    /**
     * view the traffic light summary report based on the selected filter conditions
     *
     * @return SUCCESS if there is no error
     */
    public String report() {
        try {
            //check input errors
            if (hasInputErrors()) {
                tlSumReporter = new TLSumReporter();
                tlSumReporter.setSucceed(false);
                tlSumReporter.setMsg(getText("tl.filter.invalid"));
            }
            //preparing for traffic light condtions
            String selectedChromType = tlSearchBean.getSelectedChromType();
            ChromType chromType = ChromType.fromType(selectedChromType);
            int combinatedDBSToken = tlSearchBean.getCombinatedToken();
            //post-process the tpb versions
            processTPBVersions(combinatedDBSToken, selectedChromType);

            //if no version provided, we just get the latest version for the current dbsource combination
            long selectedVersion = tlSearchBean.getSelectedVersion();
            if (selectedVersion == -1 || selectedVersion == 0) {
                TPBVersion tpbVersion = this.dmSystemService.getCurrentVersion(chromType, combinatedDBSToken);
                if (tpbVersion != null) {
                    selectedVersion = tpbVersion.getId();
                    tlSearchBean.setSelectedVersion(selectedVersion);
                }
            }

            tlSumReporter = this.dmSystemService.getTLSumReporter(tlSearchBean);
            if (tlSumReporter != null) {
                tlSumReporter.setSucceed(true);
                List<TLTypeSumReporter> typeSumReporterses = tlSumReporter.getTlTypeSumReporters();
                if (typeSumReporterses != null && typeSumReporterses.size() > 0) {
                    TLTypeSumReporter firstTypeSumRep = typeSumReporterses.get(0);
                    String chartUrlParam = "dt=" + firstTypeSumRep.getTpbDataType() + "&l4=" + firstTypeSumRep.getLevel4Num() + "&l3=" + firstTypeSumRep.getLevel3Num() + "&l2=" + firstTypeSumRep.getLevel2Num() + "&l1=" + firstTypeSumRep.getLevel1Num();
                    tlSumReporter.setFirstChartParam(chartUrlParam);
                }
            }

            //save the search condition in the session for exporting the gene accessions as a csv file
            storeInSession(SESS_FILTER_CONDS, tlSearchBean);
        } catch (Exception ex) {
            logger.error(ex);
            tlSumReporter = new TLSumReporter();
            tlSumReporter.setSucceed(false);
            tlSumReporter.setMsg(getText("failed.to.create.traffic.light.summary.report"));
        }
        return SUCCESS;
    }

    /**
     * Export the Summary Report table as a csv file
     *
     * @return SUCCESS if there is no error
     */
    public String exportSumReport() {
        try {
            //get the traffic light filter conditions from the session
            TLSearchBean tlSearchBeanInSess = (TLSearchBean) this.findFromSession(SESS_FILTER_CONDS);
            if (tlSearchBeanInSess == null) {
                addActionError(getText("tl.filter.conditions.session.expired"));
                return ERROR;
            } else {
                tlSearchBean = tlSearchBeanInSess;
            }
            //preparing for traffic light condtions
            String selectedChromType = tlSearchBean.getSelectedChromType();
            ChromType chromType = ChromType.fromType(selectedChromType);
            int combinatedDBSToken = tlSearchBean.getCombinatedToken();
            //post-process the tpb versions
            processTPBVersions(combinatedDBSToken, selectedChromType);

            //if no version provided, we just get the latest version for the current dbsource combination
            long selectedVersion = tlSearchBean.getSelectedVersion();
            if (selectedVersion == -1 || selectedVersion == 0) {
                TPBVersion tpbVersion = this.dmSystemService.getCurrentVersion(chromType, combinatedDBSToken);
                if (tpbVersion != null) {
                    selectedVersion = tpbVersion.getId();
                    tlSearchBean.setSelectedVersion(selectedVersion);
                }
            }

            tlSumReporter = this.dmSystemService.getTLSumReporter(tlSearchBean);

            this.csvInputStream = createSRCSVFile(tlSumReporter, tlSearchBean);
            String chromName = tlSearchBean.getSelectedChromType();
            String csvFileName = "tpb_traffic_light_summary_chromosome_" + chromName + ".csv";
            this.contentDisposition = "attachment;filename=\"" + csvFileName + "\"";
            this.bufferSize = 10240;
            this.contentType = "application/octet-stream";

        } catch (Exception ex) {
            logger.error(ex);
            addActionError(getText("tl.failed.to.export.summary.report.csv.file"));
            return ERROR;
        }
        return SUCCESS;
    }

    private void writeExportHeader(CSVWriter csvWriter, TLSearchBean tlSearchBean){
        if(csvWriter!=null && tlSearchBean!=null){
            Date exportedTime = GregorianCalendar.getInstance().getTime();
            csvWriter.writeNext(new String[]{"Export/download date: " + exportedTime});
            //write empty line
            csvWriter.writeNext(new String[]{""});
            String termofuse = this.systemPropSettings.getPropValue(SystemPropConts.TPB_TERM_OF_USE);
            csvWriter.writeNext(new String[]{"Use of the proteome browser traffic lights indicates your acceptance of the Term Of Use at " + termofuse + "."});
            //write empty line
            csvWriter.writeNext(new String[]{""});
            String citationInfo = this.systemPropSettings.getPropValue(SystemPropConts.TPB_CITATION_INFO);
            csvWriter.writeNext(new String[]{citationInfo});
            //write empty line
            csvWriter.writeNext(new String[]{""});

            TPBVersion tpbVersion = this.dmSystemService.getTPBVersionById(tlSearchBean.getSelectedVersion()) ;

            //write the version
            csvWriter.writeNext(new String[]{"Version: " + tpbVersion.getVersionNo()});
            csvWriter.writeNext(new String[]{"Date: " + tpbVersion.getCreatedTime()});
            //write empty line
            csvWriter.writeNext(new String[]{""});

            //Write out filter conditions
            csvWriter.writeNext(new String[]{"Filter conditions:"});

            //write the selected chromosome type.
            csvWriter.writeNext(new String[]{"     Chromosome: "+ tlSearchBean.getSelectedChromType()});

//            //write the selected datasources
            csvWriter.writeNext(new String[]{"     Selected Datasources: "+ tlSearchBean.getSelectedDs()});

            //write the evidence level for a datatype
            List<TLTypeEvLevelFilter> evLevelFilters = tlSearchBean.getTlTypeEvLevelFilters();
            if (evLevelFilters != null && evLevelFilters.size() > 0) {
                csvWriter.writeNext(new String[]{"     Selected Evidence Level of Data Type:"});
                for (TLTypeEvLevelFilter typeEvLevelFilter : evLevelFilters) {
                    if (typeEvLevelFilter.isValidTypeLevel()) {
                        String dataType = typeEvLevelFilter.getDataTypeDisplayName();
                        boolean selectedL4 = typeEvLevelFilter.isTypeEvLevel4();
                        boolean selectedL3 = typeEvLevelFilter.isTypeEvLevel3();
                        boolean selectedL2 = typeEvLevelFilter.isTypeEvLevel2();
                        boolean selectedL1 = typeEvLevelFilter.isTypeEvLevel1();
                        String selectedLevel = "";
                        if (selectedL4) {
                            selectedLevel = "L4 (green)";
                        }
                        if (selectedL3) {
                            if (StringUtils.isBlank(selectedLevel)) {
                                selectedLevel = "L3 (yellow)";
                            } else {
                                selectedLevel += ", " + "L3 (yellow)";
                            }
                        }
                        if (selectedL2) {
                            if (StringUtils.isBlank(selectedLevel)) {
                                selectedLevel = "L2 (red)";
                            } else {
                                selectedLevel += ", " + "L2 (red)";
                            }
                        }
                        if (selectedL1) {
                            if (StringUtils.isBlank(selectedLevel)) {
                                selectedLevel = "L1 (black)";
                            } else {
                                selectedLevel += ", " + "L1 (black)";
                            }
                        }
                        csvWriter.writeNext(new String[]{"          "+ dataType + ": "+ selectedLevel});
                    }
                }
            }


            //write the chromosome region
            long regFrom = tlSearchBean.getRegionFrom();
            long regTo = tlSearchBean.getRegionTo();
            String chromReg = "";
            if ((regFrom == 0 && regTo != 0) || (regFrom != 0 && regTo != 0)) {
                chromReg = "From " + regFrom + " to " + regTo;
            }
            if (regFrom != 0 && regTo == 0) {
                chromReg = "From " + regFrom + " to end";
            }
            if (!chromReg.isEmpty()) {
                csvWriter.writeNext(new String[]{"     Chromosome region: "+ chromReg});
            }

            //write gene symbol or accession list
            String geneListType = tlSearchBean.getSelectedGeneValueType();
            String geneListValue = tlSearchBean.getGeneTypeValues();

            if (StringUtils.isNotBlank(geneListValue)) {
                String geneListValueTrim = DMUtil.replaceAllDelimsByNewDelim(geneListValue, " ", new String[]{",", ";", "\n", "\t"});
                if (StringUtils.equals(geneListType, "symbol")) {
                    csvWriter.writeNext(new String[]{"     Gene Symbol list: "+ geneListValueTrim});
                } else {
                    csvWriter.writeNext(new String[]{"     Gene Accession list: "+ geneListValueTrim});
                }

            }
            csvWriter.writeNext(new String[]{""});

        }
    }

    private InputStream createSRCSVFile(TLSumReporter tlSumReporter, TLSearchBean tlSearchBean) {
        CSVWriter csvWriter = null;
        try {
            ByteArrayOutputStream csvOutputStream = new ByteArrayOutputStream();
            csvWriter = new CSVWriter(new OutputStreamWriter(csvOutputStream), '\t', CSVWriter.DEFAULT_ESCAPE_CHARACTER);
            String chromosomeType = tlSearchBean.getSelectedChromType();
            //write the title
            csvWriter.writeNext(new String[]{"TPB Traffic Light Summary Report"});
            //write empty line
            csvWriter.writeNext(new String[]{""});

            writeExportHeader(csvWriter,tlSearchBean);


            //write total number of genes
            csvWriter.writeNext(new String[]{"Total Genes:" + String.valueOf(tlSumReporter.getGeneNumber())});
            //write empty line
            csvWriter.writeNext(new String[]{""});

            //write table header
            csvWriter.writeNext(new String[]{"TPB Data Type", "4: green", "3: yellow", "2: red", "1: black"});
            //write empty line
            csvWriter.writeNext(new String[]{""});
            List<TLTypeSumReporter> reporterlist = tlSumReporter.getTlTypeSumReporters();
            for (TLTypeSumReporter tltsr : reporterlist) {
                csvWriter.writeNext(new String[]{tltsr.getTpbDataType(), String.valueOf(tltsr.getLevel4Num()), String.valueOf(tltsr.getLevel3Num()), String.valueOf(tltsr.getLevel2Num()), String.valueOf(tltsr.getLevel1Num())});
            }


            csvWriter.flush();
            return new ByteArrayInputStream(csvOutputStream.toByteArray());
        } catch (Exception ex) {
            throw new DMException(ex);
        } finally {
            if (csvWriter != null) {
                try {
                    csvWriter.close();
                } catch (Exception cex) {
                    //ignore whatever
                }
            }
        }
    }


    /**
     * Export the traffic light as a CSV file
     *
     * @return SUCCESS if there is no error
     */
    public String exportTL() {
        try {
            //get the traffic light filter conditions from the session
            TLSearchBean tlSearchBeanInSess = (TLSearchBean) this.findFromSession(SESS_FILTER_CONDS);
            if (tlSearchBeanInSess == null) {
                addActionError(getText("tl.filter.conditions.session.expired"));
                return ERROR;
            } else {
                tlSearchBean = tlSearchBeanInSess;
            }
            //preparing for traffic light condtions
            String selectedChromType = tlSearchBean.getSelectedChromType();
            ChromType chromType = ChromType.fromType(selectedChromType);
            int combinatedDBSToken = tlSearchBean.getCombinatedToken();
            //post-process the tpb versions
            processTPBVersions(combinatedDBSToken, selectedChromType);

            //added the traffic light order by the gene's start position.
            OrderCondition orderCon = SqlCondition.orderCon().add(OrderBy.asc("startPosition"));
            OrderBy[] orderBys = orderCon.orderBy();

            tlPagination = this.dmSystemService.getVersionTrafficLight(tlSearchBean, 1, -1, orderBys);

            this.csvInputStream = createTLCSVFile(tlSearchBean, tlPagination);
            String chromName = tlSearchBean.getSelectedChromType();
            String csvFileName = "tpb_traffic_light_chromosome_" + chromName + ".csv";
            this.contentDisposition = "attachment;filename=\"" + csvFileName + "\"";
            this.bufferSize = 10240;
            this.contentType = "application/octet-stream";

        } catch (Exception ex) {
            logger.error(ex);
            addActionError(getText("tl.failed.to.export.traffic.light.csv.file"));
            return ERROR;
        }
        return SUCCESS;
    }

    private InputStream createTLCSVFile(TLSearchBean tlSearchBean, Pagination<TrafficLight> tlPagination) {
        CSVWriter csvWriter = null;
        try {
            //get the traffic lights list
            List<TrafficLight> trafficLights = tlPagination.getPageResults();
            ByteArrayOutputStream csvOutputStream = new ByteArrayOutputStream();
            csvWriter = new CSVWriter(new OutputStreamWriter(csvOutputStream), '\t', CSVWriter.DEFAULT_ESCAPE_CHARACTER);
            String chromosomeType = tlSearchBean.getSelectedChromType();
            //write the title
            csvWriter.writeNext(new String[]{"TPB Traffic Lights"});
            csvWriter.writeNext(new String[]{""});


            writeExportHeader(csvWriter,tlSearchBean);

            //write total result size of gene
            csvWriter.writeNext(new String[]{"Total Genes:" + String.valueOf(trafficLights.size())});
            //write empty line
            csvWriter.writeNext(new String[]{""});

            //write total result size of gene
            csvWriter.writeNext(new String[]{"4: green 3: yellow 2: red 1: black"});
            //write empty line
            csvWriter.writeNext(new String[]{""});

            //write the gene symbol and accession
            csvWriter.writeNext(new String[]{"Gene Symbol", "ENSG Accession", DataType.PE.type(), DataType.PE_MS.type(), DataType.PE_MS_ANN.type(), DataType.PE_MS_PROB.type(), DataType.PE_MS_SAM.type(),
                    DataType.PE_ANTI.type(), DataType.PE_ANTI_ANN.type(), DataType.PE_ANTI_IHC.type(), DataType.PE_ANTI_IHC_NORM.type(), DataType.PE_OTH.type(), DataType.PE_OTH_CUR.type(),
                    DataType.PTM.type(), DataType.PTM_PHS.type(), DataType.PTM_PHS_SER.type(), DataType.PTM_PHS_THR.type(), DataType.PTM_PHS_TYR.type(),
                    DataType.PTM_ACE.type(), DataType.PTM_ACE_LYS.type(), DataType.PTM_ACE_NTA.type(),
                    DataType.TE.type(), DataType.TE_MA.type(), DataType.TE_MA_PROP.type(), DataType.TE_OTH.type(), DataType.TE_OTH_CUR.type()});

            //write all the gene accessions if any
            for (TrafficLight tl : trafficLights) {
                TLGene tlGene = tl.getTlGene();
                String geneSymbol = "";
                String ensgAc = "";
                if (tlGene != null) {
                    geneSymbol = tlGene.getDisplayName();
                    ensgAc = tlGene.getEnsgAccession();
                }
                String peCLV = String.valueOf(tl.getTlPEColor().getColorLevel());
                String peMsCLV = String.valueOf(tl.getTlPEMSColor().getColorLevel());
                String peMsAnnCLV = String.valueOf(tl.getTlPEMSANNColor().getColorLevel());
                String peMsProbCLV = String.valueOf(tl.getTlPEMSPROBColor().getColorLevel());
                String peMsSamCLV = String.valueOf(tl.getTlPEMSSAMColor().getColorLevel());
                String peAntiCLV = String.valueOf(tl.getTlPEANTIColor().getColorLevel());
                String peAntiAnnCLV = String.valueOf(tl.getTlPEANTIANNColor().getColorLevel());
                String peAntiIhcCLV = String.valueOf(tl.getTlPEANTIIHCColor().getColorLevel());
                String peAntiIhcNormCLV = String.valueOf(tl.getTlPEANTIIHCNORMColor().getColorLevel());
                String peOthCLV = String.valueOf(tl.getTlPEOTHColor().getColorLevel());
                String peOthCurCLV = String.valueOf(tl.getTlPEOTHCURColor().getColorLevel());
                String ptmCLV = String.valueOf(tl.getTlPTMColor().getColorLevel());
                String ptmPhsCLV = String.valueOf(tl.getTlPTMPHSColor().getColorLevel());
                String ptmPhsSerCLV = String.valueOf(tl.getTlPTMPHSSERColor().getColorLevel());
                String ptmPhsThrCLV = String.valueOf(tl.getTlPTMPHSTHRColor().getColorLevel());
                String ptmPhsTyrCLV = String.valueOf(tl.getTlPTMPHSTYRColor().getColorLevel());
                String ptmAceCLV = String.valueOf(tl.getTlPTMACEColor().getColorLevel());
                String ptmAceLysCLV = String.valueOf(tl.getTlPTMACELYSColor().getColorLevel());
                String ptmAceNtaCLV = String.valueOf(tl.getTlPTMACENTAColor().getColorLevel());
                String teCLV = String.valueOf(tl.getTlTEColor().getColorLevel());
                String teMaCLV = String.valueOf(tl.getTlTEMAColor().getColorLevel());
                String teMaProbCLV = String.valueOf(tl.getTlTEMAPROPColor().getColorLevel());
                String teOthCLV = String.valueOf(tl.getTlTEOTHColor().getColorLevel());
                String teOthCurCLV = String.valueOf(tl.getTlTEOTHCURColor().getColorLevel());
                csvWriter.writeNext(new String[]{geneSymbol, ensgAc, peCLV, peMsCLV, peMsAnnCLV, peMsProbCLV, peMsSamCLV, peAntiCLV, peAntiAnnCLV, peAntiIhcCLV, peAntiIhcNormCLV, peOthCLV,
                        peOthCurCLV, ptmCLV, ptmPhsCLV, ptmPhsSerCLV, ptmPhsThrCLV, ptmPhsTyrCLV, ptmAceCLV, ptmAceLysCLV, ptmAceNtaCLV, teCLV, teMaCLV, teMaProbCLV, teOthCLV, teOthCurCLV});
            }
            csvWriter.flush();
            return new ByteArrayInputStream(csvOutputStream.toByteArray());
        } catch (Exception ex) {
            throw new DMException(ex);
        } finally {
            if (csvWriter != null) {
                try {
                    csvWriter.close();
                } catch (Exception cex) {
                    //ignore whatever
                }
            }
        }
    }

    /**
     * check errors for the shortcut parameters
     *
     * @return true if there is an error
     */
    private boolean checkShortcutParamsErrors() {
        boolean hasError = false;
        if (StringUtils.isNotBlank(this.chm) && this.tt != 0) {
            ChromType selectedChm = ChromType.fromType(this.chm);
            if (selectedChm.equals(ChromType.UNKNOWN)) {
                addFieldError("chromosome", getText("tl.invalid.chm.type"));
                hasError = true;
            }
            if (!DMUtil.matchTLTokenPattern(this.tt)) {
                addFieldError("tt", getText("tl.invalid.combination.token.of.datasource"));
                hasError = true;
            }
            if (hasError) {
                return hasError;
            }

            tlSearchBean.setCombinatedToken(tt);
            tlSearchBean.setSelectedChromType(this.chm);
            if (this.vid != 0) {
                tlSearchBean.setSelectedVersion(this.vid);
            }
        }
        return hasError;
    }

    /**
     * check the request error
     *
     * @return true if there is an error
     */
    public boolean hasInputErrors() {
        //remove invalid type color level filters
        removeInvalidTypeEvLevelsFilters();
        //check any errors in advanced filter mode
        boolean hasError = false;
        boolean inAdvancedMode = tlSearchBean.isAdvancedMode();
        if (inAdvancedMode) {
            long regionFrom = tlSearchBean.getRegionFrom();
            long regionTo = tlSearchBean.getRegionTo();
            if (regionFrom != 0 && regionTo != 0 && regionFrom > regionTo) {
                addFieldError("regionFrom", getText("tl.advanced.mode.chrom.regionfrom.great.than.regionto.error"));
                hasError = true;
            }
            return hasError;
        } else {
            return false;
        }
    }

    /**
     * remove the invalid evidence level for a datatype
     */
    private void removeInvalidTypeEvLevelsFilters() {
        List<TLTypeEvLevelFilter> typeEvLevelFilters = tlSearchBean.getTlTypeEvLevelFilters();
        List<TLTypeEvLevelFilter> tmpTypeLevelFilters = new ArrayList<TLTypeEvLevelFilter>();
        if (typeEvLevelFilters != null) {
            for (TLTypeEvLevelFilter tFilter : typeEvLevelFilters) {
                if (tFilter.isValidTypeLevel()) {
                    tmpTypeLevelFilters.add(tFilter);
                }
            }
            tlSearchBean.setTlTypeEvLevelFilters(tmpTypeLevelFilters);
        }
    }

    /**
     * ajax tl evidence action - see the summary of the traffic light
     *
     * @return SUCCESS if there is no error.
     */
    public String tlEvSum() {
        tlEvidenceResponse = new TLEvidenceResponse();
        try {
            TLEvidenceSummary tlEvidenceSummary = null;
            DataType dataType = DataType.fromType(tpbDataType);
            if (dataType.isPE()) {
                tlEvidenceSummary = this.dmSystemService.getPETLSummary(tlGeneId, dataType);
            } else if (dataType.isPTM()) {
                tlEvidenceSummary = this.dmSystemService.getPTMTLSummary(tlGeneId, dataType);
            } else if (dataType.isTE()) {
                tlEvidenceSummary = this.dmSystemService.getTETLSummary(tlGeneId, dataType);
            } else {
                //nothing
            }

            if (tlEvidenceSummary != null) {
                tlEvidenceResponse.setTlEvidenceSummary(tlEvidenceSummary);
            }
            tlEvidenceResponse.setSuccess(SUCCESS_RESPONSE);
            tlEvidenceResponse.setMessage(getText("tl.get.the.traffic.light.summary.success.message"));
        } catch (Exception ex) {
            logger.error(ex);
            tlEvidenceResponse.setSuccess(FAILED_RESPONSE);
            tlEvidenceResponse.setMessage(getText("tl.failed.to.get.the.traffic.light.summary"));
        }
        return SUCCESS;

    }

    /**
     * ajax srcGeneSum action , get a gene evidence summaries based on a db source and a data type
     *
     * @return SUCCESS if retrieving a gene evidence summaries based on a db source and a data type
     */
    public String srcGeneSum() {
        tlEvidenceResponse = new TLEvidenceResponse();

        try {
            DataType dataType = DataType.fromType(tpbDataType);

            TLEvidenceSummary tlEvidenceSummary = null;
            if (dataType.isPE()) {
                tlEvidenceSummary = this.dmSystemService.getTLPESummaryBySrcGene(dbSource, geneId, dataType);
            } else if (dataType.isPTM()) {
                tlEvidenceSummary = this.dmSystemService.getTLPTMSummaryBySrcGene(dbSource, geneId, dataType);
            } else if (dataType.isTE()) {
                tlEvidenceSummary = this.dmSystemService.getTLTESummaryBySrcGene(dbSource, geneId, dataType);
            } else {
                //nothing
            }

            if (tlEvidenceSummary != null) {
                tlEvidenceResponse.setTlEvidenceSummary(tlEvidenceSummary);
            }

            tlEvidenceResponse.setSuccess(SUCCESS_RESPONSE);
            tlEvidenceResponse.setMessage(getText("tl.get.the.gene.evidence.summary.success.message"));
        } catch (Exception ex) {
            logger.error(ex);
            tlEvidenceResponse.setSuccess(FAILED_RESPONSE);
            tlEvidenceResponse.setMessage(getText("tl.failed.to.get.the.gene.evidence.summary"));
        }
        return SUCCESS;
    }

    /**
     * ajax viewevidences action. get all evidences for a particular gene based on a data type
     *
     * @return SUCCESS if there is no error.
     */
    public String viewEvidences() {
        tlEvidenceResponse = new TLEvidenceResponse();
        try {
            DataType dataType = DataType.fromType(tpbDataType);
            TLEvidenceSummary evidenceSummary = null;
            if (dataType.isPE()) {
                evidenceSummary = this.dmSystemService.getAllPEEvidencesByGeneAndType(geneId, dataType);
            } else if (dataType.isPTM()) {
                evidenceSummary = this.dmSystemService.getAllPTMEvidencesByGeneAndType(geneId, dataType);
            } else if (dataType.isTE()) {
                evidenceSummary = this.dmSystemService.getAllTEEvidencesByGeneAndType(geneId, dataType);
            } else {
                //nothing
            }

            if (evidenceSummary != null) {
                tlEvidenceResponse.setTlEvidenceSummary(evidenceSummary);
            }
            tlEvidenceResponse.setSuccess(SUCCESS_RESPONSE);
            tlEvidenceResponse.setMessage(getText("tl.get.the.gene.evidences.success.message"));
        } catch (Exception ex) {
            logger.error(ex);
            tlEvidenceResponse.setSuccess(FAILED_RESPONSE);
            tlEvidenceResponse.setMessage(getText("tl.failed.to.get.the.gene.evidences"));
        }
        return SUCCESS;
    }

    /**
     * ajax method - find tpb version
     *
     * @return SUCCESS if there is no error
     */
    public String findTPBVersions() {
        //get current datasource combination
        int combinatedDBSToken = tlSearchBean.getCombinatedToken();
        //populate the tpb version based on the current datasource combination
        processTPBVersions(combinatedDBSToken, tlSearchBean.getSelectedChromType());
        return SUCCESS;
    }

    /**
     * private method - process the tpb version based on a selected chromosome and a combination token
     *
     * @param token         a combination token
     * @param selectedChrom a selected chromosome
     */
    private void processTPBVersions(int token, String selectedChrom) {
        ChromType chromType = ChromType.fromType(selectedChrom);
        List<TPBVersion> allTpbVersions = this.dmSystemService.getAllTPBVersionByChromTypeTrackToken(chromType, token);
        tpbVersions = convertToVersionMap(allTpbVersions);
    }

    /**
     * private method - convert a list of TPBVersion into a Map
     *
     * @param tlVersions a list of TPBVersion objects
     * @return a HashMap which contains a list of TPBVersion objects
     */
    private HashMap<String, String> convertToVersionMap(List<TPBVersion> tlVersions) {
        HashMap<String, String> versions = new LinkedHashMap<String, String>();
        //put the head
        versions.put("-1", "- select a version -");
        for (TPBVersion tpbVersion : tlVersions) {
            long versionId = tpbVersion.getId();
            int versionNum = tpbVersion.getVersionNo();
            Date versionTime = tpbVersion.getCreatedTime();
            versions.put(String.valueOf(versionId), (versionNum + " - [ " + DMUtil.dateToDDMMYYYYStr(versionTime) + " ]"));
        }
        return versions;
    }

    /**
     * get a pagination of traffic light
     *
     * @return a pagination of traffic light
     */
    public Pagination<TrafficLight> getTlPagination() {
        return tlPagination;
    }

    /**
     * set a pagination of traffic light
     *
     * @param tlPagination a pagination of traffic light
     */
    public void setTlPagination(Pagination<TrafficLight> tlPagination) {
        this.tlPagination = tlPagination;
    }

    /**
     * get a traffic light gene id
     *
     * @return a traffic light gene id
     */
    public long getTlGeneId() {
        return tlGeneId;
    }

    /**
     * set a traffic light gene id
     *
     * @param tlGeneId a traffic light gene id
     */
    public void setTlGeneId(long tlGeneId) {
        this.tlGeneId = tlGeneId;
    }

    /**
     * get a gene id
     *
     * @return a gene id
     */
    public long getGeneId() {
        return geneId;
    }

    /**
     * set a gene id
     *
     * @param geneId a gene id
     */
    public void setGeneId(long geneId) {
        this.geneId = geneId;
    }

    /**
     * get a tpb data type
     *
     * @return a tpb data type
     */
    public String getTpbDataType() {
        return tpbDataType;
    }

    /**
     * set a tpb data type
     *
     * @param tpbDataType a tpb data type
     */
    public void setTpbDataType(String tpbDataType) {
        this.tpbDataType = tpbDataType;
    }

    /**
     * get a datasource
     *
     * @return a datasource
     */
    public String getDbSource() {
        return dbSource;
    }

    /**
     * set a datasource
     *
     * @param dbSource a datasource
     */
    public void setDbSource(String dbSource) {
        this.dbSource = dbSource;
    }

    /**
     * get a traffic light evidence json response
     *
     * @return a traffic light evidence json response
     */
    public TLEvidenceResponse getTlEvidenceResponse() {
        return tlEvidenceResponse;
    }

    /**
     * set a traffic light evidence json response
     *
     * @param tlEvidenceResponse a traffic light evidence json response
     */
    public void setTlEvidenceResponse(TLEvidenceResponse tlEvidenceResponse) {
        this.tlEvidenceResponse = tlEvidenceResponse;
    }

    /**
     * get a HashMap of tpb data types
     *
     * @return a HashMap of tpb data types
     */
    public HashMap<String, String> getTpbDataTypes() {
        return tpbDataTypes;
    }

    /**
     * set a HashMap of tpb data type
     *
     * @param tpbDataTypes a HashMap of tpb data type
     */
    public void setTpbDataTypes(HashMap<String, String> tpbDataTypes) {
        this.tpbDataTypes = tpbDataTypes;
    }

    /**
     * get a HashMap of gene value types (symbol or accession)
     *
     * @return a HashMap of gene value types (symbol or accession)
     */
    public HashMap<String, String> getGeneValueTypes() {
        return geneValueTypes;
    }

    /**
     * set a HashMap of gene value types (symbol or accession)
     *
     * @param geneValueTypes a HashMap of gene value types (symbol or accession)
     */
    public void setGeneValueTypes(HashMap<String, String> geneValueTypes) {
        this.geneValueTypes = geneValueTypes;
    }

    /**
     * get a shortcut parameter name for chromosome type
     *
     * @return a shortcut parameter name for chromosome type
     */
    public String getChm() {
        return chm;
    }

    /**
     * set a shortcut parameter name for chromosome type
     *
     * @param chm a shortcut parameter name for chromosome type
     */
    public void setChm(String chm) {
        this.chm = chm;
    }

    /**
     * get a shortcut parameter name for datasource combination token
     *
     * @return a shortcut parameter name for datasource combination token
     */
    public int getTt() {
        return tt;
    }

    /**
     * set a shortcut parameter name for datasource combination token
     *
     * @param tt a shortcut parameter name for datasource combination token
     */
    public void setTt(int tt) {
        this.tt = tt;
    }

    /**
     * get a shortcut parameter name for traffic light version
     *
     * @return a shortcut parameter name for traffic light version
     */
    public long getVid() {
        return vid;
    }

    /**
     * set a shortcut parameter name for traffic light version
     *
     * @param vid a shortcut parameter name for traffic light version
     */
    public void setVid(long vid) {
        this.vid = vid;
    }

    /**
     * get a traffic light summary reporter json response
     *
     * @return a traffic light summary reporter json response
     */
    public TLSumReporter getTlSumReporter() {
        return tlSumReporter;
    }

    /**
     * set a traffic light summary reporter json response
     *
     * @param tlSumReporter a traffic light summary reporter json response
     */
    public void setTlSumReporter(TLSumReporter tlSumReporter) {
        this.tlSumReporter = tlSumReporter;
    }

    /**
     * get a content type of a csv file
     *
     * @return a content type of a csv file
     */
    public String getContentType() {
        return contentType;
    }

    /**
     * set a content type of a csv file
     *
     * @param contentType a content type of a csv file
     */
    public void setContentType(String contentType) {
        this.contentType = contentType;
    }

    /**
     * get a csv file InputStream
     *
     * @return a csv file InputStream
     */
    public InputStream getCsvInputStream() {
        return csvInputStream;
    }

    /**
     * set a csv file InputStream
     *
     * @param csvInputStream a csv file InputStream
     */
    public void setCsvInputStream(InputStream csvInputStream) {
        this.csvInputStream = csvInputStream;
    }

    /**
     * get a csv file content disposition
     *
     * @return a csv file content disposition
     */
    public String getContentDisposition() {
        return contentDisposition;
    }

    /**
     * set a csv file content disposition
     *
     * @param contentDisposition a csv file content disposition
     */
    public void setContentDisposition(String contentDisposition) {
        this.contentDisposition = contentDisposition;
    }

    /**
     * get a csv file buffer size
     *
     * @return a csv file buffer size
     */
    public int getBufferSize() {
        return bufferSize;
    }

    /**
     * set a csv file buffer size
     *
     * @param bufferSize a csv file buffer size
     */
    public void setBufferSize(int bufferSize) {
        this.bufferSize = bufferSize;
    }
}
