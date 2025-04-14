error id: file://<WORKSPACE>/data/code-rep-dataset/Dataset4/Tasks/2038.java
file://<WORKSPACE>/data/code-rep-dataset/Dataset4/Tasks/2038.java
### com.thoughtworks.qdox.parser.ParseException: syntax error @[1,1]

error in qdox parser
file content:
```java
offset: 1
uri: file://<WORKSPACE>/data/code-rep-dataset/Dataset4/Tasks/2038.java
text:
```scala
i@@nt total = Math.min(available, count);

/*******************************************************************************
 * Copyright 2011 See AUTHORS file.
 * 
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 * 
 *   http://www.apache.org/licenses/LICENSE-2.0
 * 
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 ******************************************************************************/

package com.badlogic.gdx.audio;

import com.badlogic.gdx.math.MathUtils;

/** @author Nathan Sweet, xoppa */
public class CircularFloatBuffer {
	private final float[] buffer;
	private int writePosition, readPosition;
	private int available;
	
	public final int size;

	public CircularFloatBuffer (int size) {
		buffer = new float[this.size = size];
	}

	public void write (float[] data, int offset, int count) {
		int copy = 0;
		if (writePosition > readPosition || available == 0) {
			copy = Math.min(buffer.length - writePosition, count);
			System.arraycopy(data, offset, buffer, writePosition, copy);
			writePosition = (writePosition + copy) % buffer.length;
			available += copy;
			count -= copy;
			if (count == 0) return;
		}
		copy = Math.min(readPosition - writePosition, count);
		System.arraycopy(data, offset, buffer, writePosition, copy);
		writePosition += copy;
		available += copy;
	}

	public void combine (float[] data, int offset, int count) {
		int copy = 0;
		if (writePosition > readPosition || available == 0) {
			copy = Math.min(buffer.length - writePosition, count);
			combine(data, offset, buffer, writePosition, copy);
			writePosition = (writePosition + copy) % buffer.length;
			available += copy;
			count -= copy;
			if (count == 0) return;
		}
		copy = Math.min(readPosition - writePosition, count);
		combine(data, offset, buffer, writePosition, copy);
		writePosition += copy;
		available += copy;
	}

	public int read (float[] data, int offset, int count) {
		if (available == 0) return 0;

		int total = count = Math.min(available, count);

		int copy = Math.min(buffer.length - readPosition, total);
		System.arraycopy(buffer, readPosition, data, offset, copy);
		readPosition = (readPosition + copy) % buffer.length;
		available -= copy;
		count -= copy;
		if (count > 0 && available > 0) {
			copy = Math.min(buffer.length - available, count);
			System.arraycopy(buffer, readPosition, data, offset, copy);
			readPosition = (readPosition + copy) % buffer.length;
			available -= copy;
		}

		return total;
	}
	
	public int skip(int count) {
		int total = count = Math.min(available, count);
		available -= total;
		readPosition = (readPosition + total) % buffer.length;
		return total;
	}

	public void clear () {
		for (int i = 0, n = buffer.length; i < n; i++)
			buffer[i] = 0;
		readPosition = 0;
		writePosition = 0;
		available = 0;
	}

	public void setWritePosition (int writePosition) {
		this.writePosition = Math.abs(writePosition) % buffer.length;
		;
	}

	public int getWritePosition () {
		return writePosition;
	}

	public void setReadPosition (int readPosition) {
		this.readPosition = Math.abs(readPosition) % buffer.length;
	}

	public int getReadPosition () {
		return readPosition;
	}
	
	public int getAvailable() {
		return available;
	}

	private void dump () {
		for (int i = 0, n = buffer.length; i < n; i++)
			System.out.println(buffer[i] + (i == writePosition ? " <- write" : "") + (i == readPosition ? " <- read" : ""));
		System.out.println();
	}

	static private void combine (float[] src, int srcPos, float[] dest, int destPos, int length) {
		for (int i = 0; i < length; i++) {
			int destIndex = destPos + i;
			float a = src[srcPos + i];
			float b = dest[destIndex];
			dest[destIndex] = a + b;
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

QDox parse error in file://<WORKSPACE>/data/code-rep-dataset/Dataset4/Tasks/2038.java