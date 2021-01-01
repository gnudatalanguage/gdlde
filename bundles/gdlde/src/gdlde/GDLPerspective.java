package gdlde;

import org.eclipse.ui.IPageLayout;
import org.eclipse.ui.IPerspectiveFactory;

public class GDLPerspective implements IPerspectiveFactory {
  public void createInitialLayout(IPageLayout layout) {
      GDLCommunicatorSingleton.getInstance();
  }
}