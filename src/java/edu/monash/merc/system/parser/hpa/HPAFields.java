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

package edu.monash.merc.system.parser.hpa;

/**
 * Created with IntelliJ IDEA.
 * User: simonyu
 * Date: 16/12/2013
 * Time: 11:31 AM
 * To change this template use File | Settings | File Templates.
 */
public interface HPAFields {

    static String PATH_ENTRY = "proteinAtlas/entry";

    static String PATH_ENTRY_NAME = "proteinAtlas/entry";

    static String PATH_ENTRY_IDENTIFIER = "proteinAtlas/entry/identifier" ;

    static String PATH_ENTRY_TISSUE_EXPRESSION = "proteinAtlas/entry/tissueExpression";

    static String PATH_ENTRY_TE_VERIFICATION = "proteinAtlas/entry/tissueExpression/verification";

    static String PATH_ENTRY_TE_DATA = "proteinAtlas/entry/tissueExpression/data";

    static String ATT_ENTRY_VERSION = "version";

    static String ATT_IDENTIFIER_ID = "id";

    static String ATT_IDENTIFIER_DB = "db";

    static String ATT_IDENTIFIER_VERSION = "version";

    static String ATT_TE_TYPE = "type";

    static  String ATT_TE_TECHNOLOGY = "technology";

    static String ATT_TE_ASSAY_TYPE = "assayType";

}
