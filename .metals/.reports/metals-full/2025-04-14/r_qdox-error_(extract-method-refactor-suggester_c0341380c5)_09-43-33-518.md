error id: file://<WORKSPACE>/data/code-rep-dataset/Dataset5/Tasks/12457.java
file://<WORKSPACE>/data/code-rep-dataset/Dataset5/Tasks/12457.java
### com.thoughtworks.qdox.parser.ParseException: syntax error @[1,1]

error in qdox parser
file content:
```java
offset: 1
uri: file://<WORKSPACE>/data/code-rep-dataset/Dataset5/Tasks/12457.java
text:
```scala
i@@f (null == jarFile || !jarFile.exists()) {

/*
 * Copyright  2000-2005 The Apache Software Foundation
 *
 *  Licensed under the Apache License, Version 2.0 (the "License");
 *  you may not use this file except in compliance with the License.
 *  You may obtain a copy of the License at
 *
 *      http://www.apache.org/licenses/LICENSE-2.0
 *
 *  Unless required by applicable law or agreed to in writing, software
 *  distributed under the License is distributed on an "AS IS" BASIS,
 *  WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 *  See the License for the specific language governing permissions and
 *  limitations under the License.
 *
 */

package org.apache.tools.ant.taskdefs;

import java.io.File;
import java.io.IOException;
import java.util.Vector;

import org.apache.tools.ant.BuildException;
import org.apache.tools.ant.DirectoryScanner;
import org.apache.tools.ant.Project;
import org.apache.tools.ant.taskdefs.condition.IsSigned;
import org.apache.tools.ant.types.FileSet;
import org.apache.tools.ant.util.FileUtils;
import org.apache.tools.ant.util.IdentityMapper;
import org.apache.tools.ant.util.FileNameMapper;

/**
 * Signs JAR or ZIP files with the javasign command line tool. The tool detailed
 * dependency checking: files are only signed if they are not signed. The
 * <tt>signjar</tt> attribute can point to the file to generate; if this file
 * exists then its modification date is used as a cue as to whether to resign
 * any JAR file.
 *
 * Timestamp driven signing is based on the unstable and inadequately documented
 * information in the Java1.5 docs
 * @see <a href="http://java.sun.com/j2se/1.5.0/docs/guide/security/time-of-signing-beta1.html">
 * beta documentation</a>
 * @ant.task category="java"
 * @since Ant 1.1
 */
public class SignJar extends AbstractJarSignerTask {

    private static final FileUtils FILE_UTILS = FileUtils.getFileUtils();

    /**
     * name to a signature file
     */
    protected String sigfile;

    /**
     * name of a single jar
     */
    protected File signedjar;

    /**
     * flag for internal sf signing
     */
    protected boolean internalsf;

    /**
     * sign sections only?
     */
    protected boolean sectionsonly;

    /**
     * flag to preserve timestamp on modified files
     */
    private boolean preserveLastModified;

    /**
     * Whether to assume a jar which has an appropriate .SF file in is already
     * signed.
     */
    protected boolean lazy;

    /**
     * the output directory when using filesets.
     */
    protected File destDir;

    /**
     * mapper for todir work
     */
    private FileNameMapper mapper;

    /**
     * URL for a tsa; null implies no tsa support
     */
    protected String tsaurl;

    /**
     * alias for the TSA in the keystore
     */
    protected String tsacert;

    /**
     * error string for unit test verification: {@value}
     */
    public static final String ERROR_TODIR_AND_SIGNEDJAR
            = "'destdir' and 'signedjar' cannot both be set";
    /**
     * error string for unit test verification: {@value}
     */
    public static final String ERROR_TOO_MANY_MAPPERS = "Too many mappers";
    /**
     * error string for unit test verification {@value}
     */
    public static final String ERROR_SIGNEDJAR_AND_FILESETS = "You cannot specify the signed JAR when using filesets";
    /**
     * error string for unit test verification: {@value}
     */
    public static final String ERROR_BAD_MAP = "Cannot map source file to anything sensible: ";
    /**
     * error string for unit test verification: {@value}
     */
    public static final String ERROR_MAPPER_WITHOUT_DEST = "The destDir attribute is required if a mapper is set";
    /**
     * error string for unit test verification: {@value}
     */
    public static final String ERROR_NO_ALIAS = "alias attribute must be set";
    /**
     * error string for unit test verification: {@value}
     */
    public static final String ERROR_NO_STOREPASS = "storepass attribute must be set";

    /**
     * name of .SF/.DSA file; optional
     *
     * @param sigfile the name of the .SF/.DSA file
     */
    public void setSigfile(final String sigfile) {
        this.sigfile = sigfile;
    }

    /**
     * name of signed JAR file; optional
     *
     * @param signedjar the name of the signed jar file
     */
    public void setSignedjar(final File signedjar) {
        this.signedjar = signedjar;
    }

    /**
     * Flag to include the .SF file inside the signature; optional; default
     * false
     *
     * @param internalsf if true include the .SF file inside the signature
     */
    public void setInternalsf(final boolean internalsf) {
        this.internalsf = internalsf;
    }

    /**
     * flag to compute hash of entire manifest; optional, default false
     *
     * @param sectionsonly flag to compute hash of entire manifest
     */
    public void setSectionsonly(final boolean sectionsonly) {
        this.sectionsonly = sectionsonly;
    }

    /**
     * flag to control whether the presence of a signature file means a JAR is
     * signed; optional, default false
     *
     * @param lazy flag to control whether the presence of a signature
     */
    public void setLazy(final boolean lazy) {
        this.lazy = lazy;
    }

    /**
     * Optionally sets the output directory to be used.
     *
     * @param destDir the directory in which to place signed jars
     * @since Ant 1.7
     */
    public void setDestDir(File destDir) {
        this.destDir = destDir;
    }


    /**
     * add a mapper to determine file naming policy. Only used with toDir
     * processing.
     *
     * @param newMapper
     * @since Ant 1.7
     */
    public void add(FileNameMapper newMapper) {
        if (mapper != null) {
            throw new BuildException(ERROR_TOO_MANY_MAPPERS);
        }
        mapper = newMapper;
    }

    /**
     * get the active mapper; may be null
     * @return mapper or null
     * @since Ant 1.7
     */
    public FileNameMapper getMapper() {
        return mapper;
    }

    /**
     * get the -tsaurl url
     * @return url or null
     * @since Ant 1.7
     */
    public String getTsaurl() {
        return tsaurl;
    }

    /**
     *
     * @param tsaurl
     * @since Ant 1.7
     */
    public void setTsaurl(String tsaurl) {
        this.tsaurl = tsaurl;
    }

    /**
     * get the -tsacert option
     * @since Ant 1.7
     * @return a certificate alias or null
     */
    public String getTsacert() {
        return tsacert;
    }

    /**
     * set the alias in the keystore of the TSA to use;
     * @param tsacert
     */
    public void setTsacert(String tsacert) {
        this.tsacert = tsacert;
    }

    /**
     * sign the jar(s)
     *
     * @throws BuildException on errors
     */
    public void execute() throws BuildException {
        //validation logic
        final boolean hasFileset = filesets.size() > 0;
        final boolean hasJar = jar != null;
        final boolean hasSignedJar = signedjar != null;
        final boolean hasDestDir = destDir != null;
        final boolean hasMapper = mapper != null;

        if (!hasJar && !hasFileset) {
            throw new BuildException(ERROR_NO_SOURCE);
        }
        if (null == alias) {
            throw new BuildException(ERROR_NO_ALIAS);
        }

        if (null == storepass) {
            throw new BuildException(ERROR_NO_STOREPASS);
        }

        if (hasDestDir && hasSignedJar) {
            throw new BuildException(ERROR_TODIR_AND_SIGNEDJAR);
        }


        if (hasFileset && hasSignedJar) {
            throw new BuildException(ERROR_SIGNEDJAR_AND_FILESETS);
        }

        //this isnt strictly needed, but by being fussy now,
        //we can change implementation details later
        if (!hasDestDir && hasMapper) {
            throw new BuildException(ERROR_MAPPER_WITHOUT_DEST);
        }

        beginExecution();


        try {
            //special case single jar handling with signedjar attribute set
            if (hasJar && hasSignedJar) {
                // single jar processing
                signOneJar(jar, signedjar);
                //return here.
                return;
            }

            //the rest of the method treats single jar like
            //a nested fileset with one file

            Vector sources = createUnifiedSources();
            //set up our mapping policy
            FileNameMapper destMapper;
            if (hasMapper) {
                destMapper = mapper;
            } else {
                //no mapper? use the identity policy
                destMapper = new IdentityMapper();
            }


            //at this point the filesets are set up with lists of files,
            //and the mapper is ready to map from source dirs to dest files
            //now we iterate through every JAR giving source and dest names
            // deal with the filesets
            for (int i = 0; i < sources.size(); i++) {
                FileSet fs = (FileSet) sources.elementAt(i);
                //get all included files in a fileset
                DirectoryScanner ds = fs.getDirectoryScanner(getProject());
                String[] jarFiles = ds.getIncludedFiles();
                File baseDir = fs.getDir(getProject());

                //calculate our destination directory; it is either the destDir
                //attribute, or the base dir of the fileset (for in situ updates)
                File toDir = hasDestDir ? destDir : baseDir;

                //loop through all jars in the fileset
                for (int j = 0; j < jarFiles.length; j++) {
                    String jarFile = jarFiles[j];
                    //determine the destination filename via the mapper
                    String[] destFilenames = destMapper.mapFileName(jarFile);
                    if (destFilenames == null || destFilenames.length != 1) {
                        //we only like simple mappers.
                        throw new BuildException(ERROR_BAD_MAP + jarFile);
                    }
                    File destFile = new File(toDir, destFilenames[0]);
                    File jarSource = new File(baseDir, jarFile);
                    signOneJar(jarSource, destFile);
                }
            }
        } finally {
            endExecution();
        }
    }

    /**
     * Sign one jar.
     * <p/>
     * The signing only takes place if {@link #isUpToDate(File, File)} indicates
     * that it is needed.
     *
     * @param jarSource source to sign
     * @param jarTarget target; may be null
     * @throws BuildException
     */
    private void signOneJar(File jarSource, File jarTarget)
            throws BuildException {


        File targetFile = jarTarget;
        if (targetFile == null) {
            targetFile = jarSource;
        }
        if (isUpToDate(jarSource, targetFile)) {
            return;
        }

        long lastModified = jarSource.lastModified();
        final ExecTask cmd = createJarSigner();

        setCommonOptions(cmd);

        bindToKeystore(cmd);
        if (null != sigfile) {
            addValue(cmd, "-sigfile");
            String value = this.sigfile;
            addValue(cmd, value);
        }

        //DO NOT SET THE -signedjar OPTION if source==dest
        //unless you like fielding hotspot crash reports
        if (null != targetFile && !jarSource.equals(targetFile)) {
            addValue(cmd, "-signedjar");
            addValue(cmd, targetFile.getPath());
        }

        if (internalsf) {
            addValue(cmd, "-internalsf");
        }

        if (sectionsonly) {
            addValue(cmd, "-sectionsonly");
        }

        //add -tsa operations if declared
        addTimestampAuthorityCommands(cmd);

        //JAR source is required
        addValue(cmd, jarSource.getPath());

        //alias is required for signing
        addValue(cmd, alias);

        log("Signing JAR: " +
                jarSource.getAbsolutePath()
                +" to " +
                targetFile.getAbsolutePath()
                + " as " + alias);

        cmd.execute();

        // restore the lastModified attribute
        if (preserveLastModified) {
            targetFile.setLastModified(lastModified);
        }
    }

    /**
     * If the tsa parameters are set, this passes them to the command.
     * There is no validation of java version, as third party JDKs
     * may implement this on earlier/later jarsigner implementations.
     * @param cmd
     */
    private void addTimestampAuthorityCommands(final ExecTask cmd) {
        if(tsaurl!=null) {
            addValue(cmd, "-tsa");
            addValue(cmd, tsaurl);
        }
        if (tsacert != null) {
            addValue(cmd, "-tsacert");
            addValue(cmd, tsacert);
        }
    }

    /**
     * Compare a jar file with its corresponding signed jar. The logic for this
     * is complex, and best explained in the source itself. Essentially if
     * either file doesnt exist, or the destfile has an out of date timestamp,
     * then the return value is false.
     * <p/>
     * If we are signing ourself, the check {@link #isSigned(File)} is used to
     * trigger the process.
     *
     * @param jarFile       the unsigned jar file
     * @param signedjarFile the result signed jar file
     * @return true if the signedjarFile is considered up to date
     */
    protected boolean isUpToDate(File jarFile, File signedjarFile) {
        if (null == jarFile && !jarFile.exists()) {
            //these are pathological cases, but retained in case somebody
            //subclassed us.
            return false;
        }

        //we normally compare destination with source
        File destFile = signedjarFile;
        if (destFile == null) {
            //but if no dest is specified, compare source to source
            destFile = jarFile;
        }

        //if, by any means, the destfile and source match,
        if (jarFile.equals(destFile)) {
            if (lazy) {
                //we check the presence of signatures on lazy signing
                return isSigned(jarFile);
            }
            //unsigned or non-lazy self signings are always false
            return false;
        }

        //if they are different, the timestamps are used
        return FILE_UTILS.isUpToDate(jarFile, destFile);
    }

    /**
     * test for a file being signed, by looking for a signature in the META-INF
     * directory with our alias.
     *
     * @param file the file to be checked
     * @return true if the file is signed
     * @see IsSigned#isSigned(File, String)
     */
    protected boolean isSigned(File file) {
        try {
            return IsSigned.isSigned(file, alias);
        } catch (IOException e) {
            //just log this
            log(e.toString(), Project.MSG_VERBOSE);
            return false;
        }
    }

    /**
     * true to indicate that the signed jar modification date remains the same
     * as the original. Defaults to false
     *
     * @param preserveLastModified if true preserve the last modified time
     */
    public void setPreserveLastModified(boolean preserveLastModified) {
        this.preserveLastModified = preserveLastModified;
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

QDox parse error in file://<WORKSPACE>/data/code-rep-dataset/Dataset5/Tasks/12457.java