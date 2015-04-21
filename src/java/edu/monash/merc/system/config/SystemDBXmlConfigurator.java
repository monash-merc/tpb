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

import edu.monash.merc.dto.DbInitEntry;
import edu.monash.merc.dto.TLColorDbBean;
import edu.monash.merc.dto.TPBDataTypeDbBean;
import edu.monash.merc.exception.DMConfigException;
import org.apache.commons.lang.StringUtils;
import org.apache.log4j.Logger;
import org.jaxen.JaxenException;
import org.jaxen.jdom.JDOMXPath;
import org.jdom.Attribute;
import org.jdom.Document;
import org.jdom.Element;
import org.jdom.JDOMException;
import org.jdom.input.SAXBuilder;
import org.springframework.core.io.Resource;

import java.io.IOException;
import java.io.InputStream;
import java.util.ArrayList;
import java.util.List;

/**
 * SystemDBXmlConfigurator class loads the initial xml data for the TPB data types.
 *
 * @author Simon Yu
 *         <p/>
 *         Email: xiaoming.yu@monash.edu
 * @version 1.0
 * @since 1.0
 *        <p/>
 *        Date: 19/04/12 12:23 PM
 */
public class SystemDBXmlConfigurator {

    private Resource location;

    private boolean ignoreResourceNotFound = false;

    private SAXBuilder parser = new SAXBuilder();

    private Logger logger = Logger.getLogger(this.getClass().getName());

    @SuppressWarnings("unchecked")
    public DbInitEntry loadDbFromXml() {
        if (this.location != null) {
            InputStream is = null;
            try {
                //load the xml file as stream
                is = this.location.getInputStream();
                Document xmlDoc = this.parser.build(is);
                //build dateType element path
                JDOMXPath dataTypeEntityPath = new JDOMXPath(DbXmlConfigConts.DATA_TYPE_ENTITY_PATH);
                //build color element path
                JDOMXPath colorEntityPath = new JDOMXPath(DbXmlConfigConts.TL_COLOR_ENTITY_PATH);
                //get a list of data type elements
                List<Element> dataTypeEntityElements = dataTypeEntityPath.selectNodes(xmlDoc);
                //get a list of color elements
                List<Element> colorEntityElements = colorEntityPath.selectNodes(xmlDoc);
                //start to parse the dataType and colors
                return parseTpbDataTypes(dataTypeEntityElements, colorEntityElements);
            } catch (IOException ex) {
                if (this.ignoreResourceNotFound) {
                    logger.error("data type xml file not found");
                } else {
                    throw new DMConfigException(ex);
                }
            } catch (JDOMException dex) {
                throw new DMConfigException(dex);
            } catch (JaxenException jex) {
                throw new DMConfigException(jex);
            } finally {
                try {
                    if (is != null) {
                        is.close();
                    }
                } catch (Exception ex) {
                    //Ignore whatever
                }
            }
        }
        return null;
    }

