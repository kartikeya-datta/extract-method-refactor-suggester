error id: file://<WORKSPACE>/data/code-rep-dataset/Dataset3/Tasks/7838.java
file://<WORKSPACE>/data/code-rep-dataset/Dataset3/Tasks/7838.java
### com.thoughtworks.qdox.parser.ParseException: syntax error @[18,1]

error in qdox parser
file content:
```java
offset: 706
uri: file://<WORKSPACE>/data/code-rep-dataset/Dataset3/Tasks/7838.java
text:
```scala
public final class MvcDefaultServletHandler extends AbstractFeatureSpecification {

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
p@@ackage org.springframework.web.servlet.config;

import org.springframework.beans.factory.parsing.ProblemCollector;
import org.springframework.context.config.AbstractFeatureSpecification;
import org.springframework.context.config.FeatureSpecificationExecutor;
import org.springframework.web.servlet.handler.SimpleUrlHandlerMapping;
import org.springframework.web.servlet.mvc.HttpRequestHandlerAdapter;
import org.springframework.web.servlet.resource.DefaultServletHttpRequestHandler;

/**
 * Specifies the Spring MVC "default-servlet-handler" container feature. The
 * feature provides the following fine-grained configuration:
 *
 * <ul>
 * 	<li>{@link DefaultServletHttpRequestHandler} for serving static files by 
 * 		forwarding to the Servlet container's "default" Servlet. 
 * 	<li>{@link SimpleUrlHandlerMapping} to map the above request handler to "/**"
 * 	<li>{@link HttpRequestHandlerAdapter} to enable the DispatcherServlet to be
 *     able to invoke the above request handler.
 * </ul>
 *
 * This handler will forward all requests to the default Servlet. Therefore 
 * it is important that it remains last in the order of all other URL 
 * HandlerMappings. That will be the case if you use the {@link MvcAnnotationDriven} 
 * feature or alternatively if you are setting up your customized HandlerMapping 
 * instance be sure to set its "order" property to a value lower than that of 
 * the DefaultServletHttpRequestHandler, which is Integer.MAX_VALUE. 
 * 
 * @author Rossen Stoyanchev
 * @since 3.1
 */
public class MvcDefaultServletHandler extends AbstractFeatureSpecification {

	private static final Class<? extends FeatureSpecificationExecutor> EXECUTOR_TYPE = MvcDefaultServletHandlerExecutor.class;

	private String defaultServletName;

	/**
	 * <p>Creates an instance of MvcDefaultServletHandler without.
	 * If this constructor is used the {@link DefaultServletHttpRequestHandler} 
	 * will try to auto-detect the container's default Servlet at startup time 
	 * using a list of known names.
	 *   
	 * <p>If the default Servlet cannot be detected because of using an 
	 * unknown container or because it has been manually configured, an 
	 * alternate constructor provided here can be used to specify the 
	 * servlet name explicitly.
	 */
	public MvcDefaultServletHandler() {
		super(EXECUTOR_TYPE);
	}

	/**
	 * The name of the default Servlet to forward to for static resource requests.  
	 * The {@link DefaultServletHttpRequestHandler} will try to auto-detect the 
	 * container's default Servlet at startup time using a list of known names.  
	 * However if the default Servlet cannot be detected because of using an unknown 
	 * container or because it has been manually configured, you can use this 
	 * constructor to set the servlet name explicitly.
	 * 
	 * @param defaultServletName the name of the default servlet
	 */
	public MvcDefaultServletHandler(String defaultServletName) {
		this();
		this.defaultServletName = defaultServletName;
	}

	String defaultServletName() {
		return this.defaultServletName;
	}

	@Override
	protected void doValidate(ProblemCollector problems) {
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

QDox parse error in file://<WORKSPACE>/data/code-rep-dataset/Dataset3/Tasks/7838.java