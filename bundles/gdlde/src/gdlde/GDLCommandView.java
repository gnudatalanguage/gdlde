package gdlde;

import org.eclipse.swt.SWT;
import org.eclipse.swt.events.KeyAdapter;
import org.eclipse.swt.events.KeyEvent;
import org.eclipse.swt.layout.GridData;
import org.eclipse.swt.layout.GridLayout;
import org.eclipse.swt.widgets.Composite;
import org.eclipse.swt.widgets.Label;
import org.eclipse.swt.widgets.Text;
import org.eclipse.ui.ISizeProvider;
import org.eclipse.ui.part.ViewPart;

public class GDLCommandView extends ViewPart implements ISizeProvider {
    @Override
    public void createPartControl(final Composite parent) {
        final GridLayout gridLayout;
        final Label label;
        final Text txtCommand;
        final GDLCommunicatorSingleton comm = GDLCommunicatorSingleton.getInstance();

        gridLayout = new GridLayout();
        gridLayout.numColumns = 2;

        parent.setLayout(gridLayout);

        label = new Label(parent, SWT.NONE);
        label.setSize(300,30);
        label.setText("GDL> ");

        txtCommand = new Text(parent, SWT.BORDER);
        txtCommand.setLayoutData(new GridData(GridData.FILL_HORIZONTAL));

        // Adding readline-like behavior
        txtCommand.addKeyListener(new KeyAdapter() {
            @Override
            public void keyPressed(KeyEvent e) {
                if (e.keyCode == SWT.CR || e.keyCode == SWT.KEYPAD_CR) {
                    String cmd = txtCommand.getText();
                    if (cmd != "") {
                        comm.IssueCommand(cmd, true);
                        txtCommand.setText("");
                    }
                    if (!cmd.toLowerCase().trim().equals("exit")) {
                        comm.cmd_history.add(cmd);
                        comm.cmd_history_pos = comm.cmd_history.size();
                    }
                    e.doit = false;
                } else if (e.keyCode == SWT.ARROW_UP) {
                    if (comm.cmd_history_pos > 0) {
                        comm.cmd_history_pos--;
                        txtCommand.setText(comm.cmd_history.get(comm.cmd_history_pos));
                    }
                    txtCommand.setSelection(txtCommand.getText().length());
                    e.doit = false;
                } else if (e.keyCode == SWT.ARROW_DOWN) {
                    if (comm.cmd_history_pos < comm.cmd_history.size() - 1) {
                        comm.cmd_history_pos++;
                        txtCommand.setText(comm.cmd_history.get(comm.cmd_history_pos));
                    } else if (comm.cmd_history_pos == comm.cmd_history.size() - 1) {
                        comm.cmd_history_pos++;
                        txtCommand.setText("");
                    }
                    txtCommand.setSelection(txtCommand.getText().length());
                    e.doit = false;
                }
            }
        });
    }

    @Override
    public void setFocus() {
    }

    // FIXME: Below doesn't work!
    @Override
    public int getSizeFlags(boolean width) {
        return SWT.WRAP;
    }

    // FIXME: Below doesn't work!
    @Override
    public int computePreferredSize(boolean width, int availableParallel, int availablePerpendicular, int preferredResult) {
        if (!width) return 100;
        return 0;
    }

}
