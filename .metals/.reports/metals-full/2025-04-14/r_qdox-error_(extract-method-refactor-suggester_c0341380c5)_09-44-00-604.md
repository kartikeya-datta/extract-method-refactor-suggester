error id: file://<WORKSPACE>/data/code-rep-dataset/Dataset3/Tasks/13471.java
file://<WORKSPACE>/data/code-rep-dataset/Dataset3/Tasks/13471.java
### com.thoughtworks.qdox.parser.ParseException: syntax error @[1,1]

error in qdox parser
file content:
```java
offset: 1
uri: file://<WORKSPACE>/data/code-rep-dataset/Dataset3/Tasks/13471.java
text:
```scala
i@@f (!runtimeName.trim().isEmpty()) builder.append("  --runtime-name=").append(runtimeName);

/*
 * JBoss, Home of Professional Open Source
 * Copyright 2012 Red Hat Inc. and/or its affiliates and other contributors
 * as indicated by the @author tags. All rights reserved.
 * See the copyright.txt in the distribution for a
 * full listing of individual contributors.
 *
 * This copyrighted material is made available to anyone wishing to use,
 * modify, copy, or redistribute it subject to the terms and conditions
 * of the GNU Lesser General Public License, v. 2.1.
 * This program is distributed in the hope that it will be useful, but WITHOUT A
 * WARRANTY; without even the implied warranty of MERCHANTABILITY or FITNESS FOR A
 * PARTICULAR PURPOSE.  See the GNU Lesser General Public License for more details.
 * You should have received a copy of the GNU Lesser General Public License,
 * v.2.1 along with this distribution; if not, write to the Free Software
 * Foundation, Inc., 51 Franklin Street, Fifth Floor, Boston,
 * MA  02110-1301, USA.
 */
package org.jboss.as.cli.gui.metacommand;

import java.awt.BorderLayout;
import java.awt.Container;
import java.awt.Dialog;
import java.awt.GridBagConstraints;
import java.awt.GridBagLayout;
import java.awt.Insets;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.KeyEvent;
import java.io.File;
import java.io.IOException;
import javax.swing.Box;
import javax.swing.JButton;
import javax.swing.JCheckBox;
import javax.swing.JDialog;
import javax.swing.JFileChooser;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JTextField;
import javax.swing.text.JTextComponent;
import org.jboss.as.cli.gui.GuiMain;
import org.jboss.as.cli.gui.component.HelpButton;
import org.jboss.as.cli.gui.component.ServerGroupChooser;

/**
 * Creates a dialog that lets you build a deploy command.  This dialog
 * behaves differently depending on standalone or domain mode.
 *
 * @author Stan Silvert ssilvert@redhat.com (C) 2012 Red Hat Inc.
 */
public class DeployCommandDialog extends JDialog implements ActionListener {
    // make this static so that it always retains the last directory chosen
    private static JFileChooser fileChooser = new JFileChooser(new File("."));

    private JPanel inputPanel = new JPanel(new GridBagLayout());
    private JTextField pathField = new JTextField(40);
    private JTextField nameField = new JTextField(40);
    private JTextField runtimeNameField = new JTextField(40);
    private JCheckBox forceCheckBox = new JCheckBox("force");
    private JCheckBox disabledCheckBox = new JCheckBox("disabled");
    private ServerGroupChooser serverGroupChooser = new ServerGroupChooser();
    private JCheckBox allServerGroups = new JCheckBox("all-server-groups");

    public DeployCommandDialog() {
        super(GuiMain.getMainWindow(), "deploy", Dialog.ModalityType.APPLICATION_MODAL);

        setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
        Container contentPane = getContentPane();
        contentPane.setLayout(new BorderLayout(10, 10));

        setAllServerGroupsListener();
        setForceListener();

        contentPane.add(makeInputPanel(), BorderLayout.CENTER);

        contentPane.add(makeButtonPanel(), BorderLayout.SOUTH);
        pack();
        setResizable(false);
    }

