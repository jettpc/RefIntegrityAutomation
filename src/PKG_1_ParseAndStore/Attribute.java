package PKG_1_ParseAndStore;

// custom data structure to store all attribute information
// An Entity contains Attributes *************************
public class Attribute {
    private String name;
    private Property[] prop;
    private String ref;

    // default constructor!
    public Attribute(){
        name = "AttributeName";
        ref = "referenceName";
        prop = new Property[1];
        prop[0] = new Property();
        System.out.println("successfully created PKG_1_ParseAndStore.Attribute");
    }

    public Attribute(int p) { // pass the entity and array of numbers. Length is how many attributes, the number at each index is how many properties are on that specific attribute
        name = null;
        ref = null;
        prop = new Property[p];
        for(int i = 0; i < p; i++) {
            prop[i] = new Property();
        }
    }

    // setters and getters
    public void setName(String n) {
        this.name = n;
    }
    public void setProperty(Property p, int index) {
        this.prop[index] = p;
    }
    public void setReference(String r) {
        this.ref = r;
    }
    public String getName() {
        return this.name;
    }
    public Property getProp(int index) {
        return this.prop[index];
    }
    public String getRef() {
        return this.ref;
    }

    // to string
    public String toString() {
        StringBuilder sb = new StringBuilder();
        for(int i = 0; i < prop.length; i++) {
            sb.append("\n\t\t" + prop[i].toString());
        }
        if(ref != null) {
            return "\t" + name + ": {\n\t\treferences: {\n\t\t\tmodel: " + ref + ",\n\t\t}," + sb + "\n\t}\n";
        }
        return "\t" + name + ": {" + sb + "\n\t}\n";
    }
}
