package com.smarthotel.servicepack.connection;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.Timer;
import java.util.TimerTask;

import org.apache.log4j.Logger;

import com.smarthotel.servicepack.exceptions.ConnectionException;





/**
 * Handles DBConnections.
 * @author Carlos Guerra.
 */
public class DBConnector extends TimerTask {
	
	static Logger logger = Logger.getLogger( DBConnector.class );
	
	private String host;
	private String sid;
	private String user;
	private String pass;
	private String driver;
	private String database;
	private String port;
	private Boolean open = Boolean.FALSE; 
	private boolean accessBD = false;
	
	private Connection conn;
	private Timer timerTimeout = new Timer();     
	
	
	private static final DBConnector OBJECT_CONN = new DBConnector();
	private int timeout;
	
	/**
	 * Singleton.
	 * @return DBConnector singleton instance.
	 */
	public static DBConnector getInstance() {
        return OBJECT_CONN;
    }

	/**
	 * Inicializo los datos de la conexi�n en la clase.
	 * @param host 		The database host.
	 * @param sid 		The database sid
	 * @param user 		The database user
	 * @param pass 		The database password
	 * @param timeout 	Connection timeout.
	 * @param database 	The database name
	 * @param driver 	The jdbc driver to use.
	 * @param port 		Database port.
	 * @throws ConnectionException When connection could not get initialized.
	 */
    public void init( String host, String sid, String user, String pass, int timeout, 
    		String database, String driver, String port ) throws ConnectionException {

    	logger.trace( ">> init(...) Entering init..." );
    	timerTimeout = new Timer();	
	 	timerTimeout.scheduleAtFixedRate( this, 0, timeout * 1000 );
    	try {
    		
    		this.host = host;
    		this.sid = sid;
    		this.user = user;
    		this.pass = pass;
    		this.timeout = timeout;
    		this.driver = driver;
    		this.database = database;
    		this.port = port;
    		
    		if ( driver.equals( "ORACLE" ) ) {
    			
    			logger.debug( "-- init(...) Oracle driver selected." );
    			Class.forName( "oracle.jdbc.driver.OracleDriver" );
    			
    		} else if ( driver.equals( "MYSQL" ) ) {
    			
    			logger.debug( "-- init(...) MySQL driver selected." );
    			Class.forName( "com.mysql.jdbc.Driver" );
    			
    		} else {
    			throw new ConnectionException( "Driver no valido, ORACLE o MYSQL" );
    		}
    		
    		logger.debug( "Conection initialized. Timeout: " + this.timeout );

    		
		} catch ( ClassNotFoundException e ) {
			
			throw new ConnectionException( "No hay driver" );

		} finally {
			
			open = Boolean.FALSE;
		}
    	
    	
    	logger.trace( "<< init(...) Leaving init..." );
    	
    }
    
    /**
     * Checks database.
     * @return True/false.
     */
    public boolean isAccessBD() {
		return accessBD;
	}
    
    /**
     * Establishes the accessBD parameter.
     * @param accessBD The desired accessBD value.
     */
	public synchronized void setAccessBD( boolean accessBD ) {
		this.accessBD = accessBD;
	}
	
	/**
     * Ejecutado cuando se para la aplicacion. 
     */
    public void finish() {

    	// cierro la conexion si esta abierta
    	if ( open ) {
    		close();
    	}
    	
    	// paro el timer de escucha
    	timerTimeout.cancel();
    }
    
