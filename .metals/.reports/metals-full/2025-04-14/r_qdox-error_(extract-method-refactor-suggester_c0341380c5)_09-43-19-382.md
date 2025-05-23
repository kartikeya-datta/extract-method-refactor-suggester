error id: file://<WORKSPACE>/data/code-rep-dataset/Dataset2/Tasks/5404.java
file://<WORKSPACE>/data/code-rep-dataset/Dataset2/Tasks/5404.java
### com.thoughtworks.qdox.parser.ParseException: syntax error @[1,1]

error in qdox parser
file content:
```java
offset: 1
uri: file://<WORKSPACE>/data/code-rep-dataset/Dataset2/Tasks/5404.java
text:
```scala
c@@ls.remove();

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

// 26 Mar 2002: Jeremy Bennett (mail@jeremybennett.com). Minor fixes. Made
// extends/derived scrollpanes have optional scrollbars.


package org.argouml.uml.ui.foundation.core;

import java.awt.Color;
import java.awt.GridLayout;

import javax.swing.ImageIcon;
import javax.swing.JList;
import javax.swing.JPanel;
import javax.swing.JScrollPane;

import org.argouml.application.api.Argo;
import org.argouml.model.uml.UmlFactory;
import org.argouml.ui.ProjectBrowser;
import org.argouml.uml.MMUtil;
import org.argouml.uml.ui.UMLAttributesListModel;
import org.argouml.uml.ui.UMLCheckBox;
import org.argouml.uml.ui.UMLClassifiersListModel;
import org.argouml.uml.ui.UMLClientDependencyListModel;
import org.argouml.uml.ui.UMLConnectionListModel;
import org.argouml.uml.ui.UMLEnumerationBooleanProperty;
import org.argouml.uml.ui.UMLGeneralizationListModel;
import org.argouml.uml.ui.UMLList;
import org.argouml.uml.ui.UMLOperationsListModel;
import org.argouml.uml.ui.UMLReflectionBooleanProperty;
import org.argouml.uml.ui.UMLSpecializationListModel;
import ru.novosoft.uml.MElementListener;
import ru.novosoft.uml.MFactory;
import ru.novosoft.uml.foundation.core.MAssociation;
import ru.novosoft.uml.foundation.core.MAssociationEnd;
import ru.novosoft.uml.foundation.core.MAttribute;
import ru.novosoft.uml.foundation.core.MClassifier;
import ru.novosoft.uml.foundation.core.MGeneralizableElement;
import ru.novosoft.uml.foundation.core.MGeneralization;
import ru.novosoft.uml.foundation.core.MNamespace;
import ru.novosoft.uml.foundation.core.MOperation;
import ru.novosoft.uml.foundation.core.MParameter;
import ru.novosoft.uml.foundation.data_types.MVisibilityKind;
import ru.novosoft.uml.foundation.extension_mechanisms.MStereotype;

abstract public class PropPanelClassifier extends PropPanelNamespace {

    protected JPanel _modifiersPanel;
    protected JScrollPane extendsScroll;
    protected JScrollPane implementsScroll;
    protected JScrollPane derivedScroll;
    protected JScrollPane opsScroll;
    protected JScrollPane attrScroll;
    protected JScrollPane connectScroll;
    protected JScrollPane innerScroll;

  ////////////////////////////////////////////////////////////////
  // contructors
  public PropPanelClassifier(String name, int columns) {
      this(name, null, columns);
  }

  public PropPanelClassifier(String name, ImageIcon icon, int columns) {
      super(name,icon,columns);

      Class mclass = MClassifier.class;

      //
      //   this will cause the components on this page to be notified
      //      anytime a stereotype, namespace, operation, etc
      //      has its name changed or is removed anywhere in the model
      Class[] namesToWatch = { MStereotype.class,MNamespace.class,MOperation.class,
			       MParameter.class,MAttribute.class,MAssociation.class,MClassifier.class };
      setNameEventListening(namesToWatch);

      JList extendsList = new UMLList(new UMLGeneralizationListModel(this,"generalization",true),true);
      extendsList.setBackground(getBackground());
      extendsList.setForeground(Color.blue);
      extendsScroll= new JScrollPane(extendsList,JScrollPane.VERTICAL_SCROLLBAR_AS_NEEDED,JScrollPane.HORIZONTAL_SCROLLBAR_AS_NEEDED);

      JList implementsList = new UMLList(new UMLClientDependencyListModel(this,null,true),true);
      implementsList.setBackground(getBackground());
      implementsList.setForeground(Color.blue);
      implementsScroll= new JScrollPane(implementsList);

      _modifiersPanel = new JPanel(new GridLayout(0,2));
      _modifiersPanel.add(new UMLCheckBox(localize("public"),this,new UMLEnumerationBooleanProperty("visibility",mclass,"getVisibility","setVisibility",MVisibilityKind.class,MVisibilityKind.PUBLIC,null)));
      _modifiersPanel.add(new UMLCheckBox(Argo.localize("UMLMenu", "checkbox.abstract-lc"),this,new UMLReflectionBooleanProperty("isAbstract",mclass,"isAbstract","setAbstract")));
      _modifiersPanel.add(new UMLCheckBox(Argo.localize("UMLMenu", "checkbox.final-lc"),this,new UMLReflectionBooleanProperty("isLeaf",mclass,"isLeaf","setLeaf")));
      _modifiersPanel.add(new UMLCheckBox(localize("root"),this,new UMLReflectionBooleanProperty("isRoot",mclass,"isRoot","setRoot")));

      JList derivedList = new UMLList(new UMLSpecializationListModel(this,"specialization",true),true);
      derivedList.setForeground(Color.blue);
      derivedList.setVisibleRowCount(1);
      derivedScroll=new JScrollPane(derivedList,JScrollPane.VERTICAL_SCROLLBAR_AS_NEEDED,JScrollPane.HORIZONTAL_SCROLLBAR_AS_NEEDED);      

      JList opsList = new UMLList(new UMLOperationsListModel(this,"feature",true),true);
      opsList.setForeground(Color.blue);
      opsList.setVisibleRowCount(1);
      opsScroll = new JScrollPane(opsList);

      JList attrList = new UMLList(new UMLAttributesListModel(this,"feature",true),true);
      attrList.setForeground(Color.blue);
      attrList.setVisibleRowCount(1);
      attrScroll= new JScrollPane(attrList);

      JList connectList = new UMLList(new UMLConnectionListModel(this,null,true),true);
      connectList.setForeground(Color.blue);
      connectList.setVisibleRowCount(1);
      connectScroll= new JScrollPane(connectList);

      JList innerList = new UMLList(new UMLClassifiersListModel(this,"ownedElement",true),true);
      innerList.setForeground(Color.blue);
      innerList.setVisibleRowCount(1);
      innerScroll= new JScrollPane(innerList);

  }


