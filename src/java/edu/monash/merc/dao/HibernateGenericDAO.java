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

import edu.monash.merc.repository.IRepository;
import org.hibernate.Session;
import org.hibernate.SessionFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.config.BeanDefinition;
import org.springframework.context.annotation.Scope;
import org.springframework.stereotype.Repository;

import java.lang.reflect.ParameterizedType;
import java.util.List;

/**
 * Base Hibernate DAO class which provides database access functionality.
 *
 * @author Simon Yu
 *         <p/>
 *         Email: xiaoming.yu@monash.edu
 * @version 1.0
 * @since 1.0
 *        <p/>
 *        Date: 3/01/12 3:39 PM
 */
@Scope(BeanDefinition.SCOPE_PROTOTYPE)
@Repository
public class HibernateGenericDAO<T> implements IRepository<T> {
    protected Class<T> persistClass;

    @Autowired
    private SessionFactory sessionFactory;

    @SuppressWarnings("unchecked")
    public HibernateGenericDAO(SessionFactory sessionFactory) {
        this.persistClass = (Class<T>) ((ParameterizedType) this.getClass().getGenericSuperclass()).getActualTypeArguments()[0];
        this.sessionFactory = sessionFactory;
    }

    @SuppressWarnings("unchecked")
    public HibernateGenericDAO() {
        this.persistClass = (Class<T>) ((ParameterizedType) this.getClass().getGenericSuperclass()).getActualTypeArguments()[0];
    }

    public void setSessionFactory(SessionFactory sessionFactory) {
        this.sessionFactory = sessionFactory;
    }

    public Session session() {
        return this.sessionFactory.getCurrentSession();
    }

    /**
     * {@inheritDoc}
     */
    public void add(T entity) {
        this.session().save(entity);
    }

    /**
     * {@inheritDoc}
     */
    @SuppressWarnings("unchecked")
    public T get(long id) {
        return (T) this.session().get(this.persistClass, id);
    }

    /**
     * {@inheritDoc}
     */
    public void remove(T entity) {
        this.session().delete(entity);
    }

    /**
     * {@inheritDoc}
     */
    public void update(T entity) {
        this.session().update(entity);
    }

    /**
     * {@inheritDoc}
     */
    public void merge(T entity) {
        this.session().merge(entity);
    }

    /**
     * {@inheritDoc}
     */
    public int saveAll(List<T> entities) {
        int insertedCount = 0;
        for (int i = 0; i < entities.size(); i++) {
            add(entities.get(i));
            if (++insertedCount % 20 == 0) {
                flushAndClear();
            }
        }
        flushAndClear();
        return insertedCount;
    }

    /**
     * {@inheritDoc}
     */
    public int updateAll(List<T> entities) {
        int updatedCount = 0;
        for (int i = 0; i < entities.size(); i++) {
            update(entities.get(i));
            if (++updatedCount % 20 == 0) {
                flushAndClear();
            }
        }
        flushAndClear();
        return updatedCount;
    }

    /**
     * clear all the caches in the current session.
     */
    protected void flushAndClear() {
        if (this.session().isDirty()) {
            this.session().flush();
            this.session().clear();
        }
    }
}
