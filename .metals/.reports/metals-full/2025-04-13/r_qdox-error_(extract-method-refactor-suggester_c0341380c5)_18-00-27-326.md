error id: file://<WORKSPACE>/data/code-rep-dataset/Dataset3/Tasks/6367.java
file://<WORKSPACE>/data/code-rep-dataset/Dataset3/Tasks/6367.java
### com.thoughtworks.qdox.parser.ParseException: syntax error @[1,1]

error in qdox parser
file content:
```java
offset: 1
uri: file://<WORKSPACE>/data/code-rep-dataset/Dataset3/Tasks/6367.java
text:
```scala
b@@oolean b = ch >= '0' && ch <= '9';

/*
 * Licensed to the Apache Software Foundation (ASF) under one or more
 * contributor license agreements.  See the NOTICE file distributed with
 * this work for additional information regarding copyright ownership.
 * The ASF licenses this file to You under the Apache License, Version 2.0
 * (the "License"); you may not use this file except in compliance with
 * the License.  You may obtain a copy of the License at
 * 
 *      http://www.apache.org/licenses/LICENSE-2.0
 * 
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */

package org.apache.commons.lang3;

import java.text.NumberFormat;
import java.util.Calendar;

/**
 * Tests the difference in performance between CharUtils and CharSet.
 * 
 * Sample runs:

Now: Thu Mar 18 14:29:48 PST 2004
Sun Microsystems Inc. Java(TM) 2 Runtime Environment, Standard Edition 1.3.1_10-b03
Sun Microsystems Inc. Java HotSpot(TM) Client VM 1.3.1_10-b03
Windows XP 5.1 x86 pentium i486 i386
Do nohting: 0 milliseconds.
run_CharUtils_isAsciiNumeric: 4,545 milliseconds.
run_inlined_CharUtils_isAsciiNumeric: 3,417 milliseconds.
run_inlined_CharUtils_isAsciiNumeric: 85,679 milliseconds.


Now: Thu Mar 18 14:24:51 PST 2004
Sun Microsystems Inc. Java(TM) 2 Runtime Environment, Standard Edition 1.4.2_04-b05
Sun Microsystems Inc. Java HotSpot(TM) Client VM 1.4.2_04-b05
Windows XP 5.1 x86 pentium i486 i386
Do nohting: 0 milliseconds.
run_CharUtils_isAsciiNumeric: 2,578 milliseconds.
run_inlined_CharUtils_isAsciiNumeric: 2,477 milliseconds.
run_inlined_CharUtils_isAsciiNumeric: 114,429 milliseconds.

Now: Thu Mar 18 14:27:55 PST 2004
Sun Microsystems Inc. Java(TM) 2 Runtime Environment, Standard Edition 1.4.2_04-b05
Sun Microsystems Inc. Java HotSpot(TM) Server VM 1.4.2_04-b05
Windows XP 5.1 x86 pentium i486 i386
Do nohting: 0 milliseconds.
run_CharUtils_isAsciiNumeric: 630 milliseconds.
run_inlined_CharUtils_isAsciiNumeric: 709 milliseconds.
run_inlined_CharUtils_isAsciiNumeric: 84,420 milliseconds.


 * @version $Id$
 */
public class CharUtilsPerfRun {
    final static String VERSION = "$Id$";

    final static int WARM_UP = 100;

    final static int COUNT = 5000;

    final static char[] CHAR_SAMPLES;
    static {
        CHAR_SAMPLES = new char[Character.MAX_VALUE];
        for (char i = Character.MIN_VALUE; i < Character.MAX_VALUE; i++) {
            CHAR_SAMPLES[i] = i;
        }
    }

    public static void main(String[] args) {
        new CharUtilsPerfRun().run();
    }

    private void printSysInfo() {
        System.out.println(VERSION);
        System.out.println("Now: " + Calendar.getInstance().getTime());
        System.out.println(System.getProperty("java.vendor")
                + " "
                + System.getProperty("java.runtime.name")
                + " "
                + System.getProperty("java.runtime.version"));
        System.out.println(System.getProperty("java.vm.vendor")
                + " "
                + System.getProperty("java.vm.name")
                + " "
                + System.getProperty("java.vm.version"));
        System.out.println(System.getProperty("os.name")
            + " "
            + System.getProperty("os.version")
            + " "
            + System.getProperty("os.arch")
            + " "
            + System.getProperty("sun.cpu.isalist"));
    }

    private void run() {
        this.printSysInfo();
        long start;
        start = System.currentTimeMillis();
        this.printlnTotal("Do nohting", start);
        //System.out.println("Warming up...");
        run_CharUtils_isAsciiNumeric(WARM_UP);
        //System.out.println("Measuring...");
        start = System.currentTimeMillis();
        run_CharUtils_isAsciiNumeric(COUNT);
        this.printlnTotal("run_CharUtils_isAsciiNumeric", start);
        //System.out.println("Warming up...");
        run_inlined_CharUtils_isAsciiNumeric(WARM_UP);
        //System.out.println("Measuring...");
        start = System.currentTimeMillis();
        run_inlined_CharUtils_isAsciiNumeric(COUNT);
        this.printlnTotal("run_inlined_CharUtils_isAsciiNumeric", start);
        //System.out.println("Warming up...");
        run_CharSet(WARM_UP);
        //System.out.println("Measuring...");
        start = System.currentTimeMillis();
        run_CharSet(COUNT);
        this.printlnTotal("run_CharSet", start);
    }

    private int run_CharSet(int loopCount) {
        int t = 0;
        for (int i = 0; i < loopCount; i++) {
            for (char ch : CHAR_SAMPLES) {
                boolean b = CharSet.ASCII_NUMERIC.contains(ch);
                t += b ? 1 : 0;
            }
        }
        return t;
    }

    private int run_CharUtils_isAsciiNumeric(int loopCount) {
        int t = 0;
        for (int i = 0; i < loopCount; i++) {
            for (char ch : CHAR_SAMPLES) {
                boolean b = CharUtils.isAsciiNumeric(ch);
                t += b ? 1 : 0;
            }
        }
        return t;
    }

    private int run_inlined_CharUtils_isAsciiNumeric(int loopCount) {
        int t = 0;
        for (int i = 0; i < loopCount; i++) {
            for (char ch : CHAR_SAMPLES) {
                boolean b = (ch >= '0' && ch <= '9');
                t += b ? 1 : 0;
            }
        }
        return t;
    }

    private void printlnTotal(String prefix, long start) {
        long total = System.currentTimeMillis() - start;
        System.out.println(prefix + ": " + NumberFormat.getInstance().format(total) + " milliseconds.");
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

QDox parse error in file://<WORKSPACE>/data/code-rep-dataset/Dataset3/Tasks/6367.java