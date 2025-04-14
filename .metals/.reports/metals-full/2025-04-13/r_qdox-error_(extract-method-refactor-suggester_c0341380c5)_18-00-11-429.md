error id: file://<WORKSPACE>/data/code-rep-dataset/Dataset5/Tasks/325.java
file://<WORKSPACE>/data/code-rep-dataset/Dataset5/Tasks/325.java
### com.thoughtworks.qdox.parser.ParseException: syntax error @[1,1]

error in qdox parser
file content:
```java
offset: 1
uri: file://<WORKSPACE>/data/code-rep-dataset/Dataset5/Tasks/325.java
text:
```scala
J@@SLPServiceTypeID stid = new JSLPServiceTypeID(this, anURL, (String[]) parameters[1]);

/*******************************************************************************
 * Copyright (c) 2007 Versant Corp.
 * All rights reserved. This program and the accompanying materials
 * are made available under the terms of the Eclipse Public License v1.0
 * which accompanies this distribution, and is available at
 * http://www.eclipse.org/legal/epl-v10.html
 *
 * Contributors:
 *     Markus Kuppe
 ******************************************************************************/
package org.eclipse.ecf.provider.jslp.identity;

import ch.ethz.iks.slp.ServiceURL;
import org.eclipse.ecf.core.identity.*;
import org.eclipse.ecf.core.util.StringUtils;
import org.eclipse.ecf.discovery.identity.*;
import org.eclipse.ecf.internal.provider.jslp.Messages;

public class JSLPNamespace extends Namespace {
	private static final String JSLP_SCHEME = "jslp"; //$NON-NLS-1$

	private static final long serialVersionUID = -3041453162456476102L;

	public static final String NAME = "ecf.namespace.slp"; //$NON-NLS-1$

	/* (non-Javadoc)
	 * @see org.eclipse.ecf.core.identity.Namespace#createInstance(java.lang.Object[])
	 */
	public ID createInstance(Object[] parameters) throws IDCreateException {
		if (parameters == null || parameters.length < 1 || parameters.length > 2) {
			throw new IDCreateException(Messages.JSLPNamespace_2);
		} else if (parameters[0] == null || parameters[0].equals("")) { //$NON-NLS-1$
			throw new IDCreateException(Messages.JSLPNamespace_3);
		} else if (parameters[0] instanceof ServiceURL) { // handles internal creation
			ServiceURL anURL = (ServiceURL) parameters[0];
			JSLPServiceTypeID stid = new JSLPServiceTypeID(this, anURL);
			return new JSLPServiceID(this, stid, anURL.getHost());
		} else if (parameters[0] instanceof JSLPServiceID) { // handles conversion call where conversion isn't necessary
			return (ID) parameters[0];
		} else if (parameters[0] instanceof IServiceID) {
			IServiceID anId = (IServiceID) parameters[0];
			return createInstance(new Object[] {anId.getServiceTypeID(), parameters[1]});
		} else if (parameters[0] instanceof IServiceTypeID) {
			IServiceTypeID stid = (IServiceTypeID) parameters[0];
			return createInstance(new Object[] {stid.getName(), parameters[1]});
		} else if (parameters[0] instanceof String) { // creates from either external or internal string
			String type = (String) parameters[0];
			IServiceTypeID stid = null;
			if (StringUtils.contains(type, "._")) { //$NON-NLS-1$ // converts external to internal
				ServiceTypeID aStid = new ServiceTypeID(this, type);
				stid = new JSLPServiceTypeID(this, aStid);
			} else {
				stid = new JSLPServiceTypeID(this, type);
			}
			String name = null;
			if (parameters.length > 1) {
				try {
					name = (String) parameters[1];
				} catch (final ClassCastException e) {
					throw new IDCreateException(Messages.JSLPNamespace_4);
				}
			}
			return new JSLPServiceID(this, stid, name);
		} else {
			throw new IDCreateException(Messages.JSLPNamespace_3);
		}
	}

	/* (non-Javadoc)
	 * @see org.eclipse.ecf.core.identity.Namespace#getScheme()
	 */
	public String getScheme() {
		return JSLP_SCHEME;
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see org.eclipse.ecf.core.identity.Namespace#getSupportedParameterTypesForCreateInstance()
	 */
	public Class[][] getSupportedParameterTypes() {
		return new Class[][] { {String.class}, {String.class, String.class}, {ServiceURL.class}, {IServiceTypeID.class}, {IServiceID.class}};
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

QDox parse error in file://<WORKSPACE>/data/code-rep-dataset/Dataset5/Tasks/325.java