getSettings().getDefaultPageFactory().setChildFactory(new GroovyPageFactory(this));

/*
 * $Id$
 * $Revision$
 * $Date$
 *
 * ====================================================================
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *  http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */
package wicket.examples.groovy;

import wicket.WebApplication;
import wicket.util.time.Duration;

/**
 * HttpApplication class for hello world example.
 * @author Jonathan Locke
 */
public class GroovyApplication extends WebApplication
{
    /**
     * Constructor.
     */
    public GroovyApplication()
    {
        // Must be set prior to loading a groovy file, as watching for changes 
        // of the groovy file does already use the value defined.
        getSettings().setResourcePollFrequency(Duration.ONE_SECOND);
        
        getSettings().getPageFactory().setChildFactory(new GroovyPageFactory(this));
        getSettings().setHomePage("wicket.examples.groovy.Page1");
    }
}

