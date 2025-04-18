error id: file://<WORKSPACE>/data/code-rep-dataset/Dataset2/Tasks/5037.java
file://<WORKSPACE>/data/code-rep-dataset/Dataset2/Tasks/5037.java
### com.thoughtworks.qdox.parser.ParseException: syntax error @[1,1]

error in qdox parser
file content:
```java
offset: 1
uri: file://<WORKSPACE>/data/code-rep-dataset/Dataset2/Tasks/5037.java
text:
```scala
i@@f (!(oTarget instanceof MModelElement)) {

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

package org.argouml.uml.ui;

import ru.novosoft.uml.*;
import ru.novosoft.uml.foundation.core.*;
import ru.novosoft.uml.foundation.data_types.*;

import java.lang.ref.WeakReference;

import java.awt.BorderLayout;

import java.util.List;
import java.util.ArrayList;
import java.util.Iterator;

import javax.swing.event.EventListenerList;

import tudresden.ocl.*;
import tudresden.ocl.gui.*;
import tudresden.ocl.gui.events.*;
import tudresden.ocl.parser.OclParserException;
import tudresden.ocl.check.OclTypeException;

import org.argouml.kernel.*;
import org.argouml.model.uml.UmlFactory;
import org.argouml.ui.*;
import org.argouml.ocl.*;

/**
  * Tab for OCL constraint editing.
  *
  * @author v1.0: Falk Finger
  * @author v2.0: Steffen Zschaler
  */
public class TabConstraints extends TabSpawnable implements TabModelTarget {
  
  /**
    * Adapter to provide information and a manipulation interface for a
    * target element's set of constraints to the constraint editor.
    */
  private static class ConstraintModel implements OCLEditorModel {
    
    /**
      * The target element being edited.
      */
    private MModelElementImpl m_mmeiTarget;
    
    /**
      * A list of all the constraints in m_nmeiTarget. This is necessary to
      * induce a consistent order on the constraints.
      */
    private ArrayList m_alConstraints;
    
    /**
      * List of listeners.
      */
    private EventListenerList m_ellListeners = new EventListenerList();
    
    /**
      * Construct a new ConstraintModel.
      */
    public ConstraintModel (MModelElementImpl mmeiTarget) {
      super();

      m_mmeiTarget = mmeiTarget;
      
      m_alConstraints = new ArrayList (m_mmeiTarget.getConstraints());
    }
    
    /**
      * Return the number of constraints in this model.
      */
    public int getConstraintCount() {
      return m_alConstraints.size();
    }
    
    /**
      * Return the constraint with the specified index.
      *
      * @param nIdx the index of the constraint to be returned.
      *             0 <= nIdx < {@link #getConstraintCount}
      */
    public ConstraintRepresentation getConstraintAt(int nIdx) {
      return representationFor (nIdx);
    }

    /** 
      * Remove the specified constraint from the model.
      *
      * @param nIdx the index of the constraint to be removed.
      *             0 <= nIdx < {@link #getConstraintCount}
      */
    public void removeConstraintAt (int nIdx) {
      if ((nIdx < 0) ||
          (nIdx > m_alConstraints.size())) {
        return;
      }
      
      MConstraint mc = (MConstraint) m_alConstraints.remove (nIdx);
      
      if (mc != null) {
        m_mmeiTarget.removeConstraint (mc);
      }
      
      fireConstraintRemoved (mc, nIdx);
    }
    
    /**
      * Add a fresh constraint to the model.
      */
    public void addConstraint() {
      // null elements represent new constraints, which will be added to the
      // target the first time any actual editing takes place.
      // This is done to ensure syntactical correctness of constraints stored
      // with the target.
      m_alConstraints.add (null);
      
      fireConstraintAdded();
    }
    
    private class CR implements ConstraintRepresentation {
      
      /**
        * The constraint being represented.
        */
      private MConstraint m_mcConstraint;
      
      /**
        * The constraint's index in the list of constraints. Necessary only for
        * new constraints, where m_mcConstraint is still null.
        */
      private int m_nIdx = -1;
      
      public CR (MConstraint mcConstraint, int nIdx) {
        super();
        
        m_mcConstraint = mcConstraint;
        m_nIdx = nIdx;
      }
      
      public CR (int nIdx) {
        this (null, nIdx);
      }
      
      /**
        * Get the name of the constraint.
        */
      public String getName() {
        if (m_mcConstraint == null) {
          return "newConstraint";
        }
        else {
          return m_mcConstraint.getName();
        }
      }
      
      /**
        * Get the constraint's body.
        */
      public String getData() {
        if (m_mcConstraint == null) {
          return OCLUtil.getContextString (m_mmeiTarget);
        }
        else {
          return m_mcConstraint.getBody().getBody();
        }
      }
      
      /**
        * Set the constraint's body text. For the exceptions the detailed message must
        * be human readable.
        *
        * @param sData the new body of the constraint
        *
        * @exception IllegalStateException if the constraint is not in a state to
        *     accept body changes.
        * @exception OclParserException if the specified constraint is not
        *     syntactically correct.
        * @exception OclTypeException if the specified constraint does not adhere by
        *     OCL type rules.
        */
      public void setData(String sData, EditingUtilities euHelper)
          throws IllegalStateException, OclParserException, OclTypeException {
        // Parse and check specified constraint.
        OclTree tree = null;

        MModelElement mmeContext = m_mmeiTarget;
        while (! (mmeContext instanceof MClassifier)) {
          mmeContext = mmeContext.getModelElementContainer();
        }

        try {
          tree = euHelper.parseAndCheckConstraint (sData,
              new ArgoFacade(mmeContext));
        }
        catch (java.io.IOException ioe) {
          // Ignored: Highly unlikely, and what would we do anyway?
          return;
        }

        // Split constraint body, if user wants us to
        if (euHelper.getDoAutoSplit()) {
          List lConstraints = euHelper.splitConstraint (tree);

          if (lConstraints.size() > 0) {
            removeConstraintAt (m_nIdx);

            for (Iterator i = lConstraints.iterator(); i.hasNext();) {
              OclTree ocltCurrent = (OclTree) i.next();

              MConstraint mc = UmlFactory.getFactory().getCore().createConstraint();
              mc.setName (ocltCurrent.getConstraintName());
              mc.setBody (UmlFactory.getFactory().getDataTypes().createBooleanExpression ("OCL",
                  ocltCurrent.getExpression()));

              m_mmeiTarget.addConstraint (mc);

              if (m_mmeiTarget.getNamespace() != null) {
                // Apparently namespace management is not supported for all model
                // elements. As this does not seem to cause problems, I'll just
                // leave it at that for the moment...
                m_mmeiTarget.getNamespace().addOwnedElement (mc);
              }

              m_alConstraints.add (mc);
              fireConstraintAdded();
            }

            return;
          }
        }
        
        // Store constraint body
        MConstraint mcOld = null;
        
        if (m_mcConstraint == null) {
          // New constraint, first time setData is called
          m_mcConstraint = UmlFactory.getFactory().getCore().createConstraint();
          
          m_mcConstraint.setName ("newConstraint");
          m_mcConstraint.setBody (UmlFactory.getFactory().getDataTypes().createBooleanExpression("OCL", sData));
          
          m_mmeiTarget.addConstraint (m_mcConstraint);
          
          if (m_mmeiTarget.getNamespace() != null) {
            // Apparently namespace management is not supported for all model
            // elements. As this does not seem to cause problems, I'll just
            // leave it at that for the moment...
            m_mmeiTarget.getNamespace().addOwnedElement (m_mcConstraint);
          }
          
          m_alConstraints.set (m_nIdx, m_mcConstraint);
        }
        else {
          mcOld = UmlFactory.getFactory().getCore().createConstraint();
          mcOld.setName (m_mcConstraint.getName());
          mcOld.setBody (UmlFactory.getFactory().getDataTypes().createBooleanExpression ("OCL",
                              m_mcConstraint.getBody().getBody()));
          
          m_mcConstraint.setBody (UmlFactory.getFactory().getDataTypes().createBooleanExpression ("OCL", sData));
        }
        
        fireConstraintDataChanged (m_nIdx, mcOld, m_mcConstraint);
      }
      
      /**
        * Set the constraint's name.
        */
      public void setName (final String sName,
                           final EditingUtilities euHelper)
          throws IllegalStateException, IllegalArgumentException {
        if (m_mcConstraint != null) {
          // Check name for consistency with spec
          if (!euHelper.isValidConstraintName (sName)) {
            throw new IllegalArgumentException ("Please specify a valid name.");
          }
          
          // Set name
          MConstraint mcOld = UmlFactory.getFactory().getCore().createConstraint();
          mcOld.setName (m_mcConstraint.getName());
          mcOld.setBody (UmlFactory.getFactory().getDataTypes().createBooleanExpression ("OCL",
                              m_mcConstraint.getBody().getBody()));

          m_mcConstraint.setName (sName);
          
          fireConstraintNameChanged (m_nIdx, mcOld, m_mcConstraint);
          
          // Also set name in constraint body -- Added 03/14/2001
          try {
            OclTree tree = null;

            MModelElement mmeContext = m_mmeiTarget;
            while (! (mmeContext instanceof MClassifier)) {
              mmeContext = mmeContext.getModelElementContainer();
            }

            tree = euHelper.parseAndCheckConstraint (
                m_mcConstraint.getBody().getBody(),
                new ArgoFacade(mmeContext));

            if (tree != null) {
              tree.apply (new tudresden.ocl.parser.analysis.DepthFirstAdapter() {
                int name_ID = 0;
                public void caseAConstraintBody(tudresden.ocl.parser.node.AConstraintBody node) {
                  // replace name
                  if (name_ID == 0) {
                    node.setName (new tudresden.ocl.parser.node.TName (sName));
                  }
                  else {
                    node.setName (new tudresden.ocl.parser.node.TName (sName + "_" + name_ID));
                  }
                  name_ID ++;
                }
              });

              setData (tree.getExpression(), euHelper);
            }
          }
          catch (Throwable t) {
            // OK, so that didn't work out... Just ignore any problems and don't
            // set the name in the constraint body
          }
        }
        else {
          throw new IllegalStateException ("Please define and submit a constraint body first.");
        }
      }
    }
    
    /**
      * Create a representation adapter for the given constraint.
      */
    private CR representationFor (int nIdx) {
      if ((nIdx < 0) ||
          (nIdx >= m_alConstraints.size())) {
        return null;
      }
      
      MConstraint mc = (MConstraint) m_alConstraints.get (nIdx);
      
      if (mc != null) {
        return new CR (mc, nIdx);
      }
      else {
        return new CR (nIdx);        
      }
    }
    
    /**
      * Add a listener to be informed of changes in the model.
      *
      * @param ccl the new listener
      */
    public void addConstraintChangeListener (ConstraintChangeListener ccl) {
      m_ellListeners.add (ConstraintChangeListener.class, ccl);
    }
    
    /**
      * Remove a listener to be informed of changes in the model.
      *
      * @param ccl the listener to be removed
      */
    public void removeConstraintChangeListener (ConstraintChangeListener ccl) {
      m_ellListeners.remove (ConstraintChangeListener.class, ccl);
    }
    
    protected void fireConstraintRemoved (MConstraint mc, int nIdx) {
      // Guaranteed to return a non-null array
      Object[] listeners = m_ellListeners.getListenerList();
    
      ConstraintChangeEvent cce = null;
      
      // Process the listeners last to first, notifying
      // those that are interested in this event
      for (int i = listeners.length - 2; i >= 0; i -= 2) {
        if (listeners[i] == ConstraintChangeListener.class) {
          // Lazily create the event:
          if (cce == null) {
            cce = new ConstraintChangeEvent (this,
                                                nIdx,
                                                new CR (mc, nIdx),
                                                null);
          }
          
          ((ConstraintChangeListener) listeners[i + 1]).constraintRemoved (cce);
        }
      }
    }

    protected void fireConstraintAdded() {
      // Guaranteed to return a non-null array
      Object[] listeners = m_ellListeners.getListenerList();
    
      ConstraintChangeEvent cce = null;
      
      // Process the listeners last to first, notifying
      // those that are interested in this event
      for (int i = listeners.length - 2; i >= 0; i -= 2) {
        if (listeners[i] == ConstraintChangeListener.class) {
          // Lazily create the event:
          if (cce == null) {
            int nIdx = m_alConstraints.size() - 1;
            cce = new ConstraintChangeEvent (this,
                                                nIdx,
                                                null,
                                                representationFor (nIdx));
          }
          
          ((ConstraintChangeListener) listeners[i + 1]).constraintAdded (cce);
        }
      }
    }

    protected void fireConstraintDataChanged (int nIdx,
                                                   MConstraint mcOld,
                                                   MConstraint mcNew) {
      // Guaranteed to return a non-null array
      Object[] listeners = m_ellListeners.getListenerList();
    
      ConstraintChangeEvent cce = null;
      
      // Process the listeners last to first, notifying
      // those that are interested in this event
      for (int i = listeners.length - 2; i >= 0; i -= 2) {
        if (listeners[i] == ConstraintChangeListener.class) {
          // Lazily create the event:
          if (cce == null) {
            cce = new ConstraintChangeEvent (this,
                                                nIdx,
                                                new CR (mcOld, nIdx),
                                                new CR (mcNew, nIdx));
          }
          
          ((ConstraintChangeListener) listeners[i + 1]).constraintDataChanged (cce);
        }
      }
    }
          
    protected void fireConstraintNameChanged (int nIdx,
                                                   MConstraint mcOld,
                                                   MConstraint mcNew) {
      // Guaranteed to return a non-null array
      Object[] listeners = m_ellListeners.getListenerList();
    
      ConstraintChangeEvent cce = null;
      
      // Process the listeners last to first, notifying
      // those that are interested in this event
      for (int i = listeners.length - 2; i >= 0; i -= 2) {
        if (listeners[i] == ConstraintChangeListener.class) {
          // Lazily create the event:
          if (cce == null) {
            cce = new ConstraintChangeEvent (this,
                                                nIdx,
                                                new CR (mcOld, nIdx),
                                                new CR (mcNew, nIdx));
          }
          
          ((ConstraintChangeListener) listeners[i + 1]).constraintNameChanged (cce);
        }
      }
    }
  }
  
  /**
    * The actual editor pane.
    */
  private OCLEditor m_ocleEditor;
  
  /**
    * The current target element.
    */
  private MModelElementImpl m_mmeiTarget;
  
  public TabConstraints() {
    super ("tab.constraints");
    
    setLayout (new BorderLayout (0, 0));
    
    m_ocleEditor = new OCLEditor();
    m_ocleEditor.setOptionMask (OCLEditor.OPTIONMASK_TYPECHECK |
                                  OCLEditor.OPTIONMASK_AUTOSPLIT);
    add (m_ocleEditor);
  }
  
  //TabModelTarget interface methods
  /**
    * Should this tab be activated for the current target element?
    */
  public boolean shouldBeEnabled() {
    return (m_mmeiTarget != null);
  }
  
  /**
    * Get the target element whose properties this tab presents.
    */
  public Object getTarget() {
    return m_mmeiTarget;
  }
  
  /**
    * Refresh the tab because the target has changed.
    */
  public void refresh() {
    setTarget(m_mmeiTarget);
  }
  
  /**
    * Set the target element to be displayed in this tab. Only model elements
    * will be accepted by the constraint tab.
    */
  public void setTarget(Object oTarget) {
    if (!(oTarget instanceof MModelElementImpl)) {
      m_mmeiTarget = null;
      return;
    }

    m_mmeiTarget = (MModelElementImpl) oTarget;
    
    // Set editor's model
    m_ocleEditor.setModel (new ConstraintModel (m_mmeiTarget));
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

QDox parse error in file://<WORKSPACE>/data/code-rep-dataset/Dataset2/Tasks/5037.java