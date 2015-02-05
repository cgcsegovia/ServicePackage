package com.smarthotel.servicepack.servlets;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.util.Iterator;
import java.util.List;

import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.log4j.Logger;
import org.apache.tomcat.util.http.fileupload.DiskFileUpload;
import org.apache.tomcat.util.http.fileupload.FileItem;

import com.smarthotel.servicepack.utils.UtilImagen;


public class UploadServlet  extends HttpServlet {
	/**
	 * For serializable interface.
	 */
	private static final long serialVersionUID = 661499421528134664L;
	static Logger logger = Logger.getLogger(UploadServlet.class);
	
	
	public void doPost( HttpServletRequest req, HttpServletResponse res ) {
		String imagesServerPath = req.getSession().getServletContext().getRealPath("/images");
		
		
		try {
			
			// Create a new file upload handler
			DiskFileUpload upload = new DiskFileUpload();
		
			@SuppressWarnings("unchecked")
			List<FileItem> items = (List<FileItem>)upload.parseRequest( req );
	
			// get parametres and file
			String path = "";
			FileItem fileItem = null;
			Iterator<FileItem> iter = items.iterator();
			
			while ( iter.hasNext() ) {
				
				FileItem item = (FileItem) iter.next();
				
				
				if ( item.isFormField() ) {
					
					logger.debug( "field " + item.getFieldName() + " : " + item.getString() );
					String fieldName =  item.getFieldName();
					
					if ( fieldName.compareTo( "id" ) == 0 ) {
						path = item.getString();
					}
				
				} else {
					fileItem = item;
				}
				
			}
			
			//si se ha recibido todo lo necesario
			if ( fileItem != null ) {
						
				//get parameters
//				ServletContext context = getServletContext();
				
				
				//copiar fichero
				File tosave = new File( imagesServerPath, "/" +  path +"_sin.jpg" );
				logger.debug( " dest: " + tosave.getAbsolutePath() );
				fileItem.write( tosave );
				File resultado = new File(imagesServerPath, "/" +  path +".jpg" );
				UtilImagen.resize(new FileInputStream(tosave), new FileOutputStream(resultado), 300, 300);
				tosave.delete();
				logger.debug( "file transformed: ");
				
			    //devolver html para refrescar
			    res.setStatus( HttpServletResponse.SC_OK );
			    logger.debug( "file uploaded: " + tosave.getAbsolutePath() );
			} else {
				logger.debug( "missing parameters" );
				res.setStatus( HttpServletResponse.SC_BAD_REQUEST ); 
			}
			
		
		} catch ( Exception e ) {
			logger.error( "Error: " + e.getMessage() );
			//e.printStackTrace();
			res.setStatus( HttpServletResponse.SC_INTERNAL_SERVER_ERROR );	//devolver error
		}
	}

}
