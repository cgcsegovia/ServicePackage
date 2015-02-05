package com.smarthotel.servicepack.servlets;

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.sql.PreparedStatement;
import java.sql.ResultSet;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

import org.apache.log4j.Logger;
import org.json.JSONException;
import org.json.JSONObject;

import com.smarthotel.servicepack.authentication.AuthenticationSession;
import com.smarthotel.servicepack.authentication.QueryAuthentication;
import com.smarthotel.servicepack.common.Constants;
import com.smarthotel.servicepack.connection.DBConnector;


public class AutenticationServlet extends HttpServlet {
	/**
	 * For serializable interface.
	 */
	private static final long serialVersionUID = 661499421528134664L;
	static Logger logger = Logger.getLogger(AutenticationServlet.class);

	public void doPost( HttpServletRequest req, HttpServletResponse res ) {
		DBConnector conn = DBConnector.getInstance();

		QueryAuthentication qa = QueryAuthentication.getInstance();
		
		String input = null;
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
			
			String service = null;
			JSONObject params  = null;
			
			if ( !json.isNull( "service" ) ) {
				service = json.getString( "service" );
			} else {
				service = null;
			}
			if ( !json.isNull( "params" ) ) {
				params = json.getJSONObject( "params" );
			} else {
				params = null;
			}			
			
			if ( service.isEmpty() || params== null ) {
				res.getWriter().write("La entrada es incorrecta");
				res.setStatus( HttpServletResponse.SC_BAD_REQUEST );
				return;
			} else {
				if (service.equals("getLogin")){
					String login = null;
					if ( !params.isNull( "login" ) ) {
						login = params.getString("login");
					}
					String password = null;
					if ( !params.isNull( "password" ) ) {
						password = params.getString("password");
					}
					
					if ( login==null || login.isEmpty() || password==null || password.isEmpty() ) {
						res.getWriter().write("La entrada es incorrecta");
						res.setStatus( HttpServletResponse.SC_BAD_REQUEST );
						return;
					} else {
						StringBuilder query =  new StringBuilder();
			            query.append("SELECT u.id, u."+qa.getAuthentication().getName()+", u."+qa.getAuthentication().getSurname()+", u.rol_id, r.descripcion as rol from "+qa.getAuthentication().getTable());
			            query.append(" u inner join ROL r on u.rol_id=r.id  WHERE "+qa.getAuthentication().getLogin()+"=? AND ");
			            query.append(qa.getAuthentication().getPassword()+"=");
			            if(qa.getAuthentication().getEncrypt().equals("md5")) // md5
			            {	 query.append("MD5(?)");  }
			            else if(qa.getAuthentication().getEncrypt().equals("normal"))// normal
			            {	 query.append("?"); }
		         
			            conn.open();
			            
			            if ( !conn.getOpen() ) { 
			            	logger.error( "--doPost(req,res) ERROR: La conexion esta cerrada" );
			            	res.setStatus( HttpServletResponse.SC_INTERNAL_SERVER_ERROR );
			            	return;
			            }
			            
			            PreparedStatement ps  = conn.prepareStatement( query.toString() );
			            ps.setString( 1, login );
			            ps.setString( 2, password ); 
						
			            // Execute query.
			            logger.debug( "--doPost(req,res) Query:" + ps.toString() );
			            
			        	ResultSet rs = ps.executeQuery();
			        	
			        	if(rs==null){
			        		res.setStatus( HttpServletResponse.SC_UNAUTHORIZED );
			        		return;
			        	} else {
			        
			        		AuthenticationSession auth = new AuthenticationSession();
			        		
				        	if(rs.next()) {
				        		auth.setAuth(true);
				        		auth.setIdUser(rs.getInt("id"));
				        		auth.setName(rs.getString(qa.getAuthentication().getName()));
				        		auth.setSurname(rs.getString(qa.getAuthentication().getSurname()));
				        		auth.setIdRol(rs.getInt("rol_id"));
				        		auth.setRol(rs.getString("rol"));
				        		HttpSession session = req.getSession(true);
				        		session.setAttribute(Constants.AUTHENTICATION, auth);
				        	} else {
				        		res.setStatus( HttpServletResponse.SC_UNAUTHORIZED );
				        		return;
				        	}
				        	res.setStatus(HttpServletResponse.SC_OK);
				            return;        	
			        	}
					}
				} else if (service.equals("getLogout")){
					HttpSession session = req.getSession(true);
					session.setAttribute(Constants.AUTHENTICATION, null);
				} else if((service.equals("getAutentication"))){ 
					HttpSession session = req.getSession(true);
					AuthenticationSession auth = (AuthenticationSession) session.getAttribute(Constants.AUTHENTICATION);
					if (auth==null){
						res.setStatus( HttpServletResponse.SC_NO_CONTENT );
					} else {
						JSONObject salida = new JSONObject();
						try {
							salida.put( qa.getAuthentication().getName(), auth.getName() );
							salida.put( qa.getAuthentication().getSurname(), auth.getSurname() );
							salida.put( "rol_id", auth.getIdRol() );
							salida.put( "rol", auth.getRol() );
						} catch (JSONException e) {
							res.getWriter().write("Ocurrió un error");
							res.setStatus( HttpServletResponse.SC_INTERNAL_SERVER_ERROR );
							return;
						}
						res.getWriter().write(salida.toString());
						res.setStatus( HttpServletResponse.SC_OK );
					}
				}
			}
		} catch ( Exception e ) {
			
			logger.error( "Error: " + e.getMessage() );
			
			//cerrar conexi�n
			if ( conn != null ) {
				conn.rollback();
				conn.close();
			}
			
			//devolver error
			try {
				res.setStatus( HttpServletResponse.SC_INTERNAL_SERVER_ERROR );
				res.getWriter().write( "ERROR:" + e.getMessage() );
			} catch ( IOException e2 ) { 
				logger.error( "Error: " + e.getMessage() );
			}
			
			
		}		
	}
/** Intentando evitar cross domain con jsonp **/
	@Override
	protected void doGet(HttpServletRequest req, HttpServletResponse resp)
			throws ServletException, IOException {
		this.doPost(req, resp);
	}

}
