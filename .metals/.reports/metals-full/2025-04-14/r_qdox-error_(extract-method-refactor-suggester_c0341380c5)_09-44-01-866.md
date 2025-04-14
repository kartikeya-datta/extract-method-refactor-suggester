error id: file://<WORKSPACE>/data/code-rep-dataset/Dataset5/Tasks/6928.java
file://<WORKSPACE>/data/code-rep-dataset/Dataset5/Tasks/6928.java
### com.thoughtworks.qdox.parser.ParseException: syntax error @[1,8]

error in qdox parser
file content:
```java
offset: 8
uri: file://<WORKSPACE>/data/code-rep-dataset/Dataset5/Tasks/6928.java
text:
```scala
public M@@yComponent(MarkupContainer parent,final String id)

/*
 * $Id$
 * $Revision$
 * $Date$
 *
 * ====================================================================
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *  http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */
package wicket.markup;

import wicket.MarkupContainer;
import wicket.markup.html.WebMarkupContainer;
import wicket.model.Model;

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
	//private Date dateParam;
	private String dateParam;

	/**
	 * Construct.
	 * 
	 * @param id
	 */
	public MyComponent(MarkupContainer<?> parent,final String id)
	{
		super(parent,id, new Model(""));
	}

	/**
	 * Sets the number of rows per page.
	 * 
	 * @param param
	 */
	public void setIntParam(final int param)
	{
		this.intParam = param;
	}

	/**
	 * Sets the number of rows per page.
	 * 
	 * @param param
	 */
	public void setIntegerParam(final Integer param)
	{
		this.integerParam = param;
	}

	/**
	 * Sets the number of rows per page.
	 * 
	 * @param param
	 */
	public void setLong1Param(final long param)
	{
		this.long1Param = param;
	}

	/**
	 * Sets the number of rows per page.
	 * 
	 * @param param
	 */
	public void setLong2Param(final Long param)
	{
		this.long2Param = param;
	}

	/**
	 * Sets the number of rows per page.
	 * 
	 * @param param
	 */
	public void setFloat1Param(final float param)
	{
		this.float1Param = param;
	}

	/**
	 * Sets the number of rows per page.
	 * 
	 * @param param
	 */
	public void setFloat2Param(final Float param)
	{
		this.float2Param = param;
	}

	/**
	 * Sets the number of rows per page.
	 * 
	 * @param param
	 */
	public void setDouble1Param(final double param)
	{
		this.double1Param = param;
	}

	/**
	 * Sets the number of rows per page.
	 * 
	 * @param param
	 */
	public void setDouble2Param(final Double param)
	{
		this.double2Param = param;
	}

	/**
	 * Sets the number of rows per page.
	 * 
	 * @param param
	 */
	//public void setDateParam(final Date param)
	public void setDateParam(final String param)
	{
		this.dateParam = param;
	}

	/**
	 * Sets the number of rows per page.
	 * 
	 * @param param
	 */
	public void setHexParam(final String param)
	{
		this.hexParam = param;
	}
	
	/**
	 * @see wicket.MarkupContainer#onComponentTagBody(wicket.markup.MarkupStream, wicket.markup.ComponentTag)
	 */
	protected void onComponentTagBody(MarkupStream markupStream, ComponentTag openTag)
	{
		StringBuffer str = new StringBuffer();
		
	    str.append("intParam: " + intParam + "<br/>");
	    str.append("integerParam: " + integerParam.toString() + "<br/>");
	    str.append("long1Param: " + long1Param + "<br/>");
	    str.append("long2Param: " + long2Param.toString() + "<br/>");
	    str.append("float1Param: " + float1Param + "<br/>");
	    str.append("float2Param: " + float2Param.toString() + "<br/>");
	    str.append("double1Param: " + double1Param + "<br/>");
	    str.append("double2Param: " + double2Param.toString() + "<br/>");
	    str.append("dateParam: " + dateParam + "<br/>");
	    str.append("hexParam: " + hexParam + "<br/>");
	    
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

QDox parse error in file://<WORKSPACE>/data/code-rep-dataset/Dataset5/Tasks/6928.java