error id: file://<WORKSPACE>/data/code-rep-dataset/Dataset3/Tasks/5172.java
file://<WORKSPACE>/data/code-rep-dataset/Dataset3/Tasks/5172.java
### com.thoughtworks.qdox.parser.ParseException: syntax error @[1,1]

error in qdox parser
file content:
```java
offset: 1
uri: file://<WORKSPACE>/data/code-rep-dataset/Dataset3/Tasks/5172.java
text:
```scala
d@@ynaBean.set(propertyName, Double.valueOf(v));

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
package org.apache.commons.math.stat.univariate;

import java.io.Serializable;
import java.lang.reflect.InvocationTargetException;
import java.util.ArrayList;
import java.util.List;

import org.apache.commons.beanutils.PropertyUtils;
import org.apache.commons.beanutils.DynaBean;
import org.apache.commons.beanutils.BasicDynaClass;
import org.apache.commons.beanutils.DynaProperty;
import org.apache.commons.math.MathException;
import org.apache.commons.math.util.NumberTransformer;

/**
 * This implementation of DescriptiveStatistics uses commons-beanutils to gather
 * univariate statistics for a List of Java Beans by property.  This 
 * implementation uses beanutils' PropertyUtils to get a simple, nested,
 * indexed, mapped, or combined property from an element of a List.
 * @version $Revision$ $Date$
 */
public class BeanListUnivariateImpl extends ListUnivariateImpl implements Serializable {

    /** Serializable version identifier */
    static final long serialVersionUID = -6428201899045406285L;
    
	/**
	 * propertyName of the property to get from the bean
	 */
	private String propertyName;

	/**
	 * No argument Constructor
	 */
	public BeanListUnivariateImpl(){
	    this(new ArrayList());
	}
	
	/**
	 * Construct a BeanListUnivariate with specified
	 * backing list
	 * @param list Backing List
	 */
	public BeanListUnivariateImpl(List list) {
		this(list, null);
	}

	/**
	 * Construct a BeanListUnivariate with specified
	 * backing list and propertyName
	 * @param list Backing List
	 * @param propertyName Bean propertyName
	 */
	public BeanListUnivariateImpl(List list, String propertyName) {
		super(list);
		setPropertyName(propertyName);
	}

	/**
	 * @return propertyName
	 */
	public String getPropertyName() {
		return propertyName;
	}

	/**
	 * @param propertyName Name of Property
	 */
	public void setPropertyName(String propertyName) {
		this.propertyName = propertyName;
		this.transformer = new NumberTransformer() {

			/**
			 * @see org.apache.commons.math.util.NumberTransformer#transform(java.lang.Object)
			 */
			public double transform(final Object o) throws MathException {
				try {
					return (
						(Number) PropertyUtils.getProperty(
							o,
							getPropertyName()))
						.doubleValue();
				} catch (IllegalAccessException e) {
					throw new MathException(
						"IllegalAccessException in Transformation: "
							+ e.getMessage(),
						e);
				} catch (InvocationTargetException e) {
					throw new MathException(
						"InvocationTargetException in Transformation: "
							+ e.getMessage(),
						e);
				} catch (NoSuchMethodException e) {
					throw new MathException(
						"oSuchMethodException in Transformation: "
							+ e.getMessage(),
						e);
				}
			}
		};
	}

	/**
	  *  Creates a {@link org.apache.commons.beanutils.DynaBean} with a 
	  *  {@link org.apache.commons.beanutils.DynaProperty} named 
	  *  <code>propertyName,</code> sets the value of the property to <code>v</code>
	  *  and adds the DynaBean to the underlying list.
	  *
	  */
	public void addValue(double v)  {
	    DynaProperty[] props = new DynaProperty[] {
	            new DynaProperty(propertyName, Double.class)
	    };
	    BasicDynaClass dynaClass = new BasicDynaClass(null, null, props);
	    DynaBean dynaBean = null;
	    try {
	        dynaBean = dynaClass.newInstance();
	    } catch (Exception ex) {              // InstantiationException, IllegalAccessException
	        throw new RuntimeException(ex);   // should never happen
	    }
		dynaBean.set(propertyName, new Double(v));
		addObject(dynaBean);
	}

	/**
	 * Adds a bean to this list. 
	 *
	 * @param bean Bean to add to the list
	 */
	public void addObject(Object bean) {
		list.add(bean);
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

QDox parse error in file://<WORKSPACE>/data/code-rep-dataset/Dataset3/Tasks/5172.java