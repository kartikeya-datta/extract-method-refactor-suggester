error id: file://<WORKSPACE>/data/code-rep-dataset/Dataset3/Tasks/1991.java
file://<WORKSPACE>/data/code-rep-dataset/Dataset3/Tasks/1991.java
### com.thoughtworks.qdox.parser.ParseException: syntax error @[1,1]

error in qdox parser
file content:
```java
offset: 1
uri: file://<WORKSPACE>/data/code-rep-dataset/Dataset3/Tasks/1991.java
text:
```scala
i@@f (registryInitializer != null) {

/*
 * Copyright 2002-2014 the original author or authors.
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *  http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */

package org.springframework.http.converter.protobuf;

import java.io.IOException;
import java.io.InputStreamReader;
import java.io.OutputStreamWriter;
import java.lang.reflect.Method;
import java.nio.charset.Charset;
import java.util.concurrent.ConcurrentHashMap;

import com.google.protobuf.ExtensionRegistry;
import com.google.protobuf.Message;
import com.google.protobuf.TextFormat;
import com.googlecode.protobuf.format.HtmlFormat;
import com.googlecode.protobuf.format.JsonFormat;
import com.googlecode.protobuf.format.XmlFormat;

import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpInputMessage;
import org.springframework.http.HttpOutputMessage;
import org.springframework.http.MediaType;
import org.springframework.http.converter.AbstractHttpMessageConverter;
import org.springframework.http.converter.HttpMessageNotReadableException;
import org.springframework.http.converter.HttpMessageNotWritableException;
import org.springframework.util.FileCopyUtils;


/**
 * An {@code HttpMessageConverter} that can read and write Protobuf
 * {@link com.google.protobuf.Message} using
 * <a href="https://developers.google.com/protocol-buffers/">Google Protocol buffers</a>.
 *
 * <p>By default it supports {@code "application/json"}, {@code "application/xml"},
 * {@code "text/plain"} and {@code "application/x-protobuf"} while writing also
 * supports {@code "text/html"}
 *
 * <p>To generate Message Java classes you need to install the protoc binary.
 *
 * <p>Tested against Protobuf version 2.5.0.
 *
 * @author Alex Antonov
 * @author Brian Clozel
 * @since 4.1
 */
public class ProtobufHttpMessageConverter extends AbstractHttpMessageConverter<Message> {

	public static final Charset DEFAULT_CHARSET = Charset.forName("UTF-8");

	public static final MediaType PROTOBUF = new MediaType("application", "x-protobuf", DEFAULT_CHARSET);

	public static final String X_PROTOBUF_SCHEMA_HEADER = "X-Protobuf-Schema";

	public static final String X_PROTOBUF_MESSAGE_HEADER = "X-Protobuf-Message";

	private static final ConcurrentHashMap<Class<?>, Method> methodCache = new ConcurrentHashMap<Class<?>, Method>();


	private ExtensionRegistry extensionRegistry = ExtensionRegistry.newInstance();


	/**
	 * Construct a new instance.
	 */
	public ProtobufHttpMessageConverter() {
		this(null);
	}

	/**
	 * Construct a new instance with an {@link ExtensionRegistryInitializer}
	 * that allows the registration of message extensions.
	 */
	public ProtobufHttpMessageConverter(ExtensionRegistryInitializer registryInitializer) {
		super(PROTOBUF, MediaType.TEXT_PLAIN, MediaType.APPLICATION_XML, MediaType.APPLICATION_JSON);
		if (this.extensionRegistry != null) {
			registryInitializer.initializeExtensionRegistry(this.extensionRegistry);
		}
	}


	@Override
	protected boolean supports(Class<?> clazz) {
		return Message.class.isAssignableFrom(clazz);
	}

	@Override
	protected Message readInternal(Class<? extends Message> clazz, HttpInputMessage inputMessage)
			throws IOException, HttpMessageNotReadableException {

		MediaType contentType = inputMessage.getHeaders().getContentType();
		contentType = (contentType != null ? contentType : PROTOBUF);

		Charset charset = getCharset(inputMessage.getHeaders());
		InputStreamReader reader = new InputStreamReader(inputMessage.getBody(), charset);

		try {
			Message.Builder builder = getMessageBuilder(clazz);

			if (MediaType.APPLICATION_JSON.isCompatibleWith(contentType)) {
				JsonFormat.merge(reader, this.extensionRegistry, builder);
			}
			else if (MediaType.TEXT_PLAIN.isCompatibleWith(contentType)) {
				TextFormat.merge(reader, this.extensionRegistry, builder);
			}
			else if (MediaType.APPLICATION_XML.isCompatibleWith(contentType)) {
				XmlFormat.merge(reader, this.extensionRegistry, builder);
			}
			else {
				builder.mergeFrom(inputMessage.getBody(), this.extensionRegistry);
			}
			return builder.build();
		}
		catch (Exception e) {
			throw new HttpMessageNotReadableException("Could not read Protobuf message: " + e.getMessage(), e);
		}
	}

	private Charset getCharset(HttpHeaders headers) {
		if (headers == null || headers.getContentType() == null || headers.getContentType().getCharSet() == null) {
			return DEFAULT_CHARSET;
		}
		return headers.getContentType().getCharSet();
	}

	/**
	 * Create a new {@code Message.Builder} instance for the given class.
	 * <p>This method uses a ConcurrentHashMap for caching method lookups.
	 */
	private Message.Builder getMessageBuilder(Class<? extends Message> clazz) throws Exception {
		Method method = methodCache.get(clazz);
		if (method == null) {
			method = clazz.getMethod("newBuilder");
			methodCache.put(clazz, method);
		}
		return (Message.Builder) method.invoke(clazz);
	}

	/**
	 * This method overrides the parent implementation, since this HttpMessageConverter
	 * can also produce {@code MediaType.HTML "text/html"} ContentType.
	 */
	@Override
	protected boolean canWrite(MediaType mediaType) {
		return super.canWrite(mediaType) || MediaType.TEXT_HTML.isCompatibleWith(mediaType);
	}

	@Override
	protected void writeInternal(Message message, HttpOutputMessage outputMessage)
			throws IOException, HttpMessageNotWritableException {

		MediaType contentType = outputMessage.getHeaders().getContentType();
		Charset charset = getCharset(contentType);

		if (MediaType.TEXT_HTML.isCompatibleWith(contentType)) {
			HtmlFormat.print(message, new OutputStreamWriter(outputMessage.getBody(), charset));
		}
		else if (MediaType.APPLICATION_JSON.isCompatibleWith(contentType)) {
			JsonFormat.print(message, new OutputStreamWriter(outputMessage.getBody(), charset));
		}
		else if (MediaType.TEXT_PLAIN.isCompatibleWith(contentType)) {
			TextFormat.print(message, new OutputStreamWriter(outputMessage.getBody(), charset));
		}
		else if (MediaType.APPLICATION_XML.isCompatibleWith(contentType)) {
			XmlFormat.print(message, new OutputStreamWriter(outputMessage.getBody(), charset));
		}
		else if (PROTOBUF.isCompatibleWith(contentType)) {
			setProtoHeader(outputMessage, message);
			FileCopyUtils.copy(message.toByteArray(), outputMessage.getBody());
		}
	}

	private Charset getCharset(MediaType contentType) {
		return contentType.getCharSet() != null ? contentType.getCharSet() : DEFAULT_CHARSET;
	}

	/**
	 * Set the "X-Protobuf-*" HTTP headers when responding with a message of
	 * content type "application/x-protobuf"
	 * <p><b>Note:</b> <code>outputMessage.getBody()</code> should not have been called
	 * before because it writes HTTP headers (making them read only).</p>
	 */
	private void setProtoHeader(HttpOutputMessage response, Message message) {
		response.getHeaders().set(X_PROTOBUF_SCHEMA_HEADER, message.getDescriptorForType().getFile().getName());
		response.getHeaders().set(X_PROTOBUF_MESSAGE_HEADER, message.getDescriptorForType().getFullName());
	}

	@Override
	protected MediaType getDefaultContentType(Message message) {
		return PROTOBUF;
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

QDox parse error in file://<WORKSPACE>/data/code-rep-dataset/Dataset3/Tasks/1991.java