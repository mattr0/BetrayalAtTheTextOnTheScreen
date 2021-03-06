package betrayalatthetextonthescreen;

import java.util.ArrayList;
import java.util.List;

/**
 *
 * Player Class - instantiated for each player (currently 1 player).<br>
 * Constructor initializes player name (if no name is specified, should default 
 * to DEFAULT_PLAYER_NAME), current player location (defaulted to room number 0), 
 * gui, rooms visited array, and player inventory.<br>
 * <P>
 * Instance variables: player name, current player location, player inventory,
 * pointer to map, player gui, rooms visited array, and debug<br>
 * Methods: get/set player name, get/set player location, get/set/clear player inventory, 
 * get/set player visited, get player inventory index, can add/add/remove/can remove inventory item, 
 * parse, and to string.<br>
 * <P>
 * ToDo:<br>
 *
 * @author Pippy Vallone, Trinity Headen, and Michael Elijius
 * 
 */
public class Player {
    static final String DEFAULT_PLAYER_NAME = "I'm a default";
    static final int MAX_INVENTORY_SIZE = 10;
    private String playerName;
    private int playerLocation;
    private Inventory playerInventory;
    private Debug debug;
    private Map playerMap;
    private GUI playerGui;
    private boolean[] roomsVisited;
    private boolean gameOver;
    
    /**
     * Player constructor<br>
     * Calls second player constructor with the default player name<br>
     */
    
    Player(Map map)
    {
        this(DEFAULT_PLAYER_NAME, map);
    }
    
    /**
     * Player constructor<br>
     * Initializes player name, gui, room visited array. and debug<br>
     * Initializes player location to 0<br>
     * Initializes empty player inventory with specified max size<br>
     * Saves pointer to the map<br>
     * Prints initial room description and sets the starting room to visited<br>
     * @param name player name<br>
     */
    
    Player (String name, Map map) 
    {
        playerName = name;
        playerLocation = 0;
        playerInventory = new Inventory(MAX_INVENTORY_SIZE);
        debug = new Debug();
        playerMap = map;
        playerGui = new GUI(this, playerName);
        this.gameOver = false;
        playerGui.writeGUI("Its Halloween night, and your friend dared you to "
                + "enter the SCARY HAUNTED HOUSE down the road, reluctantly "
                + "you entered the house, and suddenly the door slams behind you! "
                + " Darkness envelops you.\n"
                + "For instructions on how to play, type 'help'.\n");
        playerGui.writeGUI(map.enterRoomDescription(playerLocation, false));
        playerGui.writeGUI("\nWhat would you like to do?");
        roomsVisited = new boolean[map.getNumberOfRooms()];
        for(boolean visited : roomsVisited)
        {
            visited = false;
        }
        this.setPlayerVisited();
    }
    
    public String getPlayerName()
    {
        return this.playerName;
    }
    
    public void setPlayerName(String name)
    {
        this.playerName = name;
    }
    
    public int getPlayerLocation()
    {
        return this.playerLocation;
    }
    
    public void setPlayerLocation(int room)
    {
        this.playerLocation = room;
    }
    
    public Inventory getPlayerInventory()
    {
        return this.playerInventory;
    }
    
    public void setPlayerInventory(List<String> items)
    {
        this.playerInventory.clearInventory();
        this.playerInventory.setInventory(items);
    }
    
    public boolean getPlayerVisited()
    {
        return this.roomsVisited[this.playerLocation];
    }
    
    public void setPlayerVisited()
    {
        this.roomsVisited[playerLocation] = true;
    }
    
    /**
     * Clear Player Inventory Method<br>
     * Removes all strings from the playerInventory list.<br>
     */
    public void clearPlayerInventory()
    {
        this.playerInventory.clearInventory();
    }
    
    /**
     * Get Player Inventory Index<br>
     * Returns the index within the player's inventory of the specified item.<br>
     * @param item item to be searched for<br>
     * @return int - index of specified item<br>
     */
    public int getPlayerInventoryIndex (String item)
    {
        int index = this.playerInventory.getInventoryItemIndex(item);
        return index;
    }
    
    public boolean getGameOver()
    {
        return this.gameOver;
    }
    
    public void setGameOver(boolean over)
    {
        this.gameOver = over;
    }

    /**
     * Add Inventory Item Method<br>
     * Adds specified item to the player's inventory.<br>
     * Prints a debug of how many items are in the player's inventory.<br>
     * @param item item to be added to the inventory<br>
     */
    public void addInventoryItem(String item)
    {
        debug.debug("There are " + playerInventory.getInventoryLength() + " items in player's inventory.");
        this.playerInventory.addInventoryItem(item);
    }
    
    /**
     * Can Add Inventory Item Method<br>
     * Checks if there is room for another item in the player's inventory.<br>
     * @return boolean value - True if an item can be added to the inventory, else false<br>
     */
    public boolean canAddInventoryItem()
    {
        return this.playerInventory.canAddInventoryItem();
    }
    
