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

import edu.monash.merc.common.name.GPMDbType;

import java.io.Serializable;
import java.util.List;

/**
 * @author Simon Yu
 *         <p/>
 *         Email: xiaoming.yu@monash.edu
 * @version 1.0
 * @since 1.0
 *        <p/>
 *        Date: 29/08/13 2:27 PM
 */
public class GPMDbBean implements Serializable {

    private String releaseToken;

    private String nominalMass;

    private String sequenceAssembly;

    private String sequenceSource;

    private String maximumLoge;

    private GPMDbType gpmDbType;

    private List<GPMDbEntryBean> pgmDbEntryBeans;

    public String getReleaseToken() {
        return releaseToken;
    }

    public void setReleaseToken(String releaseToken) {
        this.releaseToken = releaseToken;
    }

    public String getNominalMass() {
        return nominalMass;
    }

    public void setNominalMass(String nominalMass) {
        this.nominalMass = nominalMass;
    }

    public String getSequenceAssembly() {
        return sequenceAssembly;
    }

    public void setSequenceAssembly(String sequenceAssembly) {
        this.sequenceAssembly = sequenceAssembly;
    }

    public String getSequenceSource() {
        return sequenceSource;
    }

    public void setSequenceSource(String sequenceSource) {
        this.sequenceSource = sequenceSource;
    }

    public String getMaximumLoge() {
        return maximumLoge;
    }

    public void setMaximumLoge(String maximumLoge) {
        this.maximumLoge = maximumLoge;
    }

    public GPMDbType getGpmDbType() {
        return gpmDbType;
    }

    public void setGpmDbType(GPMDbType gpmDbType) {
        this.gpmDbType = gpmDbType;
    }

    public List<GPMDbEntryBean> getPgmDbEntryBeans() {
        return pgmDbEntryBeans;
    }

    public void setPgmDbEntryBeans(List<GPMDbEntryBean> pgmDbEntryBeans) {
        this.pgmDbEntryBeans = pgmDbEntryBeans;
    }
}
