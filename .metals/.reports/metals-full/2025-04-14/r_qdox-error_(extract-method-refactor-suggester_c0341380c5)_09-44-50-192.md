error id: file://<WORKSPACE>/data/code-rep-dataset/Dataset2/Tasks/9558.java
file://<WORKSPACE>/data/code-rep-dataset/Dataset2/Tasks/9558.java
### com.thoughtworks.qdox.parser.ParseException: syntax error @[1,1]

error in qdox parser
file content:
```java
offset: 1
uri: file://<WORKSPACE>/data/code-rep-dataset/Dataset2/Tasks/9558.java
text:
```scala
a@@ddField(Argo.localize("UMLMenu", "label.namespace"),namespaceComboBox);

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


// 21 Mar 2002: Jeremy Bennett (mail@jeremybennett.com). Changed to use the
// labels "Generalizes:" and "Specializes:" for inheritance.

// 4 Apr 2002: Jeremy Bennett (mail@jeremybennett.com). Labels corrected to
// "Generalizations:" and "Specializations".

package org.argouml.uml.ui.foundation.core;

import java.awt.*;
import java.util.*;
import javax.swing.*;
import org.argouml.swingext.*;
import org.argouml.util.*;

import ru.novosoft.uml.*;
import ru.novosoft.uml.foundation.core.*;
import ru.novosoft.uml.foundation.data_types.*;
import ru.novosoft.uml.foundation.extension_mechanisms.*;
import ru.novosoft.uml.model_management.*;

import org.argouml.application.api.*;
import org.argouml.ui.ProjectBrowser;
import org.argouml.uml.ui.*;

public class PropPanelAssociation extends PropPanelModelElement {


