error id: file://<WORKSPACE>/data/code-rep-dataset/Dataset3/Tasks/10756.java
file://<WORKSPACE>/data/code-rep-dataset/Dataset3/Tasks/10756.java
### com.thoughtworks.qdox.parser.ParseException: syntax error @[1,1]

error in qdox parser
file content:
```java
offset: 1
uri: file://<WORKSPACE>/data/code-rep-dataset/Dataset3/Tasks/10756.java
text:
```scala
t@@empFileProvider = TempFileProvider.create("cli", Executors.newSingleThreadScheduledExecutor(), true);

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
package org.jboss.as.cli.handlers;


import java.io.BufferedReader;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.IOException;
import java.net.MalformedURLException;
import java.net.URL;
import java.util.Arrays;
import java.util.Collection;
import java.util.Collections;
import java.util.List;
import java.util.concurrent.Executors;

import org.jboss.as.cli.CommandContext;
import org.jboss.as.cli.CommandFormatException;
import org.jboss.as.cli.CommandLineCompleter;
import org.jboss.as.cli.CommandLineException;
import org.jboss.as.cli.Util;
import org.jboss.as.cli.impl.ArgumentWithValue;
import org.jboss.as.cli.impl.ArgumentWithoutValue;
import org.jboss.as.cli.impl.CommaSeparatedCompleter;
import org.jboss.as.cli.impl.FileSystemPathArgument;
import org.jboss.as.cli.operation.OperationFormatException;
import org.jboss.as.cli.operation.ParsedCommandLine;
import org.jboss.as.cli.operation.impl.DefaultOperationRequestAddress;
import org.jboss.as.controller.client.ModelControllerClient;
import org.jboss.as.controller.client.Operation;
import org.jboss.as.controller.client.OperationBuilder;
import org.jboss.dmr.ModelNode;
import org.jboss.vfs.TempFileProvider;
import org.jboss.vfs.VFSUtils;
import org.jboss.vfs.spi.MountHandle;

/**
 *
 * @author Alexey Loubyansky
 */
public class DeployHandler extends DeploymentHandler {

    private final ArgumentWithoutValue force;
    private final ArgumentWithoutValue l;
    private final ArgumentWithoutValue path;
    private final ArgumentWithoutValue url;
    private final ArgumentWithoutValue name;
    private final ArgumentWithoutValue rtName;
    private final ArgumentWithValue serverGroups;
    private final ArgumentWithoutValue allServerGroups;
    private final ArgumentWithoutValue disabled;
    private final ArgumentWithoutValue unmanaged;
    private final ArgumentWithValue script;

