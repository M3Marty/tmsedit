package org.m3m.tmsedit.history;

import javafx.scene.control.Label;
import lombok.RequiredArgsConstructor;

import static java.lang.System.Logger.Level.DEBUG;
import static org.m3m.tmsedit.logging.TextAreaLogger.log;

@RequiredArgsConstructor
public class HistoryStack {

	private final Label historyId;

	private HistoryEvent<?> currentState = new StartingHistoryEvent();

	public void happen(HistoryEvent<?> event) {
		currentState.addFollowing(event);
		event.setParent(currentState);
		redo();
	}

	public void undo() {
		currentState.undo();
		currentState = currentState.getParent();
		log.log(DEBUG, "Change state to %s", currentState);
		historyId.setText(Integer.toHexString(currentState.hashCode()));
	}

	public void redo() {
		currentState = currentState.primaryFollowing();
		if (currentState == null)
			return;
		currentState.redo();
		log.log(DEBUG, "Change state to %s", currentState);
		historyId.setText(Integer.toHexString(currentState.hashCode()));
	}
}
