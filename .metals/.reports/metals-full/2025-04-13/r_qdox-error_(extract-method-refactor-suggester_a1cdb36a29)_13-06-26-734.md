error id: file://<WORKSPACE>/data/code-rep-dataset/Dataset5/Tasks/15939.java
file://<WORKSPACE>/data/code-rep-dataset/Dataset5/Tasks/15939.java
### com.thoughtworks.qdox.parser.ParseException: syntax error @[1,15]

error in qdox parser
file content:
```java
offset: 15
uri: file://<WORKSPACE>/data/code-rep-dataset/Dataset5/Tasks/15939.java
text:
```scala
private final S@@tring message;

/*
 * Licensed to the Apache Software Foundation (ASF) under one or more
 * contributor license agreements.  See the NOTICE file distributed with
 * this work for additional information regarding copyright ownership.
 * The ASF licenses this file to You under the Apache License, Version 2.0
 * (the "License"); you may not use this file except in compliance with
 * the License.  You may obtain a copy of the License at
 *
 *      http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */
package org.apache.wicket.protocol.http.request;

import java.io.IOException;

import javax.servlet.http.HttpServletResponse;

import org.apache.wicket.IRequestTarget;
import org.apache.wicket.RequestCycle;
import org.apache.wicket.WicketRuntimeException;
import org.apache.wicket.protocol.http.WebRequestCycle;
import org.apache.wicket.protocol.http.WebResponse;


/**
 * Response target that is to be used in a servlet environment to send an error
 * code and optionally a message. NOTE: this target can only be used in a
 * servlet environment with {@link org.apache.wicket.protocol.http.WebRequestCycle}s.
 * 
 * @author Eelco Hillenius
 */
public final class WebErrorCodeResponseTarget implements IRequestTarget
{
	/** the servlet error code. */
	private final int errorCode;

	/** the optional message to send to the client. */
	private String message;

	/**
	 * Construct.
	 * 
	 * @param errorCode
	 *            the servlet error code; use one of the
	 *            {@link javax.servlet.http.HttpServletResponse} constants
	 * @see javax.servlet.http.HttpServletResponse
	 */
	public WebErrorCodeResponseTarget(int errorCode)
	{
		this(errorCode, null);
	}

	/**
	 * Construct.
	 * 
	 * @param errorCode
	 *            the servlet error code; use one of the
	 *            {@link javax.servlet.http.HttpServletResponse} constants
	 * @param message
	 *            the optional message to send to the client
	 * @see javax.servlet.http.HttpServletResponse
	 */
	public WebErrorCodeResponseTarget(int errorCode, String message)
	{
		this.errorCode = errorCode;
		this.message = message;
	}

	/**
	 * Respond by sending the set errorCode and optionally the message to the
	 * browser.
	 * 
	 * @see org.apache.wicket.IRequestTarget#respond(org.apache.wicket.RequestCycle)
	 */
	public void respond(RequestCycle requestCycle)
	{
		WebResponse webResponse = ((WebRequestCycle)requestCycle).getWebResponse();
		HttpServletResponse httpServletResponse = webResponse.getHttpServletResponse();
		try
		{
			if (message != null)
			{
				httpServletResponse.sendError(errorCode, message);
			}
			else
			{
				httpServletResponse.sendError(errorCode);
			}
		}
		catch (IOException e)
		{
			throw new WicketRuntimeException(e);
		}
	}

	/**
	 * Gets the servlet error code.
	 * 
	 * @return the servlet error code
	 */
	public final int getErrorCode()
	{
		return errorCode;
	}

	/**
	 * Gets the optional message to send to the client.
	 * 
	 * @return the optional message to send to the client
	 */
	public final String getMessage()
	{
		return message;
	}

	/**
	 * @see org.apache.wicket.IRequestTarget#detach(org.apache.wicket.RequestCycle)
	 */
	public void detach(RequestCycle requestCycle)
	{
	}

	/**
	 * @see java.lang.Object#equals(java.lang.Object)
	 */
	public boolean equals(Object obj)
	{
		boolean equal = false;
		if (obj instanceof WebErrorCodeResponseTarget)
		{
			WebErrorCodeResponseTarget that = (WebErrorCodeResponseTarget)obj;
			if (errorCode == that.errorCode)
			{
				if (message != null)
				{
					equal = (that.message != null && message.equals(that.message));
				}
				else
				{
					equal = (that.message == null);
				}
			}
		}
		return equal;
	}

	/**
	 * @see java.lang.Object#hashCode()
	 */
	public int hashCode()
	{
		int result = "WebErrorCodeResponseTarget".hashCode();
		result += message != null ? message.hashCode() : 0;
		result += errorCode;
		return 17 * result;
	}

	/**
	 * @see java.lang.Object#toString()
	 */
	public String toString()
	{
		return "[WebErrorCodeResponseTarget@" + hashCode() + " errorCode=" + getErrorCode()
				+ ((message != null) ? " (" + message + ")" : "" + "]");
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

QDox parse error in file://<WORKSPACE>/data/code-rep-dataset/Dataset5/Tasks/15939.java