error id: file://<WORKSPACE>/data/code-rep-dataset/Dataset5/Tasks/9406.java
file://<WORKSPACE>/data/code-rep-dataset/Dataset5/Tasks/9406.java
### com.thoughtworks.qdox.parser.ParseException: syntax error @[1,1]

error in qdox parser
file content:
```java
offset: 1
uri: file://<WORKSPACE>/data/code-rep-dataset/Dataset5/Tasks/9406.java
text:
```scala
S@@tringBuilder strBuf = new StringBuilder("<html><body bgcolor=\"yellow\"><b>"); // $NON-NLS-1$

/*
 * Licensed to the Apache Software Foundation (ASF) under one or more
 * contributor license agreements.  See the NOTICE file distributed with
 * this work for additional information regarding copyright ownership.
 * The ASF licenses this file to You under the Apache License, Version 2.0
 * (the "License"); you may not use this file except in compliance with
 * the License.  You may obtain a copy of the License at
 *
 *   http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed  under the  License is distributed on an "AS IS" BASIS,
 * WITHOUT  WARRANTIES OR CONDITIONS  OF ANY KIND, either  express  or
 * implied.
 *
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */

package org.apache.jmeter.visualizers;

import java.awt.Component;
import java.awt.GridLayout;
import java.io.ByteArrayInputStream;
import java.io.StringWriter;

import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JTree;
import javax.swing.ToolTipManager;
import javax.swing.tree.DefaultMutableTreeNode;
import javax.swing.tree.DefaultTreeCellRenderer;
import javax.swing.tree.TreeSelectionModel;

import org.apache.jmeter.samplers.SampleResult;
import org.apache.jmeter.util.JMeterUtils;
import org.apache.jmeter.util.XPathUtil;
import org.apache.jorphan.logging.LoggingManager;
import org.apache.jorphan.util.JOrphanUtils;
import org.apache.log.Logger;
import org.w3c.dom.Node;
import org.w3c.dom.NodeList;
import org.w3c.tidy.Tidy;
import org.xml.sax.SAXException;

