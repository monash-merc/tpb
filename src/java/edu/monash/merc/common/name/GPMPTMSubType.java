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

package edu.monash.merc.common.name;

import org.apache.commons.lang.StringUtils;

/**
 * @author Simon Yu
 *         <p/>
 *         Email: xiaoming.yu@monash.edu
 * @version 1.0
 * @since 1.0
 *        <p/>
 *        Date: 24/06/13 5:30 PM
 */
public enum GPMPTMSubType {
    PHS_S("S"), PHS_T("T"), PHS_Y("Y"), NON_PHS_S("NON_S"), NON_PHS_T("NON_T"), NON_PHS_Y("NON_Y"), LYS("LYS"), NON_LYS("NON_LYS"), NTA("NTA"), NON_NTA("NON_NTA"), UKNOWN("Unknown");
    private String type;

    GPMPTMSubType(String type) {
        this.type = type;
    }

    public String type() {
        return this.type;
    }

    public static GPMPTMSubType fromType(String type) {
        if (StringUtils.isBlank(type)) {
            return UKNOWN;
        }
        if (StringUtils.equalsIgnoreCase(type, PHS_S.type())) {
            return PHS_S;
        }
        if (StringUtils.equalsIgnoreCase(type, PHS_T.type())) {
            return PHS_T;
        }
        if (StringUtils.equalsIgnoreCase(type, PHS_Y.type())) {
            return PHS_Y;
        }
        if (StringUtils.equalsIgnoreCase(type, NON_PHS_S.type())) {
            return NON_PHS_S;
        }
        if (StringUtils.equalsIgnoreCase(type, NON_PHS_T.type())) {
            return NON_PHS_T;
        }
        if (StringUtils.equalsIgnoreCase(type, NON_PHS_Y.type())) {
            return NON_PHS_Y;
        }
        if (StringUtils.equalsIgnoreCase(type, LYS.type())) {
            return LYS;
        }
        if (StringUtils.equalsIgnoreCase(type, NON_LYS.type())) {
            return NON_LYS;
        }
        if (StringUtils.equalsIgnoreCase(type, NTA.type())) {
            return NTA;
        }
        if (StringUtils.equalsIgnoreCase(type, NON_NTA.type())) {
            return NON_NTA;
        }
        return UKNOWN;
    }

    public String toString() {
        switch (this) {
            case PHS_S:
                return "S";
            case PHS_T:
                return "T";
            case PHS_Y:
                return "Y";
            case NON_PHS_S:
                return "NON_S";
            case NON_PHS_T:
                return "NON_T";
            case NON_PHS_Y:
                return "NON_Y";
            case LYS:
                return "LYS";
            case NON_LYS:
                return "NON_LYS";
            case NTA:
                return "NTA";
            case NON_NTA:
                return "NON_NTA";
            default:
                return "Unknown";
        }
    }
}
