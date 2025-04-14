error id: file://<WORKSPACE>/data/code-rep-dataset/Dataset2/Tasks/6964.java
file://<WORKSPACE>/data/code-rep-dataset/Dataset2/Tasks/6964.java
### com.thoughtworks.qdox.parser.ParseException: syntax error @[1,1]

error in qdox parser
file content:
```java
offset: 1
uri: file://<WORKSPACE>/data/code-rep-dataset/Dataset2/Tasks/6964.java
text:
```scala
r@@rToWire(DataByteOutputStream out, Compression c) {

// Copyright (c) 1999 Brian Wellington (bwelling@xbill.org)
// Portions Copyright (c) 1999 Network Associates, Inc.

package org.xbill.DNS;

import java.net.*;
import java.io.*;
import java.util.*;
import org.xbill.DNS.utils.*;

/**
 * A6 Record - maps a domain name to an IPv6 address (experimental)
 *
 * @author Brian Wellington
 */

public class A6Record extends Record {

private static A6Record member = new A6Record();

private short prefixBits;
private Inet6Address suffix;
private Name prefix;

private
A6Record() {}

private
A6Record(Name name, short dclass, int ttl) {
	super(name, Type.A6, dclass, ttl);
}

static A6Record
getMember() {
	return member;
}

/**
 * Creates an A6 Record from the given data
 * @param prefixBits The number of bits in the address prefix
 * @param suffix The address suffix
 * @param prefix The name of the prefix
 */
public
A6Record(Name name, short dclass, int ttl, int prefixBits,
	 Inet6Address suffix, Name prefix)
throws IOException
{
	this(name, dclass, ttl);
	this.prefixBits = (short) prefixBits;
	this.suffix = suffix;
	this.prefix = prefix;
}

Record
rrFromWire(Name name, short type, short dclass, int ttl, int length,
	   DataByteInputStream in)
throws IOException
{
	A6Record rec = new A6Record(name, dclass, ttl);

	if (in == null)
		return rec;

	rec.prefixBits = in.readByte();
	int suffixbits = 128 - rec.prefixBits;
	int suffixbytes = (suffixbits + 7) / 8;
	byte [] data = new byte[suffixbytes];
	in.read(data);
	rec.suffix = new Inet6Address(128 - rec.prefixBits, data);
	if (rec.prefixBits > 0)
		rec.prefix = new Name(in);
	return rec;
}

Record
rdataFromString(Name name, short dclass, int ttl, MyStringTokenizer st,
		Name origin)
throws TextParseException
{
	A6Record rec = new A6Record(name, dclass, ttl);
	rec.prefixBits = Short.parseShort(st.nextToken());
	rec.suffix = new Inet6Address(st.nextToken());
	if (rec.prefixBits > 0)
		rec.prefix = Name.fromString(st.nextToken(), origin);
	return rec;
}

/** Converts rdata to a String */
public String
rdataToString() {
	StringBuffer sb = new StringBuffer();
	if (suffix != null) {
		sb.append(prefixBits);
		sb.append(" ");
		sb.append(suffix);
		if (prefix != null) {
			sb.append(" ");
			sb.append(prefix);
		}
	}
	return sb.toString();
}

/** Returns the number of bits in the prefix */
public short
getPrefixBits() {
	return prefixBits;
}

/** Returns the address suffix */
public Inet6Address
getSuffix() {
	return suffix;
}

/** Returns the address prefix */
public Name
getPrefix() {
	return prefix;
}

void
rrToWire(DataByteOutputStream out, Compression c) throws IOException {
	if (suffix == null)
		return;
	out.write(prefixBits);
	int suffixbits = 128 - prefixBits;
	int suffixbytes = (suffixbits + 7) / 8;
	byte [] data = suffix.toBytes();
	out.write(data, 16 - suffixbytes, suffixbytes);
	if (prefix != null)
		prefix.toWire(out, null);
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

QDox parse error in file://<WORKSPACE>/data/code-rep-dataset/Dataset2/Tasks/6964.java