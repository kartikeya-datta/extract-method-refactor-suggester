error id: file://<WORKSPACE>/data/code-rep-dataset/Dataset5/Tasks/3619.java
file://<WORKSPACE>/data/code-rep-dataset/Dataset5/Tasks/3619.java
### com.thoughtworks.qdox.parser.ParseException: syntax error @[1,1]

error in qdox parser
file content:
```java
offset: 1
uri: file://<WORKSPACE>/data/code-rep-dataset/Dataset5/Tasks/3619.java
text:
```scala
P@@roperties sys = null;

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

package org.apache.tools.ant.types;

import org.apache.tools.ant.BuildException;
import org.apache.tools.ant.Project;
import org.apache.tools.ant.util.JavaEnvUtils;

import java.util.Enumeration;
import java.util.LinkedList;
import java.util.List;
import java.util.ListIterator;
import java.util.Properties;
import java.util.Vector;

/**
 * A representation of a Java command line that is
 * a composite of 2 <tt>Commandline</tt>. One is used for the
 * vm/options and one for the classname/arguments. It provides
 * specific methods for a java command line.
 *
 */
public class CommandlineJava implements Cloneable {

    /**
     * commands to the JVM
     */
    private Commandline vmCommand = new Commandline();
    /**
     * actual java commands
     */
    private Commandline javaCommand = new Commandline();
    /**
     * properties to add using -D
     */
    private SysProperties sysProperties = new SysProperties();
    private Path classpath = null;
    private Path bootclasspath = null;
    private String vmVersion;
    private String maxMemory = null;
    /**
     *  any assertions to make? Currently only supported in forked JVMs
     */
    private Assertions assertions = null;

    /**
     * Indicate whether it will execute a jar file or not, in this case
     * the first vm option must be a -jar and the 'executable' is a jar file.
     */
     private boolean executeJar = false;

    /**
     * Whether system properties and bootclasspath shall be cloned.
     * @since Ant 1.7
     */
    private boolean cloneVm = false;

    /**
     * Specialized Environment class for System properties
     */
    public static class SysProperties extends Environment implements Cloneable {
        private Properties sys = null;
        private Vector propertySets = new Vector();

        /**
         * get the properties as an array; this is an override of the
         * superclass, as it evaluates all the properties
         * @return the array of definitions; may be null
         * @throws BuildException on error
         */
        public String[] getVariables() throws BuildException {

            List definitions = new LinkedList();
            ListIterator list = definitions.listIterator();
            addDefinitionsToList(list);
            if (definitions.size() == 0) {
                return null;
            } else {
                return (String[]) definitions.toArray(new String[0]);
            }
        }

        /**
         * add all definitions (including property sets) to a list
         * @param listIt list iterator supporting add method
         */
        public void addDefinitionsToList(ListIterator listIt) {
            String[] props = super.getVariables();
            if (props != null) {
                for (int i = 0; i < props.length; i++) {
                    listIt.add("-D" + props[i]);
                }
            }
            Properties propertySetProperties = mergePropertySets();
            for (Enumeration e = propertySetProperties.keys();
                 e.hasMoreElements();) {
                String key = (String) e.nextElement();
                String value = propertySetProperties.getProperty(key);
                listIt.add("-D" + key + "=" + value);
            }
        }

        /**
         * This method gets the size of the sysproperties instance. This merges all
         * property sets, so is not an O(1) operation.
         * @return the size of the sysproperties instance
         */
        public int size() {
            Properties p = mergePropertySets();
            return variables.size() + p.size();
        }

        /**
         * cache the system properties and set the system properties to the
         * new values
         * @throws BuildException if Security prevented this operation
         */
        public void setSystem() throws BuildException {
            try {
                sys = System.getProperties();
                Properties p = new Properties();
                for (Enumeration e = sys.propertyNames(); e.hasMoreElements();) {
                    String name = (String) e.nextElement();
                    p.put(name, sys.getProperty(name));
                }
                p.putAll(mergePropertySets());
                for (Enumeration e = variables.elements(); e.hasMoreElements();) {
                    Environment.Variable v = (Environment.Variable) e.nextElement();
                    p.put(v.getKey(), v.getValue());
                }
                System.setProperties(p);
            } catch (SecurityException e) {
                throw new BuildException("Cannot modify system properties", e);
            }
        }

        /**
         * restore the system properties to the cached value
         * @throws BuildException  if Security prevented this operation, or
         * there was no system properties to restore
         */
        public void restoreSystem() throws BuildException {
            if (sys == null) {
                throw new BuildException("Unbalanced nesting of SysProperties");
            }

            try {
                System.setProperties(sys);
                sys = null;
            } catch (SecurityException e) {
                throw new BuildException("Cannot modify system properties", e);
            }
        }

