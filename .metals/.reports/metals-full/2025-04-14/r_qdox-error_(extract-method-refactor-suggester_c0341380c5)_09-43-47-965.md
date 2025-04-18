error id: file://<WORKSPACE>/data/code-rep-dataset/Dataset3/Tasks/857.java
file://<WORKSPACE>/data/code-rep-dataset/Dataset3/Tasks/857.java
### com.thoughtworks.qdox.parser.ParseException: syntax error @[1,1]

error in qdox parser
file content:
```java
offset: 1
uri: file://<WORKSPACE>/data/code-rep-dataset/Dataset3/Tasks/857.java
text:
```scala
.@@setAllowExpression(true)

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
package org.jboss.as.connector.subsystems.datasources;

import static org.jboss.as.controller.descriptions.ModelDescriptionConstants.COMPOSITE;
import static org.jboss.as.controller.descriptions.ModelDescriptionConstants.DISABLE;
import static org.jboss.as.controller.descriptions.ModelDescriptionConstants.ENABLE;
import static org.jboss.as.controller.descriptions.ModelDescriptionConstants.NAME;
import static org.jboss.as.controller.descriptions.ModelDescriptionConstants.OP;
import static org.jboss.as.controller.descriptions.ModelDescriptionConstants.OP_ADDR;
import static org.jboss.as.controller.descriptions.ModelDescriptionConstants.STEPS;
import static org.jboss.as.controller.descriptions.ModelDescriptionConstants.VALUE;

import java.util.Arrays;
import java.util.List;

import org.jboss.as.connector.logging.ConnectorLogger;
import org.jboss.as.controller.ObjectListAttributeDefinition;
import org.jboss.as.controller.ObjectTypeAttributeDefinition;
import org.jboss.as.controller.OperationFailedException;
import org.jboss.as.controller.PathAddress;
import org.jboss.as.controller.PropertiesAttributeDefinition;
import org.jboss.as.controller.SimpleAttributeDefinition;
import org.jboss.as.controller.SimpleAttributeDefinitionBuilder;
import org.jboss.as.controller.SimpleOperationDefinition;
import org.jboss.as.controller.SimpleOperationDefinitionBuilder;
import org.jboss.as.controller.access.constraint.SensitivityClassification;
import org.jboss.as.controller.access.management.SensitiveTargetAccessConstraintDefinition;
import org.jboss.as.controller.descriptions.ModelDescriptionConstants;
import org.jboss.as.controller.descriptions.NonResolvingResourceDescriptionResolver;
import org.jboss.as.controller.operations.common.Util;
import org.jboss.as.controller.operations.validation.ParameterValidator;
import org.jboss.as.controller.registry.AttributeAccess;
import org.jboss.as.controller.transform.OperationResultTransformer;
import org.jboss.as.controller.transform.OperationTransformer;
import org.jboss.as.controller.transform.TransformationContext;
import org.jboss.dmr.ModelNode;
import org.jboss.dmr.ModelType;
import org.jboss.jca.common.api.metadata.Defaults;
import org.jboss.jca.common.api.metadata.common.Credential;
import org.jboss.jca.common.api.metadata.common.Recovery;
import org.jboss.jca.common.api.metadata.common.Security;
import org.jboss.jca.common.api.metadata.common.XaPool;
import org.jboss.jca.common.api.metadata.ds.DataSource;
import org.jboss.jca.common.api.metadata.ds.Driver;
import org.jboss.jca.common.api.metadata.ds.DsPool;
import org.jboss.jca.common.api.metadata.ds.Statement;
import org.jboss.jca.common.api.metadata.ds.TimeOut;
import org.jboss.jca.common.api.metadata.ds.Validation;
import org.jboss.jca.common.api.metadata.ds.XaDataSource;

/**
 * @author @author <a href="mailto:stefano.maestri@redhat.com">Stefano
 *         Maestri</a>
 */
public class Constants {

    static final String DATASOURCES = "datasources";

    static final String DATA_SOURCE = "data-source";

    static final String XA_DATASOURCE = "xa-data-source";

    private static final String CONNECTION_URL_NAME = "connection-url";

    static final String JDBC_DRIVER_NAME = "jdbc-driver";

    private static final String DATASOURCE_DRIVER_CLASS_NAME = "driver-class";

    private static final String DATASOURCE_CLASS_NAME = "datasource-class";

    private static final String DATASOURCE_DRIVER_NAME = "driver-name";

    private static final String DRIVER_NAME_NAME = "driver-name";

    private static final String DRIVER_MODULE_NAME_NAME = "driver-module-name";

    private static final String DRIVER_MAJOR_VERSION_NAME = "driver-major-version";

    private static final String DRIVER_MINOR_VERSION_NAME = "driver-minor-version";

    private static final String DRIVER_CLASS_NAME_NAME = "driver-class-name";

    private static final String DRIVER_DATASOURCE_CLASS_NAME_NAME = "driver-datasource-class-name";

    private static final String DRIVER_XA_DATASOURCE_CLASS_NAME_NAME = "driver-xa-datasource-class-name";

    private static final String CONNECTION_PROPERTIES_NAME = "connection-properties";

    private static final String CONNECTION_PROPERTY_VALUE_NAME = "value";

    private static final String NEW_CONNECTION_SQL_NAME = "new-connection-sql";

    private static final String TRANSACTION_ISOLATION_NAME = "transaction-isolation";

    private static final String URL_DELIMITER_NAME = "url-delimiter";

    private static final String URL_PROPERTY_NAME = "url-property";

    private static final String URL_SELECTOR_STRATEGY_CLASS_NAME_NAME = "url-selector-strategy-class-name";

    private static final String USE_JAVA_CONTEXT_NAME = "use-java-context";

    private static final String CONNECTABLE_NAME = "connectable";

    private static final String TRACKING_NAME = "tracking";

    static final String POOLNAME_NAME = "pool-name";

    private static final String ENABLED_NAME = "enabled";

    private static final String JTA_NAME = "jta";

    private static final String JNDINAME_NAME = "jndi-name";

    private static final String ALLOCATION_RETRY_NAME = "allocation-retry";

    private static final String ALLOCATION_RETRY_WAIT_MILLIS_NAME = "allocation-retry-wait-millis";

    private static final String ALLOW_MULTIPLE_USERS_NAME = "allow-multiple-users";

    private static final String CONNECTION_LISTENER_CLASS_NAME = "connection-listener-class";

    private static final String CONNECTION_LISTENER_PROPERTY_NAME = "connection-listener-property";

