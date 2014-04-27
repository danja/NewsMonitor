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

package org.apache.commons.feedparser;


/**
 * 
 * @author <a href="mailto:burton@apache.org">Kevin A. Burton (burtonator)</a>
 * @version $Id: ContentDetectorResult.java 373614 2006-01-30 22:31:21Z mvdb $
 */
public class ContentDetectorResult {

    /**
     * True if this is a feed (RSS or Atom)
     */
    public boolean isFeed = false;

    public boolean isRSS = false;
    public boolean isAtom = false;

    public boolean isHTML = false;
    
}
