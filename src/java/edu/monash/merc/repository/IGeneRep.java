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
import edu.monash.merc.domain.Accession;
import edu.monash.merc.domain.Gene;

import java.util.Date;
import java.util.List;

/**
 * IGeneRep interface which defines the database operations for Gene.
 *
 * @author Simon Yu
 *         <p/>
 *         Email: xiaoming.yu@monash.edu
 * @version 1.0
 * @since 1.0
 *        <p/>
 *        Date: 16/04/12 12:13 PM
 */
public interface IGeneRep {
    /**
     * Delete a Gene by id
     *
     * @param id A Gene id
     */
    void deleteGeneById(long id);

    /**
     * Get a Gene specified by an Ensembl gene accession,  db source and version timestamp
     *
     * @param ensgAccession An ensembl gene accession
     * @param dbSource      A db source name
     * @param versionTime   A version timestamp
     * @return An unique Gene object
     */
    Gene getGeneByEnsgAndDbVersion(String ensgAccession, String dbSource, Date versionTime);

//    /**
//     * Get a list of Gene for a specified DbAcType type and Chromosome Type and a version timestamp
//     *
//     * @param dbAcType    a DbAcType type
//     * @param chromType   a ChromType chromosome type
//     * @param versionTime a version timestamp
//     * @return A list of Gene based on  a specified DbAcType type and Chromosome Type and a version timestamp
//     */
//    List<Gene> getGeneByDBSChromVersion(DbAcType dbAcType, ChromType chromType, Date versionTime);

    /**
     * Get a list of Gene for a specified DbAcType type and Chromosome Type and a version timestamp
     *
     * @param dbAcType    a DbAcType type
     * @param chromType   a ChromType chromosome type
     * @param versionTime a version timestamp
     * @return A list of Gene based on  a specified DbAcType type and Chromosome Type and a version timestamp
     */
    List<Gene> getGenesByDBSChromVersion(DbAcType dbAcType, ChromType chromType, Date versionTime);

    /**
     * Get a list of Gene for a specified TLGene id;
     *
     * @param tlGeneId a specified TLGene id
     * @return A list of Genes based on  a specified TLGene id
     */
    List<Gene> getGenesByTLGeneId(long tlGeneId);

    /**
     * Get a list of Accession associated with this Gene
     *
     * @param geneId a Gene Id
     * @return a List of associated Accessions objects
     */
    List<Accession> getAllAssociatedAccessionsByGeneId(long geneId);
}
