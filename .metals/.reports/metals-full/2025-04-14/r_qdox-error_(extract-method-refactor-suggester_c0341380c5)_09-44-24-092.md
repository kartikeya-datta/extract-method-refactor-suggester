error id: file://<WORKSPACE>/data/code-rep-dataset/Dataset2/Tasks/3349.java
file://<WORKSPACE>/data/code-rep-dataset/Dataset2/Tasks/3349.java
### com.thoughtworks.qdox.parser.ParseException: syntax error @[1,10]

error in qdox parser
file content:
```java
offset: 10
uri: file://<WORKSPACE>/data/code-rep-dataset/Dataset2/Tasks/3349.java
text:
```scala
@version $@@Revision: 1.6 $

/*
 * JBoss, the OpenSource J2EE webOS
 *
 * Distributable under LGPL license.
 * See terms of license at gnu.org.
 */
package org.jboss.logging.log4j;

import java.io.PrintStream;

import org.apache.log4j.AppenderSkeleton;
import org.apache.log4j.Category;
import org.apache.log4j.Priority;
import org.apache.log4j.spi.LoggingEvent;
import org.apache.log4j.Layout;

/** A log4j Appender implementation that writes to the System.out and
System.err console streams. It also installs PrintStreams for System.out
and System.err to route logging through those objects to the log4j
system via a category named Default.

@author <a href="mailto:Scott_Stark@displayscape.com">Scott Stark</a>.
@version $Revision: 1.5 $
*/
public class ConsoleAppender extends AppenderSkeleton
{
    private Category category;
    private PrintStream out;
    private PrintStream err;

    /** Creates new ConsoleAppender */
    public ConsoleAppender()
    {
        out = System.out;
        err = System.err;
    }

    public void activateOptions()
    {
        super.activateOptions();
        category = Category.getInstance("Default");
        System.setOut(new CategoryStream(category, Priority.INFO, out));
        System.setErr(new CategoryStream(category, Priority.ERROR, err));
    }

    public boolean requiresLayout()
    {
        return true;
    }

    public void close()
    {
        if( out != null )
            System.setOut(out);
        out = null;
        if( err != null )
            System.setErr(err);
        err = null;
    }

    protected void append(LoggingEvent event)
    {
        String msg = this.layout.format(event);
        if( event.priority == Priority.ERROR )
            err.print(msg);
        else
            out.print(msg);
        if(this.layout.ignoresThrowable())
        {
            String[] s = event.getThrowableStrRep();
            if (s != null)
            {
        	    int len = s.length;
        	    for(int i = 0; i < len; i++)
                {
                    if( event.priority == Priority.ERROR )
                    {
        	            err.print(s[i]);
                	    err.print(Layout.LINE_SEP);
                    }
                    else
                    {
        	            out.print(s[i]);
        	            out.print(Layout.LINE_SEP);
                    }
                }
            }
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

QDox parse error in file://<WORKSPACE>/data/code-rep-dataset/Dataset2/Tasks/3349.java