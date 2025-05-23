error id: file://<WORKSPACE>/data/code-rep-dataset/Dataset2/Tasks/10053.java
file://<WORKSPACE>/data/code-rep-dataset/Dataset2/Tasks/10053.java
### com.thoughtworks.qdox.parser.ParseException: syntax error @[1,24]

error in qdox parser
file content:
```java
offset: 24
uri: file://<WORKSPACE>/data/code-rep-dataset/Dataset2/Tasks/10053.java
text:
```scala
"menu_format_paragraph")@@,"menu_format_paragraph");

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

import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.ContainerEvent;
import java.awt.event.ContainerListener;
import java.util.Enumeration;
import java.util.Observable;
import java.util.Observer;
import java.util.logging.Logger;

import javax.swing.ButtonGroup;
import javax.swing.JRadioButtonMenuItem;
import javax.swing.text.html.HTML;

import org.columba.core.action.IMenu;
import org.columba.core.gui.frame.FrameMediator;
import org.columba.core.xml.XmlElement;
import org.columba.mail.config.MailConfig;
import org.columba.mail.gui.composer.ComposerController;
import org.columba.mail.gui.composer.html.HtmlEditorController;
import org.columba.mail.gui.composer.html.util.FormatInfo;
import org.columba.mail.util.MailResourceLoader;

/**
 * Submenu for formatting text.
 * <p>
 * Possible values are:
 *  - normal
 *  - preformatted
 *  - heading 1
 *  - heading 2
 *  - heading 3
 *  - address
 *
 * Note: This is the place to add further formats like lists, etc.
 *
 * Note: The HtmlEditorView and -Controller must of course also support
 *       new formats when adding them!
 *
 * @author fdietz, Karl Peder Olesen (karlpeder)
 */
public class ParagraphMenu extends IMenu implements Observer, ActionListener,
    ContainerListener {

    /** JDK 1.4+ logging framework logger, used for logging. */
    private static final Logger LOG = Logger.getLogger("org.columba.mail.gui.composer.html.action");

    /** Html tags corresponding to supported paragraph styles */
    public static final HTML.Tag[] STYLE_TAGS = {
        HTML.Tag.P, HTML.Tag.PRE, HTML.Tag.H1, HTML.Tag.H2, HTML.Tag.H3,
        HTML.Tag.ADDRESS
    };

    protected ButtonGroup group;

    /**
     * @param controller
     * @param caption
     */
    public ParagraphMenu(FrameMediator controller) {
        super(controller,
            MailResourceLoader.getString("menu", "composer",
                "menu_format_paragraph"));

        initMenu();

        // register for text selection changes
        ((ComposerController) controller).getEditorController().addObserver(this);

        // register for changes to the editor
        ((ComposerController) controller).addContainerListenerForEditor(this);

        // register for changes to editor type (text / html)
        XmlElement optionsElement = MailConfig.getInstance().get("composer_options")
                                                        .getElement("/options");
        XmlElement htmlElement = optionsElement.getElement("html");

        if (htmlElement == null) {
            htmlElement = optionsElement.addSubElement("html");
        }

        String enableHtml = htmlElement.getAttribute("enable", "false");
        htmlElement.addObserver(this);

        // set initial enabled state
        setEnabled(Boolean.valueOf(enableHtml).booleanValue());
    }

    /**
     * Initializes the sub menu by creating a menu item for each
     * available paragraph style. All menu items are grouped in a
     * ButtonGroup (as radio buttons).
     */
    protected void initMenu() {
        group = new ButtonGroup();

        for (int i = 0; i < STYLE_TAGS.length; i++) {
            JRadioButtonMenuItem m = new ParagraphFormatMenuItem(STYLE_TAGS[i]);
            m.addActionListener(this);
            add(m);

            group.add(m);
        }
    }

    /**
     * Method is called when text selection has changed.
     * <p>
     * Set state of togglebutton / -menu to pressed / not pressed
     * when selections change.
     *
     * @see java.util.Observer#update(java.util.Observable, java.lang.Object)
     */
    public void update(Observable arg0, Object arg1) {
        if (arg0 instanceof HtmlEditorController) {
            // select the menu item corresponding to present format
            FormatInfo info = (FormatInfo) arg1;

            if (info.isHeading1()) {
                selectMenuItem(HTML.Tag.H1);
            } else if (info.isHeading2()) {
                selectMenuItem(HTML.Tag.H2);
            } else if (info.isHeading3()) {
                selectMenuItem(HTML.Tag.H3);
            } else if (info.isPreformattet()) {
                selectMenuItem(HTML.Tag.PRE);
            } else if (info.isAddress()) {
                selectMenuItem(HTML.Tag.ADDRESS);
            } else {
                // select the "Normal" entry as default
                selectMenuItem(HTML.Tag.P);
            }
        } else if (arg0 instanceof XmlElement) {
            // possibly change btw. html and text
            XmlElement e = (XmlElement) arg0;

            if (e.getName().equals("html")) {
                String enableHtml = e.getAttribute("enable", "false");

                // This action should only be enabled in html mode
                setEnabled(Boolean.valueOf(enableHtml).booleanValue());
            }
        }
    }

    /**
     * Private utility to select a given sub menu, given the
     * corresponding html tag.
     * If such a sub menu does not exist - nothing happens
     */
    private void selectMenuItem(HTML.Tag tag) {
        Enumeration e = group.getElements();
        while (e.hasMoreElements()) {
            ParagraphFormatMenuItem item = (ParagraphFormatMenuItem) e.nextElement();

            if (item.getAssociatedTag().equals(tag)) {
                item.setSelected(true);

                return; // done
            }
        }
    }

    public void actionPerformed(ActionEvent e) {
        HtmlEditorController ctrl = (HtmlEditorController) 
            ((ComposerController) controller).getEditorController();

        // set paragraph formatting according to the given action
        ParagraphFormatMenuItem source = (ParagraphFormatMenuItem)e.getSource();
        ctrl.setParagraphFormat(source.getAssociatedTag());
    }

    /**
     * This event could mean that a the editor controller has changed.
     * Therefore this object is re-registered as observer to keep
     * getting information about format changes.
     *
     * @see java.awt.event.ContainerListener#componentAdded(java.awt.event.ContainerEvent)
     */
    public void componentAdded(ContainerEvent e) {
        LOG.info("Re-registering as observer on editor controller");
        ((ComposerController) getFrameMediator()).getEditorController()
            .addObserver(this);
    }

    public void componentRemoved(ContainerEvent e) {}
    
    /**
     * A specialized radio button menu item class used to render paragraph
     * format actions.
     */
    protected static class ParagraphFormatMenuItem extends JRadioButtonMenuItem {
        protected HTML.Tag tag;
        
        public ParagraphFormatMenuItem(HTML.Tag tag) {
            super(MailResourceLoader.getString("menu", "composer",
                "menu_format_paragraph_"+ tag.toString()) );
            this.tag = tag;
        }
        
        public HTML.Tag getAssociatedTag() {
            return tag;
        }
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

QDox parse error in file://<WORKSPACE>/data/code-rep-dataset/Dataset2/Tasks/10053.java