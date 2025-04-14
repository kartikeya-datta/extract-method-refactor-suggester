error id: file://<WORKSPACE>/data/code-rep-dataset/Dataset5/Tasks/6839.java
file://<WORKSPACE>/data/code-rep-dataset/Dataset5/Tasks/6839.java
### com.thoughtworks.qdox.parser.ParseException: syntax error @[1,1]

error in qdox parser
file content:
```java
offset: 1
uri: file://<WORKSPACE>/data/code-rep-dataset/Dataset5/Tasks/6839.java
text:
```scala
c@@loseConnection = new JCheckBox("", TCPSampler.CLOSE_CONNECTION_DEFAULT);

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
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 *
 */

package org.apache.jmeter.protocol.tcp.config.gui;

import java.awt.BorderLayout;
import java.awt.Dimension;
import java.awt.FlowLayout;
import java.awt.event.ItemEvent;
import java.awt.event.ItemListener;

import javax.swing.JCheckBox;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JTextArea;
import javax.swing.JTextField;

import org.apache.jmeter.config.ConfigTestElement;
import org.apache.jmeter.config.gui.AbstractConfigGui;
import org.apache.jmeter.gui.ServerPanel;
import org.apache.jmeter.gui.util.HorizontalPanel;
import org.apache.jmeter.gui.util.VerticalPanel;
import org.apache.jmeter.protocol.tcp.sampler.TCPSampler;
import org.apache.jmeter.testelement.TestElement;
import org.apache.jmeter.util.JMeterUtils;
import org.apache.jorphan.gui.JLabeledTextField;

public class TCPConfigGui extends AbstractConfigGui {

    private static final long serialVersionUID = 240L;

    private ServerPanel serverPanel;
    
    private JLabeledTextField classname;

    private JCheckBox reUseConnection;

    // NOTUSED yet private JTextField filename;

    private JCheckBox setNoDelay;

    private JCheckBox closeConnection;

    private JTextField soLinger;

    private JTextField eolByte;

    private JTextArea requestData;

    private boolean displayName = true;

    public TCPConfigGui() {
        this(true);
    }

    public TCPConfigGui(boolean displayName) {
        this.displayName = displayName;
        init();
    }

    @Override
    public String getLabelResource() {
        return "tcp_config_title"; // $NON-NLS-1$
    }

    @Override
    public void configure(TestElement element) {
        super.configure(element);
        // N.B. this will be a config element, so we cannot use the getXXX() methods
        classname.setText(element.getPropertyAsString(TCPSampler.CLASSNAME));
        serverPanel.setServer(element.getPropertyAsString(TCPSampler.SERVER));
        // Default to original behaviour, i.e. re-use connection
        reUseConnection.setSelected(element.getPropertyAsBoolean(TCPSampler.RE_USE_CONNECTION,true));
        serverPanel.setPort(element.getPropertyAsString(TCPSampler.PORT));
        // filename.setText(element.getPropertyAsString(TCPSampler.FILENAME));
        serverPanel.setResponseTimeout(element.getPropertyAsString(TCPSampler.TIMEOUT));
        serverPanel.setConnectTimeout(element.getPropertyAsString(TCPSampler.TIMEOUT_CONNECT));
        setNoDelay.setSelected(element.getPropertyAsBoolean(TCPSampler.NODELAY));
        requestData.setText(element.getPropertyAsString(TCPSampler.REQUEST));
        closeConnection.setSelected(element.getPropertyAsBoolean(TCPSampler.CLOSE_CONNECTION, TCPSampler.CLOSE_CONNECTION_DEFAULT));
        soLinger.setText(element.getPropertyAsString(TCPSampler.SO_LINGER));
        eolByte.setText(element.getPropertyAsString(TCPSampler.EOL_BYTE));
    }

    @Override
    public TestElement createTestElement() {
        ConfigTestElement element = new ConfigTestElement();
        modifyTestElement(element);
        return element;
    }

    /**
     * Modifies a given TestElement to mirror the data in the gui components.
     *
     * @see org.apache.jmeter.gui.JMeterGUIComponent#modifyTestElement(TestElement)
     */
    @Override
    public void modifyTestElement(TestElement element) {
        configureTestElement(element);
        // N.B. this will be a config element, so we cannot use the setXXX() methods
        element.setProperty(TCPSampler.CLASSNAME, classname.getText(), "");
        element.setProperty(TCPSampler.SERVER, serverPanel.getServer());
        element.setProperty(TCPSampler.RE_USE_CONNECTION, reUseConnection.isSelected());
        element.setProperty(TCPSampler.PORT, serverPanel.getPort());
        // element.setProperty(TCPSampler.FILENAME, filename.getText());
        element.setProperty(TCPSampler.NODELAY, setNoDelay.isSelected());
        element.setProperty(TCPSampler.TIMEOUT, serverPanel.getResponseTimeout());
        element.setProperty(TCPSampler.TIMEOUT_CONNECT, serverPanel.getConnectTimeout(),"");
        element.setProperty(TCPSampler.REQUEST, requestData.getText());
        element.setProperty(TCPSampler.CLOSE_CONNECTION, closeConnection.isSelected(), TCPSampler.CLOSE_CONNECTION_DEFAULT);
        element.setProperty(TCPSampler.SO_LINGER, soLinger.getText(), "");
        element.setProperty(TCPSampler.EOL_BYTE, eolByte.getText(), "");
    }

    /**
     * Implements JMeterGUIComponent.clearGui
     */
    @Override
    public void clearGui() {
        super.clearGui();

        serverPanel.clear();
        classname.setText(""); //$NON-NLS-1$
        requestData.setText(""); //$NON-NLS-1$
        reUseConnection.setSelected(true);
        setNoDelay.setSelected(false);
        closeConnection.setSelected(TCPSampler.CLOSE_CONNECTION_DEFAULT);
        soLinger.setText(""); //$NON-NLS-1$
        eolByte.setText(""); //$NON-NLS-1$
    }


