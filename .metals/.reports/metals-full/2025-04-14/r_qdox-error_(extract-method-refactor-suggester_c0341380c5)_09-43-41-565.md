error id: file://<WORKSPACE>/data/code-rep-dataset/Dataset2/Tasks/586.java
file://<WORKSPACE>/data/code-rep-dataset/Dataset2/Tasks/586.java
### com.thoughtworks.qdox.parser.ParseException: syntax error @[1,10]

error in qdox parser
file content:
```java
offset: 10
uri: file://<WORKSPACE>/data/code-rep-dataset/Dataset2/Tasks/586.java
text:
```scala
@version $@@Revision: 1.2 $

package org.jboss.web.tomcat.tc4;

import java.io.IOException;
import java.net.URL;
import java.net.JarURLConnection;
import java.util.HashSet;

import org.apache.catalina.Context;
import org.apache.catalina.Loader;
import org.apache.catalina.loader.WebappLoader;
import org.apache.commons.digester.Rule;

/** This action sets the Context parent class loader to the loader passed into
 the ctor and adds the urls for the jars that contain the javax.servlet.Servlet
 class, the org/apache/jasper/JspC.class, and the
 org/apache/jasper/runtime/HttpJspBase.class.

@author Scott.Stark@jboss.org
@version $Revision: 1.1 $
 */
public class SetParentClassLoaderRule extends Rule
{
   ClassLoader loader;

   public SetParentClassLoaderRule(ClassLoader loader)
   {
      this.loader = loader;
   }

   /** The stack must look like Embedded/Service/Engine/Host/Context
    */
   public void end() throws Exception
   {
      Context context = (Context) digester.peek();
      context.setParentClassLoader(loader);

      // Locate the Servlet and JSP jars
      HashSet cp = new HashSet();
      String path0 = getResource("javax/servlet/Servlet.class", loader);
      if( path0 != null )
         cp.add(path0);
      String path1 = getResource("org/apache/jasper/JspC.class", loader);
      if( path1 != null )
         cp.add(path1);
      String path2 = getResource("org/apache/jasper/runtime/HttpJspBase.class", loader);
      if( path2 != null )
         cp.add(path2);

      // Add the Servlet/JSP jars to the web context classpath
      Loader ctxLoader = context.getLoader();
      if( ctxLoader == null )
      {
         ctxLoader = new WebappLoader(loader);
         context.setLoader(ctxLoader);
      }
      String[] jars = new String[cp.size()];
      cp.toArray(jars);
      for(int j = 0; j < jars.length; j ++)
         ctxLoader.addRepository(jars[j]);
   }

   private String getResource(String name, ClassLoader loader) throws IOException
   {
      URL res = loader.getResource(name);
      if( res == null )
         return null;

      if( res.getProtocol().equals("jar") )
      {
         JarURLConnection jarConn = (JarURLConnection) res.openConnection();
         res = jarConn.getJarFileURL();
      }
      return res.toExternalForm();
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

QDox parse error in file://<WORKSPACE>/data/code-rep-dataset/Dataset2/Tasks/586.java