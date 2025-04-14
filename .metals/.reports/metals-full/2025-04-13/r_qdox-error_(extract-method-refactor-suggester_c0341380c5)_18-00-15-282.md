error id: file://<WORKSPACE>/data/code-rep-dataset/Dataset2/Tasks/3750.java
file://<WORKSPACE>/data/code-rep-dataset/Dataset2/Tasks/3750.java
### com.thoughtworks.qdox.parser.ParseException: syntax error @[1,1]

error in qdox parser
file content:
```java
offset: 1
uri: file://<WORKSPACE>/data/code-rep-dataset/Dataset2/Tasks/3750.java
text:
```scala
e@@lse if(name.equals(VFS.EA_SIZE))

/*
 * VFSFile.java - A file residing on a virtual file system
 * :tabSize=8:indentSize=8:noTabs=false:
 * :folding=explicit:collapseFolds=1:
 *
 * Copyright (C) 1998, 2005 Slava Pestov
 *
 * This program is free software; you can redistribute it and/or
 * modify it under the terms of the GNU General Public License
 * as published by the Free Software Foundation; either version 2
 * of the License, or any later version.
 *
 * This program is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
 * GNU General Public License for more details.
 *
 * You should have received a copy of the GNU General Public License
 * along with this program; if not, write to the Free Software
 * Foundation, Inc., 59 Temple Place - Suite 330, Boston, MA  02111-1307, USA.
 */

package org.gjt.sp.jedit.io;

//{{{ Imports
import java.awt.Color;
import java.io.*;
import java.text.*;
import java.util.Date;
import org.gjt.sp.jedit.*;
import org.gjt.sp.util.Log;
//}}}

/**
 * A directory entry returned from a file listing.
 * @since jEdit 4.3pre2
 */
public class VFSFile implements Serializable
{
	//{{{ File types
	public static final int FILE = 0;
	public static final int DIRECTORY = 1;
	public static final int FILESYSTEM = 2;
	//}}}

	//{{{ Instance variables
	/**
	 * @deprecated Use the accessor/mutator methods instead.
	 */
	public String name;
	/**
	 * @deprecated Use the accessor/mutator methods instead.
	 */
	public String path;
	/**
	 * @deprecated Use the accessor/mutator methods instead.
	 */
	public String symlinkPath;
	/**
	 * @deprecated Use the accessor/mutator methods instead.
	 */
	public String deletePath;
	/**
	 * @deprecated Use the accessor/mutator methods instead.
	 */
	public int type;
	/**
	 * @deprecated Use the accessor/mutator methods instead.
	 */
	public long length;
	/**
	 * @deprecated Use the accessor/mutator methods instead.
	 */
	public boolean hidden;
	/**
	 * @deprecated Use the accessor/mutator methods instead.
	 */
	public boolean canRead;
	/**
	 * @deprecated Use the accessor/mutator methods instead.
	 */
	public boolean canWrite;
	//}}}

	//{{{ VFSFile constructor
	/**
	 * @since jEdit 4.3pre2
	 */
	public VFSFile()
	{
	} //}}}

	//{{{ VFSFile constructor
	public VFSFile(String name, String path, String deletePath,
		int type, long length, boolean hidden)
	{
		this.name = name;
		this.path = path;
		this.deletePath = deletePath;
		this.symlinkPath = path;
		this.type = type;
		this.length = length;
		this.hidden = hidden;
		if(path != null)
		{
			// maintain backwards compatibility
			VFS vfs = VFSManager.getVFSForPath(path);
			canRead = ((vfs.getCapabilities() & VFS.READ_CAP) != 0);
			canWrite = ((vfs.getCapabilities() & VFS.WRITE_CAP) != 0);
		}
	} //}}}

	//{{{ getName() method
	public String getName()
	{
		return name;
	} //}}}

	//{{{ setName() method
	public void setName(String name)
	{
		this.name = name;
	} //}}}

	//{{{ getPath() method
	public String getPath()
	{
		return path;
	} //}}}

