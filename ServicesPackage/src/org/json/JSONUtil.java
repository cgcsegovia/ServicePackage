package org.json;

import org.apache.log4j.Logger;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;


/**
 * 
 * @author xefero
 *
 */
public class JSONUtil {

	public static final int JSPROCESS_RESULT_FAILED = 0;
	public static final int JSPROCESS_RESULT_SUCCESS = 1;
	
	public static final String SYSTEM_LINE_SEPARATOR = System.getProperty("line.separator");
	
	public static final String KEY_ERROR = "error";
	public static final String MESSAGE_ERROR_JSON_EXCEPTION = "error_json_exception";
	public static final String MESSAGE_ERROR_JSON_INVALID   = "error_json_invalid";
	
	public static final int INDENT_NUM_CHARS = 5;
	public static Logger log = Logger.getLogger( JSONUtil.class );
	
	
	public static String toStringIndent(JSONObject jsonObject){
		String strReturn = null;
		
		try {
			strReturn = jsonObject.toString(JSONUtil.INDENT_NUM_CHARS);
		} 
		catch (JSONException e) {
			System.out.println( "JSONUtil toStringIndent error: JSONException" );
			return null;			
		}
		
		return strReturn;
	}
	
	//.............................//
	// methods: override get basic //
	//.............................//
	
	public static Object get(JSONObject data, String jsonEntityKey) {
		Object result = null;
		try {
			result = data.get( jsonEntityKey );
		} catch (JSONException e) {
			result = null;
			log.error( "JSONUtil.get() Error: " + e.getMessage() );
		}
		return result;
	}
	
	public static String getString(JSONObject jsonObject,String key){
		
		String valueReturn = null;		
		try 					{valueReturn = jsonObject.getString(key);} 
		catch (JSONException e) {
			log.error( "Error while trying to get String key " + key + ". Error: " + e.getMessage() + "\njson: " + jsonObject );
			return null;}		
		if(valueReturn==null)	{return null;}
		
		return valueReturn;		
	}
	
	public static int getInt(JSONObject jsonObject,String key){
		
		int valueReturn = -1;		
		try 					{valueReturn = jsonObject.getInt(key);} 
		catch (JSONException e) {return -1;}
		
		return valueReturn;		
	}
	
	public static Boolean getBoolean(JSONObject jsonObject,String key){
		
		Boolean valueReturn = null;		
		try 					{ valueReturn = jsonObject.getBoolean(key);} 
		catch (JSONException e) {
			
			return null;}
		
		return valueReturn;		
	}

	public static int appendJSONObjectObject(JSONObject jsonObject,String key,JSONObject jsonAdd){
		
		int proccessResult = JSONUtil.JSPROCESS_RESULT_SUCCESS;		
		try 					{jsonObject.append(key,jsonAdd);} 
		catch (JSONException e) {proccessResult = JSONUtil.JSPROCESS_RESULT_FAILED;}
		
		return proccessResult;
	}
	
	public static int appendJSONObjectArray(JSONObject jsonObject,String key,JSONArray jsonArray){
		
		int proccessResult = JSONUtil.JSPROCESS_RESULT_SUCCESS;		
		try 					{jsonObject.append(key,jsonArray);}		
		catch (JSONException e) {proccessResult = JSONUtil.JSPROCESS_RESULT_FAILED;}
		
		return proccessResult;
	}
	
	public static int putJSONObjectObject(JSONObject jsonObject,String key,JSONObject jsonAdd){
		
		int proccessResult = JSONUtil.JSPROCESS_RESULT_SUCCESS;		
		try 					{jsonObject.put(key,jsonAdd);} 
		catch (JSONException e) {proccessResult = JSONUtil.JSPROCESS_RESULT_FAILED;}
		
		return proccessResult;
	}

	
	public static int putJSONObjectArray(JSONObject jsonObject,String key,JSONArray jsonArray){
				
		int proccessResult = JSONUtil.JSPROCESS_RESULT_SUCCESS;		
		try 					{jsonObject.put(key,jsonArray);}		
		catch (JSONException e) {proccessResult = JSONUtil.JSPROCESS_RESULT_FAILED;}
		
		return proccessResult;
	}

	
	
