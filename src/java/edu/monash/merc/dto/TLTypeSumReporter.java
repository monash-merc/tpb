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

package edu.monash.merc.dto;

import java.io.Serializable;

/**
 * TLTypeSumReporter class which is a DTO class populates the traffic light data type summary report.
 *
 * @author Simon Yu
 *         <p/>
 *         Email: xiaoming.yu@monash.edu
 * @version 1.0
 * @since 1.0
 *        <p/>
 *        Date: 9/01/13 11:47 AM
 */
public class TLTypeSumReporter implements Serializable {

    private String tpbDataType;

    private int level4Num;

    private int level3Num;

    private int level2Num;

    private int level1Num;

    private int totalNum;

    public String getTpbDataType() {
        return tpbDataType;
    }

    public void setTpbDataType(String tpbDataType) {
        this.tpbDataType = tpbDataType;
    }

    public int getLevel4Num() {
        return level4Num;
    }

    public void setLevel4Num(int level4Num) {
        this.level4Num = level4Num;
    }

    public int getLevel3Num() {
        return level3Num;
    }

    public void setLevel3Num(int level3Num) {
        this.level3Num = level3Num;
    }

    public int getLevel2Num() {
        return level2Num;
    }

    public void setLevel2Num(int level2Num) {
        this.level2Num = level2Num;
    }

    public int getLevel1Num() {
        return level1Num;
    }

    public void setLevel1Num(int level1Num) {
        this.level1Num = level1Num;
    }

    public int getTotalNum() {
        return this.level1Num + this.level2Num + this.level3Num + this.level4Num;
    }

    public void setTotalNum(int totalNum) {
        this.totalNum = totalNum;
    }
}
