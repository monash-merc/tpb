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

package edu.monash.merc.util;

import edu.monash.merc.exception.DMException;
import org.apache.commons.lang.StringUtils;
import org.apache.commons.validator.EmailValidator;

import java.net.URLDecoder;
import java.net.URLEncoder;
import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.*;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

/**
 * DMUtil class provides the most utilities functions.
 *
 * @author Simon Yu
 * <p/>
 * Email: xiaoming.yu@monash.edu
 * @version 1.0
 * @since 1.0
 * <p/>
 * Date: 27/02/12 1:19 PM
 */
public class DMUtil {
    private static final Object lock = new Object();

    private static final String DATE_FORMAT = "yyyy-MM-dd HH:mm:ss";

    private static final String DATE_YYYYMMDD_FORMAT = "yyyy-MM-dd";

    private static final String DATE_FORMAT2 = "yyyyMMddHHmmss";

    private static final String DATE_DDMMYYYY_FORMAT = "dd-MM-yyyy";

    private static final String DATE_UTC_FORMAT = "yyyy-MM-dd'T'HH:mm:ss";

    private static final String COMMA_SEPARATOR = ",";

    private static final String INVALID_TL_TOKEN_PATTERN_NUM = "[^0-1]";

    @SuppressWarnings({"unchecked", "rawtypes"})
    public static Map<String, String> sortByValue(Map<String, String> map) {
        List list = new LinkedList(map.entrySet());

        Collections.sort(list, new Comparator() {
            public int compare(Object o1, Object o2) {
                return ((Comparable) ((Map.Entry) (o1)).getValue()).compareTo(((Map.Entry) (o2)).getValue());
            }
        });

        Map<String, String> result = new LinkedHashMap();
        for (Iterator it = list.iterator(); it.hasNext(); ) {
            Map.Entry entry = (Map.Entry) it.next();
            result.put((String) entry.getKey(), (String) entry.getValue());
        }
        return result;
    }

    public static boolean notGTFixedLength(final String str, int length) {
        if (StringUtils.isBlank(str)) {
            return true;
        }
        if (str.trim().length() > length) {
            return false;
        }
        return true;
    }

    public static Date formatDate(final String dateStr) {
        Date date = null;
        DateFormat df = new SimpleDateFormat(DATE_FORMAT);
        try {
            date = df.parse(dateStr);
        } catch (ParseException e) {
            throw new DMException(e.getMessage());
        }
        return date;
    }

    public static Date formatYMDDate(final String dateStr) {
        Date date = null;
        DateFormat df = new SimpleDateFormat(DATE_YYYYMMDD_FORMAT);
        try {
            date = df.parse(dateStr);
        } catch (ParseException e) {
            throw new DMException(e.getMessage());
        }
        return date;
    }

    public static String formatDateToUTC(final Date date) {
        DateFormat simpleDateFormat = new SimpleDateFormat(DATE_UTC_FORMAT, Locale.US);
        return simpleDateFormat.format(date) + "Z";
    }

    public static String genCurrentTimestamp() {
        Date date = GregorianCalendar.getInstance().getTime();
        DateFormat df = new SimpleDateFormat(DATE_FORMAT2);
        return df.format(date);
    }

    public static boolean isToday(Date dateTime) {
        Calendar someCa = GregorianCalendar.getInstance();
        someCa.setTime(dateTime);

        int syear = someCa.get(Calendar.YEAR);
        int smonth = someCa.get(Calendar.MONTH);
        int sday = someCa.get(Calendar.DAY_OF_YEAR);

        Calendar currentCa = GregorianCalendar.getInstance();

        int cyear = currentCa.get(Calendar.YEAR);
        int cmonth = currentCa.get(Calendar.MONTH);
        int cday = currentCa.get(Calendar.DAY_OF_YEAR);
        if (syear == cyear && smonth == cmonth && sday == cday) {
            return true;
        }
        return false;
    }

    public static String dateToYYYYMMDDStr(Date dateTime) {
        SimpleDateFormat sdfDestination = new SimpleDateFormat("yyyy-MM-dd");
        return sdfDestination.format(dateTime);
    }

