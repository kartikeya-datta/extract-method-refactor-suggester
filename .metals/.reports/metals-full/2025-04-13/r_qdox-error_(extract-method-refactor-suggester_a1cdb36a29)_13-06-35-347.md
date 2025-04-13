error id: file://<WORKSPACE>/data/code-rep-dataset/Dataset2/Tasks/8335.java
file://<WORKSPACE>/data/code-rep-dataset/Dataset2/Tasks/8335.java
### com.thoughtworks.qdox.parser.ParseException: syntax error @[1,1]

error in qdox parser
file content:
```java
offset: 1
uri: file://<WORKSPACE>/data/code-rep-dataset/Dataset2/Tasks/8335.java
text:
```scala
g@@etRecords(String namestr, int type, int dclass, int cred) {

// Copyright (c) 1999-2004 Brian Wellington (bwelling@xbill.org)

package org.xbill.DNS;

import java.util.*;
import java.net.*;

/**
 * A high level API for mapping queries to DNS Records.
 * <P>
 * As of dnsjava 1.4.0, all functions in this class are wrappers
 * around functions in the Lookup and ReverseMap class, and those should be
 * used instead.
 *
 * @see Lookup
 * @see ReverseMap
 *
 * @author Brian Wellington
 */

public final class dns {

/* Otherwise the class could be instantiated */
private
dns() {}

/**
 * Converts an InetAddress into the corresponding domain name
 * (127.0.0.1 -> 1.0.0.127.IN-ADDR.ARPA.)
 * @return A String containing the domain name.
 */
public static String
inaddrString(InetAddress addr) {
	return ReverseMap.fromAddress(addr).toString();
}

/**
 * Converts an String containing an IP address in dotted quad form into the
 * corresponding domain name.
 * ex. 127.0.0.1 -> 1.0.0.127.IN-ADDR.ARPA.
 * @return A String containing the domain name.
 */
public static String
inaddrString(String s) {
	try {
		return ReverseMap.fromAddress(s).toString();
	}
	catch (UnknownHostException e) {
		return null;
	}
}

/**
 * Sets the Resolver to be used by functions in the dns class
 */
public static synchronized void
setResolver(Resolver res) {
	Lookup.setDefaultResolver(res);
}

/**
 * Obtains the Resolver used by functions in the dns class.  This can be used
 * to set Resolver properties.
 */
public static synchronized Resolver
getResolver() {
	return Lookup.getDefaultResolver();
}

/**
 * Specifies the domains which will be appended to unqualified names before
 * beginning the lookup process.  If this is not set, FindServer will be used.
 * Unlike the Lookup setSearchPath function, this will silently ignore
 * invalid names.
 * @see FindServer
 */
public static synchronized void
setSearchPath(String [] domains) {
	if (domains == null || domains.length == 0) {
		Lookup.setDefaultSearchPath((Name []) null);
		return;
	}

	List l = new ArrayList();
	for (int i = 0; i < domains.length; i++) {
		try {
			l.add(Name.fromString(domains[i], Name.root));
		}
		catch (TextParseException e) {
		}
	}
	Name [] searchPath = (Name [])l.toArray(new Name[l.size()]);
	Lookup.setDefaultSearchPath(searchPath);
}

/**
 * Obtains the Cache used by functions in the dns class.  This can be used
 * to perform more specific queries and/or remove elements.
 *
 * @param dclass The dns class of data in the cache
 */
public static synchronized Cache
getCache(int dclass) {
	return Lookup.getDefaultCache(dclass);
}

/**
 * Obtains the (class IN) Cache used by functions in the dns class.  This
 * can be used to perform more specific queries and/or remove elements.
 */
public static synchronized Cache
getCache() {
	return Lookup.getDefaultCache(DClass.IN);
}

/**
 * Finds records with the given name, type, and class with a certain credibility
 * @param namestr The name of the desired records
 * @param type The type of the desired records
 * @param dclass The class of the desired records
 * @param cred The minimum credibility of the desired records
 * @see Credibility
 * @return The matching records, or null if none are found
 */
public static Record []
getRecords(String namestr, int type, int dclass, byte cred) {
	try {
		Lookup lookup = new Lookup(namestr, type, dclass);
		lookup.setCredibility(cred);
		return lookup.run();
	} catch (Exception e) {
		return null;
	}
}

/**
 * Finds credible records with the given name, type, and class
 * @param namestr The name of the desired records
 * @param type The type of the desired records
 * @param dclass The class of the desired records
 * @return The matching records, or null if none are found
 */
public static Record []
getRecords(String namestr, int type, int dclass) {
	return getRecords(namestr, type, dclass, Credibility.NORMAL);
}

/**
 * Finds any records with the given name, type, and class
 * @param namestr The name of the desired records
 * @param type The type of the desired records
 * @param dclass The class of the desired records
 * @return The matching records, or null if none are found
 */
public static Record []
getAnyRecords(String namestr, int type, int dclass) {
	return getRecords(namestr, type, dclass, Credibility.ANY);
}

/**
 * Finds credible records with the given name and type in class IN
 * @param namestr The name of the desired records
 * @param type The type of the desired records
 * @return The matching records, or null if none are found
 */
public static Record []
getRecords(String namestr, int type) {
	return getRecords(namestr, type, DClass.IN, Credibility.NORMAL);
}

/**
 * Finds any records with the given name and type in class IN
 * @param namestr The name of the desired records
 * @param type The type of the desired records
 * @return The matching records, or null if none are found
 */
public static Record []
getAnyRecords(String namestr, int type) {
	return getRecords(namestr, type, DClass.IN, Credibility.ANY);
}

/**
 * Finds credible records for the given dotted quad address and type in class IN
 * @param addr The dotted quad address of the desired records
 * @param type The type of the desired records
 * @return The matching records, or null if none are found
 */
public static Record []
getRecordsByAddress(String addr, int type) {
	String namestr = inaddrString(addr);
	return getRecords(namestr, type, DClass.IN, Credibility.NORMAL);
}

/**
 * Finds any records for the given dotted quad address and type in class IN
 * @param addr The dotted quad address of the desired records
 * @param type The type of the desired records
 * @return The matching records, or null if none are found
 */
public static Record []
getAnyRecordsByAddress(String addr, int type) {
	String name = inaddrString(addr);
	return getRecords(name, type, DClass.IN, Credibility.ANY);
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

QDox parse error in file://<WORKSPACE>/data/code-rep-dataset/Dataset2/Tasks/8335.java