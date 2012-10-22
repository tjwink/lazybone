package com.jlusoft.lazybone.inject;

import static java.lang.annotation.RetentionPolicy.RUNTIME;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.Target;
/**
 * 获取系统服务器管理器，如WindowManager
 * |@InjectManager("Context.SERVICE")
 * |WindowManager mWindowManager;
 * @author tingjin
 *
 */
@Retention(RUNTIME)
@Target( { ElementType.FIELD})
public @interface InjectManager {
	String value();
}
