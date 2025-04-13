error id: file://<WORKSPACE>/data/code-rep-dataset/Dataset5/Tasks/18197.java
file://<WORKSPACE>/data/code-rep-dataset/Dataset5/Tasks/18197.java
### com.thoughtworks.qdox.parser.ParseException: syntax error @[1,1]

error in qdox parser
file content:
```java
offset: 1
uri: file://<WORKSPACE>/data/code-rep-dataset/Dataset5/Tasks/18197.java
text:
```scala
a@@ssertEquals(ValueMetaData.CASCADE_NONE, meta.getField("firstName").getCascadeDetach());

package org.apache.openjpa.persistence.detachment;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import org.apache.openjpa.meta.ClassMetaData;
import org.apache.openjpa.meta.MetaDataRepository;
import org.apache.openjpa.meta.ValueMetaData;
import org.apache.openjpa.persistence.OpenJPAEntityManager;
import org.apache.openjpa.persistence.detachment.model.DMCustomer;
import org.apache.openjpa.persistence.detachment.model.DMCustomerInventory;
import org.apache.openjpa.persistence.detachment.model.DMItem;
import org.apache.openjpa.persistence.test.SingleEMFTestCase;

/**
 * Tests detachment behavior according to JPA 2.0 Specification. The primary
 * changes in detachment behavior from the existing OpenJPA behavior are
 * i. detach(x) does not flush if x is dirty
 * ii. detach(x) removes x from persistence context
 * iii. detach(x) propagates via CascadeType.DETACH. It is not clear how that
 * impacts the detach graph. So currently, detach graph is same as 'loaded'.   
 * 
 * The test uses a 'domain model' with following cascade relation
 * 
 *            ALL                 PERSIST, MERGE, REFRESH
 * Customer --------> Inventory   ----------------------> Item
 *          <-------
 *            MERGE
 *            
 * @author Pinaki Poddar
 *
 */
public class TestDetach extends SingleEMFTestCase {
    private OpenJPAEntityManager em;
    private DMCustomer root;
    
    public void setUp() {
        super.setUp(DMCustomer.class, DMCustomerInventory.class, DMItem.class,
            CLEAR_TABLES);
        em = emf.createEntityManager();
        root = createData();
    }
    
    public void testDetachCascadeIsSet() {
        MetaDataRepository repos = emf.getConfiguration()
                                      .getMetaDataRepositoryInstance();
        ClassMetaData meta = repos.getCachedMetaData(DMCustomer.class);
        assertEquals(ValueMetaData.CASCADE_IMMEDIATE, meta.getField("firstName").getCascadeDetach());
        assertEquals(ValueMetaData.CASCADE_IMMEDIATE, meta.getField("customerInventories").getElement().getCascadeDetach());
        
        meta = repos.getCachedMetaData(DMCustomerInventory.class);
        assertEquals(ValueMetaData.CASCADE_NONE, meta.getField("customer").getCascadeDetach());
        assertEquals(ValueMetaData.CASCADE_NONE, meta.getField("item").getCascadeDetach());
        
    }
    
    public void testDetachRemovesEntityAndCascadedRelationFromContext() {
        em.getTransaction().begin();
        
        DMCustomer pc = em.find(DMCustomer.class, root.getId());
        List<DMCustomerInventory> inventories = pc.getCustomerInventories();
        DMItem item = inventories.get(0).getItem();
        
        assertNotDetached(pc);
        for (DMCustomerInventory i : inventories) assertNotDetached(i);
        assertNotDetached(item);   
        
        em.clear(pc);
        
        assertDetached(pc);
        for (DMCustomerInventory i : inventories) assertDetached(i);
        
        em.getTransaction().rollback();
        
        assertNotNull(pc.getFirstName());
    }
    
    public void testDetachingDirtyEntityDoesNotImplicitlyFlush() {
        em.getTransaction().begin();
        DMCustomer pc = em.find(DMCustomer.class, root.getId());
        String original = pc.getLastName();
        pc.setLastName("Changed That Should not be Saved");
 
        em.clear(pc);
        em.getTransaction().commit();
        
        DMCustomer pc2 = em.find(DMCustomer.class, root.getId());
        assertNotNull(pc2);
        assertEquals(original, pc2.getLastName());
    }
    
