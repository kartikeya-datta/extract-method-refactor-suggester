error id: file://<WORKSPACE>/data/code-rep-dataset/Dataset2/Tasks/2056.java
file://<WORKSPACE>/data/code-rep-dataset/Dataset2/Tasks/2056.java
### com.thoughtworks.qdox.parser.ParseException: syntax error @[1,1]

error in qdox parser
file content:
```java
offset: 1
uri: file://<WORKSPACE>/data/code-rep-dataset/Dataset2/Tasks/2056.java
text:
```scala
O@@bject uid = generateNextMessageUid();

// The contents of this file are subject to the Mozilla Public License Version
// 1.1
//(the "License"); you may not use this file except in compliance with the
//License. You may obtain a copy of the License at http://www.mozilla.org/MPL/
//
//Software distributed under the License is distributed on an "AS IS" basis,
//WITHOUT WARRANTY OF ANY KIND, either express or implied. See the License
//for the specific language governing rights and
//limitations under the License.
//
//The Original Code is "The Columba Project"
//
//The Initial Developers of the Original Code are Frederik Dietz and Timo
// Stich.
//Portions created by Frederik Dietz and Timo Stich are Copyright (C) 2003.
//
//All Rights Reserved.
package org.columba.addressbook.folder;

import java.io.File;
import java.util.Iterator;
import java.util.logging.Logger;

import javax.swing.event.EventListenerList;

import org.columba.addressbook.config.FolderItem;
import org.columba.addressbook.gui.tree.AddressbookTreeNode;
import org.columba.addressbook.model.Contact;
import org.columba.addressbook.model.ContactItem;
import org.columba.addressbook.model.ContactItemMap;
import org.columba.addressbook.model.GroupItem;
import org.columba.addressbook.model.HeaderItemList;
import org.columba.core.command.WorkerStatusController;
import org.columba.core.io.DiskIO;
import org.columba.core.main.MainInterface;

/**
 * Abstract base class for every contact folder.
 * 
 * @author fdietz
 *
 */
