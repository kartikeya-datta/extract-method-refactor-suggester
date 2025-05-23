error id: file://<WORKSPACE>/data/code-rep-dataset/Dataset5/Tasks/12835.java
file://<WORKSPACE>/data/code-rep-dataset/Dataset5/Tasks/12835.java
### com.thoughtworks.qdox.parser.ParseException: syntax error @[1,15]

error in qdox parser
file content:
```java
offset: 15
uri: file://<WORKSPACE>/data/code-rep-dataset/Dataset5/Tasks/12835.java
text:
```scala
private final J@@LabeledTextArea textMessage = new JLabeledTextArea(JMeterUtils.getResString("jms_text_area")); // $NON-NLS-1$

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

package org.apache.jmeter.protocol.jms.control.gui;

import java.awt.BorderLayout;
import java.awt.Dimension;

import javax.swing.Box;
import javax.swing.BoxLayout;
import javax.swing.JCheckBox;
import javax.swing.JPanel;
import javax.swing.event.ChangeEvent;
import javax.swing.event.ChangeListener;

import org.apache.jmeter.config.Arguments;
import org.apache.jmeter.config.gui.ArgumentsPanel;
import org.apache.jmeter.gui.util.FilePanel;
import org.apache.jmeter.gui.util.JLabeledRadioI18N;
import org.apache.jmeter.gui.util.VerticalPanel;
import org.apache.jmeter.protocol.jms.sampler.PublisherSampler;
import org.apache.jmeter.samplers.gui.AbstractSamplerGui;
import org.apache.jmeter.testelement.TestElement;
import org.apache.jmeter.util.JMeterUtils;
import org.apache.jorphan.gui.JLabeledPasswordField;
import org.apache.jorphan.gui.JLabeledTextArea;
import org.apache.jorphan.gui.JLabeledTextField;

/**
 * This is the GUI for JMS Publisher
 *
 */
public class JMSPublisherGui extends AbstractSamplerGui implements ChangeListener {

    private static final long serialVersionUID = 240L;

    private static final String ALL_FILES = "*.*"; //$NON-NLS-1$

    //++ These names are used in the JMX files, and must not be changed
    /** Take source from the named file */
    public static final String USE_FILE_RSC   = "jms_use_file"; //$NON-NLS-1$
    /** Take source from a random file */
    public static final String USE_RANDOM_RSC = "jms_use_random_file"; //$NON-NLS-1$
    /** Take source from the text area */
    private static final String USE_TEXT_RSC   = "jms_use_text"; //$NON-NLS-1$

    /** Create a TextMessage */
    public static final String TEXT_MSG_RSC = "jms_text_message"; //$NON-NLS-1$
    /** Create a MapMessage */
    public static final String MAP_MSG_RSC = "jms_map_message"; //$NON-NLS-1$
    /** Create an ObjectMessage */
    public static final String OBJECT_MSG_RSC = "jms_object_message"; //$NON-NLS-1$
    /** Create a BytesMessage */
    public static final String BYTES_MSG_RSC = "jms_bytes_message"; //$NON-NLS-1$
    //-- End of names used in JMX files

    // Button group resources when Bytes Message is selected
    private static final String[] CONFIG_ITEMS_BYTES_MSG = { USE_FILE_RSC, USE_RANDOM_RSC};

    // Button group resources
    private static final String[] CONFIG_ITEMS = { USE_FILE_RSC, USE_RANDOM_RSC, USE_TEXT_RSC };

    private static final String[] MSGTYPES_ITEMS = { TEXT_MSG_RSC, MAP_MSG_RSC, OBJECT_MSG_RSC, BYTES_MSG_RSC };

    private final JCheckBox useProperties = new JCheckBox(JMeterUtils.getResString("jms_use_properties_file"), false); //$NON-NLS-1$

    private final JLabeledRadioI18N configChoice = new JLabeledRadioI18N("jms_config", CONFIG_ITEMS, USE_TEXT_RSC); //$NON-NLS-1$

    private final JLabeledTextField jndiICF = new JLabeledTextField(JMeterUtils.getResString("jms_initial_context_factory")); //$NON-NLS-1$

