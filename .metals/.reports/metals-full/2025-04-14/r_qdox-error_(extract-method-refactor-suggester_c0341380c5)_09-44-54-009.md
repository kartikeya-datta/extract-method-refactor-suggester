error id: file://<WORKSPACE>/data/code-rep-dataset/Dataset3/Tasks/13079.java
file://<WORKSPACE>/data/code-rep-dataset/Dataset3/Tasks/13079.java
### com.thoughtworks.qdox.parser.ParseException: syntax error @[1,1]

error in qdox parser
file content:
```java
offset: 1
uri: file://<WORKSPACE>/data/code-rep-dataset/Dataset3/Tasks/13079.java
text:
```scala
C@@onstructor<?>[] cons = RandomStringUtils.class.getDeclaredConstructors();

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
package org.apache.commons.lang;

import java.lang.reflect.Constructor;
import java.lang.reflect.Modifier;
import java.util.Random;

import junit.framework.Test;
import junit.framework.TestSuite;
import junit.textui.TestRunner;

/**
 * Unit tests {@link org.apache.commons.lang.RandomStringUtils}.
 *
 * @author <a href="mailto:steven@caswell.name">Steven Caswell</a>
 * @author <a href="mailto:ridesmet@users.sourceforge.net">Ringo De Smet</a>
 * @author Phil Steitz
 * @version $Id$
 */
public class RandomStringUtilsTest extends junit.framework.TestCase {
    /**
     * Construct a new instance of RandomStringUtilsTest with the specified name
     */
    public RandomStringUtilsTest(String name) {
        super(name);
    }

    public static Test suite() {
        TestSuite suite = new TestSuite(RandomStringUtilsTest.class);
        suite.setName("RandomStringUtils Tests");
        return suite;
    }
    
    /**
     * Set up instance variables required by this test case.
     */
    @Override
    public void setUp() {
    }
    
    /**
     * Tear down instance variables required by this test case.
     */
    @Override
    public void tearDown() {
    }
    
    //-----------------------------------------------------------------------
    public void testConstructor() {
        assertNotNull(new RandomStringUtils());
        Constructor[] cons = RandomStringUtils.class.getDeclaredConstructors();
        assertEquals(1, cons.length);
        assertEquals(true, Modifier.isPublic(cons[0].getModifiers()));
        assertEquals(true, Modifier.isPublic(RandomStringUtils.class.getModifiers()));
        assertEquals(false, Modifier.isFinal(RandomStringUtils.class.getModifiers()));
    }
    
