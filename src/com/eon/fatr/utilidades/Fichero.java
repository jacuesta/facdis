package com.eon.fatr.utilidades;

import java.io.BufferedInputStream;
import java.io.BufferedOutputStream;
import java.io.BufferedReader;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.OutputStream;
import java.io.RandomAccessFile;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;
import java.util.Properties;
import java.util.StringTokenizer;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;

import com.eon.fatr.utilidades.Recurso;
import com.eon.fatr.utilidades.Util;

/**
 * Clase que realiza distintas acciones sobre ficheros de texto.
 */
public class Fichero {
	private static Log logger = LogFactory.getFactory().getInstance(Fichero.class);
	
	private static Properties properties;
	private static String longitudLectura;
	private static String tipoLectura;
	private static String separador;
	private static String caracterRellenoAlfanumerico;
	private static String caracterRellenoNumerico;
	private static String direccionRellenoAlfanumerico;
	private static String direccionRellenoNumerico;
	private static String valorDefectoAlfanumerico;
	private static String valorDefectoNumerico;
	public static String posicionRuptura;
	private static String numeroLineas;
	private static String longitudEscritura;
	private static String tipoEscritura;
	private static String separador_Esc;
	private static String caracterRellenoAlfanumerico_Esc;
	private static String caracterRellenoNumerico_Esc;
	private static String direccionRellenoAlfanumerico_Esc;
	private static String direccionRellenoNumerico_Esc;
	private static String valorDefectoAlfanumerico_Esc;
	private static String valorDefectoNumerico_Esc;
	private static String cargaCampoTicketEnergetico;
	private static RandomAccessFile miRAFile;
	private static FileOutputStream out;
	private static BufferedOutputStream bos;
	public static long posicion = 0;
	
	//Variables para tratar escritura con 3 tipos de dato
	private static String longitudEscritura_02;
	private static String tipoEscritura_02;
	private static String longitudEscritura_03;
	private static String tipoEscritura_03;
	
	//Variables para tratar escritura con 3 tipos de dato
	private static String longitudLectura_02;
	private static String tipoLectura_02;
	
	
	
	// Variables que controlan el FTP para la generacion de itinerarios de lectura
	public static String ip_FTP_LECT_Down ;
	public static String Usuario_FTP_LECT_Down ;
	public static String Password_FTP_LECT_Down ;
	public static String Directorio_FTP_LECT_Down ;
	
	public static String ip_FTP_LECT_Up ;
	public static String Usuario_FTP_LECT_Up ;
	public static String Password_FTP_LECT_Up ;
	public static String Directorio_FTP_LECT_Up ;
	
	public static String ip_FTP_ITI_Up ;
	public static String Usuario_FTP_ITI_Up ;
	public static String Password_FTP_ITI_Up ;
	public static String Directorio_FTP_ITI_Up ;
	
