/**
 *
 */
package it.danja.newsmonitor.templating;

import java.util.HashMap;
import java.util.Map;

import freemarker.template.Configuration;
import freemarker.template.Template;

/**
 * @author danny
 *
 */
public abstract class TemplateLoaderBase implements TemplateLoader {

    private Configuration configuration = new Configuration();
    private Map<String, Template> templateMap = new HashMap<String, Template>();

    public Configuration getConfiguration() {
        return configuration;
    }

    /* (non-Javadoc)
	 * @see it.danja.newsmonitor.standalone.templating.TemplateLoader#init()
     */
    @Override
    public void init() {
        configuration.setDefaultEncoding("UTF-8");
        loadTemplates();
    }

    public Map<String, Template> getTemplateMap() {
        return templateMap;
    }

    public Template getTemplate(String name) {
        return templateMap.get(name);
    }
}