    public void testDetachingNewEntityIsIgnored() {
        em.getTransaction().begin();
        DMCustomer pc = em.find(DMCustomer.class, root.getId());
        List<DMCustomerInventory> inventories = pc.getCustomerInventories();
        
        DMCustomer newPC = new DMCustomer();
        newPC.setCustomerInventories(inventories);
        for (DMCustomerInventory inventory : inventories)
            inventory.setCustomer(newPC);
        
        em.clear(newPC);
        for (DMCustomerInventory inventory : inventories) {
            assertNotDetached(inventory);
        }
        em.getTransaction().rollback();
    }
    
    public void testDetachingDetachedEntityIsIgnored() {
        em.getTransaction().begin();
        DMCustomer pc = em.find(DMCustomer.class, root.getId());
        List<DMCustomerInventory> inventories = pc.getCustomerInventories();
        
        em.clear(pc);
        DMCustomer detached = pc;
        assertDetached(detached);
        for (DMCustomerInventory inventory : inventories) {
            assertDetached(inventory);
        }
        
        List<DMCustomerInventory> newInventories = new ArrayList<DMCustomerInventory>();
        newInventories.addAll(inventories);
        DMCustomerInventory newInventory = new DMCustomerInventory();
        newInventory.setCustomer(detached);
        newInventories.add(newInventory);
        detached.setCustomerInventories(newInventories);
        em.persist(newInventory);
        assertNotDetached(newInventory);
        
        em.clear(detached);
        assertDetached(detached);
        assertEquals(inventories.size()+1, newInventories.size());
        for (DMCustomerInventory inventory : newInventories) {
            if (inventory == newInventory)
                assertNotDetached(inventory);
            else
                assertDetached(inventory);
        }
        em.getTransaction().rollback();
    }
    
    
    public void testFlushingBeforeDetachingSavesChange() {
        
    }
    
    public void testManagedEntityContinuesToReferDetachedEntities() {
        em.getTransaction().begin();
        
        DMCustomer pc = em.find(DMCustomer.class, root.getId());
        List<DMCustomerInventory> inventories = pc.getCustomerInventories();
        DMItem item = inventories.get(1).getItem();
        
        em.clear(inventories.get(0));
        
        DMCustomerInventory attached0 = inventories.get(0);
        DMCustomerInventory attached1 = inventories.get(1);
        
        assertSame(pc.getCustomerInventories().get(0), attached0);
        assertSame(pc.getCustomerInventories().get(1), attached1);
        
        em.getTransaction().rollback();
    }
    
    DMCustomer createData() {
        DMItem item1 = new DMItem();
        DMItem item2 = new DMItem();
        item1.setName("item-1"); item1.setPrice(100.0);
        item2.setName("item-2"); item2.setPrice(200.0);
        
        DMCustomerInventory inventory1 = new DMCustomerInventory();
        DMCustomerInventory inventory2 = new DMCustomerInventory();
        inventory1.setItem(item1); inventory1.setQuantity(10);
        inventory2.setItem(item2); inventory2.setQuantity(20);
        DMCustomer customer = new DMCustomer();
        customer.setFirstName("Detached"); customer.setLastName("Customer");
        customer.setCustomerInventories(Arrays.asList(
            new DMCustomerInventory[]{inventory1,inventory2}));
        inventory1.setCustomer(customer);
        inventory2.setCustomer(customer);
        
        em.getTransaction().begin();
        em.persist(customer);
        em.getTransaction().commit();
        em.clear();
        
        return customer;
    }
    
    void assertDetached(Object pc) {
        assertTrue(pc + " should be detached", em.isDetached(pc));
        assertFalse(pc + " should not be in cache", em.contains(pc));
    }
    
    void assertNotDetached(Object pc) {
        assertFalse(pc + " should not be detached", em.isDetached(pc));
        assertTrue(pc + " should be in cache", em.contains(pc));
    }
}
```

```



#### Error stacktrace:

