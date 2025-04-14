error id: file://<WORKSPACE>/data/code-rep-dataset/Dataset5/Tasks/17620.java
file://<WORKSPACE>/data/code-rep-dataset/Dataset5/Tasks/17620.java
### com.thoughtworks.qdox.parser.ParseException: syntax error @[230,2]

error in qdox parser
file content:
```java
offset: 7632
uri: file://<WORKSPACE>/data/code-rep-dataset/Dataset5/Tasks/17620.java
text:
```scala
{ // TODO finalize javadoc

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
package wicket.markup.html.form.upload;

import java.io.File;

import org.apache.commons.fileupload.FileItem;

import wicket.markup.html.form.validation.IValidationErrorHandler;


/**
 * Form that uploads files and writes them to the file system. It uses a conflict handler
 * that is called when a file with the same name exists in the same directory when
 * trying to save an uploaded file.
 *
 * @author Eelco Hillenius
 */
public class FileUploadForm extends AbstractUploadForm
{
    /** Serial Version ID */
	private static final long serialVersionUID = 6615560494113373735L;

	/**
     * This conflict handler tries to delete the current file and returns the given
     * file handler. Hence, the current file will be overwritten by the upload file.
     */
    public final static FileExistsConflictHandler OVERWRITE_FILE_CONFLICT_HANDLER =
        new FileExistsConflictHandler() 
    {
        /**
         * @see wicket.markup.html.form.upload.FileUploadForm.FileExistsConflictHandler#getFileForSaving(java.io.File)
         */
        public File getFileForSaving(final File uploadFile)
        {
            if(!uploadFile.delete()) // delete current file
            {
            	// fix for java/win bug
            	// see: http://forum.java.sun.com/thread.jsp?forum=4&thread=158689&tstart=0&trange=15
            	System.gc();
            	try { Thread.sleep(100); } catch (InterruptedException e) {}
            	if(!uploadFile.delete())
            	{
            		throw new IllegalStateException("unable to delete old file " +
            			uploadFile.getAbsolutePath());	
            	}
            }
            return uploadFile;
        } 
    };

    /**
     * This conflict handler renames the file using an ascending number until it finds
     * a name that it not used yet. Names are of form: {simple-filename}({number}).{ext}.
     * e.g.: myfile.gif, myfile(1).gif and myfile(2).gif
     */
    public final static FileExistsConflictHandler NUMBER_FILE_CONFLICT_HANDLER =
        new FileExistsConflictHandler() 
    {
        /**
         * @see wicket.markup.html.form.upload.FileUploadForm.FileExistsConflictHandler#getFileForSaving(java.io.File)
         */
        public File getFileForSaving(final File uploadFile)
        {
            final File targetDirectory = uploadFile.getParentFile();
    		final String fileName = uploadFile.getName();
    		final int extloc = fileName.lastIndexOf('.');
    		final String ext = fileName.substring((extloc + 1), fileName.length());
    		final String name = fileName.substring(0, extloc);
    		File newFile = null;
			int i = 1;
			while(true)
			{
				File testFile = new File(targetDirectory, (fileName + "(" + i + ")." + ext));
				if (testFile.exists())
				{
					i++;
				}
				else
				{
					newFile = testFile;
					break;
				}
			}
			return newFile;
        } 
    };

    /** overwrite existing resources. */
	private final static int MODE_OVERWRITE = 0;

	/** give the resource a new, numbered, name when a resource with the same name exists. */
	private final static int MODE_NUMBER = 1;

	/** the current upload mode. */
	private int uploadMode = MODE_NUMBER;

    /** the directory where the uploaded files should be put. */
	private final File targetDirectory;

	private String fileName = null;

	/**
	 * conflict handler that will be called when a file with the same name
     * already exists in the same directory when trying to save an uploaded file.
     */
	private FileExistsConflictHandler fileExistsConflictHandler;

    /**
     * Construct; uses NUMBER_FILE_CONFLICT_HANDLER as the fileExistsConflictHandler.
     * @param name component name
     * @param validationErrorHandler error handler for validations
     * @param targetDirectory the directory where the uploaded files should be put
     */
    public FileUploadForm(String name, IValidationErrorHandler validationErrorHandler, File targetDirectory)
    {
        this(name, validationErrorHandler, targetDirectory, NUMBER_FILE_CONFLICT_HANDLER);
    }

    /**
     * Construct.
     * @param name component name
     * @param validationErrorHandler error handler for validations
     * @param targetDirectory the directory where the uploaded files should be put
     * @param fileExistsConflictHandler conflict handler that will be called when a file with
     * 		the same name already exists in the same directory when trying to save an
     * 		uploaded file
     */
    public FileUploadForm(String name, IValidationErrorHandler validationErrorHandler,
            File targetDirectory, FileExistsConflictHandler fileExistsConflictHandler)
    {
        super(name, validationErrorHandler);
        this.targetDirectory = targetDirectory;
        this.fileExistsConflictHandler = fileExistsConflictHandler;
    }

    /**
     * @see wicket.markup.html.form.upload.AbstractUploadForm#processFormField(org.apache.commons.fileupload.FileItem)
     */
    protected final void processFormField(FileItem item)
    {
        this.fileName = item.getString();
    }

    /**
     * @see wicket.markup.html.form.upload.AbstractUploadForm#processUploadedFile(org.apache.commons.fileupload.FileItem)
     */
    protected final void processUploadedFile(FileItem item)
    {
		if (item == null)
		{
			throw new NullPointerException("no file");
		}
		String originalName = item.getName();
		int extloc = originalName.lastIndexOf('.');
		String ext = originalName.substring((extloc + 1), originalName.length());
		if (fileName == null)
		{
			fileName = originalName.substring(0, extloc);
		}

		if (fileName == null || fileName.trim().equals(""))
		{
		    throw new RuntimeException("no file name given");
		}

		File targetFile = new File(targetDirectory, fileName + "." + ext);
		saveFile(item, targetFile);
    }

    /**
     * Saves the uploaded file to disk.
     * @param item the upload item 
     * @param targetFile the target file
     */
    private void saveFile(final FileItem item, final File targetFile)
    {
        final File writeTo;
        // do some checking: does file allready exist?
		if (targetFile.exists())
		{ // file allready does exist
		    writeTo = fileExistsConflictHandler.getFileForSaving(targetFile);
		}
		else
		{
		    writeTo = targetFile;
		}

		try
        {
		    item.write(writeTo);
        }
        catch (Exception e)
        {
            throw new RuntimeException(e);
        }
    }

    /**
     * Interface for handlers that will be called when a file with the same name
     * already exists in the same directory when trying to save an uploaded file.
     */
    public static interface FileExistsConflictHandler
    {
        /**
         * Get the file handle that should be used to save the upload to.
         * @param uploadFile the current, allready existing file.
         * @return the file that should be used to save the upload to
         */
        File getFileForSaving(File uploadFile);
    }
}@@
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

QDox parse error in file://<WORKSPACE>/data/code-rep-dataset/Dataset5/Tasks/17620.java