    public DeployHandler(CommandContext ctx) {
        super(ctx, "deploy", true);

        final DefaultOperationRequestAddress requiredAddress = new DefaultOperationRequestAddress();
        requiredAddress.toNodeType(Util.DEPLOYMENT);
        addRequiredPath(requiredAddress);

        l = new ArgumentWithoutValue(this, "-l");
        l.setExclusive(true);

        final FilenameTabCompleter pathCompleter = Util.isWindows() ? new WindowsFilenameTabCompleter(ctx) : new DefaultFilenameTabCompleter(ctx);
        path = new FileSystemPathArgument(this, pathCompleter, 0, "--path");
        path.addCantAppearAfter(l);

        url = new ArgumentWithValue(this, "--url");
        url.addCantAppearAfter(path);
        path.addCantAppearAfter(url);

        force = new ArgumentWithoutValue(this, "--force", "-f");
        force.addRequiredPreceding(path);

        name = new ArgumentWithValue(this, new CommandLineCompleter() {
            @Override
            public int complete(CommandContext ctx, String buffer, int cursor, List<String> candidates) {

                ParsedCommandLine args = ctx.getParsedCommandLine();
                try {
                    if(path.isPresent(args) || url.isPresent(args)) {
                        return -1;
                    }
                } catch (CommandFormatException e) {
                    return -1;
                }

                int nextCharIndex = 0;
                while (nextCharIndex < buffer.length()) {
                    if (!Character.isWhitespace(buffer.charAt(nextCharIndex))) {
                        break;
                    }
                    ++nextCharIndex;
                }

                if(ctx.getModelControllerClient() != null) {
                    List<String> deployments = Util.getDeployments(ctx.getModelControllerClient());
                    if(deployments.isEmpty()) {
                        return -1;
                    }

                    String opBuffer = buffer.substring(nextCharIndex).trim();
                    if (opBuffer.isEmpty()) {
                        candidates.addAll(deployments);
                    } else {
                        for(String name : deployments) {
                            if(name.startsWith(opBuffer)) {
                                candidates.add(name);
                            }
                        }
                        Collections.sort(candidates);
                    }
                    return nextCharIndex;
                } else {
                    return -1;
                }

            }}, "--name");
        name.addCantAppearAfter(l);
        path.addCantAppearAfter(name);
        url.addCantAppearAfter(name);

        rtName = new ArgumentWithValue(this, "--runtime-name");
        rtName.addRequiredPreceding(path);

        allServerGroups = new ArgumentWithoutValue(this, "--all-server-groups")  {
            @Override
            public boolean canAppearNext(CommandContext ctx) throws CommandFormatException {
                if(!ctx.isDomainMode()) {
                    return false;
                }
                return super.canAppearNext(ctx);
            }
        };

        allServerGroups.addRequiredPreceding(path);
        allServerGroups.addRequiredPreceding(name);
        allServerGroups.addCantAppearAfter(force);
        force.addCantAppearAfter(allServerGroups);

        serverGroups = new ArgumentWithValue(this, new CommaSeparatedCompleter() {
            @Override
            protected Collection<String> getAllCandidates(CommandContext ctx) {
                return Util.getServerGroups(ctx.getModelControllerClient());
            }}, "--server-groups") {
            @Override
            public boolean canAppearNext(CommandContext ctx) throws CommandFormatException {
                if(!ctx.isDomainMode()) {
                    return false;
                }
                return super.canAppearNext(ctx);
            }
        };
        serverGroups.addRequiredPreceding(path);
        serverGroups.addRequiredPreceding(name);
        serverGroups.addCantAppearAfter(force);
        force.addCantAppearAfter(serverGroups);

        serverGroups.addCantAppearAfter(allServerGroups);
        allServerGroups.addCantAppearAfter(serverGroups);

        disabled = new ArgumentWithoutValue(this, "--disabled");
        disabled.addRequiredPreceding(path);
        disabled.addCantAppearAfter(serverGroups);
        disabled.addCantAppearAfter(allServerGroups);
        disabled.addCantAppearAfter(force);
        force.addCantAppearAfter(disabled);

        unmanaged = new ArgumentWithoutValue(this, "--unmanaged");
        unmanaged.addRequiredPreceding(path);

        script = new ArgumentWithValue(this, "--script");
        script.addRequiredPreceding(path);
    }

