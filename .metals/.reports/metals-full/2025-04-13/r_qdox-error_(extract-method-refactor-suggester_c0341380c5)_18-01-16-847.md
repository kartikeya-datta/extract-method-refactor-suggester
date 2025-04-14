error id: file://<WORKSPACE>/data/code-rep-dataset/Dataset2/Tasks/7375.java
file://<WORKSPACE>/data/code-rep-dataset/Dataset2/Tasks/7375.java
### com.thoughtworks.qdox.parser.ParseException: syntax error @[1,1]

error in qdox parser
file content:
```java
offset: 1
uri: file://<WORKSPACE>/data/code-rep-dataset/Dataset2/Tasks/7375.java
text:
```scala
t@@his(name, dclass, ttl, Collections.singletonList(string));

// Copyright (c) 1999-2004 Brian Wellington (bwelling@xbill.org)

package org.xbill.DNS;

import java.io.*;
import java.util.*;
import org.xbill.DNS.utils.*;

/**
 * Text - stores text strings
 *
 * @author Brian Wellington
 */

public class TXTRecord extends Record {

private List strings;

TXTRecord() {}

Record
getObject() {
	return new TXTRecord();
}

/**
 * Creates a TXT Record from the given data
 * @param strings The text strings
 * @throws IllegalArgumentException One of the strings has invalid escapes
 */
public
TXTRecord(Name name, int dclass, long ttl, List strings) {
	super(name, Type.TXT, dclass, ttl);
	if (strings == null)
		throw new IllegalArgumentException
				("TXTRecord: strings must not be null");
	this.strings = new ArrayList(strings.size());
	Iterator it = strings.iterator();
	try {
		while (it.hasNext()) {
			String s = (String) it.next();
			this.strings.add(byteArrayFromString(s));
		}
	}
	catch (TextParseException e) {
		throw new IllegalArgumentException(e.getMessage());
	}
}

/**
 * Creates a TXT Record from the given data
 * @param string One text string
 * @throws IllegalArgumentException The string has invalid escapes
 */
public
TXTRecord(Name name, int dclass, long ttl, String string) {
	this(name, dclass, ttl, Collections.nCopies(1, string));
}

void
rrFromWire(DNSInput in) throws IOException {
	strings = new ArrayList(2);
	while (in.remaining() > 0) {
		byte [] b = in.readCountedString();
		strings.add(b);
	}
}

void
rdataFromString(Tokenizer st, Name origin) throws IOException {
	strings = new ArrayList(2);
	while (true) {
		Tokenizer.Token t = st.get();
		if (!t.isString())
			break;
		try {
			strings.add(byteArrayFromString(t.value));
		}
		catch (TextParseException e) { 
			throw st.exception(e.getMessage());
		}

	}
	st.unget();
}

/** converts to a String */
String
rrToString() {
	StringBuffer sb = new StringBuffer();
	Iterator it = strings.iterator();
	while (it.hasNext()) {
		byte [] array = (byte []) it.next();
		sb.append(byteArrayToString(array, true));
		if (it.hasNext())
			sb.append(" ");
	}
	return sb.toString();
}

/**
 * Returns the text strings
 * @return A list of Strings corresponding to the text strings.
 */
public List
getStrings() {
	List list = new ArrayList(strings.size());
	for (int i = 0; i < strings.size(); i++)
		list.add(byteArrayToString((byte []) strings.get(i), false));
	return list;
}

/**
 * Returns the text strings
 * @return A list of byte arrays corresponding to the text strings.
 */
public List
getStringsAsByteArrays() {
	return strings;
}

void
rrToWire(DNSOutput out, Compression c, boolean canonical) {
	Iterator it = strings.iterator();
	while (it.hasNext()) {
		byte [] b = (byte []) it.next();
		out.writeCountedString(b);
	}
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

QDox parse error in file://<WORKSPACE>/data/code-rep-dataset/Dataset2/Tasks/7375.java