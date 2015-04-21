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

package edu.monash.merc.service.impl;

import edu.monash.merc.common.name.*;
import edu.monash.merc.common.page.Pagination;
import edu.monash.merc.common.sql.OrderBy;
import edu.monash.merc.domain.*;
import edu.monash.merc.domain.rifcs.RIFCSDataset;
import edu.monash.merc.dto.*;
import edu.monash.merc.dto.barcode.BarcodeDataBean;
import edu.monash.merc.dto.gpm.GPMDbBean;
import edu.monash.merc.dto.gpm.GPMDbEntryBean;
import edu.monash.merc.dto.gpm.GPMEntryBean;
import edu.monash.merc.dto.hpa.HPAEntryBean;
import edu.monash.merc.dto.rifcs.RifcsInfoBean;
import edu.monash.merc.dto.tl.TLSearchBean;
import edu.monash.merc.exception.DMException;
import edu.monash.merc.mail.MailService;
import edu.monash.merc.rifcs.RifcsService;
import edu.monash.merc.service.*;
import edu.monash.merc.system.version.TLVersionTrack;
import edu.monash.merc.util.DMUtil;
import org.apache.commons.lang.StringUtils;
import org.apache.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.beans.factory.config.BeanDefinition;
import org.springframework.context.annotation.Scope;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.io.File;
import java.util.*;

/**
 * DMSystemServiceImpl class implements the DMSystemService interface which provides the TPB Data service operations.
 *
 * @author Simon Yu
 *         <p/>
 *         Email: xiaoming.yu@monash.edu
 * @version 1.0
 * @since 1.0
 *        <p/>
 *        Date: 16/04/12 11:49 AM
 */
@Scope(BeanDefinition.SCOPE_PROTOTYPE)
@Service
@Transactional
@Qualifier("DMSystemService")
public class DMSystemServiceImpl implements DMSystemService {

    @Autowired
    protected GeneService geneService;

    @Autowired
    protected DBSourceService dbSourceService;

    @Autowired
    protected AccessionService accessionService;

    @Autowired
    protected AccessionTypeService accessionTypeService;

    @Autowired
    protected TPBDataTypeService tpbDataTypeService;

    @Autowired
    protected TLColorService tlColorService;

    @Autowired
    protected DSVersionService dsVersionService;

    @Autowired
    protected TPBVersionService tpbVersionService;

    @Autowired
    protected PEEvidenceService peEvidenceService;

    @Autowired
    protected PTMEvidenceService ptmEvidenceService;

    @Autowired
    protected NXAnnotationService nxAnnotationService;

    @Autowired
    protected NXAnnEvidenceService nxAnnEvidenceService;

    @Autowired
    protected NXIsoFormAnnService nxIsoFormAnnService;

    @Autowired
    protected TLGeneService tlGeneService;

    @Autowired
    protected TLService tlService;

    @Autowired
    protected TPBGeneService tpbGeneService;

    @Autowired
    protected RifcsDatasetService rifcsDatasetService;

    @Autowired
    private RifcsService rifcsService;

    @Autowired
    private MailService mailService;

    @Autowired
    private ProbeService probeService;

    @Autowired
    private TEEvidenceService TEEvidenceService;

    private Logger logger = Logger.getLogger(this.getClass().getName());

    public void setGeneService(GeneService geneService) {
        this.geneService = geneService;
    }

    public void setDbSourceService(DBSourceService dbSourceService) {
        this.dbSourceService = dbSourceService;
    }

    public void setAccessionService(AccessionService accessionService) {
        this.accessionService = accessionService;
    }

    public void setAccessionTypeService(AccessionTypeService accessionTypeService) {
        this.accessionTypeService = accessionTypeService;
    }

    public void setTpbDataTypeService(TPBDataTypeService tpbDataTypeService) {
        this.tpbDataTypeService = tpbDataTypeService;
    }

    public void setTlColorService(TLColorService tlColorService) {
        this.tlColorService = tlColorService;
    }

    public void setDsVersionService(DSVersionService dsVersionService) {
        this.dsVersionService = dsVersionService;
    }

    public void setTpbVersionService(TPBVersionService tpbVersionService) {
        this.tpbVersionService = tpbVersionService;
    }

    public void setPeEvidenceService(PEEvidenceService peEvidenceService) {
        this.peEvidenceService = peEvidenceService;
    }

    public void setPtmEvidenceService(PTMEvidenceService ptmEvidenceService) {
        this.ptmEvidenceService = ptmEvidenceService;
    }

    public void setNxAnnotationService(NXAnnotationService nxAnnotationService) {
        this.nxAnnotationService = nxAnnotationService;
    }

    public void setNxAnnEvidenceService(NXAnnEvidenceService nxAnnEvidenceService) {
        this.nxAnnEvidenceService = nxAnnEvidenceService;
    }

    public void setNxIsoFormAnnService(NXIsoFormAnnService nxIsoFormAnnService) {
        this.nxIsoFormAnnService = nxIsoFormAnnService;
    }

    public void setTlGeneService(TLGeneService tlGeneService) {
        this.tlGeneService = tlGeneService;
    }

    public void setTlService(TLService tlService) {
        this.tlService = tlService;
    }

    public void setTpbGeneService(TPBGeneService tpbGeneService) {
        this.tpbGeneService = tpbGeneService;
    }

    public void setRifcsDatasetService(RifcsDatasetService rifcsDatasetService) {
        this.rifcsDatasetService = rifcsDatasetService;
    }

    public void setRifcsService(RifcsService rifcsService) {
        this.rifcsService = rifcsService;
    }

    public void setMailService(MailService mailService) {
        this.mailService = mailService;
    }

    public void setProbeService(ProbeService probeService) {
        this.probeService = probeService;
    }

    public void setTEEvidenceService(TEEvidenceService TEEvidenceService) {
        this.TEEvidenceService = TEEvidenceService;
    }

    //Gene Implementations

    /**
     * {@inheritDoc}
     */
    public void saveGene(Gene gene) {
        this.geneService.saveGene(gene);
    }

    /**
     * {@inheritDoc}
     */
    public void mergeGene(Gene gene) {
        this.geneService.mergeGene(gene);
    }

    /**
     * {@inheritDoc}
     */
    public void updateGene(Gene gene) {
        this.geneService.updateGene(gene);
    }

    /**
     * {@inheritDoc}
     */
    public void deleteGene(Gene gene) {
        this.geneService.deleteGene(gene);
    }

    /**
     * {@inheritDoc}
     */
    public Gene getGeneById(long id) {
        return this.geneService.getGeneById(id);
    }

    /**
     * {@inheritDoc}
     */
    public void deleteGeneById(long id) {
        this.geneService.deleteGeneById(id);
    }

    /**
     * {@inheritDoc}
     */
    public Gene getGeneByEnsgAndDbVersion(String ensgAccession, String dbSource, Date versionTime) {
        return this.geneService.getGeneByEnsgAndDbVersion(ensgAccession, dbSource, versionTime);
    }

    /**
     * {@inheritDoc}
     */
    public List<Gene> getGenesByDBSChromVersion(DbAcType dbAcType, ChromType chromType, Date versionTime) {
        return this.geneService.getGenesByDBSChromVersion(dbAcType, chromType, versionTime);
    }

    /**
     * {@inheritDoc}
     */
    public List<Gene> getGenesByTLGeneId(long tlGeneId) {
        return this.geneService.getGenesByTLGeneId(tlGeneId);
    }

    /**
     * {@inheritDoc}
     */
    public List<Accession> getAllAssociatedAccessionsByGeneId(long geneId) {
        return this.geneService.getAllAssociatedAccessionsByGeneId(geneId);
    }

    /**
     * {@inheritDoc}
     */
    public void saveDBSource(DBSource dbSource) {
        this.dbSourceService.saveDBSource(dbSource);
    }

    /**
     * {@inheritDoc}
     */
    public void mergeDBSource(DBSource dbSource) {
        this.dbSourceService.mergeDBSource(dbSource);
    }

    /**
     * {@inheritDoc}
     */
    public void updateDBSource(DBSource dbSource) {
        this.dbSourceService.updateDBSource(dbSource);
    }

    /**
     * {@inheritDoc}
     */
    public void deleteDBSourceById(long id) {
        this.dbSourceService.deleteDBSourceById(id);
    }

    /**
     * {@inheritDoc}
     */
    public DBSource getDBSourceById(long id) {
        return this.dbSourceService.getDBSourceById(id);
    }

    /**
     * {@inheritDoc}
     */
    public DBSource getDBSourceByName(String dbName) {
        return this.dbSourceService.getDBSourceByName(dbName);
    }

    /**
     * {@inheritDoc}
     */
    public void saveAccessionType(AccessionType accessionType) {
        this.accessionTypeService.saveAccessionType(accessionType);
    }

    /**
     * {@inheritDoc}
     */

    public void mergeAccessionType(AccessionType accessionType) {
        this.accessionTypeService.mergeAccessionType(accessionType);
    }

    /**
     * {@inheritDoc}
     */
    public void updateAccessionType(AccessionType accessionType) {
        this.accessionTypeService.updateAccessionType(accessionType);
    }

    /**
     * {@inheritDoc}
     */
    public void deleteAccessionType(AccessionType accessionType) {
        this.accessionTypeService.deleteAccessionType(accessionType);
    }

    /**
     * {@inheritDoc}
     */
    public void deleteAccessionTypeById(long id) {
        this.accessionTypeService.deleteAccessionTypeById(id);
    }

    /**
     * {@inheritDoc}
     */
    public AccessionType getAccessionTypeById(long id) {
        return this.accessionTypeService.getAccessionTypeById(id);
    }

    /**
     * {@inheritDoc}
     */
    public AccessionType getAccessionTypeByType(String typeName) {
        return this.accessionTypeService.getAccessionTypeByType(typeName);
    }

    /**
     * {@inheritDoc}
     */
    public void saveAccession(Accession accession) {
        this.accessionService.saveAccession(accession);
    }

    /**
     * {@inheritDoc}
     */
    public void mergeAccession(Accession accession) {
        this.accessionService.mergeAccession(accession);
    }

    /**
     * {@inheritDoc}
     */
    public void updateAccession(Accession accession) {
        this.accessionService.updateAccession(accession);
    }

    /**
     * {@inheritDoc}
     */
    public void deleteAccession(Accession accession) {
        this.accessionService.deleteAccession(accession);
    }

    /**
     * {@inheritDoc}
     */
    public void deleteAccessionById(long id) {
        this.accessionService.deleteAccessionById(id);
    }

    /**
     * {@inheritDoc}
     */
    public void deleteAccessionByAcId(String acId) {
        this.accessionService.deleteAccessionByAcId(acId);
    }

    /**
     * {@inheritDoc}
     */
    public Accession getAccessionById(long id) {
        return this.accessionService.getAccessionById(id);
    }

    /**
     * {@inheritDoc}
     */
    public Accession getAccessionByAccessionAcType(String accession, String acType) {
        return this.accessionService.getAccessionByAccessionAcType(accession, acType);
    }

    /**
     * {@inheritDoc}
     */
    public void saveTPBDataType(TPBDataType tpbDataType) {
        this.tpbDataTypeService.saveTPBDataType(tpbDataType);
    }

    /**
     * {@inheritDoc}
     */
    public void mergeTPBDataType(TPBDataType tpbDataType) {
        this.tpbDataTypeService.mergeTPBDataType(tpbDataType);
    }

    /**
     * {@inheritDoc}
     */
    public void updateTPBDataType(TPBDataType tpbDataType) {
        this.tpbDataTypeService.updateTPBDataType(tpbDataType);
    }

    /**
     * {@inheritDoc}
     */
    public void deleteTPBDataType(TPBDataType tpbDataType) {
        this.tpbDataTypeService.deleteTPBDataType(tpbDataType);
    }

    /**
     * {@inheritDoc}
     */
    public void deleteTPBDataTypeById(long id) {
        this.tpbDataTypeService.deleteTPBDataTypeById(id);
    }

    /**
     * {@inheritDoc}
     */
    public TPBDataType getTPBDataTypeById(long id) {
        return this.tpbDataTypeService.getTPBDataTypeById(id);
    }

    /**
     * {@inheritDoc}
     */
    public TPBDataType getTPBDataTypeByTypeName(String type) {
        return this.tpbDataTypeService.getTPBDataTypeByTypeName(type);
    }

    /**
     * {@inheritDoc}
     */
    public List<TPBDataType> getSubTPBDataType(long id) {
        return this.tpbDataTypeService.getSubTPBDataType(id);
    }

    /**
     * {@inheritDoc}
     */
    public List<TPBDataType> getSubTPBDataType(String dataType) {
        return this.tpbDataTypeService.getSubTPBDataType(dataType);
    }

    /**
     * {@inheritDoc}
     */
    public boolean isLastLevelType(String dataType) {
        return this.tpbDataTypeService.isLastLevelType(dataType);
    }

    /**
     * {@inheritDoc}
     */
    public boolean isLastLevelType(long id) {
        return this.tpbDataTypeService.isLastLevelType(id);
    }

    //DSVersion

    /**
     * {@inheritDoc}
     */
    public void saveDSVersion(DSVersion dsVersion) {
        this.dsVersionService.saveDSVersion(dsVersion);
    }

    /**
     * {@inheritDoc}
     */
    public void mergeDSVersion(DSVersion dsVersion) {
        this.dsVersionService.mergeDSVersion(dsVersion);
    }

    /**
     * {@inheritDoc}
     */
    public void updateDSVersion(DSVersion dsVersion) {
        this.dsVersionService.updateDSVersion(dsVersion);
    }

    /**
     * {@inheritDoc}
     */
    public void deleteDSVersion(DSVersion dsVersion) {
        this.dsVersionService.deleteDSVersion(dsVersion);
    }

    /**
     * {@inheritDoc}
     */
    public void deleteDSVersionById(long id) {
        this.dsVersionService.deleteDSVersionById(id);
    }

    /**
     * {@inheritDoc}
     */
    public DSVersion getDSVersionById(long id) {
        return this.dsVersionService.getDSVersionById(id);
    }

    /**
     * {@inheritDoc}
     */
    public DSVersion getCurrentDSVersionByChromDbs(DbAcType dbAcType, ChromType chromType) {
        return this.dsVersionService.getCurrentDSVersionByChromDbs(dbAcType, chromType);
    }

    /**
     * {@inheritDoc}
     */
    public boolean checkUpToDate(DbAcType dbAcType, ChromType chromType, String fileName, String timeToken) {
        return this.dsVersionService.checkUpToDate(dbAcType, chromType, fileName, timeToken);
    }

    /**
     * {@inheritDoc}
     */
    public List<DBVersionBean> getLatestDBSVersionByChromosome(ChromType chromType) {
        return this.dsVersionService.getLatestDBSVersionByChromosome(chromType);
    }

    //TPBVersion

    /**
     * {@inheritDoc}
     */
    public void saveTPBVersion(TPBVersion tpbVersion) {
        this.tpbVersionService.saveTPBVersion(tpbVersion);
    }

    /**
     * {@inheritDoc}
     */
    public void mergeTPBVersion(TPBVersion tpbVersion) {
        this.tpbVersionService.mergeTPBVersion(tpbVersion);
    }

    /**
     * {@inheritDoc}
     */
    public void updateTPBVersion(TPBVersion tpbVersion) {
        this.tpbVersionService.updateTPBVersion(tpbVersion);
    }

    /**
     * {@inheritDoc}
     */
    public void deleteTPBVersion(TPBVersion tpbVersion) {
        this.tpbVersionService.deleteTPBVersion(tpbVersion);
    }

    /**
     * {@inheritDoc}
     */
    public TPBVersion getTPBVersionById(long id) {
        return this.tpbVersionService.getTPBVersionById(id);
    }

    /**
     * {@inheritDoc}
     */
    public void deleteTPBVersionById(long id) {
        this.tpbVersionService.deleteTPBVersionById(id);
    }

    /**
     * {@inheritDoc}
     */
    public TPBVersion getCurrentVersion(ChromType chromType, int trackTokne) {
        return this.tpbVersionService.getCurrentVersion(chromType, trackTokne);
    }

    /**
     * {@inheritDoc}
     */
    public boolean checkTPBVersionAvailable(ChromType chromType, TLVersionTrack tlVersionTrack) {
        return this.tpbVersionService.checkTPBVersionAvailable(chromType, tlVersionTrack);
    }

    /**
     * {@inheritDoc}
     */
    public TPBVersion getCurrentTPBVersionByChromTypeTrackToken(ChromType chromType, int trackToken) {
        return this.tpbVersionService.getCurrentTPBVersionByChromTypeTrackToken(chromType, trackToken);
    }

    /**
     * {@inheritDoc}
     */
    public List<TPBVersion> getAllTPBVersionByChromTypeTrackToken(ChromType chromType, int trackToken) {
        return this.tpbVersionService.getAllTPBVersionByChromTypeTrackToken(chromType, trackToken);
    }

    /**
     * {@inheritDoc}
     */
    public List<TPBVersion> getAllTPBVersions() {
        return this.tpbVersionService.getAllTPBVersions();
    }

    public List<MaxDsTPBVersion> getAllChromosomeTPBVersionByMaxCombinatedDs() {
        return this.tpbVersionService.getAllChromosomeTPBVersionByMaxCombinatedDs();
    }

    /**
     * {@inheritDoc}
     */


    //Traffic Lights TLColor
    public void saveTLColor(TLColor tlColor) {
        this.tlColorService.saveTLColor(tlColor);
    }

    public void mergeTLColor(TLColor tlColor) {
        this.tlColorService.mergeTLColor(tlColor);
    }

    public void updateTLColor(TLColor tlColor) {
        this.tlColorService.updateTLColor(tlColor);
    }

    public void deleteTLColor(TLColor tlColor) {
        this.tlColorService.deleteTLColor(tlColor);
    }

    public void deleteTLColorById(long id) {
        this.tlColorService.deleteTLColorById(id);
    }

    public TLColor getTLColorById(long id) {
        return this.tlColorService.getTLColorById(id);
    }

    public TLColor getTLColorByColor(String color) {
        return this.tlColorService.getTLColorByColor(color);
    }

    public TLColor getTLColorByColorLevel(int colorLevel) {
        return this.tlColorService.getTLColorByColorLevel(colorLevel);
    }

    //Evidence
    public void savePEEvidence(PEEvidence peEvidence) {
        this.peEvidenceService.savePEEvidence(peEvidence);
    }

    public void mergePEEvidence(PEEvidence peEvidence) {
        this.peEvidenceService.mergePEEvidence(peEvidence);
    }

    public void updatePEEvidence(PEEvidence peEvidence) {
        this.peEvidenceService.updatePEEvidence(peEvidence);
    }

    public void deletePEEvidence(PEEvidence peEvidence) {
        this.peEvidenceService.deletePEEvidence(peEvidence);
    }

    public void deletePEEvidenceById(long id) {
        this.peEvidenceService.deletePEEvidenceById(id);
    }

    public PEEvidence getPEEvidenceById(long id) {
        return this.peEvidenceService.getPEEvidenceById(id);
    }

    public PEEvidence getPESummaryByGeneAndType(long geneId, DataType dataType) {
        return this.peEvidenceService.getPESummaryByGeneAndType(geneId, dataType);
    }

    public List<PEEvidence> getPEEvidencesByGeneAndType(long geneId, DataType dataType) {
        return this.peEvidenceService.getPEEvidencesByGeneAndType(geneId, dataType);
    }

    public TLEvidenceSummary getAllPEEvidencesByGeneAndType(long geneId, DataType dataType) {
        List<PEEvidence> allEvidences = this.getPEEvidencesByGeneAndType(geneId, dataType);
        return copyFromPEEvidences(allEvidences);
    }

