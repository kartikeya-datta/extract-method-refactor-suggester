error id: file://<WORKSPACE>/data/code-rep-dataset/Dataset5/Tasks/2452.java
file://<WORKSPACE>/data/code-rep-dataset/Dataset5/Tasks/2452.java
### com.thoughtworks.qdox.parser.ParseException: syntax error @[1,1]

error in qdox parser
file content:
```java
offset: 1
uri: file://<WORKSPACE>/data/code-rep-dataset/Dataset5/Tasks/2452.java
text:
```scala
S@@tring serviceName = (String) (parameters[1] != null ? parameters[1] : anURL.getHost());

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

import ch.ethz.iks.slp.ServiceType;
import ch.ethz.iks.slp.ServiceURL;
import org.eclipse.ecf.core.identity.*;
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
		// error case
		if (parameters == null || parameters.length < 1 || parameters.length > 3) {
			throw new IDCreateException(Messages.JSLPNamespace_2);

			// error case
		} else if (parameters[0] == null || parameters[0].equals("")) { //$NON-NLS-1$
			throw new IDCreateException(Messages.JSLPNamespace_3);

			// create by jSLP ServiceURL
		} else if (parameters[0] instanceof ServiceURL) {
			ServiceURL anURL = (ServiceURL) parameters[0];
			IServiceTypeID stid = new JSLPServiceTypeID(this, anURL, (String[]) parameters[2]);
			String serviceName = (String) (parameters[2] != null ? parameters[1] : anURL.getHost());
			return new JSLPServiceID(this, stid, serviceName);

			// conversion call where conversion isn't necessary
		} else if (parameters[0] instanceof JSLPServiceID) {
			return (ID) parameters[0];

			// convert from IServiceID to IServiceTypeID, String
		} else if (parameters[0] instanceof IServiceID && parameters.length == 1) {
			IServiceID anId = (IServiceID) parameters[0];
			Object[] newParams = new Object[2];
			newParams[0] = anId.getServiceTypeID();
			newParams[1] = anId.getName();
			return createInstance(newParams);

			// create by ECF discovery generic IServiceTypeID (but not JSLPServiceID!!!)
		} else if (parameters[0] instanceof IServiceTypeID) {
			IServiceTypeID stid = (IServiceTypeID) parameters[0];
			parameters[0] = stid.getName();
			return createInstance(parameters);

			// create by jSLP ServiceType
		} else if (parameters[0] instanceof ServiceType) {
			IServiceTypeID stid = new JSLPServiceTypeID(this, (ServiceType) parameters[0]);
			return new JSLPServiceID(this, stid, (String) parameters[1]);

			// create by jSLP ServiceType String representation (from external)
		} else if (parameters[0] instanceof String && ((String) parameters[0]).startsWith("service:")) { //$NON-NLS-1$
			parameters[0] = new ServiceType((String) parameters[0]);
			return createInstance(parameters);

			// create by ECF discovery generic String representation
		} else if (parameters[0] instanceof String && ((String) parameters[0]).startsWith("_")) { //$NON-NLS-1$
			String type = (String) parameters[0];
			String name = (String) parameters[1];
			IServiceTypeID stid = new JSLPServiceTypeID(this, new ServiceTypeID(this, type));
			return new JSLPServiceID(this, stid, name);

			// create by "jslp:..."
		} else if (parameters[0] instanceof String && ((String) parameters[0]).startsWith(getScheme() + Namespace.SCHEME_SEPARATOR)) {
			String str = (String) parameters[0];
			int index = str.indexOf(Namespace.SCHEME_SEPARATOR);
			parameters[0] = str.substring(index + 1);
			return createInstance(parameters);

			// error case second parameter not a String
		} else if (parameters.length == 2 && parameters[1] != null && !(parameters[1] instanceof String)) {
			throw new IDCreateException(Messages.JSLPNamespace_4);

			// error case
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
		return new Class[][] { {String.class}, {String.class, String.class}, {ServiceURL.class, String[].class, String.class}, {IServiceTypeID.class}, {IServiceID.class}, {ServiceType.class, String.class}};
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

QDox parse error in file://<WORKSPACE>/data/code-rep-dataset/Dataset5/Tasks/2452.java