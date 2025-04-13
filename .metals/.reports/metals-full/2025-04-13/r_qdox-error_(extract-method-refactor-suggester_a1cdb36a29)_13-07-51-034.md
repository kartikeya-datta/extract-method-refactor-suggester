error id: file://<WORKSPACE>/data/code-rep-dataset/Dataset3/Tasks/9315.java
file://<WORKSPACE>/data/code-rep-dataset/Dataset3/Tasks/9315.java
### com.thoughtworks.qdox.parser.ParseException: syntax error @[1,8]

error in qdox parser
file content:
```java
offset: 8
uri: file://<WORKSPACE>/data/code-rep-dataset/Dataset3/Tasks/9315.java
text:
```scala
public C@@lass<?> getFieldType(String field) {

/*
 * Copyright 2002-2012 the original author or authors.
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

package org.springframework.web.bind;

import java.util.ArrayList;
import java.util.List;

import org.springframework.validation.Errors;
import org.springframework.validation.FieldError;
import org.springframework.validation.ObjectError;
import org.springframework.web.util.HtmlUtils;

/**
 * Errors wrapper that adds automatic HTML escaping to the wrapped instance,
 * for convenient usage in HTML views. Can be retrieved easily via
 * RequestContext's {@code getErrors} method.
 *
 * <p>Note that BindTag does <i>not</i> use this class to avoid unnecessary
 * creation of ObjectError instances. It just escapes the messages and values
 * that get copied into the respective BindStatus instance.
 *
 * @author Juergen Hoeller
 * @since 01.03.2003
 * @see org.springframework.web.servlet.support.RequestContext#getErrors
 * @see org.springframework.web.servlet.tags.BindTag
 */
public class EscapedErrors implements Errors {

	private final Errors source;


	/**
	 * Create a new EscapedErrors instance for the given source instance.
	 */
	public EscapedErrors(Errors source) {
		if (source == null) {
			throw new IllegalArgumentException("Cannot wrap a null instance");
		}
		this.source = source;
	}

	public Errors getSource() {
		return this.source;
	}


	@Override
	public String getObjectName() {
		return this.source.getObjectName();
	}

	@Override
	public void setNestedPath(String nestedPath) {
		this.source.setNestedPath(nestedPath);
	}

	@Override
	public String getNestedPath() {
		return this.source.getNestedPath();
	}

	@Override
	public void pushNestedPath(String subPath) {
		this.source.pushNestedPath(subPath);
	}

	@Override
	public void popNestedPath() throws IllegalStateException {
		this.source.popNestedPath();
	}


	@Override
	public void reject(String errorCode) {
		this.source.reject(errorCode);
	}

	@Override
	public void reject(String errorCode, String defaultMessage) {
		this.source.reject(errorCode, defaultMessage);
	}

	@Override
	public void reject(String errorCode, Object[] errorArgs, String defaultMessage) {
		this.source.reject(errorCode, errorArgs, defaultMessage);
	}

	@Override
	public void rejectValue(String field, String errorCode) {
		this.source.rejectValue(field, errorCode);
	}

	@Override
	public void rejectValue(String field, String errorCode, String defaultMessage) {
		this.source.rejectValue(field, errorCode, defaultMessage);
	}

	@Override
	public void rejectValue(String field, String errorCode, Object[] errorArgs, String defaultMessage) {
		this.source.rejectValue(field, errorCode, errorArgs, defaultMessage);
	}

	@Override
	public void addAllErrors(Errors errors) {
		this.source.addAllErrors(errors);
	}


	@Override
	public boolean hasErrors() {
		return this.source.hasErrors();
	}

	@Override
	public int getErrorCount() {
		return this.source.getErrorCount();
	}

	@Override
	public List<ObjectError> getAllErrors() {
		return escapeObjectErrors(this.source.getAllErrors());
	}

	@Override
	public boolean hasGlobalErrors() {
		return this.source.hasGlobalErrors();
	}

	@Override
	public int getGlobalErrorCount() {
		return this.source.getGlobalErrorCount();
	}

	@Override
	public List<ObjectError> getGlobalErrors() {
		return escapeObjectErrors(this.source.getGlobalErrors());
	}

	@Override
	public ObjectError getGlobalError() {
		return escapeObjectError(this.source.getGlobalError());
	}

	@Override
	public boolean hasFieldErrors() {
		return this.source.hasFieldErrors();
	}

	@Override
	public int getFieldErrorCount() {
		return this.source.getFieldErrorCount();
	}

	@Override
	public List<FieldError> getFieldErrors() {
		return this.source.getFieldErrors();
	}

	@Override
	public FieldError getFieldError() {
		return this.source.getFieldError();
	}

	@Override
	public boolean hasFieldErrors(String field) {
		return this.source.hasFieldErrors(field);
	}

	@Override
	public int getFieldErrorCount(String field) {
		return this.source.getFieldErrorCount(field);
	}

	@Override
	public List<FieldError> getFieldErrors(String field) {
		return escapeObjectErrors(this.source.getFieldErrors(field));
	}

	@Override
	public FieldError getFieldError(String field) {
		return escapeObjectError(this.source.getFieldError(field));
	}

	@Override
	public Object getFieldValue(String field) {
		Object value = this.source.getFieldValue(field);
		return (value instanceof String ? HtmlUtils.htmlEscape((String) value) : value);
	}

	@Override
	public Class getFieldType(String field) {
		return this.source.getFieldType(field);
	}

	@SuppressWarnings("unchecked")
	private <T extends ObjectError> T escapeObjectError(T source) {
		if (source == null) {
			return null;
		}
		if (source instanceof FieldError) {
			FieldError fieldError = (FieldError) source;
			Object value = fieldError.getRejectedValue();
			if (value instanceof String) {
				value = HtmlUtils.htmlEscape((String) value);
			}
			return (T) new FieldError(
					fieldError.getObjectName(), fieldError.getField(), value,
					fieldError.isBindingFailure(), fieldError.getCodes(),
					fieldError.getArguments(), HtmlUtils.htmlEscape(fieldError.getDefaultMessage()));
		}
		else {
			return (T) new ObjectError(
					source.getObjectName(), source.getCodes(), source.getArguments(),
					HtmlUtils.htmlEscape(source.getDefaultMessage()));
		}
	}

	private <T extends ObjectError> List<T> escapeObjectErrors(List<T> source) {
		List<T> escaped = new ArrayList<T>(source.size());
		for (T objectError : source) {
			escaped.add(escapeObjectError(objectError));
		}
		return escaped;
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

QDox parse error in file://<WORKSPACE>/data/code-rep-dataset/Dataset3/Tasks/9315.java