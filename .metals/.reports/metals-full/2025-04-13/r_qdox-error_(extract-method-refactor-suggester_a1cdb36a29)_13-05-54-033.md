error id: file://<WORKSPACE>/data/code-rep-dataset/Dataset5/Tasks/12217.java
file://<WORKSPACE>/data/code-rep-dataset/Dataset5/Tasks/12217.java
### com.thoughtworks.qdox.parser.ParseException: syntax error @[1,1]

error in qdox parser
file content:
```java
offset: 1
uri: file://<WORKSPACE>/data/code-rep-dataset/Dataset5/Tasks/12217.java
text:
```scala
g@@etPages().setHomePage(SpringWebPage.class);

/*
 * $Id$
 * $Revision$
 * $Date$
 *
 * ====================================================================
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
package wicket.examples.springframework2;

import wicket.examples.springframework2.SpringApplication;
import wicket.util.time.Duration;

/**
 * Here are some short explanations:
 * I used your approach of a SpringApplicationController and a
 * SpringApplication. But you can also use the nromal deployment model of
 * Wicket, if you want to use the SpringBeanModel.
 *
 * I differ between the ApplicationContext of the DispatcherServlet and
 * the ApplicationContext for the middle tier components (this is what
 * SpringBeanModel wants to access): The first is configured in web.xml as
 * you did, the later is configured by setting up a
 * org.springframework.web.context.ContextLoaderListener and the context
 * parameter "contextConfigLocation" in web.xml
 *
 * The SpringContextLocator encapsulates how to get a
 * ApplicationContext. It therefor provides two static methods.
 *
 * SpringBeanModel is the base for Spring bean access. It tries to
 * access the ApplicationContext of the middle tier by calling
 * SpringContextLocator.getApplicationContext(Session) (in the request we
 * would find the DispatcherServlet context). The middle tier context is
 * independent of the DispatcherServlet, because it's set by the
 * ContextLoaderListener, so the locator will also work, if you don't use
 * your SpringApplicationController approach. Instead you can configure the
 * Wicket application framework directly in web.xml !
 *
 * SpringBeanPropertyModel provides OGNL support
 *
 * There are so many possible ways of configuration with Spring. I like
 * your approach of integration with the mvc framework of Spring, because
 * it give me the possibility to configure the objects and the wiring of
 * ApplicationSettings and WicketServlet and I can also use some advanced
 * features of the Spring framework (e.g. interception). But if one wants
 * to set up the Wicket servlet in web.xml, he could also use the
 * SpringBeanModel when he also sets up the ContextLoaderListener.
 *
 * I tried to configure the DispatcherServlet to use the values set with
 * the context parameter contextConfigLocation, but I failed. Without the
 * Listener the spring.xml context is not available. Perhaps there is a way
 * I cannot see.
 *
 * After I had a look at the PersistentObjectModel and its subclass, I'm
 * sure that IModel and IDetachableModel is a good integration point for
 * Wicket and Spring.
 *
 * @author Martin Frey
 */
public class SpringExampleApplication extends SpringApplication
{
    /**
     * 
     * @see wicket.examples.springframework.SpringApplication#initSettings()
     */
    public void initSettings()
    {
        getPages().setHomePage(SpringHtmlPage.class);
        getSettings().setResourcePollFrequency(Duration.ONE_SECOND);
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

QDox parse error in file://<WORKSPACE>/data/code-rep-dataset/Dataset5/Tasks/12217.java