        /**
         *  deep clone
         * @return a cloned instance of SysProperties
         * @exception CloneNotSupportedException for signature
         */
        public Object clone() throws CloneNotSupportedException {
            try {
                SysProperties c = (SysProperties) super.clone();
                c.variables = (Vector) variables.clone();
                c.propertySets = (Vector) propertySets.clone();
                return c;
            } catch (CloneNotSupportedException e) {
                return null;
            }
        }

        /**
         * add a propertyset to the total set
         * @param ps the new property set
         */
        public void addSyspropertyset(PropertySet ps) {
            propertySets.addElement(ps);
        }

        /**
         * add a propertyset to the total set
         * @param ps the new property set
         * @since Ant 1.6.3
         */
        public void addSysproperties(SysProperties ps) {
            variables.addAll(ps.variables);
            propertySets.addAll(ps.propertySets);
        }        

        /**
         * merge all property sets into a single Properties object
         * @return the merged object
         */
        private Properties mergePropertySets() {
            Properties p = new Properties();
            for (Enumeration e = propertySets.elements();
                 e.hasMoreElements();) {
                PropertySet ps = (PropertySet) e.nextElement();
                p.putAll(ps.getProperties());
            }
            return p;
        }

    }

    /**
     * constructor uses the VM we are running on now.
     */
    public CommandlineJava() {
        setVm(JavaEnvUtils.getJreExecutable("java"));
        setVmversion(JavaEnvUtils.getJavaVersion());
    }

    /**
     * create a new argument to the java program
     * @return an argument to be configured
     */
    public Commandline.Argument createArgument() {
        return javaCommand.createArgument();
    }

    /**
     * create a new JVM argument
     * @return an argument to be configured
     */
    public Commandline.Argument createVmArgument() {
        return vmCommand.createArgument();
    }

    /**
     * add a system property
     * @param sysp a property to be set in the JVM
     */
    public void addSysproperty(Environment.Variable sysp) {
        sysProperties.addVariable(sysp);
    }

    /**
     * add a set of system properties
     * @param sysp a set of properties
     */
    public void addSyspropertyset(PropertySet sysp) {
        sysProperties.addSyspropertyset(sysp);
    }

    /**
     * add a set of system properties
     * @param sysp a set of properties
     * @since Ant 1.6.3
     */
    public void addSysproperties(SysProperties sysp) {
        sysProperties.addSysproperties(sysp);
    }

    /**
     * Set the executable used to start the new JVM.
     * @param vm the executable to use
     */
    public void setVm(String vm) {
        vmCommand.setExecutable(vm);
    }

    /**
     * Set the JVM version required.
     * @param value the version required
     */
    public void setVmversion(String value) {
        vmVersion = value;
    }

    /**
     * If set, system properties will be copied to the cloned VM - as
     * well as the bootclasspath unless you have explicitly specified
     * a bootclaspath.
     * @param cloneVm if true copy the system properties
     * @since Ant 1.7
     */
    public void setCloneVm(boolean cloneVm) {
        this.cloneVm = cloneVm;
    }

    /**
     * get the current assertions
     * @return assertions or null
     */
    public Assertions getAssertions() {
        return assertions;
    }

    /**
     *  add an assertion set to the command
     * @param assertions assertions to make
     */
    public void setAssertions(Assertions assertions) {
        this.assertions = assertions;
    }

    /**
     * set a jar file to execute via the -jar option.
     * @param jarpathname the pathname of the jar to execute
     */
    public void setJar(String jarpathname) {
        javaCommand.setExecutable(jarpathname);
        executeJar = true;
    }

    /**
     * @return the pathname of the jar file to run via -jar option
     * or <tt>null</tt> if there is no jar to run.
     * @see #getClassname()
     */
    public String getJar() {
        if (executeJar) {
            return javaCommand.getExecutable();
        }
        return null;
    }

    /**
     * set the classname to execute
     * @param classname the fully qualified classname.
     */
    public void setClassname(String classname) {
        javaCommand.setExecutable(classname);
        executeJar = false;
    }

    /**
     * @return the name of the class to run or <tt>null</tt> if there is no class.
     * @see #getJar()
     */
    public String getClassname() {
        if (!executeJar) {
            return javaCommand.getExecutable();
        }
        return null;
    }

    /**
     * Create a classpath.
     * @param p the project to use to create the path in
     * @return a path to be configured
     */
    public Path createClasspath(Project p) {
        if (classpath == null) {
            classpath = new Path(p);
        }
        return classpath;
    }

