error id: file://<WORKSPACE>/data/code-rep-dataset/Dataset5/Tasks/548.java
file://<WORKSPACE>/data/code-rep-dataset/Dataset5/Tasks/548.java
### com.thoughtworks.qdox.parser.ParseException: syntax error @[1,1]

error in qdox parser
file content:
```java
offset: 1
uri: file://<WORKSPACE>/data/code-rep-dataset/Dataset5/Tasks/548.java
text:
```scala
i@@terations.setText("1"); // $NON-NLS-1$

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

import javax.naming.Context;
import javax.swing.JCheckBox;
import javax.swing.JPanel;
import javax.swing.event.ChangeEvent;
import javax.swing.event.ChangeListener;

import org.apache.jmeter.gui.util.HorizontalPanel;
import org.apache.jmeter.gui.util.JLabeledRadioI18N;
import org.apache.jmeter.gui.util.VerticalPanel;
import org.apache.jmeter.protocol.jms.sampler.SubscriberSampler;
import org.apache.jmeter.samplers.gui.AbstractSamplerGui;
import org.apache.jmeter.testelement.TestElement;
import org.apache.jmeter.util.JMeterUtils;
import org.apache.jorphan.gui.JLabeledTextField;

/**
 * This is the GUI for JMS Subscriber <br>
 *
 */
public class JMSSubscriberGui extends AbstractSamplerGui implements ChangeListener {

    private static final long serialVersionUID = 240L;

    private final JCheckBox useProperties =
        new JCheckBox(JMeterUtils.getResString("jms_use_properties_file"), false); // $NON-NLS-1$

    private final JLabeledTextField jndiICF =
        new JLabeledTextField(JMeterUtils.getResString("jms_initial_context_factory")); // $NON-NLS-1$

    private final JLabeledTextField urlField =
        new JLabeledTextField(JMeterUtils.getResString("jms_provider_url")); // $NON-NLS-1$

    private final JLabeledTextField jndiConnFac =
        new JLabeledTextField(JMeterUtils.getResString("jms_connection_factory")); // $NON-NLS-1$

    private final JLabeledTextField jmsDestination =
        new JLabeledTextField(JMeterUtils.getResString("jms_topic")); // $NON-NLS-1$

    private final JLabeledTextField jmsUser =
        new JLabeledTextField(JMeterUtils.getResString("jms_user")); // $NON-NLS-1$

    private final JLabeledTextField jmsPwd =
        new JLabeledTextField(JMeterUtils.getResString("jms_pwd")); // $NON-NLS-1$

    private final JLabeledTextField iterations =
        new JLabeledTextField(JMeterUtils.getResString("jms_itertions")); // $NON-NLS-1$

    private final JCheckBox useAuth =
        new JCheckBox(JMeterUtils.getResString("jms_use_auth"), false); //$NON-NLS-1$

    private final JCheckBox readResponse =
        new JCheckBox(JMeterUtils.getResString("jms_read_response"), true); // $NON-NLS-1$

    private final JLabeledTextField timeout = 
        new JLabeledTextField(JMeterUtils.getResString("jms_timeout")); //$NON-NLS-1$

    //++ Do not change these strings; they are used in JMX files to record the button settings
    public final static String RECEIVE_RSC = "jms_subscriber_receive"; // $NON-NLS-1$

    public final static String ON_MESSAGE_RSC = "jms_subscriber_on_message"; // $NON-NLS-1$
    //--

    // Button group resources
    private final static String[] CLIENT_ITEMS = { RECEIVE_RSC, ON_MESSAGE_RSC };

    private final JLabeledRadioI18N clientChoice =
        new JLabeledRadioI18N("jms_client_type", CLIENT_ITEMS, RECEIVE_RSC); // $NON-NLS-1$

    private final JCheckBox stopBetweenSamples =
        new JCheckBox(JMeterUtils.getResString("jms_stop_between_samples"), true); // $NON-NLS-1$
    
    public JMSSubscriberGui() {
        init();
    }

    public String getLabelResource() {
        return "jms_subscriber_title"; // $NON-NLS-1$
    }

    /**
     * @see org.apache.jmeter.gui.JMeterGUIComponent#createTestElement()
     */
    public TestElement createTestElement() {
        SubscriberSampler sampler = new SubscriberSampler();
        modifyTestElement(sampler);
        return sampler;
    }

