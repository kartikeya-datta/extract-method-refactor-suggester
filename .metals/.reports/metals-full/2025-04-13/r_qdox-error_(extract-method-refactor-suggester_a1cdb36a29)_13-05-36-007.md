error id: file://<WORKSPACE>/data/code-rep-dataset/Dataset5/Tasks/6360.java
file://<WORKSPACE>/data/code-rep-dataset/Dataset5/Tasks/6360.java
### com.thoughtworks.qdox.parser.ParseException: syntax error @[1,1]

error in qdox parser
file content:
```java
offset: 1
uri: file://<WORKSPACE>/data/code-rep-dataset/Dataset5/Tasks/6360.java
text:
```scala
r@@eturn fIsFragmentResolved && (fMatchingChildCount > 0);

/*
 * Copyright 2005 The Apache Software Foundation.
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
package org.apache.xerces.xpointer;

import org.apache.xerces.impl.Constants;
import org.apache.xerces.impl.dv.XSSimpleType;
import org.apache.xerces.util.SymbolTable;
import org.apache.xerces.xni.Augmentations;
import org.apache.xerces.xni.QName;
import org.apache.xerces.xni.XMLAttributes;
import org.apache.xerces.xni.XNIException;
import org.apache.xerces.xs.AttributePSVI;
import org.apache.xerces.xs.XSTypeDefinition;

/**
 * <p>
 * Implements the XPointerPart interface and handles processing of
 * ShortHand Pointers.  It identifies at most one element in the 
 * resource's information set; specifically, the first one (if any) 
 * in document order that has a matching NCName as an identifier.
 * </p>
 *
 * @version $Id$
 *
 */
class ShortHandPointer implements XPointerPart {
    
    // The name of the ShortHand pointer
    private String fShortHandPointer;
    
    // The name of the ShortHand pointer
    private boolean fIsFragmentResolved = false;
    
    // SymbolTable
    private SymbolTable fSymbolTable;
    
    //
    // Constructors
    //
    public ShortHandPointer() {
    }
    
    public ShortHandPointer(SymbolTable symbolTable) {
        fSymbolTable = symbolTable;
    }
    
    /**
     * The XPointerProcessor takes care of this.  Simply set the ShortHand Pointer here.
     * 
     * @see org.apache.xerces.xpointer.XPointerPart#parseXPointer(java.lang.String)
     */
    public void parseXPointer(String part) throws XNIException {
        fShortHandPointer = part;
        // reset fIsFragmentResolved
        fIsFragmentResolved = false;
    }
    
    /**
     * Resolves the XPointer ShortHand pointer based on the rules defined in 
     * Section 3.2 of the XPointer Framework Recommendation.
     * Note that in the current implementation only supports DTD determined ID's. 
     *
     * @see org.apache.xerces.xpointer.XPointerPart#resolveXPointer(org.apache.xerces.xni.QName, org.apache.xerces.xni.XMLAttributes, org.apache.xerces.xni.Augmentations, int event)
     */
    int fMatchingChildCount = 0;
    public boolean resolveXPointer(QName element, XMLAttributes attributes,
            Augmentations augs, int event) throws XNIException {

        // reset fIsFragmentResolved
        if (fMatchingChildCount == 0) {
            fIsFragmentResolved = false;
        }

        // On startElement or emptyElement, if no matching elements or parent 
        // elements were found, check for a matching idenfitier.
        if (event == XPointerPart.EVENT_ELEMENT_START) {
            if (fMatchingChildCount == 0) {
                fIsFragmentResolved = hasMatchingIdentifier(element, attributes, augs,
                    event);
            }
            if (fIsFragmentResolved) {
               fMatchingChildCount++;
            }
        } else if (event == XPointerPart.EVENT_ELEMENT_EMPTY) {
            if (fMatchingChildCount == 0) {
                fIsFragmentResolved = hasMatchingIdentifier(element, attributes, augs,
                    event);
            }
        }
        else {
            // On endElement, decrease the matching child count if the child or
            // its parent was resolved.
            if (fIsFragmentResolved) {
                fMatchingChildCount--;
            }
        }
        
        return fIsFragmentResolved ;
    }
    
