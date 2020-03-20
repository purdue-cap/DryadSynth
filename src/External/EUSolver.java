import java.util.*;
import java.io.*;

public class EUSolver extends ExternalSolver {

    String entryPath;
    public EUSolver(String entryPath) {
        this.entryPath = entryPath;
    }

    @Override
    public String solveEncoded(String problem, long timeout) throws Exception{
        this.running = true;
        File inputFile = File.createTempFile("sygus", ".sl");
        BufferedWriter bw = new BufferedWriter(new FileWriter(inputFile));
        bw.write(problem);
        bw.close();
        inputFile.deleteOnExit();

        Process process =
            Runtime.getRuntime().exec(this.entryPath + " " + inputFile.getAbsolutePath());
        Worker worker = new Worker(process);
        worker.start();
        try {
            if (timeout <= 0) {
                worker.join();
            } else {
                worker.join(timeout);
            }
            if (worker.exit == 0) {
                return worker.output;
            }
            throw new Exception();
        } catch(InterruptedException ex) {
            worker.interrupt();
            Thread.currentThread().interrupt();
            throw ex;
        } finally {
            this.running = false;
            process.destroyForcibly();
        }
    }

    private static class Worker extends Thread {
        private final Process process;
        private Integer exit;
        private String output;
        private Worker(Process process) {
            this.process = process;
            this.output = "";
        }
        public void run() {
            try { 
                String line;
                BufferedReader stdout =
                    new BufferedReader(new InputStreamReader(process.getInputStream()));
                while ((line = stdout.readLine()) != null) {
                    output += line;
                }
                stdout.close();
                exit = process.waitFor();
            } catch (InterruptedException ignore) {
                return;
            } catch (IOException ignore) {
                return;
            }
        }  
    }
}