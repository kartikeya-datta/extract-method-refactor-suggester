error id: file://<WORKSPACE>/data/code-rep-dataset/Dataset2/Tasks/3172.java
file://<WORKSPACE>/data/code-rep-dataset/Dataset2/Tasks/3172.java
### com.thoughtworks.qdox.parser.ParseException: syntax error @[1,1]

error in qdox parser
file content:
```java
offset: 1
uri: file://<WORKSPACE>/data/code-rep-dataset/Dataset2/Tasks/3172.java
text:
```scala
o@@rigttl = TTL.parseTTL(st.nextToken());

// Copyright (c) 1999 Brian Wellington (bwelling@xbill.org)
// Portions Copyright (c) 1999 Network Associates, Inc.

package DNS;

import java.io.*;
import java.text.*;
import java.util.*;
import DNS.utils.*;

public class SIGRecord extends Record {

short covered;
byte alg, labels;
int origttl;
Date expire, timeSigned;
short footprint;
Name signer;
byte [] signature;

public
SIGRecord(Name _name, short _dclass, int _ttl, int _covered, int _alg,
	  int _origttl, Date _expire, Date _timeSigned,
	  int _footprint, Name _signer, byte [] _signature)
{
	super(_name, Type.SIG, _dclass, _ttl);
	covered = (short) _covered;
	alg = (byte) _alg;
	labels = name.labels();
	origttl = _origttl;
	expire = _expire;
	timeSigned = _timeSigned;
	footprint = (short) _footprint;
	signer = _signer;
	signature = _signature;
}

public
SIGRecord(Name _name, short _dclass, int _ttl,
	  int length, CountedDataInputStream in, Compression c)
throws IOException
{
	super(_name, Type.SIG, _dclass, _ttl);
	if (in == null)
		return;
	int start = in.getPos();
	covered = in.readShort();
	alg = in.readByte();
	labels = in.readByte();
	origttl = in.readInt();
	expire = new Date(1000 * (long)in.readInt());
	timeSigned = new Date(1000 * (long)in.readInt());
	footprint = in.readShort();
	signer = new Name(in, c);
	signature = new byte[length - (in.getPos() - start)];
	in.read(signature);
}

public
SIGRecord(Name _name, short _dclass, int _ttl, MyStringTokenizer st,
	     Name origin)
throws IOException
{
	super(_name, Type.SIG, _dclass, _ttl);
	covered = Type.value(st.nextToken());
	alg = Byte.parseByte(st.nextToken());
	labels = name.labels();
	origttl = Integer.parseInt(st.nextToken());
	expire = parseDate(st.nextToken());
	timeSigned = parseDate(st.nextToken());
	footprint = (short) Integer.parseInt(st.nextToken());
	signer = new Name(st.nextToken(), origin);
	if (st.hasMoreTokens())
		signature = base64.fromString(st.nextToken());
}

public String
toString() {
	StringBuffer sb = toStringNoData();
	if (signature != null) {
		sb.append (Type.string(covered));
		sb.append (" ");
		sb.append (alg);
		sb.append (" ");
		sb.append (origttl);
		sb.append (" (\n\t");
		sb.append (formatDate(expire));
		sb.append (" ");
		sb.append (formatDate(timeSigned));
		sb.append (" ");
		sb.append ((int)footprint & 0xFFFF);
		sb.append (" ");
		sb.append (signer);
		sb.append ("\n");
		String s = base64.toString(signature);
		sb.append (IO.formatBase64String(s, 64, "\t", true));
        }
	return sb.toString();
}

public short
getTypeCovered() {
	return covered;
}

public byte
getAlgorithm() {
	return alg;
}

public byte
getLabels() {
	return labels;
}

public int
getOrigTTL() {
	return origttl;
}

public Date
getExpire() {
	return expire;
}

public Date
getTimeSigned() {
	return timeSigned;
}

public short
getFootprint() {
	return footprint;
}

public Name
getSigner() {
	return signer;
}

public byte []
getSignature() {
	return signature;
}

void
rrToWire(DataByteOutputStream dbs, Compression c) throws IOException {
	if (signature == null)
		return;

	dbs.writeShort(covered);
	dbs.writeByte(alg);
	dbs.writeByte(labels);
	dbs.writeInt(origttl);
	dbs.writeInt((int)expire.getTime() / 1000);
	dbs.writeInt((int)timeSigned.getTime() / 1000);
	dbs.writeShort(footprint);
	signer.toWire(dbs, null);
	dbs.write(signature);
}

private String
formatDate(Date d) {
	Calendar c = new GregorianCalendar(TimeZone.getTimeZone("UTC"));
	StringBuffer sb = new StringBuffer();
	NumberFormat w4 = new DecimalFormat();
	w4.setMinimumIntegerDigits(4);
	w4.setGroupingUsed(false);
	NumberFormat w2 = new DecimalFormat();
	w2.setMinimumIntegerDigits(2);

	c.setTime(d);
	sb.append(w4.format(c.get(c.YEAR)));
	sb.append(w2.format(c.get(c.MONTH)+1));
	sb.append(w2.format(c.get(c.DAY_OF_MONTH)));
	sb.append(w2.format(c.get(c.HOUR_OF_DAY)));
	sb.append(w2.format(c.get(c.MINUTE)));
	sb.append(w2.format(c.get(c.SECOND)));
	return sb.toString();
}

private Date
parseDate(String s) {
	Calendar c = new GregorianCalendar(TimeZone.getTimeZone("UTC"));

	int year = Integer.parseInt(s.substring(0, 4));
	int month = Integer.parseInt(s.substring(4, 6));
	int date = Integer.parseInt(s.substring(6, 8));
	int hour = Integer.parseInt(s.substring(8, 10));
	int minute = Integer.parseInt(s.substring(10, 12));
	int second = Integer.parseInt(s.substring(12, 14));
	c.set(year, month, date, hour, minute, second);

	return c.getTime();
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

QDox parse error in file://<WORKSPACE>/data/code-rep-dataset/Dataset2/Tasks/3172.java