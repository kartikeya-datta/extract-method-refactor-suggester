error id: file://<WORKSPACE>/data/code-rep-dataset/Dataset5/Tasks/5414.java
file://<WORKSPACE>/data/code-rep-dataset/Dataset5/Tasks/5414.java
### com.thoughtworks.qdox.parser.ParseException: syntax error @[1,1]

error in qdox parser
file content:
```java
offset: 1
uri: file://<WORKSPACE>/data/code-rep-dataset/Dataset5/Tasks/5414.java
text:
```scala
c@@hoiceList.setEditable(false);//TODO - is this correct?

/*
 * ====================================================================
 * The Apache Software License, Version 1.1
 *
 * Copyright (c) 2001 The Apache Software Foundation.  All rights
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
 */
package org.apache.jorphan.gui;

import java.awt.Insets;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.ItemEvent;
import java.awt.event.ItemListener;
import java.util.ArrayList;
import java.util.LinkedList;
import java.util.List;

import javax.swing.BorderFactory;
import javax.swing.JButton;
import javax.swing.JComboBox;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.event.ChangeEvent;
import javax.swing.event.ChangeListener;

public class JLabeledChoice extends JPanel implements JLabeledField
{
    private JLabel mLabel = new JLabel();
    private JComboBox choiceList;
    
    // Maybe move to vector if MT problems occur
    private ArrayList mChangeListeners = new ArrayList(3);
    
    private JButton delete, add;

    /**
     * Default constructor, The label and the Text field are left empty.
     */
    public JLabeledChoice()
    {
        super();
        choiceList = new JComboBox();
        init();
    }

    public List getComponentList()
    {
        List comps = new LinkedList();
        comps.add(mLabel);
        comps.add(choiceList);
        return comps;
    }

    public JLabeledChoice(String pLabel, boolean editable)
    {
        super();
        choiceList = new JComboBox();
        mLabel.setText(pLabel);
        choiceList.setEditable(editable);
        init();
    }

    public void setEditable(boolean editable)
    {
        choiceList.setEditable(false);
    }

    public void addValue(String item)
    {
        choiceList.addItem(item);
    }

    public void setValues(String[] items)
    {
        choiceList.removeAllItems();
        for (int i = 0; i < items.length; i++)
        {
            choiceList.addItem(items[i]);
        }
    }

    /**
     * Constructs a new component with the label displaying the
     * passed text.
     *
     * @param pLabel The text to in the label.
     */
    public JLabeledChoice(String pLabel, String[] items)
    {
        super();
        mLabel.setText(pLabel);
        choiceList = new JComboBox(items);
        choiceList.setEditable(false);
        init();
    }

    public JLabeledChoice(String pLabel, String[] items, boolean editable)
    {
        super();
        mLabel.setText(pLabel);
        choiceList = new JComboBox(items);
        choiceList.setEditable(editable);
        init();
    }

    /**
     * Initialises all of the components on this panel.
     */
    private void init()
    {
        /*if(choiceList.isEditable())
        {
            choiceList.addActionListener(new ComboListener());
        }*/
        choiceList.setBorder(BorderFactory.createLoweredBevelBorder());
        // Register the handler for focus listening. This handler will
        // only notify the registered when the text changes from when
        // the focus is gained to when it is lost.
        choiceList.addItemListener(new ItemListener()
        {
            /**
             * Callback method when the focus to the Text Field component
             * is lost.
             *
             * @param pFocusEvent The focus event that occured.
             */
            public void itemStateChanged(ItemEvent e)
            {
                if (e.getStateChange() == ItemEvent.SELECTED)
                {
                    notifyChangeListeners();
                }
            }
        });

        // Add the sub components
        this.add(mLabel);
        this.add(choiceList);
        if (choiceList.isEditable())
        {
            add = new JButton("Add");
            add.setMargin(new Insets(1, 1, 1, 1));
            add.addActionListener(new AddListener());
            this.add(add);
            delete = new JButton("Del");
            delete.setMargin(new Insets(1, 1, 1, 1));
            delete.addActionListener(new DeleteListener());
            this.add(delete);
        }

    }

    /**
     * Set the text displayed in the label.
     *
     * @param pLabel The new label text.
     */
    public void setLabel(String pLabel)
    {
        mLabel.setText(pLabel);
    }

    /**
     * Set the text displayed in the Text Field.
     *
     * @param pText The new text to display in the text field.
     */
    public void setText(String pText)
    {
        choiceList.setSelectedItem(pText);
    }

    /**
     * Returns the text in the Text Field.
     *
     * @return The text in the Text Field.
     */
    public String getText()
    {
        return (String) choiceList.getSelectedItem();
    }

    public Object[] getSelectedItems()
    {
        return choiceList.getSelectedObjects();
    }

    public String[] getItems()
    {
        String[] items = new String[choiceList.getItemCount()];
        for (int i = 0; i < items.length; i++)
        {
            items[i] = (String) choiceList.getItemAt(i);
        }
        return items;
    }

    /**
     * Returns the text of the label.
     *
     * @return The text of the label.
     */
    public String getLabel()
    {
        return mLabel.getText();
    }

    /**
     * Adds a change listener, that will be notified when the text in the
     * text field is changed. The ChangeEvent that will be passed
     * to registered listeners will contain this object as the source, allowing
     * the new text to be extracted using the {@link #getText() getText} method.
     *
     * @param pChangeListener The listener to add
     */
    public void addChangeListener(ChangeListener pChangeListener)
    {
        mChangeListeners.add(pChangeListener);
    }

    /**
     * Removes a change listener.
     *
     * @param pChangeListener The change listener to remove.
     */
    public void removeChangeListener(ChangeListener pChangeListener)
    {
        mChangeListeners.remove(pChangeListener);
    }

    /**
     * Notify all registered change listeners that the
     * text in the text field has changed.
     */
    private void notifyChangeListeners()
    {
        ChangeEvent ce = new ChangeEvent(this);
        for (int index = 0; index < mChangeListeners.size(); index++)
        {
            ((ChangeListener) mChangeListeners.get(index)).stateChanged(ce);
        }
    }

    private class AddListener implements ActionListener
    {

        public void actionPerformed(ActionEvent e)
        {
            Object item = choiceList.getSelectedItem();
            int index = choiceList.getSelectedIndex();
            if (!item.equals(choiceList.getItemAt(index)))
            {
                choiceList.addItem(item);
            }
            choiceList.setSelectedItem(item);
            notifyChangeListeners();
        }
    }

    private class DeleteListener implements ActionListener
    {

        public void actionPerformed(ActionEvent e)
        {
            if (choiceList.getItemCount() > 1)
            {
                choiceList.removeItemAt(choiceList.getSelectedIndex());
                notifyChangeListeners();
            }
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

QDox parse error in file://<WORKSPACE>/data/code-rep-dataset/Dataset5/Tasks/5414.java