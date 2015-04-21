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

package edu.monash.merc.domain;

import org.hibernate.annotations.GenericGenerator;
import org.hibernate.annotations.Index;

import javax.persistence.*;
import java.util.Date;

/**
 * TPBVersion Domain class
 *
 * @author Simon Yu
 *         <p/>
 *         Email: xiaoming.yu@monash.edu
 * @version 1.0
 * @since 1.0
 *        <p/>
 *        Date: 24/05/12 4:47 PM
 */
@Entity
@Table(name = "tpb_version")
@org.hibernate.annotations.Table(appliesTo = "tpb_version",
        indexes = {@Index(name = "idx_tpbv_chromosome", columnNames = {"chromosome"})
        })
public class TPBVersion extends Domain {

    @Id
    @GeneratedValue(generator = "pk_generator")
    @GenericGenerator(name = "pk_generator", strategy = "org.hibernate.id.enhanced.TableGenerator",
            parameters = {
                    @org.hibernate.annotations.Parameter(name = "table_name", value = "pk_gen_tab"),
                    @org.hibernate.annotations.Parameter(name = "value_column_name ", value = "pk_next_val"),
                    @org.hibernate.annotations.Parameter(name = "segment_column_name", value = "pk_sequence"),
                    @org.hibernate.annotations.Parameter(name = "segment_value", value = "tpb_version_id"),
                    @org.hibernate.annotations.Parameter(name = "increment_size  ", value = "20"),
                    @org.hibernate.annotations.Parameter(name = "optimizer ", value = "hilo")
            })
    @Column(name = "id", nullable = false)
    private long id;

    @Column(name = "version_no")
    private int versionNo;


    @Column(name = "chromosome")
    private String chromosome;

    @Temporal(TemporalType.TIMESTAMP)
    @Column(name = "created_time")
    private Date createdTime;

    @Basic
    @Column(name = "track_token")
    private int trackToken;

    @ManyToOne(targetEntity = DSVersion.class)
    @JoinColumn(name = "nx_version_id")
    private DSVersion nxVersion;

    @ManyToOne(targetEntity = DSVersion.class)
    @JoinColumn(name = "gpm_version_id")
    private DSVersion gpmVersion;

    @ManyToOne(targetEntity = DSVersion.class)
    @JoinColumn(name = "gpm_psty_version_id")
    private DSVersion gpmPstyVersion;

    @ManyToOne(targetEntity = DSVersion.class)
    @JoinColumn(name = "gpm_lys_version_id")
    private DSVersion gpmLysVersion;

    @ManyToOne(targetEntity = DSVersion.class)
    @JoinColumn(name = "gpm_nta_version_id")
    private DSVersion gpmNtaVersion;

    @ManyToOne(targetEntity = DSVersion.class)
    @JoinColumn(name = "barcode_hgu133a_version_id")
    private DSVersion bcHgu133aVersion;

    @ManyToOne(targetEntity = DSVersion.class)
    @JoinColumn(name = "barcode_hgu133p2_version_id")
    private DSVersion bcHgu133p2Version;

    @ManyToOne(targetEntity = DSVersion.class)
    @JoinColumn(name = "hpa_version_id")
    private DSVersion hpaVersion;

    public long getId() {
        return id;
    }

    public void setId(long id) {
        this.id = id;
    }

    public int getVersionNo() {
        return versionNo;
    }

    public void setVersionNo(int versionNo) {
        this.versionNo = versionNo;
    }

    public String getChromosome() {
        return chromosome;
    }

    public void setChromosome(String chromosome) {
        this.chromosome = chromosome;
    }

    public Date getCreatedTime() {
        return createdTime;
    }

    public void setCreatedTime(Date createdTime) {
        this.createdTime = createdTime;
    }

    public int getTrackToken() {
        return trackToken;
    }

    public void setTrackToken(int trackToken) {
        this.trackToken = trackToken;
    }

    public DSVersion getNxVersion() {
        return nxVersion;
    }

    public void setNxVersion(DSVersion nxVersion) {
        this.nxVersion = nxVersion;
    }

    public DSVersion getGpmVersion() {
        return gpmVersion;
    }

    public void setGpmVersion(DSVersion gpmVersion) {
        this.gpmVersion = gpmVersion;
    }

    public DSVersion getGpmPstyVersion() {
        return gpmPstyVersion;
    }

    public void setGpmPstyVersion(DSVersion gpmPstyVersion) {
        this.gpmPstyVersion = gpmPstyVersion;
    }

    public DSVersion getGpmLysVersion() {
        return gpmLysVersion;
    }

    public void setGpmLysVersion(DSVersion gpmLysVersion) {
        this.gpmLysVersion = gpmLysVersion;
    }

    public DSVersion getGpmNtaVersion() {
        return gpmNtaVersion;
    }

    public void setGpmNtaVersion(DSVersion gpmNtaVersion) {
        this.gpmNtaVersion = gpmNtaVersion;
    }

    public DSVersion getBcHgu133aVersion() {
        return bcHgu133aVersion;
    }

    public void setBcHgu133aVersion(DSVersion bcHgu133aVersion) {
        this.bcHgu133aVersion = bcHgu133aVersion;
    }

    public DSVersion getBcHgu133p2Version() {
        return bcHgu133p2Version;
    }

    public void setBcHgu133p2Version(DSVersion bcHgu133p2Version) {
        this.bcHgu133p2Version = bcHgu133p2Version;
    }

    public DSVersion getHpaVersion() {
        return hpaVersion;
    }

    public void setHpaVersion(DSVersion hpaVersion) {
        this.hpaVersion = hpaVersion;
    }
}
