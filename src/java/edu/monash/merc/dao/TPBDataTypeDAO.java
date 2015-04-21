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

import edu.monash.merc.domain.TPBDataType;
import edu.monash.merc.repository.ITPBDataTypeRep;
import org.hibernate.Criteria;
import org.hibernate.Query;
import org.hibernate.criterion.Projections;
import org.hibernate.criterion.Restrictions;
import org.springframework.beans.factory.config.BeanDefinition;
import org.springframework.context.annotation.Scope;
import org.springframework.stereotype.Repository;

import java.util.List;

/**
 * TPBDataTypeDAO class which provides the database operations for TPBDataType object.
 *
 * @author Simon Yu
 *         <p/>
 *         Email: xiaoming.yu@monash.edu
 * @version 1.0
 * @since 1.0
 *        <p/>
 *        Date: 19/04/12 2:05 PM
 */
@Scope(BeanDefinition.SCOPE_PROTOTYPE)
@Repository
public class TPBDataTypeDAO extends HibernateGenericDAO<TPBDataType> implements ITPBDataTypeRep {

    /**
     * {@inheritDoc}
     */
    public TPBDataType getTPBDataTypeByTypeName(String type) {
        Criteria criteria = this.session().createCriteria(this.persistClass);
        criteria.add(Restrictions.eq("dataType", type));
        return (TPBDataType) criteria.uniqueResult();
    }

    /**
     * {@inheritDoc}
     */
    public void deleteTPBDataTypeById(long id) {
        String del_hql = "DELETE FROM " + this.persistClass.getSimpleName() + " AS hpbdtype WHERE hpbdtype.id = :id";
        Query query = this.session().createQuery(del_hql);
        query.setLong("id", id);
        query.executeUpdate();
    }

    /**
     * {@inheritDoc}
     */
    @SuppressWarnings("unchecked")
    public List<TPBDataType> getSubTPBDataType(long id) {

        Criteria criteria = this.session().createCriteria(this.persistClass);
        Criteria pCriteria = criteria.createCriteria("parentDataType");
        pCriteria.add(Restrictions.eq("id", id));
        return criteria.list();
    }

    /**
     * {@inheritDoc}
     */
    @SuppressWarnings("unchecked")
    public List<TPBDataType> getSubTPBDataType(String dataType) {
        Criteria criteria = this.session().createCriteria(this.persistClass);
        Criteria pCriteria = criteria.createCriteria("parentDataType");
        pCriteria.add(Restrictions.eq("dataType", dataType));
        return criteria.list();
    }

    /**
     * {@inheritDoc}
     */
    public boolean isLastLevelType(String dataType) {
        Criteria criteria = this.session().createCriteria(this.persistClass);
        Criteria pCriteria = criteria.createCriteria("parentDataType");
        pCriteria.add(Restrictions.eq("dataType", dataType));
        criteria.setProjection(Projections.rowCount());
        long total = (Long) criteria.uniqueResult();
        if (total < 1) {
            return true;
        } else {
            return false;
        }
    }

    /**
     * {@inheritDoc}
     */
    public boolean isLastLevelType(long id) {
        Criteria criteria = this.session().createCriteria(this.persistClass);
        Criteria pCriteria = criteria.createCriteria("parentDataType");
        pCriteria.add(Restrictions.eq("id", id));
        criteria.setProjection(Projections.rowCount());
        long total = (Long) criteria.uniqueResult();
        if (total < 1) {
            return true;
        } else {
            return false;
        }
    }
}
