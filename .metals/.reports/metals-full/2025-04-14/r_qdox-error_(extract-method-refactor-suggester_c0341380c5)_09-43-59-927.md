error id: file://<WORKSPACE>/data/code-rep-dataset/Dataset3/Tasks/549.java
file://<WORKSPACE>/data/code-rep-dataset/Dataset3/Tasks/549.java
### com.thoughtworks.qdox.parser.ParseException: syntax error @[1,1]

error in qdox parser
file content:
```java
offset: 1
uri: file://<WORKSPACE>/data/code-rep-dataset/Dataset3/Tasks/549.java
text:
```scala
M@@anagementRemotingServices.installRemotingManagementEndpoint(target, ManagementRemotingServices.MANAGEMENT_ENDPOINT, "localhost", EndpointService.EndpointType.MANAGEMENT, null, null);

/*
* JBoss, Home of Professional Open Source.
* Copyright 2011, Red Hat Middleware LLC, and individual contributors
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
package org.jboss.as.jmx;

import static org.jboss.as.controller.descriptions.ModelDescriptionConstants.ADD;
import static org.jboss.as.controller.descriptions.ModelDescriptionConstants.DESCRIBE;
import static org.jboss.as.controller.descriptions.ModelDescriptionConstants.FILE_HANDLER;
import static org.jboss.as.controller.descriptions.ModelDescriptionConstants.NAME;
import static org.jboss.as.controller.descriptions.ModelDescriptionConstants.OP;
import static org.jboss.as.controller.descriptions.ModelDescriptionConstants.OP_ADDR;
import static org.jboss.as.controller.descriptions.ModelDescriptionConstants.READ_ATTRIBUTE_OPERATION;
import static org.jboss.as.controller.descriptions.ModelDescriptionConstants.REMOVE;
import static org.jboss.as.controller.descriptions.ModelDescriptionConstants.RESULT;
import static org.jboss.as.controller.descriptions.ModelDescriptionConstants.SUBSYSTEM;
import static org.jboss.as.controller.descriptions.ModelDescriptionConstants.UNDEFINE_ATTRIBUTE_OPERATION;
import static org.jboss.as.controller.descriptions.ModelDescriptionConstants.VALUE;
import static org.jboss.as.controller.descriptions.ModelDescriptionConstants.WRITE_ATTRIBUTE_OPERATION;

import java.util.ArrayList;
import java.util.List;

import javax.management.MBeanServerConnection;
import javax.management.remote.JMXConnector;
import javax.management.remote.JMXConnectorFactory;
import javax.management.remote.JMXServiceURL;
import javax.xml.stream.XMLStreamException;

import org.jboss.as.controller.AttributeDefinition;
import org.jboss.as.controller.ModelVersion;
import org.jboss.as.controller.OperationContext;
import org.jboss.as.controller.OperationFailedException;
import org.jboss.as.controller.OperationStepHandler;
import org.jboss.as.controller.PathAddress;
import org.jboss.as.controller.PathElement;
import org.jboss.as.controller.extension.ExtensionRegistry;
import org.jboss.as.controller.operations.common.Util;
import org.jboss.as.controller.registry.AttributeAccess;
import org.jboss.as.controller.registry.ManagementResourceRegistration;
import org.jboss.as.controller.registry.Resource;
import org.jboss.as.controller.transform.OperationTransformer.TransformedOperation;
import org.jboss.as.domain.management.CoreManagementResourceDefinition;
import org.jboss.as.domain.management.audit.AccessAuditResourceDefinition;
import org.jboss.as.model.test.FailedOperationTransformationConfig;
import org.jboss.as.model.test.FailedOperationTransformationConfig.AttributesPathAddressConfig;
import org.jboss.as.model.test.ModelFixer;
import org.jboss.as.model.test.ModelTestControllerVersion;
import org.jboss.as.model.test.ModelTestUtils;
import org.jboss.as.network.SocketBinding;
import org.jboss.as.remoting.EndpointService;
import org.jboss.as.remoting.RemotingServices;
import org.jboss.as.remoting.management.ManagementRemotingServices;
import org.jboss.as.subsystem.test.AbstractSubsystemTest;
import org.jboss.as.subsystem.test.AdditionalInitialization;
import org.jboss.as.subsystem.test.ControllerInitializer;
import org.jboss.as.subsystem.test.KernelServices;
import org.jboss.as.subsystem.test.KernelServicesBuilder;
import org.jboss.dmr.ModelNode;
import org.jboss.msc.service.ServiceName;
import org.jboss.msc.service.ServiceTarget;
import org.junit.Assert;
import org.junit.Test;
import org.xnio.IoUtils;
import org.xnio.OptionMap;

/**
 *
 * @author <a href="kabir.khan@jboss.com">Kabir Khan</a>
 */
public class JMXSubsystemTestCase extends AbstractSubsystemTest {

    private static final String LAUNCH_TYPE = "launch-type";
    private static final String TYPE_STANDALONE = "STANDALONE";

    public JMXSubsystemTestCase() {
        super(JMXExtension.SUBSYSTEM_NAME, new JMXExtension());
    }

    @Test
    public void testParseEmptySubsystem() throws Exception {
        //Parse the subsystem xml into operations
        String subsystemXml =
                "<subsystem xmlns=\"" + Namespace.CURRENT.getUriString() + "\">" +
                "</subsystem>";
        List<ModelNode> operations = super.parse(subsystemXml);

        ///Check that we have the expected number of operations
        Assert.assertEquals(1, operations.size());

        //Check that each operation has the correct content
        ModelNode addSubsystem = operations.get(0);
        Assert.assertEquals(ADD, addSubsystem.get(OP).asString());
        PathAddress addr = PathAddress.pathAddress(addSubsystem.get(OP_ADDR));
        Assert.assertEquals(1, addr.size());
        PathElement element = addr.getElement(0);
        Assert.assertEquals(SUBSYSTEM, element.getKey());
        Assert.assertEquals(JMXExtension.SUBSYSTEM_NAME, element.getValue());
    }

    @Test
    public void testParseSubsystemWithBadChild() throws Exception {
        //Parse the subsystem xml into operations
        String subsystemXml =
                "<subsystem xmlns=\"" + Namespace.CURRENT.getUriString() + "\">" +
                "   <invalid/>" +
                "</subsystem>";
        try {
            super.parse(subsystemXml);
            Assert.fail("Should not have parsed bad child");
        } catch (XMLStreamException expected) {
        }
    }

    @Test
    public void testParseSubsystemWithBadAttribute() throws Exception {
        //Parse the subsystem xml into operations
        String subsystemXml =
                "<subsystem xmlns=\"" + Namespace.CURRENT.getUriString() + "\" bad=\"very_bad\">" +
                "</subsystem>";
        try {
            super.parse(subsystemXml);
            Assert.fail("Should not have parsed bad attribute");
        } catch (XMLStreamException expected) {
        }
    }

    @Test
    public void testParseSubsystemWithConnector() throws Exception {
        //Parse the subsystem xml into operations
        String subsystemXml =
                "<subsystem xmlns=\"" + Namespace.CURRENT.getUriString() + "\">" +
                "    <remoting-connector use-management-endpoint=\"false\" />" +
                "</subsystem>";
        List<ModelNode> operations = super.parse(subsystemXml);

        ///Check that we have the expected number of operations
        Assert.assertEquals(2, operations.size());

        //Check that each operation has the correct content
        ModelNode addSubsystem = operations.get(0);
        Assert.assertEquals(ADD, addSubsystem.get(OP).asString());
        assertJmxSubsystemAddress(addSubsystem.get(OP_ADDR));

        ModelNode addConnector = operations.get(1);
        Assert.assertEquals(ADD, addConnector.get(OP).asString());
        assertJmxConnectorAddress(addConnector.get(OP_ADDR));
    }

    @Test
    public void testParseSubsystemWithTwoConnectors() throws Exception {
        //Parse the subsystem xml into operations
        String subsystemXml =
                "<subsystem xmlns=\"" + Namespace.CURRENT.getUriString() + "\">" +
                "<remoting-connector/>" +
                "<remoting-connector/>" +
                "</subsystem>";
        try {
            super.parse(subsystemXml);
            Assert.fail("Should not have parsed second connector");
        } catch (XMLStreamException expected) {
        }
    }

