error id: file://<WORKSPACE>/data/code-rep-dataset/Dataset3/Tasks/826.java
file://<WORKSPACE>/data/code-rep-dataset/Dataset3/Tasks/826.java
### com.thoughtworks.qdox.parser.ParseException: syntax error @[1,9]

error in qdox parser
file content:
```java
offset: 9
uri: file://<WORKSPACE>/data/code-rep-dataset/Dataset3/Tasks/826.java
text:
```scala
private S@@tring appClientCommand = null;

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
package org.jboss.as.test.integration.ee.appclient.util;

import java.io.BufferedReader;
import java.io.File;
import java.io.InputStreamReader;
import java.io.PrintWriter;
import java.util.Vector;
import java.util.concurrent.BlockingQueue;
import java.util.concurrent.LinkedBlockingQueue;
import java.util.concurrent.TimeUnit;

import org.jboss.shrinkwrap.api.Archive;
import org.jboss.shrinkwrap.api.exporter.ZipExporter;


/**
 * @author Dominik Pospisil <dpospisi@redhat.com>
 * @author Stuart Douglas
 */
public class AppClientWrapper implements Runnable {

    private static String appClientCommand = null;

    private static final String outThreadHame = "APPCLIENT-out";
    private static final String errThreadHame = "APPCLIENT-err";

    private Process appClientProcess;
    private PrintWriter writer;
    private BufferedReader outputReader;
    private BufferedReader errorReader;
    private BlockingQueue<String> outputQueue = new LinkedBlockingQueue<String>();
    private Thread shutdownThread;
    private final Archive<?> archive;
    private final String clientArchiveName;
    private File archiveOnDisk;
    private final String args;

    /**
     * Creates new CLI wrapper. If the connect parameter is set to true the CLI
     * will connect to the server using <code>connect</code> command.
     *
     *
     * @param archive
     * @param clientArchiveName
     * @param args
     * @throws Exception
     */
    public AppClientWrapper(final Archive<?> archive, final String clientArchiveName, final String args) throws Exception {
        this.archive = archive;
        this.clientArchiveName = clientArchiveName;
        this.args = args;
        init();
    }

    /**
     * Consumes all available output from App Client.
     *
     * @param timeout number of milliseconds to wait for each subsequent line
     * @return array of App Client output lines
     */
    public String[] readAll(final long timeout) {
        Vector<String> lines = new Vector<String>();
        String line = null;
        do {
            try {
                line = outputQueue.poll(timeout, TimeUnit.MILLISECONDS);
                if (line != null) lines.add(line);
            } catch (InterruptedException ioe) {
            }

        } while (line != null);
        return lines.toArray(new String[]{});
    }

    /**
     * Consumes all available output from CLI.
     *
     * @param timeout number of milliseconds to wait for each subsequent line
     * @return array of CLI output lines
     */
    public String readAllUnformated(long timeout) {
        String[] lines = readAll(timeout);
        StringBuilder buf = new StringBuilder();
        for (String line : lines) buf.append(line + "\n");
        return buf.toString();

    }

    /**
     * Kills the app client
     *
     * @throws Exception
     */
    public synchronized void quit() throws Exception {
        appClientProcess.destroy();
        try {
            appClientProcess.waitFor();
        } catch (InterruptedException e) {
            throw new RuntimeException(e);
        }
        Runtime.getRuntime().removeShutdownHook(shutdownThread);
        if (archiveOnDisk != null) {
            archiveOnDisk.delete();
        }
    }


    private void init() throws Exception {
        shutdownThread = new Thread(new Runnable() {
            @Override
            public void run() {
                if (appClientProcess != null) {
                    appClientProcess.destroy();
                    if (archiveOnDisk != null) {
                        archiveOnDisk.delete();
                    }
                    try {
                        appClientProcess.waitFor();
                    } catch (InterruptedException e) {
                        throw new RuntimeException(e);
                    }
                }
            }
        });
        Runtime.getRuntime().addShutdownHook(shutdownThread);
        appClientProcess = Runtime.getRuntime().exec(getAppClientCommand());
        writer = new PrintWriter(appClientProcess.getOutputStream());
        outputReader = new BufferedReader(new InputStreamReader(appClientProcess.getInputStream()));
        errorReader = new BufferedReader(new InputStreamReader(appClientProcess.getErrorStream()));

        final Thread readOutputThread = new Thread(this, outThreadHame);
        readOutputThread.start();
        final Thread readErrorThread = new Thread(this, errThreadHame);
        readErrorThread.start();

    }

    private String getAppClientCommand() throws Exception {
        if (appClientCommand != null) return appClientCommand;

        final String tempDir = System.getProperty("java.io.tmpdir");
        archiveOnDisk = new File(tempDir + File.separator + archive.getName());
        if(archiveOnDisk.exists()) {
            archiveOnDisk.delete();
        }
        final ZipExporter exporter = archive.as(ZipExporter.class);
        exporter.exportTo(archiveOnDisk);
        final String appClientArg;
        if(clientArchiveName == null) {
            appClientArg = archiveOnDisk.getAbsolutePath();
        } else {
            appClientArg = archiveOnDisk.getAbsolutePath() + "#" + clientArchiveName;
        }
        //TODO: this seems dodgy
        final File targetDir = new File("../../build/target");
        if ((!targetDir.exists()) || (!targetDir.isDirectory())) throw new Exception("Missing AS target directory.");
        final String[] children = targetDir.list();
        String asDir = null;
        for (final String child : children) {
            if (child.startsWith("jboss-as")) {
                asDir = child;
                break;
            }
        }
        if (asDir == null) throw new Exception("Missing AS target directory.");

        appClientCommand = "java " +
                "-Djboss.modules.dir=../../build/target/" + asDir + "/modules " +
                "-Djline.WindowsTerminal.directConsole=false " +
                "-jar ./target/jbossas/jboss-modules.jar " +
                "-mp ../../build/target/" + asDir + "/modules " +
                "-logmodule org.jboss.logmanager  org.jboss.as.appclient " +
                "-Djboss.server.base.dir=target/jbossas/appclient " +
                "-Djboss.home.dir=target/jbossas " +
                appClientArg + " " + args;
        return appClientCommand;
    }

    /**
     *
     */
    public void run() {
        final String threadName = Thread.currentThread().getName();
        final BufferedReader reader = threadName.equals(outThreadHame) ? outputReader : errorReader;
        try {
            String line = reader.readLine();
            while (line != null) {
                if (threadName.equals(outThreadHame))
                    outputLineReceived(line);
                else
                    errorLineReceived(line);
                line = reader.readLine();
            }
        } catch (Exception e) {
        } finally {
            synchronized (this) {
                if (threadName.equals(outThreadHame))
                    outputReader = null;
                else
                    errorReader = null;
            }
        }
    }

    private synchronized void outputLineReceived(String line) {
        System.out.println("[" + outThreadHame + "] " + line);
        outputQueue.add(line);
    }

    private synchronized void errorLineReceived(String line) {
        System.out.println("[" + outThreadHame + "] " + line);
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

QDox parse error in file://<WORKSPACE>/data/code-rep-dataset/Dataset3/Tasks/826.java