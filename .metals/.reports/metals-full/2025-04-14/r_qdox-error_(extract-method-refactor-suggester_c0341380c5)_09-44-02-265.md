error id: file://<WORKSPACE>/data/code-rep-dataset/Dataset3/Tasks/7352.java
file://<WORKSPACE>/data/code-rep-dataset/Dataset3/Tasks/7352.java
### com.thoughtworks.qdox.parser.ParseException: syntax error @[24,1]

error in qdox parser
file content:
```java
offset: 1069
uri: file://<WORKSPACE>/data/code-rep-dataset/Dataset3/Tasks/7352.java
text:
```scala
private static abstract class ProcessUtil {

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
p@@ackage org.jboss.as.test.integration.respawn;

import java.io.BufferedInputStream;
import java.io.BufferedOutputStream;
import java.io.BufferedReader;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.OutputStream;
import java.io.PrintWriter;
import java.net.URISyntaxException;
import java.net.URL;
import java.security.NoSuchAlgorithmException;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.Iterator;
import java.util.List;
import java.util.Set;

import org.junit.Assert;
import org.jboss.as.controller.PathAddress;
import org.jboss.as.controller.PathElement;
import org.jboss.as.controller.descriptions.ModelDescriptionConstants;
import org.jboss.as.process.Main;
import org.jboss.as.process.ProcessController;
import org.jboss.as.protocol.StreamUtils;
import org.jboss.as.test.integration.domain.management.util.DomainTestSupport;
import org.jboss.as.test.shared.TestSuiteEnvironment;
import org.jboss.dmr.ModelNode;
import org.jboss.logging.Logger;
import org.jboss.sasl.util.UsernamePasswordHashUtil;
import org.junit.AfterClass;
import org.junit.BeforeClass;
import org.junit.Test;
import org.xnio.IoUtils;

import static org.jboss.as.test.integration.domain.management.util.Authentication.getCallbackHandler;
import static org.jboss.as.controller.descriptions.ModelDescriptionConstants.HOST;
import static org.jboss.as.controller.descriptions.ModelDescriptionConstants.MASTER;
import static org.jboss.as.controller.descriptions.ModelDescriptionConstants.NAME;
import static org.jboss.as.controller.descriptions.ModelDescriptionConstants.OP;
import static org.jboss.as.controller.descriptions.ModelDescriptionConstants.OP_ADDR;
import static org.jboss.as.controller.descriptions.ModelDescriptionConstants.OUTCOME;
import static org.jboss.as.controller.descriptions.ModelDescriptionConstants.READ_RESOURCE_OPERATION;
import static org.jboss.as.controller.descriptions.ModelDescriptionConstants.RESTART;
import static org.jboss.as.controller.descriptions.ModelDescriptionConstants.RESULT;
import static org.jboss.as.controller.descriptions.ModelDescriptionConstants.RUNNING_SERVER;
import static org.jboss.as.controller.descriptions.ModelDescriptionConstants.SERVER_CONFIG;
import static org.jboss.as.controller.descriptions.ModelDescriptionConstants.SHUTDOWN;
import static org.jboss.as.controller.descriptions.ModelDescriptionConstants.SUCCESS;

/**
 * RespawnTestCase
 *
 * @author <a href="mailto:kabir.khan@jboss.com">Kabir Khan</a>
 */
public class RespawnTestCase {

    private static final int TIMEOUT = 120000;
    private static final String HOST_CONTROLLER = Main.HOST_CONTROLLER_PROCESS_NAME;
    private static final String PROCESS_CONTROLLER = "Process";
    private static final String SERVER_ONE = "respawn-one";
    private static final String SERVER_TWO = "respawn-two";
    private static final int HC_PORT = 9999;

    static ProcessController processController;
    static ProcessUtil processUtil;
    static File domainConfigDir;
    static File hostXml;

    static TestControllerUtils utils;
    static TestControllerClient client;