public class RenderAsXML extends SamplerResultTab
    implements ResultRenderer {

    private static final Logger log = LoggingManager.getLoggerForClass();

    private static final byte[] XML_PFX = {'<','?','x','m','l',' '};//"<?xml "

    public RenderAsXML(){
        activateSearchExtension = false; // TODO work out how to search the XML pane
    }

    /** {@inheritDoc} */
    public void renderResult(SampleResult sampleResult) {
        showRenderXMLResponse(sampleResult);
    }

    private void showRenderXMLResponse(SampleResult res) {
        results.setContentType("text/xml"); // $NON-NLS-1$
        results.setCaretPosition(0);
        byte[] source = res.getResponseData();
        final ByteArrayInputStream baIS = new ByteArrayInputStream(source);
        for(int i=0; i<source.length-XML_PFX.length; i++){
            if (JOrphanUtils.startsWith(source, XML_PFX, i)){
                baIS.skip(i);// Skip the leading bytes (if any)
                break;
            }
        }

        // there is also a javax.swing.text.Document class.
        org.w3c.dom.Document document = null;

        StringWriter sw = new StringWriter();
        Tidy tidy = XPathUtil.makeTidyParser(true, true, true, sw);
        document = tidy.parseDOM(baIS, null);
        document.normalize();
        if (tidy.getParseErrors() > 0) {
            showErrorMessageDialog(sw.toString(),
                    "Tidy: " + tidy.getParseErrors() + " errors, " + tidy.getParseWarnings() + " warnings",
                    JOptionPane.WARNING_MESSAGE);
        }

        JPanel domTreePanel = new DOMTreePanel(document);
        resultsScrollPane.setViewportView(domTreePanel);
    }

    /*
     *
     * A Dom tree panel for to display response as tree view author <a
     * href="mailto:d.maung@mdl.com">Dave Maung</a> 
     * TODO implement to find any nodes in the tree using TreePath.
     *
     */
    private static class DOMTreePanel extends JPanel {

        private static final long serialVersionUID = 6871690021183779153L;

        private JTree domJTree;

        public DOMTreePanel(org.w3c.dom.Document document) {
            super(new GridLayout(1, 0));
            try {
                Node firstElement = getFirstElement(document);
                DefaultMutableTreeNode top = new XMLDefaultMutableTreeNode(firstElement);
                domJTree = new JTree(top);

                domJTree.getSelectionModel().setSelectionMode(TreeSelectionModel.SINGLE_TREE_SELECTION);
                domJTree.setShowsRootHandles(true);
                JScrollPane domJScrollPane = new JScrollPane(domJTree);
                domJTree.setAutoscrolls(true);
                this.add(domJScrollPane);
                ToolTipManager.sharedInstance().registerComponent(domJTree);
                domJTree.setCellRenderer(new DomTreeRenderer());
            } catch (SAXException e) {
                log.warn("Error trying to parse document", e);
            }

        }

        /**
         * Skip all DTD nodes, all prolog nodes. They dont support in tree view
         * We let user to insert them however in DOMTreeView, we dont display it
         *
         * @param root
         * @return
         */
        private Node getFirstElement(Node parent) {
            NodeList childNodes = parent.getChildNodes();
            Node toReturn = parent; // Must return a valid node, or may generate an NPE
            for (int i = 0; i < childNodes.getLength(); i++) {
                Node childNode = childNodes.item(i);
                toReturn = childNode;
                if (childNode.getNodeType() == Node.ELEMENT_NODE){
                    break;
                }

            }
            return toReturn;
        }

        /**
         * This class is to view as tooltext. This is very useful, when the
         * contents has long string and does not fit in the view. it will also
         * automatically wrap line for each 100 characters since tool tip
         * support html. author <a href="mailto:d.maung@mdl.com">Dave Maung</a>
         */
        private static class DomTreeRenderer extends DefaultTreeCellRenderer {

            private static final long serialVersionUID = 240210061375790195L;

            @Override
            public Component getTreeCellRendererComponent(JTree tree, Object value, boolean sel, boolean expanded,
                    boolean leaf, int row, boolean phasFocus) {
                super.getTreeCellRendererComponent(tree, value, sel, expanded, leaf, row, phasFocus);

                DefaultMutableTreeNode valueTreeNode = (DefaultMutableTreeNode) value;
                setToolTipText(getHTML(valueTreeNode.toString(), "<br>", 100)); // $NON-NLS-1$
                return this;
            }

            /**
             * get the html
             *
             * @param str
             * @param separator
             * @param maxChar
             * @return
             */
            private String getHTML(String str, String separator, int maxChar) {
                StringBuffer strBuf = new StringBuffer("<html><body bgcolor=\"yellow\"><b>"); // $NON-NLS-1$
                char[] chars = str.toCharArray();
                for (int i = 0; i < chars.length; i++) {

                    if (i % maxChar == 0 && i != 0) {
                        strBuf.append(separator);
                    }
                    strBuf.append(encode(chars[i]));

                }
                strBuf.append("</b></body></html>"); // $NON-NLS-1$
                return strBuf.toString();

            }

            private String encode(char c) {
                String toReturn = String.valueOf(c);
                switch (c) {
                case '<': // $NON-NLS-1$
                    toReturn = "&lt;"; // $NON-NLS-1$
                    break;
                case '>': // $NON-NLS-1$
                    toReturn = "&gt;"; // $NON-NLS-1$
                    break;
                case '\'': // $NON-NLS-1$
                    toReturn = "&apos;"; // $NON-NLS-1$
                    break;
                case '\"': // $NON-NLS-1$
                    toReturn = "&quot;"; // $NON-NLS-1$
                    break;

                }
                return toReturn;
            }
        }
    }

    private static void showErrorMessageDialog(String message, String title, int messageType) {
        JOptionPane.showMessageDialog(null, message, title, messageType);
    }

    /** {@inheritDoc} */
    @Override
    public String toString() {
        return JMeterUtils.getResString("view_results_render_xml"); // $NON-NLS-1$
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

QDox parse error in file://<WORKSPACE>/data/code-rep-dataset/Dataset5/Tasks/9406.java