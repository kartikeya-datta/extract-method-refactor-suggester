error id: file://<WORKSPACE>/data/code-rep-dataset/Dataset2/Tasks/7291.java
file://<WORKSPACE>/data/code-rep-dataset/Dataset2/Tasks/7291.java
### com.thoughtworks.qdox.parser.ParseException: syntax error @[1,1]

error in qdox parser
file content:
```java
offset: 1
uri: file://<WORKSPACE>/data/code-rep-dataset/Dataset2/Tasks/7291.java
text:
```scala
d@@.showDialog(MailResourceLoader.getString("dialog", "error", "mailimport"));

// The contents of this file are subject to the Mozilla Public License Version
// 1.1
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
//The Initial Developers of the Original Code are Frederik Dietz and Timo
// Stich.
//Portions created by Frederik Dietz and Timo Stich are Copyright (C) 2003.
//
//All Rights Reserved.
package org.columba.mail.gui.config.mailboximport;

import net.javaprog.ui.wizard.AbstractStep;
import net.javaprog.ui.wizard.DataModel;
import net.javaprog.ui.wizard.DefaultDataLookup;

import org.columba.core.gui.util.MultiLineLabel;
import org.columba.core.gui.util.NotifyDialog;
import org.columba.core.main.MainInterface;
import org.columba.core.plugin.PluginHandlerNotFoundException;

import org.columba.mail.folder.mailboximport.DefaultMailboxImporter;
import org.columba.mail.plugin.ImportPluginHandler;
import org.columba.mail.util.MailResourceLoader;

import java.awt.BorderLayout;
import java.awt.GridBagConstraints;
import java.awt.GridBagLayout;
import java.awt.Insets;

import java.lang.reflect.Method;

import javax.swing.JComponent;
import javax.swing.JList;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.event.ListSelectionEvent;
import javax.swing.event.ListSelectionListener;


class PluginStep extends AbstractStep implements ListSelectionListener {
    protected DataModel data;
    protected MultiLineLabel descriptionLabel;
    private ImportPluginHandler pluginHandler;

    public PluginStep(DataModel data) {
        super(MailResourceLoader.getString("dialog", "mailboximport", "plugin"),
            MailResourceLoader.getString("dialog", "mailboximport",
                "plugin_description"));
        this.data = data;

        try {
            pluginHandler = (ImportPluginHandler) MainInterface.pluginManager.getHandler(
                    "org.columba.mail.import");
        } catch (PluginHandlerNotFoundException ex) {
            NotifyDialog d = new NotifyDialog();

            //show neat error message here
            d.showDialog(ex);

            return;
        }
    }

    protected JComponent createComponent() {
        descriptionLabel = new MultiLineLabel("description");
        descriptionLabel.setWrapStyleWord(true);
        descriptionLabel.setLineWrap(true);

        JList list = new JList(((ImportPluginHandler) data.getData(
                    "Plugin.handler")).getPluginIdList());
        list.setCellRenderer(new PluginListCellRenderer());

        JComponent component = new JPanel(new BorderLayout(0, 30));
        component.add(new MultiLineLabel(MailResourceLoader.getString(
                    "dialog", "mailboximport", "plugin_text")),
            BorderLayout.NORTH);

        JPanel middlePanel = new JPanel();
        middlePanel.setAlignmentX(1);

        GridBagLayout layout = new GridBagLayout();
        middlePanel.setLayout(layout);

        Method method = null;

        try {
            method = list.getClass().getMethod("getSelectedValue", null);
        } catch (NoSuchMethodException nsme) {
        }

        data.registerDataLookup("Plugin.ID",
            new DefaultDataLookup(list, method, null));
        list.addListSelectionListener(this);
        list.setSelectedIndex(0);

        JScrollPane scrollPane = new JScrollPane(list);

        //scrollPane.setPreferredSize( new Dimension(200,200) );
        GridBagConstraints c = new GridBagConstraints();
        c.anchor = GridBagConstraints.NORTHWEST;
        c.gridx = 0;
        c.fill = GridBagConstraints.BOTH;
        c.weightx = 0.4;

        //c.gridwidth = GridBagConstraints.RELATIVE;
        c.weighty = 1.0;
        layout.setConstraints(scrollPane, c);
        middlePanel.add(scrollPane);

        c.gridwidth = GridBagConstraints.REMAINDER;
        c.weightx = 0.6;
        c.gridx = 1;
        c.anchor = GridBagConstraints.NORTHWEST;
        c.insets = new Insets(0, 10, 0, 0);

        JScrollPane scrollPane2 = new JScrollPane(descriptionLabel);

        //scrollPane2.setPreferredSize( new Dimension(200,100) );
        layout.setConstraints(scrollPane2, c);
        middlePanel.add(scrollPane2);
        component.add(middlePanel);

        return component;
    }

    public void valueChanged(ListSelectionEvent event) {
        try {
            //adjust description field
            DefaultMailboxImporter importer = (DefaultMailboxImporter) pluginHandler.getPlugin((String) data.getData(
                        "Plugin.ID"), null);
            String description = importer.getDescription();
            descriptionLabel.setText(description);
        } catch (Exception e) {
            NotifyDialog d = new NotifyDialog();

            //show neat error message here
            d.showDialog(e);
        }
    }

    public void prepareRendering() {
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

QDox parse error in file://<WORKSPACE>/data/code-rep-dataset/Dataset2/Tasks/7291.java