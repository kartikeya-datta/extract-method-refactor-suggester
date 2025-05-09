error id: file://<WORKSPACE>/data/code-rep-dataset/Dataset2/Tasks/6961.java
file://<WORKSPACE>/data/code-rep-dataset/Dataset2/Tasks/6961.java
### com.thoughtworks.qdox.parser.ParseException: syntax error @[1,17]

error in qdox parser
file content:
```java
offset: 17
uri: file://<WORKSPACE>/data/code-rep-dataset/Dataset2/Tasks/6961.java
text:
```scala
public abstract v@@oid removeMessage(Object uid, WorkerStatusController worker) throws Exception;

// This program is free software; you can redistribute it and/or modify
// it under the terms of the GNU General Public License as published by
// the Free Software Foundation; either version 2 of the License, or
// (at your option) any later version.
//
// This program is distributed in the hope that it will be useful,
// but WITHOUT ANY WARRANTY; without even the implied warranty of
// MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
// GNU Library General Public License for more details.
//
// You should have received a copy of the GNU General Public License
// along with this program; if not, write to the Free Software
// Foundation, Inc., 59 Temple Place - Suite 330, Boston, MA 02111-1307, USA.

package org.columba.mail.folder;

import java.io.File;
import java.util.Vector;

import javax.swing.tree.TreeNode;
import javax.swing.tree.TreePath;

import org.columba.core.command.WorkerStatusController;
import org.columba.core.config.AdapterNode;
import org.columba.core.config.ConfigPath;
import org.columba.core.io.DiskIO;
import org.columba.mail.command.FolderCommandReference;
import org.columba.mail.config.FolderItem;
import org.columba.mail.filter.Filter;
import org.columba.mail.filter.FilterList;
import org.columba.mail.message.AbstractMessage;
import org.columba.mail.message.ColumbaHeader;
import org.columba.mail.message.HeaderList;
import org.columba.mail.message.MimePart;
import org.columba.mail.message.MimePartTree;

/**
 *    Abstract Basic Folder class. Is subclasses by every folder
 *    class containing messages and therefore offering methods
 *    to alter the mailbox
 *
 *@author       freddy
 *@created      19. Juni 2001
 */
public abstract class Folder extends FolderTreeNode {


	protected MessageFolderInfo messageFolderInfo;

	protected FilterList filterList;

	protected int uid;

	protected boolean changed;

	protected Vector folderListeners;
	
	protected File directoryFile; // directory
	
	/**
	 *    Description of the Field
	 *
	 *@since
	 */
	protected Vector treeNodeListeners;

	protected FolderItem item;

	/**
	 * Standard constructor. 
	 * 
	 * @param node <class>AdapterNode</class> this connects the config
	 *             to the JTree TreeModel
	 * @param item <class>FolderItem</class> contains information about
	 *             the folder
	 */
	public Folder(AdapterNode node, FolderItem item) {
		super(node);

		children = new Vector();

		this.node = node;
		this.item = item;

		if (item != null)
			this.uid = item.getUid();

		this.uid = uid;

		init();
		
		String dir = ConfigPath.getConfigDirectory() + "/mail/" + uid;
		if (DiskIO.ensureDirectory(dir))
			directoryFile = new File(dir);
	}
	
	

	/**
	 * Constructor for creating temporary-folders or other types
	 * which work only in memory and aren't visible in the <class>
	 * TreeView</class>
	 * 
	 * @param name Name of the folder
	 */
	// use this constructor only for tempfolders
	public Folder(String name) {
		super(null);

		children = new Vector();

		this.item = null;

		this.uid = -1;

		init();
		
		String dir = ConfigPath.getConfigDirectory() + "/mail/" + name;
		if (DiskIO.ensureDirectory(dir))
			directoryFile = new File(dir);

	}
	
	
	/**
	 * Do some initialization work both constructors share
	 * 
	 */
	protected void init() {

		messageFolderInfo = new MessageFolderInfo();

		changed = false;

		folderListeners = new Vector();

		treeNodeListeners = new Vector();
		
		
		
	}

	/**
	 * Method getDirectoryFile returns the the folder where messages
	 * are saved
	 * 
	 * @return File <class>File</class>-class representing the mailbox directory
	 */
	public File getDirectoryFile() {
		return directoryFile;
	}
	
