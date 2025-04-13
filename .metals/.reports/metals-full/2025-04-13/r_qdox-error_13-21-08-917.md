error id: <WORKSPACE>/data/code-rep-dataset/Dataset2/Tasks/9821.java
<WORKSPACE>/data/code-rep-dataset/Dataset2/Tasks/9821.java
### com.thoughtworks.qdox.parser.ParseException: syntax error @[1,1]

error in qdox parser
file content:
```java
offset: 1
uri: file://<WORKSPACE>/data/code-rep-dataset/Dataset2/Tasks/9821.java
text:
```scala
_@@project.setUUIDRefs(XMIParser.SINGLETON.getUUIDRefs());

// Copyright (c) 1996-2002 The Regents of the University of California. All
// Rights Reserved. Permission to use, copy, modify, and distribute this
// software and its documentation without fee, and without a written
// agreement is hereby granted, provided that the above copyright notice
// and this paragraph appear in all copies.  This software program and
// documentation are copyrighted by The Regents of the University of
// California. The software program and documentation are supplied "AS
// IS", without any accompanying services from The Regents. The Regents
// does not warrant that the operation of the program will be
// uninterrupted or error-free. The end-user understands that the program
// was developed for research purposes and is advised not to rely
// exclusively on the program for any reason.  IN NO EVENT SHALL THE
// UNIVERSITY OF CALIFORNIA BE LIABLE TO ANY PARTY FOR DIRECT, INDIRECT,
// SPECIAL, INCIDENTAL, OR CONSEQUENTIAL DAMAGES, INCLUDING LOST PROFITS,
// ARISING OUT OF THE USE OF THIS SOFTWARE AND ITS DOCUMENTATION, EVEN IF
// THE UNIVERSITY OF CALIFORNIA HAS BEEN ADVISED OF THE POSSIBILITY OF
// SUCH DAMAGE. THE UNIVERSITY OF CALIFORNIA SPECIFICALLY DISCLAIMS ANY
// WARRANTIES, INCLUDING, BUT NOT LIMITED TO, THE IMPLIED WARRANTIES OF
// MERCHANTABILITY AND FITNESS FOR A PARTICULAR PURPOSE. THE SOFTWARE
// PROVIDED HEREUNDER IS ON AN "AS IS" BASIS, AND THE UNIVERSITY OF
// CALIFORNIA HAS NO OBLIGATIONS TO PROVIDE MAINTENANCE, SUPPORT,
// UPDATES, ENHANCEMENTS, OR MODIFICATIONS.

package org.argouml.uml;

import org.apache.log4j.Category;
import org.argouml.kernel.*;
import org.argouml.model.uml.UmlFactory;
import org.argouml.ui.*;
import org.argouml.xml.xmi.XMIParser;

import java.net.URL;
import java.util.Iterator;
import java.io.*;
import java.util.zip.*;
import javax.swing.*;

import ru.novosoft.uml.model_management.MModel;
import ru.novosoft.uml.foundation.core.MNamespace;
import ru.novosoft.uml.xmi.*;


/**
 * @author Piotr Kaminski
 */


/** This file updated by Jim Holt 1/17/00 for nsuml support **/


public class ProjectMemberModel extends ProjectMember {
	
	private static Category cat = Category.getInstance(org.argouml.uml.ProjectMemberModel.class);

  ////////////////////////////////////////////////////////////////
  // constants

  public static final String MEMBER_TYPE = "xmi";
  public static final String FILE_EXT = "." + MEMBER_TYPE;

  ////////////////////////////////////////////////////////////////
  // instance variables

  private MModel _model;

  ////////////////////////////////////////////////////////////////
  // constructors

  public ProjectMemberModel(String name, Project p) { super(name, p); }

  public ProjectMemberModel(MModel m, Project p) {
    super(p.getBaseName() + FILE_EXT, p);
    setModel(m);
  }

  ////////////////////////////////////////////////////////////////
  // accessors

  public MModel getModel() { return _model; }
  protected void setModel(MModel model) { _model = model; }

  public String getType() { return MEMBER_TYPE; }
  public String getFileExtension() { return FILE_EXT; }


  ////////////////////////////////////////////////////////////////
  // actions

  public void load() throws java.io.IOException, org.xml.sax.SAXException {
    cat.info("Reading " + getURL());
    XMIParser.SINGLETON.readModels(_project,getURL());
    _model = XMIParser.SINGLETON.getCurModel();
    _project._UUIDRefs = XMIParser.SINGLETON.getUUIDRefs();
    cat.info("Done reading " + getURL());
  }

  public void save(String path, boolean overwrite) throws Exception {
      save(path, overwrite, null);
  }

