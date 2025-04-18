error id: file://<WORKSPACE>/data/code-rep-dataset/Dataset5/Tasks/12472.java
file://<WORKSPACE>/data/code-rep-dataset/Dataset5/Tasks/12472.java
### com.thoughtworks.qdox.parser.ParseException: syntax error @[1,3]

error in qdox parser
file content:
```java
offset: 3
uri: file://<WORKSPACE>/data/code-rep-dataset/Dataset5/Tasks/12472.java
text:
```scala
+ d@@escriptorFileName

/*
 *  Licensed to the Apache Software Foundation (ASF) under one or more
 *  contributor license agreements.  See the NOTICE file distributed with
 *  this work for additional information regarding copyright ownership.
 *  The ASF licenses this file to You under the Apache License, Version 2.0
 *  (the "License"); you may not use this file except in compliance with
 *  the License.  You may obtain a copy of the License at
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
package org.apache.tools.ant.taskdefs.optional.ejb;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.util.Enumeration;
import java.util.HashSet;
import java.util.Hashtable;
import java.util.Iterator;
import java.util.Set;
import java.util.jar.JarOutputStream;
import java.util.jar.Manifest;
import java.util.zip.ZipEntry;

import javax.xml.parsers.SAXParser;

import org.apache.tools.ant.BuildException;
import org.apache.tools.ant.DirectoryScanner;
import org.apache.tools.ant.Location;
import org.apache.tools.ant.Project;
import org.apache.tools.ant.Task;
import org.apache.tools.ant.types.FileSet;
import org.apache.tools.ant.types.Path;
import org.apache.tools.ant.util.depend.DependencyAnalyzer;
import org.xml.sax.InputSource;
import org.xml.sax.SAXException;


/**
 * A deployment tool which creates generic EJB jars. Generic jars contains
 * only those classes and META-INF entries specified in the EJB 1.1 standard
 *
 * This class is also used as a framework for the creation of vendor specific
 * deployment tools. A number of template methods are provided through which the
 * vendor specific tool can hook into the EJB creation process.
 *
 */
public class GenericDeploymentTool implements EJBDeploymentTool {
    /** The default buffer byte size to use for IO */
    public static final int DEFAULT_BUFFER_SIZE = 1024;
    /** The level to use for compression */
    public static final int JAR_COMPRESS_LEVEL  = 9;

    /** The standard META-INF directory in jar files */
    protected static final String META_DIR  = "META-INF/";

    /** The standard MANIFEST file */
    protected static final String MANIFEST  = META_DIR + "MANIFEST.MF";

    /** Name for EJB Deployment descriptor within EJB jars */
    protected static final String EJB_DD    = "ejb-jar.xml";

    /** A dependency analyzer name to find ancestor classes */
    public static final String ANALYZER_SUPER = "super";
    /** A dependency analyzer name to find all related classes */
    public static final String ANALYZER_FULL = "full";
    /** A dependency analyzer name for no analyzer */
    public static final String ANALYZER_NONE = "none";

    /** The default analyzer */
    public static final String DEFAULT_ANALYZER = ANALYZER_SUPER;

    /** The analyzer class for the super analyzer */
    public static final String ANALYZER_CLASS_SUPER
        = "org.apache.tools.ant.util.depend.bcel.AncestorAnalyzer";
    /** The analyzer class for the super analyzer */
    public static final String ANALYZER_CLASS_FULL
        = "org.apache.tools.ant.util.depend.bcel.FullAnalyzer";

    /**
     * The configuration from the containing task. This config combined
     * with the settings of the individual attributes here constitues the
     * complete config for this deployment tool.
     */
    private EjbJar.Config config;

    /** Stores a handle to the directory to put the Jar files in */
    private File destDir;

    /** The classpath to use with this deployment tool. This is appended to
        any paths from the ejbjar task itself.*/
    private Path classpath;

    /** Instance variable that stores the suffix for the generated jarfile. */
    private String genericJarSuffix = "-generic.jar";

    /**
     * The task to which this tool belongs. This is used to access services
     * provided by the ant core, such as logging.
     */
    private Task task;

    /**
     * The classloader generated from the given classpath to load
     * the super classes and super interfaces.
     */
    private ClassLoader classpathLoader = null;

     /**
     * Set of files have been loaded into the EJB jar
     */
    private Set addedfiles;