    /**
     * Create a boot classpath.
     * @param p the project to use to create the path in
     * @return a path to be configured
     * @since Ant 1.6
     */
    public Path createBootclasspath(Project p) {
        if (bootclasspath == null) {
            bootclasspath = new Path(p);
        }
        return bootclasspath;
    }

    /**
     * Get the vm version.
     * @return the vm version
     */
    public String getVmversion() {
        return vmVersion;
    }

    /**
     * get the command line to run a java vm.
     * @return the list of all arguments necessary to run the vm.
     */
    public String[] getCommandline() {
        //create the list
        List commands = new LinkedList();
        final ListIterator listIterator = commands.listIterator();
        //fill it
        addCommandsToList(listIterator);
        //convert to an array
        return (String[]) commands.toArray(new String[0]);
    }

    /**
     * add all the commands to a list identified by the iterator passed in
     * @param listIterator an iterator that supports the add method
     * @since Ant1.6
     */
    private void addCommandsToList(final ListIterator listIterator) {
        //create the command to run Java, including user specified options
        getActualVMCommand().addCommandToList(listIterator);
        // properties are part of the vm options...
        sysProperties.addDefinitionsToList(listIterator);

        if (isCloneVm()) {
            SysProperties clonedSysProperties = new SysProperties();
            PropertySet ps = new PropertySet();
            PropertySet.BuiltinPropertySetName sys =
                new PropertySet.BuiltinPropertySetName();
            sys.setValue("system");
            ps.appendBuiltin(sys);
            clonedSysProperties.addSyspropertyset(ps);
            clonedSysProperties.addDefinitionsToList(listIterator);
        }

        //boot classpath
        Path bcp = calculateBootclasspath(true);
        if (bcp.size() > 0) {
            listIterator.add("-Xbootclasspath:" + bcp.toString());
        }

        //main classpath
        if (haveClasspath()) {
            listIterator.add("-classpath");
            listIterator.add(
                    classpath.concatSystemClasspath("ignore").toString());
        }

        //now any assertions are added
        if (getAssertions() != null) {
            getAssertions().applyAssertions(listIterator);
        }

        // JDK usage command line says that -jar must be the first option, as there is
        // a bug in JDK < 1.4 that forces the jvm type to be specified as the first
        // option, it is appended here as specified in the docs even though there is
        // in fact no order.
        if (executeJar) {
            listIterator.add("-jar");
        }
        // this is the classname to run as well as its arguments.
        // in case of 'executeJar', the executable is a jar file.
        javaCommand.addCommandToList(listIterator);
    }

    /**
     * Specify max memory of the JVM
     * -mx or -Xmx depending on VM version
     * @param max the string to pass to the jvm to specifiy the max memory
     */
    public void setMaxmemory(String max) {
        this.maxMemory = max;
    }


    /**
     * get a string description.
     * @return the command line as a string
     */
    public String toString() {
        return Commandline.toString(getCommandline());
    }

    /**
     * Returns a String that describes the command and arguments
     * suitable for verbose output before a call to
     * <code>Runtime.exec(String[])<code>
     * @return the description string
     * @since Ant 1.5
     */
    public String describeCommand() {
        return Commandline.describeCommand(getCommandline());
    }

    /**
     * Returns a String that describes the java command and arguments
     * for in VM executions.
     *
     * <p>The class name is the executable in this context.</p>
     * @return the description string
     * @since Ant 1.5
     */
    public String describeJavaCommand() {
        return Commandline.describeCommand(getJavaCommand());
    }

    /**
     * Get the VM command parameters, including memory settings
     * @return the VM command parameters
     */
    protected Commandline getActualVMCommand() {
        Commandline actualVMCommand = (Commandline) vmCommand.clone();
        if (maxMemory != null) {
            if (vmVersion.startsWith("1.1")) {
                actualVMCommand.createArgument().setValue("-mx" + maxMemory);
            } else {
                actualVMCommand.createArgument().setValue("-Xmx" + maxMemory);
            }
        }
        return actualVMCommand;
    }

    /**
     * The size of the java command line. This is a fairly intensive
     * operation, as it has to evaluate the size of many components.
     * @return the total number of arguments in the java command line.
     * @see #getCommandline()
     * @deprecated please dont use this -it effectively creates the entire command.
     */
    public int size() {
        int size = getActualVMCommand().size() + javaCommand.size()
            + sysProperties.size();
        // cloned system properties
        if (isCloneVm()) {
            size += System.getProperties().size();
        }

        // classpath is "-classpath <classpath>" -> 2 args
        if (haveClasspath()) {
            size += 2;
        }
        // bootclasspath is "-Xbootclasspath:<classpath>" -> 1 arg
        if (calculateBootclasspath(true).size() > 0) {
            size++;
        }
        // jar execution requires an additional -jar option
        if (executeJar) {
            size++;
        }
        //assertions take up space too
        if (getAssertions() != null) {
            size += getAssertions().size();
        }
        return size;
    }

