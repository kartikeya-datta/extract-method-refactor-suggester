error id: file://<WORKSPACE>/data/code-rep-dataset/Dataset3/Tasks/1808.java
file://<WORKSPACE>/data/code-rep-dataset/Dataset3/Tasks/1808.java
### com.thoughtworks.qdox.parser.ParseException: syntax error @[1,1]

error in qdox parser
file content:
```java
offset: 1
uri: file://<WORKSPACE>/data/code-rep-dataset/Dataset3/Tasks/1808.java
text:
```scala
I@@llegalStateException cannotCloseTransactionContainerEntityManger();

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

package org.jboss.as.jpa;

import org.jboss.as.server.deployment.DeploymentUnit;
import org.jboss.as.server.deployment.DeploymentUnitProcessingException;
import org.jboss.invocation.InterceptorContext;
import org.jboss.jandex.MethodInfo;
import org.jboss.logging.Cause;
import org.jboss.logging.Message;
import org.jboss.logging.MessageBundle;
import org.jboss.logging.Messages;
import org.jboss.modules.ModuleIdentifier;
import org.jboss.vfs.VirtualFile;

import javax.ejb.EJBException;
import javax.persistence.EntityManager;
import javax.persistence.PersistenceException;
import javax.persistence.TransactionRequiredException;
import java.net.URLConnection;
import java.util.Collection;

/**
 * Date: 07.06.2011
 *
 * @author <a href="mailto:jperkins@redhat.com">James R. Perkins</a>
 */
@MessageBundle(projectCode = "JBAS")
public interface JpaMessages {

    /**
     * The messages.
     */
    JpaMessages MESSAGES = Messages.getBundle(JpaMessages.class);

    /**
     * Creates an exception indicating the inability ot add the integration, represented by the {@code name} parameter,
     * module to the deployment.
     *
     * @param cause the cause of the error.
     * @param name  the name of the integration.
     *
     * @return a {@link RuntimeException} for the error.
     */
    @Message(id = 11420, value = "Could not add %s integration module to deployment")
    RuntimeException cannotAddIntegration(@Cause Throwable cause, String name);

    /**
     * Creates an exception indicating the input stream reference cannot be changed.
     *
     * @return an {@link IllegalArgumentException} for the error.
     */
    @Message(id = 11421, value = "Cannot change input stream reference.")
    IllegalArgumentException cannotChangeInputStream();

    /**
     * Creates an exception indicating the entity manager cannot be closed when it is managed by the container.
     *
     * @return an {@link IllegalStateException} for the error.
     */
    @Message(id = 11422, value = "Container managed entity manager can only be closed by the container " +
            "(will happen when @remove method is invoked on containing SFSB)")
    IllegalStateException cannotCloseContainerManagedEntityManager();

    /**
     * Creates an exception indicating only ExtendedEntityMangers can be closed.
     *
     * @param entityManagerTypeName the entity manager type name.
     *
     * @return a {@link RuntimeException} for the error.
     */
    @Message(id = 11423, value = "Can only close SFSB XPC entity manager that are instances of ExtendedEntityManager %s")
    RuntimeException cannotCloseNonExtendedEntityManager(String entityManagerTypeName);

    /**
     * Creates an exception indicating the transactional entity manager cannot be closed when it is managed by the
     * container.
     *
     * @return an {@link IllegalStateException} for the error.
     */
    @Message(id = 11424, value = "Container managed entity manager can only be closed by the container " +
            "(auto-cleared at tx/invocation end and closed when owning component is closed.)")
    IllegalArgumentException cannotCloseTransactionContainerEntityManger();

    /**
     * Creates an exception indicating the inability to create an instance of the adapter class represented by the
     * {@code className} parameter.
     *
     * @param cause     the cause of the error.
     * @param className the adapter class name.
     *
     * @return a {@link DeploymentUnitProcessingException} for the error.
     */
    @Message(id = 11425, value = "Could not create instance of adapter class '%s'")
    DeploymentUnitProcessingException cannotCreateAdapter(@Cause Throwable cause, String className);

    /**
     * Creates an exception indicating the application could not be deployed with the persistence provider, represented
     * by the {@code providerName} parameter, packaged.
     *
     * @param cause        the cause of the error.
     * @param providerName the persistence provider.
     *
     * @return a {@link DeploymentUnitProcessingException} for the error.
     */
    @Message(id = 11426, value = "Could not deploy application packaged persistence provider '%s'")
    DeploymentUnitProcessingException cannotDeployApp(@Cause Throwable cause, String providerName);

