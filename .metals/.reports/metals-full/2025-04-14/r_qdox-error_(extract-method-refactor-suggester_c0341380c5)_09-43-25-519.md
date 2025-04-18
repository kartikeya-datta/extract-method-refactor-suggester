error id: file://<WORKSPACE>/data/code-rep-dataset/Dataset3/Tasks/1072.java
file://<WORKSPACE>/data/code-rep-dataset/Dataset3/Tasks/1072.java
### com.thoughtworks.qdox.parser.ParseException: syntax error @[1,7]

error in qdox parser
file content:
```java
offset: 7
uri: file://<WORKSPACE>/data/code-rep-dataset/Dataset3/Tasks/1072.java
text:
```scala
final D@@eploymentTask deploymentTask = new DeploymentTask(new NewOperationBuilder(composite).build());

/*
 * JBoss, Home of Professional Open Source.
 * Copyright 2010, Red Hat, Inc., and individual contributors
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

package org.jboss.as.server.deployment.scanner;

import static org.jboss.as.controller.descriptions.ModelDescriptionConstants.ARCHIVE;
import static org.jboss.as.controller.descriptions.ModelDescriptionConstants.CANCELLED;
import static org.jboss.as.controller.descriptions.ModelDescriptionConstants.CHILD_TYPE;
import static org.jboss.as.controller.descriptions.ModelDescriptionConstants.COMPOSITE;
import static org.jboss.as.controller.descriptions.ModelDescriptionConstants.CONTENT;
import static org.jboss.as.controller.descriptions.ModelDescriptionConstants.DEPLOYMENT;
import static org.jboss.as.controller.descriptions.ModelDescriptionConstants.FAILED;
import static org.jboss.as.controller.descriptions.ModelDescriptionConstants.FAILURE_DESCRIPTION;
import static org.jboss.as.controller.descriptions.ModelDescriptionConstants.NAME;
import static org.jboss.as.controller.descriptions.ModelDescriptionConstants.OUTCOME;
import static org.jboss.as.controller.descriptions.ModelDescriptionConstants.PATH;
import static org.jboss.as.controller.descriptions.ModelDescriptionConstants.READ_CHILDREN_NAMES_OPERATION;
import static org.jboss.as.controller.descriptions.ModelDescriptionConstants.RELATIVE_TO;
import static org.jboss.as.controller.descriptions.ModelDescriptionConstants.RESULT;
import static org.jboss.as.controller.descriptions.ModelDescriptionConstants.STEPS;
import static org.jboss.as.controller.descriptions.ModelDescriptionConstants.SUCCESS;

import java.io.Closeable;
import java.io.File;
import java.io.FileFilter;
import java.io.FileOutputStream;
import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.concurrent.Callable;
import java.util.concurrent.CancellationException;
import java.util.concurrent.Future;
import java.util.concurrent.ScheduledExecutorService;
import java.util.concurrent.ScheduledFuture;
import java.util.concurrent.TimeUnit;
import java.util.concurrent.TimeoutException;
import java.util.concurrent.locks.Lock;
import java.util.concurrent.locks.ReentrantLock;
import java.util.regex.Pattern;

import org.jboss.as.controller.OperationFailedException;
import org.jboss.as.controller.client.NewModelControllerClient;
import org.jboss.as.controller.client.NewOperation;
import org.jboss.as.controller.client.NewOperationBuilder;
import org.jboss.as.controller.operations.common.Util;
import org.jboss.as.server.deployment.DeploymentAddHandler;
import org.jboss.as.server.deployment.DeploymentDeployHandler;
import org.jboss.as.server.deployment.DeploymentFullReplaceHandler;
import org.jboss.as.server.deployment.DeploymentRedeployHandler;
import org.jboss.as.server.deployment.DeploymentRemoveHandler;
import org.jboss.as.server.deployment.DeploymentUndeployHandler;
import org.jboss.as.server.deployment.repository.api.ContentRepository;
import org.jboss.as.server.deployment.repository.api.ServerDeploymentRepository;
import org.jboss.as.server.deployment.scanner.ZipCompletionScanner.NonScannableZipException;
import org.jboss.as.server.deployment.scanner.api.DeploymentScanner;
import org.jboss.dmr.ModelNode;
import org.jboss.dmr.Property;
import org.jboss.logging.Logger;

/**
 * Service that monitors the filesystem for deployment content and if found
 * deploys it.
 *
 * @author Brian Stansberry
 */
class FileSystemDeploymentService implements DeploymentScanner {

    private static final Pattern ARCHIVE_PATTERN = Pattern.compile("^.*\\.[SsWwJjEeRr][Aa][Rr]$");
    private static final Logger log = Logger.getLogger("org.jboss.as.deployment");

    static final String DEPLOYED = ".deployed";
    static final String FAILED_DEPLOY = ".failed";
    static final String DO_DEPLOY = ".dodeploy";
    static final String DEPLOYING = ".isdeploying";
    static final String UNDEPLOYING = ".isundeploying";
    static final String UNDEPLOYED = ".undeployed";
    static final String SKIP_DEPLOY = ".skipdeploy";
    static final String PENDING = ".pending";

    /** Max period an incomplete auto-deploy file can have no change in content */
    static final long MAX_NO_PROGRESS = 60000;

