error id: file://<WORKSPACE>/data/code-rep-dataset/Dataset2/Tasks/383.java
file://<WORKSPACE>/data/code-rep-dataset/Dataset2/Tasks/383.java
### com.thoughtworks.qdox.parser.ParseException: syntax error @[1,1]

error in qdox parser
file content:
```java
offset: 1
uri: file://<WORKSPACE>/data/code-rep-dataset/Dataset2/Tasks/383.java
text:
```scala
m@@essageList.put(newUid, new ColumbaMessage(h, message));

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
package org.columba.mail.folder.temp;

import java.io.File;
import java.io.InputStream;
import java.util.Enumeration;
import java.util.Hashtable;

import org.columba.core.command.StatusObservableImpl;
import org.columba.core.config.ConfigPath;
import org.columba.core.io.DiskIO;
import org.columba.core.io.StreamUtils;
import org.columba.core.logging.ColumbaLogger;
import org.columba.mail.filter.Filter;
import org.columba.mail.folder.DataStorageInterface;
import org.columba.mail.folder.Folder;
import org.columba.mail.folder.search.AbstractSearchEngine;
import org.columba.mail.folder.search.DefaultSearchEngine;
import org.columba.mail.message.ColumbaHeader;
import org.columba.mail.message.ColumbaMessage;
import org.columba.mail.message.HeaderList;
import org.columba.ristretto.message.Flags;
import org.columba.ristretto.message.Header;
import org.columba.ristretto.message.LocalMimePart;
import org.columba.ristretto.message.Message;
import org.columba.ristretto.message.MessageFolderInfo;
import org.columba.ristretto.message.MimePart;
import org.columba.ristretto.message.MimeTree;
import org.columba.ristretto.message.io.CharSequenceSource;
import org.columba.ristretto.message.io.SourceInputStream;
import org.columba.ristretto.parser.MessageParser;

/**
 * @author freddy
 *
 * To change this generated comment edit the template variable "typecomment":
 * Window>Preferences>Java>Templates.
 * To enable and disable the creation of type comments go to
 * Window>Preferences>Java>Code Generation.
 */
public class TempFolder extends Folder {

	protected HeaderList headerList;
	protected Hashtable messageList;

	protected int nextUid;
	protected ColumbaMessage aktMessage;
	protected DataStorageInterface dataStorage;
	protected DefaultSearchEngine searchEngine;

	/**
	 * Constructor for TempFolder.
	 * @param name
	 */
	public TempFolder() {
		super();

		// FIXME: why is this needed?
		// children is already initialised by DefaultMutableTreeNode
		//children = new Vector();

		messageFolderInfo = new MessageFolderInfo();

		changed = false;
				
		String dir = ConfigPath.getConfigDirectory() + "/mail/" + "temp";
		if (DiskIO.ensureDirectory(dir))
			directoryFile = new File(dir);
			
		observable = new StatusObservableImpl();

		headerList = new HeaderList();
		messageList = new Hashtable();

		nextUid = 0;
	}

	public void clear() {
		headerList.clear();
		messageList.clear();
	}
	
	public void expungeFolder() throws Exception {
		Object[] uids = getUids();
		
		for (int i = 0; i < uids.length; i++) {
			Object uid = uids[i];

			ColumbaHeader h = getMessageHeader(uid);
			Boolean expunged = (Boolean) h.get("columba.flags.expunged");

			//ColumbaLogger.log.debug("expunged=" + expunged);

			if (expunged.equals(Boolean.TRUE)) {
				// move message to trash

				ColumbaLogger.log.info(
					"moving message with UID " + uid + " to trash");

				// remove message
				removeMessage(uid);

			}
		}
	}
	

	protected Object generateNextUid() {
		return new Integer(nextUid++);
	}

	public void setNextUid(int next) {
		nextUid = next;
	}

	

	/**
	 * @see org.columba.modules.mail.folder.Folder#addMessage(AbstractMessage, WorkerStatusController)
	 */
	public Object addMessage(
		ColumbaMessage message)
		throws Exception {
		
		Object newUid = generateNextUid();

		ColumbaLogger.log.debug("new UID=" + newUid);

		ColumbaHeader h =
			(ColumbaHeader) ((ColumbaHeader) message.getHeaderInterface());

		h.set("columba.uid", newUid);

		headerList.add(h, newUid);

		messageList.put(newUid, message);

		return newUid;
		
	}

	/**
	 * @see org.columba.modules.mail.folder.Folder#addMessage(String, WorkerStatusController)
	 */
	public Object addMessage(String source)
		throws Exception {
		return null;
	}

	/**
	 * @see org.columba.modules.mail.folder.Folder#exists(Object)
	 */
	public boolean exists(Object uid) throws Exception {
		return messageList.containsKey(uid);
	}

	/**
	 * @see org.columba.modules.mail.folder.Folder#getHeaderList(WorkerStatusController)
	 */
	public HeaderList getHeaderList()
		throws Exception {
		return headerList;
	}

	/**
	 * @see org.columba.modules.mail.folder.Folder#markMessage(Object[], int, WorkerStatusController)
	 */
	public void markMessage(
		Object[] uids,
		int variant)
		throws Exception {
	}

	/**
	 * @see org.columba.modules.mail.folder.Folder#removeMessage(Object)
	 */
	public void removeMessage(Object uid) throws Exception {
		headerList.remove(uid);
		messageList.remove(uid);
	}

	/**
	 * @see org.columba.modules.mail.folder.Folder#getMimePart(Object, Integer[], WorkerStatusController)
	 */
	public MimePart getMimePart(
		Object uid,
		Integer[] address)
		throws Exception {
		ColumbaMessage message = (ColumbaMessage) messageList.get(uid);

		MimePart mimePart = message.getMimePartTree().getFromAddress(address);

		return mimePart;
	}

