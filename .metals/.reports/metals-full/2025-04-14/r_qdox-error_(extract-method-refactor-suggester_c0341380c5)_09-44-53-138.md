error id: file://<WORKSPACE>/data/code-rep-dataset/Dataset5/Tasks/5523.java
file://<WORKSPACE>/data/code-rep-dataset/Dataset5/Tasks/5523.java
### com.thoughtworks.qdox.parser.ParseException: syntax error @[1,1]

error in qdox parser
file content:
```java
offset: 1
uri: file://<WORKSPACE>/data/code-rep-dataset/Dataset5/Tasks/5523.java
text:
```scala
I@@nteger o = Integer.valueOf(1);

/*
 * Licensed to the Apache Software Foundation (ASF) under one or more
 * contributor license agreements.  See the NOTICE file distributed with
 * this work for additional information regarding copyright ownership.
 * The ASF licenses this file to You under the Apache License, Version 2.0
 * (the "License"); you may not use this file except in compliance with
 * the License.  You may obtain a copy of the License at
 *
 *   http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 * 
 */

package org.apache.jorphan.collections;

import java.util.Arrays;
import java.util.Collection;

import junit.framework.TestCase;

import org.apache.jorphan.logging.LoggingManager;
import org.apache.log.Logger;

public class PackageTest extends TestCase {
        public PackageTest(String name) {
            super(name);
        }

        public void testAdd1() throws Exception {
            Logger log = LoggingManager.getLoggerForClass();
            Collection<String> treePath = Arrays.asList(new String[] { "1", "2", "3", "4" });
            HashTree tree = new HashTree();
            log.debug("treePath = " + treePath);
            tree.add(treePath, "value");
            log.debug("Now treePath = " + treePath);
            log.debug(tree.toString());
            assertEquals(1, tree.list(treePath).size());
            assertEquals("value", tree.getArray(treePath)[0]);
        }

        public void testEqualsAndHashCode1() throws Exception {
            HashTree tree1 = new HashTree("abcd");
            HashTree tree2 = new HashTree("abcd");
            HashTree tree3 = new HashTree("abcde");
            HashTree tree4 = new HashTree("abcde");

            assertTrue(tree1.equals(tree1));
            assertTrue(tree1.equals(tree2));
            assertTrue(tree2.equals(tree1));
            assertTrue(tree2.equals(tree2));
            assertEquals(tree1.hashCode(), tree2.hashCode());

            assertTrue(tree3.equals(tree3));
            assertTrue(tree3.equals(tree4));
            assertTrue(tree4.equals(tree3));
            assertTrue(tree4.equals(tree4));
            assertEquals(tree3.hashCode(), tree4.hashCode());

            assertNotSame(tree1, tree2);
            assertNotSame(tree1, tree3);
            assertNotSame(tree1, tree4);
            assertNotSame(tree2, tree3);
            assertNotSame(tree2, tree4);

            assertFalse(tree1.equals(tree3));
            assertFalse(tree1.equals(tree4));
            assertFalse(tree2.equals(tree3));
            assertFalse(tree2.equals(tree4));

            assertNotNull(tree1);
            assertNotNull(tree2);

            tree1.add("abcd", tree3);
            assertFalse(tree1.equals(tree2));
            assertFalse(tree2.equals(tree1));// Check reflexive
            if (tree1.hashCode() == tree2.hashCode()) {
                // This is not a requirement
                System.out.println("WARN: unequal HashTrees should not have equal hashCodes");
            }
            tree2.add("abcd", tree4);
            assertTrue(tree1.equals(tree2));
            assertTrue(tree2.equals(tree1));
            assertEquals(tree1.hashCode(), tree2.hashCode());
        }


        public void testAddObjectAndTree() throws Exception {
            ListedHashTree tree = new ListedHashTree("key");
            ListedHashTree newTree = new ListedHashTree("value");
            tree.add("key", newTree);
            assertEquals(tree.list().size(), 1);
            assertEquals("key", tree.getArray()[0]);
            assertEquals(1, tree.getTree("key").list().size());
            assertEquals(0, tree.getTree("key").getTree("value").size());
            assertEquals(tree.getTree("key").getArray()[0], "value");
            assertNotNull(tree.getTree("key").get("value"));
        }

        public void testEqualsAndHashCode2() throws Exception {
            ListedHashTree tree1 = new ListedHashTree("abcd");
            ListedHashTree tree2 = new ListedHashTree("abcd");
            ListedHashTree tree3 = new ListedHashTree("abcde");
            ListedHashTree tree4 = new ListedHashTree("abcde");

            assertTrue(tree1.equals(tree1));
            assertTrue(tree1.equals(tree2));
            assertTrue(tree2.equals(tree1));
            assertTrue(tree2.equals(tree2));
            assertEquals(tree1.hashCode(), tree2.hashCode());

            assertTrue(tree3.equals(tree3));
            assertTrue(tree3.equals(tree4));
            assertTrue(tree4.equals(tree3));
            assertTrue(tree4.equals(tree4));
            assertEquals(tree3.hashCode(), tree4.hashCode());

            assertNotSame(tree1, tree2);
            assertNotSame(tree1, tree3);
            assertFalse(tree1.equals(tree3));
            assertFalse(tree3.equals(tree1));
            assertFalse(tree1.equals(tree4));
            assertFalse(tree4.equals(tree1));

            assertFalse(tree2.equals(tree3));
            assertFalse(tree3.equals(tree2));
            assertFalse(tree2.equals(tree4));
            assertFalse(tree4.equals(tree2));

            tree1.add("abcd", tree3);
            assertFalse(tree1.equals(tree2));
            assertFalse(tree2.equals(tree1));

            tree2.add("abcd", tree4);
            assertTrue(tree1.equals(tree2));
            assertTrue(tree2.equals(tree1));
            assertEquals(tree1.hashCode(), tree2.hashCode());

            tree1.add("a1");
            tree1.add("a2");
            // tree1.add("a3");
            tree2.add("a2");
            tree2.add("a1");

            assertFalse(tree1.equals(tree2));
            assertFalse(tree2.equals(tree1));
            if (tree1.hashCode() == tree2.hashCode()) {
                // This is not a requirement
                System.out.println("WARN: unequal ListedHashTrees should not have equal hashcodes");

            }

            tree4.add("abcdef");
            assertFalse(tree3.equals(tree4));
            assertFalse(tree4.equals(tree3));
        }


        public void testSearch() throws Exception {
            ListedHashTree tree = new ListedHashTree();
            SearchByClass<Integer> searcher = new SearchByClass<Integer>(Integer.class);
            String one = "one";
            String two = "two";
            Integer o = new Integer(1);
            tree.add(one, o);
            tree.getTree(one).add(o, two);
            tree.traverse(searcher);
            assertEquals(1, searcher.getSearchResults().size());
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

QDox parse error in file://<WORKSPACE>/data/code-rep-dataset/Dataset5/Tasks/5523.java