    /** Default timeout for deployments to execute in seconds*/
    static final long DEFAULT_DEPLOYMENT_TIMEOUT = 60;

    private File deploymentDir;
    private long scanInterval = 0;
    private volatile boolean scanEnabled = false;
    private ScheduledFuture<?> scanTask;
    private ScheduledFuture<?> rescanIncompleteTask;
    private final Lock scanLock = new ReentrantLock();

    private final Map<String, DeploymentMarker> deployed = new HashMap<String, DeploymentMarker>();
    private final HashSet<String> ignoredMissingDeployments = new HashSet<String>();
    private final HashSet<String> noticeLogged = new HashSet<String>();
    private final HashSet<File> nonscannableLogged = new HashSet<File>();
    private final Map<File, IncompleteDeploymentStatus> incompleteDeployments = new HashMap<File, IncompleteDeploymentStatus>();

    private final ScheduledExecutorService scheduledExecutor;
    private final NewModelControllerClient controllerClient;
    private final ServerDeploymentRepository deploymentRepository;
    private final ContentRepository contentRepository;

    private FileFilter filter = new ExtensibleFilter();
    private volatile boolean autoDeployZip;
    private volatile boolean autoDeployExploded;
    private volatile long maxNoProgress = MAX_NO_PROGRESS;

    private volatile long deploymentTimeout = DEFAULT_DEPLOYMENT_TIMEOUT;

    private final String relativeTo;
    private final String relativePath;

    private final Runnable scanRunnable = new Runnable() {
        @Override
        public void run() {
            try {
                scan();
            } catch (Exception e) {
                log.errorf(e, "Scan of %s threw Exception", deploymentDir.getAbsolutePath());
            }
        }
    };

    FileSystemDeploymentService(final String relativeTo, final File deploymentDir, final File relativeToDir, final NewModelControllerClient controllerClient, final ScheduledExecutorService scheduledExecutor,
            final ServerDeploymentRepository deploymentRepository, final ContentRepository contentRepository) throws OperationFailedException {
        assert contentRepository != null : "content repository is null";
        if (scheduledExecutor == null) {
            throw new IllegalStateException("null scheduled executor");
        }
        if (controllerClient == null) {
            throw new IllegalStateException("null controller client");
        }
        if (deploymentRepository == null) {
            throw new IllegalStateException("null deployment repository");
        }
        if (deploymentDir == null) {
            throw new IllegalStateException("null deployment dir");
        }
        if (!deploymentDir.exists()) {
            throw new IllegalArgumentException(deploymentDir.getAbsolutePath() + " does not exist");
        }
        if (!deploymentDir.isDirectory()) {
            throw new IllegalArgumentException(deploymentDir.getAbsolutePath() + " is not a directory");
        }
        if (!deploymentDir.canWrite()) {
            throw new IllegalArgumentException(deploymentDir.getAbsolutePath() + " is not writable");
        }
        this.relativeTo = relativeTo;
        this.deploymentDir = deploymentDir;
        this.controllerClient = controllerClient;
        this.scheduledExecutor = scheduledExecutor;
        this.deploymentRepository = deploymentRepository;
        this.contentRepository = contentRepository;

        if (relativeToDir != null) {
            String fullDir = deploymentDir.getAbsolutePath();
            String relDir = relativeToDir.getAbsolutePath();
            String sub = fullDir.substring(relDir.length());
            if (sub.startsWith(File.separator)) {
                sub = sub.length() == 1 ? "" : sub.substring(1);
            }
            this.relativePath = sub.length() > 0 ? sub + "/" : sub;
        }
        else {
            relativePath = null;
        }
        establishDeployedContentList(deploymentDir);
    }

    @Override
    public boolean isAutoDeployZippedContent() {
        return autoDeployZip;
    }

    @Override
    public void setAutoDeployZippedContent(boolean autoDeployZip) {
        this.autoDeployZip = autoDeployZip;
    }

    @Override
    public boolean isAutoDeployExplodedContent() {
        return autoDeployExploded;
    }

    @Override
    public void setAutoDeployExplodedContent(boolean autoDeployExploded) {
        if (autoDeployExploded && !this.autoDeployExploded) {
            log.warnf("Reliable deployment behaviour is not possible when auto-deployment of exploded content is enabled " +
                    "(i.e. deployment without use of \"%s\"' marker files). Configuration of auto-deployment of exploded content " +
                    "is not recommended in any situation where reliability is desired. Configuring the deployment " +
                    "scanner's %s setting to \"false\" is recommended.", DO_DEPLOY, CommonAttributes.AUTO_DEPLOY_EXPLODED);
        }
        this.autoDeployExploded = autoDeployExploded;
    }


    @Override
    public boolean isEnabled() {
        return scanEnabled;
    }

    @Override
    public long getScanInterval() {
        return scanInterval;
    }

    @Override
    public synchronized void setScanInterval(long scanInterval) {
        if (scanInterval != this.scanInterval) {
            cancelScan();
        }
        this.scanInterval = scanInterval;
        startScan();
    }

