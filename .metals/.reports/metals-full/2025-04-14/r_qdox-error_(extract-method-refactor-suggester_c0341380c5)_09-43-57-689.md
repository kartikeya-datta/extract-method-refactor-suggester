error id: file://<WORKSPACE>/data/code-rep-dataset/Dataset3/Tasks/3455.java
file://<WORKSPACE>/data/code-rep-dataset/Dataset3/Tasks/3455.java
### com.thoughtworks.qdox.parser.ParseException: syntax error @[3,1]

error in qdox parser
file content:
```java
offset: 82
uri: file://<WORKSPACE>/data/code-rep-dataset/Dataset3/Tasks/3455.java
text:
```scala
public class TestAllPredicate extends AbstractTestAnyAllOnePredicate<Integer> {

p@@ackage org.apache.commons.collections.functors;

import org.apache.commons.collections.Predicate;

import static org.apache.commons.collections.functors.AllPredicate.allPredicate;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertTrue;
import org.junit.Test;

import java.util.Collection;
import java.util.Collections;

/**
 * Tests the org.apache.commons.collections.functors.AllPredicate class.
 *
 * @since Commons Collections 3.0
 * @version $Revision: 468603 $ $Date: 2006-10-27 17:52:37 -0700 (Fri, 27 Oct 2006) $
 *
 * @author Edwin Tellman
 */
public class TestAllPredicate extends TestAnyAllOnePredicate<Integer> {

    /**
     * Creates a new <code>TestAllPredicate</code>.
     */
    public TestAllPredicate() {
        super(42);
    }

    /**
     * {@inheritDoc}
     */
    @Override
    protected final Predicate<Integer> getPredicateInstance(final Predicate<? super Integer> ... predicates) {
        return AllPredicate.allPredicate(predicates);
    }

    /**
     * {@inheritDoc}
     */    
    @Override
    protected final Predicate<Integer> getPredicateInstance(final Collection<Predicate<Integer>> predicates) {
        return AllPredicate.allPredicate(predicates);
    }

    /**
     * Verifies that providing an empty predicate array evaluates to true.
     */
    @SuppressWarnings({"unchecked"})
    @Test
    public void emptyArrayToGetInstance() {
        assertTrue("empty array not true", getPredicateInstance(new Predicate[] {}).evaluate(null));
    }

    /**
     * Verifies that providing an empty predicate collection evaluates to true.
     */
    @Test
    public void emptyCollectionToGetInstance() {
        final Predicate<Integer> allPredicate = getPredicateInstance(
                Collections.<Predicate<Integer>>emptyList());
        assertTrue("empty collection not true", allPredicate.evaluate(getTestValue()));
    }

    /**
     * Tests whether a single true predicate evaluates to true.
     */
    @SuppressWarnings("unchecked")
    @Test
    public void oneTruePredicate() {
        // use the constructor directly, as getInstance() returns the original predicate when passed
        // an array of size one.
        final Predicate<Integer> predicate = createMockPredicate(true);
        
        assertTrue("single true predicate evaluated to false",
                allPredicate(predicate).evaluate(getTestValue()));
    }

    /**
     * Tests whether a single false predicate evaluates to true.
     */
    @SuppressWarnings("unchecked")
    @Test
    public void oneFalsePredicate() {
        // use the constructor directly, as getInstance() returns the original predicate when passed
        // an array of size one.
        final Predicate<Integer> predicate = createMockPredicate(false);
        assertFalse("single false predicate evaluated to true",
                allPredicate(predicate).evaluate(getTestValue()));
    }

    /**
     * Tests whether multiple true predicates evaluates to true.
     */
    @Test
    public void allTrue() {
        assertTrue("multiple true predicates evaluated to false",
                getPredicateInstance(true, true).evaluate(getTestValue()));
        assertTrue("multiple true predicates evaluated to false",
                getPredicateInstance(true, true, true).evaluate(getTestValue()));
    }

    /**
     * Tests whether combining some true and one false evalutes to false.  Also verifies that only the first
     * false predicate is actually evaluated
     */
    @Test
    public void trueAndFalseCombined() {
        assertFalse("false predicate evaluated to true",
                getPredicateInstance(false, null).evaluate(getTestValue()));
        assertFalse("false predicate evaluated to true",
                getPredicateInstance(false, null, null).evaluate(getTestValue()));
        assertFalse("false predicate evaluated to true",
                getPredicateInstance(true, false, null).evaluate(getTestValue()));
        assertFalse("false predicate evaluated to true",
                getPredicateInstance(true, true, false).evaluate(getTestValue()));
        assertFalse("false predicate evaluated to true",
                getPredicateInstance(true, true, false, null).evaluate(getTestValue()));
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
	scala.collection.parallel.AdaptiveWorkStealingTasks$AWSTWrappedTask.internal(Tasks.scala:169)
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

QDox parse error in file://<WORKSPACE>/data/code-rep-dataset/Dataset3/Tasks/3455.java