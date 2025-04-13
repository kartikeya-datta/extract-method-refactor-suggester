error id: file://<WORKSPACE>/data/code-rep-dataset/Dataset2/Tasks/8931.java
file://<WORKSPACE>/data/code-rep-dataset/Dataset2/Tasks/8931.java
### com.thoughtworks.qdox.parser.ParseException: syntax error @[1,1]

error in qdox parser
file content:
```java
offset: 1
uri: file://<WORKSPACE>/data/code-rep-dataset/Dataset2/Tasks/8931.java
text:
```scala
s@@b.append("IN-ADDR.ARPA.");

// Copyright (c) 1999 Brian Wellington (bwelling@xbill.org)
// Portions Copyright (c) 1999 Network Associates, Inc.

/* High level API */

package DNS;

import java.util.*;
import java.io.*;
import java.net.*;

/**
 * High level API for mapping queries to DNS Records.  Caching is used
 * when possible to reduce the number of DNS requests, and a Resolver
 * is used to perform the queries.  A search path can be set or determined
 * by FindServer, which allows lookups of unqualified names.
 * @see Resolver
 * @see FindServer
 */

public final class dns {

private static Resolver res;
private static Cache cache;
private static Name [] searchPath;
private static boolean searchPathSet;

/* Otherwise the class could be instantiated */
private
dns() {}

static boolean
matchType(short type1, short type2) {
	return (type1 == Type.ANY || type2 == Type.ANY || type1 == type2);
}

/**
 * Converts an InetAddress into the corresponding domain name
 * (127.0.0.1 -> 1.0.0.127.IN-ADDR.ARPA.)
 * @return A String containing the domain name.
 */
public static String
inaddrString(InetAddress addr) {
	byte [] address = addr.getAddress();
	StringBuffer sb = new StringBuffer();
	for (int i = 3; i >= 0; i--) {
		sb.append(address[i] & 0xFF);
		sb.append(".");
	}
	sb.append(".IN-ADDR.ARPA.");
	return sb.toString();
}

/**
 * Converts an String containing an IP address in dotted quad form into the
 * corresponding domain name.
 * ex. 127.0.0.1 -> 1.0.0.127.IN-ADDR.ARPA.
 * @return A String containing the domain name.
 */
public static String
inaddrString(String s) {
	InetAddress address;
	try {
		address = InetAddress.getByName(s);
	}
	catch (UnknownHostException e) {
		return null;
	}
	return inaddrString(address);
}

/**
 * Sets the Resolver to be used by functions in the dns class
 */
public static void
setResolver(Resolver _res) {
	res = _res;
}

/**
 * Specifies the domains which will be appended to unqualified names before
 * beginning the lookup process.  If this is not set, FindServer will be used.
 * @see FindServer
 */
public static void
setSearchPath(String [] domains) {
	if (domains == null || domains.length == 0)
		searchPath = null;
	else {
		searchPath = new Name[domains.length];
		for (int i = 0; i < domains.length; i++)
			searchPath[i] = new Name(domains[i]);
	}
	searchPathSet = true;
}

private static Record []
lookup(Name name, short type, short dclass, byte cred) {
	Record [] answers;
	int answerCount = 0, n = 0;
	Enumeration e;

/*System.out.println("lookup of " + name + " " + Type.string(type));*/
	CacheResponse cached = cache.lookupRecords(name, type, dclass, cred);
/*System.out.println(cached);*/
	if (cached.isSuccessful()) {
		RRset [] rrsets = cached.answers();
		answerCount = 0;
		for (int i = 0; i < rrsets.length; i++)
			answerCount += rrsets[i].size();

		answers = new Record[answerCount];

		for (int i = 0; i < rrsets.length; i++) {
			e = rrsets[i].rrs();
			while (e.hasMoreElements()) {
				Record r = (Record)e.nextElement();
				answers[n++] = r;
			}
		}
	}
	else if (cached.isNegative()) {
		return null;
	}
	else {
		Record question = Record.newRecord(name, type, dclass);
		Message query = Message.newQuery(question);

		Message response = res.send(query);

		short rcode = response.getHeader().getRcode();
		if (rcode == Rcode.NOERROR || rcode == Rcode.NXDOMAIN)
			cache.addMessage(response);

		if (rcode != Rcode.NOERROR)
			return null;

		e = response.getSection(Section.ANSWER);
		while (e.hasMoreElements()) {
			Record r = (Record)e.nextElement();
			if (matchType(r.getType(), type))
				answerCount++;
		}
		if (answerCount == 0)
			return null;

		answers = new Record[answerCount];

		e = response.getSection(Section.ANSWER);
		while (e.hasMoreElements()) {
			Record r = (Record)e.nextElement();
			if (matchType(r.getType(), type))
				answers[n++] = r;
		}
	}



	return answers;
}


/**
 * Finds records with the given name, type, and class with a certain credibility
 * @param namestr  The name of the desired records
 * @param type  The type of the desired records
 * @param dclass  The class of the desired records
 * @param cred  The minimum credibility of the desired records
 * @see Credibility
 * @return The matching records, or null if none are found
 */
public static Record []
getRecords(String namestr, short type, short dclass, byte cred) {
	Record [] answers = null;
	Name name = new Name(namestr);

	if (!Type.isRR(type) && type != Type.ANY)
		return null;

	if (res == null) {
		try {
			setResolver(new ExtendedResolver());
		}
		catch (UnknownHostException uhe) {
			System.out.println("Failed to initialize resolver");
			System.exit(-1);
		}
	}
	if (!searchPathSet)
		searchPath = FindServer.searchPath();
	if (cache == null)
		cache = new Cache();

	if (searchPath == null || name.isQualified())
		answers = lookup(name, type, dclass, cred);
	else {
		for (int i = 0; i < searchPath.length; i++) {
			answers = lookup(new Name(namestr, searchPath[i]),
					 type, dclass, cred);
			if (answers != null)
				break;
		}
	}

	return answers;
}

/**
 * Finds credible records with the given name, type, and class with
 * @param namestr  The name of the desired records
 * @param type  The type of the desired records
 * @param dclass  The class of the desired records
 * @return The matching records, or null if none are found
 */
public static Record []
getRecords(String namestr, short type, short dclass) {
	return getRecords(namestr, type, dclass, Credibility.NONAUTH_ANSWER);
}

/**
 * Finds any records with the given name, type, and class
 * @param namestr  The name of the desired records
 * @param type  The type of the desired records
 * @param dclass  The class of the desired records
 * @return The matching records, or null if none are found
 */
public static Record []
getAnyRecords(String namestr, short type, short dclass) {
	return getRecords(namestr, type, dclass, Credibility.AUTH_ADDITIONAL);
}

/**
 * Finds credible records with the given name and type in class IN
 * @param namestr  The name of the desired records
 * @param type  The type of the desired records
 * @return The matching records, or null if none are found
 */
public static Record []
getRecords(String name, short type) {
	return getRecords(name, type, DClass.IN, Credibility.NONAUTH_ANSWER);
}

/**
 * Finds any records with the given name and type in class IN
 * @param namestr  The name of the desired records
 * @param type  The type of the desired records
 * @return The matching records, or null if none are found
 */
public static Record []
getAnyRecords(String name, short type) {
	return getRecords(name, type, DClass.IN, Credibility.AUTH_ADDITIONAL);
}

/**
 * Finds credible records for the given dotted quad address and type in class IN
 * @param addr  The dotted quad address of the desired records
 * @param type  The type of the desired records
 * @return The matching records, or null if none are found
 */
public static Record []
getRecordsByAddress(String addr, short type) {
	String name = inaddrString(addr);
	return getRecords(name, type, DClass.IN, Credibility.NONAUTH_ANSWER);
}

/**
 * Finds any records for the given dotted quad address and type in class IN
 * @param addr  The dotted quad address of the desired records
 * @param type  The type of the desired records
 * @return The matching records, or null if none are found
 */
public static Record []
getAnyRecordsByAddress(String addr, short type) {
	String name = inaddrString(addr);
	return getRecords(name, type, DClass.IN, Credibility.AUTH_ADDITIONAL);
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

QDox parse error in file://<WORKSPACE>/data/code-rep-dataset/Dataset2/Tasks/8931.java