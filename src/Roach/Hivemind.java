package Roach;

import java.io.IOException;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.ArrayList;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;
import java.util.regex.Matcher;
import java.util.regex.Pattern;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;

public class Hivemind {
    //Singleton pattern
    //There should be only one hivemind instance for all roaches. Use assimilate() to connect to the hivemind
    private Hivemind() {
        this.initializeConnection();
	Runtime.getRuntime().addShutdownHook(new HivemindShutdownHook());
    }
    private static class HivemindSingletonHelper{
        private static final Hivemind SPAWN = new Hivemind();
    }
    
    public static Hivemind assimilate() {
        return HivemindSingletonHelper.SPAWN;
    }
    
    
    //Hivemind implementation
    public static final int TEXT = 0;
    public static final int A = 1;
    public static final int IMG = 2;
    
    public static final int MULTI = 100;
    
    protected static final int MAX_TIMEOUT = 30000;
    private boolean isPrintMode = false;
    private String printstream = "";
    private String[] hvpath;
    private String[] cstofind;
    private String[] rspath;
    private String[] ttable;
    private String cstring;
    private String hostname = "";
    private final Map<String, Roach> roachSpawnList = new ConcurrentHashMap<>();
    private int currentRoachID = 1;
    private Connection conn = null;
    private Statement stat = null;
    
    public void spawnRoach(String targetHomepage) throws IOException{
        if(hostname.equals("")) memorizeHostname(targetHomepage);
        String rn = "D0R"+currentRoachID;
        Roach roach = Roach.spawn(rn,targetHomepage);
        roachSpawnList.put(rn,roach);
        currentRoachID++;
    }
    
    public void spawnRoach(String targetPage, int targetDepth) throws IOException {
        if(hostname.equals("")) memorizeHostname(targetPage);
        String rn = "D"+targetDepth+"R"+currentRoachID;
        Roach roach = Roach.spawn(rn, targetPage);
        roachSpawnList.put(rn,roach);
        currentRoachID++;
    }
    
    //Hivemind utilities
    protected synchronized void writeToDB(Object toInsert) {
        try {
            String tname = hostname.replaceAll("^www.","");
            tname = tname.replaceAll("\\.", "_");
            String entrytype = " VARCHAR(2048) CHARACTER SET utf8 COLLATE utf8_general_ci";
            
            String createtable = "CREATE TABLE IF NOT EXISTS "+tname+"("
                    + tname + "_id int NOT NULL AUTO_INCREMENT PRIMARY KEY, ";
            for(int l = 0; l < ttable.length; l++) createtable+=(ttable[l]
                    + entrytype
                + (l <(ttable.length-1) ? "," : ");"));
            stat.execute(createtable);
        
            //Check if this field exists, if it doesn't, add it to the table
            for(int l = 0; l < ttable.length; l++) 
                if(!ifColumnExists(tname, ttable[l])) 
                    stat.executeUpdate("ALTER TABLE "+tname+" ADD "+ttable[l]+" "+entrytype+";");
            
            if(isPrintMode) {
                System.out.println(toInsert);
                this.printstream = toInsert.toString();
            } else {
                insertToDB(tname, toInsert);
            }
            
        } catch(SQLException exc) {
            System.out.println(exc.toString());
        }
    }
    
    private synchronized void insertToDB(String tname, Object toInsert) {
        try {
            String que = "";
            que += "INSERT INTO "+tname+"(";
            for(int l = 0; l < ttable.length; l++) que+=ttable[l]+(l < (ttable.length-1) ? ", " : "");
            que+=") VALUES (";
            String[] toInsert2 = (String[])toInsert;
            for(int l = 0; l < toInsert2.length; l++) que+="'"+toInsert2[l]+ (l < (toInsert2.length-1) ? "', " : "'");
            que+=");";
            
            stat.executeUpdate(que);
        } catch(SQLException exc) { }
    }
    
    public synchronized boolean targetPageConfirm(Document d) {
        int thingsfound = 0;
        try {
            Elements e = d.select(cstring);
            for(Element el : e) {
                for(String toFind : cstofind) {
                    if(el.text().matches(toFind)) thingsfound++;
                    if(thingsfound==cstofind.length) break;
                }
            }
            return (thingsfound==cstofind.length);
        } catch(IllegalArgumentException siteNotFound) {
            System.out.println(siteNotFound.toString());
            return false;
        }
    }
    public static synchronized String htmlHeaderCheck(String url) throws IOException {
        String out = "";        
        if(!url.matches("^http.+$")) out+= "https";
        //if(httpsCheck(url)) out+= "s"; This line causes errors. This should have been checking if http or https
        if(!url.matches("^https*:.+")) out+= ":";
        if(!url.matches("^h*t*t*p*s*:*\\/.+")) out+= "/";
        if(!url.matches("^h*t*t*p*s*:*\\/\\/.+")) out+= "/";
        out+=url;
        return out;
    }
    
    public static String extractHostname(String url) throws IOException {
        if(Pattern.compile("^h*t*t*p*s*:*\\/*\\/*.+[\\.]+.+\\/*$").matcher(url).find()) {
            Matcher mat = Pattern.compile("[\\da-zA-Z]+\\.{1}[\\da-zA-Z]+[\\.+[\\da-zA-Z]+]*").matcher(url);
            if(mat.find()) return mat.group(0);
        }
        return url;
    }
    
    public synchronized String putHostname(String url) throws IOException {
        String out = "//"+this.hostname;
        int start_point = 0;
        Matcher mat = Pattern.compile("^h*t*t*p*s*:*\\/*\\/+[\\da-zA-z]{1}").matcher(url);
        if(mat.find()) start_point = (mat.group(0).length())-1;
        out+="/"+url.substring(start_point,url.length());
        
        return out;
    }
    
