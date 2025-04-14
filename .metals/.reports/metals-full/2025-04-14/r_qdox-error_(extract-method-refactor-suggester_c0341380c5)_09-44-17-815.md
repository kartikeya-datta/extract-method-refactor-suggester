error id: file://<WORKSPACE>/data/code-rep-dataset/Dataset3/Tasks/3913.java
file://<WORKSPACE>/data/code-rep-dataset/Dataset3/Tasks/3913.java
### com.thoughtworks.qdox.parser.ParseException: syntax error @[1,1]

error in qdox parser
file content:
```java
offset: 1
uri: file://<WORKSPACE>/data/code-rep-dataset/Dataset3/Tasks/3913.java
text:
```scala
t@@hrow new RuntimeException("Error on getting TX id from BussinessActivityManager", se);

/*
 * JBoss, Home of Professional Open Source.
 * Copyright 2013, Red Hat, Inc., and individual contributors
 * as indicated by the @author tags. See the copyright.txt file in the
 * distribution for a full listing of individual contributors.
 *
 * This is free software; you can redistribute it and/or modify it
 * under the terms of the GNU Lesser General Public License as
 * published by the Free Software Foundation; either version 2.1 of
 * the License, or (at your option) any later version.
 *
 * This software is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE. See the GNU
 * Lesser General Public License for more details.
 *
 * You should have received a copy of the GNU Lesser General Public
 * License along with this software; if not, write to the Free
 * Software Foundation, Inc., 51 Franklin St, Fifth Floor, Boston, MA
 * 02110-1301 USA, or see the FSF site: http://www.fsf.org.
 */

package org.jboss.as.test.xts.wsba.participantcompletion.service;

import java.util.HashMap;
import java.util.Map;

import org.jboss.as.test.xts.base.TestApplicationException;
import org.jboss.as.test.xts.util.EventLog;
import org.jboss.as.test.xts.util.ServiceCommand;

import org.jboss.logging.Logger;

import com.arjuna.ats.arjuna.common.Uid;
import com.arjuna.mw.wst11.BusinessActivityManager;
import com.arjuna.mw.wst11.BusinessActivityManagerFactory;
import com.arjuna.wst.SystemException;
import com.arjuna.wst11.BAParticipantManager;

import javax.inject.Inject;

import static org.jboss.as.test.xts.util.ServiceCommand.*;

/**
 * Service implemenation - this implemetation is inherited by web services.
 */
public abstract class BAParticipantCompletionSuperService implements BAParticipantCompletion {
    private static final Logger log = Logger.getLogger(BAParticipantCompletionSuperService.class);
    
    // for tests this is not needed - but the code operating with participant registry should be synchronized
    private static final Map<String, BAParticipantManager> participantRegistry = new HashMap<String, BAParticipantManager>();
    
    @Inject
    private EventLog eventLog;
    
       
    /**
     * Add an item to a set Enrolls a Participant if necessary and passes the call through to the business logic.
     * 
     * @param value the value to add to the set.
     * @throws  
     * @throws AlreadyInSetException if value is already in the set
     * @throws SetServiceException if an error occurred when attempting to add the item to the set.
     */
    public void saveData(String value, ServiceCommand... serviceCommands) throws TestApplicationException {

        log.info("[BA PARTICIPANT COMPL SERVICE] invoked saveData('" + value + "')");
        eventLog.foundEventLogName(value);
        
        BAParticipantManager participantManager;
        BusinessActivityManager activityManager = BusinessActivityManagerFactory.businessActivityManager();
        String txid;
        
        try {
            txid = activityManager.currentTransaction().toString();
        } catch (SystemException se) {
            throw new RuntimeException("Error on getting TX id from BusinessActivityManager", se);
        }
        
        if(participantRegistry.keySet().contains(txid) && ServiceCommand.isPresent(REUSE_BA_PARTICIPANT, serviceCommands)) {
            log.info("[BA PARTICIPANT COMPL SERVICE] Reusing BA participant manager - command: " + REUSE_BA_PARTICIPANT);
            participantManager = participantRegistry.get(txid);
        } else {
            try {
                // Enlist the Participant for this service:
                BAParticipantCompletionParticipant participant = new BAParticipantCompletionParticipant(serviceCommands, eventLog, value);
                log.info("[BA PARTICIPANT COMPL SERVICE] Enlisting a participant into the BA");
                participantManager = activityManager.enlistForBusinessAgreementWithParticipantCompletion(participant,
                        "BAParticipantCompletition:" + new Uid().toString());
                participantRegistry.put(txid, participantManager);
            } catch (Exception e) {
                log.error("[BA PARTICIPANT COMPL SERVICE]  Participant enlistment failed", e);
                throw new RuntimeException("Error enlisting participant", e);
            }
        }

        if (ServiceCommand.isPresent(APPLICATION_EXCEPTION, serviceCommands)) {
            throw new TestApplicationException("Intentionally thrown Application Exception - service command set to: " + APPLICATION_EXCEPTION);
        }   
        
        // There could be invoked some back-end business logic here
        
        /*
         * This service employs the participant completion protocol which means it decides when it wants to commit local
         * changes. If the local changes (adding the item to the set) succeeded, we notify the coordinator that we have
         * completed. Otherwise, we notify the coordinator that we cannot complete. If any other participant fails or the client
         * decides to cancel we can rely upon being told to compensate.
         */
        log.info("[BA PARTICIPANT COMPL SERVICE] Prepare the backend resource and if successful notify the coordinator that we have completed our work");
        if (ServiceCommand.isPresent(DO_COMPLETE, serviceCommands)) {
            try {
                // Tell the coordinator manager we have finished our work
                log.info("[BA PARTICIPANT COMPL SERVICE] Prepare successful, notifying coordinator of completion");
                participantManager.completed();
            } catch (Exception e) {
                /* Failed to notify the coordinator that we have finished our work. Compensate the work and throw an Exception
                 * to notify the client that the add operation failed. */
                log.error("[BA PARTICIPANT COMPL SERVICE] 'completed' callback failed");
                throw new RuntimeException("Error when notifying the coordinator that the work is completed", e);
            }
        } 
        
        if (ServiceCommand.isPresent(CANNOT_COMPLETE, serviceCommands)) {
            try {
                // Tell the participant manager we cannot complete. This will force the activity to fail.
                log.info("[BA PARTICIPANT COMPL SERVICE] Prepared fail, notifying coordinator that we cannot complete");
                participantManager.cannotComplete();
                return;
            } catch (Exception e) {
                log.error("[BA PARTICIPANT COMPL SERVICE] 'cannotComplete' callback failed");
                throw new RuntimeException("Error when notifying the coordinator that the work is cannot be completed", e);
            }
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

QDox parse error in file://<WORKSPACE>/data/code-rep-dataset/Dataset3/Tasks/3913.java