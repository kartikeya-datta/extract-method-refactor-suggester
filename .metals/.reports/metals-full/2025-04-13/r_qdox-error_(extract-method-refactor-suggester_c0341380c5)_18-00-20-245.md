error id: file://<WORKSPACE>/data/code-rep-dataset/Dataset5/Tasks/15278.java
file://<WORKSPACE>/data/code-rep-dataset/Dataset5/Tasks/15278.java
### com.thoughtworks.qdox.parser.ParseException: syntax error @[1,8]

error in qdox parser
file content:
```java
offset: 8
uri: file://<WORKSPACE>/data/code-rep-dataset/Dataset5/Tasks/15278.java
text:
```scala
public V@@msCommandLauncher() {

package org.apache.tools.ant.taskdefs.launcher;

import java.io.BufferedWriter;
import java.io.File;
import java.io.FileWriter;
import java.io.IOException;

import org.apache.tools.ant.Project;
import org.apache.tools.ant.util.FileUtils;

/**
 * A command launcher for VMS that writes the command to a temporary
 * DCL script before launching commands. This is due to limitations of
 * both the DCL interpreter and the Java VM implementation.
 */
public class VmsCommandLauncher extends Java13CommandLauncher {

    public VmsCommandLauncher() throws NoSuchMethodException {
        super();
    }

    /**
     * Launches the given command in a new process.
     * 
     * @param project
     *        the Ant project.
     * @param cmd
     *        the command line to execute as an array of strings.
     * @param env
     *        the environment to set as an array of strings.
     * @return the created Process.
     * @throws IOException
     *         forwarded from the exec method of the command launcher.
     */
    @Override
    public Process exec(Project project, String[] cmd, String[] env)
        throws IOException {
        File cmdFile = createCommandFile(cmd, env);
        Process p = super.exec(project, new String[] {cmdFile.getPath()}, env);
        deleteAfter(cmdFile, p);
        return p;
    }

    /**
     * Launches the given command in a new process, in the given
     * working directory. Note that under Java 1.4.0 and 1.4.1 on VMS
     * this method only works if <code>workingDir</code> is null or
     * the logical JAVA$FORK_SUPPORT_CHDIR needs to be set to TRUE.
     * 
     * @param project
     *        the Ant project.
     * @param cmd
     *        the command line to execute as an array of strings.
     * @param env
     *        the environment to set as an array of strings.
     * @param workingDir
     *        working directory where the command should run.
     * @return the created Process.
     * @throws IOException
     *         forwarded from the exec method of the command launcher.
     */
    @Override
    public Process exec(Project project, String[] cmd, String[] env,
                        File workingDir) throws IOException {
        File cmdFile = createCommandFile(cmd, env);
        Process p = super.exec(project, new String[] {
                cmdFile.getPath()
            }, env, workingDir);
        deleteAfter(cmdFile, p);
        return p;
    }

    /*
     * Writes the command into a temporary DCL script and returns the
     * corresponding File object.  The script will be deleted on exit.
     * @param cmd the command line to execute as an array of strings.
     * @param env the environment to set as an array of strings.
     * @return the command File.
     * @throws IOException if errors are encountered creating the file.
     */
    private File createCommandFile(String[] cmd, String[] env)
        throws IOException {
        File script = FILE_UTILS.createTempFile("ANT", ".COM", null, true, true);
        BufferedWriter out = null;
        try {
            out = new BufferedWriter(new FileWriter(script));

            // add the environment as logicals to the DCL script
            if (env != null) {
                int eqIndex;
                for (int i = 0; i < env.length; i++) {
                    eqIndex = env[i].indexOf('=');
                    if (eqIndex != -1) {
                        out.write("$ DEFINE/NOLOG ");
                        out.write(env[i].substring(0, eqIndex));
                        out.write(" \"");
                        out.write(env[i].substring(eqIndex + 1));
                        out.write('\"');
                        out.newLine();
                    }
                }
            }
            out.write("$ " + cmd[0]);
            for (int i = 1; i < cmd.length; i++) {
                out.write(" -");
                out.newLine();
                out.write(cmd[i]);
            }
        } finally {
            FileUtils.close(out);
        }
        return script;
    }

    private void deleteAfter(final File f, final Process p) {
        new Thread() {
            @Override
            public void run() {
                try {
                    p.waitFor();
                } catch(InterruptedException e) {
                    // ignore
                }
                FileUtils.delete(f);
            }
        }.start();
    }
}
 No newline at end of file
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

QDox parse error in file://<WORKSPACE>/data/code-rep-dataset/Dataset5/Tasks/15278.java