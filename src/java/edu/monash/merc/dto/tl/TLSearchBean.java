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

import edu.monash.merc.common.name.DbAcType;
import edu.monash.merc.util.DMUtil;
import org.apache.commons.lang.StringUtils;

import java.io.Serializable;
import java.util.List;

/**
 * TLSearchBean class which is a DTO class populates the TLSearchBean object.
 *
 * @author Simon Yu
 *         <p/>
 *         Email: xiaoming.yu@monash.edu
 * @version 1.0
 * @since 1.0
 *        <p/>
 *        Date: 12/11/12 4:48 PM
 */
public class TLSearchBean implements Serializable {

    private String selectedChromType;

    private int combinatedToken;

    private boolean nxDbSelected;

    private boolean gpmDbSelected;

    private boolean hpaDbSelected;

    private boolean bcDbSelected;

    private long selectedVersion;

    private long regionFrom;

    private long regionTo;

    private String selectedGeneValueType;

    private String geneTypeValues;

    private List<TLTypeEvLevelFilter> tlTypeEvLevelFilters;

    private boolean advancedMode;

    private boolean regionProvided;

    private boolean typeLevelProvided;

    private boolean geneListProvided;

    public String getSelectedChromType() {
        return selectedChromType;
    }

    public void setSelectedChromType(String selectedChromType) {
        this.selectedChromType = selectedChromType;
    }

    public int getCombinatedToken() {
        return combinateDBSToken();
    }

    public String getSelectedDs() {
        String selectedDs = "";
        if (this.isNxDbSelected()) {
            selectedDs = DbAcType.NextProt.type();
        }
        if (this.isGpmDbSelected()) {
            if (StringUtils.isBlank(selectedDs)) {
                selectedDs = DbAcType.GPM.type();
            } else {
                selectedDs += " " + DbAcType.GPM.type();
            }
        }
        if (this.isHpaDbSelected()) {
            if (StringUtils.isBlank(selectedDs)) {
                selectedDs = DbAcType.HPA.type();
            } else {
                selectedDs += " " + DbAcType.HPA.type();
            }
        }
        if (this.isBcDbSelected()) {
            if (StringUtils.isBlank(selectedDs)) {
                selectedDs = DbAcType.BarCode.type();
            } else {
                selectedDs += " " + DbAcType.BarCode.type();
            }
        }
        return selectedDs;
    }

    public void setCombinatedToken(int combinatedToken) {
        this.combinatedToken = combinatedToken;
        setSelectedDsBasedOnTrackToken(combinatedToken);
    }

    private void resetSelectedDbsrc() {
        this.nxDbSelected = false;
        this.gpmDbSelected = false;
        this.hpaDbSelected = false;
        this.bcDbSelected = false;
    }

    private void setSelectedDsBasedOnTrackToken(int combinatedToken) {
        resetSelectedDbsrc();
        String trackStr = String.valueOf(combinatedToken);
        String[] tokens = DMUtil.split(trackStr);
        int tokenLength = tokens.length;
        //token number is 4 digital

        if (tokenLength == 4) {
            for (int i = (tokenLength - 1); i >= 0; i--) {
                String tk = tokens[i];

                if (StringUtils.equals(tk, "1")) {
                    if (i == 3) {
                        this.nxDbSelected = true;
                    }
                    if (i == 2) {
                        this.gpmDbSelected = true;
                    }
                    if (i == 1) {
                        this.hpaDbSelected = true;
                    }
                    if (i == 0) {
                        this.bcDbSelected = true;
                    }
                }
            }
        }

        //token number is 3 digital int 111 or 110 or 101 or 100
        if (tokenLength == 3) {
            for (int i = (tokenLength - 1); i >= 0; i--) {
                String tk = tokens[i];
                if (StringUtils.equals(tk, "1")) {
                    if (i == 2) {
                        this.nxDbSelected = true;
                    }
                    if (i == 1) {
                        this.gpmDbSelected = true;
                    }
                    if (i == 0) {
                        this.hpaDbSelected = true;
                    }

                }
            }
        }
        //token number is 2 digital int 11 or 10
        if (tokenLength == 2) {
            for (int i = (tokenLength - 1); i >= 0; i--) {
                String tk = tokens[i];
                if (StringUtils.equals(tk, "1")) {
                    if (i == 1) {
                        this.nxDbSelected = true;
                    }
                    if (i == 0) {
                        this.gpmDbSelected = true;
                    }
                }
            }
        }

        //token number is int 1 0r 0
        if (tokenLength == 1) {
            String tk = tokens[0];
            if (StringUtils.equals(tk, "1")) {
                this.nxDbSelected = true;
            }
        }
    }

