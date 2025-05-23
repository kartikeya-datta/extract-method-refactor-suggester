error id: file://<WORKSPACE>/data/code-rep-dataset/Dataset2/Tasks/9427.java
file://<WORKSPACE>/data/code-rep-dataset/Dataset2/Tasks/9427.java
### com.thoughtworks.qdox.parser.ParseException: syntax error @[1,1]

error in qdox parser
file content:
```java
offset: 1
uri: file://<WORKSPACE>/data/code-rep-dataset/Dataset2/Tasks/9427.java
text:
```scala
t@@his.mediator = frameController;

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

import org.columba.api.command.ICommandReference;
import org.columba.api.command.IWorkerStatusController;
import org.columba.api.gui.frame.IFrameMediator;
import org.columba.core.command.Command;
import org.columba.core.command.StatusObservableImpl;
import org.columba.core.command.Worker;
import org.columba.core.filter.FilterRule;
import org.columba.mail.command.IMailFolderCommandReference;
import org.columba.mail.filter.MailFilterCriteria;
import org.columba.mail.folder.FolderFactory;
import org.columba.mail.folder.IMailbox;
import org.columba.mail.folder.virtual.VirtualFolder;
import org.columba.mail.gui.config.filter.ConfigFrame;
import org.columba.mail.gui.tree.FolderTreeModel;
import org.columba.ristretto.message.Header;


/**
 * This class is used to create a virtual folder based on the currently
 * selected message (if multiple selected, the first one in the selection array
 * is used) - either using Subject, To or From.
 *
 * @author Karl Peder Olesen (karlpeder), 20030621
 */
public class CreateVFolderOnMessageCommand extends Command {

    /** JDK 1.4+ logging framework logger, used for logging. */
    private static final Logger LOG = Logger.getLogger("org.columba.mail.folder.command");

    /** Used for creating a virtual folder based on Subject */
    public static final String VFOLDER_ON_SUBJECT = "Subject";

    /** Used for creating a virtual folder based on From */
    public static final String VFOLDER_ON_FROM = "From";

    /** Used for creating a virtual folder based on To */
    public static final String VFOLDER_ON_TO = "To";

    /** Type of virtual folder to create (Subject/From/To) */
    private String vfolderType;

    /** Parent for the virtual folder */
    private IMailbox parentFolder = null;

    /** Virtual folder created created */
    private VirtualFolder vfolder = null;

    private IFrameMediator mediator;
    
    /**
     * Constructor for CreateVFolderOnMessageCommand. Calls super constructor
     * and saves flag for which kind of virtual folder to create. Default for
     * filter type is FILTER_ON_SUBJECT.
     *
     * @param frameMediator
     * @param references
     * @param vfolderType
     *            Which type of filter to create. Used defined constants
     */
    public CreateVFolderOnMessageCommand(IFrameMediator frameController,
    		ICommandReference reference, String vfolderType) {
        super(reference);
        this.vfolderType = vfolderType;
        this.mediator = mediator;
    }

    /**
     * Displays search dialog for user modifications after creation of the
     * virtual folder in execute. Also refreshes the tree view.
     *
     * @see org.columba.api.command.Command#updateGUI()
     */
    public void updateGUI() throws Exception {
    	FolderTreeModel.getInstance().nodeStructureChanged(parentFolder);

        if (vfolder != null) {
            //vfolder.showFilterDialog((AbstractMailFrameController) getFrameMediator());
            new ConfigFrame(mediator, vfolder);
        }
    }

    /**
     * This method generates a virtual folder based on Subject, From or To
     * (depending on parameter transferred to constructor) of the currently
     * selected message.
     *
     * @param worker
     * @see org.columba.api.command.Command#execute(Worker)
     */
    public void execute(IWorkerStatusController worker)
        throws Exception {
        // get references to selected folder and message
    	IMailFolderCommandReference r = (IMailFolderCommandReference) getReference();
        Object[] uids = r.getUids(); // uid for messages to save

        if (uids.length == 0) {
            LOG.fine(
                "No virtual folder created since no message was selected");

            return; // no message selected.
        }

        Object uid = uids[0];
        parentFolder = (IMailbox) r.getSourceFolder();

        //register for status events
        ((StatusObservableImpl) parentFolder.getObservable()).setWorker(worker);

        // get value of Subject, From or To header
        Header header = parentFolder.getHeaderFields(uid,
                new String[] {"Subject", "From", "To"});
        String headerValue = (String) header.get(vfolderType);

        if (headerValue == null) {
            LOG.warning("Error getting " + vfolderType
                    + " header. No virtual folder created");

            return;
        }

        // create virtual folder (is attached to parentFolder)
        String name = vfolderType + " contains [" + headerValue + "]";
        vfolder = createVirtualFolder(name, vfolderType, headerValue,
                parentFolder);
    }

    /**
     * Private utility for creating a virtual folder on a given headerfield.
     * The criteria used is "contains".
     *
     * @param folderName
     *            Name of virtual folder
     * @param headerField
     *            The header field to base virtual folder on
     * @param pattern
     *            The pattern to use in the virtual folder
     * @param parent
     *            Parent folder
     * @return The filter created
     */
    public VirtualFolder createVirtualFolder(String folderName,
        String headerField, String pattern, IMailbox parent) {
        // create virtual folder
        VirtualFolder vfolder;

        try {
            vfolder = (VirtualFolder) FolderFactory.getInstance().createChild(parent,
                    folderName, "VirtualFolder");
        } catch (Exception e) {
            LOG.warning("Error creating new virtual folder: "
                    + e.getMessage());

            return null;
        }

        // set properties for virtual folder
        int parentUid = 101; // default is inbox if parent is null

        if (parent != null) {
            parentUid = parent.getUid();
        }

        vfolder.getConfiguration().setInteger("property", "source_uid", parentUid);
        vfolder.getConfiguration().setBoolean("property", "include_subfolders", false);

        // define filter rule
        FilterRule rule = vfolder.getFilter().getFilterRule();
        rule.setCondition("matchall");
        rule.removeAll();
        rule.addEmptyCriteria();

        // define criteria
        MailFilterCriteria c = new MailFilterCriteria(rule.get(0));
        c.setCriteriaString("contains");
        c.setHeaderfieldString(headerField);
        c.setTypeString(headerField);
        c.setPatternString(pattern);

        return vfolder;
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

QDox parse error in file://<WORKSPACE>/data/code-rep-dataset/Dataset2/Tasks/9427.java