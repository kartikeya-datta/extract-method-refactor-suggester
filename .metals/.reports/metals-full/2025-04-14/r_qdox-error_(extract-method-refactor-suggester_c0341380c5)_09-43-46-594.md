error id: file://<WORKSPACE>/data/code-rep-dataset/Dataset1/Tasks/3319.java
file://<WORKSPACE>/data/code-rep-dataset/Dataset1/Tasks/3319.java
### com.thoughtworks.qdox.parser.ParseException: syntax error @[1,1]

error in qdox parser
file content:
```java
offset: 1
uri: file://<WORKSPACE>/data/code-rep-dataset/Dataset1/Tasks/3319.java
text:
```scala
S@@ystem.err.println("To opt-in to this process, bundles need to have the following MANIFEST.MF");

/**
 * Licensed to the Apache Software Foundation (ASF) under one
 * or more contributor license agreements.  See the NOTICE file
 * distributed with this work for additional information
 * regarding copyright ownership.  The ASF licenses this file
 * to you under the Apache License, Version 2.0 (the
 * "License"); you may not use this file except in compliance
 * with the License.  You may obtain a copy of the License at
 *
 *   http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing,
 * software distributed under the License is distributed on an
 * "AS IS" BASIS, WITHOUT WARRANTIES OR CONDITIONS OF ANY
 * KIND, either express or implied.  See the License for the
 * specific language governing permissions and limitations
 * under the License.
 */
package org.apache.aries.spifly.statictool;

import java.io.ByteArrayInputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.net.URL;
import java.util.Properties;
import java.util.Set;
import java.util.jar.Attributes;
import java.util.jar.JarEntry;
import java.util.jar.JarInputStream;
import java.util.jar.JarOutputStream;
import java.util.jar.Manifest;

import org.apache.aries.spifly.ConsumerHeaderProcessor;
import org.apache.aries.spifly.Streams;
import org.apache.aries.spifly.Util;
import org.apache.aries.spifly.WeavingData;
import org.apache.aries.spifly.api.SpiFlyConstants;
import org.apache.aries.spifly.weaver.TCCLSetterVisitor;
import org.objectweb.asm.ClassReader;
import org.objectweb.asm.ClassVisitor;
import org.objectweb.asm.ClassWriter;
import org.osgi.framework.Constants;
import org.osgi.framework.Version;

public class Main {
    private static final String MODIFIED_BUNDLE_SUFFIX = "_spifly.jar";
    private static final String IMPORT_PACKAGE = "Import-Package";

    public static void usage() {
        System.err.println("This tool processes OSGi Bundles that use java.util.ServiceLoader.load() to");
        System.err.println("obtain implementations via META-INF/services. The byte code in the bundles is");
        System.err.println("modified so that the ThreadContextClassLoader is set appropriately for the ");
        System.err.println("duration of the java.util.ServiceLoader.load() call.");
        System.err.println("To opt-in to this process, bundles need to have the following Manifest.MF");
        System.err.println("header set:");
        System.err.println("    " + SpiFlyConstants.SPI_CONSUMER_HEADER + ": *");
        System.err.println("Modified bundles are written out under the following name:");
        System.err.println("    <original-bundle-name>" + MODIFIED_BUNDLE_SUFFIX);
        System.err.println();
        System.err.println("Usage: java " + Main.class.getName() + " bundle1.jar bundle2.jar ...");
        System.exit(-1);
    }

    public static void main(String ... args) throws Exception {
        if (args.length < 1)
            usage();

        for (String arg : args) {
            weaveJar(arg);
        }
    }

