error id: file://<WORKSPACE>/data/code-rep-dataset/Dataset2/Tasks/696.java
file://<WORKSPACE>/data/code-rep-dataset/Dataset2/Tasks/696.java
### com.thoughtworks.qdox.parser.ParseException: syntax error @[1,13]

error in qdox parser
file content:
```java
offset: 13
uri: file://<WORKSPACE>/data/code-rep-dataset/Dataset2/Tasks/696.java
text:
```scala
@deprecated U@@se {@link LevelMatchFilter} instead.

/*
 * Copyright (C) The Apache Software Foundation. All rights reserved.
 *
 * This software is published under the terms of the Apache Software License
 * version 1.1, a copy of which has been included  with this distribution in
 * the LICENSE.txt file.
 */

package org.apache.log4j.varia;

import org.apache.log4j.Level;
import org.apache.log4j.spi.Filter;
import org.apache.log4j.spi.LoggingEvent;
import org.apache.log4j.helpers.OptionConverter;

/**
   This is a very simple filter based on level matching.


   <p>The filter admits two options <b>LevelToMatch</b> and
   <b>AcceptOnMatch</b>. If there is an exact match between the value
   of the LevelToMatch option and the level of the {@link
   LoggingEvent}, then the {@link #decide} method returns {@link
   Filter#ACCEPT} in case the <b>AcceptOnMatch</b> option value is set
   to <code>true</code>, if it is <code>false</code> then {@link
   Filter#DENY} is returned. If there is no match, {@link
   Filter#NEUTRAL} is returned.

   <p>See configuration files <a
   href="../xml/doc-files/test11.xml">test11.xml</a> and <a
   href="../xml/doc-files/test12.xml">test12.xml</a> for an example of
   seeting up a <code>LevelMatchFilter</code>.

   @author Ceki G&uuml;lc&uuml;
   
   @deprecated Use {@link PriorityMatchFilter} instead.
   @since 0.9.1 */
public class PriorityMatchFilter extends Filter {
  
  /**
     @deprecated Options are now handled using the JavaBeans paradigm.
     This constant is not longer needed and will be removed in the
     <em>near</em> term.
   */
  public static final String LEVEL_TO_MATCH_OPTION = "PriorityToMatch";

  /**
     @deprecated Options are now handled using the JavaBeans paradigm.
     This constant is not longer needed and will be removed in the
     <em>near</em> term.
   */
  public static final String ACCEPT_ON_MATCH_OPTION = "AcceptOnMatch";
  
  /**
     Do we return ACCEPT when a match occurs. Default is
     <code>true</code>.  */
  boolean acceptOnMatch = true;

  /**
   */
  Level levelToMatch;

  /**
     @deprecated We now use JavaBeans introspection to configure
     components.
  */
  public
  String[] getOptionStrings() {
    return new String[] {LEVEL_TO_MATCH_OPTION, ACCEPT_ON_MATCH_OPTION};
  }


  public
  void setPriorityToMatch(String level) {
    levelToMatch = OptionConverter.toLevel(level, null);
  }
  
  public
  String getPriorityToMatch() {
    return levelToMatch == null ? null : levelToMatch.toString();
  }
  
  public
  void setAcceptOnMatch(boolean acceptOnMatch) {
    this.acceptOnMatch = acceptOnMatch;
  }
  
  public
  boolean getAcceptOnMatch() {
    return acceptOnMatch;
  }
  

  /**
     Return the decision of this filter.

     Returns {@link Filter#NEUTRAL} if the <b>PriorityToMatch</b>
     option is not set.  Otherwise, the returned decision is defined
     according to the following table:

     <p><table border=1>
     <tr><th rowspan="2" BGCOLOR="#AAAAFF">Did a priority match occur?</th>
     	 <th colspan="2" BGCOLOR="#CCCCCC">AcceptOnMatch setting</th>
     
     <tr><td BGCOLOR="#CCCCCC" align="center">TRUE</td>
     	 <td BGCOLOR="#CCCCCC" align="center">FALSE</td>
     
     <tr><td BGCOLOR="#AAAAFF" align="center">TRUE</td>
     	 <td>{@link Filter#ACCEPT}</td><td>{@link Filter#DENY}</td>
     <tr><td BGCOLOR="#AAAAFF" align="center">FALSE</td>
     	 <td>{@link Filter#DENY}</td><td>{@link Filter#ACCEPT}</td>
     
     	 <caption align="bottom">Filter decision in function of whether a match
     	 occured and the AcceptOnMatch settings</caption> 
    </table> */
  public
  int decide(LoggingEvent event) {
    if(this.levelToMatch == null) {
      return Filter.NEUTRAL;
    }
    
    boolean matchOccured = false;
    if(this.levelToMatch == event.level) {
      matchOccured = true;
    }

    if(this.acceptOnMatch ^ matchOccured) {
      return Filter.DENY;
    } else {
      return Filter.ACCEPT;
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

QDox parse error in file://<WORKSPACE>/data/code-rep-dataset/Dataset2/Tasks/696.java