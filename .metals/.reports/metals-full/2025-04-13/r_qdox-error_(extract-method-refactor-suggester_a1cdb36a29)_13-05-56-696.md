error id: file://<WORKSPACE>/data/code-rep-dataset/Dataset5/Tasks/10780.java
file://<WORKSPACE>/data/code-rep-dataset/Dataset5/Tasks/10780.java
### com.thoughtworks.qdox.parser.ParseException: syntax error @[1,1]

error in qdox parser
file content:
```java
offset: 1
uri: file://<WORKSPACE>/data/code-rep-dataset/Dataset5/Tasks/10780.java
text:
```scala
s@@uper(NAME, "Rpc Namespace"); //$NON-NLS-1$

/******************************************************************************* 
 * Copyright (c) 2010-2011 Naumen. All rights reserved. This
 * program and the accompanying materials are made available under the terms of
 * the Eclipse Public License v1.0 which accompanies this distribution, and is
 * available at http://www.eclipse.org/legal/epl-v10.html
 *
 * Contributors:
 *   Pavel Samolisov - initial API and implementation
 *******************************************************************************/
package org.eclipse.ecf.remoteservice.rpc.identity;

import java.net.URI;
import java.net.URL;
import java.util.*;
import org.eclipse.ecf.core.identity.*;
import org.eclipse.ecf.remoteservice.rpc.client.RpcClientContainer;

/**
 * This class represents a {@link Namespace} for {@link RpcClientContainer}s.
 */
public class RpcNamespace extends Namespace {

	private static final long serialVersionUID = -4255624538742281975L;

	/**
	 * The name of this namespace.
	 */
	public static final String NAME = "ecf.xmlrpc.namespace"; //$NON-NLS-1$

	/**
	 * The scheme of this namespace.
	 */
	public static final String SCHEME = "xmlrpc"; //$NON-NLS-1$

	public RpcNamespace() {
		// nothing
	}

	public RpcNamespace(String name, String desc) {
		super(name, desc);
	}

	private String getInitFromExternalForm(Object[] args) {
		if (args == null || args.length < 1 || args[0] == null)
			return null;
		if (args[0] instanceof String) {
			final String arg = (String) args[0];
			if (arg.startsWith(getScheme() + Namespace.SCHEME_SEPARATOR)) {
				final int index = arg.indexOf(Namespace.SCHEME_SEPARATOR);
				if (index >= arg.length())
					return null;
				return arg.substring(index + 1);
			}
		}
		return null;
	}

	/**
	 * Creates an instance of an {@link RPCD}. The parameters must contain specific information.
	 * 
	 * @param parameters a collection of attributes to call the right constructor on {@link RpcId}.
	 * @return an instance of {@link RpcId}. Will not be <code>null</code>.
	 */
	public ID createInstance(Object[] parameters) throws IDCreateException {
		URI uri = null;
		try {
			final String init = getInitFromExternalForm(parameters);
			if (init != null) {
				uri = URI.create(init);
				return new RpcId(this, uri);
			}
			if (parameters != null) {
				if (parameters[0] instanceof URI)
					return new RpcId(this, (URI) parameters[0]);
				else if (parameters[0] instanceof String)
					return new RpcId(this, URI.create((String) parameters[0]));
				else if (parameters[0] instanceof URL)
					return new RpcId(this, URI.create(((URL) parameters[0]).toExternalForm()));
				else if (parameters[0] instanceof RpcId)
					return (ID) parameters[0];
			}
			throw new IllegalArgumentException("Invalid parameters to RPCID creation"); //$NON-NLS-1$
		} catch (Exception e) {
			throw new IDCreateException("Could not create RPC ID", e); //$NON-NLS-1$
		}
	}

	public Class[][] getSupportedParameterTypes() {
		return new Class[][] { {String.class}, {Integer.class}, {Boolean.class}, {Double.class}, {Date.class},
				{byte[].class}, {Object[].class}, {List.class}, {Map.class}};
	}

	public String getScheme() {
		return SCHEME;
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

QDox parse error in file://<WORKSPACE>/data/code-rep-dataset/Dataset5/Tasks/10780.java