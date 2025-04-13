error id: file://<WORKSPACE>/data/code-rep-dataset/Dataset2/Tasks/1384.java
file://<WORKSPACE>/data/code-rep-dataset/Dataset2/Tasks/1384.java
### com.thoughtworks.qdox.parser.ParseException: syntax error @[18,1]

error in qdox parser
file content:
```java
offset: 817
uri: file://<WORKSPACE>/data/code-rep-dataset/Dataset2/Tasks/1384.java
text:
```scala
public class FolderCommandReference extends DefaultCommandReference implements IFolderCommandReference {

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
p@@ackage org.columba.mail.command;

import java.io.File;
import java.lang.reflect.Array;

import org.columba.core.command.DefaultCommandReference;
import org.columba.mail.folder.AbstractFolder;
import org.columba.mail.message.ColumbaMessage;


/**
 * This is a reference implemention suitable for folders containing
 * messages.
 * <p>
 * Its main purpose is to store source and/or destination folders and
 * arrays of message UIDs.
 * <p>
 *
 *
 * @author fdietz
 */
public class FolderCommandReference extends DefaultCommandReference {
    private AbstractFolder folder;
    private AbstractFolder destinationFolder;
    
    private Object[] uids;
    private Integer[] address;
    private ColumbaMessage message;
    private int markVariant;
    private String folderName;
    private int colorValue;
    private File destFile;

    /**
     * Constructor for FolderCommandReference.
     * @param folder
     */
    public FolderCommandReference(AbstractFolder folder) {
        this.folder = folder;
    }
    
    public FolderCommandReference(AbstractFolder folder, AbstractFolder destinationFolder) {
        this.folder = folder;
        this.destinationFolder = destinationFolder;
    }

    public FolderCommandReference(AbstractFolder folder, ColumbaMessage message) {
        this.folder = folder;
        this.message = message;
    }

    /**
     * Constructor for FolderCommandReference.
     * @param folder
     * @param uids
     */
    public FolderCommandReference(AbstractFolder folder, Object[] uids) {
        this.folder = folder;
        this.uids = uids;
    }
    
    /**
     * Constructor for FolderCommandReference.
     * @param folder
     * @param uids
     */
    public FolderCommandReference(AbstractFolder sourceFolder, AbstractFolder destinationFolder, Object[] uids) {
        this.folder = sourceFolder;
		this.destinationFolder = destinationFolder;
        this.uids = uids;
    }

    /**
     * Constructor for FolderCommandReference.
     * @param folder
     * @param uids
     * @param address
     */
    public FolderCommandReference(AbstractFolder folder, Object[] uids,
        Integer[] address) {
        this.folder = folder;
        this.uids = uids;
        this.address = address;
    }

    public AbstractFolder getFolder() {
        return folder;
    }

    public void setFolder(AbstractFolder folder) {
        this.folder = folder;
    }

    public Object[] getUids() {
        return uids;
    }

    public Integer[] getAddress() {
        return address;
    }

    public void setUids(Object[] uids) {
        this.uids = uids;
    }

    public ColumbaMessage getMessage() {
        return message;
    }

    public void setMessage(ColumbaMessage message) {
        this.message = message;
    }

    public void reduceToFirstUid() {
        if (uids == null) {
            return;
        }

        int size = Array.getLength(uids);

        if (size > 1) {
            Object[] oneUid = new Object[1];
            oneUid[0] = uids[0];
            uids = oneUid;
        }
    }

    public boolean tryToGetLock(Object locker) {
        //ColumbaLogger.log.info("try to get lock on: "+folder.getName() );
        //Lock result = folder.tryToGetLock();
        return folder.tryToGetLock(locker);
    }

    public void releaseLock(Object locker) {
        //ColumbaLogger.log.info("releasing lock: "+folder.getName() );
        folder.releaseLock(locker);
    }

    /**
     * Returns the markVariant.
     * @return int
     */
    public int getMarkVariant() {
        return markVariant;
    }

    /**
     * Sets the markVariant.
     * @param markVariant The markVariant to set
     */
    public void setMarkVariant(int markVariant) {
        this.markVariant = markVariant;
    }

    /**
     * Returns the folderName.
     * @return String
     */
    public String getFolderName() {
        return folderName;
    }

    /**
     * Sets the folderName.
     * @param folderName The folderName to set
     */
    public void setFolderName(String folderName) {
        this.folderName = folderName;
    }

    /**
     * @return
     */
    public File getDestFile() {
        return destFile;
    }

    /**
     * @param destFile
     */
    public void setDestFile(File destFile) {
        this.destFile = destFile;
    }

    /**
     * @return Returns the colorValue.
     */
    public int getColorValue() {
        return colorValue;
    }

    /**
     * @param colorValue The colorValue to set.
     */
    public void setColorValue(int colorValue) {
        this.colorValue = colorValue;
    }
	/**
	 * @return Returns the destinationFolder.
	 */
	public AbstractFolder getDestinationFolder() {
		return destinationFolder;
	}
	/**
	 * @param destinationFolder The destinationFolder to set.
	 */
	public void setDestinationFolder(AbstractFolder destinationFolder) {
		this.destinationFolder = destinationFolder;
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
	java.base/java.util.concurrent.ForkJoinPool.helpJoin(ForkJoinPool.java:1883)
	java.base/java.util.concurrent.ForkJoinTask.awaitDone(ForkJoinTask.java:440)
	java.base/java.util.concurrent.ForkJoinTask.join(ForkJoinTask.java:670)
	scala.collection.parallel.ForkJoinTasks$FJTWrappedTask.sync(Tasks.scala:243)
	scala.collection.parallel.ForkJoinTasks$FJTWrappedTask.sync$(Tasks.scala:243)
	scala.collection.parallel.AdaptiveWorkStealingForkJoinTasks$AWSFJTWrappedTask.sync(Tasks.scala:304)
	scala.collection.parallel.AdaptiveWorkStealingTasks$AWSTWrappedTask.internal(Tasks.scala:173)
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

QDox parse error in file://<WORKSPACE>/data/code-rep-dataset/Dataset2/Tasks/1384.java