error id: file://<WORKSPACE>/data/code-rep-dataset/Dataset5/Tasks/17515.java
file://<WORKSPACE>/data/code-rep-dataset/Dataset5/Tasks/17515.java
### com.thoughtworks.qdox.parser.ParseException: syntax error @[1,8]

error in qdox parser
file content:
```java
offset: 8
uri: file://<WORKSPACE>/data/code-rep-dataset/Dataset5/Tasks/17515.java
text:
```scala
public v@@oid onComponentTagBody(MarkupStream markupStream, ComponentTag openTag)

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
package org.apache.wicket.markup;

import org.apache.wicket.markup.html.WebMarkupContainer;
import org.apache.wicket.model.Model;

/**
 * Dummy component used for ComponentCreateTagTest
 * 
 * @author Juergen Donnerstag
 */
public class MyComponent extends WebMarkupContainer
{
	private static final long serialVersionUID = 1L;

	private int intParam;
	private Integer integerParam;
	private long long1Param;
	private Long long2Param;
	private float float1Param;
	private Float float2Param;
	private double double1Param;
	private Double double2Param;
	private String hexParam;
	// private Date dateParam;
	private String dateParam;

	/**
	 * Construct.
	 * 
	 * @param id
	 */
	public MyComponent(final String id)
	{
		super(id, new Model<String>(""));
	}

	/**
	 * Sets the number of rows per page.
	 * 
	 * @param intParam
	 */
	public void setIntParam(final int intParam)
	{
		this.intParam = intParam;
	}

	/**
	 * Sets the number of rows per page.
	 * 
	 * @param integerParam
	 */
	public void setIntegerParam(final Integer integerParam)
	{
		this.integerParam = integerParam;
	}

	/**
	 * Sets the number of rows per page.
	 * 
	 * @param long1Param
	 */
	public void setLong1Param(final long long1Param)
	{
		this.long1Param = long1Param;
	}

	/**
	 * Sets the number of rows per page.
	 * 
	 * @param long2Param
	 */
	public void setLong2Param(final Long long2Param)
	{
		this.long2Param = long2Param;
	}

	/**
	 * Sets the number of rows per page.
	 * 
	 * @param float1Param
	 */
	public void setFloat1Param(final float float1Param)
	{
		this.float1Param = float1Param;
	}

	/**
	 * Sets the number of rows per page.
	 * 
	 * @param float2Param
	 */
	public void setFloat2Param(final Float float2Param)
	{
		this.float2Param = float2Param;
	}

	/**
	 * Sets the number of rows per page.
	 * 
	 * @param double1Param
	 */
	public void setDouble1Param(final double double1Param)
	{
		this.double1Param = double1Param;
	}

	/**
	 * Sets the number of rows per page.
	 * 
	 * @param double2Param
	 */
	public void setDouble2Param(final Double double2Param)
	{
		this.double2Param = double2Param;
	}

	/**
	 * Sets the number of rows per page.
	 * 
	 * @param dateParam
	 */
	// public void setDateParam(final Date dateParam)
	public void setDateParam(final String dateParam)
	{
		this.dateParam = dateParam;
	}

	/**
	 * Sets the number of rows per page.
	 * 
	 * @param hexParam
	 */
	public void setHexParam(final String hexParam)
	{
		this.hexParam = hexParam;
	}

	/**
	 * @see org.apache.wicket.MarkupContainer#onComponentTagBody(org.apache.wicket.markup.MarkupStream,
	 *      org.apache.wicket.markup.ComponentTag)
	 */
	@Override
	protected void onComponentTagBody(MarkupStream markupStream, ComponentTag openTag)
	{
	 StringBuilder str = new StringBuilder();

		str.append("intParam: ").append(intParam).append("<br/>");
		str.append("integerParam: ").append(integerParam.toString()).append("<br/>");
		str.append("long1Param: ").append(long1Param).append("<br/>");
		str.append("long2Param: ").append(long2Param.toString()).append("<br/>");
		str.append("float1Param: ").append(float1Param).append("<br/>");
		str.append("float2Param: ").append(float2Param.toString()).append("<br/>");
		str.append("double1Param: ").append(double1Param).append("<br/>");
		str.append("double2Param: ").append(double2Param.toString()).append("<br/>");
		str.append("dateParam: ").append(dateParam).append("<br/>");
		str.append("hexParam: ").append(hexParam).append("<br/>");

		getResponse().write(str);

		super.onComponentTagBody(markupStream, openTag);
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

QDox parse error in file://<WORKSPACE>/data/code-rep-dataset/Dataset5/Tasks/17515.java