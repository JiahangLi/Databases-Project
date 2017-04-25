import java.io.*;
import java.sql.*;
import java.util.*;

/**
 * CS410 database project: Twitter System
 * Group 6: Jiahang Li, Kenny Overly, Michael Plaisance
 * Driver Class: create database, schema create and data insertion
 * @author JiahangLi
 *
 */
public class CreateDatabase {

	
	public static void main(String[] args) throws SQLException {

		
		// TODO Auto-generated method stub
		if(args.length <3){
			printUsage();
		}else{
			
			String user;
			String password;
			String databaseName;
			String sandboxUser, sandboxPassword;
			int sandboxPort;
			
			
			String sshUser = args[0];
			String sshPassword = args[1];
			String sshHost = "onyx.boisestate.edu";
			int sshPort = 22; //default host value
			
			String remoteHost = "127.0.0.1";
			int Portnumber = 10167;
			databaseName = args[2];
			
			//TODO might need to be replace by a more stable sandbox info
			sandboxUser = "jiahangli";
			sandboxPassword = "110702";
			sandboxPort = 10150;
			
			/*
			 * STEP 0
			 * CREATE a SSH session to ONYX
			 * 
			 * */
			session = CreateDatabase.doSshTunnel(sshUser, sshPassword, sshHost, sshPort, remoteHost, Portnumber, nRemotePort);
			
			
		}
		
	}

	public static void printUsage(){
		System.out.println("java CreateDatabase <Broncouser> <broncopassword> <DBname>");
	}
}