    @Test
    public void testParseSubsystemWithBadConnectorAttribute() throws Exception {
        //Parse the subsystem xml into operations
        String subsystemXml =
                "<subsystem xmlns=\"" + Namespace.CURRENT.getUriString() + "\">" +
                "<remoting-connector bad=\"verybad\"/>" +
                "</subsystem>";
        try {
            super.parse(subsystemXml);
            Assert.fail("Should not have parsed bad attribute");
        } catch (XMLStreamException expected) {
        }
    }

    @Test
    public void testInstallIntoController() throws Exception {
        //Parse the subsystem xml and install into the controller
        String subsystemXml =
                "<subsystem xmlns=\"" + Namespace.CURRENT.getUriString() + "\">" +
                "<remoting-connector/>" +
                "</subsystem>";
        KernelServices services = createKernelServicesBuilder(new BaseAdditionalInitalization())
                .setSubsystemXml(subsystemXml)
                .build();

        Assert.assertTrue(services.isSuccessfulBoot());

        //Read the whole model and make sure it looks as expected
        ModelNode model = services.readWholeModel();
        Assert.assertTrue(model.get(SUBSYSTEM).hasDefined(JMXExtension.SUBSYSTEM_NAME));

        //Make sure that we can connect to the MBean server
        int port = 12345;
        String urlString = System.getProperty("jmx.service.url",
            "service:jmx:remoting-jmx://localhost:" + port);
        JMXServiceURL serviceURL = new JMXServiceURL(urlString);

        JMXConnector connector = null;
        try {
            MBeanServerConnection connection = null;
            long end = System.currentTimeMillis() + 10000;
            do {
                try {
                    connector = JMXConnectorFactory.connect(serviceURL, null);
                    connection = connector.getMBeanServerConnection();
                } catch (Exception e) {
                    e.printStackTrace();
                    Thread.sleep(500);
                }
            } while (connection == null && System.currentTimeMillis() < end);
            Assert.assertNotNull(connection);
            connection.getMBeanCount();
        } finally {
            IoUtils.safeClose(connector);
        }

        super.assertRemoveSubsystemResources(services);
    }



    @Test
    public void testParseAndMarshalModel1_0() throws Exception {
        //Parse the subsystem xml and install into the first controller
        String subsystemXml =
                "<subsystem xmlns=\"" + Namespace.JMX_1_0.getUriString() + "\">" +
                "    <jmx-connector registry-binding=\"registry1\" server-binding=\"server1\" />" +
                "</subsystem>";
        String finishedSubsystemXml =
                        "<subsystem xmlns=\"" + Namespace.JMX_1_0.getUriString() + "\"/>";
        AdditionalInitialization additionalInit = new BaseAdditionalInitalization();

        KernelServices servicesA = createKernelServicesBuilder(additionalInit).setSubsystemXml(subsystemXml).build();
        //Get the model and the persisted xml from the first controller
        ModelNode modelA = servicesA.readWholeModel();
        String marshalled = servicesA.getPersistedSubsystemXml();
        servicesA.shutdown();

        Assert.assertTrue(marshalled.contains(Namespace.CURRENT.getUriString()));

        compareXml(null, finishedSubsystemXml, marshalled, true);

        //Install the persisted xml from the first controller into a second controller
        KernelServices servicesB = createKernelServicesBuilder(additionalInit).setSubsystemXml(marshalled).build();
        ModelNode modelB = servicesB.readWholeModel();

        //Make sure the models from the two controllers are identical
        super.compare(modelA, modelB);
    }

    @Test
    public void testParseAndMarshalModel1_1WithShowModel() throws Exception {
        //Parse the subsystem xml and install into the first controller
        String subsystemXml =
                "<subsystem xmlns=\"" + Namespace.JMX_1_1.getUriString() + "\">" +
                "<show-model value=\"true\"/>" +
                "</subsystem>";

        String finishedXml =
                "<subsystem xmlns=\"" + Namespace.CURRENT.getUriString() + "\">" +
                "    <expose-resolved-model proper-property-format=\"false\"/>" +
                "</subsystem>";

        AdditionalInitialization additionalInit = new BaseAdditionalInitalization();

        KernelServices servicesA = createKernelServicesBuilder(additionalInit).setSubsystemXml(subsystemXml).build();
        //Get the model and the persisted xml from the first controller
        ModelNode modelA = servicesA.readWholeModel();
        String marshalled = servicesA.getPersistedSubsystemXml();
        servicesA.shutdown();

        Assert.assertTrue(marshalled.contains(Namespace.CURRENT.getUriString()));
        compareXml(null, finishedXml, marshalled, true);

        //Install the persisted xml from the first controller into a second controller
        KernelServices servicesB = createKernelServicesBuilder(additionalInit).setSubsystemXml(marshalled).build();
        ModelNode modelB = servicesB.readWholeModel();

        //Make sure the models from the two controllers are identical
        super.compare(modelA, modelB);
    }


    @Test
    public void testParseAndMarshalModelWithRemoteConnectorRef1_1() throws Exception {
        //Parse the subsystem xml and install into the first controller
        String subsystemXml =
                "<subsystem xmlns=\"" + Namespace.JMX_1_1.getUriString() + "\">" +
                "<remoting-connector/> " +
                "</subsystem>";


        AdditionalInitialization additionalInit = new BaseAdditionalInitalization();

        KernelServices servicesA = createKernelServicesBuilder(additionalInit).setSubsystemXml(subsystemXml).build();
        //Get the model and the persisted xml from the first controller
        ModelNode modelA = servicesA.readWholeModel();
        String marshalled = servicesA.getPersistedSubsystemXml();
        servicesA.shutdown();

        compareXml(null, subsystemXml, marshalled, true);

        //Install the persisted xml from the first controller into a second controller
        KernelServices servicesB = createKernelServicesBuilder(additionalInit).setSubsystemXml(marshalled).build();
        ModelNode modelB = servicesB.readWholeModel();

        //Make sure the models from the two controllers are identical
        super.compare(modelA, modelB);
    }

    @Test
    public void testParseAndMarshalModel1_1() throws Exception {
        //Parse the subsystem xml and install into the first controller
        String subsystemXml =
                "<subsystem xmlns=\"" + Namespace.JMX_1_1.getUriString() + "\">" +
                "<show-model value=\"true\"/>" +
                "<remoting-connector/>" +
                "</subsystem>";

        String finishedXml =
                "<subsystem xmlns=\"" + Namespace.CURRENT.getUriString() + "\">" +
                "    <expose-resolved-model proper-property-format=\"false\"/>" +
                "    <remoting-connector/>" +
                "</subsystem>";

        AdditionalInitialization additionalInit = new BaseAdditionalInitalization();

        KernelServices servicesA = createKernelServicesBuilder(additionalInit).setSubsystemXml(subsystemXml).build();
        //Get the model and the persisted xml from the first controller
        ModelNode modelA = servicesA.readWholeModel();
        String marshalled = servicesA.getPersistedSubsystemXml();
        servicesA.shutdown();

        Assert.assertTrue(marshalled.contains(Namespace.CURRENT.getUriString()));
        compareXml(null, finishedXml, marshalled, true);

        //Install the persisted xml from the first controller into a second controller
        KernelServices servicesB = createKernelServicesBuilder(additionalInit).setSubsystemXml(marshalled).build();
        ModelNode modelB = servicesB.readWholeModel();

        //Make sure the models from the two controllers are identical
        super.compare(modelA, modelB);
    }

    @Test
    public void testParseAndMarshalModel1_2WithShowModels() throws Exception {
        //Parse the subsystem xml and install into the first controller
        String subsystemXml =
                "<subsystem xmlns=\"" + Namespace.JMX_1_2.getUriString() + "\">" +
                "   <expose-resolved-model domain-name=\"jboss.RESOLVED\"/>" +
                "   <expose-expression-model domain-name=\"jboss.EXPRESSION\"/>" +
                "</subsystem>";

        AdditionalInitialization additionalInit = new BaseAdditionalInitalization();

        KernelServices servicesA = createKernelServicesBuilder(additionalInit).setSubsystemXml(subsystemXml).build();
        //Get the model and the persisted xml from the first controller
        ModelNode modelA = servicesA.readWholeModel();
        String marshalled = servicesA.getPersistedSubsystemXml();
        servicesA.shutdown();

        Assert.assertTrue(marshalled.contains(Namespace.CURRENT.getUriString()));
        compareXml(null, subsystemXml, marshalled, true);

        //Install the persisted xml from the first controller into a second controller
        KernelServices servicesB = createKernelServicesBuilder(additionalInit).setSubsystemXml(marshalled).build();
        ModelNode modelB = servicesB.readWholeModel();

        //Make sure the models from the two controllers are identical
        super.compare(modelA, modelB);
    }

