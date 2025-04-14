error id: file://<WORKSPACE>/data/code-rep-dataset/Dataset2/Tasks/7192.java
file://<WORKSPACE>/data/code-rep-dataset/Dataset2/Tasks/7192.java
### com.thoughtworks.qdox.parser.ParseException: syntax error @[1,1]

error in qdox parser
file content:
```java
offset: 1
uri: file://<WORKSPACE>/data/code-rep-dataset/Dataset2/Tasks/7192.java
text:
```scala
t@@ypes.add(NSAP_PTR, "NSAP-PTR");

// Copyright (c) 1999-2004 Brian Wellington (bwelling@xbill.org)

package org.xbill.DNS;

import java.util.HashMap;

/**
 * Constants and functions relating to DNS Types
 *
 * @author Brian Wellington
 */

public final class Type {

/** Address */
public static final int A		= 1;

/** Name server */
public static final int NS		= 2;

/** Mail destination */
public static final int MD		= 3;

/** Mail forwarder */
public static final int MF		= 4;

/** Canonical name (alias) */
public static final int CNAME		= 5;

/** Start of authority */
public static final int SOA		= 6;

/** Mailbox domain name */
public static final int MB		= 7;

/** Mail group member */
public static final int MG		= 8;

/** Mail rename name */
public static final int MR		= 9;

/** Null record */
public static final int NULL		= 10;

/** Well known services */
public static final int WKS		= 11;

/** Domain name pointer */
public static final int PTR		= 12;

/** Host information */
public static final int HINFO		= 13;

/** Mailbox information */
public static final int MINFO		= 14;

/** Mail routing information */
public static final int MX		= 15;

/** Text strings */
public static final int TXT		= 16;

/** Responsible person */
public static final int RP		= 17;

/** AFS cell database */
public static final int AFSDB		= 18;

/** X.25 calling address */
public static final int X25		= 19;

/** ISDN calling address */
public static final int ISDN		= 20;

/** Router */
public static final int RT		= 21;

/** NSAP address */
public static final int NSAP		= 22;

/** Reverse NSAP address (deprecated) */
public static final int NSAP_PTR	= 23;

/** Signature */
public static final int SIG		= 24;

/** Key */
public static final int KEY		= 25;

/** X.400 mail mapping */
public static final int PX		= 26;

/** Geographical position (withdrawn) */
public static final int GPOS		= 27;

/** IPv6 address */
public static final int AAAA		= 28;

/** Location */
public static final int LOC		= 29;

/** Next valid name in zone */
public static final int NXT		= 30;

/** Endpoint identifier */
public static final int EID		= 31;

/** Nimrod locator */
public static final int NIMLOC		= 32;

/** Server selection */
public static final int SRV		= 33;

/** ATM address */
public static final int ATMA		= 34;

/** Naming authority pointer */
public static final int NAPTR		= 35;

/** Key exchange */
public static final int KX		= 36;

/** Certificate */
public static final int CERT		= 37;

/** IPv6 address (experimental) */
public static final int A6		= 38;

/** Non-terminal name redirection */
public static final int DNAME		= 39;

/** Options - contains EDNS metadata */
public static final int OPT		= 41;

/** Address Prefix List */
public static final int APL		= 42;

/** Delegation Signer */
public static final int DS		= 43;

/** SSH Key Fingerprint */
public static final int SSHFP		= 44;

/** Resource Record Signature */
public static final int RRSIG		= 46;

/** Next Secure Name */
public static final int NSEC		= 47;

/** DNSSEC Key */
public static final int DNSKEY		= 48;

/** Transaction key - used to compute a shared secret or exchange a key */
public static final int TKEY		= 249;

/** Transaction signature */
public static final int TSIG		= 250;

/** Incremental zone transfer */
public static final int IXFR		= 251;

/** Zone transfer */
public static final int AXFR		= 252;

/** Transfer mailbox records */
public static final int MAILB		= 253;

/** Transfer mail agent records */
public static final int MAILA		= 254;

/** Matches any type */
public static final int ANY		= 255;

private static class TypeMnemonic extends Mnemonic {
	public
	TypeMnemonic() {
		super("Type", CASE_UPPER);
		setPrefix("TYPE");
	}

	public void
	check(int val) {
		Type.check(val);
	}
}

private static Mnemonic types = new TypeMnemonic();

static {
	types.add(A, "A");
	types.add(NS, "NS");
	types.add(MD, "MD");
	types.add(MF, "MF");
	types.add(CNAME, "CNAME");
	types.add(SOA, "SOA");
	types.add(MB, "MB");
	types.add(MG, "MG");
	types.add(MR, "MR");
	types.add(NULL, "NULL");
	types.add(WKS, "WKS");
	types.add(PTR, "PTR");
	types.add(HINFO, "HINFO");
	types.add(MINFO, "MINFO");
	types.add(MX, "MX");
	types.add(TXT, "TXT");
	types.add(RP, "RP");
	types.add(AFSDB, "AFSDB");
	types.add(X25, "X25");
	types.add(ISDN, "ISDN");
	types.add(RT, "RT");
	types.add(NSAP, "NSAP");
	types.add(NSAP_PTR, "NSAP_PTR");
	types.add(SIG, "SIG");
	types.add(KEY, "KEY");
	types.add(PX, "PX");
	types.add(GPOS, "GPOS");
	types.add(AAAA, "AAAA");
	types.add(LOC, "LOC");
	types.add(NXT, "NXT");
	types.add(EID, "EID");
	types.add(NIMLOC, "NIMLOC");
	types.add(SRV, "SRV");
	types.add(ATMA, "ATMA");
	types.add(NAPTR, "NAPTR");
	types.add(KX, "KX");
	types.add(CERT, "CERT");
	types.add(A6, "A6");
	types.add(DNAME, "DNAME");
	types.add(OPT, "OPT");
	types.add(APL, "APL");
	types.add(DS, "DS");
	types.add(SSHFP, "SSHFP");
	types.add(RRSIG, "RRSIG");
	types.add(NSEC, "NSEC");
	types.add(DNSKEY, "DNSKEY");
	types.add(TKEY, "TKEY");
	types.add(TSIG, "TSIG");
	types.add(IXFR, "IXFR");
	types.add(AXFR, "AXFR");
	types.add(MAILB, "MAILB");
	types.add(MAILA, "MAILA");
	types.add(ANY, "ANY");
}

private
Type() {
}

static void
check(int i) {
	if (i < 0 || i > 0xFFFF)
		throw new InvalidTypeException(i);
}

/**
 * Converts a numeric Type into a String
 * @return The canonical string representation of the type
 * @throws InvalidTypeException The type is out of range.
 */
public static String
string(int i) {
	return types.getText(i);
}

/**
 * Converts a String representation of an Type into its numeric value.
 * @param s The string representation of the type
 * @param numberok Whether a number will be accepted or not.
 * @return The type code, or -1 on error.
 */
public static int
value(String s, boolean numberok) {
	int val = types.getValue(s);
	if (val == -1 && numberok) {
		val = types.getValue("TYPE" + s);
	}
	return val;
}

/**
 * Converts a String representation of an Type into its numeric value
 * @return The type code, or -1 on error.
 */
public static int
value(String s) {
	return value(s, false);
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

QDox parse error in file://<WORKSPACE>/data/code-rep-dataset/Dataset2/Tasks/7192.java