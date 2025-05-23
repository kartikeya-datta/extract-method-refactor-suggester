error id: file://<WORKSPACE>/data/code-rep-dataset/Dataset3/Tasks/4179.java
file://<WORKSPACE>/data/code-rep-dataset/Dataset3/Tasks/4179.java
### com.thoughtworks.qdox.parser.ParseException: syntax error @[1,1]

error in qdox parser
file content:
```java
offset: 1
uri: file://<WORKSPACE>/data/code-rep-dataset/Dataset3/Tasks/4179.java
text:
```scala
i@@f (testControllerVersion != ModelTestControllerVersion.MASTER && testControllerVersion.getTestControllerVersion() != null) {

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
package org.jboss.as.subsystem.test;

import static org.jboss.as.controller.descriptions.ModelDescriptionConstants.FAILURE_DESCRIPTION;
import static org.jboss.as.controller.descriptions.ModelDescriptionConstants.OP;
import static org.jboss.as.controller.descriptions.ModelDescriptionConstants.OP_ADDR;
import static org.jboss.as.controller.descriptions.ModelDescriptionConstants.REMOVE;
import static org.jboss.as.controller.descriptions.ModelDescriptionConstants.RESULT;
import static org.jboss.as.controller.descriptions.ModelDescriptionConstants.SUBSYSTEM;

import java.io.File;
import java.io.IOException;
import java.io.PrintWriter;
import java.io.StringReader;
import java.net.MalformedURLException;
import java.net.URL;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.Comparator;
import java.util.EnumSet;
import java.util.HashMap;
import java.util.List;
import java.util.ListIterator;
import java.util.Map;
import java.util.Set;

import javax.xml.namespace.QName;
import javax.xml.stream.XMLInputFactory;
import javax.xml.stream.XMLStreamException;
import javax.xml.stream.XMLStreamReader;

import org.jboss.as.controller.AttributeDefinition;
import org.jboss.as.controller.CompositeOperationHandler;
import org.jboss.as.controller.Extension;
import org.jboss.as.controller.ModelVersion;
import org.jboss.as.controller.OperationDefinition;
import org.jboss.as.controller.OperationFailedException;
import org.jboss.as.controller.OperationStepHandler;
import org.jboss.as.controller.PathAddress;
import org.jboss.as.controller.PathElement;
import org.jboss.as.controller.ProcessType;
import org.jboss.as.controller.ProxyController;
import org.jboss.as.controller.ResourceDefinition;
import org.jboss.as.controller.RunningMode;
import org.jboss.as.controller.RunningModeControl;
import org.jboss.as.controller.access.management.AccessConstraintDefinition;
import org.jboss.as.controller.descriptions.DescriptionProvider;
import org.jboss.as.controller.descriptions.ModelDescriptionConstants;
import org.jboss.as.controller.descriptions.OverrideDescriptionProvider;
import org.jboss.as.controller.extension.ExtensionRegistry;
import org.jboss.as.controller.extension.SubsystemInformation;
import org.jboss.as.controller.operations.common.Util;
import org.jboss.as.controller.parsing.ExtensionParsingContext;
import org.jboss.as.controller.persistence.ConfigurationPersister;
import org.jboss.as.controller.registry.AliasEntry;
import org.jboss.as.controller.registry.AttributeAccess;
import org.jboss.as.controller.registry.AttributeAccess.Storage;
import org.jboss.as.controller.registry.ImmutableManagementResourceRegistration;
import org.jboss.as.controller.registry.ManagementResourceRegistration;
import org.jboss.as.controller.registry.OperationEntry;
import org.jboss.as.controller.registry.OperationEntry.EntryType;
import org.jboss.as.controller.registry.OperationEntry.Flag;
import org.jboss.as.controller.registry.Resource;
import org.jboss.as.controller.transform.OperationTransformer.TransformedOperation;
import org.jboss.as.controller.transform.TransformerRegistry;
import org.jboss.as.model.test.ChildFirstClassLoaderBuilder;
import org.jboss.as.model.test.EAPRepositoryReachableUtil;
import org.jboss.as.model.test.ModelFixer;
import org.jboss.as.model.test.ModelTestBootOperationsBuilder;
import org.jboss.as.model.test.ModelTestControllerVersion;
import org.jboss.as.model.test.ModelTestModelControllerService;
import org.jboss.as.model.test.ModelTestOperationValidatorFilter;
import org.jboss.as.model.test.ModelTestOperationValidatorFilter.Action;
import org.jboss.as.model.test.ModelTestParser;
import org.jboss.as.model.test.ModelTestUtils;
import org.jboss.as.model.test.OperationFixer;
import org.jboss.as.model.test.StringConfigurationPersister;
import org.jboss.as.subsystem.bridge.impl.LegacyControllerKernelServicesProxy;
import org.jboss.as.subsystem.bridge.local.ScopedKernelServicesBootstrap;
import org.jboss.as.subsystem.test.ModelDescriptionValidator.ValidationConfiguration;
import org.jboss.dmr.ModelNode;
import org.jboss.modules.filter.ClassFilter;
import org.jboss.staxmapper.XMLMapper;
import org.junit.Assert;
import org.junit.Assume;
import org.xnio.IoUtils;

/**
 *
 * @author <a href="kabir.khan@jboss.com">Kabir Khan</a>
 */
final class SubsystemTestDelegate {

    private final String TEST_NAMESPACE = "urn.org.jboss.test:1.0";

    private static final ModelNode SUCCESS;
    static
    {
        SUCCESS = new ModelNode();
        SUCCESS.get(ModelDescriptionConstants.OUTCOME).set(ModelDescriptionConstants.SUCCESS);
        SUCCESS.get(ModelDescriptionConstants.RESULT);
        SUCCESS.protect();
    }

    private final Class<?> testClass;
    private final List<KernelServices> kernelServices = new ArrayList<KernelServices>();

    protected final String mainSubsystemName;
    private final Extension mainExtension;
    private final Comparator<PathAddress> removeOrderComparator;

    /**
     * ExtensionRegistry we use just for registering parsers.
     * The ModelControllerService uses a separate registry. This is done this way to allow multiple ModelControllerService
     * instantiations in the same test without having to re-initialize the parsers.
     */
    private ExtensionRegistry extensionParsingRegistry;
    private ModelTestParser testParser;
    private boolean addedExtraParsers;
    private XMLMapper xmlMapper;

    /**
     * Creates a new delegate.
     *
     * @param testClass             the test class
     * @param mainSubsystemName     the name of the subsystem
     * @param mainExtension         the extension to test
     * @param removeOrderComparator a comparator to sort addresses when removing the subsystem, {@code null} if order
     *                              doesn't matter
     */
    SubsystemTestDelegate(final Class<?> testClass, final String mainSubsystemName, final Extension mainExtension, final Comparator<PathAddress> removeOrderComparator) {
        this.testClass = testClass;
        this.mainSubsystemName = mainSubsystemName;
        this.mainExtension = mainExtension;
        this.removeOrderComparator = removeOrderComparator;
    }

    String getMainSubsystemName() {
        return mainSubsystemName;
    }

    void initializeParser() throws Exception {
        //Initialize the parser
        xmlMapper = XMLMapper.Factory.create();
        extensionParsingRegistry = new ExtensionRegistry(getProcessType(), new RunningModeControl(RunningMode.NORMAL), null, null);
        testParser = new TestParser(mainSubsystemName, extensionParsingRegistry);
        xmlMapper.registerRootElement(new QName(TEST_NAMESPACE, "test"), testParser);
        mainExtension.initializeParsers(extensionParsingRegistry.getExtensionParsingContext("Test", xmlMapper));
        addedExtraParsers = false;
    }

    void cleanup() throws Exception {
        for (KernelServices kernelServices : this.kernelServices) {
            try {
                kernelServices.shutdown();
            } catch (Exception e) {
                //we don't care
            }
        }
        kernelServices.clear();
        xmlMapper = null;
        extensionParsingRegistry = null;
        testParser = null;
    }

    Extension getMainExtension() {
        return mainExtension;
    }

    /**
     * Parse the subsystem xml and create the operations that will be passed into the controller
     *
     * @param subsystemXml the subsystem xml to be parsed
     * @return the created operations
     * @throws XMLStreamException if there is a parsing problem
     */
    List<ModelNode> parse(String subsystemXml) throws XMLStreamException {
        return parse(null, subsystemXml);
    }

    /**
     * Parse the subsystem xml and create the operations that will be passed into the controller
     *
     * @param additionalParsers additional initialization that should be done to the parsers before initializing our extension. These parsers
     *                          will only be initialized the first time this method is called from within a test
     * @param subsystemXml      the subsystem xml to be parsed
     * @return the created operations
     * @throws XMLStreamException if there is a parsing problem
     */
    List<ModelNode> parse(AdditionalParsers additionalParsers, String subsystemXml) throws XMLStreamException {
        String xml = "<test xmlns=\"" + TEST_NAMESPACE + "\">" +
                subsystemXml +
                "</test>";
        final XMLStreamReader reader = XMLInputFactory.newInstance().createXMLStreamReader(new StringReader(xml));
        addAdditionalParsers(additionalParsers);
        final List<ModelNode> operationList = new ArrayList<ModelNode>();
        xmlMapper.parseDocument(operationList, reader);
        return operationList;
    }

    /**
     * Output the model to xml
     *
     * @param model the model to marshall
     * @return the xml
     */
    String outputModel(ModelNode model) throws Exception {

        StringConfigurationPersister persister = new StringConfigurationPersister(Collections.<ModelNode>emptyList(), testParser, true);

        // Use ProcessType.HOST_CONTROLLER for this ExtensionRegistry so we don't need to provide
        // a PathManager via the ExtensionContext. All we need the Extension to do here is register the xml writers
        ExtensionRegistry outputExtensionRegistry = new ExtensionRegistry(ProcessType.HOST_CONTROLLER, new RunningModeControl(RunningMode.NORMAL), null, null);
        outputExtensionRegistry.setSubsystemParentResourceRegistrations(MOCK_RESOURCE_REG, MOCK_RESOURCE_REG);
        outputExtensionRegistry.setWriterRegistry(persister);

        Extension extension = mainExtension.getClass().newInstance();
        extension.initialize(outputExtensionRegistry.getExtensionContext("Test", false));

        ConfigurationPersister.PersistenceResource resource = persister.store(model, Collections.<PathAddress>emptySet());
        resource.commit();
        return persister.getMarshalled();
    }

    /**
     * Initializes the controller and populates the subsystem model from the passed in xml.
     *
     * @param subsystemXml the subsystem xml to be parsed
     * @return the kernel services allowing access to the controller and service container
     * @deprecated Use {@link #createKernelServicesBuilder(AdditionalInitialization)} instead
     */
    KernelServices installInController(String subsystemXml) throws Exception {
        return createKernelServicesBuilder(null)
                .setSubsystemXml(subsystemXml)
                .build();
    }

    /**
     * Initializes the controller and populates the subsystem model from the passed in xml.
     *
     * @param additionalInit Additional initialization that should be done to the parsers, controller and service container before initializing our extension
     * @param subsystemXml   the subsystem xml to be parsed
     * @deprecated Use {@link #createKernelServicesBuilder(AdditionalInitialization)} instead
     */
    KernelServices installInController(AdditionalInitialization additionalInit, String subsystemXml) throws Exception {
        return createKernelServicesBuilder(additionalInit)
                .setSubsystemXml(subsystemXml)
                .build();
    }

    /**
     * Create a new controller with the passed in operations.
     *
     * @param bootOperations the operations
     * @deprecated Use {@link #createKernelServicesBuilder(AdditionalInitialization)} instead
     */
    KernelServices installInController(List<ModelNode> bootOperations) throws Exception {
        return createKernelServicesBuilder(null)
                .setBootOperations(bootOperations)
                .build();
    }

    /**
     * Create a new controller with the passed in operations.
     *
     * @param additionalInit Additional initialization that should be done to the parsers, controller and service container before initializing our extension
     * @param bootOperations the operations
     * @deprecated Use {@link #createKernelServicesBuilder(AdditionalInitialization)} instead
     */
    KernelServices installInController(AdditionalInitialization additionalInit, List<ModelNode> bootOperations) throws Exception {
        return createKernelServicesBuilder(additionalInit)
                .setBootOperations(bootOperations)
                .build();
    }

    /**
     * Creates a new kernel services builder used to create a new controller containing the subsystem being tested
     *
     * @param additionalInit Additional initialization that should be done to the parsers, controller and service container before initializing our extension
     */
    KernelServicesBuilder createKernelServicesBuilder(AdditionalInitialization additionalInit) {
        return new KernelServicesBuilderImpl(additionalInit);
    }

    /**
     * Gets the ProcessType to use when initializing the parsers. Defaults to {@link ProcessType#EMBEDDED_SERVER}
     * To tweak the process type when installing a controller, override {@link AdditionalInitialization} and pass in to
     * {@link #createKernelServicesBuilder(AdditionalInitialization)} instead.
     *
     * @return the process type
     */
    ProcessType getProcessType() {
        return ProcessType.EMBEDDED_SERVER;
    }

    /**
     * Checks that the subystem resources can be removed, i.e. that people have registered
     * working 'remove' operations for every 'add' level.
     *
     * @param kernelServices the kernel services used to access the controller
     */
    void assertRemoveSubsystemResources(KernelServices kernelServices) {
        assertRemoveSubsystemResources(kernelServices, null);
    }

    /**
     * Checks that the subystem resources can be removed, i.e. that people have registered
     * working 'remove' operations for every 'add' level.
     *
     * @param kernelServices        the kernel services used to access the controller
     * @param ignoredChildAddresses child addresses that should not be removed, they are managed by one of the parent resources.
     *                              This set cannot contain the subsystem resource itself
     */
    void assertRemoveSubsystemResources(KernelServices kernelServices, Set<PathAddress> ignoredChildAddresses) {

        if (ignoredChildAddresses == null) {
            ignoredChildAddresses = Collections.emptySet();
        } else {
            PathAddress subsystem = PathAddress.pathAddress(PathElement.pathElement(SUBSYSTEM, mainSubsystemName));
            Assert.assertFalse("Cannot exclude removal of subsystem itself", ignoredChildAddresses.contains(subsystem));
        }

        Resource rootResource = ModelTestModelControllerService.grabRootResource(kernelServices);

        List<PathAddress> addresses = new ArrayList<PathAddress>();
        PathAddress pathAddress = PathAddress.pathAddress(PathElement.pathElement(SUBSYSTEM, mainSubsystemName));
        Resource subsystemResource = rootResource.getChild(pathAddress.getLastElement());
        Assert.assertNotNull(subsystemResource);
        addresses.add(pathAddress);

        getAllChildAddressesForRemove(pathAddress, addresses, subsystemResource);

        // If the remove order comparator is not null, then sort the addresses
        if (removeOrderComparator != null) {
            Collections.sort(addresses, removeOrderComparator);
        }

        ModelNode composite = new ModelNode();
        composite.get(OP).set(CompositeOperationHandler.NAME);
        composite.get(OP_ADDR).setEmptyList();
        composite.get("rollback-on-runtime-failure").set(true);


        for (ListIterator<PathAddress> iterator = addresses.listIterator(addresses.size()); iterator.hasPrevious(); ) {
            PathAddress cur = iterator.previous();
            if (!ignoredChildAddresses.contains(cur)) {
                ModelNode remove = new ModelNode();
                remove.get(OP).set(REMOVE);
                remove.get(OP_ADDR).set(cur.toModelNode());
                composite.get("steps").add(remove);
            }
        }


        kernelServices.executeOperation(composite);

        ModelNode model = kernelServices.readWholeModel().get(SUBSYSTEM, mainSubsystemName);
        Assert.assertFalse("Subsystem resources were not removed " + model, model.isDefined());
    }

    private void getAllChildAddressesForRemove(PathAddress address, List<PathAddress> addresses, Resource resource) {
        List<PathElement> childElements = new ArrayList<PathElement>();
        for (String type : resource.getChildTypes()) {
            for (String childName : resource.getChildrenNames(type)) {
                PathElement element = PathElement.pathElement(type, childName);
                childElements.add(element);
            }
        }

        for (PathElement childElement : childElements) {
            addresses.add(address.append(childElement));
        }

        for (PathElement childElement : childElements) {
            getAllChildAddressesForRemove(address.append(childElement), addresses, resource.getChild(childElement));
        }
    }

    /**
     * Dumps the target subsystem resource description to DMR format, needed by TransformerRegistry for non-standard subsystems
     *
     * @param kernelServices the kernel services for the started controller
     * @param modelVersion   the target subsystem model version
     * @deprecated this might no longer be needed following refactoring of TransformerRegistry
     */
    @Deprecated
    void generateLegacySubsystemResourceRegistrationDmr(KernelServices kernelServices, ModelVersion modelVersion) throws IOException {
        KernelServices legacy = kernelServices.getLegacyServices(modelVersion);

        //Generate the org.jboss.as.controller.transform.subsystem-version.dmr file - just use the format used by TransformerRegistry for now
        PathAddress pathAddress = PathAddress.pathAddress(PathElement.pathElement(SUBSYSTEM, mainSubsystemName));
        ModelNode desc = ((KernelServicesInternal)legacy).readFullModelDescription(pathAddress.toModelNode());
        File file = new File("target/test-classes").getAbsoluteFile();
        Assert.assertTrue(file.exists());
        for (String part : this.getClass().getPackage().getName().split("\\.")) {
            file = new File(file, part);
            if (!file.exists()) {
                file.mkdir();
            }
        }
        File dmrFile = new File(file, mainSubsystemName + "-" + modelVersion.getMajor() + "." + modelVersion.getMinor() +"."+modelVersion.getMicro()+ ".dmr");
        PrintWriter pw = new PrintWriter(dmrFile);
        try {
            desc.writeString(pw, false);
            //Leave this println - it only gets executed when people generate the legacy dmr files, and is useful to know where it has been written.
            System.out.println("Legacy resource definition dmr written to: " + dmrFile.getAbsolutePath());
        } finally {
            IoUtils.safeClose(pw);
        }
    }

    /**
     * Checks that the transformed model is the same as the model built up in the legacy subsystem controller via the transformed operations,
     * and that the transformed model is valid according to the resource definition in the legacy subsystem controller.
     *
     *
     *
     * @param kernelServices the main kernel services
     * @param modelVersion   the model version of the targeted legacy subsystem
     * @param legacyModelFixer use to touch up the model read from the legacy controller, use sparingly when the legacy model is just wrong. May be {@code null}
     * @param includeDefaults  whether the legacy controller model and the transformed model should include default values for undefined attributes
     * @return the whole model of the legacy controller
     */
    ModelNode checkSubsystemModelTransformation(KernelServices kernelServices, ModelVersion modelVersion, ModelFixer legacyModelFixer, boolean includeDefaults) throws IOException, OperationFailedException {

        ModelNode legacyReadResource = Util.createOperation(ModelDescriptionConstants.READ_RESOURCE_OPERATION, PathAddress.EMPTY_ADDRESS);
        legacyReadResource.get(ModelDescriptionConstants.RECURSIVE).set(true);
        legacyReadResource.get(ModelDescriptionConstants.INCLUDE_ALIASES).set(false);
        legacyReadResource.get(ModelDescriptionConstants.INCLUDE_RUNTIME).set(false);
        legacyReadResource.get(ModelDescriptionConstants.INCLUDE_DEFAULTS).set(includeDefaults);
        ModelNode legacyModel = ModelTestUtils.checkResultAndGetContents(kernelServices.executeOperation(modelVersion, kernelServices.transformOperation(modelVersion, legacyReadResource)));
        ModelNode legacySubsystem = legacyModel.require(SUBSYSTEM);
        legacySubsystem = legacySubsystem.require(mainSubsystemName);

        if (legacyModelFixer != null) {
            legacySubsystem = legacyModelFixer.fixModel(legacySubsystem);
        }

        //1) Check that the transformed model is the same as the whole model read from the legacy controller.
        //The transformed model is done via the resource transformers
        //The model in the legacy controller is built up via transformed operations
        ModelNode transformed = kernelServices.readTransformedModel(modelVersion, includeDefaults).get(SUBSYSTEM, mainSubsystemName);

        ModelTestUtils.compare(legacySubsystem, transformed, true);

        //2) Check that the transformed model is valid according to the resource definition in the legacy subsystem controller
        ResourceDefinition rd = TransformerRegistry.loadSubsystemDefinitionFromFile(this.getClass(), mainSubsystemName, modelVersion);
        Assert.assertNotNull("Could not load legacy dmr for subsystem '" + mainSubsystemName + "' version: '" + modelVersion + "' please add it", rd);
        ManagementResourceRegistration rr = ManagementResourceRegistration.Factory.create(rd);
        ModelTestUtils.checkModelAgainstDefinition(transformed, rr);
        return legacyModel;
    }

    void addAdditionalParsers(AdditionalParsers additionalParsers) {
        if (additionalParsers != null && !addedExtraParsers) {
            additionalParsers.addParsers(extensionParsingRegistry, xmlMapper);
            addedExtraParsers = true;
        }
    }

    private ExtensionRegistry cloneExtensionRegistry(AdditionalInitialization additionalInit) {
        final ExtensionRegistry clone = new ExtensionRegistry(additionalInit.getProcessType(), new RunningModeControl(additionalInit.getExtensionRegistryRunningMode()), null, null);
        for (String extension : extensionParsingRegistry.getExtensionModuleNames()) {
            ExtensionParsingContext epc = clone.getExtensionParsingContext(extension, null);
            for (Map.Entry<String, SubsystemInformation> entry : extensionParsingRegistry.getAvailableSubsystems(extension).entrySet()) {
                for (String namespace : entry.getValue().getXMLNamespaces()) {
                    epc.setSubsystemXmlMapping(entry.getKey(), namespace, null);
                }
            }
        }

        return clone;
    }

    private void validateDescriptionProviders(AdditionalInitialization additionalInit, KernelServices kernelServices) {
        ValidationConfiguration arbitraryDescriptors = additionalInit.getModelValidationConfiguration();
        ModelNode address = new ModelNode();
        address.setEmptyList();
        address.add("subsystem", mainSubsystemName);

        ModelNode op = new ModelNode();
        op.get(OP).set("read-resource-description");
        op.get(OP_ADDR).set(address);
        op.get("recursive").set(true);
        op.get("inherited").set(false);
        op.get("operations").set(true);
        op.get("include-aliases").set(true);
        ModelNode result = kernelServices.executeOperation(op);
        if (result.hasDefined(FAILURE_DESCRIPTION)) {
            throw new RuntimeException(result.get(FAILURE_DESCRIPTION).toString());
        }
        ModelNode model = result.get(RESULT);

        //System.out.println(model);

        ModelDescriptionValidator validator = new ModelDescriptionValidator(address, model, arbitraryDescriptors);
        List<ModelDescriptionValidator.ValidationFailure> validationMessages = validator.validateResource();
        if (validationMessages.size() > 0) {
            final StringBuilder builder = new StringBuilder("VALIDATION ERRORS IN MODEL:");
            for (ModelDescriptionValidator.ValidationFailure failure : validationMessages) {
                builder.append(failure);
                builder.append("\n");

            }
            if (arbitraryDescriptors != null) {
                Assert.fail("Failed due to validation errors in the model. Please fix :-) " + builder.toString());
            }
        }
    }


    private class KernelServicesBuilderImpl implements KernelServicesBuilder, ModelTestBootOperationsBuilder.BootOperationParser {
        private final ModelTestBootOperationsBuilder bootOperationBuilder;
        private final AdditionalInitialization additionalInit;
        private Map<ModelVersion, LegacyKernelServiceInitializerImpl> legacyControllerInitializers = new HashMap<ModelVersion, LegacyKernelServiceInitializerImpl>();

        public KernelServicesBuilderImpl(AdditionalInitialization additionalInit) {
            this.additionalInit = additionalInit == null ? new AdditionalInitialization() : additionalInit;
            bootOperationBuilder = new ModelTestBootOperationsBuilder(testClass, this);
        }

        @Override
        public KernelServicesBuilder setSubsystemXmlResource(String resource) throws IOException, XMLStreamException {
            bootOperationBuilder.setXmlResource(resource);
            return this;
        }

        @Override
        public KernelServicesBuilder setSubsystemXml(String subsystemXml) throws XMLStreamException {
            bootOperationBuilder.setXml(subsystemXml);
            return this;
        }

        public KernelServicesBuilder setBootOperations(List<ModelNode> bootOperations) {
            bootOperationBuilder.setBootOperations(bootOperations);
            return this;
        }

        @Override
        public KernelServicesBuilder setBootOperations(ModelNode... bootOperations) {
            bootOperationBuilder.setBootOperations(Arrays.asList(bootOperations));
            return this;
        }

        public LegacyKernelServicesInitializer createLegacyKernelServicesBuilder(AdditionalInitialization additionalInit, ModelTestControllerVersion version, ModelVersion modelVersion) {
            //Ignore this test if it is eap
            if (version.isEap()) {
                Assume.assumeTrue(EAPRepositoryReachableUtil.isReachable());
            }

            bootOperationBuilder.validateNotAlreadyBuilt();
            if (legacyControllerInitializers.containsKey(modelVersion)) {
                throw new IllegalArgumentException("There is already a legacy controller for " + modelVersion);
            }
            if (additionalInit != null) {
                if (additionalInit.getRunningMode() != RunningMode.ADMIN_ONLY) {
                    throw new IllegalArgumentException("The additional initialization must have a running mode of ADMIN_ONLY, it was " + additionalInit.getRunningMode());
                }
            }

            LegacyKernelServiceInitializerImpl initializer = new LegacyKernelServiceInitializerImpl(additionalInit, version, modelVersion);
            legacyControllerInitializers.put(modelVersion, initializer);
            return initializer;
        }

        public KernelServices build() throws Exception {
            bootOperationBuilder.validateNotAlreadyBuilt();
            List<ModelNode> bootOperations = bootOperationBuilder.build();
            AbstractKernelServicesImpl kernelServices = AbstractKernelServicesImpl.create(mainSubsystemName, additionalInit, ModelTestOperationValidatorFilter.createValidateAll(), cloneExtensionRegistry(additionalInit), bootOperations,
                    testParser, mainExtension, null, legacyControllerInitializers.size() > 0, true);
            SubsystemTestDelegate.this.kernelServices.add(kernelServices);

            validateDescriptionProviders(additionalInit, kernelServices);
            ImmutableManagementResourceRegistration subsystemReg = kernelServices.getRootRegistration().getSubModel(PathAddress.pathAddress(PathElement.pathElement(SUBSYSTEM, mainSubsystemName)));
            ModelTestUtils.validateModelDescriptions(PathAddress.EMPTY_ADDRESS, subsystemReg);

            for (Map.Entry<ModelVersion, LegacyKernelServiceInitializerImpl> entry : legacyControllerInitializers.entrySet()) {
                LegacyKernelServiceInitializerImpl legacyInitializer = entry.getValue();

                List<ModelNode> transformedBootOperations = new ArrayList<ModelNode>();
                for (ModelNode op : bootOperations) {

                    TransformedOperation transformedOp = kernelServices.transformOperation(entry.getKey(), op);
                    if (transformedOp.getTransformedOperation() != null) {
                        //Since rejected operations now execute on the slave to determine if it has been ignored or not
                        //we check the reject policy simulating a reject before adding it to the list of boot operations
                        if (!transformedOp.rejectOperation(SUCCESS)) {
                            transformedBootOperations.add(transformedOp.getTransformedOperation());
//                        } else {
//                            System.out.println(transformedOp.getFailureDescription());
                        }
                    }
                }

                LegacyControllerKernelServicesProxy legacyServices = legacyInitializer.install(kernelServices, transformedBootOperations);
                kernelServices.addLegacyKernelService(entry.getKey(), legacyServices);
            }

            return kernelServices;
        }

        @Override
        public List<ModelNode> parse(String subsystemXml) throws XMLStreamException {
            return SubsystemTestDelegate.this.parse(additionalInit, subsystemXml);
        }

        @Override
        public List<ModelNode> parseXml(String xml) throws Exception {
            ModelTestBootOperationsBuilder builder = new ModelTestBootOperationsBuilder(testClass, this);
            builder.setXml(xml);
            return builder.build();
        }

        @Override
        public List<ModelNode> parseXmlResource(String xmlResource) throws Exception {
            ModelTestBootOperationsBuilder builder = new ModelTestBootOperationsBuilder(testClass, this);
            builder.setXmlResource(xmlResource);
            return builder.build();
        }

    }

    private class LegacyKernelServiceInitializerImpl implements LegacyKernelServicesInitializer {

        private final AdditionalInitialization additionalInit;
        private final ModelTestControllerVersion testControllerVersion;
        private String extensionClassName;
        private ModelVersion modelVersion;
        private ChildFirstClassLoaderBuilder classLoaderBuilder;
        private ModelTestOperationValidatorFilter.Builder operationValidationExcludeBuilder;
        private boolean persistXml = true;
        private boolean skipReverseCheck;
        private AdditionalInitialization reverseCheckConfig;
        private ModelFixer reverseCheckModelFixer;
        private OperationFixer reverseCheckOperationFixer = new OperationFixer() {
            @Override
            public ModelNode fixOperation(ModelNode operation) {
                return operation;
            }
        };

        public LegacyKernelServiceInitializerImpl(AdditionalInitialization additionalInit, ModelTestControllerVersion version, ModelVersion modelVersion) {
            this.classLoaderBuilder = new ChildFirstClassLoaderBuilder(version.isEap());
            this.additionalInit = additionalInit == null ? AdditionalInitialization.MANAGEMENT : additionalInit;
            this.testControllerVersion = version;
            this.modelVersion = modelVersion;
        }


        @Override
        public LegacyKernelServicesInitializer addOperationValidationExclude(String name, PathAddress pathAddress) {
            addOperationValidationConfig(name, pathAddress, Action.NOCHECK, null);
            return this;
        }


        @Override
        public LegacyKernelServicesInitializer addOperationValidationResolve(String name, PathAddress pathAddress) {
            addOperationValidationConfig(name, pathAddress, Action.RESOLVE, null);
            return this;
        }

        @Override
        public LegacyKernelServicesInitializer addOperationValidationFixer(String name, PathAddress pathAddress, OperationFixer operationFixer) {
            addOperationValidationConfig(name, pathAddress, null, operationFixer);
            return this;
        }


        private void addOperationValidationConfig(String name, PathAddress pathAddress, Action action, OperationFixer operationFixer) {
            if (!additionalInit.isValidateOperations()) {
                throw new IllegalStateException("The additional initialization used to create this builder has turned off operation validation. That is not compatible with calling this method");
            }
            if (operationValidationExcludeBuilder == null) {
                operationValidationExcludeBuilder = ModelTestOperationValidatorFilter.createBuilder();
            }
            operationValidationExcludeBuilder.addOperation(pathAddress, name, action, operationFixer);
        }

        @Override
        public LegacyKernelServicesInitializer setExtensionClassName(String extensionClassName) {
            this.extensionClassName = extensionClassName;
            return this;
        }


        @Override
        public LegacyKernelServicesInitializer addURL(URL url) {
            classLoaderBuilder.addURL(url);
            return this;
        }

        @Override
        public LegacyKernelServicesInitializer addSimpleResourceURL(String resource) throws MalformedURLException {
            classLoaderBuilder.addSimpleResourceURL(resource);
            return this;
        }

        @Override
        public LegacyKernelServicesInitializer addMavenResourceURL(String...artifactGavs) throws IOException, ClassNotFoundException {
            for (String artifactGav : artifactGavs) {
                classLoaderBuilder.addMavenResourceURL(artifactGav);
            }
            return this;
        }

        @Override
        public LegacyKernelServiceInitializerImpl addParentFirstClassPattern(String pattern) {
            classLoaderBuilder.addParentFirstClassPattern(pattern);
            return this;
        }

        @Override
        public LegacyKernelServiceInitializerImpl addChildFirstClassPattern(String pattern) {
            classLoaderBuilder.addChildFirstClassPattern(pattern);
            return this;
        }

        @Override
        public LegacyKernelServicesInitializer excludeFromParent(ClassFilter exclusionFilter) {
            classLoaderBuilder.excludeFromParent(exclusionFilter);
            return this;
        }

        @Override
        public LegacyKernelServicesInitializer addSingleChildFirstClass(Class<?>...classes) {
            classLoaderBuilder.addSingleChildFirstClass(classes);
            return this;
        }

        private LegacyControllerKernelServicesProxy install(KernelServices mainServices, List<ModelNode> bootOperations) throws Exception {
            if (!skipReverseCheck) {
                bootCurrentVersionWithLegacyBootOperations(bootOperations, mainServices);
            }

            classLoaderBuilder.addParentFirstClassPattern("org.jboss.as.subsystem.bridge.shared.*");

            classLoaderBuilder.addMavenResourceURL("org.wildfly:wildfly-subsystem-test-framework:" + ModelTestControllerVersion.CurrentVersion.VERSION);
            classLoaderBuilder.addMavenResourceURL("org.wildfly:wildfly-model-test:" + ModelTestControllerVersion.CurrentVersion.VERSION);

            if (testControllerVersion != ModelTestControllerVersion.MASTER) {
                String groupId = testControllerVersion.getMavenGavVersion().startsWith("7.") ? "org.jboss.as" : "org.wildfly";
                String serverArtifactId = testControllerVersion.getMavenGavVersion().startsWith("7.") ? "jboss-as-server" : "wildfly-server";
                classLoaderBuilder.addRecursiveMavenResourceURL(groupId + ":" + serverArtifactId + ":" + testControllerVersion.getMavenGavVersion());

                //TODO Even with this there are some workarounds needed in JGroupsSubsystemTransformerTestCase, InfinispanSubsystemTransformersTestCase and LoggingSubsystemTestCase
                //Don't load modules from the scoped classloader to avoid some funky stuff going on when initializing the JAXP redirect
                //The mentioned funky stuff works fine when running in Eclipse but fails when running the tests on the command-line
                classLoaderBuilder.addParentFirstClassPattern("__redirected.*");
                classLoaderBuilder.addParentFirstClassPattern("org.jboss.modules.*");

                classLoaderBuilder.addMavenResourceURL("org.wildfly:wildfly-subsystem-test-controller-" + testControllerVersion.getTestControllerVersion() + ":" + ModelTestControllerVersion.CurrentVersion.VERSION);
            }
            ClassLoader legacyCl = classLoaderBuilder.build();

            ScopedKernelServicesBootstrap scopedBootstrap = new ScopedKernelServicesBootstrap(legacyCl);
            return scopedBootstrap.createKernelServices(mainSubsystemName, extensionClassName != null ? extensionClassName : mainExtension.getClass().getName(), additionalInit,
                    getOperationValidationFilter(), bootOperations, modelVersion, persistXml);
        }

        @Override
        public LegacyKernelServicesInitializer dontPersistXml() {
            persistXml = false;
            return this;
        }

        @Override
        public LegacyKernelServicesInitializer skipReverseControllerCheck() {
            skipReverseCheck = true;
            return this;
        }

        @Override
        public LegacyKernelServicesInitializer configureReverseControllerCheck(AdditionalInitialization additionalInit,
                ModelFixer modelFixer) {
            this.reverseCheckConfig = additionalInit;
            this.reverseCheckModelFixer = modelFixer;
            return this;
        }

        @Override
        public LegacyKernelServicesInitializer configureReverseControllerCheck(AdditionalInitialization additionalInit,
                ModelFixer modelFixer, OperationFixer fixer) {
            this.reverseCheckConfig = additionalInit;
            this.reverseCheckModelFixer = modelFixer;
            this.reverseCheckOperationFixer = fixer;
            return this;
        }

        private KernelServices bootCurrentVersionWithLegacyBootOperations(List<ModelNode> bootOperations, KernelServices mainServices) throws Exception {
            //Clone the boot operations to avoid any pollution installing them in the main controller
            List<ModelNode> clonedBootOperations = new ArrayList<ModelNode>();
            for (ModelNode op : bootOperations) {
                ModelNode cloned = reverseCheckOperationFixer.fixOperation(op.clone());
                if (cloned!=null){
                    clonedBootOperations.add(cloned);
                }
            }

            KernelServices reverseServices = createKernelServicesBuilder(reverseCheckConfig)
                .setBootOperations(clonedBootOperations)
                .build();
            final Throwable bootError = reverseServices.getBootError();
            if (bootError != null) {
                if (bootError instanceof Exception) {
                    throw (Exception) bootError;
                }
                throw new Exception(bootError);
            }
            Assert.assertTrue(reverseServices.isSuccessfulBoot());

            ModelNode reverseSubsystem = reverseServices.readWholeModel().get(SUBSYSTEM, getMainSubsystemName());
            if (reverseCheckModelFixer != null) {
                reverseSubsystem = reverseCheckModelFixer.fixModel(reverseSubsystem);
            }
            ModelTestUtils.compare(mainServices.readWholeModel().get(SUBSYSTEM, getMainSubsystemName()), reverseSubsystem);
            return reverseServices;
        }

        private ModelTestOperationValidatorFilter getOperationValidationFilter() {
            if (operationValidationExcludeBuilder != null) {
                return operationValidationExcludeBuilder.build();
            }
            if (additionalInit.isValidateOperations()) {
                return ModelTestOperationValidatorFilter.createValidateAll();
            } else {
                return ModelTestOperationValidatorFilter.createValidateNone();
            }
        }


    }

    @SuppressWarnings("deprecation")
    private final ManagementResourceRegistration MOCK_RESOURCE_REG = new ManagementResourceRegistration() {

        @Override
        public boolean isRuntimeOnly() {
            return false;
        }

        @Override
        public boolean isRemote() {
            return false;
        }

        @Override
        public OperationEntry getOperationEntry(PathAddress address, String operationName) {
            return null;
        }

        @Override
        public OperationStepHandler getOperationHandler(PathAddress address, String operationName) {
            return null;
        }

        @Override
        public DescriptionProvider getOperationDescription(PathAddress address, String operationName) {
            return null;
        }

        @Override
        public Set<Flag> getOperationFlags(PathAddress address, String operationName) {
            return null;
        }

        @Override
        public Set<String> getAttributeNames(PathAddress address) {
            return null;
        }

        @Override
        public AttributeAccess getAttributeAccess(PathAddress address, String attributeName) {
            return null;
        }

        @Override
        public Set<String> getChildNames(PathAddress address) {
            return null;
        }

        @Override
        public Set<PathElement> getChildAddresses(PathAddress address) {
            return null;
        }

        @Override
        public DescriptionProvider getModelDescription(PathAddress address) {
            return null;
        }

        @Override
        public Map<String, OperationEntry> getOperationDescriptions(PathAddress address, boolean inherited) {
            return null;
        }

        @Override
        public ProxyController getProxyController(PathAddress address) {
            return null;
        }

        @Override
        public Set<ProxyController> getProxyControllers(PathAddress address) {
            return null;
        }

        @Override
        public ManagementResourceRegistration getOverrideModel(String name) {
            return null;
        }

        @Override
        public ManagementResourceRegistration getSubModel(PathAddress address) {
            return null;
        }

        @Override
        public List<AccessConstraintDefinition> getAccessConstraints() {
            return Collections.emptyList();
        }

        @Override
        public ManagementResourceRegistration registerSubModel(PathElement address, DescriptionProvider descriptionProvider) {
            return MOCK_RESOURCE_REG;
        }

        @Override
        public ManagementResourceRegistration registerSubModel(ResourceDefinition resourceDefinition) {
            return MOCK_RESOURCE_REG;
        }

        @Override
        public void unregisterSubModel(PathElement address) {
        }

        @Override
        public boolean isAllowsOverride() {
            return true;
        }

        @Override
        public void setRuntimeOnly(boolean runtimeOnly) {
        }

        @Override
        public ManagementResourceRegistration registerOverrideModel(String name, OverrideDescriptionProvider descriptionProvider) {
            return MOCK_RESOURCE_REG;
        }

        @Override
        public void unregisterOverrideModel(String name) {
        }

        @Override
        public void registerOperationHandler(String operationName, OperationStepHandler handler,
                                             DescriptionProvider descriptionProvider) {
        }

        @Override
        public void registerOperationHandler(String operationName, OperationStepHandler handler,
                                             DescriptionProvider descriptionProvider, EnumSet<Flag> flags) {
        }

        @Override
        public void registerOperationHandler(String operationName, OperationStepHandler handler,
                                             DescriptionProvider descriptionProvider, boolean inherited) {
        }

        @Override
        public void registerOperationHandler(String operationName, OperationStepHandler handler,
                                             DescriptionProvider descriptionProvider, boolean inherited, EntryType entryType) {
        }

        @Override
        public void registerOperationHandler(String operationName, OperationStepHandler handler,
                                             DescriptionProvider descriptionProvider, boolean inherited, EnumSet<Flag> flags) {
        }


        @Override
        public void registerOperationHandler(String operationName, OperationStepHandler handler,
                                             DescriptionProvider descriptionProvider, boolean inherited, EntryType entryType, EnumSet<Flag> flags) {
        }

        @Override
        public void registerOperationHandler(OperationDefinition definition, OperationStepHandler handler) {

        }

        @Override
        public void registerOperationHandler(OperationDefinition definition, OperationStepHandler handler, boolean inherited) {

        }

        @Override
        public void unregisterOperationHandler(String operationName) {

        }

        @Override
        public void registerReadWriteAttribute(String attributeName, OperationStepHandler readHandler,
                                               OperationStepHandler writeHandler, Storage storage) {
        }

        @Override
        public void registerReadWriteAttribute(String attributeName, OperationStepHandler readHandler, OperationStepHandler writeHandler, EnumSet<AttributeAccess.Flag> flags) {
        }

        @Override
        public void registerReadWriteAttribute(AttributeDefinition definition, OperationStepHandler readHandler, OperationStepHandler writeHandler) {
        }

        @Override
        public void registerReadOnlyAttribute(String attributeName, OperationStepHandler readHandler, Storage storage) {
        }

        @Override
        public void registerReadOnlyAttribute(String attributeName, OperationStepHandler readHandler, EnumSet<AttributeAccess.Flag> flags) {
        }

        @Override
        public void registerReadOnlyAttribute(AttributeDefinition definition, OperationStepHandler readHandler) {
        }

        @Override
        public void registerMetric(String attributeName, OperationStepHandler metricHandler) {
        }

        @Override
        public void registerMetric(AttributeDefinition definition, OperationStepHandler metricHandler) {
        }

        @Override
        public void registerMetric(String attributeName, OperationStepHandler metricHandler, EnumSet<AttributeAccess.Flag> flags) {
        }

        @Override
        public void unregisterAttribute(String attributeName) {
        }

        @Override
        public void registerProxyController(PathElement address, ProxyController proxyController) {
        }

        @Override
        public void unregisterProxyController(PathElement address) {
        }

        @Override
        public void registerAlias(PathElement address, AliasEntry alias) {
        }

        @Override
        public void unregisterAlias(PathElement address) {
        }

        @Override
        public AliasEntry getAliasEntry() {
            return null;
        }

        @Override
        public boolean isAlias() {
            return false;
        }
    };


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
	java.base/java.util.concurrent.ForkJoinPool$WorkQueue.topLevelExec(ForkJoinPool.java:1182)
	java.base/java.util.concurrent.ForkJoinPool.scan(ForkJoinPool.java:1655)
	java.base/java.util.concurrent.ForkJoinPool.runWorker(ForkJoinPool.java:1622)
	java.base/java.util.concurrent.ForkJoinWorkerThread.run(ForkJoinWorkerThread.java:165)
```
#### Short summary: 

QDox parse error in file://<WORKSPACE>/data/code-rep-dataset/Dataset3/Tasks/4179.java