error id: file://<WORKSPACE>/data/code-rep-dataset/Dataset2/Tasks/9776.java
file://<WORKSPACE>/data/code-rep-dataset/Dataset2/Tasks/9776.java
### com.thoughtworks.qdox.parser.ParseException: syntax error @[1,1]

error in qdox parser
file content:
```java
offset: 1
uri: file://<WORKSPACE>/data/code-rep-dataset/Dataset2/Tasks/9776.java
text:
```scala
f@@.write("\t\"cluster-config-0.1.dtd\">\n\n");

/*
 * Copyright (C) 2002-2003, Simon Nieuviarts
 */
/***
 * Jonathan: an Open Distributed Processing Environment
 * Copyright (C) 1999 France Telecom R&D
 * Copyright (C) 2002, Simon Nieuviarts
 *
 * This library is free software; you can redistribute it and/or
 * modify it under the terms of the GNU Lesser General Public
 * License as published by the Free Software Foundation; either
 * version 2 of the License, or (at your option) any later version.
 *
 * This library is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the GNU
 * Lesser General Public License for more details.
 *
 * You should have received a copy of the GNU Lesser General Public
 * License along with this library; if not, write to the Free Software
 * Foundation, Inc., 59 Temple Place, Suite 330, Boston, MA  02111-1307  USA
 *
 * Release: 2.0
 *
 * Contact: jonathan@objectweb.org
 *
 * Author: Kathleen Milsted
 *
 * with contributions from:
 *   Francois Horn
 *   Bruno Dumant
 * 
 */
package org.objectweb.carol.cmi.compiler;
import java.util.Vector;

public class ClusterCompiler {
    private int nbClassArgs;
    private Vector classes;

    public ClusterCompiler() {
    }

    /**
     * Runs the stub compiler.
     *
     * @param args - options and input remote class names to the stub compiler.
     */
    public static void main(String[] args) {
        try {
            ClusterCompiler cc = new ClusterCompiler();
            cc.generate(args);
        } catch (Exception e) {
            e.printStackTrace();
            System.exit(1);
        }
        return;
    }

    public void generate(String[] args) throws Exception {
        ClusterConf cconf = new ClusterConf();
        CompilerContext cmpCtx = new CompilerContext();
        prepare(args, cmpCtx, cconf);

        if (cmpCtx.clusterCfgGen != null) {
            java.io.FileWriter f = new java.io.FileWriter(cmpCtx.clusterCfgGen);
            f.write("<!DOCTYPE cluster-config PUBLIC\n");
            f.write("\t\"\"\n");
            f.write("\t\"cluster-config.dtd\">\n\n");
            f.write("<cluster-config>\n\n");
            for (int i = 0; i < nbClassArgs; i++) {
                String className = (String) classes.elementAt(i);
                if (className == null || className == "") {
                    throw new Exception("Cluster stub compiler error: empty class name");
                }
                f.write("<class>\n\t<name>" + className + "</name>\n\n");
                ClassContext clCtx = new ClassContext(cmpCtx, className);
                MethodContext[] remMths = clCtx.getRemoteMethodContexts();
                for (int j = 0; j < remMths.length; j++) {
                    f.write("\t<method>\n\t\t<signature>");
                    f.write(remMths[j].mth.toString());
                    f.write("</signature>\n\t\t<one-choice/>\n\t</method>\n\n");
                }
                f.write("</class>\n\n");
            }
            f.write("</cluster-config>\n");
            f.close();
        } else {
            for (int i = 0; i < nbClassArgs; i++) {
                String className = (String) classes.elementAt(i);
                if (className == null || className == "") {
                    throw new Exception("Cluster stub compiler error: empty class name");
                }

                ClusterConfigCompiler ccc = new ClusterConfigCompiler(cmpCtx);
                ccc.run(className, cconf);
                ClusterStubCompiler csc = new ClusterStubCompiler(cmpCtx);
                csc.run(className, cconf);
            }
        }
    }

    private void prepare(
        String[] args,
        CompilerContext ctxt,
        ClusterConf cconf)
        throws Exception {
        int len = args.length;

        if (len == 0) {
            usage(ctxt);
            System.exit(0);
        }

        nbClassArgs = 0;
        classes = new Vector();

        int final_index = len - 1;
        for (int i = 0; i < len; i++) {
            if (args[i].equals("-v"))
                ctxt.verbose = true;
            else if (args[i].equals("-verbose"))
                ctxt.verbose = true;
            else if (args[i].equals("-keep"))
                ctxt.keep = true;
            else if (args[i].equals("-keepgenerated"))
                ctxt.keep = true;
            else if (args[i].equals("-noc")) {
                ctxt.compile = false;
                ctxt.keep = true;
            } else if (args[i].equals("-c")) {
                if (i != final_index) {
                    ctxt.javaCompiler = args[++i];
                } else {
                    warning("final -c option incomplete");
                }
            } else if (args[i].equals("-classpath")) {
                if (i != final_index) {
                    ctxt.classPath = args[++i];
                } else {
                    warning("final -classpath option incomplete");
                }
            } else if (args[i].equals("-conf")) {
                if (i != final_index) {
                    cconf.loadConfig(args[++i]);
                } else {
                    warning("final -conf option incomplete");
                }
            } else if (args[i].equals("-genconf")) {
                if (i != final_index) {
                    ctxt.clusterCfgGen = args[++i];
                } else {
                    warning("final -genconf option incomplete");
                }
            } else if (args[i].equals("-d")) {
                if (i != final_index) {
                    ctxt.classDir = args[++i];
                } else {
                    warning("final -d option incomplete");
                }
            } else if (args[i].equals("-s")) {
                if (i != final_index) {
                    ctxt.srcDir = args[++i];
                } else {
                    warning("final -s option incomplete");
                }
            } else if (args[i].startsWith("-")) {
                // ignore other options (for compatibility with rmic)
            } else {
                classes.addElement(args[i]);
                nbClassArgs++;
            }
        }

        if (ctxt.classDir == null)
            ctxt.classDir = ".";
        if (ctxt.srcDir == null)
            ctxt.srcDir = ctxt.classDir;
    }

    private void usage(CompilerContext ctxt) {
        System.out.println("Cluster stub compiler, version " + CompilerContext.version);
        System.out.println(
            "Usage: java "
                + this.getClass().getName()
                + " [options] [class names]");
        System.out.println();
        System.out.println(
            "Options:\n"
                + "  -v, -verbose         print details of what the compiler is doing\n"
                + "  -keep                do not delete generated source files\n"
                + "  -keepgenerated       same as -keep\n"
                + "  -noc                 do not compile generated source files (implies -keep)\n"
                + "  -c <java compiler>   compile generated source files with this java compiler\n"
                + "                       (defaults to javac)\n"
                + "  -classpath <path>    extra classpath passed to -c compiler\n"
                + "  -d <directory>       root directory for generated class files "
                + "                       (defaults to current directory)\n"
                + "  -s <directory>       root directory for generated source files\n"
                + "                       (defaults to -d directory)\n"
                + "  -conf <xml-file>     specify the XML configuration file to use\n"
                + "  -genconf <xml-file>  generate an XML configuration file example\n");
    }

    private static void warning(String str) {
        System.err.println("Cluster stub compiler warning: " + str);
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

QDox parse error in file://<WORKSPACE>/data/code-rep-dataset/Dataset2/Tasks/9776.java