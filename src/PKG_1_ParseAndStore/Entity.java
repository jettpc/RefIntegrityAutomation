package PKG_1_ParseAndStore;

// custom data structure to store all entity information
public class Entity {
    private String entityName;
    private Attribute[] attr;

    // default constructor!
    public Entity() {
        entityName = "EntityName";
        attr = new Attribute[1];
        attr[0] = new Attribute();
        System.out.println("successfully created PKG_1_ParseAndStore.Entity");
        System.out.println(this.attr.length);
    }

    public Entity(int[] a) { // pass the entity and array of numbers. Length is how many attributes, the number at each index is how many properties are on that specific attribute
        entityName = null;
        attr = new Attribute[a.length];
        for(int i = 0; i < a.length; i++) {
            attr[i] = new Attribute(a[i]);
        }
    }

    // setters and getters!
    public void setEntityName(String entityName) {
        this.entityName = entityName;
    }
    public void setAttr(Attribute a, int index) {
        this.attr[index] = a;
    }
    public String getEntityName() {
        return this.entityName;
    }
    public Attribute getAttr(int index) {
        return this.attr[index];
    }

    // how many attributes objects are in the attribute array - how many attributes does this entity have
    public int getAttrSize() { return this.attr.length; }

    // to string
    public String toString() {
        StringBuilder sb = new StringBuilder();
        for(int i = 0; i < attr.length; i++) {
            sb.append(attr[i].toString());
        }
        return "'" + entityName + "',\n{\n" + sb + "}";
    }
}