    private static final Logger log = Logger.getLogger(RespawnTestCase.class);

    @BeforeClass
    public static void createProcessController() throws IOException, URISyntaxException, NoSuchAlgorithmException {

        // Setup client
        utils = TestControllerUtils.create(DomainTestSupport.masterAddress, HC_PORT, getCallbackHandler());
        client = new TestControllerClient(utils.getConfiguration(), utils.getExecutor());

        final String testName = RespawnTestCase.class.getSimpleName();
        final File domains = new File("target" + File.separator + "domains" + File.separator + testName);
        final File masterDir = new File(domains, "master");
        final String masterDirPath = masterDir.getAbsolutePath();
        domainConfigDir = new File(masterDir, "configuration");
        // TODO this should not be necessary
        domainConfigDir.mkdirs();

        if (File.pathSeparatorChar == ':'){
            processUtil = new UnixProcessUtil();
        } else {
            processUtil = new WindowsProcessUtil();
        }

        String jbossHome = System.getProperty("jboss.home");
        if (jbossHome == null) {
            throw new IllegalStateException("-Djboss.home must be set");
        }

        ClassLoader tccl = Thread.currentThread().getContextClassLoader();
        URL url = tccl.getResource("domain-configs/domain-respawn.xml");
        Assert.assertNotNull(url);
        File domainXml = new File(url.toURI());
        url = tccl.getResource("host-configs/respawn-master.xml");
        hostXml = new File(url.toURI());

        Assert.assertTrue(domainXml.exists());
        Assert.assertTrue(hostXml.exists());
        copyFile(domainXml, domainConfigDir);
        copyFile(hostXml, domainConfigDir);

        // No point backing up the file in a test scenario, just write what we need.
        File usersFile = new File(domainConfigDir, "mgmt-users.properties");
        FileOutputStream fos = new FileOutputStream(usersFile);
        PrintWriter pw = new PrintWriter(fos);
        pw.println("slave=" + new UsernamePasswordHashUtil().generateHashedHexURP("slave", "ManagementRealm", "slave_user_password".toCharArray()));
        pw.close();
        fos.close();
        final String address = System.getProperty("jboss.test.host.master.address", "127.0.0.1");

        List<String> args = new ArrayList<String>();
        args.add("-jboss-home");
        args.add(jbossHome);
        args.add("-jvm");
        args.add(processUtil.getJavaCommand());
        args.add("--");
        args.add("-Dorg.jboss.boot.log.file=" + masterDirPath + "/log/host-controller.log");
        args.add("-Dlogging.configuration=file:" + jbossHome + "/domain/configuration/logging.properties");
        args.add("-Djboss.test.host.master.address=" + address);
        TestSuiteEnvironment.getIpv6Args(args);
        args.add("-Xms64m");
        args.add("-Xmx512m");
        args.add("-XX:MaxPermSize=256m");
        args.add("--");
        args.add("-default-jvm");
        args.add(processUtil.getJavaCommand());
        args.add("--host-config=" + hostXml.getName());
        args.add("--domain-config=" + domainXml.getName());
        args.add("-Djboss.test.host.master.address=" + address);
        args.add("-Djboss.domain.base.dir=" + masterDir.getAbsolutePath());
        args.add("--interprocess-hc-address");
        args.add(address);
        args.add("--pc-address");
        args.add(address);

        log.info(args.toString());

        processController = Main.start(args.toArray(new String[args.size()]));
    }

    @AfterClass
    public static void destroyProcessController(){
        if (processController != null){
            processController.shutdown();
            processController = null;
        }
        IoUtils.safeClose(client);
        IoUtils.safeClose(utils);
    }

