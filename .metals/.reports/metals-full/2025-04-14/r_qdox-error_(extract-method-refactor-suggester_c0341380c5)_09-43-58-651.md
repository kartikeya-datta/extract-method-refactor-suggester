error id: file://<WORKSPACE>/data/code-rep-dataset/Dataset4/Tasks/8707.java
file://<WORKSPACE>/data/code-rep-dataset/Dataset4/Tasks/8707.java
### com.thoughtworks.qdox.parser.ParseException: syntax error @[1,8]

error in qdox parser
file content:
```java
offset: 8
uri: file://<WORKSPACE>/data/code-rep-dataset/Dataset4/Tasks/8707.java
text:
```scala
public b@@yte readByte () throws IOException {

/*
 * Copyright 2010 Mario Zechner (contact@badlogicgames.com), Nathan Sweet (admin@esotericsoftware.com)
 * 
 * Licensed under the Apache License, Version 2.0 (the "License"); you may not use this file except in compliance with the
 * License. You may obtain a copy of the License at
 * 
 * http://www.apache.org/licenses/LICENSE-2.0
 * 
 * Unless required by applicable law or agreed to in writing, software distributed under the License is distributed on an "AS IS"
 * BASIS, WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied. See the License for the specific language
 * governing permissions and limitations under the License.
 */

package com.badlogic.gdx.utils;

import java.io.EOFException;
import java.io.FilterInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.UTFDataFormatException;

/**
 * Taken from http://www.javafaq.nu/java-example-code-1079.html
 * @author mzechner
 * 
 */
public class LittleEndianInputStream extends FilterInputStream {

	public LittleEndianInputStream (InputStream in) {
		super(in);
	}

	public boolean readBoolean () throws IOException {
		int bool = in.read();
		if (bool == -1) throw new EOFException();
		return (bool != 0);
	}

	public byte readByte (int b) throws IOException {
		int temp = in.read();
		if (temp == -1) throw new EOFException();
		return (byte)temp;
	}

	public int readUnsignedByte () throws IOException {
		int temp = in.read();
		if (temp == -1) throw new EOFException();
		return temp;
	}

	public short readShort () throws IOException {
		int byte1 = in.read();
		int byte2 = in.read();
		// only need to test last byte read
		// if byte1 is -1 so is byte2
		if (byte2 == -1) throw new EOFException();
		return (short)(((byte2 << 24) >>> 16) + (byte1 << 24) >>> 24);
	}

	public int readUnsignedShort () throws IOException {
		int byte1 = in.read();
		int byte2 = in.read();
		if (byte2 == -1) throw new EOFException();
		return ((byte2 << 24) >> 16) + ((byte1 << 24) >> 24);
	}

	public char readChar () throws IOException {
		int byte1 = in.read();
		int byte2 = in.read();
		if (byte2 == -1) throw new EOFException();
		return (char)(((byte2 << 24) >>> 16) + ((byte1 << 24) >>> 24));
	}

	public int readInt () throws IOException {

		int byte1 = in.read();
		int byte2 = in.read();
		int byte3 = in.read();
		int byte4 = in.read();
		if (byte4 == -1) {
			throw new EOFException();
		}
		return (byte4 << 24) + ((byte3 << 24) >>> 8) + ((byte2 << 24) >>> 16) + ((byte1 << 24) >>> 24);

	}

	public long readLong () throws IOException {

		long byte1 = in.read();
		long byte2 = in.read();
		long byte3 = in.read();
		long byte4 = in.read();
		long byte5 = in.read();
		long byte6 = in.read();
		long byte7 = in.read();
		long byte8 = in.read();
		if (byte8 == -1) {
			throw new EOFException();
		}
		return (byte8 << 56) + ((byte7 << 56) >>> 8) + ((byte6 << 56) >>> 16) + ((byte5 << 56) >>> 24) + ((byte4 << 56) >>> 32)
			+ ((byte3 << 56) >>> 40) + ((byte2 << 56) >>> 48) + ((byte1 << 56) >>> 56);

	}

	public String readUTF () throws IOException {

		int byte1 = in.read();
		int byte2 = in.read();
		if (byte2 == -1) throw new EOFException();
		int numbytes = (byte1 << 8) + byte2;
		char result[] = new char[numbytes];
		int numread = 0;
		int numchars = 0;

		while (numread < numbytes) {

			int c1 = readUnsignedByte();

			// The first 4 bits of c1 determine how many bytes are in this char
			int test = c1 >> 4;
			if (test < 8) { // one byte
				numread++;
				result[numchars++] = (char)c1;
			} else if (test == 12 || test == 13) { // 2 bytes
				numread += 2;
				if (numread > numbytes) throw new UTFDataFormatException();
				int c2 = readUnsignedByte();
				if ((c2 & 0xC0) != 0x80) throw new UTFDataFormatException();
				result[numchars++] = (char)(((c1 & 0x1F) << 6) | (c2 & 0x3F));
			} else if (test == 14) { // three bytes
				numread += 3;
				if (numread > numbytes) throw new UTFDataFormatException();
				int c2 = readUnsignedByte();
				int c3 = readUnsignedByte();
				if (((c2 & 0xC0) != 0x80) || ((c3 & 0xC0) != 0x80)) {
					throw new UTFDataFormatException();
				}
				result[numchars++] = (char)(((c1 & 0x0F) << 12) | ((c2 & 0x3F) << 6) | (c3 & 0x3F));
			} else { // malformed
				throw new UTFDataFormatException();
			}

		} // end while

		return new String(result, 0, numchars);

	}

	public final double readDouble () throws IOException {
		return Double.longBitsToDouble(this.readLong());
	}

	public final float readFloat () throws IOException {
		return Float.intBitsToFloat(this.readInt());
	}

	public final int skipBytes (int n) throws IOException {
		for (int i = 0; i < n; i += (int)skip(n - i))
			;
		return n;
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

QDox parse error in file://<WORKSPACE>/data/code-rep-dataset/Dataset4/Tasks/8707.java