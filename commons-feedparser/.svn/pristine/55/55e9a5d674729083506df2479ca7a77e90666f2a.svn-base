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

package org.apache.commons.feedparser.tools;

import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Locale;
import java.util.TimeZone;

/**
 *
 * RFC 822 date parsing utility.  Designed for parsing the ISO subset used in
 * Dublin Core, RSS 1.0, and Atom.
 *
 * 
 http://asg.web.cmu.edu/rfc/rfc822.html#sec-5
 * 
 5.1 SYNTAX
 * 
 * date-time   =  [ day "," ] date time        ; dd mm yy
 * ;  hh:mm:ss zzz
 * 
 * day         =  "Mon"  / "Tue" /  "Wed"  / "Thu"
 * /  "Fri"  / "Sat" /  "Sun"
 * 
 * date        =  1*2DIGIT month 2DIGIT        ; day month year
 * ;  e.g. 20 Jun 82
 * 
 * month       =  "Jan"  /  "Feb" /  "Mar"  /  "Apr"
 * /  "May"  /  "Jun" /  "Jul"  /  "Aug"
 * /  "Sep"  /  "Oct" /  "Nov"  /  "Dec"
 * 
 * time        =  hour zone                    ; ANSI and Military
 * 
 * hour        =  2DIGIT ":" 2DIGIT [":" 2DIGIT]
 * ; 00:00:00 - 23:59:59
 * 
 * zone        =  "UT"  / "GMT"                ; Universal Time
 * ; North American : UT
 * /  "EST" / "EDT"                ;  Eastern:  - 5/ - 4
 * /  "CST" / "CDT"                ;  Central:  - 6/ - 5
 * /  "MST" / "MDT"                ;  Mountain: - 7/ - 6
 * /  "PST" / "PDT"                ;  Pacific:  - 8/ - 7
 * /  1ALPHA                       ; Military: Z = UT;
 * ;  A:-1; (J not used)
 * ;  M:-12; N:+1; Y:+12
 * / ( ("+" / "-") 4DIGIT )        ; Local differential
 * ;  hours+min. (HHMM)
 * 
 5.2 SEMANTICS
 * 
 * If included, day-of-week must be the day implied by the date specification.
 * 
 * Time zone may be indicated in several ways. "UT" is Univer- sal Time
 * (formerly called "Greenwich Mean Time"); "GMT" is per- mitted as a reference
 * to Universal Time. The military standard uses a single character for each
 * zone. "Z" is Universal Time. "A" indicates one hour earlier, and "M"
 * indicates 12 hours ear- lier; "N" is one hour later, and "Y" is 12 hours
 * later. The letter "J" is not used. The other remaining two forms are taken
 * from ANSI standard X3.51-1975. One allows explicit indication of the amount
 * of offset from UT; the other uses common 3-character strings for indicating
 * time zones in North America.
 * 
 * 
 * 
 * @author <a href="mailto:burton@apache.org">Kevin A. Burton (burtonator)</a>
 * @version $Id$
 */
public class RFC822DateParser {

    private static SimpleDateFormat df
        = new SimpleDateFormat( "EEE, dd MMM yyyy hh:mm:ss z", Locale.ENGLISH );

    public static Date parse( String input ) throws java.text.ParseException {

        //NOTE: SimpleDateFormat uses GMT[-+]hh:mm for the TZ which breaks
        //things a bit.  Before we go on we have to repair this.

        return df.parse( input );
        
    }

    public static String toString( Date date, TimeZone tz ) {

        df.setTimeZone( tz );

        return df.format( date );
        
    }
    
}