    @Override
    protected void doHandle(CommandContext ctx) throws CommandLineException {

        final ModelControllerClient client = ctx.getModelControllerClient();

        ParsedCommandLine args = ctx.getParsedCommandLine();
        boolean l = this.l.isPresent(args);
        if (!args.hasProperties() || l) {
            listDeployments(ctx, l);
            return;
        }

        final boolean unmanaged = this.unmanaged.isPresent(args);

        final String path = this.path.getValue(args);
        final File f;
        if(path != null) {
            f = new File(path);
            if(!f.exists()) {
                throw new CommandFormatException("Path " + f.getAbsolutePath() + " doesn't exist.");
            }
            if(!unmanaged && f.isDirectory()) {
                throw new CommandFormatException(f.getAbsolutePath() + " is a directory.");
            }
        } else {
            f = null;
        }

        final URL deploymentUrl;
        final String urlString = this.url.getValue(args);
        if(urlString != null) {
            if(f != null) {
                throw new CommandFormatException("Either one of the filesystem path or --url argument can be specified at a time.");
            }
            try {
                deploymentUrl = new URL(urlString);
            } catch (MalformedURLException e) {
                throw new CommandFormatException("Failed to parse URL", e);
            }
        } else {
            deploymentUrl = null;
        }

        if (isCliArchive(f)) {
            final ModelNode request = buildRequestWOValidation(ctx);
            if(request == null) {
                throw new CommandFormatException("Operation request wasn't built.");
            }

            // if script had no server-side commands, just return
            if (!request.get("steps").isDefined()) return;

            try {
                final ModelNode result = client.execute(request);
                if(Util.isSuccess(result)) {
                    return;
                } else {
                    throw new CommandFormatException("Failed to execute archive script: " + Util.getFailureDescription(result));
                }
            } catch (IOException e) {
                throw new CommandFormatException("Failed to execute archive script: " + e.getLocalizedMessage(), e);
            }
        }

        String name = this.name.getValue(args);
        if(name == null) {
            if(f == null) {
                if(deploymentUrl == null) {
                    throw new CommandFormatException("Filesystem path, --url or --name is required.");
                } else {
                    name = deploymentUrl.getPath();
                }
            } else {
                name = f.getName();
            }
        }

        final String runtimeName = rtName.getValue(args);

        final boolean force = this.force.isPresent(args);
        final boolean disabled = this.disabled.isPresent(args);
        final String serverGroups = this.serverGroups.getValue(args);
        final boolean allServerGroups = this.allServerGroups.isPresent(args);

        if(force) {
            if(disabled || serverGroups != null || allServerGroups) {
                throw new CommandFormatException(this.force.getFullName() +
                        " only replaces the content in the deployment repository and can't be used in combination with any of " +
                        this.disabled.getFullName() + ", " + this.serverGroups.getFullName() + " or " + this.allServerGroups.getFullName() + '.');
            }

            if(Util.isDeploymentInRepository(name, client)) {
                replaceDeployment(ctx, f, deploymentUrl, name, runtimeName);
                return;
            } else if(ctx.isDomainMode()) {
                // add deployment to the repository (enabled in standalone, disabled in domain (i.e. not associated with any sg))
                final ModelNode request = buildAddRequest(ctx, f, deploymentUrl, name, runtimeName, unmanaged);
                execute(ctx, request, f, unmanaged);
                return;
            }
            // standalone mode will add and deploy
        }

        if(disabled) {
            if(serverGroups != null || allServerGroups) {
                throw new CommandFormatException(this.serverGroups.getFullName() + " and " + this.allServerGroups.getFullName() +
                        " can't be used in combination with " + this.disabled.getFullName() + '.');
            }

            if(Util.isDeploymentInRepository(name, client)) {
                throw new CommandFormatException("'" + name + "' already exists in the deployment repository (use " +
                this.force.getFullName() + " to replace the existing content in the repository).");
            }

            // add deployment to the repository disabled
            final ModelNode request = buildAddRequest(ctx, f, deploymentUrl, name, runtimeName, unmanaged);
            execute(ctx, request, f, unmanaged);
            return;
        }

        // actually, the deployment is added before it is deployed
        // but this code here is to validate arguments and not to add deployment if something is wrong
        final ModelNode deployRequest;
        if(ctx.isDomainMode()) {
            final List<String> sgList;
            if(allServerGroups) {
                if(serverGroups != null) {
                    throw new CommandFormatException(this.serverGroups.getFullName() + " can't appear in the same command with " + this.allServerGroups.getFullName());
                }
                sgList = Util.getServerGroups(client);
                if(sgList.isEmpty()) {
                    throw new CommandFormatException("No server group is available.");
                }
            } else if(serverGroups == null) {
                final StringBuilder buf = new StringBuilder();
                buf.append("One of ");
                if(f != null || deploymentUrl != null) {
                    buf.append(this.disabled.getFullName()).append(", ");
                }
                buf.append(this.allServerGroups.getFullName() + " or " + this.serverGroups.getFullName() + " is missing.");
                throw new CommandFormatException(buf.toString());
            } else {
                sgList = Arrays.asList(serverGroups.split(","));
                if(sgList.isEmpty()) {
                    throw new CommandFormatException("Couldn't locate server group name in '" + this.serverGroups.getFullName() + "=" + serverGroups + "'.");
                }
            }

            deployRequest = new ModelNode();
            deployRequest.get(Util.OPERATION).set(Util.COMPOSITE);
            deployRequest.get(Util.ADDRESS).setEmptyList();
            ModelNode steps = deployRequest.get(Util.STEPS);
            for (String serverGroup : sgList) {
                steps.add(Util.configureDeploymentOperation(Util.ADD, name, serverGroup));
            }
            for (String serverGroup : sgList) {
                steps.add(Util.configureDeploymentOperation(Util.DEPLOY, name, serverGroup));
            }
        } else {
            if(serverGroups != null || allServerGroups) {
                throw new CommandFormatException(this.serverGroups.getFullName() + " and " + this.allServerGroups.getFullName() +
                        " can't appear in standalone mode.");
            }
            deployRequest = new ModelNode();
            deployRequest.get(Util.OPERATION).set(Util.DEPLOY);
            deployRequest.get(Util.ADDRESS, Util.DEPLOYMENT).set(name);
        }

        if(f != null || deploymentUrl != null) {
            if(Util.isDeploymentInRepository(name, client)) {
                throw new CommandFormatException("'" + name + "' already exists in the deployment repository (use " +
                this.force.getFullName() + " to replace the existing content in the repository).");
            }
            final ModelNode request = new ModelNode();
            request.get(Util.OPERATION).set(Util.COMPOSITE);
            request.get(Util.ADDRESS).setEmptyList();
            final ModelNode steps = request.get(Util.STEPS);
            steps.add(buildAddRequest(ctx, f, deploymentUrl, name, runtimeName, unmanaged));
            steps.add(deployRequest);
            execute(ctx, request, f, unmanaged);
            return;
        } else if(!Util.isDeploymentInRepository(name, client)) {
            throw new CommandFormatException("'" + name + "' is not found among the registered deployments.");
        }

        try {
            final ModelNode result = client.execute(deployRequest);
            if (!Util.isSuccess(result)) {
                throw new CommandFormatException(Util.getFailureDescription(result));
            }
        } catch (IOException e) {
            throw new CommandFormatException("Failed to deploy", e);
        }
    }

