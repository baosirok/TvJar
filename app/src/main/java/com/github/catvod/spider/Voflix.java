package com.github.catvod.spider;

import android.content.Context;
import android.text.TextUtils;
import android.util.Base64;

import com.github.catvod.crawler.Spider;
import com.github.catvod.crawler.SpiderDebug;
import com.github.catvod.utils.okhttp.OKCallBack;
import com.github.catvod.utils.okhttp.OkHttpUtil;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;
import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;

import java.net.URL;
import java.net.URLEncoder;
import java.util.ArrayList;
import java.util.Comparator;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.TreeMap;
import java.util.regex.Matcher;
import java.util.regex.Pattern;




/**
 * 蓝光影院
 * <p>
 * Author: 匿名20220729
 */
public class Voflix extends Spider {
    private static final String siteUrl = "https://www.voflix.com";

    /**
     * 播放源配置
     */
    private JSONObject playerConfig;
    /**
     * 筛选配置
     */
    private JSONObject filterConfig;

    private Pattern regexCategory = Pattern.compile("/type/(\\d+).html");
    private Pattern regexVid = Pattern.compile("/detail/(\\d+).html");
    private Pattern regexPlay = Pattern.compile("/play/(\\d+)-(\\d+)-(\\d+).html");
    private Pattern regexPage = Pattern.compile("/show/(\\S+).html");

