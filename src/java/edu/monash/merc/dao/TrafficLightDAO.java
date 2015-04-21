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

package edu.monash.merc.dao;

import edu.monash.merc.common.name.ChromType;
import edu.monash.merc.common.name.DataType;
import edu.monash.merc.common.page.Pagination;
import edu.monash.merc.common.sql.OrderBy;
import edu.monash.merc.domain.TPBVersion;
import edu.monash.merc.domain.TrafficLight;
import edu.monash.merc.dto.TLSumReporter;
import edu.monash.merc.dto.TLTypeSumReporter;
import edu.monash.merc.dto.tl.TLSearchBean;
import edu.monash.merc.dto.tl.TLTypeEvLevelFilter;
import edu.monash.merc.repository.ITrafficLightRep;
import edu.monash.merc.util.DMUtil;
import org.apache.commons.lang.StringUtils;
import org.hibernate.Criteria;
import org.hibernate.Query;
import org.hibernate.criterion.*;
import org.springframework.beans.factory.config.BeanDefinition;
import org.springframework.context.annotation.Scope;
import org.springframework.stereotype.Repository;

import java.util.List;

/**
 * TrafficLightDAO class which provides the database operations for TrafficLight object.
 *
 * @author Simon Yu
 *         <p/>
 *         Email: xiaoming.yu@monash.edu
 * @version 1.0
 * @since 1.0
 *        <p/>
 *        Date: 9/03/12 11:57 AM
 */
@Scope(BeanDefinition.SCOPE_PROTOTYPE)
@Repository
public class TrafficLightDAO extends HibernateGenericDAO<TrafficLight> implements ITrafficLightRep {

    /**
     * {@inheritDoc}
     */
    public void deleteTLById(long id) {
        String del_hql = "DELETE FROM " + this.persistClass.getSimpleName() + " AS tl WHERE tl.id = :id";
        Query query = this.session().createQuery(del_hql);
        query.setLong("id", id);
        query.executeUpdate();
    }

    /**
     * {@inheritDoc}
     */
    public TrafficLight getTLByChromEnsemblAcVersionToken(ChromType chromType, String ensgAc, long versionId, int trackToken) {
        Criteria qCriteria = this.session().createCriteria(this.persistClass);

        Criteria tlGeneCriteria = qCriteria.createCriteria("tlGene");
        tlGeneCriteria.add(Restrictions.eq("chromosome", chromType.chm()));

        Disjunction orConds = Restrictions.disjunction();
        orConds.add(Restrictions.eq("ensgAccession", ensgAc));
        tlGeneCriteria.add(orConds);
        Criteria tpbVCriteria = qCriteria.createCriteria("tpbVersion");
        tpbVCriteria.add(Restrictions.eq("id", versionId));
        tpbVCriteria.add(Restrictions.eq("trackToken", trackToken));
        return (TrafficLight) qCriteria.uniqueResult();
    }

    /**
     * {@inheritDoc}
     */
    @SuppressWarnings("unchecked")
    public Pagination<TrafficLight> getVersionTrafficLight(ChromType chromType, int trackToken, long versionId, int startPage, int recordsPerPage, OrderBy[] orderConds) {

        Criteria tlCriteria = this.session().createCriteria(this.persistClass);
        Criteria tlgCriteria = tlCriteria.createCriteria("tlGene");
        Criteria tpbVCriteria = tlCriteria.createCriteria("tpbVersion");
        tlgCriteria.add(Restrictions.eq("chromosome", chromType.chm()));
        // tlgCriteria.add(Restrictions.in("displayName", geneNames));
        tpbVCriteria.add(Restrictions.eq("id", versionId));
        tpbVCriteria.add(Restrictions.eq("trackToken", trackToken));
        tlCriteria.setProjection(Projections.rowCount());
        int total = ((Long) tlCriteria.uniqueResult()).intValue();
        //get all results
        if (recordsPerPage == -1) {
            recordsPerPage = total;
            startPage = 1;
        }
        Pagination<TrafficLight> ptl = new Pagination<TrafficLight>(startPage, recordsPerPage, total);

        Criteria tlQCriteria = this.session().createCriteria(this.persistClass);
        Criteria tlgQCriteria = tlQCriteria.createCriteria("tlGene");
        Criteria tpbVQCriteria = tlQCriteria.createCriteria("tpbVersion");
        tlgQCriteria.add(Restrictions.eq("chromosome", chromType.chm()));
        tpbVQCriteria.add(Restrictions.eq("id", versionId));
        tpbVQCriteria.add(Restrictions.eq("trackToken", trackToken));
        // add orders
        if (orderConds != null && orderConds.length > 0) {
            for (int i = 0; i < orderConds.length; i++) {
                Order order = orderConds[i].getOrder();
                if (order != null) {
                    tlgQCriteria.addOrder(order);
                }
            }
        }
        tlQCriteria.setFirstResult(ptl.getFirstResult());
        tlQCriteria.setMaxResults(ptl.getSizePerPage());

        List<TrafficLight> tlList = tlQCriteria.list();
        ptl.setPageResults(tlList);
        return ptl;
    }

    /**
     * {@inheritDoc}
     */
    @SuppressWarnings("unchecked")
    public Pagination<TrafficLight> getVersionTrafficLight(TLSearchBean tlSearchBean, int startPage, int recordsPerPage, OrderBy[] orderConds) {

        String chromType = tlSearchBean.getSelectedChromType();
        int token = tlSearchBean.getCombinatedToken();
        long versionId = tlSearchBean.getSelectedVersion();
        boolean chromRegionProvided = tlSearchBean.isRegionProvided();
        boolean geneListProvided = tlSearchBean.isGeneListProvided();

        boolean typeLevelProvided = tlSearchBean.isTypeLevelProvided();

        Criteria tlCrt = this.session().createCriteria(this.persistClass);

        if (typeLevelProvided) {
            addTypeColorLevelConditions(tlCrt, tlSearchBean);
        }

        Criteria tlGeneCrt = tlCrt.createCriteria("tlGene");

        Criteria tpbVersionCrt = tlCrt.createCriteria("tpbVersion");
        tlGeneCrt.add(Restrictions.eq("chromosome", chromType));
        //chromosome region provided
        if (chromRegionProvided) {
            addChromRegionConditions(tlGeneCrt, tlSearchBean);
        }
        //gene list provided
        if (geneListProvided) {
            addGeneListConditions(tlGeneCrt, tlSearchBean);
        }

        tpbVersionCrt.add(Restrictions.eq("id", versionId));
        tpbVersionCrt.add(Restrictions.eq("trackToken", token));
        tlCrt.setProjection(Projections.rowCount());

        int total = ((Long) tlCrt.uniqueResult()).intValue();
        //get all results
        if (recordsPerPage == -1) {
            recordsPerPage = total;
            startPage = 1;
        }

        Pagination<TrafficLight> paginationTl = new Pagination<TrafficLight>(startPage, recordsPerPage, total);

        Criteria tlCriteria = this.session().createCriteria(this.persistClass);

        if (typeLevelProvided) {
            addTypeColorLevelConditions(tlCriteria, tlSearchBean);
        }

        Criteria tlGeneCriteria = tlCriteria.createCriteria("tlGene");

        Criteria tpbVersionCriteria = tlCriteria.createCriteria("tpbVersion");
        tlGeneCriteria.add(Restrictions.eq("chromosome", chromType));

        //chromosome region provided
        if (chromRegionProvided) {
            addChromRegionConditions(tlGeneCriteria, tlSearchBean);
        }
        //gene list provided
        if (geneListProvided) {
            addGeneListConditions(tlGeneCriteria, tlSearchBean);
        }

        //tpb version
        tpbVersionCriteria.add(Restrictions.eq("id", versionId));
        tpbVersionCriteria.add(Restrictions.eq("trackToken", token));
        // add orders
        if (orderConds != null && orderConds.length > 0) {
            for (int i = 0; i < orderConds.length; i++) {
                Order order = orderConds[i].getOrder();
                if (order != null) {
                    tlGeneCriteria.addOrder(order);
                }
            }
        }
        tlCriteria.setFirstResult(paginationTl.getFirstResult());
        tlCriteria.setMaxResults(paginationTl.getSizePerPage());

        List<TrafficLight> tlList = tlCriteria.list();
        paginationTl.setPageResults(tlList);

        return paginationTl;
    }

