error id: file://<WORKSPACE>/data/code-rep-dataset/Dataset5/Tasks/8704.java
file://<WORKSPACE>/data/code-rep-dataset/Dataset5/Tasks/8704.java
### com.thoughtworks.qdox.parser.ParseException: syntax error @[128,2]

error in qdox parser
file content:
```java
offset: 4206
uri: file://<WORKSPACE>/data/code-rep-dataset/Dataset5/Tasks/8704.java
text:
```scala
package org.eclipse.ecf.internal.example.collab;

/****************************************************************************
* Copyright (c) 2004 Composent, Inc. and others.
* All rights reserved. This program and the accompanying materials
* are made available under the terms of the Eclipse Public License v1.0
* which accompanies this distribution, and is available at
* http://www.eclipse.org/legal/epl-v10.html
*
* Contributors:
*    Composent, Inc. - initial API and implementation
*****************************************************************************/

package org.eclipse.ecf.example.collab;

import java.io.File;
import java.io.FileOutputStream;
import java.io.PrintStream;
import java.text.SimpleDateFormat;
import java.util.Date;
import org.eclipse.core.runtime.Platform;

public class Trace {
    private static final String TRACENAME = "org.eclipse.ecf.example.collab.Trace";

    public static final String tracePrefix = "(trace)";
    
    public static boolean ON = false;
    protected static boolean isEclipse = false;
    protected static String pluginName = "";
    protected static String debugPrefix = "/debug/";
    
    protected static PrintStream getPrintStream(String outputFileName) {
    	if (outputFileName != null) try {
    		File f = new File(outputFileName);
    		PrintStream ps = new PrintStream(new FileOutputStream(f,true));
        	System.out.println(TRACENAME+" directed to "+f.getCanonicalPath());
        	return ps;
    	} catch (Exception e) {
    		System.err.println("Exception opening output file '"+outputFileName+"' for tracing...using System.out");
    	}
    	return System.out;
    }
    
    protected static PrintStream printStream = null;
    
    static {
        String val = System.getProperty(TRACENAME);
    	printStream = getPrintStream(val);
        if (val != null) {
        	ON = true;
        	isEclipse = false;
        } else {
	        try {
	            ON = Platform.inDebugMode();
	            pluginName = ClientPlugin.getDefault().getBundle().getSymbolicName();
	        } catch (Exception e) {
	        	System.out.println("WARNING: Platform not available for trace");
	        }
	        isEclipse = true;
        }
    }

    public static Trace create(String key) {
        if (isEclipse) {
            String res = Platform
                    .getDebugOption(pluginName + debugPrefix + key);
            if (res != null) {
                Boolean on = new Boolean(res);
                if (on.booleanValue())
                    return new Trace(pluginName + "(" + key + ")");
                else
                    return null;
            } else {
                return null;
            }
        } else
            return new Trace(key);
    }

    String name;

    public void dumpStack(Throwable e, String msg) {
        msg(msg);
        e.printStackTrace(printStream);
    }

    public void msg(String msg) {
        StringBuffer sb = new StringBuffer(name);
        sb.append(getTimeString()).append(msg);
        printStream.println(sb.toString());
    }

    protected static String getTimeString() {
        Date d = new Date();
        SimpleDateFormat df = new SimpleDateFormat("[MM/dd/yy;HH:mm:ss:SSS]");
        return df.format(d);
    }

    protected Trace(String str) {
        name = tracePrefix+str;
    }
    public static String convertStringAToString(String [] strings) {
        if (strings==null) return "";
        StringBuffer sb = new StringBuffer();
        for(int i=0; i < strings.length; i++) {
            if (strings[i]==null) sb.append("(null)");
            else sb.append(strings[i]);
            if (i != (strings.length-1)) sb.append(";");
        }
        return sb.toString();
    }
    public static String convertObjectAToString(Object [] objs) {
        if (objs==null) return "";
        StringBuffer sb = new StringBuffer();
        for(int i=0; i < objs.length; i++) {
            if (objs[i]==null) sb.append("(null)");
            else sb.append(objs[i].toString());
            if (i != (objs.length-1)) sb.append(";");
        }
        return sb.toString();
    }

    public static void setThreadDebugGroup(Object obj) {
        // Do nothing
    }
}
 N@@o newline at end of file
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

QDox parse error in file://<WORKSPACE>/data/code-rep-dataset/Dataset5/Tasks/8704.java