error id: file://<WORKSPACE>/data/code-rep-dataset/Dataset5/Tasks/5678.java
file://<WORKSPACE>/data/code-rep-dataset/Dataset5/Tasks/5678.java
### com.thoughtworks.qdox.parser.ParseException: syntax error @[1,16]

error in qdox parser
file content:
```java
offset: 16
uri: file://<WORKSPACE>/data/code-rep-dataset/Dataset5/Tasks/5678.java
text:
```scala
private static P@@atternFormatter format = new PatternFormatter("%{time:MM/dd/yyyy h:mm:ss a} %{priority} - %{category}: %{message} %{throwable}\n");

package org.apache.jmeter.util;

import java.io.Writer;

import org.apache.log.Hierarchy;
import org.apache.log.Priority;
import org.apache.log.format.PatternFormatter;
import org.apache.log.output.io.WriterTarget;

/**
 * @author Administrator
 *
 * To change this generated comment edit the template variable "typecomment":
 * Window>Preferences>Java>Templates.
 */
public class LoggingManager
{
	private static PatternFormatter format = new PatternFormatter("%{time:MM/dd/yyyy h:mm:ss a} %{priority} - %{category}: %{message}\n%{throwable}\n");
	private static WriterTarget target;
	
	public static final String JMETER = "jmeter";
	public static final String ENGINE = "jmeter.engine";
	public static final String ELEMENTS = "jmeter.elements";
	public static final String GUI = "jmeter.gui";
	public static final String UTIL = "jmeter.util";
	public static final String CLASSFINDER = "jmeter.util.classfinder";
	public static final String TEST = "jmeter.test";
	public static final String HTTP = "jmeter.protocol.http";
	public static final String JDBC = "jmeter.protocol.jdbc";
	public static final String FTP = "jmeter.protocol.ftp";
	public static final String JAVA = "jmeter.protocol.java";
	
	
	LoggingManager()
	{
	}
	
	public void setPriority(Priority p,String category)
	{
		Hierarchy.getDefaultHierarchy().getLoggerFor(category).setPriority(p);
	}
	
	public void setTarget(Writer targetFile)
	{
		if(target == null)
		{
			target = new WriterTarget(targetFile,format);
			Hierarchy.getDefaultHierarchy().setDefaultLogTarget(new WriterTarget(targetFile,format));
		}
		else
		{
			target.close();
			target = new WriterTarget(targetFile,format);
			Hierarchy.getDefaultHierarchy().setDefaultLogTarget(new WriterTarget(targetFile,format));
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

QDox parse error in file://<WORKSPACE>/data/code-rep-dataset/Dataset5/Tasks/5678.java