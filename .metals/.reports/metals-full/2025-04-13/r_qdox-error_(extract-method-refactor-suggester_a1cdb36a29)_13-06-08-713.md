error id: file://<WORKSPACE>/data/code-rep-dataset/Dataset2/Tasks/3457.java
file://<WORKSPACE>/data/code-rep-dataset/Dataset2/Tasks/3457.java
### com.thoughtworks.qdox.parser.ParseException: syntax error @[1,1]

error in qdox parser
file content:
```java
offset: 1
uri: file://<WORKSPACE>/data/code-rep-dataset/Dataset2/Tasks/3457.java
text:
```scala
A@@rgoDiagram d = new UMLClassDiagram(ns);

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

package org.argouml.uml.reveng; 

import java.beans.*;
import java.util.*;
import org.tigris.gef.base.*;
import org.tigris.gef.graph.*;
import org.tigris.gef.presentation.*;
import org.argouml.kernel.*;
import org.argouml.ui.*;
import org.argouml.uml.diagram.*;
import org.argouml.uml.diagram.static_structure.*;
import org.argouml.uml.diagram.static_structure.ui.*;
import ru.novosoft.uml.foundation.core.*;
import ru.novosoft.uml.model_management.*;


/**
 * Instances of this class interface the current diagram.
 *
 * @author <a href="mailto:a_rueckert@gmx.net">Andreas Rueckert</a>
 * @version 1.0
 * @since 1.0
 */
public class DiagramInterface {
    
    Editor _currentEditor = null;

    // To know what diagrams we have to layout after the import,
    // we store them in this Vector.
    Vector _modifiedDiagrams = new Vector();

    /**
     * Creates a new DiagramInterface
     *
     * @param editor The editor to operate on.
     */
    public DiagramInterface(Editor editor) {
  	_currentEditor = editor;
    }

    /**
     * Get the current editor.
     *
     * @return The current editor.
     */
    Editor getEditor() {
	return _currentEditor;
    }
    
    /**
     * Mark a diagram as modified, so we can layout it, after the
     * import is complete.
     *
     * @param diagram The diagram to mark as modified.
     */
    void markDiagramAsModified(Object diagram) {
	if(!_modifiedDiagrams.contains(diagram)) {  // If the diagram is not already marked,
	    _modifiedDiagrams.add(diagram);         // add it to the list.
	}
    }

    /**
     * Get the list of modified diagrams.
     * 
     * @return The list of modified diagrams.
     */
    Vector getModifiedDiagrams() {
	return _modifiedDiagrams;
    }

    /**
     * Reset the list of modified diagrams.
     */
    void resetModifiedDiagrams() {
	_modifiedDiagrams = new Vector();
    }
    
    /**
     * Add a package to the current diagram. If the package already has
     * a representation in the current diagram, it is not(!) added.
     *
     * @param newPackage The package to add.
     */
    public void addPackage(MPackage newPackage) {
	if(!isInDiagram(newPackage)) {
	    GraphModel gm            = Globals.curEditor().getGraphModel();
	    LayerPerspective lay     = (LayerPerspective)getEditor().getLayerManager().getActiveLayer(); 
	    FigPackage newPackageFig = new FigPackage( gm, newPackage); 

	    getEditor().add( newPackageFig);
	    lay.putInPosition( (Fig)newPackageFig);
	    getEditor().damaged( newPackageFig);
	}
    }

    /**
     * Check if a given package has a representation in the current
     * diagram.
     *
     * @param p The package to lookup in the current diagram.
     * @return true if this package has a figure in the current diagram,
     *         false otherwise.
     */
    public boolean isInDiagram(MPackage p) {
	if(ProjectBrowser.TheInstance.getTarget() instanceof Diagram) {
	    return ((Diagram)(ProjectBrowser.TheInstance.getTarget())).getNodes().contains(p); 
	} else {
	    if(ProjectBrowser.TheInstance.getTarget() instanceof ProjectMemberDiagram) {
		return ((ProjectMemberDiagram)ProjectBrowser.TheInstance.getTarget()).getDiagram().getNodes().contains(p); 
	    } else {
		return false;
	    }
	}
    }
    
