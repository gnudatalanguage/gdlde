package gdlde;

import org.eclipse.jface.text.IDocument;
import org.eclipse.jface.text.presentation.IPresentationReconciler;
import org.eclipse.jface.text.presentation.PresentationReconciler;
import org.eclipse.jface.text.rules.DefaultDamagerRepairer;
import org.eclipse.jface.text.source.ISourceViewer;
import org.eclipse.jface.text.source.SourceViewerConfiguration;

public class GDLSourceViewerConfiguration extends SourceViewerConfiguration{
    private GDLCodeScanner scanner;

    protected GDLCodeScanner getTagScanner() {
        if (scanner == null) {
            GDLColorProvider cp = new GDLColorProvider();
            scanner = new GDLCodeScanner(cp);
        }
        return scanner;
    }

    public IPresentationReconciler getPresentationReconciler(ISourceViewer GDLSourceViewer) {
        PresentationReconciler reconciler = new PresentationReconciler();
        DefaultDamagerRepairer dr = new DefaultDamagerRepairer(getTagScanner());
        reconciler.setDamager(dr, IDocument.DEFAULT_CONTENT_TYPE);
        reconciler.setRepairer(dr, IDocument.DEFAULT_CONTENT_TYPE);
        return reconciler;
    }
}
