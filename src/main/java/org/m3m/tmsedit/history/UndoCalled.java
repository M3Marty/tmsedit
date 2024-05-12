package org.m3m.tmsedit.history;

import java.lang.annotation.ElementType;
import java.lang.annotation.Target;

@Target(ElementType.METHOD)
public @interface UndoCalled {
	String[] value();
}