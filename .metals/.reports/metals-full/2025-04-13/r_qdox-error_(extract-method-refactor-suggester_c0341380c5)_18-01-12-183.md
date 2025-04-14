error id: file://<WORKSPACE>/data/code-rep-dataset/Dataset5/Tasks/1684.java
file://<WORKSPACE>/data/code-rep-dataset/Dataset5/Tasks/1684.java
### com.thoughtworks.qdox.parser.ParseException: syntax error @[1,1]

error in qdox parser
file content:
```java
offset: 1
uri: file://<WORKSPACE>/data/code-rep-dataset/Dataset5/Tasks/1684.java
text:
```scala
i@@f (!jpa.getName().equals(conf.getSpecificationInstance().getName()))

/*
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
 * Unless required by applicable law or agreed to in writing,
 * software distributed under the License is distributed on an
 * "AS IS" BASIS, WITHOUT WARRANTIES OR CONDITIONS OF ANY
 * KIND, either express or implied.  See the License for the
 * specific language governing permissions and limitations
 * under the License.    
 */
package org.apache.openjpa.persistence.jdbc;

import java.security.AccessController;
import java.util.Map;

import org.apache.openjpa.conf.OpenJPAConfiguration;
import org.apache.openjpa.conf.OpenJPAProductDerivation;
import org.apache.openjpa.conf.Specification;
import org.apache.openjpa.jdbc.conf.JDBCConfigurationImpl;
import org.apache.openjpa.jdbc.kernel.JDBCStoreManager;
import org.apache.openjpa.lib.conf.AbstractProductDerivation;
import org.apache.openjpa.lib.conf.Configuration;
import org.apache.openjpa.lib.util.J2DoPrivHelper;
import org.apache.openjpa.persistence.FetchPlan;
import org.apache.openjpa.persistence.PersistenceProductDerivation;

/**
 * Sets JDBC-specific JPA specification defaults.
 *
 * @author Abe White
 * @nojavadoc
 */
public class JDBCPersistenceProductDerivation 
    extends AbstractProductDerivation 
    implements OpenJPAProductDerivation {
    
    public void putBrokerFactoryAliases(Map m) {
    }

    public int getType() {
        return TYPE_SPEC_STORE;
    }

    @Override
    public void validate()
        throws Exception {
        // make sure JPA is available
        AccessController.doPrivileged(J2DoPrivHelper.getClassLoaderAction(
            javax.persistence.EntityManagerFactory.class));
    }

    @Override
    public boolean beforeConfigurationLoad(Configuration c) {
        if (c instanceof OpenJPAConfiguration) {
            ((OpenJPAConfiguration) c).getStoreFacadeTypeRegistry().
                registerImplementation(FetchPlan.class, JDBCStoreManager.class, 
                JDBCFetchPlanImpl.class);
        }
        if (!(c instanceof JDBCConfigurationImpl))
            return false;

        JDBCConfigurationImpl conf = (JDBCConfigurationImpl) c;
        Specification jpa = PersistenceProductDerivation.SPEC_JPA;
        Specification ejb = PersistenceProductDerivation.ALIAS_EJB;

        conf.metaFactoryPlugin.setAlias(ejb.getName(),
            PersistenceMappingFactory.class.getName());
        conf.metaFactoryPlugin.setAlias(jpa.getName(),
            PersistenceMappingFactory.class.getName());

        conf.mappingFactoryPlugin.setAlias(ejb.getName(),
            PersistenceMappingFactory.class.getName());
        conf.mappingFactoryPlugin.setAlias(jpa.getName(),
            PersistenceMappingFactory.class.getName());

        conf.mappingDefaultsPlugin.setAlias(ejb.getName(),
            PersistenceMappingDefaults.class.getName());
        conf.mappingDefaultsPlugin.setAlias(jpa.getName(),
            PersistenceMappingDefaults.class.getName());
        return true;
    }

    @Override
    public boolean afterSpecificationSet(Configuration c) {
        if (!(c instanceof JDBCConfigurationImpl))
            return false;
        JDBCConfigurationImpl conf = (JDBCConfigurationImpl) c;
        Specification jpa = PersistenceProductDerivation.SPEC_JPA;
        if (!jpa.equals(conf.getSpecificationInstance()))
            return false;
        
        conf.mappingDefaultsPlugin.setDefault(jpa.getName());
        conf.mappingDefaultsPlugin.setString(jpa.getName());
        return true;
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

QDox parse error in file://<WORKSPACE>/data/code-rep-dataset/Dataset5/Tasks/1684.java