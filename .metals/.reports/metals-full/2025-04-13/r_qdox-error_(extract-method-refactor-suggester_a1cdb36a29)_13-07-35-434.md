error id: file://<WORKSPACE>/data/code-rep-dataset/Dataset3/Tasks/9963.java
file://<WORKSPACE>/data/code-rep-dataset/Dataset3/Tasks/9963.java
### com.thoughtworks.qdox.parser.ParseException: syntax error @[1,1]

error in qdox parser
file content:
```java
offset: 1
uri: file://<WORKSPACE>/data/code-rep-dataset/Dataset3/Tasks/9963.java
text:
```scala
w@@riter.writeStartElement(AbstractHandlerDefinition.FORMATTER.getXmlName());

/*
 * JBoss, Home of Professional Open Source.
 * Copyright 2013, Red Hat, Inc., and individual contributors
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

package org.jboss.as.logging;

import static org.jboss.as.logging.CommonAttributes.ENABLED;
import static org.jboss.as.logging.CommonAttributes.LEVEL;

import java.util.HashMap;
import java.util.Locale;
import java.util.Map;
import javax.xml.stream.XMLStreamException;
import javax.xml.stream.XMLStreamWriter;

import org.jboss.as.controller.AttributeDefinition;
import org.jboss.as.controller.DefaultAttributeMarshaller;
import org.jboss.as.controller.OperationContext;
import org.jboss.as.controller.OperationFailedException;
import org.jboss.as.controller.PathElement;
import org.jboss.as.controller.SimpleResourceDefinition;
import org.jboss.as.controller.operations.validation.EnumValidator;
import org.jboss.as.controller.operations.validation.IntRangeValidator;
import org.jboss.as.controller.registry.ManagementResourceRegistration;
import org.jboss.as.controller.transform.description.RejectTransformationDescriptionBuilder;
import org.jboss.as.controller.transform.description.ResourceTransformationDescriptionBuilder;
import org.jboss.as.logging.HandlerOperations.HandlerAddOperationStepHandler;
import org.jboss.as.logging.HandlerOperations.LogHandlerWriteAttributeHandler;
import org.jboss.as.logging.resolvers.ModelNodeResolver;
import org.jboss.as.logging.validators.Validators;
import org.jboss.dmr.ModelNode;
import org.jboss.dmr.ModelType;
import org.jboss.logmanager.handlers.SyslogHandler;
import org.jboss.logmanager.handlers.SyslogHandler.Facility;
import org.jboss.logmanager.handlers.SyslogHandler.SyslogType;

/**
 * @author <a href="mailto:jperkins@redhat.com">James R. Perkins</a>
 */
class SyslogHandlerResourceDefinition extends SimpleResourceDefinition {

    static final String SYSLOG_HANDLER = "syslog-handler";
    static final PathElement SYSLOG_HANDLER_PATH = PathElement.pathElement(SYSLOG_HANDLER);

    static final PropertyAttributeDefinition APP_NAME = PropertyAttributeDefinition.Builder.of("app-name", ModelType.STRING, true)
            .setAllowExpression(true)
            .setAttributeMarshaller(ElementAttributeMarshaller.VALUE_ATTRIBUTE_MARSHALLER)
            .setPropertyName("appName")
            .setValidator(Validators.NOT_EMPTY_NULLABLE_STRING_VALIDATOR)
            .build();

    static final PropertyAttributeDefinition FACILITY = PropertyAttributeDefinition.Builder.of("facility", ModelType.STRING, true)
            .setAllowExpression(true)
            .setAttributeMarshaller(ElementAttributeMarshaller.VALUE_ATTRIBUTE_MARSHALLER)
            .setDefaultValue(new ModelNode(FacilityAttribute.USER_LEVEL.toString()))
            .setResolver(FacilityResolver.INSTANCE)
            .setValidator(EnumValidator.create(FacilityAttribute.class, true, true))
            .build();

    static final PropertyAttributeDefinition HOSTNAME = PropertyAttributeDefinition.Builder.of("hostname", ModelType.STRING, true)
            .setAllowExpression(true)
            .setAttributeMarshaller(ElementAttributeMarshaller.VALUE_ATTRIBUTE_MARSHALLER)
            .setValidator(Validators.NOT_EMPTY_NULLABLE_STRING_VALIDATOR)
            .build();

    static final PropertyAttributeDefinition PORT = PropertyAttributeDefinition.Builder.of("port", ModelType.INT, true)
            .setAllowExpression(true)
            .setAttributeMarshaller(ElementAttributeMarshaller.VALUE_ATTRIBUTE_MARSHALLER)
            .setDefaultValue(new ModelNode(514))
            .setValidator(new IntRangeValidator(0, 65535, true, true))
            .build();

    static final PropertyAttributeDefinition SERVER_ADDRESS = PropertyAttributeDefinition.Builder.of("server-address", ModelType.STRING, true)
            .setAllowExpression(true)
            .setAttributeMarshaller(ElementAttributeMarshaller.VALUE_ATTRIBUTE_MARSHALLER)
            .setDefaultValue(new ModelNode("localhost"))
            .setPropertyName("serverHostname")
            .setValidator(Validators.NOT_EMPTY_NULLABLE_STRING_VALIDATOR)
            .build();

