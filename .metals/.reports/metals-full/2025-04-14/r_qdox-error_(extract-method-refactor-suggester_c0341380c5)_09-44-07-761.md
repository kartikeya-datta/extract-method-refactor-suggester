error id: file://<WORKSPACE>/data/code-rep-dataset/Dataset5/Tasks/16903.java
file://<WORKSPACE>/data/code-rep-dataset/Dataset5/Tasks/16903.java
### com.thoughtworks.qdox.parser.ParseException: syntax error @[1,3]

error in qdox parser
file content:
```java
offset: 3
uri: file://<WORKSPACE>/data/code-rep-dataset/Dataset5/Tasks/16903.java
text:
```scala
+ o@@rg.apache.xerces.impl.Version.fVersion

/*
 * The Apache Software License, Version 1.1
 *
 *
 * Copyright (c) 2001 The Apache Software Foundation.  All rights 
 * reserved.
 *
 * Redistribution and use in source and binary forms, with or without
 * modification, are permitted provided that the following conditions
 * are met:
 *
 * 1. Redistributions of source code must retain the above copyright
 *    notice, this list of conditions and the following disclaimer. 
 *
 * 2. Redistributions in binary form must reproduce the above copyright
 *    notice, this list of conditions and the following disclaimer in
 *    the documentation and/or other materials provided with the
 *    distribution.
 *
 * 3. The end-user documentation included with the redistribution,
 *    if any, must include the following acknowledgment:  
 *       "This product includes software developed by the
 *        Apache Software Foundation (http://www.apache.org/)."
 *    Alternately, this acknowledgment may appear in the software itself,
 *    if and wherever such third-party acknowledgments normally appear.
 *
 * 4. The names "Xerces" and "Apache Software Foundation" must
 *    not be used to endorse or promote products derived from this
 *    software without prior written permission. For written 
 *    permission, please contact apache@apache.org.
 *
 * 5. Products derived from this software may not be called "Apache",
 *    nor may "Apache" appear in their name, without prior written
 *    permission of the Apache Software Foundation.
 *
 * THIS SOFTWARE IS PROVIDED ``AS IS'' AND ANY EXPRESSED OR IMPLIED
 * WARRANTIES, INCLUDING, BUT NOT LIMITED TO, THE IMPLIED WARRANTIES
 * OF MERCHANTABILITY AND FITNESS FOR A PARTICULAR PURPOSE ARE
 * DISCLAIMED.  IN NO EVENT SHALL THE APACHE SOFTWARE FOUNDATION OR
 * ITS CONTRIBUTORS BE LIABLE FOR ANY DIRECT, INDIRECT, INCIDENTAL,
 * SPECIAL, EXEMPLARY, OR CONSEQUENTIAL DAMAGES (INCLUDING, BUT NOT
 * LIMITED TO, PROCUREMENT OF SUBSTITUTE GOODS OR SERVICES; LOSS OF
 * USE, DATA, OR PROFITS; OR BUSINESS INTERRUPTION) HOWEVER CAUSED AND
 * ON ANY THEORY OF LIABILITY, WHETHER IN CONTRACT, STRICT LIABILITY,
 * OR TORT (INCLUDING NEGLIGENCE OR OTHERWISE) ARISING IN ANY WAY OUT
 * OF THE USE OF THIS SOFTWARE, EVEN IF ADVISED OF THE POSSIBILITY OF
 * SUCH DAMAGE.
 * ====================================================================
 *
 * This software consists of voluntary contributions made by many
 * individuals on behalf of the Apache Software Foundation and was
 * originally developed by Christian Geuer-Pollmann.  For more
 * information on the Apache Software Foundation, please see
 * <http://www.apache.org/>.
 */

package dom.treewalker;
import java.io.*;
import javax.xml.parsers.*;
import org.w3c.dom.*;
import org.w3c.dom.traversal.*;
import org.xml.sax.*;


/**
 * The class tests TreeWalkerImpl.firstChild() and TreeWalkerImpl.nextSibling();
 * The class generates simple XML document and traverses it.
 * 
 * @author Christian Geuer-Pollmann <geuer-pollmann@nue.et-inf.uni-siegen.de>
 * @version $Id$
 */
public class TestFirstChild {

    public static void main(String args[]) throws Exception {


        System.out.println(" --- "
                           + org.apache.xerces.framework.Version.fVersion
                           + " --- ");
        Document doc = getNodeSet1();
        NodeFilter nodefilter = null;
        boolean entityReferenceExpansion = true;
        int whatToShow = NodeFilter.SHOW_ALL;
        TreeWalker treewalker =
        ((DocumentTraversal) doc).createTreeWalker(doc, whatToShow,
                                                   nodefilter, entityReferenceExpansion);
        ByteArrayOutputStream bytearrayoutputstream =
        new ByteArrayOutputStream();
        PrintWriter printwriter =
        new PrintWriter(new OutputStreamWriter(bytearrayoutputstream,
                                               "UTF8"));

        process2(treewalker, printwriter);
        printwriter.flush();

        System.out.println();
        System.out.println("Testing the following XML document:\n" + new String(bytearrayoutputstream.toByteArray()));
    }