    @Override
    public void init(Context context) {
        super.init(context);
        try {
            playerConfig = new JSONObject("{\"duoduozy\":{\"show\":\"VOFLIX\",\"des\":\"\",\"ps\":\"0\",\"parse\":\"https://play.shcpin.com/xplay/?url=\"},\"ysgc\":{\"show\":\"\u84dd\u5149\u4e13\u7ebf\",\"des\":\"\\/static\\/images\\/ico\\/newysgc.png\",\"ps\":\"0\",\"parse\":\"\"},\"sohu\":{\"show\":\"\u641c\u72d0\",\"des\":\"\",\"ps\":\"0\",\"parse\":\"\"},\"qq\":{\"show\":\"\u817e\u8baf\",\"des\":\"\",\"ps\":\"0\",\"parse\":\"\"},\"bilibili\":{\"show\":\"\u54d4\u54e9\",\"des\":\"\",\"ps\":\"0\",\"parse\":\"\"},\"youku\":{\"show\":\"\u4f18\u9177\",\"des\":\"\",\"ps\":\"0\",\"parse\":\"\"},\"qiyi\":{\"show\":\"\u7231\u5947\u827a\",\"des\":\"\",\"ps\":\"0\",\"parse\":\"\"},\"mgtv\":{\"show\":\"\u8292\u679c\",\"des\":\"\",\"ps\":\"0\",\"parse\":\"\"},\"xigua\":{\"show\":\"\u897f\u74dc\",\"des\":\"\",\"ps\":\"0\",\"parse\":\"\"},\"pptv\":{\"show\":\"PPTV\",\"des\":\"\",\"ps\":\"0\",\"parse\":\"\"},\"wujin\":{\"show\":\"\u65e0\u5c3d\u8d44\u6e90\",\"des\":\"\\/static\\/images\\/ico\\/wj.jpg\",\"ps\":\"0\",\"parse\":\"\"}}");
            filterConfig = new JSONObject("{\"1\":[{\"name\":\"\u5e74\u4efd\",\"key\":\"year\",\"value\":[{\"n\":\"\u5168\u90e8\",\"v\":\"\"},{\"n\":\"2022\",\"v\":\"2022\"},{\"n\":\"2021\",\"v\":\"2021\"},{\"n\":\"2020\",\"v\":\"2020\"},{\"n\":\"2019\",\"v\":\"2019\"},{\"n\":\"2018\",\"v\":\"2018\"},{\"n\":\"2017\",\"v\":\"2017\"},{\"n\":\"2016\",\"v\":\"2016\"},{\"n\":\"2015\",\"v\":\"2015\"},{\"n\":\"2014\",\"v\":\"2014\"},{\"n\":\"2013\",\"v\":\"2013\"},{\"n\":\"2012\",\"v\":\"2012\"},{\"n\":\"2011\",\"v\":\"2011\"},{\"n\":\"2010\",\"v\":\"2010\"}]},{\"name\":\"\u5730\u533a\",\"key\":\"area\",\"value\":[{\"n\":\"\u5168\u90e8\",\"v\":\"\"},{\"n\":\"\u5927\u9646\",\"v\":\"\u5927\u9646\"},{\"n\":\"\u9999\u6e2f\",\"v\":\"\u9999\u6e2f\"},{\"n\":\"\u53f0\u6e7e\",\"v\":\"\u53f0\u6e7e\"},{\"n\":\"\u7f8e\u56fd\",\"v\":\"\u7f8e\u56fd\"},{\"n\":\"\u6cd5\u56fd\",\"v\":\"\u6cd5\u56fd\"},{\"n\":\"\u82f1\u56fd\",\"v\":\"\u82f1\u56fd\"},{\"n\":\"\u65e5\u672c\",\"v\":\"\u65e5\u672c\"},{\"n\":\"\u97e9\u56fd\",\"v\":\"\u97e9\u56fd\"},{\"n\":\"\u5fb7\u56fd\",\"v\":\"\u5fb7\u56fd\"},{\"n\":\"\u6cf0\u56fd\",\"v\":\"\u6cf0\u56fd\"},{\"n\":\"\u5370\u5ea6\",\"v\":\"\u5370\u5ea6\"},{\"n\":\"\u610f\u5927\u5229\",\"v\":\"\u610f\u5927\u5229\"},{\"n\":\"\u897f\u73ed\u7259\",\"v\":\"\u897f\u73ed\u7259\"},{\"n\":\"\u52a0\u62ff\u5927\",\"v\":\"\u52a0\u62ff\u5927\"},{\"n\":\"\u5176\u4ed6\",\"v\":\"\u5176\u4ed6\"}]},{\"name\":\"\u7c7b\u578b\",\"key\":\"class\",\"value\":[{\"n\":\"\u5168\u90e8\",\"v\":\"\"},{\"n\":\"\u52a8\u4f5c\",\"v\":\"\u52a8\u4f5c\"},{\"n\":\"\u559c\u5267\",\"v\":\"\u559c\u5267\"},{\"n\":\"\u7231\u60c5\",\"v\":\"\u7231\u60c5\"},{\"n\":\"\u79d1\u5e7b\",\"v\":\"\u79d1\u5e7b\"},{\"n\":\"\u6050\u6016\",\"v\":\"\u6050\u6016\"},{\"n\":\"\u5267\u60c5\",\"v\":\"\u5267\u60c5\"},{\"n\":\"\u6218\u4e89\",\"v\":\"\u6218\u4e89\"},{\"n\":\"\u60ac\u7591\",\"v\":\"\u60ac\u7591\"},{\"n\":\"\u5192\u9669\",\"v\":\"\u5192\u9669\"},{\"n\":\"\u72af\u7f6a\",\"v\":\"\u72af\u7f6a\"},{\"n\":\"\u5947\u5e7b\",\"v\":\"\u5947\u5e7b\"},{\"n\":\"\u60ca\u609a\",\"v\":\"\u60ca\u609a\"},{\"n\":\"\u9752\u6625\",\"v\":\"\u9752\u6625\"},{\"n\":\"\u52a8\u753b\",\"v\":\"\u52a8\u753b\"}]},{\"name\":\"\u6392\u5e8f\",\"key\":\"by\",\"value\":[{\"n\":\"\u6700\u65b0\",\"v\":\"time\"},{\"n\":\"\u4eba\u6c14\",\"v\":\"hits\"},{\"n\":\"\u8bc4\u5206\",\"v\":\"score\"}]}],\"2\":[{\"name\":\"\u5e74\u4efd\",\"key\":\"year\",\"value\":[{\"n\":\"\u5168\u90e8\",\"v\":\"\"},{\"n\":\"2022\",\"v\":\"2022\"},{\"n\":\"2021\",\"v\":\"2021\"},{\"n\":\"2020\",\"v\":\"2020\"},{\"n\":\"2019\",\"v\":\"2019\"},{\"n\":\"2018\",\"v\":\"2018\"},{\"n\":\"2017\",\"v\":\"2017\"},{\"n\":\"2016\",\"v\":\"2016\"},{\"n\":\"2015\",\"v\":\"2015\"},{\"n\":\"2014\",\"v\":\"2014\"},{\"n\":\"2013\",\"v\":\"2013\"},{\"n\":\"2012\",\"v\":\"2012\"},{\"n\":\"2011\",\"v\":\"2011\"},{\"n\":\"2010\",\"v\":\"2010\"}]},{\"name\":\"\u5730\u533a\",\"key\":\"area\",\"value\":[{\"n\":\"\u5168\u90e8\",\"v\":\"\"},{\"n\":\"\u5185\u5730\",\"v\":\"\u5185\u5730\"},{\"n\":\"\u97e9\u56fd\",\"v\":\"\u97e9\u56fd\"},{\"n\":\"\u9999\u6e2f\",\"v\":\"\u9999\u6e2f\"},{\"n\":\"\u53f0\u6e7e\",\"v\":\"\u53f0\u6e7e\"},{\"n\":\"\u65e5\u672c\",\"v\":\"\u65e5\u672c\"},{\"n\":\"\u7f8e\u56fd\",\"v\":\"\u7f8e\u56fd\"},{\"n\":\"\u6cf0\u56fd\",\"v\":\"\u6cf0\u56fd\"},{\"n\":\"\u82f1\u56fd\",\"v\":\"\u82f1\u56fd\"},{\"n\":\"\u65b0\u52a0\u5761\",\"v\":\"\u65b0\u52a0\u5761\"},{\"n\":\"\u5176\u4ed6\",\"v\":\"\u5176\u4ed6\"}]},{\"name\":\"\u7c7b\u578b\",\"key\":\"class\",\"value\":[{\"n\":\"\u5168\u90e8\",\"v\":\"\"},{\"n\":\"\u53e4\u88c5\",\"v\":\"\u53e4\u88c5\"},{\"n\":\"\u6218\u4e89\",\"v\":\"\u6218\u4e89\"},{\"n\":\"\u9752\u6625\u5076\u50cf\",\"v\":\"\u9752\u6625\u5076\u50cf\"},{\"n\":\"\u559c\u5267\",\"v\":\"\u559c\u5267\"},{\"n\":\"\u5bb6\u5ead\",\"v\":\"\u5bb6\u5ead\"},{\"n\":\"\u72af\u7f6a\",\"v\":\"\u72af\u7f6a\"},{\"n\":\"\u52a8\u4f5c\",\"v\":\"\u52a8\u4f5c\"},{\"n\":\"\u5947\u5e7b\",\"v\":\"\u5947\u5e7b\"},{\"n\":\"\u5267\u60c5\",\"v\":\"\u5267\u60c5\"},{\"n\":\"\u5386\u53f2\",\"v\":\"\u5386\u53f2\"},{\"n\":\"\u7ecf\u5178\",\"v\":\"\u7ecf\u5178\"},{\"n\":\"\u4e61\u6751\",\"v\":\"\u4e61\u6751\"},{\"n\":\"\u60c5\u666f\",\"v\":\"\u60c5\u666f\"},{\"n\":\"\u5546\u6218\",\"v\":\"\u5546\u6218\"},{\"n\":\"\u7f51\u5267\",\"v\":\"\u7f51\u5267\"},{\"n\":\"\u5176\u4ed6\",\"v\":\"\u5176\u4ed6\"}]},{\"name\":\"\u6392\u5e8f\",\"key\":\"by\",\"value\":[{\"n\":\"\u6700\u65b0\",\"v\":\"time\"},{\"n\":\"\u4eba\u6c14\",\"v\":\"hits\"},{\"n\":\"\u8bc4\u5206\",\"v\":\"score\"}]}],\"3\":[{\"name\":\"\u5e74\u4efd\",\"key\":\"year\",\"value\":[{\"n\":\"\u5168\u90e8\",\"v\":\"\"},{\"n\":\"2022\",\"v\":\"2022\"},{\"n\":\"2021\",\"v\":\"2021\"},{\"n\":\"2020\",\"v\":\"2020\"},{\"n\":\"2019\",\"v\":\"2019\"},{\"n\":\"2018\",\"v\":\"2018\"},{\"n\":\"2017\",\"v\":\"2017\"},{\"n\":\"2016\",\"v\":\"2016\"},{\"n\":\"2015\",\"v\":\"2015\"},{\"n\":\"2014\",\"v\":\"2014\"},{\"n\":\"2013\",\"v\":\"2013\"},{\"n\":\"2012\",\"v\":\"2012\"},{\"n\":\"2011\",\"v\":\"2011\"},{\"n\":\"2010\",\"v\":\"2010\"}]},{\"name\":\"\u5730\u533a\",\"key\":\"area\",\"value\":[{\"n\":\"\u5168\u90e8\",\"v\":\"\"},{\"n\":\"\u5185\u5730\",\"v\":\"\u5185\u5730\"},{\"n\":\"\u6e2f\u53f0\",\"v\":\"\u6e2f\u53f0\"},{\"n\":\"\u65e5\u97e9\",\"v\":\"\u65e5\u97e9\"},{\"n\":\"\u6b27\u7f8e\",\"v\":\"\u6b27\u7f8e\"}]},{\"name\":\"\u6392\u5e8f\",\"key\":\"by\",\"value\":[{\"n\":\"\u6700\u65b0\",\"v\":\"time\"},{\"n\":\"\u4eba\u6c14\",\"v\":\"hits\"},{\"n\":\"\u8bc4\u5206\",\"v\":\"score\"}]}],\"5\":[{\"name\":\"\u5e74\u4efd\",\"key\":\"year\",\"value\":[{\"n\":\"\u5168\u90e8\",\"v\":\"\"},{\"n\":\"2022\",\"v\":\"2022\"},{\"n\":\"2021\",\"v\":\"2021\"},{\"n\":\"2020\",\"v\":\"2020\"},{\"n\":\"2019\",\"v\":\"2019\"},{\"n\":\"2018\",\"v\":\"2018\"},{\"n\":\"2017\",\"v\":\"2017\"},{\"n\":\"2016\",\"v\":\"2016\"},{\"n\":\"2015\",\"v\":\"2015\"},{\"n\":\"2014\",\"v\":\"2014\"},{\"n\":\"2013\",\"v\":\"2013\"},{\"n\":\"2012\",\"v\":\"2012\"},{\"n\":\"2011\",\"v\":\"2011\"},{\"n\":\"2010\",\"v\":\"2010\"}]},{\"name\":\"\u7c7b\u578b\",\"key\":\"class\",\"value\":[{\"n\":\"\u5168\u90e8\",\"v\":\"\"},{\"n\":\"\u756a\u5267\",\"v\":\"\u756a\u5267\"},{\"n\":\"\u56fd\u521b\",\"v\":\"\u56fd\u521b\"},{\"n\":\"\u52a8\u753b\u7247\",\"v\":\"\u52a8\u753b\u7247\"}]},{\"name\":\"\u6392\u5e8f\",\"key\":\"by\",\"value\":[{\"n\":\"\u6700\u65b0\",\"v\":\"time\"},{\"n\":\"\u4eba\u6c14\",\"v\":\"hits\"},{\"n\":\"\u8bc4\u5206\",\"v\":\"score\"}]}]}");
        } catch (JSONException e) {
            SpiderDebug.log(e);
        }
    }

