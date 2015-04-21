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

package edu.monash.merc.dto.gpm;

import edu.monash.merc.common.name.GPMPTMSubType;
import edu.monash.merc.dto.EntryBean;
import edu.monash.merc.dto.PTMEvidenceBean;

import java.util.ArrayList;
import java.util.List;

/**
 * @author Simon Yu
 *         <p/>
 *         Email: xiaoming.yu@monash.edu
 * @version 1.0
 * @since 1.0
 *        <p/>
 *        Date: 29/08/13 2:44 PM
 */
public class GPMDbEntryBean extends EntryBean {

    private List<PTMEvidenceBean> ptmEvidenceBeanList = new ArrayList<PTMEvidenceBean>();

    private boolean nonResS;

    private boolean nonResT;

    private boolean nonResY;

    private boolean nonLys;

    private boolean nonNta;

    private PTMEvidenceBean nonResSEvidenceBean;

    private PTMEvidenceBean nonResTEvidenceBean;

    private PTMEvidenceBean nonResYEvidenceBean;

    private PTMEvidenceBean nonLysEvidenceBean;

    private PTMEvidenceBean nonNtaEvidenceBean;

    public GPMDbEntryBean() {
        this.ptmEvidenceBeanList = new ArrayList<PTMEvidenceBean>();
    }

    public List<PTMEvidenceBean> getPtmEvidenceBeans() {

        if (nonResS && nonResSEvidenceBean != null) {
            this.ptmEvidenceBeanList.add(nonResSEvidenceBean);
        }
        if (nonResT && nonResTEvidenceBean != null) {
            this.ptmEvidenceBeanList.add(nonResTEvidenceBean);
        }
        if (nonResY && nonResYEvidenceBean != null) {
            this.ptmEvidenceBeanList.add(nonResYEvidenceBean);
        }
        if (nonLys && nonLysEvidenceBean != null) {
            this.ptmEvidenceBeanList.add(nonLysEvidenceBean);
        }
        if (nonNta && nonNtaEvidenceBean != null) {
            this.ptmEvidenceBeanList.add(nonNtaEvidenceBean);
        }
        return this.ptmEvidenceBeanList;
    }

    public void setPtmEvidenceBean(PTMEvidenceBean ptmEvidenceBean, GPMPTMSubType gpmptmSubType) {
        if (this.ptmEvidenceBeanList == null) {
            this.ptmEvidenceBeanList = new ArrayList<PTMEvidenceBean>();
        }
        if (gpmptmSubType.equals(GPMPTMSubType.NON_PHS_S)) {
            this.nonResSEvidenceBean = ptmEvidenceBean;
            nonResS = true;
        } else if (gpmptmSubType.equals(GPMPTMSubType.NON_PHS_T)) {
            this.nonResTEvidenceBean = ptmEvidenceBean;
            nonResT = true;
        } else if (gpmptmSubType.equals(GPMPTMSubType.NON_PHS_Y)) {
            this.nonResYEvidenceBean = ptmEvidenceBean;
            nonResY = true;
        } else if (gpmptmSubType.equals(GPMPTMSubType.PHS_S)) {
            this.ptmEvidenceBeanList.add(ptmEvidenceBean);
            nonResS = false;
            this.nonResSEvidenceBean = null;
        } else if (gpmptmSubType.equals(GPMPTMSubType.PHS_T)) {
            this.ptmEvidenceBeanList.add(ptmEvidenceBean);
            nonResT = false;
            this.nonResTEvidenceBean = null;
        } else if (gpmptmSubType.equals(GPMPTMSubType.PHS_Y)) {
            this.ptmEvidenceBeanList.add(ptmEvidenceBean);
            nonResY = false;
            this.nonResYEvidenceBean = null;
        } else if (gpmptmSubType.equals(GPMPTMSubType.NON_LYS)) {
            this.nonLysEvidenceBean = ptmEvidenceBean;
            nonLys = true;
        } else if (gpmptmSubType.equals(GPMPTMSubType.NON_NTA)) {
            this.nonNtaEvidenceBean = ptmEvidenceBean;
            nonNta = true;
        } else if (gpmptmSubType.equals(GPMPTMSubType.LYS)) {
            this.ptmEvidenceBeanList.add(ptmEvidenceBean);
            nonLys = false;
            this.nonLysEvidenceBean = null;
        } else if (gpmptmSubType.equals(GPMPTMSubType.NTA)) {
            this.ptmEvidenceBeanList.add(ptmEvidenceBean);
            nonNta = false;
            this.nonNtaEvidenceBean = null;
        }
    }
}