    /**
     * Cierro la conexion.
     */
    public void close() {
    	logger.trace( ">> close() Entering close." );
    	
    	if ( !open ) {
    		return;
    	}
    	
    	try {
			conn.close();
			
		} catch ( SQLException e ) {
			logger.error( "-- close() Error: " + e.getMessage() );
			e.printStackTrace();
		}
    	
		open = Boolean.FALSE;
		logger.trace( "<< close() Leaving close." );
    }
    
    
    /**
     * Inicializo la conexion a la base de datos.
     * @throws ConnectionException When connection could not get initialized.
     */
    private void initConnection() throws ConnectionException {
    
    	logger.trace( ">> initConection(...) Entering initConection." );
    	setAccessBD( false );
    	
    	if ( !open ) {
    		
			try {
				
				logger.info( "Abriendo la conexion...." );
				
				DriverManager.setLoginTimeout( timeout );
				logger.info( "Timeout real..." + DriverManager.getLoginTimeout() );
				
				if ( driver.equals( "ORACLE" ) ) {

					conn = DriverManager.getConnection( "jdbc:oracle:thin:@//" + host + ":"
																   + port + "/" + sid, user, pass );
					
				} else if ( driver.equals( "MYSQL" ) ) {

					conn = DriverManager.getConnection( "jdbc:mysql://" + host + ":" + port 
																	 + "/" + database, user, pass );
					
				} else {
					throw new ConnectionException( "Driver no valido, ORACLE o MYSQL" );
				}
				
				conn.setAutoCommit( false );
				
				open = Boolean.TRUE;

			} catch ( SQLException e ) {
				
				open = Boolean.FALSE;
				logger.error( "Fallo al obtener la conexi�n" );
				throw new ConnectionException( "Fallo al obtener la conexion" );
			}
    	}
    	
    	
    	logger.trace( "<< initConection(...) Leaving initConection." );
    }
    
    
    /**
     * Ejecuta una query sobre bd.
     * @param query Query to execute.
     * @return ResultSet The query results.
     * @throws ConnectionException If there was any problem while connecting to database.
     */
    public   ResultSet getRSQuery( String query ) throws ConnectionException {
    	
    	logger.trace( ">> getRSQuery(" + query + ") Entering getRSQuery." );
    	
    	//si no esta abierta la conexi�n, se abre
    	if ( !open ) {
    		initConnection();
    	}
    	
    	//se indica que ha habido consulta en este periodo
    	setAccessBD( true );
    	
    	//ejecutar query
    	Statement stmt;
    	
		try {
			
			stmt = conn.createStatement();
			logger.debug( "query: " + query );
	        ResultSet rst = stmt.executeQuery( query );
		    
	        if ( rst != null ) {
	        	
		    	return rst;
		    	
		    } else {
		    	
		    	//this.close();
		    	throw new ConnectionException( "El recordset es vacio." );
		    }
	        
		} catch ( SQLException e ) {
			this.open = Boolean.FALSE;
			//this.close();
		}
		
		logger.trace( "<< getRSQuery(" + query + ") Leaving getRSQuery." );
		return null;
    }

    
    /**
     * Metodo ejecutado pot timer periodicamente.
     * Comprueba si tengo q cerrar la conexion por inactividad
     */
	@Override
	public void run() {	
	
		//logger.debug( ">> run() Entering run." );
		
		//si no ha habido acceso en el periodo y la conexion est� abierta
		
		if ( !accessBD && open ) {
		
			//cerrar la conexion
			if ( conn != null ) {
				close();
			}
			
			logger.debug(  "Cerrando la conexion................" );
			open = Boolean.FALSE;			
		}
		
		//se inicializa tag para indicar si hay aceesos duracte periodo
		accessBD = false;
		//logger.debug( "<< run() Leaving run." );
		
	}      
	

	/**
	 * Creates a PreparedStatement object given a query String.
	 * @param statement The query string.
	 * @return The PreparedStatement for the query String.
	 */
	
	public PreparedStatement prepareStatement( String statement ) {
		
		logger.trace( ">> prepareStatement( " + statement + ") Entering prepareStatement." );
		PreparedStatement result = null;
		
		try {
			
			result = conn.prepareStatement( statement, Statement.RETURN_GENERATED_KEYS );
			
		} catch ( SQLException e ) {
			logger.error( "-- prepareStatement( " + statement + " ) Error: " + e.getMessage() );
		}
		
		logger.trace( "<< prepareStatement( " + statement + ") Leaving prepareStatement." );
		return result;
	}

	
	/**
	 * Retrieves the java.sql.Connection object (private member).  
	 * @return The Connection in this connector. 
	 */
	public Connection getConnection() {
		return this.conn;
	}
	
    /**
     * Abro la conexion.
     */
    public void open() {
    	logger.trace( ">> open() Entering open." );
    	
    	if ( open ) {
    		return;
    	}
    	
    	try {
    		
			initConnection();
			open = Boolean.TRUE;
			

		} catch ( ConnectionException e ) {
			logger.error( "-- Error: " + e.getMessage() );
			open = Boolean.FALSE;
		}
    	
		
		logger.trace( "<< open() Leaving open." );
    }
    
    /**
     * Obtengo si esta abierta o no la conexion.
     * @return True if connection is open. False otherwise.
     */
	public Boolean getOpen() {
		return open;
	}
	/**
	 * Hace commit sobre la conexi�n.
	 */
	public void commit(){
		logger.trace( ">> commit() Entering commit." );
		if ( !open ){ 
			open(); 
		}
		try {
			conn.commit();
		} catch ( SQLException e ) {
			try {
				conn.rollback();
			} catch ( SQLException e1 ) {
				logger.debug( "rollback. " + e1.getMessage() );
			}
		}
		logger.trace( "<< commit() Leaving commit." );
	}
	/**
	 * Hace rollback sobre la conexi�n.
	 */
	public void rollback(){
		logger.trace( ">> rollback() Entering commit." );
		if ( !open ){ 
			open();
		}
		try {
			conn.rollback();
		} catch ( SQLException e ) {
			logger.debug( "rollback. " +  e.getMessage() );
		}
		logger.trace( "<< rollback() Leaving commit." );
	}
	/**
	 * Cambia la base de  datos de acceso
	 * @param database base de datos
	 * @throws ConnectionException 
	 */
	public void setDatabase(String database) throws ConnectionException{
		if (!this.database.equals(database)){
			this.database = database;
    		if(open) close();
    		initConnection();
		}
	}

	public PreparedStatement prepareStatement(String src,
			int returnGeneratedKeys) {
		// TODO Auto-generated method stub
		return null;
	}

	
} //END