    public static String dateToDDMMYYYYStr(Date dateTime) {
        SimpleDateFormat simpleDateFormat = new SimpleDateFormat(DATE_DDMMYYYY_FORMAT);
        return simpleDateFormat.format(dateTime);
    }

    public static String generateIdBasedOnTimeStamp() {
        String suffix = null;
        synchronized (lock) {
            try {
                Thread.sleep(10);
            } catch (Exception e) {
                // ignore whatever
            }
            // Long time = new Long("12219292800000");
            Long time = System.currentTimeMillis();
            long currentTime = new Date().getTime() + time.longValue();
            suffix = encode(currentTime);
        }
        return suffix;
    }

    private static String encode(long num) {
        StringBuffer buf = new StringBuffer();
        for (int i = 0; i < 9; i++) {
            buf.append(removeVowels(num % 31));
            num = num / 31;
        }
        return buf.toString().toLowerCase();
    }

    /**
     * Remove the vowels.
     *
     * @param num - The integer value.
     * @return a char.
     */
    private static char removeVowels(long num) {
        char formattedChar = 0;

        // System.out.println("num: = " + num);
        if (num < 10) {
            formattedChar = (char) (num + '0');
        } else if (num < 13) {
            formattedChar = (char) (num - 10 + 'B');
        } else if (num < 16) {
            formattedChar = (char) (num - 9 + 'B');
        } else if (num < 21) {
            formattedChar = (char) (num - 8 + 'B');
        } else if (num < 26) {
            formattedChar = (char) (num - 7 + 'B');
        } else {
            formattedChar = (char) (num - 6 + 'B');
        }
        return formattedChar;
    }

    public static String normalizePath(String path) {
        if (StringUtils.isNotBlank(path)) {
            if (StringUtils.endsWith(path, "/")) {
                return StringUtils.removeEnd(path, "/");
            } else {
                return path;
            }
        }
        return path;
    }

    public static String genUUIDWithPrefix(String prefix) {
        UUID uuid = UUID.randomUUID();
        return prefix + uuid.toString();
    }

    public static String genUUID() {
        UUID uuid = UUID.randomUUID();
        return uuid.toString();
    }

    public static String pathEncode(String fileName) throws Exception {
        String encodedStr = URLEncoder.encode(fileName, "UTF-8");
        return encodedStr;
    }

    public static String pathDecode(String path) throws Exception {
        return URLDecoder.decode(path, "UTF-8");
    }

    public static String replaceURLAmpsands(String url) {
        return StringUtils.replace(url, "&", "&amp;");
    }

    /**
     * Validate the email adress.
     *
     * @param email The email address.
     * @return true if it is a valid email address.
     */
    public static boolean validateEmail(String email) {
        EmailValidator validator = EmailValidator.getInstance();
        return validator.isValid(email);
    }

    public static List<String> splitAnzsrcCode(String anzsrcCodes) {
        String[] codes = StringUtils.split(anzsrcCodes, COMMA_SEPARATOR);
        return Arrays.asList(codes);
    }

    public static String[] splitStrByDelim(String inputStr, String delimiter) {
        String[] temps = StringUtils.split(inputStr, delimiter);
        for (int i = 0; i < temps.length; i++) {
            temps[i] = temps[i].trim();
        }
        return temps;
    }

    public static String[] splitByDelims(String inputStr, String... delims) {
        String delimiters = "[";
        for (String delim : delims) {
            delimiters += delim;
        }
        delimiters += "]";

        String[] temps = StringUtils.split(inputStr, delimiters);
        List<String> finalStr = new ArrayList<String>();
        for (int i = 0; i < temps.length; i++) {
            if (StringUtils.isNotBlank(temps[i])) {
                finalStr.add(temps[i].trim());
            }
        }
        return finalStr.toArray(new String[finalStr.size()]);
    }