    private TLEvidenceSummary copyFromPEEvidences(List<PEEvidence> peEvidences) {
        TLEvidenceSummary evidenceSummary = new TLEvidenceSummary();
        evidenceSummary.setPrimaryDataType(DataType.PE.type());

        if (peEvidences != null) {
            List<TLEvidence> tlEvidenceList = new ArrayList<TLEvidence>();
            for (PEEvidence peEvidence : peEvidences) {
                int colorLevel = peEvidence.getColorLevel();
                Gene gene = peEvidence.getGene();
                long geneId = gene.getId();
                String geneName = gene.getDisplayName();
                String ensemblId = gene.getEnsgAccession();
                if (StringUtils.isBlank(ensemblId)) {
                    ensemblId = "N/A";
                }
                long evidenceId = peEvidence.getId();
                String evidence = peEvidence.getEvidenceValue();

                //identified accession
                Accession identifiedAc = peEvidence.getIdentifiedAccession();
                String identifiedAccession = identifiedAc.getAccession();

                //primary DBSource
                DBSource primaryDbSource = gene.getDbSource();
                String dbSourceName = primaryDbSource.getDbName();

                TPBDataType tpbDataType = peEvidence.getTpbDataType();
                String dataType = tpbDataType.getDataType();
                String dataTypeDisplayName = tpbDataType.getDisplayName();
                String typeShortName = StringUtils.replace(dataType, "_", " ");
                String hyperLink = peEvidence.getHyperLink();
                TLEvidence tlEvidence = new TLEvidence();
                tlEvidence.setGeneId(geneId);
                tlEvidence.setGeneName(geneName);
                tlEvidence.setEnsemblId(ensemblId);
                tlEvidence.setEvidenceId(evidenceId);
                tlEvidence.setEvidence(evidence);
                tlEvidence.setIdentifiedAccession(identifiedAccession);
                tlEvidence.setDbSource(dbSourceName);
                tlEvidence.setTpbDataType(dataType);
                tlEvidence.setTypeShortName(typeShortName);
                tlEvidence.setTypeDisplayName(dataTypeDisplayName);
                tlEvidence.setColorLevel(colorLevel);
                tlEvidence.setHyperLink(hyperLink);

                boolean lastLevelType = this.isLastLevelType(tpbDataType.getId());
                tlEvidence.setLastLevel(lastLevelType);
                tlEvidence.setTlLevel(tpbDataType.getTlLevel());
                tlEvidenceList.add(tlEvidence);
            }
            //set the evidence list
            evidenceSummary.setTlEvidences(tlEvidenceList);
        }
        return evidenceSummary;
    }

    public TLEvidenceSummary getPETLSummary(long tlGeneId, DataType dataType) {
        return this.getTLEvidenceSummary(tlGeneId, dataType);
    }

    private TLEvidenceSummary getTLEvidenceSummary(long tlGeneId, DataType dataType) {
        TrafficLight trafficLight = this.getTrafficLightById(tlGeneId);
        //get the traffic light gene
        TLGene tlGene = trafficLight.getTlGene();
        //get the TPBDataType
        TPBDataType tpbDataType = this.getTPBDataTypeByTypeName(dataType.type());
        //Create TLEvidenceSummary
        TLEvidenceSummary tlEvidenceSummary = new TLEvidenceSummary();
        //create a traffic light TLEvidenceSummaryHeader
        TLEvidenceSummaryHeader tlEvidenceSummaryHeader = generateTLEvidenceSummaryHeaderByTLGene(tlGene, null, tpbDataType);
        //set the TLEvidenceSummaryHeader
        tlEvidenceSummary.setTlEvidenceSummaryHeader(tlEvidenceSummaryHeader);

        //get all genes which are associated with this traffic light gene
        List<Gene> genes = this.getGenesByTLGeneId(tlGeneId);
        //create a list of TLEvidence Bean to store the TLEvidence
        List<TLEvidence> tlEvidences = new ArrayList<TLEvidence>();
        for (Gene gene : genes) {
            Evidence tlEvidenceSum = null;
            if (dataType.isPE()) {
                tlEvidenceSum = this.getPESummaryByGeneAndType(gene.getId(), dataType);
            }
            if (dataType.isPTM()) {
                tlEvidenceSum = this.getPTMSummaryByGeneAndType(gene.getId(), dataType);
            }
            if (dataType.isTE()) {
                tlEvidenceSum = this.getTESummaryByGeneAndType(gene.getId(), dataType);
            }
            if (tlEvidenceSum != null) {
                //create a TLEvidence Bean
                TLEvidence tlEvidence = covertToTLEvidence(gene, tpbDataType, tlEvidenceSum);
                boolean lastLevelType = this.isLastLevelType(tpbDataType.getId());
                tlEvidence.setLastLevel(lastLevelType);
                tlEvidence.setTlLevel(tpbDataType.getTlLevel());
                tlEvidences.add(tlEvidence);
            }
        }
        if (tlEvidences.size() != 0) {
            tlEvidenceSummary.setTlEvidences(tlEvidences);
        }
        return tlEvidenceSummary;
    }

    private TLEvidenceSummaryHeader generateTLEvidenceSummaryHeaderByTLGene(TLGene tlGene, String dbSourceName, TPBDataType tpbDataType) {
        String tlGeneName = tlGene.getDisplayName();
        String tlEnsgAc = tlGene.getEnsgAccession();
        if (StringUtils.isBlank(tlEnsgAc)) {
            tlEnsgAc = "N/A";
        }
        String tpbDataTypeName = tpbDataType.getDataType();
        String typeDisplayName = tpbDataType.getDisplayName();
        String typeShortName = StringUtils.replace(tpbDataTypeName, "_", " ");

        TLEvidenceSummaryHeader tlEvidenceSummaryHeader = new TLEvidenceSummaryHeader();
        tlEvidenceSummaryHeader.setGeneName(tlGeneName);
        tlEvidenceSummaryHeader.setEnsemblId(tlEnsgAc);
        tlEvidenceSummaryHeader.setTpbDataType(tpbDataTypeName);
        tlEvidenceSummaryHeader.setTypeShortName(typeShortName);
        tlEvidenceSummaryHeader.setTypeDisplayName(typeDisplayName);
        //set the dbSource Name
        tlEvidenceSummaryHeader.setDbSourceName(dbSourceName);

        return tlEvidenceSummaryHeader;
    }

    private TLEvidence covertToTLEvidence(Gene gene, TPBDataType tpbDataType, Evidence anEvidence) {
        int colorLevel = anEvidence.getColorLevel();
        long geneId = gene.getId();
        String geneName = gene.getDisplayName();
        String ensemblId = gene.getEnsgAccession();
        if (StringUtils.isBlank(ensemblId)) {
            ensemblId = "N/A";
        }
        long evidenceId = 0;
        if (anEvidence instanceof PEEvidence) {
            evidenceId = ((PEEvidence) anEvidence).getId();
        }
        if (anEvidence instanceof PTMEvidence) {
            evidenceId = ((PTMEvidence) anEvidence).getId();
        }
        if (anEvidence instanceof TEEvidence) {
            evidenceId = ((TEEvidence) anEvidence).getId();
        }

        String evidence = anEvidence.getEvidenceValue();

        //identified accession
        Accession identifiedAc = anEvidence.getIdentifiedAccession();
        String identifiedAccession = identifiedAc.getAccession();

        //primary DBSource
        DBSource primaryDbSource = gene.getDbSource();
        String dbSourceName = primaryDbSource.getDbName();

        String dataType = tpbDataType.getDataType();
        String dataTypeDisplayName = tpbDataType.getDisplayName();
        String typeShortName = StringUtils.replace(dataType, "_", " ");
        TLEvidence tlEvidence = new TLEvidence();
        tlEvidence.setGeneId(geneId);
        tlEvidence.setGeneName(geneName);
        tlEvidence.setEnsemblId(ensemblId);
        tlEvidence.setEvidenceId(evidenceId);
        tlEvidence.setEvidence(evidence);
        tlEvidence.setIdentifiedAccession(identifiedAccession);
        tlEvidence.setDbSource(dbSourceName);
        tlEvidence.setTpbDataType(dataType);
        tlEvidence.setTypeShortName(typeShortName);
        tlEvidence.setTypeDisplayName(dataTypeDisplayName);
        tlEvidence.setColorLevel(colorLevel);
        return tlEvidence;
    }

    public TLEvidenceSummary getTLPESummaryBySrcGene(String dbSource, long geneId, DataType dataType) {
        return this.getTLEvidenceSummaryBySrcGene(dbSource, geneId, dataType);
    }

    private TLEvidenceSummary getTLEvidenceSummaryBySrcGene(String dbSource, long geneId, DataType dataType) {
        //get the gene
        Gene gene = this.getGeneById(geneId);
        //get the TPBDataType
        TPBDataType tpbDataType = this.getTPBDataTypeByTypeName(dataType.type());

        //Create TLEvidenceSummary
        TLEvidenceSummary tlEvidenceSummary = new TLEvidenceSummary();
        //create a traffic TLEvidenceSummaryHeader
        TLEvidenceSummaryHeader tlEvidenceSummaryHeader = generateTLEvidenceSummaryHeaderByGene(gene, dbSource, tpbDataType);
        //set the TLEvidenceBaseInfo
        tlEvidenceSummary.setTlEvidenceSummaryHeader(tlEvidenceSummaryHeader);

        List<TLEvidence> tlEvidences = new ArrayList<TLEvidence>();

        List<TPBDataType> subTPBDataTypes = this.getSubTPBDataType(tpbDataType.getId());
        //if this DataType is already a last level of DataType
        if (subTPBDataTypes == null || subTPBDataTypes.size() == 0) {
            Evidence evidence = null;

            if (dataType.isPE()) {
                evidence = this.getPESummaryByGeneAndType(gene.getId(), dataType);
            }

            if (dataType.isPTM()) {
                evidence = this.getPTMSummaryByGeneAndType(gene.getId(), dataType);
            }

            if (dataType.isTE()) {
                evidence = this.getTESummaryByGeneAndType(gene.getId(), dataType);
            }

            if (evidence != null) {
                //create TLEvidence Bean
                TLEvidence tlEvidence = covertToTLEvidence(gene, tpbDataType, evidence);
                tlEvidence.setLastLevel(true);
                tlEvidence.setTlLevel(tpbDataType.getTlLevel());
                tlEvidences.add(tlEvidence);
            }
            //get all evidences for all sub DataTypes
        } else {
            for (TPBDataType subTpbDataType : subTPBDataTypes) {
                String subDType = subTpbDataType.getDataType();
                DataType subDataType = DataType.fromType(subDType);
                Evidence evidence = null;

                if (dataType.isPE()) {
                    evidence = this.getPESummaryByGeneAndType(gene.getId(), subDataType);
                }
                if (dataType.isPTM()) {
                    evidence = this.getPTMSummaryByGeneAndType(gene.getId(), subDataType);
                }
                if (dataType.isTE()) {
                    evidence = this.getTESummaryByGeneAndType(gene.getId(), subDataType);
                }

                if (evidence != null) {
                    //create TLEvidence Bean
                    TLEvidence tlEvidence = covertToTLEvidence(gene, subTpbDataType, evidence);
                    //check whether it's last level of the TPBDataType or not
                    boolean lastLevelType = this.isLastLevelType(subTpbDataType.getId());
                    tlEvidence.setLastLevel(lastLevelType);
                    tlEvidence.setTlLevel(subTpbDataType.getTlLevel());
                    tlEvidences.add(tlEvidence);
                }
            }
        }

        if (tlEvidences.size() != 0) {
            tlEvidenceSummary.setTlEvidences(tlEvidences);
        }
        return tlEvidenceSummary;
    }

    private TLEvidenceSummaryHeader generateTLEvidenceSummaryHeaderByGene(Gene gene, String dbSourceName, TPBDataType tpbDataType) {
        String tlGeneName = gene.getDisplayName();
        String tlEnsgAc = gene.getEnsgAccession();
        if (StringUtils.isBlank(tlEnsgAc)) {
            tlEnsgAc = "N/A";
        }
        String tpbDataTypeName = tpbDataType.getDataType();
        String typeDisplayName = tpbDataType.getDisplayName();
        String typeShortName = StringUtils.replace(tpbDataTypeName, "_", " ");

        TLEvidenceSummaryHeader tlEvidenceSummaryHeader = new TLEvidenceSummaryHeader();
        tlEvidenceSummaryHeader.setGeneName(tlGeneName);
        tlEvidenceSummaryHeader.setEnsemblId(tlEnsgAc);
        tlEvidenceSummaryHeader.setTpbDataType(tpbDataTypeName);
        tlEvidenceSummaryHeader.setTypeShortName(typeShortName);
        tlEvidenceSummaryHeader.setTypeDisplayName(typeDisplayName);
        tlEvidenceSummaryHeader.setDbSourceName(dbSourceName);
        return tlEvidenceSummaryHeader;
    }


    @Override
    public void savePTMEvidence(PTMEvidence ptmEvidence) {
        this.ptmEvidenceService.savePTMEvidence(ptmEvidence);
    }

    @Override
    public void mergePTMEvidence(PTMEvidence ptmEvidence) {
        this.ptmEvidenceService.mergePTMEvidence(ptmEvidence);
    }

    @Override
    public void updatePTMEvidence(PTMEvidence ptmEvidence) {
        this.ptmEvidenceService.updatePTMEvidence(ptmEvidence);
    }

    @Override
    public void deletePTMEvidence(PTMEvidence ptmEvidence) {
        this.ptmEvidenceService.deletePTMEvidence(ptmEvidence);
    }

    @Override
    public PTMEvidence getPTMEvidenceById(long id) {
        return this.ptmEvidenceService.getPTMEvidenceById(id);
    }

    @Override
    public void deletePTMEvidenceById(long id) {
        this.ptmEvidenceService.deletePTMEvidenceById(id);
    }

    @Override
    public PTMEvidence getPTMSummaryByGeneAndType(long geneId, DataType dataType) {
        return this.ptmEvidenceService.getPTMSummaryByGeneAndType(geneId, dataType);
    }

    @Override
    public List<PTMEvidence> getPTMEvidencesByGeneAndType(long geneId, DataType dataType) {
        return this.ptmEvidenceService.getPTMEvidencesByGeneAndType(geneId, dataType);
    }

    @Override
    public TLEvidenceSummary getPTMTLSummary(long tlGeneId, DataType dataType) {
        return this.getTLEvidenceSummary(tlGeneId, dataType);
    }

    @Override
    public TLEvidenceSummary getTLPTMSummaryBySrcGene(String dbSource, long geneId, DataType dataType) {
        return this.getTLEvidenceSummaryBySrcGene(dbSource, geneId, dataType);
    }

    @Override
    public TLEvidenceSummary getAllPTMEvidencesByGeneAndType(long geneId, DataType dataType) {
        List<PTMEvidence> allEvidences = this.getPTMEvidencesByGeneAndType(geneId, dataType);
        return copyFromPTMEvidences(allEvidences);
    }

    private TLEvidenceSummary copyFromPTMEvidences(List<PTMEvidence> ptmEvidences) {
        TLEvidenceSummary evidenceSummary = new TLEvidenceSummary();
        evidenceSummary.setPrimaryDataType(DataType.PTM.type());

        if (ptmEvidences != null) {
            List<TLEvidence> tlEvidenceList = new ArrayList<TLEvidence>();
            for (PTMEvidence ptmEvidence : ptmEvidences) {
                int colorLevel = ptmEvidence.getColorLevel();
                Gene gene = ptmEvidence.getGene();
                long geneId = gene.getId();
                String geneName = gene.getDisplayName();
                String ensemblId = gene.getEnsgAccession();
                if (StringUtils.isBlank(ensemblId)) {
                    ensemblId = "N/A";
                }
                long evidenceId = ptmEvidence.getId();
                int pos = ptmEvidence.getPos();
                String evidence = ptmEvidence.getEvidenceValue();

                //identified accession
                Accession identifiedAc = ptmEvidence.getIdentifiedAccession();
                String identifiedAccession = identifiedAc.getAccession();

                //primary DBSource
                DBSource primaryDbSource = gene.getDbSource();
                String dbSourceName = primaryDbSource.getDbName();

                TPBDataType tpbDataType = ptmEvidence.getTpbDataType();
                String dataType = tpbDataType.getDataType();
                String dataTypeDisplayName = tpbDataType.getDisplayName();
                String typeShortName = StringUtils.replace(dataType, "_", " ");
                String hyperLink = ptmEvidence.getHyperLink();

                TLEvidence tlEvidence = new TLEvidence();
                tlEvidence.setGeneId(geneId);
                tlEvidence.setGeneName(geneName);
                tlEvidence.setEnsemblId(ensemblId);
                tlEvidence.setEvidenceId(evidenceId);
                tlEvidence.setPos(pos);
                tlEvidence.setEvidence(evidence);
                tlEvidence.setIdentifiedAccession(identifiedAccession);
                tlEvidence.setDbSource(dbSourceName);
                tlEvidence.setTpbDataType(dataType);
                tlEvidence.setTypeShortName(typeShortName);
                tlEvidence.setTypeDisplayName(dataTypeDisplayName);
                tlEvidence.setColorLevel(colorLevel);
                tlEvidence.setHyperLink(hyperLink);

                boolean lastLevelType = this.isLastLevelType(tpbDataType.getId());
                tlEvidence.setLastLevel(lastLevelType);
                tlEvidence.setTlLevel(tpbDataType.getTlLevel());
                tlEvidenceList.add(tlEvidence);
            }
            //set the evidence list
            evidenceSummary.setTlEvidences(tlEvidenceList);
        }
        return evidenceSummary;
    }

    //TE Evidence
    @Override
    public void saveTEEvidence(TEEvidence TEEvidence) {
        this.TEEvidenceService.saveTEEvidence(TEEvidence);
    }

    @Override
    public void mergeTEEvidence(TEEvidence TEEvidence) {
        this.TEEvidenceService.mergeTEEvidence(TEEvidence);
    }

    @Override
    public void updateTEEvidence(TEEvidence TEEvidence) {
        this.TEEvidenceService.updateTEEvidence(TEEvidence);
    }

    @Override
    public void deleteTEEvidence(TEEvidence TEEvidence) {
        this.TEEvidenceService.deleteTEEvidence(TEEvidence);
    }

    @Override
    public TEEvidence getTEEvidenceById(long id) {
        return this.TEEvidenceService.getTEEvidenceById(id);
    }

    @Override
    public void deleteTEEvidenceById(long id) {
        this.TEEvidenceService.deleteTEEvidenceById(id);
    }

    @Override
    public TEEvidence getTESummaryByGeneAndType(long geneId, DataType dataType) {
        return this.TEEvidenceService.getTESummaryByGeneAndType(geneId, dataType);
    }

    @Override
    public List<TEEvidence> getTEEvidencesByGeneAndType(long geneId, DataType dataType) {
        return this.TEEvidenceService.getTEEvidencesByGeneAndType(geneId, dataType);
    }

    @Override
    public TLEvidenceSummary getTETLSummary(long tlGeneId, DataType dataType) {
        return this.getTLEvidenceSummary(tlGeneId, dataType);
    }

    @Override
    public TLEvidenceSummary getTLTESummaryBySrcGene(String dbSource, long geneId, DataType dataType) {
        return this.getTLEvidenceSummaryBySrcGene(dbSource, geneId, dataType);
    }

    @Override
    public TLEvidenceSummary getAllTEEvidencesByGeneAndType(long geneId, DataType dataType) {
        List<TEEvidence> allTeMaEvidences = this.getTEEvidencesByGeneAndType(geneId, dataType);
        return copyFromTeEvidences(allTeMaEvidences);
    }

    private TLEvidenceSummary copyFromTeEvidences(List<TEEvidence> TEEvidences) {
        TLEvidenceSummary evidenceSummary = new TLEvidenceSummary();
        evidenceSummary.setPrimaryDataType(DataType.TE.type());

        if (TEEvidences != null) {
            List<TLEvidence> tlEvidenceList = new ArrayList<TLEvidence>();
            for (TEEvidence TEEvidence : TEEvidences) {
                int colorLevel = TEEvidence.getColorLevel();
                Gene gene = TEEvidence.getGene();
                long geneId = gene.getId();
                String geneName = gene.getDisplayName();
                String ensemblId = gene.getEnsgAccession();
                if (StringUtils.isBlank(ensemblId)) {
                    ensemblId = "N/A";
                }
                long evidenceId = TEEvidence.getId();
                String tissueName = TEEvidence.getTissueName();
                double expression = TEEvidence.getExpression();
                boolean uniqueGene = TEEvidence.isUnique();
                String evidence = TEEvidence.getEvidenceValue();


                //identified accession
                Accession identifiedAc = TEEvidence.getIdentifiedAccession();
                String identifiedAccession = identifiedAc.getAccession();

                //primary DBSource
                DBSource primaryDbSource = gene.getDbSource();
                String dbSourceName = primaryDbSource.getDbName();

                TPBDataType tpbDataType = TEEvidence.getTpbDataType();
                String dataType = tpbDataType.getDataType();
                String dataTypeDisplayName = tpbDataType.getDisplayName();
                String typeShortName = StringUtils.replace(dataType, "_", " ");
                String hyperLink = TEEvidence.getHyperLink();
                TLEvidence tlEvidence = new TLEvidence();
                tlEvidence.setGeneId(geneId);
                tlEvidence.setGeneName(geneName);
                tlEvidence.setEnsemblId(ensemblId);
                tlEvidence.setEvidenceId(evidenceId);
                tlEvidence.setEvidence(evidence);
                tlEvidence.setTissue(tissueName);
                tlEvidence.setExpression(expression);
                tlEvidence.setUniqueGene(uniqueGene);
                tlEvidence.setIdentifiedAccession(identifiedAccession);
                tlEvidence.setDbSource(dbSourceName);
                tlEvidence.setTpbDataType(dataType);
                tlEvidence.setTypeShortName(typeShortName);
                tlEvidence.setTypeDisplayName(dataTypeDisplayName);
                tlEvidence.setColorLevel(colorLevel);
                tlEvidence.setHyperLink(hyperLink);

                boolean lastLevelType = this.isLastLevelType(tpbDataType.getId());
                tlEvidence.setLastLevel(lastLevelType);
                tlEvidence.setTlLevel(tpbDataType.getTlLevel());
                tlEvidenceList.add(tlEvidence);
            }
            //set the evidence list
            evidenceSummary.setTlEvidences(tlEvidenceList);
        }
        return evidenceSummary;
    }


