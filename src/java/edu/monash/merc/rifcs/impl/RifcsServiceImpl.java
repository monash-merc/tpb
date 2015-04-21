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

package edu.monash.merc.rifcs.impl;

import edu.monash.merc.exception.DMException;
import edu.monash.merc.rifcs.RifcsService;
import freemarker.template.Template;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.context.annotation.Scope;
import org.springframework.stereotype.Service;
import org.springframework.web.servlet.view.freemarker.FreeMarkerConfigurer;

import java.io.File;
import java.io.FileWriter;
import java.io.Writer;
import java.util.Map;

/**
 * RifcsServiceImpl class implements the RifcsService interface which provides the rifcs creation service operation.
 *
 * @author Simon Yu
 *         <p/>
 *         Email: xiaoming.yu@monash.edu
 * @version 1.0
 * @since 1.0
 *        <p/>
 *        Date: 27/11/12  3:18 PM
 */
@Scope("prototype")
@Service
@Qualifier("rifcsService")
public class RifcsServiceImpl implements RifcsService {

    /**
     * rifcs file extention
     */
    private static String RIFCS_EXT = ".xml";

    @Autowired
    @Qualifier("rifcsFreeMarker")
    private FreeMarkerConfigurer rifcsFreeMarker;

    /**
     * set a  FreeMarkerConfigurer
     *
     * @param rifcsFreeMarker a FreeMarkerConfigurer object
     */
    public void setRifcsFreeMarker(FreeMarkerConfigurer rifcsFreeMarker) {
        this.rifcsFreeMarker = rifcsFreeMarker;
    }

    /**
     * {@inheritDoc}
     */
    public void createRifcsFile(String rifcsStoreLocaton, String identifier, Map<String, String> templateValues, String rifcsTemplate) {
        try {
            Writer rifcsWriter = new FileWriter(new File(rifcsStoreLocaton + File.separator + identifier + RIFCS_EXT));
            Template template = this.rifcsFreeMarker.getConfiguration().getTemplate(rifcsTemplate);
            template.process(templateValues, rifcsWriter);
        } catch (Exception ex) {
            throw new DMException(ex);
        }
    }
}
