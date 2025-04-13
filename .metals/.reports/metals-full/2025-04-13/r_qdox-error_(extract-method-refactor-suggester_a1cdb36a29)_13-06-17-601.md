error id: file://<WORKSPACE>/data/code-rep-dataset/Dataset2/Tasks/5202.java
file://<WORKSPACE>/data/code-rep-dataset/Dataset2/Tasks/5202.java
### com.thoughtworks.qdox.parser.ParseException: syntax error @[1,21]

error in qdox parser
file content:
```java
offset: 21
uri: file://<WORKSPACE>/data/code-rep-dataset/Dataset2/Tasks/5202.java
text:
```scala
public static final b@@yte BADMODE	= 19;

// Copyright (c) 1999 Brian Wellington (bwelling@xbill.org)
// Portions Copyright (c) 1999 Network Associates, Inc.

package org.xbill.DNS;

import org.xbill.DNS.utils.*;

/**
 * Constants and functions relating to DNS rcodes (error values)
 *
 * @author Brian Wellington
 */

public final class Rcode {

private static StringValueTable rcodes = new StringValueTable();
private static StringValueTable tsigrcodes = new StringValueTable();

/** No error */
public static final byte NOERROR	= 0;

/** Format error */
public static final byte FORMERR	= 1;

/** Server failure */
public static final byte SERVFAIL	= 2;

/** The name does not exist */
public static final byte NXDOMAIN	= 3;

/** The operation requested is not implemented */
public static final byte NOTIMPL	= 4;

/** The operation was refused by the server */
public static final byte REFUSED	= 5;

/** The name exists */
public static final byte YXDOMAIN	= 6;

/** The RRset (name, type) exists */
public static final byte YXRRSET	= 7;

/** The RRset (name, type) does not exist */
public static final byte NXRRSET	= 8;

/** The requestor is not authorized to perform this operation */
public static final byte NOTAUTH	= 9;

/** The zone specified is not a zone */
public static final byte NOTZONE	= 10;

/* EDNS extended rcodes */
/** Unsupported EDNS level */
public static final byte BADVERS	= 16;

/* TSIG/TKEY only rcodes */
/** The signature is invalid (TSIG/TKEY extended error) */
public static final byte BADSIG		= 16;

/** The key is invalid (TSIG/TKEY extended error) */
public static final byte BADKEY		= 17;

/** The time is out of range (TSIG/TKEY extended error) */
public static final byte BADTIME	= 18;

/** The mode is invalid (TKEY extended error) */
public static final byte BADMODE	= 18;

static {
	rcodes.put2(NOERROR, "NOERROR");
	rcodes.put2(FORMERR, "FORMERR");
	rcodes.put2(SERVFAIL, "SERVFAIL");
	rcodes.put2(NXDOMAIN, "NXDOMAIN");
	rcodes.put2(NOTIMPL, "NOTIMPL");
	rcodes.put2(REFUSED, "REFUSED");
	rcodes.put2(YXDOMAIN, "YXDOMAIN");
	rcodes.put2(YXRRSET, "YXRRSET");
	rcodes.put2(NXRRSET, "NXRRSET");
	rcodes.put2(NOTAUTH, "NOTAUTH");
	rcodes.put2(NOTZONE, "NOTZONE");
	rcodes.put2(BADVERS, "BADVERS");

	tsigrcodes.put2(BADSIG, "BADSIG");
	tsigrcodes.put2(BADKEY, "BADKEY");
	tsigrcodes.put2(BADTIME, "BADTIME");
	tsigrcodes.put2(BADMODE, "BADMODE");
}

private
Rcode() {}

/** Converts a numeric Rcode into a String */
public static String
string(int i) {
	String s = rcodes.getString(i);
	return (s != null) ? s : new Integer(i).toString();
}

/** Converts a numeric TSIG extended Rcode into a String */
public static String
TSIGstring(int i) {
	String s = tsigrcodes.getString(i);
	if (s != null)
		return s;
	s = rcodes.getString(i);
	return (s != null) ? s : new Integer(i).toString();
}

/** Converts a String representation of an Rcode into its numeric value */
public static byte
value(String s) {
	byte i = (byte) rcodes.getValue(s.toUpperCase());
	if (i >= 0)
		return i;
	try {
		return Byte.parseByte(s);
	}
	catch (Exception e) {
		return (-1);
	}
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

QDox parse error in file://<WORKSPACE>/data/code-rep-dataset/Dataset2/Tasks/5202.java