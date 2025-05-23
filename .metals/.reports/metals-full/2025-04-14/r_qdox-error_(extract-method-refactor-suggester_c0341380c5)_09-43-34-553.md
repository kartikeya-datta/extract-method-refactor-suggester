error id: file://<WORKSPACE>/data/code-rep-dataset/Dataset5/Tasks/11623.java
file://<WORKSPACE>/data/code-rep-dataset/Dataset5/Tasks/11623.java
### com.thoughtworks.qdox.parser.ParseException: syntax error @[1,1]

error in qdox parser
file content:
```java
offset: 1
uri: file://<WORKSPACE>/data/code-rep-dataset/Dataset5/Tasks/11623.java
text:
```scala
j@@ndiPropertiesPanel = new ArgumentsPanel(JMeterUtils.getResString("jms_jndi_props"));

/*
 * Copyright 2001-2004 The Apache Software Foundation.
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
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

import javax.swing.BorderFactory;
import javax.swing.JComboBox;
import javax.swing.JPanel;

import org.apache.jmeter.config.Arguments;
import org.apache.jmeter.config.ConfigTestElement;
import org.apache.jmeter.config.gui.ArgumentsPanel;
import org.apache.jmeter.protocol.jms.sampler.JMSSampler;
import org.apache.jmeter.testelement.TestElement;
import org.apache.jmeter.testelement.property.BooleanProperty;
import org.apache.jmeter.testelement.property.TestElementProperty;
import org.apache.jmeter.util.JMeterUtils;
import org.apache.jorphan.gui.JLabeledChoice;
import org.apache.jorphan.gui.JLabeledTextArea;
import org.apache.jorphan.gui.JLabeledTextField;

/**
 * Configuration screen for Java Messaging Point-to-Point requests.
 * <br>
 * Created on:  October 28, 2004
 *
 * @author Martijn Blankestijn
 * @version $Id$ 
 */
public class JMSConfigGui extends JPanel {

	private JLabeledTextField queueuConnectionFactory =
		new JLabeledTextField(JMeterUtils.getResString("jms_queue_connection_factory"));
	private JLabeledTextField sendQueue =
		new JLabeledTextField(JMeterUtils.getResString("jms_send_queue"));
	private JLabeledTextField receiveQueue =
		new JLabeledTextField(JMeterUtils.getResString("jms_receive_queue"));
	private JLabeledTextField timeout =
		new JLabeledTextField(JMeterUtils.getResString("jms_timeout"));
	private JLabeledTextArea soapXml =
		new JLabeledTextArea(JMeterUtils.getResString("jms_msg_content"), null);
	private JLabeledTextField initialContextFactory =
		new JLabeledTextField(JMeterUtils.getResString("jms_initial_context_factory"));
	private JLabeledTextField providerUrl =
		new JLabeledTextField(JMeterUtils.getResString("jms_provider_url"));

	private String[] labels =
		new String[] {
			JMeterUtils.getResString("jms_request"),
			JMeterUtils.getResString("jms_requestreply")};
	private JLabeledChoice oneWay =
		new JLabeledChoice(JMeterUtils.getResString("jms_communication_style"), labels);
	private ArgumentsPanel jmsPropertiesPanel;
	private ArgumentsPanel jndiPropertiesPanel;

	public JMSConfigGui() {
		init();
	}

	/**
	 * @param element
	 */
	protected void configureTestElement(TestElement element) {
		element.setProperty(TestElement.NAME, getName());
		element.setProperty(TestElement.GUI_CLASS, this.getClass().getName());
		element.setProperty(TestElement.TEST_CLASS, element.getClass().getName());
	}

	/**
	 * Clears all fields.
	 */
	public void clear() {
		queueuConnectionFactory.setText("");
		sendQueue.setText("");
		receiveQueue.setText("");
		((JComboBox) oneWay.getComponentList().get(1)).setSelectedItem(
			JMeterUtils.getResString("jms_request"));
		timeout.setText("");
		soapXml.setText("");
		initialContextFactory.setText("");
		providerUrl.setText("");
		jmsPropertiesPanel.clear();
		jndiPropertiesPanel.clear();
	}

	/**
	 * @return
	 */
	public TestElement createTestElement() {
		ConfigTestElement element = new ConfigTestElement();

		this.configureTestElement(element);
		element.setProperty(
			JMSSampler.QUEUE_CONNECTION_FACTORY_JNDI,
			queueuConnectionFactory.getText());
		element.setProperty(JMSSampler.SEND_QUEUE, sendQueue.getText());
		element.setProperty(JMSSampler.RECEIVE_QUEUE, receiveQueue.getText());

		boolean isOneway = oneWay.getText().equals(JMeterUtils.getResString("jms_request"));
		element.setProperty(new BooleanProperty(JMSSampler.IS_ONE_WAY, isOneway));

		element.setProperty(JMSSampler.TIMEOUT, timeout.getText());
		element.setProperty(JMSSampler.XML_DATA, soapXml.getText());

		element.setProperty(
			JMSSampler.JNDI_INITIAL_CONTEXT_FACTORY,
			initialContextFactory.getText());
		element.setProperty(JMSSampler.JNDI_CONTEXT_PROVIDER_URL, providerUrl.getText());
		Arguments jndiArgs = (Arguments) jndiPropertiesPanel.createTestElement();
		element.setProperty(new TestElementProperty(JMSSampler.JNDI_PROPERTIES, jndiArgs));

		Arguments args = (Arguments) jmsPropertiesPanel.createTestElement();
		element.setProperty(new TestElementProperty(JMSSampler.JMS_PROPERTIES, args));
		return element;
	}

