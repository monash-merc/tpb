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

package edu.monash.merc.service;

import edu.monash.merc.domain.TLColor;

/**
 * TLColorService interface which provides the service operations for TLColor.
 *
 * @author Simon Yu
 *         <p/>
 *         Email: xiaoming.yu@monash.edu
 * @version 1.0
 * @since 1.0
 *        <p/>
 *        Date: 23/04/12 11:25 AM
 */
public interface TLColorService {
    /**
     * save the TLColor object
     *
     * @param tlColor a TLColor object
     */
    void saveTLColor(TLColor tlColor);

    /**
     * merge the TLColor object
     *
     * @param tlColor a TLColor object
     */
    void mergeTLColor(TLColor tlColor);

    /**
     * update the TLColor object
     *
     * @param tlColor a TLColor object
     */
    void updateTLColor(TLColor tlColor);

    /**
     * save the TLColor object
     *
     * @param tlColor a TLColor object
     */
    void deleteTLColor(TLColor tlColor);

    /**
     * delete TLColor by id
     *
     * @param id a TLColor id
     */
    void deleteTLColorById(long id);

    /**
     * get TLColor by id
     *
     * @param id a TLColor id
     * @return a TLColor object
     */
    TLColor getTLColorById(long id);

    /**
     * get a TLColor by a color value
     *
     * @param color a color value
     * @return a TLColor object
     */
    TLColor getTLColorByColor(String color);

    /**
     * get a TLColor by a color level value
     *
     * @param colorLevel a color level value of a TLColor
     * @return a TLColor object
     */
    TLColor getTLColorByColorLevel(int colorLevel);
}
