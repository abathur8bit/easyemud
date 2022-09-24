offset = 0  -- 0=0-19 1=1-20
-- function calcExits(r)
--     return (r-1)*3;
-- end

function response(resp)
    sock:println('Hello '..resp..'!');
    mainLoop()
end

function connected()
    sock:println('Welcome to Hunt the Wumpus');
    mainLoop()
end

function mainLoop()
    showRoom()
    showPits()
    showBats()
    showWumpus()

    if(sock:getMode()==1) then
        --playing
        sock:ask("[S]hoot or [M]ove? ",moveResponse)
    end
end

function showRoom()
     sock:println("")
     sock:println('You are in room '..sock:getPlayer(offset))
     sock:println("Tunnels lead to "..sock:getExits(offset))
end

function showPits()
    if(sock:isPitNearby()) then
        sock:println("You feel a draft")
    end
end

function showBats()
    if(sock:isBatNearby()) then
        sock:println("Bats nearby")
    end

    if(isWithBat()) then
        sock:println("player and bat in same room")
    end
end

function showWumpus()
    if(sock:isWumpusNearby()) then
        sock:println("I smell a Wumpus")
     elseif(sock:getWumpus() == sock:getPlayer()) then
        sock:println("")
        sock:println("You bumped into the Wumpus!")
        r=sock:roll(1,10)
        if(offset==0) then
            sock:println("Wumpus rolled a "..r)
        end
        if(r > 5) then
            sock:setMode(0)     -- player is dead
            sock:println("Gulp! The Wumpus ate you!")
            sock:ask("GAME OVER! Play again [Y/N]? ",playAgainResponse)
        else
            sock:println("Whoosh! You feel him brush past you into another room")
            sock:moveWumpus()
            mainLoop()
        end
    end
end

function isWithBat()
    if(sock:getBat() == sock:getPlayer()) then
        return true
    end
end

function moveResponse(resp)
    if(resp == 'm') then
        sock:ask("Where to? ",moveDest)
    else
        sock:println("Can't shoot yet")
        mainLoop()
    end
end

function moveDest(dest)
    sock:moveTo(dest-offset);
    mainLoop()
end

function playAgainResponse(resp)
    if(resp == 'y' or resp == 'Y') then
        sock:println("Great, respawning.")
        sock:setMode(1)     --playing again
        sock:spawnWorld()   --reset wumpus, bat and pit locations
        mainLoop()          --main loop
    else
        sock:println("Catch you next time.")
        sock:println("Closing connection.")
        sock:close()
    end
end