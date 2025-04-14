error id: file://<WORKSPACE>/data/code-rep-dataset/Dataset2/Tasks/3509.java
file://<WORKSPACE>/data/code-rep-dataset/Dataset2/Tasks/3509.java
### com.thoughtworks.qdox.parser.ParseException: syntax error @[1,1]

error in qdox parser
file content:
```java
offset: 1
uri: file://<WORKSPACE>/data/code-rep-dataset/Dataset2/Tasks/3509.java
text:
```scala
r@@ec.cert = st.getBase64();

// Copyright (c) 1999-2004 Brian Wellington (bwelling@xbill.org)

package org.xbill.DNS;

import java.io.*;
import org.xbill.DNS.utils.*;

/**
 * Certificate Record  - Stores a certificate associated with a name.  The
 * certificate might also be associated with a KEYRecord.
 * @see KEYRecord
 *
 * @author Brian Wellington
 */

public class CERTRecord extends Record {

/** PKIX (X.509v3) */
public static final int PKIX = 1;

/** Simple Public Key Infrastructure  */
public static final int SPKI = 2;

/** Pretty Good Privacy */
public static final int PGP = 3;

/** Certificate stored in a URL */
public static final int URL = 253;

/** Object ID (private) */
public static final int OID = 254;

private static CERTRecord member = new CERTRecord();

private int certType, keyTag;
private int alg;
private byte [] cert;

private
CERTRecord() {}

private
CERTRecord(Name name, int dclass, long ttl) {
	super(name, Type.CERT, dclass, ttl);
}

static CERTRecord
getMember() {
	return member;
}

/**
 * Creates a CERT Record from the given data
 * @param certType The type of certificate (see constants)
 * @param keyTag The ID of the associated KEYRecord, if present
 * @param alg The algorithm of the associated KEYRecord, if present
 * @param cert Binary data representing the certificate
 */
public
CERTRecord(Name name, int dclass, long ttl, int certType, int keyTag,
	   int alg, byte []  cert)
{
	this(name, dclass, ttl);
	checkU16("certType", certType);
	checkU16("keyTag", keyTag);
	checkU8("alg", alg);
	this.certType = certType;
	this.keyTag = keyTag;
	this.alg = alg;
	this.cert = cert;
}

Record
rrFromWire(Name name, int type, int dclass, long ttl, int length,
	   DataByteInputStream in)
throws IOException
{
	CERTRecord rec = new CERTRecord(name, dclass, ttl);
	if (in == null)
		return rec;
	rec.certType = in.readShort();
	rec.keyTag = in.readUnsignedShort();
	rec.alg = in.readByte();
	if (length > 5) {
		rec.cert = new byte[length - 5];
		in.read(rec.cert);
	}
	return rec;
}

Record
rdataFromString(Name name, int dclass, long ttl, Tokenizer st, Name origin)
throws IOException
{
	CERTRecord rec = new CERTRecord(name, dclass, ttl);
	rec.certType = st.getUInt16();
	rec.keyTag = st.getUInt16();
	rec.alg = st.getUInt8();
	rec.cert = base64.fromString(remainingStrings(st));
	return rec;
}

/**
 * Converts rdata to a String
 */
public String
rdataToString() {
	StringBuffer sb = new StringBuffer();
	if (cert != null) {
		sb.append (certType);
		sb.append (" ");
		sb.append (keyTag);
		sb.append (" ");
		sb.append (alg);
		if (cert != null) {
			if (Options.check("multiline")) {
				sb.append(" (\n");
				sb.append(base64.formatString(cert, 64,
							      "\t", true));
			} else {
				sb.append(" ");
				sb.append(base64.toString(cert));
			}
		}
	}
	return sb.toString();
}

/**
 * Returns the type of certificate
 */
public int
getCertType() {
	return certType;
}

/**
 * Returns the ID of the associated KEYRecord, if present
 */
public int
getKeyTag() {
	return keyTag;
}

/**
 * Returns the algorithm of the associated KEYRecord, if present
 */
public int
getAlgorithm() {
	return alg;
}

/**
 * Returns the binary representation of the certificate
 */
public byte []
getCert() {
	return cert;
}

void
rrToWire(DataByteOutputStream out, Compression c, boolean canonical) {
	if (cert == null)
		return;

	out.writeShort(certType);
	out.writeShort(keyTag);
	out.writeByte(alg);
	out.writeArray(cert);
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
	scala.collection.parallel.AdaptiveWorkStealingTasks$AWSTWrappedTask.compute(Tasks.scala:152)
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

QDox parse error in file://<WORKSPACE>/data/code-rep-dataset/Dataset2/Tasks/3509.java