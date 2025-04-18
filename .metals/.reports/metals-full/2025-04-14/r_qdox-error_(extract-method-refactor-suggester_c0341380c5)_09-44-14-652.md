error id: file://<WORKSPACE>/data/code-rep-dataset/Dataset5/Tasks/4487.java
file://<WORKSPACE>/data/code-rep-dataset/Dataset5/Tasks/4487.java
### com.thoughtworks.qdox.parser.ParseException: syntax error @[1,1]

error in qdox parser
file content:
```java
offset: 1
uri: file://<WORKSPACE>/data/code-rep-dataset/Dataset5/Tasks/4487.java
text:
```scala
c@@ommandline.createArgument().setLine(java2iioparams);

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


import java.io.BufferedReader;
import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.OutputStream;
import java.util.Hashtable;
import java.util.Iterator;
import java.util.Vector;
import org.apache.tools.ant.BuildException;
import org.apache.tools.ant.Project;
import org.apache.tools.ant.taskdefs.ExecTask;
import org.apache.tools.ant.taskdefs.Execute;
import org.apache.tools.ant.taskdefs.ExecuteStreamHandler;
import org.apache.tools.ant.taskdefs.Java;
import org.apache.tools.ant.types.Commandline;
import org.apache.tools.ant.types.Path;


/**
 * BorlandDeploymentTool is dedicated to the Borland Application Server 4.5 and 4.5.1
 * This task generates and compiles the stubs and skeletons for all ejb described into the
 * Deployment Descriptor, builds the jar file including the support files and verify
 * whether the produced jar is valid or not.
 * The supported options are:
 * <ul>
 * <li>debug  (boolean)    : turn on the debug mode for generation of
 *                           stubs and skeletons (default:false)</li>
 * <li>verify (boolean)    : turn on the verification at the end of the jar
 *                           production  (default:true) </li>
 * <li>verifyargs (String) : add optional argument to verify command
 *                           (see vbj com.inprise.ejb.util.Verify)</li>
 * <li>basdtd (String)     : location of the BAS DTD </li>
 * <li>generateclient (boolean) : turn on the client jar file generation </li>
 * <li>version (int)       : tell what is the Borland appserver version 4 or 5 </li>
 * </ul>
 *
 *<PRE>
 *
 *      &lt;ejbjar srcdir=&quot;${build.classes}&quot;
 *               basejarname=&quot;vsmp&quot;
 *               descriptordir=&quot;${rsc.dir}/hrmanager&quot;&gt;
 *        &lt;borland destdir=&quot;tstlib&quot;&gt;
 *          &lt;classpath refid=&quot;classpath&quot; /&gt;
 *        &lt;/borland&gt;
 *        &lt;include name=&quot;**\ejb-jar.xml&quot;/&gt;
 *        &lt;support dir=&quot;${build.classes}&quot;&gt;
 *          &lt;include name=&quot;demo\smp\*.class&quot;/&gt;
 *          &lt;include name=&quot;demo\helper\*.class&quot;/&gt;
 *         &lt;/support&gt;
 *     &lt;/ejbjar&gt;
 *</PRE>
 *
 */
