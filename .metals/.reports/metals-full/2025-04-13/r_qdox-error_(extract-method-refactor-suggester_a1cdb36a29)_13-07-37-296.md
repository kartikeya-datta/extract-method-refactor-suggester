error id: file://<WORKSPACE>/data/code-rep-dataset/Dataset3/Tasks/10072.java
file://<WORKSPACE>/data/code-rep-dataset/Dataset3/Tasks/10072.java
### com.thoughtworks.qdox.parser.ParseException: syntax error @[1,7]

error in qdox parser
file content:
```java
offset: 7
uri: file://<WORKSPACE>/data/code-rep-dataset/Dataset3/Tasks/10072.java
text:
```scala
final C@@mpEntityBeanContext ctx = instance.getEjbContext();

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

import javax.ejb.EJBException;
import org.jboss.as.cmp.component.CmpEntityBeanComponentInstance;
import org.jboss.as.cmp.context.CmpEntityBeanContext;
import org.jboss.as.cmp.jdbc.bridge.CMRMessage;
import org.jboss.as.cmp.jdbc.bridge.JDBCCMRFieldBridge;
import org.jboss.as.cmp.jdbc.bridge.JDBCEntityBridge;
import org.jboss.as.ee.component.ComponentInstance;
import org.jboss.invocation.Interceptor;
import org.jboss.invocation.InterceptorContext;
import org.jboss.invocation.InterceptorFactory;
import org.jboss.invocation.InterceptorFactoryContext;
import org.jboss.logging.Logger;

/**
 * @author John Bailey
 */
public class CmpEntityBeanJdbcRelationshipInterceptor implements Interceptor {
    private static final Logger log = Logger.getLogger(CmpEntityBeanJdbcRelationshipInterceptor.class);
    private static final CmpEntityBeanJdbcRelationshipInterceptor INSTANCE = new CmpEntityBeanJdbcRelationshipInterceptor();

    public Object processInvocation(InterceptorContext context) throws Exception {
        CMRMessage relationshipMessage = context.getPrivateData(CMRMessage.class);
        if (relationshipMessage == null) {
            // Not a relationship message. Invoke down the chain
            return context.proceed();
        }
        final CmpEntityBeanComponentInstance instance = (CmpEntityBeanComponentInstance) context.getPrivateData(ComponentInstance.class);
        final CmpEntityBeanContext ctx = instance.getEntityContext();

        // We are going to work with the context a lot
        JDBCCMRFieldBridge cmrField = (JDBCCMRFieldBridge) context.getParameters()[0];

        if (CMRMessage.GET_RELATED_ID == relationshipMessage) {
            // call getRelateId
            if (log.isTraceEnabled()) {
                log.trace("Getting related id: field=" + cmrField.getFieldName() + " id=" + ctx.getPrimaryKey());
            }
            return cmrField.getRelatedId(ctx);

        } else if (CMRMessage.ADD_RELATION == relationshipMessage) {
            // call addRelation
            Object relatedId = context.getParameters()[1];
            if (log.isTraceEnabled()) {
                log.trace("Add relation: field=" + cmrField.getFieldName() +
                        " id=" + ctx.getPrimaryKey() +
                        " relatedId=" + relatedId);
            }

            cmrField.addRelation(ctx, relatedId);

            return null;

        } else if (CMRMessage.REMOVE_RELATION == relationshipMessage) {
            // call removeRelation
            Object relatedId = context.getParameters()[1];
            if (log.isTraceEnabled()) {
                log.trace("Remove relation: field=" + cmrField.getFieldName() +
                        " id=" + ctx.getPrimaryKey() +
                        " relatedId=" + relatedId);
            }

            cmrField.removeRelation(ctx, relatedId);

            return null;
        } else if (CMRMessage.SCHEDULE_FOR_CASCADE_DELETE == relationshipMessage) {
            JDBCEntityBridge entity = (JDBCEntityBridge) cmrField.getEntity();
            entity.scheduleForCascadeDelete(ctx);
            return null;
        } else if (CMRMessage.SCHEDULE_FOR_BATCH_CASCADE_DELETE == relationshipMessage) {
            JDBCEntityBridge entity = (JDBCEntityBridge) cmrField.getEntity();
            entity.scheduleForBatchCascadeDelete(ctx);
            return null;
        } else {
            // this should not be possible we are using a type safe enum
            throw new EJBException("Unknown cmp2.0-relationship-message=" + relationshipMessage);
        }
    }

    public static InterceptorFactory FACTORY = new InterceptorFactory() {
        public Interceptor create(InterceptorFactoryContext context) {
            return INSTANCE;
        }
    };
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

QDox parse error in file://<WORKSPACE>/data/code-rep-dataset/Dataset3/Tasks/10072.java