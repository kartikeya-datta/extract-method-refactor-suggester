error id: file://<WORKSPACE>/data/code-rep-dataset/Dataset2/Tasks/5789.java
file://<WORKSPACE>/data/code-rep-dataset/Dataset2/Tasks/5789.java
### com.thoughtworks.qdox.parser.ParseException: syntax error @[1,10]

error in qdox parser
file content:
```java
offset: 10
uri: file://<WORKSPACE>/data/code-rep-dataset/Dataset2/Tasks/5789.java
text:
```scala
@version $@@Id: GenerateReport.java,v 1.30 2003/03/07 16:39:52 jmcnally dead $

package org.tigris.scarab.actions;

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
import java.util.ArrayList;

// Turbine Stuff 
import org.apache.turbine.TemplateContext;
import org.apache.turbine.RunData;

import org.apache.torque.om.NumberKey;
import org.apache.fulcrum.intake.Intake;
import org.apache.fulcrum.intake.model.Group;

// Scarab Stuff
import org.tigris.scarab.om.ScarabUser;
import org.tigris.scarab.tools.ScarabRequestTool;
import org.tigris.scarab.tools.ScarabLocalizationTool;
import org.tigris.scarab.reports.ReportBridge;
import org.tigris.scarab.om.ReportPeer;
import org.tigris.scarab.om.ReportManager;
import org.tigris.scarab.om.Report;
import org.tigris.scarab.actions.base.RequireLoginFirstAction;
import org.tigris.scarab.util.ScarabConstants;

/**
    This class is responsible for report generation forms
    @author <a href="mailto:jmcnally@collab.net">John D. McNally</a>
    @version $Id: GenerateReport.java,v 1.29 2003/03/04 17:27:18 jmcnally Exp $
*/
public class GenerateReport 
    extends RequireLoginFirstAction
{
    private static final String NO_PERMISSION_MESSAGE = 
        "NoPermissionToEditReport";

    public void doCreatenew(RunData data, TemplateContext context)
        throws Exception
    {
        String key = data.getParameters()
            .getString(ScarabConstants.CURRENT_REPORT);
        data.getParameters().remove(ScarabConstants.CURRENT_REPORT);
        if (key != null && key.length() > 0) 
        {
            ((ScarabUser)data.getUser()).setCurrentReport(key, null);
        }
        setTarget(data, "reports,Info.vm");            
    }
    

    public void doSavereport(RunData data, TemplateContext context)
        throws Exception
    {
        ScarabLocalizationTool l10n = getLocalizationTool(context);
        ReportBridge report = populateReport(data, context);
        Intake intake = getIntakeTool(context);
        if (!report.isEditable((ScarabUser)data.getUser())) 
        {
            setNoPermissionMessage(context);
            setTarget(data, "reports,ReportList.vm");                        
        }
        else if (intake.isAllValid()) 
        {
            // make sure report has a name
            if (report.getName() == null || report.getName().trim().length() == 0) 
            {
                Group intakeReport = 
                    intake.get("Report", report.getQueryKey(), false);
                if (intakeReport == null) 
                {   
                    intakeReport = intake.get("Report", "", false);
                }  
            
                if (intakeReport != null) 
                {   
                    intakeReport.setValidProperties(report);
                }
            }

            if (report.getName() == null || report.getName().trim().length() == 0) 
            {
                getScarabRequestTool(context)
                    .setAlertMessage(l10n.get("SavedReportsMustHaveName"));
                setTarget(data, "reports,SaveReport.vm");
            }
            else 
            {
                // make sure name is unique
                org.tigris.scarab.om.Report savedReport = ReportPeer
                    .retrieveByName(report.getName());
                if (savedReport == null 
 savedReport.getQueryKey().equals(report.getQueryKey()))
                {
                    report.save();
                    getScarabRequestTool(context)
                        .setConfirmMessage(l10n.get("ReportSaved"));
                    setTarget(data, "reports,Report_1.vm");                    
                }
                else 
                {
                    getScarabRequestTool(context).setAlertMessage(
                        l10n.get("ReportNameNotUnique"));
                    setTarget(data, "reports,SaveReport.vm");
                }
            }
        }
        else 
        {
            getScarabRequestTool(context).setAlertMessage(
                l10n.get("ErrorPreventedSavingReport"));
        }
    }



    public void doDeletestoredreport(RunData data, TemplateContext context)
        throws Exception
    {
        ScarabUser user = (ScarabUser)data.getUser();
        String[] reportIds = data.getParameters().getStrings("report_id");
        if (reportIds == null || reportIds.length == 0) 
        {
            getScarabRequestTool(context).setAlertMessage(
                getLocalizationTool(context).get("MustSelectReport"));            
        }
        else 
        {
            for (int i=0;i<reportIds.length; i++)
            {
                String reportId = reportIds[i];
                if (reportId != null && reportId.length() > 0)
                {
                    Report torqueReport = ReportManager
                        .getInstance(new NumberKey(reportId), false);
                    if (new ReportBridge(torqueReport).isDeletable(user)) 
                    {
                        torqueReport.setDeleted(true);
                        torqueReport.save();
                    }                   
                    else 
                    {
                        getScarabRequestTool(context).setAlertMessage(
                            getLocalizationTool(context).get(NO_PERMISSION_MESSAGE));
                    }
                }
            }
        }        
    }

    public void doPrint(RunData data, TemplateContext context)
        throws Exception
    {
        populateReport(data, context);
        setTarget(data, "reports,Report_1.vm");
        ScarabLocalizationTool l10n = getLocalizationTool(context);
        getScarabRequestTool(context)
            .setInfoMessage(l10n.get("UseBrowserPrintReport"));
    }

    private ReportBridge populateReport(RunData data, TemplateContext context)
       throws Exception
    {
        return getScarabRequestTool(context).getReport();
    }

    private void setNoPermissionMessage(TemplateContext context)
    {
        ScarabRequestTool scarabR = getScarabRequestTool(context);
        ScarabLocalizationTool l10n = getLocalizationTool(context);
        scarabR.setAlertMessage(l10n.get(NO_PERMISSION_MESSAGE));
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

QDox parse error in file://<WORKSPACE>/data/code-rep-dataset/Dataset2/Tasks/5789.java