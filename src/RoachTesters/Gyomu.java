package RoachTesters;

import Roach.Hivemind;
import java.io.IOException;

public class Gyomu {
    public static void main(String[] args) throws IOException {
        Hivemind hv = Hivemind.assimilate();
        String[] hivemindpath = {
          "#category > section > ul > li > a",
          "#sec_contents > section > div.row.list_info > div > div > div > div.list_txt > a"
        };
        String[] hivemind_result_path = {
            "#sec_contents > section > div.row.item_detail > div.col-sm-7.detail_info_box > div:nth-child(1) > h3",
            "#sec_contents > section > div.row.item_detail > div.col-sm-7.detail_info_box > p:nth-child(2)",
            "/img/#sec_contents > section > div.row.item_detail > div.col-sm-5 > div > img",
            "#sec_contents > section > div.row.item_detail > div.col-sm-7.detail_info_box > dl:nth-child(3) > dd",
            "#sec_contents > section > div.row.item_detail > div.col-sm-7.detail_info_box > dl:nth-child(4) > dd",
            "#sec_contents > section > div.row.item_detail > div.col-sm-7.detail_info_box > dl:nth-child(5) > dd",
            "#sec_contents > section > div.row.item_detail > div.col-sm-7.detail_info_box > dl:nth-child(6) > dd",
            "#sec_contents > section > div.row.item_detail > div.col-sm-7.detail_info_box > dl.last > dd"
        };
        String[] hivemind_confirmation_string = { "JAN" };
        
        hv.setHivemindPath(hivemindpath);
        hv.setTargetPageConfirmStringLocation("#sec_contents > section > div.row.item_detail "
                + "> div.col-sm-7.detail_info_box > dl:nth-child(4) > dt");
        hv.setTargetPageStringsToFind(hivemind_confirmation_string);
        hv.setResultPath(hivemind_result_path);
        
        String[] toSave = { "Name","Description","Image","Weight", "JAN", "Preservation", "Nutrition", "Allergy" };
        hv.setFieldsToUpload(toSave);
        
        for(int l = 0; l < 1; l++) hv.spawnRoach("http://gyomusuper.jp/item/index.php");
    }    
}