	/**
	 * Lee las propiedades de lectura de ficheros de un fichero de configuración.
	 * @param seccion la sección de las propiedades.
	 * @param nombreInterfaz el nombre del fichero de configuración.
	 * @return 0 si la lectura se ha hecho con éxito y otro número si se ha producido algún error.
	 * @throws AccesoDatosException si la ejecución de la consulta SQL produce un error de Oracle.
	 * @throws IOException si los ficheros de propiedades contienen algún error.
	 * @throws FileNotFoundException si no encuentra el fichero.
	 * @throws Exception si se produce cualquier otro error.
	 */
	public static int leerPropiedadesFTP(String nombreInterfaz) throws IOException, FileNotFoundException, Exception{
		properties = new Properties();	
		properties.load(Recurso.getInputStream(nombreInterfaz));
		
		ip_FTP_LECT_Down = properties.getProperty("ip_FTP_LECT_Down");
		Usuario_FTP_LECT_Down = properties.getProperty("Usuario_FTP_LECT_Down");
		Password_FTP_LECT_Down = properties.getProperty("Password_FTP_LECT_Down");
		Directorio_FTP_LECT_Down = properties.getProperty("Directorio_FTP_LECT_Down");

		ip_FTP_LECT_Up = properties.getProperty("ip_FTP_LECT_Up");
		Usuario_FTP_LECT_Up = properties.getProperty("Usuario_FTP_LECT_Up");
		Password_FTP_LECT_Up = properties.getProperty("Password_FTP_LECT_Up");
		Directorio_FTP_LECT_Up = properties.getProperty("Directorio_FTP_LECT_Up");

		ip_FTP_ITI_Up = properties.getProperty("ip_FTP_ITI_Up");
		Usuario_FTP_ITI_Up = properties.getProperty("Usuario_FTP_ITI_Up");
		Password_FTP_ITI_Up = properties.getProperty("Password_FTP_ITI_Up");
		Directorio_FTP_ITI_Up = properties.getProperty("Directorio_FTP_ITI_Up");
		
		return 0;
	}

	
	/**
	 * Lee las propiedades de lectura de ficheros de un fichero de configuración.
	 * @param seccion la sección de las propiedades.
	 * @param nombreInterfaz el nombre del fichero de configuración.
	 * @return 0 si la lectura se ha hecho con éxito y otro número si se ha producido algún error.
	 * @throws AccesoDatosException si la ejecución de la consulta SQL produce un error de Oracle.
	 * @throws IOException si los ficheros de propiedades contienen algún error.
	 * @throws FileNotFoundException si no encuentra el fichero.
	 * @throws Exception si se produce cualquier otro error.
	 */
	public static int leerPropiedadesLectura(String seccion) throws IOException, FileNotFoundException, Exception{
		
		
		longitudLectura = PropiedadesAplicacion.getInstance().getPropiedad("longitudCampos_R_" + seccion);
		tipoLectura = PropiedadesAplicacion.getInstance().getPropiedad("tipoCampos_R_" + seccion);
		separador = PropiedadesAplicacion.getInstance().getPropiedad("separador_R_" + seccion);
		caracterRellenoAlfanumerico = PropiedadesAplicacion.getInstance().getPropiedad("caracterRellenoAlfanumerico_R_" + seccion);
		caracterRellenoNumerico = PropiedadesAplicacion.getInstance().getPropiedad("caracterRellenoNumerico_R_" + seccion);
		direccionRellenoAlfanumerico = PropiedadesAplicacion.getInstance().getPropiedad("direccionRellenoAlfanumerico_R_" + seccion);
		direccionRellenoNumerico = PropiedadesAplicacion.getInstance().getPropiedad("direccionRellenoNumerico_R_" + seccion);
		valorDefectoAlfanumerico = PropiedadesAplicacion.getInstance().getPropiedad("valorDefectoAlfanumerico_R_" + seccion);
		valorDefectoNumerico = PropiedadesAplicacion.getInstance().getPropiedad("valorDefectoNumerico_R_" + seccion);
		posicionRuptura = PropiedadesAplicacion.getInstance().getPropiedad("posicionRuptura_E_" + seccion);
		numeroLineas = PropiedadesAplicacion.getInstance().getPropiedad("numeroLineas_E_" + seccion);
		
		int iTiposLinea = Integer.parseInt(PropiedadesAplicacion.getInstance().getPropiedad("tipos_campos_R_" + seccion));
		
		if(iTiposLinea == 2){
			longitudLectura_02 = PropiedadesAplicacion.getInstance().getPropiedad("longitudCampos_R_02_" + seccion);
			tipoLectura_02 = PropiedadesAplicacion.getInstance().getPropiedad("tipoCampos_R_02_" + seccion);
		}
		
		if(longitudLectura.equals("") || tipoLectura.equals("")){
			return 1;
		}
		else{
			if(separador.equals("") && caracterRellenoAlfanumerico.equals("") && caracterRellenoNumerico.equals("") && direccionRellenoAlfanumerico.equals("") && direccionRellenoNumerico.equals("")){
				return 1;
			}
			else{
				return 0;
			}
		}
	}

