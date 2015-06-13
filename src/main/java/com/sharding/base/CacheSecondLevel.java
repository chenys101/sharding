package com.sharding.base;

public abstract interface CacheSecondLevel {
	public abstract Object get(String paramString);

	public abstract Object[] gets(String[] paramArrayOfString);

	public abstract void set(String paramString, Object paramObject);

	public abstract void delete(String paramString);
}
