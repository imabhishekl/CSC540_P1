/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package my.control;
import TableStrcuture.Rooms;
import TableStrcuture.Reserve_room;

/**
 *
 * @author Pooja Asher
 */
public class Test {
    public static void main(String args[]){
        LibrarySystem l=new LibrarySystem();
        l.setup();
        System.out.println("main");
        try{
            Rooms r=ButtonEvents.getRoom("Hunt",3,"study");
        }
        catch(Exception e){};
    }
}
