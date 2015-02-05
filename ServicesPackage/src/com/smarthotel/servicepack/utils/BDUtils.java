package com.smarthotel.servicepack.utils;

import java.sql.ResultSet;
import java.sql.ResultSetMetaData;
import java.sql.SQLException;

import org.json.JSONArray;
import org.json.JSONObject;
import org.json.JSONException;

public class BDUtils
{
	public static JSONArray RStoJSON( ResultSet rs )  throws SQLException, JSONException
	{
	    JSONArray json = new JSONArray();
	    ResultSetMetaData rsmd = rs.getMetaData();

	    while(rs.next()) {
	      int numColumns = rsmd.getColumnCount();
	      JSONObject obj = new JSONObject();
	      
	      for (int i=1; i<numColumns+1; i++) {
	    	  
	        String column_name = rsmd.getColumnLabel(i);
	        Object value = null;
	        if(rsmd.getColumnType(i)==java.sql.Types.ARRAY){
	        	value = rs.getArray(column_name);
	        }
	        else if(rsmd.getColumnType(i)==java.sql.Types.BIGINT){
	        	value = rs.getInt(column_name);
	        }
	        else if(rsmd.getColumnType(i)==java.sql.Types.BOOLEAN){
	        	value = rs.getBoolean(column_name);
	        }
	        else if(rsmd.getColumnType(i)==java.sql.Types.BLOB){
	        	value = rs.getBlob(column_name);
	        }
	        else if(rsmd.getColumnType(i)==java.sql.Types.DOUBLE){
	        	value = rs.getDouble(column_name); 
	        }
	        else if(rsmd.getColumnType(i)==java.sql.Types.FLOAT){
	        	value = rs.getFloat(column_name);
	        }
	        else if(rsmd.getColumnType(i)==java.sql.Types.INTEGER){
	        	value = rs.getInt(column_name);
	        }
	        else if(rsmd.getColumnType(i)==java.sql.Types.NVARCHAR){
	        	value = rs.getNString(column_name);
	        }
	        else if(rsmd.getColumnType(i)==java.sql.Types.VARCHAR){
	        	value = rs.getString(column_name);
	        }
	        else if(rsmd.getColumnType(i)==java.sql.Types.TINYINT){
	        	value = rs.getInt(column_name);
	        }
	        else if(rsmd.getColumnType(i)==java.sql.Types.SMALLINT){
	        	value = rs.getInt(column_name);
	        }
	        else if(rsmd.getColumnType(i)==java.sql.Types.DATE){
	        	value = rs.getDate(column_name);
	        }
	        else if(rsmd.getColumnType(i)==java.sql.Types.TIMESTAMP){
	        	value = rs.getTimestamp(column_name);   
	        }
	        else{
	         	value = rs.getObject(column_name);
	        }
	        if(value==null){
	        	obj.put(column_name.toLowerCase(), JSONObject.NULL);
	        } else {
	        	obj.put(column_name.toLowerCase(), value);
	        }
	      }

	      json.put(obj);
	    }

	    return json;
	}
	
	public static JSONArray RStoJSONLABEL( ResultSet rs )  throws SQLException, JSONException
	{
	    JSONArray json = new JSONArray();
	    ResultSetMetaData rsmd = rs.getMetaData();

	    while(rs.next()) {
	      int numColumns = rsmd.getColumnCount();
	      JSONObject obj = new JSONObject();
	      Object value = null;
	      for (int i=1; i<numColumns+1; i++) {
	        String column_name = rsmd.getColumnLabel(i);

	        if(rsmd.getColumnType(i)==java.sql.Types.ARRAY){
	        	value = rs.getArray(column_name);
	        }
	        else if(rsmd.getColumnType(i)==java.sql.Types.BIGINT){
	        	value = rs.getInt(column_name);
	        }
	        else if(rsmd.getColumnType(i)==java.sql.Types.BOOLEAN){
	        	value = rs.getBoolean(column_name);
	        }
	        else if(rsmd.getColumnType(i)==java.sql.Types.BLOB){
	        	value = rs.getBlob(column_name);
	        }
	        else if(rsmd.getColumnType(i)==java.sql.Types.DOUBLE){
	        	value = rs.getDouble(column_name); 
	        }
	        else if(rsmd.getColumnType(i)==java.sql.Types.FLOAT){
	        	value = rs.getFloat(column_name);
	        }
	        else if(rsmd.getColumnType(i)==java.sql.Types.INTEGER){
	        	value = rs.getInt(column_name);
	        }
	        else if(rsmd.getColumnType(i)==java.sql.Types.NVARCHAR){
	        	value = rs.getNString(column_name);
	        }
	        else if(rsmd.getColumnType(i)==java.sql.Types.VARCHAR){
	        	value = rs.getString(column_name);
	        }
	        else if(rsmd.getColumnType(i)==java.sql.Types.TINYINT){
	        	value = rs.getInt(column_name);
	        }
	        else if(rsmd.getColumnType(i)==java.sql.Types.SMALLINT){
	        	value = rs.getInt(column_name);
	        }
	        else if(rsmd.getColumnType(i)==java.sql.Types.DATE){
	        	value = rs.getDate(column_name);
	        }
	        else if(rsmd.getColumnType(i)==java.sql.Types.TIMESTAMP){
	        	value = rs.getTimestamp(column_name);   
	        }
	        else{
	         	value = rs.getObject(column_name);
	        }
	        if(value==null){
	        	obj.put(column_name.toLowerCase(), JSONObject.NULL);
	        } else {
	        	obj.put(column_name.toLowerCase(), value);
	        }
	      }

	      json.put(obj);
	    }

	    return json;
	}
}
