error id: file://<WORKSPACE>/data/code-rep-dataset/Dataset3/Tasks/5818.java
file://<WORKSPACE>/data/code-rep-dataset/Dataset3/Tasks/5818.java
### com.thoughtworks.qdox.parser.ParseException: syntax error @[1,1]

error in qdox parser
file content:
```java
offset: 1
uri: file://<WORKSPACE>/data/code-rep-dataset/Dataset3/Tasks/5818.java
text:
```scala
s@@uper(true, ATTRIBUTE_DEFINITIONS);

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

package org.jboss.as.domain.management.security;

import static org.jboss.as.domain.management.DomainManagementMessages.MESSAGES;

import org.jboss.as.controller.AttributeDefinition;
import org.jboss.as.controller.OperationContext;
import org.jboss.as.controller.OperationFailedException;
import org.jboss.as.controller.OperationStepHandler;
import org.jboss.as.controller.PathAddress;
import org.jboss.as.controller.PathElement;
import org.jboss.as.controller.SimpleAttributeDefinition;
import org.jboss.as.controller.SimpleAttributeDefinitionBuilder;
import org.jboss.as.controller.descriptions.ModelDescriptionConstants;
import org.jboss.as.controller.descriptions.common.ControllerResolver;
import org.jboss.as.controller.operations.validation.StringLengthValidator;
import org.jboss.as.controller.registry.AttributeAccess;
import org.jboss.as.controller.registry.ManagementResourceRegistration;
import org.jboss.as.controller.registry.OperationEntry;
import org.jboss.as.controller.registry.Resource;
import org.jboss.dmr.ModelNode;
import org.jboss.dmr.ModelType;

/**
 * {@link org.jboss.as.controller.ResourceDefinition} for a management security realm's LDAP-based authentication resource.
 *
 * @author Brian Stansberry (c) 2011 Red Hat Inc.
 */
public class LdapAuthenticationResourceDefinition extends LdapResourceDefinition {

    public static final SimpleAttributeDefinition BASE_DN = new SimpleAttributeDefinitionBuilder(ModelDescriptionConstants.BASE_DN, ModelType.STRING, false)
            .setValidator(new StringLengthValidator(1, Integer.MAX_VALUE, false, false))
            .setAllowExpression(true)
            .setFlags(AttributeAccess.Flag.RESTART_RESOURCE_SERVICES)
            .build();

    public static final SimpleAttributeDefinition RECURSIVE = new SimpleAttributeDefinitionBuilder(ModelDescriptionConstants.RECURSIVE, ModelType.BOOLEAN, true)
            .setDefaultValue(new ModelNode(false))
            .setAllowExpression(true)
            .setFlags(AttributeAccess.Flag.RESTART_RESOURCE_SERVICES)
            .build();

    public static final SimpleAttributeDefinition USER_DN = new SimpleAttributeDefinitionBuilder(ModelDescriptionConstants.USER_DN, ModelType.STRING, true)
            .setValidator(new StringLengthValidator(1, Integer.MAX_VALUE, true, false))
            .setDefaultValue(new ModelNode(UserLdapCallbackHandler.DEFAULT_USER_DN))
            .setAllowExpression(true)
            .setFlags(AttributeAccess.Flag.RESTART_RESOURCE_SERVICES)
            .build();

    public static final SimpleAttributeDefinition ALLOW_EMPTY_PASSWORDS = new SimpleAttributeDefinitionBuilder(ModelDescriptionConstants.ALLOW_EMPTY_PASSWORDS, ModelType.BOOLEAN, true)
            .setDefaultValue(new ModelNode(false))
            .setAllowExpression(true)
            .setFlags(AttributeAccess.Flag.RESTART_RESOURCE_SERVICES)
            .build();

    public static final SimpleAttributeDefinition USERNAME_FILTER = new SimpleAttributeDefinitionBuilder(ModelDescriptionConstants.USERNAME_ATTRIBUTE, ModelType.STRING, false)
            .setXmlName("attribute")
            .setAlternatives(ModelDescriptionConstants.ADVANCED_FILTER)
            .setValidator(new StringLengthValidator(1, Integer.MAX_VALUE, true, true))
            .setValidateNull(false)
            .setAllowExpression(true)
            .setFlags(AttributeAccess.Flag.RESTART_RESOURCE_SERVICES).build();