    private void addTypeColorLevelConditions(Criteria criteria, TLSearchBean tlSearchBean) {
        if (tlSearchBean.isTypeLevelProvided()) {
            List<TLTypeEvLevelFilter> tlTypeLevelFilters = tlSearchBean.getTlTypeEvLevelFilters();
            for (TLTypeEvLevelFilter tlevelFilter : tlTypeLevelFilters) {
                if (tlevelFilter.isValidTypeLevel()) {
                    String dataType = tlevelFilter.getDataType();
                    List<Integer> colorLevels = tlevelFilter.getCheckedColorLevels();
                    addSpecificTPBTypeColorLevelConditions(criteria, dataType, colorLevels);
                }
            }
        }
    }

    private void addSpecificTPBTypeColorLevelConditions(Criteria criteria, String dataType, List<Integer> colorLevels) {
        //convert the dataType into DataType
        DataType tpbDataType = DataType.fromType(dataType);
        //PE
        if (DataType.PE.equals(tpbDataType)) {
            Criteria peColorCriteria = criteria.createCriteria("tlPEColor");
            //color level size is great than one. we use or condition
            if (colorLevels != null && colorLevels.size() > 1) {
                Disjunction colorLevelOr = Restrictions.disjunction();
                for (Integer level : colorLevels) {
                    colorLevelOr.add(Restrictions.eq("colorLevel", level.intValue()));
                }
                peColorCriteria.add(colorLevelOr);
            }
            //if only on color level, we just use eq condition
            if (colorLevels != null && colorLevels.size() == 1) {
                peColorCriteria.add(Restrictions.eq("colorLevel", colorLevels.get(0).intValue()));
            }
        }

        //PE MS
        if (DataType.PE_MS.equals(tpbDataType)) {
            Criteria peMsColorCriteria = criteria.createCriteria("tlPEMSColor");
            if (colorLevels != null && colorLevels.size() > 1) {
                Disjunction colorLevelOr = Restrictions.disjunction();
                for (Integer level : colorLevels) {
                    colorLevelOr.add(Restrictions.eq("colorLevel", level.intValue()));
                }
                peMsColorCriteria.add(colorLevelOr);
            }
            if (colorLevels != null && colorLevels.size() == 1) {
                peMsColorCriteria.add(Restrictions.eq("colorLevel", colorLevels.get(0).intValue()));
            }
        }

        //PE MS Ann
        if (DataType.PE_MS_ANN.equals(tpbDataType)) {
            Criteria peMsAnnColorCriteria = criteria.createCriteria("tlPEMSANNColor");
            if (colorLevels != null && colorLevels.size() > 1) {
                Disjunction colorLevelOr = Restrictions.disjunction();
                for (Integer level : colorLevels) {
                    colorLevelOr.add(Restrictions.eq("colorLevel", level.intValue()));
                }
                peMsAnnColorCriteria.add(colorLevelOr);
            }
            if (colorLevels != null && colorLevels.size() == 1) {
                peMsAnnColorCriteria.add(Restrictions.eq("colorLevel", colorLevels.get(0).intValue()));
            }
        }

        //PE MS Prob
        if (DataType.PE_MS_PROB.equals(tpbDataType)) {
            Criteria peMsProbColorCriteria = criteria.createCriteria("tlPEMSPROBColor");
            if (colorLevels != null && colorLevels.size() > 1) {
                Disjunction colorLevelOr = Restrictions.disjunction();
                for (Integer level : colorLevels) {
                    colorLevelOr.add(Restrictions.eq("colorLevel", level.intValue()));
                }
                peMsProbColorCriteria.add(colorLevelOr);
            }
            if (colorLevels != null && colorLevels.size() == 1) {
                peMsProbColorCriteria.add(Restrictions.eq("colorLevel", colorLevels.get(0).intValue()));
            }
        }

        //PE MS Sam
        if (DataType.PE_MS_SAM.equals(tpbDataType)) {
            Criteria peMsSamColorCriteria = criteria.createCriteria("tlPEMSSAMColor");
            if (colorLevels != null && colorLevels.size() > 1) {
                Disjunction colorLevelOr = Restrictions.disjunction();
                for (Integer level : colorLevels) {
                    colorLevelOr.add(Restrictions.eq("colorLevel", level.intValue()));
                }
                peMsSamColorCriteria.add(colorLevelOr);
            }
            if (colorLevels != null && colorLevels.size() == 1) {
                peMsSamColorCriteria.add(Restrictions.eq("colorLevel", colorLevels.get(0).intValue()));
            }
        }

        //PE ANTI
        if (DataType.PE_ANTI.equals(tpbDataType)) {
            Criteria peAntiColorCriteria = criteria.createCriteria("tlPEANTIColor");
            if (colorLevels != null && colorLevels.size() > 1) {
                Disjunction colorLevelOr = Restrictions.disjunction();
                for (Integer level : colorLevels) {
                    colorLevelOr.add(Restrictions.eq("colorLevel", level.intValue()));
                }
                peAntiColorCriteria.add(colorLevelOr);
            }
            if (colorLevels != null && colorLevels.size() == 1) {
                peAntiColorCriteria.add(Restrictions.eq("colorLevel", colorLevels.get(0).intValue()));
            }
        }

        //PE ANTI ANN
        if (DataType.PE_ANTI_ANN.equals(tpbDataType)) {
            Criteria peAntiAnnColorCriteria = criteria.createCriteria("tlPEANTIANNColor");
            if (colorLevels != null && colorLevels.size() > 1) {
                Disjunction colorLevelOr = Restrictions.disjunction();
                for (Integer level : colorLevels) {
                    colorLevelOr.add(Restrictions.eq("colorLevel", level.intValue()));
                }
                peAntiAnnColorCriteria.add(colorLevelOr);
            }
            if (colorLevels != null && colorLevels.size() == 1) {
                peAntiAnnColorCriteria.add(Restrictions.eq("colorLevel", colorLevels.get(0).intValue()));
            }
        }

        //PE ANTI IHC
        if (DataType.PE_ANTI_IHC.equals(tpbDataType)) {
            Criteria peAntiIhcColorCriteria = criteria.createCriteria("tlPEANTIIHCColor");
            if (colorLevels != null && colorLevels.size() > 1) {
                Disjunction colorLevelOr = Restrictions.disjunction();
                for (Integer level : colorLevels) {
                    colorLevelOr.add(Restrictions.eq("colorLevel", level.intValue()));
                }
                peAntiIhcColorCriteria.add(colorLevelOr);
            }
            if (colorLevels != null && colorLevels.size() == 1) {
                peAntiIhcColorCriteria.add(Restrictions.eq("colorLevel", colorLevels.get(0).intValue()));
            }
        }

        //PE ANTI IHC Norm
        if (DataType.PE_ANTI_IHC_NORM.equals(tpbDataType)) {
            Criteria peAntiIhcNormColorCriteria = criteria.createCriteria("tlPEANTIIHCNORMColor");
            if (colorLevels != null && colorLevels.size() > 1) {
                Disjunction colorLevelOr = Restrictions.disjunction();
                for (Integer level : colorLevels) {
                    colorLevelOr.add(Restrictions.eq("colorLevel", level.intValue()));
                }
                peAntiIhcNormColorCriteria.add(colorLevelOr);
            }
            if (colorLevels != null && colorLevels.size() == 1) {
                peAntiIhcNormColorCriteria.add(Restrictions.eq("colorLevel", colorLevels.get(0).intValue()));
            }
        }

        //PE OTH
        if (DataType.PE_OTH.equals(tpbDataType)) {
            Criteria peOthColorCriteria = criteria.createCriteria("tlPEOTHColor");
            if (colorLevels != null && colorLevels.size() > 1) {
                Disjunction colorLevelOr = Restrictions.disjunction();
                for (Integer level : colorLevels) {
                    colorLevelOr.add(Restrictions.eq("colorLevel", level.intValue()));
                }
                peOthColorCriteria.add(colorLevelOr);
            }
            if (colorLevels != null && colorLevels.size() == 1) {
                peOthColorCriteria.add(Restrictions.eq("colorLevel", colorLevels.get(0).intValue()));
            }
        }

        //PE OTH Cur
        if (DataType.PE_OTH_CUR.equals(tpbDataType)) {
            Criteria peOthCurColorCriteria = criteria.createCriteria("tlPEOTHCURColor");
            if (colorLevels != null && colorLevels.size() > 1) {
                Disjunction colorLevelOr = Restrictions.disjunction();
                for (Integer level : colorLevels) {
                    colorLevelOr.add(Restrictions.eq("colorLevel", level.intValue()));
                }
                peOthCurColorCriteria.add(colorLevelOr);
            }
            if (colorLevels != null && colorLevels.size() == 1) {
                peOthCurColorCriteria.add(Restrictions.eq("colorLevel", colorLevels.get(0).intValue()));
            }
        }

        //PTM
        if (DataType.PTM.equals(tpbDataType)) {
            Criteria ptmColorCriteria = criteria.createCriteria("tlPTMColor");
            //color level size is great than one. we use or condition
            if (colorLevels != null && colorLevels.size() > 1) {
                Disjunction colorLevelOr = Restrictions.disjunction();
                for (Integer level : colorLevels) {
                    colorLevelOr.add(Restrictions.eq("colorLevel", level.intValue()));
                }
                ptmColorCriteria.add(colorLevelOr);
            }
            //if only on color level, we just use eq condition
            if (colorLevels != null && colorLevels.size() == 1) {
                ptmColorCriteria.add(Restrictions.eq("colorLevel", colorLevels.get(0).intValue()));
            }
        }

        //PTM PHS
        if (DataType.PTM_PHS.equals(tpbDataType)) {
            Criteria ptmPhsColorCriteria = criteria.createCriteria("tlPTMPHSColor");
            //color level size is great than one. we use or condition
            if (colorLevels != null && colorLevels.size() > 1) {
                Disjunction colorLevelOr = Restrictions.disjunction();
                for (Integer level : colorLevels) {
                    colorLevelOr.add(Restrictions.eq("colorLevel", level.intValue()));
                }
                ptmPhsColorCriteria.add(colorLevelOr);
            }
            //if only on color level, we just use eq condition
            if (colorLevels != null && colorLevels.size() == 1) {
                ptmPhsColorCriteria.add(Restrictions.eq("colorLevel", colorLevels.get(0).intValue()));
            }
        }

        //PTM PHS SER
        if (DataType.PTM_PHS_SER.equals(tpbDataType)) {
            Criteria ptmPhsSerColorCriteria = criteria.createCriteria("tlPTMPHSSERColor");
            //color level size is great than one. we use or condition
            if (colorLevels != null && colorLevels.size() > 1) {
                Disjunction colorLevelOr = Restrictions.disjunction();
                for (Integer level : colorLevels) {
                    colorLevelOr.add(Restrictions.eq("colorLevel", level.intValue()));
                }
                ptmPhsSerColorCriteria.add(colorLevelOr);
            }
            //if only on color level, we just use eq condition
            if (colorLevels != null && colorLevels.size() == 1) {
                ptmPhsSerColorCriteria.add(Restrictions.eq("colorLevel", colorLevels.get(0).intValue()));
            }
        }

        //PTM PHS THR
        if (DataType.PTM_PHS_THR.equals(tpbDataType)) {
            Criteria ptmPhsThrColorCriteria = criteria.createCriteria("tlPTMPHSTHRColor");
            //color level size is great than one. we use or condition
            if (colorLevels != null && colorLevels.size() > 1) {
                Disjunction colorLevelOr = Restrictions.disjunction();
                for (Integer level : colorLevels) {
                    colorLevelOr.add(Restrictions.eq("colorLevel", level.intValue()));
                }
                ptmPhsThrColorCriteria.add(colorLevelOr);
            }
            //if only on color level, we just use eq condition
            if (colorLevels != null && colorLevels.size() == 1) {
                ptmPhsThrColorCriteria.add(Restrictions.eq("colorLevel", colorLevels.get(0).intValue()));
            }
        }

        //PTM PHS TYR
        if (DataType.PTM_PHS_TYR.equals(tpbDataType)) {
            Criteria ptmPhsTyrColorCriteria = criteria.createCriteria("tlPTMPHSTYRColor");
            //color level size is great than one. we use or condition
            if (colorLevels != null && colorLevels.size() > 1) {
                Disjunction colorLevelOr = Restrictions.disjunction();
                for (Integer level : colorLevels) {
                    colorLevelOr.add(Restrictions.eq("colorLevel", level.intValue()));
                }
                ptmPhsTyrColorCriteria.add(colorLevelOr);
            }
            //if only on color level, we just use eq condition
            if (colorLevels != null && colorLevels.size() == 1) {
                ptmPhsTyrColorCriteria.add(Restrictions.eq("colorLevel", colorLevels.get(0).intValue()));
            }
        }


        //PTM ACE
        if (DataType.PTM_ACE.equals(tpbDataType)) {
            Criteria ptmAceColorCriteria = criteria.createCriteria("tlPTMACEColor");
            //color level size is great than one. we use or condition
            if (colorLevels != null && colorLevels.size() > 1) {
                Disjunction colorLevelOr = Restrictions.disjunction();
                for (Integer level : colorLevels) {
                    colorLevelOr.add(Restrictions.eq("colorLevel", level.intValue()));
                }
                ptmAceColorCriteria.add(colorLevelOr);
            }
            //if only on color level, we just use eq condition
            if (colorLevels != null && colorLevels.size() == 1) {
                ptmAceColorCriteria.add(Restrictions.eq("colorLevel", colorLevels.get(0).intValue()));
            }
        }

        //PTM ACE LYS
        if (DataType.PTM_ACE_LYS.equals(tpbDataType)) {
            Criteria ptmAceLysColorCriteria = criteria.createCriteria("tlPTMACELYSColor");
            //color level size is great than one. we use or condition
            if (colorLevels != null && colorLevels.size() > 1) {
                Disjunction colorLevelOr = Restrictions.disjunction();
                for (Integer level : colorLevels) {
                    colorLevelOr.add(Restrictions.eq("colorLevel", level.intValue()));
                }
                ptmAceLysColorCriteria.add(colorLevelOr);
            }
            //if only on color level, we just use eq condition
            if (colorLevels != null && colorLevels.size() == 1) {
                ptmAceLysColorCriteria.add(Restrictions.eq("colorLevel", colorLevels.get(0).intValue()));
            }
        }

        //PTM ACE NTA
        if (DataType.PTM_ACE_NTA.equals(tpbDataType)) {
            Criteria ptmAceNtaColorCriteria = criteria.createCriteria("tlPTMACENTAColor");
            //color level size is great than one. we use or condition
            if (colorLevels != null && colorLevels.size() > 1) {
                Disjunction colorLevelOr = Restrictions.disjunction();
                for (Integer level : colorLevels) {
                    colorLevelOr.add(Restrictions.eq("colorLevel", level.intValue()));
                }
                ptmAceNtaColorCriteria.add(colorLevelOr);
            }
            //if only on color level, we just use eq condition
            if (colorLevels != null && colorLevels.size() == 1) {
                ptmAceNtaColorCriteria.add(Restrictions.eq("colorLevel", colorLevels.get(0).intValue()));
            }
        }


        //TE
        if (DataType.TE.equals(tpbDataType)) {
            Criteria teColorCriteria = criteria.createCriteria("tlTEColor");
            //color level size is great than one. we use or condition
            if (colorLevels != null && colorLevels.size() > 1) {
                Disjunction colorLevelOr = Restrictions.disjunction();
                for (Integer level : colorLevels) {
                    colorLevelOr.add(Restrictions.eq("colorLevel", level.intValue()));
                }
                teColorCriteria.add(colorLevelOr);
            }
            //if only on color level, we just use eq condition
            if (colorLevels != null && colorLevels.size() == 1) {
                teColorCriteria.add(Restrictions.eq("colorLevel", colorLevels.get(0).intValue()));
            }
        }

        //TE MA
        if (DataType.TE_MA.equals(tpbDataType)) {
            Criteria teMaColorCriteria = criteria.createCriteria("tlTEMAColor");
            //color level size is great than one. we use or condition
            if (colorLevels != null && colorLevels.size() > 1) {
                Disjunction colorLevelOr = Restrictions.disjunction();
                for (Integer level : colorLevels) {
                    colorLevelOr.add(Restrictions.eq("colorLevel", level.intValue()));
                }
                teMaColorCriteria.add(colorLevelOr);
            }
            //if only on color level, we just use eq condition
            if (colorLevels != null && colorLevels.size() == 1) {
                teMaColorCriteria.add(Restrictions.eq("colorLevel", colorLevels.get(0).intValue()));
            }
        }

        //TE MA PROP
        if (DataType.TE_MA_PROP.equals(tpbDataType)) {
            Criteria teMaPropColorCriteria = criteria.createCriteria("tlTEMAPROPColor");
            //color level size is great than one. we use or condition
            if (colorLevels != null && colorLevels.size() > 1) {
                Disjunction colorLevelOr = Restrictions.disjunction();
                for (Integer level : colorLevels) {
                    colorLevelOr.add(Restrictions.eq("colorLevel", level.intValue()));
                }
                teMaPropColorCriteria.add(colorLevelOr);
            }
            //if only on color level, we just use eq condition
            if (colorLevels != null && colorLevels.size() == 1) {
                teMaPropColorCriteria.add(Restrictions.eq("colorLevel", colorLevels.get(0).intValue()));
            }
        }

        //TE OTH
        if (DataType.TE_OTH.equals(tpbDataType)) {
            Criteria teOthColorCriteria = criteria.createCriteria("tlTEOTHColor");
            //color level size is great than one. we use or condition
            if (colorLevels != null && colorLevels.size() > 1) {
                Disjunction colorLevelOr = Restrictions.disjunction();
                for (Integer level : colorLevels) {
                    colorLevelOr.add(Restrictions.eq("colorLevel", level.intValue()));
                }
                teOthColorCriteria.add(colorLevelOr);
            }
            //if only on color level, we just use eq condition
            if (colorLevels != null && colorLevels.size() == 1) {
                teOthColorCriteria.add(Restrictions.eq("colorLevel", colorLevels.get(0).intValue()));
            }
        }

        //TE OTH CUR
        if (DataType.TE_OTH_CUR.equals(tpbDataType)) {
            Criteria teOthCurColorCriteria = criteria.createCriteria("tlTEOTHCURColor");
            //color level size is great than one. we use or condition
            if (colorLevels != null && colorLevels.size() > 1) {
                Disjunction colorLevelOr = Restrictions.disjunction();
                for (Integer level : colorLevels) {
                    colorLevelOr.add(Restrictions.eq("colorLevel", level.intValue()));
                }
                teOthCurColorCriteria.add(colorLevelOr);
            }
            //if only on color level, we just use eq condition
            if (colorLevels != null && colorLevels.size() == 1) {
                teOthCurColorCriteria.add(Restrictions.eq("colorLevel", colorLevels.get(0).intValue()));
            }
        }


    }

