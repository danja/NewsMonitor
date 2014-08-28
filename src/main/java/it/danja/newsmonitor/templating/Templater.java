/**
 * 
 */
package it.danja.newsmonitor.templating;

import it.danja.newsmonitor.discovery.LinkExplorer;

import java.io.IOException;
import java.io.StringWriter;
import java.io.Writer;
import java.util.Map;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import freemarker.template.Template;
import freemarker.template.TemplateException;

/**
 * @author danny
 * 
 */
public class Templater {

	private static Logger log = LoggerFactory.getLogger(LinkExplorer.class);

	private Map<String, Template> templateMap = null;

	public void setTemplateMap(Map<String, Template> templateMap) {
		this.templateMap = templateMap;
	}

	// TODO refactor, move to another class?
	public String apply(String templateName, Object dataModel) {
		// log.info("templateName = " + templateName);

		Writer writer = new StringWriter();
		try {
			Template template = templateMap.get(templateName);
			// log.info("template = " + template);
			// log.info("dataModel = " + dataModel);
			template.process(dataModel, writer);
			writer.close();
		} catch (TemplateException e) {
			log.error(e.getMessage());
		} catch (IOException e) {
			log.error(e.getMessage());
		}

		return writer.toString();
	}
}
