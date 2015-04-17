package gdlde;

import org.eclipse.jface.text.rules.IWhitespaceDetector;

public class GDLWhitespaceDetector implements IWhitespaceDetector {
    public boolean isWhitespace(final char ch) {
        return Character.isWhitespace(ch);
    }
}
