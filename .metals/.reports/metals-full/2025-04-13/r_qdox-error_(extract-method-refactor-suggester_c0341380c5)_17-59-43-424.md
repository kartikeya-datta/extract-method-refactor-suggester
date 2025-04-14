error id: file://<WORKSPACE>/data/code-rep-dataset/Dataset5/Tasks/10578.java
file://<WORKSPACE>/data/code-rep-dataset/Dataset5/Tasks/10578.java
### com.thoughtworks.qdox.parser.ParseException: syntax error @[1,11]

error in qdox parser
file content:
```java
offset: 11
uri: file://<WORKSPACE>/data/code-rep-dataset/Dataset5/Tasks/10578.java
text:
```scala
protected R@@esultCollector collector;

// $Header$
/*
 * Copyright 2000-2004 The Apache Software Foundation.
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
import org.apache.jorphan.logging.LoggingManager;
import org.apache.log.Logger;

/**
 * This is the base class for JMeter GUI components which can display test
 * results in some way.  It provides the following conveniences to developers:
 * <ul>
 *   <li>Implements the
 *     {@link org.apache.jmeter.gui.JMeterGUIComponent JMeterGUIComponent}
 *     interface that allows your Gui visualizer to "plug-in" to the JMeter
 *     GUI environment.  Provides implementations for the following methods:
 *     <ul>
 *       <li>{@link org.apache.jmeter.gui.JMeterGUIComponent#configure(TestElement) configure(TestElement)}.
 *         Any additional parameters of your Visualizer need to be handled by
 *         you.</li>
 *       <li>{@link org.apache.jmeter.gui.JMeterGUIComponent#createTestElement() createTestElement()}.
 *         For most purposes, the default
 *         {@link org.apache.jmeter.reporters.ResultCollector ResultCollector}
 *         created by this method is sufficient.</li>
 *       <li>{@link org.apache.jmeter.gui.JMeterGUIComponent#getMenuCategories getMenuCategories()}.
 *         To control where in the GUI your visualizer can be added.</li>
 *       <li>{@link org.apache.jmeter.gui.JMeterGUIComponent#modifyTestElement(TestElement) modifyTestElement(TestElement)}.
 *         Again, additional parameters you require have to be handled by
 *         you.</li>
 *       <li>{@link org.apache.jmeter.gui.JMeterGUIComponent#createPopupMenu() createPopupMenu()}.</li>
 *     </ul></li>
 *   <li>Provides convenience methods to help you make a JMeter-compatible GUI:
 *     <ul>
 *       <li>{@link #makeTitlePanel()}.  Returns a panel that includes the name
 *         of the component, and a FilePanel that allows users to control what
 *         file samples are logged to.</li>
 *       <li>{@link #getModel()} and {@link #setModel(ResultCollector)} methods
 *         for setting and getting the model class that handles the receiving
 *         and logging of sample results.</li>
 *     </ul></li>
 * </ul>
 * For most developers, making a new visualizer is primarly for the purpose of
 * either calculating new statistics on the sample results that other
 * visualizers don't calculate, or displaying the results visually in a new and
 * interesting way.  Making a new visualizer for either of these purposes is
 * easy - just extend this class and implement the
 * {@link org.apache.jmeter.visualizers.Visualizer#add(SampleResult) add(SampleResult)}
 * method and display the results as you see fit.  This AbstractVisualizer and
 * the default {@link org.apache.jmeter.reporters.ResultCollector ResultCollector}
 * handle logging and registering to receive SampleEvents for you - all you need
 * to do is include the JPanel created by makeTitlePanel somewhere in your gui
 * to allow users set the log file.
 * <p>
 * If you are doing more than that, you may need to extend
 * {@link org.apache.jmeter.reporters.ResultCollector ResultCollector} as well
 * and modify the {@link #configure(TestElement)},
 * {@link #modifyTestElement(TestElement)}, and {@link #createTestElement()}
 * methods to create and modify your alternate ResultCollector.  For an example
 * of this, see the
 * {@link org.apache.jmeter.visualizers.MailerVisualizer MailerVisualizer}.
 * <p>
 *
 * @author    Michael Stover
 * @version   $Revision$
 */
