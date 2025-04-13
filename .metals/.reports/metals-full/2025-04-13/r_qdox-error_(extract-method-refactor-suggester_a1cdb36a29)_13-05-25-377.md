error id: file://<WORKSPACE>/data/code-rep-dataset/Dataset5/Tasks/4660.java
file://<WORKSPACE>/data/code-rep-dataset/Dataset5/Tasks/4660.java
### com.thoughtworks.qdox.parser.ParseException: syntax error @[1,1]

error in qdox parser
file content:
```java
offset: 1
uri: file://<WORKSPACE>/data/code-rep-dataset/Dataset5/Tasks/4660.java
text:
```scala
S@@tring attr = XMLWriter.makeAttribute("class", className);

/* *******************************************************************
 * Copyright (c) 2002 Palo Alto Research Center, Incorporated (PARC).
 * All rights reserved. 
 * This program and the accompanying materials are made available 
 * under the terms of the Common Public License v1.0 
 * which accompanies this distribution and is available at 
 * http://www.eclipse.org/legal/cpl-v10.html 
 *  
 * Contributors: 
 *     Xerox/PARC     initial implementation 
 * ******************************************************************/

package org.aspectj.testing.harness.bridge;

import org.aspectj.bridge.IMessageHandler;
import org.aspectj.bridge.MessageUtil;
import org.aspectj.testing.Tester;
import org.aspectj.testing.run.IRunIterator;
import org.aspectj.testing.run.IRunStatus;
import org.aspectj.testing.run.WrappedRunIterator;
import org.aspectj.testing.util.TestClassLoader;
import org.aspectj.testing.xml.SoftMessage;
import org.aspectj.testing.xml.XMLWriter;
import org.aspectj.util.FileUtil;
import org.aspectj.util.LangUtil;

import java.io.File;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.net.URL;
import java.util.Arrays;
import java.util.List;

/**
 * Run a class in this VM using reflection.
 */
public class JavaRun implements IAjcRun {
    Spec spec;
	private Sandbox sandbox;   

    /** programmatic initialization per spec */
    public JavaRun(Spec spec) {
        this.spec = spec;
    }
    // XXX init(Spec)
    
    /**
     * This checks the spec for a class name
     * and checks the sandbox for a readable test source directory,
     * a writable run dir, and (non-null, possibly-empty) lists
     * of readable classpath dirs and jars.
	 * @return true if all checks pass
     * @see org.aspectj.testing.harness.bridge.AjcTest.IAjcRun#setup(File, File)
	 */
	public boolean setupAjcRun(Sandbox sandbox, Validator validator) {
		this.sandbox = sandbox;        
		return (validator.nullcheck(spec.className, "class name")
            && validator.nullcheck(sandbox, "sandbox")
            && validator.canReadDir(sandbox.getTestBaseSrcDir(this), "testBaseSrc dir")
            && validator.canWriteDir(sandbox.runDir, "run dir")
            && validator.canReadFiles(sandbox.getClasspathJars(true, this), "classpath jars")
            && validator.canReadDirs(sandbox.getClasspathDirectories(true, this), "classpath dirs")
            );            
        
	}
    
    /** caller must record any exceptions */
    public boolean run(IRunStatus status)
        throws IllegalAccessException,
                InvocationTargetException,
                ClassNotFoundException,
                NoSuchMethodException {

        boolean completedNormally = false;
        Class targetClass = null;
        if (!LangUtil.isEmpty(spec.dirChanges)) {
            MessageUtil.info(status, "XXX dirChanges not implemented in JavaRun");
        }
        TestClassLoader loader = null;
        try {
            final boolean readable = true;
            File[] libs = sandbox.getClasspathJars(readable, this);
            URL[] urls = FileUtil.getFileURLs(libs);
            File[] dirs = sandbox.getClasspathDirectories(readable, this);
            loader = new TestClassLoader(urls, dirs);
            // make the following load test optional
            // Class testAspect = loader.loadClass("org.aspectj.lang.JoinPoint");
            targetClass = loader.loadClass(spec.className);
            Method main = targetClass.getMethod("main", Globals.MAIN_PARM_TYPES);
            setupTester(sandbox.getTestBaseSrcDir(this), loader, status);
            main.invoke(null, new Object[] { spec.getOptionsArray() });
            completedNormally = true;
        } catch (ClassNotFoundException e) {
            String[] classes = FileUtil.listFiles(sandbox.classesDir);
            MessageUtil.info(status, "sandbox.classes: " + Arrays.asList(classes));
            MessageUtil.fail(status, null, e);
        } finally {
            if (!completedNormally) {
                MessageUtil.info(status, spec.toLongString());
                MessageUtil.info(status, "targetClass: " + targetClass);
                MessageUtil.info(status, "sandbox: " + sandbox);
                MessageUtil.info(status, "loader: " + loader);
            }
        }
        return completedNormally;
    }

