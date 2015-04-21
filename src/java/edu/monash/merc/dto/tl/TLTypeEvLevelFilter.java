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

package edu.monash.merc.dto.tl;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

/**
 * TLTypeEvLevelFilter class which is a DTO class populates the TLTypeEvLevelFilter object.
 *
 * @author Simon Yu
 *         <p/>
 *         Email: xiaoming.yu@monash.edu
 * @version 1.0
 * @since 1.0
 *        <p/>
 *        Date: 15/11/12 12:46 PM
 */
public class TLTypeEvLevelFilter implements Serializable {

    private String dataTypeDisplayName;

    private String dataType;

    private boolean typeEvLevel1;

    private boolean typeEvLevel2;

    private boolean typeEvLevel3;

    private boolean typeEvLevel4;

    public String getDataTypeDisplayName() {
        return dataTypeDisplayName;
    }

    public void setDataTypeDisplayName(String dataTypeDisplayName) {
        this.dataTypeDisplayName = dataTypeDisplayName;
    }

    public String getDataType() {
        return dataType;
    }

    public void setDataType(String dataType) {
        this.dataType = dataType;
    }

    public boolean isTypeEvLevel1() {
        return typeEvLevel1;
    }

    public void setTypeEvLevel1(boolean typeEvLevel1) {
        this.typeEvLevel1 = typeEvLevel1;
    }

    public boolean isTypeEvLevel2() {
        return typeEvLevel2;
    }

    public void setTypeEvLevel2(boolean typeEvLevel2) {
        this.typeEvLevel2 = typeEvLevel2;
    }

    public boolean isTypeEvLevel3() {
        return typeEvLevel3;
    }

    public void setTypeEvLevel3(boolean typeEvLevel3) {
        this.typeEvLevel3 = typeEvLevel3;
    }

    public boolean isTypeEvLevel4() {
        return typeEvLevel4;
    }

    public void setTypeEvLevel4(boolean typeEvLevel4) {
        this.typeEvLevel4 = typeEvLevel4;
    }

    public List<Integer> getCheckedColorLevels() {
        List<Integer> checkedLevels = new ArrayList<Integer>();
        if (isValidTypeLevel()) {
            if (this.typeEvLevel4) {
                checkedLevels.add(4);
            }
            if (this.typeEvLevel3) {
                checkedLevels.add(3);
            }
            if (this.typeEvLevel2) {
                checkedLevels.add(2);
            }
            if (this.typeEvLevel1) {
                checkedLevels.add(1);
            }
        }
        return checkedLevels;
    }

    public boolean isValidTypeLevel() {
        if ((this.typeEvLevel4 && this.typeEvLevel3 && this.typeEvLevel2 && this.typeEvLevel1) || (!this.typeEvLevel4 && !this.typeEvLevel3 && !this.typeEvLevel2 && !this.typeEvLevel1)) {
            return false;
        } else {
            return true;
        }
    }
}
