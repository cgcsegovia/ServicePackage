package com.smarthotel.servicepack.servlets;

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Types;

import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

import org.apache.log4j.Logger;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import com.smarthotel.servicepack.authentication.AuthenticationSession;
import com.smarthotel.servicepack.common.Constants;
import com.smarthotel.servicepack.connection.DBConnector;
import com.smarthotel.servicepack.exceptions.ConnectionException;
import com.smarthotel.servicepack.exceptions.ValidationException;
import com.smarthotel.servicepack.services.Param;
import com.smarthotel.servicepack.services.QueryServices;
import com.smarthotel.servicepack.services.Service;
import com.smarthotel.servicepack.utils.BDUtils;
import com.smarthotel.servicepack.utils.ValidationUtils;

/**
 * @author Carlos Guerra.
 */
public class ServiceServlet extends HttpServlet {

	/**
	 * For serializable interface.
	 */
	private static final long serialVersionUID = 661499421528117664L;
	static Logger logger = Logger.getLogger(ServiceServlet.class);

	public void doPost( HttpServletRequest req, HttpServletResponse res ) {
		DBConnector conn = DBConnector.getInstance();
		
		QueryServices qs = QueryServices.getInstance();
		
		
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
			} else {
				
					res.setContentType( "application/x-json;charset=UTF-8" );
					res.setCharacterEncoding( "UTF-8" );
			
					Service serviceObj = qs.getServices().get(service);
					if (serviceObj==null){
						res.getWriter().write("El servicio " + service + " no existe");
						res.setStatus( HttpServletResponse.SC_NOT_FOUND );
					} else {
						
						if (serviceObj.getRol()!=Constants.ROL_ANONIMO){
							HttpSession session = req.getSession(true);
							AuthenticationSession auth = (AuthenticationSession) session.getAttribute(Constants.AUTHENTICATION);
							if(auth!=null&&auth.isAuth()&&(auth.getIdRol()<=serviceObj.getRol())){
								JSONObject salida = executeService(conn,serviceObj,params,auth);
								res.getWriter().write(salida.toString());
								res.setStatus( HttpServletResponse.SC_OK );
							} else {
								res.getWriter().write("Servicio no autorizado");
								res.setStatus( HttpServletResponse.SC_NON_AUTHORITATIVE_INFORMATION );
							}
						} else {
							JSONObject salida = executeService(conn,serviceObj,params, null);
							res.getWriter().write(salida.toString());
							res.setStatus( HttpServletResponse.SC_OK );
						}
								
						
					}
				
			}	
		} catch ( SQLException e ) {
			if(e.getSQLState().equals("23000")){
				// Fallo de índice
				try {
					res.getWriter().write("Falla un índice."+e.getMessage());
				} catch (IOException e1) {
					logger.error( "Error: " + e1.getMessage() );
				}
				res.setStatus( HttpServletResponse.SC_CONFLICT);
			} else {
				try {
					res.getWriter().write(e.getMessage());
				} catch (IOException e1) {
					logger.error( "Error: " + e1.getMessage() );
				}
				res.setStatus( HttpServletResponse.SC_INTERNAL_SERVER_ERROR);
			}
		} catch ( ValidationException e ){
			try {
			res.getWriter().write(e.getMessage());
			} catch ( IOException e2 ) { 
				logger.error( "Error: " + e2.getMessage() );
			}
			res.setStatus( HttpServletResponse.SC_BAD_REQUEST);
		} catch ( Exception e ) {
			
			logger.error( "Error: " + e.getMessage() );
			res.setStatus( HttpServletResponse.SC_INTERNAL_SERVER_ERROR );
			
			//cerrar conexi�n
			if ( conn != null ) {
				/*conn.rollback();*/
				conn.close();
			}
			
			//devolver error
			try {
				res.getWriter().write( "ERROR:" + e.getMessage() );
			} catch ( IOException e2 ) { 
				logger.error( "Error: " + e2.getMessage() );
			}
			
			
		}
		
	}


	private JSONObject executeService(DBConnector conn, Service serviceObj, JSONObject params, AuthenticationSession auth) throws ValidationException,ConnectionException, JSONException, NumberFormatException, SQLException {
		JSONObject salida = new JSONObject();
		conn.setDatabase(serviceObj.getBbdd());

		if (serviceObj.getQuery().getType().equals(Constants.TYPE_SELECT)) {
			conn.open();
			PreparedStatement pst = conn.prepareStatement(serviceObj.getQuery()
					.getSrc());
			int i = 1;
			for (Param param : serviceObj.getQuery().getParams()) {

				if (param.getType().equals(Constants.PARAM_TYPE_BOOLEAN)) {
					if(params.isNull(param.getName())&&param.getNotNull().equals(Constants.FIELD_TRUE)){
						i++;
						logger.error("Falla el campo: "+param.getName()+", llega a null.");
						throw new ValidationException("Falla el campo: "+param.getName()+", llega a null.");
					} else {
						Boolean valor = (params.isNull(param.getName())?null:params.getBoolean(param.getName()));
						if (valor == null){						
							pst.setNull(i++, Types.NULL);
						} else {
							pst.setBoolean(i++,	valor);
						}
					}
				} else if (param.getType().equals(Constants.PARAM_TYPE_INTEGER)) {
					if(params.isNull(param.getName())&&param.getNotNull().equals(Constants.FIELD_TRUE)){
						i++;
						logger.error("Falla el campo: "+param.getName()+", llega a null.");
						throw new ValidationException("Falla el campo: "+param.getName()+", llega a null.");
					} else {
						Integer valor = (params.isNull(param.getName())?null:params.getInt(param.getName()));
						if (valor == null){						
							pst.setNull(i++, Types.NULL);
						} else {
							pst.setInt(i++,	valor);
						}
					}
				} else if (param.getType().equals(Constants.PARAM_TYPE_REAL)) {
					if(params.isNull(param.getName())&&param.getNotNull().equals(Constants.FIELD_TRUE)){
						i++;
						logger.error("Falla el campo: "+param.getName()+", llega a null.");
						throw new ValidationException("Falla el campo: "+param.getName()+", llega a null.");
					} else {
						Double valor = (params.isNull(param.getName())?null:params.getDouble(param.getName()));
						if (valor == null){						
							pst.setNull(i++, Types.NULL);
						} else {
							pst.setDouble(i++,	valor);
						}
					}
				} else if (param.getType().equals(Constants.PARAM_TYPE_STRING)) {
					if(params.getString(param.getName()).isEmpty()&&param.getNotNull().equals(Constants.FIELD_TRUE)){
						i++;
						logger.error("Falla el campo: "+param.getName()+", llega a null.");
						throw new ValidationException("Falla el campo: "+param.getName()+", llega a null.");
					} else {
						String valor = (params.isNull(param.getName())?null:params.getString(param.getName()));
						if (valor == null){						
							pst.setNull(i++, Types.NULL);
						} else {
							pst.setString(i++,	valor);
						}
					}
				} else if (param.getType().equals(Constants.PARAM_TYPE_EMAIL)) {
					if(params.getString(param.getName()).isEmpty()&&param.getNotNull().equals(Constants.FIELD_TRUE)){
						i++;
						logger.error("Falla el campo: "+param.getName()+", llega a null.");
						throw new ValidationException("Falla el campo: "+param.getName()+", llega a null.");
					}else{
						if (ValidationUtils.validateEmail( params.getString(param.getName()))){
							String valor = (params.isNull(param.getName())?null:params.getString(param.getName()));
							if (valor == null){						
								pst.setNull(i++, Types.NULL);
							} else {
								pst.setString(i++,	valor);
							}
						}else{
							throw new ValidationException("El campo "+param.getName()+"Es incorrecto.");
						}
					}
				} else if (param.getType().equals(Constants.PARAM_TYPE_IDUSER)) {
					if(auth!= null && param.getNotNull().equals(Constants.FIELD_TRUE)){
						pst.setLong(i++,	auth.getIdUser());
					}else{
						throw new ValidationException("El campo "+param.getName()+"Es incorrecto.");
					}
				} else {
					i++;
					logger.error("Falla el campo: "+param.getName());
					throw new ValidationException("Falla el campo: "+param.getName());
				}

			}
			logger.debug(pst.toString());
			ResultSet rs = pst.executeQuery();
			
			JSONArray arr = BDUtils.RStoJSON(rs);
			salida.put(serviceObj.getQuery().getName(),arr );
			rs.close();
			pst.close();
		} else if (serviceObj.getQuery().getType().equals(Constants.TYPE_CHANGE)) {
			conn.open();
			PreparedStatement pst = conn.prepareStatement(serviceObj.getQuery()
					.getSrc());
			int i = 1;
			JSONObject salidaObj = new JSONObject();
			for (Param param : serviceObj.getQuery().getParams()) {
				if (param.getType().equals(Constants.PARAM_TYPE_BOOLEAN)) {
					if(params.isNull(param.getName())&&param.getNotNull().equals(Constants.FIELD_TRUE)){
						i++;
						logger.error("Falla el campo: "+param.getName()+", llega a null.");
						throw new ValidationException("Falla el campo: "+param.getName()+", llega a null.");
					}else{
						Boolean valor = (params.isNull(param.getName())?null:params.getBoolean(param.getName()));
						if (valor == null){						
							pst.setNull(i++, Types.NULL);
						} else {
							pst.setBoolean(i++,	valor);
						}
						salidaObj.put(param.getName(), valor);
					}
				} else if (param.getType().equals(Constants.PARAM_TYPE_INTEGER)) {
					if(params.isNull(param.getName())&&param.getNotNull().equals(Constants.FIELD_TRUE)){
						i++;
						logger.error("Falla el campo: "+param.getName()+", llega a null.");
						throw new ValidationException("Falla el campo: "+param.getName()+", llega a null."); 
					}else{
						Integer valor = (params.isNull(param.getName())?null:params.getInt(param.getName()));
						if (valor == null){						
							pst.setNull(i++, Types.NULL);
						} else {
							pst.setInt(i++,	valor);
						}
						salidaObj.put(param.getName(), valor);
					}
				} else if (param.getType().equals(Constants.PARAM_TYPE_REAL)) {
					if(params.isNull(param.getName())&&param.getNotNull().equals(Constants.FIELD_TRUE)){
						i++;
						logger.error("Falla el campo: "+param.getName()+", llega a null.");
						throw new ValidationException("Falla el campo: "+param.getName()+", llega a null.");
					}else{
						Double valor = (params.isNull(param.getName())?null:params.getDouble(param.getName()));
						if (valor == null){						
							pst.setNull(i++, Types.NULL);
						} else {
							pst.setDouble(i++,	valor);
						}
						salidaObj.put(param.getName(), valor);
					}
				} else if (param.getType().equals(Constants.PARAM_TYPE_STRING)) {
					if(params.getString(param.getName()).isEmpty()&&param.getNotNull().equals(Constants.FIELD_TRUE)){
						i++;
						logger.error("Falla el campo: "+param.getName()+", llega a null.");
						throw new ValidationException("Falla el campo: "+param.getName()+", llega a null.");
					}else{
						String valor = (params.isNull(param.getName())?null:params.getString(param.getName()));
						if (valor == null){						
							pst.setNull(i++, Types.NULL);
						} else {
							pst.setString(i++,	valor);
						}
						salidaObj.put(param.getName(), valor);
					}
				} else if (param.getType().equals(Constants.PARAM_TYPE_EMAIL)) {
					if(params.getString(param.getName()).isEmpty()&&param.getNotNull().equals(Constants.FIELD_TRUE)){
						i++;
						logger.error("Falla el campo: "+param.getName()+", llega a null.");
						throw new ValidationException("Falla el campo: "+param.getName()+", llega a null.");
					}else{
						if (ValidationUtils.validateEmail( params.getString(param.getName()))){
							String valor = (params.isNull(param.getName())?null:params.getString(param.getName()));
							if (valor == null){						
								pst.setNull(i++, Types.NULL);
							} else {
								pst.setString(i++,	valor);
							}
							salidaObj.put(param.getName(), valor);
						}else{
							throw new ValidationException("El campo "+param.getName()+"Es incorrecto.");
						}
					}
				} else if (param.getType().equals(Constants.PARAM_TYPE_IDUSER)) {
					if(auth!= null && param.getNotNull().equals(Constants.FIELD_TRUE)){
						pst.setLong(i++,	auth.getIdUser());
					}else{
						throw new ValidationException("El campo "+param.getName()+"Es incorrecto.");
					}
				} else {
					i++;
					logger.error("Falla el campo: "+param.getName());
					throw new ValidationException("Falla el campo: "+param.getName());
				}

			}
			
			logger.debug(pst.toString());
			pst.executeUpdate();	
			
			salida.put(serviceObj.getQuery().getName(),salidaObj );
			
			conn.commit();
			pst.close();
		} else if (serviceObj.getQuery().getType()
				.equals(Constants.TYPE_INSERT)) {
			conn.open();
			PreparedStatement pst = conn.prepareStatement(serviceObj.getQuery()
					.getSrc());
			int i = 1;
			JSONObject salidaObj = new JSONObject();
			for (Param param : serviceObj.getQuery().getParams()) {
				if (param.getType().equals(Constants.PARAM_TYPE_BOOLEAN)) {
					if(params.isNull(param.getName())&&param.getNotNull().equals(Constants.FIELD_TRUE)){
						i++;
						logger.error("Falla el campo: "+param.getName()+", llega a null.");
						throw new ValidationException("Falla el campo: "+param.getName()+", llega a null.");
					}else{
						Boolean valor = (params.isNull(param.getName())?null:params.getBoolean(param.getName()));
						if (valor == null){						
							pst.setNull(i++, Types.NULL);
						} else {
							pst.setBoolean(i++,	valor);
						}
						salidaObj.put(param.getName(), valor);
					}
				} else if (param.getType().equals(Constants.PARAM_TYPE_INTEGER)) {
					if(params.isNull(param.getName())&&param.getNotNull().equals(Constants.FIELD_TRUE)){
						i++;
						logger.error("Falla el campo: "+param.getName()+", llega a null.");
						throw new ValidationException("Falla el campo: "+param.getName()+", llega a null."); 
					}else{
						Integer valor = (params.isNull(param.getName())?null:params.getInt(param.getName()));
						if (valor == null){						
							pst.setNull(i++, Types.NULL);
						} else {
							pst.setInt(i++,	valor);
						}
						salidaObj.put(param.getName(), valor);
					}
				} else if (param.getType().equals(Constants.PARAM_TYPE_REAL)) {
					if(params.isNull(param.getName())&&param.getNotNull().equals(Constants.FIELD_TRUE)){
						i++;
						logger.error("Falla el campo: "+param.getName()+", llega a null.");
						throw new ValidationException("Falla el campo: "+param.getName()+", llega a null.");
					}else{
						Double valor = (params.isNull(param.getName())?null:params.getDouble(param.getName()));
						if (valor == null){						
							pst.setNull(i++, Types.NULL);
						} else {
							pst.setDouble(i++,	valor);
						}
						salidaObj.put(param.getName(), valor);
					}
				} else if (param.getType().equals(Constants.PARAM_TYPE_STRING)) {
					if(params.getString(param.getName()).isEmpty()&&param.getNotNull().equals(Constants.FIELD_TRUE)){
						i++;
						logger.error("Falla el campo: "+param.getName()+", llega a null.");
						throw new ValidationException("Falla el campo: "+param.getName()+", llega a null.");
					}else{
						String valor = (params.isNull(param.getName())?null:params.getString(param.getName()));
						if (valor == null){						
							pst.setNull(i++, Types.NULL);
						} else {
							pst.setString(i++,	valor);
						}
						salidaObj.put(param.getName(), valor);
					}
				} else if (param.getType().equals(Constants.PARAM_TYPE_EMAIL)) {
					if(params.getString(param.getName()).isEmpty()&&param.getNotNull().equals(Constants.FIELD_TRUE)){
						i++;
						logger.error("Falla el campo: "+param.getName()+", llega a null.");
						throw new ValidationException("Falla el campo: "+param.getName()+", llega a null.");
					}else{
						if (ValidationUtils.validateEmail( params.getString(param.getName()))){
							String valor = (params.isNull(param.getName())?null:params.getString(param.getName()));
							if (valor == null){						
								pst.setNull(i++, Types.NULL);
							} else {
								pst.setString(i++,	valor);
							}
							salidaObj.put(param.getName(), valor);
						}else{
							throw new ValidationException("El campo "+param.getName()+"Es incorrecto.");
						}
					}
				} else if (param.getType().equals(Constants.PARAM_TYPE_IDUSER)) {
					if(auth!= null && param.getNotNull().equals(Constants.FIELD_TRUE)){
						pst.setLong(i++,	auth.getIdUser());
						salidaObj.put(param.getName(), auth.getIdUser());
					}else{
						throw new ValidationException("El campo "+param.getName()+"Es incorrecto.");
					}
				} else {
					i++;
					logger.error("Falla el campo: "+param.getName());
					throw new ValidationException("Falla el campo: "+param.getName());
				}
			}
			
			logger.debug(pst.toString());
			pst.executeUpdate();	
			
			ResultSet rs = pst.getGeneratedKeys();
	        if (rs.next()){
	        	salidaObj.put("id", rs.getLong(1));
	        }

	        salida.put(serviceObj.getQuery().getName(),salidaObj );
			
			conn.commit();
			pst.close();
		} else if (serviceObj.getQuery().getType()
				.equals(Constants.TYPE_DELETE)) {
			conn.open();
			PreparedStatement pst = conn.prepareStatement(serviceObj.getQuery()
					.getSrc());
			int i = 1;
			JSONObject salidaObj = new JSONObject();
			for (Param param : serviceObj.getQuery().getParams()) {
				if (param.getType().equals(Constants.PARAM_TYPE_BOOLEAN)) {
					if(params.getString(param.getName()).isEmpty()&&param.getNotNull().equals(Constants.FIELD_TRUE)){
						i++;
						logger.error("Falla el campo: "+param.getName()+", llega a null.");
						throw new ValidationException("Falla el campo: "+param.getName()+", llega a null.");
					}else{
						Boolean valor = (params.isNull(param.getName())?null:params.getBoolean(param.getName()));
						if (valor == null){						
							pst.setNull(i++, Types.NULL);
						} else {
							pst.setBoolean(i++,	valor);
						}
						salidaObj.put(param.getName(), valor);
					}
				} else if (param.getType().equals(Constants.PARAM_TYPE_INTEGER)) {
					if(params.isNull(param.getName())&&param.getNotNull().equals(Constants.FIELD_TRUE)){
						i++;
						logger.error("Falla el campo: "+param.getName()+", llega a null.");
						throw new ValidationException("Falla el campo: "+param.getName()+", llega a null."); 
					}else{
						Integer valor = (params.isNull(param.getName())?null:params.getInt(param.getName()));
						if (valor == null){						
							pst.setNull(i++, Types.NULL);
						} else {
							pst.setInt(i++,	valor);
						}
						salidaObj.put(param.getName(), valor);
					}
				} else if (param.getType().equals(Constants.PARAM_TYPE_REAL)) {
					if(params.isNull(param.getName())&&param.getNotNull().equals(Constants.FIELD_TRUE)){
						i++;
						logger.error("Falla el campo: "+param.getName()+", llega a null.");
						throw new ValidationException("Falla el campo: "+param.getName()+", llega a null.");
					}else{
						Double valor = (params.isNull(param.getName())?null:params.getDouble(param.getName()));
						if (valor == null){						
							pst.setNull(i++, Types.NULL);
						} else {
							pst.setDouble(i++,	valor);
						}
						salidaObj.put(param.getName(), valor);
					}
				} else if (param.getType().equals(Constants.PARAM_TYPE_STRING)) {
					if(params.getString(param.getName()).isEmpty()&&param.getNotNull().equals(Constants.FIELD_TRUE)){
						i++;
						logger.error("Falla el campo: "+param.getName()+", llega a null.");
						throw new ValidationException("Falla el campo: "+param.getName()+", llega a null.");
					}else{
						String valor = (params.isNull(param.getName())?null:params.getString(param.getName()));
						if (valor == null){						
							pst.setNull(i++, Types.NULL);
						} else {
							pst.setString(i++,	valor);
						}
						salidaObj.put(param.getName(), valor);
					} 
				} else if (param.getType().equals(Constants.PARAM_TYPE_EMAIL)) {
					if(params.getString(param.getName()).isEmpty()&&param.getNotNull().equals(Constants.FIELD_TRUE)){
						i++;
						logger.error("Falla el campo: "+param.getName()+", llega a null.");
						throw new ValidationException("Falla el campo: "+param.getName()+", llega a null.");
					}else{
						if (ValidationUtils.validateEmail( params.getString(param.getName()))){
							String valor = (params.isNull(param.getName())?null:params.getString(param.getName()));
							if (valor == null){						
								pst.setNull(i++, Types.NULL);
							} else {
								pst.setString(i++,	valor);
							}
							salidaObj.put(param.getName(), valor);
						}else{
							throw new ValidationException("El campo "+param.getName()+"Es incorrecto.");
						}
					}
				} else if (param.getType().equals(Constants.PARAM_TYPE_IDUSER)) {
					if(auth!= null && param.getNotNull().equals(Constants.FIELD_TRUE)){
						pst.setInt(i++,	params.getInt(param.getName()));
						salidaObj.put(param.getName(), auth.getIdUser());
					}else{
						throw new ValidationException("El campo "+param.getName()+"Es incorrecto.");
					}
				} else {
					i++;
					logger.error("Falla el campo: "+param.getName());
					throw new ValidationException("Falla el campo: "+param.getName());
				}
			}
			
			
			pst.executeUpdate();	
			
			salida.put(serviceObj.getQuery().getName(), salidaObj);
			
			conn.commit();
			pst.close();
		}
		return salida;
	}

}
