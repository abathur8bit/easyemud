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

package com.axorion;

import org.luaj.vm2.Globals;
import org.luaj.vm2.LuaValue;
import org.luaj.vm2.lib.jse.JsePlatform;

public class Main {
    public static void main(String[] args) {
        System.out.println("Hello world!");
        String script = "print('hello, world', _VERSION)";

        // create an environment to run in
        Globals globals = JsePlatform.standardGlobals();

        // Use the convenience function on the globals to load a chunk.
        LuaValue chunk = globals.load(script, "maven-exmaple");

        // Use any of the "call()" or "invoke()" functions directly on the chunk.
        chunk.call();

        chunk = globals.loadfile("scripts/java.lua");
        chunk.call();
    }
}