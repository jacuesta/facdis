package com.eon.firmaDigital.gestion.general.utils;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.File;
import java.io.FileOutputStream;
import java.io.FileReader;
import java.io.IOException;
//import java.io.InputStream;
//import java.io.InputStreamReader;
import java.io.OutputStreamWriter;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;

//import com.mundivia.facturaduos.controller.exception.GeneralException;

public final class FileUtils {

	 private static final Log log = LogFactory.getLog(FileUtils.class);

	public static String read(String ficheroOriginal, String encoding)
	{
		String fileContent="";
		BufferedReader in = null;
		
		try
		{
			// abrir el archivo de entrada
			String UTF8Str;
			in = new BufferedReader(new FileReader(ficheroOriginal));
			
			// leer una línea
			while( (UTF8Str = in.readLine()) != null) {
				// leer con el encoding adecuado
				encoding=null;
				if ( encoding == null || "".equals(encoding) ) {
					UTF8Str = new String(UTF8Str.getBytes());
				} else {
					UTF8Str = new String(UTF8Str.getBytes(),encoding);
				}	
				
				// concatenar la línea
				fileContent = fileContent.concat(UTF8Str); 
			}
			in.close();
		} catch (Exception e) {
			log.equals("read-error en la generación:"+e.getMessage());
			System.out.println("error en la generación");
			e.printStackTrace();
		} finally {
			// cerrar el archivo de lectura
			try {
				if (null != in) in.close();
			} catch (IOException e) {
				System.out.println("error cerrando el archivo");		
			}
		}
		
		return fileContent;
	}
	
	public static void write(String ficheroSalida, String fileContent, String encoding)
	{
		BufferedWriter out = null;
		FileOutputStream fos = null;
		OutputStreamWriter osw = null;			
		try
		{
			// abrir el archivo de salida
			fos = new FileOutputStream(ficheroSalida);
			if ( encoding == null || "".equals(encoding) ) {
				osw = new OutputStreamWriter(fos);
			} else {
				osw = new OutputStreamWriter(fos,encoding);
			}
			out = new BufferedWriter(osw);
						
			// escribir el contenido
			out.write(fileContent);	
			out.close();
			fos.close();
			osw.close();
		} catch (Exception e) {
			log.info(e);
			System.out.println("error en la generación");			
		} finally {
			// cerrar el archivo de salida
			try {
				if (null != osw) osw.close();
				if (null != fos) fos.close();
				if (null != out) out.close();
			} catch (IOException e) {
				System.out.println("error cerrando el archivo");		
			}
		}
	}
	
	public static void createPath(String path) {
		// Crear la ruta (si no existe)
		File pathDir = new File(path);
		pathDir.mkdirs();		
	}
}