    @Override
    public ModelNode buildRequestWithoutHeaders(CommandContext ctx) throws CommandFormatException {

        final ModelControllerClient client = ctx.getModelControllerClient();

        ParsedCommandLine args = ctx.getParsedCommandLine();
        boolean l = this.l.isPresent(args);
        if (!args.hasProperties() || l) {
            throw new OperationFormatException("Command is missing arguments for non-interactive mode: '" + args.getOriginalLine() + "'.");
        }

        final boolean unmanaged = this.unmanaged.isPresent(args);

        final String path = this.path.getValue(args);
        final File f;
        if(path != null) {
            f = new File(path);
            if(!f.exists()) {
                throw new OperationFormatException("Path " + f.getAbsolutePath() + " doesn't exist.");
            }
            if(!unmanaged && f.isDirectory()) {
                throw new OperationFormatException(f.getAbsolutePath() + " is a directory.");
            }
        } else {
            f = null;
        }

        String name = this.name.getValue(args);
        if(name == null) {
            if(f == null) {
                throw new OperationFormatException("Either path or --name is required.");
            }
            name = f.getName();
        }

        final String runtimeName = rtName.getValue(args);

        final boolean force = this.force.isPresent(args);
        final boolean disabled = this.disabled.isPresent(args);
        final String serverGroups = this.serverGroups.getValue(args);
        final boolean allServerGroups = this.allServerGroups.isPresent(args);
        final boolean archive = isCliArchive(f);

        if(force) {
            if(f == null) {
                throw new OperationFormatException(this.force.getFullName() + " requires a filesystem path of the deployment to be added to the deployment repository.");
            }
            if(disabled || serverGroups != null || allServerGroups) {
                throw new OperationFormatException(this.force.getFullName() +
                        " only replaces the content in the deployment repository and can't be used in combination with any of " +
                        this.disabled.getFullName() + ", " + this.serverGroups.getFullName() + " or " + this.allServerGroups.getFullName() + '.');
            }
            if (archive) {
                throw new OperationFormatException(this.force.getFullName() + " can't be used in combination with a CLI archive.");
            }

            if(Util.isDeploymentInRepository(name, client)) {
                // in batch mode these kind of checks might be inaccurate
                // because the previous commands and operations are not taken into account
                return buildDeploymentReplace(f, name, runtimeName);
            } else {
                // add deployment to the repository (enabled in standalone, disabled in domain (i.e. not associated with any sg))
                return buildDeploymentAdd(f, name, runtimeName, unmanaged);
            }
        }

        if(disabled) {
            if(f == null) {
                throw new OperationFormatException(this.disabled.getFullName() +
                        " requires a filesystem path of the deployment to be added to the deployment repository.");
            }

            if(serverGroups != null || allServerGroups) {
                throw new OperationFormatException(this.serverGroups.getFullName() + " and " + this.allServerGroups.getFullName() +
                        " can't be used in combination with " + this.disabled.getFullName() + '.');
            }

            if (archive) {
                throw new OperationFormatException(this.disabled.getFullName() + " can't be used in combination with a CLI archive.");
            }

            if(!ctx.isBatchMode() && Util.isDeploymentInRepository(name, client)) {
                throw new OperationFormatException("'" + name + "' already exists in the deployment repository (use " +
                    this.force.getFullName() + " to replace the existing content in the repository).");
            }

            // add deployment to the repository disabled
            return buildDeploymentAdd(f, name, runtimeName, unmanaged);
        }

        if (archive) {
            if(serverGroups != null || allServerGroups) {
                throw new OperationFormatException(this.serverGroups.getFullName() + " and " + this.allServerGroups.getFullName() +
                        " can't be used in combination with a CLI archive.");
            }

            TempFileProvider tempFileProvider;
            MountHandle root;
            try {
                tempFileProvider = TempFileProvider.create("cli", Executors.newSingleThreadScheduledExecutor());
                root = extractArchive(f, tempFileProvider);
            } catch (IOException e) {
                throw new OperationFormatException("Unable to extract archive '" + f.getAbsolutePath() + "' to temporary location");
            }

            final File currentDir = ctx.getCurrentDir();
            ctx.setCurrentDir(root.getMountSource());
            String holdbackBatch = activateNewBatch(ctx);

            try {
                String script = this.script.getValue(args);
                if (script == null) {
                    script = "deploy.scr";
                }

                File scriptFile = new File(ctx.getCurrentDir(),script);
                if (!scriptFile.exists()) {
                    throw new CommandFormatException("ERROR: script '" + script + "' not found.");
                }

                BufferedReader reader = null;
                try {
                    reader = new BufferedReader(new FileReader(scriptFile));
                    String line = reader.readLine();
                    while (!ctx.isTerminated() && line != null) {
                        ctx.handle(line);
                        line = reader.readLine();
                    }
                } catch (FileNotFoundException e) {
                    throw new CommandFormatException("ERROR: script '" + script + "' not found.");
                } catch (IOException e) {
                    throw new CommandFormatException("Failed to read the next command from " + scriptFile.getName() + ": " + e.getMessage(), e);
                } catch (CommandLineException e) {
                    throw new CommandFormatException(e.getMessage(), e);
                } finally {
                    if(reader != null) {
                        try {
                            reader.close();
                        } catch (IOException e) {
                        }
                    }
                }

                return ctx.getBatchManager().getActiveBatch().toRequest();
            } finally {
                // reset current dir in context
                ctx.setCurrentDir(currentDir);
                discardBatch(ctx, holdbackBatch);

                VFSUtils.safeClose(root, tempFileProvider);
            }
        }

        // actually, the deployment is added before it is deployed
        // but this code here is to validate arguments and not to add deployment if something is wrong
        final ModelNode deployRequest;
        if(ctx.isDomainMode()) {
            final List<String> sgList;
            if(allServerGroups) {
                if(serverGroups != null) {
                    throw new OperationFormatException(this.serverGroups.getFullName() + " can't appear in the same command with " + this.allServerGroups.getFullName());
                }
                sgList = Util.getServerGroups(client);
                if(sgList.isEmpty()) {
                    throw new OperationFormatException("No server group is available.");
                }
            } else if(serverGroups == null) {
                final StringBuilder buf = new StringBuilder();
                buf.append("One of ");
                if(f != null) {
                    buf.append(this.disabled.getFullName()).append(", ");
                }
                buf.append(this.allServerGroups.getFullName() + " or " + this.serverGroups.getFullName() + " is missing.");
                throw new OperationFormatException(buf.toString());
            } else {
                sgList = Arrays.asList(serverGroups.split(","));
                if(sgList.isEmpty()) {
                    throw new OperationFormatException("Couldn't locate server group name in '" + this.serverGroups.getFullName() + "=" + serverGroups + "'.");
                }
            }

            deployRequest = new ModelNode();
            deployRequest.get(Util.OPERATION).set(Util.COMPOSITE);
            deployRequest.get(Util.ADDRESS).setEmptyList();
            ModelNode steps = deployRequest.get(Util.STEPS);
            for (String serverGroup : sgList) {
                steps.add(Util.configureDeploymentOperation(Util.ADD, name, serverGroup));
            }
            for (String serverGroup : sgList) {
                steps.add(Util.configureDeploymentOperation(Util.DEPLOY, name, serverGroup));
            }
        } else {
            if(serverGroups != null || allServerGroups) {
                throw new OperationFormatException(this.serverGroups.getFullName() + " and " + this.allServerGroups.getFullName() +
                        " can't appear in standalone mode.");
            }
            deployRequest = new ModelNode();
            deployRequest.get(Util.OPERATION).set(Util.DEPLOY);
            deployRequest.get(Util.ADDRESS, Util.DEPLOYMENT).set(name);
        }

        final ModelNode addRequest;
        if(f != null) {
            if(!ctx.isBatchMode() && Util.isDeploymentInRepository(name, client)) {
                throw new OperationFormatException("'" + name + "' already exists in the deployment repository (use " +
                    this.force.getFullName() + " to replace the existing content in the repository).");
            }
            addRequest = this.buildDeploymentAdd(f, name, runtimeName, unmanaged);
        } else if(!ctx.isBatchMode() && !Util.isDeploymentInRepository(name, client)) {
            throw new OperationFormatException("'" + name + "' is not found among the registered deployments.");
        } else {
            addRequest = null;
        }

        if(addRequest != null) {
            final ModelNode composite = new ModelNode();
            composite.get(Util.OPERATION).set(Util.COMPOSITE);
            composite.get(Util.ADDRESS).setEmptyList();
            final ModelNode steps = composite.get(Util.STEPS);
            steps.add(addRequest);
            steps.add(deployRequest);
            return composite;
        }
        return deployRequest;
    }

