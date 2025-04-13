error id: file://<WORKSPACE>/data/code-rep-dataset/Dataset5/Tasks/9107.java
file://<WORKSPACE>/data/code-rep-dataset/Dataset5/Tasks/9107.java
### com.thoughtworks.qdox.parser.ParseException: syntax error @[1,28]

error in qdox parser
file content:
```java
offset: 28
uri: file://<WORKSPACE>/data/code-rep-dataset/Dataset5/Tasks/9107.java
text:
```scala
transient protected static L@@ogger log = Hierarchy.getDefaultHierarchy().getLoggerFor("jmeter.gui");

package org.apache.jmeter.visualizers.gui;

import java.awt.Component;
import java.awt.Container;
import java.util.Arrays;
import java.util.Collection;

import javax.swing.JCheckBox;
import javax.swing.JPopupMenu;
import javax.swing.event.ChangeEvent;
import javax.swing.event.ChangeListener;

import org.apache.jmeter.gui.AbstractJMeterGuiComponent;
import org.apache.jmeter.gui.UnsharedComponent;
import org.apache.jmeter.gui.util.FilePanel;
import org.apache.jmeter.gui.util.MenuFactory;
import org.apache.jmeter.reporters.AbstractListenerElement;
import org.apache.jmeter.reporters.ResultCollector;
import org.apache.jmeter.testelement.TestElement;
import org.apache.jmeter.util.JMeterUtils;
import org.apache.jmeter.visualizers.Visualizer;
import org.apache.log.Hierarchy;
import org.apache.log.Logger;

/****************************************
 * Title: JMeter Description: Copyright: Copyright (c) 2000 Company: Apache
 *
 *@author    Michael Stover
 *@created   $Date$
 *@version   1.0
 ***************************************/

public abstract class AbstractVisualizer extends AbstractJMeterGuiComponent implements Visualizer, ChangeListener, UnsharedComponent
{

    transient private static Logger log = Hierarchy.getDefaultHierarchy().getLoggerFor("jmeter.gui");

    private FilePanel filePanel;
    private JCheckBox errorLogging;
    ResultCollector collector;

    /****************************************
     * !ToDo (Constructor description)
     ***************************************/
    public AbstractVisualizer()
    {
        super();
        filePanel = new FilePanel(this, JMeterUtils.getResString("file_visualizer_output_file"));
        errorLogging = new JCheckBox(JMeterUtils.getResString("log_errors_only"));
    }

    protected JCheckBox getErrorLoggingCheckbox()
    {
        return errorLogging;
    }

    protected ResultCollector getModel()
    {
        return collector;
    }

    protected Component getFilePanel()
    {
        filePanel.add(getErrorLoggingCheckbox());
        //filePanel.add(Box.createHorizontalGlue());
        return filePanel;
    }

    public void setFile(String filename)
    {
        filePanel.setFilename(filename);
    }

    public String getFile()
    {
        return filePanel.getFilename();
    }
    
    

    /****************************************
     * !ToDo (Method description)
     *
     *@return   !ToDo (Return description)
     ***************************************/
    public JPopupMenu createPopupMenu()
    {
        return MenuFactory.getDefaultVisualizerMenu();
    }

    public void stateChanged(ChangeEvent e)
    {
        log.info("getting new collector");
        collector = (ResultCollector) createTestElement();
        try
        {
            collector.loadExistingFile();
        }
        catch(Exception err)
        {
            log.debug("Error occurred while loading file",err);
        }
    }

    /****************************************
     * !ToDoo (Method description)
     *
     *@return   !ToDo (Return description)
     ***************************************/
    public Collection getMenuCategories()
    {
        return Arrays.asList(new String[] { MenuFactory.LISTENERS });
    }

    public TestElement createTestElement()
    {
        if (collector == null)
        {
            collector = new ResultCollector();
        }
        modifyTestElement(collector);
        return (TestElement) collector.clone();
    }

    /**
     * Modifies a given TestElement to mirror the data in the gui components.
     * @see org.apache.jmeter.gui.JMeterGUIComponent#modifyTestElement(TestElement)
     */
    public void modifyTestElement(TestElement c)
    {
        configureTestElement((AbstractListenerElement)c);
        if (c instanceof ResultCollector)
        {
            ResultCollector rc = (ResultCollector) c;
            rc.setErrorLogging(errorLogging.isSelected());
            rc.setFilename(getFile());
            collector = rc;
        }
    }

    public void configure(TestElement el)
    {
        super.configure(el);
        setFile(el.getPropertyAsString(ResultCollector.FILENAME));
        ResultCollector rc = (ResultCollector) el;
        errorLogging.setSelected(rc.isErrorLogging());
    }

    /****************************************
     * !ToDo (Method description)
     *
     *@param mc  !ToDo (Parameter description)
     ***************************************/
    protected void configureTestElement(AbstractListenerElement mc)
    {
        super.configureTestElement(mc);
        mc.setListener(this);
    }
    /**
     * override parent method to add the file panel to the title panel.
     */
    protected Container makeTitlePanel()
    {
        Container box = super.makeTitlePanel();
        box.add(getFilePanel());
        return box;
    }

    /**
     * @param collector
     */
    protected void setModel(ResultCollector collector)
    {
        this.collector = collector;
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

QDox parse error in file://<WORKSPACE>/data/code-rep-dataset/Dataset5/Tasks/9107.java