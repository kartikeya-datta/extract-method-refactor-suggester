final ModifiableConnDef connectionDefinitionValue = RaOperationUtil.buildConnectionDefinitionObject(context, recoveryEnvModel, poolName, isXa);

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

package org.jboss.as.connector.subsystems.resourceadapters;

import static org.jboss.as.connector.logging.ConnectorMessages.MESSAGES;
import static org.jboss.as.connector.subsystems.resourceadapters.CommonAttributes.CONNECTION_DEFINITIONS_NODE_ATTRIBUTE;
import static org.jboss.as.controller.descriptions.ModelDescriptionConstants.OP_ADDR;

import java.util.List;

import org.jboss.as.connector.util.ConnectorServices;
import org.jboss.as.controller.AbstractAddStepHandler;
import org.jboss.as.controller.AttributeDefinition;
import org.jboss.as.controller.OperationContext;
import org.jboss.as.controller.OperationFailedException;
import org.jboss.as.controller.PathAddress;
import org.jboss.as.controller.ServiceVerificationHandler;
import org.jboss.dmr.ModelNode;
import org.jboss.jca.common.api.metadata.common.TransactionSupportEnum;
import org.jboss.jca.common.api.validator.ValidateException;
import org.jboss.msc.service.ServiceController;
import org.jboss.msc.service.ServiceName;
import org.jboss.msc.service.ServiceTarget;

/**
 * Adds a recovery-environment to the Transactions subsystem
 *
 */
public class ConnectionDefinitionAdd extends AbstractAddStepHandler {

    public static final ConnectionDefinitionAdd INSTANCE = new ConnectionDefinitionAdd();

    @Override
    protected void populateModel(ModelNode operation, ModelNode modelNode) throws OperationFailedException {
        for (AttributeDefinition attribute : CONNECTION_DEFINITIONS_NODE_ATTRIBUTE) {
           attribute.validateAndSet(operation, modelNode);
        }
    }

    @Override
    protected void performRuntime(OperationContext context, ModelNode operation, ModelNode recoveryEnvModel,
                                  ServiceVerificationHandler verificationHandler,
                                  List<ServiceController<?>> serviceControllers) throws OperationFailedException {

        final ModelNode address = operation.require(OP_ADDR);
        PathAddress path = PathAddress.pathAddress(address);
        final String archiveName = path.getElement(path.size() -2).getValue();
        final String poolName = PathAddress.pathAddress(address).getLastElement().getValue();

        try {
            ServiceName serviceName = ServiceName.of(ConnectorServices.RA_SERVICE, archiveName, poolName);
            ServiceName raServiceName = ServiceName.of(ConnectorServices.RA_SERVICE, archiveName);

            final ModifiableResourceAdapter ravalue = ((ModifiableResourceAdapter) context.getServiceRegistry(false).getService(raServiceName).getValue());
            boolean isXa = ravalue.getTransactionSupport() == TransactionSupportEnum.XATransaction;

            final ModifiableConnDef connectionDefinitionValue = RaOperationUtil.buildConnectionDefinitionObject(context, operation, poolName, isXa);



            final ServiceTarget serviceTarget = context.getServiceTarget();

            final ConnectionDefinitionService service = new ConnectionDefinitionService(connectionDefinitionValue);
            ServiceController<?> controller = serviceTarget.addService(serviceName, service).setInitialMode(ServiceController.Mode.ACTIVE)
                    .addDependency(raServiceName, ModifiableResourceAdapter.class, service.getRaInjector())
                    .addListener(verificationHandler).install();

            serviceControllers.add(controller);

        } catch (ValidateException e) {
            throw new OperationFailedException(e, new ModelNode().set(MESSAGES.failedToCreate("ConnectionDefinition", operation, e.getLocalizedMessage())));
        }
    }



}