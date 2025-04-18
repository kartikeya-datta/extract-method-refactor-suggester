error id: file://<WORKSPACE>/data/code-rep-dataset/Dataset5/Tasks/13595.java
file://<WORKSPACE>/data/code-rep-dataset/Dataset5/Tasks/13595.java
### com.thoughtworks.qdox.parser.ParseException: syntax error @[1,1]

error in qdox parser
file content:
```java
offset: 1
uri: file://<WORKSPACE>/data/code-rep-dataset/Dataset5/Tasks/13595.java
text:
```scala
.@@getDelegate()).detachCopy(employee);

/*
 * Licensed to the Apache Software Foundation (ASF) under one
 * or more contributor license agreements.  See the NOTICE file
 * distributed with this work for additional information
 * regarding copyright ownership.  The ASF licenses this file
 * to you under the Apache License, Version 2.0 (the
 * "License"); you may not use this file except in compliance
 * with the License.  You may obtain a copy of the License at
 *
 * http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing,
 * software distributed under the License is distributed on an
 * "AS IS" BASIS, WITHOUT WARRANTIES OR CONDITIONS OF ANY
 * KIND, either express or implied.  See the License for the
 * specific language governing permissions and limitations
 * under the License.
 */
package org.apache.openjpa.persistence.lockmgr;

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.io.PrintStream;
import java.io.PrintWriter;
import java.io.StringWriter;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.persistence.EntityManager;
import javax.persistence.LockModeType;

import org.apache.openjpa.lib.log.Log;
import org.apache.openjpa.persistence.OpenJPAEntityManager;
import org.apache.openjpa.persistence.test.SQLListenerTestCase;

/**
 * Base class to support pseudo programmable pre-define JPA related
 * actions which is driven by action table define by a test case. E.g.
 * <xmp>
 * Object[][] actions = {
 *   { Act.Actions [, action_specific_parameters]* }
 * };
 * </xmp>
 * See enum type <code>Act</code> for list of valid actions.
 * <p>The action objects are passed as argument to the launchActionSequence()
 * method. The actions in the sequence table are invoked in the order
 * specified. Each action sequence table in the argument list are launched in
 * a separate thread.
 *
 * @author Albert Lee
 * @since 2.0
 */
public abstract class SequencedActionsTest extends SQLListenerTestCase {

    protected static final String Default_FirstName = "Def FirstName";
    protected static final Class<?>[] ExpectingOptimisticLockExClass =
        new Class<?>[]
            { javax.persistence.OptimisticLockException.class };
    protected static final Class<?>[] ExpectingPessimisticLockExClass =
        new Class<?>[]
            { javax.persistence.PessimisticLockException.class };
    protected static final Class<?>[] ExpectingLockTimeoutExClass =
        new Class<?>[]
            { javax.persistence.LockTimeoutException.class };
    protected static final Class<?>[] ExpectingAnyLockExClass =
        new Class<?>[] {
            javax.persistence.PessimisticLockException.class,
            javax.persistence.LockTimeoutException.class };

    protected static final int MinThreadWaitInMs = 45000;

    private long waitInMsec = -1;
    private String empTableName;
    private List<TestThread> threads = null;

    protected void commonSetUp() {
        empTableName = getMapping(LockEmployee.class).getTable().getFullName();

        cleanupDB();

        LockEmployee e1, e2, e3;
        e1 = newEmployee(1);
        e2 = newEmployee(2);
        e3 = newEmployee(3);

        resetSQL();
        EntityManager em = null;
        try {
            em = emf.createEntityManager();
            em.getTransaction().begin();
            em.persist(e1);
            em.persist(e2);
            em.persist(e3);
            em.getTransaction().commit();
        } finally {
            if (em != null && em.isOpen()) {
                em.close();
            }
        }
        assertAllSQLInOrder(
            "INSERT INTO " + empTableName + " .*",
            "INSERT INTO " + empTableName + " .*",
            "INSERT INTO " + empTableName + " .*");

        // dynamic runtime test to determine wait time.
        long speedCnt = -1;
        if (waitInMsec == -1) {
            speedCnt = platformSpeedTest();
            try {
                waitInMsec = MinThreadWaitInMs + 500000 / (speedCnt / 1000000);
            } catch (Throwable t) {
            }
        }
        if (waitInMsec <= 0) {
            waitInMsec = MinThreadWaitInMs; // set to default
        }
        getLog().trace(
            "**** Speed Cont=" + speedCnt + ", waitTime(ms)=" + waitInMsec);
    }

