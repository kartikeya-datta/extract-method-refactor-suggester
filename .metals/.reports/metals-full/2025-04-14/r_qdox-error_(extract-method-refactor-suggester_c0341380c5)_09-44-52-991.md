error id: file://<WORKSPACE>/data/code-rep-dataset/Dataset2/Tasks/7379.java
file://<WORKSPACE>/data/code-rep-dataset/Dataset2/Tasks/7379.java
### com.thoughtworks.qdox.parser.ParseException: syntax error @[1,1]

error in qdox parser
file content:
```java
offset: 1
uri: file://<WORKSPACE>/data/code-rep-dataset/Dataset2/Tasks/7379.java
text:
```scala
M@@ethod compile = c.getMethod("compile", new Class[] {String[].class}); //$NON-NLS-1$

/*******************************************************************************
 * Copyright (c) 2002 International Business Machines Corp. and others.
 * All rights reserved. This program and the accompanying materials 
 * are made available under the terms of the Common Public License v0.5 
 * which accompanies this distribution, and is available at
 * http://www.eclipse.org/legal/cpl-v05.html
 * 
 * Contributors:
 *     IBM Corporation - initial API and implementation
 ******************************************************************************/
package org.eclipse.jdt.core;

import java.io.File;
import java.io.PrintWriter;
import java.lang.reflect.Constructor;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;

import org.apache.tools.ant.BuildException;
import org.apache.tools.ant.Project;
import org.apache.tools.ant.taskdefs.Javac;
import org.apache.tools.ant.taskdefs.compilers.DefaultCompilerAdapter;
import org.apache.tools.ant.types.Commandline;
import org.apache.tools.ant.types.FileSet;
import org.apache.tools.ant.types.Path;
import org.eclipse.core.runtime.IPath;
import org.eclipse.jdt.internal.core.Util;

/**
 * Ant 1.5 compiler adapter for the Eclipse Java compiler. This adapter permits the
 * Eclipse Java compiler to be used with the <code>javac</code> task in Ant scripts. In order
 * to use it, just set the property <code>build.compiler</code> as follows:
 * <p>
 * <code>&lt;property name="build.compiler" value="org.eclipse.jdt.core.JDTCompilerAdapter"/&gt;</code>
 * </p>
 * <p>
 * For more information on Ant check out the website at http://jakarta.apache.org/ant/ .
 * </p>
 * 
 * @since 2.0
 */
