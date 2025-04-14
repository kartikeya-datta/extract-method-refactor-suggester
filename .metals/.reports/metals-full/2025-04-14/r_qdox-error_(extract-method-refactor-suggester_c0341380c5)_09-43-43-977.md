error id: file://<WORKSPACE>/data/code-rep-dataset/Dataset1/Tasks/2497.java
file://<WORKSPACE>/data/code-rep-dataset/Dataset1/Tasks/2497.java
### com.thoughtworks.qdox.parser.ParseException: syntax error @[1,1]

error in qdox parser
file content:
```java
offset: 1
uri: file://<WORKSPACE>/data/code-rep-dataset/Dataset1/Tasks/2497.java
text:
```scala
i@@f (timeSlice > 0)

/*

   Derby - Class org.apache.derby.impl.drda.ClientThread

   Licensed to the Apache Software Foundation (ASF) under one or more
   contributor license agreements.  See the NOTICE file distributed with
   this work for additional information regarding copyright ownership.
   The ASF licenses this file to You under the Apache License, Version 2.0
   (the "License"); you may not use this file except in compliance with
   the License.  You may obtain a copy of the License at

      http://www.apache.org/licenses/LICENSE-2.0

   Unless required by applicable law or agreed to in writing, software
   distributed under the License is distributed on an "AS IS" BASIS,
   WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
   See the License for the specific language governing permissions and
   limitations under the License.

 */

package org.apache.derby.impl.drda;

import java.io.*;
import java.net.*;
import java.security.*;

final class ClientThread extends Thread {

	NetworkServerControlImpl parent;
	ServerSocket serverSocket;
	private int timeSlice;
	private int connNum;
    
    ClientThread (NetworkServerControlImpl nsi, ServerSocket ss) {
        
        // Create a more meaningful name for this thread (but preserve its
        // thread id from the default name).
        NetworkServerControlImpl.setUniqueThreadName(this, "NetworkServerThread");
        
        parent=nsi;
        serverSocket=ss;
        timeSlice=nsi.getTimeSlice();
    }
	
    public void run() 
    {
        Socket clientSocket = null;
        
        for (;;) { // Nearly infinite loop. The loop is terminated if
                   // 1) We are shut down or 2) SSL won't work. In all
                   // other cases we just continue and try another
                   // accept on the socket.

            try { // Check for all other exceptions....

                try { // Check for underlying InterruptedException,
                      // SSLException and IOException

                    try{ // Check for PrivilegedActionException

                        clientSocket = 
                            (Socket) AccessController.doPrivileged(
                                 new PrivilegedExceptionAction() {
                                     public Object run() throws IOException
                                     {
                                         return serverSocket.accept();
                                     }
                                 }
                                 );

                        clientSocket.setKeepAlive(parent.getKeepAlive());
                        
                        // Set time out: Stops DDMReader.fill() from
                        // waiting indefinitely when timeSlice is set.
                        if (timeSlice != 0)
                            clientSocket.setSoTimeout(timeSlice);
                        
                        //create a new Session for this socket
                        parent.addSession(clientSocket);
                        
                    } catch (PrivilegedActionException e) {
                        // Just throw the underlying exception
                        throw e.getException();
                    } // end inner try/catch block
                    
                } catch (InterruptedException ie) {
                    // This is a shutdown and we'll just exit the
                    // thread. NOTE: This is according to the logic
                    // before this rewrite. I am not convinced that it
                    // is allways the case, but will not alter the
                    // behaviour since it is not within the scope of
                    // this change (DERBY-2108).
                    return;

                } catch (javax.net.ssl.SSLException ssle) {
                    // SSLException is a subclass of
                    // IOException. Print stack trace and...
                    
                    parent.consoleExceptionPrintTrace(ssle);
                    
                    // ... we need to do a controlled shutdown of the
                    // server, since SSL for some reason will not
                    // work.
                    
                    parent.directShutdown();
                    
                    return; // Exit the thread
                    
                } catch (IOException ioe) {
                    // IOException causes this thread to stop.  No
                    // console error message if this was caused by a
                    // shutdown
                    synchronized (parent.getShutdownSync()) {
                        if (!parent.getShutdown()) {
                            parent.consolePropertyMessage("DRDA_UnableToAccept.S");
                        }
                    }
                    return; // Exit the thread
                }
            } catch (Exception e) {
                // Catch and log all other exceptions
                
                parent.consoleExceptionPrintTrace(e);
            } // end outer try/catch block
            
        } // end for(;;)
        
    }// end run()
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

QDox parse error in file://<WORKSPACE>/data/code-rep-dataset/Dataset1/Tasks/2497.java