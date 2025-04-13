error id: file://<WORKSPACE>/data/code-rep-dataset/Dataset2/Tasks/5640.java
file://<WORKSPACE>/data/code-rep-dataset/Dataset2/Tasks/5640.java
### com.thoughtworks.qdox.parser.ParseException: syntax error @[1,22]

error in qdox parser
file content:
```java
offset: 22
uri: file://<WORKSPACE>/data/code-rep-dataset/Dataset2/Tasks/5640.java
text:
```scala
private static final i@@nt quantum = 20;

// Copyright (c) 1999 Brian Wellington (bwelling@xbill.org)
// Portions Copyright (c) 1999 Network Associates, Inc.

package org.xbill.DNS;

import java.util.*;
import java.io.*;
import java.net.*;
import org.xbill.Task.*;

/**
 * An implementation of Resolver that can send queries to multiple servers,
 * sending the queries multiple times if necessary.
 * @see Resolver
 *
 * @author Brian Wellington
 */

public class ExtendedResolver implements Resolver {

class QElement {
	Object obj;
	int res;

	public
	QElement(Object _obj, int _res) {
		obj = _obj;
		res = _res;
	}
}

class Receiver implements ResolverListener {
	Vector queue;
	Hashtable idMap;

	public
	Receiver(Vector _queue, Hashtable _idMap) {
		queue = _queue;
		idMap = _idMap;
	}

	public void
	enqueueInfo(Object id, Object obj) {
		Integer R;
		int r;
		synchronized (idMap) {
			R = (Integer)idMap.get(id);
			if (R == null)
				return;
			r = R.intValue();
			idMap.remove(id);
		}
		synchronized (queue) {
			QElement qe = new QElement(obj, r);
			queue.addElement(qe);
			queue.notify();
		}
	}


	public void
	receiveMessage(Object id, Message m) {
		if (Options.check("verbose"))
			System.err.println("ExtendedResolver: " +
					   "message from " + id);
		enqueueInfo(id, m);
	}

