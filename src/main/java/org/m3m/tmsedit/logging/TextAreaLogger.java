package org.m3m.tmsedit.logging;

import javafx.application.Platform;
import javafx.scene.control.TextArea;
import javafx.scene.text.Font;
import lombok.*;

import java.util.*;

public class TextAreaLogger implements System.Logger {

	public static TextAreaLogger log;

	private final Queue<String> toLog = new LinkedList<>();

	private final TextArea logArea;

	@Getter @Setter
	private boolean isFlushing = false;

	public TextAreaLogger(TextArea logField) {
		this.logArea = logField;
		logField.setEditable(false);
		logField.setFont(Font.font("Monospaced", 13));
		logField.setWrapText(true);

		TextAreaLogger.log = this;
	}

	public void flush() {
		if (!Platform.isFxApplicationThread()) {
			Platform.runLater(this::flush);
			return;
		}

		synchronized (toLog) {
			while (!toLog.isEmpty()) {
				String message = toLog.remove();
				System.out.print(message);
				logArea.appendText(message);
				logArea.setScrollTop(1);
			}
		}
	}

	@Override
	public String getName() {
		return "Application Logger";
	}

	@Override
	public boolean isLoggable(Level level) {
		return true;
	}

	@Override
	public void log(Level level, ResourceBundle bundle, String msg, Throwable thrown) {
		synchronized (toLog) {
			toLog.add(String.format("[%s] %7s: %s: %s\n",
					Thread.currentThread().getName(),
					level, msg, thrown.toString()));
		}

		if (isFlushing)
			flush();
	}

	@Override
	public void log(Level level, ResourceBundle bundle, String format, Object... params) {
		synchronized (toLog) {
			toLog.add(String.format("[%s] %7s: %s\n",
					Thread.currentThread().getName(),
					level, String.format(format, params)));
		}

		if (isFlushing)
			flush();
	}
}