    /**
     * 爬虫headers
     *
     * @param url
     * @return
     */
    protected HashMap<String, String> getHeaders(String url) {
        HashMap<String, String> headers = new HashMap<>();
        headers.put("method", "GET");
        if (!TextUtils.isEmpty(url)) {
            headers.put("Referer", url);
        }
        headers.put("Accept-Encoding", "gzip, deflate, br");
        headers.put("upgrade-insecure-requests", "1");
        headers.put("DNT", "1");
        headers.put("user-agent", "Mozilla/5.0 (Windows NT 10.0; Win64; x64) AppleWebKit/537.36 (KHTML, like Gecko) Chrome/103.0.0.0 Safari/537.36");
        headers.put("accept", "text/html,application/xhtml+xml,application/xml;q=0.9,image/avif,image/webp,image/apng,*/*;q=0.8,application/signed-exchange;v=b3;q=0.9");
        headers.put("accept-language", "zh-CN,zh;q=0.9");
        return headers;
    }

    /**
     * 获取分类数据 + 首页最近更新视频列表数据
     *
     * @param filter 是否开启筛选 关联的是 软件设置中 首页数据源里的筛选开关
     * @return
     */
    @Override
    public String homeContent(boolean filter) {
        try {
            Document doc = Jsoup.parse(new URL(siteUrl).openStream(), "utf-8",OkHttpUtil.string(siteUrl, getHeaders(siteUrl)));
            // 分类节点
            Elements elements = doc.select("ul[class='navbar-items swiper-wrapper'] >li a");
            JSONArray classes = new JSONArray();
            for (Element ele : elements) {
                String name = ele.text();
                boolean show = true;
                if (filter) {
                    show = name.equals("电影") ||
                            name.equals("剧集") ||
                            name.equals("动漫") ||
                            name.equals("综艺") ||
                            name.equals("国产剧") ||
                            name.equals("日韩剧") ||
                            name.equals("欧美剧");
                }
                if (show) {
                    Matcher mather = regexCategory.matcher(ele.attr("href"));
                    if (!mather.find())
                        continue;
                    String id = mather.group(1).trim();
                    JSONObject jsonObject = new JSONObject();
                    jsonObject.put("type_id", id);
                    jsonObject.put("type_name", name);
                    classes.put(jsonObject);
                }
            }
            JSONObject result = new JSONObject();
            if (filter) {
                result.put("filters", filterConfig);
            }
            result.put("class", classes);
            try {
                Elements list = doc.select("div[class='module-items module-poster-items-base'] >a");
                JSONArray videos = new JSONArray();
                for (int i = 0; i < list.size(); i++) {
                    Element vod = list.get(i);
                    Matcher matcher = regexVid.matcher(vod.selectFirst(".module-poster-item").attr("href"));
                    if (!matcher.find())
                        continue;
                    String title = vod.selectFirst(".module-poster-item").attr("title");
                    String cover = vod.selectFirst("div.module-item-cover div.module-item-pic img").attr("data-original");
                    String remark = vod.selectFirst("div.module-item-cover div.module-item-note").text();
                    if (!TextUtils.isEmpty(cover) && !cover.startsWith("http")) {
                        cover = siteUrl + cover;
                    }
                    String id = matcher.group(1);
                    JSONObject v = new JSONObject();
                    v.put("vod_id", id);
                    v.put("vod_name", title);
                    v.put("vod_pic", cover);
                    v.put("vod_remarks", remark);
                    videos.put(v);
                }
                result.put("list", videos);
            } catch (Exception e) {
                SpiderDebug.log(e);
            }
            return result.toString();
        } catch (Exception e) {
            SpiderDebug.log(e);
        }
        return "";
    }

