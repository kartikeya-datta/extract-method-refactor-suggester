error id: file://<WORKSPACE>/data/code-rep-dataset/Dataset2/Tasks/2214.java
file://<WORKSPACE>/data/code-rep-dataset/Dataset2/Tasks/2214.java
### com.thoughtworks.qdox.parser.ParseException: syntax error @[1,1]

error in qdox parser
file content:
```java
offset: 1
uri: file://<WORKSPACE>/data/code-rep-dataset/Dataset2/Tasks/2214.java
text:
```scala
i@@f (!zipEntryName.toLowerCase().endsWith(".class")) {//$NON-NLS-1$

/**********************************************************************
Copyright (c)2002 IBM Corp. and others.
All rights reserved.   This program and the accompanying materials
are made available under the terms of the Common Public License v0.5
which accompanies this distribution, and is available at
http://www.eclipse.org/legal/cpl-v05.html
 
Contributors:
     IBM Corporation - initial API and implementation
**********************************************************************/

package org.eclipse.jdt.core;

import java.io.File;
import java.io.IOException;
import java.util.Map;
import java.util.zip.ZipEntry;
import java.util.zip.ZipFile;

import org.eclipse.core.runtime.CoreException;
import org.eclipse.core.runtime.IConfigurationElement;
import org.eclipse.core.runtime.IExtension;
import org.eclipse.core.runtime.IExtensionPoint;
import org.eclipse.core.runtime.IPluginDescriptor;
import org.eclipse.core.runtime.Plugin;
import org.eclipse.jdt.core.compiler.IScanner;
import org.eclipse.jdt.core.util.ClassFormatException;
import org.eclipse.jdt.core.util.IClassFileDisassembler;
import org.eclipse.jdt.core.util.IClassFileReader;
import org.eclipse.jdt.internal.compiler.parser.Scanner;
import org.eclipse.jdt.internal.compiler.util.Util;
import org.eclipse.jdt.internal.core.util.ClassFileReader;
import org.eclipse.jdt.internal.core.util.Disassembler;
import org.eclipse.jdt.internal.formatter.CodeFormatter;

/**
 * Factory for creating various compiler tools, such as scanners, parsers and compilers.
 * 
 * @since 2.0
 */
public class ToolFactory {

	/**
	 * Create an instance of a code formatter. A code formatter implementation can be contributed via the 
	 * extension point "org.eclipse.jdt.core.codeFormatter". If unable to find a registered extension, the factory 
	 * will default to using the default code formatter.
	 * 
	 * @see ICodeFormatter
	 * @see ToolFactory#createDefaultCodeFormatter()
	 */
	public static ICodeFormatter createCodeFormatter(){
		
			Plugin jdtCorePlugin = JavaCore.getPlugin();
			if (jdtCorePlugin == null) return null;
		
			IExtensionPoint extension = jdtCorePlugin.getDescriptor().getExtensionPoint(JavaCore.FORMATTER_EXTPOINT_ID);
			if (extension != null) {
				IExtension[] extensions =  extension.getExtensions();
				for(int i = 0; i < extensions.length; i++){
					IConfigurationElement [] configElements = extensions[i].getConfigurationElements();
						IPluginDescriptor plugin = extension.getDeclaringPluginDescriptor();
						if (plugin.isPluginActivated()) {
							for(int j = 0; j < configElements.length; j++){
								try {
									Object execExt = configElements[j].createExecutableExtension("class"); //$NON-NLS-1$
									if (execExt instanceof ICodeFormatter){
										// use first contribution found
										return (ICodeFormatter)execExt;
									}
								} catch(CoreException e){
								}
							}
						}
				}	
			}
		// no proper contribution found, use default formatter			
		return createDefaultCodeFormatter(null);
	}

	/**
	 * Create an instance of the buit-in code formatter. A code formatter implementation can be contributed via the 
	 * extension point "org.eclipse.jdt.core.codeFormatter". If unable to find a registered extension, the factory will 
	 * default to using the default code formatter.
	 * 
	 * @param options - the options map to use for formatting with the default code formatter. Recognized options
	 * 	are documented on <code>JavaCore#getDefaultOptions()</code>. If set to <code>null</code>, then use 
	 * 	the current settings from <code>JavaCore#getOptions</code>.
	 * 
	 * @see ICodeFormatter
	 * @see ToolFactory#createCodeFormatter()
	 * @see JavaCore#getOptions()
	 */
	public static ICodeFormatter createDefaultCodeFormatter(Map options){

		if (options == null) options = JavaCore.getOptions();
		return new CodeFormatter(options);
	}
	
	/**
	 * Create a classfile bytecode disassembler, able to produce a String representation of a given classfile.
	 * 
	 * @see IClassFileDisassembler
	 */
	public static IClassFileDisassembler createDefaultClassFileDisassembler(){
		return new Disassembler();
	}
	
	/**
	 * Create a default classfile reader, able to expose the internal representation of a given classfile
	 * according to the decoding flag used to initialize the reader.
	 * Answer null if the file named fileName doesn't represent a valid .class file.
	 * 
	 * The decoding flags are described in IClassFileReader.
	 * 
	 * @param fileName the name of the file to be read
	 * @param decodingFlag the flag used to decode the class file reader.
	 * @return IClassFileReader
	 * 
	 * @see IClassFileReader
	 */
	public static IClassFileReader createDefaultClassFileReader(String fileName, int decodingFlag){
		try {
			return new ClassFileReader(Util.getFileByteContent(new File(fileName)), decodingFlag);
		} catch(ClassFormatException e) {
			return null;
		} catch(IOException e) {
			return null;
		}
	}	

	/**
	 * Create a default classfile reader, able to expose the internal representation of a given classfile
	 * according to the decoding flag used to initialize the reader.
	 * Answer null if the file named zipFileName doesn't represent a valid zip file or if the zipEntryName
	 * is not a valid entry name for the specified zip file or if the bytes don't represent a valid
	 * .class file according to the JVM specifications.
	 * 
	 * The decoding flags are described in IClassFileReader.
	 * 
	 * @param zipFileName the name of the zip file
	 * @param zipEntryName the name of the entry in the zip file to be read
	 * @param decodingFlag the flag used to decode the class file reader.
	 * @return IClassFileReader
	 * @see IClassFileReader
	 */
	public static IClassFileReader createDefaultClassFileReader(String zipFileName, String zipEntryName, int decodingFlag){
		try {
			ZipFile zipFile = new ZipFile(zipFileName);
			ZipEntry zipEntry = zipFile.getEntry(zipEntryName);
			if (zipEntry == null) {
				return null;
			}
			if (!zipEntryName.toLowerCase().endsWith(".class")) {
				return null;
			}
			byte classFileBytes[] = Util.getZipEntryByteContent(zipEntry, zipFile);
			return new ClassFileReader(classFileBytes, decodingFlag);
		} catch(ClassFormatException e) {
			return null;
		} catch(IOException e) {
			return null;
		}
	}	
	
	/**
	 * Create a scanner, indicating the level of detail requested for tokenizing. The scanner can then be
	 * used to tokenize some source in a Java aware way.
	 * Here is a typical scanning loop:
	 * 
	 * <code>
	 *   IScanner scanner = ToolFactory.createScanner(false, false, false, false);
	 *   scanner.setSource("int i = 0;".toCharArray());
	 *   while (true) {
	 *     int token = scanner.getNextToken();
	 *     if (token == ITerminalSymbols.TokenNameEOF) break;
	 *     System.out.println(token + " : " + new String(scanner.getCurrentTokenSource()));
	 *   }
	 * </code>
	 * 
  	 * @return IScanner
	 * 
	 * @param tokenizeComments -  if set to <code>false</code>, comments will be silently consumed
	 * @param tokenizeWhiteSpace -  if set to <code>false</code>, white spaces will be silently consumed,
		@param assertKeyword - if set to <code>false</code>, occurrences of 'assert' will be reported as identifiers
	 * (<code>ITerminalSymbols#TokenNameIdentifier</code>), whereas if set to <code>true</code>, it
	 * would report assert keywords (<code>ITerminalSymbols#TokenNameassert</code>). Java 1.4 has introduced
	 * a new 'assert' keyword.
	 * @param recordLineSeparator - if set to <code>true</code>, the scanner will record positions of encountered line 
	 * separator ends. In case of multi-character line separators, the last character position is considered. These positions
	 * can then be extracted using <code>IScanner#getLineEnds</code>
	 * 
	 * @see IScanner
	 */
	public static IScanner createScanner(boolean tokenizeComments, boolean tokenizeWhiteSpace, boolean assertMode, boolean recordLineSeparator){

		Scanner scanner = new Scanner(tokenizeComments, tokenizeWhiteSpace, false, assertMode);
		scanner.recordLineSeparator = recordLineSeparator;
		return scanner;
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

QDox parse error in file://<WORKSPACE>/data/code-rep-dataset/Dataset2/Tasks/2214.java