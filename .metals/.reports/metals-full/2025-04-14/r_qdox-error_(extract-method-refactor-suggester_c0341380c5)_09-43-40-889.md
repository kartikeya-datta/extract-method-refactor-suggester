error id: file://<WORKSPACE>/data/code-rep-dataset/Dataset5/Tasks/5310.java
file://<WORKSPACE>/data/code-rep-dataset/Dataset5/Tasks/5310.java
### com.thoughtworks.qdox.parser.ParseException: syntax error @[1,1]

error in qdox parser
file content:
```java
offset: 1
uri: file://<WORKSPACE>/data/code-rep-dataset/Dataset5/Tasks/5310.java
text:
```scala
l@@og("project.createTask(\"copy\") failed - direct", Project.MSG_VERBOSE);

/* *******************************************************************
 * Copyright (c) 1999-2001 Xerox Corporation, 
 *               2002 Palo Alto Research Center, Incorporated (PARC).
 * All rights reserved. 
 * This program and the accompanying materials are made available 
 * under the terms of the Common Public License v1.0 
 * which accompanies this distribution and is available at 
 * http://www.eclipse.org/legal/cpl-v10.html 
 *  
 * Contributors: 
 *     Xerox/PARC     initial implementation 
 * ******************************************************************/

//XXX INCLUDES CODE FROM ANT -- UNDER APACHE LICENSE
package org.aspectj.internal.tools.ant.taskdefs;

import org.apache.tools.ant.types.*;
import java.io.*;
import java.util.zip.ZipOutputStream;
import java.util.*;
import java.util.zip.*;
import org.apache.tools.ant.taskdefs.*;
import org.apache.tools.ant.*;

public class AJInstaller extends MatchingTask {
    static final String INCLUDE_CLASSES = "$installer$/org/aspectj/*.class";
    static final String MAIN_CLASS = "$installer$.org.aspectj.Main";
    static final String CONTENTS_FILE = "$installer$/org/aspectj/resources/contents.txt";
    private String htmlSrc;

    public void setHtmlSrc(String v) { htmlSrc = v; }

    private String resourcesSrc;

    public void setResourcesSrc(String v) { resourcesSrc = v; }

    private String mainclass;

    public void setMainclass(String v) { mainclass = v; }

    private File installerClassJar;

    public void setInstallerclassjar(String v) { 
        installerClassJar = project.resolveFile(v);
    }

    protected List contentsNames = new ArrayList();

    protected long contentsBytes = 0;

    protected void addToContents(File file, String vPath) {
        contentsNames.add(vPath);
        contentsBytes += file.length();
    }

    String[] getFiles(File baseDir) {
        DirectoryScanner ds = new DirectoryScanner();
        setBasedir(baseDir.getAbsolutePath());
        ds.setBasedir(baseDir);
        //ds.setIncludes(new String [] {pattern});
        ds.scan();
        return ds.getIncludedFiles();
    }

