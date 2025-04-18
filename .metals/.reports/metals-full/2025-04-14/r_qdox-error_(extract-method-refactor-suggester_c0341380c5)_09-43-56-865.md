error id: file://<WORKSPACE>/data/code-rep-dataset/Dataset5/Tasks/6218.java
file://<WORKSPACE>/data/code-rep-dataset/Dataset5/Tasks/6218.java
### com.thoughtworks.qdox.parser.ParseException: syntax error @[1,1]

error in qdox parser
file content:
```java
offset: 1
uri: file://<WORKSPACE>/data/code-rep-dataset/Dataset5/Tasks/6218.java
text:
```scala
a@@ssertTrue(guid2.equals(guid2a) && guid3.equals(guid3a));

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

package org.eclipse.ecf.tests.securestorage;

import junit.framework.TestCase;

import org.eclipse.ecf.core.identity.ID;
import org.eclipse.ecf.core.identity.IDCreateException;
import org.eclipse.ecf.core.identity.IDFactory;
import org.eclipse.ecf.internal.tests.securestorage.Activator;
import org.eclipse.ecf.storage.IIDEntry;
import org.eclipse.ecf.storage.IIDStore;
import org.eclipse.ecf.storage.INamespaceEntry;
import org.eclipse.equinox.security.storage.ISecurePreferences;

/**
 *
 */
public class IDStoreTest extends TestCase {

	IIDStore idStore;

	/* (non-Javadoc)
	 * @see junit.framework.TestCase#setUp()
	 */
	protected void setUp() throws Exception {
		super.setUp();
		idStore = Activator.getDefault().getIDStore();
	}

	protected void clearStore() {
		final INamespaceEntry[] namespaces = idStore.getNamespaceEntries();
		for (int i = 0; i < namespaces.length; i++) {
			namespaces[i].delete();
		}
	}

	/* (non-Javadoc)
	 * @see junit.framework.TestCase#tearDown()
	 */
	protected void tearDown() throws Exception {
		super.tearDown();
		clearStore();
		idStore = null;
	}

	public void testIDStore() {
		assertNotNull(idStore);
	}

	protected IIDEntry addGUID() throws IDCreateException {
		final ID newGUID = IDFactory.getDefault().createGUID();
		return idStore.store(newGUID);
	}

	protected IIDEntry addStringID(String value) throws IDCreateException {
		final ID newID = IDFactory.getDefault().createStringID(value);
		return idStore.store(newID);
	}

	protected IIDEntry addLongID(long value) throws IDCreateException {
		final ID newID = IDFactory.getDefault().createLongID(value);
		return idStore.store(newID);
	}

	public void testStoreGUID() throws Exception {
		final ISecurePreferences prefs = addGUID().getPreferences();
		assertNotNull(prefs);
	}

	public void testStoreGUIDs() throws Exception {
		testStoreGUID();
		testStoreGUID();
		testStoreGUID();
	}

	public void testStoreLongIDs() throws Exception {
		addLongID(1);
		addLongID(2);
		addLongID(3);
	}

	public void testListEmptyNamespaces() throws Exception {
		final INamespaceEntry[] namespaces = idStore.getNamespaceEntries();
		assertNotNull(namespaces);
	}

	public void testOneNamespace() throws Exception {
		testStoreGUID();
		testStoreGUID();
		final INamespaceEntry[] namespaces = idStore.getNamespaceEntries();
		assertTrue(namespaces.length == 1);
	}

	public void testTwoNamespace() throws Exception {
		testStoreGUID();
		addStringID("1");
		final INamespaceEntry[] namespaces = idStore.getNamespaceEntries();
		assertTrue(namespaces.length == 2);
	}

	public void testGetNamespaceNode() throws Exception {
		final ID newGUID = IDFactory.getDefault().createGUID();
		idStore.store(newGUID);
		final ISecurePreferences namespacePrefs = idStore.getNamespaceEntry(newGUID.getNamespace()).getPreferences();
		assertNotNull(namespacePrefs);
		assertTrue(namespacePrefs.name().equals(newGUID.getNamespace().getName()));
	}

