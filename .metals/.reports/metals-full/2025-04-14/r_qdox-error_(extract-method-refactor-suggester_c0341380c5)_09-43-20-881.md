error id: file://<WORKSPACE>/data/code-rep-dataset/Dataset5/Tasks/4936.java
file://<WORKSPACE>/data/code-rep-dataset/Dataset5/Tasks/4936.java
### com.thoughtworks.qdox.parser.ParseException: syntax error @[1,7]

error in qdox parser
file content:
```java
offset: 7
uri: file://<WORKSPACE>/data/code-rep-dataset/Dataset5/Tasks/4936.java
text:
```scala
final i@@nt middle = (begin + end) >>> 1;

/*
 * Licensed to the Apache Software Foundation (ASF) under one or more
 * contributor license agreements.  See the NOTICE file distributed with
 * this work for additional information regarding copyright ownership.
 * The ASF licenses this file to You under the Apache License, Version 2.0
 * (the "License"); you may not use this file except in compliance with
 * the License.  You may obtain a copy of the License at
 * 
 *      http://www.apache.org/licenses/LICENSE-2.0
 * 
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */

package org.apache.xerces.impl.dtd;

import org.apache.xerces.util.SymbolTable;
import org.apache.xerces.xni.Augmentations;
import org.apache.xerces.xni.XMLDTDContentModelHandler;
import org.apache.xerces.xni.XNIException;

/**
 * <p>A DTD grammar that produces balanced syntax trees.</p>
 * 
 * @xerces.internal
 * 
 * @author Michael Glavassevich, IBM
 * @version $Id$
 */
final class BalancedDTDGrammar extends DTDGrammar {
    
    //
    // Data
    //
    
    /** Mixed. */
    private boolean fMixed;
    
    /** Stack depth */
    private int fDepth = 0;
    
    /** Children content model operation stack. */
    private short [] fOpStack = null;
    
    /** Holder for choice/sequence/leaf groups at each depth. */
    private int [][] fGroupIndexStack;
    
    /** Sizes of the allocated portions of each int[] in fGroupIndexStack. */
    private int [] fGroupIndexStackSizes;
    
    //
    // Constructors
    //

    /** Default constructor. */
    public BalancedDTDGrammar(SymbolTable symbolTable, XMLDTDDescription desc) {
        super(symbolTable, desc);
    } // BalancedDTDGrammar(SymbolTable,XMLDTDDescription)
    
    //
    // Public methods
    //
    
    /**
     * The start of a content model. Depending on the type of the content
     * model, specific methods may be called between the call to the
     * startContentModel method and the call to the endContentModel method.
     *
     * @param elementName The name of the element.
     * @param augs Additional information that may include infoset
     *                      augmentations.
     * @throws XNIException Thrown by handler to signal an error.
     */
    public final void startContentModel(String elementName, Augmentations augs)
        throws XNIException {
        fDepth = 0;
        initializeContentModelStacks();
        super.startContentModel(elementName, augs);
    } // startContentModel(String)
    
    /**
     * A start of either a mixed or children content model. A mixed
     * content model will immediately be followed by a call to the
     * <code>pcdata()</code> method. A children content model will
     * contain additional groups and/or elements.
     *
     * @param augs Additional information that may include infoset
     *                      augmentations.
     * @throws XNIException Thrown by handler to signal an error.
     *
     * @see #any
     * @see #empty
     */
    public final void startGroup(Augmentations augs) throws XNIException {
        ++fDepth;
        initializeContentModelStacks();
        fMixed = false;
    } // startGroup()
    
    /**
     * The appearance of "#PCDATA" within a group signifying a
     * mixed content model. This method will be the first called
     * following the content model's <code>startGroup()</code>.
     *
     *@param augs Additional information that may include infoset
     *                      augmentations.
     *
     * @throws XNIException Thrown by handler to signal an error.
     *
     * @see #startGroup
     */
    public final void pcdata(Augmentations augs) throws XNIException {
        fMixed = true;
    } // pcdata()

    /**
     * A referenced element in a mixed or children content model.
     *
     * @param elementName The name of the referenced element.
     * @param augs Additional information that may include infoset
     *                      augmentations.
     *
     * @throws XNIException Thrown by handler to signal an error.
     */
    public final void element(String elementName, Augmentations augs) throws XNIException {
        addToCurrentGroup(addUniqueLeafNode(elementName));
    } // element(String)

    /**
     * The separator between choices or sequences of a mixed or children
     * content model.
     *
     * @param separator The type of children separator.
     * @param augs Additional information that may include infoset
     *                      augmentations.
     * @throws XNIException Thrown by handler to signal an error.
     *
     * @see org.apache.xerces.xni.XMLDTDContentModelHandler#SEPARATOR_CHOICE
     * @see org.apache.xerces.xni.XMLDTDContentModelHandler#SEPARATOR_SEQUENCE
     */
    public final void separator(short separator, Augmentations augs) throws XNIException {
        if (separator == XMLDTDContentModelHandler.SEPARATOR_CHOICE) {
            fOpStack[fDepth] = XMLContentSpec.CONTENTSPECNODE_CHOICE;
        }
        else if (separator == XMLDTDContentModelHandler.SEPARATOR_SEQUENCE) {
            fOpStack[fDepth] = XMLContentSpec.CONTENTSPECNODE_SEQ;
        }
    } // separator(short)

