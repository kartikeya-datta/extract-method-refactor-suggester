error id: file://<WORKSPACE>/data/code-rep-dataset/Dataset2/Tasks/5564.java
file://<WORKSPACE>/data/code-rep-dataset/Dataset2/Tasks/5564.java
### com.thoughtworks.qdox.parser.ParseException: syntax error @[1,1]

error in qdox parser
file content:
```java
offset: 1
uri: file://<WORKSPACE>/data/code-rep-dataset/Dataset2/Tasks/5564.java
text:
```scala
r@@ec.key = base64.fromString(remainingStrings(st));

// Copyright (c) 1999 Brian Wellington (bwelling@xbill.org)
// Portions Copyright (c) 1999 Network Associates, Inc.

package org.xbill.DNS;

import java.io.*;
import java.util.*;
import org.xbill.DNS.utils.*;

/**
 * Key - contains a cryptographic public key.  The data can be converted
 * to objects implementing java.security.interfaces.PublicKey
 * @see DNSSEC
 *
 * @author Brian Wellington
 */

public class KEYRecord extends Record {

private static KEYRecord member = new KEYRecord();

private short flags;
private byte proto, alg;
private byte [] key;
private int footprint = -1;

/* flags */
/** This key cannot be used for confidentiality (encryption) */
public static final int FLAG_NOCONF = 0x8000;

/** This key cannot be used for authentication */
public static final int FLAG_NOAUTH = 0x4000;

/** This key cannot be used for authentication or confidentiality */
public static final int FLAG_NOKEY = 0xC000;

/** A zone key */
public static final int OWNER_ZONE = 0x0100;

/** A host/end entity key */
public static final int OWNER_HOST = 0x0200;

/** A user key */
public static final int OWNER_USER = 0x0000;

/* protocols */
/** Key was created for use with transaction level security */
public static final int PROTOCOL_TLS = 1;

/** Key was created for use with email */
public static final int PROTOCOL_EMAIL = 2;

/** Key was created for use with DNSSEC */
public static final int PROTOCOL_DNSSEC = 3;

/** Key was created for use with IPSEC */
public static final int PROTOCOL_IPSEC = 4;

/** Key was created for use with any protocol */
public static final int PROTOCOL_ANY = 255;

private
KEYRecord() {}

private
KEYRecord(Name name, short dclass, int ttl) {
	super(name, Type.KEY, dclass, ttl);
}

static KEYRecord
getMember() {
	return member;
}

/**
 * Creates a KEY Record from the given data
 * @param flags Flags describing the key's properties
 * @param proto The protocol that the key was created for
 * @param alg The key's algorithm
 * @param key Binary data representing the key
 */
public
KEYRecord(Name name, short dclass, int ttl, int flags, int proto, int alg,
	  byte []  key)
{
	this(name, dclass, ttl);
	this.flags = (short) flags;
	this.proto = (byte) proto;
	this.alg = (byte) alg;
	this.key = key;
}

Record
rrFromWire(Name name, short type, short dclass, int ttl, int length,
	   DataByteInputStream in)
throws IOException
{
	KEYRecord rec = new KEYRecord(name, dclass, ttl);
	if (in == null)
		return rec;
	rec.flags = in.readShort();
	rec.proto = in.readByte();
	rec.alg = in.readByte();
	if (length > 4) {
		rec.key = new byte[length - 4];
		in.read(rec.key);
	}
	return rec;
}

Record
rdataFromString(Name name, short dclass, int ttl, MyStringTokenizer st,
		Name origin)
throws TextParseException
{
	KEYRecord rec = new KEYRecord(name, dclass, ttl);
	rec.flags = (short) Integer.decode(nextString(st)).intValue();
	rec.proto = (byte) Integer.parseInt(nextString(st));
	rec.alg = (byte) Integer.parseInt(nextString(st));
	/* If this is a null key, there's no key data */
	if (!((rec.flags & (FLAG_NOKEY)) == (FLAG_NOKEY)))
		rec.key = base64.fromString(st.remainingTokens());
	else
		rec.key = null;
	return rec;
}

/**
 * Converts rdata to a String
 */
public String
rdataToString() {
	StringBuffer sb = new StringBuffer();
	if (key != null || (flags & (FLAG_NOKEY)) == (FLAG_NOKEY) ) {
		if (!Options.check("nohex")) {
			sb.append("0x");
			sb.append(Integer.toHexString(flags & 0xFFFF));
		}
		else
			sb.append(flags & 0xFFFF);
		sb.append(" ");
		sb.append(proto & 0xFF);
		sb.append(" ");
		sb.append(alg & 0xFF);
		if (key != null) {
			sb.append(" (\n");
			sb.append(base64.formatString(key, 64, "\t", true));
			sb.append(" ; key_tag = ");
			sb.append(getFootprint() & 0xFFFF);
		}
	}
	return sb.toString();
}

/**
 * Returns the flags describing the key's properties
 */
public short
getFlags() {
	return flags;
}

/**
 * Returns the protocol that the key was created for
 */
public byte
getProtocol() {
	return proto;
}

/**
 * Returns the key's algorithm
 */
public byte
getAlgorithm() {
	return alg;
}

/**
 * Returns the binary data representing the key
 */
public byte []
getKey() {
	return key;
}

/**
 * Returns the key's footprint (after computing it)
 */
public short
getFootprint() {
	if (footprint >= 0)
		return (short)footprint;
	
	int foot = 0;

	DataByteOutputStream out = new DataByteOutputStream();
	rrToWire(out, null, false);
	byte [] rdata = out.toByteArray();

	if (alg == DNSSEC.RSAMD5) {
		int d1 = rdata[rdata.length - 3] & 0xFF;
		int d2 = rdata[rdata.length - 2] & 0xFF;
		foot = (d1 << 8) + d2;
	}
	else {
		int i;
		for (i = 0; i < rdata.length - 1; i += 2) {
			int d1 = rdata[i] & 0xFF;
			int d2 = rdata[i + 1] & 0xFF;
			foot += ((d1 << 8) + d2);
		}
		if (i < rdata.length) {
			int d1 = rdata[i] & 0xFF;
			foot += (d1 << 8);
		}
		foot += ((foot >> 16) & 0xffff);
	}
	footprint = (foot & 0xffff);
	return (short) footprint;
}

void
rrToWire(DataByteOutputStream out, Compression c, boolean canonical) {
	if (key == null && (flags & (FLAG_NOKEY)) != (FLAG_NOKEY) )
		return;

	out.writeShort(flags);
	out.writeByte(proto);
	out.writeByte(alg);
	if (key != null)
		out.writeArray(key);
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

QDox parse error in file://<WORKSPACE>/data/code-rep-dataset/Dataset2/Tasks/5564.java