package PKG_1_ParseAndStore;

// custom data structure to store all property information
// An Attributes contains Properties *********************
public class Property {
    private String name;
    private String value;

    // default constructor!
    public Property(){
        name = "PropertyName";
        value = "PropertyValue";
    }

    // setters and getters
    public void setName(String n) {
        this.name = n;
    }
    public void setValue(String v) {
        this.value = v;
    }
    public String getName() {
        return this.name;
    }
    public String getValue() {
        return this.value;
    }
    // to string
    public String toString() {
        return name + ": " + value;
    }
}
