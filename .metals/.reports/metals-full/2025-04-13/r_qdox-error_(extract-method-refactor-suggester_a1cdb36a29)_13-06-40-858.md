error id: file://<WORKSPACE>/data/code-rep-dataset/Dataset5/Tasks/17768.java
file://<WORKSPACE>/data/code-rep-dataset/Dataset5/Tasks/17768.java
### com.thoughtworks.qdox.parser.ParseException: syntax error @[360,80]

error in qdox parser
file content:
```java
offset: 10423
uri: file://<WORKSPACE>/data/code-rep-dataset/Dataset5/Tasks/17768.java
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
package wicket.util.resource;

import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStream;
import java.net.URL;
import java.util.Locale;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;

import wicket.util.file.File;
import wicket.util.file.Path;
import wicket.util.string.Strings;
import wicket.util.time.Time;
import wicket.util.watch.IChangeable;


/**
 * Resource abstraction. Resources can be loaded from the source tree or from the
 * classpath. When loaded from the source tree, they have a File nature which can be
 * watched for changes. When loaded from the classpath, they have a stream nature and are
 * not watched for changes.
 * 
 * @author Jonathan Locke
 */
public final class Resource implements IResource, IChangeable
{
    /** Logging  */
    private static Log log = LogFactory.getLog(Resource.class);
    
    /** Any associated file */
    private File file;

    /** Any resource location */
    private URL url;

    /** Resource stream */
    private InputStream inputStream;

    /**
     * Constructor
     */
    private Resource()
    {
    }

    /**
     * Constructor
     * @param file A resource file
     */
    private Resource(final File file)
    {
        this.file = file;
    }

    /**
     * Constructor
     * @param url A resource URL
     */
    private Resource(final URL url)
    {
        // Get filename from URL
        String filename = url.getFile();

        // If there is a filename
        if (filename != null)
        {
            // If a file with the given name exists
            final File file = new File(filename);

            if (file.exists())
            {
                // save that file for future checking
                this.file = file;
            }
        }

        // Save URL
        this.url = url;
    }

    /**
     * Locate a resource based on its class name and the extension provided.
     * 
     * @param c Class next to which resource file is
     * @param extension The extension of the file with the same name as the class
     * @return The resource
     * @see Resource#locate(Path, Class, String, Locale, String)
     */
    public static Resource locate(final Class c, final String extension)
    {
        return locate(c, extension);
    }

    /**
     * Locate a resource based on a rootPath (sourcePath), a class name and
     * an extension.
     * 
     * @param path Source path to look along for resource
     * @param c Class next to which resource file is
     * @param extension The extension of the file with the same name as the class
     * @return The resource
     * @see Resource#locate(Path, Class, String, Locale, String)
     */
    public static Resource locate(final Path path, final Class c, final String extension)
    {
        return locate(path, c, null, Locale.getDefault(), extension);
    }

    /**
     * Loads a resource from the file system from next to a given class. This method
     * prefers to load from the source tree first in the default locale. If a source tree
     * cannot be found, the classpath is used.
     * 
     * @param path Source path to look along for resource
     * @param c Class next to which resource file is
     * @param style Any resource style, such as a skin style
     * @param locale The locale of the resource to load
     * @param extension The extension of the file with the same name as the class
     * @return The resource
     */
    public static Resource locate(final Path path, final Class c, final String style,
            final Locale locale, final String extension)
    {
        return locate(path, c.getClassLoader(), c.getName(), style, locale, extension);
    }

    /**
     * Loads a resource from the file system from next to a given class. This method
     * prefers to load from the source tree first in the default locale. If a source tree
     * cannot be found, the classpath is used.
     * 
     * @param sourcePath Source path to look along for resource
     * @param classloader The classloader to look on if the resource is not on the source
     *            path
     * @param resourcePath The path to the resource
     * @param style Any resource style, such as a skin style
     * @param locale The locale of the resource to load
     * @param extension The extension of the resource
     * @return The resource
     */
    public static Resource locate(final Path sourcePath, final ClassLoader classloader,
            String resourcePath, final String style, final Locale locale, final String extension)
    {
        // If no extension specified, extract extension
        final String extensionString;

        if (extension == null)
        {
            extensionString = "." + Strings.lastPathComponent(resourcePath, '.');
            resourcePath = Strings.beforeLastPathComponent(resourcePath, '.');
        }
        else
        {
            extensionString = "." + extension;
        }

        // Compute string components
        resourcePath = resourcePath.replace('.', '/');

        // The resource
        Resource resource = null;

        // Search the sourcePath provided first and classpath second
        if ((sourcePath != null) && (sourcePath.size() > 0))
        {
            resource = new ResourceLocator()
			{
	            /**
	             * Check if file exists.
	             * 
	             * @param filename name of the file
	             * @return null, if file not found
	             */
	            protected Resource locate(final String filename)
	            {
	                log.debug("locate Resource from source path: " + filename);
	                final File file = sourcePath.find(filename);

	                if (file != null)
	                {
	                    return new Resource(file);
	                }

	                return null;
	            }
	    	}.locate(resourcePath, style, locale, extensionString);
	    	
	    	if (resource != null)
	    	{
	    		return resource;
	    	}
        }

        // Try classpath second
        resource = new ResourceLocator()
		{
            /**
             * Check if file exists.
             * @param filename the resource's filename
             * @return null, if file not found
             */
            protected Resource locate(final String filename)
            {
                log.debug("locate Resource from class path: " + filename);
                // Try loading filename on classpath
                final URL url = classloader.getResource(filename);

                if (url != null)
                {
                    return new Resource(url);
                }

                return null;
            }
    	}.locate(resourcePath, style, locale, extensionString);

    	return resource;
    }

    /**
     * Create a Resource based on a Class's name and an extension
     * @param classname A 'path' with dotted separators
     * @param extension The file's extension incl. '.'
     * @return null, if file does not exist
     */
    public static Resource locate(final String classname, final String extension)
    {
        String filename = Strings.replaceAll(classname, ".", "/") + extension;
        final URL url = Thread.currentThread().getContextClassLoader().getResource(filename);
        
        return (url != null ? new Resource(url) : null);
    }

    /**
     * Closes input stream
     * @throws IOException
     */
    public void close() throws IOException
    {
        if (inputStream != null)
        {
            inputStream.close();
            inputStream = null;
        }
    }

    /**
     * @return Returns the file.
     */
    public File getFile()
    {
        return file;
    }

    /**
     * @return The extension of this resource, such as "jpeg" or "html"
     */
    public String getExtension()
    {
        if (file != null)
        {
            return Strings.lastPathComponent(file.getName(), '.');
        }
        else
        {
            return Strings.lastPathComponent(url.getPath(), '.');
        }
    }

    /**
     * @return Returns the inputStream.
     * @throws ResourceNotFoundException
     */
    public InputStream getInputStream() throws ResourceNotFoundException
    {
        if (inputStream == null)
        {
            if (file != null)
            {
                try
                {
                    inputStream = new FileInputStream(file);
                }
                catch (FileNotFoundException e)
                {
                    throw new ResourceNotFoundException("Resource " + file 
                            + " could not be found", e);
                }
            }
            else if (url != null)
            {
                try
                {
                    inputStream = url.openStream();
                }
                catch (IOException e)
                {
                    throw new ResourceNotFoundException("Resource " + url 
                            + " could not be opened", e);
                }
            }
        }

        return inputStream;
    }

    /**
     * @return Returns the url.
     */
    public URL getUrl()
    {
        return url;
    }

    /**
     * @see wicket.util.watch.IChangeable#lastModifiedTime()
     */
    public Time lastModifiedTime()
    {
        if (file != null)
        {
            return file.lastModifiedTime();
        }

        return null;
    }

    /**
     * @see java.lang.Object#toString()
     */
    public String toString()
    {
        return (file != null) ? file.toString() : url.toString();
    }
}

///////////////////////////////// End of File /////////////////////////////////@@
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

QDox parse error in file://<WORKSPACE>/data/code-rep-dataset/Dataset5/Tasks/17768.java