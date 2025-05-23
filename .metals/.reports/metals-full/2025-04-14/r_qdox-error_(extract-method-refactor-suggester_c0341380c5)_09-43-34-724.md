error id: file://<WORKSPACE>/data/code-rep-dataset/Dataset3/Tasks/11541.java
file://<WORKSPACE>/data/code-rep-dataset/Dataset3/Tasks/11541.java
### com.thoughtworks.qdox.parser.ParseException: syntax error @[1,1]

error in qdox parser
file content:
```java
offset: 1
uri: file://<WORKSPACE>/data/code-rep-dataset/Dataset3/Tasks/11541.java
text:
```scala
.@@addRejectCheck(RejectAttributeChecker.SIMPLE_EXPRESSIONS, VaultResourceDefinition.OPTIONS)

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

package org.jboss.as.security;

import java.util.Collections;

import org.jboss.as.controller.Extension;
import org.jboss.as.controller.ExtensionContext;
import org.jboss.as.controller.ModelVersion;
import org.jboss.as.controller.OperationFailedException;
import org.jboss.as.controller.PathAddress;
import org.jboss.as.controller.PathElement;
import org.jboss.as.controller.SubsystemRegistration;
import org.jboss.as.controller.descriptions.ModelDescriptionConstants;
import org.jboss.as.controller.descriptions.StandardResourceDescriptionResolver;
import org.jboss.as.controller.operations.common.GenericSubsystemDescribeHandler;
import org.jboss.as.controller.parsing.ExtensionParsingContext;
import org.jboss.as.controller.registry.ManagementResourceRegistration;
import org.jboss.as.controller.registry.Resource;
import org.jboss.as.controller.transform.CombinedTransformer;
import org.jboss.as.controller.transform.OperationResultTransformer;
import org.jboss.as.controller.transform.ResourceTransformationContext;
import org.jboss.as.controller.transform.TransformationContext;
import org.jboss.as.controller.transform.description.RejectAttributeChecker;
import org.jboss.as.controller.transform.description.ResourceTransformationDescriptionBuilder;
import org.jboss.as.controller.transform.description.TransformationDescription;
import org.jboss.as.controller.transform.description.TransformationDescriptionBuilder;
import org.jboss.dmr.ModelNode;
import org.jboss.msc.service.ServiceName;

/**
 * The security extension.
 *
 * @author <a href="mailto:mmoyses@redhat.com">Marcus Moyses</a>
 * @author <a href="mailto:darran.lofthouse@jboss.com">Darran Lofthouse</a>
 */
public class SecurityExtension implements Extension {

    public static final ServiceName JBOSS_SECURITY = ServiceName.JBOSS.append("security");

    public static final String SUBSYSTEM_NAME = "security";
    static final PathElement PATH_SUBSYSTEM = PathElement.pathElement(ModelDescriptionConstants.SUBSYSTEM, SUBSYSTEM_NAME);

    private static final String RESOURCE_NAME = SecurityExtension.class.getPackage().getName() + ".LocalDescriptions";

    private static final int MANAGEMENT_API_MAJOR_VERSION = 1;
    private static final int MANAGEMENT_API_MINOR_VERSION = 2;
    private static final int MANAGEMENT_API_MICRO_VERSION = 0;

    private static final SecuritySubsystemParser PARSER = SecuritySubsystemParser.getInstance();
    static final PathElement ACL_PATH = PathElement.pathElement(Constants.ACL, Constants.CLASSIC);
    static final PathElement PATH_JASPI_AUTH = PathElement.pathElement(Constants.AUTHENTICATION, Constants.JASPI);
    static final PathElement PATH_CLASSIC_AUTHENTICATION = PathElement.pathElement(Constants.AUTHENTICATION, Constants.CLASSIC);
    static final PathElement SECURITY_DOMAIN_PATH = PathElement.pathElement(Constants.SECURITY_DOMAIN);
    static final PathElement PATH_AUTHORIZATION_CLASSIC = PathElement.pathElement(Constants.AUTHORIZATION, Constants.CLASSIC);
    static final PathElement PATH_MAPPING_CLASSIC = PathElement.pathElement(Constants.MAPPING, Constants.CLASSIC);
    static final PathElement PATH_AUDIT_CLASSIC = PathElement.pathElement(Constants.AUDIT, Constants.CLASSIC);
    static final PathElement PATH_LOGIN_MODULE_STACK = PathElement.pathElement(Constants.LOGIN_MODULE_STACK);
    static final PathElement VAULT_PATH = PathElement.pathElement(Constants.VAULT, Constants.CLASSIC);
    static final PathElement JSSE_PATH = PathElement.pathElement(Constants.JSSE, Constants.CLASSIC);

