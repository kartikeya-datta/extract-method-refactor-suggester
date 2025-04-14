error id: file://<WORKSPACE>/data/code-rep-dataset/Dataset3/Tasks/11595.java
file://<WORKSPACE>/data/code-rep-dataset/Dataset3/Tasks/11595.java
### com.thoughtworks.qdox.parser.ParseException: syntax error @[1,1]

error in qdox parser
file content:
```java
offset: 1
uri: file://<WORKSPACE>/data/code-rep-dataset/Dataset3/Tasks/11595.java
text:
```scala
v@@iewConfiguration.addViewInterceptor(new SecurityContextInterceptorFactory(securityRequired), InterceptorOrder.View.SECURITY_CONTEXT);

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

package org.jboss.as.ejb3.security;

import java.lang.reflect.Method;
import java.lang.reflect.Modifier;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

import org.jboss.as.ee.component.ComponentConfiguration;
import org.jboss.as.ee.component.ViewConfiguration;
import org.jboss.as.ee.component.ViewConfigurator;
import org.jboss.as.ee.component.ViewDescription;
import org.jboss.as.ee.component.interceptors.InterceptorOrder;
import org.jboss.as.ee.component.serialization.WriteReplaceInterface;
import org.jboss.as.ejb3.logging.EjbLogger;
import org.jboss.as.ejb3.component.EJBComponentDescription;
import org.jboss.as.ejb3.component.EJBViewDescription;
import org.jboss.as.ejb3.component.MethodIntf;
import org.jboss.as.ejb3.component.session.SessionBeanComponentDescription;
import org.jboss.as.ejb3.deployment.ApplicableMethodInformation;
import org.jboss.as.ejb3.security.service.EJBViewMethodSecurityAttributesService;
import org.jboss.as.server.deployment.DeploymentPhaseContext;
import org.jboss.as.server.deployment.DeploymentUnit;
import org.jboss.as.server.deployment.DeploymentUnitProcessingException;
import org.jboss.as.server.deployment.reflect.ClassReflectionIndexUtil;
import org.jboss.as.server.deployment.reflect.DeploymentReflectionIndex;
import org.jboss.invocation.ImmediateInterceptorFactory;
import org.jboss.invocation.Interceptor;
import org.jboss.msc.service.ServiceName;

import static org.jboss.as.ejb3.logging.EjbLogger.ROOT_LOGGER;

/**
 * {@link ViewConfigurator} responsible for setting up necessary security interceptors on an EJB view.
 * <p/>
 * User: Jaikiran Pai
 */
public class EJBSecurityViewConfigurator implements ViewConfigurator {

