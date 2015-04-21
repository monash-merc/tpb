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

import java.util.ArrayList;
import java.util.LinkedList;
import java.util.List;

/**
 * Enum ChromType which defines the chromosome types: chromosome 1-22, X, Y and MT. A total of 25 types
 *
 * @author Simon Yu
 *         <p/>
 *         Email: xiaoming.yu@monash.edu
 * @version 1.0
 * @since 1.0
 *        <p/>
 *        Date: 25/05/12 1:40 PM
 */
public enum ChromType {
    CHM1("1"), CHM2("2"), CHM3("3"), CHM4("4"), CHM5("5"), CHM6("6"), CHM7("7"), CHM8("8"), CHM9("9"), CHM10("10"), CHM11("11"), CHM12("12"), CHM13("13"),
    CHM14("14"), CHM15("15"), CHM16("16"), CHM17("17"), CHM18("18"), CHM19("19"), CHM20("20"), CHM21("21"), CHM22("22"), CHMX("X"), CHMY("Y"), CHMMT("MT"), CHMOTHER("Other"), UNKNOWN("Unknown");

    private String chm;

    ChromType(String chm) {
        this.chm = chm;
    }

    public static ChromType fromType(String type) {
        if (StringUtils.isBlank(type)) {
            return UNKNOWN;
        }
        if (StringUtils.equalsIgnoreCase(type, CHM1.chm())) {
            return CHM1;
        }
        if (StringUtils.equalsIgnoreCase(type, CHM2.chm())) {
            return CHM2;
        }
        if (StringUtils.equalsIgnoreCase(type, CHM3.chm())) {
            return CHM3;
        }
        if (StringUtils.equalsIgnoreCase(type, CHM4.chm())) {
            return CHM4;
        }
        if (StringUtils.equalsIgnoreCase(type, CHM5.chm())) {
            return CHM5;
        }
        if (StringUtils.equalsIgnoreCase(type, CHM6.chm())) {
            return CHM6;
        }
        if (StringUtils.equalsIgnoreCase(type, CHM7.chm())) {
            return CHM7;
        }
        if (StringUtils.equalsIgnoreCase(type, CHM8.chm())) {
            return CHM8;
        }
        if (StringUtils.equalsIgnoreCase(type, CHM9.chm())) {
            return CHM9;
        }
        if (StringUtils.equalsIgnoreCase(type, CHM10.chm())) {
            return CHM10;
        }
        if (StringUtils.equalsIgnoreCase(type, CHM11.chm())) {
            return CHM11;
        }
        if (StringUtils.equalsIgnoreCase(type, CHM12.chm())) {
            return CHM12;
        }
        if (StringUtils.equalsIgnoreCase(type, CHM13.chm())) {
            return CHM13;
        }
        if (StringUtils.equalsIgnoreCase(type, CHM14.chm())) {
            return CHM14;
        }
        if (StringUtils.equalsIgnoreCase(type, CHM15.chm())) {
            return CHM15;
        }
        if (StringUtils.equalsIgnoreCase(type, CHM16.chm())) {
            return CHM16;
        }
        if (StringUtils.equalsIgnoreCase(type, CHM17.chm())) {
            return CHM17;
        }
        if (StringUtils.equalsIgnoreCase(type, CHM18.chm())) {
            return CHM18;
        }
        if (StringUtils.equalsIgnoreCase(type, CHM19.chm())) {
            return CHM19;
        }
        if (StringUtils.equalsIgnoreCase(type, CHM20.chm())) {
            return CHM20;
        }
        if (StringUtils.equalsIgnoreCase(type, CHM21.chm())) {
            return CHM21;
        }
        if (StringUtils.equalsIgnoreCase(type, CHM22.chm())) {
            return CHM22;
        }
        if (StringUtils.equalsIgnoreCase(type, CHMX.chm())) {
            return CHMX;
        }
        if (StringUtils.equalsIgnoreCase(type, CHMY.chm())) {
            return CHMY;
        }
        if (StringUtils.equalsIgnoreCase(type, CHMMT.chm())) {
            return CHMMT;
        }
        if (StringUtils.equalsIgnoreCase(type, CHMOTHER.chm())) {
            return CHMOTHER;
        }

        return UNKNOWN;
    }

