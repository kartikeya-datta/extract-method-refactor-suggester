error id: file://<WORKSPACE>/data/code-rep-dataset/Dataset2/Tasks/6828.java
file://<WORKSPACE>/data/code-rep-dataset/Dataset2/Tasks/6828.java
### com.thoughtworks.qdox.parser.ParseException: syntax error @[1,1]

error in qdox parser
file content:
```java
offset: 1
uri: file://<WORKSPACE>/data/code-rep-dataset/Dataset2/Tasks/6828.java
text:
```scala
b@@uf.append(event.getRenderedMessage());

/*
 * Copyright (C) The Apache Software Foundation. All rights reserved.
 *
 * This software is published under the terms of the Apache Software
 * License version 1.1, a copy of which has been included with this
 * distribution in the LICENSE.APL file.  */

package org.apache.log4j.xml;

import org.apache.log4j.Layout;
import org.apache.log4j.spi.LoggingEvent;
import org.apache.log4j.spi.LocationInfo;
import org.apache.log4j.helpers.OptionConverter;
import org.apache.log4j.helpers.DateLayout;

/**
   The output of the XMLLayout consists of a series of log4j:event
   elements as defined in the <a
   href="doc-files/log4j.dtd">log4j.dtd</a>. It does not output a
   complete well-formed XML file. The output is designed to be
   included as an <em>external entity</em> in a separate file to form
   a correct XML file.

   <p>For example, if <code>abc</code> is the name of the file where
   the XMLLayout ouput goes, then a well-formed XML file would be:

   <pre>
   &lt;?xml version="1.0" ?&gt;

   &lt;!DOCTYPE log4j:eventSet SYSTEM "log4j.dtd" [&lt;!ENTITY data SYSTEM "abc"&gt;]&gt;

   &lt;log4j:eventSet xmlns:log4j="http://log4j.org"&gt;
       &nbsp;&nbsp;&data;
   &lt;/log4j:eventSet&gt;
   </pre>

   <p>This approach enforces the independence of the XMLLayout and the
   appender where it is embedded.

   @author Ceki  G&uuml;lc&uuml;
   @since 0.9.0 */
public class XMLLayout extends Layout {

  private  final int DEFAULT_SIZE = 256;
  private final int UPPER_LIMIT = 2048;

  private StringBuffer buf = new StringBuffer(DEFAULT_SIZE);
  private boolean locationInfo = false;

  /**
     This is a string constant to name the option for setting the
     location information flag. Current value of this string constant
     is <b>LocationInfo</b>. 

     <p>See the {@link #setOption(java.lang.String, java.lang.String)}
     method for the meaning of this option.  

     <p>Note all option keys are case sensitive.
     
  */
  public static final String LOCATION_INFO_OPTION = "LocationInfo";

  public
  void activateOptions() {
  }



  /**
     Formats a {@link LoggingEvent} in conformance with the log4j.dtd.  */
  public
  String format(LoggingEvent event) {

    // Reset working buffer. If the buffer is too large, then we need a new
    // one in order to avoid the penalty of creating a large array.
    if(buf.capacity() > UPPER_LIMIT) {
      buf = new StringBuffer(DEFAULT_SIZE);
    } else {
      buf.setLength(0);
    }
    
    // We yield to the \r\n heresy.

    buf.append("<log4j:event category=\"");
    buf.append(event.categoryName);
    buf.append("\" timestamp=\"");
    buf.append(event.timeStamp);
    buf.append("\" priority=\"");
    buf.append(event.priority);
    buf.append("\" thread=\"");
    buf.append(event.getThreadName());
    buf.append("\">\r\n");


       buf.append("<log4j:message>");
       buf.append(event.message);
       buf.append("</log4j:message>\r\n");       

       String ndc = event.getNDC();
       if(ndc != null) {
	 buf.append("<log4j:NDC>");
	 buf.append(ndc);
	 buf.append("</log4j:NDC>\r\n");       
       }

       String t = event.getThrowableInformation();
       if(t != null) {
	 buf.append("<log4j:throwable>");
	 buf.append(t);
	 buf.append("</log4j:throwable>\r\n");
       }

       if(locationInfo) { 
	 LocationInfo locationInfo = event.getLocationInformation();	
	 buf.append("<log4j:locationInfo class=\"");
	 buf.append(locationInfo.getClassName());
	 buf.append("\" method=\"");
	 buf.append(locationInfo.getMethodName());
	 buf.append("\" file=\"");
	 buf.append(locationInfo.getFileName());
	 buf.append("\" line=\"");
	 buf.append(locationInfo.getLineNumber());
	 buf.append("\"/>\r\n");
       }

    buf.append("</log4j:event>\r\n\r\n");

    return buf.toString();
  }

  public
  String[] getOptionStrings() {
    return new String[]{LOCATION_INFO_OPTION};
  }


  /**
     The XMLLayout prints and does not ignore exceptions. Hence the
     return value <code>false</code>.
  */
  public
  boolean ignoresThrowable() {
    return false;
  }


  /**

     The XMLLayout specific options are:

     <p>The <b>LocationInfo</b> option takes a boolean value. If true,
     the oupyt will include location information. By default no
     location information is sent to the server.
  
  */
  public
  void setOption(String key, String value) {

    if(value == null) return;
    if (key.equals(LOCATION_INFO_OPTION)) {
      locationInfo = OptionConverter.toBoolean(value, locationInfo);    
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

QDox parse error in file://<WORKSPACE>/data/code-rep-dataset/Dataset2/Tasks/6828.java