    //nextprot annotations
    public void saveNXAnnotation(NXAnnotation nxAnnotation) {
        this.nxAnnotationService.saveNXAnnotation(nxAnnotation);
    }

    public void updateNXAnnotation(NXAnnotation nxAnnotation) {
        this.nxAnnotationService.updateNXAnnotation(nxAnnotation);
    }

    public void deleteNXAnnotation(NXAnnotation nxAnnotation) {
        this.nxAnnotationService.deleteNXAnnotation(nxAnnotation);
    }

    public void deleteNXAnnotationById(long id) {
        this.nxAnnotationService.deleteNXAnnotationById(id);
    }

    public NXAnnotation getNXAnnotationById(long id) {
        return this.nxAnnotationService.getNXAnnotationById(id);
    }

    //nextprot annotation evidence
    public void saveNXAnnEvidence(NXAnnEvidence nxAnnEvidence) {
        this.nxAnnEvidenceService.saveNXAnnEvidence(nxAnnEvidence);
    }

    public void updateNXAnnEvidence(NXAnnEvidence nxAnnEvidence) {
        this.nxAnnEvidenceService.updateNXAnnEvidence(nxAnnEvidence);
    }

    public void deleteNXAnnEvidence(NXAnnEvidence nxAnnEvidence) {
        this.nxAnnEvidenceService.deleteNXAnnEvidence(nxAnnEvidence);
    }

    public void deleteNXAnnEvidenceById(long id) {
        this.nxAnnEvidenceService.deleteNXAnnEvidenceById(id);
    }

    public NXAnnEvidence getNXAnnEvidenceById(long id) {
        return this.nxAnnEvidenceService.getNXAnnEvidenceById(id);
    }

    //annotation isoform annotation
    public void saveNXIsoFormAnn(NXIsoFormAnn nxIsoFormAnn) {
        this.nxIsoFormAnnService.saveNXIsoFormAnn(nxIsoFormAnn);
    }

    public void updateNXIsoFormAnn(NXIsoFormAnn nxIsoFormAnn) {
        this.nxIsoFormAnnService.updateNXIsoFormAnn(nxIsoFormAnn);
    }

    public void deleteNXIsoFormAnn(NXIsoFormAnn nxIsoFormAnn) {
        this.nxIsoFormAnnService.deleteNXIsoFormAnn(nxIsoFormAnn);
    }

    public void deleteNXIsoFormAnnById(long id) {
        this.nxIsoFormAnnService.deleteNXIsoFormAnnById(id);
    }

    public NXIsoFormAnn getNXIsoFormAnnById(long id) {
        return this.nxIsoFormAnnService.getNXIsoFormAnnById(id);
    }

    private DSVersion createDSVersionByDbsChrom(ChromType chromType, DbAcType dbAcType, Date importedTime, String fileName, String timeToken) {
        //check the current  tpbversion
        DSVersion latestDSVersion = this.getCurrentDSVersionByChromDbs(dbAcType, chromType);

        if (latestDSVersion == null) {
            latestDSVersion = new DSVersion();
            latestDSVersion.setCreatedTime(importedTime);
            latestDSVersion.setDbSource(dbAcType.type());
            latestDSVersion.setChromosome(chromType.chm());
            latestDSVersion.setFileName(fileName);
            latestDSVersion.setTimestampToken(timeToken);
            latestDSVersion.setVersionNo(1);

            this.saveDSVersion(latestDSVersion);
            return latestDSVersion;
        } else {
            int latestVersionNum = latestDSVersion.getVersionNo();
            DSVersion currentDSVersion = new DSVersion();
            currentDSVersion.setCreatedTime(importedTime);
            currentDSVersion.setDbSource(dbAcType.type());
            currentDSVersion.setChromosome(chromType.chm());
            currentDSVersion.setFileName(fileName);
            currentDSVersion.setTimestampToken(timeToken);
            currentDSVersion.setVersionNo(latestVersionNum + 1);
            this.saveDSVersion(currentDSVersion);
            return currentDSVersion;
        }
    }

    public void saveNextProtDataEntryByChromosome(ChromType chromType, List<NXEntryBean> nxEntryBeans, Date importedTime, String fileName, String timeToken) {

        logger.info("TPB is starting to save the nextprot data.");
        //create new nextprot DSVersion
        DSVersion nxDSVersion = createDSVersionByDbsChrom(chromType, DbAcType.NextProt, importedTime, fileName, timeToken);
        boolean evidenceSaved = false;
        for (NXEntryBean nxEntryBean : nxEntryBeans) {
            GeneBean geneBean = nxEntryBean.getGeneBean();
            AccessionBean identifiedAcBean = nxEntryBean.getIdentifiedAccessionBean();

            List<Accession> savedAcs = new ArrayList<Accession>();

            List<DbSourceAcEntryBean> nxDbSourceAcEntryBeans = nxEntryBean.getDbSourceAcEntryBeans();
            for (DbSourceAcEntryBean nxDbSourceAcEntryBean : nxDbSourceAcEntryBeans) {
                //DBSource
                DBSourceBean dbSourceBean = nxDbSourceAcEntryBean.getDbSourceBean();
                //persist DBSource
                DBSource dbSource = persistDBSource(dbSourceBean);

                //Accession
                AccessionBean accessionBean = nxDbSourceAcEntryBean.getAccessionBean();
                String acType = accessionBean.getAcType();
                String acid = accessionBean.getAccession();

                //persist AccessionType
                AccessionType accessionType = persistAccessionType(acType);
                //persist Accession
                Accession accession = persistAccession(dbSource, accessionBean, accessionType);
                //if this accession is not added
                if (!savedAcs.contains(accession)) {
                    savedAcs.add(accession);
                }
            }

            //start to save gene
            String dbName = nxEntryBean.getDbSourceName();
            DBSource dbSource = this.getDBSourceByName(dbName);

            //persist Gene
            Gene nxGene = persistGene(geneBean, dbSource, savedAcs, importedTime);

            //get the identified accession
            String nextProtAc = identifiedAcBean.getAccession();
            String actType = identifiedAcBean.getAcType();
            Accession accession = this.getAccessionByAccessionAcType(nextProtAc, actType);

            NXPeTeOthEntryBean peTeOthEntryBean = nxEntryBean.getNxPeTeOthEntryBean();

            //PE OTH CUR Evidence
            PEEvidenceBean peOthCurEvidenceBean = peTeOthEntryBean.getNxPeOthEvidenceBean();
            if (peOthCurEvidenceBean != null) {
                //data type
                TPBDataTypeBean peOthCurDTypeBean = peOthCurEvidenceBean.getTpbDataTypeBean();
                String peOthTypeName = peOthCurDTypeBean.getDataType();
                TPBDataType peOThDType = this.getTPBDataTypeByTypeName(peOthTypeName);
                Evidence peOthCurEvidence = this.persistPEEvidence(nxGene, accession, peOThDType, peOthCurEvidenceBean, importedTime);
                evidenceSaved = true;

            }
            //save the te oth cur evidence
            TEEvidenceBean teOthCurEvidenceBean = peTeOthEntryBean.getNxTeOthEvidenceBean();
            if (teOthCurEvidenceBean != null) {
                TPBDataTypeBean teOthCurDTypeBean = teOthCurEvidenceBean.getTpbDataTypeBean();
                String teOthCurTypeName = teOthCurDTypeBean.getDataType();
                TPBDataType teOthCurDType = this.getTPBDataTypeByTypeName(teOthCurTypeName);

                TEEvidence teEvidence = new TEEvidence();
                teEvidence.setColorLevel(teOthCurEvidenceBean.getColorLevel());
                teEvidence.setHyperLink(teOthCurEvidenceBean.getHyperlink());
                teEvidence.setEvidenceValue(teOthCurEvidenceBean.getEvidenceValue());
                teEvidence.setUnique(true);
                TEEvidence teOthCurEvidence = this.persistTEEvidence(nxGene, accession, teOthCurDType, teEvidence, importedTime);
                evidenceSaved = true;
            }


            //get PE MS Ann and PE Anti Ann
            NXPeMsAntiEntryBean nxPeMsAntiEntryBean = nxEntryBean.getNxPeMsAntiEntryBean();

            //check if PE MS Ann, PE Antibody Ann Evidences are available
            if (nxPeMsAntiEntryBean != null) {
                List<PEEvidenceBean> nxPEMSAnnEvBeans = nxPeMsAntiEntryBean.getPeMsAnnEvidenceBeans();
                if (nxPEMSAnnEvBeans != null) {
                    for (PEEvidenceBean peMsAnEvidenceBean : nxPEMSAnnEvBeans) {
                        //data type
                        TPBDataTypeBean msTpbDTypeBean = peMsAnEvidenceBean.getTpbDataTypeBean();
                        String msType = msTpbDTypeBean.getDataType();
                        TPBDataType msTpbDType = this.getTPBDataTypeByTypeName(msType);
                        Evidence peMsAnn = this.persistPEEvidence(nxGene, accession, msTpbDType, peMsAnEvidenceBean, importedTime);
                        evidenceSaved = true;
                    }
                }

                PEEvidenceBean peAntiAnnEvidenceBean = nxPeMsAntiEntryBean.getPeAntiAnnEvidenceBean();
                if (peAntiAnnEvidenceBean != null) {
                    //data type
                    TPBDataTypeBean antiTpbDTypeBean = peAntiAnnEvidenceBean.getTpbDataTypeBean();
                    String antiType = antiTpbDTypeBean.getDataType();
                    TPBDataType antiTpbDType = this.getTPBDataTypeByTypeName(antiType);
                    Evidence peAntiAnn = this.persistPEEvidence(nxGene, accession, antiTpbDType, peAntiAnnEvidenceBean, importedTime);
                    evidenceSaved = true;
                }
            }
        }
        if (!evidenceSaved) {
            throw new DMException("no evidence from NextProt datasource is saved.");
        }
    }

    private Gene persistGeneForNx(GeneBean geneBean, DBSource dbSource, List<Accession> accessions, Date importedTime) {
        Gene nxGene = new Gene();
        nxGene.setDisplayName(geneBean.getDisplayName());
        nxGene.setChromosome(geneBean.getChromosome());
        nxGene.setEnsgAccession(geneBean.getEnsgAccession());
        nxGene.setStartPosition(geneBean.getStartPosition());
        nxGene.setEndPosition(geneBean.getEndPosition());
        nxGene.setBand(geneBean.getBand());
        nxGene.setStrand(geneBean.getStrand());
        nxGene.setDescription(geneBean.getDescription());

        //set a list of associated Accession
        nxGene.setAccessions(accessions);

        //set the identified DBSource
        nxGene.setDbSource(dbSource);

        //set the created time
        nxGene.setCreatedTime(importedTime);
        //set the last updated time
        nxGene.setLastUpdatedTime(importedTime);
        //just save this gene
        this.saveGene(nxGene);
        return nxGene;
    }


    private DBSource persistDBSource(DBSourceBean dbSourceBean) {
        String dbName = dbSourceBean.getDbName();
        DBSource dbSource = new DBSource();
        dbSource.setDbName(dbName);
        dbSource.setDbOwner(dbSourceBean.getDbOwner());
        dbSource.setDescription(dbSourceBean.getDescription());
        dbSource.setHyperLink(dbSourceBean.getHyperLink());
        dbSource.setPrimaryEvidences(dbSourceBean.isPrimaryEvidences());

        //try to find the DBSource from database
        DBSource foundDbSource = this.getDBSourceByName(dbName);
        if (foundDbSource == null) {
            this.saveDBSource(dbSource);
            foundDbSource = dbSource;
        } else {
            if (!foundDbSource.equals(dbSource)) {
                foundDbSource.setDbName(dbName);
                String dbOwner = dbSource.getDbOwner();
                if (StringUtils.isNotBlank(dbOwner)) {
                    foundDbSource.setDbOwner(dbOwner);
                }

                String hyperLink = dbSource.getHyperLink();
                if (StringUtils.isNotBlank(hyperLink)) {
                    foundDbSource.setHyperLink(hyperLink);
                }

                String desc = dbSource.getDescription();
                if (StringUtils.isNotBlank(desc)) {
                    foundDbSource.setDescription(desc);
                }

                boolean primaryEv = dbSource.isPrimaryEvidences();
                if (primaryEv) {
                    foundDbSource.setPrimaryEvidences(primaryEv);
                }
                this.mergeDBSource(foundDbSource);
            }
        }
        return foundDbSource;
    }

    private AccessionType persistAccessionType(String accessionType) {
        //accession type
        AccessionType foundAcType = this.getAccessionTypeByType(accessionType);
        if (foundAcType == null) {
            foundAcType = new AccessionType();
            foundAcType.setAcType(accessionType);
            this.saveAccessionType(foundAcType);
        }
        return foundAcType;
    }

    private Accession persistAccession(DBSource dbSource, AccessionBean accessionBean, AccessionType accessionType) {
        String accessionId = accessionBean.getAccession();
        Accession accession = new Accession();
        accession.setAccession(accessionId);
        accession.setDescription(accessionBean.getDescription());
        accession.setAcType(accessionType);
        accession.setDbSource(dbSource);
        Accession foundAc = this.getAccessionByAccessionAcType(accessionId, accessionType.getAcType());
        if (foundAc == null) {
            this.saveAccession(accession);
            foundAc = accession;
        } else {
            String desc = accessionBean.getDescription();
            if (StringUtils.isNotBlank(desc)) {
                foundAc.setDescription(accessionBean.getDescription());
            }
            foundAc.setAcType(accessionType);
            foundAc.setDbSource(dbSource);
            this.mergeAccession(foundAc);
        }
        return foundAc;
    }

    private PEEvidence persistPEEvidence(Gene gene, Accession accession, TPBDataType tpbDataType, PEEvidenceBean peEvidenceBean, Date importedTime) {
        PEEvidence peEvidence = new PEEvidence();
        peEvidence.setColorLevel(peEvidenceBean.getColorLevel());
        peEvidence.setEvidenceValue(peEvidenceBean.getEvidenceValue());
        peEvidence.setHyperLink(peEvidenceBean.getHyperlink());
        //set the created time
        peEvidence.setCreatedTime(importedTime);
        //set the last update time
        peEvidence.setLastUpdatedTime(importedTime);

        peEvidence.setIdentifiedAccession(accession);
        peEvidence.setTpbDataType(tpbDataType);
        peEvidence.setGene(gene);
        //save the pe evidence
        this.savePEEvidence(peEvidence);
        return peEvidence;
    }

    private void persistNXAnnoations(TLGene tlGene, Accession accession, List<NXAnnEntryBean> nxAnnEntryBeans) {
        if (nxAnnEntryBeans != null) {
            for (NXAnnEntryBean nxAnnEntryBean : nxAnnEntryBeans) {
                NXAnnotation nxAnnotation = copyFromNXAnnEntryBean(nxAnnEntryBean);

                System.out.println("=============> annotation category: " + nxAnnotation.getCategory());
                List<NXAnnEvidenceBean> nxAnnEvidenceBeans = nxAnnEntryBean.getNxAnnEvidenceBeans();

                List<NXIsoFormAnnBean> nxisoFormAnnBeans = nxAnnEntryBean.getNxisoFormAnnBeans();

                List<NXAnnEvidence> nxAnnEvidences = copyFromNXAnnEvidenceBeans(nxAnnEvidenceBeans, nxAnnotation);
                List<NXIsoFormAnn> nxIsoFormAnns = copyFromNXIsoFormAnnBeans(nxisoFormAnnBeans, nxAnnotation);

                if (nxAnnEvidences.size() > 0) {
                    nxAnnotation.setNxAnnEvidences(nxAnnEvidences);
                }
                if (nxIsoFormAnns.size() > 0) {
                    System.out.println("============= nxIsoFormAnns SIZE: : " + nxIsoFormAnns.size());
                    nxAnnotation.setNxIsoFormAnns(nxIsoFormAnns);
                }
                nxAnnotation.setIdentifiedAccession(accession);
                System.out.println("============= start to save annotation for the nextprot accession: " + accession.getAccession());
                this.saveNXAnnotation(nxAnnotation);
            }
        }
    }

    private NXAnnotation copyFromNXAnnEntryBean(NXAnnEntryBean nxAnnEntryBean) {
        NXAnnotation nxAnnotation = new NXAnnotation();

        if (nxAnnEntryBean != null) {
            String category = nxAnnEntryBean.getCategory();
            String qualityQualifier = nxAnnEntryBean.getQualityQualifier();
            String uniqueName = nxAnnEntryBean.getUniqueName();
            String desc = nxAnnEntryBean.getDescription();
            String cvName = nxAnnEntryBean.getCvName();
            String cvTermAc = nxAnnEntryBean.getCvTermAccession();

            if (StringUtils.isNotBlank(category)) {
                nxAnnotation.setCategory(category);
            }
            if (StringUtils.isNotBlank(qualityQualifier)) {
                nxAnnotation.setQualityQualifier(qualityQualifier);
            }
            if (StringUtils.isNotBlank(uniqueName)) {
                nxAnnotation.setUniqueName(uniqueName);
            }
            if (StringUtils.isNotBlank(cvName)) {
                nxAnnotation.setCvName(cvName);
            }
            if (StringUtils.isNotBlank(cvTermAc)) {
                nxAnnotation.setCvTermAccession(cvTermAc);
            }
            if (StringUtils.isNotBlank(desc)) {
                nxAnnotation.setDescription(desc);
            }
        }
        return nxAnnotation;
    }

    private List<NXAnnEvidence> copyFromNXAnnEvidenceBeans(List<NXAnnEvidenceBean> nxAnnEvidenceBeans, NXAnnotation nxAnnotation) {
        List<NXAnnEvidence> annEvidenceList = new ArrayList<NXAnnEvidence>();
        if (nxAnnEvidenceBeans != null) {
            for (NXAnnEvidenceBean nxAnnEvidenceBean : nxAnnEvidenceBeans) {

                NXAnnEvidence nxAnnEvidence = new NXAnnEvidence();
                boolean isNegative = nxAnnEvidenceBean.isNegative();
                String qualifierType = nxAnnEvidenceBean.getQualifierType();
                String resourceAssocType = nxAnnEvidenceBean.getResourceAssocType();
                int resourceRef = nxAnnEvidenceBean.getResourceRef();

                nxAnnEvidence.setNegative(isNegative);
                if (StringUtils.isNotBlank(qualifierType)) {
                    nxAnnEvidence.setQualifierType(qualifierType);
                }
                if (StringUtils.isNotBlank(resourceAssocType)) {
                    nxAnnEvidence.setResourceAssocType(resourceAssocType);
                }
                nxAnnEvidence.setResourceRef(resourceRef);
                nxAnnEvidence.setNxAnnotation(nxAnnotation);
                annEvidenceList.add(nxAnnEvidence);
            }
        }
        return annEvidenceList;
    }

