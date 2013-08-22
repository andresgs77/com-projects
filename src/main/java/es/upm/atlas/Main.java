/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package es.upm.atlas;

import java.io.IOException;
import java.io.InputStream;
import java.io.UnsupportedEncodingException;
import java.util.Iterator;
import org.apache.log4j.PropertyConfigurator;

/**
 *
 * @author hagarcia
 */
public class Main {
    
    public static void main(String [] args) throws Exception
	{
        // Configure Log4J
        PropertyConfigurator.configure(Main.class.getClassLoader().getResource("log4j.properties"));
        OntologyConverter converter = new OntologyConverter();
        
        converter.transformToOnto();
        
            
       }
                
                       
}    

