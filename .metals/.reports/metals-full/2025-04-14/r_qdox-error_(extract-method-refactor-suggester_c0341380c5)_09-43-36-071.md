error id: file://<WORKSPACE>/data/code-rep-dataset/Dataset2/Tasks/9161.java
file://<WORKSPACE>/data/code-rep-dataset/Dataset2/Tasks/9161.java
### com.thoughtworks.qdox.parser.ParseException: syntax error @[1,16]

error in qdox parser
file content:
```java
offset: 16
uri: file://<WORKSPACE>/data/code-rep-dataset/Dataset2/Tasks/9161.java
text:
```scala
private static S@@hort [] typecache = new Short [44];

// Copyright (c) 1999 Brian Wellington (bwelling@xbill.org)
// Portions Copyright (c) 1999 Network Associates, Inc.

package org.xbill.DNS;

import java.util.HashMap;
import org.xbill.DNS.utils.*;

/**
 * Constants and functions relating to DNS Types
 *
 * @author Brian Wellington
 */

public final class Type {

/** Address */
public static final short A		= 1;

/** Name server */
public static final short NS		= 2;

/** Mail destination */
public static final short MD		= 3;

/** Mail forwarder */
public static final short MF		= 4;

/** Canonical name (alias) */
public static final short CNAME		= 5;

/** Start of authority */
public static final short SOA		= 6;

/** Mailbox domain name */
public static final short MB		= 7;

/** Mail group member */
public static final short MG		= 8;

/** Mail rename name */
public static final short MR		= 9;

/** Null record */
public static final short NULL		= 10;

/** Well known services */
public static final short WKS		= 11;

/** Domain name pointer */
public static final short PTR		= 12;

/** Host information */
public static final short HINFO		= 13;

/** Mailbox information */
public static final short MINFO		= 14;

/** Mail routing information */
public static final short MX		= 15;

/** Text strings */
public static final short TXT		= 16;

/** Responsible person */
public static final short RP		= 17;

/** AFS cell database */
public static final short AFSDB		= 18;

/** X_25 calling address */
public static final short X25		= 19;

/** ISDN calling address */
public static final short ISDN		= 20;

/** Router */
public static final short RT		= 21;

/** NSAP address */
public static final short NSAP		= 22;

/** Reverse NSAP address (deprecated) */
public static final short NSAP_PTR	= 23;

/** Signature */
public static final short SIG		= 24;

/** Key */
public static final short KEY		= 25;

/** X.400 mail mapping */
public static final short PX		= 26;

/** Geographical position (withdrawn) */
public static final short GPOS		= 27;

/** IPv6 address (old) */
public static final short AAAA		= 28;

/** Location */
public static final short LOC		= 29;

/** Next valid name in zone */
public static final short NXT		= 30;

/** Endpoint identifier */
public static final short EID		= 31;

/** Nimrod locator */
public static final short NIMLOC	= 32;

/** Server selection */
public static final short SRV		= 33;

/** ATM address */
public static final short ATMA		= 34;

/** Naming authority pointer */
public static final short NAPTR		= 35;

/** Key exchange */
public static final short KX		= 36;

/** Certificate */
public static final short CERT		= 37;

/** IPv6 address */
public static final short A6		= 38;

/** Non-terminal name redirection */
public static final short DNAME		= 39;

/** Options - contains EDNS metadata */
public static final short OPT		= 41;

/** Address Prefix List */
public static final short APL		= 42;

/** Delegation Signer */
public static final short DS		= 43;

/** Transaction key - used to compute a shared secret or exchange a key */
public static final short TKEY		= 249;

/** Transaction signature */
public static final short TSIG		= 250;

/** Incremental zone transfer */
public static final short IXFR		= 251;

/** Zone transfer */
public static final short AXFR		= 252;

/** Transfer mailbox records */
public static final short MAILB		= 253;

/** Transfer mail agent records */
public static final short MAILA		= 254;

/** Matches any type */
public static final short ANY           = 255;

private static class DoubleHashMap {
	private HashMap v2s, s2v;

