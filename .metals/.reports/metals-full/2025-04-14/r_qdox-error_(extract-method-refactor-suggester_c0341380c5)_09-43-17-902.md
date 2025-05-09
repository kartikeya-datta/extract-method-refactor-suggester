error id: file://<WORKSPACE>/data/code-rep-dataset/Dataset5/Tasks/12223.java
file://<WORKSPACE>/data/code-rep-dataset/Dataset5/Tasks/12223.java
### com.thoughtworks.qdox.parser.ParseException: syntax error @[1,1]

error in qdox parser
file content:
```java
offset: 1
uri: file://<WORKSPACE>/data/code-rep-dataset/Dataset5/Tasks/12223.java
text:
```scala
b@@uilt_label.setText("Built: " + new Date(Version.getTime()).toString());

/* *******************************************************************
 * Copyright (c) 1999-2001 Xerox Corporation, 
 *               2002 Palo Alto Research Center, Incorporated (PARC).
 * All rights reserved. 
 * This program and the accompanying materials are made available 
 * under the terms of the Eclipse Public License v1.0 
 * which accompanies this distribution and is available at 
 * http://www.eclipse.org/legal/epl-v10.html 
 *  
 * Contributors: 
 *     Xerox/PARC     initial implementation 
 * ******************************************************************/


package org.aspectj.ajde.ui.swing;

import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.Component;
import java.awt.Dimension;
import java.awt.event.ActionEvent;
import java.io.IOException;
import java.util.Date;

import javax.swing.BorderFactory;
//import javax.swing.Box;
//import javax.swing.ButtonGroup;
import javax.swing.ImageIcon;
import javax.swing.JButton;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JTabbedPane;
import javax.swing.JTextArea;
import javax.swing.UIManager;
import javax.swing.border.Border;
import javax.swing.border.TitledBorder;

import org.aspectj.ajde.Ajde;
import org.aspectj.bridge.Version;

/**
 * UI for setting user-configureable options.
 *
 * @author Mik Kersten
 */
public class OptionsFrame extends JFrame {

	private static final long serialVersionUID = -859222442871124487L;

	// XXX using \n b/c JTextArea.setLineWrap(true) lineates inside words.
    private static final String ABOUT_TEXT =
        "\nThe AspectJ compiler and core tools are produced by the\n" +
        "AspectJ project.\n\n" +
        "This software is distributed under the Eclipse Public License\n" +
        "version 1.0, approved by the Open Source Initiative as\n" +
        "conforming to the Open Source Definition.\n\n" +
        "For support or for more information about the AspectJ\n" +
        "project or the license, visit the project home page at\n" + 
        "    http://eclipse.org/aspectj\n\n" +
        "If you find a bug (not solved by the documentation in the\n" +
        "Development Environment Guide available with this release,\n" +
        "any release notes, or the bug database), please submit steps\n" +
        "to reproduce the bug (using the IDE component) at:\n" + 
        "    http://bugs.eclipse.org/bugs/enter_bug.cgi?product=AspectJ";
        
    private JTabbedPane main_tabbedPane = new JTabbedPane();
    private JPanel button_panel = new JPanel();
    private JButton apply_button = new JButton();
    private JButton cancel_button = new JButton();
    private JButton ok_button = new JButton();
    private TitledBorder titledBorder1;
    private TitledBorder titledBorder2;
    private TitledBorder titledBorder3;
//    private Border border1;
    private TitledBorder titledBorder4;
    private TitledBorder titledBorder5;
//    private Border border2;
    private TitledBorder titledBorder6;
//    private Box temp_box = Box.createVerticalBox();
//    private Border border3;
    private TitledBorder titledBorder7;
    private Border border4;
    private TitledBorder titledBorder8;
    private Border border5;
    private TitledBorder titledBorder9;
//    private Border border6;
    private TitledBorder titledBorder10;
//    private ButtonGroup views_buttonGroup = new ButtonGroup();
    private Border border7;
    private TitledBorder titledBorder11;
    private Border border8;
    private TitledBorder titledBorder12;
    private JPanel about_panel = new JPanel();
    private BorderLayout borderLayout9 = new BorderLayout();
    JTextArea jTextArea1 = new JTextArea();
    JPanel jPanel1 = new JPanel();
    JLabel version_label = new JLabel();
    JLabel jLabel1 = new JLabel();
    BorderLayout borderLayout1 = new BorderLayout();
    Border border9;
    JLabel built_label = new JLabel();

