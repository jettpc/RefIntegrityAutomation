# RefIntegrityAutomation

 * <h1>Migration referential integrity solution!</h1>
 * REASON FOR THIS PROGRAM:
 *
 * <h5>SITUATION</h5>: Given complex relational database with over 50 entities, when API testing, the mock db needs to
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
 
 NOTE: For this program I modified the migration file to help readers understand the entity data structure used in 
 this program. I also reduced the number of entities from 50+ to 8 and rewrote entity names, attributes, and properties
 to be more generic.
