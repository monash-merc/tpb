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

package edu.monash.merc.system.scheduling.impl;

import edu.monash.merc.common.name.ChromType;
import edu.monash.merc.common.name.DbAcType;
import edu.monash.merc.common.name.GPMDbType;
import edu.monash.merc.domain.DSVersion;
import edu.monash.merc.domain.TPBGene;
import edu.monash.merc.dto.CsvProbeTissueEntryBean;
import edu.monash.merc.dto.NXEntryBean;
import edu.monash.merc.dto.ProbeGeneBean;
import edu.monash.merc.dto.barcode.BarcodeDataBean;
import edu.monash.merc.dto.gpm.GPMDbBean;
import edu.monash.merc.dto.gpm.GPMEntryBean;
import edu.monash.merc.dto.hpa.HPAEntryBean;
import edu.monash.merc.exception.DMConfigException;
import edu.monash.merc.service.DMSystemService;
import edu.monash.merc.system.config.SystemPropConts;
import edu.monash.merc.system.config.SystemPropSettings;
import edu.monash.merc.system.parser.GPMDbParser;
import edu.monash.merc.system.parser.TEMaParser;
import edu.monash.merc.system.parser.gpm.GPMRSSReader;
import edu.monash.merc.system.parser.gpm.GPMSyndEntry;
import edu.monash.merc.system.parser.nextprot.NxXMLSAXParser;
import edu.monash.merc.system.parser.xml.GPMWSXmlParser;
import edu.monash.merc.system.parser.xml.HPAWSXmlParser;
import edu.monash.merc.system.parser.xml.WSXmlInputFactory;
import edu.monash.merc.system.remote.FTPFileGetter;
import edu.monash.merc.system.remote.HttpBarcodeFileGetter;
import edu.monash.merc.system.remote.HttpHpaFileGetter;
import edu.monash.merc.system.scheduling.TPBProcessor;
import edu.monash.merc.system.version.DSVCombination;
import edu.monash.merc.system.version.TLVersionTrack;
import edu.monash.merc.util.DMUtil;
import edu.monash.merc.util.file.DMFileGZipper;
import edu.monash.merc.wsclient.biomart.BioMartClient;
import edu.monash.merc.wsclient.biomart.GeneConsts;
import org.apache.commons.lang.StringUtils;
import org.apache.commons.net.ftp.FTPFile;
import org.apache.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.io.Resource;
import org.springframework.core.io.ResourceLoader;
import org.springframework.stereotype.Service;

import javax.annotation.PostConstruct;
import java.io.File;
import java.io.FileInputStream;
import java.io.InputStream;
import java.util.*;

/**
 * @author Simon Yu
 * @version 1.0
 * @email Xiaoming.Yu@monash.edu
 * <p/>
 * Date: 28/02/12
 * Time: 11:38 AM
 */

/**
 * TpbDataProcessor class implements the TPBProcessor interface, importing the data from the different data sources
 *
 * @author Simon Yu
 *         <p/>
 *         Email: xiaoming.yu@monash.edu
 * @version 1.0
 * @since 1.0
 *        <p/>
 *        Date: 27/11/12 4:04 PM
 */
@Service
public class TpbDataProcessor implements TPBProcessor {

    private static String NEXTPROT_CHROME_NAME = "chromosome";

    private String downloadLocation;

    private String chromosomeTypes;

    private boolean dbEnsemblImportEnabled = true;

    private boolean probeImportEnabled = true;

    private String probePlatforms;

    private boolean dsNxImportEnabled = true;

    private boolean dsGpmImportEnabled = true;

    private boolean dsHpaImportEnabled = true;

    private boolean dsGPMpSytImportEnabled = true;

    private boolean dsGPMLysImportEnabled = true;

    private boolean dsGPMNtaImportEnabled = true;

    private boolean dsBarcodeImportEnabled = true;

    private ResourceLoader resourceLoader;

    @Autowired
    private SystemPropSettings systemPropSettings;

    @Autowired
    private DMSystemService dmSystemService;

    private Logger logger = Logger.getLogger(this.getClass().getName());

    public void setSystemPropSettings(SystemPropSettings systemPropSettings) {
        this.systemPropSettings = systemPropSettings;
    }