    /**
     * Method getNodeSet1
     *
     * @return
     * @throws ParserConfigurationException
     */
    private static Document getNodeSet1()
    throws ParserConfigurationException {

        DocumentBuilderFactory dfactory =
        DocumentBuilderFactory.newInstance();

        dfactory.setValidating(false);
        dfactory.setNamespaceAware(true);

        DocumentBuilder db = dfactory.newDocumentBuilder();
        Document doc = db.newDocument();
        Element root = doc.createElement("RootElement");
        Element e1 = doc.createElement("Element1");
        Element e2 = doc.createElement("Element2");
        Element e3 = doc.createElement("Element3");
        Text e3t = doc.createTextNode("Text in Element3");

        e3.appendChild(e3t);
        root.appendChild(e1);
        root.appendChild(e2);
        root.appendChild(e3);
        doc.appendChild(root);

        String s1 ="<RootElement><Element1/><Element2/><Element3>Text in Element3</Element3></RootElement>";

        return doc;
    }

    /**
     * recursively traverses the tree
     *
     * for simplicity, I don't handle comments, Attributes, PIs etc.
     * Only Text, Document and Element
     *
     * @param treewalker
     * @param printwriter
     */
    private static void process2(TreeWalker treewalker,
                                 PrintWriter printwriter) {

        Node currentNode = treewalker.getCurrentNode();

        switch (currentNode.getNodeType()) {
        
        case Node.TEXT_NODE :
        case Node.CDATA_SECTION_NODE :
            printwriter.print(currentNode.getNodeValue());
            break;

        case Node.ENTITY_REFERENCE_NODE :
        case Node.DOCUMENT_NODE :
        case Node.ELEMENT_NODE :
        default :
            if (currentNode.getNodeType() == Node.ELEMENT_NODE) {
                printwriter.print('<');
                printwriter.print(currentNode.getNodeName());
                printwriter.print(">");
            }

            Node node1 = treewalker.firstChild();

            if (node1 == null) {
                System.out.println(getNodeTypeString(currentNode.getNodeType())
                                   + "_NODE parent: "
                                   + currentNode.getNodeName()
                                   + " has no children ");
            }
            else {
                System.out.println(getNodeTypeString(currentNode.getNodeType())
                                   + "_NODE parent: "
                                   + currentNode.getNodeName()
                                   + " has children ");

                while (node1 != null) {
                    {
                        String qStr = "";

                        for (Node q = node1; q != null; q = q.getParentNode()) {
                            qStr = q.getNodeName() + "/" + qStr;
                        }

                        System.out.println(getNodeTypeString(currentNode.getNodeType())
                                           + "_NODE process child " + qStr);
                    }


                    // recursion !!!
                    process2(treewalker, printwriter);

                    node1 = treewalker.nextSibling();
                    if (node1 != null) {
                        System.out.println("treewalker.nextSibling() = "
                                           + node1.getNodeName());
                    }
                } // while(node1 != null)
            }

            System.out.println("setCurrentNode() back to "
                               + currentNode.getNodeName());
            treewalker.setCurrentNode(currentNode);

            if (currentNode.getNodeType() == Node.ELEMENT_NODE) {
                printwriter.print("</");
                printwriter.print(currentNode.getNodeName());
                printwriter.print(">");
            }

            break;
        }
    }

    /** Field nodeTypeString */
    private static String[] nodeTypeString = new String[]{ "", "ELEMENT",
        "ATTRIBUTE",
        "TEXT_NODE",
        "CDATA_SECTION",
        "ENTITY_REFERENCE",
        "ENTITY",
        "PROCESSING_INSTRUCTION",
        "COMMENT",
        "DOCUMENT",
        "DOCUMENT_TYPE",
        "DOCUMENT_FRAGMENT",
        "NOTATION"};



    /**
     *    
     *  Transforms <code>org.w3c.dom.Node.XXX_NODE</code> NodeType values into
     *  XXX Strings.
     * 
     *  @param nodeType as taken from the {@link org.w3c.dom.Node#getNodeType}
     * function
     *  @return the String value.
     *  @see org.w3c.dom.Node#getNodeType
     * @param nodeType
     * @return 
     */
    public static String getNodeTypeString(short nodeType) {

        if ((nodeType > 0) && (nodeType < 13)) {
            return nodeTypeString[nodeType];
        }
        else {
            return "";
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

QDox parse error in file://<WORKSPACE>/data/code-rep-dataset/Dataset5/Tasks/16903.java