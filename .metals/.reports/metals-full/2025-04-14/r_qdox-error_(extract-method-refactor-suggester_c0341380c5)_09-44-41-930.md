error id: file://<WORKSPACE>/data/code-rep-dataset/Dataset5/Tasks/8248.java
file://<WORKSPACE>/data/code-rep-dataset/Dataset5/Tasks/8248.java
### com.thoughtworks.qdox.parser.ParseException: syntax error @[1,1]

error in qdox parser
file content:
```java
offset: 1
uri: file://<WORKSPACE>/data/code-rep-dataset/Dataset5/Tasks/8248.java
text:
```scala
d@@oOneJar( new File( fs.getDir(project), jarFiles[j] ), null);

/*
 * The Apache Software License, Version 1.1
 *
 * Copyright (c) 1999 The Apache Software Foundation.  All rights
 * reserved.
 *
 * Redistribution and use in source and binary forms, with or without
 * modification, are permitted provided that the following conditions
 * are met:
 *
 * 1. Redistributions of source code must retain the above copyright
 *    notice, this list of conditions and the following disclaimer.
 *
 * 2. Redistributions in binary form must reproduce the above copyright
 *    notice, this list of conditions and the following disclaimer in
 *    the documentation and/or other materials provided with the
 *    distribution.
 *
 * 3. The end-user documentation included with the redistribution, if
 *    any, must include the following acknowlegement:
 *       "This product includes software developed by the
 *        Apache Software Foundation (http://www.apache.org/)."
 *    Alternately, this acknowlegement may appear in the software itself,
 *    if and wherever such third-party acknowlegements normally appear.
 *
 * 4. The names "The Jakarta Project", "Ant", and "Apache Software
 *    Foundation" must not be used to endorse or promote products derived
 *    from this software without prior written permission. For written
 *    permission, please contact apache@apache.org.
 *
 * 5. Products derived from this software may not be called "Apache"
 *    nor may "Apache" appear in their names without prior written
 *    permission of the Apache Group.
 *
 * THIS SOFTWARE IS PROVIDED ``AS IS'' AND ANY EXPRESSED OR IMPLIED
 * WARRANTIES, INCLUDING, BUT NOT LIMITED TO, THE IMPLIED WARRANTIES
 * OF MERCHANTABILITY AND FITNESS FOR A PARTICULAR PURPOSE ARE
 * DISCLAIMED.  IN NO EVENT SHALL THE APACHE SOFTWARE FOUNDATION OR
 * ITS CONTRIBUTORS BE LIABLE FOR ANY DIRECT, INDIRECT, INCIDENTAL,
 * SPECIAL, EXEMPLARY, OR CONSEQUENTIAL DAMAGES (INCLUDING, BUT NOT
 * LIMITED TO, PROCUREMENT OF SUBSTITUTE GOODS OR SERVICES; LOSS OF
 * USE, DATA, OR PROFITS; OR BUSINESS INTERRUPTION) HOWEVER CAUSED AND
 * ON ANY THEORY OF LIABILITY, WHETHER IN CONTRACT, STRICT LIABILITY,
 * OR TORT (INCLUDING NEGLIGENCE OR OTHERWISE) ARISING IN ANY WAY OUT
 * OF THE USE OF THIS SOFTWARE, EVEN IF ADVISED OF THE POSSIBILITY OF
 * SUCH DAMAGE.
 * ====================================================================
 *
 * This software consists of voluntary contributions made by many
 * individuals on behalf of the Apache Software Foundation.  For more
 * information on the Apache Software Foundation, please see
 * <http://www.apache.org/>.
 */
package org.apache.tools.ant.taskdefs;

import java.io.File;
import java.io.IOException;
import java.util.Enumeration;
import java.util.Vector;
import java.util.zip.ZipEntry;
import java.util.zip.ZipFile;
import org.apache.tools.ant.Task;
import org.apache.tools.ant.BuildException;
import org.apache.tools.ant.DirectoryScanner;
import org.apache.tools.ant.Project;

import org.apache.tools.ant.types.FileSet;

/**
 * Sign a archive.
 *
 * @author Peter Donald <a href="mailto:donaldp@apache.org">donaldp@apache.org</a>
 * @author Nick Fortescue <a href="mailto:nick@ox.compsoc.net">nick@ox.compsoc.net</a>
 */
public class SignJar extends Task {

    /**
     * The name of the jar file.
     */
    protected File jar;

    /**
     * The alias of signer.
     */
    protected String alias;

    /**
     * The name of keystore file.
     */
    protected File keystore;

    protected String storepass;
    protected String storetype;
    protected String keypass;
    protected File sigfile;
    protected File signedjar;
    protected boolean verbose;
    protected boolean internalsf;
    protected boolean sectionsonly;

    /**
     * the filesets of the jars to sign
     */
    protected Vector filesets = new Vector();
    /**
     * Whether to assume a jar which has an appropriate .SF file in is already
     * signed.
     */
    protected boolean lazy;

    public void setJar(final File jar) {
        this.jar = jar;
    }

    public void setAlias(final String alias) {
        this.alias = alias;
    }

    public void setKeystore(final File keystore) {
        this.keystore = keystore;
    }

    public void setStorepass(final String storepass) {
        this.storepass = storepass;
    }

    public void setStoretype(final String storetype) {
        this.storetype = storetype;
    }

    public void setKeypass(final String keypass) {
        this.keypass = keypass;
    }

    public void setSigfile(final File sigfile) {
        this.sigfile = sigfile;
    }

