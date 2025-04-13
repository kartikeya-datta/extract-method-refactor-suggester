error id: file://<WORKSPACE>/data/code-rep-dataset/Dataset2/Tasks/306.java
file://<WORKSPACE>/data/code-rep-dataset/Dataset2/Tasks/306.java
### com.thoughtworks.qdox.parser.ParseException: syntax error @[1,1]

error in qdox parser
file content:
```java
offset: 1
uri: file://<WORKSPACE>/data/code-rep-dataset/Dataset2/Tasks/306.java
text:
```scala
s@@b.append ((int)footprint & 0xFFFF);

import java.net.*;
import java.io.*;
import java.util.*;
import java.text.*;

public class dnsSIGRecord extends dnsRecord {

short typeCovered;
byte alg;
byte labels;
int origTTL;
Date expire, timeSigned;
short footprint;
dnsName signer;
byte [] signature;

public dnsSIGRecord(dnsName rname, short rclass) {
	super(rname, dns.SIG, rclass);
}

public dnsSIGRecord(dnsName rname, short rclass, int rttl, short typeCovered,
		    byte alg, byte labels, int origTTL, Date expire,
		    Date timeSigned, short footprint, dnsName signer,
		    byte [] signature)
{
	this(rname, rclass);
	this.rttl = rttl;
	this.typeCovered = typeCovered;
	this.alg = alg;
	this.labels = labels;
	this.origTTL = origTTL;
	this.expire = expire;
	this.timeSigned = timeSigned;
	this.footprint = footprint;
	this.signer = signer;
	this.signature = signature;
	this.rlength = (short) (18 + signer.length() + signature.length);
}

void parse(CountedDataInputStream in, dnsCompression c) throws IOException {
	typeCovered = (short) in.readUnsignedShort();
	alg = (byte) in.readUnsignedByte();
	labels = (byte) in.readUnsignedByte();
	origTTL = in.readInt();
	expire = new Date (1000 * (long)in.readInt());
	timeSigned = new Date (1000 * (long)in.readInt());
	footprint = (short) in.readUnsignedShort();
	int pos = in.pos();
	signer = new dnsName(in, c);
	signature = new byte[rlength - 18 - (in.pos() - pos)];
	in.read(signature);
}

void rrToBytes(DataOutputStream out) throws IOException {
	out.writeShort(typeCovered);
	out.writeByte(alg);
	out.writeByte(labels);
	out.writeInt(origTTL);
	out.writeInt((int)(expire.getTime() / 1000));
	out.writeInt((int)(timeSigned.getTime() / 1000));
	out.writeShort(footprint);
	signer.toBytes(out);
	out.write(signature, 0, signature.length);
}

void rrToCanonicalBytes(DataOutputStream out) throws IOException {
	out.writeShort(typeCovered);
	out.writeByte(alg);
	out.writeByte(labels);
	out.writeInt(origTTL);
	out.writeInt((int)(expire.getTime() / 1000));
	out.writeInt((int)(timeSigned.getTime() / 1000));
	out.writeShort(footprint);
	signer.toCanonicalBytes(out);
	out.write(signature, 0, signature.length);
}

String rrToString() {
	if (rlength == 0)
		return null;
	StringBuffer sb = new StringBuffer();
	sb.append (dns.typeString(typeCovered));
	sb.append ("\t");
	sb.append (alg);
	sb.append (" ");
	sb.append (origTTL);
	sb.append (" (\n\t");
	sb.append (formatDate(expire));
        sb.append (" ");
	sb.append (formatDate(timeSigned));
	sb.append (" ");
	sb.append (footprint);
	sb.append (" ");
	sb.append (signer);
	String s = base64.toString(signature);
	for (int i = 0; i < s.length(); i += 64) {
		sb.append ("\n\t");
		if (i + 64 >= s.length()) {
			sb.append(s.substring(i));
			sb.append(" )");
		}
		else {
			sb.append(s.substring(i, i+64));
		}
	}
	return sb.toString();
}

String formatDate(Date d) {
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
	scala.collection.parallel.AdaptiveWorkStealingTasks$AWSTWrappedTask.internal(Tasks.scala:159)
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

QDox parse error in file://<WORKSPACE>/data/code-rep-dataset/Dataset2/Tasks/306.java