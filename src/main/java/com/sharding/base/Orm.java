package com.sharding.base;

import java.io.IOException;
import java.lang.reflect.Field;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.lang.reflect.Modifier;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.gelivable.dao.ValueGetter;
import org.springframework.beans.factory.config.BeanDefinition;
import org.springframework.context.annotation.ClassPathScanningCandidateComponentProvider;
import org.springframework.core.type.classreading.MetadataReader;
import org.springframework.core.type.classreading.MetadataReaderFactory;
import org.springframework.core.type.filter.TypeFilter;
import org.springframework.util.ClassUtils;

public class Orm {

	private String tablePrefix = "sharding_";
	private final Map<Class, String> tableNameMap = new HashMap<Class, String>();
	private final Map<Class, Table> tableMap = new HashMap<Class, Table>();
	private final Map<Class, boolean[]> cacheMap = new HashMap<Class, boolean[]>();
	private final Map<Class, Integer> idAllocSizeMap = new HashMap<Class, Integer>();
	private final Map<String, Class> entityMap = new HashMap<String, Class>();
	private final Map<Class, List<ValueSetter>> setterMap = new HashMap<Class, List<ValueSetter>>();
	private final Map<Class, List<ValueGetter>> getterMap = new HashMap<Class, List<ValueGetter>>();
	private final Map<Class, Method> setIdMap = new HashMap<Class, Method>();
	private final Map<Class, String> createSqlMap = new HashMap<Class, String>();
	
	public void setEntityList(List<Class> typeList) {
		for (Class type : typeList) {
			registerEntity(type);
		}
	}

	public void setEntityPackage(String entityPackage) {
		ClassPathScanningCandidateComponentProvider scan = new ClassPathScanningCandidateComponentProvider(
				false);

		scan.addIncludeFilter(new TypeFilter() {
			public boolean match(MetadataReader metadataReader,
					MetadataReaderFactory metadataReaderFactory)
					throws IOException {
				return true;
			}
		});

		List<Class> list = new ArrayList<Class>();
		for (BeanDefinition candidate : scan
				.findCandidateComponents(entityPackage)) {
			try {
				Class cls = ClassUtils.resolveClassName(
						candidate.getBeanClassName(),
						ClassUtils.getDefaultClassLoader());
				list.add((Class) cls);
			} catch (IllegalArgumentException ex) {
				throw new RuntimeException(ex);
			}
		}

		setEntityList(list);
	}

	public void setEntityPackageList(List<String> entityPackageList) {
		for (String entityPackage : entityPackageList) {
			setEntityPackage(entityPackage);
		}
	}

	public Orm() {
	}

	public Orm(String tablePrefix) {
		this.tablePrefix = tablePrefix;
	}

	public <T>void registerEntity(Class<T> type) {
		prepareCacheMap(type);
		prepareIdAllocSizeMap(type);
		prepareSetterGetterMap(type);
		prepareTableMap(type);
		prepareSetIdMap(type);
		prepareCreateSqlMap(type);
		entityMap.put(type.getSimpleName().toUpperCase(), type);
	}
	
	private <T>void prepareIdAllocSizeMap(Class<T> type) {
        Entity annotation = (Entity) type.getAnnotation(Entity.class);
        if (annotation != null && annotation.idAllocSize() > 0) {
        	System.err.println(annotation.idAllocSize());
        	idAllocSizeMap.put(type, annotation.idAllocSize());
        }
    }
	
	public <T>Integer getIdAllocSize(Class<T> type) {
        return idAllocSizeMap.get(type);
    }
	
	private <T>void prepareTableMap(Class<T> type) {
		Entity annotation = (Entity) type.getAnnotation(Entity.class);
		Table table = null;
		String tableName = null;
		if (annotation != null) {
			if (!"".equals(annotation.tableName())) {
				tableName = annotation.tableName();
			}

			String split = annotation.split();
			if (!"".equals(split)) {
				table = new Table(split);
			}
		}

		if (tableName == null) {
			String className = type.getSimpleName();
			tableName = tablePrefix
					+ Character.toLowerCase(className.charAt(0))
					+ className.substring(1);
		}

		tableNameMap.put(type, tableName);
		if (table != null) {
			table.setName(tableName);
			tableMap.put(type, table);
		}
	}