    public void setSignedjar(final File signedjar) {
        this.signedjar = signedjar;
    }

    public void setVerbose(final boolean verbose) {
        this.verbose = verbose;
    }

    public void setInternalsf(final boolean internalsf) {
        this.internalsf = internalsf;
    }

    public void setSectionsonly(final boolean sectionsonly) {
        this.sectionsonly = sectionsonly;
    }

    public void setLazy(final boolean lazy) {
        this.lazy = lazy;
    }

    /**
     * Adds a set of files (nested fileset attribute).
     */
    public void addFileset(final FileSet set) {
        filesets.addElement(set);
    }


    public void execute() throws BuildException {
        if (null == jar && null == filesets) {
            throw new BuildException("jar must be set through jar attribute or nested filesets");
        }
        if( null != jar ) {
            doOneJar(jar, signedjar);
            return;
        } else {
            //Assume null != filesets

            // deal with the filesets
            for (int i=0; i<filesets.size(); i++) {
                FileSet fs = (FileSet) filesets.elementAt(i);
                DirectoryScanner ds = fs.getDirectoryScanner(project);
                String[] jarFiles = ds.getIncludedFiles();
                for(int j=0; j<jarFiles.length; j++) {
                    doOneJar( new File( jarFiles[j] ), null);
                }
            }
        }
    }

    private void doOneJar(File jarSource, File jarTarget) throws BuildException {
        if (project.getJavaVersion().equals(Project.JAVA_1_1)) {
            throw new BuildException("The signjar task is only available on JDK versions 1.2 or greater");
        }

        if (null == alias) {
            throw new BuildException("alias attribute must be set");
        }

        if (null == storepass) {
            throw new BuildException("storepass attribute must be set");
        }

        if(isUpToDate(jarSource, jarTarget)) return;

        final StringBuffer sb = new StringBuffer();

        final ExecTask cmd = (ExecTask) project.createTask("exec");
        cmd.setExecutable("jarsigner");

        if (null != keystore) {
            cmd.createArg().setValue("-keystore");
            cmd.createArg().setValue( keystore.toString() );
        }

        if (null != storepass) {
            cmd.createArg().setValue("-storepass");
            cmd.createArg().setValue(storepass);
        }

        if (null != storetype) {
            cmd.createArg().setValue("-storetype");
            cmd.createArg().setValue(storetype);
        }

        if (null != keypass) {
            cmd.createArg().setValue("-keypass");
            cmd.createArg().setValue(keypass);
        }

        if (null != sigfile) {
            cmd.createArg().setValue("-sigfile");
            cmd.createArg().setValue( sigfile.toString() );
        }

        if (null != jarTarget) {
            cmd.createArg().setValue("-signedjar");
            cmd.createArg().setValue( jarTarget.toString() );
        }

        if (verbose) {
            cmd.createArg().setValue("-verbose");
        }

        if (internalsf) {
            cmd.createArg().setValue("-internalsf");
        }

        if (sectionsonly) {
            cmd.createArg().setValue("-sectionsonly");
        }

        cmd.createArg().setValue( jarSource.toString() );

        cmd.createArg().setValue(alias);

        log("Signing Jar : " + jarSource.getAbsolutePath());
        cmd.setFailonerror(true);
        cmd.setTaskName( getTaskName() );
        cmd.execute();
    }

    protected boolean isUpToDate(File jarFile, File signedjarFile) {
        if( null == jarFile ) {
            return false;
        }

        if( null != signedjarFile ) {

            if(!jarFile.exists()) return false;
            if(!signedjarFile.exists()) return false;
            if(jarFile.equals(signedjarFile)) return false;
            if(signedjarFile.lastModified() > jarFile.lastModified())
                return true;
        } else {
            if( lazy ) {
                return isSigned(jarFile);
            }
        }

        return false;
    }

    protected boolean isSigned(File file) {
        final String SIG_START = "META-INF/";
        final String SIG_END = ".SF";

        if( !file.exists() ) {
            return false;
        }
        ZipFile jarFile = null;
        try {
            jarFile = new ZipFile(file);
            if(null == alias) {
                Enumeration entries = jarFile.entries();
                while(entries.hasMoreElements()) {
                    String name =  ((ZipEntry)entries.nextElement()).getName();
                    if(name.startsWith(SIG_START) && name.endsWith(SIG_END)) {
                        return true;
                    }
                }
                return false;
            } else {
                return jarFile.getEntry(SIG_START+alias.toUpperCase()+
                                        SIG_END) != null;
            }
        } catch(IOException e) {
            return false;
        } finally {
            if(jarFile != null) {
                try {jarFile.close();} catch(IOException e) {}
            }
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
	java.base/java.util.concurrent.ForkJoinPool.helpJoin(ForkJoinPool.java:1883)
	java.base/java.util.concurrent.ForkJoinTask.awaitDone(ForkJoinTask.java:440)
	java.base/java.util.concurrent.ForkJoinTask.join(ForkJoinTask.java:670)
	scala.collection.parallel.ForkJoinTasks$FJTWrappedTask.sync(Tasks.scala:243)
	scala.collection.parallel.ForkJoinTasks$FJTWrappedTask.sync$(Tasks.scala:243)
	scala.collection.parallel.AdaptiveWorkStealingForkJoinTasks$AWSFJTWrappedTask.sync(Tasks.scala:304)
	scala.collection.parallel.AdaptiveWorkStealingTasks$AWSTWrappedTask.internal(Tasks.scala:173)
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

QDox parse error in file://<WORKSPACE>/data/code-rep-dataset/Dataset5/Tasks/8248.java