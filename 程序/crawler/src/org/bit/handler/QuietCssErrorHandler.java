package org.bit.handler;
import org.w3c.css.sac.CSSException;
import org.w3c.css.sac.CSSParseException;
import com.gargoylesoftware.htmlunit.DefaultCssErrorHandler;

public class QuietCssErrorHandler extends DefaultCssErrorHandler{

	@Override
	public void error(CSSParseException exception) {
		super.error(exception);
	}

	@Override
	public void fatalError(CSSParseException exception) {
		super.fatalError(exception);
	}

	@Override
	public void warning(CSSParseException exception) {
		super.warning(exception);
	}

}
