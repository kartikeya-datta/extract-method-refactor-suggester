error id: file://<WORKSPACE>/data/code-rep-dataset/Dataset3/Tasks/9819.java
file://<WORKSPACE>/data/code-rep-dataset/Dataset3/Tasks/9819.java
### com.thoughtworks.qdox.parser.ParseException: syntax error @[1,1]

error in qdox parser
file content:
```java
offset: 1
uri: file://<WORKSPACE>/data/code-rep-dataset/Dataset3/Tasks/9819.java
text:
```scala
t@@hrow PicketLinkLogger.ROOT_LOGGER.typeAlreadyDefined(handler.getClazz());

/*
 * JBoss, Home of Professional Open Source.
 * Copyright 2012, Red Hat, Inc., and individual contributors
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
package org.wildfly.extension.picketlink.federation.service;

import org.jboss.as.server.deployment.DeploymentUnit;
import org.jboss.as.web.common.WarMetaData;
import org.jboss.msc.service.ServiceController;
import org.jboss.msc.service.StartContext;
import org.jboss.msc.service.StartException;
import org.jboss.msc.service.StopContext;
import org.jboss.msc.value.InjectedValue;
import org.picketlink.common.constants.GeneralConstants;
import org.picketlink.common.constants.JBossSAMLURIConstants;
import org.picketlink.common.exceptions.ConfigurationException;
import org.picketlink.config.federation.KeyValueType;
import org.picketlink.config.federation.PicketLinkType;
import org.picketlink.config.federation.ProviderType;
import org.picketlink.config.federation.STSType;
import org.picketlink.config.federation.TokenProviderType;
import org.picketlink.config.federation.handler.Handler;
import org.picketlink.config.federation.handler.Handlers;
import org.picketlink.config.federation.parsers.STSConfigParser;
import org.picketlink.identity.federation.core.saml.v2.interfaces.SAML2Handler;
import org.picketlink.identity.federation.web.handlers.saml2.RolesGenerationHandler;
import org.picketlink.identity.federation.web.handlers.saml2.SAML2AuthenticationHandler;
import org.picketlink.identity.federation.web.handlers.saml2.SAML2EncryptionHandler;
import org.picketlink.identity.federation.web.handlers.saml2.SAML2LogOutHandler;
import org.picketlink.identity.federation.web.handlers.saml2.SAML2SignatureValidationHandler;
import org.wildfly.extension.picketlink.federation.config.ProviderConfiguration;
import org.wildfly.extension.picketlink.federation.metrics.PicketLinkSubsystemMetrics;
import org.wildfly.extension.picketlink.logging.PicketLinkLogger;

import java.io.IOException;
import java.io.InputStream;
import java.net.URL;
import java.util.ArrayList;
import java.util.List;

import static org.wildfly.extension.picketlink.logging.PicketLinkLogger.ROOT_LOGGER;

/**
 * @author <a href="mailto:psilva@redhat.com">Pedro Silva</a>
 */
public abstract class EntityProviderService<T extends PicketLinkFederationService<T>, C extends ProviderConfiguration> implements PicketLinkFederationService<T> {

    private final InjectedValue<FederationService> federationService = new InjectedValue<FederationService>();
    private final PicketLinkType picketLinkType;
    private volatile PicketLinkSubsystemMetrics metrics;

    public EntityProviderService(C configuration) {
        this.picketLinkType = createPicketLinkType(configuration);
    }

    @Override
    public void start(StartContext context) throws StartException {
        ROOT_LOGGER.debugf("Starting service for %s.", getConfiguration().getAlias());
    }

    @Override
    public void stop(StopContext context) {
        ROOT_LOGGER.debugf("Stopping service for %s.", getConfiguration().getAlias());
        context.getController().setMode(ServiceController.Mode.REMOVE);
    }

    @Override
    public void configure(DeploymentUnit deploymentUnit) {
        configureHandlers();
        configureWarMetadata(deploymentUnit);
        configureWebComponents(deploymentUnit);
        doConfigureDeployment(deploymentUnit);
        configureTokenProviders();
        configureKeyProvider();
    }

    @Override
    public PicketLinkSubsystemMetrics getMetrics() {
        if (this.metrics == null) {
            synchronized (this) {
                if (this.metrics == null) {
                    try {
                        this.metrics = new PicketLinkSubsystemMetrics(((ProviderConfiguration) getPicketLinkType().getIdpOrSP()).getSecurityDomain());
                    } catch (ConfigurationException e) {
                        ROOT_LOGGER.federationErrorCollectingMetric(e);
                    }
                }
            }
        }

        return this.metrics;
    }

    protected List<Class<? extends SAML2Handler>> getDefaultHandlers() {
        List<Class<? extends SAML2Handler>> defaultHandlers = new ArrayList<Class<? extends SAML2Handler>>();

        defaultHandlers.add(SAML2LogOutHandler.class);
        defaultHandlers.add(SAML2AuthenticationHandler.class);
        defaultHandlers.add(RolesGenerationHandler.class);
        defaultHandlers.add(SAML2EncryptionHandler.class);
        defaultHandlers.add(SAML2SignatureValidationHandler.class);

        return defaultHandlers;
    }

    private void configureKeyProvider() {
        getConfiguration().setKeyProvider(getFederationService().getValue().getKeyProviderType());
    }

