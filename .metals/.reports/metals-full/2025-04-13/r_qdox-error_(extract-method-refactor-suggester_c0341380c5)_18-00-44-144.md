error id: file://<WORKSPACE>/data/code-rep-dataset/Dataset3/Tasks/12871.java
file://<WORKSPACE>/data/code-rep-dataset/Dataset3/Tasks/12871.java
### com.thoughtworks.qdox.parser.ParseException: syntax error @[1,15]

error in qdox parser
file content:
```java
offset: 15
uri: file://<WORKSPACE>/data/code-rep-dataset/Dataset3/Tasks/12871.java
text:
```scala
private final M@@ap<String, ApplicationException> applicationExceptions = new ConcurrentHashMap<String, ApplicationException>();

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

package org.jboss.as.ejb3.deployment;

import org.jboss.as.ee.component.ComponentDescription;
import org.jboss.as.ee.component.EEApplicationClasses;
import org.jboss.as.ee.component.EEModuleDescription;
import org.jboss.as.ejb3.component.messagedriven.MessageDrivenComponentDescription;
import org.jboss.as.ejb3.component.session.SessionBeanComponentDescription;
import org.jboss.as.server.deployment.DeploymentPhaseContext;
import org.jboss.as.server.deployment.DeploymentUnit;
import org.jboss.as.server.deployment.DeploymentUnitProcessingException;
import org.jboss.modules.Module;

import javax.ejb.ApplicationException;
import java.lang.annotation.Annotation;
import java.util.ArrayList;
import java.util.Collection;
import java.util.Collections;
import java.util.HashSet;
import java.util.Map;
import java.util.Set;
import java.util.concurrent.ConcurrentHashMap;

/**
 * User: jpai
 */
public class EjbJarDescription {

    private final EEModuleDescription eeModuleDescription;

    private final Map<String, ApplicationException> applicationExceptions = new ConcurrentHashMap();

    private final Set<String> applicationLevelSecurityRoles = new HashSet<String>();

    private final EEApplicationClasses applicationClassesDescription;

    /**
     * True if this represents EJB's packaged in a war
     */
    private final boolean war;

    public EjbJarDescription(EEModuleDescription eeModuleDescription, final EEApplicationClasses applicationClassesDescription, boolean war) {
        this.war = war;
        if (eeModuleDescription == null) {
            throw new IllegalArgumentException(EEModuleDescription.class.getSimpleName() + " cannot be null");
        }
        this.eeModuleDescription = eeModuleDescription;
        this.applicationClassesDescription = applicationClassesDescription;
    }

    public void addSecurityRole(final String role) {
        if (role == null || role.trim().isEmpty()) {
            throw new IllegalArgumentException("Security role name cannot be null or empty: " + role);
        }
        this.applicationLevelSecurityRoles.add(role);
    }

    /**
     * Returns the security roles that have been defined at the application level (via security-role elements in the
     * ejb-jar.xml). Note that this set does *not* include the roles that have been defined at each individual component
     * level (via @DeclareRoles, @RolesAllowed annotations or security-role-ref element)
     * <p/>
     * Returns an empty set if no roles have been defined at the application level.
     *
     * @return
     */
    public Set<String> getSecurityRoles() {
        return Collections.unmodifiableSet(this.applicationLevelSecurityRoles);
    }

    public void addApplicationException(final String exceptionClassName, final boolean rollback, final boolean inherited) {
        if (exceptionClassName == null || exceptionClassName.isEmpty()) {
            throw new IllegalArgumentException("Invalid exception class name: " + exceptionClassName);
        }
        //TODO: Is this a good idea? ApplicationException's equals/hashCode
        //will not work the way that would be expected
        ApplicationException appException = new ApplicationException() {
            @Override
            public boolean inherited() {
                return inherited;
            }

            @Override
            public boolean rollback() {
                return rollback;
            }

            @Override
            public Class<? extends Annotation> annotationType() {
                return ApplicationException.class;
            }
        };
        // add it to the map
        this.applicationExceptions.put(exceptionClassName, appException);
    }

