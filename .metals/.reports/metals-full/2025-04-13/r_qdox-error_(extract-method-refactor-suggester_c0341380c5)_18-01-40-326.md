error id: file://<WORKSPACE>/data/code-rep-dataset/Dataset2/Tasks/966.java
file://<WORKSPACE>/data/code-rep-dataset/Dataset2/Tasks/966.java
### com.thoughtworks.qdox.parser.ParseException: syntax error @[1,1]

error in qdox parser
file content:
```java
offset: 1
uri: file://<WORKSPACE>/data/code-rep-dataset/Dataset2/Tasks/966.java
text:
```scala
<@@code>WARN</code>, <code>INFO</code>, <code>DEBUG</code>,

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

// Contributors:  Kitching Simon <Simon.Kitching@orange.ch>
//                Nicholas Wolff
package org.apache.log4j;


/**
   Defines the minimum set of levels recognized by the system, that is
   <code>OFF</code>, <code>FATAL</code>, <code>ERROR</code>,
   <code>WARN</code>, <code>INFO</code, <code>DEBUG</code>,
   <code>TRACE</code>, and <code>ALL</code>.

   <p>The <code>Level</code> class may be subclassed to define a larger
   level set.

   @author Ceki G&uuml;lc&uuml;
   @author Yoav Shapira
 */
public class Level {
  /**
   * OFF level integer value.
   */
  public static final int OFF_INT = Integer.MAX_VALUE;

  /**
   * FATAL level integer value.
   */
  public static final int FATAL_INT = 50000;

  /**
   * ERROR level integer value.
   */
  public static final int ERROR_INT = 40000;

  /**
   * WARN level integer value.
   */
  public static final int WARN_INT = 30000;

  /**
   * INFO level integer value.
   */
  public static final int INFO_INT = 20000;

  /**
   * DEBUG level integer value.
   */
  public static final int DEBUG_INT = 10000;

  /**
   * TRACE level integer value.
   */
  public static final int TRACE_INT = 5000;

  /**
   * ALL level integer value.
   */
  public static final int ALL_INT = Integer.MIN_VALUE;

  /**
   * The <code>OFF</code> has the highest possible rank and is
   * intended to turn off logging.
   */
  public static final Level OFF = new Level(OFF_INT, "OFF", 0);

  /**
   * The <code>FATAL</code> level designates very severe error
   * events that will presumably lead the application to abort.
   */
  public static final Level FATAL = new Level(FATAL_INT, "FATAL", 0);

  /**
   * The <code>ERROR</code> level designates error events that
   * might still allow the application to continue running.
   */
  public static final Level ERROR = new Level(ERROR_INT, "ERROR", 3);

  /**
   * The <code>WARN</code> level designates potentially harmful situations.
   */
  public static final Level WARN = new Level(WARN_INT, "WARN", 4);

  /**
   * The <code>INFO</code> level designates informational messages
   * that highlight the progress of the application at coarse-grained
   * level.
   */
  public static final Level INFO = new Level(INFO_INT, "INFO", 6);

  /**
   * The <code>DEBUG</code> Level designates fine-grained
   * informational events that are most useful to debug an
   * application.
   */
  public static final Level DEBUG = new Level(DEBUG_INT, "DEBUG", 7);

  /**
   * The <code>TRACE</code> Level designates finer-grained
   * informational events than the <code>DEBUG</code level.
   */
  public static final Level TRACE = new Level(TRACE_INT, "TRACE", 7);

  /**
   * The <code>ALL</code> has the lowest possible rank and is intended to
   * turn on all logging.
   */
  public static final Level ALL = new Level(ALL_INT, "ALL", 7);

  /**
   * The integer value of this Level instance.
   */
  int level;

  /**
   * The label of this Level instance.
   */
  String levelStr;

  /**
   * The UNIX SysLog equivalent value of this Level instance.
   */
  int syslogEquivalent;

  /**
   * Instantiate a level object.
   *
   * @param level The integer level value
   * @param levelStr The level name
   * @param syslogEquivalent The UNIX SystLog level equivalent
   */
  protected Level(int level, String levelStr, int syslogEquivalent) {
    this.level = level;
    this.levelStr = levelStr;
    this.syslogEquivalent = syslogEquivalent;
  }

