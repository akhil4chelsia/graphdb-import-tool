package com.test.graphdb;

import java.util.Arrays;

import com.shell.graphdb.model.RelationshipInfo;
import com.shell.graphdb.model.VertexListData;
import com.shell.graphdb.utils.ExcelUtils;

public class Test {
	
	@org.junit.Test
	public void testReadAndWriteFile(){
		ExcelUtils utils = new ExcelUtils();
		VertexListData CDS_WELL_vertexList = utils.readFile("input/CDS_WELL.xlsx");
		VertexListData SMDS_WELLBORE_vertexList = utils.readFile("input/SMDS_WELLBORE.xlsx");
		
		RelationshipInfo relationShipInfo = new RelationshipInfo();
		relationShipInfo.setFromTableName("SMDS_WELLBORE");
		relationShipInfo.setToTableName("CDS_WELL");
		relationShipInfo.setFromColumnName("UNIQUE_WELL_IDENTIFIER");
		relationShipInfo.setToColumnName("UNIQUE_WELL_IDENTIFIER");
		relationShipInfo.setRelationshipName("have");
		
		SMDS_WELLBORE_vertexList.setRelationShipInfo(relationShipInfo);
		
		utils.createVertexFile(Arrays.asList(CDS_WELL_vertexList,SMDS_WELLBORE_vertexList));
		
		utils.createEdgeFile(Arrays.asList(CDS_WELL_vertexList,SMDS_WELLBORE_vertexList));
	}
	
/*
	@org.junit.Test
	@Ignore
	public void testCreateVertexCsv(){
		Vertex v1 = new Vertex(); 
		v1.setId("v3");
		v1.setLabel("well");
		v1.addProperties("unique_well_identifier", "110000903046");
		v1.addProperties("well_name", "GAMBA 45");
		v1.addProperties("common_name", "GA04501");
		v1.addProperties("well_number", "40");
		
		VertexListData vertexListData1 = new VertexListData();
		vertexListData1.setRdmsTableName("wells");
		vertexListData1.addToVertexList(v1);
		
		Vertex v2 = new Vertex(); // table - well alias
		v2.setId("v4");
		v2.setLabel("well_alias");
		v2.addProperties("unique_well_identifier", "110000903046");
		v2.addProperties("well_name", "GAMBA 45");
		v2.addProperties("alias", "990000004429");
		
		VertexListData vertexListData2 = new VertexListData();
		vertexListData2.setRdmsTableName("well_alias");
		RelationshipInfo rn = new RelationshipInfo();
		rn.setTableName("well_alias");
		rn.setToTableName("wells");
		rn.setFromColumnName("unique_well_identifier");
		rn.setToColumnName("unique_well_identifier");
		vertexListData2.setRelationShipInfo(rn);
		vertexListData2.addToVertexList(v2);
			
		new ExcelUtils().createVertexFile(Arrays.asList(vertexListData1,vertexListData2));
		
	}
	
	@org.junit.Test
	@Ignore
	public void testReadFile(){
		System.out.println(new File("input/CDS_WELL.xlsx").getName().split("\\.")[0]);		
		VertexListData vertexListData = new ExcelUtils().readFile("input/CDS_WELL.xlsx");		
		System.out.println(vertexListData);
	}*/
	
	
}
