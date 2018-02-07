/*
 * Copyright 2018 danny.
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
package it.danja.newsmonitor.interpreters;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;

import java.util.regex.Matcher;
import java.util.regex.Pattern;
import java.util.regex.PatternSyntaxException;
import org.xml.sax.SAXException;

/**
 *
 * @author danny
 */
public class GenericParser extends FeedParserBase {

    private static Logger log = LoggerFactory.getLogger(GenericParser.class);

    public void parse(InputStream inputStream) {

        try {

            InputStreamReader in = new InputStreamReader(inputStream);

            // read contents into string builder
            StringBuilder input = new StringBuilder();
            int ch;
            while ((ch = in.read()) != -1) {
                input.append((char) ch);
            }

            // search for all occurrences of pattern
            String patternString = "\\s+href\\s*=\\s*(\"[^\"]*\"|[^\\s>]*)\\s*";

            Pattern pattern = Pattern.compile(patternString, Pattern.CASE_INSENSITIVE);
            Matcher matcher = pattern.matcher(input);

            // < href="https://aaroncourville.wordpress.com/author/aaroncourville/" >
            while (matcher.find()) {
                int start = matcher.start();
                int end = matcher.end();
                String match = input.substring(start, end);
                match = cleanup(match);
                log.info("in GenericParser, match = " + match);
                try {
                    getHandler().startElement(match, null, null, null);
                } catch (SAXException ex) {
                    // ignore
                }
            }
        } catch (IOException e) {
            e.printStackTrace();
        } catch (PatternSyntaxException e) {
            e.printStackTrace();
        }

    }

    /*
    String string = join(inputStream);
   
    private String join(InputStream inputStream) {
        return new BufferedReader(new InputStreamReader(inputStream))
                .lines().collect(Collectors.joining("\n"));
    }
     */
    // < href="https://aaroncourville.wordpress.com/author/aaroncourville/" >
    private String cleanup(String match) {
        match = match.trim();
        try {
            match = match.substring(6, match.length() - 1);
        } catch (Exception e) {
            // ignore
        }
        return match;
    }
}
