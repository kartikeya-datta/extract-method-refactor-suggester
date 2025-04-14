error id: file://<WORKSPACE>/data/code-rep-dataset/Dataset2/Tasks/7615.java
file://<WORKSPACE>/data/code-rep-dataset/Dataset2/Tasks/7615.java
### com.thoughtworks.qdox.parser.ParseException: syntax error @[3,1]

error in qdox parser
file content:
```java
offset: 84
uri: file://<WORKSPACE>/data/code-rep-dataset/Dataset2/Tasks/7615.java
text:
```scala
@SuppressWarnings({"serial","serial"}) class CacheEntry implements Serializable {

p@@ackage org.columba.core.io;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.io.Serializable;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.Date;
import java.util.Iterator;
import java.util.List;

public class StreamCache {

	public static final long DEFAULT_SIZE = 10 * 1024 * 1024;// Byte

	public static final String FIFO_FILE = ".fifo";

	private File dir;

	private List<CacheEntry> fifo;

	private long actSize;

	private long maxSize;

	public StreamCache(File directory) {
		this(directory, DEFAULT_SIZE);
	}

	public StreamCache(File directory, long maxSize) {
		dir = directory;
		if (!dir.exists()) {
			if (!dir.mkdirs()) {
				throw new RuntimeException(dir.toString()
						+ " could not be created!");
			}
		} else {
			// try to restore from previous session
			try {
				restore();
			} catch (IOException e) {
				// Never mind
			}
		}
		actSize = 0;

		// If the fifo could not be restored initialize it
		if (fifo == null) {
			fifo = new ArrayList<CacheEntry>(100);
		}

		this.maxSize = maxSize;
	}

	public InputStream passiveAdd(Object key, InputStream in)
			throws IOException {
		File streamFile = new File(dir.getAbsoluteFile() + File.separator
				+ key.toString() + ".cache");
		return new StreamCacheCopyStream(in, key, streamFile, this);
	}

	public void activeAdd(Object key, InputStream in) throws IOException {
		File streamFile = new File(dir.getAbsoluteFile() + File.separator
				+ key.toString() + ".cache");
		StreamUtils.streamCopy(in, new FileOutputStream(streamFile));
		add(key, streamFile);
	}

	void add(Object key, File out) {
		fifo.add(new CacheEntry(key, out));

		actSize += out.length();

		Comparator _myComperator = new Comparator() {

			public int compare(Object arg0, Object arg1) {
				Date a = ((CacheEntry) arg0).lastAccess;
				Date b = ((CacheEntry) arg1).lastAccess;

				if (a.before(b))
					return 1;
				if (a.after(b))
					return -1;
				return 0;
			}

		};
		Collections.sort(fifo, _myComperator);

		ensureMaxSize();
	}

	private void ensureMaxSize() {
		while (actSize > maxSize) {
			CacheEntry entry = fifo.remove(fifo.size() - 1);
			actSize -= entry.file.length();
			entry.file.delete();
		}
	}

	public InputStream get(Object key) {
		Iterator it = fifo.iterator();

		while (it.hasNext()) {
			CacheEntry c = (CacheEntry) it.next();
			if (c.key.equals(key)) {
				try {
					return c.createStream();
				} catch (FileNotFoundException e) {
					it.remove();
					actSize -= c.file.length();
				}
			}
		}

		return null;
	}

	public long getActSize() {
		return actSize;
	}

	public void clear() {
		Iterator it = fifo.iterator();

		while (it.hasNext()) {
			CacheEntry c = (CacheEntry) it.next();
			if (!c.file.delete()) {
				// Try again after shutdown
				c.file.deleteOnExit();
			}
			it.remove();
		}

		actSize = 0;
	}

	public void persist() throws IOException {
		File file = new File(dir.getAbsoluteFile() + File.separator + FIFO_FILE);
		ObjectOutputStream out = new ObjectOutputStream(new FileOutputStream(
				file));
		out.writeObject(fifo);
		out.close();
	}

	public void restore() throws IOException {
		File file = new File(dir.getAbsoluteFile() + File.separator + FIFO_FILE);
		try {
			fifo = (List<CacheEntry>) new ObjectInputStream(new FileInputStream(file))
					.readObject();
		} catch (ClassNotFoundException e) {
			// ignore this yet
		}
	}

	public long getMaxSize() {
		return maxSize;
	}

	public void setMaxSize(long maxSize) {
		this.maxSize = maxSize;
		ensureMaxSize();
	}
}

class CacheEntry implements Serializable {
	Date lastAccess;

	Object key;

	File file;

	public CacheEntry(Object key, File file) {
		this.key = key;
		this.file = file;
		this.lastAccess = new Date();
	}

	public InputStream createStream() throws FileNotFoundException {
		this.lastAccess = new Date();
		return new FileInputStream(file);
	}

	/**
	 * @return Returns the file.
	 */
	public File getFile() {
		return file;
	}

	/**
	 * @param file
	 *            The file to set.
	 */
	public void setFile(File file) {
		this.file = file;
	}

	/**
	 * @return Returns the key.
	 */
	public Object getKey() {
		return key;
	}

	/**
	 * @param key
	 *            The key to set.
	 */
	public void setKey(Object key) {
		this.key = key;
	}

	/**
	 * @return Returns the lastAccess.
	 */
	public Date getLastAccess() {
		return lastAccess;
	}

	/**
	 * @param lastAccess
	 *            The lastAccess to set.
	 */
	public void setLastAccess(Date lastAccess) {
		this.lastAccess = lastAccess;
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

QDox parse error in file://<WORKSPACE>/data/code-rep-dataset/Dataset2/Tasks/7615.java