    public void setDmSystemService(DMSystemService dmSystemService) {
        this.dmSystemService = dmSystemService;
    }

    public ResourceLoader getResourceLoader() {
        return resourceLoader;
    }

    public void setResourceLoader(ResourceLoader resourceLoader) {
        this.resourceLoader = resourceLoader;
    }

    @PostConstruct
    public void tpbInit() {
        this.chromosomeTypes = this.systemPropSettings.getPropValue(SystemPropConts.CHROMOSOME_IMPORT_TYPE);

        String ensemblEnabled = this.systemPropSettings.getPropValue(SystemPropConts.DATASOURCE_ENSEMBL_IMPORT_ENABLE);
        String dsNxEnabled = this.systemPropSettings.getPropValue(SystemPropConts.DATASOURCE_NEXTPORT_IMPORT_ENABLE);
        String dsGpmEnabled = this.systemPropSettings.getPropValue(SystemPropConts.DATASOURCE_GPM_IMPORT_ENABLE);
        String dsHpaEnabled = this.systemPropSettings.getPropValue(SystemPropConts.DATASOURCE_HPA_IMPORT_ENABLE);

        String dsGpmPsytEnabled = this.systemPropSettings.getPropValue(SystemPropConts.DATASOURCE_GPM_PSYT_IMPORT_ENABLE);
        String dsGpmLysEnabled = this.systemPropSettings.getPropValue(SystemPropConts.DATASOURCE_GPM_LYS_IMPORT_ENABLE);
        String dsGpmNtaEnabled = this.systemPropSettings.getPropValue(SystemPropConts.DATASOURCE_GPM_NTA_IMPORT_ENABLE);

        String probeEnabled = this.systemPropSettings.getPropValue(SystemPropConts.DATASOURCE_PROBE_IMPORT_ENABLE);
        this.probePlatforms = this.systemPropSettings.getPropValue(SystemPropConts.MICROARRAY_PROBE_PLATFORMS);
        String barcodeEnabled = this.systemPropSettings.getPropValue(SystemPropConts.DATASOURCE_BARCODE_IMPORT_ENABLE);

        this.dbEnsemblImportEnabled = Boolean.valueOf(ensemblEnabled);
        this.dsNxImportEnabled = Boolean.valueOf(dsNxEnabled).booleanValue();
        this.dsGpmImportEnabled = Boolean.valueOf(dsGpmEnabled).booleanValue();
        this.dsHpaImportEnabled = Boolean.valueOf(dsHpaEnabled).booleanValue();

        this.dsGPMpSytImportEnabled = Boolean.valueOf(dsGpmPsytEnabled);
        this.dsGPMLysImportEnabled = Boolean.valueOf(dsGpmLysEnabled);
        this.dsGPMNtaImportEnabled = Boolean.valueOf(dsGpmNtaEnabled);

        this.probeImportEnabled = Boolean.valueOf(probeEnabled).booleanValue();
        this.dsBarcodeImportEnabled = Boolean.valueOf(barcodeEnabled).booleanValue();

        this.downloadLocation = DMUtil.normalizePath(this.systemPropSettings.getPropValue(SystemPropConts.TPB_DB_DOWNLOAD_LOCATION));

//        try {
//            Resource dbDownloadResource = this.resourceLoader.getResource("classpath:dbDownload");
//            this.downloadLocation = DMUtil.normalizePath(dbDownloadResource.getURL().getPath());
//        } catch (Exception ex) {
//            throw new DMConfigException("Failed to get the download location, " + ex.getMessage());
//        }
    }

