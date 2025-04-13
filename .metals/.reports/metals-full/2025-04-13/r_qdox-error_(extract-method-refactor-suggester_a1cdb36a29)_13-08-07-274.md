error id: file://<WORKSPACE>/data/code-rep-dataset/Dataset2/Tasks/1652.java
file://<WORKSPACE>/data/code-rep-dataset/Dataset2/Tasks/1652.java
### com.thoughtworks.qdox.parser.ParseException: syntax error @[1,10]

error in qdox parser
file content:
```java
offset: 10
uri: file://<WORKSPACE>/data/code-rep-dataset/Dataset2/Tasks/1652.java
text:
```scala
@version $@@Id: Issue.java,v 1.4 2001/02/23 03:11:37 jmcnally dead $

package org.tigris.scarab.om;

/* ================================================================
 * Copyright (c) 2000 Collab.Net.  All rights reserved.
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

// JDK
import java.util.*;

// Turbine
import org.tigris.scarab.util.*;
import org.apache.turbine.util.*;
import org.apache.turbine.util.db.*;
// import org.tigris.scarab.om.project.peer.*;

import org.tigris.scarab.om.BaseScarabObject;
import org.tigris.scarab.attribute.Attribute;
import org.tigris.scarab.baseom.*;
import org.tigris.scarab.baseom.peer.*;

/**
    This is an object representation of the Issue table.

    @author <a href="mailto:jon@collab.net">Jon S. Stevens</a>
    @version $Id: Issue.java,v 1.3 2001/01/23 22:43:23 jmcnally Exp $
*/
public class Issue extends BaseScarabObject
{
    /** primary table class */
    ScarabIssue scarabIssue;

    /** The attributes that pertain to this module. */
    Vector collScarabAttributes;

    /**
     * Creates a new <code>Issue</code> instance.
     *
     */
    public Issue()
    {
        scarabIssue = new ScarabIssue();
    }

    /**
     * Creates a new <code>Issue</code> instance backed by a 
     * <code>ScarabIssue</code>.
     *
     * @param sm  a <code>ScarabIssue</code> value
     */
    public Issue(ScarabIssue sm)
    {
        scarabIssue = sm;
    }

    /**
     * Creates a new <code>Issue</code> instance backed by a 
     * <code>ScarabIssue</code> with id.
     *
     * @param id an <code>Object</code> value
     */
    public Issue(Object id) throws Exception
    {
        scarabIssue = ScarabIssuePeer.retrieveById(id);
    }

    
    /**
     * Get the value of scarabIssue.
     * @return value of scarabIssue.
     */
    public ScarabIssue getScarabIssue() 
    {
        return scarabIssue;
    }
    
    public Object getId()
    {
        return scarabIssue.getPrimaryKey();
    }

    public void setId(Object id) throws Exception
    {
        scarabIssue.setPrimaryKey(id);
    }
/*
    /**
     * Get the value of issueId.
     * @return value of issueId.
     * /
    public String getIssueId() 
    {
        return scarabIssue.getIssueId();
    }
    
    /**
     * Set the value of issueId.
     * @param v  Value to assign to issueId.
     * /
    public void setIssueId(String  v) 
    {
        scarabIssue.setIssueId(v);
    }
*/    

    public void addAttribute(Attribute attribute) throws Exception
    {
        ScarabIssueAttributeValue sAtt = 
            attribute.getScarabIssueAttributeValue();
        scarabIssue.addScarabIssueAttributeValues(sAtt);
    }

    /**
     * Should contain AttValues for the Issue as well as empty AttValues
     * that are relevant for the module, but have not been set for
     * the issue.
     */
    public HashMap getAllAttributes() throws Exception
    {
        return null;
    }


    public HashMap getAttributes() throws Exception
    {
        Criteria crit = new Criteria(2)
            .add(ScarabIssueAttributeValuePeer.DELETED, false);        
        Vector siaValues = scarabIssue.getScarabIssueAttributeValues(crit);

        HashMap map = new HashMap( (int)(1.25*siaValues.size() + 1) );
        for ( int i=0; i<siaValues.size(); i++ ) 
        {
            Attribute att = Attribute.getInstance(
               (ScarabIssueAttributeValue) siaValues.get(i) );
            String name = att.getName();
            map.put(name.toUpperCase(), att);
        }

        return map;
    }

    public HashMap getModuleAttributes() throws Exception
    {
        Criteria crit = new Criteria(2)
            .add(ScarabRModuleAttributePeer.DELETED, false);        
        Vector moduleAttributes = 
            scarabIssue.getScarabModule().getScarabRModuleAttributes(crit);
        HashMap siaValuesMap = getAttributes();

        HashMap map = new HashMap( (int)(1.25*moduleAttributes.size() + 1) );
try{
        for ( int i=0; i<moduleAttributes.size(); i++ ) 
        {
            Attribute att = Attribute.getInstance(
               (ScarabRModuleAttribute) moduleAttributes.get(i), 
               this.getScarabIssue());
            String key = att.getName().toUpperCase();

            if ( siaValuesMap.containsKey(key) ) 
            {
                map.put( key, siaValuesMap.get(key) );
            }
            else 
            {
                ScarabIssueAttributeValue siav = 
                    new ScarabIssueAttributeValue();
                siav.setScarabAttribute(att.getScarabAttribute());
                siav.setScarabIssue(this.getScarabIssue());
                att.setScarabIssueAttributeValue(siav);
                map.put( key, att ); 
            }             
        }
}catch(Exception e){e.printStackTrace();}
        return map;
    }

    /*
    /**
        get the owner_id of the project
    * /
    public int getOwnerId()
    {
        return scarabIssue.getOwnerId();
    }
    /**
        set the owner_id of the project
    * /
    public void setOwnerId(int id)
    {
        scarabIssue.setOwner(id);
    }
    /**
        get the qa_contact_id of the project
    * /
    public int getQaContactId()
    {
        return scarabIssue.getQaContactId();
    }
    /**
        set the qa_contact_id of the project
    * /
    public void setQaContactId(int id)
    {
        scarabIssue.setQaContactId(id);
    }
    */



    public void save() throws Exception
    {
        scarabIssue.save();        
    }

    public String getQueryKey()
    {
        StringBuffer qs = new StringBuffer("Issue[");
        qs.append(scarabIssue.getPrimaryKey().toString());
        return qs.append("]").toString();
    }

    /**
        calls the doPopulate() method with validation false
    */
    public Issue doPopulate(RunData data)
        throws Exception
    {
        return doPopulate(data, false);
    }

    /**
        populates project based on the existing project data from POST
    */
    public Issue doPopulate(RunData data, boolean validate)
        throws Exception
    {
        String prefix = getQueryKey().toLowerCase();

        if ( scarabIssue.isNew() ) 
        {
            int project_id = data.getParameters().getInt(prefix + "id", -1); 
            if (validate)
            {
                if (project_id == -1)
                    throw new Exception ( "Missing project_id!" );
            }
            setId(new Integer(project_id));
            setCreatedBy( ((ScarabUser)data.getUser()).getPrimaryKeyAsInt() );
            setCreatedDate( new Date() );
        }

        if (validate)
        {
        }

        return this;
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

QDox parse error in file://<WORKSPACE>/data/code-rep-dataset/Dataset2/Tasks/1652.java