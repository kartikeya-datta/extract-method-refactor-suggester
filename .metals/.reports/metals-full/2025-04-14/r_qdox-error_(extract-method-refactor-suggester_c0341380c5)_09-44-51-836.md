error id: file://<WORKSPACE>/data/code-rep-dataset/Dataset3/Tasks/1246.java
file://<WORKSPACE>/data/code-rep-dataset/Dataset3/Tasks/1246.java
### com.thoughtworks.qdox.parser.ParseException: syntax error @[1,1]

error in qdox parser
file content:
```java
offset: 1
uri: file://<WORKSPACE>/data/code-rep-dataset/Dataset3/Tasks/1246.java
text:
```scala
B@@eanPopulator.getNamedInstance(ObjectStoreEnvironmentBean.class, "default");

/*
 * JBoss, Home of Professional Open Source.
 * Copyright 2012, Red Hat, Inc., and individual contributors
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

/**
 *
 */
package org.jboss.as.txn.service;

import org.jboss.as.controller.services.path.PathManager;
import org.jboss.msc.service.Service;
import org.jboss.msc.service.StartContext;
import org.jboss.msc.service.StartException;
import org.jboss.msc.service.StopContext;
import org.jboss.msc.value.InjectedValue;

import com.arjuna.ats.arjuna.common.ObjectStoreEnvironmentBean;
import com.arjuna.ats.internal.arjuna.objectstore.hornetq.HornetqJournalEnvironmentBean;
import com.arjuna.common.internal.util.propertyservice.BeanPopulator;

/**
 * Configures the {@link ObjectStoreEnvironmentBean}s using an injected path.
 *
 * @author Brian Stansberry (c) 2011 Red Hat Inc.
 */
public class ArjunaObjectStoreEnvironmentService implements Service<Void> {

    private final InjectedValue<PathManager> pathManagerInjector = new InjectedValue<PathManager>();
    private final boolean useHornetqJournalStore;
    private final boolean enableAsyncIO;
    private final String path;
    private final String pathRef;

    private final boolean useJdbcStore;
    private final String dataSourceJndiName;
    private final JdbcStoreConfig jdbcSoreConfig;

    private volatile PathManager.Callback.Handle callbackHandle;

    public ArjunaObjectStoreEnvironmentService(final boolean useHornetqJournalStore, final boolean enableAsyncIO, final String path, final String pathRef, final boolean useJdbcStore, final String dataSourceJndiName, final JdbcStoreConfig jdbcSoreConfig) {
        this.useHornetqJournalStore = useHornetqJournalStore;
        this.enableAsyncIO = enableAsyncIO;
        this.path = path;
        this.pathRef = pathRef;
        this.useJdbcStore = useJdbcStore;
        this.dataSourceJndiName = dataSourceJndiName;
        this.jdbcSoreConfig = jdbcSoreConfig;
    }

    @Override
    public Void getValue() throws IllegalStateException, IllegalArgumentException {
        return null;
    }

    @Override
    public void start(StartContext context) throws StartException {
        callbackHandle = pathManagerInjector.getValue().registerCallback(pathRef, PathManager.ReloadServerCallback.create(), PathManager.Event.UPDATED, PathManager.Event.REMOVED);
        String objectStoreDir = pathManagerInjector.getValue().resolveRelativePathEntry(path, pathRef);

         final ObjectStoreEnvironmentBean defaultActionStoreObjectStoreEnvironmentBean =
           BeanPopulator.getNamedInstance(ObjectStoreEnvironmentBean.class, null);


        if(useHornetqJournalStore) {
            HornetqJournalEnvironmentBean hornetqJournalEnvironmentBean = BeanPopulator.getDefaultInstance(
                    com.arjuna.ats.internal.arjuna.objectstore.hornetq.HornetqJournalEnvironmentBean.class
            );
            hornetqJournalEnvironmentBean.setAsyncIO(enableAsyncIO);
            hornetqJournalEnvironmentBean.setStoreDir(objectStoreDir+"/HornetqObjectStore");
            defaultActionStoreObjectStoreEnvironmentBean.setObjectStoreType(
                    "com.arjuna.ats.internal.arjuna.objectstore.hornetq.HornetqObjectStoreAdaptor"
            );
        } else {
            defaultActionStoreObjectStoreEnvironmentBean.setObjectStoreDir(objectStoreDir);
        }

        final ObjectStoreEnvironmentBean stateStoreObjectStoreEnvironmentBean =
            BeanPopulator.getNamedInstance(ObjectStoreEnvironmentBean.class, "stateStore");
        stateStoreObjectStoreEnvironmentBean.setObjectStoreDir(objectStoreDir);
        final ObjectStoreEnvironmentBean communicationStoreObjectStoreEnvironmentBean =
            BeanPopulator.getNamedInstance(ObjectStoreEnvironmentBean.class, "communicationStore");
        communicationStoreObjectStoreEnvironmentBean.setObjectStoreDir(objectStoreDir);

        if(useJdbcStore) {
            defaultActionStoreObjectStoreEnvironmentBean.setObjectStoreType("com.arjuna.ats.internal.arjuna.objectstore.jdbc.JDBCStore");
            stateStoreObjectStoreEnvironmentBean.setObjectStoreType("com.arjuna.ats.internal.arjuna.objectstore.jdbc.JDBCStore");
            communicationStoreObjectStoreEnvironmentBean.setObjectStoreType("com.arjuna.ats.internal.arjuna.objectstore.jdbc.JDBCStore");

            defaultActionStoreObjectStoreEnvironmentBean.setJdbcAccess("com.arjuna.ats.internal.arjuna.objectstore.jdbc.accessors.DataSourceJDBCAccess;datasourceName=" + dataSourceJndiName);
            stateStoreObjectStoreEnvironmentBean.setJdbcAccess("com.arjuna.ats.internal.arjuna.objectstore.jdbc.accessors.DataSourceJDBCAccess;datasourceName=" + dataSourceJndiName);
            communicationStoreObjectStoreEnvironmentBean.setJdbcAccess("com.arjuna.ats.internal.arjuna.objectstore.jdbc.accessors.DataSourceJDBCAccess;datasourceName=" + dataSourceJndiName);


            defaultActionStoreObjectStoreEnvironmentBean.setTablePrefix(jdbcSoreConfig.getActionTablePrefix());
            stateStoreObjectStoreEnvironmentBean.setTablePrefix(jdbcSoreConfig.getStateTablePrefix());
            communicationStoreObjectStoreEnvironmentBean.setTablePrefix(jdbcSoreConfig.getCommunicationTablePrefix());


            defaultActionStoreObjectStoreEnvironmentBean.setDropTable(jdbcSoreConfig.isActionDropTable());
            stateStoreObjectStoreEnvironmentBean.setDropTable(jdbcSoreConfig.isStateDropTable());
            communicationStoreObjectStoreEnvironmentBean.setDropTable(jdbcSoreConfig.isCommunicationDropTable());

        }

    }


