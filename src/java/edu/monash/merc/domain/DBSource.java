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

import org.apache.commons.lang.builder.EqualsBuilder;
import org.apache.commons.lang.builder.HashCodeBuilder;
import org.hibernate.annotations.GenericGenerator;
import org.hibernate.annotations.Index;

import javax.persistence.Basic;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import javax.persistence.Table;

/**
 * DBSource Domain class
 *
 * @author Simon Yu
 *         <p/>
 *         Email: xiaoming.yu@monash.edu
 * @version 1.0
 * @since 1.0
 *        <p/>
 *        Date: 28/03/12 1:10 PM
 */
@Entity
@Table(name = "db_source")
@org.hibernate.annotations.Table(appliesTo = "db_source",
        indexes = {@Index(name = "idx_db_name", columnNames = {"db_name"})
        })
public class DBSource extends Domain {
    @Id
    @GeneratedValue(generator = "pk_generator")
    @GenericGenerator(name = "pk_generator", strategy = "org.hibernate.id.enhanced.TableGenerator",
            parameters = {
                    @org.hibernate.annotations.Parameter(name = "table_name", value = "pk_gen_tab"),
                    @org.hibernate.annotations.Parameter(name = "value_column_name ", value = "pk_next_val"),
                    @org.hibernate.annotations.Parameter(name = "segment_column_name", value = "pk_sequence"),
                    @org.hibernate.annotations.Parameter(name = "segment_value", value = "db_source_id"),
                    @org.hibernate.annotations.Parameter(name = "increment_size  ", value = "20"),
                    @org.hibernate.annotations.Parameter(name = "optimizer ", value = "hilo")
            })
    @Column(name = "id", nullable = false)
    private long id;

    @Basic
    @Column(name = "db_name")
    private String dbName;

    @Basic
    @Column(name = "database_owner")
    private String dbOwner;

    @Basic
    @Column(name = "hyper_link")
    private String hyperLink;

    @Basic
    @Column(name = "primary_evidences")
    private boolean primaryEvidences;

    @Basic
    @Column(name = "description", length = 2000)
    private String description;

    public long getId() {
        return id;
    }

    public void setId(long id) {
        this.id = id;
    }

    public String getDbName() {
        return dbName;
    }

    public void setDbName(String dbName) {
        this.dbName = dbName;
    }

    public String getDbOwner() {
        return dbOwner;
    }

    public void setDbOwner(String dbOwner) {
        this.dbOwner = dbOwner;
    }

    public String getHyperLink() {
        return hyperLink;
    }

    public void setHyperLink(String hyperLink) {
        this.hyperLink = hyperLink;
    }

    public boolean isPrimaryEvidences() {
        return primaryEvidences;
    }

    public void setPrimaryEvidences(boolean primaryEvidences) {
        this.primaryEvidences = primaryEvidences;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public boolean equals(Object obj) {
        if (!(obj instanceof DBSource)) {
            return false;
        }
        if (this == obj) {
            return true;
        }
        DBSource dbs = (DBSource) obj;
        return new EqualsBuilder().append(this.dbName, dbs.getDbName()).append(this.dbOwner, dbs.getDbOwner())
                .append(this.hyperLink, dbs.getHyperLink()).append(this.description, dbs.getDescription()).append(this.primaryEvidences, dbs.isPrimaryEvidences()).isEquals();
//        return new EqualsBuilder().appendSuper(super.equals(obj)).append(this.dbName, dbs.getDbName()).append(this.dbOwner, dbs.getDbOwner())
//                       .append(this.hyperLink, dbs.getHyperLink()).append(this.description, dbs.getDescription()).append(this.primaryEvidences, dbs.isPrimaryEvidences()).isEquals();
//
    }

    public int hashCode() {
        return new HashCodeBuilder(17, 31).append(this.dbName).append(this.dbOwner).append(this.hyperLink).append(this.description)
                .append(this.primaryEvidences).toHashCode();
    }
}
