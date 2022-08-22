/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package Classes.AbstractClasses;

/**
 *
 * @author Nkanabo
 */
public class Email {
    public String notice_id;
    public String date;
    public String title;
    public String message;
    
    public Email(String notice_id, String date, String title, String message){
        this.notice_id = notice_id;
        this.date = date;
        this.title = title;
        this.message = message;
        
    }
    
}
