error id: file://<WORKSPACE>/data/code-rep-dataset/Dataset5/Tasks/11007.java
file://<WORKSPACE>/data/code-rep-dataset/Dataset5/Tasks/11007.java
### com.thoughtworks.qdox.parser.ParseException: syntax error @[1,19]

error in qdox parser
file content:
```java
offset: 19
uri: file://<WORKSPACE>/data/code-rep-dataset/Dataset5/Tasks/11007.java
text:
```scala
private transient W@@SDLHelper HELPER = null;

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

import java.awt.Dimension;
import java.awt.Font;
import java.awt.event.ActionEvent;

import javax.swing.JButton;
import javax.swing.JCheckBox;
import javax.swing.JOptionPane;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.border.Border;
import javax.swing.border.EmptyBorder;

import org.apache.jmeter.protocol.http.sampler.HTTPSamplerBase;
import org.apache.jmeter.protocol.http.sampler.WebServiceSampler;
import org.apache.jmeter.samplers.gui.AbstractSamplerGui;
import org.apache.jmeter.testelement.TestElement;
import org.apache.jmeter.util.JMeterUtils;
import org.apache.jmeter.gui.util.FilePanel;
import org.apache.jorphan.gui.JLabeledChoice;
import org.apache.jorphan.gui.JLabeledTextArea;
import org.apache.jorphan.gui.JLabeledTextField;
import org.apache.jorphan.gui.layout.VerticalLayout;
import org.apache.jmeter.protocol.http.util.WSDLHelper;
import org.apache.jmeter.protocol.http.control.AuthManager;

/**
 * This is the GUI for the webservice samplers. It extends AbstractSamplerGui
 * and is modeled after the SOAP sampler GUI. I've added instructional notes to
 * the GUI for instructional purposes. XML parsing is pretty heavy weight,
 * therefore the notes address those situations. <br>
 * Created on: Jun 26, 2003
 * 
 * author Peter Lin
 */
public class WebServiceSamplerGui extends AbstractSamplerGui implements java.awt.event.ActionListener {

	private JLabeledTextField domain = new JLabeledTextField(JMeterUtils.getResString("web_server_domain")); // $NON-NLS-1$

    private JLabeledTextField protocol = new JLabeledTextField(JMeterUtils.getResString("protocol")); // $NON-NLS-1$

    private JLabeledTextField port = new JLabeledTextField(JMeterUtils.getResString("web_server_port")); // $NON-NLS-1$

    private JLabeledTextField path = new JLabeledTextField(JMeterUtils.getResString("path")); // $NON-NLS-1$

    private JLabeledTextField soapAction = new JLabeledTextField(JMeterUtils.getResString("webservice_soap_action")); // $NON-NLS-1$

    private JLabeledTextArea soapXml = new JLabeledTextArea(JMeterUtils.getResString("soap_data_title"), null); // $NON-NLS-1$

    private JLabeledTextField wsdlField = new JLabeledTextField(JMeterUtils.getResString("wsdl_url")); // $NON-NLS-1$

    private JButton wsdlButton = new JButton(JMeterUtils.getResString("load_wsdl")); // $NON-NLS-1$

    private JButton selectButton = new JButton(JMeterUtils.getResString("configure_wsdl")); // $NON-NLS-1$

    private JLabeledChoice wsdlMethods = null;

    private WSDLHelper HELPER = null;

    private FilePanel soapXmlFile = new FilePanel(JMeterUtils.getResString("get_xml_from_file"), ".xml"); // $NON-NLS-1$

    private JLabeledTextField randomXmlFile = new JLabeledTextField(JMeterUtils.getResString("get_xml_from_random")); // $NON-NLS-1$

    private JLabeledTextField connectTimeout = new JLabeledTextField(JMeterUtils.getResString("webservice_timeout")); // $NON-NLS-1$

	/**
	 * We create several JLabel objects to display usage instructions in the
	 * GUI. The reason there are multiple labels is to make sure it displays
	 * correctly.
	 */
    private JLabel wsdlMessage = new JLabel(JMeterUtils.getResString("get_xml_message")); // $NON-NLS-1$

