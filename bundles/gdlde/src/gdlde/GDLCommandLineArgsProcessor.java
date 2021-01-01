package gdlde;

import org.eclipse.core.filesystem.EFS;
import org.eclipse.core.filesystem.IFileStore;
import org.eclipse.core.runtime.IProgressMonitor;
import org.eclipse.core.runtime.IStatus;
import org.eclipse.core.runtime.Platform;
import org.eclipse.core.runtime.Status;
//import org.eclipse.swt.widgets.Event;
//import org.eclipse.swt.widgets.Listener;
import org.eclipse.ui.IStartup;
import org.eclipse.ui.IWorkbenchPage;
import org.eclipse.ui.PartInitException;
import org.eclipse.ui.PlatformUI;
import org.eclipse.ui.ide.IDE;
import org.eclipse.ui.progress.UIJob;

import java.io.File;

public class GDLCommandLineArgsProcessor implements IStartup {
    public void earlyStartup() {
        UIJob uiJob=new UIJob("open_file") {
            @Override
            public IStatus runInUIThread(IProgressMonitor monitor) {
                if (!PlatformUI.isWorkbenchRunning()) return Status.OK_STATUS;
                String[] args = Platform.getCommandLineArgs();
                for (String arg: args) {
                    if (!arg.startsWith("-")) {
                        File fileToOpen = new File(arg);
                        IFileStore fileStore = EFS.getLocalFileSystem().getStore(fileToOpen.toURI());
                        IWorkbenchPage page = PlatformUI.getWorkbench().getActiveWorkbenchWindow().getActivePage();
                        try {
                            IDE.openEditorOnFileStore(page, fileStore);
                        } catch (PartInitException e) {
                            e.printStackTrace();
                        }
                    }
                }
                return Status.OK_STATUS;
            }
        };
        uiJob.schedule();
    }
}
