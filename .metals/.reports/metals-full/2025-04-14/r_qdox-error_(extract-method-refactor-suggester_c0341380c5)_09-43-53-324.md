error id: file://<WORKSPACE>/data/code-rep-dataset/Dataset2/Tasks/853.java
file://<WORKSPACE>/data/code-rep-dataset/Dataset2/Tasks/853.java
### com.thoughtworks.qdox.parser.ParseException: syntax error @[1,1]

error in qdox parser
file content:
```java
offset: 1
uri: file://<WORKSPACE>/data/code-rep-dataset/Dataset2/Tasks/853.java
text:
```scala
i@@f (encloser != null && (encloser.getOwner() instanceof MPackage)) {

// Copyright (c) 1996-99 The Regents of the University of California. All
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

package org.argouml.uml.diagram.static_structure.ui;

import java.awt.*;
import java.awt.event.*;
import java.util.*;
import java.beans.*;
import javax.swing.*;
import javax.swing.plaf.metal.MetalLookAndFeel;

import ru.novosoft.uml.foundation.core.*;
import ru.novosoft.uml.foundation.data_types.*;
import ru.novosoft.uml.model_management.*;

import org.tigris.gef.base.*;
import org.tigris.gef.presentation.*;
import org.tigris.gef.graph.*;

import org.argouml.ui.*;
import org.argouml.uml.*;
import org.argouml.uml.generator.*;
import org.argouml.uml.diagram.ui.*;

/** Class to display graphics for a UML Interface in a diagram. */

public class FigInterface extends FigNodeModelElement {

  ////////////////////////////////////////////////////////////////
  // constants
  public static int OVERLAP = 4;

  ////////////////////////////////////////////////////////////////
  // instance variables
  protected FigRect _bigPort;
  protected FigRect _outline;
  protected FigText _stereoFake;
  protected FigText _oper;
  public MElementResidence resident = new MElementResidenceImpl();

  ////////////////////////////////////////////////////////////////
  // constructors

  public FigInterface() {

//     if (node instanceof MElementImpl)
//       ((MElementImpl)node).addVetoableChangeListener(this);

    _bigPort = new FigRect(8, 8, 92, 64, Color.cyan, Color.cyan);

    _outline = new FigRect(8,8,92,30, Color.black, Color.white);

    _stereoFake = new FigText(10,10,92,15,Color.black, "Times", 10);
    _stereoFake.setExpandOnly(true);
    _stereoFake.setFilled(false);
    _stereoFake.setLineWidth(0);
    _stereoFake.setEditable(false);
    _stereoFake.setText("<<Interface>>");
    _stereoFake.setHeight(15);

    _name.setHeight(18);
    _name.setY(23);
    _name.setWidth(92);
    _name.setLineWidth(0);
    _name.setFilled(false);

    _oper = new FigText(10,40,92,33,Color.black, "Times", 10);
    _oper.setFont(LABEL_FONT);
    _oper.setExpandOnly(true);
    _oper.setJustification(FigText.JUSTIFY_LEFT);

    addFig(_bigPort);
    addFig(_outline);
    addFig(_stereoFake);
    addFig(_name);
    addFig(_oper);
    Rectangle r = getBounds();
    setBounds(r.x, r.y, r.width, r.height);
  }

  public FigInterface(GraphModel gm, Object node) {
    this();
    setOwner(node);
  }

  public String placeString() { return "new Interface"; }

  public Object clone() {
    FigInterface figClone = (FigInterface) super.clone();
    Vector v = figClone.getFigs();
    figClone._bigPort = (FigRect) v.elementAt(0);
    figClone._outline = (FigRect) v.elementAt(1);
    figClone._stereoFake = (FigText) v.elementAt(2);
    figClone._name = (FigText) v.elementAt(3);
    figClone._oper = (FigText) v.elementAt(4);
    return figClone;
  }

  ////////////////////////////////////////////////////////////////
  // Fig implemetation

  public Selection makeSelection() {
    return new SelectionInterface(this);
  }

  public void setOwner(Object node) {
    super.setOwner(node);
    bindPort(node, _bigPort);
  }

  public Dimension getMinimumSize() {
    Dimension stereoMin = _stereoFake.getMinimumSize();
    Dimension nameMin = _name.getMinimumSize();
    Dimension operMin = _oper.getMinimumSize();

    int h = stereoMin.height + nameMin.height - OVERLAP + operMin.height;
    int w = Math.max(stereoMin.width, Math.max(nameMin.width, operMin.width));
    return new Dimension(w, h);
  }

  public void setLineColor(Color c) {
    super.setLineColor(c);
    _stereoFake.setLineWidth(0);
    _name.setLineWidth(0);
  }

  public void setLineWidth(int w) {
    super.setLineWidth(w);
    _stereoFake.setLineWidth(0);
    _name.setLineWidth(0);
  }

  /* Override setBounds to keep shapes looking right */
  public void setBounds(int x, int y, int w, int h) {
    if (_name == null) return;

    Rectangle oldBounds = getBounds();
    Dimension stereoMin = _stereoFake.getMinimumSize();
    Dimension nameMin = _name.getMinimumSize();
    Dimension operMin = _oper.getMinimumSize();

    _outline.setBounds(x, y, w,
		       stereoMin.height + nameMin.height - OVERLAP);
    _stereoFake.setBounds(x+1, y+1, w-2, stereoMin.height);
    _name.setBounds(x+1, y + stereoMin.height - OVERLAP + 1, w-2, nameMin.height);
    _oper.setBounds(x, y + _outline.getBounds().height-1,
		    w, h - _outline.getBounds().height+1);
    _bigPort.setBounds(x+1, y+1, w-2, h-2);

    calcBounds(); //_x = x; _y = y; _w = w; _h = h;
    updateEdges();
    firePropChange("bounds", oldBounds, getBounds());
  }

  public void setEnclosingFig(Fig encloser) {
    super.setEnclosingFig(encloser);
    if (!(getOwner() instanceof MModelElement)) return;
    MModelElement me = (MModelElement) getOwner();
    MNamespace m = null;
    ProjectBrowser pb = ProjectBrowser.TheInstance;
    if (encloser != null && (encloser.getOwner() instanceof MModel)) {
      m = (MNamespace) encloser.getOwner();
    }
    else {
      if (pb.getTarget() instanceof UMLDiagram) {
	m = (MNamespace) ((UMLDiagram)pb.getTarget()).getNamespace();
      }
    }

    // The next if-clause is important for the Deployment-diagram
    // it detects if the enclosing fig is a component, in this case
    // the ImplementationLocation will be set for the owning MInterface
    if (encloser != null && (encloser.getOwner() instanceof MComponentImpl)) {
      MComponent component = (MComponent) encloser.getOwner();
      MInterface in = (MInterface) getOwner();
      resident.setImplementationLocation(component);
      resident.setResident(in);
    }
    else {
      resident.setImplementationLocation(null);
      resident.setResident(null);
    }     
    try {
      me.setNamespace(m);
    }
    catch (Exception e) {
      System.out.println("could not set package");
    }
  }

  ////////////////////////////////////////////////////////////////
  // internal methods

  protected void textEdited(FigText ft) throws PropertyVetoException {
    super.textEdited(ft);
    MClassifier cls = (MClassifier) getOwner();
    if (cls == null) return;
    if (ft == _oper) {
      String s = ft.getText();
      ParserDisplay.SINGLETON.parseOperationCompartment(cls, s);
    }
  }

  protected void modelChanged() {
    super.modelChanged();
    MClassifier cls = (MClassifier) getOwner();
    if (cls == null) return;
    //    String clsNameStr = GeneratorDisplay.Generate(cls.getName());

    Collection behs = MMUtil.SINGLETON.getOperations(cls);
    String operStr = "";
    if (behs != null) {
	Iterator iter = behs.iterator();
      while (iter.hasNext()) {
	    MBehavioralFeature bf = (MBehavioralFeature) iter.next();
	    operStr += GeneratorDisplay.Generate(bf);
	    if (iter.hasNext())
	      operStr += "\n";
      }
    }

    _oper.setText(operStr);

    if (cls.isAbstract()) _name.setFont(ITALIC_LABEL_FONT);
    else _name.setFont(LABEL_FONT);

  }

  static final long serialVersionUID = 4928213949795787107L;

} /* end class FigInterface */


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

QDox parse error in file://<WORKSPACE>/data/code-rep-dataset/Dataset2/Tasks/853.java