    @Test
    public void testDomainRespawn() throws Exception {

        System.out.println("testDomainRespawn()");

        //Make sure everything started
        List<RunningProcess> processes = waitForAllProcessesFullyStarted();

        //Kill the master HC and make sure that it gets restarted
        RunningProcess originalHc = processUtil.getProcess(processes, HOST_CONTROLLER);
        Assert.assertNotNull(originalHc);
        processUtil.killProcess(originalHc);
        processes = waitForAllProcesses();
        RunningProcess respawnedHc = processUtil.getProcess(processes, HOST_CONTROLLER);
        Assert.assertNotNull(respawnedHc);
        Assert.assertFalse(originalHc.getProcessId().equals(respawnedHc.getProcessId()));

        readHostControllerServers();

    }

    @Test
    public void testReloadHc() throws Exception {

        System.out.println("testReloadHc()");

        List<RunningProcess> original = waitForAllProcessesFullyStarted();
        Set<String> serverIds = new HashSet<String>();
        for (RunningProcess proc : original) {
            if (!proc.getProcess().equals(HOST_CONTROLLER)) {
                serverIds.add(proc.getProcessId());
            }
        }

        executeReloadOperation(null, null);

        List<RunningProcess> reloaded = waitForAllProcesses(serverIds);
        Assert.assertEquals(original.size(), reloaded.size());
        //Check new processes different from old, apart from HC
        for (RunningProcess reloadedProc : reloaded) {
            RunningProcess orig = findProcess(original, reloadedProc.getProcess());
            if (reloadedProc.getProcess().equals(HOST_CONTROLLER)) {
                Assert.assertTrue(reloadedProc.getProcessId().equals(orig.getProcessId()));
            } else {
                Assert.assertFalse(reloadedProc.getProcessId().equals(orig.getProcessId()));
            }
        }
    }

    @Test
    public void testReloadHcButNotServers() throws Exception {

        System.out.println("testReloadHcButNotServers()");

        List<RunningProcess> original = waitForAllProcessesFullyStarted();

        //Execute reload w/ restart-servers=false, admin-only=true
        executeReloadOperation(false, true);

        //Read HC model until there are no servers
        long start = System.currentTimeMillis();
        long timeout = start + TIMEOUT;
        long minCheckPeriod =  start + 5000;
        while (true) {
            Thread.sleep(500);
            if (lookupServerInModel(MASTER, SERVER_ONE) || lookupServerInModel(MASTER, SERVER_TWO)) {
                if (System.currentTimeMillis() >= timeout) {
                    Assert.fail("Should not have servers in restarted admin-only HC model");
                }
            } else if (System.currentTimeMillis() >= minCheckPeriod) {
                break;
            } // else loop and retest in case the server reconnects w/in minCheckPeriod
        }

        System.out.println("reloaded into admin-only after " + (System.currentTimeMillis() - start) + " ms");

        //Execute reload w/ restart-servers=false, admin-only=false
        executeReloadOperation(false, false);
        System.out.println("reloaded out of admin-only; waiting for servers");
        //Wait for servers
        readHostControllerServers();


        //Check all processes are the same
        List<RunningProcess> reloaded = waitForAllProcesses();
        Assert.assertEquals(original.size(), reloaded.size());
        //Check new processes different from old, apart from HC
        for (RunningProcess reloadedProc : reloaded) {
            RunningProcess orig = findProcess(original, reloadedProc.getProcess());
            Assert.assertTrue(reloadedProc.getProcessId().equals(orig.getProcessId()));
        }
    }

    @Test
    public void testReloadHcButNotServersWithFailedServer() throws Exception {

        System.out.println("testReloadHcButNotServersWithFailedServer()");

        List<RunningProcess> original = waitForAllProcessesFullyStarted();

        RunningProcess serverOne = processUtil.getProcess(original, SERVER_ONE);
        Assert.assertNotNull(serverOne);

        System.out.println("killing respawn-one: " + serverOne);
        processUtil.killProcess(serverOne);

        //Execute reload w/ restart-servers=false, admin-only=false
        executeReloadOperation(false, false);
        //Wait for servers
        readHostControllerServer(SERVER_TWO);

        manageServer("stop", SERVER_ONE);
        Thread.sleep(5000);
        readHostControllerServer(SERVER_TWO);
        manageServer("start", SERVER_ONE);

        //Check all processes are the same
        List<RunningProcess> reloaded = waitForAllProcesses();
        Assert.assertEquals(original.size(), reloaded.size());
    }

