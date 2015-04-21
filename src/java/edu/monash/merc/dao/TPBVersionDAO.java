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
import edu.monash.merc.domain.DSVersion;
import edu.monash.merc.domain.TPBVersion;
import edu.monash.merc.dto.MaxDsTPBVersion;
import edu.monash.merc.repository.ITPBVersionRep;
import edu.monash.merc.system.version.TLVersionTrack;
import org.hibernate.Criteria;
import org.hibernate.Query;
import org.hibernate.criterion.Order;
import org.hibernate.criterion.Projections;
import org.hibernate.criterion.Restrictions;
import org.hibernate.transform.Transformers;
import org.springframework.beans.factory.config.BeanDefinition;
import org.springframework.context.annotation.Scope;
import org.springframework.stereotype.Repository;

import java.util.List;

/**
 * TPBVersionDAO class which provides the database operations for TPBVersion object.
 *
 * @author Simon Yu
 *         <p/>
 *         Email: xiaoming.yu@monash.edu
 * @version 1.0
 * @since 1.0
 *        <p/>
 *        Date: 25/05/12 10:34 AM
 */
@Scope(BeanDefinition.SCOPE_PROTOTYPE)
@Repository
public class TPBVersionDAO extends HibernateGenericDAO<TPBVersion> implements ITPBVersionRep {

    /**
     * {@inheritDoc}
     */
    public TPBVersion getCurrentVersion(ChromType chromType, int trackToken) {
        Criteria qCriteria = this.session().createCriteria(this.persistClass);
        qCriteria.add(Restrictions.eq("chromosome", chromType.chm()).ignoreCase());
        qCriteria.add(Restrictions.eq("trackToken", trackToken));
        qCriteria.addOrder(Order.desc("versionNo")).setMaxResults(1);
        return (TPBVersion) qCriteria.uniqueResult();
    }

    /**
     * {@inheritDoc}
     */
    public void deleteTPBVersionById(long id) {
        String del_hql = "DELETE FROM " + this.persistClass.getSimpleName() + " AS tpbv WHERE tpbv.id = :id";
        Query query = this.session().createQuery(del_hql);
        query.setLong("id", id);
        query.executeUpdate();
    }

    /**
     * {@inheritDoc}
     */
    public boolean checkTPBVersionAvailable(ChromType chromType, TLVersionTrack tlVersionTrack) {
        Criteria qCriteria = this.session().createCriteria(this.persistClass);
        qCriteria.add(Restrictions.eq("chromosome", chromType.chm()).ignoreCase());

        int trackToken = tlVersionTrack.getTrackToken();
        qCriteria.add(Restrictions.eq("trackToken", trackToken));

        DSVersion nxVersion = tlVersionTrack.getNxDsVersion();
        if (nxVersion != null) {
            Criteria nxCriteria = qCriteria.createCriteria("nxVersion");
            nxCriteria.add(Restrictions.eq("id", nxVersion.getId()));
        }

        DSVersion gpmVersion = tlVersionTrack.getGpmDsVersion();
        if (gpmVersion != null) {
            Criteria gpmCriteria = qCriteria.createCriteria("gpmVersion");
            gpmCriteria.add(Restrictions.eq("id", gpmVersion.getId()));
        }

        DSVersion gpmPstyVersion = tlVersionTrack.getGpmPstyDsVersion();
        if (gpmPstyVersion != null) {
            Criteria gpmPstyCriteria = qCriteria.createCriteria("gpmPstyVersion");
            gpmPstyCriteria.add(Restrictions.eq("id", gpmPstyVersion.getId()));
        }

        DSVersion gpmLysVersion = tlVersionTrack.getGpmLysDsVersion();
        if (gpmLysVersion != null) {
            Criteria gpmLysCriteria = qCriteria.createCriteria("gpmLysVersion");
            gpmLysCriteria.add(Restrictions.eq("id", gpmLysVersion.getId()));
        }

        DSVersion gpmNtaVersion = tlVersionTrack.getGpmNtaDsVersion();
        if (gpmNtaVersion != null) {
            Criteria gpmNtaCriteria = qCriteria.createCriteria("gpmNtaVersion");
            gpmNtaCriteria.add(Restrictions.eq("id", gpmNtaVersion.getId()));
        }

        DSVersion hpaVersion = tlVersionTrack.getHpaDsVersion();
        if (hpaVersion != null) {
            Criteria hpaCriteria = qCriteria.createCriteria("hpaVersion");
            hpaCriteria.add(Restrictions.eq("id", hpaVersion.getId()));
        }

        DSVersion bcBcHgu133aVersion = tlVersionTrack.getBcHgu133aDsVersion();
        if (bcBcHgu133aVersion != null) {
            Criteria bcHgu133aVersionCriteria = qCriteria.createCriteria("bcHgu133aVersion");
            bcHgu133aVersionCriteria.add(Restrictions.eq("id", bcBcHgu133aVersion.getId()));
        }

        DSVersion bcBcHgu133p2Version = tlVersionTrack.getBcHgu133p2DsVersion();
        if (bcBcHgu133p2Version != null) {
            Criteria bcHgu133p2VersionCriteria = qCriteria.createCriteria("bcHgu133p2Version");
            bcHgu133p2VersionCriteria.add(Restrictions.eq("id", bcBcHgu133p2Version.getId()));
        }

        long num = (Long) qCriteria.setProjection(Projections.rowCount()).uniqueResult();
        if (num >= 1) {
            return true;
        } else {
            return false;
        }
    }

