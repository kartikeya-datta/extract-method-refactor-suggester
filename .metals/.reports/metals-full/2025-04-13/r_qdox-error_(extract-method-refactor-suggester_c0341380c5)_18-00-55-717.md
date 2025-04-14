error id: file://<WORKSPACE>/data/code-rep-dataset/Dataset5/Tasks/1448.java
file://<WORKSPACE>/data/code-rep-dataset/Dataset5/Tasks/1448.java
### com.thoughtworks.qdox.parser.ParseException: syntax error @[1,26]

error in qdox parser
file content:
```java
offset: 26
uri: file://<WORKSPACE>/data/code-rep-dataset/Dataset5/Tasks/1448.java
text:
```scala
transient private static L@@ogger log = Hierarchy.getDefaultHierarchy().getLoggerFor("jmeter.gui");

/*
 *  ====================================================================
 *  The Apache Software License, Version 1.1
 *
 *  Copyright (c) 2001 The Apache Software Foundation.  All rights
 *  reserved.
 *
 *  Redistribution and use in source and binary forms, with or without
 *  modification, are permitted provided that the following conditions
 *  are met:
 *
 *  1. Redistributions of source code must retain the above copyright
 *  notice, this list of conditions and the following disclaimer.
 *
 *  2. Redistributions in binary form must reproduce the above copyright
 *  notice, this list of conditions and the following disclaimer in
 *  the documentation and/or other materials provided with the
 *  distribution.
 *
 *  3. The end-user documentation included with the redistribution,
 *  if any, must include the following acknowledgment:
 *  "This product includes software developed by the
 *  Apache Software Foundation (http://www.apache.org/)."
 *  Alternately, this acknowledgment may appear in the software itself,
 *  if and wherever such third-party acknowledgments normally appear.
 *
 *  4. The names "Apache" and "Apache Software Foundation" and
 *  "Apache JMeter" must not be used to endorse or promote products
 *  derived from this software without prior written permission. For
 *  written permission, please contact apache@apache.org.
 *
 *  5. Products derived from this software may not be called "Apache",
 *  "Apache JMeter", nor may "Apache" appear in their name, without
 *  prior written permission of the Apache Software Foundation.
 *
 *  THIS SOFTWARE IS PROVIDED ``AS IS'' AND ANY EXPRESSED OR IMPLIED
 *  OF MERCHANTABILITY AND FITNESS FOR A PARTICULAR PURPOSE ARE
 *  DISCLAIMED.  IN NO EVENT SHALL THE APACHE SOFTWARE FOUNDATION OR
 *  ITS CONTRIBUTORS BE LIABLE FOR ANY DIRECT, INDIRECT, INCIDENTAL,
 *  SPECIAL, EXEMPLARY, OR CONSEQUENTIAL DAMAGES (INCLUDING, BUT NOT
 *  LIMITED TO, PROCUREMENT OF SUBSTITUTE GOODS OR SERVICES; LOSS OF
 *  USE, DATA, OR PROFITS; OR BUSINESS INTERRUPTION) HOWEVER CAUSED AND
 *  ON ANY THEORY OF LIABILITY, WHETHER IN CONTRACT, STRICT LIABILITY,
 *  OR TORT (INCLUDING NEGLIGENCE OR OTHERWISE) ARISING IN ANY WAY OUT
 *  OF THE USE OF THIS SOFTWARE, EVEN IF ADVISED OF THE POSSIBILITY OF
 *  SUCH DAMAGE.
 *  ====================================================================
 *
 *  This software consists of voluntary contributions made by many
 *  individuals on behalf of the Apache Software Foundation.  For more
 *  information on the Apache Software Foundation, please see
 *  <http://www.apache.org/>.
 */
package org.apache.jmeter.visualizers;

import java.util.Collections;
import java.util.Iterator;
import java.util.LinkedList;
import java.util.List;

import org.apache.jmeter.samplers.Clearable;
import org.apache.jmeter.samplers.SampleResult;
import org.apache.log.Hierarchy;
import org.apache.log.Logger;
/**
 *  The model that collects the average of the set of pages to be sampled
 *
 *@author     Khor Soon Hin
 *@created    2001/08/11
 *@version    1.0
 */

