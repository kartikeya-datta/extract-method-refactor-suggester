error id: file://<WORKSPACE>/data/code-rep-dataset/Dataset3/Tasks/4552.java
file://<WORKSPACE>/data/code-rep-dataset/Dataset3/Tasks/4552.java
### com.thoughtworks.qdox.parser.ParseException: syntax error @[1,7]

error in qdox parser
file content:
```java
offset: 7
uri: file://<WORKSPACE>/data/code-rep-dataset/Dataset3/Tasks/4552.java
text:
```scala
final F@@ile subsystemFile = new File(baseDir, subsystem.getSubsystem());

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
package org.jboss.as.config.assembly;

import java.io.BufferedWriter;
import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.util.HashMap;
import java.util.Iterator;
import java.util.Map;
import java.util.Set;
import java.util.TreeMap;
import java.util.TreeSet;

import javax.xml.stream.XMLOutputFactory;
import javax.xml.stream.XMLStreamException;

/**
 *
 * @author <a href="kabir.khan@jboss.com">Kabir Khan</a>
 */
class ConfigurationAssembler {

    private final File baseDir;
    private final File templateFile;
    private final String templateRootElementName;
    private final File subsystemsFile;
    private final File outputFile;

    public ConfigurationAssembler(File baseDir, File templateFile, String templateRootElementName, File subsystemsFile, File outputFile) {
        this.baseDir = baseDir.getAbsoluteFile();
        this.templateFile = templateFile.getAbsoluteFile();
        this.templateRootElementName = templateRootElementName;
        this.subsystemsFile = subsystemsFile.getAbsoluteFile();
        this.outputFile = outputFile.getAbsoluteFile();
    }

    public void assemble() throws IOException, XMLStreamException {
        TemplateParser templateParser = new TemplateParser(templateFile, templateRootElementName);
        templateParser.parse();
        SubsystemsParser subsystemsParser = new SubsystemsParser(subsystemsFile);
        subsystemsParser.parse();

        populateTemplate(templateParser, subsystemsParser);

        if (outputFile.exists()) {
            outputFile.delete();
        }
        if (!outputFile.getParentFile().exists()) {
            if (!outputFile.getParentFile().mkdirs()) {
                throw new IllegalStateException("Could not create " + outputFile.getParentFile());
            }
        }
        FormattingXMLStreamWriter writer = new FormattingXMLStreamWriter(XMLOutputFactory.newInstance().createXMLStreamWriter(new BufferedWriter(new FileWriter(outputFile))));
        try {
            writer.writeStartDocument();
            templateParser.getRootNode().marshall(writer);
            writer.writeEndDocument();
        } finally {
            try {
                writer.close();
            } catch (Exception ignore) {
            }
        }
    }