	/**
	 * @see org.columba.modules.mail.folder.Folder#getMessageSource(Object, WorkerStatusController)
	 */
	public String getMessageSource(Object uid)
		throws Exception {

		ColumbaMessage message = getMessage(uid);
		if (message == null) {
			System.out.println("no message for uid=" + uid);
			System.out.println("list-count=" + headerList.count());
			System.out.println("message-count=" + messageList.size());
			for (Enumeration e = messageList.keys(); e.hasMoreElements();) {
				System.out.println(e.nextElement());

			}

		}

		return message.getStringSource();
	}

	/**
	 * @see org.columba.modules.mail.folder.Folder#getMimePartTree(Object, WorkerStatusController)
	 */
	public MimeTree getMimePartTree(
		Object uid)
		throws Exception {
		return ((ColumbaMessage)messageList.get(uid)).getMimePartTree();
	}

	/**
	 * @see org.columba.modules.mail.folder.Folder#getMessageHeader(Object, WorkerStatusController)
	 */
	public ColumbaHeader getMessageHeader(
		Object uid)
		throws Exception {

		ColumbaHeader header = (ColumbaHeader) headerList.get(uid);

		return header;
	}

	/**
	 * @see org.columba.modules.mail.folder.Folder#getMessage(Object, WorkerStatusController)
	 */
	public ColumbaMessage getMessage(
		Object uid)
		throws Exception {
		ColumbaMessage message = (ColumbaMessage) messageList.get(uid);

		return message;
	}

	public AbstractSearchEngine getSearchEngineInstance() {
		if (searchEngine == null)
			searchEngine = new DefaultSearchEngine(this);

		return searchEngine;
	}

	/**
	 * @see org.columba.modules.mail.folder.Folder#searchMessages(Filter, Object[], WorkerStatusController)
	 */
	public Object[] searchMessages(
		Filter filter,
		Object[] uids)
		throws Exception {
		return getSearchEngineInstance().searchMessages(filter, uids);
	}

	/**
	 * @see org.columba.modules.mail.folder.Folder#searchMessages(Filter, WorkerStatusController)
	 */
	public Object[] searchMessages(
		Filter filter)
		throws Exception {
		return getSearchEngineInstance().searchMessages(filter);
	}

	/**
	 * @see org.columba.modules.mail.folder.FolderTreeNode#instanceNewChildNode(AdapterNode, FolderItem)
	 */
	public String getDefaultChild() {
		return null;
	}

	public String getName() {
		return toString();
	}

	public String toString() {
		return (String) getUserObject();
	}

	/* (non-Javadoc)
	 * @see org.columba.mail.folder.MailboxInterface#addMessage(java.io.InputStream)
	 */
	public Object addMessage(InputStream in) throws Exception {
		StringBuffer source = StreamUtils.readInString( in );
		Message message = MessageParser.parse( new CharSequenceSource( source ) );

		Object newUid = generateNextUid();

		ColumbaLogger.log.debug("new UID=" + newUid);

		ColumbaHeader h = new ColumbaHeader( message.getHeader() );
		h.set("columba.uid", newUid);

		headerList.add(h, newUid);

		messageList.put(newUid, message);

		return newUid;
	}

	/* (non-Javadoc)
	 * @see org.columba.mail.folder.MailboxInterface#getAttribute(java.lang.Object, java.lang.String)
	 */
	public Object getAttribute(Object uid, String key) throws Exception {
		return ((ColumbaHeader)headerList.getHeader(uid)).getAttributes().get(key);
	}

	/* (non-Javadoc)
	 * @see org.columba.mail.folder.MailboxInterface#getFlags(java.lang.Object)
	 */
	public Flags getFlags(Object uid) throws Exception {
		return ((ColumbaHeader)headerList.getHeader(uid)).getFlags();
	}

	/* (non-Javadoc)
	 * @see org.columba.mail.folder.MailboxInterface#getHeaderFields(java.lang.Object, java.lang.String[])
	 */
	public Header getHeaderFields(Object uid, String[] keys) throws Exception {
		Header header = ((Message)messageList.get(uid)).getHeader();
		
		Header subHeader = new Header();
		String value;
		for( int i=0; i<keys.length; i++) {
			value = header.get(keys[i]);
			if( value != null) {
				subHeader.set(keys[i], value);
			}
		}
		
		return subHeader;
	}

	/* (non-Javadoc)
	 * @see org.columba.mail.folder.MailboxInterface#getMessageSourceStream(java.lang.Object)
	 */
	public InputStream getMessageSourceStream(Object uid) throws Exception {
		return new SourceInputStream( ((Message)messageList.get(uid)).getSource() );
	}

	/* (non-Javadoc)
	 * @see org.columba.mail.folder.MailboxInterface#getMimePartBodyStream(java.lang.Object, java.lang.Integer[])
	 */
	public InputStream getMimePartBodyStream(Object uid, Integer[] address)
		throws Exception {
			Message message = (Message)messageList.get(uid);
			
			LocalMimePart mimepart = (LocalMimePart) message.getMimePartTree().getFromAddress(address);
			
		return mimepart.getInputStream();
	}

	/* (non-Javadoc)
	 * @see org.columba.mail.folder.MailboxInterface#getMimePartSourceStream(java.lang.Object, java.lang.Integer[])
	 */
	public InputStream getMimePartSourceStream(Object uid, Integer[] address)
		throws Exception {
			Message message = (Message)messageList.get(uid);
			
			LocalMimePart mimepart = (LocalMimePart) message.getMimePartTree().getFromAddress(address);
			
			return new SourceInputStream( mimepart.getSource() );
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

QDox parse error in file://<WORKSPACE>/data/code-rep-dataset/Dataset2/Tasks/383.java