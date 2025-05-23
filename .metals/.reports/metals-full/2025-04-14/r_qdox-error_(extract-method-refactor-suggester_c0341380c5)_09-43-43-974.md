error id: file://<WORKSPACE>/data/code-rep-dataset/Dataset5/Tasks/2962.java
file://<WORKSPACE>/data/code-rep-dataset/Dataset5/Tasks/2962.java
### com.thoughtworks.qdox.parser.ParseException: syntax error @[1,1]

error in qdox parser
file content:
```java
offset: 1
uri: file://<WORKSPACE>/data/code-rep-dataset/Dataset5/Tasks/2962.java
text:
```scala
t@@rue, true, true);

/*
 * ====================================================================
 * The Apache Software License, Version 1.1
 *
 * Copyright (c) 2003 The Apache Software Foundation.  All rights
 * reserved.
 *
 * Redistribution and use in source and binary forms, with or without
 * modification, are permitted provided that the following conditions
 * are met:
 *
 * 1. Redistributions of source code must retain the above copyright
 * notice, this list of conditions and the following disclaimer.
 *
 * 2. Redistributions in binary form must reproduce the above copyright
 * notice, this list of conditions and the following disclaimer in
 * the documentation and/or other materials provided with the
 * distribution.
 *
 * 3. The end-user documentation included with the redistribution,
 * if any, must include the following acknowledgment:
 * "This product includes software developed by the
 * Apache Software Foundation (http://www.apache.org/)."
 * Alternately, this acknowledgment may appear in the software itself,
 * if and wherever such third-party acknowledgments normally appear.
 *
 * 4. The names "Apache" and "Apache Software Foundation" and
 * "Apache JMeter" must not be used to endorse or promote products
 * derived from this software without prior written permission. For
 * written permission, please contact apache@apache.org.
 *
 * 5. Products derived from this software may not be called "Apache",
 * "Apache JMeter", nor may "Apache" appear in their name, without
 * prior written permission of the Apache Software Foundation.
 *
 * THIS SOFTWARE IS PROVIDED ``AS IS'' AND ANY EXPRESSED OR IMPLIED
 * WARRANTIES, INCLUDING, BUT NOT LIMITED TO, THE IMPLIED WARRANTIES
 * OF MERCHANTABILITY AND FITNESS FOR A PARTICULAR PURPOSE ARE
 * DISCLAIMED.  IN NO EVENT SHALL THE APACHE SOFTWARE FOUNDATION OR
 * ITS CONTRIBUTORS BE LIABLE FOR ANY DIRECT, INDIRECT, INCIDENTAL,
 * SPECIAL, EXEMPLARY, OR CONSEQUENTIAL DAMAGES (INCLUDING, BUT NOT
 * LIMITED TO, PROCUREMENT OF SUBSTITUTE GOODS OR SERVICES; LOSS OF
 * USE, DATA, OR PROFITS; OR BUSINESS INTERRUPTION) HOWEVER CAUSED AND
 * ON ANY THEORY OF LIABILITY, WHETHER IN CONTRACT, STRICT LIABILITY,
 * OR TORT (INCLUDING NEGLIGENCE OR OTHERWISE) ARISING IN ANY WAY OUT
 * OF THE USE OF THIS SOFTWARE, EVEN IF ADVISED OF THE POSSIBILITY OF
 * SUCH DAMAGE.
 * ====================================================================
 *
 * This software consists of voluntary contributions made by many
 * individuals on behalf of the Apache Software Foundation.  For more
 * information on the Apache Software Foundation, please see
 * <http://www.apache.org/>.
 * 
 * @author <a href="mailto:jsalvata@apache.org">Jordi Salvat i Alabart</a>
 * @version $Id$
 */
package org.apache.jmeter.testbeans.gui;

import java.awt.BorderLayout;
import java.awt.Component;
import java.awt.Graphics;
import java.awt.Rectangle;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.beans.PropertyChangeListener;
import java.beans.PropertyEditor;
import java.beans.PropertyEditorSupport;
import java.io.File;

import javax.swing.JButton;
import javax.swing.JFileChooser;
import javax.swing.JPanel;

