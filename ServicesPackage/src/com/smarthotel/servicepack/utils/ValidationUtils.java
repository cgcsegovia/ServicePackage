package com.smarthotel.servicepack.utils;

import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class ValidationUtils {
	 private static final String PATTERN_EMAIL = "^[_A-Za-zñÑ0-9-\\+]+(\\.[_A-Za-zñÑ0-9-]+)*@"
	            + "[A-Za-zñÑ0-9-]+(\\.[A-Za-zñÑ0-9]+)*(\\.[A-Za-zñÑ]{2,})$";
	 
	    /**
	     * Validate given email with regular expression.
	     * 
	     * @param email
	     *            email for validation
	     * @return true valid email, otherwise false
	     */
	    public static boolean validateEmail(String email) {
	 
	        // Compiles the given regular expression into a pattern.
	        Pattern pattern = Pattern.compile(PATTERN_EMAIL);
	 
	        // Match the given input against this pattern
	        Matcher matcher = pattern.matcher(email);
	        return matcher.matches();
	 
	    }
}