public abstract class AbstractFolder extends AddressbookTreeNode implements
		ContactStorage {

	/** JDK 1.4+ logging framework logger, used for logging. */
	private static final Logger LOG = Logger
			.getLogger("org.columba.addressbook.folder");

	protected EventListenerList listenerList = new EventListenerList();

	protected int nextMessageUid;
	
	/**
	 * folder where we save everything name of folder is usually the UID-number
	 */
	private File directoryFile;

	/**
	 * FolderItem keeps information about the folder for example: name,
	 * accessrights, type
	 */

	private ContactItemCacheStorage cacheStorage;

	public AbstractFolder(String name, String dir) {
		super(name);

		if (DiskIO.ensureDirectory(dir)) {
			directoryFile = new File(dir);
		}

		cacheStorage = new ContactItemCacheStorageImpl(this);
	}

	public AbstractFolder(FolderItem item) {
		super(item);

		String dir = MainInterface.config.getConfigDirectory()
				+ "/addressbook/" + getUid();

		if (DiskIO.ensureDirectory(dir)) {
			directoryFile = new File(dir);
		}

		cacheStorage = new ContactItemCacheStorageImpl(this);
	}

	/**
	 * Adds a listener.
	 */
	public void addFolderListener(FolderListener l) {
		listenerList.add(FolderListener.class, l);
	}

	/**
	 * Removes a previously registered listener.
	 */
	public void removeFolderListener(FolderListener l) {
		listenerList.remove(FolderListener.class, l);
	}

	/**
	 * Propagates an event to all registered listeners notifying them of a item
	 * addition.
	 */
	protected void fireItemAdded(Object uid) {

		FolderEvent e = new FolderEvent(this, null);
		// Guaranteed to return a non-null array
		Object[] listeners = listenerList.getListenerList();

		// Process the listeners last to first, notifying
		// those that are interested in this event
		for (int i = listeners.length - 2; i >= 0; i -= 2) {
			if (listeners[i] == FolderListener.class) {
				((FolderListener) listeners[i + 1]).itemAdded(e);
			}
		}
	}

	/**
	 * Propagates an event to all registered listeners notifying them of a item
	 * removal.
	 */
	protected void fireItemRemoved(Object uid) {

		FolderEvent e = new FolderEvent(this, null);
		// Guaranteed to return a non-null array
		Object[] listeners = listenerList.getListenerList();

		// Process the listeners last to first, notifying
		// those that are interested in this event
		for (int i = listeners.length - 2; i >= 0; i -= 2) {
			if (listeners[i] == FolderListener.class) {
				((FolderListener) listeners[i + 1]).itemRemoved(e);
			}
		}
	}

	/**
	 * Propagates an event to all registered listeners notifying them of a item
	 * change.
	 */
	protected void fireItemChanged(Object uid) {

		FolderEvent e = new FolderEvent(this, null);
		// Guaranteed to return a non-null array
		Object[] listeners = listenerList.getListenerList();

		// Process the listeners last to first, notifying
		// those that are interested in this event
		for (int i = listeners.length - 2; i >= 0; i -= 2) {
			if (listeners[i] == FolderListener.class) {
				((FolderListener) listeners[i + 1]).itemChanged(e);
			}
		}
	}

	public File getDirectoryFile() {
		return directoryFile;
	}

	public void createChildren(WorkerStatusController worker) {
	}

	/**
	 * @see org.columba.addressbook.folder.ContactStorage#getHeaderItemList()
	 */
	public ContactItemMap getContactItemMap() throws Exception {
		return cacheStorage.getContactItemMap();
	}

	/**
	 * Check if contact exists with email or displayname.
	 * 
	 * @param contact		given contact email or displayname
	 * @return				contact UID, if exists. Otherwise, null
	 * @throws Exception
	 */
	public Object exists(String contact) throws Exception{
		Iterator it = getContactItemMap().iterator();
		while ( it.hasNext()) {
			ContactItem item = (ContactItem) it.next();
			String address = item.getAddress();
			String displayname = item.getDisplayName();
			
			if ( address.equalsIgnoreCase(contact)) return item.getUid();
			if ( displayname.equalsIgnoreCase(contact)) return item.getUid();
			
		}
		return null;
	}
	

	/**
	 * save header-cache (HeaderItemList)
	 */
	public void save() throws Exception {

	}

	/**
	 * load header-cache (HeaderItemList)
	 */
	public void load() throws Exception {

	}

	/**
	 * @see javax.swing.tree.DefaultMutableTreeNode#getPathToRoot(TreeNode, int)
	 */
	/*
	 * protected TreeNode[] getPathToRoot(TreeNode aNode, int depth) {
	 * TreeNode[] retNodes;
	 * 
	 * if (aNode == null) { if (depth == 0) { return null; } else { retNodes =
	 * new TreeNode[depth]; } } else { depth++; retNodes =
	 * getPathToRoot(aNode.getParent(), depth); retNodes[retNodes.length -
	 * depth] = aNode; }
	 * 
	 * return retNodes; }
	 */

	/**
	 * Method getTreePath.
	 * 
	 * @return String
	 */
	/*
	 * public String getTreePath() { TreeNode[] treeNode = getPathToRoot(this,
	 * 0);
	 * 
	 * StringBuffer path = new StringBuffer();
	 * 
	 * for (int i = 1; i < treeNode.length; i++) { AddressbookTreeNode folder =
	 * (AddressbookTreeNode) treeNode[i]; path.append("/" + folder.getName()); }
	 * 
	 * return path.toString(); }
	 */

	/**
	 * @see org.columba.addressbook.folder.ContactStorage#add(org.columba.addressbook.folder.Contact)
	 */
	public Object add(Contact contact) throws Exception {
		Object uid = generateNextFolderUid();

		ContactItem item = new ContactItem(contact);
		item.setUid(uid);

		cacheStorage.add(uid, item);

		fireItemAdded(uid);

		return uid;
	}

	/**
	 * 
	 * @see org.columba.addressbook.folder.ContactStorage#modify(java.lang.Object,
	 *      org.columba.addressbook.folder.Contact)
	 */
	public void modify(Object uid, Contact contact) throws Exception {

		ContactItem item = new ContactItem(contact);

		item.setUid(uid);

		cacheStorage.modify(uid, item);

		fireItemChanged(uid);
	}

	/**
	 * @see org.columba.addressbook.folder.ContactStorage#remove(java.lang.Object)
	 */
	public void remove(Object uid) throws Exception {
		cacheStorage.remove(uid);

		fireItemRemoved(uid);
	}

	/**
	 * @see org.columba.addressbook.folder.ContactStorage#get(java.lang.Object)
	 */
	public abstract Contact get(Object uid) throws Exception;

	/**
	 * @see org.columba.addressbook.folder.ContactStorage#count()
	 */
	public int count() {
		return cacheStorage.count();
	}

	/**
	 * @see org.columba.addressbook.folder.ContactStorage#exists(java.lang.Object)
	 */
	public boolean exists(Object uid) {
		return cacheStorage.exists(uid);
	}

	/**
	 * Get all contact *and* group items of this folder.
	 * <p>
	 * GroupItems are retrieved accessing the subfolders of this folder.
	 * <p>
	 * 
	 * @return
	 */
	public HeaderItemList getHeaderItemList() throws Exception {
		// create list containing all contact item of this folder
		HeaderItemList list = new HeaderItemList(getContactItemMap());

		// add group items
		for (int i = 0; i < getChildCount(); i++) {
			GroupFolder groupFolder = (GroupFolder) getChildAt(i);
			GroupItem groupItem = new GroupItem(groupFolder.getGroup());
			list.add(groupItem);
		}

		return list;
	}
	
	/**
	 * @return Returns the messageUid.
	 */
	public int getNextMessageUid() {
		return nextMessageUid;
	}
	/**
	 * @param messageUid The messageUid to set.
	 */
	public void setNextMessageUid(int messageUid) {
		this.nextMessageUid = messageUid;
	}
	
	public Integer generateNextMessageUid() {
		return new Integer(nextMessageUid++);
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

QDox parse error in file://<WORKSPACE>/data/code-rep-dataset/Dataset2/Tasks/2056.java