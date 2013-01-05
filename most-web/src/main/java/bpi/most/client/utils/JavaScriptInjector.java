package bpi.most.client.utils;

import com.google.gwt.dom.client.BodyElement;
import com.google.gwt.dom.client.Document;
import com.google.gwt.dom.client.ScriptElement;
import com.google.gwt.user.client.Element;

/**
 * 
 * The JavaScripInjector allows to inject javascript sourcefiles directly in the
 * sourcecode right before the end of the Body Tag. i.e. this is used in the 3 D
 * module to inject the Bim surfer
 * 
 * @author sg
 */
public class JavaScriptInjector {

	private static BodyElement body;

	public static void inject(String js) {
		BodyElement bodyElem = getBody();
		ScriptElement scriptElement = createNewScriptElement();
		scriptElement.setText(js);
		bodyElem.appendChild(scriptElement);
	}

	private static ScriptElement createNewScriptElement() {
		ScriptElement script = Document.get().createScriptElement();
		script.setAttribute("type", "text/javascript");

		return script;
	}

	private static BodyElement getBody() {
		if (body == null) {
			Element bodyElement = (Element) Document.get()
					.getElementsByTagName("body").getItem(0);
			assert bodyElement != null : "Error retrieving body";
			BodyElement bodyElem = BodyElement.as(bodyElement);
			JavaScriptInjector.body = bodyElem;
		}
		return JavaScriptInjector.body;
	}

}