    private void addChromRegionConditions(Criteria geneCriteria, TLSearchBean tlSearchBean) {
        if (tlSearchBean.isRegionProvided()) {
            long regionFrom = tlSearchBean.getRegionFrom();
            long regionTo = tlSearchBean.getRegionTo();
            if (regionFrom != 0 && regionTo != 0) {
                geneCriteria.add(Restrictions.ge("endPosition", regionFrom)).add(Restrictions.le("startPosition", regionTo));
            }
            if (regionFrom != 0 && regionTo == 0) {
                geneCriteria.add(Restrictions.ge("endPosition", regionFrom));
            }
            if (regionFrom == 0 && regionTo != 0) {
                geneCriteria.add(Restrictions.le("startPosition", regionTo));
            }
        }
    }

    private void addGeneListConditions(Criteria geneCriteria, TLSearchBean tlSearchBean) {
        if (tlSearchBean.isGeneListProvided()) {
            String geneValueStr = tlSearchBean.getGeneTypeValues();
            //separated by comma,semicolon, space, tab or newline
            String[] geneValues = DMUtil.splitByDelims(geneValueStr, ",", ";", " ", "\n", "\t");

            String selectedGeneType = tlSearchBean.getSelectedGeneValueType();

            if (geneValues.length > 1) {
                if (StringUtils.equals("accession", selectedGeneType)) {
                    geneCriteria.add(Restrictions.in("ensgAccession", geneValues));
                }
                if (StringUtils.equals("symbol", selectedGeneType)) {
                    geneCriteria.add(Restrictions.in("displayName", geneValues));
                }
            }
            //only one value. we use wildcard to match it
            if (geneValues.length == 1) {
                if (StringUtils.equals("accession", selectedGeneType)) {
                    geneCriteria.add(Restrictions.like("ensgAccession", geneValues[0], MatchMode.ANYWHERE).ignoreCase());
                }
                if (StringUtils.equals("symbol", selectedGeneType)) {
                    geneCriteria.add(Restrictions.like("displayName", geneValues[0], MatchMode.ANYWHERE).ignoreCase());
                }
            }
        }
    }