    private static final String SETTXQUERYTIMEOUT_NAME = "set-tx-query-timeout";

    private static final String XA_RESOURCE_TIMEOUT_NAME = "xa-resource-timeout";

    private static final String QUERYTIMEOUT_NAME = "query-timeout";

    private static final String USETRYLOCK_NAME = "use-try-lock";

    private static final String USERNAME_NAME = "user-name";

    private static final String PASSWORD_NAME = "password";

    private static final String SECURITY_DOMAIN_NAME = "security-domain";

    private static final String SHAREPREPAREDSTATEMENTS_NAME = "share-prepared-statements";

    private static final String PREPAREDSTATEMENTSCACHESIZE_NAME = "prepared-statements-cache-size";

    private static final String TRACKSTATEMENTS_NAME = "track-statements";

    private static final String VALID_CONNECTION_CHECKER_CLASSNAME_NAME = "valid-connection-checker-class-name";

    private static final String CHECKVALIDCONNECTIONSQL_NAME = "check-valid-connection-sql";

    private static final String VALIDATEONMATCH_NAME = "validate-on-match";

    private static final String SPY_NAME = "spy";

    private static final String USE_CCM_NAME = "use-ccm";

    private static final String STALECONNECTIONCHECKERCLASSNAME_NAME = "stale-connection-checker-class-name";

    private static final String EXCEPTIONSORTERCLASSNAME_NAME = "exception-sorter-class-name";

    private static final String XADATASOURCEPROPERTIES_NAME = "xa-datasource-properties";

    private static final String XADATASOURCEPROPERTIES_VALUE_NAME = "value";

    private static final String XADATASOURCECLASS_NAME = "xa-datasource-class";

    private static final String INTERLEAVING_NAME = "interleaving";

    private static final String NOTXSEPARATEPOOL_NAME = "no-tx-separate-pool";

    private static final String PAD_XID_NAME = "pad-xid";

    private static final String SAME_RM_OVERRIDE_NAME = "same-rm-override";

    private static final String WRAP_XA_RESOURCE_NAME = "wrap-xa-resource";

    private static final String EXCEPTIONSORTER_PROPERTIES_NAME = "exception-sorter-properties";

    private static final String STALECONNECTIONCHECKER_PROPERTIES_NAME = "stale-connection-checker-properties";

    private static final String VALIDCONNECTIONCHECKER_PROPERTIES_NAME = "valid-connection-checker-properties";

    private static final String REAUTHPLUGIN_CLASSNAME_NAME = "reauth-plugin-class-name";

    private static final String REAUTHPLUGIN_PROPERTIES_NAME = "reauth-plugin-properties";

    private static final String RECOVERY_USERNAME_NAME = "recovery-username";

    private static final String RECOVERY_PASSWORD_NAME = "recovery-password";

    private static final String RECOVERY_SECURITY_DOMAIN_NAME = "recovery-security-domain";

    private static final String RECOVER_PLUGIN_CLASSNAME_NAME = "recovery-plugin-class-name";

    private static final String RECOVER_PLUGIN_PROPERTIES_NAME = "recovery-plugin-properties";

    private static final String NO_RECOVERY_NAME = "no-recovery";

    static final SensitivityClassification DS_SECURITY =
            new SensitivityClassification(DataSourcesExtension.SUBSYSTEM_NAME, "data-source-security", false, true, true);

    static final SensitiveTargetAccessConstraintDefinition DS_SECURITY_DEF = new SensitiveTargetAccessConstraintDefinition(DS_SECURITY);

    static final SimpleAttributeDefinition DEPLOYMENT_NAME = SimpleAttributeDefinitionBuilder.create("deployment-name", ModelType.STRING)
            .setAllowExpression(true)
            .setAllowNull(true)
            .build();

    static final SimpleAttributeDefinition MODULE_SLOT = SimpleAttributeDefinitionBuilder.create("module-slot", ModelType.STRING)
            .setAllowExpression(false)
            .setAllowNull(true)
            .build();

    static final SimpleAttributeDefinition JDBC_COMPLIANT = SimpleAttributeDefinitionBuilder.create("jdbc-compliant", ModelType.BOOLEAN)
            .setAllowNull(true)
            .build();

    static final String STATISTICS = "statistics";


    static SimpleAttributeDefinition CONNECTION_URL = new SimpleAttributeDefinitionBuilder(CONNECTION_URL_NAME, ModelType.STRING, false)
            .setAllowExpression(true)
            .setXmlName(DataSource.Tag.CONNECTION_URL.getLocalName())
            .build();

    static SimpleAttributeDefinition DRIVER_CLASS = new SimpleAttributeDefinitionBuilder(DATASOURCE_DRIVER_CLASS_NAME, ModelType.STRING)
            .setAllowNull(true)
            .setAllowExpression(true)
            .setXmlName(DataSource.Tag.DRIVER_CLASS.getLocalName())
            .build();


    static SimpleAttributeDefinition DATASOURCE_CLASS = new SimpleAttributeDefinitionBuilder(DATASOURCE_CLASS_NAME, ModelType.STRING)
            .setXmlName(DataSource.Tag.DATASOURCE_CLASS.getLocalName())
            .setAllowExpression(true)
            .setAllowNull(true)
            .build();

    static SimpleAttributeDefinition JNDI_NAME = new SimpleAttributeDefinitionBuilder(JNDINAME_NAME, ModelType.STRING, false)
            .setXmlName(DataSource.Attribute.JNDI_NAME.getLocalName())
            .setAllowExpression(true)
            .setValidator(new ParameterValidator() {
                @Override
                public void validateParameter(String parameterName, ModelNode value) throws OperationFailedException {
                    if (value.isDefined()) {
                        if (value.getType() != ModelType.EXPRESSION) {
                            String str = value.asString();
                            if (!str.startsWith("java:/") && !str.startsWith("java:jboss/")) {
                                throw ConnectorLogger.ROOT_LOGGER.jndiNameInvalidFormat();
                            } else if (str.endsWith("/") || str.indexOf("//") != -1) {
                                throw ConnectorLogger.ROOT_LOGGER.jndiNameShouldValidate();
                            }
                        }
                    } else {
                        throw ConnectorLogger.ROOT_LOGGER.jndiNameRequired();
                    }
                }

                @Override
                public void validateResolvedParameter(String parameterName, ModelNode value) throws OperationFailedException {
                    validateParameter(parameterName, value.resolve());
                }
            })
            .build();