  ////////////////////////////////////////////////////////////////
  // contructors
    /*
  public PropPanelAssociation() {
    super("Association",_associationIcon,2);

    Class mclass = MAssociation.class;

    //
    //   this will cause the components on this page to be notified
    //      anytime a stereotype, namespace, operation, etc
    //      has its name changed or is removed anywhere in the model
    Class[] namesToWatch = { MStereotype.class,MNamespace.class,MClassifier.class };
    setNameEventListening(namesToWatch);

    addField(Argo.localize("UMLMenu", "label.name"), nameField);
    
    add(Argo.localize("UMLMenu", "label.name"), nameField);
    addCaption(Argo.localize("UMLMenu", "label.name"),1,0,0);
    addField(nameField,1,0,0);

    addCaption(Argo.localize("UMLMenu", "label.stereotype"),2,0,0);
    addField(new UMLComboBoxNavigator(this, Argo.localize("UMLMenu", "tooltip.nav-stereo"),stereotypeBox),2,0,0);

    addCaption(Argo.localize("UMLMenu", "label.namespace"),3,0,0);
    addLinkField(namespaceScroll,3,0,0);

    addCaption(Argo.localize("UMLMenu", "label.modifiers"),4,0,1);

    JPanel modifiersPanel = new JPanel(new GridLayout(0,3));
    modifiersPanel.add(new UMLCheckBox(Argo.localize("UMLMenu", "checkbox.abstract-uc"),this,new UMLReflectionBooleanProperty("isAbstract",mclass,"isAbstract","setAbstract")));
    modifiersPanel.add(new UMLCheckBox(Argo.localize("UMLMenu", "checkbox.final-uc"),this,new UMLReflectionBooleanProperty("isLeaf",mclass,"isLeaf","setLeaf")));
    modifiersPanel.add(new UMLCheckBox(localize("Root"),this,new UMLReflectionBooleanProperty("isRoot",mclass,"isRoot","setRoot")));
    addField(modifiersPanel,4,0,0);


    addCaption(Argo.localize("UMLMenu", "label.association-ends"),0,1,0.25);
    JList assocEndList = new UMLList(new UMLAssociationEndListModel(this,"connection",true),true);
    assocEndList.setBackground(getBackground());
    assocEndList.setForeground(Color.blue);
    addField(new JScrollPane(assocEndList),0,1,0.25);

    addCaption("Generalizations:",1,1,0);
    JList extendsList = new UMLList(new UMLGeneralizationListModel(this,"generalization",true),true);
    JScrollPane extendsScroll=new JScrollPane(extendsList,JScrollPane.VERTICAL_SCROLLBAR_NEVER,JScrollPane.HORIZONTAL_SCROLLBAR_NEVER);
    addLinkField(extendsScroll,1,1,0);

    addCaption("Specializations:",2,1,1);
    JList derivedList = new UMLList(new UMLSpecializationListModel(this,null,true),true);
    derivedList.setForeground(Color.blue);
    derivedList.setVisibleRowCount(1);
    derivedList.setFont(smallFont);
    JScrollPane derivedScroll=new JScrollPane(derivedList,JScrollPane.VERTICAL_SCROLLBAR_NEVER,JScrollPane.HORIZONTAL_SCROLLBAR_NEVER);
    addField(derivedScroll,2,1,0);

    new PropPanelButton(this,buttonPanel,_navUpIcon, Argo.localize("UMLMenu", "button.go-up"),"navigateNamespace",null);
    new PropPanelButton(this,buttonPanel,_navBackIcon, Argo.localize("UMLMenu", "button.go-back"),"navigateBackAction","isNavigateBackEnabled");
    new PropPanelButton(this,buttonPanel,_navForwardIcon, Argo.localize("UMLMenu", "button.go-forward"),"navigateForwardAction","isNavigateForwardEnabled");
    new PropPanelButton(this,buttonPanel,_deleteIcon, Argo.localize("UMLMenu", "button.delete-association"),"removeElement",null);
    //does this make sense??new PropPanelButton(this,buttonPanel,_generalizationIcon, Argo.localize("UMLMenu", "button.add-generalization"),"addGeneralization",null);
    //does this make sense??new PropPanelButton(this,buttonPanel,_realizationIcon, Argo.localize("UMLMenu", "button.add-realization"),"addRealization",null);
    //does this make sense??new PropPanelButton(this,buttonPanel,_associationIcon, Argo.localize("UMLMenu", "button.add-association"),"newAssociation",null);

  }
*/
  public PropPanelAssociation() {
    super("Association",_associationIcon, ConfigLoader.getTabPropsOrientation());

    Class mclass = MAssociation.class;

    //
    //   this will cause the components on this page to be notified
    //      anytime a stereotype, namespace, operation, etc
    //      has its name changed or is removed anywhere in the model
    Class[] namesToWatch = { MStereotype.class,MNamespace.class,MClassifier.class };
    setNameEventListening(namesToWatch);

    addField(Argo.localize("UMLMenu", "label.name"), nameField);
    addField(Argo.localize("UMLMenu", "label.stereotype"), new UMLComboBoxNavigator(this, Argo.localize("UMLMenu", "tooltip.nav-stereo"),stereotypeBox));
    
    addField(Argo.localize("UMLMenu", "label.namespace"),namespaceScroll);

    JPanel modifiersPanel = new JPanel(new GridLayout2(0,3,GridLayout2.ROWCOLPREFERRED));
    modifiersPanel.add(new UMLCheckBox(Argo.localize("UMLMenu", "checkbox.abstract-uc"),this,new UMLReflectionBooleanProperty("isAbstract",mclass,"isAbstract","setAbstract")));
    modifiersPanel.add(new UMLCheckBox(Argo.localize("UMLMenu", "checkbox.final-uc"),this,new UMLReflectionBooleanProperty("isLeaf",mclass,"isLeaf","setLeaf")));
    modifiersPanel.add(new UMLCheckBox(Argo.localize("UMLMenu", "checkbox.root-uc"),this,new UMLReflectionBooleanProperty("isRoot",mclass,"isRoot","setRoot")));
    addField(Argo.localize("UMLMenu", "label.modifiers"),modifiersPanel);

    add(LabelledLayout.getSeperator());
    
    JList assocEndList = new UMLList(new UMLAssociationEndListModel(this,"connection",true),true);
    assocEndList.setBackground(getBackground());
    assocEndList.setForeground(Color.blue);
    addField(Argo.localize("UMLMenu", "label.association-ends"),new JScrollPane(assocEndList));

    JList extendsList = new UMLList(new UMLGeneralizationListModel(this,"generalization",true),true);
    extendsList.setVisibleRowCount(1);
    JScrollPane extendsScroll=new JScrollPane(extendsList,JScrollPane.VERTICAL_SCROLLBAR_NEVER,JScrollPane.HORIZONTAL_SCROLLBAR_NEVER);
    addLinkField(Argo.localize("UMLMenu", "label.generalizations"), extendsScroll);

    JList derivedList = new UMLList(new UMLSpecializationListModel(this,null,true),true);
    derivedList.setForeground(Color.blue);
    derivedList.setVisibleRowCount(1);
    derivedList.setFont(smallFont);
    JScrollPane derivedScroll=new JScrollPane(derivedList,JScrollPane.VERTICAL_SCROLLBAR_NEVER,JScrollPane.HORIZONTAL_SCROLLBAR_NEVER);
    addField(Argo.localize("UMLMenu", "label.specializations"),derivedScroll);

    new PropPanelButton(this,buttonPanel,_navUpIcon, Argo.localize("UMLMenu", "button.go-up"),"navigateNamespace",null);
    new PropPanelButton(this,buttonPanel,_navBackIcon, Argo.localize("UMLMenu", "button.go-back"),"navigateBackAction","isNavigateBackEnabled");
    new PropPanelButton(this,buttonPanel,_navForwardIcon, Argo.localize("UMLMenu", "button.go-forward"),"navigateForwardAction","isNavigateForwardEnabled");
    new PropPanelButton(this,buttonPanel,_deleteIcon, Argo.localize("UMLMenu", "button.delete-association"),"removeElement",null);
    //does this make sense??new PropPanelButton(this,buttonPanel,_generalizationIcon, Argo.localize("UMLMenu", "button.add-generalization"),"addGeneralization",null);
    //does this make sense??new PropPanelButton(this,buttonPanel,_realizationIcon, Argo.localize("UMLMenu", "button.add-realization"),"addRealization",null);
    //does this make sense??new PropPanelButton(this,buttonPanel,_associationIcon, Argo.localize("UMLMenu", "button.add-association"),"newAssociation",null);

  }
    
    protected boolean isAcceptibleBaseMetaClass(String baseClass) {
        return baseClass.equals("Association") ||
          baseClass.equals("ModelElement");
    }



} /* end class PropPanelAssociation */
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

QDox parse error in file://<WORKSPACE>/data/code-rep-dataset/Dataset2/Tasks/9558.java