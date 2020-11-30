package fieldtracker;

import java.io.Serializable;
import java.util.ArrayList;

public class FieldAttribute implements Serializable, Cloneable {

    private static final long serialVersionUID = -5551555572969096599L;


    private String attributeName;
    private ArrayList<String> attributeValues;


    public String getAttributeName() {
        return attributeName;
    }

    public void setAttributeName(String attributeName) {
        this.attributeName = attributeName;
    }

    public ArrayList<String> getAttributeValues() {
        return attributeValues;
    }

    public void setAttributeValues(ArrayList<String> attributeValues) {
        this.attributeValues = attributeValues;
    }
}
