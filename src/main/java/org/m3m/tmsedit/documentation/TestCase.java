package org.m3m.tmsedit.documentation;

import lombok.*;

import java.util.*;

@Getter @Setter
@ToString(callSuper = true)
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

	public enum Priority {
		HIGH, MEDIUM, LOW, NOT_SET
	}

	public enum Severity {
		BLOCKER, CRITICAL, MAJOR, NORMAL, MINOR, TRIVIAL, NOT_SET
	}

	public enum Behavior {
		POSITIVE, NEGATIVE, DESTRUCTIVE, UNDEFINED
	}

	public enum Type {
		FUNCTIONAL,
		SMOKE,
		REGRESSION,
		SECURITY,
		USABILITY,
		PERFORMANCE,
		ACCEPTANCE,
		COMPATIBILITY,
		INTEGRATION,
		EXPLORATORY,
		OTHER
	}

	public enum Status {
		ACTUAL, DRAFT, DEPRECATED
	}

	public enum Layer {
		UNIT, API, E2E, UNKNOWN
	}
}