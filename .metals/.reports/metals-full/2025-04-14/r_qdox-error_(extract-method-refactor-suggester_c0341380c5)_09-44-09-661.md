error id: file://<WORKSPACE>/data/code-rep-dataset/Dataset5/Tasks/13427.java
file://<WORKSPACE>/data/code-rep-dataset/Dataset5/Tasks/13427.java
### com.thoughtworks.qdox.parser.ParseException: syntax error @[1,1]

error in qdox parser
file content:
```java
offset: 1
uri: file://<WORKSPACE>/data/code-rep-dataset/Dataset5/Tasks/13427.java
text:
```scala
J@@Label label2 = new JLabel(JMeterUtils.getResString("html_extractor_type")); // $NON-NLS-1$

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
import java.awt.Component;
import java.awt.GridBagConstraints;
import java.awt.GridBagLayout;
import java.util.List;

import javax.swing.BorderFactory;
import javax.swing.Box;
import javax.swing.DefaultComboBoxModel;
import javax.swing.JComboBox;
import javax.swing.JComponent;
import javax.swing.JLabel;
import javax.swing.JPanel;

import org.apache.jmeter.extractor.HtmlExtractor;
import org.apache.jmeter.gui.util.HorizontalPanel;
import org.apache.jmeter.processor.gui.AbstractPostProcessorGui;
import org.apache.jmeter.testelement.AbstractScopedTestElement;
import org.apache.jmeter.testelement.TestElement;
import org.apache.jmeter.util.JMeterUtils;
import org.apache.jorphan.gui.JLabeledTextField;

/**
 * CSS/JQuery Expression Extractor Post-Processor GUI
 * @since 2.9
 */
public class HtmlExtractorGui extends AbstractPostProcessorGui {
    private static final long serialVersionUID = 240L;

    /**
     * This choice means don't explicitly set Implementation and rely on default
     */
    private static final String USE_DEFAULT_EXTRACTOR_IMPL = ""; // $NON-NLS-1$

    private JLabeledTextField expressionField;

    private JLabeledTextField attributeField;
    
    private JLabeledTextField defaultField;

    private JLabeledTextField matchNumberField;

    private JLabeledTextField refNameField;

    private JComboBox extractorImplName;


    public HtmlExtractorGui() {
        super();
        init();
    }

    @Override
    public String getLabelResource() {
        return "html_extractor_title"; //$NON-NLS-1$
    }

    @Override
    public void configure(TestElement el) {
        super.configure(el);
        if (el instanceof HtmlExtractor){
            HtmlExtractor htmlExtractor = (HtmlExtractor) el;
            showScopeSettings(htmlExtractor, true);
            expressionField.setText(htmlExtractor.getExpression());
            attributeField.setText(htmlExtractor.getAttribute());
            defaultField.setText(htmlExtractor.getDefaultValue());
            matchNumberField.setText(htmlExtractor.getMatchNumberAsString());
            refNameField.setText(htmlExtractor.getRefName());
            extractorImplName.setSelectedItem(htmlExtractor.getExtractor());
        }
    }

    /**
     * @see org.apache.jmeter.gui.JMeterGUIComponent#createTestElement()
     */
    @Override
    public TestElement createTestElement() {
        AbstractScopedTestElement extractor = new HtmlExtractor();
        modifyTestElement(extractor);
        return extractor;
    }

    /**
     * Modifies a given TestElement to mirror the data in the gui components.
     *
     * @see org.apache.jmeter.gui.JMeterGUIComponent#modifyTestElement(TestElement)
     */
    @Override
    public void modifyTestElement(TestElement extractor) {
        super.configureTestElement(extractor);
        if (extractor instanceof HtmlExtractor) {
            HtmlExtractor htmlExtractor = (HtmlExtractor) extractor;
            saveScopeSettings(htmlExtractor);
            htmlExtractor.setRefName(refNameField.getText());
            htmlExtractor.setExpression(expressionField.getText());
            htmlExtractor.setAttribute(attributeField.getText());
            htmlExtractor.setDefaultValue(defaultField.getText());
            htmlExtractor.setMatchNumber(matchNumberField.getText());
            if(extractorImplName.getSelectedIndex()< HtmlExtractor.getImplementations().length) {
                htmlExtractor.setExtractor(HtmlExtractor.getImplementations()[extractorImplName.getSelectedIndex()]);
            } else {
                htmlExtractor.setExtractor(USE_DEFAULT_EXTRACTOR_IMPL);               
            }

        }
    }

    /**
     * Implements JMeterGUIComponent.clearGui
     */
    @Override
    public void clearGui() {
        super.clearGui();
        extractorImplName.setSelectedItem(HtmlExtractor.DEFAULT_EXTRACTOR);
        expressionField.setText(""); //$NON-NLS-1$        
        attributeField.setText(""); //$NON-NLS-1$
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
        box.add(makeExtractorPanel());
        add(box, BorderLayout.NORTH);
        add(makeParameterPanel(), BorderLayout.CENTER);
    }

    

    private Component makeExtractorPanel() {
        JPanel panel = new HorizontalPanel();
        panel.setBorder(BorderFactory.createTitledBorder(JMeterUtils.getResString("html_extractor_type"))); //$NON-NLS-1$
        
        DefaultComboBoxModel m = new DefaultComboBoxModel();
        for (String s : HtmlExtractor.getImplementations()){
            m.addElement(s);
        }
        m.addElement(USE_DEFAULT_EXTRACTOR_IMPL);
        extractorImplName = new JComboBox(m);
        extractorImplName.setSelectedItem(HtmlExtractor.DEFAULT_EXTRACTOR);
        JLabel label2 = new JLabel(JMeterUtils.getResString("extractor_type")); // $NON-NLS-1$
        label2.setLabelFor(extractorImplName);
        panel.add(label2);
        panel.add(extractorImplName);
        return panel;
    }

    private JPanel makeParameterPanel() {        
        expressionField = new JLabeledTextField(JMeterUtils.getResString("expression_field")); //$NON-NLS-1$
        attributeField = new JLabeledTextField(JMeterUtils.getResString("attribute_field")); //$NON-NLS-1$
        defaultField = new JLabeledTextField(JMeterUtils.getResString("default_value_field")); //$NON-NLS-1$
        refNameField = new JLabeledTextField(JMeterUtils.getResString("ref_name_field")); //$NON-NLS-1$
        matchNumberField = new JLabeledTextField(JMeterUtils.getResString("match_num_field")); //$NON-NLS-1$

        JPanel panel = new JPanel(new GridBagLayout());
        GridBagConstraints gbc = new GridBagConstraints();
        initConstraints(gbc);
        addField(panel, refNameField, gbc);
        resetContraints(gbc);
        addField(panel, expressionField, gbc);
        resetContraints(gbc);
        addField(panel, attributeField, gbc);
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

QDox parse error in file://<WORKSPACE>/data/code-rep-dataset/Dataset5/Tasks/13427.java