    /**
     * 获取分类信息数据
     *
     * @param tid    分类id
     * @param pg     页数
     * @param filter 同homeContent方法中的filter
     * @param extend 筛选参数{k:v, k1:v1}
     * @return
     */
    @Override
    public String categoryContent(String tid, String pg, boolean filter, HashMap<String, String> extend) {
        try {
            String[] urlParams = new String[]{"", "", "", "", "", "", "", "", "", "", "", ""};
            urlParams[0] = tid;
            urlParams[8] = pg;
            if (extend != null && extend.size() > 0) {
                for (Iterator<String> it = extend.keySet().iterator(); it.hasNext(); ) {
                    String key = it.next();
                    String value = extend.get(key);
                    if (value.trim().length() == 0)
                        continue;
                    urlParams[Integer.parseInt(key)] = URLEncoder.encode(value);
                }
            }
            // 获取分类数据的url
            String url = siteUrl + "/show/" + TextUtils.join("-", urlParams) + "/";
            String html = OkHttpUtil.string(url, getHeaders(url));
            Document doc = Jsoup.parse(new URL(url).openStream(), "utf-8",OkHttpUtil.string(url, getHeaders(url)));

            JSONObject result = new JSONObject();
            int pageCount = 0;
            int page = -1;

            Elements pageInfo = doc.select("div[id='page'] a");
            if (pageInfo.size() == 0) {
                page = Integer.parseInt(pg);
                pageCount = page;
            } else {
                for (int i = 0; i < pageInfo.size(); i++) {
                    Element li = pageInfo.get(i);
                    Element a = li.selectFirst("a");
                    if (a == null)
                        continue;
                    String name = a.text();
                    if (page == -1 && li.hasClass("page-current")) {
                        Matcher matcher = regexPage.matcher(a.attr("href"));
                        if (matcher.find()) {
                            page = Integer.parseInt(matcher.group(1).split("-")[8]);
                        } else {
                            page = 0;
                        }
                    }
                    if (name.equals("尾页")) {
                        Matcher matcher = regexPage.matcher(a.attr("href"));
                        if (matcher.find()) {
                            pageCount = Integer.parseInt(matcher.group(1).split("-")[8]);
                        } else {
                            pageCount = 0;
                        }
                        break;
                    }
                }
            }

            JSONArray videos = new JSONArray();
            if (!html.contains("没有找到您想要的结果哦")) {
                Elements list = doc.select("div[class='module-items module-poster-items-base'] >a");
                for (int i = 0; i < list.size(); i++) {
                    Element vod = list.get(i);
                    String title = vod.selectFirst(".module-poster-item").attr("title");
                    String cover = vod.selectFirst("div.module-item-cover div.module-item-pic img").attr("data-original");
                    if (!TextUtils.isEmpty(cover) && !cover.startsWith("http")) {
                        cover = siteUrl + cover;
                    }
                    String remark = vod.selectFirst("div.module-item-cover div.module-item-note").text();
                    Matcher matcher = regexVid.matcher(vod.selectFirst(".module-poster-item").attr("href"));
                    if (!matcher.find())
                        continue;
                    String id = matcher.group(1);
                    JSONObject v = new JSONObject();
                    v.put("vod_id", id);
                    v.put("vod_name", title);
                    v.put("vod_pic", cover);
                    v.put("vod_remarks", remark);
                    videos.put(v);
                }
            }
            result.put("page", page);
            result.put("pagecount", pageCount);
            result.put("limit", 48);
            result.put("total", pageCount <= 1 ? videos.length() : pageCount * 48);

            result.put("list", videos);
            return result.toString();
        } catch (Exception e) {
            SpiderDebug.log(e);
        }
        return "";
    }


