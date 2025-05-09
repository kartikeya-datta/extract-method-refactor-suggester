error id: file://<WORKSPACE>/data/code-rep-dataset/Dataset3/Tasks/7056.java
file://<WORKSPACE>/data/code-rep-dataset/Dataset3/Tasks/7056.java
### com.thoughtworks.qdox.parser.ParseException: syntax error @[1,1]

error in qdox parser
file content:
```java
offset: 1
uri: file://<WORKSPACE>/data/code-rep-dataset/Dataset3/Tasks/7056.java
text:
```scala
r@@eturn con.newInstance(new Object[] { m.getName() });

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
package org.apache.commons.collections;

import java.lang.reflect.Constructor;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.lang.reflect.Modifier;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import junit.framework.TestCase;
import junit.framework.TestSuite;

/**
 * A {@link TestCase} that can define both simple and bulk test methods.
 * <p>
 * A <I>simple test method</I> is the type of test traditionally 
 * supplied by by {@link TestCase}.  To define a simple test, create a public 
 * no-argument method whose name starts with "test".  You can specify the
 * the name of simple test in the constructor of <code>BulkTest</code>;
 * a subsequent call to {@link TestCase#run} will run that simple test.
 * <p>
 * A <I>bulk test method</I>, on the other hand, returns a new instance
 * of <code>BulkTest</code>, which can itself define new simple and bulk
 * test methods.  By using the {@link #makeSuite} method, you can 
 * automatically create a hierarchal suite of tests and child bulk tests.
 * <p>
 * For instance, consider the following two classes:
 *
 * <Pre>
 *  public class TestSet extends BulkTest {
 *
 *      private Set set;
 *
 *      public TestSet(Set set) {
 *          this.set = set;
 *      }
 *
 *      public void testContains() {
 *          boolean r = set.contains(set.iterator().next()));
 *          assertTrue("Set should contain first element, r);
 *      }
 *
 *      public void testClear() {
 *          set.clear();
 *          assertTrue("Set should be empty after clear", set.isEmpty());
 *      }
 *  }
 *
 *
 *  public class TestHashMap extends BulkTest {
 *
 *      private Map makeFullMap() {
 *          HashMap result = new HashMap();
 *          result.put("1", "One");
 *          result.put("2", "Two");
 *          return result;
 *      }
 *
 *      public void testClear() {
 *          Map map = makeFullMap();
 *          map.clear();
 *          assertTrue("Map empty after clear", map.isEmpty());
 *      }
 *
 *      public BulkTest bulkTestKeySet() {
 *          return new TestSet(makeFullMap().keySet());
 *      }
 *
 *      public BulkTest bulkTestEntrySet() {
 *          return new TestSet(makeFullMap().entrySet());
 *      }
 *  }
 *  </Pre>
 *
 *  In the above examples, <code>TestSet</code> defines two
 *  simple test methods and no bulk test methods; <code>TestHashMap</code>
 *  defines one simple test method and two bulk test methods.  When
 *  <code>makeSuite(TestHashMap.class).run</code> is executed, 
 *  <I>five</I> simple test methods will be run, in this order:<P>
 *
 *  <Ol>
 *  <Li>TestHashMap.testClear()
 *  <Li>TestHashMap.bulkTestKeySet().testContains();
 *  <Li>TestHashMap.bulkTestKeySet().testClear();
 *  <Li>TestHashMap.bulkTestEntrySet().testContains();
 *  <Li>TestHashMap.bulkTestEntrySet().testClear();
 *  </Ol>
 *
 *  In the graphical junit test runners, the tests would be displayed in
 *  the following tree:<P>
 *
 *  <UL>
 *  <LI>TestHashMap</LI>
 *      <UL>
 *      <LI>testClear
 *      <LI>bulkTestKeySet
 *          <UL>
 *          <LI>testContains
 *          <LI>testClear
 *          </UL>
 *      <LI>bulkTestEntrySet
 *          <UL>
 *          <LI>testContains
 *          <LI>testClear
 *          </UL>
 *      </UL>
 *  </UL>
 *
 *  A subclass can override a superclass's bulk test by
 *  returning <code>null</code> from the bulk test method.  If you only
 *  want to override specific simple tests within a bulk test, use the
 *  {@link #ignoredTests} method.<P>
 *
 *  Note that if you want to use the bulk test methods, you <I>must</I>
 *  define your <code>suite()</code> method to use {@link #makeSuite}.
 *  The ordinary {@link TestSuite} constructor doesn't know how to 
 *  interpret bulk test methods.
 *
 *  @author Paul Jack
 *  @version $Id$
 */
public class BulkTest extends TestCase implements Cloneable {


    // Note:  BulkTest is Cloneable to make it easier to construct 
    // BulkTest instances for simple test methods that are defined in 
    // anonymous inner classes.  Basically we don't have to worry about
    // finding weird constructors.  (And even if we found them, technically
    // it'd be illegal for anyone but the outer class to invoke them).  
    // Given one BulkTest instance, we can just clone it and reset the 
    // method name for every simple test it defines.  


