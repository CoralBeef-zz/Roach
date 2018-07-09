package RoachTesters;

import Roach.Hivemind;
import java.io.IOException;

/**
 *
 * @author donne
 */
public class MultiPageGet {
    public static final int SEIYU = 0;
    
    public static void main(String[] args) throws IOException {
        MultiPageGet mpg = new MultiPageGet();
        System.out.println("The price in SEIYU is "+mpg.getPrice("4901020293013", SEIYU));
    }
    
    public static String getPrice(String jan, int target) throws IOException{
        
        Hivemind hv = Hivemind.assimilate();
        switch(target) {
            
            case SEIYU:
                String[] hp = {
                  "#searchList > div > table > tbody > tr > td.W140.alR.priceTd >"
                        + " div > span.fPrice.f16.priceBlk__price > strong"
                };
                hv.setHivemindPath(hp);
                hv.setTargetPageConfirmStringLocation("#searchList > div > table > tbody > tr > "
                        + "td.W140.alR.priceTd > div > span.fPrice.f16.priceBlk__price > span");
                String[] hcs = { "¥" };
                hv.setTargetPageStringsToFind(hcs);
                String[] hrp = {
                };
                String[] toSave = { "Price" };

                hv.setResultPath(hrp);
                hv.setFieldsToUpload(toSave);

                for(int l = 0; l < 1; l++) hv.spawnRoach("https://www.the-seiyu.com/front/app/catalog/list/init?searchWord="+jan);
                return hv.getPrintStream();
            //break;
        }
        return "ERROR";
    }
}
