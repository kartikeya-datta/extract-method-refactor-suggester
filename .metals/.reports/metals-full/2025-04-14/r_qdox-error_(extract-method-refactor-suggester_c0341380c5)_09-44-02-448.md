error id: file://<WORKSPACE>/data/code-rep-dataset/Dataset2/Tasks/2233.java
file://<WORKSPACE>/data/code-rep-dataset/Dataset2/Tasks/2233.java
### com.thoughtworks.qdox.parser.ParseException: syntax error @[1,1]

error in qdox parser
file content:
```java
offset: 1
uri: file://<WORKSPACE>/data/code-rep-dataset/Dataset2/Tasks/2233.java
text:
```scala
s@@uper(parent, true);

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
package org.columba.mail.gui.config.filter;

import java.awt.BorderLayout;
import java.awt.GridLayout;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.KeyEvent;

import javax.swing.BorderFactory;
import javax.swing.Box;
import javax.swing.BoxLayout;
import javax.swing.JButton;
import javax.swing.JComboBox;
import javax.swing.JComponent;
import javax.swing.JDialog;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JTextField;
import javax.swing.KeyStroke;
import javax.swing.SwingConstants;

import net.javaprog.ui.wizard.plaf.basic.SingleSideEtchedBorder;

import org.columba.core.gui.util.ButtonWithMnemonic;
import org.columba.core.gui.util.ImageLoader;
import org.columba.core.gui.util.LabelWithMnemonic;
import org.columba.core.help.HelpManager;
import org.columba.mail.filter.Filter;
import org.columba.mail.filter.FilterRule;
import org.columba.mail.util.MailResourceLoader;

public class FilterDialog extends JDialog implements ActionListener {

    private JTextField nameTextField;

    private JButton addActionButton;

    private Filter filter;

    private JFrame frame;

    private CriteriaList criteriaList;

    private ActionList actionList;

    private JComboBox condList;

    /**
     * Boolean stating whetever the dialog was cancelled or not. Default value
     * is <code>true</code>.
     */
    private boolean dialogWasCancelled = true;

    public FilterDialog(JFrame parent, Filter filter) {
        super(parent, false);

        setTitle(MailResourceLoader.getString("dialog", "filter",
                "dialog_title"));
        this.filter = filter;

        //System.out.println("filternode name: " + filter.getName());
        initComponents();
        updateComponents(true);
        pack();
        setLocationRelativeTo(null);
        setVisible(true);
    }