    public OptionsFrame(IconRegistry icons) {
        try {
            jbInit();

            this.setTitle("AJDE Settings");
            this.setIconImage(((ImageIcon)icons.getBrowserOptionsIcon()).getImage());
            this.setSize(500, 500);
            this.setLocation(200, 100);

			version_label.setText("Version: " + Version.text);
			built_label.setText("Built: " + new Date(Version.time).toString());

            addOptionsPanel(new BuildOptionsPanel());
            loadOptions();
       }
        catch(Exception e) {
            Ajde.getDefault().getErrorHandler().handleError("Could not open OptionsFrame", e);
        }
    }

	/**
	 * Adds the panel in the second-to-last postion.
	 */
	public void addOptionsPanel(OptionsPanel panel) {
		main_tabbedPane.add(panel, main_tabbedPane.getComponentCount()-1);
		loadOptions();
	}

	public void removeOptionsPanel(OptionsPanel panel) {
		main_tabbedPane.remove(panel);
	}

    public void showPanel(OptionsPanel panel) {
        setVisible(true);
        main_tabbedPane.setSelectedComponent(panel);
    }

	private void loadOptions() {
		try {
			Component[] components = main_tabbedPane.getComponents();
			for
(int i = 0; i < components.length; i++) {
				if (components[i] instanceof OptionsPanel) {
					((OptionsPanel)components[i]).loadOptions();
				}
			}
		} catch (IOException ioe) {
			Ajde.getDefault().getErrorHandler().handleError("Could not load options.", ioe);
		}
	}

	private void saveOptions() {
		try {
			Component[] components = main_tabbedPane.getComponents();
			for (int i = 0; i < components.length; i++) {
				if (components[i] instanceof OptionsPanel) {
					((OptionsPanel)components[i]).saveOptions();
				}
			}
		} catch (IOException ioe) {
			Ajde.getDefault().getErrorHandler().handleError("Could not load options.", ioe);
		}
	}

    private void close() {
        this.setVisible(false);
    }

    private void apply_button_actionPerformed(ActionEvent e) {
        saveOptions();
    }

    private void ok_button_actionPerformed(ActionEvent e) {
        saveOptions();
        close();
    }

    private void cancel_button_actionPerformed(ActionEvent e) {
        close();
    }

