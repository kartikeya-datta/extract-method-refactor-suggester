error id: file://<WORKSPACE>/data/code-rep-dataset/Dataset3/Tasks/7955.java
file://<WORKSPACE>/data/code-rep-dataset/Dataset3/Tasks/7955.java
### com.thoughtworks.qdox.parser.ParseException: syntax error @[1,1]

error in qdox parser
file content:
```java
offset: 1
uri: file://<WORKSPACE>/data/code-rep-dataset/Dataset3/Tasks/7955.java
text:
```scala
r@@eturn value == null ? true : value.asBoolean();

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

package org.jboss.as.ee.datasource;

import org.jboss.as.ee.component.Attachments;
import org.jboss.as.ee.component.BindingConfiguration;
import org.jboss.as.ee.component.ClassConfigurator;
import org.jboss.as.ee.component.EEModuleClassConfiguration;
import org.jboss.as.ee.component.EEModuleClassDescription;
import org.jboss.as.ee.component.EEModuleDescription;
import org.jboss.as.server.deployment.DeploymentPhaseContext;
import org.jboss.as.server.deployment.DeploymentUnit;
import org.jboss.as.server.deployment.DeploymentUnitProcessingException;
import org.jboss.as.server.deployment.DeploymentUnitProcessor;
import org.jboss.as.server.deployment.annotation.CompositeIndex;
import org.jboss.jandex.AnnotationInstance;
import org.jboss.jandex.AnnotationTarget;
import org.jboss.jandex.AnnotationValue;
import org.jboss.jandex.ClassInfo;
import org.jboss.jandex.DotName;

import javax.annotation.sql.DataSourceDefinition;
import javax.annotation.sql.DataSourceDefinitions;
import java.util.Arrays;
import java.util.Collections;
import java.util.List;

/**
 * Deployment processor responsible for processing {@link DataSourceDefinition} and {@link DataSourceDefinitions}
 * and creating {@link org.jboss.as.ee.component.BindingConfiguration}s out of them
 *
 * @author John Bailey
 * @author Jason T. Greene
 */
public class DataSourceDefinitionAnnotationParser implements DeploymentUnitProcessor {

    private static final DotName DATASOURCE_DEFINITION = DotName.createSimple(DataSourceDefinition.class.getName());
    private static final DotName DATASOURCE_DEFINITIONS = DotName.createSimple(DataSourceDefinitions.class.getName());

    @Override
    public void deploy(DeploymentPhaseContext phaseContext) throws DeploymentUnitProcessingException {
        final DeploymentUnit deploymentUnit = phaseContext.getDeploymentUnit();
        final EEModuleDescription eeModuleDescription = deploymentUnit.getAttachment(Attachments.EE_MODULE_DESCRIPTION);
        final CompositeIndex index = deploymentUnit.getAttachment(org.jboss.as.server.deployment.Attachments.COMPOSITE_ANNOTATION_INDEX);

        // @DataSourceDefinitions
        List<AnnotationInstance> datasourceDefinitions = index.getAnnotations(DATASOURCE_DEFINITIONS);
        if (datasourceDefinitions != null) {
            for (AnnotationInstance annotation : datasourceDefinitions) {
                final AnnotationTarget target = annotation.target();
                if (target instanceof ClassInfo == false) {
                    throw new DeploymentUnitProcessingException("@DataSourceDefinitions can only be applied " +
                            "on class. " + target + " is not a class");
                }
                // get the nested @DataSourceDefinition out of the outer @DataSourceDefinitions
                List<AnnotationInstance> datasources = this.getNestedDataSourceAnnotations(annotation);
                // process the nested @DataSourceDefinition
                for (AnnotationInstance datasource : datasources) {
                    // create binding configurations out of it
                    this.processDataSourceDefinition(eeModuleDescription, datasource, (ClassInfo) target);
                }
            }
        }

        // @DataSourceDefinition
        List<AnnotationInstance> datasources = index.getAnnotations(DATASOURCE_DEFINITION);
        if (datasources != null) {
            for (AnnotationInstance datasource : datasources) {
                final AnnotationTarget target = datasource.target();
                if (target instanceof ClassInfo == false) {
                    throw new DeploymentUnitProcessingException("@DataSourceDefinition can only be applied " +
                            "on class. " + target + " is not a class");
                }
                // create binding configurations out of it
                this.processDataSourceDefinition(eeModuleDescription, datasource, (ClassInfo) target);
            }
        }
    }

    @Override
    public void undeploy(DeploymentUnit context) {
    }

    private void processDataSourceDefinition(final EEModuleDescription eeModuleDescription, final AnnotationInstance datasourceDefinition, final ClassInfo targetClass) throws DeploymentUnitProcessingException {
        // create BindingConfiguration out of the @DataSource annotation
        final BindingConfiguration bindingConfiguration = this.getBindingConfiguration(datasourceDefinition);
        EEModuleClassDescription classDescription = eeModuleDescription.getOrAddClassByName(targetClass.name().toString());
        // add the binding configuration via a class configurator
        classDescription.getConfigurators().add(new ClassConfigurator() {
            @Override
            public void configure(DeploymentPhaseContext context, EEModuleClassDescription description, EEModuleClassConfiguration configuration) throws DeploymentUnitProcessingException {
                configuration.getBindingConfigurations().add(bindingConfiguration);
            }
        });
    }

