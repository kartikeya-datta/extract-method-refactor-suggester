error id: file://<WORKSPACE>/data/code-rep-dataset/Dataset3/Tasks/6732.java
file://<WORKSPACE>/data/code-rep-dataset/Dataset3/Tasks/6732.java
### com.thoughtworks.qdox.parser.ParseException: syntax error @[1,1]

error in qdox parser
file content:
```java
offset: 1
uri: file://<WORKSPACE>/data/code-rep-dataset/Dataset3/Tasks/6732.java
text:
```scala
c@@ontrollerOperations.add(operation.clone()); // clone so we don't log op nodes mutated during execution

/*
 * JBoss, Home of Professional Open Source.
 * Copyright 2011, Red Hat, Inc., and individual contributors
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

package org.jboss.as.controller;

import static org.jboss.as.controller.ControllerLogger.MGMT_OP_LOGGER;
import static org.jboss.as.controller.ControllerMessages.MESSAGES;
import static org.jboss.as.controller.descriptions.ModelDescriptionConstants.ALLOW_RESOURCE_SERVICE_RESTART;
import static org.jboss.as.controller.descriptions.ModelDescriptionConstants.CANCELLED;
import static org.jboss.as.controller.descriptions.ModelDescriptionConstants.FAILED;
import static org.jboss.as.controller.descriptions.ModelDescriptionConstants.FAILURE_DESCRIPTION;
import static org.jboss.as.controller.descriptions.ModelDescriptionConstants.OP;
import static org.jboss.as.controller.descriptions.ModelDescriptionConstants.OPERATION_HEADERS;
import static org.jboss.as.controller.descriptions.ModelDescriptionConstants.OPERATION_REQUIRES_RELOAD;
import static org.jboss.as.controller.descriptions.ModelDescriptionConstants.OPERATION_REQUIRES_RESTART;
import static org.jboss.as.controller.descriptions.ModelDescriptionConstants.OP_ADDR;
import static org.jboss.as.controller.descriptions.ModelDescriptionConstants.OUTCOME;
import static org.jboss.as.controller.descriptions.ModelDescriptionConstants.RESPONSE_HEADERS;
import static org.jboss.as.controller.descriptions.ModelDescriptionConstants.RESULT;
import static org.jboss.as.controller.descriptions.ModelDescriptionConstants.ROLLBACK_ON_RUNTIME_FAILURE;
import static org.jboss.as.controller.descriptions.ModelDescriptionConstants.ROLLED_BACK;
import static org.jboss.as.controller.descriptions.ModelDescriptionConstants.ROLLOUT_PLAN;
import static org.jboss.as.controller.descriptions.ModelDescriptionConstants.RUNTIME_UPDATE_SKIPPED;
import static org.jboss.as.controller.descriptions.ModelDescriptionConstants.SERVER_GROUPS;
import static org.jboss.as.controller.descriptions.ModelDescriptionConstants.SUCCESS;

import java.net.InetAddress;
import java.security.Principal;
import java.util.ArrayDeque;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.Deque;
import java.util.EnumMap;
import java.util.HashSet;
import java.util.List;
import java.util.Set;
import java.util.concurrent.LinkedBlockingDeque;
import java.util.concurrent.atomic.AtomicReference;

import javax.security.auth.Subject;

import org.jboss.as.controller.access.Caller;
import org.jboss.as.controller.access.Environment;
import org.jboss.as.controller.audit.AuditLogger;
import org.jboss.as.controller.client.MessageSeverity;
import org.jboss.as.controller.persistence.ConfigurationPersistenceException;
import org.jboss.as.controller.persistence.ConfigurationPersister;
import org.jboss.as.controller.registry.Resource;
import org.jboss.as.controller.security.InetAddressPrincipal;
import org.jboss.dmr.ModelNode;
import org.jboss.dmr.Property;
import org.wildfly.security.manager.WildFlySecurityManager;

/**
 * Base class for operation context implementations.
 *
 * @author <a href="mailto:david.lloyd@redhat.com">David M. Lloyd</a>
 */
@SuppressWarnings("deprecation")
abstract class AbstractOperationContext implements OperationContext {

    private static final Set<String> NON_COPIED_HEADERS =
            Collections.unmodifiableSet(new HashSet<String>(Arrays.asList(ALLOW_RESOURCE_SERVICE_RESTART,
                    ROLLBACK_ON_RUNTIME_FAILURE, ROLLOUT_PLAN)));

    static final ThreadLocal<Thread> controllingThread = new ThreadLocal<Thread>();