    private long platformSpeedTest() {
        PlatformSpeedTestThread speedThread = new PlatformSpeedTestThread();
        speedThread.start();
        try {
            Thread.sleep(1000);
        } catch (InterruptedException e) {
            logStack(e);
        }
        speedThread.interrupt();
        try {
            Thread.sleep(50);
        } catch (InterruptedException e) {
            logStack(e);
        }
        return speedThread.getLoopCnt();
    }

    private void cleanupDB() {
        EntityManager em = null;
        try {
            em = emf.createEntityManager();
            em.getTransaction().begin();

            em.createQuery("delete from " + empTableName).executeUpdate();

            em.getTransaction().commit();
        } finally {
            if (em != null && em.isOpen()) {
                em.close();
            }
        }
    }

    private LockEmployee newEmployee(int id) {
        LockEmployee e = new LockEmployee();
        e.setId(id);
        return e;
    }

    protected Log getLog() {
        return emf.getConfiguration().getLog("Tests");
    }

    protected Log getDumpStackLog() {
        return emf.getConfiguration().getLog("DumpStack");
    }

    protected void logStack(Throwable t) {
        StringWriter str = new StringWriter();
        PrintWriter print = new PrintWriter(str);
        t.printStackTrace(print);
        getDumpStackLog().trace(str.toString());
    }

    private void notifyParent() {
        getLog().trace("notifyParent:");
        synchronized (this) {
            notify();
        }
    }

    // List of support actions and parameter definition.
    protected enum Act {
        // JPA entity manager API
        CreateEm,          // ()
        CloseEm,           // ()
        Find,              // ([int id])
        FindWithLock,      // (int id, LockModeType lockType,
                           //      [String properties, Object value]*)
        FindObject,        // (Object obj, LockModeType lockType)
        Refresh,           // ([int id])
        RefreshWithLock,   // (int id, LockModeType lockType,
                           //      [String properties, Object value]*)
        RefreshObject,     // (Object obj, LockModeType lockType)
        Lock,              // (int id, LockModeType lockType,
                           //      [String properties, Object value]*)
        LockObject,        // (Object obj, LockModeType lockType)
        Persist,           // (int id, String firstName)
        Clear,             // ()
        Flush,             // ()
        Remove,            // ([int id])

        UpdateEmployee,    // ([int id[, String newFirstName]])

        // OpenJPA entity manager API
        Detach,            // ();

        // Transaction API
        StartTx,           // ()
        CommitTx,          // ()
        RollbackTx,        // ()

        // Thread management functions
        NewThread,         // (int thread)
        StartThread,       // (int thread)
        Notify,            // (int thread, [int sleepTimeMs)] )
        Wait,              // (int thread, [int sleepTimeMs)] )

        WaitAllChildren,   // ()
        JoinParent,        // ()
        YieldThread,       // ()

        // Utility functions
        Sleep,             // (int sleepTimeMs)
        Info,              // (Object msg)
        Trace,             // (Object msg)
        Error,             // (Object msg)
        Warn,              // (Object msg)
        DetachSerialize,   // (int id, int idx)

        // Test and assertion functions
        TestException,     // (int thread, [Class exceptions]*)
        ResetException,    // ()
        TestEmployee,      // (int id, String FirstName, int versionInc)
        EmployeeNotNull,   // (int id)
        SaveVersion,       // ([int id])
        TestVersion,       // (int id, int increment)
        TestLockMode,      // (int id, LockModeType lockMode)

        Test,              // Open-ended testing actions
    };

