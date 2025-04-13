error id: file://<WORKSPACE>/data/code-rep-dataset/Dataset5/Tasks/18051.java
file://<WORKSPACE>/data/code-rep-dataset/Dataset5/Tasks/18051.java
### com.thoughtworks.qdox.parser.ParseException: syntax error @[1,1]

error in qdox parser
file content:
```java
offset: 1
uri: file://<WORKSPACE>/data/code-rep-dataset/Dataset5/Tasks/18051.java
text:
```scala
v@@oid clearGui();

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

package org.apache.jmeter.gui;

import java.util.Collection;

import javax.swing.JPopupMenu;

import org.apache.jmeter.testelement.TestElement;

/**
 * Implementing this interface indicates that the class is a JMeter GUI
 * Component. A JMeter GUI Component is essentially the GUI display code
 * associated with a JMeter Test Element. The writer of the component must take
 * care to make the component be consistent with the rest of JMeter's GUI look
 * and feel and behavior. Use of the provided abstract classes is highly
 * recommended to make this task easier.
 *
 * @see AbstractJMeterGuiComponent
 * @see org.apache.jmeter.config.gui.AbstractConfigGui
 * @see org.apache.jmeter.assertions.gui.AbstractAssertionGui
 * @see org.apache.jmeter.control.gui.AbstractControllerGui
 * @see org.apache.jmeter.timers.gui.AbstractTimerGui
 * @see org.apache.jmeter.visualizers.gui.AbstractVisualizer
 * @see org.apache.jmeter.samplers.gui.AbstractSamplerGui
 *
 */

public interface JMeterGUIComponent {

    /**
     * Sets the name of the JMeter GUI Component. The name of the component is
     * used in the Test Tree as the name of the tree node.
     *
     * @param name
     *            the name of the component
     */
    void setName(String name);

    /**
     * Gets the name of the JMeter GUI component. The name of the component is
     * used in the Test Tree as the name of the tree node.
     *
     * @return the name of the component
     */
    String getName();

    /**
     * Get the component's label. This label is used in drop down lists that
     * give the user the option of choosing one type of component in a list of
     * many. It should therefore be a descriptive name for the end user to see.
     * It must be unique to the class.
     *
     * It is also used by Help to find the appropriate location in the
     * documentation.
     *
     * Normally getLabelResource() should be overridden instead of
     * this method; the definition of this method in AbstractJMeterGuiComponent
     * is intended for general use.
     *
     * @see #getLabelResource()
     * @return GUI label for the component.
     */
    String getStaticLabel();

    /**
     * Get the component's resource name, which getStaticLabel uses to derive
     * the component's label in the local language. The resource name is fixed,
     * and does not vary with the selected language.
     *
     * Normally this method should be overriden in preference to overriding
     * getStaticLabel(). However where the resource name is not available or required,
     * getStaticLabel() may be overridden instead.
     *
     * @return the resource name
     */
    String getLabelResource();

    /**
     * Get the component's document anchor name. Used by Help to find the
     * appropriate location in the documentation
     *
     * @return Document anchor (#ref) for the component.
     */
    String getDocAnchor();

    /**
     * JMeter test components are separated into a model and a GUI
     * representation. The model holds the data and the GUI displays it. The GUI
     * class is responsible for knowing how to create and initialize with data
     * the model class that it knows how to display, and this method is called
     * when new test elements are created.
     *
     * @return the Test Element object that the GUI component represents.
     */
    TestElement createTestElement();

    /**
     * GUI components are responsible for populating TestElements they create
     * with the data currently held in the GUI components. This method should
     * overwrite whatever data is currently in the TestElement as it is called
     * after a user has filled out the form elements in the gui with new
     * information.
     *
     * @param element
     *            the TestElement to modify
     */
    void modifyTestElement(TestElement element);

    /**
     * Test GUI elements can be disabled, in which case they do not become part
     * of the test when run.
     *
     * @return true if the element should be part of the test run, false
     *         otherwise
     */
    boolean isEnabled();

    /**
     * Set whether this component is enabled.
     *
     * @param enabled
     *            true for enabled, false for disabled.
     */
    void setEnabled(boolean enabled);

    /**
     * When a user right-clicks on the component in the test tree, or selects
     * the edit menu when the component is selected, the component will be asked
     * to return a JPopupMenu that provides all the options available to the
     * user from this component.
     *
     * @return a JPopupMenu appropriate for the component.
     */
    JPopupMenu createPopupMenu();

    /**
     * The GUI must be able to extract the data from the TestElement and update
     * all GUI fields to represent those data. This method is called to allow
     * JMeter to show the user the GUI that represents the test element's data.
     *
     * @param element
     *            the TestElement to configure
     */
    void configure(TestElement element);

    /**
     * This is the list of add menu categories this gui component will be
     * available under. For instance, if this represents a Controller, then the
     * MenuFactory.CONTROLLERS category should be in the returned collection.
     * When a user right-clicks on a tree element and looks through the "add"
     * menu, which category your GUI component shows up in is determined by
     * which categories are returned by this method. Most GUI's belong to only
     * one category, but it is possible for a component to exist in multiple
     * categories.
     *
     * @return a Collection of Strings, where each element is one of the
     *         constants defined in MenuFactory
     *
     * @see org.apache.jmeter.gui.util.MenuFactory
     */
    Collection<String> getMenuCategories();

    /**
     * Clear the gui and return it to initial default values. This is necessary
     * because most gui classes are instantiated just once and re-used for
     * multiple test element objects and thus they need to be cleared between
     * use.
     */
    public void clearGui();
    // N.B. originally called clear()
    // @see also Clearable
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

QDox parse error in file://<WORKSPACE>/data/code-rep-dataset/Dataset5/Tasks/18051.java