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
 * Enum DbAcType which defines the db source and accession types.
 *
 * @author Simon Yu
 *         <p/>
 *         Email: xiaoming.yu@monash.edu
 * @version 1.0
 * @since 1.0
 *        <p/>
 *        Date: 23/05/12 4:54 PM
 */
public enum DbAcType {
    NextProt("NextProt"), GPM("GPM"), GPMPSYT("GPMpSYT"), GPMLYS("GPMLYS"), GPMNTA("GPMNTA"), PA("PA"), HPA("HPA"), Ensembl("Ensembl"), Gene("Gene"), Protein("Protein"), Transcript("Transcript"), SwissProt("SwissProt"), BarCode("BarCode"), BarcodeHgu133a("BarcodeHGU133a"), BarcodeHgu133plus2("BarcodeHGU133plus2"), Affymetrix("Affymetrix"), Unknown("Unknown");

    private String name;

    DbAcType(String name) {
        this.name = name;
    }

    public String type() {
        return this.name;
    }

    public static DbAcType fromType(String type) {
        if (StringUtils.equalsIgnoreCase(type, NextProt.type())) {
            return NextProt;
        }
        if (StringUtils.equalsIgnoreCase(type, GPM.type())) {
            return GPM;
        }
        if (StringUtils.equalsIgnoreCase(type, GPMPSYT.type())) {
            return GPMPSYT;
        }
        if (StringUtils.equalsIgnoreCase(type, GPMLYS.type())) {
            return GPMLYS;
        }
        if (StringUtils.equalsIgnoreCase(type, GPMNTA.type())) {
            return GPMNTA;
        }
        if (StringUtils.equalsIgnoreCase(type, HPA.type())) {
            return HPA;
        }
        if (StringUtils.equalsIgnoreCase(type, PA.type())) {
            return PA;
        }
        if (StringUtils.equalsIgnoreCase(type, Ensembl.type())) {
            return Ensembl;
        }
        if (StringUtils.equalsIgnoreCase(type, Gene.type())) {
            return Gene;
        }
        if (StringUtils.equalsIgnoreCase(type, Protein.type())) {
            return Protein;
        }
        if (StringUtils.equalsIgnoreCase(type, Transcript.type())) {
            return Transcript;
        }
        if (StringUtils.equalsIgnoreCase(type, SwissProt.type())) {
            return SwissProt;
        }
        if (StringUtils.equalsIgnoreCase(type, BarCode.type())) {
            return BarCode;
        }
        if (StringUtils.equalsIgnoreCase(type, BarcodeHgu133a.type())) {
            return BarcodeHgu133a;
        }
        if (StringUtils.equalsIgnoreCase(type, BarcodeHgu133plus2.type())) {
            return BarcodeHgu133plus2;
        }
        if (StringUtils.equalsIgnoreCase(type, Affymetrix.type())) {
            return Affymetrix;
        }
        return Unknown;
    }

    public String toString() {
        switch (this) {
            case NextProt:
                return "NextProt";
            case GPM:
                return "GPM";
            case GPMPSYT:
                return "GPMpSYT";
            case GPMLYS:
                return "GPMLYS";
            case GPMNTA:
                return "GPMNTA";
            case PA:
                return "PA";
            case HPA:
                return "HPA";
            case Ensembl:
                return "Ensembl";
            case Gene:
                return "Gene";
            case Protein:
                return "Protein";
            case Transcript:
                return "Transcript";
            case SwissProt:
                return "SwissProt";
            case BarCode:
                return "BarCode";
            case BarcodeHgu133a:
                return "BarcodeHgu133a";
            case BarcodeHgu133plus2:
                return "BarcodeHgu133plus2";
            case Affymetrix:
                return "Affymetrix";
            default:
                return "Unknown";
        }
    }
}
