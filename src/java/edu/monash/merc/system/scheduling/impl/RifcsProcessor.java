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

package edu.monash.merc.system.scheduling.impl;

import edu.monash.merc.dto.rifcs.RifcsInfoBean;
import edu.monash.merc.exception.DMConfigException;
import edu.monash.merc.rifcs.RifcsService;
import edu.monash.merc.service.DMSystemService;
import edu.monash.merc.system.config.RifcsPropConts;
import edu.monash.merc.system.config.SystemPropConts;
import edu.monash.merc.system.config.SystemPropSettings;
import edu.monash.merc.system.scheduling.TPBProcessor;
import edu.monash.merc.util.DMUtil;
import edu.monash.merc.util.file.DMFileUtils;
import edu.monash.merc.util.file.ScanFileFilter;
import org.apache.commons.lang.StringUtils;
import org.apache.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.context.ResourceLoaderAware;
import org.springframework.core.io.Resource;
import org.springframework.core.io.ResourceLoader;
import org.springframework.stereotype.Service;

import javax.annotation.PostConstruct;
import java.io.File;
import java.util.Date;
import java.util.GregorianCalendar;
import java.util.List;

/**
 * RifcsProcessor class implements the TPBProcessor interface, processing the RIFCS public registration
 *
 * @author Simon Yu
 *         <p/>
 *         Email: xiaoming.yu@monash.edu
 * @version 1.0
 * @since 1.0
 *        <p/>
 *        Date: 27/11/12 4:04 PM
 */
@Service
@Qualifier("rifcsProcessor")
public class RifcsProcessor implements TPBProcessor, ResourceLoaderAware {

    @Autowired
    private RifcsService rifcsService;

    @Autowired
    private SystemPropSettings systemPropSettings;

    @Autowired
    private DMSystemService dmSystemService;

    private ResourceLoader resourceLoader;

    private boolean rifcsEnabled;

    private String rifcsDir;

    private String rifcsStoreLocation;

    private RifcsInfoBean rifcsInfoBean;

    private Logger logger = Logger.getLogger(this.getClass().getName());

    public void setRifcsService(RifcsService rifcsService) {
        this.rifcsService = rifcsService;
    }

    public RifcsService getRifcsService() {
        return rifcsService;
    }

    public void setResourceLoader(ResourceLoader resourceLoader) {
        this.resourceLoader = resourceLoader;
    }

    public ResourceLoader getResourceLoader() {
        return resourceLoader;
    }

    public boolean isRifcsEnabled() {
        return rifcsEnabled;
    }

    public void setRifcsEnabled(boolean rifcsEnabled) {
        this.rifcsEnabled = rifcsEnabled;
    }

    public String getRifcsDir() {
        return rifcsDir;
    }

    public void setRifcsDir(String rifcsDir) {
        this.rifcsDir = rifcsDir;
    }

    public String getRifcsStoreLocation() {
        return rifcsStoreLocation;
    }

    public void setRifcsStoreLocation(String rifcsStoreLocation) {
        this.rifcsStoreLocation = rifcsStoreLocation;
    }

    public RifcsInfoBean getRifcsInfoBean() {
        return rifcsInfoBean;
    }

    public void setRifcsInfoBean(RifcsInfoBean rifcsInfoBean) {
        this.rifcsInfoBean = rifcsInfoBean;
    }

    @PostConstruct
    public void initProcess() {
        try {
            Resource rootRes = this.resourceLoader.getResource("/");
            //root full path
            String rootFullPath = rootRes.getURL().getPath();

            //root relative path
            String rootRelPath = getRootRelPath(rootFullPath);

            //get the pre-defined files location
            Resource rifcsResource = this.resourceLoader.getResource("classpath:rifcs");

            //rifcs directory
            this.rifcsDir = rifcsResource.getURL().getPath();
            if (StringUtils.isBlank(this.rifcsDir)) {
                throw new DMConfigException("The pre-defined rifcs files directory not found");
            }

            String rifcsEnabledStr = this.systemPropSettings.getPropValue(SystemPropConts.RDA_RIFCS_ENABLED);
            this.rifcsEnabled = Boolean.valueOf(rifcsEnabledStr);
            String rificsRepubStr = this.systemPropSettings.getPropValue(RifcsPropConts.TPB_RIFCS_REPUBLISH_REQUIRED);
            boolean rifcsRepublishRequired = Boolean.valueOf(rificsRepubStr);
            this.rifcsStoreLocation = DMUtil.normalizePath(this.systemPropSettings.getPropValue(RifcsPropConts.TPB_RIFCS_DATA_STORE));
            String tpbGroupName = this.systemPropSettings.getPropValue(RifcsPropConts.TPB_RIFCS_TPB_GROUP_NAME);
            String serverName = this.systemPropSettings.getPropValue(RifcsPropConts.TPB_SERVER_NAME);
            String rifcsKeyPrefix = this.systemPropSettings.getPropValue(RifcsPropConts.TPB_RIFCS_KEY_PREFIX);
            String rifcsTemplate = this.systemPropSettings.getPropValue(RifcsPropConts.TPB_RIFCS_TEMPLATE_FILE);

            rifcsInfoBean = new RifcsInfoBean();
            rifcsInfoBean.setRepublishRequired(rifcsRepublishRequired);
            rifcsInfoBean.setRifcsStoreLocation(this.rifcsStoreLocation);
            rifcsInfoBean.setTpbGroupName(tpbGroupName);
            rifcsInfoBean.setServerName(serverName);
            rifcsInfoBean.setAppRootRelPath(rootRelPath);
            rifcsInfoBean.setRifcsKeyPrefix(rifcsKeyPrefix);
            rifcsInfoBean.setRifcsTemplate(rifcsTemplate);

            //check if it is valid or not
            if (!rifcsInfoBean.valid()) {
                throw new DMConfigException("Some configuration values missed in the rifcs.properties file");
            }
        } catch (Exception ex) {
            throw new DMConfigException("Failed to load rifcs properties, " + ex.getMessage());
        }
    }

    private String getRootRelPath(String fullPath) {
        String path = StringUtils.removeEnd(fullPath, "/");
        String relPpath = StringUtils.substringAfterLast(path, "/");
        if (StringUtils.isBlank(relPpath)) {
            throw new DMConfigException("The root relative path is null");
        }
        return relPpath;
    }

    public void process() {
        if (this.rifcsEnabled) {
            try {
                //create a published date
                Date publishDate = GregorianCalendar.getInstance().getTime();
                rifcsInfoBean.setPublishedDate(publishDate);
                //Copy the predefined files into the rifcs store location
                copyRifcsFiles(this.rifcsDir, this.rifcsStoreLocation);
                logger.info("Copying the rifcs files has been completed");
                //call DMService to create the rifcs files
                this.dmSystemService.createRifcs(rifcsInfoBean);
                logger.info("The rifcs publishing has been completed");
            } catch (Exception ex) {
                logger.error("Failed to publish the rifcs file(s), " + ex.getMessage());
            }
        }
    }

    private void copyRifcsFiles(String srcDir, String destDir) {
        //create a xml file filter
        ScanFileFilter xmlFileFilter = new ScanFileFilter(null, ".xml");
        List<String> fileNameList = DMFileUtils.discoverFileNames(this.rifcsDir, xmlFileFilter);
        for (String fileName : fileNameList) {
            String srcFile = srcDir + fileName;
            String destFile = destDir + File.separator + fileName;
            //copy the file
            DMFileUtils.copyFile(srcFile, destFile, false);
        }
    }
}
