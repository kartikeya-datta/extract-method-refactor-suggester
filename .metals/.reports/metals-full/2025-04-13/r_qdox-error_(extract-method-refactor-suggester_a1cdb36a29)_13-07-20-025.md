error id: file://<WORKSPACE>/data/code-rep-dataset/Dataset3/Tasks/22.java
file://<WORKSPACE>/data/code-rep-dataset/Dataset3/Tasks/22.java
### com.thoughtworks.qdox.parser.ParseException: syntax error @[18,1]

error in qdox parser
file content:
```java
offset: 694
uri: file://<WORKSPACE>/data/code-rep-dataset/Dataset3/Tasks/22.java
text:
```scala
public class BeanMethodInterceptor extends AbstractMethodInterceptor {

/*
 * Copyright 2002-2008 the original author or authors.
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
p@@ackage org.springframework.config.java.ext;

import static java.lang.String.*;

import java.lang.reflect.Method;

import net.sf.cglib.proxy.MethodProxy;

import org.springframework.beans.factory.config.ConfigurableBeanFactory;


/**
 * Intercepts the invocation of any {@link Bean}-annotated methods in order to ensure proper
 * handling of bean semantics such as scoping and AOP proxying.
 * 
 * @see Bean
 * @see BeanRegistrar
 * 
 * @author Chris Beams
 */
class BeanMethodInterceptor extends AbstractMethodInterceptor {

	/**
	 * Enhances a {@link Bean @Bean} method to check the supplied BeanFactory for the
	 * existence of this bean object.
	 */
	public Object intercept(Object obj, Method method, Object[] args, MethodProxy proxy) throws Throwable {
		String beanName = getBeanName(method);

		// TODO: re-enable for @ScopedProxy support
		// boolean isScopedProxy = (AnnotationUtils.findAnnotation(method,
		// ScopedProxy.class) != null);
		//
		// String scopedBeanName =
		// ScopedProxy.Util.resolveHiddenScopedProxyBeanName(beanName);
		// if (isScopedProxy && beanFactory.isCurrentlyInCreation(scopedBeanName))
		// beanName = scopedBeanName;

		if (factoryContainsBean(beanName)) {
			// we have an already existing cached instance of this bean -> retrieve it
			Object cachedBean = beanFactory.getBean(beanName);
			if (log.isInfoEnabled())
				log.info(format("Returning cached singleton object [%s] for @Bean method %s.%s", cachedBean,
				        method.getDeclaringClass().getSimpleName(), beanName));

			return cachedBean;
		}

		return proxy.invokeSuper(obj, args);
	}

	/**
	 * Check the beanFactory to see whether the bean named <var>beanName</var> already
	 * exists. Accounts for the fact that the requested bean may be "in creation", i.e.:
	 * we're in the middle of servicing the initial request for this bean. From JavaConfig's
	 * perspective, this means that the bean does not actually yet exist, and that it is now
	 * our job to create it for the first time by executing the logic in the corresponding
	 * Bean method.
	 * <p>
	 * Said another way, this check repurposes
	 * {@link ConfigurableBeanFactory#isCurrentlyInCreation(String)} to determine whether
	 * the container is calling this method or the user is calling this method.
	 * 
	 * @param beanName name of bean to check for
	 * 
	 * @return true if <var>beanName</var> already exists in beanFactory
	 */
	private boolean factoryContainsBean(String beanName) {
		return beanFactory.containsBean(beanName) && !beanFactory.isCurrentlyInCreation(beanName);
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

QDox parse error in file://<WORKSPACE>/data/code-rep-dataset/Dataset3/Tasks/22.java