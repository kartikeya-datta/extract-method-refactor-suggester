error id: file://<WORKSPACE>/data/code-rep-dataset/Dataset5/Tasks/17200.java
file://<WORKSPACE>/data/code-rep-dataset/Dataset5/Tasks/17200.java
### com.thoughtworks.qdox.parser.ParseException: syntax error @[1,1]

error in qdox parser
file content:
```java
offset: 1
uri: file://<WORKSPACE>/data/code-rep-dataset/Dataset5/Tasks/17200.java
text:
```scala
t@@hrow new IllegalArgumentException("Descriptor must not be null");

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
 */
package org.apache.jmeter.testbeans.gui;

import java.awt.BorderLayout;
import java.awt.Component;
import java.awt.Graphics;
import java.awt.Rectangle;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.beans.IntrospectionException;
import java.beans.PropertyChangeListener;
import java.beans.PropertyDescriptor;
import java.beans.PropertyEditor;
import java.beans.PropertyEditorSupport;
import java.io.File;

import javax.swing.JButton;
import javax.swing.JFileChooser;
import javax.swing.JPanel;

import org.apache.jmeter.gui.util.FileDialoger;

/**
 * A property editor for File properties.
 * <p>
 * Note that it never gives out File objects, but always Strings. This is
 * because JMeter is now too dumb to handle File objects (there's no
 * FileProperty).
 *
 */
public class FileEditor implements PropertyEditor, ActionListener {

    /**
     * The editor's panel.
     */
    private final JPanel panel;

    /**
     * The editor handling the text field inside:
     */
    private final PropertyEditor editor;

    /**
     * @throws IntrospectionException 
     * @deprecated Only for use by test cases
     */
    @Deprecated
    public FileEditor() throws IntrospectionException {
        this(new PropertyDescriptor("dummy", null, null));
    }

    public FileEditor(PropertyDescriptor descriptor) {
        if (descriptor == null) {
            throw new NullPointerException("Descriptor must not be null");
        }

        // Create a button to trigger the file chooser:
        JButton button = new JButton("Browse...");
        button.addActionListener(this);

        // Get a WrapperEditor to provide the field or combo -- we'll delegate
        // most methods to it:
        boolean notNull = GenericTestBeanCustomizer.notNull(descriptor);
        boolean notExpression = GenericTestBeanCustomizer.notExpression(descriptor);
        boolean notOther = GenericTestBeanCustomizer.notOther(descriptor);
        Object defaultValue = descriptor.getValue(GenericTestBeanCustomizer.DEFAULT);
        ComboStringEditor cse = new ComboStringEditor();
        cse.setNoUndefined(notNull);
        cse.setNoEdit(notExpression && notOther);
        editor = new WrapperEditor(this, new SimpleFileEditor(), cse,
                !notNull, // acceptsNull
                !notExpression, // acceptsExpressions
                !notOther, // acceptsOther
                defaultValue); // default

        // Create a panel containing the combo and the button:
        panel = new JPanel(new BorderLayout(5, 0));
        panel.add(editor.getCustomEditor(), BorderLayout.CENTER);
        panel.add(button, BorderLayout.EAST);
    }

    /**
     * {@inheritDoc}
     */
    public void actionPerformed(ActionEvent e) {
        JFileChooser chooser = FileDialoger.promptToOpenFile();

        if (chooser == null){
            return;
        }

        setValue(chooser.getSelectedFile().getPath());
    }

    /**
     * @param listener
     */
    public void addPropertyChangeListener(PropertyChangeListener listener) {
        editor.addPropertyChangeListener(listener);
    }

    /**
     * @return the text
     */
    public String getAsText() {
        return editor.getAsText();
    }

    /**
     * @return custom editor panel
     */
    public Component getCustomEditor() {
        return panel;
    }

    /**
     * @return the Java initialisation string
     */
    public String getJavaInitializationString() {
        return editor.getJavaInitializationString();
    }

    /**
     * @return the editor tags
     */
    public String[] getTags() {
        return editor.getTags();
    }

    /**
     * @return the value
     */
    public Object getValue() {
        return editor.getValue();
    }

    /**
     * @return true if the editor is paintable
     */
    public boolean isPaintable() {
        return editor.isPaintable();
    }

    /**
     * @param gfx
     * @param box
     */
    public void paintValue(Graphics gfx, Rectangle box) {
        editor.paintValue(gfx, box);
    }

    /**
     * @param listener
     */
    public void removePropertyChangeListener(PropertyChangeListener listener) {
        editor.removePropertyChangeListener(listener);
    }

    /**
     * @param text
     * @throws java.lang.IllegalArgumentException
     */
    public void setAsText(String text) throws IllegalArgumentException {
        editor.setAsText(text);
    }

    /**
     * @param value
     */
    public void setValue(Object value) {
        editor.setValue(value);
    }

    /**
     * @return true if supports a custom editor
     */
    public boolean supportsCustomEditor() {
        return editor.supportsCustomEditor();
    }

    private static class SimpleFileEditor extends PropertyEditorSupport {

        @Override
        public String getAsText() {
            Object value = super.getValue();
            if (value instanceof File) {
                return ((File) value).getPath();
            }
            return (String) value; // assume it's string
        }

        @Override
        public void setAsText(String text) throws IllegalArgumentException {
            super.setValue(new File(text));
        }

        @Override
        public Object getValue() {
            return super.getValue();
        }

        @Override
        public void setValue(Object file) {
            super.setValue(file);
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

QDox parse error in file://<WORKSPACE>/data/code-rep-dataset/Dataset5/Tasks/17200.java