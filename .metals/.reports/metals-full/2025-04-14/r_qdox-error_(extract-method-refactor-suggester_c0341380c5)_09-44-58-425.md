error id: file://<WORKSPACE>/data/code-rep-dataset/Dataset2/Tasks/4911.java
file://<WORKSPACE>/data/code-rep-dataset/Dataset2/Tasks/4911.java
### com.thoughtworks.qdox.parser.ParseException: syntax error @[1,1]

error in qdox parser
file content:
```java
offset: 1
uri: file://<WORKSPACE>/data/code-rep-dataset/Dataset2/Tasks/4911.java
text:
```scala
t@@hrow st.exception("no text format defined for TKEY");

// Copyright (c) 1999 Brian Wellington (bwelling@xbill.org)
// Portions Copyright (c) 1999 Network Associates, Inc.

package org.xbill.DNS;

import java.io.*;
import java.util.*;
import java.text.*;
import org.xbill.DNS.utils.*;

/**
 * Transaction Key - used to compute and/or securely transport a shared
 * secret to be used with TSIG.
 * @see TSIG
 *
 * @author Brian Wellington
 */

public class TKEYRecord extends Record {

private static TKEYRecord member = new TKEYRecord();

private Name alg;
private Date timeInception;
private Date timeExpire;
private short mode, error;
private byte [] key;
private byte [] other;

/** The key is assigned by the server (unimplemented) */
public static final short SERVERASSIGNED	= 1;

/** The key is computed using a Diffie-Hellman key exchange */
public static final short DIFFIEHELLMAN		= 2;

/** The key is computed using GSS_API (unimplemented) */
public static final short GSSAPI		= 3;

/** The key is assigned by the resolver (unimplemented) */
public static final short RESOLVERASSIGNED	= 4;

/** The key should be deleted */
public static final short DELETE		= 5;

private
TKEYRecord() {}

private
TKEYRecord(Name name, short dclass, int ttl) {
	super(name, Type.TKEY, dclass, ttl);
}

static TKEYRecord
getMember() {
	return member;
}

/**
 * Creates a TKEY Record from the given data.
 * @param alg The shared key's algorithm
 * @param timeInception The beginning of the validity period of the shared
 * secret or keying material
 * @param timeExpire The end of the validity period of the shared
 * secret or keying material
 * @param mode The mode of key agreement
 * @param error The extended error field.  Should be 0 in queries
 * @param key The shared secret
 * @param other The other data field.  Currently unused
 * responses.
 */
public
TKEYRecord(Name name, short dclass, int ttl, Name alg,
	   Date timeInception, Date timeExpire, short mode, short error,
	   byte [] key, byte other[])
{
	this(name, dclass, ttl);
	if (!alg.isAbsolute())
		throw new RelativeNameException(alg);
	this.alg = alg;
	this.timeInception = timeInception;
	this.timeExpire = timeExpire;
	this.mode = mode;
	this.error = error;
	this.key = key;
	this.other = other;
}

Record
rrFromWire(Name name, short type, short dclass, int ttl, int length,
	   DataByteInputStream in)
throws IOException
{
	TKEYRecord rec = new TKEYRecord(name, dclass, ttl);
	if (in == null)
		return rec;
	rec.alg = new Name(in);
	rec.timeInception = new Date(1000 * (long)in.readInt());
	rec.timeExpire = new Date(1000 * (long)in.readInt());
	rec.mode = in.readShort();
	rec.error = in.readShort();

	int keylen = in.readUnsignedShort();
	if (keylen > 0) {
		rec.key = new byte[keylen];
		in.read(rec.key);
	}
	else
		rec.key = null;

	int otherlen = in.readUnsignedShort();
	if (otherlen > 0) {
		rec.other = new byte[otherlen];
		in.read(rec.other);
	}
	else
		rec.other = null;
	return rec;
}

Record
rdataFromString(Name name, short dclass, int ttl, Tokenizer st, Name origin)
throws IOException
{
	throw new TextParseException("no text format defined for TKEY");
}

protected String
modeString() {
	switch (mode) {
		case SERVERASSIGNED:	return "SERVERASSIGNED";
		case DIFFIEHELLMAN:	return "DIFFIEHELLMAN";
		case GSSAPI:		return "GSSAPRESOLVERASSIGNED";
		case RESOLVERASSIGNED:	return "RESOLVERASSIGNED";
		case DELETE:		return "DELETE";
		default:		return new Short(mode).toString();
	}
}

/** Converts rdata to a String */
public String
rdataToString() {
	StringBuffer sb = new StringBuffer();
	if (alg == null)
		return sb.toString();

	sb.append(alg);
	sb.append(" ");
	if (Options.check("multiline"))
		sb.append("(\n\t");
	sb.append(SIGRecord.formatDate(timeInception));
	sb.append(" ");
	sb.append(SIGRecord.formatDate(timeExpire));
	sb.append(" ");
	sb.append(modeString());
	sb.append(" ");
	sb.append(Rcode.TSIGstring(error));
	if (Options.check("multiline")) {
		sb.append("\n");
		if (key != null) {
			sb.append(base64.formatString(key, 64, "\t", false));
			sb.append("\n");
		}
		if (other != null)
			sb.append(base64.formatString(other, 64, "\t", false));
		sb.append(" )");
	} else {
		sb.append(" ");
		if (key != null) {
			sb.append(base64.toString(key));
			sb.append(" ");
		}
		if (other != null)
			sb.append(base64.toString(other));
	}
	return sb.toString();
}

/** Returns the shared key's algorithm */
public Name
getAlgorithm() {
	return alg;
}

/**
 * Returns the beginning of the validity period of the shared secret or
 * keying material
 */
public Date
getTimeInception() {
	return timeInception;
}

/**
 * Returns the end of the validity period of the shared secret or
 * keying material
 */
public Date
getTimeExpire() {
	return timeExpire;
}

/** Returns the key agreement mode */
public short
getMode() {
	return mode;
}

/** Returns the extended error */
public short
getError() {
	return error;
}

/** Returns the shared secret or keying material */
public byte []
getKey() {
	return key;
}

/** Returns the other data */
public byte []
getOther() {
	return other;
}

void
rrToWire(DataByteOutputStream out, Compression c, boolean canonical) {
	if (alg == null)
		return;

	alg.toWire(out, null, canonical);

	out.writeInt((int)(timeInception.getTime() / 1000));
	out.writeInt((int)(timeExpire.getTime() / 1000));

	out.writeShort(mode);
	out.writeShort(error);

	if (key != null) {
		out.writeShort((short)key.length);
		out.writeArray(key);
	}
	else
		out.writeShort(0);

	if (other != null) {
		out.writeShort((short)other.length);
		out.writeArray(other);
	}
	else
		out.writeShort(0);
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

QDox parse error in file://<WORKSPACE>/data/code-rep-dataset/Dataset2/Tasks/4911.java