	/**
	 * Lee las propiedades de escritura de ficheros de un fichero de configuración.
	 * @param seccion la sección de las propiedades.
	 * @param nombreInterfaz el nombre del fichero de configuración.
	 * @return 0 si la lectura se ha hecho con éxito y otro número si se ha producido algún error.
	 * @throws AccesoDatosException si la ejecución de la consulta SQL produce un error de Oracle.
	 * @throws IOException si los ficheros de propiedades contienen algún error.
	 * @throws FileNotFoundException si no encuentra el fichero.
	 * @throws Exception si se produce cualquier otro error.
	 */
	public static int leerPropiedadesEscritura(String seccion) throws IOException, FileNotFoundException, Exception{
		
		
		longitudEscritura = PropiedadesAplicacion.getInstance().getPropiedad("longitudCampos_E_" + seccion);
		tipoEscritura = PropiedadesAplicacion.getInstance().getPropiedad("tipoCampos_E_" + seccion);
		separador_Esc = PropiedadesAplicacion.getInstance().getPropiedad("separador_E_" + seccion);
		caracterRellenoAlfanumerico_Esc = PropiedadesAplicacion.getInstance().getPropiedad("caracterRellenoAlfanumerico_E_" + seccion);
		caracterRellenoNumerico_Esc = PropiedadesAplicacion.getInstance().getPropiedad("caracterRellenoNumerico_E_" + seccion);
		direccionRellenoAlfanumerico_Esc = PropiedadesAplicacion.getInstance().getPropiedad("direccionRellenoAlfanumerico_E_" + seccion);
		direccionRellenoNumerico_Esc = PropiedadesAplicacion.getInstance().getPropiedad("direccionRellenoNumerico_E_" + seccion);
		valorDefectoAlfanumerico_Esc = PropiedadesAplicacion.getInstance().getPropiedad("valorDefectoAlfanumerico_E_" + seccion);
		valorDefectoNumerico_Esc = PropiedadesAplicacion.getInstance().getPropiedad("valorDefectoNumerico_E_" + seccion);
		//FATR-194 - INICIO
		cargaCampoTicketEnergetico = PropiedadesAplicacion.getInstance().getPropiedad("cargaCampoTicketEnergetico");
		
		if (cargaCampoTicketEnergetico.equals("N")) {
			longitudEscritura = longitudEscritura.substring(0, longitudEscritura.length() - 2);
			tipoEscritura = tipoEscritura.substring(0, tipoEscritura.length() - 2);
		}
		//FATR-194 - FIN
		
		if(longitudEscritura.equals("") || tipoEscritura.equals("")){
			return 1;
		}
		else{
			if(separador_Esc.equals("") && caracterRellenoAlfanumerico_Esc.equals("") && caracterRellenoNumerico_Esc.equals("") && direccionRellenoAlfanumerico_Esc.equals("") && direccionRellenoNumerico_Esc.equals("")){
				return 1;
			}
			else{
				return 0;
			}
		}
	}
	
	/**
	 * Carga en una lista las longitudes de los campos del fichero.
	 * @param modo lectura o escritura.
	 * @return la lista con las longitudes de los campos del fichero.
	 * @throws AccesoDatosException si la ejecución de la consulta SQL produce un error de Oracle.
	 * @throws IOException si los ficheros de propiedades contienen algún error.
	 * @throws Exception si se produce cualquier otro error.
	 */
	public static List cargarLongitudesCampos(String modo) throws IOException, Exception {
		
		List longitudCampos = new ArrayList();
		String cadena = "";
		
		if(modo.equals("LECTURA")){
			cadena =longitudLectura;
		}
		else{
			cadena = longitudEscritura;
		}
		StringTokenizer st = new StringTokenizer(cadena, ";");
		while(st.hasMoreElements()){
			longitudCampos.add(String.valueOf(st.nextToken()));
		}
		
		return longitudCampos;
	}
	
	/**
	 * Carga en una lista los tipos de los campos del fichero.
	 * @param modo lectura o escritura.
	 * @return la lista con los tipos de los campos del fichero.
	 * @throws AccesoDatosException si la ejecución de la consulta SQL produce un error de Oracle.
	 * @throws IOException si los ficheros de propiedades contienen algún error.
	 * @throws Exception si se produce cualquier otro error.
	 */
	public static List cargarTiposCampos(String modo) throws IOException, Exception{
		
		List tipoCampos = new ArrayList();
		String cadena = "";
		
		if(modo.equals("LECTURA")){
			cadena = tipoLectura;
		}
		else{
			cadena = tipoEscritura;
		}
		StringTokenizer st = new StringTokenizer(cadena, ";");
		while(st.hasMoreElements()){
			tipoCampos.add(String.valueOf(st.nextToken()));
		}
		
		return tipoCampos;
	}

	/**
	 * Abre un fichero.
	 * @param nombreInterfaz el nombre del fichero.
	 * @throws AccesoDatosException si la ejecución de la consulta SQL produce un error de Oracle.
	 * @throws IOException si los ficheros de propiedades contienen algún error.
	 * @throws FileNotFoundException si no encuentra el fichero.
	 * @throws Exception si se produce cualquier otro error.
	 */
	public static void abrirFichero(String nombreInterfaz) throws IOException, FileNotFoundException, Exception {
		miRAFile = new RandomAccessFile( nombreInterfaz, "r" );
	}
	
