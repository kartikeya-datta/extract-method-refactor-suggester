error id: file://<WORKSPACE>/data/code-rep-dataset/Dataset2/Tasks/8447.java
file://<WORKSPACE>/data/code-rep-dataset/Dataset2/Tasks/8447.java
### com.thoughtworks.qdox.parser.ParseException: syntax error @[1,1]

error in qdox parser
file content:
```java
offset: 1
uri: file://<WORKSPACE>/data/code-rep-dataset/Dataset2/Tasks/8447.java
text:
```scala
M@@essage sendAXFR(Message query) {

// Copyright (c) 1999 Brian Wellington (bwelling@xbill.org)
// Portions Copyright (c) 1999 Network Associates, Inc.

package DNS;

import java.util.*;
import java.io.*;
import java.net.*;

public class ExtendedResolver implements Resolver {

public class Receiver implements ResolverListener {
	public void
	receiveMessage(int id, Message m) {
		Integer ID, R;
		synchronized (idMap) {
			ID = new Integer(id);
			R = (Integer)idMap.get(ID);
			idMap.remove(ID);
		}
		synchronized (queue) {
			queue.addElement(m);
			queue.addElement(R);
			queue.notify();
		}
	}
}

static final int quantum = 15;

SimpleResolver [] resolvers;
boolean [] invalid;
Receiver receiver;
Vector queue;
Hashtable idMap;
Vector workerthreads;

public
ExtendedResolver() throws UnknownHostException {
	String [] servers = FindServer.find();
	if (servers != null) {
		resolvers = new SimpleResolver[servers.length];
		for (int i = 0; i < servers.length; i++)
			resolvers[i] = new SimpleResolver(servers[i]);
	}
	else {
		resolvers = new SimpleResolver[1];
		resolvers[0] = new SimpleResolver();
	}
	invalid = new boolean[resolvers.length];
	receiver = new Receiver();
	queue = new Vector();
	idMap = new Hashtable();
}

boolean
sendTo(Message query, int r, int q) {
	if (invalid[r])
		return false;
	q -= r;
	switch (q) {
		case 0:
			resolvers[r].setTimeout(quantum);
			break;
		case 1:
			resolvers[r].setTimeout(2 * quantum);
			break;
		case 3:
			resolvers[r].setTimeout(3 * quantum);
			break;
		default:
			if (q < 6)
				return true;
			return false;
	}
	int id = resolvers[r].sendAsync(query, receiver);
	synchronized (idMap) {
		idMap.put(new Integer(id), new Integer(r));
	}
	return true;
}

public void
setPort(int port) {
	for (int i = 0; i < resolvers.length; i++)
		resolvers[i].setPort(port);
}

public void
setTCP(boolean flag) {
	for (int i = 0; i < resolvers.length; i++)
		resolvers[i].setTCP(flag);
}

public void
setIgnoreTruncation(boolean flag) {
	for (int i = 0; i < resolvers.length; i++)
		resolvers[i].setIgnoreTruncation(flag);
}

public void
setEDNS(int level) {
	for (int i = 0; i < resolvers.length; i++)
		resolvers[i].setEDNS(level);
}

public void
setTSIGKey(String name, String key) {
	for (int i = 0; i < resolvers.length; i++)
		resolvers[i].setTSIGKey(name, key);
}

public void
setTSIGKey(String key) {
	for (int i = 0; i < resolvers.length; i++)
		resolvers[i].setTSIGKey(key);
}

public void
setTimeout(int secs) {
	for (int i = 0; i < resolvers.length; i++)
		resolvers[i].setTimeout(secs);
}

public Message
send(Message query) {
	int q, r;
	Message best = null;
	byte rcode;

	for (q = 0; q < 20; q++) {
		boolean ok = false;
		for (r = 0; r < resolvers.length; r++)
			ok |= sendTo(query, r, q);
		if (!ok)
			break;
		Message m = null;
		synchronized (queue) {
			try {
				queue.wait((quantum+1) * 1000);
			}
			catch (InterruptedException e) {
				System.out.println("interrupted");
			}
			if (queue.size() == 0)
				continue;
			m = (Message) queue.firstElement();
			queue.removeElementAt(0);
			Integer I = (Integer) queue.firstElement();
			queue.removeElementAt(0);
			r = I.intValue();
		}
		if (m == null)
			invalid[r] = true;
		else {
			rcode = m.getHeader().getRcode();
			if (rcode == Rcode.NOERROR)
				return m;
			else {
				if (best == null)
					best = m;
				invalid[r] = true;
			}
		}
	}
	return best;
}

private int
uniqueID(Message m) {
	Record r = m.getQuestion();
	return (((r.getName().hashCode() & 0xFFFF) << 16) +
		(r.getType() + hashCode() << 8) +
		(hashCode() & 0xFF));
}

public int
sendAsync(final Message query, final ResolverListener listener) {
	final int id = uniqueID(query);
	if (workerthreads == null)
		workerthreads = new Vector();
	WorkerThread t = null;
	synchronized (workerthreads) {
		if (workerthreads.size() > 0) {
			t = (WorkerThread) workerthreads.firstElement();
			workerthreads.removeElement(t);
		}
	}
	if (t == null) {
		t = new WorkerThread(this, workerthreads);
		t.setDaemon(true);
		t.start();
	}
	synchronized (t) {
		t.assign(query, id, listener);
		t.notify();
	}
	return id;
}

public
Message sendAXFR(Message query) throws IOException {
	return resolvers[0].sendAXFR(query);
}

public Resolver
getResolver(int i) {
	if (i < resolvers.length)
		return resolvers[i];
	return null;
}

public Resolver []
getResolvers() {
	return resolvers;
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

QDox parse error in file://<WORKSPACE>/data/code-rep-dataset/Dataset2/Tasks/8447.java