error id: file://<WORKSPACE>/data/code-rep-dataset/Dataset5/Tasks/15226.java
file://<WORKSPACE>/data/code-rep-dataset/Dataset5/Tasks/15226.java
### com.thoughtworks.qdox.parser.ParseException: syntax error @[1,1]

error in qdox parser
file content:
```java
offset: 1
uri: file://<WORKSPACE>/data/code-rep-dataset/Dataset5/Tasks/15226.java
text:
```scala
s@@howScopeSettings(assertion, true);

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

package org.apache.jmeter.assertions.gui;

import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

import javax.swing.BorderFactory;
import javax.swing.Box;
import javax.swing.ButtonGroup;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JRadioButton;
import javax.swing.JTextField;

import org.apache.jmeter.assertions.SizeAssertion;
import org.apache.jmeter.testelement.TestElement;
import org.apache.jmeter.util.JMeterUtils;
import org.apache.jorphan.gui.layout.VerticalLayout;

/**
 * GUI for {@link SizeAssertion}
 */
public class SizeAssertionGui extends AbstractAssertionGui implements ActionListener {

    private static final long serialVersionUID = 241L;
    
    /** Radio button indicating that the body response should be tested. */
    private JRadioButton responseBodyButton;

    /** Radio button indicating that the network response size should be tested. */
    private JRadioButton responseNetworkButton;

    /** Radio button indicating that the responseMessage should be tested. */
    private JRadioButton responseMessageButton;

    /** Radio button indicating that the responseCode should be tested. */
    private JRadioButton responseCodeButton;

    /** Radio button indicating that the headers should be tested. */
    private JRadioButton responseHeadersButton;

    private JTextField size;

    private JRadioButton equalButton, notequalButton, greaterthanButton, lessthanButton, greaterthanequalButton,
            lessthanequalButton;

    private int execState; // store the operator

    public SizeAssertionGui() {
        init();
    }

    public String getLabelResource() {
        return "size_assertion_title"; //$NON-NLS-1$
    }

    public TestElement createTestElement() {
        SizeAssertion el = new SizeAssertion();
        modifyTestElement(el);
        return el;
    }

    /**
     * Modifies a given TestElement to mirror the data in the gui components.
     *
     * @see org.apache.jmeter.gui.JMeterGUIComponent#modifyTestElement(TestElement)
     */
    public void modifyTestElement(TestElement el) {
        configureTestElement(el);
        SizeAssertion assertion = (SizeAssertion) el;
        
        if (responseHeadersButton.isSelected()) {
            assertion.setTestFieldResponseHeaders();
        } else if (responseBodyButton.isSelected()) {
            assertion.setTestFieldResponseBody();
        } else if (responseCodeButton.isSelected()) {
            assertion.setTestFieldResponseCode();
        } else if (responseMessageButton.isSelected()) {
            assertion.setTestFieldResponseMessage();
        } else {
            assertion.setTestFieldNetworkSize();
        }
        assertion.setAllowedSize(size.getText());
        assertion.setCompOper(getState());
        saveScopeSettings(assertion);
    }

    /**
     * Implements JMeterGUIComponent.clearGui
     */
    @Override
    public void clearGui() {
        super.clearGui();

        responseNetworkButton.setSelected(true); // default
        responseHeadersButton.setSelected(false);
        responseBodyButton.setSelected(false);
        responseCodeButton.setSelected(false);
        responseMessageButton.setSelected(false);
        
        size.setText(""); //$NON-NLS-1$
        equalButton.setSelected(true);
        notequalButton.setSelected(false);
        greaterthanButton.setSelected(false);
        lessthanButton.setSelected(false);
        greaterthanequalButton.setSelected(false);
        lessthanequalButton.setSelected(false);
        execState = SizeAssertion.EQUAL;
    }

    @Override
    public void configure(TestElement el) {
        super.configure(el);
        SizeAssertion assertion = (SizeAssertion) el;
        size.setText(assertion.getAllowedSize());
        setState(assertion.getCompOper());
        showScopeSettings(assertion);
        
        if (assertion.isTestFieldResponseHeaders()) {
        responseHeadersButton.setSelected(true);
        } else if (assertion.isTestFieldResponseBody()) {
            responseBodyButton.setSelected(true);
        } else if (assertion.isTestFieldResponseCode()) {
            responseCodeButton.setSelected(true);
        } else if (assertion.isTestFieldResponseMessage()) {
            responseMessageButton.setSelected(true);
        } else {
            responseNetworkButton.setSelected(true);
        }
    }

