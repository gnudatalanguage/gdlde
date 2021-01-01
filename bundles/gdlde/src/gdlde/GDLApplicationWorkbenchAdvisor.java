package gdlde;

import org.eclipse.ui.application.WorkbenchAdvisor;

public class GDLApplicationWorkbenchAdvisor extends WorkbenchAdvisor {
	private static final String PERSPECTIVE_ID = "gdlde.gdlperspective";

	public String getInitialWindowPerspectiveId() {
		return PERSPECTIVE_ID;
	}
}