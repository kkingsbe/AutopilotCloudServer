import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.PrintWriter;
import java.net.InetAddress;
import java.net.Socket;
import java.util.Scanner;

public class test {
    public static void main(String[] args) throws IOException {
        Socket socket = new Socket(InetAddress.getLocalHost(), 5005);
        //Socket socket = new Socket("3.92.241.164", 5005);
        PrintWriter out = new PrintWriter(socket.getOutputStream(), true);
        BufferedReader in = new BufferedReader(new InputStreamReader(socket.getInputStream()));
        Scanner sc = new Scanner(System.in);

        out.println("client");
        while(true)
        {
            System.out.println(in.readLine());
            out.println(sc.nextLine());
        }
    }
}