    @SuppressWarnings("unchecked")
    private List<Long> getAllTrafficLight(TLSearchBean tlSearchBean) {

        String chromType = tlSearchBean.getSelectedChromType();
        int token = tlSearchBean.getCombinatedToken();
        long versionId = tlSearchBean.getSelectedVersion();
        boolean chromRegionProvided = tlSearchBean.isRegionProvided();
        boolean geneListProvided = tlSearchBean.isGeneListProvided();

        boolean typeLevelProvided = tlSearchBean.isTypeLevelProvided();

        Criteria tlCriteria = this.session().createCriteria(this.persistClass);

        if (typeLevelProvided) {
            addTypeColorLevelConditions(tlCriteria, tlSearchBean);
        }

        Criteria tlGeneCriteria = tlCriteria.createCriteria("tlGene");

        Criteria tpbVersionCriteria = tlCriteria.createCriteria("tpbVersion");
        tlGeneCriteria.add(Restrictions.eq("chromosome", chromType));

        //chromosome region provided
        if (chromRegionProvided) {
            addChromRegionConditions(tlGeneCriteria, tlSearchBean);
        }

        //gene list provided
        if (geneListProvided) {
            addGeneListConditions(tlGeneCriteria, tlSearchBean);
        }

        //tpb version
        tpbVersionCriteria.add(Restrictions.eq("id", versionId));

        tpbVersionCriteria.add(Restrictions.eq("trackToken", token));
        tlCriteria.setProjection(Projections.property("id"));
        return tlCriteria.list();
    }


