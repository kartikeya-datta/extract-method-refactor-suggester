error id: file://<WORKSPACE>/data/code-rep-dataset/Dataset5/Tasks/1435.java
file://<WORKSPACE>/data/code-rep-dataset/Dataset5/Tasks/1435.java
### com.thoughtworks.qdox.parser.ParseException: syntax error @[1,16]

error in qdox parser
file content:
```java
offset: 16
uri: file://<WORKSPACE>/data/code-rep-dataset/Dataset5/Tasks/1435.java
text:
```scala
private static L@@og log = LogFactory.getLog(LogConnector.class);

/*
 * $Id$
 * $Revision$
 * $Date$
 *
 * ====================================================================
 * Copyright (c) 2003, Open Edge B.V.
 * All rights reserved.
 * Redistribution and use in source and binary forms, with or without 
 * modification, are permitted provided that the following conditions are met:
 * Redistributions of source code must retain the above copyright notice, 
 * this list of conditions and the following disclaimer. Redistributions 
 * in binary form must reproduce the above copyright notice, this list of 
 * conditions and the following disclaimer in the documentation and/or other 
 * materials provided with the distribution. Neither the name of OpenEdge B.V. 
 * nor the names of its contributors may be used to endorse or promote products 
 * derived from this software without specific prior written permission.
 * 
 * THIS SOFTWARE IS PROVIDED BY THE COPYRIGHT HOLDERS AND CONTRIBUTORS "AS IS" 
 * AND ANY EXPRESS OR IMPLIED WARRANTIES, INCLUDING, BUT NOT LIMITED TO, THE 
 * IMPLIED WARRANTIES OF MERCHANTABILITY AND FITNESS FOR A PARTICULAR PURPOSE 
 * ARE DISCLAIMED. IN NO EVENT SHALL THE COPYRIGHT OWNER OR CONTRIBUTORS BE 
 * LIABLE FOR ANY DIRECT, INDIRECT, INCIDENTAL, SPECIAL, EXEMPLARY, OR 
 * CONSEQUENTIAL DAMAGES (INCLUDING, BUT NOT LIMITED TO, PROCUREMENT OF 
 * SUBSTITUTE GOODS OR SERVICES; LOSS OF USE, DATA, OR PROFITS; OR BUSINESS 
 * INTERRUPTION) HOWEVER CAUSED AND ON ANY THEORY OF LIABILITY, WHETHER IN 
 * CONTRACT, STRICT LIABILITY, OR TORT (INCLUDING NEGLIGENCE OR OTHERWISE) 
 * ARISING IN ANY WAY OUT OF THE USE OF THIS SOFTWARE, EVEN IF ADVISED OF 
 * THE POSSIBILITY OF SUCH DAMAGE.
 */
package nl.openedge.util.jetty;

import java.io.IOException;
import java.io.InputStream;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;

/**
 * Thread for connecting an inputstream to a log.
 * 
 * @author Eelco Hillenius
 */
public class LogConnector extends Thread
{
	/** constant for empty string. */
	private static final String EMPTY_STRING = "";

	/** default interval for reading from stream. */
	private static final int DEFAULT_READ_INTERVAL = 100;

	/** the inputstream to connect. */
	private InputStream inputStream;

	/** string buffer for internal use. */
	private StringBuffer buffer = new StringBuffer();

	/** logger. */
	private Log log = LogFactory.getLog(LogConnector.class);

	/** prefix all output to the log with this. */
	private String linePrefix = "** REMOTE ** >";

	/** postfix all output to the log with this. */
	private String linePostfix = EMPTY_STRING;

	/** interval for reading from the stream. */
	private long readInterval = DEFAULT_READ_INTERVAL;

	/**
	 * @see java.lang.Runnable#run()
	 */
	public void run()
	{
		while (true) // do until break out (like when inputstream closes).
		{
			int c;
			char chr;
			try
			{
				while ((c = inputStream.read()) != -1) // until input stops
				{
					chr = (char) c;
					if (chr == '\n') // if newline
					{
						buffer.insert(0, linePrefix);
						buffer.append(linePostfix);
						log.info(buffer.toString());
						buffer.delete(0, buffer.length());
					}
					else
					// append to buffer until newline
					{
						buffer.append(chr);
					}
				}
				//Thread.sleep(100);
				Thread.yield();
			}
			catch (IOException e)
			{
				log.error(e.getMessage());
				if (buffer.length() > 0) // flush buffer if it is not empty
				{
					log.info(buffer.toString());
				}
				break; // and leave thread
			}
		}
	}

	/**
	 * Get is.
	 * 
	 * @return InputStream Returns the is.
	 */
	public InputStream getInputStream()
	{
		return inputStream;
	}

	/**
	 * Set is.
	 * 
	 * @param is
	 *            is to set.
	 */
	public void setInputStream(InputStream is)
	{
		this.inputStream = is;
	}

	/**
	 * Get log.
	 * 
	 * @return Log Returns the log.
	 */
	public Log getLog()
	{
		return log;
	}

	/**
	 * Set log.
	 * 
	 * @param log
	 *            log to set.
	 */
	public void setLog(Log log)
	{
		this.log = log;
	}

	/**
	 * Get linePrefix; prefix all output to the log with this.
	 * 
	 * @return String Returns the linePrefix.
	 */
	public String getLinePrefix()
	{
		return linePrefix;
	}

	/**
	 * Set linePrefix; prefix all output to the log with this.
	 * 
	 * @param theLinePrefix
	 *            linePrefix to set.
	 */
	public void setLinePrefix(String theLinePrefix)
	{
		if (theLinePrefix == null)
		{
			this.linePrefix = EMPTY_STRING;
		}
		else
		{
			this.linePrefix = theLinePrefix;	
		}
	}

	/**
	 * Get linePostfix; postfix all output to the log with this.
	 * 
	 * @return String Returns the linePostfix.
	 */
	public String getLinePostfix()
	{
		return linePostfix;
	}

	/**
	 * Set linePostfix; postfix all output to the log with this.
	 * 
	 * @param linePostfix
	 *            linePostfix to set.
	 */
	public void setLinePostfix(String linePostfix)
	{
		this.linePostfix = linePostfix;
	}

	/**
	 * Get interval for reading from the stream.
	 * 
	 * @return long Returns the readInterval.
	 */
	public long getReadInterval()
	{
		return readInterval;
	}

	/**
	 * Set interval for reading from the stream.
	 * 
	 * @param readInterval
	 *            readInterval to set.
	 */
	public void setReadInterval(long readInterval)
	{
		this.readInterval = readInterval;
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
	java.base/java.util.concurrent.ForkJoinPool$WorkQueue.topLevelExec(ForkJoinPool.java:1182)
	java.base/java.util.concurrent.ForkJoinPool.scan(ForkJoinPool.java:1655)
	java.base/java.util.concurrent.ForkJoinPool.runWorker(ForkJoinPool.java:1622)
	java.base/java.util.concurrent.ForkJoinWorkerThread.run(ForkJoinWorkerThread.java:165)
```
#### Short summary: 

QDox parse error in file://<WORKSPACE>/data/code-rep-dataset/Dataset5/Tasks/1435.java