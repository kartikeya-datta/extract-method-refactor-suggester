error id: file://<WORKSPACE>/data/code-rep-dataset/Dataset2/Tasks/7969.java
file://<WORKSPACE>/data/code-rep-dataset/Dataset2/Tasks/7969.java
### com.thoughtworks.qdox.parser.ParseException: syntax error @[1,1]

error in qdox parser
file content:
```java
offset: 1
uri: file://<WORKSPACE>/data/code-rep-dataset/Dataset2/Tasks/7969.java
text:
```scala
b@@yte [] fingerprint)

// Copyright (c) 2004 Brian Wellington (bwelling@xbill.org)

package org.xbill.DNS;

import java.io.*;
import java.util.*;
import org.xbill.DNS.utils.*;

/**
 * SSH Fingerprint - stores the fingerprint of an SSH host key.
 *
 * @author Brian Wellington
 */

public class SSHFPRecord extends Record {

public static class Algorithm {
	private Algorithm() {}

	public static final int RSA = 1;
	public static final int DSS = 2;
}

public static class Digest {
	private Digest() {}

	public static final int SHA1 = 1;
}

private int alg;
private int digestType;
private byte [] fingerprint;

SSHFPRecord() {} 

Record
getObject() {
	return new SSHFPRecord();
}

/**
 * Creates an SSHFP Record from the given data.
 * @param alg The public key's algorithm.
 * @param digestType The public key's digest type.
 * @param fingerprint The public key's fingerprint.
 */
public
SSHFPRecord(Name name, int dclass, long ttl, int alg, int digestType,
	    byte [] fingetprint)
{
	super(name, Type.SSHFP, dclass, ttl);
	this.alg = checkU8("alg", alg);
	this.digestType = checkU8("digestType", digestType);
	this.fingerprint = fingerprint;
}

void
rrFromWire(DNSInput in) throws IOException {
	alg = in.readU8();
	digestType = in.readU8();
	fingerprint = in.readByteArray();
}

void
rdataFromString(Tokenizer st, Name origin) throws IOException {
	alg = st.getUInt8();
	digestType = st.getUInt8();
	fingerprint = st.getHex(true);
}

String
rrToString() {
	StringBuffer sb = new StringBuffer();
	sb.append(alg);
	sb.append(" ");
	sb.append(digestType);
	sb.append(" ");
	sb.append(base16.toString(fingerprint));
	return sb.toString();
}

/** Returns the public key's algorithm. */
public int
getAlgorithm() {
	return alg;
}

/** Returns the public key's digest type. */
public int
getDigestType() {
	return digestType;
}

/** Returns the fingerprint */
public byte []
getFingerPrint() {
	return fingerprint;
}

void
rrToWire(DNSOutput out, Compression c, boolean canonical) {
	out.writeU8(alg);
	out.writeU8(digestType);
	out.writeByteArray(fingerprint);
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

QDox parse error in file://<WORKSPACE>/data/code-rep-dataset/Dataset2/Tasks/7969.java