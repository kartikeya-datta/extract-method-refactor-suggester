error id: file://<WORKSPACE>/data/code-rep-dataset/Dataset5/Tasks/5639.java
file://<WORKSPACE>/data/code-rep-dataset/Dataset5/Tasks/5639.java
### com.thoughtworks.qdox.parser.ParseException: syntax error @[1,1]

error in qdox parser
file content:
```java
offset: 1
uri: file://<WORKSPACE>/data/code-rep-dataset/Dataset5/Tasks/5639.java
text:
```scala
i@@f(value == null)

package org.apache.jmeter.modifiers;
import java.io.Serializable;

import org.apache.jmeter.engine.event.LoopIterationEvent;
import org.apache.jmeter.engine.event.LoopIterationListener;
import org.apache.jmeter.engine.util.NoThreadClone;
import org.apache.jmeter.testelement.AbstractTestElement;
import org.apache.jmeter.testelement.property.BooleanProperty;
import org.apache.jmeter.testelement.property.IntegerProperty;
import org.apache.jmeter.threads.JMeterContextService;
import org.apache.jmeter.threads.JMeterVariables;
import org.apache.jmeter.util.JMeterUtils;
import org.apache.jorphan.logging.LoggingManager;
import org.apache.log.Logger;
/**
 * @author Administrator
 *
 * To change this generated comment edit the template variable "typecomment":
 * Window>Preferences>Java>Templates.
 */
public class CounterConfig
	extends AbstractTestElement
	implements Serializable, LoopIterationListener,NoThreadClone
{
	private static Logger log = LoggingManager.getLoggerFor(JMeterUtils.ELEMENTS);
	private final static String START = "CounterConfig.start";
	private final static String END = "CounterConfig.end";
	private final static String INCREMENT = "CounterConfig.incr";
	private final static String PER_USER = "CounterConfig.per_user";
	private final static String VAR_NAME = "CounterConfig.name";
	
	private boolean perUser = false;
	private int globalCounter = -1;
	private int currentIterationCount = -1;
	/**
	 * @see org.apache.jmeter.engine.event.LoopIterationListener#iterationStart(LoopIterationEvent)
	 */
	public synchronized void iterationStart(LoopIterationEvent event)
	{
		JMeterVariables variables = JMeterContextService.getContext().getVariables();
		int start=getStart(),end=getEnd(),increment=getIncrement();
		if(!isPerUser())
		{
			if (globalCounter==-1 || globalCounter>end)
			{
				globalCounter=start;
			}
			variables.put(getVarName(),Integer.toString(globalCounter));
			globalCounter+=increment;
		}
		else
		{		
			String value = variables.get(getVarName());
			if(value == null || value.equals(""))
			{
				variables.put(getVarName(),Integer.toString(start));
                value = variables.get(getVarName());
			}
			else
			{
				try
				{
					int current = Integer.parseInt(value);
					current += increment;
					if(current > end)
					{
						current = start;
					}
					variables.put(getVarName(),Integer.toString(current));
				}
				catch(NumberFormatException e)
				{
					log.info("Bad number in Counter config",e);
				}		
			}	
		}			
	}
	
	public void setStart(int start)
	{
		setProperty(new IntegerProperty(START,start));
	}
	
	public void setStart(String start)
	{
		setProperty(START,start);
	}
	
	public int getStart()
	{
		return getPropertyAsInt(START);
	}
	
	public void setEnd(int end)
	{
		setProperty(new IntegerProperty(END,end));
	}
	
	public void setEnd(String end)
	{
		setProperty(END,end);
	}
	
	public int getEnd()
	{
		return getPropertyAsInt(END);
	}
	
	public void setIncrement(int inc)
	{
		setProperty(new IntegerProperty(INCREMENT,inc));
	}
	
	public void setIncrement(String incr)
	{
		setProperty(INCREMENT,incr);
	}
	
	public int getIncrement()
	{
		return getPropertyAsInt(INCREMENT);
	}
	
	public void setIsPerUser(boolean isPer)
	{
		setProperty(new BooleanProperty(PER_USER,isPer));
	}
	
	public boolean isPerUser()
	{
		return getPropertyAsBoolean(PER_USER);
	}
	
	public void setVarName(String name)
	{
		setProperty(VAR_NAME,name);
	}
	
	public String getVarName()
	{
		return getPropertyAsString(VAR_NAME);
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

QDox parse error in file://<WORKSPACE>/data/code-rep-dataset/Dataset5/Tasks/5639.java