    private static void weaveJar(String jarPath) throws IOException {
        System.out.println("[SPI Fly Static Tool] Processing: " + jarPath);

        File jarFile = new File(jarPath);
        File tempDir = new File(System.getProperty("java.io.tmpdir") + File.separator + jarFile.getName() + "_" + System.currentTimeMillis());
        Manifest manifest = unJar(jarFile, tempDir);
        String consumerHeader = manifest.getMainAttributes().getValue(SpiFlyConstants.SPI_CONSUMER_HEADER);
        if (consumerHeader != null) {
            String bcp = manifest.getMainAttributes().getValue(Constants.BUNDLE_CLASSPATH);
            weaveDir(tempDir, consumerHeader, bcp);

            manifest.getMainAttributes().remove(new Attributes.Name(SpiFlyConstants.SPI_CONSUMER_HEADER));
            manifest.getMainAttributes().putValue(SpiFlyConstants.PROCESSED_SPI_CONSUMER_HEADER, consumerHeader);
            // TODO if new packages needed then...
            extendImportPackage(manifest);

            File newJar = getNewJarFile(jarFile);
            jar(newJar, tempDir, manifest);
        } else {
            System.out.println("[SPI Fly Static Tool] This file is not marked as an SPI Consumer.");
        }
        delTree(tempDir);
    }

    private static void extendImportPackage(Manifest manifest) throws IOException {
        String utilPkgVersion = getPackageVersion(Util.class);

        Version osgiVersion = Version.parseVersion(utilPkgVersion);

        Version minVersion = new Version(osgiVersion.getMajor(), osgiVersion.getMinor(), osgiVersion.getMicro());
        Version maxVersion = new Version(osgiVersion.getMajor(), osgiVersion.getMinor() + 1, 0);

        String ip = manifest.getMainAttributes().getValue(IMPORT_PACKAGE);
        StringBuilder sb = new StringBuilder(ip);
        sb.append(",");
        sb.append(Util.class.getPackage().getName());
        sb.append(";version=\"[");
        sb.append(minVersion);
        sb.append(",");
        sb.append(maxVersion);
        sb.append(")\"");
        manifest.getMainAttributes().putValue(IMPORT_PACKAGE, sb.toString());
    }

    private static String getPackageVersion(Class<?> clazz) throws IOException {
        URL url = clazz.getResource("packageinfo");
        if (url == null) {
            throw new RuntimeException("'packageinfo' file with version information not found for package: "
                    + clazz.getPackage().getName());
        }

        byte[] bytes = Streams.suck(url.openStream());
        Properties p = new Properties();
        p.load(new ByteArrayInputStream(bytes));
        return p.getProperty("version");
    }

    private static File getNewJarFile(File jarFile) {
        String s = jarFile.getAbsolutePath();
        int idx = s.lastIndexOf('.');
        s = s.substring(0, idx);
        s += MODIFIED_BUNDLE_SUFFIX;
        return new File(s);
    }

    private static void weaveDir(File dir, String consumerHeader, String bundleClassPath) throws IOException {
        String dirName = dir.getAbsolutePath();

        DirTree dt = new DirTree(dir);
        for (File f : dt.getFiles()) {
            if (!f.getName().endsWith(".class"))
                continue;

            String className = f.getAbsolutePath().substring(dirName.length());
            if (className.startsWith(File.separator))
                className = className.substring(1);
            className = className.substring(0, className.length() - ".class".length());
            className = className.replace(File.separator, ".");

            Set<WeavingData> wd = ConsumerHeaderProcessor.processHeader(consumerHeader);
            InputStream is = new FileInputStream(f);
            byte[] b;
            try {
                ClassReader cr = new ClassReader(is);
                ClassWriter cw = new ClassWriter(0);
                ClassVisitor cv = new TCCLSetterVisitor(cw, className, wd);
                cr.accept(cv, 0);
                b = cw.toByteArray();
            } finally {
                is.close();
            }

            OutputStream os = new FileOutputStream(f);
            try {
                os.write(b);
            } finally {
                os.close();
            }
        }

        if (bundleClassPath != null) {
            for (String entry : bundleClassPath.split(",")) {
                File jarFile = new File(dir, entry.trim());
                if (jarFile.isFile()) {
                    weaveBCPJar(jarFile, consumerHeader);
                }
            }
        }
    }

