package com.smarthotel.servicepack.connection;

import javax.servlet.ServletContext;
import javax.servlet.ServletContextEvent;
import javax.servlet.ServletContextListener;

import org.apache.log4j.BasicConfigurator;
import org.apache.log4j.Logger;

import com.smarthotel.servicepack.authentication.QueryAuthentication;
import com.smarthotel.servicepack.common.Constants;
import com.smarthotel.servicepack.exceptions.ConnectionException;
import com.smarthotel.servicepack.services.QueryServices;



/**
 * ConnectionListener class. Initializes the connection and keeps it alive.
 * @author Carlos Guerra
 */
public class ConnectionListener implements ServletContextListener  {
	
	static Logger logger = Logger.getLogger( ConnectionListener.class );

	@Override
	public void contextDestroyed( ServletContextEvent arg0 ) {
		
		logger.trace( "Parando servicio de conexion............................................." );
		DBConnector.getInstance().finish();
		
	}

	@Override
	public void contextInitialized( ServletContextEvent arg0 ) {
		
		BasicConfigurator.configure();

		logger.trace( "Arrancando servicio de conexion.........................................." );
		
		ServletContext context = arg0.getServletContext();
		
		String host 		= context.getInitParameter( Constants.WEB_APLDB_HOST 		);
		String sid 			= context.getInitParameter( Constants.WEB_APLDB_SID 		);
		String user 		= context.getInitParameter( Constants.WEB_APLDB_USER 		);
		String pass 		= context.getInitParameter( Constants.WEB_APLDB_PASSW 		);
		String timeout 		= context.getInitParameter( Constants.WEB_APLDB_TIMEOUT 	);
		
		String driver 		= context.getInitParameter( Constants.WEB_APLDB_DRIVER 		);
		String database 	= context.getInitParameter( Constants.WEB_APLDB_DB 			);
		String port			= context.getInitParameter( Constants.WEB_APLDB_PORT 		);
		String configfile	= context.getRealPath( 		Constants.CONFIGURATION_SERVICES );
		String authfile	    = context.getRealPath( 		Constants.CONFIGURATION_AUTHENTICATION );

		DBConnector conn =  DBConnector.getInstance();
				
		try {
			
			conn.init( host, sid, user, pass, Integer.parseInt( timeout ), database, driver, port );

		} catch ( ConnectionException e ) {
			
			logger.error( "Error: " + e.getMessage() );
		}
		
		logger.trace( "Arrancando servicio de querys.........................................." );
		
		QueryServices qs = QueryServices.getInstance();
		
		qs.loadXMLServices( configfile );

		logger.trace( "Arrancando autenticación..............................................." );
		
		QueryAuthentication sa = QueryAuthentication.getInstance();
		
		sa.loadXMLAuthentication( authfile );
	}

}
