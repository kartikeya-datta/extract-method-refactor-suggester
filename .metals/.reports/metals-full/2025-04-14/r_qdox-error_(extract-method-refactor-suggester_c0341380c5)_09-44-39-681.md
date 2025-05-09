error id: file://<WORKSPACE>/data/code-rep-dataset/Dataset2/Tasks/2008.java
file://<WORKSPACE>/data/code-rep-dataset/Dataset2/Tasks/2008.java
### com.thoughtworks.qdox.parser.ParseException: syntax error @[1,1]

error in qdox parser
file content:
```java
offset: 1
uri: file://<WORKSPACE>/data/code-rep-dataset/Dataset2/Tasks/2008.java
text:
```scala
h@@ = (ColumbaHeader) headerList.get(str);

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

package org.columba.mail.pop3;

import java.util.Enumeration;

import org.columba.core.command.StatusObservable;
import org.columba.core.logging.ColumbaLogger;
import org.columba.core.main.MainInterface;
import org.columba.mail.folder.headercache.AbstractHeaderCache;
import org.columba.mail.folder.headercache.CachedHeaderfields;
import org.columba.mail.folder.headercache.ObjectReader;
import org.columba.mail.folder.headercache.ObjectWriter;
import org.columba.mail.message.ColumbaHeader;
import org.columba.mail.message.HeaderList;
import org.columba.mail.util.MailResourceLoader;
import org.columba.ristretto.message.HeaderInterface;

/**
 * Header caching facility very similar to the ones used by folders.
 * <p>
 * We need this for the managing server/messages remotely feature, which shows
 * a messagelist of all messages on the POP3 server to the user.
 * 
 * @author freddy
 */
public class POP3HeaderCache extends AbstractHeaderCache {

	protected POP3Server server;
	/**
	 * Constructor for POP3HeaderCache.
	 * 
	 * @param folder
	 */
	public POP3HeaderCache(POP3Server server) {
		super(server.getConfigFile());

		this.server = server;
	}

	public StatusObservable getObservable() {
		return server.getObservable();
	}

	public void load() throws Exception {
		ColumbaLogger.log.info("loading header-cache=" + headerFile);
		headerList = new HeaderList();

		try {

			reader = new ObjectReader(headerFile);
		} catch (Exception e) {
			if (MainInterface.DEBUG)
				e.printStackTrace();
		}

		int capacity = ((Integer) reader.readObject()).intValue();
		ColumbaLogger.log.info("capacity=" + capacity);

		if (getObservable() != null)
			getObservable().setMessage(
				MailResourceLoader.getString(
					"statusbar",
					"message",
					"load_headers"));

		if (getObservable() != null)
			getObservable().setMax(capacity);

		for (int i = 1; i <= capacity; i++) {
			if (getObservable() != null)
				getObservable().setCurrent(i);

			ColumbaHeader h = new ColumbaHeader();

			loadHeader(h);

			headerList.add(h, h.get("columba.pop3uid"));
			//headerList.add(h, (String) h.get("columba.uid"));
		}

		// close stream
		reader.close();
	}

	public void save() throws Exception {
		// we didn't load any header to save
		if (!isHeaderCacheLoaded())
			return;

		ColumbaLogger.log.info("saving header-cache=" + headerFile);

		try {
			writer = new ObjectWriter(headerFile);

		} catch (Exception e) {
			if (MainInterface.DEBUG)
				e.printStackTrace();
		}

		int count = headerList.count();
		if (count == 0)
			return;

		writer.writeObject(new Integer(count));

		ColumbaHeader h;

		for (Enumeration e = headerList.keys(); e.hasMoreElements();) {
			String str = (String) e.nextElement();

			h = (ColumbaHeader) headerList.getHeader(str);

			saveHeader(h);
		}

		writer.close();
	}

	protected void loadHeader(HeaderInterface h) throws Exception {
		
		String[] columnNames = CachedHeaderfields.POP3_HEADERFIELDS;

		for (int j = 0; j < columnNames.length; j++) {			
			h.set(columnNames[j], reader.readObject());
		}

	}

	protected void saveHeader(HeaderInterface h) throws Exception {
		
		String[] columnNames = CachedHeaderfields.POP3_HEADERFIELDS;
		Object o;
		for (int j = 0; j < columnNames.length; j++) {			
			writer.writeObject(h.get(columnNames[j]));
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

QDox parse error in file://<WORKSPACE>/data/code-rep-dataset/Dataset2/Tasks/2008.java