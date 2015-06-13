package com.sharding.util;

import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.Random;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class Functions {

	public static boolean isEmpty(String str) {
		return (str == null || str.length() == 0);
	}

	public static boolean isBlank(String str) {
		return (str == null || str.trim().length() == 0);
	}

	public static boolean isNumeric(String str) {
		if (str == null || str.trim().length() == 0) {
			return false;
		}
		int offset = 0;
		if (str.startsWith("-") || str.startsWith("+")) {
			if (str.length() > 1) {
				offset = 1;
			} else {
				return false;
			}
		}
		for (int i = offset; i < str.length(); i++) {
			if (!Character.isDigit(str.charAt(i))) {
				return false;
			}
		}
		return true;
	}

	public static String toUTF8(String arg) {
		byte b[] = arg.getBytes();
		char c[] = new char[b.length];
		for (int x = 0; x < b.length; x++)
			c[x] = (char) (b[x] & 0xff);
		return new String(c);
	}

	public static Date getNow() {
		return new Date(System.currentTimeMillis());
	}

	// �õ������賿��ʱ��.
	public static Date getTodayBegin() {
		return getDayBegin(getNow());
	}

	// �õ�����23��59 ʱ��
	public static Date getTodayEnd() {
		return getDayEnd(getNow());
	}

	// �õ�ĳһ����賿 00:00:00ʱ��.
	public static Date getDayBegin(Date date) {
		Calendar cal = Calendar.getInstance();
		cal.setTime(date);
		cal.set(Calendar.HOUR_OF_DAY, 0);
		cal.set(Calendar.MINUTE, 0);
		cal.set(Calendar.SECOND, 0);
		cal.set(Calendar.MILLISECOND, 0);
		return cal.getTime();
	}
	
	// �õ�ĳһ���23:59:59 ʱ��
	public static Date getDayEnd(Date date) {
		Calendar cal = Calendar.getInstance();
		cal.setTime(date);
		cal.set(Calendar.HOUR_OF_DAY, 23);
		cal.set(Calendar.MINUTE, 59);
		cal.set(Calendar.SECOND, 59);
		cal.set(Calendar.MILLISECOND, 999);
		return cal.getTime();
	}

	// �õ�24Сʱ��ĳСʱ�Ŀ�ʼʱ��
	public static Date getHourBegin(Date date, int hour) {
		Calendar cal = Calendar.getInstance();
		cal.setTime(date);
		cal.set(Calendar.HOUR_OF_DAY, hour);
		cal.set(Calendar.MINUTE, 0);
		cal.set(Calendar.SECOND, 0);
		cal.set(Calendar.MILLISECOND, 0);
		return cal.getTime();
	}

	// �õ�24Сʱ��ĳСʱ��ĩβʱ��
	public static Date getHourEnd(Date date, int hour) {
		Calendar cal = Calendar.getInstance();
		cal.setTime(date);
		cal.set(Calendar.HOUR_OF_DAY, hour);
		cal.set(Calendar.MINUTE, 59);
		cal.set(Calendar.SECOND, 59);
		cal.set(Calendar.MILLISECOND, 999);
		return cal.getTime();
	}

	public static int getYear(Date date) {
		Calendar cld = Calendar.getInstance();
		cld.setTime(date);
		return cld.get(Calendar.YEAR);
	}

	// ���ص������� ��Ȼ��-1 Ҳ����˵���ص����Ǵ� 0--11
	public static int getMonth(Date date) {
		Calendar cld = Calendar.getInstance();
		cld.setTime(date);
		return cld.get(Calendar.MONTH);
	}

	public static int getDate(Date date) {
		Calendar cld = Calendar.getInstance();
		cld.setTime(date);
		return cld.get(Calendar.DAY_OF_MONTH);
	}

	public static int getHour(Date date) {
		Calendar cld = Calendar.getInstance();
		cld.setTime(date);
		return cld.get(Calendar.HOUR_OF_DAY);
	}

	public static String format(Date date, String fmt) {
		if (date == null)
			return "";
		DateFormat formatter = new SimpleDateFormat(fmt);
		return formatter.format(date);
	}

	public static String format(Date date) {
		return format(date, "yyyy-MM-dd");
	}

	public static String format() {
		return format(new Date(System.currentTimeMillis()), "yyyy-MM-dd");
	}

	// �õ�������ڿ�ʼ��ʱ��,���ڵĿ�ʼ��getFirstDayOfWeek()�õ�
	public static Date getThisWeekStart() {
		Calendar cal = Calendar.getInstance();
		cal.add(Calendar.DATE, -(cal.get(Calendar.DAY_OF_WEEK) - 1));
		return getDayBegin(cal.getTime());
	}

	// ���µĿ�ʼ
	public static Date getThisMonthStart() {
		Calendar cal = Calendar.getInstance();
		cal.set(Calendar.DAY_OF_MONTH, 1);
		return getDayBegin(cal.getTime());
	}

	// ���µĿ�ʼ
	public static Date getMonthStart(Date date) {
		Calendar cal = Calendar.getInstance();
		cal.setTime(date);
		cal.set(Calendar.DAY_OF_MONTH, 1);
		return getDayBegin(cal.getTime());
	}
	
	//����������ȡ����ĳ�¿�ʼ����
	public static Date getTheMonthStart(int monthNums) {
		Calendar cal = Calendar.getInstance();
		cal.setTime(getThisMonthStart());
		cal.add(Calendar.MONTH, monthNums);
		return cal.getTime();
	}

	// n��ǰ��� + -
	public static Date addDay(Date date, int day) {
		Calendar cal = Calendar.getInstance();
		cal.setTime(date);
		cal.add(Calendar.DATE, day);
		return new Date(cal.getTime().getTime());
	}

	// n��ǰ��� + -
	public static Date addMonth(Date date, int month) {
		Calendar cal = Calendar.getInstance();
		cal.setTime(date);
		cal.add(Calendar.MONTH, month);
		return new Date(cal.getTime().getTime());
	}

	// n��ǰ��� + -
	public static java.util.Date addYear(java.util.Date date, int year) {
		Calendar cal = Calendar.getInstance();
		cal.setTime(date);
		cal.add(Calendar.YEAR, year);

		return new java.util.Date(cal.getTime().getTime());
	}

	// ������������֮�������
	public static int getDays(Date date1, Date date2) {
		int days = 0;
		days = (int) (Math.abs((date2.getTime() - date1.getTime())) / (24 * 60 * 60 * 1000));
		return days;
	}

	public static int getDays(String date1, String date2) {
		Date d1 = WebUtil.dateValue(date1, "yyyy-MM-dd HH:mm:ss", new Date());
		Date d2 = WebUtil.dateValue(date2, "yyyy-MM-dd HH:mm:ss", new Date());
		return getDays(d1, d2);
	}

	// ������������֮���ʱ��� ��ϸ���� ��������ΪString
	public static String getDayDif(Date date1, Date date2) {
		long DAY = 24 * 60 * 60 * 1000;
		long between = Math.abs((date2.getTime() - date1.getTime()));
		long day = between / DAY;
		long hour = (between / (60 * 60 * 1000) - day * 24);
		long min = ((between / (60 * 1000)) - day * 24 * 60 - hour * 60);
		long s = (between / 1000 - day * 24 * 60 * 60 - hour * 60 * 60 - min * 60);
		return "" + day + "��" + hour + "Сʱ" + min + "��" + s + "��";
	}

	//ʱ���ʽΪyyyy-MM-dd HH:mm:ss
	public static String getDayDif(String date1, String date2) {
		Date d1 = WebUtil.dateValue(date1, "yyyy-MM-dd HH:mm:ss", new Date());
		Date d2 = WebUtil.dateValue(date2, "yyyy-MM-dd HH:mm:ss", new Date());
		return getDayDif(d1, d2);
	}

	/** ȡ��һ���������ĵ��ַ������ֽڳ��� */
	public static int getLen4ZH(String s) {
		return s == null ? 0 : s.replaceAll("[^\\x00-\\xff]", "12").length();
	}


	// ��ð������ֺ���ĸ������ַ���,bitΪλ��
	public static String getRandomStr(int bit) {
		String[] str = { "0", "1", "2", "3", "4", "5", "6", "7", "8", "9", "A",
				"B", "C", "D", "E", "F", "G", "H", "I", "J", "K", "L", "M",
				"N", "O", "P", "Q", "R", "S", "T", "U", "V", "W", "X", "Y", "Z" };
		Random rdm = new Random();
		StringBuilder sb = new StringBuilder();
		for (int j = 0; j < bit; j++) {
			sb.append(str[rdm.nextInt(36)]);
		}
		return sb.toString();
	}

	// ��ð������ֵ�����ַ���,bitΪλ��
	public static String getNumberRandomStr(int bit) {
		String[] str = { "0", "1", "2", "3", "4", "5", "6", "7", "8", "9" };
		Random rdm = new Random();
		StringBuilder sb = new StringBuilder();
		for (int j = 0; j < bit; j++) {
			sb.append(str[rdm.nextInt(10)]);
		}
		return sb.toString();
	}

	//remove head-end newlines
	public static String cutHeadEndNewlines(String content) {
		if (null == content)
			return "";
		Pattern pattern = Pattern.compile("^[\r\n]+|[\r\n]+$");
		Matcher matcher = pattern.matcher(content);
		return matcher.replaceAll("");
	}
}