    protected ModelNode buildDeploymentReplace(final File f, String name, String runtimeName) throws OperationFormatException {
        final ModelNode request = new ModelNode();
        request.get(Util.OPERATION).set(Util.FULL_REPLACE_DEPLOYMENT);
        request.get(Util.NAME).set(name);
        if(runtimeName != null) {
            request.get(Util.RUNTIME_NAME).set(runtimeName);
        }

        byte[] bytes = Util.readBytes(f);
        request.get(Util.CONTENT).get(0).get(Util.BYTES).set(bytes);
        return request;
    }

    protected ModelNode buildDeploymentAdd(final File f, String name, String runtimeName, boolean unmanaged) throws OperationFormatException {
        final ModelNode request = new ModelNode();
        request.get(Util.OPERATION).set(Util.ADD);
        request.get(Util.ADDRESS, Util.DEPLOYMENT).set(name);
        if (runtimeName != null) {
            request.get(Util.RUNTIME_NAME).set(runtimeName);
        }
        if(unmanaged) {
            final ModelNode content = request.get(Util.CONTENT).get(0);
            content.get(Util.PATH).set(f.getAbsolutePath());
            content.get(Util.ARCHIVE).set(f.isFile());
        } else {
            byte[] bytes = Util.readBytes(f);
            request.get(Util.CONTENT).get(0).get(Util.BYTES).set(bytes);
        }
        return request;
    }