    public void process() {

        Date importedTime = GregorianCalendar.getInstance().getTime();
        //The Ensembl Genes must be imported first
        //import Genes for HUMAN from Ensembl Database (include all chromosome types)
        if (dbEnsemblImportEnabled) {
            importEnsemblGenes(GeneConsts.HUMAN, null);
            //try to pause 1 minute after ensembl genes completed
            rest(6000);
        }

        //import the probes, it must be after the ensemble importing finished
        if (probeImportEnabled) {
            List<String> requiredPlatForms = getProbePlatforms(this.probePlatforms);
            for (String platform : requiredPlatForms) {
                importGeneProbes(GeneConsts.HUMAN, platform);
            }
            //try to pause 1 minute after probe completed
            rest(6000);
        }

        //set the required chromosome types as all if required chromosome types is empty in the configuration file
        List<ChromType> requiredChromTypes = getChmTypes(chromosomeTypes);
        if (requiredChromTypes.isEmpty()) {
            requiredChromTypes = ChromType.allChroms();
        }

        for (ChromType chromType : requiredChromTypes) {
            logger.info("The chromosome - " + chromType.chm() + " will be imported");
        }

        if (dsHpaImportEnabled) {
            logger.info("TPB is starting to import the hpa data.");
            long startTime = System.currentTimeMillis();
            processHPAData(requiredChromTypes, importedTime);
            long endTime = System.currentTimeMillis();
            logger.info("The total process time of the hpa: " + (endTime - startTime) / 1000 + "seconds.");
            //try to pause 1 minute after NextProt datasource completed
            rest(6000);
        }

        //start to process the gpm data
        if (dsGpmImportEnabled) {
            logger.info("TPB is starting to import the gpm data.");
            long startTime = System.currentTimeMillis();
            processGPMData(requiredChromTypes, importedTime);
            long endTime = System.currentTimeMillis();
            logger.info("The total process time of the gpm: " + (endTime - startTime) / 1000 + "seconds.");
            //try to pause 1 minute after gpm datasource completed
            rest(6000);
        }

        //proteome central - GPM PSYT
        if (dsGPMpSytImportEnabled) {
            logger.info("TPB is starting to import the proteomecentral data - GPM pSYT.");
            long startTime = System.currentTimeMillis();
            processProteomeCentralGPMDb(requiredChromTypes, importedTime, GPMDbType.GPMDB_PSYT);
            long endTime = System.currentTimeMillis();
            logger.info("The total process time of the GPM pSYT: " + (endTime - startTime) / 1000 + "seconds.");
            //try to pause 1 minute after gpm datasource completed
            rest(6000);
        }

        //proteome central - GPM LYS
        if (dsGPMLysImportEnabled) {
            logger.info("TPB is starting to import the proteomecentral data - GPM LYS.");
            long startTime = System.currentTimeMillis();
            processProteomeCentralGPMDb(requiredChromTypes, importedTime, GPMDbType.GPMDB_LYS);
            long endTime = System.currentTimeMillis();
            logger.info("The total process time of the GPM LYS: " + (endTime - startTime) / 1000 + "seconds.");
            //try to pause 1 minute after gpm datasource completed
            rest(6000);
        }

        //proteome central - GPM NTA
        if (dsGPMNtaImportEnabled) {
            logger.info("TPB is starting to import the proteomecentral data - GPM NTA.");
            long startTime = System.currentTimeMillis();
            processProteomeCentralGPMDb(requiredChromTypes, importedTime, GPMDbType.GPMDB_NTA);
            long endTime = System.currentTimeMillis();
            logger.info("The total process time of the GPM NTA: " + (endTime - startTime) / 1000 + "seconds.");
            //try to pause 1 minute after gpm datasource completed
            rest(6000);
        }

        //start to process the nextprot data
        for (ChromType chromType : requiredChromTypes) {
            if (dsNxImportEnabled) {
                logger.info("TPB is starting to import the chromosome " + chromType.chm() + " data from the nextprot.");
                long startTime = System.currentTimeMillis();
                processNextProtData(chromType, importedTime);
                long endTime = System.currentTimeMillis();
                logger.info("The total process time of the nextprot: " + (endTime - startTime) / 1000 + "seconds.");
                //try to pause 1 minute between two chromosome files
                rest(6000);
            }
        }

        //barcode data
        if (dsBarcodeImportEnabled) {
            logger.info("TPB is starting to import the barcode data.");
            long startTime = System.currentTimeMillis();
            processBarcodeData(requiredChromTypes, importedTime);
            long endTime = System.currentTimeMillis();
            logger.info("The total process time of the barcode: " + (endTime - startTime) / 1000 + "seconds.");
            //try to pause 1 minute after gpm datasource completed
            rest(6000);
        }

        //start to generate the traffic Lights
        for (ChromType chromType : requiredChromTypes) {
            createTLByChromosome(chromType, importedTime);
            //try to pause 60 seconds between two chromosome type
            rest(6000);
        }
        logger.info("The traffic lights creation process has been completed.");
    }

