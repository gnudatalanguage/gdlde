package gdlde;

import org.eclipse.core.resources.IFile;
import org.eclipse.core.resources.ResourcesPlugin;
import org.eclipse.core.runtime.CoreException;
import org.eclipse.core.runtime.IPath;
import org.eclipse.core.runtime.Path;
import org.eclipse.jface.text.source.ISourceViewer;
import org.eclipse.jface.text.source.IVerticalRuler;
import org.eclipse.swt.widgets.Composite;
import org.eclipse.ui.IEditorInput;
import org.eclipse.ui.IFileEditorInput;
import org.eclipse.ui.editors.text.TextEditor;
import org.eclipse.ui.ide.FileStoreEditorInput;

public class GDLEditor extends TextEditor
{
    private IEditorInput input;

    @Override
    protected ISourceViewer createSourceViewer(Composite parent, IVerticalRuler ruler, int styles)
    {
        ISourceViewer viewer = new GDLSourceViewer(parent, ruler, getOverviewRuler(), isOverviewRulerVisible(), styles);

        // ensure decoration support has been created and configured.
        getSourceViewerDecorationSupport(viewer);

        //do any custom stuff
        return viewer;
    }

    @Override
    protected void doSetInput(final IEditorInput newInput) throws CoreException {
        super.doSetInput(newInput);
        this.input = newInput;
    }

    @Override
    protected void initializeEditor() {
        super.initializeEditor();
        setSourceViewerConfiguration(new GDLSourceViewerConfiguration());
    }

    protected IPath getInputFilePath(){
        IPath path = null;
        if (this.input instanceof IFileEditorInput) {
            final IFileEditorInput ife = (IFileEditorInput)this.input;
            path = ife.getFile().getFullPath();
        }
        else if (this.input instanceof FileStoreEditorInput) {
            final FileStoreEditorInput fsei = (FileStoreEditorInput)this.input;
            path = Path.fromOSString(fsei.getURI().getPath());
        }
        return path;
    }
}