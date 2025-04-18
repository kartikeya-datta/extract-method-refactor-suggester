error id: file://<WORKSPACE>/data/code-rep-dataset/Dataset1/Tasks/2360.java
file://<WORKSPACE>/data/code-rep-dataset/Dataset1/Tasks/2360.java
### com.thoughtworks.qdox.parser.ParseException: syntax error @[1,7]

error in qdox parser
file content:
```java
offset: 7
uri: file://<WORKSPACE>/data/code-rep-dataset/Dataset1/Tasks/2360.java
text:
```scala
final i@@nt numCategories = atLeast(10000);

package org.apache.lucene.facet.taxonomy.directory;

import java.io.IOException;
import java.util.HashSet;
import java.util.Random;
import java.util.concurrent.atomic.AtomicInteger;

import org.apache.lucene.facet.taxonomy.CategoryPath;
import org.apache.lucene.facet.taxonomy.directory.DirectoryTaxonomyWriter.DiskOrdinalMap;
import org.apache.lucene.facet.taxonomy.directory.DirectoryTaxonomyWriter.MemoryOrdinalMap;
import org.apache.lucene.facet.taxonomy.directory.DirectoryTaxonomyWriter.OrdinalMap;
import org.apache.lucene.store.Directory;
import org.apache.lucene.util.IOUtils;
import org.apache.lucene.util.LuceneTestCase;
import org.apache.lucene.util._TestUtil;

/*
 * Licensed to the Apache Software Foundation (ASF) under one or more
 * contributor license agreements.  See the NOTICE file distributed with
 * this work for additional information regarding copyright ownership.
 * The ASF licenses this file to You under the Apache License, Version 2.0
 * (the "License"); you may not use this file except in compliance with
 * the License.  You may obtain a copy of the License at
 *
 *     http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */

public class TestAddTaxonomy extends LuceneTestCase {

  private void dotest(int ncats, final int range) throws Exception {
    final AtomicInteger numCats = new AtomicInteger(ncats);
    Directory dirs[] = new Directory[2];
    for (int i = 0; i < dirs.length; i++) {
      dirs[i] = newDirectory();
      final DirectoryTaxonomyWriter tw = new DirectoryTaxonomyWriter(dirs[i]);
      Thread[] addThreads = new Thread[4];
      for (int j = 0; j < addThreads.length; j++) {
        addThreads[j] = new Thread() {
          @Override
          public void run() {
            Random random = random();
            while (numCats.decrementAndGet() > 0) {
              String cat = Integer.toString(random.nextInt(range));
              try {
                tw.addCategory(new CategoryPath("a", cat));
              } catch (IOException e) {
                throw new RuntimeException(e);
              }
            }
          }
        };
      }
      
      for (Thread t : addThreads) t.start();
      for (Thread t : addThreads) t.join();
      tw.close();
    }

    DirectoryTaxonomyWriter tw = new DirectoryTaxonomyWriter(dirs[0]);
    OrdinalMap map = randomOrdinalMap();
    tw.addTaxonomy(dirs[1], map);
    tw.close();
    
    validate(dirs[0], dirs[1], map);
    
    IOUtils.close(dirs);
  }
  
  private OrdinalMap randomOrdinalMap() throws IOException {
    if (random().nextBoolean()) {
      return new DiskOrdinalMap(_TestUtil.createTempFile("taxoMap", "", TEMP_DIR));
    } else {
      return new MemoryOrdinalMap();
    }
  }

  private void validate(Directory dest, Directory src, OrdinalMap ordMap) throws Exception {
    CategoryPath cp = new CategoryPath();
    DirectoryTaxonomyReader destTR = new DirectoryTaxonomyReader(dest);
    try {
      final int destSize = destTR.getSize();
      DirectoryTaxonomyReader srcTR = new DirectoryTaxonomyReader(src);
      try {
        int[] map = ordMap.getMap();
        
        // validate taxo sizes
        int srcSize = srcTR.getSize();
        assertTrue("destination taxonomy expected to be larger than source; dest="
            + destSize + " src=" + srcSize,
            destSize >= srcSize);
        
        // validate that all source categories exist in destination, and their
        // ordinals are as expected.
        for (int j = 1; j < srcSize; j++) {
          srcTR.getPath(j, cp);
          int destOrdinal = destTR.getOrdinal(cp);
          assertTrue(cp + " not found in destination", destOrdinal > 0);
          assertEquals(destOrdinal, map[j]);
        }
      } finally {
        srcTR.close();
      }
    } finally {
      destTR.close();
    }
  }

