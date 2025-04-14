error id: file://<WORKSPACE>/data/code-rep-dataset/Dataset3/Tasks/9691.java
file://<WORKSPACE>/data/code-rep-dataset/Dataset3/Tasks/9691.java
### com.thoughtworks.qdox.parser.ParseException: syntax error @[1,31]

error in qdox parser
file content:
```java
offset: 31
uri: file://<WORKSPACE>/data/code-rep-dataset/Dataset3/Tasks/9691.java
text:
```scala
"Missing matrix variable '" + n@@ame + "' for method parameter type [" + paramType + "]");

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

package org.springframework.web.servlet.mvc.method.annotation;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import org.springframework.core.MethodParameter;
import org.springframework.util.CollectionUtils;
import org.springframework.util.MultiValueMap;
import org.springframework.util.StringUtils;
import org.springframework.web.bind.ServletRequestBindingException;
import org.springframework.web.bind.annotation.MatrixVariable;
import org.springframework.web.bind.annotation.ValueConstants;
import org.springframework.web.context.request.NativeWebRequest;
import org.springframework.web.context.request.RequestAttributes;
import org.springframework.web.method.annotation.AbstractNamedValueMethodArgumentResolver;
import org.springframework.web.servlet.HandlerMapping;

/**
 * Resolves method arguments annotated with an {@link MatrixVariable @PathParam}.
 *
 * <p>If the method parameter is of type Map and no name is specified, then it will
 * by resolved by the {@link MatrixVariableMapMethodArgumentResolver} instead.
 *
 * @author Rossen Stoyanchev
 * @since 3.2
 */
public class MatrixVariableMethodArgumentResolver extends AbstractNamedValueMethodArgumentResolver {

	public MatrixVariableMethodArgumentResolver() {
		super(null);
	}

	public boolean supportsParameter(MethodParameter parameter) {
		if (!parameter.hasParameterAnnotation(MatrixVariable.class)) {
			return false;
		}
		if (Map.class.isAssignableFrom(parameter.getParameterType())) {
			String paramName = parameter.getParameterAnnotation(MatrixVariable.class).value();
			return StringUtils.hasText(paramName);
		}
		return true;
	}

	@Override
	protected NamedValueInfo createNamedValueInfo(MethodParameter parameter) {
		MatrixVariable annotation = parameter.getParameterAnnotation(MatrixVariable.class);
		return new PathParamNamedValueInfo(annotation);
	}

	@Override
	protected Object resolveName(String name, MethodParameter parameter, NativeWebRequest request) throws Exception {

		@SuppressWarnings("unchecked")
		Map<String, MultiValueMap<String, String>> pathParameters =
			(Map<String, MultiValueMap<String, String>>) request.getAttribute(
					HandlerMapping.MATRIX_VARIABLES_ATTRIBUTE, RequestAttributes.SCOPE_REQUEST);

		if (CollectionUtils.isEmpty(pathParameters)) {
			return null;
		}

		String pathVar = parameter.getParameterAnnotation(MatrixVariable.class).pathVar();
		List<String> paramValues = null;

		if (!pathVar.equals(ValueConstants.DEFAULT_NONE)) {
			if (pathParameters.containsKey(pathVar)) {
				paramValues = pathParameters.get(pathVar).get(name);
			}
		}
		else {
			boolean found = false;
			paramValues = new ArrayList<String>();
			for (MultiValueMap<String, String> params : pathParameters.values()) {
				if (params.containsKey(name)) {
					if (found) {
						String paramType = parameter.getParameterType().getName();
						throw new ServletRequestBindingException(
								"Found more than one match for URI path parameter '" + name +
								"' for parameter type [" + paramType + "]. Use pathVar attribute to disambiguate.");
					}
					paramValues.addAll(params.get(name));
					found = true;
				}
			}
		}

		if (CollectionUtils.isEmpty(paramValues)) {
			return null;
		}
		else if (paramValues.size() == 1) {
			return paramValues.get(0);
		}
		else {
			return paramValues;
		}
	}

	@Override
	protected void handleMissingValue(String name, MethodParameter param) throws ServletRequestBindingException {
		String paramType = param.getParameterType().getName();
		throw new ServletRequestBindingException(
				"Missing URI path parameter '" + name + "' for method parameter type [" + paramType + "]");
	}


	private static class PathParamNamedValueInfo extends NamedValueInfo {

		private PathParamNamedValueInfo(MatrixVariable annotation) {
			super(annotation.value(), annotation.required(), annotation.defaultValue());
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

QDox parse error in file://<WORKSPACE>/data/code-rep-dataset/Dataset3/Tasks/9691.java