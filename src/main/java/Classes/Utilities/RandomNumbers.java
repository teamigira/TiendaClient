/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package Classes.Utilities;

import java.util.Random;

/**
 *
 * @author Nkanabo
 */
public class RandomNumbers {
    
    public static int generateNumber(){
        Random rnd = new Random();
        int random = rnd.nextInt(1000000)+100;
        return random;
    }
}
