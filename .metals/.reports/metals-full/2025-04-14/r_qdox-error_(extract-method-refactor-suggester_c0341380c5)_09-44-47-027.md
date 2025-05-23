error id: file://<WORKSPACE>/data/code-rep-dataset/Dataset3/Tasks/14646.java
file://<WORKSPACE>/data/code-rep-dataset/Dataset3/Tasks/14646.java
### com.thoughtworks.qdox.parser.ParseException: syntax error @[19,1]

error in qdox parser
file content:
```java
offset: 729
uri: file://<WORKSPACE>/data/code-rep-dataset/Dataset3/Tasks/14646.java
text:
```scala
public final class ProducesRequestCondition extends AbstractRequestCondition<ProducesRequestCondition> {

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

p@@ackage org.springframework.web.servlet.mvc.condition;

import java.util.ArrayList;
import java.util.Collection;
import java.util.Collections;
import java.util.Iterator;
import java.util.LinkedHashSet;
import java.util.List;
import java.util.Set;

import javax.servlet.http.HttpServletRequest;

import org.springframework.http.MediaType;
import org.springframework.util.StringUtils;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.servlet.mvc.condition.HeadersRequestCondition.HeaderExpression;

/**
 * A logical disjunction (' || ') request condition to match requests against producible media type expressions.
 * 
 * <p>For details on the syntax of the expressions see {@link RequestMapping#consumes()}. If the condition is 
 * created with 0 producible media type expressions, it matches to every request.
 * 
 * <p>This request condition is also capable of parsing header expressions specifically selecting 'Accept' header
 * expressions and converting them to prodicuble media type expressions.
 * 
 * @author Arjen Poutsma
 * @author Rossen Stoyanchev
 * @since 3.1
 */
public class ProducesRequestCondition extends AbstractRequestCondition<ProducesRequestCondition> {

	private final List<ProduceMediaTypeExpression> expressions;

	/**
	 * Creates a {@link ProducesRequestCondition} with the given producible media type expressions.
	 * @param produces the expressions to parse; if 0 the condition matches to every request
	 */
	public ProducesRequestCondition(String... produces) {
		this(produces, null);
	}
	
	/**
	 * Creates a {@link ProducesRequestCondition} with the given header and produces expressions.
	 * In addition to produces expressions, {@code "Accept"} header expressions are extracted and treated as
	 * producible media type expressions.
	 * @param produces the produces expressions to parse; if 0, the condition matches to all requests
	 * @param headers the header expression to parse; if 0, the condition matches to all requests
	 */
	public ProducesRequestCondition(String[] produces, String[] headers) {
		this(parseExpressions(produces, headers));
	}

	/**
	 * A private constructor.
	 */
	private ProducesRequestCondition(Collection<ProduceMediaTypeExpression> expressions) {
		this.expressions = new ArrayList<ProduceMediaTypeExpression>(expressions);
		Collections.sort(this.expressions);
	}

	private static Set<ProduceMediaTypeExpression> parseExpressions(String[] produces, String[] headers) {
		Set<ProduceMediaTypeExpression> result = new LinkedHashSet<ProduceMediaTypeExpression>();
		if (headers != null) {
			for (String header : headers) {
				HeaderExpression expr = new HeaderExpression(header);
				if ("Accept".equalsIgnoreCase(expr.name)) {
					for( MediaType mediaType : MediaType.parseMediaTypes(expr.value)) {
						result.add(new ProduceMediaTypeExpression(mediaType, expr.isNegated));
					}
				}
			}
		}
		if (produces != null) {
			for (String produce : produces) {
				result.add(new ProduceMediaTypeExpression(produce));
			}
		}
		return result;
	}

	/**
	 * Returns the producible media types contained in all expressions of this condition.
	 */
	public Set<MediaType> getMediaTypes() {
		Set<MediaType> result = new LinkedHashSet<MediaType>();
		for (ProduceMediaTypeExpression expression : expressions) {
			result.add(expression.getMediaType());
		}
		return result;
	}

	/**
	 * Returns true if this condition contains no producible media type expressions.
	 */
	public boolean isEmpty() {
		return expressions.isEmpty();
	}

	@Override
	protected Collection<ProduceMediaTypeExpression> getContent() {
		return expressions;
	}

	@Override
	protected String getToStringInfix() {
		return " || ";
	}