    public void addOperation() {
        Object target = getTarget();
        if(target instanceof MClassifier) {
            MClassifier classifier = (MClassifier) target;
            MOperation oper = UmlFactory.getFactory().getCore().buildOperation(classifier);
            oper.addMElementListener(((MElementListener)ProjectBrowser.TheInstance.getActiveDiagram().presentationFor(classifier)));
            navigateTo(oper);
            // 2002-07-15
            // Jaap Branderhorst
            // Force an update of the navigation pane to solve issue 323
            ProjectBrowser.TheInstance.getNavPane().forceUpdate();
        }
    }

    public void addAttribute() {
        Object target = getTarget();
        if(target instanceof MClassifier) {
	    MClassifier cls = (MClassifier) target;
	    MAttribute attr = UmlFactory.getFactory().getCore().buildAttribute(cls);
	      navigateTo(attr);
          // 2002-07-15
            // Jaap Branderhorst
            // Force an update of the navigation pane to solve issue 323
            ProjectBrowser.TheInstance.getNavPane().forceUpdate();
        }
    }

    public void addAssociation() {
        Object target = getTarget();
        if(target instanceof MClassifier) {
            MClassifier classifier = (MClassifier) target;
            MNamespace ns = classifier.getNamespace();
            if(ns != null) {
                MFactory factory = classifier.getFactory();
                MAssociation newAssociation = factory.createAssociation();
                if(newAssociation != null) {
                    MAssociationEnd end = factory.createAssociationEnd();
                    end.setType(classifier);
                    newAssociation.addConnection(end);
                    end = ns.getFactory().createAssociationEnd();
                    newAssociation.addConnection(end);
                    ns.addOwnedElement(newAssociation);
                    navigateTo(newAssociation);
                }
            }
        }
        // 2002-07-15
        // Jaap Branderhorst
        // Force an update of the navigation pane to solve issue 323
        ProjectBrowser.TheInstance.getNavPane().forceUpdate();
    }

    public void addGeneralization() {
        Object target = getTarget();
        if(target instanceof MGeneralizableElement) {
            MGeneralizableElement genElem = (MGeneralizableElement) target;
            MNamespace ns = genElem.getNamespace();
            if(ns != null) {
                MGeneralization newGen = ns.getFactory().createGeneralization();
                if(newGen != null) {
                    newGen.setChild(genElem);
                    ns.addOwnedElement(newGen);
                    navigateTo(newGen);
                }
            }
        }
        // 2002-07-15
        // Jaap Branderhorst
        // Force an update of the navigation pane to solve issue 323
        ProjectBrowser.TheInstance.getNavPane().forceUpdate();
    }

    public void removeElement() {
	//overrides removeElement in PropPanel
        Object target = getTarget();
        if(target instanceof MClassifier) {
            MClassifier cls = (MClassifier) target;

            Object newTarget = cls.getNamespace();

            UmlFactory.getFactory().getCore().removeClassifier(cls);

            if(newTarget != null) {
                navigateTo(newTarget);
            }
            // 2002-07-15
            // Jaap Branderhorst
            // Force an update of the navigation pane to solve issue 323
            ProjectBrowser.TheInstance.getNavPane().forceUpdate();
        }
    }

    protected boolean isAcceptibleBaseMetaClass(String baseClass) {
        return baseClass.equals("Classifier") ||
            baseClass.equals("GeneralizableElement");
    }


} /* end class PropPanelClassifier */
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

QDox parse error in file://<WORKSPACE>/data/code-rep-dataset/Dataset2/Tasks/5404.java