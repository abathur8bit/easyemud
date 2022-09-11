package com.axorion;

import org.luaj.vm2.Globals;
import org.luaj.vm2.LuaError;
import org.luaj.vm2.LuaValue;
import org.luaj.vm2.lib.jse.CoerceJavaToLua;
import org.luaj.vm2.lib.jse.JsePlatform;
import ssobjects.telnet.*;

import java.io.IOException;
import java.net.InetAddress;
import java.util.HashMap;
import java.util.NoSuchElementException;
import java.util.StringTokenizer;

public class EasyeMudServer extends TelnetServer {
    MudWorld mudWorld;
    char ESCAPE = 27;
    long lastIdle = 0;

    HashMap<TelnetServerSocket,LuaServerInterface> sockInterfaces = new HashMap<>();
    public EasyeMudServer(InetAddress host,int port,long idle) throws Exception {
        super(host,port,idle);
        mudWorld = new MudWorld();
    }

    @Override
    public void connectionAccepted(TelnetServerSocket sock) {
        System.out.println("New connection from ["+sock.getHostAddress()+"]");
        try {
            LuaServerInterface lsi = sockInterfaces.get(sock);
            if(lsi == null) {
                lsi = new LuaServerInterface(sock,mudWorld);
                sockInterfaces.put(sock,lsi);
            }
            Globals globals = JsePlatform.standardGlobals();
            lsi.globals = globals;
            LuaValue instance = CoerceJavaToLua.coerce(lsi);
            globals.set("sock",instance);    //obj is what lua will use to identify the object
            LuaValue chunk = globals.loadfile("scripts/login.lua");
            chunk.call();   //called so we can find lua functions
            globals.get("connected").call();
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
        TelnetServerSocket sock = msg.sock;
        String text = msg.text;
        try {
            LuaServerInterface lsi = sockInterfaces.get(sock);
            if(lsi == null) {
                sock.println("ERROR: No lsi");
                close(msg.sock);
            } else {
                lsi.globals.loadfile("scripts/login.lua").call();
                String cmd = "";
                try {
                    StringTokenizer tok = new StringTokenizer(text);
                    cmd = tok.nextToken();
                    if(cmd.equalsIgnoreCase("reset")) {
                        //specialty command
                        lsi.awaitHandler = null;
                        lsi.awaitingInput = false;
                        lsi.globals.get("connected").call();
                    } else {
                        if(lsi.awaitingInput) {
                            lsi.awaitingInput = false;
                            LuaValue handler = lsi.awaitHandler;
                            if(handler == null) {
                                handler = lsi.globals.get("response");
                            }
                            handler.call(text);
//                            lsi.globals.get(handler).call(text);
                        } else {
                            if(cmd.equalsIgnoreCase("cls")) {
                                clearScreen(msg);
                            } else {
                                sock.println("I don't understand");
                            }
                        }
                    }
                } catch(NoSuchElementException e) {
                    lsi.awaitHandler = null;
                    lsi.awaitingInput = false;
                    lsi.globals.get("connected").call();
                } catch(LuaError e) {
                    System.out.println("ERROR: Lua "+e.getMessage());
                    e.printStackTrace();
                }
            }
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
