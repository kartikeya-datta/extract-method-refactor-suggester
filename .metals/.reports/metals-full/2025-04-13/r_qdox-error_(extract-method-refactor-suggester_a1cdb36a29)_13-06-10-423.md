error id: file://<WORKSPACE>/data/code-rep-dataset/Dataset2/Tasks/9654.java
file://<WORKSPACE>/data/code-rep-dataset/Dataset2/Tasks/9654.java
### com.thoughtworks.qdox.parser.ParseException: syntax error @[1,1]

error in qdox parser
file content:
```java
offset: 1
uri: file://<WORKSPACE>/data/code-rep-dataset/Dataset2/Tasks/9654.java
text:
```scala
M@@ailInterface.config.getMainFrameOptionsConfig().getHeaderTableItem();

/*
 * Created on 26.03.2003
 *
 * To change this generated comment go to
 * Window>Preferences>Java>Code Generation>Code and Comments
 */
package org.columba.mail.gui.menu;

import org.columba.core.action.AbstractColumbaAction;
import org.columba.core.gui.frame.FrameMediator;
import org.columba.core.gui.menu.CMenu;
import org.columba.core.gui.menu.CMenuItem;
import org.columba.core.gui.menu.Menu;
import org.columba.core.gui.menu.MenuBarGenerator;
import org.columba.core.gui.util.NotifyDialog;
import org.columba.core.main.MainInterface;
import org.columba.core.plugin.MenuPluginHandler;
import org.columba.core.plugin.PluginHandlerNotFoundException;
import org.columba.mail.main.MailInterface;


/**
 * @author frd
 *
 * To change this generated comment go to
 * Window>Preferences>Java>Code Generation>Code and Comments
 */
public class MailMenu extends Menu {
    private CMenu fetchMessageSubmenu;
    private CMenu manageSubmenu;
    private CMenu sortSubMenu;

    /**
     * @param xmlRoot
     * @param frameMediator
     */
    public MailMenu(String xmlRoot, String extension,
        FrameMediator frameController) {
        super(xmlRoot, frameController);

        extendMenuFromFile(extension);

        try {
            ((MenuPluginHandler) MainInterface.pluginManager.getHandler(
                "org.columba.mail.menu")).insertPlugins(this);
        } catch (PluginHandlerNotFoundException ex) {
            NotifyDialog d = new NotifyDialog();
            d.showDialog(ex);
        }
    }

    public MenuBarGenerator createMenuBarGeneratorInstance(String xmlRoot,
        FrameMediator frameController) {
        if (menuGenerator == null) {
            menuGenerator = new MailMenuBarGenerator(frameController, xmlRoot);
        }

        return menuGenerator;
    }

    public void updatePopServerMenu() {
        CMenuItem menuItem;

        fetchMessageSubmenu.removeAll();

		AbstractColumbaAction[] actions = MailInterface.mailCheckingManager.getActions();
		for ( int i=0; i<actions.length; i++) {
			fetchMessageSubmenu.add(new CMenuItem(actions[i]));
		}
		
		/*
        for (int i = 0; i < MailInterface.popServerCollection.count(); i++) {
            POP3ServerController c = MailInterface.popServerCollection.get(i);
            c.updateAction();
            fetchMessageSubmenu.add(new CMenuItem(c.getCheckAction()));
        }
        */

		/*
        manageSubmenu.removeAll();

        for (int i = 0; i < MailInterface.popServerCollection.count(); i++) {
            POP3ServerController c = MailInterface.popServerCollection.get(i);
            c.updateAction();
            menuItem = new CMenuItem(c.getManageAction());
            manageSubmenu.add(menuItem);
        }
        */
    }

    public void updateSortMenu() {
        //FIXME

        /*
        HeaderTableItem v =
                MailConfig.getMainFrameOptionsConfig().getHeaderTableItem();

        sortSubMenu.removeAll();

        ButtonGroup group = new ButtonGroup();
        JRadioButtonMenuItem menuItem;
        String c;

        for (int i = 0; i < v.count(); i++) {
                c = (String) v.getName(i);

                boolean enabled = v.getEnabled(i);

                if (enabled == true) {
                        String str = null;
                        try {
                                str =
                                        MailResourceLoader.getString("header", c.toLowerCase());
                        } catch (Exception ex) {
                                //ex.printStackTrace();
                                System.out.println("exeption: " + ex.getMessage());
                                str = c;
                        }

                        menuItem = new JRadioButtonMenuItem(str);
                        menuItem.setActionCommand(c);
                        menuItem.addActionListener(
                                MainInterface
                                        .headerTableViewer
                                        .getHeaderItemActionListener());
                        if (c
                                .equals(
                                        MainInterface
                                                .headerTableViewer
                                                .getTableModelSorter()
                                                .getSortingColumn()))
                                menuItem.setSelected(true);

                        //menuItem.addActionListener( new AbstractColumbaActionListener( mainInterface ));

                        sortSubMenu.add(menuItem);
                        group.add(menuItem);
                }

        }

        menuItem = new JRadioButtonMenuItem("In Order Received");
        menuItem.addActionListener(
                MainInterface.headerTableViewer.getHeaderItemActionListener());
        sortSubMenu.add(menuItem);
        group.add(menuItem);

        sortSubMenu.addSeparator();

        group = new ButtonGroup();

        menuItem = new JRadioButtonMenuItem("Ascending");
        menuItem.addActionListener(
                MainInterface.headerTableViewer.getHeaderItemActionListener());
        if (MainInterface
                .headerTableViewer
                .getTableModelSorter()
                .getSortingOrder()
                == true)
                menuItem.setSelected(true);

        sortSubMenu.add(menuItem);
        group.add(menuItem);
        menuItem = new JRadioButtonMenuItem("Descending");
        menuItem.addActionListener(
                MainInterface.headerTableViewer.getHeaderItemActionListener());
        if (MainInterface
                .headerTableViewer
                .getTableModelSorter()
                .getSortingOrder()
                == false)
                menuItem.setSelected(true);
        sortSubMenu.add(menuItem);
        group.add(menuItem);
        */
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

QDox parse error in file://<WORKSPACE>/data/code-rep-dataset/Dataset2/Tasks/9654.java