	/**
	 * Lee un fichero de texto.
	 * @param nombreInterfaz el nombre del fichero.
	 * @param listaLongitud la lista con las longitudes de los campos.
	 * @param listaTipo la lista con los tipos de campo.
	 * @return la lista con datos del fichero.
	 * @throws AccesoDatosException si la ejecución de la consulta SQL produce un error de Oracle.
	 * @throws IOException si los ficheros de propiedades contienen algún error.
	 * @throws Exception si se produce cualquier otro error.
	 */
	public static List leerFichero(String nombreInterfaz, List listaLongitud, List listaTipo) throws IOException, Exception {
		
		String line = "";
		String cadena = "";
		String cadena2 ="";
		String tipo = "";
		int longitud1 = 0;
		int longitud2 = 0;
		int longitud3 = 0;
		int cont = 0;
		
		FileInputStream in = new FileInputStream(nombreInterfaz);
		BufferedInputStream s = new BufferedInputStream(in);
		BufferedReader myInput = new BufferedReader(new InputStreamReader(s));
		
		List listaFichero = new ArrayList();
		while((line = myInput.readLine()) != null) {
			List listaFichero2 = new ArrayList();
			if (separador.equals("")){
				Iterator it4 = listaLongitud.iterator();
				Iterator it5 = listaTipo.iterator();
				longitud1 = 0;
				while(it4.hasNext()){
					cadena2 = (String)it4.next();
					longitud2 = Integer.parseInt(cadena2);
					longitud3 =longitud1 + longitud2;
					cadena = line.substring(longitud1, longitud3);
					tipo = (String)it5.next();
					
					if (tipo.equals("X")){
						if (caracterRellenoAlfanumerico.equals("")){
							cadena = cadena.trim();
						}
						else{
							if (direccionRellenoAlfanumerico.equals("L")){
								cadena = Util.eliminaCaracteresIzq(cadena, caracterRellenoAlfanumerico);
							}
							else{
								cadena = Util.eliminaCaracteresDrcha(cadena, caracterRellenoAlfanumerico);
							}
						}
						if(cadena.equals("")){
							cadena = valorDefectoAlfanumerico;
						}
					}
					else{
						if (caracterRellenoNumerico.equals("")){
							cadena = cadena.trim();
						}
						else{
							if (direccionRellenoNumerico.equals("L")){
								cadena = Util.eliminaCaracteresIzq(cadena, caracterRellenoNumerico);
							}
							else{
								cadena = Util.eliminaCaracteresDrcha(cadena, caracterRellenoNumerico);
							}
						}
						if(cadena.equals("")){
							cadena = valorDefectoNumerico;
						}
					}
					listaFichero2.add(cadena);
					longitud1 = longitud3;
				}
			}
			else{
				if(line.indexOf(separador + separador) != -1){
					String array[] = convertirStringAArray(line, listaLongitud.size());
					if(array.length != listaLongitud.size()){
						logger.error("El formato del fichero no es correcto. La orden procesada tiene un número de campos distinto al definido.");
					}
					else{
						for(int i=0; i<array.length; i++){
							//cadena = String.valueOf(st.nextToken());
							cadena = array[i];
							Iterator it6 = listaTipo.iterator();
							tipo = (String)it6.next();
							if (tipo.equals("X")){
								if (caracterRellenoAlfanumerico.equals("")){
									cadena = cadena.trim();
								}
								else{
									if (direccionRellenoAlfanumerico.equals("L")){
										cadena = Util.eliminaCaracteresIzq(cadena, caracterRellenoAlfanumerico);
									}
									else{
										cadena = Util.eliminaCaracteresDrcha(cadena, caracterRellenoAlfanumerico);
									}
								}
							}
							else{
								if (caracterRellenoNumerico.equals("")){
									cadena = cadena.trim();
								}
								else{
									if (direccionRellenoNumerico.equals("L")){
										cadena = Util.eliminaCaracteresIzq(cadena, caracterRellenoNumerico);
									}
									else{
										cadena = Util.eliminaCaracteresDrcha(cadena, caracterRellenoNumerico);
									}
								}
							}
							listaFichero2.add(cont, cadena);
							cont++;
						}
					}
				}
				else{
					if(line.indexOf(separador) == -1){
						logger.info("El separador del fichero es distinto al parametrizado en el fichero properties.");
					}
					else{
						StringTokenizer st = new StringTokenizer(line, separador);
						if(st.countTokens() != listaLongitud.size()){
							logger.error("El formato del fichero no es correcto. La orden procesada tiene un número de campos distinto al definido.");
						}
						else{
							while(st.hasMoreElements()){
								cadena = String.valueOf(st.nextToken());
								Iterator it6 = listaTipo.iterator();
								tipo = (String)it6.next();
								if (tipo.equals("X")){
									if (caracterRellenoAlfanumerico.equals("")){
										cadena = cadena.trim();
									}
									else{
										if (direccionRellenoAlfanumerico.equals("L")){
											cadena = Util.eliminaCaracteresIzq(cadena, caracterRellenoAlfanumerico);
										}
										else{
											cadena = Util.eliminaCaracteresDrcha(cadena, caracterRellenoAlfanumerico);
										}
									}
								}
								else{
									if (caracterRellenoNumerico.equals("")){
										cadena = cadena.trim();
									}
									else{
										if (direccionRellenoNumerico.equals("L")){
											cadena = Util.eliminaCaracteresIzq(cadena, caracterRellenoNumerico);
										}
										else{
											cadena = Util.eliminaCaracteresDrcha(cadena, caracterRellenoNumerico);
										}
									}
								}
								listaFichero2.add(cont, cadena);
								cont++;
							}
						}
					}
				}
			}
			listaFichero.add(listaFichero2);
			cont = 0;
		}
		
		myInput.close();
		s.close();
		in.close();
		
		return listaFichero;
	}
	
