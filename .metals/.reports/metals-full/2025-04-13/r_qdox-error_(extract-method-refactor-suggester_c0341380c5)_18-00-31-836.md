error id: file://<WORKSPACE>/data/code-rep-dataset/Dataset5/Tasks/1511.java
file://<WORKSPACE>/data/code-rep-dataset/Dataset5/Tasks/1511.java
### com.thoughtworks.qdox.parser.ParseException: syntax error @[1,26]

error in qdox parser
file content:
```java
offset: 26
uri: file://<WORKSPACE>/data/code-rep-dataset/Dataset5/Tasks/1511.java
text:
```scala
transient private static L@@ogger log = Hierarchy.getDefaultHierarchy().getLoggerFor(

package org.apache.jmeter.protocol.http.proxy;
/******************************************************************
*** File Admin.java
***
***/

import java.io.BufferedReader;
import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.ServerSocket;
import java.net.Socket;

import org.apache.log.Hierarchy;
import org.apache.log.Logger;

//
// Class:     Admin
// Abstract:  The admin thread listens on admin socket and handle all
//            communications with the remote administrator.
//

public class Admin extends Thread
{
	
	private static Logger log = Hierarchy.getDefaultHierarchy().getLoggerFor(
			"jmeter.protocol.http");
    //
    // Member variables
    //

    ServerSocket adminSocket = null;
    Socket appletSocket = null;
    String passwordCandidate = null;
    BufferedReader in = null;
    DataOutputStream out = null;
    Config config = null;
    Cache cache;


    //
    // Public methods
    //

    //
    // Constructor
    //
    Admin(Config configObject, Cache cacheManager)
    {
        try
        {
            config = configObject;
            cache = cacheManager;
            adminSocket = new ServerSocket(0);
            config.setAdminPort(adminSocket.getLocalPort());
        }
        catch (IOException e)
        {
            log.error("Error opening admin socket",e);
        }
    }


    //
    // Handle communications with remote administrator
    //
    public void run()
    {
        while(true)
        {
            try
            {
                appletSocket = adminSocket.accept();
                in = new BufferedReader(new InputStreamReader(new DataInputStream(appletSocket.getInputStream())));
                out = new DataOutputStream(appletSocket.getOutputStream());

                do
                {
                    // Read password candidate sent by applet
                    String passwordCandidate = in.readLine();

                    // Send applet ack/nack on password
                    if (config.getPassword().equals(passwordCandidate))
                    {
                        out.writeBytes("ACCEPT\n");
                        break;
                    }
                    else
                    {
                        out.writeBytes("REJECT\n");
                    }
                    out.flush();
                }
                while (true);

                //
                // Password is OK, so let's send the administrator the
                // parameters values and read his new values
                //
                while(true)
                {
                    out.writeBytes(config.toString());
                    out.flush();

                    config.parse(in.readLine());
                    log.info("Configuration changed by administrator.");

                    // Administrator wants to clean the cache
                    if (config.getCleanCache())
                    {
                        cache.clean();
                        config.setCleanCache(false); //no need to clean again
                    }
                }
            }
            catch (Exception e)
            {
                //
                // This line was reached because the administrator closed
                // the connection with the proxy. That's fine, we are now
                // available for another administrator to log in.
                //
                log.error("Connection with administrator closed.",e);
            }
            finally
            {
                try
                {
                    out.close();
                    in.close();
                }
                catch(Exception exc)
                {}
            }
        }//while
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

QDox parse error in file://<WORKSPACE>/data/code-rep-dataset/Dataset5/Tasks/1511.java