    private void initComponents() {
        JPanel namePanel = new JPanel();
        namePanel.setBorder(BorderFactory.createEmptyBorder(12, 12, 12, 12));
        namePanel.setLayout(new BoxLayout(namePanel, BoxLayout.X_AXIS));

        JLabel nameLabel = new LabelWithMnemonic(MailResourceLoader.getString(
                "dialog", "filter", "filter_description"));
        namePanel.add(nameLabel);
        namePanel.add(Box.createHorizontalStrut(5));
        nameTextField = new JTextField(22);
        nameLabel.setLabelFor(nameTextField);
        namePanel.add(nameTextField);
        getContentPane().add(namePanel, BorderLayout.NORTH);

        JPanel centerPanel = new JPanel(new BorderLayout(0, 0));
        centerPanel.setBorder(BorderFactory.createCompoundBorder(BorderFactory
                .createEmptyBorder(0, 12, 10, 11), BorderFactory
                .createCompoundBorder(BorderFactory
                        .createTitledBorder(MailResourceLoader.getString(
                                "dialog", "filter", "if")), BorderFactory
                        .createEmptyBorder(10, 10, 10, 10))));

        JPanel middleIfPanel = new JPanel(new BorderLayout());
        centerPanel.add(middleIfPanel, BorderLayout.CENTER);

        JPanel ifPanel = new JPanel();
        ifPanel.setBorder(BorderFactory.createEmptyBorder(0, 0, 10, 0));
        ifPanel.setLayout(new BoxLayout(ifPanel, BoxLayout.X_AXIS));

        ifPanel.add(Box.createHorizontalGlue());

        nameLabel = new LabelWithMnemonic(MailResourceLoader.getString(
                "dialog", "filter", "execute_actions"));

        ifPanel.add(nameLabel);

        ifPanel.add(Box.createHorizontalStrut(5));

        String[] cond = {
                MailResourceLoader
                        .getString("dialog", "filter", "all_criteria"),
                MailResourceLoader
                        .getString("dialog", "filter", "any_criteria")};
        condList = new JComboBox(cond);
        nameLabel.setLabelFor(condList);
        ifPanel.add(condList);

        middleIfPanel.add(ifPanel, BorderLayout.NORTH);

        //middleIfPanel.add(Box.createRigidArea(new java.awt.Dimension(0,
        // 10)));
        criteriaList = new CriteriaList(filter);

        //JScrollPane scrollPane = new JScrollPane( criteriaList );
        middleIfPanel.add(criteriaList, BorderLayout.CENTER);

        //rootPanel.add(middleIfPanel);
        //rootPanel.add( Box.createRigidArea( new java.awt.Dimension(0,10) ) );
        JPanel middleThenPanel = new JPanel(new BorderLayout());
        centerPanel.add(middleThenPanel, BorderLayout.SOUTH);

        //middleThenPanel.setBorder(border);
        //middleThenPanel.add( Box.createRigidArea( new java.awt.Dimension(0,5)
        // ) );
        JPanel thenPanel = new JPanel();
        thenPanel.setBorder(BorderFactory.createEmptyBorder(5, 0, 5, 0));
        thenPanel.setLayout(new BoxLayout(thenPanel, BoxLayout.X_AXIS));

        addActionButton = new ButtonWithMnemonic(MailResourceLoader.getString(
                "dialog", "filter", "add_action"));
        addActionButton.setIcon(ImageLoader.getImageIcon("stock_add_16.png"));
        addActionButton.addActionListener(this);
        addActionButton.setActionCommand("ADD_ACTION");

        //thenPanel.add(addActionButton);
        //thenPanel.add( Box.createRigidArea( new java.awt.Dimension(5,0) ) );
        JLabel actionLabel = new LabelWithMnemonic(MailResourceLoader
                .getString("dialog", "filter", "action_list"));
        thenPanel.add(Box.createRigidArea(new java.awt.Dimension(5, 0)));
        thenPanel.add(actionLabel);

        thenPanel.add(Box.createHorizontalGlue());

        middleThenPanel.add(thenPanel, BorderLayout.NORTH);

        //middleThenPanel.add(Box.createRigidArea(new java.awt.Dimension(0,
        // 10)));
        actionList = new ActionList(filter, frame);
        middleThenPanel.add(actionList);

        getContentPane().add(centerPanel);

        JPanel bottomPanel = new JPanel(new BorderLayout());
        bottomPanel.setBorder(new SingleSideEtchedBorder(SwingConstants.TOP));

        JPanel buttonPanel = new JPanel(new GridLayout(1, 2, 6, 0));
        buttonPanel.setBorder(BorderFactory.createEmptyBorder(12, 12, 12, 12));

        ButtonWithMnemonic okButton = new ButtonWithMnemonic(MailResourceLoader
                .getString("global", "ok"));
        okButton.setActionCommand("CLOSE"); //$NON-NLS-1$
        okButton.addActionListener(this);
        buttonPanel.add(okButton);

        ButtonWithMnemonic cancelButton = new ButtonWithMnemonic(
                MailResourceLoader.getString("global", "cancel"));
        cancelButton.setActionCommand("CANCEL"); //$NON-NLS-1$
        cancelButton.addActionListener(this);
        buttonPanel.add(cancelButton);

        ButtonWithMnemonic helpButton = new ButtonWithMnemonic(
                MailResourceLoader.getString("global", "help"));
        buttonPanel.add(helpButton);
        bottomPanel.add(buttonPanel, BorderLayout.EAST);
        getContentPane().add(bottomPanel, BorderLayout.SOUTH);
        getRootPane().setDefaultButton(okButton);
        getRootPane().registerKeyboardAction(this, "CANCEL",
                KeyStroke.getKeyStroke(KeyEvent.VK_ESCAPE, 0),
                JComponent.WHEN_IN_FOCUSED_WINDOW);

        // associate with JavaHelp
        HelpManager.getHelpManager().enableHelpOnButton(helpButton,
                "organizing_and_managing_your_email_3");
        HelpManager.getHelpManager().enableHelpKey(getRootPane(),
                "organizing_and_managing_your_email_3");
    }

    public void updateComponents(boolean b) {
        if (b) {
            // set component values
            criteriaList.updateComponents(b);
            actionList.updateComponents(b);

            // filter description JTextField
            nameTextField.setText(filter.getName());
            nameTextField.selectAll();

            // all / match any JComboBox
            FilterRule filterRule = filter.getFilterRule();
            String value = filterRule.getCondition();

            if (value.equals("matchall")) {
                condList.setSelectedIndex(0);
            } else {
                condList.setSelectedIndex(1);
            }
        } else {
            // get values from components
            criteriaList.updateComponents(b);
            actionList.updateComponents(b);

            filter.setName(nameTextField.getText());

            int index = condList.getSelectedIndex();
            FilterRule filterRule = filter.getFilterRule();

            if (index == 0) {
                filterRule.setCondition("matchall");
            } else {
                filterRule.setCondition("matchany");
            }
        }
    }

    public void actionPerformed(ActionEvent e) {
        String action = e.getActionCommand();

        if (action.equals("CLOSE")) {
            updateComponents(false);
            setVisible(false);

            //frame.listView.update();
            dialogWasCancelled = false;
        } else if (action.equals("CANCEL")) {
            setVisible(false);
            dialogWasCancelled = true;
        } else if (action.equals("ADD_CRITERION")) {
            criteriaList.add();
        } else if (action.equals("ADD_ACTION")) {
            //System.out.println( "add" );
            actionList.add();
        }
    }

    /**
     * Returns if the dialog was cancelled or not. The dialog is cancelled if
     * the user presses the <code>Cancel</code> button or presses the
     * <code>Escape</code> key.
     * 
     * @return true if the user pressed the cancel button or escape; false
     *         otherwise.
     */
    public boolean wasCancelled() {
        return dialogWasCancelled;
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

QDox parse error in file://<WORKSPACE>/data/code-rep-dataset/Dataset2/Tasks/2233.java