    /**
     * Creates an exception indicating a failure to get the Hibernate session factory from the entity manager.
     *
     * @param cause the cause of the error.
     *
     * @return a {@link RuntimeException} for the error.
     */
    @Message(id = 11427, value = "Couldn't get Hibernate session factory from entity manager")
    RuntimeException cannotGetSessionFactory(@Cause Throwable cause);

    /**
     * A message indicating the inability to inject a
     * {@link javax.persistence.spi.PersistenceUnitTransactionType#RESOURCE_LOCAL} container managed EntityManager
     * using the {@link javax.persistence.PersistenceContext} annotation.
     *
     * @return the message.
     */
    @Message(id = 11428, value = "Cannot inject RESOURCE_LOCAL container managed EntityManagers using @PersistenceContext")
    String cannotInjectResourceLocalEntityManager();

    /**
     * Creates an exception indicating the inability to inject a
     * {@link javax.persistence.spi.PersistenceUnitTransactionType#RESOURCE_LOCAL} entity manager, represented by the
     * {@code unitName} parameter, using the {@code <persistence-context-ref>}.
     *
     * @param unitName the unit name.
     *
     * @return a {@link DeploymentUnitProcessingException} for the error.
     */
    @Message(id = 11429, value = "Cannot inject RESOURCE_LOCAL entity manager %s using <persistence-context-ref>")
    DeploymentUnitProcessingException cannotInjectResourceLocalEntityManager(String unitName);

    /**
     * Creates an exception indicating the persistence provider adapter module, represented by the {@code adapterModule}
     * parameter, had an error loading.
     *
     * @param cause                    the cause of the error.
     * @param adapterModule            the name of the adapter module.
     * @param persistenceProviderClass the persistence provider class.
     *
     * @return a {@link DeploymentUnitProcessingException} for the error.
     */
    @Message(id = 11430, value = "Persistence provider adapter module (%s) load error (class %s)")
    DeploymentUnitProcessingException cannotLoadAdapterModule(@Cause Throwable cause, String adapterModule, String persistenceProviderClass);

    /**
     * Creates an exception indicating the entity class could not be loaded with the
     * {@link javax.persistence.spi.PersistenceUnitInfo#getNewTempClassLoader()}.
     *
     * @param cause     the cause of the error.
     * @param className the entity class name.
     *
     * @return a {@link RuntimeException} for the error.
     */
    @Message(id = 11431, value = "Could not load entity class '%s' with PersistenceUnitInfo.getNewTempClassLoader()")
    RuntimeException cannotLoadEntityClass(@Cause Throwable cause, String className);

    /**
     * Creates an exception indicating the {@code injectionTypeName} could not be loaded from the JPA modules class
     * loader.
     *
     * @param cause             the cause of the error.
     * @param injectionTypeName the name of the type.
     *
     * @return a {@link RuntimeException} for the error.
     */
    @Message(id = 11432, value = "Couldn't load %s from JPA modules classloader")
    RuntimeException cannotLoadFromJpa(@Cause Throwable cause, String injectionTypeName);

    /**
     * Creates an exception indicating the module, represented by the {@code moduleId} parameter, could not be loaded
     * for the adapter, represented by the {@code name} parameter.
     *
     * @param cause    the cause of the error.
     * @param moduleId the module id that was attempting to be loaded.
     * @param name     the name of the adapter.
     *
     * @return a {@link RuntimeException} for the error.
     */
    @Message(id = 11433, value = "Could not load module %s to add %s adapter to deployment")
    RuntimeException cannotLoadModule(@Cause Throwable cause, ModuleIdentifier moduleId, String name);

    /**
     * Creates an exception indicating the persistence provider module, represented by the
     * {@code persistenceProviderModule} parameter, had an error loading.
     *
     * @param cause                     the cause of the error.
     * @param persistenceProviderModule the name of the adapter module.
     * @param persistenceProviderClass  the persistence provider class.
     *
     * @return a {@link DeploymentUnitProcessingException} for the error.
     */
    @Message(id = 11434, value = "Persistence provider module load error %s (class %s)")
    DeploymentUnitProcessingException cannotLoadPersistenceProviderModule(@Cause Throwable cause, String persistenceProviderModule, String persistenceProviderClass);

    /**
     * Creates an exception indicating the top of the stack could not be replaced because the stack is {@code null}.
     *
     * @return a {@link RuntimeException} for the error.
     */
    @Message(id = 11435, value = "Internal error: Cannot replace top of stack as stack is null (same as being empty).")
    RuntimeException cannotReplaceStack();

