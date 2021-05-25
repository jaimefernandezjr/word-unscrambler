import CORBA
import WordUnscrambler
import CosNaming
import sys

orb = CORBA.ORB_init(sys.argv, CORBA.ORB_ID)

obj = orb.resolve_initial_references("NameService")
rootContext = obj._narrow(CosNaming.NamingContext)

if rootContext is None:
    print ("Failed to narrow the root naming context")
    sys.exit(1)

name = [CosNaming.NameComponent("WordUnscrambler", ""), ]
try:
    obj = rootContext.resolve(name)
except CosNaming.NamingContext.NotFound as ex:
    print ("Name not found")
sys.exit(1)

wordUnscrambler = obj._narrow(WordUnscramblerApp.WordUnscrambler)

if wordUnscrambler is None:
    print ("Object reference is not an WordUnscramblerApp::WordUnscrambler")
    sys.exit(1)

print("----------------------------")
print("Welcome to Word Unscrambler!")
print("----------------------------\n")

player = ""

def checkIfNameIsValid(name):
    if name == "":
        return "Username cannot be null"
    else:
        return "Okay"


playerName = input("Player name: ")

errorDiagnosis = checkIfNameIsValid(playerName)

if errorDiagnosis != "Okay":
    print(errorDiagnosis)
else:
    isRegistered = wordUnscrambler.registerPlayer(playerName)
    if isRegistered == True:
        player = playerName
    else:
        print("This name is already taken. Please enter another name.")
        SystemExit
        pass
    pass


