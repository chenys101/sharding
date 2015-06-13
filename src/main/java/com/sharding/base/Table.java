package com.sharding.base;

import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.util.Date;

public class Table {

	static final char YEAR = 'y';
	static final char MONTH = 'm';
	static final String FORMAT_YEAR = "%1$tY";
	static final String FORMAT_MONTH = "%1$tY%1$tm";

	private String name;
	private String splitField;
	private int mod;
	private char date;
	private boolean hash;
	private Method splitMethod;

	/**
	 * Mod:splitField:10 Date:splitField:(y|m) Hash:splitField:10
	 * 
	 * @param define
	 */
	public Table(String define) {
		String[] fields = define.split(":");
		if (fields.length != 3 && fields.length != 2) {
			throw new IllegalArgumentException(define);
		}
		splitField = fields[1];
		if ("Mod".equals(fields[0])) {
			mod = Integer.parseInt(fields[2]);
		} else if ("Date".equals(fields[0])) {
			date = fields[2].charAt(0);
			if (date != YEAR && date != MONTH) {
				throw new IllegalArgumentException(define);
			}
		} else if ("Hash".equals(fields[0])) {
			hash = true;
			mod = Integer.parseInt(fields[2]);
		} else {
			throw new IllegalArgumentException(define);
		}
	}

	public Method getSplitMethod() {
		return splitMethod;
	}

	public void setSplitMethod(Method splitMethod) {
		this.splitMethod = splitMethod;
	}

	public String getSplitField() {
		return splitField;
	}

	public void setSplitField(String splitField) {
		this.splitField = splitField;
	}

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	public String getName(Object entity) {
		if (splitField == null) {
			return name;
		}
		long l = 0;
		String str = "";
		try {
			Object x = splitMethod.invoke(entity);
			if (x instanceof Date) {
				l = ((Date) x).getTime();
			} else if (x instanceof Number) {
				l = ((Number) x).longValue();
			} else if (x instanceof String) {
				str = (String) x;
			}
		} catch (IllegalAccessException ex) {
			throw new RuntimeException(ex.getMessage(), ex);
		} catch (IllegalArgumentException ex) {
			throw new RuntimeException(ex.getMessage(), ex);
		} catch (InvocationTargetException ex) {
			throw new RuntimeException(ex.getMessage(), ex);
		}
		if (mod > 0) {
			return name + '_' + l % mod;
		}
		if (date == YEAR) {
			return name + '_' + String.format(FORMAT_YEAR, new Date(l));
		}
		if (date == MONTH) {
			return name + '_' + String.format(FORMAT_MONTH, new Date(l));
		}
		if (hash) {
			return name + '_' + rsHash(str) % mod;
		}

		return name;
	}

	public String getName(Date x) {
		return getName(x.getTime());
	}

	public String getName(long x) {
		if (splitField == null) {
			return name;
		}
		if (mod > 0) {
			long modValue = x % mod;
			if (modValue > 0) {
				return name + '_' + modValue;
			}
		}
		if (date == YEAR) {
			return name + '_' + String.format(FORMAT_YEAR, new Date(x));
		}
		if (date == MONTH) {
			return name + '_' + String.format(FORMAT_MONTH, new Date(x));
		}
		return name;
	}

	public String getName(String x) {
		if (splitField == null) {
			return name;
		}
		if (hash) {
			long modValue = rsHash(x) % mod;
			if (modValue > 0) {
				return name + '_' + modValue;
			}
		}
		return name;
	}
	
	private long rsHash(String str) {
		if (null == str)
			return 0;
		int b = 378551;
		int a = 63689;
		long hash = 0;
		for (int i = 0; i < str.length(); i++) {
			hash = hash * a + str.charAt(i);
			a = a * b;
		}
		return hash > 0 ? hash : Math.abs(hash);
	}

	public int getMod() {
		return mod;
	}
}