    @Test
    public void testParseAndMarshalModel1_2WithShowModelsAndOldPropertyFormat() throws Exception {
        //Parse the subsystem xml and install into the first controller
        String subsystemXml =
                "<subsystem xmlns=\"" + Namespace.JMX_1_2.getUriString() + "\">" +
                "   <expose-resolved-model domain-name=\"jboss.RESOLVED\" proper-property-format=\"false\"/>" +
                "   <expose-expression-model domain-name=\"jboss.EXPRESSION\"/>" +
                "</subsystem>";

        AdditionalInitialization additionalInit = new BaseAdditionalInitalization();

        KernelServices servicesA = createKernelServicesBuilder(additionalInit).setSubsystemXml(subsystemXml).build();
        Assert.assertTrue(servicesA.isSuccessfulBoot());
        //Get the model and the persisted xml from the first controller
        ModelNode modelA = servicesA.readWholeModel();
        Assert.assertTrue(modelA.get(SUBSYSTEM, "jmx", CommonAttributes.EXPOSE_MODEL, CommonAttributes.RESOLVED).hasDefined(CommonAttributes.PROPER_PROPERTY_FORMAT));
        Assert.assertFalse(modelA.get(SUBSYSTEM, "jmx", CommonAttributes.EXPOSE_MODEL, CommonAttributes.RESOLVED, CommonAttributes.PROPER_PROPERTY_FORMAT).asBoolean());
        String marshalled = servicesA.getPersistedSubsystemXml();
        servicesA.shutdown();

        Assert.assertTrue(marshalled.contains(Namespace.CURRENT.getUriString()));
        compareXml(null, subsystemXml, marshalled, true);

        //Install the persisted xml from the first controller into a second controller
        KernelServices servicesB = createKernelServicesBuilder(additionalInit).setSubsystemXml(marshalled).build();
        ModelNode modelB = servicesB.readWholeModel();

        //Make sure the models from the two controllers are identical
        super.compare(modelA, modelB);
    }


    @Test
    public void testParseAndMarshalModel1_3WithShowModels() throws Exception {
        //Parse the subsystem xml and install into the first controller
        String subsystemXml =
                "<subsystem xmlns=\"" + Namespace.JMX_1_3.getUriString() + "\">" +
                "   <expose-resolved-model domain-name=\"jboss.RESOLVED\"/>" +
                "   <expose-expression-model domain-name=\"jboss.EXPRESSION\"/>" +
                "</subsystem>";

        AdditionalInitialization additionalInit = new BaseAdditionalInitalization();

        KernelServices servicesA = createKernelServicesBuilder(additionalInit).setSubsystemXml(subsystemXml).build();
        //Get the model and the persisted xml from the first controller
        ModelNode modelA = servicesA.readWholeModel();
        String marshalled = servicesA.getPersistedSubsystemXml();
        servicesA.shutdown();

        Assert.assertTrue(marshalled.contains(Namespace.CURRENT.getUriString()));
        compareXml(null, subsystemXml, marshalled, true);

        //Install the persisted xml from the first controller into a second controller
        KernelServices servicesB = createKernelServicesBuilder(additionalInit).setSubsystemXml(marshalled).build();
        ModelNode modelB = servicesB.readWholeModel();

        //Make sure the models from the two controllers are identical
        super.compare(modelA, modelB);
    }

    @Test
    public void testParseAndMarshalModel1_3WithShowModelsAndOldPropertyFormat() throws Exception {
        //Parse the subsystem xml and install into the first controller
        String subsystemXml =
                "<subsystem xmlns=\"" + Namespace.JMX_1_3.getUriString() + "\">" +
                "   <expose-resolved-model domain-name=\"jboss.RESOLVED\" proper-property-format=\"false\"/>" +
                "   <expose-expression-model domain-name=\"jboss.EXPRESSION\"/>" +
                "   <sensitivity non-core-mbeans=\"true\"/>" +
                "</subsystem>";

        AdditionalInitialization additionalInit = new BaseAdditionalInitalization();

        KernelServices servicesA = createKernelServicesBuilder(additionalInit).setSubsystemXml(subsystemXml).build();
        Assert.assertTrue(servicesA.isSuccessfulBoot());
        //Get the model and the persisted xml from the first controller
        ModelNode modelA = servicesA.readWholeModel();
        Assert.assertTrue(modelA.get(SUBSYSTEM, "jmx", CommonAttributes.EXPOSE_MODEL, CommonAttributes.RESOLVED).hasDefined(CommonAttributes.PROPER_PROPERTY_FORMAT));
        Assert.assertFalse(modelA.get(SUBSYSTEM, "jmx", CommonAttributes.EXPOSE_MODEL, CommonAttributes.RESOLVED, CommonAttributes.PROPER_PROPERTY_FORMAT).asBoolean());
        String marshalled = servicesA.getPersistedSubsystemXml();
        servicesA.shutdown();

        Assert.assertTrue(marshalled.contains(Namespace.CURRENT.getUriString()));
        compareXml(null, subsystemXml, marshalled, true);

        //Install the persisted xml from the first controller into a second controller
        KernelServices servicesB = createKernelServicesBuilder(additionalInit).setSubsystemXml(marshalled).build();
        ModelNode modelB = servicesB.readWholeModel();

        //Make sure the models from the two controllers are identical
        super.compare(modelA, modelB);
    }

    @Test
    public void testParseAndMarshallModelWithAuditLogButNoHandlerReferences() throws Exception {
        String subsystemXml =
                "<subsystem xmlns=\"" + Namespace.CURRENT.getUriString() + "\">" +
                "   <expose-resolved-model domain-name=\"jboss.RESOLVED\" proper-property-format=\"false\"/>" +
                "   <expose-expression-model domain-name=\"jboss.EXPRESSION\"/>" +
                "   <audit-log log-boot=\"true\" log-read-only=\"false\" enabled=\"false\"/>" +
                "   <sensitivity non-core-mbeans=\"true\"/>" +
                "</subsystem>";

        AdditionalInitialization additionalInit = new BaseAdditionalInitalization();

        KernelServices servicesA = createKernelServicesBuilder(additionalInit).setSubsystemXml(subsystemXml).build();
        Assert.assertTrue(servicesA.isSuccessfulBoot());
        //Get the model and the persisted xml from the first controller
        ModelNode modelA = servicesA.readWholeModel();
        Assert.assertTrue(modelA.get(SUBSYSTEM, "jmx", CommonAttributes.EXPOSE_MODEL, CommonAttributes.RESOLVED).hasDefined(CommonAttributes.PROPER_PROPERTY_FORMAT));
        Assert.assertFalse(modelA.get(SUBSYSTEM, "jmx", CommonAttributes.EXPOSE_MODEL, CommonAttributes.RESOLVED, CommonAttributes.PROPER_PROPERTY_FORMAT).asBoolean());
        String marshalled = servicesA.getPersistedSubsystemXml();
        servicesA.shutdown();

        Assert.assertTrue(marshalled.contains(Namespace.CURRENT.getUriString()));
        compareXml(null, subsystemXml, marshalled, true);

        //Install the persisted xml from the first controller into a second controller
        KernelServices servicesB = createKernelServicesBuilder(additionalInit).setSubsystemXml(marshalled).build();
        ModelNode modelB = servicesB.readWholeModel();

        //Make sure the models from the two controllers are identical
        super.compare(modelA, modelB);
    }

