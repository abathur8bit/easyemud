print('Greetings from Lua')
for i=1,5 do
 print("i is "..i)
end

a=5
repeat
 a=a-1
 print("a="..a)
until a==0

b=0
while b<5 do
 b=b+1
 print("b="..b);
end

function add(a,b)
 return a+b
end

function sub(a,b)
 return a-b
end

op = {}
op["calc"]=add

a=5
b=4
c=op.calc(a,b)
print("first c="..c);
op["calc"]=sub
c=op.calc(5,4)
print("second c="..c);

lookup = {}
lookup["rock"]={}
lookup["rock"]["rock"] = "draw"
lookup["rock"]["paper"] = "lose"
lookup["rock"]["scissors"] = "win"

lookup["paper"]={}
lookup["paper"]["rock"] = "win"
lookup["paper"]["paper"] = "draw"
lookup["paper"]["scissors"] = "lose"

lookup["scissors"]={}
lookup["scissors"]["rock"] = "lose"
lookup["scissors"]["paper"] = "win"
lookup["scissors"]["scissors"] = "draw"

determination=lookup.scissors.rock
print("determination="..determination)

map = {}
map["place"]="portland"
print("place="..map["place"])


function setName()
 name='Fred';
end
