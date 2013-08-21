/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package es.upm.atlas;

import java.util.ArrayList;

/**
 *
 * @author hagarcia
 */
public class Property {
    String id;
    String label;
    String description;
    ArrayList<OntologyClass> domainClasses; 
    ArrayList<OntologyClass> rangeClasses; 

    public Property() {
        domainClasses = new <OntologyClass>ArrayList();
        rangeClasses = new <OntologyClass>ArrayList();;
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getLabel() {
        return label;
    }

    public void setLabel(String label) {
        this.label = label;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public ArrayList getDomainClasses() {
        return domainClasses;
    }

    public void setDomainClasses(ArrayList domainClasses) {
        this.domainClasses = domainClasses;
    }

    public ArrayList getRangeClasses() {
        return rangeClasses;
    }

    public void setRangeClasses(ArrayList rangeClasses) {
        this.rangeClasses = rangeClasses;
    }
    
    
    
    public String toString(){
        String str="Id:"+id+" Label:"+label+" Desc:"+description;
        if (domainClasses!=null)
            for (OntologyClass domainClass:domainClasses){
                str += " domainClass={" + domainClass.getId()+","+domainClass.getLabel()+"}";
            }
        if (rangeClasses!=null)
            for (OntologyClass rangeClass:rangeClasses){
                str += " rangeClass={" + rangeClass.getId()+","+rangeClass.getLabel()+"}";
            }
        return str;
    }
    
            
}