	/**
	 * Crea un fichero.
	 * @param nombreInterfaz el nombre del fichero.
	 * @throws AccesoDatosException si la ejecución de la consulta SQL produce un error de Oracle.
	 * @throws IOException si los ficheros de propiedades contienen algún error.
	 * @throws FileNotFoundException si no encuentra el fichero.
	 * @throws Exception si se produce cualquier otro error.
	 */
	public static void crearFichero(String nombreInterfaz) throws IOException, FileNotFoundException, Exception {
		out = new FileOutputStream(nombreInterfaz);
		bos = new BufferedOutputStream(out);
	}
	
	/**
	 * Escribe un fichero.
	 * @param listaFichero la lista con los datos a escribir.
	 * @param listaLongitud la lista con las longitudes de los campos.
	 * @param listaTipo a la lista con los tipos de los campos.
	 * @throws AccesoDatosException si la ejecución de la consulta SQL produce un error de Oracle.
	 * @throws IOException si los ficheros de propiedades contienen algún error.
	 * @throws Exception si se produce cualquier otro error.
	 */
	public static void escribirFichero(List listaFichero, List listaLongitud, List listaTipo) throws IOException, Exception{
		
		String cadena = "";
		String lon = "";
		String tipo = "";
		String caracter = "";
		String direccion = "";
		int numeroCaracteres = 0;
		
		Iterator it = listaFichero.iterator();
		while (it.hasNext()) {
			List listaFichero3 = (List) it.next();
			Iterator it2 = listaFichero3.iterator();
			Iterator it4 = listaLongitud.iterator();
			Iterator it5 = listaTipo.iterator();
			while (it2.hasNext()) {
				cadena = (String)it2.next();
				tipo = (String)it5.next();
				if (!separador_Esc.equals("")){
					if (cadena == null){
						if (tipo.equals("X")){
							cadena = valorDefectoAlfanumerico_Esc + separador_Esc;
						}
						else{
							cadena = valorDefectoNumerico_Esc + separador_Esc;
						}
					}
					else{
						cadena = cadena + separador_Esc;
					}
					bos.write(cadena.getBytes(), 0, cadena.getBytes().length);
					bos.flush();
				}
				else{
					lon = (String)it4.next();
					if (cadena == null){
						if (tipo.equals("X")){
							cadena = valorDefectoAlfanumerico_Esc;
						}
						else{
							cadena = valorDefectoNumerico_Esc;
						}
					}
					if (cadena.length() < Integer.parseInt(lon)){
						numeroCaracteres = Integer.parseInt(lon) - cadena.length();
						if (tipo.equals("X")){
							caracter = caracterRellenoAlfanumerico_Esc;
							direccion = direccionRellenoAlfanumerico_Esc;
						}
						else{
							caracter = caracterRellenoNumerico_Esc;
							direccion = direccionRellenoNumerico_Esc;
						}
						cadena = Util.rellenaCaracteres(cadena, numeroCaracteres, caracter, direccion);
					}
					else if (cadena.length() > Integer.parseInt(lon)){
						cadena = cadena.substring(0, Integer.parseInt(lon));
					}
					bos.write(cadena.getBytes(), 0, cadena.getBytes().length);
					bos.flush();
				}
			}
			cadena = "\r\n";
			bos.write(cadena.getBytes(), 0, cadena.getBytes().length);
		}
	}
	