    private static String Regex(Pattern pattern, String content) {
        if (pattern == null) {
            return content;
        }
        try {
            Matcher matcher = pattern.matcher(content);
            if (matcher.find()) {
                return matcher.group(1).trim();
            }
        } catch (Exception e) {
            SpiderDebug.log(e);
        }
        return content;
    }
    /**
     * 视频详情信息
     *
     * @param ids 视频id
     * @return
     */
    @Override
    public String detailContent(List<String> ids) {
        try {
            // 视频详情url
            String url = siteUrl + "/detail/" + ids.get(0) + ".html";
            Document doc = Jsoup.parse(new URL(url).openStream(), "utf-8",OkHttpUtil.string(url, getHeaders(url)));
            JSONObject result = new JSONObject();
            JSONObject vodList = new JSONObject();

            // 取基本数据
            String cover = doc.selectFirst("div.module-item-pic img").attr("data-original");
            if (!TextUtils.isEmpty(cover) && !cover.startsWith("http")) {
                cover = siteUrl + cover;
            }
            String title = doc.selectFirst("div.module-info-heading h1").text();
            String category = "", area = "", year = "", remark = "", director = "", actor = "", desc = "";
            Elements data = doc.select("div.module-info-items > div");
            desc = doc.selectFirst("div.module-info-introduction-content p").text().trim();
            category =doc.select("div.module-info-tag div").get(2).text().trim();
            year=doc.select("div.module-info-tag div").get(0).text().trim();
            area =doc.select("div.module-info-tag div").get(1).text().trim();
            //year = Regex(Pattern.compile("上映：(\\S+)"), data.get(0).text());
            actor = Regex(Pattern.compile("主演：(\\S+)"), data.nextAll().text());
            director = Regex(Pattern.compile("导演：(\\S+)"), data.nextAll().text());
            // remark=data.select("div.module-info-item-content").text().trim();

            vodList.put("vod_id", ids.get(0));
            vodList.put("vod_name", title);
            vodList.put("vod_pic", cover);
            vodList.put("type_name", category);
            vodList.put("vod_year", year);
            vodList.put("vod_area", area);
            vodList.put("vod_remarks", remark);
            vodList.put("vod_actor", actor);
            vodList.put("vod_director", director);
            vodList.put("vod_content", desc);

            Map<String, String> vod_play = new TreeMap<>(new Comparator<String>() {
                @Override
                public int compare(String o1, String o2) {
                    try {
                        int sort1 = playerConfig.getJSONObject(o1).getInt("or");
                        int sort2 = playerConfig.getJSONObject(o2).getInt("or");

                        if (sort1 == sort2) {
                            return 1;
                        }
                        return sort1 - sort2 > 0 ? 1 : -1;
                    } catch (JSONException e) {
                        SpiderDebug.log(e);
                    }
                    return 1;
                }
            });

            // 取播放列表数据
            Elements sources = doc.select("div[id=y-playList] > div");
            //Elements sourceList = doc.select("div.stui-vodlist__head > ul.stui-content__playlist");

            for (int i = 0; i < sources.size(); i++) {
                Element source = sources.get(i);
                String sourceName = source.attr("data-dropdown-value");
                boolean found = false;
                for (Iterator<String> it = playerConfig.keys(); it.hasNext(); ) {
                    String flag = it.next();
                    if (playerConfig.getJSONObject(flag).getString("show").equals(sourceName)) {
                        //sourceName = flag;
                        sourceName = playerConfig.getJSONObject(flag).getString("show");
                        found = true;
                        break;
                    }
                }
                if (!found)
                    continue;
                String playList = "";
                Elements playListA = doc.select("div.module-play-list div a");
                List<String> vodItems = new ArrayList<>();

                for (int j = 0; j < playListA.size(); j++) {
                    Element vod = playListA.get(j);
                    Matcher matcher = regexPlay.matcher(vod.attr("href"));
                    if (!matcher.find())
                        continue;
                    String playURL = matcher.group(1) + "-" + matcher.group(2) + "-" + matcher.group(3);
                    vodItems.add(vod.text() + "$" + playURL);
                }
                if (vodItems.size() > 0)
                    playList = TextUtils.join("#", vodItems);

                if (playList.length() == 0)
                    continue;

                vod_play.put(sourceName, playList);
            }

            if (vod_play.size() > 0) {
                String vod_play_from = TextUtils.join("$$$", vod_play.keySet());
                String vod_play_url = TextUtils.join("$$$", vod_play.values());
                vodList.put("vod_play_from", vod_play_from);
                vodList.put("vod_play_url", vod_play_url);
            }
            JSONArray list = new JSONArray();
            list.put(vodList);
            result.put("list", list);
            return result.toString();
        } catch (Exception e) {
            SpiderDebug.log(e);
        }
        return "";
    }

