error id: file://<WORKSPACE>/data/code-rep-dataset/Dataset2/Tasks/2021.java
file://<WORKSPACE>/data/code-rep-dataset/Dataset2/Tasks/2021.java
### com.thoughtworks.qdox.parser.ParseException: syntax error @[1,1]

error in qdox parser
file content:
```java
offset: 1
uri: file://<WORKSPACE>/data/code-rep-dataset/Dataset2/Tasks/2021.java
text:
```scala
l@@i = formatLocationInfo(event);

/*
 * ============================================================================
 *                   The Apache Software License, Version 1.1
 * ============================================================================
 *
 *    Copyright (C) 1999 The Apache Software Foundation. All rights reserved.
 *
 * Redistribution and use in source and binary forms, with or without modifica-
 * tion, are permitted provided that the following conditions are met:
 *
 * 1. Redistributions of  source code must  retain the above copyright  notice,
 *    this list of conditions and the following disclaimer.
 *
 * 2. Redistributions in binary form must reproduce the above copyright notice,
 *    this list of conditions and the following disclaimer in the documentation
 *    and/or other materials provided with the distribution.
 *
 * 3. The end-user documentation included with the redistribution, if any, must
 *    include  the following  acknowledgment:  "This product includes  software
 *    developed  by the  Apache Software Foundation  (http://www.apache.org/)."
 *    Alternately, this  acknowledgment may  appear in the software itself,  if
 *    and wherever such third-party acknowledgments normally appear.
 *
 * 4. The names "log4j" and  "Apache Software Foundation"  must not be used to
 *    endorse  or promote  products derived  from this  software without  prior
 *    written permission. For written permission, please contact
 *    apache@apache.org.
 *
 * 5. Products  derived from this software may not  be called "Apache", nor may
 *    "Apache" appear  in their name,  without prior written permission  of the
 *    Apache Software Foundation.
 *
 * THIS SOFTWARE IS PROVIDED ``AS IS'' AND ANY EXPRESSED OR IMPLIED WARRANTIES,
 * INCLUDING, BUT NOT LIMITED TO, THE IMPLIED WARRANTIES OF MERCHANTABILITY AND
 * FITNESS  FOR A PARTICULAR  PURPOSE ARE  DISCLAIMED.  IN NO  EVENT SHALL  THE
 * APACHE SOFTWARE  FOUNDATION  OR ITS CONTRIBUTORS  BE LIABLE FOR  ANY DIRECT,
 * INDIRECT, INCIDENTAL, SPECIAL,  EXEMPLARY, OR CONSEQUENTIAL  DAMAGES (INCLU-
 * DING, BUT NOT LIMITED TO, PROCUREMENT  OF SUBSTITUTE GOODS OR SERVICES; LOSS
 * OF USE, DATA, OR  PROFITS; OR BUSINESS  INTERRUPTION)  HOWEVER CAUSED AND ON
 * ANY  THEORY OF LIABILITY,  WHETHER  IN CONTRACT,  STRICT LIABILITY,  OR TORT
 * (INCLUDING  NEGLIGENCE OR  OTHERWISE) ARISING IN  ANY WAY OUT OF THE  USE OF
 * THIS SOFTWARE, EVEN IF ADVISED OF THE POSSIBILITY OF SUCH DAMAGE.
 *
 * This software  consists of voluntary contributions made  by many individuals
 * on  behalf of the Apache Software  Foundation.  For more  information on the
 * Apache Software Foundation, please see <http://www.apache.org/>.
 *
 */

package org.apache.log4j.chainsaw.layout;

import org.apache.log4j.Layout;
import org.apache.log4j.Logger;
import org.apache.log4j.PatternLayout;
import org.apache.log4j.spi.LocationInfo;
import org.apache.log4j.spi.LoggingEvent;

import java.io.IOException;
import java.io.Writer;

import java.util.Hashtable;
import java.util.Iterator;
import java.util.Set;


/**
 * This layout is used for formatting HTML text for use inside
 * the Chainsaw Event Detail Panel, and the tooltip used
 * when mouse-over on a particular log event row.
 *
 * It relies an an internal PatternLayout to accomplish this, but ensures HTML characters
 * from any LoggingEvent are escaped first.
 *
 * @author Paul Smith <psmith@apache.org>
 */
public class EventDetailLayout extends Layout {
  private PatternLayout patternLayout = new PatternLayout();

  public EventDetailLayout() {
  }

  public void setConversionPattern(String conversionPattern) {
    patternLayout.setConversionPattern(conversionPattern);
    patternLayout.activateOptions();
  }

  public String getConversionPattern() {
    return patternLayout.getConversionPattern();
  }

  /* (non-Javadoc)
   * @see org.apache.log4j.Layout#getFooter()
   */
  public String getFooter() {
    return "";
  }

  /* (non-Javadoc)
   * @see org.apache.log4j.Layout#getHeader()
   */
  public String getHeader() {
    return "";
  }

