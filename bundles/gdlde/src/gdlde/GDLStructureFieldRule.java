package gdlde;

import org.eclipse.jface.text.rules.ICharacterScanner;
import org.eclipse.jface.text.rules.IRule;
import org.eclipse.jface.text.rules.IToken;
import org.eclipse.jface.text.rules.Token;

public class GDLStructureFieldRule implements IRule {
    private IToken token;

    public GDLStructureFieldRule(IToken token) {
        super();
        this.token = token;
    }

    public IToken evaluate(ICharacterScanner scanner)
    {
        char ch;

        ch = (char)scanner.read();
        if (ch == '.') {
            scanner.unread(); scanner.unread();
            ch = (char)scanner.read();
            if (!Character.isDigit(ch) && !Character.isWhitespace(ch)) {
                scanner.read(); // skipping dot
                do {
                    ch = (char) scanner.read();
                } while (Character.isLetter(ch) || ch == '_');
                scanner.unread();
                return token;
            } else {
                return Token.UNDEFINED;
            }
        }
        scanner.unread();
        return Token.UNDEFINED;
    }
}
