package lib;

import java.io.*;
import java.net.Socket;
import java.sql.*;
import java.util.*;

import com.jcraft.jsch.*;
import com.mysql.jdbc.ConnectionImpl;

public class Execute {

	public static void main(String[] args) throws SQLException {
		if (args.length != 7 || args.length != 8){
			System.out.println("Usage DBConnectTest <BroncoUser> <BroncoPassword> <DBname> <query/update> <TaskNumber> <TaskQueryFile> <outputFile> <parametersforQuery>");
		}
		else{
			Connection con = null;
			Session session = null;
			try
			{
				String strSshUser = args[0];                  	   // SSH loging username
				String strSshPassword = args[1];                   // SSH login password
				String strDBname = args[2];                  	   // Database name
				String strQueryUpdate = args[3];                   // Either 'Query' or 'Update'
				String strTaskNumber = args[4];               	   // The task number that will be queried or updated
				String strTaskQueryFile = args[5];                 // The txt file name that contains the command
				String strOutputFile = args[6];                    // The outputted txt file
				
				if(args.length == 8){
					String strParametersForQuery = args[7];        // Parameters if 'Query' is selected
				}
				
				String strSshHost = "onyx.boisestate.edu";         // hostname or ip or SSH server
				int nSshPort = 22;                                 // remote SSH host port number
				String strRemoteHost = "localhost";  			   // hostname or ip of your database server

				int nLocalPort = 3367;  						   // local port number use to bind SSH tunnel
				
				int nRemotePort = Integer.parseInt("10167");       // remote port number of your database 
				
				/*
				 * STEP 0
				 * CREATE a SSH session to ONYX
				 * 
				 * */
				session = Execute.doSshTunnel(strSshUser, strSshPassword, strSshHost, nSshPort, strRemoteHost, nLocalPort, nRemotePort);
				
				
				/*
				 * STEP 1 and 2
				 * LOAD the Database DRIVER and obtain a CONNECTION
				 * 
				 * */
				Class.forName("com.mysql.jdbc.Driver");
				con = DriverManager.getConnection("jdbc:mysql://localhost:"+nLocalPort, strDbUser, strDbPassword);

				/*
				 * STEP 3
				 * EXECUTE STATEMENTS
				 * 
				 * */

				Statement stmt = null;
				stmt = con.createStatement();
				
				ResultSet resultSet = stmt.executeQuery("select * from `COMPANY`.`EMPLOYEE`");
				
				/*
				 * STEP 4
				 * Use result sets (tables) to navigate through the results
				 * 
				 * */
				
				ResultSetMetaData rsmd = resultSet.getMetaData();

				int columnsNumber = rsmd.getColumnCount();
				while (resultSet.next()) {
					for (int i = 1; i <= columnsNumber; i++) {
						if (i > 1) System.out.print(",  ");
						String columnValue = resultSet.getString(i);
						System.out.print(columnValue + " " + rsmd.getColumnName(i));
					}
					System.out.println(" ");
				}
				
				/*TO INSERT INTO TABLES
				 * You can also read from a file and store in a data structure of your choice*/
				String[] data = {"boise", "nampa"};
				insertLocations(con,data);
				
			}
			catch( Exception e )
			{
				e.printStackTrace();
			}
			finally{
				
				/*
				 * STEP 5
				 * CLOSE CONNECTION AND SSH SESSION
				 * 
				 * */
				
				con.close();
				session.disconnect();
			}

		}
	}
	
	private static Session doSshTunnel( String strSshUser, String strSshPassword, String strSshHost, int nSshPort, String strRemoteHost, int nLocalPort, int nRemotePort ) throws JSchException
	{
		/*This is one of the available choices to connect to mysql
		 * If you think you know another way, you can go ahead*/
		
		final JSch jsch = new JSch();
		java.util.Properties configuration = new java.util.Properties();
		configuration.put("StrictHostKeyChecking", "no");

		Session session = jsch.getSession( strSshUser, strSshHost, 22 );
		session.setPassword( strSshPassword );

		session.setConfig(configuration);
		session.connect();
		session.setPortForwardingL(nLocalPort, strRemoteHost, nRemotePort);
		return session;
	}
	
	
		private static void insertLocations(Connection con, String[] data) throws SQLException {
			  String sql;
			  java.sql.Statement stmt = null;
			  stmt = con.createStatement();
			  for(int i=0;i<data.length;i++){
				  sql = "INSERT INTO `COMPANY`.`DEPT_LOCATIONS`(`DNUMBER`,`DLOCATION`)VALUES(1,'"+data[i]+"')";
				  int res = stmt.executeUpdate(sql);
				  System.out.println(res);
			  }
			  


	}

}





