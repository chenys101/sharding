package com.sharding.base;

public interface SqlPageBuilder {
	public String buildPageSql(String sql, int pageNo, int pageSize);
}
