void aspectSettings( Parameters parameters, Configuration[] children )

/*
 * Copyright (C) The Apache Software Foundation. All rights reserved.
 *
 * This software is published under the terms of the Apache Software License
 * version 1.1, a copy of which has been included with this distribution in
 * the LICENSE file.
 */
package org.apache.myrmidon.aspects;

import org.apache.avalon.framework.configuration.Configuration;
import org.apache.avalon.framework.parameters.Parameters;
import org.apache.log.Logger;
import org.apache.myrmidon.api.Task;
import org.apache.myrmidon.api.TaskException;

/**
 * AspectHandler is the interface through which aspects are handled.
 *
 * @author <a href="mailto:conor@apache.org">Conor MacNeill</a>
 * @author <a href="mailto:donaldp@apache.org">Peter Donald</a>
 */
public interface AspectHandler
{
    Configuration preCreate( Configuration taskModel )
        throws TaskException;

    void aspect( Parameters parameters, Configuration[] children )
        throws TaskException;

    void postCreate( Task task )
        throws TaskException;

    void preLoggable( Logger logger )
        throws TaskException;

    void preConfigure( Configuration taskModel )
        throws TaskException;

    void preExecute()
        throws TaskException;

    void preDestroy()
        throws TaskException;

    boolean error( TaskException te )
        throws TaskException;
}