public abstract class AbstractVisualizer
    extends AbstractJMeterGuiComponent
    implements Visualizer, ChangeListener, UnsharedComponent
{
    /** Logging. */
    protected static transient Logger log =LoggingManager.getLoggerForClass();

    /** A panel allowing results to be saved. */
    private FilePanel filePanel;
    
    /** A checkbox choosing whether or not only errors should be logged. */
    private JCheckBox errorLogging;
    
    ResultCollector collector;

    public AbstractVisualizer()
    {
        super();
        
        errorLogging =
            new JCheckBox(JMeterUtils.getResString("log_errors_only"));

        filePanel = new FilePanel(
                JMeterUtils.getResString("file_visualizer_output_file"),".jtl");
        filePanel.addChangeListener(this);
        filePanel.add(errorLogging);
                
    }

    /**
     * Gets the checkbox which selects whether or not only errors should be
     * logged.  Subclasses don't normally need to worry about this checkbox,
     * because it is automatically added to the GUI in
     * {@link #makeTitlePanel()}, and the behavior is handled in this base
     * class.
     * 
     * @return the error logging checkbox
     */
    protected JCheckBox getErrorLoggingCheckbox()
    {
        return errorLogging;
    }

    /**
     * Provides access to the ResultCollector model class for extending
     * implementations.  Using this method and setModel(ResultCollector) is
     * only necessary if your visualizer requires a differently behaving
     * ResultCollector.  Using these methods will allow maximum reuse of the
     * methods provided by AbstractVisualizer in this event.
     */
    protected ResultCollector getModel()
    {
        return collector;
    }

   /**
    * Gets the file panel which allows the user to save results to a file.
    * Subclasses don't normally need to worry about this panel, because it
    * is automatically added to the GUI in {@link #makeTitlePanel()}, and the
    * behavior is handled in this base class.
    * 
    * @return the file panel allowing users to save results
    */
    protected Component getFilePanel()
    {
        return filePanel;
    }

    /**
     * Sets the filename which results will be saved to.  This will set the
     * filename in the FilePanel.  Subclasses don't normally need to call this
     * method, because configuration of the FilePanel is handled in this base
     * class.
     * 
     * @param filename the new filename
     * 
     * @see #getFilePanel()
     */
    public void setFile(String filename)
    {
        // TODO: Does this method need to be public?  It isn't currently
        // called outside of this class.
        filePanel.setFilename(filename);
    }

    /**
     * Gets the filename which has been entered in the FilePanel.  Subclasses
     * don't normally need to call this method, because configuration of the
     * FilePanel is handled in this base class.
     * 
     * @return the current filename
     * 
     * @see #getFilePanel()
     */
    public String getFile()
    {
        // TODO: Does this method need to be public?  It isn't currently
        // called outside of this class.
        return filePanel.getFilename();
    }
    
    

    /**
     * When a user right-clicks on the component in the test tree, or
     * selects the edit menu when the component is selected, the 
     * component will be asked to return a JPopupMenu that provides
     * all the options available to the user from this component.
     * <p>
     * This implementation returns menu items appropriate for most
     * visualizer components.
     *
     * @return   a JPopupMenu appropriate for the component.
     */
    public JPopupMenu createPopupMenu()
    {
        return MenuFactory.getDefaultVisualizerMenu();
    }

    /**
     * Invoked when the target of the listener has changed its state.  This
     * implementation assumes that the target is the FilePanel, and will
     * update the result collector for the new filename.
     * 
     * @param e the event that has occurred
     */
    public void stateChanged(ChangeEvent e)
    {
        log.info("getting new collector");
        collector = (ResultCollector) createTestElement();
        try
        {
            collector.loadExistingFile();
        }
        catch (Exception err)
        {
            log.debug("Error occurred while loading file", err);
        }
    }

    /**
     * This is the list of menu categories this gui component will be available
     * under. This implementation returns
     * {@link org.apache.jmeter.gui.util.MenuFactory#LISTENERS}, which
     * is appropriate for most visualizer components.
     *
     * @return   a Collection of Strings, where each element is one of the
     *           constants defined in MenuFactory
     */
    public Collection getMenuCategories()
    {
        return Arrays.asList(new String[] { MenuFactory.LISTENERS });
    }

    /* Implements JMeterGUIComponent.modifyTestElement(TestElement) */
    public TestElement createTestElement()
    {
        if (collector == null)
        {
            collector = new ResultCollector();
        }
        modifyTestElement(collector);
        return (TestElement) collector.clone();
    }

    /* Implements JMeterGUIComponent.modifyTestElement(TestElement) */
    public void modifyTestElement(TestElement c)
    {
        configureTestElement((AbstractListenerElement) c);
        if (c instanceof ResultCollector)
        {
            ResultCollector rc = (ResultCollector) c;
            rc.setErrorLogging(errorLogging.isSelected());
            rc.setFilename(getFile());
            collector = rc;
        }
    }

    /* Overrides AbstractJMeterGuiComponent.configure(TestElement) */
    public void configure(TestElement el)
    {
        super.configure(el);
        setFile(el.getPropertyAsString(ResultCollector.FILENAME));
        ResultCollector rc = (ResultCollector) el;
        errorLogging.setSelected(rc.isErrorLogging());
    }

    /**
     * This provides a convenience for extenders when they implement the
     * {@link org.apache.jmeter.gui.JMeterGUIComponent#createTestElement()} method.  This method
     * will set the name, gui class, and test class for the created Test
     * Element.  It should be called by every extending class when creating
     * Test Elements, as that will best assure consistent behavior.
     * 
     * @param mc  the TestElement being created.
     */
    protected void configureTestElement(AbstractListenerElement mc)
    {
        // TODO: Should the method signature of this method be changed to
        // match the super-implementation (using a TestElement parameter
        // instead of AbstractListenerElement)?  This would require an
        // instanceof check before adding the listener (below), but would
        // also make the behavior a bit more obvious for sub-classes -- the
        // Java rules dealing with this situation aren't always intuitive,
        // and a subclass may think it is calling this version of the method
        // when it is really calling the superclass version instead.
        super.configureTestElement(mc);
        mc.setListener(this);
    }
    
    /**
     * Create a standard title section for JMeter components.  This includes
     * the title for the component and the Name Panel allowing the user to
     * change the name for the component.  The AbstractVisualizer also adds
     * the FilePanel allowing the user to save the results, and the
     * error logging checkbox, allowing the user to choose whether or not only
     * errors should be logged.
     * <p>  
     * This method is typically added to the top of the component at the
     * beginning of the component's init method.
     * 
     * @return a panel containing the component title, name panel, file panel,
     *         and error logging checkbox
     */
    protected Container makeTitlePanel()
    {
        Container panel = super.makeTitlePanel();
        // Note: the file panel already includes the error logging checkbox,
        // so we don't have to add it explicitly.
        panel.add(getFilePanel());
        return panel;
    }

    /**
     * Provides extending classes the opportunity to set the ResultCollector
     * model for the Visualizer.  This is useful to allow maximum reuse of the
     * methods from AbstractVisualizer.
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
	java.base/java.util.concurrent.ForkJoinPool$WorkQueue.topLevelExec(ForkJoinPool.java:1182)
	java.base/java.util.concurrent.ForkJoinPool.scan(ForkJoinPool.java:1655)
	java.base/java.util.concurrent.ForkJoinPool.runWorker(ForkJoinPool.java:1622)
	java.base/java.util.concurrent.ForkJoinWorkerThread.run(ForkJoinWorkerThread.java:165)
```
#### Short summary: 

QDox parse error in file://<WORKSPACE>/data/code-rep-dataset/Dataset5/Tasks/10578.java