    static SimpleAttributeDefinition DATASOURCE_DRIVER = new SimpleAttributeDefinitionBuilder(DATASOURCE_DRIVER_NAME, ModelType.STRING, false)
            .setXmlName(DataSource.Tag.DRIVER.getLocalName())
            .setAllowExpression(true)
            .build();


    static SimpleAttributeDefinition NEW_CONNECTION_SQL = new SimpleAttributeDefinitionBuilder(NEW_CONNECTION_SQL_NAME, ModelType.STRING, true)
            .setAllowExpression(true)
            .setXmlName(DataSource.Tag.NEW_CONNECTION_SQL.getLocalName())
            .build();

    static SimpleAttributeDefinition URL_DELIMITER = new SimpleAttributeDefinitionBuilder(URL_DELIMITER_NAME, ModelType.STRING, true)
            .setXmlName(DataSource.Tag.URL_DELIMITER.getLocalName())
            .setAllowExpression(true)
            .build();


    static SimpleAttributeDefinition URL_PROPERTY = new SimpleAttributeDefinitionBuilder(URL_PROPERTY_NAME, ModelType.STRING)
            .setAllowExpression(true)
            .setAllowNull(true)
            .setXmlName(XaDataSource.Tag.URL_PROPERTY.getLocalName())
            .build();

    static SimpleAttributeDefinition URL_SELECTOR_STRATEGY_CLASS_NAME = new SimpleAttributeDefinitionBuilder(URL_SELECTOR_STRATEGY_CLASS_NAME_NAME, ModelType.STRING, true)
            .setXmlName(DataSource.Tag.URL_SELECTOR_STRATEGY_CLASS_NAME.getLocalName())
            .setAllowExpression(true)
            .build();

    static SimpleAttributeDefinition USE_JAVA_CONTEXT = new SimpleAttributeDefinitionBuilder(USE_JAVA_CONTEXT_NAME, ModelType.BOOLEAN, true)
            .setXmlName(DataSource.Attribute.USE_JAVA_CONTEXT.getLocalName())
            .setDefaultValue(new ModelNode(Defaults.USE_JAVA_CONTEXT))
            .setAllowExpression(true)
            .build();

    static SimpleAttributeDefinition ENABLED = new SimpleAttributeDefinitionBuilder(ENABLED_NAME, ModelType.BOOLEAN)
            .setXmlName(DataSource.Attribute.ENABLED.getLocalName())
            .setAllowExpression(false)
            .setDefaultValue(new ModelNode(Defaults.ENABLED))
            .setAllowNull(true)
            .build();

    static SimpleAttributeDefinition CONNECTABLE = new SimpleAttributeDefinitionBuilder(CONNECTABLE_NAME, ModelType.BOOLEAN)
            .setXmlName(DataSource.Attribute.CONNECTABLE.getLocalName())
            .setAllowExpression(true)
            .setDefaultValue(new ModelNode(Defaults.CONNECTABLE))
            .setAllowNull(true)
            .build();

    static SimpleAttributeDefinition TRACKING = new SimpleAttributeDefinitionBuilder(TRACKING_NAME, ModelType.BOOLEAN)
            .setXmlName(DataSource.Attribute.TRACKING.getLocalName())
            .setDefaultValue(new ModelNode(false))
            .setAllowExpression(true)
            .setAllowNull(true)
            .build();

    static SimpleAttributeDefinition JTA = new SimpleAttributeDefinitionBuilder(JTA_NAME, ModelType.BOOLEAN, true)
            .setXmlName(DataSource.Attribute.JTA.getLocalName())
            .setDefaultValue(new ModelNode(Defaults.JTA))
            .setAllowExpression(true)
            .build();

    static SimpleAttributeDefinition CONNECTION_PROPERTIES = new SimpleAttributeDefinitionBuilder(CONNECTION_PROPERTIES_NAME, ModelType.STRING, true)
            .setXmlName(DataSource.Tag.CONNECTION_PROPERTY.getLocalName())
            .setAllowExpression(true)
            .build();

    static SimpleAttributeDefinition CONNECTION_PROPERTY_VALUE = new SimpleAttributeDefinitionBuilder(CONNECTION_PROPERTY_VALUE_NAME, ModelType.STRING, true)
            .setXmlName(DataSource.Tag.CONNECTION_PROPERTY.getLocalName())
            .setAllowExpression(true)
            .build();

    public static SimpleAttributeDefinition USERNAME = new SimpleAttributeDefinitionBuilder(USERNAME_NAME, ModelType.STRING)
            .setXmlName(Credential.Tag.USER_NAME.getLocalName())
            .setAllowExpression(true)
            .setAllowNull(true)
            .addAlternatives(SECURITY_DOMAIN_NAME)
            .addAccessConstraint(SensitiveTargetAccessConstraintDefinition.CREDENTIAL)
            .addAccessConstraint(DS_SECURITY_DEF)
            .build();

    public static SimpleAttributeDefinition PASSWORD = new SimpleAttributeDefinitionBuilder(PASSWORD_NAME, ModelType.STRING)
            .setXmlName(Credential.Tag.PASSWORD.getLocalName())
            .setAllowExpression(true)
            .setAllowNull(true)
            .setRequires(USERNAME_NAME)
            .addAccessConstraint(SensitiveTargetAccessConstraintDefinition.CREDENTIAL)
            .addAccessConstraint(DS_SECURITY_DEF)
            .build();

    static SimpleAttributeDefinition SECURITY_DOMAIN = new SimpleAttributeDefinitionBuilder(SECURITY_DOMAIN_NAME, ModelType.STRING)
            .setXmlName(Security.Tag.SECURITY_DOMAIN.getLocalName())
            .setAllowExpression(true)
            .setAllowNull(true)
            .addAlternatives(USERNAME_NAME)
            .addAccessConstraint(SensitiveTargetAccessConstraintDefinition.SECURITY_DOMAIN_REF)
            .addAccessConstraint(DS_SECURITY_DEF)
            .build();

    static SimpleAttributeDefinition PREPARED_STATEMENTS_CACHE_SIZE = new SimpleAttributeDefinitionBuilder(PREPAREDSTATEMENTSCACHESIZE_NAME, ModelType.LONG, true)
            .setAllowExpression(true)
            .setXmlName(Statement.Tag.PREPARED_STATEMENT_CACHE_SIZE.getLocalName())
            .build();


    static SimpleAttributeDefinition SHARE_PREPARED_STATEMENTS = new SimpleAttributeDefinitionBuilder(SHAREPREPAREDSTATEMENTS_NAME, ModelType.BOOLEAN, true)
            .setXmlName(Statement.Tag.SHARE_PREPARED_STATEMENTS.getLocalName())
            .setDefaultValue(new ModelNode(Defaults.SHARE_PREPARED_STATEMENTS))
            .setAllowExpression(true)
            .build();


