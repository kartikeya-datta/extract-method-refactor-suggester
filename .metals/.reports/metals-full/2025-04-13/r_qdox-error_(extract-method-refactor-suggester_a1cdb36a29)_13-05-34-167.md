error id: file://<WORKSPACE>/data/code-rep-dataset/Dataset1/Tasks/2210.java
file://<WORKSPACE>/data/code-rep-dataset/Dataset1/Tasks/2210.java
### com.thoughtworks.qdox.parser.ParseException: syntax error @[1,1]

error in qdox parser
file content:
```java
offset: 1
uri: file://<WORKSPACE>/data/code-rep-dataset/Dataset1/Tasks/2210.java
text:
```scala
c@@atch (Throwable e)

/**
 * Licensed to the Apache Software Foundation (ASF) under one
 * or more contributor license agreements.  See the NOTICE file
 * distributed with this work for additional information
 * regarding copyright ownership.  The ASF licenses this file
 * to you under the Apache License, Version 2.0 (the
 * "License"); you may not use this file except in compliance
 * with the License.  You may obtain a copy of the License at
 *
 *     http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */

package org.apache.cassandra.cli;

import jline.ConsoleReader;
import jline.History;
import org.apache.cassandra.auth.SimpleAuthenticator;
import org.apache.cassandra.thrift.*;
import org.apache.thrift.TException;
import org.apache.thrift.protocol.TBinaryProtocol;
import org.apache.thrift.transport.TFramedTransport;
import org.apache.thrift.transport.TSocket;
import org.apache.thrift.transport.TTransport;

import java.io.File;
import java.io.IOException;
import java.util.Arrays;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Map;
import java.util.Set;

/**
 * Cassandra Command Line Interface (CLI) Main
 */
public class CliMain
{
    public final static String HISTORYFILE = ".cassandra.history";

    private static TTransport transport_ = null;
    private static Cassandra.Client thriftClient_ = null;
    private static CliSessionState css_ = new CliSessionState();
    private static CliClient cliClient_;
    private static CliCompleter completer_ = new CliCompleter();

    /**
     * Establish a thrift connection to cassandra instance
     *
     * @param server - hostname or IP of the server
     * @param port   - Thrift port number
     */
    public static void connect(String server, int port)
    {

        TSocket socket = new TSocket(server, port);

        if (transport_ != null)
            transport_.close();

        if (css_.framed)
        {
            transport_ = new TFramedTransport(socket);
        }
        else 
        {
            transport_ = socket;
        }

        TBinaryProtocol binaryProtocol = new TBinaryProtocol(transport_, true, true);
        Cassandra.Client cassandraClient = new Cassandra.Client(binaryProtocol);

        try
        {
            transport_.open();
        }
        catch (Exception e)
        {
            // Should move this to Log4J as well probably...
            css_.err.format("Exception connecting to %s/%d - %s\n", server, port, e.getMessage());

            if (css_.debug)
                e.printStackTrace();

            return;
        }

        thriftClient_ = cassandraClient;
        cliClient_ = new CliClient(css_, thriftClient_);
        
        if (css_.keyspace != null)
        {
            try {
                thriftClient_.set_keyspace(css_.keyspace);
                cliClient_.setKeyspace(css_.keyspace);

                Set<String> cfnames = new HashSet<String>();
                KsDef ksd = cliClient_.getKSMetaData(css_.keyspace);
                for (CfDef cfd : ksd.cf_defs) {
                    cfnames.add(cfd.name);
                }
                updateCompletor(cfnames);
                
            }
            catch (InvalidRequestException e)
            {
                css_.err.println("Keyspace " + css_.keyspace + " not found");
                return;
            }
            catch (TException e)
            {
                css_.err.println("Did you specify 'keyspace'?");
                return;
            }
            catch (NotFoundException e)
            {
                css_.err.println("Keyspace " + css_.keyspace + " not found");
                return;
            }
        }
        
        if ((css_.username != null) && (css_.password != null))
        {
            // Authenticate 
            Map<String, String> credentials = new HashMap<String, String>();
            credentials.put(SimpleAuthenticator.USERNAME_KEY, css_.username);
            credentials.put(SimpleAuthenticator.PASSWORD_KEY, css_.password);
            AuthenticationRequest authRequest = new AuthenticationRequest(credentials);
            try 
            {
                thriftClient_.login(authRequest);
                cliClient_.setUsername(css_.username);
            } 
            catch (AuthenticationException e) 
            {
                css_.err.println("Exception during authentication to the cassandra node, " +
                		"Verify the keyspace exists, and that you are using the correct credentials.");
                return;
            } 
            catch (AuthorizationException e) 
            {
                css_.err.println("You are not authorized to use keyspace: " + css_.keyspace);
                return;
            }
            catch (TException e) 
            {
                css_.err.println("Login failure. Did you specify 'keyspace', 'username' and 'password'?");
                return;
            } 
        }
        
        // Lookup the cluster name, this is to make it clear which cluster the user is connected to
        String clusterName;

        try
        {
            clusterName = thriftClient_.describe_cluster_name();
        }
        catch (Exception e)
        {

            css_.err.println("Exception retrieving information about the cassandra node, check you have connected to the thrift port.");

            if (css_.debug)
                e.printStackTrace();

            return;
        }

        css_.out.printf("Connected to: \"%s\" on %s/%d%n", clusterName, server, port);
    }

