error id: file://<WORKSPACE>/data/code-rep-dataset/Dataset2/Tasks/2566.java
file://<WORKSPACE>/data/code-rep-dataset/Dataset2/Tasks/2566.java
### com.thoughtworks.qdox.parser.ParseException: syntax error @[6,1]

error in qdox parser
file content:
```java
offset: 138
uri: file://<WORKSPACE>/data/code-rep-dataset/Dataset2/Tasks/2566.java
text:
```scala
class FindServer {

// Copyright (c) 1999 Brian Wellington (bwelling@xbill.org)
// Portions Copyright (c) 1999 Network Associates, Inc.

p@@ackage DNS;

import java.io.*;
import java.util.*;

class FindResolver {

static String [] server = null;
static boolean searched = false;

static String []
findProperty() {
	String s;
	String [] array;
	s = System.getProperty("dns.resolver");
	if (s != null) {
		array = new String[1];
		array[0] = s;
	}
	else {
		Vector v = null;
		for (int i = 1; i <= 3; i++) {
			s = System.getProperty("dns.server" + i);
			if (s == null)
				break;
			if (v == null)
				v = new Vector();
			v.addElement(s);
		}
		if (v == null)
			return null;
		array = new String[v.size()];
		for (int i = 0; i < v.size(); i++)
			array[i] = (String) v.elementAt(i);
	}
	return array;
}

static String []
findUnix() {
	InputStream in = null;
	String [] array;
	try {
		in = new FileInputStream("/etc/resolv.conf");
	}
	catch (FileNotFoundException e) {
		return null;
	}
	InputStreamReader isr = new InputStreamReader(in);
	BufferedReader br = new BufferedReader(isr);
	Vector v = null;
	try {
		while (true) {
			String line = br.readLine();
			if (line == null)
				return null;
			if (!line.startsWith("nameserver "))
				continue;
			if (v == null)
				v = new Vector();
			StringTokenizer st = new StringTokenizer(line);
			st.nextToken(); /* skip nameserver */
			v.addElement(st.nextToken());
		}
	}
	catch (IOException e) {
	}
	if (v == null)
		return null;
	array = new String[v.size()];
	for (int i = 0; i < v.size(); i++)
		array[i] = (String) v.elementAt(i);
	return null;
}

public static String []
find() {
	if (server != null || searched)
		return server;

	searched = true;
	server = findProperty();
	if (server != null)
		return server;

	server = findUnix();
	if (server != null)
		return server;

	return null;
}

public static String
find1() {
	String [] array = find();
	if (array == null)
		return null;
	else
		return array[0];
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

QDox parse error in file://<WORKSPACE>/data/code-rep-dataset/Dataset2/Tasks/2566.java