    static SimpleAttributeDefinition TRACK_STATEMENTS = new SimpleAttributeDefinitionBuilder(TRACKSTATEMENTS_NAME, ModelType.STRING, true)
            .setAllowExpression(true)
            .setXmlName(Statement.Tag.TRACK_STATEMENTS.getLocalName())
            .setDefaultValue(new ModelNode(Defaults.TRACK_STATEMENTS.name()))
            .build();

    static SimpleAttributeDefinition ALLOCATION_RETRY = new SimpleAttributeDefinitionBuilder(ALLOCATION_RETRY_NAME, ModelType.INT, true)
            .setXmlName(TimeOut.Tag.ALLOCATION_RETRY.getLocalName())
            .setAllowExpression(true)
            .build();


    static SimpleAttributeDefinition ALLOCATION_RETRY_WAIT_MILLIS = new SimpleAttributeDefinitionBuilder(ALLOCATION_RETRY_WAIT_MILLIS_NAME, ModelType.LONG, true)
            .setXmlName(TimeOut.Tag.ALLOCATION_RETRY_WAIT_MILLIS.getLocalName())
            .setAllowExpression(true)
            .build();


    static SimpleAttributeDefinition ALLOW_MULTIPLE_USERS = new SimpleAttributeDefinitionBuilder(ALLOW_MULTIPLE_USERS_NAME, ModelType.BOOLEAN, true)
            .setXmlName(DsPool.Tag.ALLOW_MULTIPLE_USERS.getLocalName())
            .setDefaultValue(new ModelNode(Defaults.ALLOW_MULTIPLE_USERS))
            .setAllowExpression(false)
            .build();

    static SimpleAttributeDefinition CONNECTION_LISTENER_CLASS = new SimpleAttributeDefinitionBuilder(CONNECTION_LISTENER_CLASS_NAME, ModelType.STRING)
            .setAllowExpression(true)
            .setAllowNull(true)
            .setXmlName(org.jboss.jca.common.api.metadata.common.Extension.Attribute.CLASS_NAME.getLocalName())
            .build();
    static PropertiesAttributeDefinition CONNECTION_LISTENER_PROPERTIES = new PropertiesAttributeDefinition.Builder(CONNECTION_LISTENER_PROPERTY_NAME, true)
            .setXmlName(org.jboss.jca.common.api.metadata.common.Extension.Tag.CONFIG_PROPERTY.getLocalName())
            .setAllowNull(true)
            .setAllowExpression(true)
            .build();


    static SimpleAttributeDefinition QUERY_TIMEOUT = new SimpleAttributeDefinitionBuilder(QUERYTIMEOUT_NAME, ModelType.LONG, true)
            .setXmlName(TimeOut.Tag.QUERY_TIMEOUT.getLocalName())
            .setAllowExpression(true)
            .build();

    static SimpleAttributeDefinition USE_TRY_LOCK = new SimpleAttributeDefinitionBuilder(USETRYLOCK_NAME, ModelType.LONG, true)
            .setXmlName(TimeOut.Tag.USE_TRY_LOCK.getLocalName())
            .setAllowExpression(true)
            .build();

    static SimpleAttributeDefinition SET_TX_QUERY_TIMEOUT = new SimpleAttributeDefinitionBuilder(SETTXQUERYTIMEOUT_NAME, ModelType.BOOLEAN, true)
            .setXmlName(TimeOut.Tag.SET_TX_QUERY_TIMEOUT.getLocalName())
            .setDefaultValue(new ModelNode(Defaults.SET_TX_QUERY_TIMEOUT))
            .setAllowExpression(true)
            .build();

    static SimpleAttributeDefinition TRANSACTION_ISOLATION = new SimpleAttributeDefinitionBuilder(TRANSACTION_ISOLATION_NAME, ModelType.STRING, true)
            .setXmlName(DataSource.Tag.TRANSACTION_ISOLATION.getLocalName())
            .setAllowExpression(true)
            .build();

    static SimpleAttributeDefinition CHECK_VALID_CONNECTION_SQL = new SimpleAttributeDefinitionBuilder(CHECKVALIDCONNECTIONSQL_NAME, ModelType.STRING, true)
            .setXmlName(Validation.Tag.CHECK_VALID_CONNECTION_SQL.getLocalName())
            .setAllowExpression(true)
            .build();

    static SimpleAttributeDefinition EXCEPTION_SORTER_CLASSNAME = new SimpleAttributeDefinitionBuilder(EXCEPTIONSORTERCLASSNAME_NAME, ModelType.STRING, true)
            .setXmlName(org.jboss.jca.common.api.metadata.common.Extension.Attribute.CLASS_NAME.getLocalName())
            .setAllowExpression(true)
            .build();

    static PropertiesAttributeDefinition EXCEPTION_SORTER_PROPERTIES = new PropertiesAttributeDefinition.Builder(EXCEPTIONSORTER_PROPERTIES_NAME, true)
            .setXmlName(org.jboss.jca.common.api.metadata.common.Extension.Tag.CONFIG_PROPERTY.getLocalName())
            .setAllowExpression(true)
            .setRequires(EXCEPTIONSORTERCLASSNAME_NAME)
            .build();


    static SimpleAttributeDefinition STALE_CONNECTION_CHECKER_CLASSNAME = new SimpleAttributeDefinitionBuilder(STALECONNECTIONCHECKERCLASSNAME_NAME, ModelType.STRING, true)
            .setXmlName(org.jboss.jca.common.api.metadata.common.Extension.Attribute.CLASS_NAME.getLocalName())
            .setAllowExpression(true)
            .build();

    static PropertiesAttributeDefinition STALE_CONNECTION_CHECKER_PROPERTIES = new PropertiesAttributeDefinition.Builder(STALECONNECTIONCHECKER_PROPERTIES_NAME, true)
            .setXmlName(org.jboss.jca.common.api.metadata.common.Extension.Tag.CONFIG_PROPERTY.getLocalName())
            .setAllowNull(true)
            .setAllowExpression(true)
            .setRequires(STALECONNECTIONCHECKERCLASSNAME_NAME)
            .build();

    static SimpleAttributeDefinition VALID_CONNECTION_CHECKER_CLASSNAME = new SimpleAttributeDefinitionBuilder(VALID_CONNECTION_CHECKER_CLASSNAME_NAME, ModelType.STRING, true)
            .setXmlName(org.jboss.jca.common.api.metadata.common.Extension.Attribute.CLASS_NAME.getLocalName())
            .setAllowExpression(true)
            .build();