    protected void execute(CommandContext ctx, ModelNode request, File f, boolean unmanaged) throws CommandFormatException {

        addHeaders(ctx, request);

        ModelNode result;
        try {
            if(!unmanaged && f != null) {
                OperationBuilder op = new OperationBuilder(request);
                op.addFileAsAttachment(f);
                request.get(Util.CONTENT).get(0).get(Util.INPUT_STREAM_INDEX).set(0);
                Operation operation = op.build();
                result = ctx.getModelControllerClient().execute(operation);
                operation.close();
            } else {
                result = ctx.getModelControllerClient().execute(request);
            }
        } catch (Exception e) {
            throw new CommandFormatException("Failed to add the deployment content to the repository: " + e.getLocalizedMessage());
        }
        if (!Util.isSuccess(result)) {
            throw new CommandFormatException(Util.getFailureDescription(result));
        }
    }

    protected ModelNode buildAddRequest(CommandContext ctx, final File f, final URL url, String name, final String runtimeName, boolean unmanaged) throws CommandFormatException {
        final ModelNode request = new ModelNode();
        request.get(Util.OPERATION).set(Util.ADD);
        request.get(Util.ADDRESS, Util.DEPLOYMENT).set(name);
        if (runtimeName != null) {
            request.get(Util.RUNTIME_NAME).set(runtimeName);
        }
        final ModelNode content = request.get(Util.CONTENT).get(0);
        if(f == null) {
            if(url == null) {
                addRequiresDeployment();
            }
            content.get(Util.URL).set(url.toExternalForm());
        } else {
            if (unmanaged) {
                content.get(Util.PATH).set(f.getAbsolutePath());
                content.get(Util.ARCHIVE).set(f.isFile());
            } else {
                content.get(Util.INPUT_STREAM_INDEX).set(0);
            }
        }
        return request;
    }