    /**
     * Remove Inventory Item Method<br>
     * Removes the specified item from the player's inventory.<br>
     * @param item item to be removed from the inventory<br>
     */
    public void removeInventoryItem(String item)
    {
        this.playerInventory.removeInventoryItem(item);
    }
    
    /**
     * Can Remove Inventory Item Method<br>
     * Checks if the player has the specified item.<br>
     * @param item item to be checked<br>
     * @return boolean value - True if player's inventory contains specified item, else false<br>
     */
    public boolean canRemoveInventoryItem(String item)
    {
        return this.playerInventory.ifHasItem(item);
    }
    
    /**
     * Parse Method<br>
     * Saves user input processed by the parser and responds accordingly.<br> 
     * <P>
     * TODO:<br>
     * Accept alternate methods of specifying door to move through.<br>
     */
    void parse(String[] userInputArray)
    {
        if(!userInputArray[0].equals("quit"))
        {
            switch (userInputArray[0])
            {
                case "inventory":
                    playerGui.writeGUI(getPlayerInventory().toString());
                    break;
                case "drop":
                case "put":
                    debug.debug(userInputArray[1]);
                    if(canRemoveInventoryItem(userInputArray[1]))
                    {
                        removeInventoryItem(userInputArray[1]);
                        playerMap.putDown(getPlayerLocation(), userInputArray[1]);
                        playerGui.writeGUI("You are no longer carrying " + userInputArray[1] + ".");
                    }
                    else
                        playerGui.writeGUI("You are not carrying " + userInputArray[1] + ".");
                    break;
                case "look":
                case "view":
                    playerGui.writeGUI(playerMap.look(getPlayerLocation()));
                    break;
                case "pick":
                case "take":
                    if(playerMap.ifRoomHasItem(getPlayerLocation(), userInputArray[1]))
                    {
                        if (canAddInventoryItem()) 
                        {
                            addInventoryItem(userInputArray[1]);
                            playerMap.removeInventoryItem(getPlayerLocation(), userInputArray[1]);
                            playerGui.writeGUI(userInputArray[1] + " has been added to your inventory.");
                        }
                        else
                        {
                            playerGui.writeGUI("You can't carry anything more.  You leave the " + userInputArray[1] + " where it is.");
                        }    
                    }
                    else
                    {
                        playerGui.writeGUI("You don't see " + userInputArray[1] + " here.");
                    }
                    break;
                case "move":
                case "go":
                    try
                    {
                        int doorNumber = Integer.parseInt(userInputArray[1]) - 1;
                        if(playerMap.ifDoorExists(getPlayerLocation(), doorNumber))
                        {
                            switch(doorNumber)
                            {
                                case 0:
                                    playerGui.writeGUI("You move through the left most door.");
                                    break;
                                case 1:
                                    if(playerMap.getNumberOfDoors(getPlayerLocation()) == 2)
                                    {
                                        playerGui.writeGUI("You move through the right most door.");
                                    }
                                    else
                                    {
                                        playerGui.writeGUI("You move through the central door.");
                                    }
                                    break;
                                case 2:
                                    playerGui.writeGUI("You move through the right most door.");
                            }
                            setPlayerLocation(playerMap.getDoor(getPlayerLocation(), doorNumber));
                            playerGui.writeGUI(playerMap.enterRoomDescription(getPlayerLocation(), this.roomsVisited[this.playerLocation]));
                            setPlayerVisited();
                        }
                        else
                            playerGui.writeGUI("There are not that many doors here.");
                    }
                    catch(NumberFormatException exception)
                    {
                        switch(userInputArray[1])
                        {
                            case "left":
                                setPlayerLocation(playerMap.getDoor(getPlayerLocation(), 0));
                                playerGui.writeGUI("You move through the left most door.");
                                playerGui.writeGUI(playerMap.enterRoomDescription(getPlayerLocation(), this.roomsVisited[this.playerLocation]));
                                setPlayerVisited();
                                break;
                            case "right":
                                if(playerMap.getNumberOfDoors(getPlayerLocation()) == 2)
                                {
                                    setPlayerLocation(playerMap.getDoor(getPlayerLocation(), 1));
                                    playerGui.writeGUI("You move through the right most door.");
                                    playerGui.writeGUI(playerMap.enterRoomDescription(getPlayerLocation(), this.roomsVisited[this.playerLocation]));
                                    setPlayerVisited();
                                }
                                else if(playerMap.getNumberOfDoors(getPlayerLocation()) == 3)
                                {
                                    setPlayerLocation(playerMap.getDoor(getPlayerLocation(), 2));
                                    playerGui.writeGUI("You move through the right most door.");
                                    playerGui.writeGUI(playerMap.enterRoomDescription(getPlayerLocation(), this.roomsVisited[this.playerLocation]));
                                    setPlayerVisited();
                                }
                                break;
                            case "middle":
                            case "center":
                            case "central":
                                if(playerMap.getNumberOfDoors(getPlayerLocation()) == 2)
                                {
                                    playerGui.writeGUI("I don't understand.  There is not a central door here.");
                                }
                                else if(playerMap.getNumberOfDoors(getPlayerLocation()) == 3)
                                {
                                    setPlayerLocation(playerMap.getDoor(getPlayerLocation(), 1));
                                    playerGui.writeGUI("You move through the central door.");
                                    playerGui.writeGUI(playerMap.enterRoomDescription(getPlayerLocation(), this.roomsVisited[this.playerLocation]));
                                    setPlayerVisited();
                                }
                                break;
                            default:
                            playerGui.writeGUI("Please specify the door you would like to go through.");
                        }
                    }
                    break;
                case "sit":
                    playerGui.writeGUI("Sitting doesn't sound like the best use of your time.");
                case "open":
                    switch(userInputArray[1])
                    {
                        case "refrigerator":
                        case "fridge":
                            if("Kitchen".equals(playerMap.getRoomName(playerLocation)))
                            {
                                playerGui.writeGUI("That might not be the best idea.  You never know what is in a refrigerator.");
                            }   
                            else
                            {
                                playerGui.writeGUI("You don't see a refrigerator here.");
                            }
                            break;
                        case "toilet":
                            if("Powder Room".equals(playerMap.getRoomName(playerLocation)))
                            {
                                playerGui.writeGUI("You instantly regret this decision.\n"
                                        + "Wait, is that a bobby pin?\n"
                                        + "It must have fallen off the toilet seat.\n"
                                        + "What a weird place to keep your bobby pins.");
                                playerMap.putDown(this.playerLocation, "bobby pin");
                            }
                            break;
                        default:
                            playerGui.writeGUI("I don't know how you would open that");
                            break;
                    }
                    break;
                case "unlock":
                    if(this.playerLocation == (playerMap.getNumberOfRooms() - 1))//last room in rooms array
                    {
                        if(playerInventory.ifHasItem("key"))
                        {
                            playerGui.writeGUI("You insert the key into the lock.\n"
                                    + "It does not initially turn and you fear for a "
                                    + "moment that the key will break, but finally it "
                                    + "twists with a subtle click.\nThe door gives way "
                                    + "and the outside greets you like an old friend.\n"
                                    + "Congratulations, freedom is yours.");
                            this.gameOver = true;
                        }
                        else if(playerInventory.ifHasItem("bobby pin"))
                        {
                            playerGui.writeGUI("You cleverly insert the bobby pin into"
                                    + " the key hole and give it a hearty jiggle.\n"
                                    + "As you attempt to twist the pin and hear a loud SNAP.\n"
                                    + "It turns out lockpicking is way harder in real life "
                                    + "than it is in the movies.\n"
                                    + "The remains of the broken bobby pin sit unreachable in"
                                    + " the back of the lock, rendering it useless and stuck"
                                    + " in the locked position.\n"
                                    + "Welp.  I geuss that's the game.\n");
                            this.gameOver = true;     
                        }
                        else
                        {
                            playerGui.writeGUI("You don't have anything to use to unlock the door.");
                        }
                    }
                    else
                    {
                        playerGui.writeGUI("There is nothing to unlock here.");
                    }
                    break;
                case "help":
                    playerGui.writeGUI("Good of you to ask for help when you need it.\n"
                            + "Welcome to the house.\nYou can move and interact by typing "
                            + "commands into the text box.\nHere's what I can do:\n"
                            + "Type 'inventory' to see what you are carrying.\n"
                            + "Type 'drop' followed by an item you're carrying to put that "
                            + "item down on the floor.\n"
                            + "Type 'look' to have me repeat the full room description.\n"
                            + "Type 'pick up' followed by an item in the room to pick it up.\n"
                            + "Type 'move' followed by a door in the room to walk through that "
                            + "door and enter a new room (i.e. 'move door 2' or 'move left door').\n"
                            + "Type 'open' followed by an object in the room to open it up and "
                            + "look inside.\n"
                            + "Type 'unlock' followed by a locked item to... unlock things.\n"
                            + "Type 'help' to hear this spiel again.\n"
                            + "I hope this helped.");
                    break;
                default:
            }
            if(!this.gameOver)
            {
                playerGui.writeGUI("\nWhat would you like to do?");
            }
        }
        else
        {
            this.gameOver = true;
        }
    }
    
    /**
     * To String Method<br>
     * Creates and returns a string containing the name, location, and inventory contents for the player.<br>
     * @return String - all information regarding the player and its variables <br>
     */
    public String toString()
    {
        String returnString = "";
        returnString += "Name: " + this.playerName;
        returnString += "\tLocation: " + this.playerLocation + "\n";
        returnString += this.playerInventory + "\n";
        for(int index = 0; index < this.roomsVisited.length; index++)
        {
            returnString += index + ": " + roomsVisited[index] + "\t";
        }
        returnString += "\n";
        return returnString;
    }
    // other methods to follow
    
}
