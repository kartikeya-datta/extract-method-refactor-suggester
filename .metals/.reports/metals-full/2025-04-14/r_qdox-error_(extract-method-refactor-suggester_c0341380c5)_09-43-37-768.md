error id: file://<WORKSPACE>/data/code-rep-dataset/Dataset2/Tasks/359.java
file://<WORKSPACE>/data/code-rep-dataset/Dataset2/Tasks/359.java
### com.thoughtworks.qdox.parser.ParseException: syntax error @[1,1]

error in qdox parser
file content:
```java
offset: 1
uri: file://<WORKSPACE>/data/code-rep-dataset/Dataset2/Tasks/359.java
text:
```scala
a@@ddItem(new ColorItem(Color.black, "None"));

//The contents of this file are subject to the Mozilla Public License Version 1.1
//(the "License"); you may not use this file except in compliance with the
//License. You may obtain a copy of the License at http://www.mozilla.org/MPL/
//
//Software distributed under the License is distributed on an "AS IS" basis,
//WITHOUT WARRANTY OF ANY KIND, either express or implied. See the License
//for the specific language governing rights and
//limitations under the License.
//
//The Original Code is "The Columba Project"
//
//The Initial Developers of the Original Code are Frederik Dietz and Timo Stich.
//Portions created by Frederik Dietz and Timo Stich are Copyright (C) 2003.
//
//All Rights Reserved.
package org.columba.core.gui.util;

import java.awt.Color;
import java.awt.event.ItemEvent;
import java.awt.event.ItemListener;

import javax.swing.ComboBoxModel;
import javax.swing.JColorChooser;
import javax.swing.JComboBox;

/**
 * A JComboBox that displays Colors.
 * @author redsolo
 */
public class ColorComboBox extends JComboBox implements ItemListener {

    private boolean codeSelectionUpdate = false;

    /**
     * Constructs a color combobo.x
     */
    public ColorComboBox() {
        super();

        // Add the default colors items.
        addItem(new ColorItem(Color.black, "Black"));
        addItem(new ColorItem(Color.blue, "Blue"));
        addItem(new ColorItem(Color.gray, "Gray"));
        addItem(new ColorItem(Color.green, "Green"));
        addItem(new ColorItem(Color.red, "Red"));
        addItem(new ColorItem(Color.yellow, "Yellow"));
        addItem(new ColorItem(Color.black, "Custom"));

        setRenderer(new ColorItemRenderer());

        addItemListener(this);
    }

    /**
     * Selects the combobox item with the specified color name.
     * @param name the name of the color to select.
     */
    public void setSelectedColor(String name) {
        codeSelectionUpdate = true;
        ComboBoxModel model = getModel();
        if (name == null) {
            setSelectedIndex(0);
        } else {
            for (int i = 0; i < model.getSize(); i++) {
                ColorItem object = (ColorItem) model.getElementAt(i);

                if (object.getName().equalsIgnoreCase(name)) {
                    setSelectedIndex(i);

                    break;
                }
            }
        }
        codeSelectionUpdate = false;
    }

    /**
     * Sets the color for the Custom color item.
     * @param color the new color for the Custom color.
     */
    public void setCustomColor(Color color) {
        ColorItem item = (ColorItem) getModel().getElementAt(getModel().getSize() - 1);
        item.setColor(color);
        repaint();
    }

    /**
     * Sets the color for the Custom color item.
     * @param rgb the new color, in rgb value, for the Custom color.
     */
    public void setCustomColor(int rgb) {
        setCustomColor(ColorFactory.getColor(rgb));
    }

    /**
     * Returns the selected coloritem.
     * @return the selected coloritem.
     */
    public ColorItem getSelectedColorItem() {
        return (ColorItem) getSelectedItem();
    }

    /** {@inheritDoc} */
    public void itemStateChanged(ItemEvent e) {

        if ((!codeSelectionUpdate) && (e.getStateChange() == ItemEvent.SELECTED)) {
            ColorItem item = (ColorItem) getSelectedItem();

            if (item.getName().equalsIgnoreCase("custom")) {
                Color newColor = JColorChooser.showDialog(null,
                        "Choose Background Color", item.getColor());

                item.setColor(newColor);
                repaint();
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
	java.base/java.util.concurrent.ForkJoinPool$WorkQueue.topLevelExec(ForkJoinPool.java:1182)
	java.base/java.util.concurrent.ForkJoinPool.scan(ForkJoinPool.java:1655)
	java.base/java.util.concurrent.ForkJoinPool.runWorker(ForkJoinPool.java:1622)
	java.base/java.util.concurrent.ForkJoinWorkerThread.run(ForkJoinWorkerThread.java:165)
```
#### Short summary: 

QDox parse error in file://<WORKSPACE>/data/code-rep-dataset/Dataset2/Tasks/359.java