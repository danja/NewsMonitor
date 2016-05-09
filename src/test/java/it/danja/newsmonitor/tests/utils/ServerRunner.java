/*
 * Copyright 2016 danny.
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
package it.danja.newsmonitor.tests.utils;

import it.danja.newsmonitor.utils.HttpServer;

/**
 *
 * @author danny
 */
public class ServerRunner {

    private final String url = "http://localhost:8088/test-data/atom-sample.xml";
    private final static String rootDir = "src/main/resources/META-INF/resources/static/newsmonitor";

    private static HttpServer server = new HttpServer(rootDir, 8088);

    public static void main(String[] args) {
        server.init();
        server.start();

        while (true) {
            Thread.yield();
        }

    }
}