    /**
     * Disconnect thrift connection to cassandra instance
     */
    public static void disconnect()
    {
        if (transport_ != null)
        {
            transport_.close();
            transport_ = null;
        }
    }

    private static void printBanner()
    {
        css_.out.println("Welcome to cassandra CLI.\n");
        css_.out.println("Type 'help' or '?' for help. Type 'quit' or 'exit' to quit.");
    }

    /**
     * Checks whether the thrift client is connected.
     */
    public static boolean isConnected()
    {
        if (thriftClient_ == null)
        {
            css_.out.println("Not connected to a cassandra instance.");
            return false;
        }
        return true;
    }
    
    public static void updateCompletor(Set<String> candidates)
    {
        Set<String> actions = new HashSet<String>();
        for (String cf : candidates)
        {
            for (String cmd : completer_.getKeyspaceCommands())
                actions.add(String.format("%s %s", cmd, cf));
        }
        
        String[] strs = Arrays.copyOf(actions.toArray(), actions.toArray().length, String[].class);
        
        completer_.setCandidateStrings(strs);
    }

    private static void processCLIStmt(String query)
    {
        try
        {
            cliClient_.executeCLIStmt(query);
        }
        catch (InvalidRequestException ire)
        {
            css_.err.println(ire.why);
            if (css_.debug)
                ire.printStackTrace(css_.err);
            
            // Abort a batch run when errors are encountered
            if (css_.batch)
                System.exit(4);
        }
        catch (Exception e)
        {
            css_.err.println((e.getCause() == null) ? e.getMessage() : e.getCause().getMessage());
            
            if (css_.debug)
                e.printStackTrace(css_.err);
            
            // Abort a batch run when errors are encountered
            if (css_.batch)
                System.exit(8);
        }
    }

    public static void main(String args[]) throws IOException
    {
        // process command line args
        CliOptions cliOptions = new CliOptions();
        cliOptions.processArgs(css_, args);

        // connect to cassandra server if host argument specified.
        if (css_.hostName != null)
        {
            connect(css_.hostName, css_.thriftPort);
        }
        
        if ( cliClient_ == null )
        {
            // Connection parameter was either invalid or not present.
            // User must connect explicitly using the "connect" CLI statement.
            cliClient_ = new CliClient(css_, null);
        }

        ConsoleReader reader = new ConsoleReader();
        
        if (!css_.batch)
        {
            reader.addCompletor(completer_);
            reader.setBellEnabled(false);
            
            String historyFile = System.getProperty("user.home") + File.separator + HISTORYFILE;

            try
            {
                History history = new History(new File(historyFile));
                reader.setHistory(history);
            }
            catch (IOException exp)
            {
                css_.err.printf("Unable to open %s for writing %n", historyFile);
            }
        }
        else
        {
            css_.out.close();
        }

        printBanner();

        String line;
        while ((line = reader.readLine("[" + cliClient_.getUsername() + "@" + cliClient_.getKeySpace() + "] ")) != null)
        {
            processCLIStmt(line);
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

QDox parse error in file://<WORKSPACE>/data/code-rep-dataset/Dataset1/Tasks/2210.java