    /**
     * Handler used to parse the EJB XML descriptor
     */
    private DescriptorHandler handler;

    /**
     * Dependency analyzer used to collect class dependencies
     */
    private DependencyAnalyzer dependencyAnalyzer;

    /** No arg constructor */
    public GenericDeploymentTool() {
    }


    /**
     * Set the destination directory; required.
     * @param inDir the destination directory.
     */
    public void setDestdir(File inDir) {
        this.destDir = inDir;
    }

    /**
     * Get the destination directory.
     *
     * @return the destination directory into which EJB jars are to be written
     */
    protected File getDestDir() {
        return destDir;
    }


    /**
     * Set the task which owns this tool
     *
     * @param task the Task to which this deployment tool is associated.
     */
    public void setTask(Task task) {
        this.task = task;
    }

    /**
     * Get the task for this tool.
     *
     * @return the Task instance this tool is associated with.
     */
    protected Task getTask() {
        return task;
    }

    /**
     * Get the basename terminator.
     *
     * @return an ejbjar task configuration
     */
    protected EjbJar.Config getConfig() {
        return config;
    }

    /**
     * Indicate if this build is using the base jar name.
     *
     * @return true if the name of the generated jar is coming from the
     *              basejarname attribute
     */
    protected boolean usingBaseJarName() {
        return config.baseJarName != null;
    }

    /**
     * Set the suffix for the generated jar file.
     * @param inString the string to use as the suffix.
     */
    public void setGenericJarSuffix(String inString) {
        this.genericJarSuffix = inString;
    }

    /**
     * Add the classpath for the user classes
     *
     * @return a Path instance to be configured by Ant.
     */
    public Path createClasspath() {
        if (classpath == null) {
            classpath = new Path(task.getProject());
        }
        return classpath.createPath();
    }

    /**
     * Set the classpath to be used for this compilation.
     *
     * @param classpath the classpath to be used for this build.
     */
    public void setClasspath(Path classpath) {
        this.classpath = classpath;
    }

    /**
     * Get the classpath by combining the one from the surrounding task, if any
     * and the one from this tool.
     *
     * @return the combined classpath
     */
    protected Path getCombinedClasspath() {
        Path combinedPath = classpath;
        if (config.classpath != null) {
            if (combinedPath == null) {
                combinedPath = config.classpath;
            } else {
                combinedPath.append(config.classpath);
            }
        }

        return combinedPath;
    }

    /**
     * Log a message to the Ant output.
     *
     * @param message the message to be logged.
     * @param level the severity of this message.
     */
    protected void log(String message, int level) {
        getTask().log(message, level);
    }

    /**
     * Get the build file location associated with this element's task.
     *
     * @return the task's location instance.
     */
    protected Location getLocation() {
        return getTask().getLocation();
    }

    private void createAnalyzer() {
        String analyzer = config.analyzer;
        if (analyzer == null) {
            analyzer = DEFAULT_ANALYZER;
        }

        if (analyzer.equals(ANALYZER_NONE)) {
            return;
        }

        String analyzerClassName = null;
        if (analyzer.equals(ANALYZER_SUPER)) {
            analyzerClassName = ANALYZER_CLASS_SUPER;
        } else if (analyzer.equals(ANALYZER_FULL)) {
            analyzerClassName = ANALYZER_CLASS_FULL;
        } else {
            analyzerClassName = analyzer;
        }

        try {
            Class analyzerClass = Class.forName(analyzerClassName);
            dependencyAnalyzer
                = (DependencyAnalyzer) analyzerClass.newInstance();
            dependencyAnalyzer.addClassPath(new Path(task.getProject(),
                config.srcDir.getPath()));
            dependencyAnalyzer.addClassPath(config.classpath);
        } catch (NoClassDefFoundError e) {
            dependencyAnalyzer = null;
            task.log("Unable to load dependency analyzer: " + analyzerClassName
                + " - dependent class not found: " + e.getMessage(),
                Project.MSG_WARN);
        } catch (Exception e) {
            dependencyAnalyzer = null;
            task.log("Unable to load dependency analyzer: " + analyzerClassName
                     + " - exception: " + e.getMessage(),
                Project.MSG_WARN);
        }
    }