    /** Thread that initiated execution of the overall operation for which this context is the whole or a part */
    final Thread initiatingThread;
    private final EnumMap<Stage, Deque<Step>> steps;
    private final ModelController.OperationTransactionControl transactionControl;
    private final ControlledProcessState processState;
    private final boolean booting;
    private final ProcessType processType;
    private final RunningMode runningMode;
    private final Environment callEnvironment;
    // We only respect interruption on the way in; once we complete all steps
    // and begin
    // returning, any calls that can throw InterruptedException are converted to
    // an uninterruptible form. This is to ensure rollback changes are not
    // interrupted
    boolean respectInterruption = true;

    Stage currentStage = Stage.MODEL;

    ResultAction resultAction;
    /** Tracks whether we've detected cancellation */
    boolean cancelled;
    /** Currently executing step */
    Step activeStep;
    Caller caller;
    /** Whether operation execution has begun; i.e. whether completeStep() has been called */
    private boolean executing;

    /** Operations that were added by the controller, before execution started */
    private final List<ModelNode> controllerOperations = new ArrayList<ModelNode>(2);
    private boolean auditLogged;
    private final AuditLogger auditLogger;

    enum ContextFlag {
        ROLLBACK_ON_FAIL, ALLOW_RESOURCE_SERVICE_RESTART,
    }

    AbstractOperationContext(final ProcessType processType, final RunningMode runningMode,
                             final ModelController.OperationTransactionControl transactionControl,
                             final ControlledProcessState processState,
                             final boolean booting,
                             final AuditLogger auditLogger) {
        this.processType = processType;
        this.runningMode = runningMode;
        this.transactionControl = transactionControl;
        this.processState = processState;
        this.booting = booting;
        this.auditLogger = auditLogger;
        steps = new EnumMap<Stage, Deque<Step>>(Stage.class);
        for (Stage stage : Stage.values()) {
            if (booting && stage == Stage.VERIFY) {
                // Use a concurrent structure as the parallel boot threads will
                // concurrently add steps
                steps.put(stage, new LinkedBlockingDeque<Step>());
            } else {
                steps.put(stage, new ArrayDeque<Step>());
            }
        }
        initiatingThread = Thread.currentThread();
        this.callEnvironment = new Environment(processState, processType);
    }

    @Override
    public boolean isBooting() {
        return booting;
    }

    @Override
    public void addStep(final OperationStepHandler step, final Stage stage) throws IllegalArgumentException {
        addStep(step, stage, false);
    }

    @Override
    public void addStep(final ModelNode operation, final OperationStepHandler step, final Stage stage)
            throws IllegalArgumentException {
        final ModelNode response = activeStep == null ? new ModelNode().setEmptyObject() : activeStep.response;
        addStep(response, operation, null, step, stage);
    }

    @Override
    public void addStep(final ModelNode operation, final OperationStepHandler step, final Stage stage, final boolean addFirst)
            throws IllegalArgumentException {
        final ModelNode response = activeStep == null ? new ModelNode().setEmptyObject() : activeStep.response;
        addStep(response, operation, null, step, stage, addFirst);
    }

    @Override
    public void addStep(final ModelNode response, final ModelNode operation, final OperationStepHandler step, final Stage stage)
            throws IllegalArgumentException {
        addStep(response, operation, null, step, stage);
    }

    @Override
    public void addStep(OperationStepHandler step, Stage stage, boolean addFirst) throws IllegalArgumentException {
        final ModelNode response = activeStep == null ? new ModelNode().setEmptyObject() : activeStep.response;
        addStep(response, activeStep.operation, activeStep.address, step, stage, addFirst);
    }

    void addStep(final ModelNode response, final ModelNode operation, final PathAddress address, final OperationStepHandler step,
            final Stage stage) throws IllegalArgumentException {
        addStep(response, operation, address, step, stage, false);
    }

    @Override
    public void addStep(ModelNode response, ModelNode operation, OperationStepHandler step, Stage stage, boolean addFirst)
            throws IllegalArgumentException {
        addStep(response, operation, null, step, stage, addFirst);
    }

