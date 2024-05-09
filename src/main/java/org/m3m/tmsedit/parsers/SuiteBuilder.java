package org.m3m.tmsedit.parsers;

import lombok.AllArgsConstructor;
import org.m3m.tmsedit.documentation.*;
import org.w3c.dom.Node;

import java.util.stream.StreamSupport;

@AllArgsConstructor
public class SuiteBuilder {

	private ParserDataProvider provider;

	private boolean isElementNode(Node node) {
		return node.getNodeType() == Node.ELEMENT_NODE;
	}

	Step parseStep(Node node) {
		Step step = new Step();

		step.setAction(provider.getStepAction(node));
		step.setAction(provider.getStepData(node));
		step.setAction(provider.getStepResult(node));

		StreamSupport.stream(provider.getSteps(node).spliterator(), false)
				.filter(this::isElementNode).map(this::parseStep).forEach(step::addStep);

		return step;
	}

	TestCase parseTestCase(Node node) {
		TestCase testCase = new TestCase(provider.getId(node).orElse(null));

		testCase.setTitle(provider.getCaseTitle(node));
		testCase.setDescription(provider.getCaseDescription(node));
		testCase.setPreCondition(provider.getCasePreCondition(node));
		testCase.setPostCondition(provider.getCasePostCondition(node));

		testCase.setFlaky(provider.isFlaky(node));
		testCase.setAutomated(provider.isAutomated(node));
		testCase.setMuted(provider.isMuted(node));

		testCase.setPriority(provider.getPriority(node));
		testCase.setSeverity(provider.getSeverity(node));
		testCase.setBehavior(provider.getBehavior(node));
		testCase.setType(provider.getType(node));
		testCase.setStatus(provider.getStatus(node));
		testCase.setLayer(provider.getLayer(node));

		StreamSupport.stream(provider.getSuites(node).spliterator(), false)
				.filter(this::isElementNode).map(this::parseSuite).forEach(testCase::addSuite);

		StreamSupport.stream(provider.getTestCases(node).spliterator(), false)
				.filter(this::isElementNode).map(this::parseTestCase).forEach(testCase::addCase);

		StreamSupport.stream(provider.getSteps(node).spliterator(), false)
				.filter(this::isElementNode).map(this::parseStep).forEach(testCase::addStep);

		return testCase;
	}

	Suite parseSuite(Node node) {
		Suite suite = new Suite(provider.getId(node).orElse(null));
		suite.setTitle(provider.getSuiteTitle(node));
		suite.setDescription(provider.getSuiteDescription(node));
		suite.setPreCondition(provider.getSuitePreCondition(node));

		StreamSupport.stream(provider.getSuites(node).spliterator(), false)
				.filter(this::isElementNode).map(this::parseSuite).forEach(suite::addSuite);

		StreamSupport.stream(provider.getTestCases(node).spliterator(), false)
				.filter(this::isElementNode).map(this::parseTestCase).forEach(suite::addCase);

		return suite;
	}
}