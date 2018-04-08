package com.shell.graphdb.utils;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;

import org.apache.poi.ss.usermodel.Cell;
import org.apache.poi.ss.usermodel.CellStyle;
import org.apache.poi.ss.usermodel.DataFormat;
import org.apache.poi.ss.usermodel.Row;
import org.apache.poi.ss.usermodel.Sheet;
import org.apache.poi.ss.usermodel.Workbook;
import org.apache.poi.xssf.usermodel.XSSFSheet;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;

import com.shell.graphdb.model.Edge;
import com.shell.graphdb.model.RelationshipInfo;
import com.shell.graphdb.model.Vertex;
import com.shell.graphdb.model.VertexListData;

public class ExcelUtils {

	private Map<String, Integer> headerIndexmap = new HashMap<>();

	public ExcelUtils() {
		File output = new File("out");
		if(!output.exists()){
			output.mkdirs();
		}
	}

	public void createVertexFile(List<VertexListData> vertexListData) {

		XSSFWorkbook workbook = new XSSFWorkbook();
		XSSFSheet sheet = workbook.createSheet("VERTEX");

		DataFormat fmt = workbook.createDataFormat();
		CellStyle textStyle = workbook.createCellStyle();
		textStyle.setDataFormat(fmt.getFormat("@"));
		sheet.setDefaultColumnStyle(9, textStyle);
		
		
		int rowNum = 0;
		int colNum = 0;
		Row row = sheet.createRow(rowNum++);

		for (VertexListData eachList : vertexListData) {

			List<String> headers = eachList.getHeaders();
			for (String each : headers) {
				if (!headerIndexmap.containsKey(each)) {
					headerIndexmap.put(each, colNum);
					Cell headerCell = row.createCell(colNum++);
					headerCell.setCellType(Cell.CELL_TYPE_STRING);
					headerCell.setCellValue(each);
					
				}

			}

		}

		for (VertexListData eachList : vertexListData) {

			List<Vertex> vertexList = eachList.getVertexList();
			for (Vertex vertex : vertexList) {
				row = sheet.createRow(rowNum++);
				Cell idCell = row.createCell(headerIndexmap.get("~id"));
				idCell.setCellType(Cell.CELL_TYPE_STRING);
				idCell.setCellValue(vertex.getId());
				
				Cell labelCell = row.createCell(headerIndexmap.get("~label"));
				labelCell.setCellType(Cell.CELL_TYPE_STRING);
				labelCell.setCellValue(vertex.getLabel());
				
				List<String> headers = eachList.getHeaders();
				headers.removeAll(Arrays.asList("~id", "~label"));
				for (String header : headers) {
					String value = (String) vertex.getProperties().get(header);
					Cell propCell = row.createCell(headerIndexmap.get(header));
					propCell.setCellType(Cell.CELL_TYPE_STRING);
					propCell.setCellValue(value);
				}
			}

		}
		FileOutputStream outputStream = null;
		try {
			outputStream = new FileOutputStream("out/VERTEX.xlsx");
			workbook.write(outputStream);
			outputStream.close();
		} catch (FileNotFoundException e) {
			e.printStackTrace();
		} catch (IOException e) {
			e.printStackTrace();
		} finally {
			closeOutputStream(outputStream);
		}
	}

	public VertexListData readFile(String filePath) {
		VertexListData data = new VertexListData();
		FileInputStream excelFile = null;
		
		try {
			File file= new File(filePath);	
			String fileName = file.getName().split("\\.")[0];
			excelFile = new FileInputStream(file);
			Workbook workbook = new XSSFWorkbook(excelFile);
			Sheet datatypeSheet = workbook.getSheetAt(0);
			Iterator<Row> iterator = datatypeSheet.iterator();
			int rowIndex = 0;
			
			Map<Integer,String> indexHeadersMap = new HashMap<>();
			data.setRdmsTableName(fileName);
			List<Vertex> vertexList = new ArrayList<>();
			data.setVertexList(vertexList);
			
			while (iterator.hasNext()) {

				Row currentRow = iterator.next();
				Iterator<Cell> cellIterator = currentRow.iterator();

				Vertex vertex = new Vertex();
				vertex.setId(fileName+"-V"+(rowIndex));
				vertex.setLabel(fileName);
				int colIndex = 0;
				while (cellIterator.hasNext()) {

					Cell currentCell = cellIterator.next();
					String cellValue = currentCell.getCellType()==1?currentCell.getStringCellValue():Double.toString(currentCell.getNumericCellValue());
					if (rowIndex == 0) {
						indexHeadersMap.put(colIndex++, cellValue);
					} else {
						vertex.addProperties(indexHeadersMap.get(colIndex++), cellValue);
					}

				}
				
				if(rowIndex++>0){
					data.addToVertexList(vertex);
				}				
			}
		} catch (FileNotFoundException e) {
			e.printStackTrace();
		} catch (IOException e) {
			e.printStackTrace();
		}finally {
			closeInputStream(excelFile);
		}
		return data;
	}

