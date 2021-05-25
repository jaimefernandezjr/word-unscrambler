import CORBA
import WordUnscramblerApp
import CosNaming
import sys

orb = CORBA.ORB_init(sys.argv, CORBA.ORB_ID)

obj = orb.resolve_initial_references("NameService")
rootContext = obj._narrow(CosNaming.NamingContext)

if rootContext is None:
    print ("Failed to narrow the root naming context")
    sys.exit(1)

name = [CosNaming.NameComponent("group", ""), ]
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

playerName = raw_input("Player Name: ");
if playerName == "":
    print("Player name cannot be null")
else:
    if wordUnscrambler.registerPlayer(playerName):
        player = playerName
    else:
        print("This name is already taken. Please enter another name.")

shuffledMysteryWord = wordUnscrambler.getShuffledMysteryWord(player)
lives = 5
while True:
    if lives == 0:
        print("The answer is " + wordUnscrambler.answer(player))
        print("YOU LOSE!")
        break

    print("----------------------")
    print("Word Unscrambling Game")
    print("----------------------")

    print("\nLives: " + str(lives))
    print("Scrambled Word: " + shuffledMysteryWord)
    print("\nMAIN SELECTION:"
          "\n1 - Answer"
          "\n2 - Shuffle"
          "\n3 - Main Menu")

    choice = input("\nCHOICE: ")

    if choice == 1:
        ans = raw_input("Answer: ")
        if wordUnscrambler.checkAnswer(player, ans):
            print("Your answer is correct!")
            print("YOU WIN!")
            while True:
                print("\nDo you want to play again?"
                      "\n1 - Yes"
                      "\n2 - No")
                decision = input("\nCHOICE: ")
                if decision == 1:
                    shuffledMysteryWord = wordUnscrambler.getShuffledMysteryWord(player)
                    break
                elif decision == 2:
                    isRemovePlayer = wordUnscrambler.removePlayer(player)
                    if isRemovePlayer:
                        print("The Player is removed from the game.")
                        sys.exit()
                    else:
                        print("The Player unsuccessfully removed from the game.")
                else:
                    print("Invalid Choice")
                    continue
        else:
            print("Your answer is incorrect")
            lives = lives - 1
            continue
    elif choice == 2:
        shuffledMysteryWord = wordUnscrambler.shuffleLetters(player, shuffledMysteryWord)
    elif choice == 3:
        print("\n1 - Resume"
              "\n2 - Restart"
              "\n3 - Quit")

        option = input("\nCHOICE: ")

        if option == 1:
            continue
        elif option == 2:
            shuffledMysteryWord = wordUnscrambler.getShuffledMysteryWord(player)
            lives = 5
            continue
        elif option == 3:
            isRemovePlayer = wordUnscrambler.removePlayer(player)
            if isRemovePlayer:
                print("The Player is removed from the game.")
                sys.exit()
            else:
                print("The Player unsuccessfully removed from the game.")
        else:
            print("Invalid Choice")
    else:
        print("Invalid choice")