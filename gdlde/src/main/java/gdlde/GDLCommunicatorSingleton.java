package gdlde;

import org.eclipse.ui.console.*;

import java.io.*;
import java.util.ArrayList;

public class GDLCommunicatorSingleton {
    private OutputStream procOut;
    public ArrayList<String> cmd_history = new ArrayList<String> ();
    public int cmd_history_pos = 0;
    ProcessBuilder pb;
    Process process;
    private MessageConsoleStream outputConsole;

    private void startGDL() {
        try {
            String gdl_home = System.getenv().get("GDL_HOME"); // May be defined on Windows Only
            if (gdl_home == null) gdl_home = "gdl";
            else {
                if (gdl_home.startsWith("+")) gdl_home = gdl_home.substring(1);
                gdl_home += File.separator + "bin" + File.separator + "gdl";
            }
            pb = new ProcessBuilder(gdl_home, "-gdlde").redirectErrorStream(true);
            process = pb.start();
            this.procOut = process.getOutputStream();
            final StreamBoozer seInfo = new StreamBoozer(process.getInputStream(), outputConsole);
            seInfo.start();

            new Thread() {
                @Override
                public void run() {
                    try {
                        process.waitFor();
                        try {
                            outputConsole.write(("GDL Process is stopped. Restarting..." +
                                    System.lineSeparator()).getBytes());
                        } catch (IOException e1) {
                            e1.printStackTrace();
                        }
                    } catch (InterruptedException e) {
                        try {
                            outputConsole.write(("GDL Process is interrupted. Restarting..." +
                                    System.lineSeparator()).getBytes());
                        } catch (IOException e1) {
                            e1.printStackTrace();
                        }
                    }
                    startGDL();
                };
            }.start();
        } catch (IOException e) {
            try {
                e.printStackTrace();
                outputConsole.write(("Cannot find GDL executable." + System.lineSeparator() +
                        "Please set GDL_HOME environment variable to home directory of GDL!").getBytes());
            } catch (IOException e1) {
                e1.printStackTrace();
            }
        }
    }

    private GDLCommunicatorSingleton() {
        IConsoleManager consoleManager = ConsolePlugin.getDefault().getConsoleManager();

        MessageConsole console = new MessageConsole("", null);
        consoleManager.addConsoles(new IConsole[]{console});
        consoleManager.showConsoleView(console);
        this.outputConsole = console.newMessageStream();
        startGDL();
    }

    private static class SingletonHolder {
        static final GDLCommunicatorSingleton single = new GDLCommunicatorSingleton();
    }

    public static GDLCommunicatorSingleton getInstance() {
        return SingletonHolder.single;
    }

    public void IssueCommand(String cmd, boolean echo) {
        if (this.procOut != null) {
            try {
                if (echo)
                    this.outputConsole.write(("GDL> " + cmd + System.lineSeparator()).getBytes());
                this.procOut.write((cmd + System.lineSeparator()).getBytes());
                this.procOut.flush();
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
    }

    class StreamBoozer extends Thread {
        private InputStream in;
        private OutputStream out;

        StreamBoozer(InputStream in, OutputStream out) {
            this.in = in;
            this.out = out;
        }

        @Override
        public void run() {
            BufferedReader br = null;
            try {
                br = new BufferedReader(new InputStreamReader(in));
                String line = null;
                while ((line = br.readLine()) != null) {
                    out.write((line + System.lineSeparator()).getBytes());
                }
            } catch (Exception e) {
                e.printStackTrace();
            } finally {
                try {
                    br.close();
                } catch (IOException e) {
                    startGDL();
                }
            }
        }
    }
}