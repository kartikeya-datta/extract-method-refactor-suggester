error id: file://<WORKSPACE>/data/code-rep-dataset/Dataset2/Tasks/4184.java
file://<WORKSPACE>/data/code-rep-dataset/Dataset2/Tasks/4184.java
### com.thoughtworks.qdox.parser.ParseException: syntax error @[1,1]

error in qdox parser
file content:
```java
offset: 1
uri: file://<WORKSPACE>/data/code-rep-dataset/Dataset2/Tasks/4184.java
text:
```scala
O@@bject element = iter.next();

/*
 * ============================================================================
 *                   The Apache Software License, Version 1.1
 * ============================================================================
 *
 *    Copyright (C) 1999 The Apache Software Foundation. All rights reserved.
 *
 * Redistribution and use in source and binary forms, with or without modifica-
 * tion, are permitted provided that the following conditions are met:
 *
 * 1. Redistributions of  source code must  retain the above copyright  notice,
 *    this list of conditions and the following disclaimer.
 *
 * 2. Redistributions in binary form must reproduce the above copyright notice,
 *    this list of conditions and the following disclaimer in the documentation
 *    and/or other materials provided with the distribution.
 *
 * 3. The end-user documentation included with the redistribution, if any, must
 *    include  the following  acknowledgment:  "This product includes  software
 *    developed  by the  Apache Software Foundation  (http://www.apache.org/)."
 *    Alternately, this  acknowledgment may  appear in the software itself,  if
 *    and wherever such third-party acknowledgments normally appear.
 *
 * 4. The names "log4j" and  "Apache Software Foundation"  must not be used to
 *    endorse  or promote  products derived  from this  software without  prior
 *    written permission. For written permission, please contact
 *    apache@apache.org.
 *
 * 5. Products  derived from this software may not  be called "Apache", nor may
 *    "Apache" appear  in their name,  without prior written permission  of the
 *    Apache Software Foundation.
 *
 * THIS SOFTWARE IS PROVIDED ``AS IS'' AND ANY EXPRESSED OR IMPLIED WARRANTIES,
 * INCLUDING, BUT NOT LIMITED TO, THE IMPLIED WARRANTIES OF MERCHANTABILITY AND
 * FITNESS  FOR A PARTICULAR  PURPOSE ARE  DISCLAIMED.  IN NO  EVENT SHALL  THE
 * APACHE SOFTWARE  FOUNDATION  OR ITS CONTRIBUTORS  BE LIABLE FOR  ANY DIRECT,
 * INDIRECT, INCIDENTAL, SPECIAL,  EXEMPLARY, OR CONSEQUENTIAL  DAMAGES (INCLU-
 * DING, BUT NOT LIMITED TO, PROCUREMENT  OF SUBSTITUTE GOODS OR SERVICES; LOSS
 * OF USE, DATA, OR  PROFITS; OR BUSINESS  INTERRUPTION)  HOWEVER CAUSED AND ON
 * ANY  THEORY OF LIABILITY,  WHETHER  IN CONTRACT,  STRICT LIABILITY,  OR TORT
 * (INCLUDING  NEGLIGENCE OR  OTHERWISE) ARISING IN  ANY WAY OUT OF THE  USE OF
 * THIS SOFTWARE, EVEN IF ADVISED OF THE POSSIBILITY OF SUCH DAMAGE.
 *
 * This software  consists of voluntary contributions made  by many individuals
 * on  behalf of the Apache Software  Foundation.  For more  information on the
 * Apache Software Foundation, please see <http://www.apache.org/>.
 *
 */

/*
 */
package org.apache.log4j.chainsaw.filter;

import java.util.HashMap;
import java.util.HashSet;
import java.util.Iterator;
import java.util.Map;
import java.util.Set;

import javax.swing.DefaultListModel;
import javax.swing.ListModel;

/**
 * A Container class used to hold unique LoggingEvent values
 * and provide them as unique ListModels.
 * 
 * @author Paul Smith 
 *
 */
