error id: file://<WORKSPACE>/data/code-rep-dataset/Dataset5/Tasks/8207.java
file://<WORKSPACE>/data/code-rep-dataset/Dataset5/Tasks/8207.java
### com.thoughtworks.qdox.parser.ParseException: syntax error @[19,1]

error in qdox parser
file content:
```java
offset: 695
uri: file://<WORKSPACE>/data/code-rep-dataset/Dataset5/Tasks/8207.java
text:
```scala
final class UnparsedEntityHandler implements XMLDTDFilter, EntityState {

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

p@@ackage org.apache.xerces.jaxp;

import java.util.HashMap;

import org.apache.xerces.impl.validation.EntityState;
import org.apache.xerces.impl.validation.ValidationManager;
import org.apache.xerces.xni.Augmentations;
import org.apache.xerces.xni.XMLDTDHandler;
import org.apache.xerces.xni.XMLLocator;
import org.apache.xerces.xni.XMLResourceIdentifier;
import org.apache.xerces.xni.XMLString;
import org.apache.xerces.xni.XNIException;
import org.apache.xerces.xni.parser.XMLDTDFilter;
import org.apache.xerces.xni.parser.XMLDTDSource;

/**
 * <p>This filter records which unparsed entities have been
 * declared in the DTD and provides this information to a ValidationManager.
 * Events are forwarded to the registered XMLDTDHandler without modification.</p>
 * 
 * @author Michael Glavassevich, IBM
 * @version $Id$
 */
class UnparsedEntityHandler implements XMLDTDFilter, EntityState {

    /** DTD source and handler. **/
    private XMLDTDSource fDTDSource;
    private XMLDTDHandler fDTDHandler;
    
    /** Validation manager. */
    private final ValidationManager fValidationManager;
    
    /** Map for tracking unparsed entities. */
    private HashMap fUnparsedEntities = null;
    
    UnparsedEntityHandler(ValidationManager manager) {
        fValidationManager = manager;
    }
    
    /*
     * XMLDTDHandler methods
     */

    public void startDTD(XMLLocator locator, Augmentations augmentations)
            throws XNIException {
        fValidationManager.setEntityState(this);
        if (fUnparsedEntities != null && !fUnparsedEntities.isEmpty()) {
            // should only clear this if the last document contained unparsed entities
            fUnparsedEntities.clear();
        }
        if (fDTDHandler != null) {
            fDTDHandler.startDTD(locator, augmentations);
        }
    }

    public void startParameterEntity(String name,
            XMLResourceIdentifier identifier, String encoding,
            Augmentations augmentations) throws XNIException {
        if (fDTDHandler != null) {
            fDTDHandler.startParameterEntity(name, identifier, encoding, augmentations);
        }
    }

    public void textDecl(String version, String encoding,
            Augmentations augmentations) throws XNIException {
        if (fDTDHandler != null) {
            fDTDHandler.textDecl(version, encoding, augmentations);
        }
    }

    public void endParameterEntity(String name, Augmentations augmentations)
            throws XNIException {
        if (fDTDHandler != null) {
            fDTDHandler.endParameterEntity(name, augmentations);
        }
    }

    public void startExternalSubset(XMLResourceIdentifier identifier,
            Augmentations augmentations) throws XNIException {
        if (fDTDHandler != null) {
            fDTDHandler.startExternalSubset(identifier, augmentations);
        }
    }

    public void endExternalSubset(Augmentations augmentations)
            throws XNIException {
        if (fDTDHandler != null) {
            fDTDHandler.endExternalSubset(augmentations);
        }
    }

    public void comment(XMLString text, Augmentations augmentations)
            throws XNIException {
        if (fDTDHandler != null) {
            fDTDHandler.comment(text, augmentations);
        }
    }

    public void processingInstruction(String target, XMLString data,
            Augmentations augmentations) throws XNIException {
        if (fDTDHandler != null) {
            fDTDHandler.processingInstruction(target, data, augmentations);
        }
    }

