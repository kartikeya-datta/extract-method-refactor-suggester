error id: file://<WORKSPACE>/data/code-rep-dataset/Dataset2/Tasks/5870.java
file://<WORKSPACE>/data/code-rep-dataset/Dataset2/Tasks/5870.java
### com.thoughtworks.qdox.parser.ParseException: syntax error @[1,8]

error in qdox parser
file content:
```java
offset: 8
uri: file://<WORKSPACE>/data/code-rep-dataset/Dataset2/Tasks/5870.java
text:
```scala
public v@@oid expungeFolder(WorkerStatusController worker)

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
//
//$Log: IMAPRootFolder.java,v $
//
package org.columba.mail.folder.imap;

import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.Vector;

import javax.swing.ImageIcon;
import javax.swing.Timer;

import org.columba.core.command.WorkerStatusController;
import org.columba.core.gui.util.ImageLoader;
import org.columba.core.logging.ColumbaLogger;
import org.columba.core.main.MainInterface;
import org.columba.core.xml.XmlElement;
import org.columba.mail.command.FolderCommandReference;
import org.columba.mail.config.AccountItem;
import org.columba.mail.config.FolderItem;
import org.columba.mail.config.MailConfig;
import org.columba.mail.filter.Filter;
import org.columba.mail.folder.Folder;
import org.columba.mail.folder.FolderTreeNode;
import org.columba.mail.folder.Root;
import org.columba.mail.folder.command.CheckForNewMessagesCommand;
import org.columba.mail.imap.IMAPStore;
import org.columba.mail.imap.parser.Imap4Parser;
import org.columba.mail.imap.parser.ListInfo;
import org.columba.mail.imap.protocol.IMAPProtocol;
import org.columba.mail.message.AbstractMessage;
import org.columba.mail.message.ColumbaHeader;
import org.columba.mail.message.HeaderList;
import org.columba.mail.message.MimePart;
import org.columba.mail.message.MimePartTree;

public class IMAPRootFolder extends Folder implements ActionListener {
	protected final static ImageIcon imapRootIcon =
		ImageLoader.getSmallImageIcon("remotehost.png");

	private IMAPProtocol imap;
	//private boolean select=false;
	private boolean fetch = false;
	private Imap4Parser parser;
	private StringBuffer cache;
	private int state;
	private Vector lsubList;

	private final static int ONE_SECOND = 1000;
	private Timer timer;

	//    private ImapOperator operator;

	private AccountItem accountItem;

	private IMAPStore store;

	public IMAPRootFolder(FolderItem folderItem) {
		//super(node, folderItem);
		super(folderItem);

		accountItem =
			MailConfig.getAccountList().uidGet(
				folderItem.getInteger("account_uid"));

		store = new IMAPStore(accountItem.getImapItem(), this);
		
		restartTimer();

	}

	public IMAPRootFolder(AccountItem accountItem) {
		//super(node, folderItem);
		super(getDefaultItem("IMAPRootFolder", getDefaultProperties()));

		this.accountItem = accountItem;

		getFolderItem().set("account_uid", accountItem.getInteger("uid"));
		getFolderItem().set("property", "name", accountItem.get("name"));

		((Root) MainInterface.treeModel.getRoot()).addWithXml(this);

		store = new IMAPStore(accountItem.getImapItem(), this);
		
		restartTimer();

	}

	public ImageIcon getCollapsedIcon() {
		return imapRootIcon;
	}

	public ImageIcon getExpandedIcon() {
		return imapRootIcon;
	}

	public String getDefaultChild() {
		return "IMAPFolder";
	}

	/**
		 * @see org.columba.mail.folder.FolderTreeNode#addFolder(java.lang.String)
		 */
	/*
	public FolderTreeNode addFolder(String name) throws Exception {
	
		String path = name;
	
		boolean result = getStore().createFolder(path);
	
		if (result)
			return super.addFolder(name);
	
		return null;
	}
	*/

