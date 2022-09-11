package com.axorion;

import org.luaj.vm2.Globals;
import org.luaj.vm2.LuaValue;
import ssobjects.telnet.TelnetServerSocket;

import java.io.IOException;

public class LuaServerInterface {
    String command;
    TelnetServerSocket sock;
    Globals globals;
    MudWorld world;
    boolean awaitingInput = false;
    public LuaValue awaitHandler = null;

    public LuaServerInterface(TelnetServerSocket sock,MudWorld world) {
        this.sock = sock;
        this.world = world;
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
    public int getRoom(int offset) {
        return world.getRoom()+offset;
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
        calcExits(exits,world.getWumpus());
        return contains(world.getRoom(),exits);
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
        calcExits(exits,world.getRoom());
        boolean valid = false;
        for(int i = 0; i < exits.length; i++) {
            if(n == exits[i])
                valid = true;
        }
        if(valid)
            world.setRoom(n);
    }

    public String getExits(int offset) {
        int[] exits = new int[3];
        calcExits(exits,world.getRoom());
        StringBuilder buff = new StringBuilder();
        buff.append(exits[0]+offset)
            .append(',')
            .append(exits[1]+offset)
            .append(',')
            .append(exits[2]+offset);

        return buff.toString();
    }

    public void calcExits(int[] exits,int room) {
        int start = (room)*3;
        for(int i = start, exitIndex = 0; i < start+3; i++,exitIndex++) {
            exits[exitIndex] = world.getWorld(i);
        }
    }
}
