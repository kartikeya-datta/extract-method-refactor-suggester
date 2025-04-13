error id: file://<WORKSPACE>/data/code-rep-dataset/Dataset5/Tasks/10150.java
file://<WORKSPACE>/data/code-rep-dataset/Dataset5/Tasks/10150.java
### com.thoughtworks.qdox.parser.ParseException: syntax error @[220,2]

error in qdox parser
file content:
```java
offset: 8111
uri: file://<WORKSPACE>/data/code-rep-dataset/Dataset5/Tasks/10150.java
text:
```scala
import org.apache.commons.lang3.StringUtils;

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

package org.apache.jmeter.protocol.system.gui;

import java.awt.BorderLayout;
import java.awt.event.ItemEvent;
import java.awt.event.ItemListener;

import javax.swing.BorderFactory;
import javax.swing.Box;
import javax.swing.BoxLayout;
import javax.swing.JCheckBox;
import javax.swing.JPanel;

import org.apache.commons.lang.StringUtils;
import org.apache.jmeter.config.Argument;
import org.apache.jmeter.config.Arguments;
import org.apache.jmeter.config.gui.ArgumentsPanel;
import org.apache.jmeter.gui.util.VerticalPanel;
import org.apache.jmeter.protocol.system.SystemSampler;
import org.apache.jmeter.samplers.gui.AbstractSamplerGui;
import org.apache.jmeter.testelement.TestElement;
import org.apache.jmeter.util.JMeterUtils;
import org.apache.jorphan.gui.JLabeledTextField;
import org.apache.jorphan.gui.ObjectTableModel;
import org.apache.jorphan.reflect.Functor;

/**
 * GUI for {@link SystemSampler}
 */
public class SystemSamplerGui extends AbstractSamplerGui implements ItemListener {

    /**
     * 
     */
    private static final long serialVersionUID = -2413845772703695934L;
    
    private JCheckBox checkReturnCode;
    private JLabeledTextField desiredReturnCode;
    private JLabeledTextField directory;
    private JLabeledTextField command;
    private ArgumentsPanel argsPanel;
    private ArgumentsPanel envPanel;
    
    /**
     * Constructor for JavaTestSamplerGui
     */
    public SystemSamplerGui() {
        super();
        init();
    }

    public String getLabelResource() {
        return "system_sampler_title";
    }

    @Override
    public String getStaticLabel() {
        return JMeterUtils.getResString(getLabelResource());
    }

    /**
     * Initialize the GUI components and layout.
     */
    private void init() {
        setLayout(new BorderLayout());
        setBorder(makeBorder());

        add(makeTitlePanel(), BorderLayout.NORTH);
       
        JPanel panelb = new VerticalPanel();
        panelb.add(makeReturnCodePanel());
        panelb.add(Box.createVerticalStrut(5));
        panelb.add(makeCommandPanel(), BorderLayout.CENTER);
        
        add(panelb, BorderLayout.CENTER);
    }

    /* Implements JMeterGuiComponent.createTestElement() */
    public TestElement createTestElement() {
        SystemSampler sampler = new SystemSampler();
        modifyTestElement(sampler);
        return sampler;
    }

    public void modifyTestElement(TestElement sampler) {
        super.configureTestElement(sampler);
        SystemSampler systemSampler = (SystemSampler)sampler;
        systemSampler.setCheckReturnCode(checkReturnCode.isSelected());
        if(checkReturnCode.isSelected()) {
            if(!StringUtils.isEmpty(desiredReturnCode.getText())) {
                systemSampler.setExpectedReturnCode(Integer.parseInt(desiredReturnCode.getText()));
            } else {
                systemSampler.setExpectedReturnCode(SystemSampler.DEFAULT_RETURN_CODE);
            }
        } else {
            systemSampler.setExpectedReturnCode(SystemSampler.DEFAULT_RETURN_CODE);
        }
        systemSampler.setCommand(command.getText());
        systemSampler.setArguments((Arguments)argsPanel.createTestElement());
        systemSampler.setEnvironmentVariables((Arguments)envPanel.createTestElement());
        systemSampler.setDirectory(directory.getText());
    }

