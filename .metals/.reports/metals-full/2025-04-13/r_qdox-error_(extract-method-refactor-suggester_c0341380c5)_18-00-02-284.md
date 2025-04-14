error id: file://<WORKSPACE>/data/code-rep-dataset/Dataset5/Tasks/7666.java
file://<WORKSPACE>/data/code-rep-dataset/Dataset5/Tasks/7666.java
### com.thoughtworks.qdox.parser.ParseException: syntax error @[1,1]

error in qdox parser
file content:
```java
offset: 1
uri: file://<WORKSPACE>/data/code-rep-dataset/Dataset5/Tasks/7666.java
text:
```scala
o@@utput.println("\t\t\t<name><![CDATA[" + file.getName() + "]]></name>");

/*
 * Copyright  2002-2004 The Apache Software Foundation
 *
 *  Licensed under the Apache License, Version 2.0 (the "License");
 *  you may not use this file except in compliance with the License.
 *  You may obtain a copy of the License at
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
package org.apache.tools.ant.taskdefs.cvslib;

import java.io.PrintWriter;
import java.text.SimpleDateFormat;
import java.util.Enumeration;
import java.util.TimeZone;

/**
 * Class used to generate an XML changelog.
 *
 * @version $Revision$ $Date$
 */
class ChangeLogWriter {
    /** output format for dates written to xml file */
    private static final SimpleDateFormat c_outputDate
        = new SimpleDateFormat("yyyy-MM-dd");
    /** output format for times written to xml file */
    private static final SimpleDateFormat c_outputTime
        = new SimpleDateFormat("HH:mm");

    static {
        TimeZone utc = TimeZone.getTimeZone("UTC");
        c_outputDate.setTimeZone(utc);
        c_outputTime.setTimeZone(utc);
    }

    /**
     * Print out the specified entries.
     *
     * @param output writer to which to send output.
     * @param entries the entries to be written.
     */
    public void printChangeLog(final PrintWriter output,
                               final CVSEntry[] entries) {
        output.println("<?xml version=\"1.0\" encoding=\"UTF-8\"?>");
        output.println("<changelog>");
        for (int i = 0; i < entries.length; i++) {
            final CVSEntry entry = entries[i];

            printEntry(output, entry);
        }
        output.println("</changelog>");
        output.flush();
        output.close();
    }


    /**
     * Print out an individual entry in changelog.
     *
     * @param entry the entry to print
     * @param output writer to which to send output.
     */
    private void printEntry(final PrintWriter output, final CVSEntry entry) {
        output.println("\t<entry>");
        output.println("\t\t<date>" + c_outputDate.format(entry.getDate())
            + "</date>");
        output.println("\t\t<time>" + c_outputTime.format(entry.getDate())
            + "</time>");
        output.println("\t\t<author><![CDATA[" + entry.getAuthor()
            + "]]></author>");

        final Enumeration enumeration = entry.getFiles().elements();

        while (enumeration.hasMoreElements()) {
            final RCSFile file = (RCSFile) enumeration.nextElement();

            output.println("\t\t<file>");
            output.println("\t\t\t<name>" + file.getName() + "</name>");
            output.println("\t\t\t<revision>" + file.getRevision()
                + "</revision>");

            final String previousRevision = file.getPreviousRevision();

            if (previousRevision != null) {
                output.println("\t\t\t<prevrevision>" + previousRevision
                    + "</prevrevision>");
            }

            output.println("\t\t</file>");
        }
        output.println("\t\t<msg><![CDATA[" + entry.getComment() + "]]></msg>");
        output.println("\t</entry>");
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

QDox parse error in file://<WORKSPACE>/data/code-rep-dataset/Dataset5/Tasks/7666.java