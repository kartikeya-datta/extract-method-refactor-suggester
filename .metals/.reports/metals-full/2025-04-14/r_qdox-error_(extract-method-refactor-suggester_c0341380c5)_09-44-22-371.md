error id: file://<WORKSPACE>/data/code-rep-dataset/Dataset3/Tasks/8366.java
file://<WORKSPACE>/data/code-rep-dataset/Dataset3/Tasks/8366.java
### com.thoughtworks.qdox.parser.ParseException: syntax error @[1,21]

error in qdox parser
file content:
```java
offset: 21
uri: file://<WORKSPACE>/data/code-rep-dataset/Dataset3/Tasks/8366.java
text:
```scala
public static final S@@tring SUBSYSTEM_NAME = "ee";

/*
 * JBoss, Home of Professional Open Source.
 * Copyright 2010, Red Hat, Inc., and individual contributors
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

package org.jboss.as.ee.subsystem;

import static org.jboss.as.controller.descriptions.ModelDescriptionConstants.ADD;
import static org.jboss.as.controller.descriptions.ModelDescriptionConstants.DESCRIBE;
import static org.jboss.as.controller.descriptions.ModelDescriptionConstants.NAME;
import static org.jboss.as.controller.descriptions.ModelDescriptionConstants.OP;
import static org.jboss.as.controller.descriptions.ModelDescriptionConstants.OP_ADDR;
import static org.jboss.as.controller.descriptions.ModelDescriptionConstants.REMOVE;
import static org.jboss.as.controller.parsing.ParseUtils.missingRequired;
import static org.jboss.as.controller.parsing.ParseUtils.requireNoAttributes;
import static org.jboss.as.controller.parsing.ParseUtils.requireNoContent;
import static org.jboss.as.controller.parsing.ParseUtils.requireNoNamespaceAttribute;
import static org.jboss.as.controller.parsing.ParseUtils.unexpectedAttribute;
import static org.jboss.as.controller.parsing.ParseUtils.unexpectedElement;

import java.util.Collections;
import java.util.EnumSet;
import java.util.List;
import java.util.Locale;

import javax.xml.stream.Location;
import javax.xml.stream.XMLStreamConstants;
import javax.xml.stream.XMLStreamException;

import org.jboss.as.controller.Extension;
import org.jboss.as.controller.ExtensionContext;
import org.jboss.as.controller.OperationContext;
import org.jboss.as.controller.OperationFailedException;
import org.jboss.as.controller.OperationStepHandler;
import org.jboss.as.controller.PathAddress;
import org.jboss.as.controller.SubsystemRegistration;
import org.jboss.as.controller.descriptions.DescriptionProvider;
import org.jboss.as.controller.descriptions.ModelDescriptionConstants;
import org.jboss.as.controller.descriptions.common.CommonDescriptions;
import org.jboss.as.controller.parsing.ExtensionParsingContext;
import org.jboss.as.controller.persistence.SubsystemMarshallingContext;
import org.jboss.as.controller.registry.ManagementResourceRegistration;
import org.jboss.as.controller.registry.OperationEntry;
import org.jboss.as.ee.component.deployers.DefaultEarSubDeploymentsIsolationProcessor;
import org.jboss.as.ee.structure.GlobalModuleDependencyProcessor;
import org.jboss.dmr.ModelNode;
import org.jboss.staxmapper.XMLElementReader;
import org.jboss.staxmapper.XMLElementWriter;
import org.jboss.staxmapper.XMLExtendedStreamReader;
import org.jboss.staxmapper.XMLExtendedStreamWriter;

/**
 * JBossAS domain extension used to initialize the ee subsystem handlers and associated classes.
 *
 * @author Weston M. Price
 * @author Emanuel Muckenhuber
 */
public class EeExtension implements Extension {

    public static final String NAMESPACE = "urn:jboss:domain:ee:1.0";
    private static final String SUBSYSTEM_NAME = "ee";
    private static final EESubsystemParser parser = new EESubsystemParser();

