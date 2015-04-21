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

import edu.monash.merc.common.name.DataType;
import edu.monash.merc.dao.PEEvidenceDAO;
import edu.monash.merc.domain.PEEvidence;
import edu.monash.merc.service.PEEvidenceService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.config.BeanDefinition;
import org.springframework.context.annotation.Scope;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;


/**
 * PEEvidenceServiceImpl class implements the PEEvidenceService interface which provides the service operations for PEEvidence.
 *
 * @author Simon Yu
 *         <p/>
 *         Email: xiaoming.yu@monash.edu
 * @version 1.0
 * @since 1.0
 *        <p/>
 *        Date: 23/04/12 1:09 PM
 */
@Scope(BeanDefinition.SCOPE_PROTOTYPE)
@Service
@Transactional
public class PEEvidenceServiceImpl implements PEEvidenceService {
    @Autowired
    private PEEvidenceDAO peEvidenceDao;

    /**
     * set a PEEvidenceDAO
     *
     * @param peEvidenceDao a PEEvidenceDAO object
     */
    public void setPeEvidenceDao(PEEvidenceDAO peEvidenceDao) {
        this.peEvidenceDao = peEvidenceDao;
    }

    /**
     * {@inheritDoc}
     */
    public void savePEEvidence(PEEvidence peEvidence) {
        this.peEvidenceDao.add(peEvidence);
    }

    /**
     * {@inheritDoc}
     */
    public void mergePEEvidence(PEEvidence peEvidence) {
        this.peEvidenceDao.merge(peEvidence);
    }

    /**
     * {@inheritDoc}
     */
    public void updatePEEvidence(PEEvidence peEvidence) {
        this.peEvidenceDao.update(peEvidence);
    }

    /**
     * {@inheritDoc}
     */
    public void deletePEEvidence(PEEvidence peEvidence) {
        this.peEvidenceDao.remove(peEvidence);
    }

    /**
     * {@inheritDoc}
     */
    public void deletePEEvidenceById(long id) {
        this.peEvidenceDao.deletePEEvidenceById(id);
    }

    /**
     * {@inheritDoc}
     */
    public PEEvidence getPEEvidenceById(long id) {
        return this.peEvidenceDao.get(id);
    }

    /**
     * {@inheritDoc}
     */
    public PEEvidence getPESummaryByGeneAndType(long geneId, DataType dataType) {
        return this.peEvidenceDao.getPESummaryByGeneAndType(geneId, dataType);
    }

    /**
     * {@inheritDoc}
     */
    public List<PEEvidence> getPEEvidencesByGeneAndType(long geneId, DataType dataType) {
        return this.peEvidenceDao.getPEEvidencesByGeneAndType(geneId, dataType);
    }
}