    void addStep(final ModelNode response, final ModelNode operation, final PathAddress address, final OperationStepHandler step,
            final Stage stage, boolean addFirst) throws IllegalArgumentException {
        assert isControllingThread();
        if (response == null) {
            throw MESSAGES.nullVar("response");
        }
        if (operation == null) {
            throw MESSAGES.nullVar("operation");
        }
        if (step == null) {
            throw MESSAGES.nullVar("step");
        }
        if (stage == null) {
            throw MESSAGES.nullVar("stage");
        }
        if (currentStage == Stage.DONE) {
            throw MESSAGES.operationAlreadyComplete();
        }
        if (stage.compareTo(currentStage) < 0) {
            throw MESSAGES.stageAlreadyComplete(stage);
        }
        if (stage == Stage.DOMAIN && processType != ProcessType.HOST_CONTROLLER) {
            throw MESSAGES.invalidStage(stage, processType);
        }
        if (stage == Stage.DONE) {
            throw MESSAGES.invalidStepStage();
        }
        if (!booting && activeStep != null) {
            // Added steps inherit the caller type of their parent
            if (activeStep.operation.hasDefined(OPERATION_HEADERS)) {
                ModelNode activeHeaders = activeStep.operation.get(OPERATION_HEADERS);
                for (Property property : activeHeaders.asPropertyList()) {
                    String key = property.getName();
                    if (!NON_COPIED_HEADERS.contains(key)) {
                        operation.get(OPERATION_HEADERS, key).set(property.getValue());
                    }
                }
            }
        }

        final Deque<Step> deque = steps.get(stage);
        if (addFirst) {
            deque.addFirst(new Step(step, response, operation, address));
        } else {
            deque.addLast(new Step(step, response, operation, address));
        }

        if (!executing) {
            recordControllerOperation(operation);
        }
    }

    @Override
    public final ModelNode getFailureDescription() {
        return activeStep.response.get(FAILURE_DESCRIPTION);
    }

    @Override
    public final boolean hasFailureDescription() {
        return activeStep.response.has(FAILURE_DESCRIPTION);
    }

    @Override
    public final ModelNode getResponseHeaders() {
        return activeStep.response.get(RESPONSE_HEADERS);
    }

    /**
     * Package-protected method used to initiate operation execution.
     * @return the result action
     */
    ResultAction executeOperation() {
        return completeStepInternal();
    }

    private ResultAction completeStepInternal() {
        try {
            doCompleteStep();
            if (resultAction == ResultAction.KEEP) {
                report(MessageSeverity.INFO, MESSAGES.operationSucceeded());
            } else {
                report(MessageSeverity.INFO, MESSAGES.operationRollingBack());
            }
            return resultAction;
        } finally {
            respectInterruption = false;
        }
    }

    @Override
    public final void completeStep(RollbackHandler rollbackHandler) {
        if (rollbackHandler == null) {
            throw MESSAGES.nullVar("rollbackHandler");
        }
        if (rollbackHandler == RollbackHandler.NOOP_ROLLBACK_HANDLER) {
            completeStep(ResultHandler.NOOP_RESULT_HANDLER);
        } else {
            completeStep(new RollbackDelegatingResultHandler(rollbackHandler));
        }
        // we return and executeStep picks it up
    }

    @Override
    public final void completeStep(ResultHandler resultHandler) {
        if (resultHandler == null) {
            throw MESSAGES.nullVar("resultHandler");
        }
        this.activeStep.resultHandler = resultHandler;
        // we return and executeStep picks it up
    }

    @Override
    public final void stepCompleted() {
        completeStep(ResultHandler.NOOP_RESULT_HANDLER);
    }

    /**
     * If appropriate for this implementation, block waiting for the
     * {@link ModelControllerImpl#acquireContainerMonitor()} method to return, ensuring that the controller's
     * MSC ServiceContainer is settled and it is safe to proceed to service status verification.
     *
     * @throws InterruptedException if the thread is interrupted while blocking
     */
    abstract void awaitModelControllerContainerMonitor() throws InterruptedException;

    /**
     * Create a persistence resource (if appropriate for this implementation) for use in persisting the configuration
     * model that results from this operation. If a resource is created, it should perform as much persistence work
     * as possible without modifying the official persistence store (i.e. the config file) in order to detect any
     * persistence issues.
     *
     * @return the persistence resource, or {@code null} if persistence is not supported
     *
     * @throws ConfigurationPersistenceException if there is a problem creating the persistence resource
     */
    abstract ConfigurationPersister.PersistenceResource createPersistenceResource() throws ConfigurationPersistenceException;

    /**
     * Release any locks held by the given step.
     *
     * @param step the step
     */
    abstract void releaseStepLocks(Step step);

    /**
     * Wait for completion of removal of any services removed by this context.
     *
     * @throws InterruptedException if the thread is interrupted while waiting
     */
    abstract void waitForRemovals() throws InterruptedException;

    /**
     * Gets whether any steps have taken actions that indicate a wish to write to the model or the service container.
     *
     * @return {@code true} if no
     */
    abstract boolean isReadOnly();

