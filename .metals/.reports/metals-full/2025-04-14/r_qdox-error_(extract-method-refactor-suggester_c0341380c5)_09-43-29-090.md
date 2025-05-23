error id: file://<WORKSPACE>/data/code-rep-dataset/Dataset2/Tasks/400.java
file://<WORKSPACE>/data/code-rep-dataset/Dataset2/Tasks/400.java
### com.thoughtworks.qdox.parser.ParseException: syntax error @[1,1]

error in qdox parser
file content:
```java
offset: 1
uri: file://<WORKSPACE>/data/code-rep-dataset/Dataset2/Tasks/400.java
text:
```scala
F@@ilterDialog dialog = new FilterDialog(getFrameMediator().getView().getFrame(), filter);

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
package org.columba.mail.folder.command;

import java.util.logging.Logger;

import org.columba.core.command.DefaultCommandReference;
import org.columba.core.command.StatusObservableImpl;
import org.columba.core.command.WorkerStatusController;
import org.columba.core.xml.XmlElement;

import org.columba.mail.command.FolderCommand;
import org.columba.mail.command.FolderCommandReference;
import org.columba.mail.filter.Filter;
import org.columba.mail.folder.MessageFolder;
import org.columba.mail.gui.config.filter.FilterDialog;

import org.columba.ristretto.message.Header;


/**
 * This class is used to create a filter based on the currently selected
 * message (if multiple selected, the first one in the selection array is used) -
 * either using Subject, To or From.
 *
 * @author Karl Peder Olesen (karlpeder), 20030620
 */
public class CreateFilterOnMessageCommand extends FolderCommand {

    /** JDK 1.4+ logging framework logger, used for logging. */
    private static final Logger LOG = Logger.getLogger("org.columba.mail.folder.command");

    /** Used for creating a filter based on Subject */
    public static final String FILTER_ON_SUBJECT = "Subject";

    /** Used for creating a filter based on From */
    public static final String FILTER_ON_FROM = "From";

    /** Used for creating a filter based on To */
    public static final String FILTER_ON_TO = "To";

    /** Type of filter to create */
    private String filterType;

    /** Filter created */
    private Filter filter = null;

    /** The source folder where the filter should be added to. */
    private MessageFolder srcFolder;

    /**
     * Constructor for CreateFilterOnMessageCommand. Calls super constructor
     * and saves flag for which kind of filter to create. Default for filter
     * type is FILTER_ON_SUBJECT.
     *
     * @param references
     * @param filterType  Which type of filter to create. Used defined constants
     */
    public CreateFilterOnMessageCommand(DefaultCommandReference[] references,
        String filterType) {
        super(references);
        this.filterType = filterType;
    }

    /**
     * Displays filter dialog for user modifications after creation of the
     * filter in execute. If the user cancels the dialog then the filter is not
     * stored into the filter list in the source folder.
     *
     * @see org.columba.core.command.Command#updateGUI()
     */
    public void updateGUI() throws Exception {
        if ((filter != null) && (srcFolder != null)) {
            FilterDialog dialog = new FilterDialog(filter);

            if (!dialog.wasCancelled()) {
                srcFolder.getFilterList().add(filter);
            }
        }
    }

    /**
     * This method generates filter based on Subject, From or To (depending on
     * parameter transferred to constructor) of the currently selected message.
     *
     * @param worker
     * @see org.columba.core.command.Command#execute(Worker)
     */
    public void execute(WorkerStatusController worker)
        throws Exception {
        // get references to selected folder and message
        FolderCommandReference[] r = (FolderCommandReference[]) getReferences();
        Object[] uids = r[0].getUids(); // uid for messages to save

        if (uids.length == 0) {
            LOG.fine(
                "No filter created since no message was selected");

            return; // no message selected.
        }

        Object uid = uids[0];
        srcFolder = (MessageFolder) r[0].getFolder();

        // register for status events
        ((StatusObservableImpl) srcFolder.getObservable()).setWorker(worker);

        // get value of Subject, From or To header
        Header header = srcFolder.getHeaderFields(uid,
                new String[] {"Subject", "From", "To"});
        String headerValue = (String) header.get(filterType);

        if (headerValue == null) {
            LOG.warning("Error getting " + filterType
                    + " header. No filter created");

            return;
        }

        // create filter
        String descr = filterType + " contains [" + headerValue + "]";
        filter = createFilter(descr, filterType, headerValue);
    }

    /**
     * Private utility for creating a filter on a given headerfield. The
     * criteria used is "contains" and the action is set to "Mark as Read".
     *
     * @param filterDescr  Name / description to assign to filter
     * @param headerField  The header field to base filter on
     * @param pattern  The pattern to use in the filter
     * @return The filter created
     */
    public Filter createFilter(String filterDescr, String headerField,
        String pattern) {
        // create filter element
        XmlElement filter = new XmlElement("filter");
        filter.addAttribute("description", filterDescr);
        filter.addAttribute("enabled", "true");

        XmlElement rules = new XmlElement("rules");
        rules.addAttribute("condition", "matchall");

        // create criteria list
        XmlElement criteria = new XmlElement("criteria");
        criteria.addAttribute("type", headerField);
        criteria.addAttribute("headerfield", headerField);
        criteria.addAttribute("criteria", "contains");
        criteria.addAttribute("pattern", pattern);
        rules.addElement(criteria);
        filter.addElement(rules);

        // create action list
        XmlElement actionList = new XmlElement("actionlist");
        XmlElement action = new XmlElement("action");
        action.addAttribute("type", "Mark as Read");
        actionList.addElement(action);
        filter.addElement(actionList);

        // return filter based on the XmlElement just created
        return new Filter(filter);
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

QDox parse error in file://<WORKSPACE>/data/code-rep-dataset/Dataset2/Tasks/400.java