    protected void replaceDeployment(CommandContext ctx, final File f, final URL url, String name, final String runtimeName) throws CommandFormatException {
        // replace
        final ModelNode request = new ModelNode();
        request.get(Util.OPERATION).set(Util.FULL_REPLACE_DEPLOYMENT);
        request.get(Util.NAME).set(name);
        if(runtimeName != null) {
            request.get(Util.RUNTIME_NAME).set(runtimeName);
        }
        final ModelNode content = request.get(Util.CONTENT).get(0);
        if(f == null) {
            if(url == null) {
                forceRequiresDeployment();
            }
            content.get(Util.URL).set(url.toExternalForm());
        } else {
            content.get(Util.INPUT_STREAM_INDEX).set(0);
        }
        execute(ctx, request, f, false);
    }

    protected void addRequiresDeployment() throws CommandFormatException {
        throw new CommandFormatException("Filesystem path or --url pointing to the deployment is required.");
    }

    protected void forceRequiresDeployment() throws CommandFormatException {
        argumentRequiresDeployment(this.force);
    }

    protected void argumentRequiresDeployment(ArgumentWithoutValue arg) throws CommandFormatException {
        throw new CommandFormatException(arg.getFullName() + " requires a filesystem path or --url pointing to the deployment to be added to the repository.");
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

QDox parse error in file://<WORKSPACE>/data/code-rep-dataset/Dataset3/Tasks/10756.java