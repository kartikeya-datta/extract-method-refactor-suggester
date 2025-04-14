error id: file://<WORKSPACE>/data/code-rep-dataset/Dataset5/Tasks/2266.java
file://<WORKSPACE>/data/code-rep-dataset/Dataset5/Tasks/2266.java
### com.thoughtworks.qdox.parser.ParseException: syntax error @[1,1]

error in qdox parser
file content:
```java
offset: 1
uri: file://<WORKSPACE>/data/code-rep-dataset/Dataset5/Tasks/2266.java
text:
```scala
t@@hrow new RuntimeException("No MD5 digest found"); //$NON-NLS-1$

/*******************************************************************************
 * Copyright (c) 2005, 2007 Remy Suen
 * All rights reserved. This program and the accompanying materials
 * are made available under the terms of the Eclipse Public License v1.0
 * which accompanies this distribution, and is available at
 * http://www.eclipse.org/legal/epl-v10.html
 *
 * Contributors:
 *    Remy Suen <remy.suen@gmail.com> - initial API and implementation
 ******************************************************************************/
package org.eclipse.ecf.protocol.msn.internal.encode;

import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;

public final class Challenge {

	public static final String PRODUCT_ID = "PROD0090YUAUV{2B"; //$NON-NLS-1$

	private static final String PRODUCT_KEY = "YMM8C_H7KCQ2S_KL"; //$NON-NLS-1$

	private static MessageDigest instance;

	static {
		try {
			instance = MessageDigest.getInstance("MD5"); //$NON-NLS-1$
		} catch (NoSuchAlgorithmException e) {
			throw new RuntimeException("No MD5 digest found");
		}
	}

	public static String createQuery(String challenge) {
		String[] s = computeMD5DigestAsStringArray((challenge + PRODUCT_KEY)
				.getBytes());
		String md5Hash = computeMD5Digest((challenge + PRODUCT_KEY).getBytes());
		int[] md5 = new int[4];
		for (int i = 0; i < 4; i++) {
			md5[i] = Integer.parseInt(s[i], 16);
		}

		String chl = challenge + PRODUCT_ID;
		while (chl.length() % 8 != 0) {
			chl += '0';
		}

		char[] array = chl.toCharArray();
		String[] values = new String[chl.length() / 4];
		for (int i = 0; i < array.length; i += 4) {
			int j = array[i + 3];
			String value = Integer.toHexString(j);
			j = array[i + 2];
			value += Integer.toHexString(j);
			j = array[i + 1];
			value += Integer.toHexString(j);
			j = array[i];
			value += Integer.toHexString(j);
			values[i / 4] = value;
		}

		int[] ints = new int[values.length];
		for (int i = 0; i < values.length; i++) {
			ints[i] = Integer.parseInt(values[i], 16);
		}

		long high = 0;
		long low = 0;
		for (int i = 0; i < ints.length; i += 2) {
			long temp = ints[i];
			temp = (temp * 0xe79a9c1L) % 0x7fffffff;
			temp += high;
			temp = md5[0] * temp + md5[1];
			temp = temp % 0x7fffffff;

			high = ints[i + 1];
			high = (high + temp) % 0x7fffffff;
			high = md5[2] * high + md5[3];
			high = high % 0x7fffffff;

			low = low + high + temp;
		}

		high = (high + md5[1]) % 0x7fffffff;
		low = (low + md5[3]) % 0x7fffffff;

		String highString = Long.toHexString(high);
		String lowString = Long.toHexString(low);

		while (highString.length() < 8) {
			highString = '0' + highString;
		}

		while (lowString.length() < 8) {
			lowString = '0' + lowString;
		}

		highString = highString.substring(6, 8) + highString.substring(4, 6)
				+ highString.substring(2, 4) + highString.substring(0, 2);
		lowString = lowString.substring(6, 8) + lowString.substring(4, 6)
				+ lowString.substring(2, 4) + lowString.substring(0, 2);

		high = Long.parseLong(highString, 16);
		low = Long.parseLong(lowString, 16);

		String first = Long.toHexString((Long.parseLong(
				md5Hash.substring(0, 8), 16) ^ high));
		String second = Long.toHexString((Long.parseLong(md5Hash.substring(8,
				16), 16) ^ low));
		String third = Long.toHexString((Long.parseLong(md5Hash.substring(16,
				24), 16) ^ high));
		String fourth = Long.toHexString((Long.parseLong(md5Hash.substring(24,
				32), 16) ^ low));

		while (first.length() < 8) {
			first = '0' + first;
		}

		while (second.length() < 8) {
			second = '0' + second;
		}

		while (third.length() < 8) {
			third = '0' + third;
		}

		while (fourth.length() < 8) {
			fourth = '0' + fourth;
		}

		return first + second + third + fourth;
	}

	/**
	 * Computes the MD5 digest of a string given its bytes.
	 * 
	 * @param bytes
	 *            the bytes of the string to be digested
	 * @return the MD5 digest of the original string
	 */
	private static final String computeMD5Digest(byte[] bytes) {
		byte[] hash = instance.digest(bytes);
		StringBuffer buffer = new StringBuffer();
		synchronized (buffer) {
			for (int i = 0; i < hash.length; i++) {
				int value = 0xff & hash[i];
				if (value < 16) {
					buffer.append('0').append(Integer.toHexString(value));
				} else {
					buffer.append(Integer.toHexString(value));
				}
			}
			return buffer.toString();
		}
	}

	private static final String[] computeMD5DigestAsStringArray(byte[] bytes) {
		byte[] hash = instance.digest(bytes);
		StringBuffer buffer = new StringBuffer();
		synchronized (buffer) {
			for (int i = 0; i < hash.length; i++) {
				int value = 0xff & hash[i];
				if (value < 16) {
					buffer.append('0').append(Integer.toHexString(value));
				} else {
					buffer.append(Integer.toHexString(value));
				}
			}
		}

		String result = buffer.toString();
		String[] results = new String[4];
		results[0] = result.substring(0, 8);
		results[1] = result.substring(8, 16);
		results[2] = result.substring(16, 24);
		results[3] = result.substring(24, 32);

		for (int i = 0; i < 4; i++) {
			char[] array = results[i].toCharArray();
			char[] swapped = new char[8];
			for (int j = 0; j < 8; j += 2) {
				swapped[7 - j] = array[j + 1];
				swapped[6 - j] = array[j];
			}
			results[i] = new String(swapped);
		}

		for (int i = 0; i < 4; i++) {
			long l = Long.parseLong(results[i], 16);
			l = l & 0x7fffffff;
			if (l < 0x10000000) {
				results[i] = '0' + Long.toHexString(l);
			} else {
				results[i] = Long.toHexString(l);
			}
		}

		return results;
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

QDox parse error in file://<WORKSPACE>/data/code-rep-dataset/Dataset5/Tasks/2266.java