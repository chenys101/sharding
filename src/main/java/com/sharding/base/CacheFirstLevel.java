package com.sharding.base;

public abstract interface CacheFirstLevel {
	public abstract Object get(String paramString);

	public abstract void set(String paramString, Object paramObject);

	public abstract void delete(String paramString);
}