    //import ensembl  genes
    private void importEnsemblGenes(String species, String chromosome) {
        BioMartClient client = new BioMartClient();
        try {
            logger.info("TPB is starting to import the Ensembl genes.");
            String wsURL = this.systemPropSettings.getPropValue(SystemPropConts.BIOMART_RESTFUL_WS_URL);
            client.configChrom(wsURL, species, chromosome);
            List<TPBGene> tpbGeneList = client.importGenes();
            logger.info("The total size of Ensembl genes: " + tpbGeneList.size());
            this.dmSystemService.importTPBGenes(tpbGeneList);
            logger.info("Successfully imported the ensembl genes into database.");
        } catch (Exception ex) {
            logger.error(ex);
        } finally {
            try {
                client.release();
            } catch (Exception cex) {
                //ignore whatever
            }
        }
    }

    //import gene probes

    private void importGeneProbes(String species, String platform) {
        BioMartClient client = new BioMartClient();
        try {
            logger.info("TPB is starting to import the probes for platform - " + platform);
            String wsURL = this.systemPropSettings.getPropValue(SystemPropConts.BIOMART_RESTFUL_WS_URL);
            client.configProbe(wsURL, species);
            List<ProbeGeneBean> probeGeneBeans = client.importProbes(platform);
            logger.info("The total size of the gene probes: " + probeGeneBeans.size());
            this.dmSystemService.importProbes(probeGeneBeans);
            logger.info("Successfully imported the gene probes into database.");
        } catch (Exception ex) {
            logger.error(ex);
        } finally {
            try {
                client.release();
            } catch (Exception cex) {
                //ignore whatever
            }
        }
    }

    //process the nextprot data
    private void processNextProtData(ChromType chromType, Date importedTime) {
        FTPFileGetter ftpFileGetter = new FTPFileGetter();
        String ftpHost = systemPropSettings.getPropValue(SystemPropConts.FTP_NX_SERVER_NAME);
        String ftpUserName = systemPropSettings.getPropValue(SystemPropConts.FTP_NX_USER_NAME);
        String ftpPassword = systemPropSettings.getPropValue(SystemPropConts.FTP_NX_USER_PASSWORD);
        String workingDir = systemPropSettings.getPropValue(SystemPropConts.FTP_NX_CHROMOSOME_REL_DIR);
        try {
            if (ftpFileGetter.connectAndLogin(ftpHost, ftpUserName, ftpPassword)) {
                ftpFileGetter.changeToWorkingDirectory(workingDir);
                ftpFileGetter.setPassiveMode(true);
                //get all chromosome file
                Vector<String> chromosomeFiles = ftpFileGetter.listFileNames();

                //set the binary download mode
                ftpFileGetter.binary();
                InputStream chromInputStream = null;
                for (String file : chromosomeFiles) {
                    if (StringUtils.contains(file, NEXTPROT_CHROME_NAME + "_" + chromType.chm() + ".xml")) {
                        String fileLastModifiedTime = ftpFileGetter.getLastModifiedTime(file);
                        logger.info("The nextprot xml file: " + file + ", last modified time: " + fileLastModifiedTime);
                        //check if the file hasn't been updated, then get the file

                        if (!checkUpToDate(DbAcType.NextProt, chromType, file, fileLastModifiedTime)) {
                            chromInputStream = ftpFileGetter.downloadFileStream(file);
                            DMFileGZipper gZipper = new DMFileGZipper();
                            String outFileName = StringUtils.substringBefore(file, ".gz");
                            String storeDir = this.downloadLocation + File.separator;
                            gZipper.unzipFile(chromInputStream, storeDir + outFileName);

                            //call completePendingCommand to to finish command
                            if (!ftpFileGetter.completePendingCommand()) {
                                ftpFileGetter.logout();
                                ftpFileGetter.disconnect();
                            }
                            FileInputStream fileInputStream = new FileInputStream(new File(storeDir + outFileName));
                            processNextProtXML(chromType, fileInputStream, importedTime, file, fileLastModifiedTime);
                        } else {
                            logger.info("The nextprot xml file - " + file + " is already imported.");
                        }
                    }
                }
            } else {
                logger.error("Failed to login the ftp server - " + ftpHost);
            }
        } catch (Exception ex) {
            logger.error(ex);
        } finally {
            try {
                ftpFileGetter.logout();
                ftpFileGetter.disconnect();
            } catch (Exception ftpEx) {
                //ignore whatever caught here
            }
        }
    }

