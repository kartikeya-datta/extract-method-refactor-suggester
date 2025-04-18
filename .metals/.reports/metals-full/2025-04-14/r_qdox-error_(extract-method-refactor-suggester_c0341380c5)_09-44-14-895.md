error id: file://<WORKSPACE>/data/code-rep-dataset/Dataset5/Tasks/263.java
file://<WORKSPACE>/data/code-rep-dataset/Dataset5/Tasks/263.java
### com.thoughtworks.qdox.parser.ParseException: syntax error @[1,1]

error in qdox parser
file content:
```java
offset: 1
uri: file://<WORKSPACE>/data/code-rep-dataset/Dataset5/Tasks/263.java
text:
```scala
x@@mlDataField.setText(response == null ? "" : response);

/*
 * Licensed to the Apache Software Foundation (ASF) under one or more
 * contributor license agreements. See the NOTICE file distributed with this
 * work for additional information regarding copyright ownership. The ASF
 * licenses this file to You under the Apache License, Version 2.0 (the
 * "License"); you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 * 
 * http://www.apache.org/licenses/LICENSE-2.0
 * 
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS, WITHOUT
 * WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * 
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */

package org.apache.jmeter.visualizers;

import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.Dimension;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.ByteArrayInputStream;
import java.io.IOException;
import java.io.UnsupportedEncodingException;
import java.util.ArrayList;
import java.util.List;

import javax.swing.Box;
import javax.swing.JButton;
import javax.swing.JCheckBox;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JSplitPane;
import javax.swing.JTabbedPane;
import javax.swing.JTextArea;
import javax.swing.border.Border;
import javax.swing.border.EmptyBorder;
import javax.xml.parsers.ParserConfigurationException;

import org.apache.commons.lang3.exception.ExceptionUtils;
import org.apache.jmeter.assertions.gui.XMLConfPanel;
import org.apache.jmeter.extractor.XPathExtractor;
import org.apache.jmeter.samplers.SampleResult;
import org.apache.jmeter.util.JMeterUtils;
import org.apache.jmeter.util.TidyException;
import org.apache.jmeter.util.XPathUtil;
import org.apache.jorphan.gui.GuiUtils;
import org.apache.jorphan.gui.JLabeledTextField;
import org.apache.jorphan.logging.LoggingManager;
import org.apache.jorphan.util.JOrphanUtils;
import org.apache.log.Logger;
import org.w3c.dom.Document;
import org.xml.sax.SAXException;


/**
 * Implement ResultsRender for XPath tester
 */
public class RenderAsXPath implements ResultRenderer, ActionListener {

    private static final Logger logger = LoggingManager.getLoggerForClass();

    private static final String XPATH_TESTER_COMMAND = "xpath_tester"; // $NON-NLS-1$

    private JPanel xmlWithXPathPane;

    private JTextArea xmlDataField;

    private JLabeledTextField xpathExpressionField;

    private JTextArea xpathResultField;

    private JTabbedPane rightSide;

    private SampleResult sampleResult = null;

    private JScrollPane xmlDataPane;
    
    // Should we return fragment as text, rather than text of fragment?
    private final JCheckBox getFragment =
        new JCheckBox(JMeterUtils.getResString("xpath_tester_fragment"));//$NON-NLS-1$

    private final XMLConfPanel xmlConfPanel = new XMLConfPanel();

    /** {@inheritDoc} */
    @Override
    public void clearData() {
        this.xmlDataField.setText(""); // $NON-NLS-1$
        // don't set empty to keep xpath
        // xpathExpressionField.setText(""); // $NON-NLS-1$
        this.xpathResultField.setText(""); // $NON-NLS-1$
    }

    /** {@inheritDoc} */
    @Override
    public void init() {
        // Create the panels for the xpath tab
        xmlWithXPathPane = createXpathExtractorPanel();
    }

    /**
     * Display the response as text or as rendered HTML. Change the text on the
     * button appropriate to the current display.
     *
     * @param e the ActionEvent being processed
     */
    @Override
    public void actionPerformed(ActionEvent e) {
        String command = e.getActionCommand();
        if ((sampleResult != null) && (XPATH_TESTER_COMMAND.equals(command))) {
            String response = xmlDataField.getText();
            XPathExtractor extractor = new XPathExtractor();
            xmlConfPanel.modifyTestElement(extractor);
            extractor.setFragment(getFragment.isSelected());
            executeAndShowXPathTester(response, extractor);
        }
    }

    /**
     * Launch xpath engine to parse a input text
     * @param textToParse
     */
    private void executeAndShowXPathTester(String textToParse, XPathExtractor extractor) {
        if (textToParse != null && textToParse.length() > 0
                && this.xpathExpressionField.getText().length() > 0) {
            this.xpathResultField.setText(process(textToParse, extractor));
            this.xpathResultField.setCaretPosition(0); // go to first line
        }
    }

    private String process(String textToParse, XPathExtractor extractor) {
        try {
            Document doc = parseResponse(textToParse, extractor);
            List<String> matchStrings = new ArrayList<String>();
            XPathUtil.putValuesForXPathInList(doc, xpathExpressionField.getText(),
                    matchStrings, extractor.getFragment());
            StringBuilder builder = new StringBuilder();
            int nbFound = matchStrings.size();
            builder.append("Match count: ").append(nbFound).append("\n");
            for (int i = 0; i < nbFound; i++) {
                builder.append("Match[").append(i+1).append("]=").append(matchStrings.get(i)).append("\n");
            }                
            return builder.toString();
        } catch (Exception e) {
            return "Exception:"+ ExceptionUtils.getStackTrace(e);
        }
    }
    