    /**
     *  The full name of this bulk test instance.  This is the full name
     *  that is compared to {@link #ignoredTests} to see if this
     *  test should be ignored.  It's also displayed in the text runner
     *  to ease debugging.
     */
    String verboseName;


    /**
     *  Constructs a new <code>BulkTest</code> instance that will run the
     *  specified simple test.
     *
     *  @param name  the name of the simple test method to run
     */
    public BulkTest(String name) {
        super(name);
        this.verboseName = getClass().getName();
    }


    /**
     *  Creates a clone of this <code>BulkTest</code>.<P>
     *
     *  @return  a clone of this <code>BulkTest</code>
     */
    @Override
    public Object clone() {
        try {
            return super.clone();
        } catch (CloneNotSupportedException e) {
            throw new Error(); // should never happen
        }
    }


    /**
     *  Returns an array of test names to ignore.<P>
     *
     *  If a test that's defined by this <code>BulkTest</code> or
     *  by one of its bulk test methods has a name that's in the returned
     *  array, then that simple test will not be executed.<P>
     *
     *  A test's name is formed by taking the class name of the
     *  root <code>BulkTest</code>, eliminating the package name, then
     *  appending the names of any bulk test methods that were invoked
     *  to get to the simple test, and then appending the simple test
     *  method name.  The method names are delimited by periods:
     *
     *  <pre>
     *  TestHashMap.bulkTestEntrySet.testClear
     *  </pre>
     *
     *  is the name of one of the simple tests defined in the sample classes
     *  described above.  If the sample <code>TestHashMap</code> class
     *  included this method:
     *
     *  <pre>
     *  public String[] ignoredTests() {
     *      return new String[] { "TestHashMap.bulkTestEntrySet.testClear" };
     *  }
     *  </pre>
     *
     *  then the entry set's clear method wouldn't be tested, but the key
     *  set's clear method would.
     *
     *  @return an array of the names of tests to ignore, or null if
     *   no tests should be ignored
     */
    public String[] ignoredTests() {
        return null;
    }


    /**
     *  Returns the display name of this <code>BulkTest</code>.
     *
     *  @return the display name of this <code>BulkTest</code>
     */
    @Override
    public String toString() {
        return getName() + "(" + verboseName + ") ";
    }


    /**
     *  Returns a {@link TestSuite} for testing all of the simple tests
     *  <I>and</I> all the bulk tests defined by the given class.<P>
     *
     *  The class is examined for simple and bulk test methods; any child
     *  bulk tests are also examined recursively; and the results are stored
     *  in a hierarchal {@link TestSuite}.<P>
     *
     *  The given class must be a subclass of <code>BulkTest</code> and must
     *  not be abstract.<P>
     *
     *  @param c  the class to examine for simple and bulk tests
     *  @return  a {@link TestSuite} containing all the simple and bulk tests
     *    defined by that class
     */
    public static TestSuite makeSuite(Class<? extends BulkTest> c) {
        if (Modifier.isAbstract(c.getModifiers())) {
            throw new IllegalArgumentException("Class must not be abstract.");
        }
        if (!BulkTest.class.isAssignableFrom(c)) {
            throw new IllegalArgumentException("Class must extend BulkTest.");
        }
        return new BulkTestSuiteMaker(c).make();
    }

}


// It was easier to use a separate class to do all the reflection stuff
// for making the TestSuite instances.  Having permanent state around makes
// it easier to handle the recursion.
class BulkTestSuiteMaker {

    /** The class that defines simple and bulk tests methods. */
    private Class<? extends BulkTest> startingClass;

    /** List of ignored simple test names. */
    private List<String> ignored;
   
    /** The TestSuite we're currently populating.  Can change over time. */
    private TestSuite result;

    /** 
     *  The prefix for simple test methods.  Used to check if a test is in 
     *  the ignored list.
     */ 
    private String prefix;

    /** 
     *  Constructor.
     *
     *  @param startingClass  the starting class
     */     
    public BulkTestSuiteMaker(Class<? extends BulkTest> startingClass) {
        this.startingClass = startingClass;
    }

    /**
     *  Makes a hierarchal TestSuite based on the starting class.
     *
     *  @return  the hierarchal TestSuite for startingClass
     */
    public TestSuite make() {
         this.result = new TestSuite();
         this.prefix = getBaseName(startingClass);
         result.setName(prefix);

         BulkTest bulk = makeFirstTestCase(startingClass);
         ignored = new ArrayList<String>();
         String[] s = bulk.ignoredTests();
         if (s != null) {
             ignored.addAll(Arrays.asList(s));
         }
         make(bulk);
         return result;
    }

