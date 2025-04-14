error id: file://<WORKSPACE>/data/code-rep-dataset/Dataset3/Tasks/6916.java
file://<WORKSPACE>/data/code-rep-dataset/Dataset3/Tasks/6916.java
### com.thoughtworks.qdox.parser.ParseException: syntax error @[1,1]

error in qdox parser
file content:
```java
offset: 1
uri: file://<WORKSPACE>/data/code-rep-dataset/Dataset3/Tasks/6916.java
text:
```scala
S@@tring port = "9999";

/*
* JBoss, Home of Professional Open Source.
* Copyright 2011, Red Hat, Inc., and individual contributors
* as indicated by the @author tags. See the copyright.txt file in the
* distribution for a full listing of individual contributors.
*
* This is free software; you can redistribute it and/or modify it
* under the terms of the GNU Lesser General Public License as
* published by the Free Software Foundation; either version 2.1 of
* the License, or (at your option) any later version.
*
* This software is distributed in the hope that it will be useful,
* but WITHOUT ANY WARRANTY; without even the implied warranty of
* MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE. See the GNU
* Lesser General Public License for more details.
*
* You should have received a copy of the GNU Lesser General Public
* License along with this software; if not, write to the Free
* Software Foundation, Inc., 51 Franklin St, Fifth Floor, Boston, MA
* 02110-1301 USA, or see the FSF site: http://www.fsf.org.
*/

package org.jboss.as.jdr;

import static org.jboss.as.jdr.JdrMessages.MESSAGES;

import org.apache.commons.cli.CommandLine;
import org.apache.commons.cli.CommandLineParser;
import org.apache.commons.cli.GnuParser;
import org.apache.commons.cli.HelpFormatter;
import org.apache.commons.cli.Options;
import org.apache.commons.cli.ParseException;

import org.jboss.as.controller.OperationFailedException;

/**
 * Provides a main for collecting a JDR report from the command line.
 *
 * @author Mike M. Clark
 * @author Jesse Jaggars
 */
public class CommandLineMain {

    private static CommandLineParser parser = new GnuParser();
    private static Options options = new Options();
    private static HelpFormatter formatter = new HelpFormatter();
    private static final String usage = "jdr.{sh,bat} [options]";

    static {
        options.addOption("h", "help", false, MESSAGES.jdrHelpMessage());
        options.addOption("H", "host", true, MESSAGES.jdrHostnameMessage());
        options.addOption("p", "port", true, MESSAGES.jdrPortMessage());
        options.addOption("s", "protocol", true, MESSAGES.jdrProtocolMessage());
    }

    /**
     * Creates a JBoss Diagnostic Reporter (JDR) Report. A JDR report response
     * is printed to <code>System.out</code>.
     *
     * @param args ignored
     */
    public static void main(String[] args) {
        String port = "9990";
        String host = "localhost";
        String protocol = "http-remoting";

        try {
            CommandLine line = parser.parse(options, args, false);

            if (line.hasOption("help")) {
                formatter.printHelp(usage, options);
                return;
            }
            if (line.hasOption("host")) {
                host = line.getOptionValue("host");
            }

            if (line.hasOption("port")) {
                port = line.getOptionValue("port");
            }

            if (line.hasOption("protocol")) {
                protocol = line.getOptionValue("protocol");
            }
        } catch (ParseException e) {
            System.out.println(e.getMessage());
            formatter.printHelp(usage, options);
            return;
        }

        System.out.println("Initializing JBoss Diagnostic Reporter...");

        JdrReportService reportService = new JdrReportService();

        JdrReport response = null;
        try {
            response = reportService.standaloneCollect(protocol, host, port);
            System.out.println("JDR started: " + response.getStartTime().toString());
            System.out.println("JDR ended: " + response.getEndTime().toString());
            System.out.println("JDR location: " + response.getLocation());
        } catch (OperationFailedException e) {
            System.out.println("Failed to complete the JDR report: " + e.getMessage());
        }

        System.exit(0);
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

QDox parse error in file://<WORKSPACE>/data/code-rep-dataset/Dataset3/Tasks/6916.java