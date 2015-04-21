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

import edu.monash.merc.domain.Probe;
import edu.monash.merc.domain.TPBGene;

import java.util.List;

/**
 * @author Simon Yu
 *         <p/>
 *         Email: xiaoming.yu@monash.edu
 * @version 1.0
 * @since 1.0
 *        <p/>
 *        Date: 2/07/13 10:52 AM
 */
public interface ProbeService {

    /**
     * get a Probe by id
     *
     * @param id a Probe database id
     * @return a Probe object
     */
    Probe getProbeById(long id);

    /**
     * save a Probe object
     *
     * @param probe a Probe object
     */
    void saveProbe(Probe probe);

    /**
     * merge a Probe object
     *
     * @param probe a Probe object
     */
    void mergeProbe(Probe probe);

    /**
     * update a Probe object
     *
     * @param probe a Probe object
     */
    void updateProbe(Probe probe);

    /**
     * delete a Probe object
     *
     * @param probe a Probe object
     */
    void deleteProbe(Probe probe);

    /**
     * get a Probe object by a probe set id
     *
     * @param probeId a probe set id
     * @return a Probe object
     */
    Probe getProbeByProbeId(String probeId);

    /**
     * get a List of Probe by a TPBGene accession
     *
     * @param ensgAccession a TPBGene accession
     * @return a List of Probe
     */
    List<Probe> getProbesByGeneAccession(String ensgAccession);

    /**
     * get a list of TPBGene by a Probe Id
     *
     * @param probeId a Probe Id
     * @return a list of TPBGene which are associated with a specific Probe Id
     */
    List<TPBGene> getTPBGenesByProbeId(String probeId);
}
