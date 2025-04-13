error id: file://<WORKSPACE>/data/code-rep-dataset/Dataset2/Tasks/4143.java
file://<WORKSPACE>/data/code-rep-dataset/Dataset2/Tasks/4143.java
### com.thoughtworks.qdox.parser.ParseException: syntax error @[1,1]

error in qdox parser
file content:
```java
offset: 1
uri: file://<WORKSPACE>/data/code-rep-dataset/Dataset2/Tasks/4143.java
text:
```scala
D@@ataByteInputStream in, Compression c) throws IOException

// Copyright (c) 1999 Brian Wellington (bwelling@xbill.org)
// Portions Copyright (c) 1999 Network Associates, Inc.

package DNS;

import java.io.*;
import java.util.*;
import DNS.utils.*;

public class SOARecord extends Record {

Name host, admin;
int serial, refresh, retry, expire, minimum;

public
SOARecord(Name _name, short _dclass, int _ttl, Name _host, Name _admin,
	  int _serial, int _refresh, int _retry, int _expire, int _minimum)
throws IOException
{
	super(_name, Type.SOA, _dclass, _ttl);
	host = _host;
	admin = _admin;
	serial = _serial;
	refresh = _refresh;
	retry = _retry;
	expire = _expire;
	minimum = _minimum;
}

public
SOARecord(Name _name, short _dclass, int _ttl, int length,
	  CountedDataInputStream in, Compression c) throws IOException
{
	super(_name, Type.SOA, _dclass, _ttl);
	if (in == null)
		return;
	host = new Name(in, c);
	admin = new Name(in, c);
	serial = in.readInt();
	refresh = in.readInt();
	retry = in.readInt();
	expire = in.readInt();
	minimum = in.readInt();
}

public
SOARecord(Name _name, short _dclass, int _ttl, MyStringTokenizer st,
	     Name origin)
throws IOException
{
	super(_name, Type.SOA, _dclass, _ttl);
	host = new Name(st.nextToken(), origin);
	admin = new Name(st.nextToken(), origin);
	serial = Integer.parseInt(st.nextToken());
	refresh = TTL.parseTTL(st.nextToken());
	retry = TTL.parseTTL(st.nextToken());
	expire = TTL.parseTTL(st.nextToken());
	minimum = TTL.parseTTL(st.nextToken());
}


public String
toString() {
	StringBuffer sb = toStringNoData();
	if (host != null) {
		sb.append(host);
		sb.append(" ");
		sb.append(admin);
		sb.append(" (\n\t\t\t\t\t");
		sb.append(serial);
		sb.append("\t; serial\n\t\t\t\t\t");
		sb.append(refresh);
		sb.append("\t; refresh\n\t\t\t\t\t");
		sb.append(retry);
		sb.append("\t; retry\n\t\t\t\t\t");
		sb.append(expire);
		sb.append("\t; expire\n\t\t\t\t\t");
		sb.append(minimum);
		sb.append(")\t; minimum");
	}
	return sb.toString();
}

public Name
getHost() {  
	return host;
}       

public Name
getAdmin() {  
	return admin;
}       

public int
getSerial() {  
	return serial;
}       

public int
getRefresh() {  
	return refresh;
}       

public int
getRetry() {  
	return retry;
}       

public int
getExpire() {  
	return expire;
}       

public int
getMinimum() {  
	return minimum;
}       

void
rrToWire(DataByteOutputStream dbs, Compression c) throws IOException {
	if (host == null)
		return;

	host.toWire(dbs, c);
	admin.toWire(dbs, c);
	dbs.writeInt(serial);
	dbs.writeInt(refresh);
	dbs.writeInt(retry);
	dbs.writeInt(expire);
        dbs.writeInt(minimum);
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

QDox parse error in file://<WORKSPACE>/data/code-rep-dataset/Dataset2/Tasks/4143.java