package com.jlusoft.lazybone.orm;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;
/**
 * 并不是所有dto的成员变量都需要保存到数据库中,这个annotation用于标识那些不用保存到数据库中成员域。
 * @author tingjin
 *
 */
@Retention(RetentionPolicy.RUNTIME)
@Target( { ElementType.FIELD})
public @interface Ignore {

}