    @Test
    public void testParseAndMarshallModelWithAuditLogAndHandlerReferences() throws Exception {
        String subsystemXml =
                "<subsystem xmlns=\"" + Namespace.CURRENT.getUriString() + "\">" +
                "   <expose-resolved-model domain-name=\"jboss.RESOLVED\" proper-property-format=\"false\"/>" +
                "   <expose-expression-model domain-name=\"jboss.EXPRESSION\"/>" +
                "   <audit-log log-boot=\"true\" log-read-only=\"false\" enabled=\"false\">" +
                "       <handlers>" +
                "               <handler name=\"test\"/>" +
                "       </handlers>" +
                "   </audit-log>" +
                "</subsystem>";

        AdditionalInitialization additionalInit = new AuditLogInitialization();

        KernelServices servicesA = createKernelServicesBuilder(additionalInit).setSubsystemXml(subsystemXml).build();
        Assert.assertTrue(servicesA.isSuccessfulBoot());
        //Get the model and the persisted xml from the first controller
        ModelNode modelA = servicesA.readWholeModel();
        Assert.assertTrue(modelA.get(SUBSYSTEM, "jmx", CommonAttributes.EXPOSE_MODEL, CommonAttributes.RESOLVED).hasDefined(CommonAttributes.PROPER_PROPERTY_FORMAT));
        Assert.assertFalse(modelA.get(SUBSYSTEM, "jmx", CommonAttributes.EXPOSE_MODEL, CommonAttributes.RESOLVED, CommonAttributes.PROPER_PROPERTY_FORMAT).asBoolean());
        String marshalled = servicesA.getPersistedSubsystemXml();
        servicesA.shutdown();

        Assert.assertTrue(marshalled.contains(Namespace.CURRENT.getUriString()));
        compareXml(null, subsystemXml, marshalled, true);

        //Install the persisted xml from the first controller into a second controller
        KernelServices servicesB = createKernelServicesBuilder(additionalInit).setSubsystemXml(marshalled).build();
        ModelNode modelB = servicesB.readWholeModel();

        //Make sure the models from the two controllers are identical
        super.compare(modelA, modelB);
    }

    @Test
    public void testDescribeHandler() throws Exception {
        //Parse the subsystem xml and install into the first controller
        String subsystemXml =
                "<subsystem xmlns=\"" + Namespace.CURRENT.getUriString() + "\">" +
                "   <expose-resolved-model domain-name=\"jboss.RESOLVED\"/>" +
                "   <expose-expression-model domain-name=\"jboss.EXPRESSION\"/>" +
                "   <remoting-connector />" +
                "</subsystem>";
        KernelServices servicesA = createKernelServicesBuilder(null).setSubsystemXml(subsystemXml).build();
        //Get the model and the describe operations from the first controller
        ModelNode modelA = servicesA.readWholeModel();
        ModelNode describeOp = new ModelNode();
        describeOp.get(OP).set(DESCRIBE);
        describeOp.get(OP_ADDR).set(
                PathAddress.pathAddress(
                        PathElement.pathElement(SUBSYSTEM, JMXExtension.SUBSYSTEM_NAME)).toModelNode());
        List<ModelNode> operations = checkResultAndGetContents(servicesA.executeOperation(describeOp)).asList();
        servicesA.shutdown();

        Assert.assertEquals(4, operations.size());


        //Install the describe options from the first controller into a second controller
        KernelServices servicesB = createKernelServicesBuilder(null).setBootOperations(operations).build();
        ModelNode modelB = servicesB.readWholeModel();

        //Make sure the models from the two controllers are identical
        super.compare(modelA, modelB);
    }

    @Test
    public void testShowModelAlias() throws Exception {
        String subsystemXml =
                "<subsystem xmlns=\"" + Namespace.CURRENT.getUriString() + "\"/>";

        KernelServices services = createKernelServicesBuilder(null).setSubsystemXml(subsystemXml).build();

        ModelNode model = services.readWholeModel();
        Assert.assertFalse(model.get(SUBSYSTEM, JMXExtension.SUBSYSTEM_NAME, CommonAttributes.EXPOSE_MODEL, CommonAttributes.RESOLVED).isDefined());

        ModelNode read = createOperation(READ_ATTRIBUTE_OPERATION);
        read.get(NAME).set(CommonAttributes.SHOW_MODEL);
        Assert.assertFalse(services.executeForResult(read).asBoolean());

        ModelNode write = createOperation(WRITE_ATTRIBUTE_OPERATION);
        write.get(NAME).set(CommonAttributes.SHOW_MODEL);
        write.get(VALUE).set(true);
        services.executeForResult(write);

        model = services.readWholeModel();
        Assert.assertTrue(model.get(SUBSYSTEM, JMXExtension.SUBSYSTEM_NAME, CommonAttributes.EXPOSE_MODEL, CommonAttributes.RESOLVED).isDefined());
        Assert.assertTrue(services.executeForResult(read).asBoolean());

        write.get(VALUE).set(false);
        services.executeForResult(write);

        model = services.readWholeModel();
        Assert.assertFalse(model.get(SUBSYSTEM, JMXExtension.SUBSYSTEM_NAME, CommonAttributes.EXPOSE_MODEL, CommonAttributes.RESOLVED).isDefined());
        Assert.assertFalse(services.executeForResult(read).asBoolean());
    }

    @Test
    public void testTransformationAS712() throws Exception {
        testTransformation_1_0_0(ModelTestControllerVersion.V7_1_2_FINAL);
    }

    @Test
    public void testTransformationAS713() throws Exception {
        testTransformation_1_0_0(ModelTestControllerVersion.V7_1_3_FINAL);
    }

    @Test
    public void testTransformationEAP600() throws Exception {
        testTransformation_1_0_0(ModelTestControllerVersion.EAP_6_0_0);
    }

    @Test
    public void testTransformationAS600() throws Exception {
        testTransformation_1_0_0(ModelTestControllerVersion.EAP_6_0_0);
    }