    private BindingConfiguration getBindingConfiguration(final AnnotationInstance datasourceAnnotation) {

        final AnnotationValue nameValue = datasourceAnnotation.value("name");
        if (nameValue == null || nameValue.asString().isEmpty()) {
            throw new IllegalArgumentException("@DataSourceDefinition annotations must provide a name.");
        }
        String name = nameValue.asString();
        // if the name doesn't have a namespace then it defaults to java:comp/env
        if (!name.startsWith("java:")) {
            name = "java:comp/env/" + name;
        }

        final AnnotationValue classValue = datasourceAnnotation.value("className");
        if (classValue == null || classValue.asString().equals(Object.class.getName())) {
            throw new IllegalArgumentException("@DataSourceDefinition annotations must provide a driver class name.");
        }

        final String type = classValue.asString();
        final DirectDataSourceInjectionSource directDataSourceInjectionSource = new DirectDataSourceInjectionSource();
        directDataSourceInjectionSource.setClassName(type);
        directDataSourceInjectionSource.setDatabaseName(asString(datasourceAnnotation, DirectDataSourceInjectionSource.DATABASE_NAME_PROP));
        directDataSourceInjectionSource.setDescription(asString(datasourceAnnotation, DirectDataSourceInjectionSource.DESCRIPTION_PROP));
        directDataSourceInjectionSource.setInitialPoolSize(asInt(datasourceAnnotation, DirectDataSourceInjectionSource.INITIAL_POOL_SIZE_PROP));
        directDataSourceInjectionSource.setIsolationLevel(asInt(datasourceAnnotation, DirectDataSourceInjectionSource.ISOLATION_LEVEL_PROP));
        directDataSourceInjectionSource.setLoginTimeout(asInt(datasourceAnnotation, DirectDataSourceInjectionSource.LOGIN_TIMEOUT_PROP));
        directDataSourceInjectionSource.setMaxIdleTime(asInt(datasourceAnnotation, DirectDataSourceInjectionSource.MAX_IDLE_TIME_PROP));
        directDataSourceInjectionSource.setMaxStatements(asInt(datasourceAnnotation, DirectDataSourceInjectionSource.MAX_STATEMENTS_PROP));
        directDataSourceInjectionSource.setMaxPoolSize(asInt(datasourceAnnotation, DirectDataSourceInjectionSource.MAX_POOL_SIZE_PROP));
        directDataSourceInjectionSource.setMinPoolSize(asInt(datasourceAnnotation, DirectDataSourceInjectionSource.MIN_POOL_SIZE_PROP));
        directDataSourceInjectionSource.setPassword(asString(datasourceAnnotation, DirectDataSourceInjectionSource.PASSWORD_PROP));
        directDataSourceInjectionSource.setPortNumber(asInt(datasourceAnnotation, DirectDataSourceInjectionSource.PORT_NUMBER_PROP));
        directDataSourceInjectionSource.setProperties(asArray(datasourceAnnotation, DirectDataSourceInjectionSource.PROPERTIES_PROP));
        directDataSourceInjectionSource.setServerName(asString(datasourceAnnotation, DirectDataSourceInjectionSource.SERVER_NAME_PROP));
        directDataSourceInjectionSource.setTransactional(asBool(datasourceAnnotation, DirectDataSourceInjectionSource.TRANSACTIONAL_PROP));
        directDataSourceInjectionSource.setUrl(asString(datasourceAnnotation, DirectDataSourceInjectionSource.URL_PROP));
        directDataSourceInjectionSource.setUser(asString(datasourceAnnotation, DirectDataSourceInjectionSource.USER_PROP));


        final BindingConfiguration bindingDescription = new BindingConfiguration(name, directDataSourceInjectionSource);
        return bindingDescription;
    }

    private int asInt(AnnotationInstance annotation, String string) {
        AnnotationValue value = annotation.value(string);
        return value == null ? -1 : value.asInt();
    }

    private String asString(final AnnotationInstance annotation, String property) {
        AnnotationValue value = annotation.value(property);
        return value == null ? null : value.asString();
    }

    private boolean asBool(final AnnotationInstance annotation, String property) {
        AnnotationValue value = annotation.value(property);
        return value == null ? false : value.asBoolean();
    }

    private String[] asArray(final AnnotationInstance annotation, String property) {
        AnnotationValue value = annotation.value(property);
        return value == null ? null : value.asStringArray();
    }

    /**
     * Returns the nested {@link DataSourceDefinition} annotations out of the outer {@link DataSourceDefinitions} annotation
     *
     * @param datasourceDefinitions The outer {@link DataSourceDefinitions} annotation
     * @return
     */
    private List<AnnotationInstance> getNestedDataSourceAnnotations(AnnotationInstance datasourceDefinitions) {
        if (datasourceDefinitions == null) {
            return Collections.emptyList();
        }
        AnnotationInstance[] nested = datasourceDefinitions.value().asNestedArray();
        return Arrays.asList(nested);
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

QDox parse error in file://<WORKSPACE>/data/code-rep-dataset/Dataset3/Tasks/7955.java