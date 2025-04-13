error id: file://<WORKSPACE>/data/code-rep-dataset/Dataset5/Tasks/6389.java
file://<WORKSPACE>/data/code-rep-dataset/Dataset5/Tasks/6389.java
### com.thoughtworks.qdox.parser.ParseException: syntax error @[1,1]

error in qdox parser
file content:
```java
offset: 1
uri: file://<WORKSPACE>/data/code-rep-dataset/Dataset5/Tasks/6389.java
text:
```scala
S@@tringBuffer b = new StringBuffer("ContainerTypeDescription[");

/*******************************************************************************
 * Copyright (c) 2004 Composent, Inc. and others. All rights reserved. This
 * program and the accompanying materials are made available under the terms of
 * the Eclipse Public License v1.0 which accompanies this distribution, and is
 * available at http://www.eclipse.org/legal/epl-v10.html
 * 
 * Contributors: Composent, Inc. - initial API and implementation
 ******************************************************************************/
package org.eclipse.ecf.core;

import java.util.Arrays;
import java.util.HashMap;
import java.util.Map;
import java.util.Properties;
import org.eclipse.ecf.core.provider.IContainerInstantiator;

/**
 * Description of an IContainer implementation.
 * 
 */
public class ContainerTypeDescription {
	protected String name;
	protected String instantiatorClass;
	protected ClassLoader classLoader;
	protected IContainerInstantiator instantiator;
	protected String description;
	protected String[] argTypes;
	protected String[] argDefaults;
	protected String[] argNames;
	protected int hashCode = 0;
	protected static final String[] EMPTY = new String[0];
	protected Map properties;

	public ContainerTypeDescription(String name) {
		this(name,null);
	}
	public ContainerTypeDescription(String name, String description) {
		this(name,description,new HashMap());
	}
	public ContainerTypeDescription(String name, String description, Map properties) {
		this.name = name;
		this.description = description;
		this.properties = properties;
	}
	public ContainerTypeDescription(ClassLoader loader, String name,
			String instantiatorClass, String desc) {
		this(loader, name, instantiatorClass, desc, EMPTY, EMPTY, EMPTY);
	}

	public ContainerTypeDescription(String name, String instantiatorClass,
			String desc) {
		this(null, name, instantiatorClass, desc);
	}

	public ContainerTypeDescription(ClassLoader loader, String name,
			String instantiatorClass, String desc, String[] argTypes,
			String[] argDefaults, String[] argNames) {
		this(loader, name, instantiatorClass, desc, argTypes, argDefaults,
				argNames, new Properties());
	}

	public ContainerTypeDescription(ClassLoader loader, String name,
			String instantiatorClass, String desc, String[] argTypes,
			String[] argDefaults, String[] argNames, Map props) {
		this.classLoader = loader;
		if (name == null)
			throw new RuntimeException(
					new InstantiationException(
							"SharedObjectContainerDescription<init> name cannot be null"));
		this.name = name;
		if (instantiatorClass == null)
			throw new RuntimeException(
					new InstantiationException(
							"SharedObjectContainerDescription<init> instantiatorClass cannot be null"));
		this.instantiatorClass = instantiatorClass;
		this.hashCode = name.hashCode();
		this.description = desc;
		this.argTypes = argTypes;
		this.argDefaults = argDefaults;
		this.argNames = argNames;
		this.properties = props;
	}

	public ContainerTypeDescription(String name, IContainerInstantiator inst,
			String desc, String[] argTypes, String[] argDefaults,
			String[] argNames) {
		this(name, inst, desc, argTypes, argDefaults, argNames,
				new Properties());
	}

	public ContainerTypeDescription(String name, IContainerInstantiator inst,
			String desc, String[] argTypes, String[] argDefaults,
			String[] argNames, Map props) {
		if (name == null)
			throw new RuntimeException(
					new InstantiationException(
							"SharedObjectContainerDescription<init> name cannot be null"));
		if (inst == null)
			throw new RuntimeException(
					new InstantiationException(
							"SharedObjectContainerDescription<init> instantiator instance cannot be null"));
		this.instantiator = inst;
		this.name = name;
		this.classLoader = this.instantiator.getClass().getClassLoader();
		this.description = desc;
		this.argTypes = argTypes;
		this.argDefaults = argDefaults;
		this.argNames = argNames;
		this.properties = props;
	}

	public ContainerTypeDescription(String name, IContainerInstantiator inst,
			String desc) {
		this(name, inst, desc, EMPTY, EMPTY, EMPTY);
	}

	public String getName() {
		return name;
	}

	public ClassLoader getClassLoader() {
		return classLoader;
	}

	public boolean equals(Object other) {
		if (!(other instanceof ContainerTypeDescription))
			return false;
		ContainerTypeDescription scd = (ContainerTypeDescription) other;
		return scd.name.equals(name);
	}

	public int hashCode() {
		return hashCode;
	}

	public String toString() {
		StringBuffer b = new StringBuffer("SharedObjectContainerDescription[");
		b.append("name:").append(name).append(";");
		if (instantiator == null)
			b.append("class:").append(instantiatorClass).append(";");
		else
			b.append("instantiator:").append(instantiator).append(";");
		b.append("desc:").append(description).append(";");
		b.append("argtypes:").append(Arrays.asList(argTypes)).append(";");
		b.append("argdefaults:").append(Arrays.asList(argDefaults)).append(";");
		b.append("argnames:").append(Arrays.asList(argNames)).append("]");
		return b.toString();
	}

	protected IContainerInstantiator getInstantiator()
			throws ClassNotFoundException, InstantiationException,
			IllegalAccessException {
		synchronized (this) {
			if (instantiator == null)
				initializeInstantiator(classLoader);
			return instantiator;
		}
	}

	private void initializeInstantiator(ClassLoader cl)
			throws ClassNotFoundException, InstantiationException,
			IllegalAccessException {
		if (cl == null)
			cl = this.getClass().getClassLoader();
		// Load instantiator class
		Class clazz = Class.forName(instantiatorClass, true, cl);
		// Make new instance
		instantiator = (IContainerInstantiator) clazz.newInstance();
	}

	/**
	 * @return Returns the description.
	 */
	public String getDescription() {
		return description;
	}

	public String[] getArgDefaults() {
		return argDefaults;
	}

	public String[] getArgTypes() {
		return argTypes;
	}

	public String[] getArgNames() {
		return argNames;
	}

	public Map getProperties() {
		return properties;
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

QDox parse error in file://<WORKSPACE>/data/code-rep-dataset/Dataset5/Tasks/6389.java