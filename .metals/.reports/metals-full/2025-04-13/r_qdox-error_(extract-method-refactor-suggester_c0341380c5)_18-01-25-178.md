error id: file://<WORKSPACE>/data/code-rep-dataset/Dataset5/Tasks/17619.java
file://<WORKSPACE>/data/code-rep-dataset/Dataset5/Tasks/17619.java
### com.thoughtworks.qdox.parser.ParseException: syntax error @[175,2]

error in qdox parser
file content:
```java
offset: 5062
uri: file://<WORKSPACE>/data/code-rep-dataset/Dataset5/Tasks/17619.java
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

import java.util.Iterator;
import java.util.List;

import javax.servlet.http.HttpServletRequest;

import org.apache.commons.fileupload.DiskFileUpload;
import org.apache.commons.fileupload.FileItem;
import org.apache.commons.fileupload.FileUpload;
import org.apache.commons.fileupload.FileUploadBase;
import org.apache.commons.fileupload.FileUploadException;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;

import wicket.RequestCycle;
import wicket.markup.ComponentTag;
import wicket.markup.html.form.Form;
import wicket.markup.html.form.validation.IValidationErrorHandler;
import wicket.protocol.http.HttpRequest;


/**
 * Base class for upload components.
 *
 * @author Eelco Hillenius
 */
public abstract class AbstractUploadForm extends Form
{
    /** Code broadcaster for reporting. */
    private static Log log = LogFactory.getLog(AbstractUploadForm.class);

    /**
     * Construct.
     * @param name component name
     * @param validationErrorHandler validation error handler
     */
    public AbstractUploadForm(String name, IValidationErrorHandler validationErrorHandler)
    {
        super(name, validationErrorHandler);
    }

    /**
     * @see wicket.Component#handleComponentTag(RequestCycle, ComponentTag)
     */
    protected final void handleComponentTag(final RequestCycle cycle, final ComponentTag tag)
    {
        super.handleComponentTag(cycle, tag);
        tag.put("enctype", "multipart/form-data");
    }

    /**
     * Handles an upload.
     * @param cycle the request cycle
     * @see wicket.markup.html.form.Form#handleSubmit(wicket.RequestCycle)
     */
    public void handleSubmit(RequestCycle cycle)
    {
		try
        {
			HttpServletRequest request = ((HttpRequest)cycle.getRequest()).getServletRequest();
			boolean isMultipart = FileUpload.isMultipartContent(request);
			if(!isMultipart)
			{
			    throw new IllegalStateException("request is not a multipart request");
			}
			prepareUpload();
			FileUploadBase upload = createUpload();
            List items = parseRequest(request, upload);
			processFileItems(items);
			finishUpload();
        }
        catch (FileUploadException e)
        {
            throw new RuntimeException(e); // for the time being, we throw
        }
    }

    /**
     * Process the list of file items.
     * @param items List of {@link FileItem}s
     */
    protected void processFileItems(List items)
    {
        for (Iterator i = items.iterator(); i.hasNext();)
        {
            FileItem item = (FileItem) i.next();

            if (item.isFormField())
            {
                processFormField(item);
            }
            else
            {
                processUploadedFile(item);
            }
        }
    }

    /**
     * Template method that is called before the handling of the upload form starts.
     * Use for initialization of directories etc.
     */
    protected void prepareUpload()
    {
        
    }

    /**
     * Template method that is called after the handling of the upload form finishes.
     * Use for things like re-rendering the UI etc.
     */
    protected void finishUpload()
    {
        
    }

    /**
     * Process a form field.
     * @param item form field item (item.isFormField() == true)
     */
    protected abstract void processFormField(FileItem item);

    /**
     * Process an upload item.
     * @param item upload item (item.isFormField() == false)
     */
    protected abstract void processUploadedFile(FileItem item);

    /**
     * parse the request and return a List of {@link FileItem}s.
     * @param request http servlet request
     * @param upload upload object
     * @return List with {@link FileItem}s
     * @throws FileUploadException
     */
    protected List parseRequest(HttpServletRequest request, FileUploadBase upload)
    	throws FileUploadException
    {
        List items = upload.parseRequest(request);
        return items;
    }

    /**
     * Create an upload object. Override this to
     * use anything else than {@link DiskFileUpload} or to parameterize the upload object
     * (e.g. set the max size, temp dir, etc).
     * @return upload object
     */
    protected FileUploadBase createUpload()
    {
        FileUploadBase upload = new DiskFileUpload();
        return upload;
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

QDox parse error in file://<WORKSPACE>/data/code-rep-dataset/Dataset5/Tasks/17619.java