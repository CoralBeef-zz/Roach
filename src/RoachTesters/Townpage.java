/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package RoachTesters;

import Roach.Hivemind;
import java.io.IOException;

/**
 *
 * @author donne
 */
public class Townpage {
        public static void main(String[] args) throws IOException {
            
            
        Hivemind hv = Hivemind.assimilate();
        String[] hivemindpath = {
            "#contents > div > div > div.mainContents > ul.siteregion > li > a",
            "#wrapAllInside > div.mainWrapper > div.primaryContentWrapper > div.mainContent > div > article.searchMap > "
                + "div.currentAreaFeatures.hide > section > div.map-position.pref_map > ul.area_btns > li > a",
            "#wrapAllInside > div > div.primaryContentWrapper > div > div.searchResultsWrapper > div > article > section > h4 > a",
            "body > div.container > div > nav > div > ul > li.nav-shop > a",
        };
        String[] hivemind_result_path = {
            "body > div.container > div > div > div > div.main > div > article.item.item-table > div > section.item-body.basic > dl:nth-child(1) > dd",
            "body > div.container > div > div > div > div.main > div > article.item.item-table > div > "
                + "section.item-body.basic > dl:nth-child(5) > dd > p:nth-child(1)",
            "body > div.container > div > div > div > div.main > div > article.item.item-table > div > section.item-body.basic > dl:nth-child(3) > dd > p.tell",
            "body > div.container > div > div > div > div.main > div > article.item.item-table > div > "
                + "section.item-body.basic > dl:nth-child(10) > dd > div:nth-child(1) > p:nth-child(2) > a"
        };
        String[] hivemind_confirmation_string = { "基本情報" };
        
        hv.setHivemindPath(hivemindpath);
        hv.setTargetPageConfirmStringLocation("body > div.container > div > div > div > div.main > div > article.item.item-table > div > div > h1");
        hv.setTargetPageStringsToFind(hivemind_confirmation_string);
        hv.setResultPath(hivemind_result_path);
        
        String[] toSave = { "掲載名","住所","電話番号","メールアドレス" };
        hv.setFieldsToUpload(toSave);
        
        for(int l = 0; l < 1; l++) hv.spawnRoach("https://itp.ne.jp/sitemap/");
    }
}