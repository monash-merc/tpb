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
import java.util.List;

/**
 * DbInitEntry class which is a DTO class populates the DbInitEntry object.
 *
 * @author Simon Yu
 *         <p/>
 *         Email: xiaoming.yu@monash.edu
 * @version 1.0
 * @since 1.0
 *        <p/>
 *        Date: 19/04/12 4:40 PM
 */
public class DbInitEntry implements Serializable {

    private List<TPBDataTypeDbBean> firstLevelDataTypeDbBeans;

    private List<TPBDataTypeDbBean> secondLevelDataTypeDbBeans;

    private List<TPBDataTypeDbBean> thirdLevelDataTypeDbBeans;

    private List<TPBDataTypeDbBean> fourthLevelDataTypeDbBeans;

    private List<TLColorDbBean> colorDbBeans;

    public List<TPBDataTypeDbBean> getFirstLevelDataTypeDbBeans() {
        return firstLevelDataTypeDbBeans;
    }

    public void setFirstLevelDataTypeDbBeans(List<TPBDataTypeDbBean> firstLevelDataTypeDbBeans) {
        this.firstLevelDataTypeDbBeans = firstLevelDataTypeDbBeans;
    }

    public List<TPBDataTypeDbBean> getSecondLevelDataTypeDbBeans() {
        return secondLevelDataTypeDbBeans;
    }

    public void setSecondLevelDataTypeDbBeans(List<TPBDataTypeDbBean> secondLevelDataTypeDbBeans) {
        this.secondLevelDataTypeDbBeans = secondLevelDataTypeDbBeans;
    }

    public List<TPBDataTypeDbBean> getThirdLevelDataTypeDbBeans() {
        return thirdLevelDataTypeDbBeans;
    }

    public void setThirdLevelDataTypeDbBeans(List<TPBDataTypeDbBean> thirdLevelDataTypeDbBeans) {
        this.thirdLevelDataTypeDbBeans = thirdLevelDataTypeDbBeans;
    }

    public List<TPBDataTypeDbBean> getFourthLevelDataTypeDbBeans() {
        return fourthLevelDataTypeDbBeans;
    }

    public void setFourthLevelDataTypeDbBeans(List<TPBDataTypeDbBean> fourthLevelDataTypeDbBeans) {
        this.fourthLevelDataTypeDbBeans = fourthLevelDataTypeDbBeans;
    }

    public List<TLColorDbBean> getColorDbBeans() {
        return colorDbBeans;
    }

    public void setColorDbBeans(List<TLColorDbBean> colorDbBeans) {
        this.colorDbBeans = colorDbBeans;
    }
}