    private boolean checkUpToDate(DbAcType dbAcType, ChromType chromType, String fileName, String timetoken) {
        return this.dmSystemService.checkUpToDate(dbAcType, chromType, fileName, timetoken);
    }

    private void processNextProtXML(ChromType chromType, InputStream fileInputStream, Date importedTime, String fileName, String timeToken) {
        NxXMLSAXParser parser = new NxXMLSAXParser();
        List<NXEntryBean> nxEntryBeans = parser.parseNextProtXML(fileInputStream);
        logger.info("The total size of the nextprot data: " + nxEntryBeans.size());
        this.dmSystemService.saveNextProtDataEntryByChromosome(chromType, nxEntryBeans, importedTime, fileName, timeToken);
    }

    public void processGPMData(List<ChromType> chromTypes, Date importedTime) {
        FTPFileGetter ftpFileGetter = new FTPFileGetter();
        GPMRSSReader gpmrssReader = new GPMRSSReader();
        String url = systemPropSettings.getPropValue(SystemPropConts.GPM_RSS_FEED_URL);
        try {
            String gpmFtpServer = null;
            String gpmFtpUserName = "anonymous";
            String gpmFtpPassword = null;
            String workingDir = null;
            String gpm2tpbReleasedFile = null;
            GPMSyndEntry gpmSyndEntry = gpmrssReader.readRSS(url);
            if (gpmSyndEntry != null) {
                gpmFtpServer = gpmSyndEntry.getGmpFtpServer();
                workingDir = gpmSyndEntry.getTpbWorkDir();
                gpm2tpbReleasedFile = gpmSyndEntry.getReleasedTpbFileName();
            }
            if (ftpFileGetter.connectAndLogin(gpmFtpServer, gpmFtpUserName, gpmFtpPassword)) {
                ftpFileGetter.changeToWorkingDirectory(workingDir);
                ftpFileGetter.setPassiveMode(true);
                //set the binary download mode
                ftpFileGetter.binary();
                FTPFile[] ftpFiles = ftpFileGetter.listFiles(gpm2tpbReleasedFile);
                //only file exists
                if (ftpFiles != null && ftpFiles.length == 1) {
                    String fileLastModifiedTime = ftpFileGetter.getLastModifiedTime(gpm2tpbReleasedFile);
                    logger.info("The gpm xml file: " + gpm2tpbReleasedFile + ", last modified time: " + fileLastModifiedTime);
                    if (!checkUpToDate(DbAcType.GPM, null, gpm2tpbReleasedFile, fileLastModifiedTime)) {
                        InputStream gpmInputStream = ftpFileGetter.downloadFileStream(gpm2tpbReleasedFile);
                        DMFileGZipper gZipper = new DMFileGZipper();
                        String outFileName = StringUtils.substringBefore(gpm2tpbReleasedFile, ".gz");
                        String storeDir = this.downloadLocation + File.separator;
                        gZipper.unzipFile(gpmInputStream, storeDir + outFileName);

                        //call completePendingCommand to to finish command
                        if (!ftpFileGetter.completePendingCommand()) {
                            ftpFileGetter.logout();
                            ftpFileGetter.disconnect();
                        }
                        processGPMXML(chromTypes, (storeDir + outFileName), gpm2tpbReleasedFile, importedTime, fileLastModifiedTime);
                    } else {
                        logger.info("The gpm xml file - " + gpm2tpbReleasedFile + " is already imported.");
                    }
                } else {
                    logger.warn("The gpm xml file: " + gpm2tpbReleasedFile + " doesn't exist, ignore the data importing for the gpm.");
                }
            }
        } catch (Exception ex) {
            logger.error(ex);
        } finally {
            try {
                gpmrssReader.release();
                ftpFileGetter.logout();
                ftpFileGetter.disconnect();
            } catch (Exception ftpEx) {
                //ignore whatever caught here
            }
        }
    }

