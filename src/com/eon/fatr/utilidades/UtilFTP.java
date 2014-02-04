package com.eon.fatr.utilidades;

import java.io.File;
import java.util.ArrayList;
import java.util.List;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;

import com.enterprisedt.net.ftp.FTPFile;
import com.enterprisedt.net.ftp.FTPTransferType;
import com.enterprisedt.net.ftp.Protocol;
import com.enterprisedt.net.ftp.SecureFileTransferClient;

public class UtilFTP {
    
	private static Log logger = LogFactory.getFactory().getInstance(UtilFTP.class);
	private static SecureFileTransferClient  ftp = new SecureFileTransferClient();
	
	public UtilFTP() {
	}
    
	public UtilFTP(String conexionSegura, String ftpTransferType) throws Exception {
		
		if (conexionSegura.equals("S"))
			ftp.setProtocol(Protocol.SFTP);	
		else	
			ftp.setProtocol(Protocol.FTP);
		
		FTPTransferType fTPTransferType = FTPTransferType.BINARY;
		if (ftpTransferType.equals("A")){
			fTPTransferType = FTPTransferType.ASCII;
		}
		
		ftp.setContentType(fTPTransferType);

	}
	
	public UtilFTP(String ipFTP, String Usuario_FTP, String Password_FTP, String conexionSegura, String ftpTransferType) throws Exception {
		
		ftp.setRemoteHost(ipFTP);
		ftp.setUserName(Usuario_FTP);
		ftp.setPassword(Password_FTP);
		
		if (conexionSegura.equals("S"))
			ftp.setProtocol(Protocol.SFTP);	
		else	
			ftp.setProtocol(Protocol.FTP);
		
		FTPTransferType fTPTransferType = FTPTransferType.BINARY;
		if (ftpTransferType.equals("A")){
			fTPTransferType = FTPTransferType.ASCII;
		}
		
		ftp.setContentType(fTPTransferType);

	}
	
	//SE LLAMA DESDE OTRA CLASE: Batch_FACDIS_EnvioCSA
	public boolean enviarFTP(String ficheroOrigen, String directorioDestino, String ficheroDestino) {

		boolean bCtrl = false;
		
		try {
			ftp.connect();
			
			logger.debug("FICHERO ORIGEN ---------- ************* " + ficheroOrigen);
			logger.debug("DIRECTORIO DESTINO ---------- ************* " + directorioDestino);
			logger.debug("FICHERO DESTINO ---------- ************* " + ficheroDestino);
			
			File fichDest= new File(ficheroDestino);
			ficheroDestino=fichDest.getName();
			
			bCtrl = subirFicheroFTP(ficheroOrigen, directorioDestino, ficheroDestino);
			
		}
		catch (Exception e) {
			logger.error("Se ha producido un error en el envío por FTP:" + e);
		} finally{
			if(ftp.isConnected())
				try {
					ftp.disconnect();
				} catch (Exception e1) {
					logger.error("Error en la desconexión: " + e1.getMessage());
				}
		}
		return bCtrl;
	}
	
	//SE LLAMA DESDE OTRA CLASE: Batch_FACDIS_EnvioCSA
	public List<String> recuperarFTP(String fichero, String rutaFichero, String DirectorioFTP) {

		List<String> listaFicheros=new ArrayList<String>();
		
		try {
			
			ftp.connect();
			
			listaFicheros = recuperarFicheroFTP(fichero, rutaFichero, DirectorioFTP);
			
		}
		catch (Exception e) {
			logger.error("Se ha producido un error en la recuperación FTP de ficheros " + fichero + ":" + e);
		} finally{
			if(ftp.isConnected())
				try {
					ftp.disconnect();
				} catch (Exception e1) {
					logger.error("Error en la desconexión: " + e1.getMessage());
				}
		}
		
		return listaFicheros;
	}
	
	//SE LLAMA DESDE OTRA CLASE: Batch_FACDIS_CargaFacturas
	public boolean recuperarDirectorioFTP(String directorio_ftp, String directorio_local) {
		
		//Se lista el Directorio
		//Se bajan los ficheros del directorio
		//Se borran los ficheros bajados
		boolean bCorrecto = false;
		FTPFile[] directorio = null;
		String nombreLocalFichero = "";
		
		try {
			// connect
			ftp.connect();
			
			//Cambio al directorio
			if (!directorio_ftp.equals(""))
				ftp.changeDirectory(directorio_ftp);
			
			//Listado directorios
			directorio=ftp.directoryList();
			
			//Recorremos array
			for (int i = 0; i <= directorio.length-1; i++) {
				if (!directorio[i].getName().equals(".") && !directorio[i].getName().equals("..")) {
					
					nombreLocalFichero = directorio_local + File.separator + directorio[i].getName();
					
					//Bajamos archivo
					ftp.downloadFile(nombreLocalFichero, directorio[i].getName());
					
					//Una vez copiado se borra
					ftp.deleteFile(directorio[i].getName());
						
				}
			}
			
			//Descarga correcta
			bCorrecto = true;
			
		} catch (Exception e1) {
			logger.error("Error en la descarga: " + e1.getMessage() + ". Fichero: " + nombreLocalFichero);
		} finally {
			if(ftp.isConnected())
				try {
					ftp.disconnect();
				} catch (Exception e1) {
					logger.error("Error en la desconexión: " + e1.getMessage());
				}	
		}
		
		return bCorrecto;
	}
	