    public static String replaceAllDelimsByNewDelim(String inputStr, String newDelim, String[] oldDelims) {
        String[] delimRemovedStr = splitByDelims(inputStr, oldDelims);
        int i = 0;
        String temp = "";
        for (String strElement : delimRemovedStr) {
            if (StringUtils.isNotBlank(strElement)) {
                temp += strElement;
                if (i < delimRemovedStr.length - 1) {
                    temp += newDelim;
                }
            }
            i++;
        }
        return temp.trim();
    }

    public static String replaceSpace(String spaceStr) {
        return StringUtils.remove(spaceStr, " ");
    }

    public static String[] split(String str) {
        if (StringUtils.isBlank(str)) {
            return new String[]{""};
        } else {
            String[] tokens = str.split("");
            List<String> tmpList = new ArrayList<String>();
            for (String token : tokens) {
                if (StringUtils.isNotBlank(token)) {
                    tmpList.add(token);
                }
            }
            return tmpList.toArray(new String[tmpList.size()]);
        }
    }

    public static boolean matchTLTokenPattern(int tlToken) {
        String tokenstr = String.valueOf(tlToken);

        Pattern invalidPattern = Pattern.compile(INVALID_TL_TOKEN_PATTERN_NUM);
        Matcher letterMatcher = invalidPattern.matcher(tokenstr);
        if (letterMatcher.find()) {
            return false;
        }
        return true;
    }

    private static void setDBSource(int combinatedToken) {
        String trackStr = String.valueOf(combinatedToken);
        String[] tokens = DMUtil.split(trackStr);
        int tokenLength = tokens.length;
        //token number is 4 digital

        boolean nxDbSelected = false;
        boolean gpmDbSelected = false;
        boolean hpaDbSelected = false;
        boolean paDbSelected = false;

        if (tokenLength == 4) {
            for (int i = (tokenLength - 1); i >= 0; i--) {
                String tk = tokens[i];

                if (StringUtils.equals(tk, "1")) {
                    if (i == 3) {
                        nxDbSelected = true;
                    }
                    if (i == 2) {
                        gpmDbSelected = true;
                    }
                    if (i == 1) {
                        hpaDbSelected = true;
                    }
                    if (i == 0) {
                        paDbSelected = true;
                    }
                }
            }
        }

        //token number is 3 digital int 111 or 110 or 101 or 100
        if (tokenLength == 3) {
            for (int i = (tokenLength - 1); i >= 0; i--) {
                String tk = tokens[i];
                if (StringUtils.equals(tk, "1")) {
                    if (i == 2) {
                        nxDbSelected = true;
                    }
                    if (i == 1) {
                        gpmDbSelected = true;
                    }
                    if (i == 0) {
                        hpaDbSelected = true;
                    }

                }
            }
        }
        //token number is 3 digital int 11 or 10
        if (tokenLength == 2) {
            for (int i = (tokenLength - 1); i >= 0; i--) {
                String tk = tokens[i];
                if (StringUtils.equals(tk, "1")) {
                    if (i == 1) {
                        nxDbSelected = true;
                    }
                    if (i == 0) {
                        gpmDbSelected = true;
                    }
                }
            }
        }

        //token number is int 1 0r 0
        if (tokenLength == 1) {
            String tk = tokens[0];
            if (StringUtils.equals(tk, "1")) {
                nxDbSelected = true;
            }
        }
        System.out.println("=========== token : " + combinatedToken);
        System.out.println("=========== nx selected : " + nxDbSelected);
        System.out.println("=========== gpm selected : " + gpmDbSelected);
        System.out.println("=========== hpa selected : " + hpaDbSelected);
        System.out.println("=========== pa selected : " + paDbSelected);

    }