    static StandardResourceDescriptionResolver getResourceDescriptionResolver(final String keyPrefix) {
        return new StandardResourceDescriptionResolver(keyPrefix, RESOURCE_NAME, SecurityExtension.class.getClassLoader(), true, true);
    }
    static StandardResourceDescriptionResolver getResourceDescriptionResolver(final String... keyPrefix) {
           StringBuilder prefix = new StringBuilder();
           for (String kp : keyPrefix) {
               if (prefix.length()>0){
                   prefix.append('.');
               }
               prefix.append(kp);
           }
           return new StandardResourceDescriptionResolver(prefix.toString(), RESOURCE_NAME, SecurityExtension.class.getClassLoader(), true, false);
       }

    @Override
    public void initialize(ExtensionContext context) {

        final boolean registerRuntimeOnly = context.isRuntimeOnlyRegistrationValid();

        final SubsystemRegistration subsystem = context.registerSubsystem(SUBSYSTEM_NAME, MANAGEMENT_API_MAJOR_VERSION,
                MANAGEMENT_API_MINOR_VERSION, MANAGEMENT_API_MICRO_VERSION);
        final ManagementResourceRegistration registration = subsystem.registerSubsystemModel(SecuritySubsystemRootResourceDefinition.INSTANCE);
        registration.registerOperationHandler(GenericSubsystemDescribeHandler.DEFINITION, GenericSubsystemDescribeHandler.INSTANCE);

        final ManagementResourceRegistration securityDomain = registration.registerSubModel(new SecurityDomainResourceDefinition(registerRuntimeOnly));
        final ManagementResourceRegistration jaspi = securityDomain.registerSubModel(JASPIAuthenticationResourceDefinition.INSTANCE);
        jaspi.registerSubModel(LoginModuleStackResourceDefinition.INSTANCE);
        securityDomain.registerSubModel(ClassicAuthenticationResourceDefinition.INSTANCE);
        securityDomain.registerSubModel(AuthorizationResourceDefinition.INSTANCE);
        securityDomain.registerSubModel(MappingResourceDefinition.INSTANCE);
        securityDomain.registerSubModel(ACLResourceDefinition.INSTANCE);
        securityDomain.registerSubModel(AuditResourceDefinition.INSTANCE);
        securityDomain.registerSubModel(IdentityTrustResourceDefinition.INSTANCE);
        securityDomain.registerSubModel(JSSEResourceDefinition.INSTANCE);
        registration.registerSubModel(VaultResourceDefinition.INSTANCE);
        subsystem.registerXMLElementWriter(PARSER);

        if (context.isRegisterTransformers()) {
            registerTransformers(subsystem);
        }
    }

    @Override
    public void initializeParsers(ExtensionParsingContext context) {
        context.setSubsystemXmlMapping(SUBSYSTEM_NAME, Namespace.SECURITY_1_0.getUriString(), PARSER);
        context.setSubsystemXmlMapping(SUBSYSTEM_NAME, Namespace.SECURITY_1_1.getUriString(), PARSER);
        context.setSubsystemXmlMapping(SUBSYSTEM_NAME, Namespace.SECURITY_1_2.getUriString(), PARSER);
    }