    private void testTransformation_1_0_0(ModelTestControllerVersion controllerVersion) throws Exception {
        String subsystemXml =
                "<subsystem xmlns=\"" + Namespace.CURRENT.getUriString() + "\">" +
                "   <expose-resolved-model domain-name=\"jboss.as\" proper-property-format=\"false\"/>" +
                "   <remoting-connector />" +
                "   <audit-log enabled=\"false\" log-boot=\"true\" log-read-only=\"true\">" +
                "      <handlers>" +
                "          <handler name=\"test\"/>" +
                "      </handlers>" +
                "   </audit-log>" +
                "</subsystem>";

        ModelVersion oldVersion = ModelVersion.create(1, 0, 0);
        KernelServicesBuilder builder = createKernelServicesBuilder(new AuditLogInitialization()).setSubsystemXml(subsystemXml);
        builder.createLegacyKernelServicesBuilder(null, controllerVersion, oldVersion)
                .setExtensionClassName(JMXExtension.class.getName())
                .addMavenResourceURL("org.jboss.as:jboss-as-jmx:" + controllerVersion.getMavenGavVersion())
                .configureReverseControllerCheck(AdditionalInitialization.MANAGEMENT, new ModelFixer() {
                    @Override
                    public ModelNode fixModel(ModelNode modelNode) {
                        //This is slightly weird...
                        //The reason is that in 7.2 the default behaviour is 'true' which is the a new feature, while 7.1.x uses 'false' under the scenes
                        //So the ops from 7.1.x can never result in 'true'
                        modelNode.get(CommonAttributes.EXPOSE_MODEL, CommonAttributes.RESOLVED, CommonAttributes.PROPER_PROPERTY_FORMAT).set(false);

                        //The audit log operations will get discarded since, so the audit log will not be present in the reverse model
                        ModelNode logger = modelNode.get(JmxAuditLoggerResourceDefinition.PATH_ELEMENT.getKey(), JmxAuditLoggerResourceDefinition.PATH_ELEMENT.getValue());
                        logger.get(JmxAuditLoggerResourceDefinition.LOG_BOOT.getName()).set(true);
                        logger.get(JmxAuditLoggerResourceDefinition.LOG_READ_ONLY.getName()).set(true);
                        logger.get(JmxAuditLoggerResourceDefinition.ENABLED.getName()).set(false);
                        logger.get(JmxAuditLogHandlerReferenceResourceDefinition.PATH_ELEMENT.getKey(), "test").setEmptyObject();

                        return modelNode;
                    }
                });

        KernelServices mainServices = builder.build();
        Assert.assertTrue(mainServices.isSuccessfulBoot());
        KernelServices legacyServices = mainServices.getLegacyServices(oldVersion);
        Assert.assertNotNull(legacyServices);

        ModelFixer modelFixer7_1_x = new ModelFixer() {
            public ModelNode fixModel(ModelNode modelNode) {
                if (modelNode.hasDefined("remoting-connector")) {
                    if (modelNode.get("remoting-connector").hasDefined("jmx")) {
                        if (modelNode.get("remoting-connector", "jmx").keys().size() == 0) {
                            //The default is true, 7.1.x does not include the default for this value
                            modelNode.get("remoting-connector", "jmx", "use-management-endpoint").set(true);
                        }
                    }
                }
                return modelNode;
            }
        };

        ModelNode legacyModel = checkSubsystemModelTransformation(mainServices, oldVersion, modelFixer7_1_x);
        check_1_0_0_Model(legacyModel.get(SUBSYSTEM, JMXExtension.SUBSYSTEM_NAME), true, true);

        //Test that show-model=>expression is ignored
        ModelNode op = createOperation(ADD, CommonAttributes.EXPOSE_MODEL, CommonAttributes.EXPRESSION);
        TransformedOperation transformedOp = mainServices.transformOperation(oldVersion, op);
        Assert.assertTrue(transformedOp.rejectOperation(null));

        op = createOperation(WRITE_ATTRIBUTE_OPERATION, CommonAttributes.EXPOSE_MODEL, CommonAttributes.EXPRESSION);
        op.get(NAME).set(CommonAttributes.DOMAIN_NAME);
        op.get(VALUE).set("discarded");
        transformedOp = mainServices.transformOperation(oldVersion, op);
        Assert.assertTrue(transformedOp.rejectOperation(null));

        op = createOperation(READ_ATTRIBUTE_OPERATION, CommonAttributes.EXPOSE_MODEL, CommonAttributes.EXPRESSION);
        op.get(NAME).set(CommonAttributes.DOMAIN_NAME);
        transformedOp = mainServices.transformOperation(oldVersion, op);
        Assert.assertTrue(transformedOp.rejectOperation(null));

        op = createOperation(REMOVE, CommonAttributes.EXPOSE_MODEL, CommonAttributes.EXPRESSION);
        transformedOp = mainServices.transformOperation(oldVersion, op);
        Assert.assertTrue(transformedOp.rejectOperation(null));

        //Test the show-model=>resolved domain name is rejected if we try to make it anything different from the default
        op = createOperation(WRITE_ATTRIBUTE_OPERATION, CommonAttributes.EXPOSE_MODEL, CommonAttributes.RESOLVED);
        op.get(NAME).set(CommonAttributes.DOMAIN_NAME);
        op.get(VALUE).set("discarded");
        transformedOp = mainServices.transformOperation(oldVersion, op);
        Assert.assertTrue(transformedOp.rejectOperation(null));

        op = createOperation(WRITE_ATTRIBUTE_OPERATION, CommonAttributes.EXPOSE_MODEL, CommonAttributes.RESOLVED);
        op.get(NAME).set(CommonAttributes.DOMAIN_NAME);
        op.get(VALUE).set("jboss.as");
        transformedOp = mainServices.transformOperation(oldVersion, op);
        Assert.assertNull(transformedOp.getTransformedOperation());
        Assert.assertFalse(transformedOp.rejectOperation(null));

        op = createOperation(UNDEFINE_ATTRIBUTE_OPERATION, CommonAttributes.EXPOSE_MODEL, CommonAttributes.RESOLVED);
        op.get(NAME).set(CommonAttributes.DOMAIN_NAME);
        transformedOp = mainServices.transformOperation(oldVersion, op);
        Assert.assertFalse(transformedOp.rejectOperation(null));

        op = createOperation(READ_ATTRIBUTE_OPERATION, CommonAttributes.EXPOSE_MODEL, CommonAttributes.RESOLVED);
        op.get(NAME).set(CommonAttributes.DOMAIN_NAME);
        transformedOp = mainServices.transformOperation(oldVersion, op);
        Assert.assertNull(transformedOp.getTransformedOperation());
        Assert.assertEquals(CommonAttributes.DEFAULT_RESOLVED_DOMAIN, mainServices.executeOperation(oldVersion, transformedOp).get(RESULT).asString());

        op = createOperation(REMOVE, CommonAttributes.EXPOSE_MODEL, CommonAttributes.RESOLVED);
        transformedOp = mainServices.transformOperation(oldVersion, op);
        checkOutcome(mainServices.executeOperation(op));
        checkOutcome(mainServices.executeOperation(oldVersion, transformedOp));
        legacyModel = checkSubsystemModelTransformation(mainServices, oldVersion, modelFixer7_1_x);
        check_1_0_0_Model(legacyModel.get(SUBSYSTEM, getMainSubsystemName()), true, false);

        op = createOperation(ADD, CommonAttributes.EXPOSE_MODEL, CommonAttributes.RESOLVED);
        op.get(CommonAttributes.PROPER_PROPERTY_FORMAT).set(false);
        transformedOp = mainServices.transformOperation(oldVersion, op);
        checkOutcome(mainServices.executeOperation(op));
        checkOutcome(mainServices.executeOperation(oldVersion, transformedOp));
        legacyModel = checkSubsystemModelTransformation(mainServices, oldVersion, modelFixer7_1_x);
        check_1_0_0_Model(legacyModel.get(SUBSYSTEM, getMainSubsystemName()), true, true);

        op = Util.getWriteAttributeOperation(PathAddress.pathAddress(
                    PathElement.pathElement(SUBSYSTEM, getMainSubsystemName()),
                    PathElement.pathElement(CommonAttributes.REMOTING_CONNECTOR, CommonAttributes.JMX)),
                CommonAttributes.USE_MANAGEMENT_ENDPOINT, false);
        ModelTestUtils.checkOutcome(mainServices.executeOperation(oldVersion, mainServices.transformOperation(oldVersion, op)));
        transformedOp = mainServices.transformOperation(oldVersion, op);
        checkOutcome(mainServices.executeOperation(op));
        checkOutcome(mainServices.executeOperation(oldVersion, transformedOp));
        legacyModel = checkSubsystemModelTransformation(mainServices, oldVersion, modelFixer7_1_x);
        check_1_0_0_Model(legacyModel.get(SUBSYSTEM, getMainSubsystemName()), true, true);
        Assert.assertFalse(legacyModel.get(SUBSYSTEM, getMainSubsystemName(), CommonAttributes.REMOTING_CONNECTOR, CommonAttributes.JMX, CommonAttributes.USE_MANAGEMENT_ENDPOINT).asBoolean());

        //Check that these audit log operations get discarded/rejected
        PathAddress address = PathAddress.pathAddress(
                PathElement.pathElement(SUBSYSTEM, getMainSubsystemName()),
                JmxAuditLoggerResourceDefinition.PATH_ELEMENT);
        checkDiscardedOperation(Util.getWriteAttributeOperation(address, JmxAuditLoggerResourceDefinition.LOG_BOOT.getName(), new ModelNode(false)), mainServices, oldVersion);
        checkDiscardedOperation(Util.getUndefineAttributeOperation(address, JmxAuditLoggerResourceDefinition.LOG_BOOT.getName()), mainServices, oldVersion);
        checkDiscardedOperation(Util.getReadAttributeOperation(address, JmxAuditLoggerResourceDefinition.LOG_BOOT.getName()), mainServices, oldVersion);
        checkDiscardedOperation(Util.getWriteAttributeOperation(address, JmxAuditLoggerResourceDefinition.LOG_READ_ONLY.getName(), new ModelNode(false)), mainServices, oldVersion);
        checkDiscardedOperation(Util.getUndefineAttributeOperation(address, JmxAuditLoggerResourceDefinition.LOG_READ_ONLY.getName()), mainServices, oldVersion);
        checkDiscardedOperation(Util.getReadAttributeOperation(address, JmxAuditLoggerResourceDefinition.LOG_READ_ONLY.getName()), mainServices, oldVersion);
        checkRejectedOperation(Util.getWriteAttributeOperation(address, JmxAuditLoggerResourceDefinition.ENABLED.getName(), new ModelNode(true)), mainServices, oldVersion);
        checkRejectedOperation(Util.getUndefineAttributeOperation(address, JmxAuditLoggerResourceDefinition.ENABLED.getName()), mainServices, oldVersion);
        checkDiscardedOperation(Util.getReadAttributeOperation(address, JmxAuditLoggerResourceDefinition.ENABLED.getName()), mainServices, oldVersion);
        checkDiscardedOperation(Util.createRemoveOperation(address), mainServices, oldVersion);
        address = address.append(JmxAuditLogHandlerReferenceResourceDefinition.PATH_ELEMENT.getKey(), "test");
        checkDiscardedOperation(Util.createAddOperation(address), mainServices, oldVersion);
        checkDiscardedOperation(Util.createRemoveOperation(address), mainServices, oldVersion);
    }

