package org.m3m.tmsedit.parser;

import org.m3m.tmsedit.documentation.TestCase;
import org.w3c.dom.Node;

import java.util.Optional;

public interface ParserDataProvider {

	Optional<Integer> getId(Node node);

	String getSuiteTitle(Node node);
	String getSuiteDescription(Node node);
	String getSuitePreCondition(Node node);

	Iterable<Node> getSuites(Node node);
	Iterable<Node> getTestCases(Node node);

	default String getCaseTitle(Node node) {
		return getSuiteTitle(node);
	}

	default String getCaseDescription(Node node) {
		return getSuiteDescription(node);
	}

	default String getCasePreCondition(Node node) {
		return getSuitePreCondition(node);
	}

	String getCasePostCondition(Node node);

	boolean isFlaky(Node node);
	boolean isAutomated(Node node);
	boolean isMuted(Node node);

	TestCase.Priority getPriority(Node node);
	TestCase.Severity getSeverity(Node node);
	TestCase.Behavior getBehavior(Node node);
	TestCase.Type getType(Node node);
	TestCase.Status getStatus(Node node);
	TestCase.Layer getLayer(Node node);

	Iterable<Node> getSteps(Node node);

	String getStepAction(Node node);
	String getStepData(Node node);
	String getStepResult(Node node);
}