    /**
     * Creates an exception indicating that both {@code key1} and {@code key2} cannot be specified for the object.
     *
     * @param key1      the first key/tag.
     * @param value1    the first value.
     * @param key2      the second key/tag.
     * @param value2    the second value.
     * @param parentTag the parent tag.
     * @param object    the object the values are being specified for.
     *
     * @return a {@link DeploymentUnitProcessingException} for the error.
     */
    @Message(id = 11436, value = "Cannot specify both %s (%s) and %s (%s) in %s for %s")
    DeploymentUnitProcessingException cannotSpecifyBoth(String key1, Object value1, String key2, Object value2, String parentTag, Object object);

    /**
     * Creates an exception indicating the extended persistence context for the SFSB already exists.
     *
     * @param puScopedName          the persistence unit name.
     * @param existingEntityManager the existing transactional entity manager.
     * @param self                  the entity manager attempting to be created.
     *
     * @return an {@link javax.ejb.EJBException} for the error.
     */
    @Message(id = 11437, value = "Found extended persistence context in SFSB invocation call stack but that cannot be used " +
            "because the transaction already has a transactional context associated with it.  " +
            "This can be avoided by changing application code, either eliminate the extended " +
            "persistence context or the transactional context.  See JPA spec 2.0 section 7.6.3.1.  " +
            "Scoped persistence unit name=%s, persistence context already in transaction =%s, extended persistence context =%s")
    EJBException cannotUseExtendedPersistenceTransaction(String puScopedName, EntityManager existingEntityManager, EntityManager self);

    /**
     * Creates an exception indicating the child could not be found on the parent.
     *
     * @param child  the child that could not be found.
     * @param parent the parent.
     *
     * @return a {@link RuntimeException} for the error.
     */
    @Message(id = 11438, value = "Could not find child '%s' on '%s'")
    RuntimeException childNotFound(String child, VirtualFile parent);

    /**
     * Creates an exception indicating the class level annotation must provide the parameter specified.
     *
     * @param annotation the annotation.
     * @param parameter  the parameter.
     *
     * @return an {@link IllegalArgumentException} for the error.
     */
    @Message(id = 11439, value = "Class level %s annotations must provide a %s.")
    IllegalArgumentException classLevelAnnotationParameterRequired(String annotation, String parameter);

    /**
     * A message indicating that the deployment unit, represented by the {@code path} parameter, could not be found at
     * the current deployment unit, represented by the {@code deploymentUnit} parameter.
     *
     * @param puName         the persistence unit name.
     * @param deploymentUnit the deployment unit.
     *
     * @return the message.
     */
    @Message(id = 11440, value = "Can't find a deployment unit named %s at %s")
    String deploymentUnitNotFound(String puName, DeploymentUnit deploymentUnit);

    /**
     * Creates an exception indicating that the deployment unit, represented by the {@code path} and {@code puName}
     * parameters, could not be found at the current deployment unit, represented by the {@code deploymentUnit}
     * parameter.
     *
     * @param path           the path.
     * @param puName         the persistence unit name.
     * @param deploymentUnit the deployment unit.
     *
     * @return an {@link IllegalArgumentException} for the error.
     */
    @Message(id = 11441, value = "Can't find a deployment unit named %s#%s at %s")
    IllegalArgumentException deploymentUnitNotFound(String path, String puName, DeploymentUnit deploymentUnit);

    /**
     * Creates an exception indicating the parameter, likely a collection, is empty.
     *
     * @param parameterName the parameter name.
     *
     * @return an {@link IllegalArgumentException} for the error.
     */
    @Message(id = 11442, value = "Parameter %s is empty")
    IllegalArgumentException emptyParameter(String parameterName);

    /**
     * Creates an exception indicating there was an error when trying to get the transaction associated with the
     * current thread.
     *
     * @param cause the cause of the error.
     *
     * @return an {@link IllegalStateException} for the error.
     */
    @Message(id = 11443, value = "An error occurred while getting the transaction associated with the current thread: %s")
    IllegalStateException errorGettingTransaction(Exception cause);

    /**
     * Creates an exception indicating a failure to get the adapter for the persistence provider.
     *
     * @param className the adapter class name.
     *
     * @return a {@link DeploymentUnitProcessingException} for the error.
     */
    @Message(id = 11445, value = "Failed to get adapter for persistence provider '%s'")
    DeploymentUnitProcessingException failedToGetAdapter(String className);

    /**
     * Creates an exception indicating a failure to add the persistence unit service.
     *
     * @param cause  the cause of the error.
     * @param puName the persistence unit name.
     *
     * @return a {@link DeploymentUnitProcessingException} for the error.
     */
    @Message(id = 11446, value = "Failed to add persistence unit service for %s")
    DeploymentUnitProcessingException failedToAddPersistenceUnit(@Cause Throwable cause, String puName);