```
com.thoughtworks.qdox.parser.impl.Parser.yyerror(Parser.java:2025)
	com.thoughtworks.qdox.parser.impl.Parser.yyparse(Parser.java:2147)
	com.thoughtworks.qdox.parser.impl.Parser.parse(Parser.java:2006)
	com.thoughtworks.qdox.library.SourceLibrary.parse(SourceLibrary.java:232)
	com.thoughtworks.qdox.library.SourceLibrary.parse(SourceLibrary.java:190)
	com.thoughtworks.qdox.library.SourceLibrary.addSource(SourceLibrary.java:94)
	com.thoughtworks.qdox.library.SourceLibrary.addSource(SourceLibrary.java:89)
	com.thoughtworks.qdox.library.SortedClassLibraryBuilder.addSource(SortedClassLibraryBuilder.java:162)
	com.thoughtworks.qdox.JavaProjectBuilder.addSource(JavaProjectBuilder.java:174)
	scala.meta.internal.mtags.JavaMtags.indexRoot(JavaMtags.scala:48)
	scala.meta.internal.metals.SemanticdbDefinition$.foreachWithReturnMtags(SemanticdbDefinition.scala:97)
	scala.meta.internal.metals.Indexer.indexSourceFile(Indexer.scala:489)
	scala.meta.internal.metals.Indexer.$anonfun$indexWorkspaceSources$7(Indexer.scala:361)
	scala.meta.internal.metals.Indexer.$anonfun$indexWorkspaceSources$7$adapted(Indexer.scala:356)
	scala.collection.IterableOnceOps.foreach(IterableOnce.scala:619)
	scala.collection.IterableOnceOps.foreach$(IterableOnce.scala:617)
	scala.collection.AbstractIterator.foreach(Iterator.scala:1306)
	scala.collection.parallel.ParIterableLike$Foreach.leaf(ParIterableLike.scala:938)
	scala.collection.parallel.Task.$anonfun$tryLeaf$1(Tasks.scala:52)
	scala.runtime.java8.JFunction0$mcV$sp.apply(JFunction0$mcV$sp.scala:18)
	scala.util.control.Breaks$$anon$1.catchBreak(Breaks.scala:97)
	scala.collection.parallel.Task.tryLeaf(Tasks.scala:55)
	scala.collection.parallel.Task.tryLeaf$(Tasks.scala:49)
	scala.collection.parallel.ParIterableLike$Foreach.tryLeaf(ParIterableLike.scala:935)
	scala.collection.parallel.AdaptiveWorkStealingTasks$AWSTWrappedTask.internal(Tasks.scala:159)
	scala.collection.parallel.AdaptiveWorkStealingTasks$AWSTWrappedTask.internal$(Tasks.scala:156)
	scala.collection.parallel.AdaptiveWorkStealingForkJoinTasks$AWSFJTWrappedTask.internal(Tasks.scala:304)
	scala.collection.parallel.AdaptiveWorkStealingTasks$AWSTWrappedTask.compute(Tasks.scala:149)
	scala.collection.parallel.AdaptiveWorkStealingTasks$AWSTWrappedTask.compute$(Tasks.scala:148)
	scala.collection.parallel.AdaptiveWorkStealingForkJoinTasks$AWSFJTWrappedTask.compute(Tasks.scala:304)
	java.base/java.util.concurrent.RecursiveAction.exec(RecursiveAction.java:194)
	java.base/java.util.concurrent.ForkJoinTask.doExec(ForkJoinTask.java:373)
	java.base/java.util.concurrent.ForkJoinPool$WorkQueue.topLevelExec(ForkJoinPool.java:1182)
	java.base/java.util.concurrent.ForkJoinPool.scan(ForkJoinPool.java:1655)
	java.base/java.util.concurrent.ForkJoinPool.runWorker(ForkJoinPool.java:1622)
	java.base/java.util.concurrent.ForkJoinWorkerThread.run(ForkJoinWorkerThread.java:165)
```
#### Short summary: 

QDox parse error in file://<WORKSPACE>/data/code-rep-dataset/Dataset5/Tasks/18197.java