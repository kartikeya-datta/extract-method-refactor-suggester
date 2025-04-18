error id: file://<WORKSPACE>/data/code-rep-dataset/Dataset5/Tasks/9098.java
file://<WORKSPACE>/data/code-rep-dataset/Dataset5/Tasks/9098.java
### com.thoughtworks.qdox.parser.ParseException: syntax error @[1,1]

error in qdox parser
file content:
```java
offset: 1
uri: file://<WORKSPACE>/data/code-rep-dataset/Dataset5/Tasks/9098.java
text:
```scala
l@@og("Exception occurred while trying to check/create "

/*
 * Copyright  2001-2004 The Apache Software Foundation
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
package org.apache.tools.ant.taskdefs.optional.i18n;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.OutputStreamWriter;
import java.util.Hashtable;
import java.util.Locale;
import java.util.Vector;
import org.apache.tools.ant.BuildException;
import org.apache.tools.ant.DirectoryScanner;
import org.apache.tools.ant.Project;
import org.apache.tools.ant.taskdefs.MatchingTask;
import org.apache.tools.ant.types.FileSet;
import org.apache.tools.ant.util.FileUtils;
import org.apache.tools.ant.util.LineTokenizer;

/**
 * Translates text embedded in files using Resource Bundle files.
 * Since ant 1.6 preserves line endings
 *
 * @author Magesh Umasankar, Don Brown
 * @author <a href="mailto:tom@tbee.org">Tom Eugelink</a>
 */
public class Translate extends MatchingTask {
    /**
     * search a bundle matching the specified language, the country and the variant
     */
    private static final int BUNDLE_SPECIFIED_LANGUAGE_COUNTRY_VARIANT = 0;
    /**
     * search a bundle matching the specified language, and the country
     */
    private static final int BUNDLE_SPECIFIED_LANGUAGE_COUNTRY = 1;
    /**
     * search a bundle matching the specified language only
     */
    private static final int BUNDLE_SPECIFIED_LANGUAGE = 2;
    /**
     * search a bundle matching nothing special
     */
    private static final int BUNDLE_NOMATCH = 3;
    /**
     * search a bundle matching the language, the country and the variant
     * of the current locale of the computer
     */
    private static final int BUNDLE_DEFAULT_LANGUAGE_COUNTRY_VARIANT = 4;
    /**
     * search a bundle matching the language, and the country
     * of the current locale of the computer
     */
    private static final int BUNDLE_DEFAULT_LANGUAGE_COUNTRY = 5;
    /**
     * search a bundle matching the language only
     * of the current locale of the computer
     */
    private static final int BUNDLE_DEFAULT_LANGUAGE = 6;
    /**
     * number of possibilities for the search
     */
     private static final int BUNDLE_MAX_ALTERNATIVES = BUNDLE_DEFAULT_LANGUAGE + 1;
    /**
     * Family name of resource bundle
     */
    private String bundle;

    /**
     * Locale specific language of the resource bundle
     */
    private String bundleLanguage;

    /**
     * Locale specific country of the resource bundle
     */
    private String bundleCountry;

    /**
     * Locale specific variant of the resource bundle
     */
    private String bundleVariant;

    /**
     * Destination directory
     */
    private File toDir;

    /**
     * Source file encoding scheme
     */
    private String srcEncoding;

    /**
     * Destination file encoding scheme
     */
    private String destEncoding;

    /**
     * Resource Bundle file encoding scheme, defaults to srcEncoding
     */
    private String bundleEncoding;

    /**
     * Starting token to identify keys
     */
    private String startToken;

    /**
     * Ending token to identify keys
     */
    private String endToken;

    /**
     * Whether or not to create a new destination file.
     * Defaults to <code>false</code>.
     */
    private boolean forceOverwrite;

    /**
     * Vector to hold source file sets.
     */
    private Vector filesets = new Vector();

    /**
     * Holds key value pairs loaded from resource bundle file
     */
    private Hashtable resourceMap = new Hashtable();
    /**

     * Used to resolve file names.
     */
    private FileUtils fileUtils = FileUtils.newFileUtils();

    /**
     * Last Modified Timestamp of resource bundle file being used.
     */
    private long[] bundleLastModified = new long[BUNDLE_MAX_ALTERNATIVES];

    /**
     * Last Modified Timestamp of source file being used.
     */
    private long srcLastModified;

    /**
     * Last Modified Timestamp of destination file being used.
     */
    private long destLastModified;

    /**
     * Has at least one file from the bundle been loaded?
     */
    private boolean loaded = false;

    /**
     * Sets Family name of resource bundle; required.
     * @param bundle family name of resource bundle
     */
    public void setBundle(String bundle) {
        this.bundle = bundle;
    }

    /**
     * Sets locale specific language of resource bundle; optional.
     * @param bundleLanguage langage of the bundle
     */
    public void setBundleLanguage(String bundleLanguage) {
        this.bundleLanguage = bundleLanguage;
    }

    /**
     * Sets locale specific country of resource bundle; optional.
     * @param bundleCountry country of the bundle
     */
    public void setBundleCountry(String bundleCountry) {
        this.bundleCountry = bundleCountry;
    }

    /**
     * Sets locale specific variant of resource bundle; optional.
     * @param bundleVariant locale variant of resource bundle
     */
    public void setBundleVariant(String bundleVariant) {
        this.bundleVariant = bundleVariant;
    }

    /**
     * Sets Destination directory; required.
     * @param toDir destination directory
     */
    public void setToDir(File toDir) {
        this.toDir = toDir;
    }

    /**
     * Sets starting token to identify keys; required.
     * @param startToken starting token to identify keys
     */
    public void setStartToken(String startToken) {
        this.startToken = startToken;
    }

    /**
     * Sets ending token to identify keys; required.
     * @param endToken ending token to identify keys
     */
    public void setEndToken(String endToken) {
        this.endToken = endToken;
    }

    /**
     * Sets source file encoding scheme; optional,
     * defaults to encoding of local system.
     * @param srcEncoding source file encoding
     */
    public void setSrcEncoding(String srcEncoding) {
        this.srcEncoding = srcEncoding;
    }

    /**
     * Sets destination file encoding scheme; optional.  Defaults to source file
     * encoding
     * @param destEncoding destination file encoding scheme
     */
    public void setDestEncoding(String destEncoding) {
        this.destEncoding = destEncoding;
    }

    /**
     * Sets Resource Bundle file encoding scheme; optional.  Defaults to source file
     * encoding
     * @param bundleEncoding bundle file encoding scheme
     */
    public void setBundleEncoding(String bundleEncoding) {
        this.bundleEncoding = bundleEncoding;
    }

    /**
     * Whether or not to overwrite existing file irrespective of
     * whether it is newer than the source file as well as the
     * resource bundle file.
     * Defaults to false.
     * @param forceOverwrite whether or not to overwrite existing files
     */
    public void setForceOverwrite(boolean forceOverwrite) {
        this.forceOverwrite = forceOverwrite;
    }

    /**
     * Adds a set of files to translate as a nested fileset element.
     * @param set the fileset to be added
     */
    public void addFileset(FileSet set) {
        filesets.addElement(set);
    }

    /**
     * Check attributes values, load resource map and translate
     * @throws BuildException if the required attributes are not set
     * Required : <ul>
     *       <li>bundle</li>
     *       <li>starttoken</li>
     *       <li>endtoken</li>
     *            </ul>
     */
    public void execute() throws BuildException {
        if (bundle == null) {
            throw new BuildException("The bundle attribute must be set.",
                                     getLocation());
        }

        if (startToken == null) {
            throw new BuildException("The starttoken attribute must be set.",
                                     getLocation());
        }

        if (endToken == null) {
            throw new BuildException("The endtoken attribute must be set.",
                                     getLocation());
        }

        if (bundleLanguage == null) {
            Locale l = Locale.getDefault();
            bundleLanguage  = l.getLanguage();
        }

        if (bundleCountry == null) {
            bundleCountry = Locale.getDefault().getCountry();
        }

        if (bundleVariant == null) {
            Locale l = new Locale(bundleLanguage, bundleCountry);
            bundleVariant = l.getVariant();
        }

        if (toDir == null) {
            throw new BuildException("The todir attribute must be set.",
                                     getLocation());
        }

        if (!toDir.exists()) {
            toDir.mkdirs();
        } else if (toDir.isFile()) {
            throw new BuildException(toDir + " is not a directory");
        }

        if (srcEncoding == null) {
            srcEncoding = System.getProperty("file.encoding");
        }

        if (destEncoding == null) {
            destEncoding = srcEncoding;
        }

        if (bundleEncoding == null) {
            bundleEncoding = srcEncoding;
        }

        loadResourceMaps();

        translate();
    }

    /**
     * Load resource maps based on resource bundle encoding scheme.
     * The resource bundle lookup searches for resource files with various
     * suffixes on the basis of (1) the desired locale and (2) the default
     * locale (basebundlename), in the following order from lower-level
     * (more specific) to parent-level (less specific):
     *
     * basebundlename + "_" + language1 + "_" + country1 + "_" + variant1
     * basebundlename + "_" + language1 + "_" + country1
     * basebundlename + "_" + language1
     * basebundlename
     * basebundlename + "_" + language2 + "_" + country2 + "_" + variant2
     * basebundlename + "_" + language2 + "_" + country2
     * basebundlename + "_" + language2
     *
     * To the generated name, a ".properties" string is appeneded and
     * once this file is located, it is treated just like a properties file
     * but with bundle encoding also considered while loading.
     */
    private void loadResourceMaps() throws BuildException {
        Locale locale = new Locale(bundleLanguage,
                                   bundleCountry,
                                   bundleVariant);
        String language = locale.getLanguage().length() > 0
            ? "_" + locale.getLanguage() : "";
        String country = locale.getCountry().length() > 0
            ? "_" + locale.getCountry() : "";
        String variant = locale.getVariant().length() > 0
            ? "_" + locale.getVariant() : "";
        String bundleFile = bundle + language + country + variant;
        processBundle(bundleFile, BUNDLE_SPECIFIED_LANGUAGE_COUNTRY_VARIANT, false);

        bundleFile = bundle + language + country;
        processBundle(bundleFile, BUNDLE_SPECIFIED_LANGUAGE_COUNTRY, false);

        bundleFile = bundle + language;
        processBundle(bundleFile, BUNDLE_SPECIFIED_LANGUAGE, false);

        bundleFile = bundle;
        processBundle(bundleFile, BUNDLE_NOMATCH, false);

        //Load default locale bundle files
        //using default file encoding scheme.
        locale = Locale.getDefault();

        language = locale.getLanguage().length() > 0
            ? "_" + locale.getLanguage() : "";
        country = locale.getCountry().length() > 0
            ? "_" + locale.getCountry() : "";
        variant = locale.getVariant().length() > 0
            ? "_" + locale.getVariant() : "";
        bundleEncoding = System.getProperty("file.encoding");

        bundleFile = bundle + language + country + variant;
        processBundle(bundleFile, BUNDLE_DEFAULT_LANGUAGE_COUNTRY_VARIANT, false);

        bundleFile = bundle + language + country;
        processBundle(bundleFile, BUNDLE_DEFAULT_LANGUAGE_COUNTRY, false);

        bundleFile = bundle + language;
        processBundle(bundleFile, BUNDLE_DEFAULT_LANGUAGE, true);
    }

    /**
     * Process each file that makes up this bundle.
     */
    private void processBundle(final String bundleFile, final int i,
                               final boolean checkLoaded) throws BuildException {
        final File propsFile = getProject().resolveFile(bundleFile + ".properties");
        FileInputStream ins = null;
        try {
            ins = new FileInputStream(propsFile);
            loaded = true;
            bundleLastModified[i] = propsFile.lastModified();
            log("Using " + propsFile, Project.MSG_DEBUG);
            loadResourceMap(ins);
        } catch (IOException ioe) {
            log(propsFile + " not found.", Project.MSG_DEBUG);
            //if all resource files associated with this bundle
            //have been scanned for and still not able to
            //find a single resrouce file, throw exception
            if (!loaded && checkLoaded) {
                throw new BuildException(ioe.getMessage(), getLocation());
            }
        }
    }

    /**
     * Load resourceMap with key value pairs.  Values of existing keys
     * are not overwritten.  Bundle's encoding scheme is used.
     */
    private void loadResourceMap(FileInputStream ins) throws BuildException {
        try {
            BufferedReader in = null;
            InputStreamReader isr = new InputStreamReader(ins, bundleEncoding);
            in = new BufferedReader(isr);
            String line = null;
            while ((line = in.readLine()) != null) {
                //So long as the line isn't empty and isn't a comment...
                if (line.trim().length() > 1 && '#' != line.charAt(0) && '!' != line.charAt(0)) {
                    //Legal Key-Value separators are :, = and white space.
                    int sepIndex = line.indexOf('=');
                    if (-1 == sepIndex) {
                        sepIndex = line.indexOf(':');
                    }
                    if (-1 == sepIndex) {
                        for (int k = 0; k < line.length(); k++) {
                            if (Character.isSpaceChar(line.charAt(k))) {
                                sepIndex = k;
                                break;
                            }
                        }
                    }
                    //Only if we do have a key is there going to be a value
                    if (-1 != sepIndex) {
                        String key = line.substring(0, sepIndex).trim();
                        String value = line.substring(sepIndex + 1).trim();
                        //Handle line continuations, if any
                        while (value.endsWith("\\")) {
                            value = value.substring(0, value.length() - 1);
                            if ((line = in.readLine()) != null) {
                                value = value + line.trim();
                            } else {
                                break;
                            }
                        }
                        if (key.length() > 0) {
                            //Has key already been loaded into resourceMap?
                            if (resourceMap.get(key) == null) {
                                resourceMap.put(key, value);
                            }
                        }
                    }
                }
            }
            if (in != null) {
                in.close();
            }
        } catch (IOException ioe) {
            throw new BuildException(ioe.getMessage(), getLocation());
        }
    }

    /**
     * Reads source file line by line using the source encoding and
     * searches for keys that are sandwiched between the startToken
     * and endToken.  The values for these keys are looked up from
     * the hashtable and substituted.  If the hashtable doesn't
     * contain the key, they key itself is used as the value.
     * Detination files and directories are created as needed.
     * The destination file is overwritten only if
     * the forceoverwritten attribute is set to true if
     * the source file or any associated bundle resource file is
     * newer than the destination file.
     */
    private void translate() throws BuildException {
        int filesProcessed = 0;
        for (int i = 0; i < filesets.size(); i++) {
            FileSet fs = (FileSet) filesets.elementAt(i);
            DirectoryScanner ds = fs.getDirectoryScanner(getProject());
            String[] srcFiles = ds.getIncludedFiles();
            for (int j = 0; j < srcFiles.length; j++) {
                try {
                    File dest = fileUtils.resolveFile(toDir, srcFiles[j]);
                    //Make sure parent dirs exist, else, create them.
                    try {
                        File destDir = new File(dest.getParent());
                        if (!destDir.exists()) {
                            destDir.mkdirs();
                        }
                    } catch (Exception e) {
                        log("Exception occured while trying to check/create "
                            + " parent directory.  " + e.getMessage(),
                            Project.MSG_DEBUG);
                    }
                    destLastModified = dest.lastModified();
                    File src = fileUtils.resolveFile(ds.getBasedir(), srcFiles[j]);
                    srcLastModified = src.lastModified();
                    //Check to see if dest file has to be recreated
                    boolean needsWork = forceOverwrite
 destLastModified < srcLastModified;
                    if (!needsWork) {
                        for (int icounter = 0; icounter < BUNDLE_MAX_ALTERNATIVES; icounter++) {
                            needsWork = (destLastModified < bundleLastModified[icounter]);
                            if (needsWork) {
                                break;
                            }
                        }
                    }
                    if (needsWork) {
                        log("Processing " + srcFiles[j],
                            Project.MSG_DEBUG);
                        FileOutputStream fos = new FileOutputStream(dest);
                        BufferedWriter out
                            = new BufferedWriter(new OutputStreamWriter(fos, destEncoding));
                        FileInputStream fis = new FileInputStream(src);
                        BufferedReader in
                            = new BufferedReader(new InputStreamReader(fis, srcEncoding));
                        String line;
                        LineTokenizer lineTokenizer = new LineTokenizer();
                        lineTokenizer.setIncludeDelims(true);
                        line = lineTokenizer.getToken(in);
                        while ((line) != null) {
                            // 2003-02-21 new replace algorithm by tbee (tbee@tbee.org)
                            // because it wasn't able to replace something like "@aaa;@bbb;"

                            // is there a startToken
                            // and there is still stuff following the startToken
                            int startIndex = line.indexOf(startToken);
                            while (startIndex >= 0
                                && (startIndex + startToken.length()) <= line.length()) {
                                // the new value, this needs to be here
                                // because it is required to calculate the next position to search from
                                // at the end of the loop
                                String replace = null;

                                // we found a starttoken, is there an endtoken following?
                                // start at token+tokenlength because start and end
                                // token may be indentical
                                int endIndex = line.indexOf(endToken, startIndex + startToken.length());
                                if (endIndex < 0) {
                                    startIndex += 1;
                                } else {
                                    // grab the token
                                    String token
                                        = line.substring(startIndex + startToken.length(), endIndex);

                                    // If there is a white space or = or :, then
                                    // it isn't to be treated as a valid key.
                                    boolean validToken = true;
                                    for (int k = 0; k < token.length() && validToken; k++) {
                                        char c = token.charAt(k);
                                        if (c == ':' || c == '='
 Character.isSpaceChar(c)) {
                                            validToken = false;
                                        }
                                    }
                                    if (!validToken) {
                                        startIndex += 1;
                                    } else {
                                        // find the replace string
                                        if (resourceMap.containsKey(token)) {
                                            replace = (String) resourceMap.get(token);
                                        } else {
                                            log("Replacement string missing for: "
                                                + token, Project.MSG_VERBOSE);
                                            replace = startToken + token + endToken;
                                        }


                                        // generate the new line
                                        line = line.substring(0, startIndex)
                                             + replace
                                             + line.substring(endIndex + endToken.length());

                                        // set start position for next search
                                        startIndex += replace.length();
                                    }
                                }

                                // find next starttoken
                                startIndex = line.indexOf(startToken, startIndex);
                            }
                            out.write(line);
                            line = lineTokenizer.getToken(in);
                        }
                        if (in != null) {
                            in.close();
                        }
                        if (out != null) {
                            out.close();
                        }
                        ++filesProcessed;
                    } else {
                        log("Skipping " + srcFiles[j]
                            + " as destination file is up to date",
                            Project.MSG_VERBOSE);
                    }
                } catch (IOException ioe) {
                    throw new BuildException(ioe.getMessage(), getLocation());
                }
            }
        }
        log("Translation performed on " + filesProcessed + " file(s).", Project.MSG_DEBUG);
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

QDox parse error in file://<WORKSPACE>/data/code-rep-dataset/Dataset5/Tasks/9098.java