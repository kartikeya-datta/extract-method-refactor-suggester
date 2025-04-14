error id: file://<WORKSPACE>/data/code-rep-dataset/Dataset5/Tasks/8683.java
file://<WORKSPACE>/data/code-rep-dataset/Dataset5/Tasks/8683.java
### com.thoughtworks.qdox.parser.ParseException: syntax error @[1,1]

error in qdox parser
file content:
```java
offset: 1
uri: file://<WORKSPACE>/data/code-rep-dataset/Dataset5/Tasks/8683.java
text:
```scala
m@@_incompatible = ((flags & DUPLICATES_ALLOWED) != 0) ? new int[0] : new int[] { id };

/*
 * Licensed to the Apache Software Foundation (ASF) under one or more
 * contributor license agreements.  See the NOTICE file distributed with
 * this work for additional information regarding copyright ownership.
 * The ASF licenses this file to You under the Apache License, Version 2.0
 * (the "License"); you may not use this file except in compliance with
 * the License.  You may obtain a copy of the License at
 *
 *   http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed  under the  License is distributed on an "AS IS" BASIS,
 * WITHOUT  WARRANTIES OR CONDITIONS  OF ANY KIND, either  express  or
 * implied.
 *
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */
package org.apache.commons.cli.avalon;

// Renamed from org.apache.avalon.excalibur.cli

/**
 * Basic class describing an type of option. Typically, one creates a static
 * array of <code>CLOptionDescriptor</code>s, and passes it to
 * {@link CLArgsParser#CLArgsParser(String[], CLOptionDescriptor[])}.
 *
 * @see CLArgsParser
 * @see CLUtil
 */
public final class CLOptionDescriptor {
    /** Flag to say that one argument is required */
    public static final int ARGUMENT_REQUIRED = 1 << 1;

    /** Flag to say that the argument is optional */
    public static final int ARGUMENT_OPTIONAL = 1 << 2;

    /** Flag to say this option does not take arguments */
    public static final int ARGUMENT_DISALLOWED = 1 << 3;

    /** Flag to say this option requires 2 arguments */
    public static final int ARGUMENTS_REQUIRED_2 = 1 << 4;

    /** Flag to say this option may be repeated on the command line */
    public static final int DUPLICATES_ALLOWED = 1 << 5;

    private final int m_id;

    private final int m_flags;

    private final String m_name;

    private final String m_description;

    private final int[] m_incompatible;

    /**
     * Constructor.
     *
     * @param name
     *            the name/long option
     * @param flags
     *            the flags
     * @param id
     *            the id/character option
     * @param description
     *            description of option usage
     */
    public CLOptionDescriptor(final String name, final int flags, final int id, final String description) {

        checkFlags(flags);

        m_id = id;
        m_name = name;
        m_flags = flags;
        m_description = description;
        m_incompatible = ((flags & DUPLICATES_ALLOWED) > 0) ? new int[0] : new int[] { id };
    }


    /**
     * Constructor.
     *
     * @param name
     *            the name/long option
     * @param flags
     *            the flags
     * @param id
     *            the id/character option
     * @param description
     *            description of option usage
     * @param incompatible
     *            descriptors for incompatible options
     */
    public CLOptionDescriptor(final String name, final int flags, final int id, final String description,
            final CLOptionDescriptor[] incompatible) {

        checkFlags(flags);

        m_id = id;
        m_name = name;
        m_flags = flags;
        m_description = description;

        m_incompatible = new int[incompatible.length];
        for (int i = 0; i < incompatible.length; i++) {
            m_incompatible[i] = incompatible[i].getId();
        }
    }

    private void checkFlags(final int flags) {
        int modeCount = 0;
        if ((ARGUMENT_REQUIRED & flags) == ARGUMENT_REQUIRED) {
            modeCount++;
        }
        if ((ARGUMENT_OPTIONAL & flags) == ARGUMENT_OPTIONAL) {
            modeCount++;
        }
        if ((ARGUMENT_DISALLOWED & flags) == ARGUMENT_DISALLOWED) {
            modeCount++;
        }
        if ((ARGUMENTS_REQUIRED_2 & flags) == ARGUMENTS_REQUIRED_2) {
            modeCount++;
        }

        if (0 == modeCount) {
            final String message = "No mode specified for option " + this;
            throw new IllegalStateException(message);
        } else if (1 != modeCount) {
            final String message = "Multiple modes specified for option " + this;
            throw new IllegalStateException(message);
        }
    }

    /**
     * Get the array of incompatible option ids.
     *
     * @return the array of incompatible option ids
     */
    protected final int[] getIncompatible() {
        return m_incompatible;
    }

    /**
     * Retrieve textual description.
     *
     * @return the description
     */
    public final String getDescription() {
        return m_description;
    }

    /**
     * Retrieve flags about option. Flags include details such as whether it
     * allows parameters etc.
     *
     * @return the flags
     */
    public final int getFlags() {
        return m_flags;
    }

    /**
     * Retrieve the id for option. The id is also the character if using single
     * character options.
     *
     * @return the id
     */
    public final int getId() {
        return m_id;
    }

    /**
     * Retrieve name of option which is also text for long option.
     *
     * @return name/long option
     */
    public final String getName() {
        return m_name;
    }

    /**
     * Convert to String.
     *
     * @return the converted value to string.
     */
    @Override
    public final String toString() {
        final StringBuilder sb = new StringBuilder();
        sb.append("[OptionDescriptor ");
        sb.append(m_name);
        sb.append(", ");
        sb.append(m_id);
        sb.append(", ");
        sb.append(m_flags);
        sb.append(", ");
        sb.append(m_description);
        sb.append(" ]");
        return sb.toString();
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

QDox parse error in file://<WORKSPACE>/data/code-rep-dataset/Dataset5/Tasks/8683.java