	public void testGetIDEntries() throws Exception {
		final ID newGUID = IDFactory.getDefault().createGUID();
		idStore.store(newGUID);
		// Get namespace entry
		final INamespaceEntry namespaceEntry = idStore.getNamespaceEntry(newGUID.getNamespace());
		assertNotNull(namespaceEntry);

		final IIDEntry[] idEntries = namespaceEntry.getIDEntries();
		assertTrue(idEntries.length == 1);
		// Create GUID from idEntry
		final ID persistedGUID = idEntries[0].createID();
		assertNotNull(persistedGUID);
		assertTrue(persistedGUID.equals(newGUID));
	}

	public void testGetLongIDEntries() throws Exception {
		final ID newLongID = IDFactory.getDefault().createLongID(1);
		idStore.store(newLongID);
		// Get namespace entry
		final INamespaceEntry namespaceEntry = idStore.getNamespaceEntry(newLongID.getNamespace());
		assertNotNull(namespaceEntry);

		final IIDEntry[] idEntries = namespaceEntry.getIDEntries();
		assertTrue(idEntries.length == 1);
		// Create LongID from idEntry
		final ID persistedLongID = idEntries[0].createID();
		assertNotNull(persistedLongID);
		assertTrue(persistedLongID.equals(newLongID));
	}

	public void testCreateAssociation() throws Exception {
		// Create two GUIDs and store them in idStore
		final ID guid1 = IDFactory.getDefault().createGUID();
		final IIDEntry entry1 = idStore.store(guid1);
		final ID guid2 = IDFactory.getDefault().createGUID();
		final IIDEntry entry2 = idStore.store(guid2);

		final String key = "foo";
		// Create association
		entry1.putAssociate(key, entry2, true);

		// Get entry1a
		final IIDEntry entry1a = idStore.store(guid1);
		assertNotNull(entry1a);
		// Get associates (should include entry2)
		final IIDEntry[] entries = entry1a.getAssociates(key);
		assertNotNull(entries);
		assertTrue(entries.length == 1);
		// entry2a should be same as entry2
		final IIDEntry entry2a = entries[0];
		assertNotNull(entry2a);
		final ID guid2a = entry2a.createID();
		// and guid2a should equal guid2
		assertTrue(guid2.equals(guid2a));

	}

	public void testCreateAssociations() throws Exception {
		// Create two GUIDs and store them in idStore
		final ID guid1 = IDFactory.getDefault().createGUID();
		final IIDEntry entry1 = idStore.store(guid1);
		final ID guid2 = IDFactory.getDefault().createGUID();
		final IIDEntry entry2 = idStore.store(guid2);
		final ID guid3 = IDFactory.getDefault().createGUID();
		final IIDEntry entry3 = idStore.store(guid3);
		final ID guid4 = IDFactory.getDefault().createGUID();
		final IIDEntry entry4 = idStore.store(guid4);

		final String key1 = "foo";
		final String key2 = "foo2";

		// Create association
		entry1.putAssociate(key1, entry2, true);
		// Create another association with same key (key1)
		entry1.putAssociate(key1, entry3, false);
		// Create another association with different key (key2)
		entry1.putAssociate(key2, entry4, true);

		// Get entry1a
		final IIDEntry entry1a = idStore.store(guid1);
		assertNotNull(entry1a);
		final ID guid1a = entry1a.createID();
		assertTrue(guid1.equals(guid1a));

		// Get associates (should include entry2)
		final IIDEntry[] entries1 = entry1a.getAssociates(key1);
		assertNotNull(entries1);
		assertTrue(entries1.length == 2);
		// entry2a should be same as entry2
		final IIDEntry entry2a = entries1[0];
		assertNotNull(entry2a);
		final ID guid2a = entry2a.createID();

		// entry3a should be same as entry3
		final IIDEntry entry3a = entries1[1];
		assertNotNull(entry3a);
		final ID guid3a = entry3a.createID();

		// Since the order can be turned around, 2a can equal 2 or 3
		assertTrue((guid2.equals(guid2a) && guid3.equals(guid3a)) || (guid2.equals(guid3a) && guid3.equals(guid2a)));

		final IIDEntry[] entries2 = entry1a.getAssociates(key2);
		assertNotNull(entries2);
		assertTrue(entries2.length == 1);
		// entry4a should be same as entry4
		final IIDEntry entry4a = entries2[0];
		assertNotNull(entry4a);
		final ID guid4a = entry4a.createID();
		// and guid4a should equal guid4
		assertTrue(guid4.equals(guid4a));

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

QDox parse error in file://<WORKSPACE>/data/code-rep-dataset/Dataset5/Tasks/6218.java