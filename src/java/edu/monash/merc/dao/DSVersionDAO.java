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
import edu.monash.merc.common.name.DbAcType;
import edu.monash.merc.domain.DSVersion;
import edu.monash.merc.dto.DBVersionBean;
import edu.monash.merc.repository.IDSVersionRep;
import org.apache.commons.lang.StringUtils;
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
 * DSVersionDAO class which provides the database operations for DSVersion object.
 *
 * @author Simon Yu
 *         <p/>
 *         Email: xiaoming.yu@monash.edu
 * @version 1.0
 * @since 1.0
 *        <p/>
 *        Date: 15/05/12 4:01 PM
 */
@Scope(BeanDefinition.SCOPE_PROTOTYPE)
@Repository
public class DSVersionDAO extends HibernateGenericDAO<DSVersion> implements IDSVersionRep {

    /**
     * {@inheritDoc}
     */
    public void deleteDsVersionById(long id) {
        String del_hql = "DELETE FROM " + this.persistClass.getSimpleName() + " AS dsv WHERE dsv.id = :id";
        Query query = this.session().createQuery(del_hql);
        query.setLong("id", id);
        query.executeUpdate();
    }

    /**
     * {@inheritDoc}
     */
    public DSVersion getCurrentDSVersionByChromDbs(DbAcType dbAcType, ChromType chromType) {
        Criteria qCriteria = this.session().createCriteria(this.persistClass);
        qCriteria.add(Restrictions.eq("dbSource", dbAcType.type())).add(Restrictions.eq("chromosome", chromType.chm()).ignoreCase());
        qCriteria.addOrder(Order.desc("versionNo")).setMaxResults(1);
        return (DSVersion) qCriteria.uniqueResult();
    }

    /**
     * {@inheritDoc}
     */
    public boolean checkUpToDate(DbAcType dbAcType, ChromType chromType, String fileName, String timeToken) {
        Criteria criteria = this.session().createCriteria(this.persistClass);
        criteria.setProjection(Projections.rowCount());
        if (dbAcType != null) {
            criteria.add(Restrictions.eq("dbSource", dbAcType.type()));
        }
        if (chromType != null) {
            criteria.add(Restrictions.eq("chromosome", chromType.chm()).ignoreCase());
        }
        if (StringUtils.isNotBlank(fileName)) {
            criteria.add(Restrictions.eq("fileName", fileName).ignoreCase());
        }
        if (StringUtils.isNotBlank(timeToken)) {
            criteria.add(Restrictions.eq("timestampToken", timeToken));
        }
        long num = (Long) criteria.uniqueResult();
        if (num >= 1) {
            return true;
        } else {
            return false;
        }
    }

    /**
     * {@inheritDoc}
     */
    @SuppressWarnings("unchecked")
    public List<DBVersionBean> getLatestDBSVersionByChromosome(ChromType chromType) {
        Criteria dsvCriteria = this.session().createCriteria(this.persistClass);
        dsvCriteria.add(Restrictions.eq("chromosome", chromType.chm()));
        dsvCriteria.setProjection(Projections.projectionList().add(Projections.groupProperty("dbSource").as("dbSource")).add(Projections.max("id").as("id")).add(Projections.max("createdTime").as("createdTime")).add(Projections.max("versionNo").as("versionNo")));
        dsvCriteria.addOrder(Order.asc("dbSource"));
        return dsvCriteria.setResultTransformer(Transformers.aliasToBean(DBVersionBean.class)).list();
    }
}