    protected Copy getCopyTask() {
        Copy cd = (Copy)project.createTask("copy");
        if (null == cd) {
            log("project.createTask(\"copy\") failed - direct", project.MSG_VERBOSE);
            cd = new Copy();
            cd.setProject(getProject());
        }
        return cd;
    }
    protected void finishZipOutputStream(ZipOutputStream zOut) throws IOException, BuildException {
        writeContents(zOut);
        writeManifest(zOut);
        File tmpDirF = File.createTempFile("tgz", ".di");
        File tmpDir = new File(tmpDirF.getAbsolutePath() + "r");
        tmpDirF.delete();
        String tmp = tmpDir.getAbsolutePath();

        // installer class files
        Expand expand = new Expand();
        expand.setProject(getProject());
        expand.setSrc(installerClassJar);
        expand.setDest(new File(tmp));
        PatternSet patterns = new PatternSet();
        patterns.setIncludes(INCLUDE_CLASSES);
        expand.addPatternset(patterns);
        expand.execute();        

        // move the correct resource files into the jar
        Copy cd = getCopyTask();
        fileset = new FileSet();
        fileset.setDir(new File(resourcesSrc));
        fileset.setIncludes("*");
        fileset.setExcludes("contents.txt,properties.txt");
        cd.addFileset(fileset);
        cd.setTodir(new File(tmp+"/$installer$/org/aspectj/resources"));
        cd.execute();
        project.addFilter("installer.main.class", this.mainclass);
        Copy cf = getCopyTask();
        fileset = new FileSet();
        fileset.setDir(new File(resourcesSrc));
        fileset.setIncludes("properties.txt");
        cf.setFiltering(true);
        cf.addFileset(fileset);
        cf.setTodir(new File(tmp+"/$installer$/org/aspectj/resources"));
        cf.execute();
        // move the correct resource files into the jar
        cd = getCopyTask();
        fileset = new FileSet();
        fileset.setDir(new File(htmlSrc));
        fileset.setIncludes("*");
        cd.addFileset(fileset);
        cd.setTodir(new File(tmp+"/$installer$/org/aspectj/resources"));
        cd.execute();
        // now move these files into the jar
        setBasedir(tmp);
        writeFiles(zOut, getFiles(tmpDir));
        // and delete the tmp dir
        Delete dt = (Delete)project.createTask("delete");
        if (null == dt) {
            dt = new Delete();
            dt.setProject(getProject());
        }
        dt.setDir(new File(tmp));
        dt.execute();
    }

    static final char NEWLINE = '\n';

    protected void writeContents(ZipOutputStream zOut) throws IOException {
        // write to a StringBuffer
        StringBuffer buf = new StringBuffer();
        buf.append(contentsBytes);
        buf.append(NEWLINE);
        for (Iterator i = contentsNames.iterator(); i.hasNext(); ) {
            String name = (String)i.next();
            buf.append(name);
            buf.append(NEWLINE);
        }
        zipFile(new StringBufferInputStream(buf.toString()), zOut, CONTENTS_FILE, System.currentTimeMillis());
    }

    protected void writeManifest(ZipOutputStream zOut) throws IOException {
        // write to a StringBuffer
        StringBuffer buf = new StringBuffer();
        buf.append("Manifest-Version: 1.0");
        buf.append(NEWLINE);
        buf.append("Main-Class: " + MAIN_CLASS);
        buf.append(NEWLINE);
        zipFile(new StringBufferInputStream(buf.toString()), zOut, "META-INF/MANIFEST.MF", System.currentTimeMillis());
    }

    //XXX cut-and-paste from Zip super-class (under apache license)
    private File zipFile;
    private File baseDir;
    private boolean doCompress = true;
    protected String archiveType = "zip";

    /**
     * This is the name/location of where to
     * create the .zip file.
     */
    public void setZipfile(String zipFilename) {
        zipFile = project.resolveFile(zipFilename);
    }

    /**
     * This is the base directory to look in for
     * things to zip.
     */
    public void setBasedir(String baseDirname) {
        baseDir = project.resolveFile(baseDirname);
    }

    /**
     * Sets whether we want to compress the files or only store them.
     */
    public void setCompress(String compress) {
        doCompress = Project.toBoolean(compress);
    }

    protected void initZipOutputStream(ZipOutputStream zOut)
        throws IOException, BuildException
    {
    }

    protected void zipDir(File dir, ZipOutputStream zOut, String vPath)
        throws IOException
    {
    }