	/**
	 * Returns the "other" instance if "other" as long as it contains any expressions; or "this" instance otherwise.
	 * In other words "other" takes precedence over "this" as long as it contains any expressions.
	 * <p>Example: method-level "produces" overrides type-level "produces" condition. 
	 */
	public ProducesRequestCondition combine(ProducesRequestCondition other) {
		return !other.expressions.isEmpty() ? other : this;
	}

	/**
	 * Checks if any of the producible media type expressions match the given request and returns an instance that 
	 * is guaranteed to contain matching media type expressions only.
	 * 
	 * @param request the current request
	 * 
	 * @return the same instance if the condition contains no expressions; 
	 * 		or a new condition with matching expressions; or {@code null} if no expressions match.
	 */
	public ProducesRequestCondition getMatchingCondition(HttpServletRequest request) {
		if (isEmpty()) {
			return this;
		}
		Set<ProduceMediaTypeExpression> result = new LinkedHashSet<ProduceMediaTypeExpression>(expressions);
		for (Iterator<ProduceMediaTypeExpression> iterator = result.iterator(); iterator.hasNext();) {
			ProduceMediaTypeExpression expression = iterator.next();
			if (!expression.match(request)) {
				iterator.remove();
			}
		}
		return (result.isEmpty()) ? null : new ProducesRequestCondition(result);
	}

	/**
	 * Returns:
	 * <ul>
	 * 	<li>0 if the two conditions have the same number of expressions
	 * 	<li>Less than 1 if "this" has more in number or more specific producible media type expressions
	 * 	<li>Greater than 1 if "other" has more in number or more specific producible media type expressions
	 * </ul>   
	 * 
	 * <p>It is assumed that both instances have been obtained via {@link #getMatchingCondition(HttpServletRequest)}
	 * and each instance contains the matching producible media type expression only or is otherwise empty.
	 */
	public int compareTo(ProducesRequestCondition other, HttpServletRequest request) {
		String acceptHeader = request.getHeader("Accept");
		List<MediaType> acceptedMediaTypes = MediaType.parseMediaTypes(acceptHeader);
		MediaType.sortByQualityValue(acceptedMediaTypes);
		
		for (MediaType acceptedMediaType : acceptedMediaTypes) {
			int thisIndex = this.indexOfMediaType(acceptedMediaType);
			int otherIndex = other.indexOfMediaType(acceptedMediaType);
			if (thisIndex != otherIndex) {
				return otherIndex - thisIndex;
			} else if (thisIndex != -1 && otherIndex != -1) {
				ProduceMediaTypeExpression thisExpr = this.expressions.get(thisIndex);
				ProduceMediaTypeExpression otherExpr = other.expressions.get(otherIndex);
				int result = thisExpr.compareTo(otherExpr);
				if (result != 0) {
					return result;
				}
			}
		}
		return 0;
	}

	private int indexOfMediaType(MediaType mediaType) {
		for (int i = 0; i < expressions.size(); i++) {
			if (mediaType.includes(expressions.get(i).getMediaType())) {
				return i;
			}
		}
		return -1;
	}

	/**
	 * Parsing and request matching logic for producible media type expressions.
	 * @see RequestMapping#produces() 
	 */
	static class ProduceMediaTypeExpression extends MediaTypeExpression {

		ProduceMediaTypeExpression(MediaType mediaType, boolean negated) {
			super(mediaType, negated);
		}

		ProduceMediaTypeExpression(String expression) {
			super(expression);
		}

		@Override
		protected boolean match(HttpServletRequest request, MediaType mediaType) {
			List<MediaType> acceptedMediaTypes = getAcceptedMediaTypes(request);
			for (MediaType acceptedMediaType : acceptedMediaTypes) {
				if (mediaType.isCompatibleWith(acceptedMediaType)) {
					return true;
				}
			}
			return false;
		}

		private List<MediaType> getAcceptedMediaTypes(HttpServletRequest request) {
			String acceptHeader = request.getHeader("Accept");
			if (StringUtils.hasLength(acceptHeader)) {
				return MediaType.parseMediaTypes(acceptHeader);
			}
			else {
				return Collections.singletonList(MediaType.ALL);
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
	scala.collection.parallel.AdaptiveWorkStealingTasks$AWSTWrappedTask.internal(Tasks.scala:159)
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

QDox parse error in file://<WORKSPACE>/data/code-rep-dataset/Dataset3/Tasks/14646.java