    static PropertiesAttributeDefinition VALID_CONNECTION_CHECKER_PROPERTIES = new PropertiesAttributeDefinition.Builder(VALIDCONNECTIONCHECKER_PROPERTIES_NAME, true)
            .setXmlName(org.jboss.jca.common.api.metadata.common.Extension.Tag.CONFIG_PROPERTY.getLocalName())
            .setAllowNull(true)
            .setAllowExpression(true)
            .setRequires(VALID_CONNECTION_CHECKER_CLASSNAME_NAME)
            .build();

    static SimpleAttributeDefinition VALIDATE_ON_MATCH = new SimpleAttributeDefinitionBuilder(VALIDATEONMATCH_NAME, ModelType.BOOLEAN, true)
            .setXmlName(Validation.Tag.VALIDATE_ON_MATCH.getLocalName())
            .setAllowExpression(true)
            .build();

    static SimpleAttributeDefinition SPY = new SimpleAttributeDefinitionBuilder(SPY_NAME, ModelType.BOOLEAN, true)
            .setXmlName(DataSource.Attribute.SPY.getLocalName())
            .setDefaultValue(new ModelNode(Defaults.SPY))
            .setAllowExpression(true)
            .build();

    static SimpleAttributeDefinition USE_CCM = new SimpleAttributeDefinitionBuilder(USE_CCM_NAME, ModelType.BOOLEAN, true)
            .setXmlName(DataSource.Attribute.USE_CCM.getLocalName())
            .setAllowExpression(true)
            .setDefaultValue(new ModelNode(Defaults.USE_CCM))
            .build();

    static SimpleAttributeDefinition XA_DATASOURCE_CLASS = new SimpleAttributeDefinitionBuilder(XADATASOURCECLASS_NAME, ModelType.STRING, true)
            .setXmlName(XaDataSource.Tag.XA_DATASOURCE_CLASS.getLocalName())
            .setAllowExpression(true)
            .build();

    static SimpleAttributeDefinition INTERLEAVING = new SimpleAttributeDefinitionBuilder(INTERLEAVING_NAME, ModelType.BOOLEAN, true)
            .setXmlName(XaPool.Tag.INTERLEAVING.getLocalName())
            .setAllowExpression(true)
            .setDefaultValue(new ModelNode(Defaults.INTERLEAVING))
            .build();

    static SimpleAttributeDefinition NO_TX_SEPARATE_POOL = new SimpleAttributeDefinitionBuilder(NOTXSEPARATEPOOL_NAME, ModelType.BOOLEAN, true)
            .setXmlName(XaPool.Tag.NO_TX_SEPARATE_POOLS.getLocalName())
            .setDefaultValue(new ModelNode(Defaults.NO_TX_SEPARATE_POOL))
            .setAllowExpression(true)
            .build();

    static SimpleAttributeDefinition PAD_XID = new SimpleAttributeDefinitionBuilder(PAD_XID_NAME, ModelType.BOOLEAN, true)
            .setXmlName(XaPool.Tag.PAD_XID.getLocalName())
            .setDefaultValue(new ModelNode(Defaults.PAD_XID))
            .setAllowExpression(true)
            .build();

    static SimpleAttributeDefinition SAME_RM_OVERRIDE = new SimpleAttributeDefinitionBuilder(SAME_RM_OVERRIDE_NAME, ModelType.BOOLEAN, true)
            .setXmlName(XaPool.Tag.IS_SAME_RM_OVERRIDE.getLocalName())
            .setAllowExpression(true)
            .build();

    static SimpleAttributeDefinition WRAP_XA_RESOURCE = new SimpleAttributeDefinitionBuilder(WRAP_XA_RESOURCE_NAME, ModelType.BOOLEAN, true)
            .setXmlName(XaPool.Tag.WRAP_XA_RESOURCE.getLocalName())
            .setDefaultValue(new ModelNode(Defaults.WRAP_XA_RESOURCE))
            .setAllowExpression(true)
            .build();

    static SimpleAttributeDefinition XA_RESOURCE_TIMEOUT = new SimpleAttributeDefinitionBuilder(XA_RESOURCE_TIMEOUT_NAME, ModelType.INT, true)
            .setXmlName(TimeOut.Tag.XA_RESOURCE_TIMEOUT.getLocalName())
            .setAllowExpression(true)
            .build();

    static SimpleAttributeDefinition REAUTH_PLUGIN_CLASSNAME = new SimpleAttributeDefinitionBuilder(REAUTHPLUGIN_CLASSNAME_NAME, ModelType.STRING, true)
            .setXmlName(org.jboss.jca.common.api.metadata.common.Extension.Attribute.CLASS_NAME.getLocalName())
            .setAllowExpression(true)
            .build();

    static PropertiesAttributeDefinition REAUTHPLUGIN_PROPERTIES = new PropertiesAttributeDefinition.Builder(REAUTHPLUGIN_PROPERTIES_NAME, true)
            .setXmlName(org.jboss.jca.common.api.metadata.common.Extension.Tag.CONFIG_PROPERTY.getLocalName())
            .setAllowNull(true)
            .setAllowExpression(true)
            .setRequires(REAUTHPLUGIN_CLASSNAME_NAME)
            .build();

    static SimpleAttributeDefinition STATISTICS_ENABLED = new SimpleAttributeDefinitionBuilder(ModelDescriptionConstants.STATISTICS_ENABLED, ModelType.BOOLEAN)
            .setDefaultValue(new ModelNode(false))
            .setAllowNull(true)
            .setAllowExpression(true)
            .build();


