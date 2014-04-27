/*
 * Copyright 1999,2004 The Apache Software Foundation.
 * 
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 * 
 *      http://www.apache.org/licenses/LICENSE-2.0
 * 
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */

package org.apache.commons.feedparser.locate;


/**
 *
 * @author <a href="mailto:burton@openprivacy.org">Kevin A. Burton</a>
 */
public interface AnchorParserListener {

    public void setContext( Object context );

    /**
     * Return a result if necessary;
     *
     * 
     */
    public Object getResult();

    /**
     * Called when the AnchorParser finds an Anchor.  Return false if you wish
     * to stop parsing.
     *
     * FIXME: Pass a fourth attribute that is the body of the anchor here.
     * 
     * 
     */
    public boolean onAnchor( String href, String rel, String title )
        throws AnchorParserException;

}
