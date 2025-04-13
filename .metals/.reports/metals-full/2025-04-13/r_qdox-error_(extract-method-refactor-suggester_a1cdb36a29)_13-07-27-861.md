error id: file://<WORKSPACE>/data/code-rep-dataset/Dataset2/Tasks/1632.java
file://<WORKSPACE>/data/code-rep-dataset/Dataset2/Tasks/1632.java
### com.thoughtworks.qdox.parser.ParseException: syntax error @[1,8]

error in qdox parser
file content:
```java
offset: 8
uri: file://<WORKSPACE>/data/code-rep-dataset/Dataset2/Tasks/1632.java
text:
```scala
public b@@oolean isValidStereoType(Object m, MStereotype stereo) {

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

package org.argouml.model.uml.foundation.extensionmechanisms;

import java.util.ArrayList;
import java.util.Collection;
import java.util.Iterator;
import java.util.List;

import org.argouml.kernel.Project;
import org.argouml.kernel.ProjectManager;
import org.argouml.ui.ProjectBrowser;

import ru.novosoft.uml.MFactory;
import ru.novosoft.uml.foundation.core.MModelElement;
import ru.novosoft.uml.foundation.core.MNamespace;
import ru.novosoft.uml.foundation.extension_mechanisms.MStereotype;
import ru.novosoft.uml.foundation.extension_mechanisms.MTaggedValue;
import ru.novosoft.uml.model_management.MModel;

/**
 * Helper class for UML Foundation::ExtensionMechanisms Package.
 *
 * Current implementation is a placeholder.
 *
 * @since ARGO0.11.2
 * @author Thierry Lach
 * @stereotype singleton
 */
public class ExtensionMechanismsHelper {

    /** Don't allow instantiation.
     */
    private ExtensionMechanismsHelper() {
    }

     /** Singleton instance.
     */
    private static ExtensionMechanismsHelper SINGLETON =
                   new ExtensionMechanismsHelper();


    /** Singleton instance access method.
     */
    public static ExtensionMechanismsHelper getHelper() {
        return SINGLETON;
    }

    /**
     * Returns all stereotypes in some namespace
     */
    public Collection getStereotypes(MNamespace ns) {
        List l = new ArrayList();
        if (ns == null) return l;
    	Iterator it = ns.getOwnedElements().iterator();
    	while (it.hasNext()) {
    		Object o = it.next();
    		if (o instanceof MStereotype) {
    			l.add(o);
    		}
    	}
    	return l;
    }

    /**
     * Returns all stereotypes in some model
     * @param ns
     * @return Collection The stereotypes found. An empty arraylist is returned
     * if nothing is found.
     */
    public Collection getStereotypes(MModel ns) {
        List l = new ArrayList();
        if (ns == null) return l;
        Iterator it = ns.getOwnedElements().iterator();
        while (it.hasNext()) {
            Object o = it.next();
            if (o instanceof MStereotype) {
                l.add(o);
            }
        }
        return l;
    }

    /**
     * Finds a stereotype in some namespace. Returns null if no such stereotype is found.
     */
    public MStereotype getStereotype(MNamespace ns, MStereotype stereo) {
    	String name = stereo.getName();
    	String baseClass = stereo.getBaseClass();
    	Iterator it = getStereotypes(ns).iterator();
    	while (it.hasNext()) {
    		Object o = it.next();
    		if (o instanceof MStereotype &&
                ((MStereotype)o).getName() != null &&
    			((MStereotype)o).getName().equals(name) &&
                ((MStereotype)o).getBaseClass() != null &&
    			((MStereotype)o).getBaseClass().equals(baseClass)) {
    			return (MStereotype)o;
    		}
    	}
    	return null;
    }

    /**
     * Searches the given stereotype in all models in the current project.
     * @param stereo
     * @return MStereotype
     */
    public MStereotype getStereotype(MStereotype stereo) {
        if (stereo == null) return null;
        String name = stereo.getName();
        String baseClass = stereo.getBaseClass();
        if (name == null || baseClass == null) return null;
        ProjectBrowser pb = ProjectBrowser.TheInstance;
        Iterator it2 = ProjectManager.getManager().getCurrentProject().getModels().iterator();
        while (it2.hasNext()) {
            MModel model = (MModel)it2.next();
            Iterator it = getStereotypes(model).iterator();
            while (it.hasNext()) {
                Object o = it.next();
                if (o instanceof MStereotype &&
                    ((MStereotype)o).getName().equals(name) &&
                    ((MStereotype)o).getBaseClass().equals(baseClass)) {
                    return (MStereotype)o;
                }
            }
        }
        return null;
    }

    public String getMetaModelName(MModelElement m) {
        if (m == null) return null;
        return getMetaModelName(m.getClass());
    }

    protected String getMetaModelName(Class clazz) {
        if (clazz == null) return null;
        String name = clazz.getName();
        name = name.substring(name.lastIndexOf('.')+2,name.length());
        if (name.endsWith("Impl")) {
            name = name.substring(0,name.lastIndexOf("Impl"));
        }
        return name;
    }

    /**
     * Returns all possible stereotypes for some modelelement. Possible stereotypes
     * are those stereotypes that are owned by the same namespace the modelelement
     * is owned by and that have a baseclass that is the same as the metamodelelement
     * name of the modelelement.
     * @param m
     * @return Collection
     */
    public Collection getAllPossibleStereotypes(MModelElement m) {
        List ret = new ArrayList();
        if (m == null) return ret;
        Iterator it = getStereotypes().iterator();
        String baseClass = getMetaModelName(m);
        while (it.hasNext()) {
            MStereotype stereo = (MStereotype)it.next();
            if (isValidStereoType(m.getClass(), stereo)) {
                ret.add(stereo);
            }
        }
        return ret;
    }

    protected boolean isValidStereoType(Class clazz, MStereotype stereo) {
        if (clazz == null || stereo == null) return false;
        if (getMetaModelName(clazz).equals(stereo.getBaseClass()))
            return true;
        else {
            if (getMetaModelName(clazz).equals("ModelElement")) return false;
            return isValidStereoType(clazz.getSuperclass(), stereo);
        }
    }

    /**
     * Returns true if the given stereotype has a baseclass that equals the baseclass
     * of the given modelelement or one of the superclasses of the given modelelement.
     * @param m
     * @param stereo
     * @return boolean
     */
    public boolean isValidStereoType(MModelElement m, MStereotype stereo) {
        if (m == null) return false;
       return isValidStereoType(m.getClass(), stereo);
    }

    public Collection getStereotypes() {
        List ret = new ArrayList();
        Project p = ProjectManager.getManager().getCurrentProject();
        Iterator it = p.getModels().iterator();
        while (it.hasNext()) {
            MModel model = (MModel)it.next();
            ret.addAll(getStereotypes(model));
        }
        return ret;
    }

    /**
     * Sets the stereotype of some modelelement. The method also copies a
     * stereotype that is not a part of the current model to the current model.
     * @param m
     * @param stereo
     */
    public void setStereoType(MModelElement m, MStereotype stereo) {
        if (stereo != null && m.getModel() != stereo.getModel()) {
            stereo.setNamespace(m.getModel());
        }
        m.setStereotype(stereo);
    }

    /**
     * Sets a tagged value of some modelelement.
     * @param model element
     * @param tag
     * @param value
     */
    public void setTaggedValue(Object o, String tag, String value) {
        if (o != null && o instanceof MModelElement) {
		    MTaggedValue tv = MFactory.getDefaultFactory().createTaggedValue();
		    tv.setModelElement((MModelElement)o);
		    tv.setTag(tag);
		    tv.setValue(value);
        }
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

QDox parse error in file://<WORKSPACE>/data/code-rep-dataset/Dataset2/Tasks/1632.java