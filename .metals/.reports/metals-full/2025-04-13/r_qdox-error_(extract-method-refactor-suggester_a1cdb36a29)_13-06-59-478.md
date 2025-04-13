error id: file://<WORKSPACE>/data/code-rep-dataset/Dataset5/Tasks/4987.java
file://<WORKSPACE>/data/code-rep-dataset/Dataset5/Tasks/4987.java
### com.thoughtworks.qdox.parser.ParseException: syntax error @[1,1]

error in qdox parser
file content:
```java
offset: 1
uri: file://<WORKSPACE>/data/code-rep-dataset/Dataset5/Tasks/4987.java
text:
```scala
i@@f (source instanceof ComboStringEditor) {

/*
 * Licensed to the Apache Software Foundation (ASF) under one or more
 * contributor license agreements.  See the NOTICE file distributed with
 * this work for additional information regarding copyright ownership.
 * The ASF licenses this file to You under the Apache License, Version 2.0
 * (the "License"); you may not use this file except in compliance with
 * the License.  You may obtain a copy of the License at
 *
 * http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS, WITHOUT
 * WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied. See the
 * License for the specific language governing permissions and limitations
 * under the License.
 *
 */

/*
 * Created on May 21, 2004
 */
package org.apache.jmeter.testbeans.gui;

import java.awt.Component;
import java.awt.event.FocusEvent;
import java.awt.event.FocusListener;
import java.beans.PropertyChangeEvent;
import java.beans.PropertyChangeListener;
import java.beans.PropertyEditorSupport;
import java.util.Properties;

import org.apache.jmeter.util.JMeterUtils;
import org.fife.ui.rsyntaxtextarea.RSyntaxTextArea;
import org.fife.ui.rsyntaxtextarea.SyntaxConstants;
import org.fife.ui.rtextarea.RTextScrollPane;

public class TextAreaEditor extends PropertyEditorSupport implements FocusListener, PropertyChangeListener {

    private RSyntaxTextArea textUI;

    private RTextScrollPane scroller;

    private Properties languageProperties;

    /** {@inheritDoc} */
    @Override
    public void focusGained(FocusEvent e) {
    }

    /** {@inheritDoc} */
    @Override
    public void focusLost(FocusEvent e) {
        firePropertyChange();
    }

    private final void init() {// called from ctor, so must not be overridable
        textUI = new RSyntaxTextArea(20, 20);
        textUI.discardAllEdits();
        textUI.setSyntaxEditingStyle(SyntaxConstants.SYNTAX_STYLE_JAVA);
        textUI.setCodeFoldingEnabled(true);
        textUI.setAntiAliasingEnabled(true);
        textUI.addFocusListener(this);
        textUI.setWrapStyleWord(true);
        textUI.setLineWrap(true);
        scroller = new RTextScrollPane(textUI);
        scroller.setFoldIndicatorEnabled(true);
        languageProperties = JMeterUtils.loadProperties("org/apache/jmeter/testbeans/gui/textarea.properties"); //$NON-NLS-1$
    }

    /**
     *
     */
    public TextAreaEditor() {
        super();
        init();
    }

    /**
     * @param source
     */
    public TextAreaEditor(Object source) {
        super(source);
        init();
        setValue(source);
    }

    /** {@inheritDoc} */
    @Override
    public String getAsText() {
        return textUI.getText();
    }

    /** {@inheritDoc} */
    @Override
    public Component getCustomEditor() {
        return scroller;
    }

    /** {@inheritDoc} */
    @Override
    public void setAsText(String text) throws IllegalArgumentException {
        textUI.setText(text);
    }

    /** {@inheritDoc} */
    @Override
    public void setValue(Object value) {
        if (value != null) {
            textUI.setText(value.toString());
        } else {
            textUI.setText("");
        }
    }

    /** {@inheritDoc} */
    @Override
    public Object getValue() {
        return textUI.getText();
    }

    /** {@inheritDoc} */
    @Override
    public boolean supportsCustomEditor() {
        return true;
    }

    @Override
    public void propertyChange(PropertyChangeEvent evt) {
        Object source = evt.getSource();
        if (source instanceof ComboStringEditor && source != null) {
            ComboStringEditor cse = (ComboStringEditor) source;
            String lang = cse.getAsText().toLowerCase();
            if (languageProperties.containsKey(lang)) {
                textUI.setSyntaxEditingStyle(languageProperties.getProperty(lang));
            } else {
                textUI.setSyntaxEditingStyle(SyntaxConstants.SYNTAX_STYLE_NONE);
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

QDox parse error in file://<WORKSPACE>/data/code-rep-dataset/Dataset5/Tasks/4987.java