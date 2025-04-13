error id: file://<WORKSPACE>/data/code-rep-dataset/Dataset2/Tasks/5906.java
file://<WORKSPACE>/data/code-rep-dataset/Dataset2/Tasks/5906.java
### com.thoughtworks.qdox.parser.ParseException: syntax error @[1,1]

error in qdox parser
file content:
```java
offset: 1
uri: file://<WORKSPACE>/data/code-rep-dataset/Dataset2/Tasks/5906.java
text:
```scala
C@@acheResponse cached = cache.lookupRecords(name, type, dclass, cred);

// Copyright (c) 1999 Brian Wellington (bwelling@xbill.org)
// Portions Copyright (c) 1999 Network Associates, Inc.

/* High level API */

package DNS;

import java.util.*;
import java.io.*;
import java.net.*;

public final class dns {

private static Resolver res;
private static Cache cache;

static boolean
matchType(short type1, short type2) {
	return (type1 == Type.ANY || type2 == Type.ANY || type1 == type2);
}

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

public static void
init(String defaultResolver) {
	Resolver.setDefaultResolver(defaultResolver);
}

public static void
setResolver(Resolver _res) {
	res = _res;
}

public static Record []
getRecords(String namestr, short type, short dclass, byte cred) {
	Message query;
	Message response;
	Record question;
	Record [] answers;
	int answerCount = 0, i = 0;
	Enumeration e;
	Name name = new Name(namestr);

/*System.out.println("lookup of " + name + " " + Type.string(type));*/
	if (!Type.isRR(type) && type != Type.ANY)
		return null;

	if (res == null) {
		try {
			res = new Resolver();
		}
		catch (UnknownHostException uhe) {
			System.out.println("Failed to initialize resolver");
			System.exit(-1);
		}
	}
	if (cache == null)
		cache = new Cache();

	CacheResponse cached = cache.lookupRecords(name, type, cred);
/*System.out.println(cached);*/
	if (cached.isSuccessful()) {
		RRset rrset = cached.answer();
		answerCount = rrset.size();
		e = rrset.rrs();
	}
	else if (cached.isNegative()) {
		answerCount = 0;
		e = null;
	}
	else {
		query = new Message();
		query.getHeader().setOpcode(Opcode.QUERY);
		query.getHeader().setFlag(Flags.RD);
		question = Record.newRecord(name, type, dclass);
		query.addRecord(Section.QUESTION, question);

		try {
			response = res.send(query);
		}
		catch (IOException ioe) {
			return null;
		}

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

		e = response.getSection(Section.ANSWER);
	}

	if (answerCount == 0)
		return null;

	answers = new Record[answerCount];

	while (e.hasMoreElements()) {
		Record r = (Record)e.nextElement();
		if (matchType(r.getType(), type))
			answers[i++] = r;
	}

	return answers;
}

public static Record []
getRecords(String namestr, short type, short dclass) {
	return getRecords(namestr, type, dclass, Credibility.NONAUTH_ANSWER);
}

public static Record []
getAnyRecords(String namestr, short type, short dclass) {
	return getRecords(namestr, type, dclass, Credibility.AUTH_ADDITIONAL);
}

public static Record []
getRecords(String name, short type) {
	return getRecords(name, type, DClass.IN, Credibility.NONAUTH_ANSWER);
}

public static Record []
getAnyRecords(String name, short type) {
	return getRecords(name, type, DClass.IN, Credibility.AUTH_ADDITIONAL);
}

public static Record []
getRecordsByAddress(String addr, short type) {
	String name = inaddrString(addr);
	return getRecords(name, type, DClass.IN, Credibility.NONAUTH_ANSWER);
}

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
	java.base/java.util.concurrent.ForkJoinPool$WorkQueue.topLevelExec(ForkJoinPool.java:1182)
	java.base/java.util.concurrent.ForkJoinPool.scan(ForkJoinPool.java:1655)
	java.base/java.util.concurrent.ForkJoinPool.runWorker(ForkJoinPool.java:1622)
	java.base/java.util.concurrent.ForkJoinWorkerThread.run(ForkJoinWorkerThread.java:165)
```
#### Short summary: 

QDox parse error in file://<WORKSPACE>/data/code-rep-dataset/Dataset2/Tasks/5906.java