    private final JLabeledTextField urlField = new JLabeledTextField(JMeterUtils.getResString("jms_provider_url")); //$NON-NLS-1$

    private final JLabeledTextField jndiConnFac = new JLabeledTextField(JMeterUtils.getResString("jms_connection_factory")); //$NON-NLS-1$

    private final JLabeledTextField jmsDestination = new JLabeledTextField(JMeterUtils.getResString("jms_topic")); //$NON-NLS-1$

    private final JCheckBox useAuth = new JCheckBox(JMeterUtils.getResString("jms_use_auth"), false); //$NON-NLS-1$

    private final JLabeledTextField jmsUser = new JLabeledTextField(JMeterUtils.getResString("jms_user")); //$NON-NLS-1$

    private final JLabeledTextField jmsPwd = new JLabeledPasswordField(JMeterUtils.getResString("jms_pwd")); //$NON-NLS-1$

    private final JLabeledTextField iterations = new JLabeledTextField(JMeterUtils.getResString("jms_itertions")); //$NON-NLS-1$

    private final FilePanel messageFile = new FilePanel(JMeterUtils.getResString("jms_file"), ALL_FILES); //$NON-NLS-1$

    private final FilePanel randomFile = new FilePanel(JMeterUtils.getResString("jms_random_file"), ALL_FILES); //$NON-NLS-1$

    private final JLabeledTextArea textMessage = new JLabeledTextArea(JMeterUtils.getResString("jms_text_area"));

    private final JLabeledRadioI18N msgChoice = new JLabeledRadioI18N("jms_message_type", MSGTYPES_ITEMS, TEXT_MSG_RSC); //$NON-NLS-1$
    
    private JCheckBox useNonPersistentDelivery;

    // These are the names of properties used to define the labels
    private static final String DEST_SETUP_STATIC = "jms_dest_setup_static"; // $NON-NLS-1$

    private static final String DEST_SETUP_DYNAMIC = "jms_dest_setup_dynamic"; // $NON-NLS-1$
    // Button group resources
    private static final String[] DEST_SETUP_ITEMS = { DEST_SETUP_STATIC, DEST_SETUP_DYNAMIC };

    private final JLabeledRadioI18N destSetup =
        new JLabeledRadioI18N("jms_dest_setup", DEST_SETUP_ITEMS, DEST_SETUP_STATIC); // $NON-NLS-1$

    private ArgumentsPanel jmsPropertiesPanel;

    public JMSPublisherGui() {
        init();
    }

    /**
     * the name of the property for the JMSPublisherGui is jms_publisher.
     */
    @Override
    public String getLabelResource() {
        return "jms_publisher"; //$NON-NLS-1$
    }

    /**
     * @see org.apache.jmeter.gui.JMeterGUIComponent#createTestElement()
     */
    @Override
    public TestElement createTestElement() {
      PublisherSampler sampler = new PublisherSampler();
      setupSamplerProperties(sampler);

      return sampler;
  }
    /**
     * Modifies a given TestElement to mirror the data in the gui components.
     *
     * @see org.apache.jmeter.gui.JMeterGUIComponent#modifyTestElement(TestElement)
     */
    @Override
    public void modifyTestElement(TestElement s) {
        PublisherSampler sampler = (PublisherSampler) s;
        setupSamplerProperties(sampler);
        sampler.setDestinationStatic(destSetup.getText().equals(DEST_SETUP_STATIC));
    }

    /**
     * Initialize the provided {@link PublisherSampler} with all the values as configured in the GUI.
     * 
     * @param sampler {@link PublisherSampler} instance
     */
    private void setupSamplerProperties(final PublisherSampler sampler) {
      this.configureTestElement(sampler);
      sampler.setUseJNDIProperties(String.valueOf(useProperties.isSelected()));
      sampler.setJNDIIntialContextFactory(jndiICF.getText());
      sampler.setProviderUrl(urlField.getText());
      sampler.setConnectionFactory(jndiConnFac.getText());
      sampler.setDestination(jmsDestination.getText());
      sampler.setUsername(jmsUser.getText());
      sampler.setPassword(jmsPwd.getText());
      sampler.setTextMessage(textMessage.getText());
      sampler.setInputFile(messageFile.getFilename());
      sampler.setRandomPath(randomFile.getFilename());
      sampler.setConfigChoice(configChoice.getText());
      sampler.setMessageChoice(msgChoice.getText());
      sampler.setIterations(iterations.getText());
      sampler.setUseAuth(useAuth.isSelected());
      sampler.setUseNonPersistentDelivery(useNonPersistentDelivery.isSelected());
     
      Arguments args = (Arguments) jmsPropertiesPanel.createTestElement();
      sampler.setJMSProperties(args);
    }

