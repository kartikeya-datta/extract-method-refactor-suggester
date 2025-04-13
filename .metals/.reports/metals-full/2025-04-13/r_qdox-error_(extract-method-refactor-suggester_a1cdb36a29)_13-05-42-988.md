error id: file://<WORKSPACE>/data/code-rep-dataset/Dataset1/Tasks/3265.java
file://<WORKSPACE>/data/code-rep-dataset/Dataset1/Tasks/3265.java
### com.thoughtworks.qdox.parser.ParseException: syntax error @[1,1]

error in qdox parser
file content:
```java
offset: 1
uri: file://<WORKSPACE>/data/code-rep-dataset/Dataset1/Tasks/3265.java
text:
```scala
a@@ssert !StorageService.instance.isClientMode();

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

package org.apache.cassandra.gms;

import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.io.IOException;
import java.util.*;
import org.apache.cassandra.io.ICompactSerializer;
import org.apache.cassandra.service.StorageService;

import org.apache.log4j.Logger;

/**
 * This abstraction represents both the HeartBeatState and the ApplicationState in an EndPointState
 * instance. Any state for a given endpoint can be retrieved from this instance.
 */

public class EndPointState
{
    private static ICompactSerializer<EndPointState> serializer_;
    static
    {
        serializer_ = new EndPointStateSerializer();
    }
    
    HeartBeatState hbState_;
    Map<String, ApplicationState> applicationState_ = new Hashtable<String, ApplicationState>();
    
    /* fields below do not get serialized */
    long updateTimestamp_;
    boolean isAlive_;
    boolean isAGossiper_;

    public static ICompactSerializer<EndPointState> serializer()
    {
        return serializer_;
    }
    
    EndPointState(HeartBeatState hbState) 
    { 
        hbState_ = hbState; 
        updateTimestamp_ = System.currentTimeMillis(); 
        isAlive_ = true; 
        isAGossiper_ = false;
    }
        
    HeartBeatState getHeartBeatState()
    {
        return hbState_;
    }
    
    synchronized void setHeartBeatState(HeartBeatState hbState)
    {
        updateTimestamp();
        hbState_ = hbState;
    }
    
    public ApplicationState getApplicationState(String key)
    {
        return applicationState_.get(key);
    }
    
    public Map<String, ApplicationState> getApplicationStateMap()
    {
        return applicationState_;
    }
    
    void addApplicationState(String key, ApplicationState appState)
    {
        assert !StorageService.instance().isClientMode();
        applicationState_.put(key, appState);        
    }
    
    /* getters and setters */
    long getUpdateTimestamp()
    {
        return updateTimestamp_;
    }
    
    synchronized void updateTimestamp()
    {
        updateTimestamp_ = System.currentTimeMillis();
    }
    
    public boolean isAlive()
    {        
        return isAlive_;
    }

    synchronized void isAlive(boolean value)
    {        
        isAlive_ = value;        
    }

    
    boolean isAGossiper()
    {        
        return isAGossiper_;
    }

    synchronized void isAGossiper(boolean value)
    {                
        //isAlive_ = false;
        isAGossiper_ = value;        
    }

    public List<Map.Entry<String,ApplicationState>> getSortedApplicationStates()
    {
        ArrayList<Map.Entry<String, ApplicationState>> entries = new ArrayList<Map.Entry<String, ApplicationState>>();
        entries.addAll(applicationState_.entrySet());
        Collections.sort(entries, new Comparator<Map.Entry<String, ApplicationState>>()
        {
            public int compare(Map.Entry<String, ApplicationState> lhs, Map.Entry<String, ApplicationState> rhs)
            {
                return lhs.getValue().compareTo(rhs.getValue());
            }
        });

        return entries;
    }

}

class EndPointStateSerializer implements ICompactSerializer<EndPointState>
{
    private static Logger logger_ = Logger.getLogger(EndPointStateSerializer.class);
    
    public void serialize(EndPointState epState, DataOutputStream dos) throws IOException
    {
        /* These are for estimating whether we overshoot the MTU limit */
        int estimate = 0;

        /* serialize the HeartBeatState */
        HeartBeatState hbState = epState.getHeartBeatState();
        HeartBeatState.serializer().serialize(hbState, dos);

        /* serialize the map of ApplicationState objects */
        int size = epState.applicationState_.size();
        dos.writeInt(size);
        if ( size > 0 )
        {   
            Set<String> keys = epState.applicationState_.keySet();
            for( String key : keys )
            {
                if ( Gossiper.MAX_GOSSIP_PACKET_SIZE - dos.size() < estimate )
                {
                    logger_.info("@@@@ Breaking out to respect the MTU size in EndPointState serializer. Estimate is " + estimate + " @@@@");
                    break;
                }
            
                ApplicationState appState = epState.applicationState_.get(key);
                if ( appState != null )
                {
                    int pre = dos.size();
                    dos.writeUTF(key);
                    ApplicationState.serializer().serialize(appState, dos);                    
                    int post = dos.size();
                    estimate = post - pre;
                }                
            }
        }
    }

    public EndPointState deserialize(DataInputStream dis) throws IOException
    {
        HeartBeatState hbState = HeartBeatState.serializer().deserialize(dis);
        EndPointState epState = new EndPointState(hbState);               

        int appStateSize = dis.readInt();
        for ( int i = 0; i < appStateSize; ++i )
        {
            if ( dis.available() == 0 )
            {
                break;
            }
            
            String key = dis.readUTF();    
            ApplicationState appState = ApplicationState.serializer().deserialize(dis);            
            epState.addApplicationState(key, appState);            
        }
        return epState;
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

QDox parse error in file://<WORKSPACE>/data/code-rep-dataset/Dataset1/Tasks/3265.java