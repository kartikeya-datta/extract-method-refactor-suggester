error id: file://<WORKSPACE>/data/code-rep-dataset/Dataset2/Tasks/5546.java
file://<WORKSPACE>/data/code-rep-dataset/Dataset2/Tasks/5546.java
### com.thoughtworks.qdox.parser.ParseException: syntax error @[1,1]

error in qdox parser
file content:
```java
offset: 1
uri: file://<WORKSPACE>/data/code-rep-dataset/Dataset2/Tasks/5546.java
text:
```scala
i@@f (cat.getPriority() != null && cat.getPriority().equals(Priority.DEBUG)) {

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

package org.argouml.util;

import org.apache.log4j.Category;
import org.apache.log4j.Priority;
import org.argouml.model.uml.UmlFactory;

import java.util.*;
import java.beans.*;

import ru.novosoft.uml.*;
import ru.novosoft.uml.model_management.*;
import ru.novosoft.uml.foundation.core.*;

/** In the furture this will be a trash can icon in the project
 * browser.  Deleting an object moves it to the trash.  You can move
 * things back out of the trash if you like.  Eventually you empty the
 * trash.  Critics check for relationships between things that will
 * break when the trash is empty.  E.g., Class X's superclass is in
 * the trash, you must fix this before you empty the trash. */

public class Trash {
    protected static Category cat = 
        Category.getInstance(Trash.class);
    
  public static Trash SINGLETON = new Trash();

  /** Keys are model objects, values are TrashItems with recovery info */
  public Vector _contents = new Vector();
  public MModel Trash_Model = UmlFactory.getFactory().getModelManagement().createModel();


  protected Trash() { 
	  Trash_Model.setName("Trash");
  }

  public void addItemFrom(Object obj, Vector places) {
    if (obj == null) {
      cat.warn("tried to add null to trash!");
      return;
    }
    if (obj instanceof MModelElement) {
      MModelElement me = (MModelElement) obj;
	  TrashItem ti = new TrashItem(obj, places);
	  _contents.addElement(ti);
      // next two lines give runtime exceptions. Remove should be done properly
	  //me.setNamespace(null);
      // me.setNamespace(Trash_Model);
	  cat.debug("added " + obj + " to trash");
    }
    //needs-more-work: trash diagrams
  }

  public boolean contains(Object obj) {
    int size = _contents.size();
    for (int i = 0; i < size; i++) {
      TrashItem ti = (TrashItem) _contents.elementAt(i);
      if (ti._item == obj) return true;
    }
    return false;
  }
  
  public void recoverItem(Object obj) {
    cat.debug("needs-more-work: recover from trash");
    if (obj instanceof MModelElement) {
      TrashItem ti = null; //needs-more-work: find in trash
	  //((MModelElement)obj).recoverFromTrash(ti);
    }
  }

  public void removeItem(Object obj) {
    if (obj == null) {
      cat.debug("tried to remove null from trash!");
      return;
    }
    TrashItem ti = null; //needs-more-work: find in trash
    _contents.removeElement(ti);
  }

  public void emptyTrash() {
    cat.debug("needs-more-work: emptyTheTrash not implemented yet");
    if (cat.getPriority().equals(Priority.DEBUG)) {
        StringBuffer buf = new StringBuffer("Trash contents:");
        buf.append("\n");
        java.util.Enumeration keys = _contents.elements();
        while (keys.hasMoreElements()) {
          Object k = keys.nextElement();
          buf.append("| " + ((TrashItem)k)._item + "\n");
        }
        cat.debug(buf.toString());
    }
    
  }

  public int getSize() { return _contents.size(); }

} /* end class Trash */

////////////////////////////////////////////////////////////////

class TrashItem {

  Object _item;
  Object _recoveryInfo = null;
  Vector _places;

  TrashItem(Object item, Vector places) {
    _item = item;
    _places = places;
    if (item instanceof MModelElement) {
		// this can't work with nsuml. Toby
		/*      try {
				_recoveryInfo = ((MModelElement)item).prepareForTrash();
				}
				catch (PropertyVetoException pve) { }
		*/
    }
  }

  public boolean equals(Object o) {
    if (o instanceof TrashItem) {
      TrashItem ti = (TrashItem) o;
      return ti._item == _item;
    }
    return false;
  }

  public int hashCode() { return _item.hashCode(); }

} /* end class TrashItem */
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

QDox parse error in file://<WORKSPACE>/data/code-rep-dataset/Dataset2/Tasks/5546.java