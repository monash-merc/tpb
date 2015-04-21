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

import edu.monash.merc.common.name.DataType;
import edu.monash.merc.domain.PEEvidence;

import java.util.List;

/**
 * PEEvidenceService interface which provides the service operations for PEEvidence.
 *
 * @author Simon Yu
 *         <p/>
 *         Email: xiaoming.yu@monash.edu
 * @version 1.0
 * @since 1.0
 *        <p/>
 *        Date: 23/04/12 1:07 PM
 */
public interface PEEvidenceService {
    /**
     * save a PEEvidence
     *
     * @param peEvidence a PEEvidence object
     */
    void savePEEvidence(PEEvidence peEvidence);

    /**
     * merge a PEEvidence
     *
     * @param peEvidence a PEEvidence object
     */
    void mergePEEvidence(PEEvidence peEvidence);

    /**
     * update a PEEvidence
     *
     * @param peEvidence a PEEvidence object
     */
    void updatePEEvidence(PEEvidence peEvidence);

    /**
     * delete a PEEvidence
     *
     * @param peEvidence a PEEvidence object
     */
    void deletePEEvidence(PEEvidence peEvidence);


    /**
     * get PEEvidence by id
     *
     * @param id a PEEvidence id
     * @return a PEEvidence object
     */
    PEEvidence getPEEvidenceById(long id);

    /**
     * delete a PEEvidence by id
     *
     * @param id a PEEvidence id
     */
    void deletePEEvidenceById(long id);

    /**
     * get a PEEvidence summary by gene id and a TPB data type
     *
     * @param geneId   a Gene id
     * @param dataType a TPB data type
     * @return a PEEvidence object represents a PE Evidence Summary
     */
    PEEvidence getPESummaryByGeneAndType(long geneId, DataType dataType);

    /**
     * get all PEEvidence by gene id and a TPB data type
     *
     * @param geneId   a Gene id
     * @param dataType a TPB data type
     * @return a list of PEEvidence objects
     */
    List<PEEvidence> getPEEvidencesByGeneAndType(long geneId, DataType dataType);
}