	public static int putJSONObjectString(JSONObject jsonObject,String key,String value){
		
		int proccessResult = JSONUtil.JSPROCESS_RESULT_SUCCESS;		
		try 					{jsonObject.put(key,value);} 
		catch (JSONException e) {proccessResult = JSONUtil.JSPROCESS_RESULT_FAILED;}
		
		return proccessResult;
	}
	
	public static int putJSONObjectInt(JSONObject jsonObject,String key,int value){
		
		int proccessResult = JSONUtil.JSPROCESS_RESULT_SUCCESS;		
		try 					{jsonObject.put(key,value);} 
		catch (JSONException e) {proccessResult = JSONUtil.JSPROCESS_RESULT_FAILED;}
		
		return proccessResult;
	}

	public static int putJSONObjectBoolean(JSONObject jsonObject,String key,boolean value){
		
		int proccessResult = JSONUtil.JSPROCESS_RESULT_SUCCESS;		
		try 					{jsonObject.put(key,value);} 
		catch (JSONException e) {proccessResult = JSONUtil.JSPROCESS_RESULT_FAILED;}
		
		return proccessResult;
	}

	
	public static JSONObject getJSONObject(JSONObject jsonObject,String key){
		
		JSONObject valueReturn = null;		
		try 					{valueReturn = jsonObject.getJSONObject(key);} 
		catch (JSONException e) {return null;}		
		if(valueReturn==null)	{return null;}
		
		return valueReturn;				
	}

	public static JSONArray getJSONArray(JSONObject jsonObject,String key){
		
		JSONArray valueReturn = null;		
		try 					{valueReturn = jsonObject.getJSONArray(key);} 
		catch (JSONException e) {return null;}		
		if(valueReturn==null)	{return null;}
		
		return valueReturn;				
	}
	
	@SuppressWarnings("unused")
	public static int getJSONArrayCount(JSONObject jsonObject,String key){
		
		int countReturn;
		
		JSONArray valueReturn = null;		
		try 					{countReturn = jsonObject.getJSONArray(key).length();} 
		catch (JSONException e) {return 0;}		
		if(valueReturn==null)	{return 0;}
		
		return countReturn;				
	}
	
	public static JSONObject getJSONArrayObject(JSONArray jsonArray,int itemIndex){
		
		JSONObject valueReturn = null;		
		try 					{valueReturn = jsonArray.getJSONObject(itemIndex);} 
		catch (JSONException e) {return null;}		
		if(valueReturn==null)	{return null;}
		
		return valueReturn;		
		
	}
	
	//.
	public static int getJSONObjecInJSONObjectArray(JSONObject jsonObjOrig,
													String keyJsonArray,
													int itemIndex){

		int numObjectsReturn = 0;
		try {
			numObjectsReturn = jsonObjOrig.getJSONArray(keyJsonArray).getInt(itemIndex);			
		} 		
		catch (JSONException e) {
			System.out.println( "getJSONObjectsCountInJSONObjectArray:JSONException" );			
		}		
		return numObjectsReturn;
	}

	//.
	public static int getJSONObjectsCountInJSONObjectArray(JSONObject jsonObjOrig,
															String keyJsonArray){

		int numObjectsReturn = 0;
		try {
			numObjectsReturn = jsonObjOrig.getJSONArray(keyJsonArray).length();			
		} 		
		catch (JSONException e) {
			System.out.println( "getJSONObjectsCountInJSONObjectArray:JSONException" );			
		}		
		return numObjectsReturn;
	}

	//.
	public static void putJSONObjectInJSONObjectArray(JSONObject jsonObjOrig,
													  String keyJsonArray,JSONObject jsonObject){
		
		try {
			jsonObjOrig.getJSONArray(keyJsonArray).put(jsonObject);			
		} 
		
		catch (JSONException e) {
			System.out.println( "putJSONObjectInJSONObjectArray:JSONException");
			
		}		
		
	}	 

