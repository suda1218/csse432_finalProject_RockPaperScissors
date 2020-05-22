package final_project;

import java.io.*;
import java.net.*;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Random;
import java.util.Scanner;

public class Server {

	private ServerSocket serverSocket;
	private Socket clientSocket;
	private ArrayList<BufferedReader> ins = new ArrayList<>();
	private ArrayList<PrintWriter> outs = new ArrayList<>();
	private ArrayList<Socket> clientSockets = new ArrayList<Socket>();
	private int port;

	public Server(int port) {
		super();
		this.port = port;
		try {
			serverSocket = new ServerSocket(this.port);

		} catch (IOException e) {
			e.printStackTrace();
		}

	}

	public ServerSocket getServerSocket() {
		return this.serverSocket;
	}

	public void start() throws IOException {
		int clientNumber = 0;

		Scanner in = new Scanner(System.in);

		System.out.println("Hi, here is the server.");

		while (true) {
			clientSocket = serverSocket.accept();
			if (clientSocket != null) {
				Thread th = new Thread(new RunnableServer(this.clientSockets, this.clientSocket, this.serverSocket,
						this.outs, this.ins, clientNumber++));
				th.start();
			}
		}

	}

	public void stop() {
		try {
			for (BufferedReader in : this.ins) {
				in.close();
			}
			for (PrintWriter out : this.outs) {
				out.close();
			}

			for (Socket s : this.clientSockets) {
				s.close();
			}
			serverSocket.close();
		} catch (IOException e) {
			e.printStackTrace();
		}
	}

	public static void main(String args[]) throws IOException {
		int port;

		if (args.length > 1)
			port = Integer.parseInt(args[1]);
		else
			port = 9999;

		Server server = new Server(port);
		server.start();
	}
}

class RunnableServer implements Runnable {

	private PrintWriter out;
	private BufferedReader in;
	private ServerSocket serverSocket;
	private Socket clientSocket;
	private ArrayList<Socket> clientSockets;
	private ArrayList<PrintWriter> outs;
	private ArrayList<BufferedReader> ins;
	private int clientNumber;

	public RunnableServer(ArrayList<Socket> clientSockets, Socket clientSocket, ServerSocket serverSocket,
			ArrayList<PrintWriter> outs, ArrayList<BufferedReader> ins, int clientNumber) {
		this.clientSocket = clientSocket;
		this.serverSocket = serverSocket;
		this.clientSockets = clientSockets;
		this.outs = outs;
		this.ins = ins;
		this.clientNumber = clientNumber;
	}

	@Override
	public void run() {
		// TODO Auto-generated method stub
		try {
			this.clientSockets.add(clientSocket);

			out = new PrintWriter(clientSocket.getOutputStream(), true);
			in = new BufferedReader(new InputStreamReader(clientSocket.getInputStream()));
			this.outs.add(out);
			this.ins.add(in);
		} catch (IOException e) {
			e.printStackTrace();
		}

		String lineFromClient = "";

		while (true) {
			try {
				lineFromClient = in.readLine();
			} catch (IOException e) {
				e.printStackTrace();
			}

			if ("C".contentEquals(lineFromClient)) {
				for (PrintWriter out : this.outs) {
					out.println("Oh the player from " + this.clientSocket.getInetAddress()
							+ " is trying to cheat! Shame on him/her!");
				}
			}

			if ("e".contentEquals(lineFromClient)) {
				break;
			}

			int result = play(lineFromClient);

			if (result == 0) {
				out.println("It's a tie");
			} else if (result == 1) {
				out.println("You win!");
			} else if (result == -1) {
				out.println("You lose!");
			}
			out.println();

		}
	}

	public int play(String line) {

		String myS = "";
		Random random = new Random();
		int first = random.nextInt(3);

		if (first == 0) {
			myS = "R";
		} else if (first == 1) {
			myS = "P";
		} else if (first == 2) {
			myS = "S";
		}

		if (myS.equals(line)) {
			return 0;
		} else if (line.equals("R")) {
			if (myS.equals("S")) {
				return 1;
			} else if (myS.equals("P")) {
				return -1;
			}
		} else if (line.equals("P")) {
			if (myS.equals("R")) {
				return 1;
			} else if (myS.equals("S")) {
				return -1;
			}
		} else if (line.equals("S")) {
			if (myS.equals("R")) {
				return -1;
			} else if (myS.equals("P")) {
				return 1;
			}
		}

		return -2;
	}

}

