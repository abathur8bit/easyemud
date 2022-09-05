package com.axorion;

import ssobjects.telnet.*;

import java.io.IOException;
import java.net.InetAddress;
import java.util.StringTokenizer;

public class EasyeMudServer extends TelnetServer {
    char ESCAPE = 27;
    long lastIdle = 0;

    public EasyeMudServer(InetAddress host,int port,long idle) throws Exception {
        super(host,port,idle);
    }
    @Override
    public void connectionAccepted(TelnetServerSocket sock) {
        System.out.println("New connection from ["+sock.getHostAddress()+"]");
        try {

            sock.println("Welcome");
            printlnAllExcept("Just joined "+sock.getHostAddress(),sock);
        } catch(Exception e) {
            e.printStackTrace();
        }
    }

    @Override
    public void connectionClosed(TelnetServerSocket sock,IOException e) {
        System.out.println("Closed connection from ["+sock.getHostAddress()+"]");
    }

    @Override
    public void processSingleMessage(TelnetMessage msg) {
        try {
            StringTokenizer tok = new StringTokenizer(msg.text);
            String cmd = tok.nextToken();
            if(cmd.equalsIgnoreCase("cls"))
                clearScreen(msg);

            msg.sock.print("What? ");

            printlnAll("got:"+msg.text+CRLF);
        } catch(IOException e) {
            System.out.println("ERROR got exception when sending data");
        }
    }

    @Override
    public void idle(long deltaTime) {
        long now = System.currentTimeMillis();
//        System.out.println("idle deltaTime/currentTime ["+deltaTime+"/"+(now-lastIdle)+"]");
        lastIdle = now;
//        try{Thread.sleep(1000);} catch(InterruptedException e){}
    }
    public void clearScreen(TelnetMessage msg) throws IOException
    {
        msg.sock.print(ESCAPE+"[2J");
    }

    public static void main(String[] args) throws Exception
    {
        int hostPort = 6660;
        EasyeMudServer server = new EasyeMudServer(null,hostPort,100);
        System.out.println("Running on port "+hostPort+"...");
        server.run();
    }
}