	public static void closeOutputStream(OutputStream os) {
		if (os != null) {
			try {
				os.close();
			} catch (IOException e) {
			}
		}
	}

	public static void closeInputStream(InputStream is) {
		if (is != null) {
			try {
				is.close();
			} catch (IOException e) {
			}
		}
	}

	public void createEdgeFile(List<VertexListData> vertexListData) {
	
		List<List<Edge>> allEdges = new ArrayList<>();
		
		for(VertexListData child:vertexListData){
			if(child.hasFK()){
				VertexListData parent = getParentTable(child.getRelationShipInfo(),vertexListData);
				List<Edge> edgeList = createRelationShip(child,parent);
				allEdges.add(edgeList);
			}
		}
		
		XSSFWorkbook workbook = new XSSFWorkbook();
		XSSFSheet sheet = workbook.createSheet("EDGE");

		int rowNum = 0;
		int colNum = 0; 
		DataFormat fmt = workbook.createDataFormat();
		CellStyle textStyle = workbook.createCellStyle();
		textStyle.setDataFormat(fmt.getFormat("@"));
		sheet.setDefaultColumnStyle(9, textStyle);
		Row row = sheet.createRow(rowNum++);

		for(String header:Arrays.asList("~id","~from","~to","~label")){
			Cell headerCell = row.createCell(colNum++);
			headerCell.setCellType(Cell.CELL_TYPE_STRING);
			headerCell.setCellValue(header);
		}
		
		for(List<Edge> edgeList:allEdges){
			for(Edge edge:edgeList){				
				row = sheet.createRow(rowNum++);
				Cell idCell = row.createCell(0);
				idCell.setCellType(Cell.CELL_TYPE_STRING);
				idCell.setCellValue(edge.getId());
				
				Cell fromCell = row.createCell(1);
				fromCell.setCellType(Cell.CELL_TYPE_STRING);
				fromCell.setCellValue(edge.getFrom());
				
				Cell toCell = row.createCell(2);
				toCell.setCellType(Cell.CELL_TYPE_STRING);
				toCell.setCellValue(edge.getTo());
				
				Cell labelCell = row.createCell(3);
				labelCell.setCellType(Cell.CELL_TYPE_STRING);
				labelCell.setCellValue(edge.getLabel());
			}
		}
		
		FileOutputStream outputStream = null;
		try {
			outputStream = new FileOutputStream("out/EDGE.xlsx");
			workbook.write(outputStream);
			outputStream.close();
		} catch (FileNotFoundException e) {
			e.printStackTrace();
		} catch (IOException e) {
			e.printStackTrace();
		} finally {
			closeOutputStream(outputStream);
		}
	}

	private List<Edge> createRelationShip(VertexListData child, VertexListData parent) {
		List<Edge> edgeList = new ArrayList<>();
		RelationshipInfo relationshipInfo = child.getRelationShipInfo();
		int index=1;
		for(Vertex pvertex : parent.getVertexList()){
			String parentColumn = pvertex.getProperties().getProperty(relationshipInfo.getToColumnName());			
			for(Vertex cvertex : child.getVertexList()){				
				String childColumn = cvertex.getProperties().getProperty(relationshipInfo.getFromColumnName());				
				if(parentColumn.equals(childColumn)){
					Edge edge = new Edge();
					edge.setId("E"+index++);
					edge.setFrom(pvertex.getId());
					edge.setTo(cvertex.getId());
					edge.setLabel(relationshipInfo.getRelationshipName());
					edgeList.add(edge);
				}
			}
		}
		return edgeList;
	}

	private VertexListData getParentTable(RelationshipInfo relationShipInfo, List<VertexListData> vertexListData) {
		for(VertexListData each:vertexListData){
			if(each.getRdmsTableName().equals(relationShipInfo.getToTableName())){
				return each;
			}
		}
		return null;
	}
		
}