	public
	DoubleHashMap() {
		v2s = new HashMap();
		s2v = new HashMap();
	}

	public void
	put(short value, String string) {
		Short s = Type.toShort(value);
		v2s.put(s, string);
		s2v.put(string, s);
	}

	public Short
	getValue(String string) {
		return (Short) s2v.get(string);
	}

	public String
	getString(short value) {
		return (String) v2s.get(Type.toShort(value));
	}
}

private static DoubleHashMap types = new DoubleHashMap();
private static Short [] typecache = new Short [43];

static {
	for (short i = 0; i < typecache.length; i++)
		typecache[i] = new Short(i);
	types.put(A, "A");
	types.put(NS, "NS");
	types.put(MD, "MD");
	types.put(MF, "MF");
	types.put(CNAME, "CNAME");
	types.put(SOA, "SOA");
	types.put(MB, "MB");
	types.put(MG, "MG");
	types.put(MR, "MR");
	types.put(NULL, "NULL");
	types.put(WKS, "WKS");
	types.put(PTR, "PTR");
	types.put(HINFO, "HINFO");
	types.put(MINFO, "MINFO");
	types.put(MX, "MX");
	types.put(TXT, "TXT");
	types.put(RP, "RP");
	types.put(AFSDB, "AFSDB");
	types.put(X25, "X25");
	types.put(ISDN, "ISDN");
	types.put(RT, "RT");
	types.put(NSAP, "NSAP");
	types.put(NSAP_PTR, "NSAP_PTR");
	types.put(SIG, "SIG");
	types.put(KEY, "KEY");
	types.put(PX, "PX");
	types.put(GPOS, "GPOS");
	types.put(AAAA, "AAAA");
	types.put(LOC, "LOC");
	types.put(NXT, "NXT");
	types.put(EID, "EID");
	types.put(NIMLOC, "NIMLOC");
	types.put(SRV, "SRV");
	types.put(ATMA, "ATMA");
	types.put(NAPTR, "NAPTR");
	types.put(KX, "KX");
	types.put(CERT, "CERT");
	types.put(A6, "A6");
	types.put(DNAME, "DNAME");
	types.put(OPT, "OPT");
	types.put(APL, "APL");
	types.put(DS, "DS");
	types.put(TKEY, "TKEY");
	types.put(TSIG, "TSIG");
	types.put(IXFR, "IXFR");
	types.put(AXFR, "AXFR");
	types.put(MAILB, "MAILB");
	types.put(MAILA, "MAILA");
	types.put(ANY, "ANY");
}

private
Type() {
}

/** Converts a numeric Type into a String */
public static String
string(short i) {
	String s = types.getString(i);
	return (s != null) ? s : ("TYPE" + i);
}

/** Converts a String representation of an Type into its numeric value */
public static short
value(String s) {
	Short val = types.getValue(s.toUpperCase());
	if (val != null)
		return val.shortValue();
	try {
		if (s.toUpperCase().startsWith("TYPE"))
			return (Short.parseShort(s.substring(4)));
		else
			return Short.parseShort(s);
	}
	catch (Exception e) {
		return (-1);
	}
}

/** Is this type valid for a record (a non-meta type)? */
public static boolean
isRR(int type) {
	switch (type) {
		case OPT:
		case TKEY:
		case TSIG:
		case IXFR:
		case AXFR:
		case MAILB:
		case MAILA:
		case ANY:
			return false;
		default:
			return true;
	}
}

/* Converts a type into a Short, for use in Hashmaps, etc. */
static Short
toShort(short type) {
	if (type < typecache.length)
		return (typecache[type]);
	return new Short(type);
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

QDox parse error in file://<WORKSPACE>/data/code-rep-dataset/Dataset2/Tasks/9161.java