    @Override
    public void setDeploymentTimeout(long deploymentTimeout) {
        this.deploymentTimeout = deploymentTimeout;
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public synchronized void startScanner() {
        final boolean scanEnabled = this.scanEnabled;
        if (scanEnabled) {
            return;
        }
        this.scanEnabled = true;
        startScan();
        log.infof("Started %s for directory %s", getClass().getSimpleName(), deploymentDir.getAbsolutePath());
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public synchronized void stopScanner() {
        this.scanEnabled = false;
        cancelScan();
    }

    /** Hook solely for unit test to control how long deployments with no progress can exist without failing */
    void setMaxNoProgress(long max) {
        this.maxNoProgress = max;
    }

    private void establishDeployedContentList(File dir) throws OperationFailedException {
        final Set<String> deploymentNames = getDeploymentNames();
        final File[] children = dir.listFiles();
        for (File child : children) {
            final String fileName = child.getName();
            if (child.isDirectory()) {
                if (! isEEArchive(fileName)) {
                    establishDeployedContentList(child);
                }
            } else if (fileName.endsWith(DEPLOYED)) {
                final String deploymentName = fileName.substring(0, fileName.length() - DEPLOYED.length());
                if (deploymentNames.contains(deploymentName)) {
                    deployed.put(deploymentName, new DeploymentMarker(child.lastModified()));
                } else {
                    if (!child.delete()) {
                        log.warnf("Cannot removed extraneous deployment marker file %s", fileName);
                    }
                }
            }
        }
    }

    /** This method isn't private solely to allow a unit test in the same package to call it */
    void scan() {

        try {
            scanLock.lockInterruptibly();
        } catch (InterruptedException ie) {
            Thread.currentThread().interrupt();
            return;
        }

        boolean scheduleRescan = false;
        try {
            if (scanEnabled) { // confirm the scan is still wanted
                log.tracef("Scanning directory %s for deployment content changes", deploymentDir.getAbsolutePath());

                ScanContext scanContext = new ScanContext();
                scanDirectory(deploymentDir, scanContext);

                // WARN about markers with no associated content. Do this first in case any auto-deploy issue
                // is due to a file that wasn't meant to be auto-deployed, but has a misspelled marker
                ignoredMissingDeployments.retainAll(scanContext.ignoredMissingDeployments);
                for (String deploymentName : scanContext.ignoredMissingDeployments) {
                    if(ignoredMissingDeployments.add(deploymentName)) {
                        log.warnf("Deployment of '%s' requested, but the deployment is not present", deploymentName);
                    }
                }

                // Log INFO about non-auto-deploy files that have no marker files
                noticeLogged.retainAll(scanContext.nonDeployable);
                for (String fileName : scanContext.nonDeployable) {
                    if (noticeLogged.add(fileName)) {
                        log.infof("Found %s in deployment directory. To trigger deployment create a file called %s%s", fileName, fileName, DO_DEPLOY);
                    }
                }

                // Deal with any incomplete or non-scannable auto-deploy content
                ScanStatus status = handleAutoDeployFailures(scanContext);
                if (status != ScanStatus.PROCEED) {
                    if (status == ScanStatus.RETRY && scanInterval > 1000) {
                        // in finally block, schedule a non-repeating task to try again more quickly
                        scheduleRescan = true;
                    }
                    return;
                }

                List<ScannerTask> scannerTasks = scanContext.scannerTasks;

                // Add remove actions to the plan for anything we count as
                // deployed that we didn't find on the scan
                for (String missing : scanContext.toRemove) {
                    // TODO -- minor -- this assumes the deployment was in the root deploymentDir,
                    // not a child dir, and therefore puts the '.isundeploying' file there
                    File parent = deploymentDir;
                    scannerTasks.add(new UndeployTask(missing, parent));
                }

                // Process the tasks
                if (scannerTasks.size() > 0) {
                    List<ModelNode> updates = new ArrayList<ModelNode>(scannerTasks.size());

                    for (ScannerTask task : scannerTasks) {
                        task.recordInProgress(); // puts down .isdeploying, .isundeploying
                        final ModelNode update = task.getUpdate();
                        if (log.isDebugEnabled()) {
                            log.debugf("Deployment scan of [%s] found update action [%s]", deploymentDir, update);
                        }
                        updates.add(update);
                    }

                    while (!updates.isEmpty()) {
                        ModelNode composite = getCompositeUpdate(updates);

                        final DeploymentTask deploymentTask = new DeploymentTask(NewOperationBuilder.Factory.create(composite).build());
                        final Future<ModelNode> futureResults = scheduledExecutor.submit(deploymentTask);
                        final ModelNode results;
                        try {
                            results = futureResults.get(deploymentTimeout, TimeUnit.SECONDS);
                        } catch (TimeoutException e) {
                            futureResults.cancel(true);
                            final ModelNode failure = new ModelNode();
                            failure.get(OUTCOME).set(FAILED);
                            failure.get(FAILURE_DESCRIPTION).set("Did not receive a response to the deployment operation within " +
                                    "the allowed timeout period [" + deploymentTimeout + " seconds]. Check the server configuration" +
                                    "file and the server logs to find more about the status of the deployment.");
                            for (ScannerTask task : scannerTasks) {
                                task.handleFailureResult(failure);
                            }
                            break;
                        } catch (Exception e) {
                            log.error("File system deployment service failed", e);
                            futureResults.cancel(true);
                            final ModelNode failure = new ModelNode();
                            failure.get(OUTCOME).set(FAILED);
                            failure.get(FAILURE_DESCRIPTION).set(e.getMessage());
                            for (ScannerTask task : scannerTasks) {
                                task.handleFailureResult(failure);
                            }
                            break;
                        }

                        final List<Property> resultList = results.get(RESULT).asPropertyList();
                        final List<ModelNode> toRetry = new ArrayList<ModelNode>();
                        final List<ScannerTask> retryTasks = new ArrayList<ScannerTask>();
                        for (int i = 0; i < resultList.size(); i++) {
                            final ModelNode result = resultList.get(i).getValue();
                            final ScannerTask task = scannerTasks.get(i);
                            final ModelNode outcome = result.get(OUTCOME);
                            if (outcome.isDefined() && SUCCESS.equals(outcome.asString())) {
                                task.handleSuccessResult();
                            } else if (outcome.isDefined() && CANCELLED.equals(outcome.asString())) {
                                toRetry.add(updates.get(i));
                                retryTasks.add(task);
                            } else {
                                task.handleFailureResult(result);
                            }
                        }
                        updates = toRetry;
                        scannerTasks = retryTasks;
                    }
                }
                log.tracef("Scan complete");
            }
        } finally {
            scanLock.unlock();

            if (scheduleRescan) {
                synchronized (this) {
                    if (scanEnabled) {
                        rescanIncompleteTask = scheduledExecutor.schedule(scanRunnable, 200, TimeUnit.MILLISECONDS);
                    }
                }
            }
        }
    }

    /**
     * Scan the given directory for content changes.
     *
     * @param directory the directory to scan
     * @param scanContext context of the scan
     */
    private void scanDirectory(final File directory, final ScanContext scanContext) {
        final File[] children = directory.listFiles(filter);
        if (children == null) {
            return;
        }

        for (File child : children) {
            final String fileName = child.getName();
            if (fileName.endsWith(DEPLOYED)) {
                final String deploymentName = fileName.substring(0, fileName.length() - DEPLOYED.length());
                scanContext.toRemove.remove(deploymentName);
                if (!deployed.containsKey(deploymentName)) {
                    removeExtraneousMarker(child, fileName);
                }
                else if (deployed.get(deploymentName).lastModified != child.lastModified()) {
                    scanContext.scannerTasks.add(new RedeployTask(deploymentName, child.lastModified(), directory));
                }
            }
            else if (fileName.endsWith(DO_DEPLOY)) {
                final String deploymentName = fileName.substring(0, fileName.length() - DO_DEPLOY.length());
                final File deploymentFile = new File(directory, deploymentName);
                if (!deploymentFile.exists()) {
                    scanContext.ignoredMissingDeployments.add(deploymentName);
                    continue;
                }
                long timestamp = getDeploymentTimestamp(deploymentFile);
                final String path = relativeTo == null ? deploymentFile.getAbsolutePath() : relativePath + deploymentName; // TODO: sub-directories in the deploymentDir
                final boolean archive = deploymentFile.isFile();
                addContentAddingTask(path, archive, deploymentName, deploymentFile, timestamp, scanContext);
            }
            else if (fileName.endsWith(FAILED_DEPLOY)) {
                final String deploymentName = fileName.substring(0, fileName.length() - FAILED_DEPLOY.length());
                scanContext.toRemove.remove(deploymentName);
                if (!deployed.containsKey(deploymentName) && !(new File(child.getParent(), deploymentName).exists())) {
                    removeExtraneousMarker(child, fileName);
                }
            }
            else if (isEEArchive(fileName)) {
                boolean autoDeployable = child.isDirectory() ? autoDeployExploded : autoDeployZip;
                if (autoDeployable) {
                    if (!isAutoDeployDisabled(child)) {
                        final File failedMarker = new File(directory, fileName + FAILED_DEPLOY);
                        if(failedMarker.exists()) {// && child.lastModified() <= failedMarker.lastModified()) {
                            continue;  // Don't auto-retry failed deployments
                        }
                        final File undeployedMarker = new File(directory, fileName + UNDEPLOYED);
                        if(undeployedMarker.exists()) {// && child.lastModified() <= undeployedMarker.lastModified()) {
                            continue;  // Don't auto-deploy undeployed deployments
                        }

                        DeploymentMarker marker = deployed.get(fileName);
                        long timestamp = getDeploymentTimestamp(child);
                        if (marker == null || marker.lastModified != timestamp) {
                            try {
                                if (isZipComplete(child)) {
                                    final String path =  relativeTo == null ? child.getAbsolutePath() : relativePath + fileName;
                                    final boolean archive = child.isFile();
                                    addContentAddingTask(path, archive, fileName, child, timestamp, scanContext);
                                }
                                else {
                                    scanContext.incompleteFiles.put(child, new IncompleteDeploymentStatus(child));
                                }
                            } catch (NonScannableZipException e) {
                                // Track for possible logging in scan()
                                scanContext.nonscannable.put(child, e);
                            }
                        }
                    }
                }
                else if (!deployed.containsKey(fileName)
                        && !new File(fileName + DO_DEPLOY).exists() && !new File(fileName + FAILED_DEPLOY).exists()) {
                    // Track for possible INFO logging of the need for a marker
                    scanContext.nonDeployable.add(fileName);
                }
            }
            else if (fileName.endsWith(DEPLOYING) || fileName.endsWith(UNDEPLOYING)) {
                // These markers should not survive a scan
                removeExtraneousMarker(child, fileName);
            }
            else if (fileName.endsWith(PENDING)) {
                // Do some housekeeping if the referenced deployment is gone
                final String deploymentName = fileName.substring(0, fileName.length() - PENDING.length());
                File deployment = new File(child.getParent(), deploymentName);
                if (!deployment.exists()) {
                    removeExtraneousMarker(child, fileName);
                }
            }
            else if (child.isDirectory()) { // exploded deployments would have been caught by isEEArchive(fileName) above
                scanDirectory(child, scanContext);
            }
        }
    }

    private long addContentAddingTask(final String path, final boolean archive, final String deploymentName, final File deploymentFile, final long timestamp,
            final ScanContext scanContext) {
        if (scanContext.registeredDeployments.contains(deploymentName)) {
            scanContext.scannerTasks.add(new ReplaceTask(path, archive, deploymentName, deploymentFile, timestamp));
        } else {
            scanContext.scannerTasks.add(new DeployTask(path, archive, deploymentName, deploymentFile, timestamp));
        }
        scanContext.toRemove.remove(deploymentName);
        return timestamp;
    }

    private boolean isZipComplete(File file) throws NonScannableZipException {
        if (file.isDirectory()) {
            for (File child : file.listFiles()) {
                if (!isZipComplete(child)) {
                    return false;
                }
            }
            return true;
        }
        else if (isEEArchive(file.getName())) {
            try {
                return ZipCompletionScanner.isCompleteZip(file);
            } catch (IOException e) {
                log.error(String.format("Failed checking whether %s was a complete zip", file.getPath()), e);
                return false;
            }
        }
        else {
            // A non-zip child
            return true;
        }
    }

    private boolean isAutoDeployDisabled(File file) {
        final File parent = file.getParentFile();
        final String name = file.getName();
        return new File(parent, name + SKIP_DEPLOY).exists() || new File(parent, name + DO_DEPLOY).exists();
    }

    private long getDeploymentTimestamp(File deploymentFile) {
        if (deploymentFile.isDirectory()) {
            // Scan for most recent file
            long latest = deploymentFile.lastModified();
            for (File child : deploymentFile.listFiles()) {
                long childTimestamp = getDeploymentTimestamp(child);
                if (childTimestamp > latest) {
                    latest = childTimestamp;
                }
            }
            return latest;
        }
        else {
            return deploymentFile.lastModified();
        }
    }

    private boolean isEEArchive(String fileName) {
        return ARCHIVE_PATTERN.matcher(fileName).matches();
    }

    private void removeExtraneousMarker(File child, final String fileName) {
        if (!child.delete()) {
            log.warnf("Cannot remove extraneous deployment marker file %s", fileName);
        }
    }

    /**
     * Handle incompletely copied or non-scannable auto-deploy content and then abort scan
     *
     * @return true if the scan should be aborted
     */
    private ScanStatus handleAutoDeployFailures(ScanContext scanContext) {

        ScanStatus result = ScanStatus.PROCEED;
        boolean warnLogged = false;

        Set<File> noLongerIncomplete = new HashSet<File>(incompleteDeployments.keySet());
        noLongerIncomplete.removeAll(scanContext.incompleteFiles.keySet());

        int oldIncompleteCount = incompleteDeployments.size();
        incompleteDeployments.keySet().retainAll(scanContext.incompleteFiles.keySet());
        if (scanContext.incompleteFiles.size() > 0) {

            result = ScanStatus.RETRY;

            // If user dealt with some incomplete stuff but others remain, log everything again
            boolean logAll = incompleteDeployments.size() != oldIncompleteCount;

            long now = System.currentTimeMillis();
            for (Map.Entry<File, IncompleteDeploymentStatus> entry : scanContext.incompleteFiles.entrySet()) {
                File incompleteFile = entry.getKey();
                String deploymentName = incompleteFile.getName();
                IncompleteDeploymentStatus status = incompleteDeployments.get(incompleteFile);
                if (status == null || status.size < entry.getValue().size) {
                    status = entry.getValue();
                }

                if (now - status.timestamp > maxNoProgress) {
                    if (!status.warned) {
                        // Treat no progress for an extended period as a failed deployment
                        String suffix = deployed.containsKey(deploymentName)
                                            ? " A previous version of this content was deployed and remains deployed."
                                            : "";
                        String msg = String.format("Deployment content %s appears to be incomplete and is not progressing toward " +
                                "completion. This content cannot be auto-deployed.%s", incompleteFile, suffix, DO_DEPLOY, SKIP_DEPLOY);
                        writeFailedMarker(incompleteFile, new ModelNode().set(msg));
                        log.error(msg);
                        status.warned = true;
                        warnLogged = true;

                        result = ScanStatus.ABORT;
                    }

                    // Clean up any .pending file
                    new File(incompleteFile.getParentFile(), deploymentName + PENDING).delete();
                }
                else {
                    boolean newIncomplete = incompleteDeployments.put(incompleteFile, status) == null;
                    if (newIncomplete || logAll) {
                        log.infof("Scan found incompletely copied file content for deployment %s. Deployment changes will not be " +
                                "processed until all content is complete.", entry.getKey().getPath());
                    }
                    if (newIncomplete) {
                        File pending = new File(incompleteFile.getParentFile(), deploymentName + PENDING);
                        createMarkerFile(pending, deploymentName);
                    }
                }
            }
        }

        // Clean out any old "pending" files
        for (File complete : noLongerIncomplete) {
            File pending = new File(complete.getParentFile(), complete.getName() + PENDING);
            removeExtraneousMarker(pending, pending.getName());
        }

        int oldNonScannableCount = nonscannableLogged.size();
        nonscannableLogged.retainAll(scanContext.nonscannable.keySet());
        if (scanContext.nonscannable.size() > 0) {

            result = (result == ScanStatus.PROCEED ? ScanStatus.RETRY : result);

            // If user dealt with some nonscannable stuff but others remain, log everything again
            boolean logAll = nonscannableLogged.size() != oldNonScannableCount;

            for (Map.Entry<File, NonScannableZipException> entry : scanContext.nonscannable.entrySet()) {
                File nonScannable = entry.getKey();
                String fileName = nonScannable.getName();
                if (nonscannableLogged.add(nonScannable) || logAll) {
                    NonScannableZipException e = entry.getValue();
                    String msg = String.format("File %s was configured for auto-deploy but could not be safely auto-deployed. The reason the file " +
                            "could not be auto-deployed was: %s.  To enable deployment of this file create a file called %s%s",
                            fileName, e.getLocalizedMessage(), fileName, DO_DEPLOY);
                    writeFailedMarker(nonScannable, new ModelNode().set(msg));
                    log.error(msg);
                    warnLogged = true;

                    result = ScanStatus.ABORT;
                }
            }
        }

        if (warnLogged) {

            Set<String> allProblems = new HashSet<String>();
            for (File f : scanContext.nonscannable.keySet()) {
                allProblems.add(f.getName());
            }
            for (File f : scanContext.incompleteFiles.keySet()) {
                allProblems.add(f.getName());
            }

            log.warnf("Scan found content configured for auto-deploy that could not be safely auto-deployed. See details above. " +
                    "Deployment changes will not be processed until all problematic content is either removed or whether to " +
                    "deploy the content or not is indicated via a %s or %s marker file. Problematic deployments are %s",
                    DO_DEPLOY, SKIP_DEPLOY, allProblems);
        }

        return result;
    }

    private synchronized void startScan() {
        if (scanEnabled) {
            if (scanInterval > 0) {
                scanTask = scheduledExecutor.scheduleWithFixedDelay(scanRunnable, 0, scanInterval, TimeUnit.MILLISECONDS);
            } else {
                scanTask = scheduledExecutor.schedule(scanRunnable, scanInterval, TimeUnit.MILLISECONDS);
            }
        }
    }

    /**
     * Invoke with the object monitor held
     */
    private void cancelScan() {
        if (rescanIncompleteTask != null) {
            rescanIncompleteTask.cancel(false);
            rescanIncompleteTask = null;
        }
        if (scanTask != null) {
            scanTask.cancel(false);
            scanTask = null;
        }
    }

    private Set<String> getDeploymentNames() throws CancellationException {
        final ModelNode op = Util.getEmptyOperation(READ_CHILDREN_NAMES_OPERATION, new ModelNode());
        op.get(CHILD_TYPE).set(DEPLOYMENT);
        ModelNode response;
        try {
            response = controllerClient.execute(op);
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
        final ModelNode result = response.get(RESULT);
        final Set<String> deploymentNames = new HashSet<String>();
        if (result.isDefined()) {
            final List<ModelNode> deploymentNodes = result.asList();
            for (ModelNode node : deploymentNodes) {
                deploymentNames.add(node.asString());
            }
        }
        return deploymentNames;
    }

    private ModelNode getCompositeUpdate(final List<ModelNode> updates) {
        final ModelNode op = Util.getEmptyOperation(COMPOSITE, new ModelNode());
        final ModelNode steps = op.get(STEPS);
        for (ModelNode update : updates) {
            steps.add(update);
        }
        return op;
    }

    private ModelNode getCompositeUpdate(final ModelNode... updates) {
        final ModelNode op = Util.getEmptyOperation(COMPOSITE, new ModelNode());
        final ModelNode steps = op.get(STEPS);
        for (ModelNode update : updates) {
            steps.add(update);
        }
        return op;
    }

    private void safeClose(final Closeable closeable) {
        if (closeable != null) {
            try {
                closeable.close();
            } catch (IOException ignored) {
            }
        }
    }

    private void createMarkerFile(final File marker, String deploymentName) {
        FileOutputStream fos = null;
        try {
            marker.createNewFile();
            fos = new FileOutputStream(marker);
            fos.write(deploymentName.getBytes());
        } catch (IOException io) {
            log.errorf(io, "Caught exception writing deployment marker file %s", marker.getAbsolutePath());
        } finally {
            safeClose(fos);
        }
    }

    private void writeFailedMarker(final File deploymentFile, final ModelNode failureDescription) {
        final File failedMarker = new File(deploymentFile.getParent(), deploymentFile.getName() + FAILED_DEPLOY);
        final File deployMarker = new File(deploymentFile.getParent(), deploymentFile.getName() + DO_DEPLOY);
        if (deployMarker.exists() && !deployMarker.delete()) {
            log.warnf("Unable to remove marker file %s", deployMarker);
        }
        final File deployedMarker = new File(deploymentFile.getParent(), deploymentFile.getName() + DEPLOYED);
        if (deployedMarker.exists() && !deployedMarker.delete()) {
            log.warnf("Unable to remove marker file %s", deployedMarker);
        }
        final File undeployedMarker = new File(deploymentFile.getParent(), deploymentFile.getName() + UNDEPLOYED);
        if (undeployedMarker.exists() && !undeployedMarker.delete()) {
            log.warnf("Unable to remove marker file %s", undeployedMarker);
        }
        FileOutputStream fos = null;
        try {
            failedMarker.createNewFile();
            fos = new FileOutputStream(failedMarker);
            fos.write(failureDescription.asString().getBytes());
        } catch (IOException io) {
            log.errorf(io, "Caught exception writing deployment failed marker file %s", failedMarker.getAbsolutePath());
        } finally {
            safeClose(fos);
        }
    }

    private abstract class ScannerTask {
        protected final String deploymentName;
        protected final String parent;
        private final String inProgressMarkerSuffix;

        private ScannerTask(final String deploymentName, final File parent, final String inProgressMarkerSuffix) {
            this.deploymentName = deploymentName;
            this.parent = parent.getAbsolutePath();
            this.inProgressMarkerSuffix = inProgressMarkerSuffix;
            File marker = new File(parent, deploymentName + PENDING);
            if (!marker.exists()) {
                createMarkerFile(marker, deploymentName);
            }
        }

        protected void recordInProgress() {
            File marker = new File(parent, deploymentName + inProgressMarkerSuffix);
            createMarkerFile(marker, deploymentName);
            deleteUndeployedMarker();
            deletePendingMarker();
        }

        protected abstract ModelNode getUpdate();

        protected abstract void handleSuccessResult();

        protected abstract void handleFailureResult(final ModelNode result);

        protected void deletePendingMarker() {
            final File pendingMarker = new File(parent, deploymentName + PENDING);
            if (pendingMarker.exists() && !pendingMarker.delete()) {
                log.warnf("Unable to remove marker file %s", pendingMarker);
            }
        }

        protected void deleteUndeployedMarker() {
            final File undeployedMarker = new File(parent, deploymentName + UNDEPLOYED);
            if (undeployedMarker.exists() && !undeployedMarker.delete()) {
                log.warnf("Unable to remove marker file %s", undeployedMarker);
            }
        }

        protected void removeInProgressMarker() {
            File marker = new File(new File(parent), deploymentName + inProgressMarkerSuffix);
            if (marker.exists() && !marker.delete()) {
                log.warnf("Cannot delete deployment progress marker file %s", marker);
            }
        }
    }

    private abstract class ContentAddingTask extends ScannerTask {
        private final String path;
        private final boolean archive;
        protected final File deploymentFile;
        protected final long doDeployTimestamp;

        protected ContentAddingTask(final String path, final boolean archive, final String deploymentName, final File deploymentFile, long markerTimestamp) {
            super(deploymentName, deploymentFile.getParentFile(), DEPLOYING);
            this.path = path;
            this.archive = archive;
            this.deploymentFile = deploymentFile;
            this.doDeployTimestamp = markerTimestamp;
        }

        protected ModelNode createContent() {
            final ModelNode content = new ModelNode();
            final ModelNode contentItem = content.get(0);
            contentItem.get(PATH).set(path);
            if (relativeTo != null)
                contentItem.get(RELATIVE_TO).set(relativeTo);
            contentItem.get(ARCHIVE).set(archive);
            return content;
        }

        @Override
        protected void handleSuccessResult() {
            final File doDeployMarker = new File(new File(parent), deploymentFile.getName() + DO_DEPLOY);
            if (doDeployMarker.exists() && !doDeployMarker.delete()) {
                log.errorf("Failed to delete deployment marker file %s", doDeployMarker.getAbsolutePath());
            }

            // Remove any previous failure marker
            final File failedMarker = new File(deploymentFile.getParent(), deploymentFile.getName() + FAILED_DEPLOY);
            if (failedMarker.exists() && !failedMarker.delete()) {
                log.warnf("Unable to remove marker file %s", failedMarker);
            }

            // Remove the in-progress marker
            removeInProgressMarker();

            final File deployedMarker = new File(parent, deploymentFile.getName() + DEPLOYED);
            createMarkerFile(deployedMarker, deploymentName);
            deployedMarker.setLastModified(doDeployTimestamp);
            if (deployed.containsKey(deploymentName)) {
                deployed.remove(deploymentName);
            }
            deployed.put(deploymentName, new DeploymentMarker(deployedMarker.lastModified()));
        }
    }

    private final class DeployTask extends ContentAddingTask {
        private DeployTask(final String path, final boolean archive, final String deploymentName, final File deploymentFile, long markerTimestamp) {
            super(path, archive, deploymentName, deploymentFile, markerTimestamp);
        }

        @Override
        protected ModelNode getUpdate() {
            final ModelNode address = new ModelNode().add(DEPLOYMENT, deploymentName);
            final ModelNode addOp = Util.getEmptyOperation(DeploymentAddHandler.OPERATION_NAME, address);
            addOp.get(CONTENT).set(createContent());
            final ModelNode deployOp = Util.getEmptyOperation(DeploymentDeployHandler.OPERATION_NAME, address);
            return getCompositeUpdate(addOp, deployOp);
        }

        @Override
        protected void handleFailureResult(final ModelNode result) {
            log.error(result.get(FAILURE_DESCRIPTION).asString());

            // Remove the in-progress marker
            removeInProgressMarker();

            writeFailedMarker(deploymentFile, result.get(FAILURE_DESCRIPTION));
        }
    }

    private final class ReplaceTask extends ContentAddingTask {
        private ReplaceTask(final String path, final boolean archive, String deploymentName, File deploymentFile, long markerTimestamp) {
            super(path, archive, deploymentName, deploymentFile, markerTimestamp);
        }

        @Override
        protected ModelNode getUpdate() {
            final ModelNode replaceOp = Util.getEmptyOperation(DeploymentFullReplaceHandler.OPERATION_NAME, new ModelNode());
            replaceOp.get(NAME).set(deploymentName);
            replaceOp.get(CONTENT).set(createContent());
            return replaceOp;
        }

        @Override
        protected void handleFailureResult(ModelNode result) {

            // Remove the in-progress marker
            removeInProgressMarker();

            writeFailedMarker(deploymentFile, result.get(FAILURE_DESCRIPTION));
        }
    }

    private final class RedeployTask extends ScannerTask {
        private final long markerLastModified;

        private RedeployTask(final String deploymentName, final long markerLastModified, final File parent) {
            super(deploymentName, parent, DEPLOYING);
            this.markerLastModified = markerLastModified;
        }

        @Override
        protected ModelNode getUpdate() {
            final ModelNode address = new ModelNode().add(DEPLOYMENT, deploymentName);
            return Util.getEmptyOperation(DeploymentRedeployHandler.OPERATION_NAME, address);
        }

        @Override
        protected void handleSuccessResult() {

            // Remove the in-progress marker
            removeInProgressMarker();

            deployed.remove(deploymentName);
            deployed.put(deploymentName, new DeploymentMarker(markerLastModified));

        }

        @Override
        protected void handleFailureResult(ModelNode result) {

            // Remove the in-progress marker
            removeInProgressMarker();

            writeFailedMarker(new File(parent, deploymentName), result.get(FAILURE_DESCRIPTION));
        }
    }

    private final class UndeployTask extends ScannerTask {
        private UndeployTask(final String deploymentName, final File parent) {
            super(deploymentName, parent, UNDEPLOYING);
        }

        @Override
        protected ModelNode getUpdate() {
            final ModelNode address = new ModelNode().add(DEPLOYMENT, deploymentName);
            final ModelNode undeployOp = Util.getEmptyOperation(DeploymentUndeployHandler.OPERATION_NAME, address);
            final ModelNode removeOp = Util.getEmptyOperation(DeploymentRemoveHandler.OPERATION_NAME, address);
            return getCompositeUpdate(undeployOp, removeOp);
        }

        @Override
        protected void handleSuccessResult() {

            // Remove the in-progress marker
            removeInProgressMarker();

            final File deployedMarker = new File(parent, deploymentName + UNDEPLOYED);
            createMarkerFile(deployedMarker, deploymentName);

            deployed.remove(deploymentName);
            noticeLogged.remove(deploymentName);
        }

        @Override
        protected void handleFailureResult(ModelNode result) {

            // Remove the in-progress marker
            removeInProgressMarker();

            writeFailedMarker(new File(parent, deploymentName), result.get(FAILURE_DESCRIPTION));
        }
    }

    private class DeploymentMarker {
        private long lastModified;

        private DeploymentMarker(final long lastModified) {
            this.lastModified = lastModified;
        }
    }

    private class ScanContext {
        /** Existing deployments */
        private final Set<String> registeredDeployments = getDeploymentNames();
        /** Tasks generated by the scan */
        private final List<ScannerTask> scannerTasks = new ArrayList<ScannerTask>();
        /** Files to undeploy at the end of the scan */
        private final Set<String> toRemove = new HashSet<String>(deployed.keySet());
        /** Marker files with no corresponding content */
        private final HashSet<String> ignoredMissingDeployments = new HashSet<String>();
        /** Partially copied files detected by the scan */
        private Map<File, IncompleteDeploymentStatus> incompleteFiles = new HashMap<File, IncompleteDeploymentStatus>();
        /** Non-auto-deployable files detected by the scan without an appropriate marker */
        private final HashSet<String> nonDeployable = new HashSet<String>();
        /** Auto-deployable files detected by the scan where ZipScanner threw a NonScannableZipException */
        private final Map<File, NonScannableZipException> nonscannable = new HashMap<File, NonScannableZipException>();
    }

    private class IncompleteDeploymentStatus {
        private final long timestamp = System.currentTimeMillis();
        private final long size;
        private boolean warned;

        IncompleteDeploymentStatus(File file) {
            this.size = file.length();
        }
    }

    /** Possible overall scan behaviors following return from handling auto-deploy failures */
    private enum ScanStatus {
        ABORT, RETRY, PROCEED
    }

    private class DeploymentTask implements Callable<ModelNode> {
        private final NewOperation deploymentOp;

        private DeploymentTask(final NewOperation deploymentOp) {
            this.deploymentOp = deploymentOp;
        }

        @Override
        public ModelNode call() {
            try {
                return controllerClient.execute(deploymentOp);
            } catch (IOException e) {
                throw new RuntimeException(e);
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

QDox parse error in file://<WORKSPACE>/data/code-rep-dataset/Dataset3/Tasks/1072.java