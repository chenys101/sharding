package com.sharding.base;

import java.lang.reflect.Method;
public class ValueGetter {
    private final Method method;
    private final String fieldName;
    private final String columnName;

    private String fieldPath;
    private Class referType;

    public ValueGetter(Method method, String fieldName) {
        this.method = method;
        this.fieldName = fieldName;
        this.columnName = fieldName;
    }

    public ValueGetter(Method method, String fieldName, String columName) {
        this.method = method;
        this.fieldName = fieldName;
        this.columnName = columName;
    }

    public Method getMethod() {
        return method;
    }

    public String getFieldName() {
        return fieldName;
    }

    public String getColumnName() {
        return columnName;
    }

    public String getFieldPath() {
        return fieldPath;
    }

    public void setFieldPath(String fieldPath) {
        this.fieldPath = fieldPath;
    }

    public Object get(Object obj) {
        try {
            return method.invoke(obj);
        } catch (Exception ex) {
            throw new RuntimeException(ex);
        }
    }

    public Class getReferType() {
        return referType;
    }

    public void setReferType(Class referType) {
        this.referType = referType;
    }

}