    @Test
    public void testTransformationAS720() throws Exception {
        testTransformation_1_1_0(ModelTestControllerVersion.V7_2_0_FINAL);
    }

    @Test
    public void testTransformationEAP610() throws Exception {
        testTransformation_1_1_0(ModelTestControllerVersion.EAP_6_1_0);
    }

    @Test
    public void testTransformationEAP611() throws Exception {
        testTransformation_1_1_0(ModelTestControllerVersion.EAP_6_1_1);
    }

    private void testTransformation_1_1_0(ModelTestControllerVersion controllerVersion) throws Exception {
        String subsystemXml =
                "<subsystem xmlns=\"" + Namespace.CURRENT.getUriString() + "\">" +
                "   <expose-resolved-model domain-name=\"jboss.as\" proper-property-format=\"false\"/>" +
                "   <expose-expression-model domain-name=\"jboss.EXPRESSION\"/>" +
                "   <remoting-connector />" +
                "   <audit-log enabled=\"false\" log-boot=\"true\" log-read-only=\"true\">" +
                "      <handlers>" +
                "          <handler name=\"test\"/>" +
                "      </handlers>" +
                "   </audit-log>" +
                "</subsystem>";

        ModelVersion oldVersion = ModelVersion.create(1, 1, 0);
        KernelServicesBuilder builder = createKernelServicesBuilder(new AuditLogInitialization()).setSubsystemXml(subsystemXml);
        builder.createLegacyKernelServicesBuilder(null, controllerVersion, oldVersion)
                .setExtensionClassName(JMXExtension.class.getName())
                .addMavenResourceURL("org.jboss.as:jboss-as-jmx:" + controllerVersion.getMavenGavVersion())
                .configureReverseControllerCheck(AdditionalInitialization.MANAGEMENT, new ModelFixer() {
                    @Override
                    public ModelNode fixModel(ModelNode modelNode) {
                        //The audit log operations will get discarded since, so the audit log will not be present in the reverse model
                        ModelNode logger = modelNode.get(JmxAuditLoggerResourceDefinition.PATH_ELEMENT.getKey(), JmxAuditLoggerResourceDefinition.PATH_ELEMENT.getValue());
                        logger.get(JmxAuditLoggerResourceDefinition.LOG_BOOT.getName()).set(true);
                        logger.get(JmxAuditLoggerResourceDefinition.LOG_READ_ONLY.getName()).set(true);
                        logger.get(JmxAuditLoggerResourceDefinition.ENABLED.getName()).set(false);
                        logger.get(JmxAuditLogHandlerReferenceResourceDefinition.PATH_ELEMENT.getKey(), "test").setEmptyObject();

                        return modelNode;
                    }
                });

        KernelServices mainServices = builder.build();
        Assert.assertTrue(mainServices.isSuccessfulBoot());
        KernelServices legacyServices = mainServices.getLegacyServices(oldVersion);
        Assert.assertNotNull(legacyServices);

        ModelFixer modelFixer7_1_x = new ModelFixer() {
            public ModelNode fixModel(ModelNode modelNode) {
                if (modelNode.hasDefined("remoting-connector")) {
                    if (modelNode.get("remoting-connector").hasDefined("jmx")) {
                        if (modelNode.get("remoting-connector", "jmx").keys().size() == 0) {
                            //The default is true, 7.1.x does not include the default for this value
                            modelNode.get("remoting-connector", "jmx", "use-management-endpoint").set(true);
                        }
                    }
                }
                return modelNode;
            }
        };

        ModelNode legacyModel = checkSubsystemModelTransformation(mainServices, oldVersion, modelFixer7_1_x);

        ModelNode op = Util.getWriteAttributeOperation(PathAddress.pathAddress(
                    PathElement.pathElement(SUBSYSTEM, getMainSubsystemName()),
                    PathElement.pathElement(CommonAttributes.REMOTING_CONNECTOR, CommonAttributes.JMX)),
                CommonAttributes.USE_MANAGEMENT_ENDPOINT, false);
        ModelTestUtils.checkOutcome(mainServices.executeOperation(oldVersion, mainServices.transformOperation(oldVersion, op)));
        TransformedOperation transformedOp = mainServices.transformOperation(oldVersion, op);
        checkOutcome(mainServices.executeOperation(op));
        checkOutcome(mainServices.executeOperation(oldVersion, transformedOp));
        legacyModel = checkSubsystemModelTransformation(mainServices, oldVersion, modelFixer7_1_x);
        Assert.assertFalse(legacyModel.get(SUBSYSTEM, getMainSubsystemName(), CommonAttributes.REMOTING_CONNECTOR, CommonAttributes.JMX, CommonAttributes.USE_MANAGEMENT_ENDPOINT).asBoolean());

        //Check that these audit log operations get discarded/rejected
        PathAddress address = PathAddress.pathAddress(
                PathElement.pathElement(SUBSYSTEM, getMainSubsystemName()),
                JmxAuditLoggerResourceDefinition.PATH_ELEMENT);
        checkDiscardedOperation(Util.getWriteAttributeOperation(address, JmxAuditLoggerResourceDefinition.LOG_BOOT.getName(), new ModelNode(false)), mainServices, oldVersion);
        checkDiscardedOperation(Util.getUndefineAttributeOperation(address, JmxAuditLoggerResourceDefinition.LOG_BOOT.getName()), mainServices, oldVersion);
        checkDiscardedOperation(Util.getReadAttributeOperation(address, JmxAuditLoggerResourceDefinition.LOG_BOOT.getName()), mainServices, oldVersion);
        checkDiscardedOperation(Util.getWriteAttributeOperation(address, JmxAuditLoggerResourceDefinition.LOG_READ_ONLY.getName(), new ModelNode(false)), mainServices, oldVersion);
        checkDiscardedOperation(Util.getUndefineAttributeOperation(address, JmxAuditLoggerResourceDefinition.LOG_READ_ONLY.getName()), mainServices, oldVersion);
        checkDiscardedOperation(Util.getReadAttributeOperation(address, JmxAuditLoggerResourceDefinition.LOG_READ_ONLY.getName()), mainServices, oldVersion);
        checkRejectedOperation(Util.getWriteAttributeOperation(address, JmxAuditLoggerResourceDefinition.ENABLED.getName(), new ModelNode(true)), mainServices, oldVersion);
        checkRejectedOperation(Util.getUndefineAttributeOperation(address, JmxAuditLoggerResourceDefinition.ENABLED.getName()), mainServices, oldVersion);
        checkDiscardedOperation(Util.getReadAttributeOperation(address, JmxAuditLoggerResourceDefinition.ENABLED.getName()), mainServices, oldVersion);
        checkDiscardedOperation(Util.createRemoveOperation(address), mainServices, oldVersion);
        address = address.append(JmxAuditLogHandlerReferenceResourceDefinition.PATH_ELEMENT.getKey(), "test");
        checkDiscardedOperation(Util.createAddOperation(address), mainServices, oldVersion);
        checkDiscardedOperation(Util.createRemoveOperation(address), mainServices, oldVersion);
    }

    @Test
    public void testRejectExpressionsAS712() throws Exception {
        testRejectExpressions_1_0_0(ModelTestControllerVersion.V7_1_2_FINAL);
    }

