package com.shell.graphdb.model;

import java.util.Properties;

public class Vertex {

	private String id;
	private Properties props;
	private String label;

	public Properties getProperties() {
		return props;
	}

	public void addProperties(String key, String value) {
		if (props == null) {
			props = new Properties();
		}
		props.put(key, value);
	}

	public String getLabel() {
		return label;
	}

	public void setLabel(String label) {
		this.label = label;
	}

	public String getId() {
		return id;
	}

	public void setId(String id) {
		this.id = id;
	}

}
