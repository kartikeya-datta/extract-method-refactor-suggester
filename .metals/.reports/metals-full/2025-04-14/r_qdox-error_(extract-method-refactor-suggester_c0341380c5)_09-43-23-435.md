error id: file://<WORKSPACE>/data/code-rep-dataset/Dataset3/Tasks/15072.java
file://<WORKSPACE>/data/code-rep-dataset/Dataset3/Tasks/15072.java
### com.thoughtworks.qdox.parser.ParseException: syntax error @[1,22]

error in qdox parser
file content:
```java
offset: 22
uri: file://<WORKSPACE>/data/code-rep-dataset/Dataset3/Tasks/15072.java
text:
```scala
private static final l@@ong serialVersionUID = -1736147315703444603L;

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
package org.apache.commons.collections.keyvalue;

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.io.Serializable;
import java.util.Arrays;
import java.util.HashMap;
import java.util.Map;

import junit.framework.Assert;
import junit.framework.Test;
import junit.framework.TestCase;
import junit.framework.TestSuite;

/**
 * Unit tests for {@link org.apache.commons.collections.keyvalue.MultiKey}.
 *
 * @version $Revision$ $Date$
 *
 * @author Stephen Colebourne
 */
public class TestMultiKey extends TestCase {

    Integer ONE = new Integer(1);
    Integer TWO = new Integer(2);
    Integer THREE = new Integer(3);
    Integer FOUR = new Integer(4);
    Integer FIVE = new Integer(5);

    public TestMultiKey(String name) {
        super(name);
    }

    public static Test suite() {
        return new TestSuite(TestMultiKey.class);
    }

    public static void main(String[] args) {
        String[] testCaseName = { TestMultiKey.class.getName() };
        junit.textui.TestRunner.main(testCaseName);
    }

    @Override
    public void setUp() throws Exception {
        super.setUp();
    }

    @Override
    protected void tearDown() throws Exception {
        super.tearDown();
    }

    //-----------------------------------------------------------------------
    public void testConstructors() throws Exception {
        MultiKey<Integer> mk = null;
        mk = new MultiKey<Integer>(ONE, TWO);
        Assert.assertTrue(Arrays.equals(new Object[] { ONE, TWO }, mk.getKeys()));

        mk = new MultiKey<Integer>(ONE, TWO, THREE);
        Assert.assertTrue(Arrays.equals(new Object[] { ONE, TWO, THREE }, mk.getKeys()));

        mk = new MultiKey<Integer>(ONE, TWO, THREE, FOUR);
        Assert.assertTrue(Arrays.equals(new Object[] { ONE, TWO, THREE, FOUR }, mk.getKeys()));

        mk = new MultiKey<Integer>(ONE, TWO, THREE, FOUR, FIVE);
        Assert.assertTrue(Arrays.equals(new Object[] { ONE, TWO, THREE, FOUR, FIVE }, mk.getKeys()));

        mk = new MultiKey<Integer>(new Integer[] { THREE, FOUR, ONE, TWO }, false);
        Assert.assertTrue(Arrays.equals(new Object[] { THREE, FOUR, ONE, TWO }, mk.getKeys()));
    }

    public void testConstructorsByArray() throws Exception {
        MultiKey<Integer> mk = null;
        Integer[] keys = new Integer[] { THREE, FOUR, ONE, TWO };
        mk = new MultiKey<Integer>(keys);
        Assert.assertTrue(Arrays.equals(new Object[] { THREE, FOUR, ONE, TWO }, mk.getKeys()));
        keys[3] = FIVE;  // no effect
        Assert.assertTrue(Arrays.equals(new Object[] { THREE, FOUR, ONE, TWO }, mk.getKeys()));

        keys = new Integer[] {};
        mk = new MultiKey<Integer>(keys);
        Assert.assertTrue(Arrays.equals(new Object[] {}, mk.getKeys()));

        keys = new Integer[] { THREE, FOUR, ONE, TWO };
        mk = new MultiKey<Integer>(keys, true);
        Assert.assertTrue(Arrays.equals(new Object[] { THREE, FOUR, ONE, TWO }, mk.getKeys()));
        keys[3] = FIVE;  // no effect
        Assert.assertTrue(Arrays.equals(new Object[] { THREE, FOUR, ONE, TWO }, mk.getKeys()));

        keys = new Integer[] { THREE, FOUR, ONE, TWO };
        mk = new MultiKey<Integer>(keys, false);
        Assert.assertTrue(Arrays.equals(new Object[] { THREE, FOUR, ONE, TWO }, mk.getKeys()));
        // change key - don't do this!
        // the hashcode of the MultiKey is now broken
        keys[3] = FIVE;
        Assert.assertTrue(Arrays.equals(new Object[] { THREE, FOUR, ONE, FIVE }, mk.getKeys()));
    }

    public void testConstructorsByArrayNull() throws Exception {
        Integer[] keys = null;
        try {
            new MultiKey<Integer>(keys);
            fail();
        } catch (IllegalArgumentException ex) {}
        try {
            new MultiKey<Integer>(keys, true);
            fail();
        } catch (IllegalArgumentException ex) {}
        try {
            new MultiKey<Integer>(keys, false);
            fail();
        } catch (IllegalArgumentException ex) {}
    }

