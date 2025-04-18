error id: file://<WORKSPACE>/data/code-rep-dataset/Dataset3/Tasks/14026.java
file://<WORKSPACE>/data/code-rep-dataset/Dataset3/Tasks/14026.java
### com.thoughtworks.qdox.parser.ParseException: syntax error @[1,18]

error in qdox parser
file content:
```java
offset: 18
uri: file://<WORKSPACE>/data/code-rep-dataset/Dataset3/Tasks/14026.java
text:
```scala
private volatile P@@OA poa;

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

package org.jboss.as.jacorb.service;

import org.jboss.logging.Logger;
import org.jboss.msc.inject.Injector;
import org.jboss.msc.service.Service;
import org.jboss.msc.service.ServiceName;
import org.jboss.msc.service.StartContext;
import org.jboss.msc.service.StartException;
import org.jboss.msc.service.StopContext;
import org.jboss.msc.value.InjectedValue;
import org.omg.CORBA.ORB;
import org.omg.CORBA.Policy;
import org.omg.PortableServer.IdAssignmentPolicyValue;
import org.omg.PortableServer.IdUniquenessPolicyValue;
import org.omg.PortableServer.ImplicitActivationPolicyValue;
import org.omg.PortableServer.LifespanPolicyValue;
import org.omg.PortableServer.POA;
import org.omg.PortableServer.POAHelper;
import org.omg.PortableServer.RequestProcessingPolicyValue;
import org.omg.PortableServer.ServantRetentionPolicyValue;
import org.omg.PortableServer.ThreadPolicyValue;

import java.util.ArrayList;
import java.util.List;

/**
 * <p>
 * This class implements a service that creates and activates {@code org.omg.PortableServer.POA} objects.
 * </p>
 *
 * @author <a href="mailto:sguilhen@redhat.com">Stefan Guilhen</a>
 */
public class CorbaPOAService implements Service<POA> {

    private static final Logger log = Logger.getLogger("org.jboss.as.jacorb");

    public static final ServiceName SERVICE_NAME = ServiceName.JBOSS.append("jacorb", "poa-service");

    private POA poa;

    private final InjectedValue<ORB> orbInjector = new InjectedValue<ORB>();

    private final InjectedValue<POA> parentPOAInjector = new InjectedValue<POA>();

    private final String poaName;

    private final String bindingName;

    // the policy values that can be assigned to created POAs.

    private final IdAssignmentPolicyValue idAssignmentPolicyValue;

    private final IdUniquenessPolicyValue idUniquenessPolicyValue;

    private final ImplicitActivationPolicyValue implicitActivationPolicyValue;

    private final LifespanPolicyValue lifespanPolicyValue;

    private final RequestProcessingPolicyValue requestProcessingPolicyValue;

    private final ServantRetentionPolicyValue servantRetentionPolicyValue;

    private final ThreadPolicyValue threadPolicyValue;

    /**
     * <p>
     * Creates a {@code CorbaPOAService} with the specified POA name and binding name. The {@code POA} created by this
     * service will not be associated with any policies.
     * </p>
     *
     * @param poaName     the name of the {@code POA} that will be created by this service (ex. "RootPOA").
     * @param bindingName the JNDI context name where the created {@code POA} will be bound. If null, the JNDI binding
     *                    won't be performed.
     */
    public CorbaPOAService(String poaName, String bindingName) {
        this(poaName, bindingName, null, null, null, null, null, null, null);
    }

    /**
     * <p>
     * Creates a {@code CorbaPOAService} with the specified POA name, binding name and policy values.
     * </p>
     *
     * @param poaName                       the name of the {@code POA} that will be created by this service (ex. "RootPOA").
     * @param bindingName                   the JNDI context name where the created {@code POA} will be bound. If null, the JNDI binding
     *                                      won't be performed.
     * @param idAssignmentPolicyValue       the {@code IdAssignmentPolicyValue} that will be associated with the created
     *                                      {@code POA}. Can be null.
     * @param idUniquenessPolicyValue       the {@code IdUniquenessPolicyValue} that will be associated with the created
     *                                      {@code POA}. Can be null.
     * @param implicitActivationPolicyValue the {@code ImplicitActivationPolicyValue} that will be associated with the
     *                                      created {@code POA}. Can be null.
     * @param lifespanPolicyValue           the {@code LifespanPolicyValue} that will be associated with the created {@code POA}.
     *                                      Can be null.
     * @param requestProcessingPolicyValue  the {@code RequestProcessingPolicyValue} that will be associated with the
     *                                      created {@code POA}. Can be null.
     * @param servantRetentionPolicyValue   the {@code ServantRetentionPolicyValue} that will be associated with the created
     *                                      {@code POA}. Can be null.
     * @param threadPolicyValue             the {@code ThreadPolicyValue} that will be associated with the created {@code POA}. Can
     *                                      be null.
     */
    public CorbaPOAService(String poaName, String bindingName, IdAssignmentPolicyValue idAssignmentPolicyValue,
                           IdUniquenessPolicyValue idUniquenessPolicyValue, ImplicitActivationPolicyValue implicitActivationPolicyValue,
                           LifespanPolicyValue lifespanPolicyValue, RequestProcessingPolicyValue requestProcessingPolicyValue,
                           ServantRetentionPolicyValue servantRetentionPolicyValue, ThreadPolicyValue threadPolicyValue) {
        this.poaName = poaName;
        this.bindingName = bindingName;
        this.idAssignmentPolicyValue = idAssignmentPolicyValue;
        this.idUniquenessPolicyValue = idUniquenessPolicyValue;
        this.implicitActivationPolicyValue = implicitActivationPolicyValue;
        this.lifespanPolicyValue = lifespanPolicyValue;
        this.requestProcessingPolicyValue = requestProcessingPolicyValue;
        this.servantRetentionPolicyValue = servantRetentionPolicyValue;
        this.threadPolicyValue = threadPolicyValue;
    }


