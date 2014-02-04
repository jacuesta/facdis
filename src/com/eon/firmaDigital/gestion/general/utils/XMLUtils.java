package com.eon.firmaDigital.gestion.general.utils;

import java.io.File;
import java.io.IOException;
import java.io.StringReader;
import java.io.StringWriter;
import java.io.Writer;
import java.util.List;

import javax.xml.XMLConstants;
import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;
import javax.xml.parsers.ParserConfigurationException;
import javax.xml.transform.Source;
import javax.xml.transform.dom.DOMSource;
import javax.xml.transform.stream.StreamSource;
import javax.xml.validation.Schema;
import javax.xml.validation.SchemaFactory;
import javax.xml.validation.Validator;

import org.apache.log4j.Logger;
import org.apache.xml.serialize.OutputFormat;
import org.apache.xml.serialize.XMLSerializer;
import org.w3c.dom.Document;
import org.xml.sax.InputSource;
import org.xml.sax.SAXException;

/**
 * Pretty-prints xml, supplied as a string.
 * <p/>
 * eg.
 * <code>
 * String formattedXml = new XmlFormatter().format("<tag><nested>hello</nested></tag>");
 * </code>
 */
public class XMLUtils {

	static Logger log = Logger.getLogger(XMLUtils.class);

	public static void format(String filename, String encoding) {
        String fileContent = FileUtils.read(filename,encoding);
        fileContent = XMLUtils.format(fileContent);
        FileUtils.write(filename,fileContent,encoding); 
    }

    public static String format(String unformattedXml) {
    	String fileContent="";
    	Writer out = null;
    	try {
            final Document document = parseXmlFile(unformattedXml);
            OutputFormat format = new OutputFormat(document);
//            if(unformattedXml.contains("ISO-8859-1")){
            	format.setEncoding("ISO-8859-1");
//            }
            format.setLineWidth(65);
//			Error 13/11/2013 - Cortar direcciones y cambiar caracteres
//          format.setIndenting(true);
            format.setIndenting(false);
            format.setIndent(2);
            out = new StringWriter();
            XMLSerializer serializer = new XMLSerializer(out, format);
            serializer.serialize(document);
            
            fileContent= out.toString();
            out.close();
        } catch (IOException e) {
            throw new RuntimeException(e);
        } finally {
			// cerrar el archivo de lectura
			try {
				if (null != out) out.close();
			} catch (IOException e) {
				log.error("error cerrando el archivo; " + e.getMessage());		
			}
		}
        return fileContent;
    }

    private static Document parseXmlFile(String in) {
        try {
            DocumentBuilderFactory dbf = DocumentBuilderFactory.newInstance();
            DocumentBuilder db = dbf.newDocumentBuilder();
            InputSource is = new InputSource(new StringReader(in));
            return db.parse(is);
        } catch (ParserConfigurationException e) {
            throw new RuntimeException(e);
        } catch (SAXException e) {
            throw new RuntimeException(e);
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }
    
 	/**
     * M�todo que retorna si es un documento v�lido o no
     *
     * @return boolean
     *
     **/
    public static boolean ValidaXml(String ficheroEntrada, List<String> ficherosXsd) {
    	return ValidaXml(ficheroEntrada,"",ficherosXsd);
    }
 	/**
     * M�todo que retorna si es un documento v�lido o no
     *
     * @return boolean
     *
     **/
    public static boolean ValidaXml(String ficheroEntrada, String ruta, List<String> ficherosXsd) {
    	try {    	
	        // xml - DOM
	        DocumentBuilderFactory builderFactory = DocumentBuilderFactory.newInstance();
	        builderFactory.setNamespaceAware(true);
	        DocumentBuilder psr = builderFactory.newDocumentBuilder();
	        Document doc = psr.parse(new File(ficheroEntrada));
	
	        // http://www.w3.org/2001/XMLSchema
	        SchemaFactory factory = SchemaFactory.newInstance(XMLConstants.W3C_XML_SCHEMA_NS_URI);
	
	        int i=0;
	        Source[] filesxsd = new Source[ficherosXsd.size()];
	        for(String ficheroxsd : ficherosXsd) {
	        	filesxsd[i++] = new StreamSource(new File(ruta+ficheroxsd));	        	
	        }
	        
	        Schema schema = factory.newSchema(filesxsd);
	        Validator valida = schema.newValidator();
	
	        // Intenta validar DOM
	        try {
	            valida.validate(new DOMSource(doc));
	            return true;
	
	        } catch (SAXException saxe) {
	            log.error(saxe.getMessage());
	            return false;
	        }
	       
		} catch (Exception e) {
		    log.error("Error parseando el archivo de entrada: "+ e.getMessage());
		    return false;
		}
    }

    public static void main(String[] args) {
        String unformattedXml2 =
            "<?xml version=\"1.0\" encoding=\"UTF-8\"?><QueryMessage" +
                    "        xmlns=\"http://www.SDMX.org/resources/SDMXML/schemas/v2_0/message\"" +
                    "        xmlns:query=\"http://www.SDMX.org/resources/SDMXML/schemas/v2_0/query\">" +
                    "    <Query>" +
                    "        <query:CategorySchemeWhere>" +
                    "   \t\t\t\t\t         <query:AgencyID>ECB</query:AgencyID>" +
                    "        </query:CategorySchemeWhere>" +
                    "    </Query>" +
                    "</QueryMessage>";
               
    }
}

