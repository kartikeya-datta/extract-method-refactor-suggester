error id: file://<WORKSPACE>/data/code-rep-dataset/Dataset2/Tasks/3635.java
file://<WORKSPACE>/data/code-rep-dataset/Dataset2/Tasks/3635.java
### com.thoughtworks.qdox.parser.ParseException: syntax error @[1,1]

error in qdox parser
file content:
```java
offset: 1
uri: file://<WORKSPACE>/data/code-rep-dataset/Dataset2/Tasks/3635.java
text:
```scala
s@@uper("action.generate-code-for-project", NO_ICON);

// Copyright (c) 1996-01 The Regents of the University of California. All
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

package org.argouml.uml.ui;

import java.awt.event.ActionEvent;
import java.util.Collection;
import java.util.Iterator;
import java.util.Vector;

import org.argouml.kernel.Project;
import org.argouml.kernel.ProjectManager;
import org.argouml.model.uml.modelmanagement.ModelManagementHelper;
import org.argouml.ui.ArgoDiagram;
import org.argouml.ui.ProjectBrowser;
import org.argouml.uml.diagram.ui.UMLDiagram;
import org.argouml.uml.generator.Generator;
import org.argouml.uml.generator.ui.ClassGenerationDialog;
import ru.novosoft.uml.foundation.core.MClassifier;
import ru.novosoft.uml.foundation.core.MNamespace;

/** Action to trigger code generation for all classes/interfaces in the
 *  project, which have a source code path set in tagged value 'src_path'
 *  @stereotype singleton
 */
public class ActionGenerateProjectCode extends UMLAction {

    ////////////////////////////////////////////////////////////////
    // static variables

    public static ActionGenerateProjectCode SINGLETON = new ActionGenerateProjectCode();


    ////////////////////////////////////////////////////////////////
    // constructors

    protected ActionGenerateProjectCode() {
	super("Generate Code for Project", NO_ICON);
    }


    ////////////////////////////////////////////////////////////////
    // main methods

    public void actionPerformed(ActionEvent ae) {
      Vector classes = new Vector();
      // The following lines should be substituted by the following 2 commented lines.
      // (This is because getting the project still does not seem to work...)
      ProjectBrowser pb = ProjectBrowser.TheInstance;
      ArgoDiagram activeDiagram = pb.getActiveDiagram();
      if (!(activeDiagram instanceof org.argouml.uml.diagram.ui.UMLDiagram)) return;
      ru.novosoft.uml.foundation.core.MNamespace ns = ((org.argouml.uml.diagram.ui.UMLDiagram)activeDiagram).getNamespace();
      if (ns == null) return;
      while (ns.getNamespace() != null) ns = ns.getNamespace();
      Collection elems = ModelManagementHelper.getHelper().getAllModelElementsOfKind(ns,MClassifier.class);
      //Project p = ProjectManager.getManager().getCurrentProject();
      //Collection elems = ModelManagementHelper.getHelper().getAllModelElementsOfKind(MClassifier.class);
      Iterator iter = elems.iterator();
      while (iter.hasNext()) {
        MClassifier cls = (MClassifier)iter.next();
        if (isCodeRelevantClassifier(cls)) {
          classes.addElement(cls);
        }
      }
      ClassGenerationDialog cgd = new ClassGenerationDialog(classes,true);
      cgd.show();
    }

    public boolean shouldBeEnabled() {
      ProjectBrowser pb = ProjectBrowser.TheInstance;
      ArgoDiagram activeDiagram = pb.getActiveDiagram();
      return super.shouldBeEnabled() && (activeDiagram instanceof UMLDiagram);
    }

    private boolean isCodeRelevantClassifier(MClassifier cls) {
      String path = Generator.getCodePath(cls);
      String name = cls.getName();
      if (name == null || name.length() == 0 || Character.isDigit(name.charAt(0))) {
        return false;
      }
      if (path != null) {
        return (path.length() > 0);
      }
      MNamespace parent = cls.getNamespace();
      while (parent != null) {
        path = Generator.getCodePath(parent);
        if (path != null) {
          return (path.length() > 0);
        }
        parent = parent.getNamespace();
      }
      return false;
    }

} /* end class ActionGenerateProjectCode */
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

QDox parse error in file://<WORKSPACE>/data/code-rep-dataset/Dataset2/Tasks/3635.java