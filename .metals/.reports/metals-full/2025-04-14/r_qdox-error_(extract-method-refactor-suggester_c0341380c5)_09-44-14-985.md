error id: file://<WORKSPACE>/data/code-rep-dataset/Dataset5/Tasks/1489.java
file://<WORKSPACE>/data/code-rep-dataset/Dataset5/Tasks/1489.java
### com.thoughtworks.qdox.parser.ParseException: syntax error @[1,26]

error in qdox parser
file content:
```java
offset: 26
uri: file://<WORKSPACE>/data/code-rep-dataset/Dataset5/Tasks/1489.java
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
 package org.apache.jmeter.save.old.xml;

import java.io.IOException;
import java.io.Writer;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.util.Iterator;
import java.util.LinkedList;

import org.apache.jmeter.save.old.SaveHandler;
import org.apache.log.Hierarchy;
import org.apache.log.Logger;
import org.xml.sax.Attributes;

/**
 * Title:
 * Description:
 * Copyright:    Copyright (c) 2000
 * Company:
 * @author
 * @version 1.0
 */

public abstract class TagHandler implements SaveHandler
{
	private static Logger log = Hierarchy.getDefaultHierarchy().getLoggerFor(
			"jmeter.util");
	private LinkedList tagsIn = new LinkedList();
	private boolean done = false;
	protected String tagName;
	protected XmlHandler xmlParent;

	private StringBuffer dataStore;


	public abstract void setAtts(Attributes atts) throws Exception;

	public void setXmlParent(XmlHandler xml)
	{
		xmlParent = xml;
	}

	public void startSave(Writer out) throws IOException
	{
		out.write("<?xml version=\"1.0\" encoding=\"UTF-8\"?>\n\n");
	}

	/************************************************************
	 *  Indicates whether this tag has been completely parsed
	 *
	 *@return    True or false whether this tag is done being parsed
	 ***********************************************************/
	public boolean isDone()
	{
		return done;
	}

	protected void setDone(boolean done)
	{
		this.done = done;
	}

	public void unknownTagStart(String name,Attributes atts)
	{
	}

	public void unknownTagEnd(String name)
	{
	}

	public void setData(String data)
	{
	}

	public String getTagName()
	{
		return tagName;
	}

	public void setTagName(String tagName)
	{
		this.tagName = tagName;
	}

	public abstract Object getModel();

	/************************************************************
	 *  Handles character data passed to this class
	 *
	 *@param  data  The data within the XML tag to be handled
	 ***********************************************************/
	public void handleData(String data)
	{
		this.callTagCombo(data);
	}

	protected void startTag(String tag)
	{
		if(!tagName.equals(tag))
			tagsIn.addLast(tag);
	}

	protected void endTag(String tag)
	{
		if(tagName.equals(tag))
		{
			setDone(true);
		}
		else
		{
			tagsIn.removeLast();
		}
	}

	protected String getMethodName()
	{
		StringBuffer methodName = new StringBuffer();
		boolean first = true;
		for (Iterator it = tagsIn.iterator(); it.hasNext(); )
		{
			if (!first)
			{
				methodName.append("_");
			}
			else
			{
				first = false;
			}
			methodName.append(it.next());
		}
		return methodName.toString();
	}

	protected void callTagCombo(Attributes atts)
	{
		try
		{
			Method method = this.getClass().getMethod(getMethodName(), new Class[]{Attributes.class});
			method.invoke(this, new Object[]{atts});
		}
		catch(NoSuchMethodException e)
		{
		}
		catch (Exception e)
		{
			log.error("",e);
		}
	}

	protected void callTagCombo(String data)
	{
		if(dataStore == null)
		{
			dataStore = new StringBuffer();
		}
		dataStore.append(data);
	}

	private void releaseData()
	{
		if(dataStore != null)
		{
			try
			{
				if(getMethodName().equals(""))
				{
					setData(dataStore.toString());
				}
				else
				{
					Method method = this.getClass().getMethod(getMethodName(), new Class[]{String.class});
					method.invoke(this, new Object[]{dataStore.toString()});
				}
			}
			catch(NoSuchMethodException e)
			{
			}
			catch (Exception ex)
			{
				log.error("",ex);
			}
		}
		dataStore = null;
	}

	protected void passToHandler(String localName, Attributes atts)
	{
		releaseData();
		try
		{
			Class handler = this.getClass();
			startTag(localName);
			Method method = handler.getMethod(localName + "TagStart", new Class[]{Attributes.class});
			method.invoke(this, new Object[]{atts});
		}
		catch (NoSuchMethodException e)
		{
			unknownTagStart(localName,atts);
		}
		catch(InvocationTargetException e)
		{
			log.error("",e);
		}
		catch(IllegalAccessException e)
		{
			log.error("",e);
		}
		callTagCombo(atts);
	}

	protected void passToHandler(String localName)
	{
		releaseData();
		try
		{
			Class handler = this.getClass();
			endTag(localName);
			Method method = handler.getMethod(localName + "TagEnd", new Class[0]);
			method.invoke(this, new Object[0]);
		}
		catch (NoSuchMethodException e)
		{
			unknownTagEnd(localName);
		}
		catch(InvocationTargetException e)
		{
			log.error("",e);
		}
		catch(IllegalAccessException e)
		{
			log.error("",e);
		}
	}

	public void notifySubElementEnded(Object childObj)
	{
	}
}
 No newline at end of file
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

QDox parse error in file://<WORKSPACE>/data/code-rep-dataset/Dataset5/Tasks/1489.java