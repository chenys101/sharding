package com.sharding.base;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Map;

import org.springframework.dao.DataAccessException;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.core.RowMapper;
import org.springframework.jdbc.core.namedparam.NamedParameterJdbcTemplate;

import com.alibaba.fastjson.JSON;

public class Dao {

	private JdbcTemplate jdbcTemplate;
	private NamedParameterJdbcTemplate namedParameterJdbcTemplate;
	private Orm orm;
	private SqlPageBuilder sqlPageBuilder;
	private IdGenerator idGenerator;
	private CacheFirstLevel cacheFirstLevel;
	private CacheSecondLevel cacheSecondLevel;
	
	public long create(Object entity) throws DataAccessException {
        Class type = entity.getClass();
        long id = generateId(type,"id");
        orm.setObjectId(entity, id);
        namedParameterJdbcTemplate.update(orm.getCreateSql(entity), orm.map(entity));
        return id;
    }
	public <T> long generateId(Class<T> type, String primaryKey) {
        Integer idAllocSize = orm.getIdAllocSize(type);
        return idGenerator.generate(orm.getTableName(type), primaryKey, idAllocSize.intValue());
    }
	
	public <T> T findDb(Class<T> type, Object id) throws DataAccessException {
        T obj = jdbcTemplate.queryForObject("select * from "+orm.getTableName(type)
        		+ " where id = ?", new RowMapperImpl<T>(type), id);
        return obj;
    }

    public <T> T findDb(Class<T> type, long x, Object id) throws DataAccessException {
        T obj = jdbcTemplate.queryForObject("select * from "+orm.getTableName(type, x)
    			+ " where id = ?", new RowMapperImpl<T>(type), id);
        return obj;
    }
	
	//------------------------Count Field Begin-------------------------
	public <T>int count(Class<T> type, String criteria, Object... params)
            throws DataAccessException {
        return jdbcTemplate.queryForObject("select count(*) from " + 
                orm.getTableName(type) + " where " + criteria, Integer.class, params);
    }

    public <T>int count(Class<T> type, String criteria, Map<String, ?> paramMap)
            throws DataAccessException {
        return jdbcTemplate.queryForObject("select count(*) from " + 
                orm.getTableName(type) + " where " + criteria, Integer.class, paramMap);
    }

    public <T>int count(Class<T> type, long x, String criteria,Object... params)
            throws DataAccessException {
        return jdbcTemplate.queryForObject("select count(*) from " + 
                orm.getTableName(type, x) + " where " + criteria, Integer.class, params);
    }

    public <T>int count(Class<T> type, long x, String criteria, 
            Map<String, ?> paramMap) throws DataAccessException {
        return jdbcTemplate.queryForObject("select count(*) from " + 
                orm.getTableName(type, x) + " where " + criteria, Integer.class, paramMap);
    }
	
    public <T>int count(Class<T> type, String x, String criteria,Object... params)
            throws DataAccessException {
        return jdbcTemplate.queryForObject("select count(*) from " + 
                orm.getTableName(type, x) + " where " + criteria, Integer.class, params);
    }

    public <T>int count(Class<T> type, String x, String criteria, 
            Map<String, ?> paramMap) throws DataAccessException {
        return jdbcTemplate.queryForObject("select count(*) from " + 
                orm.getTableName(type, x) + " where " + criteria, Integer.class, paramMap);
    }
    
    public <T>int count(String sql, Object... params)
            throws DataAccessException {
        return jdbcTemplate.queryForObject(sql, Integer.class, params);
    }

    public <T>int count(String sql, Map<String, ?> paramMap)
            throws DataAccessException {
        return jdbcTemplate.queryForObject(sql, Integer.class, paramMap);
    }
    //------------------------Count Field End-------------------------
	
    //------------------------ListPage Field Begin--------------------
    public <T> List<T> page(Class<T> type, String sql, int pageNo, int pageSize,
            Object... params) throws DataAccessException {
        return list(type, sqlPageBuilder.buildPageSql(sql, pageNo, pageSize), params);
    }
    
    public <T> List<T> page(Class<T> type, String sql, int pageNo, int pageSize,
            Map<String, ?> paramMap) throws DataAccessException {
        return list(type, sqlPageBuilder.buildPageSql(sql, pageNo, pageSize), paramMap);
    }

    public <T> List<T> page(Class<T> type, long x, String sql, int pageNo, 
            int pageSize, Object... params) throws DataAccessException {
        return list(type, x, sqlPageBuilder.buildPageSql(sql, pageNo, pageSize), params);
    }
    
    public <T> List<T> page(Class<T> type, long x, String sql, int pageNo, 
            int pageSize, Map<String, ?> paramMap) throws DataAccessException {
        return list(type, x, sqlPageBuilder.buildPageSql(sql, pageNo, pageSize), paramMap);
    }
    
    public <T> List<T> list(Class<T> type, String sql, Object... params)
            throws DataAccessException {
        if (cacheSecondLevel != null && orm.getSecondLevelCache(type)) {
            return list(type, jdbcTemplate.query(sql, longIdRowMapper, params), 0, false);
        } else {
            return jdbcTemplate.query(sql, new RowMapperImpl<T>(type), params);
        }
    }
    
