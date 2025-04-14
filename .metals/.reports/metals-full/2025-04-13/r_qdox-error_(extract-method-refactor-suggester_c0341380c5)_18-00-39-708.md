error id: file://<WORKSPACE>/data/code-rep-dataset/Dataset5/Tasks/3005.java
file://<WORKSPACE>/data/code-rep-dataset/Dataset5/Tasks/3005.java
### com.thoughtworks.qdox.parser.ParseException: syntax error @[1,1]

error in qdox parser
file content:
```java
offset: 1
uri: file://<WORKSPACE>/data/code-rep-dataset/Dataset5/Tasks/3005.java
text:
```scala
r@@eturn getProperty1() + ", " + getProperty2() + ", " + getProperty3();

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
package org.apache.wicket.examples.ajax.builtin.tree;

import java.io.Serializable;

/**
 * Bean that is set to every node of tree as user object.
 * This bean has properties that are used to hold values for the cells.
 *  
 * @author Matej Knopp
 */
public class ModelBean implements Serializable
{
	private String property1;
	private String property2;
	private String property3;
	private String property4;
	private String property5;
	private String property6;

	/**
	 * Creates the bean. 
	 * 
	 * @param s
	 *		String that will be suffix of each property. 		
	 */
	public ModelBean(String s)
	{
		property1 = "1:" + s;
		property2 = "2:" + s;
		property3 = "3:" + s;
		property4 = "4:" + s;
		property5 = "5:" + s;
		property6 = "6:" + s;
	}

	/**
	 * Returns the first property.
	 * 
	 * @return
	 * 		First property
	 */
	public String getProperty1()
	{
		return property1;
	}

	/**
	 * Sets the value of first property.
	 * 
	 * @param property1
	 * 		Mew value
	 */
	public void setProperty1(String property1)
	{
		this.property1 = property1;
	}

	/**
	 * Returns the second property.
	 * 
	 * @return
	 * 		Second property
	 */
	public String getProperty2()
	{
		return property2;
	}

	/**
	 * Sets the value of second property
	 * 
	 * @param property2
	 * 			New value
	 */
	public void setProperty2(String property2)
	{
		this.property2 = property2;
	}

	/**
	 * Returns the value of third property.
	 * 
	 * @return
	 * 		Third property
	 */
	public String getProperty3()
	{
		return property3;
	}

	/**
	 * Sets the value of third property
	 * 
	 * @param property3
	 * 		New value
	 */
	public void setProperty3(String property3)
	{
		this.property3 = property3;
	}

	/**
	 * Returns the value of fourth property
	 * 
	 * @return
	 * 		Value of fourth property
	 */
	public String getProperty4()
	{
		return property4;
	}

	/**
	 * Sets the value of fourth property
	 * 
	 * @param property4
	 * 		New value
	 */
	public void setProperty4(String property4)
	{
		this.property4 = property4;
	}

	/**
	 * Returns the value of fifth property
	 * 
	 * @return
	 * 		Value of fifth property	
	 */
	public String getProperty5()
	{
		return property5;
	}

	/**
	 * Sets the value of fifth property
	 * 
	 * @param property5
	 * 		New value
	 */
	public void setProperty5(String property5)
	{
		this.property5 = property5;
	}
	
	/**
	 * Returns the value of sixth property.
	 * 
	 * @return
	 * 		Value of sixth property
	 */
	public String getProperty6()
	{
		return property6;
	}

	/**
	 * Sets the value of sixth property
	 * 
	 * @param property6
	 * 		New value
	 */
	public void setProperty6(String property6)
	{
		this.property6 = property6;
	}
	
	/**
	 * @see java.lang.Object#toString()
	 */
	@Override
	public String toString()
	{
		return getProperty1();
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

QDox parse error in file://<WORKSPACE>/data/code-rep-dataset/Dataset5/Tasks/3005.java