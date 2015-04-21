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
 * Enum DataType which defines the TPB data types.
 *
 * @author Simon Yu
 *         <p/>
 *         Email: xiaoming.yu@monash.edu
 * @version 1.0
 * @since 1.0
 *        <p/>
 *        Date: 23/05/12 3:26 PM
 */
public enum DataType {

    PE("PE"),
    PE_MS("PE_MS"),
    PE_MS_ANN("PE_MS_ANN"), PE_MS_PROB("PE_MS_PROB"), PE_MS_SAM("PE_MS_SAM"),
    PE_ANTI("PE_ANTI"),
    PE_ANTI_ANN("PE_ANTI_ANN"),
    PE_ANTI_IHC("PE_ANTI_IHC"),
    PE_ANTI_IHC_NORM("PE_ANTI_IHC_NORM"),
    PE_OTH("PE_OTH"),
    PE_OTH_CUR("PE_OTH_CUR"),

    SEQ("SEQ"),
    SEQ_ISO("SEQ_ISO"),
    SEQ_ISO_CNT("SEQ_ISO_CNT"),
    SEQ_SNP("SEQ_SNP"),
    SEQ_SNP_CNT("SEQ_SNP_CNT"),
    SEQ_VAR("SEQ_VAR"),
    SEQ_VAR_CNT("SEQ_VAR_CNT"),

    PTM("PTM"),
    PTM_GLY("PTM_GLY"),
    PTM_GLY_NLNK("PTM_GLY_NLNK"), PTM_GLY_OLNK("PTM_GLY_OLNK"), PTM_GLY_GLY("PTM_GLY_GLY"), PTM_GLY_OTH("PTM_GLY_OTH"),
    PTM_PHS("PTM_PHS"),
    PTM_PHS_SER("PTM_PHS_SER"), PTM_PHS_THR("PTM_PHS_THR"), PTM_PHS_TYR("PTM_PHS_TYR"), PTM_PHS_OTH("PTM_PHS_OTH"),
    PTM_ACE("PTM_ACE"),
    PTM_ACE_LYS("PTM_ACE_LYS"), PTM_ACE_NTA("PTM_ACE_NTA"), PTM_ACE_OAC("PTM_ACE_OAC"), PTM_ACE_OTH("PTM_ACE_OTH"),
    PTM_LPD("PTM_LPD"),
    PTM_LPD_PRN("PTM_LPD_PRN"), PTM_LPD_MYR("PTM_LPD_MYR"), PTM_LPD_PLM("PTM_LPD_PLM"), PTM_LPD_GPI("PTM_LPD_GPI"), PTM_LPD_OTH("PTM_LPD_OTH"),
    PTM_CLV("PTM_CLV"),
    PTM_CLV_SIG("PTM_CLV_SIG"), PTM_CLV_PRO("PTM_CLV_PRO"), PTM_CLV_OTH("PTM_CLV_OTH"),
    PTM_OTH("PTM_OTH"),
    PTM_OTH_SS("PTM_OTH_SS"), PTM_OTH_UNC("PTM_OTH_UNC"),

    DIS("DIS"),
    DIS_CAN("DIS_CAN"),
    DIS_OTH("DIS_OTH"),

    STR("STR"),
    STR_3D("STR_3D"),
    STR_3D_XRAY("STR_3D_XRAY"), STR_3D_NMR("STR_3D_NMR"), STR_3D_OTH("STR_3D_OTH"),
    STR_HOM("STR_HOM"),
    STR_HOM_MTF("STR_HOM_MTF"), STR_HOM_DOM("STR_HOM_DOM"), STR_HOM_OTH("STR_HOM_OTH"),

    LOC("LOC"),
    LOC_SBC("LOC_SBC"),
    LOC_CLT("LOC_CLT"),
    LOC_TIS("LOC_TIS"),

    TE("TE"),
    TE_MA("TE_MA"),
    TE_MA_PROP("TE_MA_PROP"),
    TE_OTH("TE_OTH"),
    TE_OTH_CUR("TE_OTH_CUR"),

