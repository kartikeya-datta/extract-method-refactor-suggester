error id: file://<WORKSPACE>/data/code-rep-dataset/Dataset2/Tasks/8327.java
file://<WORKSPACE>/data/code-rep-dataset/Dataset2/Tasks/8327.java
### com.thoughtworks.qdox.parser.ParseException: syntax error @[1,1]

error in qdox parser
file content:
```java
offset: 1
uri: file://<WORKSPACE>/data/code-rep-dataset/Dataset2/Tasks/8327.java
text:
```scala
o@@utput.write(Long.toString(event.getTimeStamp() - LoggingEvent.getStartTime()));

/*
 * Copyright 1999,2004 The Apache Software Foundation.
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

package org.apache.log4j;

import java.io.IOException;
import java.io.Writer;

import org.apache.log4j.helpers.Transform;
import org.apache.log4j.spi.LocationInfo;
import org.apache.log4j.spi.LoggingEvent;


/**
   This layout outputs events in a HTML table.

   @author Ceki G&uuml;lc&uuml;
 */
public class HTMLLayout extends Layout {
  static final String TRACE_PREFIX = "<br>&nbsp;&nbsp;&nbsp;&nbsp;";

  /**
     A string constant used in naming the option for setting the the
     location information flag.  Current value of this string
     constant is <b>LocationInfo</b>.

     <p>Note that all option keys are case sensitive.

     @deprecated Options are now handled using the JavaBeans paradigm.
     This constant is not longer needed and will be removed in the
     <em>near</em> term.

  */
  public static final String LOCATION_INFO_OPTION = "LocationInfo";

  /**
     A string constant used in naming the option for setting the the
     HTML document title.  Current value of this string
     constant is <b>Title</b>.
  */
  public static final String TITLE_OPTION = "Title";
  protected static final int BUF_SIZE = 256;
  protected static final int MAX_CAPACITY = 1024;

  // output buffer appended to when format() is invoked
  private StringBuffer sbuf = new StringBuffer(BUF_SIZE);

  // Print no location info by default
  boolean locationInfo = false;
  String title = "Log4J Log Messages";

  /**
     The <b>LocationInfo</b> option takes a boolean value. By
     default, it is set to false which means there will be no location
     information output by this layout. If the the option is set to
     true, then the file name and line number of the statement
     at the origin of the log statement will be output.

     <p>If you are embedding this layout within an {@link
     org.apache.log4j.net.SMTPAppender} then make sure to set the
     <b>LocationInfo</b> option of that appender as well.
   */
  public void setLocationInfo(boolean flag) {
    locationInfo = flag;
  }

  /**
     Returns the current value of the <b>LocationInfo</b> option.
   */
  public boolean getLocationInfo() {
    return locationInfo;
  }

  /**
    The <b>Title</b> option takes a String value. This option sets the
    document title of the generated HTML document.

    <p>Defaults to 'Log4J Log Messages'.
  */
  public void setTitle(String title) {
    this.title = title;
  }

  /**
     Returns the current value of the <b>Title</b> option.
  */
  public String getTitle() {
    return title;
  }

  /**
      Returns the content type output by this layout, i.e "text/html".
   */
  public String getContentType() {
    return "text/html";
  }

  /**
     No options to activate.
  */
  public void activateOptions() {
  }

  public void format(java.io.Writer output, LoggingEvent event) throws java.io.IOException {
 
    output.write(Layout.LINE_SEP + "<tr>" + Layout.LINE_SEP);

    output.write("<td>");
    output.write(Long.toString(event.timeStamp - LoggingEvent.getStartTime()));
    output.write("</td>" + Layout.LINE_SEP);

    output.write("<td title=\"" + event.getThreadName() + " thread\">");
    Transform.escapeTags(event.getThreadName(), output);
    output.write("</td>" + Layout.LINE_SEP);

    output.write("<td title=\"Level\">");

    if (event.getLevel().equals(Level.DEBUG)) {
      output.write("<font color=\"#339933\">");
      output.write(event.getLevel().toString());
      output.write("</font>");
    } else if (event.getLevel().isGreaterOrEqual(Level.WARN)) {
      output.write("<font color=\"#993300\"><strong>");
      output.write(event.getLevel().toString());
      output.write("</strong></font>");
    } else {
      output.write(event.getLevel().toString());
    }

    output.write("</td>" + Layout.LINE_SEP);

    output.write("<td title=\"" + event.getLoggerName() + " category\">");
    output.write(event.getLoggerName());
    output.write("</td>" + Layout.LINE_SEP);

    if (locationInfo) {
      LocationInfo locInfo = event.getLocationInformation();
      output.write("<td>");
      Transform.escapeTags(locInfo.getFileName(), output);
      output.write(':');
      output.write(locInfo.getLineNumber());
      output.write("</td>" + Layout.LINE_SEP);
    }

    output.write("<td title=\"Message\">");
    Transform.escapeTags(event.getRenderedMessage(), output);
    output.write("</td>" + Layout.LINE_SEP);
    output.write("</tr>" + Layout.LINE_SEP);

    if (event.getNDC() != null) {
      output.write(
        "<tr><td bgcolor=\"#EEEEEE\" style=\"font-size : xx-small;\" colspan=\"6\" title=\"Nested Diagnostic Context\">");
      Transform.escapeTags(event.getNDC(), output);
      output.write("</td></tr>" + Layout.LINE_SEP);
    }

    String[] s = event.getThrowableStrRep();

    if (s != null) {
      output.write(
        "<tr><td bgcolor=\"#993300\" style=\"color:White; font-size : xx-small;\" colspan=\"6\">");
      appendThrowableAsHTML(s, output);
      output.write("</td></tr>" + Layout.LINE_SEP);
    }
  }

