error id: file://<WORKSPACE>/data/code-rep-dataset/Dataset2/Tasks/9212.java
file://<WORKSPACE>/data/code-rep-dataset/Dataset2/Tasks/9212.java
### com.thoughtworks.qdox.parser.ParseException: syntax error @[1,1]

error in qdox parser
file content:
```java
offset: 1
uri: file://<WORKSPACE>/data/code-rep-dataset/Dataset2/Tasks/9212.java
text:
```scala
t@@hrow st.exception("no text format defined for TSIG");

// Copyright (c) 1999 Brian Wellington (bwelling@xbill.org)
// Portions Copyright (c) 1999 Network Associates, Inc.

package org.xbill.DNS;

import java.io.*;
import java.util.*;
import java.text.*;
import org.xbill.DNS.utils.*;

/**
 * Transaction Signature - this record is automatically generated by the
 * resolver.  TSIG records provide transaction security between the
 * sender and receiver of a message, using a shared key.
 * @see Resolver
 * @see TSIG
 *
 * @author Brian Wellington
 */

public class TSIGRecord extends Record {

private static TSIGRecord member = new TSIGRecord();

private Name alg;
private Date timeSigned;
private short fudge;
private byte [] signature;
private int originalID;
private short error;
private byte [] other;

private
TSIGRecord() {} 

private
TSIGRecord(Name name, short dclass, int ttl) {
	super(name, Type.TSIG, dclass, ttl);
}

static TSIGRecord
getMember() {
	return member;
}

/**
 * Creates a TSIG Record from the given data.  This is normally called by
 * the TSIG class
 * @param alg The shared key's algorithm
 * @param timeSigned The time that this record was generated
 * @param fudge The fudge factor for time - if the time that the message is
 * received is not in the range [now - fudge, now + fudge], the signature
 * fails
 * @param signature The signature
 * @param originalID The message ID at the time of its generation
 * @param error The extended error field.  Should be 0 in queries.
 * @param other The other data field.  Currently used only in BADTIME
 * responses.
 * @see TSIG
 */
public
TSIGRecord(Name name, short dclass, int ttl, Name alg, Date timeSigned,
	   short fudge, byte [] signature, int originalID, short error,
	   byte other[])
{
	this(name, dclass, ttl);
	if (!alg.isAbsolute())
		throw new RelativeNameException(alg);
	this.alg = alg;
	this.timeSigned = timeSigned;
	this.fudge = fudge;
	this.signature = signature;
	this.originalID = originalID;
	this.error = error;
	this.other = other;
}

Record
rrFromWire(Name name, short type, short dclass, int ttl, int length,
	   DataByteInputStream in)
throws IOException
{
	TSIGRecord rec = new TSIGRecord(name, dclass, ttl);
	if (in == null)
		return rec;
	rec.alg = new Name(in);

	short timeHigh = in.readShort();
	int timeLow = in.readInt();
	long time = ((long)timeHigh & 0xFFFF) << 32;
	time += (long)timeLow & 0xFFFFFFFF;
	rec.timeSigned = new Date(time * 1000);
	rec.fudge = in.readShort();

	int sigLen = in.readUnsignedShort();
	rec.signature = new byte[sigLen];
	in.read(rec.signature);

	rec.originalID = in.readUnsignedShort();
	rec.error = in.readShort();

	int otherLen = in.readUnsignedShort();
	if (otherLen > 0) {
		rec.other = new byte[otherLen];
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
	throw new TextParseException("no text format defined for TSIG");
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

	sb.append (timeSigned.getTime() / 1000);
	sb.append (" ");
	sb.append (fudge);
	sb.append (" ");
	sb.append (signature.length);
	if (Options.check("multiline")) {
		sb.append ("\n");
		sb.append (base64.formatString(signature, 64, "\t", false));
	} else {
		sb.append (" ");
		sb.append (base64.toString(signature));
	}
	sb.append (Rcode.TSIGstring(error));
	sb.append (" ");
	if (other == null)
		sb.append (0);
	else {
		sb.append (other.length);
		if (Options.check("multiline"))
			sb.append("\n\n\n\t");
		else
			sb.append(" ");
		if (error == Rcode.BADTIME) {
			if (other.length != 6) {
				sb.append("<invalid BADTIME other data>");
			} else {
				long time = ((other[0] & 0xFF) << 40) +
					    ((other[1] & 0xFF) << 32) +
					    ((other[2] & 0xFF) << 24) +
					    ((other[3] & 0xFF) << 16) +
					    ((other[4] & 0xFF) << 8) +
					    ((other[5] & 0xFF)     );
				sb.append("<server time: ");
				sb.append(new Date(time * 1000));
				sb.append(">");
			}
		} else
			sb.append(base64.toString(other));
		sb.append(">");
	}
	if (Options.check("multiline"))
		sb.append(" )");
	return sb.toString();
}

/** Returns the shared key's algorithm */
public Name
getAlgorithm() {
	return alg;
}

/** Returns the time that this record was generated */
public Date
getTimeSigned() {
	return timeSigned;
}

/** Returns the time fudge factor */
public short
getFudge() {
	return fudge;
}

/** Returns the signature */
public byte []
getSignature() {
	return signature;
}

/** Returns the original message ID */
public int
getOriginalID() {
	return originalID;
}

/** Returns the extended error */
public short
getError() {
	return error;
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

	long time = timeSigned.getTime() / 1000;
	short timeHigh = (short) (time >> 32);
	int timeLow = (int) (time);
	out.writeShort(timeHigh);
	out.writeInt(timeLow);
	out.writeShort(fudge);

	out.writeShort((short)signature.length);
	out.writeArray(signature);

	out.writeShort(originalID);
	out.writeShort(error);

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

QDox parse error in file://<WORKSPACE>/data/code-rep-dataset/Dataset2/Tasks/9212.java