error id: file://<WORKSPACE>/data/code-rep-dataset/Dataset2/Tasks/956.java
file://<WORKSPACE>/data/code-rep-dataset/Dataset2/Tasks/956.java
### com.thoughtworks.qdox.parser.ParseException: syntax error @[1,15]

error in qdox parser
file content:
```java
offset: 15
uri: file://<WORKSPACE>/data/code-rep-dataset/Dataset2/Tasks/956.java
text:
```scala
public static H@@elpManager getInstance() {

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

package org.columba.core.help;

import java.awt.Component;
import java.awt.Font;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.net.URL;
import java.util.Locale;

import javax.help.HelpBroker;
import javax.help.HelpSet;
import javax.help.JHelp;
import javax.help.TextHelpModel;
import javax.swing.JFrame;
import javax.swing.JMenu;
import javax.swing.JMenuBar;
import javax.swing.JMenuItem;
import javax.swing.JOptionPane;
import javax.swing.UIManager;

/**
 * @author fdietz
 *
 * This class manages all JavaHelp relevant helpsets, its also
 * encapsulates the broker which is used for context sensitiv help.
 * This class is a singleton.
 */
public class HelpManager {
    private static HelpManager instance;

    // name of helpset resource
    final static String helpsetName = "jhelpset";
    private JHelp jh = null;
    private HelpSet hs = null;
    private HelpBroker hb = null;
    private String hsName = null; // name for the HelpSet 
    private String hsPath = null; // URL spec to the HelpSet
    private JFrame frame;

    /**
 * Creates a new instance. This method is private because it should
 * only get called from the static getHelpManager() method.
 */
    private HelpManager() {
        ClassLoader loader = getClass().getClassLoader();
        URL url = HelpSet.findHelpSet(loader, helpsetName, "",
                Locale.getDefault());

        if (url == null) {
            url = HelpSet.findHelpSet(loader, helpsetName, ".hs",
                    Locale.getDefault());

            if (url == null) {
                // could not find it!
                JOptionPane.showMessageDialog(null, "HelpSet not found",
                    "Error", JOptionPane.ERROR_MESSAGE);

                return;
            }
        }

        try {
            hs = new HelpSet(loader, url);
        } catch (Exception ee) {
            JOptionPane.showMessageDialog(null, "HelpSet not found", "Error",
                JOptionPane.ERROR_MESSAGE);

            return;
        }

        // The JavaHelp can't be added to a BorderLayout because it
        // isnt' a component. For this demo we'll use the embeded method
        // since we don't want a Frame to be created.
        hb = hs.createHelpBroker();

        // TODO (@author fdietz): fix the font settings for the content viewer
        // setting the fonts like this doesn't seem to work
        Font font = (Font) UIManager.get("Label.font");
        hb.setFont(font);

        jh = new JHelp(hs);

        // set main font
        jh.setFont(font);

        jh.getContentViewer().setFont(font);
        jh.getCurrentNavigator().setFont(font);
    }

    /**
     * Opens the help frame.
     */
    public void openHelpFrame() {
        if (frame == null) {
            TextHelpModel m = jh.getModel();
            HelpSet hs = m.getHelpSet();
            String title = hs.getTitle();

            if (title == null || title.equals("")) {
                title = "Unnamed HelpSet"; // maybe based on HS?
            }

            frame = new JFrame(title);
            frame.getContentPane().add(jh);
            JMenuBar menuBar = new JMenuBar();
            JMenuItem mi;
            JMenu file = (JMenu) menuBar.add(new JMenu("File"));
            file.setMnemonic('F');

            mi = (JMenuItem) file.add(new JMenuItem("Exit"));
            mi.setMnemonic('x');
            mi.addActionListener(new ActionListener() {
                    public void actionPerformed(ActionEvent e) {
                        frame.setVisible(false);
                    }
                });
            //JMenu options = (JMenu) menuBar.add(new JMenu("Options"));
            //options.setMnemonic('O');
            frame.setJMenuBar(menuBar);
            frame.pack();
            frame.setExtendedState(JFrame.MAXIMIZED_BOTH);
        }
        frame.setVisible(true);
    }

    /**
 * @return
 */
    public HelpBroker getHelpBroker() {
        return hb;
    }

    /**
 * Associate button with topic ID.
 *
 * Topic ID's are listed in jhelpmap.jhm in package lib/usermanual.jar
 *
 * @param c                        component
 * @param helpID        helpID
 */
    public void enableHelpOnButton(Component c, String helpID) {
        getHelpBroker().enableHelpOnButton(c, helpID, hs);
    }

    /**
 * Enables the F1 help key on components.
 */
    public void enableHelpKey(Component c, String helpID) {
        getHelpBroker().enableHelpKey(c, helpID, hs);
    }

    /**
 * Returns the singleton help manager instance.
 */
    public static HelpManager getHelpManager() {
        if (instance == null) {
            instance = new HelpManager();
        }

        return instance;
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

QDox parse error in file://<WORKSPACE>/data/code-rep-dataset/Dataset2/Tasks/956.java