package com.eon.fatr.utilidades;

import java.io.File;

import java.net.MalformedURLException;

import java.net.URL;

import oracle.xml.parser.schema.XMLSchema;

import oracle.xml.parser.schema.XSDBuilder;

import oracle.xml.parser.v2.DOMParser;

import oracle.xml.parser.v2.XMLParseException;

public class SchemaValidator{
	
	public SchemaValidator(){
	}
	
	private String process(String s, XMLSchema xmlschema) throws Exception{
	
		DOMParser domparser = new DOMParser();
		URL url = createURL(s);
		String s1 = "";
		domparser.setXMLSchema(xmlschema);
		domparser.setValidationMode(6);
		domparser.setPreserveWhitespace(true);
		
		try{
			domparser.parse(url);
		}catch(XMLParseException xmlparseexception){
			s1 = "Parser Exception: " + xmlparseexception.getMessage();
		}
		catch(Exception exception){
			s1 = "NonParserException: " + exception.getMessage();
		}
		return s1;
	}
	
	private URL createURL(String s){
	
		URL url = null;
		try{
			url = new URL(s);
		}catch(MalformedURLException malformedurlexception){
			File file = new File(s);
	
			try{
					String s1 = file.getAbsolutePath();
					String s2 = System.getProperty("file.separator");
					if(s2.length() == 1){
					
						char c = s2.charAt(0);
						if(c != '/') s1 = s1.replace(c, '/');
						if(s1.charAt(0) != '/') s1 = '/' + s1;
					}
					s1 = "file://" + s1;
					url = new URL(s1);
			}catch(MalformedURLException malformedurlexception1){
				System.exit(0);
			}
		}
		return url;
	
	}
	
	public String validateSchema(String s, String s1){
	
		//boolean flag = false;
		
		String flag = "";
		
		try{
	
			XSDBuilder xsdbuilder = new XSDBuilder();
			URL url = createURL(s);
			XMLSchema xmlschema = (XMLSchema)xsdbuilder.build(url);
			flag = process(s1, xmlschema).trim();
		
		}catch(Exception exception) { }
	
		return flag;
	
	}
}