	// we can't use 
	//   "folder.addFolder(subchild)"
	// here
	//
	// -> this would tell the IMAP server to create a new
	// -> folder, too
	//
	// -> but we only want to create it in Columba without 
	// -> touching the server
	protected FolderTreeNode addIMAPChildFolder(
		FolderTreeNode folder,
		ListInfo info,
		String subchild)
		throws Exception {
		ColumbaLogger.log.debug("creating folder=" + subchild);

		ColumbaLogger.log.debug("info.getName()=" + info.getName());
		ColumbaLogger.log.debug("info.getLastName()=" + info.getLastName());

		if (subchild.equals(info.getLastName())) {

			// this is just a parent-folder we need to
			// create in order to create a child-folder
			ColumbaLogger.log.debug("creating immediate folder=" + subchild);

			return folder.addFolder(subchild, "IMAPFolder");

		} else {

			// this folder is associated with ListInfo
			// pass parameters to folderinfo 			
			ColumbaLogger.log.debug("create final folder" + subchild);

			IMAPFolder imapFolder =
				(IMAPFolder) folder.addFolder(subchild, "IMAPFolder");
			FolderItem folderItem = imapFolder.getFolderItem();

			folderItem.set("selectable", false);

			return imapFolder;
		}

	}

	protected void addIMAPSubFolder(
		FolderTreeNode folder,
		String name,
		ListInfo info)
		throws Exception {

		ColumbaLogger.log.debug("creating folder=" + name);

		if (name.indexOf(store.getDelimiter()) != -1) {

			// delimiter found
			//  -> recursively create all necessary folders to create
			//  -> the final folder 
			String subchild =
				name.substring(0, name.indexOf(store.getDelimiter()));
			FolderTreeNode subFolder =
				(FolderTreeNode) folder.getChild(subchild);

			// if folder doesn't exist already
			if (subFolder == null) {
				// this is the final folder
				subFolder = addIMAPChildFolder(folder, info, subchild);
			}

			// recursively go on
			addIMAPSubFolder(
				subFolder,
				name.substring(name.indexOf(store.getDelimiter()) + 1),
				info);

		} else {

			// no delimiter found
			//  -> this is already the final folder

			// if folder doesn't exist already
			if (folder.getChild(name) == null) {

				//addIMAPChildFolder(folder, info, name);
				folder.addFolder(name, "IMAPFolder");
			}

		}
	}

	public void createChildren(WorkerStatusController worker) {
		try {

			ListInfo[] listInfo = getStore().lsub("", "*", worker);

			for (int i = 0; i < listInfo.length; i++) {
				ListInfo info = listInfo[i];
				getStore().setDelimiter(info.getDelimiter());
				ColumbaLogger.log.debug(
					"delimiter=" + getStore().getDelimiter());

				addIMAPSubFolder(this, info.getName().trim(), info);

			}
		} catch (Exception ex) {
			ex.printStackTrace();

		} finally {

		}
	}

	public IMAPStore getStore() {
		return store;
	}

	public void restartTimer() {

		if (accountItem.getImapItem().getBoolean("enable_mailcheck")) {
			int interval =
				accountItem.getImapItem().getInteger("mailcheck_interval");

			timer = new Timer(ONE_SECOND * interval * 60, this);
			timer.restart();

			System.out.println("---------------->timer restarted");
		} else {
			System.out.println("----------------->timer stopped");

			if (timer != null) {
				timer.stop();
				timer = null;
			}
		}
	}

	public void actionPerformed(ActionEvent e) {
		Object src = e.getSource();

		if (src.equals(timer)) {

			System.out.println("timer action");

			FolderCommandReference[] r = new FolderCommandReference[1];
			r[0] = new FolderCommandReference(this);

			MainInterface.processor.addOp(
				new CheckForNewMessagesCommand(r));


		}
	}

