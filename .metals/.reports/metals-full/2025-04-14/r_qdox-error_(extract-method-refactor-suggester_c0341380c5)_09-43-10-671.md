error id: file://<WORKSPACE>/data/code-rep-dataset/Dataset2/Tasks/841.java
file://<WORKSPACE>/data/code-rep-dataset/Dataset2/Tasks/841.java
### com.thoughtworks.qdox.parser.ParseException: syntax error @[1,1]

error in qdox parser
file content:
```java
offset: 1
uri: file://<WORKSPACE>/data/code-rep-dataset/Dataset2/Tasks/841.java
text:
```scala
F@@ormLayout layout = new FormLayout("10dlu, max(70dlu;default), 3dlu, fill:max(150dlu;default):grow ",

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
package org.columba.mail.gui.config.account;

import com.jgoodies.forms.layout.FormLayout;

import org.columba.core.gui.util.DefaultFormBuilder;

import org.columba.mail.config.PGPItem;
import org.columba.mail.util.MailResourceLoader;

import java.awt.Component;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

import java.io.File;

import javax.swing.JButton;
import javax.swing.JCheckBox;
import javax.swing.JComboBox;
import javax.swing.JFileChooser;
import javax.swing.JLabel;
import javax.swing.JTextField;


public class SecurityPanel extends DefaultPanel implements ActionListener {
    private JLabel idLabel;
    private JTextField idTextField;
    private JLabel typeLabel;
    private JComboBox typeComboBox;
    private JLabel pathLabel;
    private JButton pathButton;
    private JCheckBox enableCheckBox;
    private JCheckBox alwaysSignCheckBox;
    private JCheckBox alwaysEncryptCheckBox;
    private PGPItem item;

    public SecurityPanel(PGPItem item) {
        super();
        this.item = item;

        initComponents();

        updateComponents(true);

        layoutComponents();

        //enableCheckBox.setEnabled(false);
    }

    protected void updateComponents(boolean b) {
        if (b) {
            idTextField.setText(item.get("id"));
            pathButton.setText(item.get("path"));

            enableCheckBox.setSelected(item.getBoolean("enabled"));

            alwaysSignCheckBox.setSelected(item.getBoolean("always_sign"));
            alwaysEncryptCheckBox.setSelected(item.getBoolean("always_encrypt"));

            enablePGP(enableCheckBox.isSelected());
        } else {
            item.set("id", idTextField.getText());
            item.set("path", pathButton.getText());

            item.set("enabled", enableCheckBox.isSelected());

            item.set("always_sign", alwaysSignCheckBox.isSelected());
            item.set("always_encrypt", alwaysEncryptCheckBox.isSelected());
        }
    }

    protected void layoutComponents() {
        //		Create a FormLayout instance. 
        FormLayout layout = new FormLayout("10dlu, max(100;default), 3dlu, fill:max(150dlu;default):grow ",
                
            // 2 columns
            ""); // rows are added dynamically (no need to define them here)

        // create a form builder
        DefaultFormBuilder builder = new DefaultFormBuilder(this, layout);

        // create EmptyBorder between components and dialog-frame 
        builder.setDefaultDialogBorder();

        // skip the first column
        builder.setLeadingColumnOffset(1);

        // Add components to the panel:
        builder.appendSeparator(MailResourceLoader.getString("dialog",
                "account", "pgp_options"));
        builder.nextLine();

        builder.append(enableCheckBox, 3);
        builder.nextLine();

        builder.append(idLabel, 1);
        builder.append(idTextField);
        builder.nextLine();

        builder.append(alwaysSignCheckBox, 3);
        builder.nextLine();

        //      TODO: reactivate when feature is supported

        /*
builder.append(alwaysEncryptCheckBox, 3);
builder.nextLine();
*/
        /*
setLayout(new BorderLayout());
add(builder.getPanel(), BorderLayout.CENTER);
*/
        /*
GridBagLayout layout = new GridBagLayout();
GridBagConstraints c = new GridBagConstraints();

setLayout(new BorderLayout());
setBorder(BorderFactory.createEmptyBorder(5, 5, 5, 5));

JPanel mainMiddle = new JPanel();
mainMiddle.setLayout(new BorderLayout());

JPanel middle = new JPanel();
middle.setBorder(javax.swing.BorderFactory.createTitledBorder(javax.swing.BorderFactory.createEtchedBorder(), MailResourceLoader.getString("dialog", "account", "pgp_options"))); //$NON-NLS-1$
middle.setLayout(new BorderLayout());

JPanel enablePanel = new JPanel();
enablePanel.setBorder(BorderFactory.createEmptyBorder(0, 10, 0, 10));
enablePanel.setLayout(new BoxLayout(enablePanel, BoxLayout.X_AXIS));

enablePanel.add(enableCheckBox);
enablePanel.add(Box.createHorizontalGlue());
layout = new GridBagLayout();
c = new GridBagConstraints();
c = new GridBagConstraints();
c.gridwidth = GridBagConstraints.REMAINDER;
c.gridx = 0;
c.weightx = 1.0;
c.anchor = GridBagConstraints.WEST;
layout.setConstraints(enablePanel, c);
add(enablePanel, BorderLayout.NORTH);

JPanel innerPanel = new JPanel();
innerPanel.setBorder(BorderFactory.createEmptyBorder(5, 5, 5, 5));
innerPanel.setLayout(new BorderLayout());

JPanel leftPanel = new JPanel();
leftPanel.setLayout(new GridLayout(0, 1, 50, 5));
leftPanel.add(idLabel);
leftPanel.add(typeLabel);
leftPanel.add(pathLabel);

JPanel rightPanel = new JPanel();
rightPanel.setLayout(new GridLayout(0, 1, 50, 5));
rightPanel.add(idTextField);
rightPanel.add(typeComboBox);

rightPanel.add(pathButton);

innerPanel.add(leftPanel, BorderLayout.CENTER);
innerPanel.add(rightPanel, BorderLayout.EAST);

middle.add(innerPanel, BorderLayout.NORTH);

JPanel alwaysPanel = new JPanel();
alwaysPanel.setLayout(new BoxLayout(alwaysPanel, BoxLayout.Y_AXIS));
alwaysPanel.setBorder(BorderFactory.createEmptyBorder(0, 0, 10, 0));

JPanel alwaysSignPanel = new JPanel();
alwaysSignPanel.setBorder(
        BorderFactory.createEmptyBorder(0, 10, 0, 10));
alwaysSignPanel.setLayout(
        new BoxLayout(alwaysSignPanel, BoxLayout.X_AXIS));

alwaysSignPanel.add(alwaysSignCheckBox);
alwaysSignPanel.add(Box.createHorizontalGlue());
layout = new GridBagLayout();
c = new GridBagConstraints();
c = new GridBagConstraints();
c.gridwidth = GridBagConstraints.REMAINDER;
c.gridx = 0;
c.weightx = 1.0;
c.anchor = GridBagConstraints.WEST;
layout.setConstraints(alwaysSignPanel, c);
alwaysPanel.add(alwaysSignPanel);

JPanel alwaysEncryptPanel = new JPanel();
alwaysEncryptPanel.setBorder(
        BorderFactory.createEmptyBorder(0, 10, 0, 10));
alwaysEncryptPanel.setLayout(
        new BoxLayout(alwaysEncryptPanel, BoxLayout.X_AXIS));

alwaysEncryptPanel.add(alwaysEncryptCheckBox);
alwaysEncryptPanel.add(Box.createHorizontalGlue());
layout = new GridBagLayout();
c = new GridBagConstraints();
c = new GridBagConstraints();
c.gridwidth = GridBagConstraints.REMAINDER;
c.gridx = 0;
c.weightx = 1.0;
c.anchor = GridBagConstraints.WEST;
layout.setConstraints(alwaysEncryptPanel, c);
alwaysPanel.add(alwaysEncryptPanel);

middle.add(alwaysPanel, BorderLayout.CENTER);

mainMiddle.add(middle, BorderLayout.NORTH);

add(mainMiddle, BorderLayout.CENTER);
*/
    }

    protected void initComponents() {
        enableCheckBox = new JCheckBox(MailResourceLoader.getString("dialog",
                    "account", "enable_PGP_Support")); //$NON-NLS-1$
        enableCheckBox.setAlignmentX(Component.LEFT_ALIGNMENT);
        enableCheckBox.setActionCommand("ENABLE");
        enableCheckBox.addActionListener(this);

        idLabel = new JLabel(MailResourceLoader.getString("dialog", "account",
                    "User_ID")); //$NON-NLS-1$

        typeLabel = new JLabel(MailResourceLoader.getString("dialog",
                    "account", "PGP_Version")); //$NON-NLS-1$

        pathLabel = new JLabel(MailResourceLoader.getString("dialog",
                    "account", "Path_to_Binary")); //$NON-NLS-1$

        idTextField = new JTextField();

        typeComboBox = new JComboBox();

        //typeComboBox.setMargin( new Insets( 0,0,0,0 ) );
        typeComboBox.insertItemAt("GnuPG", 0);
        typeComboBox.insertItemAt("PGP2", 1);
        typeComboBox.insertItemAt("PGP5", 2);
        typeComboBox.insertItemAt("PGP6", 3);
        typeComboBox.setSelectedIndex(0);
        typeComboBox.setEnabled(false);

        pathButton = new JButton();

        //pathButton.setMargin( new Insets( 0,0,0,0 ) );
        pathButton.setActionCommand("PATH");
        pathButton.addActionListener(this);

        alwaysSignCheckBox = new JCheckBox(MailResourceLoader.getString(
                    "dialog", "account", "Always_sign_when_sending_messages")); //$NON-NLS-1$
        alwaysSignCheckBox.setAlignmentX(Component.LEFT_ALIGNMENT);
        alwaysSignCheckBox.setEnabled(false);

        alwaysEncryptCheckBox = new JCheckBox(MailResourceLoader.getString(
                    "dialog", "account", "Always_encrypt_when_sending_messages")); //$NON-NLS-1$
        alwaysEncryptCheckBox.setAlignmentX(Component.LEFT_ALIGNMENT);
        alwaysEncryptCheckBox.setEnabled(false);
    }

    public void enablePGP(boolean b) {
        if (b) {
            //typeComboBox.setEnabled( true );
            idTextField.setEnabled(true);
            idLabel.setEnabled(true);
            typeLabel.setEnabled(true);
            pathLabel.setEnabled(true);
            pathButton.setEnabled(true);
            alwaysSignCheckBox.setEnabled(true);
            alwaysEncryptCheckBox.setEnabled(true);
        } else {
            //typeComboBox.setEnabled( false );
            idTextField.setEnabled(false);
            idLabel.setEnabled(false);
            typeLabel.setEnabled(false);
            pathLabel.setEnabled(false);
            pathButton.setEnabled(false);
            alwaysSignCheckBox.setEnabled(false);
            alwaysEncryptCheckBox.setEnabled(false);
        }
    }

    public void actionPerformed(ActionEvent e) {
        String action = e.getActionCommand();

        if (action.equals("ENABLE")) {
            enablePGP(enableCheckBox.isSelected());
        } else if (action.equals("PATH")) {
            JFileChooser fileChooser = new JFileChooser();
            File aktFile;

            fileChooser.setDialogTitle(MailResourceLoader.getString("dialog",
                    "account", "PGP_Binary")); //$NON-NLS-1$
            fileChooser.setFileSelectionMode(JFileChooser.FILES_ONLY);

            int returnVal = fileChooser.showDialog(null,
                    MailResourceLoader.getString("dialog", "account",
                        "Select_File")); //$NON-NLS-1$

            if (returnVal == JFileChooser.APPROVE_OPTION) {
                File file = fileChooser.getSelectedFile();
                pathButton.setText(file.getPath());
            }
        }
    }

    public boolean isFinished() {
        boolean result = true;

        /*
String name = getAccountName();
String address = getAddress();

if ( name.length() == 0 )
{
    result = false;
    JOptionPane.showMessageDialog( MainInterface.mainFrame,
                                   "You have to enter a name for this account!");
}
else if ( address.length() == 0 )
{
    result = false;
    JOptionPane.showMessageDialog( MainInterface.mainFrame,
                                   "You have to enter your address!");
}
else
{
    result = true;
}
*/
        return result;
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

QDox parse error in file://<WORKSPACE>/data/code-rep-dataset/Dataset2/Tasks/841.java