public class JDTCompilerAdapter extends DefaultCompilerAdapter {
	private static String compilerClass = "org.eclipse.jdt.internal.compiler.batch.Main"; //$NON-NLS-1$
	/**
	 * Performs a compile using the JDT batch compiler 
	 */
	public boolean execute() throws BuildException {
		attributes.log(Util.bind("ant.jdtadapter.info.usingJdtCompiler"), Project.MSG_VERBOSE); //$NON-NLS-1$
		Commandline cmd = setupJavacCommand();

		try {
			Class c = Class.forName(compilerClass);
			Constructor batchCompilerConstructor = c.getConstructor(new Class[] { PrintWriter.class, Boolean.TYPE});
			Object batchCompilerInstance = batchCompilerConstructor.newInstance(new Object[] {new PrintWriter(System.out), new Boolean(true)});
			Method compile = c.getMethod("compile", new Class[] {String[].class});
			Object result = compile.invoke(batchCompilerInstance, new Object[] { cmd.getArguments()});
			return ((Boolean) result).booleanValue();
		} catch (ClassNotFoundException cnfe) {
			throw new BuildException(Util.bind("ant.jdtadapter.error.missingJDTCompiler")); //$NON-NLS-1$
		} catch (Exception ex) {
			throw new BuildException(ex);
		}
	}
	
	
	protected Commandline setupJavacCommand() throws BuildException {
		Commandline cmd = new Commandline();
		
		/*
		 * This option is used to never exit at the end of the ant task. 
		 */
		cmd.createArgument().setValue("-noExit"); //$NON-NLS-1$

		cmd.createArgument().setValue("-bootclasspath"); //$NON-NLS-1$
        if (bootclasspath != null && bootclasspath.size() != 0) {
			/*
			 * Set the bootclasspath for the Eclipse compiler.
			 */
			cmd.createArgument().setPath(bootclasspath);        	
        } else {
            /*
             * No bootclasspath, we will add one throught the JRE_LIB variable
             */
			IPath jre_lib = JavaCore.getClasspathVariable("JRE_LIB"); //$NON-NLS-1$
			if (jre_lib == null) {
				throw new BuildException(Util.bind("ant.jdtadapter.error.missingJRELIB")); //$NON-NLS-1$
			}
			cmd.createArgument().setPath(new Path(null, jre_lib.toOSString()));        	
        }

        Path classpath = new Path(project);

       /*
         * Eclipse compiler doesn't support -extdirs.
         * It is emulated using the classpath. We add extdirs entries after the 
         * bootclasspath.
         */
        addExtdirs(extdirs, classpath);

		/*
		 * The java runtime is already handled, so we simply want to retrieve the
		 * ant runtime and the compile classpath.
		 */
		includeJavaRuntime = false;
        classpath.append(getCompileClasspath());

        // For -sourcepath, use the "sourcepath" value if present.
        // Otherwise default to the "srcdir" value.
        Path sourcepath = null;
        
        // retrieve the method getSourcepath() using reflect
        // This is done to improve the compatibility to ant 1.5
        Class javacClass = Javac.class;
        Method getSourcepathMethod = null;
        try {
	        getSourcepathMethod = javacClass.getMethod("getSourcepath", null); //$NON-NLS-1$
        } catch(NoSuchMethodException e) {
        }
        Path compileSourcepath = null;
        if (getSourcepathMethod != null) {
	 		try {
				compileSourcepath = (Path) getSourcepathMethod.invoke(attributes, null);
			} catch (IllegalAccessException e) {
			} catch (InvocationTargetException e) {
			}
        }
        if (compileSourcepath != null) {
            sourcepath = compileSourcepath;
        } else {
            sourcepath = src;
        }
		classpath.append(sourcepath);
		/*
		 * Set the classpath for the Eclipse compiler.
		 */
		cmd.createArgument().setValue("-classpath"); //$NON-NLS-1$
		cmd.createArgument().setPath(classpath);

        String memoryParameterPrefix = Project.getJavaVersion().equals(Project.JAVA_1_1) ? "-J-" : "-J-X";//$NON-NLS-1$//$NON-NLS-2$
        if (memoryInitialSize != null) {
            if (!attributes.isForkedJavac()) {
                attributes.log(Util.bind("ant.jdtadapter.error.ignoringMemoryInitialSize"), Project.MSG_WARN);//$NON-NLS-1$
            } else {
                cmd.createArgument().setValue(memoryParameterPrefix
                                              + "ms" + memoryInitialSize); //$NON-NLS-1$
            }
        }

        if (memoryMaximumSize != null) {
            if (!attributes.isForkedJavac()) {
                attributes.log(Util.bind("ant.jdtadapter.error.ignoringMemoryMaximumSize"), Project.MSG_WARN);//$NON-NLS-1$
            } else {
                cmd.createArgument().setValue(memoryParameterPrefix
                                              + "mx" + memoryMaximumSize); //$NON-NLS-1$
            }
        }

        if (debug) {
	       // retrieve the method getSourcepath() using reflect
	        // This is done to improve the compatibility to ant 1.5
	        Method getDebugLevelMethod = null;
	        try {
		        getDebugLevelMethod = javacClass.getMethod("getDebugLevel", null); //$NON-NLS-1$
	        } catch(NoSuchMethodException e) {
	        }
     	    String debugLevel = null;
	        if (getDebugLevelMethod != null) {
				try {
					debugLevel = (String) getDebugLevelMethod.invoke(attributes, null);
				} catch (IllegalAccessException e) {
				} catch (InvocationTargetException e) {
				}
        	}
			if (debugLevel != null) {
				if (debugLevel.length() == 0) {
					cmd.createArgument().setValue("-g:none"); //$NON-NLS-1$
				} else {
					cmd.createArgument().setValue("-g:" + debugLevel); //$NON-NLS-1$
				}
			} else {
				cmd.createArgument().setValue("-g"); //$NON-NLS-1$
            }
        } else {
            cmd.createArgument().setValue("-g:none"); //$NON-NLS-1$
        }
        
		/*
		 * Handle the nowarn option. If none, then we generate all warnings.
		 */		
        if (attributes.getNowarn()) {
			if (deprecation) {
				cmd.createArgument().setValue("-warn:allDeprecation"); //$NON-NLS-1$
			} else {
	            cmd.createArgument().setValue("-nowarn"); //$NON-NLS-1$
			}
        } else {
			/*
			 * deprecation option.
			 */		
			if (deprecation) {
				cmd.createArgument().setValue(
					"-warn:allDeprecation,constructorName,packageDefaultMethod,maskedCatchBlocks,unusedLocals,unusedArguments,unusedImports,syntheticAccess,staticReceiver"); //$NON-NLS-1$
			} else {
				cmd.createArgument().setValue(
					"-warn:constructorName,packageDefaultMethod,maskedCatchBlocks,unusedLocals,unusedArguments,unusedImports,syntheticAccess,staticReceiver"); //$NON-NLS-1$
			}
        }

		/*
		 * destDir option.
		 */		
		if (destDir != null) {
			cmd.createArgument().setValue("-d"); //$NON-NLS-1$
			cmd.createArgument().setFile(destDir.getAbsoluteFile());
		}

		/*
		 * target option.
		 */		
		if (target != null) {
			cmd.createArgument().setValue("-target"); //$NON-NLS-1$
			cmd.createArgument().setValue(target);
		}

		/*
		 * verbose option
		 */
		if (verbose) {
			cmd.createArgument().setValue("-verbose"); //$NON-NLS-1$
			/*
			 * extra option allowed by the Eclipse compiler
			 */
			cmd.createArgument().setValue("-log"); //$NON-NLS-1$
			cmd.createArgument().setValue(destDir.getAbsolutePath() + ".log"); //$NON-NLS-1$
		}

		/*
		 * failnoerror option
		 */
		if (!attributes.getFailonerror()) {
			cmd.createArgument().setValue("-proceedOnError"); //$NON-NLS-1$
		}

		/*
		 * source option
		 */
		String source = attributes.getSource();
        if (source != null) {
            cmd.createArgument().setValue("-source"); //$NON-NLS-1$
            cmd.createArgument().setValue(source);
        }
        
		if (Project.getJavaVersion().equals(Project.JAVA_1_4)) {
			cmd.createArgument().setValue("-1.4"); //$NON-NLS-1$
		} else {
			cmd.createArgument().setValue("-1.3"); //$NON-NLS-1$
		}
		
		/*
		 * encoding option
		 */
        if (encoding != null) {
            cmd.createArgument().setValue("-encoding"); //$NON-NLS-1$
            cmd.createArgument().setValue(encoding);
        }

		/*
		 * extra option allowed by the Eclipse compiler
		 */
		cmd.createArgument().setValue("-time"); //$NON-NLS-1$

		/*
		 * extra option allowed by the Eclipse compiler
		 */
		cmd.createArgument().setValue("-noImportError"); //$NON-NLS-1$

		/*
		 * Eclipse compiler doesn't have a -sourcepath option. This is
		 * handled through the javac task that collects all source files in
		 * srcdir option.
		 */        
        logAndAddFilesToCompile(cmd);
		return cmd;
	}
	
    /**
     * Emulation of extdirs feature in java >= 1.2.
     * This method adds all files in the given
     * directories (but not in sub-directories!) to the classpath,
     * so that you don't have to specify them all one by one.
     * @param extdirs - Path to append files to
     */
    private void addExtdirs(Path extdirs, Path classpath) {
        if (extdirs == null) {
            String extProp = System.getProperty("java.ext.dirs"); //$NON-NLS-1$
            if (extProp != null) {
                extdirs = new Path(classpath.getProject(), extProp);
            } else {
                return;
            }
        }

        String[] dirs = extdirs.list();
        for (int i = 0; i < dirs.length; i++) {
            File dir = classpath.getProject().resolveFile(dirs[i]);
            if (dir.exists() && dir.isDirectory()) {
                FileSet fs = new FileSet();
                fs.setDir(dir);
                fs.setIncludes("*"); //$NON-NLS-1$
                classpath.addFileset(fs);
            }
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

QDox parse error in file://<WORKSPACE>/data/code-rep-dataset/Dataset2/Tasks/7379.java