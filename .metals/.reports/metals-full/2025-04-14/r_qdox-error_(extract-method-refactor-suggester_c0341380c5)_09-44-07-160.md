error id: file://<WORKSPACE>/data/code-rep-dataset/Dataset2/Tasks/903.java
file://<WORKSPACE>/data/code-rep-dataset/Dataset2/Tasks/903.java
### com.thoughtworks.qdox.parser.ParseException: syntax error @[1,1]

error in qdox parser
file content:
```java
offset: 1
uri: file://<WORKSPACE>/data/code-rep-dataset/Dataset2/Tasks/903.java
text:
```scala
.@@getSourceFolder();

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
package org.columba.mail.gui.config.subscribe;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Enumeration;
import java.util.Iterator;
import java.util.List;
import java.util.regex.Pattern;

import javax.swing.tree.DefaultMutableTreeNode;
import javax.swing.tree.DefaultTreeModel;
import javax.swing.tree.TreeNode;

import org.columba.core.command.Command;
import org.columba.core.command.WorkerStatusController;
import org.columba.core.util.ListTools;
import org.columba.mail.folder.imap.IMAPRootFolder;
import org.columba.mail.imap.IMAPServer;
import org.columba.ristretto.imap.ListInfo;
import org.columba.ristretto.imap.Namespace;
import org.columba.ristretto.imap.NamespaceCollection;
import org.frapuccino.checkabletree.CheckableItemImpl;

public class SynchronizeFolderListCommand extends Command {
	private Pattern delimiterPattern;

	private IMAPRootFolder root;

	private IMAPServer store;

	private TreeNode node;

	private String delimiter;

	/**
	 * @param references
	 */
	public SynchronizeFolderListCommand(SubscribeCommandReference reference) {
		super(reference);
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see org.columba.core.command.Command#execute(org.columba.core.command.Worker)
	 */
	public void execute(WorkerStatusController worker) throws Exception {
		root = (IMAPRootFolder) ((SubscribeCommandReference) getReference())
				.getFolder();

		store = root.getServer();

		node = createTreeStructure();
	}

	private List fetchUnsubscribedFolders(String reference) throws Exception {
		NamespaceCollection namespaces;

		//Does the server support the namespace extension?
		if (store.isSupported("NAMESPACE")) {
			namespaces = store.fetchNamespaces();
		} else {
			// create default namespace
			namespaces = new NamespaceCollection();
			namespaces.addPersonalNamespace(new Namespace("", "/"));
		}

		ArrayList result = new ArrayList();
		Iterator it;

		//Process personal namespaces
		if (namespaces.getPersonalNamespaceSize() > 0) {
			it = namespaces.getPersonalIterator();
			while (it.hasNext()) {
				Namespace pN = (Namespace) it.next();

				ListInfo[] list = store.list("", pN.getPrefix() + '%');
				result.addAll(Arrays.asList(list));
			}
		}

		//Process other users namespaces
		if (namespaces.getOtherUserNamespaceSize() > 0) {
			it = namespaces.getOtherUserIterator();
			while (it.hasNext()) {
				Namespace pN = (Namespace) it.next();

				ListInfo[] list = store.list("", pN.getPrefix() + '%');
				result.addAll(Arrays.asList(list));
			}
		}

		//Process shared namespaces
		if (namespaces.getSharedNamespaceSize() > 0) {
			it = namespaces.getSharedIterator();
			while (it.hasNext()) {
				Namespace pN = (Namespace) it.next();

				ListInfo[] list = store.list("", pN.getPrefix() + '%');
				result.addAll(Arrays.asList(list));
			}
		}

		// Handle special case in which INBOX has a NIL delimiter
		// -> there  might exist a pseudo hierarchy under INBOX+delimiter
		it = result.iterator();
		while( it.hasNext() ) {
			ListInfo info = (ListInfo) it.next();
			if( info.getName().equalsIgnoreCase("INBOX")) {
				if( info.getDelimiter() == null) {
					result.addAll(Arrays.asList(store.list("", "INBOX" + store.getDelimiter() +'%')));
				}				
				break;
			}
		}
		
		return result;
	}

	private TreeNode createTreeStructure() throws Exception {
		ListInfo[] lsub = store.fetchSubscribedFolders();

		// Create list of unsubscribed folders
		List subscribedFolders = new ArrayList(Arrays.asList(lsub));		
		// INBOX is always subscribed
		subscribedFolders.add(new ListInfo("INBOX",null,0));
		
		List unsubscribedFolders = fetchUnsubscribedFolders("");
		ListTools.substract(unsubscribedFolders, subscribedFolders);

		// Now we have the subscribed folders in subscribedFolders
		// and the unsubscribed folders in unsubscribedFolders
		// Next step: Create a treestructure
		DefaultMutableTreeNode root = new CheckableItemImpl();

		// Initialize the Pattern
		String pattern = "([^\\" + store.getDelimiter() + "]+)\\"
				+ store.getDelimiter() + "?";
		delimiterPattern = Pattern.compile(pattern);
		delimiter = store.getDelimiter();


		Iterator it = unsubscribedFolders.iterator();

		while (it.hasNext()) {
			ListInfoTreeNode node = insertTreeNode((ListInfo) it.next(), root);
			node.setSelected(false);
		}

		it = subscribedFolders.iterator();

		while (it.hasNext()) {
			ListInfoTreeNode node = insertTreeNode((ListInfo) it.next(), root);
			node.setSelected(true);
		}
		return root;
	}

	private ListInfoTreeNode insertTreeNode(ListInfo listInfo,
			DefaultMutableTreeNode parent) {
		//split the hierarchical name with at the delimiters
		String[] hierarchy = listInfo.getName().split("\\" + listInfo.getDelimiter());
		
		DefaultMutableTreeNode actParent = parent;
		StringBuffer mailboxName = new StringBuffer();
		
		mailboxName.append(hierarchy[0]);
		actParent = ensureChild(hierarchy[0], mailboxName.toString(),
				actParent);

		for( int i=1; i<hierarchy.length; i++) {
			mailboxName.append(listInfo.getDelimiter());
			mailboxName.append(hierarchy[i]);
			actParent = ensureChild(hierarchy[i], mailboxName.toString(),
					actParent);
		}

		return (ListInfoTreeNode) actParent;
	}

	private DefaultMutableTreeNode ensureChild(String name, String mailbox,
			DefaultMutableTreeNode parent) {
		Enumeration children = parent.children();
		ListInfoTreeNode node;

		while (children.hasMoreElements()) {
			node = (ListInfoTreeNode) children.nextElement();

			if (node.toString().equals(name)) {
				return node;
			}
		}

		node = new ListInfoTreeNode(name, mailbox);
		parent.add(node);

		return node;
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see org.columba.core.command.Command#updateGUI()
	 */
	public void updateGUI() throws Exception {
		SubscribeDialog dialog = ((SubscribeCommandReference) getReference())
				.getDialog();

		dialog.syncFolderListDone(new DefaultTreeModel(node));
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

QDox parse error in file://<WORKSPACE>/data/code-rep-dataset/Dataset2/Tasks/903.java