    /**
     * init() adds jndiICF to the mainPanel. The class reuses logic from
     * SOAPSampler, since it is common.
     */
    private void init() {
        setLayout(new BorderLayout());
        setBorder(makeBorder());
        add(makeTitlePanel(), BorderLayout.NORTH);

        JPanel mainPanel = new VerticalPanel();
        add(mainPanel, BorderLayout.CENTER);
        
        mainPanel.add(useProperties);
        mainPanel.add(jndiICF);
        mainPanel.add(urlField);
        mainPanel.add(jndiConnFac);
        mainPanel.add(createDestinationPane());
        mainPanel.add(createAuthPane());
        mainPanel.add(iterations);

        jmsPropertiesPanel = new ArgumentsPanel(JMeterUtils.getResString("jms_props")); //$NON-NLS-1$
        mainPanel.add(jmsPropertiesPanel);

        configChoice.setLayout(new BoxLayout(configChoice, BoxLayout.X_AXIS));
        mainPanel.add(configChoice);
        msgChoice.setLayout(new BoxLayout(msgChoice, BoxLayout.X_AXIS));
        mainPanel.add(msgChoice);
        mainPanel.add(messageFile);
        mainPanel.add(randomFile);
        mainPanel.add(textMessage);
        Dimension pref = new Dimension(400, 150);
        textMessage.setPreferredSize(pref);

        useProperties.addChangeListener(this);
        useAuth.addChangeListener(this);
        configChoice.addChangeListener(this);
        msgChoice.addChangeListener(this);
    }

    @Override
    public void clearGui(){
        super.clearGui();
        useProperties.setSelected(false);
        jndiICF.setText(""); // $NON-NLS-1$
        urlField.setText(""); // $NON-NLS-1$
        jndiConnFac.setText(""); // $NON-NLS-1$
        jmsDestination.setText(""); // $NON-NLS-1$
        jmsUser.setText(""); // $NON-NLS-1$
        jmsPwd.setText(""); // $NON-NLS-1$
        textMessage.setText(""); // $NON-NLS-1$
        messageFile.setFilename(""); // $NON-NLS-1$
        randomFile.setFilename(""); // $NON-NLS-1$
        msgChoice.setText(""); // $NON-NLS-1$
        configChoice.setText(USE_TEXT_RSC);
        updateConfig(USE_TEXT_RSC);
        msgChoice.setText(TEXT_MSG_RSC);
        iterations.setText("1"); // $NON-NLS-1$
        useAuth.setSelected(false);
        jmsUser.setEnabled(false);
        jmsPwd.setEnabled(false);
        destSetup.setText(DEST_SETUP_STATIC);
        useNonPersistentDelivery.setSelected(false);
        jmsPropertiesPanel.clear();
    }

