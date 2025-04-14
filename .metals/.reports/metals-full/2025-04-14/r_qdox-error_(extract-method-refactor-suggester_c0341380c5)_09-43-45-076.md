error id: file://<WORKSPACE>/data/code-rep-dataset/Dataset2/Tasks/3378.java
file://<WORKSPACE>/data/code-rep-dataset/Dataset2/Tasks/3378.java
### com.thoughtworks.qdox.parser.ParseException: syntax error @[1,1]

error in qdox parser
file content:
```java
offset: 1
uri: file://<WORKSPACE>/data/code-rep-dataset/Dataset2/Tasks/3378.java
text:
```scala
o@@ut.writeUnsignedInt(sig.getOrigTTL());

// Copyright (c) 1999 Brian Wellington (bwelling@xbill.org)
// Portions Copyright (c) 1999 Network Associates, Inc.

package org.xbill.DNS;

import java.util.*;

import org.xbill.DNS.utils.*;

/**
 * Constants and functions relating to DNSSEC (algorithm constants).
 * DNSSEC provides authentication for DNS information.  RRsets are
 * signed by an appropriate key, and a SIG record is added to the set.
 * A KEY record is obtained from DNS and used to validate the signature,
 * The KEY record must also be validated or implicitly trusted - to
 * validate a key requires a series of validations leading to a trusted
 * key.  The key must also be authorized to sign the data.
 * @see SIGRecord
 * @see KEYRecord
 * @see RRset
 *
 * @author Brian Wellington
 */

public class DNSSEC {

private
DNSSEC() { }

public static final byte RSAMD5 = 1;
public static final byte RSA = RSAMD5;
public static final byte DH = 2;
public static final byte DSA = 3;
public static final byte RSASHA1 = 5;

public static final byte Failed = -1;
public static final byte Insecure = 0;
public static final byte Secure = 1;

private static void
digestSIG(DataByteOutputStream out, SIGRecord sig) {
	out.writeShort(sig.getTypeCovered());
	out.writeByte(sig.getAlgorithm());
	out.writeByte(sig.getLabels());
	out.writeInt(sig.getOrigTTL());
	out.writeInt((int) (sig.getExpire().getTime() / 1000));
	out.writeInt((int) (sig.getTimeSigned().getTime() / 1000));
	out.writeShort(sig.getFootprint());
	sig.getSigner().toWireCanonical(out);
}

/**
 * Creates an array containing fields of the SIG record and the RRsets to
 * be signed/verified.
 * @param sig The SIG record used to sign/verify the rrset.
 * @param rrset The data to be signed/verified.
 * @return The data to be cryptographically signed or verified.
 */
public static byte []
digestRRset(SIGRecord sig, RRset rrset) {
	DataByteOutputStream out = new DataByteOutputStream();
	digestSIG(out, sig);

	int size = rrset.size();
	byte [][] records = new byte[size][];

	Iterator it = rrset.rrs();
	Name name = rrset.getName();
	Name wild = null;
	if (name.labels() > sig.getLabels())
		wild = name.wild(name.labels() - sig.getLabels());
	while (it.hasNext()) {
		Record rec = (Record) it.next();
		if (wild != null)
			rec = rec.withName(wild);
		records[--size] = rec.toWireCanonical();
	}
	Arrays.sort(records);
	for (int i = 0; i < records.length; i++)
		out.writeArray(records[i]);
	return out.toByteArray();
}

/**
 * Creates an array containing fields of the SIG record and the message to
 * be signed.
 * @param sig The SIG record used to sign/verify the rrset.
 * @param msg The message to be signed/verified.
 * @param previous If this is a response, the signature from the query.
 * @return The data to be cryptographically signed or verified.
 */
public static byte []
digestMessage(SIGRecord sig, Message msg, byte [] previous) {
	DataByteOutputStream out = new DataByteOutputStream();
	digestSIG(out, sig);

	if (previous != null)
		out.writeArray(previous);
	
	msg.toWire(out);
	return out.toByteArray();
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

QDox parse error in file://<WORKSPACE>/data/code-rep-dataset/Dataset2/Tasks/3378.java