    private JLabel wsdlMessage2 = new JLabel(JMeterUtils.getResString("get_xml_message2")); // $NON-NLS-1$

    private JLabel wsdlMessage3 = new JLabel(JMeterUtils.getResString("get_xml_message3")); // $NON-NLS-1$

    private JLabel wsdlMessage4 = new JLabel(JMeterUtils.getResString("get_xml_message4")); // $NON-NLS-1$

    private JLabel wsdlMessage5 = new JLabel(JMeterUtils.getResString("get_xml_message5")); // $NON-NLS-1$

	/**
	 * This is the font for the note.
	 */
    private Font plainText = new Font("plain", Font.PLAIN, 10); // $NON-NLS-1$

	/**
	 * checkbox for memory cache.
	 */
    private JCheckBox memCache = new JCheckBox(JMeterUtils.getResString("memory_cache"), true); // $NON-NLS-1$

	/**
	 * checkbox for reading the response
	 */
    private JCheckBox readResponse = new JCheckBox(JMeterUtils.getResString("read_soap_response")); // $NON-NLS-1$

	/**
	 * checkbox for use proxy
	 */
    private JCheckBox useProxy = new JCheckBox(JMeterUtils.getResString("webservice_use_proxy")); // $NON-NLS-1$

	/**
	 * text field for the proxy host
	 */
    private JLabeledTextField proxyHost = new JLabeledTextField(JMeterUtils.getResString("webservice_proxy_host")); // $NON-NLS-1$

	/**
	 * text field for the proxy port
	 */
    private JLabeledTextField proxyPort = new JLabeledTextField(JMeterUtils.getResString("webservice_proxy_port")); // $NON-NLS-1$

	/**
	 * Text note about read response and its usage.
	 */
    private JLabel readMessage = new JLabel(JMeterUtils.getResString("read_response_note")); // $NON-NLS-1$

    private JLabel readMessage2 = new JLabel(JMeterUtils.getResString("read_response_note2")); // $NON-NLS-1$

    private JLabel readMessage3 = new JLabel(JMeterUtils.getResString("read_response_note3")); // $NON-NLS-1$

	/**
	 * Text note for proxy
	 */
    private JLabel proxyMessage = new JLabel(JMeterUtils.getResString("webservice_proxy_note")); // $NON-NLS-1$

    private JLabel proxyMessage2 = new JLabel(JMeterUtils.getResString("webservice_proxy_note2")); // $NON-NLS-1$

    private JLabel proxyMessage3 = new JLabel(JMeterUtils.getResString("webservice_proxy_note3")); // $NON-NLS-1$

	public WebServiceSamplerGui() {
		init();
	}

	public String getLabelResource() {
		return "webservice_sampler_title"; // $NON-NLS-1$
	}

	/**
	 * @see org.apache.jmeter.gui.JMeterGUIComponent#createTestElement()
	 */
	public TestElement createTestElement() {
		WebServiceSampler sampler = new WebServiceSampler();
		this.configureTestElement(sampler);
		this.modifyTestElement(sampler);
		return sampler;
	}

	/**
	 * Modifies a given TestElement to mirror the data in the gui components.
	 * 
	 * @see org.apache.jmeter.gui.JMeterGUIComponent#modifyTestElement(TestElement)
	 */
	public void modifyTestElement(TestElement s) {
		WebServiceSampler sampler = (WebServiceSampler) s;
		this.configureTestElement(sampler);
		sampler.setDomain(domain.getText());
        sampler.setProperty(HTTPSamplerBase.PORT,port.getText());
        sampler.setProtocol(protocol.getText());
		sampler.setPath(path.getText());
		sampler.setWsdlURL(wsdlField.getText());
		sampler.setMethod(HTTPSamplerBase.POST);
		sampler.setSoapAction(soapAction.getText());
		sampler.setXmlData(soapXml.getText());
		sampler.setXmlFile(soapXmlFile.getFilename());
		sampler.setXmlPathLoc(randomXmlFile.getText());
        sampler.setTimeout(connectTimeout.getText());
		sampler.setMemoryCache(memCache.isSelected());
		sampler.setReadResponse(readResponse.isSelected());
		sampler.setUseProxy(useProxy.isSelected());
		sampler.setProxyHost(proxyHost.getText());
		sampler.setProxyPort(proxyPort.getText());
	}

