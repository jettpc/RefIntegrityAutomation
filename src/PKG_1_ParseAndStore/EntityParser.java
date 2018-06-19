package PKG_1_ParseAndStore;

import java.io.File;
import java.io.FileNotFoundException;
import java.util.Scanner;

// this class is used to allocate space for and populate the "Entity" data structure to store all entities/tables information
public class EntityParser {

    int[] staticCount = entityAndMaxAttributeCount(); // find max number of entities and max num of attributes

    public Entity[] entity;

    public EntityParser() { entity = populateData(staticCount[0], staticCount[1]); }

    /**
     * counts the TOTAL # OF ENTITIES
     * and MAX # OF ATTRIBUTES PER ENTITY
     * by reading the migration file (described in ACTION above)
     * and keeping counters for certain symbols
     * @return int[] where index 0 = total number of entities and
     * index 1 = largest/max attribute count of an individual entity
     */
    public static int[] entityAndMaxAttributeCount() {
        // initializing variables
        int entityCount = 0;
        int attributeCount = 0;
        int maxAttributeCount = 0;
        int[] returnCount = new int[2];
        String line = "";
        File f = new File("migration.txt");

        // appropriate file reading error handling
        Scanner scan = null;
        try {
            scan = new Scanner(f);
            while (scan.hasNextLine()) { // scan tables file
                line = scan.nextLine();
                if (line.contains(": {") && !line.contains("references")) { // count number of attributes of current entity
                    attributeCount++;
                }
                else if (line.contains("[") && !line.contains("const tables")) { // count total number of entities
                    entityCount++;
                }
                else if (line.contains("],")) { // end of an entity
                    if (maxAttributeCount < attributeCount) { // calculate if that entity had the greatest number of attributes
                        maxAttributeCount = attributeCount;
                    }
                    attributeCount = 0; // reset attribute count after each entity
                }
                else if (line.contains("];")) { // calculate last entities attributes and break out of loop
                    if (maxAttributeCount < attributeCount) {
                        maxAttributeCount = attributeCount;
                    }
                    break;
                }
            }
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        } finally {
            if(scan != null) {
                scan.close();
            }
        }
        returnCount[0] = entityCount;       // return value for entityCount
        returnCount[1] = maxAttributeCount; // return value for maxAttributeCount
        return returnCount;
    }

    /**
     * Counts correct # of attributes and # of properties each attribute within an
     * entity contains, and creates an appropriately sized entity object with null
     * values as placeholders. Next this function utilizes a helper function that
     * scans the migrations file again and sets the values for everything (entity
     * names, attribute names, property names and values)
     * @param totalNumOfEntities the total number of entities/tables in the relational db
     * @param maxSizeOfAttributes largest number of attributes an entity has (found by scanning)
     * @return an entity array with all of the correct table/entity information
     */
    public static Entity[] populateData(int totalNumOfEntities, int maxSizeOfAttributes) {
        Entity[] entityArray = new Entity[totalNumOfEntities];
        int[] tempAttrCount = new int[maxSizeOfAttributes]; // max number of attributes
        int[] properAttrSize;
        int properAttrCount = 0;    // gets reset later
        int entityNumber = 0;       // does not get reset
        int attributeNumber = 0;    // gets reset later
        int propertyCount = 0;      // gets reset later
        boolean refScope = false;
        String line = "";
        File f = new File("migration.txt");

        //appropriate file reading error handling
        Scanner scanSize = null;
        try {
            scanSize = new Scanner(f);
            for(int i = 0; i < totalNumOfEntities; i++) {  // allocate memory for appropriate # of attributes and properties for each entity
                while(scanSize.hasNextLine()) {
                    line = scanSize.nextLine();
                    if(line.contains("}")) {
                        refScope = false;
                    }
                    if(refScope == false && line.contains(":") && !line.contains("{") && !line.contains("up: function(migration, DataTypes)")) { // count for total # of properties of each attribute
                        propertyCount++;
                    }
                    else if(refScope == false && line.equals("                    },") || line.equals("                    }")) { // signifies the end of an attribute
                        tempAttrCount[attributeNumber] = propertyCount; // store # of properties for indexed attribute
                        propertyCount = 0; // reset propertyCount
                        attributeNumber++; // move to next attribute
                    }
                    else if(line.contains("references")) { // ensures properties of a reference are not included in property count of attribute
                        refScope = true;
                    }
                    else if(line.contains("]")) { // marks the end of an entity
                        break;
                    }
                }
                /*
                 GOAL BELOW: allocate an appropriately sized array of attributes and containing
                 values of that attributes # of properties to create the appropriately sized entity
                 NOTE: Since tempAttrCount.length is the length of the largest number of attributes found,
                 we need to only allocate memory for the given entities # of attributes
                 */
                properAttrCount = 0;
                for(int k = 0; k < tempAttrCount.length; k++) { // get appropriate size
                    if(tempAttrCount[k] != 0) {
                        properAttrCount++;
                    }
                }
                properAttrSize = new int[properAttrCount]; // create appropriately sized array
                for(int l = 0; l < properAttrSize.length; l++) { // copy data over to properSized array
                    properAttrSize[l] = tempAttrCount[l];
                }
                entityArray[entityNumber] = new Entity(properAttrSize); // create the entity
                entityNumber++;
                attributeNumber = 0;
                for(int n = 0; n < tempAttrCount.length; n++) { // clear temp data storage
                    tempAttrCount[n] = 0;
                }
            }
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        } finally {
            if(scanSize != null) {
                scanSize.close();
            }
        }
        entityArray = setData(entityArray); //helper method that assigns/sets data
        return entityArray;                 //return the beautiful, appropriately sized data structure
    }

