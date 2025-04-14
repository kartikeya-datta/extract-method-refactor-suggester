error id: file://<WORKSPACE>/data/code-rep-dataset/Dataset5/Tasks/1445.java
file://<WORKSPACE>/data/code-rep-dataset/Dataset5/Tasks/1445.java
### com.thoughtworks.qdox.parser.ParseException: syntax error @[1,11]

error in qdox parser
file content:
```java
offset: 11
uri: file://<WORKSPACE>/data/code-rep-dataset/Dataset5/Tasks/1445.java
text:
```scala
transient I@@terator threadValues;

package org.apache.jmeter.modifiers;

import java.io.Serializable;
import java.util.Iterator;
import java.util.LinkedList;
import java.util.List;

import org.apache.jmeter.config.ConfigTestElement;
import org.apache.jmeter.samplers.Sampler;
import org.apache.jmeter.testelement.ThreadListener;
import org.apache.jmeter.testelement.VariablesCollection;
import org.apache.jmeter.threads.JMeterVariables;

/**
 * @author Administrator
 *
 * To change this generated comment edit the template variable "typecomment":
 * Window>Preferences>Java>Templates.
 */
public class UserParameters
	extends ConfigTestElement
	implements Serializable, ThreadListener {
		
		private static final String NAMES = "UserParameters.names";
		private static final String THREAD_VALUES = "UserParameters.thread_values";

	VariablesCollection vars = new VariablesCollection();
	int counter = 0;
	Iterator threadValues;
	/**
	 * @see org.apache.jmeter.config.Modifier#modifyEntry(Sampler)
	 */
	public boolean modifyEntry(Sampler Sampler) {
		return false;
	}
	
	public void iterationStarted(int iter)
	{
		if(iter == 1)
		{
			setVariables();
		}
	}
	
	public void setJMeterVariables(JMeterVariables vars)
	{
		this.vars.addJMeterVariables(vars);
	}
	
	public List getNames()
	{
		return (List)getProperty(NAMES);
	}
	
	public List getThreadLists()
	{
		return (List)getProperty(THREAD_VALUES);
	}	
	
	/**
	 * The list of names of the variables to hold values.  This list must come in
	 * the same order as the sub lists that are given to setThreadLists(List).
	 */
	public void setNames(List list)
	{
		setProperty(NAMES,list);
	}
	
	/**
	 * The thread list is a list of lists.  Each list within the parent list is a
	 * collection of values for a simulated user.  As many different sets of 
	 * values can be supplied in this fashion to cause JMeter to set different 
	 * values to variables for different test threads.
	 */
	public void setThreadLists(List threadLists)
	{
		setProperty(THREAD_VALUES,threadLists);
	}
	
	private synchronized List getValues()
	{
		if(threadValues == null || !threadValues.hasNext())
		{
			threadValues = ((List)getProperty(THREAD_VALUES)).iterator();
		}
		if(threadValues.hasNext())
		{
			return (List)threadValues.next();
		}
		else
		{
			return new LinkedList();
		}
	}	
		
	
	private void setVariables()
	{
		Iterator namesIter = getNames().iterator();
		Iterator valueIter = getValues().iterator();
		JMeterVariables jmvars = vars.getVariables();
		while(namesIter.hasNext() && valueIter.hasNext())
		{
			String name = (String)namesIter.next();
			String value = (String)valueIter.next();
			jmvars.put(name,value);
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

QDox parse error in file://<WORKSPACE>/data/code-rep-dataset/Dataset5/Tasks/1445.java