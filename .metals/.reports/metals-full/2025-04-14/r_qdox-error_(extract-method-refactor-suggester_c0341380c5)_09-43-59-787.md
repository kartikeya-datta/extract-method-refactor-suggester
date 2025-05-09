error id: file://<WORKSPACE>/data/code-rep-dataset/Dataset2/Tasks/1325.java
file://<WORKSPACE>/data/code-rep-dataset/Dataset2/Tasks/1325.java
### com.thoughtworks.qdox.parser.ParseException: syntax error @[1,1]

error in qdox parser
file content:
```java
offset: 1
uri: file://<WORKSPACE>/data/code-rep-dataset/Dataset2/Tasks/1325.java
text:
```scala
s@@etVisible(false);

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
package org.columba.mail.gui.tree.util;

import com.jgoodies.forms.layout.FormLayout;

import net.javaprog.ui.wizard.plaf.basic.SingleSideEtchedBorder;

import org.columba.core.gui.util.ButtonWithMnemonic;
import org.columba.core.gui.util.DefaultFormBuilder;
import org.columba.core.gui.util.DialogStore;

import org.columba.mail.folder.FolderTreeNode;
import org.columba.mail.main.MailInterface;
import org.columba.mail.util.MailResourceLoader;

import java.awt.BorderLayout;
import java.awt.Container;
import java.awt.Dimension;
import java.awt.GridLayout;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.KeyEvent;

import javax.swing.BorderFactory;
import javax.swing.JButton;
import javax.swing.JComboBox;
import javax.swing.JComponent;
import javax.swing.JDialog;
import javax.swing.JLabel;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JTextField;
import javax.swing.JTree;
import javax.swing.KeyStroke;
import javax.swing.SwingConstants;
import javax.swing.tree.TreePath;
import javax.swing.tree.TreeSelectionModel;


public class CreateFolderDialog extends JDialog implements ActionListener {
    protected boolean bool = false;
    protected JTextField textField;
    protected JButton okButton;
    protected JButton cancelButton;
    protected JButton helpButton;
    protected JComboBox typeBox;
    protected JTree tree;
    protected String name;
    protected TreePath selected;

    public CreateFolderDialog(TreePath selected) {
        super(DialogStore.getOwner(),
            MailResourceLoader.getString("dialog", "folder", "edit_name"), true);

        name = MailResourceLoader.getString("dialog", "folder",
                "new_folder_name");

        this.selected = selected;

        initComponents();
        layoutComponents();
    }

    protected void layoutComponents() {
        Container contentPane = getContentPane();
        contentPane.setLayout(new BorderLayout());

        // layout center panel
        FormLayout layout = new FormLayout("left:max(20dlu;pref), 3dlu, 80dlu:grow",
                "");

        DefaultFormBuilder builder = new DefaultFormBuilder(layout);

        // create EmptyBorder between components and dialog-frame 
        builder.setDefaultDialogBorder();

        // skip the first column
        //builder.setLeadingColumnOffset(1);
        // Add components to the panel:
        // TODO: LOCALIZE
        builder.append(new JLabel(MailResourceLoader.getString("dialog",
                    "folder", "name")));
        builder.append(textField);
        builder.nextLine();

        builder.append(new JLabel("Type:"));
        builder.append(typeBox);

        builder.appendRow("3dlu");
        builder.appendRow("fill:d:grow");
        builder.nextLine(2);

        JScrollPane scrollPane = new JScrollPane(tree);
        scrollPane.setPreferredSize(new Dimension(200, 300));
        builder.append(scrollPane, 3);

        contentPane.add(builder.getPanel(), BorderLayout.CENTER);

        // init bottom panel with OK, Cancel buttons
        JPanel bottomPanel = new JPanel(new BorderLayout(0, 0));
        bottomPanel.setBorder(new SingleSideEtchedBorder(SwingConstants.TOP));

        JPanel buttonPanel = new JPanel(new GridLayout(1, 2, 5, 0));
        buttonPanel.setBorder(BorderFactory.createEmptyBorder(11, 11, 11, 11));

        buttonPanel.add(okButton);

        buttonPanel.add(cancelButton);
        bottomPanel.add(buttonPanel, BorderLayout.EAST);
        contentPane.add(bottomPanel, BorderLayout.SOUTH);

        getRootPane().setDefaultButton(okButton);
        getRootPane().registerKeyboardAction(this, "CANCEL",
            KeyStroke.getKeyStroke(KeyEvent.VK_ESCAPE, 0),
            JComponent.WHEN_IN_FOCUSED_WINDOW);
    }

    protected void initComponents() {
        // Init components
        textField = new JTextField(name, 15);
        textField.setSelectionStart(0);
        textField.setSelectionEnd(name.length());

        typeBox = new JComboBox(new String[] { "Standard Mailbox" });

        tree = new JTree(MailInterface.treeModel);
        tree.setCellRenderer(new FolderTreeCellRenderer());
        tree.setRootVisible(false);
        tree.getSelectionModel().setSelectionMode(TreeSelectionModel.SINGLE_TREE_SELECTION);
        tree.expandRow(0);
        tree.expandRow(1);

        // try to set selection
        if (selected != null) {
            tree.setSelectionPath(selected);
        }

        // button panel
        okButton = new ButtonWithMnemonic(MailResourceLoader.getString(
                    "global", "ok"));
        okButton.setActionCommand("OK");
        okButton.addActionListener(this);
        cancelButton = new ButtonWithMnemonic(MailResourceLoader.getString(
                    "global", "cancel"));
        cancelButton.setActionCommand("CANCEL");
        cancelButton.addActionListener(this);
        helpButton = new ButtonWithMnemonic(MailResourceLoader.getString(
                    "global", "help"));
        helpButton.setActionCommand("HELP");
        helpButton.addActionListener(this);
    }

    public void showDialog() {
        pack();
        setLocationRelativeTo(null);
        setVisible(true);
    }

    public String getName() {
        return name;
    }

    public FolderTreeNode getSelected() {
        return (FolderTreeNode) tree.getSelectionPath().getLastPathComponent();
    }

    public boolean success() {
        return bool;
    }

    public void actionPerformed(ActionEvent e) {
        String action = e.getActionCommand();

        if (action.equals("OK")) {
            name = textField.getText().trim();

            // fixing bug with id 553176
            if (name.indexOf('/') != -1) {
                // if the character / is found shows the user a error message
                JOptionPane.showMessageDialog(null,
                    MailResourceLoader.getString("dialog", "folder",
                        "error_char_text"),
                    MailResourceLoader.getString("dialog", "folder",
                        "error_char_title"), JOptionPane.ERROR_MESSAGE);

                return;
            }

            bool = true;
            dispose();
        } else if (action.equals("CANCEL")) {
            bool = false;

            dispose();
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

QDox parse error in file://<WORKSPACE>/data/code-rep-dataset/Dataset2/Tasks/1325.java