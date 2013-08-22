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
public class EntityProperty {
    String id;
    String label;
    String description;
    ArrayList<EntityClass> domainClasses; 
    ArrayList<EntityClass> rangeClasses; 

    public EntityProperty() {
        domainClasses =  new <EntityClass>ArrayList();
        rangeClasses =  new <EntityClass>ArrayList();;
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

    public ArrayList<EntityClass> getDomainClasses() {
        return domainClasses;
    }

    public void setDomainClasses(ArrayList domainClasses) {
        this.domainClasses = domainClasses;
    }

    public ArrayList<EntityClass> getRangeClasses() {
        return rangeClasses;
    }

    public void setRangeClasses(ArrayList rangeClasses) {
        this.rangeClasses = rangeClasses;
    }
    
    
    
    public String toString(){
        String str="Id:"+id+" Label:"+label+" Desc:"+description;
        if (domainClasses!=null)
            for (EntityClass domainClass:domainClasses){
                str += " domainClass={" + domainClass.getId()+","+domainClass.getLabel()+"}";
            }
        if (rangeClasses!=null)
            for (EntityClass rangeClass:rangeClasses){
                str += " rangeClass={" + rangeClass.getId()+","+rangeClass.getLabel()+"}";
            }
        return str;
    }
    
            
}