    private List<NXIsoFormAnn> copyFromNXIsoFormAnnBeans(List<NXIsoFormAnnBean> nxisoFormAnnBeans, NXAnnotation nxAnnotation) {
        List<NXIsoFormAnn> nxIsoFormAnnList = new ArrayList<NXIsoFormAnn>();

        if (nxisoFormAnnBeans != null) {
            for (NXIsoFormAnnBean nxisoFormAnnBean : nxisoFormAnnBeans) {

                NXIsoFormAnn nxIsoFormAnn = new NXIsoFormAnn();

                String isoformRef = nxisoFormAnnBean.getIsoFormRef();
                int first = nxisoFormAnnBean.getFirstPosition();
                String firstStatus = nxisoFormAnnBean.getFirstStatus();
                int last = nxisoFormAnnBean.getLastPosition();
                String lastStatus = nxisoFormAnnBean.getLastStatus();

                if (StringUtils.isNotBlank(isoformRef)) {
                    nxIsoFormAnn.setIsoFormRef(isoformRef);
                }
                nxIsoFormAnn.setFirstPosition(first);

                if (StringUtils.isNotBlank(firstStatus)) {
                    nxIsoFormAnn.setFirstStatus(firstStatus);
                }

                nxIsoFormAnn.setLastPosition(last);

                if (StringUtils.isNotBlank(lastStatus)) {
                    nxIsoFormAnn.setLastStatus(lastStatus);
                }
                nxIsoFormAnn.setNxAnnotation(nxAnnotation);
                nxIsoFormAnnList.add(nxIsoFormAnn);
            }
        }
        return nxIsoFormAnnList;
    }

    public void saveGPMDataEntry(List<ChromType> chromTypes, List<GPMEntryBean> gpmEntryBeans, Date importedTime, String fileName, String timeToken) {
        logger.info("TPB is starting to save the gpm data.");
        //create new gmp  DSVersion  for chrom 7
        if (chromTypes.isEmpty()) {
            chromTypes = ChromType.allChroms();
        }
        for (ChromType chromType : chromTypes) {
            DSVersion gpmChromDSVersion = createDSVersionByDbsChrom(chromType, DbAcType.GPM, importedTime, fileName, timeToken);
        }
        //create new gmp  DSVersion  for chrom
        boolean evidenceSaved = false;

        for (GPMEntryBean gpmEntryBean : gpmEntryBeans) {
            GeneBean geneBean = gpmEntryBean.getGeneBean();
            String ensgAc = geneBean.getEnsgAccession();

            TPBGene foundTPBGene = this.getTPBGeneByEnsgAc(ensgAc);
            if (foundTPBGene != null) {
                String chrom = foundTPBGene.getChromosome();
                ChromType foundChromType = ChromType.fromType(chrom);
                //if the chromosome name is an unknown name, then we have to set it as other chromosome
                if (foundChromType.equals(ChromType.UNKNOWN)) {
                    foundChromType = ChromType.CHMOTHER;
                }
                //only focus on the required chromosome type
                if (chromTypes.contains(foundChromType)) {
                    //update the gene bean info from the found tpg bean
                    updateFromTPBGene(foundTPBGene, geneBean);

                    DBSourceBean gpmDbSource = gpmEntryBean.getPrimaryDbSourceBean();
                    AccessionBean identAcBean = gpmEntryBean.getIdentifiedAccessionBean();

                    List<DbSourceAcEntryBean> dbSourceAcEntryBeans = gpmEntryBean.getDbSourceAcEntryBeans();
                    //save the sessions and db sources
                    List<Accession> savedAcs = new ArrayList<Accession>();
                    for (DbSourceAcEntryBean gpmDbSourceAcEntryBean : dbSourceAcEntryBeans) {
                        //DBSource
                        DBSourceBean dbSourceBean = gpmDbSourceAcEntryBean.getDbSourceBean();
                        //persist DBSource
                        DBSource dbSource = persistDBSource(dbSourceBean);

                        //Accession
                        AccessionBean accessionBean = gpmDbSourceAcEntryBean.getAccessionBean();
                        String acType = accessionBean.getAcType();

                        //persist AccessionType
                        AccessionType accessionType = persistAccessionType(acType);
                        //persist Accession
                        Accession accession = persistAccession(dbSource, accessionBean, accessionType);
                        //if this accession is not added
                        if (!savedAcs.contains(accession)) {
                            savedAcs.add(accession);
                        }
                    }
                    //persist the GPM Db source bean
                    DBSource dbSource = persistDBSource(gpmDbSource);
                    //persist Gene
                    Gene gpmGene = persistGene(geneBean, dbSource, savedAcs, importedTime);

                    //get the identified accession
                    String identAc = identAcBean.getAccession();
                    String actType = identAcBean.getAcType();
                    Accession identifiedAccession = this.getAccessionByAccessionAcType(identAc, actType);

                    //PE PE MS Prob Evidence
                    PEEvidenceBean peMsProbEvidenceBean = gpmEntryBean.getPeMsProbEvidenceBean();
                    if (peMsProbEvidenceBean != null) {
                        //data type
                        TPBDataTypeBean peMsProbTypeBean = peMsProbEvidenceBean.getTpbDataTypeBean();
                        String peMsProbTypeName = peMsProbTypeBean.getDataType();
                        TPBDataType peMsProbType = this.getTPBDataTypeByTypeName(peMsProbTypeName);
                        Evidence peMsProbEvidence = this.persistPEEvidence(gpmGene, identifiedAccession, peMsProbType, peMsProbEvidenceBean, importedTime);
                        evidenceSaved = true;
                    }

                    //pe ms samoles evidence
                    PEEvidenceBean peMsSamplesEvidenceBean = gpmEntryBean.getPeMsSamplesEvidenceBean();
                    if (peMsSamplesEvidenceBean != null) {
                        //data type
                        TPBDataTypeBean peMsSamTpbTypeBean = peMsSamplesEvidenceBean.getTpbDataTypeBean();
                        String peMsSamTypeName = peMsSamTpbTypeBean.getDataType();
                        TPBDataType peMsSamType = this.getTPBDataTypeByTypeName(peMsSamTypeName);
                        Evidence peMsProbEvidence = this.persistPEEvidence(gpmGene, identifiedAccession, peMsSamType, peMsSamplesEvidenceBean, importedTime);
                        evidenceSaved = true;
                    }
                }
            }
        }

        //if no evidence saved, just make sure not save the ds version, genes, accession and db sources, and auto rollback
        if (!evidenceSaved) {
            throw new DMException("no evidence is saved");
        }
    }

    private void updateFromTPBGene(TPBGene tpbGene, GeneBean geneBean) {
        if (tpbGene != null) {
            //check the gene symbol name in the GeneBean, if it's empty, we have to set it as Unknown name first, then do updating later from the tpb gene.
            String geneSymbol = geneBean.getDisplayName();
            if (StringUtils.isBlank(geneSymbol)) {
                geneBean.setDisplayName(NameType.UNKNOWN.cn());
            }

            String geneName = tpbGene.getGeneName();
            //if the geneName is not blank from the tpb gene, we just copy the gene name
            if (StringUtils.isNotBlank(geneName)) {
                geneBean.setDisplayName(geneName);
            }

            String chromosome = tpbGene.getChromosome();
            if (StringUtils.isNotBlank(chromosome)) {
                geneBean.setChromosome(chromosome);
            }

            String desc = tpbGene.getDescription();
            if (StringUtils.isNotBlank(desc)) {
                geneBean.setDescription(desc);
            }

            String ensgAc = tpbGene.getEnsgAccession();
            if (StringUtils.isNotBlank(ensgAc)) {
                geneBean.setEnsgAccession(ensgAc);
            }

            long start = tpbGene.getStartPosition();
            if (start != 0) {
                geneBean.setStartPosition(start);
            }

            long stop = tpbGene.getEndPosition();
            if (stop != 0) {
                geneBean.setEndPosition(stop);
            }
            String band = tpbGene.getBand();
            if (StringUtils.isNotBlank(band)) {
                geneBean.setBand(band);
            }

            String strand = tpbGene.getStrand();
            if (StringUtils.isNotBlank(strand)) {
                geneBean.setStrand(strand);
            }
        }
    }


    //Persist the gene (new or update)
    private Gene persistGene(GeneBean geneBean, DBSource dbSource, List<Accession> accessions, Date importedTime) {
        Gene foundGene = null;
        String ensgAg = geneBean.getEnsgAccession();
        if (StringUtils.isNotBlank(ensgAg)) {
            foundGene = getGeneByEnsgAndDbVersion(ensgAg, dbSource.getDbName(), importedTime);
        }

        if (foundGene == null) {
            foundGene = new Gene();
            foundGene.setDisplayName(geneBean.getDisplayName());
            foundGene.setChromosome(geneBean.getChromosome());
            foundGene.setEnsgAccession(geneBean.getEnsgAccession());
            foundGene.setStartPosition(geneBean.getStartPosition());
            foundGene.setEndPosition(geneBean.getEndPosition());
            foundGene.setBand(geneBean.getBand());
            foundGene.setStrand(geneBean.getStrand());
            foundGene.setDescription(geneBean.getDescription());
            //set a list of associated Accession
            foundGene.setAccessions(accessions);
            //set the identified DBSource
            foundGene.setDbSource(dbSource);
            //set the created time
            foundGene.setCreatedTime(importedTime);
            //set the last updated time
            foundGene.setLastUpdatedTime(importedTime);
            //just save this gene
            this.saveGene(foundGene);
        } else {
            List<Accession> foundGeneAcs = this.getAllAssociatedAccessionsByGeneId(foundGene.getId());
            if (foundGeneAcs != null) {
                for (Accession ac : foundGeneAcs) {
                    if (!accessions.contains(ac)) {
                        accessions.add(ac);
                    }
                }
            }
            //set a list of associated Accession
            foundGene.setAccessions(accessions);
            //set the identified DBSource
            foundGene.setDbSource(dbSource);
            //set the created time
            foundGene.setCreatedTime(importedTime);
            //set the last updated time
            foundGene.setLastUpdatedTime(importedTime);
            this.mergeGene(foundGene);
        }
        return foundGene;
    }

    @Override
    public void saveProteomeCentralData(List<ChromType> chromTypes, GPMDbBean gpmDbBean, String fileName, Date importedTime) {

        //create new gmp  DSVersion
        if (chromTypes.isEmpty()) {
            chromTypes = ChromType.allChroms();
        }

        String timetoken = gpmDbBean.getReleaseToken();
        GPMDbType importedGpmDbType = gpmDbBean.getGpmDbType();
        logger.info("TPB is starting to save the proteome central - GPM " + importedGpmDbType.type() + " data.");

        DbAcType importedDbAcType = null;
        if (importedGpmDbType.equals(GPMDbType.GPMDB_PSYT)) {
            importedDbAcType = DbAcType.GPMPSYT;
        } else if (importedGpmDbType.equals(GPMDbType.GPMDB_LYS)) {
            importedDbAcType = DbAcType.GPMLYS;
        } else if (importedGpmDbType.equals(GPMDbType.GPMDB_NTA)) {
            importedDbAcType = DbAcType.GPMNTA;
        }
        //if the gpm db is unknow or dbactype is null. just return
        if (importedDbAcType == null || importedGpmDbType.equals(GPMDbType.GPMDB_UKNOWN)) {
            return;
        }

        boolean alreadyImported = this.checkUpToDate(importedDbAcType, null, fileName, timetoken);
        //if this file is not imported, then we start to save it into database
        if (!alreadyImported) {
            //create new gmp  DSVersion  for chrom
            for (ChromType chromType : chromTypes) {
                createDSVersionByDbsChrom(chromType, importedDbAcType, importedTime, fileName, timetoken);
            }
            //save evidence flag, if no evidence is save, then it will throw exception to roll back the gpm datasource version
            boolean evidenceSaved = false;
            List<GPMDbEntryBean> gpmDbEntryBeans = gpmDbBean.getPgmDbEntryBeans();
            for (GPMDbEntryBean gpmDbEntryBean : gpmDbEntryBeans) {
                GeneBean geneBean = gpmDbEntryBean.getGeneBean();
                String ensgAc = geneBean.getEnsgAccession();
                //only gene access is not blank
                if (StringUtils.isNotBlank(ensgAc)) {

                    String chrom = geneBean.getChromosome();
                    ChromType currentChromType = ChromType.fromType(chrom);
                    //if it's an unknown chromosome type, we set it as an other chromosome type
                    if (currentChromType.equals(ChromType.UNKNOWN)) {
                        currentChromType = ChromType.CHMOTHER;
                    }

                    //only focus on the required chromosome type
                    if (chromTypes.contains(currentChromType)) {
                        TPBGene foundTPBGene = this.getTPBGeneByEnsgAc(ensgAc);

                        if (foundTPBGene != null) {
                            updateFromTPBGene(foundTPBGene, geneBean);
                        } else {
                            //just save the gene from Proteome Central datasource into tpb gene
                            saveProteomeCentralGeneToTPBGene(geneBean);
                        }

                        DBSourceBean proteomeCentralGPMDbSource = gpmDbEntryBean.getPrimaryDbSourceBean();
                        AccessionBean identAcBean = gpmDbEntryBean.getIdentifiedAccessionBean();

                        List<DbSourceAcEntryBean> dbSourceAcEntryBeans = gpmDbEntryBean.getDbSourceAcEntryBeans();

                        //save the sessions and db sources
                        List<Accession> savedAcs = new ArrayList<Accession>();
                        for (DbSourceAcEntryBean gpmDbSourceAcEntryBean : dbSourceAcEntryBeans) {
                            //DBSource
                            DBSourceBean dbSourceBean = gpmDbSourceAcEntryBean.getDbSourceBean();
                            //persist DBSource
                            DBSource dbSource = persistDBSource(dbSourceBean);

                            //Accession
                            AccessionBean accessionBean = gpmDbSourceAcEntryBean.getAccessionBean();
                            String acType = accessionBean.getAcType();

                            //persist AccessionType
                            AccessionType accessionType = persistAccessionType(acType);
                            //persist Accession
                            Accession accession = persistAccession(dbSource, accessionBean, accessionType);
                            //if this accession is not added
                            if (!savedAcs.contains(accession)) {
                                savedAcs.add(accession);
                            }
                        }

                        //persist the proteome Central GPM Db source bean
                        DBSource dbSource = persistDBSource(proteomeCentralGPMDbSource);
                        //persist Gene
                        Gene proteomeCentralGene = persistGene(geneBean, dbSource, savedAcs, importedTime);

                        //get the identified accession
                        String identAc = identAcBean.getAccession();
                        String actType = identAcBean.getAcType();
                        //find the identified accession first
                        Accession identifiedAccession = this.getAccessionByAccessionAcType(identAc, actType);

                        //Get all PTM Evidence from the PTMEvidenceBean list, then save it
                        List<PTMEvidenceBean> ptmEvidenceBeans = gpmDbEntryBean.getPtmEvidenceBeans();
                        for (PTMEvidenceBean ptmEvidenceBean : ptmEvidenceBeans) {
                            //data type
                            TPBDataTypeBean ptmDataTypeBean = ptmEvidenceBean.getTpbDataTypeBean();
                            String ptmDataTypeName = ptmDataTypeBean.getDataType();
                            TPBDataType ptmDataType = this.getTPBDataTypeByTypeName(ptmDataTypeName);
                            Evidence ptmEvidence = this.persistPTMEvidence(proteomeCentralGene, identifiedAccession, ptmDataType, ptmEvidenceBean, importedTime);
                            evidenceSaved = true;
                        }
                    }
                }
            }
            //if not evidence to be saved, we just make sure it will roll back for the DSVersion record.
            if (!evidenceSaved) {
                throw new DMException("no ptm evidence is saved");
            }
        }
    }

    //save the PTMEvidence
    private PTMEvidence persistPTMEvidence(Gene gene, Accession accession, TPBDataType tpbDataType, PTMEvidenceBean ptmEvidenceBean, Date importedTime) {
        PTMEvidence ptmEvidence = new PTMEvidence();
        ptmEvidence.setPos(ptmEvidenceBean.getPos());
        ptmEvidence.setColorLevel(ptmEvidenceBean.getColorLevel());
        ptmEvidence.setEvidenceValue(ptmEvidenceBean.getEvidenceValue());
        ptmEvidence.setHyperLink(ptmEvidenceBean.getHyperlink());
        //set the created time
        ptmEvidence.setCreatedTime(importedTime);
        //set the last update time
        ptmEvidence.setLastUpdatedTime(importedTime);

        ptmEvidence.setIdentifiedAccession(accession);
        ptmEvidence.setTpbDataType(tpbDataType);
        ptmEvidence.setGene(gene);
        //save the pe evidence
        this.savePTMEvidence(ptmEvidence);
        return ptmEvidence;
    }

    //save the gene from  Proteome CentralGPM  datasource into TPB Gene (tpb baseline gene table)
    private void saveProteomeCentralGeneToTPBGene(GeneBean geneBean) {

        String ensgAc = geneBean.getEnsgAccession();
        if (StringUtils.isNotBlank(ensgAc)) {
            TPBGene tpbGene = new TPBGene();
            tpbGene.setEnsgAccession(geneBean.getEnsgAccession());

            //check the gene name first, if it's empty, then we have to set it as an unknow gene symbol name
            String genename = geneBean.getDisplayName();
            if (StringUtils.isNotBlank(genename)) {
                tpbGene.setGeneName(genename);
            } else {
                tpbGene.setGeneName(NameType.UNKNOWN.cn());
            }

            String chromsome = geneBean.getChromosome();
            if (StringUtils.isNotBlank(chromsome)) {
                tpbGene.setChromosome(chromsome);
            } else {
                tpbGene.setChromosome(ChromType.UNKNOWN.chm());
            }


            long start = geneBean.getStartPosition();
            if (start != 0) {
                tpbGene.setStartPosition(start);
            }

            long stop = geneBean.getEndPosition();
            if (stop != 0) {
                tpbGene.setEndPosition(stop);
            }

            String band = geneBean.getBand();
            if (StringUtils.isNotBlank(band)) {
                tpbGene.setBand(band);
            }

            String strand = geneBean.getStrand();
            if (StringUtils.isNotBlank(strand)) {
                tpbGene.setStrand(strand);
            }

            String desc = geneBean.getDescription();
            if (StringUtils.isNotBlank(desc)) {
                tpbGene.setDescription(geneBean.getDescription());
            }
            this.saveTPBGene(tpbGene);
        }
    }

    public void saveHPADataEntry(List<ChromType> chromTypes, List<HPAEntryBean> hpaEntryBeans, Date importedTime, String fileName, String versionToken) {
        //create DSVersions for chromosome types
        logger.info("TPB is starting to save the hpa data.");
        if (chromTypes.isEmpty()) {
            chromTypes = ChromType.allChroms();
        }
        for (ChromType chromType : chromTypes) {
            DSVersion hpaChromDSVersion = createDSVersionByDbsChrom(chromType, DbAcType.HPA, importedTime, fileName, versionToken);
        }
        //create new hpa  DSVersion  for chrom
        boolean evidenceSaved = false;

        for (HPAEntryBean hpaEntryBean : hpaEntryBeans) {
            GeneBean geneBean = hpaEntryBean.getGeneBean();
            String ensgAc = geneBean.getEnsgAccession();

            TPBGene foundTPBGene = this.getTPBGeneByEnsgAc(ensgAc);
            if (foundTPBGene != null) {
                String chrom = foundTPBGene.getChromosome();
                ChromType foundChromType = ChromType.fromType(chrom);
                if (foundChromType.equals(ChromType.UNKNOWN)) {
                    foundChromType = ChromType.CHMOTHER;
                }
                //only focus on the required chromosome type
                if (chromTypes.contains(foundChromType)) {
                    //update the gene bean info from the found tpg bean
                    updateFromTPBGene(foundTPBGene, geneBean);

                    DBSourceBean hpaDbSourceBean = hpaEntryBean.getPrimaryDbSourceBean();
                    AccessionBean identAcBean = hpaEntryBean.getIdentifiedAccessionBean();

                    List<DbSourceAcEntryBean> dbSourceAcEntryBeans = hpaEntryBean.getDbSourceAcEntryBeans();

                    //save the sessions and db sources
                    List<Accession> savedAcs = new ArrayList<Accession>();
                    for (DbSourceAcEntryBean hpaDbSourceAcEntryBean : dbSourceAcEntryBeans) {
                        //DBSource
                        DBSourceBean dbSourceBean = hpaDbSourceAcEntryBean.getDbSourceBean();

                        //persist DBSource
                        DBSource dbSource = persistDBSource(dbSourceBean);

                        //Accession
                        AccessionBean accessionBean = hpaDbSourceAcEntryBean.getAccessionBean();
                        String acType = accessionBean.getAcType();

                        //persist AccessionType
                        AccessionType accessionType = persistAccessionType(acType);

                        //persist Accession
                        Accession accession = persistAccession(dbSource, accessionBean, accessionType);
                        //if this accession is not added
                        if (!savedAcs.contains(accession)) {
                            savedAcs.add(accession);
                        }
                    }

                    //persist the hpa Db source bean
                    DBSource hpaDbSource = persistDBSource(hpaDbSourceBean);

                    //persist Gene
                    Gene hpaGene = persistGene(geneBean, hpaDbSource, savedAcs, importedTime);

                    //get the identified accession
                    String identAc = identAcBean.getAccession();
                    String actType = identAcBean.getAcType();

                    Accession identifiedAccession = this.getAccessionByAccessionAcType(identAc, actType);

                    //PE_ANTI_IHC_NORM
                    List<PEEvidenceBean> peAntiIHCNormEvidenceBeans = hpaEntryBean.getPeAntiIHCNormEvidencesBeans();
                    for (PEEvidenceBean peAnitIhcNormEvidenceBean : peAntiIHCNormEvidenceBeans) {
                        //data type
                        TPBDataTypeBean antiDataTypeBean = peAnitIhcNormEvidenceBean.getTpbDataTypeBean();
                        String antiDataTypeName = antiDataTypeBean.getDataType();
                        TPBDataType antiType = this.getTPBDataTypeByTypeName(antiDataTypeName);
                        this.persistPEEvidence(hpaGene, identifiedAccession, antiType, peAnitIhcNormEvidenceBean, importedTime);
                        evidenceSaved = true;
                    }

                }
            } else {
                //no way to save this gene information
            }
        }

        //if no evidence saved, just make sure not save the ds version, genes, accession and db sources, and auto rollback
        if (!evidenceSaved) {
            throw new DMException("no evidence is saved");
        }
    }