public class BorlandDeploymentTool extends GenericDeploymentTool
                                   implements ExecuteStreamHandler {
    /** Borland 1.1 ejb id */
    public static final String PUBLICID_BORLAND_EJB
    = "-//Inprise Corporation//DTD Enterprise JavaBeans 1.1//EN";

    protected static final String DEFAULT_BAS45_EJB11_DTD_LOCATION
    = "/com/inprise/j2ee/xml/dtds/ejb-jar.dtd";

    protected static final String DEFAULT_BAS_DTD_LOCATION
    = "/com/inprise/j2ee/xml/dtds/ejb-inprise.dtd";

    protected static final String BAS_DD = "ejb-inprise.xml";
    protected static final String BES_DD = "ejb-borland.xml";


    /** Java2iiop executable **/
    protected static final String JAVA2IIOP = "java2iiop";

    /** Verify class */
    protected static final String VERIFY = "com.inprise.ejb.util.Verify";

    /** Instance variable that stores the suffix for the borland jarfile. */
    private String jarSuffix = "-ejb.jar";

    /** Instance variable that stores the location of the borland DTD file. */
    private String borlandDTD;

    /** Instance variable that determines whether the debug mode is on */
    private boolean java2iiopdebug = false;

    /** store additional param for java2iiop command used to build EJB Stubs */
    private String java2iioparams = null;

    /** Instance variable that determines whether the client jar file is generated */
    private boolean generateclient = false;

    /** Borland Enterprise Server = version 5 */
    static final int    BES       = 5;
    /** Borland Application Server or Inprise Application Server  = version 4 */
    static final int    BAS       = 4;

    /** borland appserver version 4 or 5 */
    private int version = BAS;


    /**
     * Instance variable that determines whether it is necessary to verify the
     * produced jar
     */
    private boolean verify     = true;
    private String  verifyArgs = "";

    private Hashtable genfiles = new Hashtable();

    /**
     * set the debug mode for java2iiop (default false)
     * @param debug the setting to use.
     **/
    public void setDebug(boolean debug) {
        this.java2iiopdebug = debug;
    }

    /**
     * set the verify  mode for the produced jar (default true)
     * @param verify the setting to use.
     **/
    public void setVerify(boolean verify) {
        this.verify = verify;
    }


    /**
     * Setter used to store the suffix for the generated borland jar file.
     * @param inString the string to use as the suffix.
     */
    public void setSuffix(String inString) {
        this.jarSuffix = inString;
    }


    /**
     * sets some additional args to send to verify command
     * @param args additional command line parameters
     */
    public void setVerifyArgs(String args) {
        this.verifyArgs = args;
    }

    /**
     * Setter used to store the location of the borland DTD. This can be a file on the system
     * or a resource on the classpath.
     * @param inString the string to use as the DTD location.
     */
    public void setBASdtd(String inString) {
        this.borlandDTD = inString;
    }


    /**
     * setter used to store whether the task will include the generate client task.
     * (see : BorlandGenerateClient task)
     * @param b if true generate the client task.
     */
    public void setGenerateclient(boolean b) {
        this.generateclient = b;
    }

    /**
     * setter used to store the borland appserver version [4 or 5]
     * @param version app server version 4 or 5
     */
    public void setVersion(int version) {
        this.version = version;
    }

    /**
     * If filled, the params are added to the java2iiop command.
     * (ex: -no_warn_missing_define)
     * @param params additional params for java2iiop
     */
    public void setJava2iiopParams(String params) {
        this.java2iioparams = params;
    }


    /**
     * Get the borland descriptor handler.
     * @param srcDir the source directory.
     * @return the descriptor.
     */
    protected DescriptorHandler getBorlandDescriptorHandler(final File srcDir) {
        DescriptorHandler handler =
            new DescriptorHandler(getTask(), srcDir) {
                    protected void processElement() {
                        if (currentElement.equals("type-storage")) {
                            // Get the filename of vendor specific descriptor
                            String fileNameWithMETA = currentText;
                            //trim the META_INF\ off of the file name
                            String fileName
                                = fileNameWithMETA.substring(META_DIR.length(),
                                    fileNameWithMETA.length());
                            File descriptorFile = new File(srcDir, fileName);

                            ejbFiles.put(fileNameWithMETA, descriptorFile);
                        }
                    }
                };
        handler.registerDTD(PUBLICID_BORLAND_EJB,
                            borlandDTD == null ? DEFAULT_BAS_DTD_LOCATION : borlandDTD);

        for (Iterator i = getConfig().dtdLocations.iterator(); i.hasNext();) {
            EjbJar.DTDLocation dtdLocation = (EjbJar.DTDLocation) i.next();
            handler.registerDTD(dtdLocation.getPublicId(), dtdLocation.getLocation());
        }
        return handler;
    }

    /**
     * Add any vendor specific files which should be included in the
     * EJB Jar.
     * @param ejbFiles the map to add the files to.
     * @param ddPrefix the prefix to use.
     */
    protected void addVendorFiles(Hashtable ejbFiles, String ddPrefix) {

        //choose the right vendor DD
        if (!(version == BES || version == BAS)) {
            throw new BuildException("version " + version + " is not supported");
        }

        String dd = (version == BES ? BES_DD : BAS_DD);

        log("vendor file : " + ddPrefix + dd, Project.MSG_DEBUG);

        File borlandDD = new File(getConfig().descriptorDir, ddPrefix + dd);
        if (borlandDD.exists()) {
            log("Borland specific file found " + borlandDD,  Project.MSG_VERBOSE);
            ejbFiles.put(META_DIR + dd ,  borlandDD);
        } else {
            log("Unable to locate borland deployment descriptor. "
                + "It was expected to be in "
                + borlandDD.getPath(), Project.MSG_WARN);
            return;
        }
    }

    /**
     * Get the vendor specific name of the Jar that will be output. The modification date
     * of this jar will be checked against the dependent bean classes.
     */
    File getVendorOutputJarFile(String baseName) {
        return new File(getDestDir(), baseName +  jarSuffix);
    }

    /**
     * Verify the produced jar file by invoking the Borland verify tool
     * @param sourceJar java.io.File representing the produced jar file
     */
    private void verifyBorlandJar(File sourceJar) {
        if (version == BAS) {
            verifyBorlandJarV4(sourceJar);
            return;
        }
        if (version == BES) {
            verifyBorlandJarV5(sourceJar);
            return;
        }
        log("verify jar skipped because the version is invalid ["
            + version + "]", Project.MSG_WARN);
    }

    /**
     * Verify the produced jar file by invoking the Borland iastool tool
     * @param sourceJar java.io.File representing the produced jar file
     */
    private void verifyBorlandJarV5(File sourceJar) {
        log("verify BES " + sourceJar, Project.MSG_INFO);
        try {
            ExecTask execTask = null;
            execTask = new ExecTask(getTask());
            execTask.setDir(new File("."));
            execTask.setExecutable("iastool");
            //classpath
            if (getCombinedClasspath() != null)  {
                execTask.createArg().setValue("-VBJclasspath");
                execTask.createArg().setValue(getCombinedClasspath().toString());
            }

            if (java2iiopdebug) {
                execTask.createArg().setValue("-debug");
            }
            execTask.createArg().setValue("-verify");
            execTask.createArg().setValue("-src");
            // ejb jar file to verify
            execTask.createArg().setValue(sourceJar.getPath());
            log("Calling iastool", Project.MSG_VERBOSE);
            execTask.execute();
        } catch (Exception e) {
            // Have to catch this because of the semantics of calling main()
            String msg = "Exception while calling generateclient Details: "
                + e.toString();
            throw new BuildException(msg, e);
        }
    }

    /**
     * Verify the produced jar file by invoking the Borland verify tool
     * @param sourceJar java.io.File representing the produced jar file
     */
    private void verifyBorlandJarV4(File sourceJar) {
        org.apache.tools.ant.taskdefs.Java javaTask = null;
        log("verify BAS " + sourceJar, Project.MSG_INFO);
        try  {
            String args = verifyArgs;
            args += " " + sourceJar.getPath();

            javaTask = new Java(getTask());
            javaTask.setTaskName("verify");
            javaTask.setClassname(VERIFY);
            Commandline.Argument arguments = javaTask.createArg();
            arguments.setLine(args);
            Path classpath = getCombinedClasspath();
            if (classpath != null)  {
                javaTask.setClasspath(classpath);
                javaTask.setFork(true);
            }

            log("Calling " + VERIFY + " for " + sourceJar.toString(),
                Project.MSG_VERBOSE);
            javaTask.execute();
        } catch (Exception e) {
            //TO DO : delete the file if it is not a valid file.
            String msg = "Exception while calling " + VERIFY + " Details: "
                + e.toString();
            throw new BuildException(msg, e);
        }
    }


    /**
     * Generate the client jar corresponding to the jar file passed as parameter
     * the method uses the BorlandGenerateClient task.
     * @param sourceJar java.io.File representing the produced jar file
     */
    private void generateClient(File sourceJar) {
        getTask().getProject().addTaskDefinition("internal_bas_generateclient",
            org.apache.tools.ant.taskdefs.optional.ejb.BorlandGenerateClient.class);

        org.apache.tools.ant.taskdefs.optional.ejb.BorlandGenerateClient gentask = null;
        log("generate client for " + sourceJar, Project.MSG_INFO);
        try {
            Project project = getTask().getProject();
            gentask
                = (BorlandGenerateClient) project.createTask("internal_bas_generateclient");
            gentask.setEjbjar(sourceJar);
            gentask.setDebug(java2iiopdebug);
            Path classpath = getCombinedClasspath();
            if (classpath != null) {
                gentask.setClasspath(classpath);
            }
            gentask.setVersion(version);
            gentask.setTaskName("generate client");
            gentask.execute();
        } catch (Exception e) {
            //TO DO : delete the file if it is not a valid file.
            String msg = "Exception while calling " + VERIFY + " Details: "
                + e.toString();
            throw new BuildException(msg, e);
        }
    }

    /**
     * Generate stubs & skeleton for each home found into the DD
     * Add all the generate class file into the ejb files
     * @param ithomes : iterator on home class
     */
    private void buildBorlandStubs(Iterator ithomes) {
        Execute execTask = null;

        execTask = new Execute(this);
        Project project = getTask().getProject();
        execTask.setAntRun(project);
        execTask.setWorkingDirectory(project.getBaseDir());

        Commandline commandline = new Commandline();
        commandline.setExecutable(JAVA2IIOP);
        //debug ?
        if (java2iiopdebug) {
            commandline.createArgument().setValue("-VBJdebug");
        }
        //set the classpath
        commandline.createArgument().setValue("-VBJclasspath");
        commandline.createArgument().setPath(getCombinedClasspath());
        //list file
        commandline.createArgument().setValue("-list_files");
        //no TIE classes
        commandline.createArgument().setValue("-no_tie");

        if (java2iioparams != null) {
            log("additional  " + java2iioparams + " to java2iiop ", 0);
            commandline.createArgument().setValue(java2iioparams);
        }


        //root dir
        commandline.createArgument().setValue("-root_dir");
        commandline.createArgument().setValue(getConfig().srcDir.getAbsolutePath());
        //compiling order
        commandline.createArgument().setValue("-compile");
        //add the home class
        while (ithomes.hasNext()) {
            commandline.createArgument().setValue(ithomes.next().toString());
        }

        try {
            log("Calling java2iiop", Project.MSG_VERBOSE);
            log(commandline.describeCommand(), Project.MSG_DEBUG);
            execTask.setCommandline(commandline.getCommandline());
            int result = execTask.execute();
            if (Execute.isFailure(result)) {
                String msg = "Failed executing java2iiop (ret code is "
                    + result + ")";
                throw new BuildException(msg, getTask().getLocation());
            }
        } catch (java.io.IOException e) {
            log("java2iiop exception :" + e.getMessage(), Project.MSG_ERR);
            throw new BuildException(e, getTask().getLocation());
        }
    }

    /**
     * Method used to encapsulate the writing of the JAR file. Iterates over the
     * filenames/java.io.Files in the Hashtable stored on the instance variable
     * ejbFiles.
     * @param baseName the base name.
     * @param jarFile  the jar file to write to.
     * @param files    the files to write to the jar.
     * @param publicId the id to use.
     * @throws BuildException if there is an error.
     */
    protected void writeJar(String baseName, File jarFile, Hashtable files, String publicId)
        throws BuildException {
        //build the home classes list.
        Vector homes = new Vector();
        Iterator it = files.keySet().iterator();
        while (it.hasNext()) {
            String clazz = (String) it.next();
            if (clazz.endsWith("Home.class")) {
                //remove .class extension
                String home = toClass(clazz);
                homes.add(home);
                log(" Home " + home, Project.MSG_VERBOSE);
            }
        }

        buildBorlandStubs(homes.iterator());

        //add the gen files to the collection
        files.putAll(genfiles);

        super.writeJar(baseName, jarFile, files, publicId);

        if (verify) {
            verifyBorlandJar(jarFile);
        }

        if (generateclient) {
            generateClient(jarFile);
        }
        genfiles.clear();
    }

    /**
     * convert a class file name : A/B/C/toto.class
     * into    a class name: A.B.C.toto
     */
    private String toClass(String filename) {
        //remove the .class
        String classname = filename.substring(0, filename.lastIndexOf(".class"));
        classname = classname.replace('\\', '.');
        return classname;
    }

    /**
     * convert a file name : A/B/C/toto.java
     * into    a class name: A/B/C/toto.class
     */
    private  String toClassFile(String filename) {
        //remove the .class
        String classfile = filename.substring(0, filename.lastIndexOf(".java"));
        classfile = classfile + ".class";
        return classfile;
    }

    // implementation of org.apache.tools.ant.taskdefs.ExecuteStreamHandler interface

    /** {@inheritDoc}. */
    public void start() throws IOException  { }
    /** {@inheritDoc}. */
    public void stop()  {  }
    /** {@inheritDoc}. */
    public void setProcessInputStream(OutputStream param1) throws IOException   { }

    /**
     * Set the output stream of the process.
     * @param is the input stream.
     * @throws IOException if there is an error.
     */
    public void setProcessOutputStream(InputStream is) throws IOException {
        try {
            BufferedReader reader = new BufferedReader(new InputStreamReader(is));
            String javafile;
            while ((javafile = reader.readLine()) != null) {
                if (javafile.endsWith(".java")) {
                    String classfile = toClassFile(javafile);
                    String key = classfile.substring(
                        getConfig().srcDir.getAbsolutePath().length() + 1);
                    genfiles.put(key, new File(classfile));
                }
            }
            reader.close();
        } catch (Exception e) {
            String msg = "Exception while parsing  java2iiop output. Details: " + e.toString();
            throw new BuildException(msg, e);
        }
    }

    /**
     * Set the error stream of the process.
     * @param is the input stream.
     * @throws IOException if there is an error.
     */
    public void setProcessErrorStream(InputStream is) throws IOException {
        BufferedReader reader = new BufferedReader(new InputStreamReader(is));
        String s = reader.readLine();
        if (s != null) {
            log("[java2iiop] " + s, Project.MSG_ERR);
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

QDox parse error in file://<WORKSPACE>/data/code-rep-dataset/Dataset5/Tasks/4487.java