    //-----------------------------------------------------------------------
    /**
     * Test the implementation
     */
    public void testRandomStringUtils() {
        String r1 = RandomStringUtils.random(50);
        assertEquals("random(50) length", 50, r1.length());
        String r2 = RandomStringUtils.random(50);
        assertEquals("random(50) length", 50, r2.length());
        assertTrue("!r1.equals(r2)", !r1.equals(r2));
        
        r1 = RandomStringUtils.randomAscii(50);
        assertEquals("randomAscii(50) length", 50, r1.length());
        for(int i = 0; i < r1.length(); i++) {
            assertTrue("char between 32 and 127", r1.charAt(i) >= 32 && r1.charAt(i) <= 127);
        }        
        r2 = RandomStringUtils.randomAscii(50);
        assertTrue("!r1.equals(r2)", !r1.equals(r2));

        r1 = RandomStringUtils.randomAlphabetic(50);
        assertEquals("randomAlphabetic(50)", 50, r1.length());
        for(int i = 0; i < r1.length(); i++) {
            assertEquals("r1 contains alphabetic", true, Character.isLetter(r1.charAt(i)) && !Character.isDigit(r1.charAt(i)));
        }
        r2 = RandomStringUtils.randomAlphabetic(50);
        assertTrue("!r1.equals(r2)", !r1.equals(r2));
        
        r1 = RandomStringUtils.randomAlphanumeric(50);
        assertEquals("randomAlphanumeric(50)", 50, r1.length());
        for(int i = 0; i < r1.length(); i++) {
            assertEquals("r1 contains alphanumeric", true, Character.isLetterOrDigit(r1.charAt(i)));
        }
        r2 = RandomStringUtils.randomAlphabetic(50);
        assertTrue("!r1.equals(r2)", !r1.equals(r2));
        
        r1 = RandomStringUtils.randomNumeric(50);
        assertEquals("randomNumeric(50)", 50, r1.length());
        for(int i = 0; i < r1.length(); i++) {
            assertEquals("r1 contains numeric", true, Character.isDigit(r1.charAt(i)) && !Character.isLetter(r1.charAt(i)));
        }
        r2 = RandomStringUtils.randomNumeric(50);
        assertTrue("!r1.equals(r2)", !r1.equals(r2));
        
        String set = "abcdefg";
        r1 = RandomStringUtils.random(50, set);
        assertEquals("random(50, \"abcdefg\")", 50, r1.length());
        for(int i = 0; i < r1.length(); i++) {
            assertTrue("random char in set", set.indexOf(r1.charAt(i)) > -1);
        }
        r2 = RandomStringUtils.random(50, set);
        assertTrue("!r1.equals(r2)", !r1.equals(r2));
        
        r1 = RandomStringUtils.random(50, (String) null);
        assertEquals("random(50) length", 50, r1.length());
        r2 = RandomStringUtils.random(50, (String) null);
        assertEquals("random(50) length", 50, r2.length());
        assertTrue("!r1.equals(r2)", !r1.equals(r2));
        
        set = "stuvwxyz";
        r1 = RandomStringUtils.random(50, set.toCharArray());
        assertEquals("random(50, \"stuvwxyz\")", 50, r1.length());
        for(int i = 0; i < r1.length(); i++) {
            assertTrue("random char in set", set.indexOf(r1.charAt(i)) > -1);
        }
        r2 = RandomStringUtils.random(50, set);
        assertTrue("!r1.equals(r2)", !r1.equals(r2));
        
        r1 = RandomStringUtils.random(50, (char[]) null);
        assertEquals("random(50) length", 50, r1.length());
        r2 = RandomStringUtils.random(50, (char[]) null);
        assertEquals("random(50) length", 50, r2.length());
        assertTrue("!r1.equals(r2)", !r1.equals(r2));

        long seed = System.currentTimeMillis();
        r1 = RandomStringUtils.random(50,0,0,true,true,null,new Random(seed));
        r2 = RandomStringUtils.random(50,0,0,true,true,null,new Random(seed));
        assertEquals("r1.equals(r2)", r1, r2);

        r1 = RandomStringUtils.random(0);
        assertEquals("random(0).equals(\"\")", "", r1);

    }
    public void testExceptions() {
        try {
            RandomStringUtils.random(-1);
            fail();
        } catch (IllegalArgumentException ex) {}
        try {
            RandomStringUtils.random(-1, true, true);
            fail();
        } catch (IllegalArgumentException ex) {}
        try {
            RandomStringUtils.random(-1, new char[0]);
            fail();
        } catch (IllegalArgumentException ex) {}
        try {
            RandomStringUtils.random(-1, "");
            fail();
        } catch (IllegalArgumentException ex) {}
        try {
            RandomStringUtils.random(-1, 'a', 'z', false, false);
            fail();
        } catch (IllegalArgumentException ex) {}
        try {
            RandomStringUtils.random(-1, 'a', 'z', false, false, new char[0]);
            fail();
        } catch (IllegalArgumentException ex) {}
        try {
            RandomStringUtils.random(-1, 'a', 'z', false, false, new char[0], new Random());
            fail();
        } catch (IllegalArgumentException ex) {}
    }
    
    /**
     * Make sure boundary alphanumeric characters are generated by randomAlphaNumeric
     * This test will fail randomly with probability = 6 * (61/62)**1000 ~ 5.2E-7
     */  
    public void testRandomAlphaNumeric() {
        char[] testChars = {'a', 'z', 'A', 'Z', '0', '9'};
        boolean[] found = {false, false, false, false, false, false};
        for (int i = 0; i < 100; i++) {
            String randString = RandomStringUtils.randomAlphanumeric(10);
            for (int j = 0; j < testChars.length; j++) {
                if (randString.indexOf(testChars[j]) > 0) {
                    found[j] = true;
                }
            }
        }
        for (int i = 0; i < testChars.length; i++) {
            if (!found[i]) {
                fail("alphanumeric character not generated in 1000 attempts: " 
                   + testChars[i] +" -- repeated failures indicate a problem ");
            }
        }
    }
    
    /**
     * Make sure '0' and '9' are generated by randomNumeric
     * This test will fail randomly with probability = 2 * (9/10)**1000 ~ 3.5E-46
     */  
    public void testRandomNumeric() {
        char[] testChars = {'0','9'};
        boolean[] found = {false, false};
        for (int i = 0; i < 100; i++) {
            String randString = RandomStringUtils.randomNumeric(10);
            for (int j = 0; j < testChars.length; j++) {
                if (randString.indexOf(testChars[j]) > 0) {
                    found[j] = true;
                }
            }
        }
        for (int i = 0; i < testChars.length; i++) {
            if (!found[i]) {
                fail("digit not generated in 1000 attempts: " 
                   + testChars[i] +" -- repeated failures indicate a problem ");
            }
        }  
    }
    