    static final List<SimpleAttributeDefinition> DATASOURCE_ATTRIBUTE_RELOAD_REQUIRED = Arrays.asList(JTA, STATISTICS_ENABLED);
    static final SimpleAttributeDefinition[] DATASOURCE_ATTRIBUTE = new SimpleAttributeDefinition[]{CONNECTION_URL,
            DRIVER_CLASS, Constants.DATASOURCE_CLASS, JNDI_NAME,
            DATASOURCE_DRIVER,
            NEW_CONNECTION_SQL, URL_DELIMITER,
            URL_SELECTOR_STRATEGY_CLASS_NAME, USE_JAVA_CONTEXT,
            JTA, org.jboss.as.connector.subsystems.common.pool.Constants.MAX_POOL_SIZE,
            org.jboss.as.connector.subsystems.common.pool.Constants.MIN_POOL_SIZE, org.jboss.as.connector.subsystems.common.pool.Constants.INITIAL_POOL_SIZE,
            org.jboss.as.connector.subsystems.common.pool.Constants.POOL_PREFILL, org.jboss.as.connector.subsystems.common.pool.Constants.POOL_USE_STRICT_MIN,
            org.jboss.as.connector.subsystems.common.pool.Constants.CAPACITY_INCREMENTER_CLASS, org.jboss.as.connector.subsystems.common.pool.Constants.CAPACITY_DECREMENTER_CLASS,
            USERNAME, PASSWORD, SECURITY_DOMAIN,
            REAUTH_PLUGIN_CLASSNAME,
            org.jboss.as.connector.subsystems.common.pool.Constants.POOL_FLUSH_STRATEGY,
            ALLOW_MULTIPLE_USERS, CONNECTION_LISTENER_CLASS, CONNECTION_PROPERTIES,
            PREPARED_STATEMENTS_CACHE_SIZE,
            SHARE_PREPARED_STATEMENTS,
            TRACK_STATEMENTS,
            ALLOCATION_RETRY,
            ALLOCATION_RETRY_WAIT_MILLIS,
            org.jboss.as.connector.subsystems.common.pool.Constants.BLOCKING_TIMEOUT_WAIT_MILLIS, org.jboss.as.connector.subsystems.common.pool.Constants.IDLETIMEOUTMINUTES,
            QUERY_TIMEOUT,
            USE_TRY_LOCK,
            SET_TX_QUERY_TIMEOUT,
            TRANSACTION_ISOLATION,
            CHECK_VALID_CONNECTION_SQL,
            EXCEPTION_SORTER_CLASSNAME,
            STALE_CONNECTION_CHECKER_CLASSNAME,
            VALID_CONNECTION_CHECKER_CLASSNAME,
            org.jboss.as.connector.subsystems.common.pool.Constants.BACKGROUNDVALIDATIONMILLIS,
            org.jboss.as.connector.subsystems.common.pool.Constants.BACKGROUNDVALIDATION,
            org.jboss.as.connector.subsystems.common.pool.Constants.USE_FAST_FAIL,
            VALIDATE_ON_MATCH, SPY,
            USE_CCM, ENABLED, CONNECTABLE, STATISTICS_ENABLED, TRACKING};

    static final PropertiesAttributeDefinition[] DATASOURCE_PROPERTIES_ATTRIBUTES = new PropertiesAttributeDefinition[]{
            REAUTHPLUGIN_PROPERTIES,
            EXCEPTION_SORTER_PROPERTIES,
            STALE_CONNECTION_CHECKER_PROPERTIES,
            VALID_CONNECTION_CHECKER_PROPERTIES, CONNECTION_LISTENER_PROPERTIES,
            org.jboss.as.connector.subsystems.common.pool.Constants.CAPACITY_INCREMENTER_PROPERTIES, org.jboss.as.connector.subsystems.common.pool.Constants.CAPACITY_DECREMENTER_PROPERTIES,

    };

    static SimpleAttributeDefinition RECOVERY_USERNAME = new SimpleAttributeDefinitionBuilder(RECOVERY_USERNAME_NAME, ModelType.STRING)
            .setXmlName(Credential.Tag.USER_NAME.getLocalName())
            .setAllowExpression(true)
            .setAllowNull(true)
            .addAlternatives(RECOVERY_SECURITY_DOMAIN_NAME)
            .addAccessConstraint(SensitiveTargetAccessConstraintDefinition.CREDENTIAL)
            .build();

    static SimpleAttributeDefinition RECOVERY_PASSWORD = new SimpleAttributeDefinitionBuilder(RECOVERY_PASSWORD_NAME, ModelType.STRING)
            .setXmlName(Credential.Tag.PASSWORD.getLocalName())
            .setAllowExpression(true)
            .setAllowNull(true)
            .setRequires(RECOVERY_USERNAME_NAME)
            .addAccessConstraint(SensitiveTargetAccessConstraintDefinition.CREDENTIAL)
            .build();

    static SimpleAttributeDefinition RECOVERY_SECURITY_DOMAIN = new SimpleAttributeDefinitionBuilder(RECOVERY_SECURITY_DOMAIN_NAME, ModelType.STRING)
            .setXmlName(Security.Tag.SECURITY_DOMAIN.getLocalName())
            .setAllowExpression(true)
            .setAllowNull(true)
            .addAlternatives(RECOVERY_USERNAME_NAME)
            .build();


    static SimpleAttributeDefinition RECOVER_PLUGIN_CLASSNAME = new SimpleAttributeDefinitionBuilder(RECOVER_PLUGIN_CLASSNAME_NAME, ModelType.STRING, true)
            .setXmlName(org.jboss.jca.common.api.metadata.common.Extension.Attribute.CLASS_NAME.getLocalName())
            .setAllowExpression(true)
            .build();

    static PropertiesAttributeDefinition RECOVER_PLUGIN_PROPERTIES = new PropertiesAttributeDefinition.Builder(RECOVER_PLUGIN_PROPERTIES_NAME, true)
            .setXmlName(org.jboss.jca.common.api.metadata.common.Extension.Tag.CONFIG_PROPERTY.getLocalName())
            .setAllowNull(true)
            .setAllowExpression(true)
            .build();

