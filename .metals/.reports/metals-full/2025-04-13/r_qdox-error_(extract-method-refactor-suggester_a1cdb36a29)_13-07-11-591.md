error id: file://<WORKSPACE>/data/code-rep-dataset/Dataset3/Tasks/1909.java
file://<WORKSPACE>/data/code-rep-dataset/Dataset3/Tasks/1909.java
### com.thoughtworks.qdox.parser.ParseException: syntax error @[1,1]

error in qdox parser
file content:
```java
offset: 1
uri: file://<WORKSPACE>/data/code-rep-dataset/Dataset3/Tasks/1909.java
text:
```scala
a@@ssertTrue("file filter removed", !fileFilter.getFileFilters().contains(filters.get(i)));

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
package org.apache.commons.io.filefilter;

import java.io.File;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

public abstract class ConditionalFileFilterAbstractTestCase
  extends IOFileFilterAbstractTestCase {
    
  private static final String TEST_FILE_NAME_PREFIX = "TestFile";
  private static final String TEST_FILE_TYPE = ".tst";
  
  protected TesterTrueFileFilter[] trueFilters;
  protected TesterFalseFileFilter[] falseFilters;
  
  private File file;
  private File workingPath;
  
  public ConditionalFileFilterAbstractTestCase(final String name) {
    super(name);
  }
  
  public void setUp() throws Exception {
    this.workingPath = this.determineWorkingDirectoryPath(this.getWorkingPathNamePropertyKey(), this.getDefaultWorkingPath());
    this.file = new File(this.workingPath, TEST_FILE_NAME_PREFIX + 1 + TEST_FILE_TYPE);
    this.trueFilters = new TesterTrueFileFilter[4];
    this.falseFilters = new TesterFalseFileFilter[4];
    this.trueFilters[1] = new TesterTrueFileFilter();
    this.trueFilters[2] = new TesterTrueFileFilter();
    this.trueFilters[3] = new TesterTrueFileFilter();
    this.falseFilters[1] = new TesterFalseFileFilter();
    this.falseFilters[2] = new TesterFalseFileFilter();
    this.falseFilters[3] = new TesterFalseFileFilter();
  }
  
  public void testAdd() {
    List filters = new ArrayList();
    ConditionalFileFilter fileFilter = this.getConditionalFileFilter();
    filters.add(new TesterTrueFileFilter());
    filters.add(new TesterTrueFileFilter());
    filters.add(new TesterTrueFileFilter());
    filters.add(new TesterTrueFileFilter());
    for(int i = 0; i < filters.size(); i++) {
      assertEquals("file filters count: ", i, fileFilter.getFileFilters().size());
      fileFilter.addFileFilter((IOFileFilter) filters.get(i));
      assertEquals("file filters count: ", i+1, fileFilter.getFileFilters().size());
    }
    for(Iterator iter = fileFilter.getFileFilters().iterator(); iter.hasNext();) {
      IOFileFilter filter = (IOFileFilter) iter.next();
      assertTrue("found file filter", filters.contains(filter));
    }
    assertEquals("file filters count", filters.size(), fileFilter.getFileFilters().size());
  }
  
  public void testRemove() {
    List filters = new ArrayList();
    ConditionalFileFilter fileFilter = this.getConditionalFileFilter();
    filters.add(new TesterTrueFileFilter());
    filters.add(new TesterTrueFileFilter());
    filters.add(new TesterTrueFileFilter());
    filters.add(new TesterTrueFileFilter());
    for(int i = 0; i < filters.size(); i++) {
      fileFilter.removeFileFilter((IOFileFilter) filters.get(i));
      assertTrue("file filter removed", !fileFilter.getFileFilters().contains((IOFileFilter) filters.get(i)));
    }
    assertEquals("file filters count", 0, fileFilter.getFileFilters().size());
  }

  public void testNoFilters() throws Exception {
    ConditionalFileFilter fileFilter = this.getConditionalFileFilter();
    File file = new File(this.workingPath, TEST_FILE_NAME_PREFIX + 1 + TEST_FILE_TYPE);
    assertFileFiltering(1, (IOFileFilter) fileFilter, file, false);
    assertFilenameFiltering(1, (IOFileFilter) fileFilter, file, false);
  }
  
  public void testFilterBuiltUsingConstructor() throws Exception {
    List testFilters = this.getTestFilters();
    List testTrueResults = this.getTrueResults();
    List testFalseResults = this.getFalseResults();
    List testFileResults = this.getFileResults();
    List testFilenameResults = this.getFilenameResults();
    
    for(int i = 1; i < testFilters.size(); i++) {
      List filters = (List) testFilters.get(i);
      boolean[] trueResults = (boolean []) testTrueResults.get(i);
      boolean[] falseResults = (boolean []) testFalseResults.get(i);
      boolean fileResults = ((Boolean) testFileResults.get(i)).booleanValue();
      boolean filenameResults = ((Boolean) testFilenameResults.get(i)).booleanValue();

      // Test conditional AND filter created by passing filters to the constructor
      IOFileFilter filter = this.buildFilterUsingConstructor(filters);
      
      // Test as a file filter
      this.resetTrueFilters(this.trueFilters);
      this.resetFalseFilters(this.falseFilters);
      this.assertFileFiltering(i, filter, this.file, fileResults);
      this.assertTrueFiltersInvoked(i, trueFilters, trueResults);
      this.assertFalseFiltersInvoked(i, falseFilters, falseResults);

      // Test as a filename filter
      this.resetTrueFilters(this.trueFilters);
      this.resetFalseFilters(this.falseFilters);
      this.assertFilenameFiltering(i, filter, this.file, filenameResults);
      this.assertTrueFiltersInvoked(i, trueFilters, trueResults);
      this.assertFalseFiltersInvoked(i, falseFilters, falseResults);
    }
  }
  
  public void testFilterBuiltUsingAdd() throws Exception {
    List testFilters = this.getTestFilters();
    List testTrueResults = this.getTrueResults();
    List testFalseResults = this.getFalseResults();
    List testFileResults = this.getFileResults();
    List testFilenameResults = this.getFilenameResults();
    
    for(int i = 1; i < testFilters.size(); i++) {
      List filters = (List) testFilters.get(i);
      boolean[] trueResults = (boolean []) testTrueResults.get(i);
      boolean[] falseResults = (boolean []) testFalseResults.get(i);
      boolean fileResults = ((Boolean) testFileResults.get(i)).booleanValue();
      boolean filenameResults = ((Boolean) testFilenameResults.get(i)).booleanValue();

      // Test conditional AND filter created by passing filters to the constructor
      IOFileFilter filter = this.buildFilterUsingAdd(filters);
      
      // Test as a file filter
      this.resetTrueFilters(this.trueFilters);
      this.resetFalseFilters(this.falseFilters);
      this.assertFileFiltering(i, filter, this.file, fileResults);
      this.assertTrueFiltersInvoked(i, trueFilters, trueResults);
      this.assertFalseFiltersInvoked(i, falseFilters, falseResults);

      // Test as a filename filter
      this.resetTrueFilters(this.trueFilters);
      this.resetFalseFilters(this.falseFilters);
      this.assertFilenameFiltering(i, filter, this.file, filenameResults);
      this.assertTrueFiltersInvoked(i, trueFilters, trueResults);
      this.assertFalseFiltersInvoked(i, falseFilters, falseResults);
    }
  }

  protected abstract ConditionalFileFilter getConditionalFileFilter();
  protected abstract IOFileFilter buildFilterUsingAdd(List filters);
  protected abstract IOFileFilter buildFilterUsingConstructor(List filters);
  protected abstract List getTestFilters();
  protected abstract List getTrueResults();
  protected abstract List getFalseResults();
  protected abstract List getFileResults();
  protected abstract List getFilenameResults();
  protected abstract String getWorkingPathNamePropertyKey();
  protected abstract String getDefaultWorkingPath();
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

QDox parse error in file://<WORKSPACE>/data/code-rep-dataset/Dataset3/Tasks/1909.java