error id: file://<WORKSPACE>/data/code-rep-dataset/Dataset2/Tasks/89.java
file://<WORKSPACE>/data/code-rep-dataset/Dataset2/Tasks/89.java
### com.thoughtworks.qdox.parser.ParseException: syntax error @[1,1]

error in qdox parser
file content:
```java
offset: 1
uri: file://<WORKSPACE>/data/code-rep-dataset/Dataset2/Tasks/89.java
text:
```scala
h@@ostname = FindServer.find1();

// Copyright (c) 1999 Brian Wellington (bwelling@xbill.org)
// Portions Copyright (c) 1999 Network Associates, Inc.

package DNS;

import java.util.*;
import java.io.*;
import java.net.*;
import DNS.utils.*;

public class Resolver {

class WorkerThread extends Thread {
	private Message query;
	private int id;
	private ResolverListener listener;

	public void assign(Message _query, int _id, ResolverListener _listener)
	{
		query = _query;
		id = _id;
		listener = _listener;
	}

	public void run() {
		while (true) {
			Message response = null;
			try {
				response = send(query);
			}
			catch (IOException e) {
			}
			listener.receiveMessage(id, response);
			synchronized (workerthreads) {
				if (workerthreads.size() > 3)
					return;
				workerthreads.addElement(this);
			}
			synchronized (this) {
				try {
					wait();
				}
				catch (InterruptedException e) {
				}
			}
		}
	}
}

public static final int PORT		= 53;

InetAddress addr;
int port = PORT;
boolean useTCP, ignoreTruncation;
int EDNSlevel = -1;
TSIG tsig;
int timeoutValue = 60 * 1000;
Vector workerthreads;

static String defaultResolver = "localhost";

public
Resolver(String hostname) throws UnknownHostException {
	if (hostname == null) {
		hostname = FindResolver.find1();
		if (hostname == null)
			hostname = defaultResolver;
	}
	addr = InetAddress.getByName(hostname);
}

public
Resolver() throws UnknownHostException {
	this(null);
}

public static void
setDefaultResolver(String hostname) {
	defaultResolver = hostname;
}

public void
setPort(int port) {
	this.port = port;
}

public void
setTCP(boolean flag) {
	this.useTCP = flag;
}

public void
setIgnoreTruncation(boolean flag) {
	this.ignoreTruncation = flag;
}

public void
setEDNS(int level) {
	this.EDNSlevel = level;
}

public void
setTSIGKey(String name, String key) {
	byte [] keyArray = base64.fromString(key);
	if (keyArray == null) {
		System.out.println("Invalid TSIG key string");
		return;
	}
	tsig = new TSIG(name, keyArray);
}

public void
setTSIGKey(String key) {
	String name;
	try {
		name = InetAddress.getLocalHost().getHostName();
	}
	catch (UnknownHostException e) {
		System.out.println("getLocalHost failed");
		return;
	}
	setTSIGKey(name, key);
}

public void
setTimeout(int secs) {
	timeoutValue = secs * 1000;
}

Message
sendTCP(Message query, byte [] out) throws IOException {
	byte [] in;
	Socket s;
	int inLength;
	DataInputStream dataIn;

	try {
		s = new Socket(addr, port);
	}
	catch (SocketException e) {
		System.out.println(e);
		return null;
	}

	new DataOutputStream(s.getOutputStream()).writeShort(out.length);
	s.getOutputStream().write(out);
	s.setSoTimeout(timeoutValue);

	try {
		dataIn = new DataInputStream(s.getInputStream());
		inLength = dataIn.readUnsignedShort();
		in = new byte[inLength];
		dataIn.readFully(in);
	}
	catch (InterruptedIOException e) {
		s.close();
		System.out.println(";; No response");
		return null;
	}

	s.close();
	Message response = new Message(in);
	if (tsig != null) {
		boolean ok = tsig.verify(response, in, query.getTSIG());
		System.out.println(";; TSIG verify: " + ok);
	}
	return response;
}

public Message
send(Message query) throws IOException {
	byte [] out, in;
	Message response;
	DatagramSocket s;
	DatagramPacket dp;
	int udpLength = 512;

	try {
		s = new DatagramSocket();
	}
	catch (SocketException e) {
		System.out.println(e);
		return null;
	}

	if (EDNSlevel >= 0) {
		udpLength = 1280;
		query.addRecord(Section.ADDITIONAL, EDNS.newOPT(udpLength));
	}

	if (tsig != null)
		tsig.apply(query, null);


	out = query.toWire();

	if (useTCP)
		return sendTCP(query, out);

	s.send(new DatagramPacket(out, out.length, addr, port));

	dp = new DatagramPacket(new byte[udpLength], udpLength);
	s.setSoTimeout(timeoutValue);
	try {
		s.receive(dp);
	}
	catch (InterruptedIOException e) {
		s.close();
		System.out.println(";; No response");
		return null;
	}
	in = new byte [dp.getLength()];
	System.arraycopy(dp.getData(), 0, in, 0, in.length);
	response = new Message(in);
	if (tsig != null) {
		boolean ok = tsig.verify(response, in, query.getTSIG());
		System.out.println(";; TSIG verify: " + ok);
	}

	s.close();
	if (response.getHeader().getFlag(Flags.TC) && !ignoreTruncation)
		return sendTCP(query, out);
	else
		return response;
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
		t = new WorkerThread();
		t.setDaemon(true);
		t.start();
	}
	synchronized (t) {
		t.assign(query, id, listener);
		t.notify();
	}
	return id;
}

