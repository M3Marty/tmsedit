package org.m3m.tmsedit.history;

import lombok.AllArgsConstructor;
import lombok.Getter;

import java.lang.annotation.ElementType;
import java.lang.annotation.Target;

@Target(ElementType.METHOD)
public @interface Undo {

	Type value() default Type.COMMON;

	@Getter
	@AllArgsConstructor
	enum Type {
		COMMON(HistoryEvent.class),
		LOGGED(LoggedHistoryEvent.class),
		SKIPPED(SkippedHistoryEvent.class);

		@SuppressWarnings("rawtypes")
		private final Class<? extends HistoryEvent> eventType;
	}
}