error id: file://<WORKSPACE>/data/code-rep-dataset/Dataset2/Tasks/7329.java
file://<WORKSPACE>/data/code-rep-dataset/Dataset2/Tasks/7329.java
### com.thoughtworks.qdox.parser.ParseException: syntax error @[1,1]

error in qdox parser
file content:
```java
offset: 1
uri: file://<WORKSPACE>/data/code-rep-dataset/Dataset2/Tasks/7329.java
text:
```scala
<@@p><li>Search for <code>resource</code> using the same class

/*
 * Copyright (C) The Apache Software Foundation. All rights reserved.
 *
 * This software is published under the terms of the Apache Software
 * License version 1.1, a copy of which has been included with this
 * distribution in the LICENSE.APL file.  */

package org.apache.log4j.helpers;

import java.net.URL;
//import java.awt.Image;
//import java.awt.Toolkit;

/**
   Load resources (or images) from various sources.
 
  @author Sven Reimers
  @author Ceki G&uuml;lc&uuml;
 */

public class Loader  { 

  static String TSTR = "Caught Exception while in Loader.getResource. This may be innocuous.";
  
  /**
     This method will search for <code>resource</code> in different
     places. The rearch order is as follows:

     <ol>

     <p><li>Search for <code>resource</code> with same the class
     loader that loaded <code>clazz</code>.

     <p><li>Try one last time with
     <code>ClassLoader.getSystemResource(resource)</code>, that is is
     using the system class loader in JDK 1.2 and virtual machine's
     built-in class loader in JDK 1.1.

     </ol>
     
  */
  static 
  public
  URL getResource(String resource, Class clazz) {
    
    URL url = null;


    // Is it under CLAZZ/resource somewhere in the classpath?  CLAZZ
    // stands for fully qualified name of "clazz" where dots have been
    // changed to directory separators
    ///LogLog.debug("Trying to find ["+resource+"] using clazz.getResource().");
    ///
    ///try {
    ///	 url = clazz.getResource(resource);
    ///	 if(url != null) 
    ///	   return url;
    ///} catch (Throwable t) {
    ///	 LogLog.warn(TSTR,t);
    ///}
    ///	 
    ///// attempt to get the resource under CLAZZ/resource from the
    ///// system class path. The system class loader should not throw
    ///// InvalidJarIndexExceptions
    ///String fullyQualified = resolveName(resource, clazz);
    ///LogLog.debug("Trying to find ["+fullyQualified+
    ///		    "] using ClassLoader.getSystemResource().");
    ///url = ClassLoader.getSystemResource(fullyQualified);
    ///if(url != null) 
    ///	 return url;
    
    
    // Let the class loader of clazz and parents (by the delagation
    // property) seearch for resource
    ClassLoader loader = clazz.getClassLoader();
    if(loader != null) {
      try {
	LogLog.debug("Trying to find ["+resource+"] using "+loader
		     +" class loader.");
	url = loader.getResource(resource); 
	if(url != null) 
	  return url;
      } catch(Throwable t) {
	LogLog.warn(TSTR, t);
      }
    }
    
    
    // Attempt to get the resource from the class path. It may be the
    // case that clazz was loaded by the Extentsion class loader which
    // the parent of the system class loader. Hence the code below.
    LogLog.debug("Trying to find ["+resource+"] using ClassLoader.getSystemResource().");
    url = ClassLoader.getSystemResource(resource);
    return url;
  }

  /**
     Append the fully qualified name of a class before resource
     (replace . with /). 
  */
  static
  String resolveName(String resource, Class clazz) {
    String fqcn = clazz.getName();
    int index = fqcn.lastIndexOf('.');
    if (index != -1) {
      fqcn = fqcn.substring(0, index).replace('.', '/');
      resource = fqcn+"/"+resource;
    }
    return resource;
  }


  //public static Image getGIF_Image ( String path ) {
  //  Image img = null;
  //  try {
  //	URL url = ClassLoader.getSystemResource(path);
  //	System.out.println(url);
  //	img = (Image) (Toolkit.getDefaultToolkit()).getImage(url);
  //  }
  //  catch (Exception e) {
  //	System.out.println("Exception occured: " + e.getMessage() + 
  //			   " - " + e );
  //	      
  //  }
  //  return (img);
  //}
  //
  //public static Image getGIF_Image ( URL url ) {
  //  Image img = null;
  //  try {
  //	System.out.println(url);
  //	img = (Image) (Toolkit.getDefaultToolkit()).getImage(url);
  //  } catch (Exception e) {
  //	System.out.println("Exception occured: " + e.getMessage() + 
  //			   " - " + e );
  //	      
  //  }
  //  return (img);
  //}
  //
  //public static URL getHTML_Page ( String path ) {
  //  URL url = null;
  //  return (url = ClassLoader.getSystemResource(path));
  //  }    
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

QDox parse error in file://<WORKSPACE>/data/code-rep-dataset/Dataset2/Tasks/7329.java