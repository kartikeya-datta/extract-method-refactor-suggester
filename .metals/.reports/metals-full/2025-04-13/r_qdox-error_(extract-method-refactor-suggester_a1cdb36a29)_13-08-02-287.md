error id: file://<WORKSPACE>/data/code-rep-dataset/Dataset5/Tasks/17059.java
file://<WORKSPACE>/data/code-rep-dataset/Dataset5/Tasks/17059.java
### com.thoughtworks.qdox.parser.ParseException: syntax error @[105,2]

error in qdox parser
file content:
```java
offset: 3736
uri: file://<WORKSPACE>/data/code-rep-dataset/Dataset5/Tasks/17059.java
text:
```scala
import wicket.protocol.http.WebApplication;

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
package wicket.examples.springframework;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.springframework.web.servlet.ModelAndView;
import org.springframework.web.servlet.mvc.AbstractController;

import wicket.WebApplication;
import wicket.protocol.http.WicketServlet;

/**
 * I'm not sure this is the best approach to integrate Spring and Wicket.
 * But beside that you now may use all of Spring's ApplicationContext capabilities,  
 * you may simply add Wicket functionality to existing Spring applications.
 * 
 * The apprach taken is to create a Spring controller derived from 
 * AbstractController and forward handleRequestInternal() to Wicket's
 * Servlet object and it's doGet() method.
 * 
 * The Wicket application to use must be configured with Spring's web application
 * context (<servlet-name>-servlet.xml). 
 * 
 * @author Juergen Donnerstag
 */
public class SpringApplicationController extends AbstractController 
{
    /** Logging */
    private static Log log = LogFactory.getLog(SpringApplicationController.class);

    /** The Wicket application object */
    private SpringApplication application;

    /**
     * JavaBean method to provide Wicket's application object. Will be set
     * through Spring BeanFactory and WebApplicationContext.
     * <servlet-name>-servlet.xml
     * 
     * @param application Wicket application object
     */
    public void setApplication(final SpringApplication application)
    {
        this.application = application;
        this.application.setWicketServlet(new WicketSpringServlet(application));
        //this.application.setSpringApplicationContext(this.getApplicationContext());
    }
    
    /**
     * Handle the request. Simply forward it to Wicket.
     * 
     * @see org.springframework.web.servlet.mvc.AbstractController#handleRequestInternal(javax.servlet.http.HttpServletRequest,
     *      javax.servlet.http.HttpServletResponse)
     */
    protected ModelAndView handleRequestInternal(HttpServletRequest servletRequest,
            HttpServletResponse servletResponse) throws Exception
    {
        if (application != null)
        {
             application.getWicketServlet().doGet(servletRequest, servletResponse);
        }
        else
        {
            log.error("Wickets application object is not available. Probably the bean named 'wicketApplication' was not found in Spring's web application context: <servlet-name>-servlet.xml");
        }
        
        return null;
    }
    
    public final class WicketSpringServlet extends WicketServlet
    {
        public WicketSpringServlet(final WebApplication application)
        {
            this.webApplication = application;
        }
        
        public void init()
        {
            ; // replace super implementation with nothing. Apllication class
              // will be defined through Spring xml. 
        }
    }
}
 N@@o newline at end of file
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

QDox parse error in file://<WORKSPACE>/data/code-rep-dataset/Dataset5/Tasks/17059.java