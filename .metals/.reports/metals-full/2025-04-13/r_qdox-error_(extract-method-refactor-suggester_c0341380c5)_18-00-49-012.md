error id: file://<WORKSPACE>/data/code-rep-dataset/Dataset2/Tasks/9231.java
file://<WORKSPACE>/data/code-rep-dataset/Dataset2/Tasks/9231.java
### com.thoughtworks.qdox.parser.ParseException: syntax error @[1,1]

error in qdox parser
file content:
```java
offset: 1
uri: file://<WORKSPACE>/data/code-rep-dataset/Dataset2/Tasks/9231.java
text:
```scala
i@@f (s == null || s.length() == 0 || !Character.isDigit(s.charAt(0)))

// Copyright (c) 1999-2004 Brian Wellington (bwelling@xbill.org)

package org.xbill.DNS;

/**
 * Routines for parsing BIND-style TTL values.  These values consist of
 * numbers followed by 1 letter units of time (W - week, D - day, H - hour,
 * M - minute, S - second).
 *
 * @author Brian Wellington
 */

public final class TTL {

private
TTL() {}

static void
check(long i) {
	if (i < 0 || i > 0xFFFFFFFFL)
		throw new InvalidTTLException(i);
}

/**
 * Parses a BIND-stype TTL
 * @return The TTL as a number of seconds
 * @throws NumberFormatException The TTL was not a valid TTL.
 */
public static long
parseTTL(String s) {
	if (s == null || !Character.isDigit(s.charAt(0)))
		throw new NumberFormatException();
	long value = 0;
	long ttl = 0;
	for (int i = 0; i < s.length(); i++) {
		char c = s.charAt(i);
		long oldvalue = value;
		if (Character.isDigit(c)) {
			value = (value * 10) + Character.getNumericValue(c);
			if (value < oldvalue)
				throw new NumberFormatException();
		} else {
			switch (Character.toUpperCase(c)) {
				case 'W': value *= 7;
				case 'D': value *= 24;
				case 'H': value *= 60;
				case 'M': value *= 60;
				case 'S': break;
				default:  throw new NumberFormatException();
			}
			ttl += value;
			value = 0;
			if (ttl > 0xFFFFFFFFL)
				throw new NumberFormatException();
		}
	}
	if (ttl == 0) {
		ttl = value;
		if (ttl > 0xFFFFFFFFL)
			throw new NumberFormatException();
	}
	return ttl;
}

public static String
format(long ttl) {
	TTL.check(ttl);
	StringBuffer sb = new StringBuffer();
	long secs, mins, hours, days, weeks;
	secs = ttl % 60;
	ttl /= 60;
	mins = ttl % 60;
	ttl /= 60;
	hours = ttl % 24;
	ttl /= 24;
	days = ttl % 7;
	ttl /= 7;
	weeks = ttl;
	if (weeks > 0)
		sb.append(weeks + "W");
	if (days > 0)
		sb.append(days + "D");
	if (hours > 0)
		sb.append(hours + "H");
	if (mins > 0)
		sb.append(mins + "M");
	if (secs > 0 || (weeks == 0 && days == 0 && hours == 0 && mins == 0))
		sb.append(secs + "S");
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

QDox parse error in file://<WORKSPACE>/data/code-rep-dataset/Dataset2/Tasks/9231.java