    /**
     * Gets whether the currently executing thread is allowed to control this operation context.
     *
     * @return {@code true} if the currently executing thread is allowed to control the context
     */
    boolean isControllingThread() {
        return Thread.currentThread() == initiatingThread || controllingThread.get() == initiatingThread;
    }

    /**
     * Log an audit record of this operation.
     */
    void logAuditRecord() {
        if (!auditLogged) {
            try {
                AccessAuditContext accessContext = SecurityActions.currentAccessAuditContext();
                Caller caller = getCaller();
                Subject subject = SecurityActions.getSubject(caller);
                auditLogger.log(
                        isReadOnly(),
                        resultAction,
                        caller == null ? null : caller.getName(),
                        accessContext == null ? null : accessContext.getDomainUuid(),
                        accessContext == null ? null : accessContext.getAccessMechanism(),
                        getSubjectInetAddress(subject),
                        getModel(),
                        controllerOperations);
                auditLogged = true;
            } catch (Exception e) {
                ControllerLogger.MGMT_OP_LOGGER.failedToUpdateAuditLog(e);
            }
        }
    }

    private InetAddress getSubjectInetAddress(Subject subject) {
        InetAddressPrincipal principal = getPrincipal(subject, InetAddressPrincipal.class);
        return principal != null ? principal.getInetAddress() : null;
    }


    private <T extends Principal> T getPrincipal(Subject subject, Class<T> clazz) {
        if (subject == null) {
            return null;
        }
        Set<T> principals = subject.getPrincipals(clazz);
        assert principals.size() <= 1;
        if (principals.size() == 0) {
            return null;
        }
        return principals.iterator().next();
    }

    /**
     * Record an operation added before execution began (i.e. added by the controller and not by a step)
     * @param operation the operation
     */
    private void recordControllerOperation(ModelNode operation) {
        controllerOperations.add(operation);
    }

    abstract Resource getModel();

    /**
     * Perform the work of completing a step.
     */
    private void doCompleteStep() {

        assert isControllingThread();
        // If someone called this when the operation is done, fail.
        if (currentStage == null) {
            throw MESSAGES.operationAlreadyComplete();
        }

        /** Execution has begun */
        executing = true;

        // If previous steps have put us in a state where we shouldn't do any more, just stop
        if (!canContinueProcessing()) {
            respectInterruption = false;
            logAuditRecord();
            return;
        }

        // Locate the next step to execute.
        ModelNode response = activeStep == null ? null : activeStep.response;
        Step step;
        do {
            step = steps.get(currentStage).pollFirst();
            if (step == null) {
                // No steps remain in this stage; proceed to the next stage.
                if (currentStage.hasNext()) {
                    currentStage = currentStage.next();
                    if (currentStage == Stage.VERIFY) {
                        // a change was made to the runtime. Thus, we must wait
                        // for stability before resuming in to verify.
                        try {
                            awaitModelControllerContainerMonitor();
                        } catch (InterruptedException e) {
                            cancelled = true;
                            if (response != null) {
                                response.get(OUTCOME).set(CANCELLED);
                                response.get(FAILURE_DESCRIPTION).set(MESSAGES.operationCancelled());
                                response.get(ROLLED_BACK).set(true);
                            }
                            resultAction = ResultAction.ROLLBACK;
                            respectInterruption = false;
                            logAuditRecord();
                            Thread.currentThread().interrupt();
                            if (activeStep != null && activeStep.resultHandler != null) {
                                // Finalize
                                activeStep.finalizeStep(null);
                            }
                            return;
                        }
                    }
                }
            } else {
                // Execute the step, but make sure we always finalize any steps
                Throwable toThrow = null;
                // Whether to return after try/finally
                boolean exit = false;
                try {
                    executeStep(step);
                } catch (RuntimeException re) {
                    toThrow = re;
                } catch (Error e) {
                    toThrow = e;
                } finally {
                    if (step.resultHandler == null) {
                        // A recursive step executed
                        throwThrowable(toThrow);
                        exit = true; // we're on the return path
                    } else {
                        // A non-recursive step executed
                        // See if it put us in a state where we shouldn't do any
                        // more
                        if (!canContinueProcessing()) {
                            // We're done. Do the cleanup that would happen in
                            // executeStep's finally block
                            // if this was a recursive step
                            respectInterruption = false;
                            logAuditRecord();
                            step.finalizeStep(toThrow);
                            exit = true; // we're on the return path
                        } else {
                            throwThrowable(toThrow);
                            // else move on to next step
                            response = activeStep.response;
                        }
                    }
                }
                if (exit) {
                    return;
                }
            }
        } while (currentStage != Stage.DONE);

        // All steps are completed without triggering rollback; time for final
        // processing

        // Prepare persistence of any configuration changes
        ConfigurationPersister.PersistenceResource persistenceResource = null;
        if (isModelAffected() && resultAction != ResultAction.ROLLBACK) {
            try {
                persistenceResource = createPersistenceResource();
            } catch (ConfigurationPersistenceException e) {
                MGMT_OP_LOGGER.failedToPersistConfigurationChange(e);
                if (response != null) {
                    response.get(OUTCOME).set(FAILED);
                    response.get(FAILURE_DESCRIPTION).set(MESSAGES.failedToPersistConfigurationChange(e.getLocalizedMessage()));
                }
                resultAction = ResultAction.ROLLBACK;
                logAuditRecord();
                return;
            }
        }

        // Allow any containing TransactionControl to vote
        final AtomicReference<ResultAction> ref = new AtomicReference<ResultAction>(
                transactionControl == null ? ResultAction.KEEP : ResultAction.ROLLBACK);
        if (transactionControl != null) {
            if (MGMT_OP_LOGGER.isTraceEnabled()) {
                MGMT_OP_LOGGER.trace("Prepared response is " + response);
            }
            transactionControl.operationPrepared(new ModelController.OperationTransaction() {
                public void commit() {
                    ref.set(ResultAction.KEEP);
                }

                public void rollback() {
                    ref.set(ResultAction.ROLLBACK);
                }
            }, response);
        }
        resultAction = ref.get();

        // Commit the persistence of any configuration changes
        if (persistenceResource != null) {
            if (resultAction == ResultAction.ROLLBACK) {
                persistenceResource.rollback();
            } else {
                persistenceResource.commit();
            }
        }

        logAuditRecord();
    }

