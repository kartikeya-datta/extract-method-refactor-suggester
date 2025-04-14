error id: file://<WORKSPACE>/data/code-rep-dataset/Dataset5/Tasks/1432.java
file://<WORKSPACE>/data/code-rep-dataset/Dataset5/Tasks/1432.java
### com.thoughtworks.qdox.parser.ParseException: syntax error @[1,22]

error in qdox parser
file content:
```java
offset: 22
uri: file://<WORKSPACE>/data/code-rep-dataset/Dataset5/Tasks/1432.java
text:
```scala
final private static L@@og log = LogFactory.getLog(Export.class);

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
package wicket.examples.displaytag.export;

import java.util.List;

import javax.servlet.http.HttpServletResponse;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;

import wicket.RequestCycle;
import wicket.WicketRuntimeException;
import wicket.protocol.http.WebResponse;


/**
 */
public class Export
{
    final private Log log = LogFactory.getLog(Export.class);

    /**
     * called when data are not displayed in a html page but should be exported.
     * @param cycle
     * @param exportView
     * @param data
     * @return int EVAL_PAGE or SKIP_PAGE
     */
    public int doExport(final RequestCycle cycle, final BaseExportView exportView, final List data) 
    {
        final boolean exportFullList = true;
        final boolean exportHeader = true;
        final boolean exportDecorated = true;

        String mimeType = exportView.getMimeType();
        String exportString = exportView.doExport();

        String filename = null; // getExportFileName(MediaTypeEnum.XML);
        return writeExport(cycle, mimeType, exportString, filename);
    }

    /**
     * Will write the export. The default behavior is to write directly to the response. If the ResponseOverrideFilter
     * is configured for this request, will instead write the export content to a StringBuffer in the Request object.
     * @param cycle
     * @param mimeType mime type to set in the response
     * @param exportString String
     * @param filename name of the file to be saved. Can be null, if set the content-disposition header will be added.
     * @return int
     */
    protected int writeExport(final RequestCycle cycle, final String mimeType, final String exportString, final String filename)
    {
        WebResponse response = (WebResponse)cycle.getResponse();
        HttpServletResponse servletResponse = response.getHttpServletResponse();

        // response can't be already committed at this time
        if (servletResponse.isCommitted())
        {
            throw new WicketRuntimeException("HTTP response already committed. Can not change that any more");
        }

        // if cache is disabled using http header, export will not work.
        // Try to remove bad headers overwriting them, since there is no way to remove a single header and reset()
        // could remove other "useful" headers like content encoding
        if (servletResponse.containsHeader("Cache-Control"))
        {
            servletResponse.setHeader("Cache-Control", "public");
        }
        if (servletResponse.containsHeader("Expires"))
        {
            servletResponse.setHeader("Expires", "Thu, 01 Dec 2069 16:00:00 GMT");
        }
        if (servletResponse.containsHeader("Pragma"))
        {
            // Pragma: no-cache
            // http 1.0 equivalent of Cache-Control: no-cache
            // there is no "Cache-Control: public" equivalent, so just try to set it to an empty String (note
            // this is NOT a valid header)
            servletResponse.setHeader("Pragma", "");
        }

        try
        {
            servletResponse.resetBuffer();
        }
        catch (Exception e)
        {
            throw new WicketRuntimeException("Unable to reset HTTP response", e);
        }

        response.setContentType(mimeType);

        if ((filename != null) && (filename.trim().length() > 0))
        {
            servletResponse.setHeader("Content-Disposition", "attachment; filename=\"" + filename + "\"");
        }

        response.write(exportString);
        return 0;
    }

    /**
     * Returns the file name for the given media. Can be null
     * @param exportType instance of MediaTypeEnum
     * @return String filename
     */
    public String getExportFileName(MediaTypeEnum exportType)
    {
        return "Test" + "." + exportType;
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
	scala.collection.parallel.AdaptiveWorkStealingTasks$AWSTWrappedTask.internal(Tasks.scala:159)
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

QDox parse error in file://<WORKSPACE>/data/code-rep-dataset/Dataset5/Tasks/1432.java