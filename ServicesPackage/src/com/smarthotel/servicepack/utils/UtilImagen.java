package com.smarthotel.servicepack.utils;

import java.awt.Graphics2D;
import java.awt.Image;
import java.awt.RenderingHints;
import java.awt.Toolkit;
import java.awt.geom.AffineTransform;
import java.awt.image.BufferedImage;
import java.io.BufferedOutputStream;
import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;

import javax.imageio.ImageIO;

import com.sun.image.codec.jpeg.ImageFormatException;
import com.sun.image.codec.jpeg.JPEGCodec;
import com.sun.image.codec.jpeg.JPEGEncodeParam;
import com.sun.image.codec.jpeg.JPEGImageEncoder;

public class UtilImagen {
	
	public static void transformarImagen(File imagen, int width, int height, int calidad) throws ImageFormatException, IOException {
		//esto deja la imagen original en un objeto Image 
	
		Image image = Toolkit.getDefaultToolkit().getImage(imagen.getAbsolutePath()); 
	
		// esto reduce la imagen a los valores de las variables width, height 
	
		BufferedImage tnsImg = new BufferedImage(width,height, BufferedImage.TYPE_INT_RGB); 
		Graphics2D graphics2D = tnsImg.createGraphics(); 
		graphics2D.setRenderingHint(RenderingHints.KEY_INTERPOLATION,RenderingHints.VALUE_INTERPOLATION_BILINEAR); 
		graphics2D.drawImage(image, 0, 0, width, height, null); 
	
		//esto guarda la imagen en un fichero jpg 
	
		BufferedOutputStream out = new BufferedOutputStream(new FileOutputStream(imagen)); 
		//creamos el "parseador" para guardar la imagen en formato JPG 
		JPEGImageEncoder encoder = JPEGCodec.createJPEGEncoder(out); 
		JPEGEncodeParam param = encoder.getDefaultJPEGEncodeParam(tnsImg); 
	
		//Asignamos la calidad con la que se va a guardar la imagen de 0-100 
		calidad = Math.max(0, Math.min(calidad, 100)); 
		param.setQuality((float)calidad / 100.0f, false); 
		encoder.setJPEGEncodeParam(param); 
		encoder.encode(tnsImg); 
		out.close(); 
	}
	
	public static void resize(InputStream input, OutputStream output, int width, int height) throws Exception {
	    BufferedImage src = ImageIO.read(input);
	    BufferedImage dest = new BufferedImage(width, height, BufferedImage.TYPE_INT_RGB);
	    Graphics2D g = dest.createGraphics();
	    AffineTransform at = AffineTransform.getScaleInstance((double)width / src.getWidth(), (double)height / src.getHeight());
	    g.drawRenderedImage(src, at);
	    ImageIO.write(dest, "JPG", output);
	    output.close();
	}
}