    @Override
    public void configure(DeploymentPhaseContext context, ComponentConfiguration componentConfiguration, ViewDescription viewDescription, ViewConfiguration viewConfiguration) throws DeploymentUnitProcessingException {
        if (componentConfiguration.getComponentDescription() instanceof EJBComponentDescription == false) {
            throw EjbLogger.ROOT_LOGGER.invalidEjbComponent(componentConfiguration.getComponentName(), componentConfiguration.getComponentClass());
        }
        final DeploymentReflectionIndex deploymentReflectionIndex = context.getDeploymentUnit().getAttachment(org.jboss.as.server.deployment.Attachments.REFLECTION_INDEX);
        final EJBComponentDescription ejbComponentDescription = (EJBComponentDescription) componentConfiguration.getComponentDescription();
        // The getSecurityDomain() will return a null value if neither an explicit security domain is configured
        // for the bean nor there's any default security domain that's configured at EJB3 subsystem level.
        // In such cases, we do *not* apply any security interceptors
        if (ejbComponentDescription.getSecurityDomain() == null || ejbComponentDescription.getSecurityDomain().isEmpty()) {
            ROOT_LOGGER.debug("Security is *not* enabled on EJB: " + ejbComponentDescription.getEJBName() +
                    ", since no explicit security domain is configured for the bean, nor is there any default security domain configured in the EJB3 subsystem");
            return;
        }

        final String viewClassName = viewDescription.getViewClassName();
        final EJBViewDescription ejbViewDescription = (EJBViewDescription) viewDescription;

        // setup the JACC contextID.
        DeploymentUnit deploymentUnit = context.getDeploymentUnit();
        String contextID = deploymentUnit.getName();
        if (deploymentUnit.getParent() != null) {
            contextID = deploymentUnit.getParent().getName() + "!" + contextID;
        }

        final EJBViewMethodSecurityAttributesService.Builder viewMethodSecurityAttributesServiceBuilder;
        final ServiceName viewMethodSecurityAttributesServiceName;
        // The way @WebService view integrates with EJBs is tricky. It marks the fully qualified bean class name as the view name of the service endpoint. Now, if that bean also has a @LocalBean (i.e. no-interface view)
        // then we now have 2 views with the same view name. In such cases, it's fine to skip one of those views and register this service only once, since essentially, the service is expected to return the same data
        // for both these views. So here we skip the @WebService view if the bean also has a @LocalBean (no-interface) view and let the EJBViewMethodSecurityAttributesService be built when the no-interface view is processed
        if (ejbComponentDescription instanceof SessionBeanComponentDescription && MethodIntf.SERVICE_ENDPOINT == ejbViewDescription.getMethodIntf() && ((SessionBeanComponentDescription) ejbComponentDescription).hasNoInterfaceView()) {
            viewMethodSecurityAttributesServiceBuilder = null;
            viewMethodSecurityAttributesServiceName = null;
        } else {
            viewMethodSecurityAttributesServiceBuilder = new EJBViewMethodSecurityAttributesService.Builder(viewClassName);
            viewMethodSecurityAttributesServiceName =  EJBViewMethodSecurityAttributesService.getServiceName(ejbComponentDescription.getApplicationName(), ejbComponentDescription.getModuleName(), ejbComponentDescription.getEJBName(), viewClassName);
        }
        // setup the method specific security interceptor(s)
        boolean beanHasMethodLevelSecurityMetadata = false;
        final List<Method> viewMethods = viewConfiguration.getProxyFactory().getCachedMethods();
        final List<Method> methodsWithoutExplicitSecurityConfiguration = new ArrayList<Method>();
        for (final Method viewMethod : viewMethods) {
            // TODO: proxy factory exposes non-public methods, is this a bug in the no-interface view?
            if (!Modifier.isPublic(viewMethod.getModifiers())) {
                continue;
            }
            if (viewMethod.getDeclaringClass() == WriteReplaceInterface.class) {
                continue;
            }
            // setup the authorization interceptor
            final ApplicableMethodInformation<EJBMethodSecurityAttribute> permissions = ejbComponentDescription.getDescriptorMethodPermissions();
            boolean methodHasSecurityMetadata = handlePermissions(contextID, componentConfiguration, viewConfiguration, deploymentReflectionIndex, viewClassName, ejbViewDescription, viewMethod, permissions, false, viewMethodSecurityAttributesServiceBuilder);
            if (!methodHasSecurityMetadata) {
                //if it was not handled by the descriptor processor we look for annotation basic info
                methodHasSecurityMetadata = handlePermissions(contextID, componentConfiguration, viewConfiguration, deploymentReflectionIndex, viewClassName, ejbViewDescription, viewMethod, ejbComponentDescription.getAnnotationMethodPermissions(), true, viewMethodSecurityAttributesServiceBuilder);
            }
            // if any method has security metadata then the bean has method level security metadata
            if (methodHasSecurityMetadata) {
                beanHasMethodLevelSecurityMetadata = true;
            } else {
                // make a note that this method didn't have any explicit method permissions configured
                methodsWithoutExplicitSecurityConfiguration.add(viewMethod);
            }
        }

        final boolean securityRequired = beanHasMethodLevelSecurityMetadata || ejbComponentDescription.hasBeanLevelSecurityMetadata();
        // setup the security context interceptor
        viewConfiguration.addViewInterceptor(new SecurityContextInterceptorFactory(securityRequired, true, contextID), InterceptorOrder.View.SECURITY_CONTEXT);
        // now add the security interceptor if the bean has *any* security metadata applicable
        if (securityRequired) {
            // also check the missing-method-permissions-deny-access configuration and add the authorization interceptor
            // to methods which don't have explicit method permissions.
            // (@see http://anil-identity.blogspot.in/2010/02/tip-interpretation-of-missing-ejb.html for details)
            final Boolean denyAccessToMethodsMissingPermissions = ((EJBComponentDescription) componentConfiguration.getComponentDescription()).isMissingMethodPermissionsDeniedAccess();
            // default to "deny access"
            if (denyAccessToMethodsMissingPermissions == null || denyAccessToMethodsMissingPermissions == true) {
                for (final Method viewMethod : methodsWithoutExplicitSecurityConfiguration) {
                    if (viewMethodSecurityAttributesServiceBuilder != null) {
                        // build the EJBViewMethodSecurityAttributesService to expose these security attributes to other components like WS (@see https://issues.jboss.org/browse/WFLY-308)
                        viewMethodSecurityAttributesServiceBuilder.addMethodSecurityMetadata(viewMethod, EJBMethodSecurityAttribute.denyAll());
                    }
                    // "deny access" implies we need the authorization interceptor to be added so that it can nuke the invocation
                    final Interceptor authorizationInterceptor = new AuthorizationInterceptor(EJBMethodSecurityAttribute.denyAll(), viewClassName, viewMethod, contextID);
                    viewConfiguration.addViewInterceptor(viewMethod, new ImmediateInterceptorFactory(authorizationInterceptor), InterceptorOrder.View.EJB_SECURITY_AUTHORIZATION_INTERCEPTOR);
                }
            }
            if (viewMethodSecurityAttributesServiceBuilder != null) {
                final EJBViewMethodSecurityAttributesService viewMethodSecurityAttributesService = viewMethodSecurityAttributesServiceBuilder.build();
                context.getServiceTarget().addService(viewMethodSecurityAttributesServiceName, viewMethodSecurityAttributesService).install();
            }
        } else {
            // if security is not applicable for the EJB, then do *not* add the security related interceptors
            ROOT_LOGGER.debug("Security is *not* enabled on EJB: " + ejbComponentDescription.getEJBName() + ", no security interceptors will apply");

            if (viewMethodSecurityAttributesServiceBuilder != null) {
                // we install the service anyway since other components can depend on it
                final EJBViewMethodSecurityAttributesService viewMethodSecurityAttributesService = viewMethodSecurityAttributesServiceBuilder.build();
                context.getServiceTarget().addService(viewMethodSecurityAttributesServiceName, viewMethodSecurityAttributesService).install();
            }
            return;
        }

    }