    /**
     * 
     * @param element
     * @param attributes
     * @param augs
     * @param event
     * @return
     * @throws XNIException
     */
    private boolean hasMatchingIdentifier(QName element,
            XMLAttributes attributes, Augmentations augs, int event)
    throws XNIException {
        String normalizedValue = null;
        
        // The identifiers of an element are determined by the 
        // ShortHand Pointer as follows:
        
        if (attributes != null) {
            for (int i = 0; i < attributes.getLength(); i++) {
                
                // 1. If an element information item has an attribute information item 
                // among its [attributes] that is a schema-determined ID, then it is 
                // identified by the value of that attribute information item's 
                // [schema normalized value] property;
                normalizedValue = getSchemaDeterminedID(attributes, i);
                if (normalizedValue != null) {
                    break;
                }
                
                // 2. If an element information item has an element information item among 
                // its [children] that is a schema-determined ID, then it is identified by 
                // the value of that element information item's [schema normalized value] property;
                // ???
                normalizedValue = getChildrenSchemaDeterminedID(attributes, i);
                if (normalizedValue != null) {
                    break;
                }
                
                // 3. If an element information item has an attribute information item among 
                // its [attributes] that is a DTD-determined ID, then it is identified by the 
                // value of that attribute information item's [normalized value] property.
                // An attribute information item is a DTD-determined ID if and only if it has 
                // a [type definition] property whose value is equal to ID.
                normalizedValue = getDTDDeterminedID(attributes, i);
                if (normalizedValue != null) {
                    break;
                }
                // 4. No externally determined ID's
            }
        }
        
        if (normalizedValue != null
                && normalizedValue.equals(fShortHandPointer)) {
            return true;
        }
        
        return false;
    }
    
    /**
     * Rerturns the DTD determine-ID
     * 
     * @param attributes
     * @param index
     * @return String 
     * @throws XNIException
     */
    public String getDTDDeterminedID(XMLAttributes attributes, int index)
    throws XNIException {
        
        if (attributes.getType(index).equals("ID")) {
            return attributes.getValue(index);
        }
        return null;
    }
    
    /**
     * Returns the schema-determined-ID.
     * 
     * 
     * @param attributes
     * @param index
     * @return A String containing the schema-determined ID. 
     * @throws XNIException
     */
    public String getSchemaDeterminedID(XMLAttributes attributes, int index)
    throws XNIException {
        Augmentations augs = attributes.getAugmentations(index);
        AttributePSVI attrPSVI = (AttributePSVI) augs
        .getItem(Constants.ATTRIBUTE_PSVI);
        
        if (attrPSVI != null) {
            // An element or attribute information item is a schema-determined 
            // ID if and only if one of the following is true:]
            
            // 1. It has a [member type definition] or [type definition] property 
            // whose value in turn has [name] equal to ID and [target namespace] 
            // equal to http://www.w3.org/2001/XMLSchema;
            
            // 2. It has a [base type definition] whose value has that [name] and [target namespace];
            
            // 3. It has a [base type definition] whose value has a [base type definition] 
            // whose value has that [name] and [target namespace], and so on following 
            // the [base type definition] property recursively;
            
            XSTypeDefinition typeDef = attrPSVI.getMemberTypeDefinition();
            if (typeDef != null) {
                typeDef = attrPSVI.getTypeDefinition();
            }
            
            // 
            if (typeDef != null && ((XSSimpleType) typeDef).isIDType()) {
                return attrPSVI.getSchemaNormalizedValue();
            }
            
            // 4 & 5 NA
        }
        
        return null;
    }
    
    /**
     * Not quite sure how this can be correctly implemented.
     * 
     * @param attributes
     * @param index
     * @return String - We return null since we currenly do not supprt this. 
     * @throws XNIException
     */
    public String getChildrenSchemaDeterminedID(XMLAttributes attributes,
            int index) throws XNIException {
        return null;
    }
    
    /**
     * 
     * @see org.apache.xerces.xpointer.XPointerPart#isFragmentResolved()
     */
    public boolean isFragmentResolved() {
        return fIsFragmentResolved;
    }
    
    /**
     * 
     * @see org.apache.xerces.xpointer.XPointerPart#isChildFragmentResolved()
     */
    public boolean isChildFragmentResolved() {
        return fIsFragmentResolved & ( fMatchingChildCount >  0);
    }
    
    /**
     * Returns the name of the ShortHand pointer
     * 
     * @see org.apache.xerces.xpointer.XPointerPart#getSchemeName()
     */
    public String getSchemeName() {
        return fShortHandPointer;
    }
    
    /**
     * @see org.apache.xerces.xpointer.XPointerPart#getSchemeData()
     */
    public String getSchemeData() {
        return null;
    }
    
    /**
     * @see org.apache.xerces.xpointer.XPointerPart#setSchemeName(java.lang.String)
     */
    public void setSchemeName(String schemeName) {
        fShortHandPointer = schemeName;
    }
    
    /**
     * @see org.apache.xerces.xpointer.XPointerPart#setSchemeData(java.lang.String)
     */
    public void setSchemeData(String schemeData) {
        // NA
    }
}
 No newline at end of file
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

QDox parse error in file://<WORKSPACE>/data/code-rep-dataset/Dataset5/Tasks/6360.java