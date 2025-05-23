error id: file://<WORKSPACE>/data/code-rep-dataset/Dataset2/Tasks/1339.java
file://<WORKSPACE>/data/code-rep-dataset/Dataset2/Tasks/1339.java
### com.thoughtworks.qdox.parser.ParseException: syntax error @[1,1]

error in qdox parser
file content:
```java
offset: 1
uri: file://<WORKSPACE>/data/code-rep-dataset/Dataset2/Tasks/1339.java
text:
```scala
(@@IFolderListener) listeners[i + 1]);

//The contents of this file are subject to the Mozilla Public License Version 1.1
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
//The Initial Developers of the Original Code are Frederik Dietz and Timo Stich.
//Portions created by Frederik Dietz and Timo Stich are Copyright (C) 2003.
//
//All Rights Reserved.

package org.columba.mail.folder;

import java.util.logging.Logger;

import javax.swing.event.EventListenerList;
import javax.swing.tree.DefaultMutableTreeNode;
import javax.swing.tree.TreeNode;
import javax.swing.tree.TreePath;

import org.columba.core.base.Lock;
import org.columba.core.xml.XmlElement;
import org.columba.mail.config.FolderItem;
import org.columba.mail.config.IFolderItem;
import org.columba.mail.folder.event.FolderEvent;
import org.columba.mail.folder.event.FolderEventDelegator;
import org.columba.mail.folder.event.FolderListener;
import org.columba.mail.folder.event.IFolderListener;

/**
 * Represents a treenode and is the abstract class every folder extends.
 * <p>
 * See tree.xml configuration file.
 * 
 * @author fdietz
 */
public abstract class AbstractFolder extends DefaultMutableTreeNode implements IMailFolder {

	
	/** JDK 1.4+ logging framework logger, used for logging. */
	private static final Logger LOG = Logger
			.getLogger("org.columba.mail.folder");

	// the next new folder will get this UID
	private static int nextUid = 0;

	// folderitem wraps xml configuration from tree.xml
	protected IFolderItem node;

	// locking mechanism
	protected Lock myLock = new Lock();

	// Root folder cache
	private IMailFolder rootFolder;

	protected EventListenerList listenerList = new EventListenerList();

	public AbstractFolder(String name, String type) {
		super();

		XmlElement defaultElement = new XmlElement("folder");
		defaultElement.addAttribute("type", type);
		defaultElement.addAttribute("uid", Integer.toString(nextUid++));
		defaultElement.addElement(new XmlElement("property"));

		setConfiguration(new FolderItem(defaultElement));
		try {
			setName(name);
		} catch (Exception e) {
			LOG.severe(e.getMessage());
		}

		// register interest on tree node changes
		addFolderListener(FolderEventDelegator.getInstance());
	}

	public AbstractFolder() {
		super();

		// register interest on tree node changes
		addFolderListener(FolderEventDelegator.getInstance());
	}

	public AbstractFolder(FolderItem node) {
		super();
		setConfiguration(node);

		// register interest on tree node changes
		addFolderListener(FolderEventDelegator.getInstance());
	}

	/**
	 * Adds a listener.
	 */
	public void addFolderListener(IFolderListener l) {
		listenerList.add(IFolderListener.class, l);
	}

	/**
	 * Removes a previously registered listener.
	 */
	public void removeFolderListener(IFolderListener l) {
		listenerList.remove(IFolderListener.class, l);
	}

	/**
	 * Propagates an event to all registered listeners notifying them that this
	 * folder has been renamed.
	 */
	public void fireFolderPropertyChanged() {
		FolderEvent e = new FolderEvent(this);
		// Guaranteed to return a non-null array
		Object[] listeners = listenerList.getListenerList();

		// Process the listeners last to first, notifying
		// those that are interested in this event
		for (int i = listeners.length - 2; i >= 0; i -= 2) {
			if (listeners[i] == IFolderListener.class) {
				((IFolderListener) listeners[i + 1]).folderPropertyChanged(e);
			}
		}
	}

	/**
	 * Propagates an event to all registered listeners notifying them that a
	 * subfolder has been added to this folder.
	 */
	public void fireFolderAdded(IMailFolder folder) {
		FolderEvent e = new FolderEvent(this, folder);
		// Guaranteed to return a non-null array
		Object[] listeners = listenerList.getListenerList();

		// Process the listeners last to first, notifying
		// those that are interested in this event
		for (int i = listeners.length - 2; i >= 0; i -= 2) {
			if (listeners[i] == IFolderListener.class) {
				((IFolderListener) listeners[i + 1]).folderAdded(e);
			}
		}
	}

