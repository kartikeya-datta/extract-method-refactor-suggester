error id: file://<WORKSPACE>/data/code-rep-dataset/Dataset3/Tasks/554.java
file://<WORKSPACE>/data/code-rep-dataset/Dataset3/Tasks/554.java
### com.thoughtworks.qdox.parser.ParseException: syntax error @[1,1]

error in qdox parser
file content:
```java
offset: 1
uri: file://<WORKSPACE>/data/code-rep-dataset/Dataset3/Tasks/554.java
text:
```scala
i@@f(nextSecurityDomain.isEmpty()) {

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
package org.jboss.as.webservices.tomcat;

import java.util.List;
import java.util.Set;

import org.jboss.as.ee.structure.Attachments;
import org.jboss.as.server.deployment.DeploymentUnit;
import org.jboss.as.webservices.logging.WSLogger;
import org.jboss.as.webservices.metadata.model.EJBEndpoint;
import org.jboss.metadata.ear.jboss.JBossAppMetaData;
import org.jboss.metadata.ear.spec.EarMetaData;
import org.jboss.metadata.javaee.spec.SecurityRoleMetaData;
import org.jboss.metadata.javaee.spec.SecurityRolesMetaData;
import org.jboss.ws.common.integration.WSHelper;
import org.jboss.wsf.spi.deployment.Deployment;
import org.jboss.wsf.spi.deployment.Endpoint;
import org.jboss.wsf.spi.metadata.j2ee.EJBArchiveMetaData;
import org.jboss.wsf.spi.metadata.j2ee.EJBMetaData;
import org.jboss.wsf.spi.metadata.j2ee.EJBSecurityMetaData;

/**
 * Creates web app security meta data for EJB deployment.
 *
 * @author <a href="mailto:ropalka@redhat.com">Richard Opalka</a>
 * @author <a href="mailto:tdiesler@redhat.com">Thomas Diesler</a>
 */
abstract class AbstractSecurityMetaDataAccessorEJB implements SecurityMetaDataAccessorEJB {

    /**
     * @see org.jboss.webservices.integration.tomcat.AbstractSecurityMetaDataAccessorEJB#getSecurityDomain(Deployment)
     *
     * @param dep webservice deployment
     * @return security domain associated with EJB 3 deployment
     */
    public String getSecurityDomain(final Deployment dep) {
        String securityDomain = null;

        for (final EJBEndpoint ejbEndpoint : getEjbEndpoints(dep)) {
            String nextSecurityDomain = ejbEndpoint.getSecurityDomain();
            if (nextSecurityDomain == null || nextSecurityDomain.isEmpty()) {
                nextSecurityDomain = null;
            }
            securityDomain = getDomain(securityDomain, nextSecurityDomain);
        }

        if (securityDomain == null) {
            final DeploymentUnit unit = WSHelper.getRequiredAttachment(dep, DeploymentUnit.class);
            if (unit.getParent() != null) {
                final EarMetaData jbossAppMD = unit.getParent().getAttachment(Attachments.EAR_METADATA);
                return jbossAppMD instanceof JBossAppMetaData ? ((JBossAppMetaData)jbossAppMD).getSecurityDomain() : null;
            }
        }

        return securityDomain;
    }

    public SecurityRolesMetaData getSecurityRoles(final Deployment dep) {
        final SecurityRolesMetaData securityRolesMD = new SecurityRolesMetaData();

        Set<String> firstEndpointDeclaredSecurityRoles = null;
        for (final EJBEndpoint ejbEndpoint : getEjbEndpoints(dep)) {
            final Set<String> declaredSecurityRoles = ejbEndpoint.getDeclaredSecurityRoles();
            if (firstEndpointDeclaredSecurityRoles == null) {
                firstEndpointDeclaredSecurityRoles = declaredSecurityRoles;
            } else if (!firstEndpointDeclaredSecurityRoles.equals(declaredSecurityRoles)) {
                WSLogger.ROOT_LOGGER.multipleEndpointsWithDifferentDeclaredSecurityRoles();
            }
            //union of declared security roles from all endpoints...
            for (final String roleName : declaredSecurityRoles) {
                final SecurityRoleMetaData securityRoleMD = new SecurityRoleMetaData();
                securityRoleMD.setRoleName(roleName);
                securityRolesMD.add(securityRoleMD);
            }
        }

        return securityRolesMD;
    }

    protected abstract List<EJBEndpoint> getEjbEndpoints(final Deployment dep);

    /**
     * @see org.jboss.webservices.integration.tomcat.SecurityMetaDataAccessorEJB#getAuthMethod(Endpoint)
     *
     * @param endpoint EJB webservice endpoint
     * @return authentication method or null if not specified
     */
    public String getAuthMethod(final Endpoint endpoint) {
        final EJBSecurityMetaData ejbSecurityMD = this.getEjbSecurityMetaData(endpoint);
        final boolean hasEjbSecurityMD = ejbSecurityMD != null;

        return hasEjbSecurityMD ? ejbSecurityMD.getAuthMethod() : null;
    }

    /**
     * @see org.jboss.webservices.integration.tomcat.SecurityMetaDataAccessorEJB#isSecureWsdlAccess(Endpoint)
     *
     * @param endpoint EJB webservice endpoint
     * @return whether WSDL access have to be secured
     */
    public boolean isSecureWsdlAccess(final Endpoint endpoint) {
        final EJBSecurityMetaData ejbSecurityMD = this.getEjbSecurityMetaData(endpoint);
        final boolean hasEjbSecurityMD = ejbSecurityMD != null;

        return hasEjbSecurityMD ? ejbSecurityMD.getSecureWSDLAccess() : false;
    }

    /**
     * @see org.jboss.webservices.integration.tomcat.SecurityMetaDataAccessorEJB#getTransportGuarantee(Endpoint)
     *
     * @param endpoint EJB webservice endpoint
     * @return transport guarantee or null if not specified
     */
    public String getTransportGuarantee(final Endpoint endpoint) {
        final EJBSecurityMetaData ejbSecurityMD = this.getEjbSecurityMetaData(endpoint);
        final boolean hasEjbSecurityMD = ejbSecurityMD != null;

        return hasEjbSecurityMD ? ejbSecurityMD.getTransportGuarantee() : null;
    }

    /**
     * Gets EJB security meta data if associated with EJB endpoint.
     *
     * @param endpoint EJB webservice endpoint
     * @return EJB security meta data or null
     */
    private EJBSecurityMetaData getEjbSecurityMetaData(final Endpoint endpoint) {
        final String ejbName = endpoint.getShortName();
        final Deployment dep = endpoint.getService().getDeployment();
        final EJBArchiveMetaData ejbArchiveMD = WSHelper.getOptionalAttachment(dep, EJBArchiveMetaData.class);
        final EJBMetaData ejbMD = ejbArchiveMD != null ? ejbArchiveMD.getBeanByEjbName(ejbName) : null;

        return ejbMD != null ? ejbMD.getSecurityMetaData() : null;
    }

    /**
     * Returns security domain value. This method checks domain is the same for every EJB 3 endpoint.
     *
     * @param oldSecurityDomain our security domain
     * @param nextSecurityDomain next security domain
     * @return security domain value
     * @throws IllegalStateException if domains have different values
     */
    private String getDomain(final String oldSecurityDomain, final String nextSecurityDomain) {
        if (nextSecurityDomain == null) {
            return oldSecurityDomain;
        }

        if (oldSecurityDomain == null) {
            return nextSecurityDomain;
        }

        ensureSameDomains(oldSecurityDomain, nextSecurityDomain);

        return oldSecurityDomain;
    }

    /**
     * This method ensures both passed domains contain the same value.
     *
     * @param oldSecurityDomain our security domain
     * @param newSecurityDomain next security domain
     * @throws IllegalStateException if domains have different values
     */
    private void ensureSameDomains(final String oldSecurityDomain, final String newSecurityDomain) {
        final boolean domainsDiffer = !oldSecurityDomain.equals(newSecurityDomain);
        if (domainsDiffer)
            throw WSLogger.ROOT_LOGGER.multipleSecurityDomainsDetected(oldSecurityDomain, newSecurityDomain);
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

QDox parse error in file://<WORKSPACE>/data/code-rep-dataset/Dataset3/Tasks/554.java