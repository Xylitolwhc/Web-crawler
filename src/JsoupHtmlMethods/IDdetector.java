package JsoupHtmlMethods;

import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;

import java.util.ArrayList;
import java.util.List;
import java.util.Objects;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

/**
 * @author Created by hanson on 2017/9/20.
 * <img src="C:\Users\wuhan\IdeaProjects\c5game_test\src\JsoupHtmlMethods\imgs/headtem.png" alt="Author Avator">
 * @version v0.0.1
 * {@code xxx}
 */
public class IDdetector {

    /**
     * @param doc the html page you get from the url
     * @return the ids of the items
     * @see <a href="https://jsoup.org/">jsoup Java HTML Parser</a>
     * @see MainMethods#getHtml(String)
     * @since version v0.0.1
     * @deprecated Use <code>xxx</code> instead
     */
    public static List<Integer> getIdList(Document doc) {

        List<Integer> idList = new ArrayList<>();
        Elements items = doc
                .getElementsByClass("tab-content").first()
                .getElementsByClass("list-item4 clearfix ").first()
                .getElementsByClass("selling");
        for (Element e : items) {
            String name = e
                    .getElementsByClass("name").first()
                    .getElementsByClass(" text-unique ").first()
                    .text();

            String url = e
                    .getElementsByClass("name").first()
                    .select("a[href]").first()
                    .attr("href");
            Integer id = -1;
            String pattern = "(\\d+)";
            Pattern p0 = Pattern.compile(pattern);
            Matcher matcher = p0.matcher(url);
            if (matcher.find()) {
                id = Integer.parseInt(matcher.group(1));
            }

            Double price = 0.0;
            String patternID = "(\\d+\\.?\\d*)";
            Pattern p = Pattern.compile(patternID);
            Matcher m = p.matcher(e.getElementsByClass("price").first().text());
            if (m.find()) {
                price = Double.parseDouble(m.group());
            }

//            System.out.println(name);
//            System.out.println(price);
//            System.out.println(id);
            idList.add(id);
        }
        return idList;
    }

    public static void main(String[] args) {
        for (int i = 1; i < 25; i++) {
            String url1 = "https://www.c5game.com/csgo/default/result.html?sort=price.desc&page=" + i,
                    url2 = "https://www.c5game.com/csgo/default/result.html?type=csgo_type_knife&page=" + i,
                    url = "https://www.c5game.com/csgo/default/result.html?min=20&max=30&sort=price.desc&page=" + i;
            Document doc = MainMethods.getHtml(url);
            doc.hashCode();
            Objects.hash();
            List<Integer> idList = getIdList(doc);
            for (Integer id : idList) {
                MainMethods.searching(id, 1.0);
            }
        }
    }
}
