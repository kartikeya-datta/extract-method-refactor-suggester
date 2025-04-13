error id: file://<WORKSPACE>/data/code-rep-dataset/Dataset5/Tasks/6139.java
file://<WORKSPACE>/data/code-rep-dataset/Dataset5/Tasks/6139.java
### com.thoughtworks.qdox.parser.ParseException: syntax error @[1,1]

error in qdox parser
file content:
```java
offset: 1
uri: file://<WORKSPACE>/data/code-rep-dataset/Dataset5/Tasks/6139.java
text:
```scala
S@@tring cpe[]= JOrphanUtils.split(cp,File.pathSeparator);

/*
 * Created on Apr 30, 2003
 *
 * To change the template for this generated file go to
 * Window>Preferences>Java>Code Generation>Code and Comments
 */
package org.apache.jmeter.junit;

import java.io.File;

import junit.framework.TestCase;
import org.apache.jmeter.util.JMeterUtils;
import org.apache.jorphan.logging.LoggingManager;
import org.apache.jorphan.util.JOrphanUtils;
import org.apache.log.Logger;

/**
 * @author ano ano
 *
 * To change the template for this generated type comment go to
 * Window>Preferences>Java>Code Generation>Code and Comments
 */
public abstract class JMeterTestCase extends TestCase
{
	// Used by findTestFile
	private static final String filePrefix; 
	public JMeterTestCase(){
		super();
	}
    
    public JMeterTestCase(String name)
    {
        super(name);
    }
    
    /*
     * If not running under AllTests.java, make sure that the properties
     * (and log file) are set up correctly.
     * 
     * N.B. In order for this to work correctly, the JUnit test must be started
     * in the bin directory, and all the JMeter jars (plus any others needed at
     * run-time) need to be on the classpath.
     * 
     */
    static {
    	if (JMeterUtils.getJMeterProperties() == null){
    		String file="jmetertest.properties";
			File f = new File(file);
			if (!f.canRead()){
				System.out.println("Can't find "+file+" - trying bin directory");
				file="bin/"+file;// JMeterUtils assumes Unix-style separators
				// Also need to set working directory so test files can be found
				System.setProperty("user.dir",System.getProperty("user.dir")+File.separatorChar+"bin");
				System.out.println("Setting user.dir="+System.getProperty("user.dir"));
				filePrefix="bin/";
			} else {
				filePrefix="";
			}
    		JMeterUtils jmu = new JMeterUtils();
    		jmu.initializeProperties(file);
			logprop("java.version");
			logprop("java.vendor");
			logprop("java.home");
			logprop("user.home");
			logprop("user.dir");
			logprop("java.class.version");
			logprop("os.name");
			logprop("os.version");
			logprop("os.arch");
			//logprop("java.class.path");
			String cp = System.getProperty("java.class.path");
			String cpe[]= JOrphanUtils.split(cp,";");
			System.out.println("java.class.path=");
			for (int i=0;i<cpe.length;i++){
				System.out.println(cpe[i]);
			}
    	} else {
    		filePrefix="";
    	}
    }
    
	private static void logprop(String prop)
	{
		System.out.println(prop+"="+System.getProperty(prop));
	}

	// Helper method to find a file
	protected static File findTestFile(String file)
	{
		File f= new File(file);
		if (filePrefix.length() > 0 && !f.isAbsolute())
		{
			f= new File(filePrefix+file);// Add the offset
		}
		return f;
	}

    protected static final Logger testLog = LoggingManager.getLoggerForClass();
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

QDox parse error in file://<WORKSPACE>/data/code-rep-dataset/Dataset5/Tasks/6139.java