/*
 * Copyright 2014 danny.
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
package it.danja.newsmonitor.osgi;

import static javax.ws.rs.core.MediaType.TEXT_HTML;
import javax.ws.rs.GET;
import javax.ws.rs.Path;
import javax.ws.rs.Produces;
import javax.ws.rs.core.Response;
import org.apache.felix.scr.annotations.Component;
import org.apache.felix.scr.annotations.Property;
import org.apache.felix.scr.annotations.Service;
//import org.apache.stanbol.commons.viewable.Viewable;
//import org.apache.stanbol.commons.web.base.resource.BaseStanbolResource;

@Component
@Service(WebUI.class) //{Object.class, 
@Property(name = "javax.ws.rs", boolValue = true)
@Path("newsmonitor")
public class WebUI {

     private static final String STATIC_RESOURCE_PATH = "/it/danja/newsmonitor/static";
     
    @GET
    @Produces(TEXT_HTML)
    @Path("views")
    public Response get() {
        return Response.ok("A TEST STRING", TEXT_HTML).build();
    }
}
