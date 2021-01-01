package gdlde;

import org.eclipse.core.commands.AbstractHandler;
import org.eclipse.core.commands.ExecutionEvent;
import org.eclipse.core.commands.ExecutionException;
import org.eclipse.ui.IWorkbenchPage;
import org.eclipse.ui.PlatformUI;

public class GDLRunMenuHandler extends AbstractHandler {
    GDLCompileMenuHandler comp_handler = new GDLCompileMenuHandler();

    @Override
    public Object execute(ExecutionEvent event) throws ExecutionException {
        comp_handler.execute(event);
        IWorkbenchPage activePage = PlatformUI.getWorkbench().getActiveWorkbenchWindow().getActivePage();
        String name = (activePage.getActiveEditor().getEditorInput().getName().split("\\.(?=[^\\.]+$)"))[0];

        GDLCommunicatorSingleton comm = GDLCommunicatorSingleton.getInstance();
        comm.IssueCommand(name, false);
        return null;
    }
}
