error id: file://<WORKSPACE>/data/code-rep-dataset/Dataset2/Tasks/6620.java
file://<WORKSPACE>/data/code-rep-dataset/Dataset2/Tasks/6620.java
### com.thoughtworks.qdox.parser.ParseException: syntax error @[1,1]

error in qdox parser
file content:
```java
offset: 1
uri: file://<WORKSPACE>/data/code-rep-dataset/Dataset2/Tasks/6620.java
text:
```scala
g@@etObservable().clearMessageWithDelay();

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

import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.util.Enumeration;

import org.columba.core.logging.ColumbaLogger;
import org.columba.mail.folder.Folder;
import org.columba.mail.message.ColumbaHeader;
import org.columba.mail.message.HeaderList;
import org.columba.mail.util.MailResourceLoader;
import org.columba.ristretto.message.HeaderInterface;

/**
 * IMAP-specific implementation of a header cache.
 * 
 *
 * @author fdietz
 */
public class RemoteHeaderCache extends AbstractFolderHeaderCache {

	/**
	 * Constructor for RemoteHeaderCache.
	 * @param folder
	 */
	public RemoteHeaderCache(Folder folder) {
		super(folder);
	}

	public void load() throws Exception {
		ColumbaLogger.log.info("loading header-cache=" + headerFile);
		headerList = new HeaderList();

		ObjectInputStream p = openInputStream();
		int capacity = 0 ;
		
		if (p.available() > 0)
			capacity = p.readInt();
		
		ColumbaLogger.log.info("capacity=" + capacity);

		if (getObservable() != null) {
			getObservable().setMessage(
				MailResourceLoader.getString(
					"statusbar",
					"message",
					"load_headers"));
			getObservable().setMax(capacity);
			getObservable().resetCurrent();
		}

		for (int i = 1; i <= capacity; i++) {
			if (getObservable() != null)
				getObservable().setCurrent(i);

			ColumbaHeader h = new ColumbaHeader();

			loadHeader(p, h);

			headerList.add(h, (Integer) h.get("columba.uid"));
		}

		// close stream
		closeInputStream();

		// we are done
		if (getObservable() != null) {
			getObservable().clearMessage(500);	// 500 ms delay
			getObservable().resetCurrent();
		}
		
	}

	public void save() throws Exception {
		// we didn't load any header to save
		if (!isHeaderCacheLoaded())
			return;

		ColumbaLogger.log.info("saving header-cache=" + headerFile);

		ObjectOutputStream p = openOutputStream();

		int count = headerList.count();
		if (count == 0)
			return;

		p.writeInt(count);

		ColumbaHeader h;

		for (Enumeration e = headerList.keys(); e.hasMoreElements();) {
			Object uid = (Integer) e.nextElement();

			h = (ColumbaHeader) headerList.getHeader(uid);

			saveHeader(p, h);
		}

		closeOutputStream();
	}

	/* (non-Javadoc)
	 * @see org.columba.mail.folder.headercache.AbstractHeaderCache#loadHeader(java.io.ObjectInputStream, org.columba.mail.message.HeaderInterface)
	 */
	protected void loadHeader(ObjectInputStream p, HeaderInterface h)
		throws Exception {

		Object uid = p.readObject();
		h.set("columba.uid", uid);
		super.loadHeader(p, h);
	}

	/* (non-Javadoc)
	 * @see org.columba.mail.folder.headercache.AbstractHeaderCache#saveHeader(java.io.ObjectOutputStream, org.columba.mail.message.HeaderInterface)
	 */
	protected void saveHeader(ObjectOutputStream p, HeaderInterface h)
		throws Exception {

		p.writeObject(h.get("columba.uid"));

		super.saveHeader(p, h);
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

QDox parse error in file://<WORKSPACE>/data/code-rep-dataset/Dataset2/Tasks/6620.java