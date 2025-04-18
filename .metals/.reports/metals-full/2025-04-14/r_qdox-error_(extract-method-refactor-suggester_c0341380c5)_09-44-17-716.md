error id: file://<WORKSPACE>/data/code-rep-dataset/Dataset2/Tasks/3631.java
file://<WORKSPACE>/data/code-rep-dataset/Dataset2/Tasks/3631.java
### com.thoughtworks.qdox.parser.ParseException: syntax error @[1,1]

error in qdox parser
file content:
```java
offset: 1
uri: file://<WORKSPACE>/data/code-rep-dataset/Dataset2/Tasks/3631.java
text:
```scala
i@@f (mode == CONFIG_MODE || this.options == null) {

/*******************************************************************************
 * Copyright (c) 2004 Ben Konrath <ben@bagu.org>
 * Copyright (c) 2006 Red Hat Incorporated
 * All rights reserved. This program and the accompanying materials 
 * are made available under the terms of the Eclipse Public License v1.0
 * which accompanies this distribution, and is available at
 * http://www.eclipse.org/legal/epl-v10.html
 * 
 * Contributors:
 *     Ben Konrath <ben@bagu.org> - initial implementation
 *     Red Hat Incorporated - improvements based on comments from JDT developers
 *     IBM Corporation - Code review and integration
 *******************************************************************************/
package org.eclipse.jdt.core.formatter;

import java.io.BufferedInputStream;
import java.io.BufferedWriter;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileWriter;
import java.io.IOException;
import java.text.MessageFormat;
import java.util.ArrayList;
import java.util.Properties;

import org.eclipse.core.runtime.IPlatformRunnable;
import org.eclipse.jdt.core.ToolFactory;
import org.eclipse.jdt.internal.core.util.Util;
import org.eclipse.jface.text.BadLocationException;
import org.eclipse.jface.text.Document;
import org.eclipse.jface.text.IDocument;
import org.eclipse.osgi.util.NLS;
import org.eclipse.text.edits.TextEdit;

/**
 * Implements an Eclipse Application for org.eclipse.jdt.core.JavaCodeFormatter.
 * 
 * There are a couple improvments that could be made: 1. Make a list of all the
 * files first so that a file does not get formatted twice. 2. Use a text based
 * progress monitor for output.
 * 
 * @author Ben Konrath <bkonrath@redhat.com>
 * @since 3.2
 */
public class CodeFormatterApplication implements IPlatformRunnable {

	/**
	 * Deals with the messages in the properties file (cut n' pasted from a
	 * generated class).
	 */
	private final static class Messages extends NLS {
		private static final String BUNDLE_NAME = "org.eclipse.jdt.core.formatter.messages";//$NON-NLS-1$

		public static String CommandLineConfigFile;

		public static String CommandLineDone;

		public static String CommandLineErrorConfig;

		public static String CommandLineErrorFile;

		public static String CommandLineErrorFileDir;

		public static String CommandLineErrorQuietVerbose;
		
		public static String CommandLineErrorNoConfigFile;

		public static String CommandLineFormatting;

		public static String CommandLineStart;

		public static String CommandLineUsage;

		public static String ConfigFileReadingError;

		public static String FormatProblem;

		public static String CaughtException;

		public static String ExceptionSkip;

		static {
			NLS.initializeMessages(BUNDLE_NAME, Messages.class);
		}

		/**
		 * Bind the given message's substitution locations with the given string
		 * values.
		 * 
		 * @param message
		 *            the message to be manipulated
		 * @return the manipulated String
		 */
		public static String bind(String message) {
			return bind(message, null);
		}

		/**
		 * Bind the given message's substitution locations with the given string
		 * values.
		 * 
		 * @param message
		 *            the message to be manipulated
		 * @param binding
		 *            the object to be inserted into the message
		 * @return the manipulated String
		 */
		public static String bind(String message, Object binding) {
			return bind(message, new Object[] {
				binding
			});
		}

		/**
		 * Bind the given message's substitution locations with the given string
		 * values.
		 * 
		 * @param message
		 *            the message to be manipulated
		 * @param binding1
		 *            An object to be inserted into the message
		 * @param binding2
		 *            A second object to be inserted into the message
		 * @return the manipulated String
		 */
		public static String bind(String message, Object binding1, Object binding2) {
			return bind(message, new Object[] {
					binding1, binding2
			});
		}

		/**
		 * Bind the given message's substitution locations with the given string
		 * values.
		 * 
		 * @param message
		 *            the message to be manipulated
		 * @param bindings
		 *            An array of objects to be inserted into the message
		 * @return the manipulated String
		 */
		public static String bind(String message, Object[] bindings) {
			return MessageFormat.format(message, bindings);
		}
	}

	private final String ARG_CONFIG = "-config"; //$NON-NLS-1$

	private final String ARG_HELP = "-help"; //$NON-NLS-1$

	private final String ARG_QUIET = "-quiet"; //$NON-NLS-1$

	private final String ARG_VERBOSE = "-verbose"; //$NON-NLS-1$

	private String configName;

	private Properties options = null;

	private final String PDE_LAUNCH = "-pdelaunch"; //$NON-NLS-1$

	private boolean quiet = false;

	private boolean verbose = false;

	/** 
	 * Display the command line usage message.
	 */
	private void displayHelp() {
		System.out.println(Messages.bind(Messages.CommandLineUsage));
	}

	private void displayHelp(String message) {
		System.err.println(message);
		System.out.println();
		displayHelp();
	}

	/**
	 * Recursively format the Java source code that is contained in the
	 * directory rooted at dir.
	 */
	private void formatDirTree(File dir, CodeFormatter codeFormatter) {

		File[] files = dir.listFiles();
		if (files == null)
			return;

		for (int i = 0; i < files.length; i++) {
			File file = files[i];
			if (file.isDirectory()) {
				formatDirTree(file, codeFormatter);
			} else if (Util.isJavaLikeFileName(file.getPath())) {
				formatFile(file, codeFormatter);
			}
		}
	}

	/**
	 * Format the given Java source file.
	 */
	private void formatFile(File file, CodeFormatter codeFormatter) {
		IDocument doc = new Document();
		try {
			// read the file
			if (this.verbose) {
				System.out.println(Messages.bind(Messages.CommandLineFormatting, file.getAbsolutePath()));
			}
			String contents = new String(org.eclipse.jdt.internal.compiler.util.Util.getFileCharContent(file, null));
			// format the file (the meat and potatoes)
			doc.set(contents);
			TextEdit edit = codeFormatter.format(CodeFormatter.K_COMPILATION_UNIT, contents, 0, contents.length(), 0, null);
			if (edit != null) {
				edit.apply(doc);
			} else {
				System.err.println(Messages.bind(Messages.FormatProblem, file.getAbsolutePath()));
				return;
			}

			// write the file
			final BufferedWriter out = new BufferedWriter(new FileWriter(file));
			try {
				out.write(doc.get());
				out.flush();
			} finally {
				try {
					out.close();
				} catch (IOException e) {
					/* ignore */
				}
			}
		} catch (IOException e) {
			String errorMessage = Messages.bind(Messages.CaughtException, "IOException", e.getLocalizedMessage()); //$NON-NLS-1$
			Util.log(e, errorMessage);
			System.err.println(Messages.bind(Messages.ExceptionSkip ,errorMessage));
		} catch (BadLocationException e) {
			String errorMessage = Messages.bind(Messages.CaughtException, "BadLocationException", e.getLocalizedMessage()); //$NON-NLS-1$
			Util.log(e, errorMessage);
			System.err.println(Messages.bind(Messages.ExceptionSkip ,errorMessage));
		}
	}

	private File[] processCommandLine(String[] argsArray) {

		ArrayList args = new ArrayList();
		for (int i = 0; i < argsArray.length; i++) {
			args.add(argsArray[i]);
		}
		int index = 0;
		final int argCount = argsArray.length;

		final int DEFAULT_MODE = 0;
		final int CONFIG_MODE = 1;
		
		int mode = DEFAULT_MODE;
		final int INITIAL_SIZE = 1;
		int fileCounter = 0;
		
		File[] filesToFormat = new File[INITIAL_SIZE];

		loop: while (index < argCount) {
			String currentArg = argsArray[index++];

			switch(mode) {
				case DEFAULT_MODE :
					if (PDE_LAUNCH.equals(currentArg)) {
						continue loop;
					}			
					if (ARG_HELP.equals(currentArg)) {
						displayHelp();
						return null;				
					}
					if (ARG_VERBOSE.equals(currentArg)) {
						this.verbose = true;
						continue loop;
					}
					if (ARG_QUIET.equals(currentArg)) {
						this.quiet = true;
						continue loop;
					}
					if (ARG_CONFIG.equals(currentArg)) {
						mode = CONFIG_MODE;
						continue loop;
					}
					// the current arg should be a file or a directory name
					File file = new File(currentArg);
					if (file.exists()) {
						if (filesToFormat.length == fileCounter) {
							System.arraycopy(filesToFormat, 0, (filesToFormat = new File[fileCounter * 2]), 0, fileCounter);
						}
						filesToFormat[fileCounter++] = file;
					} else {
						displayHelp(Messages.bind(Messages.CommandLineErrorFile, currentArg));
						return null;
					}	
					break;
				case CONFIG_MODE :
					this.configName = currentArg;
					this.options = readConfig(currentArg);
					if (this.options == null) {
						displayHelp(Messages.bind(Messages.CommandLineErrorConfig, currentArg));
						return null;
					}
					mode = DEFAULT_MODE;
					continue loop;
			}			
		}

		if (mode == CONFIG_MODE) {
			displayHelp(Messages.bind(Messages.CommandLineErrorNoConfigFile));
			return null;			
		}
		if (this.quiet && this.verbose) {
			displayHelp(
				Messages.bind(
					Messages.CommandLineErrorQuietVerbose,
					new String[] { ARG_QUIET, ARG_VERBOSE }
				));
			return null;
		}
		if (fileCounter == 0) {
			displayHelp(Messages.bind(Messages.CommandLineErrorFileDir));
			return null;
		}
		if (filesToFormat.length != fileCounter) {
			System.arraycopy(filesToFormat, 0, (filesToFormat = new File[fileCounter]), 0, fileCounter);
		}
		return filesToFormat;
	}

	/**
	 * Return a Java Properties file representing the options that are in the
	 * specified config file.
	 */
	private Properties readConfig(String filename) {
		BufferedInputStream stream = null;
		try {
			stream = new BufferedInputStream(new FileInputStream(new File(filename)));
			final Properties formatterOptions = new Properties();
			formatterOptions.load(stream);
			return formatterOptions;
		} catch (IOException e) {
			Util.log(e, Messages.bind(Messages.ConfigFileReadingError));
		} finally {
			if (stream != null) {
				try {
					stream.close();
				} catch (IOException e) {
					/* ignore */
				}
			}
		}
		return null;
	}

	/**
	 * Runs the Java code formatter application
	 */
	public Object run(Object args) throws Exception {
		File[] filesToFormat = processCommandLine((String[]) args);

		if (filesToFormat == null) {
			return EXIT_OK;
		}

		if (!this.quiet) {
			if (this.configName != null) {
				System.out.println(Messages.bind(Messages.CommandLineConfigFile, this.configName));
			}
			System.out.println(Messages.bind(Messages.CommandLineStart));
		}

		// format the list of files and/or directories
		final CodeFormatter codeFormatter = ToolFactory.createCodeFormatter(this.options);
		for (int i = 0, max = filesToFormat.length; i < max; i++) {
			final File file = filesToFormat[i];
			if (file.isDirectory()) {
				formatDirTree(file, codeFormatter);
			} else if (Util.isJavaLikeFileName(file.getPath())) {
				formatFile(file, codeFormatter);
			}			
		}
		if (!this.quiet) {
			System.out.println(Messages.bind(Messages.CommandLineDone));
		}

		return EXIT_OK;
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

QDox parse error in file://<WORKSPACE>/data/code-rep-dataset/Dataset2/Tasks/3631.java