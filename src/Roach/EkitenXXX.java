package Roach;

import java.io.IOException;

public class EkitenXXX {
    public static void main(String[] args) throws IOException{
        Hivemind hv = Hivemind.assimilate();
        String[] hp = {
          "#sitemap > table:nth-child(5) > tbody > tr > td > ul > li > a",
          "#sitemap > table:nth-child(5) > tbody > tr > td > ul > li > a",
          "#sitemap > table:nth-child(5) > tbody > tr > td > ul > li > a",
          "#sitemap > table:nth-child(5) > tbody > tr > td > ul > li > a",
          "#sitemap > table:nth-child(5) > tbody > tr > td > ul > li > a",
        };
        hv.setHivemindPath(hp);
        hv.setTargetPageConfirmStringLocation("#content > h3");
        String[] hcs = { "地域で探す" };
        hv.setTargetPageStringsToFind(hcs);
        String[] hrp = {
            "/multi/#area_search_list > table > tbody > tr > td > a"
        };
        String[] toSave = { "District"};

        hv.setResultPath(hrp);
        hv.setFieldsToUpload(toSave);
        
        for(int l = 0; l < 1; l++) hv.spawnRoach("http://www.ekiten.jp/documents/sitemap.html");
        
        
    }
}