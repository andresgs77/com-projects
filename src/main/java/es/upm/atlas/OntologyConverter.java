/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package es.upm.atlas;

import com.hp.hpl.jena.ontology.OntClass;
import com.hp.hpl.jena.ontology.OntDocumentManager;
import com.hp.hpl.jena.ontology.OntModel;
import com.hp.hpl.jena.ontology.OntModelSpec;
import com.hp.hpl.jena.ontology.OntProperty;
import com.hp.hpl.jena.ontology.Ontology;
import com.hp.hpl.jena.rdf.model.ModelFactory;
import java.io.BufferedReader;
import java.io.FileInputStream;
import java.io.FileReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.UnsupportedEncodingException;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.Properties;
import org.apache.log4j.Logger;

/**
 *
 * @author hagarcia
 */
public class OntologyConverter {
    
    private static Logger log = Logger.getLogger(OntologyConverter.class);
    ArrayList<EntityProperty> entityProperties = new ArrayList<>();
    ArrayList<EntityClass> entityClasses = new ArrayList<>();
    String ontologyName;
    String baseUri;
    String dataAndObjectPropertyFile;
    String classFile;

    public OntologyConverter() throws Exception {
        Properties prop = new Properties();
        //load a properties file        
        log.info("Reading atlas properties");
    	prop.load(getClass().getClassLoader().getResourceAsStream("atlas.properties"));
        dataAndObjectPropertyFile=prop.getProperty("dataAndObjectPropertyFile");
        classFile=prop.getProperty("classFile");
        baseUri=prop.getProperty("baseURI");
        ontologyName=prop.getProperty("ontologyName");
        log.info("dataAndObjectPropertyFile:"+dataAndObjectPropertyFile);
        log.info("classFile:"+classFile);
        log.info("baseUri:"+baseUri);
        log.info("ontologyName:"+ontologyName);
    }                           
    
    private void readRelationsandProperties() throws UnsupportedEncodingException, IOException{ 
        
        log.info( "Reading relations and property data" );
        InputStream in = this.getClass().getClassLoader().getResourceAsStream(dataAndObjectPropertyFile);
        BufferedReader br = new BufferedReader(new InputStreamReader(in, "UTF-8"));
        
        String line;
        int i=0;
        EntityProperty entityProperty;
        
        EntityClass domainClass;
        EntityClass rangeClass;
        log.info( "Starts processing data" );
        while((line = br.readLine()) != null) {
              i++;
              //if (i<97) continue;
              //System.out.println(i+"- "+line);
              entityProperty = new EntityProperty();                
              //get the property id
              entityProperty.setId(line.substring(0,line.indexOf(";")));
              
              if (line.contains("label=")){              
                line=line.substring(line.indexOf(";")+1,line.length());  
                entityProperty.setLabel(line.substring(6,line.indexOf(";")));                
              }
              if (line.contains("desc=")){              
                line=line.substring(line.indexOf(";")+1,line.length());
                entityProperty.setDescription(line.substring(5,line.indexOf(";")));                              
              }
               if (line.contains("Range=")){
                  line=line.substring(line.indexOf(";Range")+1,line.length());
                  line=line.substring(6,line.length());                  
                  while(true){
                      if (line.contains("Class=")){
                        rangeClass = new EntityClass();  
                        rangeClass.setId(line.substring(6,line.indexOf(";")));
                        
                        line=line.substring(line.indexOf(";")+1,line.length());
                        rangeClass.setLabel(line.substring(6,line.indexOf(";")));
                        entityProperty.getRangeClasses().add(rangeClass);
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
                        domainClass = new EntityClass();  
                        domainClass.setId(line.substring(6,line.indexOf(";")));
                        
                        line=line.substring(line.indexOf(";")+1,line.length());
                        domainClass.setLabel(line.substring(6,line.indexOf(";")));
                        entityProperty.getDomainClasses().add(domainClass);
                      }
                      if (line.indexOf(";")+1==line.length())
                          break;
                      else                                    
                          line=line.substring(line.indexOf(";")+1,line.length());                      
                  }                  
              }                            
              log.info(entityProperty);              
              entityProperties.add(entityProperty);
              //if (i==2)
              //    break;
              
        }      
    }
    
