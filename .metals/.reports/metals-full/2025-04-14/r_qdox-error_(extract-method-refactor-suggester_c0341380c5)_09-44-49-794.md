error id: file://<WORKSPACE>/data/code-rep-dataset/Dataset2/Tasks/4096.java
file://<WORKSPACE>/data/code-rep-dataset/Dataset2/Tasks/4096.java
### com.thoughtworks.qdox.parser.ParseException: syntax error @[1,1]

error in qdox parser
file content:
```java
offset: 1
uri: file://<WORKSPACE>/data/code-rep-dataset/Dataset2/Tasks/4096.java
text:
```scala
r@@eturn Turbine.getConfiguration().getBoolean("scarab.anonymous.enable");

package org.tigris.scarab.tools;

/* ================================================================
 * Copyright (c) 2000-2003 CollabNet.  All rights reserved.
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

import java.text.DateFormat;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Calendar;
import java.util.Collections;
import java.util.Comparator;
import java.util.HashMap;
import java.util.Hashtable;
import java.util.Iterator;
import java.util.List;
import java.util.Locale;
import java.util.Map;
import java.util.TimeZone;

import org.apache.commons.collections.map.LinkedMap;
import org.apache.commons.lang.StringUtils;
import org.apache.fulcrum.intake.Intake;
import org.apache.fulcrum.intake.model.Field;
import org.apache.fulcrum.intake.model.Group;
import org.apache.fulcrum.localization.Localization;
import org.apache.fulcrum.parser.ParameterParser;
import org.apache.fulcrum.parser.StringValueParser;
import org.apache.fulcrum.pool.Recyclable;
import org.apache.torque.NoRowsException;
import org.apache.torque.TooManyRowsException;
import org.apache.torque.TorqueException;
import org.apache.torque.om.ComboKey;
import org.apache.torque.om.NumberKey;
import org.apache.torque.om.SimpleKey;
import org.apache.turbine.RunData;
import org.apache.turbine.TemplateContext;
import org.apache.turbine.Turbine;
import org.apache.turbine.services.pull.ApplicationTool;
import org.apache.turbine.tool.IntakeTool;
import org.tigris.scarab.om.Activity;
import org.tigris.scarab.om.ActivitySet;
import org.tigris.scarab.om.Attachment;
import org.tigris.scarab.om.AttachmentManager;
import org.tigris.scarab.om.Attribute;
import org.tigris.scarab.om.AttributeGroup;
import org.tigris.scarab.om.AttributeGroupManager;
import org.tigris.scarab.om.AttributeManager;
import org.tigris.scarab.om.AttributeOption;
import org.tigris.scarab.om.AttributeOptionManager;
import org.tigris.scarab.om.AttributeOptionPeer;
import org.tigris.scarab.om.AttributePeer;
import org.tigris.scarab.om.AttributeValue;
import org.tigris.scarab.om.Depend;
import org.tigris.scarab.om.DependManager;
import org.tigris.scarab.om.FrequencyPeer;
import org.tigris.scarab.om.Issue;
import org.tigris.scarab.om.IssueManager;
import org.tigris.scarab.om.IssueTemplateInfo;
import org.tigris.scarab.om.IssueTemplateInfoManager;
import org.tigris.scarab.om.IssueTemplateInfoPeer;
import org.tigris.scarab.om.IssueType;
import org.tigris.scarab.om.IssueTypeManager;
import org.tigris.scarab.om.MITList;
import org.tigris.scarab.om.MITListManager;
import org.tigris.scarab.om.Module;
import org.tigris.scarab.om.ModuleManager;
import org.tigris.scarab.om.ParentChildAttributeOption;
import org.tigris.scarab.om.Query;
import org.tigris.scarab.om.QueryManager;
import org.tigris.scarab.om.QueryPeer;
import org.tigris.scarab.om.RModuleAttribute;
import org.tigris.scarab.om.RModuleAttributeManager;
import org.tigris.scarab.om.RModuleIssueType;
import org.tigris.scarab.om.ROptionOption;
import org.tigris.scarab.om.ReportManager;
import org.tigris.scarab.om.ScarabUser;
import org.tigris.scarab.om.ScarabUserManager;
import org.tigris.scarab.om.ScopePeer;
import org.tigris.scarab.om.Transition;
import org.tigris.scarab.om.TransitionPeer;
import org.tigris.scarab.reports.ReportBridge;
import org.tigris.scarab.services.cache.ScarabCache;
import org.tigris.scarab.tools.localization.L10NKeySet;
import org.tigris.scarab.tools.localization.L10NMessage;
import org.tigris.scarab.tools.localization.Localizable;
import org.tigris.scarab.util.IteratorWithSize;
import org.tigris.scarab.util.Log;
import org.tigris.scarab.util.ScarabConstants;
import org.tigris.scarab.util.ScarabException;
import org.tigris.scarab.util.ScarabLink;
import org.tigris.scarab.util.ScarabPaginatedList;
import org.tigris.scarab.util.SimpleSkipFiltering;
import org.tigris.scarab.util.SnippetRenderer;
import org.tigris.scarab.util.SubsetIteratorWithSize;
import org.tigris.scarab.util.WindowIterator;
import org.tigris.scarab.util.word.ComplexQueryException;
import org.tigris.scarab.util.word.IssueSearch;
import org.tigris.scarab.util.word.IssueSearchFactory;
import org.tigris.scarab.util.word.MaxConcurrentSearchException;
import org.tigris.scarab.util.word.QueryResult;
import org.tigris.scarab.util.word.SearchIndex;

/**
 * This class is used by the Scarab API
 */
