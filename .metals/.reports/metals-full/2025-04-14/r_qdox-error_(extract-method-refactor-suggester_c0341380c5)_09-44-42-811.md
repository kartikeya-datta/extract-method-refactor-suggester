error id: file://<WORKSPACE>/data/code-rep-dataset/Dataset2/Tasks/7576.java
file://<WORKSPACE>/data/code-rep-dataset/Dataset2/Tasks/7576.java
### com.thoughtworks.qdox.parser.ParseException: syntax error @[1,1]

error in qdox parser
file content:
```java
offset: 1
uri: file://<WORKSPACE>/data/code-rep-dataset/Dataset2/Tasks/7576.java
text:
```scala
n@@ewQuery.setUserId(user.getUserId());

package org.tigris.scarab.om;

/* ================================================================
 * Copyright (c) 2000-2002 CollabNet.  All rights reserved.
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

import java.util.List;
import java.util.Arrays;
import java.util.Date;
import java.util.Locale;

import org.apache.turbine.TemplateContext;
//import org.apache.fulcrum.template.TemplateContext;
import org.apache.turbine.modules.ContextAdapter;
import org.apache.turbine.Turbine;
import org.apache.fulcrum.localization.Localization;

import org.apache.torque.TorqueException;
import org.apache.torque.util.Criteria;
import org.apache.torque.om.ObjectKey;
import org.apache.torque.om.Persistent;
import org.apache.torque.om.NumberKey;

import org.tigris.scarab.services.security.ScarabSecurity;
import org.tigris.scarab.services.cache.ScarabCache;
import org.tigris.scarab.om.Module;
import org.tigris.scarab.om.ModuleManager;
import org.tigris.scarab.util.ScarabConstants;
import org.tigris.scarab.util.ScarabException;
import org.tigris.scarab.util.Email;
import org.tigris.scarab.util.Log;

/** 
 * You should add additional methods to this class to meet the
 * application requirements.  This class will only be generated as
 * long as it does not already exist in the output directory.
 */
