error id: file://<WORKSPACE>/data/code-rep-dataset/Dataset3/Tasks/7680.java
file://<WORKSPACE>/data/code-rep-dataset/Dataset3/Tasks/7680.java
### com.thoughtworks.qdox.parser.ParseException: syntax error @[1,1]

error in qdox parser
file content:
```java
offset: 1
uri: file://<WORKSPACE>/data/code-rep-dataset/Dataset3/Tasks/7680.java
text:
```scala
r@@eturn (Date)date.clone();

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
package org.jboss.as.controller.audit;

import java.net.InetAddress;
import java.util.Date;
import java.util.List;

import org.jboss.as.controller.OperationContext.ResultAction;
import org.jboss.as.controller.registry.Resource;
import org.jboss.as.core.security.AccessMechanism;
import org.jboss.dmr.ModelNode;

/**
 *
 * @author <a href="kabir.khan@jboss.com">Kabir Khan</a>
 */
abstract class AuditLogItem {

    private final Date date = new Date();
    private final String asVersion;
    protected final boolean readOnly;
    protected final boolean booting;
    protected final String userId;
    protected final String domainUUID;
    protected final AccessMechanism accessMechanism;
    protected final InetAddress remoteAddress;

    AuditLogItem(String asVersion, boolean readOnly, boolean booting, String userId, String domainUUID, AccessMechanism accessMechanism,
            InetAddress remoteAddress) {
        this.asVersion = asVersion;
        this.readOnly = readOnly;
        this.booting = booting;
        this.userId = userId;
        this.domainUUID = domainUUID;
        this.accessMechanism = accessMechanism;
        this.remoteAddress = remoteAddress;
    }

    static AuditLogItem createModelControllerItem(String asVersion, boolean readOnly, boolean booting, ResultAction resultAction, String userId,
                String domainUUID, AccessMechanism accessMechanism, InetAddress remoteAddress, Resource resultantModel,
                List<ModelNode> operations) {
        return new ModelControllerAuditLogItem(asVersion, readOnly, booting, resultAction, userId, domainUUID, accessMechanism, remoteAddress, resultantModel, operations);
    }

    static AuditLogItem createMethodAccessItem(String asVersion, boolean readOnly, boolean booting, String userId, String domainUUID,
                AccessMechanism accessMechanism, InetAddress remoteAddress, String methodName, String[] methodSignature,
                Object[] methodParams, Throwable error) {
        return new MethodAccessAuditLogItem(asVersion, readOnly, booting, userId, domainUUID, accessMechanism, remoteAddress, methodName, methodSignature, methodParams, error);
    }


    abstract String format(AuditLogItemFormatter formatter);

    /**
     * Get the asVersion
     * @return the asVersion
     */
    public String getAsVersion() {
        return asVersion;
    }

    /**
     * Get the readOnly
     * @return the readOnly
     */
    boolean isReadOnly() {
        return readOnly;
    }

    /**
     * Get the booting
     * @return the booting
     */
    boolean isBooting() {
        return booting;
    }

    /**
     * Get the userId
     * @return the userId
     */
    String getUserId() {
        return userId;
    }

    /**
     * Get the domainUUID
     * @return the domainUUID
     */
    String getDomainUUID() {
        return domainUUID;
    }

    /**
     * Get the accessMechanism
     * @return the accessMechanism
     */
    AccessMechanism getAccessMechanism() {
        return accessMechanism;
    }

    /**
     * Get the remoteAddress
     * @return the remoteAddress
     */
    InetAddress getRemoteAddress() {
        return remoteAddress;
    }

    /**
     * Get the date
     * @return the date
     */
    public Date getDate() {
        return date;
    }


    static class ModelControllerAuditLogItem extends AuditLogItem {
        private final ResultAction resultAction;
        private final Resource resultantModel;
        private final List<ModelNode> operations;

        ModelControllerAuditLogItem(String asVersion, boolean readOnly, boolean booting, ResultAction resultAction, String userId,
                String domainUUID, AccessMechanism accessMechanism, InetAddress remoteAddress, Resource resultantModel,
                List<ModelNode> operations) {
            super(asVersion, readOnly, booting, userId, domainUUID, accessMechanism, remoteAddress);
            this.resultAction = resultAction;
            this.resultantModel = resultantModel;
            this.operations = operations;
        }

        @Override
        String format(AuditLogItemFormatter formatter) {
            return formatter.formatAuditLogItem(this);
        }

        /**
         * Get the resultAction
         * @return the resultAction
         */
        ResultAction getResultAction() {
            return resultAction;
        }

        /**
         * Get the resultantModel
         * @return the resultantModel
         */
        Resource getResultantModel() {
            return resultantModel;
        }

        /**
         * Get the operations
         * @return the operations
         */
        List<ModelNode> getOperations() {
            return operations;
        }
    }

    static class MethodAccessAuditLogItem extends AuditLogItem {
        private final String methodName;
        private final String[] methodSignature;
        private final Object[] methodParams;
        private final Throwable error;

        MethodAccessAuditLogItem(String asVersion, boolean readOnly, boolean booting, String userId, String domainUUID,
                AccessMechanism accessMechanism, InetAddress remoteAddress, String methodName, String[] methodSignature,
                Object[] methodParams, Throwable error) {
            super(asVersion, readOnly, booting, userId, domainUUID, accessMechanism, remoteAddress);
            this.methodName = methodName;
            this.methodSignature = methodSignature;
            this.methodParams = methodParams;
            this.error = error;
        }

        @Override
        String format(AuditLogItemFormatter formatter) {
            return formatter.formatAuditLogItem(this);
        }

        /**
         * Get the methodName
         * @return the methodName
         */
        String getMethodName() {
            return methodName;
        }

        /**
         * Get the methodSignature
         * @return the methodSignature
         */
        String[] getMethodSignature() {
            return methodSignature;
        }

        /**
         * Get the methodParams
         * @return the methodParams
         */
        Object[] getMethodParams() {
            return methodParams;
        }

        /**
         * Get the error
         * @return the error
         */
        Throwable getError() {
            return error;
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

QDox parse error in file://<WORKSPACE>/data/code-rep-dataset/Dataset3/Tasks/7680.java