	/**
	 * init() adds soapAction to the mainPanel. The class reuses logic from
	 * SOAPSampler, since it is common.
	 */
	private void init() {
		this.setLayout(new VerticalLayout(5, VerticalLayout.LEFT, VerticalLayout.TOP));
		wsdlMessage.setFont(plainText);
		wsdlMessage2.setFont(plainText);
		wsdlMessage3.setFont(plainText);
		wsdlMessage4.setFont(plainText);
		wsdlMessage5.setFont(plainText);
		readMessage.setFont(plainText);
		readMessage2.setFont(plainText);
		readMessage3.setFont(plainText);

		// MAIN PANEL
		JPanel mainPanel = new JPanel();
		Border margin = new EmptyBorder(10, 10, 5, 10);
		mainPanel.setBorder(margin);
		mainPanel.setLayout(new VerticalLayout(5, VerticalLayout.LEFT));

		// TITLE
		JLabel panelTitleLabel = new JLabel(getStaticLabel());
		Font curFont = panelTitleLabel.getFont();
		int curFontSize = curFont.getSize();
		curFontSize += 4;
		panelTitleLabel.setFont(new Font(curFont.getFontName(), curFont.getStyle(), curFontSize));
		mainPanel.add(panelTitleLabel);
		// NAME
		mainPanel.add(getNamePanel());

		// Button for browsing webservice wsdl
		JPanel wsdlEntry = new JPanel();
		mainPanel.add(wsdlEntry);
		wsdlEntry.add(wsdlField);
		wsdlEntry.add(wsdlButton);
		wsdlButton.addActionListener(this);

		// Web Methods
		JPanel listPanel = new JPanel();
		JLabel selectLabel = new JLabel("Web Methods");
		wsdlMethods = new JLabeledChoice();
		mainPanel.add(listPanel);
		listPanel.add(selectLabel);
		listPanel.add(wsdlMethods);
		listPanel.add(selectButton);
		selectButton.addActionListener(this);

        mainPanel.add(protocol);
		mainPanel.add(domain);
		mainPanel.add(port);
		mainPanel.add(path);
        mainPanel.add(connectTimeout);
		mainPanel.add(soapAction);
		// OPTIONAL TASKS
		// we create a preferred size for the soap text area
		// the width is the same as the soap file browser
		Dimension pref = new Dimension(400, 200);
		soapXml.setPreferredSize(pref);
		mainPanel.add(soapXml);
		mainPanel.add(soapXmlFile);
		mainPanel.add(wsdlMessage);
		mainPanel.add(wsdlMessage2);
		mainPanel.add(wsdlMessage3);
		mainPanel.add(wsdlMessage4);
		mainPanel.add(wsdlMessage5);
		mainPanel.add(randomXmlFile);
		mainPanel.add(memCache);
		mainPanel.add(readResponse);
		mainPanel.add(readMessage);
		mainPanel.add(readMessage2);
		mainPanel.add(readMessage3);

		// add the proxy elements
		mainPanel.add(useProxy);
		useProxy.addActionListener(this);
		mainPanel.add(proxyHost);
		mainPanel.add(proxyPort);
		// add the proxy notes
		proxyMessage.setFont(plainText);
		proxyMessage2.setFont(plainText);
		proxyMessage3.setFont(plainText);
		mainPanel.add(proxyMessage);
		mainPanel.add(proxyMessage2);
		mainPanel.add(proxyMessage3);

		this.add(mainPanel);
	}