  void appendThrowableAsHTML(String[] s, Writer output) throws IOException {
    if (s != null) {
      int len = s.length;

      if (len == 0) {
        return;
      }

      Transform.escapeTags(s[0], output);
      output.write(Layout.LINE_SEP);

      for (int i = 1; i < len; i++) {
        output.write(TRACE_PREFIX);
        Transform.escapeTags(s[i], output);
        output.write(Layout.LINE_SEP);
      }
    }
  }

  /**
     Returns appropriate HTML headers.
  */
  public String getHeader() {
    StringBuffer sbuf = new StringBuffer();
    sbuf.append(
      "<!DOCTYPE HTML PUBLIC \"-//W3C//DTD HTML 4.01 Transitional//EN\" \"http://www.w3.org/TR/html4/loose.dtd\">"
      + Layout.LINE_SEP);
    sbuf.append("<html>" + Layout.LINE_SEP);
    sbuf.append("<head>" + Layout.LINE_SEP);
    sbuf.append("<title>" + title + "</title>" + Layout.LINE_SEP);
    sbuf.append("<style type=\"text/css\">" + Layout.LINE_SEP);
    sbuf.append("<!--" + Layout.LINE_SEP);
    sbuf.append(
      "body, table {font-family: arial,sans-serif; font-size: x-small;}"
      + Layout.LINE_SEP);
    sbuf.append(
      "th {background: #336699; color: #FFFFFF; text-align: left;}"
      + Layout.LINE_SEP);
    sbuf.append("-->" + Layout.LINE_SEP);
    sbuf.append("</style>" + Layout.LINE_SEP);
    sbuf.append("</head>" + Layout.LINE_SEP);
    sbuf.append(
      "<body bgcolor=\"#FFFFFF\" topmargin=\"6\" leftmargin=\"6\">"
      + Layout.LINE_SEP);
    sbuf.append("<hr size=\"1\" noshade>" + Layout.LINE_SEP);
    sbuf.append(
      "Log session start time " + new java.util.Date() + "<br>"
      + Layout.LINE_SEP);
    sbuf.append("<br>" + Layout.LINE_SEP);
    sbuf.append(
      "<table cellspacing=\"0\" cellpadding=\"4\" border=\"1\" bordercolor=\"#224466\" width=\"100%\">"
      + Layout.LINE_SEP);
    sbuf.append("<tr>" + Layout.LINE_SEP);
    sbuf.append("<th>Time</th>" + Layout.LINE_SEP);
    sbuf.append("<th>Thread</th>" + Layout.LINE_SEP);
    sbuf.append("<th>Level</th>" + Layout.LINE_SEP);
    sbuf.append("<th>Category</th>" + Layout.LINE_SEP);

    if (locationInfo) {
      sbuf.append("<th>File:Line</th>" + Layout.LINE_SEP);
    }

    sbuf.append("<th>Message</th>" + Layout.LINE_SEP);
    sbuf.append("</tr>" + Layout.LINE_SEP);

    return sbuf.toString();
  }

  /**
     Returns the appropriate HTML footers.
  */
  public String getFooter() {
    StringBuffer sbuf = new StringBuffer();
    sbuf.append("</table>" + Layout.LINE_SEP);
	sbuf.append("<br>" + Layout.LINE_SEP);
	sbuf.append("</body></html>");

    return sbuf.toString();
  }

  /**
     The HTML layout handles the throwable contained in logging
     events. Hence, this method return <code>false</code>.  */
  public boolean ignoresThrowable() {
    return false;
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
	java.base/java.util.concurrent.ForkJoinPool.helpJoin(ForkJoinPool.java:1883)
	java.base/java.util.concurrent.ForkJoinTask.awaitDone(ForkJoinTask.java:440)
	java.base/java.util.concurrent.ForkJoinTask.join(ForkJoinTask.java:670)
	scala.collection.parallel.ForkJoinTasks$FJTWrappedTask.sync(Tasks.scala:243)
	scala.collection.parallel.ForkJoinTasks$FJTWrappedTask.sync$(Tasks.scala:243)
	scala.collection.parallel.AdaptiveWorkStealingForkJoinTasks$AWSFJTWrappedTask.sync(Tasks.scala:304)
	scala.collection.parallel.AdaptiveWorkStealingTasks$AWSTWrappedTask.internal(Tasks.scala:173)
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

QDox parse error in file://<WORKSPACE>/data/code-rep-dataset/Dataset2/Tasks/8327.java