    public <T> List<T> list(Class<T> type, String sql, Map<String, ?> paramMap)
            throws DataAccessException {
        if (cacheSecondLevel != null && orm.getSecondLevelCache(type)) {
            return list(type, jdbcTemplate.query(sql, longIdRowMapper, paramMap), 0, false);
        } else {
            return jdbcTemplate.query(sql, new RowMapperImpl<T>(type), paramMap);
        }
    }

    public <T> List<T> list(Class<T> type, long x, String sql, Object... params)
            throws DataAccessException {
        if (cacheSecondLevel != null && orm.getSecondLevelCache(type)) {
            return list(type, jdbcTemplate.query(sql, longIdRowMapper, params), x, true);
        } else {
            return jdbcTemplate.query(sql, new RowMapperImpl<T>(type), params);
        }
    }
    
    public <T> List<T> list(Class<T> type, long x, String sql, Map<String, ?> paramMap)
            throws DataAccessException {
        if (cacheSecondLevel != null && orm.getSecondLevelCache(type)) {
            return list(type, jdbcTemplate.query(sql, longIdRowMapper, paramMap), x, true);
        } else {
            return jdbcTemplate.query(sql, new RowMapperImpl<T>(type), paramMap);
        }
    }
    
    <T> List<T> list(Class<T> type, List idList, long x, boolean xx) {
        Object[] table = new Object[idList.size()];
        List<String> keyList = new ArrayList<String>();
        if (idList.isEmpty()) {
            return Collections.EMPTY_LIST;
        }
        
        for (int i = 0; i < idList.size(); i ++) {
            Object id = idList.get(i);
            String key = type.getSimpleName() + '-' + id;
            if (cacheFirstLevel != null && orm.getFirstLevelCache(type)) {
                Object o = cacheFirstLevel.get(key);
                if (o != null) {
                    table[i] = o;
                } else {
                    keyList.add(key);
                }
            } else {
                keyList.add(key);
            }
        }

        String[] keys = new String[keyList.size()];
        for (int i = 0; i < keys.length; i ++) {
            keys[i] = keyList.get(i);
        }
        
        if (! keyList.isEmpty()) {
            Object[] os = cacheSecondLevel.gets(keys);
            for (int i = 0; i < os.length; i ++) {
                for (int j = 0; j < table.length; j ++) {
                    if (table[j] == null) {
                        Object o = os[i];
                        if (o != null) {
                            table[j] = string2Object(type, (String)o);
                            if (cacheFirstLevel != null && orm.getFirstLevelCache(type)) {
                                cacheFirstLevel.set(keys[i], table[j]);
                            }
                        } else {
                            if (xx) {
                                table[j] = findDb(type, x, idList.get(j));
                            } else {
                                table[j] = findDb(type, idList.get(j));
                            }
                            if (table[j] != null) {
                                //setCache(keys[i], object2String(table[j]));
                            }
                        }
                        break;
                    }
                }
            }
        }

        List<T> result = new ArrayList<T>(table.length);
        for (int i = 0, c = table.length; i < c; ++i) {
            result.add((T)table[i]);
        }
        return result;
    }
    //------------------------ListPage Field End--------------------
    
    public static RowMapper<Long> longIdRowMapper = new RowMapper<Long>() {
        @Override
        public Long mapRow(ResultSet rs, int i) throws SQLException {
            return rs.getLong(1);
        }
    };

    public static RowMapper<String> stringIdRowMapper = new RowMapper<String>() {
        @Override
        public String mapRow(ResultSet rs, int i) throws SQLException {
            return rs.getString(1);
        }
    };
    
    private class RowMapperImpl<T> implements RowMapper<T> {
        Class<T> type;
        public RowMapperImpl(Class<T> type) {
            this.type = type;
        }

        @Override
        public T mapRow(ResultSet rs, int i) throws SQLException {
            return orm.map(type, rs);
        }
    }
    
    public static <T> T string2Object(Class<T> type, String s) {
        try {
            return JSON.parseObject(s, type);
        } catch (Exception ex) {
            throw new RuntimeException(ex);
        }
    }
    
	public <T>String getTableName(Class<T>  type) {
		return orm.getTableName(type);
	}
    
	/**
	 * With split table, any SQL user define must use this method get table name
	 * @param type 	Entity type.
	 * @param x 	Split field value.
	 * @return
	 */
	public <T>String getTableName(Class<T>  type, long x) {
		return orm.getTableName(type, x);
	}
	
	/**
	 * 4 split table, hash string field like url
	 * @param Entity type.
	 * @param Split field value.
	 * @return
	 */
	public <T>String getTableName(Class<T>  type, String x) {
		return orm.getTableName(type, x);
	}
	
	//---------------Getter and Setter-------------------
	public void setIdGenerator(IdGenerator idGenerator) {
		this.idGenerator = idGenerator;
	}

	public void setOrm(Orm orm) {
		this.orm = orm;
	}

	public void setJdbcTemplate(JdbcTemplate simpleJdbcTemplate) {
		this.jdbcTemplate = simpleJdbcTemplate;
		this.namedParameterJdbcTemplate = new NamedParameterJdbcTemplate(
				jdbcTemplate);
	}

	public JdbcTemplate getJdbcTemplate() {
		return jdbcTemplate;
	}

	public NamedParameterJdbcTemplate getNamedParameterJdbcTemplate() {
		return namedParameterJdbcTemplate;
	}
	
	public void setSqlPageBuilder(SqlPageBuilder sqlPageBuilder) {
		this.sqlPageBuilder = sqlPageBuilder;
	}
}
