import java.io.*;
import java.net.*;

public class AtiendeClient extends Thread {

	private String usuario;
	private Socket client;
	private ComunHilos ch;

	public AtiendeClient(Socket client, ComunHilos ch) {
		this.usuario = "";
		this.client = client;
		this.ch = ch;
	}

	@Override
	public void run() {
		try {
			DataInputStream in = new DataInputStream(client.getInputStream());

			ch.anadirClient(client);

			ch.showHistory();

			usuario = in.readUTF();

			String msg = "[" + usuario + "] " + in.readUTF();

			while (!msg.equals("[" + usuario + "] " + "*")) {
				System.out.println(msg);
				ch.anadirMensaje(msg);
				msg = "[" + usuario + "] " + in.readUTF();
			}

			ch.eliminarClient(client);
			System.out.println(usuario + " desconectado");

		} catch (IOException e) {
			e.printStackTrace();
		}
	}
}