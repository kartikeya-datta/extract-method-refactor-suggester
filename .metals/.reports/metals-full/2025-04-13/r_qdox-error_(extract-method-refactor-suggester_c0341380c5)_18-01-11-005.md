error id: file://<WORKSPACE>/data/code-rep-dataset/Dataset3/Tasks/13948.java
file://<WORKSPACE>/data/code-rep-dataset/Dataset3/Tasks/13948.java
### com.thoughtworks.qdox.parser.ParseException: syntax error @[1,1]

error in qdox parser
file content:
```java
offset: 1
uri: file://<WORKSPACE>/data/code-rep-dataset/Dataset3/Tasks/13948.java
text:
```scala
p@@arser = new JBossServiceXmlDescriptorParser(propertyReplacer);

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

package org.jboss.as.service;

import java.net.URL;
import org.jboss.as.service.descriptor.JBossServiceAttributeConfig;
import org.jboss.as.service.descriptor.JBossServiceConfig;
import org.jboss.as.service.descriptor.JBossServiceConstructorConfig;
import org.jboss.as.service.descriptor.JBossServiceDependencyConfig;
import org.jboss.as.service.descriptor.JBossServiceXmlDescriptor;
import org.jboss.as.service.descriptor.JBossServiceXmlDescriptorParser;
import org.jboss.as.model.ParseResult;
import org.jboss.staxmapper.XMLMapper;
import org.junit.Before;
import org.junit.Test;

import javax.xml.namespace.QName;
import javax.xml.stream.XMLInputFactory;
import javax.xml.stream.XMLStreamReader;
import java.io.File;
import java.io.FileInputStream;
import java.io.InputStream;
import java.util.List;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertNull;

/**
 * Test to verify the {@code JBossServiceXmlDescriptorParser} implementation.
 *
 * @author John E. Bailey
 */
public class JBossServiceXmlDescriptorParserTestCase {

    private JBossServiceXmlDescriptorParser parser;
    private XMLMapper xmlMapper;
    private XMLInputFactory inputFactory;

    @Before
    public void setupParser() throws Exception {
        parser = new JBossServiceXmlDescriptorParser();
        xmlMapper = XMLMapper.Factory.create();
        xmlMapper.registerRootElement(new QName(JBossServiceXmlDescriptorParser.NAMESPACE, "server"), parser);
        inputFactory = XMLInputFactory.newInstance();
    }

    @Test
    public void testParse() throws Exception {
        final File testXmlFile = getResourceFile(JBossServiceXmlDescriptorParserTestCase.class, "/test/serviceXmlDeployment.jar/META-INF/jboss-service.xml");
        final ParseResult<JBossServiceXmlDescriptor> jBossServiceXmlDescriptorParseResult = new ParseResult<JBossServiceXmlDescriptor>();
        InputStream inputStream = null;
        try {
            inputStream = new FileInputStream(testXmlFile);
            final XMLStreamReader reader = inputFactory.createXMLStreamReader(inputStream);
            xmlMapper.parseDocument(jBossServiceXmlDescriptorParseResult, reader);
        } finally {
            if (inputStream != null) inputStream.close();
        }
        final JBossServiceXmlDescriptor xmlDescriptor = jBossServiceXmlDescriptorParseResult.getResult();
        assertNotNull(xmlDescriptor);
        final List<JBossServiceConfig> serviceConfigs = xmlDescriptor.getServiceConfigs();
        assertEquals(3, serviceConfigs.size());

        for (JBossServiceConfig jBossServiceConfig : serviceConfigs) {
            assertEquals("org.jboss.as.service.LegacyService", jBossServiceConfig.getCode());
            if (jBossServiceConfig.getName().equals("jboss.test.service")) {
                final JBossServiceConstructorConfig constructorConfig = jBossServiceConfig.getConstructorConfig();
                assertNotNull(constructorConfig);
                final JBossServiceConstructorConfig.Argument[] arguments = constructorConfig.getArguments();
                assertEquals(1, arguments.length);
                assertEquals(String.class.getName(), arguments[0].getType());
                assertEquals("Test Value", arguments[0].getValue());
            } else if (jBossServiceConfig.getName().equals("jboss.test.service.second")) {
                assertNull(jBossServiceConfig.getConstructorConfig());
                final JBossServiceDependencyConfig[] dependencyConfigs = jBossServiceConfig.getDependencyConfigs();
                assertEquals(1, dependencyConfigs.length);
                assertEquals("jboss.test.service", dependencyConfigs[0].getDependencyName());
                assertEquals("other", dependencyConfigs[0].getOptionalAttributeName());
                final JBossServiceAttributeConfig[] attributeConfigs = jBossServiceConfig.getAttributeConfigs();
                assertEquals(1, attributeConfigs.length);
                assertEquals("somethingElse", attributeConfigs[0].getName());
                assertNull(attributeConfigs[0].getInject());
                final JBossServiceAttributeConfig.ValueFactory valueFactory = attributeConfigs[0].getValueFactory();
                assertNotNull(valueFactory);
                assertEquals("jboss.test.service", valueFactory.getBeanName());
                assertEquals("appendSomethingElse", valueFactory.getMethodName());
                final JBossServiceAttributeConfig.ValueFactoryParameter[] parameters = valueFactory.getParameters();
                assertEquals(1, parameters.length);
                assertEquals("java.lang.String", parameters[0].getType());
                assertEquals("more value", parameters[0].getValue());
            } else if (jBossServiceConfig.getName().equals("jboss.test.service.third")) {
                assertNull(jBossServiceConfig.getConstructorConfig());

                final JBossServiceAttributeConfig[] attributeConfigs = jBossServiceConfig.getAttributeConfigs();
                assertEquals(2, attributeConfigs.length);
                assertEquals("other", attributeConfigs[0].getName());
                assertNull(attributeConfigs[0].getValueFactory());

                final JBossServiceAttributeConfig.Inject inject = attributeConfigs[0].getInject();
                assertNotNull(inject);
                assertEquals("jboss.test.service.second", inject.getBeanName());
                assertEquals("other", inject.getPropertyName());

                assertEquals("somethingElse", attributeConfigs[1].getName());
                assertNull(attributeConfigs[1].getValueFactory());
                assertNull(attributeConfigs[1].getInject());
                assertEquals("Another test value", attributeConfigs[1].getValue());
            }
        }
    }

    protected URL getResource(final Class<?> testClass, final String path) throws Exception {
        return testClass.getResource(path);
    }

    protected File getResourceFile(final Class<?> testClass, final String path) throws Exception {
        return new File(getResource(testClass, path).toURI());
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

QDox parse error in file://<WORKSPACE>/data/code-rep-dataset/Dataset3/Tasks/13948.java