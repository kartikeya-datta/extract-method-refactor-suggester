error id: file://<WORKSPACE>/data/code-rep-dataset/Dataset2/Tasks/825.java
file://<WORKSPACE>/data/code-rep-dataset/Dataset2/Tasks/825.java
### com.thoughtworks.qdox.parser.ParseException: syntax error @[1,1]

error in qdox parser
file content:
```java
offset: 1
uri: file://<WORKSPACE>/data/code-rep-dataset/Dataset2/Tasks/825.java
text:
```scala
d@@nsSRVRecord(dnsName _name, short _dclass, int _ttl, MyStringTokenizer st)

// Copyright (c) 1999 Brian Wellington (bwelling@anomaly.munge.com)
// Portions Copyright (c) 1999 Network Associates, Inc.

import java.io.*;
import java.util.*;

public class dnsSRVRecord extends dnsRecord {

short priority, weight, port;
dnsName target;

public
dnsSRVRecord(dnsName _name, short _dclass, int _ttl, int _priority,
	     int _weight, int _port, dnsName _target)
{
	super(_name, dns.SRV, _dclass, _ttl);
	priority = (short) _priority;
	weight = (short) _priority;
	port = (short) _priority;
	target = _target;
}

public
dnsSRVRecord(dnsName _name, short _dclass, int _ttl,
	    int length, CountedDataInputStream in, dnsCompression c)
throws IOException
{
	super(_name, dns.SRV, _dclass, _ttl);
	if (in == null)
		return;
	priority = (short) in.readUnsignedShort();
	weight = (short) in.readUnsignedShort();
	port = (short) in.readUnsignedShort();
	target = new dnsName(in, c);
}

public
dnsSRVRecord(dnsName _name, short _dclass, int _ttl, StringTokenizer st)
throws IOException
{
	super(_name, dns.SRV, _dclass, _ttl);
	priority = Short.parseShort(st.nextToken());
	weight = Short.parseShort(st.nextToken());
	port = Short.parseShort(st.nextToken());
	target = new dnsName(st.nextToken());
}

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

public short
getPriority() {
	return priority;
}

public short
getWeight() {
	return weight;
}

public short
getPort() {
	return port;
}

public dnsName
getTarget() {
	return target;
}

byte []
rrToWire() throws IOException {
	if (target == null)
		return null;

	ByteArrayOutputStream bs = new ByteArrayOutputStream();
	DataOutputStream ds = new DataOutputStream(bs);

	ds.writeShort(priority);
	ds.writeShort(weight);
	ds.writeShort(port);
	target.toWire(ds);
	return bs.toByteArray();
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

QDox parse error in file://<WORKSPACE>/data/code-rep-dataset/Dataset2/Tasks/825.java