    /* Overrides AbstractJMeterGuiComponent.configure(TestElement) */
    @Override
    public void configure(TestElement el) {
        super.configure(el);
        SystemSampler systemSampler = (SystemSampler) el;
        checkReturnCode.setSelected(systemSampler.getCheckReturnCode());
        desiredReturnCode.setText(Integer.toString(systemSampler.getExpectedReturnCode()));
        desiredReturnCode.setEnabled(checkReturnCode.isSelected());
        command.setText(systemSampler.getCommand());
        argsPanel.configure(systemSampler.getArguments());
        envPanel.configure(systemSampler.getEnvironmentVariables());
        directory.setText(systemSampler.getDirectory());
    }

    /**
     * @return JPanel return code config
     */
    private JPanel makeReturnCodePanel() {
        JPanel panel = new JPanel();
        panel.setLayout(new BoxLayout(panel, BoxLayout.X_AXIS));
        panel.setBorder(BorderFactory.createTitledBorder(JMeterUtils.getResString("return_code_config_box_title")));
        checkReturnCode = new JCheckBox(JMeterUtils.getResString("check_return_code_title"));
        checkReturnCode.addItemListener(this);
        desiredReturnCode = new JLabeledTextField(JMeterUtils.getResString("expected_return_code_title"));
        desiredReturnCode.setSize(desiredReturnCode.getSize().height, 30);
        panel.add(checkReturnCode);
        panel.add(Box.createHorizontalStrut(5));
        panel.add(desiredReturnCode);
        checkReturnCode.setSelected(true);
        return panel;
    }
    
    /**
     * @return JPanel Command + directory
     */
    private JPanel makeCommandPanel() {
        JPanel panel = new JPanel(new BorderLayout());
        panel.setBorder(BorderFactory.createTitledBorder(JMeterUtils.getResString("command_config_box_title")));
        
        JPanel cmdPanel = new JPanel();
        cmdPanel.setLayout(new BoxLayout(cmdPanel, BoxLayout.X_AXIS));
        
        directory = new JLabeledTextField(JMeterUtils.getResString("directory_field_title"));
        cmdPanel.add(directory);
        cmdPanel.add(Box.createHorizontalStrut(5));
        command = new JLabeledTextField(JMeterUtils.getResString("command_field_title"));
        cmdPanel.add(command);
        panel.add(cmdPanel, BorderLayout.NORTH);
        panel.add(makeArgumentsPanel(), BorderLayout.CENTER);
        panel.add(makeEnvironmentPanel(), BorderLayout.SOUTH);
        return panel;
    }
    
    /**
     * @return JPanel Arguments Panel
     */
    private JPanel makeArgumentsPanel() {
        argsPanel = new ArgumentsPanel(JMeterUtils.getResString("arguments_panel_title"), null, true, false , 
                new ObjectTableModel(new String[] { ArgumentsPanel.COLUMN_RESOURCE_NAMES_1 },
                        Argument.class,
                        new Functor[] {
                        new Functor("getValue") },  // $NON-NLS-1$
                        new Functor[] {
                        new Functor("setValue") }, // $NON-NLS-1$
                        new Class[] {String.class }));
        return argsPanel;
    }
    
    /**
     * @return JPanel Environment Panel
     */
    private JPanel makeEnvironmentPanel() {
        envPanel = new ArgumentsPanel(JMeterUtils.getResString("environment_panel_title"));
        return envPanel;
    }

    /**
     * @see org.apache.jmeter.gui.AbstractJMeterGuiComponent#clearGui()
     */
    @Override
    public void clearGui() {
        super.clearGui();
        directory.setText("");
        command.setText("");
        argsPanel.clearGui();
        envPanel.clearGui();
        desiredReturnCode.setText("");
        checkReturnCode.setSelected(false);
        desiredReturnCode.setEnabled(false);
    }

    public void itemStateChanged(ItemEvent e) {
        if(e.getSource()==checkReturnCode) {
            desiredReturnCode.setEnabled(e.getStateChange() == ItemEvent.SELECTED);
        }
    }
}
 N@@o newline at end of file
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

QDox parse error in file://<WORKSPACE>/data/code-rep-dataset/Dataset5/Tasks/10150.java