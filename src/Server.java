import java.io.*;
import java.net.ServerSocket;
import java.net.Socket;
import java.util.Scanner;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

public class Server {
    private static PrintWriter controllerOut;
    private static PrintWriter uavOut;
    //Compile to bytecode version 8
    //To get it to run on linux, type: java -cp AutopilotCloudServer.jar Server
    /**
     * Runs the server. When a client connects, the server spawns a new thread to do
     * the servicing and immediately returns to listening. The application limits the
     * number of threads via a thread pool (otherwise millions of clients could cause
     * the server to run out of resources by allocating too many threads).
     */

    public static void main(String[] args) throws Exception {
        try (ServerSocket listener = new ServerSocket(5005)) {
            System.out.println("The server is now running...");
            ExecutorService pool = Executors.newFixedThreadPool(4);
            while (true) {
                pool.execute(new Client(listener.accept()));
            }
        }
    }

    private static class Client implements Runnable {
        private Socket socket;

        Client(Socket socket) {
            this.socket = socket;
        }

        @Override
        public void run() {
            System.out.println("Attempted connection from: " + socket);
            try {
                BufferedReader in = new BufferedReader(new InputStreamReader(socket.getInputStream()));
                PrintWriter out = new PrintWriter(socket.getOutputStream(), true);
                String line = "";

                while (true) {
                    if(line != null) {
                        line = in.readLine();
                        //System.out.println("Recieved: " + line + " from: " + socket);
                        System.out.println(line);
                        if (line.equals("client")) {
                            System.out.println("Controller Accepted!");
                            out.println("Connection Accepted!");
                            controllerOut = out;
                            controller(in);
                        }
                        if (line.equals("UAV")) {
                            System.out.println("UAV Accepted!");
                            out.println("Connection Accepted!");
                            uavOut = out;
                            uav(in);
                        }
                    }
                }
            } catch (Exception e) {
                System.out.println("Error:" + socket);
                e.printStackTrace();
            } finally {
                try { socket.close(); } catch (IOException e) {}
                System.out.println("Closed: " + socket);
            }
        }

        private void controller(BufferedReader in) throws IOException {
            String line = "";
            while(!line.equals("end"))
            {
                line = in.readLine();
                if(line != null)
                {
                    System.out.println("Recieved: \"" + line + "\" from the conroller client");
                    uavOut.println(line);
                }
            }
        }

        private void uav(BufferedReader in) throws IOException {
            String line = "";
            while(!line.equals("end"))
            {
                line = in.readLine();
                if(line != null)
                {
                    System.out.println("Recieved: \"" + line + "\" from the conroller client");
                    controllerOut.println(line);
                }
            }
        }
    }
}