    public boolean ifColumnExists(String tname, String col) {
        try {
            stat.executeQuery("SELECT * FROM "+tname+" WHERE "+col+" = '';"); 
            return true;
        } catch(SQLException exc) {
            return false;
        }
    }
    
    public boolean ifIdExists(String tname, int id) {
        boolean out = false;
        try {
            try (ResultSet rs = stat.executeQuery("SELECT * FROM "+tname+" WHERE "+tname+"_id = "+id+";")) {
                out = rs.next();
            }
            return out;
        } catch(SQLException exc) {
            return out;
        }
    }
    
    public static String parsePath(String inp) {
        return inp.replaceAll("^\\/.+\\/", "");
    }
    
    public static String parsePathResultToReturn(Elements e, String path) {
        switch(Hivemind.parsePathCode(path)) {
            case Hivemind.TEXT: return e.text();
            case Hivemind.A: return e.attr("abs:href");
            case Hivemind.IMG: return e.attr("abs:src");
            default: return e.text();
        }
    }
    
    public static int parsePathCode(String path) {
        int out = 0;
        Matcher match= Pattern.compile("^\\/.+\\/").matcher(path);
        String pathcode = path;
        
        if(match.find()) pathcode = match.group(0);
        if(!pathcode.equals("")) {
            switch(pathcode.substring(1,pathcode.length()-1).toUpperCase()) {
                case "A": out = Hivemind.A; break;
                case "IMG": out = Hivemind.IMG; break;
                
                case "MULTI": out = Hivemind.MULTI; break;
                default: out = Hivemind.TEXT; break;
            }
        }
        return out;
    }
    
    public void splitResult(String resultColumnName, String resultToSplit, String splitterString) {
        try {
            String tname = hostname.replaceAll("^www.","");
            tname = tname.replaceAll("\\.", "_");
            ArrayList<String> rlist = new ArrayList<>();
            ResultSet rs = stat.executeQuery("SELECT * FROM "+tname+" WHERE "+resultColumnName+" = '"+resultToSplit+"''");
            System.out.println("SELECT * FROM "+tname+" WHERE "+resultColumnName+" = '"+resultToSplit+"''");
            if(rs.next()) for (String ttable1 : ttable) rlist.add(rs.getString(ttable1));
            String[] splits = rs.getString(resultColumnName).split(splitterString);
            
            for (String split : splits) {
                String ins = "INSERT INTO "+tname+" (";
                for(int l2 = 0; l2 < ttable.length; l2++) ins+=ttable[l2]+(l2 < (ttable.length-12) ? ", " : "");
                ins += ") VALUES (";
                for(int l2 = 0; l2 < rlist.size(); l2++) ins+="'"+rlist.get(l2) + (l2 < (rlist.size()-12) ? ", " : "");
                System.out.println(ins);
                stat.executeUpdate(ins);
                
                String upd = "UPDATE "+tname+" SET ("+resultColumnName+"='"+split+"') WHERE "+tname+"_id = "+rs.getInt(tname+"_id");
                System.out.println(upd);
                stat.executeUpdate(upd);
                String del = "DELETE FROM "+tname+" WHERE "+tname+"_id = "+rs.getInt(tname+"_id");
                System.out.println(del);
                stat.executeUpdate(del);
            }
            
            rs.close();
        } catch(SQLException exc) {
            System.out.println(exc.toString());
        }
    }
    
    
    
    //Hivemind get-set methods
    public synchronized String[] getHivemindPath() {
        return this.hvpath;
    }
    
    public synchronized String[] getResultPath() {
        return this.rspath;
    }
    
    public synchronized String getPrintStream() {
        return this.printstream;
    }
    
    public synchronized void setHivemindPath(String[] path) {
        this.hvpath = path;
    }
    
    public synchronized void setTargetPageConfirmStringLocation(String confirmString) {
        this.cstring = confirmString;
    }
    
    public synchronized void setTargetPageStringsToFind(String[] stringsToFind) {
        this.cstofind = stringsToFind;
    }
    
    public synchronized void setFieldsToUpload(String[] fields) {
        this.ttable = fields;
    }
    
    public synchronized void setResultPath(String[] path) {
        this.rspath = path;
    }
    
    public synchronized void setPrintMode() {
        this.isPrintMode = true;
    }
    
    public synchronized void setPrintMode(boolean turnOffIfFalse) {
        this.isPrintMode = turnOffIfFalse;
    }

    //Private methods
    
    
    private void memorizeHostname(String url) throws IOException{
        this.hostname = Hivemind.extractHostname(url);
    }
    
    private void initializeConnection() {
        try {
            Class.forName("com.mysql.jdbc.Driver");
            String url = "jdbc:mysql:///roachdb?useUnicode=true&characterEncoding=utf-8"; 
            conn = DriverManager.getConnection(url,"root",""); 
            stat = conn.createStatement();
        } catch (ClassNotFoundException | SQLException e) { 
            System.err.println(e.getMessage()); 
        } 
    }
    
    private void close() {
        try {
            stat.close();
            conn.close();
        } catch(SQLException e) {
            System.err.println(e.getMessage()); 
        }
    }
    
    private class HivemindShutdownHook extends Thread {
	@Override
	public void run(){
            System.out.println("Closing Hivemind");
            Hivemind.assimilate().close();
	}
    }
    
    //Not used yet
    private static boolean httpsCheck(String url) throws IOException{
        return false;
    }
    
}