	public static void escribirFicheroPorRegistros(List listaFichero) throws IOException, Exception{
		
		String cadena = "";
		String lon = "";
		String tipo = "";
		String caracter = "";
		String direccion = "";
		String registro = "";
		int numeroCaracteres = 0;
		List listaLongitud = new ArrayList();
		List listaTipo = new ArrayList();
		List listaLongitud_02 = new ArrayList();
		List listaTipo_02 = new ArrayList();
		List listaLongitud_03 = new ArrayList();
		List listaTipo_03 = new ArrayList();
		
		Iterator it = listaFichero.iterator();
		StringTokenizer st = new StringTokenizer(longitudEscritura, ";");
		while(st.hasMoreElements()){
			listaLongitud.add(String.valueOf(st.nextToken()));
		}
		st = new StringTokenizer(tipoEscritura, ";");
		while(st.hasMoreElements()){
			listaTipo.add(String.valueOf(st.nextToken()));
		}
		st = new StringTokenizer(longitudEscritura_02, ";");
		while(st.hasMoreElements()){
			listaLongitud_02.add(String.valueOf(st.nextToken()));
		}
		st = new StringTokenizer(tipoEscritura_02, ";");
		while(st.hasMoreElements()){
			listaTipo_02.add(String.valueOf(st.nextToken()));
		}
		st = new StringTokenizer(longitudEscritura_03, ";");
		while(st.hasMoreElements()){
			listaLongitud_03.add(String.valueOf(st.nextToken()));
		}
		st = new StringTokenizer(tipoEscritura_03, ";");
		while(st.hasMoreElements()){
			listaTipo_03.add(String.valueOf(st.nextToken()));
		}
		
		while (it.hasNext()) {
			List listaFichero3 = (List) it.next();
			Iterator it2 = listaFichero3.iterator();
			
			//El segundo elemento indica el código del registro
			registro = (String) listaFichero3.get(1);
			//Dependiendo del código del registro, se recuperan unas longitudes y tipos u otros.
			Iterator it4 = null;
			Iterator it5 = null;
			if(registro.equals("01")){
				it4 = listaLongitud.iterator();
				it5 = listaTipo.iterator();
			} else if(registro.equals("02")){
				it4 = listaLongitud_02.iterator();
				it5 = listaTipo_02.iterator();				
			} else if(registro.equals("03")){
				it4 = listaLongitud_03.iterator();
				it5 = listaTipo_03.iterator();
			}
			
			while (it2.hasNext()) {
				cadena = (String)it2.next();
				// LNC-150 replazar los saltos de linea y retorno de carro por un espacio en blanco				
				if (cadena != null){
					 if (cadena.indexOf("\n") != -1){								 
						cadena = cadena.replaceAll("\r\n"," ");
					 }
				}
				tipo = (String)it5.next();
				if (!separador_Esc.equals("")){
					if (cadena == null){
						if (tipo.equals("X")){
							cadena = valorDefectoAlfanumerico_Esc + separador_Esc;
						}
						else{
							cadena = valorDefectoNumerico_Esc + separador_Esc;
						}
					}
					else{
						cadena = cadena + separador_Esc;
					}
					bos.write(cadena.getBytes(), 0, cadena.getBytes().length);
					bos.flush();
				}
				else{
					lon = (String)it4.next();
					if (cadena == null){
						if (tipo.equals("X")){
							cadena = valorDefectoAlfanumerico_Esc;
						}
						else{
							cadena = valorDefectoNumerico_Esc;
						}
					}
					if (cadena.length() < Integer.parseInt(lon)){
						numeroCaracteres = Integer.parseInt(lon) - cadena.length();
						if (tipo.equals("X")){
							caracter = caracterRellenoAlfanumerico_Esc;
							direccion = direccionRellenoAlfanumerico_Esc;
						}
						else{
							caracter = caracterRellenoNumerico_Esc;
							direccion = direccionRellenoNumerico_Esc;
						}
						cadena = Util.rellenaCaracteres(cadena, numeroCaracteres, caracter, direccion);
					}
					else if (cadena.length() > Integer.parseInt(lon)){
						cadena = cadena.substring(0, Integer.parseInt(lon));
					}
					bos.write(cadena.getBytes(), 0, cadena.getBytes().length);
					bos.flush();
				}
			}
			cadena = "\r\n";
			bos.write(cadena.getBytes(), 0, cadena.getBytes().length);
		}
	}
		
	/**
	 * Cierra un fichero.
	 * @throws AccesoDatosException si la ejecución de la consulta SQL produce un error de Oracle.
	 * @throws IOException si los ficheros de propiedades contienen algún error.
	 * @throws Exception si se produce cualquier otro error.
	 */
	public static void cerrarFichero() throws IOException, Exception {
		bos.close();
		out.close();
		bos = null;
		out = null;
	}
	
