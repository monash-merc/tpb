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
import edu.monash.merc.dao.TEEvidenceDAO;
import edu.monash.merc.domain.TEEvidence;
import edu.monash.merc.service.TEEvidenceService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.config.BeanDefinition;
import org.springframework.context.annotation.Scope;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

/**
 * @author Simon Yu
 *         <p/>
 *         Email: xiaoming.yu@monash.edu
 * @version 1.0
 * @since 1.0
 *        <p/>
 *        Date: 8/07/13 1:00 PM
 */
@Scope(BeanDefinition.SCOPE_PROTOTYPE)
@Service
@Transactional
public class TEEvidenceServiceImpl implements TEEvidenceService {

    @Autowired
    private TEEvidenceDAO TEEvidenceDao;

    public void setTEEvidenceDao(TEEvidenceDAO TEEvidenceDao) {
        this.TEEvidenceDao = TEEvidenceDao;
    }

    @Override
    public void saveTEEvidence(TEEvidence TEEvidence) {
        this.TEEvidenceDao.add(TEEvidence);
    }

    @Override
    public void mergeTEEvidence(TEEvidence TEEvidence) {
        this.TEEvidenceDao.merge(TEEvidence);
    }

    @Override
    public void updateTEEvidence(TEEvidence TEEvidence) {
        this.TEEvidenceDao.update(TEEvidence);
    }

    @Override
    public void deleteTEEvidence(TEEvidence TEEvidence) {
        this.TEEvidenceDao.remove(TEEvidence);
    }

    @Override
    public TEEvidence getTEEvidenceById(long id) {
        return this.TEEvidenceDao.get(id);
    }

    @Override
    public void deleteTEEvidenceById(long id) {
        this.TEEvidenceDao.deleteTEEvidenceById(id);
    }

    @Override
    public TEEvidence getTESummaryByGeneAndType(long geneId, DataType dataType) {
        return this.TEEvidenceDao.getTESummaryByGeneAndType(geneId, dataType);
    }

    @Override
    public List<TEEvidence> getTEEvidencesByGeneAndType(long geneId, DataType dataType) {
        return this.TEEvidenceDao.getTEEvidencesByGeneAndType(geneId, dataType);
    }
}
