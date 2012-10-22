package com.jlusoft.lazybone.inject;

import static java.lang.annotation.RetentionPolicy.RUNTIME;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.Target;

/**
 * 为Activity中的Android View注入
 * 
 * 注入时需要指定控件id
 * 填入activity内的方法名到click,longclick,itemclick，可以实现自动安装监听器。
 * 指定的方法名字并没有限制，但对参数有限制，如OnClick需要View参数。
 * @author winktj
 *
 */
@Retention(RUNTIME)
@Target( { ElementType.FIELD})
public @interface InjectView {
	int id() ;
	String click() default "";
	String longclick() default "";
	String itemclick() default "";

}
