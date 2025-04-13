error id: file://<WORKSPACE>/data/code-rep-dataset/Dataset5/Tasks/15227.java
file://<WORKSPACE>/data/code-rep-dataset/Dataset5/Tasks/15227.java
### com.thoughtworks.qdox.parser.ParseException: syntax error @[1,1]

error in qdox parser
file content:
```java
offset: 1
uri: file://<WORKSPACE>/data/code-rep-dataset/Dataset5/Tasks/15227.java
text:
```scala
s@@howScopeSettings(re, true);

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

package org.apache.jmeter.extractor.gui;

import java.awt.BorderLayout;
import java.awt.GridBagConstraints;
import java.awt.GridBagLayout;
import java.util.List;

import javax.swing.BorderFactory;
import javax.swing.Box;
import javax.swing.ButtonGroup;
import javax.swing.JComponent;
import javax.swing.JPanel;
import javax.swing.JRadioButton;

import org.apache.jmeter.extractor.RegexExtractor;
import org.apache.jmeter.processor.gui.AbstractPostProcessorGui;
import org.apache.jmeter.testelement.AbstractScopedTestElement;
import org.apache.jmeter.testelement.TestElement;
import org.apache.jmeter.util.JMeterUtils;
import org.apache.jorphan.gui.JLabeledTextField;

/**
 * Regular Expression Extractor Post-Processor GUI
 */
public class RegexExtractorGui extends AbstractPostProcessorGui {
    private static final long serialVersionUID = 240L;

    private JLabeledTextField regexField;

    private JLabeledTextField templateField;

    private JLabeledTextField defaultField;

    private JLabeledTextField matchNumberField;

    private JLabeledTextField refNameField;

    private JRadioButton useBody;

    private JRadioButton useUnescapedBody;

    private JRadioButton useHeaders;

    private JRadioButton useURL;

    private JRadioButton useCode;

    private JRadioButton useMessage;

    private ButtonGroup group;

    public RegexExtractorGui() {
        super();
        init();
    }

    public String getLabelResource() {
        return "regex_extractor_title"; //$NON-NLS-1$
    }

    @Override
    public void configure(TestElement el) {
        super.configure(el);
        if (el instanceof RegexExtractor){
            RegexExtractor re = (RegexExtractor) el;
            showScopeSettings(re);
            useHeaders.setSelected(re.useHeaders());
            useBody.setSelected(re.useBody());
            useUnescapedBody.setSelected(re.useUnescapedBody());
            useURL.setSelected(re.useUrl());
            useCode.setSelected(re.useCode());
            useMessage.setSelected(re.useMessage());
            regexField.setText(re.getRegex());
            templateField.setText(re.getTemplate());
            defaultField.setText(re.getDefaultValue());
            matchNumberField.setText(re.getMatchNumberAsString());
            refNameField.setText(re.getRefName());
        }
    }

    /**
     * @see org.apache.jmeter.gui.JMeterGUIComponent#createTestElement()
     */
    public TestElement createTestElement() {
        AbstractScopedTestElement extractor = new RegexExtractor();
        modifyTestElement(extractor);
        return extractor;
    }

    /**
     * Modifies a given TestElement to mirror the data in the gui components.
     *
     * @see org.apache.jmeter.gui.JMeterGUIComponent#modifyTestElement(TestElement)
     */
    public void modifyTestElement(TestElement extractor) {
        super.configureTestElement(extractor);
        if (extractor instanceof RegexExtractor) {
            RegexExtractor regex = (RegexExtractor) extractor;
            saveScopeSettings(regex);
            regex.setUseField(group.getSelection().getActionCommand());
            regex.setRefName(refNameField.getText());
            regex.setRegex(regexField.getText());
            regex.setTemplate(templateField.getText());
            regex.setDefaultValue(defaultField.getText());
            regex.setMatchNumber(matchNumberField.getText());
        }
    }

    /**
     * Implements JMeterGUIComponent.clearGui
     */
    @Override
    public void clearGui() {
        super.clearGui();

        useBody.setSelected(true);

        regexField.setText(""); //$NON-NLS-1$
        templateField.setText(""); //$NON-NLS-1$
        defaultField.setText(""); //$NON-NLS-1$
        refNameField.setText(""); //$NON-NLS-1$
        matchNumberField.setText(""); //$NON-NLS-1$
    }