    /*================= internal business =================*/
    /**
     * Converts (X)HTML response to DOM object Tree.
     * This version cares of charset of response.
     * @param unicodeData
     * @return
     *
     */
    private Document parseResponse(String unicodeData, XPathExtractor extractor)
      throws UnsupportedEncodingException, IOException, ParserConfigurationException,SAXException,TidyException
    {
      //TODO: validate contentType for reasonable types?

      // NOTE: responseData encoding is server specific
      //       Therefore we do byte -> unicode -> byte conversion
      //       to ensure UTF-8 encoding as required by XPathUtil
      // convert unicode String -> UTF-8 bytes
      byte[] utf8data = unicodeData.getBytes("UTF-8"); // $NON-NLS-1$
      ByteArrayInputStream in = new ByteArrayInputStream(utf8data);
      boolean isXML = JOrphanUtils.isXML(utf8data);
      // this method assumes UTF-8 input data
      return XPathUtil.makeDocument(in,false,false,extractor.useNameSpace(),
              extractor.isTolerant(),extractor.isQuiet(),extractor.showWarnings(),
              extractor.reportErrors(),isXML, extractor.isDownloadDTDs());
    }


    /** {@inheritDoc} */
    @Override
    public void renderResult(SampleResult sampleResult) {
        String response = ViewResultsFullVisualizer.getResponseAsString(sampleResult);
        try {
            xmlDataField.setText(response == null ? "" : XPathUtil.formatXml(response));
            xmlDataField.setCaretPosition(0);
        } catch (Exception e) {
            logger.error("Exception converting to XML:"+response+ ", message:"+e.getMessage(),e);
            xmlDataField.setText("Exception converting to XML:"+response+ ", message:"+e.getMessage());
            xmlDataField.setCaretPosition(0);
        }
    }

    /** {@inheritDoc} */
    @Override
    public String toString() {
        return JMeterUtils.getResString("xpath_tester"); // $NON-NLS-1$
    }


    /** {@inheritDoc} */
    @Override
    public void setupTabPane() {
         // Add xpath tester pane
        if (rightSide.indexOfTab(JMeterUtils.getResString("xpath_tester_title")) < 0) { // $NON-NLS-1$
            rightSide.addTab(JMeterUtils.getResString("xpath_tester_title"), xmlWithXPathPane); // $NON-NLS-1$
        }
        clearData();
    }

    /**
     * @return XPath Tester panel
     */
    private JPanel createXpathExtractorPanel() {
        
        xmlDataField = new JTextArea();
        xmlDataField.setEditable(false);
        xmlDataField.setLineWrap(true);
        xmlDataField.setWrapStyleWord(true);

        this.xmlDataPane = GuiUtils.makeScrollPane(xmlDataField);
        xmlDataPane.setMinimumSize(new Dimension(0, 400));

        JPanel pane = new JPanel(new BorderLayout(0, 5));

        JSplitPane mainSplit = new JSplitPane(JSplitPane.VERTICAL_SPLIT,
                xmlDataPane, createXpathExtractorTasksPanel());
        mainSplit.setDividerLocation(400);
        pane.add(mainSplit, BorderLayout.CENTER);
        return pane;
    }

    /**
     * Create the XPath task pane
     *
     * @return XPath task pane
     */
    private JPanel createXpathExtractorTasksPanel() {
        Box xpathActionPanel = Box.createVerticalBox();
        
        Box selectorAndButton = Box.createHorizontalBox();

        Border margin = new EmptyBorder(5, 5, 0, 5);
        xpathActionPanel.setBorder(margin);
        xpathExpressionField = new JLabeledTextField(JMeterUtils.getResString("xpath_tester_field")); // $NON-NLS-1$
        
        JButton xpathTester = new JButton(JMeterUtils.getResString("xpath_tester_button_test")); // $NON-NLS-1$
        xpathTester.setActionCommand(XPATH_TESTER_COMMAND);
        xpathTester.addActionListener(this);
        
        selectorAndButton.add(xpathExpressionField);
        selectorAndButton.add(xpathTester);
        
        xpathActionPanel.add(selectorAndButton);
        xpathActionPanel.add(xmlConfPanel);
        xpathActionPanel.add(getFragment);
        
        xpathResultField = new JTextArea();
        xpathResultField.setEditable(false);
        xpathResultField.setLineWrap(true);
        xpathResultField.setWrapStyleWord(true);

        JPanel xpathTasksPanel = new JPanel(new BorderLayout(0, 5));
        xpathTasksPanel.add(xpathActionPanel, BorderLayout.NORTH);
        xpathTasksPanel.add(GuiUtils.makeScrollPane(xpathResultField), BorderLayout.CENTER);

        return xpathTasksPanel;
    }

    /** {@inheritDoc} */
    @Override
    public synchronized void setRightSide(JTabbedPane side) {
        rightSide = side;
    }

    /** {@inheritDoc} */
    @Override
    public synchronized void setSamplerResult(Object userObject) {
        if (userObject instanceof SampleResult) {
            sampleResult = (SampleResult) userObject;
        }
    }

    /** {@inheritDoc} */
    @Override
    public void setLastSelectedTab(int index) {
        // nothing to do
    }

    /** {@inheritDoc} */
    @Override
    public void renderImage(SampleResult sampleResult) {
        clearData();
        xmlDataField.setText(JMeterUtils.getResString("xpath_tester_no_text")); // $NON-NLS-1$
    }

    /** {@inheritDoc} */
    @Override
    public void setBackgroundColor(Color backGround) {
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

QDox parse error in file://<WORKSPACE>/data/code-rep-dataset/Dataset5/Tasks/263.java