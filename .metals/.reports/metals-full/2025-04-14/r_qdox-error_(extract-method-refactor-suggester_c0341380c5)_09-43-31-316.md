error id: file://<WORKSPACE>/data/code-rep-dataset/Dataset5/Tasks/17631.java
file://<WORKSPACE>/data/code-rep-dataset/Dataset5/Tasks/17631.java
### com.thoughtworks.qdox.parser.ParseException: syntax error @[164,80]

error in qdox parser
file content:
```java
offset: 3860
uri: file://<WORKSPACE>/data/code-rep-dataset/Dataset5/Tasks/17631.java
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
package wicket.markup.html.image;


import javax.imageio.ImageIO;
import javax.imageio.ImageWriter;

import wicket.RenderException;
import wicket.util.resource.IResource;
import wicket.util.resource.ResourceNotFoundException;

import java.awt.image.BufferedImage;

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.InputStream;

/**
 * @author Jonathan Locke
 */
public class DynamicImage extends Image
{
    /** Serial Version ID */
	private static final long serialVersionUID = 5934721258765771884L;

	private String extension;

    private BufferedImage image;

    private InputStream inputStream;

    private ByteArrayOutputStream out;

    /**
     * Constructor
     * @param name
     */
    public DynamicImage(String name)
    {
        super(name);
    }

    /**
     * @return Returns the extension.
     */
    public String getExtension()
    {
        return extension;
    }

    /**
     * @return Returns the image.
     */
    public BufferedImage getImage()
    {
        return image;
    }

    /**
     * @return Returns the inputStream.
     */
    public InputStream getInputStream()
    {
        return inputStream;
    }

    /**
     * @param extension The extension to set.
     * @return This
     */
    public DynamicImage setExtension(String extension)
    {
        this.extension = extension;

        return this;
    }

    /**
     * @param image The image to set
     * @return This
     */
    public DynamicImage setImage(final BufferedImage image)
    {
        try
        {
            out = new ByteArrayOutputStream();

            // Get image writer for extension
            final ImageWriter writer = (ImageWriter) ImageIO.getImageWritersByFormatName(extension)
                    .next();

            // Write out gif
            writer.setOutput(ImageIO.createImageOutputStream(out));
            writer.write(image);

            // Set input stream
            setInputStream(new ByteArrayInputStream(out.toByteArray()));
        }
        catch (IOException e)
        {
            throw new RenderException("Unable to convert image to stream", e);
        }

        return this;
    }

    /**
     * @param inputStream Image source input stream
     * @return This
     */
    public DynamicImage setInputStream(final InputStream inputStream)
    {
        this.inputStream = inputStream;

        return this;
    }

    /**
     * @param source The source attribute of the image tag
     * @return Gets the image resource to attach to the component.
     */
    protected IResource getImageResource(final String source)
    {
        return new IResource()
        {
            public void close() throws IOException
            {
                inputStream.close();
            }

            public String getExtension()
            {
                return extension;
            }

            public InputStream getInputStream() throws ResourceNotFoundException
            {
                return inputStream;
            }
        };
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

QDox parse error in file://<WORKSPACE>/data/code-rep-dataset/Dataset5/Tasks/17631.java