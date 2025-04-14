error id: file://<WORKSPACE>/data/code-rep-dataset/Dataset5/Tasks/14190.java
file://<WORKSPACE>/data/code-rep-dataset/Dataset5/Tasks/14190.java
### com.thoughtworks.qdox.parser.ParseException: syntax error @[1,1]

error in qdox parser
file content:
```java
offset: 1
uri: file://<WORKSPACE>/data/code-rep-dataset/Dataset5/Tasks/14190.java
text:
```scala
r@@eturn cursor < combos.length;

/*
 * Licensed to the Apache Software Foundation (ASF) under one
 * or more contributor license agreements.  See the NOTICE file
 * distributed with this work for additional information
 * regarding copyright ownership.  The ASF licenses this file
 * to you under the Apache License, Version 2.0 (the
 * "License"); you may not use this file except in compliance
 * with the License.  You may obtain a copy of the License at
 *
 * http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing,
 * software distributed under the License is distributed on an
 * "AS IS" BASIS, WITHOUT WARRANTIES OR CONDITIONS OF ANY
 * KIND, either express or implied.  See the License for the
 * specific language governing permissions and limitations
 * under the License.    
 */
package org.apache.openjpa.persistence.test;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.BitSet;
import java.util.List;

import org.apache.openjpa.persistence.jdbc.update.TestParentChild;

/**
 * Aids to run a single test under different combination of configuration
 * parameters.
 * 
 * Each configurable property can be registered to this receiver with all its
 * possible values. This class generates all combinations of all the possible
 * property values as configuration and invokes the same test with each 
 * configuration combination.
 * The properties can be designated as <em>runtime</code> to be included in
 * combination for execution but excluded from configuration.
 * 
 *  For example,
 *  @see TestParentChild
 *  
 * @author Pinaki Poddar
 *
 */
public class CombinatorialTestHelper {
	private CombinationGenerator geneartor;
	private List<String> propertyKeys;
	private List currentOption;
	private BitSet runtimeKeys = new BitSet();
	
	private List[] combos;
	private int cursor;
	
	public CombinatorialTestHelper() {
		geneartor = new CombinationGenerator();
		propertyKeys = new ArrayList<String>();
		currentOption = null;
		runtimeKeys = new BitSet();
		combos = null;
		cursor = 0;
	}
	
	/**
	 * Generates the key-value property array as expected by its superclass
	 * by appending the current combinatorially generated properties.
	 * 
	 * The important side effect of this method is to set the current 
	 * configuration options.
	 * 
	 * If no property is configured for combinatorial generation then returns
	 * the given list as it is.
	 * 
	 */
	Object[] setCombinatorialOption(Object[] props) {
		if (propertyKeys.isEmpty() || 
			propertyKeys.size() == runtimeKeys.cardinality())
			return props;
		
		if (combos == null) {
			combos = geneartor.generate();
			cursor = 0;
		}
		// Each non-runtime property contributes a key-value pair
		Object[] options = new Object[2*(propertyKeys.size()- 
				runtimeKeys.cardinality())];
		currentOption = combos[cursor++];
		int k = 0;
		for (int i = 0; i < propertyKeys.size(); i++) {
			if (runtimeKeys.get(i))
				continue;
			options[k++] = propertyKeys.get(i);
			options[k++] = currentOption.get(i);
		}
		if (props == null || props.length == 0)
			return options;
		
		Object[] newProps = new Object[props.length + options.length];
		System.arraycopy(props, 0, newProps, 0, props.length);
		System.arraycopy(options, 0, newProps, props.length, options.length);
		return newProps;
	}
	
	/**
	 * Adds options for the given configuration property.
	 */
	public void addOption(String property, Object[] options) {
		addOption(property, options, false);
	}
	
	/**
	 * Adds options for the given configuration property.
	 */
	public void addOption(String property, List options) {
		addOption(property, options, false);
	}
	
	/**
	 * Adds options for the given property.
	 * If runtime is true then this property is not added to configuration.
	 */
	public void addOption(String property, Object[] options, boolean runtime) {
		addOption(property, Arrays.asList(options), runtime);
	}

	/**
	 * Adds options for the given property.
	 * If runtime is true then this property is not added to configuration.
	 */
	public void addOption(String property, List options, boolean runtime) {
		if (geneartor == null) {
			geneartor = new CombinationGenerator();
		}
		if (propertyKeys == null) {
			propertyKeys = new ArrayList<String>();
		}
		if (!propertyKeys.contains(property)) {
			geneartor.addDimension(options);
			propertyKeys.add(property);
			if (runtime) runtimeKeys.set(propertyKeys.size()-1);
		}
	}

	/**
	 * Gets the value of current option for the given key.
	 * Raises exception if the given key is not an option.
	 */
	public Object getOption(String key) {
		int index = propertyKeys.indexOf(key);
		if (index == -1)
			throw new IllegalArgumentException("Unknown option " + key);
		return currentOption.get(index);
	}
	
	/**
	 * Gets the string value of current option for the given key.
	 * Raises exception if the given key is not an option.
	 */
	public String getOptionAsString(String key) {
		return getOption(key).toString();
	}

	/**
	 * Gets the values of the current options.
	 */
	public List getOptions() {
		return currentOption;
	}
	
	/**
	 * Gets the key and values of the current options as printable string.
	 */
	public String getOptionsAsString() {
		StringBuffer buf = new StringBuffer();
		for (int i = 0; i <propertyKeys.size(); i++) {
			String key = propertyKeys.get(i);
			if (!runtimeKeys.get(i))
				buf.append(key +  " : " + getOption(key)).append("\r\n");
		}
		for (int i = 0; i <propertyKeys.size(); i++) {
			String key = propertyKeys.get(i);
			if (runtimeKeys.get(i))
				buf.append("* " + key +  " : " + getOption(key)).append("\r\n");
		}
		return buf.toString();
	}
	
	/**
	 * Affirms if this receiver has more combinations.
	 */
	public boolean hasMoreCombination() {
		return cursor < combos.length-1;
	}
	
	/**
	 * Gets total number of combinations.
	 */
	public int getCombinationSize() {
		return geneartor == null ? 0 : geneartor.getSize();
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

QDox parse error in file://<WORKSPACE>/data/code-rep-dataset/Dataset5/Tasks/14190.java