    /**
     * {@inheritDoc}
     */
    public TPBVersion getCurrentTPBVersionByChromTypeTrackToken(ChromType chromType, int trackToken) {
        Criteria qCriteria = this.session().createCriteria(this.persistClass);
        qCriteria.add(Restrictions.eq("chromosome", chromType.chm()).ignoreCase());
        qCriteria.add(Restrictions.eq("trackToken", trackToken));
        qCriteria.addOrder(Order.desc("versionNo")).setMaxResults(1);
        return (TPBVersion) qCriteria.uniqueResult();
    }

    /**
     * {@inheritDoc}
     */
    @SuppressWarnings("unchecked")
    public List<TPBVersion> getAllTPBVersionByChromTypeTrackToken(ChromType chromType, int trackToken) {
        Criteria qCriteria = this.session().createCriteria(this.persistClass);
        qCriteria.add(Restrictions.eq("chromosome", chromType.chm()).ignoreCase());
        qCriteria.add(Restrictions.eq("trackToken", trackToken));
        qCriteria.addOrder(Order.asc("versionNo"));
        return qCriteria.list();
    }

    /**
     * {@inheritDoc}
     */
    @SuppressWarnings("unchecked")
    public List<TPBVersion> getAllTPBVersions() {
        Criteria tpbCriteria = this.session().createCriteria(this.persistClass);
        return tpbCriteria.list();
    }

    /**
     * {@inheritDoc}
     */
    @SuppressWarnings("unchecked")
    public List<MaxDsTPBVersion> getAllChromosomeTPBVersionByMaxCombinatedDs() {
        Criteria tpbVCriteria = this.session().createCriteria(this.persistClass);
        tpbVCriteria.setProjection(Projections.projectionList().add(Projections.groupProperty("chromosome").as("chromosome")).add(Projections.max("id").as("id")).add(Projections.max("trackToken").as("trackToken")).add(Projections.max("versionNo").as("versionNo")));
        List<MaxDsTPBVersion> maxDsTPBVersions = tpbVCriteria.setResultTransformer(Transformers.aliasToBean(MaxDsTPBVersion.class)).list();
//        System.out.println("======= max ds tpb versions size: " + maxDsTPBVersions.size());
//        for(MaxDsTPBVersion maxDsTPBVersion : maxDsTPBVersions){
//            System.out.println("========= > " +maxDsTPBVersion.getId() + " -- " + maxDsTPBVersion.getChromosome() + " -- " + maxDsTPBVersion.getTrackToken() + " -- " + maxDsTPBVersion.getVersionNo());
//        }
        return maxDsTPBVersions;
    }
}