    public boolean hasComponent(String componentName) {
        return eeModuleDescription.hasComponent(componentName);
    }

    public EEModuleDescription getEEModuleDescription() {
        return this.eeModuleDescription;
    }

    Map<String, ApplicationException> getApplicationExceptions() {
        return Collections.unmodifiableMap(this.applicationExceptions);
    }

    public EjbJarConfiguration createEjbJarConfiguration(final DeploymentPhaseContext phaseContext) throws DeploymentUnitProcessingException {
        EjbJarConfiguration ejbDeploymentConfiguration = new EjbJarConfiguration(this);
        prepareEjbJarConfiguration(ejbDeploymentConfiguration, phaseContext);
        return ejbDeploymentConfiguration;
    }

    protected void prepareEjbJarConfiguration(EjbJarConfiguration ejbDeploymentConfiguration, DeploymentPhaseContext phaseContext) throws DeploymentUnitProcessingException {
        final DeploymentUnit deploymentUnit = phaseContext.getDeploymentUnit();
        final Module module = deploymentUnit.getAttachment(org.jboss.as.server.deployment.Attachments.MODULE);
        if (module == null) {
            throw new IllegalStateException("Module not yet set in deployment unit " + deploymentUnit);
        }
        ClassLoader moduleClassLoader = module.getClassLoader();
        // setup application exceptions
        this.prepareApplicationExceptions(ejbDeploymentConfiguration, moduleClassLoader);

    }

    private void prepareApplicationExceptions(EjbJarConfiguration ejbDeploymentConfiguration, ClassLoader classLoader) throws DeploymentUnitProcessingException {
        for (Map.Entry<String, ApplicationException> entry : this.applicationExceptions.entrySet()) {
            String applicationExceptionClass = entry.getKey();
            try {
                Class<?> exceptionClass = classLoader.loadClass(applicationExceptionClass);
                ejbDeploymentConfiguration.addApplicationException(exceptionClass, entry.getValue());
            } catch (ClassNotFoundException cnfe) {
                throw new DeploymentUnitProcessingException(cnfe);
            }
        }
    }

    /**
     * Returns the {@link SessionBeanComponentDescription session beans} belonging to this {@link EjbJarDescription}.
     * <p/>
     * Returns an empty collection if no session beans exist
     *
     * @return
     */
    public Collection<SessionBeanComponentDescription> getSessionBeans() {
        Collection<SessionBeanComponentDescription> sessionBeans = new ArrayList<SessionBeanComponentDescription>();
        for (ComponentDescription componentDescription : this.eeModuleDescription.getComponentDescriptions()) {
            if (componentDescription instanceof SessionBeanComponentDescription) {
                sessionBeans.add((SessionBeanComponentDescription) componentDescription);
            }
        }
        return sessionBeans;
    }

    public Collection<MessageDrivenComponentDescription> getMessageDrivenBeans() {
        Collection<MessageDrivenComponentDescription> mdbs = new ArrayList<MessageDrivenComponentDescription>();
        for (ComponentDescription componentDescription : this.eeModuleDescription.getComponentDescriptions()) {
            if (componentDescription instanceof MessageDrivenComponentDescription) {
                mdbs.add((MessageDrivenComponentDescription) componentDescription);
            }
        }
        return mdbs;
    }

    public void addSessionBeans(Collection<SessionBeanComponentDescription> sessionBeans) {
        for (SessionBeanComponentDescription sessionBean : sessionBeans) {
            this.eeModuleDescription.addComponent(sessionBean);
        }
    }

    public boolean isWar() {
        return war;
    }

    public EEApplicationClasses getApplicationClassesDescription() {
        return applicationClassesDescription;
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

QDox parse error in file://<WORKSPACE>/data/code-rep-dataset/Dataset3/Tasks/12871.java