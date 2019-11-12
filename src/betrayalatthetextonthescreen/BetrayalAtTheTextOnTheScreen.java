/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package betrayalatthetextonthescreen;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Random;
/**
 * Main Class<br>
 * <P>
 * TODO<br>
 * Move parser handler into another method<br>
 * Move map builder into another file<br>
 * Move debugger into another file<br>
 * @author Pippy Vallone, Trinity Headen, and Michael Elijius
 */
public class BetrayalAtTheTextOnTheScreen 
{
    final static int NUMBER_OF_ROOMS = 7;
    static Room[] rooms = new Room[NUMBER_OF_ROOMS];
    static Player player;
    static List<String> roomNames = new ArrayList<String>(Arrays.asList("Kitchen", "Bathroom", "Main Hall", "Bedroom", "Living Room", "Study", "Observatory"));
    static List<String> roomDescs = new ArrayList<String>(Arrays.asList("Kitchen Description", "Bathroom Description", "Main Hall Description", "Bedroom Description", "Living Room Description", "Study Description", "Observatory Description"));
    static boolean debug = false;
    /**
     * @param args the command line arguments
     */
    public static void main(String[] args) 
    {
        buildMap();
        player = new Player("Player 1");
        player.addInventoryItem("Blueberry");
        Parser parser = new Parser();
        String[] userInputArray = parser.parseInput();
        while (!userInputArray[0].equals("quit")) 
        {
            switch (userInputArray[0])
            {
                case "inventory":
                    System.out.println(player.getPlayerInventory());
                    break;
                case "drop":
                case "put down":
                    //System.out.println(userInputArray[1]);
                    if(player.removeInventoryItem(userInputArray[1]))
                    {
                        rooms[player.getPlayerLocation()].addInventoryItem(userInputArray[1]);
                        rooms[player.getPlayerLocation()].appendRoomDescription("There is a " + userInputArray[1] + " on the floor here.");
                        System.out.println("You are no longer carrying " + userInputArray[1] + ".");
                    }
                    else
                        System.out.println("You are not carrying " + userInputArray[1] + ".");
                    break;
                case "look":
                case "view":
                    System.out.println("You are in the " + rooms[player.getPlayerLocation()].getRoomName());
                    System.out.println(rooms[player.getPlayerLocation()].getRoomDescription());
                    break;
                case "pick up":
                    if(rooms[player.getPlayerLocation()].removeInventoryItem(userInputArray[1]))
                    {
                        if (player.addInventoryItem(userInputArray[1])) 
                        {
                            System.out.println(userInputArray[1] + " has been added to your inventory.");
                        }
                        else
                        {
                            rooms[player.getPlayerLocation()].addInventoryItem(userInputArray[1]);
                            System.out.println("You can't carry anything more.  You leave the " + userInputArray[1] + " where it is.");
                        }    
                    }
                    else
                    {
                        System.out.println("You don't see a " + userInputArray[1] + " here.");
                    }
                break;
                default:
            }        
            System.out.println();
            userInputArray = parser.parseInput();
        }
    }
    
    static void debug(String message)
    {
        if (debug) 
            System.out.println(message);
    }
    
    /**
     *
     * Map Builder Method<br>
     * Creates as many rooms as specified by NUMBER_OF_ROOMS constant.<br>
     * Randomly selects a name for each room and sets a corresponding room description.<br>
     * Randomizes number of doors for each room (2-3) and appends a sentence onto the room description stating how many doors are in the room.<br>
     * Selects one door per room at random to lead to a predetermined route that ensures every room is accessible.<br>
     * Randomizes the destination for any remaining doors (can lead to themselves).<br>
     * Prints debug to console.<br>
     * Assumes NUMBER_OF_ROOMS is defined within file
     * <P>
     * TODO<br> 
     * Move out of main and into another file.<br>
     *
     */
    static void buildMap()
    {
        Random rand = new Random();
        int randomNumber;
        List<Integer> roomList = new ArrayList<Integer>();
        roomList.add(NUMBER_OF_ROOMS - 2);
        roomList.add(NUMBER_OF_ROOMS - 1);
        for (int index = 0; index < NUMBER_OF_ROOMS - 2; index++) 
            roomList.add(index);
        for (int index = 0; index < NUMBER_OF_ROOMS; index++) 
        {
            rooms[index] = new Room(("" + index), index);
            randomNumber = rand.nextInt(roomNames.size());
            rooms[index].setRoomName(roomNames.get(randomNumber));
            rooms[index].setBaseRoomDescription(roomDescs.get(randomNumber));            
            rooms[index].setRoomDescription(roomDescs.get(randomNumber));
            roomNames.remove(randomNumber);
            roomDescs.remove(randomNumber);
            if(rand.nextBoolean())
            {
                debug("Room " + index + " has two doors.");
                rooms[index].appendRoomDescription("There are 2 doors here.");
                if(rand.nextBoolean())
                {
                    debug("Room " + index + "'s door one progresses.");
                    rooms[index].setDoor(1, roomList.get(index));
                    rooms[index].setDoor(2, rand.nextInt(NUMBER_OF_ROOMS - 1));
                }
                else
                {
                    debug("Room " + index + "'s door two progresses.");
                    rooms[index].setDoor(2, roomList.get(index));
                    rooms[index].setDoor(1, rand.nextInt(NUMBER_OF_ROOMS - 1));
                }
            }
            else
            {
                debug("Room " + index + " has three doors here.");
                rooms[index].appendRoomDescription("There are 3 doors here.");
                randomNumber = rand.nextInt(3);
                switch(randomNumber)
                {
                    case 0:
                        debug("Room " + index + "'s door one progresses.");
                        rooms[index].setDoor(1, roomList.get(index));
                        rooms[index].setDoor(2, rand.nextInt(NUMBER_OF_ROOMS - 1));
                        rooms[index].setDoor(3, rand.nextInt(NUMBER_OF_ROOMS - 1));
                        break;
                    case 1:
                        debug("Room " + index + "'s door two progresses.");
                        rooms[index].setDoor(2, roomList.get(index));
                        rooms[index].setDoor(1, rand.nextInt(NUMBER_OF_ROOMS - 1));
                        rooms[index].setDoor(3, rand.nextInt(NUMBER_OF_ROOMS - 1));
                        break;
                    case 2:
                        debug("Room " + index + "'s door three progresses.");
                        rooms[index].setDoor(3, roomList.get(index));
                        rooms[index].setDoor(1, rand.nextInt(NUMBER_OF_ROOMS - 1));
                        rooms[index].setDoor(2, rand.nextInt(NUMBER_OF_ROOMS - 1));
                        break;
                }
            }
        }
        for (int index = 0; index < NUMBER_OF_ROOMS; index++) 
        {
            debug(rooms[index].toString());
        }
    }
    
}
