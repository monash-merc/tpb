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
 * NXAnnEntryBean class which is a DTO class populates the NXAnnEntryBean object.
 *
 * @author Simon Yu
 *         <p/>
 *         Email: xiaoming.yu@monash.edu
 * @version 1.0
 * @since 1.0
 *        <p/>
 *        Date: 7/05/12 12:45 PM
 */
public class NXAnnEntryBean implements Serializable {

    private String identifiedAccession;

    private String category;

    private String qualityQualifier;

    private String uniqueName;

    private String cvTermAccession;

    private String cvName;

    private String description;

    private List<NXAnnEvidenceBean> nxAnnEvidenceBeans;

    private List<NXIsoFormAnnBean> nxisoFormAnnBeans;

    public String getIdentifiedAccession() {
        return identifiedAccession;
    }

    public void setIdentifiedAccession(String identifiedAccession) {
        this.identifiedAccession = identifiedAccession;
    }

    public String getCategory() {
        return category;
    }

    public void setCategory(String category) {
        this.category = category;
    }

    public String getQualityQualifier() {
        return qualityQualifier;
    }

    public void setQualityQualifier(String qualityQualifier) {
        this.qualityQualifier = qualityQualifier;
    }

    public String getUniqueName() {
        return uniqueName;
    }

    public void setUniqueName(String uniqueName) {
        this.uniqueName = uniqueName;
    }

    public String getCvTermAccession() {
        return cvTermAccession;
    }

    public void setCvTermAccession(String cvTermAccession) {
        this.cvTermAccession = cvTermAccession;
    }

    public String getCvName() {
        return cvName;
    }

    public void setCvName(String cvName) {
        this.cvName = cvName;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public List<NXAnnEvidenceBean> getNxAnnEvidenceBeans() {
        return nxAnnEvidenceBeans;
    }

    public void setNxAnnEvidenceBeans(List<NXAnnEvidenceBean> nxAnnEvidenceBeans) {
        this.nxAnnEvidenceBeans = nxAnnEvidenceBeans;
    }

    public List<NXIsoFormAnnBean> getNxisoFormAnnBeans() {
        return nxisoFormAnnBeans;
    }

    public void setNxisoFormAnnBeans(List<NXIsoFormAnnBean> nxisoFormAnnBeans) {
        this.nxisoFormAnnBeans = nxisoFormAnnBeans;
    }
}
