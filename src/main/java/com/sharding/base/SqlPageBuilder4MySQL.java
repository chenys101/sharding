package com.sharding.base;

public class SqlPageBuilder4MySQL implements SqlPageBuilder {
	
	@Override
    public String buildPageSql(String sql, int pageNo, int pageSize) {
        return sql + " LIMIT " + (pageNo - 1) * pageSize + "," + pageSize;
    }
}