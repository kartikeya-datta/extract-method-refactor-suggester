error id: file://<WORKSPACE>/data/code-rep-dataset/Dataset2/Tasks/2980.java
file://<WORKSPACE>/data/code-rep-dataset/Dataset2/Tasks/2980.java
### com.thoughtworks.qdox.parser.ParseException: syntax error @[1,1]

error in qdox parser
file content:
```java
offset: 1
uri: file://<WORKSPACE>/data/code-rep-dataset/Dataset2/Tasks/2980.java
text:
```scala
n@@ew Rfc822Parser().parse(source, null);

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
package org.columba.mail.folder.outbox;

import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.util.List;
import java.util.Vector;

import org.columba.core.command.WorkerStatusController;
import org.columba.core.logging.ColumbaLogger;
import org.columba.mail.composer.SendableMessage;
import org.columba.mail.config.FolderItem;
import org.columba.mail.folder.headercache.CachedFolder;
import org.columba.mail.folder.headercache.LocalHeaderCache;
import org.columba.mail.folder.mh.CachedMHFolder;
import org.columba.mail.message.AbstractMessage;
import org.columba.mail.message.HeaderInterface;
import org.columba.mail.message.SendableHeader;
import org.columba.mail.parser.Rfc822Parser;

public class OutboxFolder extends CachedMHFolder {

	private SendListManager[] sendListManager = new SendListManager[2];
	private int actSender;
	private boolean isSending;

	protected OutboxHeaderCache cache;

	public OutboxFolder(FolderItem item) {
		super(item);

		sendListManager[0] = new SendListManager();
		sendListManager[1] = new SendListManager();
		actSender = 0;

		isSending = false;

		cache = new OutboxHeaderCache(this);

	}

	public AbstractMessage getMessage(
		Object uid,
		WorkerStatusController worker)
		throws Exception {
		if (aktMessage != null) {
			if (aktMessage.getUID().equals(uid)) {
				// this message is already cached
				ColumbaLogger.log.info("using already cached message..");

				return aktMessage;
			}
		}

		String source = getMessageSource(uid, worker);

		AbstractMessage message =
			new Rfc822Parser().parse(source, true, null, 0);
		message.setUID(uid);

		SendableHeader header = (SendableHeader) getHeaderList(worker).get(uid);
		SendableMessage sendableMessage = new SendableMessage();
		sendableMessage.setHeader(header);
		sendableMessage.setMimePartTree(message.getMimePartTree());
		sendableMessage.setSource(source);

		aktMessage = sendableMessage;

		return sendableMessage;
	}

	public String getDefaultChild() {
		return "MHFolder";
	}

	private void swapListManagers() throws Exception {
		// copy lost Messages
		System.out.println(
			"Sizes : "
				+ sendListManager[actSender].count()
				+ " - "
				+ sendListManager[1
				- actSender].count());

		while (sendListManager[actSender].hasMoreMessages()) {
			sendListManager[1
				- actSender].add(
					(SendableMessage) getMessage(sendListManager[actSender]
						.getNextUid(),
					null));
		}

		// swap
		actSender = 1 - actSender;

		System.out.println(
			"Sizes : "
				+ sendListManager[actSender].count()
				+ " - "
				+ sendListManager[1
				- actSender].count());

	}

	public void stoppedSending() {
		isSending = false;
	}

	public void save(WorkerStatusController worker) throws Exception {
		// only save header-cache if folder data changed
		if (getChanged() == true) {

			getHeaderCacheInstance().save(worker);
			setChanged(false);
		}
	}

	class OutboxHeaderCache extends LocalHeaderCache {
		public OutboxHeaderCache(CachedFolder folder) {
			super(folder);
		}

		public HeaderInterface createHeaderInstance() {
			return new SendableHeader();
		}

		protected void loadHeader(ObjectInputStream p, HeaderInterface h)
			throws Exception {
			super.loadHeader(p, h);

			int accountUid = p.readInt();
			((SendableHeader) h).setAccountUid(accountUid);

			List recipients = (Vector) p.readObject();
			((SendableHeader) h).setRecipients(recipients);

		}

		protected void saveHeader(ObjectOutputStream p, HeaderInterface h)
			throws Exception {
			super.saveHeader(p, h);

			p.writeInt(((SendableHeader) h).getAccountUid());

			p.writeObject(((SendableHeader) h).getRecipients());

		}
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

QDox parse error in file://<WORKSPACE>/data/code-rep-dataset/Dataset2/Tasks/2980.java