	//{{{ setPath() method
	public void setPath(String path)
	{
		this.path = path;
	} //}}}

	//{{{ getSymlinkPath() method
	public String getSymlinkPath()
	{
		return symlinkPath;
	} //}}}

	//{{{ setSymlinkPath() method
	public void setSymlinkPath(String symlinkPath)
	{
		this.symlinkPath = symlinkPath;
	} //}}}

	//{{{ getDeletePath() method
	public String getDeletePath()
	{
		return deletePath;
	} //}}}

	//{{{ setDeletePath() method
	public void setDeletePath(String deletePath)
	{
		this.deletePath = deletePath;
	} //}}}

	//{{{ getType() method
	public int getType()
	{
		return type;
	} //}}}

	//{{{ setType() method
	public void setType(int type)
	{
		this.type = type;
	} //}}}

	//{{{ getLength() method
	public long getLength()
	{
		return length;
	} //}}}

	//{{{ setLength() method
	public void setLength(long length)
	{
		this.length = length;
	} //}}}

	//{{{ isHidden() method
	public boolean isHidden()
	{
		return hidden;
	} //}}}

	//{{{ setHidden() method
	public void setHidden(boolean hidden)
	{
		this.hidden = hidden;
	} //}}}

	//{{{ isReadable() method
	public boolean isReadable()
	{
		return canRead;
	} //}}}

	//{{{ setReadable() method
	public void setReadable(boolean canRead)
	{
		this.canRead = canRead;
	} //}}}

	//{{{ isWriteable() method
	public boolean isWriteable()
	{
		return canWrite;
	} //}}}

	//{{{ setWriteable() method
	public void setWriteable(boolean canWrite)
	{
		this.canWrite = canWrite;
	} //}}}

	protected boolean colorCalculated;
	protected Color color;

	//{{{ getExtendedAttribute() method
	/**
	 * Returns the value of an extended attribute. Note that this
	 * returns formatted strings (eg, "10 Mb" for a file size of
	 * 1048576 bytes). If you need access to the raw data, access
	 * fields and methods of this class.
	 * @param name The extended attribute name
	 * @since jEdit 4.2pre1
	 */
	public String getExtendedAttribute(String name)
	{
		if(name.equals(VFS.EA_TYPE))
		{
			switch(getType())
			{
			case FILE:
				return jEdit.getProperty("vfs.browser.type.file");
			case DIRECTORY:
				return jEdit.getProperty("vfs.browser.type.directory");
			case FILESYSTEM:
				return jEdit.getProperty("vfs.browser.type.filesystem");
			default:
				throw new IllegalArgumentException();
			}
		}
		else if(name.equals(VFS.EA_STATUS))
		{
			if(isReadable())
			{
				if(isWriteable())
					return jEdit.getProperty("vfs.browser.status.rw");
				else
					return jEdit.getProperty("vfs.browser.status.ro");
			}
			else
			{
				if(isWriteable())
					return jEdit.getProperty("vfs.browser.status.append");
				else
					return jEdit.getProperty("vfs.browser.status.no");
			}
		}
		else if(getName().equals(VFS.EA_SIZE))
		{
			if(getType() != FILE)
				return null;
			else
				return MiscUtilities.formatFileSize(getLength());
		}
		else
			return null;
	} //}}}

	//{{{ getColor() method
	public Color getColor()
	{
		if(!colorCalculated)
		{
			colorCalculated = true;
			color = VFS.getDefaultColorFor(name);
		}

		return color;
	} //}}}

	//{{{ toString() method
	public String toString()
	{
		return name;
	} //}}}
	
	//{{{ fetchedAttrs() method
	protected boolean fetchedAttrs()
	{
		return fetchedAttrs;
	} //}}}
	
	//{{{ fetchAttrs() method
	protected void fetchAttrs()
	{
		fetchedAttrs = true;
	} //}}}
	
	private boolean fetchedAttrs;
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

QDox parse error in file://<WORKSPACE>/data/code-rep-dataset/Dataset2/Tasks/3750.java