    //TLGene

    /**
     * {@inheritDoc}
     */
    public TLGene getTLGeneById(long id) {
        return this.tlGeneService.getTLGeneById(id);
    }

    /**
     * {@inheritDoc}
     */
    public void saveTLGene(TLGene tlGene) {
        this.tlGeneService.saveTLGene(tlGene);
    }

    /**
     * {@inheritDoc}
     */
    public void mergeTLGene(TLGene tlGene) {
        this.tlGeneService.mergeTLGene(tlGene);
    }

    /**
     * {@inheritDoc}
     */
    public void updateTLGene(TLGene tlGene) {
        this.tlGeneService.updateTLGene(tlGene);
    }

    /**
     * {@inheritDoc}
     */
    public void deleteTLGene(TLGene tlGene) {
        this.tlGeneService.deleteTLGene(tlGene);
    }

    /**
     * {@inheritDoc}
     */
    public void deleteTLGeneById(long id) {
        this.tlGeneService.deleteTLGeneById(id);
    }

    //TrafficLight

    /**
     * {@inheritDoc}
     */
    public void saveTrafficLight(TrafficLight trafficLight) {
        this.tlService.saveTrafficLight(trafficLight);
    }

    /**
     * {@inheritDoc}
     */
    public void mergeTrafficLight(TrafficLight trafficLight) {
        this.tlService.mergeTrafficLight(trafficLight);
    }

    /**
     * {@inheritDoc}
     */
    public void updateTrafficLight(TrafficLight trafficLight) {
        this.tlService.updateTrafficLight(trafficLight);
    }

    /**
     * {@inheritDoc}
     */
    public void deleteTrafficLight(TrafficLight trafficLight) {
        this.tlService.deleteTrafficLight(trafficLight);
    }

    /**
     * {@inheritDoc}
     */
    public TrafficLight getTrafficLightById(long id) {
        return this.tlService.getTrafficLightById(id);
    }

    /**
     * {@inheritDoc}
     */
    public void deleteTLById(long id) {
        this.tlService.deleteTLById(id);
    }

    /**
     * {@inheritDoc}
     */
    public boolean createVersionTLByChrom(ChromType chromType, TLVersionTrack tlVersionTrack, Date createdTime) {

        boolean created = false;
        if (tlVersionTrack != null && chromType != null) {
            boolean tlExisted = this.checkTPBVersionAvailable(chromType, tlVersionTrack);
            if (tlExisted) {
                logger.info("The traffic light already existed.");
            } else {
                //create a traffic light tpb version first.
                TPBVersion tpbVersion = createTPBVersion(chromType, tlVersionTrack, createdTime);
                if (tlVersionTrack.isSingleDbs()) {
                    logger.info("It's single datasource, token : " + tlVersionTrack.getTrackToken());
                    created = createSingleDbsTLs(chromType, tlVersionTrack, tpbVersion);
                } else {
                    created = createCombinedDbsTLs(chromType, tlVersionTrack, tpbVersion);
                }
                //if no traffic light was created, then throw exception to roll back the tpbversion
                if (!created) {
                    throw new DMException("no traffice light was created");
                }
            }
        }
        return created;
    }

    private boolean createSingleDbsTLs(ChromType chromType, TLVersionTrack tlVersionTrack, TPBVersion tpbVersion) {
        //create or update the traffic light based on the evidences from NextProt datasource
        boolean nxDsIncluded = tlVersionTrack.isNxDsIncluded();
        DSVersion nxDSVersion = tlVersionTrack.getNxDsVersion();
        if (nxDsIncluded && nxDSVersion != null) {
            logger.info("The nextprot datasource is included. and the nextprot version : " + nxDSVersion.getVersionNo());
            createTrafficLights(chromType, tpbVersion, nxDSVersion);
        }

        //create or update the traffic light based on the evidences from GPM datasource
        boolean gpmDsIncluded = tlVersionTrack.isGpmDsIncluded();
        if (gpmDsIncluded) {
            logger.info("The gpm datasource is included.");
            DSVersion gpmDSVersion = tlVersionTrack.getGpmDsVersion();

            if (gpmDSVersion != null) {
                logger.info("The gpm version : " + gpmDSVersion.getVersionNo());
                createTrafficLights(chromType, tpbVersion, gpmDSVersion);
            }

            //create or update the traffic light based on the evidences from GPM PSYT datasource
            DSVersion gpmPstyDsVersion = tlVersionTrack.getGpmPstyDsVersion();
            if (gpmPstyDsVersion != null) {
                logger.info("The gpm psty datasource version : " + gpmPstyDsVersion.getVersionNo());
                createTrafficLights(chromType, tpbVersion, gpmPstyDsVersion);
            }

            //create or update the traffic light based on the evidences from GPM LYS datasource
            DSVersion gpmLysDsVersion = tlVersionTrack.getGpmLysDsVersion();
            if (gpmLysDsVersion != null) {
                logger.info("The gpm lys datasource version : " + gpmLysDsVersion.getVersionNo());
                createTrafficLights(chromType, tpbVersion, gpmLysDsVersion);
            }

            //create or update the traffic light based on the evidences from GPM NTA datasource
            DSVersion gpmNtaDsVersion = tlVersionTrack.getGpmNtaDsVersion();
            if (gpmNtaDsVersion != null) {
                logger.info("The gpm nta datasource version : " + gpmNtaDsVersion.getVersionNo());
                createTrafficLights(chromType, tpbVersion, gpmNtaDsVersion);
            }
        }

        //create or update the traffic light based on the evidences from HPA datasource
        boolean phaDsIncluded = tlVersionTrack.isHpaDsIncluded();

        DSVersion hpaDSVersion = tlVersionTrack.getHpaDsVersion();
        if (phaDsIncluded && hpaDSVersion != null) {
            logger.info("The hpa datasource is included. and the hpa version : " + hpaDSVersion.getVersionNo());
            createTrafficLights(chromType, tpbVersion, hpaDSVersion);
        }

        //create or update the traffic light based on the evidences from the Barcode datasource
        boolean bcDsIncluded = tlVersionTrack.isBcDsIncluded();
        if (bcDsIncluded) {
            DSVersion bcHgu133aDsVersion = tlVersionTrack.getBcHgu133aDsVersion();
            if (bcHgu133aDsVersion != null) {
                logger.info("The barcode datasource is included. and the barcode hgu133a version : " + bcHgu133aDsVersion.getVersionNo());
                createTrafficLights(chromType, tpbVersion, bcHgu133aDsVersion);
            }
            DSVersion bcHgu133p2DsVersion = tlVersionTrack.getBcHgu133p2DsVersion();
            if (bcHgu133p2DsVersion != null) {
                logger.info("The barcode datasource is included. and the barcode hgu133plus2 version : " + bcHgu133p2DsVersion.getVersionNo());
                createTrafficLights(chromType, tpbVersion, bcHgu133p2DsVersion);
            }
        }
        return true;
    }

    private boolean createCombinedDbsTLs(ChromType chromType, TLVersionTrack tlVersionTrack, TPBVersion tpbVersion) {

        int[] twoTokens = tlVersionTrack.separateTokenAsTwo();
        if (twoTokens.length == 1) {
            return createSingleDbsTLs(chromType, tlVersionTrack, tpbVersion);
        }

        logger.info("It's two combined datasource, token : " + tlVersionTrack.getTrackToken());

        TPBVersion firstTokenTpbVersion = this.getCurrentVersion(chromType, twoTokens[0]);
        TPBVersion secondTokenTpbVersion = this.getCurrentVersion(chromType, twoTokens[1]);
        //if one of them is unavailable, then just return fals to indicated the combined datasources are incorrect.
        if (firstTokenTpbVersion == null || secondTokenTpbVersion == null) {
            return false;
        }

        logger.info("It's two combined datasource, the first token : " + firstTokenTpbVersion.getTrackToken() + " version id: " + firstTokenTpbVersion.getId());
        logger.info("It's two combined datasource, the second token : " + secondTokenTpbVersion.getTrackToken() + " version id: " + secondTokenTpbVersion.getId());

        //first tpb token traffice lights
        List<TrafficLight> firstTokenTls = this.getTrafficLightsByChromAndTPBVersion(chromType, firstTokenTpbVersion);
        int firstTokenTLSize = firstTokenTls.size();
        logger.info("The total size of first token tl is : " + firstTokenTLSize);

        //copy the first token traffic lights as a new verion traffic lights, then merge the second
        int firstCounter = 0;
        for (TrafficLight tl : firstTokenTls) {
            boolean copied = copyAsNewTLFromPrevTL(tl, tpbVersion);
            if (copied) {
                firstCounter++;
            }
        }
        //if not copy the same size  as the size of the first token traffic lights, then it will return false.
        if (firstCounter != firstTokenTLSize) {
            return false;
        } else {
            logger.info("The total size of first token traffic lights have been copied");
        }


        //second token traffic lights
        List<TrafficLight> secondTokenTls = this.getTrafficLightsByChromAndTPBVersion(chromType, secondTokenTpbVersion);
        int secondTokenTLSize = secondTokenTls.size();
        logger.info("The total size of second token tl is : " + secondTokenTLSize);
        int secondCounter = 0;

        for (TrafficLight tl : secondTokenTls) {
            TLGene tlGene = tl.getTlGene();
            String ensgAc = tlGene.getEnsgAccession();
            TrafficLight existedTl = null;
            if (StringUtils.isNotBlank(ensgAc)) {
                existedTl = this.getTLByChromEnsemblAcVersionToken(chromType, ensgAc, tpbVersion.getId(), tpbVersion.getTrackToken());
            }
            //if no traffic existed in the current tpbversion, then we copy this traffic light as new tl and append it into the version
            if (existedTl == null) {
                boolean copied = copyAsNewTLFromPrevTL(tl, tpbVersion);
                if (copied) {
                    secondCounter++;
                }
            } else {
                boolean merged = mergeTwoTrafficLights(existedTl, tl);
                if (merged) {
                    secondCounter++;
                }
            }
        }
        //if not copy the same size  as the size of the second token traffic lights, then it will return false.
        if (secondCounter != secondTokenTLSize) {
            return false;
        } else {
            logger.info("The total size of second token traffic lights have been copied");
        }
        return true;
    }


    private boolean copyAsNewTLFromPrevTL(TrafficLight prevTl, TPBVersion tpbVersion) {
        if (prevTl == null || tpbVersion == null) {
            return false;
        }
        TrafficLight tlNew = new TrafficLight();
        //get a default black color first
        TLColor blackColor = this.getTLColorByColorLevel(ColorType.BLACK.color());

        //Total 24 types at this moment
        //PE
        TLColor prevPeColor = prevTl.getTlPEColor();
        if (prevPeColor == null) {
            tlNew.setTlPEColor(blackColor);
        } else {
            tlNew.setTlPEColor(prevPeColor);
        }

        //PE MS
        TLColor prevPeMsColor = prevTl.getTlPEMSColor();
        if (prevPeMsColor == null) {
            tlNew.setTlPEMSColor(blackColor);
        } else {
            tlNew.setTlPEMSColor(prevPeMsColor);
        }

        //PE MS ANN
        TLColor prevPeMsAnnColor = prevTl.getTlPEMSANNColor();
        if (prevPeMsAnnColor == null) {
            tlNew.setTlPEMSANNColor(blackColor);
        } else {
            tlNew.setTlPEMSANNColor(prevPeMsAnnColor);
        }

        //PE MS PROB
        TLColor prevPeMsProbColor = prevTl.getTlPEMSPROBColor();
        if (prevPeMsProbColor == null) {
            tlNew.setTlPEMSPROBColor(blackColor);
        } else {
            tlNew.setTlPEMSPROBColor(prevPeMsProbColor);
        }

        //PE MS Samples
        TLColor prevPeMsSamColor = prevTl.getTlPEMSSAMColor();
        if (prevPeMsSamColor == null) {
            tlNew.setTlPEMSSAMColor(blackColor);
        } else {
            tlNew.setTlPEMSSAMColor(prevPeMsSamColor);
        }

        //PE ANTI
        TLColor prevPeAntiColor = prevTl.getTlPEANTIColor();
        if (prevPeAntiColor == null) {
            tlNew.setTlPEANTIColor(blackColor);
        } else {
            tlNew.setTlPEANTIColor(prevPeAntiColor);
        }

        //PE ANTI ANN
        TLColor prevPeAntiAnnColor = prevTl.getTlPEANTIANNColor();
        if (prevPeAntiAnnColor == null) {
            tlNew.setTlPEANTIANNColor(blackColor);
        } else {
            tlNew.setTlPEANTIANNColor(prevPeAntiAnnColor);
        }

        //PE ANTI IHC
        TLColor prevPeAntiIhcColor = prevTl.getTlPEANTIIHCColor();
        if (prevPeAntiIhcColor == null) {
            tlNew.setTlPEANTIIHCColor(blackColor);
        } else {
            tlNew.setTlPEANTIIHCColor(prevPeAntiIhcColor);
        }

        //PE ANTI ANN
        TLColor prevPeAntiIhcNormColor = prevTl.getTlPEANTIIHCNORMColor();
        if (prevPeAntiIhcNormColor == null) {
            tlNew.setTlPEANTIIHCNORMColor(blackColor);
        } else {
            tlNew.setTlPEANTIIHCNORMColor(prevPeAntiIhcNormColor);
        }

        //PE OTH
        TLColor prevPeOthColor = prevTl.getTlPEOTHColor();
        if (prevPeOthColor == null) {
            tlNew.setTlPEOTHColor(blackColor);
        } else {
            tlNew.setTlPEOTHColor(prevPeOthColor);
        }

        //PE OTH CUR
        TLColor prevPeOthCurColor = prevTl.getTlPEOTHCURColor();
        if (prevPeOthCurColor == null) {
            tlNew.setTlPEOTHCURColor(blackColor);
        } else {
            tlNew.setTlPEOTHCURColor(prevPeOthCurColor);
        }

        //PTM
        TLColor prevPtmColor = prevTl.getTlPTMColor();
        if (prevPtmColor == null) {
            tlNew.setTlPTMColor(blackColor);
        } else {
            tlNew.setTlPTMColor(prevPtmColor);
        }

        //PTM PHS
        TLColor prevPtmPhsColor = prevTl.getTlPTMPHSColor();
        if (prevPtmPhsColor == null) {
            tlNew.setTlPTMPHSColor(blackColor);
        } else {
            tlNew.setTlPTMPHSColor(prevPtmPhsColor);
        }

        //PTM PHS SER
        TLColor prevPtmPhsSerColor = prevTl.getTlPTMPHSSERColor();
        if (prevPtmPhsSerColor == null) {
            tlNew.setTlPTMPHSSERColor(blackColor);
        } else {
            tlNew.setTlPTMPHSSERColor(prevPtmPhsSerColor);
        }

        //PTM PHS THR
        TLColor prevPtmPhsThrColor = prevTl.getTlPTMPHSTHRColor();
        if (prevPtmPhsThrColor == null) {
            tlNew.setTlPTMPHSTHRColor(blackColor);
        } else {
            tlNew.setTlPTMPHSTHRColor(prevPtmPhsThrColor);
        }

        //PTM PHS TYR
        TLColor prevPtmPhsTyrColor = prevTl.getTlPTMPHSTYRColor();
        if (prevPtmPhsTyrColor == null) {
            tlNew.setTlPTMPHSTYRColor(blackColor);
        } else {
            tlNew.setTlPTMPHSTYRColor(prevPtmPhsTyrColor);
        }

        //PTM ACE
        TLColor prevPtmAceColor = prevTl.getTlPTMACEColor();
        if (prevPtmAceColor == null) {
            tlNew.setTlPTMACEColor(blackColor);
        } else {
            tlNew.setTlPTMACEColor(prevPtmAceColor);
        }

        //PTM ACE LYS
        TLColor prevPtmAceLysColor = prevTl.getTlPTMACELYSColor();
        if (prevPtmAceLysColor == null) {
            tlNew.setTlPTMACELYSColor(blackColor);
        } else {
            tlNew.setTlPTMACELYSColor(prevPtmAceLysColor);
        }

        //PTM ACE NTA
        TLColor prevPtmAceNtaColor = prevTl.getTlPTMACENTAColor();
        if (prevPtmAceNtaColor == null) {
            tlNew.setTlPTMACENTAColor(blackColor);
        } else {
            tlNew.setTlPTMACENTAColor(prevPtmAceNtaColor);
        }

        //TE
        TLColor prevTeColor = prevTl.getTlTEColor();
        if (prevTeColor == null) {
            tlNew.setTlTEColor(blackColor);
        } else {
            tlNew.setTlTEColor(prevTeColor);
        }

        //TE MA
        TLColor prevTeMaColor = prevTl.getTlTEMAColor();
        if (prevTeMaColor == null) {
            tlNew.setTlTEMAColor(blackColor);
        } else {
            tlNew.setTlTEMAColor(prevTeMaColor);
        }

        //TE MA PROP
        TLColor prevTeMaPropColor = prevTl.getTlTEMAPROPColor();
        if (prevTeMaPropColor == null) {
            tlNew.setTlTEMAPROPColor(blackColor);
        } else {
            tlNew.setTlTEMAPROPColor(prevTeMaPropColor);
        }

        //TE OTH
        TLColor prevTeOthColor = prevTl.getTlTEOTHColor();
        if (prevTeOthColor == null) {
            tlNew.setTlTEOTHColor(blackColor);
        } else {
            tlNew.setTlTEOTHColor(prevTeOthColor);
        }

        //TE OTH CUR
        TLColor prevTeOthCurColor = prevTl.getTlTEOTHCURColor();
        if (prevTeOthCurColor == null) {
            tlNew.setTlTEOTHCURColor(blackColor);
        } else {
            tlNew.setTlTEOTHCURColor(prevTeOthCurColor);
        }

        //set tpb version
        tlNew.setTpbVersion(tpbVersion);

        //get the previous traffic light TLGene
        TLGene tlGene = prevTl.getTlGene();
        TLGene tlGeneNew = copyAsNewTLGene(tlGene);

        //get all source genes for previous TLGene
        List<Gene> allGenes = this.getGenesByTLGeneId(tlGene.getId());
        //set all genes
        tlGeneNew.setGenes(allGenes);

        //save the TLGene
        this.saveTLGene(tlGeneNew);
        //set TLGene into traffic light
        tlNew.setTlGene(tlGeneNew);
        //save the traffic light
        this.saveTrafficLight(tlNew);
        return true;
    }

    private TLGene copyAsNewTLGene(TLGene tlGene) {
        if (tlGene == null) {

        }
        TLGene tlGeneNew = new TLGene();
        if (tlGene == null) {
            return tlGeneNew;
        }
        tlGeneNew.setDisplayName(tlGene.getDisplayName());
        tlGeneNew.setChromosome(tlGene.getChromosome());
        tlGeneNew.setEnsgAccession(tlGene.getEnsgAccession());
        tlGeneNew.setStartPosition(tlGene.getStartPosition());
        tlGeneNew.setEndPosition(tlGene.getEndPosition());
        tlGeneNew.setBand(tlGene.getBand());
        tlGeneNew.setStrand(tlGene.getStrand());
        tlGeneNew.setDescription(tlGene.getDescription());
        return tlGeneNew;
    }

