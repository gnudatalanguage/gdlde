package gdlde;

import org.eclipse.swt.graphics.Color;
import org.eclipse.swt.graphics.RGB;
import org.eclipse.swt.widgets.Display;

import java.util.HashMap;
import java.util.Iterator;
import java.util.Map;

public class GDLColorProvider {
    public static final RGB COMMENT= new RGB(64, 128, 128);
    public static final RGB STRING= new RGB(255, 0, 0);
    public static final RGB NUMBER= new RGB(127, 127, 0);
    public static final RGB KEYWORD= new RGB(127, 0, 0);
    public static final RGB SYSTEMPRODECURE= new RGB(0, 0, 127);
    public static final RGB SYSTEMFUNCTION= new RGB(0, 0, 255);
    public static final RGB USERPROCDEURE= new RGB(0, 127, 127);
    public static final RGB USERFUNCTION= new RGB(0, 192, 192);
    public static final RGB TEXT= new RGB(0, 0, 0);

    protected Map<RGB, Color> fColorTable= new HashMap<RGB, Color>(10);

    public void dispose() {
        Iterator e= fColorTable.values().iterator();
        while (e.hasNext())
            ((Color) e.next()).dispose();
    }

    public Color getColor(RGB rgb) {
        Color color= (Color) fColorTable.get(rgb);
        if (color == null) {
            color= new Color(Display.getCurrent(), rgb);
            fColorTable.put(rgb, color);
        }
        return color;
    }
}