    /**
     * Modifies a given TestElement to mirror the data in the gui components.
     *
     * @see org.apache.jmeter.gui.JMeterGUIComponent#modifyTestElement(TestElement)
     */
    public void modifyTestElement(TestElement s) {
        SubscriberSampler sampler = (SubscriberSampler) s;
        this.configureTestElement(sampler);
        sampler.setUseJNDIProperties(String.valueOf(useProperties.isSelected()));
        sampler.setJNDIIntialContextFactory(jndiICF.getText());
        sampler.setProviderUrl(urlField.getText());
        sampler.setConnectionFactory(jndiConnFac.getText());
        sampler.setDestination(jmsDestination.getText());
        sampler.setUsername(jmsUser.getText());
        sampler.setPassword(jmsPwd.getText());
        sampler.setUseAuth(useAuth.isSelected());
        sampler.setIterations(iterations.getText());
        sampler.setReadResponse(String.valueOf(readResponse.isSelected()));
        sampler.setClientChoice(clientChoice.getText());
        sampler.setStopBetweenSamples(stopBetweenSamples.isSelected());
        sampler.setTimeout(timeout.getText());
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
        
        jndiICF.setToolTipText(Context.INITIAL_CONTEXT_FACTORY);
        urlField.setToolTipText(Context.PROVIDER_URL);
        jmsUser.setToolTipText(Context.SECURITY_PRINCIPAL);
        jmsPwd.setToolTipText(Context.SECURITY_CREDENTIALS);
        mainPanel.add(useProperties);
        mainPanel.add(jndiICF);
        mainPanel.add(urlField);
        mainPanel.add(jndiConnFac);
        mainPanel.add(jmsDestination);
        mainPanel.add(useAuth);
        mainPanel.add(jmsUser);
        mainPanel.add(jmsPwd);
        mainPanel.add(iterations);

        mainPanel.add(readResponse);
        mainPanel.add(timeout);
        
        JPanel choice = new HorizontalPanel();
        choice.add(clientChoice);
        choice.add(stopBetweenSamples);
        mainPanel.add(choice);

        useProperties.addChangeListener(this);
        useAuth.addChangeListener(this);
    }

    /**
     * the implementation loads the URL and the soap action for the request.
     */
    @Override
    public void configure(TestElement el) {
        super.configure(el);
        SubscriberSampler sampler = (SubscriberSampler) el;
        useProperties.setSelected(sampler.getUseJNDIPropertiesAsBoolean());
        jndiICF.setText(sampler.getJNDIInitialContextFactory());
        urlField.setText(sampler.getProviderUrl());
        jndiConnFac.setText(sampler.getConnectionFactory());
        jmsDestination.setText(sampler.getDestination());
        jmsUser.setText(sampler.getUsername());
        jmsPwd.setText(sampler.getPassword());
        iterations.setText(sampler.getIterations());
        useAuth.setSelected(sampler.isUseAuth());
        readResponse.setSelected(sampler.getReadResponseAsBoolean());
        clientChoice.setText(sampler.getClientChoice());
        stopBetweenSamples.setSelected(sampler.isStopBetweenSamples());
        timeout.setText(sampler.getTimeout());
    }

    @Override
    public void clearGui(){
        super.clearGui();
        useProperties.setSelected(false); // $NON-NLS-1$
        jndiICF.setText(""); // $NON-NLS-1$
        urlField.setText(""); // $NON-NLS-1$
        jndiConnFac.setText(""); // $NON-NLS-1$
        jmsDestination.setText(""); // $NON-NLS-1$
        jmsUser.setText(""); // $NON-NLS-1$
        jmsPwd.setText(""); // $NON-NLS-1$
        iterations.setText(""); // $NON-NLS-1$
        timeout.setText("");
        useAuth.setSelected(false);
        readResponse.setSelected(true);
        clientChoice.setText(RECEIVE_RSC);
        stopBetweenSamples.setSelected(false);
    }

    /**
     * When the state of a widget changes, it will notify the gui. the method
     * then enables or disables certain parameters.
     */
    public void stateChanged(ChangeEvent event) {
        if (event.getSource() == useProperties) {
            jndiICF.setEnabled(!useProperties.isSelected());
            urlField.setEnabled(!useProperties.isSelected());
        } else if (event.getSource() == useAuth) {
            jmsUser.setEnabled(!useAuth.isSelected());
            jmsPwd.setEnabled(!useAuth.isSelected());
        }
    }
}
 No newline at end of file
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

QDox parse error in file://<WORKSPACE>/data/code-rep-dataset/Dataset5/Tasks/548.java