    /**
     * Configure this tool for use in the ejbjar task.
     *
     * @param config the configuration from the surrounding ejbjar task.
     */
    public void configure(EjbJar.Config config) {
        this.config = config;

        createAnalyzer();
        classpathLoader = null;
    }

    /**
     * Utility method that encapsulates the logic of adding a file entry to
     * a .jar file.  Used by execute() to add entries to the jar file as it is
     * constructed.
     * @param jStream A JarOutputStream into which to write the
     *        jar entry.
     * @param inputFile A File from which to read the
     *        contents the file being added.
     * @param logicalFilename A String representing the name, including
     *        all relevant path information, that should be stored for the entry
     *        being added.
     * @throws BuildException if there is a problem.
     */
    protected void addFileToJar(JarOutputStream jStream,
                                File inputFile,
                                String logicalFilename)
        throws BuildException {
        FileInputStream iStream = null;
        try {
            if (!addedfiles.contains(logicalFilename)) {
                iStream = new FileInputStream(inputFile);
                // Create the zip entry and add it to the jar file
                ZipEntry zipEntry = new ZipEntry(logicalFilename.replace('\\', '/'));
                jStream.putNextEntry(zipEntry);

                // Create the file input stream, and buffer everything over
                // to the jar output stream
                byte[] byteBuffer = new byte[2 * DEFAULT_BUFFER_SIZE];
                int count = 0;
                do {
                    jStream.write(byteBuffer, 0, count);
                    count = iStream.read(byteBuffer, 0, byteBuffer.length);
                } while (count != -1);

                //add it to list of files in jar
                addedfiles.add(logicalFilename);
           }
        } catch (IOException ioe) {
            log("WARNING: IOException while adding entry "
                + logicalFilename + " to jarfile from "
                + inputFile.getPath() + " "  + ioe.getClass().getName()
                + "-" + ioe.getMessage(), Project.MSG_WARN);
        } finally {
            // Close up the file input stream for the class file
            if (iStream != null) {
                try {
                    iStream.close();
                } catch (IOException closeException) {
                    // ignore
                }
            }
        }
    }

    /**
     * Get a descriptionHandler.
     * @param srcDir the source directory.
     * @return a handler.
     */
    protected DescriptorHandler getDescriptorHandler(File srcDir) {
        DescriptorHandler h = new DescriptorHandler(getTask(), srcDir);

        registerKnownDTDs(h);

        // register any DTDs supplied by the user
        for (Iterator i = getConfig().dtdLocations.iterator(); i.hasNext();) {
            EjbJar.DTDLocation dtdLocation = (EjbJar.DTDLocation) i.next();
            h.registerDTD(dtdLocation.getPublicId(), dtdLocation.getLocation());
        }
        return h;
    }

    /**
     * Register the locations of all known DTDs.
     *
     * vendor-specific subclasses should override this method to define
     * the vendor-specific locations of the EJB DTDs
     * @param handler no used in this class.
     */
    protected void registerKnownDTDs(DescriptorHandler handler) {
        // none to register for generic
    }

