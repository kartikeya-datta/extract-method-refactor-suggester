error id: file://<WORKSPACE>/data/code-rep-dataset/Dataset5/Tasks/15565.java
file://<WORKSPACE>/data/code-rep-dataset/Dataset5/Tasks/15565.java
### com.thoughtworks.qdox.parser.ParseException: syntax error @[1,1]

error in qdox parser
file content:
```java
offset: 1
uri: file://<WORKSPACE>/data/code-rep-dataset/Dataset5/Tasks/15565.java
text:
```scala
s@@uper.setFork(true);

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
package org.apache.tools.ant.taskdefs;

import org.apache.tools.ant.Project;
import org.apache.tools.ant.BuildException;
import org.apache.tools.ant.taskdefs.compilers.AptExternalCompilerAdapter;
import org.apache.tools.ant.types.Path;
import org.apache.tools.ant.types.Reference;
import org.apache.tools.ant.util.JavaEnvUtils;

import java.util.Vector;
import java.io.File;

/**
 * Apt Task for running the Annotation processing tool for JDK 1.5.  It derives
 * from the existing Javac task, and forces the compiler based on whether we're
 * executing internally, or externally.
 *
 * @since Ant 1.7
 */


public class Apt
        extends Javac {
    private boolean compile = true;
    private String factory;
    private Path factoryPath;
    private Vector options = new Vector();
    private File preprocessDir;
    /** The name of the apt tool. */
    public static final String EXECUTABLE_NAME = "apt";
    /** An warning message when ignoring compiler attribute. */
    public static final String ERROR_IGNORING_COMPILER_OPTION
        = "Ignoring compiler attribute for the APT task, as it is fixed";
    /** A warning message if used with java < 1.5. */
    public static final String ERROR_WRONG_JAVA_VERSION
        = "Apt task requires Java 1.5+";

    /**
     * exposed for debug messages
     */
    public static final String WARNING_IGNORING_FORK =
        "Apt only runs in its own JVM; fork=false option ignored";

    /**
     * The nested option element.
     */
    public static final class Option {
        private String name;
        private String value;

        /** Constructor for Option */
        public Option() {
            //default
        }

        /**
         * Get the name attribute.
         * @return the name attribute.
         */
        public String getName() {
            return name;
        }

        /**
         * Set the name attribute.
         * @param name the name of the option.
         */
        public void setName(String name) {
            this.name = name;
        }

        /**
         * Get the value attribute.
         * @return the value attribute.
         */
        public String getValue() {
            return value;
        }

        /**
         * Set the value attribute.
         * @param value the value of the option.
         */
        public void setValue(String value) {
            this.value = value;
        }
    }

    /**
     * Construtor for Apt task.
     * This sets the apt compiler adapter as the compiler in the super class.
     */
    public Apt() {
        super();
        super.setCompiler(AptExternalCompilerAdapter.class.getName());
        setFork(true);
    }

    /**
     * Get the name of the apt executable.
     *
     * @return the name of the executable.
     */
    public String getAptExecutable() {
        return JavaEnvUtils.getJdkExecutable(EXECUTABLE_NAME);
    }

    /**
     * Set the compiler.
     * This is not allowed and a warning log message is made.
     * @param compiler not used.
     */
    public void setCompiler(String compiler) {
        log(ERROR_IGNORING_COMPILER_OPTION, Project.MSG_WARN);
    }

    /**
     * Set the fork attribute.
     * Non-forking APT is highly classpath dependent and appears to be too
     * brittle to work. The sole reason this attribute is retained
     * is the superclass does it
     * @param fork if false; warn the option is ignored.
     */
    public void setFork(boolean fork) {
        if (!fork) {
            log(WARNING_IGNORING_FORK, Project.MSG_WARN);
        }
    }

    /**
     * Get the compiler class name.
     * @return the compiler class name.
     */
    public String getCompiler() {
        return super.getCompiler();
    }

    /**
     * Get the compile option for the apt compiler.
     * If this is false the "-nocompile" argument will be used.
     * @return the value of the compile option.
     */
    public boolean isCompile() {
        return compile;
    }

    /**
     * Set the compile option for the apt compiler.
     * Default value is true.
     * @param compile if true set the compile option.
     */
    public void setCompile(boolean compile) {
        this.compile = compile;
    }

    /**
     * Get the factory option for the apt compiler.
     * If this is non-null the "-factory" argument will be used.
     * @return the value of the factory option.
     */
    public String getFactory() {
        return factory;
    }

    /**
     * Set the factory option for the apt compiler.
     * Default value is null.
     * @param factory the classname of the factory.
     */
    public void setFactory(String factory) {
        this.factory = factory;
    }

    /**
     * Add a reference to a path to the factoryPath attribute.
     * @param ref a reference to a path.
     */
    public void setFactoryPathRef(Reference ref) {
        createFactoryPath().setRefid(ref);
    }

    /**
     * Add a path to the factoryPath attribute.
     * @return a path to be configured.
     */
    public Path createFactoryPath() {
        if (factoryPath == null) {
            factoryPath = new Path(getProject());
        }
        return factoryPath.createPath();
    }

    /**
     * Get the factory path attribute.
     * If this is not null, the "-factorypath" argument will be used.
     * The default value is null.
     * @return the factory path attribute.
     */
    public Path getFactoryPath() {
        return factoryPath;
    }

    /**
     * Create a nested option.
     * @return an option to be configured.
     */
    public Option createOption() {
        Option opt = new Option();
        options.add(opt);
        return opt;
    }

    /**
     * Get the options to the compiler.
     * Each option will use '"-E" name ["=" value]' argument.
     * @return the options.
     */
    public Vector getOptions() {
        return options;
    }

    /**
     * Get the preprocessdir attribute.
     * This corresponds to the "-s" argument.
     * The default value is null.
     * @return the preprocessdir attribute.
     */
    public File getPreprocessDir() {
        return preprocessDir;
    }

    /**
     * Set the preprocessdir attribute.
     * @param preprocessDir where to place processor generated source files.
     */
    public void setPreprocessDir(File preprocessDir) {
        this.preprocessDir = preprocessDir;
    }

    /**
     * Do the compilation.
     * @throws BuildException on error.
     */
    public void execute()
            throws BuildException {
        super.execute();
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

QDox parse error in file://<WORKSPACE>/data/code-rep-dataset/Dataset5/Tasks/15565.java