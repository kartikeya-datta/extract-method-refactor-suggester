error id: file://<WORKSPACE>/data/code-rep-dataset/Dataset5/Tasks/15400.java
file://<WORKSPACE>/data/code-rep-dataset/Dataset5/Tasks/15400.java
### com.thoughtworks.qdox.parser.ParseException: syntax error @[1,1]

error in qdox parser
file content:
```java
offset: 1
uri: file://<WORKSPACE>/data/code-rep-dataset/Dataset5/Tasks/15400.java
text:
```scala
s@@ampler.removeProperty(HTTPSamplerBase.IMAGE_PARSER);

// $Header$
/*
 * Copyright 2001-2005 The Apache Software Foundation.
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

package org.apache.jmeter.protocol.http.control.gui;

import java.awt.BorderLayout;
import java.awt.Dimension;

import javax.swing.BorderFactory;
import javax.swing.JCheckBox;
import javax.swing.JPanel;

import junit.framework.TestCase;

import org.apache.jmeter.gui.util.HorizontalPanel;
import org.apache.jmeter.protocol.http.config.gui.MultipartUrlConfigGui;
import org.apache.jmeter.protocol.http.config.gui.UrlConfigGui;
import org.apache.jmeter.protocol.http.sampler.HTTPSamplerFactory;
import org.apache.jmeter.protocol.http.sampler.HTTPSamplerBase;
import org.apache.jmeter.samplers.gui.AbstractSamplerGui;
import org.apache.jmeter.testelement.TestElement;
import org.apache.jmeter.util.JMeterUtils;

/**
 * @version   $Revision$ on $Date$
 */
public class HttpTestSampleGui extends AbstractSamplerGui
{
    private UrlConfigGui urlConfigGui;
    private JCheckBox getImages;
    private JCheckBox isMon;

    public HttpTestSampleGui()
    {
        init();
    }

    public void configure(TestElement element)
    {
        super.configure(element);
        urlConfigGui.configure(element);
        getImages.setSelected(((HTTPSamplerBase) element).isImageParser());
        isMon.setSelected(((HTTPSamplerBase) element).isMonitor());
    }

    public TestElement createTestElement()
    {
        HTTPSamplerBase sampler = HTTPSamplerFactory.newInstance("HTTPSampler");
        modifyTestElement(sampler);
        return sampler;
    }

    /**
     * Modifies a given TestElement to mirror the data in the gui components.
     * @see org.apache.jmeter.gui.JMeterGUIComponent#modifyTestElement(TestElement)
     */
    public void modifyTestElement(TestElement sampler)
    {
        TestElement el = urlConfigGui.createTestElement();
        sampler.clear();
        sampler.addTestElement(el);
        if (getImages.isSelected())
        {
            ((HTTPSamplerBase)sampler).setImageParser(true);
        }
        else
        {
            ((HTTPSamplerBase)sampler).setImageParser(false);
        }
        if (isMon.isSelected()){
			((HTTPSamplerBase)sampler).setMonitor("true");
        } else {
			((HTTPSamplerBase)sampler).setMonitor("false");
        }
        this.configureTestElement(sampler);
    }

    public String getLabelResource()
    {
        return "web_testing_title";
    }

    protected void init()
    {
        setLayout(new BorderLayout(0, 5));
        setBorder(makeBorder());

        add(makeTitlePanel(), BorderLayout.NORTH);

        // URL CONFIG
        urlConfigGui = new MultipartUrlConfigGui();
        add(urlConfigGui, BorderLayout.CENTER);

        // OPTIONAL TASKS
        add(createOptionalTasksPanel(), BorderLayout.SOUTH);
    }

    private JPanel createOptionalTasksPanel()
    {
        // OPTIONAL TASKS
        HorizontalPanel optionalTasksPanel = new HorizontalPanel();
        optionalTasksPanel.setBorder(
            BorderFactory.createTitledBorder(
                BorderFactory.createEtchedBorder(),
                JMeterUtils.getResString("optional_tasks")));

        // RETRIEVE IMAGES
        JPanel retrieveImagesPanel = new JPanel();
        getImages =
            new JCheckBox(
                JMeterUtils.getResString("web_testing_retrieve_images"));
        retrieveImagesPanel.add(getImages);
        JPanel isMonitorPanel = new JPanel();
        isMon = new JCheckBox(
            JMeterUtils.getResString("monitor_is_title"));
        isMonitorPanel.add(isMon);
        optionalTasksPanel.add(retrieveImagesPanel);
		optionalTasksPanel.add(isMonitorPanel);
        return optionalTasksPanel;
    }
        
    public Dimension getPreferredSize()
    {
        return getMinimumSize();
    }

    public static class Test extends TestCase
    {
        HttpTestSampleGui gui;
        
        public Test(String name)
        {
            super(name);
        }
        
        public void setUp()
        {
            gui = new HttpTestSampleGui();
        }
        
        public void testCloneSampler() throws Exception
        {
            HTTPSamplerBase sampler = (HTTPSamplerBase)gui.createTestElement();
            sampler.addArgument("param","value");
            HTTPSamplerBase clonedSampler = (HTTPSamplerBase)sampler.clone();
            clonedSampler.setRunningVersion(true);
            sampler.getArguments().getArgument(0).setValue("new value");
            assertEquals(
                "Sampler didn't clone correctly",
                "new value",
                sampler.getArguments().getArgument(0).getValue());
        }
    }

    /* (non-Javadoc)
     * @see org.apache.jmeter.gui.JMeterGUIComponent#clear()
     */
    public void clear()
    {
        super.clear();
        getImages.setSelected(false);
        urlConfigGui.clear();
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

QDox parse error in file://<WORKSPACE>/data/code-rep-dataset/Dataset5/Tasks/15400.java