  /**
   * Convert the string passed as argument to a level. If the
   * conversion fails, then this method returns {@link #DEBUG}.
   *
   * @param sArg The level name
   * @return The matching Level object
   */
  public static Level toLevel(String sArg) {
    return toLevel(sArg, Level.DEBUG);
  }

  /**
   * Convert an integer passed as argument to a level. If the
   * conversion fails, then this method returns {@link #DEBUG}.
   *
   * @param val The level integer value
   * @return The matching Level object
   */
  public static Level toLevel(int val) {
    return toLevel(val, Level.DEBUG);
  }

  /**
   * Two Levels (formerly Priorities) are equal if their level
   * integer value fields are equal.  If the argument is not
   * a Level, this method returns False.
   *
   * @param o The other Level
   * @return boolean True if equals
   * @since 1.2
   */
  public boolean equals(Object o) {
    if (o instanceof Level) {
      Level r = (Level) o;

      return (this.level == r.level);
    } else {
      return false;
    }
  }

  /**
   * The hashCode of a Level (i.e. Priority) is its level field.
   *
   * @return The integer level value
   */
  public int hashCode() {
    return level;
  }

  /**
   * Return the syslog equivalent of this priority as an integer.
   *
   * @return The UNIX SysLog equivalent
   */
  public final int getSyslogEquivalent() {
    return syslogEquivalent;
  }

  /**
   *  Returns <code>true</code> if this level has a higher or equal
   *  level than the level passed as argument, <code>false</code>
   *  otherwise.
   *
   *  <p>You should think twice before overriding the default
   *  implementation of <code>isGreaterOrEqual</code> method.</p>
   */
  public boolean isGreaterOrEqual(Level r) {
    return level >= r.level;
  }

  /**
   * Return all possible priorities as an array of Level objects in
   * descending order.
   *
   * @return Level[] All the Levels
   * @deprecated This method will be removed with no replacement.
   */
  public static Level[] getAllPossiblePriorities() {
    return new Level[] {
      Level.FATAL, Level.ERROR, Level.WARN, Level.INFO, Level.DEBUG,
      Level.TRACE
    };
  }

  /**
   * Returns the string representation of this Level.
   *
   * @return String The Level name
   */
  public final String toString() {
    return levelStr;
  }

  /**
   * Returns the integer representation of this level.
   */
  public final int toInt() {
    return level;
  }

  /**
   * Convert an integer passed as argument to a level. If the
   * conversion fails, then this method returns the specified default.
   *
   * @param val The integer value
   * @param defaultLevel The Level to return if no match is found
   * @return The matching Level
   */
  public static Level toLevel(int val, Level defaultLevel) {
    switch (val) {
    case ALL_INT:
      return ALL;
    case TRACE_INT:
      return TRACE;
    case DEBUG_INT:
      return DEBUG;
    case INFO_INT:
      return INFO;
    case WARN_INT:
      return WARN;
    case ERROR_INT:
      return ERROR;
    case FATAL_INT:
      return FATAL;
    case OFF_INT:
      return OFF;
    default:
      return defaultLevel;
    }
  }

  /**
   * Convert the string passed as argument to a level. If the
   * conversion fails, then this method returns the
   * <code>defaultLevel</code>.
   *
   * @param sArg The Level name
   * @param defaultLevel Level to return if no match is found
   * @return The matching Level
   */
  public static Level toLevel(String sArg, Level defaultLevel) {
    if (sArg == null) {
      return defaultLevel;
    }

    String s = sArg.toUpperCase();

    if (s.equals("ALL")) {
      return ALL;
    }

    if (s.equals("TRACE")) {
      return TRACE;
    }

    if (s.equals("DEBUG")) {
      return DEBUG;
    }

    if (s.equals("INFO")) {
      return INFO;
    }

    if (s.equals("WARN")) {
      return WARN;
    }

    if (s.equals("ERROR")) {
      return ERROR;
    }

    if (s.equals("FATAL")) {
      return FATAL;
    }

    if (s.equals("OFF")) {
      return OFF;
    }

    return defaultLevel;
  }
}

// End of class: Level.java
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
	scala.collection.parallel.AdaptiveWorkStealingTasks$AWSTWrappedTask.compute(Tasks.scala:152)
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

QDox parse error in file://<WORKSPACE>/data/code-rep-dataset/Dataset2/Tasks/966.java