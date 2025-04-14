error id: file://<WORKSPACE>/data/code-rep-dataset/Dataset5/Tasks/14893.java
file://<WORKSPACE>/data/code-rep-dataset/Dataset5/Tasks/14893.java
### com.thoughtworks.qdox.parser.ParseException: syntax error @[1,1]

error in qdox parser
file content:
```java
offset: 1
uri: file://<WORKSPACE>/data/code-rep-dataset/Dataset5/Tasks/14893.java
text:
```scala
l@@og.error("Error setting class:'"+className+"' in JavaSampler "+getName()+", check for a missing jar in your jmeter 'search_paths' and 'plugin_dependency_paths' properties");

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

package org.apache.jmeter.protocol.java.config.gui;

import java.awt.BorderLayout;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;

import javax.swing.ComboBoxModel;
import javax.swing.JComboBox;
import javax.swing.JLabel;
import javax.swing.JPanel;

import org.apache.jmeter.config.Argument;
import org.apache.jmeter.config.Arguments;
import org.apache.jmeter.config.gui.AbstractConfigGui;
import org.apache.jmeter.config.gui.ArgumentsPanel;
import org.apache.jmeter.gui.util.HorizontalPanel;
import org.apache.jmeter.protocol.java.config.JavaConfig;
import org.apache.jmeter.protocol.java.sampler.JavaSampler;
import org.apache.jmeter.protocol.java.sampler.JavaSamplerClient;
import org.apache.jmeter.testelement.TestElement;
import org.apache.jmeter.testelement.property.PropertyIterator;
import org.apache.jmeter.util.JMeterUtils;
import org.apache.jorphan.reflect.ClassFinder;
import org.apache.jorphan.logging.LoggingManager;
import org.apache.log.Logger;

/**
 * The <code>JavaConfigGui</code> class provides the user interface for the
 * {@link JavaConfig} object.
 *
 */
public class JavaConfigGui extends AbstractConfigGui implements ActionListener {
    private static final long serialVersionUID = 240L;

    /** Logging */
    private static final Logger log = LoggingManager.getLoggerForClass();

    /** A combo box allowing the user to choose a test class. */
    private JComboBox classnameCombo;

    /**
     * Indicates whether or not the name of this component should be displayed
     * as part of the GUI. If true, this is a standalone component. If false, it
     * is embedded in some other component.
     */
    private boolean displayName = true;

    /** A panel allowing the user to set arguments for this test. */
    private ArgumentsPanel argsPanel;

    /**
     * Create a new JavaConfigGui as a standalone component.
     */
    public JavaConfigGui() {
        this(true);
    }

    /**
     * Create a new JavaConfigGui as either a standalone or an embedded
     * component.
     *
     * @param displayNameField
     *            tells whether the component name should be displayed with the
     *            GUI. If true, this is a standalone component. If false, this
     *            component is embedded in some other component.
     */
    public JavaConfigGui(boolean displayNameField) {
        this.displayName = displayNameField;
        init();
    }

    /** {@inheritDoc} */
    @Override
    public String getLabelResource() {
        return "java_request_defaults"; // $NON-NLS-1$
    }

    /**
     * Initialize the GUI components and layout.
     */
    private void init() {// called from ctor, so must not be overridable
        setLayout(new BorderLayout(0, 5));

        if (displayName) {
            setBorder(makeBorder());
            add(makeTitlePanel(), BorderLayout.NORTH);
        }

        JPanel classnameRequestPanel = new JPanel(new BorderLayout(0, 5));
        classnameRequestPanel.add(createClassnamePanel(), BorderLayout.NORTH);
        classnameRequestPanel.add(createParameterPanel(), BorderLayout.CENTER);

        add(classnameRequestPanel, BorderLayout.CENTER);
    }

    /**
     * Create a panel with GUI components allowing the user to select a test
     * class.
     *
     * @return a panel containing the relevant components
     */
    private JPanel createClassnamePanel() {
        List<String> possibleClasses = new ArrayList<String>();

        try {
            // Find all the classes which implement the JavaSamplerClient
            // interface.
            possibleClasses = ClassFinder.findClassesThatExtend(JMeterUtils.getSearchPaths(),
                    new Class[] { JavaSamplerClient.class });

            // Remove the JavaConfig class from the list since it only
            // implements the interface for error conditions.

            possibleClasses.remove(JavaSampler.class.getName() + "$ErrorSamplerClient");
        } catch (Exception e) {
            log.debug("Exception getting interfaces.", e);
        }

        JLabel label = new JLabel(JMeterUtils.getResString("protocol_java_classname")); // $NON-NLS-1$

        classnameCombo = new JComboBox(possibleClasses.toArray());
        classnameCombo.addActionListener(this);
        classnameCombo.setEditable(false);
        label.setLabelFor(classnameCombo);

        HorizontalPanel panel = new HorizontalPanel();
        panel.add(label);
        panel.add(classnameCombo);

        return panel;
    }