    /**
     * Get the chromosome type.
     *
     * @return a String represents a chromosome type
     */
    public String chm() {
        return this.chm;
    }

    public static List<String> allChmTypes() {
        List<String> types = new LinkedList<String>();
        types.add(CHM1.chm());
        types.add(CHM2.chm());
        types.add(CHM3.chm());
        types.add(CHM4.chm());
        types.add(CHM5.chm());
        types.add(CHM6.chm());
        types.add(CHM7.chm());
        types.add(CHM8.chm());
        types.add(CHM9.chm());
        types.add(CHM10.chm());
        types.add(CHM11.chm());
        types.add(CHM12.chm());
        types.add(CHM13.chm());
        types.add(CHM14.chm());
        types.add(CHM15.chm());
        types.add(CHM16.chm());
        types.add(CHM17.chm());
        types.add(CHM18.chm());
        types.add(CHM19.chm());
        types.add(CHM20.chm());
        types.add(CHM21.chm());
        types.add(CHM22.chm());
        types.add(CHMX.chm());
        types.add(CHMY.chm());
        types.add(CHMMT.chm());
        types.add(CHMOTHER.chm());
        return types;
    }

    public static List<String> allNamedChmTypes() {
        List<String> types = new LinkedList<String>();
        types.add(CHM1.chm());
        types.add(CHM2.chm());
        types.add(CHM3.chm());
        types.add(CHM4.chm());
        types.add(CHM5.chm());
        types.add(CHM6.chm());
        types.add(CHM7.chm());
        types.add(CHM8.chm());
        types.add(CHM9.chm());
        types.add(CHM10.chm());
        types.add(CHM11.chm());
        types.add(CHM12.chm());
        types.add(CHM13.chm());
        types.add(CHM14.chm());
        types.add(CHM15.chm());
        types.add(CHM16.chm());
        types.add(CHM17.chm());
        types.add(CHM18.chm());
        types.add(CHM19.chm());
        types.add(CHM20.chm());
        types.add(CHM21.chm());
        types.add(CHM22.chm());
        types.add(CHMX.chm());
        types.add(CHMY.chm());
        types.add(CHMMT.chm());
        return types;
    }

    public static List<ChromType> allChroms() {
        List<ChromType> chroms = new ArrayList<ChromType>();
        chroms.add(CHM1);
        chroms.add(CHM2);
        chroms.add(CHM3);
        chroms.add(CHM4);
        chroms.add(CHM5);
        chroms.add(CHM6);
        chroms.add(CHM7);
        chroms.add(CHM8);
        chroms.add(CHM9);
        chroms.add(CHM10);
        chroms.add(CHM11);
        chroms.add(CHM12);
        chroms.add(CHM13);
        chroms.add(CHM14);
        chroms.add(CHM15);
        chroms.add(CHM16);
        chroms.add(CHM17);
        chroms.add(CHM18);
        chroms.add(CHM19);
        chroms.add(CHM20);
        chroms.add(CHM21);
        chroms.add(CHM22);
        chroms.add(CHMX);
        chroms.add(CHMY);
        chroms.add(CHMMT);
        chroms.add(CHMOTHER);
        return chroms;
    }

    public String toString() {
        switch (this) {
            case CHM1:
                return "1";
            case CHM2:
                return "2";
            case CHM3:
                return "3";
            case CHM4:
                return "4";
            case CHM5:
                return "5";
            case CHM6:
                return "6";
            case CHM7:
                return "7";
            case CHM8:
                return "8";
            case CHM9:
                return "9";
            case CHM10:
                return "10";
            case CHM11:
                return "11";
            case CHM12:
                return "12";
            case CHM13:
                return "13";
            case CHM14:
                return "14";
            case CHM15:
                return "15";
            case CHM16:
                return "16";
            case CHM17:
                return "17";
            case CHM18:
                return "18";
            case CHM19:
                return "19";
            case CHM20:
                return "20";
            case CHM21:
                return "21";
            case CHM22:
                return "22";
            case CHMX:
                return "X";
            case CHMY:
                return "Y";
            case CHMMT:
                return "MT";
            case CHMOTHER:
                return "Other";
            default:
                return "Unknown";
        }
    }
}