    /** {@inheritDoc}. */
    public void processDescriptor(String descriptorFileName, SAXParser saxParser) {

        checkConfiguration(descriptorFileName, saxParser);

        try {
            handler = getDescriptorHandler(config.srcDir);

            // Retrive the files to be added to JAR from EJB descriptor
            Hashtable ejbFiles = parseEjbFiles(descriptorFileName, saxParser);

            // Add any support classes specified in the build file
            addSupportClasses(ejbFiles);

            // Determine the JAR filename (without filename extension)
            String baseName = getJarBaseName(descriptorFileName);

            String ddPrefix = getVendorDDPrefix(baseName, descriptorFileName);

            File manifestFile = getManifestFile(ddPrefix);
            if (manifestFile != null) {
                ejbFiles.put(MANIFEST, manifestFile);
            }



            // First the regular deployment descriptor
            ejbFiles.put(META_DIR + EJB_DD,
                         new File(config.descriptorDir, descriptorFileName));

            // now the vendor specific files, if any
            addVendorFiles(ejbFiles, ddPrefix);

            // add any dependent files
            checkAndAddDependants(ejbFiles);

            // Lastly create File object for the Jar files. If we are using
            // a flat destination dir, then we need to redefine baseName!
            if (config.flatDestDir && baseName.length() != 0) {
                int startName = baseName.lastIndexOf(File.separator);
                if (startName == -1) {
                    startName = 0;
                }

                int endName   = baseName.length();
                baseName = baseName.substring(startName, endName);
            }

            File jarFile = getVendorOutputJarFile(baseName);


            // Check to see if we need a build and start doing the work!
            if (needToRebuild(ejbFiles, jarFile)) {
                // Log that we are going to build...
                log("building "
                              + jarFile.getName()
                              + " with "
                              + String.valueOf(ejbFiles.size())
                              + " files",
                              Project.MSG_INFO);

                // Use helper method to write the jarfile
                String publicId = getPublicId();
                writeJar(baseName, jarFile, ejbFiles, publicId);

            } else {
                // Log that the file is up to date...
                log(jarFile.toString() + " is up to date.",
                              Project.MSG_VERBOSE);
            }

        } catch (SAXException se) {
            String msg = "SAXException while parsing '"
                + descriptorFileName
                + "'. This probably indicates badly-formed XML."
                + "  Details: "
                + se.getMessage();
            throw new BuildException(msg, se);
        } catch (IOException ioe) {
            String msg = "IOException while parsing'"
                + descriptorFileName.toString()
                + "'.  This probably indicates that the descriptor"
                + " doesn't exist. Details: "
                + ioe.getMessage();
            throw new BuildException(msg, ioe);
        }
    }

    /**
     * This method is called as the first step in the processDescriptor method
     * to allow vendor-specific subclasses to validate the task configuration
     * prior to processing the descriptor.  If the configuration is invalid,
     * a BuildException should be thrown.
     *
     * @param descriptorFileName String representing the file name of an EJB
     *                           descriptor to be processed
     * @param saxParser          SAXParser which may be used to parse the XML
     *                           descriptor
     * @throws BuildException if there is a problem.
     */
    protected void checkConfiguration(String descriptorFileName,
                                    SAXParser saxParser) throws BuildException {

        /*
         * For the GenericDeploymentTool, do nothing.  Vendor specific
         * subclasses should throw a BuildException if the configuration is
         * invalid for their server.
         */
    }

    /**
     * This method returns a list of EJB files found when the specified EJB
     * descriptor is parsed and processed.
     *
     * @param descriptorFileName String representing the file name of an EJB
     *                           descriptor to be processed
     * @param saxParser          SAXParser which may be used to parse the XML
     *                           descriptor
     * @return                   Hashtable of EJB class (and other) files to be
     *                           added to the completed JAR file
     * @throws SAXException      Any SAX exception, possibly wrapping another
     *                           exception
     * @throws IOException       An IOException from the parser, possibly from a
     *                           the byte stream or character stream
     */
    protected Hashtable parseEjbFiles(String descriptorFileName, SAXParser saxParser)
                            throws IOException, SAXException {
        FileInputStream descriptorStream = null;
        Hashtable ejbFiles = null;

        try {

            /* Parse the ejb deployment descriptor.  While it may not
             * look like much, we use a SAXParser and an inner class to
             * get hold of all the classfile names for the descriptor.
             */
            descriptorStream
                = new FileInputStream(new File(config.descriptorDir, descriptorFileName));
            saxParser.parse(new InputSource(descriptorStream), handler);

            ejbFiles = handler.getFiles();

        } finally {
            if (descriptorStream != null) {
                try {
                    descriptorStream.close();
                } catch (IOException closeException) {
                    // ignore
                }
            }
        }

        return ejbFiles;
    }

    /**
     * Adds any classes the user specifies using <i>support</i> nested elements
     * to the <code>ejbFiles</code> Hashtable.
     *
     * @param ejbFiles Hashtable of EJB classes (and other) files that will be
     *                 added to the completed JAR file
     */
    protected void addSupportClasses(Hashtable ejbFiles) {
        // add in support classes if any
        Project project = task.getProject();
        for (Iterator i = config.supportFileSets.iterator(); i.hasNext();) {
            FileSet supportFileSet = (FileSet) i.next();
            File supportBaseDir = supportFileSet.getDir(project);
            DirectoryScanner supportScanner = supportFileSet.getDirectoryScanner(project);
            supportScanner.scan();
            String[] supportFiles = supportScanner.getIncludedFiles();
            for (int j = 0; j < supportFiles.length; ++j) {
                ejbFiles.put(supportFiles[j], new File(supportBaseDir, supportFiles[j]));
            }
        }
    }


