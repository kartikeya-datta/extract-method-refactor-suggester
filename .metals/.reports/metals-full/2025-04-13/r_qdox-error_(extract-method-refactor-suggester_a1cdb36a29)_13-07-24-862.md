error id: file://<WORKSPACE>/data/code-rep-dataset/Dataset3/Tasks/12845.java
file://<WORKSPACE>/data/code-rep-dataset/Dataset3/Tasks/12845.java
### com.thoughtworks.qdox.parser.ParseException: syntax error @[1,1]

error in qdox parser
file content:
```java
offset: 1
uri: file://<WORKSPACE>/data/code-rep-dataset/Dataset3/Tasks/12845.java
text:
```scala
t@@his.expectedContentLength = StompHeaderAccessor.getContentLength(headers);

/*
 * Copyright 2002-2014 the original author or authors.
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 * http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */

package org.springframework.messaging.simp.stomp;


import org.springframework.messaging.Message;
import org.springframework.util.Assert;
import org.springframework.util.LinkedMultiValueMap;
import org.springframework.util.MultiValueMap;

import java.nio.ByteBuffer;
import java.util.Collections;
import java.util.List;
import java.util.Queue;
import java.util.concurrent.LinkedBlockingQueue;


/**
 * An extension of {@link org.springframework.messaging.simp.stomp.StompDecoder}
 * that buffers content remaining in the input ByteBuffer after the parent
 * class has read all (complete) STOMP frames from it. The remaining content
 * represents an incomplete STOMP frame. When called repeatedly with additional
 * data, the decode method returns one or more messages or, if there is not
 * enough data still, continues to buffer.
 *
 * <p>A single instance of this decoder can be invoked repeatedly to read all
 * messages from a single stream (e.g. WebSocket session) as long as decoding
 * does not fail. If there is an exception, StompDecoder instance should not
 * be used any more as its internal state is not guaranteed to be consistent.
 * It is expected that the underlying session is closed at that point.
 *
 * @author Rossen Stoyanchev
 * @since 4.0.3
 */
public class BufferingStompDecoder extends StompDecoder {

	private final int bufferSizeLimit;

	private final Queue<ByteBuffer> chunks = new LinkedBlockingQueue<ByteBuffer>();

	private volatile Integer expectedContentLength;


	public BufferingStompDecoder(int bufferSizeLimit) {
		Assert.isTrue(bufferSizeLimit > 0, "Buffer size must be greater than 0");
		this.bufferSizeLimit = bufferSizeLimit;
	}


	/**
	 * Return the configured buffer size limit.
	 */
	public int getBufferSizeLimit() {
		return this.bufferSizeLimit;
	}

	/**
	 * Calculate the current buffer size.
	 */
	public int getBufferSize() {
		int size = 0;
		for (ByteBuffer buffer : this.chunks) {
			size = size + buffer.remaining();
		}
		return size;
	}

	/**
	 * Get the expected content length of the currently buffered, incomplete STOMP frame.
	 */
	public Integer getExpectedContentLength() {
		return this.expectedContentLength;
	}


	/**
	 * Decodes one or more STOMP frames from the given {@code ByteBuffer} into a
	 * list of {@link Message}s.
	 *
	 * <p>If there was enough data to parse a "content-length" header, then the
	 * value is used to determine how much more data is needed before a new
	 * attempt to decode is made.
	 *
	 * <p>If there was not enough data to parse the "content-length", or if there
	 * is "content-length" header, every subsequent call to decode attempts to
	 * parse again with all available data. Therefore the presence of a "content-length"
	 * header helps to optimize the decoding of large messages.
	 *
	 * @param newBuffer a buffer containing new data to decode
	 *
	 * @return decoded messages or an empty list
	 * @throws StompConversionException raised in case of decoding issues
	 */
	@Override
	public List<Message<byte[]>> decode(ByteBuffer newBuffer) {

		this.chunks.add(newBuffer);

		checkBufferLimits();

		if (getExpectedContentLength() != null && getBufferSize() < this.expectedContentLength) {
			return Collections.<Message<byte[]>>emptyList();
		}

		ByteBuffer bufferToDecode = assembleChunksAndReset();

		MultiValueMap<String, String> headers = new LinkedMultiValueMap<String, String>();
		List<Message<byte[]>> messages = decode(bufferToDecode, headers);

		if (bufferToDecode.hasRemaining()) {
			this.chunks.add(bufferToDecode);
			this.expectedContentLength = getContentLength(headers);
		}

		return messages;
	}

	private void checkBufferLimits() {
		if (getExpectedContentLength() != null) {
			if (getExpectedContentLength() > getBufferSizeLimit()) {
				throw new StompConversionException(
						"The 'content-length' header " + getExpectedContentLength() +
								"  exceeds the configured message buffer size limit " + getBufferSizeLimit());
			}
		}
		if (getBufferSize() > getBufferSizeLimit()) {
			throw new StompConversionException("The configured stomp frame buffer size limit of " +
					getBufferSizeLimit() + " bytes has been exceeded");

		}
	}

	private ByteBuffer assembleChunksAndReset() {
		ByteBuffer result;
		if (this.chunks.size() == 1) {
			result = this.chunks.remove();
		}
		else {
			result = ByteBuffer.allocate(getBufferSize());
			for (ByteBuffer partial : this.chunks) {
				result.put(partial);
			}
			result.flip();
		}
		this.chunks.clear();
		this.expectedContentLength = null;
		return result;
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

QDox parse error in file://<WORKSPACE>/data/code-rep-dataset/Dataset3/Tasks/12845.java