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

/**
 * mPopup - Dialog Popup window
 * @author Simon Yu
 */

var mPopup = {

    /**
     * The window owner. the display window will be based on this owner
     */
    owner:undefined,

    /**
     * The mouse hovered position
     */
    hovered:false,

    /**
     * Settings
     */
    settings:{
        /**
         * The most outside window id
         */
        id:"mPopup",
        /**
         * Window title
         */
        title:"Title",
        /**
         * The dialog content
         */
        content:"",

        /**
         * The most outside window css class
         */
        className:"",

        /**
         * The default window width size
         */
        width:250,

        /**
         * flag  whether it should close if mouse click outside the popup windows
         */
        close_on_body_click:true,

        /**
         * The top offset of the window in px
         */
        top_offset:5,

        /**
         * The left offset of the window in px
         */
        left_offset:5,

        /**
         * Check if popup window already showed or not
         */
        pwDone:false
    },

    /**
     * Display the Dialog
     */
    show:function (options) {
        var position = mPopup.getPosition();
        // reset the default to avoid the conflicts between multiple windows
        mPopup.settings.close_on_body_click = true;
        mPopup.settings.title_visiable = true;
        mPopup.settings.top_offset = 5;
        mPopup.settings.left_offset = 5;
        mPopup.settings.width = 250;
        mPopup.settings.content = "";
        mPopup.settings.title = "Title";
        mPopup.settings.id = "mPopup";
        mPopup.settings.className = "";
        mPopup.settings.pwDone = false;
        $.extend(mPopup.settings, options);

        //close the dialog window
        mPopup.close();

        //create dialog window object
        var popup = $("#" + mPopup.settings.id);
        if (popup.size() == 0) {

            var browserHalfSize = mPopup.getBrowserHalfSize();
            var browserHalfHeight = browserHalfSize.height;
            var browserHalfWidth = browserHalfSize.width;

           // alert("browserHalfHeight: " + browserHalfHeight + ", browserHalfWidth: " + browserHalfWidth);
            if(browserHalfHeight >100){
                browserHalfHeight = browserHalfHeight -100;
            }
            if(browserHalfWidth > 200){
                browserHalfWidth = browserHalfWidth -200;
            }
            var topPosition = browserHalfHeight + mPopup.settings.top_offset;
            var leftPosition = browserHalfWidth + mPopup.settings.left_offset;

            var positionCss = 'top:' + topPosition + 'px;left:' + leftPosition + 'px;';

            //popup window
            var html = '';
            html += '<div id="' + mPopup.settings.id + '" class="mp_outter_panel ' + mPopup.settings.className + '" style="position:absolute;display:none;' + positionCss + '">';
            html += '    <div class="mp_popup_panel" style="width:' + mPopup.settings.width + 'px">';
            html += '        <div class="mp_inner_panel">';
            html += '	        <div class="mp_header">';
            html += '               <div class="mp_title">' + mPopup.settings.title + '</div>';
            html += '               <div class="mp_close" onclick="mPopup.close();" title="Close"></div>';
            html += '		    </div>';
            html += '           <div class="mp_content">';
            html += '               <div class="mp_content_display">' + mPopup.settings.content +'</div>';
            html += '               <br/>';
            html += '           </div>';
            html += '        </div>';
            html += '    </div>';
            html += '    <div style="clear:both"></div>';
            html += '</div>';

            $("body").append($(html));

            popup = $("#" + mPopup.settings.id);
            popup.hover(function () {
                mPopup.hovered = true;
            });
        }

        //show the pop-up windows
        popup.show();

        //set the popup window already showed
        mPopup.pwDone = true;

        // auto close when body click
        $(document).mousedown(function () {
            if (!mPopup.hovered && mPopup.settings.close_on_body_click) {
                mPopup.close();
            }
        });

        jQuery(mPopup.owner).addClass("active");

        popup.mouseover(function () {
            mPopup.hovered = true;
        });

        popup.mouseout(function () {
            mPopup.hovered = false;
        });

        jQuery(mPopup.owner).mouseover(function () {
            mPopup.hovered = true;
        });

        jQuery(mPopup.owner).mouseout(function () {
            mPopup.hovered = false;
        });
    },

    /**
     * Update the contents
     */
    update:function (content) {
        $("#" + mPopup.settings.id + " .mp_content_display").html(content);
    },

    /**
     * Close the dialog
     */
    close:function () {
        var popup = $("#" + mPopup.settings.id);
        $(mPopup.owner).removeClass("active");
        popup.fadeOut('slow');
        popup.remove();
    },

    /**
     * Get be half size of the browser
     *
     * */
    getBrowserHalfSize:function () {
        var browserWidth = window.innerWidth || document.documentElement.clientWidth ||
            document.body.clientWidth;
        var browserHeight = window.innerHeight || document.documentElement.clientHeight ||
            document.body.clientHeight;
        var scrollX = document.documentElement.scrollLeft || document.body.scrollLeft;
        var scrollY = document.documentElement.scrollTop || document.body.scrollTop;
        return {width:( browserWidth / 2) + scrollX, height:( browserHeight / 2) + scrollY};
    },
    /**
     * Get the current Window Size
     */
    getBrowserSize:function () {
        var browserWidth = window.innerWidth || document.documentElement.clientWidth ||
            document.body.clientWidth;
        var browserHeight = window.innerHeight || document.documentElement.clientHeight ||
            document.body.clientHeight;
        var scrollX = document.documentElement.scrollLeft || document.body.scrollLeft;
        var scrollY = document.documentElement.scrollTop || document.body.scrollTop;
        return {width:browserWidth + scrollX, height:browserHeight + scrollY};
    },

    /**
     * Get the owner position - top, left, width and height
     */
    getPosition:function () {
        if (mPopup.owner == undefined) {
            return {top:0, left:0, width:0, height:0};
        }

        var e = mPopup.owner;
        var oTop = e.offsetTop;
        var oLeft = e.offsetLeft;
        var oWidth = e.offsetWidth;
        var oHeight = e.offsetHeight;

        while (e = e.offsetParent) {
            oTop += e.offsetTop;
            oLeft += e.offsetLeft;
        }

        return {
            top:oTop,
            left:oLeft,
            width:oWidth,
            height:oHeight
        }
    }
};

$(document).ready(function () {
    $(document).bind('keypress', function (e) {
        if (mPopup.pwDone) {
            if (e.which == 13) {
                //do nothing,
            }
            //click the Esc key, it will close the pop-up window.
            if (e.keyCode == 27) {

                mPopup.close();
            }
        }
    });
});


jQuery.fn.mPopup = function (settings) {
    if (mPopup.ownder == this[0]) {
        return false;
    }
    mPopup.close();
    mPopup.owner = this[0];
    mPopup.show(settings);
};

jQuery.fn.mPopup.close = function () {
    mPopup.close();
};


jQuery.fn.mPopup.update = function (content) {
    mPopup.update(content);
}


