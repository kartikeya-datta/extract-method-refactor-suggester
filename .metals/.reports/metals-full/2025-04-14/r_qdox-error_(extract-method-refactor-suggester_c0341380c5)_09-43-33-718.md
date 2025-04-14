error id: file://<WORKSPACE>/data/code-rep-dataset/Dataset3/Tasks/1315.java
file://<WORKSPACE>/data/code-rep-dataset/Dataset3/Tasks/1315.java
### com.thoughtworks.qdox.parser.ParseException: syntax error @[1,1]

error in qdox parser
file content:
```java
offset: 1
uri: file://<WORKSPACE>/data/code-rep-dataset/Dataset3/Tasks/1315.java
text:
```scala
r@@etval.append(TEST_DATA_PATH);

/*
 * Licensed to the Apache Software Foundation (ASF) under one or more
 * contributor license agreements.  See the NOTICE file distributed with
 * this work for additional information regarding copyright ownership.
 * The ASF licenses this file to You under the Apache License, Version 2.0
 * (the "License"); you may not use this file except in compliance with
 * the License.  You may obtain a copy of the License at
 *
 *      http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */
package org.apache.commons.collections.comparators;

import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.Serializable;
import java.util.Collections;
import java.util.Comparator;
import java.util.LinkedList;
import java.util.List;

import org.apache.commons.collections.AbstractTestObject;

/**
 * Abstract test class for testing the Comparator interface.
 * <p>
 * Concrete subclasses declare the comparator to be tested.
 * They also declare certain aspects of the tests.
 *
 * @author Stephen Colebourne
 */
public abstract class AbstractTestComparator<T> extends AbstractTestObject {

    /**
     * JUnit constructor.
     * 
     * @param testName  the test class name
     */
    public AbstractTestComparator(String testName) {
        super(testName);
    }

    //-----------------------------------------------------------------------
    
    /**
     * Implement this method to return a list of sorted objects.
     * 
     * @return sorted objects
     */
    public abstract List<T> getComparableObjectsOrdered();

    //-----------------------------------------------------------------------
    /**
     * Implements the abstract superclass method to return the comparator.
     * 
     * @return a full iterator
     */
    @Override
    public abstract Comparator<T> makeObject();

    /**
     * Overrides superclass to block tests.
     */
    @Override
    public boolean supportsEmptyCollections() {
        return false;
    }

    /**
     * Overrides superclass to block tests.
     */
    @Override
    public boolean supportsFullCollections() {
        return false;
    }

    /**
     * Overrides superclass to set the compatability to version 2
     * as there were no Comparators in version 1.x.
     */
    @Override
    public String getCompatibilityVersion() {
        return "2";
    }

    //-----------------------------------------------------------------------
    /**
     * Reverse the list.
     */
    protected void reverseObjects(List<?> list) {
        Collections.reverse(list);
    }

    /**
     * Randomize the list.
     */
    protected void randomizeObjects(List<?> list) {
        Collections.shuffle(list);
    }

    /**
     * Sort the list.
     */
    protected void sortObjects(List<T> list, Comparator<? super T> comparator) {
        Collections.sort(list, comparator);
    }

    //-----------------------------------------------------------------------
    /**
     * Test sorting an empty list
     */
    public void testEmptyListSort() {
        List<T> list = new LinkedList<T>();
        sortObjects(list, makeObject());

        List<T> list2 = new LinkedList<T>();

        assertTrue("Comparator cannot sort empty lists", list2.equals(list));
    }

    /**
     * Test sorting a reversed list.
     */
    public void testReverseListSort() {
        Comparator<T> comparator = makeObject();

        List<T> randomList = getComparableObjectsOrdered();
        reverseObjects(randomList);
        sortObjects(randomList, comparator);

        List<T> orderedList = getComparableObjectsOrdered();

        assertTrue("Comparator did not reorder the List correctly",
                   orderedList.equals(randomList));
    }

    /**
     * Test sorting a random list.
     */
    public void testRandomListSort() {
        Comparator<T> comparator = makeObject();

        List<T> randomList = getComparableObjectsOrdered();
        randomizeObjects(randomList);
        sortObjects(randomList,comparator);

        List<T> orderedList = getComparableObjectsOrdered();

        /* debug 
        Iterator i = randomList.iterator();
        while (i.hasNext()) {
            System.out.println(i.next());
        }
        */

        assertTrue("Comparator did not reorder the List correctly",
                   orderedList.equals(randomList));

    }

    /**
     * Nearly all Comparators should be Serializable.
     */
    public void testComparatorIsSerializable() {
        Comparator<T> comparator = makeObject();
        assertTrue("This comparator should be Serializable.",
                   comparator instanceof Serializable);
    }

    public String getCanonicalComparatorName(Object object) {
        StringBuilder retval = new StringBuilder();
        retval.append("data/test/");
        String colName = object.getClass().getName();
        colName = colName.substring(colName.lastIndexOf(".")+1,colName.length());
        retval.append(colName);
        retval.append(".version");
        retval.append(getCompatibilityVersion());
        retval.append(".obj");
        return retval.toString();
    }

    /**
     * Compare the current serialized form of the Comparator
     * against the canonical version in SVN.
     */
    @SuppressWarnings("unchecked")
    public void testComparatorCompatibility() throws IOException, ClassNotFoundException {
        if (!skipSerializedCanonicalTests()) {
            Comparator<T> comparator = null;
    
            // test to make sure the canonical form has been preserved
            try {
                comparator = (Comparator<T>) readExternalFormFromDisk(getCanonicalComparatorName(makeObject()));
            } catch (FileNotFoundException exception) {
    
                boolean autoCreateSerialized = false;
    
                if (autoCreateSerialized) {
                      comparator = makeObject();
                    String fileName = getCanonicalComparatorName(comparator);
                    writeExternalFormToDisk((Serializable) comparator, fileName);
                    fail("Serialized form could not be found.  A serialized version "
                            + "has now been written (and should be added to CVS): " + fileName);
                } else {
                    fail("The Serialized form could be located to test serialization "
                            + "compatibility: " + exception.getMessage());
                }
            }
    
            
            // make sure the canonical form produces the ordering we currently
            // expect
            List<T> randomList = getComparableObjectsOrdered();
            reverseObjects(randomList);
            sortObjects(randomList, comparator);
    
            List<T> orderedList = getComparableObjectsOrdered();
    
            assertTrue("Comparator did not reorder the List correctly",
                       orderedList.equals(randomList));
        }
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

QDox parse error in file://<WORKSPACE>/data/code-rep-dataset/Dataset3/Tasks/1315.java