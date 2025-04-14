error id: file://<WORKSPACE>/data/code-rep-dataset/Dataset5/Tasks/3841.java
file://<WORKSPACE>/data/code-rep-dataset/Dataset5/Tasks/3841.java
### com.thoughtworks.qdox.parser.ParseException: syntax error @[1,8]

error in qdox parser
file content:
```java
offset: 8
uri: file://<WORKSPACE>/data/code-rep-dataset/Dataset5/Tasks/3841.java
text:
```scala
public I@@IDEntry store(ID id) {

/****************************************************************************
 * Copyright (c) 2008 Composent, Inc. and others.
 * All rights reserved. This program and the accompanying materials
 * are made available under the terms of the Eclipse Public License v1.0
 * which accompanies this distribution, and is available at
 * http://www.eclipse.org/legal/epl-v10.html
 *
 * Contributors:
 *    Composent, Inc. - initial API and implementation
 *****************************************************************************/

package org.eclipse.ecf.internal.storage;

import java.util.ArrayList;
import java.util.List;
import org.eclipse.core.runtime.IAdapterManager;
import org.eclipse.ecf.core.identity.ID;
import org.eclipse.ecf.core.identity.Namespace;
import org.eclipse.ecf.storage.*;
import org.eclipse.equinox.security.storage.*;

/**
 *
 */
public class IDStore implements IIDStore {

	private static final String idStoreNameSegment = "/ECF/Namespace"; //$NON-NLS-1$
	private static final INamespaceEntry[] EMPTY_ARRAY = {};

	private String getIDAsString(ID id) {
		final IIDStoreAdapter idadapter = (IIDStoreAdapter) id.getAdapter(IIDStoreAdapter.class);
		final String idName = (idadapter != null) ? idadapter.getNameForStorage() : id.toExternalForm();
		if (idName == null || idName.equals("")) //$NON-NLS-1$
			return null;
		return EncodingUtils.encodeSlashes(idName);
	}

	protected ISecurePreferences getRoot() {
		return SecurePreferencesFactory.getDefault();
	}

	protected ISecurePreferences getNamespaceRoot() {
		ISecurePreferences root = getRoot();
		if (root == null)
			return null;
		return root.node(idStoreNameSegment);
	}

	/* (non-Javadoc)
	 * @see org.eclipse.ecf.storage.IIDStore#getEntry(org.eclipse.ecf.core.identity.ID)
	 */
	public IIDEntry getEntry(ID id) {
		ISecurePreferences namespaceRoot = getNamespaceRoot();
		if (namespaceRoot == null)
			return null;
		INamespaceEntry namespaceEntry = getNamespaceEntry(id.getNamespace());
		final String idAsString = getIDAsString(id);
		if (idAsString == null)
			return null;
		return new IDEntry(namespaceEntry.getPreferences().node(idAsString));
	}

	/* (non-Javadoc)
	 * @see org.eclipse.ecf.storage.IIDStore#getNamespaceEntries()
	 */
	public INamespaceEntry[] getNamespaceEntries() {
		ISecurePreferences namespaceRoot = getNamespaceRoot();
		if (namespaceRoot == null)
			return EMPTY_ARRAY;
		List results = new ArrayList();
		String names[] = namespaceRoot.childrenNames();
		for (int i = 0; i < names.length; i++)
			results.add(new NamespaceEntry(namespaceRoot.node(names[i])));
		return (INamespaceEntry[]) results.toArray(new INamespaceEntry[] {});
	}

	/* (non-Javadoc)
	 * @see org.eclipse.ecf.storage.IIDStore#getNamespaceEntry(org.eclipse.ecf.core.identity.Namespace)
	 */
	public INamespaceEntry getNamespaceEntry(Namespace namespace) {
		if (namespace == null)
			return null;
		final INamespaceStoreAdapter nsadapter = (INamespaceStoreAdapter) namespace.getAdapter(INamespaceStoreAdapter.class);
		final String nsName = (nsadapter != null) ? nsadapter.getNameForStorage() : namespace.getName();
		if (nsName == null)
			return null;
		ISecurePreferences namespaceRoot = getNamespaceRoot();
		if (namespaceRoot == null)
			return null;
		return new NamespaceEntry(namespaceRoot.node(nsName));
	}

	/* (non-Javadoc)
	 * @see org.eclipse.core.runtime.IAdaptable#getAdapter(java.lang.Class)
	 */
	public Object getAdapter(Class adapter) {
		if (adapter == null)
			return null;
		if (adapter.isInstance(this)) {
			return this;
		}
		IAdapterManager adapterManager = Activator.getDefault().getAdapterManager();
		return (adapterManager == null) ? null : adapterManager.loadAdapter(this, adapter.getName());
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

QDox parse error in file://<WORKSPACE>/data/code-rep-dataset/Dataset5/Tasks/3841.java