    public static final SimpleAttributeDefinition ADVANCED_FILTER = new SimpleAttributeDefinitionBuilder(ModelDescriptionConstants.ADVANCED_FILTER, ModelType.STRING, false)
            .setXmlName("filter")
            .setAlternatives(ModelDescriptionConstants.USERNAME_ATTRIBUTE)
            .setValidator(new StringLengthValidator(1, Integer.MAX_VALUE, true, false))
            .setValidateNull(false)
            .setAllowExpression(true)
            .setFlags(AttributeAccess.Flag.RESTART_RESOURCE_SERVICES)
            .build();

    public static final AttributeDefinition[] ATTRIBUTE_DEFINITIONS = {
        CONNECTION, BASE_DN, RECURSIVE, USER_DN, ALLOW_EMPTY_PASSWORDS, USERNAME_FILTER, ADVANCED_FILTER
    };

    public LdapAuthenticationResourceDefinition() {
        super(PathElement.pathElement(ModelDescriptionConstants.AUTHENTICATION, ModelDescriptionConstants.LDAP),
                ControllerResolver.getResolver("core.management.security-realm.authentication.ldap"),
                new LdapAuthenticationAddHandler(), new SecurityRealmChildRemoveHandler(true),
                OperationEntry.Flag.RESTART_RESOURCE_SERVICES, OperationEntry.Flag.RESTART_RESOURCE_SERVICES);
    }

    @Override
    public void registerAttributes(ManagementResourceRegistration resourceRegistration) {
        SecurityRealmChildWriteAttributeHandler handler = new LdapAuthenticationWriteHandler();
        handler.registerAttributes(resourceRegistration);
    }

    protected static void validateAttributeCombination(ModelNode operation) throws OperationFailedException {
        boolean usernameFileDefined = operation.hasDefined(ModelDescriptionConstants.USERNAME_ATTRIBUTE);
        boolean advancedFilterDefined = operation.hasDefined(ModelDescriptionConstants.ADVANCED_FILTER);
        if (usernameFileDefined && advancedFilterDefined) {
            throw MESSAGES.operationFailedOnlyOneOfRequired(ModelDescriptionConstants.USERNAME_ATTRIBUTE,
                    ModelDescriptionConstants.ADVANCED_FILTER);
        } else if ((usernameFileDefined || advancedFilterDefined) == false) {
            throw MESSAGES.operationFailedOneOfRequired(ModelDescriptionConstants.USERNAME_ATTRIBUTE,
                    ModelDescriptionConstants.ADVANCED_FILTER);
        }
    }

    private static class LdapAuthenticationWriteHandler extends SecurityRealmChildWriteAttributeHandler {

        private LdapAuthenticationWriteHandler() {
            super(ATTRIBUTE_DEFINITIONS);
        }

        @Override
        public void execute(OperationContext context, ModelNode operation) throws OperationFailedException {
            context.addStep(new OperationStepHandler() {

                @Override
                public void execute(OperationContext context, ModelNode ignored) throws OperationFailedException {
                    final Resource resource = context.readResource(PathAddress.EMPTY_ADDRESS);
                    final ModelNode model = resource.getModel();
                    validateAttributeCombination(model);

                    context.stepCompleted();
                }
            }, OperationContext.Stage.MODEL);
            super.execute(context, operation);
        }

    }

    private static class LdapAuthenticationAddHandler extends SecurityRealmChildAddHandler {

        private LdapAuthenticationAddHandler() {
            super(true, false, ATTRIBUTE_DEFINITIONS);
        }

        @Override
        protected void updateModel(OperationContext context, ModelNode operation) throws OperationFailedException {
            validateAttributeCombination(operation);

            super.updateModel(context, operation);
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

QDox parse error in file://<WORKSPACE>/data/code-rep-dataset/Dataset3/Tasks/5818.java