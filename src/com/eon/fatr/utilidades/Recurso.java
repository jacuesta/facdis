/*
$Author: cgonzalez $
$Date: 2013/07/05 10:56:48 $
$Revision: 1.1 $
$State: Exp $
*/
package com.eon.fatr.utilidades;

import java.io.InputStream;

/**
 *Clase de utilidad para poder leer un fichero visible por el ClassLoader.
 *
 *Ejemplo de uso:
 *	
 *	InputStream is = Recurso.getInputStream("/config/cics.properties");
 *	<br>
 *	Con la sentencia anterior obtenemos un InputStream a un fichero de
 *  propiedades, cics.properties, que se encuentra situado en el paquete 
 *  config.
 *  <br>
 *  <br>
 *  No olvidar poner la barra / inicial, ya que ésta apunta al paquete
 *  por defecto, en caso contrario, buscará el fichero en el paquete
 *  com.endesa.util.config
 */
public class Recurso {

    /**
     * Método que dado un nombre de fichero, realiza una búsqueda en el 
     * classpath y devuelve un InputStream al mismo.
     * @param entrada  Apuntador al fichero a buscar.
     * @exception IOException si no existe el fichero apuntado por entrada.
     */
    public static InputStream getInputStream(String entrada)
        throws java.io.IOException {
        return Recurso.class.getResourceAsStream(entrada);
    }

}
