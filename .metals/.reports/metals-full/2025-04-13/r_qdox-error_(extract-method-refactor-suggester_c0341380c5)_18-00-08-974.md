error id: file://<WORKSPACE>/data/code-rep-dataset/Dataset2/Tasks/3553.java
file://<WORKSPACE>/data/code-rep-dataset/Dataset2/Tasks/3553.java
### com.thoughtworks.qdox.parser.ParseException: syntax error @[1,1]

error in qdox parser
file content:
```java
offset: 1
uri: file://<WORKSPACE>/data/code-rep-dataset/Dataset2/Tasks/3553.java
text:
```scala
r@@es = _res = new Resolver();

// Copyright (c) 1999 Brian Wellington (bwelling@xbill.org)
// Portions Copyright (c) 1999 Network Associates, Inc.

/* High level API */

package DNS;

import java.util.*;
import java.io.*;
import java.net.*;

public final class dns {

private static Resolver _res;

static boolean
matchType(short type1, short type2) {
	return (type1 == Type.ANY || type2 == Type.ANY || type1 == type2);
}

public static void
init(String defaultResolver) {
	Resolver.setDefaultResolver(defaultResolver);
}

public static Record []
getRecords(Resolver res, String name, short type, short dclass) {
	Message query = new Message();
	Message response;
	Record question;
	Record [] answers;
	int answerCount = 0, i = 0;
	Enumeration e;

	if (res == _res && _res == null) {
		try {
			_res = new Resolver();
		}
		catch (UnknownHostException uhe) {
			System.out.println("Failed to initialize resolver");
			System.exit(-1);
		}
	}

	query.getHeader().setOpcode(Opcode.QUERY);
	query.getHeader().setFlag(Flags.RD);
	question = Record.newRecord(new Name(name), type, dclass);
	query.addRecord(Section.QUESTION, question);

	try {
		response = res.send(query);
	}
	catch (IOException ioe) {
		return null;
	}

	if (response.getHeader().getRcode() != Rcode.NOERROR)
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
			answers[i++] = r;
	}

	return answers;
}

public static Record []
getRecords(Resolver res, String name, short type) {
	return getRecords(res, name, type, DClass.IN);
}

public static Record []
getRecords(String name, short type, short dclass) {
	return getRecords(_res, name, type, dclass);
}

public static Record []
getRecords(String name, short type) {
	return getRecords(_res, name, type, DClass.IN);
}


public static Record []
getRecordsByAddress(Resolver res, String addr, short type) {
	byte [] address;
	try {
		address = InetAddress.getByName(addr).getAddress();
	}
	catch (UnknownHostException e) {
		return null;
	}
	StringBuffer sb = new StringBuffer();
	for (int i = 3; i >= 0; i--) {
		sb.append(address[i] & 0xFF);
		sb.append(".");
	}
	sb.append(".IN-ADDR.ARPA.");
	String name = sb.toString();
	return getRecords(res, name, type, DClass.IN);
}

public static Record []
getRecordsByAddress(String addr, short type) {
	return getRecordsByAddress(_res, addr, type);
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

QDox parse error in file://<WORKSPACE>/data/code-rep-dataset/Dataset2/Tasks/3553.java