    static final PropertyAttributeDefinition SYSLOG_FORMATTER = PropertyAttributeDefinition.Builder.of("syslog-format", ModelType.STRING, true)
            .setAllowExpression(true)
            .setAttributeMarshaller(new DefaultAttributeMarshaller() {
                @Override
                public void marshallAsElement(final AttributeDefinition attribute, final ModelNode resourceModel, final boolean marshallDefault, final XMLStreamWriter writer) throws XMLStreamException {
                    if (isMarshallable(attribute, resourceModel, marshallDefault)) {
                        writer.writeStartElement(CommonAttributes.FORMATTER.getXmlName());
                        writer.writeStartElement(attribute.getXmlName());
                        final String content = resourceModel.get(attribute.getName()).asString();
                        writer.writeAttribute(Attribute.SYSLOG_TYPE.getLocalName(), content);
                        writer.writeEndElement();
                        writer.writeEndElement();
                    }
                }
            })
            .setDefaultValue(new ModelNode(SyslogType.RFC5424.name()))
            .setPropertyName("syslogType")
            .setValidator(EnumValidator.create(SyslogType.class, true, true))
            .build();

    /*
    * Attributes
    */
    static final AttributeDefinition[] ATTRIBUTES = {
            APP_NAME,
            ENABLED,
            FACILITY,
            HOSTNAME,
            LEVEL,
            PORT,
            SERVER_ADDRESS,
            SYSLOG_FORMATTER
    };

    static final HandlerAddOperationStepHandler ADD_HANDLER = new HandlerAddOperationStepHandler(SyslogHandler.class, ATTRIBUTES);
    static final LogHandlerWriteAttributeHandler WRITE_HANDLER = new LogHandlerWriteAttributeHandler(ATTRIBUTES);

    static final SyslogHandlerResourceDefinition INSTANCE = new SyslogHandlerResourceDefinition();

    public SyslogHandlerResourceDefinition() {
        super(SYSLOG_HANDLER_PATH,
                LoggingExtension.getResourceDescriptionResolver(SYSLOG_HANDLER),
                ADD_HANDLER,
                HandlerOperations.REMOVE_HANDLER);
    }

    @Override
    public void registerAttributes(final ManagementResourceRegistration resourceRegistration) {
        for (AttributeDefinition def : ATTRIBUTES) {
            resourceRegistration.registerReadWriteAttribute(def, null, WRITE_HANDLER);
        }
    }

    /**
     * Add the transformers for the custom handler.
     *
     *
     * @param subsystemBuilder      the default subsystem builder
     * @param loggingProfileBuilder the logging profile builder
     *
     * @return the builder created for the resource
     */
    static RejectTransformationDescriptionBuilder addTransformers(final ResourceTransformationDescriptionBuilder subsystemBuilder,
                                                                  final ResourceTransformationDescriptionBuilder loggingProfileBuilder) {
        loggingProfileBuilder.rejectChildResource(SYSLOG_HANDLER_PATH);
        return subsystemBuilder.rejectChildResource(SYSLOG_HANDLER_PATH);
    }

    static enum FacilityAttribute {
        KERNEL("kernel"),
        USER_LEVEL("user-level"),
        MAIL_SYSTEM("mail-system"),
        SYSTEM_DAEMONS("system-daemons"),
        SECURITY("security"),
        SYSLOGD("syslogd"),
        LINE_PRINTER("line-printer"),
        NETWORK_NEWS("network-news"),
        UUCP("uucp"),
        CLOCK_DAEMON("clock-daemon"),
        SECURITY2("security2"),
        FTP_DAEMON("ftp-daemon"),
        NTP("ntp"),
        LOG_AUDIT("log-audit"),
        LOG_ALERT("log-alert"),
        CLOCK_DAEMON2("clock-daemon2"),
        LOCAL_USE_0("local-use-0"),
        LOCAL_USE_1("local-use-1"),
        LOCAL_USE_2("local-use-2"),
        LOCAL_USE_3("local-use-3"),
        LOCAL_USE_4("local-use-4"),
        LOCAL_USE_5("local-use-5"),
        LOCAL_USE_6("local-use-6"),
        LOCAL_USE_7("local-use-7");

        private static final Map<String, FacilityAttribute> MAP;

        static {
            MAP = new HashMap<String, FacilityAttribute>();
            for (FacilityAttribute facilityAttribute : values()) {
                MAP.put(facilityAttribute.toString(), facilityAttribute);
            }
        }

        private final Facility facility;
        private final String value;

        FacilityAttribute(final String value) {
            this.value = value;
            this.facility = Facility.valueOf(value.replace("-", "_").toUpperCase(Locale.ENGLISH));
        }

        public Facility getFacility() {
            return facility;
        }

        public String getValue() {
            return value;
        }

        @Override
        public String toString() {
            return value;
        }

        static FacilityAttribute fromString(final String value) {
            return MAP.get(value);
        }
    }

    static class FacilityResolver implements ModelNodeResolver<String> {
        static final FacilityResolver INSTANCE = new FacilityResolver();

        @Override
        public String resolveValue(final OperationContext context, final ModelNode value) throws OperationFailedException {
            return FacilityAttribute.fromString(value.asString()).getFacility().name();
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

QDox parse error in file://<WORKSPACE>/data/code-rep-dataset/Dataset3/Tasks/9963.java