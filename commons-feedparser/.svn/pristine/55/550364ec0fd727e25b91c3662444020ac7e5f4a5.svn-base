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

import java.util.HashMap;
import java.util.Locale;
import java.util.StringTokenizer;

public class RFC3066LocaleParser {

    static HashMap locales = new HashMap();
    
    public static Locale parse( String input ) {

        StringTokenizer tok = new StringTokenizer( input, "-" );

        while ( tok.hasMoreElements() ) {
            String current = (String)tok.nextElement();

            current = current.toLowerCase();

            if ( current.length() == 2 ) {
                return getLocale( current );
            }
            
        }

        return null;

    }

    public static Locale getLocale( String language ) {
        return (Locale)locales.get( language );
    }
    
    private static void initLocale( String name ) {
        locales.put( name, new Locale( name ) );
    }
    
    static {

        // Taken from: http://www.w3.org/WAI/ER/IG/ert/iso639.htm
        
        initLocale( "aa" ); //afar"
        initLocale( "ab" ); //abkhazian"
        initLocale( "af" ); //afrikaans"
        initLocale( "am" ); //amharic"
        initLocale( "ar" ); //arabic"
        initLocale( "as" ); //assamese"
        initLocale( "ay" ); //aymara"
        initLocale( "az" ); //azerbaijani"
        initLocale( "ba" ); //bashkir"
        initLocale( "be" ); //byelorussian"
        initLocale( "bg" ); //bulgarian"
        initLocale( "bh" ); //bihari"
        initLocale( "bi" ); //bislama"
        initLocale( "bn" ); //bengali"" ); //bangla"
        initLocale( "bo" ); //tibetan"
        initLocale( "br" ); //breton"
        initLocale( "ca" ); //catalan"
        initLocale( "co" ); //corsican"
        initLocale( "cs" ); //czech"
        initLocale( "cy" ); //welsh"
        initLocale( "da" ); //danish"
        initLocale( "de" ); //german"
        initLocale( "dz" ); //bhutani"
        initLocale( "el" ); //greek"
        initLocale( "en" ); //english"" ); //american"
        initLocale( "eo" ); //esperanto"
        initLocale( "es" ); //spanish"
        initLocale( "et" ); //estonian"
        initLocale( "eu" ); //basque"
        initLocale( "fa" ); //persian"
        initLocale( "fi" ); //finnish"
        initLocale( "fj" ); //fiji"
        initLocale( "fo" ); //faeroese"
        initLocale( "fr" ); //french"
        initLocale( "fy" ); //frisian"
        initLocale( "ga" ); //irish"
        initLocale( "gd" ); //gaelic"" ); //scots gaelic"
        initLocale( "gl" ); //galician"
        initLocale( "gn" ); //guarani"
        initLocale( "gu" ); //gujarati"
        initLocale( "ha" ); //hausa"
        initLocale( "hi" ); //hindi"
        initLocale( "hr" ); //croatian"
        initLocale( "hu" ); //hungarian"
        initLocale( "hy" ); //armenian"
        initLocale( "ia" ); //interlingua"
        initLocale( "ie" ); //interlingue"
        initLocale( "ik" ); //inupiak"
        initLocale( "in" ); //indonesian"
        initLocale( "is" ); //icelandic"
        initLocale( "it" ); //italian"
        initLocale( "iw" ); //hebrew"
        initLocale( "ja" ); //japanese"
        initLocale( "ji" ); //yiddish"
        initLocale( "jw" ); //javanese"
        initLocale( "ka" ); //georgian"
        initLocale( "kk" ); //kazakh"
        initLocale( "kl" ); //greenlandic"
        initLocale( "km" ); //cambodian"
        initLocale( "kn" ); //kannada"
        initLocale( "ko" ); //korean"
        initLocale( "ks" ); //kashmiri"
        initLocale( "ku" ); //kurdish"
        initLocale( "ky" ); //kirghiz"
        initLocale( "la" ); //latin"
        initLocale( "ln" ); //lingala"
        initLocale( "lo" ); //laothian"
        initLocale( "lt" ); //lithuanian"
        initLocale( "lv" ); //latvian"" ); //lettish"
        initLocale( "mg" ); //malagasy"
        initLocale( "mi" ); //maori"
        initLocale( "mk" ); //macedonian"
        initLocale( "ml" ); //malayalam"
        initLocale( "mn" ); //mongolian"
        initLocale( "mo" ); //moldavian"
        initLocale( "mr" ); //marathi"
        initLocale( "ms" ); //malay"
        initLocale( "mt" ); //maltese"
        initLocale( "my" ); //burmese"
        initLocale( "na" ); //nauru"
        initLocale( "ne" ); //nepali"
        initLocale( "nl" ); //dutch"
        initLocale( "no" ); //norwegian"
        initLocale( "oc" ); //occitan"
        initLocale( "om" ); //oromo"" ); //afan"
        initLocale( "or" ); //oriya"
        initLocale( "pa" ); //punjabi"
        initLocale( "pl" ); //polish"
        initLocale( "ps" ); //pashto"" ); //pushto"
        initLocale( "pt" ); //portuguese"
        initLocale( "qu" ); //quechua"
        initLocale( "rm" ); //rhaeto-romance"
        initLocale( "rn" ); //kirundi"
        initLocale( "ro" ); //romanian"
        initLocale( "ru" ); //russian"
        initLocale( "rw" ); //kinyarwanda"
        initLocale( "sa" ); //sanskrit"
        initLocale( "sd" ); //sindhi"
        initLocale( "sg" ); //sangro"
        initLocale( "sh" ); //serbo-croatian"
        initLocale( "si" ); //singhalese"
        initLocale( "sk" ); //slovak"
        initLocale( "sl" ); //slovenian"
        initLocale( "sm" ); //samoan"
        initLocale( "sn" ); //shona"
        initLocale( "so" ); //somali"
        initLocale( "sq" ); //albanian"
        initLocale( "sr" ); //serbian"
        initLocale( "ss" ); //siswati"
        initLocale( "st" ); //sesotho"
        initLocale( "su" ); //sudanese"
        initLocale( "sv" ); //swedish"
        initLocale( "sw" ); //swahili"
        initLocale( "ta" ); //tamil"
        initLocale( "te" ); //tegulu"
        initLocale( "tg" ); //tajik"
        initLocale( "th" ); //thai"
        initLocale( "ti" ); //tigrinya"
        initLocale( "tk" ); //turkmen"
        initLocale( "tl" ); //tagalog"
        initLocale( "tn" ); //setswana"
        initLocale( "to" ); //tonga"
        initLocale( "tr" ); //turkish"
        initLocale( "ts" ); //tsonga"
        initLocale( "tt" ); //tatar"
        initLocale( "tw" ); //twi"
        initLocale( "uk" ); //ukrainian"
        initLocale( "ur" ); //urdu"
        initLocale( "uz" ); //uzbek"
        initLocale( "vi" ); //vietnamese"
        initLocale( "vo" ); //volapuk"
        initLocale( "wo" ); //wolof"
        initLocale( "xh" ); //xhosa"
        initLocale( "yo" ); //yoruba"
        initLocale( "zh" ); //chinese"
        initLocale( "zu" ); //Zulu"
    
    }
    
}