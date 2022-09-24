package com.axorion;

import org.luaj.vm2.Globals;
import org.luaj.vm2.LuaValue;
import ssobjects.telnet.TelnetServerSocket;

import java.io.IOException;

public class LuaServerInterface {
    String command;
    TelnetServerSocket sock;
    EasyeMudServer server;
    Globals globals;
    MudWorld world;
    boolean awaitingInput = false;
    public LuaValue awaitHandler = null;
    int mode = 1;   //0=dead, 1=playing     //todo should be an enum, but not sure how to best integreat enum with lua

    public LuaServerInterface(TelnetServerSocket sock,MudWorld world,EasyeMudServer server) {
        this.sock = sock;
        this.world = world;
        this.server = server;
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

    public int getMode() {return mode;}
    public void setMode(int m) {mode = m;}
    public void ask(String prompt,LuaValue handler) {
        print(prompt);
        awaitingInput = true;
        awaitHandler = handler;
    }

    public int roll(int min,int max) {
        return world.rnd(min,max);
    }

    public void moveWumpus() {
        world.moveWumpus();
    }
    public void spawnWorld() {
        world.spawn();
    }

    public int getPlayer(int offset) {
        return world.getPlayer()+offset;
    }

    public int getWumpus() {
        return world.getWumpus();
    }

    public int getBat(int n) {
        return world.getBat(n);
    }

    public int getPit(int n) {
        return world.getPit(n);
    }

    public int getWorld(int n) {
        return world.getWorld(n);
    }

    public boolean isWumpusNearby() {
        int[] exits = new int[3];
        connectingRooms(exits,world.getWumpus());
        return contains(world.getPlayer(),exits);
    }

    public boolean isPitNearby() {
        int[] rooms = new int[3];
        connectingRooms(rooms,world.getPlayer());
        boolean nearby = false;
        for(int i=0; i<world.getPitCount() && nearby == false; i++) {
            nearby = contains(world.getPit(i),rooms);       //see if we are near a bat
        }
        return nearby;
    }

    public boolean isBatNearby() {
        int[] rooms = new int[3];
        connectingRooms(rooms,world.getPlayer());
        boolean nearby = false;
        for(int i=0; i<world.getBatCount() && nearby == false; i++) {
            nearby = contains(world.getBat(i),rooms);       //see if we are near a bat
        }
        return nearby;
    }

    /**
     * Returns if any of the room numbers are the same as loc.
     */
    protected boolean contains(int loc,int[] rooms) {
        for(int r : rooms) {
            if(loc == r)
                return true;
        }
        return false;
    }

    public void moveTo(int n) {
        int[] exits = new int[3];
        connectingRooms(exits,world.getPlayer());
        boolean valid = false;
        for(int i = 0; i < exits.length; i++) {
            if(n == exits[i])
                valid = true;
        }
        if(valid)
            world.setPlayer(n);
    }

    public String getExits(int offset) {
        int[] exits = new int[3];
        connectingRooms(exits,world.getPlayer());
        StringBuilder buff = new StringBuilder();
        buff.append(exits[0]+offset)
            .append(',')
            .append(exits[1]+offset)
            .append(',')
            .append(exits[2]+offset);

        return buff.toString();
    }

    public void connectingRooms(int[] exits,int sourceRoom) {
        int start = (sourceRoom)*3;
        for(int i = start, exitIndex = 0; i < start+3; i++,exitIndex++) {
            exits[exitIndex] = world.getWorld(i);
        }
    }

    public void close() {
        server.close(sock);
    }
}
