public TestContainer(MarkupContainer parent,String id)

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
package wicket.markup.html.basic;

import wicket.MarkupContainer;
import wicket.markup.html.WebMarkupContainer;
import wicket.markup.html.WebPage;


/**
 * Mock page for testing.
 *
 * @author Chris Turner
 */
public class SimplePage_9 extends WebPage 
{
	private static final long serialVersionUID = 1L;

	/**
	 * Construct.
	 */
	public SimplePage_9() 
	{
		add(new TestContainer(this,"test"));
	}

	/**
	 * 
	 */
	private static class TestContainer extends WebMarkupContainer
	{
		private static final long serialVersionUID = 1L;

		/**
		 * Construct.
		 * @param id
		 */
		public TestContainer(MarkupContainer<?> parent,String id)
		{
			super(parent,id);
			add(new Label(this,"test", "myTest"));
		}
	}
}