    public void testSize() {
        Assert.assertEquals(2, new MultiKey<Integer>(ONE, TWO).size());
        Assert.assertEquals(2, new MultiKey<Object>(null, null).size());
        Assert.assertEquals(3, new MultiKey<Integer>(ONE, TWO, THREE).size());
        Assert.assertEquals(3, new MultiKey<Object>(null, null, null).size());
        Assert.assertEquals(4, new MultiKey<Integer>(ONE, TWO, THREE, FOUR).size());
        Assert.assertEquals(4, new MultiKey<Object>(null, null, null, null).size());
        Assert.assertEquals(5, new MultiKey<Integer>(ONE, TWO, THREE, FOUR, FIVE).size());
        Assert.assertEquals(5, new MultiKey<Object>(null, null, null, null, null).size());

        Assert.assertEquals(0, new MultiKey<Object>(new Object[] {}).size());
        Assert.assertEquals(1, new MultiKey<Integer>(new Integer[] { ONE }).size());
        Assert.assertEquals(2, new MultiKey<Integer>(new Integer[] { ONE, TWO }).size());
        Assert.assertEquals(7, new MultiKey<Integer>(new Integer[] { ONE, TWO, ONE, TWO, ONE, TWO, ONE }).size());
    }

    public void testGetIndexed() {
        MultiKey<Integer> mk = new MultiKey<Integer>(ONE, TWO);
        Assert.assertSame(ONE, mk.getKey(0));
        Assert.assertSame(TWO, mk.getKey(1));
        try {
            mk.getKey(-1);
            fail();
        } catch (IndexOutOfBoundsException ex) {}
        try {
            mk.getKey(2);
            fail();
        } catch (IndexOutOfBoundsException ex) {}
    }

    public void testGetKeysSimpleConstructor() {
        MultiKey<Integer> mk = new MultiKey<Integer>(ONE, TWO);
        Object[] array = mk.getKeys();
        Assert.assertSame(ONE, array[0]);
        Assert.assertSame(TWO, array[1]);
        Assert.assertEquals(2, array.length);
    }

    public void testGetKeysArrayConstructorCloned() {
        Integer[] keys = new Integer[] { ONE, TWO };
        MultiKey<Integer> mk = new MultiKey<Integer>(keys, true);
        Object[] array = mk.getKeys();
        Assert.assertTrue(array != keys);
        Assert.assertTrue(Arrays.equals(array, keys));
        Assert.assertSame(ONE, array[0]);
        Assert.assertSame(TWO, array[1]);
        Assert.assertEquals(2, array.length);
    }

    public void testGetKeysArrayConstructorNonCloned() {
        Integer[] keys = new Integer[] { ONE, TWO };
        MultiKey<Integer> mk = new MultiKey<Integer>(keys, false);
        Object[] array = mk.getKeys();
        Assert.assertTrue(array != keys);  // still not equal
        Assert.assertTrue(Arrays.equals(array, keys));
        Assert.assertSame(ONE, array[0]);
        Assert.assertSame(TWO, array[1]);
        Assert.assertEquals(2, array.length);
    }

    public void testHashCode() {
        MultiKey<Integer> mk1 = new MultiKey<Integer>(ONE, TWO);
        MultiKey<Integer> mk2 = new MultiKey<Integer>(ONE, TWO);
        MultiKey<Object> mk3 = new MultiKey<Object>(ONE, "TWO");

        Assert.assertTrue(mk1.hashCode() == mk1.hashCode());
        Assert.assertTrue(mk1.hashCode() == mk2.hashCode());
        Assert.assertTrue(mk1.hashCode() != mk3.hashCode());

        int total = (0 ^ ONE.hashCode()) ^ TWO.hashCode();
        Assert.assertEquals(total, mk1.hashCode());
    }

    public void testEquals() {
        MultiKey<Integer> mk1 = new MultiKey<Integer>(ONE, TWO);
        MultiKey<Integer> mk2 = new MultiKey<Integer>(ONE, TWO);
        MultiKey<Object> mk3 = new MultiKey<Object>(ONE, "TWO");

        Assert.assertEquals(mk1, mk1);
        Assert.assertEquals(mk1, mk2);
        Assert.assertTrue(mk1.equals(mk3) == false);
        Assert.assertTrue(mk1.equals("") == false);
        Assert.assertTrue(mk1.equals(null) == false);
    }

    static class SystemHashCodeSimulatingKey implements Serializable {

        private static final long serialVersionUID = 1L;
        private final String name;
        private int hashCode = 1;

        public SystemHashCodeSimulatingKey(String name)
        {
            this.name = name;
        }

        @Override
        public boolean equals(Object obj)
        {
            return obj instanceof SystemHashCodeSimulatingKey 
                && name.equals(((SystemHashCodeSimulatingKey)obj).name);
        }

        @Override
        public int hashCode()
        {
            return hashCode;
        }

        private Object readResolve() {
            hashCode=2; // simulate different hashCode after deserialization in another process
            return this;
        }
    }
    
    public void testEqualsAfterSerialization() throws IOException, ClassNotFoundException
    {
        SystemHashCodeSimulatingKey sysKey = new SystemHashCodeSimulatingKey("test");
        MultiKey mk = new MultiKey(ONE, sysKey);
        Map map = new HashMap();
        map.put(mk, TWO);

        // serialize
        ByteArrayOutputStream baos = new ByteArrayOutputStream();
        ObjectOutputStream out = new ObjectOutputStream(baos);
        out.writeObject(sysKey);
        out.writeObject(map);
        out.close();

        // deserialize
        ByteArrayInputStream bais = new ByteArrayInputStream(baos.toByteArray());
        ObjectInputStream in = new ObjectInputStream(bais);
        sysKey = (SystemHashCodeSimulatingKey)in.readObject(); // simulate deserialization in another process
        Map map2 = (Map) in.readObject();
        in.close();

        assertEquals(2, sysKey.hashCode()); // different hashCode now

        MultiKey mk2 = new MultiKey(ONE, sysKey);
        assertEquals(TWO, map2.get(mk2));        
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

QDox parse error in file://<WORKSPACE>/data/code-rep-dataset/Dataset3/Tasks/15072.java