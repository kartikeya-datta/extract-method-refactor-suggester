error id: file://<WORKSPACE>/data/code-rep-dataset/Dataset2/Tasks/4091.java
file://<WORKSPACE>/data/code-rep-dataset/Dataset2/Tasks/4091.java
### com.thoughtworks.qdox.parser.ParseException: syntax error @[1,1]

error in qdox parser
file content:
```java
offset: 1
uri: file://<WORKSPACE>/data/code-rep-dataset/Dataset2/Tasks/4091.java
text:
```scala
t@@arget.toWireCanonical(out);

// Copyright (c) 1999 Brian Wellington (bwelling@xbill.org)
// Portions Copyright (c) 1999 Network Associates, Inc.

package DNS;

import java.io.*;
import java.util.*;
import DNS.utils.*;

/** Server Selection Record  - finds hosts running services in a domain.  An
 * SRV record will normally be named <service>.<protocol>.domain - an
 * example would be http.tcp.example.com (if HTTP used SRV records)
 */

public class SRVRecord extends Record {

private short priority, weight, port;
private Name target;

private
SRVRecord() {}

/**
 * Creates an SRV Record from the given data
 * @param priority The priority of this SRV.  Records with lower priority
 * are preferred.
 * @param weight The weight, used to select between records at the same
 * priority.
 * @param port The TCP/UDP port that the service uses
 * @param target The host running the service
 */
public
SRVRecord(Name _name, short _dclass, int _ttl, int _priority,
	  int _weight, int _port, Name _target)
{
	super(_name, Type.SRV, _dclass, _ttl);
	priority = (short) _priority;
	weight = (short) _priority;
	port = (short) _priority;
	target = _target;
}

public
SRVRecord(Name _name, short _dclass, int _ttl,
	  int length, DataByteInputStream in, Compression c)
throws IOException
{
	super(_name, Type.SRV, _dclass, _ttl);
	if (in == null)
		return;
	priority = (short) in.readUnsignedShort();
	weight = (short) in.readUnsignedShort();
	port = (short) in.readUnsignedShort();
	target = new Name(in, c);
}

public
SRVRecord(Name _name, short _dclass, int _ttl, MyStringTokenizer st,
	  Name origin)
throws IOException
{
	super(_name, Type.SRV, _dclass, _ttl);
	priority = Short.parseShort(st.nextToken());
	weight = Short.parseShort(st.nextToken());
	port = Short.parseShort(st.nextToken());
	target = new Name(st.nextToken(), origin);
}

/** Converts to a String */
public String
toString() {
	StringBuffer sb = toStringNoData();
	if (target != null) {
		sb.append(priority);
		sb.append(" ");
		sb.append(weight);
		sb.append(" ");
		sb.append(port);
		sb.append(" ");
		sb.append(target);
	}
	return sb.toString();
}

/** Returns the priority */
public short
getPriority() {
	return priority;
}

/** Returns the weight */
public short
getWeight() {
	return weight;
}

/** Returns the port that the service runs on */
public short
getPort() {
	return port;
}

/** Returns the host running that the service */
public Name
getTarget() {
	return target;
}

void
rrToWire(DataByteOutputStream out, Compression c) throws IOException {
	if (target == null)
		return;

	out.writeShort(priority);
	out.writeShort(weight);
	out.writeShort(port);
	target.toWire(out, null);
}

void
rrToWireCanonical(DataByteOutputStream out) throws IOException {
	if (target == null)
		return;

	out.writeShort(priority);
	out.writeShort(weight);
	out.writeShort(port);
	target.toWireCanonical(out, null);
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

QDox parse error in file://<WORKSPACE>/data/code-rep-dataset/Dataset2/Tasks/4091.java