    @Test
    public void testStartKilledServer() throws Exception {

        List<RunningProcess> original = waitForAllProcessesFullyStarted();
        RunningProcess serverOne = processUtil.getProcess(original, SERVER_ONE);
        Assert.assertNotNull(serverOne);

        System.out.println("killing respawn-one: " + serverOne);
        processUtil.killProcess(serverOne);
        manageServer("start", SERVER_ONE);
        readHostControllerServer(SERVER_ONE);

        //Check all processes are the same
        List<RunningProcess> reloaded = waitForAllProcesses();
        Assert.assertEquals(original.size(), reloaded.size());
    }

    @Test
    public void testHCReloadAbortPreservesServers() throws Exception {

        System.out.println("testHCReloadAbortPreservesServers()");

        List<RunningProcess> original = waitForAllProcessesFullyStarted();

        try {
            // Replace host.xml with an invalid doc
            File toBreak = new File(domainConfigDir, hostXml.getName());
            PrintWriter pw = null;
            try {
                pw = new PrintWriter(toBreak);
                pw.println("<host/>");
            } finally {
                if (pw != null) {
                    pw.close();
                }
            }

            // Execute reload w/ restart-servers=false, admin-only=false
            // The reload should abort the HC due to bad xml
            executeReloadOperation(false, false);

            long deadline = System.currentTimeMillis() + 30000;
            boolean origHCGone;
            do {
                // Check that the originalHC process doesn't exist and
                // both original servers still exist. The originalHC still existing
                // is not a failure condition until we hit the deadline
                origHCGone = true;
                Set<RunningProcess> updated = new HashSet<RunningProcess>(waitForProcesses(SERVER_ONE, SERVER_TWO));
                for (RunningProcess process : original) {
                    if (!process.getProcess().equals(HOST_CONTROLLER)) {
                        Assert.assertTrue(process.getProcess() + " is missing", updated.contains(process));
                    } else {
                        origHCGone = !updated.contains(process);
                    }
                }
                if (!origHCGone) {
                    Thread.sleep(100);
                }
            } while (!origHCGone && System.currentTimeMillis() < deadline);

            if (!origHCGone) {
                Assert.fail("Original HC process did not terminate within 30 seconds");
            }

        } finally {
            copyFile(hostXml, domainConfigDir);
            waitForAllProcesses();
        }
    }


    private RunningProcess findProcess(List<RunningProcess> processes, String name) {
        RunningProcess proc = null;
        for (RunningProcess cur : processes) {
            if (cur.getProcess().equals(name)) {
                Assert.assertNull(proc);
                proc = cur;
            }
        }
        Assert.assertNotNull(proc);
        return proc;
    }


    private void executeReloadOperation(Boolean restartServers, Boolean adminOnly) throws Exception {
        ModelNode operation = new ModelNode();
        operation.get(OP).set("reload");
        operation.get(OP_ADDR).set(PathAddress.pathAddress(PathElement.pathElement(HOST, "master")).toModelNode());
        if (restartServers != null) {
            operation.get(ModelDescriptionConstants.RESTART_SERVERS).set(restartServers);
        }
        if (adminOnly != null) {
            operation.get(ModelDescriptionConstants.ADMIN_ONLY).set(adminOnly);
        }
        final TestControllerClient client = getControllerClient();
        try {
            Assert.assertEquals(SUCCESS, client.executeAwaitClosed(operation).get(OUTCOME).asString());
        } catch (IOException canHappenWhenShuttingDownController) {
        }
    }

