package com.sharding.util;

import java.math.BigDecimal;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class WebUtil {
	
	// 根据静态化参数串获取相应参数值
	public static String getParam(String paramStr, String paramName) {
		if (paramStr == null || paramStr.trim().equals("")) {
			return "";
		}
		if (paramName == null || paramName.trim().equals("")) {
			return "";
		}
		paramStr = paramStr.replaceAll("^/*", "/").replaceAll("/*$", "");
		Pattern p = Pattern.compile("/" + paramName + "[^/]*",
				Pattern.CASE_INSENSITIVE);
		Matcher m = p.matcher(paramStr);
		String result = "";
		if (m.find()) {
			result = m.group().replaceFirst("/" + paramName, "");
		}
		return result;
	}
	
	/** ************************取默认value值的开始********************************** */
	public static String stringValue(String v, String def) {
		if (v == null || v.length() == 0)
			return def;
		return v.trim();
	}

	public static String[] stringArrayValue(String[] v, String[] def) {
		if (v == null || v.length == 0)
			return def;
		return v;
	}

	public static byte byteValue(String v, byte def) {
		if (v == null || v.length() == 0)
			return def;
		try {
			return Byte.parseByte(v);
		} catch (Exception e) {
			return def;
		}
	}

	public static char charValue(String v, char def) {
		if (v == null || v.length() == 0)
			return def;
		try {
			return (char) Integer.parseInt(v);
		} catch (Exception e) {
			return def;
		}
	}

	public static int intValue(String v, int def) {
		if (v == null || v.length() == 0)
			return def;
		try {
			return Integer.parseInt(v.trim());
		} catch (Exception e) {
			return def;
		}
	}

	public static Integer integerValue(String v) {
		return integerValue(v, null);
	}

	public static Integer integerValue(String v, int def) {
		if (Functions.isBlank(v))
			return new Integer(def);
		try {
			return Integer.valueOf(v);
		} catch (Exception e) {
			return new Integer(def);
		}
	}

	public static Integer integerValue(String v, Integer def) {
		if (Functions.isBlank(v))
			return def;
		try {
			return Integer.valueOf(v);
		} catch (Exception e) {
			return def;
		}
	}

	public static long longValue(String v, long def) {
		if (v == null || v.length() == 0)
			return def;
		try {
			return Long.parseLong(v.trim());
		} catch (Exception e) {
			return def;
		}
	}

	public static boolean booleanValue(String v, boolean def) {
		if (v == null || v.length() == 0)
			return def;

		if (v.equalsIgnoreCase("true") || v.equalsIgnoreCase("yes")
				|| v.equalsIgnoreCase("1")) {
			return true;
		} else if (v.equalsIgnoreCase("false") || v.equalsIgnoreCase("no")
				|| v.equalsIgnoreCase("0")) {
			return false;
		} else {
			return def;
		}
	}

	public static float floatValue(String v, float def) {
		if (v == null || v.length() == 0)
			return def;
		try {
			return Float.parseFloat(v.trim());
		} catch (Exception e) {
			return def;
		}
	}

	public static float floatValue(String v, int remain, float def) {
		try {
			BigDecimal bd = new BigDecimal(v);
			bd = bd.setScale(remain, BigDecimal.ROUND_HALF_UP);
			return bd.floatValue();
		} catch (Exception e) {
			return def;
		}
	}

	public static double doubleValue(String v, double def) {
		if (v == null || v.length() == 0)
			return def;
		try {
			return Double.parseDouble(v.trim());
		} catch (Exception e) {
			return def;
		}
	}

	public static Date dateValue(String v, Date def) {
		return dateValue(v, "yyyy-MM-dd", def);
	}

	public static Date dateTimeValue(String v, Date def) {
		return dateValue(v, "yyyy-MM-dd HH:mm:ss", def);
	}
	
	public static Date dateValue(String v, String fm, Date def) {
		if (v == null || v.length() == 0)
			return def;
		try {
			return new SimpleDateFormat(fm).parse(v.trim());
		} catch (Exception e) {
			return def;
		}
	}
	/** ************************取默认value值的结束********************************** */
}