import org.apache.jmeter.gui.util.FileDialoger;
import org.apache.jorphan.logging.LoggingManager;
import org.apache.log.Logger;

/**
 * A property editor for File properties.
 * <p>
 * Note that it never gives out File objects, but always Strings. This is
 * because JMeter is now too dumb to handle File objects (there's no
 * FileProperty).
 */
public class FileEditor implements PropertyEditor, ActionListener
{
	protected static Logger log= LoggingManager.getLoggerForClass();

    /**
	 * The editor's panel.
	 */
	private JPanel panel;

	/**
	 * The editor handling the text field inside:
	 */
	PropertyEditor editor;

    public FileEditor()
    {
		// Create a button to trigger the file chooser:
		JButton button= new JButton("Browse...");
		button.addActionListener(this);

		// Get a WrapperEditor to provide the field or combo:
		editor= new WrapperEditor(
			new SimpleFileEditor(),
			new ComboStringEditor(),
			true, true);

		// Create a panel containing the combo and the button:
		panel= new JPanel(new BorderLayout(5,0));
		panel.add(editor.getCustomEditor(), BorderLayout.CENTER);
		panel.add(button, BorderLayout.EAST);//JDK1.4: was LINE_END
    }

    /* (non-Javadoc)
     * @see java.awt.event.ActionListener#actionPerformed(java.awt.event.ActionEvent)
     */
    public void actionPerformed(ActionEvent e)
    {
		JFileChooser chooser = FileDialoger.promptToOpenFile();

		File file = chooser.getSelectedFile();

		setValue(file);
    }

    /**
     * @param listener
     */
    public void addPropertyChangeListener(PropertyChangeListener listener)
    {
        editor.addPropertyChangeListener(listener);
    }

    /**
     * @return
     */
    public String getAsText()
    {
        return editor.getAsText();
    }

    /**
     * @return
     */
    public Component getCustomEditor()
    {
        return panel;
    }

    /**
     * @return
     */
    public String getJavaInitializationString()
    {
        return editor.getJavaInitializationString();
    }

    /**
     * @return
     */
    public String[] getTags()
    {
        return editor.getTags();
    }

    /**
     * @return
     */
    public Object getValue()
    {
        return editor.getValue();
    }

    /**
     * @return
     */
    public boolean isPaintable()
    {
        return editor.isPaintable();
    }

    /**
     * @param gfx
     * @param box
     */
    public void paintValue(Graphics gfx, Rectangle box)
    {
        editor.paintValue(gfx, box);
    }

    /**
     * @param listener
     */
    public void removePropertyChangeListener(PropertyChangeListener listener)
    {
        editor.removePropertyChangeListener(listener);
    }

    /**
     * @param text
     * @throws java.lang.IllegalArgumentException
     */
    public void setAsText(String text) throws IllegalArgumentException
    {
        editor.setAsText(text);
    }

    /**
     * @param value
     */
    public void setValue(Object value)
    {
        editor.setValue(value);
    }

    /**
     * @return
     */
    public boolean supportsCustomEditor()
    {
        return editor.supportsCustomEditor();
    }

	private static class SimpleFileEditor extends PropertyEditorSupport
	{
		/* (non-Javadoc)
		 * @see java.beans.PropertyEditor#getAsText()
		 */
		public String getAsText()
		{
			return ((File)super.getValue()).getPath();
		}

		/* (non-Javadoc)
		 * @see java.beans.PropertyEditor#setAsText(java.lang.String)
		 */
		public void setAsText(String text) throws IllegalArgumentException
		{
			super.setValue(new File(text));
		}
        
		/*
		 * Oh, I forgot: JMeter doesn't support File properties yet. Need to work
		 * on this as a String :-(
		 */
		public Object getValue()
		{
			return getAsText(); // should be super.getValue();
		}
		
		/**
		 * Tsk, tsk... I need to handle Strings when setting too.
		 */
		public void setValue(Object file)
		{
			setAsText((String)file);
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

QDox parse error in file://<WORKSPACE>/data/code-rep-dataset/Dataset5/Tasks/2962.java