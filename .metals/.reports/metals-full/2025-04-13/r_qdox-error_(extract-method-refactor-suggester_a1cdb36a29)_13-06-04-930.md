error id: file://<WORKSPACE>/data/code-rep-dataset/Dataset5/Tasks/15122.java
file://<WORKSPACE>/data/code-rep-dataset/Dataset5/Tasks/15122.java
### com.thoughtworks.qdox.parser.ParseException: syntax error @[1,1]

error in qdox parser
file content:
```java
offset: 1
uri: file://<WORKSPACE>/data/code-rep-dataset/Dataset5/Tasks/15122.java
text:
```scala
r@@eturn JMeterUtils.getResString("tcp_config_title")+" (ALPHA CODE)";

package org.apache.jmeter.protocol.tcp.config.gui;
import java.awt.BorderLayout;

import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JTextField;

import org.apache.jmeter.config.ConfigTestElement;
import org.apache.jmeter.config.gui.AbstractConfigGui;
import org.apache.jmeter.gui.util.VerticalPanel;
import org.apache.jmeter.protocol.tcp.sampler.TCPSampler;
import org.apache.jmeter.testelement.TestElement;
import org.apache.jmeter.util.JMeterUtils;

/**
 * @version   $Revision$ $Date$
 */
public class TCPConfigGui extends AbstractConfigGui
{
    private final static String SERVER = "server";     //$NON-NLS-1$
    private final static String PORT = "port";         //$NON-NLS-1$   
	private final static String FILENAME = "filename"; //$NON-NLS-1$ 
	private final static String TIMEOUT = "timeout";   //$NON-NLS-1$
	private final static String NODELAY = "nodelay";   //$NON-NLS-1$

    private JTextField server;
    private JTextField port;
	private JTextField filename;
	private JTextField timeout;
	private JTextField nodelay;

    private boolean displayName = true;

    public TCPConfigGui()
    {
        this(true);
    }

    public TCPConfigGui(boolean displayName)
    {
        this.displayName = displayName;
        init();
    }
    
    public String getStaticLabel()
    {
        return JMeterUtils.getResString("tcp_sample_title");
    }

    public void configure(TestElement element)
    {
        super.configure(element);
        server.setText(element.getPropertyAsString(TCPSampler.SERVER));
        port.setText(element.getPropertyAsString(TCPSampler.PORT));
		filename.setText(element.getPropertyAsString(TCPSampler.FILENAME));
		timeout.setText(element.getPropertyAsString(TCPSampler.TIMEOUT));
		nodelay.setText(element.getPropertyAsString(TCPSampler.NODELAY));
	}

    public TestElement createTestElement()
    {
        ConfigTestElement element = new ConfigTestElement();
        modifyTestElement(element);
        return element;
    }

    /**
     * Modifies a given TestElement to mirror the data in the gui components.
     * @see org.apache.jmeter.gui.JMeterGUIComponent#modifyTestElement(TestElement)
     */
    public void modifyTestElement(TestElement element)
    {
        configureTestElement(element);
        element.setProperty(TCPSampler.SERVER, server.getText());
        element.setProperty(TCPSampler.PORT, port.getText());
		element.setProperty(TCPSampler.FILENAME, filename.getText());
		element.setProperty(TCPSampler.NODELAY, nodelay.getText());
		element.setProperty(TCPSampler.TIMEOUT, timeout.getText());
    }

    private JPanel createTimeoutPanel()
    {
        JLabel label = new JLabel(JMeterUtils.getResString("tcp_timeout"));

        timeout = new JTextField(10);
        timeout.setName(TIMEOUT);
        label.setLabelFor(timeout);

        JPanel timeoutPanel = new JPanel(new BorderLayout(5, 0));
        timeoutPanel.add(label, BorderLayout.WEST);
        timeoutPanel.add(timeout, BorderLayout.CENTER);
        return timeoutPanel;
    }

	private JPanel createNoDelayPanel()
	{
		JLabel label = new JLabel(JMeterUtils.getResString("tcp_nodelay"));

		nodelay = new JTextField(10);
		nodelay.setName(NODELAY);
		label.setLabelFor(nodelay);

		JPanel nodelayPanel = new JPanel(new BorderLayout(5, 0));
		nodelayPanel.add(label, BorderLayout.WEST);
		nodelayPanel.add(nodelay, BorderLayout.CENTER);
		return nodelayPanel;
	}

	private JPanel createServerPanel()
	{
		JLabel label = new JLabel(JMeterUtils.getResString("server"));

		server = new JTextField(10);
		server.setName(SERVER);
		label.setLabelFor(server);

		JPanel serverPanel = new JPanel(new BorderLayout(5, 0));
		serverPanel.add(label, BorderLayout.WEST);
		serverPanel.add(server, BorderLayout.CENTER);
		return serverPanel;
	}

    private JPanel createPortPanel()
    {
        JLabel label = new JLabel(JMeterUtils.getResString("tcp_port"));

        port = new JTextField(10);
        port.setName(PORT);
        label.setLabelFor(port);

        JPanel PortPanel = new JPanel(new BorderLayout(5, 0));
        PortPanel.add(label, BorderLayout.WEST);
        PortPanel.add(port, BorderLayout.CENTER);
        return PortPanel;
    }

	private JPanel createFilenamePanel()
	{
		JLabel label = new JLabel(JMeterUtils.getResString("file_to_retrieve"));

		filename = new JTextField(10);
		filename.setName(FILENAME);
		label.setLabelFor(filename);

		JPanel filenamePanel = new JPanel(new BorderLayout(5, 0));
		filenamePanel.add(label, BorderLayout.WEST);
		filenamePanel.add(filename, BorderLayout.CENTER);
		return filenamePanel;
	}

    private void init()
    {
        setLayout(new BorderLayout(0, 5));

        if (displayName)
        {
            setBorder(makeBorder());
            add(makeTitlePanel(), BorderLayout.NORTH);
        }

        VerticalPanel mainPanel = new VerticalPanel();
        mainPanel.add(createServerPanel());
        mainPanel.add(createPortPanel());
		mainPanel.add(createTimeoutPanel());
		mainPanel.add(createNoDelayPanel());

		mainPanel.add(createFilenamePanel());
		add(mainPanel, BorderLayout.CENTER);
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

QDox parse error in file://<WORKSPACE>/data/code-rep-dataset/Dataset5/Tasks/15122.java