    /**
     * {@inheritDoc}
     */
    @Override
    public void initialize(ExtensionContext context) {
        final SubsystemRegistration subsystem = context.registerSubsystem(SUBSYSTEM_NAME);
        final ManagementResourceRegistration registration = subsystem.registerSubsystemModel(new DescriptionProvider() {

            public ModelNode getModelDescription(final Locale locale) {
                return EeSubsystemDescriptions.getSubsystemDescription(locale);
            }
        });
        final DefaultEarSubDeploymentsIsolationProcessor isolationProcessor = new DefaultEarSubDeploymentsIsolationProcessor();
        final GlobalModuleDependencyProcessor moduleDependencyProcessor = new GlobalModuleDependencyProcessor();
        final EeSubsystemAdd subsystemAdd = new EeSubsystemAdd(isolationProcessor, moduleDependencyProcessor);
        registration.registerOperationHandler(ADD, subsystemAdd, subsystemAdd, false);
        registration.registerOperationHandler(REMOVE, EeSubsystemRemove.INSTANCE, EeSubsystemRemove.INSTANCE, false,
                OperationEntry.EntryType.PUBLIC, EnumSet.of(OperationEntry.Flag.RESTART_ALL_SERVICES));
        registration.registerOperationHandler(DESCRIBE, EESubsystemDescribeHandler.INSTANCE, EESubsystemDescribeHandler.INSTANCE, false, OperationEntry.EntryType.PRIVATE);
        EeWriteAttributeHandler writeHandler = new EeWriteAttributeHandler(isolationProcessor, moduleDependencyProcessor);
        writeHandler.registerAttributes(registration);

        subsystem.registerXMLElementWriter(parser);
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public void initializeParsers(ExtensionParsingContext context) {
        context.setSubsystemXmlMapping(NAMESPACE, parser);
    }

    static ModelNode createEESubSystemAddOperation() {
        final ModelNode subsystem = new ModelNode();
        subsystem.get(OP).set(ADD);
        subsystem.get(OP_ADDR).add(ModelDescriptionConstants.SUBSYSTEM, SUBSYSTEM_NAME);
        return subsystem;
    }


    static class EESubsystemParser implements XMLStreamConstants, XMLElementReader<List<ModelNode>>, XMLElementWriter<SubsystemMarshallingContext> {

        /**
         * {@inheritDoc}
         */
        @Override
        public void writeContent(XMLExtendedStreamWriter writer, SubsystemMarshallingContext context) throws XMLStreamException {
            //TODO seems to be a problem with empty elements cleaning up the queue in FormattingXMLStreamWriter.runAttrQueue
            //context.startSubsystemElement(NewEeExtension.NAMESPACE, true);
            context.startSubsystemElement(EeExtension.NAMESPACE, false);

            ModelNode eeSubSystem = context.getModelNode();
            CommonAttributes.EAR_SUBDEPLOYMENTS_ISOLATED.marshallAsElement(eeSubSystem, writer);
            GlobalModulesDefinition.INSTANCE.marshallAsElement(eeSubSystem, writer);

            writer.writeEndElement();

        }

        /**
         * {@inheritDoc}
         */
        @Override
        public void readElement(XMLExtendedStreamReader reader, List<ModelNode> list) throws XMLStreamException {
            // EE subsystem doesn't have any attributes, so make sure that the xml doesn't have any
            requireNoAttributes(reader);

            final ModelNode eeSubSystem = createEESubSystemAddOperation();
            // add the subsytem to the ModelNode(s)
            list.add(eeSubSystem);

            // elements
            final EnumSet<Element> encountered = EnumSet.noneOf(Element.class);
            while (reader.hasNext() && reader.nextTag() != END_ELEMENT) {
                switch (Namespace.forUri(reader.getNamespaceURI())) {
                    case EE_1_0: {
                        final Element element = Element.forName(reader.getLocalName());
                        if (!encountered.add(element)) {
                            throw unexpectedElement(reader);
                        }
                        switch (element) {
                            case GLOBAL_MODULES: {
                                final ModelNode model = parseGlobalModules(reader);
                                eeSubSystem.get(CommonAttributes.GLOBAL_MODULES).set(model);
                                break;
                            }
                            case EAR_SUBDEPLOYMENTS_ISOLATED: {
                                Location location = reader.getLocation();
                                final String earSubDeploymentsIsolated = this.parseEarSubDeploymentsIsolatedElement(reader);
                                // set the ear subdeployment isolation on the subsystem operation
                                CommonAttributes.EAR_SUBDEPLOYMENTS_ISOLATED.parseAndSetParameter(earSubDeploymentsIsolated, eeSubSystem, location);
                                break;
                            }
                            default: {
                                throw unexpectedElement(reader);
                            }
                        }
                        break;
                    }
                    default: {
                        throw unexpectedElement(reader);
                    }
                }
            }
        }

        static ModelNode parseGlobalModules(XMLExtendedStreamReader reader) throws XMLStreamException {

            ModelNode globalModules = new ModelNode();

            requireNoAttributes(reader);

            while (reader.hasNext() && reader.nextTag() != END_ELEMENT) {
                switch (Element.forName(reader.getLocalName())) {
                    case MODULE: {
                        final int count = reader.getAttributeCount();
                        String name = null;
                        String slot = null;
                        for (int i = 0; i < count; i++) {
                            requireNoNamespaceAttribute(reader, i);
                            final String value = reader.getAttributeValue(i);
                            final Attribute attribute = Attribute.forName(reader.getAttributeLocalName(i));
                            switch (attribute) {
                                case NAME:
                                    if (name != null) {
                                        throw unexpectedAttribute(reader, i);
                                    }
                                    name = value;
                                    break;
                                case SLOT:
                                    if (slot != null) {
                                        throw unexpectedAttribute(reader, i);
                                    }
                                    slot = value;
                                    break;
                                default:
                                    unexpectedAttribute(reader, i);
                            }
                        }
                        if (name == null) {
                            missingRequired(reader, Collections.singleton(NAME));
                        }
                        if (slot == null) {
                            slot = "main";
                        }
                        final ModelNode module = new ModelNode();
                        module.get(CommonAttributes.NAME).set(name);
                        module.get(CommonAttributes.SLOT).set(slot);
                        globalModules.add(module);
                        requireNoContent(reader);
                        break;
                    }
                    default: {
                        unexpectedElement(reader);
                    }
                }
            }
            return globalModules;
        }

        static String parseEarSubDeploymentsIsolatedElement(XMLExtendedStreamReader reader) throws XMLStreamException {

            // we don't expect any attributes for this element.
            requireNoAttributes(reader);

            final String value = reader.getElementText();
            if (value == null || value.trim().isEmpty()) {
                throw new XMLStreamException("Invalid value: " + value + " for '" + Element.EAR_SUBDEPLOYMENTS_ISOLATED + "' element", reader.getLocation());
            }
            return value.trim();
        }
    }

    private static class EESubsystemDescribeHandler implements OperationStepHandler, DescriptionProvider {
        static final EESubsystemDescribeHandler INSTANCE = new EESubsystemDescribeHandler();

        public void execute(OperationContext context, ModelNode operation) throws OperationFailedException {
            final ModelNode model = context.readModel(PathAddress.EMPTY_ADDRESS);
            final ModelNode op = createEESubSystemAddOperation();
            if (model.hasDefined(CommonAttributes.GLOBAL_MODULES)) {
                op.get(CommonAttributes.GLOBAL_MODULES).set(model.get(CommonAttributes.GLOBAL_MODULES));
            }
            if (model.hasDefined(Element.EAR_SUBDEPLOYMENTS_ISOLATED.getLocalName())) {
                op.get(Element.EAR_SUBDEPLOYMENTS_ISOLATED.getLocalName()).set(model.get(Element.EAR_SUBDEPLOYMENTS_ISOLATED.getLocalName()));
            }
            context.getResult().add(op);
            context.completeStep();
        }

        @Override
        public ModelNode getModelDescription(Locale locale) {
            return CommonDescriptions.getSubsystemDescribeOperation(locale);
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

QDox parse error in file://<WORKSPACE>/data/code-rep-dataset/Dataset3/Tasks/8366.java