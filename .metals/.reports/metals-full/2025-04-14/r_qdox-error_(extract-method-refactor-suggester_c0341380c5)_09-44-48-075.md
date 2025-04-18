error id: file://<WORKSPACE>/data/code-rep-dataset/Dataset3/Tasks/15043.java
file://<WORKSPACE>/data/code-rep-dataset/Dataset3/Tasks/15043.java
### com.thoughtworks.qdox.parser.ParseException: syntax error @[1,1]

error in qdox parser
file content:
```java
offset: 1
uri: file://<WORKSPACE>/data/code-rep-dataset/Dataset3/Tasks/15043.java
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
package org.jboss.as.cmp.jdbc.bridge;

import java.lang.reflect.Method;
import java.util.Collection;
import java.util.HashSet;
import java.util.Set;
import javax.ejb.EJBException;
import javax.ejb.FinderException;
import javax.ejb.ObjectNotFoundException;
import javax.transaction.Transaction;
import javax.transaction.TransactionManager;
import org.jboss.as.cmp.bridge.SelectorBridge;
import org.jboss.as.cmp.component.CmpEntityBeanComponent;
import org.jboss.as.cmp.context.CmpEntityBeanContext;
import org.jboss.as.cmp.jdbc.JDBCQueryCommand;
import org.jboss.as.cmp.jdbc.JDBCStoreManager;
import org.jboss.as.cmp.jdbc.metadata.JDBCQueryMetaData;

/**
 * JDBCSelectorBridge represents one ejbSelect method.
 * <p/>
 * Life-cycle:
 * Tied to the EntityBridge.
 * <p/>
 * Multiplicity:
 * One for each entity bean ejbSelect method.
 *
 * @author <a href="mailto:dain@daingroup.com">Dain Sundstrom</a>
 * @version $Revision: 81030 $
 */
public class JDBCSelectorBridge implements SelectorBridge {
    private final JDBCQueryMetaData queryMetaData;
    private final JDBCStoreManager manager;
    private TransactionManager tm;
    private boolean syncBeforeSelect;

    public JDBCSelectorBridge(JDBCStoreManager manager, JDBCQueryMetaData queryMetaData) {
        this.queryMetaData = queryMetaData;
        this.manager = manager;

        CmpEntityBeanComponent component = manager.getComponent();
        tm = component.getTransactionManager();
        syncBeforeSelect = !manager.getCmpConfig().isSyncOnCommitOnly();
    }

    // BridgeInvoker implementation

    public Object invoke(CmpEntityBeanContext ctx, Method method, Object[] args) throws Exception {
        Transaction tx = (ctx != null ? ctx.getTransaction() : tm.getTransaction());

        if (syncBeforeSelect) {
            manager.getComponent().synchronizeEntitiesWithinTransaction(tx);
        }

        return execute(ctx, args);
    }

    // SelectorBridge implementation

    public String getSelectorName() {
        return queryMetaData.getMethod().getName();
    }

    public Method getMethod() {
        return queryMetaData.getMethod();
    }

    private Class getReturnType() {
        return queryMetaData.getMethod().getReturnType();
    }

    public Object execute(CmpEntityBeanContext ctx, Object[] args) throws FinderException {
        Collection retVal;
        Method method = getMethod();
        try {
            JDBCQueryCommand query = manager.getQueryManager().getQueryCommand(method);
            final CmpEntityBeanComponent selectedComponent = query.getSelectManager().getComponent();
            JDBCQueryCommand.EntityProxyFactory factory = new JDBCQueryCommand.EntityProxyFactory() {
                public Object getEntityObject(Object primaryKey) {
                    return queryMetaData.isResultTypeMappingLocal() && selectedComponent.getLocalHomeClass() != null ?
                            selectedComponent.getEjbLocalObject(primaryKey) : selectedComponent.getEJBObject(primaryKey);
                }
            };
            retVal = query.execute(method, args, null, factory);
        } catch (FinderException e) {
            throw e;
        } catch (EJBException e) {
            throw e;
        } catch (Exception e) {
            throw new EJBException("Error in " + getSelectorName(), e);
        }

        if (!Collection.class.isAssignableFrom(getReturnType())) {
            // single object
            if (retVal.size() == 0) {
                throw new ObjectNotFoundException();
            }
            if (retVal.size() > 1) {
                throw new FinderException(getSelectorName() +
                        " returned " + retVal.size() + " objects");
            }

            Object o = retVal.iterator().next();
            if (o == null && method.getReturnType().isPrimitive()) {
                throw new FinderException(
                        "Cannot return null as a value of primitive type " + method.getReturnType().getName()
                );
            }

            return o;
        } else {
            // collection or set
            if (Set.class.isAssignableFrom(getReturnType())) {
                return new HashSet(retVal);
            } else {
                return retVal;
            }
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

QDox parse error in file://<WORKSPACE>/data/code-rep-dataset/Dataset3/Tasks/15043.java