	/**
	 * @param el
	 */
	public void configure(TestElement el) {
		setName(el.getPropertyAsString(TestElement.NAME));
		queueuConnectionFactory.setText(
			el.getPropertyAsString(JMSSampler.QUEUE_CONNECTION_FACTORY_JNDI));
		sendQueue.setText(el.getPropertyAsString(JMSSampler.SEND_QUEUE));
		receiveQueue.setText(el.getPropertyAsString(JMSSampler.RECEIVE_QUEUE));

		JComboBox box = (JComboBox) oneWay.getComponentList().get(1);
		String selected = null;
		if (el.getPropertyAsBoolean(JMSSampler.IS_ONE_WAY)) {
			selected = JMeterUtils.getResString("jms_request");
		} else {
			selected = JMeterUtils.getResString("jms_requestreply");
		}
		box.setSelectedItem(selected);

		timeout.setText(el.getPropertyAsString(JMSSampler.TIMEOUT));
		soapXml.setText(el.getPropertyAsString(JMSSampler.XML_DATA));
		initialContextFactory.setText(
			el.getPropertyAsString(JMSSampler.JNDI_INITIAL_CONTEXT_FACTORY));
		providerUrl.setText(el.getPropertyAsString(JMSSampler.JNDI_CONTEXT_PROVIDER_URL));

		jmsPropertiesPanel.configure(
			(TestElement) el.getProperty(JMSSampler.JMS_PROPERTIES).getObjectValue());

		jndiPropertiesPanel.configure(
			(TestElement) el.getProperty(JMSSampler.JNDI_PROPERTIES).getObjectValue());
	}

	/**
	 * Initializes the configuration screen.
	 *
	 */
	private void init() {
		setLayout(new BorderLayout());

		JPanel jmsQueueingPanel = new JPanel(new BorderLayout());
		jmsQueueingPanel.setBorder(
			BorderFactory.createTitledBorder(
				BorderFactory.createEtchedBorder(),
				JMeterUtils.getResString("jms_queueing")));

		JPanel qcfPanel = new JPanel(new BorderLayout(5, 0));
		qcfPanel.add(queueuConnectionFactory, BorderLayout.CENTER);
		jmsQueueingPanel.add(qcfPanel, BorderLayout.NORTH);

		JPanel sendQueuePanel = new JPanel(new BorderLayout(5, 0));
		sendQueuePanel.add(sendQueue);
		jmsQueueingPanel.add(sendQueuePanel, BorderLayout.CENTER);

		JPanel receiveQueuePanel = new JPanel(new BorderLayout(5, 0));
		receiveQueuePanel.add(receiveQueue);
		jmsQueueingPanel.add(receiveQueuePanel, BorderLayout.SOUTH);

		JPanel jndiPanel = createJNDIPanel();

		JPanel messagePanel = new JPanel(new BorderLayout());
		messagePanel.setBorder(
			BorderFactory.createTitledBorder(
				BorderFactory.createEtchedBorder(),
				JMeterUtils.getResString("jms_message_title")));

		JPanel messageNorthPanel = new JPanel(new BorderLayout());
		JPanel onewayPanel = new JPanel(new BorderLayout());
		onewayPanel.add(oneWay);
		messageNorthPanel.add(onewayPanel, BorderLayout.NORTH);

		JPanel timeoutPanel = new JPanel(new BorderLayout());
		timeoutPanel.add(timeout);
		messageNorthPanel.add(timeoutPanel, BorderLayout.SOUTH);

		messagePanel.add(messageNorthPanel, BorderLayout.NORTH);

		JPanel soapXmlPanel = new JPanel(new BorderLayout());
		soapXmlPanel.add(soapXml);
		messagePanel.add(soapXmlPanel, BorderLayout.CENTER);

		jmsPropertiesPanel = new ArgumentsPanel(JMeterUtils.getResString("jms_props"));
		messagePanel.add(jmsPropertiesPanel, BorderLayout.SOUTH);

		add(jmsQueueingPanel, BorderLayout.NORTH);
		add(messagePanel, BorderLayout.CENTER);
		add(jndiPanel, BorderLayout.SOUTH);
	}

	/**
	 * Creates the panel for the JNDI configuration.
	 * @return the JNDI Panel
	 */
	private JPanel createJNDIPanel() {
		JPanel jndiPanel = new JPanel(new BorderLayout());
		jndiPanel.setBorder(
			BorderFactory.createTitledBorder(
				BorderFactory.createEtchedBorder(),
				JMeterUtils.getResString("jms_jndi_props")));

		JPanel contextPanel = new JPanel(new BorderLayout(10, 0));
		contextPanel.add(initialContextFactory);
		jndiPanel.add(contextPanel, BorderLayout.NORTH);

		JPanel providerPanel = new JPanel(new BorderLayout(10, 0));
		providerPanel.add(providerUrl);
		jndiPanel.add(providerPanel, BorderLayout.SOUTH);

		jndiPropertiesPanel = new ArgumentsPanel(JMeterUtils.getResString("jndi_props"));
		jndiPanel.add(jndiPropertiesPanel);
		return jndiPanel;
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

QDox parse error in file://<WORKSPACE>/data/code-rep-dataset/Dataset5/Tasks/11623.java