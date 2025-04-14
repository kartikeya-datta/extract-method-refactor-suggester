error id: file://<WORKSPACE>/data/code-rep-dataset/Dataset5/Tasks/7077.java
file://<WORKSPACE>/data/code-rep-dataset/Dataset5/Tasks/7077.java
### com.thoughtworks.qdox.parser.ParseException: syntax error @[1,22]

error in qdox parser
file content:
```java
offset: 22
uri: file://<WORKSPACE>/data/code-rep-dataset/Dataset5/Tasks/7077.java
text:
```scala
private final static S@@tring blankstr = blanks(30);

/*
 * The Apache Software License, Version 1.1
 *
 * Copyright (c) 2002 The Apache Software Foundation.  All rights
 * reserved.
 *
 * Redistribution and use in source and binary forms, with or without
 * modification, are permitted provided that the following conditions
 * are met:
 *
 * 1. Redistributions of source code must retain the above copyright
 *    notice, this list of conditions and the following disclaimer.
 *
 * 2. Redistributions in binary form must reproduce the above copyright
 *    notice, this list of conditions and the following disclaimer in
 *    the documentation and/or other materials provided with the
 *    distribution.
 *
 * 3. The end-user documentation included with the redistribution, if
 *    any, must include the following acknowlegement:
 *       "This product includes software developed by the
 *        Apache Software Foundation (http://www.apache.org/)."
 *    Alternately, this acknowlegement may appear in the software itself,
 *    if and wherever such third-party acknowlegements normally appear.
 *
 * 4. The names "The Jakarta Project", "Ant", and "Apache Software
 *    Foundation" must not be used to endorse or promote products derived
 *    from this software without prior written permission. For written
 *    permission, please contact apache@apache.org.
 *
 * 5. Products derived from this software may not be called "Apache"
 *    nor may "Apache" appear in their names without prior written
 *    permission of the Apache Group.
 *
 * THIS SOFTWARE IS PROVIDED ``AS IS'' AND ANY EXPRESSED OR IMPLIED
 * WARRANTIES, INCLUDING, BUT NOT LIMITED TO, THE IMPLIED WARRANTIES
 * OF MERCHANTABILITY AND FITNESS FOR A PARTICULAR PURPOSE ARE
 * DISCLAIMED.  IN NO EVENT SHALL THE APACHE SOFTWARE FOUNDATION OR
 * ITS CONTRIBUTORS BE LIABLE FOR ANY DIRECT, INDIRECT, INCIDENTAL,
 * SPECIAL, EXEMPLARY, OR CONSEQUENTIAL DAMAGES (INCLUDING, BUT NOT
 * LIMITED TO, PROCUREMENT OF SUBSTITUTE GOODS OR SERVICES; LOSS OF
 * USE, DATA, OR PROFITS; OR BUSINESS INTERRUPTION) HOWEVER CAUSED AND
 * ON ANY THEORY OF LIABILITY, WHETHER IN CONTRACT, STRICT LIABILITY,
 * OR TORT (INCLUDING NEGLIGENCE OR OTHERWISE) ARISING IN ANY WAY OUT
 * OF THE USE OF THIS SOFTWARE, EVEN IF ADVISED OF THE POSSIBILITY OF
 * SUCH DAMAGE.
 * ====================================================================
 *
 * This software consists of voluntary contributions made by many
 * individuals on behalf of the Apache Software Foundation.  For more
 * information on the Apache Software Foundation, please see
 * <http://www.apache.org/>.
 *
 */
package org.apache.tools.ant.taskdefs.optional.starteam;

import java.io.IOException;
import java.util.Hashtable;

import com.starbase.starteam.File;
import com.starbase.starteam.Folder;
import com.starbase.starteam.Item;
import com.starbase.starteam.Status;
import com.starbase.starteam.View;
import com.starbase.starteam.ViewConfiguration;

import org.apache.tools.ant.BuildException;

/**
 * StarTeamList.java
 *
 *
 * Created: Tue Dec 25 06:51:14 2001
 *
 * @author <a href="mailto:stevec@ignitesports.com">Steve Cohen</a>
 * @version 1.0
 */

