error id: file://<WORKSPACE>/data/code-rep-dataset/Dataset2/Tasks/2145.java
file://<WORKSPACE>/data/code-rep-dataset/Dataset2/Tasks/2145.java
### com.thoughtworks.qdox.parser.ParseException: syntax error @[1,1]

error in qdox parser
file content:
```java
offset: 1
uri: file://<WORKSPACE>/data/code-rep-dataset/Dataset2/Tasks/2145.java
text:
```scala
d@@bs.writeShort(keyTag);

// Copyright (c) 1999 Brian Wellington (bwelling@xbill.org)
// Portions Copyright (c) 1999 Network Associates, Inc.

package DNS;

import java.io.*;
import java.util.*;
import DNS.utils.*;

public class CERTRecord extends Record {

short certType, keyTag;
byte alg;
byte [] cert;

static int NOCONF = 0x8000;
static int NOAUTH = 0x4000;

public
CERTRecord(Name _name, short _dclass, int _ttl, int _certType,
	      int _keyTag, int _alg, byte []  _cert)
{
	super(_name, Type.CERT, _dclass, _ttl);
	certType = (short) _certType;
	keyTag = (short) _keyTag;
	alg = (byte) _alg;
	cert = _cert;
}

public
CERTRecord(Name _name, short _dclass, int _ttl,
	      int length, DataByteInputStream in, Compression c)
throws IOException
{
	super(_name, Type.CERT, _dclass, _ttl);
	if (in == null)
		return;
	certType = in.readShort();
	keyTag = in.readShort();
	alg = in.readByte();
	if (length > 5) {
		cert = new byte[length - 5];
		in.read(cert);
	}
}

public
CERTRecord(Name _name, short _dclass, int _ttl, MyStringTokenizer st,
	   Name origin)
throws IOException
{
	super(_name, Type.CERT, _dclass, _ttl);
	certType = (short) Integer.parseInt(st.nextToken());
	keyTag = (short) Integer.parseInt(st.nextToken());
	alg = (byte) Integer.parseInt(st.nextToken());
	cert = base64.fromString(st.nextToken());
}

public String
toString() {
	StringBuffer sb = toStringNoData();
	if (cert != null) {
		sb.append (certType);
		sb.append (" ");
		sb.append (keyTag);
		sb.append (" ");
		sb.append (alg);
		if (cert != null) {
			sb.append (" (\n");
			String s = base64.toString(cert);
			sb.append (IO.formatBase64String(s, 64, "\t", true));
		}
	}
	return sb.toString();
}

public short
getCertType() {
	return certType;
}

public short
getKeyTag() {
	return keyTag;
}

public byte
getAlgorithm() {
	return alg;
}

void
rrToWire(DataByteOutputStream dbs, Compression c) throws IOException {
	if (cert == null)
		return;

	dbs.writeShort(certType);
	dbs.writeByte(keyTag);
	dbs.writeByte(alg);
	dbs.write(cert);
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

QDox parse error in file://<WORKSPACE>/data/code-rep-dataset/Dataset2/Tasks/2145.java