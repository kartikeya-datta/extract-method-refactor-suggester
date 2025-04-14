error id: file://<WORKSPACE>/data/code-rep-dataset/Dataset2/Tasks/8752.java
file://<WORKSPACE>/data/code-rep-dataset/Dataset2/Tasks/8752.java
### com.thoughtworks.qdox.parser.ParseException: syntax error @[1,1]

error in qdox parser
file content:
```java
offset: 1
uri: file://<WORKSPACE>/data/code-rep-dataset/Dataset2/Tasks/8752.java
text:
```scala
L@@OG.severe("Error loading local header cache!");

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
package org.columba.mail.folder.headercache;

import java.io.File;
import java.io.IOException;
import java.util.Calendar;
import java.util.Date;
import java.util.Enumeration;
import java.util.logging.Logger;

import org.columba.core.command.StatusObservable;
import org.columba.core.main.Main;
import org.columba.mail.folder.AbstractLocalFolder;
import org.columba.mail.folder.AbstractMessageFolder;
import org.columba.mail.folder.IDataStorage;
import org.columba.mail.message.ColumbaHeader;
import org.columba.mail.message.HeaderList;
import org.columba.mail.message.IColumbaHeader;
import org.columba.mail.util.MailResourceLoader;
import org.columba.ristretto.io.Source;
import org.columba.ristretto.message.Flags;
import org.columba.ristretto.message.MailboxInfo;
import org.columba.ristretto.parser.HeaderParser;

/**
 * Implementation of a local headercache facility, which is also able to resync
 * itself with the {@IDataStorage}.
 * 
 * @author fdietz
 */
public class LocalHeaderCache extends AbstractHeaderCache {

	protected AbstractMessageFolder folder;

	/** JDK 1.4+ logging framework logger, used for logging. */
	private static final Logger LOG = Logger
			.getLogger("org.columba.mail.folder.headercache");

	private static final int WEEK = 1000 * 60 * 60 * 24 * 7;

	private boolean configurationChanged;

	public LocalHeaderCache(AbstractLocalFolder folder) {
		super(new File(folder.getDirectoryFile(), ".header"));

		this.folder = folder;

		configurationChanged = false;
	}

	public StatusObservable getObservable() {
		return folder.getObservable();
	}