    private boolean mergeTwoTrafficLights(TrafficLight existedTl, TrafficLight prevTl) {
        if (existedTl == null || prevTl == null) {
            return false;
        }

        //merge the color for all data type first
        //PE
        TLColor existedPeColor = existedTl.getTlPEColor();
        TLColor prevPeColor = prevTl.getTlPEColor();
        existedTl.setTlPEColor(getHigherTLColor(existedPeColor, prevPeColor));

        //PE MS
        TLColor existedPeMsColor = existedTl.getTlPEMSColor();
        TLColor prevPeMsColor = prevTl.getTlPEMSColor();
        existedTl.setTlPEMSColor(getHigherTLColor(existedPeMsColor, prevPeMsColor));

        //PE MS ANN
        TLColor existedPeMsAnnColor = existedTl.getTlPEMSANNColor();
        TLColor prevPeMsAnnColor = prevTl.getTlPEMSANNColor();
        existedTl.setTlPEMSANNColor(getHigherTLColor(existedPeMsAnnColor, prevPeMsAnnColor));

        //PE MS PROB
        TLColor existedPeMsProbColor = existedTl.getTlPEMSPROBColor();
        TLColor prevPeMsProbColor = prevTl.getTlPEMSPROBColor();
        existedTl.setTlPEMSPROBColor(getHigherTLColor(existedPeMsProbColor, prevPeMsProbColor));

        //PE MS Samples
        TLColor existedPeMsSamColor = existedTl.getTlPEMSSAMColor();
        TLColor prevPeMsSamColor = prevTl.getTlPEMSSAMColor();
        existedTl.setTlPEMSSAMColor(getHigherTLColor(existedPeMsSamColor, prevPeMsSamColor));

        //PE ANTI
        TLColor existedPeAntiColor = existedTl.getTlPEANTIColor();
        TLColor prevPeAntiColor = prevTl.getTlPEANTIColor();
        existedTl.setTlPEANTIColor(getHigherTLColor(existedPeAntiColor, prevPeAntiColor));

        //PE ANTI ANN
        TLColor existedPeAntiAnnColor = existedTl.getTlPEANTIANNColor();
        TLColor prevPeAntiAnnColor = prevTl.getTlPEANTIANNColor();
        existedTl.setTlPEANTIANNColor(getHigherTLColor(existedPeAntiAnnColor, prevPeAntiAnnColor));

        //PE ANTI IHC
        TLColor existedPeAntiIhcColor = existedTl.getTlPEANTIIHCColor();
        TLColor prevPeAntiIhcColor = prevTl.getTlPEANTIIHCColor();
        existedTl.setTlPEANTIIHCColor(getHigherTLColor(existedPeAntiIhcColor, prevPeAntiIhcColor));

        //PE ANTI ANN
        TLColor existedPeAntiIhcNormColor = existedTl.getTlPEANTIIHCNORMColor();
        TLColor prevPeAntiIhcNormColor = prevTl.getTlPEANTIIHCNORMColor();
        existedTl.setTlPEANTIIHCNORMColor(getHigherTLColor(existedPeAntiIhcNormColor, prevPeAntiIhcNormColor));

        //PE OTH
        TLColor existedPeOthColor = existedTl.getTlPEOTHColor();
        TLColor prevPeOthColor = prevTl.getTlPEOTHColor();
        existedTl.setTlPEOTHColor(getHigherTLColor(existedPeOthColor, prevPeOthColor));

        //PE OTH CUR
        TLColor existedPeOthCurColor = existedTl.getTlPEOTHCURColor();
        TLColor prevPeOthCurColor = prevTl.getTlPEOTHCURColor();
        existedTl.setTlPEOTHCURColor(getHigherTLColor(existedPeOthCurColor, prevPeOthCurColor));

        //PTM
        TLColor existedPtmColor = existedTl.getTlPTMColor();
        TLColor prevPtmColor = prevTl.getTlPTMColor();
        existedTl.setTlPTMColor(getHigherTLColor(existedPtmColor, prevPtmColor));

        //PTM PHS
        TLColor existedPtmPhsColor = existedTl.getTlPTMPHSColor();
        TLColor prevPtmPhsColor = prevTl.getTlPTMPHSColor();
        existedTl.setTlPTMPHSColor(getHigherTLColor(existedPtmPhsColor, prevPtmPhsColor));

        //PTM PHS SER
        TLColor existedPtmPhsSerColor = existedTl.getTlPTMPHSSERColor();
        TLColor prevPtmPhsSerColor = prevTl.getTlPTMPHSSERColor();
        existedTl.setTlPTMPHSSERColor(getHigherTLColor(existedPtmPhsSerColor, prevPtmPhsSerColor));

        //PTM PHS THR
        TLColor existedPtmPhsThrColor = existedTl.getTlPTMPHSTHRColor();
        TLColor prevPtmPhsThrColor = prevTl.getTlPTMPHSTHRColor();
        existedTl.setTlPTMPHSTHRColor(getHigherTLColor(existedPtmPhsThrColor, prevPtmPhsThrColor));

        //PTM PHS TYR
        TLColor existedPtmPhsTyrColor = existedTl.getTlPTMPHSTYRColor();
        TLColor prevPtmPhsTyrColor = prevTl.getTlPTMPHSTYRColor();
        existedTl.setTlPTMPHSTYRColor(getHigherTLColor(existedPtmPhsTyrColor, prevPtmPhsTyrColor));

        //PTM ACE
        TLColor existedPtmAceColor = existedTl.getTlPTMACEColor();
        TLColor prevPtmAceColor = prevTl.getTlPTMACEColor();
        existedTl.setTlPTMACEColor(getHigherTLColor(existedPtmAceColor, prevPtmAceColor));

        //PTM ACE LYS
        TLColor existedPtmAceLysColor = existedTl.getTlPTMACELYSColor();
        TLColor prevPtmAceLysColor = prevTl.getTlPTMACELYSColor();
        existedTl.setTlPTMACELYSColor(getHigherTLColor(existedPtmAceLysColor, prevPtmAceLysColor));

        //PTM ACE NTA
        TLColor existedPtmAceNtaColor = existedTl.getTlPTMACENTAColor();
        TLColor prevPtmAceNtaColor = prevTl.getTlPTMACENTAColor();
        existedTl.setTlPTMACENTAColor(getHigherTLColor(existedPtmAceNtaColor, prevPtmAceNtaColor));

        //TE
        TLColor existedTeColor = existedTl.getTlTEColor();
        TLColor prevTeColor = prevTl.getTlTEColor();
        existedTl.setTlTEColor(getHigherTLColor(existedTeColor, prevTeColor));

        //TE MA
        TLColor existedTeMaColor = existedTl.getTlTEMAColor();
        TLColor prevTeMaColor = prevTl.getTlTEMAColor();
        existedTl.setTlTEMAColor(getHigherTLColor(existedTeMaColor, prevTeMaColor));

        //TE MA PROP
        TLColor existedTeMaPropColor = existedTl.getTlTEMAPROPColor();
        TLColor prevTeMaPropColor = prevTl.getTlTEMAPROPColor();
        existedTl.setTlTEMAPROPColor(getHigherTLColor(existedTeMaPropColor, prevTeMaPropColor));

        //TE OTH
        TLColor existedTeOthColor = existedTl.getTlTEOTHColor();
        TLColor prevTeOthColor = prevTl.getTlTEOTHColor();
        existedTl.setTlTEOTHColor(getHigherTLColor(existedTeOthColor, prevTeOthColor));

        //TE OTH CUR
        TLColor existedTeOthCurColor = existedTl.getTlTEOTHCURColor();
        TLColor prevTeOthCurColor = prevTl.getTlTEOTHCURColor();
        existedTl.setTlTEOTHCURColor(getHigherTLColor(existedTeOthCurColor, prevTeOthCurColor));

        //merge all genes
        //for existed current version TLGene
        TLGene existedTLGene = existedTl.getTlGene();
        //get all source genes for current existed traffic light TLGene
        List<Gene> existedAllGenes = this.getGenesByTLGeneId(existedTLGene.getId());
        //for previous traffic light TLGene
        TLGene prevTLGene = prevTl.getTlGene();
        //get all associated source genes for previous traffic light TLGene
        List<Gene> prevAllGenes = this.getGenesByTLGeneId(prevTLGene.getId());
        for (Gene gene : prevAllGenes) {
            if (!existedAllGenes.contains(gene)) {
                existedAllGenes.add(gene);
            }
        }

        //update all source genes into TLGene
        existedTLGene.setGenes(existedAllGenes);
        //merge TLGene
        this.mergeTLGene(existedTLGene);
        //set the TLGene into traffic light
        existedTl.setTlGene(existedTLGene);
        //merge TrafficLight
        this.mergeTrafficLight(existedTl);
        return true;
    }

    private TLColor getHigherTLColor(TLColor existedTLColor, TLColor prevTLColor) {

        //get a default black color first
        TLColor blackColor = this.getTLColorByColorLevel(ColorType.BLACK.color());
        //both TLColor are null, then return black color
        if (existedTLColor == null && prevTLColor == null) {
            return blackColor;
        }
        if (existedTLColor != null && prevTLColor == null) {
            return existedTLColor;
        }
        if (existedTLColor == null && prevTLColor != null) {
            return prevTLColor;
        }
        if (existedTLColor.getColorLevel() > prevTLColor.getColorLevel()) {
            return existedTLColor;
        } else {
            return prevTLColor;
        }
    }

    @Deprecated
    public boolean createVersionTLByChromType(ChromType chromType, TLVersionTrack tlVersionTrack, Date createdTime) {
        boolean created = false;
        if (tlVersionTrack != null) {
            //check this combination version is available in TPBVersion table or not?
            //if not, then create a combination traffic lights and create a tpb version
            //if the traffic lights are already created before, we just ignore this combination
            boolean tpbVersionExisted = this.checkTPBVersionAvailable(chromType, tlVersionTrack);
            if (!tpbVersionExisted) {
                //create a traffic light tpb version first.
                TPBVersion tpbVersion = createTPBVersion(chromType, tlVersionTrack, createdTime);

                //create or update the traffic light based on the evidences from NextProt datasource
                boolean nxDsIncluded = tlVersionTrack.isNxDsIncluded();
                DSVersion nxDSVersion = tlVersionTrack.getNxDsVersion();
                if (nxDsIncluded && nxDSVersion != null) {
                    createTrafficLights(chromType, tpbVersion, nxDSVersion);
                }

                //create or update the traffic light based on the evidences from GPM datasource
                boolean gpmDsIncluded = tlVersionTrack.isGpmDsIncluded();
                DSVersion gpmDSVersion = tlVersionTrack.getGpmDsVersion();
                if (gpmDsIncluded && gpmDSVersion != null) {
                    createTrafficLights(chromType, tpbVersion, gpmDSVersion);
                }

                //create or update the traffic light based on the evidences from GPM PSYT datasource
                DSVersion gpmPstyDsVersion = tlVersionTrack.getGpmPstyDsVersion();
                if (gpmDsIncluded && gpmPstyDsVersion != null) {
                    createTrafficLights(chromType, tpbVersion, gpmPstyDsVersion);
                }

                //create or update the traffic light based on the evidences from GPM LYS datasource
                DSVersion gpmLysDsVersion = tlVersionTrack.getGpmLysDsVersion();
                if (gpmDsIncluded && gpmLysDsVersion != null) {
                    createTrafficLights(chromType, tpbVersion, gpmLysDsVersion);
                }

                //create or update the traffic light based on the evidences from GPM NTA datasource
                DSVersion gpmNtaDsVersion = tlVersionTrack.getGpmNtaDsVersion();
                if (gpmDsIncluded && gpmNtaDsVersion != null) {
                    createTrafficLights(chromType, tpbVersion, gpmNtaDsVersion);
                }

                //create or update the traffic light based on the evidences from HPA datasource
                boolean phaDsIncluded = tlVersionTrack.isHpaDsIncluded();
                DSVersion hpaDSVersion = tlVersionTrack.getHpaDsVersion();
                if (phaDsIncluded && hpaDSVersion != null) {
                    createTrafficLights(chromType, tpbVersion, hpaDSVersion);
                }

                //create or update the traffic light based on the evidences from the Barcode datasource
                boolean bcDsIncluded = tlVersionTrack.isBcDsIncluded();

                if (bcDsIncluded) {
                    DSVersion bcHgu133aDsVersion = tlVersionTrack.getBcHgu133aDsVersion();
                    if (bcHgu133aDsVersion != null) {
                        System.out.println("========= barcode ds is included. and barcode hgu 133a ds version : " + bcHgu133aDsVersion.getVersionNo());
                        createTrafficLights(chromType, tpbVersion, bcHgu133aDsVersion);
                    }
                    DSVersion bcHgu133p2DsVersion = tlVersionTrack.getBcHgu133p2DsVersion();
                    if (bcHgu133p2DsVersion != null) {
                        System.out.println("========= barcode ds is included. and barcode hgu 133plus2 ds version : " + bcHgu133p2DsVersion.getVersionNo());
                        createTrafficLights(chromType, tpbVersion, bcHgu133p2DsVersion);
                    }
                }
                created = true;
                logger.info("The traffic light is completed in the Traffic Light Service.");
            } else {
                logger.info("The traffic light already existed.");
            }
        }
        return created;
    }

    private void createTrafficLights(ChromType chromType, TPBVersion tpbVersion, DSVersion dsVersion) {
        TLColor tlGreenColor = this.getTLColorByColorLevel(ColorType.GREEN.color());
        TLColor tlYellowColor = this.getTLColorByColorLevel(ColorType.YELLOW.color());
        TLColor tlRedColor = this.getTLColorByColorLevel(ColorType.RED.color());
        TLColor tlBlackColor = this.getTLColorByColorLevel(ColorType.BLACK.color());

        if (dsVersion != null) {
            String dbSource = dsVersion.getDbSource();
            DbAcType dataSourceType = DbAcType.fromType(dbSource);

            Date dsVersionTime = dsVersion.getCreatedTime();
            System.out.println("============== data source type : " + dbSource + "  datasource version time : " + dsVersionTime);
            //get all genes based on a datasource type and datasource version time
            List<Gene> genes = this.getGenesByDBSChromVersion(dataSourceType, chromType, dsVersionTime);

            System.out.println("=================== total gene size : " + genes.size());
            for (Gene gene : genes) {
                String ensgAc = gene.getEnsgAccession();

                TrafficLight trafficLight = null;
                if (StringUtils.isNotBlank(ensgAc)) {
                    trafficLight = this.getTLByChromEnsemblAcVersionToken(chromType, ensgAc, tpbVersion.getId(), tpbVersion.getTrackToken());
                }
                //here we just recreate a new traffic light based on a traffic light which we got from db, it might be null or existed
                //we will check it later.
                trafficLight = generateTrafficLightEntity(trafficLight, gene.getId(), tlGreenColor, tlYellowColor, tlRedColor, tlBlackColor);

                //set tpb version
                trafficLight.setTpbVersion(tpbVersion);
                //if id is zero which means it's a new traffic
                if (trafficLight.getId() == 0) {
                    //new TrafficLight
                    TLGene tlGene = null;
                    //if the ensembl accession is not null, we create a TLGene based on a source gene and update it from our master TPBGene.
                    if (StringUtils.isNotBlank(ensgAc)) {
                        TPBGene tpbGene = this.getTPBGeneByEnsgAc(ensgAc);
                        if (tpbGene != null) {
                            tlGene = copyFromTPBGene(tpbGene);
                        } else {
                            tlGene = copyFromGene(gene);
                        }
                    } else {  //if ensembl accession is null, we just create a TLGene based on a source gene
                        tlGene = copyFromGene(gene);
                    }

                    //replace unknown chrom as other chrom type if any
                    tlGene = replaceUnknownChrom(tlGene);

                    //temp for store all the source genes for this TLGene
                    List<Gene> allGenes = new ArrayList<Gene>();

                    allGenes.add(gene);
                    //set all source genes
                    tlGene.setGenes(allGenes);
                    //save the TLGene
                    this.saveTLGene(tlGene);
                    //set the TLGene for traffic light
                    trafficLight.setTlGene(tlGene);
                    //save as a new TrafficLight
                    this.saveTrafficLight(trafficLight);

                } else {// a TrafficLight already existed, we just need updating it
                    //update TrafficLight
                    TLGene tlGene = trafficLight.getTlGene();
                    //ensembl accession is not null, then we try to update the TLGene info based on our master TPBGene
                    if (StringUtils.isNotBlank(ensgAc)) {
                        TPBGene tpbGene = this.getTPBGeneByEnsgAc(ensgAc);
                        if (tpbGene != null) {
                            long tlgid = tlGene.getId();
                            tlGene = copyFromTPBGene(tpbGene);
                            tlGene.setId(tlgid);
                        } else {
                            //this just try to replace the unknown gene name if available
                            tlGene = replaceUnknownGeneName(tlGene, gene);
                        }
                    } else {
                        //this just try to replace the unknown gene name if available
                        tlGene = replaceUnknownGeneName(tlGene, gene);
                    }

                    //replace unknown chrom as other chrom type if any
                    tlGene = replaceUnknownChrom(tlGene);

                    List<Gene> allGenes = this.getGenesByTLGeneId(tlGene.getId());

                    if (allGenes != null) {
                        if (!allGenes.contains(gene)) {
                            allGenes.add(gene);
                        }
                    } else {
                        allGenes = new ArrayList<Gene>();
                        allGenes.add(gene);
                    }
                    //set the all genes for this traffic light genes
                    tlGene.setGenes(allGenes);

                    //merge TLGene
                    this.mergeTLGene(tlGene);
                    //merge TrafficLight
                    this.mergeTrafficLight(trafficLight);
                }
            }
        }
    }

    private TLGene replaceUnknownChrom(TLGene tlGene) {
        String chromName = tlGene.getChromosome();
        ChromType chromType = ChromType.fromType(chromName);
        if (chromType.equals(ChromType.UNKNOWN)) {
            tlGene.setChromosome(ChromType.CHMOTHER.chm());
        }
        return tlGene;
    }

    private TLGene replaceUnknownGeneName(TLGene tlGene, Gene gene) {
        if (tlGene != null && gene != null) {
            String tlGeneDisplayName = tlGene.getDisplayName();
            String geneDisplayName = gene.getDisplayName();
            if (StringUtils.equals(tlGeneDisplayName, "Unknown")) {
                if (!StringUtils.equals(geneDisplayName, "Unknown")) {
                    tlGeneDisplayName = geneDisplayName;
                    tlGene.setDisplayName(tlGeneDisplayName);
                }
            }
        }
        return tlGene;
    }

    private TLGene copyFromGene(Gene gene) {
        TLGene tlGene = new TLGene();
        String displayName = gene.getDisplayName();
        if (StringUtils.isNotBlank(displayName)) {
            tlGene.setDisplayName(displayName);
        }

        String chromosome = gene.getChromosome();
        if (StringUtils.isNotBlank(chromosome)) {
            tlGene.setChromosome(chromosome);
        }

        String desc = gene.getDescription();
        if (StringUtils.isNotBlank(desc)) {
            tlGene.setDescription(desc);
        }

        String ensgAc = gene.getEnsgAccession();
        if (StringUtils.isNotBlank(ensgAc)) {
            tlGene.setEnsgAccession(ensgAc);
        }

        long start = gene.getStartPosition();
        if (start != 0) {
            tlGene.setStartPosition(start);
        }

        long stop = gene.getEndPosition();
        if (stop != 0) {
            tlGene.setEndPosition(stop);
        }

        String band = gene.getBand();
        if (StringUtils.isNotBlank(band)) {
            tlGene.setBand(band);
        }

        String strand = gene.getStrand();
        if (StringUtils.isNotBlank(strand)) {
            tlGene.setStrand(strand);
        }
        return tlGene;
    }

