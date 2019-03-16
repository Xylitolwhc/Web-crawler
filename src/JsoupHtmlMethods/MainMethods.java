package JsoupHtmlMethods;

/*
    @author Xylitolwhc
    @date 2018
 */

import org.jsoup.Connection;
import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;

import java.math.BigDecimal;
import java.util.HashMap;
import java.util.Map;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

/**
 * Created by hanson on 2017/9/20.
 */

public class MainMethods {
    final static String baseurl = "https://www.c5game.com/";

    static Document getHtml(String url) {
        Document doc = null;
        // 访问链接并获取页面内容
        try {
            doc = Jsoup.connect(url).header("Accept-Language", "zh-CN").get();
        } catch (Exception e) {
            e.printStackTrace();
        }
        return doc;
    }

    //检查是否为CSGO商品
    static Boolean forCSGO(Document doc) {
        return !doc.getElementsByClass("sale-item-info").first().getElementsByClass("icon-csgo").isEmpty();
    }

    //得到商品名称
    static String getNameTag(Document doc) {
        Element tag = doc
                .getElementsByClass("sale-item sale-item-csgo").first()
                .getElementsByClass("name").first();
        return tag.text();
    }

    //得到商品steam参考价
    static Double getItemSteamPrice(Document doc) {

        Element tag = doc
                .getElementsByClass("sale-item sale-item-csgo").first()
                .getElementsByClass("sale-item-info").first()
                .getElementsByClass("hero").first()
                .select("span").first();

        String pattern = "(\\d+\\.?\\d*)(\\D+)(\\d+\\.?\\d*)";
        Pattern p = Pattern.compile(pattern);
        Matcher m = p.matcher(tag.text());
        if (m.find()) {
            return Double.parseDouble(m.group(3));
        }
        return -1.0;
    }

    //得到商品的在售最低价格，无在售则返回-1.0
    static Double getItemSellPrice(Document doc) {
        Element tag = doc
                .getElementsByClass("sale-item-lists").first()
                .getElementsByClass("floor-nav").first()
                .getElementsByClass("active").first();

        String pattern = "(\\d+\\.?\\d*)";
        Pattern p = Pattern.compile(pattern);
        Matcher m = p.matcher(tag.text());
        if (m.find()) {
            if (Integer.parseInt(m.group()) != 0) {
                Boolean bar = doc.getElementsByClass("wear-select-area  clearfix").isEmpty();
                if (!bar) {
                    Element t = doc
                            .getElementsByClass("wear-select-area  clearfix").first()
                            .getElementsByClass("active").first()
                            .select("span").get(1);
                    Matcher m1 = p.matcher(t.text());
                    if (m1.find()) {
                        return Double.parseDouble(m1.group());
                    }
                } else {

                    Element e0 = doc.getElementsByClass("sale-item-lists").first()
                            .getElementsByClass("tab-content").first()
                            .getElementsByClass("table sale-item-table").first()
                            .select("span[class]").first();
                    Matcher p2 = p.matcher(e0.text());
                    if (p2.find()) {
                        return Double.parseDouble(p2.group());
                    }
                }
            }
        }
        return -1.0;
    }

    static void searching(Integer id, Double maxpercent) {
        String name = null;
        double sellprice = 0, steamprice = 0, percent = 1;
        try {
            Document doc = getHtml(baseurl + "csgo/" + id + "/S.html");
            name = null;
            sellprice = 0;
            if (forCSGO(doc)) {
                name = getNameTag(doc);
                sellprice = getItemSellPrice(doc);
                steamprice = getItemSteamPrice(doc);
                percent = new BigDecimal(sellprice / steamprice).setScale(4, BigDecimal.ROUND_HALF_UP).doubleValue();
                if (sellprice == -1.0) {
                }//System.out.println(name + " 无在售");
                else if (percent <= maxpercent)
                    System.out.println(id + " " + name + " " + sellprice + " 比例：" + new java.text.DecimalFormat("#.00").format(percent * 100) + "%");
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public static void main(String[] args) {
        int i = 0;
        Document doc = null;
        Map<String, String> cookieMap = new HashMap<>();
        cookieMap.put("C5Machines", "E8RDawefHcGn39GYvwYInw%3D%3D");
        cookieMap.put("C5Appid", "730");
        cookieMap.put("MEIQIA_EXTRA_TRACK_ID", "0tplG6kWcU0EbGUHYg8HICOM0tR");
        cookieMap.put("C5Lang", "zh");
        cookieMap.put("C5Notice1505973777", "close");
        cookieMap.put("aliyungf_tc", "AQAAAGyosUgq8wcAdxgU3trl3aWhAHVc");
        cookieMap.put("C5SessionID", "4jlqcu8nch7k59suu417e4g732");
        cookieMap.put("C5Token", "59c49067ecd2e");
        cookieMap.put("C5Login", "554461063");
        cookieMap.put("Hm_lvt_86084b1bece3626cd94deede7ecf31a8", "1506009635,1506009647,1506054238,1506054248");
        cookieMap.put("Hm_lpvt_86084b1bece3626cd94deede7ecf31a8", "1506080010");
        while (true) {
            try {
                doc = Jsoup.connect("https://www.c5game.com/api/activity/saleBuy.json")
                        .method(Connection.Method.POST)
                        .cookies(cookieMap)
                        .data("sale", "62").post();
            } catch (Exception e) {
                i++;
                System.out.print(i + " ");
                e.printStackTrace();
                continue;
            }
            break;
        }
    }
}
