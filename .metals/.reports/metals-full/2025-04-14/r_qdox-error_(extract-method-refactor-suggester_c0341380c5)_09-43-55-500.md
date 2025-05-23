error id: file://<WORKSPACE>/data/code-rep-dataset/Dataset3/Tasks/10782.java
file://<WORKSPACE>/data/code-rep-dataset/Dataset3/Tasks/10782.java
### com.thoughtworks.qdox.parser.ParseException: syntax error @[1,1]

error in qdox parser
file content:
```java
offset: 1
uri: file://<WORKSPACE>/data/code-rep-dataset/Dataset3/Tasks/10782.java
text:
```scala
s@@etParams(ctx, builder.getModelNode());

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
package org.jboss.as.cli.handlers.jca;

import java.util.Collections;
import java.util.List;

import org.jboss.as.cli.CommandContext;
import org.jboss.as.cli.CommandFormatException;
import org.jboss.as.cli.ParsedArguments;
import org.jboss.as.cli.Util;
import org.jboss.as.cli.handlers.BaseOperationCommand;
import org.jboss.as.cli.handlers.SimpleTabCompleter;
import org.jboss.as.cli.impl.ArgumentWithValue;
import org.jboss.as.cli.impl.ArgumentWithoutValue;
import org.jboss.as.cli.impl.DefaultCompleter;
import org.jboss.as.cli.impl.DefaultCompleter.CandidatesProvider;
import org.jboss.as.cli.impl.RequestParamArgWithValue;
import org.jboss.as.cli.impl.RequestParamArgWithoutValue;
import org.jboss.as.cli.impl.RequestParamPropertiesArg;
import org.jboss.as.cli.impl.RequiredRequestParamArg;
import org.jboss.as.cli.operation.OperationFormatException;
import org.jboss.as.cli.operation.OperationRequestAddress;
import org.jboss.as.cli.operation.impl.DefaultOperationRequestAddress;
import org.jboss.as.cli.operation.impl.DefaultOperationRequestBuilder;
import org.jboss.dmr.ModelNode;

/**
 *
 * @author Alexey Loubyansky
 */
public class BaseDataSourceAddHandler extends BaseOperationCommand {

    private final String dsType;
    private final ArgumentWithValue profile;
    private final ArgumentWithValue jndiName;

    public BaseDataSourceAddHandler(String commandName, String dsType) {
        super(commandName, true);

        this.dsType = dsType;

        profile = new ArgumentWithValue(this, new DefaultCompleter(new CandidatesProvider(){
            @Override
            public List<String> getAllCandidates(CommandContext ctx) {
                return Util.getNodeNames(ctx.getModelControllerClient(), null, "profile");
            }}), "--profile") {
            @Override
            public boolean canAppearNext(CommandContext ctx) throws CommandFormatException {
                if(!ctx.isDomainMode()) {
                    return false;
                }
                return super.canAppearNext(ctx);
            }
        };

        jndiName =  new RequiredRequestParamArg("jndi-name", this, "--jndi-name") {
            @Override
            public boolean canAppearNext(CommandContext ctx) throws CommandFormatException {
                if(ctx.isDomainMode() && !profile.isPresent(ctx.getParsedArguments())) {
                    return false;
                }
                return super.canAppearNext(ctx);
            }
        };

        initArguments();
    }

    /* (non-Javadoc)
     * @see org.jboss.as.cli.OperationCommand#buildRequest(org.jboss.as.cli.CommandContext)
     */
    @Override
    public ModelNode buildRequest(CommandContext ctx) throws CommandFormatException {

        DefaultOperationRequestBuilder builder = new DefaultOperationRequestBuilder();
        ParsedArguments args = ctx.getParsedArguments();

        if(ctx.isDomainMode()) {
            String profile = this.profile.getValue(args);
            if(profile == null) {
                throw new OperationFormatException("--profile argument value is missing.");
            }
            builder.addNode("profile",profile);
        }

        builder.addNode("subsystem", "datasources");
        builder.addNode(dsType, jndiName.getValue(args, true));
        builder.setOperationName("add");

        setParams(args, builder.getModelNode());

        return builder.buildRequest();
    }

    private void initArguments() {
        ArgumentWithoutValue lastRequired = initRequiredArguments();
        initOptionalArguments(lastRequired);
    }

