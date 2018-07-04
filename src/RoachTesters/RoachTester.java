package RoachTesters;

import Roach.Hivemind;
import java.io.IOException;

public class RoachTester {
    public static void main(String[] args) throws IOException{
        Hivemind hv = Hivemind.assimilate();
        String[] hp = {
          "[href~=h*t*t*p*s*:*\\/\\/r.gnavi.co.jp\\/area\\/aream[\\da-zA-Z]+\\/+$]",
          "div.shopintro__text.ptn-oblong > ul > li.shop__column-item a"
        };
        hv.setHivemindPath(hp);
        hv.setTargetPageConfirmStringLocation("div#info-table > table > tbody > tr > th");
        String[] hcs = { "店名", "電話番号", "住所" };
        hv.setTargetPageStringsToFind(hcs);
        String[] hrp = {
            "p#info-name","p#info-kana","span.number","ul#info-fax","p.note","p.adr"
        };
        String[] toSave = { "name","kana","number","fax","note","adr" };

        hv.setResultPath(hrp);
        hv.setFieldsToUpload(toSave);
        
        for(int l = 0; l < 1; l++) hv.spawnRoach("//r.gnavi.co.jp/");
    }
}