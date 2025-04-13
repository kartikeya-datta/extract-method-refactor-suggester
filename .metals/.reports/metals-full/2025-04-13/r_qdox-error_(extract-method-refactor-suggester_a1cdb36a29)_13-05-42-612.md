error id: file://<WORKSPACE>/data/code-rep-dataset/Dataset2/Tasks/3580.java
file://<WORKSPACE>/data/code-rep-dataset/Dataset2/Tasks/3580.java
### com.thoughtworks.qdox.parser.ParseException: syntax error @[1,1]

error in qdox parser
file content:
```java
offset: 1
uri: file://<WORKSPACE>/data/code-rep-dataset/Dataset2/Tasks/3580.java
text:
```scala
r@@eplacement = Name.fromString(st.nextToken(), origin);

// Copyright (c) 2000 Brian Wellington (bwelling@xbill.org)

package org.xbill.DNS;

import java.io.*;
import java.util.*;
import org.xbill.DNS.utils.*;

/**
 * Name Authority Pointer Record  - specifies rewrite rule, that when applied
 * to an existing string will produce a new domain.
 *
 * @author Chuck Santos
 */

public class NAPTRRecord extends Record {

private short order, preference;
private String flags, service, regexp;
private Name replacement;

private NAPTRRecord() {}

/**
 * Creates an NAPTR Record from the given data
 * @param order The order of this NAPTR.  Records with lower order are
 * preferred.
 * @param preference The preference, used to select between records at the
 * same order.
 * @param flags The control aspects of the NAPTRRecord.
 * @param service The service or protocol available down the rewrite path.
 * @param regexp The regular/substitution expression.
 * @param replacement The domain-name to query for the next DNS resource
 * record, depending on the value of the flags field.
 */
public
NAPTRRecord(Name _name, short _dclass, int _ttl, int _order, int _preference,
	    String _flags, String _service, String _regexp, Name _replacement)
{
	super(_name, Type.NAPTR, _dclass, _ttl);
	order = (short) _order;
	preference = (short) _preference;
	flags = _flags;
	service = _service;
	regexp = _regexp;
	replacement = _replacement;
	if (Options.check("verbose"))
		System.err.println(" NAPTR Set Member Constructor: " +
				   this.toString());
}

NAPTRRecord(Name _name, short _dclass, int _ttl, int length,
	    DataByteInputStream in)
throws IOException
{
	super(_name, Type.NAPTR, _dclass, _ttl);
	if (in == null) return;
	order = (short) in.readUnsignedShort();
	preference = (short) in.readUnsignedShort();
	flags = in.readString();
	service = in.readString();
	regexp = in.readString();
	replacement = new Name(in);
}

NAPTRRecord(Name _name, short _dclass, int _ttl, MyStringTokenizer st,
	    Name origin)
throws IOException
{
	super(_name, Type.NAPTR, _dclass, _ttl);
	order = Short.parseShort(st.nextToken());
	preference = Short.parseShort(st.nextToken());
	flags = st.nextToken();
	service = st.nextToken();
	regexp = st.nextToken();
	replacement = new Name(st.nextToken(), origin);
	if (Options.check("verbose"))
		System.err.println(" NAPTR MyStringTokenizer Constructor: " +
				   this.toString());
}

/** Converts rdata to a String */
public String
rdataToString() {
	StringBuffer sb = new StringBuffer();
	if (replacement != null) {
		sb.append(order);
		sb.append(" ");
		sb.append(preference);
		sb.append(" ");
		sb.append(flags);
		sb.append(" ");
		sb.append(service);
		sb.append(" ");
		sb.append(regexp);
		sb.append(" ");
		sb.append(replacement);
	}
	if (Options.check("verbose"))
		System.err.println(" NAPTR toString(): : " + sb.toString());
	return sb.toString();
}

/** Returns the order */
public short
getOrder() {
	return order;
}

/** Returns the preference */
public short
getPreference() {
	return preference;
}

/** Returns flags */
public String
getFlags() {
	return flags;
}

/** Returns service */
public String
getService() {
	return service;
}

/** Returns regexp */
public String
getRegexp() {
	return regexp;
}

/** Returns the replacement domain-name */
public Name
getReplacement() {
	return replacement;
}

void rrToWire(DataByteOutputStream out, Compression c) throws IOException {
	if (replacement == null && regexp == null)
		return;
	out.writeShort(order);
	out.writeShort(preference);
	out.writeString(flags);
	out.writeString(service);
	out.writeString(regexp);
	replacement.toWire(out, null);
	if (Options.check("verbose"))
		System.err.println(" NAPTR rrToWire(): " + this.toString());
}

void rrToWireCanonical(DataByteOutputStream out) throws IOException {
	if (replacement == null && regexp == null)
		return;
	out.writeShort(order);
	out.writeShort(preference);
	out.writeString(flags);
	out.writeString(service);
	out.writeString(regexp);
	replacement.toWireCanonical(out);
	if (Options.check("verbose"))
		System.err.println(" NAPTR rrToWireCanonical(): " +
				   this.toString());
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

QDox parse error in file://<WORKSPACE>/data/code-rep-dataset/Dataset2/Tasks/3580.java