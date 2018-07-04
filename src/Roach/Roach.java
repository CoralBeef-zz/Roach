package Roach;

import org.jsoup.Connection;
import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;

import java.io.*;
import java.net.SocketTimeoutException;
import java.net.UnknownHostException;
import java.sql.Timestamp;

public class Roach {
    private final Hivemind hvm = Hivemind.assimilate();
    private String URL = "";
    private String[] path = hvm.getHivemindPath();
    private String roachName = "BROODMOTHER";
    private int depth = 0;
    private final RoachLife roach_life;
    private String rlog = "";
    private int timeout = 5000;
    
    public static Roach spawn(String rchname, String targetPage) {
        Roach r = new Roach();
        r.nameThisRoach(rchname);
        r.URL = targetPage;
        r.log("New Roach spawned! Target page: "+targetPage);
        return r;
    }
    
    public String viewRoachLog() {
        this.log("viewing Roach Log");
        return this.rlog;
    }

    private class RoachLife extends Thread {
        @Override
        public void run() {   
            try {
                crawl(URL);
                log("Roach end");
            } catch(IOException ioe) {
                log("Encountered error: "+ioe);
            }
        }
    }
    
    private Roach() {
        roach_life = new RoachLife();
        this.log("spawning a new Roach");
        roach_life.start();
    }
    
    
    //Roach private utility methods
    private void gather(Document d) throws IOException{
        try {
            String[] rpath = hvm.getResultPath();
            String[] toInsert = new String[rpath.length];
            for(int l = 0; l < rpath.length; l++) {
                Elements e = d.select(Hivemind.parsePath(rpath[l]));
                if(Hivemind.parsePathCode(rpath[l]) == Hivemind.MULTI) {
                    for(String result : Hivemind.parsePathResultToReturn(e, rpath[l]).split(" ")) {
                        log("Multi-gathering "+Hivemind.parsePath(rpath[l])+ " and found "+result);
                        toInsert[l] = result;
                        hvm.writeToDB(toInsert);
                    }
                } else {
                    String found = Hivemind.parsePathResultToReturn(e, rpath[l]);
                    log("Gathering "+Hivemind.parsePath(rpath[l])+ " and found "+found);
                    toInsert[l] = found; 
                    hvm.writeToDB(toInsert);
                }
            }
            log("Gathering done! Moving on!");
            decreaseDepth();
        } catch (IllegalArgumentException siteNotFound) {
            log("Gathering Error!  ERR:" + siteNotFound.toString());
        }
    }
    
    private void decreaseDepth() {
        depth -= hvm.getHivemindPath().length <= 0 ? 0 : hvm.getHivemindPath().length < 2 ? 1 : 2;
    }
    
    private void crawl(String url) throws IOException {
        String urlx = Hivemind.htmlHeaderCheck(url);
        log("Crawling to "+urlx+" with tunnel depth "+depth);
        try {
            Document d = Jsoup.connect(urlx).timeout(timeout).get();
            boolean targetPageConfirmed = hvm.targetPageConfirm(d);
            if(targetPageConfirmed) log("Target page identified! URL: "+urlx);
            else log("This page is not the target page");
            if(depth >= path.length && !targetPageConfirmed) {
                log("Finished digging but target page not found. Moving on.");
                decreaseDepth();
            }
            else if(depth >= path.length && targetPageConfirmed) {
                this.gather(d);
            }
            else {                
                log("Digging pathway: "+path[depth]);
                Elements e = d.select(Hivemind.parsePath(path[depth]));
        
                for(Element el : e) {
                    String link = "Empty";
                    try {
                        link = el.attr("abs:href");
                        if(depth <= path.length) {
                            depth++;
                            this.crawl(link);
                            log("Crawling to next tunnel in depth "+(depth+1));
                        } else {
                            log("Journey end");
                            return;
                        }
                    } catch(IOException ioexc) {
                        log("Encountered error "+ioexc.toString());
                    }
                }
            }
        } catch (IllegalArgumentException | SocketTimeoutException | UnknownHostException exc) {
            if(exc instanceof SocketTimeoutException && timeout <= Hivemind.MAX_TIMEOUT) {
                log("Socket Timed out. Timeout extending from "+timeout+"ms to "+(timeout+2000)+"ms and retrying");
                timeout+=2000;
                crawl(url);
            } else if(exc instanceof SocketTimeoutException && timeout >= Hivemind.MAX_TIMEOUT) 
                log("Roach exceeded Maximum Timeout ("+Hivemind.MAX_TIMEOUT+"ms)");
            else if(exc instanceof UnknownHostException) {
                log("Bad hyperlink found ["+url+"] Fixed: ["+hvm.putHostname(url)+"]");
                crawl(hvm.putHostname(url));
            }
            else log("Digging error, trying another tunnel  ERR:" + exc.toString());
        }
    }
    
    private void nameThisRoach(String name) {
        if(this.roachName.equals("BROODMOTHER")) this.roachName = name;
        else log("Attempt to rename "+this.roachName+" failed!");
    }
    
    private void log(String log_entry) {
        String out = "["+ new Timestamp(System.currentTimeMillis()) + "";
        out += (out.length() >= 24 ? "" : "X") + "]  "+this.roachName+" reports:  ";
        for(int l = 0; l < depth; l++) out+="--";
        out+=log_entry;
        System.out.println(out);
        rlog+=out+"\n";
    }
    
}