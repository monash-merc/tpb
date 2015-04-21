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

package edu.monash.merc.system.parser.nextprot;

/**
 * @author Simon Yu
 *         <p/>
 *         Email: xiaoming.yu@monash.edu
 * @version 1.0
 * @since 1.0
 *        <p/>
 *        Date: 23/07/13 1:33 PM
 */
public interface NXFields {
    //common
    static String EV_PROTEIN_LEVEL = "protein level";

    static String EV_TRANSCRIPT_LEVEL = "transcript level";

    static String EV_HOMOLOGY = "homology";

    static String EV_PREDICTED = "predicated";

    static String EV_DUBIOUS = "dubious";

    static String EV_UNCERTAIN = "uncertain";

    static String NX_BASE_URL = "http://www.nextprot.org/db/entry/";

    static String XREF_CA_PROTEOMIC_DATABASES = "Proteomic databases";

    static String XREF_CA_ANTIBODY_DATABASES = "Antibody databases";

    static String XREF_DB_PRIDE = "PRIDE";

    static String XREF_DB_PEPTIDE_ATLAS = "PeptideAtlas";

    static String XREF_DB_HPA = "HPA";

    static String XREF_AC_PREFIX_ENSG = "ENSG";

    static String XREF_AC_ENSG_URL_END_PART = "/normal";

    static String XREF_AC_PREFIX_CAB = "CAB";

    static String XREF_AC_PREFIX_HPA = "HPA";

    static String XREF_HPA_ANTIBODY_DESC = "Human Protein Atlas Antibodies";


    /// path
    static String PATH_PROTEIN = "nextprotExport/proteins/protein";

    static String PATH_PROTEIN_EXISTENCE = "nextprotExport/proteins/protein/proteinExistence";

    static String PATH_PROTEIN_DESC_MAIN_ENTITY = "nextprotExport/proteins/protein/proteinNames/entityName";

    static String PATH_PROTEIN_DESC = "nextprotExport/proteins/protein/proteinNames/entityName/value";

    static String PATH_CHROM_LOCATION = "nextprotExport/proteins/protein/chromosomalLocations/chromosomalLocation";

    static String PATH_GENE_SYMBOL_MAIN_ENTITY = "nextprotExport/proteins/protein/geneNames/entityName";

    static String PATH_GENE_SYMBOL = "nextprotExport/proteins/protein/geneNames/entityName/value";

    static String PATH_IDENTIFIERS = "nextprotExport/proteins/protein/identifiers";

    static String PATH_IDENTIFIER = "nextprotExport/proteins/protein/identifiers/identifier";

    static String PATH_XREFS = "nextprotExport/proteins/protein/xrefs";

    static String PATH_XREF = "nextprotExport/proteins/protein/xrefs/xref";

    static String PATH_XREF_URL = "nextprotExport/proteins/protein/xrefs/xref/url";

    //Attributes
    static String ATTR_VALUE = "value";

    static String ATTR_MAIN_NAME = "isMain";

    static String ATTR_PROTEIN_UNIQUE_NAME = "uniqueName";

    static String ATTR_CHROMOSOME = "chromosome";

    static String ATTR_BAND = "band";

    static String ATTR_STRAND = "strand";

    static String ATTR_ACCESSION = "accession";

    static String ATTR_TYPE = "type";

    static String ATTR_NAME = "name";

    static String ATTR_DATABASE = "database";

    static String ATTR_XREF_DATABASE = "database";

    static String ATTR_XREF_CATEGORY = "category";

    static String ATTR_XREF_ACCESSION = "accession";

    static String ATTR_XREF_ID = "id";
}
