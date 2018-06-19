import PKG_1_ParseAndStore.Entity;
import PKG_1_ParseAndStore.EntityParser;

import java.util.*;

/**
 * <h1>Migration referential integrity solution!</h1>
 * REASON FOR THIS PROGRAM:
 *
 * SITUATION: Given complex relational database with over 50 entities, when API testing, the mock db needs to
 * create create tables without any referential integrity constraints.
 *
 * OBSTACLES: TIME CONSUMING! Manually creating the tree is very, very time consuming
 *
 * ACTION: SOLVE THE PUZZLE! Took ownership and created this program that, given the desired entity,
 * traverses through the migration file containing all entities, entity attributes (POTENTIAL RELATIONSHIPS), and
 * attribute properties, and stores it in an array of Entities (personal custom data structure). Next this program
 * recurses through the given entity and outputs the order in which you should create your tables so there is no
 * referential integrity constraint, AS WELL AS no extra space allocated for tables not used.
 *
 * RESULTS: SAVE TIME WITH GENERATED AUTOMATED TESTING!
 *
 * @author  Paul Jett
 * @version 1.2
 * @since   7/30/17
 */

public class Main {

    static Set<String> globalRefList = new LinkedHashSet<String>();

    public static void main(String[] args)  {

        //******************** Step 1: Get user input ********************
        System.out.println("Please enter the entity you would like the table creation order of: ");
        Scanner in = new Scanner(System.in);
        String entityName = in.nextLine();
        in.close();

        //******************** Step 2: Create and populate custom data structure ********************
        EntityParser data = new EntityParser();

        //******************** Step 3: Check entity exists ********************
        int entityIndex = 0;
        boolean entityExists = false;
        for (int i = 0; i < data.entity.length; i++) {
            if (data.entity[i].getEntityName().equalsIgnoreCase(entityName)) {
                entityIndex = i;
                entityExists = true;
                break;
            }
        }
        if(!entityExists) {
            System.out.println("Name of Entity does not exist in file. Exiting program");
                System.exit(1);
        }

        //******************** Step 4: Post-Order Depth First Search ********************
        postOrderDFS(data.entity, entityIndex);

        //******************** Step 5: PROFIT!!! copy and paste creation order ********************
        Iterator<String> refList_Iter = globalRefList.iterator();
        while(refList_Iter.hasNext()) {
            System.out.println(refList_Iter.next());
        }
    }

    /**
     * Post-order DFS which searches for all entity relationships
     * amongst the entities attributes and adds them to a global list.
     * @param e entity array containing every entity/table in the relational db migration file
     * @param eIndex the root index, and current index of the entity being checked for relationships
     */
    public static void postOrderDFS (Entity[] e, int eIndex) {

        for (int attrIndex = 0; attrIndex < e[eIndex].getAttrSize(); attrIndex++) { //for every attribute[] in the entity
            if (e[eIndex].getAttr(attrIndex).getRef() != null && !e[eIndex].getAttr(attrIndex).getRef().equals(e[eIndex].getEntityName())) { //if a reference exists and it doesn't reference itself
                postOrderDFS(e, getEntityIndex(e, e[eIndex].getAttr(attrIndex).getRef())); //traverse left to right (index 0, 1, 2, ... , n)
            }
        }
        globalRefList.add(e[eIndex].getEntityName()); //add fully traversed root, or current node to global ref list
    }

    /**
     * Takes the given entities reference name and returns
     * the index of the referenced entity in order to continue the
     * recursive traversal of the graph-like entity data structure
     * @param e entity array containing every entity/table in the relational db migration file
     * @param s name of the indexed entities non-indexed reference
     * @return index of inputted entities reference
     */
    public static int getEntityIndex(Entity[] e, String s) {
        int newEIndex = 0;
        for (int i = 0; i < e.length; i++) {
            if (e[i].getEntityName().equalsIgnoreCase(s)) {
                newEIndex = i;
                break;
            }
        }
        return newEIndex;
    }
}