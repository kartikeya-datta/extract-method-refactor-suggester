public interface IResourceListener extends IRequestListener

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
package wicket;

/**
 * Listens for requests regarding resources. Such resources are typically non-static in
 * nature and may even be created on the fly.
 * <p>
 * One use for resource listeners is that they may be used to create components such as
 * Image which respond to requests with a localized resource loaded from the classpath.
 * The Image class allows reusable components to transparently bundle localized image
 * resources. Components that use Image resources (instead of referencing static images in
 * their markup) can be distributed as a self-contained JAR file.
 * @see wicket.markup.html.image.Image
 * @see wicket.markup.html.image.DynamicImage
 * @author Jonathan Locke
 */
public interface IResourceListener extends IListener
{ // TODO finalize javadoc
    /**
     * Called when a resource is requested.
     * @param cycle The request cycle
     */
    public void resourceRequested(RequestCycle cycle);
}

///////////////////////////////// End of File /////////////////////////////////