    private void processGPMXML(List<ChromType> chromTypes, String gpmXmlFile, String gmpRemoteFileName, Date importedTime, String timeToken) {
        GPMWSXmlParser xmlParser = new GPMWSXmlParser();
        List<GPMEntryBean> gpmEntryBeans = xmlParser.parseGPMXml(gpmXmlFile, WSXmlInputFactory.getInputFactoryConfiguredForXmlConformance());
        logger.info("The total size of the gpm data: " + gpmEntryBeans.size());
        this.dmSystemService.saveGPMDataEntry(chromTypes, gpmEntryBeans, importedTime, gmpRemoteFileName, timeToken);
    }

    //process the proteome central data  - GPM SPYT
    private void processProteomeCentralGPMDb(List<ChromType> chromTypes, Date importedTime, GPMDbType gpmDbType) {

        FTPFileGetter ftpFileGetter = new FTPFileGetter();
        String ftpHost = systemPropSettings.getPropValue(SystemPropConts.PROTEOMECENTRAL_FTP_SERVER_NAME);
        String ftpUserName = systemPropSettings.getPropValue(SystemPropConts.PROTEOMECENTRAL_FTP_USER_NAME);
        String ftpPassword = systemPropSettings.getPropValue(SystemPropConts.PROTEOMECENTRAL_FTP_USER_PASSWORD);
        String workingDir = null;
        String fileName = null;

        if (gpmDbType.equals(GPMDbType.GPMDB_PSYT)) {
            workingDir = systemPropSettings.getPropValue(SystemPropConts.GPM_PSYT_FTP_WORK_DIR);
            fileName = systemPropSettings.getPropValue(SystemPropConts.GPM_PSYT_FILE_NAME);
        } else if (gpmDbType.equals(GPMDbType.GPMDB_LYS)) {
            workingDir = systemPropSettings.getPropValue(SystemPropConts.GPM_LYS_FTP_WORK_DIR);
            fileName = systemPropSettings.getPropValue(SystemPropConts.GPM_LYS_FILE_NAME);
        } else if (gpmDbType.equals(GPMDbType.GPMDB_NTA)) {
            workingDir = systemPropSettings.getPropValue(SystemPropConts.GPM_NTA_FTP_WORK_DIR);
            fileName = systemPropSettings.getPropValue(SystemPropConts.GPM_NTA_FILE_NAME);
        }

        if (StringUtils.isBlank(fileName) || StringUtils.isBlank(workingDir)) {
            return;
        }

        try {
            if (ftpFileGetter.connectAndLogin(ftpHost, ftpUserName, ftpPassword)) {
                ftpFileGetter.changeToWorkingDirectory(workingDir);
                ftpFileGetter.setBufferSize(10240);
                ftpFileGetter.setPassiveMode(true);

                //set the binary download mode
                ftpFileGetter.binary();

                String destFileName = this.downloadLocation + File.separator + gpmDbType.type() + "_" + fileName;
                boolean completed = ftpFileGetter.downloadFile(fileName, destFileName);
                FileInputStream fileInputStream = new FileInputStream(new File(destFileName));
                //start to process data
                processProteomeCentralGPMData(chromTypes, fileInputStream, fileName, importedTime, gpmDbType);

            } else {
                logger.error("Failed to login the  proteomecentral ftp server - " + ftpHost);
            }
        } catch (Exception ex) {
            logger.error(ex);
        } finally {
            try {
                ftpFileGetter.logout();
                ftpFileGetter.disconnect();
            } catch (Exception ftpEx) {
                //ignore whatever caught here
            }
        }
    }

    //process the protoeme central data - GPM pSYT, LYS and NTA
    private void processProteomeCentralGPMData(List<ChromType> chromTypes, InputStream inputStream, String fileName, Date importedTime, GPMDbType gpmDbType) {
        GPMDbParser parser = new GPMDbParser();
        GPMDbBean gpmDbBean = parser.parse(inputStream, "utf-8", gpmDbType);
        this.dmSystemService.saveProteomeCentralData(chromTypes, gpmDbBean, fileName, importedTime);
    }

