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

import edu.monash.merc.domain.Probe;
import edu.monash.merc.domain.TPBGene;
import edu.monash.merc.repository.IProbeRep;
import org.hibernate.Criteria;
import org.hibernate.Query;
import org.hibernate.criterion.Restrictions;
import org.springframework.beans.factory.config.BeanDefinition;
import org.springframework.context.annotation.Scope;
import org.springframework.stereotype.Repository;

import java.util.List;

/**
 * @author Simon Yu
 *         <p/>
 *         Email: xiaoming.yu@monash.edu
 * @version 1.0
 * @since 1.0
 *        <p/>
 *        Date: 2/07/13 10:27 AM
 */
@Scope(BeanDefinition.SCOPE_PROTOTYPE)
@Repository
public class ProbeDAO extends HibernateGenericDAO<Probe> implements IProbeRep {

    /**
     * {@inheritDoc}
     */
    @Override
    public Probe getProbeByProbeId(String probeId) {
        Criteria criteria = this.session().createCriteria(this.persistClass);
        criteria.add(Restrictions.eq("probeId", probeId));
        return (Probe) criteria.uniqueResult();
    }

    /**
     * {@inheritDoc}
     */
    @Override
    @SuppressWarnings("unchecked")
    public List<Probe> getProbesByGeneAccession(String ensgAccession) {
        Criteria criteria = this.session().createCriteria(this.persistClass);
        // create the alias for genes
        Criteria geneCrit = criteria.createAlias("tpbGenes", "tpbGenes");
        geneCrit.add(Restrictions.eq("tpbGenes.ensgAccession", ensgAccession));
        return criteria.list();
    }

    /**
     * {@inheritDoc}
     */
    @Override
    @SuppressWarnings("unchecked")
    public List<TPBGene> getTPBGenesByProbeId(String probeId) {
        //using the Query rather than Criteria as the Query is simple
        String tpbGeneHQL = "SELECT DISTINCT tg FROM TPBGene tg INNER JOIN tg.probes pbs WHERE pbs.probeId = :probeId";
        Query geneQuery = this.session().createQuery(tpbGeneHQL);
        geneQuery.setString("probeId", probeId);
        return geneQuery.list();
    }
}
