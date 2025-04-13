error id: file://<WORKSPACE>/data/code-rep-dataset/Dataset3/Tasks/1700.java
file://<WORKSPACE>/data/code-rep-dataset/Dataset3/Tasks/1700.java
### com.thoughtworks.qdox.parser.ParseException: syntax error @[1,11]

error in qdox parser
file content:
```java
offset: 11
uri: file://<WORKSPACE>/data/code-rep-dataset/Dataset3/Tasks/1700.java
text:
```scala
protected R@@equestMappingInfo createRequestMappingInfo(RequestMapping annot,

/*
 * Copyright 2002-2011 the original author or authors.
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

package org.springframework.web.servlet.mvc.method.annotation;

import java.lang.reflect.Method;

import org.springframework.core.annotation.AnnotationUtils;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.servlet.mvc.condition.ConsumesRequestCondition;
import org.springframework.web.servlet.mvc.condition.HeadersRequestCondition;
import org.springframework.web.servlet.mvc.condition.ParamsRequestCondition;
import org.springframework.web.servlet.mvc.condition.PatternsRequestCondition;
import org.springframework.web.servlet.mvc.condition.ProducesRequestCondition;
import org.springframework.web.servlet.mvc.condition.RequestCondition;
import org.springframework.web.servlet.mvc.condition.RequestMethodsRequestCondition;
import org.springframework.web.servlet.mvc.method.RequestMappingInfo;
import org.springframework.web.servlet.mvc.method.RequestMappingInfoHandlerMapping;

/**
 * A sub-class of {@link RequestMappingInfoHandlerMapping} that prepares {@link RequestMappingInfo}s 
 * from @{@link RequestMapping} annotations on @{@link Controller} classes.
 *
 * @author Arjen Poutsma
 * @author Rossen Stoyanchev
 * @since 3.1
 */
public class RequestMappingHandlerMapping extends RequestMappingInfoHandlerMapping {

	private boolean useSuffixPatternMatch = true;

	/**
	 * Set whether to use a suffix pattern match (".*") when matching patterns to URLs.
	 * If enabled a method mapped to "/users" will also match to "/users.*".
	 * <p>Default is "true". Turn this convention off if you intend to interpret path mappings strictly. 
	 */
	public void setUseSuffixPatternMatch(boolean useSuffixPatternMatch) {
		this.useSuffixPatternMatch = useSuffixPatternMatch;
	}

	/**
	 * Returns the value of the useSuffixPatternMatch flag, see {@link #setUseSuffixPatternMatch(boolean)}.
	 */
	public boolean isUseSuffixPatternMatch() {
		return useSuffixPatternMatch;
	}

	/**
	 * {@inheritDoc} 
	 * The default implementation checks for the presence of a type-level {@link Controller} 
	 * annotation via {@link AnnotationUtils#findAnnotation(Class, Class)}.
	 */
	@Override
	protected boolean isHandler(Class<?> beanType) {
		return AnnotationUtils.findAnnotation(beanType, Controller.class) != null;
	}

	/**
	 * Determines if the given method is a handler method and creates a {@link RequestMappingInfo} for it. 
	 * 
	 * <p>The default implementation expects the presence of a method-level @{@link RequestMapping} 
	 * annotation via {@link AnnotationUtils#findAnnotation(Class, Class)}. The presence of 
	 * type-level annotations is also checked and if present a RequestMappingInfo is created for each type- 
	 * and method-level annotations and combined via {@link RequestMappingInfo#combine(RequestMappingInfo)}.
	 *
	 * @param method the method to create a RequestMappingInfo for
	 * @param handlerType the actual handler type, possibly a sub-type of {@code method.getDeclaringClass()}
	 * @return the info, or {@code null}
	 */
	@Override
	protected RequestMappingInfo getMappingForMethod(Method method, Class<?> handlerType) {
		RequestMapping methodAnnot = AnnotationUtils.findAnnotation(method, RequestMapping.class);
		if (methodAnnot != null) {
			RequestMapping typeAnnot = AnnotationUtils.findAnnotation(handlerType, RequestMapping.class);
			RequestMappingInfo methodInfo = createRequestMappingInfo(methodAnnot, true, method, handlerType);
			if (typeAnnot != null) {
				RequestMappingInfo typeInfo = createRequestMappingInfo(typeAnnot, false, method, handlerType);
				return typeInfo.combine(methodInfo);
			}
			else {
				return methodInfo;
			}
		}
		return null;
	}

	/**
	 * Override this method to create a {@link RequestMappingInfo} from a @{@link RequestMapping} annotation. The main 
	 * reason for doing so is to provide a custom {@link RequestCondition} to the RequestMappingInfo constructor. 
	 * 
	 * <p>This method is invoked both for type- and method-level @{@link RequestMapping} annotations. The resulting 
	 * {@link RequestMappingInfo}s are combined via {@link RequestMappingInfo#combine(RequestMappingInfo)}.
	 * 
	 * @param annot a type- or a method-level {@link RequestMapping} annotation
	 * @param isMethodAnnotation {@code true} if this is a method annotation; {@code false} if it is a type annotation
	 * @param method the method with which the created RequestMappingInfo will be combined
	 * @param handlerType the handler type
	 * @return a {@link RequestMappingInfo} instance; never {@code null}
	 */
	RequestMappingInfo createRequestMappingInfo(RequestMapping annot, 
												boolean isMethodAnnotation, 
												Method method, 
												Class<?> handlerType) {
		return new RequestMappingInfo(
				new PatternsRequestCondition(annot.value(), getUrlPathHelper(), getPathMatcher(), useSuffixPatternMatch),
				new RequestMethodsRequestCondition(annot.method()),
				new ParamsRequestCondition(annot.params()),
				new HeadersRequestCondition(annot.headers()),
				new ConsumesRequestCondition(annot.consumes(), annot.headers()),
				new ProducesRequestCondition(annot.produces(), annot.headers()), null);
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

QDox parse error in file://<WORKSPACE>/data/code-rep-dataset/Dataset3/Tasks/1700.java