    private boolean canContinueProcessing() {

        // Cancellation is detected via interruption.
        if (Thread.currentThread().isInterrupted()) {
            cancelled = true;
        }
        // Rollback when any of:
        // 1. operation is cancelled
        // 2. operation failed in model phase
        // 3. operation failed in runtime/verify and rollback_on_fail is set
        // 4. isRollbackOnly
        if (cancelled) {
            if (activeStep != null) {
                activeStep.response.get(OUTCOME).set(CANCELLED);
                activeStep.response.get(FAILURE_DESCRIPTION).set(MESSAGES.operationCancelled());
                activeStep.response.get(ROLLED_BACK).set(true);
            }
            resultAction = ResultAction.ROLLBACK;
        } else if (activeStep != null && activeStep.response.hasDefined(FAILURE_DESCRIPTION)
                && (isRollbackOnRuntimeFailure() || currentStage == Stage.MODEL)) {
            activeStep.response.get(OUTCOME).set(FAILED);
            activeStep.response.get(ROLLED_BACK).set(true);
            resultAction = ResultAction.ROLLBACK;
        }
        return resultAction != ResultAction.ROLLBACK;
    }

    @SuppressWarnings("ConstantConditions")
    private void executeStep(final Step step) {
        step.predecessor = this.activeStep;
        this.activeStep = step;

        try {
            try {
                ClassLoader oldTccl = WildFlySecurityManager.setCurrentContextClassLoaderPrivileged(step.handler.getClass());
                try {
                    step.handler.execute(this, step.operation);
                    // AS7-6046
                    if (isErrorLoggingNecessary() && step.response.hasDefined(FAILURE_DESCRIPTION)) {
                        MGMT_OP_LOGGER.operationFailed(step.operation.get(OP), step.operation.get(OP_ADDR),
                                step.response.get(FAILURE_DESCRIPTION));
                    }
                } finally {
                    WildFlySecurityManager.setCurrentContextClassLoaderPrivileged(oldTccl);
                }

            } catch (Throwable t) {
                // Special handling for OperationClientException marker
                // interface
                if (!(t instanceof OperationClientException)) {
                    logAuditRecord();
                    throw t;
                } else if (currentStage != Stage.DONE) {
                    // Handler threw OCE before calling completeStep(); that's
                    // equivalent to
                    // a request that we set the failure description and call
                    // completeStep()
                    final ModelNode failDesc = OperationClientException.class.cast(t).getFailureDescription();
                    step.response.get(FAILURE_DESCRIPTION).set(failDesc);
                    if (isErrorLoggingNecessary()) {
                        MGMT_OP_LOGGER.operationFailed(step.operation.get(OP), step.operation.get(OP_ADDR),
                                step.response.get(FAILURE_DESCRIPTION));
                    } else {
                        // A client-side mistake post-boot that only affects model, not runtime, is logged at DEBUG
                        MGMT_OP_LOGGER.operationFailedOnClientError(step.operation.get(OP), step.operation.get(OP_ADDR),
                                step.response.get(FAILURE_DESCRIPTION));
                    }
                    completeStepInternal();
                } else {
                    // Handler threw OCE after calling completeStep()
                    // Throw it on and let standard error handling deal with it
                    throw t;
                }
            }
        } catch (Throwable t) {
            if (t instanceof StackOverflowError) {
                // This can happen with an operation with many, many steps that use the recursive no-arg completeStep()
                // variant. This should be rare now as handlers are largely migrated from this variant.
                // But, log a special message for this case
                MGMT_OP_LOGGER.operationFailed(t, step.operation.get(OP), step.operation.get(OP_ADDR),
                        AbstractControllerService.BOOT_STACK_SIZE_PROPERTY, AbstractControllerService.DEFAULT_BOOT_STACK_SIZE);
            } else {
                MGMT_OP_LOGGER.operationFailed(t, step.operation.get(OP), step.operation.get(OP_ADDR));
            }
            // If this block is entered, then the step failed
            // The question is, did it fail before or after calling
            // completeStep()?
            if (currentStage != Stage.DONE) {
                // It failed before, so consider the operation a failure.
                if (!step.response.hasDefined(FAILURE_DESCRIPTION)) {
                    step.response.get(FAILURE_DESCRIPTION).set(MESSAGES.operationHandlerFailed(t.getLocalizedMessage()));
                }
                step.response.get(OUTCOME).set(FAILED);
                resultAction = getFailedResultAction(t);
                if (resultAction == ResultAction.ROLLBACK) {
                    step.response.get(ROLLED_BACK).set(true);
                }
            } else {
                // It failed after! Just return, ignore the failure
                report(MessageSeverity.WARN, MESSAGES.stepHandlerFailed(step.handler));
            }
        } finally {
            // Make sure non-recursive steps finalize
            finishStep(step);
        }
    }