    /**
     * {@inheritDoc}
     */
    public TLSumReporter getTLSumReporter(TLSearchBean tlSearchBean) {

        TLSumReporter tlSumReporter = new TLSumReporter();
        String chromType = tlSearchBean.getSelectedChromType();
        tlSumReporter.setChromosome(chromType);

        List<Long> tlIds = getAllTrafficLight(tlSearchBean);
        //PE
        int totalPeLevel4 = countEvidenceNumByLevel(tlIds, DataType.PE, 4);
        int totalPeLevel3 = countEvidenceNumByLevel(tlIds, DataType.PE, 3);
        int totalPeLevel2 = countEvidenceNumByLevel(tlIds, DataType.PE, 2);
        int totalPeLevel1 = countEvidenceNumByLevel(tlIds, DataType.PE, 1);
        TLTypeSumReporter peSumReporter = new TLTypeSumReporter();
        peSumReporter.setLevel4Num(totalPeLevel4);
        peSumReporter.setLevel3Num(totalPeLevel3);
        peSumReporter.setLevel2Num(totalPeLevel2);
        peSumReporter.setLevel1Num(totalPeLevel1);
        peSumReporter.setTpbDataType("PE");
        tlSumReporter.setTLTypeSumReporter(peSumReporter);

        //PE MS
        int totalPeMsLevel4 = countEvidenceNumByLevel(tlIds, DataType.PE_MS, 4);
        int totalPeMsLevel3 = countEvidenceNumByLevel(tlIds, DataType.PE_MS, 3);
        int totalPeMsLevel2 = countEvidenceNumByLevel(tlIds, DataType.PE_MS, 2);
        int totalPeMsLevel1 = countEvidenceNumByLevel(tlIds, DataType.PE_MS, 1);
        TLTypeSumReporter peMsSumReporter = new TLTypeSumReporter();
        peMsSumReporter.setLevel4Num(totalPeMsLevel4);
        peMsSumReporter.setLevel3Num(totalPeMsLevel3);
        peMsSumReporter.setLevel2Num(totalPeMsLevel2);
        peMsSumReporter.setLevel1Num(totalPeMsLevel1);
        peMsSumReporter.setTpbDataType("PE MS");
        tlSumReporter.setTLTypeSumReporter(peMsSumReporter);

        //PE MS ANN
        int totalPeMsAnnLevel4 = countEvidenceNumByLevel(tlIds, DataType.PE_MS_ANN, 4);
        int totalPeMsAnnLevel3 = countEvidenceNumByLevel(tlIds, DataType.PE_MS_ANN, 3);
        int totalPeMsAnnLevel2 = countEvidenceNumByLevel(tlIds, DataType.PE_MS_ANN, 2);
        int totalPeMsAnnLevel1 = countEvidenceNumByLevel(tlIds, DataType.PE_MS_ANN, 1);
        TLTypeSumReporter peMsAnnSumReporter = new TLTypeSumReporter();
        peMsAnnSumReporter.setLevel4Num(totalPeMsAnnLevel4);
        peMsAnnSumReporter.setLevel3Num(totalPeMsAnnLevel3);
        peMsAnnSumReporter.setLevel2Num(totalPeMsAnnLevel2);
        peMsAnnSumReporter.setLevel1Num(totalPeMsAnnLevel1);
        peMsAnnSumReporter.setTpbDataType("PE MS ANN");
        tlSumReporter.setTLTypeSumReporter(peMsAnnSumReporter);

        //PE MS PROB
        int totalPeMsProbLevel4 = countEvidenceNumByLevel(tlIds, DataType.PE_MS_PROB, 4);
        int totalPeMsProbLevel3 = countEvidenceNumByLevel(tlIds, DataType.PE_MS_PROB, 3);
        int totalPeMsProbLevel2 = countEvidenceNumByLevel(tlIds, DataType.PE_MS_PROB, 2);
        int totalPeMsProbLevel1 = countEvidenceNumByLevel(tlIds, DataType.PE_MS_PROB, 1);
        TLTypeSumReporter peMsProbSumReporter = new TLTypeSumReporter();
        peMsProbSumReporter.setLevel4Num(totalPeMsProbLevel4);
        peMsProbSumReporter.setLevel3Num(totalPeMsProbLevel3);
        peMsProbSumReporter.setLevel2Num(totalPeMsProbLevel2);
        peMsProbSumReporter.setLevel1Num(totalPeMsProbLevel1);
        peMsProbSumReporter.setTpbDataType("PE MS PROB");
        tlSumReporter.setTLTypeSumReporter(peMsProbSumReporter);

        //PE MS SAM
        int totalPeMsSamLevel4 = countEvidenceNumByLevel(tlIds, DataType.PE_MS_SAM, 4);
        int totalPeMsSamLevel3 = countEvidenceNumByLevel(tlIds, DataType.PE_MS_SAM, 3);
        int totalPeMsSamLevel2 = countEvidenceNumByLevel(tlIds, DataType.PE_MS_SAM, 2);
        int totalPeMsSamLevel1 = countEvidenceNumByLevel(tlIds, DataType.PE_MS_SAM, 1);
        TLTypeSumReporter peMsSamSumReporter = new TLTypeSumReporter();
        peMsSamSumReporter.setLevel4Num(totalPeMsSamLevel4);
        peMsSamSumReporter.setLevel3Num(totalPeMsSamLevel3);
        peMsSamSumReporter.setLevel2Num(totalPeMsSamLevel2);
        peMsSamSumReporter.setLevel1Num(totalPeMsSamLevel1);
        peMsSamSumReporter.setTpbDataType("PE MS SAM");
        tlSumReporter.setTLTypeSumReporter(peMsSamSumReporter);

        //PE ANTI
        int totalPeAntiLevel4 = countEvidenceNumByLevel(tlIds, DataType.PE_ANTI, 4);
        int totalPeAntiLevel3 = countEvidenceNumByLevel(tlIds, DataType.PE_ANTI, 3);
        int totalPeAntiLevel2 = countEvidenceNumByLevel(tlIds, DataType.PE_ANTI, 2);
        int totalPeAntiLevel1 = countEvidenceNumByLevel(tlIds, DataType.PE_ANTI, 1);
        TLTypeSumReporter peAntiSumReporter = new TLTypeSumReporter();
        peAntiSumReporter.setLevel4Num(totalPeAntiLevel4);
        peAntiSumReporter.setLevel3Num(totalPeAntiLevel3);
        peAntiSumReporter.setLevel2Num(totalPeAntiLevel2);
        peAntiSumReporter.setLevel1Num(totalPeAntiLevel1);
        peAntiSumReporter.setTpbDataType("PE ANTI");
        tlSumReporter.setTLTypeSumReporter(peAntiSumReporter);

        //PE ANTI ANN
        int totalPeAntiAnnLevel4 = countEvidenceNumByLevel(tlIds, DataType.PE_ANTI_ANN, 4);
        int totalPeAntiAnnLevel3 = countEvidenceNumByLevel(tlIds, DataType.PE_ANTI_ANN, 3);
        int totalPeAntiAnnLevel2 = countEvidenceNumByLevel(tlIds, DataType.PE_ANTI_ANN, 2);
        int totalPeAntiAnnLevel1 = countEvidenceNumByLevel(tlIds, DataType.PE_ANTI_ANN, 1);
        TLTypeSumReporter peAntiAnnSumReporter = new TLTypeSumReporter();
        peAntiAnnSumReporter.setLevel4Num(totalPeAntiAnnLevel4);
        peAntiAnnSumReporter.setLevel3Num(totalPeAntiAnnLevel3);
        peAntiAnnSumReporter.setLevel2Num(totalPeAntiAnnLevel2);
        peAntiAnnSumReporter.setLevel1Num(totalPeAntiAnnLevel1);
        peAntiAnnSumReporter.setTpbDataType("PE ANTI ANN");
        tlSumReporter.setTLTypeSumReporter(peAntiAnnSumReporter);

        //PE ANTI IHC
        int totalPeAntiIhcLevel4 = countEvidenceNumByLevel(tlIds, DataType.PE_ANTI_IHC, 4);
        int totalPeAntiIhcLevel3 = countEvidenceNumByLevel(tlIds, DataType.PE_ANTI_IHC, 3);
        int totalPeAntiIhcLevel2 = countEvidenceNumByLevel(tlIds, DataType.PE_ANTI_IHC, 2);
        int totalPeAntiIhcLevel1 = countEvidenceNumByLevel(tlIds, DataType.PE_ANTI_IHC, 1);
        TLTypeSumReporter peAntiIhcSumReporter = new TLTypeSumReporter();
        peAntiIhcSumReporter.setLevel4Num(totalPeAntiIhcLevel4);
        peAntiIhcSumReporter.setLevel3Num(totalPeAntiIhcLevel3);
        peAntiIhcSumReporter.setLevel2Num(totalPeAntiIhcLevel2);
        peAntiIhcSumReporter.setLevel1Num(totalPeAntiIhcLevel1);
        peAntiIhcSumReporter.setTpbDataType("PE ANTI IHC");
        tlSumReporter.setTLTypeSumReporter(peAntiIhcSumReporter);

        //PE ANTI IHC NORM
        int totalPeAntiIhcNormLevel4 = countEvidenceNumByLevel(tlIds, DataType.PE_ANTI_IHC_NORM, 4);
        int totalPeAntiIhcNormLevel3 = countEvidenceNumByLevel(tlIds, DataType.PE_ANTI_IHC_NORM, 3);
        int totalPeAntiIhcNormLevel2 = countEvidenceNumByLevel(tlIds, DataType.PE_ANTI_IHC_NORM, 2);
        int totalPeAntiIhcNormLevel1 = countEvidenceNumByLevel(tlIds, DataType.PE_ANTI_IHC_NORM, 1);
        TLTypeSumReporter peAntiIhcNormSumReporter = new TLTypeSumReporter();
        peAntiIhcNormSumReporter.setLevel4Num(totalPeAntiIhcNormLevel4);
        peAntiIhcNormSumReporter.setLevel3Num(totalPeAntiIhcNormLevel3);
        peAntiIhcNormSumReporter.setLevel2Num(totalPeAntiIhcNormLevel2);
        peAntiIhcNormSumReporter.setLevel1Num(totalPeAntiIhcNormLevel1);
        peAntiIhcNormSumReporter.setTpbDataType("PE ANTI IHC NORM");
        tlSumReporter.setTLTypeSumReporter(peAntiIhcNormSumReporter);

        //PE OTH
        int totalPeOthLevel4 = countEvidenceNumByLevel(tlIds, DataType.PE_OTH, 4);
        int totalPeOthLevel3 = countEvidenceNumByLevel(tlIds, DataType.PE_OTH, 3);
        int totalPeOthLevel2 = countEvidenceNumByLevel(tlIds, DataType.PE_OTH, 2);
        int totalPeOthLevel1 = countEvidenceNumByLevel(tlIds, DataType.PE_OTH, 1);
        TLTypeSumReporter peOthSumReporter = new TLTypeSumReporter();
        peOthSumReporter.setLevel4Num(totalPeOthLevel4);
        peOthSumReporter.setLevel3Num(totalPeOthLevel3);
        peOthSumReporter.setLevel2Num(totalPeOthLevel2);
        peOthSumReporter.setLevel1Num(totalPeOthLevel1);
        peOthSumReporter.setTpbDataType("PE OTH");
        tlSumReporter.setTLTypeSumReporter(peOthSumReporter);

        //PE OTH CUR
        int totalPeOthCurLevel4 = countEvidenceNumByLevel(tlIds, DataType.PE_OTH_CUR, 4);
        int totalPeOthCurLevel3 = countEvidenceNumByLevel(tlIds, DataType.PE_OTH_CUR, 3);
        int totalPeOthCurLevel2 = countEvidenceNumByLevel(tlIds, DataType.PE_OTH_CUR, 2);
        int totalPeOthCurLevel1 = countEvidenceNumByLevel(tlIds, DataType.PE_OTH_CUR, 1);
        TLTypeSumReporter peOthCurSumReporter = new TLTypeSumReporter();
        peOthCurSumReporter.setLevel4Num(totalPeOthCurLevel4);
        peOthCurSumReporter.setLevel3Num(totalPeOthCurLevel3);
        peOthCurSumReporter.setLevel2Num(totalPeOthCurLevel2);
        peOthCurSumReporter.setLevel1Num(totalPeOthCurLevel1);
        peOthCurSumReporter.setTpbDataType("PE OTH CUR");
        tlSumReporter.setTLTypeSumReporter(peOthCurSumReporter);

        //PTM
        int totalPtmLevel4 = countEvidenceNumByLevel(tlIds, DataType.PTM, 4);
        int totalPtmLevel3 = countEvidenceNumByLevel(tlIds, DataType.PTM, 3);
        int totalPtmLevel2 = countEvidenceNumByLevel(tlIds, DataType.PTM, 2);
        int totalPtmLevel1 = countEvidenceNumByLevel(tlIds, DataType.PTM, 1);
        TLTypeSumReporter ptmSumReporter = new TLTypeSumReporter();
        ptmSumReporter.setLevel4Num(totalPtmLevel4);
        ptmSumReporter.setLevel3Num(totalPtmLevel3);
        ptmSumReporter.setLevel2Num(totalPtmLevel2);
        ptmSumReporter.setLevel1Num(totalPtmLevel1);
        ptmSumReporter.setTpbDataType("PTM");
        tlSumReporter.setTLTypeSumReporter(ptmSumReporter);

        //PTM PHS
        int totalPtmPhsLevel4 = countEvidenceNumByLevel(tlIds, DataType.PTM_PHS, 4);
        int totalPtmPhsLevel3 = countEvidenceNumByLevel(tlIds, DataType.PTM_PHS, 3);
        int totalPtmPhsLevel2 = countEvidenceNumByLevel(tlIds, DataType.PTM_PHS, 2);
        int totalPtmPhsLevel1 = countEvidenceNumByLevel(tlIds, DataType.PTM_PHS, 1);
        TLTypeSumReporter ptmPhsSumReporter = new TLTypeSumReporter();
        ptmPhsSumReporter.setLevel4Num(totalPtmPhsLevel4);
        ptmPhsSumReporter.setLevel3Num(totalPtmPhsLevel3);
        ptmPhsSumReporter.setLevel2Num(totalPtmPhsLevel2);
        ptmPhsSumReporter.setLevel1Num(totalPtmPhsLevel1);
        ptmPhsSumReporter.setTpbDataType("PTM PHS");
        tlSumReporter.setTLTypeSumReporter(ptmPhsSumReporter);

        //PTM PHS SER
        int totalPtmPhsSerLevel4 = countEvidenceNumByLevel(tlIds, DataType.PTM_PHS_SER, 4);
        int totalPtmPhsSerLevel3 = countEvidenceNumByLevel(tlIds, DataType.PTM_PHS_SER, 3);
        int totalPtmPhsSerLevel2 = countEvidenceNumByLevel(tlIds, DataType.PTM_PHS_SER, 2);
        int totalPtmPhsSerLevel1 = countEvidenceNumByLevel(tlIds, DataType.PTM_PHS_SER, 1);
        TLTypeSumReporter ptmPhsSerSumReporter = new TLTypeSumReporter();
        ptmPhsSerSumReporter.setLevel4Num(totalPtmPhsSerLevel4);
        ptmPhsSerSumReporter.setLevel3Num(totalPtmPhsSerLevel3);
        ptmPhsSerSumReporter.setLevel2Num(totalPtmPhsSerLevel2);
        ptmPhsSerSumReporter.setLevel1Num(totalPtmPhsSerLevel1);
        ptmPhsSerSumReporter.setTpbDataType("PTM PHS SER");
        tlSumReporter.setTLTypeSumReporter(ptmPhsSerSumReporter);

        //PTM PHS THR
        int totalPtmPhsThrLevel4 = countEvidenceNumByLevel(tlIds, DataType.PTM_PHS_THR, 4);
        int totalPtmPhsThrLevel3 = countEvidenceNumByLevel(tlIds, DataType.PTM_PHS_THR, 3);
        int totalPtmPhsThrLevel2 = countEvidenceNumByLevel(tlIds, DataType.PTM_PHS_THR, 2);
        int totalPtmPhsThrLevel1 = countEvidenceNumByLevel(tlIds, DataType.PTM_PHS_THR, 1);
        TLTypeSumReporter ptmPhsThrSumReporter = new TLTypeSumReporter();
        ptmPhsThrSumReporter.setLevel4Num(totalPtmPhsThrLevel4);
        ptmPhsThrSumReporter.setLevel3Num(totalPtmPhsThrLevel3);
        ptmPhsThrSumReporter.setLevel2Num(totalPtmPhsThrLevel2);
        ptmPhsThrSumReporter.setLevel1Num(totalPtmPhsThrLevel1);
        ptmPhsThrSumReporter.setTpbDataType("PTM PHS THR");
        tlSumReporter.setTLTypeSumReporter(ptmPhsThrSumReporter);

        //PTM PHS TYR
        int totalPtmPhsTyrLevel4 = countEvidenceNumByLevel(tlIds, DataType.PTM_PHS_TYR, 4);
        int totalPtmPhsTyrLevel3 = countEvidenceNumByLevel(tlIds, DataType.PTM_PHS_TYR, 3);
        int totalPtmPhsTyrLevel2 = countEvidenceNumByLevel(tlIds, DataType.PTM_PHS_TYR, 2);
        int totalPtmPhsTyrLevel1 = countEvidenceNumByLevel(tlIds, DataType.PTM_PHS_TYR, 1);
        TLTypeSumReporter ptmPhsTyrSumReporter = new TLTypeSumReporter();
        ptmPhsTyrSumReporter.setLevel4Num(totalPtmPhsTyrLevel4);
        ptmPhsTyrSumReporter.setLevel3Num(totalPtmPhsTyrLevel3);
        ptmPhsTyrSumReporter.setLevel2Num(totalPtmPhsTyrLevel2);
        ptmPhsTyrSumReporter.setLevel1Num(totalPtmPhsTyrLevel1);
        ptmPhsTyrSumReporter.setTpbDataType("PTM PHS TYR");
        tlSumReporter.setTLTypeSumReporter(ptmPhsTyrSumReporter);


        //PTM ACE
        int totalPtmAceLevel4 = countEvidenceNumByLevel(tlIds, DataType.PTM_ACE, 4);
        int totalPtmAceLevel3 = countEvidenceNumByLevel(tlIds, DataType.PTM_ACE, 3);
        int totalPtmAceLevel2 = countEvidenceNumByLevel(tlIds, DataType.PTM_ACE, 2);
        int totalPtmAceLevel1 = countEvidenceNumByLevel(tlIds, DataType.PTM_ACE, 1);
        TLTypeSumReporter ptmAceSumReporter = new TLTypeSumReporter();
        ptmAceSumReporter.setLevel4Num(totalPtmAceLevel4);
        ptmAceSumReporter.setLevel3Num(totalPtmAceLevel3);
        ptmAceSumReporter.setLevel2Num(totalPtmAceLevel2);
        ptmAceSumReporter.setLevel1Num(totalPtmAceLevel1);
        ptmAceSumReporter.setTpbDataType("PTM ACE");
        tlSumReporter.setTLTypeSumReporter(ptmAceSumReporter);

        //PTM ACE LYS
        int totalPtmAceLysLevel4 = countEvidenceNumByLevel(tlIds, DataType.PTM_ACE_LYS, 4);
        int totalPtmAceLysLevel3 = countEvidenceNumByLevel(tlIds, DataType.PTM_ACE_LYS, 3);
        int totalPtmAceLysLevel2 = countEvidenceNumByLevel(tlIds, DataType.PTM_ACE_LYS, 2);
        int totalPtmAceLysLevel1 = countEvidenceNumByLevel(tlIds, DataType.PTM_ACE_LYS, 1);
        TLTypeSumReporter ptmAceLysSumReporter = new TLTypeSumReporter();
        ptmAceLysSumReporter.setLevel4Num(totalPtmAceLysLevel4);
        ptmAceLysSumReporter.setLevel3Num(totalPtmAceLysLevel3);
        ptmAceLysSumReporter.setLevel2Num(totalPtmAceLysLevel2);
        ptmAceLysSumReporter.setLevel1Num(totalPtmAceLysLevel1);
        ptmAceLysSumReporter.setTpbDataType("PTM ACE LYS");
        tlSumReporter.setTLTypeSumReporter(ptmAceLysSumReporter);

        //PTM ACE NTA
        int totalPtmAceNtaLevel4 = countEvidenceNumByLevel(tlIds, DataType.PTM_ACE_NTA, 4);
        int totalPtmAceNtaLevel3 = countEvidenceNumByLevel(tlIds, DataType.PTM_ACE_NTA, 3);
        int totalPtmAceNtaLevel2 = countEvidenceNumByLevel(tlIds, DataType.PTM_ACE_NTA, 2);
        int totalPtmAceNtaLevel1 = countEvidenceNumByLevel(tlIds, DataType.PTM_ACE_NTA, 1);
        TLTypeSumReporter ptmAceNtaSumReporter = new TLTypeSumReporter();
        ptmAceNtaSumReporter.setLevel4Num(totalPtmAceNtaLevel4);
        ptmAceNtaSumReporter.setLevel3Num(totalPtmAceNtaLevel3);
        ptmAceNtaSumReporter.setLevel2Num(totalPtmAceNtaLevel2);
        ptmAceNtaSumReporter.setLevel1Num(totalPtmAceNtaLevel1);
        ptmAceNtaSumReporter.setTpbDataType("PTM PHS THR");
        tlSumReporter.setTLTypeSumReporter(ptmAceNtaSumReporter);

        //TE
        int totalTeLevel4 = countEvidenceNumByLevel(tlIds, DataType.TE, 4);
        int totalTeLevel3 = countEvidenceNumByLevel(tlIds, DataType.TE, 3);
        int totalTeLevel2 = countEvidenceNumByLevel(tlIds, DataType.TE, 2);
        int totalTeLevel1 = countEvidenceNumByLevel(tlIds, DataType.TE, 1);
        TLTypeSumReporter teSumReporter = new TLTypeSumReporter();
        teSumReporter.setLevel4Num(totalTeLevel4);
        teSumReporter.setLevel3Num(totalTeLevel3);
        teSumReporter.setLevel2Num(totalTeLevel2);
        teSumReporter.setLevel1Num(totalTeLevel1);
        teSumReporter.setTpbDataType("TE");
        tlSumReporter.setTLTypeSumReporter(teSumReporter);

        //TE MA
        int totalTeMaLevel4 = countEvidenceNumByLevel(tlIds, DataType.TE_MA, 4);
        int totalTeMaLevel3 = countEvidenceNumByLevel(tlIds, DataType.TE_MA, 3);
        int totalTeMaLevel2 = countEvidenceNumByLevel(tlIds, DataType.TE_MA, 2);
        int totalTeMaLevel1 = countEvidenceNumByLevel(tlIds, DataType.TE_MA, 1);
        TLTypeSumReporter teMaSumReporter = new TLTypeSumReporter();
        teMaSumReporter.setLevel4Num(totalTeMaLevel4);
        teMaSumReporter.setLevel3Num(totalTeMaLevel3);
        teMaSumReporter.setLevel2Num(totalTeMaLevel2);
        teMaSumReporter.setLevel1Num(totalTeMaLevel1);
        teMaSumReporter.setTpbDataType("TE MA");
        tlSumReporter.setTLTypeSumReporter(teMaSumReporter);

        //TE MA PROP
        int totalTeMaPropLevel4 = countEvidenceNumByLevel(tlIds, DataType.TE_MA_PROP, 4);
        int totalTeMaPropLevel3 = countEvidenceNumByLevel(tlIds, DataType.TE_MA_PROP, 3);
        int totalTeMaPropLevel2 = countEvidenceNumByLevel(tlIds, DataType.TE_MA_PROP, 2);
        int totalTeMaPropLevel1 = countEvidenceNumByLevel(tlIds, DataType.TE_MA_PROP, 1);
        TLTypeSumReporter teMaPropSumReporter = new TLTypeSumReporter();
        teMaPropSumReporter.setLevel4Num(totalTeMaPropLevel4);
        teMaPropSumReporter.setLevel3Num(totalTeMaPropLevel3);
        teMaPropSumReporter.setLevel2Num(totalTeMaPropLevel2);
        teMaPropSumReporter.setLevel1Num(totalTeMaPropLevel1);
        teMaPropSumReporter.setTpbDataType("TE MA PROP");
        tlSumReporter.setTLTypeSumReporter(teMaPropSumReporter);


        //TE OTH
        int totalTeOthLevel4 = countEvidenceNumByLevel(tlIds, DataType.TE_OTH, 4);
        int totalTeOthLevel3 = countEvidenceNumByLevel(tlIds, DataType.TE_OTH, 3);
        int totalTeOthLevel2 = countEvidenceNumByLevel(tlIds, DataType.TE_OTH, 2);
        int totalTeOthLevel1 = countEvidenceNumByLevel(tlIds, DataType.TE_OTH, 1);
        TLTypeSumReporter teOthSumReporter = new TLTypeSumReporter();
        teOthSumReporter.setLevel4Num(totalTeOthLevel4);
        teOthSumReporter.setLevel3Num(totalTeOthLevel3);
        teOthSumReporter.setLevel2Num(totalTeOthLevel2);
        teOthSumReporter.setLevel1Num(totalTeOthLevel1);
        teOthSumReporter.setTpbDataType("TE OTH");
        tlSumReporter.setTLTypeSumReporter(teOthSumReporter);

        //TE OTH CUR
        int totalTeOthCurLevel4 = countEvidenceNumByLevel(tlIds, DataType.TE_OTH_CUR, 4);
        int totalTeOthCurLevel3 = countEvidenceNumByLevel(tlIds, DataType.TE_OTH_CUR, 3);
        int totalTeOthCurLevel2 = countEvidenceNumByLevel(tlIds, DataType.TE_OTH_CUR, 2);
        int totalTeOthCurLevel1 = countEvidenceNumByLevel(tlIds, DataType.TE_OTH_CUR, 1);
        TLTypeSumReporter teOthCurSumReporter = new TLTypeSumReporter();
        teOthCurSumReporter.setLevel4Num(totalTeOthCurLevel4);
        teOthCurSumReporter.setLevel3Num(totalTeOthCurLevel3);
        teOthCurSumReporter.setLevel2Num(totalTeOthCurLevel2);
        teOthCurSumReporter.setLevel1Num(totalTeOthCurLevel1);
        teOthCurSumReporter.setTpbDataType("TE OTH CUR");
        tlSumReporter.setTLTypeSumReporter(teOthCurSumReporter);


        return tlSumReporter;
    }

