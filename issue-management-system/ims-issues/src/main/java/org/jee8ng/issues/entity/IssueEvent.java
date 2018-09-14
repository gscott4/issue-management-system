package org.jee8ng.issues.entity;


public class IssueEvent {
	
	private Issue instance;
	
	public IssueEvent(Issue instance) {
		this.instance = instance;
	}

	public Issue getInstance() {
		return instance;
	}

	public void setInstance(Issue instance) {
		this.instance = instance;
	}
}
