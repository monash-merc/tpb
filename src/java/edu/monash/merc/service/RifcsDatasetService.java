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

import edu.monash.merc.domain.rifcs.RIFCSDataset;

/**
 * RifcsDatasetService interface which provides the service operations for RIFCSDataset.
 *
 * @author Simon Yu
 *         <p/>
 *         Email: xiaoming.yu@monash.edu
 * @version 1.0
 * @since 1.0
 *        <p/>
 *        Date: 28/11/12 2:22 PM
 */
public interface RifcsDatasetService {

    /**
     * Save a RIFCSDataset
     *
     * @param rifcsDataset A RIFCSDataset
     */
    void saveRifcsDataset(RIFCSDataset rifcsDataset);

    /**
     * Merge a RIFCSDataset
     *
     * @param rifcsDataset A RIFCSDataset
     */
    void mergeRifcsDataset(RIFCSDataset rifcsDataset);

    /**
     * Update a RIFCSDataset
     *
     * @param rifcsDataset A RIFCSDataset
     */
    void updateRifcsDataset(RIFCSDataset rifcsDataset);


    /**
     * Delete a RIFCSDataset
     *
     * @param rifcsDataset a RIFCSDataset object
     */
    void deleteRifcsDataset(RIFCSDataset rifcsDataset);

    /**
     * Get a RIFCSDataset by id
     *
     * @param id a RIFCSDataset id
     * @return a RIFCSDataset object
     */
    RIFCSDataset getRifcsDatasetById(long id);

    /**
     * Get a RIFCSDataset by a tpb version id and track token number
     *
     * @param tpbVersionId the tpb version id
     * @param trackToken a track token number
     * @return a RIFCSDataset
     */

    RIFCSDataset getRifcsDsByTpbVersionAndTrackToken(long tpbVersionId, int trackToken);

    /**
     * Delete a RIFCSDataset by id
     *
     * @param id a RIFCSDataset id
     */
    void deleteRifcsDsById(long id);

    /**
     * Delete a RIFCSDataset by a tpb version id and a track token number
     *
     * @param tpbVersionId a tpb version id
     * @param trackToken a track token number
     */
    void deleteRifcsDsByTpbVersionAndTrackToken(long tpbVersionId, int trackToken);

}