    private void init() {
        setLayout(new BorderLayout());
        setBorder(makeBorder());

        Box box = Box.createVerticalBox();
        box.add(makeTitlePanel());
        box.add(createScopePanel(true));
        box.add(makeSourcePanel());
        add(box, BorderLayout.NORTH);
        add(makeParameterPanel(), BorderLayout.CENTER);
    }

    private JPanel makeSourcePanel() {
        JPanel panel = new JPanel();
        panel.setBorder(BorderFactory.createTitledBorder(JMeterUtils.getResString("regex_source"))); //$NON-NLS-1$

        useBody = new JRadioButton(JMeterUtils.getResString("regex_src_body")); //$NON-NLS-1$
        useUnescapedBody = new JRadioButton(JMeterUtils.getResString("regex_src_body_unescaped")); //$NON-NLS-1$
        useHeaders = new JRadioButton(JMeterUtils.getResString("regex_src_hdrs")); //$NON-NLS-1$
        useURL = new JRadioButton(JMeterUtils.getResString("regex_src_url")); //$NON-NLS-1$
        useCode = new JRadioButton(JMeterUtils.getResString("assertion_code_resp")); //$NON-NLS-1$
        useMessage = new JRadioButton(JMeterUtils.getResString("assertion_message_resp")); //$NON-NLS-1$

        group = new ButtonGroup();
        group.add(useBody);
        group.add(useUnescapedBody);
        group.add(useHeaders);
        group.add(useURL);
        group.add(useCode);
        group.add(useMessage);

        panel.add(useBody);
        panel.add(useUnescapedBody);
        panel.add(useHeaders);
        panel.add(useURL);
        panel.add(useCode);
        panel.add(useMessage);

        useBody.setSelected(true);

        // So we know which button is selected
        useBody.setActionCommand(RegexExtractor.USE_BODY);
        useUnescapedBody.setActionCommand(RegexExtractor.USE_BODY_UNESCAPED);
        useHeaders.setActionCommand(RegexExtractor.USE_HDRS);
        useURL.setActionCommand(RegexExtractor.USE_URL);
        useCode.setActionCommand(RegexExtractor.USE_CODE);
        useMessage.setActionCommand(RegexExtractor.USE_MESSAGE);

        return panel;
    }

    private JPanel makeParameterPanel() {
        regexField = new JLabeledTextField(JMeterUtils.getResString("regex_field")); //$NON-NLS-1$
        templateField = new JLabeledTextField(JMeterUtils.getResString("template_field")); //$NON-NLS-1$
        defaultField = new JLabeledTextField(JMeterUtils.getResString("default_value_field")); //$NON-NLS-1$
        refNameField = new JLabeledTextField(JMeterUtils.getResString("ref_name_field")); //$NON-NLS-1$
        matchNumberField = new JLabeledTextField(JMeterUtils.getResString("match_num_field")); //$NON-NLS-1$

        JPanel panel = new JPanel(new GridBagLayout());
        GridBagConstraints gbc = new GridBagConstraints();
        initConstraints(gbc);
        addField(panel, refNameField, gbc);
        resetContraints(gbc);
        addField(panel, regexField, gbc);
        resetContraints(gbc);
        addField(panel, templateField, gbc);
        resetContraints(gbc);
        addField(panel, matchNumberField, gbc);
        resetContraints(gbc);
        gbc.weighty = 1;
        addField(panel, defaultField, gbc);
        return panel;
    }

    private void addField(JPanel panel, JLabeledTextField field, GridBagConstraints gbc) {
        List<JComponent> item = field.getComponentList();
        panel.add(item.get(0), gbc.clone());
        gbc.gridx++;
        gbc.weightx = 1;
        gbc.fill=GridBagConstraints.HORIZONTAL;
        panel.add(item.get(1), gbc.clone());
    }

    // Next line
    private void resetContraints(GridBagConstraints gbc) {
        gbc.gridx = 0;
        gbc.gridy++;
        gbc.weightx = 0;
        gbc.fill=GridBagConstraints.NONE;
    }

    private void initConstraints(GridBagConstraints gbc) {
        gbc.anchor = GridBagConstraints.NORTHWEST;
        gbc.fill = GridBagConstraints.NONE;
        gbc.gridheight = 1;
        gbc.gridwidth = 1;
        gbc.gridx = 0;
        gbc.gridy = 0;
        gbc.weightx = 0;
        gbc.weighty = 0;
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

QDox parse error in file://<WORKSPACE>/data/code-rep-dataset/Dataset5/Tasks/15227.java