	//.
	public static void putJSONObjectInJSONObjectArrayPostion(JSONObject jsonObjOrig,
													  		 String keyJsonArray,JSONObject jsonObject,
													  		 int objIndex){
		
		try {
			jsonObjOrig.getJSONArray(keyJsonArray).put(objIndex,jsonObject);			
		} 
		
		catch (JSONException e) {
			System.out.println( "putJSONObjectInJSONObjectArray:JSONException");
			
		}		
		
	}	 
	
	public static void deleteJSONObjectInJSONObjectArray(JSONObject jsonObjOrig,
			  											 String keyJsonArray,int objIndex){

		try {
			jsonObjOrig.getJSONArray(keyJsonArray).remove(objIndex);			
		} 
		
		catch (JSONException e) {
			System.out.println( "deleteJSONObjectInJSONObjectArray:JSONException");
		}		
	
	}	 
		
	
	//JSONObject jsonField = tableFields.getJSONObject(itemIndex);
	
	//....................................//
	// methods: update json object values //
	//....................................//	
	public static int updateJSONObjectValueString(JSONObject jsonObject,String key,String value){
						
		int proccessResult = JSONUtil.JSPROCESS_RESULT_SUCCESS;
		try {
			jsonObject.remove(key);
			jsonObject.put(key, value);
		} 
		catch (JSONException e) {		
			proccessResult = JSONUtil.JSPROCESS_RESULT_FAILED;			
		}
		
		return proccessResult;
	}
	
	public static int updateJSONObjectValueInt(JSONObject jsonObject,String key,int value){
		
		int proccessResult = JSONUtil.JSPROCESS_RESULT_SUCCESS;
		try {
			jsonObject.remove(key);
			jsonObject.put(key, value);
		} 
		catch (JSONException e) {		
			proccessResult = JSONUtil.JSPROCESS_RESULT_FAILED;			
		}
		
		return proccessResult;
	}
	
	public static int updateJSONObjectValueBoolean(JSONObject jsonObject,String key,boolean value){
		
		int proccessResult = JSONUtil.JSPROCESS_RESULT_SUCCESS;
		try {
			jsonObject.remove(key);
			jsonObject.put(key, value);
		} 
		catch (JSONException e) {		
			proccessResult = JSONUtil.JSPROCESS_RESULT_FAILED;			
		}
		
		return proccessResult;
	}
	
	/*
		this.schemeSettings.remove(OrmScheme.KEY_SETTINGS_DATABASENAME);
		try {
			this.schemeSettings.put(OrmScheme.KEY_SETTINGS_DATABASENAME,databaseName);
		} 
		catch (JSONException e) {deb.out("changeSettings_databaseName error");}			 
	*/
	
	//......................//
	// methods: conversions //
	//......................//	
	public static JSONObject toJSONObject(String jsonString) {		
		JSONObject jsonObjReturn;
		try {
			jsonObjReturn = new JSONObject(jsonString);
		} 
		catch (JSONException e) {return null;}		
		return jsonObjReturn;
	}

	//......................//
	// methods: validations //
	//......................//	
	public static boolean isValid(JSONObject jsonObj){		
		if(jsonObj.length()<1){
			return false;
		}		
		return true;
	}
	
	public static boolean isValid(JSONObject jsonObj,String[] keys){
		if(jsonObj.length()<1){
			return false;
		}		
		boolean allKeysCheck = true;
		
		for(String itemKey:keys) {
			if(!jsonObj.has(itemKey)){
				allKeysCheck = false;
				return false;
			}
		}
			
		if(!allKeysCheck) {
			return false;
		}
		return true;
	}
	
	//..............................//
	// methods: create json objects //
	//..............................//	
	
    public static JSONObject getjsonResponseError(String msgError){
    	
        JSONObject jsonObjReturn = new JSONObject();                
        try 	{jsonObjReturn.put("error",msgError);} 
        catch 	(JSONException ex) {jsonObjReturn = null;}        
        return jsonObjReturn;            
        
    } //end method

	
    
    
} //end class