    private TLGene copyFromTPBGene(TPBGene tpbGene) {
        TLGene tlGene = new TLGene();

        String displayName = tpbGene.getGeneName();
        if (StringUtils.isNotBlank(displayName)) {
            tlGene.setDisplayName(displayName);
        }

        String chromosome = tpbGene.getChromosome();
        if (StringUtils.isNotBlank(chromosome)) {
            tlGene.setChromosome(chromosome);
        }

        String desc = tpbGene.getDescription();
        if (StringUtils.isNotBlank(desc)) {
            tlGene.setDescription(desc);
        }

        String ensgAc = tpbGene.getEnsgAccession();
        if (StringUtils.isNotBlank(ensgAc)) {
            tlGene.setEnsgAccession(ensgAc);
        }

        long start = tpbGene.getStartPosition();
        if (start != 0) {
            tlGene.setStartPosition(start);
        }

        long stop = tpbGene.getEndPosition();
        if (stop != 0) {
            tlGene.setEndPosition(stop);
        }

        String band = tpbGene.getBand();
        if (StringUtils.isNotBlank(band)) {
            tlGene.setBand(band);
        }

        String strand = tpbGene.getStrand();
        if (StringUtils.isNotBlank(strand)) {
            tlGene.setStrand(strand);
        }
        return tlGene;
    }

    private TrafficLight generateTrafficLightEntity(TrafficLight trafficLight, long sourceGeneId, TLColor tlGreenColor, TLColor tlYellowColor, TLColor tlRedColor, TLColor tlBlackColor) {
        if (trafficLight == null) {
            trafficLight = new TrafficLight();
        }
        //get all data types evidences first
        //PE
        PEEvidence peSum = this.getPESummaryByGeneAndType(sourceGeneId, DataType.PE);
        PEEvidence peMsSum = this.getPESummaryByGeneAndType(sourceGeneId, DataType.PE_MS);
        PEEvidence peMsAnnSum = this.getPESummaryByGeneAndType(sourceGeneId, DataType.PE_MS_ANN);
        PEEvidence peMsProbSum = this.getPESummaryByGeneAndType(sourceGeneId, DataType.PE_MS_PROB);
        PEEvidence peMsSamSum = this.getPESummaryByGeneAndType(sourceGeneId, DataType.PE_MS_SAM);
        PEEvidence peAntiSum = this.getPESummaryByGeneAndType(sourceGeneId, DataType.PE_ANTI);
        PEEvidence peAntiAnnSum = this.getPESummaryByGeneAndType(sourceGeneId, DataType.PE_ANTI_ANN);
        PEEvidence peAntiIhcSum = this.getPESummaryByGeneAndType(sourceGeneId, DataType.PE_ANTI_IHC);
        PEEvidence peAntiIhcNormSum = this.getPESummaryByGeneAndType(sourceGeneId, DataType.PE_ANTI_IHC_NORM);
        PEEvidence peOthSum = this.getPESummaryByGeneAndType(sourceGeneId, DataType.PE_OTH);
        PEEvidence peOthCurSum = this.getPESummaryByGeneAndType(sourceGeneId, DataType.PE_OTH_CUR);

        //PE
        TLColor peColor = generateTLEvColor(peSum, DataType.PE, trafficLight, tlGreenColor, tlYellowColor, tlRedColor, tlBlackColor);
        trafficLight.setTlPEColor(peColor);

        //PE MS
        TLColor peMsColor = generateTLEvColor(peMsSum, DataType.PE_MS, trafficLight, tlGreenColor, tlYellowColor, tlRedColor, tlBlackColor);
        trafficLight.setTlPEMSColor(peMsColor);

        //PE MS ANN
        TLColor peMsAnnColor = generateTLEvColor(peMsAnnSum, DataType.PE_MS_ANN, trafficLight, tlGreenColor, tlYellowColor, tlRedColor, tlBlackColor);
        trafficLight.setTlPEMSANNColor(peMsAnnColor);

        //PE MS PROB
        TLColor peMsProbColor = generateTLEvColor(peMsProbSum, DataType.PE_MS_PROB, trafficLight, tlGreenColor, tlYellowColor, tlRedColor, tlBlackColor);
        trafficLight.setTlPEMSPROBColor(peMsProbColor);

        //PE MS Samples
        TLColor peMsSamColor = generateTLEvColor(peMsSamSum, DataType.PE_MS_SAM, trafficLight, tlGreenColor, tlYellowColor, tlRedColor, tlBlackColor);
        trafficLight.setTlPEMSSAMColor(peMsSamColor);

        //PE ANTI
        TLColor peAntiColor = generateTLEvColor(peAntiSum, DataType.PE_ANTI, trafficLight, tlGreenColor, tlYellowColor, tlRedColor, tlBlackColor);
        trafficLight.setTlPEANTIColor(peAntiColor);

        //PE ANTI ANN
        TLColor peAntiAnnColor = generateTLEvColor(peAntiAnnSum, DataType.PE_ANTI_ANN, trafficLight, tlGreenColor, tlYellowColor, tlRedColor, tlBlackColor);
        trafficLight.setTlPEANTIANNColor(peAntiAnnColor);

        //PE ANTI IHC
        TLColor peAntiIhcColor = generateTLEvColor(peAntiIhcSum, DataType.PE_ANTI_IHC, trafficLight, tlGreenColor, tlYellowColor, tlRedColor, tlBlackColor);
        trafficLight.setTlPEANTIIHCColor(peAntiIhcColor);

        //PE ANTI ANN
        TLColor peAntiIhcNormColor = generateTLEvColor(peAntiIhcNormSum, DataType.PE_ANTI_IHC_NORM, trafficLight, tlGreenColor, tlYellowColor, tlRedColor, tlBlackColor);
        trafficLight.setTlPEANTIIHCNORMColor(peAntiIhcNormColor);

        //PE OTH
        TLColor peOthColor = generateTLEvColor(peOthSum, DataType.PE_OTH, trafficLight, tlGreenColor, tlYellowColor, tlRedColor, tlBlackColor);
        trafficLight.setTlPEOTHColor(peOthColor);

        //PE OTH CUR
        TLColor peOthCurColor = generateTLEvColor(peOthCurSum, DataType.PE_OTH_CUR, trafficLight, tlGreenColor, tlYellowColor, tlRedColor, tlBlackColor);
        trafficLight.setTlPEOTHCURColor(peOthCurColor);

        //PTM
        PTMEvidence ptmSum = this.getPTMSummaryByGeneAndType(sourceGeneId, DataType.PTM);
        PTMEvidence ptmPhsSum = this.getPTMSummaryByGeneAndType(sourceGeneId, DataType.PTM_PHS);
        PTMEvidence ptmPhsSerSum = this.getPTMSummaryByGeneAndType(sourceGeneId, DataType.PTM_PHS_SER);
        PTMEvidence ptmPhsThrSum = this.getPTMSummaryByGeneAndType(sourceGeneId, DataType.PTM_PHS_THR);
        PTMEvidence ptmPhsTyrSum = this.getPTMSummaryByGeneAndType(sourceGeneId, DataType.PTM_PHS_TYR);
        PTMEvidence ptmAceSum = this.getPTMSummaryByGeneAndType(sourceGeneId, DataType.PTM_ACE);
        PTMEvidence ptmAceLysSum = this.getPTMSummaryByGeneAndType(sourceGeneId, DataType.PTM_ACE_LYS);
        PTMEvidence ptmAceNtaSum = this.getPTMSummaryByGeneAndType(sourceGeneId, DataType.PTM_ACE_NTA);

        //PTM
        TLColor ptmColor = generateTLEvColor(ptmSum, DataType.PTM, trafficLight, tlGreenColor, tlYellowColor, tlRedColor, tlBlackColor);
        trafficLight.setTlPTMColor(ptmColor);

        //PTM PHS
        TLColor ptmPhsColor = generateTLEvColor(ptmPhsSum, DataType.PTM_PHS, trafficLight, tlGreenColor, tlYellowColor, tlRedColor, tlBlackColor);
        trafficLight.setTlPTMPHSColor(ptmPhsColor);

        //PTM PHS SER
        TLColor ptmPhsSerColor = generateTLEvColor(ptmPhsSerSum, DataType.PTM_PHS_SER, trafficLight, tlGreenColor, tlYellowColor, tlRedColor, tlBlackColor);
        trafficLight.setTlPTMPHSSERColor(ptmPhsSerColor);

        //PTM PHS THR
        TLColor ptmPhsThrColor = generateTLEvColor(ptmPhsThrSum, DataType.PTM_PHS_THR, trafficLight, tlGreenColor, tlYellowColor, tlRedColor, tlBlackColor);
        trafficLight.setTlPTMPHSTHRColor(ptmPhsThrColor);

        //PTM PHS TYR
        TLColor ptmPhsTyrColor = generateTLEvColor(ptmPhsTyrSum, DataType.PTM_PHS_TYR, trafficLight, tlGreenColor, tlYellowColor, tlRedColor, tlBlackColor);
        trafficLight.setTlPTMPHSTYRColor(ptmPhsTyrColor);

        //PTM ACE
        TLColor ptmAceColor = generateTLEvColor(ptmAceSum, DataType.PTM_ACE, trafficLight, tlGreenColor, tlYellowColor, tlRedColor, tlBlackColor);
        trafficLight.setTlPTMACEColor(ptmAceColor);

        //PTM ACE LYS
        TLColor ptmAceLysColor = generateTLEvColor(ptmAceLysSum, DataType.PTM_ACE_LYS, trafficLight, tlGreenColor, tlYellowColor, tlRedColor, tlBlackColor);
        trafficLight.setTlPTMACELYSColor(ptmAceLysColor);

        //PTM ACE NTA
        TLColor ptmAceNtaColor = generateTLEvColor(ptmAceNtaSum, DataType.PTM_ACE_NTA, trafficLight, tlGreenColor, tlYellowColor, tlRedColor, tlBlackColor);
        trafficLight.setTlPTMACENTAColor(ptmAceNtaColor);


        //TE
        TEEvidence teSum = this.getTESummaryByGeneAndType(sourceGeneId, DataType.TE);
        TEEvidence teMaSum = this.getTESummaryByGeneAndType(sourceGeneId, DataType.TE_MA);
        TEEvidence teMaPropSum = this.getTESummaryByGeneAndType(sourceGeneId, DataType.TE_MA_PROP);
        TEEvidence teOthSum = this.getTESummaryByGeneAndType(sourceGeneId, DataType.TE_OTH);
        TEEvidence teOthCurSum = this.getTESummaryByGeneAndType(sourceGeneId, DataType.TE_OTH_CUR);

        //TE
        TLColor teColor = generateTLEvColor(teSum, DataType.TE, trafficLight, tlGreenColor, tlYellowColor, tlRedColor, tlBlackColor);
        trafficLight.setTlTEColor(teColor);

        //TE MA
        TLColor teMaColor = generateTLEvColor(teMaSum, DataType.TE_MA, trafficLight, tlGreenColor, tlYellowColor, tlRedColor, tlBlackColor);
        trafficLight.setTlTEMAColor(teMaColor);

        //TE MA PROP
        TLColor teMaPropColor = generateTLEvColor(teMaPropSum, DataType.TE_MA_PROP, trafficLight, tlGreenColor, tlYellowColor, tlRedColor, tlBlackColor);
        trafficLight.setTlTEMAPROPColor(teMaPropColor);

        //TE OTH
        TLColor teOthColor = generateTLEvColor(teOthSum, DataType.TE_OTH, trafficLight, tlGreenColor, tlYellowColor, tlRedColor, tlBlackColor);
        trafficLight.setTlTEOTHColor(teOthColor);

        //TE OTH CUR
        TLColor teOthCurColor = generateTLEvColor(teOthCurSum, DataType.TE_OTH_CUR, trafficLight, tlGreenColor, tlYellowColor, tlRedColor, tlBlackColor);
        trafficLight.setTlTEOTHCURColor(teOthCurColor);
        return trafficLight;
    }

    private TLColor generateTLEvColor(Evidence evidence, DataType dataType, TrafficLight trafficLight, TLColor tlGreenColor, TLColor tlYellowColor, TLColor tlRedColor, TLColor tlBlackColor) {
        TLColor tlEvColor = tlBlackColor;

        if (evidence != null) {
            if (evidence.getColorLevel() == ColorType.GREEN.color()) {
                tlEvColor = tlGreenColor;
            } else if (evidence.getColorLevel() == ColorType.YELLOW.color()) {
                tlEvColor = tlYellowColor;
            } else if (evidence.getColorLevel() == ColorType.RED.color()) {
                tlEvColor = tlRedColor;
            } else {
                tlEvColor = tlBlackColor;
            }
        }
        //to see new color is higher than the existed one or not
        TLColor existedEvTLColor = trafficLight.getTLColorByDataType(dataType);
        if (existedEvTLColor == null) {
            return tlEvColor;
        } else {
            if (tlEvColor.getColorLevel() > existedEvTLColor.getColorLevel()) {
                return tlEvColor;
            } else {
                return existedEvTLColor;
            }
        }
    }

    private TPBVersion createTPBVersion(ChromType chromType, TLVersionTrack tlVersionTrack, Date createdTime) {
        TPBVersion tpbVersion = new TPBVersion();
        //set the chromosome type
        tpbVersion.setChromosome(chromType.chm());
        //TPBVersion time:
        tpbVersion.setCreatedTime(createdTime);
        int trackToken = tlVersionTrack.getTrackToken();
        tpbVersion.setTrackToken(trackToken);

        TPBVersion currentTPBVersion = this.getCurrentTPBVersionByChromTypeTrackToken(chromType, trackToken);
        //create a TPBVersion number
        int versionNo = 1;
        if (currentTPBVersion != null) {
            int currentVersionNo = currentTPBVersion.getVersionNo();
            versionNo = currentVersionNo + 1;
        }
        tpbVersion.setVersionNo(versionNo);

        DSVersion nxDSVersion = tlVersionTrack.getNxDsVersion();
        if (nxDSVersion != null) {
            tpbVersion.setNxVersion(nxDSVersion);
        }

        DSVersion gpmDSVersion = tlVersionTrack.getGpmDsVersion();
        if (gpmDSVersion != null) {
            tpbVersion.setGpmVersion(gpmDSVersion);
        }

        DSVersion gpmPstyVersion = tlVersionTrack.getGpmPstyDsVersion();
        if (gpmPstyVersion != null) {
            tpbVersion.setGpmPstyVersion(gpmPstyVersion);
        }

        DSVersion gpmLysVersion = tlVersionTrack.getGpmLysDsVersion();
        if (gpmLysVersion != null) {
            tpbVersion.setGpmLysVersion(gpmLysVersion);
        }

        DSVersion gpmNtaVersion = tlVersionTrack.getGpmNtaDsVersion();
        if (gpmNtaVersion != null) {
            tpbVersion.setGpmNtaVersion(gpmNtaVersion);
        }

        DSVersion hpaDSVersion = tlVersionTrack.getHpaDsVersion();
        if (hpaDSVersion != null) {
            tpbVersion.setHpaVersion(hpaDSVersion);
        }

        DSVersion bcHgu133aDSVersion = tlVersionTrack.getBcHgu133aDsVersion();
        if (bcHgu133aDSVersion != null) {
            tpbVersion.setBcHgu133aVersion(bcHgu133aDSVersion);
        }

        DSVersion bcHgu133p2DSVersion = tlVersionTrack.getBcHgu133p2DsVersion();
        if (bcHgu133p2DSVersion != null) {
            tpbVersion.setBcHgu133p2Version(bcHgu133p2DSVersion);
        }
        this.saveTPBVersion(tpbVersion);
        return tpbVersion;
    }

    /**
     * {@inheritDoc}
     */
    public TrafficLight getTLByChromEnsemblAcVersionToken(ChromType chromType, String ensgAc, long versionId, int trackToken) {
        return this.tlService.getTLByChromEnsemblAcVersionToken(chromType, ensgAc, versionId, trackToken);
    }

    /**
     * {@inheritDoc}
     */
    public Pagination<TrafficLight> getVersionTrafficLight(ChromType chromType, int trackToken, long versionId, int startPage, int recordsPerPage, OrderBy[] orderConds) {
        return this.tlService.getVersionTrafficLight(chromType, trackToken, versionId, startPage, recordsPerPage, orderConds);
    }

    /**
     * {@inheritDoc}
     */
    public Pagination<TrafficLight> getVersionTrafficLight(TLSearchBean tlSearchBean, int startPage, int recordsPerPage, OrderBy[] orderConds) {
        return this.tlService.getVersionTrafficLight(tlSearchBean, startPage, recordsPerPage, orderConds);
    }

    /**
     * {@inheritDoc}
     */
    public TLSumReporter getTLSumReporter(TLSearchBean tlSearchBean) {
        return this.tlService.getTLSumReporter(tlSearchBean);
    }

    /**
     * {@inheritDoc}
     */
    public List<TrafficLight> getTrafficLightsByChromAndTPBVersion(ChromType chromType, TPBVersion tpbVersion) {
        return this.tlService.getTrafficLightsByChromAndTPBVersion(chromType, tpbVersion);
    }

    /**
     * {@inheritDoc}
     */
    public TPBGene getTPBGeneById(long id) {
        return this.tpbGeneService.getTPBGeneById(id);
    }

    /**
     * {@inheritDoc}
     */
    public void saveTPBGene(TPBGene tpbGene) {
        this.tpbGeneService.saveTPBGene(tpbGene);
    }

    /**
     * {@inheritDoc}
     */
    public void mergeTPBGene(TPBGene tpbGene) {
        this.tpbGeneService.mergeTPBGene(tpbGene);
    }

    /**
     * {@inheritDoc}
     */
    public void updateTPBGene(TPBGene tpbGene) {
        this.tpbGeneService.updateTPBGene(tpbGene);
    }

    /**
     * {@inheritDoc}
     */
    public void deleteTPBGene(TPBGene tpbGene) {
        this.tpbGeneService.deleteTPBGene(tpbGene);
    }

    /**
     * {@inheritDoc}
     */
    public TPBGene getTPBGeneByEnsgAc(String ensgAccession) {
        return this.tpbGeneService.getTPBGeneByEnsgAc(ensgAccession);
    }

    /**
     * {@inheritDoc}
     */
    public void importTPBGenes(List<TPBGene> tpbGenes) {
        for (TPBGene tpbGene : tpbGenes) {
            String ensgAc = tpbGene.getEnsgAccession();
            if (StringUtils.isNotBlank(ensgAc)) {
                TPBGene existedTpbGene = this.getTPBGeneByEnsgAc(ensgAc);
                if (existedTpbGene != null) {
                    tpbGene.setId(existedTpbGene.getId());
                    this.mergeTPBGene(tpbGene);
                } else {
                    this.saveTPBGene(tpbGene);
                }
            }
        }
    }

    @Override
    public Probe getProbeById(long id) {
        return this.probeService.getProbeById(id);
    }

    @Override
    public void saveProbe(Probe probe) {
        this.probeService.saveProbe(probe);
    }

    @Override
    public void mergeProbe(Probe probe) {
        this.probeService.mergeProbe(probe);
    }

    @Override
    public void updateProbe(Probe probe) {
        this.probeService.updateProbe(probe);
    }

    @Override
    public void deleteProbe(Probe probe) {
        this.probeService.deleteProbe(probe);
    }

    @Override
    public Probe getProbeByProbeId(String probeId) {
        return this.probeService.getProbeByProbeId(probeId);
    }

    @Override
    public List<Probe> getProbesByGeneAccession(String ensgAccession) {
        return this.probeService.getProbesByGeneAccession(ensgAccession);
    }


    @Override
    public List<TPBGene> getTPBGenesByProbeId(String probeId) {
        return this.probeService.getTPBGenesByProbeId(probeId);
    }

    @Override
    public void importProbes(List<ProbeGeneBean> probeGeneBeans) {
        if (probeGeneBeans != null) {
            for (ProbeGeneBean probeGeneBean : probeGeneBeans) {
                String ensgAc = probeGeneBean.getEnsgAccession();
                String probeId = probeGeneBean.getProbeId();

                TPBGene tpbGene = this.getTPBGeneByEnsgAc(ensgAc);
                //we only focus on tpb gene is available
                if (tpbGene != null) {
                    Probe probe = this.getProbeByProbeId(probeId);
                    //create a Probe instance first if a Probe is not found from the database
                    if (probe == null) {
                        probe = new Probe();
                        probe.setProbeId(probeId);
                    }

                    //try to find all TPBGenes which are associated with this probe
                    List<TPBGene> tpbGenes = this.getTPBGenesByProbeId(probeId);
                    //if found
                    if (tpbGenes != null) {
                        //try to find all TPBGenes which are associated with this probe
                        //if the current TPBGene is not included this TPBGene list, then we have to add it. otherwise we just ignore
                        if (!tpbGenes.contains(tpbGene)) {
                            //try to find all TPBGenes which are associated with this probe
                            tpbGenes.add(tpbGene);
                        }
                    } else {
                        //if not found
                        //try to find all TPBGenes which are associated with this probe
                        tpbGenes = new ArrayList<TPBGene>();
                        tpbGenes.add(tpbGene);
                    }

                    //set the tpb genes
                    probe.setTpbGenes(tpbGenes);
                    //check if primary key id is zero which means new entity,
                    if (probe.getId() == 0) {
                        this.saveProbe(probe);
                    } else {
                        this.mergeProbe(probe);
                    }
                }
            }
            System.out.println("============ total probe_gene size : " + probeGeneBeans.size());
        }
    }


