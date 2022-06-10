/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package Classes.Utilities;

/**
 *
 * @author Nkanabo
 */
import Classes.Functions.Notifications;
import Classes.Functions.Stocks;
import static com.nkanabo.Tienda.Utilities.Notifications;
import java.net.URISyntaxException;
import java.text.ParseException;
import java.util.Date;

import java.util.Timer;

import java.util.TimerTask;
import java.util.logging.Level;
import java.util.logging.Logger;

public class StockThread {

    long delay = 10 * 1000; // delay in milliseconds
    LoopTask task = new LoopTask();
    Timer timer = new Timer("TaskName");

    public void start() {
        timer.cancel();
        timer = new Timer("TaskName");
        Date executionDate = new Date(); // no params = now
        timer.scheduleAtFixedRate(task, executionDate, delay);
    }

    private class LoopTask extends TimerTask {
        @Override
        public void run() {
            try {
                Stocks.crawlStock();
                Notifications.crawlEmails();
            } catch (ClassNotFoundException | ParseException | URISyntaxException ex) {
                Logger.getLogger(StockThread.class.getName()).log(Level.SEVERE, null, ex);
            }
        }
    }

    public static void main() {
        StockThread executingTask = new StockThread();
        executingTask.start();
    }
}