    /**
     * get the Java command to be used.
     * @return the java command -not a clone.
     */
    public Commandline getJavaCommand() {
        return javaCommand;
    }

    /**
     * Get the VM command, including memory.
     * @return A deep clone of the instance's VM command, with memory settings added
     */
    public Commandline getVmCommand() {
        return getActualVMCommand();
    }

    /**
     * get the classpath for the command
     * @return the classpath or null
     */
    public Path getClasspath() {
        return classpath;
    }

    /**
     * get the boot classpath
     * @return boot classpath or null
     */
    public Path getBootclasspath() {
        return bootclasspath;
    }

    /**
     * cache current system properties and set them to those in this
     * java command
     * @throws BuildException  if Security prevented this operation
     */
    public void setSystemProperties() throws BuildException {
        sysProperties.setSystem();
    }

    /**
     * @throws BuildException  if Security prevented this operation, or
     * there was no system properties to restore
     */
    public void restoreSystemProperties() throws BuildException {
        sysProperties.restoreSystem();
    }

    /**
     * get the system properties object
     * @return The system properties object
     */
    public SysProperties getSystemProperties() {
        return sysProperties;
    }

    /**
     * clone the object; clone of all fields in the class
     * @return a CommandlineJava object
     * @throws BuildException if anything went wrong.
     * @throws CloneNotSupportedException never
     */
    public Object clone() throws CloneNotSupportedException {
        try {
            CommandlineJava c = (CommandlineJava) super.clone();
            c.vmCommand = (Commandline) vmCommand.clone();
            c.javaCommand = (Commandline) javaCommand.clone();
            c.sysProperties = (SysProperties) sysProperties.clone();
            if (classpath != null) {
                c.classpath = (Path) classpath.clone();
            }
            if (bootclasspath != null) {
                c.bootclasspath = (Path) bootclasspath.clone();
            }
            if (assertions != null) {
                c.assertions = (Assertions) assertions.clone();
            }
            return c;
        } catch (CloneNotSupportedException e) {
            throw new BuildException(e);
        }
    }

    /**
     * Clear out the java arguments.
     */
    public void clearJavaArgs() {
        javaCommand.clearArgs();
    }

    /**
     * Has the classpath been specified and shall it really be used or
     * will build.sysclasspath null it?
     * @return true if the classpath is to be used
     * @since Ant 1.6
     */
    protected boolean haveClasspath() {
        Path fullClasspath = classpath != null
            ? classpath.concatSystemClasspath("ignore") : null;
        return fullClasspath != null
            && fullClasspath.toString().trim().length() > 0;
    }

    /**
     * Has the bootclasspath been specified and shall it really be
     * used (build.sysclasspath could be set or the VM may not support
     * it)?
     *
     * @param log whether to log a warning if a bootclasspath has been
     * specified but will be ignored.
     * @return true if the bootclasspath is to be used
     * @since Ant 1.6
     */
    protected boolean haveBootclasspath(boolean log) {
        return calculateBootclasspath(log).size() > 0;
    }

    /**
     * Calculate the bootclasspath based on the bootclasspath
     * specified, the build.sysclasspath and ant.build.clonevm magic
     * properties as well as the cloneVm attribute.
     *
     * @since Ant 1.7
     */
    private Path calculateBootclasspath(boolean log) {
        if (vmVersion.startsWith("1.1")) {
            if (bootclasspath != null && log) {
                bootclasspath.log("Ignoring bootclasspath as "
                                  + "the target VM doesn't support it.");
            }
        } else {
            if (bootclasspath != null) {
                return bootclasspath.concatSystemBootClasspath(isCloneVm()
                                                               ? "last"
                                                               : "ignore");
            } else if (isCloneVm()) {
                return Path.systemBootClasspath;
            }
        }
        return new Path(null);
    }

    /**
     * Has the cloneVm attribute or the magic property
     * ant.build.clonevm been set?
     *
     * @since 1.7
     */
    private boolean isCloneVm() {
        return cloneVm
 "true".equals(System.getProperty("ant.build.clonevm"));
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

QDox parse error in file://<WORKSPACE>/data/code-rep-dataset/Dataset5/Tasks/3619.java