    private void registerTransformers(SubsystemRegistration subsystemRegistration) {
        ResourceTransformationDescriptionBuilder builder = TransformationDescriptionBuilder.Factory.createSubsystemInstance();
        ResourceTransformationDescriptionBuilder securityDomain = builder.addChildResource(SECURITY_DOMAIN_PATH);

        final ModulesToAttributeTransformer loginModule = new ModulesToAttributeTransformer(Constants.LOGIN_MODULE, Constants.LOGIN_MODULES);
        ResourceTransformationDescriptionBuilder child = securityDomain.addChildResource(PATH_CLASSIC_AUTHENTICATION)
                .setCustomResourceTransformer(loginModule)
                .addOperationTransformationOverride(ModelDescriptionConstants.ADD)
                .setCustomOperationTransformer(loginModule)
                .inheritResourceAttributeDefinitions().end();
        child.discardChildResource(PathElement.pathElement(Constants.LOGIN_MODULE));


        final ModulesToAttributeTransformer policyModule = new ModulesToAttributeTransformer(Constants.POLICY_MODULE, Constants.POLICY_MODULES);

        child = securityDomain.addChildResource(PATH_AUTHORIZATION_CLASSIC)
                .setCustomResourceTransformer(policyModule)
                .addOperationTransformationOverride(ModelDescriptionConstants.ADD)
                .setCustomOperationTransformer(policyModule)
                .inheritResourceAttributeDefinitions().end();
        child.discardChildResource(PathElement.pathElement(Constants.POLICY_MODULE));

        final ModulesToAttributeTransformer mappingModule = new ModulesToAttributeTransformer(Constants.MAPPING_MODULE, Constants.MAPPING_MODULES);

        child = securityDomain.addChildResource(PATH_MAPPING_CLASSIC)
                .setCustomResourceTransformer(mappingModule)
                .addOperationTransformationOverride(ModelDescriptionConstants.ADD)
                .setCustomOperationTransformer(mappingModule)
                .inheritResourceAttributeDefinitions().end();
        child.discardChildResource(PathElement.pathElement(Constants.MAPPING_MODULE));

        final ModulesToAttributeTransformer providerModule = new ModulesToAttributeTransformer(Constants.PROVIDER_MODULE, Constants.PROVIDER_MODULES);

        child = securityDomain.addChildResource(PATH_AUDIT_CLASSIC)
                .setCustomResourceTransformer(providerModule)
                .addOperationTransformationOverride(ModelDescriptionConstants.ADD)
                .setCustomOperationTransformer(providerModule)
                .inheritResourceAttributeDefinitions().end();
        child.discardChildResource(PathElement.pathElement(Constants.PROVIDER_MODULE));

        final ModulesToAttributeTransformer authModule = new ModulesToAttributeTransformer(Constants.AUTH_MODULE, Constants.AUTH_MODULES);

        ResourceTransformationDescriptionBuilder jaspiReg = securityDomain.addChildResource(PATH_JASPI_AUTH);
        jaspiReg.setCustomResourceTransformer(authModule)
                .addOperationTransformationOverride(ModelDescriptionConstants.ADD)
                .setCustomOperationTransformer(authModule)
                .inheritResourceAttributeDefinitions().end();
        jaspiReg.discardChildResource(PathElement.pathElement(Constants.AUTH_MODULE));

        child = jaspiReg.addChildResource(PATH_LOGIN_MODULE_STACK)
                .setCustomResourceTransformer(loginModule)
                .addOperationTransformationOverride(ModelDescriptionConstants.ADD)
                .setCustomOperationTransformer(loginModule)
                .inheritResourceAttributeDefinitions().end();
        child.discardChildResource(PathElement.pathElement(Constants.LOGIN_MODULE));

        //reject expressions
        securityDomain.getAttributeBuilder().addRejectCheck(RejectAttributeChecker.SIMPLE_EXPRESSIONS, SecurityDomainResourceDefinition.CACHE_TYPE).end();
        builder.addChildResource(VAULT_PATH).getAttributeBuilder()
                .addRejectCheck(RejectAttributeChecker.SIMPLE_EXPRESSIONS, VaultResourceDefinition.CODE)
                .addRejectCheck(RejectAttributeChecker.SIMPLE_LIST_EXPRESSIONS, VaultResourceDefinition.OPTIONS)
                .end();
        builder.addChildResource(JSSE_PATH).getAttributeBuilder()
                .addRejectCheck(new RejectAttributeChecker.ObjectFieldsRejectAttributeChecker(
                        Collections.singletonMap(JSSEResourceDefinition.ADDITIONAL_PROPERTIES.getName(), RejectAttributeChecker.SIMPLE_EXPRESSIONS))
                        , JSSEResourceDefinition.ADDITIONAL_PROPERTIES
                ).end();

        TransformationDescription.Tools.register(builder.build(), subsystemRegistration, ModelVersion.create(1, 1, 0));
    }


    private static void transformModulesToAttributes(final PathAddress address, final String newName, final String oldName, final TransformationContext context, final ModelNode model) {
        ModelNode modules = model.get(oldName).setEmptyList();
        for (Resource.ResourceEntry entry : context.readResourceFromRoot(address).getChildren(newName)) {
            Resource moduleResource = context.readResourceFromRoot(address.append(entry.getPathElement()));
            modules.add(moduleResource.getModel());
        }
    }

    private static class ModulesToAttributeTransformer implements CombinedTransformer {
        private final String resourceName;
        private final String oldName;

        ModulesToAttributeTransformer(String resourceName, String oldName) {
            this.resourceName = resourceName;
            this.oldName = oldName;
        }

        @Override
        public TransformedOperation transformOperation(TransformationContext context, PathAddress address, ModelNode operation) throws OperationFailedException {
            transformModulesToAttributes(address, resourceName, oldName, context, operation);
            return new TransformedOperation(operation, OperationResultTransformer.ORIGINAL_RESULT);
        }

        @Override
        public void transformResource(ResourceTransformationContext context, PathAddress address, Resource resource) throws OperationFailedException {
            ModelNode model = new ModelNode();
            transformModulesToAttributes(address, resourceName, oldName, context, model);
            resource.writeModel(model);
            final ResourceTransformationContext childContext = context.addTransformedResource(PathAddress.EMPTY_ADDRESS, resource);
            childContext.processChildren(resource);
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

QDox parse error in file://<WORKSPACE>/data/code-rep-dataset/Dataset3/Tasks/11541.java