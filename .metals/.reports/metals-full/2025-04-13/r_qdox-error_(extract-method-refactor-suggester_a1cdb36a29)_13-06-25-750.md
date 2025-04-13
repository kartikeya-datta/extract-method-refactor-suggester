error id: file://<WORKSPACE>/data/code-rep-dataset/Dataset5/Tasks/16583.java
file://<WORKSPACE>/data/code-rep-dataset/Dataset5/Tasks/16583.java
### com.thoughtworks.qdox.parser.ParseException: syntax error @[1,17]

error in qdox parser
file content:
```java
offset: 17
uri: file://<WORKSPACE>/data/code-rep-dataset/Dataset5/Tasks/16583.java
text:
```scala
public abstract I@@D createInstance(Class[] argTypes, Object[] args)

/*******************************************************************************
 * Copyright (c) 2004 Composent, Inc. and others. All rights reserved. This
 * program and the accompanying materials are made available under the terms of
 * the Eclipse Public License v1.0 which accompanies this distribution, and is
 * available at http://www.eclipse.org/legal/epl-v10.html
 * 
 * Contributors: Composent, Inc. - initial API and implementation
 ******************************************************************************/
package org.eclipse.ecf.core.identity;

import java.io.Serializable;
import java.net.URI;
import java.net.URISyntaxException;

/**
 * Namespace base class
 * 
 * All implementers of the 'namespace' extension point must
 * implement a subclass of this Namespace class
 * 
 * This class defines the properties associated with an
 * identity Namespace. Namespaces are defined by a unique 'name' (e.g. 'email',
 * 'icq', 'aolim').
 * 
 */
public abstract class Namespace implements Serializable {
	private static final long serialVersionUID = 3976740272094720312L;
	private String name;
	private String description;
	private int hashCode;
	private boolean isInitialized = false;

	public Namespace() {
	}

	public final boolean initialize(String name, String desc) {
		if (name == null)
			throw new RuntimeException(new InstantiationException(
					"Namespace<init> name cannot be null"));
		if (!isInitialized) {
			this.name = name;
			this.description = desc;
			this.hashCode = name.hashCode();
			return true;
		} else
			return false;
	}

	public Namespace(String name, String desc) {
		initialize(name, desc);
	}

	/**
	 * Override of Object.equals. This equals method returns true if the
	 * provided Object is also a Namespace instance, and the names of the two
	 * instances match.
	 * 
	 * @param other
	 *            the Object to test for equality
	 */
	public boolean equals(Object other) {
		if (!(other instanceof Namespace))
			return false;
		return ((Namespace) other).name.equals(name);
	}

	public int hashCode() {
		return hashCode;
	}

	protected boolean testIDEquals(BaseID first, BaseID second) {
		// First check that namespaces are the same and non-null
		Namespace sn = second.getNamespace();
		if (sn == null || !this.equals(sn))
			return false;
		return first.namespaceEquals(second);
	}

	protected String getNameForID(BaseID id) {
		return id.namespaceGetName();
	}

	protected URI getURIForID(BaseID id) throws URISyntaxException {
		return id.namespaceToURI();
	}

	protected int getCompareToForObject(BaseID first, BaseID second) {
		return first.namespaceCompareTo(second);
	}

	protected int getHashCodeForID(BaseID id) {
		return id.namespaceHashCode();
	}

	/**
	 * Get the name of this namespace.  May not return null.
	 * 
	 * @return String name of Namespace instance
	 * 
	 */
	public String getName() {
		return name;
	}
	/**
	 * Get the description, associated with this Namespace.  The returned value may be null.
	 * @return the description associated with this Namespace
	 */
	public String getDescription() {
		return description;
	}
	/**
	 * Make an instance of this namespace.  Namespace subclasses, provided by plugins
	 * must implement this method to construct ID instances for the given namespace.
	 * 
	 * @param argTypes a Class[] of classes defining the types of the args.  May be null if
	 * no arguments given.  The arity and classes provided must correspond to the types of 
	 * the associated arguments
	 * @param args an Object[] of arguments for creating ID instances.  May be null if no
	 * arguments provided
	 * @return a non-null ID instance.  The class used may extend BaseID or may implement
	 * the ID interface directly
	 * @throws IDInstantiationException if construction fails
	 */
	public abstract ID makeInstance(Class[] argTypes, Object[] args)
			throws IDInstantiationException;

	/**
	 * Get the URI scheme associated with this namespace instance.  For example,
	 * a namespace with name "ecf.http" would have an associated scheme identifier of "http".
	 * Subclasses must provide an implementation that returns a valid non-null scheme 
	 * identifier.
	 * 
	 * @return a String scheme identifier
	 */
	public abstract String getScheme();
	
	public String toString() {
		StringBuffer b = new StringBuffer("Namespace[");
		b.append("name=").append(name).append(";");
		b.append("scheme=").append(getScheme()).append(";");
		b.append("description=").append("]");
		return b.toString();
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

QDox parse error in file://<WORKSPACE>/data/code-rep-dataset/Dataset5/Tasks/16583.java