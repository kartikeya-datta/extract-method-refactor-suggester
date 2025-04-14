error id: file://<WORKSPACE>/data/code-rep-dataset/Dataset2/Tasks/7530.java
file://<WORKSPACE>/data/code-rep-dataset/Dataset2/Tasks/7530.java
### com.thoughtworks.qdox.parser.ParseException: syntax error @[1,1]

error in qdox parser
file content:
```java
offset: 1
uri: file://<WORKSPACE>/data/code-rep-dataset/Dataset2/Tasks/7530.java
text:
```scala
r@@ec.serial = st.getUInt32();

// Copyright (c) 1999 Brian Wellington (bwelling@xbill.org)
// Portions Copyright (c) 1999 Network Associates, Inc.

package org.xbill.DNS;

import java.io.*;
import org.xbill.DNS.utils.*;

/**
 * Start of Authority - describes properties of a zone.
 *
 * @author Brian Wellington
 */

public class SOARecord extends Record {

private static SOARecord member = new SOARecord();

private Name host, admin;
private long serial, refresh, retry, expire, minimum;

private
SOARecord() {}

private
SOARecord(Name name, int dclass, long ttl) {
	super(name, Type.SOA, dclass, ttl);
}

static SOARecord
getMember() {
	return member;
}

/**
 * Creates an SOA Record from the given data
 * @param host The primary nameserver for the zone
 * @param admin The zone administrator's address
 * @param serial The zone's serial number
 * @param refresh The amount of time until a secondary checks for a new serial
 * number
 * @param retry The amount of time between a secondary's checks for a new
 * serial number
 * @param expire The amount of time until a secondary expires a zone
 * @param minimum The minimum TTL for records in the zone
*/
public
SOARecord(Name name, int dclass, long ttl, Name host, Name admin,
	  long serial, long refresh, long retry, long expire, long minimum)
{
	this(name, dclass, ttl);
	if (!host.isAbsolute())
		throw new RelativeNameException(host);
	this.host = host;
	if (!admin.isAbsolute())
		throw new RelativeNameException(admin);
	checkU32("serial", serial);
	checkU32("refresh", refresh);
	checkU32("retry", retry);
	checkU32("expire", expire);
	checkU32("minimum", minimum);
	this.admin = admin;
	this.serial = serial;
	this.refresh = refresh;
	this.retry = retry;
	this.expire = expire;
	this.minimum = minimum;
}

Record
rrFromWire(Name name, int type, int dclass, long ttl, int length,
	   DataByteInputStream in)
throws IOException
{
	SOARecord rec = new SOARecord(name, dclass, ttl);
	if (in == null)
		return rec;
	rec.host = new Name(in);
	rec.admin = new Name(in);
	rec.serial = in.readUnsignedInt();
	rec.refresh = in.readUnsignedInt();
	rec.retry = in.readUnsignedInt();
	rec.expire = in.readUnsignedInt();
	rec.minimum = in.readUnsignedInt();
	return rec;
}

Record
rdataFromString(Name name, int dclass, long ttl, Tokenizer st, Name origin)
throws IOException
{
	SOARecord rec = new SOARecord(name, dclass, ttl);
	rec.host = st.getName(origin);
	rec.admin = st.getName(origin);
	rec.serial = (int) st.getUInt32();
	rec.refresh = st.getTTL();
	rec.retry = st.getTTL();
	rec.expire = st.getTTL();
	rec.minimum = st.getTTL();
	return rec;
}

/** Convert to a String */
public String
rdataToString() {
	StringBuffer sb = new StringBuffer();
	if (host != null) {
		sb.append(host);
		sb.append(" ");
		sb.append(admin);
		if (Options.check("multiline")) {
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
			sb.append(" )\t; minimum");
		} else {
			sb.append(" ");
			sb.append(serial);
			sb.append(" ");
			sb.append(refresh);
			sb.append(" ");
			sb.append(retry);
			sb.append(" ");
			sb.append(expire);
			sb.append(" ");
			sb.append(minimum);
		}
	}
	return sb.toString();
}

/** Returns the primary nameserver */
public Name
getHost() {  
	return host;
}       

/** Returns the zone administrator's address */
public Name
getAdmin() {  
	return admin;
}       

/** Returns the zone's serial number */
public long
getSerial() {  
	return serial;
}       

/** Returns the zone refresh interval */
public long
getRefresh() {  
	return refresh;
}       

/** Returns the zone retry interval */
public long
getRetry() {  
	return retry;
}       

/** Returns the time until a secondary expires a zone */
public long
getExpire() {  
	return expire;
}       

/** Returns the minimum TTL for records in the zone */
public long
getMinimum() {  
	return minimum;
}       

void
rrToWire(DataByteOutputStream out, Compression c, boolean canonical) {
	if (host == null)
		return;

	host.toWire(out, c, canonical);
	admin.toWire(out, c, canonical);
	out.writeUnsignedInt(serial);
	out.writeUnsignedInt(refresh);
	out.writeUnsignedInt(retry);
	out.writeUnsignedInt(expire);
	out.writeUnsignedInt(minimum);
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

QDox parse error in file://<WORKSPACE>/data/code-rep-dataset/Dataset2/Tasks/7530.java