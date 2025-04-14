error id: file://<WORKSPACE>/data/code-rep-dataset/Dataset5/Tasks/660.java
file://<WORKSPACE>/data/code-rep-dataset/Dataset5/Tasks/660.java
### com.thoughtworks.qdox.parser.ParseException: syntax error @[1,1]

error in qdox parser
file content:
```java
offset: 1
uri: file://<WORKSPACE>/data/code-rep-dataset/Dataset5/Tasks/660.java
text:
```scala
i@@f ((value.toString() != null) && value.toString().equals(this.toString()))

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
package org.apache.wicket.util.lang;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import org.apache.wicket.util.string.StringValue;


/**
 * A base class for defining enumerated types. Since this class extends StringValue, every
 * enumerated type subclass is a StringValue that can be manipulated, converted and displayed in
 * useful ways. In addition to constructing a type with the given name, lists are kept of all
 * enumeration values by subclass. The list of available values in the enumeration represented by a
 * given subclass can be retrieved by calling getValues(Class).
 * 
 * @author Jonathan Locke
 */
public abstract class EnumeratedType extends StringValue
{
	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	/** Map of type values by class */
	private static final Map<String, List<EnumeratedType>> valueListByClass = Generics.newConcurrentHashMap();

	/**
	 * Constructor.
	 * 
	 * @param name
	 *            Name of this enumerated type value
	 */
	public EnumeratedType(final String name)
	{
		super(name);

		// Add this object to the list of values for our subclass
		getValues(getClass()).add(this);
	}

	/**
	 * Gets the enumerated type values for a given subclass of EnumeratedType.
	 * 
	 * @param c
	 *            The enumerated type subclass to get values for
	 * @return List of all values of the given subclass
	 */
	public static List<EnumeratedType> getValues(final Class<? extends EnumeratedType> c)
	{
		// Get values for class
		List<EnumeratedType> valueList = valueListByClass.get(c.getName());

		// If no list exists
		if (valueList == null)
		{
			// create lazily
			valueList = new ArrayList<EnumeratedType>();
			valueListByClass.put(c.getName(), valueList);
		}

		return valueList;
	}

	/**
	 * Method to ensure that == works after deserialization
	 * 
	 * @return object instance
	 * @throws java.io.ObjectStreamException
	 */
	public Object readResolve() throws java.io.ObjectStreamException
	{
		EnumeratedType result = this;
		List<EnumeratedType> values = getValues(getClass());
		if (values != null)
		{
			for (EnumeratedType value : values)
			{
				if (value.toString() != null && value.toString().equals(this.toString()))
				{
					result = value;
					break;
				}
			}
		}
		return result;
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

QDox parse error in file://<WORKSPACE>/data/code-rep-dataset/Dataset5/Tasks/660.java