    @Override
    public void stop(StopContext context) {
        callbackHandle.remove();
    }

    public InjectedValue<PathManager> getPathManagerInjector() {
        return pathManagerInjector;
    }

    public static final class JdbcStoreConfig {
        private final String actionTablePrefix;
        private final boolean actionDropTable;
        private final String stateTablePrefix;
        private final boolean stateDropTable;
        private final String communicationTablePrefix;
        private final boolean communicationDropTable;

        private JdbcStoreConfig(final String actionTablePrefix, final boolean actionDropTable, final String stateTablePrefix, final boolean stateDropTable, final String communicationTablePrefix, final boolean communicationDropTable) {
            this.actionTablePrefix = actionTablePrefix;
            this.actionDropTable = actionDropTable;
            this.stateTablePrefix = stateTablePrefix;
            this.stateDropTable = stateDropTable;
            this.communicationTablePrefix = communicationTablePrefix;
            this.communicationDropTable = communicationDropTable;
        }

        public String getActionTablePrefix() {
            return actionTablePrefix;
        }

        public boolean isActionDropTable() {
            return actionDropTable;
        }

        public String getStateTablePrefix() {
            return stateTablePrefix;
        }

        public boolean isStateDropTable() {
            return stateDropTable;
        }

        public String getCommunicationTablePrefix() {
            return communicationTablePrefix;
        }

        public boolean isCommunicationDropTable() {
            return communicationDropTable;
        }
    }

    public static final class JdbcStoreConfigBulder {
            private String actionTablePrefix;
            private boolean actionDropTable;
            private String stateTablePrefix;
            private boolean stateDropTable;
            private String communicationTablePrefix;
            private boolean communicationDropTable;

        public JdbcStoreConfigBulder setActionTablePrefix(String actionTablePrefix) {
            this.actionTablePrefix = actionTablePrefix;
            return this;
        }

        public JdbcStoreConfigBulder setActionDropTable(boolean actionDropTable) {
            this.actionDropTable = actionDropTable;
            return this;
        }

        public JdbcStoreConfigBulder setStateTablePrefix(String stateTablePrefix) {
            this.stateTablePrefix = stateTablePrefix;
            return this;
        }

        public JdbcStoreConfigBulder setStateDropTable(boolean stateDropTable) {
            this.stateDropTable = stateDropTable;
            return this;
        }

        public JdbcStoreConfigBulder setCommunicationTablePrefix(String communicationTablePrefix) {
            this.communicationTablePrefix = communicationTablePrefix;
            return this;
        }

        public JdbcStoreConfigBulder setCommunicationDropTable(boolean communicationDropTable) {
            this.communicationDropTable = communicationDropTable;
            return this;
        }

        public JdbcStoreConfig build() {
            return new JdbcStoreConfig(actionTablePrefix, actionDropTable, stateTablePrefix, stateDropTable, communicationTablePrefix, communicationDropTable);
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

QDox parse error in file://<WORKSPACE>/data/code-rep-dataset/Dataset3/Tasks/1246.java