    //process the hpa data
    public void processHPAData(List<ChromType> chromTypes, Date importedTime) {

        HttpHpaFileGetter fileGetter = new HttpHpaFileGetter();
        String remoteHpaFile = this.systemPropSettings.getPropValue(SystemPropConts.HPA_DATA_RELEASE_LOCATION);
        String destFile = this.downloadLocation + File.separator + "proteinatlas.xml";
        try {
            //start to import hpa xml file
           // fileGetter.importHPAXML(remoteHpaFile, destFile);
            fileGetter.importHPAXMLBZ2(remoteHpaFile, destFile);
            logger.info("TPB finished to download the hpa file: " + destFile);
            HPAWSXmlParser parser = new HPAWSXmlParser();

            List<HPAEntryBean> hpaEntryBeans = parser.parseHPAXml(destFile, WSXmlInputFactory.getInputFactoryConfiguredForXmlConformance());

            if ((hpaEntryBeans != null) && (hpaEntryBeans.size() > 0)) {
                String hpaVersion = hpaEntryBeans.get(0).getHpaVersion();
                logger.info("The total size of the  hpa data: " + hpaEntryBeans.size() + " with a version : " + hpaVersion);
                if (!checkUpToDate(DbAcType.HPA, null, remoteHpaFile, hpaVersion)) {
                    this.dmSystemService.saveHPADataEntry(chromTypes, hpaEntryBeans, importedTime, remoteHpaFile, hpaVersion);
                } else {
                    logger.info("The hpa xml file - " + destFile + " is already imported.");
                }
            }
        } catch (Exception ex) {
            logger.error(ex);
        }
    }

    public void processBarcodeData(List<ChromType> chromTypes, Date importedTime) {

        String barcodeReleasedLocation = this.systemPropSettings.getPropValue(SystemPropConts.BARCODE_DATA_RELEASE_LOCATION);
        String barcodeHug133aFile = this.systemPropSettings.getPropValue(SystemPropConts.BARCODE_HGU133A_DATA_FILE);
        String barcodeHug133plus2File = this.systemPropSettings.getPropValue(SystemPropConts.BARCODE_HGU133PLUS2_DATA_FILE);
        String barcodePath = DMUtil.normalizePath(barcodeReleasedLocation);

        try {
            //import hgu133a file
            if (StringUtils.isNotBlank(barcodeHug133aFile)) {
                String remoteHgu133aFile = barcodePath + "/" + barcodeHug133aFile;
                String destHgu133aFile = this.downloadLocation + File.separator + barcodeHug133aFile;

                HttpBarcodeFileGetter barcodeHgu133aFileGetter = new HttpBarcodeFileGetter();
                String hgu133aTimeToken = barcodeHgu133aFileGetter.downloadBarcodeFile(remoteHgu133aFile, destHgu133aFile);
                if (!checkUpToDate(DbAcType.BarcodeHgu133a, null, barcodeHug133aFile, hgu133aTimeToken)) {
                    processSingleBarcodeFile(chromTypes, barcodeHug133aFile, DbAcType.BarcodeHgu133a.type(), importedTime, hgu133aTimeToken);
                }
            }

            //import hgu133plus2 file
            if (StringUtils.isNotBlank(barcodeHug133plus2File)) {
                String remoteHgu133plus2File = barcodePath + "/" + barcodeHug133plus2File;
                String destHgu133plus2File = this.downloadLocation + File.separator + barcodeHug133plus2File;

                HttpBarcodeFileGetter barcodeHgu133p2FileGetter = new HttpBarcodeFileGetter();
                String hgu133p2TimeToken = barcodeHgu133p2FileGetter.downloadBarcodeFile(remoteHgu133plus2File, destHgu133plus2File);
                if (!checkUpToDate(DbAcType.BarcodeHgu133plus2, null, barcodeHug133plus2File, hgu133p2TimeToken)) {
                    processSingleBarcodeFile(chromTypes, barcodeHug133plus2File, DbAcType.BarcodeHgu133plus2.type(), importedTime, hgu133p2TimeToken);
                }
            }

        } catch (Exception ex) {
            logger.error(ex);
        }
    }