	/*
	public String getDelimiter() {
		return delimiter;
	}
	
	public boolean isLogin(SwingWorker worker) throws Exception {
		if (getState() == Imap4.STATE_AUTHENTICATE)
			return true;
		else {
			// we are in Imap4.STATE_NONAUTHENTICATE
	
			login(worker);
			return false;
		}
	
	}
	
	public void clearLSubList() {
		lsubList = null;
	}
	
	public int getAccountUid() {
		return accountUid;
	}
	
	public ImapItem getImapItem() {
		return item;
	}
	
	public Imap4 getImapServerConnection() {
		return imap;
	}
	
	private boolean isSubscribed(IMAPFolder folder, Vector lsubList) {
		String folderPath = folder.getImapPath();
	
		for (int i = 0; i < lsubList.size(); i++) {
	
			String path = (String) lsubList.get(i);
	
			if (folderPath.equalsIgnoreCase(path))
				return true;
		}
	
		return false;
	}
	
	private void removeUnsubscribedFolders(Folder child) {
		if (!(child instanceof IMAPRootFolder)) {
			if (child.getChildCount() == 0) {
				Folder parent = (Folder) child.getParent();
				child.removeFromParent();
				System.out.println("folder removed:" + parent);
	
				removeUnsubscribedFolders(parent);
			}
		}
	}
	
	public void removeUnsubscribedFolders(Folder parent, Vector lsubList) {
		for (int i = 0; i < parent.getChildCount(); i++) {
			if (!(parent.getChildAt(i) instanceof IMAPFolder))
				continue;
	
			IMAPFolder child = (IMAPFolder) parent.getChildAt(i);
	
			if (child.getChildCount() == 0) {
				// if folder is not subscribed: remove folder
				if (!isSubscribed(child, lsubList)) {
					//removeUnsubscribedFolders(child);
					child.removeFromParent();
					i--;
				}
			} else
				removeUnsubscribedFolders(child, lsubList);
		}
	}
	
	private void addFolder(Folder folder, String name, Vector lsubList) {
	
		if (name.indexOf(delimiter) != -1) {
	
			String subchild = name.substring(0, name.indexOf(delimiter));
			IMAPFolder subFolder = (IMAPFolder) folder.getChild(subchild);
	
			if (subFolder == null) {
	
				// folder does not exist, create new folder
				subFolder =
					(IMAPFolder) MainInterface
						.treeModel
						.addImapFolder(
						folder,
						subchild,
						item,
						this,
						accountUid);
	
				//subFolder.getFolderItem().setMessageFolder("false");
			}
	
			addFolder(
				subFolder,
				name.substring(name.indexOf(delimiter) + 1, name.length()),
				lsubList);
		} else {
			if (folder.getChild(name) == null)
				MainInterface.treeModel.addImapFolder(
					folder,
					name,
					item,
					this,
					accountUid);
		}
	
	}
	*/

	/*
	private void addSubscribedFolders(Vector lsubList) throws Exception {
		boolean inbox = false;
		boolean answer;
	
		for (int i = 0; i < lsubList.size(); i++) {
			String name = (String) lsubList.get(i);
	
			if (name.toLowerCase().equalsIgnoreCase("inbox"))
				inbox = true;
	
			addFolder(this, name, lsubList);
	
		}
	
		if (inbox == false) {
			answer = imap.flist("", "");
			//System.out.println("trying to parse flist");
			String str = imap.getResult();
			//System.out.println("str: "+ str );
			str = str.toLowerCase();
			//System.out.println("str: "+ str );
	
			if (str.indexOf("inbox") != -1) {
				Folder childFolder = (Folder) getChild("Inbox");
				if (childFolder == null) {
					MainInterface.treeModel.addImapFolder(
						this,
						"Inbox",
						item,
						this,
						accountUid);
				}
			} else {
				System.out.println("string inbox not found");
			}
		}
	}
	
	protected void lsubsub(Vector lsubList, Vector v) throws Exception {
	
		//lsubList.addAll(v);
	
		for (int i = 0; i < v.size(); i++) {
			String path = (String) v.get(i);
			lsubList.add(path);
	
			System.out.println("path:" + path);
	
			boolean answer = imap.flsub(path + "/*");
	
			if (imap.getResult().length() > 0) {
				Vector temp = Imap4Parser.parseLsub(imap.getResult());
	
				if (temp.size() > 0)
					lsubsub(lsubList, temp);
			}
	
		}
	
	}
	*/

	/**
	 *
	 * this method is called to get the initial folder-list
	 *  ( this happens when doppel-clicking the imap root-folder
	 * */