    /**
     * the implementation loads the URL and the soap action for the request.
     */
    @Override
    public void configure(TestElement el) {
        super.configure(el);
        PublisherSampler sampler = (PublisherSampler) el;
        useProperties.setSelected(sampler.getUseJNDIPropertiesAsBoolean());
        jndiICF.setText(sampler.getJNDIInitialContextFactory());
        urlField.setText(sampler.getProviderUrl());
        jndiConnFac.setText(sampler.getConnectionFactory());
        jmsDestination.setText(sampler.getDestination());
        jmsUser.setText(sampler.getUsername());
        jmsPwd.setText(sampler.getPassword());
        textMessage.setText(sampler.getTextMessage());
        messageFile.setFilename(sampler.getInputFile());
        randomFile.setFilename(sampler.getRandomPath());
        configChoice.setText(sampler.getConfigChoice());
        msgChoice.setText(sampler.getMessageChoice());
        updateConfig(sampler.getConfigChoice());
        iterations.setText(sampler.getIterations());
        useAuth.setSelected(sampler.isUseAuth());
        jmsUser.setEnabled(useAuth.isSelected());
        jmsPwd.setEnabled(useAuth.isSelected());
        destSetup.setText(sampler.isDestinationStatic() ? DEST_SETUP_STATIC : DEST_SETUP_DYNAMIC);
        useNonPersistentDelivery.setSelected(sampler.getUseNonPersistentDelivery());
        jmsPropertiesPanel.configure(sampler.getJMSProperties());
        updateChoice(msgChoice.getText());
    }

    /**
     * When a widget state changes, it will notify this class so we can
     * enable/disable the correct items.
     */
    @Override
    public void stateChanged(ChangeEvent event) {
        if (event.getSource() == configChoice) {
            updateConfig(configChoice.getText());
        } else if (event.getSource() == msgChoice) {
            updateChoice(msgChoice.getText());
        } else if (event.getSource() == useProperties) {
            jndiICF.setEnabled(!useProperties.isSelected());
            urlField.setEnabled(!useProperties.isSelected());
        } else if (event.getSource() == useAuth) {
            jmsUser.setEnabled(useAuth.isSelected());
            jmsPwd.setEnabled(useAuth.isSelected());
        }
    }
    /**
     * Update choice contains the actual logic for hiding or showing Textarea if Bytes message
     * is selected
     *
     * @param command
     * @since 2.9
     */
    private void updateChoice(String command) {
        String oldChoice = configChoice.getText();
        if (BYTES_MSG_RSC.equals(command)) {
            String newChoice = USE_TEXT_RSC.equals(oldChoice) ? 
                    USE_FILE_RSC : oldChoice;
            configChoice.resetButtons(CONFIG_ITEMS_BYTES_MSG, newChoice);
            textMessage.setEnabled(false);
        } else {
            configChoice.resetButtons(CONFIG_ITEMS, oldChoice);
            textMessage.setEnabled(true);
        }
        validate();
    }
    /**
     * Update config contains the actual logic for enabling or disabling text
     * message, file or random path.
     *
     * @param command
     */
    private void updateConfig(String command) {
        if (command.equals(USE_TEXT_RSC)) {
            textMessage.setEnabled(true);
            messageFile.enableFile(false);
            randomFile.enableFile(false);
        } else if (command.equals(USE_RANDOM_RSC)) {
            textMessage.setEnabled(false);
            messageFile.enableFile(false);
            randomFile.enableFile(true);
        } else {
            textMessage.setEnabled(false);
            messageFile.enableFile(true);
            randomFile.enableFile(false);
        }
    }
    
    /**
     * @return JPanel that contains destination infos
     */
    private JPanel createDestinationPane() {
        JPanel pane = new JPanel(new BorderLayout(3, 0));
        pane.add(jmsDestination, BorderLayout.WEST);
        destSetup.setLayout(new BoxLayout(destSetup, BoxLayout.X_AXIS));
        pane.add(destSetup, BorderLayout.CENTER);
        useNonPersistentDelivery = new JCheckBox(JMeterUtils.getResString("jms_use_non_persistent_delivery"),false); //$NON-NLS-1$
        pane.add(useNonPersistentDelivery, BorderLayout.EAST);
        return pane;
    }
    
    /**
     * @return JPanel Panel with checkbox to choose auth , user and password
     */
    private JPanel createAuthPane() {
        JPanel pane = new JPanel();
        pane.setLayout(new BoxLayout(pane, BoxLayout.X_AXIS));
        pane.add(useAuth);
        pane.add(Box.createHorizontalStrut(10));
        pane.add(jmsUser);
        pane.add(Box.createHorizontalStrut(10));
        pane.add(jmsPwd);
        return pane;
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

QDox parse error in file://<WORKSPACE>/data/code-rep-dataset/Dataset5/Tasks/12835.java