    public void launchActionSequence(String testName, Object parameters,
        Object[][]... actions) {
        Log log = getLog();
        log.trace("============/// " + testName + " ///============");
        if (parameters != null) {
            if (parameters instanceof String[]) {
                for (String parameter : (String[]) parameters) {
                    log.trace("---> " + parameter);
                }
            } else if (parameters instanceof String) {
                log.trace("---> " + (String) parameters);
            }
        }

        EntityManager em = null;
        try {
            em = emf.createEntityManager();
            em.getTransaction().begin();
            LockEmployee ei = em.find(LockEmployee.class, 1);
            assertNotNull(ei);
            ei.setFirstName(Default_FirstName);
            em.getTransaction().commit();
        } catch (Exception ex) {
            logStack(ex);
            Throwable rootCause = ex.getCause();
            String failStr = "Unable to pre-initialize FirstName to known "
                + "value:" + ex.getClass().getName() + ":" + ex;
            if (rootCause != null) {
                failStr += "\n        -- Cause --> "
                    + rootCause.getClass().getName() + ":" + rootCause;
            }
            fail(failStr);
        } finally {
            if (em != null && em.isOpen()) {
                if (em.getTransaction().isActive()) {
                    if (em.getTransaction().getRollbackOnly()) {
                        log.trace("finally: rolledback");
                        em.getTransaction().rollback();
                        log.trace("finally: rolledback completed");
                    } else {
                        log.trace("finally: commit");
                        em.getTransaction().commit();
                        log.trace("finally: commit completed");
                    }
                }
                em.close();
            }
        }
        int numThreads = actions.length;
        threads = new ArrayList<TestThread>(numThreads - 1);
        TestThread mainThread = new TestThread(0, actions);
        threads.add(mainThread);
        launchCommonSequence(mainThread);
    }