	/**
	 * the implementation loads the URL and the soap action for the request.
	 */
	public void configure(TestElement el) {
		super.configure(el);
		WebServiceSampler sampler = (WebServiceSampler) el;
		wsdlField.setText(sampler.getWsdlURL());
        protocol.setText(sampler.getProtocol());
		domain.setText(sampler.getDomain());
        port.setText(sampler.getPropertyAsString(HTTPSamplerBase.PORT));
		path.setText(sampler.getPath());
		soapAction.setText(sampler.getSoapAction());
		soapXml.setText(sampler.getXmlData());
		soapXmlFile.setFilename(sampler.getXmlFile());
		randomXmlFile.setText(sampler.getXmlPathLoc());
        connectTimeout.setText(sampler.getTimeout());
		memCache.setSelected(sampler.getMemoryCache());
		readResponse.setSelected(sampler.getReadResponse());
		useProxy.setSelected(sampler.getUseProxy());
		if (sampler.getProxyHost().length() == 0) {
			proxyHost.setEnabled(false);
		} else {
			proxyHost.setText(sampler.getProxyHost());
		}
		if (sampler.getProxyPort() == 0) {
			proxyPort.setEnabled(false);
		} else {
			proxyPort.setText(String.valueOf(sampler.getProxyPort()));
		}
	}

	/**
	 * configure the sampler from the WSDL. If the WSDL did not include service
	 * node, it will use the original URL minus the querystring. That may not be
	 * correct, so we should probably add a note. For Microsoft webservices it
	 * will work, since that's how IIS works.
	 */
	public void configureFromWSDL() {
		if (HELPER != null) {
            if(HELPER.getBinding() != null) {
                this.protocol.setText(HELPER.getProtocol());
    			this.domain.setText(HELPER.getBindingHost());
    			if (HELPER.getBindingPort() > 0) {
    				this.port.setText(String.valueOf(HELPER.getBindingPort()));
    			} else {
    				this.port.setText("80"); // $NON-NLS-1$
    			}
    			this.path.setText(HELPER.getBindingPath());
    		}
    		this.soapAction.setText(HELPER.getSoapAction(this.wsdlMethods.getText()));
        }
	}

	/**
	 * The method uses WSDLHelper to get the information from the WSDL. Since
	 * the logic for getting the description is isolated to this method, we can
	 * easily replace it with a different WSDL driver later on.
	 * 
	 * @param url
	 * @return array of web methods
	 */
	public String[] browseWSDL(String url) {
		try {
			// We get the AuthManager and pass it to the WSDLHelper
			// once the sampler is updated to Axis, all of this stuff
			// should not be necessary. Now I just need to find the
			// time and motivation to do it.
			WebServiceSampler sampler = (WebServiceSampler) this.createTestElement();
			AuthManager manager = sampler.getAuthManager();
			HELPER = new WSDLHelper(url, manager);
			HELPER.parse();
			return HELPER.getWebMethods();
		} catch (Exception exception) {
			JOptionPane.showConfirmDialog(this,
                    JMeterUtils.getResString("wsdl_helper_error"), // $NON-NLS-1$
                    "Warning",
					JOptionPane.OK_CANCEL_OPTION, JOptionPane.ERROR_MESSAGE);
			return null;
		}
	}

	/**
	 * method from ActionListener
	 * 
	 * @param event
	 *            that occurred
	 */
	public void actionPerformed(ActionEvent event) {
		if (event.getSource() == selectButton) {
			this.configureFromWSDL();
		} else if (event.getSource() == useProxy) {
			// if use proxy is checked, we enable
			// the text fields for the host and port
			boolean use = useProxy.isSelected();
			if (use) {
				proxyHost.setEnabled(true);
				proxyPort.setEnabled(true);
			} else {
				proxyHost.setEnabled(false);
				proxyPort.setEnabled(false);
			}
		} else {
			if (this.domain.getText() != null) {
				String[] wsdlData = browseWSDL(wsdlField.getText());
				if (wsdlData != null) {
					wsdlMethods.setValues(wsdlData);
					wsdlMethods.repaint();
				}
			} else {
				JOptionPane.showConfirmDialog(this,
                        JMeterUtils.getResString("wsdl_url_error"), // $NON-NLS-1$
                        "Warning",
						JOptionPane.OK_CANCEL_OPTION, JOptionPane.ERROR_MESSAGE);
			}
		}
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

QDox parse error in file://<WORKSPACE>/data/code-rep-dataset/Dataset5/Tasks/11007.java