error id: file://<WORKSPACE>/data/code-rep-dataset/Dataset3/Tasks/12400.java
file://<WORKSPACE>/data/code-rep-dataset/Dataset3/Tasks/12400.java
### com.thoughtworks.qdox.parser.ParseException: syntax error @[1,18]

error in qdox parser
file content:
```java
offset: 18
uri: file://<WORKSPACE>/data/code-rep-dataset/Dataset3/Tasks/12400.java
text:
```scala
private volatile P@@olicyConfiguration policyConfiguration;

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

package org.jboss.as.security.service;

import java.security.Policy;

import javax.security.jacc.PolicyConfiguration;
import javax.security.jacc.PolicyConfigurationFactory;
import javax.security.jacc.PolicyContextException;

import org.jboss.as.security.SecurityExtension;
import org.jboss.logging.Logger;
import org.jboss.msc.inject.Injector;
import org.jboss.msc.service.Service;
import org.jboss.msc.service.ServiceName;
import org.jboss.msc.service.StartContext;
import org.jboss.msc.service.StartException;
import org.jboss.msc.service.StopContext;
import org.jboss.msc.value.InjectedValue;

/**
 * A service for JACC policies
 *
 * @author <a href="mailto:mmoyses@redhat.com">Marcus Moyses</a>
 */
public abstract class JaccService<T> implements Service<PolicyConfiguration> {

    protected static final Logger log = Logger.getLogger(JaccService.class);

    public static final ServiceName SERVICE_NAME = SecurityExtension.JBOSS_SECURITY.append("jacc");

    private final String contextId;

    private final T metaData;

    private final Boolean standalone;

    private PolicyConfiguration policyConfiguration;

    private final InjectedValue<PolicyConfiguration> parentPolicy = new InjectedValue<PolicyConfiguration>();

    public JaccService(final String contextId, T metaData, Boolean standalone) {
        if (contextId == null)
            throw new IllegalArgumentException("JACC Context Id passed is null");
        this.contextId = contextId;
        this.metaData = metaData;
        this.standalone = standalone;
    }

    /** {@inheritDoc} */
    @Override
    public PolicyConfiguration getValue() throws IllegalStateException, IllegalArgumentException {
        return policyConfiguration;
    }

    /** {@inheritDoc} */
    @Override
    public void start(StartContext context) throws StartException {
        try {
            PolicyConfigurationFactory pcf = PolicyConfigurationFactory.getPolicyConfigurationFactory();
            synchronized (pcf) { // synchronize on the factory
                policyConfiguration = pcf.getPolicyConfiguration(contextId, false);
                if (standalone) {
                    if (metaData != null) {
                        createPermissions(metaData, policyConfiguration);
                    } else {
                        log.debug("Cannot create permissions with 'null' metaData for id=" + contextId);
                    }
                }
                if (!standalone) {
                    PolicyConfiguration parent = parentPolicy.getValue();
                    if (parent != null) {
                        parent = pcf.getPolicyConfiguration(parent.getContextID(), false);
                        parent.linkConfiguration(policyConfiguration);
                        policyConfiguration.commit();
                        parent.commit();
                    } else {
                        log.debug("Could not retrieve parent policy for policy " + contextId);
                    }
                } else {
                    policyConfiguration.commit();
                }
                // Allow the policy to incorporate the policy configs
                Policy.getPolicy().refresh();
            }
        } catch (Exception e) {
            throw new StartException(e);
        }
    }

    /** {@inheritDoc} */
    @Override
    public void stop(StopContext context) {
        try {
            PolicyConfigurationFactory pcf = PolicyConfigurationFactory.getPolicyConfigurationFactory();
            synchronized (pcf) { // synchronize on the factory
                policyConfiguration = pcf.getPolicyConfiguration(contextId, false);
                policyConfiguration.delete();
            }
        } catch (Exception e) {
            log.warn("Error deleting JACC policy", e);
        }
        policyConfiguration = null;
    }

    /**
     * Target {@code Injector}
     *
     * @return target
     */
    public Injector<PolicyConfiguration> getParentPolicyInjector() {
        return parentPolicy;
    }

    /**
     * Create JACC permissions for the deployment
     *
     * @param metaData
     * @param policyConfiguration
     * @throws PolicyContextException
     */
    public abstract void createPermissions(T metaData, PolicyConfiguration policyConfiguration) throws PolicyContextException;

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

QDox parse error in file://<WORKSPACE>/data/code-rep-dataset/Dataset3/Tasks/12400.java