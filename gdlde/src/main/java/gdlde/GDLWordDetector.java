package gdlde;

import org.eclipse.jface.text.rules.IWordDetector;

public class GDLWordDetector implements IWordDetector {
    public boolean isWordStart(final char ch) {
        return Character.isLetter(ch) || ch == '_';
    }

    public boolean isWordPart(final char ch) {
        return Character.isLetter(ch) || ch == '_';
    }
}