    static SimpleAttributeDefinition NO_RECOVERY = new SimpleAttributeDefinitionBuilder(NO_RECOVERY_NAME, ModelType.BOOLEAN, true)
            .setXmlName(Recovery.Attribute.NO_RECOVERY.getLocalName())
            .setAllowExpression(true)
            .build();
    static final SimpleAttributeDefinition[] XA_DATASOURCE_ATTRIBUTE = new SimpleAttributeDefinition[]{
            Constants.XA_DATASOURCE_CLASS, JNDI_NAME, DATASOURCE_DRIVER,
            NEW_CONNECTION_SQL, URL_DELIMITER,
            URL_SELECTOR_STRATEGY_CLASS_NAME, USE_JAVA_CONTEXT,
            org.jboss.as.connector.subsystems.common.pool.Constants.MAX_POOL_SIZE, org.jboss.as.connector.subsystems.common.pool.Constants.MIN_POOL_SIZE, org.jboss.as.connector.subsystems.common.pool.Constants.INITIAL_POOL_SIZE,
            org.jboss.as.connector.subsystems.common.pool.Constants.POOL_PREFILL, org.jboss.as.connector.subsystems.common.pool.Constants.POOL_USE_STRICT_MIN, INTERLEAVING,
            org.jboss.as.connector.subsystems.common.pool.Constants.CAPACITY_INCREMENTER_CLASS, org.jboss.as.connector.subsystems.common.pool.Constants.CAPACITY_DECREMENTER_CLASS,
            NO_TX_SEPARATE_POOL, PAD_XID, SAME_RM_OVERRIDE,
            WRAP_XA_RESOURCE, USERNAME, PASSWORD,
            SECURITY_DOMAIN,
            REAUTH_PLUGIN_CLASSNAME,
            org.jboss.as.connector.subsystems.common.pool.Constants.POOL_FLUSH_STRATEGY, ALLOW_MULTIPLE_USERS, CONNECTION_LISTENER_CLASS,
            PREPARED_STATEMENTS_CACHE_SIZE,
            SHARE_PREPARED_STATEMENTS, TRACK_STATEMENTS,
            ALLOCATION_RETRY, ALLOCATION_RETRY_WAIT_MILLIS,
            org.jboss.as.connector.subsystems.common.pool.Constants.BLOCKING_TIMEOUT_WAIT_MILLIS, org.jboss.as.connector.subsystems.common.pool.Constants.IDLETIMEOUTMINUTES,
            QUERY_TIMEOUT, USE_TRY_LOCK, SET_TX_QUERY_TIMEOUT,
            TRANSACTION_ISOLATION, CHECK_VALID_CONNECTION_SQL,
            EXCEPTION_SORTER_CLASSNAME,
            STALE_CONNECTION_CHECKER_CLASSNAME,
            VALID_CONNECTION_CHECKER_CLASSNAME,
            org.jboss.as.connector.subsystems.common.pool.Constants.BACKGROUNDVALIDATIONMILLIS,
            org.jboss.as.connector.subsystems.common.pool.Constants.BACKGROUNDVALIDATION,
            org.jboss.as.connector.subsystems.common.pool.Constants.USE_FAST_FAIL,
            VALIDATE_ON_MATCH, XA_RESOURCE_TIMEOUT,
            SPY, USE_CCM, ENABLED, CONNECTABLE, STATISTICS_ENABLED, TRACKING,
            RECOVERY_USERNAME, RECOVERY_PASSWORD,
            RECOVERY_SECURITY_DOMAIN, RECOVER_PLUGIN_CLASSNAME,
            NO_RECOVERY, URL_PROPERTY};

    static final PropertiesAttributeDefinition[] XA_DATASOURCE_PROPERTIES_ATTRIBUTES = new PropertiesAttributeDefinition[]{
            REAUTHPLUGIN_PROPERTIES,
            EXCEPTION_SORTER_PROPERTIES,
            STALE_CONNECTION_CHECKER_PROPERTIES,
            VALID_CONNECTION_CHECKER_PROPERTIES,
            RECOVER_PLUGIN_PROPERTIES, CONNECTION_LISTENER_PROPERTIES,
            org.jboss.as.connector.subsystems.common.pool.Constants.CAPACITY_INCREMENTER_PROPERTIES, org.jboss.as.connector.subsystems.common.pool.Constants.CAPACITY_DECREMENTER_PROPERTIES,
    };

    static SimpleAttributeDefinition XADATASOURCE_PROPERTIES = new SimpleAttributeDefinitionBuilder(XADATASOURCEPROPERTIES_NAME, ModelType.STRING, false)
            .setXmlName(XaDataSource.Tag.XA_DATASOURCE_PROPERTY.getLocalName())
            .setAllowExpression(true)
            .build();

    static SimpleAttributeDefinition XADATASOURCE_PROPERTY_VALUE = new SimpleAttributeDefinitionBuilder(XADATASOURCEPROPERTIES_VALUE_NAME, ModelType.STRING, true)
            .setXmlName(XaDataSource.Tag.XA_DATASOURCE_PROPERTY.getLocalName())
            .setAllowExpression(true)
            .build();

    static final SimpleAttributeDefinition DRIVER_NAME = new SimpleAttributeDefinitionBuilder(DRIVER_NAME_NAME, ModelType.STRING)
            .setXmlName(Driver.Attribute.NAME.getLocalName())
            .setAllowNull(false)
                    //.setResourceOnly()
            .build();

    static final SimpleAttributeDefinition DRIVER_MODULE_NAME = new SimpleAttributeDefinitionBuilder(DRIVER_MODULE_NAME_NAME, ModelType.STRING)
            .setXmlName(Driver.Attribute.MODULE.getLocalName())
            .setAllowExpression(false)
            .build();

    static final SimpleAttributeDefinition DRIVER_MAJOR_VERSION = new SimpleAttributeDefinitionBuilder(DRIVER_MAJOR_VERSION_NAME, ModelType.INT)
            .setAllowNull(true)
            .setAllowExpression(true)
            .setXmlName(Driver.Attribute.MAJOR_VERSION.getLocalName())
            .build();


    static final SimpleAttributeDefinition DRIVER_MINOR_VERSION = new SimpleAttributeDefinitionBuilder(DRIVER_MINOR_VERSION_NAME, ModelType.INT)
            .setAllowNull(true)
            .setAllowExpression(true)
            .setXmlName(Driver.Attribute.MINOR_VERSION.getLocalName())
            .build();
    static final SimpleAttributeDefinition DRIVER_CLASS_NAME = new SimpleAttributeDefinitionBuilder(DRIVER_CLASS_NAME_NAME, ModelType.STRING)
            .setXmlName(Driver.Tag.DRIVER_CLASS.getLocalName())
            .setAllowNull(true)
            .setAllowExpression(false)
            .build();

    static final SimpleAttributeDefinition DRIVER_DATASOURCE_CLASS_NAME = new SimpleAttributeDefinitionBuilder(DRIVER_DATASOURCE_CLASS_NAME_NAME, ModelType.STRING)
            .setXmlName(Driver.Tag.DATASOURCE_CLASS.getLocalName())
            .setAllowNull(true)
            .setAllowExpression(true)
            .build();


    static final SimpleAttributeDefinition DRIVER_XA_DATASOURCE_CLASS_NAME = new SimpleAttributeDefinitionBuilder(DRIVER_XA_DATASOURCE_CLASS_NAME_NAME, ModelType.STRING)
            .setXmlName(Driver.Tag.XA_DATASOURCE_CLASS.getLocalName())
            .setAllowNull(true)
            .setAllowExpression(false)
            .build();

