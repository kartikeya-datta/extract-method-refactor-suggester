error id: file://<WORKSPACE>/data/code-rep-dataset/Dataset5/Tasks/1483.java
file://<WORKSPACE>/data/code-rep-dataset/Dataset5/Tasks/1483.java
### com.thoughtworks.qdox.parser.ParseException: syntax error @[1,26]

error in qdox parser
file content:
```java
offset: 26
uri: file://<WORKSPACE>/data/code-rep-dataset/Dataset5/Tasks/1483.java
text:
```scala
transient private static L@@ogger log = Hierarchy.getDefaultHierarchy().getLoggerFor("jmeter.engine");

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
 *  WARRANTIES, INCLUDING, BUT NOT LIMITED TO, THE IMPLIED WARRANTIES
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
package org.apache.jmeter.samplers;
import java.io.Serializable;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;

import org.apache.avalon.framework.configuration.Configuration;
import org.apache.jmeter.assertions.AssertionResult;
import org.apache.jmeter.testelement.TestElement;
import org.apache.log.Hierarchy;
import org.apache.log.Logger;

/**
 *  This is a nice packaging for the various information returned from taking a
 *  sample of an entry.
 *
 *@author     $Author$
 *@created    $Date$
 *@version    $Revision$
 */
public class SampleResult implements Serializable
{
	//public final static String URL = "sampler.url";

	public final static String TAG_NAME = "sampleResult";
	public final static String TEXT = "text";
	public final static String BINARY = "bin";
	private byte[] responseData;
	private String responseCode;
	private String label;
	private TestElement samplerData;
	private String threadName;
	private String responseMessage;
	private long timeStamp = 0;
	private List assertionResults = new ArrayList();
	private List subResults = new ArrayList();
	private String dataType;
	private boolean success;
	private boolean mark = false;
	private Set files = new HashSet();
	

	Map map;
	long time;
	/**
	 *  Description of the Field
	 */
	private final static String SAMPLE_LABEL = "displayName";
	private final static String SAMPLER_CONFIG = "samplerConfig";
	private final static String DATA_TYPE = "dataType";
	private final static String RESPONSE_CODE = "responseCode";
	private final static String RESPONSE_MESSAGE = "responseMessage";
	private final static String THREAD_NAME = "threadName";
	private final static String TIME_STAMP = "timeStamp";
	/**
	 *  Description of the Field
	 */
	private final static String ASSERTION_RESULTS = "assertionResults";
	/**
	 *  Description of the Field
	 */
	private final static String RESPONSE_DATA = "responseData";
	/**
	 *  Description of the Field
	 */
	private final static String SUCCESS = "success";

	/**
	 *  Description of the Field
	 */
	private final static String TOTAL_TIME = "totalTime";

	private static Logger log = Hierarchy.getDefaultHierarchy().getLoggerFor("jmeter.engine");


	/**
	 *  !ToDo (Constructor description)
	 */
	public SampleResult()
	{
		map = new HashMap();
		time = 0;
	}
	
	public void setMarked(String filename)
	{
		files.add(filename);
	}
	
	public boolean isMarked(String filename)
	{
		return files.contains(filename);
	}	
	
	public String getResponseCode()
	{
		return responseCode;
	}
	
	public void setResponseCode(String code)
	{
		responseCode = code;
	}
	
	public String getResponseMessage()
	{
		return responseMessage;
	}
	
	public void setResponseMessage(String msg)
	{
		responseMessage = msg;
	}
	
	public String getThreadName()
	{
		return threadName;
	}
	
	public void setThreadName(String threadName)
	{
		this.threadName = threadName;
	}
	
	public long getTimeStamp()
	{
		return timeStamp;
	}
	
	public void setTimeStamp(long timeStamp)
	{
		this.timeStamp = timeStamp;
	}

	public String getSampleLabel()
	{
		return label;
	}

	public void setSampleLabel(String label)
	{
		this.label = label;
	}

	public void addAssertionResult(AssertionResult assertResult)
	{
		assertionResults.add(assertResult);
	}

	public AssertionResult[] getAssertionResults()
	{
		return (AssertionResult[])assertionResults.toArray(new AssertionResult[0]);
	}

	public void addSubResult(SampleResult subResult)
	{
		subResults.add(subResult);
	}

	public SampleResult[] getSubResults()
	{
		return (SampleResult[])subResults.toArray(new SampleResult[0]);
	}

	/**
	 *  Description of the Method
	 *
	 *@param  info  Description of the Parameter
	 */
	public void configure(Configuration info)
	{
		setTime(info.getAttributeAsLong(TOTAL_TIME,0L));
	}

	/**
	 *  Set the time this sample took to occur.
	 *
	 *@param  t  !ToDo (Parameter description)
	 */
	public void setTime(long t)
	{
		time = t;
	}

	/**
	 *  Sets the responseData attribute of the SampleResult object
	 *
	 *@param  response  The new responseData value
	 */
	public void setResponseData(byte[] response)
	{
		responseData = response;
		/*DefaultConfiguration responseChild = new DefaultConfiguration(RESPONSE_DATA, "");
		responseChild.setValue(getHexString(response));
		addChild(responseChild);*/
	}

	


	/**
	 *  Gets the responseData attribute of the SampleResult object
	 *
	 *@return    The responseData value
	 */
	public byte[] getResponseData()
	{
		return responseData;
		/*Configuration responseChild = getChild(RESPONSE_DATA);
		ByteArrayOutputStream bytes = new ByteArrayOutputStream();
		String res = responseChild.getValue("");
		for (int i = 0; i < res.length(); i+=2)
		{
			bytes.write(Integer.parseInt(res.substring(i,i+2),16));
		}
		return bytes.toByteArray();*/
	}

	public void setSamplerData(TestElement s)
	{
		samplerData = s;
	}

	public TestElement getSamplerData()
	{
		return samplerData;
	}

	/**
	 *  Get the time it took this sample to occur.
	 *
	 *@return    !ToDo (Return description)
	 */
	public long getTime()
	{
		return time;
	}

	/**
	 *  !ToDoo (Method description)
	 *
	 *@return    !ToDo (Return description)
	 */
	public boolean isSuccessful()
	{
		return success;
	}
	
	public void setDataType(String dataType)
	{
		this.dataType = dataType;
	}
	
	public String getDataType()
	{
		return dataType;
	}

	/**
	 *  Sets the successful attribute of the SampleResult object
	 *
	 *@param  success  The new successful value
	 */
	public void setSuccessful(boolean success)
	{
		this.success = success;
	}

	/**
	 *  Returns the display name
	 *
	 *@return    display name of this sample result
	 */
	public String toString()
	{
		return getSampleLabel();
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

QDox parse error in file://<WORKSPACE>/data/code-rep-dataset/Dataset5/Tasks/1483.java