    private void shutdownHostController(boolean restart) throws Exception {
        final ModelNode operation = new ModelNode();
        operation.get(OP).set(SHUTDOWN);
        operation.get(OP_ADDR).set(PathAddress.pathAddress(PathElement.pathElement(HOST, "master")).toModelNode());
        operation.get(RESTART).set(restart);

    }

    private void manageServer(String operationName, String serverName) throws Exception {
        ModelNode operation = new ModelNode();
        operation.get(OP).set(operationName);
        operation.get(OP_ADDR).set(getHostControllerServerConfigAddress(MASTER, serverName));
        operation.get("blocking").set(true);

        try {
            Assert.assertEquals(SUCCESS, getControllerClient().execute(operation).get(OUTCOME).asString());
        } catch (IOException canHappenWhenShuttingDownController) {
        }
    }

    private void readHostControllerServer(String serverName) throws Exception {
        final long time = System.currentTimeMillis() + TIMEOUT;
        boolean hasOne = false;

        do {
            Thread.sleep(250);
            hasOne = lookupServerInModel(MASTER, serverName);
            if (hasOne) {
                break;
            }
        } while (System.currentTimeMillis() < time);
        Assert.assertTrue(hasOne);
    }

    private void readHostControllerServers() throws Exception {
        final long time = System.currentTimeMillis() + TIMEOUT;
        boolean hasOne = false;
        boolean hasTwo = false;
        do {
            Thread.sleep(250);
            hasOne = lookupServerInModel(MASTER, SERVER_ONE);
            hasTwo = lookupServerInModel(MASTER, SERVER_TWO);
            if (hasOne && hasTwo) {
                break;
            }
        } while (System.currentTimeMillis() < time);
        Assert.assertTrue(hasOne);
        Assert.assertTrue(hasTwo);
    }

    private boolean lookupServerInModel(String host, String server) throws Exception {
        final ModelNode operation = new ModelNode();
        operation.get(OP).set(READ_RESOURCE_OPERATION);
        operation.get(OP_ADDR).set(getHostControllerServerAddress(host, server));

        try {
            final ModelNode result = getControllerClient().execute(operation);
            if (result.get(OUTCOME).asString().equals(SUCCESS)){
                final ModelNode model = result.require(RESULT);
                if (model.hasDefined(NAME) && model.get(NAME).asString().equals(server)) {
                    return true;
                }
            }
        } catch (IOException e) {
            //
        }
        return false;
    }

    private ModelNode getHostControllerServerAddress(String host, String server) {
        return PathAddress.pathAddress(PathElement.pathElement(HOST, host), PathElement.pathElement(RUNNING_SERVER, server)).toModelNode();
    }

    private ModelNode getHostControllerServerConfigAddress(String host, String server) {
        return PathAddress.pathAddress(PathElement.pathElement(HOST, host), PathElement.pathElement(SERVER_CONFIG, server)).toModelNode();
    }

    private List<RunningProcess> waitForAllProcesses() throws Exception {
        return waitForProcesses(HOST_CONTROLLER, SERVER_ONE, SERVER_TWO);
    }

    private List<RunningProcess> waitForAllProcessesFullyStarted() throws Exception {
        List<RunningProcess> result = waitForProcesses(HOST_CONTROLLER, SERVER_ONE, SERVER_TWO);
        readHostControllerServers();
        return result;
    }

    private List<RunningProcess> waitForProcesses(String... requiredNames) throws Exception {
        final long time = System.currentTimeMillis() + TIMEOUT;
        List<RunningProcess> runningProcesses;
        do {
            Thread.sleep(200);
            runningProcesses = processUtil.getRunningProcesses();
            if (processUtil.containsProcesses(runningProcesses, requiredNames)){
                return runningProcesses;
            }
        } while(System.currentTimeMillis() < time);
        Assert.fail("Did not have all running processes " + runningProcesses);
        return null;
    }