	private <T>void prepareCacheMap(Class<T> type) {
		Entity annotation = (Entity) type.getAnnotation(Entity.class);
		if (annotation != null) {
			if (annotation.firstLevelCache() && !annotation.secondLevelCache()) {
				System.out.println("WARN: " + type
						+ " firstLevelCache true but secondLevelCache false!");
			}
			boolean[] cache = new boolean[2];
			if (annotation.firstLevelCache()) {
				cache[0] = true;
			}
			if (annotation.secondLevelCache()) {
				cache[1] = true;
			}
			cacheMap.put(type, cache);
		}
	}
	
	private <T>void prepareSetterGetterMap(Class<T> type) {
        List<ValueSetter> listSetter = new ArrayList<ValueSetter>();
        List<ValueGetter> listGetter = new ArrayList<ValueGetter>();
        for (Field field : type.getDeclaredFields()) {
            int modifiers = field.getModifiers();
            Transient tt = field.getAnnotation(Transient.class);
            if (! isSupportedProperty(field.getType())) {
                continue;
            }
            if (tt == null && !Modifier.isTransient(modifiers) && !Modifier.isStatic(modifiers)) {
                String fieldName = field.getName();
                String columnName = fieldName;
                Method setter = setter(type, field);
                Method getter = getter(type, field);
                DBColumn annotation = (DBColumn) field.getAnnotation(DBColumn.class);
                if (annotation != null) {
                    columnName = annotation.name();
                }
                if (setter != null && getter != null) {
                    ValueSetter vs = new ValueSetter(setter, fieldName, columnName);
                    ValueGetter vg = new ValueGetter(getter, fieldName, columnName);
                    listSetter.add(vs);
                    listGetter.add(vg);
                }
            }
        }
        if (listSetter.size() > 0) {
            setterMap.put(type, listSetter);
            getterMap.put(type, listGetter);
        }
    }
	
	private <T>void prepareCreateSqlMap(Class<T> type) {
        StringBuilder buf = new StringBuilder();
        buf.append("insert into ").append(tableNameMap.get(type)).append(" (");
        for (ValueSetter setter : setterMap.get(type)) {
            buf.append(setter.getColumnName()).append(',');
        }
        buf.setCharAt(buf.length() - 1, ')');
        buf.append(" values (");
        for (ValueSetter setter : setterMap.get(type)) {
            buf.append(':').append(setter.getColumnName()).append(',');
        }
        buf.setCharAt(buf.length() - 1, ')');
        createSqlMap.put(type, buf.toString());
    }
	
	private static <T>Method setter(Class<T> type, Field field) {
        String fieldName = field.getName();
        String methodName = "set" + Character.toUpperCase(fieldName.charAt(0))
                + fieldName.substring(1);
        Method result = null;
        try {
            result = type.getMethod(methodName, field.getType());
        } catch (NoSuchMethodException ex) {
            throw new RuntimeException(ex);
        }
        if (result == null) {
            return null;
        }
        if (result.getReturnType() != void.class) {
            return null;
        }
        return result;
    }
	
	private static <T>Method getter(Class<T> type, Field field) {
        String fieldName = field.getName();
        String methodName = "get" + Character.toUpperCase(fieldName.charAt(0))
                + fieldName.substring(1);
        Method result = null;
        try {
            result = type.getMethod(methodName);
        } catch (NoSuchMethodException ex) {
            if (field.getType() == boolean.class
                    || field.getType() == Boolean.class) {
                methodName = "is" + Character.toUpperCase(fieldName.charAt(0))
                        + fieldName.substring(1);
                try {
                    result = type.getMethod(methodName);
                } catch (NoSuchMethodException ex1) {
                    throw new RuntimeException(ex1);
                }
            } else {
                throw new RuntimeException(ex);
            }
        }
        if (result == null) {
            return null;
        }
        if (result.getReturnType() != field.getType()) {
            return null;
        }
        return result;
    }
	