    private void jbInit() throws Exception {
        titledBorder1 = new TitledBorder(BorderFactory.createEtchedBorder(Color.white,new Color(156, 156, 158)),"Sorting");
        titledBorder2 = new TitledBorder(BorderFactory.createEtchedBorder(Color.white,new Color(148, 145, 140)),"Associations (navigeable relations between sturcture nodes)");
        titledBorder3 = new TitledBorder(BorderFactory.createEtchedBorder(Color.white,new Color(156, 156, 158)),"Member Filtering (nodes to exclude from view)");
        BorderFactory.createLineBorder(Color.black,2);
        titledBorder4 = new TitledBorder(BorderFactory.createEtchedBorder(Color.white,new Color(148, 145, 140)),"Compile Options");
        titledBorder5 = new TitledBorder("");
        BorderFactory.createLineBorder(Color.black,2);
        titledBorder6 = new TitledBorder(BorderFactory.createEtchedBorder(Color.white,new Color(148, 145, 140)),"Run Options");
        BorderFactory.createEtchedBorder(Color.white,new Color(156, 156, 158));
        titledBorder7 = new TitledBorder(BorderFactory.createEtchedBorder(Color.white,new Color(156, 156, 158)),"Granularity (all nodes below selected level will be hidden)");
        border4 = BorderFactory.createEtchedBorder(Color.white,new Color(156, 156, 158));
        titledBorder8 = new TitledBorder(border4,"Member Visibility");
        border5 = BorderFactory.createEtchedBorder(Color.white,new Color(156, 156, 158));
        titledBorder9 = new TitledBorder(border5,"Member Modifiers");
        BorderFactory.createEmptyBorder();
        titledBorder10 = new TitledBorder(BorderFactory.createEtchedBorder(Color.white,new Color(148, 145, 140)),"Structure View Properties");
        border7 = BorderFactory.createEtchedBorder(Color.white,new Color(156, 156, 158));
        titledBorder11 = new TitledBorder(border7,"Member Kinds");
        border8 = BorderFactory.createEtchedBorder(Color.white,new Color(148, 145, 140));
        titledBorder12 = new TitledBorder(border8,"Build Paths");
        border9 = BorderFactory.createEmptyBorder(6,6,6,6);
        jPanel1.setLayout(borderLayout1);
        jLabel1.setFont(new java.awt.Font("Dialog", 1, 14));
        jLabel1.setText("AspectJ Development Environment (AJDE)");
        version_label.setFont(new java.awt.Font("Dialog", 1, 12));
        version_label.setText("Version: ");
        apply_button.setFont(new java.awt.Font("Dialog", 0, 11));
        apply_button.setMaximumSize(new Dimension(70, 24));
        apply_button.setMinimumSize(new Dimension(63, 24));
        apply_button.setPreferredSize(new Dimension(70, 24));
        apply_button.setText("Apply");
        apply_button.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(ActionEvent e) {
                apply_button_actionPerformed(e);
            }
        });
        cancel_button.setFont(new java.awt.Font("Dialog", 0, 11));
        cancel_button.setMaximumSize(new Dimension(70, 24));
        cancel_button.setMinimumSize(new Dimension(67, 24));
        cancel_button.setPreferredSize(new Dimension(70, 24));
        cancel_button.setText("Cancel");
        cancel_button.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(ActionEvent e) {
                cancel_button_actionPerformed(e);
            }
        });
        ok_button.setFont(new java.awt.Font("Dialog", 0, 11));
        ok_button.setMaximumSize(new Dimension(70, 24));
        ok_button.setMinimumSize(new Dimension(49, 24));
        ok_button.setPreferredSize(new Dimension(70, 24));
        ok_button.setText("OK");
        ok_button.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(ActionEvent e) {
                ok_button_actionPerformed(e);
            }
        });
        main_tabbedPane.setFont(new java.awt.Font("Dialog", 0, 11));
        titledBorder1.setTitle("Ordering (sort order of nodes)");
        titledBorder1.setTitleFont(new java.awt.Font("Dialog", 0, 11));
        titledBorder2.setTitle("Associations (navigeable relations between structure nodes)");
        titledBorder2.setTitleFont(new java.awt.Font("Dialog", 0, 11));
        titledBorder3.setTitleFont(new java.awt.Font("Dialog", 0, 11));
        titledBorder6.setTitleFont(new java.awt.Font("Dialog", 0, 11));
        titledBorder5.setTitleFont(new java.awt.Font("Dialog", 0, 11));
        titledBorder4.setTitle("Compiler Flags");
        titledBorder4.setTitleFont(new java.awt.Font("Dialog", 0, 11));
        titledBorder7.setTitleFont(new java.awt.Font("Dialog", 0, 11));
        titledBorder8.setTitle("Access Modifiers");
        titledBorder8.setTitleFont(new java.awt.Font("Dialog", 0, 11));
        titledBorder9.setTitleFont(new java.awt.Font("Dialog", 0, 11));
        titledBorder10.setTitleFont(new java.awt.Font("Dialog", 0, 11));
        titledBorder11.setTitleFont(new java.awt.Font("Dialog", 0, 11));
        titledBorder12.setTitleFont(new java.awt.Font("Dialog", 0, 11));
        about_panel.setLayout(borderLayout9);
        jTextArea1.setBackground(UIManager.getColor("ColorChooser.background"));
        jTextArea1.setFont(new java.awt.Font("SansSerif", 0, 12));
        jTextArea1.setEditable(false);
        jTextArea1.setText(ABOUT_TEXT);
        
        about_panel.setBorder(border9);
        built_label.setText("Built: ");
        built_label.setFont(new java.awt.Font("Dialog", 1, 12));
        main_tabbedPane.add(about_panel,   "About AJDE");
        this.getContentPane().add(button_panel, BorderLayout.SOUTH);
        button_panel.add(ok_button, null);
        button_panel.add(cancel_button, null);
        button_panel.add(apply_button, null);
        this.getContentPane().add(main_tabbedPane, BorderLayout.CENTER);
        about_panel.add(jTextArea1, BorderLayout.CENTER);
        about_panel.add(jPanel1,  BorderLayout.NORTH);
        jPanel1.add(jLabel1,  BorderLayout.NORTH);
        jPanel1.add(version_label,  BorderLayout.CENTER);
        jPanel1.add(built_label, BorderLayout.SOUTH);
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

QDox parse error in file://<WORKSPACE>/data/code-rep-dataset/Dataset5/Tasks/12223.java