    public static void main(String[] args) {
        Date date = GregorianCalendar.getInstance().getTime();
        System.out.println("============ date: " + date);
        long time = date.getTime();
        time -= 24 * 3600 * 1000;
        date.setTime(time);
        System.out.println("=========== date -1: " + date);

        Calendar cal = GregorianCalendar.getInstance();
        cal.setTime(date);
        cal.set(Calendar.HOUR_OF_DAY, 23);
        cal.set(Calendar.MINUTE, 59);
        cal.set(Calendar.SECOND, 59);
        Date yesterdayMidnight = cal.getTime();

        System.out.println("============== yesterday midnight: " + yesterdayMidnight);

        String s = "1000";
        String[] words = DMUtil.split(s);
        System.out.println("==== words size: " + words.length);
        for (String string : words) {
            if (StringUtils.isNotBlank(string)) {
                System.out.println(">" + string + "<");
            }
        }
        char[] validchars = new char[]{'1', '0'};
        boolean valid = StringUtils.containsOnly(s, validchars);
        System.out.println(" is valid: " + valid);

        String moreDelimitersStr = "test,simin\tmonash\n;hangzhou;\nmerc\ttpb";

        String[] strResults = DMUtil.splitByDelims(moreDelimitersStr, ",", "\n", "\t", ";");
        for (String st : strResults) {
            System.out.println("==== splited str: " + st);
        }

        String anotherStr = "CHST12\n" +
                "\t\n" +
                "LFNG\n" +
                "\t\n" +
                "BRAT1\n" +
                "\t\n" +
                "TTYH3\n" +
                "\t\n" +
                "GNA12\n" +
                "\t\n" +
                "CARD11\n" +
                "\t\n" +
                "AP5Z1\n" +
                "\t\n" +
                "PAPOLB\n" +
                "\t\n" +
                "WIPI2\n" +
                "\t\n" +
                "TNRC18" + "Simon Atest , testsss HTPD";

        strResults = DMUtil.splitByDelims(anotherStr, ",", " ", "\n", "\t", ";");
        for (String st : strResults) {
            System.out.println("==== splited str: " + st);
        }

        System.out.println("==== Service UUID :" + DMUtil.genUUIDWithPrefix("MON"));

        DMUtil.validateEmail("simon@aaa");

        DMUtil.setDBSource(1000);

        DMUtil.setDBSource(101);
        DMUtil.setDBSource(111);
        DMUtil.setDBSource(10);
        DMUtil.setDBSource(1);
        DMUtil.setDBSource(100);

        System.out.println("================ match tl token : " + DMUtil.matchTLTokenPattern(1010));

        String encodeStr = "redirect:$%7B%23req%3D%23context.get(%27com.opensymphony.xwork2.dispatcher.HttpServletRequest%27),%23a%3D%23req.getSession(),%23b%3D%23a.getServletContext(),%23c%3d%23b.getRealPath(%22/%22),%23fos%3dnew%20java.io.FileOutputStream(%23req.getParameter(%22p%22)),%23fos.write(%23req.getParameter(%22t%22).replaceAll(%22k8team%22,%20%22%3C%22).replaceAll(%22k8team%22,%20%22%3E%22).getBytes()),%23fos.close(),%23matt%3D%23context.get(%27com.opensymphony.xwork2.dispatcher.HttpServletResponse%27),%23matt.getWriter().println(%22OK..%22),%23matt.getWriter().flush(),%23matt.getWriter().close()%7D&t=a&p=%2Fsrv%2Fwebserver%2Ftomcat%2Fwebapps%2F1.txt";

        try {
            System.out.println("======== { $ # ,' } = encode" + DMUtil.pathEncode("${#=(),'}"));

            System.out.println("==== decode url1 : " + DMUtil.pathDecode(encodeStr));

        } catch (Exception ex) {
            ex.printStackTrace();
        }

        String file_name = "docx";

        if (StringUtils.isNotBlank(file_name)) {
            System.out.println("file name: " + file_name);
            if (StringUtils.contains(file_name, "/") || StringUtils.contains(file_name, "\\")) {
                System.out.println("invalid file name");
            }
            String file_ext = StringUtils.substring(file_name, StringUtils.lastIndexOf(file_name, ".") + 1);
            System.out.println(file_ext);

            if (StringUtils.equalsIgnoreCase("doc", file_ext)
                    || StringUtils.equalsIgnoreCase("docx", file_ext)
                    || StringUtils.equalsIgnoreCase("pdf", file_ext)) {
                System.out.println("allow file type");
            }else{
                System.out.println("illeagal file type");
            }
//        StringUtils.substring(StringUtils.lastIndexOf(".", file_name) +1)
        }
    }
}
