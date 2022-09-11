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
    sock:println('You are in room '..sock:getRoom(offset))
    sock:println("Tunnels lead to "..sock:getExits(offset))
    if(sock:isWumpusNearby()) then
        sock:println("I smell a Wumpus")
    end
    if(sock:getWumpus() == sock:getRoom()) then
        sock:println("You bumped into the Wumpus!")
        if(sock:roll(1,10) > 5) then
            sock:println("The Wumpus ate you!")
        else
            sock:println("You hear the Wumpus shuffling around")
            sock:moveWumpus()
            if(sock:getWumpus() == sock:getRoom()) then
                sock:println("The Wumpus ate you!")
            else
                sock:println("The Wumpus stomps off to another room")
            end
        end
    end
    sock:ask('[S]hoot or [M]ove?',moveResponse)
end

function moveResponse(resp)
    if(resp == 'm') then
        sock:ask("Where to? ",moveDest)
    else
        sock:println("Can't shoot yet")
    end
end

function moveDest(dest)
    sock:moveTo(dest);
    showRoom()
end