    private void launchCommonSequence(TestThread thisThread ) {
        int threadToRun = thisThread.threadToRun;
        Object[][][] actions = thisThread.actions;
        Map<Integer,LockEmployee> employees = thisThread.employees;

        assertNotNull("Test sequence table must be defined", actions);
        assert (actions.length >= 1);

        int numThreads = actions.length;
        int saveVersion = -1;

        Log log = getLog();
        log.trace(">>>> Sequenced Test: Threads=" + threadToRun + '/'
            + numThreads);
        long endTime = System.currentTimeMillis() + waitInMsec;

        EntityManager em = null;
        Integer id = 1;
        LockEmployee employee = null;
        LockModeType lockMode = null;
        Act curAction = null;
        Object[][] threadSequence = actions[threadToRun];
        for (Object[] args : threadSequence) {
            curAction = (Act) args[0];
            log.trace("** Act=" + Arrays.toString(args));
            try {
                    switch (curAction) {
                    case CreateEm:
                        em = emf.createEntityManager();
                        break;
                    case CloseEm:
                        if (em != null && em.isOpen()) {
                            em.close();
                            em = null;
                        }
                        break;
                    case Clear:
                        em.clear();
                        break;
                    case Flush:
                        em.flush();
                        break;
                    case Find:
                        id = 1;
                        if (args.length > 1) {
                            id = (Integer)args[1];
                        }
                        employee = em.find(LockEmployee.class, id);
                        log.trace("Employee=" + employee);
                        if( employee != null ) {
                            employees.put(id, employee);
                        } else {
                            employees.remove(id);
                        }
                        break;
                    case FindWithLock:
                        id = 1;
                        if (args[1] != null) {
                            id = (Integer)args[1];
                        }
                        lockMode = LockModeType.NONE;
                        if (args[2] != null) {
                            lockMode = (LockModeType)args[2];
                        }
                        Map<String, Object> findProps = buildPropsMap(args, 3);
                        if (findProps != null) {
                            employee = em.find(LockEmployee.class, id,
                                lockMode, findProps);
                        } else {
                            employee = em
                                .find(LockEmployee.class, id, lockMode);
                        }
                        log.trace("Employee=" + employee);
                        if( employee != null ) {
                            employees.put(id, employee);
                        } else {
                            employees.remove(id);
                        }
                        break;
                    case FindObject:
                        em.find((Class<?>)args[1], args[2],
                            (LockModeType) args[3]);
                        // log.trace("Employee=" + employee);
                        break;
                    case Persist:
                        id = 1;
                        if (args[1] != null) {
                            id = (Integer)args[1];
                        }
                        String firstName = (String)args[2];
                        employee = new LockEmployee();
                        employee.setId(id);
                        employee.setFirstName(firstName);
                        log.trace("Employee=" + employee);
                        em.persist(employee);
                        break;
                    case Remove:
                        id = 1;
                        if (args.length > 1) {
                            id = (Integer)args[1];
                        }
                        employee = employees.get(id);
                        log.trace("Employee=" + employee);
                        em.remove(employee);
                        break;
                    case Refresh:
                        id = 1;
                        if (args.length > 1) {
                            id = (Integer)args[1];
                        }
                        employee = employees.get(id);
                        log.trace("Employee(before)=" + employee);
                        em.refresh(employee);
                        log.trace("Employee(after) =" + employee);
                        break;
                    case RefreshWithLock:
                        id = 1;
                        if (args[1] != null) {
                            id = (Integer)args[1];
                        }
                        lockMode = LockModeType.NONE;
                        if (args[2] != null) {
                            lockMode = (LockModeType)args[2];
                        }
                        employee = employees.get(id);
                        log.trace("Employee(before)=" + employee);
                        Map<String, Object> refreshProps = buildPropsMap(args,
                            3);
                        if (refreshProps != null) {
                            em.refresh(employee, lockMode, refreshProps);
                        } else {
                            em.refresh(employee, lockMode);
                        }
                        log.trace("Employee(after) =" + employee);
                        break;
                    case RefreshObject:
                        em.refresh(args[1], (LockModeType) args[2]);
                        break;
                    case Lock:
                        id = 1;
                        if (args[1] != null) {
                            id = (Integer)args[1];
                        }
                        lockMode = LockModeType.NONE;
                        if (args[2] != null) {
                            lockMode = (LockModeType)args[2];
                        }
                        employee = employees.get(id);
                        log.trace("Employee=" + employee);
                        Map<String, Object> lockProps = buildPropsMap(args, 3);
                        if (lockProps != null) {
                            em.lock(employee, lockMode, lockProps);
                        } else {
                            em.lock(employee, lockMode);
                        }
                        break;
                    case LockObject:
                        em.lock(args[1], (LockModeType) args[2]);
                        break;
                    case UpdateEmployee:
                        id = 1;
                        if (args.length > 1) {
                            id = (Integer) args[1];
                        }
                        employee = employees.get(id);
                        log.trace("Employee (before):" + employee);
                        String newFirstName = "Unknown";
                        if (args.length > 2) {
                            newFirstName = (String) args[2];
                        } else {
                            newFirstName = (new Date()).toString();
                        }
                        employee.setFirstName(newFirstName);
                        log.trace("Employee (after) :" + employee);
                        break;

                    case Detach:
                        id = 1;
                        if (args[1] != null) {
                            id = (Integer)args[1];
                        }
                        employee = employees.get(id);
                        log.trace("Employee (before) :" + employee);
                        LockEmployee detEmployee = ((OpenJPAEntityManager) em
                            .getDelegate()).detach(employee);
                        employees.put((Integer)args[2], detEmployee);
                        log.trace("Employee (after)  :" + detEmployee);
                        break;

                    case StartTx:
                        em.getTransaction().begin();
                        break;
                    case CommitTx:
                        em.getTransaction().commit();
                        break;
                    case RollbackTx:
                        em.getTransaction().rollback();
                        break;

                    case NewThread:
                        int childToRun = (Integer) args[1];
                        TestThread t1 = new TestThread(childToRun, actions);
                        threads.add(t1);
                        break;
                    case StartThread:
                        threads.get((Integer) args[1]).start();
                        break;
                    case Notify:
                        int notifyThreadid = 0;
                        if (args.length > 1 && args[1] != null) {
                            notifyThreadid = (Integer) args[1];
                        }
                        if (args.length > 2) {
                            Thread.sleep((Integer) args[2]);
                        }
                        if( notifyThreadid == 0) {
                            notifyParent();
                        } else {
                            threads.get(notifyThreadid).notifyThread();
                        }
                        break;
                    case Wait:
                        int waitThreadid = threadToRun;
                        if (args.length > 1 && args[1] != null) {
                            waitThreadid = (Integer) args[1];
                        }
                        int waitTime = 5000; //15000;
                        if (args.length > 2 && args[2] != null) {
                            waitTime = (Integer) args[2];
                        }
                        if( waitThreadid != 0) {
                            thisThread.wait(waitTime);
                        } else {
                            synchronized (this) {
                                wait(waitTime);
                            }
                        }
                        break;

                    case EmployeeNotNull:
                        id = 1;
                        if (args[1] != null) {
                            id = (Integer)args[1];
                        }
                        employee = employees.get(id);
                        assertNotNull(employee);
                        break;
                    case TestEmployee:
                        id = 1;
                        if (args[1] != null) {
                            id = (Integer)args[1];
                        }
                        employee = employees.get(id);
                        switch (args.length) {
                        case 4:
                            if (args[3] != null) {
                                assertEquals("", saveVersion
                                    + (Integer) args[3], employee.getVersion());
                            }
                        case 3:
                            if (args[2] != null) {
                                assertEquals("", (String) args[2], employee
                                    .getFirstName());
                            }
                        case 2:
                            if (args[1] != null) {
                                assertEquals("", id.intValue(),
                                    employee.getId());
                            }
                            break;
                        case 1:
                            assertNull(employee);
                        }
                        break;
                    case SaveVersion:
                        id = 1;
                        if (args.length > 1) {
                            id = (Integer) args[1];
                        }
                        employee = employees.get(id);
                        saveVersion = employee.getVersion();
                        log.trace("save version= " + saveVersion);
                        break;
                    case TestVersion:
                        id = 1;
                        if (args[1] != null) {
                            id = (Integer) args[1];
                        }
                        int increment = (Integer)args[2];
                        employee = employees.get(id);
                        log.trace("test version: expected="
                            + (saveVersion + increment) + ", testing="
                            + employee.getVersion());

                        assertEquals("", saveVersion + increment, employee
                            .getVersion());
                        break;
                    case TestLockMode:
                        id = 1;
                        if (args[1] != null) {
                            id = (Integer) args[1];
                        }
                        employee = employees.get(id);
                        LockModeType expectedlockMode = (LockModeType)args[2];
                        LockModeType testinglockMode = em.getLockMode(employee);
                        log.trace("test version: expected=" + expectedlockMode
                            + ", testing=" + em.getLockMode(employee));

                        assertEquals("", expectedlockMode, testinglockMode);
                        break;
                    case ResetException:
                        thisThread.throwable = null;
                        break;
                    case TestException:
                        List<Class<?>> expectedExceptions = null;
                        if (args.length > 2) {
                            expectedExceptions = new ArrayList<Class<?>>();
                            for (int i = 2; i < args.length; ++i) {
                                if (args[i] instanceof Object[]) {
                                    for (Object o : (Object[]) args[i]) {
                                        if (o != null && o instanceof Class) {
                                            expectedExceptions
                                                .add((Class<?>) o);
                                        }
                                    }
                                } else {
                                    if (args[i] != null
                                        && args[i] instanceof Class) {
                                        expectedExceptions
                                            .add((Class<?>) args[i]);
                                    }
                                }
                            }
                        }
                        String testExClass = null;
                        Throwable curThrowable = null;
                        int threadId = threadToRun;
                        if (args.length > 1) {
                            threadId = (Integer) args[1];
                        }
                        TestThread exThread = threads.get(threadId);
                        curThrowable = exThread.throwable;
                        testExClass = processException(curAction, curThrowable);

                        boolean exMatched = false;
                        if (expectedExceptions != null
                            && expectedExceptions.size() > 0) {
                            for (Class<?> expectedException :
                                expectedExceptions) {
                                if (matchExpectedException(expectedException,
                                    curThrowable)) {
                                    exMatched = true;
                                    break;
                                }
                            }
                        } else {
                            if (curThrowable == null) {
                                exMatched = true;
                            }
                        }
                        if (!exMatched) {
                            log.trace(testExClass);
                            if (curThrowable != null) {
                                logStack(curThrowable);
                            }
                        }
                        assertTrue("Expecting=" + expectedExceptions
                            + ", Testing=" + testExClass, exMatched);
                        exThread.throwable = null;
                        break;

                    case WaitAllChildren:
                        // wait for threads to die or timeout
                        log.trace("checking if thread is alive for " +
                            (endTime-System.currentTimeMillis()) + "ms.");
                        int deadThreads = 0;
                        List<TestThread> proceedThread =
                            new ArrayList<TestThread>(threads);
                        while (proceedThread.size() > 0
                            && System.currentTimeMillis() < endTime) {
                            for (Thread thread : proceedThread) {
                                if (thread.isAlive()) {
                                    log.trace(thread + " is still alive, wait" +
                                        " for 500ms and try again.");
                                    try {
                                        Thread.sleep(500);
                                    } catch (InterruptedException e1) {
                                    }
                                    log.trace("waiting children thread ("
                                        + (endTime - System.currentTimeMillis())
                                            + " ms left)");
                                    continue;
                                } else {
                                    deadThreads++;
                                    proceedThread.remove(thread);
                                    break;
                                }
                            }
                        }
                        if (proceedThread.size() > 0) {
                        log.trace(proceedThread.size()
                            + " threads still alive.");
                        for (TestThread thread : proceedThread) {
                            log.trace("Send interrupt to thread "
                                + thread.threadToRun);
                            thread.interrupt();
                        }
                    }
                        break;
                    case JoinParent:
                        for (Thread thread : threads) {
                            boolean alive = thread.isAlive();
                            if (alive) {
                                log.trace(thread.getName() + " is still alive");
                                try {
                                    thread.interrupt();
                                    thread.join();
                                } catch (Exception e) {
                                    logStack(e);
                                }
                            }
                        }
                        break;
                    case YieldThread:
                        Thread.yield();
                        break;

                    case Sleep:
                        Thread.sleep((Integer) args[1]);
                        break;
                    case DetachSerialize:
                        id = 1;
                        if (args[1] != null) {
                            id = (Integer) args[1];
                        }
                        employee = employees.get(id);
                        log.trace("Employee (before)=" + employee);
                        ByteArrayOutputStream baos =
                            new ByteArrayOutputStream();
                        ObjectOutputStream oos = new ObjectOutputStream(baos);
                        employee.writeExternal(oos);
                        oos.flush(); baos.flush();
                        ByteArrayInputStream bais = new ByteArrayInputStream(
                            baos.toByteArray());
                        ObjectInputStream ois = new ObjectInputStream(bais);
                        LockEmployee transformedEmployee = new LockEmployee();
                        transformedEmployee.readExternal(ois);
                        log.trace("Employee (after) =" + transformedEmployee);

                        employees.put((Integer)args[2],transformedEmployee);
                        break;
                    case Info:
                        log.info(args[1]);
                        break;
                    case Warn:
                        log.warn(args[1]);
                        break;
                    case Error:
                        log.error(args[1]);
                        break;
                    case Trace:
                        log.trace(args[1]);
                        break;

                    case Test:
                        em.lock("xxx", LockModeType.WRITE);
                        break;
                    default:
                    }
                } catch (Exception ex) {
                    // only remember the first exception caught
                    if (thisThread.throwable == null) {
                        thisThread.throwable = ex;
                    }
                    log.trace("Caught exception and continue: " + ex);
                    logStack(ex);
                } catch (Error err) {
                    // only remember the first exception caught
                    if (thisThread.throwable == null) {
                        thisThread.throwable = err;
                    }
                    log.trace("Caught exception and continue: " + err);
                    logStack(err);
                }
            }
            if (em != null && em.isOpen()) {
                if (em.getTransaction().isActive()) {
                    if (thisThread != null) {
                        thisThread.systemRolledback = em.getTransaction()
                            .getRollbackOnly();
                    }
                    try {
                    if (em.getTransaction().getRollbackOnly()) {
                        log.trace("finally: rolledback");
                        em.getTransaction().rollback();
                        log.trace("finally: rolledback completed");
                    } else {
                        log.trace("finally: commit");
                        em.getTransaction().commit();
                        log.trace("finally: commit completed");
                    }
                    } catch(Exception finalEx) {
                        String failStr = processException(curAction, finalEx);
                        log.trace("Fincally:" + failStr);
                    }
                }
                em.close();
                Throwable firstThrowable = thisThread.throwable;
            if (firstThrowable != null) {
                if( firstThrowable instanceof Error ) {
                    throw (Error)firstThrowable;
                } else {
                    ByteArrayOutputStream baos = new ByteArrayOutputStream();
                    PrintStream ps = new PrintStream(baos);
                    firstThrowable.printStackTrace(ps);
                    ps.flush();
                    fail(baos.toString());
                }
            }
            log.trace("<<<< Sequenced Test: Threads=" + threadToRun + '/'
                + numThreads);
        }
    }

