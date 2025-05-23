error id: file://<WORKSPACE>/data/code-rep-dataset/Dataset2/Tasks/7469.java
file://<WORKSPACE>/data/code-rep-dataset/Dataset2/Tasks/7469.java
### com.thoughtworks.qdox.parser.ParseException: syntax error @[1,1]

error in qdox parser
file content:
```java
offset: 1
uri: file://<WORKSPACE>/data/code-rep-dataset/Dataset2/Tasks/7469.java
text:
```scala
a@@ddField(Argo.localize("UMLMenu", "label.namespace-visibility"), getNamespaceVisibilityPanel());

// Copyright (c) 1996-2001 The Regents of the University of California. All
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

// 21 Mar 2002: Jeremy Bennett (mail@jeremybennett.com). Changed to use the
// labels "Generalizes:" and "Specializes:" for inheritance.

// 4 Apr 2002: Jeremy Bennett (mail@jeremybennett.com). Labels corrected to
// "Generalizations:" and "Specializations".


package org.argouml.uml.ui.foundation.core;

import javax.swing.JScrollPane;
import javax.swing.JList;

import org.argouml.application.api.Argo;
import org.argouml.model.uml.foundation.core.CoreFactory;
import org.argouml.swingext.LabelledLayout;
import org.argouml.uml.ui.PropPanelButton;
import org.argouml.uml.ui.UMLCheckBox;
import org.argouml.uml.ui.UMLComboBoxNavigator;
import org.argouml.uml.ui.UMLReflectionBooleanProperty;
import org.argouml.uml.ui.UMLLinkedList;
import org.argouml.util.ConfigLoader;

import ru.novosoft.uml.foundation.core.MClass;
import ru.novosoft.uml.foundation.core.MClassifier;
import ru.novosoft.uml.foundation.core.MNamespace;

public class PropPanelClass extends PropPanelClassifier {

    private JScrollPane _attributeScroll;
    private JScrollPane _operationScroll;
    
    private static UMLClassAttributeListModel attributeListModel =
        new UMLClassAttributeListModel();
    
    private static UMLClassOperationListModel operationListModel =
        new UMLClassOperationListModel();
    
  ////////////////////////////////////////////////////////////////
  // contructors
  public PropPanelClass() { 
    super("Class", ConfigLoader.getTabPropsOrientation());
    Class mclass = MClass.class;

    addField(Argo.localize("UMLMenu", "label.name"), getNameTextField());
    addField(Argo.localize("UMLMenu", "label.stereotype"), new UMLComboBoxNavigator(this, Argo.localize("UMLMenu", "tooltip.nav-stereo"),getStereotypeBox()));
    addField(Argo.localize("UMLMenu", "label.namespace"), getNamespaceComboBox());
    _modifiersPanel.add(new UMLCheckBox(Argo.localize("UMLMenu", "checkbox.active-uc"),this,new UMLReflectionBooleanProperty("isActive",mclass,"isActive","setActive")));
    addField(Argo.localize("UMLMenu", "label.modifiers"), _modifiersPanel);
    // addField(Argo.localize("UMLMenu", "label.namespace-visibility"), namespaceVisibilitypanel);
    
    add(LabelledLayout.getSeperator());
    
    addField(Argo.localize("UMLMenu", "label.dependency"), getClientDependencyScroll());
    addField(Argo.localize("UMLMenu", "label.generalizations"), getGeneralizationScroll());
    addField(Argo.localize("UMLMenu", "label.specializations"), getSpecializationScroll());
    
    add(LabelledLayout.getSeperator());
     
    addField(Argo.localize("UMLMenu", "label.attributes"), getAttributeScroll());
    addField(Argo.localize("UMLMenu", "label.association-ends"), getAssociationEndScroll());
    addField(Argo.localize("UMLMenu", "label.operations"), getOperationScroll());
    addField(Argo.localize("UMLMenu", "label.owned-elements"), getOwnedElementsScroll());
    
  
    new PropPanelButton(this,buttonPanel,_navUpIcon, Argo.localize("UMLMenu", "button.go-up"),"navigateUp",null);
    new PropPanelButton(this,buttonPanel,_navBackIcon, Argo.localize("UMLMenu", "button.go-back"),"navigateBackAction","isNavigateBackEnabled");
    new PropPanelButton(this,buttonPanel,_navForwardIcon, Argo.localize("UMLMenu", "button.go-forward"),"navigateForwardAction","isNavigateForwardEnabled");
    new PropPanelButton(this,buttonPanel,_addAttrIcon, Argo.localize("UMLMenu", "button.add-attribute"),"addAttribute",null);
    new PropPanelButton(this,buttonPanel,_addOpIcon, Argo.localize("UMLMenu", "button.add-operation"),"addOperation",null);
    new PropPanelButton(this,buttonPanel,_innerClassIcon, Argo.localize("UMLMenu", "button.add-inner-class"),"addInnerClass",null);
    new PropPanelButton(this,buttonPanel,_classIcon, Argo.localize("UMLMenu", "button.add-new-class"),"newClass",null);
    new PropPanelButton(this,buttonPanel,_deleteIcon, Argo.localize("UMLMenu", "button.delete-class"),"removeElement",null);

  }

    public void addInnerClass() {
        Object target = getTarget();
        if(target instanceof MClassifier) {
            MClassifier classifier = (MClassifier) target;
            MClass inner = CoreFactory.getFactory().buildClass(classifier);
            navigateTo(inner);
        }
    }

    public void newClass() {
        Object target = getTarget();
        if(target instanceof MClassifier) {
            MClassifier classifier = (MClassifier) target;
            MNamespace ns = classifier.getNamespace();
            if(ns != null) {
                MClassifier peer = CoreFactory.getFactory().buildClass(ns);
                navigateTo(peer);
            }
        }
       
    }

    /**
     * Returns the operationScroll.
     * @return JScrollPane
     */
    public JScrollPane getOperationScroll() {
        if (_operationScroll == null) {
            JList list = new UMLLinkedList(operationListModel);
            _operationScroll = new JScrollPane(list);
        }
        return _operationScroll;
    }

    /**
     * Returns the attributeScroll.
     * @return JScrollPane
     */
    public JScrollPane getAttributeScroll() {
        if (_attributeScroll == null) {
            JList list = new UMLLinkedList(attributeListModel);
            _attributeScroll = new JScrollPane(list);
        }
        return _attributeScroll;
    }
    

} /* end class PropPanelClass */
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

QDox parse error in file://<WORKSPACE>/data/code-rep-dataset/Dataset2/Tasks/7469.java