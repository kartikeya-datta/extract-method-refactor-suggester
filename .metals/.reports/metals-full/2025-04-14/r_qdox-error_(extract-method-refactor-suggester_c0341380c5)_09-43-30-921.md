error id: file://<WORKSPACE>/data/code-rep-dataset/Dataset2/Tasks/627.java
file://<WORKSPACE>/data/code-rep-dataset/Dataset2/Tasks/627.java
### com.thoughtworks.qdox.parser.ParseException: syntax error @[1,1]

error in qdox parser
file content:
```java
offset: 1
uri: file://<WORKSPACE>/data/code-rep-dataset/Dataset2/Tasks/627.java
text:
```scala
C@@olumbaLogger.log.info(

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
package org.columba.mail.gui.composer.html.action;

import org.columba.core.action.CheckBoxAction;
import org.columba.core.gui.frame.FrameMediator;
import org.columba.core.logging.ColumbaLogger;
import org.columba.core.xml.XmlElement;

import org.columba.mail.config.MailConfig;
import org.columba.mail.util.MailResourceLoader;

import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

import java.util.Observable;
import java.util.Observer;

import javax.swing.JCheckBoxMenuItem;


/**
 * CheckBox menu item for switching between HTML and text messages.
 * <br>
 * This will change the stored option, which in turn are told to
 * notify observers => editor changes btw. HTML and text etc.
 *
 * @author fdietz, Karl Peder Olesen
 */
public class EnableHtmlAction extends CheckBoxAction implements ActionListener,
    Observer {
    /**
     * @param frameMediator
     * @param name
     */
    public EnableHtmlAction(FrameMediator frameMediator) {
        super(frameMediator,
            MailResourceLoader.getString("menu", "composer",
                "menu_format_enable_html"));
    }

    /**
     * Overwritten to initialize the selection state of the
     * CheckBoxMenuItem.
     *
     * @see org.columba.core.action.CheckBoxAction#setCheckBoxMenuItem(javax.swing.JCheckBoxMenuItem)
     */
    public void setCheckBoxMenuItem(JCheckBoxMenuItem checkBoxMenuItem) {
        /* *20030912, karlpeder* Method signature changed from
         * setCheckBoxMenuItem(JCheckBoxMenuItem,AbstractFrameView).
         * Else it doesn't get called during creation of menu
         */
        super.setCheckBoxMenuItem(checkBoxMenuItem);

        ColumbaLogger.log.debug(
            "Initializing selected state of EnableHtmlAction");

        // enable/disable menuitem, based on configuration text/html state
        XmlElement optionsElement = MailConfig.get("composer_options")
                                              .getElement("/options");
        XmlElement htmlElement = optionsElement.getElement("html");

        //	create default element if not available
        if (htmlElement == null) {
            htmlElement = optionsElement.addSubElement("html");
        }

        String enableHtml = htmlElement.getAttribute("enable", "false");

        if (enableHtml.equals("true")) {
            getCheckBoxMenuItem().setSelected(true);
        } else {
            getCheckBoxMenuItem().setSelected(false);
        }

        // let the menu item listen for changes btw. html and text
        htmlElement.addObserver(this);
    }

    /**
     * Update checked state of menu item if change btw. html and text
     * has been made somewhere.
     *
     * @see java.util.Observer#update(java.util.Observable, java.lang.Object)
     */
    public void update(Observable o, Object arg) {
        XmlElement e = (XmlElement) o;

        if (e.getName().equals("html")) {
            String enableHtml = e.getAttribute("enable", "false");
            getCheckBoxMenuItem().setSelected(Boolean.valueOf(enableHtml)
                                                     .booleanValue());
        }
    }

    /* (non-Javadoc)
     * @see java.awt.event.ActionListener#actionPerformed(java.awt.event.ActionEvent)
     */
    public void actionPerformed(ActionEvent evt) {
        boolean selection = getCheckBoxMenuItem().isSelected();

        XmlElement optionsElement = MailConfig.get("composer_options")
                                              .getElement("/options");
        XmlElement htmlElement = optionsElement.getElement("html");

        //	create default element if not available
        if (htmlElement == null) {
            htmlElement = optionsElement.addSubElement("html");
        }

        // change configuration based on menuitem selection	 
        htmlElement.addAttribute("enable", Boolean.toString(selection));
        htmlElement.notifyObservers(); // notify everyone listening to this option
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

QDox parse error in file://<WORKSPACE>/data/code-rep-dataset/Dataset2/Tasks/627.java