	/**
	 * Propagates an event to all registered listeners notifying them that this
	 * folder has been removed from its parent folder. This method removes all
	 * registered listeners.
	 */
	public void fireFolderRemoved() {
		FolderEvent e = new FolderEvent(this, this);
		// Guaranteed to return a non-null array
		Object[] listeners = listenerList.getListenerList();

		// Process the listeners last to first, notifying
		// those that are interested in this event
		for (int i = listeners.length - 2; i >= 0; i -= 2) {
			if (listeners[i] == IFolderListener.class) {
				((IFolderListener) listeners[i + 1]).folderRemoved(e);
				listenerList.remove(IFolderListener.class,
						(FolderListener) listeners[i + 1]);
			}
		}
	}

	/**
	 * Method getSelectionTreePath.
	 * 
	 * @return TreePath
	 */
	public TreePath getSelectionTreePath() {
		return new TreePath(getPathToRoot(this, 0));
	}

	/**
	 * @see javax.swing.tree.DefaultMutableTreeNode#getPathToRoot(TreeNode, int)
	 */
	protected TreeNode[] getPathToRoot(TreeNode aNode, int depth) {
		TreeNode[] retNodes;

		if (aNode == null) {
			if (depth == 0) {
				return null;
			} else {
				retNodes = new TreeNode[depth];
			}
		} else {
			depth++;
			retNodes = getPathToRoot(aNode.getParent(), depth);
			retNodes[retNodes.length - depth] = aNode;
		}

		return retNodes;
	}

	/**
	 * Return tree path as string
	 * 
	 * @return String tree path
	 */
	public String getTreePath() {
		TreeNode[] treeNode = getPathToRoot(this, 0);

		StringBuffer path = new StringBuffer();

		for (int i = 1; i < treeNode.length; i++) {
			AbstractFolder folder = (AbstractFolder) treeNode[i];
			path.append("/" + folder.getName());
		}

		return path.toString();
	}
	
	/**
	 * Returns the folder's UID.
	 */
	public int getUid() {
		return node.getInteger("uid");
	}

	/**
	 * Returns the folder's configuration.
	 */
	public IFolderItem getConfiguration() {
		return node;
	}

	/**
	 * Sets the folder's configuration.
	 */
	public void setConfiguration(IFolderItem node) {
		this.node = node;

		try {
			if (node.getInteger("uid") >= nextUid) {
				nextUid = node.getInteger("uid") + 1;
			}
		} catch (NumberFormatException ex) {
			node.setInteger("uid", nextUid++);
		}
	}

	public String getType() {
		IFolderItem item = getConfiguration();
		
		return item.get("type");
	}
	
	/**
	 * Returns the folder's name.
	 */
	public String getName() {
		String name = null;

		IFolderItem item = getConfiguration();
		name = item.getString("property", "name");

		//(tstich) it is necessary to return null because imap account
		//creation needs it!
		//if ( name == null ) name = "FIXME";
		
		return name;
	}

	public String toString() {
		return getName();
	}

	/**
	 * Sets the folder's name. This method notifies registered FolderListeners.
	 */
	public void setName(String newName) throws Exception {
		IFolderItem item = getConfiguration();
		item.setString("property", "name", newName);
		fireFolderPropertyChanged();
	}
	
	/*
	public MailFolderCommandReference getCommandReference(MailFolderCommandReference r) {
		return r;
	}
	*/

	/**
	 * ******************************** locking mechanism
	 * ***************************
	 */
	public boolean tryToGetLock(Object locker) {
		return myLock.tryToGetLock(locker);
	}

	public void releaseLock(Object locker) {
		myLock.release(locker);
	}

