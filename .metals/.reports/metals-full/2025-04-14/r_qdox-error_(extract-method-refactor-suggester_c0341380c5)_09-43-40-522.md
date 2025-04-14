error id: file://<WORKSPACE>/data/code-rep-dataset/Dataset2/Tasks/1542.java
file://<WORKSPACE>/data/code-rep-dataset/Dataset2/Tasks/1542.java
### com.thoughtworks.qdox.parser.ParseException: syntax error @[1,1]

error in qdox parser
file content:
```java
offset: 1
uri: file://<WORKSPACE>/data/code-rep-dataset/Dataset2/Tasks/1542.java
text:
```scala
r@@eturn id & 0xFFFF;

// Copyright (c) 1999 Brian Wellington (bwelling@anomaly.munge.com)
// Portions Copyright (c) 1999 Network Associates, Inc.

import java.io.*;
import java.util.*;

public class dnsHeader {

private int id; 
private boolean [] flags;
byte rcode, opcode;
private short [] counts;

public
dnsHeader(int _id) {
	counts = new short[4];
	flags = new boolean[16];
	id = _id;
}

public
dnsHeader(CountedDataInputStream in) throws IOException {
	this(in.readUnsignedShort());
	readFlags(in);
	for (int i=0; i<counts.length; i++)
		counts[i] = in.readShort();
}

public void
toWire(CountedDataOutputStream out) throws IOException {
	out.writeShort(id);
	writeFlags(out);
	for (int i=0; i<counts.length; i++)
		out.writeShort(counts[i]);
}

public byte []
toWire() throws IOException {
	ByteArrayOutputStream out = new ByteArrayOutputStream();
	CountedDataOutputStream dout = new CountedDataOutputStream(out);
	toWire(dout);
	return out.toByteArray();
}

public void
setFlag(int bit) {
	flags[bit] = true;
}

public void
unsetFlag(int bit) {
	flags[bit] = false;
}

public boolean
getFlag(int bit) {
	return flags[bit];
}

public int
getID() {
	return id;
}

public void
setID(int _id) {
	id = _id;
}

static short
randomID() {
	// why can't this be static?
	Random random = new Random();
	return (short) (random.nextInt() & 0xFFFF);
}

public void
setRcode(byte value) {
	rcode = value;
}

public byte
getRcode() {
	return rcode;
}

public void
setOpcode(byte value) {
	opcode = value;
}

public byte
getOpcode() {
	return opcode;
}

public void
setCount(int field, short value) {
	counts[field] = value;
}

public void
incCount(int field) {
	counts[field]++;
}

public void
decCount(int field) {
	counts[field]--;
}

public short
getCount(int field) {
	return counts[field];
}

private void
writeFlags(CountedDataOutputStream out) throws IOException {
	short flags1 = 0, flags2 = 0;
	for (int i = 0; i < 8; i++) {
		if (flags[i])	flags1 |= (1 << (7-i));
		if (flags[i+8])	flags2 |= (1 << (7-i));
	}
	flags1 |= (opcode << 3);
	flags2 |= (rcode);
	out.writeByte(flags1);
	out.writeByte(flags2);
}

private void
readFlags(CountedDataInputStream in) throws IOException {
	short flags1 = (short)in.readUnsignedByte();
	short flags2 = (short)in.readUnsignedByte();
	for (int i = 0; i < 8; i++) {
		flags[i] =	((flags1 & (1 << (7-i))) != 0);
		flags[i+8] =	((flags2 & (1 << (7-i))) != 0);
	}
	opcode = (byte) ((flags1 >> 3) & 0xF);
	rcode = (byte) (flags2 & 0xF);
}

public String
printFlags() {
	String s;
	StringBuffer sb = new StringBuffer();

	for (int i = 0; i < flags.length; i++)
		if (getFlag(i) && ((s = dns.flagString(i)) != null)) {
			sb.append(s);
			sb.append(" ");
		}
	return sb.toString();
}

public String
toString() {
	StringBuffer sb = new StringBuffer();

	sb.append(";; ->>HEADER<<- "); 
	sb.append("opcode: " + dns.opcodeString(getOpcode()));
	sb.append(", status: " + dns.rcodeString(getRcode()));
	sb.append(", id: " + getID());
	sb.append("\n");

	sb.append(";; flags: " + printFlags());
	sb.append("; ");
	for (int i = 0; i < 4; i++)
		sb.append(dns.sectionString(i) + ": " + getCount(i) + " ");
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
	java.base/java.util.concurrent.ForkJoinPool$WorkQueue.topLevelExec(ForkJoinPool.java:1182)
	java.base/java.util.concurrent.ForkJoinPool.scan(ForkJoinPool.java:1655)
	java.base/java.util.concurrent.ForkJoinPool.runWorker(ForkJoinPool.java:1622)
	java.base/java.util.concurrent.ForkJoinWorkerThread.run(ForkJoinWorkerThread.java:165)
```
#### Short summary: 

QDox parse error in file://<WORKSPACE>/data/code-rep-dataset/Dataset2/Tasks/1542.java