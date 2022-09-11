import static org.junit.Assert.*;

import com.axorion.LuaServerInterface;
import com.axorion.MudWorld;
import org.junit.Test;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;


public class InterfaceTest {
    @Test
    public void testExits() {
        MudWorld world = new MudWorld();
        LuaServerInterface lsi = new LuaServerInterface(null,world);
        assertEquals(lsi.getRoom(0),0);
        int[] exits = new int[3];
        lsi.calcExits(exits,0);
        assertEquals(1,exits[0]);
        assertEquals(4,exits[1]);
        assertEquals(7,exits[2]);

        world.setRoom(1);
        lsi.calcExits(exits,1);
        assertEquals(2,exits[0]);
        assertEquals(0,exits[1]);
        assertEquals(9,exits[2]);

        world.setRoom(19);
        lsi.calcExits(exits,19);
        assertEquals(15,exits[0]);
        assertEquals(18,exits[1]);
        assertEquals(12,exits[2]);
    }

    @Test
    public void testWumpus() {
        MudWorld world = new MudWorld();
        LuaServerInterface lsi = new LuaServerInterface(null,world);
        int wumpus = world.getWumpus();
        world.setRoom(wumpus-1);
        assertTrue(lsi.isWumpusNearby());
    }
}
