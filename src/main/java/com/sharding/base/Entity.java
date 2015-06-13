package com.sharding.base;

import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

@Target({ java.lang.annotation.ElementType.TYPE })
@Retention(RetentionPolicy.RUNTIME)
public @interface Entity {
	public abstract boolean firstLevelCache() default false;

	public abstract boolean secondLevelCache() default false;
	
	public abstract String tableName() default "";

	public abstract String split() default "";
	
	int idAllocSize() default 10;
}
