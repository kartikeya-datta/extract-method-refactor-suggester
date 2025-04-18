error id: file://<WORKSPACE>/data/code-rep-dataset/Dataset5/Tasks/13832.java
file://<WORKSPACE>/data/code-rep-dataset/Dataset5/Tasks/13832.java
### com.thoughtworks.qdox.parser.ParseException: syntax error @[1,1]

error in qdox parser
file content:
```java
offset: 1
uri: file://<WORKSPACE>/data/code-rep-dataset/Dataset5/Tasks/13832.java
text:
```scala
S@@tringBuilder buf = new StringBuilder("{dateselector date: ");

/*
 *  Licensed to the Apache Software Foundation (ASF) under one or more
 *  contributor license agreements.  See the NOTICE file distributed with
 *  this work for additional information regarding copyright ownership.
 *  The ASF licenses this file to You under the Apache License, Version 2.0
 *  (the "License"); you may not use this file except in compliance with
 *  the License.  You may obtain a copy of the License at
 *
 *      http://www.apache.org/licenses/LICENSE-2.0
 *
 *  Unless required by applicable law or agreed to in writing, software
 *  distributed under the License is distributed on an "AS IS" BASIS,
 *  WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 *  See the License for the specific language governing permissions and
 *  limitations under the License.
 *
 */

package org.apache.tools.ant.types.selectors;

import java.io.File;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.text.ParseException;
import java.util.Locale;

import org.apache.tools.ant.Project;
import org.apache.tools.ant.types.Parameter;
import org.apache.tools.ant.types.TimeComparison;
import org.apache.tools.ant.util.FileUtils;

/**
 * Selector that chooses files based on their last modified date.
 *
 * @since 1.5
 */
public class DateSelector extends BaseExtendSelector {

    /** Utilities used for file operations */
    private static final FileUtils FILE_UTILS = FileUtils.getFileUtils();

    private long millis = -1;
    private String dateTime = null;
    private boolean includeDirs = false;
    private long granularity = 0;
    private String pattern;
    private TimeComparison when = TimeComparison.EQUAL;

    /** Key to used for parameterized custom selector */
    public static final String MILLIS_KEY = "millis";
    /** Key to used for parameterized custom selector */
    public static final String DATETIME_KEY = "datetime";
    /** Key to used for parameterized custom selector */
    public static final String CHECKDIRS_KEY = "checkdirs";
    /** Key to used for parameterized custom selector */
    public static final String GRANULARITY_KEY = "granularity";
    /** Key to used for parameterized custom selector */
    public static final String WHEN_KEY = "when";
    /** Key to used for parameterized custom selector */
    public static final String PATTERN_KEY = "pattern";

    /**
     * Creates a new <code>DateSelector</code> instance.
     *
     */
    public DateSelector() {
        granularity = FILE_UTILS.getFileTimestampGranularity();
    }

    /**
     * @return a string describing this object
     */
    public String toString() {
        StringBuffer buf = new StringBuffer("{dateselector date: ");
        buf.append(dateTime);
        buf.append(" compare: ").append(when.getValue());
        buf.append(" granularity: ");
        buf.append(granularity);
        if (pattern != null) {
            buf.append(" pattern: ").append(pattern);
        }
        buf.append("}");
        return buf.toString();
    }

    /**
     * Set the time; for users who prefer to express time in ms since 1970.
     *
     * @param millis the time to compare file's last modified date to,
     *        expressed in milliseconds.
     */
    public void setMillis(long millis) {
        this.millis = millis;
    }

    /**
     * Returns the millisecond value the selector is set for.
     * @return the millisecond value.
     */
    public long getMillis() {
        if (dateTime != null) {
            validate();
        }
        return millis;
    }

    /**
     * Sets the date. The user must supply it in MM/DD/YYYY HH:MM AM_PM format,
     * unless an alternate pattern is specified via the pattern attribute.
     *
     * @param dateTime a formatted date <code>String</code>.
     */
    public void setDatetime(String dateTime) {
        this.dateTime = dateTime;
        millis = -1;
    }

    /**
     * Set whether to check dates on directories.
     *
     * @param includeDirs whether to check the timestamp on directories.
     */
    public void setCheckdirs(boolean includeDirs) {
        this.includeDirs = includeDirs;
    }

