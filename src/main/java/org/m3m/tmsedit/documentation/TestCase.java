package org.m3m.tmsedit.documentation;

import lombok.*;

import java.util.*;

@Getter @Setter
public class TestCase extends Suite {

	private String postCondition;
	private boolean isFlaky, isAutomated, isMuted;
	private Priority priority;
	private Severity severity;
	private Behavior behavior;
	private Type type;
	private Status status;
	private Layer layer;

	private List<Step> steps = new LinkedList<>();

	public void addStep(Step step) {
		steps.add(step);
	}

	public TestCase(Integer id) {
		super(id);
	}

	public String toStringVerbose() {
		return "TestCase{" +
				"postCondition='" + postCondition + '\'' +
				", isFlaky=" + isFlaky +
				", isAutomated=" + isAutomated +
				", isMuted=" + isMuted +
				", priority=" + priority +
				", severity=" + severity +
				", behavior=" + behavior +
				", type=" + type +
				", status=" + status +
				", layer=" + layer +
				", steps=" + steps +
				'}';
	}

	@AllArgsConstructor
	public enum Priority {
		HIGH("High"), MEDIUM("Medium"),
		LOW("Low"), NOT_SET("Not set");

		private final String stringValue;

		@Override
		public String toString() {
			return stringValue;
		}
	}

	@AllArgsConstructor
	public enum Severity {
		BLOCKER("Blocker"), CRITICAL("Critical"),
		MAJOR("Major"), NORMAL("Normal"),
		MINOR("Minor"), TRIVIAL("Trivial"),
		NOT_SET("Not set");

		private final String stringValue;

		@Override
		public String toString() {
			return stringValue;
		}
	}

	@AllArgsConstructor
	public enum Behavior {
		POSITIVE("Positive"), NEGATIVE("Negative"),
		DESTRUCTIVE("Destructive"), UNDEFINED("Undefined");

		private final String stringValue;

		@Override
		public String toString() {
			return stringValue;
		}
	}

	@AllArgsConstructor
	public enum Type {
		FUNCTIONAL("Functional"),
		SMOKE("Smoke"),
		REGRESSION("Regression"),
		SECURITY("Security"),
		USABILITY("Usability"),
		PERFORMANCE("Performance"),
		ACCEPTANCE("Acceptance"),
		COMPATIBILITY("Compatibility"),
		INTEGRATION("Integration"),
		EXPLORATORY("Exploratory"),
		OTHER("Other");

		private final String stringValue;

		@Override
		public String toString() {
			return stringValue;
		}
	}

	@AllArgsConstructor
	public enum Status {
		ACTUAL("Actual"), DRAFT("Draft"),
		DEPRECATED("Deprecated");

		private final String stringValue;

		@Override
		public String toString() {
			return stringValue;
		}
	}

	@AllArgsConstructor
	public enum Layer {
		UNIT("Unit"), API("Api"),
		E2E("E2E"), UNKNOWN("Unknown");

		private final String stringValue;

		@Override
		public String toString() {
			return stringValue;
		}
	}
}