error id: file://<WORKSPACE>/data/code-rep-dataset/Dataset3/Tasks/6275.java
file://<WORKSPACE>/data/code-rep-dataset/Dataset3/Tasks/6275.java
### com.thoughtworks.qdox.parser.ParseException: syntax error @[1,1]

error in qdox parser
file content:
```java
offset: 1
uri: file://<WORKSPACE>/data/code-rep-dataset/Dataset3/Tasks/6275.java
text:
```scala
r@@eturn ModelParsingSupport.getXmlContent("standalone", getTargetNamespace(), getTargetNamespaceLocation(), content);

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

package org.jboss.as.model.base;

import java.util.Map;
import java.util.Set;

import javax.xml.stream.XMLStreamException;

import org.jboss.as.model.AbstractSubsystemElement;
import org.jboss.as.model.ProfileElement;
import org.jboss.as.model.PropertiesElement;
import org.jboss.as.model.ServerGroupDeploymentElement;
import org.jboss.as.model.ServerModel;
import org.jboss.as.model.UpdateFailedException;
import org.jboss.as.model.base.util.MockSubsystemElement;
import org.jboss.as.model.base.util.ModelParsingSupport;

/**
 * @author Emanuel Muckenhuber
 */
public abstract class ServerModelTestBase extends DomainModelElementTestBase {

    public ServerModelTestBase(String name) {
        super(name);
    }

    public void testName() throws Exception  {
        final String content = "<name>default</name>";
        final ServerModel model = parse(content);
        assertEquals("default", model.getServerName());
    }

    public void testProfile() throws Exception {
        final String content = "<profile name=\"default\">" + MockSubsystemElement.getFullXmlContent() + "</profile>";
        final ServerModel model = parse(content);
        final ProfileElement profile = model.getProfile();
        assertEquals("default", profile.getName());
        Set<AbstractSubsystemElement<? extends AbstractSubsystemElement<?>>> subsystems = profile.getSubsystems();
        assertEquals(2, subsystems.size());
    }

    public void testSystemProperties() throws Exception  {
        final String content = "<name>default</name><system-properties><property name=\"prop1\" value=\"value1\"/><property name=\"prop2\" value=\"value2\"/></system-properties>";
        final ServerModel model = parse(content);
        assertEquals("default", model.getServerName());
        final PropertiesElement properties = model.getSystemProperties();
        Map<String, String> props = properties.getProperties();
        assertEquals(2, props.size());
        assertEquals("value1", props.get("prop1"));
        assertEquals("value1", properties.getProperty("prop1"));
        assertEquals("value2", props.get("prop2"));
        assertEquals("value2", properties.getProperty("prop2"));
        Set<String> names = properties.getPropertyNames();
        assertEquals(2, names.size());
        assertTrue(names.contains("prop1"));
        assertTrue(names.contains("prop2"));
    }

    public void testDeployments() throws Exception {
        final String content = ModelParsingSupport.wrap("deployments", "<deployment name=\"my-war.ear_v1\" runtime-name=\"my-war.ear\" sha1=\"550cc80cf885bc0f11ce45e23fecef2d7d7dd1c6\" start=\"false\"/>");
        final ServerModel model = parse(content);
        ServerGroupDeploymentElement deployment = model.getDeployment("my-war.ear_v1");
        assertNotNull(deployment);
    }

    /** {@inheritDoc} */
    public void testSerializationDeserialization() throws Exception {
        // TODO Auto-generated method stub

    }

    ServerModel parse(final String content) throws XMLStreamException, UpdateFailedException {
        return ModelParsingSupport.parseServerModel(getXMLMapper(), getFullContent(content));
    }

    String getFullContent(final String content) {
        return ModelParsingSupport.getXmlContent("server", getTargetNamespace(), getTargetNamespaceLocation(), content);
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

QDox parse error in file://<WORKSPACE>/data/code-rep-dataset/Dataset3/Tasks/6275.java