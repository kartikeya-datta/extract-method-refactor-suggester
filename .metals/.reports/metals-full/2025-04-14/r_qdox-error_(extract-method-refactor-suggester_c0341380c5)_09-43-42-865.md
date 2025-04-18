error id: file://<WORKSPACE>/data/code-rep-dataset/Dataset3/Tasks/14169.java
file://<WORKSPACE>/data/code-rep-dataset/Dataset3/Tasks/14169.java
### com.thoughtworks.qdox.parser.ParseException: syntax error @[1,1]

error in qdox parser
file content:
```java
offset: 1
uri: file://<WORKSPACE>/data/code-rep-dataset/Dataset3/Tasks/14169.java
text:
```scala
f@@or (Entry<String, List<String>> entry : stompHeaders.toStompHeaderMap().entrySet()) {

/*
 * Copyright 2002-2013 the original author or authors.
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *      http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */
package org.springframework.messaging.simp.stomp;

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.nio.charset.Charset;
import java.util.List;
import java.util.Map.Entry;

import org.springframework.messaging.Message;
import org.springframework.messaging.support.MessageBuilder;
import org.springframework.util.Assert;
import org.springframework.util.LinkedMultiValueMap;
import org.springframework.util.MultiValueMap;


/**
 * @author Gary Russell
 * @author Rossen Stoyanchev
 * @since 4.0
 */
public class StompMessageConverter {

	private static final Charset STOMP_CHARSET = Charset.forName("UTF-8");

	public static final byte LF = 0x0a;

	public static final byte CR = 0x0d;

	private static final byte COLON = ':';

	/**
	 * @param stompContent a complete STOMP message (without the trailing 0x00) as byte[] or String.
	 */
	public Message<?> toMessage(Object stompContent) {

		byte[] byteContent = null;
		if (stompContent instanceof String) {
			byteContent = ((String) stompContent).getBytes(STOMP_CHARSET);
		}
		else if (stompContent instanceof byte[]){
			byteContent = (byte[]) stompContent;
		}
		else {
			throw new IllegalArgumentException(
					"stompContent is neither String nor byte[]: " + stompContent.getClass());
		}

		int totalLength = byteContent.length;
		if (byteContent[totalLength-1] == 0) {
			totalLength--;
		}

		int payloadIndex = findIndexOfPayload(byteContent);
		if (payloadIndex == 0) {
			throw new StompConversionException("No command found");
		}

		String headerContent = new String(byteContent, 0, payloadIndex, STOMP_CHARSET);
		Parser parser = new Parser(headerContent);

		// TODO: validate command and whether a payload is allowed
		StompCommand command = StompCommand.valueOf(parser.nextToken(LF).trim());
		Assert.notNull(command, "No command found");

		MultiValueMap<String, String> headers = new LinkedMultiValueMap<String, String>();
		while (parser.hasNext()) {
			String header = parser.nextToken(COLON);
			if (header != null) {
				if (parser.hasNext()) {
					String value = parser.nextToken(LF);
					headers.add(header, value);
				}
				else {
					throw new StompConversionException("Parse exception for " + headerContent);
				}
			}
		}

		byte[] payload = new byte[totalLength - payloadIndex];
		System.arraycopy(byteContent, payloadIndex, payload, 0, totalLength - payloadIndex);
		StompHeaderAccessor stompHeaders = StompHeaderAccessor.create(command, headers);
		return MessageBuilder.withPayloadAndHeaders(payload, stompHeaders).build();
	}

	private int findIndexOfPayload(byte[] bytes) {
		int i;
		// ignore any leading EOL from the previous message
		for (i = 0; i < bytes.length; i++) {
			if (bytes[i] != '\n' && bytes[i] != '\r') {
				break;
			}
			bytes[i] = ' ';
		}
		int index = 0;
		for (; i < bytes.length - 1; i++) {
			if (bytes[i] == LF && bytes[i+1] == LF) {
				index = i + 2;
				break;
			}
			if ((i < (bytes.length - 3)) &&
					(bytes[i] == CR && bytes[i+1] == LF && bytes[i+2] == CR && bytes[i+3] == LF)) {
				index = i + 4;
				break;
			}
		}
		if (i >= bytes.length) {
			throw new StompConversionException("No end of headers found");
		}
		return index;
	}

	public byte[] fromMessage(Message<?> message) {

		byte[] payload;
		if (message.getPayload() instanceof byte[]) {
			payload = (byte[]) message.getPayload();
		}
		else {
			throw new IllegalArgumentException(
					"stompContent is not byte[]: " + message.getPayload().getClass());
		}

		ByteArrayOutputStream out = new ByteArrayOutputStream();
		StompHeaderAccessor stompHeaders = StompHeaderAccessor.wrap(message);

		try {
			out.write(stompHeaders.getCommand().toString().getBytes("UTF-8"));
			out.write(LF);
			for (Entry<String, List<String>> entry : stompHeaders.toNativeHeaderMap().entrySet()) {
				String key = entry.getKey();
				key = replaceAllOutbound(key);
				for (String value : entry.getValue()) {
					out.write(key.getBytes("UTF-8"));
					out.write(COLON);
					value = replaceAllOutbound(value);
					out.write(value.getBytes("UTF-8"));
					out.write(LF);
				}
			}
			out.write(LF);
			out.write(payload);
			out.write(0);
			return out.toByteArray();
		}
		catch (IOException e) {
			throw new StompConversionException("Failed to serialize " + message, e);
		}
	}

	private String replaceAllOutbound(String key) {
		return key.replaceAll("\\\\", "\\\\")
				.replaceAll(":", "\\\\c")
				.replaceAll("\n", "\\\\n")
				.replaceAll("\r", "\\\\r");
	}


	private class Parser {

		private final String content;

		private int offset;

		public Parser(String content) {
			this.content = content;
		}

		public boolean hasNext() {
			return this.offset < this.content.length();
		}

		public String nextToken(byte delimiter) {
			if (this.offset >= this.content.length()) {
				return null;
			}
			int delimAt = this.content.indexOf(delimiter, this.offset);
			if (delimAt == -1) {
				if (this.offset == this.content.length() - 1 && delimiter == COLON &&
						this.content.charAt(this.offset) == LF) {
					this.offset++;
					return null;
				}
				else if (this.offset == this.content.length() - 2 && delimiter == COLON &&
						this.content.charAt(this.offset) == CR &&
						this.content.charAt(this.offset + 1) == LF) {
					this.offset += 2;
					return null;
				}
				else {
					throw new StompConversionException("No delimiter found at offset " + offset + " in " + this.content);
				}
			}
			int escapeAt = this.content.indexOf('\\', this.offset);
			String token = this.content.substring(this.offset, delimAt + 1);
			this.offset += token.length();
			if (escapeAt >= 0 && escapeAt < delimAt) {
				char escaped = this.content.charAt(escapeAt + 1);
				if (escaped == 'n' || escaped == 'c' || escaped == '\\') {
					token = token.replaceAll("\\\\n", "\n")
							.replaceAll("\\\\r", "\r")
							.replaceAll("\\\\c", ":")
							.replaceAll("\\\\\\\\", "\\\\");
				}
				else {
					throw new StompConversionException("Invalid escape sequence \\" + escaped);
				}
			}
			int length = token.length();
			if (delimiter == LF && length > 1 && token.charAt(length - 2) == CR) {
				return token.substring(0, length - 2);
			}
			else {
				return token.substring(0, length - 1);
			}
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

QDox parse error in file://<WORKSPACE>/data/code-rep-dataset/Dataset3/Tasks/14169.java