    private int countEvidenceNumByLevel(List<Long> tlIds, DataType dataType, int level) {
        Criteria tlCrt = this.session().createCriteria(this.persistClass);
        tlCrt.add(Restrictions.in("id", tlIds));
        String tlColor = null;

        //PE
        if (DataType.PE.equals(dataType)) {
            tlColor = "tlPEColor";
        }
        if (DataType.PE_MS.equals(dataType)) {
            tlColor = "tlPEMSColor";
        }
        if (DataType.PE_MS_ANN.equals(dataType)) {
            tlColor = "tlPEMSANNColor";
        }
        if (DataType.PE_MS_PROB.equals(dataType)) {
            tlColor = "tlPEMSPROBColor";
        }
        if (DataType.PE_MS_SAM.equals(dataType)) {
            tlColor = "tlPEMSSAMColor";
        }
        if (DataType.PE_ANTI.equals(dataType)) {
            tlColor = "tlPEANTIColor";
        }
        if (DataType.PE_ANTI_ANN.equals(dataType)) {
            tlColor = "tlPEANTIANNColor";
        }
        if (DataType.PE_ANTI_IHC.equals(dataType)) {
            tlColor = "tlPEANTIIHCColor";
        }
        if (DataType.PE_ANTI_IHC_NORM.equals(dataType)) {
            tlColor = "tlPEANTIIHCNORMColor";
        }
        if (DataType.PE_OTH.equals(dataType)) {
            tlColor = "tlPEOTHColor";
        }
        if (DataType.PE_OTH_CUR.equals(dataType)) {
            tlColor = "tlPEOTHCURColor";
        }
        //PTM
        if (DataType.PTM.equals(dataType)) {
            tlColor = "tlPTMColor";
        }
        if (DataType.PTM_PHS.equals(dataType)) {
            tlColor = "tlPTMPHSColor";
        }
        if (DataType.PTM_PHS_SER.equals(dataType)) {
            tlColor = "tlPTMPHSSERColor";
        }
        if (DataType.PTM_PHS_THR.equals(dataType)) {
            tlColor = "tlPTMPHSTHRColor";
        }
        if (DataType.PTM_PHS_TYR.equals(dataType)) {
            tlColor = "tlPTMPHSTYRColor";
        }
        if (DataType.PTM_ACE.equals(dataType)) {
            tlColor = "tlPTMACEColor";
        }
        if (DataType.PTM_ACE_LYS.equals(dataType)) {
            tlColor = "tlPTMACELYSColor";
        }
        if (DataType.PTM_ACE_NTA.equals(dataType)) {
            tlColor = "tlPTMACENTAColor";
        }

        //TE
        if (DataType.TE.equals(dataType)) {
            tlColor = "tlTEColor";
        }
        //TE MA
        if (DataType.TE_MA.equals(dataType)) {
            tlColor = "tlTEMAColor";
        }
        //TE MA PROP
        if (DataType.TE_MA_PROP.equals(dataType)) {
            tlColor = "tlTEMAPROPColor";
        }
        //TE OTH
        if (DataType.TE_OTH.equals(dataType)) {
            tlColor = "tlTEOTHColor";
        }
        //TE OTH CUR
        if (DataType.TE_OTH_CUR.equals(dataType)) {
            tlColor = "tlTEOTHCURColor";
        }

        Criteria levelCriteria = tlCrt.createCriteria(tlColor);
        levelCriteria.add((Restrictions.eq("colorLevel", level)));
        tlCrt.setProjection(Projections.rowCount());
        return ((Long) tlCrt.uniqueResult()).intValue();
    }

    @SuppressWarnings("unchecked")
    @Override
    public List<TrafficLight> getTrafficLightsByChromAndTPBVersion(ChromType chromType, TPBVersion tpbVersion) {
        Criteria tlQCriteria = this.session().createCriteria(this.persistClass);
        Criteria tlgQCriteria = tlQCriteria.createCriteria("tlGene");
        Criteria tpbVQCriteria = tlQCriteria.createCriteria("tpbVersion");
        tlgQCriteria.add(Restrictions.eq("chromosome", chromType.chm()));
        tpbVQCriteria.add(Restrictions.eq("id", tpbVersion.getId()));
        tpbVQCriteria.add(Restrictions.eq("trackToken", tpbVersion.getTrackToken()));
        return tlQCriteria.list();
    }
}
