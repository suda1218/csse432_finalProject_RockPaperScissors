package final_project;

import java.io.*;
import java.net.*;

public class Client {

	private Socket clientSocket;
	private PrintWriter out;
	private BufferedReader in;

	public void startConnection(String ip, int port) {
		
		System.out.println("Welcome to Rock Paper and Scissors Game! You are play from" + ip);
		System.out.print("prompt> ");
		System.out.println("Rock = R, Paper = P, Scissors = S, exit = e, cheat = C");
		System.out.println("Now enter your choice and let's play! >>>");
		
		try {
			clientSocket = new Socket(ip, port);
			out = new PrintWriter(clientSocket.getOutputStream(), true);
			in = new BufferedReader(new InputStreamReader(clientSocket.getInputStream()));
		} catch (UnknownHostException e) {
			e.printStackTrace();
		} catch (IOException e) {
			e.printStackTrace();
		}
	}

	public BufferedReader getIn() {
		return this.in;
	}

	public void sendMessage(String msg) {
		out.println(msg);
		return;
	}

	public void stopConnection(Thread th) {
		
		try {
			out.close();
			in.close();
			th.stop();
			System.out.print("Exit Game...Hope you stay safe and have a good one!");
			clientSocket.close();
		} catch (IOException e) {
			e.printStackTrace();
		}

	}

	public static void main(String[] args) {
		Client client = new Client();

		int port;
		String hostname = "";
		if (args.length > 2) {
			hostname = args[1];
			port = Integer.parseInt(args[2]);
		} else { // default
			hostname = "127.0.0.1";
			port = 9999;
		}

		client.startConnection(hostname, port);

		BufferedReader reader = new BufferedReader(new InputStreamReader(System.in));

		String input = "", response = "";

		Thread th = new Thread(new RunnableClient(client));
		th.start();

		while (true) {
			try {
				input = reader.readLine();
				client.sendMessage(input);
			} catch (IOException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}

			if ("e".contentEquals(input)) {
				break;
			}
		}

		client.stopConnection(th);
	}
}

class RunnableClient implements Runnable {
	private Client client;

	public RunnableClient(Client client) {
		this.client = client;

	}

	@Override
	public void run() {
		while (true) {
			String re;
			try {
				re = client.getIn().readLine();
				System.out.println(re);
			} catch (IOException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}

		}

	}
}

