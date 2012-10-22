package com.jlusoft.lazybone.inject;

import static java.lang.annotation.RetentionPolicy.RUNTIME;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.Target;

/**
 * 实现资源注入
 * 
 * 目前只支持Drawable和String.
 * 
 * 
 * @author tingjin
 *
 */
@Retention(RUNTIME)
@Target( { ElementType.FIELD})
public @interface InjectResource {
	int id();
}