public Message
sendAXFR(Message query) throws IOException {
	byte [] out, in;
	Socket s;
	int inLength;
	DataInputStream dataIn;
	int soacount = 0;
	Message response;
	boolean first = true;

	try {
		s = new Socket(addr, port);
	}
	catch (SocketException e) {
		System.out.println(e);
		return null;
	}

	if (tsig != null)
		tsig.apply(query, null);

	out = query.toWire();
	new DataOutputStream(s.getOutputStream()).writeShort(out.length);
	s.getOutputStream().write(out);
	s.setSoTimeout(timeoutValue);

	response = new Message();
	response.getHeader().setID(query.getHeader().getID());
	if (tsig != null)
		tsig.verifyAXFRStart();
	while (soacount < 2) {
		try {
			dataIn = new DataInputStream(s.getInputStream());
			inLength = dataIn.readUnsignedShort();
			in = new byte[inLength];
			dataIn.readFully(in);
		}
		catch (InterruptedIOException e) {
			s.close();
			System.out.println(";; No response");
			return null;
		}
		Message m = new Message(in);
		if (m.getHeader().getCount(Section.QUESTION) != 0 ||
		    m.getHeader().getCount(Section.ANSWER) <= 0 ||
		    m.getHeader().getCount(Section.AUTHORITY) != 0)
		{
			StringBuffer sb = new StringBuffer();
			sb.append("Invalid AXFR: ");
			for (int i=0; i < 4; i++) {
				Enumeration e = m.getSection(i);
				System.out.println("--");
				while (e.hasMoreElements()) {
					Record r;
					r = (Record)e.nextElement();
					System.out.println(r);
				}
				System.out.println();
			}
			System.out.println(sb.toString());
			s.close();
			return null;
		}
		for (int i = 1; i < 4; i++) {
			Enumeration e = m.getSection(i);
			while (e.hasMoreElements()) {
				Record r = (Record)e.nextElement();
				response.addRecord(i, r);
				if (r instanceof SOARecord)
					soacount++;
			}
		}
		if (tsig != null) {
			boolean required = (soacount > 1 || first);
			boolean ok = tsig.verifyAXFR(m, in, query.getTSIG(),
						     required, first);
			System.out.println("TSIG verify: " + ok);
		}
		first = false;
	}

	s.close();
	return response;
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

QDox parse error in file://<WORKSPACE>/data/code-rep-dataset/Dataset2/Tasks/89.java