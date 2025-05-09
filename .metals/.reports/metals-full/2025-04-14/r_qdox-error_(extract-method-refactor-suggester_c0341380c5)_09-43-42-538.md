error id: file://<WORKSPACE>/data/code-rep-dataset/Dataset3/Tasks/17.java
file://<WORKSPACE>/data/code-rep-dataset/Dataset3/Tasks/17.java
### com.thoughtworks.qdox.parser.ParseException: syntax error @[1,1]

error in qdox parser
file content:
```java
offset: 1
uri: file://<WORKSPACE>/data/code-rep-dataset/Dataset3/Tasks/17.java
text:
```scala
a@@ssertEquals("Cloned entry does not match original",

package org.apache.commons.collections;

/* ====================================================================
 * The Apache Software License, Version 1.1
 *
 * Copyright (c) 2001-2002 The Apache Software Foundation.  All rights
 * reserved.
 *
 * Redistribution and use in source and binary forms, with or without
 * modification, are permitted provided that the following conditions
 * are met:
 *
 * 1. Redistributions of source code must retain the above copyright
 *    notice, this list of conditions and the following disclaimer.
 *
 * 2. Redistributions in binary form must reproduce the above copyright
 *    notice, this list of conditions and the following disclaimer in
 *    the documentation and/or other materials provided with the
 *    distribution.
 *
 * 3. The end-user documentation included with the redistribution,
 *    if any, must include the following acknowledgment:
 *       "This product includes software developed by the
 *        Apache Software Foundation (http://www.apache.org/)."
 *    Alternately, this acknowledgment may appear in the software itself,
 *    if and wherever such third-party acknowledgments normally appear.
 *
 * 4. The names "Apache" and "Apache Software Foundation" and
 *    "Apache Turbine" must not be used to endorse or promote products
 *    derived from this software without prior written permission. For
 *    written permission, please contact apache@apache.org.
 *
 * 5. Products derived from this software may not be called "Apache",
 *    "Apache Turbine", nor may "Apache" appear in their name, without
 *    prior written permission of the Apache Software Foundation.
 *
 * THIS SOFTWARE IS PROVIDED ``AS IS'' AND ANY EXPRESSED OR IMPLIED
 * WARRANTIES, INCLUDING, BUT NOT LIMITED TO, THE IMPLIED WARRANTIES
 * OF MERCHANTABILITY AND FITNESS FOR A PARTICULAR PURPOSE ARE
 * DISCLAIMED.  IN NO EVENT SHALL THE APACHE SOFTWARE FOUNDATION OR
 * ITS CONTRIBUTORS BE LIABLE FOR ANY DIRECT, INDIRECT, INCIDENTAL,
 * SPECIAL, EXEMPLARY, OR CONSEQUENTIAL DAMAGES (INCLUDING, BUT NOT
 * LIMITED TO, PROCUREMENT OF SUBSTITUTE GOODS OR SERVICES; LOSS OF
 * USE, DATA, OR PROFITS; OR BUSINESS INTERRUPTION) HOWEVER CAUSED AND
 * ON ANY THEORY OF LIABILITY, WHETHER IN CONTRACT, STRICT LIABILITY,
 * OR TORT (INCLUDING NEGLIGENCE OR OTHERWISE) ARISING IN ANY WAY OUT
 * OF THE USE OF THIS SOFTWARE, EVEN IF ADVISED OF THE POSSIBILITY OF
 * SUCH DAMAGE.
 * ====================================================================
 *
 * This software consists of voluntary contributions made by many
 * individuals on behalf of the Apache Software Foundation.  For more
 * information on the Apache Software Foundation, please see
 * <http://www.apache.org/>.
 */

import java.io.IOException;
import java.io.Serializable;
import java.util.Iterator;
import java.util.List;
import java.util.Map;

import junit.framework.Test;

/**
 * Unit tests 
 * {@link org.apache.commons.collections.SequencedHashMap}.
 * Be sure to use the "labRat" instance whenever possible,
 * so that subclasses will be tested correctly.
 * 
 * @author <a href="mailto:morgand@apache.org">Morgan Delagrange</a>
 * @author <a href="mailto:dlr@collab.net">Daniel Rall</a>
 * @author <a href="mailto:hps@intermeta.de">Henning P. Schmiedehausen</a>
 * @author <a href="mailto:jstrachan@apache.org">James Strachan</a>
 */
public class TestSequencedHashMap extends TestMap {
    /**
     * The instance to experiment on.
     */
    protected SequencedHashMap labRat;

    public TestSequencedHashMap(String name) {
        super(name);
    }

    public static Test suite() {
        return BulkTest.makeSuite(TestSequencedHashMap.class);
    }

    // current versions of SequencedHashMap and subclasses are not
    // compatible with Collections 1.x
    public String getCompatibilityVersion() {
        return "2";
    }

    public static void main(String[] args) {
        String[] testCaseName = { TestSequencedHashMap.class.getName() };
        junit.textui.TestRunner.main(testCaseName);
    }

    public void setUp() throws Exception {
        super.setUp();
        // use makeMap and cast the result to a SeqHashMap
        // so that subclasses of SeqHashMap can share these tests
        labRat = (SequencedHashMap) makeEmptyMap();
    }

    public Map makeEmptyMap() {
        return new SequencedHashMap();
    }

    protected Object[] getKeys() {
        return new Object[] { "foo", "baz", "eek" };
    }

    protected Object[] getValues() {
        return new Object[] { "bar", "frob", new Object() };
    }
 
    public void testSequenceMap() throws Throwable {
        Object[] keys = getKeys();
        int expectedSize = keys.length;
        Object[] values = getValues();
        for (int i = 0; i < expectedSize; i++) {
            labRat.put(keys[i], values[i]);
        }

        // Test size().
        assertEquals("size() does not match expected size",
                     expectedSize, labRat.size());

        // Test clone(), iterator(), and get(Object).
        SequencedHashMap clone = (SequencedHashMap) labRat.clone();
        assertEquals("Size of clone does not match original",
                     labRat.size(), clone.size());
        Iterator origEntries = labRat.entrySet().iterator();
        Iterator copiedEntries = clone.entrySet().iterator();
        while (origEntries.hasNext()) {
            Map.Entry origEntry = (Map.Entry)origEntries.next();
            Map.Entry copiedEntry = (Map.Entry)copiedEntries.next();
            assertEquals("Cloned key does not match original",
                         origEntry.getKey(), copiedEntry.getKey());
            assertEquals("Cloned value does not match original",
                         origEntry.getValue(), copiedEntry.getValue());
            assertEquals("Cloned entry does not match orginal",
                         origEntry, copiedEntry);
        }
        assertTrue("iterator() returned different number of elements than keys()",
               !copiedEntries.hasNext());

        // Test sequence()
        List seq = labRat.sequence();
        assertEquals("sequence() returns more keys than in the Map",
                     expectedSize, seq.size());

        for (int i = 0; i < seq.size(); i++) {
            assertEquals("Key " + i + " is not the same as the key in the Map",
                         keys[i], seq.get(i));
        }
    }

    public void testYoungest() {
        labRat.put(new Integer(1),"foo");
        labRat.put(new Integer(2),"bar");
        assertTrue("first key is correct",labRat.get(0).equals(new Integer(1)));
        labRat.put(new Integer(1),"boo");
        assertTrue("second key is reassigned to first",labRat.get(0).equals(new Integer(2)));
    }

    public void testYoungestReplaceNullWithValue() {
        labRat.put(new Integer(1),null);
        labRat.put(new Integer(2),"foo");
        assertTrue("first key is correct",labRat.get(0).equals(new Integer(1)));
        labRat.put(new Integer(1),"bar");
        assertTrue("second key is reassigned to first",labRat.get(0).equals(new Integer(2)));
    }

    public void testYoungestReplaceValueWithNull() {
        labRat.put(new Integer(1),"bar");
        labRat.put(new Integer(2),"foo");
        assertTrue("first key is correct",labRat.get(0).equals(new Integer(1)));
        labRat.put(new Integer(1),null);
        assertTrue("second key is reassigned to first",labRat.get(0).equals(new Integer(2)));
    }

    // override TestMap method with more specific tests
    public void testFullMapSerialization() 
    throws IOException, ClassNotFoundException {
        SequencedHashMap map = (SequencedHashMap) makeFullMap();

        if (!(map instanceof Serializable)) return;  

        byte[] objekt = writeExternalFormToBytes((Serializable) map);
        SequencedHashMap map2 = (SequencedHashMap) readExternalFormFromBytes(objekt);

        assertEquals("Both maps are same size",map.size(), getSampleKeys().length);
        assertEquals("Both maps are same size",map2.size(),getSampleKeys().length);

        assertEquals("Both maps have the same first key",
                     map.getFirstKey(),getSampleKeys()[0]);
        assertEquals("Both maps have the same first key",
                     map2.getFirstKey(),getSampleKeys()[0]);
        assertEquals("Both maps have the same last key",
                     map.getLastKey(),getSampleKeys()[getSampleKeys().length - 1]);
        assertEquals("Both maps have the same last key",
                     map2.getLastKey(),getSampleKeys()[getSampleKeys().length - 1]);
    }

    public void testIndexOf() throws Exception {
        Object[] keys = getKeys();
        int expectedSize = keys.length;
        Object[] values = getValues();
        for (int i = 0; i < expectedSize; i++) {
            labRat.put(keys[i], values[i]);
        }
        // test that the index returned are in the same order that they were 
        // placed in the map
        for (int i = 0; i < keys.length; i++) {
            assertEquals("indexOf with existing key failed", i, labRat.indexOf(keys[i]));
        }
        // test non existing key..
        assertEquals("test with non-existing key failed", -1, labRat.indexOf("NonExistingKey"));
    }
    
    protected void tearDown() throws Exception {
        labRat = null;
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

QDox parse error in file://<WORKSPACE>/data/code-rep-dataset/Dataset3/Tasks/17.java