    private void setAllServerGroupsListener() {
        allServerGroups.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent e) {
                if (allServerGroups.isSelected()) {
                    serverGroupChooser.setEnabled(false);
                } else {
                    serverGroupChooser.setEnabled(true);
                }
            }
        });
    }

    private void setForceListener() {
        forceCheckBox.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent e) {
                if (forceCheckBox.isSelected()) {
                    serverGroupChooser.setEnabled(false);
                    allServerGroups.setEnabled(false);
                    allServerGroups.setSelected(false);
                    disabledCheckBox.setEnabled(false);
                    disabledCheckBox.setSelected(false);
                } else {
                    serverGroupChooser.setEnabled(true);
                    allServerGroups.setEnabled(true);
                    disabledCheckBox.setEnabled(true);
                }
            }
        });
    }

    private JPanel makeInputPanel() {
        GridBagConstraints gbConst = new GridBagConstraints();
        gbConst.anchor = GridBagConstraints.WEST;
        gbConst.insets = new Insets(5,5,5,5);

        JLabel pathLabel = new JLabel("File Path:");

        gbConst.gridwidth = 1;
        inputPanel.add(pathLabel, gbConst);

        addStrut();
        inputPanel.add(pathField, gbConst);

        addStrut();
        JButton browse = new JButton("Browse ...");
        browse.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent ae) {
                int returnVal = fileChooser.showOpenDialog(DeployCommandDialog.this);
                if (returnVal == JFileChooser.APPROVE_OPTION) {
                    try {
                        pathField.setText(fileChooser.getSelectedFile().getCanonicalPath());
                    } catch (IOException e) {
                        e.printStackTrace();
                    }
                }
            }
        });
        gbConst.gridwidth = GridBagConstraints.REMAINDER;
        inputPanel.add(browse, gbConst);

        JLabel nameLabel = new JLabel("Name:");
        gbConst.gridwidth = 1;
        inputPanel.add(nameLabel, gbConst);
        addStrut();
        gbConst.gridwidth = GridBagConstraints.REMAINDER;
        inputPanel.add(nameField, gbConst);

        JLabel runtimeNameLabel = new JLabel("Runtime Name:");
        gbConst.gridwidth = 1;
        inputPanel.add(runtimeNameLabel, gbConst);
        addStrut();
        gbConst.gridwidth = GridBagConstraints.REMAINDER;
        inputPanel.add(runtimeNameField, gbConst);

        JLabel forceLabel = new JLabel();
        gbConst.gridwidth = 1;
        inputPanel.add(forceLabel, gbConst);
        gbConst.gridwidth = GridBagConstraints.REMAINDER;
        inputPanel.add(forceCheckBox, gbConst);

        JLabel disabledLabel = new JLabel();
        gbConst.gridwidth = 1;
        inputPanel.add(disabledLabel, gbConst);
        gbConst.gridwidth = GridBagConstraints.REMAINDER;
        inputPanel.add(disabledCheckBox, gbConst);

        if (serverGroupChooser.isStandalone()) return inputPanel;

        JLabel serverGroupLabel = new JLabel();
        gbConst.gridwidth = 1;
        inputPanel.add(serverGroupLabel, gbConst);
        gbConst.gridwidth = GridBagConstraints.REMAINDER;
        inputPanel.add(serverGroupChooser, gbConst);

        gbConst.gridwidth = 1;
        inputPanel.add(new JLabel(), gbConst);
        gbConst.gridwidth = GridBagConstraints.REMAINDER;
        inputPanel.add(allServerGroups, gbConst);

        return inputPanel;
    }

    private void addStrut() {
        inputPanel.add(Box.createHorizontalStrut(5));
    }

    private JPanel makeButtonPanel() {
        JPanel buttonPanel = new JPanel();

        JButton ok = new JButton("OK");
        ok.addActionListener(this);
        ok.setMnemonic(KeyEvent.VK_ENTER);

        JButton cancel = new JButton("Cancel");
        cancel.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent ae) {
                DeployCommandDialog.this.dispose();
            }
        });

        buttonPanel.add(ok);
        buttonPanel.add(cancel);
        buttonPanel.add(new HelpButton("deploy.txt"));
        return buttonPanel;
    }

    public void actionPerformed(ActionEvent e) {
        StringBuilder builder = new StringBuilder("deploy");

        String path = pathField.getText();
        if (!path.trim().isEmpty()) {
            builder.append("  ").append(path);
        } else {
            JOptionPane.showMessageDialog(this, "A file must be selected.", "Empty File Path", JOptionPane.ERROR_MESSAGE);
            return;
        }

        String name = nameField.getText();
        if (!name.trim().isEmpty()) builder.append("  --name=").append(name);

        String runtimeName = runtimeNameField.getText();
        if (!runtimeName.trim().isEmpty()) builder.append("  --runtime_name=").append(runtimeName);

        if (forceCheckBox.isSelected()) builder.append("  --force");
        if (disabledCheckBox.isSelected() && disabledCheckBox.isEnabled()) builder.append("  --disabled");

        if (!serverGroupChooser.isStandalone()) {
            if (allServerGroups.isSelected() && allServerGroups.isEnabled()) {
                builder.append("  --all-server-groups");
            } else if (serverGroupChooser.isEnabled()) {
                builder.append(serverGroupChooser.getCmdLineArg());
            }
        }

        JTextComponent cmdText = GuiMain.getCommandLine().getCmdText();
        cmdText.setText(builder.toString());
        dispose();
        cmdText.requestFocus();
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

QDox parse error in file://<WORKSPACE>/data/code-rep-dataset/Dataset3/Tasks/13471.java