    static final SimpleAttributeDefinition[] JDBC_DRIVER_ATTRIBUTES = {
            DEPLOYMENT_NAME,
            DRIVER_NAME,
            DRIVER_MODULE_NAME,
            MODULE_SLOT,
            DRIVER_CLASS_NAME,
            DRIVER_DATASOURCE_CLASS_NAME,
            DRIVER_XA_DATASOURCE_CLASS_NAME,
            XA_DATASOURCE_CLASS,
            DRIVER_MAJOR_VERSION,
            DRIVER_MINOR_VERSION,
            JDBC_COMPLIANT
    };


    static final ObjectTypeAttributeDefinition INSTALLED_DRIVER = ObjectTypeAttributeDefinition.Builder.of("installed-driver", JDBC_DRIVER_ATTRIBUTES).build();
    static final ObjectListAttributeDefinition INSTALLED_DRIVERS = ObjectListAttributeDefinition.Builder.of("installed-drivers", INSTALLED_DRIVER)
            .setResourceOnly().setFlags(AttributeAccess.Flag.STORAGE_RUNTIME)
            .build();

    //static final SimpleOperationDefinition INSTALLED_DRIVERS_LIST = new SimpleOperationDefinitionBuilder("installed-drivers-list", DataSourcesExtension.getResourceDescriptionResolver())
    static final SimpleOperationDefinition INSTALLED_DRIVERS_LIST = new SimpleOperationDefinitionBuilder("installed-drivers-list", new NonResolvingResourceDescriptionResolver())
            .setRuntimeOnly()
            .setReplyType(ModelType.LIST)
            .setReplyParameters(JDBC_DRIVER_ATTRIBUTES)
            .build();
    static final SimpleOperationDefinition GET_INSTALLED_DRIVER = new SimpleOperationDefinitionBuilder("get-installed-driver", DataSourcesExtension.getResourceDescriptionResolver())
            .setRuntimeOnly()
            .setParameters(DRIVER_NAME)
            .setReplyParameters(DRIVER_MINOR_VERSION, DRIVER_MAJOR_VERSION, DEPLOYMENT_NAME, DRIVER_NAME, DRIVER_XA_DATASOURCE_CLASS_NAME, XA_DATASOURCE_CLASS, JDBC_COMPLIANT, MODULE_SLOT, DRIVER_CLASS_NAME, DRIVER_MODULE_NAME)
            .setAttributeResolver(DataSourcesExtension.getResourceDescriptionResolver("jdbc-driver"))
            .build();
    static final SimpleOperationDefinition DATASOURCE_ENABLE = new SimpleOperationDefinitionBuilder(ENABLE, DataSourcesExtension.getResourceDescriptionResolver()).build();
    static final SimpleOperationDefinition DATASOURCE_DISABLE = new SimpleOperationDefinitionBuilder(DISABLE, DataSourcesExtension.getResourceDescriptionResolver())
            .build();
    static final SimpleOperationDefinition FLUSH_IDLE_CONNECTION = new SimpleOperationDefinitionBuilder("flush-idle-connection-in-pool", DataSourcesExtension.getResourceDescriptionResolver())
            .setRuntimeOnly().build();
    static final SimpleOperationDefinition FLUSH_ALL_CONNECTION = new SimpleOperationDefinitionBuilder("flush-all-connection-in-pool", DataSourcesExtension.getResourceDescriptionResolver())
            .setRuntimeOnly().build();
    static final SimpleOperationDefinition DUMP_QUEUED_THREADS = new SimpleOperationDefinitionBuilder("dump-queued-threads-in-pool", DataSourcesExtension.getResourceDescriptionResolver())
            .setRuntimeOnly().build();
    static final SimpleOperationDefinition FLUSH_INVALID_CONNECTION = new SimpleOperationDefinitionBuilder("flush-invalid-connection-in-pool", DataSourcesExtension.getResourceDescriptionResolver())
            .setRuntimeOnly().build();
    static final SimpleOperationDefinition FLUSH_GRACEFULLY_CONNECTION = new SimpleOperationDefinitionBuilder("flush-gracefully-connection-in-pool", DataSourcesExtension.getResourceDescriptionResolver())
            .setRuntimeOnly().build();
    static final SimpleOperationDefinition TEST_CONNECTION = new SimpleOperationDefinitionBuilder("test-connection-in-pool", DataSourcesExtension.getResourceDescriptionResolver())
            .setParameters(USERNAME, PASSWORD)
            .setRuntimeOnly().build();

    static final OperationTransformer ENABLE_TRANSFORMER = new OperationTransformer() {
        @Override
        public TransformedOperation transformOperation(TransformationContext context, PathAddress address, ModelNode operation) throws OperationFailedException {

            final String attributeName = operation.require(NAME).asString();
            if (ENABLED.getName().equals(attributeName)) {
                final ModelNode transformed = new ModelNode();
                //If using the same transformer for UNDEFINE_ATTRIBUTE as well, check if it is undefined and set whatever is default
                ModelNode value = operation.get(VALUE);
                boolean booleanValue = value.isDefined() ? value.asBoolean() : Defaults.ENABLED;
                transformed.get(OP).set(booleanValue ? ENABLE : DISABLE);
                transformed.get(OP_ADDR).set(address.toModelNode());

                return new TransformedOperation(transformed, OperationResultTransformer.ORIGINAL_RESULT);
            }
            return new TransformedOperation(operation, OperationResultTransformer.ORIGINAL_RESULT);
        }
    };

    static final OperationTransformer ENABLE_ADD_TRANSFORMER = new OperationTransformer() {
        @Override
        public TransformedOperation transformOperation(TransformationContext context, PathAddress address, ModelNode operation) throws OperationFailedException {
            if (operation.hasDefined(ENABLED.getName()) && operation.get(ENABLED.getName()).asBoolean()) {
                ModelNode add = operation.clone();
                add.remove(ENABLED.getName());

                ModelNode composite = new ModelNode();
                composite.get(OP).set(COMPOSITE);
                composite.get(OP_ADDR).setEmptyList();
                composite.get(STEPS).add(add);

                ModelNode enable = Util.createEmptyOperation(ENABLE, PathAddress.pathAddress(operation.get(OP_ADDR)));
                composite.get(STEPS).add(enable);

                return new TransformedOperation(composite, OperationResultTransformer.ORIGINAL_RESULT);

            } else {
                return new TransformedOperation(operation, OperationResultTransformer.ORIGINAL_RESULT);
            }
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

QDox parse error in file://<WORKSPACE>/data/code-rep-dataset/Dataset3/Tasks/857.java