    /**
     * Creates an exception indicating a failure to get the module for the deployment unit represented by the
     * {@code deploymentUnit} parameter.
     *
     * @param deploymentUnit the deployment unit that failed.
     *
     * @return a {@link DeploymentUnitProcessingException} for the error.
     */
    @Message(id = 11447, value = "Failed to get module attachment for %s")
    DeploymentUnitProcessingException failedToGetModuleAttachment(DeploymentUnit deploymentUnit);

    /**
     * A message indicating a failure to parse the file.
     *
     * @param file the file that could not be parsed.
     *
     * @return the message.
     */
    @Message(id = 11448, value = "Failed to parse %s")
    String failedToParse(VirtualFile file);

    /**
     * Creates an exception indicating the entity manager factory implementation can only be a Hibernate version.
     *
     * @return a {@link RuntimeException} for the error.
     */
    @Message(id = 11449, value = "Can only inject from a Hibernate EntityManagerFactoryImpl")
    RuntimeException hibernateOnlyEntityManagerFactory();

    /**
     * Creates an exception indicating the persistence unit, represented by the {@code puName} parameter, used an
     * incorrect persistence provider class name.
     *
     * @param puName                       the persistence unit name.
     * @param persistenceProviderModule    the module name.
     * @param persistenceProviderClassName the provider class name.
     * @param providerClasses              a collection providers.
     *
     * @return a {@link DeploymentUnitProcessingException} for the error.
     */
    @Message(id = 11450, value = "%s used incorrect persistence provider class name. Module = %s, persistenceProvider specified = %s, providers found = {%s}")
    DeploymentUnitProcessingException incorrectPersistenceProvider(String puName, String persistenceProviderModule, String persistenceProviderClassName, Collection<?> providerClasses);

    /**
     * Creates an exception indicating the persistence unit name contains an invalid character.
     *
     * @param persistenceUnitName the persistence unit name.
     * @param c                   the invalid character.
     *
     * @return an {@link IllegalArgumentException} for the error.
     */
    @Message(id = 11451, value = "Persistence unit name (%s) contains illegal '%s' character")
    IllegalArgumentException invalidPersistenceUnitName(String persistenceUnitName, char c);

    /**
     * Creates an exception indicating the scoped persistence name is invalid.
     *
     * @param validName the valid scope name.
     * @param name      the scope name that was supplied.
     *
     * @return a {@link RuntimeException} for the error.
     */
    @Message(id = 11452, value = "Scoped persistence name should be \"%s\" but was %s")
    RuntimeException invalidScopeName(String validName, String name);

    /**
     * Creates an exception indicating the inability to integrate the module, represented by the {@code integrationName}
     * parameter, to the deployment as it expected a {@link java.net.JarURLConnection}.
     *
     * @param integrationName the name of the integration that could not be integrated.
     * @param connection      the invalid connection.
     *
     * @return a {@link RuntimeException} for the error.
     */
    @Message(id = 11453, value = "Could not add %s integration module to deployment, did not get expected JarUrlConnection, got %s")
    RuntimeException invalidUrlConnection(String integrationName, URLConnection connection);

    /**
     * Creates an exception indicating the {@code persistence-unit-ref} without a {@code lookup} or
     * {@code persistence-unit-name} is not yet supported (implemented).
     *
     * @return a {@link RuntimeException} for the error.
     */
    @Message(id = 11454, value = "Support for persistence-unit-ref without a lookup or persistence-unit-name, isn't yet implemented")
    RuntimeException lookupOrPersistenceUnitNameRequired();

    /**
     * Creates an exception indicating the persistence unit metadata likely because thread local was not set.
     *
     * @return a {@link RuntimeException} for the error.
     */
    @Message(id = 11455, value = "Missing PersistenceUnitMetadata (thread local wasn't set)")
    RuntimeException missingPersistenceUnitMetadata();

    /**
     * Creates an exception indicating the persistence provider adapter module, represented by the {@code adapterModule}
     * parameter, has more than one adapter.
     *
     * @param adapterModule the adapter module name.
     *
     * @return a {@link DeploymentUnitProcessingException} for the error.
     */
    @Message(id = 11456, value = "Persistence provider adapter module (%s) has more than one adapter")
    DeploymentUnitProcessingException multipleAdapters(String adapterModule);

