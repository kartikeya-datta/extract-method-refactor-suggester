error id: file://<WORKSPACE>/data/code-rep-dataset/Dataset3/Tasks/14649.java
file://<WORKSPACE>/data/code-rep-dataset/Dataset3/Tasks/14649.java
### com.thoughtworks.qdox.parser.ParseException: syntax error @[1,1]

error in qdox parser
file content:
```java
offset: 1
uri: file://<WORKSPACE>/data/code-rep-dataset/Dataset3/Tasks/14649.java
text:
```scala
i@@f(cache.containsNotRemoved(context.getParameters()[0])) {

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

package org.jboss.as.cmp.component.interceptors;

import static org.jboss.as.cmp.CmpMessages.MESSAGES;

import java.lang.reflect.Method;
import java.util.Collection;
import java.util.Enumeration;
import java.util.Iterator;

import javax.ejb.ObjectNotFoundException;

import org.jboss.as.cmp.component.CmpEntityBeanComponent;
import org.jboss.as.cmp.component.CmpEntityBeanComponentInstance;
import org.jboss.as.cmp.context.CmpEntityBeanContext;
import org.jboss.as.cmp.jdbc.JDBCEntityPersistenceStore;
import org.jboss.as.cmp.jdbc.JDBCQueryCommand;
import org.jboss.as.ejb3.component.entity.EntityBeanComponent;
import org.jboss.as.ejb3.component.entity.EntityBeanComponentInstance;
import org.jboss.as.ejb3.component.entity.entitycache.ReadyEntityCache;
import org.jboss.as.ejb3.component.entity.interceptors.EntityBeanHomeFinderInterceptorFactory;
import org.jboss.invocation.InterceptorContext;

/**
 * @author John Bailey
 */
public class CmpEntityBeanHomeFinderInterceptorFactory extends EntityBeanHomeFinderInterceptorFactory {
    private final Method finderMethod;
    private final boolean localHome;

    public CmpEntityBeanHomeFinderInterceptorFactory(final Method finderMethod, final boolean localHome) {
        super(finderMethod);
        this.finderMethod = finderMethod;
        this.localHome = localHome;
    }


    protected Object invokeFind(final InterceptorContext context, final EntityBeanComponentInstance instance) throws Exception {
        final CmpEntityBeanComponentInstance cmpInstance = CmpEntityBeanComponentInstance.class.cast(instance);
        final CmpEntityBeanComponent cmpComponent = cmpInstance.getComponent();
        try {
            cmpComponent.getComponentClass().getDeclaredMethod(finderMethod.getName(), finderMethod.getParameterTypes());
            return super.invokeFind(context, instance);
        } catch (NoSuchMethodException ignored) {
        }

        final JDBCEntityPersistenceStore store = cmpComponent.getStoreManager();

        final CmpEntityBeanContext entityContext = cmpInstance.getEjbContext();

        // as per the spec 9.6.4, entities must be synchronized with the datastore when an ejbFind<METHOD> is called.
        if (finderMethod.getName().equals("findByPrimaryKey")) {
            if(finderMethod.getParameterTypes() == null) {
                throw MESSAGES.nullArgumentForFindByPrimaryKey();
            }
            if(finderMethod.getParameterTypes().length != 1) {
                throw MESSAGES.illegalNumberOfArgumentsForFindByPrimaryKey(finderMethod.getParameterTypes().length);
            }
            final ReadyEntityCache cache = cmpComponent.getCache();
            if(cache.isCached(context.getParameters()[0])) {
                return this.localHome ? cmpComponent.getEJBLocalObject(context.getParameters()[0]) : cmpComponent.getEJBObject(context.getParameters()[0]);
            }
        }else if(!store.getCmpConfig().isSyncOnCommitOnly()) {
            cmpComponent.synchronizeEntitiesWithinTransaction(entityContext.getTransaction());
        }

        final JDBCQueryCommand.EntityProxyFactory factory = new JDBCQueryCommand.EntityProxyFactory() {
            public Object getEntityObject(final Object primaryKey) {
                return localHome ? cmpComponent.getEJBLocalObject(primaryKey) : cmpComponent.getEJBObject(primaryKey);
            }
        };

        if (getReturnType() == ReturnType.SINGLE) {
            return store.findEntity(context.getMethod(), context.getParameters(), entityContext, factory);
        } else {
            return store.findEntities(context.getMethod(), context.getParameters(), entityContext, factory);
        }
    }

    protected Object prepareResults(final InterceptorContext context, final Object result, final EntityBeanComponent component) throws ObjectNotFoundException {
        switch (getReturnType()) {
            case COLLECTION: {
                return result;
            }
            case ENUMERATION: {
                Collection<Object> entities = (Collection<Object>) result;
                final Iterator<Object> iterator = entities.iterator();
                return new Enumeration<Object>() {
                    public boolean hasMoreElements() {
                        return iterator.hasNext();
                    }

                    public Object nextElement() {
                        return iterator.next();
                    }
                };
            }
            default: {
                return result;
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

QDox parse error in file://<WORKSPACE>/data/code-rep-dataset/Dataset3/Tasks/14649.java