    private List<RunningProcess> waitForAllProcesses(Set<String> excludedProcessIds) throws Exception {
        final long time = System.currentTimeMillis() + TIMEOUT;
        List<RunningProcess> runningProcesses;
        do {
            Thread.sleep(200);
            runningProcesses = processUtil.getRunningProcesses();
            for (Iterator<RunningProcess> it = runningProcesses.iterator() ; it.hasNext() ; ) {
                RunningProcess proc = it.next();
                if (excludedProcessIds.contains(proc.getProcessId())) {
                    it.remove();
                }
            }
            if (processUtil.containsProcesses(runningProcesses, HOST_CONTROLLER, SERVER_ONE, SERVER_TWO)){
                return runningProcesses;
            }
        } while(System.currentTimeMillis() < time);
        Assert.fail("Did not have all running processes " + runningProcesses);
        return null;

    }

    private static void copyFile(File file, File directory) throws IOException{
        File tgt = new File(directory, file.getName());
        if (tgt.exists()) {
            if (!tgt.delete()) {
                throw new IllegalStateException("Could not delete file " + tgt.getAbsolutePath());
            }
        }
        final InputStream in = new BufferedInputStream(new FileInputStream(file));
        try {
            final OutputStream out = new BufferedOutputStream(new FileOutputStream(tgt));
            try {
                int i = in.read();
                while (i != -1) {
                    out.write(i);
                    i = in.read();
                }
            } finally {
                IoUtils.safeClose(out);
            }
        } finally {
            IoUtils.safeClose(in);
        }
    }

    static TestControllerClient getControllerClient() throws IOException {
        client.connect(); // Ensure connected
        return client;
    }

    private abstract static class ProcessUtil {

        List<String> initialProcessIds;

        ProcessUtil(){
            initialProcessIds = getInitialProcessIds();
        }

        List<String> getInitialProcessIds(){
            List<String> processes = listProcesses();
            List<String> ids = new ArrayList<String>();
            for (String proc : processes){
                ids.add(parseProcessId(proc));
            }
            return ids;
        }

        String parseProcessId(String proc){
            proc = proc.trim();
            int i = proc.indexOf(' ');
            return proc.substring(0, i);
        }

        List<RunningProcess> getRunningProcesses(){
            List<RunningProcess> running = new ArrayList<RunningProcess>();
            List<String> processes = listProcesses();
            for (String proc : processes){
                String id = parseProcessId(proc);
                if (!initialProcessIds.contains(id)){
                    if (proc.contains(SERVER_ONE)){
                        running.add(new RunningProcess(id, SERVER_ONE));
                    } else if (proc.contains(SERVER_TWO)){
                        running.add(new RunningProcess(id, SERVER_TWO));
                    } else if (proc.contains(HOST_CONTROLLER) && !proc.contains(PROCESS_CONTROLLER)){
                        running.add(new RunningProcess(id, HOST_CONTROLLER));
                    }
                }
            }
            return running;
        }

        List<String> listProcesses() {
            final Process p;
            try {
                p = Runtime.getRuntime().exec(getJpsCommand());
            } catch (IOException e) {
                throw new RuntimeException(e);
            }

            List<String> processes = new ArrayList<String>();
            BufferedReader input = new BufferedReader(new InputStreamReader(p.getInputStream()));
            try {
                String line;
                while ((line = input.readLine()) != null) {
                    if (line.contains("jboss-modules.jar")){
                        processes.add(line);
                    }

                }
            } catch (IOException e) {
                throw new RuntimeException(e);
            } finally {
                StreamUtils.safeClose(input);
            }
            return processes;
        }

        void killProcess(RunningProcess process) {
            try {
                Runtime.getRuntime().exec(getKillCommand(process));
            } catch (IOException e) {
                throw new RuntimeException(e);
            }

            final long time = System.currentTimeMillis() + TIMEOUT;
            do {
                try {
                    Thread.sleep(200);
                } catch (InterruptedException e) {
                    throw new RuntimeException(e);
                }
                List<RunningProcess> runningProcesses = processUtil.getRunningProcesses();
                if (processUtil.getProcessById(runningProcesses, process.getProcessId()) == null){
                    return;
                }
            } while(System.currentTimeMillis() < time);

            Assert.fail("Did not kill process " + process + " " + processUtil.getRunningProcesses());
        }