public class Query 
    extends org.tigris.scarab.om.BaseQuery
    implements Persistent
{
    private static final String GET_R_QUERY_USER = 
        "getRQueryUser";

    /**
     * A local reference, so that getScarabUser is guaranteed to return
     * the same instance as was passed to setScarabUser
     */
    private ScarabUser scarabUser;
    
    /**
     * Get the value of scarabUser.
     * @return value of scarabUser.
     */
    public ScarabUser getScarabUser() 
        throws TorqueException
    {
        ScarabUser user = this.scarabUser;
        if (user == null) 
        {
            user = super.getScarabUser();
        }
        
        return user;
    }
    
    /**
     * Set the value of scarabUser.
     * @param v  Value to assign to scarabUser.
     */
    public void setScarabUser(ScarabUser  v) 
        throws TorqueException
    {
        this.scarabUser = v;
        super.setScarabUser(v);
    }
    
    /**
     * Throws UnsupportedOperationException.  Use
     * <code>getModule()</code> instead.
     *
     * @return a <code>ScarabModule</code> value
     */
    public ScarabModule getScarabModule()
    {
        throw new UnsupportedOperationException(
            "Should use getModule");
    }

    /**
     * Throws UnsupportedOperationException.  Use
     * <code>setModule(Module)</code> instead.
     *
     */
    public void setScarabModule(ScarabModule module)
    {
        throw new UnsupportedOperationException(
            "Should use setModule(Module). Note module cannot be new.");
    }

    /**
     * Use this instead of setScarabModule.  Note: module cannot be new.
     */
    public void setModule(Module me)
        throws TorqueException
    {
        if (me == null) 
        {
            setModuleId((NumberKey)null);            
        }
        else 
        {
            NumberKey id = me.getModuleId();
            if (id == null) 
            {
                throw new TorqueException("Modules must be saved prior to " +
                    "being associated with other objects.");
            }
            setModuleId(id);
        }        
    }

    /**
     * Module getter.  Use this method instead of getScarabModule().
     *
     * @return a <code>Module</code> value
     */
    public Module getModule()
        throws TorqueException
    {
        Module module = null;
        ObjectKey id = getModuleId();
        if ( id != null ) 
        {
            module = ModuleManager.getInstance(id);
        }
        
        return module;
    }

    /**
     * A new Query object
     */
    public static Query getInstance() 
    {
        return new Query();
    }

    public boolean saveAndSendEmail( ScarabUser user, Module module, 
                                     TemplateContext context )
        throws Exception
    {
        // If it's a module scoped query, user must have Item | Approve 
        //   permission, Or its Approved field gets set to false
        boolean success = true;
        if (getScopeId().equals(Scope.PERSONAL__PK) 
 user.hasPermission(ScarabSecurity.ITEM__APPROVE, module))
        {
            setApproved(true);
        }
        else
        {
            setApproved(false);
            setScopeId(Scope.PERSONAL__PK);

            // Send Email to the people with module edit ability so
            // that they can approve the new template
            if (context != null)
            {
                context.put("user", user);
                context.put("module", module);

                String subject = Localization.getString(
                    ScarabConstants.DEFAULT_BUNDLE_NAME,
                    Locale.getDefault(),
                    "NewQueryRequiresApproval");

                String template = Turbine.getConfiguration().
                    getString("scarab.email.requireapproval.template",
                              "email/RequireApproval.vm");

                ScarabUser[] toUsers = module
                    .getUsers(ScarabSecurity.ITEM__APPROVE);

                if (Log.get().isDebugEnabled()) 
                {
                    if (toUsers == null || toUsers.length ==0) 
                    {
                        Log.get().debug("No users to approve query.");    
                    }
                    else 
                    {
                        Log.get().debug("Users to approve query: ");    
                        for (int i=0; i<toUsers.length; i++) 
                        {
                            Log.get().debug(toUsers[i].getEmail());
                        }  
                    }          
                }
                
                
                String fromUser = "scarab.email.default";
                if (!Email.sendEmail(new ContextAdapter(context), module, 
                    fromUser, module.getSystemEmail(), Arrays.asList(toUsers),
                    null, subject, template))
                {
                    success = false;
                }
            }
        }
        if (getMITList() != null) 
        {
            getMITList().save();
            // it would be good if this updated our list id, but it doesn't
            // happen automatically so reset it.
            setMITList(getMITList());            
        }
        save();
        return success;
    }

    /**
     * Generates link to Issue List page, re-running stored query.
     */
    public String getExecuteLink(String link) 
    {
       link = link 
          + "/template/IssueList.vm?action=Search&eventSubmit_doSearch=Search" 
          + "&resultsperpage=25&pagenum=1" + getValue();

       NumberKey listId = getListId();
       if (listId != null) 
       {
           link += "&" + ScarabConstants.CURRENT_MITLIST_ID + "=" + listId;
       }
       else 
       {
           link += "&" + ScarabConstants.REMOVE_CURRENT_MITLIST_QKEY + "=true";
       }
       return link;
    }

    /**
     * Generates link to the Query Detail page.
     */
    public String getEditLink(String link) 
    {
        link = link + "/template/EditQuery.vm?queryId=" + getQueryId()
                    + getValue();

       NumberKey listId = getListId();
       if (listId != null) 
       {
           link += "&" + ScarabConstants.CURRENT_MITLIST_ID + "=" + listId;
       }
       else 
       {
           link += "&" + ScarabConstants.REMOVE_CURRENT_MITLIST_QKEY + "=true";
       }
       return link;
    }

    /**
     * Subscribes user to query.
     */
    public void subscribe(ScarabUser user, String frequencyId)
        throws Exception
    {
        RQueryUser rqu = getRQueryUser(user);
        rqu.setSubscriptionFrequency(frequencyId);
        rqu.setIsSubscribed(true);
        rqu.save();
    }

    /**
     * Unsubscribes user from query.
     */
    public void unSubscribe(ScarabUser user)
        throws Exception
    {
        RQueryUser rqu = getRQueryUser(user);
        if (rqu.getIsdefault())
        {
            rqu.setIsSubscribed(false);
            rqu.save();
        }
        else
        {
            rqu.delete(user);
        }
    }


    /**
     * Gets RQueryUser object for this query and user.
     */
    public RQueryUser getRQueryUser(ScarabUser user)
        throws Exception
    {
        RQueryUser result = new RQueryUser();
        Object obj = ScarabCache.get(this, GET_R_QUERY_USER, user); 
        if ( obj == null ) 
        {        
            Criteria crit = new Criteria();
            crit.add(RQueryUserPeer.QUERY_ID, getQueryId());
            crit.add(RQueryUserPeer.USER_ID, user.getUserId());
            if (RQueryUserPeer.doSelect(crit).size() > 0)
            {
                result = (RQueryUser)getRQueryUsers().get(0);
            }
            else
            {
                result.setQuery(this);
                result.setUserId(user.getUserId());
            }
            ScarabCache.put(result, this, GET_R_QUERY_USER, user);
        }
        else 
        {
            result = (RQueryUser)obj;
        }
        return result;
    }

    /**
     * Checks permission and approves or rejects query. If query
     * is approved, query type set to "module", else set to "personal".
     */
    public void approve( ScarabUser user, boolean approved )
         throws Exception
    {                
        Module module = getModule();

        if (user.hasPermission(ScarabSecurity.ITEM__APPROVE, module))
        {
            setApproved(true);
            if (approved)
            {
                setScopeId(Scope.MODULE__PK);
            }
            save();
        } 
        else
        {
            throw new ScarabException(ScarabConstants.NO_PERMISSION_MESSAGE);
        }            
    }


    /**
     * Checks if user has permission to delete query.
     * Only the creating user can delete a personal query.
     * Only project owner or admin can delete a project-wide query.
     */
    public void delete( ScarabUser user )
         throws Exception
    {                
        Module module = getModule();
        if (user.hasPermission(ScarabSecurity.ITEM__APPROVE, module)
 (user.getUserId().equals(getUserId()) 
             && getScopeId().equals(Scope.PERSONAL__PK)))
        {
            // Delete user-query maps.
            List rqus = getRQueryUsers();
            for (int i=0; i<rqus.size(); i++)
            {
                RQueryUser rqu = (RQueryUser)rqus.get(i);
                rqu.delete(user);
            }
            setDeleted(true);
            save();
            
        } 
        else
        {
            throw new ScarabException(ScarabConstants.NO_PERMISSION_MESSAGE);
        }            
    }

    /**
     * Checks if user has permission to delete query.
     * Only the creating user can delete a personal query.
     * Only project owner or admin can delete a project-wide query.
     */
    public void copyQuery( ScarabUser user )
         throws Exception
    {                
         Query newQuery = new Query();
         newQuery.setName(getName() + " (copy)");
         newQuery.setDescription(getDescription());
         newQuery.setValue(getValue());
         newQuery.setModuleId(getModuleId());
         newQuery.setIssueTypeId(getIssueTypeId());
         newQuery.setListId(getListId());
         newQuery.setApproved(getApproved());
         newQuery.setCreatedDate(new Date());
         newQuery.setUserId(getUserId());
         newQuery.setScopeId(getScopeId());
         newQuery.save();

         RQueryUser rqu = getRQueryUser(user);
         if (rqu != null)
         {
             RQueryUser rquNew = new RQueryUser();
             rquNew.setQueryId(newQuery.getQueryId());
             rquNew.setUserId(user.getUserId());
             rquNew.setSubscriptionFrequency(rqu.getSubscriptionFrequency());
             rquNew.setIsdefault(rqu.getIsdefault());
             rquNew.setIsSubscribed(rqu.getIsSubscribed());
             rquNew.save();
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
	scala.collection.parallel.AdaptiveWorkStealingTasks$AWSTWrappedTask.compute(Tasks.scala:152)
	scala.collection.parallel.AdaptiveWorkStealingTasks$AWSTWrappedTask.compute$(Tasks.scala:148)
	scala.collection.parallel.AdaptiveWorkStealingForkJoinTasks$AWSFJTWrappedTask.compute(Tasks.scala:304)
	java.base/java.util.concurrent.RecursiveAction.exec(RecursiveAction.java:194)
	java.base/java.util.concurrent.ForkJoinTask.doExec(ForkJoinTask.java:373)
	java.base/java.util.concurrent.ForkJoinPool.helpJoin(ForkJoinPool.java:1883)
	java.base/java.util.concurrent.ForkJoinTask.awaitDone(ForkJoinTask.java:440)
	java.base/java.util.concurrent.ForkJoinTask.join(ForkJoinTask.java:670)
	scala.collection.parallel.ForkJoinTasks$FJTWrappedTask.sync(Tasks.scala:243)
	scala.collection.parallel.ForkJoinTasks$FJTWrappedTask.sync$(Tasks.scala:243)
	scala.collection.parallel.AdaptiveWorkStealingForkJoinTasks$AWSFJTWrappedTask.sync(Tasks.scala:304)
	scala.collection.parallel.AdaptiveWorkStealingTasks$AWSTWrappedTask.internal(Tasks.scala:173)
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

QDox parse error in file://<WORKSPACE>/data/code-rep-dataset/Dataset2/Tasks/7576.java