    private boolean handlePermissions(String contextID, ComponentConfiguration componentConfiguration, ViewConfiguration viewConfiguration, DeploymentReflectionIndex deploymentReflectionIndex, String viewClassName, EJBViewDescription ejbViewDescription, Method viewMethod, ApplicableMethodInformation<EJBMethodSecurityAttribute> permissions, boolean annotations,
                                      final EJBViewMethodSecurityAttributesService.Builder viewMethodSecurityAttributesServiceBuilder) {
        EJBMethodSecurityAttribute ejbMethodSecurityMetaData = permissions.getViewAttribute(ejbViewDescription.getMethodIntf(), viewMethod);
        final List<EJBMethodSecurityAttribute> allAttributes = new ArrayList<EJBMethodSecurityAttribute>();
        allAttributes.addAll(permissions.getAllAttributes(ejbViewDescription.getMethodIntf(), viewMethod));

        if (ejbMethodSecurityMetaData == null) {
            ejbMethodSecurityMetaData = permissions.getViewAttribute(MethodIntf.BEAN, viewMethod);
        }
        allAttributes.addAll(permissions.getAllAttributes(MethodIntf.BEAN, viewMethod));

        final Method classMethod = ClassReflectionIndexUtil.findMethod(deploymentReflectionIndex, componentConfiguration.getComponentClass(), viewMethod);
        if (ejbMethodSecurityMetaData == null) {
            //if this is null we try with the corresponding bean method
            if (classMethod != null) {
                ejbMethodSecurityMetaData = permissions.getAttribute(ejbViewDescription.getMethodIntf(), classMethod);
                if (ejbMethodSecurityMetaData == null) {
                    ejbMethodSecurityMetaData = permissions.getAttribute(MethodIntf.BEAN, classMethod);

                }
            }
        }
        if (classMethod != null) {
            allAttributes.addAll(permissions.getAllAttributes(ejbViewDescription.getMethodIntf(), classMethod));
            allAttributes.addAll(permissions.getAllAttributes(MethodIntf.BEAN, classMethod));
        }


        //we do not add the security interceptor if there is no security information
        if (ejbMethodSecurityMetaData != null) {

            if (!annotations &&
                    !ejbMethodSecurityMetaData.isDenyAll() &&
                    !ejbMethodSecurityMetaData.isPermitAll()) {
                //roles are additive when defined in the deployment descriptor
                final Set<String> rolesAllowed = new HashSet<String>();
                for (EJBMethodSecurityAttribute attr : allAttributes) {
                    rolesAllowed.addAll(attr.getRolesAllowed());
                }
                ejbMethodSecurityMetaData = EJBMethodSecurityAttribute.rolesAllowed(rolesAllowed);
            }
            // build the EJBViewMethodSecurityAttributesService to expose these security attributes to other components like WS (@see https://issues.jboss.org/browse/WFLY-308)
            viewMethodSecurityAttributesServiceBuilder.addMethodSecurityMetadata(viewMethod, ejbMethodSecurityMetaData);
            // add the interceptor
            final Interceptor authorizationInterceptor = new AuthorizationInterceptor(ejbMethodSecurityMetaData, viewClassName, viewMethod, contextID);
            viewConfiguration.addViewInterceptor(viewMethod, new ImmediateInterceptorFactory(authorizationInterceptor), InterceptorOrder.View.EJB_SECURITY_AUTHORIZATION_INTERCEPTOR);

            return true;
        }
        return false;
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

QDox parse error in file://<WORKSPACE>/data/code-rep-dataset/Dataset3/Tasks/11595.java