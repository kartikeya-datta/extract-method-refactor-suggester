error id: file://<WORKSPACE>/data/code-rep-dataset/Dataset5/Tasks/17687.java
file://<WORKSPACE>/data/code-rep-dataset/Dataset5/Tasks/17687.java
### com.thoughtworks.qdox.parser.ParseException: syntax error @[127,80]

error in qdox parser
file content:
```java
offset: 2698
uri: file://<WORKSPACE>/data/code-rep-dataset/Dataset5/Tasks/17687.java
text:
```scala
{ // TODO finalize javadoc

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
package wicket.protocol.http;

import javax.servlet.http.Cookie;

import java.util.Collections;
import java.util.Map;

/**
 * A dummy request.
 * @author Jonathan Locke
 */
public class NullHttpRequest extends HttpRequest
{
    /** singleton instance. */
    private static NullHttpRequest NULL = new NullHttpRequest();

    /**
     * Constructor.
     */
    private NullHttpRequest()
    {
        super(null);
    }

    /**
     * Gets the singleton instance.
     * @return the singleton instance
     */
    public static NullHttpRequest getInstance()
    {
        return NULL;
    }

    /**
     * @see wicket.Request#getParameter(java.lang.String)
     */
    public String getParameter(String key)
    {
        return null;
    }

    /**
     * @see wicket.Request#getParameterMap()
     */
    public Map getParameterMap()
    {
        return Collections.EMPTY_MAP;
    }

    /**
     * @see wicket.Request#getParameters(java.lang.String)
     */
    public String[] getParameters(String key)
    {
        return null;
    }

    /**
     * @see wicket.Request#getURL()
     */
    public String getURL()
    {
        return "[no request url]";
    }

    /**
     * @see wicket.protocol.http.HttpRequest#getContextPath()
     */
    public String getContextPath()
    {
        return "[no context path]";
    }

    /**
     * @see wicket.protocol.http.HttpRequest#getCookies()
     */
    public Cookie[] getCookies()
    {
        return null;
    }

    /**
     * @see wicket.protocol.http.HttpRequest#getPathInfo()
     */
    public String getPathInfo()
    {
        return "[no path info]";
    }

    /**
     * @see wicket.protocol.http.HttpRequest#getServletPath()
     */
    public String getServletPath()
    {
        return "[no servlet path]";
    }

    /**
     * @see java.lang.Object#toString()
     */
    public String toString()
    {
        return "[NullHttpRequest]";
    }
}

///////////////////////////////// End of File /////////////////////////////////@@
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

QDox parse error in file://<WORKSPACE>/data/code-rep-dataset/Dataset5/Tasks/17687.java