    @Test
    public void testRejectExpressionsAS713() throws Exception {
        testRejectExpressions_1_0_0(ModelTestControllerVersion.V7_1_3_FINAL);
    }

    @Test
    public void testRejectExpressionsEAP600() throws Exception {
        testRejectExpressions_1_0_0(ModelTestControllerVersion.EAP_6_0_0);
    }

    @Test
    public void testRejectExpressionsEAP601() throws Exception {
        testRejectExpressions_1_0_0(ModelTestControllerVersion.EAP_6_0_1);
    }

    /**
     * Tests rejection of expressions in 1.1.0 model.
     *
     * @throws Exception
     */
    private void testRejectExpressions_1_0_0(ModelTestControllerVersion controllerVersion) throws Exception {
        String subsystemXml =
            "<subsystem xmlns=\"" + Namespace.CURRENT.getUriString() + "\">" +
                    "   <expose-resolved-model domain-name=\"${test.domain-name:non-standard}\" proper-property-format=\"${test.proper-property-format:true}\"/>" +
                    "   <expose-expression-model domain-name=\"jboss.as\"/>" +
                    "   <remoting-connector use-management-endpoint=\"${test.exp:false}\"/>" +
                    "   <audit-log enabled=\"${test:false}\" log-boot=\"true\" log-read-only=\"true\">" +
                    "      <handlers>" +
                    "          <handler name=\"test\"/>" +
                    "      </handlers>" +
                    "   </audit-log>" +
                    "</subsystem>";

        // create builder for current subsystem version
        KernelServicesBuilder builder = createKernelServicesBuilder(new ManagementAuditLogInitialization());

        // create builder for legacy subsystem version
        ModelVersion version_1_0_0 = ModelVersion.create(1, 0, 0);
        builder.createLegacyKernelServicesBuilder(null, controllerVersion, version_1_0_0)
                .addMavenResourceURL("org.jboss.as:jboss-as-jmx:" + controllerVersion.getMavenGavVersion());

        KernelServices mainServices = builder.build();
        Assert.assertTrue(mainServices.isSuccessfulBoot());
        KernelServices legacyServices = mainServices.getLegacyServices(version_1_0_0);
        Assert.assertNotNull(legacyServices);
        Assert.assertTrue(legacyServices.isSuccessfulBoot());

        PathAddress subsystemAddress = PathAddress.pathAddress(PathElement.pathElement(SUBSYSTEM, JMXExtension.SUBSYSTEM_NAME));
        ModelTestUtils.checkFailedTransformedBootOperations(
                mainServices,
                version_1_0_0,
                builder.parseXml(subsystemXml),
                new FailedOperationTransformationConfig()
                        .addFailedAttribute(
                                subsystemAddress.append(CommonAttributes.EXPOSE_MODEL, CommonAttributes.RESOLVED),
                                new FailedOperationTransformationConfig.ChainedConfig(
                                        createChainedConfigList(
                                                new FailedOperationTransformationConfig.RejectExpressionsConfig(ExposeModelResourceResolved.DOMAIN_NAME),
                                                new CorrectDomainNameConfig(ExposeModelResourceResolved.DOMAIN_NAME),
                                                new CorrectPropertyFormatConfig(ExposeModelResourceResolved.PROPER_PROPERTY_FORMAT)),
                                        ExposeModelResourceResolved.DOMAIN_NAME, ExposeModelResourceResolved.PROPER_PROPERTY_FORMAT))
                        .addFailedAttribute(
                                subsystemAddress.append(CommonAttributes.EXPOSE_MODEL, CommonAttributes.EXPRESSION),
                                FailedOperationTransformationConfig.REJECTED_RESOURCE)
                        .addFailedAttribute(
                                subsystemAddress.append(RemotingConnectorResource.REMOTE_CONNECTOR_CONFIG_PATH),
                                new FailedOperationTransformationConfig.RejectExpressionsConfig(RemotingConnectorResource.USE_MANAGEMENT_ENDPOINT))
                        .addFailedAttribute(
                                subsystemAddress.append(JmxAuditLoggerResourceDefinition.PATH_ELEMENT),
                                new FailedOperationTransformationConfig.ChainedConfig(
                                        createChainedConfigList(
                                            new FailedOperationTransformationConfig.RejectExpressionsConfig(JmxAuditLoggerResourceDefinition.ENABLED),
                                            new ChangeEnabledFromTrueToFalseConfig(JmxAuditLoggerResourceDefinition.ENABLED)), JmxAuditLoggerResourceDefinition.ENABLED)));
    }

    @Test
    public void testRejection720() throws Exception {
        testRejectExpressions_1_1_0(ModelTestControllerVersion.V7_2_0_FINAL);
    }

    @Test
    public void testRejectionEAP610() throws Exception {
        testRejectExpressions_1_1_0(ModelTestControllerVersion.EAP_6_1_0);
    }

    @Test
    public void testRejectionEAP611() throws Exception {
        testRejectExpressions_1_1_0(ModelTestControllerVersion.EAP_6_1_1);
    }

    /**
     * Tests rejection of expressions in 1.1.0 model.
     *
     * @throws Exception
     */
    private void testRejectExpressions_1_1_0(ModelTestControllerVersion controllerVersion) throws Exception {
        String subsystemXml =
            "<subsystem xmlns=\"" + Namespace.CURRENT.getUriString() + "\">" +
                    "   <expose-resolved-model domain-name=\"${test.domain-name:non-standard}\" proper-property-format=\"${test.proper-property-format:true}\"/>" +
                    "   <expose-expression-model domain-name=\"jboss.as\"/>" +
                    "   <remoting-connector use-management-endpoint=\"${test.exp:false}\"/>" +
                    "   <audit-log enabled=\"${test:false}\" log-boot=\"true\" log-read-only=\"true\">" +
                    "      <handlers>" +
                    "          <handler name=\"test\"/>" +
                    "      </handlers>" +
                    "   </audit-log>" +
                    "</subsystem>";

        // create builder for current subsystem version
        KernelServicesBuilder builder = createKernelServicesBuilder(new ManagementAuditLogInitialization());

        // create builder for legacy subsystem version
        ModelVersion version_1_1_0 = ModelVersion.create(1, 1, 0);
        builder.createLegacyKernelServicesBuilder(null, controllerVersion, version_1_1_0)
                .addMavenResourceURL("org.jboss.as:jboss-as-jmx:" + controllerVersion.getMavenGavVersion());

        KernelServices mainServices = builder.build();
        Assert.assertTrue(mainServices.isSuccessfulBoot());
        KernelServices legacyServices = mainServices.getLegacyServices(version_1_1_0);
        Assert.assertNotNull(legacyServices);
        Assert.assertTrue(legacyServices.isSuccessfulBoot());

        PathAddress subsystemAddress = PathAddress.pathAddress(PathElement.pathElement(SUBSYSTEM, JMXExtension.SUBSYSTEM_NAME));
        ModelTestUtils.checkFailedTransformedBootOperations(
                mainServices,
                version_1_1_0,
                builder.parseXml(subsystemXml),
                new FailedOperationTransformationConfig()
                        .addFailedAttribute(
                                subsystemAddress.append(JmxAuditLoggerResourceDefinition.PATH_ELEMENT),
                                new FailedOperationTransformationConfig.ChainedConfig(
                                        createChainedConfigList(
                                            new FailedOperationTransformationConfig.RejectExpressionsConfig(JmxAuditLoggerResourceDefinition.ENABLED),
                                            new ChangeEnabledFromTrueToFalseConfig(JmxAuditLoggerResourceDefinition.ENABLED)), JmxAuditLoggerResourceDefinition.ENABLED)));
    }

    private List<FailedOperationTransformationConfig.AttributesPathAddressConfig<?>> createChainedConfigList(FailedOperationTransformationConfig.AttributesPathAddressConfig<?>...cfgs){
        List<AttributesPathAddressConfig<?>> list = new ArrayList<FailedOperationTransformationConfig.AttributesPathAddressConfig<?>>();
        for (AttributesPathAddressConfig<?> cfg : cfgs) {
            list.add(cfg);
        }
        return list;
    }