    /** Whether ERROR level logging is appropriate for any operation failures*/
    private boolean isErrorLoggingNecessary() {
        // Log for any boot failure or for any failure that may affect this processes' runtime services.
        // Post-boot MODEL failures aren't ERROR logged as they have no impact outside the scope of
        // the soon-to-be-abandoned OperationContext.
        // TODO consider logging Stage.DOMAIN problems if it's clear the message will be comprehensible.
        // Currently Stage.DOMAIN failure handling involves message manipulation before sending the
        // failure data to the client; logging stuff before that is done is liable to just produce a log mess.
        return isBooting() || currentStage == Stage.RUNTIME || currentStage == Stage.VERIFY;
    }

    private void finishStep(Step step) {
        boolean finalize = true;
        Throwable toThrow = null;
        try {
            if (step.resultHandler != null) {
                // A non-recursive step executed
                if (!hasMoreSteps()) {
                    // this step was the last registered step;
                    // go ahead and shift back into recursive mode to wrap
                    // things up
                    completeStepInternal();
                } else {
                    // Let doCompleteStep carry on with subsequent steps.
                    // If this step has failed in a way that will prevent
                    // subsequent steps running,
                    // and doCompleteStep will finalize this step.
                    // Otherwise, some subsequent step will finalize this step
                    finalize = false;
                }
            }
        } catch (RuntimeException re) {
            toThrow = re;
        } catch (Error e) {
            toThrow = e;
        } finally {
            if (finalize) {
                // We're on the way out on the recursive call path. Finish off
                // this step.
                // Any throwable we caught will get rethrown by this call
                step.finalizeStep(toThrow);
            } else {
                // non-recursive steps get finished off by the succeeding
                // recursive step
                // Throw on toThrow if it's not null; otherwise just return
                throwThrowable(toThrow);
            }
        }
    }

    private static void throwThrowable(Throwable toThrow) {
        if (toThrow != null) {
            if (toThrow instanceof RuntimeException) {
                throw (RuntimeException) toThrow;
            } else {
                throw (Error) toThrow;
            }
        }
    }

    /**
     * Decide whether failure should trigger a rollback.
     *
     * @param cause
     *            the cause of the failure, or {@code null} if failure is not
     *            the result of catching a throwable
     * @return the result action
     */
    private ResultAction getFailedResultAction(Throwable cause) {
        if (currentStage == Stage.MODEL || cancelled || isRollbackOnRuntimeFailure() || isRollbackOnly()
 (cause != null && !(cause instanceof OperationFailedException))) {
            return ResultAction.ROLLBACK;
        }
        return ResultAction.KEEP;
    }