    protected ArgumentWithoutValue initRequiredArguments() {
        /*
        driverClass = new ArgumentWithValue(this, "--driver-class") {
        @Override
        public boolean canAppearNext(CommandContext ctx) throws CommandFormatException {
            if(ctx.isDomainMode() && !profile.isPresent(ctx.getParsedArguments())) {
                return false;
            }
            return super.canAppearNext(ctx);
        }
        };
        */

        RequestParamArgWithValue driverName = new RequiredRequestParamArg("driver-name", this,
                new DefaultCompleter(new CandidatesProvider() {
                    @Override
                    public List<String> getAllCandidates(CommandContext ctx) {
                        final String profileName;
                        if (ctx.isDomainMode()) {
                            profileName = profile.getValue(ctx.getParsedArguments());
                            if (profileName == null) {
                                return Collections.emptyList();
                            }
                        } else {
                            profileName = null;
                        }

                        OperationRequestAddress datasources = new DefaultOperationRequestAddress();
                        if (profileName != null) {
                            datasources.toNode("profile", profileName);
                        }
                        datasources.toNode("subsystem", "datasources");
                        return Util.getNodeNames(
                                ctx.getModelControllerClient(), datasources,
                                "jdbc-driver");
                    }
                }));
        driverName.addRequiredPreceding(jndiName);

        RequestParamArgWithValue poolName = new RequiredRequestParamArg("pool-name", this, "--pool-name");
        poolName.addRequiredPreceding(driverName);

        return poolName;
    }