    UNKNOWN("UNKNOWN");

    private String type;

    DataType(String type) {
        this.type = type;
    }

    /**
     * get a tpb data type
     *
     * @return a String represents a tpb data type
     */
    public String type() {
        return this.type;
    }

    /**
     * Get a DataType by a String value
     *
     * @param type a DataType String value
     * @return a DataType based on a String value.
     */
    public static DataType fromType(String type) {
        if (StringUtils.isNotBlank(type)) {
            for (DataType dataType : DataType.values()) {
                if (StringUtils.equalsIgnoreCase(dataType.type(), type)) {
                    return dataType;
                }
            }
        }
        return DataType.UNKNOWN;
    }

    public boolean isPE() {
        switch (this) {
            case PE:     //PE
                return true;
            case PE_MS:
                return true;
            case PE_MS_ANN:
                return true;
            case PE_MS_PROB:
                return true;
            case PE_MS_SAM:
                return true;
            case PE_ANTI:
                return true;
            case PE_ANTI_ANN:
                return true;
            case PE_ANTI_IHC:
                return true;
            case PE_ANTI_IHC_NORM:
                return true;
            case PE_OTH:
                return true;
            case PE_OTH_CUR:
                return true;
            default:
                return false;
        }
    }

    public boolean isPTM() {
        switch (this) {
            case PTM:      //PTM
                return true;
            case PTM_GLY:
                return true;
            case PTM_GLY_NLNK:
                return true;
            case PTM_GLY_OLNK:
                return true;
            case PTM_GLY_GLY:
                return true;
            case PTM_GLY_OTH:
                return true;
            case PTM_PHS:
                return true;
            case PTM_PHS_SER:
                return true;
            case PTM_PHS_THR:
                return true;
            case PTM_PHS_TYR:
                return true;
            case PTM_PHS_OTH:
                return true;
            case PTM_ACE:
                return true;
            case PTM_ACE_LYS:
                return true;
            case PTM_ACE_NTA:
                return true;
            case PTM_ACE_OAC:
                return true;
            case PTM_ACE_OTH:
                return true;
            case PTM_LPD:
                return true;
            case PTM_LPD_PRN:
                return true;
            case PTM_LPD_MYR:
                return true;
            case PTM_LPD_PLM:
                return true;
            case PTM_LPD_GPI:
                return true;
            case PTM_LPD_OTH:
                return true;
            case PTM_CLV:
                return true;
            case PTM_CLV_SIG:
                return true;
            case PTM_CLV_PRO:
                return true;
            case PTM_CLV_OTH:
                return true;
            case PTM_OTH:
                return true;
            case PTM_OTH_SS:
                return true;
            case PTM_OTH_UNC:
                return true;
            default:
                return false;
        }
    }

    public boolean isTE() {
        switch (this) {
            case TE:
                return true;
            case TE_MA:
                return true;
            case TE_MA_PROP:
                return true;
            case TE_OTH:
                return true;
            case TE_OTH_CUR:
                return true;
            default:
                return false;
        }
    }

