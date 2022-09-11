package com.axorion;

public class MudWorld {
    static final int START_ARROWS = 5;
    int[] world = {
        1,4,7,      // 0
        2,0,9,      // 1
        3,1,11,     // 2
        4,2,13,     // 3
        0,3,5,      // 4
        6,14,4,     // 5
        7,5,16,     // 6
        8,6,0,      // 7
        9,7,17,     // 8
        10,8,1,     // 9
        11,9,18,    // 10
        12,10,2,    // 11
        13,11,19,   // 12
        14,12,3,    // 13
        5,13,15,    // 14
        16,19,14,   // 15
        17,15,6,    // 16
        18,16,8,    // 17
        19,17,10,   // 18
        15,18,12};  // 19
    int wumpus;
    int[] bat = new int[2];
    int[] pit = new int[2];
    int room = 0;   //player
    int arrows = START_ARROWS;

    public MudWorld() {
//        for(int i=0; i<20; i++) {
//            int n=rnd(1,10);
//
//            if(n<1 || n>10) System.out.println(n+" is out of range "+i);
//            else System.out.println(n+" "+i);
//        }

        wumpus = rnd(1,19);
        bat[0] = rnd(1,19);
        do {
            bat[1] = rnd(1,19);
        } while(bat[1] == bat[0]);
        pit[0] = rnd(1,19);
        do {
            pit[1] = rnd(1,19);
        } while(pit[1] == pit[0]);

        System.out.println("wumpus="+getWumpus());
        System.out.println("pit 0="+getPit(0));
        System.out.println("pit 1="+getPit(1));
        System.out.println("bat 0="+getBat(0));
        System.out.println("bat 1="+getBat(1));
    }

    public int getWumpus() {
        return wumpus;
    }

    public void setWumpus(int wumpus) {
        this.wumpus = wumpus;
    }

    public void setRoom(int room) {
        this.room = room;
    }

    public int getRoom() {
        return room;
    }

    public int getWorld(int n) {
        return world[n];
    }

    public int getBat(int n) {
        return bat[n];
    }

    public void setBat(int n,int w) {
        bat[n] = w;
    }

    public int getPit(int n) {
        return pit[n];
    }

    public void setPit(int n,int w) {
        pit[n] = w;
    }

    public void moveWumpus() {
        wumpus = rnd(0,19);
    }

    /**
     * random number between min and max inclusive.
     */
    public int rnd(int min,int max) {
        return (int)Math.floor(Math.random()*(max-min+1)+min);
    }
}
