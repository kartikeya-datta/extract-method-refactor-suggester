error id: file://<WORKSPACE>/data/code-rep-dataset/Dataset2/Tasks/3191.java
file://<WORKSPACE>/data/code-rep-dataset/Dataset2/Tasks/3191.java
### com.thoughtworks.qdox.parser.ParseException: syntax error @[1,1]

error in qdox parser
file content:
```java
offset: 1
uri: file://<WORKSPACE>/data/code-rep-dataset/Dataset2/Tasks/3191.java
text:
```scala
f@@older.setNextMessageUid(nextUid);

package org.columba.mail.folder;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.util.Enumeration;

import org.columba.core.command.WorkerStatusController;
import org.columba.core.logging.ColumbaLogger;
import org.columba.mail.message.ColumbaHeader;
import org.columba.mail.message.HeaderInterface;
import org.columba.mail.message.HeaderList;

/**
 * @author freddy
 *
 * To change this generated comment edit the template variable "typecomment":
 * Window>Preferences>Java>Templates.
 * To enable and disable the creation of type comments go to
 * Window>Preferences>Java>Code Generation.
 */
public abstract class AbstractHeaderCache {

	protected HeaderList headerList;

	protected File headerFile;

	protected boolean headerCacheAlreadyLoaded;

	protected LocalFolder folder;

	public AbstractHeaderCache(LocalFolder folder) {
		this.folder = folder;

		headerFile = new File(folder.getDirectoryFile(), ".header");

		headerList = new HeaderList();

		headerCacheAlreadyLoaded = false;
	}
	
	public HeaderInterface createHeaderInstance()
	{
		return new ColumbaHeader();
	}

	public boolean isHeaderCacheAlreadyLoaded() {
		return headerCacheAlreadyLoaded;
	}

	public boolean exists(Object uid) throws Exception {
		return headerList.contains(uid);
	}

	public int count() {
		return headerList.size();
	}

	public void remove(Object uid) throws Exception {
		ColumbaLogger.log.debug("trying to remove message UID=" + uid);

		if (headerList.containsKey(uid)) {
			ColumbaLogger.log.debug("remove UID=" + uid);

			headerList.remove(uid);
		}
	}

	public void add(HeaderInterface header) throws Exception {
		headerList.add(header, header.get("columba.uid"));
	}

	public HeaderList getHeaderList(WorkerStatusController worker)
		throws Exception {
		// if there exists a ".header" cache-file
		//  try to load the cache	
		if (headerCacheAlreadyLoaded == false) {
			try {
				if (headerFile.exists()) {
					load(worker);
					headerCacheAlreadyLoaded = true;
				} else {
					headerList =
						folder.getDataStorageInstance().recreateHeaderList(
							worker);
					headerCacheAlreadyLoaded = true;
				}
			} catch (Exception ex) {
				ex.printStackTrace();

				headerList =
					folder.getDataStorageInstance().recreateHeaderList(worker);

				headerCacheAlreadyLoaded = true;

			}

		}

		//System.out.println("headerList=" + headerList);

		return headerList;
	}

	public void load(WorkerStatusController worker) throws Exception {

		ColumbaLogger.log.info("loading header-cache=" + headerFile);

		FileInputStream istream = new FileInputStream(headerFile.getPath());
		ObjectInputStream p = new ObjectInputStream(istream);

		int capacity = p.readInt();
		ColumbaLogger.log.info("capacity=" + capacity);

		if (capacity != folder.getDataStorageInstance().getMessageCount()) {
			// messagebox headercache-file is corrupted

			headerList =
				folder.getDataStorageInstance().recreateHeaderList(worker);
			return;
		}

		headerList = new HeaderList(capacity);
		Integer uid;

		//System.out.println("Number of Messages : " + capacity);

		worker.setDisplayText("Loading headers from cache...");
		
		if (worker != null)
			worker.setProgressBarMaximum(capacity);

		int nextUid = -1;

		for (int i = 1; i <= capacity; i++) {
			if (worker != null)
				worker.setProgressBarValue(i);

			//ColumbaHeader h = message.getHeader();
			HeaderInterface h = createHeaderInstance();

			/*
			// read current number of message
			p.readInt();
			*/

			loadHeader(p, h);

			//System.out.println("message=" + h.get("subject"));

			headerList.add(h, (Integer) h.get("columba.uid"));

			if ( h.get("columba.flags.recent").equals(Boolean.TRUE) ) folder.getMessageFolderInfo().incRecent();
			if ( h.get("columba.flags.seen").equals(Boolean.FALSE)  ) folder.getMessageFolderInfo().incUnseen();
			folder.getMessageFolderInfo().incExists();
		
			int aktUid = ((Integer) h.get("columba.uid")).intValue();
			if (nextUid < aktUid)
				nextUid = aktUid;

		}

		nextUid++;
		ColumbaLogger.log.debug("next UID for new messages =" + nextUid);
		folder.setNextUid(nextUid);

		// close stream
		p.close();
	}

	public void save(WorkerStatusController worker) throws Exception {

		// we didn't load any header to save
		if (isHeaderCacheAlreadyLoaded() == false)
			return;

		ColumbaLogger.log.info("saveing header-cache=" + headerFile);
		// this has to called only if the uid becomes higher than Integer allows
		//cleanUpIndex();

		//System.out.println("saving headerfile: "+ headerFile.toString() );

		FileOutputStream istream = new FileOutputStream(headerFile.getPath());
		ObjectOutputStream p = new ObjectOutputStream(istream);

		//int count = getMessageFileCount();
		int count = headerList.count();
		ColumbaLogger.log.info("capacity=" + count);
		p.writeInt(count);

		ColumbaHeader h;
		//Message message;

		int i = 0;
		for (Enumeration e = headerList.keys(); e.hasMoreElements();) {
			Object uid = e.nextElement();

			h = (ColumbaHeader) headerList.getHeader(uid);

			if (h != null)
				saveHeader(p, h);
		}
		/*
		for (int i = 0; i < count; i++) {
			p.writeInt(i + 1);
		
			h = headerList.getHeader(new Integer(i));
		
			saveHeader(p, h);
		
		}
		*/
		//p.flush();
		p.close();
	}

	protected abstract void loadHeader(ObjectInputStream p, HeaderInterface h)
		throws Exception;

	protected abstract void saveHeader(ObjectOutputStream p, HeaderInterface h)
		throws Exception;

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

QDox parse error in file://<WORKSPACE>/data/code-rep-dataset/Dataset2/Tasks/3191.java