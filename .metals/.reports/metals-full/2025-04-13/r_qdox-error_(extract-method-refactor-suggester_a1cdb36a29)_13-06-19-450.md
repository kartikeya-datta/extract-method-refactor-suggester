error id: file://<WORKSPACE>/data/code-rep-dataset/Dataset2/Tasks/4374.java
file://<WORKSPACE>/data/code-rep-dataset/Dataset2/Tasks/4374.java
### com.thoughtworks.qdox.parser.ParseException: syntax error @[1,1]

error in qdox parser
file content:
```java
offset: 1
uri: file://<WORKSPACE>/data/code-rep-dataset/Dataset2/Tasks/4374.java
text:
```scala
S@@tring [] servers = FindServer.servers();

// Copyright (c) 1999 Brian Wellington (bwelling@xbill.org)
// Portions Copyright (c) 1999 Network Associates, Inc.

package DNS;

import java.util.*;
import java.io.*;
import java.net.*;

public class ExtendedResolver implements Resolver {

class QElement {
	Message m;
	int res;

	public
	QElement(Message _m, int _res) {
		m = _m;
		res = _res;
	}
}

class Receiver implements ResolverListener {
	public void
	receiveMessage(int id, Message m) {
		Integer ID, R;
		int r;
		synchronized (idMap) {
			ID = new Integer(id);
			R = (Integer)idMap.get(ID);
			if (R == null)
				return;
			r = R.intValue();
			idMap.remove(ID);
		}
		synchronized (queue) {
			QElement qe = new QElement(m, r);
			queue.addElement(qe);
			queue.notify();
		}
	}
}

static final int quantum = 20;

Vector resolvers;
Receiver receiver;
Vector queue;
Hashtable idMap;

private void
init() {
	resolvers = new Vector();
	receiver = new Receiver();
	queue = new Vector();
	idMap = new Hashtable();
}

public
ExtendedResolver() throws UnknownHostException {
	init();
	String [] servers = FindServer.find();
	if (servers != null) {
		for (int i = 0; i < servers.length; i++)
			resolvers.addElement(new SimpleResolver(servers[i]));
	}
	else
		resolvers.addElement(new SimpleResolver());
}

public
ExtendedResolver(String [] servers) throws UnknownHostException {
	init();
	for (int i = 0; i < servers.length; i++)
		resolvers.addElement(new SimpleResolver(servers[i]));
}

public
ExtendedResolver(Resolver [] res) throws UnknownHostException {
	init();
	for (int i = 0; i < res.length; i++)
		resolvers.addElement(res[i]);
}

boolean
sendTo(Message query, int r, int q) {
	q -= r;
	Resolver res = (Resolver) resolvers.elementAt(r);
	/* Three retries */
	if (q >= 0 && q < 3) {
		synchronized (idMap) {
			res.setTimeout(2 * quantum * (q + 1));
			int id = res.sendAsync(query, receiver);
			idMap.put(new Integer(id), new Integer(r));
		}
	}
	if (q < 6)
		return true;
	return false;
}

public void
setPort(int port) {
	for (int i = 0; i < resolvers.size(); i++)
		((Resolver)resolvers.elementAt(i)).setPort(port);
}

public void
setTCP(boolean flag) {
	for (int i = 0; i < resolvers.size(); i++)
		((Resolver)resolvers.elementAt(i)).setTCP(flag);
}

public void
setIgnoreTruncation(boolean flag) {
	for (int i = 0; i < resolvers.size(); i++)
		((Resolver)resolvers.elementAt(i)).setIgnoreTruncation(flag);
}

public void
setEDNS(int level) {
	for (int i = 0; i < resolvers.size(); i++)
		((Resolver)resolvers.elementAt(i)).setEDNS(level);
}

public void
setTSIGKey(String name, String key) {
	for (int i = 0; i < resolvers.size(); i++)
		((Resolver)resolvers.elementAt(i)).setTSIGKey(name, key);
}

public void
setTSIGKey(String key) {
	for (int i = 0; i < resolvers.size(); i++)
		((Resolver)resolvers.elementAt(i)).setTSIGKey(key);
}

public void
setTimeout(int secs) {
	for (int i = 0; i < resolvers.size(); i++)
		((Resolver)resolvers.elementAt(i)).setTimeout(secs);
}

public Message
send(Message query) {
	int q, r;
	Message best = null;
	byte rcode;
	boolean [] invalid = new boolean[resolvers.size()];

	for (q = 0; q < 20; q++) {
		Message m;
		boolean ok = false;
		for (r = 0; r < resolvers.size(); r++)
			if (!invalid[r])
				ok |= sendTo(query, r, q);
		if (!ok)
			break;
		long start = System.currentTimeMillis();
		long now = start;
		while (true) {
			now = System.currentTimeMillis();
			if (now - start > quantum * 1000)
				break;
			synchronized (queue) {
				try {
					long left;
					left = (quantum * 1000) + start - now;
					if (left > 0)
						queue.wait(left);
				}
				catch (InterruptedException e) {
				}
				if (queue.size() == 0)
					continue;
				QElement qe = (QElement) queue.firstElement();
				queue.removeElement(qe);
				m = qe.m;
				r = qe.res;
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
	}
	return best;
}

private int
uniqueID(Message m) {
	Record r = m.getQuestion();
	return (((r.getName().hashCode() & 0xFFFF) << 16) +
		(r.getType() << 8) +
		(hashCode() & 0xFF));
}

public int
sendAsync(final Message query, final ResolverListener listener) {
	final int id = uniqueID(query);
	WorkerThread.assignThread(this, query, id, listener);
	return id;
}

public
Message sendAXFR(Message query) {
	return ((Resolver)resolvers.elementAt(0)).sendAXFR(query);
}

public Resolver
getResolver(int i) {
	if (i < resolvers.size())
		return (Resolver)resolvers.elementAt(i);
	return null;
}

public Resolver []
getResolvers() {
	Resolver [] res = new Resolver[resolvers.size()];
	for (int i = 0; i < resolvers.size(); i++)
		res[i] = (Resolver) resolvers.elementAt(i);
	return res;
}

public void
addResolver(Resolver r) {
	resolvers.addElement(r);
}

public void
deleteResolver(Resolver r) {
	resolvers.removeElement(r);
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

QDox parse error in file://<WORKSPACE>/data/code-rep-dataset/Dataset2/Tasks/4374.java