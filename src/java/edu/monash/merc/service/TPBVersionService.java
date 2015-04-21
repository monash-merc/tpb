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

import edu.monash.merc.common.name.ChromType;
import edu.monash.merc.domain.TPBVersion;
import edu.monash.merc.dto.MaxDsTPBVersion;
import edu.monash.merc.system.version.TLVersionTrack;

import java.util.List;

/**
 * TPBVersionService interface which provides the service operations for TPBVersion.
 *
 * @author Simon Yu
 *         <p/>
 *         Email: xiaoming.yu@monash.edu
 * @version 1.0
 * @since 1.0
 *        <p/>
 *        Date: 25/05/12 3:03 PM
 */
public interface TPBVersionService {

    /**
     * Save a TPBVersion
     *
     * @param tpbVersion A TPBVersion
     */
    void saveTPBVersion(TPBVersion tpbVersion);

    /**
     * Merge a TPBVersion
     *
     * @param tpbVersion A TPBVersion
     */
    void mergeTPBVersion(TPBVersion tpbVersion);

    /**
     * Update a TPBVersion
     *
     * @param tpbVersion A TPBVersion
     */
    void updateTPBVersion(TPBVersion tpbVersion);

    /**
     * Delete a TPBVersion
     *
     * @param tpbVersion a TPBVersion object
     */
    void deleteTPBVersion(TPBVersion tpbVersion);

    /**
     * Get a TPBVersion by id
     *
     * @param id a TPBVersion id
     * @return a TPBVersion object
     */
    TPBVersion getTPBVersionById(long id);

    /**
     * Delete a TPBVersion by id
     *
     * @param id a TPBVersion id
     */
    void deleteTPBVersionById(long id);

    /**
     * get the current TPBVersion by a chromosome type
     *
     * @param chromType  a chromosome ChromType type
     * @param trackToken a combination of different datasource track token
     * @return a TPBVersion object if available or null
     */
    TPBVersion getCurrentVersion(ChromType chromType, int trackToken);

    /**
     * get a TPBVersion by a chromosome type and a traffic light version track
     *
     * @param chromType      a chromosome ChromType
     * @param tlVersionTrack a traffic light version track  TLVersionTrack
     * @return true if available or false.
     */
    boolean checkTPBVersionAvailable(ChromType chromType, TLVersionTrack tlVersionTrack);

    /**
     * get the current TPBVersion by a chromosome type and a combination track token.
     *
     * @param chromType  a chromosome ChromType
     * @param trackToken a combination of datasource track token
     * @return a TPBVersion if available or null
     */
    TPBVersion getCurrentTPBVersionByChromTypeTrackToken(ChromType chromType, int trackToken);

    /**
     * get all TPBVersions by a chromosome and a combination datasource track token.
     *
     * @param chromType  a chromosome ChromType
     * @param trackToken a combination datasource track token
     * @return a list of TPBVersions if available
     */
    List<TPBVersion> getAllTPBVersionByChromTypeTrackToken(ChromType chromType, int trackToken);

    /**
     * get All TPBVersions
     *
     * @return a list of TPBVersion objects
     */
    List<TPBVersion> getAllTPBVersions();

    /**
     * get all Max combinated datasource tpb version
     *
     * @return a MaxDsTPBVersion object.
     */
    List<MaxDsTPBVersion> getAllChromosomeTPBVersionByMaxCombinatedDs();
}
