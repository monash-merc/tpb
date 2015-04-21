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

import edu.monash.merc.common.name.ChromType;
import edu.monash.merc.common.name.DbAcType;
import edu.monash.merc.dao.GeneDAO;
import edu.monash.merc.domain.Accession;
import edu.monash.merc.domain.Gene;
import edu.monash.merc.service.GeneService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.config.BeanDefinition;
import org.springframework.context.annotation.Scope;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.Date;
import java.util.List;

/**
 * GeneServiceImpl class implements the GeneService interface which provides the service operations for Gene.
 *
 * @author Simon Yu
 *         <p/>
 *         Email: xiaoming.yu@monash.edu
 * @version 1.0
 * @since 1.0
 *        <p/>
 *        Date: 21/05/12 12:57 PM
 */
@Scope(BeanDefinition.SCOPE_PROTOTYPE)
@Service
@Transactional
public class GeneServiceImpl implements GeneService {

    @Autowired
    private GeneDAO geneDao;

    /**
     * set a  GeneDAO
     *
     * @param geneDao a GeneDAO object
     */
    public void setGeneDao(GeneDAO geneDao) {
        this.geneDao = geneDao;
    }

    /**
     * {@inheritDoc}
     */
    public void saveGene(Gene gene) {
        this.geneDao.add(gene);
    }

    /**
     * {@inheritDoc}
     */
    public void mergeGene(Gene gene) {
        this.geneDao.merge(gene);
    }

    /**
     * {@inheritDoc}
     */
    public void updateGene(Gene gene) {
        this.geneDao.update(gene);
    }

    /**
     * {@inheritDoc}
     */
    public void deleteGene(Gene gene) {
        this.geneDao.remove(gene);
    }

    /**
     * {@inheritDoc}
     */
    public Gene getGeneById(long id) {
        return this.geneDao.get(id);
    }

    /**
     * {@inheritDoc}
     */
    public void deleteGeneById(long id) {
        this.geneDao.deleteGeneById(id);
    }

    /**
     * {@inheritDoc}
     */
    public Gene getGeneByEnsgAndDbVersion(String ensgAccession, String dbSource, Date versionTime) {
        return this.geneDao.getGeneByEnsgAndDbVersion(ensgAccession, dbSource, versionTime);
    }

    /**
     * {@inheritDoc}
     */
    public List<Gene> getGenesByDBSChromVersion(DbAcType dbAcType, ChromType chromType, Date versionTime) {
        return this.geneDao.getGenesByDBSChromVersion(dbAcType, chromType, versionTime);
    }

    /**
     * {@inheritDoc}
     */
    public List<Gene> getGenesByTLGeneId(long tlGeneId) {
        return this.geneDao.getGenesByTLGeneId(tlGeneId);
    }

    /**
     * {@inheritDoc}
     */
    public List<Accession> getAllAssociatedAccessionsByGeneId(long geneId) {
        return this.geneDao.getAllAssociatedAccessionsByGeneId(geneId);
    }
}