    private void readClasses() throws Exception{
        log.info( "Reading classes" );
        InputStream in = this.getClass().getClassLoader().getResourceAsStream(classFile);
        BufferedReader br = new BufferedReader(new InputStreamReader(in, "UTF-8"));
        
        String line;
        int i=0;
        EntityClass entityClass;
                
        log.info( "Starts processing data" );
        while((line = br.readLine()) != null) {
            String[] classDescription = line.split(";");
            log.info( "Class: "+line);
            entityClass= new EntityClass();                
            //get the property id
            entityClass.setId(classDescription[0]);
            entityClass.setLabel(classDescription[1]);
            entityClass.setDescription(classDescription[2]);
            entityClasses.add(entityClass);
        }
    }
    
    public void transformToOnto() throws Exception{        
        readRelationsandProperties();
        //Load the property file where the uris are defined
        
        OntDocumentManager docManager = new OntDocumentManager();
        OntModelSpec modelSpec = new OntModelSpec(OntModelSpec.OWL_DL_MEM);
        modelSpec.setDocumentManager(docManager);
        
        OntModel model = ModelFactory.createOntologyModel(modelSpec);
        model.setDynamicImports(true);
        //model.setNsPrefix( "onto", ns_sourceOnto );
        model.setNsPrefix( "base", baseUri );        
        Ontology modelOnt = model.createOntology(ontologyName);
        
        for (EntityProperty entityProperty:entityProperties){
            
            OntProperty ontProperty=model.getOntProperty(baseUri+entityProperty.getId());
            if (ontProperty!=null)
                continue;
           
            if (entityProperty.getRangeClasses().isEmpty())
                //datatype property
                ontProperty=model.createDatatypeProperty(baseUri+entityProperty.getId());            
            else // object property            
                ontProperty=model.createObjectProperty(baseUri+entityProperty.getId());
           
            ontProperty.setLabel(entityProperty.getLabel(),"en");
            if (entityProperty.getDescription()!=null)
                ontProperty.setComment(entityProperty.getDescription(),"en");
            
            for (EntityClass domainClass:entityProperty.getDomainClasses()){
                OntClass ontDomainClass=model.getOntClass(baseUri+domainClass);
                if (ontDomainClass==null){ 
                    ontDomainClass=model.createClass(baseUri+domainClass.getId());
                    ontDomainClass.addLabel(domainClass.getLabel(),"en");
                }
                ontProperty.addDomain(ontDomainClass);
            }
            
            for (EntityClass rangeClass:entityProperty.getRangeClasses()){
                OntClass ontRangeClass=model.getOntClass(baseUri+rangeClass);
                if (ontRangeClass==null){
                    ontRangeClass=model.createClass(baseUri+rangeClass.getId());
                    ontRangeClass.addLabel(rangeClass.getLabel(),"en");
                }
                ontProperty.addRange(ontRangeClass);
            }
            
        }    
        
        for (EntityClass entityClass:entityClasses){
            OntClass ontClass=model.getOntClass(baseUri+entityClass.getId());
            if (ontClass==null){ 
                ontClass=model.createClass(baseUri+entityClass.getId());
                ontClass.addLabel(entityClass.getLabel(),"en");
            }
            ontClass.addComment(entityClass.getDescription(),"en");
        }               
        
//        Iterator ontClassToPrint=model.listClasses();
//        System.out.println("total de clases:"+ontClassToPrint);
//        int i=0;
//        while (ontClassToPrint.hasNext()){
//            i++;
//            OntClass myOntClass=(OntClass)ontClassToPrint.next();
//            System.out.println(myOntClass);
//        }
//        System.out.println("total de clases:"+i);
        
        model.write(System.out, "N3");
        
        
    }
}
