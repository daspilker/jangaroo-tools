package net.jangaroo.exml.generator;

import freemarker.core.Environment;
import freemarker.template.Configuration;
import freemarker.template.DefaultObjectWrapper;
import freemarker.template.Template;
import freemarker.template.TemplateException;
import net.jangaroo.exml.api.Exmlc;
import net.jangaroo.exml.api.ExmlcException;
import net.jangaroo.exml.model.ConfigClass;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.OutputStreamWriter;
import java.io.Writer;

/**
 *
 */
public final class ExmlConfigClassGenerator {

  public ExmlConfigClassGenerator() {
  }

  public void generateClass(final ConfigClass configClass, File result) throws IOException, TemplateException {
    // Maybe even the directory does not exist.
    File targetPackageFolder = result.getAbsoluteFile().getParentFile();
    if(!targetPackageFolder.exists()) {
      //noinspection ResultOfMethodCallIgnored
      targetPackageFolder.mkdirs(); // NOSONAR
    }

    Writer writer = null;
    try {
      writer = new OutputStreamWriter(new FileOutputStream(result), Exmlc.OUTPUT_CHARSET);
      generateClass(configClass, writer);
    } finally {
      try {
        if (writer != null) {
          writer.close();
        }
      } catch (IOException e) {
        //never happen
      }
    }
  }

  private static void generateClass(final ConfigClass configClass, final Writer output) throws IOException, TemplateException {
    if (configClass.getSuperClassName() == null) {
      throw new ExmlcException("Config class " + configClass.getFullName() + "'s super class name is null!");
    }
    Configuration cfg = new Configuration();
    cfg.setClassForTemplateLoading(ConfigClass.class, "/");
    cfg.setObjectWrapper(new DefaultObjectWrapper());
    Template template = cfg.getTemplate("/net/jangaroo/exml/templates/exml_config_class.ftl");
    Environment env = template.createProcessingEnvironment(configClass, output);
    env.setOutputEncoding(Exmlc.OUTPUT_CHARSET);
    env.process();
  }

}
