error id: file://<WORKSPACE>/data/code-rep-dataset/Dataset2/Tasks/1589.java
file://<WORKSPACE>/data/code-rep-dataset/Dataset2/Tasks/1589.java
### com.thoughtworks.qdox.parser.ParseException: syntax error @[1,1]

error in qdox parser
file content:
```java
offset: 1
uri: file://<WORKSPACE>/data/code-rep-dataset/Dataset2/Tasks/1589.java
text:
```scala
.@@getInstance(attribute, activityInfo.getValue());

package org.tigris.scarab.util.xml;

/* ================================================================
 * Copyright (c) 2000-2001 CollabNet.  All rights reserved.
 * 
 * Redistribution and use in source and binary forms, with or without
 * modification, are permitted provided that the following conditions are
 * met:
 * 
 * 1. Redistributions of source code must retain the above copyright
 * notice, this list of conditions and the following disclaimer.
 * 
 * 2. Redistributions in binary form must reproduce the above copyright
 * notice, this list of conditions and the following disclaimer in the
 * documentation and/or other materials provided with the distribution.
 * 
 * 3. The end-user documentation included with the redistribution, if
 * any, must include the following acknowlegement: "This product includes
 * software developed by Collab.Net <http://www.Collab.Net/>."
 * Alternately, this acknowlegement may appear in the software itself, if
 * and wherever such third-party acknowlegements normally appear.
 * 
 * 4. The hosted project names must not be used to endorse or promote
 * products derived from this software without prior written
 * permission. For written permission, please contact info@collab.net.
 * 
 * 5. Products derived from this software may not use the "Tigris" or 
 * "Scarab" names nor may "Tigris" or "Scarab" appear in their names without 
 * prior written permission of Collab.Net.
 * 
 * THIS SOFTWARE IS PROVIDED ``AS IS'' AND ANY EXPRESSED OR IMPLIED
 * WARRANTIES, INCLUDING, BUT NOT LIMITED TO, THE IMPLIED WARRANTIES OF
 * MERCHANTABILITY AND FITNESS FOR A PARTICULAR PURPOSE ARE DISCLAIMED.
 * IN NO EVENT SHALL COLLAB.NET OR ITS CONTRIBUTORS BE LIABLE FOR ANY
 * DIRECT, INDIRECT, INCIDENTAL, SPECIAL, EXEMPLARY, OR CONSEQUENTIAL
 * DAMAGES (INCLUDING, BUT NOT LIMITED TO, PROCUREMENT OF SUBSTITUTE
 * GOODS OR SERVICES; LOSS OF USE, DATA, OR PROFITS; OR BUSINESS
 * INTERRUPTION) HOWEVER CAUSED AND ON ANY THEORY OF LIABILITY, WHETHER
 * IN CONTRACT, STRICT LIABILITY, OR TORT (INCLUDING NEGLIGENCE OR
 * OTHERWISE) ARISING IN ANY WAY OUT OF THE USE OF THIS SOFTWARE, EVEN IF
 * ADVISED OF THE POSSIBILITY OF SUCH DAMAGE.
 *
 * ====================================================================
 * 
 * This software consists of voluntary contributions made by many
 * individuals on behalf of Collab.Net.
 */

import org.xml.sax.Attributes;

import org.apache.commons.digester.Digester;

import org.tigris.scarab.om.Activity;
import org.tigris.scarab.om.Transaction;
import org.tigris.scarab.om.TransactionTypePeer;
import org.tigris.scarab.om.Issue;
import org.tigris.scarab.om.Attribute;
import org.tigris.scarab.om.AttributeOption;
import org.tigris.scarab.om.AttributeValue;
import org.tigris.scarab.om.ParentChildAttributeOption;

/**
 * Handler for the xpath "scarab/module/issue/transaction/activity"
 *
 * @author <a href="mailto:kevin.minshull@bitonic.com">Kevin Minshull</a>
 * @author <a href="mailto:richard.han@bitonic.com">Richard Han</a>
 */
public class ActivityRule extends BaseRule
{
    public ActivityRule(Digester digester, String state)
    {
        super(digester, state);
    }
    
    /**
     * This method is called when the beginning of a matching XML element
     * is encountered.
     *
     * @param attributes The attribute list of this element
     */
    public void begin(Attributes attributes) throws Exception
    {
        log().debug("(" + getState() + ") activity begin()");
        super.doInsertionOrValidationAtBegin(attributes);
    }
    
    protected void doInsertionAtBegin(Attributes attributes)
    {
        ActivityInfo activityInfo = new ActivityInfo();
        digester.push(activityInfo);
    }
    
    protected void doValidationAtBegin(Attributes attributes)
    {
    }
    
    /**
     * This method is called when the end of a matching XML element
     * is encountered.
     */
    public void end() throws Exception
    {
        log().debug("(" + getState() + ") activity end()");
        super.doInsertionOrValidationAtEnd();
    }
    
    /**
     * This function will add the activity to the transaction.
     * FIXME: there is a problem with this function (besides the bad code)
     * the create issue stuff is working, but the edit issue stuff
     * does not appear to be working.
     */
    protected void doInsertionAtEnd()
        throws Exception
    {
        ActivityInfo activityInfo = (ActivityInfo)digester.pop();
        Transaction transaction = (Transaction)digester.pop();
        Issue issue = (Issue)digester.pop();
        
        Attribute attribute = Attribute.getInstance(activityInfo.getName());
        if(attribute == null)
        {
            // create attribute
            attribute = Attribute.getInstance();
            attribute.setName(activityInfo.getName());
            attribute.setAttributeType(activityInfo.getType());
            attribute.setDescription("Generated by Data import util");
            attribute.save();
            
            if (attribute.isOptionAttribute())
            {
                // create option
                ParentChildAttributeOption newPCAO = 
                    ParentChildAttributeOption.getInstance();
                newPCAO.setName(activityInfo.getName());
                newPCAO.setAttributeId(attribute.getAttributeId());
                newPCAO.save();
            }
        }
        
        AttributeValue attributeValue = null;
        boolean createTransaction = transaction.getTransactionType().getTypeId().equals(TransactionTypePeer.CREATE_ISSUE__PK);
        if (createTransaction)
        {
            attributeValue = AttributeValue.getNewInstance(attribute, issue);
        }
        
        if (attribute.isOptionAttribute())
        {
            AttributeOption attributeOption = AttributeOption
                .getInstance(attribute, activityInfo.getName());
            
            if (attributeOption == null)
            {
                // create option
                ParentChildAttributeOption newPCAO = 
                    ParentChildAttributeOption.getInstance();
                newPCAO.setName(activityInfo.getName());
                newPCAO.setAttributeId(attribute.getAttributeId());
                newPCAO.save();
                if (createTransaction)
                {
                    attributeOption = AttributeOption
                        .getInstance(attribute, activityInfo.getValue());
                }
            }
            if (createTransaction)
            {
                attributeValue.setOptionId(attributeOption.getOptionId());
            }
        }
        else
        {
            if (createTransaction)
            {
                attributeValue.setValue(activityInfo.getValue());
            }
        }
        
        if (createTransaction)
        {
            attributeValue.startTransaction(transaction);
            attributeValue.save();
        }
        else
        {
            Activity activity = new Activity();
            activity.create(issue, attribute, activityInfo.getDescription(), transaction,                           
                            activityInfo.getOldValue(), activityInfo.getValue());
        }
        
        digester.push(issue);
        digester.push(transaction);
    }
    
    protected void doValidationAtEnd()
    {
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

QDox parse error in file://<WORKSPACE>/data/code-rep-dataset/Dataset2/Tasks/1589.java