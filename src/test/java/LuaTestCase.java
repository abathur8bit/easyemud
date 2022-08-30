/* *****************************************************************************
 * Copyright 2019 Lee Patterson <https://8BitCoder.com> <https://github.com/abathur8bit>
 *
 * Created 8/30/2022
 *
 * You may use and modify at will. Please credit me in the source.
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *   http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 * ******************************************************************************/

import static org.junit.Assert.*;
import org.junit.Test;
import org.luaj.vm2.Globals;
import org.luaj.vm2.LuaNil;
import org.luaj.vm2.LuaTable;
import org.luaj.vm2.LuaValue;
import org.luaj.vm2.lib.jse.JsePlatform;

/** Tests to check out how luaj works. */
public class LuaTestCase {
    @Test
    public void testAssert() {
        assertEquals(1,1);
    }

    @Test
    public void testDirectEvaluation() {
        String script = "return math.pow(..., 3)";
        Globals globals = JsePlatform.standardGlobals();
        LuaValue chunk = globals.load(script, "cube");
        int result = chunk.call(LuaValue.valueOf(5)).toint();
        assertEquals(125, result);
    }

    @Test
    public void passString() {
        String name="Lee";
        String script="print('name=',name)";
        Globals globals = JsePlatform.standardGlobals();    // create an environment to run in
        globals.set("name",name);
        LuaValue chunk = globals.load(script, "maven-exmaple");
        chunk.call();
    }

    @Test
    public void passIntLuaModify() {
        int a=5;
        String script =
            "a=...\n" +
            "a=a+a\n"+
            "print('hello, world', a)\n" +
            "print('the greeting')\n" +
            "print('sheild='..shield)\n" +
            "shield=456\n" +
            "return a";

        Globals globals = JsePlatform.standardGlobals();    // create an environment to run in
        LuaValue chunk = globals.load(script, "maven-exmaple");
        globals.set("shield",123);
        int result = chunk.call(LuaValue.valueOf(a)).toint();
        assertEquals(10,result);
        int shield=globals.get("shield").toint();
        assertEquals(456,shield);
    }

    @Test
    public void passIntValueLuaModify() {
        String script =
            "print('sheild='..shield)\n" +
            "shield=456\n";
        int shield=123;

        Globals globals = JsePlatform.standardGlobals();    // create an environment to run in
        LuaValue chunk = globals.load(script, "maven-exmaple");
        globals.set("shield",shield);
        chunk.call();
        shield=globals.get("shield").toint();
        assertEquals(456,shield);
    }

    @Test
    public void passStringValueLuaModify() {
        String script =
            "print('sheild='..shield)\n" +
            "shield='wooden'\n";
        String shield="iron";

        Globals globals = JsePlatform.standardGlobals();    // create an environment to run in
        LuaValue chunk = globals.load(script, "maven-exmaple");
        globals.set("shield",shield);
        chunk.call();
        shield=globals.get("shield").toString();
        assertEquals("wooden",shield);
    }

    @Test
    public void passTable() {
        String script =
            "print('place='..map['place'])\n"+
            "map['place']='Pittsburgh'\n"+
            "print('place now='..map['place'])\n"+
            "map['went']='over there'\n"
            ;
        Globals globals = JsePlatform.standardGlobals();    // create an environment to run in
        LuaValue chunk = globals.load(script, "maven-exmaple");
        LuaValue table = LuaTable.tableOf();
        table.set("place","Portland");
        globals.set("map",table);
        chunk.call();
        assertEquals("Pittsburgh",table.get("place").toString());
        assertEquals("over there",table.get("went").toString());
        assertEquals(LuaNil.NIL,table.get("XXX"));
    }

    @Test
    public void callFunction() {
        String script =
            "function setName() \n"+
            " print('name='..name);\n"+
            " name='Pauline';\n"+
            " print('name now='..name);\n"+
            "end\n";
        String name="Lee";
        Globals globals = JsePlatform.standardGlobals();    // create an environment to run in
        globals.set("name",name);
        LuaValue chunk = globals.load(script, "maven-exmaple");
        chunk.call();   //script must be run to pick up the functions
        LuaValue setName = globals.get("setName");
        assertNotNull(setName);
        setName.call();
        name = globals.get("name").toString();
        assertEquals("Pauline",name);
    }

    @Test
    public void testMultiLine() {
        String script = "print('hello, world', _VERSION)\n" +
            "print('the greeting')";

        // create an environment to run in
        Globals globals = JsePlatform.standardGlobals();

        // Use the convenience function on the globals to load a chunk.
        LuaValue chunk = globals.load(script, "maven-exmaple");

        // Use any of the "call()" or "invoke()" functions directly on the chunk.
        chunk.call();
    }
}
