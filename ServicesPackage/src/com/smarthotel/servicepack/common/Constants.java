package com.smarthotel.servicepack.common;

/**
 * hotelCMS constants.
 * @author Carlos Guerra
 *
 */
public class Constants {

	public static final int DEFAULT_BUFFER_SIZE = 1024;
	

	/**
	 * CONFIGURATION:web.xml .
	 */	
	public static final String WEB_APLDB_HOST 		= "APLDB_HOST";
	public static final String WEB_APLDB_SID		= "APLDB_SID";
	public static final String WEB_APLDB_USER		= "APLDB_USER";
	public static final String WEB_APLDB_PASSW		= "APLDB_PASSW";
	public static final String WEB_APLDB_TIMEOUT	= "APLDB_TIMEOUT";
	public static final String WEB_APLDB_DRIVER		= "APLDB_DRIVER";
	public static final String WEB_APLDB_DB			= "APLDB_DB";
	public static final String WEB_APLDB_PORT		= "APLDB_PORT";
	
	/**
	 * Services.xml
	 */
	public static final String CONFIGURATION_SERVICES =  "WEB-INF/Services.xml";
	public static final String CONFIGURATION_AUTHENTICATION = "WEB-INF/Authentication.xml";
	public static final String SERVICE = "service";
	public static final String QUERY = "query";
	public static final String PARAM = "param";
	
	public static final String FIELD_ROL = "rol";
	public static final String FIELD_NAME = "name";
	public static final String FIELD_NOTNULL = "not-null";
	public static final String FIELD_BD = "bd";
	public static final String FIELD_SRC = "src";
	public static final String FIELD_TYPE = "type";
	public static final String FIELD_NULL = "null";
	public static final String FIELD_TRUE = "true";
	public static final String FIELD_FALSE = "false";


	public static final String PARAM_TYPE_INTEGER 	= "integer";
	public static final String PARAM_TYPE_STRING 	= "string";
	public static final String PARAM_TYPE_EMAIL 	= "email";
	public static final String PARAM_TYPE_BOOLEAN 	= "boolean";
	public static final String PARAM_TYPE_REAL 		= "real";
	public static final String PARAM_TYPE_IDUSER 	= "idUser";
	
	public static final String TYPE_SELECT = "select";
	public static final String TYPE_CHANGE = "change";
	public static final String TYPE_INSERT = "insert";
	public static final String TYPE_DELETE = "delete";

	public static final int ROL_SUPERUSER 	= 1;
	public static final int ROL_PRODUCT 	= 2;
	public static final int ROL_HEADQUARTER = 3;
	public static final int ROL_MERCHANT 	= 4;
	public static final int ROL_PUBLIC 		= 5;
	public static final int ROL_ANONIMO		= 6;
	
	public static final String AUTHENTICATION = "Authentication";
	
	public static final String AUTH_LOGIN = "login";
	public static final String AUTH_PASSWORD = "password";
	public static final String AUTH_ENCRYPT = "encrypt";
	public static final String AUTH_TABLE = "table";
	public static final String AUTH_NAME = "name";
	public static final String AUTH_SURNAME = "surname";
}
