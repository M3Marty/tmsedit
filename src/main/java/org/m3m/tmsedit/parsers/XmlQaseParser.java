package org.m3m.tmsedit.parsers;

import org.m3m.tmsedit.documentation.*;
import org.m3m.tmsedit.parser.*;
import org.w3c.dom.*;
import org.xml.sax.SAXException;

import javax.xml.parsers.DocumentBuilderFactory;
import javax.xml.parsers.ParserConfigurationException;
import java.io.*;
import java.util.NoSuchElementException;
import java.util.Optional;
import java.util.function.Function;

import static java.lang.System.Logger.Level.*;
import static org.m3m.tmsedit.logging.TextAreaLogger.log;

public final class XmlQaseParser implements FileParser, ParserDataProvider {

	@Override
	public Suite read(InputStream from) throws IOException {
		try {
			var builder = DocumentBuilderFactory.newInstance().newDocumentBuilder();
			Document doc = builder.parse(from);
			var root = doc.getDocumentElement();
			root.normalize();
			Node suiteNode = root.getElementsByTagName("node").item(0);
			return new SuiteBuilder(this).parseSuite(suiteNode);
		} catch (ParserConfigurationException | SAXException e) {
			throw new RuntimeException(e);
		}
	}

	@Override
	public void write(OutputStream to) throws IOException {

	}

	private Optional<Node> findNode(Node parent, String type) {
		NodeList children = parent.getChildNodes();
		for (int i = 0; i < children.getLength(); i++)
			if (children.item(i).getNodeName().equals(type))
				return Optional.of(children.item(i));

		return Optional.empty();
	}

	private Optional<String> findNodeTextContent(Node parent, String type) {
		return findNode(parent, type).map(Node::getTextContent).map(String::trim);
	}

	@Override
	public Optional<Integer> getId(Node node) {
		try {
			return findNodeTextContent(node, "id").map(Integer::parseInt);
		} catch (NumberFormatException e) {
			return Optional.empty();
		}
	}

	@Override
	public String getSuiteTitle(Node node) {
		return findNodeTextContent(node, "title").orElse("");
	}

	@Override
	public String getSuiteDescription(Node node) {
		return findNodeTextContent(node, "description").orElse("");
	}

	@Override
	public String getSuitePreCondition(Node node) {
		return findNodeTextContent(node, "preconditions").orElse("");
	}

	@Override
	public Iterable<Node> getSuites(Node node) {
		return new NodeListIterator(findNode(node, "suites").map(Node::getChildNodes));
	}

	@Override
	public Iterable<Node> getTestCases(Node node) {
		return new NodeListIterator(findNode(node, "cases").map(Node::getChildNodes));
	}

	@Override
	public String getCasePostCondition(Node node) {
		return findNodeTextContent(node, "postconditions").orElse("");
	}

	@Override
	public boolean isFlaky(Node node) {
		return findNodeTextContent(node, "is_flaky")
				.map(s -> s.equals("yes")).orElse(false);
	}

	@Override
	public boolean isAutomated(Node node) {
		return findNodeTextContent(node, "automation")
				.map(s -> s.equals("automated")).orElse(false);
	}

	@Override
	public boolean isMuted(Node node) {
		return findNodeTextContent(node, "muted")
				.map(s -> s.equals("yes")).orElse(false);
	}

	private <T> T findNodeEnumContent(Node node, String type,
			Function<String, T> mapping, T orElse) {
		Optional<String> content = findNodeTextContent(node, type);

		try {
			return content
					.map(String::toUpperCase)
					.map(mapping)
					.orElseThrow();
		} catch (IllegalArgumentException e) {
			log.log(WARNING, String.format("No such value %s as %s",
					content.orElse(null), type), e);
		} catch (NoSuchElementException e) {
			log.log(WARNING, "No tag <%s> in node %s/%s consider changing driver",
					type, node.getParentNode().getNodeName(), node.getNodeName());
		}

		return orElse;
	}

	@Override
	public TestCase.Priority getPriority(Node node) {
		return findNodeEnumContent(node, "priority",
				TestCase.Priority::valueOf, TestCase.Priority.NOT_SET);
	}

	@Override
	public TestCase.Severity getSeverity(Node node) {
		return findNodeEnumContent(node, "severity",
				TestCase.Severity::valueOf, TestCase.Severity.NOT_SET);
	}

	@Override
	public TestCase.Behavior getBehavior(Node node) {
		return findNodeEnumContent(node, "behavior",
				TestCase.Behavior::valueOf, TestCase.Behavior.UNDEFINED);
	}

	@Override
	public TestCase.Type getType(Node node) {
		return findNodeEnumContent(node, "type",
				TestCase.Type::valueOf, TestCase.Type.OTHER);
	}

	@Override
	public TestCase.Status getStatus(Node node) {
		return findNodeEnumContent(node, "status",
				TestCase.Status::valueOf, TestCase.Status.ACTUAL);
	}

	@Override
	public TestCase.Layer getLayer(Node node) {
		return findNodeEnumContent(node, "layer",
				TestCase.Layer::valueOf, TestCase.Layer.UNKNOWN);
	}

	@Override
	public Iterable<Node> getSteps(Node node) {
		return new NodeListIterator(findNode(node, "steps").map(Node::getChildNodes));
	}

	@Override
	public String getStepAction(Node node) {
		return findNodeTextContent(node, "action").orElse("");
	}

	@Override
	public String getStepData(Node node) {
		return findNodeTextContent(node, "data").orElse("");
	}

	@Override
	public String getStepResult(Node node) {
		return findNodeTextContent(node, "result").orElse("");
	}
}