	/*
	public void lsub(SwingWorker worker) throws Exception {
	
		boolean answer;
		lsubList = new Vector();
		Vector v = new Vector();
	
		getImapServerConnection().setState(Imap4.STATE_NONAUTHENTICATE);
	
		isLogin(worker);
	
		if (worker != null)
			worker.setText("Retrieve folder listing...");
	
		if (worker != null)
			worker.startTimer();
	
		answer = imap.flist("", "");
	
		String result = imap.getResult();
	
		delimiter = Imap4Parser.parseDelimiter(result);
		System.out.println("delimiter=" + delimiter);
	
		answer = imap.flsub("*");
		//System.out.println("trying to parse lsub");
		String result2 = imap.getResult();
		System.out.println("--------->result:\n" + result2);
	
		lsubList = Imap4Parser.parseLsub(result2);
	
		//lsubsub(lsubList, v);
	
		v = new Vector();
		v.add("INBOX");
	
		lsubsub(lsubList, v);
	
		// add subscribed folders
	
		addSubscribedFolders(lsubList);
	
		// remove unsubscribed folders
	
		removeUnsubscribedFolders(this, lsubList);
	
		if (worker != null)
			worker.stopTimer();
	
	}
	*/

	/**
	 *
	 *  this method is called by the subscribe/unsubscribe dialog
	 *
	 *
	 **/
	/*
	public Vector getLSubList() throws Exception {
		if (lsubList != null)
			return lsubList;
	
		boolean answer;
		lsubList = new Vector();
	
		try {
	
			answer = imap.flsub("*");
			//System.out.println("trying to parse lsub");
	
			lsubList = Imap4Parser.parseLsub(imap.getResult());
	
		} catch (Exception ex) {
			//System.out.println("imapfolder->lsub: "+ ex.getMessage() );
			throw new Exception(
				"IMAPRootFolder->getLSubList: " + ex.getMessage());
		}
	
		return lsubList;
	}
	
	public boolean subscribe(String path) throws Exception {
		boolean answer = false;
	
		try {
			answer = imap.subscribe(path);
		} catch (Exception ex) {
			//System.out.println("imapfolder->lsub: "+ ex.getMessage() );
			throw new Exception(
				"IMAPRootFolder->subscribe: " + ex.getMessage());
		}
	
		return answer;
	}
	
	public boolean unsubscribe(String path) throws Exception {
		boolean answer = false;
		try {
			answer = imap.unsubscribe(path);
		} catch (Exception ex) {
			//System.out.println("imapfolder->lsub: "+ ex.getMessage() );
			throw new Exception(
				"IMAPRootFolder->unsubscribe: " + ex.getMessage());
		}
	
		return answer;
	}
	*/
	/**
	 *
	 *  this method is called by the subscribe/unsubscribe dialog
	 *
	 *
	 **/
	/*
	public Vector getList(SubscribeTreeNode treeNode) throws Exception {
		StringBuffer buf = new StringBuffer(treeNode.getName());
		SubscribeTreeNode node = (SubscribeTreeNode) treeNode.getParent();
		while (node != null) {
			if (node.getName().equals("root"))
				break;
	
			buf.insert(0, node.getName() + "/");
			node = (SubscribeTreeNode) node.getParent();
		}
	
		String name = buf.toString();
	
		Vector v = new Vector();
	
		boolean answer;
	
		try {
	
			answer = imap.flist(name.trim() + "/%", "");
			//System.out.println("trying to parse list");
			v = Imap4Parser.parseList(imap.getResult());
	
		} catch (Exception ex) {
			//System.out.println("imapfolder->lsub: "+ ex.getMessage() );
			throw new Exception("IMAPRootFolder->getList: " + ex.getMessage());
		}
	
		return v;
	}
	*/
	/**
	 *
	 *  this method is called by the subscribe/unsubscribe dialog
	 *
	 *
	 **/

	/*
	public Vector getList() throws Exception {
		Vector v = new Vector();
	
		boolean answer;
	
		isLogin(null);
	
		answer = imap.flist("*", "");
		// System.out.println("trying to parse list");
		v = Imap4Parser.parseList(imap.getResult());
	
		return v;
	}
	*/

	/*
	public boolean addFolder(String path) throws Exception {
		isLogin(null);
	
		boolean b = imap.create(path);
	
		if (b == false) {
			throw new ImapException(imap.getResult());
		}
	
		return b;
	}
	*/