    protected void zipFile(InputStream in, ZipOutputStream zOut, String vPath,
                           long lastModified)
        throws IOException
    {
        ZipEntry ze = new ZipEntry(vPath);
        ze.setTime(lastModified);

        /*
         * XXX ZipOutputStream.putEntry expects the ZipEntry to know its
         * size and the CRC sum before you start writing the data when using
         * STORED mode.
         *
         * This forces us to process the data twice.
         *
         * I couldn't find any documentation on this, just found out by try
         * and error.
         */
        if (!doCompress) {
            long size = 0;
            CRC32 cal = new CRC32();
            if (!in.markSupported()) {
                // Store data into a byte[]
                ByteArrayOutputStream bos = new ByteArrayOutputStream();
                byte[] buffer = new byte[8 * 1024];
                int count = 0;
                do {
                    size += count;
                    cal.update(buffer, 0, count);
                    bos.write(buffer, 0, count);
                    count = in.read(buffer, 0, buffer.length);
                } while (count != -1);
                in = new ByteArrayInputStream(bos.toByteArray());
            } else {
                in.mark(Integer.MAX_VALUE);
                byte[] buffer = new byte[8 * 1024];
                int count = 0;
                do {
                    size += count;
                    cal.update(buffer, 0, count);
                    count = in.read(buffer, 0, buffer.length);
                } while (count != -1);
                in.reset();
            }
            ze.setSize(size);
            ze.setCrc(cal.getValue());
        }
        zOut.putNextEntry(ze);
        byte[] buffer = new byte[8 * 1024];
        int count = 0;
        do {
            zOut.write(buffer, 0, count);
            count = in.read(buffer, 0, buffer.length);
        } while (count != -1);
    }

    protected void zipFile(File file, ZipOutputStream zOut, String vPath)
        throws IOException
    {
        if ( !vPath.startsWith("$installer$") ) {
            addToContents(file, vPath);
        }
        FileInputStream fIn = new FileInputStream(file);
        try {
            zipFile(fIn, zOut, vPath, file.lastModified());
        } finally {
            fIn.close();
        }
    }

    public void execute() throws BuildException {
        if (installerClassJar == null) {
            throw new BuildException("installerClassJar attribute must be set!");
        }
        if (!installerClassJar.canRead() 
 !installerClassJar.getPath().endsWith(".jar")) {
            throw new BuildException("not readable jar:" + installerClassJar);
        }
//        if (installerClassDir == null) {
//            throw new BuildException("installerClassDir attribute must be set!");
//        }
//        if (!installerClassDir.exists()) {
//            throw new BuildException("no such directory: installerClassDir=" + installerClassDir);
//        }
        if (baseDir == null) {
            throw new BuildException("basedir attribute must be set!");
        }
        if (!baseDir.exists()) {
            throw new BuildException("basedir does not exist!");
        }
        DirectoryScanner ds = super.getDirectoryScanner(baseDir);
        String[] files = ds.getIncludedFiles();
        String[] dirs  = ds.getIncludedDirectories();
        log("Building installer: "+ zipFile.getAbsolutePath());
        ZipOutputStream zOut = null;
        try {
            zOut = new ZipOutputStream(new FileOutputStream(zipFile));
            if (doCompress) {
                zOut.setMethod(ZipOutputStream.DEFLATED);
            } else {
                zOut.setMethod(ZipOutputStream.STORED);
            }
            initZipOutputStream(zOut);
            writeDirs(zOut, dirs);
            writeFiles(zOut, files);
            finishZipOutputStream(zOut);
        } catch (IOException ioe) {
            String msg = "Problem creating " + archiveType + " " + ioe.getMessage();
            throw new BuildException(msg, ioe, location);
        } finally {
            if (zOut != null) {
                try {
                    // close up
                    zOut.close();
                }
                catch (IOException e) {}
            }
        }
    }

    protected void writeDirs(ZipOutputStream zOut, String[] dirs) throws IOException {
        for (int i = 0; i < dirs.length; i++) {
            File f = new File(baseDir,dirs[i]);
            String name = dirs[i].replace(File.separatorChar,'/')+"/";
            zipDir(f, zOut, name);
        }
    }

    protected void writeFiles(ZipOutputStream zOut, String[] files) throws IOException {
        for (int i = 0; i < files.length; i++) {
            File f = new File(baseDir,files[i]);
            String name = files[i].replace(File.separatorChar,'/');
            zipFile(f, zOut, name);
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

QDox parse error in file://<WORKSPACE>/data/code-rep-dataset/Dataset5/Tasks/5310.java