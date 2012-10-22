package com.jlusoft.lazybone.inject;

import static java.lang.annotation.RetentionPolicy.RUNTIME;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.Target;

import android.R.integer;

/**
 * 实现fragment注入。务必使用FragmentActivity
 * @author tingjin
 *
 */
@Retention(RUNTIME)
@Target( { ElementType.FIELD})
public @interface InjectFragment {
	int value();
}
