package com.shell.graphdb.model;

public class RelationshipInfo {

	private String fromTableName;
	private String toTableName;
	private String fromColumnName;
	private String toColumnName;
	private String relationshipName;

	public String getFromTableName() {
		return fromTableName;
	}

	public void setFromTableName(String tableName) {
		this.fromTableName = tableName;
	}

	public String getToTableName() {
		return toTableName;
	}

	public void setToTableName(String toTableName) {
		this.toTableName = toTableName;
	}

	public String getToColumnName() {
		return toColumnName;
	}

	public void setToColumnName(String toColumnName) {
		this.toColumnName = toColumnName;
	}

	public String getFromColumnName() {
		return fromColumnName;
	}

	public void setFromColumnName(String fromColumnName) {
		this.fromColumnName = fromColumnName;
	}

	public String getRelationshipName() {
		return relationshipName;
	}

	public void setRelationshipName(String relationshipName) {
		this.relationshipName = relationshipName;
	}

}
