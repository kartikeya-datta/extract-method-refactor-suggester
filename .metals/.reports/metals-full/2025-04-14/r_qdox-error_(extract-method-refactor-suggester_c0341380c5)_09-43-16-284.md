error id: file://<WORKSPACE>/data/code-rep-dataset/Dataset5/Tasks/15421.java
file://<WORKSPACE>/data/code-rep-dataset/Dataset5/Tasks/15421.java
### com.thoughtworks.qdox.parser.ParseException: syntax error @[1,1]

error in qdox parser
file content:
```java
offset: 1
uri: file://<WORKSPACE>/data/code-rep-dataset/Dataset5/Tasks/15421.java
text:
```scala
i@@f (projectPath.startsWith(PROJECT_PREFIX)) {

/*
 * The Apache Software License, Version 1.1
 *
 * Copyright (c) 2000-2002 The Apache Software Foundation.  All rights
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

package org.apache.tools.ant.taskdefs.optional.vss;



import org.apache.tools.ant.BuildException;
import org.apache.tools.ant.Project;
import org.apache.tools.ant.Task;
import org.apache.tools.ant.taskdefs.Execute;
import org.apache.tools.ant.taskdefs.LogStreamHandler;
import org.apache.tools.ant.types.Commandline;

/**
 * A base class for creating tasks for executing commands on Visual SourceSafe.
 * <p>
 * The class extends the 'exec' task as it operates by executing the ss.exe program
 * supplied with SourceSafe. By default the task expects ss.exe to be in the path,
 * you can override this be specifying the ssdir attribute.
 * </p>
 * <p>
 * This class provides set and get methods for 'login' and 'vsspath' attributes. It
 * also contains constants for the flags that can be passed to SS.
 * </p>
 *
 * @author Craig Cottingham
 * @author Andrew Everitt
 */
public abstract class MSVSS extends Task {

    private String m_SSDir = "";
    private String m_vssLogin = null;
    private String m_vssPath = null;
    private String m_serverPath = null;

    /**
     * directory where <code>ss.exe</code> resides; optional.
     * By default the task expects it to be in the PATH.
     *
     * @param dir the directory containing ss.exe
     */
    public final void setSsdir(String dir) {
        m_SSDir = Project.translatePath(dir);
    }
    
    /**
     * Builds and returns the command string to execute ss.exe
     */
    public final String getSSCommand() {
        String toReturn = m_SSDir;
        if (!toReturn.equals("") && !toReturn.endsWith("\\")) {
            toReturn += "\\";
        }
        toReturn += SS_EXE;
        
        return toReturn;
    }

    /**
     * The login to use when accessing VSS, formatted as "username,password";
     * optional.
     * <p>
     * You can omit the password if your database is not password protected.
     *  if you have a password and omit it, Ant/VSS will hang.
     *
     * @param login the login string to use
     */
    public final void setLogin(String login) {
        m_vssLogin = login;
    }

    /**
     * the appropriate login command if the 'login' attribute was specified, otherwise an empty string
     */
    public void getLoginCommand(Commandline cmd) {
        if (m_vssLogin == null) {
            return;
        } else {
            cmd.createArgument().setValue(FLAG_LOGIN + m_vssLogin);
        }
    }

    /**
     * SourceSafe path which specifies the project/file(s) you wish to 
     * perform the action on; required. You should not specify the leading dollar-sign - 
     * it is prepended by Ant automatically.
     * <p>
     * Ant can't cope with a '$' sign in an attribute so we have to add it here.
     * Also we strip off any 'vss://' prefix which is an XMS special and should probably be removed!
     * @todo dont add a $ prefix if it has one
     * @param vssPath
     */
    public final void setVsspath(String vssPath) {
        String projectPath;
        if (vssPath.startsWith("vss://")) {
            projectPath = vssPath.substring(5);
        } else {
            projectPath = vssPath;
        }

        if (projectPath.charAt(0) == '$') {
            m_vssPath = projectPath;
        } else {
            m_vssPath = PROJECT_PREFIX + projectPath;
        }
    }

    /**
     * @return m_vssPath
     */
    public String getVsspath() {
        return m_vssPath;
    }

    /**
     * Set the directory where <code>srssafe.ini</code> resides; optional.
     * @param serverPath
     */
    public final void setServerpath(String serverPath) {
        m_serverPath = serverPath;
    }

    protected int run(Commandline cmd) {
        try {
            Execute exe = new Execute(new LogStreamHandler(this, 
                                                           Project.MSG_INFO,
                                                           Project.MSG_WARN));

            // If location of ss.ini is specified we need to set the 
            // environment-variable SSDIR to this value
            if (m_serverPath != null) {
                String[] env = exe.getEnvironment();
                if (env == null) {
                    env = new String[0];
                }
                String[] newEnv = new String[env.length + 1];
                for (int i = 0; i < env.length ; i++) {
                    newEnv[i] = env[i];
                }
                newEnv[env.length] = "SSDIR=" + m_serverPath;

                exe.setEnvironment(newEnv);
            }
            
            exe.setAntRun(getProject());
            exe.setWorkingDirectory(getProject().getBaseDir());
            exe.setCommandline(cmd.getCommandline());
            return exe.execute();
        } catch (java.io.IOException e) {
            throw new BuildException(e, getLocation());
        }
    }

    /**
     * Constant for the thing to execute
     */
    private static final String SS_EXE = "ss";
    /** */
    public static final String PROJECT_PREFIX = "$";

    /**
     * The 'CP' command
     */
    public static final String COMMAND_CP = "CP";
    /**
     * The 'Add' command
     */
    public static final String COMMAND_ADD = "Add";
    /**
     * The 'Get' command
     */
    public static final String COMMAND_GET = "Get";
    /**
     * The 'Checkout' command
     */
    public static final String COMMAND_CHECKOUT = "Checkout";
    /**
     * The 'Checkin' command
     */
    public static final String COMMAND_CHECKIN = "Checkin";
    /**
     * The 'Label' command
     */
    public static final String COMMAND_LABEL = "Label";
    /**
     * The 'History' command
     */
    public static final String COMMAND_HISTORY = "History";
    /** 
     * The 'Create' command 
     */
    public static final String COMMAND_CREATE = "Create";


    /** */
    public static final String FLAG_LOGIN = "-Y";
    /** */
    public static final String FLAG_OVERRIDE_WORKING_DIR = "-GL";
    /** */
    public static final String FLAG_AUTORESPONSE_DEF = "-I-";
    /** */
    public static final String FLAG_AUTORESPONSE_YES = "-I-Y";
    /** */
    public static final String FLAG_AUTORESPONSE_NO = "-I-N";
    /** */
    public static final String FLAG_RECURSION = "-R";
    /** */
    public static final String FLAG_VERSION = "-V";
    /** */
    public static final String FLAG_VERSION_DATE = "-Vd";
    /** */
    public static final String FLAG_VERSION_LABEL = "-VL";
    /** */
    public static final String FLAG_WRITABLE = "-W";
    /** */
    public static final String VALUE_NO = "-N";
    /** */
    public static final String VALUE_YES = "-Y";
    /** */
    public static final String FLAG_QUIET = "-O-";
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
	scala.collection.parallel.AdaptiveWorkStealingTasks$AWSTWrappedTask.internal(Tasks.scala:159)
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

QDox parse error in file://<WORKSPACE>/data/code-rep-dataset/Dataset5/Tasks/15421.java