    /**
     * <p> Configure the STS Token Providers. </p>
     */
    private void configureTokenProviders() {
        STSType stsType = getFederationService().getValue().getStsType();

        if (stsType != null) {
            int tokenTimeout = stsType.getTokenTimeout();
            int clockSkew = stsType.getClockSkew();

            STSType providerStsType = getPicketLinkType().getStsType();

            providerStsType.setTokenTimeout(tokenTimeout);
            providerStsType.setClockSkew(clockSkew);

            List<TokenProviderType> tokenProviders = providerStsType.getTokenProviders().getTokenProvider();

            for (TokenProviderType tokenProviderType : tokenProviders) {
                if (tokenProviderType.getTokenType().equals(JBossSAMLURIConstants.ASSERTION_NSURI.get())) {
                    KeyValueType keyValueTypeTokenTimeout = new KeyValueType();

                    keyValueTypeTokenTimeout.setKey(GeneralConstants.ASSERTIONS_VALIDITY);
                    keyValueTypeTokenTimeout.setValue(String.valueOf(tokenTimeout));

                    KeyValueType keyValueTypeClockSkew = new KeyValueType();

                    keyValueTypeClockSkew.setKey(GeneralConstants.CLOCK_SKEW);
                    keyValueTypeClockSkew.setValue(String.valueOf(clockSkew));

                    tokenProviderType.add(keyValueTypeTokenTimeout);
                    tokenProviderType.add(keyValueTypeClockSkew);
                }
            }
        }
    }

    /**
     * <p> Configure the SAML Handlers. </p>
     */
    private void configureHandlers() {
        if (getPicketLinkType().getHandlers().getHandler().isEmpty()) {
            getPicketLinkType().setHandlers(new Handlers());

            for (Class<? extends SAML2Handler> commonHandlerClass : getDefaultHandlers()) {
                addHandler(commonHandlerClass, getPicketLinkType().getHandlers());
            }
        }
    }

    /**
     * <p> Hook for pre-configured handlers. </p>
     */
    protected void doAddHandlers() {

    }

    private void configureWarMetadata(DeploymentUnit deploymentUnit) {
        WarMetaData warMetaData = deploymentUnit.getAttachment(WarMetaData.ATTACHMENT_KEY);

        warMetaData.getMergedJBossWebMetaData().setSecurityDomain(this.getConfiguration().getSecurityDomain());
    }

    protected abstract void configureWebComponents(DeploymentUnit deploymentUnit);

    /**
     * <p> Subclasses should implement this method to configureDeployment a specific PicketLink Provider type. Eg.: Identity Provider or Service
     * Provider. </p>
     *
     * @param deploymentUnit
     */
    protected abstract void doConfigureDeployment(DeploymentUnit deploymentUnit);

    @SuppressWarnings("unchecked")
    @Override
    public T getValue() throws IllegalStateException, IllegalArgumentException {
        return (T) this;
    }

    private PicketLinkType createPicketLinkType(C configuration) {
        PicketLinkType picketLinkType = new PicketLinkType();

        picketLinkType.setStsType(createSTSType());
        picketLinkType.setHandlers(new Handlers());
        picketLinkType.setEnableAudit(true);

        picketLinkType.setIdpOrSP((ProviderType) configuration);

        return picketLinkType;
    }

    void addHandler(final Handler handler) {
        Handlers handlers = getPicketLinkType().getHandlers();

        for (Handler actualHandler : new ArrayList<Handler>(handlers.getHandler())) {
            if (actualHandler.getClazz().equals(handler.getClazz())) {
                throw PicketLinkLogger.ROOT_LOGGER.federationHandlerAlreadyDefined(handler.getClazz());
            }
        }

        handlers.add(handler);
    }

    void addHandler(Class<? extends SAML2Handler> handlerClassName, Handlers handlers) {
        for (Handler handler : handlers.getHandler()) {
            if (handler.getClazz().equals(handlerClassName.getName())) {
                return;
            }
        }

        Handler handler = new Handler();

        handler.setClazz(handlerClassName.getName());

        handlers.add(handler);
    }

    void removeHandler(final Handler handler) {
        getPicketLinkType().getHandlers().remove(handler);
    }

    @SuppressWarnings("unchecked")
    public C getConfiguration() {
        return (C) getPicketLinkType().getIdpOrSP();
    }

    private STSType createSTSType() {
        STSType stsType = null;

        InputStream stream = null;

        try {
            URL url = getClass().getClassLoader().getResource("core-sts.xml");

            if (url == null) {
                url = Thread.currentThread().getContextClassLoader().getResource("core-sts");
            }

            if (url != null) {
                stream = url.openStream();
                stsType = (STSType) new STSConfigParser().parse(stream);
            }
        } catch (Exception e) {
            throw PicketLinkLogger.ROOT_LOGGER.federationCouldNotParseSTSConfig(e);
        } finally {
            try {
                if (stream != null) {
                    stream.close();
                }
            } catch (IOException ignored) {
            }
        }

        return stsType;
    }

    public InjectedValue<FederationService> getFederationService() {
        return this.federationService;
    }

    PicketLinkType getPicketLinkType() {
        return this.picketLinkType;
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

QDox parse error in file://<WORKSPACE>/data/code-rep-dataset/Dataset3/Tasks/9819.java