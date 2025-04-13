error id: file://<WORKSPACE>/data/code-rep-dataset/Dataset2/Tasks/824.java
file://<WORKSPACE>/data/code-rep-dataset/Dataset2/Tasks/824.java
### com.thoughtworks.qdox.parser.ParseException: syntax error @[1,1]

error in qdox parser
file content:
```java
offset: 1
uri: file://<WORKSPACE>/data/code-rep-dataset/Dataset2/Tasks/824.java
text:
```scala
.@@getTableSelection().getFolder();

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
package org.columba.mail.gui.table;

import java.awt.Insets;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.FocusEvent;
import java.awt.event.FocusListener;

import javax.swing.BorderFactory;
import javax.swing.JButton;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JTextField;
import javax.swing.JToggleButton;

import org.columba.core.gui.util.ButtonWithMnemonic;
import org.columba.core.gui.util.CTextField;
import org.columba.core.gui.util.ImageLoader;
import org.columba.core.gui.util.LabelWithMnemonic;
import org.columba.core.gui.util.ToolbarToggleButton;
import org.columba.mail.folder.MessageFolder;
import org.columba.mail.gui.config.search.SearchFrame;
import org.columba.mail.gui.frame.MailFrameMediator;
import org.columba.mail.gui.table.model.TableModelFilter;
import org.columba.mail.main.MailInterface;
import org.columba.mail.util.MailResourceLoader;

import com.jgoodies.forms.builder.PanelBuilder;
import com.jgoodies.forms.layout.CellConstraints;
import com.jgoodies.forms.layout.FormLayout;


public class FilterToolbar extends JPanel implements ActionListener {
    public JToggleButton newButton;
    public JToggleButton oldButton;
    private JToggleButton answeredButton;
    private JToggleButton flaggedButton;
    private JToggleButton expungedButton;
    private JToggleButton draftButton;
    private JToggleButton attachmentButton;
    private JToggleButton secureButton;
    public JButton clearButton;
    public JButton advancedButton;
    private JLabel label;
    private JTextField textField;
    private TableController tableController;

    public FilterToolbar(TableController headerTableViewer) {
        super();
        this.tableController = headerTableViewer;

        initComponents();
        layoutComponents();
    }

    public void initComponents() {
        //addSeparator();
        newButton = new ToolbarToggleButton(ImageLoader.getSmallImageIcon(
                    "mail-new.png"));
        newButton.setToolTipText(MailResourceLoader.getString("menu",
                "mainframe", "filtertoolbar_unread"));
        newButton.addActionListener(this);
        newButton.setActionCommand("NEW");
        newButton.setSelected(false);
        newButton.setMargin(new Insets(0, 0, 0, 0));

        answeredButton = new ToolbarToggleButton(ImageLoader.getSmallImageIcon(
                    "reply_small.png"));
        answeredButton.addActionListener(this);
        answeredButton.setToolTipText(MailResourceLoader.getString("menu",
                "mainframe", "filtertoolbar_answered"));
        answeredButton.setActionCommand("ANSWERED");
        answeredButton.setMargin(new Insets(0, 0, 0, 0));
        answeredButton.setSelected(false);

        flaggedButton = new ToolbarToggleButton(ImageLoader.getSmallImageIcon(
                    "mark-as-important-16.png"));
        flaggedButton.setToolTipText(MailResourceLoader.getString("menu",
                "mainframe", "filtertoolbar_flagged"));
        flaggedButton.setMargin(new Insets(0, 0, 0, 0));
        flaggedButton.addActionListener(this);
        flaggedButton.setActionCommand("FLAGGED");
        flaggedButton.setSelected(false);

        expungedButton = new ToolbarToggleButton(ImageLoader.getSmallImageIcon(
                    "stock_delete-16.png"));
        expungedButton.setToolTipText(MailResourceLoader.getString("menu",
                "mainframe", "filtertoolbar_expunged"));
        expungedButton.setMargin(new Insets(0, 0, 0, 0));
        expungedButton.addActionListener(this);
        expungedButton.setActionCommand("EXPUNGED");
        expungedButton.setSelected(false);

        attachmentButton = new ToolbarToggleButton(ImageLoader.getSmallImageIcon(
                    "attachment.png"));
        attachmentButton.setToolTipText(MailResourceLoader.getString("menu",
                "mainframe", "filtertoolbar_attachment"));
        attachmentButton.setMargin(new Insets(0, 0, 0, 0));
        attachmentButton.addActionListener(this);
        attachmentButton.setActionCommand("ATTACHMENT");
        attachmentButton.setSelected(false);

        label = new LabelWithMnemonic(MailResourceLoader.getString("menu",
                    "mainframe", "filtertoolbar_header"));

        textField = new CTextField();
        label.setLabelFor(textField);
        textField.addActionListener(this);
        textField.setActionCommand("TEXTFIELD");
        textField.addFocusListener(new FocusListener() {
                public void focusGained(FocusEvent e) {
                }

                public void focusLost(FocusEvent e) {
                    TableModelFilter model = tableController.getTableModelFilteredView();

                    try {
                        model.setPatternString(textField.getText());
                    } catch (Exception ex) {
                        ex.printStackTrace();
                    }
                }
            });

        clearButton = new ButtonWithMnemonic(MailResourceLoader.getString(
                    "menu", "mainframe", "filtertoolbar_clear"));
        clearButton.setToolTipText(MailResourceLoader.getString("menu",
                "mainframe", "filtertoolbar_clear_tooltip"));

        attachmentButton.setSelected(false);
        clearButton.setActionCommand("CLEAR");
        clearButton.addActionListener(this);

        advancedButton = new ButtonWithMnemonic(MailResourceLoader.getString(
                    "menu", "mainframe", "filtertoolbar_advanced"));
        advancedButton.setToolTipText(MailResourceLoader.getString("menu",
                "mainframe", "filtertoolbar_advanced_tooltip"));

        attachmentButton.setSelected(false);
        advancedButton.setActionCommand("ADVANCED");
        advancedButton.addActionListener(this);
    }