    /**
     * Scans the migrations file again and sets the values for everything (entity
     * names, attribute names, property names and values)
     * @param eArray appropriately sized entities within an entity array
     * @return an entity array with all the values set
     */
    public static Entity[] setData (Entity[] eArray) {
        int eCount = 0; // entity counter
        int aCount = 0; // attribute counter
        int pCount = 0; // property counter
        String line = "";
        String[] storeSplit;
        boolean refScope = false;
        File f = new File("migration.txt");

        // appropriate file reading error handling
        Scanner scanSet = null;
        try {
            scanSet = new Scanner(f);
            while (scanSet.hasNextLine()) {
                line = scanSet.nextLine();
                if(line.contains("}")) {
                    refScope = false;
                }
                if (refScope == false && line.contains("',") && !line.contains(":")) { // entity name
                    line = line.replaceAll("[^a-zA-Z]",""); // clean the String
                    eArray[eCount].setEntityName(line);
                }
                else if (refScope == false && line.contains(": {") && !line.contains("references")) { // attribute name
                    line = line.replaceAll("[^a-zA-Z]", ""); // clean String
                    eArray[eCount].getAttr(aCount).setName(line);
                }
                else if (refScope == false && line.contains(":") && !line.contains("{")) { // properties name and value
                    line = line.replaceAll("[^a-zA-Z:.,()0-9]", ""); // clean String
                    storeSplit = line.split(":");
                    eArray[eCount].getAttr(aCount).getProp(pCount).setName(storeSplit[0]);
                    eArray[eCount].getAttr(aCount).getProp(pCount).setValue(storeSplit[1]);
                    pCount++;
                }
                else if (refScope == false && line.contains("}") && !line.equals("                        },") && !line.equals("                        }")) { // move to next attribute
                    pCount = 0;
                    aCount++;
                }
                else if (line.contains("]")) { // move to next entity
                    eCount++;
                    pCount = 0;
                    aCount = 0;
                }
                else if (line.contains("references")) { // helps set attributes reference
                    refScope = true;
                }
                else if (line.contains("model:") && refScope == true) { // set attributes reference
                    line = line.replaceAll("[^a-zA-Z:.]", ""); // clean String
                    storeSplit = line.split(":");
                    eArray[eCount].getAttr(aCount).setReference(storeSplit[1]);;
                }
                else if (line.contains("        /* ================================================================================= */")) { // end of table definitions
                    break;
                }
            }
            scanSet.close();
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        } finally {
            if(scanSet != null) {
                scanSet.close();
            }
        }
        return eArray;
    }




}