	/**
	 * ************************** treenode management
	 * ******************************
	 */
	public void insert(IMailFolder newFolder, int newIndex) {
		IMailFolder oldParent = (IMailFolder) newFolder.getParent();
		int oldIndex = oldParent.getIndex(newFolder);
		oldParent.remove(oldIndex);

		XmlElement oldParentNode = oldParent.getConfiguration().getRoot();
		XmlElement newChildNode = newFolder.getConfiguration().getRoot();
		oldParentNode.removeElement(newChildNode);

		newFolder.setParent(this);
		children.insertElementAt(newFolder, newIndex);

		XmlElement newParentNode = getConfiguration().getRoot();

		int j = -1;
		boolean inserted = false;

		for (int i = 0; i < newParentNode.count(); i++) {
			XmlElement n = newParentNode.getElement(i);
			String name = n.getName();

			if (name.equals("folder")) {
				j++;
			}

			if (j == newIndex) {
				newParentNode.insertElement(newChildNode, i);
				inserted = true;
			}
		}

		if (!inserted) {
			if ((j + 1) == newIndex) {
				newParentNode.append(newChildNode);
			}
		}
	}

	/**
	 * Removes this folder from its parent. This method will notify registered
	 * FolderListeners.
	 */
	public void removeFolder() throws Exception {
		// remove XmlElement
		getConfiguration().getRoot().getParent().removeElement(
				getConfiguration().getRoot());

		// notify listeners
		fireFolderRemoved();
		
	}

	/**
	 * Adds a child folder to this folder. This method will notify registered
	 * FolderListeners.
	 */
	public void addSubfolder(IMailFolder child) throws Exception {
		getConfiguration().getRoot().addElement(
				child.getConfiguration().getRoot());
		fireFolderAdded(child);
	}

	public AbstractFolder findChildWithName(String str, boolean recurse) {
		return findChildWithName(str, recurse, null);
	}

	public AbstractFolder findChildWithName(String str, boolean recurse, Class type) {
		for (int i = 0; i < getChildCount(); i++) {
			AbstractFolder child = (AbstractFolder) getChildAt(i);
			// Check the type
			if( type != null && !type.equals(child.getClass()) ) continue;				
			
			String name = child.getName();

			if (name.equalsIgnoreCase(str)) {
				return child;
			} else if (recurse) {
				AbstractFolder subchild = child.findChildWithName(str, true);

				if (subchild != null) {
					return subchild;
				}
			}
		}

		return null;
	}

	public AbstractFolder findChildWithUID(int uid, boolean recurse) {
		for (int i = 0; i < getChildCount(); i++) {
			AbstractFolder child = (AbstractMessageFolder) getChildAt(i);
			int childUid = child.getUid();

			if (uid == childUid) {
				return child;
			} else if (recurse) {
				AbstractFolder subchild = child.findChildWithUID(uid, true);

				if (subchild != null) {
					return subchild;
				}
			}
		}

		return null;
	}

	/**
	 * 
	 * AbstractFolder wraps XmlElement
	 * 
	 * all treenode manipulation is passed to the corresponding XmlElement
	 */
	public void moveTo(IMailFolder newParent) {
		// do the same for the XmlElement node
		getConfiguration().getRoot().removeFromParent();	
		
		// do the same for the XmlElement of child
		newParent.getConfiguration().getRoot().addElement(
				getConfiguration().getRoot());

		newParent.fireFolderAdded(this);
	}

	/** ******************* capabilities ************************************* */
	/**
	 * Does this treenode support adding messages?
	 * 
	 * @return true, if this folder is able to contain messages, false otherwise
	 *  
	 */
	public boolean supportsAddMessage() {
		return false;
	}

	/**
	 * Returns true if this folder can have sub folders of the specified type;
	 * false otherwise.
	 * 
	 * @param newFolderType
	 *            the folder that is going to be inserted as a child.
	 * @return true if this folder can have sub folders; false otherwise.
	 */
	public boolean supportsAddFolder(String newFolderType) {
		return false;
	}

	/**
	 * Returns true if this folder type can be moved around in the folder tree.
	 * 
	 * @return true if this folder type can be moved around in the folder tree.
	 */
	public boolean supportsMove() {
		return false;
	}

	/**
	 * Return the root folder of this folder.
	 * <p>
	 * This is especially useful when using IMAP. IMAP has a root folder which
	 * is labelled with the account name.
	 * 
	 * @return root parent folder of this folder
	 */
	public IMailFolder getRootFolder() {
		// If rootFolder is not cached traverse the tree
		if (rootFolder == null) {
			IMailFolder parent = (IMailFolder) getParent();

			// There is no parent
			if (parent == null) {
				return this;
			}

			if (parent instanceof RootFolder) {
				rootFolder = parent;
			} else {
				rootFolder = parent.getRootFolder();
			}
		}

		return rootFolder;
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

QDox parse error in file://<WORKSPACE>/data/code-rep-dataset/Dataset2/Tasks/1339.java