    protected void initOptionalArguments(ArgumentWithoutValue lastRequired) {
        RequestParamArgWithValue username = new RequestParamArgWithValue("user-name", this, "--username");
        username.addRequiredPreceding(lastRequired);

        RequestParamArgWithValue password =  new RequestParamArgWithValue("password", this, "--password");
        password.addRequiredPreceding(lastRequired);

        RequestParamArgWithoutValue useJavaContext =  new RequestParamArgWithoutValue("use-java-context", this);
        useJavaContext.addRequiredPreceding(lastRequired);

        RequestParamArgWithValue maxPoolSize = new RequestParamArgWithValue("max-pool-size", this);
        maxPoolSize.addRequiredPreceding(lastRequired);

        RequestParamArgWithValue minPoolSize = new RequestParamArgWithValue("min-pool-size", this);
        minPoolSize.addRequiredPreceding(lastRequired);

        RequestParamArgWithValue disabled = new RequestParamArgWithValue("enabled", this, "--disabled") {
            @Override
            public boolean isValueRequired() {
                return false;
            }
            @Override
            public void set(ParsedArguments args, ModelNode request) throws CommandFormatException {
                if(isPresent(args)) {
                    setValue(request, "enabled", "false");
                }
            }
        };
        disabled.addRequiredPreceding(lastRequired);

        RequestParamArgWithValue newConnectionSql = new RequestParamArgWithValue("new-connection-sql", this);
        newConnectionSql.addRequiredPreceding(lastRequired);

        RequestParamArgWithValue urlDelimiter = new RequestParamArgWithValue("url-delimiter", this);
        urlDelimiter.addRequiredPreceding(lastRequired);
        RequestParamArgWithValue urlSelectorStrategyClass = new RequestParamArgWithValue("url-selector-strategy-class-name", this, "--url-selector-strategy-class");
        urlSelectorStrategyClass.addRequiredPreceding(lastRequired);

        RequestParamArgWithoutValue poolPrefill =  new RequestParamArgWithoutValue("pool-prefill", this);
        poolPrefill.addRequiredPreceding(lastRequired);
        RequestParamArgWithoutValue poolUseStrictMin =  new RequestParamArgWithoutValue("pool-use-strict-min", this);
        poolUseStrictMin.addRequiredPreceding(lastRequired);

        RequestParamArgWithValue flushStrategy =  new RequestParamArgWithValue("flush-strategy", this, new SimpleTabCompleter(new String[]{"FAILING_CONNECTION_ONLY", "IDLE_CONNECTIONS", "ENTIRE_POOL"}));
        flushStrategy.addRequiredPreceding(lastRequired);

        RequestParamArgWithValue securityDomain = new RequestParamArgWithValue("security-domain", this);
        securityDomain.addRequiredPreceding(lastRequired);

        RequestParamArgWithValue reauthPluginClass = new RequestParamArgWithValue("reauth-plugin-class-name", this);
        reauthPluginClass.addRequiredPreceding(lastRequired);
        RequestParamArgWithValue reauthPluginProps = new RequestParamPropertiesArg("reauth-plugin-properties", this);
        reauthPluginProps.addRequiredPreceding(reauthPluginClass);

        RequestParamArgWithoutValue sharePreparedStatements =  new RequestParamArgWithoutValue("share-prepared-statements", this);
        sharePreparedStatements.addRequiredPreceding(lastRequired);

        RequestParamArgWithValue psCacheSize = new RequestParamArgWithValue("prepared-statements-cacheSize", this, "--prepared-statements-cache-size");
        psCacheSize.addRequiredPreceding(lastRequired);

        RequestParamArgWithValue trackStatements =  new RequestParamArgWithValue("track-statements", this, new SimpleTabCompleter(new String[]{"FALSE", "NOWARN", "TRUE"}));
        trackStatements.addRequiredPreceding(lastRequired);

        RequestParamArgWithValue allocationRetry =  new RequestParamArgWithValue("allocation-retry", this);
        allocationRetry.addRequiredPreceding(lastRequired);

        RequestParamArgWithValue allocationRetryWait =  new RequestParamArgWithValue("allocation-retry-wait-millis", this);
        allocationRetryWait.addRequiredPreceding(lastRequired);

        RequestParamArgWithValue blockingTimeoutWait =  new RequestParamArgWithValue("blocking-timeout-wait-millis", this);
        blockingTimeoutWait.addRequiredPreceding(lastRequired);

        RequestParamArgWithValue idleTimeout =  new RequestParamArgWithValue("idle-timeout-minutes", this);
        idleTimeout.addRequiredPreceding(lastRequired);

        RequestParamArgWithValue queryTimeout =  new RequestParamArgWithValue("query-timeout", this);
        queryTimeout.addRequiredPreceding(lastRequired);

        RequestParamArgWithValue useTryLock =  new RequestParamArgWithValue("use-try-lock", this);
        useTryLock.addRequiredPreceding(lastRequired);

        RequestParamArgWithValue setTxQueryTimeout =  new RequestParamArgWithValue("set-tx-query-timeout", this);
        setTxQueryTimeout.addRequiredPreceding(lastRequired);

        RequestParamArgWithValue txIsolation =  new RequestParamArgWithValue("transaction-isolation", this, new SimpleTabCompleter(new String[]{"TRANSACTION_READ_UNCOMMITTED", "TRANSACTION_READ_COMMITTED", "TRANSACTION_REPEATABLE_READ", "TRANSACTION_SERIALIZABLE", "TRANSACTION_NONE"}));
        txIsolation.addRequiredPreceding(lastRequired);

        RequestParamArgWithValue checkValidConnectionSql =  new RequestParamArgWithValue("check-valid-connection-sql", this);
        checkValidConnectionSql.addRequiredPreceding(lastRequired);

        RequestParamArgWithValue exceptionSorterClass =  new RequestParamArgWithValue("exception-sorter-class-name", this, "--exception-sorter-class");
        exceptionSorterClass.addRequiredPreceding(lastRequired);
        RequestParamArgWithValue exceptionSorterProps =  new RequestParamPropertiesArg("exceptionsorter-properties", this, "--exception-sorter-properties");
        exceptionSorterProps.addRequiredPreceding(exceptionSorterClass);

        RequestParamArgWithValue staleConnectionCheckerClass =  new RequestParamArgWithValue("stale-connection-checker-class-name", this, "--stale-connection-checker-class");
        staleConnectionCheckerClass.addRequiredPreceding(lastRequired);
        RequestParamArgWithValue staleConnectionCheckerProps =  new RequestParamPropertiesArg("staleconnectionchecker-properties", this, "--stale-connection-checker-properties");
        staleConnectionCheckerProps.addRequiredPreceding(staleConnectionCheckerClass);

        RequestParamArgWithValue validConnectionCheckerClass =  new RequestParamArgWithValue("valid-connection-checker-class-name", this, "--valid-connection-checker-class");
        validConnectionCheckerClass.addRequiredPreceding(lastRequired);
        RequestParamArgWithValue validConnectionCheckerProps =  new RequestParamPropertiesArg("validconnectionchecker-properties", this, "--valid-connection-checker-properties");
        validConnectionCheckerProps.addRequiredPreceding(validConnectionCheckerClass);

        RequestParamArgWithoutValue backgroundValidation =  new RequestParamArgWithoutValue("background-validation", this);
        backgroundValidation.addRequiredPreceding(lastRequired);

        RequestParamArgWithValue backgroundValidationMins =  new RequestParamArgWithValue("background-validation-minutes", this);
        backgroundValidationMins.addRequiredPreceding(lastRequired);

        RequestParamArgWithoutValue useFastFail =  new RequestParamArgWithoutValue("use-fast-fail", this);
        useFastFail.addRequiredPreceding(lastRequired);

        RequestParamArgWithoutValue validateOnMatch =  new RequestParamArgWithoutValue("validate-on-match", this);
        validateOnMatch.addRequiredPreceding(lastRequired);

        RequestParamArgWithoutValue spy =  new RequestParamArgWithoutValue("spy", this);
        spy.addRequiredPreceding(lastRequired);

        RequestParamArgWithoutValue useCCM =  new RequestParamArgWithoutValue("use-ccm", this);
        useCCM.addRequiredPreceding(lastRequired);
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

QDox parse error in file://<WORKSPACE>/data/code-rep-dataset/Dataset3/Tasks/10782.java