    @Override
    public void saveTEMAEntry(List<ChromType> chromTypes, BarcodeDataBean barcodeDataBean, String fileName, Date importedTime, String fileTimeToken) {
        //if BarcodeDataBean is not null
        if (barcodeDataBean != null) {
            String barcodeType = barcodeDataBean.getBarcodeType();
            //get all barcode data entry beans
            List<CsvProbeTissueEntryBean> barcodePTEntryBeans = barcodeDataBean.getProbeTissueEntryBeans();

            //if the CsvProbeTissueEntryBean barcodePTEntryBeans  is not null.
            if (barcodePTEntryBeans != null) {
                logger.info("TPB is starting to save the barcode data - " + fileName);
                //if chromosome type list is empty which means all chromosomes
                if (chromTypes.isEmpty()) {
                    chromTypes = ChromType.allChroms();
                }
                //create DSVersion
                for (ChromType chromType : chromTypes) {
                    createDSVersionByDbsChrom(chromType, DbAcType.fromType(barcodeType), importedTime, fileName, fileTimeToken);
                }
                boolean evidenceSaved = false;
                //save the primary dbsource - barcode dbsource first
                DBSource barcodeDbs = createBarCodeDBSource(barcodeType);

                //get the TE_MA_PROP data type
                TPBDataType teMaPropDataType = this.getTPBDataTypeByTypeName(DataType.TE_MA_PROP.type());

                for (CsvProbeTissueEntryBean probeTissueBean : barcodePTEntryBeans) {
                    String probeId = probeTissueBean.getProbeId();

                    List<TPBGene> tpbGenes = this.getTPBGenesByProbeId(probeId);
                    if (tpbGenes != null) {
                        //save the probe accession first as we don't need to save this probe accession repeatly during the for loop.
                        Accession affymetrixAc = saveBarcodeAccession(probeId, DbAcType.Affymetrix);

                        if (tpbGenes.size() == 1) {
                            //unique gene evidence
                            List<TEEvidence> TEEvidences = genAllTEMaEvidences(probeTissueBean, true);
                            TPBGene tpbGene = tpbGenes.get(0);
                            boolean saved = persistTeEvidences(tpbGene, chromTypes, affymetrixAc, TEEvidences, barcodeDbs, teMaPropDataType, importedTime);
                            if (saved) {
                                evidenceSaved = true;
                            }
                        }

                        if (tpbGenes.size() > 1) {
                            //non-unique gene evidences, we have to make all evidences are linked into each gene
                            List<TEEvidence> TEEvidences = genAllTEMaEvidences(probeTissueBean, false);
                            for (TPBGene tpbGene : tpbGenes) {
                                boolean saved = persistTeEvidences(tpbGene, chromTypes, affymetrixAc, TEEvidences, barcodeDbs, teMaPropDataType, importedTime);
                                if (saved) {
                                    evidenceSaved = true;
                                }
                            }
                        }
                    }
                }

                if (!evidenceSaved) {
                    throw new DMException("no evidence from barcode datasource is saved.");
                }
            }
        }
    }

    private boolean persistTeEvidences(TPBGene tpbGene, List<ChromType> chromTypes, Accession affymetrixAc, List<TEEvidence> TEEvidences, DBSource barcodeDbs, TPBDataType teMaPropDataType, Date importedTime) {
        boolean evidenceSaved = false;
        String chrom = tpbGene.getChromosome();
        ChromType tpbGeneChromType = ChromType.fromType(chrom);
        //if the chromosome name is an unknown name, then we have to set it as other chromosome
        if (tpbGeneChromType.equals(ChromType.UNKNOWN)) {
            tpbGeneChromType = ChromType.CHMOTHER;
        }
        //only focus on the required chromosome type
        if (chromTypes.contains(tpbGeneChromType)) {
            List<Accession> savedAccessions = new ArrayList<Accession>();
            Accession ensgAc = saveBarcodeAccession(tpbGene.getEnsgAccession(), DbAcType.Gene);
            savedAccessions.add(affymetrixAc);
            savedAccessions.add(ensgAc);

            //create GeneBean
            GeneBean geneBean = createGeneBeanFromTPBGene(tpbGene);
            //save the gene for barcode
            Gene barcodeGene = persistGene(geneBean, barcodeDbs, savedAccessions, importedTime);
            //save all evidence
            for (TEEvidence TEEvidence : TEEvidences) {
                this.persistTEEvidence(barcodeGene, affymetrixAc, teMaPropDataType, TEEvidence, importedTime);
                evidenceSaved = true;
            }
        }
        return evidenceSaved;
    }

    private DBSource createBarCodeDBSource(String barcodeType) {
        //save the primary dbsource - barcode dbsource first
        DBSourceBean bcDbSourceBean = new DBSourceBean();
        bcDbSourceBean.setDbName(barcodeType);
        bcDbSourceBean.setPrimaryEvidences(true);
        DBSource barcodeDbs = persistDBSource(bcDbSourceBean);
        return barcodeDbs;
    }

    private Accession saveBarcodeAccession(String ac, DbAcType dbAcType) {
        //save barcode accessions
        AccessionBean accessionBean = new AccessionBean();
        //set the accession value
        accessionBean.setAccession(ac);

        //dbsource
        DBSourceBean dbSourceBean = new DBSourceBean();

        if (dbAcType.equals(DbAcType.Affymetrix)) {
            accessionBean.setAcType(DbAcType.Affymetrix.type());
            dbSourceBean.setDbName(DbAcType.Affymetrix.type());
        } else if (dbAcType.equals(DbAcType.Gene)) {
            accessionBean.setAcType(DbAcType.Gene.type());
            dbSourceBean.setDbName(DbAcType.Ensembl.type());
        } else {
            return null;
        }

        //persist the dbsource
        DBSource dbSource = persistDBSource(dbSourceBean);

        //persist accession type
        AccessionType accessionType = persistAccessionType(accessionBean.getAcType());

        //persist accession
        return persistAccession(dbSource, accessionBean, accessionType);
    }


    private List<TEEvidence> genAllTEMaEvidences(CsvProbeTissueEntryBean probeTissueEntryBean, boolean uniqueGene) {
        List<TEEvidence> TEEvidences = new ArrayList<TEEvidence>();
        List<CsvTissueExpBean> tissueExpBeans = probeTissueEntryBean.getTissueExpBeans();
        for (CsvTissueExpBean tissueExpBean : tissueExpBeans) {
            TEEvidence TEEvidence = new TEEvidence();
            double tissueExpression = tissueExpBean.getExpression();

            TEEvidence.setTissueName(tissueExpBean.getTissueName());
            TEEvidence.setExpression(tissueExpression);
            if (uniqueGene) {
                if (tissueExpression >= 0.8) {
                    TEEvidence.setColorLevel(ColorType.GREEN.color());
                } else if (tissueExpression >= 0.5 && tissueExpression < 0.8) {
                    TEEvidence.setColorLevel(ColorType.YELLOW.color());
                } else if (tissueExpression > 0 && tissueExpression < 0.5) {
                    TEEvidence.setColorLevel(ColorType.RED.color());
                } else {
                    TEEvidence.setColorLevel(ColorType.BLACK.color());
                }
            } else {
                if (tissueExpression >= 0.5) {
                    TEEvidence.setColorLevel(ColorType.YELLOW.color());
                } else if (tissueExpression > 0 && tissueExpression < 0.5) {
                    TEEvidence.setColorLevel(ColorType.RED.color());
                } else {
                    TEEvidence.setColorLevel(ColorType.BLACK.color());
                }
            }
            TEEvidence.setHyperLink("http://barcode.luhs.org/index.php?page=tissuegene");
            TEEvidences.add(TEEvidence);
            TEEvidence.setUnique(uniqueGene);
        }
        return TEEvidences;
    }

    private TEEvidence persistTEEvidence(Gene gene, Accession identifiedAccession, TPBDataType tpbDataType, TEEvidence teEvidence, Date importedTime) {
        //set the created time
        teEvidence.setCreatedTime(importedTime);
        //set the last update time
        teEvidence.setLastUpdatedTime(importedTime);
        //set identified accession
        teEvidence.setIdentifiedAccession(identifiedAccession);
        //set tpb data type
        teEvidence.setTpbDataType(tpbDataType);
        //set the source gene
        teEvidence.setGene(gene);
        //save the pe evidence
        this.saveTEEvidence(teEvidence);
        return teEvidence;
    }


    private GeneBean createGeneBeanFromTPBGene(TPBGene tpbGene) {
        GeneBean geneBean = new GeneBean();

        String displayName = tpbGene.getGeneName();
        if (StringUtils.isNotBlank(displayName)) {
            geneBean.setDisplayName(displayName);
        }

        String chromosome = tpbGene.getChromosome();
        if (StringUtils.isNotBlank(chromosome)) {
            geneBean.setChromosome(chromosome);
        }

        String desc = tpbGene.getDescription();
        if (StringUtils.isNotBlank(desc)) {
            geneBean.setDescription(desc);
        }

        String ensgAc = tpbGene.getEnsgAccession();
        if (StringUtils.isNotBlank(ensgAc)) {
            geneBean.setEnsgAccession(ensgAc);
        }

        long start = tpbGene.getStartPosition();
        if (start != 0) {
            geneBean.setStartPosition(start);
        }

        long stop = tpbGene.getEndPosition();
        if (stop != 0) {
            geneBean.setEndPosition(stop);
        }

        String band = tpbGene.getBand();
        if (StringUtils.isNotBlank(band)) {
            geneBean.setBand(band);
        }

        String strand = tpbGene.getStrand();
        if (StringUtils.isNotBlank(strand)) {
            geneBean.setStrand(strand);
        }
        return geneBean;
    }


    /**
     * {@inheritDoc}
     */
    public void saveRifcsDataset(RIFCSDataset rifcsDataset) {
        this.rifcsDatasetService.saveRifcsDataset(rifcsDataset);
    }

    /**
     * {@inheritDoc}
     */
    public void mergeRifcsDataset(RIFCSDataset rifcsDataset) {
        this.rifcsDatasetService.mergeRifcsDataset(rifcsDataset);
    }

    /**
     * {@inheritDoc}
     */
    public void updateRifcsDataset(RIFCSDataset rifcsDataset) {
        this.rifcsDatasetService.updateRifcsDataset(rifcsDataset);
    }

    /**
     * {@inheritDoc}
     */
    public void deleteRifcsDataset(RIFCSDataset rifcsDataset) {
        this.rifcsDatasetService.deleteRifcsDataset(rifcsDataset);
    }

    /**
     * {@inheritDoc}
     */
    public RIFCSDataset getRifcsDatasetById(long id) {
        return this.rifcsDatasetService.getRifcsDatasetById(id);
    }

    /**
     * {@inheritDoc}
     */
    public RIFCSDataset getRifcsDsByTpbVersionAndTrackToken(long tpbVersionId, int trackToken) {
        return this.rifcsDatasetService.getRifcsDsByTpbVersionAndTrackToken(tpbVersionId, trackToken);
    }

    /**
     * {@inheritDoc}
     */
    public void deleteRifcsDsById(long id) {
        this.rifcsDatasetService.deleteRifcsDsById(id);
    }

    /**
     * {@inheritDoc}
     */
    public void deleteRifcsDsByTpbVersionAndTrackToken(long tpbVersionId, int trackToken) {
        this.rifcsDatasetService.deleteRifcsDsByTpbVersionAndTrackToken(tpbVersionId, trackToken);
    }

    /**
     * {@inheritDoc}
     */
    public void createRifcs(RifcsInfoBean rifcsInfoBean) {
        boolean republishRequired = rifcsInfoBean.isRepublishRequired();
        //get all max combinated datasource tpb versions
        // List<TPBVersion> tpbVersions = this.getAllTPBVersions();
        List<MaxDsTPBVersion> maxDsTPBVersions = this.getAllChromosomeTPBVersionByMaxCombinatedDs();
        String rifcsStoreLocation = rifcsInfoBean.getRifcsStoreLocation();
        String rifcsTemplate = rifcsInfoBean.getRifcsTemplate();


        //create a traffic light base url
        String baseUrl = rifcsInfoBean.getServerName() + File.separator + rifcsInfoBean.getAppRootRelPath();

       // int i =0;
        //loop the traffic versions
        for (MaxDsTPBVersion tpbVersion : maxDsTPBVersions) {

            //we don't need the version no id
            long tpbvId = tpbVersion.getId();
            int tpbversionNo = tpbVersion.getVersionNo();
            int trackToken = tpbVersion.getTrackToken();
            String chromosome = tpbVersion.getChromosome();
          //  System.out.println("================== tpbversion id: " + tpbvId + ", version no: " + tpbversionNo + ", token : " + trackToken + ", chromosome : " + chromosome);

            //try find a RIFCSDataset if any based on a tpb version and a track token
            RIFCSDataset rifcsDataset = this.getRifcsDsByTpbVersionAndTrackToken(tpbvId, trackToken);
            //A RIFCSDataset not found
            if (rifcsDataset == null) {
                //create the RIFCS unique key
                String rifcsUniqueKey = DMUtil.genUUIDWithPrefix(rifcsInfoBean.getRifcsKeyPrefix());

                //create the traffic light url
                String tlUrl = generateTlUrl(baseUrl, chromosome, trackToken);
                //populate the RIFCS template data
                Map<String, String> templateData = populateTemplateMap(rifcsUniqueKey, chromosome, trackToken, tlUrl, rifcsInfoBean);
                createRifcsFile(rifcsStoreLocation, rifcsUniqueKey, templateData, rifcsTemplate);

                //save the RifcsDataset
                rifcsDataset = new RIFCSDataset();
                rifcsDataset.setUniqueKey(rifcsUniqueKey);
                rifcsDataset.setPublished(true);
                rifcsDataset.setTrackToken(trackToken);
                rifcsDataset.setPublishDate(rifcsInfoBean.getPublishedDate());
                rifcsDataset.setTpbVersionId(tpbvId);
                //persist the RIFCSDataset
                this.saveRifcsDataset(rifcsDataset);
            } else {
                if (republishRequired || (!rifcsDataset.isPublished())) {
                    String rifcsUniqueKey = rifcsDataset.getUniqueKey();

                    String tlUrl = generateTlUrl(baseUrl, chromosome, trackToken);
                    //populate the RIFCS template data
                    Map<String, String> templateData = populateTemplateMap(rifcsUniqueKey, chromosome, trackToken, tlUrl, rifcsInfoBean);
                    createRifcsFile(rifcsStoreLocation, rifcsUniqueKey, templateData, rifcsTemplate);

                    //set rifcs publishing flag to tru
                    rifcsDataset.setPublished(true);
                    //set the published date
                    rifcsDataset.setPublishDate(rifcsInfoBean.getPublishedDate());
                    //upate the RIFCSDataset
                    this.mergeRifcsDataset(rifcsDataset);
                }
            }
        }
    }


    //create a traffic light url
    private String generateTlUrl(String baseUrl, String chromosome, int trackToken) {
        StringBuffer url = new StringBuffer();
        url.append(baseUrl).append("/tl/trafficlight.jspx?");
        url.append("chm=").append(chromosome);
        url.append("&tt=").append(trackToken);
        //don't need the version id, as the application will pickup the current version
        //url.append("&vid=").append(tpbversionId);
        return DMUtil.replaceURLAmpsands(url.toString());
    }

    //populate rifcs template data
    private Map<String, String> populateTemplateMap(String rifcsIdentifier, String chromosome, int trackToken, String tlUrl, RifcsInfoBean rifcsInfoBean) {
        String tpbGroupName = rifcsInfoBean.getTpbGroupName();
        String originatingSource = rifcsInfoBean.getServerName();
        String dbSources = combinatedDbSources(trackToken);

        Map<String, String> templateMap = new HashMap<String, String>();
        templateMap.put("TLDatasetGroupName", tpbGroupName);
        templateMap.put("TLDatasetIdentifierKey", rifcsIdentifier);
        templateMap.put("TLDatasetOriginatingSrc", originatingSource);
        templateMap.put("TLDatasetLocalKey", rifcsIdentifier);
        templateMap.put("NameChrom", chromosome);
        templateMap.put("NameDbSource", dbSources);
        templateMap.put("TLDatasetURL", tlUrl);

        templateMap.put("DescChrom", chromosome);
        templateMap.put("DescDbSource", dbSources);

        return templateMap;
    }

    //call RIFCSService to create a rifcs file
    private void createRifcsFile(String rifcsStoreLocation, String rifcsFileIdentifier, Map<String, String> templateMap, String rifcsTemplate) {
        this.rifcsService.createRifcsFile(rifcsStoreLocation, rifcsFileIdentifier, templateMap, rifcsTemplate);
    }

    //generate a dbsource combinated string
    private String combinatedDbSources(int combinatedToken) {
        boolean nxDbSelected = false;
        boolean gpmDbSelected = false;
        boolean hpaDbSelected = false;
        boolean bcDbSelected = false;
        String trackStr = String.valueOf(combinatedToken);
        String[] tokens = DMUtil.split(trackStr);
        int tokenLength = tokens.length;
        //token number is 4 digital

        if (tokenLength == 4) {
            for (int i = (tokenLength - 1); i >= 0; i--) {
                String tk = tokens[i];

                if (StringUtils.equals(tk, "1")) {
                    if (i == 3) {
                        nxDbSelected = true;
                    }
                    if (i == 2) {
                        gpmDbSelected = true;
                    }
                    if (i == 1) {
                        hpaDbSelected = true;
                    }
                    if (i == 0) {
                        bcDbSelected = true;
                    }
                }
            }
        }

        //token number is 3 digital int 111 or 110 or 101 or 100
        if (tokenLength == 3) {
            for (int i = (tokenLength - 1); i >= 0; i--) {
                String tk = tokens[i];
                if (StringUtils.equals(tk, "1")) {
                    if (i == 2) {
                        nxDbSelected = true;
                    }
                    if (i == 1) {
                        gpmDbSelected = true;
                    }
                    if (i == 0) {
                        hpaDbSelected = true;
                    }
                }
            }
        }
        //token number is 3 digital int 11 or 10
        if (tokenLength == 2) {
            for (int i = (tokenLength - 1); i >= 0; i--) {
                String tk = tokens[i];
                if (StringUtils.equals(tk, "1")) {
                    if (i == 1) {
                        nxDbSelected = true;
                    }
                    if (i == 0) {
                        gpmDbSelected = true;
                    }
                }
            }
        }

        //token number is int 1 0r 0
        if (tokenLength == 1) {
            String tk = tokens[0];
            if (StringUtils.equals(tk, "1")) {
                nxDbSelected = true;
            }
        }
        String dbsText = "";
        if (nxDbSelected) {
            dbsText += DbAcType.NextProt.type();
        }
        if (gpmDbSelected) {
            if (StringUtils.isBlank(dbsText)) {
                dbsText += DbAcType.GPM.type();
            } else {
                dbsText += ", " + DbAcType.GPM.type();
            }
        }
        if (hpaDbSelected) {
            if (StringUtils.isBlank(dbsText)) {
                dbsText += DbAcType.HPA.type();
            } else {
                dbsText += ", " + DbAcType.HPA.type();
            }
        }
        if (bcDbSelected) {
            if (StringUtils.isBlank(dbsText)) {
                dbsText += DbAcType.BarCode.type();
            } else {
                dbsText += ", " + DbAcType.BarCode.type();
            }
        }
        return dbsText;
    }

    public void sendMail(String emailFrom, String emailTo, String emailSubject, String emailBody, boolean isHtml) {
        this.mailService.sendMail(emailFrom, emailTo, emailSubject, emailBody, isHtml);
    }

    public void sendMail(String emailFrom, String emailTo, String emailSubject, Map<String, String> templateValues, String templateFile, boolean isHtml) {
        this.mailService.sendMail(emailFrom, emailTo, emailSubject, templateValues, templateFile, isHtml);
    }
}
