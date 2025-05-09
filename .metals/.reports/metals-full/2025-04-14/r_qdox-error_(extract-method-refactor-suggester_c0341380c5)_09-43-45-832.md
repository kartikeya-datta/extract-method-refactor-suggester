error id: file://<WORKSPACE>/data/code-rep-dataset/Dataset3/Tasks/11230.java
file://<WORKSPACE>/data/code-rep-dataset/Dataset3/Tasks/11230.java
### com.thoughtworks.qdox.parser.ParseException: syntax error @[1,15]

error in qdox parser
file content:
```java
offset: 15
uri: file://<WORKSPACE>/data/code-rep-dataset/Dataset3/Tasks/11230.java
text:
```scala
public static T@@estParser create(XMLMapper xmlMapper, TestModelType type) {

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
package org.jboss.as.core.model.test;

import java.util.List;
import java.util.concurrent.Executors;

import javax.xml.namespace.QName;
import javax.xml.stream.XMLStreamException;

import org.jboss.as.controller.descriptions.ModelDescriptionConstants;
import org.jboss.as.controller.parsing.Namespace;
import org.jboss.as.controller.persistence.ModelMarshallingContext;
import org.jboss.as.controller.persistence.SubsystemMarshallingContext;
import org.jboss.as.host.controller.parsing.DomainXml;
import org.jboss.as.host.controller.parsing.HostXml;
import org.jboss.as.model.test.ModelTestParser;
import org.jboss.as.server.parsing.StandaloneXml;
import org.jboss.dmr.ModelNode;
import org.jboss.staxmapper.XMLElementReader;
import org.jboss.staxmapper.XMLElementWriter;
import org.jboss.staxmapper.XMLExtendedStreamReader;
import org.jboss.staxmapper.XMLExtendedStreamWriter;
import org.jboss.staxmapper.XMLMapper;

/**
 *
 * @author <a href="kabir.khan@jboss.com">Kabir Khan</a>
 */
public class TestParser implements ModelTestParser {
    private final TestModelType type;
    private final XMLElementReader<List<ModelNode>> reader;
    private final XMLElementWriter<ModelMarshallingContext> writer;
    private volatile ModelWriteSanitizer writeSanitizer;

    public TestParser(TestModelType type, XMLElementReader<List<ModelNode>> reader, XMLElementWriter<ModelMarshallingContext> writer) {
        this.type = type;
        this.reader = reader;
        this.writer = writer;
    }

    static TestParser create(XMLMapper xmlMapper, TestModelType type) {
        TestParser testParser;
        String root;
        if (type == TestModelType.STANDALONE) {
            StandaloneXml standaloneXml = new StandaloneXml(null, Executors.newCachedThreadPool(), null);
            testParser = new TestParser(type, standaloneXml, standaloneXml);
            root = "server";
        } else if (type == TestModelType.DOMAIN) {
            DomainXml domainXml = new DomainXml(null, Executors.newCachedThreadPool(), null);
            testParser = new TestParser(type, domainXml, domainXml);
            root = "domain";
        } else if (type == TestModelType.HOST) {
            HostXml hostXml = new HostXml("master");
            testParser = new TestParser(type, hostXml, hostXml);
            root = "host";
        } else {
            throw new IllegalArgumentException("Unknown type " + type);
        }


        for (Namespace ns : Namespace.ALL_NAMESPACES) {
            xmlMapper.registerRootElement(new QName(ns.getUriString(), root), testParser);
        }
        return testParser;
    }

    void setModelWriteSanitizer(ModelWriteSanitizer writeSanitizer) {
        this.writeSanitizer = writeSanitizer;
    }

    @Override
    public void readElement(XMLExtendedStreamReader reader, List<ModelNode> value) throws XMLStreamException {
        this.reader.readElement(reader, value);
    }

    @Override
    public void writeContent(XMLExtendedStreamWriter streamWriter, ModelMarshallingContext context) throws XMLStreamException {
        this.writer.writeContent(streamWriter, sanitizeContext(wrapPossibleHost(context)));
    }

    private ModelMarshallingContext wrapPossibleHost(final ModelMarshallingContext context) {

        if (type == TestModelType.HOST) {
            return new ModelMarshallingContext() {

                @Override
                public XMLElementWriter<SubsystemMarshallingContext> getSubsystemWriter(String subsystemName) {
                    return context.getSubsystemWriter(subsystemName);
                }

                @Override
                public XMLElementWriter<SubsystemMarshallingContext> getSubsystemDeploymentWriter(String subsystemName) {
                    return context.getSubsystemDeploymentWriter(subsystemName);
                }

                @Override
                public ModelNode getModelNode() {
                    return context.getModelNode().get(ModelDescriptionConstants.HOST, "master");
                }
            };
        }

        return context;
    }
    private ModelMarshallingContext sanitizeContext(final ModelMarshallingContext context) {
        if (writeSanitizer == null) {
            return context;
        }
        final ModelNode model = writeSanitizer.sanitize(context.getModelNode());
        return new ModelMarshallingContext() {

            @Override
            public XMLElementWriter<SubsystemMarshallingContext> getSubsystemWriter(String subsystemName) {
                return context.getSubsystemWriter(subsystemName);
            }

            @Override
            public XMLElementWriter<SubsystemMarshallingContext> getSubsystemDeploymentWriter(String subsystemName) {
                return context.getSubsystemDeploymentWriter(subsystemName);
            }

            @Override
            public ModelNode getModelNode() {
                return model;
            }
        };
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

QDox parse error in file://<WORKSPACE>/data/code-rep-dataset/Dataset3/Tasks/11230.java