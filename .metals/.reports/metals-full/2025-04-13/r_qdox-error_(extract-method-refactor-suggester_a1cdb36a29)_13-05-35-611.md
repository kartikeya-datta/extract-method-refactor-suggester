error id: file://<WORKSPACE>/data/code-rep-dataset/Dataset3/Tasks/8540.java
file://<WORKSPACE>/data/code-rep-dataset/Dataset3/Tasks/8540.java
### com.thoughtworks.qdox.parser.ParseException: syntax error @[18,1]

error in qdox parser
file content:
```java
offset: 712
uri: file://<WORKSPACE>/data/code-rep-dataset/Dataset3/Tasks/8540.java
text:
```scala
final class DefaultMessageResolver implements MessageResolver, MessageSourceResolvable {

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
p@@ackage org.springframework.ui.message;

import java.util.Locale;
import java.util.Map;

import org.springframework.context.MessageSource;
import org.springframework.context.MessageSourceResolvable;
import org.springframework.context.expression.MapAccessor;
import org.springframework.core.style.ToStringCreator;
import org.springframework.expression.AccessException;
import org.springframework.expression.EvaluationContext;
import org.springframework.expression.EvaluationException;
import org.springframework.expression.Expression;
import org.springframework.expression.ExpressionParser;
import org.springframework.expression.ParseException;
import org.springframework.expression.ParserContext;
import org.springframework.expression.PropertyAccessor;
import org.springframework.expression.TypedValue;
import org.springframework.expression.spel.support.StandardEvaluationContext;

class DefaultMessageResolver implements MessageResolver, MessageSourceResolvable {

	private Severity severity;

	private String[] codes;

	private Map<String, Object> args;
	
	private String defaultText;

	private ExpressionParser expressionParser;

	public DefaultMessageResolver(Severity severity, String[] codes, Map<String, Object> args,
			String defaultText, ExpressionParser expressionParser) {
		this.severity = severity;
		this.codes = codes;
		this.args = args;
		this.defaultText = defaultText;
		this.expressionParser = expressionParser;
	}

	// implementing MessageResolver

	public Message resolveMessage(MessageSource messageSource, Locale locale) {
		String messageString = messageSource.getMessage(this, locale);
		Expression message;
		try {
			message = expressionParser.parseExpression(messageString, ParserContext.TEMPLATE_EXPRESSION);
		} catch (ParseException e) {
			throw new MessageResolutionException("Failed to parse message expression", e);
		}
		try {
			StandardEvaluationContext context = new StandardEvaluationContext();
			context.setRootObject(args);
			context.addPropertyAccessor(new MapAccessor());
			context.addPropertyAccessor(new MessageSourceResolvableAccessor(messageSource, locale));
			String text = (String) message.getValue(context);
			return new TextMessage(severity, text);
		} catch (EvaluationException e) {
			throw new MessageResolutionException("Failed to evaluate expression to generate message text", e);
		}
	}

	// implementing MessageSourceResolver

	public String[] getCodes() {
		return codes;
	}

	public Object[] getArguments() {
		return null;
	}

	public String getDefaultMessage() {
		return defaultText;
	}

	public String toString() {
		return new ToStringCreator(this).append("severity", severity).append("codes", codes).append("defaultText",
				defaultText).toString();
	}

	private static class TextMessage implements Message {

		private Severity severity;

		private String text;

		public TextMessage(Severity severity, String text) {
			this.severity = severity;
			this.text = text;
		}

		public Severity getSeverity() {
			return severity;
		}

		public String getText() {
			return text;
		}

	}

	private static class MessageSourceResolvableAccessor implements PropertyAccessor {

		private MessageSource messageSource;
		
		private Locale locale;
		
		public MessageSourceResolvableAccessor(MessageSource messageSource, Locale locale) {
			this.messageSource = messageSource;
			this.locale = locale;
		}

		public boolean canRead(EvaluationContext context, Object target, String name) throws AccessException {
			return true;
		}

		public TypedValue read(EvaluationContext context, Object target, String name) throws AccessException {
			// TODO this does not get called when resolving MessageSourceResolvable variables; only when accessing properties on MessageSourceResolvable targets.
			return new TypedValue(messageSource.getMessage((MessageSourceResolvable)target, locale));
		}

		public boolean canWrite(EvaluationContext context, Object target, String name) throws AccessException {
			return false;
		}

		public void write(EvaluationContext context, Object target, String name, Object newValue) throws AccessException {
			throw new UnsupportedOperationException("Should not be called");
		}

		public Class<?>[] getSpecificTargetClasses() {
			return new Class[] { MessageSourceResolvable.class };
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

QDox parse error in file://<WORKSPACE>/data/code-rep-dataset/Dataset3/Tasks/8540.java