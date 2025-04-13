error id: file://<WORKSPACE>/data/code-rep-dataset/Dataset3/Tasks/15045.java
file://<WORKSPACE>/data/code-rep-dataset/Dataset3/Tasks/15045.java
### com.thoughtworks.qdox.parser.ParseException: syntax error @[1,1]

error in qdox parser
file content:
```java
offset: 1
uri: file://<WORKSPACE>/data/code-rep-dataset/Dataset3/Tasks/15045.java
text:
```scala
s@@electedComponent.getEJBLocalObject(primaryKey) : selectedComponent.getEJBObject(primaryKey);

/*
 * JBoss, Home of Professional Open Source.
 * Copyright 2008, Red Hat Middleware LLC, and individual contributors
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
package org.jboss.as.cmp.jdbc2.bridge;

import java.lang.reflect.Method;
import java.util.Collection;
import javax.ejb.FinderException;
import javax.transaction.Transaction;
import javax.transaction.TransactionManager;
import org.jboss.as.cmp.bridge.SelectorBridge;
import org.jboss.as.cmp.component.CmpEntityBeanComponent;
import org.jboss.as.cmp.context.CmpEntityBeanContext;
import org.jboss.as.cmp.jdbc.JDBCQueryCommand;
import org.jboss.as.cmp.jdbc.metadata.JDBCQueryMetaData;
import org.jboss.as.cmp.jdbc2.JDBCStoreManager2;
import org.jboss.as.cmp.jdbc2.QueryCommand;
import org.jboss.as.cmp.jdbc2.schema.Schema;

/**
 * @author <a href="mailto:alex@jboss.org">Alexey Loubyansky</a>
 * @version <tt>$Revision: 81030 $</tt>
 */
public class EJBSelectBridge implements SelectorBridge {
    private static final byte SINGLE = 0;
    private static final byte COLLECTION = 2;

    private final JDBCQueryMetaData metadata;
    private final QueryCommand command;
    private final byte returnType;
    private final Schema schema;
    private boolean syncBeforeSelect;
    private TransactionManager tm;

    public EJBSelectBridge(final JDBCStoreManager2 manager, CmpEntityBeanComponent component, Schema schema, JDBCQueryMetaData metadata, QueryCommand command) {
        this.schema = schema;
        this.metadata = metadata;
        this.command = command;

        Class type = metadata.getMethod().getReturnType();
        if (Collection.class.isAssignableFrom(type)) {
            returnType = COLLECTION;
        } else {
            returnType = SINGLE;
        }

        tm = component.getTransactionManager();
        syncBeforeSelect = !manager.getCmpConfig().isSyncOnCommitOnly();
    }

    // BridgeInvoker implementation

    public Object invoke(CmpEntityBeanContext instance, Method method, Object[] args)
            throws Exception {
        Transaction tx = (instance != null ? instance.getTransaction() : tm.getTransaction());

        if (syncBeforeSelect) {
            instance.getComponent().synchronizeEntitiesWithinTransaction(tx);
        }

        return execute(instance, args);
    }

    // SelectorBridge implementation

    public String getSelectorName() {
        return metadata.getMethod().getName();
    }

    public Method getMethod() {
        return metadata.getMethod();
    }

    public Object execute(CmpEntityBeanContext ctx, Object[] args) throws FinderException {
        JDBCStoreManager2 manager = command.getStoreManager();
        final CmpEntityBeanComponent selectedComponent = manager.getComponent();
        JDBCQueryCommand.EntityProxyFactory factory = new JDBCQueryCommand.EntityProxyFactory() {
            public Object getEntityObject(Object primaryKey) {
                return metadata.isResultTypeMappingLocal() && selectedComponent.getLocalHomeClass() != null ?
                        selectedComponent.getEjbLocalObject(primaryKey) : selectedComponent.getEJBObject(primaryKey);
            }
        };
        Object result;
        switch (returnType) {
            case SINGLE:
                result = command.fetchOne(schema, args, factory);
                if (result == null && getMethod().getReturnType().isPrimitive()) {
                    throw new FinderException(
                            "Cannot return null as a value of primitive type " + getMethod().getReturnType().getName()
                    );
                }
                break;
            case COLLECTION:
                result = command.fetchCollection(schema, args, factory);
                break;
            default:
                throw new IllegalStateException("Unexpected return type: " + returnType);
        }
        return result;
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

QDox parse error in file://<WORKSPACE>/data/code-rep-dataset/Dataset3/Tasks/15045.java