    /**
     * Handle action events for this component. This method currently handles
     * events for the classname combo box.
     *
     * @param evt
     *            the ActionEvent to be handled
     */
    @Override
    public void actionPerformed(ActionEvent evt) {
        if (evt.getSource() == classnameCombo) {
            String className = ((String) classnameCombo.getSelectedItem()).trim();
            try {
                JavaSamplerClient client = (JavaSamplerClient) Class.forName(className, true,
                        Thread.currentThread().getContextClassLoader()).newInstance();

                Arguments currArgs = new Arguments();
                argsPanel.modifyTestElement(currArgs);
                Map<String, String> currArgsMap = currArgs.getArgumentsAsMap();

                Arguments newArgs = new Arguments();
                Arguments testParams = null;
                try {
                    testParams = client.getDefaultParameters();
                } catch (AbstractMethodError e) {
                    log.warn("JavaSamplerClient doesn't implement "
                            + "getDefaultParameters.  Default parameters won't "
                            + "be shown.  Please update your client class: " + className);
                }

                if (testParams != null) {
                    PropertyIterator i = testParams.getArguments().iterator();
                    while (i.hasNext()) {
                        Argument arg = (Argument) i.next().getObjectValue();
                        String name = arg.getName();
                        String value = arg.getValue();

                        // If a user has set parameters in one test, and then
                        // selects a different test which supports the same
                        // parameters, those parameters should have the same
                        // values that they did in the original test.
                        if (currArgsMap.containsKey(name)) {
                            String newVal = currArgsMap.get(name);
                            if (newVal != null && newVal.length() > 0) {
                                value = newVal;
                            }
                        }
                        newArgs.addArgument(name, value);
                    }
                }

                argsPanel.configure(newArgs);
            } catch (Exception e) {
                log.error("Error getting argument list for " + className, e);
            }
        }
    }

    /**
     * Create a panel containing components allowing the user to provide
     * arguments to be passed to the test class instance.
     *
     * @return a panel containing the relevant components
     */
    private JPanel createParameterPanel() {
        argsPanel = new ArgumentsPanel(JMeterUtils.getResString("paramtable")); // $NON-NLS-1$
        return argsPanel;
    }

    /** {@inheritDoc} */
    @Override
    public void configure(TestElement config) {
        super.configure(config);

        argsPanel.configure((Arguments) config.getProperty(JavaSampler.ARGUMENTS).getObjectValue());

        String className = config.getPropertyAsString(JavaSampler.CLASSNAME);
        if(checkContainsClassName(classnameCombo.getModel(), className)) {
            classnameCombo.setSelectedItem(className);
        } else {
            log.error("Error setting class:'"+className+"' in JavaSampler "+getName()+", check for a missing jar in your jmeter classpath");
        }
    }

    /**
     * Check combo contains className
     * @param model ComboBoxModel
     * @param className String class name
     * @return boolean
     */
    private static final boolean checkContainsClassName(ComboBoxModel model, String className) {
        int size = model.getSize();
        Set<String> set = new HashSet<String>(size);
        for (int i = 0; i < size; i++) {
            set.add((String)model.getElementAt(i));
        }
        return set.contains(className);
    }

    /** {@inheritDoc} */
    @Override
    public TestElement createTestElement() {
        JavaConfig config = new JavaConfig();
        modifyTestElement(config);
        return config;
    }

    /** {@inheritDoc} */
    @Override
    public void modifyTestElement(TestElement config) {
        configureTestElement(config);
        ((JavaConfig) config).setArguments((Arguments) argsPanel.createTestElement());
        ((JavaConfig) config).setClassname(String.valueOf(classnameCombo.getSelectedItem()));
    }

    /* (non-Javadoc)
     * @see org.apache.jmeter.gui.AbstractJMeterGuiComponent#clearGui()
     */
    @Override
    public void clearGui() {
        super.clearGui();
        this.displayName = true;
        argsPanel.clearGui();
        classnameCombo.setSelectedIndex(0);
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

QDox parse error in file://<WORKSPACE>/data/code-rep-dataset/Dataset5/Tasks/14893.java