    /**
     *  Appends all the simple tests and bulk tests defined by the given
     *  instance's class to the current TestSuite.
     *
     *  @param bulk  An instance of the class that defines simple and bulk
     *    tests for us to append
     */
    void make(BulkTest bulk) {
        Class<? extends BulkTest> c = bulk.getClass();
        Method[] all = c.getMethods();
        for (int i = 0; i < all.length; i++) {
            if (isTest(all[i])) addTest(bulk, all[i]);
            if (isBulk(all[i])) addBulk(bulk, all[i]);
        }
    }

    /**
     *  Adds the simple test defined by the given method to the TestSuite.
     *
     *  @param bulk  The instance of the class that defined the method
     *   (I know it's weird.  But the point is, we can clone the instance
     *   and not have to worry about constructors.)
     *  @param m  The simple test method
     */
    void addTest(BulkTest bulk, Method m) {
        BulkTest bulk2 = (BulkTest)bulk.clone();
        bulk2.setName(m.getName());
        bulk2.verboseName = prefix + "." + m.getName();
        if (ignored.contains(bulk2.verboseName)) return;
        result.addTest(bulk2);
    }

    /**
     *  Adds a whole new suite of tests that are defined by the result of
     *  the given bulk test method.  In other words, the given bulk test
     *  method is invoked, and the resulting BulkTest instance is examined
     *  for yet more simple and bulk tests.
     *
     *  @param bulk  The instance of the class that defined the method
     *  @param m  The bulk test method
     */
    void addBulk(BulkTest bulk, Method m) {
        String verboseName = prefix + "." + m.getName();
        if (ignored.contains(verboseName)) return;
        
        BulkTest bulk2;
        try {
            bulk2 = (BulkTest)m.invoke(bulk, (Object[]) null);
            if (bulk2 == null) return;
        } catch (InvocationTargetException ex) {
            ex.getTargetException().printStackTrace();
            throw new Error(); // FIXME;
        } catch (IllegalAccessException ex) {
            ex.printStackTrace();
            throw new Error(); // FIXME;
        }

        // Save current state on the stack.
        String oldPrefix = prefix;
        TestSuite oldResult = result;

        prefix = prefix + "." + m.getName();
        result = new TestSuite();
        result.setName(m.getName());

        make(bulk2);

        oldResult.addTest(result);

        // Restore the old state
        prefix = oldPrefix;
        result = oldResult;
    }

    /**
     *  Returns the base name of the given class.
     *
     *  @param c  the class
     *  @return the name of that class, minus any package names
     */
    private static String getBaseName(Class<?> c) {
        String name = c.getName();
        int p = name.lastIndexOf('.');
        if (p > 0) {
            name = name.substring(p + 1);
        }
        return name;
    }


    // These three methods are used to create a valid BulkTest instance
    // from a class.

    private static <T> Constructor<T> getTestCaseConstructor(Class<T> c) {
        try {
            return c.getConstructor(new Class[] { String.class });
        } catch (NoSuchMethodException e) {
            throw new IllegalArgumentException(c + " must provide " +
             "a (String) constructor");
        }
    }

    private static <T extends BulkTest> BulkTest makeTestCase(Class<T> c, Method m) {
        Constructor<T> con = getTestCaseConstructor(c);
        try {
            return (BulkTest) con.newInstance(new Object[] { m.getName() });
        } catch (InvocationTargetException e) {
            e.printStackTrace();
            throw new RuntimeException(); // FIXME;
        } catch (IllegalAccessException e) {
            throw new Error(); // should never occur
        } catch (InstantiationException e) {
            throw new RuntimeException(); // FIXME;
        }
    }

    private static <T extends BulkTest> BulkTest makeFirstTestCase(Class<T> c) {
        Method[] all = c.getMethods();
        for (int i = 0; i < all.length; i++) {
            if (isTest(all[i])) return makeTestCase(c, all[i]);
        }
        throw new IllegalArgumentException(c.getName() + " must provide " 
          + " at least one test method.");
    }

    /**
     *  Returns true if the given method is a simple test method.
     */
    private static boolean isTest(Method m) {
        if (!m.getName().startsWith("test")) return false;
        if (m.getReturnType() != Void.TYPE) return false;
        if (m.getParameterTypes().length != 0) return false;
        int mods = m.getModifiers();
        if (Modifier.isStatic(mods)) return false;
        if (Modifier.isAbstract(mods)) return false;
        return true;
    }

    /**
     *  Returns true if the given method is a bulk test method.
     */
    private static boolean isBulk(Method m) {
        if (!m.getName().startsWith("bulkTest")) return false;
        if (m.getReturnType() != BulkTest.class) return false;
        if (m.getParameterTypes().length != 0) return false;
        int mods = m.getModifiers();
        if (Modifier.isStatic(mods)) return false;
        if (Modifier.isAbstract(mods)) return false;
        return true;
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

QDox parse error in file://<WORKSPACE>/data/code-rep-dataset/Dataset3/Tasks/7056.java