    @Override
    public void start(StartContext context) throws StartException {
        log.debugf("Starting Service " + context.getController().getName().getCanonicalName());

        ORB orb = this.orbInjector.getOptionalValue();
        POA parentPOA = this.parentPOAInjector.getOptionalValue();

        // if an ORB has been injected, we will use the ORB.resolve_initial_references method to instantiate the POA.
        if (orb != null) {
            try {
                this.poa = POAHelper.narrow(orb.resolve_initial_references(this.poaName));
            } catch (Exception e) {
                throw new StartException("Failed to resolve initial reference " + this.poaName, e);
            }
        }
        // if a parent POA has been injected, we use it to create the policies and then the POA itself.
        else if (parentPOA != null) {
            try {
                Policy[] poaPolicies = this.createPolicies(parentPOA);
                this.poa = parentPOA.create_POA(this.poaName, null, poaPolicies);
            } catch (Exception e) {
                throw new StartException("Failed to create POA from parent POA", e);
            }
        } else {
            throw new StartException("Unable to instantiate POA: either the running ORB or the parent POA must be specified");
        }

        // check if the POA should be bound to JNDI under java:/jboss.
        if (this.bindingName != null) {
            CorbaServiceUtil.bindObject(context.getChildTarget(), this.bindingName, this.poa);
        }

        // activate the created POA.
        try {
            this.poa.the_POAManager().activate();
        } catch (Exception e) {
            throw new StartException("Failed to activate POA", e);
        }
    }

    @Override
    public void stop(StopContext context) {
        log.debugf("Stopping Service " + context.getController().getName().getCanonicalName());
        // destroy the created POA.
        this.poa.destroy(false, false);
    }

    @Override
    public POA getValue() throws IllegalStateException, IllegalArgumentException {
        return this.poa;
    }

    public Injector<ORB> getORBInjector() {
        return this.orbInjector;
    }

    public Injector<POA> getParentPOAInjector() {
        return this.parentPOAInjector;
    }

    /**
     * <p>
     * Create the {@code Policy} array containing the {@code POA} policies using the values specified in the constructor.
     * When creating a {@code POA}, the parent {@code POA} is responsible for generating the relevant policies beforehand.
     * </p>
     *
     * @param poa the {@code POA} used to create the {@code Policy} objects.
     * @return the constructed {@code Policy} array.
     */
    private Policy[] createPolicies(POA poa) {
        List<Policy> policies = new ArrayList<Policy>();

        if (this.idAssignmentPolicyValue != null)
            policies.add(poa.create_id_assignment_policy(this.idAssignmentPolicyValue));
        if (this.idUniquenessPolicyValue != null)
            policies.add(poa.create_id_uniqueness_policy(this.idUniquenessPolicyValue));
        if (this.implicitActivationPolicyValue != null)
            policies.add(poa.create_implicit_activation_policy(this.implicitActivationPolicyValue));
        if (this.lifespanPolicyValue != null)
            policies.add(poa.create_lifespan_policy(this.lifespanPolicyValue));
        if (this.requestProcessingPolicyValue != null)
            policies.add(poa.create_request_processing_policy(this.requestProcessingPolicyValue));
        if (this.servantRetentionPolicyValue != null)
            policies.add(poa.create_servant_retention_policy(this.servantRetentionPolicyValue));
        if (this.threadPolicyValue != null)
            policies.add(poa.create_thread_policy(this.threadPolicyValue));

        return policies.toArray(new Policy[policies.size()]);
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
	java.base/java.util.concurrent.ForkJoinPool.helpJoin(ForkJoinPool.java:1883)
	java.base/java.util.concurrent.ForkJoinTask.awaitDone(ForkJoinTask.java:440)
	java.base/java.util.concurrent.ForkJoinTask.join(ForkJoinTask.java:670)
	scala.collection.parallel.ForkJoinTasks$FJTWrappedTask.sync(Tasks.scala:243)
	scala.collection.parallel.ForkJoinTasks$FJTWrappedTask.sync$(Tasks.scala:243)
	scala.collection.parallel.AdaptiveWorkStealingForkJoinTasks$AWSFJTWrappedTask.sync(Tasks.scala:304)
	scala.collection.parallel.AdaptiveWorkStealingTasks$AWSTWrappedTask.internal(Tasks.scala:173)
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

QDox parse error in file://<WORKSPACE>/data/code-rep-dataset/Dataset3/Tasks/14026.java