	/**
	 * Cierra un fichero que ha sido leído por paquetes.
	 * @throws AccesoDatosException si la ejecución de la consulta SQL produce un error de Oracle.
	 * @throws IOException si los ficheros de propiedades contienen algún error.
	 * @throws Exception si se produce cualquier otro error.
	 */
	public static void cerrarFicheroPaquetes() throws IOException, Exception {
		miRAFile.close();
	}

	/**
	 * Lee un fichero por paquetes.
	 * @param listaLongitud la lista con las longitudes de los campos.
	 * @param listaTipo la lista con los tipos de los campos.
	 * @return la lista con los datos leídos del fichero.
	 * @throws AccesoDatosException si la ejecución de la consulta SQL produce un error de Oracle.
	 * @throws IOException si los ficheros de propiedades contienen algún error.
	 * @throws Exception si se produce cualquier otro error.
	 */
	public static List leerFicheroPaquetes(List listaLongitud, List listaTipo) throws IOException, Exception {
		String line = "";
		String cadena = "";
		String cadena2 ="";
		String tipo = "";
		int longitud1 = 0;
		int longitud2 = 0;
		int longitud3 = 0;
		int cont = 0;
		int contador = 0;
		String campoRuptura = "";
 
		List listaFichero = new ArrayList();
		miRAFile.seek(posicion);		
		uno: while((line = miRAFile.readLine()) != null) {
			List listaFichero2 = new ArrayList();
			if (separador.equals("")){
				Iterator it4 = listaLongitud.iterator();
				Iterator it5 = listaTipo.iterator();
				longitud1 = 0;
				while(it4.hasNext()){
					cadena2 = (String)it4.next();
					longitud2 = Integer.parseInt(cadena2);
					longitud3 =longitud1 + longitud2;
					cadena = line.substring(longitud1, longitud3);
					tipo = (String)it5.next();
					if (tipo.equals("X")){
						if (caracterRellenoAlfanumerico.equals("")){
							cadena = cadena.trim();
						}
						else{
							if (direccionRellenoAlfanumerico.equals("L")){
								cadena = Util.eliminaCaracteresIzq(cadena, caracterRellenoAlfanumerico);
							}
							else{
								cadena = Util.eliminaCaracteresDrcha(cadena, caracterRellenoAlfanumerico);
							}
						}
						if(cadena.equals("")){
							cadena = valorDefectoAlfanumerico;
						}
					}
					else{
						if (caracterRellenoNumerico.equals("")){
							cadena = cadena.trim();
						}
						else{
							if (direccionRellenoNumerico.equals("L")){
								cadena = Util.eliminaCaracteresIzq(cadena, caracterRellenoNumerico);
							}
							else{
								cadena = Util.eliminaCaracteresDrcha(cadena, caracterRellenoNumerico);
							}
						}
						if(cadena.equals("")){
							cadena = valorDefectoNumerico;
						}
					}
					listaFichero2.add(cadena);
					if (!posicionRuptura.equals("")){
						if(longitud1 == Integer.parseInt(posicionRuptura)){
							if((!campoRuptura.equals(cadena)) && (!campoRuptura.equals(""))){
								break uno;
							}
							else{							
								campoRuptura = cadena;
							}
						}
					}
					else{
						if(contador > Integer.parseInt(numeroLineas)){
							break uno;
						}
					}
					longitud1 = longitud3;
				}
			}
			else{
				if(line.indexOf(separador + separador) != -1){
					String array[] = convertirStringAArray(line, listaLongitud.size());
					if(array.length != listaLongitud.size()){
						logger.error("El formato del fichero no es correcto. La orden procesada tiene un número de campos distinto al definido.");
					}
					else{
						for(int i=0; i<array.length; i++){
							cadena = array[i];
							Iterator it6 = listaTipo.iterator();
							tipo = (String)it6.next();
							if (tipo.equals("X")){
								if (caracterRellenoAlfanumerico.equals("")){
									cadena = cadena.trim();
								}
								else{
									if (direccionRellenoAlfanumerico.equals("L")){
										cadena = Util.eliminaCaracteresIzq(cadena, caracterRellenoAlfanumerico);
									}
									else{
										cadena = Util.eliminaCaracteresDrcha(cadena, caracterRellenoAlfanumerico);
									}
								}
							}
							else{
								if (caracterRellenoNumerico.equals("")){
									cadena = cadena.trim();
								}
								else{
									if (direccionRellenoNumerico.equals("L")){
										cadena = Util.eliminaCaracteresIzq(cadena, caracterRellenoNumerico);
									}
									else{
										cadena = Util.eliminaCaracteresDrcha(cadena, caracterRellenoNumerico);
									}
								}
							}
							listaFichero2.add(cont, cadena);
							if (!posicionRuptura.equals("")){
								if(longitud1 == Integer.parseInt(posicionRuptura)){
									if((!campoRuptura.equals(cadena)) && (!campoRuptura.equals(""))){
										break uno;
									}
									else{							
										campoRuptura = cadena;
									}
								}
							}
							else{
								if(contador > Integer.parseInt(numeroLineas)){
									break uno;
								}
							}
							cont++;
						}
					}
				}
				else{
					StringTokenizer st = new StringTokenizer(line, separador);
					if(st.countTokens() != listaLongitud.size()){
						logger.error("El formato del fichero no es correcto. La orden procesada tiene un número de campos distinto al definido.");
					}
					else{
						while(st.hasMoreElements()){
							cadena = String.valueOf(st.nextToken());
							Iterator it6 = listaTipo.iterator();
							tipo = (String)it6.next();
							if (tipo.equals("X")){
								if (caracterRellenoAlfanumerico.equals("")){
									cadena = cadena.trim();
								}
								else{
									if (direccionRellenoAlfanumerico.equals("L")){
										cadena = Util.eliminaCaracteresIzq(cadena, caracterRellenoAlfanumerico);
									}
									else{
										cadena = Util.eliminaCaracteresDrcha(cadena, caracterRellenoAlfanumerico);
									}
								}
							}
							else{
								if (caracterRellenoNumerico.equals("")){
									cadena = cadena.trim();
								}
								else{
									if (direccionRellenoNumerico.equals("L")){
										cadena = Util.eliminaCaracteresIzq(cadena, caracterRellenoNumerico);
									}
									else{
										cadena = Util.eliminaCaracteresDrcha(cadena, caracterRellenoNumerico);
									}
								}
							}
							listaFichero2.add(cont, cadena);
							if (!posicionRuptura.equals("")){
								if(longitud1 == Integer.parseInt(posicionRuptura)){
									if((!campoRuptura.equals(cadena)) && (!campoRuptura.equals(""))){
										break uno;
									}
									else{							
										campoRuptura = cadena;
									}
								}
							}
							else{
								if(contador > Integer.parseInt(numeroLineas)){
									break uno;
								}
							}
							cont++;
						}
					}
				}
			}
			listaFichero.add(listaFichero2);
			cont = 0;
			contador++;
			posicion = miRAFile.getFilePointer();
		}
		
		return listaFichero;
	}
	
