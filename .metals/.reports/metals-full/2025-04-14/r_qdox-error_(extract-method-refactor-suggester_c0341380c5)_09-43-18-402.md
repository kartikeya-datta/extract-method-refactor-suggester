error id: file://<WORKSPACE>/data/code-rep-dataset/Dataset3/Tasks/8005.java
file://<WORKSPACE>/data/code-rep-dataset/Dataset3/Tasks/8005.java
### com.thoughtworks.qdox.parser.ParseException: syntax error @[1,8]

error in qdox parser
file content:
```java
offset: 8
uri: file://<WORKSPACE>/data/code-rep-dataset/Dataset3/Tasks/8005.java
text:
```scala
public v@@oid renew(Map<String,String> credentials, Map topologyConf) {

/**
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
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */

package backtype.storm.security.auth.kerberos;

import backtype.storm.security.auth.IAutoCredentials;
import backtype.storm.security.auth.ICredentialsRenewer;
import backtype.storm.security.auth.AuthUtils;

import java.util.Date;
import java.util.Map;
import java.util.Set;
import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.lang.reflect.Method;
import java.lang.reflect.Constructor;
import java.security.Principal;
import java.util.concurrent.atomic.AtomicReference;

import javax.security.auth.kerberos.KerberosTicket;
import javax.security.auth.kerberos.KerberosPrincipal;
import javax.security.auth.login.Configuration;
import javax.security.auth.login.LoginException;
import javax.security.auth.login.LoginContext;
import javax.security.auth.RefreshFailedException;
import javax.security.auth.Subject;
import javax.xml.bind.DatatypeConverter;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * Automatically take a user's TGT, and push it, and renew it in Nimbus.
 */
public class AutoTGT implements IAutoCredentials, ICredentialsRenewer {
    private static final Logger LOG = LoggerFactory.getLogger(AutoTGT.class);
    private static final float TICKET_RENEW_WINDOW = 0.80f;
    protected static AtomicReference<KerberosTicket> kerbTicket = new AtomicReference<KerberosTicket>();
    private Map conf;

    public void prepare(Map conf) {
        this.conf = conf;
    }

    private static KerberosTicket getTGT(Subject subject) {
        Set<KerberosTicket> tickets = subject.getPrivateCredentials(KerberosTicket.class);
        for(KerberosTicket ticket: tickets) {
            KerberosPrincipal server = ticket.getServer();
            if (server.getName().equals("krbtgt/" + server.getRealm() + "@" + server.getRealm())) {
                tickets = null;
                return ticket;
            }
        }
        tickets = null;
        return null;
    } 

    @Override
    public void populateCredentials(Map<String, String> credentials) {
        //Log the user in and get the TGT
        try {
            Configuration login_conf = AuthUtils.GetConfiguration(conf);
            ClientCallbackHandler client_callback_handler = new ClientCallbackHandler(login_conf);
        
            //login our user
            Configuration.setConfiguration(login_conf); 
            LoginContext lc = new LoginContext(AuthUtils.LOGIN_CONTEXT_CLIENT, client_callback_handler);
            try {
                lc.login();
                final Subject subject = lc.getSubject();
                KerberosTicket tgt = getTGT(subject);

                if (tgt == null) { //error
                    throw new RuntimeException("Fail to verify user principal with section \""
                            +AuthUtils.LOGIN_CONTEXT_CLIENT+"\" in login configuration file "+ login_conf);
                }

                if (!tgt.isForwardable()) {
                    throw new RuntimeException("The TGT found is not forwardable");
                }

                if (!tgt.isRenewable()) {
                    throw new RuntimeException("The TGT found is not renewable");
                }

                LOG.info("Pushing TGT for "+tgt.getClient()+" to topology.");
                saveTGT(tgt, credentials);
            } finally {
                lc.logout();
            }
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }

    public static void saveTGT(KerberosTicket tgt, Map<String, String> credentials) {
        try {
            ByteArrayOutputStream bao = new ByteArrayOutputStream();
            ObjectOutputStream out = new ObjectOutputStream(bao);
            out.writeObject(tgt);
            out.flush();
            out.close();
            credentials.put("TGT", DatatypeConverter.printBase64Binary(bao.toByteArray()));
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }

    public static KerberosTicket getTGT(Map<String, String> credentials) {
        KerberosTicket ret = null;
        if (credentials != null && credentials.containsKey("TGT")) {
            try {
                ByteArrayInputStream bin = new ByteArrayInputStream(DatatypeConverter.parseBase64Binary(credentials.get("TGT")));
                ObjectInputStream in = new ObjectInputStream(bin);
                ret = (KerberosTicket)in.readObject();
                in.close();
            } catch (Exception e) {
                throw new RuntimeException(e);
            }
        }
        return ret;
    }

    @Override
    public void updateSubject(Subject subject, Map<String, String> credentials) {
        populateSubjectWithTGT(subject, credentials);
    }

    @Override
    public void populateSubject(Subject subject, Map<String, String> credentials) {
        populateSubjectWithTGT(subject, credentials);
        loginHadoopUser(subject);
    }

    private void populateSubjectWithTGT(Subject subject, Map<String, String> credentials) {
        KerberosTicket tgt = getTGT(credentials);
        if (tgt != null) {
            KerberosTicket oldTGT = getTGT(subject);
            subject.getPrivateCredentials().add(tgt);
            if (oldTGT != null && !oldTGT.equals(tgt)) {
                subject.getPrivateCredentials().remove(oldTGT);
            }
            subject.getPrincipals().add(tgt.getClient());
            kerbTicket.set(tgt);
        } else {
            LOG.info("No TGT found in credentials");
        }
    }

    /**
     * Hadoop does not just go off of a TGT, it needs a bit more.  This
     * should fill in the rest.
     * @param subject the subject that should have a TGT in it.
     */
    private void loginHadoopUser(Subject subject) {
        Class<?> ugi = null;
        try {
            ugi = Class.forName("org.apache.hadoop.security.UserGroupInformation");
        } catch (ClassNotFoundException e) {
            LOG.info("Hadoop was not found on the class path");
            return;
        }
        try {
            Method isSecEnabled = ugi.getMethod("isSecurityEnabled");
            if (!((Boolean)isSecEnabled.invoke(null))) {
                LOG.warn("Hadoop is on the classpath but not configured for " +
                  "security, if you want security you need to be sure that " +
                  "hadoop.security.authentication=kerberos in core-site.xml " +
                  "in your jar");
                return;
            }
 
            try {
                Method login = ugi.getMethod("loginUserFromSubject", Subject.class);
                login.invoke(null, subject);
            } catch (NoSuchMethodException me) {
                //The version of Hadoop does not have the needed client changes.
                // So don't look now, but do something really ugly to work around it.
                // This is because we are reaching into the hidden bits of Hadoop security, and it works for now, but may stop at any point in time.

                //We are just trying to do the following
                // Configuration conf = new Configuration();
                // HadoopKerberosName.setConfiguration(conf);
                // subject.getPrincipals().add(new User(tgt.getClient().toString(), AuthenticationMethod.KERBEROS, null));
                String name = getTGT(subject).getClient().toString();

                LOG.warn("The Hadoop client does not have loginUserFromSubject, Trying to hack around it. This may not work...");
                Class<?> confClass = Class.forName("org.apache.hadoop.conf.Configuration");
                Constructor confCons = confClass.getConstructor();
                Object conf = confCons.newInstance();
                Class<?> hknClass = Class.forName("org.apache.hadoop.security.HadoopKerberosName");
                Method hknSetConf = hknClass.getMethod("setConfiguration",confClass);
                hknSetConf.invoke(null, conf);

                Class<?> authMethodClass = Class.forName("org.apache.hadoop.security.UserGroupInformation$AuthenticationMethod");
                Object kerbAuthMethod = null;
                for (Object authMethod : authMethodClass.getEnumConstants()) {
                    if ("KERBEROS".equals(authMethod.toString())) {
                        kerbAuthMethod = authMethod;
                        break;
                    }
                }

                Class<?> userClass = Class.forName("org.apache.hadoop.security.User");
                Constructor userCons = userClass.getConstructor(String.class, authMethodClass, LoginContext.class);
                userCons.setAccessible(true);
                Object user = userCons.newInstance(name, kerbAuthMethod, null);
                subject.getPrincipals().add((Principal)user);
            }
        } catch (Exception e) {
            LOG.warn("Something went wrong while trying to initialize Hadoop through reflection. This version of hadoop may not be compatible.", e);
        }
    }

    private long getRefreshTime(KerberosTicket tgt) {
        long start = tgt.getStartTime().getTime();
        long end = tgt.getEndTime().getTime();
        return start + (long) ((end - start) * TICKET_RENEW_WINDOW);
    }

    @Override
    public void renew(Map<String,String> credentials) {
        KerberosTicket tgt = getTGT(credentials);
        if (tgt != null) {
            long refreshTime = getRefreshTime(tgt);
            long now = System.currentTimeMillis();
            if (now >= refreshTime) {
                try {
                    LOG.info("Renewing TGT for "+tgt.getClient());
                    tgt.refresh();
                    saveTGT(tgt, credentials);
                } catch (RefreshFailedException e) {
                    LOG.warn("Failed to refresh TGT", e);
                }
            }
        }
    }

    public static void main(String[] args) throws Exception {
        AutoTGT at = new AutoTGT();
        Map conf = new java.util.HashMap();
        conf.put("java.security.auth.login.config", args[0]);
        at.prepare(conf);
        Map<String,String> creds = new java.util.HashMap<String,String>();
        at.populateCredentials(creds);
        Subject s = new Subject();
        at.populateSubject(s, creds);
        LOG.info("Got a Subject "+s);
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

QDox parse error in file://<WORKSPACE>/data/code-rep-dataset/Dataset3/Tasks/8005.java