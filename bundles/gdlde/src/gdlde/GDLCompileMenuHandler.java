package gdlde;

import org.eclipse.core.commands.AbstractHandler;
import org.eclipse.core.commands.ExecutionEvent;
import org.eclipse.core.commands.ExecutionException;
import org.eclipse.ui.IWorkbenchPage;
import org.eclipse.ui.PlatformUI;

public class GDLCompileMenuHandler extends AbstractHandler {
    @Override
    public Object execute(ExecutionEvent event) throws ExecutionException {
        try {
            IWorkbenchPage activePage = PlatformUI.getWorkbench().getActiveWorkbenchWindow().getActivePage();
            GDLEditor ed = (GDLEditor)activePage.getActiveEditor();
            if (!ed.isDirty() || activePage.saveEditor(ed, false)) {
                GDLCommunicatorSingleton comm = GDLCommunicatorSingleton.getInstance();
                comm.IssueCommand(".compile " + ed.getInputFilePath(), false);
            }
        } catch (NullPointerException e) {
            // Not found open file. Do nothing.
            e.printStackTrace();
        }
        return null;
    }
}
