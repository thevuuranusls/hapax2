import hapax.Template;
import hapax.TemplateDataDictionary;
import hapax.TemplateDictionary;
import hapax.TemplateException;
import hapax.TemplateResourceLoader;

public class Main {

	public static void main(String[] args) throws TemplateException {
		TemplateResourceLoader loader = new TemplateResourceLoader("");
		Template t = loader.getTemplate("template");

		TemplateDictionary dict = TemplateDictionary.create();
		TemplateDataDictionary menu = dict.addSection("menu");
		menu.addSection("menuitem").setVariable("number", "" + 1);
		menu.addSection("menuitem").setVariable("number", "" + 2);
		menu.addSection("submenu").addSection("template").addSection("menu")
				.addSection("menuitem").setVariable("number", "" + 3);

		System.out.println(t.renderToString(dict));
	}

}