  public void testAddEmpty() throws Exception {
    Directory dest = newDirectory();
    DirectoryTaxonomyWriter destTW = new DirectoryTaxonomyWriter(dest);
    destTW.addCategory(new CategoryPath("Author", "Rob Pike"));
    destTW.addCategory(new CategoryPath("Aardvarks", "Bob"));
    destTW.commit();
    
    Directory src = newDirectory();
    new DirectoryTaxonomyWriter(src).close(); // create an empty taxonomy
    
    OrdinalMap map = randomOrdinalMap();
    destTW.addTaxonomy(src, map);
    destTW.close();
    
    validate(dest, src, map);
    
    IOUtils.close(dest, src);
  }
  
  public void testAddToEmpty() throws Exception {
    Directory dest = newDirectory();
    
    Directory src = newDirectory();
    DirectoryTaxonomyWriter srcTW = new DirectoryTaxonomyWriter(src);
    srcTW.addCategory(new CategoryPath("Author", "Rob Pike"));
    srcTW.addCategory(new CategoryPath("Aardvarks", "Bob"));
    srcTW.close();
    
    DirectoryTaxonomyWriter destTW = new DirectoryTaxonomyWriter(dest);
    OrdinalMap map = randomOrdinalMap();
    destTW.addTaxonomy(src, map);
    destTW.close();
    
    validate(dest, src, map);
    
    IOUtils.close(dest, src);
  }
  
  // A more comprehensive and big random test.
  public void testBig() throws Exception {
    dotest(200, 10000);
    dotest(1000, 20000);
    dotest(400000, 1000000);
  }

  // a reasonable random test
  public void testMedium() throws Exception {
    Random random = random();
    int numTests = atLeast(3);
    for (int i = 0; i < numTests; i++) {
      dotest(_TestUtil.nextInt(random, 2, 100), 
             _TestUtil.nextInt(random, 100, 1000));
    }
  }
  
  public void testSimple() throws Exception {
    Directory dest = newDirectory();
    DirectoryTaxonomyWriter tw1 = new DirectoryTaxonomyWriter(dest);
    tw1.addCategory(new CategoryPath("Author", "Mark Twain"));
    tw1.addCategory(new CategoryPath("Animals", "Dog"));
    tw1.addCategory(new CategoryPath("Author", "Rob Pike"));
    
    Directory src = newDirectory();
    DirectoryTaxonomyWriter tw2 = new DirectoryTaxonomyWriter(src);
    tw2.addCategory(new CategoryPath("Author", "Rob Pike"));
    tw2.addCategory(new CategoryPath("Aardvarks", "Bob"));
    tw2.close();

    OrdinalMap map = randomOrdinalMap();

    tw1.addTaxonomy(src, map);
    tw1.close();

    validate(dest, src, map);
    
    IOUtils.close(dest, src);
  }

  public void testConcurrency() throws Exception {
    // tests that addTaxonomy and addCategory work in parallel
    final int numCategories = atLeast(5000);
    
    // build an input taxonomy index
    Directory src = newDirectory();
    DirectoryTaxonomyWriter tw = new DirectoryTaxonomyWriter(src);
    for (int i = 0; i < numCategories; i++) {
      tw.addCategory(new CategoryPath("a", Integer.toString(i)));
    }
    tw.close();
    
    // now add the taxonomy to an empty taxonomy, while adding the categories
    // again, in parallel -- in the end, no duplicate categories should exist.
    Directory dest = newDirectory();
    final DirectoryTaxonomyWriter destTW = new DirectoryTaxonomyWriter(dest);
    Thread t = new Thread() {
      @Override
      public void run() {
        for (int i = 0; i < numCategories; i++) {
          try {
            destTW.addCategory(new CategoryPath("a", Integer.toString(i)));
          } catch (IOException e) {
            // shouldn't happen - if it does, let the test fail on uncaught exception.
            throw new RuntimeException(e);
          }
        }
      }
    };
    t.start();
    
    OrdinalMap map = new MemoryOrdinalMap();
    destTW.addTaxonomy(src, map);
    t.join();
    destTW.close();
    
    // now validate
    
    DirectoryTaxonomyReader dtr = new DirectoryTaxonomyReader(dest);
    // +2 to account for the root category + "a"
    assertEquals(numCategories + 2, dtr.getSize());
    HashSet<CategoryPath> categories = new HashSet<CategoryPath>();
    for (int i = 1; i < dtr.getSize(); i++) {
      CategoryPath cat = dtr.getPath(i);
      assertTrue("category " + cat + " already existed", categories.add(cat));
    }
    dtr.close();
    
    IOUtils.close(src, dest);
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

QDox parse error in file://<WORKSPACE>/data/code-rep-dataset/Dataset1/Tasks/2360.java