  public void save(String path, boolean overwrite, Writer writer) throws Exception{

      if (writer == null) {
	  	throw new IllegalArgumentException("No Writer specified!");
      }


      //if (!path.endsWith("/")) path += "/";
      //String fullpath = path + getName();

    XMIWriter xmiwriter = null;

    try {
      ProjectBrowser pb = ProjectBrowser.TheInstance;

      xmiwriter = new XMIWriter(_model,writer);
      xmiwriter.gen();
    }
    catch (Exception ex) {
      logNotContainedElements(xmiwriter);
      throw ex;
    }
    finally {
    	if (xmiwriter != null) {
    		if (!xmiwriter.getNotContainedElements().isEmpty()) {
    			logNotContainedElements(xmiwriter);
    			throw new IncompleteXMIException();
    		}
    	}
    }
  }

	private void logNotContainedElements(XMIWriter xmiwriter) {
		if (xmiwriter != null) {
			Iterator it = xmiwriter.getNotContainedElements().iterator();
			while (it.hasNext())
	  			cat.error("Not contained in XMI: " + it.next());
      	}
	}

} /* end class ProjectMemberModel */
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
	scala.meta.internal.mtags.MtagsIndexer.index(MtagsIndexer.scala:21)
	scala.meta.internal.mtags.MtagsIndexer.index$(MtagsIndexer.scala:20)
	scala.meta.internal.mtags.JavaMtags.index(JavaMtags.scala:38)
	scala.meta.internal.tvp.IndexedSymbols.javaSymbols(IndexedSymbols.scala:111)
	scala.meta.internal.tvp.IndexedSymbols.workspaceSymbolsFromPath(IndexedSymbols.scala:120)
	scala.meta.internal.tvp.IndexedSymbols.$anonfun$workspaceSymbols$2(IndexedSymbols.scala:146)
	scala.collection.concurrent.TrieMap.getOrElseUpdate(TrieMap.scala:960)
	scala.meta.internal.tvp.IndexedSymbols.$anonfun$workspaceSymbols$1(IndexedSymbols.scala:146)
	scala.meta.internal.tvp.IndexedSymbols.withTimer(IndexedSymbols.scala:71)
	scala.meta.internal.tvp.IndexedSymbols.workspaceSymbols(IndexedSymbols.scala:143)
	scala.meta.internal.tvp.FolderTreeViewProvider.$anonfun$projects$9(MetalsTreeViewProvider.scala:306)
	scala.collection.Iterator$$anon$9.next(Iterator.scala:584)
	scala.collection.Iterator$$anon$10.nextCur(Iterator.scala:594)
	scala.collection.Iterator$$anon$10.hasNext(Iterator.scala:608)
	scala.collection.Iterator$$anon$6.hasNext(Iterator.scala:477)
	scala.collection.Iterator$$anon$10.hasNext(Iterator.scala:601)
	scala.collection.Iterator$$anon$8.hasNext(Iterator.scala:562)
	scala.collection.immutable.List.prependedAll(List.scala:155)
	scala.collection.immutable.List$.from(List.scala:685)
	scala.collection.immutable.List$.from(List.scala:682)
	scala.collection.SeqFactory$Delegate.from(Factory.scala:306)
	scala.collection.immutable.Seq$.from(Seq.scala:42)
	scala.collection.IterableOnceOps.toSeq(IterableOnce.scala:1473)
	scala.collection.IterableOnceOps.toSeq$(IterableOnce.scala:1473)
	scala.collection.AbstractIterator.toSeq(Iterator.scala:1306)
	scala.meta.internal.tvp.ClasspathTreeView.children(ClasspathTreeView.scala:62)
	scala.meta.internal.tvp.FolderTreeViewProvider.getProjectRoot(MetalsTreeViewProvider.scala:390)
	scala.meta.internal.tvp.MetalsTreeViewProvider.$anonfun$children$1(MetalsTreeViewProvider.scala:84)
	scala.collection.immutable.List.map(List.scala:247)
	scala.meta.internal.tvp.MetalsTreeViewProvider.children(MetalsTreeViewProvider.scala:84)
	scala.meta.internal.metals.WorkspaceLspService.$anonfun$treeViewChildren$1(WorkspaceLspService.scala:705)
	scala.concurrent.Future$.$anonfun$apply$1(Future.scala:687)
	scala.concurrent.impl.Promise$Transformation.run(Promise.scala:467)
	java.base/java.util.concurrent.ThreadPoolExecutor.runWorker(ThreadPoolExecutor.java:1136)
	java.base/java.util.concurrent.ThreadPoolExecutor$Worker.run(ThreadPoolExecutor.java:635)
	java.base/java.lang.Thread.run(Thread.java:840)
```
#### Short summary: 

QDox parse error in <WORKSPACE>/data/code-rep-dataset/Dataset2/Tasks/9821.java