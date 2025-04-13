error id: file://<WORKSPACE>/data/code-rep-dataset/Dataset3/Tasks/4398.java
file://<WORKSPACE>/data/code-rep-dataset/Dataset3/Tasks/4398.java
### com.thoughtworks.qdox.parser.ParseException: syntax error @[1,8]

error in qdox parser
file content:
```java
offset: 8
uri: file://<WORKSPACE>/data/code-rep-dataset/Dataset3/Tasks/4398.java
text:
```scala
public v@@oid add(MessageResolver messageResolver, String element) {

/*
 * Copyright 2004-2009 the original author or authors.
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
package org.springframework.ui.message.support;

import java.text.MessageFormat;
import java.util.ArrayList;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Locale;
import java.util.Map;

import org.springframework.context.MessageSource;
import org.springframework.context.i18n.LocaleContextHolder;
import org.springframework.context.support.AbstractMessageSource;
import org.springframework.core.style.ToStringCreator;
import org.springframework.ui.message.Message;
import org.springframework.ui.message.MessageContext;
import org.springframework.ui.message.MessageResolver;
import org.springframework.ui.message.Severity;
import org.springframework.util.CachingMapDecorator;

import edu.emory.mathcs.backport.java.util.Collections;

/**
 * The default message context implementation.
 * Uses a {@link MessageSource} to resolve messages that are added by callers.
 * 
 * @author Keith Donald
 */
public class DefaultMessageContext implements MessageContext {

	private MessageSource messageSource;

	@SuppressWarnings("serial")
	private Map<String, ArrayList<Message>> messagesByElement = new CachingMapDecorator<String, ArrayList<Message>>(new LinkedHashMap<String, ArrayList<Message>>()) {
		protected ArrayList<Message> create(String element) {
			return new ArrayList<Message>();
		}
	};

	/**
	 * Creates a new default message context.
	 * Defaults to a message source that simply resolves default text and cannot resolve localized message codes.
	 */
	public DefaultMessageContext() {
		init(null);
	}

	/**
	 * Creates a new default message context.
	 * @param messageSource the message source to resolve messages added to this context
	 */
	public DefaultMessageContext(MessageSource messageSource) {
		init(messageSource);
	}

	/**
	 * The message source configured to resolve message text.
	 * @return the message source
	 */
	public MessageSource getMessageSource() {
		return messageSource;
	}

	// implementing message context

	@SuppressWarnings("unchecked")
	public Map<String, List<Message>> getMessages() {
		return Collections.unmodifiableMap(messagesByElement);
	}
	
	@SuppressWarnings("unchecked")
	public List<Message> getMessages(String element) {
		List<Message> messages = messagesByElement.get(element);
		if (messages.isEmpty()) {
			return Collections.emptyList();
		}
		return Collections.unmodifiableList(messages);
	}

	public void addMessage(String element, MessageResolver messageResolver) {
		List<Message> messages = messagesByElement.get(element);
		messages.add(new ResolvableMessage(messageResolver));
	}

	public String toString() {
		return new ToStringCreator(this).append("messagesByElement", messagesByElement).toString();
	}

	// internal helpers

	private void init(MessageSource messageSource) {
		setMessageSource(messageSource);
	}

	private void setMessageSource(MessageSource messageSource) {
		if (messageSource == null) {
			messageSource = new DefaultTextFallbackMessageSource();
		}
		this.messageSource = messageSource;
	}

	private static class DefaultTextFallbackMessageSource extends AbstractMessageSource {
		protected MessageFormat resolveCode(String code, Locale locale) {
			return null;
		}
	}
	
	class ResolvableMessage implements Message {

		private MessageResolver resolver;
		
		private Message resolvedMessage;
		
		public ResolvableMessage(MessageResolver resolver) {
			this.resolver = resolver;
		}
		
		public Severity getSeverity() {
			return getMessage().getSeverity();
		}

		public String getText() {
			return getMessage().getText();
		}

		public Message getMessage() {
			if (resolvedMessage == null) {
				resolvedMessage = resolver.resolveMessage(messageSource, LocaleContextHolder.getLocale());
			}
			return resolvedMessage;
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

QDox parse error in file://<WORKSPACE>/data/code-rep-dataset/Dataset3/Tasks/4398.java