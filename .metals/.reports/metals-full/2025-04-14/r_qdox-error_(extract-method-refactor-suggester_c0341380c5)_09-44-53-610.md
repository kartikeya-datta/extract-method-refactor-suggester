error id: file://<WORKSPACE>/data/code-rep-dataset/Dataset2/Tasks/4757.java
file://<WORKSPACE>/data/code-rep-dataset/Dataset2/Tasks/4757.java
### com.thoughtworks.qdox.parser.ParseException: syntax error @[1,1]

error in qdox parser
file content:
```java
offset: 1
uri: file://<WORKSPACE>/data/code-rep-dataset/Dataset2/Tasks/4757.java
text:
```scala
i@@tem.setBoolean("automatically_apply_filter",

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

import javax.swing.JCheckBox;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JTextField;

import org.columba.core.gui.util.CheckBoxWithMnemonic;
import org.columba.mail.config.ImapItem;
import org.columba.mail.util.MailResourceLoader;

import com.jgoodies.forms.builder.DefaultFormBuilder;


/**
 *
 * @author  freddy
 * @version
 */
public class ImapAttributPanel extends JPanel {
    private ImapItem item;
    private JCheckBox secureCheckBox;
    private JCheckBox storePasswordCheckBox;
    private JCheckBox automaticallyApplyFilterCheckBox;
    private JCheckBox intervalCheckingCheckBox;
    private JPanel jPanel1;
    private JLabel intervalCheckingLabel;
    private JLabel intervalCheckingLabel2;
    private JTextField intervalCheckingTextField;
    private JCheckBox cleanupCheckBox;
    private JPanel cleanupPanel;

    //private ConfigFrame frame;
    public ImapAttributPanel(ImapItem item) {
        //super( " Imap4 Settings " );
        this.item = item;
        initComponents();
    }

    public void updateComponents(boolean b) {
        if (b) {
            /*
if ( item.isSavePassword() )
    storePasswordCheckBox.setSelected(true);
    */
            automaticallyApplyFilterCheckBox.setSelected(item.getBoolean(
                    "automatically_apply_filter"));
        } else {
            /*
if ( storePasswordCheckBox.isSelected() == true )
    item.setSavePassword("true");
else
    item.setSavePassword("false");
    */
            item.set("automatically_apply_filter",
                automaticallyApplyFilterCheckBox.isSelected());
        }
    }

    public void createPanel(DefaultFormBuilder builder) {
        builder.appendSeparator(MailResourceLoader.getString("dialog",
                "account", "options"));

        builder.append(automaticallyApplyFilterCheckBox, 4);
        builder.nextLine();

        /*
builder.append(cleanupCheckBox, 3);
builder.nextLine();
*/
    }

    protected void initComponents() {
        cleanupCheckBox = new JCheckBox();
        cleanupCheckBox.setEnabled(false);
        cleanupCheckBox.setText(MailResourceLoader.getString("dialog",
                "account", "Expunge_Inbox_on_Exit"));

        automaticallyApplyFilterCheckBox = new CheckBoxWithMnemonic(MailResourceLoader.getString(
                    "dialog", "account", "apply_filter"));
    }

    /*
private void initComponents() {


        GridBagLayout layout = new GridBagLayout();
        GridBagConstraints c = new GridBagConstraints();
        setLayout(layout);

        JPanel intervalCheckingPanel = new JPanel();
        //intervalCheckingPanel.add( Box.createRigidArea( new java.awt.Dimension(10,0) ) );
        intervalCheckingPanel.setLayout(
                new BoxLayout(intervalCheckingPanel, BoxLayout.X_AXIS));
        intervalCheckingCheckBox = new JCheckBox();
        intervalCheckingCheckBox.setEnabled(false);
        intervalCheckingCheckBox.setText(
                MailResourceLoader.getString(
                        "dialog/account",
                        "imapattributpanel",
                        "enable_interval_message_checking"));
        intervalCheckingCheckBox.setMnemonic(
                MailResourceLoader.getMnemonic(
                        "dialog/account",
                        "imapattributpanel",
                        "enable_interval_message_checking"));
        //$NON-NLS-1$
        intervalCheckingCheckBox.setAlignmentX(Component.LEFT_ALIGNMENT);
        intervalCheckingPanel.add(intervalCheckingCheckBox);
        intervalCheckingPanel.add(Box.createHorizontalGlue());
        c.gridx = 0;
        c.weightx = 1.0;
        c.anchor = GridBagConstraints.WEST;
        c.gridwidth = GridBagConstraints.REMAINDER;
        layout.setConstraints(intervalCheckingPanel, c);
        add(intervalCheckingPanel);



        JPanel cleanupPanel = new JPanel();
        //cleanupPanel.add( Box.createRigidArea( new java.awt.Dimension(10,0) ) );
        cleanupPanel.setLayout(new BoxLayout(cleanupPanel, BoxLayout.X_AXIS));
        cleanupCheckBox = new JCheckBox();
        cleanupCheckBox.setEnabled(false);
        cleanupCheckBox.setText(
                MailResourceLoader.getString(
                        "dialog",
                        "account",
                        "Expunge_Inbox_on_Exit"));
        //$NON-NLS-1$
        cleanupCheckBox.setAlignmentX(Component.LEFT_ALIGNMENT);
        cleanupPanel.add(cleanupCheckBox);
        cleanupPanel.add(Box.createHorizontalGlue());
        c.gridx = 0;
        c.weightx = 1.0;
        c.anchor = GridBagConstraints.WEST;
        c.gridwidth = GridBagConstraints.REMAINDER;
        layout.setConstraints(cleanupPanel, c);
        add(cleanupPanel);

}
*/
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

QDox parse error in file://<WORKSPACE>/data/code-rep-dataset/Dataset2/Tasks/4757.java