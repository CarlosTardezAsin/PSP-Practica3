import java.io.DataOutputStream;
import java.io.IOException;
import java.net.Socket;
import java.util.ArrayList;

public class ComunHilos {

    private ArrayList<String> history = new ArrayList<>();
    private ArrayList<Socket> sockets = new ArrayList<>();

    public void anadirMensaje(String msg) throws IOException {
        history.add(msg);

        for (Socket sc : sockets) {
            DataOutputStream out = new DataOutputStream(sc.getOutputStream());
            out.writeUTF(msg);
        }
    }

    public void showHistory() throws IOException {
        for (Socket sc : sockets) {
            for (String m : history) {
                DataOutputStream out = new DataOutputStream(sc.getOutputStream());
                out.writeUTF(m);
            }
        }
    }

    public synchronized void anadirClient(Socket client) throws IOException {

        DataOutputStream out = new DataOutputStream(client.getOutputStream());

        while (sockets.size() == 2) {
            try {
                out.writeUTF("*Actualmente el servidor esta lleno. Se encuentra en cola para poder unirse al chat*");
                wait();
            } catch (InterruptedException e) {
                e.printStackTrace();
            } 
        }

        sockets.add(client);
        out.writeUTF("*Se ha unido al chat, ya puede ver el historial y participar*");   
        notifyAll();
    }

    public synchronized void eliminarClient(Socket client) {
        sockets.remove(client);
        notifyAll();
    }
}