    /**
     * Set the state of the radio Button
     */
    public void setState(int state) {
        if (state == SizeAssertion.EQUAL) {
            equalButton.setSelected(true);
            execState = state;
        } else if (state == SizeAssertion.NOTEQUAL) {
            notequalButton.setSelected(true);
            execState = state;
        } else if (state == SizeAssertion.GREATERTHAN) {
            greaterthanButton.setSelected(true);
            execState = state;
        } else if (state == SizeAssertion.LESSTHAN) {
            lessthanButton.setSelected(true);
            execState = state;
        } else if (state == SizeAssertion.GREATERTHANEQUAL) {
            greaterthanequalButton.setSelected(true);
            execState = state;
        } else if (state == SizeAssertion.LESSTHANEQUAL) {
            lessthanequalButton.setSelected(true);
            execState = state;
        }
    }

    /**
     * Get the state of the radio Button
     */
    public int getState() {
        return execState;
    }

    private void init() {
        setLayout(new VerticalLayout(5, VerticalLayout.BOTH, VerticalLayout.TOP));
        setBorder(makeBorder());

        add(makeTitlePanel());

        add(createScopePanel(true));
        add(createFieldPanel());

        // USER_INPUT
        JPanel sizePanel = new JPanel();
        sizePanel.setBorder(BorderFactory.createTitledBorder(BorderFactory.createEtchedBorder(),
                JMeterUtils.getResString("size_assertion_size_test"))); //$NON-NLS-1$

        sizePanel.add(new JLabel(JMeterUtils.getResString("size_assertion_label"))); //$NON-NLS-1$
        size = new JTextField(12);
        sizePanel.add(size);

        sizePanel.add(createComparatorButtonPanel());

        add(sizePanel);
    }
    
    /**
     * Create a panel allowing the user to choose which response field should be
     * tested.
     *
     * @return a new panel for selecting the response field
     */
    private JPanel createFieldPanel() {
        JPanel panel = new JPanel();
        panel.setBorder(BorderFactory.createTitledBorder(JMeterUtils.getResString("assertion_resp_size_field"))); //$NON-NLS-1$

        responseNetworkButton = new JRadioButton(JMeterUtils.getResString("assertion_network_size")); //$NON-NLS-1$
        responseHeadersButton = new JRadioButton(JMeterUtils.getResString("assertion_headers")); //$NON-NLS-1$
        responseBodyButton = new JRadioButton(JMeterUtils.getResString("assertion_body_resp")); //$NON-NLS-1$
        responseCodeButton = new JRadioButton(JMeterUtils.getResString("assertion_code_resp")); //$NON-NLS-1$
        responseMessageButton = new JRadioButton(JMeterUtils.getResString("assertion_message_resp")); //$NON-NLS-1$

        ButtonGroup group = new ButtonGroup();
        group.add(responseNetworkButton);
        group.add(responseHeadersButton);
        group.add(responseBodyButton);
        group.add(responseCodeButton);
        group.add(responseMessageButton);

        panel.add(responseNetworkButton);
        panel.add(responseHeadersButton);
        panel.add(responseBodyButton);
        panel.add(responseCodeButton);
        panel.add(responseMessageButton);

        responseNetworkButton.setSelected(true);

        return panel;
    }

    private Box createComparatorButtonPanel() {
        ButtonGroup group = new ButtonGroup();

        equalButton = createComparatorButton("=", SizeAssertion.EQUAL, group); //$NON-NLS-1$
        notequalButton = createComparatorButton("!=", SizeAssertion.NOTEQUAL, group); //$NON-NLS-1$
        greaterthanButton = createComparatorButton(">", SizeAssertion.GREATERTHAN, group); //$NON-NLS-1$
        lessthanButton = createComparatorButton("<", SizeAssertion.LESSTHAN, group); //$NON-NLS-1$
        greaterthanequalButton = createComparatorButton(">=", SizeAssertion.GREATERTHANEQUAL, group); //$NON-NLS-1$
        lessthanequalButton = createComparatorButton("<=", SizeAssertion.LESSTHANEQUAL, group); //$NON-NLS-1$

        equalButton.setSelected(true);
        execState = Integer.parseInt(equalButton.getActionCommand());

        // Put the check boxes in a column in a panel
        Box checkPanel = Box.createVerticalBox();
        JLabel compareLabel = new JLabel(JMeterUtils.getResString("size_assertion_comparator_label")); //$NON-NLS-1$
        checkPanel.add(compareLabel);
        checkPanel.add(equalButton);
        checkPanel.add(notequalButton);
        checkPanel.add(greaterthanButton);
        checkPanel.add(lessthanButton);
        checkPanel.add(greaterthanequalButton);
        checkPanel.add(lessthanequalButton);
        return checkPanel;
    }

    private JRadioButton createComparatorButton(String name, int value, ButtonGroup group) {
        JRadioButton button = new JRadioButton(name);
        button.setActionCommand(String.valueOf(value));
        button.addActionListener(this);
        group.add(button);
        return button;
    }

    public void actionPerformed(ActionEvent e) {
        int comparator = Integer.parseInt(e.getActionCommand());
        execState = comparator;
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

QDox parse error in file://<WORKSPACE>/data/code-rep-dataset/Dataset5/Tasks/15226.java