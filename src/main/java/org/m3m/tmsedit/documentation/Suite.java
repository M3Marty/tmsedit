package org.m3m.tmsedit.documentation;

import lombok.*;

import java.util.*;

@ToString
public class Suite {

	@Getter
	private final Integer id;

	@Getter @Setter
	private String title, description, preCondition;

	private List<Suite> suites = new LinkedList<>();
	private List<TestCase> cases = new LinkedList<>();

	public Suite(Integer id) {
		this.id = id;
	}

	public List<Suite> getSuites() {
		return Collections.unmodifiableList(suites);
	}

	public List<TestCase> getCases() {
		return Collections.unmodifiableList(cases);
	}

	public void addSuite(Suite suite) {
		this.suites.add(suite);
	}

	public void addSuite(Suite suite, int index) {
		this.suites.add(index, suite);
	}

	public void addCase(TestCase testCase) {
		this.cases.add(testCase);
	}

	public void addCase(TestCase testCase, int index) {
		this.cases.add(index, testCase);
	}
}