    /**
     * Using the EJB descriptor file name passed from the <code>ejbjar</code>
     * task, this method returns the "basename" which will be used to name the
     * completed JAR file.
     *
     * @param descriptorFileName String representing the file name of an EJB
     *                           descriptor to be processed
     * @return                   The "basename" which will be used to name the
     *                           completed JAR file
     */
    protected String getJarBaseName(String descriptorFileName) {

        String baseName = "";

        // Work out what the base name is
        if (config.namingScheme.getValue().equals(EjbJar.NamingScheme.BASEJARNAME)) {
            String canonicalDescriptor = descriptorFileName.replace('\\', '/');
            int index = canonicalDescriptor.lastIndexOf('/');
            if (index != -1) {
                baseName = descriptorFileName.substring(0, index + 1);
            }
            baseName += config.baseJarName;
        } else if (config.namingScheme.getValue().equals(EjbJar.NamingScheme.DESCRIPTOR)) {
            int lastSeparatorIndex = descriptorFileName.lastIndexOf(File.separator);
            int endBaseName = -1;
            if (lastSeparatorIndex != -1) {
                endBaseName = descriptorFileName.indexOf(config.baseNameTerminator,
                                                            lastSeparatorIndex);
            } else {
                endBaseName = descriptorFileName.indexOf(config.baseNameTerminator);
            }

            if (endBaseName != -1) {
                baseName = descriptorFileName.substring(0, endBaseName);
            } else {
                throw new BuildException("Unable to determine jar name "
                    + "from descriptor \"" + descriptorFileName + "\"");
            }
        } else if (config.namingScheme.getValue().equals(EjbJar.NamingScheme.DIRECTORY)) {
            File descriptorFile = new File(config.descriptorDir, descriptorFileName);
            String path = descriptorFile.getAbsolutePath();
            int lastSeparatorIndex
                = path.lastIndexOf(File.separator);
            if (lastSeparatorIndex == -1) {
                throw new BuildException("Unable to determine directory name holding descriptor");
            }
            String dirName = path.substring(0, lastSeparatorIndex);
            int dirSeparatorIndex = dirName.lastIndexOf(File.separator);
            if (dirSeparatorIndex != -1) {
                dirName = dirName.substring(dirSeparatorIndex + 1);
            }

            baseName = dirName;
        } else if (config.namingScheme.getValue().equals(EjbJar.NamingScheme.EJB_NAME)) {
            baseName = handler.getEjbName();
        }
        return baseName;
    }

    /**
     * Get the prefix for vendor deployment descriptors.
     *
     * This will contain the path and the start of the descriptor name,
     * depending on the naming scheme
     * @param baseName the base name to use.
     * @param descriptorFileName the file name to use.
     * @return the prefix.
     */
    public String getVendorDDPrefix(String baseName, String descriptorFileName) {
        String ddPrefix = null;

        if (config.namingScheme.getValue().equals(EjbJar.NamingScheme.DESCRIPTOR)) {
            ddPrefix = baseName + config.baseNameTerminator;
        } else if (config.namingScheme.getValue().equals(EjbJar.NamingScheme.BASEJARNAME)
 config.namingScheme.getValue().equals(EjbJar.NamingScheme.EJB_NAME)
 config.namingScheme.getValue().equals(EjbJar.NamingScheme.DIRECTORY)) {
            String canonicalDescriptor = descriptorFileName.replace('\\', '/');
            int index = canonicalDescriptor.lastIndexOf('/');
            if (index == -1) {
                ddPrefix = "";
            } else {
                ddPrefix = descriptorFileName.substring(0, index + 1);
            }
        }
        return ddPrefix;
    }

    /**
     * Add any vendor specific files which should be included in the
     * EJB Jar.
     * @param ejbFiles a hashtable entryname -> file.
     * @param ddPrefix a prefix to use.
     */
    protected void addVendorFiles(Hashtable ejbFiles, String ddPrefix) {
        // nothing to add for generic tool.
    }


    /**
     * Get the vendor specific name of the Jar that will be output. The modification date
     * of this jar will be checked against the dependent bean classes.
     * @param baseName the basename to use.
     */
    File getVendorOutputJarFile(String baseName) {
        return new File(destDir, baseName + genericJarSuffix);
    }

