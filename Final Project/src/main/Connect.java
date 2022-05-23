package main;
import java.sql.*;
public class Connect {
	Connection con;
	ResultSet res;
	Statement st;
	boolean check = true;
	public Connect() {
		
		while (check) {
			try {
				Class.forName("com.mysql.jdbc.Driver");
				con = DriverManager.getConnection("jdbc:mysql://localhost:3306/pt_pudding", "root", "");
				System.out.println("Connect success");
				st=con.createStatement();
				check = false;
				st.executeUpdate("USE pt_pudding");
				break;
			} catch (Exception e) {}
			
			try {
				Class.forName("com.mysql.jdbc.Driver");
				con = DriverManager.getConnection("jdbc:mysql://localhost:3306/", "root", "");
				System.out.println("Connect to base");
				st=con.createStatement();
				st.executeUpdate("CREATE DATABASE pt_pudding");
				System.out.println("pt_pudding database created.");
				st.executeUpdate("USE pt_pudding");
			} catch (Exception e) {}
		}
		initTable();
	}
	
	public void initTable() {
		try {
			st.executeUpdate("CREATE TABLE Menu (KodeMenu CHAR(6) PRIMARY KEY NOT NULL UNIQUE, NamaMenu VARCHAR(50) NOT NULL, HargaMenu INTEGER, StokMenu INTEGER)");
			System.out.println("Created table Menu");
		} catch (Exception e) {}
	}
	
	public void insert (String kodeMenu, String namaMenu, int hargaMenu, int stokMenu) {
		try {
			String query = "INSERT INTO Menu VALUES ('" + kodeMenu + "','" + namaMenu + "'," + hargaMenu + "," + stokMenu + ")";
			System.out.println(query);
			st.executeUpdate(query);
			System.out.println("Success: " + query);
		} catch (Exception e) {e.printStackTrace();}
	}
	
	public ResultSet select() {
		try {
			res = st.executeQuery("SELECT * FROM Menu");
			System.out.println("select success");
		} catch (Exception e) {e.printStackTrace(); System.out.println("select error");}
		return res;
	}
	
	public void update (String kodeMenu, int hargaMenu, int stokMenu) {
		try {
			String query = "UPDATE Menu SET HargaMenu = " + hargaMenu + ", StokMenu = " + stokMenu + " WHERE KodeMenu = '" + kodeMenu + "'";
			System.out.println(query);
			st.executeUpdate(query);
			System.out.println("Success: " + query);
		} catch (Exception e) {e.printStackTrace();}
	}
	
	public void delete (String kodeMenu) {
		try {
			String query = "DELETE FROM Menu WHERE KodeMenu = '" + kodeMenu + "'";
			System.out.println(query);
			st.executeUpdate(query);
			System.out.println("Success: " + query);
		} catch (Exception e) {e.printStackTrace();}
	}
	
	public Connection getCon() {return con;}
	public void close() {try {con.close();} catch (Exception e) {}}

}
