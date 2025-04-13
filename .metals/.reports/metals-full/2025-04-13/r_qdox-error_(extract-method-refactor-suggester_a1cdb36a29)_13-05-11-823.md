error id: file://<WORKSPACE>/data/code-rep-dataset/Dataset3/Tasks/978.java
file://<WORKSPACE>/data/code-rep-dataset/Dataset3/Tasks/978.java
### com.thoughtworks.qdox.parser.ParseException: syntax error @[1,1]

error in qdox parser
file content:
```java
offset: 1
uri: file://<WORKSPACE>/data/code-rep-dataset/Dataset3/Tasks/978.java
text:
```scala
i@@f (topology_conf.containsKey(Config.TOPOLOGY_GROUPS)) {

/**
 * Licensed to the Apache Software Foundation (ASF) under one
 * or more contributor license agreements.  See the NOTICE file
 * distributed with this work for additional information
 * regarding copyright ownership.  The ASF licenses this file
 * to you under the Apache License, Version 2.0 (the
 * "License"); you may not use this file except in compliance
 * with the License.  You may obtain a copy of the License at
 *
 * http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */

package backtype.storm.security.auth.authorizer;

import java.util.Arrays;
import java.util.Map;
import java.util.Set;
import java.util.HashSet;
import java.util.Collection;
import java.io.IOException;

import backtype.storm.Config;
import backtype.storm.security.auth.IAuthorizer;
import backtype.storm.security.auth.ReqContext;
import backtype.storm.security.auth.AuthUtils;
import backtype.storm.security.auth.IPrincipalToLocal;
import backtype.storm.security.auth.IGroupMappingServiceProvider;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * An authorization implementation that simply checks if a user is allowed to perform specific
 * operations.
 */
public class SimpleACLAuthorizer implements IAuthorizer {
    private static final Logger LOG = LoggerFactory.getLogger(SimpleACLAuthorizer.class);

    protected Set<String> _userCommands = new HashSet<String>(Arrays.asList("submitTopology", "fileUpload", "getNimbusConf", "getClusterInfo"));
    protected Set<String> _supervisorCommands = new HashSet<String>(Arrays.asList("fileDownload"));
    protected Set<String> _topoCommands = new HashSet<String>(Arrays.asList("killTopology","rebalance","activate","deactivate","getTopologyConf","getTopology","getUserTopology","getTopologyInfo","uploadNewCredentials"));

    protected Set<String> _admins;
    protected Set<String> _supervisors;
    protected IPrincipalToLocal _ptol;
    protected IGroupMappingServiceProvider _groupMappingProvider;
    /**
     * Invoked once immediately after construction
     * @param conf Storm configuration
     */
    @Override
    public void prepare(Map conf) {
        _admins = new HashSet<String>();
        _supervisors = new HashSet<String>();

        if (conf.containsKey(Config.NIMBUS_ADMINS)) {
            _admins.addAll((Collection<String>)conf.get(Config.NIMBUS_ADMINS));
        }
        if (conf.containsKey(Config.NIMBUS_SUPERVISOR_USERS)) {
            _supervisors.addAll((Collection<String>)conf.get(Config.NIMBUS_SUPERVISOR_USERS));
        }

        _ptol = AuthUtils.GetPrincipalToLocalPlugin(conf);
        _groupMappingProvider = AuthUtils.GetGroupMappingServiceProviderPlugin(conf);
    }

    /**
     * permit() method is invoked for each incoming Thrift request
     * @param context request context includes info about
     * @param operation operation name
     * @param topology_storm configuration of targeted topology
     * @return true if the request is authorized, false if reject
     */
    @Override
    public boolean permit(ReqContext context, String operation, Map topology_conf) {
        LOG.info("[req "+ context.requestID()+ "] Access "
                 + " from: " + (context.remoteAddress() == null? "null" : context.remoteAddress().toString())
                 + (context.principal() == null? "" : (" principal:"+ context.principal()))
                 +" op:"+operation
                 + (topology_conf == null? "" : (" topoology:"+topology_conf.get(Config.TOPOLOGY_NAME))));

        String principal = context.principal().getName();
        String user = _ptol.toLocal(context.principal());
        if (_admins.contains(principal) || _admins.contains(user)) {
            return true;
        }

        if (_supervisors.contains(principal) || _supervisors.contains(user)) {
            return _supervisorCommands.contains(operation);
        }

        if (_userCommands.contains(operation)) {
            return true;
        }

        if (_topoCommands.contains(operation)) {
            Set topoUsers = new HashSet<String>();
            if (topology_conf.containsKey(Config.TOPOLOGY_USERS)) {
                topoUsers.addAll((Collection<String>)topology_conf.get(Config.TOPOLOGY_USERS));
            }

            if (topoUsers.contains(principal) || topoUsers.contains(user)) {
                return true;
            }

            Set<String> topoGroups = new HashSet<String>();
            if (topology_conf.containsKey(Config.TOPOLOGY_GROUPS) && topology_conf.get(Config.TOPOLOGY_GROUPS) != null) {
                topoGroups.addAll((Collection<String>)topology_conf.get(Config.TOPOLOGY_GROUPS));
            }

            if(_groupMappingProvider != null && topoGroups.size() > 0) {
                try {
                    Set<String> userGroups = _groupMappingProvider.getGroups(user);
                    for (String tgroup : topoGroups) {
                        if(userGroups.contains(tgroup))
                            return true;
                    }
                } catch(IOException e) {
                    LOG.warn("Error while trying to fetch user groups",e);
                }
            }
        }
        return false;
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

QDox parse error in file://<WORKSPACE>/data/code-rep-dataset/Dataset3/Tasks/978.java