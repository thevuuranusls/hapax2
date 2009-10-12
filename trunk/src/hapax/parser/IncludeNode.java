package hapax.parser;

import hapax.Iterator;
import hapax.Modifiers;
import hapax.Path;
import hapax.Template;
import hapax.TemplateDictionary;
import hapax.TemplateException;
import hapax.TemplateLoader;

import java.io.PrintWriter;
import java.io.StringWriter;
import java.text.MessageFormat;
import java.util.List;

/**
 * Represents an <code>{{&gt;<i>name</i>}}</code> include section.
 *
 * @author dcoker
 * @author jdp
 */
public final class IncludeNode
    extends TemplateNode
    implements TemplateNode.Section
{

    private final String name;

    final List<Modifiers.FLAGS> modifiers;


    IncludeNode(int lno, String spec) {
        super(lno);
        String split[] = spec.split(":");
        this.name = split[0];
        this.modifiers = Modifiers.parseModifiers(split);
    }


    public String getSectionName(){
        return this.name;
    }
    @Override
    public final void evaluate(TemplateDictionary dict, TemplateLoader context, PrintWriter out)
        throws TemplateException
    {
        String sectionName = this.name;

        List<TemplateDictionary> section = dict.getSection(sectionName);

        if (null != section){

            String filename = this.resolveName(dict);

            Template template = context.getTemplate(filename);
            if (null != template){
                /*
                 * Modified rendering
                 */
                PrintWriter previous_printwriter = null;
                StringWriter sw = null;
                if (!this.modifiers.isEmpty()) {
                    previous_printwriter = out;
                    sw = new StringWriter();
                    out = new PrintWriter(sw);
                }

                if (section.size() == 0) {

                    Iterator.Define(dict,sectionName,0,1);
                    /*
                     * Once
                     */
                    template.render(dict, out);
                }
                else {
                    /*
                     * Repeat
                     */
                    for (int cc = 0, count = section.size(); cc < count; cc++){

                        TemplateDictionary child = section.get(cc);

                        Iterator.Define(child,sectionName,cc,count);

                        template.render(child, out);
                    }
                }

                /*
                 */
                if (previous_printwriter != null) {
                    String results = sw.toString();
                    out = previous_printwriter;
                    out.write(Modifiers.applyModifiers(results, this.modifiers));
                }
            }
            else
                throw new TemplateException("Template not found '"+filename+"'.");
        }
    }

    private String resolveName(TemplateDictionary dict)
        throws TemplateException
    {
        String name = this.name;
        /*
         * When it's quoted, it's protected from redirect
         */
        String basename = TrimQuotes(name);

        if (name == basename){

            String redirect = dict.getVariable(name);

            if (null != redirect && 0 != redirect.length())
                return redirect;
        }
        return basename;
    }

    private final static String TrimQuotes(String string){

        if ('"' == string.charAt(0)) {
            int stringLen = string.length();
            if ('"' == string.charAt(stringLen-1))
                string = string.substring(1,stringLen-1);
            else
                string = string.substring(1);
        } 
        return string;
    }
}