    /**
     * This method checks the timestamp on each file listed in the <code>
     * ejbFiles</code> and compares them to the timestamp on the <code>jarFile
     * </code>.  If the <code>jarFile</code>'s timestamp is more recent than
     * each EJB file, <code>true</code> is returned.  Otherwise, <code>false
     * </code> is returned.
     * TODO: find a way to check the manifest-file, that is found by naming convention
     *
     * @param ejbFiles Hashtable of EJB classes (and other) files that will be
     *                 added to the completed JAR file
     * @param jarFile  JAR file which will contain all of the EJB classes (and
     *                 other) files
     * @return         boolean indicating whether or not the <code>jarFile</code>
     *                 is up to date
     */
    protected boolean needToRebuild(Hashtable ejbFiles, File jarFile) {
        if (jarFile.exists()) {
            long lastBuild = jarFile.lastModified();

            Iterator fileIter = ejbFiles.values().iterator();

            // Loop through the files seeing if any has been touched
            // more recently than the destination jar.
            while (fileIter.hasNext()) {
                File currentFile = (File) fileIter.next();
                if (lastBuild < currentFile.lastModified()) {
                    log("Build needed because " + currentFile.getPath() + " is out of date",
                        Project.MSG_VERBOSE);
                    return true;
                }
            }
            return false;
        }

        return true;
    }

    /**
     * Returns the Public ID of the DTD specified in the EJB descriptor.  Not
     * every vendor-specific <code>DeploymentTool</code> will need to reference
     * this value or may want to determine this value in a vendor-specific way.
     *
     * @return Public ID of the DTD specified in the EJB descriptor.
     */
    protected String getPublicId() {
        return handler.getPublicId();
    }

    /**
     * Get the manifets file to use for building the generic jar.
     *
     * If the file does not exist the global manifest from the config is used
     * otherwise the default Ant manifest will be used.
     *
     * @param prefix the prefix where to llook for the manifest file based on
     *        the naming convention.
     *
     * @return the manifest file or null if the manifest file does not exist
     */
    protected File getManifestFile(String prefix) {
        File manifestFile
            = new File(getConfig().descriptorDir, prefix + "manifest.mf");
        if (manifestFile.exists()) {
            return manifestFile;
        }

        if (config.manifest != null) {
            return config.manifest;
        }
        return null;
    }

    /**
     * Method used to encapsulate the writing of the JAR file. Iterates over the
     * filenames/java.io.Files in the Hashtable stored on the instance variable
     * ejbFiles.
     * @param baseName the base name to use.
     * @param jarfile  the jar file to write to.
     * @param files    the files to write to the jar.
     * @param publicId the id to use.
     * @throws BuildException if there is a problem.
     */
    protected void writeJar(String baseName, File jarfile, Hashtable files,
                            String publicId) throws BuildException {

        JarOutputStream jarStream = null;
        try {
            // clean the addedfiles set
            if (addedfiles == null) {
                addedfiles = new HashSet();
            } else {
                addedfiles.clear();
            }

            /* If the jarfile already exists then whack it and recreate it.
             * Should probably think of a more elegant way to handle this
             * so that in case of errors we don't leave people worse off
             * than when we started =)
             */
            if (jarfile.exists()) {
                jarfile.delete();
            }
            jarfile.getParentFile().mkdirs();
            jarfile.createNewFile();

            InputStream in = null;
            Manifest manifest = null;
            try {
                File manifestFile = (File) files.get(MANIFEST);
                if (manifestFile != null && manifestFile.exists()) {
                    in = new FileInputStream(manifestFile);
                } else {
                    String defaultManifest = "/org/apache/tools/ant/defaultManifest.mf";
                    in = this.getClass().getResourceAsStream(defaultManifest);
                    if (in == null) {
                        throw new BuildException("Could not find "
                            + "default manifest: " + defaultManifest);
                    }
                }

                manifest = new Manifest(in);
            } catch (IOException e) {
                throw new BuildException ("Unable to read manifest", e, getLocation());
            } finally {
                if (in != null) {
                    in.close();
                }
            }

            // Create the streams necessary to write the jarfile

            jarStream = new JarOutputStream(new FileOutputStream(jarfile), manifest);
            jarStream.setMethod(JarOutputStream.DEFLATED);

            // Loop through all the class files found and add them to the jar
            for (Iterator entryIterator = files.keySet().iterator(); entryIterator.hasNext();) {
                String entryName = (String) entryIterator.next();
                if (entryName.equals(MANIFEST)) {
                    continue;
                }

                File entryFile = (File) files.get(entryName);

                log("adding file '" + entryName + "'",
                              Project.MSG_VERBOSE);

                addFileToJar(jarStream, entryFile, entryName);

                // See if there are any inner classes for this class and add them in if there are
                InnerClassFilenameFilter flt = new InnerClassFilenameFilter(entryFile.getName());
                File entryDir = entryFile.getParentFile();
                String[] innerfiles = entryDir.list(flt);
                if (innerfiles != null) {
                    for (int i = 0, n = innerfiles.length; i < n; i++) {

                        //get and clean up innerclass name
                        int entryIndex = entryName.lastIndexOf(entryFile.getName()) - 1;
                        if (entryIndex < 0) {
                            entryName = innerfiles[i];
                        } else {
                            entryName = entryName.substring(0, entryIndex)
                                + File.separatorChar + innerfiles[i];
                        }
                        // link the file
                        entryFile = new File(config.srcDir, entryName);

                        log("adding innerclass file '" + entryName + "'",
                                Project.MSG_VERBOSE);

                        addFileToJar(jarStream, entryFile, entryName);

                    }
                }
            }
        } catch (IOException ioe) {
            String msg = "IOException while processing ejb-jar file '"
                + jarfile.toString()
                + "'. Details: "
                + ioe.getMessage();
            throw new BuildException(msg, ioe);
        } finally {
            if (jarStream != null) {
                try {
                    jarStream.close();
                } catch (IOException closeException) {
                    // ignore
                }
            }
        }
    } // end of writeJar


