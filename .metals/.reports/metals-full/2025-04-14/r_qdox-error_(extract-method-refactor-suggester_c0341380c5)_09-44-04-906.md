error id: file://<WORKSPACE>/data/code-rep-dataset/Dataset2/Tasks/626.java
file://<WORKSPACE>/data/code-rep-dataset/Dataset2/Tasks/626.java
### com.thoughtworks.qdox.parser.ParseException: syntax error @[1,1]

error in qdox parser
file content:
```java
offset: 1
uri: file://<WORKSPACE>/data/code-rep-dataset/Dataset2/Tasks/626.java
text:
```scala
i@@ssue = IssueManager.getInstance(new NumberKey(key));

package org.tigris.scarab.tools;

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

import java.text.DateFormat;
import java.text.ParseException;
import java.util.List;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.Iterator;
import java.util.Locale;
import java.util.Hashtable;

// Turbine
import org.apache.commons.lang.Strings;
import org.apache.turbine.Log;
import org.apache.torque.om.NumberKey;
import org.apache.torque.om.ObjectKey;
import org.apache.torque.om.ComboKey;
import org.apache.torque.util.Criteria;
import org.apache.turbine.RunData;
import org.apache.turbine.tool.IntakeTool;
import org.apache.fulcrum.localization.Localization;
import org.apache.fulcrum.intake.Intake;
import org.apache.fulcrum.intake.model.Group;
import org.apache.fulcrum.intake.model.Field;
import org.apache.fulcrum.pool.RecyclableSupport;
import org.apache.fulcrum.util.parser.StringValueParser;
import org.apache.fulcrum.util.parser.ValueParser;
import org.apache.commons.collections.SequencedHashMap;

// Scarab
import org.tigris.scarab.om.ScarabUser;
import org.tigris.scarab.om.ScarabUserManager;
import org.tigris.scarab.om.Issue;
import org.tigris.scarab.om.IssueManager;
import org.tigris.scarab.om.IssuePeer;
import org.tigris.scarab.om.IssueType;
import org.tigris.scarab.om.IssueTypeManager;
import org.tigris.scarab.om.IssueTypePeer;
import org.tigris.scarab.om.Query;
import org.tigris.scarab.om.QueryManager;
import org.tigris.scarab.om.QueryPeer;
import org.tigris.scarab.om.IssueTemplateInfo;
import org.tigris.scarab.om.IssueTemplateInfoManager;
import org.tigris.scarab.om.IssueTemplateInfoPeer;
import org.tigris.scarab.om.Depend;
import org.tigris.scarab.om.DependManager;
import org.tigris.scarab.om.ScopePeer;
import org.tigris.scarab.om.FrequencyPeer;
import org.tigris.scarab.om.Attribute;
import org.tigris.scarab.om.AttributeManager;
import org.tigris.scarab.om.AttributeValuePeer;
import org.tigris.scarab.om.AttributeGroup;
import org.tigris.scarab.om.AttributeGroupManager;
import org.tigris.scarab.om.Attachment;
import org.tigris.scarab.om.AttachmentManager;
import org.tigris.scarab.om.AttributeOption;
import org.tigris.scarab.om.ROptionOption;
import org.tigris.scarab.om.AttributeOptionManager;
import org.tigris.scarab.om.RModuleAttribute;
import org.tigris.scarab.om.RModuleAttributeManager;
import org.tigris.scarab.om.AttributeValue;
import org.tigris.scarab.om.ParentChildAttributeOption;
import org.tigris.scarab.om.Module;
import org.tigris.scarab.om.ModuleManager;
import org.tigris.scarab.util.ScarabConstants;
import org.tigris.scarab.util.word.IssueSearch;
import org.tigris.scarab.util.word.SearchIndex;
import org.tigris.scarab.om.Report;
import org.tigris.scarab.om.ReportManager;
import org.tigris.scarab.om.TransactionPeer;
import org.tigris.scarab.om.TransactionTypePeer;
import org.tigris.scarab.om.ActivityPeer;
import org.tigris.scarab.util.ScarabException;  

