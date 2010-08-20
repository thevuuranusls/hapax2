import hapax.Template;
import hapax.TemplateDataDictionary;
import hapax.TemplateDictionary;
import hapax.TemplateException;
import hapax.TemplateLoader;
import hapax.TemplateResourceLoader;

public class HapaxTest {

	public static void main(String[] args) {
		TemplateDataDictionary top = TemplateDictionary.create();
		TemplateDataDictionary imports = top.addSection("imports");
		imports.addSection("import").setVariable("import_spec", "java.lang.Long");
		imports.addSection("import").setVariable("import_spec", "java.lang.String");
		imports.addSection("import").setVariable("import_spec", "java.lang.Double");

		try {
			TemplateLoader templateLoader = TemplateResourceLoader.create("");
			Template template = templateLoader.getTemplate("foo.xtm");
			String output = template.renderToString(top);
			System.out.println(output);
		} catch (TemplateException e) {
			e.printStackTrace();
		}

	}
}
