package Roach;

import java.io.IOException;

public class Towns {
    public static void main(String[] args) throws IOException {
        Hivemind hv = Hivemind.assimilate();
        String[] hp = {
          "body > div.l-wrapper > div > div.l-contents_wrapper > main > div "
                + "> div > ul > li > div.grouped_list_body > ul "
                + "> li > a"
        };
        hv.setHivemindPath(hp);
        hv.setTargetPageConfirmStringLocation("body > div.l-wrapper > div > div.l-top_contents "
                + "> div.js_search_condition_wrapper > div.box.p-search_condition.js_search_condition "
                + "> div > div.layout_media.item_center.p-search_condition_drop_down_area > nav > ul "
                + "> li > div > div > div > h2");
        String[] hcs = { ".+の市区町村" };
        hv.setTargetPageStringsToFind(hcs);
        String[] hrp = {
            "body > div.l-wrapper > div > div.l-top_contents > div.js_search_condition_wrapper "
                + "> div.box.p-search_condition.js_search_condition > div "
                + "> div.layout_media.item_center.p-search_condition_drop_down_area "
                + "> nav > ul > li:nth-child(1) > div > div > div > div:nth-child(3) > ul > li > em",
            "body > div.l-wrapper > div > div.l-top_contents > div.js_search_condition_wrapper "
                + "> div.box.p-search_condition.js_search_condition > div "
                + "> div.layout_media.item_center.p-search_condition_drop_down_area > nav > ul "
                + "> li:nth-child(1) > div > div > div > div > ul > li > div.grouped_list_body > ul > li"
        };
        String[] toSave = { "Prefectures", "Towns" };

        hv.setResultPath(hrp);
        hv.setFieldsToUpload(toSave);
        
        for(int l = 0; l < 1; l++) hv.spawnRoach("//www.ekiten.jp/cat_seitai/");
        
        
    }    
}