  //  /* (non-Javadoc)
  //   * @see org.apache.log4j.Layout#format(java.io.Writer, org.apache.log4j.spi.LoggingEvent)
  //   */
  //  public void format(Writer output, LoggingEvent event)
  //    throws IOException {
  //    boolean pastFirst = false;
  //    output.write("<html><body><table cellspacing=0 cellpadding=0>");
  //
  //    List columnNames = ChainsawColumns.getColumnsNames();
  //
  //    Vector v = ChainsawAppenderHandler.convert(event);
  //
  //    /**
  //     * we need to add the ID property from the event
  //     */
  //    v.add(event.getProperty(ChainsawConstants.LOG4J_ID_KEY));
  //    
  //    //             ListIterator iter = displayFilter.getDetailColumns().listIterator();
  //    Iterator iter = columnNames.iterator();
  //    String column = null;
  //    int index = -1;
  //
  //    while (iter.hasNext()) {
  //      column = (String) iter.next();
  //      index = columnNames.indexOf(column);
  //
  //      if (index > -1) {
  //        if (pastFirst) {
  //          output.write("</td></tr>");
  //        }
  //
  //        output.write("<tr><td valign=\"top\"><b>");
  //        output.write(column);
  //        output.write(": </b></td><td>");
  //
  //
  //        if (index<v.size()) {
  //			Object o = v.get(index);
  //
  //			if (o != null) {
  //				output.write(escape(o.toString()));
  //			} else {
  //				output.write("{null}");
  //			}
  //			
  //		}else {
  ////            output.write("Invalid column " + column + " (index=" + index + ")");      
  //        }
  //
  //        pastFirst = true;
  //      }
  //    }
  //
  //    output.write("</table></body></html>");
  //  }

  /**
    * Escape &lt;, &gt; &amp; and &quot; as their entities. It is very
    * dumb about &amp; handling.
    * @param aStr the String to escape.
    * @return the escaped String
    */
  private static String escape(String string) {
    if (string == null) {
      return "";
    }

    final StringBuffer buf = new StringBuffer();

    for (int i = 0; i < string.length(); i++) {
      char c = string.charAt(i);

      switch (c) {
      case '<':
        buf.append("&lt;");

        break;

      case '>':
        buf.append("&gt;");

        break;

      case '\"':
        buf.append("&quot;");

        break;

      case '&':
        buf.append("&amp;");

        break;

      default:
        buf.append(c);

        break;
      }
    }

    return buf.toString();
  }

  /**
   * Takes a source event and copies it into a new LoggingEvent object
   * and ensuring all the internal elements of the event are HTML safe
   * @param event
   * @return new LoggingEvent
   */
  private static LoggingEvent copyForHTML(LoggingEvent event) {
    String fqnCategory = escape(event.fqnOfCategoryClass);
    Logger logger = Logger.getLogger(event.getLoggerName());
    String threadName = event.getThreadName();
    Object msg = event.getMessage();
    String ndc = event.getNDC();
    Hashtable mdc = formatMDC(event);
    String[] throwableStringRep = event.getThrowableStrRep();
    LocationInfo li = null;
    if (event.locationInformationExists()) {
        formatLocationInfo(event);
    }
    Hashtable properties = formatProperties(event);
    LoggingEvent copy =
      new LoggingEvent(
        fqnCategory, logger, event.timeStamp, event.getLevel(), threadName, msg,
        ndc, mdc, throwableStringRep, li, properties);

    return copy;
  }

  /**
  * @param event
  * @return
  */
  private static Hashtable formatMDC(LoggingEvent event) {
    Set keySet = event.getMDCKeySet();
    Hashtable hashTable = new Hashtable();

    for (Iterator iter = keySet.iterator(); iter.hasNext();) {
      Object key = (Object) iter.next();
      Object value = event.getMDC(key.toString());
      hashTable.put(escape(key.toString()), escape(value.toString()));
    }

    return hashTable;
  }

  /**
   * @param event
   * @return
   */
  private static LocationInfo formatLocationInfo(LoggingEvent event) {
    LocationInfo info = event.getLocationInformation();
    LocationInfo newInfo =
      new LocationInfo(
        escape(info.getFileName()), escape(info.getClassName()),
        escape(info.getMethodName()), escape(info.getLineNumber()));

    return newInfo;
  }

  /**
   * @param event
   * @return
   */
  private static Hashtable formatProperties(LoggingEvent event) {
    Set keySet = event.getPropertyKeySet();
    Hashtable hashTable = new Hashtable();

    for (Iterator iter = keySet.iterator(); iter.hasNext();) {
      Object key = (Object) iter.next();
      Object value = event.getProperty(key.toString());
      hashTable.put(escape(key.toString()), escape(value.toString()));
    }

    return hashTable;
  }

  /* (non-Javadoc)
     * @see org.apache.log4j.Layout#ignoresThrowable()
     */
  public boolean ignoresThrowable() {
    return false;
  }

  /* (non-Javadoc)
   * @see org.apache.log4j.spi.OptionHandler#activateOptions()
   */
  public void activateOptions() {
  }

  /* (non-Javadoc)
   * @see org.apache.log4j.Layout#format(java.io.Writer, org.apache.log4j.spi.LoggingEvent)
   */
  public void format(Writer output, LoggingEvent event)
    throws IOException {
    patternLayout.format(output, copyForHTML(event));
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

QDox parse error in file://<WORKSPACE>/data/code-rep-dataset/Dataset2/Tasks/2021.java