	/**
	 * @see org.columba.modules.mail.folder.FolderTreeNode#createChildren(WorkerStatusController)
	 */
	public void createChildren( WorkerStatusController  worker )
	{}
	
	

	/**
	 * Method applyFilter.
	 * @param uids
	 * @return boolean
	 * @throws Exception
	 */
	/*
	public boolean applyFilter(Object[] uids) throws Exception {
		boolean result = false;

		result = getFilterList().processAll(uids);

		return result;
	}
	*/
	/**
	 * Method setChanged.
	 * @param b
	 */
	public void setChanged(boolean b) {
		changed = b;
	}

	/**
	 * Method setMessageFolderInfo.
	 * @param i
	 */
	public void setMessageFolderInfo(MessageFolderInfo i) {
		messageFolderInfo = i;
	}

	/**
	 * Method setFilterList.
	 * @param list
	 */
	public void setFilterList(FilterList list) {
		filterList = list;
	}

	/**
	 * Method getUid
	 * 
	 * @return int UID of <class>Folder</class>
	 */
	public int getUid() {
		return uid;
	}

	/**
	 * Method getChanged.
	 * @return boolean
	 */
	public boolean getChanged() {
		return changed;
	}

	/**
	 * Method getMessageFolderInfo.
	 * @return MessageFolderInfo
	 */
	public MessageFolderInfo getMessageFolderInfo() {
		return messageFolderInfo;
	}

	/**
	 * Method getFilterList.
	 * @return FilterList
	 */
	public FilterList getFilterList() {
		return filterList;
	}

	/**
	 * Method hasFilters.
	 * @return boolean
	 */
	public boolean hasFilters() {
		if (getFilterList() == null)
			return false;

		if (getFilterList().count() > 0) {
			return true;
		} else {
			return false;
		}
	}
	
	public abstract void expungeFolder(WorkerStatusController worker ) throws Exception;
	

	/**
	 * Method addMessage.
	 * 
	 * @param message Message object can be null 
	 * @param source  raw string of message 
	 * @return Object UID of message
	 * @throws Exception
	 */
	public abstract Object addMessage(AbstractMessage message, WorkerStatusController worker) throws Exception;
	
	
	public abstract Object addMessage(String source, WorkerStatusController worker) throws Exception;
	/**
	 * Method exists.
	 * 
	 * @param uid UID of message
	 * @return boolean true, if message exists
	 * @throws Exception
	 */
	public abstract boolean exists( Object uid ) throws Exception;
		
	/**
	 * Method getHeaderList.
	 * 
	 * @param worker for accessing the <class>StatusBar</class>
	 * @return HeaderList 
	 * @throws Exception
	 */
	public abstract HeaderList getHeaderList(WorkerStatusController worker)
		throws Exception;

	/**
	 * Method mark.
	 * @param uid
	 * @param variant
	 * @param worker
	 * @throws Exception
	 */
	public abstract void markMessage(Object[] uids, int variant, WorkerStatusController worker) throws Exception;

	/**
	 * Method removeMessage.
	 * @param uid
	 * @throws Exception
	 */
	public abstract void removeMessage(Object uid) throws Exception;

	/**
	 * Method getMimePart.
	 * @param uid
	 * @param address
	 * @param worker
	 * @return MimePart
	 * @throws Exception
	 */
	public abstract MimePart getMimePart(
		Object uid,
		Integer[] address,
		WorkerStatusController worker)
		throws Exception;

	/**
	 * Method getMessageSource.
	 * @param uid
	 * @param worker
	 * @return String
	 * @throws Exception
	 */
	public abstract String getMessageSource(Object uid, WorkerStatusController worker)
		throws Exception;

	/**
	 * Method getMimePartTree.
	 * @param uid
	 * @param worker
	 * @return MimePartTree
	 * @throws Exception
	 */
	public abstract MimePartTree getMimePartTree(Object uid, WorkerStatusController worker) throws Exception;

	/**
	 * Method getHeader.
	 * @param uid
	 * @param worker
	 * @return ColumbaHeader
	 * @throws Exception
	 */
	public abstract ColumbaHeader getMessageHeader(Object uid, WorkerStatusController worker) throws Exception;

	/**
	 * Method getMessage.
	 * @param uid
	 * @param worker
	 * @return AbstractMessage
	 * @throws Exception
	 */
	public abstract AbstractMessage getMessage(Object uid, WorkerStatusController worker) throws Exception;

