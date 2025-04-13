error id: file://<WORKSPACE>/data/code-rep-dataset/Dataset2/Tasks/7232.java
file://<WORKSPACE>/data/code-rep-dataset/Dataset2/Tasks/7232.java
### com.thoughtworks.qdox.parser.ParseException: syntax error @[1,1]

error in qdox parser
file content:
```java
offset: 1
uri: file://<WORKSPACE>/data/code-rep-dataset/Dataset2/Tasks/7232.java
text:
```scala
i@@f ((s[2] & 0x3) != 0)

// Copyright (c) 1999 Brian Wellington (bwelling@xbill.org)
// Portions Copyright (c) 1999 Network Associates, Inc.

package org.xbill.DNS.utils;

import java.io.*;
import java.util.*;

/**
 * Routines for converting between Strings of base64-encoded data and arrays of
 * binary data.
 *
 * @author Brian Wellington
 */

public class base64 {

static private final String Base64 = "ABCDEFGHIJKLMNOPQRSTUVWXYZabcdefghijklmnopqrstuvwxyz0123456789+/=";

private
base64() {}

/**
 * Convert binary data to a base64-encoded String
 * @param b An array containing binary data
 * @return A String containing the encoded data
 */
public static String
toString(byte [] b) {
	ByteArrayOutputStream os = new ByteArrayOutputStream();

	for (int i = 0; i < (b.length + 2) / 3; i++) {
		short [] s = new short[3];
		short [] t = new short[4];
		for (int j = 0; j < 3; j++) {
			if ((i * 3 + j) < b.length)
				s[j] = (short) (b[i*3+j] & 0xFF);
			else
				s[j] = -1;
		}
		
		t[0] = (short) (s[0] >> 2);
		if (s[1] == -1)
			t[1] = (short) (((s[0] & 0x3) << 4));
		else
			t[1] = (short) (((s[0] & 0x3) << 4) + (s[1] >> 4));
		if (s[1] == -1)
			t[2] = t[3] = 64;
		else if (s[2] == -1) {
			t[2] = (short) (((s[1] & 0xF) << 2));
			t[3] = 64;
		}
		else {
			t[2] = (short) (((s[1] & 0xF) << 2) + (s[2] >> 6));
			t[3] = (short) (s[2] & 0x3F);
		}
		for (int j = 0; j < 4; j++)
			os.write(Base64.charAt(t[j]));
	}
	return new String(os.toByteArray());
}

/**
 * Formats data into a nicely formatted base64 encoded String
 * @param b An array containing binary data
 * @param lineLength The number of characters per line
 * @param prefix A string prefixing the characters on each line
 * @param addClose Whether to add a close parenthesis or not
 * @return A String representing the formatted output
 */
public static String
formatString(byte [] b, int lineLength, String prefix, boolean addClose) {
	String s = toString(b);
	StringBuffer sb = new StringBuffer();
	for (int i = 0; i < s.length(); i += lineLength) {
		sb.append (prefix);
		if (i + lineLength >= s.length()) {
			sb.append(s.substring(i));
			if (addClose)
				sb.append(" )");
		}
		else {
			sb.append(s.substring(i, i+64));
			sb.append("\n");
		}
	}
	return sb.toString();
}


/**
 * Convert a base64-encoded String to binary data
 * @param b A String containing the encoded data
 * @return An array containing the binary data, or null if the string is invalid
 */
public static byte []
fromString(String str) {
	if (str.length() % 4 != 0) {
		return null;
	}
	ByteArrayOutputStream bs = new ByteArrayOutputStream();
	DataOutputStream ds = new DataOutputStream(bs);

	for (int i = 0; i < (str.length() + 3) / 4; i++) {
		short [] s = new short[4];
		short [] t = new short[3];

		for (int j = 0; j < 4; j++)
			s[j] = (short) Base64.indexOf(str.charAt(i*4+j));

		t[0] = (short) ((s[0] << 2) + (s[1] >> 4));
		if (s[2] == 64) {
			t[1] = t[2] = (short) (-1);
			if ((s[1] & 0xF) != 0)
				return null;
		}
		else if (s[3] == 64) {
			t[1] = (short) (((s[1] << 4) + (s[2] >> 2)) & 0xFF);
			t[2] = (short) (-1);
			if ((s[2] & 0xF) != 0)
				return null;
		}
		else {
			t[1] = (short) (((s[1] << 4) + (s[2] >> 2)) & 0xFF);
			t[2] = (short) (((s[2] << 6) + s[3]) & 0xFF);
		}

		try {
			for (int j = 0; j < 3; j++)
				if (t[j] >= 0)
					ds.writeByte(t[j]);
		}
		catch (IOException e) {
		}
	}
	return bs.toByteArray();
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

QDox parse error in file://<WORKSPACE>/data/code-rep-dataset/Dataset2/Tasks/7232.java