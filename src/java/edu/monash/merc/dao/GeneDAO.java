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
import edu.monash.merc.domain.Accession;
import edu.monash.merc.domain.Gene;
import edu.monash.merc.repository.IGeneRep;
import org.hibernate.Criteria;
import org.hibernate.Query;
import org.hibernate.criterion.Projections;
import org.hibernate.criterion.Restrictions;
import org.springframework.beans.factory.config.BeanDefinition;
import org.springframework.context.annotation.Scope;
import org.springframework.stereotype.Repository;

import java.util.Date;
import java.util.List;

/**
 * GeneDAO class which provides the database operations for Gene object.
 *
 * @author Simon Yu
 *         <p/>
 *         Email: xiaoming.yu@monash.edu
 * @version 1.0
 * @since 1.0
 *        <p/>
 *        Date: 21/05/12 12:20 PM
 */
@Scope(BeanDefinition.SCOPE_PROTOTYPE)
@Repository
public class GeneDAO extends HibernateGenericDAO<Gene> implements IGeneRep {

    /**
     * {@inheritDoc}
     */
    public void deleteGeneById(long id) {
        String del_hql = "DELETE FROM " + this.persistClass.getSimpleName() + " AS g WHERE g.id = :id";
        Query query = this.session().createQuery(del_hql);
        query.setLong("id", id);
        query.executeUpdate();
    }

    /**
     * {@inheritDoc}
     */
    public Gene getGeneByEnsgAndDbVersion(String ensgAccession, String dbSource, Date versionTime) {
        Criteria qCriteria = this.session().createCriteria(this.persistClass);
        Criteria dbsCriteria = qCriteria.createCriteria("dbSource");
        qCriteria.add(Restrictions.eq("ensgAccession", ensgAccession).ignoreCase()).add(Restrictions.eq("createdTime", versionTime));
        dbsCriteria.add(Restrictions.eq("dbName", dbSource).ignoreCase());
        return (Gene) qCriteria.uniqueResult();
    }


//    @SuppressWarnings("unchecked")
//    public List<Gene> getGeneByDBSChromVersion(DbAcType dbAcType, ChromType chromType, Date versionTime) {
//        // String gQueryStr = "SELECT DISTINCT(g) FROM Gene g JOIN g.accessions acs JOIN acs.dbSource dbs WHERE dbs.dbName =:dbName AND g.createdTime =:createdTime AND g.chromosome = :chromosome"; //
//        String gQueryStr = "SELECT g FROM Gene g JOIN g.dbSource dbs WHERE dbs.dbName = :dbName AND g.createdTime = :createdTime AND g.chromosome = :chromosome";
//        Query gQuery = this.session().createQuery(gQueryStr);
//        gQuery.setString("dbName", dbAcType.type());
//        gQuery.setString("chromosome", chromType.chm());
//        gQuery.setTimestamp("createdTime", versionTime);
//        return gQuery.list();
//    }

    /**
     * {@inheritDoc}
     */
    @SuppressWarnings("unchecked")
    public List<Gene> getGenesByDBSChromVersion(DbAcType dbAcType, ChromType chromType, Date versionTime) {

        String gQueryBase = "SELECT g FROM Gene g JOIN g.dbSource dbs WHERE dbs.dbName = :dbName AND g.createdTime = :createdTime AND ";
        List<String> allNamedChroms = ChromType.allNamedChmTypes();
        if (chromType.equals(ChromType.CHMOTHER)) {
            gQueryBase += "g.chromosome NOT IN (:chromosomes)";
        } else {
            gQueryBase += "g.chromosome = :chromosome";
        }

        Query gQuery = this.session().createQuery(gQueryBase);
        gQuery.setString("dbName", dbAcType.type());
        if (chromType.equals(ChromType.CHMOTHER)) {
            gQuery.setParameterList("chromosomes", allNamedChroms);
        } else {
            gQuery.setString("chromosome", chromType.chm());
        }
        gQuery.setTimestamp("createdTime", versionTime);
        return gQuery.list();
    }

    /**
     * {@inheritDoc}
     */
    @SuppressWarnings("unchecked")
    public List<Gene> getGenesByTLGeneId(long tlGeneId) {
        String sgQueryStr = "SELECT DISTINCT(g) FROM Gene g JOIN g.tlGenes tlgs  WHERE tlgs.id = :tlGeneId";
        Query sgQuery = this.session().createQuery(sgQueryStr);
        sgQuery.setLong("tlGeneId", tlGeneId);
        return sgQuery.list();
    }

    /**
     * {@inheritDoc}
     */
    @SuppressWarnings("unchecked")
    public List<Accession> getAllAssociatedAccessionsByGeneId(long geneId) {
        String acQueryStr = "SELECT DISTINCT(gacs) FROM Gene g JOIN g.accessions gacs  WHERE g.id = :geneId";
        Query acQuery = this.session().createQuery(acQueryStr);
        acQuery.setLong("geneId", geneId);
        return acQuery.list();
    }
}