	/**
	 * Method addFolderListener.
	 * @param listener
	 */
	public void addFolderListener(FolderChangeListener listener) {
		folderListeners.add(listener);
	}

	/**
	 * Method removeFolderListener.
	 * @param listener
	 */
	public void removeFolderListener(FolderChangeListener listener) {
		folderListeners.remove(listener);
	}

	/**
	 * @see javax.swing.tree.DefaultMutableTreeNode#getPathToRoot(TreeNode, int)
	 */
	protected TreeNode[] getPathToRoot(TreeNode aNode, int depth) {
		TreeNode[] retNodes;

		if (aNode == null) {
			if (depth == 0)
				return null;
			else
				retNodes = new TreeNode[depth];
		} else {
			depth++;
			retNodes = getPathToRoot(aNode.getParent(), depth);
			retNodes[retNodes.length - depth] = aNode;
		}
		return retNodes;
	}

	/**
	 * Method getTreePath.
	 * @return String
	 */
	public String getTreePath() {
		TreeNode[] treeNode = getPathToRoot(this, 0);

		StringBuffer path = new StringBuffer();

		for (int i = 1; i < treeNode.length; i++) {
			FolderTreeNode folder = (FolderTreeNode) treeNode[i];
			path.append("/" + folder.getName());
		}

		return path.toString();
	}

	/**
	 * Method getSelectionTreePath.
	 * @return TreePath
	 */
	public TreePath getSelectionTreePath() {
		//TreeNodeList list = new TreeNodeList( getTreePath() );
		TreeNode[] treeNodes = getPathToRoot(this, 0);
		TreePath path = new TreePath(treeNodes[0]);

		for (int i = 1; i < treeNodes.length; i++) {
			Folder folder = (Folder) treeNodes[i];
			path.pathByAddingChild(folder);
		}

		return path;
	}

	/**
	 * @see org.columba.modules.mail.folder.FolderTreeNode#getChild(String)
	 */
	/************************************ treenode implementation ***********/

	public TreeNode getChild(String str) {
		for (int i = 0; i < getChildCount(); i++) {
			Folder child = (Folder) getChildAt(i);
			String name = child.getName();

			if (name.equalsIgnoreCase(str))
				return child;
		}
		return null;
	}

	/**
	 * Method isParent.
	 * @param folder
	 * @return boolean
	 */
	public boolean isParent(Folder folder) {

		Folder parent = (Folder) folder.getParent();
		if (parent == null)
			return false;

		//while ( parent.getUid() != 100 )
		while (parent.getFolderItem() != null) {

			if (parent.getUid() == getUid()) {

				return true;
			}

			parent = (Folder) parent.getParent();
		}

		return false;
	}

	/**
	 * @see org.columba.modules.mail.folder.FolderTreeNode#getName()
	 */
	public String getName() {
		String name = null;

		FolderItem item = getFolderItem();
		name = item.getName();

		return name;
	}

	/**
	 * @see org.columba.modules.mail.folder.FolderTreeNode#setName(String)
	 */
	public void setName(String newName) {

		FolderItem item = getFolderItem();
		item.setName(newName);

	}
	
	public String toString()
	{
		return getName();
	}

	/**
	 * Method getFolderItem.
	 * @return FolderItem
	 */
	/************************* FolderItem ****************************/

	public FolderItem getFolderItem() {
		return item;
	}

	
	/**
	 * Method renameFolder.
	 * @param name
	 * @return boolean
	 * @throws Exception
	 */
	public boolean renameFolder(String name) throws Exception {
		setName( name );
		
		return true;
	}
	
	
	/**
	 * Method removeAll.
	 */
	public void removeAll()
	{
	}
	
	/**
	 * Method getUids.
	 * @return Object[]
	 */
	public Object[] getUids(WorkerStatusController worker) throws Exception
	{
		return null;
	}
	
	public abstract Object[] searchMessages(
		Filter filter,
		Object[] uids,
		WorkerStatusController worker) throws Exception;
		
	public abstract Object[] searchMessages(
		Filter filter,
		WorkerStatusController worker) throws Exception;
		
	public FolderCommandReference[] getCommandReference( FolderCommandReference[]  r)
	{
		return r;
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

QDox parse error in file://<WORKSPACE>/data/code-rep-dataset/Dataset2/Tasks/6961.java