    public boolean isNxDbSelected() {
        return nxDbSelected;
    }

    public void setNxDbSelected(boolean nxDbSelected) {
        this.nxDbSelected = nxDbSelected;
    }

    public boolean isGpmDbSelected() {
        return gpmDbSelected;
    }

    public void setGpmDbSelected(boolean gpmDbSelected) {
        this.gpmDbSelected = gpmDbSelected;
    }

    public boolean isHpaDbSelected() {
        return hpaDbSelected;
    }

    public void setHpaDbSelected(boolean hpaDbSelected) {
        this.hpaDbSelected = hpaDbSelected;
    }

    public boolean isBcDbSelected() {
        return bcDbSelected;
    }

    public void setBcDbSelected(boolean bcDbSelected) {
        this.bcDbSelected = bcDbSelected;
    }

    public long getSelectedVersion() {
        return selectedVersion;
    }

    public void setSelectedVersion(long selectedVersion) {
        this.selectedVersion = selectedVersion;
    }

    public List<TLTypeEvLevelFilter> getTlTypeEvLevelFilters() {
        return tlTypeEvLevelFilters;
    }

    public void setTlTypeEvLevelFilters(List<TLTypeEvLevelFilter> tlTypeEvLevelFilters) {
        this.tlTypeEvLevelFilters = tlTypeEvLevelFilters;
    }

    public long getRegionFrom() {
        return regionFrom;
    }

    public void setRegionFrom(long regionFrom) {
        this.regionFrom = regionFrom;
    }

    public long getRegionTo() {
        return regionTo;
    }

    public void setRegionTo(long regionTo) {
        this.regionTo = regionTo;
    }

    public String getSelectedGeneValueType() {
        return selectedGeneValueType;
    }

    public void setSelectedGeneValueType(String selectedGeneValueType) {
        this.selectedGeneValueType = selectedGeneValueType;
    }

    public String getGeneTypeValues() {
        return geneTypeValues;
    }

    public void setGeneTypeValues(String geneTypeValues) {
        this.geneTypeValues = geneTypeValues;
    }

    public boolean isAdvancedMode() {
        if (typeLevelFilterProvided()) {
            return true;
        }
        if (StringUtils.isNotBlank(geneTypeValues)) {
            return true;
        }
        if ((regionFrom != 0 && regionTo != 0) || (regionFrom != 0 && regionTo == 0) || regionFrom == 0 && regionTo != 0) {
            return true;
        }
        return false;
    }

    public void setAdvancedMode(boolean advancedMode) {
        this.advancedMode = advancedMode;
    }

    public boolean isRegionProvided() {
        if (regionFrom == 0 && regionTo == 0) {
            return false;
        }
        return true;
    }

    public void setRegionProvided(boolean regionProvided) {
        this.regionProvided = regionProvided;
    }

    public boolean isTypeLevelProvided() {
        return typeLevelFilterProvided();
    }

    public void setTypeLevelProvided(boolean typeLevelProvided) {
        this.typeLevelProvided = typeLevelProvided;
    }

    public boolean isGeneListProvided() {
        if (StringUtils.isNotBlank(geneTypeValues)) {
            return true;
        }
        return false;
    }

    public void setGeneListProvided(boolean geneListProvided) {
        this.geneListProvided = geneListProvided;
    }

    private boolean typeLevelFilterProvided() {
        boolean typeLevelProvided = false;
        if (tlTypeEvLevelFilters != null) {
            for (TLTypeEvLevelFilter tlTypeEvLevelFilter : tlTypeEvLevelFilters) {
                if (tlTypeEvLevelFilter.isValidTypeLevel()) {
                    return true;
                }
            }
        }
        return typeLevelProvided;
    }

    private int combinateDBSToken() {
        String ds_selected = "1";
        String none_ds_selected = "0";
        String token = none_ds_selected;

        if (this.nxDbSelected) {
            token = ds_selected;
        }

        if (this.gpmDbSelected) {
            token = token + ds_selected;
        } else {
            token = token + none_ds_selected;
        }
        if (this.hpaDbSelected) {
            token = token + ds_selected;
        } else {
            token = token + none_ds_selected;
        }
        if (this.bcDbSelected) {
            token = token + ds_selected;
        } else {
            token = token + none_ds_selected;
        }

        return Integer.valueOf(StringUtils.reverse(token));
    }
}