	public static String[] convertirStringAArray(String cadena, int numeroCampos){
		String array[] = new String[numeroCampos];
		String cadena1 = "";
		int posicionIni = 0;
		int posicionFin = 0;
		int cont = 0;
		
		while(cadena.indexOf("|") != -1){
			posicionFin = cadena.indexOf("|");
			cadena1 = cadena.substring(posicionIni, posicionFin);
			cadena = cadena.substring(posicionFin+1, cadena.length());
			array[cont] = cadena1;
			cont++;
		}
		return array;
	}
	
	public static void copiarFichero(String fichero, String directorio){
		InputStream in = null;
        OutputStream out = null;
		try{
			File ficOrigen = new File(fichero);
			if (!ficOrigen.exists()) {
				logger.error("EL FICHERO " + fichero + " NO EXISTE.");
			} else {
						
				File ficDestino = new File(directorio +  ficOrigen.getName());
				in = new FileInputStream(ficOrigen);
		        out = new FileOutputStream(ficDestino);
		    
		        // Transfer bytes from in to out
		        byte[] buf = new byte[1024];
		        int len;
		        while ((len = in.read(buf)) > 0) {
		            out.write(buf, 0, len);
		        }
		        in.close();
		        out.close();

			}
		} catch(FileNotFoundException fnfe){
			logger.error("No se ha podido copiar el fichero " + fichero + " en el directorio destino " + directorio + ". ERROR: " + fnfe.toString());
		} catch(IOException ioe){
			logger.error("No se ha podido copiar el fichero " + fichero + " en el directorio destino " + directorio + ". ERROR: " + ioe.toString());
		}
		finally{
			try{
				if(in != null) in.close();
				if(out != null) out.close();
			}catch(IOException ioe){
				logger.error("ERROR AL CERRAR RECURSOS: " +  ioe.getMessage());
			}
			
		}
	}
	
	
	
	
}

