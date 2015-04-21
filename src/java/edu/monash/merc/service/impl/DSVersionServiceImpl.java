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
import edu.monash.merc.dao.DSVersionDAO;
import edu.monash.merc.domain.DSVersion;
import edu.monash.merc.dto.DBVersionBean;
import edu.monash.merc.service.DSVersionService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.config.BeanDefinition;
import org.springframework.context.annotation.Scope;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

/**
 * DSVersionServiceImpl class impelements the  DSVersionService interface which provides the service operations for DSVersion.
 *
 * @author Simon Yu
 *         <p/>
 *         Email: xiaoming.yu@monash.edu
 * @version 1.0
 * @since 1.0
 *        <p/>
 *        Date: 22/05/12 12:41 PM
 */
@Scope(BeanDefinition.SCOPE_PROTOTYPE)
@Service
@Transactional
public class DSVersionServiceImpl implements DSVersionService {

    @Autowired
    private DSVersionDAO dsVersionDao;

    /**
     * set a DSVersionDAO
     *
     * @param dsVersionDao a DSVersionDAO object
     */
    public void setDsVersionDao(DSVersionDAO dsVersionDao) {
        this.dsVersionDao = dsVersionDao;
    }

    /**
     * {@inheritDoc}
     */
    public void saveDSVersion(DSVersion dsVersion) {
        this.dsVersionDao.add(dsVersion);
    }

    /**
     * {@inheritDoc}
     */
    public void mergeDSVersion(DSVersion dsVersion) {
        this.dsVersionDao.merge(dsVersion);
    }

    /**
     * {@inheritDoc}
     */
    public void updateDSVersion(DSVersion dsVersion) {
        this.dsVersionDao.update(dsVersion);
    }

    /**
     * {@inheritDoc}
     */
    public void deleteDSVersion(DSVersion dsVersion) {
        this.dsVersionDao.remove(dsVersion);
    }

    /**
     * {@inheritDoc}
     */
    public void deleteDSVersionById(long id) {
        this.dsVersionDao.deleteDsVersionById(id);
    }

    /**
     * {@inheritDoc}
     */
    public DSVersion getDSVersionById(long id) {
        return this.dsVersionDao.get(id);
    }

    /**
     * {@inheritDoc}
     */
    public DSVersion getCurrentDSVersionByChromDbs(DbAcType dbAcType, ChromType chromType) {
        return this.dsVersionDao.getCurrentDSVersionByChromDbs(dbAcType, chromType);
    }

    /**
     * {@inheritDoc}
     */
    public boolean checkUpToDate(DbAcType dbAcType, ChromType chromType, String fileName, String timeToken) {
        return this.dsVersionDao.checkUpToDate(dbAcType, chromType, fileName, timeToken);
    }

    @Override
    public List<DBVersionBean> getLatestDBSVersionByChromosome(ChromType chromType) {
        return this.dsVersionDao.getLatestDBSVersionByChromosome(chromType);
    }
}