	private <T>boolean isSupportedProperty(Class<T> type) {
		return type == String.class || type == long.class || type == int.class
				|| type == Date.class || type == boolean.class
				|| type == double.class || type == long[].class
				|| type == Long.class || type == Integer.class
				|| type == Boolean.class || type == Double.class
				|| type == byte.class || type == short.class
				|| type == float.class || type == Byte.class
				|| type == Short.class || type == Float.class;
	}

	public <T>String getTableName(Class<T> type) {
		return tableNameMap.get(type);
	}

	public <T>String getTableName(Class<T> type, long x) {
		Table table = tableMap.get(type);
		return table.getName(x);
	}

	public <T>String getTableName(Class<T> type, String x) {
		Table table = tableMap.get(type);
		return table.getName(x);
	}

	public <T>boolean getFirstLevelCache(Class<T> type) {
		boolean[] cache = cacheMap.get(type);
		if (cache == null) {
			return false; // default fasle
		}
		return cache[0];
	}

	public <T>boolean getSecondLevelCache(Class<T> type) {
		boolean[] cache = cacheMap.get(type);
		if (cache == null) {
			return true; // default true
		}
		return cache[1];
	}

	public Object getSplitFiledValue(Object entity) {
		Table table = tableMap.get(entity.getClass());
		if (table == null) {
			return null;
		}
		try {
			return table.getSplitMethod().invoke(entity);
		} catch (IllegalAccessException ex) {
			throw new RuntimeException(ex);
		} catch (IllegalArgumentException ex) {
			throw new RuntimeException(ex);
		} catch (InvocationTargetException ex) {
			throw new RuntimeException(ex);
		}
	}
	
	private <T>void prepareSetIdMap(Class<T> type) {
        String methodName = "setId";
        try {
            setIdMap.put(type, type.getMethod(methodName, long.class));
        } catch (NoSuchMethodException ex) {
            throw new RuntimeException(ex);
        } catch (SecurityException ex) {
            throw new RuntimeException(ex);
        }
    }
	
	public String getCreateSql(Object entity) {
        Class type = entity.getClass();
        Table table = tableMap.get(type);
        if (table == null) {
            return createSqlMap.get(type);
        } else {
            return createSqlMap.get(type).replaceFirst(
                    table.getName(), table.getName(entity));
        }
    }
	
	public List<Class> getEntityList() {
		List<Class> result = new ArrayList<Class>(entityMap.size());
		result.addAll(entityMap.values());
		Collections.sort(result, new Comparator<Class>() {
			public int compare(Class o1, Class o2) {
				return o1.getName().compareTo(o2.getName());
			}
		});
		return result;
	}
	
	public <T> T map(Class<T> type, ResultSet rs) throws SQLException {
        T obj = null;
        try {
            obj = type.newInstance();
            for (ValueSetter setter : setterMap.get(type)) {
                setter.set(obj, rs);
            }
        } catch (InstantiationException ex) {
            throw new RuntimeException(ex);
        } catch (IllegalAccessException ex) {
            throw new RuntimeException(ex);
        }

        return obj;
    }
	
	public Map<String, Object> map(Object obj) {
        Map<String, Object> m = new HashMap<String, Object>();
        for (ValueGetter getter : getterMap.get(obj.getClass())) {
            m.put(getter.getColumnName(), getter.get(obj));
        }
        return m;
    }
	
	public void setObjectId(Object obj, Object id) {
		try {
            Method setId = setIdMap.get(obj.getClass());
            setId.invoke(obj, id);
        } catch (IllegalAccessException ex) {
            throw new RuntimeException(ex);
        } catch (IllegalArgumentException ex) {
            throw new RuntimeException(ex);
        } catch (InvocationTargetException ex) {
            throw new RuntimeException(ex);
        }
    }
}
