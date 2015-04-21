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

import edu.monash.merc.dao.TPBDataTypeDAO;
import edu.monash.merc.domain.TPBDataType;
import edu.monash.merc.service.TPBDataTypeService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.config.BeanDefinition;
import org.springframework.context.annotation.Scope;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

/**
 * TPBDataTypeServiceImpl class implements the TPBDataTypeService interface which provides the service operations for TPBDataType.
 *
 * @author Simon Yu
 *         <p/>
 *         Email: xiaoming.yu@monash.edu
 * @version 1.0
 * @since 1.0
 *        <p/>
 *        Date: 19/04/12 2:20 PM
 */
@Scope(BeanDefinition.SCOPE_PROTOTYPE)
@Service
@Transactional
public class TPBDataTypeServiceImpl implements TPBDataTypeService {
    @Autowired
    private TPBDataTypeDAO tpbDataTypeDao;

    /**
     * Set the TPBDataTypeDAO object
     *
     * @param tpbDataTypeDao a TPBDataTypeDAO object
     */
    public void setTpbDataTypeDao(TPBDataTypeDAO tpbDataTypeDao) {
        this.tpbDataTypeDao = tpbDataTypeDao;
    }

    /**
     * {@inheritDoc}
     */
    public void saveTPBDataType(TPBDataType tpbDataType) {
        this.tpbDataTypeDao.add(tpbDataType);
    }

    /**
     * {@inheritDoc}
     */
    public void mergeTPBDataType(TPBDataType tpbDataType) {
        this.tpbDataTypeDao.merge(tpbDataType);
    }

    /**
     * {@inheritDoc}
     */
    public void updateTPBDataType(TPBDataType tpbDataType) {
        this.tpbDataTypeDao.update(tpbDataType);
    }

    /**
     * {@inheritDoc}
     */
    public void deleteTPBDataType(TPBDataType tpbDataType) {
        this.tpbDataTypeDao.remove(tpbDataType);
    }

    /**
     * {@inheritDoc}
     */
    public void deleteTPBDataTypeById(long id) {
        this.tpbDataTypeDao.deleteTPBDataTypeById(id);
    }

    /**
     * {@inheritDoc}
     */
    public TPBDataType getTPBDataTypeById(long id) {
        return this.tpbDataTypeDao.get(id);
    }

    /**
     * {@inheritDoc}
     */
    public TPBDataType getTPBDataTypeByTypeName(String type) {
        return this.tpbDataTypeDao.getTPBDataTypeByTypeName(type);
    }

    /**
     * {@inheritDoc}
     */
    public List<TPBDataType> getSubTPBDataType(long id) {
        return this.tpbDataTypeDao.getSubTPBDataType(id);
    }

    /**
     * {@inheritDoc}
     */
    public List<TPBDataType> getSubTPBDataType(String dataType) {
        return this.tpbDataTypeDao.getSubTPBDataType(dataType);
    }

    /**
     * {@inheritDoc}
     */
    public boolean isLastLevelType(String dataType) {
        return this.tpbDataTypeDao.isLastLevelType(dataType);
    }

    /**
     * {@inheritDoc}
     */
    public boolean isLastLevelType(long id) {
        return this.tpbDataTypeDao.isLastLevelType(id);
    }
}
