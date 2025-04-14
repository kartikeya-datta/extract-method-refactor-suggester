error id: file://<WORKSPACE>/data/code-rep-dataset/Dataset5/Tasks/8018.java
file://<WORKSPACE>/data/code-rep-dataset/Dataset5/Tasks/8018.java
### com.thoughtworks.qdox.parser.ParseException: syntax error @[280,2]

error in qdox parser
file content:
```java
offset: 7698
uri: file://<WORKSPACE>/data/code-rep-dataset/Dataset5/Tasks/8018.java
text:
```scala
{

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
package wicket;

import java.io.Serializable;

/**
 * Represents a generic message meant for the end-user/ pages.
 *
 * @author Eelco Hillenius
 */
public class FeedbackMessage implements Serializable
{ // TODO finalize javadoc
    /**
     * Constant for an undefined level; note that components might decide
     * not to render anything when this level is used.
     */
    public static final int UNDEFINED = 0;

    /** constant for debug level. */
    public static final int DEBUG = 1;

    /** constant for info level. */
    public static final int INFO = 2;

    /** constant for warn level. */
    public static final int WARN = 3;

    /** constant for error level. */
    public static final int ERROR = 4;

    /** constant for fatal level. */
    public static final int FATAL = 5;

    /** levels as strings for debugging/ toString method. */
    private static final String[] LEVELS_AS_STRING = new String[]
    {
        "UNDEFINED", "DEBUG", "INFO", "WARN", "ERROR", "FATAL"
    };

    /** the reporting component. */
    private Component reporter;

    /** the actual message. */
    private String message;

    /**
     * The message level; can be used by rendering components.
     * Note that what actually happens with the level indication is
     * totally up to the components that render messages like these.
     * The default level is UNDEFINED.
     */
    private int level = UNDEFINED;

    /**
     * Construct using fields.
     * @param reporter the message reporter
     * @param message the actual message
     * @param level the level of the message
     */
    public FeedbackMessage(Component reporter, String message, int level)
    {
        this.reporter = reporter;
        this.message = message;
        this.level = level;
    }

    /**
     * Gets a new constructed message with level DEBUG.
     * @param reporter the reporter of the message
     * @param message the actual message
     * @return a new message with level DEBUG
     */
    public final static FeedbackMessage debug(Component reporter, String message)
    {
        return new FeedbackMessage(reporter, message, DEBUG);
    }

    /**
     * Gets a new constructed message with level INFO.
     * @param reporter the reporter of the message
     * @param message the actual message
     * @return a new message with level INFO
     */
    public final static FeedbackMessage info(Component reporter, String message)
    {
        return new FeedbackMessage(reporter, message, INFO);
    }

    /**
     * Gets a new constructed message with level WARN.
     * @param reporter the reporter of the message
     * @param message the actual message
     * @return a new message with level WARN
     */
    public final static FeedbackMessage warn(Component reporter, String message)
    {
        return new FeedbackMessage(reporter, message, WARN);
    }

    /**
     * Gets a new constructed message with level ERROR.
     * @param reporter the reporter of the message
     * @param message the actual message
     * @return a new message with level ERROR
     */
    public final static FeedbackMessage error(Component reporter, String message)
    {
        return new FeedbackMessage(reporter, message, ERROR);
    }

    /**
     * Gets a new constructed message with level FATAL.
     * @param reporter the reporter of the message
     * @param message the actual message
     * @return a new message with level FATAL
     */
    public final static FeedbackMessage fatal(Component reporter, String message)
    {
        return new FeedbackMessage(reporter, message, FATAL);
    }

    /**
     * Gets the actual message.
     * @return the message.
     */
    public final String getMessage()
    {
        return message;
    }

    /**
     * Sets the actual message.
     * @param message the actual message.
     */
    protected final void setMessage(String message)
    {
        this.message = message;
    }

    /**
     * Gets the reporting component.
     * @return the reporting component.
     */
    public final Component getReporter()
    {
        return reporter;
    }

    /**
     * Sets the reporting component.
     * @param reporter the reporting component.
     */
    public final void setReporter(Component reporter)
    {
        this.reporter = reporter;
    }

    /**
     * Gets the message level; can be used by rendering components.
     * Note that what actually happens with the level indication is
     * totally up to the components that render messages like these.
     * @return the message level indicator.
     */
    public final int getLevel()
    {
        return level;
    }

    /**
     * Gets the current level as a String
     * @return the current level as a String
     */
    public final String getLevelAsString()
    {
        return LEVELS_AS_STRING[getLevel()];
    }

    /**
     * Sets the message level; can be used by rendering components.
     * Note that what actually happens with the level indication is
     * totally up to the components that render messages like these.
     * @param level the message level indicator.
     */
    protected final void setLevel(int level)
    {
        this.level = level;
    }

    /**
     * Gets whether the current level is UNDEFINED.
     * @return whether the current level is UNDEFINED.
     */
    public final boolean isLevelUndefined()
    {
        return (getLevel() == UNDEFINED); 
    }

    /**
     * Gets whether the current level is DEBUG or up.
     * @return whether the current level is DEBUG or up.
     */
    public final boolean isLevelDebug()
    {
        return isLevel(DEBUG); 
    }

    /**
     * Gets whether the current level is INFO or up.
     * @return whether the current level is INFO or up.
     */
    public final boolean isLevelInfo()
    {
        return isLevel(INFO); 
    }

    /**
     * Gets whether the current level is WARN or up.
     * @return whether the current level is WARN or up.
     */
    public final boolean isLevelWarn()
    {
        return isLevel(WARN); 
    }

    /**
     * Gets whether the current level is ERROR or up.
     * @return whether the current level is ERROR or up.
     */
    public final boolean isLevelError()
    {
        return isLevel(ERROR); 
    }

    /**
     * Gets whether the current level is FATAL or up.
     * @return whether the current level is FATAL or up.
     */
    public final boolean isLevelFatal()
    {
        return isLevel(FATAL); 
    }

    /**
     * Returns whether this level is greater than or equal to the given level.
     * @param level the level
     * @return whether this level is greater than or equal to the given level
     */
    public final boolean isLevel(int level)
    {
        return (getLevel() >= level);
    }

    /**
     * @see java.lang.Object#toString()
     */
    public String toString()
    {
        return ("'" + getMessage() + "' (reporter: " + getReporter().getName()
                + ", level: " + LEVELS_AS_STRING[getLevel()] + ")");
    }
}@@
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

QDox parse error in file://<WORKSPACE>/data/code-rep-dataset/Dataset5/Tasks/8018.java