    /**
     * Add all available classes, that depend on Remote, Home, Bean, PK
     * @param checkEntries files, that are extracted from the deployment descriptor
     * @throws BuildException if there is a problem.
     */
    protected void checkAndAddDependants(Hashtable checkEntries)
        throws BuildException {

        if (dependencyAnalyzer == null) {
            return;
        }

        dependencyAnalyzer.reset();

        Iterator i = checkEntries.keySet().iterator();
        while (i.hasNext()) {
            String entryName = (String) i.next();
            if (entryName.endsWith(".class")) {
                String className = entryName.substring(0,
                    entryName.length() - ".class".length());
                className = className.replace(File.separatorChar, '/');
                className = className.replace('/', '.');

                dependencyAnalyzer.addRootClass(className);
            }
        }

        Enumeration e = dependencyAnalyzer.getClassDependencies();

        while (e.hasMoreElements()) {
            String classname = (String) e.nextElement();
            String location
                = classname.replace('.', File.separatorChar) + ".class";
            File classFile = new File(config.srcDir, location);
            if (classFile.exists()) {
                checkEntries.put(location, classFile);
                log("dependent class: " + classname + " - " + classFile,
                    Project.MSG_VERBOSE);
            }
        }
    }


    /**
     * Returns a Classloader object which parses the passed in generic EjbJar classpath.
     * The loader is used to dynamically load classes from javax.ejb.* and the classes
     * being added to the jar.
     * @return a classloader.
     */
    protected ClassLoader getClassLoaderForBuild() {
        if (classpathLoader != null) {
            return classpathLoader;
        }

        Path combinedClasspath = getCombinedClasspath();

        // only generate a new ClassLoader if we have a classpath
        if (combinedClasspath == null) {
            classpathLoader = getClass().getClassLoader();
        } else {
            classpathLoader
                = getTask().getProject().createClassLoader(combinedClasspath);
        }

        return classpathLoader;
    }

    /**
     * Called to validate that the tool parameters have been configured.
     *
     * @throws BuildException If the Deployment Tool's configuration isn't
     *                        valid
     */
    public void validateConfigured() throws BuildException {
        if ((destDir == null) || (!destDir.isDirectory())) {
            String msg = "A valid destination directory must be specified "
                            + "using the \"destdir\" attribute.";
            throw new BuildException(msg, getLocation());
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

QDox parse error in file://<WORKSPACE>/data/code-rep-dataset/Dataset5/Tasks/12472.java