    /**
     * Clear (static) testing state and setup base directory,
     * unless spec.skipTesting.
     * @return null if successful, error message otherwise
     */
    protected void setupTester(File baseDir, ClassLoader loader, IMessageHandler handler) {
        if (null == loader) {
            setupTester(baseDir, handler);
            return;
        }
        File baseDirSet = null;
        try {
            if (!spec.skipTester) { 
                Class tc = loader.loadClass("org.aspectj.testing.Tester");
                // Tester.clear();
                Method m = tc.getMethod("clear", new Class[0]);
                m.invoke(null, new Object[0]);             
                // Tester.setMessageHandler(handler);
                m = tc.getMethod("setMessageHandler", new Class[] {IMessageHandler.class});
                m.invoke(null, new Object[] { handler});             
                
                //Tester.setBASEDIR(baseDir);
                m = tc.getMethod("setBASEDIR", new Class[] {File.class});
                m.invoke(null, new Object[] { baseDir});             

                //baseDirSet = Tester.getBASEDIR();
                m = tc.getMethod("getBASEDIR", new Class[0]);
                baseDirSet = (File) m.invoke(null, new Object[0]);             
                
                if (!baseDirSet.equals(baseDir)) {
                    String l = "AjcScript.setupTester() setting "
                             + baseDir + " returned " + baseDirSet;
                    MessageUtil.debug(handler, l);
                }
            }
        } catch (Throwable t) {
            MessageUtil.abort(handler, "baseDir=" + baseDir, t);
        }
    }

    /**
     * Clear (static) testing state and setup base directory,
     * unless spec.skipTesting.
     * This implementation assumes that Tester is defined for the
     * same class loader as this class.
     * @return null if successful, error message otherwise
     */
    protected void setupTester(File baseDir, IMessageHandler handler) {
        File baseDirSet = null;
        try {
            if (!spec.skipTester) {                
                Tester.clear();
                Tester.setMessageHandler(handler);
                Tester.setBASEDIR(baseDir);
                baseDirSet = Tester.getBASEDIR();
                if (!baseDirSet.equals(baseDir)) {
                    String l = "AjcScript.setupTester() setting "
                             + baseDir + " returned " + baseDirSet;
                    MessageUtil.debug(handler, l);
                }
            }
        } catch (Throwable t) {
            MessageUtil.abort(handler, "baseDir=" + baseDir, t);
        }
    }
	public String toString() {
        return "JavaRun(" + spec + ")";
	}
        
    /** 
     * Initializer/factory for JavaRun.
     * The classpath is not here but precalculated in the Sandbox. XXX libs?
     */
    public static class Spec extends AbstractRunSpec {
        public static final String XMLNAME = "run";
        /**
         * skip description, skip sourceLocation, 
         * do keywords, do options, skip paths, do comment, 
         * do dirChanges, do messages but skip children. 
         */
        private static final XMLNames NAMES = new XMLNames(XMLNames.DEFAULT,
                "", "", null, null, "", null, false, false, true);
                
        /** fully-qualified name of the class to run */
        protected String className;
        
        /** minimum required version of Java, if any */
        protected String javaVersion;
        
        /** if true, skip Tester setup (e.g., if Tester n/a) */
        protected boolean skipTester;
        
        public Spec() {
            super(XMLNAME);
            setXMLNames(NAMES);
        }
        
        /**
         * @param version "1.1", "1.2", "1.3", "1.4"
         * @throws IllegalArgumentException if version is not recognized
         */
        public void setJavaVersion(String version) {
            LangUtil.supportsJava(version);
            this.javaVersion = version;
        }
        
        /** @className fully-qualified name of the class to run */
        public void setClassName(String className) {
            this.className = className;
        }
        
        /** @param skip if true, then do not set up Tester */
        public void setSkipTester(boolean skip) {
            skipTester = skip;
        }
        
        /** override to set dirToken to Sandbox.RUN_DIR */
        public void addDirChanges(DirChanges.Spec spec) {
            if (null == spec) {
                return;
            }
            spec.setDirToken(Sandbox.RUN_DIR);
            super.addDirChanges(spec);
        }

        /** @return a JavaRun with this as spec if setup completes successfully. */
        public IRunIterator makeRunIterator(Sandbox sandbox, Validator validator) {
            JavaRun run = new JavaRun(this);
            if (run.setupAjcRun(sandbox, validator)) {
                // XXX need name for JavaRun
                return new WrappedRunIterator(this, run);
            }
            return null;
        }

        /** 
         * Write this out as a run element as defined in
         * AjcSpecXmlReader.DOCTYPE.
         * @see AjcSpecXmlReader#DOCTYPE 
         * @see IXmlWritable#writeXml(XMLWriter) 
         */
        public void writeXml(XMLWriter out) {
            String attr = out.makeAttribute("class", className);
            out.startElement(xmlElementName, attr, false);
            if (skipTester) {
                out.printAttribute("skipTester", "true");
            }
            if (null != javaVersion) {
                out.printAttribute("vm", javaVersion);
            }
            super.writeAttributes(out);
            out.endAttributes();
            if (!LangUtil.isEmpty(dirChanges)) {
                DirChanges.Spec.writeXml(out, dirChanges);
            }
            List messages = getMessages();
            if (0 < messages.size()) {
                SoftMessage.writeXml(out, messages);
            }
            out.endElement(xmlElementName);
        }
        public String toLongString() {
            return toString() + "[" + super.toLongString() + "]";        
        }
        
        public String toString() {
            if (skipTester) {
                return "JavaRun(" + className + ", skipTester)";
            } else {
                return "JavaRun(" + className + ")";
            }
        }
        
        /**
         * This implementation skips if:
         * <ul>
         * <li>current VM is not at least any specified javaVersion </li>
         * </ul>
         * @return false if this wants to be skipped, true otherwise
         */
        protected boolean doAdoptParentValues(RT parentRuntime, IMessageHandler handler) {
            if (!super.doAdoptParentValues(parentRuntime, handler)) {
                return false;
            }
            if ((null != javaVersion) && (!LangUtil.supportsJava(javaVersion))) {
                skipMessage(handler, "requires Java version " + javaVersion);
                return false;
            }
            return true;
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

QDox parse error in file://<WORKSPACE>/data/code-rep-dataset/Dataset5/Tasks/4660.java