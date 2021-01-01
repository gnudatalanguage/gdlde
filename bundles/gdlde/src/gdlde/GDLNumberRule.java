package gdlde;

import org.eclipse.jface.text.rules.ICharacterScanner;
import org.eclipse.jface.text.rules.IRule;
import org.eclipse.jface.text.rules.IToken;
import org.eclipse.jface.text.rules.Token;

public class GDLNumberRule implements IRule {
    private IToken token;

    public GDLNumberRule(IToken token) {
        super();
        this.token = token;
    }

    public IToken evaluate(ICharacterScanner scanner)
    {
        char ch;

        ch = (char)scanner.read();
        if (!Character.isDigit(ch)) {
            scanner.unread();
            return Token.UNDEFINED;
        }
        do
        {
            ch = (char)scanner.read();
        } while (Character.isDigit(ch) || ch == 'b' || ch == 'd' || ch == 'e' || ch == 'l' || ch == 's' || ch == 'u');
        scanner.unread();
        return token;
    }
}