    /**
     * 获取视频播放信息
     *
     * @param flag     播放源
     * @param id       视频id
     * @param vipFlags 所有可能需要vip解析的源
     * @return
     */

    private final Pattern urlt = Pattern.compile("\"url\": *\"([^\"]*)\",");
    private final Pattern token = Pattern.compile("\"token\": *\"([^\"]*)\"");
    private final Pattern vkey = Pattern.compile("\"vkey\": *\"([^\"]*)\",");

    @Override
    public String playerContent(String flag, String id, List<String> vipFlags) {
        try {
            JSONObject headers = new JSONObject();
            headers.put("Referer", "https://www.voflix.com");
            headers.put("User-Agent", " Mozilla/5.0 (Windows NT 10.0; Win64; x64) AppleWebKit/537.36 (KHTML, like Gecko) Chrome/86.0.4240.198 Safari/537.36");
            headers.put("Accept", " text/html,application/xhtml+xml,application/xml;q=0.9,image/webp,image/apng,*/*;q=0.8,application/signed-exchange;v=b3;q=0.9");
            headers.put("Accept-Language", " zh-CN,zh;q=0.9,en-GB;q=0.8,en-US;q=0.7,en;q=0.6");
            headers.put("Accept-Encoding", " gzip, deflate");
            String url = siteUrl + "/play/" + id + ".html";
            Document doc = Jsoup.parse(OkHttpUtil.string(url, getHeaders(url)));
Elements allScript = doc.select("script");
            JSONObject result = new JSONObject();
            for (int i = 0; i < allScript.size(); i++) {
                String scContent = allScript.get(i).html().trim();
                if (scContent.startsWith("var player_")) { // 取直链
                    int start = scContent.indexOf('{');
                    int end = scContent.lastIndexOf('}') + 1;
                    String json = scContent.substring(start, end);
                    JSONObject player = new JSONObject(json);
                    if (playerConfig.has(player.getString("from"))) {
                        JSONObject pCfg = playerConfig.getJSONObject(player.getString("from"));
                             if (player.getString("from").contains("duoduozy")) {
                            String videoUrl = pCfg.getString("parse") + player.getString("url");
                            Document docs = Jsoup.parse(OkHttpUtil.string(videoUrl, getHeaders(videoUrl)));
                        Elements allScripts = docs.select("body script");
                        for (int j = 0; j < allScripts.size(); j++) {
                            String Content = allScripts.get(j).html().trim();
                            Matcher matcher = urlt.matcher(Content);
                            if (!matcher.find()) {
                                return "";
                            }
                            String urlt = matcher.group(1);
                            Matcher matcher1 = token.matcher(Content);
                            if (!matcher1.find()) {
                                return "";
                            }
                            String token = matcher1.group(1);
                            Matcher matcher2 = vkey.matcher(Content);
                            if (!matcher2.find()) {
                                return "";
                            }
                            String vkey = matcher2.group(1);
                            String tUrl = "https://play.shcpin.com/xplay/555tZ4pvzHE3BpiO838.php?url=" + urlt + "&vkey=" + vkey + "&token" + token +"&sign=F4penExTGogdt6U8";
                            JSONObject zurl = new JSONObject(OkHttpUtil.string(tUrl, getHeaders(tUrl)));
                            String zurls = new String(Base64.decode(zurl.getString("url").substring(8).getBytes(), 0));
                            result.put("url", zurls.substring(8, url.length() - 8));
                            result.put("header", headers.toString());
                            result.put("parse", 0);
                            result.put("playUrl", "");

                        }
                       }
                      }
                     }
                    }
                    
                
            
            return result.toString();
        } catch (Exception e) {
            SpiderDebug.log(e);
        }
        return "";
    }
    /**
     * 搜索
     *
     * @param key
     * @param quick 是否播放页的快捷搜索
     * @return
     */
    @Override
    public String searchContent(String key, boolean quick) {
        try {
            if (quick)
                return "";
            long currentTime = System.currentTimeMillis();
            String url = siteUrl + "/index.php/ajax/suggest?mid=1&wd=" + URLEncoder.encode(key) + "&limit=10&timestamp=" + currentTime;
            JSONObject searchResult = new JSONObject(OkHttpUtil.string(url, getHeaders(url)));
            JSONObject result = new JSONObject();
            JSONArray videos = new JSONArray();
            if (searchResult.getInt("total") > 0) {
                JSONArray lists = new JSONArray(searchResult.getString("list"));
                for (int i = 0; i < lists.length(); i++) {
                    JSONObject vod = lists.getJSONObject(i);
                    String id = vod.getString("id");
                    String title = vod.getString("name");
                    String cover = vod.getString("pic");
                    if (!TextUtils.isEmpty(cover) && !cover.startsWith("http")) {
                        cover = siteUrl + cover;
                    }
                    JSONObject v = new JSONObject();
                    v.put("vod_id", id);
                    v.put("vod_name", title);
                    v.put("vod_pic", cover);
                    v.put("vod_remarks", "");
                    videos.put(v);
                }
            }
            result.put("list", videos);
            return result.toString();
        } catch (Exception e) {
            SpiderDebug.log(e);
        }
        return "";
    }
}