        abstract String[] getJpsCommand();

        abstract String getJavaCommand();

        abstract String getKillCommand(RunningProcess process);

        private boolean containsProcesses(List<RunningProcess> runningProcesses, String...names){
            for (String name : names){
                boolean found = false;
                for (RunningProcess proc : runningProcesses) {
                    if (proc.getProcess().equals(name)){
                        found = true;
                        continue;
                    }
                }
                if (!found){
                    return false;
                }
            }
            return true;
        }

        private RunningProcess getProcess(List<RunningProcess> runningProcesses, String name){
            for (RunningProcess proc : runningProcesses){
                if (proc.getProcess().equals(name)){
                    return proc;
                }
            }
            return null;
        }

        private RunningProcess getProcessById(List<RunningProcess> runningProcesses, String id){
            for (RunningProcess proc : runningProcesses){
                if (proc.getProcessId().equals(id)){
                    return proc;
                }
            }
            return null;
        }
    }

    private static class UnixProcessUtil extends ProcessUtil {
        @Override
        String[] getJpsCommand() {
            final File jreHome = new File(System.getProperty("java.home"));
            Assert.assertTrue("JRE home not found. File: " + jreHome.getAbsoluteFile(), jreHome.exists());
            if (System.getProperty("java.vendor.url","whatever").contains("ibm.com")) {
                return new String[] { "sh", "-c", "ps -ef | awk '{$1=\"\"; print $0}'" };
            } else {
                File jpsExe = new File(jreHome, "bin/jps");
                if (!jpsExe.exists()) {
                    jpsExe = new File(jreHome, "../bin/jps");
                }
                Assert.assertTrue("JPS executable not found. File: " + jpsExe, jpsExe.exists());
                return new String[] { jpsExe.getAbsolutePath(), "-lv" };
            }
        }

        @Override
        String getJavaCommand() {
            return System.getProperty("java.home") + "/bin/java";
        }

        @Override
        String getKillCommand(RunningProcess process) {
            return "kill -9 " + process.getProcessId();
        }
    }

    private static class WindowsProcessUtil extends ProcessUtil {

        @Override
        String[] getJpsCommand() {
            final File jreHome = new File(System.getProperty("java.home"));
            Assert.assertTrue("JRE home not found. File: " + jreHome.getAbsoluteFile(), jreHome.exists());
            File jpsExe = new File(jreHome, "bin/jps.exe");
            if (!jpsExe.exists()) {
                jpsExe = new File(jreHome, "../bin/jps.exe");
            }
            Assert.assertTrue("JPS executable not found. File: " + jpsExe, jpsExe.exists());
            return new String[] { jpsExe.getAbsolutePath(), "-lv" };
        }

        @Override
        String getJavaCommand() {
            return System.getProperty("java.home") + "/bin/java.exe";
        }

        @Override
        String getKillCommand(RunningProcess process) {
            return "taskkill /f /pid " + process.getProcessId();
        }
    }

    private static class RunningProcess {
        final String processId;
        final String process;

        private RunningProcess(String processId, String process) {
            this.processId = processId;
            this.process = process;
        }

        public String getProcessId() {
            return processId;
        }

        public String getProcess() {
            return process;
        }

        @Override
        public int hashCode() {
            return processId.hashCode();
        }

        @Override
        public boolean equals(Object obj) {
            return obj instanceof RunningProcess && ((RunningProcess) obj).processId.equals(processId);
        }

        @Override
        public String toString() {
            return "Process{id=" + processId + ", process=" + process + "}";
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

QDox parse error in file://<WORKSPACE>/data/code-rep-dataset/Dataset3/Tasks/7352.java