/**
 * This class is used by the Scarab API
 */
public class ScarabRequestTool
    extends RecyclableSupport
    implements ScarabRequestScope
{
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
    private String alert = null;

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
     * A Module object which represents the current module
     * selected by the user within a request.
     */
    private Module currentModule = null;

    /**
     * A IssueType object which represents the current issue type
     * selected by the user within a request.
     */
    private IssueType currentIssueType = null;

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
     * A list of Issues
     */
    private List issueList;
    
    /**
     * A ReportGenerator
     */
    private Report reportGenerator = null;
    
    private int nbrPages = 0;
    private int prevPage = 0;
    private int nextPage = 0;
    
    /**
     * Constructor does initialization stuff
     */    
    public ScarabRequestTool()
    {
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
        currentModule = null;
        currentIssueType = null;
        reportingIssue = null;
        module = null;
        attributeOption = null;
        roo = null;
        pcao = null;
        issueList = null;
        reportGenerator = null;
        nbrPages = 0;
        prevPage = 0;
        nextPage = 0;
    }

    /**
     * Sets the <code>Alert!</code> message for this request.
     *
     * @param message The alert message to set.
     */
    public void setAlert(String message)
    {
        this.alert = message;
    }

    /**
     * Retrieves any <code>Alert!</code> message which has been set.
     *
     * @return The alert message.
     */
    public String getAlert()
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
     * Get the intake tool. FIXME: why is it getting it
     * from the Module and not from the IntakeService?
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
        this.issueType = issueType;
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
        this.pcao = pcao;
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
try{
        if (attributeOption == null)
        {
            String optId = getIntakeTool()
                .get("AttributeOption", IntakeTool.DEFAULT_KEY)
                .get("OptionId").toString();
            if ( optId == null || optId.length() == 0 )
            {
                attributeOption = AttributeOption.getInstance();
            }
            else 
            {
                attributeOption = AttributeOptionManager
                    .getInstance(new NumberKey(optId));
            }
        }
}catch(Exception e){e.printStackTrace();}
        return attributeOption;
    }

    /**
     * @see org.tigris.scarab.tools.ScarabRequestScope#setUser(ScarabUser)
     */
    public void setUser (ScarabUser user)
    {
        this.user = user;
    }

    /**
     * @see org.tigris.scarab.tools.ScarabRequestScope#getUser()
     */
    public ScarabUser getUser()
    {
        return this.user;
    }

    /**
     * Return a specific User by ID from within the system.
     * You can pass in either a NumberKey or something that
     * will resolve to a String object as id.toString() is 
     * called on everything that isn't a NumberKey.
     */
    public ScarabUser getUser(Object id)
     throws Exception
    {
        ScarabUser su = null;
        try
        {
            ObjectKey pk = null;
            if (id instanceof NumberKey)
            {
                pk = (ObjectKey) id;
            }
            else
            {
                pk = (ObjectKey)new NumberKey(id.toString());
            }
            su = ScarabUserManager.getInstance(pk);
        }
        catch (Exception e)
        {
            e.printStackTrace();
        }
        return su;
    }

    /**
     * Return a specific User by username.
     */
    public ScarabUser getUserByUserName(String username)
     throws Exception
    {
        return ScarabUserManager.getInstance(username, getIssue().getIdDomain());
    }

    /**
     * A Attribute object for use within the Scarab API.
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
         if ( attId == null || attId.length() == 0 )
         {
            attId = data.getParameters().getString("attId");
            if ( attId == null || attId.length() == 0 )
            { 
                attribute = AttributeManager.getInstance();
            }
            else 
            {
                attribute = AttributeManager.getInstance(new NumberKey(attId));
            }
        }
        else 
        {
            attribute = AttributeManager.getInstance(new NumberKey(attId));
        }
     } 
}catch(Exception e){e.printStackTrace();}
        return attribute;
   }

    /**
     * A Attribute object for use within the Scarab API.
     */
    public Attribute getAttribute(NumberKey pk)
     throws Exception
    {
        try
        {
           attribute = AttributeManager.getInstance(pk);
        }
        catch(Exception e){e.printStackTrace();}
        return attribute;
   }

    /**
     * A Attribute object for use within the Scarab API.
     */
    public AttributeOption getAttributeOption(NumberKey pk)
     throws Exception
    {
        try
        {
           attributeOption = AttributeOptionManager.getInstance(pk);
        }
        catch(Exception e){e.printStackTrace();}
        return attributeOption;
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
                if ( queryId == null || queryId.length() == 0 )
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

                if ( templateId == null || templateId.length() == 0 )
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
            if ( templateId == null || templateId.length() == 0 )
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
            if ( templateId == null || templateId.length() == 0 )
            {
                data.setMessage("No template id specified.");
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
                if ( dependId == null || dependId.length() == 0 )
                {
                    depend = Depend.getInstance();
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
     * A Attachment object for use within the Scarab API.
     */
    public Attachment getAttachment()
     throws Exception
    {
try{
        if (attachment == null)
        {
            Group att = getIntakeTool()
                .get("Attachment", IntakeTool.DEFAULT_KEY, false);
            if ( att != null ) 
            {            
                String attId =  att.get("Id").toString();
                if ( attId == null || attId.length() == 0 )
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
}catch(Exception e){e.printStackTrace(); throw e;}
        return attachment;
    }

    /**
     * A AttributeGroup object for use within the Scarab API.
     */
    public AttributeGroup getAttributeGroup()
     throws Exception
    {
           AttributeGroup group = null;
try{
            String attGroupId = getIntakeTool()
                .get("AttributeGroup", IntakeTool.DEFAULT_KEY)
                .get("AttributeGroupId").toString();
            if ( attGroupId == null || attGroupId.length() == 0 )
            {
                group = new AttributeGroup();
            }
            else 
            {
                group = AttributeGroupManager
                    .getInstance(new NumberKey(attGroupId), false);
            }
}catch(Exception e){e.printStackTrace();}
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
        }
        return issueType;
    }

    /**
     * Get an issue type object.
     */
    public IssueType getIssueType()
        throws Exception
    {
        if ( issueType == null ) 
        {
            String key = data.getParameters()
                .getString("issuetypeid");
            if ( key == null ) 
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
                    e.printStackTrace();
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
      try{
            ComboKey rModAttId = (ComboKey)getIntakeTool()
                .get("RModuleAttribute", IntakeTool.DEFAULT_KEY)
                .get("Id").getValue();
            if ( rModAttId == null )
            {
                NumberKey attId = (NumberKey)getIntakeTool()
                    .get("Attribute", IntakeTool.DEFAULT_KEY)
                    .get("Id").getValue();
                Module currentModule = getCurrentModule();
                if ( attId != null && currentModule != null )
                {
                    NumberKey[] nka = {attId, currentModule.getModuleId()};
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
      }catch(Exception e){e.printStackTrace();}
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
      try{
        String modId = getIntakeTool()
            .get("Module", IntakeTool.DEFAULT_KEY).get("Id").toString();
        if ( modId == null || modId.length() == 0 )
        {
            module = ModuleManager.getInstance();
        }
        else 
        {
            module = ModuleManager.getInstance(new NumberKey(modId));
        }
      }catch(Exception e){e.printStackTrace();}
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
        if ( key != null && key.length() > 0 ) 
        {
            try
            {
                me = ModuleManager.getInstance(new NumberKey(key));
            }
            catch (Exception e)
            {
                Log.info("[ScarabRequestTool] Unable to retrieve Module: " +
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
        if (currentModule == null)
        {
            currentModule = getModule(
                data.getParameters()
                .getString(ScarabConstants.CURRENT_MODULE));
        }
        return currentModule;
    }

    /**
     * Gets the IssueType associated with the information
     * passed around in the query string. Returns null if
     * the Module could not be found.
     */
    public IssueType getCurrentIssueType() throws Exception
    {
        if (currentIssueType == null)
        {
            currentIssueType = getIssueType(
                data.getParameters()
                .getString(ScarabConstants.CURRENT_ISSUE_TYPE, "1"));
        }
        return currentIssueType;
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
        if ( reportingIssue == null ) 
        {
            String key = data.getParameters()
                .getString(ScarabConstants.REPORTING_ISSUE);
            if ( key == null ) 
            {
                getNewReportingIssue();
            }
            else 
            {
                reportingIssue = ((ScarabUser)data.getUser())
                    .getReportingIssue(key);

                // if reportingIssue is still null, the parameter must have
                // been stale, just get a new issue
                if ( reportingIssue == null ) 
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
        currentModule = me;
    }

    /**
     * Sets the current ArtifactType
     */
    public void setCurrentIssueType(IssueType issueType)
    {
        currentIssueType = issueType;
    }

    /**
     * A Issue object for use within the Scarab API.
     */
    public void setIssue(Issue issue)
    {
        this.issue = issue;
    }

    /**
     * Get an Issue object from unique id.
     * If first time calling, returns a new blank issue object.
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
            if ( issueGroup != null ) 
            {            
                issueId =  issueGroup.get("Id").toString();
            }
            else
            {
                issueId = data.getParameters().getString("id");
            }
            if ( issueId == null || issueId.length() == 0 )
            {
                issue = getCurrentModule()
                    .getNewIssue(getCurrentIssueType());
            }
            else 
            {
                issue = getIssue(issueId);
            }
        }
        return issue;
    }

    /**
     * The id may only be the issue's unique id.
     *
     * @param key a <code>String</code> value
     * @return a <code>Issue</code> value
     */
    public Issue getIssueByPk(String key)
    {
        Issue issue = null;
        try
        {
            issue = IssueManager.getInstance(new NumberKey(key), false);
        }
        catch (Exception e)
        {
            data.setMessage("Issue id was invalid.");
        }
        return issue;
    }

    /**
     * Takes unique id, and returns issue.
     */
    public Issue getIssue(String id) throws Exception
    {
        Issue issue = null;
        if (id == null)
        {
            data.setMessage("Please enter an id.");
        }
        else
        {
	    try
            {
	        issue = Issue.getIssueById(id);
	        if (issue == null)
	        {
	           String code = getCurrentModule().getCode();
                   id = code + id;
	           issue = Issue.getIssueById(id);
	        }
	    }        
	    catch (Exception e)
	    {
	        data.setMessage("That id is not valid.");
	    }
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
        if ( issueGroup != null ) 
        {            
            NumberKey[] issueIds =  (NumberKey[])
                issueGroup.get("Ids").getValue();
            if ( issueIds != null ) 
            {            
                issues = new ArrayList(issueIds.length);
                for ( int i=0; i<issueIds.length; i++ ) 
                {
                    issues.add(IssueManager.getInstance(issueIds[i], false));
                }
            }
        }
        else if ( data.getParameters().getString("issue_ids") != null ) 
        {                
            String[] issueIdStrings = data.getParameters()
                .getStrings("issue_ids");
            issues = new ArrayList(issueIdStrings.length);
            for ( int i=0; i<issueIdStrings.length; i++ ) 
            {
                issues.add(IssueManager
                    .getInstance(new NumberKey(issueIdStrings[i]), false));
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

    public Intake getConditionalIntake(String parameter)
        throws Exception
    {
        Intake intake = null;
        String param = data.getParameters().getString(parameter);
        if ( param == null ) 
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
     * Get a new SearchIssue object. 
     *
     * @return a <code>Issue</code> value
     */
    public IssueSearch getSearch()
        throws Exception
    {
        return new IssueSearch(getCurrentModule(), getCurrentIssueType());
    }

    /**
     * The most recent query entered.
    */
    public String getCurrentQuery()
        throws Exception
    {
        String currentQuery = null;
        currentQuery = (String)((ScarabUser)data.getUser())
            .getTemp(ScarabConstants.CURRENT_QUERY);
        return currentQuery;
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
     * Returns all issue templates that are global, 
     * Plus those that are personal and created by logged-in user.
    */
    public List getIssueTemplates()
        throws Exception
    {
        String sortColumn = data.getParameters().getString("sortColumn");
        String sortPolarity = data.getParameters().getString("sortPolarity");
        if (sortColumn == null)
        {
            sortColumn = "name";
        }
        if (sortPolarity == null)
        {
            sortPolarity = "asc";
        }
        return IssueTemplateInfoPeer.getAllTemplates(getCurrentModule(),
               getCurrentIssueType(), (ScarabUser)data.getUser(), 
               sortColumn, sortPolarity);
    }

    /**
     * Returns all queries that are global, 
     * Plus those that are personal and created by logged-in user.
    */
    public List getPrivateQueries()
        throws Exception
    {
        return QueryPeer.getQueries(getCurrentModule(),
               getCurrentIssueType(), (ScarabUser)data.getUser(), 
               "avail", "desc", "private");
    }

    /**
     * Returns all queries that are global, 
     * Plus those that are personal and created by logged-in user.
    */
    public List getGlobalQueries()
        throws Exception
    {
        return QueryPeer.getQueries(getCurrentModule(),
               getCurrentIssueType(), (ScarabUser)data.getUser(), 
               "avail", "desc", "global");
    }

    /**
     * Returns all queries that are global, 
     * Plus those that are personal and created by logged-in user.
    */
    public List getQueries()
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
               getCurrentIssueType(), (ScarabUser)data.getUser(), 
               sortColumn, sortPolarity, "all");
    }

    /**
     * Performs search on current query (which is stored in user session).
    */
    public List getCurrentSearchResults()
        throws Exception, ScarabException
    {
        ScarabUser user = (ScarabUser)data.getUser();
        String currentQueryString = getCurrentQuery();
        IssueSearch search = getSearch();
        List matchingIssueIds = new ArrayList();
        boolean searchSuccess = true;
        Intake intake = null;

        if (currentQueryString == null)
        {
            data.setMessage("Please enter a query.");
            searchSuccess = false;
            intake = new Intake();
        }
        else
        {
           intake = parseQuery(currentQueryString);
        }

        // If they have entered users to search on, and that returns no results
        // Don't bother running search
        String[] userList = data.getParameters().getStrings("user_list");
        if ( userList != null && userList.length > 0)
        {
            List issueIdsFromUserSearch = getIssueIdsFromUserSearch();
            if (issueIdsFromUserSearch.size() > 0)
            {
                search.setIssueIdsFromUserSearch(issueIdsFromUserSearch);
            }
            else
            {
               searchSuccess = false;
            }
        }

        if (searchSuccess)
        {
            // Set intake properties
            Group searchGroup = intake.get("SearchIssue", 
                                           getSearch().getQueryKey() );

            Field minDate = searchGroup.get("MinDate");
            if (minDate != null && minDate.toString().length() > 0)
            { 
                checkDate(search, minDate.toString());
            }
            Field maxDate = searchGroup.get("MaxDate");
            if (maxDate != null && minDate.toString().length() > 0)
            { 
                checkDate(search, minDate.toString());
            }
            Field stateChangeFromDate = searchGroup.get("StateChangeFromDate");
            if (stateChangeFromDate != null 
                && stateChangeFromDate.toString().length() > 0)
            { 
                checkDate(search, stateChangeFromDate.toString());
            }
            Field stateChangeToDate = searchGroup.get("StateChangeToDate");
            if (stateChangeToDate != null 
                && stateChangeToDate.toString().length() > 0)
            { 
                checkDate(search, stateChangeToDate.toString());
            }
            searchGroup.setProperties(search);

            // Set attribute values to search on
            SequencedHashMap avMap = search.getModuleAttributeValuesMap();
            Iterator i = avMap.iterator();
            while (i.hasNext()) 
            {
                AttributeValue aval = (AttributeValue)avMap.get(i.next());
                Group group = intake.get("AttributeValue", aval.getQueryKey());
                if ( group != null ) 
                {
                    group.setProperties(aval);
                }                
            }
            
            // If user is sorting on an attribute, set sort criteria
            // Do not use intake, since intake parsed from query is not the same
            // As intake passed from the form
            String sortColumn = data.getParameters().getString("sortColumn");
            if (sortColumn != null && sortColumn.length() > 0 
                && Strings.isNumeric(sortColumn))
            {
                search.setSortAttributeId(new NumberKey(sortColumn));
            }
            String sortPolarity = data.getParameters().getString("sortPolarity");
            if (sortPolarity != null && sortPolarity.length() > 0)
            {
                search.setSortPolarity(sortPolarity);
            }
               
            // Do search
            try
            {
                matchingIssueIds = search.getMatchingIssues();
                if (matchingIssueIds == null || matchingIssueIds.size() <= 0)
                {
                    data.setMessage("No matching issues.");
                }            
            }
            catch (ScarabException e)
            {
                String queryError = e.getMessage();
                if (queryError.startsWith(SearchIndex.PARSE_ERROR)) 
                {
                    Log.info(queryError);
                    data.setMessage(queryError);
                }
                else 
                {
                    throw e;
                }
            }
        }
        return matchingIssueIds;
    }

    /**
     * Attempts to parse a date passed in the query page.
    */
    private void checkDate(IssueSearch search, String date)
        throws Exception
    {
        try
        {
            search.parseDate(date, false);
        }
        catch (Exception e)
        {
            data.setMessage("Invalid date.");
            throw new Exception("Invalid date.");
        }
    }

    /**
     * Searches on user attributes.
    */
    private List getIssueIdsFromUserSearch()
        throws Exception
    {
        Criteria userCrit = new Criteria();
        boolean atLeastOneMatch = false;
        Object[] keys =  data.getParameters().getKeys();
        for (int i =0; i<keys.length; i++)
        {
            String key = keys[i].toString();
            if (key.startsWith("user_attr_"))
            {
               Criteria tempCrit = new Criteria()
                  .add(IssuePeer.MODULE_ID, getCurrentModule().getModuleId())
                  .add(IssuePeer.TYPE_ID, getCurrentIssueType().getIssueTypeId());

               String userId = key.substring(10);
               String attrId = data.getParameters().getString(key);

               if (attrId == null)
               {
                  attrId = "any";
               }
               List tempIssueList = null;
               List tempIssueIds = new ArrayList();
               
               // Build Criteria for created by
               Criteria createdByCrit = new Criteria()
                   .addJoin(TransactionPeer.TRANSACTION_ID, 
                                ActivityPeer.TRANSACTION_ID)
                   .addJoin(ActivityPeer.ISSUE_ID, IssuePeer.ISSUE_ID)
                   .add(TransactionPeer.TYPE_ID, 
                        TransactionTypePeer.CREATE_ISSUE__PK)
                   .add(TransactionPeer.CREATED_BY, userId);

               // If attribute is "committed by", search for creating user
               if (attrId.equals("created_by"))
               {
                   tempIssueList = IssuePeer.doSelect(createdByCrit);
               }
               // If attribute is "any", search across user attributes
               else if (attrId.equals("any"))
               {
                   Criteria.Criterion cCreated = null;
                   Criteria.Criterion cAttr = null;

                   // First get results of searching across user attributes
                   Criteria attrCrit = new Criteria()
                     .add(AttributeValuePeer.USER_ID, userId)
                     .addJoin(AttributeValuePeer.ISSUE_ID, IssuePeer.ISSUE_ID);
                   List attrList = IssuePeer.doSelect(attrCrit);
                   List attrIdList = new ArrayList();
                   for (int j=0; j < attrList.size(); j++)
                   {
                       attrIdList.add(((Issue)attrList.get(j)).getIssueId());
                   }

                   // Then get results of searching createdBy
                   List createdList = IssuePeer.doSelect(createdByCrit);
                   List createdIdList = new ArrayList();
                   for (int j=0; j < createdList.size(); j++)
                   {
                       createdIdList.add(((Issue)createdList.get(j)).getIssueId());
                   }

                   // Combine the two searches with an OR
                   if (createdIdList.size() > 0)
                   {
                       cCreated = attrCrit.getNewCriterion(IssuePeer.ISSUE_ID, 
                                           createdIdList, Criteria.IN); 
                   }
                   if (attrIdList.size() > 0)
                   {
                       cAttr = attrCrit.getNewCriterion(IssuePeer.ISSUE_ID, 
                                                        attrIdList, Criteria.IN);
                   }
                   if (cCreated != null && cAttr != null)
                   {
                       cAttr.or(cCreated);
                       tempCrit.and(cAttr);
                   }
                   else if (cCreated != null)
                   {
                       tempCrit.add(cCreated);
                   }
                   else if (cAttr != null)
                   {
                       tempCrit.add(cAttr);
                   }
                   tempIssueList = IssuePeer.doSelect(tempCrit);
               }
               else
               {
                   // A user attribute was selected to search on 
                   tempCrit.add(AttributeValuePeer.ATTRIBUTE_ID, attrId)
                     .add(AttributeValuePeer.USER_ID, userId)
                     .addJoin(AttributeValuePeer.ISSUE_ID, IssuePeer.ISSUE_ID);
                   tempIssueList = IssuePeer.doSelect(tempCrit);
               }
               if (tempIssueList.size() > 0)
               {
                   atLeastOneMatch = true;
                   // Get issue id list from issue result set
                   for (int l = 0; l < tempIssueList.size();l++)
                   {
                       tempIssueIds.add(((Issue)tempIssueList.get(l)).getIssueId());
                   }

                   // Create a criteria that is the results of an OR 
                   // Between the different users that were searched on.
                   Criteria.Criterion c1 = userCrit.
                                            getNewCriterion(IssuePeer.ISSUE_ID, 
                                            tempIssueIds, Criteria.IN);
                   userCrit.or(c1);
               }
            }
        }

        List finalIssueIds = new ArrayList();
        if (atLeastOneMatch)
        {
            // Turn final issue list resulting from OR into an id list
            // And set search property
            List finalIssueList = IssuePeer.doSelect(userCrit);
            for (int l = 0; l<finalIssueList.size();l++)
            {
                finalIssueIds.add(((Issue)finalIssueList.get(l)).getIssueId());
            }
        }
        return finalIssueIds;
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
     * a report helper class
     */
    public Report getReport()
        throws Exception
    {
        if ( reportGenerator == null ) 
        {
            String key = data.getParameters()
                .getString(ScarabConstants.CURRENT_REPORT);
            ValueParser parameters = data.getParameters();
            String id = parameters.getString("report_id");
            if ( id == null || id.length() == 0 ) 
            {
                if ( key == null ) 
                {
                    reportGenerator = getNewReport();
                }
                else 
                {
                    reportGenerator = ((ScarabUser)data.getUser())
                        .getCurrentReport(key);
                    
                    // if reportingIssue is still null, the parameter must have
                    // been stale, just get a new issue
                    if ( reportGenerator == null ) 
                    {
                        reportGenerator = getNewReport();                    
                    }
                }                
            }
            else 
            {
                reportGenerator = 
                    ReportManager.getInstance(new NumberKey(id), false);
                //reportGenerator
                //    .setQueryString(getReportQueryString(parameters));
                System.out.println("Old key " + key); 
                key = ((ScarabUser)data.getUser())
                    .setCurrentReport(reportGenerator);
                data.getParameters()
                    .remove(ScarabConstants.CURRENT_REPORT);
                data.getParameters()
                    .add(ScarabConstants.CURRENT_REPORT, key);
                System.out.println("Set new report " + key + " = " + 
                                   reportGenerator);
            }
        }
        
        return reportGenerator;
    }

    private Report getNewReport()
        throws Exception
    {
        Report report  = new Report();
        report.setModule(getCurrentModule());
        report.setGeneratedBy((ScarabUser)data.getUser());
        report.setIssueType(getCurrentIssueType());

        String key = ((ScarabUser)data.getUser()).setCurrentReport(report);
        data.getParameters().add(ScarabConstants.CURRENT_REPORT, key);

        return report;
    }

    public void setReport(Report report)
    {
        this.reportGenerator = report;
    }
    
    /*
    private static String getReportQueryString(ValueParser params) 
    {
        StringBuffer query = new StringBuffer();
        Object[] keys =  params.getKeys();
        for (int i =0; i<keys.length; i++)
        {
            String key = keys[i].toString();
            if ( key.startsWith("rep") || key.startsWith("intake")
 key.startsWith("ofg") )
            {
                String[] values = params.getStrings(key);
                for (int j=0; j<values.length; j++)
                {
                    query.append('&').append(key);
                    query.append('=').append(values[j]);
                }
            }
         }
         return query.toString();
    }
    */

    /**
     * Return all users for current module and issuetype.
     */
    public List getUsers( ) throws Exception
    {
        List users = new ArrayList();
        Module module = getCurrentModule();  
        ScarabUser[] userArray = module
            .getUsers(module.getUserPermissions(getCurrentIssueType()));
        for (int i=0;i<userArray.length;i++)
        {
            users.add(userArray[i]);
        }
        return sortUsers(users);
    }
        
    /**
     * Return results of user search.
     */
    public List getUserSearchResults()  throws Exception
    {
        String searchString = data.getParameters()
               .getString("searchString"); 
        String searchField = data.getParameters()
               .getString("searchField"); 
        Module module = getCurrentModule();  
        if (searchField == null)
        {
            data.setMessage("Please enter a search field.");
            return null ;
        }
        
        String firstName = null;
        String lastName = null;
        String email = null;
        if (searchField.equals("First Name"))
        {
            firstName = searchString;
        }
        else if (searchField.equals("Last Name"))
        {
            lastName = searchString;
        }
        else
        {
            email = searchString;
        }

        return sortUsers(module.getUsers(firstName, lastName, null, email, 
                               getCurrentIssueType()));
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
                if (sortColumn != null && sortColumn.equals("email"))
                {
                    i =  polarity * ((ScarabUser)o1).getEmail()
                         .compareTo(((ScarabUser)o2).getEmail());
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
     * Return a subset of the passed-in list.
     */
    public List getPaginatedList( List fullList, String pgNbrStr, 
                                  String nbrItmsPerPageStr)
    {

        List pageResults = null;
        int pgNbr =0 ;
        int nbrItmsPerPage =0 ;
        try
        {
           pgNbr = Integer.parseInt(pgNbrStr);
           nbrItmsPerPage = Integer.parseInt(nbrItmsPerPageStr);
           this.nbrPages =  (int)Math.ceil((float)fullList.size() 
                                               / nbrItmsPerPage);
           this.nextPage = pgNbr + 1;
           this.prevPage = pgNbr - 1;
           pageResults = fullList.subList ((pgNbr - 1) * nbrItmsPerPage, 
               Math.min(pgNbr * nbrItmsPerPage, fullList.size()));
        }
        catch(Exception e)
        {
            e.printStackTrace();
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
        return DateFormat.getDateTimeInstance(DateFormat.MEDIUM, 
            DateFormat.MEDIUM, locale);
// We may want to eventually format the date other than default, 
// this is how you would do it.
//        SimpleDateFormat sdf = new SimpleDateFormat(
//            "yyyy/MM/dd hh:mm:ss a z", locale);
//        return (DateFormat) sdf;
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
            Log.error("Permission check failed on:" + permission, e);
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
            Log.error("Permission check failed on:" + permission, e);
        }
        return hasPermission;
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
     * $scarabG.log( $scarabR.reportTimer("bar") )
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

    // ****************** Recyclable implementation ************************

    /**
     * Disposes the object after use. The method is called when the
     * object is returned to its pool.  The dispose method must call
     * its super.
     */
    public void dispose()
    {
        super.dispose();
        data = null;
        refresh();
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

QDox parse error in file://<WORKSPACE>/data/code-rep-dataset/Dataset2/Tasks/626.java