public class ScarabRequestTool
    implements ApplicationTool,Recyclable
{
    private static final String TIME_ZONE =
        Turbine.getConfiguration().getString("scarab.timezone", "");

    /**
     * The disposed flag.
     */
    private boolean disposed;
    
    /** the object containing request specific data */
    private RunData data;

    /**
     * A User object for use within the Scarab API.
     */
    private ScarabUser user = null;

    /**
     * A Issue object for use within the Scarab API.
     */
    private Issue issue = null;

    /**
     * The <code>Alert!</code> message for this request.
     */
    private Object alert = null;

    /**
     * A Attribute object for use within the Scarab API.
     */
    private Attribute attribute = null;

    /**
     * A Attachment object for use within the Scarab API.
     */
    private Attachment attachment = null;

    /**
     * A Depend object for use within the Scarab API.
     */
    private Depend depend = null;

    /**
     * A Query object for use within the Scarab API.
     */
    private Query query = null;

    /**
     * An IssueTemplateInfo object for use within the Scarab API.
     */
    private IssueTemplateInfo templateInfo = null;

    /**
     * An IssueType object for use within the Scarab API.
     */
    private IssueType issueType = null;

    /**
     * An AttributeGroup object
     */
    private AttributeGroup group = null;

    /**
     * The issue that is currently being entered.
     */
    private Issue reportingIssue = null;

    /**
     * A Module object
     */
    private Module module = null;

    /**
     * A AttributeOption object for use within the Scarab API.
     */
    private AttributeOption attributeOption = null;

    /**
     * A ROptionOption
     */
    private ROptionOption roo = null;

    /**
     * A ParentChildAttributeOption
     */
    private ParentChildAttributeOption pcao = null;

    /**
     * IssueSearch object for performing queries
     */
    private IssueSearch issueSearch = null;
    
    /**
     * A list of Issues
     */
    private List issueList;

    /**
     * keep track if the columns were reduced to avoid db limits
     */
    int initialIssueListColumnsSize = 0;
    
    /**
     * cache list so that we always return the same object for
     * all invocations within a single request
     */
    List issueListColumns = null;

    /**
     * A ReportGenerator
     */
    private ReportBridge reportGenerator = null;
    
    private int nbrPages = 0;
    private int prevPage = 0;
    private int nextPage = 0;
    private String cachedNextIssueId;
    private String cachedPrevIssueId;

    /* messages usually set in actions */
    private Object confirmMessage;
    private Object infoMessage;
    private Object alertMessage;

    /** The time zone that will be used when formatting dates */
    private final TimeZone timezone;
    
    
    /**
     * Constructor does initialization stuff
     */    
    public ScarabRequestTool()
    {
        recycle();
        timezone = TIME_ZONE == null ? null : TimeZone.getTimeZone(TIME_ZONE);
    }

    /**
     * This method expects to get a RunData object
     */
    public void init(Object data)
    {
        this.data = (RunData)data;

    }

    /**
     * nulls out the issue and user objects
     */
    public void refresh()
    {
        user = null;
        issue = null;
        attribute = null;
        attachment = null;
        depend = null;
        query = null;
        templateInfo = null;
        issueType = null;
        group = null;
        reportingIssue = null;
        module = null;
        attributeOption = null;
        roo = null;
        pcao = null;
        if (issueSearch != null)
        {
            // This must _always_ be called by dispose()
            Log.get().debug("IssueSearch object is disposed of properly.");
            issueSearch.close();
            IssueSearchFactory.INSTANCE.notifyDone();
            issueSearch = null;
        }
        issueList = null;
        issueListColumns = null;
        initialIssueListColumnsSize = 0;
        reportGenerator = null;
        nbrPages = 0;
        prevPage = 0;
        nextPage = 0;
        confirmMessage = null;
        infoMessage = null;
        alertMessage = null;
        cachedPrevIssueId = null;
        cachedNextIssueId = null;
    }

    /**
     * Sets the <code>Alert!</code> message for this request.
     *
     * @param message The alert message to set.
     */
    public void setAlert(Object message)
    {
        this.alert = message;
    }

    /**
     * Retrieves any <code>Alert!</code> message which has been set.
     *
     * @return The alert message.
     */
    public Object getAlert()
    {
        return alert;
    }

    /**
     * A Attachment object for use within the Scarab API
     */
    public void setAttachment(Attachment attachment)
    {
        this.attachment = attachment;
    }

    /**
     * A Attribute object for use within the Scarab API.
     */
    public void setAttribute (Attribute attribute)
    {
        this.attribute = attribute;
    }

    /**
     * A Depend object for use within the Scarab API.
     */
    public void setDepend (Depend depend)
    {
        this.depend = depend;
    }

    /**
     * A Query object for use within the Scarab API.
     */
    public void setQuery (Query query)
    {
        this.query = query;
    }

    /**
     * Get the intake tool.
     */
    private IntakeTool getIntakeTool()
    {
        return (IntakeTool)org.apache.turbine.modules.Module.getTemplateContext(data)
            .get(ScarabConstants.INTAKE_TOOL);
    }

    /**
     * Gets an instance of a ROptionOption from this tool.
     * if it is null it will return a new instance of an 
     * empty ROptionOption and set it within this tool.
     */
    public ROptionOption getROptionOption()
    {
        if (roo == null)
        {
            roo = ROptionOption.getInstance();
        }
        return roo;
    }

    /**
     * Sets an instance of a ROptionOption
     */
    public void setROptionOption(ROptionOption roo)
    {
        this.roo = roo;
    }


    /**
     * A IssueTemplateInfo object for use within the Scarab API.
     */
    public void setIssueTemplateInfo (IssueTemplateInfo templateInfo)
    {
        this.templateInfo = templateInfo;
    }

    /**
     * A IssueType object for use within the Scarab API.
     */
    public void setIssueType (IssueType issuetype)
    {
        this.issueType = issuetype;
    }


    /**
     * Gets an instance of a ParentChildAttributeOption from this tool.
     * if it is null it will return a new instance of an 
     * empty ParentChildAttributeOption and set it within this tool.
     */
    public ParentChildAttributeOption getParentChildAttributeOption()
    {
        if (pcao == null)
        {
            pcao = ParentChildAttributeOption.getInstance();
        }
        return pcao;
    }

    /**
     * Sets an instance of a ParentChildAttributeOption
     */
    public void setParentChildAttributeOption(ParentChildAttributeOption roo)
    {
        this.pcao = roo;
    }

    /**
     * A Attribute object for use within the Scarab API.
     */
    public void setAttributeOption (AttributeOption option)
    {
        this.attributeOption = option;
    }

    /**
     * A Attribute object for use within the Scarab API.
     */
    public AttributeOption getAttributeOption()
        throws Exception
    {
        if (attributeOption == null)
        {
            String optId = getIntakeTool()
                .get("AttributeOption", IntakeTool.DEFAULT_KEY)
                .get("OptionId").toString();
            if (optId == null || optId.length() == 0)
            {
                attributeOption = AttributeOption.getInstance();
            }
            else 
            {
                attributeOption = AttributeOptionManager
                    .getInstance(new Integer(optId));
            }
        }
        return attributeOption;
    }

    /**
     * A <code>User</code> object for use within the Scarab API,
     * generally <i>not</i> the user who is logged in.
     */
    public void setUser(ScarabUser user)
    {
        this.user = user;
    }

    /**
     * A <code>User</code> object for use within the Scarab API. This
     * is the result of whatever was set with <code>setUser()</code>
     * (generally <i>not</i> the user who is logged in).  It can
     * return <code>null</code> if <code>setUser()</code> has not been
     * previously called.  If you would like to get the currently
     * logged in <code>User</code>, retrieve that from the
     * data.getUser() method.
     *
     * @return A user used during this request.
     */
    public ScarabUser getUser()
    {
        return this.user;
    }

    /**
     * Return a specific User by ID from within the system.
     * You can pass in either a Integer or something that
     * will resolve to a String object as id.toString() is 
     * called on everything that isn't a Integer.
     */
    public ScarabUser getUser(Object id)
     throws Exception
    {
        if (id == null)
        {
            return null;
        }
        ScarabUser su = null;
        try
        {
            Integer pk = null;
            if (id instanceof Integer)
            {
                pk = (Integer) id;
            }
            else
            {
                pk = new Integer(id.toString());
            }
            su = ScarabUserManager.getInstance(pk);
        }
        catch (Exception e)
        {
            // Logged at debug level, as a null user is interpreted 
            // as an invalid user id
            Log.get().debug("User with user id "+ id +" could not be found,", e);
        }
        return su;
    }

    /**
     * Return a specific User by username.
     */
    public ScarabUser getUserByUserName(String username)
     throws Exception
    {
        ScarabUser su = null;
        try
        {
            su = ScarabUserManager
                .getInstance(username, getCurrentModule().getDomain());
        }
        catch (Exception e)
        {
            // Logged at debug level, as a null user is interpreted 
            // as an invalid username
            Log.get().debug("User, "+username+" could not be found,", e);
        }
        return su;
    }

    /**
     * @return An {@link org.tigris.scarab.om.Attribute} object
     * (possibly "blank", but never <code>null</code>).
     */
    public Attribute getAttribute()
        throws Exception
    {
        try
        {
            if (attribute == null)
            {
                String attId = getIntakeTool()
                    .get("Attribute", IntakeTool.DEFAULT_KEY)
                    .get("Id").toString();
                if (attId == null || attId.length() == 0)
                {
                    attId = data.getParameters().getString("attId");
                    if (attId == null || attId.length() == 0)
                    { 
                        attribute = AttributeManager.getInstance();
                    }
                    else 
                    {
                        attribute = AttributeManager.getInstance(new Integer(attId));
                    }
                }
                else 
                {
                    attribute = AttributeManager.getInstance(new Integer(attId));
                }
            }
        }
        catch (Exception e)
        {
            e.printStackTrace();
        }
        return attribute;
    }

    /**
     * A Attribute object for use within the Scarab API.
     */
    public Attribute getAttribute(Integer pk)
     throws Exception
    {
        Attribute attr = null;
        try
        {
           attr = AttributeManager.getInstance(pk);
        }
        catch (Exception e)
        {
            e.printStackTrace();
        }
        this.attribute = attr;
        return attr;
    }

    /**
     * A Attribute object for use within the Scarab API.
     */
    public AttributeOption getAttributeOption(Integer pk)
     throws Exception
    {
        try
        {
           attributeOption = AttributeOptionManager.getInstance(pk);
        }
        catch(Exception e)
        {
            e.printStackTrace();
        }
        return attributeOption;
    }

    public AttributeOption getAttributeOption(String key)
         throws Exception
    {
        return getAttributeOption(new Integer(key));
    }    

    /**
     * First attempts to get the RModuleUserAttributes from the user.
     * If it is empty, then it will try to get the defaults from the module.
     * If anything fails, it will return an empty list.
     */
    public List getRModuleUserAttributes()
    {
        ScarabUser user = (ScarabUser)data.getUser();
        
        if(issueListColumns == null){
            ScarabLocalizationTool l10n = getLocalizationTool();
            ScarabToolManager toolManager = new ScarabToolManager(l10n);        	
        	issueListColumns= toolManager.getRModuleUserAttributes(user, module, issueType);
        	initialIssueListColumnsSize = issueListColumns.size();
        }                
        
        // DEP: Not sure about this initial list stuff, or if we need it..
        if (initialIssueListColumnsSize > issueListColumns.size())
        {
            TemplateContext context =
                (TemplateContext) data.getTemp(Turbine.CONTEXT);
            context.put("columnLimitExceeded", Boolean.TRUE);
        }
        return issueListColumns;
    }
    

    public List getValidIssueListAttributes()
    {
        ScarabUser user = (ScarabUser)data.getUser();
        List result = null;
        try
        {
            MITList currentList = user.getCurrentMITList();
            if (currentList != null)
            {
                result = currentList.getCommonAttributes(false);
            }
        }
        catch (Exception e)
        {
            Log.get().error("Could not get list attributes", e);
        }
        if (result == null)
        {
            result = Collections.EMPTY_LIST;
        }
        return result;
    }

    /**
     * A Query object for use within the Scarab API.
     */
    public Query getQuery()
        throws Exception
    {
        try
        {
            if (query == null)
            {
                String queryId = data.getParameters()
                    .getString("queryId"); 
                if (queryId == null || queryId.length() == 0)
                {
                    query = Query.getInstance();
                }
                else 
                {
                    query = QueryManager
                        .getInstance(new NumberKey(queryId), false);
                }
            }        
        }        
        catch (Exception e)
        {
            e.printStackTrace();
        }
        return query;
    }

    /**
     * A IssueTemplateInfo object for use within the Scarab API.
     */
    public IssueTemplateInfo getIssueTemplateInfo()
        throws Exception
    {
        try
        {
            if (templateInfo == null)
            {
                String templateId = data.getParameters()
                    .getString("templateId"); 

                if (templateId == null || templateId.length() == 0)
                {
                    templateInfo = IssueTemplateInfo.getInstance();
                }
                else 
                {
                    templateInfo = IssueTemplateInfoManager
                        .getInstance(new NumberKey(templateId), false);
                }
            }        
        }        
        catch (Exception e)
        {
            e.printStackTrace();
        }
        return templateInfo;
    }

    /**
     * An Enter issue template.
     */
    public Issue getIssueTemplate()
     throws Exception
    {
        Issue template = null;
        String templateId = data.getParameters()
            .getString("templateId"); 
        try
        {
            if (templateId == null || templateId.length() == 0)
            {
                template = getCurrentModule().getNewIssue(getIssueType(
                                   getCurrentIssueType().getTemplateId().toString()));
            }
            else 
            {
                template = IssueManager
                    .getInstance(new NumberKey(templateId), false);
            }
        }        
        catch (Exception e)
        {
            e.printStackTrace();
        }
        return template;
    }

    /**
     * An Enter issue template.
     */
    public Issue getIssueTemplate(String templateId)
        throws Exception
    {
        Issue template = null;
        try
        {
            if (templateId == null || templateId.length() == 0)
            {
                setAlertMessage(L10NKeySet.NoTemplateId);
            }
            else 
            {
                template = IssueManager
                    .getInstance(new NumberKey(templateId), false);
            }
        }        
        catch (Exception e)
        {
            e.printStackTrace();
        }
        return template;
    }

    /**
     * A Depend object for use within the Scarab API.
     */
    public Depend getDepend()
     throws Exception
    {
        try
        {
            if (depend == null)
            {
                String dependId = getIntakeTool()
                    .get("Depend", IntakeTool.DEFAULT_KEY).get("Id").toString();
                if (dependId == null || dependId.length() == 0)
                {
                    depend = DependManager.getInstance();
                }
                else 
                {
                    depend = DependManager
                        .getInstance(new NumberKey(dependId), false);
                }
            }        
        }        
        catch (Exception e)
        {
            e.printStackTrace();
        }
        return depend;
    }

    /**
     * Get reason for modification.
     */
    public String getActivityReason(ActivitySet activitySet, Activity activity)
     throws Exception
    {   
        ScarabLocalizationTool l10n = getLocalizationTool();
        ScarabToolManager toolManager = new ScarabToolManager(l10n);    	
        return toolManager.getActivityReason(activitySet,activity);
        
    }
            
        
    /**
     * A Attachment object for use within the Scarab API.
     */
    public Attachment getAttachment()
        throws Exception
    {
		try
		{
	        if (attachment == null)
	        {
	            Group att = getIntakeTool()
	                .get("Attachment", IntakeTool.DEFAULT_KEY, false);
	            if (att != null) 
	            {            
	                String attId =  att.get("Id").toString();
	                if (attId == null || attId.length() == 0)
	                {
	                    attachment = new Attachment();
	                }
	                else 
	                {
	                    attachment = AttachmentManager
	                        .getInstance(new NumberKey(attId), false);
	                }
	            }
	            else 
	            {
	                attachment = new Attachment();
	            }
	        }        
		}
		catch(Exception e)
		{
		e.printStackTrace(); throw e; //EXCEPTION
		}
        return attachment;
    }

    /**
     * A AttributeGroup object for use within the Scarab API.
     */
    public AttributeGroup getAttributeGroup()
        throws Exception
    {
           AttributeGroup group = null;
try
{
            String attGroupId = getIntakeTool()
                .get("AttributeGroup", IntakeTool.DEFAULT_KEY)
                .get("AttributeGroupId").toString();
            if (attGroupId == null || attGroupId.length() == 0)
            {
                group = new AttributeGroup();
            }
            else 
            {
                group = AttributeGroupManager
                    .getInstance(new NumberKey(attGroupId), false);
            }
}
catch(Exception e)
{
e.printStackTrace();
}
        return group;
 
   }
    /**
     * Get a AttributeGroup object.
     */
    public AttributeGroup getAttributeGroup(String key)
    {
        AttributeGroup group = null;
        try
        {
            group = AttributeGroupManager
                .getInstance(new NumberKey(key), false);
        }
        catch (Exception e)
        {
            e.printStackTrace();
        }
        return group;
    }

    /**
     * Get a specific issue type by key value. Returns null if
     * the Issue Type could not be found
     *
     * @param key a <code>String</code> value
     * @return a <code>IssueType</code> value
     */
    public IssueType getIssueType(String key)
    {
        IssueType issueType = null;
        try
        {
            issueType = IssueTypeManager
                .getInstance(new NumberKey(key), false);
        }
        catch (Exception e)
        {
            // Swallow me!
        }
        return issueType;
    }

    /**
     * Get an issue type object.
     */
    public IssueType getIssueType()
        throws Exception
    {
        if (issueType == null) 
        {
            String key = data.getParameters()
                .getString("issuetypeid");
            if (key == null)
            {
                // get new issue type
                issueType = new IssueType();
            }
            else 
            {
                try
                {
                    issueType = IssueTypeManager
                        .getInstance(new NumberKey(key), false);
                }
                catch (Exception e)
                {
                    issueType = new IssueType();
                }
            }
        }
        return issueType;
    }


    /**
     * Gets a new instance of AttributeValue
     */
    public AttributeValue getNewAttributeValue(Attribute attribute, Issue issue)
        throws Exception
    {
        
        return AttributeValue.getNewInstance(attribute.getAttributeId(),issue);
    }

    /**
     * Get an RModuleAttribute object. 
     *
     * @return a <code>Module</code> value
     */
    public RModuleAttribute getRModuleAttribute()
        throws Exception
    {
        RModuleAttribute rma = null;
try
{
            ComboKey rModAttId = (ComboKey)getIntakeTool()
                .get("RModuleAttribute", IntakeTool.DEFAULT_KEY)
                .get("Id").getValue();
            if (rModAttId == null)
            {
                Integer attId = (Integer)getIntakeTool()
                    .get("Attribute", IntakeTool.DEFAULT_KEY)
                    .get("Id").getValue();
                Module currentModule = getCurrentModule();
                if (attId != null && currentModule != null)
                {
                    SimpleKey[] nka = {
                        SimpleKey.keyFor(attId), 
                        SimpleKey.keyFor(currentModule.getModuleId())
                    };
                    rma = RModuleAttributeManager
                        .getInstance(new ComboKey(nka), false);
                }
                else 
                {
                    rma = new RModuleAttribute();
                }
            }
            else 
            {
                rma = RModuleAttributeManager.getInstance(rModAttId, false);
            }
}
catch(Exception e)
{
e.printStackTrace();
}
        return rma;
    }

    /**
     * A AttributeGroup object for use within the Scarab API.
     */
    public void setAttributeGroup(AttributeGroup group)
    {
        this.group = group;
    }

    /**
     * A Module object for use within the Scarab API.
     */
    public void setModule(Module module)
    {
        this.module = module;
    }

    /**
     * Get an Module object. 
     *
     * @return a <code>Module</code> value
     */
    public Module getModule()
        throws Exception
    {
try
{
        String modId = getIntakeTool()
            .get("Module", IntakeTool.DEFAULT_KEY).get("Id").toString();
        if (modId == null || modId.length() == 0)
        {
            module = ModuleManager.getInstance();
        }
        else 
        {
            module = ModuleManager.getInstance(new Integer(modId));
        }
}
catch(Exception e)
{
e.printStackTrace();
}
       return module;
    }

    /**
     * Get a specific module by key value. Returns null if
     * the Module could not be found
     *
     * @param key a <code>String</code> value
     * @return a <code>Module</code> value
     */
    public Module getModule(String key)
    {
        Module me = null;
        if (key != null && key.length() > 0) 
        {
            try
            {
                me = ModuleManager.getInstance(new Integer(key));
            }
            catch (Exception e)
            {
                Log.get().info("[ScarabRequestTool] Unable to retrieve Module: " +
                         key, e);
            }
        }
        return me;
    }

    /**
     * Gets the Module associated with the information
     * passed around in the query string. Returns null if
     * the Module could not be found.
     */
    public Module getCurrentModule()
    {
        ScarabUser user = (ScarabUser)data.getUser();
        Module currentModule = null;
        if (user != null) 
        {
            currentModule = user.getCurrentModule();            
        }

        return currentModule;
    }

    /**
     * Gets the IssueType associated with the information
     * passed around in the query string.
     */
    public IssueType getCurrentIssueType() throws Exception
    {
        ScarabUser user = (ScarabUser)data.getUser();
        return (user == null) ? null : user.getCurrentIssueType();            
    }

    public void setCurrentIssueType(IssueType type)
    {
        ScarabUser user = (ScarabUser)data.getUser();
        if (user != null) 
        {
            user.setCurrentIssueType(type);
        }        
    }

    /**
     * Looks at the current RModuleIssueType and if it is null,
     * returns the users homepage. If it is not null, and is 
     * dedupe, returns Wizard1...else Wizard3.
     */
    public String getNextEntryTemplate(IssueType issueType)
    {
        RModuleIssueType rmit = null;
        String nextTemplate = null;
        try
        {
            Module module = getCurrentModule();
            if (module == null) 
            {
                nextTemplate = ((ScarabUser)data.getUser()).getHomePage();
                setAlertMessage(L10NKeySet.ModuleIssueTypeRequiredToEnterIssue);                
            }
            else 
            {            
                rmit = module.getRModuleIssueType(issueType);
                if (rmit == null)
                {
                    nextTemplate = ((ScarabUser)data.getUser()).getHomePage();
                    setAlertMessage(L10NKeySet.ModuleIssueTypeRequiredToEnterIssue);
                }
                else if (rmit.getDedupe() && !module
                         .getDedupeGroupsWithAttributes(issueType).isEmpty())
                {
                    nextTemplate = "entry,Wizard1.vm";
                }
                else
                {
                    nextTemplate = "entry,Wizard3.vm";
                }
            }
        }
        catch (Exception e)
        {
            // system would be messed up, if we are here.  Punt
            nextTemplate = "Index.vm";
            setAlertMessage(L10NKeySet.CannotDetermineIssueEntryTemplate);
            Log.get().error("CannotDetermineIssueEntryTemplate", e);
        }
        return nextTemplate;
    }

    /**
     * Returns name of current template
     */
    public String getCurrentTemplate()
    {
        return data.getTarget().replace('/',',');
    }

    /**
     * The issue that is currently being entered.
     *
     * @return an <code>Issue</code> value
     */
    public Issue getReportingIssue()
        throws Exception
    {
        if (reportingIssue == null) 
        {
            String key = data.getParameters()
                .getString(ScarabConstants.REPORTING_ISSUE);

            if (key == null) 
            {
                getNewReportingIssue();
            }
            else 
            {
                reportingIssue = ((ScarabUser)data.getUser())
                    .getReportingIssue(key);

                // if reportingIssue is still null, the parameter must have
                // been stale, just get a new issue
                if (reportingIssue == null) 
                {
                    getNewReportingIssue();                    
                }
            }
        }
        return reportingIssue;
    }

    private void getNewReportingIssue()
        throws Exception
    {
        reportingIssue = getCurrentModule().getNewIssue(getCurrentIssueType());
        String key = ((ScarabUser)data.getUser())
            .setReportingIssue(reportingIssue);
        data.getParameters().add(ScarabConstants.REPORTING_ISSUE, key);
    }

    public void setReportingIssue(Issue issue)
    {
        reportingIssue = issue;
    }

    /**
     * Sets the current Module
     */
    public void setCurrentModule(Module me)
    {
        ScarabUser user = (ScarabUser)data.getUser();
        if (user != null) 
        {
            user.setCurrentModule(me);
        }        
    }

    /**
     * A Issue object for use within the Scarab API.
     */
    public void setIssue(Issue issue)
    {
        this.issue = issue;
    }

    /**
     * Get an Issue object from unique id given either as an intake
     * field or a request parameter keyed with "id".
     *
     * @return a <code>Issue</code> value
     */
    public Issue getIssue()
        throws Exception
    {
        if (issue == null)
        {
            String issueId = null;
            Group issueGroup = getIntakeTool()
                .get("Issue", IntakeTool.DEFAULT_KEY, false);
            if (issueGroup != null) 
            {            
                issueId =  issueGroup.get("Id").toString();
            }
            else
            {
                issueId = data.getParameters().getString("id");
            }
            if (issueId != null && issueId.length() > 0)
            {
                issue = getIssue(issueId);
            }
        }
        return issue;
    }

    /**
     * Takes unique id, and returns issue.
     * if the issue is not valid, then it returns null.
     */
    public Issue getIssue(String id)
    {
        Issue issue = null;
        if (id == null || id.length() == 0)
        {
            setInfoMessage(L10NKeySet.EnterId);
        }
        else
        {
            try
            {
                issue = IssueManager
                    .getIssueById(id, getCurrentModule().getCode());
                if (issue == null)
                {
                    setAlertMessage(L10NKeySet.InvalidId);
                }
                else if (issue.getDeleted())
                {
                    setAlertMessage(L10NKeySet.IssueMoved);
                    issue = null;
                }
            }
            catch (Exception e)
            {
                setAlertMessage(L10NKeySet.InvalidId);
            }
        }
        return issue;
    }

    /**
     * The id may only be the issue's primary key.
     *
     * @param key a <code>String</code> value
     * @return a <code>Issue</code> value
     */
    public Issue getIssueByPk(String key)
    {
        Issue issue = null;
        try
        {
            issue = IssueManager.getInstance(new Long(key));
        }
        catch (Exception e)
        {
            setAlertMessage(L10NKeySet.InvalidIssueId);
        }
        return issue;
    }

    /**
     * Get a list of Issue objects.
     *
     * @return a <code>Issue</code> value
     */
    public List getIssues()
        throws Exception
    {
        List issues = null;

        Group issueGroup = getIntakeTool()
            .get("Issue", IntakeTool.DEFAULT_KEY, false);
        if (issueGroup != null) 
        {            
            Long[] issueIds =  (Long []) issueGroup.get("Ids").getValue();
            if (issueIds != null)
            {
                issues = getIssues(Arrays.asList(issueIds));
            }
        }
        else 
        {
            String[] paramIssueIds = data.getParameters().getStrings("issue_ids");
            if (paramIssueIds != null)
            {
                issues = getIssues(Arrays.asList(paramIssueIds));
            }
        }
        if (issues == null)
        {
            issues = Collections.EMPTY_LIST;
        }
        return issues;
    }

    /**
     * Get a list of Issue objects from a list of issue ids.  The list
     * can contain Strings or Integers, but all ids must be of the same type
     * (String or Integer).
     *
     * @param issueIds a <code>List</code> value
     * @return a <code>List</code> value
     * @exception Exception if an error occurs
     */
    public List getIssues(List issueIds)
        throws Exception
    {
        List issues = null;
        StringBuffer invalidIds = null; 
        if (issueIds == null || issueIds.isEmpty()) 
        {
            issues = Collections.EMPTY_LIST;
        }
        else 
        {
            if (issueIds.get(0) instanceof String) 
            {
                issues = new ArrayList(issueIds.size());
                Iterator i = issueIds.iterator();
                while (i.hasNext()) 
                {
                    String id = (String)i.next();
                    Issue issue = getIssue(id);
                    if (issue == null)
                    {
                        if (invalidIds == null) 
                        {
                            invalidIds = new StringBuffer(id);
                        }
                        else 
                        {
                            invalidIds.append(' ').append(id);
                        }                        
                    }
                    else
                    {
                        issues.add(issue);   
                    } 
                }
                if (invalidIds != null) 
                {                
                    setAlertMessage(getLocalizationTool()
                        .format("SomeIssueIdsNotValid", invalidIds.toString()));
                }
            }
            else if (issueIds.get(0) instanceof Long)
            {
                issues = new ArrayList(issueIds.size());
                Iterator i = issueIds.iterator();
                while (i.hasNext()) 
                {
                    Issue issue = IssueManager.getInstance((Long)i.next());
                    if (issue == null) 
                    {
                        setAlertMessage(L10NKeySet.SomeIssuePKsNotValid);
                    }
                    else 
                    {
                        issues.add(issue);   
                    }                    
                }
            }
            else 
            {
                throw new IllegalArgumentException(
                    "issue ids must be Strings or Longs, not " + 
                    issueIds.get(0).getClass().getName()); //EXCEPTION
            }
        }        
        return issues;
    }
        

    /**
     * Get all scopes.
     */
    public List getScopes()
        throws Exception
    {
        return ScopePeer.getAllScopes();
    }

    /**
     * Get all frequencies.
     */
    public List getFrequencies()
        throws Exception
    {
        return FrequencyPeer.getFrequencies();
    }

    /**
     * Generates link to Issue List page, re-running stored query.
     */
    public String getExecuteLink(String link, Query query)
    {
        // query.getValue() begins with a &
        link = link 
            + "?action=Search&eventSubmit_doSearch=Search" 
            + "&pagenum=1" + query.getValue();

        Long listId = query.getListId();
        if (listId != null) 
        {
            link += '&' + ScarabConstants.CURRENT_MITLIST_ID + '=' + listId;
        }
        else 
        {
            link += '&' + ScarabConstants.REMOVE_CURRENT_MITLIST_QKEY + "=true";
        }
        return link;
     }

    /**
     * Generates link to the Query Detail page.
     */
    public String getEditLink(String link, Query query)
    {
        // query.getValue() begins with a &
        link = link + "?queryId=" + query.getQueryId()
            + "&refine=true"
            + "&action=Search&eventSubmit_doPreparequery=foo"
            + query.getValue();

        Long listId = query.getListId();
        if (listId != null) 
        {
            link += '&' + ScarabConstants.CURRENT_MITLIST_ID + '=' + listId;
        }
        else 
        {
            link += '&' + ScarabConstants.REMOVE_CURRENT_MITLIST_QKEY + "=true";
        }
        return link;
    }

    public Intake getConditionalIntake(String parameter)
        throws Exception
    {
        Intake intake = null;
        String param = data.getParameters().getString(parameter);
        if (param == null) 
        {            
            intake = getIntakeTool();
        }
        else 
        {
            intake = new Intake();
            StringValueParser parser = new StringValueParser();
            parser.parse(param, '&', '=', true);
            intake.init(parser);
        }

        return intake;
    }

    /**
     * Get a new IssueSearch object. 
     *
     * @return a <code>Issue</code> value
     */
    public IssueSearch getNewSearch()
        throws Exception, MaxConcurrentSearchException
    {
        if (issueSearch == null) 
        {
            ScarabUser user = (ScarabUser)data.getUser();
            MITList mitList = user.getCurrentMITList();
            if (mitList == null)
            {
                setAlertMessage(L10NKeySet.NoIssueTypeList);
                Log.get().warn("Attempted to create a new IssueSearch and " +
                               " issue types had not been selected.");
            }
            else 
            {
                issueSearch = 
                    IssueSearchFactory.INSTANCE.getInstance(mitList, user);
                issueSearch.setLocale(getLocalizationTool().getPrimaryLocale());
            }
        }
        return issueSearch; 
    }

    /**
     * Get an IssueSearch object based on a query string.
     *
     * @return a <code>Issue</code> value
     */
    public IssueSearch getPopulatedSearch(String query)
        throws Exception
    {
        IssueSearch search = getNewSearch();
        ScarabLocalizationTool l10n = getLocalizationTool();
        search.setIssueListAttributeColumns(getRModuleUserAttributes());

        Intake intake = null;

        if (query == null)
        {
            setInfoMessage(L10NKeySet.EnterQuery);
            return null;
        }
        else
        {
           intake = parseQuery(query);
           
           if (!intake.isAllValid())
           {
               return null;
           }
        }

        // If they have entered users to search on, add them to the search
        StringValueParser parser = new StringValueParser();
        parser.parse(query, '&', '=', true);
        String[] userList = parser.getStrings("user_list");
        if (userList != null && userList.length > 0)
        {
            for (int i = 0; i < userList.length; i++)
            {
                String userId = userList[i];
                String[] attrIds = parser.getStrings("user_attr_" + userId);
                if (attrIds != null) 
                {
                    for (int j = 0; j < attrIds.length; j++) 
                    {
                        search.addUserCriteria(userId, attrIds[j]);
                    }
                }
            }
        }

        // Set intake properties
        boolean searchSuccess = true;
        Group searchGroup = intake.get("SearchIssue", 
                                       search.getQueryKey());
        
        Field minDate = searchGroup.get("MinDate");
        if (minDate != null && minDate.toString().length() > 0)
        { 
           searchSuccess = checkDate(search, minDate.toString());
        }
        
        Field maxDate = searchGroup.get("MaxDate");
        if (maxDate != null && maxDate.toString().length() > 0)
        { 
            searchSuccess = checkDate(search, maxDate.toString());
        }
        
        Field stateChangeFromDate = searchGroup.get("StateChangeFromDate");
        if (stateChangeFromDate != null 
            && stateChangeFromDate.toString().length() > 0)
        { 
            searchSuccess = checkDate(search, stateChangeFromDate.toString());
        }
        
        Field stateChangeToDate = searchGroup.get("StateChangeToDate");
        if (stateChangeToDate != null 
            && stateChangeToDate.toString().length() > 0)
        { 
            searchSuccess = checkDate(search, stateChangeToDate.toString());
        }
        
        if (!searchSuccess)
        {
            setAlertMessage(l10n.format("DateFormatPrompt",
                    L10NKeySet.ShortDateDisplay));
            return null;
        }
        
        try
        {
            searchGroup.setProperties(search);
        }
        catch (Exception e)
        {
            setAlertMessage(l10n.getMessage(e));
            return null;
        }
        
        Integer oldOptionId = search.getStateChangeFromOptionId();
        if (oldOptionId != null && oldOptionId.intValue() != 0
             && oldOptionId.equals(search.getStateChangeToOptionId())) 
        {
            setAlertMessage(L10NKeySet.StateChangeOldEqualNew);
            return null;
        }
        
        // Set attribute values to search on
        LinkedMap avMap = search.getCommonAttributeValuesMap();
        Iterator i = avMap.mapIterator();
        while (i.hasNext()) 
        {
            AttributeValue aval = (AttributeValue)avMap.get(i.next());
            Group group = intake.get("AttributeValue", aval.getQueryKey());
            if (group != null) 
            {
                group.setProperties(aval);
            }                
        }
        
        // If user is sorting on an attribute, set sort criteria
        // Do not use intake, since intake parsed from query is not the same
        // As intake passed from the form
        String sortColumn = data.getParameters().getString("sortColumn");
        if (sortColumn != null && sortColumn.length() > 0 
            && StringUtils.isNumeric(sortColumn))
        {
            search.setSortAttributeId(new Integer(sortColumn));
        }
        
        String sortPolarity = data.getParameters().getString("sortPolarity");
        if (sortPolarity != null && sortPolarity.length() > 0)
        {
            search.setSortPolarity(sortPolarity);
        }
        
        return search;
    }

    /**
     * Get an IssueSearch object based on current query string.
     *
     * @return a <code>Issue</code> value
     */
    public IssueSearch getPopulatedSearch()
        throws Exception
    {
        String currentQueryString = ((ScarabUser)data.getUser()).getMostRecentQuery();
        return getPopulatedSearch(currentQueryString);
    }

    /**
     * Parses query into intake values.
    */
    public Intake parseQuery(String query)
        throws Exception
    {
        Intake intake = new Intake();
        StringValueParser parser = new StringValueParser();
        parser.parse(query, '&', '=', true);
        
        intake.init(parser);
        return intake;
    }


    /**
     * Performs search on current query (which is stored in user session).
    */
    public IteratorWithSize getCurrentSearchResults()
    {
        IteratorWithSize matchingIssueIds = null;
        try 
        {
            matchingIssueIds = getUnprotectedCurrentSearchResults();
        }
        catch (MaxConcurrentSearchException e)
        {
            setAlertMessage(L10NKeySet.ResourceLimitationsPreventedSearch);
        }
        catch (ComplexQueryException e)
        {
            matchingIssueIds = IteratorWithSize.EMPTY;
            setAlertMessage(new SimpleSkipFiltering(getLocalizationTool()
                .format("SearchAbortedDueToComplexity", 
                        new SnippetRenderer(data, "ComplexQueryHelpLink.vm"))));
        }
        catch (Exception e)
        {
            matchingIssueIds = IteratorWithSize.EMPTY;
            L10NMessage l10nMessage = new L10NMessage(L10NKeySet.ErrorProcessingQuery,e);
            setAlertMessage(l10nMessage);
            Log.get().info("Error processing a query", e);
        }
        
        return matchingIssueIds;
    }

    /**
     * Caches the result of getUncachedCurrentSearchResults for the remainder
     * of the request.
     */
    private IteratorWithSize getUnprotectedCurrentSearchResults()
        throws Exception
    {
        // normally we would use "this" as the first arg to ScarabCache.get,
        // but SRT is not serializable.  The result is a mix of a query string
        // and an MITList and the two should not vary over the course of one
        // request so use the query string as the key. We were using the 
        // IssueSearch returned by getNewSearch as the key, but we have to
        // call user.getMostRecentQuery prior to getNewSearch, so using
        // that instead.
        String queryString = ((ScarabUser)data.getUser()).getMostRecentQuery();
        IteratorWithSize results = null;
        Object obj = 
            ScarabCache.get(queryString, "getUnprotectedCurrentSearchResults");
        if (obj == null) 
        {       
            results = getUncachedCurrentSearchResults();
            ScarabCache.put(results, queryString, 
                            "getUnprotectedCurrentSearchResults");
        }
        else 
        {
            results = (IteratorWithSize)obj;
        }
        return results;
    }

    /**
     * Performs search on current query (which is stored in user session).
     */
    private IteratorWithSize getUncachedCurrentSearchResults()
        throws Exception
    {
        ScarabLocalizationTool l10n = getLocalizationTool();
        ScarabUser user = (ScarabUser)data.getUser();
        String currentQueryString = user.getMostRecentQuery();
        IssueSearch search = getPopulatedSearch(currentQueryString);
        IteratorWithSize queryResults = null;

        // Do search
        try
        {
            if (search == null) 
            {
                // an alert message should have been set while attempting
                // to populate the search.
                queryResults = IteratorWithSize.EMPTY;
            }
            else 
            {
                queryResults = search.getQueryResults();
                if (!queryResults.hasNext())
                {
                    setInfoMessage(L10NKeySet.NoMatchingIssues);
                }
            }
        }
        catch (ScarabException e)
        {
            String queryError = e.getMessage();
            if (queryError.startsWith(SearchIndex.PARSE_ERROR)) 
            {
                Log.get().info(queryError);
                setAlertMessage(new SimpleSkipFiltering(
                    l10n.format("QueryParserError", 
                        new SnippetRenderer(data, "TextQueryHelp.vm"))));
            }
            else 
            {
                throw e; //EXCEPTION
            }
        }
        return queryResults;
    }

    /**
     * Gets the number of results for the current query.  Looks first
     * at the URL, then requeries if size information is missing.
     */
    public int getCurrentSearchResultsSize()
    {
        String[] prevNextList = data.getParameters().getStrings("issueList");
        int result = 0;
        if (prevNextList == null) 
        {
            result = getCurrentSearchResults().size();
        }
        else 
        {
            result = Integer.parseInt(prevNextList[1]);
        }
        return result;
    }

    /**
     * Returns the issue's position (1-based) in current issue list.
     */
    public int getIssuePosInList()
        throws Exception, ScarabException
    {
        int issuePos = -1;
        String id = getIssue().getUniqueId();
        String[] prevNextList = data.getParameters().getStrings("issueList");
        if (prevNextList != null) 
        {
            int listOffset = Math.max(0, Integer.parseInt(prevNextList[0]));
            for (int i=2; i<prevNextList.length; i++)
            {
                if (prevNextList[i].equals(id)) 
                {
                    issuePos = listOffset + i - 1;
                    break;
                }
            }        
        }

        return (issuePos <= 0) ? 1 : issuePos;
    }

    /**
     * Returns next issue id in list.
     */
    public String getNextIssue()
        throws Exception, ScarabException
    {
        String nextIssueId = null;
        String[] prevNextList = data.getParameters().getStrings("issueList");
        if (prevNextList != null) 
        {
            String id = getIssue().getUniqueId();
            for (int i=2; i<prevNextList.length-1; i++)
            {
                if (prevNextList[i].equals(id)) 
                {
                    nextIssueId = prevNextList[i+1];
                    break;
                }
            }
        }
        
        if (nextIssueId == null) 
        {
            if (cachedNextIssueId == null) 
            {
                int issuePos = getIssuePosInList();
                if (issuePos <= getCurrentSearchResultsSize())
                {
                    resetIssueIdList(issuePos);
                    nextIssueId = cachedNextIssueId;
                }
            }
            else 
            {
                nextIssueId = cachedNextIssueId;
            }
        }
        return nextIssueId;
    }

    /**
     * Returns previous issue id in list.
     */
    public String getPrevIssue()
        throws Exception, ScarabException
    {
        String prevIssueId = null;
        String[] prevNextList = data.getParameters().getStrings("issueList");
        if (prevNextList != null) 
        {
            String id = getIssue().getUniqueId();
            for (int i=3; i<prevNextList.length; i++)
            {
                if (prevNextList[i].equals(id)) 
                {
                    prevIssueId = prevNextList[i-1];
                    break;
                }
            }
        }
        
        if (prevIssueId == null) 
        {
            if (cachedPrevIssueId == null) 
            {
                int issuePos = getIssuePosInList();
                if (issuePos > 1)
                {
                    resetIssueIdList(issuePos);
                    prevIssueId = cachedPrevIssueId;
                }
            }
            else 
            {
                prevIssueId = cachedPrevIssueId;
            }
        }
        return prevIssueId;
    }

    private void resetIssueIdList(int issuePos)
    {
        IteratorWithSize idList = getCurrentSearchResults();
        ParameterParser pp = data.getParameters();
        pp.remove("issueList");
        int min = issuePos - 5;
        int max = issuePos + 10;
        pp.add("issueList", min);
        pp.add("issueList", idList.size());

        int count;
        for (count = 0; idList.hasNext() && count < min; count++) 
        {
            idList.next();
        }
        for (; idList.hasNext() && count < issuePos - 2; count++) 
        {
            pp.add("issueList",
                   ((QueryResult) idList.next()).getUniqueId());
        }
        if (idList.hasNext()) 
        {
            cachedPrevIssueId = ((QueryResult)idList.next())
                .getUniqueId();                        
            pp.add("issueList", cachedPrevIssueId);
        }
        if (idList.hasNext()) 
        {
            pp.add("issueList",
                   ((QueryResult) idList.next()).getUniqueId());
        }
        if (idList.hasNext()) 
        {
            cachedNextIssueId = ((QueryResult)idList.next())
                .getUniqueId();                        
            pp.add("issueList", cachedNextIssueId);
        }
        for (count += 3; idList.hasNext() && count < max; count++) 
        {
            pp.add("issueList",
                   ((QueryResult) idList.next()).getUniqueId());
        }
    }

    /**
     * Attempts to parse a date passed in the query page.
    */
    private boolean checkDate(IssueSearch search, String date)
        throws Exception
    {
        boolean success = true;
        try
        {
            search.parseDate(date, false);
        }
        catch (Exception e)
        {
            success = false;
        }
        return success;
    }


    /**
     * Convert paths with slashes to commas.
     */
    public String convertPath(String path)
        throws Exception
    {
            return path.replace('/',',');
    }

    /**
     * Returns all issue templates that are global, 
     * Plus those that are personal and created by logged-in user.
    */
    public List getAllIssueTemplates(IssueType issueType)
        throws Exception
    {
        ParameterParser params = data.getParameters();
        String sortColumn = params.getString("sortColumn", "name");
        String sortPolarity = params.getString("sortPolarity", "asc");
        return IssueTemplateInfoPeer.getTemplates(getCurrentModule(),
               issueType, (ScarabUser)data.getUser(), 
               sortColumn, sortPolarity, IssueTemplateInfoPeer.TYPE_ALL);
    }

    /**
     * Returns templates that are personal and created by logged-in user.
    */
    public List getPrivateTemplates(IssueType issueType)
        throws Exception
    {
        return IssueTemplateInfoPeer.getTemplates(getCurrentModule(),
               issueType, (ScarabUser)data.getUser(), 
               "name", "asc", IssueTemplateInfoPeer.TYPE_PRIVATE);
    }

    /**
     * Returns templates that are personal and created by logged-in user.
    */
    public List getGlobalTemplates(IssueType issueType)
        throws Exception
    {
        return IssueTemplateInfoPeer.getTemplates(getCurrentModule(),
               issueType, (ScarabUser)data.getUser(), 
               "name", "asc", IssueTemplateInfoPeer.TYPE_GLOBAL);
    }

    /**
     * Returns all queries that are global, 
     * Plus those that are personal and created by logged-in user.
    */
    public List getAllQueries()
        throws Exception
    {
        String sortColumn = data.getParameters().getString("sortColumn");
        String sortPolarity = data.getParameters().getString("sortPolarity");
        if (sortColumn == null)
        {
            sortColumn = "avail";
        }
        if (sortPolarity == null)
        {
            sortPolarity = "desc";
        }
        return QueryPeer.getQueries(getCurrentModule(),
               null, (ScarabUser)data.getUser(), 
               sortColumn, sortPolarity, IssueTemplateInfoPeer.TYPE_ALL);
    }

    /**
     * Returns queries that are personal and created by logged-in user.
    */
    public List getPrivateQueries()
        throws Exception
    {
        return QueryPeer.getQueries(getCurrentModule(),
               null, (ScarabUser)data.getUser(), 
               "name", "asc", IssueTemplateInfoPeer.TYPE_PRIVATE);
    }

    /**
     * Returns all queries that are global.
    */
    public List getGlobalQueries()
        throws Exception
    {
        return QueryPeer.getQueries(getCurrentModule(),
               null, (ScarabUser)data.getUser(), 
               "name", "asc", IssueTemplateInfoPeer.TYPE_GLOBAL);
    }

    /**
     * a report helper class
     */
    public ReportBridge getReport()
        throws Exception
    {
        if (reportGenerator == null) 
       { 
            String key = data.getParameters()
                .getString(ScarabConstants.CURRENT_REPORT);
            ParameterParser parameters = data.getParameters();
            String id = parameters.getString("report_id");
            if (id == null || id.length() == 0) 
            {
                ScarabUser user = (ScarabUser)data.getUser();
                MITList mitlist = user.getCurrentMITList();
                if (key == null) 
                {
                    reportGenerator = getNewReport(mitlist);
                }
                else 
                {
                    reportGenerator = user.getCurrentReport(key);
                    
                    // if reportingIssue is still null, the parameter must have
                    // been stale, just get a new report
                    if (reportGenerator == null && 
                        mitlist != null && !mitlist.isEmpty()) 
                    {
                        reportGenerator = getNewReport(mitlist);
                    }
                }                
            }
            else 
            {
                reportGenerator = new ReportBridge(
                    ReportManager.getInstance(new NumberKey(id), false));
                key = ((ScarabUser)data.getUser())
                    .setCurrentReport(reportGenerator);
                data.getParameters()
                    .remove(ScarabConstants.CURRENT_REPORT);
                data.getParameters()
                    .add(ScarabConstants.CURRENT_REPORT, key);
            }
        }

        return reportGenerator;
    }

    private ReportBridge getNewReport(MITList mitList)
        throws Exception
    {
        if (mitList == null) 
        {
            throw new IllegalArgumentException(
                "Cannot create a new report without any issue types."); //EXCEPTION
        }
        
        ScarabUser user = (ScarabUser)data.getUser();
        org.tigris.scarab.om.Report om  = new org.tigris.scarab.om.Report();
        ReportBridge report = new ReportBridge(om);
        report.setGeneratedBy(user);
        report.setMITList(mitList);
        
        String key = ((ScarabUser)data.getUser()).setCurrentReport(report);
        data.getParameters().add(ScarabConstants.CURRENT_REPORT, key);

        return report;
    }

    public void setReport(ReportBridge report)
    {
        this.reportGenerator = report;
    }
    
    /**
     *  Full featured, paginated, sorted method for returning the results 
     *  of user search.  Returns all users (no search criteria). 
     */
    public ScarabPaginatedList getUserSearchResults(MITList mitList, 
                                                    int pageNum, 
                                                    int resultsPerPage, 
                                                    String sortColumn, 
                                                    String sortPolarity, 
                                                    boolean includeCommitters)
        throws Exception
    {
        return userFilteredSearchResults(mitList, pageNum, resultsPerPage, 
                                         sortColumn, sortPolarity, "", "", 
                                         includeCommitters);
    }

    /**
     * Full featured, paginated, sorted version for returning results 
     * of a user search.
     */
    public ScarabPaginatedList getUserFilteredSearchResults(MITList mitList, 
        int pageNum, int resultsPerPage,  String sortColumn, 
        String sortPolarity, boolean includeCommitters)
        throws Exception
    {        
        String searchString = data.getParameters()
               .getString("searchString"); 
        String searchField = data.getParameters()
               .getString("searchField"); 

        if (searchField == null)
        {
            setInfoMessage(L10NKeySet.SearchFieldPrompt);
            return null ;
        }
        
        return userFilteredSearchResults(mitList, pageNum, resultsPerPage, 
                                         sortColumn, sortPolarity, 
                                         searchString, searchField, includeCommitters);

    }

    private ScarabPaginatedList userFilteredSearchResults(MITList mitList,
                                                          int pageNum,
                                                          int resultsPerPage,
                                                          String sortColumn,
                                                          String sortPolarity, 
                                                          String searchString,
                                                          String searchField,
                                                          boolean includeCommitters)
        throws Exception
    {
        ScarabPaginatedList list = null;
        String name = null;
        String userName = null;

        if ("FullName".equalsIgnoreCase(searchField))
        {
            name = searchString;
        }
        else if ("UserName".equalsIgnoreCase(searchField))
        {
            userName = searchString;
        }

        try 
        {
            list = getCurrentModule().getUsers(name, userName, mitList,
                                               pageNum, resultsPerPage, 
                                               sortColumn, sortPolarity,
                                               includeCommitters);
        } 
        catch (Exception e)
        {
            Log.get().error("Problem getting user list", e);
            list = new ScarabPaginatedList();
        }

        // These are object members are used by GlobalMacros.vm via
        // the bean interface.  Leave them here until all users of the
        // paginate macro can be updated.
        this.nbrPages = list.getNumberOfPages();
        this.nextPage = list.getNextPageNumber();
        this.prevPage = list.getPrevPageNumber();

        return list;
    }
                                

    /**
     * Return results of attribute search.
     */
    public List getAttributeSearchResults()  throws Exception
    {
        String searchString = data.getParameters()
               .getString("searchString"); 
        String searchField = data.getParameters()
               .getString("searchField"); 
        if (searchField == null)
        {
            setInfoMessage(L10NKeySet.SearchFieldPrompt);
            return null ;
        }
        
        String name = null;
        String description = null;
        if (searchField.equals("Name") || searchField.equals("Any"))
        {
            name = searchString;
        }
        if (searchField.equals("Description") || searchField.equals("Any"))
        {
            description = searchString;
        }

        return sortAttributes(AttributePeer
            .getFilteredAttributes(name, description, searchField));
    }

    /**
     * Sort users on name or email.
     */
    public List sortUsers(List userList)  throws Exception
    {
        final String sortColumn = data.getParameters().getString("sortColumn");
        final String sortPolarity = data.getParameters().getString("sortPolarity");
        final int polarity = ("desc".equals(sortPolarity)) ? -1 : 1;   
        Comparator c = new Comparator() 
        {
            public int compare(Object o1, Object o2) 
            {
                int i = 0;
                if ("username".equals(sortColumn))
                {
                    i =  polarity * ((ScarabUser)o1).getUserName()
                         .compareTo(((ScarabUser)o2).getUserName());
                }
                else
                {
                    i =  polarity * ((ScarabUser)o1).getName()
                         .compareTo(((ScarabUser)o2).getName());
                }
                return i;
             }
        };
        Collections.sort(userList, c);
        return userList;
    }


    /**
     * Sort attributes on name or description.
     */
    public List sortAttributes(List attList)  throws Exception
    {
        final String sortColumn = data.getParameters().getString("sortColumn");
        final String sortPolarity = data.getParameters().getString("sortPolarity");
        final int polarity = ("desc".equals(sortPolarity)) ? -1 : 1;   
        Comparator c = new Comparator() 
        {
            public int compare(Object o1, Object o2) 
            {
                int i = 0;
                if (sortColumn != null && sortColumn.equals("name"))
                {
                    i =  polarity * ((Attribute)o1).getName()
                         .compareTo(((Attribute)o2).getName());
                }
                else
                {
                    i =  polarity * ((Attribute)o1).getDescription()
                         .compareTo(((Attribute)o2).getDescription());
                }
                return i;
             }
        };
        Collections.sort(attList, c);
        return attList;
    }

    /**
     * Return a subset of the passed-in list.
     * 
     * @param nbrItmsPerPage negative value returns full list
     */
    public List getPaginatedList(List fullList, int pgNbr, int nbrItmsPerPage)
    {
        List pageResults = null;
        try 
        {
            if (nbrItmsPerPage < 0) 
            {
                pageResults = fullList;
            }
            else 
            {
                this.nbrPages =  (int)Math.ceil((float)fullList.size() 
                                                / nbrItmsPerPage);
                this.nextPage = pgNbr + 1;
                this.prevPage = pgNbr - 1;
                pageResults = fullList.subList ((pgNbr - 1) * nbrItmsPerPage, 
                    Math.min(pgNbr * nbrItmsPerPage, fullList.size()));
            }
        }
        catch(Exception e)
        {
            Log.get().error("", e);
        }
        return pageResults;
    }

    /**
     * Return a subset of the passed-in list.
     * 
     * @param nbrItmsPerPage negative value returns full list
     */
    public IteratorWithSize getPaginatedIterator(IteratorWithSize fullList, 
                                                 int pgNbr, 
                                                 int nbrItmsPerPage)
    {
        IteratorWithSize pageResults;
        try 
        {
            if (nbrItmsPerPage < 0) 
            {
                pageResults = fullList;
            }
            else 
            {
                this.nbrPages =  (int)Math.ceil((float)fullList.size() 
                                                / nbrItmsPerPage);
                this.nextPage = pgNbr + 1;
                this.prevPage = pgNbr - 1;
                pageResults = new SubsetIteratorWithSize(
                    fullList, (pgNbr - 1) * nbrItmsPerPage, nbrItmsPerPage); 
            }
        }
        catch(Exception e)
        {
            Log.get().error("", e);
            pageResults = IteratorWithSize.EMPTY;
        }
        return pageResults;
    }



    /**
     * Set the value of issueList.
     * @param v  Value to assign to issueList.
     */
    public void setIssueList(List  v) 
    {
        this.issueList = v;
    }

    /**
     * Checks for a query parameter "oldResultsPerPage" and compares it
     * to the current "resultsPerPage" parameter.  If they are different,
     * it returns 1 to avoid returning a value larger than the maximum
     * number of pages; otherwise it returns the value of the query parameter 
     * "pageNum".
     * Preferrable optimization would be to adjust the page number to
     * keep a set of the old displayed items on the new page.
     */
    public int getAdjustedPageNum()
    {
        ParameterParser parameters = data.getParameters();
        int resultsPerPage = parameters.getInt("resultsPerPage", 0);
        int oldResultsPerPage = parameters.getInt("oldResultsPerPage", 0);
        int pageNum = parameters.getInt("pageNum", 1);
        // I seem to be too braindead to come up with a formula to return the
        // new page that will contain the first item on the last page we 
        // viewed.
        // forget it and start over

        // Note that -1 -> All results
        if (oldResultsPerPage != 0 && oldResultsPerPage != resultsPerPage)
        {
            pageNum = 1;
        }
        return pageNum;
    }

    
    /**
     * Return the number of paginated pages.
     *
     */
    public int getNbrPages()
    {
        return nbrPages;
    }

    /**
     * Return the next page in the paginated list.
     *
     */
    public int getNextPage()
    {
        if (nextPage <= nbrPages)
        {
            return nextPage;
        }
        else
        {
            return 0;
        }       
    }

    /**
     * Return the previous page in the paginated list.
     *
     */
    public int getPrevPage()
    {
        return prevPage;
    }

    /**
     * This is used to get the format for a date in the 
     * Locale sent by the browser.
     */
    public DateFormat getDateFormat()
    {
        Locale locale = Localization.getLocale(data.getRequest());
        DateFormat df = DateFormat
            .getDateTimeInstance(DateFormat.MEDIUM, DateFormat.LONG, locale);
        if (timezone != null) 
        {
            df.setTimeZone(timezone);
        }
        return df;

        // We may want to eventually format the date other than default, 
        // this is how you would do it.
        //SimpleDateFormat sdf = new SimpleDateFormat(
        //    "yyyy/MM/dd hh:mm:ss a z", locale);
        //return (DateFormat) sdf;
    }

    /**
     * This is used to get the format for a date in the 
     * Locale sent by the browser.
     */
    public Calendar getCalendar()
    {
        Locale locale = Localization.getLocale(data.getRequest());
        Calendar cal = Calendar
            .getInstance(locale);
        if (timezone != null) 
        {
            cal.setTimeZone(timezone);
        }
        return cal;
    }

    /**
     * Determine if the user currently interacting with the scarab
     * application has a permission within the user's currently
     * selected module.
     *
     * @param permission a <code>String</code> permission value, which should
     * be a constant in this interface.
     * @return true if the permission exists for the user within the
     * current module, false otherwise
     */
    public boolean hasPermission(String permission)
    {
        boolean hasPermission = false;
        try
        {
            Module module = getCurrentModule();
            hasPermission = hasPermission(permission, module);
        }
        catch (Exception e)
        {
            hasPermission = false;
            Log.get().error("Permission check failed on:" + permission, e);
        }
        return hasPermission;
    }

    /**
     * Determine if the user currently interacting with the scarab
     * application has a permission within a module.
     *
     * @param permission a <code>String</code> permission value, which should
     * be a constant in this interface.
     * @param module a <code>Module</code> value
     * @return true if the permission exists for the user within the
     * given module, false otherwise
     */
    public boolean hasPermission(String permission, Module module)
    {
        boolean hasPermission = false;
        try
        {
            hasPermission = ((ScarabUser)data.getUser())
                .hasPermission(permission, module);
        }
        catch (Exception e)
        {
            hasPermission = false;
            Log.get().error("Permission check failed on:" + permission, e);
        }
        return hasPermission;
    }

    /* The map of associated users used on AssignIssue
     * When we first go to the screen, reset the map
     * To the currently assigned users for each issue
     */
    public void resetAssociatedUsers() throws Exception
    {
        HashMap assoUsers = new HashMap();
        List issueList = getIssues();
        if (issueList != null)
        {
            for (int i=0; i<issueList.size(); i++)
            {
                Issue issue = (Issue)issueList.get(i);
                assoUsers.put(issue.getIssueId(), issue.getAssociatedUsers());
            }
            ((ScarabUser)data.getUser()).setAssociatedUsersMap(assoUsers);
        }
    }
    public void resetSelectedUsers() throws Exception
    {
        ScarabUser user = (ScarabUser)data.getUser();
        user.setSelectedUsersMap(null);
    }

    public List getAssignIssuesList()
        throws Exception
    {        
        List issues = null;
        Map userMap = ((ScarabUser)data.getUser()).getAssociatedUsersMap();
        if (userMap != null && userMap.size() > 0)
        {
            issues = new ArrayList();
            Iterator iter = userMap.keySet().iterator();
            while (iter.hasNext()) 
            {
                issues.add(IssueManager.getInstance((Long)iter.next()));
            }
        }
        return issues;
    }

    /**
     * When a user searches for other users (in the ManageUserSearch.vm
     * template for example), the result of this search is stored into
     * the temporary data for that user. This previous result can be 
     * retrieved by this method.
     *
     * FIXME: shouldn't this be stored into the cache instead of the
     * temporary data of the user?
     *
     * @return The list of users of the last user-search.
     */
    public List getGlobalUserSearch() 
    {
        List users = (List) data.getUser().getTemp("userList");
        if (users == null) 
        {
            users = new ArrayList();
        }
        return users;
    }
    
    /**
     * Store the search result of other users for later use. The 
     * result is stored into the temporary data of the current user.
     *
     * FIXME: use the cache instead?
     *
     * @param users The list of users that is a result of a query.
     */
    public void setGlobalUserSearch(List users) 
    {
        data.getUser().setTemp("userList", users);
    }
    
    /**
     * Return the parameter used for the user-search (like in the
     * ManageUserSearch.vm template for example) returned by the
     * getGlobalUserSearch() method. These parameters are stored
     * into the temporary data of the current user.
     *
     * FIXME: use the cache instead?
     *
     * @param name The name of the parameter
     * @return The value of the parameter used in the search for users.
     */
    public String getGlobalUserSearchParam(String name) 
    {
        Hashtable params = (Hashtable) data.getUser().getTemp("userListParams");
        
        if (params == null) 
        {
            return "";
        }

        return (String) params.get(name);
    }
    
    /**
     * Set the parameters used to retrieved the users in the List given
     * to the setGlobalUserSearch(List) method. These parameters can be
     * retrieved by the getGlobalUserSearchParam(String) for later use.
     *
     * FIXME: use the cache instead?
     *
     * @param name The name of the parameter
     * @param value The value of the parameter
     */
    public void setGlobalUserSearchParam(String name, String value) 
    {
        Hashtable params = (Hashtable) data.getUser().getTemp("userListParams");
        if (params == null) 
        {
            params = new Hashtable();
        }

        if ((name != null) && (value != null))
        {
            params.put(name, value);
        }
        data.getUser().setTemp("userListParams", params);
    }

    public boolean hasItemsToApprove()
    {
        try
        {
            SecurityAdminTool sat = (SecurityAdminTool)org.apache.turbine.modules.Module.getTemplateContext(data)
                .get(ScarabConstants.SECURITY_ADMIN_TOOL);
            if (getCurrentModule().getUnapprovedQueries().isEmpty() &&
                getCurrentModule().getUnapprovedTemplates().isEmpty() &&
                sat.getPendingGroupUserRoles(getCurrentModule()).isEmpty())
            {
                return false;
            }
        }
        catch (Exception e)
        {
            Log.get().debug("Error: ", e);
        }
        return true;
    }

    public MITList getMITList(List issues)
        throws Exception
    {
        return MITListManager
            .getInstanceFromIssueList(issues, (ScarabUser)data.getUser());
    }

    /**
     * Gets a list of Attributes or the user type that are in common
     * between the issues in the given list.
     *
     * @param issues a <code>List</code> value
     * @return a <code>List</code> value
     * @exception Exception if an error occurs
     */
    public List getUserAttributes(List issues, boolean activeOnly)
        throws Exception
    {        
        List attributes = null;
        if (issues == null || issues.isEmpty()) 
        {
            attributes = Collections.EMPTY_LIST;
            Log.get().warn("ScarabRequestTool.getUserAttributes issue list was"
                     + (issues == null ? " null" : " empty")) ;
        }
        else 
        {
            attributes = getMITList(issues).getCommonUserAttributes(activeOnly);
        }
        
        return attributes;
    }


    // --------------------
    // template timing methods
    private long startTime;
    private long lapTime;

    /**
     * Should be called near the beginning of a template or wherever timing
     * should start.
     */
    public void startTimer()
    {
        startTime = System.currentTimeMillis();
        lapTime = startTime;
    }

    /**
     * Useful when performance tuning.  Usage is to call
     * <pre><code>
     * $scarabR.startTimer()
     * ...
     * $scarabR.reportTimer("foo")
     * ...
     * $scarabR.reportTimer("bar")
     *
     * or
     *
     * $scarabG.log($scarabR.reportTimer("bar"))
     * </code></pre>
     *
     * The labels are useful when output is directed to a log file, it can 
     * be "", if the output is written as part of the response.
     */
    public String reportTimer(String mesg)
    {
        long endTime = System.currentTimeMillis();
        String s = mesg + ".  Time for " + data.getTarget() + ": Lap/Split= "
            + (endTime-lapTime) + "ms; Cumulative= " + 
            (endTime-startTime) + "ms";
        lapTime = endTime;
        return s;
    }

    /**
     * Helper method to retrieve the ScarabLocalizationTool from the Context
     */
    private ScarabLocalizationTool getLocalizationTool()
    {
        return (ScarabLocalizationTool)org.apache.turbine.modules.Module
            .getTemplateContext(data).get(ScarabConstants.LOCALIZATION_TOOL);
    }

    /**
     * Helper method to retrieve the ScarabGlobalTool from the Context
     */
    private ScarabGlobalTool getGlobalTool()
    {
        return (ScarabGlobalTool)org.apache.turbine.modules.Module
            .getTemplateContext(data).get(ScarabConstants.SCARAB_GLOBAL_TOOL);
    }

    /**
     * Get any confirmation message usually set in the action.
     * @return value of confirmMessage.
     */
    public Object getConfirmMessage() 
    {
        return confirmMessage;
    }
    
    /**
     * Set confirmation message.
     * @deprecated
     * @param v  Value to assign to confirmMessage.
     */
    public void setConfirmMessage(Object  v) 
    {
        this.confirmMessage = v;
    }

    /**
     * Set confirm message using Localizable localizable.
     * @param v  Value to assign to alertMessage.
     */
    public void setConfirmMessage(Localizable localizable) 
    {
        ScarabLocalizationTool l10n = getLocalizationTool();
        this.confirmMessage = new SimpleSkipFiltering(localizable.getMessage(l10n));
    }

    /**
     * Get any informational message usually set in the action.
     * @return value of infoMessage.
     */
    public Object getInfoMessage() 
    {
        return infoMessage;
    }
    
    /**
     * Set informational message.
     * @deprecated
     * @param v  Value to assign to infoMessage.
     */
    public void setInfoMessage(Object  v) 
    {
        this.infoMessage = v;
    }

    /**
     * Set alert message using Localizable localizable.
     * @param v  Value to assign to alertMessage.
     */
    public void setInfoMessage(Localizable localizable) 
    {
        ScarabLocalizationTool l10n = getLocalizationTool();
        this.infoMessage = new SimpleSkipFiltering(localizable.getMessage(l10n));
    }

    /**
     * Get any alert message usually set in the action.
     * @return value of alertMessage.
     */
    public Object getAlertMessage() 
    {
        return alertMessage;
    }
    
    /**
     * Set alert message.
     * @deprecated
     * @param v  Value to assign to alertMessage.
     */
    public void setAlertMessage(Object  v) 
    {
        this.alertMessage = v;
    }
    
    /**
     * Set alert message using Localizable localizable.
     * @param v  Value to assign to alertMessage.
     */
    public void setAlertMessage(Localizable localizable) 
    {
        ScarabLocalizationTool l10n = getLocalizationTool();
        this.alertMessage = new SimpleSkipFiltering(localizable.getMessage(l10n));
    }

    public IssueListIterator getIssueListIterator(IteratorWithSize iterator, 
                                                  int listOffset, int size)
    {
        return new IssueListIterator(iterator, listOffset, size);
    }

    public class IssueListIterator implements Iterator
    {
        private static final String ISSUE_LIST = "issueList";
        private static final int PREV_SIZE = 1;
        private static final int NEXT_SIZE = 2;

        private WindowIterator i;
        private int size;
        private int listOffset;
        private int count = -1;

        private IssueListIterator(IteratorWithSize iterator, 
                                  int listOffset, int size)
        {
            this.listOffset = Math.max(listOffset, 0);
            this.size = size;
            this.i = new WindowIterator(iterator, PREV_SIZE, NEXT_SIZE);
        }

        public Object next()
        {
            count++;
            return i.next();
        }

        public boolean hasNext()
        {
            return i.hasNext();
        }

        public void remove()
        {
            i.remove();
        }

        public void initializeLink(ScarabLink link)
        {
            link.setPage("ViewIssue.vm")
                .addPathInfo("id", 
                             ((QueryResult)i.get(0)).getUniqueId());
            int offset = listOffset + count;
            for (int m = -1 * PREV_SIZE; m < 0; m++) 
            {
                if (i.hasValue(m)) 
                {
                    offset--;
                }
            }
            link.addPathInfo(ISSUE_LIST, offset);
            link.addPathInfo(ISSUE_LIST, size);
            for (int m = -1 * PREV_SIZE; m <= NEXT_SIZE; m++) 
            {
                if (i.hasValue(m)) 
                {
                    link.addPathInfo(ISSUE_LIST,
                                     ((QueryResult)i.get(m)).getUniqueId());
                }
            }
        }
    } 



   /**
    * @return an IssueType which may represent a template 
    */
    public Object getLastEnteredIssueTypeOrTemplate()
        throws Exception
    {
        Object result = null;
        IssueType issueType = getCurrentIssueType();
        ScarabUser user = (ScarabUser)data.getUser();
        String templateId = data.getParameters().getString("templateId");
        if (templateId != null && templateId.trim().length() > 0)
        {
            Issue template = getIssueTemplate(templateId);
            issueType = template.getIssueType().getIssueTypeForTemplateType();
            user.setLastEnteredTemplate(template);
            result = template;
        }

        if (result == null && issueType != null) 
        {
            result = issueType;
            user.setLastEnteredIssueType(issueType);
        }
        
        if (result == null) 
        {
            result = user.lastEnteredIssueTypeOrTemplate();
            if (result != null) 
            {
                if (result instanceof Issue) 
                {
                    issueType = ((Issue)result).getIssueType()
                        .getIssueTypeForTemplateType();
                }
                else if (result instanceof IssueType)
                {
                    issueType = (IssueType)result;                    
                }
                else 
                {
                    Log.get().warn("An object of unexpected class was saved as"
                        + " the last entered issuetype or template: " + 
                        result.getClass().getName());
                    result = null;
                }
                
            }
        }
        
        // finally if we have a value, check that it is active
        if (issueType != null) 
        {
            RModuleIssueType rmit = 
                getCurrentModule().getRModuleIssueType(issueType);
            if (rmit == null || !rmit.getActive()) 
            {
                result = null;
            }
        }
        
        return result;
    }
    
    public List getSortedAttributeOptions()
    {
        return AttributeOptionPeer.getSortedAttributeOptions();
    }

    /**
     * Returns if the system is configurated to allow anonymous login.
     *
     */
    public boolean isAnonymousLoginAllowed()
    {
        return Turbine.getConfiguration().getProperty("scarab.anonymous.username")!=null;
    }

    public Transition getTransition(Integer pk)
    {
        Transition tran = null;
        try
        {
            tran = TransitionPeer.retrieveByPK(pk);
        }
        catch (Exception e)
        {
            // Nothing to do. Ignore.
        }
        return tran;
    }
    
    // ****************** Recyclable implementation ************************
    /**
     * Recycles the object by removing its disposed flag.
     */
    public void recycle()
    {
        disposed = false;
    }
    /**
     * Disposes the object after use. The method is called when the
     * object is returned to its pool.  The dispose method must call
     * its super.
     */
    public void dispose()
    {
        disposed = true;
        data = null;
        refresh();
    }
    /**
     * Checks whether the object is disposed.
     *
     * @return true, if the object is disposed.
     */
    public boolean isDisposed()
    {
        return disposed;
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

QDox parse error in file://<WORKSPACE>/data/code-rep-dataset/Dataset2/Tasks/4096.java