    private String processException(Act curAction, Throwable t) {
        String failStr = "Caught exception: none";
        if (t != null) {
            getLog().trace(
                "Caught exception: " + t.getClass().getName() + ":" + t);
            logStack(t);
            Throwable rootCause = t.getCause();
            failStr = "Failed on action '" + curAction + "' with exception "
                + t;
            if (rootCause != null) {
                failStr += "\n        -- Cause --> "
                    + rootCause.getClass().getName() + ":" + rootCause;
            }
        }
        return failStr;
    }

    private Map<String, Object> buildPropsMap(Object[] args, int startIdx) {
        Map<String, Object> props = null;
        if (args.length > startIdx) {
            props = new HashMap<String, Object>();
            while (startIdx < (args.length - 1)) {
                props.put((String) args[startIdx], args[startIdx + 1]);
                startIdx += 2;
            }
        }
        getLog().trace("Properties Map= " + props);
        return props;
    }

    private boolean matchExpectedException(Class<?> expected,
        Throwable tested) {
        assertNotNull(expected);
        Class<?> testExClass = null;
        boolean exMatched = true;
        if (tested != null) {
            testExClass = tested.getClass();
            exMatched = expected.isAssignableFrom(testExClass);
            if (!exMatched) {
                Throwable testEx = tested.getCause();
                if (testEx != null) {
                    testExClass = testEx.getClass();
                    exMatched = expected.isAssignableFrom(testExClass);
                }
            }
        } else {
            exMatched = false;
        }
        return exMatched;
    }

    private class TestThread extends Thread {
        private int threadToRun;
        private Object[][][] actions;
        private Map<Integer, LockEmployee> employees = null;

        public Throwable throwable = null;
        public boolean systemRolledback = false;

        public TestThread(int threadToRun, Object[][]... actions) {
            getLog().trace("create thread " + threadToRun);
            this.threadToRun = threadToRun;
            this.actions = actions;

            this.employees = new HashMap<Integer, LockEmployee>();
        }

        public synchronized void notifyThread() {
            notify();
        }

        public synchronized void run() {
            getLog().trace("Thread " + threadToRun + ": run()");
            launchCommonSequence(this);
        }
    }

    class PlatformSpeedTestThread extends Thread {
        long loopCnt = 0;

        public long getLoopCnt() {
            return loopCnt;
        }

        public synchronized void run() {
            while (true) {
                ++loopCnt;
                if (this.isInterrupted())
                    break;
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

QDox parse error in file://<WORKSPACE>/data/code-rep-dataset/Dataset5/Tasks/13595.java