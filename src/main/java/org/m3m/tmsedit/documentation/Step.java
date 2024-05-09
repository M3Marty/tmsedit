package org.m3m.tmsedit.documentation;

import lombok.Data;

import java.util.LinkedList;
import java.util.List;

@Data
public class Step {

	private String action;
	private String data;
	private String expected;

	private List<Step> steps = new LinkedList<>();

	public void addStep(Step step) {
		steps.add(step);
	}
}
