error id: file://<WORKSPACE>/data/code-rep-dataset/Dataset3/Tasks/1192.java
file://<WORKSPACE>/data/code-rep-dataset/Dataset3/Tasks/1192.java
### com.thoughtworks.qdox.parser.ParseException: syntax error @[1,1]

error in qdox parser
file content:
```java
offset: 1
uri: file://<WORKSPACE>/data/code-rep-dataset/Dataset3/Tasks/1192.java
text:
```scala
e@@xceptionResolver.resolveException(request, response, null, ex);

package org.springframework.web.servlet.mvc.annotation;

import static org.junit.Assert.*;

import java.util.Locale;

import org.junit.Before;
import org.junit.Test;

import org.springframework.context.ApplicationContext;
import org.springframework.context.i18n.LocaleContextHolder;
import org.springframework.context.support.StaticMessageSource;
import org.springframework.http.HttpStatus;
import org.springframework.mock.web.test.MockHttpServletRequest;
import org.springframework.mock.web.test.MockHttpServletResponse;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.context.support.StaticWebApplicationContext;
import org.springframework.web.servlet.ModelAndView;

/** @author Arjen Poutsma */
public class ResponseStatusExceptionResolverTests {

	private ResponseStatusExceptionResolver exceptionResolver;

	private MockHttpServletRequest request;

	private MockHttpServletResponse response;

	@Before
	public void setUp() {
		exceptionResolver = new ResponseStatusExceptionResolver();
		request = new MockHttpServletRequest();
		response = new MockHttpServletResponse();
		request.setMethod("GET");
	}

	@Test
	public void statusCode() {
		StatusCodeException ex = new StatusCodeException();
		ModelAndView mav = exceptionResolver.resolveException(request, response, null, ex);
		assertNotNull("No ModelAndView returned", mav);
		assertTrue("No Empty ModelAndView returned", mav.isEmpty());
		assertEquals("Invalid status code", 400, response.getStatus());
	}

	@Test
	public void statusCodeAndReason() {
		StatusCodeAndReasonException ex = new StatusCodeAndReasonException();
		ModelAndView mav = exceptionResolver.resolveException(request, response, null, ex);
		assertNotNull("No ModelAndView returned", mav);
		assertTrue("No Empty ModelAndView returned", mav.isEmpty());
		assertEquals("Invalid status code", 410, response.getStatus());
		assertEquals("Invalid status reason", "You suck!", response.getErrorMessage());
	}

	@Test
	public void statusCodeAndReasonMessage() {
		Locale locale = Locale.CHINESE;
		LocaleContextHolder.setLocale(locale);
		try {
			StaticMessageSource messageSource = new StaticMessageSource();
			messageSource.addMessage("gone.reason", locale, "Gone reason message");
			exceptionResolver.setMessageSource(messageSource);

			StatusCodeAndReasonMessageException ex = new StatusCodeAndReasonMessageException();
			ModelAndView mav = exceptionResolver.resolveException(request, response, null, ex);
			assertEquals("Invalid status reason", "Gone reason message", response.getErrorMessage());
		}
		finally {
			LocaleContextHolder.resetLocaleContext();
		}
	}

	@Test
	public void notAnnotated() {
		Exception ex = new Exception();
		exceptionResolver.resolveException(request, response, null, ex);
		ModelAndView mav = exceptionResolver.resolveException(request, response, null, ex);
		assertNull("ModelAndView returned", mav);
	}

	@ResponseStatus(HttpStatus.BAD_REQUEST)
	@SuppressWarnings("serial")
	private static class StatusCodeException extends Exception {

	}

	@ResponseStatus(value = HttpStatus.GONE, reason = "You suck!")
	@SuppressWarnings("serial")
	private static class StatusCodeAndReasonException extends Exception {

	}

	@ResponseStatus(value = HttpStatus.GONE, reason = "gone.reason")
	@SuppressWarnings("serial")
	private static class StatusCodeAndReasonMessageException extends Exception {

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

QDox parse error in file://<WORKSPACE>/data/code-rep-dataset/Dataset3/Tasks/1192.java