    /**
     * toString method
     *
     * @return a String value represents the DataType
     */
    public String toString() {
        switch (this) {
            case PE:     //PE
                return "PE";
            case PE_MS:
                return "PE_MS";
            case PE_MS_ANN:
                return "PE_MS_ANN";
            case PE_MS_PROB:
                return "PE_MS_PROB";
            case PE_MS_SAM:
                return "PE_MS_SAM";
            case PE_ANTI:
                return "PE_ANTI";
            case PE_ANTI_ANN:
                return "PE_ANTI_ANN";
            case PE_ANTI_IHC:
                return "PE_ANTI_IHC";
            case PE_ANTI_IHC_NORM:
                return "PE_ANTI_IHC_NORM";
            case PE_OTH:
                return "PE_OTH";
            case PE_OTH_CUR:
                return "PE_OTH_CUR";
            case SEQ:     //SEQ
                return "SEQ";
            case SEQ_ISO:
                return "SEQ_ISO";
            case SEQ_ISO_CNT:
                return "SEQ_ISO_CNT";
            case SEQ_SNP:
                return "SEQ_SNP";
            case SEQ_SNP_CNT:
                return "SEQ_SNP_CNT";
            case SEQ_VAR:
                return "SEQ_VAR";
            case SEQ_VAR_CNT:
                return "SEQ_VAR_CNT";
            case PTM:      //PTM
                return "PTM";
            case PTM_GLY:
                return "PTM_GLY";
            case PTM_GLY_NLNK:
                return "PTM_GLY_NLNK";
            case PTM_GLY_OLNK:
                return "PTM_GLY_OLNK";
            case PTM_GLY_GLY:
                return "PTM_GLY_GLY";
            case PTM_GLY_OTH:
                return "PTM_GLY_OTH";
            case PTM_PHS:
                return "PTM_PHS";
            case PTM_PHS_SER:
                return "PTM_PHS_SER";
            case PTM_PHS_THR:
                return "PTM_PHS_THR";
            case PTM_PHS_TYR:
                return "PTM_PHS_TYR";
            case PTM_PHS_OTH:
                return "PTM_PHS_OTH";
            case PTM_ACE:
                return "PTM_ACE";
            case PTM_ACE_LYS:
                return "PTM_ACE_LYS";
            case PTM_ACE_NTA:
                return "PTM_ACE_NTA";
            case PTM_ACE_OAC:
                return "PTM_ACE_OAC";
            case PTM_ACE_OTH:
                return "PTM_ACE_OTH";
            case PTM_LPD:
                return "PTM_LPD";
            case PTM_LPD_PRN:
                return "PTM_LPD_PRN";
            case PTM_LPD_MYR:
                return "PTM_LPD_MYR";
            case PTM_LPD_PLM:
                return "PTM_LPD_PLM";
            case PTM_LPD_GPI:
                return "PTM_LPD_GPI";
            case PTM_LPD_OTH:
                return "PTM_LPD_OTH";
            case PTM_CLV:
                return "PTM_CLV";
            case PTM_CLV_SIG:
                return "PTM_CLV_SIG";
            case PTM_CLV_PRO:
                return "PTM_CLV_PRO";
            case PTM_CLV_OTH:
                return "PTM_CLV_OTH";
            case PTM_OTH:
                return "PTM_OTH";
            case PTM_OTH_SS:
                return "PTM_OTH_SS";
            case PTM_OTH_UNC:
                return "PTM_OTH_UNC";
            case DIS:      //DIS
                return "DIS";
            case DIS_CAN:
                return "DIS_CAN";
            case DIS_OTH:
                return "DIS_OTH";
            case STR:         //STR
                return "STR";
            case STR_3D:
                return "STR_3D";
            case STR_3D_XRAY:
                return "STR_3D_XRAY";
            case STR_3D_NMR:
                return "STR_3D_NMR";
            case STR_3D_OTH:
                return "STR_3D_OTH";
            case STR_HOM:
                return "STR_HOM";
            case STR_HOM_MTF:
                return "STR_HOM_MTF";
            case STR_HOM_DOM:
                return "STR_HOM_DOM";
            case STR_HOM_OTH:
                return "STR_HOM_OTH";
            case LOC:       //LOC
                return "LOC";
            case LOC_SBC:
                return "LOC_SBC";
            case LOC_CLT:
                return "LOC_CLT";
            case LOC_TIS:
                return "LOC_TIS";
            case TE:
                return "TE";
            case TE_MA:
                return "TE_MA";
            case TE_MA_PROP:
                return "TE_MA_PROP";
            case TE_OTH:
                return "TE_OTH";
            case TE_OTH_CUR:
                return "TE_OTH_CUR";
            case UNKNOWN:
                return "UNKNOWN";
            default:
                return "UNKNOWN";
        }
    }
}
