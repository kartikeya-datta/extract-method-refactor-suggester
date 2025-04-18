error id: file://<WORKSPACE>/data/code-rep-dataset/Dataset5/Tasks/13996.java
file://<WORKSPACE>/data/code-rep-dataset/Dataset5/Tasks/13996.java
### com.thoughtworks.qdox.parser.ParseException: syntax error @[1,1]

error in qdox parser
file content:
```java
offset: 1
uri: file://<WORKSPACE>/data/code-rep-dataset/Dataset5/Tasks/13996.java
text:
```scala
S@@tring openzipsString = getSystemPropertyWithoutSecurityException("org.aspectj.weaver.openarchives",Integer.toString(MAXOPEN_DEFAULT));

/* *******************************************************************
 * Copyright (c) 2002 Palo Alto Research Center, Incorporated (PARC).
 * All rights reserved. 
 * This program and the accompanying materials are made available 
 * under the terms of the Common Public License v1.0 
 * which accompanies this distribution and is available at 
 * http://www.eclipse.org/legal/cpl-v10.html 
 *  
 * Contributors: 
 *     PARC     initial implementation 
 * ******************************************************************/


package org.aspectj.weaver.bcel;

import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.util.ArrayList;
import java.util.Enumeration;
import java.util.Iterator;
import java.util.List;
import java.util.zip.ZipEntry;
import java.util.zip.ZipFile;

import org.aspectj.bridge.IMessageHandler;
import org.aspectj.bridge.MessageUtil;
import org.aspectj.weaver.BCException;
import org.aspectj.weaver.TypeX;
import org.aspectj.weaver.WeaverMessages;


public class ClassPathManager {
	
	private List entries;
	
	// In order to control how many open files we have, we maintain a list.
	// The max number is configured through the property:
	//    org.aspectj.weaver.openarchives
	// and it defaults to 1000
	private List openArchives                = new ArrayList();
	private static int maxOpenArchives       = -1;
    private static final int MAXOPEN_DEFAULT = 1000;
	
	static {
		String openzipsString = getSystemPropertyWithoutSecurityException("org.aspectj.weaver.openzips",Integer.toString(MAXOPEN_DEFAULT));
		maxOpenArchives=Integer.parseInt(openzipsString);
		if (maxOpenArchives<20) maxOpenArchives=1000;
	}
	
	
	
	public ClassPathManager(List classpath, IMessageHandler handler) {
		entries = new ArrayList();
		for (Iterator i = classpath.iterator(); i.hasNext();) {
			String name = (String) i.next();
			addPath(name, handler);
		}
	}

	protected ClassPathManager() {};
	
	public void addPath (String name, IMessageHandler handler) {
		File f = new File(name);
		String lc = name.toLowerCase();
		if (lc.endsWith(".jar") || lc.endsWith(".zip")) {
			if (!f.isFile()) {
			MessageUtil.info(handler, WeaverMessages.format(WeaverMessages.ZIPFILE_ENTRY_MISSING,name));
			return;
			}
			try {
				entries.add(new ZipFileEntry(f));
			} catch (IOException ioe) {
			MessageUtil.warn(handler, WeaverMessages.format(WeaverMessages.ZIPFILE_ENTRY_INVALID,name,ioe.getMessage()));
			return;
			}
		} else {
			if (!f.isDirectory()) {
			MessageUtil.info(handler, WeaverMessages.format(WeaverMessages.DIRECTORY_ENTRY_MISSING,name));
			return;
			}
			entries.add(new DirEntry(f));
		}
	}


	public ClassFile find(TypeX type) {
		String name = type.getName();
		for (Iterator i = entries.iterator(); i.hasNext(); ) {
			Entry entry = (Entry)i.next();
			ClassFile ret = entry.find(name);
			if (ret != null) return ret;
		}
		return null;
	}
	
	public String toString() {
		StringBuffer buf = new StringBuffer();
		boolean start = true;
		for (Iterator i = entries.iterator(); i.hasNext(); ) {
			if (start) { start = false; }
			else {buf.append(File.pathSeparator); }
			buf.append(i.next());
		}
		return buf.toString();
	}
	
	/**
	 * This method is extremely expensive and should only be called rarely
	 */
	public List getAllClassFiles() {
		List ret = new ArrayList();
		for (Iterator i = entries.iterator(); i.hasNext(); ) {
			Entry entry = (Entry)i.next();
			ret.addAll(entry.getAllClassFiles());
		}
		return ret;
	}
	
	
	
	public abstract static class ClassFile {
		public abstract InputStream getInputStream() throws IOException;
		public abstract String getPath();
		public abstract void close();
	}

	public abstract static class Entry {
		public abstract ClassFile find(String name);
		public abstract List getAllClassFiles();
	}
	
	private static class FileClassFile extends ClassFile {
		private File file;
		private FileInputStream fis;
		
