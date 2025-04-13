error id: file://<WORKSPACE>/data/code-rep-dataset/Dataset5/Tasks/5306.java
file://<WORKSPACE>/data/code-rep-dataset/Dataset5/Tasks/5306.java
### com.thoughtworks.qdox.parser.ParseException: syntax error @[1,1]

error in qdox parser
file content:
```java
offset: 1
uri: file://<WORKSPACE>/data/code-rep-dataset/Dataset5/Tasks/5306.java
text:
```scala
s@@oapXml = new JLabeledTextArea(JMeterUtils.getResString("soap_data_title")); //$NON-NLS-1$

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

package org.apache.jmeter.protocol.http.control.gui;

import java.awt.BorderLayout;
import java.awt.Dimension;
import java.awt.GridBagLayout;
import java.awt.GridBagConstraints;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

import javax.swing.JCheckBox;
import javax.swing.JPanel;

import org.apache.jmeter.protocol.http.sampler.SoapSampler;
import org.apache.jmeter.samplers.gui.AbstractSamplerGui;
import org.apache.jmeter.testelement.TestElement;
import org.apache.jmeter.util.JMeterUtils;
import org.apache.jmeter.gui.util.FilePanel;
import org.apache.jorphan.gui.JLabeledTextArea;
import org.apache.jorphan.gui.JLabeledTextField;

/**
 */
public class SoapSamplerGui extends AbstractSamplerGui {
	private JLabeledTextField urlField;
	private JLabeledTextField soapAction;
    private JCheckBox sendSoapAction;
	private JLabeledTextArea soapXml;

    private FilePanel soapXmlFile = new FilePanel();

	public SoapSamplerGui() {
		init();
	}

	public String getLabelResource() {
		return "soap_sampler_title"; //$NON-NLS-1$
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see org.apache.jmeter.gui.JMeterGUIComponent#createTestElement()
	 */
	public TestElement createTestElement() {
		SoapSampler sampler = new SoapSampler();
		modifyTestElement(sampler);
		return sampler;
	}

	/**
	 * Modifies a given TestElement to mirror the data in the gui components.
	 * 
	 * @see org.apache.jmeter.gui.JMeterGUIComponent#modifyTestElement(TestElement)
	 */
	public void modifyTestElement(TestElement s) {
		this.configureTestElement(s);
		if (s instanceof SoapSampler) {
			SoapSampler sampler = (SoapSampler) s;
			sampler.setURLData(urlField.getText());
			sampler.setXmlData(soapXml.getText());
            sampler.setXmlFile(soapXmlFile.getFilename());
			sampler.setSOAPAction(soapAction.getText());
			sampler.setSendSOAPAction(sendSoapAction.isSelected());
		}
	}

    /**
     * Implements JMeterGUIComponent.clear
     */
    public void clear() {
        super.clear();
        
        urlField.setText(""); //$NON-NLS-1$
        soapAction.setText(""); //$NON-NLS-1$
        soapXml.setText(""); //$NON-NLS-1$
        sendSoapAction.setSelected(true);
        soapXmlFile.setFilename(""); //$NON-NLS-1$
    }    

	private void init() {
		setLayout(new BorderLayout());
		setBorder(makeBorder());

		add(makeTitlePanel(), BorderLayout.NORTH);

		urlField = new JLabeledTextField(JMeterUtils.getResString("url"), 10); //$NON-NLS-1$
		soapXml = new JLabeledTextArea(JMeterUtils.getResString("soap_data_title"), null); //$NON-NLS-1$
		soapAction = new JLabeledTextField("", 10); //$NON-NLS-1$
		sendSoapAction = new JCheckBox(JMeterUtils.getResString("soap_send_action"), true); //$NON-NLS-1$

		JPanel mainPanel = new JPanel(new BorderLayout());
	    JPanel soapActionPanel = new JPanel();
	    soapActionPanel.setLayout(new GridBagLayout());
	    GridBagConstraints c = new GridBagConstraints();
	    c.fill = GridBagConstraints.HORIZONTAL;
	    c.gridwidth = 2;
	    c.gridx = 0;
	    c.gridy = 0;
	    c.weightx = 1;
	    soapActionPanel.add(urlField, c);
	    c.fill = GridBagConstraints.NONE;
	    c.gridwidth = 1;
	    c.gridy = 1;
	    c.weightx = 0;
	    soapActionPanel.add(sendSoapAction, c);
	    c.gridx = 1;
	    c.fill = GridBagConstraints.HORIZONTAL;
	    c.weightx = 1;
		soapActionPanel.add(soapAction, c);
		mainPanel.add(soapActionPanel, BorderLayout.NORTH);
		mainPanel.add(soapXml, BorderLayout.CENTER);
        mainPanel.add(soapXmlFile, BorderLayout.SOUTH);

        sendSoapAction.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
		        soapAction.setEnabled(sendSoapAction.isSelected());
		    }
            });

		add(mainPanel, BorderLayout.CENTER);
	}

	public void configure(TestElement el) {
		super.configure(el);
		SoapSampler sampler = (SoapSampler) el;
		urlField.setText(sampler.getURLData());
		sendSoapAction.setSelected(sampler.getSendSOAPAction());
		soapAction.setText(sampler.getSOAPAction());
		soapXml.setText(sampler.getXmlData());
        soapXmlFile.setFilename(sampler.getXmlFile());
	}

	public Dimension getPreferredSize() {
		return getMinimumSize();
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

QDox parse error in file://<WORKSPACE>/data/code-rep-dataset/Dataset5/Tasks/5306.java