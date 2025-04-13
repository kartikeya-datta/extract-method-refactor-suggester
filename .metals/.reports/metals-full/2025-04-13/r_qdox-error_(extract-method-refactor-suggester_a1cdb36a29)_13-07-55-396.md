error id: file://<WORKSPACE>/data/code-rep-dataset/Dataset2/Tasks/7939.java
file://<WORKSPACE>/data/code-rep-dataset/Dataset2/Tasks/7939.java
### com.thoughtworks.qdox.parser.ParseException: syntax error @[1,1]

error in qdox parser
file content:
```java
offset: 1
uri: file://<WORKSPACE>/data/code-rep-dataset/Dataset2/Tasks/7939.java
text:
```scala
C@@olumbaLogger.log.info(

//The contents of this file are subject to the Mozilla Public License Version 1.1
//(the "License"); you may not use this file except in compliance with the 
//License. You may obtain a copy of the License at http://www.mozilla.org/MPL/
//
//Software distributed under the License is distributed on an "AS IS" basis,
//WITHOUT WARRANTY OF ANY KIND, either express or implied. See the License 
//for the specific language governing rights and
//limitations under the License.
//
//The Original Code is "The Columba Project"
//
//The Initial Developers of the Original Code are Frederik Dietz and Timo Stich.
//Portions created by Frederik Dietz and Timo Stich are Copyright (C) 2003. 
//
//All Rights Reserved.
package org.columba.core.main;

import org.columba.core.logging.ColumbaLogger;

import java.io.BufferedReader;
import java.io.InputStreamReader;

import java.net.ServerSocket;
import java.net.Socket;

import java.util.List;
import java.util.StringTokenizer;
import java.util.Vector;


/**
 * Opens a server socket to manage multiple sessions of Columba
 * able to passing commands to the main session.
 * <p>
 *
 * ideas taken from www.jext.org (author Roman Guy)
 *
 * @author fdietz
 *
 */
public class ColumbaLoader implements Runnable {
    public final static int COLUMBA_PORT = 50000;
    private Thread thread;
    private ServerSocket serverSocket;

    public ColumbaLoader() {
        try {
            serverSocket = new ServerSocket(COLUMBA_PORT);
        } catch (Exception ex) {
            ex.printStackTrace();
        }

        thread = new Thread(this);
        thread.setDaemon(false);
        thread.start();
    }

    public synchronized void stop() {
        thread.interrupt();
        thread = null;

        try {
            if (serverSocket != null) {
                serverSocket.close();
            }
        } catch (Exception ex) {
            ex.printStackTrace();
        }
    }

    public synchronized boolean isRunning() {
        return thread != null;
    }

    public void run() {
        while (isRunning()) {
            try {
                // does a client trying to connect to server ?
                Socket client = serverSocket.accept();

                if (client == null) {
                    continue;
                }

                // only accept client from local machine
                String host = client.getLocalAddress().getHostAddress();

                if (!(host.equals("127.0.0.1"))) {
                    // client isn't from local machine
                    client.close();
                }

                // try to read possible arguments
                BufferedReader reader = new BufferedReader(new InputStreamReader(
                            client.getInputStream()));

                StringBuffer arguments = new StringBuffer();
                arguments.append(reader.readLine());

                if (!(arguments.toString().startsWith("columba:"))) {
                    // client isn't a Columba client
                    client.close();
                }

                if (MainInterface.DEBUG) {
                    ColumbaLogger.log.debug(
                        "passing to running Columba session:\n" +
                        arguments.toString());
                }

                // do something with the arguments..
                handleArgs(arguments.toString());

                client.close();
            } catch (Exception ex) {
                ex.printStackTrace();
            }
        }
    }

    /**
     * Parsing the given argumentString and split this String into a StringArray. The separator is
     * the character %, thus the whole arguments should not have this character inside. The
     * character itselfs is added in Main.java @see Main#loadInVMInstance(String[]). After splitting
     * is finished the CmdLineArgumentHandler is called, to do things with the arguments
     * @see CmdLineArgumentHandler
     * @param argumentString String which holds any arguments seperated by <br>%</br> character
     */
    protected void handleArgs(String argumentString) {
        List v = new Vector();

        // remove trailing "columba:"
        argumentString = argumentString.substring(8, argumentString.length());

        StringTokenizer st = new StringTokenizer(argumentString, "%");

        while (st.hasMoreTokens()) {
            String tok = (String) st.nextToken();
            v.add(tok);
        }

        String[] args = new String[v.size()];
        v.toArray((String[]) args);

        new CmdLineArgumentHandler(args);
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

QDox parse error in file://<WORKSPACE>/data/code-rep-dataset/Dataset2/Tasks/7939.java