		public FileClassFile(File file) {
			this.file = file;
		}
	
		public InputStream getInputStream() throws IOException {
			fis = new FileInputStream(file);
			return fis;
		}
		
		public void close() {
			try {
				if (fis!=null) fis.close();
			} catch (IOException ioe) {
				throw new BCException("Can't close class file : "+file.getName()+": "+ioe.toString());
			}   finally {
				fis = null;
			}
		} 
		
		public String getPath() { return file.getPath(); }	
	}
	
	public class DirEntry extends Entry {
		private String dirPath;
		
		public DirEntry(File dir) { this.dirPath = dir.getPath(); }
		public DirEntry(String dirPath) { this.dirPath = dirPath; }
		
		public ClassFile find(String name) {
			File f = new File(dirPath + File.separator + name.replace('.', File.separatorChar) + ".class");
			if (f.isFile()) return new FileClassFile(f);
			else return null;
		}
		
		public List getAllClassFiles() {
			throw new RuntimeException("unimplemented");
		}
		
		public String toString() { return dirPath; }
	}
	
	private static class ZipEntryClassFile extends ClassFile {
		private ZipEntry entry;
		private ZipFileEntry zipFile;
		private InputStream is;
		
		public ZipEntryClassFile(ZipFileEntry zipFile, ZipEntry entry) {
			this.zipFile = zipFile;
			this.entry = entry;
		}
	
		public InputStream getInputStream() throws IOException {
			is = zipFile.getZipFile().getInputStream(entry);
			return is;
		}
		
		public void close() {
			try {
				if (is!=null) is.close();
			} catch (IOException e) {
				e.printStackTrace();
			} finally {
				is = null;
			}
		} 
				
		public String getPath() { return entry.getName(); }
	
	}
	
	public class ZipFileEntry extends Entry {
		private File file;
		private ZipFile zipFile;
		
		public ZipFileEntry(File file) throws IOException {
			this.file = file;
		}
		
		public ZipFileEntry(ZipFile zipFile) {
			this.zipFile = zipFile;
		}
		
		public ZipFile getZipFile() {
			return zipFile;
		}
		
		public ClassFile find(String name) {
			ensureOpen();
			String key = name.replace('.', '/') + ".class";
			ZipEntry entry = zipFile.getEntry(key);
			if (entry != null) return new ZipEntryClassFile(this, entry);
			else               return null; // This zip will be closed when necessary...
		}
		
		public List getAllClassFiles() {
			ensureOpen();
			List ret = new ArrayList();
			for (Enumeration e = zipFile.entries(); e.hasMoreElements(); ) {
				ZipEntry entry = (ZipEntry)e.nextElement();
				String name = entry.getName();
				if (hasClassExtension(name)) ret.add(new ZipEntryClassFile(this, entry));
			}
//			if (ret.isEmpty()) close();
			return ret;
		}
		
		private void ensureOpen() {
			if (zipFile != null) return; // If its not null, the zip is already open
			try {
				if (openArchives.size()>=maxOpenArchives) {
					closeSomeArchives(openArchives.size()/10); // Close 10% of those open
				}
				zipFile = new ZipFile(file);
				openArchives.add(zipFile);
			} catch (IOException ioe) {
				throw new BCException("Can't open archive: "+file.getName()+": "+ioe.toString());
			}
		}
		
		public void closeSomeArchives(int n) {
			for (int i=n-1;i>=0;i--) {
				ZipFile zf = (ZipFile)openArchives.get(i);
				try {
					zf.close();
				} catch (IOException e) {
					e.printStackTrace();
				}
				openArchives.remove(i);
			}
		}
		
		public void close() {
			if (zipFile == null) return;
			try {
				openArchives.remove(zipFile);
				zipFile.close();
			} catch (IOException ioe) {
				throw new BCException("Can't close archive: "+file.getName()+": "+ioe.toString());
			} finally {
				zipFile = null;
			}
		}
		
		public String toString() { return file.getName(); }
	}


	/* private */ static boolean hasClassExtension(String name) {
		return name.toLowerCase().endsWith((".class"));
	}

	
	public void closeArchives() {
		for (Iterator i = entries.iterator(); i.hasNext(); ) {
			Entry entry = (Entry)i.next();
			if (entry instanceof ZipFileEntry) {
				((ZipFileEntry)entry).close();
			}
			openArchives.clear();
		}
	}
	
    // Copes with the security manager
	private static String getSystemPropertyWithoutSecurityException (String aPropertyName, String aDefaultValue) {
		try {
			return System.getProperty(aPropertyName, aDefaultValue);
		} catch (SecurityException ex) {
			return aDefaultValue;
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

QDox parse error in file://<WORKSPACE>/data/code-rep-dataset/Dataset5/Tasks/13996.java