    /**
     * Make sure boundary alpha characters are generated by randomAlphabetic
     * This test will fail randomly with probability = 4 * (51/52)**1000 ~ 1.58E-8
     */  
    public void testRandomAlphabetic() {
        char[] testChars = {'a', 'z', 'A', 'Z'};
        boolean[] found = {false, false, false, false};
        for (int i = 0; i < 100; i++) {
            String randString = RandomStringUtils.randomAlphabetic(10);
            for (int j = 0; j < testChars.length; j++) {
                if (randString.indexOf(testChars[j]) > 0) {
                    found[j] = true;
                }
            }
        }
        for (int i = 0; i < testChars.length; i++) {
            if (!found[i]) {
                fail("alphanumeric character not generated in 1000 attempts: " 
                   + testChars[i] +" -- repeated failures indicate a problem ");
            }
        }
    }
    
    /**
     * Make sure 32 and 127 are generated by randomNumeric
     * This test will fail randomly with probability = 2*(95/96)**1000 ~ 5.7E-5
     */  
    public void testRandomAscii() {
        char[] testChars = {(char) 32, (char) 126};
        boolean[] found = {false, false};
        for (int i = 0; i < 100; i++) {
            String randString = RandomStringUtils.randomAscii(10);
            for (int j = 0; j < testChars.length; j++) {
                if (randString.indexOf(testChars[j]) > 0) {
                    found[j] = true;
                }
            }
        }
        for (int i = 0; i < testChars.length; i++) {
            if (!found[i]) {
                fail("ascii character not generated in 1000 attempts: " 
                + (int) testChars[i] + 
                 " -- repeated failures indicate a problem");
            }
        }  
    }
    
    /** 
     * Test homogeneity of random strings generated --
     * i.e., test that characters show up with expected frequencies
     * in generated strings.  Will fail randomly about 1 in 1000 times.
     * Repeated failures indicate a problem.
     */
    public void testRandomStringUtilsHomog() {
        String set = "abc";
        char[] chars = set.toCharArray();
        String gen = "";
        int[] counts = {0,0,0};
        int[] expected = {200,200,200};
        for (int i = 0; i< 100; i++) {
           gen = RandomStringUtils.random(6,chars);
           for (int j = 0; j < 6; j++) {
               switch (gen.charAt(j)) {
                   case 'a': {counts[0]++; break;}
                   case 'b': {counts[1]++; break;}
                   case 'c': {counts[2]++; break;}
                   default: {fail("generated character not in set");}
               }
           }
        } 
        // Perform chi-square test with df = 3-1 = 2, testing at .001 level
        assertTrue("test homogeneity -- will fail about 1 in 1000 times",
            chiSquare(expected,counts) < 13.82);  
    }
    
    /**
     * Computes Chi-Square statistic given observed and expected counts
     * @param observed array of observed frequency counts
     * @param expected array of expected frequency counts
     */
    private double chiSquare(int[] expected, int[] observed) {
        double sumSq = 0.0d;
        double dev = 0.0d;
        for (int i = 0; i < observed.length; i++) {
            dev = (observed[i] - expected[i]);
            sumSq += dev * dev / expected[i];
        }
        return sumSq;
    }           

    /**
     * Checks if the string got by {@link RandomStringUtils#random(int)}
     * can be converted to UTF-8 and back without loss.
     *
     * @author stefanhoehne@fastmail.fm
     * @throws Exception
     */
    public void testLang100() throws Exception {
        int size = 5000;
        String encoding = "UTF-8";
        String orig = RandomStringUtils.random(size);
        byte[] bytes = orig.getBytes(encoding);
        String copy = new String(bytes, encoding);

        // for a verbose compare:
        for (int i=0; i < orig.length() && i < copy.length(); i++) {
            char o = orig.charAt(i);
            char c = copy.charAt(i);
            assertEquals("differs at " + i + "(" + Integer.toHexString((new Character(o)).hashCode()) + "," +
            Integer.toHexString((new Character(c)).hashCode()) + ")", o, c);
        }
        // compare length also
        assertEquals(orig.length(), copy.length());
        // just to be complete
        assertEquals(orig, copy);
    }

    public static void main(String args[]) {
        TestRunner.run(suite());
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
	java.base/java.util.concurrent.ForkJoinPool.helpJoin(ForkJoinPool.java:1883)
	java.base/java.util.concurrent.ForkJoinTask.awaitDone(ForkJoinTask.java:440)
	java.base/java.util.concurrent.ForkJoinTask.join(ForkJoinTask.java:670)
	scala.collection.parallel.ForkJoinTasks$FJTWrappedTask.sync(Tasks.scala:243)
	scala.collection.parallel.ForkJoinTasks$FJTWrappedTask.sync$(Tasks.scala:243)
	scala.collection.parallel.AdaptiveWorkStealingForkJoinTasks$AWSFJTWrappedTask.sync(Tasks.scala:304)
	scala.collection.parallel.AdaptiveWorkStealingTasks$AWSTWrappedTask.internal(Tasks.scala:173)
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

QDox parse error in file://<WORKSPACE>/data/code-rep-dataset/Dataset3/Tasks/13079.java