    private void check_1_0_0_Model(ModelNode legacySubsystem, boolean remotingConnector, boolean showModel) {
        Assert.assertEquals(2, legacySubsystem.keys().size());
        Assert.assertTrue(legacySubsystem.hasDefined(CommonAttributes.SHOW_MODEL));
        Assert.assertEquals(showModel, legacySubsystem.get(CommonAttributes.SHOW_MODEL).asBoolean());
        Assert.assertTrue(legacySubsystem.hasDefined(CommonAttributes.REMOTING_CONNECTOR));
        Assert.assertTrue(legacySubsystem.get(CommonAttributes.REMOTING_CONNECTOR).hasDefined(CommonAttributes.JMX));
    }


    private void checkDiscardedOperation(ModelNode op, KernelServices mainServices, ModelVersion oldVersion) throws OperationFailedException {
        TransformedOperation transformedOp = mainServices.transformOperation(oldVersion, op);
        Assert.assertNull(transformedOp.getTransformedOperation());
        Assert.assertNull(transformedOp.getFailureDescription());
    }

    private void checkRejectedOperation(ModelNode op, KernelServices mainServices, ModelVersion oldVersion) throws OperationFailedException {
        TransformedOperation transformedOp = mainServices.transformOperation(oldVersion, op);
        Assert.assertNull(transformedOp.getTransformedOperation());
        Assert.assertNotNull(transformedOp.getFailureDescription());
    }


    private void assertJmxConnectorAddress(ModelNode address) {
        PathAddress addr = PathAddress.pathAddress(address);
        Assert.assertEquals(2, addr.size());
        PathElement element = addr.getElement(0);
        Assert.assertEquals(SUBSYSTEM, element.getKey());
        Assert.assertEquals(JMXExtension.SUBSYSTEM_NAME, element.getValue());
        element = addr.getElement(1);
        Assert.assertEquals(CommonAttributes.REMOTING_CONNECTOR, element.getKey());
        Assert.assertEquals(CommonAttributes.JMX, element.getValue());
    }

    private void assertJmxSubsystemAddress(ModelNode address) {
        PathAddress addr = PathAddress.pathAddress(address);
        Assert.assertEquals(1, addr.size());
        PathElement element = addr.getElement(0);
        Assert.assertEquals(SUBSYSTEM, element.getKey());
        Assert.assertEquals(JMXExtension.SUBSYSTEM_NAME, element.getValue());
    }

    private static ModelNode createOperation(String name, String...addressElements) {
        final ModelNode addr = new ModelNode();
        addr.add(SUBSYSTEM, "jmx");
        for (int i = 0 ; i < addressElements.length ; i++) {
            addr.add(addressElements[i], addressElements[++i]);
        }
        return Util.getEmptyOperation(name, addr);
    }

    private static class BaseAdditionalInitalization extends AdditionalInitialization {

        @Override
        protected void initializeExtraSubystemsAndModel(ExtensionRegistry extensionRegistry, Resource rootResource,
                                        ManagementResourceRegistration rootRegistration) {
            rootRegistration.registerReadOnlyAttribute(LAUNCH_TYPE, new OperationStepHandler() {

                @Override
                public void execute(OperationContext context, ModelNode operation) throws OperationFailedException {
                    context.getResult().set(TYPE_STANDALONE);
                }
            }, AttributeAccess.Storage.RUNTIME);
        }

        @Override
        protected void setupController(ControllerInitializer controllerInitializer) {
            controllerInitializer.addSocketBinding("remote", 12345);
            controllerInitializer.addPath("jboss.controller.temp.dir", System.getProperty("java.io.tmpdir"), null);
        }

        @Override
        protected void addExtraServices(final ServiceTarget target) {
            ManagementRemotingServices.installRemotingEndpoint(target, ManagementRemotingServices.MANAGEMENT_ENDPOINT, "localhost", EndpointService.EndpointType.MANAGEMENT, null, null);
            ServiceName tmpDirPath = ServiceName.JBOSS.append("server", "path", "jboss.controller.temp.dir");

            RemotingServices.installSecurityServices(target, "remote", null, null, tmpDirPath, null, null);
            RemotingServices.installConnectorServicesForSocketBinding(target, ManagementRemotingServices.MANAGEMENT_ENDPOINT, "remote", SocketBinding.JBOSS_BINDING_NAME.append("remote"), OptionMap.EMPTY, null, null);
        }
    }

    private static class AuditLogInitialization extends BaseAdditionalInitalization {

        @Override
        protected void initializeExtraSubystemsAndModel(ExtensionRegistry extensionRegistry, Resource rootResource,
                                        ManagementResourceRegistration rootRegistration) {
            super.initializeExtraSubystemsAndModel(extensionRegistry, rootResource, rootRegistration);

            Resource coreManagement = Resource.Factory.create();
            rootResource.registerChild(CoreManagementResourceDefinition.PATH_ELEMENT, coreManagement);
            Resource auditLog = Resource.Factory.create();
            coreManagement.registerChild(AccessAuditResourceDefinition.PATH_ELEMENT, auditLog);

            Resource testFileHandler = Resource.Factory.create();
            testFileHandler.getModel().setEmptyObject();
            auditLog.registerChild(PathElement.pathElement(FILE_HANDLER, "test"), testFileHandler);
        }
    }

    private static class ManagementAuditLogInitialization extends AdditionalInitialization.ManagementAdditionalInitialization {

        private static final long serialVersionUID = 1L;

        @Override
        protected void initializeExtraSubystemsAndModel(ExtensionRegistry extensionRegistry, Resource rootResource,
                                        ManagementResourceRegistration rootRegistration) {
            super.initializeExtraSubystemsAndModel(extensionRegistry, rootResource, rootRegistration);

            Resource coreManagement = Resource.Factory.create();
            rootResource.registerChild(CoreManagementResourceDefinition.PATH_ELEMENT, coreManagement);
            Resource auditLog = Resource.Factory.create();
            coreManagement.registerChild(AccessAuditResourceDefinition.PATH_ELEMENT, auditLog);

            Resource testFileHandler = Resource.Factory.create();
            testFileHandler.getModel().setEmptyObject();
            auditLog.registerChild(PathElement.pathElement(FILE_HANDLER, "test"), testFileHandler);
        }
    }

    private static class CorrectDomainNameConfig extends FailedOperationTransformationConfig.AttributesPathAddressConfig<CorrectDomainNameConfig>{

        public CorrectDomainNameConfig(AttributeDefinition...attributes) {
            super(convert(attributes));

        }

        @Override
        protected boolean isAttributeWritable(String attributeName) {
            return true;
        }

        @Override
        protected boolean checkValue(String attrName, ModelNode attribute, boolean isWriteAttribute) {
            return !attribute.asString().equals("jboss.as");
        }

        @Override
        protected ModelNode correctValue(ModelNode toResolve, boolean isWriteAttribute) {
            return new ModelNode("jboss.as");
        }
    }

    private static class CorrectPropertyFormatConfig extends FailedOperationTransformationConfig.AttributesPathAddressConfig<CorrectDomainNameConfig>{

        public CorrectPropertyFormatConfig(AttributeDefinition...attributes) {
            super(convert(attributes));

        }

        @Override
        protected boolean isAttributeWritable(String attributeName) {
            return true;
        }

        @Override
        protected boolean checkValue(String attrName, ModelNode attribute, boolean isWriteAttribute) {
            return !attribute.asString().equals("false");
        }

        @Override
        protected ModelNode correctValue(ModelNode toResolve, boolean isWriteAttribute) {
            return new ModelNode(false);
        }
    }

    private static class ChangeEnabledFromTrueToFalseConfig extends FailedOperationTransformationConfig.AttributesPathAddressConfig<ChangeEnabledFromTrueToFalseConfig>{

        ChangeEnabledFromTrueToFalseConfig(AttributeDefinition...attributes){
            super(convert(attributes));
        }

        @Override
        protected boolean isAttributeWritable(String attributeName) {
            return true;
        }

        @Override
        protected boolean checkValue(String attrName, ModelNode attribute, boolean isWriteAttribute) {
            return attribute.asBoolean();
        }

        @Override
        protected ModelNode correctValue(ModelNode toResolve, boolean isWriteAttribute) {
            return new ModelNode(false);
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

QDox parse error in file://<WORKSPACE>/data/code-rep-dataset/Dataset3/Tasks/549.java