    /**
     * Create a diagram name from a package name
     *
     * @param packageName The package name.
     * @return The name for the diagram.
     */
    private String getDiagramName(String packageName) {
	return packageName.replace('.','_') + "_classes";
    }

    /**
     * Create a diagram name for a package
     *
     * @param p The package.
     * @return The name for the diagram.
     */
    private String getDiagramName(MPackage p) {
	return getDiagramName(p.getName());
    }

    /**
     * Select or create a class diagram for a package.
     *
     * @param p The package.
     * @param name The fully qualified name of this package.
     */
    public void selectClassDiagram(MPackage p, String name) {

	// Check if this diagram already exists in the project
	ProjectMember m;
	if((m = ProjectBrowser.TheInstance.getProject().findMemberByName( getDiagramName(name) + ".pgml")) != null) {

	    // The diagram already exists in this project. Select it as the current target. 
	    // Andreas: These lines did cost me a few hours of debugging.
	    // Why is it that findMemberByName sometimes returns a ProjectMemberDiagram, but in other
	    // cases a UMLDiagram?
	    if(m instanceof ProjectMemberDiagram) {
		ProjectBrowser.TheInstance.setTarget(((ProjectMemberDiagram)m).getDiagram());

		// This is sorta hack, since we don't know yet if anything will
		// be added later.
		markDiagramAsModified(((ProjectMemberDiagram)m).getDiagram());
	    } else {
		ProjectBrowser.TheInstance.setTarget(m);

		// This is sorta hack, since we don't know yet if anything will
		// be added later.
		markDiagramAsModified(m);
	    }

	} else {  // Otherwise
	    addClassDiagram(p, name);  // create a new classdiagram for the package.
	}
    }

    /**
     * Add a new class diagram for a package to the project.
     *
     * @param target The package to attach the diagram to.
     * @param name The fully qualified name of the package, which is
     *             used to generate the diagram name from.
     */
    public void addClassDiagram(MPackage target, String name) {
	Project p = ProjectBrowser.TheInstance.getProject();
	MNamespace ns = (MNamespace) target;
	try {
	    Diagram d = new UMLClassDiagram(ns);
	    d.setName(getDiagramName(name));
	    p.addMember(d);
	    ProjectBrowser.TheInstance.getNavPane().addToHistory(d);
	    ProjectBrowser.TheInstance.setTarget(d);

	    // This is sorta hack, since we don't know yet if anything will
	    // be added later.
	    markDiagramAsModified(d);
	}
	catch (PropertyVetoException pve) { }
    }
    
    /**
     * Add a class to the current diagram.
     *
     * @param newClass The new class to add to the editor.
     */
    public void addClass(MClass newClass) {
	ClassDiagramGraphModel gm        = (ClassDiagramGraphModel)getEditor().getGraphModel();
	LayerPerspective lay = (LayerPerspective)getEditor().getLayerManager().getActiveLayer(); 	
	FigClass newClassFig = new FigClass( gm, newClass); 
	
	getEditor().add( newClassFig);
	if (gm.canAddNode(newClass))
	    gm.addNode(newClass);
	lay.putInPosition( (Fig)newClassFig);
	gm.addNodeRelatedEdges( newClass);
	getEditor().damaged( newClassFig);
    }

    /**
     * Add a interface to the current diagram.
     *
     * @param newInterface The interface to add.
     */
    public void addInterface(MInterface newInterface) {
	ClassDiagramGraphModel gm        = (ClassDiagramGraphModel)getEditor().getGraphModel();
	LayerPerspective lay             = (LayerPerspective)getEditor().getLayerManager().getActiveLayer(); 
	FigInterface     newInterfaceFig = new FigInterface( gm, newInterface); 

	getEditor().add( newInterfaceFig);
	if (gm.canAddNode(newInterface))
	    gm.addNode(newInterface);
	lay.putInPosition( (Fig)newInterfaceFig);
	gm.addNodeRelatedEdges( newInterface);
	getEditor().damaged( newInterfaceFig);		
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

QDox parse error in file://<WORKSPACE>/data/code-rep-dataset/Dataset2/Tasks/3457.java