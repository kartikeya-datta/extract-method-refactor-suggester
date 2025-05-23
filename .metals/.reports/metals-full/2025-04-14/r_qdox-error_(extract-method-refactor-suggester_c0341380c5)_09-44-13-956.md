error id: file://<WORKSPACE>/data/code-rep-dataset/Dataset5/Tasks/5693.java
file://<WORKSPACE>/data/code-rep-dataset/Dataset5/Tasks/5693.java
### com.thoughtworks.qdox.parser.ParseException: syntax error @[1,15]

error in qdox parser
file content:
```java
offset: 15
uri: file://<WORKSPACE>/data/code-rep-dataset/Dataset5/Tasks/5693.java
text:
```scala
public static N@@ode importNode(Node parent, Node child) {

/*
 * Copyright  2001-2002,2004 The Apache Software Foundation
 *
 *  Licensed under the Apache License, Version 2.0 (the "License");
 *  you may not use this file except in compliance with the License.
 *  You may obtain a copy of the License at
 *
 *      http://www.apache.org/licenses/LICENSE-2.0
 *
 *  Unless required by applicable law or agreed to in writing, software
 *  distributed under the License is distributed on an "AS IS" BASIS,
 *  WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 *  See the License for the specific language governing permissions and
 *  limitations under the License.
 *
 */
package org.apache.tools.ant.taskdefs.optional.junit;

import java.util.Vector;
import org.w3c.dom.Attr;
import org.w3c.dom.CDATASection;
import org.w3c.dom.Comment;
import org.w3c.dom.DOMException;
import org.w3c.dom.Document;
import org.w3c.dom.Element;
import org.w3c.dom.NamedNodeMap;
import org.w3c.dom.Node;
import org.w3c.dom.NodeList;
import org.w3c.dom.ProcessingInstruction;
import org.w3c.dom.Text;

/**
 * Some utilities that might be useful when manipulating DOM trees.
 *
 */
public final class DOMUtil {

    /** unused constructor */
    private DOMUtil() {
    }

    /**
     * Filter interface to be applied when iterating over a DOM tree.
     * Just think of it like a <tt>FileFilter</tt> clone.
     */
    public interface NodeFilter {
        /**
         * @param       node    the node to check for acceptance.
         * @return      <tt>true</tt> if the node is accepted by this filter,
         *                      otherwise <tt>false</tt>
         */
        boolean accept(Node node);
    }

    /**
     * list a set of node that match a specific filter. The list can be made
     * recursively or not.
     * @param   parent  the parent node to search from
     * @param   filter  the filter that children should match.
     * @param   recurse <tt>true</tt> if you want the list to be made recursively
     *                  otherwise <tt>false</tt>.
     */
    public static NodeList listChildNodes(Node parent, NodeFilter filter, boolean recurse) {
        NodeListImpl matches = new NodeListImpl();
        NodeList children = parent.getChildNodes();
        if (children != null) {
            final int len = children.getLength();
            for (int i = 0; i < len; i++) {
                Node child = children.item(i);
                if (filter.accept(child)) {
                    matches.addElement(child);
                }
                if (recurse) {
                    NodeList recmatches = listChildNodes(child, filter, recurse);
                    final int reclength = matches.getLength();
                    for (int j = 0; j < reclength; j++) {
                        matches.addElement(recmatches.item(i));
                    }
                }
            }
        }
        return matches;
    }

    /** custom implementation of a nodelist */
    public static class NodeListImpl extends Vector implements NodeList {
        public int getLength() {
            return size();
        }
        public Node item(int i) {
            try {
                return (Node) elementAt(i);
            } catch (ArrayIndexOutOfBoundsException e) {
                return null; // conforming to NodeList interface
            }
        }
    }

    /**
     * return the attribute value of an element.
     * @param node the node to get the attribute from.
     * @param name the name of the attribute we are looking for the value.
     * @return the value of the requested attribute or <tt>null</tt> if the
     *         attribute was not found or if <tt>node</tt> is not an <tt>Element</tt>.
     */
    public static String getNodeAttribute(Node node, String name) {
        if (node instanceof Element) {
            Element element = (Element) node;
            return element.getAttribute(name);
        }
        return null;
    }


    /**
     * Iterate over the children of a given node and return the first node
     * that has a specific name.
     * @param   parent  the node to search child from. Can be <tt>null</tt>.
     * @param   tagname the child name we are looking for. Cannot be <tt>null</tt>.
     * @return  the first child that matches the given name or <tt>null</tt> if
     *                  the parent is <tt>null</tt> or if a child does not match the
     *                  given name.
     */
    public static Element getChildByTagName (Node parent, String tagname) {
        if (parent == null) {
            return null;
        }
        NodeList childList = parent.getChildNodes();
        final int len = childList.getLength();
        for (int i = 0; i < len; i++) {
            Node child = childList.item(i);
            if (child != null && child.getNodeType() == Node.ELEMENT_NODE
                && child.getNodeName().equals(tagname)) {
                return (Element) child;
            }
        }
        return null;
    }

    /**
     * Simple tree walker that will clone recursively a node. This is to
     * avoid using parser-specific API such as Sun's <tt>changeNodeOwner</tt>
     * when we are dealing with DOM L1 implementations since <tt>cloneNode(boolean)</tt>
     * will not change the owner document.
     * <tt>changeNodeOwner</tt> is much faster and avoid the costly cloning process.
     * <tt>importNode</tt> is in the DOM L2 interface.
     * @param   parent  the node parent to which we should do the import to.
     * @param   child   the node to clone recursively. Its clone will be
     *              appended to <tt>parent</tt>.
     * @return  the cloned node that is appended to <tt>parent</tt>
     */
    public static final Node importNode(Node parent, Node child) {
        Node copy = null;
        final Document doc = parent.getOwnerDocument();

        switch (child.getNodeType()) {
        case Node.CDATA_SECTION_NODE:
            copy = doc.createCDATASection(((CDATASection) child).getData());
            break;
        case Node.COMMENT_NODE:
            copy = doc.createComment(((Comment) child).getData());
            break;
        case Node.DOCUMENT_FRAGMENT_NODE:
            copy = doc.createDocumentFragment();
            break;
        case Node.ELEMENT_NODE:
            final Element elem = doc.createElement(((Element) child).getTagName());
            copy = elem;
            final NamedNodeMap attributes = child.getAttributes();
            if (attributes != null) {
                final int size = attributes.getLength();
                for (int i = 0; i < size; i++) {
                    final Attr attr = (Attr) attributes.item(i);
                    elem.setAttribute(attr.getName(), attr.getValue());
                }
            }
            break;
        case Node.ENTITY_REFERENCE_NODE:
            copy = doc.createEntityReference(child.getNodeName());
            break;
        case Node.PROCESSING_INSTRUCTION_NODE:
            final ProcessingInstruction pi = (ProcessingInstruction) child;
            copy = doc.createProcessingInstruction(pi.getTarget(), pi.getData());
            break;
        case Node.TEXT_NODE:
            copy = doc.createTextNode(((Text) child).getData());
            break;
        default:
            // this should never happen
            throw new IllegalStateException("Invalid node type: " + child.getNodeType());
        }

        // okay we have a copy of the child, now the child becomes the parent
        // and we are iterating recursively over its children.
        try {
            final NodeList children = child.getChildNodes();
            if (children != null) {
                final int size = children.getLength();
                for (int i = 0; i < size; i++) {
                    final Node newChild = children.item(i);
                    if (newChild != null) {
                        importNode(copy, newChild);
                    }
                }
            }
        } catch (DOMException ignored) {
        }

        // bingo append it. (this should normally not be done here)
        parent.appendChild(copy);
        return copy;
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
	scala.collection.parallel.AdaptiveWorkStealingTasks$AWSTWrappedTask.internal(Tasks.scala:159)
	scala.collection.parallel.AdaptiveWorkStealingTasks$AWSTWrappedTask.internal$(Tasks.scala:156)
	scala.collection.parallel.AdaptiveWorkStealingForkJoinTasks$AWSFJTWrappedTask.internal(Tasks.scala:304)
	scala.collection.parallel.AdaptiveWorkStealingTasks$AWSTWrappedTask.compute(Tasks.scala:149)
	scala.collection.parallel.AdaptiveWorkStealingTasks$AWSTWrappedTask.compute$(Tasks.scala:148)
	scala.collection.parallel.AdaptiveWorkStealingForkJoinTasks$AWSFJTWrappedTask.compute(Tasks.scala:304)
	java.base/java.util.concurrent.RecursiveAction.exec(RecursiveAction.java:194)
	java.base/java.util.concurrent.ForkJoinTask.doExec(ForkJoinTask.java:373)
	java.base/java.util.concurrent.ForkJoinPool.helpJoin(ForkJoinPool.java:1883)
	java.base/java.util.concurrent.ForkJoinTask.awaitDone(ForkJoinTask.java:440)
	java.base/java.util.concurrent.ForkJoinTask.join(ForkJoinTask.java:670)
	scala.collection.parallel.ForkJoinTasks$FJTWrappedTask.sync(Tasks.scala:243)
	scala.collection.parallel.ForkJoinTasks$FJTWrappedTask.sync$(Tasks.scala:243)
	scala.collection.parallel.AdaptiveWorkStealingForkJoinTasks$AWSFJTWrappedTask.sync(Tasks.scala:304)
	scala.collection.parallel.AdaptiveWorkStealingTasks$AWSTWrappedTask.internal(Tasks.scala:173)
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

QDox parse error in file://<WORKSPACE>/data/code-rep-dataset/Dataset5/Tasks/5693.java