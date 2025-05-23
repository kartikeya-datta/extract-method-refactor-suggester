error id: file://<WORKSPACE>/data/code-rep-dataset/Dataset2/Tasks/6914.java
file://<WORKSPACE>/data/code-rep-dataset/Dataset2/Tasks/6914.java
### com.thoughtworks.qdox.parser.ParseException: syntax error @[1,8]

error in qdox parser
file content:
```java
offset: 8
uri: file://<WORKSPACE>/data/code-rep-dataset/Dataset2/Tasks/6914.java
text:
```scala
public v@@oid addSubfolder(IMailFolder child) throws Exception {

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

package org.columba.mail.folder.imap;

import java.util.logging.Logger;

import org.columba.api.command.ICommand;
import org.columba.api.command.IStatusObservable;
import org.columba.core.command.Command;
import org.columba.core.command.CommandProcessor;
import org.columba.core.command.NullWorkerStatusController;
import org.columba.core.command.StatusObservableImpl;
import org.columba.core.connectionstate.ConnectionStateImpl;
import org.columba.core.filter.Filter;
import org.columba.mail.command.MailFolderCommandReference;
import org.columba.mail.config.AccountItem;
import org.columba.mail.config.FolderItem;
import org.columba.mail.config.IFolderItem;
import org.columba.mail.config.MailConfig;
import org.columba.mail.config.SpecialFoldersItem;
import org.columba.mail.folder.AbstractFolder;
import org.columba.mail.folder.IMailFolder;
import org.columba.mail.folder.RootFolder;
import org.columba.mail.gui.tree.FolderTreeModel;
import org.columba.mail.imap.FetchSubFolderListCommand;
import org.columba.mail.imap.IExistsChangedAction;
import org.columba.mail.imap.IFirstLoginAction;
import org.columba.mail.imap.IImapServer;
import org.columba.mail.imap.IMAPServer;
import org.columba.mail.imap.IMAPServerOwner;
import org.columba.mail.imap.IUpdateFlagAction;
import org.columba.mail.util.MailResourceLoader;
import org.columba.ristretto.imap.IMAPFlags;
import org.columba.ristretto.imap.ListInfo;

/**
 * Root folder for IMAP folders.
 */
public class IMAPRootFolder extends AbstractFolder implements RootFolder,
		IMAPServerOwner {

	/** JDK 1.4+ logging framework logger, used for logging. */
	private static final Logger LOG = Logger
			.getLogger("org.columba.mail.folder.imap");

	private static final String[] SPECIAL_FOLDER_NAMES = { "trash", "drafts",
			"templates", "sent" };

	// private ImapOperator operator;
	private AccountItem accountItem;

	private IImapServer server;

	private IMAPRootFolder thisFolder = this;

	/**
	 * parent directory for mail folders
	 * 
	 * for example: "/home/donald/.columba/mail/"
	 */
	private String parentPath;

	/**
	 * Status information updates are handled in using IStatusObservable.
	 * <p>
	 * Every command has to register its interest to this events before
	 * accessing the folder.
	 */
	protected IStatusObservable observable;

	public IMAPRootFolder(FolderItem folderItem, String path) {
		// super(node, folderItem);
		super(folderItem);

		// remember parent path
		// (this is necessary for IMAPRootFolder sync operations)
		parentPath = path;

		observable = new StatusObservableImpl();

		accountItem = MailConfig.getInstance().getAccountList().uidGet(
				folderItem.getInteger("account_uid"));

		updateConfiguration();
	}

	public IMAPRootFolder(AccountItem accountItem, String path) {
		// super(node, folderItem);
		// super(getDefaultItem("IMAPRootFolder", getDefaultProperties()));
		super(accountItem.get("name"), "IMAPRootFolder");
		observable = new StatusObservableImpl();

		// remember parent path
		// (this is necessary for IMAPRootFolder sync operations)
		parentPath = path;

		this.accountItem = accountItem;

		getConfiguration().setInteger("account_uid",
				accountItem.getInteger("uid"));

		updateConfiguration();
	}

	/**
	 * @param type
	 */
	public IMAPRootFolder(String name, String type) {
		super(name, type);

		IFolderItem item = getConfiguration();
		item.setString("property", "accessrights", "system");
		item.setString("property", "subfolder", "true");
	}

	public String getDefaultChild() {
		return "IMAPFolder";
	}

	/**
	 * @return observable containing status information
	 */
	public IStatusObservable getObservable() {
		return observable;
	}

	protected void syncFolder(AbstractFolder parent, String name, ListInfo info)
			throws Exception {

		if ((name.indexOf(server.getDelimiter()) != -1)
				&& (name.indexOf(server.getDelimiter()) != (name.length() - 1))) {
			// delimiter found
			// -> recursively create all necessary folders to create
			// -> the final folder
			String subchild = name.substring(0, name.indexOf(server
					.getDelimiter()));
			AbstractFolder subFolder = (AbstractFolder) parent
					.findChildWithName(subchild, false, IMAPFolder.class);

			// if folder doesn't exist already
			if (subFolder == null) {
				subFolder = new IMAPFolder(subchild, "IMAPFolder",
						getParentPath());
				parent.add(subFolder);
				parent.getConfiguration().getRoot().addElement(
						subFolder.getConfiguration().getRoot());
				FolderTreeModel.getInstance().insertNodeInto(subFolder, parent,
						parent.getIndex(subFolder));

				((IMAPFolder) subFolder).existsOnServer = true;
				subFolder.getConfiguration().setString("selectable", "false");

				// this is the final folder
				// subFolder = addIMAPChildFolder(parent, info, subchild);
			} else {
				if (!((IMAPFolder) subFolder).existsOnServer) {
					((IMAPFolder) subFolder).existsOnServer = true;
					subFolder.getConfiguration().setString("selectable",
							"false");
				}
			}

			// recursively go on
			syncFolder(subFolder, name.substring(name.indexOf(server
					.getDelimiter())
					+ server.getDelimiter().length()), info);
		} else {
			// no delimiter found
			// -> this is already the final folder
			// if folder doesn't exist already
			AbstractFolder subFolder = (AbstractFolder) parent
					.findChildWithName(name, false, IMAPFolder.class);

			if (subFolder == null) {
				subFolder = new IMAPFolder(name, "IMAPFolder", getParentPath());
				parent.add(subFolder);
				parent.getConfiguration().getRoot().addElement(
						subFolder.getConfiguration().getRoot());
				FolderTreeModel.getInstance().insertNodeInto(subFolder, parent,
						parent.getIndex(subFolder));
			}
			((IMAPFolder) subFolder).existsOnServer = true;

			// Check the Noselect flag
			if (info.getParameter(ListInfo.NOSELECT)) {
				subFolder.getConfiguration().setString("selectable", "false");
			} else {
				subFolder.getConfiguration().setString("selectable", "true");
			}

			// Check the Noinferior flag
			if (info.getParameter(ListInfo.NOINFERIORS)
					&& info.getDelimiter() != null) {
				subFolder.getConfiguration().setString("noinferiors", "true");
			} else {
				subFolder.getConfiguration().setString("noinferiors", "false");
			}
		}
	}

	protected void markAllSubfoldersAsExistOnServer(AbstractFolder parent,
			boolean value) {
		AbstractFolder child;

		for (int i = 0; i < parent.getChildCount(); i++) {
			child = (AbstractFolder) parent.getChildAt(i);

			if (child instanceof IMAPFolder) {
				markAllSubfoldersAsExistOnServer(child, value);
			}
		}

		if (parent instanceof IMAPFolder) {
			((IMAPFolder) parent).existsOnServer = value;
		}
	}

	private boolean removeNotMarkedSubfolders(AbstractFolder parent)
			throws Exception {
		AbstractFolder child;

		// first remove all subfolders recursively
		for (int i = 0; i < parent.getChildCount(); i++) {
			child = (AbstractFolder) parent.getChildAt(i);

			if (child instanceof IMAPFolder) {
				if (removeNotMarkedSubfolders(child)) {
					// A child got removed -> stay at this position to
					// get the next
					i--;
				}
			}
		}

		// maybe remove this folder
		if (parent instanceof IMAPFolder) {
			if (!((IMAPFolder) parent).existsOnServer) {
				FolderTreeModel.getInstance().removeNodeFromParent(parent);
				parent.removeFolder();
				return true;
			}
		}

		return false;
	}

	public void findSpecialFolders() {
		SpecialFoldersItem folders = accountItem.getSpecialFoldersItem();

		for (int i = 0; i < SPECIAL_FOLDER_NAMES.length; i++) {
			// Find special
			int specialUid = folders.getInteger(SPECIAL_FOLDER_NAMES[i]);

			// if have already a suitable folder skip the search
			if (this.findChildWithUID(specialUid, true) == null) {
				// search for a folder thats on the IMAP account
				// first try to find the local translation of special
				AbstractFolder specialFolder = this.findChildWithName(
						MailResourceLoader.getString("tree",
								SPECIAL_FOLDER_NAMES[i]), true);

				if (specialFolder == null) {
					// fall back to the english version
					specialFolder = this.findChildWithName(
							SPECIAL_FOLDER_NAMES[i], true);
				}

				if (specialFolder != null) {
					// we found a suitable folder -> set it
					folders.setInteger(SPECIAL_FOLDER_NAMES[i], specialFolder
							.getUid());
				}
			}
		}
	}

	public void syncSubscribedFolders() throws Exception {
		// first clear all flags
		markAllSubfoldersAsExistOnServer(this, false);

		IMAPFolder inbox = (IMAPFolder) this.findChildWithName("INBOX", false);
		inbox.existsOnServer = true;

		try {
			// create and tag all subfolders on server
			ListInfo[] listInfo = getServer().fetchSubscribedFolders();

			for (int i = 0; i < listInfo.length; i++) {
				ListInfo info = listInfo[i];
				LOG.fine("delimiter=" + getServer().getDelimiter());

				String folderPath = info.getName();

				syncFolder(this, folderPath, info);
			}

		} catch (Exception ex) {
			ex.printStackTrace();
		}

		// This fixes the strange behaviour of the courier imapserver
		// which sets the \Noselect flag on INBOX
		inbox.getConfiguration().setString("selectable", "true");

		removeNotMarkedSubfolders(this);

		findSpecialFolders();
	}

	public IImapServer getServer() {
		return server;
	}

	public void updateConfiguration() {
		try {
			if (server != null)
				server.logout();
		} catch (Exception e1) {
			// don't care
		}
		server = new IMAPServer(accountItem.getImapItem());
		server.setObservable(observable);

		server.setFirstLoginAction(new IFirstLoginAction() {
			public void actionPerformed() {
				// If we are online sync the subscribed folders on first
				// connection
				if (ConnectionStateImpl.getInstance().isOnline()) {
					ICommand c = new FetchSubFolderListCommand(
							new MailFolderCommandReference(thisFolder));
					try {
						// MainInterface.processor.addOp(c);
						c.execute(NullWorkerStatusController.getInstance());
						c.updateGUI();
					} catch (Exception e) {
						e.printStackTrace();
					}
				}
			}

		});

		server.setExistsChangedAction(new IExistsChangedAction() {

			public void actionPerformed(IMailFolder folder) {
				// Trigger synchronization of the selected Folder
				Command updateFolderCommand = new CheckForNewMessagesCommand(
						null, new MailFolderCommandReference(folder));
				CommandProcessor.getInstance().addOp(updateFolderCommand);
			}

		});

		server.setUpdateFlagAction(new IUpdateFlagAction() {

			public void actionPerformed(IMailFolder folder, IMAPFlags flags) {
				// Trigger synchronization of the IMAPFolder
				Command updateFlagCommand = new UpdateFlagCommand(
						new MailFolderCommandReference(folder), flags);
				CommandProcessor.getInstance().addOp(updateFlagCommand);

			}

		});
	}

	/**
	 * @see org.columba.mail.folder.Folder#searchMessages(org.columba.mail.filter.Filter,
	 *      java.lang.Object, org.columba.api.command.IWorkerStatusController)
	 */
	public Object[] searchMessages(Filter filter, Object[] uids)
			throws Exception {
		return null;
	}

	/**
	 * @see org.columba.mail.folder.Folder#searchMessages(org.columba.mail.filter.Filter,
	 *      org.columba.api.command.IWorkerStatusController)
	 */
	public Object[] searchMessages(Filter filter) throws Exception {
		return null;
	}

	/**
	 * @return
	 */
	public AccountItem getAccountItem() {
		return accountItem;
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see org.columba.mail.folder.FolderTreeNode#addSubfolder(org.columba.mail.folder.FolderTreeNode)
	 */
	public void addSubfolder(AbstractFolder child) throws Exception {
		if (child instanceof IMAPFolder) {
			getServer().createMailbox(child.getName(), null);
		}

		super.addSubfolder(child);
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see org.columba.mail.folder.Folder#save()
	 */
	public void save() throws Exception {
		LOG.info("Logout from IMAPServer " + getName());

		getServer().logout();
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see org.columba.mail.folder.RootFolder#getTrashFolder()
	 */
	public AbstractFolder getTrashFolder() {
		AbstractFolder ret = findChildWithUID(accountItem
				.getSpecialFoldersItem().getInteger("trash"), true);

		// has the imap account no trash folder using the default trash folder
		if (ret == null) {
			ret = (AbstractFolder) FolderTreeModel.getInstance()
					.getTrashFolder();
		}

		return ret;
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see org.columba.mail.folder.RootFolder#getInbox()
	 */
	public AbstractFolder getInboxFolder() {
		return (IMAPFolder) this.findChildWithName("INBOX", false);
	}

	/**
	 * Parent directory for mail folders.
	 * <p>
	 * For example: /home/donald/.columba/mail
	 * 
	 * @return Returns the parentPath.
	 */
	public String getParentPath() {
		return parentPath;
	}

	/**
	 * @see org.columba.mail.folder.AbstractFolder#supportsAddFolder()
	 */
	public boolean supportsAddFolder(String folder) {
		return true;
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

QDox parse error in file://<WORKSPACE>/data/code-rep-dataset/Dataset2/Tasks/6914.java