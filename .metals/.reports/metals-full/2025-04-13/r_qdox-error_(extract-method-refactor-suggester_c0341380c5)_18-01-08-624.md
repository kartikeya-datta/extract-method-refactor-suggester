error id: file://<WORKSPACE>/data/code-rep-dataset/Dataset2/Tasks/1026.java
file://<WORKSPACE>/data/code-rep-dataset/Dataset2/Tasks/1026.java
### com.thoughtworks.qdox.parser.ParseException: syntax error @[1,1]

error in qdox parser
file content:
```java
offset: 1
uri: file://<WORKSPACE>/data/code-rep-dataset/Dataset2/Tasks/1026.java
text:
```scala
r@@eturn retrieve(ref);

/*
 * @(#)StorableInput.java
 *
 * Project:		JHotdraw - a GUI framework for technical drawings
 *				http://www.jhotdraw.org
 *				http://jhotdraw.sourceforge.net
 * Copyright:	© by the original author(s) and all contributors
 * License:		Lesser GNU Public License (LGPL)
 *				http://www.opensource.org/licenses/lgpl-license.html
 */

package CH.ifa.draw.util;

import java.io.*;
import java.awt.Color;
import java.util.List;

/**
 * An input stream that can be used to resurrect Storable objects.
 * StorableInput preserves the object identity of the stored objects.
 *
 * @see Storable
 * @see StorableOutput
 *
 * @version <$CURRENT_VERSION$>s
 */
public class StorableInput {

	private StreamTokenizer fTokenizer;
	private List            fMap;

	/**
	 * Initializes a Storable input with the given input stream.
	 */
	public StorableInput(InputStream stream) {
		Reader r = new BufferedReader(new InputStreamReader(stream));
		fTokenizer = new StreamTokenizer(r);
		// include inner class separate in class names
		fTokenizer.wordChars('$', '$');
		fMap = CollectionsFactory.current().createList();
	}

	/**
	 * Reads and resurrects a Storable object from the input stream.
	 */
	public Storable readStorable() throws IOException {
		Storable storable;
		String s = readString();

		if (s.equals("NULL")) {
			return null;
		}

		if (s.equals("REF")) {
			int ref = readInt();
			return (Storable) retrieve(ref);
		}

		storable = (Storable) makeInstance(s);
		map(storable);
		storable.read(this);
		return storable;
	}

	/**
	 * Reads a string from the input stream.
	 */
	public String readString() throws IOException {
		int token = fTokenizer.nextToken();
		if (token == StreamTokenizer.TT_WORD || token == '"') {
			return fTokenizer.sval;
		}

		String msg = "String expected in line: " + fTokenizer.lineno();
		throw new IOException(msg);
	}

	/**
	 * Reads an int from the input stream.
	 */
	public int readInt() throws IOException {
		int token = fTokenizer.nextToken();
		if (token == StreamTokenizer.TT_NUMBER) {
			return (int) fTokenizer.nval;
		}

		String msg = "Integer expected in line: " + fTokenizer.lineno();
		IOException exception =  new IOException(msg);
		exception.printStackTrace();
		throw new IOException(msg);
	}

	/**
	 * Reads an int from the input stream.
	 */
	public long readLong() throws IOException {
		long token = fTokenizer.nextToken();
		if (token == StreamTokenizer.TT_NUMBER) {
			return (long)fTokenizer.nval;
		}
		String msg = "Long expected in line: " + fTokenizer.lineno();
		IOException exception =  new IOException(msg);
		//exception.printStackTrace();
		throw exception;
	}

	/**
	 * Reads a color from the input stream.
	 */
	public Color readColor() throws IOException {
		return new Color(readInt(), readInt(), readInt());
	}

	/**
	 * Reads a double from the input stream.
	 */
	public double readDouble() throws IOException {
		int token = fTokenizer.nextToken();
		if (token == StreamTokenizer.TT_NUMBER) {
			return fTokenizer.nval;
		}

		String msg = "Double expected in line: " + fTokenizer.lineno();
		throw new IOException(msg);
	}

	/**
	 * Reads a boolean from the input stream.
	 */
	public boolean readBoolean() throws IOException {
		int token = fTokenizer.nextToken();
		if (token == StreamTokenizer.TT_NUMBER) {
			return ((int) fTokenizer.nval) == 1;
		}

		String msg = "Integer expected in line: " + fTokenizer.lineno();
		throw new IOException(msg);
	}

	private Object makeInstance(String className) throws IOException {
		try {
			Class cl = Class.forName(className);
			return cl.newInstance();
		}
		catch (NoSuchMethodError e) {
			throw new IOException("Class " + className
				+ " does not seem to have a no-arg constructor");
		}
		catch (ClassNotFoundException e) {
			throw new IOException("No class: " + className);
		}
		catch (InstantiationException e) {
			throw new IOException("Cannot instantiate: " + className);
		}
		catch (IllegalAccessException e) {
			throw new IOException("Class (" + className + ") not accessible");
		}
	}

	private void map(Storable storable) {
		if (!fMap.contains(storable)) {
			fMap.add(storable);
		}
	}

	private Storable retrieve(int ref) {
		return (Storable)fMap.get(ref);
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

QDox parse error in file://<WORKSPACE>/data/code-rep-dataset/Dataset2/Tasks/1026.java