    private void processSingleBarcodeFile(List<ChromType> chromTypes, String barcodeFileName, String barcodeType, Date importedTime, String timeToken) throws Exception {
        String destFile = this.downloadLocation + File.separator + barcodeFileName;
        FileInputStream fins = new FileInputStream(destFile);
        TEMaParser teMaParser = new TEMaParser();
        List<CsvProbeTissueEntryBean> probeTissueEntryBeans = teMaParser.parse(fins);
        BarcodeDataBean barcodeDataBean = new BarcodeDataBean();
        barcodeDataBean.setBarcodeType(barcodeType);
        barcodeDataBean.add(probeTissueEntryBeans);
        logger.info("TPB finished to parse the barcode file: " + barcodeFileName);
        logger.info("Total entry size of " + destFile + " is " + probeTissueEntryBeans.size());
        this.dmSystemService.saveTEMAEntry(chromTypes, barcodeDataBean, barcodeFileName, importedTime, timeToken);
        logger.info("TPB finished to save the barcode data for " + barcodeFileName);
    }

    //create the traffic light
    public void createTLByChromosome(ChromType chromType, Date importedTime) {
        DSVersion nxDsVersion = this.dmSystemService.getCurrentDSVersionByChromDbs(DbAcType.NextProt, chromType);
        DSVersion gpmDsVersion = this.dmSystemService.getCurrentDSVersionByChromDbs(DbAcType.GPM, chromType);
        DSVersion gpmPstyDsVersion = this.dmSystemService.getCurrentDSVersionByChromDbs(DbAcType.GPMPSYT, chromType);
        DSVersion gpmLysDsVersion = this.dmSystemService.getCurrentDSVersionByChromDbs(DbAcType.GPMLYS, chromType);
        DSVersion gpmNtaDsVersion = this.dmSystemService.getCurrentDSVersionByChromDbs(DbAcType.GPMNTA, chromType);
        DSVersion hpaDsVersion = this.dmSystemService.getCurrentDSVersionByChromDbs(DbAcType.HPA, chromType);
        DSVersion bcHgu133aDsVersion = this.dmSystemService.getCurrentDSVersionByChromDbs(DbAcType.BarcodeHgu133a, chromType);
        DSVersion bcHgu133p2DsVersion = this.dmSystemService.getCurrentDSVersionByChromDbs(DbAcType.BarcodeHgu133plus2, chromType);

        List<TLVersionTrack> tlVersionTracks = DSVCombination.createTLVersionTracks(nxDsVersion, gpmDsVersion, gpmPstyDsVersion, gpmLysDsVersion, gpmNtaDsVersion, hpaDsVersion, bcHgu133aDsVersion, bcHgu133p2DsVersion);
        if (tlVersionTracks != null) {
            logger.info("Starting to create a total of " + +tlVersionTracks.size() + "  traffic lights for the " + chromType.chm() + " chromosome.");
            for (TLVersionTrack tlvTrack : tlVersionTracks) {
                try {
                    logger.info("Starting to create a traffic lights for the" + chromType.chm() + " chromosome, track token: " + tlvTrack.getTrackToken());
                    boolean succeed = this.dmSystemService.createVersionTLByChrom(chromType, tlvTrack, importedTime);
                    if (succeed) {
                        logger.info("Finished to create the traffic lights for the" + chromType.chm() + " chromosome, track token: " + tlvTrack.getTrackToken());
                    }
                } catch (Exception ex) {
                    logger.error(ex);
                }
            }
        }
    }


    private List<ChromType> getChmTypes(String chromTypes) {
        if (StringUtils.isBlank(chromTypes)) {
            return ChromType.allChroms();
        }
        if (StringUtils.equalsIgnoreCase(chromTypes, "all")) {
            return ChromType.allChroms();
        }

        List<ChromType> requiredChmTypes = new ArrayList<ChromType>();
        String[] ctypes = DMUtil.splitStrByDelim(chromTypes, ",");
        for (String type : ctypes) {
            ChromType aType = ChromType.fromType(type);
            if (!aType.equals(ChromType.UNKNOWN)) {
                requiredChmTypes.add(aType);
            }
        }
        return requiredChmTypes;
    }

    private List<String> getProbePlatforms(String mircoarrayPlatforms) {
        List<String> requiredPlatforms = new ArrayList<String>();
        String[] platforms = DMUtil.splitStrByDelim(mircoarrayPlatforms, ",");
        for (String platform : platforms) {
            if (StringUtils.isNotBlank(platform)) {
                requiredPlatforms.add(platform);
            }
        }
        return requiredPlatforms;
    }

    private void rest(long time) {
        //try to pause (time)
        try {
            Thread.sleep(time);
        } catch (Exception ex) {
            //ignore whatever caught
        }
    }
}
