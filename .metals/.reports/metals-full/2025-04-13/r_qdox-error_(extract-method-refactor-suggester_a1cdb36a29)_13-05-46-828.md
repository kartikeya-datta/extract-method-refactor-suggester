error id: file://<WORKSPACE>/data/code-rep-dataset/Dataset5/Tasks/16005.java
file://<WORKSPACE>/data/code-rep-dataset/Dataset5/Tasks/16005.java
### com.thoughtworks.qdox.parser.ParseException: syntax error @[1,1]

error in qdox parser
file content:
```java
offset: 1
uri: file://<WORKSPACE>/data/code-rep-dataset/Dataset5/Tasks/16005.java
text:
```scala
s@@etProperty( timeoutProperty, "true" );

/*
 * Copyright (C) The Apache Software Foundation. All rights reserved.
 *
 * This software is published under the terms of the Apache Software License
 * version 1.1, a copy of which has been included with this distribution in
 * the LICENSE file.
 */
package org.apache.tools.ant.taskdefs;

import java.util.Hashtable;
import org.apache.myrmidon.api.TaskException;
import org.apache.tools.ant.taskdefs.condition.Condition;
import org.apache.tools.ant.taskdefs.condition.ConditionBase;
import org.apache.tools.ant.types.EnumeratedAttribute;

/**
 * Wait for an external event to occur. Wait for an external process to start or
 * to complete some task. This is useful with the <code>parallel</code> task to
 * syncronize the execution of tests with server startup. The following
 * attributes can be specified on a waitfor task:
 * <ul>
 *   <li> maxwait - maximum length of time to wait before giving up</li>
 *   <li> maxwaitunit - The unit to be used to interpret maxwait attribute</li>
 *
 *   <li> checkevery - amount of time to sleep between each check</li>
 *   <li> checkeveryunit - The unit to be used to interpret checkevery attribute
 *   </li>
 *   <li> timeoutproperty - name of a property to set if maxwait has been
 *   exceeded.</li>
 * </ul>
 * The maxwaitunit and checkeveryunit are allowed to have the following values:
 * millesond, second, minute, hour, day and week. The default is millisecond.
 *
 * @author <a href="mailto:denis@network365.com">Denis Hennessy</a>
 * @author <a href="mailto:umagesh@apache.org">Magesh Umasankar</a>
 */

public class WaitFor extends ConditionBase
{
    private long maxWaitMillis = 1000l * 60l * 3l;// default max wait time
    private long maxWaitMultiplier = 1l;
    private long checkEveryMillis = 500l;
    private long checkEveryMultiplier = 1l;
    private String timeoutProperty;

    /**
     * Set the time between each check
     *
     * @param time The new CheckEvery value
     */
    public void setCheckEvery( long time )
    {
        checkEveryMillis = time;
    }

    /**
     * Set the check every time unit
     *
     * @param unit The new CheckEveryUnit value
     */
    public void setCheckEveryUnit( Unit unit )
    {
        checkEveryMultiplier = unit.getMultiplier();
    }

    /**
     * Set the maximum length of time to wait
     *
     * @param time The new MaxWait value
     */
    public void setMaxWait( long time )
    {
        maxWaitMillis = time;
    }

    /**
     * Set the max wait time unit
     *
     * @param unit The new MaxWaitUnit value
     */
    public void setMaxWaitUnit( Unit unit )
    {
        maxWaitMultiplier = unit.getMultiplier();
    }

    /**
     * Set the timeout property.
     *
     * @param p The new TimeoutProperty value
     */
    public void setTimeoutProperty( String p )
    {
        timeoutProperty = p;
    }

    /**
     * Check repeatedly for the specified conditions until they become true or
     * the timeout expires.
     *
     * @exception TaskException Description of Exception
     */
    public void execute()
        throws TaskException
    {
        if( countConditions() > 1 )
        {
            throw new TaskException( "You must not nest more than one condition into <waitfor>" );
        }
        if( countConditions() < 1 )
        {
            throw new TaskException( "You must nest a condition into <waitfor>" );
        }
        Condition c = (Condition)getConditions().nextElement();

        maxWaitMillis *= maxWaitMultiplier;
        checkEveryMillis *= checkEveryMultiplier;
        long start = System.currentTimeMillis();
        long end = start + maxWaitMillis;

        while( System.currentTimeMillis() < end )
        {
            if( c.eval() )
            {
                return;
            }
            try
            {
                Thread.sleep( checkEveryMillis );
            }
            catch( InterruptedException e )
            {
            }
        }

        if( timeoutProperty != null )
        {
            project.setNewProperty( timeoutProperty, "true" );
        }
    }

    public static class Unit extends EnumeratedAttribute
    {

        private final static String MILLISECOND = "millisecond";
        private final static String SECOND = "second";
        private final static String MINUTE = "minute";
        private final static String HOUR = "hour";
        private final static String DAY = "day";
        private final static String WEEK = "week";

        private final static String[] units = {
            MILLISECOND, SECOND, MINUTE, HOUR, DAY, WEEK
        };

        private Hashtable timeTable = new Hashtable();

        public Unit()
        {
            timeTable.put( MILLISECOND, new Long( 1l ) );
            timeTable.put( SECOND, new Long( 1000l ) );
            timeTable.put( MINUTE, new Long( 1000l * 60l ) );
            timeTable.put( HOUR, new Long( 1000l * 60l * 60l ) );
            timeTable.put( DAY, new Long( 1000l * 60l * 60l * 24l ) );
            timeTable.put( WEEK, new Long( 1000l * 60l * 60l * 24l * 7l ) );
        }

        public long getMultiplier()
        {
            String key = getValue().toLowerCase();
            Long l = (Long)timeTable.get( key );
            return l.longValue();
        }

        public String[] getValues()
        {
            return units;
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

QDox parse error in file://<WORKSPACE>/data/code-rep-dataset/Dataset5/Tasks/16005.java