    @SuppressWarnings("unchecked")
    private DbInitEntry parseTpbDataTypes(List<Element> dataTypeElements, List<Element> colorElements) {

        DbInitEntry dbInitEntry = new DbInitEntry();
        List<TPBDataTypeDbBean> firstLevelDataTypeBeans = new ArrayList<TPBDataTypeDbBean>();
        List<TPBDataTypeDbBean> secondLevelDataTypeBeans = new ArrayList<TPBDataTypeDbBean>();
        List<TPBDataTypeDbBean> thirdLevelDataTypeBeans = new ArrayList<TPBDataTypeDbBean>();
        List<TPBDataTypeDbBean> fourthLevelDataTypeBeans = new ArrayList<TPBDataTypeDbBean>();
        //parse the dataType elements
        for (Element dtEl : dataTypeElements) {
            TPBDataTypeDbBean tpbDataTypeDbBean = new TPBDataTypeDbBean();
            Attribute typeAttr = dtEl.getAttribute(DbXmlConfigConts.ATTR_TYPE);
            if (typeAttr != null) {
                String type = typeAttr.getValue();
                if (StringUtils.isNotBlank(type)) {
                    tpbDataTypeDbBean.setDataType(type);
                } else {
                    throw new DMConfigException("None id is provided for this data type in the xml file");
                }
            }

            List<Element> props = dtEl.getChildren(DbXmlConfigConts.EL_PROPERTY);
            for (Element prop : props) {
                Attribute nameAttr = prop.getAttribute(DbXmlConfigConts.ATTR_NAME);
                String name = null;
                if (nameAttr != null) {
                    name = nameAttr.getValue();
                }

                String nameValue = prop.getText();
                if (StringUtils.isBlank(name)) {
                    throw new DMConfigException("invalid data type entity in the xml file");
                }

                //set displayName
                if (name.equalsIgnoreCase(DbXmlConfigConts.DISPLAY_NAME_PROP)) {
                    if (StringUtils.isBlank(nameValue)) {
                        throw new DMConfigException("invalid data type entity in the xml file");
                    } else {
                        tpbDataTypeDbBean.setDisplayName(nameValue);
                    }
                }

                //set level
                if (name.equalsIgnoreCase(DbXmlConfigConts.TL_LEVEL_PROP)) {
                    if (StringUtils.isBlank(nameValue)) {
                        throw new DMConfigException("invalid data type entity in the xml file");
                    } else {
                        tpbDataTypeDbBean.setTlLevel(Integer.valueOf(nameValue).intValue());
                    }
                }

                //set parent ref id if available
                if (name.equalsIgnoreCase(DbXmlConfigConts.PARENT_REF_ID)) {
                    //if the tlLevel is not equals 1 and the parent ref id is null. we just say it's invalid
                    if (tpbDataTypeDbBean.getTlLevel() != 1 && StringUtils.isBlank(nameValue)) {
                        throw new DMConfigException("invalid data type entity in the xml file");
                    }
                    tpbDataTypeDbBean.setParentRefId(nameValue);
                }
            }
            //group the data type into different level
            int level = tpbDataTypeDbBean.getTlLevel();

            if (level == 1) {
                firstLevelDataTypeBeans.add(tpbDataTypeDbBean);
            }
            if (level == 2) {
                secondLevelDataTypeBeans.add(tpbDataTypeDbBean);
            }
            if (level == 3) {
                thirdLevelDataTypeBeans.add(tpbDataTypeDbBean);
            }
            if (level == 4) {
                fourthLevelDataTypeBeans.add(tpbDataTypeDbBean);
            }
        }

        dbInitEntry.setFirstLevelDataTypeDbBeans(firstLevelDataTypeBeans);
        dbInitEntry.setSecondLevelDataTypeDbBeans(secondLevelDataTypeBeans);
        dbInitEntry.setThirdLevelDataTypeDbBeans(thirdLevelDataTypeBeans);
        dbInitEntry.setFourthLevelDataTypeDbBeans(fourthLevelDataTypeBeans);

        //parse the color entities
        List<TLColorDbBean> tlColorDbBeans = new ArrayList<TLColorDbBean>();
        for (Element colorEntity : colorElements) {
            TLColorDbBean colorDbBean = new TLColorDbBean();
            //for each of element, get all properties
            List<Element> props = colorEntity.getChildren(DbXmlConfigConts.EL_PROPERTY);
            for (Element prop : props) {
                Attribute nameAttr = prop.getAttribute(DbXmlConfigConts.ATTR_NAME);
                String name = null;
                if (nameAttr != null) {
                    name = nameAttr.getValue();
                }

                String nameValue = prop.getText();
                if (StringUtils.isBlank(name)) {
                    throw new DMConfigException("invalid color entity in the xml file");
                }

                //set color
                if (name.equalsIgnoreCase(DbXmlConfigConts.COLOR_PROP)) {
                    if (StringUtils.isBlank(nameValue)) {
                        throw new DMConfigException("invalid color entity in the xml file");
                    } else {
                        colorDbBean.setTlColor(nameValue);
                    }
                }

                //set color level
                if (name.equalsIgnoreCase(DbXmlConfigConts.TL_COLOR_LEVEL_PROP)) {
                    if (StringUtils.isBlank(nameValue)) {
                        throw new DMConfigException("invalid color entity in the xml file");
                    } else {
                        colorDbBean.setColorLevel(Integer.valueOf(nameValue).intValue());
                    }
                }
            }
            tlColorDbBeans.add(colorDbBean);
        }
        dbInitEntry.setColorDbBeans(tlColorDbBeans);

        return dbInitEntry;
    }

    public void setLocation(Resource location) {
        this.location = location;
    }

    public Resource getLocation() {
        return location;
    }

    public boolean isIgnoreResourceNotFound() {
        return ignoreResourceNotFound;
    }

    public void setIgnoreResourceNotFound(boolean ignoreResourceNotFound) {
        this.ignoreResourceNotFound = ignoreResourceNotFound;
    }
}
