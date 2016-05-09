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

import it.danja.newsmonitor.main.Config;
import it.danja.newsmonitor.main.ConfigReader;
import java.io.IOException;
import java.util.Properties;

/**
 *
 * @author danny
 */
public class ConfigLoader {
    public static  Properties getConfig(){
                ConfigReader configReader = new ConfigReader();
        try {
            configReader.loadPropertiesFile(Config.CONFIG_PROPERTIES_STANDALONE_LOCATION);
        } catch (IOException e) {
            e.printStackTrace();
        }
        return configReader.getProperties();
    }
}