    public final ProcessType getProcessType() {
        return processType;
    }

    public final RunningMode getRunningMode() {
        return runningMode;
    }

    @Override
    public final boolean isNormalServer() {
        return processType.isServer() && runningMode == RunningMode.NORMAL;
    }

    @Override
    public final boolean isRollbackOnly() {
        return resultAction == ResultAction.ROLLBACK;
    }

    @Override
    public final void setRollbackOnly() {
        resultAction = ResultAction.ROLLBACK;
    }

    final boolean isRollingBack() {
        return currentStage == Stage.DONE && resultAction == ResultAction.ROLLBACK;
    }

    @Override
    public final void reloadRequired() {
        if (processState.isReloadSupported()) {
            activeStep.restartStamp = processState.setReloadRequired();
            activeStep.response.get(RESPONSE_HEADERS, OPERATION_REQUIRES_RELOAD).set(true);
        } else {
            restartRequired();
        }
    }

    @Override
    public final void restartRequired() {
        activeStep.restartStamp = processState.setRestartRequired();
        activeStep.response.get(RESPONSE_HEADERS, OPERATION_REQUIRES_RESTART).set(true);
    }

    @Override
    public final void revertReloadRequired() {
        if (processState.isReloadSupported()) {
            processState.revertReloadRequired(this.activeStep.restartStamp);
            if (activeStep.response.get(RESPONSE_HEADERS).hasDefined(OPERATION_REQUIRES_RELOAD)) {
                activeStep.response.get(RESPONSE_HEADERS).remove(OPERATION_REQUIRES_RELOAD);
                if (activeStep.response.get(RESPONSE_HEADERS).asInt() == 0) {
                    activeStep.response.remove(RESPONSE_HEADERS);
                }
            }
        } else {
            revertRestartRequired();
        }
    }

    @Override
    public final void revertRestartRequired() {
        processState.revertRestartRequired(this.activeStep.restartStamp);
        if (activeStep.response.get(RESPONSE_HEADERS).hasDefined(OPERATION_REQUIRES_RESTART)) {
            activeStep.response.get(RESPONSE_HEADERS).remove(OPERATION_REQUIRES_RESTART);
            if (activeStep.response.get(RESPONSE_HEADERS).asInt() == 0) {
                activeStep.response.remove(RESPONSE_HEADERS);
            }
        }
    }

    @Override
    public final void runtimeUpdateSkipped() {
        activeStep.response.get(RESPONSE_HEADERS, RUNTIME_UPDATE_SKIPPED).set(true);
    }

    @Override
    public final ModelNode getResult() {
        return activeStep.response.get(RESULT);
    }

    @Override
    public final boolean hasResult() {
        return activeStep.response.has(RESULT);
    }

    @Override
    public final ModelNode getServerResults() {
        if (processType != ProcessType.HOST_CONTROLLER) {
            throw MESSAGES.serverResultsAccessNotAllowed(ProcessType.HOST_CONTROLLER, processType);
        }
        return activeStep.response.get(SERVER_GROUPS);
    }

    private boolean hasMoreSteps() {
        Stage stage = currentStage;
        boolean more = !steps.get(stage).isEmpty();
        while (!more && stage.hasNext()) {
            stage = stage.next();
            more = !steps.get(stage).isEmpty();
        }
        return more;
    }

    @Override
    public Caller getCaller() {
        // TODO Consider threading but in general no harm in multiple instances being created rather than adding synchronization.
        Caller response = SecurityActions.getCaller(caller); // This allows for a change of Subject whilst the same OperationContext is in use.
        caller = response;

        return response;
    }

    @Override
    public Environment getCallEnvironment() {
        return callEnvironment;
    }

    class Step {
        private final OperationStepHandler handler;
        final ModelNode response;
        final ModelNode operation;
        final PathAddress address;
        final OperationId operationId;
        private Object restartStamp;
        private ResultHandler resultHandler;
        Step predecessor;

        private Step(final OperationStepHandler handler, final ModelNode response, final ModelNode operation,
                final PathAddress address) {
            this.handler = handler;
            this.response = response;
            this.operation = operation;
            this.address = address == null ? PathAddress.pathAddress(operation.get(OP_ADDR)) : address;
            String opName = operation.hasDefined(OP) ? operation.require(OP).asString() : null;
            this.operationId = new OperationId(this.address, opName);
            // Create the outcome node early so it appears at the top of the
            // response
            response.get(OUTCOME);
        }