    /**
     * The occurrence count for a child in a children content model or
     * for the mixed content model group.
     *
     * @param occurrence The occurrence count for the last element
     *                   or group.
     * @param augs Additional information that may include infoset
     *                      augmentations.
     * @throws XNIException Thrown by handler to signal an error.
     *
     * @see org.apache.xerces.xni.XMLDTDContentModelHandler#OCCURS_ZERO_OR_ONE
     * @see org.apache.xerces.xni.XMLDTDContentModelHandler#OCCURS_ZERO_OR_MORE
     * @see org.apache.xerces.xni.XMLDTDContentModelHandler#OCCURS_ONE_OR_MORE
     */
    public final void occurrence(short occurrence, Augmentations augs) throws XNIException {
        if (!fMixed) {
            int currentIndex = fGroupIndexStackSizes[fDepth] - 1;
            if (occurrence == XMLDTDContentModelHandler.OCCURS_ZERO_OR_ONE) {
                fGroupIndexStack[fDepth][currentIndex] = addContentSpecNode(XMLContentSpec.CONTENTSPECNODE_ZERO_OR_ONE, fGroupIndexStack[fDepth][currentIndex], -1);
            } 
            else if ( occurrence == XMLDTDContentModelHandler.OCCURS_ZERO_OR_MORE) {
                fGroupIndexStack[fDepth][currentIndex] = addContentSpecNode(XMLContentSpec.CONTENTSPECNODE_ZERO_OR_MORE, fGroupIndexStack[fDepth][currentIndex], -1);
            } 
            else if ( occurrence == XMLDTDContentModelHandler.OCCURS_ONE_OR_MORE) {
                fGroupIndexStack[fDepth][currentIndex] = addContentSpecNode(XMLContentSpec.CONTENTSPECNODE_ONE_OR_MORE, fGroupIndexStack[fDepth][currentIndex], -1);
            }
        }
    } // occurrence(short)

    /**
     * The end of a group for mixed or children content models.
     *
     * @param augs Additional information that may include infoset
     *                      augmentations.
     * @throws XNIException Thrown by handler to signal an error.
     */
    public final void endGroup(Augmentations augs) throws XNIException {
        final int length = fGroupIndexStackSizes[fDepth];
        final int group = length > 0 ? addContentSpecNodes(0, length - 1) : addUniqueLeafNode(null);
        --fDepth;
        addToCurrentGroup(group);
    } // endGroup()
    
    /**
     * The end of the DTD.
     *
     * @param augs Additional information that may include infoset
     *                      augmentations.
     * @throws XNIException Thrown by handler to signal an error.
     */
    public final void endDTD(Augmentations augs) throws XNIException {
        super.endDTD(augs);
        fOpStack = null;
        fGroupIndexStack = null;
        fGroupIndexStackSizes = null;
    } // endDTD()
    
    //
    // Protected methods
    //
    
    /**
     * Adds the content spec to the given element declaration.
     */
    protected final void addContentSpecToElement(XMLElementDecl elementDecl) {
        int contentSpec = fGroupIndexStackSizes[0] > 0 ? fGroupIndexStack[0][0] : -1;
        setContentSpecIndex(fCurrentElementIndex, contentSpec);
    }
    
    //
    // Private methods
    //
    
    /**
     * Creates a subtree from the leaf nodes at the current depth.
     */
    private int addContentSpecNodes(int begin, int end) {
        if (begin == end) {
            return fGroupIndexStack[fDepth][begin];
        }
        int middle = (begin + end) / 2;
        return addContentSpecNode(fOpStack[fDepth], 
                addContentSpecNodes(begin, middle), 
                addContentSpecNodes(middle + 1, end));
    } // addContentSpecNodes(int,int)
    
    /**
     * Initialize the stacks which temporarily hold content models.
     */
    private void initializeContentModelStacks() {
        if (fOpStack == null) {
            fOpStack = new short[8];
            fGroupIndexStack = new int [8][];
            fGroupIndexStackSizes = new int [8];
        }
        else if (fDepth == fOpStack.length) {
            short [] newOpStack = new short[fDepth * 2];
            System.arraycopy(fOpStack, 0, newOpStack, 0, fDepth);
            fOpStack = newOpStack;
            int [][] newGroupIndexStack = new int[fDepth * 2][];
            System.arraycopy(fGroupIndexStack, 0, newGroupIndexStack, 0, fDepth);
            fGroupIndexStack = newGroupIndexStack;
            int [] newGroupIndexStackLengths = new int[fDepth * 2];
            System.arraycopy(fGroupIndexStackSizes, 0, newGroupIndexStackLengths, 0, fDepth);
            fGroupIndexStackSizes = newGroupIndexStackLengths;
        }
        fOpStack[fDepth] = -1;
        fGroupIndexStackSizes[fDepth] = 0;
    } // initializeContentModelStacks()
    
    /**
     * Add XMLContentSpec to the current group.
     * 
     * @param contentSpec handle to the XMLContentSpec to add to the current group
     */
    private void addToCurrentGroup(int contentSpec) {
        int [] currentGroup = fGroupIndexStack[fDepth];
        int length = fGroupIndexStackSizes[fDepth]++;
        if (currentGroup == null) {
            currentGroup = new int[8];
            fGroupIndexStack[fDepth] = currentGroup;
        }
        else if (length == currentGroup.length) {
            int [] newGroup = new int[currentGroup.length * 2];
            System.arraycopy(currentGroup, 0, newGroup, 0, currentGroup.length);
            currentGroup = newGroup;
            fGroupIndexStack[fDepth] = currentGroup;
        }
        currentGroup[length] = contentSpec;
    } // addToCurrentGroup(int)

} // class BalancedDTDGrammar
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

QDox parse error in file://<WORKSPACE>/data/code-rep-dataset/Dataset5/Tasks/4936.java