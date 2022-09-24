offset = 0  -- 0=0-19 1=1-20
-- function calcExits(r)
--     return (r-1)*3;
-- end

function response(resp)
    sock:println('Hello '..resp..'!');
    showRoom()
end

function connected()
    sock:println('Welcome to Hunt the Wumpus');
    showRoom()
end

function showRoom()
    showRoomNumberExits()
    showPits()
    showBats()

    if(sock:isWumpusNearby()) then
        sock:println("I smell a Wumpus")
        sock:ask("[S]hoot or [M]ove? ",moveResponse)
    elseif(sock:getWumpus() == sock:getPlayer()) then
        sock:println("")
        sock:println("You bumped into the Wumpus!")
        r=sock:roll(1,10)
        if(offset==0) then
            sock:println("Wumpus rolled a "..r)
        end
        r=3
        if(r > 5) then
            sock:println("Gulp! The Wumpus ate you!")
            sock:ask("GAME OVER! Play again [Y/N]? ",playAgainResponse)
        else
            sock:println("Whoosh! You feel him brush past you into another room")
            sock:moveWumpus()
            showRoom()
        end
    else
        sock:ask("[S]hoot or [M]ove? ",moveResponse)
    end

end

function showRoomNumberExits()
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
        showRoom()
    end
end

function moveDest(dest)
    sock:moveTo(dest-offset);
    showRoom()
end

function playAgainResponse(resp)
    if(resp == 'y' or resp == 'Y') then
        sock:println("Great, respawning.")
        sock:spawnWorld()
        showRoom()
    else
        sock:println("Catch you next time.")
        sock:println("Closing connection.")
    end
end