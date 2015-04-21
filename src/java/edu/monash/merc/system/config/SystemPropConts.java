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

package edu.monash.merc.system.config;

/**
 * SystemPropConts interface defines the configuration constants of the system proeperties.
 *
 * @author Simon Yu
 *         <p/>
 *         Email: xiaoming.yu@monash.edu
 * @version 1.0
 * @since 1.0
 *        <p/>
 *        Date: 28/02/12 11:49 AM
 */
public interface SystemPropConts {

    static String APPLICATION_NAME = "application.name";

    static String TPB_TERM_OF_USE = "tpb.term.of.use";

    static String TPB_CITATION_INFO = "tpb.citation.info";

    static String SYSTEM_SERVICE_EMAIL = "system.service.email";

    static String TPB_DB_DOWNLOAD_LOCATION = "tpb.db.download.location";

    static String CHROMOSOME_IMPORT_TYPE = "chromosome.import.type";

    static String DATASOURCE_NEXTPORT_IMPORT_ENABLE = "datasource.nextprot.import.enable";

    static String FTP_NX_SERVER_NAME = "ftp.nx.server.name";

    static String FTP_NX_USER_NAME = "ftp.nx.user.name";

    static String FTP_NX_USER_PASSWORD = "ftp.nx.user.password";

    static String FTP_NX_CHROMOSOME_REL_DIR = "ftp.nx.chromosome.release.directory.name";

    static String DATASOURCE_ENSEMBL_IMPORT_ENABLE = "datasource.ensembl.import.enable";

    static String DATASOURCE_PROBE_IMPORT_ENABLE = "datasource.probe.import.enable";

    static String MICROARRAY_PROBE_PLATFORMS = "microarray.probe.platforms";

    static String BIOMART_RESTFUL_WS_URL = "biomart.restful.ws.url";

    static String DATASOURCE_GPM_IMPORT_ENABLE = "datasource.gpm.import.enable";

    static String DATASOURCE_HPA_IMPORT_ENABLE = "datasource.hpa.import.enable";

    static String HPA_DATA_RELEASE_LOCATION = "hpa.data.release.location";

    static String GPM_RSS_FEED_URL = "gpm.rss.feed.url";

    static String DATASOURCE_GPM_PSYT_IMPORT_ENABLE = "datasource.gpm.psyt.import.enable";

    static String DATASOURCE_GPM_LYS_IMPORT_ENABLE = "datasource.gpm.lys.import.enable";

    static String DATASOURCE_GPM_NTA_IMPORT_ENABLE = "datasource.gpm.nta.import.enable";

    static String PROTEOMECENTRAL_FTP_SERVER_NAME = "proteomecentral.ftp.server.name";

    static String PROTEOMECENTRAL_FTP_USER_NAME = "proteomecentral.ftp.user.name";

    static String PROTEOMECENTRAL_FTP_USER_PASSWORD = "proteomecentral.ftp.user.password";

    static String GPM_PSYT_FTP_WORK_DIR = "gpm.psyt.ftp.directory.name";

    static String GPM_LYS_FTP_WORK_DIR = "gpm.lys.ftp.directory.name";

    static String GPM_NTA_FTP_WORK_DIR = "gpm.nta.ftp.directory.name";

    static String GPM_PSYT_FILE_NAME = "gpm.psyt.file.name";

    static String GPM_LYS_FILE_NAME = "gpm.lys.file.name";

    static String GPM_NTA_FILE_NAME = "gpm.nta.file.name";

    static String DATASOURCE_BARCODE_IMPORT_ENABLE = "datasource.barcode.import.enable";

    static String BARCODE_DATA_RELEASE_LOCATION = "barcode.data.release.location";

    static String BARCODE_HGU133A_DATA_FILE = "barcode.hgu133a.data.file";

    static String BARCODE_HGU133PLUS2_DATA_FILE = "barcode.hgu133plus2.data.file";

    static String TRAFFIC_LIGHT_COMBINATION_DEFAILT = "traffic.light.combination.default";

    static String RDA_RIFCS_ENABLED = "rda.rifcs.enabled";
}
