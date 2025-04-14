error id: file://<WORKSPACE>/data/code-rep-dataset/Dataset5/Tasks/14027.java
file://<WORKSPACE>/data/code-rep-dataset/Dataset5/Tasks/14027.java
### com.thoughtworks.qdox.parser.ParseException: syntax error @[1,1]

error in qdox parser
file content:
```java
offset: 1
uri: file://<WORKSPACE>/data/code-rep-dataset/Dataset5/Tasks/14027.java
text:
```scala
r@@eturn (fSelector != null) ? fSelector.toString() : null;

/*
 * Copyright 2001-2004 The Apache Software Foundation.
 * 
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 * 
 *      http://www.apache.org/licenses/LICENSE-2.0
 * 
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */

package org.apache.xerces.impl.xs.identity;

import org.apache.xerces.xs.XSIDCDefinition;
import org.apache.xerces.xs.StringList;
import org.apache.xerces.xs.XSNamespaceItem;
import org.apache.xerces.xs.XSObjectList;
import org.apache.xerces.xs.XSConstants;
import org.apache.xerces.impl.xs.util.StringListImpl;
import org.apache.xerces.impl.xs.util.XSObjectListImpl;
import org.apache.xerces.impl.xs.XSAnnotationImpl;

/**
 * Base class of Schema identity constraint.
 *
 * @xerces.internal 
 *
 * @author Andy Clark, IBM
 * @version $Id$
 */
public abstract class IdentityConstraint implements XSIDCDefinition {

    //
    // Data
    //

    /** type */
    protected short type;

    /** target namespace */
    protected String fNamespace;
    
    /** Identity constraint name. */
    protected String fIdentityConstraintName;

    /** name of owning element */
    protected String fElementName;

    /** Selector. */
    protected Selector fSelector;

    /** Field count. */
    protected int fFieldCount;

    /** Fields. */
    protected Field[] fFields;

    // optional annotations
    protected XSAnnotationImpl [] fAnnotations = null;

    // number of annotations in this identity constraint
    protected int fNumAnnotations;

    //
    // Constructors
    //

    /** Default constructor. */
    protected IdentityConstraint(String namespace, String identityConstraintName, String elemName) {
        fNamespace = namespace;
        fIdentityConstraintName = identityConstraintName;
        fElementName = elemName;
    } // <init>(String,String)

    //
    // Public methods
    //

    /** Returns the identity constraint name. */
    public String getIdentityConstraintName() {
        return fIdentityConstraintName;
    } // getIdentityConstraintName():String

    /** Sets the selector. */
    public void setSelector(Selector selector) {
        fSelector = selector;
    } // setSelector(Selector)

    /** Returns the selector. */
    public Selector getSelector() {
        return fSelector;
    } // getSelector():Selector

    /** Adds a field. */
    public void addField(Field field) {
        if (fFields == null)
            fFields = new Field[4];
        else if (fFieldCount == fFields.length)
            fFields = resize(fFields, fFieldCount*2);
        fFields[fFieldCount++] = field;
    } // addField(Field)

    /** Returns the field count. */
    public int getFieldCount() {
        return fFieldCount;
    } // getFieldCount():int

    /** Returns the field at the specified index. */
    public Field getFieldAt(int index) {
        return fFields[index];
    } // getFieldAt(int):Field

    // get the name of the owning element
    public String getElementName () {
        return fElementName;
    } // getElementName(): String

    //
    // Object methods
    //

    /** Returns a string representation of this object. */
    public String toString() {
        String s = super.toString();
        int index1 = s.lastIndexOf('$');
        if (index1 != -1) {
            return s.substring(index1 + 1);
        }
        int index2 = s.lastIndexOf('.');
        if (index2 != -1) {
            return s.substring(index2 + 1);
        }
        return s;
    } // toString():String

    // equals:  returns true if and only if the String
    // representations of all members of both objects (except for
    // the elenemtName field) are equal.
    public boolean equals(IdentityConstraint id) {
        boolean areEqual = fIdentityConstraintName.equals(id.fIdentityConstraintName);
        if(!areEqual) return false;
        areEqual = fSelector.toString().equals(id.fSelector.toString());
        if(!areEqual) return false;
        areEqual = (fFieldCount == id.fFieldCount);
        if(!areEqual) return false;
        for(int i=0; i<fFieldCount; i++)
            if(!fFields[i].toString().equals(id.fFields[i].toString())) return false;
        return true;
    } // equals

    static final Field[] resize(Field[] oldArray, int newSize) {
        Field[] newArray = new Field[newSize];
        System.arraycopy(oldArray, 0, newArray, 0, oldArray.length);
        return newArray;
    }

    /**
     * Get the type of the object, i.e ELEMENT_DECLARATION.
     */
    public short getType() {
        return XSConstants.IDENTITY_CONSTRAINT;
    }

    /**
     * The <code>name</code> of this <code>XSObject</code> depending on the
     * <code>XSObject</code> type.
     */
    public String getName() {
        return fIdentityConstraintName;
    }

    /**
     * The namespace URI of this node, or <code>null</code> if it is
     * unspecified.  defines how a namespace URI is attached to schema
     * components.
     */
    public String getNamespace() {
        return fNamespace;
    }

    /**
     * {identity-constraint category} One of key, keyref or unique.
     */
    public short getCategory() {
        return type;
    }

    /**
     * {selector} A restricted XPath ([XPath]) expression
     */
    public String getSelectorStr() {
        return fSelector.toString();
    }

    /**
     * {fields} A non-empty list of restricted XPath ([XPath]) expressions.
     */
    public StringList getFieldStrs() {
        String[] strs = new String[fFieldCount];
        for (int i = 0; i < fFieldCount; i++)
            strs[i] = fFields[i].toString();
        return new StringListImpl(strs, fFieldCount);
    }

    /**
     * {referenced key} Required if {identity-constraint category} is keyref,
     * forbidden otherwise. An identity-constraint definition with
     * {identity-constraint category} equal to key or unique.
     */
    public XSIDCDefinition getRefKey() {
        return null;
    }

    /**
     * Optional. Annotation.
     */
    public XSObjectList getAnnotations() {
        return new XSObjectListImpl(fAnnotations, fNumAnnotations);
    }
    
	/**
	 * @see org.apache.xerces.xs.XSObject#getNamespaceItem()
	 */
	public XSNamespaceItem getNamespaceItem() {
        // REVISIT: implement
		return null;
	}

    public void addAnnotation(XSAnnotationImpl annotation) {
        if(annotation == null)
            return;
        if(fAnnotations == null) {
            fAnnotations = new XSAnnotationImpl[2];
        } else if(fNumAnnotations == fAnnotations.length) {
            XSAnnotationImpl[] newArray = new XSAnnotationImpl[fNumAnnotations << 1];
            System.arraycopy(fAnnotations, 0, newArray, 0, fNumAnnotations);
            fAnnotations = newArray;
        }
        fAnnotations[fNumAnnotations++] = annotation;
    }

} // class IdentityConstraint
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

QDox parse error in file://<WORKSPACE>/data/code-rep-dataset/Dataset5/Tasks/14027.java