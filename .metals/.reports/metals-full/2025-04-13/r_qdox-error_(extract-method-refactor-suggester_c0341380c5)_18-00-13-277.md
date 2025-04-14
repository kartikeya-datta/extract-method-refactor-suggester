error id: file://<WORKSPACE>/data/code-rep-dataset/Dataset2/Tasks/7684.java
file://<WORKSPACE>/data/code-rep-dataset/Dataset2/Tasks/7684.java
### com.thoughtworks.qdox.parser.ParseException: syntax error @[1,1]

error in qdox parser
file content:
```java
offset: 1
uri: file://<WORKSPACE>/data/code-rep-dataset/Dataset2/Tasks/7684.java
text:
```scala
i@@f (i == 4 && !st.hasMoreDelimiters())

// Copyright (c) 1999 Brian Wellington (bwelling@xbill.org)
// Portions Copyright (c) 1999 Network Associates, Inc.

package org.xbill.DNS;

import java.io.*;
import java.net.*;
import org.xbill.DNS.utils.*;

/**
 * A simple clone of the java.net.InetAddress class, using dnsjava routines.
 *
 * @author Brian Wellington
 */

public final class Address {

private
Address() {}

/**
 * Determines if a string contains a valid IP address.
 * @param s The string
 * @return Whether the string contains a valid IP address
 */
public static boolean
isDottedQuad(String s) {
	MyStringTokenizer st = new MyStringTokenizer(s, ".");
	int labels = 0;
	int i;
	int [] values = new int[4];
	for (i = 0; i < 4; i++) {
		if (st.hasMoreTokens() == false)
			break;
		try {
			values[i] = Integer.parseInt(st.nextToken());
		}
		catch (NumberFormatException e) {
			break;
		}
		if (values[i] < 0 || values[i] > 255)
			break;
	}
	if (i == 4)
		return true;
	else
		return false;
}

/**
 * Converts a byte array containing an IPv4 address into a dotted quad string.
 * @param attr The byte array
 * @return The string representation
 */
public static String
toDottedQuad(byte [] addr) {
	return ((addr[0] & 0xFF) + "." + (addr[1] & 0xFF) + "." +
		(addr[2] & 0xFF) + "." + (addr[3] & 0xFF));

}

/**
 * Determines the IP address of a host
 * @param name The hostname to look up
 * @return The first matching IP address
 * @exception UnknownHostException The hostname does not have any addresses
 */
public static InetAddress
getByName(String name) throws UnknownHostException {
	if (isDottedQuad(name))
		return InetAddress.getByName(name);
	Record [] records = dns.getRecords(name, Type.A);
	if (records == null)
		throw new UnknownHostException("unknown host");
	ARecord a = (ARecord) records[0];
	return a.getAddress();
}

/**
 * Determines all IP address of a host
 * @param name The hostname to look up
 * @return All matching IP addresses
 * @exception UnknownHostException The hostname does not have any addresses
 */
public static InetAddress []
getAllByName(String name) throws UnknownHostException {
	if (isDottedQuad(name))
		return InetAddress.getAllByName(name);
	Record [] records = dns.getRecords(name, Type.A);
	if (records == null)
		throw new UnknownHostException("unknown host");
	InetAddress [] addrs = new InetAddress[records.length];
	for (int i = 0; i < records.length; i++) {
		ARecord a = (ARecord) records[i];
		addrs[i] = a.getAddress();
	}
	return addrs;
}

/**
 * Determines the hostname for an address
 * @param addr The address to look up
 * @return The associated host name
 * @exception UnknownHostException There is no hostname for the address
 */
public static String
getHostName(InetAddress addr) throws UnknownHostException {
	Record [] records = dns.getRecordsByAddress(addr.getHostAddress(),
						    Type.PTR);
	if (records == null)
		throw new UnknownHostException("unknown address");
	PTRRecord ptr = (PTRRecord) records[0];
	return ptr.getTarget().toString();
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

QDox parse error in file://<WORKSPACE>/data/code-rep-dataset/Dataset2/Tasks/7684.java