    private static void weaveBCPJar(File jarFile, String consumerHeader) throws IOException {
        File tempDir = new File(System.getProperty("java.io.tmpdir") + File.separator + jarFile.getName() + "_" + System.currentTimeMillis());
        try {
            Manifest manifest = unJar(jarFile, tempDir);
            weaveDir(tempDir, consumerHeader, null);
            if (!jarFile.delete()) {
                throw new IOException("Could not replace file: " + jarFile);
            }

            jar(jarFile, tempDir, manifest);
        } finally {
            delTree(tempDir);
        }
    }

    static Manifest unJar(File jarFile, File tempDir) throws IOException {
        ensureDirectory(tempDir);

        JarInputStream jis = new JarInputStream(new FileInputStream(jarFile));
        JarEntry je = null;
        while((je = jis.getNextJarEntry()) != null) {
            if (je.isDirectory()) {
                File outDir = new File(tempDir, je.getName());
                ensureDirectory(outDir);

                continue;
            }

            File outFile = new File(tempDir, je.getName());
            File outDir = outFile.getParentFile();
            ensureDirectory(outDir);

            OutputStream out = new FileOutputStream(outFile);
            try {
                Streams.pump(jis, out);
            } finally {
                out.flush();
                out.close();
                jis.closeEntry();
            }
            outFile.setLastModified(je.getTime());
        }

        Manifest manifest = jis.getManifest();
        if (manifest != null) {
            File mf = new File(tempDir, "META-INF/MANIFEST.MF");
            File mfDir = mf.getParentFile();
            ensureDirectory(mfDir);

            OutputStream out = new FileOutputStream(mf);
            try {
                manifest.write(out);
            } finally {
                out.flush();
                out.close();
            }
        }

        jis.close();
        return manifest;
    }

    static void jar(File jarFile, File rootFile, Manifest manifest) throws IOException {
        JarOutputStream jos = new JarOutputStream(new FileOutputStream(jarFile), manifest);
        try {
            addToJarRecursively(jos, rootFile.getAbsoluteFile(), rootFile.getAbsolutePath());
        } finally {
            jos.close();
        }
    }

    static void addToJarRecursively(JarOutputStream jar, File source, String rootDirectory) throws IOException {
        String sourceName = source.getAbsolutePath().replace("\\", "/");
        sourceName = sourceName.substring(rootDirectory.length());

        if (sourceName.startsWith("/")) {
            sourceName = sourceName.substring(1);
        }

        if ("META-INF/MANIFEST.MF".equals(sourceName))
            return;

        if (source.isDirectory()) {
            /* Is there any point in adding a directory beyond just taking up space?
            if (!sourceName.isEmpty()) {
                if (!sourceName.endsWith("/")) {
                    sourceName += "/";
                }
                JarEntry entry = new JarEntry(sourceName);
                jar.putNextEntry(entry);
                jar.closeEntry();
            }
            */
            for (File nested : source.listFiles()) {
                addToJarRecursively(jar, nested, rootDirectory);
            }
            return;
        }

        JarEntry entry = new JarEntry(sourceName);
        jar.putNextEntry(entry);
        InputStream is = new FileInputStream(source);
        try {
            Streams.pump(is, jar);
        } finally {
            jar.closeEntry();
            is.close();
        }
    }

    static void delTree(File tempDir) throws IOException {
        for (File f : new DirTree(tempDir).getFiles()) {
            if (!f.delete())
                throw new IOException("Problem deleting file: " + tempDir.getAbsolutePath());
        }
    }

    private static void ensureDirectory(File outDir) throws IOException {
        if (!outDir.isDirectory())
            if (!outDir.mkdirs())
                throw new IOException("Unable to create directory " + outDir.getAbsolutePath());
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

QDox parse error in file://<WORKSPACE>/data/code-rep-dataset/Dataset1/Tasks/3319.java