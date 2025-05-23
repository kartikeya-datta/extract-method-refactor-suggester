error id: file://<WORKSPACE>/data/code-rep-dataset/Dataset5/Tasks/12828.java
file://<WORKSPACE>/data/code-rep-dataset/Dataset5/Tasks/12828.java
### com.thoughtworks.qdox.parser.ParseException: syntax error @[1,1]

error in qdox parser
file content:
```java
offset: 1
uri: file://<WORKSPACE>/data/code-rep-dataset/Dataset5/Tasks/12828.java
text:
```scala
B@@ufferedImage image = new BufferedImage(size.width, size.height, BufferedImage.TYPE_BYTE_INDEXED);

/*
 * Licensed to the Apache Software Foundation (ASF) under one or more
 * contributor license agreements.  See the NOTICE file distributed with
 * this work for additional information regarding copyright ownership.
 * The ASF licenses this file to You under the Apache License, Version 2.0
 * (the "License"); you may not use this file except in compliance with
 * the License.  You may obtain a copy of the License at
 *
 *   http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 *
 */
package org.apache.jmeter.save;

import java.awt.Dimension;
import java.awt.Graphics2D;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.OutputStream;

import javax.swing.JComponent;

import org.apache.batik.ext.awt.image.codec.png.PNGEncodeParam;
import org.apache.batik.ext.awt.image.codec.png.PNGImageEncoder;
import org.apache.batik.ext.awt.image.codec.tiff.TIFFEncodeParam;
import org.apache.batik.ext.awt.image.codec.tiff.TIFFImageEncoder;
import org.apache.jmeter.util.JMeterUtils;
import org.apache.jorphan.util.JOrphanUtils;

/**
 * Class is responsible for taking a component and saving it as a JPEG, PNG or
 * TIFF. The class is very simple. Thanks to Batik and the developers who worked
 * so hard on it.
 */
public class SaveGraphicsService {

    public static final int PNG = 0;

    public static final int TIFF = 1;

    public static final String PNG_EXTENSION = ".png"; //$NON-NLS-1$

    public static final String TIFF_EXTENSION = ".tif"; //$NON-NLS-1$

    public static final String JPEG_EXTENSION = ".jpg"; //$NON-NLS-1$

    /**
     *
     */
    public SaveGraphicsService() {
        super();
    }

/*
 * This is not currently used by JMeter code.
 * As it uses Sun-specific code (the only such in JMeter), it has been commented out for now.
 */
//  /**
//   * If someone wants to save a JPEG, use this method. There is a limitation
//   * though. It uses gray scale instead of color due to artifacts with color
//   * encoding. For some reason, it does not translate pure red and orange
//   * correctly. To make the text readable, gray scale is used.
//   *
//   * @param filename
//   * @param component
//   */
//  public void saveUsingJPEGEncoder(String filename, JComponent component) {
//      Dimension size = component.getSize();
//      // We use Gray scale, since color produces poor quality
//      // this is an unfortunate result of the default codec
//      // implementation.
//      BufferedImage image = new BufferedImage(size.width, size.height, BufferedImage.TYPE_USHORT_GRAY);
//      Graphics2D grp = image.createGraphics();
//      component.paint(grp);
//
//      File outfile = new File(filename + JPEG_EXTENSION);
//      FileOutputStream fos = createFile(outfile);
//      JPEGEncodeParam param = JPEGCodec.getDefaultJPEGEncodeParam(image);
//      Float q = new Float(1.0);
//      param.setQuality(q.floatValue(), true);
//      JPEGImageEncoder encoder = JPEGCodec.createJPEGEncoder(fos, param);
//
//      try {
//          encoder.encode(image);
//      } catch (Exception e) {
//          log.warn(e.toString());
//      } finally {
//            JOrphanUtils.closeQuietly(fos);
//      }
//  }

    /**
     * Method will save the JComponent as an image. The formats are PNG, and
     * TIFF.
     *
     * @param filename
     * @param type
     * @param component
     */
    public void saveJComponent(String filename, int type, JComponent component) {
        Dimension size = component.getSize();
        BufferedImage image = new BufferedImage(size.width, size.height, BufferedImage.TYPE_INT_RGB);
        Graphics2D grp = image.createGraphics();
        component.paint(grp);

        if (type == PNG) {
            filename += PNG_EXTENSION;
            this.savePNGWithBatik(filename, image);
        } else if (type == TIFF) {
            filename = filename + TIFF_EXTENSION;
            this.saveTIFFWithBatik(filename, image);
        }
    }

    /**
     * Use Batik to save a PNG of the graph
     *
     * @param filename
     * @param image
     */
    public void savePNGWithBatik(String filename, BufferedImage image) {
        File outfile = new File(filename);
        OutputStream fos = createFile(outfile);
        if (fos == null) {
            return;
        }
        PNGEncodeParam param = PNGEncodeParam.getDefaultEncodeParam(image);
        PNGImageEncoder encoder = new PNGImageEncoder(fos, param);
        try {
            encoder.encode(image);
        } catch (IOException e) {
            JMeterUtils.reportErrorToUser("PNGImageEncoder reported: "+e.getMessage(), "Problem creating image file");
        } finally {
            JOrphanUtils.closeQuietly(fos);
        }
    }

    /**
     * Use Batik to save a TIFF file of the graph
     *
     * @param filename
     * @param image
     */
    public void saveTIFFWithBatik(String filename, BufferedImage image) {
        File outfile = new File(filename);
        OutputStream fos = createFile(outfile);
        if (fos == null) {
            return;
        }
        TIFFEncodeParam param = new TIFFEncodeParam();
        TIFFImageEncoder encoder = new TIFFImageEncoder(fos, param);
        try {
            encoder.encode(image);
        } catch (IOException e) {
            JMeterUtils.reportErrorToUser("TIFFImageEncoder reported: "+e.getMessage(), "Problem creating image file");
        // Yuck: TIFFImageEncoder uses Error to report runtime problems
        } catch (Error e) {
            JMeterUtils.reportErrorToUser("TIFFImageEncoder reported: "+e.getMessage(), "Problem creating image file");
            if (e.getClass() != Error.class){// rethrow other errors
                throw e;
            }
        } finally {
            JOrphanUtils.closeQuietly(fos);
        }
    }

    /**
     * Create a new file for the graphics. Since the method creates a new file,
     * we shouldn't get a FNFE.
     *
     * @param filename
     * @return output stream created from the filename
     */
    private FileOutputStream createFile(File filename) {
        try {
            return new FileOutputStream(filename);
        } catch (FileNotFoundException e) {
            JMeterUtils.reportErrorToUser("Could not create file: "+e.getMessage(), "Problem creating image file");
            return null;
        }
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

QDox parse error in file://<WORKSPACE>/data/code-rep-dataset/Dataset5/Tasks/12828.java