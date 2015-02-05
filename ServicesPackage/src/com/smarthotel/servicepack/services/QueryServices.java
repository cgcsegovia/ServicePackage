package com.smarthotel.servicepack.services;

import java.io.File;
import java.util.HashMap;
import java.util.Map;

import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;

import org.apache.log4j.Logger;
import org.w3c.dom.Document;
import org.w3c.dom.Element;
import org.w3c.dom.Node;
import org.w3c.dom.NodeList;

import com.smarthotel.servicepack.common.Constants;



public class QueryServices {
	private static final QueryServices OBJECT_QS = new QueryServices();
	
	static Logger logger = Logger.getLogger( QueryServices.class );
	
	private Map<String,Service> services = new HashMap<String, Service>();
	/**
	 * Singleton.
	 * @return QueryServices singleton instance.
	 */
	public static QueryServices getInstance() {
        return OBJECT_QS;
    }
	
	public Map<String, Service> getServices() {
		return services;
	}
	
	public void setServices(Map<String, Service> services) {
		this.services = services;
	}
	/**
	 * Loads the xml configuration for services. 
	 * @param config xml in WEB-INF/
	 */
	public void loadXMLServices( String config ){
		logger.trace( ">> loadXMLServices(...) Entering... " );
		try {
			
			File fXmlFile = new File( config );
			DocumentBuilderFactory dbFactory = DocumentBuilderFactory.newInstance();
			DocumentBuilder dBuilder = dbFactory.newDocumentBuilder();
			
			Document docConf = dBuilder.parse( fXmlFile );
			docConf.getDocumentElement().normalize();

			setServices(xmlToMapConfiguration(docConf));
			
		} catch ( Exception e ) {
			logger.error( "-- loadXMLServices(...) Error: " + e.getMessage() );
		}
		
		logger.trace( "<< loadXMLServices(...) Leaving... " );
		
	}
	/**
	 * Obtains the service/querys information.
	 * @param docConf xml file 
	 * @return Map with the service/querys information
	 */
	private Map<String, Service> xmlToMapConfiguration( Document docConf ) {
		logger.trace( ">> xmlToMapConfiguration(...) Entering... " );
		Map<String, Service> salida = new HashMap<String, Service>();
		NodeList sList = docConf.getElementsByTagName( Constants.SERVICE );
		
		for ( int temp = 0; temp < sList.getLength(); temp++ ) {
			
			Node sNode = sList.item( temp );
			
			if ( sNode.getNodeType() == Node.ELEMENT_NODE ) {
				
				Element serviceElmnt = (Element) sNode;
				
				String nameService = serviceElmnt.getAttribute( Constants.FIELD_NAME );
				
				Service service = new Service();
				
				service.setRol( serviceElmnt.getAttribute( Constants.FIELD_ROL )=="" ? Constants.ROL_PUBLIC : Integer.parseInt( serviceElmnt.getAttribute( Constants.FIELD_ROL ) ) );
				service.setName( serviceElmnt.getAttribute( Constants.FIELD_NAME ) );
				service.setBbdd( serviceElmnt.getAttribute( Constants.FIELD_BD ) );
				
				Query query = new Query();
				
				NodeList qList = serviceElmnt.getElementsByTagName( Constants.QUERY );
				
				for ( int temp2 = 0; temp2 < qList.getLength(); temp2++ ) {
					
					Node qNode = qList.item( temp2 );
					
					if ( qNode.getNodeType() == Node.ELEMENT_NODE ) {
						
						Element queryElmnt = (Element) qNode;
						
						query.setSrc( queryElmnt.getAttribute( Constants.FIELD_SRC ) );
						query.setType( queryElmnt.getAttribute( Constants.FIELD_TYPE ) );
						query.setName( queryElmnt.getAttribute( Constants.FIELD_NAME ) );
						
						NodeList pList = serviceElmnt.getElementsByTagName( Constants.PARAM );					
						for ( int temp3 = 0; temp3 < pList.getLength(); temp3++ ) {
							
							Node pNode = pList.item( temp3 );
							
							if ( pNode.getNodeType() == Node.ELEMENT_NODE ) {
								Param param = new Param();
								
								Element paramElmnt = (Element) pNode;
								
								param.setType( paramElmnt.getAttribute( Constants.FIELD_TYPE ) );
								param.setName( paramElmnt.getAttribute( Constants.FIELD_NAME ) );
								if (!paramElmnt.getAttribute( Constants.FIELD_NOTNULL ).isEmpty()){
									param.setNotNull( paramElmnt.getAttribute( Constants.FIELD_NOTNULL ) );
								} else {
									param.setNotNull( Constants.FIELD_FALSE );
								}
								
								
								query.getParams().add(param);
								
							}
						}
					}
				}
				service.setQuery(query);
								
				salida.put( nameService, service );
				
			}
		}
		logger.trace( "<< xmlToMapConfiguration(...) Leaving... " );
		return salida;		
	}
	
	
}
