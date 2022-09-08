package com.axorion;

import org.luaj.vm2.Globals;
import org.luaj.vm2.LuaValue;
import org.luaj.vm2.lib.jse.CoerceJavaToLua;
import org.luaj.vm2.lib.jse.JsePlatform;
import ssobjects.telnet.*;

import java.io.IOException;
import java.net.InetAddress;
import java.util.StringTokenizer;

class LuaServerInterface {
    String command;
    TelnetServerSocket sock;
    public LuaServerInterface(TelnetServerSocket sock) {
        this.sock = sock;
    }
    public void println(String msg) {
        print(msg+"\r\n");
    }
    public void print(String msg) {
        try {
            sock.print(msg);
        } catch(IOException e) {
            System.out.println("Unable to print to socket for script");
            e.printStackTrace();
        }
    }
}
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
            LuaServerInterface lsi = new LuaServerInterface(sock);
            Globals globals = JsePlatform.standardGlobals();
            LuaValue instance = CoerceJavaToLua.coerce(lsi);
            globals.set("sock",instance);    //obj is what lua will use to identify the object
            LuaValue chunk = globals.loadfile("login.lua");
            chunk.call();   //called so we can find lua functions
            LuaValue luaFunc = globals.get("connected");
            luaFunc.call();
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