public class StarTeamList extends TreeBasedTask {
    /**
     * Sets the label StarTeam is to be listed.
     *
     * @param label the label to be listed
     */
    public void setLabel(String label) {
        _setLabel(label);
    }

    /**
     * Override of base-class abstract function creates an
     * appropriately configured view for checkoutlists - either
     * the current view or a view from this.label.
     *
     * @param raw the unconfigured <code>View</code>
     * @return the snapshot <code>View</code> appropriately configured.
     */
    protected View createSnapshotView(View raw) {

        int labelID = getLabelID(raw);

        // if a label has been supplied, use it to configure the view
        // otherwise use current view
        if (labelID >= 0) {
            return new View(raw, ViewConfiguration.createFromLabel(labelID));
        } else {
            return new View(raw, ViewConfiguration.createTip());
        }
    }

    /**
     * Required base-class abstract function implementation is a no-op here.
     *
     * @exception BuildException not thrown in this implementation
     */
    protected void testPreconditions() throws BuildException {
        //intentionally do nothing.
    }

    /**
     * Implements base-class abstract function to perform the checkout
     * operation on the files in each folder of the tree.
     *
     * @param starteamFolder the StarTeam folder from which files to be
     *                       checked out
     * @param targetFolder the local mapping of rootStarteamFolder
     */
    protected void visit(Folder starteamFolder, java.io.File targetFolder)
            throws BuildException {
        try {
            if (null == getRootLocalFolder()) {
                log("Folder: " + starteamFolder.getName() + " (Default folder: " + targetFolder + ")");
            } else {
                log("Folder: " + starteamFolder.getName() + " (Local folder: " + targetFolder + ")");
            }
            Hashtable localFiles = listLocalFiles(targetFolder);

            // For all Files in this folder, we need to check
            // if there have been modifications.

            Item[] files = starteamFolder.getItems("File");
            for (int i = 0; i < files.length; i++) {
                File eachFile = (File) files[i];
                String filename = eachFile.getName();
                java.io.File localFile =
                        new java.io.File(targetFolder, filename);

                delistLocalFile(localFiles, localFile);



                // If the file doesn't pass the include/exclude tests, skip it.
                if (!shouldProcess(filename)) {
                    continue;
                }

                list(eachFile, localFile);
            }


            // Now we recursively call this method on all sub folders in this
            // folder unless recursive attribute is off.
            Folder[] subFolders = starteamFolder.getSubFolders();
            for (int i = 0; i < subFolders.length; i++) {
                java.io.File targetSubfolder =
                        new java.io.File(targetFolder, subFolders[i].getName());
                delistLocalFile(localFiles, targetSubfolder);
                if (isRecursive()) {
                    visit(subFolders[i], targetSubfolder);
                }
            }

        } catch (IOException e) {
            throw new BuildException(e);
        }
    }

    protected void list(File reposFile, java.io.File localFile)
            throws IOException {
        StringBuffer b = new StringBuffer();
        if (null == getRootLocalFolder()) {
            // status is irrelevant to us if we have specified a
            // root local folder.
            b.append(pad(Status.name(reposFile.getStatus()), 12)).append(' ');
        }
        b.append(pad(getUserName(reposFile.getLocker()), 20))
                .append(' ')
                .append(reposFile.getModifiedTime().toString())
                .append(rpad(String.valueOf(reposFile.getSize()), 9))
                .append(' ')
                .append(reposFile.getName());

        log(b.toString());
    }

    private static final String blankstr = blanks(30);

    private static String blanks(int len) {
        StringBuffer b = new StringBuffer();
        for (int i = 0; i < len; i++) {
            b.append(' ');
        }
        return b.toString();
    }

    protected static String pad(String s, int padlen) {
        return (s + blankstr).substring(0, padlen);
    }

    protected static String rpad(String s, int padlen) {
        s = blankstr + s;
        return s.substring(s.length() - padlen);
    }


}// StarTeamList


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

QDox parse error in file://<WORKSPACE>/data/code-rep-dataset/Dataset5/Tasks/7077.java