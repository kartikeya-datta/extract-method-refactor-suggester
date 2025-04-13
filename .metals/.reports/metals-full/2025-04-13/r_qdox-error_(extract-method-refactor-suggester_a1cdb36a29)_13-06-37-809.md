error id: file://<WORKSPACE>/data/code-rep-dataset/Dataset5/Tasks/1453.java
file://<WORKSPACE>/data/code-rep-dataset/Dataset5/Tasks/1453.java
### com.thoughtworks.qdox.parser.ParseException: syntax error @[1,26]

error in qdox parser
file content:
```java
offset: 26
uri: file://<WORKSPACE>/data/code-rep-dataset/Dataset5/Tasks/1453.java
text:
```scala
transient private static L@@ogger log = Hierarchy.getDefaultHierarchy().getLoggerFor(

/*
 * ====================================================================
 * The Apache Software License, Version 1.1
 *
 * Copyright (c) 2001 The Apache Software Foundation.  All rights
 * reserved.
 *
 * Redistribution and use in source and binary forms, with or without
 * modification, are permitted provided that the following conditions
 * are met:
 *
 * 1. Redistributions of source code must retain the above copyright
 * notice, this list of conditions and the following disclaimer.
 *
 * 2. Redistributions in binary form must reproduce the above copyright
 * notice, this list of conditions and the following disclaimer in
 * the documentation and/or other materials provided with the
 * distribution.
 *
 * 3. The end-user documentation included with the redistribution,
 * if any, must include the following acknowledgment:
 * "This product includes software developed by the
 * Apache Software Foundation (http://www.apache.org/)."
 * Alternately, this acknowledgment may appear in the software itself,
 * if and wherever such third-party acknowledgments normally appear.
 *
 * 4. The names "Apache" and "Apache Software Foundation" and
 * "Apache JMeter" must not be used to endorse or promote products
 * derived from this software without prior written permission. For
 * written permission, please contact apache@apache.org.
 *
 * 5. Products derived from this software may not be called "Apache",
 * "Apache JMeter", nor may "Apache" appear in their name, without
 * prior written permission of the Apache Software Foundation.
 *
 * THIS SOFTWARE IS PROVIDED ``AS IS'' AND ANY EXPRESSED OR IMPLIED
 * WARRANTIES, INCLUDING, BUT NOT LIMITED TO, THE IMPLIED WARRANTIES
 * OF MERCHANTABILITY AND FITNESS FOR A PARTICULAR PURPOSE ARE
 * DISCLAIMED.  IN NO EVENT SHALL THE APACHE SOFTWARE FOUNDATION OR
 * ITS CONTRIBUTORS BE LIABLE FOR ANY DIRECT, INDIRECT, INCIDENTAL,
 * SPECIAL, EXEMPLARY, OR CONSEQUENTIAL DAMAGES (INCLUDING, BUT NOT
 * LIMITED TO, PROCUREMENT OF SUBSTITUTE GOODS OR SERVICES; LOSS OF
 * USE, DATA, OR PROFITS; OR BUSINESS INTERRUPTION) HOWEVER CAUSED AND
 * ON ANY THEORY OF LIABILITY, WHETHER IN CONTRACT, STRICT LIABILITY,
 * OR TORT (INCLUDING NEGLIGENCE OR OTHERWISE) ARISING IN ANY WAY OUT
 * OF THE USE OF THIS SOFTWARE, EVEN IF ADVISED OF THE POSSIBILITY OF
 * SUCH DAMAGE.
 * ====================================================================
 *
 * This software consists of voluntary contributions made by many
 * individuals on behalf of the Apache Software Foundation.  For more
 * information on the Apache Software Foundation, please see
 * <http://www.apache.org/>.
 */

package org.apache.jmeter.visualizers;

import java.util.ArrayList;
import java.util.List;

import javax.swing.event.TableModelListener;
import javax.swing.table.TableModel;

import org.apache.jmeter.samplers.SampleResult;
import org.apache.jmeter.util.JMeterUtils;
import org.apache.log.Hierarchy;
import org.apache.log.Logger;

/**
 *  This class implements the TableModel for the information kept
 *  by the GraphModel.
 *
 *@author     <a href="mailto:alf@i100.no">Alf Hogemark</a>Hogemark
 *@created    March 10, 2002
 *@version    1.0
 */
public class TableDataModel extends GraphModel implements TableModel
{
	private static Logger log = Hierarchy.getDefaultHierarchy().getLoggerFor(
			"jmeter.gui");
	List urlList = new ArrayList();

	/**
	 *  Constructor for the TableDataModel object
	 */
	public TableDataModel()
	{
		super();
	}

	/**
	 * Gets the GuiClass attribute of the TableModel object
	 *
	 * @return    The GuiClass value
	 */
	public Class getGuiClass()
	{
		return TableVisualizer.class;
	}
	
	public void clear()
	{
		super.clear();
		urlList.clear();
	}

	/**
	 * Gets the ClassLabel attribute of the GraphModel object
	 *
	 * @return    The ClassLabel value
	 */
	public String getClassLabel()
	{
		return "View Results in Table";
	}

	public Sample addNewSample(long time,long timeStamp,boolean success,String url)
	{
		Sample s = super.addNewSample(time,timeStamp,success);
		urlList.add(url);
		return s;
	}

	public Sample addSample(SampleResult e)
	{
		Sample s = addNewSample(e.getTime(),e.getTimeStamp(),e.isSuccessful(),
				(String)e.getSampleLabel());
		fireDataChanged();
		return s;
	}

		  // Implmentation of the TableModel interface
	public int getRowCount()
	{
		return getSampleCount();
	}

	public int getColumnCount()
	{
		// We have two columns : sampleNo and sampleValue
		return 4;
	}

	public String getColumnName(int columnIndex)
	{
		switch(columnIndex)
		{
			case 0:
				return "SampleNo";
			case 1:
				return JMeterUtils.getResString("url");
			case 2:
				return "Sample - ms";
			case 3:
				return JMeterUtils.getResString("Success?");
			default:
				return null;
		}
	}

	public Class getColumnClass(int columnIndex)
	{
		if(columnIndex == 0)
		{
			return Integer.class;
		}
		else if(columnIndex == 1)
		{
			return String.class;
		}
		else if(columnIndex == 2)
		{
			return Long.class;
		}
		else if(columnIndex == 3)
		{
			return Boolean.class;
		}
		else
		{
			return null;
		}
	}

	public boolean isCellEditable(int rowIndex, int columnIndex)
	{
		return false;
	}

	public Object getValueAt(int rowIndex, int columnIndex)
	{
		if(columnIndex == 0)
		{
			if((rowIndex >= 0) && (rowIndex < getSampleCount()))
			{
				return new Integer(rowIndex);
			}
		}
		else if(columnIndex == 1)
		{
			log.info("rowIndex = "+rowIndex);
			if((rowIndex >= 0) && (rowIndex < urlList.size()))
			{
				log.info(" url = "+urlList.get(rowIndex));
				return urlList.get(rowIndex);
			}
		}
		else if(columnIndex == 2)
		{
			if((rowIndex >= 0) && (rowIndex < getSampleCount()))
			{
				return new Long(((Sample)getSamples().get(rowIndex)).data);
			}
		}
		else if(columnIndex == 3)
		{
			if((rowIndex >= 0) && (rowIndex < urlList.size()))
			{
				return new Boolean(!((Sample)getSamples().get(rowIndex)).error);
			}
		}
		return null;
	}

	/**
	 * Dummy implementation
	 */
	public void setValueAt(Object aValue, int rowIndex, int columnIndex)
	{
	}

	/**
	 * Dummy implementation
	 */
	public void addTableModelListener(TableModelListener l)
	{
	}

	/**
	 * Dummy implementation
	 */
	public void removeTableModelListener(TableModelListener l)
	{
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

QDox parse error in file://<WORKSPACE>/data/code-rep-dataset/Dataset5/Tasks/1453.java