    public void layoutComponents() {
        setBorder(BorderFactory.createEmptyBorder(2, 2, 2, 2));

        FormLayout l = new FormLayout("default, default, default, default, default, 3dlu, default, 3dlu, fill:default:grow, 3dlu, default, 3dlu, default",
                "fill:default:grow");
        PanelBuilder b = new PanelBuilder(this, l);

        CellConstraints c = new CellConstraints();

        b.add(newButton, c.xy(1, 1));
        b.add(answeredButton, c.xy(2, 1));
        b.add(flaggedButton, c.xy(3, 1));
        b.add(expungedButton, c.xy(4, 1));
        b.add(attachmentButton, c.xy(5, 1));

        b.add(label, c.xy(7, 1));
        b.add(textField, c.xy(9, 1));

        b.add(clearButton, c.xy(11, 1));
        b.add(advancedButton, c.xy(13, 1));
    }

    public void update() throws Exception {
        tableController.getTableModelFilteredView().setDataFiltering(true);

        tableController.getUpdateManager().update();
    }

    public void actionPerformed(ActionEvent e) {
        String action = e.getActionCommand();

        try {
            //TableModelFilteredView model =
            // MainInterface.tableController.getHeaderTable().getTableModelFilteredView();
            TableModelFilter model = tableController.getTableModelFilteredView();

            if (action.equals("ADVANCED")) {
                MessageFolder searchFolder = (MessageFolder) MailInterface.treeModel.getFolder(106);

                MessageFolder folder = (MessageFolder) ((MailFrameMediator)tableController.getFrameController())
                                                        .getTableSelection()[0].getFolder();

                if (folder == null) {
                    return;
                }

                SearchFrame frame = new SearchFrame(tableController.getFrameController(),
                        searchFolder, folder);

                //frame.setSourceFolder(folder);
                //frame.setVisible(true);
            } else if (action.equals("NEW")) {
                model.setNewFlag(!model.getNewFlag());
                update();
            } else if (action.equals("ANSWERED")) {
                model.setAnsweredFlag(!model.getAnsweredFlag());
                update();
            } else if (action.equals("FLAGGED")) {
                model.setFlaggedFlag(!model.getFlaggedFlag());
                update();
            } else if (action.equals("EXPUNGED")) {
                model.setExpungedFlag(!model.getExpungedFlag());
                update();
            } else if (action.equals("ATTACHMENT")) {
                model.setAttachmentFlag(!model.getAttachmentFlag());
                update();
            } else if (action.equals("TEXTFIELD")) {
                model.setPatternString(textField.getText());
                update();
            } else if (action.equals("CLEAR")) {
                if (model == null) {
                    return;
                }

                model.setNewFlag(false);

                //model.setOldFlag(true);
                model.setAnsweredFlag(false);
                model.setFlaggedFlag(false);
                model.setExpungedFlag(false);
                model.setAttachmentFlag(false);
                model.setPatternString("");

                textField.setText("");

                newButton.setSelected(false);

                //oldButton.setSelected(true);
                answeredButton.setSelected(false);
                flaggedButton.setSelected(false);
                expungedButton.setSelected(false);
                attachmentButton.setSelected(false);

                tableController.getTableModelFilteredView().setDataFiltering(true);
                update();
            }
        } catch (Exception ex) {
            ex.printStackTrace();
        }
    }

    public void enableNew(boolean b) {
        newButton.setSelected(b);
    }

    public void enableAnswered(boolean b) {
        answeredButton.setSelected(b);
    }

    public void enableFlagged(boolean b) {
        flaggedButton.setSelected(b);
    }

    public void enableAttachment(boolean b) {
        attachmentButton.setSelected(b);
    }

    public void enableExpunged(boolean b) {
        expungedButton.setSelected(b);
    }

    public void setPattern(String pattern) {
        textField.setText(pattern);
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

QDox parse error in file://<WORKSPACE>/data/code-rep-dataset/Dataset2/Tasks/824.java