        /**
         * Perform any rollback needed to reverse this step (if this context is
         * rolling back), and release any locks taken by this step.
         *
         * @param toThrow
         *            RuntimeException or Error to throw when done; may be
         *            {@code null}
         */
        private void finalizeStep(Throwable toThrow) {
            try {
                finalizeInternal();
            } catch (RuntimeException t) {
                if (toThrow == null) {
                    toThrow = t;
                }
            } catch (Error t) {
                if (toThrow == null) {
                    toThrow = t;
                }
            }

            Step step = this.predecessor;
            while (step != null) {
                if (step.resultHandler != null) {
                    try {
                        step.finalizeInternal();
                    } catch (RuntimeException t) {
                        if (toThrow == null) {
                            toThrow = t;
                        }
                    } catch (Error t) {
                        if (toThrow == null) {
                            toThrow = t;
                        }
                    }
                    step = step.predecessor;
                } else {
                    AbstractOperationContext.this.activeStep = step;
                    break;
                }
            }

            throwThrowable(toThrow);
        }

        private void finalizeInternal() {

            AbstractOperationContext.this.activeStep = this;

            try {
                handleRollback();

                if (currentStage != null && currentStage != Stage.DONE) {
                    // This is a failure because the next step failed to call
                    // completeStep().
                    // Either an exception occurred beforehand, or the
                    // implementer screwed up.
                    // If an exception occurred, then this will have no effect.
                    // If the implementer screwed up, then we're essentially
                    // fixing the context state and treating
                    // the overall operation as a failure.
                    currentStage = Stage.DONE;
                    if (!response.hasDefined(FAILURE_DESCRIPTION)) {
                        response.get(FAILURE_DESCRIPTION).set(MESSAGES.operationHandlerFailedToComplete());
                    }
                    response.get(OUTCOME).set(cancelled ? CANCELLED : FAILED);
                    response.get(ROLLED_BACK).set(true);
                    resultAction = getFailedResultAction(null);
                } else if (resultAction == ResultAction.ROLLBACK) {
                    response.get(OUTCOME).set(cancelled ? CANCELLED : FAILED);
                    response.get(ROLLED_BACK).set(true);
                } else {
                    response.get(OUTCOME).set(response.hasDefined(FAILURE_DESCRIPTION) ? FAILED : SUCCESS);
                }

            } finally {
                releaseStepLocks(this);

                if (predecessor == null) {
                    // We're returning from the outermost completeStep()
                    // Null out the current stage to disallow further access to
                    // the context
                    currentStage = null;
                }
            }
        }

        private void handleRollback() {
            if (resultHandler != null) {
                try {
                    ClassLoader oldTccl = WildFlySecurityManager.setCurrentContextClassLoaderPrivileged(handler.getClass());
                    try {
                        resultHandler.handleResult(resultAction, AbstractOperationContext.this, operation);
                    } finally {
                        WildFlySecurityManager.setCurrentContextClassLoaderPrivileged(oldTccl);
                        waitForRemovals();
                    }
                } catch (Exception e) {
                    report(MessageSeverity.ERROR,
                            MESSAGES.stepHandlerFailedRollback(handler, operation.asString(), address, e));
                } finally {
                    // Clear the result handler so we never try and finalize
                    // this step again
                    resultHandler = null;
                }
            }
        }

    }

    private static class RollbackDelegatingResultHandler implements ResultHandler {

        private final RollbackHandler delegate;

        private RollbackDelegatingResultHandler(RollbackHandler delegate) {
            this.delegate = delegate;
        }


        @Override
        public void handleResult(ResultAction resultAction, OperationContext context, ModelNode operation) {
            if (resultAction == ResultAction.ROLLBACK) {
                delegate.handleRollback(context, operation);
            }
        }
    }

    static class OperationId {
        final PathAddress address;
        final String name;

        OperationId(ModelNode operation) {
            this(PathAddress.pathAddress(operation.get(OP_ADDR)), operation.hasDefined(OP) ? operation.get(OP).asString() : null);
        }

        private OperationId(PathAddress address, String name) {
            this.address = address;
            this.name = name;
        }

        @Override
        public boolean equals(Object o) {
            if (this == o) return true;
            if (o == null || getClass() != o.getClass()) return false;

            OperationId that = (OperationId) o;

            return address.equals(that.address) && !(name != null ? !name.equals(that.name) : that.name != null);

        }

        @Override
        public int hashCode() {
            int result = address.hashCode();
            result = 31 * result + (name != null ? name.hashCode() : 0);
            return result;
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

QDox parse error in file://<WORKSPACE>/data/code-rep-dataset/Dataset3/Tasks/6732.java