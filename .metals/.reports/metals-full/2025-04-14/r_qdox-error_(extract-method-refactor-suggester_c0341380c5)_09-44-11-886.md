error id: file://<WORKSPACE>/data/code-rep-dataset/Dataset3/Tasks/4006.java
file://<WORKSPACE>/data/code-rep-dataset/Dataset3/Tasks/4006.java
### com.thoughtworks.qdox.parser.ParseException: syntax error @[1,1]

error in qdox parser
file content:
```java
offset: 1
uri: file://<WORKSPACE>/data/code-rep-dataset/Dataset3/Tasks/4006.java
text:
```scala
t@@hrow new FinderException("Error invoking custom finder " +

/*
 * JBoss, Home of Professional Open Source.
 * Copyright 2011, Red Hat, Inc., and individual contributors
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
package org.jboss.as.cmp.jdbc;

import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.util.ArrayList;
import java.util.Collection;
import java.util.Collections;
import java.util.Enumeration;
import java.util.List;
import javax.ejb.FinderException;
import org.jboss.as.cmp.context.CmpEntityBeanContext;
import org.jboss.as.cmp.jdbc.metadata.JDBCReadAheadMetaData;
import org.jboss.as.ejb3.component.entity.EntityBeanComponentInstance;
import org.jboss.logging.Logger;

/**
 * CMPStoreManager CustomFindByEntitiesCommand.
 * Implements bridge for custom implemented finders in container managed entity
 * beans. These methods are called ejbFindX in the EJB implementation class,
 * where X can be anything. Such methods are called findX in the Home and/or
 * the LocalHome interface.
 *
 * @author <a href="mailto:michel.anke@wolmail.nl">Michel de Groot</a>
 * @author <a href="mailto:john-jboss@freeborg.com">John Freeborg</a>
 * @version $Revision: 81030 $
 * @see org.jboss.as.cmp.jdbc.JDBCFindEntitiesCommand
 */
public final class JDBCCustomFinderQuery implements JDBCQueryCommand {
    private final Logger log;
    private final Method finderMethod;
    private final JDBCReadAheadMetaData readAheadMetaData;
    private final ReadAheadCache readAheadCache;
    private final JDBCStoreManager manager;

    /**
     * Constructs a command which can handle multiple entity finders
     * that are BMP implemented.
     *
     * @param finderMethod the EJB finder method implementation
     */
    public JDBCCustomFinderQuery(JDBCStoreManager manager, Method finderMethod) {
        this.finderMethod = finderMethod;
        this.manager = manager;

        JDBCReadAheadMetaData readAheadMetaData = manager.getMetaData().getReadAhead();
        if ((readAheadMetaData != null) && readAheadMetaData.isOnLoad()) {
            this.readAheadCache = manager.getReadAheadCache();
            this.readAheadMetaData = readAheadMetaData;
        } else {
            this.readAheadCache = null;
            this.readAheadMetaData = null;
        }

        this.log = Logger.getLogger(
                this.getClass().getName() +
                        "." +
                        manager.getMetaData().getName() +
                        "." +
                        finderMethod.getName());

        if (log.isDebugEnabled())
            log.debug("Finder: Custom finder " + finderMethod.getName());
    }

    public JDBCStoreManager getSelectManager() {
        return manager;
    }

    public Collection execute(Method unused, Object[] args, CmpEntityBeanContext ctx, EntityProxyFactory factory) throws FinderException {
        try {
            // invoke implementation method on ejb instance
            Object value;
            final EntityBeanComponentInstance componentInstance = ctx.getComponent().getCache().get(ctx.getPrimaryKey());
            try {
                value = finderMethod.invoke(componentInstance, args);
            } finally {
                ctx.getComponent().getCache().release(componentInstance, true);
            }

            // if expected return type is Collection, return as is
            // if expected return type is not Collection, wrap value in Collection
            if (value instanceof Enumeration) {
                Enumeration enumeration = (Enumeration) value;
                List result = new ArrayList();
                while (enumeration.hasMoreElements()) {
                    result.add(enumeration.nextElement());
                }
                cacheResults(result);
                return EntityProxyFactory.Util.getEntityCollection(factory, result);
            } else if (value instanceof Collection) {
                List result;
                if (value instanceof List)
                    result = (List) value;
                else
                    result = new ArrayList((Collection) value);
                cacheResults(result);
                return EntityProxyFactory.Util.getEntityCollection(factory, result);
            } else {
                // Don't bother trying to cache this
                return Collections.singleton(value != null ? factory.getEntityObject(value) : null);
            }
        } catch (IllegalAccessException e) {
            log.error("Error invoking custom finder " + finderMethod.getName(), e);
            throw new FinderException("Unable to access finder implementation: " +
                    finderMethod.getName());
        } catch (IllegalArgumentException e) {
            log.error("Error invoking custom finder " + finderMethod.getName(), e);
            throw new FinderException("Illegal arguments for finder " +
                    "implementation: " + finderMethod.getName());
        } catch (InvocationTargetException e) {
            // Throw the exception if its a FinderException
            Throwable ex = e.getTargetException();
            if (ex instanceof FinderException) {
                throw (FinderException) ex;
            } else {
                throw new FinderException("Errror invoking cutom finder " +
                        finderMethod.getName() + ": " + ex);
            }
        }
    }

    private void cacheResults(List listOfPKs) {
        // the on-load read ahead cache strategy is the only one that makes
        // sense to support for custom finders since all we have is a list of
        // primary keys.
        if (readAheadCache != null) {
            readAheadCache.addFinderResults(listOfPKs, readAheadMetaData);
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

QDox parse error in file://<WORKSPACE>/data/code-rep-dataset/Dataset3/Tasks/4006.java