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

package edu.monash.merc.service;

import edu.monash.merc.domain.TPBDataType;

import java.util.List;

/**
 * TPBDataTypeService interface which provides the service operations for TPBDataType.
 *
 * @author Simon Yu
 *         <p/>
 *         Email: xiaoming.yu@monash.edu
 * @version 1.0
 * @since 1.0
 *        <p/>
 *        Date: 19/04/12 2:18 PM
 */
public interface TPBDataTypeService {

    /**
     * Save a TPBDataType
     *
     * @param tpbDataType a TPBDataType
     */
    void saveTPBDataType(TPBDataType tpbDataType);

    /**
     * Merge a TPBDataType
     *
     * @param tpbDataType a TPBDataType
     */
    void mergeTPBDataType(TPBDataType tpbDataType);

    /**
     * Update a TPBDataType
     *
     * @param tpbDataType a TPBDataType
     */
    void updateTPBDataType(TPBDataType tpbDataType);

    /**
     * Delete a TPBDataType
     *
     * @param tpbDataType a TPBDataType
     */
    void deleteTPBDataType(TPBDataType tpbDataType);

    /**
     * Delete a TPBDataType by id
     *
     * @param id a TPBDataType id
     */
    void deleteTPBDataTypeById(long id);

    /**
     * Get a TPBDataType by a id
     *
     * @param id A TPBDataType id
     * @return a TPBDataType object
     */
    TPBDataType getTPBDataTypeById(long id);

    /**
     * Get a TPBDataType by a type name
     *
     * @param type a type name of TPBDataType
     * @return a TPBDataType object
     */
    TPBDataType getTPBDataTypeByTypeName(String type);

    /**
     * Get the sub TPBDataType by a TPBDataType Id;
     *
     * @param id a TPBDataType id
     * @return a List of TPBDataType if available
     */
    List<TPBDataType> getSubTPBDataType(long id);


    /**
     * Get the sub TPBDataType by a TPBDataType dataType name;
     *
     * @param dataType a TPBDataType dataType
     * @return a List of TPBDataType if available
     */
    List<TPBDataType> getSubTPBDataType(String dataType);


    /**
     * Check whether it's a last level of TPBDataType or not
     *
     * @param dataType a TPBDataType dataType name
     * @return true if it's a last level of TPBDataType.
     */
    boolean isLastLevelType(String dataType);

    /**
     * Check whether it's a last level of TPBDataType or not
     *
     * @param id a TPBDataType dataType id
     * @return true if it's a last level of TPBDataType.
     */
    public boolean isLastLevelType(long id);
}
