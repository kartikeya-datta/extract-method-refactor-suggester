error id: file://<WORKSPACE>/data/code-rep-dataset/Dataset2/Tasks/1906.java
file://<WORKSPACE>/data/code-rep-dataset/Dataset2/Tasks/1906.java
### com.thoughtworks.qdox.parser.ParseException: syntax error @[1,1]

error in qdox parser
file content:
```java
offset: 1
uri: file://<WORKSPACE>/data/code-rep-dataset/Dataset2/Tasks/1906.java
text:
```scala
i@@f ((element.getFileName() == null) && (element.getContentType() == null)) {

package org.apache.struts.upload;

import java.io.File;
import java.io.IOException;
import java.io.FileOutputStream;
import java.io.ByteArrayInputStream;
import java.io.UnsupportedEncodingException;
import java.util.Hashtable;
import java.util.Enumeration;
import javax.servlet.ServletContext;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;

import org.apache.struts.action.ActionServlet;
import org.apache.struts.action.ActionMapping;

/**
 * This is a MultipartRequestHandler that writes file data directly to
 * to temporary files on disk
 *
 * @author Mike Schachter
 */
public class DiskMultipartRequestHandler implements MultipartRequestHandler {

    /**
     * The ActionServlet instance used for this class
     */
    protected ActionServlet servlet;

    /**
     * The ActionMapping instance used for this class
     */
    protected ActionMapping mapping;

    /**
     * A Hashtable representing the form files uploaded
     */
    protected Hashtable fileElements;

    /**
     * A Hashtable representing the form text input names and values
     */
    protected Hashtable textElements;

    /**
     * A Hashtable representing all elemnents
     */
    protected Hashtable allElements;
    
    public void handleRequest(HttpServletRequest request) throws ServletException {
        
        MultipartIterator iterator = new MultipartIterator(request,
                                                            servlet.getBufferSize(),
                                                            getMaxSizeFromServlet());
        MultipartElement element;

        textElements = new Hashtable();
        fileElements = new Hashtable();
        allElements = new Hashtable();

        while ((element = iterator.getNextElement()) != null) {
            if (element.getContentType() == null) {
                String textData;
                try {
                    textData = new String(element.getData(), "ISO-8859-1");
                }
                catch (UnsupportedEncodingException uee) {
                    textData = new String(element.getData());
                }

                textElements.put(element.getName(), textData);
                allElements.put(element.getName(), textData);
            }
            else {
                try {
                    DiskFile theFile = writeFile(element);
                    fileElements.put(element.getName(), theFile);
                    allElements.put(element.getName(), theFile);
                }
                catch (IOException ioe) {
                    throw new ServletException("DiskMultipartRequestHandler." +
                    "handleRequest(), IOException: " +
                    ioe.getMessage());
                }
            }
        }
    }


    protected DiskFile writeFile(MultipartElement element) throws IOException,
    ServletException {
        DiskFile theFile = null;

        //get a handle to some temporary file and open
        //a stream to it
        String tempDir = servlet.getTempDir();
        if (tempDir == null) {
            //attempt to retrieve the servlet container's temporary directory
            ServletContext context = servlet.getServletConfig().getServletContext();

            try {
                tempDir = (String) context.getAttribute("javax.servlet.context.tempdir");
            }
            catch (ClassCastException cce) {
                tempDir = ((File) context.getAttribute("javax.servlet.context.tempdir")).getAbsolutePath();
            }
            

            if (tempDir == null) {
                //default to system-wide tempdir
                tempDir = System.getProperty("java.io.tmpdir");

                if (servlet.getDebug() > 1) {
                    servlet.log("DiskMultipartRequestHandler.handleRequest(): " +
                    "defaulting to java.io.tmpdir directory \"" +
                    tempDir);
                }
            }
        }

        File tempDirectory = new File(tempDir);

        if ((!tempDirectory.exists()) || (!tempDirectory.isDirectory())) {
            throw new ServletException("DiskMultipartRequestHandler: no " +
            "temporary directory specified for disk write");
        }

        File tempFile = File.createTempFile("strts", null, tempDirectory);
        FileOutputStream fos = new FileOutputStream(tempFile);

        //int bufferSize = servlet.getBufferSize();
        int bufferSize = -1;
        if (bufferSize > 0) {
            //buffer the write if servlet.getBufferSize() > 0
            ByteArrayInputStream byteArray = new ByteArrayInputStream(element.getData());
            byte[] bufferBytes = new byte[bufferSize];
            int bytesWritten = 0;
            int bytesRead = 0;
            int offset = 0;

            while ((bytesRead = byteArray.read(bufferBytes,
            offset, bufferSize)) != -1) {
                fos.write(bufferBytes, offset, bytesRead);
                bytesWritten += bytesRead;
                offset += bytesRead;
            }
            byteArray.close();
        }
        else {
            //write in one big chunk
            fos.write(element.getData());
        }

        theFile = new DiskFile(tempFile.getAbsolutePath());
        theFile.setContentType(element.getContentType());
        theFile.setFileName(element.getFileName());
        theFile.setFileSize(element.getData().length);

        fos.close();

        return theFile;
    }

    public Hashtable getAllElements() {
        return allElements;
    }

    public Hashtable getTextElements() {
        return new Hashtable();
    }

    public Hashtable getFileElements() {
        return new Hashtable();
    }

    /**
     * Delete all the files uploaded
     */
    public void rollback() {
        Enumeration names = fileElements.keys();

        while (names.hasMoreElements()) {
            String name = (String) names.nextElement();
            DiskFile theFile = (DiskFile) fileElements.get(name);
            theFile.destroy();
        }
    }

    /**
     * Calls on {@link #rollback() rollback()} to delete
     * temporary files
     */
    public void finish() {
        rollback();
    }

    public void setServlet(ActionServlet servlet) {
        this.servlet = servlet;
    }

    public void setMapping(ActionMapping mapping) {
        this.mapping = mapping;
    }

    public ActionServlet getServlet() {
        return servlet;
    }

    public ActionMapping getMapping() {
        return mapping;
    }

    /**
     * Gets the maximum post data size in bytes from the string
     * representation in ActionServlet
     */
    protected long getMaxSizeFromServlet() throws ServletException{
        String stringSize = servlet.getMaxFileSize();
        long size = -1;
        int multiplier = 1;
        
        if (stringSize.endsWith("K")) {
            multiplier = 1024;
            stringSize = stringSize.substring(0, stringSize.length()-1);
        }
        if (stringSize.endsWith("M")) {
            multiplier = 1024*1024;
            stringSize = stringSize.substring(0, stringSize.length()-1);
        }
        else if (stringSize.endsWith("G")) {
            multiplier = 1024*1024*1024;
            stringSize = stringSize.substring(0, stringSize.length()-1);
        }
        
        try {
            size = Long.parseLong(stringSize);
        }
        catch (NumberFormatException nfe) {
            throw new ServletException("Invalid format for maximum file size: \"" +
                servlet.getMaxFileSize() + "\"");
        }
                
        return (size * multiplier);
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

QDox parse error in file://<WORKSPACE>/data/code-rep-dataset/Dataset2/Tasks/1906.java