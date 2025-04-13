error id: file://<WORKSPACE>/data/code-rep-dataset/Dataset2/Tasks/9821.java
file://<WORKSPACE>/data/code-rep-dataset/Dataset2/Tasks/9821.java
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

QDox parse error in file://<WORKSPACE>/data/code-rep-dataset/Dataset2/Tasks/9821.java