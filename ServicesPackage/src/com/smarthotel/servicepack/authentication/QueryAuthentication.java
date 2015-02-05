package com.smarthotel.servicepack.authentication;

import java.io.File;

import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;

import org.apache.log4j.Logger;
import org.w3c.dom.Document;
import org.w3c.dom.Element;
import org.w3c.dom.Node;
import org.w3c.dom.NodeList;

import com.smarthotel.servicepack.common.Constants;

public class QueryAuthentication {
	private static final QueryAuthentication OBJECT_QS = new QueryAuthentication();
	
	static Logger logger = Logger.getLogger( QueryAuthentication.class );
	
	private Authentication authentication = new Authentication();
	/**
	 * Singleton.
	 * @return QueryServices singleton instance.
	 */
	public static QueryAuthentication getInstance() {
        return OBJECT_QS;
    }
	
	public Authentication getAuthentication() {
		return authentication;
	}

	public void setAuthentication(Authentication authentication) {
		this.authentication = authentication;
	}

	/**
	 * Loads the xml configuration for authentication. 
	 * @param config xml in WEB-INF/
	 */
	public void loadXMLAuthentication( String config ){
		logger.trace( ">> loadXMLAuthentication(...) Entering... " );
		try {
			
			File fXmlFile = new File( config );
			DocumentBuilderFactory dbFactory = DocumentBuilderFactory.newInstance();
			DocumentBuilder dBuilder = dbFactory.newDocumentBuilder();
			
			Document docConf = dBuilder.parse( fXmlFile );
			docConf.getDocumentElement().normalize();

			setAuthentication(xmlToAuthConfiguration(docConf));
			
		} catch ( Exception e ) {
			logger.error( "-- loadXMLAuthentication(...) Error: " + e.getMessage() );
		}
		
		logger.trace( "<< loadXMLAuthentication(...) Leaving... " );
		
	}
	/**
	 * Obtains the Authentication information.
	 * @param docConf xml file 
	 * @return Authentication information
	 */
	private Authentication xmlToAuthConfiguration( Document docConf ) {
		logger.trace( ">> xmlToAuthConfiguration(...) Entering... " );
		Authentication salida = new Authentication();
		NodeList sList = docConf.getElementsByTagName( Constants.AUTHENTICATION );
		
		for ( int temp = 0; temp < sList.getLength(); temp++ ) {
			
			Node sNode = sList.item( temp );
			
			if ( sNode.getNodeType() == Node.ELEMENT_NODE ) {
				
				Element serviceElmnt = (Element) sNode;
				
				salida.setEncrypt( serviceElmnt.getAttribute( Constants.AUTH_ENCRYPT  ) );
				salida.setTable( serviceElmnt.getAttribute( Constants.AUTH_TABLE  ) );
				salida.setName( serviceElmnt.getAttribute( Constants.AUTH_NAME  ) );
				salida.setSurname( serviceElmnt.getAttribute( Constants.AUTH_SURNAME  ) );
				salida.setLogin( serviceElmnt.getAttribute( Constants.AUTH_LOGIN ) );
				salida.setPassword( serviceElmnt.getAttribute( Constants.AUTH_PASSWORD ) );

			}
		}
		logger.trace( "<< xmlToAuthConfiguration(...) Leaving... " );
		return salida;		
	}
}
