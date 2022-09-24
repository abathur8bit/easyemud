import static org.junit.Assert.*;

import com.axorion.LuaServerInterface;
import com.axorion.MudWorld;
import org.junit.Test;
import static org.mockito.Mockito.mock;


public class InterfaceTest {
    @Test
    public void testExits() {
        MudWorld world = new MudWorld();
        LuaServerInterface lsi = new LuaServerInterface(null,world);
        assertEquals(lsi.getPlayer(0),0);
        int[] exits = new int[3];
        lsi.connectingRooms(exits,0);
        assertEquals(1,exits[0]);
        assertEquals(4,exits[1]);
        assertEquals(7,exits[2]);

        world.setPlayer(1);
        lsi.connectingRooms(exits,1);
        assertEquals(2,exits[0]);
        assertEquals(0,exits[1]);
        assertEquals(9,exits[2]);

        world.setPlayer(19);
        lsi.connectingRooms(exits,19);
        assertEquals(15,exits[0]);
        assertEquals(18,exits[1]);
        assertEquals(12,exits[2]);
    }

    @Test
    public void testWumpus() {
        MudWorld world = new MudWorld();
        LuaServerInterface lsi = new LuaServerInterface(null,world);
        int wumpus = world.getWumpus();
        world.setPlayer(wumpus-1);
        assertTrue(lsi.isWumpusNearby());
    }
}