public class GraphAccumModel implements Clearable
{
	/**
	 *  Description of the Field
	 */
	protected String name;
	/**
	 *  Description of the Field
	 */
	protected List samples;
	/**
	 *  Description of the Field
	 */
	protected List listeners;
	/**
	 *  Description of the Field
	 */
	protected long averageSum = 0;
	/**
	 *  Description of the Field
	 */
	protected long variationSum = 0;
	/**
	 *  Description of the Field
	 */
	protected long counter = 0;
	/**
	 *  Description of the Field
	 */
	protected long previous = 0;
	/**
	 *  Description of the Field
	 */
	protected long max = 1;
	/**
	 *  Description of the Field
	 */
	protected boolean bigChange = false;
	/**
	 *  Description of the Field
	 */
	protected SampleResult current;
	private static Logger log = Hierarchy.getDefaultHierarchy().getLoggerFor("jmeter.gui");


	/**
	 *  Constructor
	 */
	public GraphAccumModel()
	{
		log.debug("Start : GraphAccumModel1");
		listeners = new LinkedList();
		samples = Collections.synchronizedList(new LinkedList());
		log.debug("End : GraphAccumModel1");
	}


	/**
	 *  Sets the Name attribute of the GraphModel object
	 *
	 *@param  name  The new Name value
	 */
	public void setName(String name)
	{
		this.name = name;
	}


	/**
	 *  Gets the SampleCount attribute of the GraphAccumModel object
	 *
	 *@return    The SampleCount value
	 */
	public int getSampleCount()
	{
		return samples.size();
	}


	/**
	 *  Gets the List attribute of the GraphAccumModel object
	 *
	 *@return    The List value
	 */
	public List getList()
	{
		return samples;
	}


	/**
	 *  Gets the Name attribute of the GraphModel object
	 *
	 *@return    The Name value
	 */
	public String getName()
	{
		return name;
	}


	/**
	 *  Gets the Max attribute of the GraphAccumModel object
	 *
	 *@return    The Max value
	 */
	public long getMax()
	{
		log.debug("getMax1 : Returning - " + max);
		return max;
	}


	/**
	 *  Adds a feature to the ModelListener attribute of the GraphAccumModel object
	 *
	 *@param  listener       The feature to be added to the GraphAccumListener
	 *      attribute
	 */
	public void addGraphAccumListener(GraphAccumListener listener)
	{
		listeners.add(listener);
	}


	/**
	 *  Clear the results
	 */
	public void clear()
	{
		log.debug("Start : clear1");
		samples.clear();
		max = 1;
		bigChange = true;
		this.fireDataChanged();
		log.debug("End : clear1");
	}

	/**
	 *  Add the new sample to the results
	 *
	 *@param  res  sample containing the results
	 */
	public void addNewSample(SampleResult res)
	{
		log.debug("Start : addNewSample1");
		// set time to time taken to load this url without components (e.g. images etc)
		long totalTime = res.getTime();
	
		if (log.isDebugEnabled())
		{
			log.debug("addNewSample1 : time - " + totalTime);
			log.debug("addNewSample1 : max - " + max);
		}
		if (totalTime > max)
		{
			bigChange = true;
			max = totalTime;
		}
		current = res;
		samples.add(res);
		log.debug("End : addNewSample1");
		fireDataChanged();
	}


	/**
	 *  Depending on whether the graph needs to be rescale call the appropriate
	 *  methods
	 */
	protected void fireDataChanged()
	{
		log.debug("Start : fireDataChanged1");
		Iterator iter = listeners.iterator();
		if (bigChange)
		{
			while (iter.hasNext())
			{
				((GraphAccumListener) iter.next()).updateGui();
			}
			bigChange = false;
		} else
		{
			quickUpdate(current);
		}
		log.debug("End : fireDataChanged1");
	}


	/**
	 *  The sample to be added did not exceed the current set of samples so do not
	 *  need to rescale graph
	 *
	 *@param  s  Description of Parameter
	 */
	protected void quickUpdate(SampleResult s)
	{
		Iterator iter = listeners.iterator();
		{
			while (iter.hasNext())
			{
				((GraphAccumListener) iter.next()).updateGui(s);
			}
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

QDox parse error in file://<WORKSPACE>/data/code-rep-dataset/Dataset5/Tasks/1448.java