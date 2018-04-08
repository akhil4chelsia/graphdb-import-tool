package com.shell.graphdb.etl;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;

import com.shell.graphdb.rdbms.MetaDataReader;
import com.shell.graphdb.rdbms.TableInfo;

import schemacrawler.schemacrawler.SchemaCrawlerException;

public class GraphDbImportTool {

	private final TableInfo[] tables;
	
	public GraphDbImportTool(String jdbcUrl, String schema) throws SQLException, SchemaCrawlerException {
		Connection conn = DriverManager.getConnection(jdbcUrl);
		tables = MetaDataReader.extractTables(conn, schema);
	}

	public static void main(String[] args) throws Exception {
		new GraphDbImportTool(args[0],args[1]).run();
	}

	private void run() {

	}
}
