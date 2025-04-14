error id: file://<WORKSPACE>/data/code-rep-dataset/Dataset5/Tasks/8784.java
file://<WORKSPACE>/data/code-rep-dataset/Dataset5/Tasks/8784.java
### com.thoughtworks.qdox.parser.ParseException: syntax error @[1,21]

error in qdox parser
file content:
```java
offset: 21
uri: file://<WORKSPACE>/data/code-rep-dataset/Dataset5/Tasks/8784.java
text:
```scala
public static final S@@tring time_text = "";

/* ********************************************************************
 * Copyright (c) 1998-2001 Xerox Corporation, 
 *               2002 Palo Alto Research Center, Incorporated (PARC).
 * All rights reserved. 
 * This program and the accompanying materials are made available 
 * under the terms of the Common Public License v1.0 
 * which accompanies this distribution and is available at
 * http://www.eclipse.org/legal/cpl-v10.html
 * 
 * Contributors:
 *     Xerox/PARC     initial implementation
 * *******************************************************************/

package org.aspectj.bridge;

import java.text.ParsePosition;
import java.text.SimpleDateFormat;
import java.util.Date;

/** release-specific version information */
public class Version {
    
    // generated from build/lib/BridgeVersion.java

    /** default version value for development version */
    public static final String DEVELOPMENT = "DEVELOPMENT";
    // VersionUptodate.java depends on this value

    /** default time value for development version */
    public static final long NOTIME = 0L;
    
    /** set by build script */
    public static final String text = "DEVELOPMENT";
    // VersionUptodate.java scans for "static final String text = "
    
    /** 
      * Time text set by build script using SIMPLE_DATE_FORMAT.
      * (if DEVELOPMENT version, invalid)
      */
    public static final String time_text = "mardi juil. 5, 2005 at 09:56:18 GMT";

    /** 
      * time in seconds-since-... format, used by programmatic clients.
      * (if DEVELOPMENT version, NOTIME)
      */
    public static final long time;
    
	/** format used by build script to set time_text */
    public static final String SIMPLE_DATE_FORMAT = "EEEE MMM d, yyyy 'at' HH:mm:ss z";
    
    // if not DEVELOPMENT version, read time text using format used to set time 
    static {
        long foundTime = NOTIME;
	    try {
	        SimpleDateFormat format = new SimpleDateFormat(SIMPLE_DATE_FORMAT);
	        ParsePosition pos = new ParsePosition(0);
	        Date date = format.parse(time_text, pos);
	        foundTime = date.getTime();
	    } catch (Throwable t) {            
	    }
        time = foundTime;
    }

    /**
     * Test whether the version is as specified by any first argument.
     * Emit text to System.err on failure
     * @param args String[] with first argument equal to Version.text
     * @see Version#text
     */
    public static void main(String[] args) {
        if ((null != args) && (0 < args.length)) {
            if (!Version.text.equals(args[0])) {
                System.err.println("version expected: \"" 
                                + args[0] 
                                + "\" actual=\"" 
                                + Version.text 
                                + "\"");
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

QDox parse error in file://<WORKSPACE>/data/code-rep-dataset/Dataset5/Tasks/8784.java