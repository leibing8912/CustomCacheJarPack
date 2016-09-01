package cn.jianke.customcache.utils;

import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class StringUtil {

    public static boolean isEmpty(String str) {
        if (str == null || "".equals(str.trim())) {
            return true;
        }
        return false;
    }

    public static boolean isNotEmpty(String str){
		if (str == null || str.trim().equals(""))
			return false;
		return true;
    }

    public static boolean isNumber(String str) {
    	   return str.matches("[\\d]+");
    }

	public static boolean isFloatNumber(String str) {
	   return str.matches("[\\d.]+");
	}

    public static boolean isPhoneNumber(String phoneNumber) {
        String reg = "1[3,4,5,7,8]{1}\\d{9}";
        return phoneNumber.matches(reg);
    }

	public static boolean isPassword( String password ){
		Pattern p = Pattern.compile("^([0-9]|[a-zA-Z]){6,}$");                            
	    Matcher m = p.matcher(password); 
	    return m.matches();
	}

	public static String replaceBlank(String str) {
	    String dest = "";
	    if (str!=null) {
	        Pattern p = Pattern.compile("\\s*|\t|\r|\n");
	        Matcher m = p.matcher(str);
	        dest = m.replaceAll("");
	    }
	    return dest;
	}

	public static String substring(String str,int start,int end){
        if (isEmpty(str)) {
            return "";
        }
        int len = str.length();
        if (start > end) {
            return "";
        }
        if (start > len) {
            return "";
        }
        if (end > len) {
            return str.substring(start, len);
        }
        return str.substring(start,end);
    }

	public static int strToInt(String str){
		int i = 0;
		if(StringUtil.isNotEmpty(str)){
			try{
				i = Integer.parseInt(str);
			}catch(Exception e){
				e.printStackTrace();
			}
		}
		return i;
	}
}
