package com.shell.graphdb.model;

import java.util.ArrayList;
import java.util.Enumeration;
import java.util.List;

public class VertexListData {

	private String rdmsTableName;
	private List<Vertex> vertexList;
	private RelationshipInfo relationShipInfo;

	public String getRdmsTableName() {
		return rdmsTableName;
	}

	public void setRdmsTableName(String rdmsTableName) {
		this.rdmsTableName = rdmsTableName;
	}

	public boolean hasFK() {
		return relationShipInfo!=null;
	}

	public List<Vertex> getVertexList() {
		return vertexList;
	}

	public void setVertexList(List<Vertex> vertexList) {
		this.vertexList = vertexList;
	}

	public void addToVertexList(Vertex vertex) {
		if (vertexList == null) {
			vertexList = new ArrayList<>();
		}
		vertexList.add(vertex);
	}

	public RelationshipInfo getRelationShipInfo() {
		return relationShipInfo;
	}

	public void setRelationShipInfo(RelationshipInfo relationShipInfo) {
		this.relationShipInfo = relationShipInfo;
	}

	public List<String> getHeaders(){
		List<String> headers = new ArrayList<>();
		headers.add("~id");
		headers.add("~label");
		if(vertexList != null && !vertexList.isEmpty()){
			Vertex first = vertexList.get(0);
			Enumeration<String> enums = (Enumeration<String>) first.getProperties().propertyNames();
		    while (enums.hasMoreElements()) {
		      String key = enums.nextElement();	
		      headers.add(key);
		    }
		}
		return headers;
	}
}