    public void elementDecl(String name, String contentModel,
            Augmentations augmentations) throws XNIException {
        if (fDTDHandler != null) {
            fDTDHandler.elementDecl(name, contentModel, augmentations);
        }
    }

    public void startAttlist(String elementName, Augmentations augmentations)
            throws XNIException {
        if (fDTDHandler != null) {
            fDTDHandler.startAttlist(elementName, augmentations);
        }
    }

    public void attributeDecl(String elementName, String attributeName,
            String type, String[] enumeration, String defaultType,
            XMLString defaultValue, XMLString nonNormalizedDefaultValue,
            Augmentations augmentations) throws XNIException {
        if (fDTDHandler != null) {
            fDTDHandler.attributeDecl(elementName, attributeName,
                    type, enumeration, defaultType,
                    defaultValue, nonNormalizedDefaultValue,
                    augmentations);
        }
    }

    public void endAttlist(Augmentations augmentations) throws XNIException {
        if (fDTDHandler != null) {
            fDTDHandler.endAttlist(augmentations);
        }
    }

    public void internalEntityDecl(String name, XMLString text,
            XMLString nonNormalizedText, Augmentations augmentations)
            throws XNIException {
        if (fDTDHandler != null) {
            fDTDHandler.internalEntityDecl(name, text,
                    nonNormalizedText, augmentations);
        }
    }

    public void externalEntityDecl(String name,
            XMLResourceIdentifier identifier, Augmentations augmentations)
            throws XNIException {
        if (fDTDHandler != null) {
            fDTDHandler.externalEntityDecl(name, identifier, augmentations);
        }
    }

    public void unparsedEntityDecl(String name,
            XMLResourceIdentifier identifier, String notation,
            Augmentations augmentations) throws XNIException {
        if (fUnparsedEntities == null) {
            fUnparsedEntities = new HashMap();
        }
        fUnparsedEntities.put(name, name);
        if (fDTDHandler != null) {
            fDTDHandler.unparsedEntityDecl(name, identifier, notation, augmentations);
        }
    }

    public void notationDecl(String name, XMLResourceIdentifier identifier,
            Augmentations augmentations) throws XNIException {
        if (fDTDHandler != null) {
            fDTDHandler.notationDecl(name, identifier, augmentations);
        }
    }

    public void startConditional(short type, Augmentations augmentations)
            throws XNIException {
        if (fDTDHandler != null) {
            fDTDHandler.startConditional(type, augmentations);
        }
    }

    public void ignoredCharacters(XMLString text, Augmentations augmentations)
            throws XNIException {
        if (fDTDHandler != null) {
            fDTDHandler.ignoredCharacters(text, augmentations);
        }

    }

    public void endConditional(Augmentations augmentations) throws XNIException {
        if (fDTDHandler != null) {
            fDTDHandler.endConditional(augmentations);
        }
    }

    public void endDTD(Augmentations augmentations) throws XNIException {
        if (fDTDHandler != null) {
            fDTDHandler.endDTD(augmentations);
        }
    }

    public void setDTDSource(XMLDTDSource source) {
        fDTDSource = source;
    }

    public XMLDTDSource getDTDSource() {
        return fDTDSource;
    }
    
    /*
     * XMLDTDSource methods
     */

    public void setDTDHandler(XMLDTDHandler handler) {
        fDTDHandler = handler;
    }

    public XMLDTDHandler getDTDHandler() {
        return fDTDHandler;
    }
    
    /*
     * EntityState methods
     */

    public boolean isEntityDeclared(String name) {
        return false;
    }

    public boolean isEntityUnparsed(String name) {
        if (fUnparsedEntities != null) {
            return fUnparsedEntities.containsKey(name);
        }
        return false;
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

QDox parse error in file://<WORKSPACE>/data/code-rep-dataset/Dataset5/Tasks/8207.java