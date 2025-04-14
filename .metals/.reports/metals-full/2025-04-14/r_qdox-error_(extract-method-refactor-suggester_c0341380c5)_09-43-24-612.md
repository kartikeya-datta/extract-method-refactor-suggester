error id: file://<WORKSPACE>/data/code-rep-dataset/Dataset3/Tasks/11030.java
file://<WORKSPACE>/data/code-rep-dataset/Dataset3/Tasks/11030.java
### com.thoughtworks.qdox.parser.ParseException: syntax error @[1,1]

error in qdox parser
file content:
```java
offset: 1
uri: file://<WORKSPACE>/data/code-rep-dataset/Dataset3/Tasks/11030.java
text:
```scala
t@@hrow new IllegalStateException("<?" + TemplateParser.EXTENSIONS_PI + "?> should not take any data");

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

import static javax.xml.stream.XMLStreamConstants.START_DOCUMENT;

import java.io.BufferedInputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.util.HashMap;
import java.util.Map;

import javax.xml.stream.XMLInputFactory;
import javax.xml.stream.XMLStreamException;
import javax.xml.stream.XMLStreamReader;

/**
 *
 * @author <a href="kabir.khan@jboss.com">Kabir Khan</a>
 */
public class TemplateParser extends NodeParser {

    private static final String SOCKET_BINDINGS_PI = "SOCKET-BINDINGS";
    private static final String EXTENSIONS_PI = "EXTENSIONS";
    private static final String SUBSYSTEMS_PI = "SUBSYSTEMS";

    private final File inputFile;
    private final String rootElementName;
    private ElementNode root;
    private ProcessingInstructionNode extensionPlaceholder;
    private final Map<String, ProcessingInstructionNode> subsystemPlaceHolders = new HashMap<String, ProcessingInstructionNode>();
    private final Map<String, ProcessingInstructionNode> socketBindingsPlaceHolder = new HashMap<String, ProcessingInstructionNode>();

    public TemplateParser(File inputFile, String rootElementName) {
        this.inputFile = inputFile;
        this.rootElementName = rootElementName;

    }

    ElementNode getRootNode() {
        return root;
    }

    ProcessingInstructionNode getExtensionPlaceHolder() {
        return extensionPlaceholder;
    }

    Map<String, ProcessingInstructionNode> getSubsystemPlaceholders(){
        return subsystemPlaceHolders;
    }

    Map<String, ProcessingInstructionNode> getSocketBindingsPlaceHolders() {
        return socketBindingsPlaceHolder;
    }

    void parse() throws IOException, XMLStreamException {
        InputStream in = new BufferedInputStream(new FileInputStream(inputFile));
        try {
            XMLInputFactory factory = XMLInputFactory.newInstance();
            factory.setProperty(XMLInputFactory.IS_COALESCING, Boolean.FALSE);
            XMLStreamReader reader = factory.createXMLStreamReader(in);

            reader.require(START_DOCUMENT, null, null);
            ParsingUtils.getNextElement(reader, rootElementName, null, false);
            root = super.parseNode(reader, rootElementName);

        } finally {
            try {
                in.close();
            } catch (Exception ignore) {
            }
        }
    }

    @Override
    protected ProcessingInstructionNode parseProcessingInstruction(XMLStreamReader reader, ElementNode parent) throws XMLStreamException {
        ProcessingInstructionNode node = null;
        String pi = reader.getPITarget();
        Map<String, String> data = parseProcessingInstructionData(reader.getPIData());
        if (pi.equals(TemplateParser.EXTENSIONS_PI)) {
            if (!data.isEmpty()) {
                throw new IllegalStateException("<?" + TemplateParser.SUBSYSTEMS_PI + "?> should not take any data");
            }
            if (extensionPlaceholder != null) {
                throw new IllegalStateException("Can only have one occurance of <?" + TemplateParser.EXTENSIONS_PI + "?>");
            }
            node = new ProcessingInstructionNode(TemplateParser.EXTENSIONS_PI, null);
            extensionPlaceholder = node;
        } else if (pi.equals(TemplateParser.SUBSYSTEMS_PI)) {
            if (!parent.getName().equals("profile")) {
                throw new IllegalStateException("<?" + TemplateParser.SUBSYSTEMS_PI + "?> must be a child of <profile> " + reader.getLocation());
            }
            if (data.size() == 0 || !data.containsKey("socket-binding-group")) {
                throw new IllegalStateException("Must have 'socket-binding-group' as <?" + TemplateParser.SUBSYSTEMS_PI + "?> data");
            }
            if (data.size() > 1) {
                throw new IllegalStateException("Only 'socket-binding-group' is valid <?" + TemplateParser.SUBSYSTEMS_PI + "?> data");
            }
            String profileName = parent.getAttributeValue("name", "");
            node = new ProcessingInstructionNode(profileName, data);
            subsystemPlaceHolders.put(profileName, node);
        } else if (pi.equals(TemplateParser.SOCKET_BINDINGS_PI)) {
            if (!parent.getName().equals("socket-binding-group")) {
                throw new IllegalStateException("<?" + TemplateParser.SOCKET_BINDINGS_PI + "?> must be a child of <socket-binding-group> " + reader.getLocation());
            }
            if (!data.isEmpty()) {
                throw new IllegalStateException("<?" + TemplateParser.SOCKET_BINDINGS_PI + "?> should not take any data");
            }

            String groupName = parent.getAttributeValue("name", "");
            node = new ProcessingInstructionNode(TemplateParser.SOCKET_BINDINGS_PI, data);
            socketBindingsPlaceHolder.put(groupName, node);
        } else {
            throw new IllegalStateException("Unknown processing instruction <?" + reader.getPITarget() + "?>" + reader.getLocation());
        }        return node;
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

QDox parse error in file://<WORKSPACE>/data/code-rep-dataset/Dataset3/Tasks/11030.java