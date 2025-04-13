error id: file://<WORKSPACE>/data/code-rep-dataset/Dataset2/Tasks/8115.java
file://<WORKSPACE>/data/code-rep-dataset/Dataset2/Tasks/8115.java
### com.thoughtworks.qdox.parser.ParseException: syntax error @[1,1]

error in qdox parser
file content:
```java
offset: 1
uri: file://<WORKSPACE>/data/code-rep-dataset/Dataset2/Tasks/8115.java
text:
```scala
s@@buf.append(event.getRenderedMessage());

/*
 * Copyright (C) The Apache Software Foundation. All rights reserved.
 *
 * This software is published under the terms of the Apache Software
 * License version 1.1, a copy of which has been included with this
 * distribution in the LICENSE.APL file.  */

package org.apache.log4j;

import org.apache.log4j.spi.LoggingEvent;
import org.apache.log4j.spi.LocationInfo;
import org.apache.log4j.helpers.OptionConverter;
import java.io.StringWriter;
import java.io.PrintWriter;
import java.io.Writer;


public class HTMLLayout extends Layout {

  protected final int BUF_SIZE = 256;
  protected final int MAX_CAPACITY = 1024;

  // output buffer appended to when format() is invoked
  private StringBuffer sbuf = new StringBuffer(BUF_SIZE);

  /**
     A string constant used in naming the option for setting the the
     location information flag.  Current value of this string
     constant is <b>LocationInfo</b>.  

     <p>Note that all option keys are case sensitive.
  */
  public static final String LOCATION_INFO_OPTION = "LocationInfo";

  // Print no location info by default
  boolean locationInfo = false;

  public
  void activateOptions() {
  }

  public 
  String format(LoggingEvent event) {
    
    if(sbuf.capacity() > MAX_CAPACITY) {
      sbuf = new StringBuffer(BUF_SIZE);
    } else {
      sbuf.setLength(0);
    }
    
    sbuf.append("\r\n\r\n<tr>");
 
    sbuf.append("<td>");
    sbuf.append(event.timeStamp - event.getStartTime());
    sbuf.append("</td>\r\n");

    sbuf.append("<td>");
    sbuf.append(event.getThreadName());
    sbuf.append("</td>\r\n");


    sbuf.append("<td>");
    if(event.priority.isGreaterOrEqual(Priority.WARN)) {
      sbuf.append("<font color=\"#FF0000\">");
      sbuf.append(event.priority);      
      sbuf.append("</font>");
    } else {
      sbuf.append(event.priority);      
    }
    sbuf.append("</td>\r\n");

    sbuf.append("<td>");
    sbuf.append(event.categoryName);
    sbuf.append("</td>\r\n");

    sbuf.append("<td>");
    sbuf.append(event.getNDC());
    sbuf.append("</td>\r\n");

    if(locationInfo) {
      LocationInfo locInfo = event.getLocationInformation();
      sbuf.append("<td>");
      sbuf.append(locInfo.getFileName());
      sbuf.append(':');
      sbuf.append(locInfo.getLineNumber());
      sbuf.append("</td>\r\n");
    }


    sbuf.append("<td>");
    sbuf.append(event.message);
    sbuf.append("</td>\r\n");


    sbuf.append("</tr>");

    Throwable t = event.throwable; // JVM is a stack machine
    if(t != null) {
      sbuf.append("\r\n<tr><td colspan=\"7\">");
      sbuf.append(getThrowableAsHTML(t));
      sbuf.append("</td></tr>");
    }


    return sbuf.toString();
  }


 /**
     Returns the content type output by this layout, i.e "text/html".
  */
  public
  String getContentType() {
    return "text/html";
  }

  

  /**
     Returns appropriate HTML headers.
  */
  public
  String getHeader() {
    StringBuffer sbuf = new StringBuffer();
    sbuf.append("<html><body>\r\n");
    sbuf.append("<table border=\"1\" cellpadding=\"2\">\r\n<tr>\r\n");
    sbuf.append("<th>Time</th><th>Thread</th><th>Priority</th><th>Category</th>");
    sbuf.append("<th>NDC</th>");
    if(locationInfo) {
      sbuf.append("<th>File:Line</th>");
    }
    sbuf.append("<th>Message</th></tr>");
    return sbuf.toString();
  }

  /**
     Returns the appropriate HTML footers.
  */
  public
  String getFooter() {
    return "</table></body></html>";
  }
  

  public
  String[] getOptionStrings() {
    return new String[] {LOCATION_INFO_OPTION};
  }

  String getThrowableAsHTML(Throwable throwable) {
    if(throwable == null) 
      return null;
 
    StringWriter sw = new StringWriter();
    HTMLPrintWriter hpw = new HTMLPrintWriter(sw);

    throwable.printStackTrace(hpw);
    return sw.toString();
  }


  public
  boolean ignoresThrowable() {
    return false;
  }

  /**
     Set HTMLLayout specific options.

     <p>The <b>LocationInfo</b> option takes a boolean value. By
     default, it is set to false which means there will be no location
     information output by this layoout. If the the option is set to
     true, then the file name and line number of the statement
     at the origin of the log statement will be output. 

     <p>If you are embedding this layout within an {@link
     org.apache.log4j.net.SMTPAppender} then make sure to set the
     <b>LocationInfo</b> option of that appender as well.
     
   */
  public
  void setOption(String key, String value) {
    if(value == null) return;

    if (key.equals(LOCATION_INFO_OPTION)) {
      locationInfo = OptionConverter.toBoolean(value, locationInfo);
    }
  }


  /**
     Format exceptions in HTML aware way.
   */
  static class HTMLPrintWriter extends PrintWriter {
    
    static String TRACE_PREFIX = "<br>&nbsp;&nbsp;&nbsp;&nbsp;";

    public
    HTMLPrintWriter(Writer writer) {
      super(writer);
    }

    /**
       Some JDKs use prinln(char[])
     */
    public
    void println(char[] c) {
      write(TRACE_PREFIX);
      this.write(c);
    }

    /**
       Yet others use println(String). Go figure.
    */    
    public
    void println(String s) {
      write(TRACE_PREFIX);
      this.write(s);
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

QDox parse error in file://<WORKSPACE>/data/code-rep-dataset/Dataset2/Tasks/8115.java