	/**
	 * @see org.columba.mail.folder.FolderTreeNode#getDefaultProperties()
	 */
	public static XmlElement getDefaultProperties() {
		XmlElement props = new XmlElement("property");
		props.addAttribute("accessrights", "system");
		props.addAttribute("subfolder", "true");

		return props;
	}

	/**
	 * @see org.columba.mail.folder.Folder#addMessage(org.columba.mail.message.AbstractMessage, org.columba.core.command.WorkerStatusController)
	 */
	public Object addMessage(
		AbstractMessage message,
		WorkerStatusController worker)
		throws Exception {
		return null;
	}

	/**
	 * @see org.columba.mail.folder.Folder#addMessage(java.lang.String, org.columba.core.command.WorkerStatusController)
	 */
	public Object addMessage(String source, WorkerStatusController worker)
		throws Exception {
		return null;
	}

	/**
	 * @see org.columba.mail.folder.Folder#exists(java.lang.Object, org.columba.core.command.WorkerStatusController)
	 */
	public boolean exists(Object uid, WorkerStatusController worker)
		throws Exception {
		return false;
	}

	/**
	 * @see org.columba.mail.folder.Folder#expungeFolder(java.lang.Object, org.columba.core.command.WorkerStatusController)
	 */
	public void expungeFolder(Object[] uids, WorkerStatusController worker)
		throws Exception {
	}

	/**
	 * @see org.columba.mail.folder.Folder#getHeaderList(org.columba.core.command.WorkerStatusController)
	 */
	public HeaderList getHeaderList(WorkerStatusController worker)
		throws Exception {
		return null;
	}

	/**
	 * @see org.columba.mail.folder.Folder#getMessageHeader(java.lang.Object, org.columba.core.command.WorkerStatusController)
	 */
	public ColumbaHeader getMessageHeader(
		Object uid,
		WorkerStatusController worker)
		throws Exception {
		return null;
	}

	/**
	 * @see org.columba.mail.folder.Folder#getMessageSource(java.lang.Object, org.columba.core.command.WorkerStatusController)
	 */
	public String getMessageSource(Object uid, WorkerStatusController worker)
		throws Exception {
		return null;
	}

	/**
	 * @see org.columba.mail.folder.Folder#getMimePart(java.lang.Object, java.lang.Integer, org.columba.core.command.WorkerStatusController)
	 */
	public MimePart getMimePart(
		Object uid,
		Integer[] address,
		WorkerStatusController worker)
		throws Exception {
		return null;
	}

	/**
	 * @see org.columba.mail.folder.Folder#getMimePartTree(java.lang.Object, org.columba.core.command.WorkerStatusController)
	 */
	public MimePartTree getMimePartTree(
		Object uid,
		WorkerStatusController worker)
		throws Exception {
		return null;
	}

	/**
	 * @see org.columba.mail.folder.Folder#markMessage(java.lang.Object, int, org.columba.core.command.WorkerStatusController)
	 */
	public void markMessage(
		Object[] uids,
		int variant,
		WorkerStatusController worker)
		throws Exception {
	}

	/**
	 * @see org.columba.mail.folder.Folder#removeMessage(java.lang.Object, org.columba.core.command.WorkerStatusController)
	 */
	public void removeMessage(Object uid, WorkerStatusController worker)
		throws Exception {
	}

	/**
	 * @see org.columba.mail.folder.Folder#searchMessages(org.columba.mail.filter.Filter, java.lang.Object, org.columba.core.command.WorkerStatusController)
	 */
	public Object[] searchMessages(
		Filter filter,
		Object[] uids,
		WorkerStatusController worker)
		throws Exception {
		return null;
	}

	/**
	 * @see org.columba.mail.folder.Folder#searchMessages(org.columba.mail.filter.Filter, org.columba.core.command.WorkerStatusController)
	 */
	public Object[] searchMessages(
		Filter filter,
		WorkerStatusController worker)
		throws Exception {
		return null;
	}

	/**
	 * @return
	 */
	public AccountItem getAccountItem() {
		return accountItem;
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
	scala.collection.parallel.AdaptiveWorkStealingTasks$AWSTWrappedTask.internal(Tasks.scala:159)
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

QDox parse error in file://<WORKSPACE>/data/code-rep-dataset/Dataset2/Tasks/5870.java