	public HeaderList getHeaderList() throws IOException {
		boolean needToRelease = false;

		// if there exists a ".header" cache-file
		//  try to load the cache
		if (!isHeaderCacheLoaded()) {
			if (headerFile.exists()) {
				try {
					load();

					if (needToSync(headerList.count())) {
						sync();
					}
				} catch (Exception e) {
					sync();
				}
			} else {
				sync();
			}

			setHeaderCacheLoaded(true);
		}

		return headerList;
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see org.columba.mail.folder.headercache.AbstractHeaderCache#needToSync(int)
	 */
	public boolean needToSync(int capacity) {
		int mcount = ((AbstractLocalFolder) folder).getDataStorageInstance()
				.getMessageCount();

		if (capacity != mcount) {
			return true;
		}

		return false;
	}

	/**
	 * @param worker
	 * @throws Exception
	 */
	public void load() throws Exception {
		LOG.fine("loading header-cache=" + headerFile);

		try {
			reader = new ObjectReader(headerFile);

			int capacity = ((Integer) reader.readObject()).intValue();
			LOG.fine("capacity=" + capacity);

			boolean needToRelease = false;

			headerList = new HeaderList(capacity);

			//System.out.println("Number of Messages : " + capacity);
			if (getObservable() != null) {
				getObservable().setMessage(
						folder.getName()
								+ ": "
								+ MailResourceLoader.getString("statusbar",
										"message", "load_headers"));
				getObservable().setMax(capacity);
				getObservable().resetCurrent(); // setCurrent(0)
			}

			int nextUid = -1;

			// exists/unread/recent should be set to 0
			folder.setMessageFolderInfo(new MailboxInfo());

			for (int i = 0; i < capacity; i++) {
				if (getObservable() != null) {
					getObservable().setCurrent(i);
				}

				ColumbaHeader h = createHeaderInstance();

				loadHeader(h);

				headerList.add(h, (Integer) h.get("columba.uid"));

				if (h.getFlags().getRecent()) {
					// no recent messages should exist on startup
					// --> remove recent flag
					h.getFlags().setRecent(false);
					//folder.getMessageFolderInfo().incRecent();
				}

				if (!h.getFlags().getSeen()) {
					folder.getMessageFolderInfo().incUnseen();
				}

				folder.getMessageFolderInfo().incExists();

				int aktUid = ((Integer) h.get("columba.uid")).intValue();

				if (nextUid < aktUid) {
					nextUid = aktUid;
				}
			}

			/*
			 * // Check if the count of the if (needToSync(capacity)) {
			 * ColumbaLogger.log.fine( "need to recreateHeaderList() because
			 * capacity is not matching");
			 * 
			 * throw new FolderInconsistentException(); }
			 */
			nextUid++;
			LOG.info("next UID for new messages =" + nextUid);
			((AbstractLocalFolder) folder).setNextMessageUid(nextUid);

		} catch (Exception e) {
			LOG.severe(e.getMessage());

			if (Main.DEBUG) {
				e.printStackTrace();
			}
		} finally {
			reader.close();
		}

	}

	/**
	 * @param worker
	 * @throws Exception
	 */
	public void save() throws Exception {
		// we didn't load any header to save
		if (!isHeaderCacheLoaded()) {
			return;
		}

		LOG.fine("saving header-cache=" + headerFile);

		// this has to called only if the uid becomes higher than Integer
		// allows
		//cleanUpIndex();
		try {
			writer = new ObjectWriter(headerFile);
		} catch (Exception e) {
			if (Main.DEBUG) {
				e.printStackTrace();
			}
		}

		// write total number of headers to file
		int count = headerList.count();
		LOG.fine("capacity=" + count);
		writer.writeObject(new Integer(count));

		ColumbaHeader h;

		//Message message;
		for (Enumeration e = headerList.keys(); e.hasMoreElements();) {
			Object uid = e.nextElement();

			h = (ColumbaHeader) headerList.get(uid);

			saveHeader(h);
		}

		writer.close();
	}

	/**
	 * @param worker
	 * @throws Exception
	 */
	private void sync() throws IOException {
		if (getObservable() != null) {
			getObservable().setMessage(
					folder.getName() + ": Syncing headercache...");
		}

		IDataStorage ds = ((AbstractLocalFolder) folder)
				.getDataStorageInstance();

		Object[] uids = ds.getMessageUids();

		HeaderList oldHeaderList = headerList;

		headerList = new HeaderList(uids.length);

		Date today = Calendar.getInstance().getTime();

		// parse all message files to recreate the header cache
		IColumbaHeader header = null;
		MailboxInfo messageFolderInfo = folder.getMessageFolderInfo();
		messageFolderInfo.setExists(0);
		messageFolderInfo.setRecent(0);
		messageFolderInfo.setUnseen(0);

		folder.setChanged(true);

		if (getObservable() != null) {
			getObservable().setMax(uids.length);
			getObservable().resetCurrent();
		}

		for (int i = 0; i < uids.length; i++) {
			if ((getObservable() != null) && ((i % 100) == 0)) {
				getObservable().setCurrent(i);
			}

			if ((oldHeaderList != null) && oldHeaderList.containsKey(uids[i])) {
				header = oldHeaderList.get(uids[i]);
				headerList.add(header, uids[i]);
			} else {
				try {
					Source source = ds.getMessageSource(uids[i]);

					if (source.length() == 0) {
						ds.removeMessage(uids[i]);

						continue;
					}

					header = new ColumbaHeader(HeaderParser.parse(source));

					// make sure that we have a Message-ID
					String messageID = (String) header.get("Message-Id");
					if (messageID != null)
						header.set("Message-ID", header.get("Message-Id"));

					header = CachedHeaderfields.stripHeaders(header);

					if (isOlderThanOneWeek(today, ((Date) header
							.getAttributes().get("columba.date")))) {
						header.getFlags().set(Flags.SEEN);
					}

					// message size should be at least 1 KB
					int size = Math.max(source.length() / 1024, 1);
					header.getAttributes().put("columba.size",
							new Integer(size));

					// set the attachment flag
					String contentType = (String) header.get("Content-Type");

					header.getAttributes().put("columba.attachment",
							header.hasAttachments());

					header.getAttributes().put("columba.uid", uids[i]);

					headerList.add(header, uids[i]);
					source.close();
					source = null;
				} catch (Exception ex) {
					ex.printStackTrace();
					LOG.severe("Error syncing HeaderCache :"
							+ ex.getLocalizedMessage());
				}
			}

			if (header.getFlags().getRecent()) {
				messageFolderInfo.incRecent();
			}

			if (!header.getFlags().getSeen()) {
				messageFolderInfo.incUnseen();
			}

			header = null;

			messageFolderInfo.incExists();

			((AbstractLocalFolder) folder)
					.setNextMessageUid(((Integer) uids[uids.length - 1])
							.intValue() + 1);

			if ((getObservable() != null) && ((i % 100) == 0)) {
				getObservable().setCurrent(i);
			}
		}

		// we are done
		if (getObservable() != null) {
			getObservable().resetCurrent();
		}
	}

	protected void loadHeader(ColumbaHeader h) throws Exception {
		h.getAttributes().put("columba.uid", new Integer(reader.readInt()));

		super.loadHeader(h);
	}

	protected void saveHeader(ColumbaHeader h) throws Exception {
		writer.writeInt(((Integer) h.getAttributes().get("columba.uid"))
				.intValue());

		super.saveHeader(h);
	}

	private boolean isOlderThanOneWeek(Date arg0, Date arg1) {
		return (arg0.getTime() - WEEK) > arg1.getTime();
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

QDox parse error in file://<WORKSPACE>/data/code-rep-dataset/Dataset2/Tasks/8752.java