	private static boolean subirFicheroFTP(String pathFicheroOrigen, String directorioDestino, String ficheroDestino ) {
		
		logger.debug("Enviar por FTP:");
		boolean bEnvioFTP = false;
		
		try {
			//vamos al directorio del FTP
			if(!directorioDestino.equals(""))
			{
				// Se va al directorio a recuperar los ficheros que llamen como 'fichero'.
				
				if (!ftp.existsDirectory(directorioDestino)) {
					ftp.createDirectory(directorioDestino);
				}
				ftp.changeDirectory(directorioDestino);
			
				//Se recupera el fichero
				File fichDestFTP= new File(ficheroDestino);
				ficheroDestino=fichDestFTP.getName();
				
				File pathFichOrigFTP= new File(pathFicheroOrigen);
				
				pathFicheroOrigen=pathFichOrigFTP.getPath();
				
				logger.info("PathFICHEROORIGEN-----> " + pathFicheroOrigen );
				logger.info("FICHERODESTINO ----> " + ficheroDestino );	
				
				ftp.uploadFile(pathFicheroOrigen, ficheroDestino);
				
	        }
			bEnvioFTP = true;
		} 
		catch (Exception e) {
			logger.error("Error en la subida del fichero: " + e.getMessage());
		}
		return bEnvioFTP;
	}
	
	private static List<String> recuperarFicheroFTP(String fichero, String directorioDestino, String directorioFTP ) {
	
		logger.debug("Recuperar FTP:");
		
		List<String> listaFicheros = new ArrayList<String>();
		
		try {
			//vamos al directorio del FTP
			if(!directorioFTP.equals("")) {
				
				// Se va al directorio a recuperar los ficheros que llamen como 'fichero'.
				ftp.changeDirectory(directorioFTP);
				
				String[] ficheros = ftp.directoryNameList();
				
				// Se recorre la lista de ficheros para recuperar los que se llamen igual
	        	for (int i = 0; i < ficheros.length ; i++) {
	        		
	        		String f = ficheros[i];
	        		
	        		if(f.indexOf(fichero) != -1){
	        			
	        			//Se recupera el fichero
	        			ftp.downloadFile(directorioDestino + File.separator + f, f);
	        			
	        			//Se añade el fichero a la lista
	        			listaFicheros.add(directorioDestino + File.separator + f);
	        			
	        			ftp.deleteFile(f);
	        		}
				}
			}
		} 
		catch (Exception e) {
			logger.error("Error en la recuperación del fichero: " + e.getMessage());
		}
	
		return listaFicheros;
	}
	
	public static boolean abrirConnFTP(
			String ipFTP, 
			String Usuario_FTP, 
			String Password_FTP) {
		boolean bCtrl = false;
		
		try {
			
			// set up client    
			ftp.setRemoteHost(ipFTP);
			ftp.setUserName(Usuario_FTP);
			ftp.setPassword(Password_FTP);
			
			ftp.connect();
			//ftp.login(Usuario_FTP, Password_FTP);

			// set up passive ASCII transfers
			//Revisar!!!!!!!!!!!
			//ftp.setConnectMode(fTPConnectMode);
			
			logger.debug("HA REALIZADO LA CONEXIÓN");
			bCtrl = true;
		} 
		catch (Exception e) {
			logger.error("Error al abrir la conexion FTP: " + e.getMessage());
		}

		return bCtrl;		
	}

	public static boolean cerrarConnFTP() {
		boolean bCtrl = false;
		try {
			// Shut down client                
			ftp.finalize();
			bCtrl = true;
		} 
		catch (Exception e) {
			logger.error("Error en la subida del fichero: " + e.getMessage());
			try{
				if(ftp.isConnected()) 
					ftp.finalize();
			}
			catch(Exception ftpExc){
				logger.error("Error al cerrar la conexion ftp: " + ftpExc.getMessage());
			}
		}
		return bCtrl;		
	}
	
	public void setTimeout(int timeout) throws Exception{
		ftp.setTimeout(timeout);
	}
	
}