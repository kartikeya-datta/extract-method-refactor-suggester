error id: file://<WORKSPACE>/data/code-rep-dataset/Dataset3/Tasks/11148.java
file://<WORKSPACE>/data/code-rep-dataset/Dataset3/Tasks/11148.java
### com.thoughtworks.qdox.parser.ParseException: syntax error @[1,1]

error in qdox parser
file content:
```java
offset: 1
uri: file://<WORKSPACE>/data/code-rep-dataset/Dataset3/Tasks/11148.java
text:
```scala
a@@ddAsWebInfResource(ServiceIface.class.getPackage(), "wsdl/SecurityService-sign-encrypt.wsdl", "wsdl/SecurityService.wsdl").

/*
 * JBoss, Home of Professional Open Source.
 * Copyright 2012, Red Hat Middleware LLC, and individual contributors
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
package org.jboss.as.test.integration.ws.wsse.signencrypt;

import java.net.MalformedURLException;
import java.net.URL;
import javax.xml.namespace.QName;
import javax.xml.ws.BindingProvider;
import javax.xml.ws.Service;
import javax.xml.ws.soap.SOAPFaultException;
import junit.framework.Assert;
import org.apache.cxf.ws.security.SecurityConstants;
import org.jboss.arquillian.container.test.api.Deployment;
import org.jboss.arquillian.container.test.api.RunAsClient;
import org.jboss.arquillian.junit.Arquillian;
import org.jboss.arquillian.test.api.ArquillianResource;
import org.jboss.logging.Logger;
import org.jboss.shrinkwrap.api.Archive;
import org.jboss.shrinkwrap.api.ShrinkWrap;
import org.jboss.shrinkwrap.api.asset.StringAsset;
import org.jboss.shrinkwrap.api.spec.WebArchive;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.jboss.as.test.integration.ws.wsse.KeystorePasswordCallback;
import org.jboss.as.test.integration.ws.wsse.ServiceIface;
import org.jboss.as.test.integration.ws.wsse.ServiceImpl;

/**
 * Test WS sign + encrypt capability for multiple clients (alice and john)
 * 
 * Certificates can ge generated using keytool -genkey -keyalg RSA -storetype JKS
 * Public key can be extracted using keytool -export
 * Public key can be imported using keytool -import
 * Keystore can be listed using keytool -list -v
 * 
 * @author Rostislav Svoboda
 */
@RunWith(Arquillian.class)
@RunAsClient
public class SignEncryptMultipleClientsTestCase {

    private static Logger log = Logger.getLogger(SignEncryptMultipleClientsTestCase.class.getName());
    @ArquillianResource
    URL baseUrl;

    @Deployment
    public static Archive<?> deployment() {

        WebArchive war = ShrinkWrap.create(WebArchive.class, "jaxws-wsse-sign-encrypt.war").
                addAsManifestResource(new StringAsset("Dependencies: org.apache.ws.security\n"), "MANIFEST.MF").
                addClasses(ServiceIface.class, ServiceImpl.class, KeystorePasswordCallback.class).
                addAsResource(ServiceIface.class.getPackage(), "bob.jks", "bob.jks").
                addAsResource(ServiceIface.class.getPackage(), "bob.properties", "bob.properties").
                addAsWebInfResource(ServiceIface.class.getPackage(), "wsdl/SecurityService.wsdl", "wsdl/SecurityService.wsdl").
                addAsWebInfResource(ServiceIface.class.getPackage(), "wsdl/SecurityService_schema1.xsd", "wsdl/SecurityService_schema1.xsd").
                addAsWebInfResource(SignEncryptMultipleClientsTestCase.class.getPackage(), "multiple-clients-jaxws-endpoint-config.xml", "jaxws-endpoint-config.xml");

        log.info(war.toString(true));

        return war;
    }

    @Test
    public void encryptedAndSignedRequestFromAlice() throws Exception {
        QName serviceName = new QName("http://www.jboss.org/jbossws/ws-extensions/wssecuritypolicy", "SecurityService");
        URL wsdlURL = new URL(baseUrl.toString() + "SecurityService?wsdl");

        Service service = Service.create(wsdlURL, serviceName);
        ServiceIface proxy = (ServiceIface) service.getPort(ServiceIface.class);
        setupWsse(proxy, "alice");

        Assert.assertEquals("Secure Hello World!", proxy.sayHello());
    }

    @Test
    public void encryptedAndSignedRequestFromJohn() throws Exception {
        QName serviceName = new QName("http://www.jboss.org/jbossws/ws-extensions/wssecuritypolicy", "SecurityService");
        URL wsdlURL = new URL(baseUrl.toString() + "SecurityService?wsdl");

        Service service = Service.create(wsdlURL, serviceName);
        ServiceIface proxy = (ServiceIface) service.getPort(ServiceIface.class);
        setupWsse(proxy, "john");

        Assert.assertEquals("Secure Hello World!", proxy.sayHello());
    }

    /*
     * Max's public key is not trusted in Bob's keystore
     * Max's keystore contain's Bob's public key as trusted.
     */
    @Test
    public void encryptedAndSignedRequestFromUntrustedMax() throws Exception {
        QName serviceName = new QName("http://www.jboss.org/jbossws/ws-extensions/wssecuritypolicy", "SecurityService");
        URL wsdlURL = new URL(baseUrl.toString() + "SecurityService?wsdl");

        Service service = Service.create(wsdlURL, serviceName);
        ServiceIface proxy = (ServiceIface) service.getPort(ServiceIface.class);
        setupWsse(proxy, "max");

        try {
            proxy.sayHello();
            Assert.fail("Max shouldn't invoke this service");
        } catch (SOAPFaultException ex) {
            // expected failure because max isn't trusted
        }
    }

    private void setupWsse(ServiceIface proxy, String clientId) throws MalformedURLException {
        ((BindingProvider) proxy).getRequestContext().put(SecurityConstants.CALLBACK_HANDLER,      new KeystorePasswordCallback());
        ((BindingProvider) proxy).getRequestContext().put(SecurityConstants.SIGNATURE_PROPERTIES,  "org/jboss/as/test/integration/ws/wsse/" + clientId + ".properties");
        ((BindingProvider) proxy).getRequestContext().put(SecurityConstants.ENCRYPT_PROPERTIES,    "org/jboss/as/test/integration/ws/wsse/" + clientId + ".properties");
        ((BindingProvider) proxy).getRequestContext().put(SecurityConstants.SIGNATURE_USERNAME,    clientId);
        ((BindingProvider) proxy).getRequestContext().put(SecurityConstants.ENCRYPT_USERNAME,      "bob");
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

QDox parse error in file://<WORKSPACE>/data/code-rep-dataset/Dataset3/Tasks/11148.java