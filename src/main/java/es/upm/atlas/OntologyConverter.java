/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package es.upm.atlas;

import com.hp.hpl.jena.ontology.OntDocumentManager;
import com.hp.hpl.jena.ontology.OntModelSpec;
import java.io.BufferedReader;
import java.io.FileReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.UnsupportedEncodingException;
import org.apache.log4j.Logger;

/**
 *
 * @author hagarcia
 */
public class OntologyConverter {
    
    private static Logger log = Logger.getLogger(OntologyConverter.class);
    
    public void readRelationsandProperties() throws UnsupportedEncodingException, IOException{ 
        
        log.info( "Reading relations and property data" );
        InputStream in = this.getClass().getClassLoader().getResourceAsStream("TextandLinkAttributesDomanRange_stage1.txt");
        BufferedReader br = new BufferedReader(new InputStreamReader(in, "UTF-8"));
        
        String line;
        int i=0;
        Property property;
        OntologyClass domainClass;
        OntologyClass rangeClass;
        log.info( "Starts processing data" );
        while((line = br.readLine()) != null) {
              i++;
              //if (i<97) continue;
              System.out.println(i+"- "+line);
              property = new Property();                
              //get the property id
              property.setId(line.substring(0,line.indexOf(";")));
              
              if (line.contains("label=")){              
                line=line.substring(line.indexOf(";")+1,line.length());  
                property.setLabel(line.substring(6,line.indexOf(";")));                
              }
              if (line.contains("desc=")){              
                line=line.substring(line.indexOf(";")+1,line.length());
                property.setDescription(line.substring(5,line.indexOf(";")));                              
              }
               if (line.contains("Range=")){
                  line=line.substring(line.indexOf(";Range")+1,line.length());
                  line=line.substring(6,line.length());                  
                  while(true){
                      if (line.contains("Class=")){
                        rangeClass = new OntologyClass();  
                        rangeClass.setId(line.substring(6,line.indexOf(";")));
                        
                        line=line.substring(line.indexOf(";")+1,line.length());
                        rangeClass.setLabel(line.substring(6,line.indexOf(";")));
                        property.getRangeClasses().add(rangeClass);
                      }
                      if (line.indexOf(";")==line.indexOf(";Domain="))
                          break;
                      else                                    
                          line=line.substring(line.indexOf(";")+1,line.length());                      
                  }                  
              }        
              if (line.contains("Domain=")){
                  line=line.substring(line.indexOf(";Domain")+1,line.length());
                  line=line.substring(7,line.length());                  
                  while(true){
                      if (line.contains("Class=")){
                        domainClass = new OntologyClass();  
                        domainClass.setId(line.substring(6,line.indexOf(";")));
                        
                        line=line.substring(line.indexOf(";")+1,line.length());
                        domainClass.setLabel(line.substring(6,line.indexOf(";")));
                        property.getDomainClasses().add(domainClass);
                      }
                      if (line.indexOf(";")+1==line.length())
                          break;
                      else                                    
                          line=line.substring(line.indexOf(";")+1,line.length());                      
                  }                  
              }                            
              log.info(property);
              //if (i==2)
              //    break;
              
        }      
    }
    
    public void transformToOnto() throws Exception{
        OntDocumentManager docManager = new OntDocumentManager();
        OntModelSpec modelSpec = new OntModelSpec(OntModelSpec.OWL_DL_MEM);
        modelSpec.setDocumentManager(docManager);
    }
}