    private void populateTemplate(TemplateParser templateParser, SubsystemsParser subsystemsParser) throws IOException, XMLStreamException{
        final Set<String> extensions = new TreeSet<String>();
        final Map<String, Map<String, ElementNode>> socketBindingsByGroup = new HashMap<String, Map<String, ElementNode>>();
        final Map<String, Map<String, ElementNode>> outboundSocketBindingsByGroup = new HashMap<String, Map<String, ElementNode>>();
        for (Map.Entry<String, ProcessingInstructionNode> subsystemEntry : templateParser.getSubsystemPlaceholders().entrySet()) {
            final String subsystemName = subsystemEntry.getKey();
            final String groupName = subsystemEntry.getValue().getDataValue("socket-binding-group", "");

            final SubsystemConfig[] subsystems = subsystemsParser.getSubsystemConfigs().get(subsystemName);
            if (subsystems == null) {
                throw new IllegalStateException("Could not find a subsystems configuration called '" + subsystemEntry.getKey() + "' in " + subsystemsFile);
            }
            final Map<String, ElementNode> socketBindings = new TreeMap<String, ElementNode>();
            if (socketBindingsByGroup.put(groupName, socketBindings) != null) {
                throw new IllegalStateException("Group '" + groupName + "' already exists");
            }
            final Map<String, ElementNode> outboundSocketBindings = new TreeMap<String, ElementNode>();
            outboundSocketBindingsByGroup.put(groupName, outboundSocketBindings);

            for (SubsystemConfig subsystem : subsystems) {
                final File subsystemFile = new File(baseDir, subsystem.getSubsystemFile());
                if (!subsystemFile.exists()) {
                    throw new IllegalStateException("Could not find '" + subsystem + "' under the base directory '" + baseDir + "'");
                }

                final SubsystemParser subsystemParser = new SubsystemParser(templateParser.getRootNode().getNamespace(), subsystem.getSupplement(), subsystemFile);
                subsystemParser.parse();
                subsystemEntry.getValue().addDelegate(subsystemParser.getSubsystem());
                extensions.add(subsystemParser.getExtensionModule());
                for (Map.Entry<String, ElementNode> entry : subsystemParser.getSocketBindings().entrySet()) {
                    if (socketBindings.containsKey(entry.getKey())) {
                        throw new IllegalStateException("SocketBinding '" + entry + "' already exists");
                    }
                    socketBindings.put(entry.getKey(), entry.getValue());
                }
                for (Map.Entry<String, ElementNode> entry : subsystemParser.getOutboundSocketBindings().entrySet()) {
                    if (outboundSocketBindings.containsKey(entry.getKey())) {
                        throw new IllegalStateException("Outbound SocketBinding '" + entry + "' already exists");
                    }
                    outboundSocketBindings.put(entry.getKey(), entry.getValue());
                }
            }
        }

        if (extensions.size() > 0) {
            final ProcessingInstructionNode extensionNode = templateParser.getExtensionPlaceHolder();
            for (String extension : extensions) {
                final ElementNode extensionElement = new ElementNode(null, "extension", templateParser.getRootNode().getNamespace());
                extensionElement.addAttribute("module", new AttributeValue(extension));
                extensionNode.addDelegate(extensionElement);
            }
        } else {
            //Delete the extensions element if there are no extensions
            for (Iterator<Node> it = templateParser.getRootNode().iterateChildren() ; it.hasNext() ; ) {
                Node node = it.next();
                if (node instanceof ElementNode) {
                    ElementNode elementNode = (ElementNode)node;
                    if (elementNode.getName().equals("extensions") || elementNode.getName().equals("profile")) {
                        it.remove();
                    }
                }
            }
        }
        if (socketBindingsByGroup.size() > 0 && outboundSocketBindingsByGroup.size() > 0) {
            for (Map.Entry<String, ProcessingInstructionNode> entry : templateParser.getSocketBindingsPlaceHolders().entrySet()) {
                Map<String, ElementNode> socketBindings = socketBindingsByGroup.get(entry.getKey());
                if (socketBindings == null) {
                    throw new IllegalArgumentException("No socket bindings for group " + entry.getKey());
                }
                if (socketBindings.size() > 0) {
                    for (ElementNode binding : socketBindings.values()) {
                        entry.getValue().addDelegate(binding);
                    }
                }
            }
            for (Map.Entry<String, ProcessingInstructionNode> entry : templateParser.getSocketBindingsPlaceHolders().entrySet()) {
                Map<String, ElementNode> outboundSocketBindings = outboundSocketBindingsByGroup.get(entry.getKey());
                if (outboundSocketBindings == null) {
                    throw new IllegalArgumentException("No outbound socket bindings for group " + entry.getKey());
                }
                if (outboundSocketBindings.size() > 0) {
                    for (ElementNode binding : outboundSocketBindings.values()) {
                        entry.getValue().addDelegate(binding);
                    }
                }
            }
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

QDox parse error in file://<WORKSPACE>/data/code-rep-dataset/Dataset3/Tasks/4552.java