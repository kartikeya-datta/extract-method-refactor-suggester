error id: file://<WORKSPACE>/data/code-rep-dataset/Dataset2/Tasks/783.java
file://<WORKSPACE>/data/code-rep-dataset/Dataset2/Tasks/783.java
### com.thoughtworks.qdox.parser.ParseException: syntax error @[1,21]

error in qdox parser
file content:
```java
offset: 21
uri: file://<WORKSPACE>/data/code-rep-dataset/Dataset2/Tasks/783.java
text:
```scala
public static final S@@tring DETAIL_CONTENT_TYPE = "text/html";

/*
 * @author Paul Smith <psmith@apache.org>
 *
*/
package org.apache.log4j.chainsaw;

/**
 * @author Paul Smith <psmith@apache.org>
 * @author Scott Deboy <sdeboy@apache.org>
 * 
 */
public class ChainsawConstants {
  private ChainsawConstants(){}
  
  static final String MAIN_PANEL = "panel";
  static final String LOWER_PANEL = "lower";
  static final String UPPER_PANEL = "upper";
  static final String EMPTY_STRING = "";
  static final String FILTERS_EXTENSION = ".filters";
  static final String SETTINGS_EXTENSION = ".settings";

  //COLUMN NAMES
  static final String LOGGER_COL_NAME = "Logger";
  static final String TIMESTAMP_COL_NAME = "Timestamp";
  static final String LEVEL_COL_NAME = "Level";
  static final String THREAD_COL_NAME = "Thread";
  static final String MESSAGE_COL_NAME = "Message";
  static final String NDC_COL_NAME = "NDC";
  static final String MDC_COL_NAME = "MDC";
  static final String THROWABLE_COL_NAME = "Throwable";
  static final String CLASS_COL_NAME = "Class";
  static final String METHOD_COL_NAME = "Method";
  static final String FILE_COL_NAME = "File";
  static final String LINE_COL_NAME = "Line";
  static final String PROPERTIES_COL_NAME = "Properties";
  static final String ID_COL_NAME = "ID";

  //none is not a real column name, but is used by filters as a way to apply no filter for colors or display
  static final String NONE_COL_NAME = "None";
  static final String LOG4J_APP_KEY = "log4japp";
  static final String LOG4J_MACHINE_KEY = "log4jmachinename";
  static final String LOG4J_REMOTEHOST_KEY = "log4j.remoteSourceInfo";
  public static final String LOG4J_ID_KEY = "log4jid";
  static final String UNKNOWN_TAB_NAME = "Unknown";
  static final String GLOBAL_MATCH = "*";
  static final String DETAIL_CONTENT_TYPE = "text/html";

  static final String EVENT_TYPE_KEY = "log4j.eventtype";
  static final String LOG4J_EVENT_TYPE = "log4j";
  static final String UTIL_LOGGING_EVENT_TYPE = "util-logging";

  static final String LEVEL_DISPLAY = "level.display";
  static final String LEVEL_DISPLAY_ICONS = "icons";
  static final String LEVEL_DISPLAY_TEXT = "text";  

  static final String DATETIME_FORMAT = "EEE MMM dd HH:mm:ss z yyyy";
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

QDox parse error in file://<WORKSPACE>/data/code-rep-dataset/Dataset2/Tasks/783.java