    /**
     * Sets the number of milliseconds leeway we will give before we consider
     * a file not to have matched a date.
     * @param granularity the number of milliseconds leeway.
     */
    public void setGranularity(int granularity) {
        this.granularity = granularity;
    }

    /**
     * Sets the type of comparison to be done on the file's last modified
     * date.
     *
     * @param tcmp The comparison to perform, an EnumeratedAttribute.
     */
    public void setWhen(TimeComparisons tcmp) {
        setWhen((TimeComparison) tcmp);
    }

    /**
     * Set the comparison type.
     * @param t TimeComparison object.
     */
    public void setWhen(TimeComparison t) {
        when = t;
    }

    /**
     * Sets the pattern to be used for the SimpleDateFormat.
     *
     * @param pattern the pattern that defines the date format.
     */
    public void setPattern(String pattern) {
        this.pattern = pattern;
    }

    /**
     * When using this as a custom selector, this method will be called.
     * It translates each parameter into the appropriate setXXX() call.
     *
     * @param parameters the complete set of parameters for this selector.
     */
    public void setParameters(Parameter[] parameters) {
        super.setParameters(parameters);
        if (parameters != null) {
            for (int i = 0; i < parameters.length; i++) {
                String paramname = parameters[i].getName();
                if (MILLIS_KEY.equalsIgnoreCase(paramname)) {
                    try {
                        setMillis(Long.parseLong(parameters[i].getValue()));
                    } catch (NumberFormatException nfe) {
                        setError("Invalid millisecond setting "
                                + parameters[i].getValue());
                    }
                } else if (DATETIME_KEY.equalsIgnoreCase(paramname)) {
                    setDatetime(parameters[i].getValue());
                } else if (CHECKDIRS_KEY.equalsIgnoreCase(paramname)) {
                    setCheckdirs(Project.toBoolean(parameters[i].getValue()));
                } else if (GRANULARITY_KEY.equalsIgnoreCase(paramname)) {
                    try {
                        setGranularity(Integer.parseInt(parameters[i].getValue()));
                    } catch (NumberFormatException nfe) {
                        setError("Invalid granularity setting "
                            + parameters[i].getValue());
                    }
                } else if (WHEN_KEY.equalsIgnoreCase(paramname)) {
                    setWhen(new TimeComparison(parameters[i].getValue()));
                } else if (PATTERN_KEY.equalsIgnoreCase(paramname)) {
                    setPattern(parameters[i].getValue());
                } else {
                    setError("Invalid parameter " + paramname);
                }
            }
        }
    }

    /**
     * This is a consistency check to ensure the selector's required
     * values have been set.
     */
    public void verifySettings() {
        if (dateTime == null && millis < 0) {
            setError("You must provide a datetime or the number of "
                    + "milliseconds.");
        } else if (millis < 0 && dateTime != null) {
            // check millis and only set it once.
            DateFormat df = ((pattern == null)
                ? DateFormat.getDateTimeInstance(
                    DateFormat.SHORT, DateFormat.SHORT, Locale.US)
                : new SimpleDateFormat(pattern));

            try {
                setMillis(df.parse(dateTime).getTime());
                if (millis < 0) {
                    setError("Date of " + dateTime
                        + " results in negative milliseconds value"
                        + " relative to epoch (January 1, 1970, 00:00:00 GMT).");
                }
            } catch (ParseException pe) {
                setError("Date of " + dateTime
                        + " Cannot be parsed correctly. It should be in"
                        + ((pattern == null)
                        ? " MM/DD/YYYY HH:MM AM_PM" : pattern) + " format.");
            }
        }
    }

    /**
     * The heart of the matter. This is where the selector gets to decide
     * on the inclusion of a file in a particular fileset.
     *
     * @param basedir the base directory from which the scan is being performed.
     * @param filename is the name of the file to check.
     * @param file is a java.io.File object the selector can use.
     * @return whether the file is selected.
     */
    public boolean isSelected(File basedir, String filename, File file) {

        validate();

        return (file.isDirectory() && !includeDirs)
 when.evaluate(file.lastModified(), millis, granularity);
    }

    /**
     * Enumerated attribute with the values for time comparison.
     * <p>
     */
    public static class TimeComparisons extends TimeComparison {
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

QDox parse error in file://<WORKSPACE>/data/code-rep-dataset/Dataset5/Tasks/13832.java