    private JPanel createNoDelayPanel() {
        JLabel label = new JLabel(JMeterUtils.getResString("tcp_nodelay")); // $NON-NLS-1$

        setNoDelay = new JCheckBox();
        label.setLabelFor(setNoDelay);

        JPanel nodelayPanel = new JPanel(new FlowLayout());
        nodelayPanel.add(label);
        nodelayPanel.add(setNoDelay);
        return nodelayPanel;
    }

    private JPanel createClosePortPanel() {
        JLabel label = new JLabel(JMeterUtils.getResString("reuseconnection")); //$NON-NLS-1$

        reUseConnection = new JCheckBox("", true);
        reUseConnection.addItemListener(new ItemListener() {
            @Override
            public void itemStateChanged(final ItemEvent e) {
                if (e.getStateChange() == ItemEvent.SELECTED) {
                    closeConnection.setEnabled(true);
                } else {
                    closeConnection.setEnabled(false);
                }
            }
        });
        label.setLabelFor(reUseConnection);

        JPanel closePortPanel = new JPanel(new FlowLayout());
        closePortPanel.add(label);
        closePortPanel.add(reUseConnection);
        return closePortPanel;
    }

    private JPanel createCloseConnectionPanel() {
        JLabel label = new JLabel(JMeterUtils.getResString("closeconnection")); // $NON-NLS-1$

        closeConnection = new JCheckBox("", false);
        label.setLabelFor(closeConnection);

        JPanel closeConnectionPanel = new JPanel(new FlowLayout());
        closeConnectionPanel.add(label);
        closeConnectionPanel.add(closeConnection);
        return closeConnectionPanel;
    }

    private JPanel createSoLingerOption() {
        JLabel label = new JLabel(JMeterUtils.getResString("solinger")); //$NON-NLS-1$ 

        soLinger = new JTextField(5); // 5 columns size
        soLinger.setMaximumSize(new Dimension(soLinger.getPreferredSize()));
        label.setLabelFor(soLinger);

        JPanel soLingerPanel = new JPanel(new FlowLayout());
        soLingerPanel.add(label);
        soLingerPanel.add(soLinger);
        return soLingerPanel;
    }

    private JPanel createEolBytePanel() {
        JLabel label = new JLabel(JMeterUtils.getResString("eolbyte")); //$NON-NLS-1$ 

        eolByte = new JTextField(3); // 3 columns size
        eolByte.setMaximumSize(new Dimension(eolByte.getPreferredSize()));
        label.setLabelFor(eolByte);

        JPanel eolBytePanel = new JPanel(new FlowLayout());
        eolBytePanel.add(label);
        eolBytePanel.add(eolByte);
        return eolBytePanel;
    }

    private JPanel createRequestPanel() {
        JLabel reqLabel = new JLabel(JMeterUtils.getResString("tcp_request_data")); // $NON-NLS-1$
        requestData = new JTextArea(3, 0);
        requestData.setLineWrap(true);
        reqLabel.setLabelFor(requestData);

        JPanel reqDataPanel = new JPanel(new BorderLayout(5, 0));
        reqDataPanel.add(reqLabel, BorderLayout.WEST);
        reqDataPanel.add(requestData, BorderLayout.CENTER);
        return reqDataPanel;
    }

    // private JPanel createFilenamePanel()//Not used yet
    // {
    //
    // JLabel label = new JLabel(JMeterUtils.getResString("file_to_retrieve")); // $NON-NLS-1$
    //
    // filename = new JTextField(10);
    // filename.setName(FILENAME);
    // label.setLabelFor(filename);
    //
    // JPanel filenamePanel = new JPanel(new BorderLayout(5, 0));
    // filenamePanel.add(label, BorderLayout.WEST);
    // filenamePanel.add(filename, BorderLayout.CENTER);
    // return filenamePanel;
    // }

    private void init() {
        setLayout(new BorderLayout(0, 5));

        serverPanel = new ServerPanel();
        
        if (displayName) {
            setBorder(makeBorder());
            add(makeTitlePanel(), BorderLayout.NORTH);
        }

        VerticalPanel mainPanel = new VerticalPanel();
        classname = new JLabeledTextField(JMeterUtils.getResString("tcp_classname"));
        mainPanel.add(classname);
        mainPanel.add(serverPanel);
        
        HorizontalPanel optionsPanel = new HorizontalPanel();
        optionsPanel.add(createClosePortPanel());
        optionsPanel.add(createCloseConnectionPanel());
        optionsPanel.add(createNoDelayPanel());
        optionsPanel.add(createSoLingerOption());
        optionsPanel.add(createEolBytePanel());
        mainPanel.add(optionsPanel);
        mainPanel.add(createRequestPanel());

        // mainPanel.add(createFilenamePanel());
        add(mainPanel, BorderLayout.CENTER);
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
	java.base/java.util.concurrent.ForkJoinPool$WorkQueue.topLevelExec(ForkJoinPool.java:1182)
	java.base/java.util.concurrent.ForkJoinPool.scan(ForkJoinPool.java:1655)
	java.base/java.util.concurrent.ForkJoinPool.runWorker(ForkJoinPool.java:1622)
	java.base/java.util.concurrent.ForkJoinWorkerThread.run(ForkJoinWorkerThread.java:165)
```
#### Short summary: 

QDox parse error in file://<WORKSPACE>/data/code-rep-dataset/Dataset5/Tasks/6839.java