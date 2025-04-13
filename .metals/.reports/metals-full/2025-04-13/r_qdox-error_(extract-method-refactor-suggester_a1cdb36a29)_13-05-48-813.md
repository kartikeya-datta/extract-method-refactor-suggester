error id: file://<WORKSPACE>/data/code-rep-dataset/Dataset5/Tasks/15578.java
file://<WORKSPACE>/data/code-rep-dataset/Dataset5/Tasks/15578.java
### com.thoughtworks.qdox.parser.ParseException: syntax error @[1,8]

error in qdox parser
file content:
```java
offset: 8
uri: file://<WORKSPACE>/data/code-rep-dataset/Dataset5/Tasks/15578.java
text:
```scala
public v@@oid onClick()

/*
 * $Id$
 * $Revision$
 * $Date$
 *
 * ====================================================================
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *  http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */
package wicket.examples.upload;

import java.io.File;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;

import wicket.PageParameters;
import wicket.examples.WicketExamplePage;
import wicket.markup.html.basic.Label;
import wicket.markup.html.form.TextField;
import wicket.markup.html.form.upload.FileUploadForm;
import wicket.markup.html.form.validation.IValidationFeedback;
import wicket.markup.html.link.Link;
import wicket.markup.html.list.ListItem;
import wicket.markup.html.list.ListView;

/**
 * Upload example.
 *
 * @author Eelco Hillenius
 */
public class UploadPage extends WicketExamplePage
{
    /** Log. */
    private static Log log = LogFactory.getLog(UploadPage.class);

    /** directory we are working with. */
    private File tempDir;

    /** list of files, model for file table. */
    private final List files = new ArrayList();

    /** reference to table for easy access. */
    private FileTable fileTable;

    /**
     * Constructor.
     * @param parameters Page parameters
     */
    public UploadPage(final PageParameters parameters)
    {
        tempDir = new File(System.getProperty("java.io.tmpdir"), "wicketuploadtest");
        if(!tempDir.isDirectory())
        {
            tempDir.mkdir();
        }
        add(new UploadForm("upload", null, tempDir));
        add(new Label("dir", tempDir.getAbsolutePath()));
        files.addAll(Arrays.asList(tempDir.list()));
        fileTable = new FileTable("fileList", files);
        add(fileTable);
    }

    /**
     * Refresh file list.
     */
    private void refreshFiles()
    {
        files.clear();
        files.addAll(Arrays.asList(tempDir.list()));
        fileTable.invalidateModel();
        
    }

    /**
     * form for uploads.
     */
    private class UploadForm extends FileUploadForm
    {
        /**
         * Construct.
         * @param name component name
         * @param validationErrorHandler error handler
         * @param targetDirectory directory to save uploads
         */
        public UploadForm(String name, IValidationFeedback validationErrorHandler, File targetDirectory)
        {
            super(name, validationErrorHandler, targetDirectory);
            add(new TextField("fileName", ""));
        }

        /**
         * @see wicket.markup.html.form.upload.AbstractUploadForm#finishUpload()
         */
        protected void finishUpload()
        {
            refreshFiles();
        }
    }

    /**
     * table for files.
     */
    private class FileTable extends ListView
    {
        /**
         * Construct.
         * @param name component name
         * @param object file list
         */
        public FileTable(String name, List object)
        {
            super(name, object);
        }

        /**
         * @see ListView#populateItem(ListItem)
         */
        protected void populateItem(ListItem listItem)
        {
            final String fileName = (String)listItem.getModelObject();
            listItem.add(new Label("file", fileName));
            listItem.add(new Link("delete") {
                
                public void onLinkClicked()
                {
                    File toDelete = new File(tempDir, fileName);
                    log.info("delete " + toDelete);
                    toDelete.delete();
                    try 
                    {
                        Thread.sleep(100); // wait for file lock (Win issue)
                    }
                    catch (InterruptedException e)
                    {
                    }
                    refreshFiles();
                } 
            });
        }
    }
}
 No newline at end of file
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

QDox parse error in file://<WORKSPACE>/data/code-rep-dataset/Dataset5/Tasks/15578.java