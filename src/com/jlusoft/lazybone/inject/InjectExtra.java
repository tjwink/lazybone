package com.jlusoft.lazybone.inject;

import static java.lang.annotation.RetentionPolicy.RUNTIME;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.Target;


/**
 * 注入从Intent获取的Extra数据。
 * 
 * value:Extra中的标识字符串
 * optional:标记是否有默认值
 * 如果找不到值就会返回null.
 * 
 * Intent返回的值有可能是空的，你可以定义一个默认值<br /> 
 * {@code @InjectExtra(value="someValue", optional=true) <br/> Integer someValue = 2;} ) <br/>
 * 当optional=false 时，不要给成员变量赋值！ {@code @InjectExtra("someValue") <br />Integer someValue = 2;<br /> // 不要这样做！}
 * 
 * @author tingjin
 * 
 */
@Retention(RUNTIME)
@Target( { ElementType.FIELD})
public @interface InjectExtra {
	String value();
	boolean optional() default false;
	
}
