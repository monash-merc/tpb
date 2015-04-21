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

package edu.monash.merc.repository;

import edu.monash.merc.common.name.ChromType;
import edu.monash.merc.common.name.DbAcType;
import edu.monash.merc.domain.DSVersion;
import edu.monash.merc.dto.DBVersionBean;

import java.util.List;

/**
 * IDSVersionRep interface which defines the database operations for DSVersion.
 *
 * @author Simon Yu
 *         <p/>
 *         Email: xiaoming.yu@monash.edu
 * @version 1.0
 * @since 1.0
 *        <p/>
 *        Date: 16/04/12 2:21 PM
 */
public interface IDSVersionRep {

    /**
     * Delete the DSVersion by id
     *
     * @param id a DSVersion object id                                       ß
     */
    void deleteDsVersionById(long id);

    /**
     * Get the current version of the DSVersion based on a DBSource Type and a ChromType chromosome type
     *
     * @param dbAcType  a DbAcType type
     * @param chromType a ChromType name
     * @return a DSVersion object
     */
    DSVersion getCurrentDSVersionByChromDbs(DbAcType dbAcType, ChromType chromType);


    /**
     * Check the data in the database is up to date or not.
     *
     * @param dbAcType  a DbAcType type
     * @param chromType a chromosome type
     * @param fileName  an imported file name
     * @param timeToken a timeToken of an imported file
     * @return true if the data is up to date
     */
    boolean checkUpToDate(DbAcType dbAcType, ChromType chromType, String fileName, String timeToken);

    /**
     * Get a list of latest DBVersions by chromsome type
     *
     * @param chromType a chromosome type
     * @return a list of latest DBVersionBean objects
     */
    List<DBVersionBean> getLatestDBSVersionByChromosome(ChromType chromType);
}