public class EventTypeEntryContainer {
  private Set ColumnNames = new HashSet();
  private Set Methods = new HashSet();
  private Set Classes = new HashSet();
  private Set MDCKeys = new HashSet();
  private Set NDCs = new HashSet();
  private Set Levels = new HashSet();
  private Set Loggers = new HashSet();
  private Set Threads = new HashSet();
  private Set FileNames = new HashSet();
  private DefaultListModel columnNameListModel = new DefaultListModel();
  private DefaultListModel methodListModel = new DefaultListModel();
  private DefaultListModel classesListModel = new DefaultListModel();
  private DefaultListModel mdcListModel = new DefaultListModel();
  private DefaultListModel ndcListModel = new DefaultListModel();
  private DefaultListModel levelListModel = new DefaultListModel();
  private DefaultListModel loggerListModel = new DefaultListModel();
  private DefaultListModel threadListModel = new DefaultListModel();
  private DefaultListModel fileNameListModel = new DefaultListModel();
  private Map modelMap = new HashMap();
  private static final String LOGGER_FIELD = "LOGGER";
  private static final String LEVEL_FIELD = "LEVEL";
  private static final String CLASS_FIELD = "CLASS";
  private static final String FILE_FIELD = "FILE";
  private static final String THREAD_FIELD = "THREAD";
  private static final String METHOD_FIELD = "METHOD";
  private static final String MDC_FIELD = "MDC";
  private static final String NDC_FIELD = "NDC";

  public EventTypeEntryContainer() {
      modelMap.put(LOGGER_FIELD, loggerListModel);
      modelMap.put(LEVEL_FIELD, levelListModel);
      modelMap.put(CLASS_FIELD, classesListModel);
      modelMap.put(FILE_FIELD, fileNameListModel);
      modelMap.put(THREAD_FIELD, threadListModel);
      modelMap.put(METHOD_FIELD, methodListModel);
      modelMap.put(NDC_FIELD, ndcListModel);
      modelMap.put(MDC_FIELD, mdcListModel);
  }
  
  public boolean modelExists(String fieldName) {
      if (fieldName != null) {
          return (modelMap.keySet().contains(fieldName.toUpperCase()));
      }
      return false;
  }
  
  public ListModel getModel(String fieldName) {
      if (fieldName != null) {
          return (ListModel)modelMap.get(fieldName.toUpperCase());
      }
      return null;
  } 
  
  void addLevel(Object level) {
    if (Levels.add(level)) {
      levelListModel.addElement(level);
    }
  }

  void addLogger(String logger) {
    if (Loggers.add(logger)) {
      loggerListModel.addElement(logger);
    }
  }

  void addFileName(String filename) {
    if (FileNames.add(filename)) {
      fileNameListModel.addElement(filename);
    }
  }

  void addThread(String thread) {
    if (Threads.add(thread)) {
      threadListModel.addElement(thread);
    }
  }

  void addNDC(String ndc) {
    if (NDCs.add(ndc)) {
      ndcListModel.addElement(ndc);
    }
  }

  void addColumnName(String name) {
    if (ColumnNames.add(name)) {
      columnNameListModel.addElement(name);
    }
  }

  void addMethod(String method) {
    if (Methods.add(method)) {
      methodListModel.addElement(method);
    }
  }

  void addClass(String className) {
    if (Classes.add(className)) {
      classesListModel.addElement(className);
    }
  }

  void addMDCKeys(Set keySet) {
    if (MDCKeys.addAll(keySet)) {
      for (Iterator iter = keySet.iterator(); iter.hasNext();) {
        Object element = (Object) iter.next();
        mdcListModel.addElement(element);
      }
    }
  }

  ListModel getColumnListModel() {
    return columnNameListModel;
  }

  /**
   * @return
   */
  DefaultListModel getClassesListModel() {
    return classesListModel;
  }

  /**
   * @return
   */
  DefaultListModel getColumnNameListModel() {
    return columnNameListModel;
  }

  /**
   * @return
   */
  DefaultListModel getFileNameListModel() {
    return fileNameListModel;
  }

  /**
   * @return
   */
  DefaultListModel getLevelListModel() {
    return levelListModel;
  }

  /**
   * @return
   */
  DefaultListModel getLoggerListModel() {
    return loggerListModel;
  }

  /**
   * @return
   */
  DefaultListModel getMdcListModel() {
    return mdcListModel;
  }

  /**
   * @return
   */
  DefaultListModel getMethodListModel() {
    return methodListModel;
  }

  /**
   * @return
   */
  DefaultListModel getNdcListModel() {
    return ndcListModel;
  }

  /**
   * @return
   */
  DefaultListModel getThreadListModel() {
    return threadListModel;
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

QDox parse error in file://<WORKSPACE>/data/code-rep-dataset/Dataset2/Tasks/4184.java