	public void
	handleException(Object id, Exception e) {
		if (Options.check("verbose"))
			System.err.println("ExtendedResolver: " +
					   "exception from " + id);
		enqueueInfo(id, e);
	}
}

private static final int quantum = 30;
private static final byte retries = 3;
private static int uniqueID = 0;
private Vector resolvers;

private void
init() {
	resolvers = new Vector();
}

/**
 * Creates a new Extended Resolver.  FindServer is used to locate the servers
 * for which SimpleResolver contexts should be initialized.
 * @see SimpleResolver
 * @see FindServer
 * @exception UnknownHostException Failure occured initializing SimpleResolvers
 */
public
ExtendedResolver() throws UnknownHostException {
	init();
	String [] servers = FindServer.servers();
	if (servers != null) {
		for (int i = 0; i < servers.length; i++) {
			Resolver r = new SimpleResolver(servers[i]);
			r.setTimeout(quantum);
			resolvers.addElement(r);
		}
	}
	else
		resolvers.addElement(new SimpleResolver());
}

/**
 * Creates a new Extended Resolver
 * @param servers  An array of server names for which SimpleResolver
 * contexts should be initialized.
 * @see SimpleResolver
 * @exception UnknownHostException Failure occured initializing SimpleResolvers
 */
public
ExtendedResolver(String [] servers) throws UnknownHostException {
	init();
	for (int i = 0; i < servers.length; i++) {
		Resolver r = new SimpleResolver(servers[i]);
		r.setTimeout(quantum);
		resolvers.addElement(r);
	}
}

/**
 * Creates a new Extended Resolver
 * @param res An array of pre-initialized Resolvers is provided.
 * @see SimpleResolver
 * @exception UnknownHostException Failure occured initializing SimpleResolvers
 */
public
ExtendedResolver(Resolver [] res) throws UnknownHostException {
	init();
	for (int i = 0; i < res.length; i++)
		resolvers.addElement(res[i]);
}

private void
sendTo(Message query, Receiver receiver, Hashtable idMap, int r) {
	Resolver res = (Resolver) resolvers.elementAt(r);
	synchronized (idMap) {
		Object id = res.sendAsync(query, receiver);
		if (Options.check("verbose"))
			System.err.println("ExtendedResolver: sending " +
					   id + "to resolver " + r);
		idMap.put(id, new Integer(r));
	}
}

/** Sets the port to communicate with on the servers */
public void
setPort(int port) {
	for (int i = 0; i < resolvers.size(); i++)
		((Resolver)resolvers.elementAt(i)).setPort(port);
}

/** Sets whether TCP connections will be sent by default */
public void
setTCP(boolean flag) {
	for (int i = 0; i < resolvers.size(); i++)
		((Resolver)resolvers.elementAt(i)).setTCP(flag);
}

/** Sets whether truncated responses will be returned */
public void
setIgnoreTruncation(boolean flag) {
	for (int i = 0; i < resolvers.size(); i++)
		((Resolver)resolvers.elementAt(i)).setIgnoreTruncation(flag);
}

/** Sets the EDNS version used on outgoing messages (only 0 is meaningful) */
public void
setEDNS(int level) {
	for (int i = 0; i < resolvers.size(); i++)
		((Resolver)resolvers.elementAt(i)).setEDNS(level);
}

/** Specifies the TSIG key that messages will be signed with */
public void
setTSIGKey(String name, String key) {
	for (int i = 0; i < resolvers.size(); i++)
		((Resolver)resolvers.elementAt(i)).setTSIGKey(name, key);
}

/**
 * Specifies the TSIG key (with the same name as the local host) that messages
 * will be signed with
 */
public void
setTSIGKey(String key) {
	for (int i = 0; i < resolvers.size(); i++)
		((Resolver)resolvers.elementAt(i)).setTSIGKey(key);
}

/** Sets the amount of time to wait for a response before giving up */
public void
setTimeout(int secs) {
	for (int i = 0; i < resolvers.size(); i++)
		((Resolver)resolvers.elementAt(i)).setTimeout(secs);
}

/**
 * Sends a message, and waits for a response.  Multiple servers are queried,
 * and queries are sent multiple times until either a successful response
 * is received, or it is clear that there is no successful response.
 * @return The response
 */
public Message
send(Message query) throws IOException {
	int q, r;
	Message best = null;
	IOException bestException = null;
	boolean [] invalid = new boolean[resolvers.size()];
	byte [] sent = new byte[resolvers.size()];
	byte [] recvd = new byte[resolvers.size()];
	Vector queue = new Vector();
	Hashtable idMap = new Hashtable();
	Receiver receiver = new Receiver(queue, idMap);

	while (true) {
		Message m;
		boolean waiting = false;
		QElement qe;
		synchronized (queue) {
			for (r = 0; r < resolvers.size(); r++) {
				if (sent[r] == recvd[r] && sent[r] < retries) {
					sendTo(query, receiver, idMap, r);
					sent[r]++;
					waiting = true;
					break;
				}
				else if (recvd[r] < sent[r])
					waiting = true;
			}
			if (!waiting)
				break;

			try {
				queue.wait();
			}
			catch (InterruptedException e) {
			}
			if (queue.size() == 0)
				continue;
			qe = (QElement) queue.firstElement();
			queue.removeElement(qe);
			if (qe.obj instanceof Message)
				m = (Message) qe.obj;
			else
				m = null;
			r = qe.res;
			recvd[r]++;
		}
		if (m == null) {
			IOException e = (IOException) qe.obj;
			if (!(e instanceof InterruptedIOException))
				invalid[r] = true;
			if (bestException == null)
				bestException = e;
		}
		else {
			short rcode = m.getRcode();
			if (rcode == Rcode.NOERROR)
				return m;
			else {
				if (best == null)
					best = m;
				else {
					short bestrcode;
					bestrcode = best.getRcode();
					if (rcode == Rcode.NXDOMAIN &&
					    bestrcode != Rcode.NXDOMAIN)
						best = m;
				}
				invalid[r] = true;
			}
		}
	}
	if (best != null)
		return best;
	throw bestException;
}

/**
 * Asynchronously sends a message, registering a listener to receive a callback
 * Multiple asynchronous lookups can be performed in parallel.
 * @return An identifier
 */
public Object
sendAsync(final Message query, final ResolverListener listener) {
	final Object id;
	synchronized (this) {
		id = new Integer(uniqueID++);
	}
	String name = this.getClass() + ": " + query.getQuestion().getName();
	WorkerThread.assignThread(new ResolveThread(this, query, id, listener),
				  name);
	return id;
}

/** Returns the i'th resolver used by this ExtendedResolver */
public Resolver
getResolver(int i) {
	if (i < resolvers.size())
		return (Resolver)resolvers.elementAt(i);
	return null;
}

/** Returns all resolvers used by this ExtendedResolver */
public Resolver []
getResolvers() {
	Resolver [] res = new Resolver[resolvers.size()];
	for (int i = 0; i < resolvers.size(); i++)
		res[i] = (Resolver) resolvers.elementAt(i);
	return res;
}

/** Adds a new resolver to be used by this ExtendedResolver */
public void
addResolver(Resolver r) {
	resolvers.addElement(r);
}

/** Deletes a resolver used by this ExtendedResolver */
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
	scala.collection.parallel.AdaptiveWorkStealingTasks$AWSTWrappedTask.internal(Tasks.scala:159)
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

QDox parse error in file://<WORKSPACE>/data/code-rep-dataset/Dataset2/Tasks/5640.java