    /**
     * Creates an exception indicating more than one thread is invoking the stateful session bean at the same time.
     *
     * @param sessionBean the stateful session bean.
     *
     * @return a {@link RuntimeException} for the error.
     */
    @Message(id = 11457, value = "More than one thread is invoking stateful session bean '%s' at the same time.")
    RuntimeException multipleThreadsInvokingSfsb(Object sessionBean);

    /**
     * Creates an exception indicating more than one thread is using the entity manager instance at the same time.
     *
     * @param entityManager the entity manager.
     *
     * @return a {@link RuntimeException} for the error.
     */
    @Message(id = 11458, value = "More than one thread is using EntityManager instance '%s' at the same time.")
    RuntimeException multipleThreadsUsingEntityManager(EntityManager entityManager);

    /**
     * Creates an exception indicating the {@code name} was not set in the {@link org.jboss.invocation.InterceptorContext}.
     *
     * @param name    the name of the field not set.
     * @param context the context.
     *
     * @return an {@link IllegalArgumentException} for the error.
     */
    @Message(id = 11459, value = "%s not set in InterceptorContext: %s")
    IllegalArgumentException notSetInInterceptorContext(String name, InterceptorContext context);

    /**
     * Creates an exception indicating the method is not yet implemented.
     *
     * @return a {@link RuntimeException} for the error.
     */
    @Message(id = 11460, value = "Not yet implemented")
    RuntimeException notYetImplemented();

    /**
     * Creates an exception indicating the {@code description} is {@code null}.
     *
     * @param description   the description of the parameter.
     * @param parameterName the parameter name.
     *
     * @return a {@link RuntimeException} for the error.
     */
    @Message(id = 11461, value = "Internal %s error, null %s passed in")
    RuntimeException nullParameter(String description, String parameterName);

    /**
     * Creates an exception indicating the variable is {@code null}.
     *
     * @param varName the variable name.
     *
     * @return an {@link IllegalArgumentException} for the error.
     */
    @Message(id = 11462, value = "Parameter %s is null")
    IllegalArgumentException nullVar(String varName);

    /**
     * A message indicating the object for the class ({@code cls} has been defined and is not {@code null}.
     *
     * @param cls      the class for the object.
     * @param previous the previously defined object.
     *
     * @return the message.
     */
    @Message(id = 11463, value = "Previous object for class %s is %s instead of null")
    String objectAlreadyDefined(Class<?> cls, Object previous);

    /**
     * Creates an exception indicating only one persistence provider can be packaged with an application.
     *
     * @param providers the list of providers found.
     *
     * @return a {@link DeploymentUnitProcessingException} for the error.
     */
    @Message(id = 11464, value = "Only one persistence provider can be packaged with an application: %s")
    DeploymentUnitProcessingException onlyOnePersistenceProviderAllowed(Collection<?> providers);

    /**
     * Creates an exception indicating the parameter must be a {@link org.jboss.as.jpa.container.AbstractEntityManager}
     * so that the metadata can be retrieved.
     *
     * @param parameterName the parameter name.
     *
     * @return a {@link RuntimeException} for the error.
     */
    @Message(id = 11465, value = "Internal error, %s needs to be a AbstractEntityManager so that we can get metadata")
    RuntimeException parameterMustBeAbstractEntityManager(String parameterName);

    /**
     * Creates an exception indicating the persistence provider could not be found.
     *
     * @param providerName the provider name.
     *
     * @return a {@link javax.persistence.PersistenceException} for the error.
     */
    @Message(id = 11466, value = "PersistenceProvider '%s' not found")
    PersistenceException persistenceProviderNotFound(String providerName);

    /**
     * Creates an exception indicating the relative path could not be found.
     *
     * @param cause the cause of the error.
     * @param path  the path that could not be found.
     *
     * @return a {@link RuntimeException} for the error.
     */
    @Message(id = 11467, value = "Could not find relative path: %s")
    RuntimeException relativePathNotFound(@Cause Throwable cause, String path);

    /**
     * A message indicating the annotation is only valid on setter method targets.
     *
     * @param annotation the annotation.
     * @param methodInfo the method information.
     *
     * @return the message.
     */
    @Message(id = 11468, value = "%s injection target is invalid.  Only setter methods are allowed: %s")
    String setterMethodOnlyAnnotation(String annotation, MethodInfo methodInfo);

    /**
     * Creates an exception indicating a transaction is required for the operation.
     *
     * @return a {@link javax.persistence.TransactionRequiredException} for the error.
     */
    @Message(id = 11469, value = "Transaction is required to perform this operation (either use a transaction or extended persistence context)")
    TransactionRequiredException transactionRequired();
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

QDox parse error in file://<WORKSPACE>/data/code-rep-dataset/Dataset3/Tasks/1808.java