error id: file://<WORKSPACE>/data/code-rep-dataset/Dataset2/Tasks/4148.java
file://<WORKSPACE>/data/code-rep-dataset/Dataset2/Tasks/4148.java
### com.thoughtworks.qdox.parser.ParseException: syntax error @[1,1]

error in qdox parser
file content:
```java
offset: 1
uri: file://<WORKSPACE>/data/code-rep-dataset/Dataset2/Tasks/4148.java
text:
```scala
C@@olumbaLogger.log.fine("converting configuration to new version...");

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
package org.columba.mail.config;

import org.columba.core.config.DefaultXmlConfig;
import org.columba.core.config.GuiItem;
import org.columba.core.config.TableItem;
import org.columba.core.config.ViewItem;
import org.columba.core.config.WindowItem;
import org.columba.core.logging.ColumbaLogger;
import org.columba.core.xml.XmlElement;

import java.io.File;


public class MainFrameOptionsXmlConfig extends DefaultXmlConfig {
    // private File file;
    WindowItem windowItem;
    GuiItem guiItem;
    TableItem headerTableItem;
    ViewItem viewItem;
    boolean initialVersionWasApplied = false;

    public MainFrameOptionsXmlConfig(File file) {
        super(file);
    }

    public boolean load() {
        boolean result = super.load();

        //		apply initial version information
        XmlElement root = getRoot().getElement(0);
        String version = root.getAttribute("version");

        if (version == null) {
            initialVersionWasApplied = true;
            root.addAttribute("version", "1.0");
        }

        convert();

        return result;
    }

    protected void convert() {
        // add initial messageframe treenode
        XmlElement root = getRoot();
        String version = root.getAttribute("version");

        if (initialVersionWasApplied) {
            ColumbaLogger.log.info("converting configuration to new version...");

            XmlElement gui = root.getElement("/options/gui");
            XmlElement messageframe = new XmlElement("messageframe");
            gui.addElement(messageframe);

            XmlElement view = new XmlElement("view");
            messageframe.addElement(view);
            view.addAttribute("id", "messageframe");

            XmlElement window = new XmlElement("window");
            window.addAttribute("width", "640");
            window.addAttribute("height", "480");
            window.addAttribute("maximized", "true");
            view.addElement(window);

            XmlElement toolbars = new XmlElement("toolbars");
            toolbars.addAttribute("main", "true");
            view.addElement(toolbars);

            XmlElement splitpanes = new XmlElement("splitpanes");
            splitpanes.addAttribute("main", "200");
            splitpanes.addAttribute("header", "200");
            splitpanes.addAttribute("attachment", "100");
            view.addElement(splitpanes);
        }
    }

    public TableItem getTableItem() {
        if (headerTableItem == null) {
            headerTableItem = new TableItem(getRoot().getElement("/options/gui/table"));
        }

        return headerTableItem;
    }

    public ViewItem getViewItem() {
        if (viewItem == null) {
            viewItem = new ViewItem(getRoot().getElement("/options/gui/viewlist/view"));
        }

        return viewItem;
    }

    public GuiItem getGuiItem() {
        if (guiItem == null) {
            guiItem = new GuiItem(getRoot().getElement("/options/gui"));
        }

        return guiItem;
    }

    public WindowItem getWindowItem() {
        if (windowItem == null) {
            windowItem = new WindowItem(getRoot().getElement("/options/gui/viewlist/view/window"));
        }

        return windowItem;
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

QDox parse error in file://<WORKSPACE>/data/code-rep-dataset/Dataset2/Tasks/4148.java