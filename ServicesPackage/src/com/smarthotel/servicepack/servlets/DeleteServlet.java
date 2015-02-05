package com.smarthotel.servicepack.servlets;

import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.InputStream;

import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

import org.apache.log4j.Logger;
import org.json.JSONObject;

import com.smarthotel.servicepack.authentication.AuthenticationSession;
import com.smarthotel.servicepack.common.Constants;

public class DeleteServlet extends HttpServlet {
	/**
	 * For serializable interface.
	 */
	private static final long serialVersionUID = 661499421528134664L;
	static Logger logger = Logger.getLogger(DeleteServlet.class);
	
	
	public void doPost( HttpServletRequest req, HttpServletResponse res ) {
		String input = null;
		String imagesServerPath = req.getSession().getServletContext().getRealPath("/images");
		
		
		try {
			// json en el body del mensaje
			InputStream is = req.getInputStream();
			byte[] bytes = new byte[Constants.DEFAULT_BUFFER_SIZE ];
			int len;
			ByteArrayOutputStream baos = new ByteArrayOutputStream();
			
			while ( (len = is.read(bytes)) != -1 ){
				baos.write( bytes, 0, len);
			}
			
			input = new String( baos.toByteArray());
			
			JSONObject json = new JSONObject( input );
			
			String imagen = null;
			
			if ( !json.isNull( "imagen" ) ) {
				imagen = json.getString( "imagen" );
			} else {
				imagen = null;
			}		
			if ( imagen.isEmpty()  ) {
				res.getWriter().write("La entrada es incorrecta");
				res.setStatus( HttpServletResponse.SC_BAD_REQUEST );
			} else {
				HttpSession session = req.getSession(true);
				AuthenticationSession auth = (AuthenticationSession) session.getAttribute(Constants.AUTHENTICATION);
				if(auth!=null&&auth.isAuth()&&(auth.getIdRol()<=4)){
							File borrar = new File(imagesServerPath, "/"+ imagen);

					borrar.delete();
				} else {
					logger.error( "Error: No autorizado" );
					res.setStatus( HttpServletResponse.SC_UNAUTHORIZED );	//devolver error
				}
			}
		
		} catch ( Exception e ) {
			logger.error( "Error: " + e.getMessage() );
			res.setStatus( HttpServletResponse.SC_INTERNAL_SERVER_ERROR );	//devolver error
		}
	}

}
