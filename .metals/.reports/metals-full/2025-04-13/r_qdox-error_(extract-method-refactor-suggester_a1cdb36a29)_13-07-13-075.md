error id: file://<WORKSPACE>/data/code-rep-dataset/Dataset2/Tasks/2019.java
file://<WORKSPACE>/data/code-rep-dataset/Dataset2/Tasks/2019.java
### com.thoughtworks.qdox.parser.ParseException: syntax error @[1,1]

error in qdox parser
file content:
```java
offset: 1
uri: file://<WORKSPACE>/data/code-rep-dataset/Dataset2/Tasks/2019.java
text:
```scala
S@@tring s = nextString(st);

// Copyright (c) 1999 Brian Wellington (bwelling@xbill.org)
// Portions Copyright (c) 1999 Network Associates, Inc.

package org.xbill.DNS;

import java.net.*;
import java.io.*;
import java.util.*;
import org.xbill.DNS.utils.*;

/**
 * Address Record - maps a domain name to an Internet address
 *
 * @author Brian Wellington
 */

public class ARecord extends Record {

private static ARecord member = new ARecord();

private int addr;

private
ARecord() {}

private
ARecord(Name name, short dclass, int ttl) {
	super(name, Type.A, dclass, ttl);
}

static ARecord
getMember() {
	return member;
}

private static final int
fromBytes(byte b1, byte b2, byte b3, byte b4) {
	return (((b1 & 0xFF) << 24) |
		((b2 & 0xFF) << 16) |
		((b3 & 0xFF) << 8) |
		(b4 & 0xFF));
}

private static final int
fromArray(byte [] array) {
	return (fromBytes(array[0], array[1], array[2], array[3]));
}

private static final String
toDottedQuad(int addr) {
	StringBuffer sb = new StringBuffer();
	sb.append(((addr >>> 24) & 0xFF));
	sb.append(".");
	sb.append(((addr >>> 16) & 0xFF));
	sb.append(".");
	sb.append(((addr >>> 8) & 0xFF));
	sb.append(".");
	sb.append((addr & 0xFF));
	return sb.toString();
}

/**
 * Creates an A Record from the given data
 * @param address The address that the name refers to
 */
public
ARecord(Name name, short dclass, int ttl, InetAddress address) {
	this(name, dclass, ttl);
	addr = fromArray(address.getAddress());
}

Record
rrFromWire(Name name, short type, short dclass, int ttl, int length,
	   DataByteInputStream in)
throws IOException
{
	ARecord rec = new ARecord(name, dclass, ttl);

	if (in == null)
		return rec;

	byte b1 = in.readByte();
	byte b2 = in.readByte();
	byte b3 = in.readByte();
	byte b4 = in.readByte();
	rec.addr = fromBytes(b1, b2, b3, b4);
	return rec;
}

Record
rdataFromString(Name name, short dclass, int ttl, MyStringTokenizer st,
		Name origin)
throws TextParseException
{
	ARecord rec = new ARecord(name, dclass, ttl);
	String s = st.nextToken();
	try {
		InetAddress address;
		if (s.equals("@me@")) {
			address = InetAddress.getLocalHost();
			if (address.equals(InetAddress.getByName("127.0.0.1")))
			{
				String msg = "InetAddress.getLocalHost() is " +
					     "broken.  Don't use @me@.";
				throw new RuntimeException(msg);
			}
		} else {
			if (!Address.isDottedQuad(s))
				throw new TextParseException
						("invalid dotted quad");
			address = Address.getByName(s);
		}
		rec.addr = fromArray(address.getAddress());
	}
	catch (UnknownHostException e) {
		throw new TextParseException("invalid address");
	}
	return rec;
}

/** Converts rdata to a String */
public String
rdataToString() {
	return (toDottedQuad(addr));
}

/** Returns the Internet address */
public InetAddress
getAddress() {
	String s = toDottedQuad(addr);
	try {
		return InetAddress.getByName(s);
	}
	catch (UnknownHostException e) {
		return null;
	}
}

void
rrToWire(DataByteOutputStream out, Compression c, boolean canonical) {
	out.writeByte(((addr >>> 24) & 0xFF));
	out.writeByte(((addr >>> 16) & 0xFF));
	out.writeByte(((addr >>> 8) & 0xFF));
	out.writeByte((addr & 0xFF));
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

QDox parse error in file://<WORKSPACE>/data/code-rep-dataset/Dataset2/Tasks/2019.java