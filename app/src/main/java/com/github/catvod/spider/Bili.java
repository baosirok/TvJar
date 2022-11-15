package com.github.catvod.spider;

import android.content.Context;
import android.text.TextUtils;
import com.github.catvod.crawler.Spider;
import com.github.catvod.crawler.SpiderDebug;
import com.github.catvod.spider.merge.JD;
import com.github.catvod.spider.merge.Vf;
import java.net.URLEncoder;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

/* loaded from: classes.dex */
public class Bili extends Spider {
    protected JSONObject Aq = null;

    protected HashMap<String, String> Aq() {
        HashMap<String, String> hashMap = new HashMap<>();
        hashMap.put("cookie", "buvid3=666");
        hashMap.put("User-Agent", "Mozilla/5.0 (Macintosh; Intel Mac OS X 10_15_7) AppleWebKit/537.36 (KHTML, like Gecko) Chrome/91.0.4472.114 Safari/537.36");
        return hashMap;
    }

    /* JADX WARN: Removed duplicated region for block: B:19:0x0059 A[Catch: Exception -> 0x0170, TryCatch #0 {Exception -> 0x0170, blocks: (B:5:0x0006, B:7:0x000c, B:9:0x0012, B:11:0x001e, B:12:0x0034, B:14:0x0045, B:16:0x004b, B:17:0x0053, B:19:0x0059, B:21:0x006b, B:22:0x008c, B:23:0x00c0, B:25:0x00c6, B:27:0x00dd, B:28:0x00ee, B:29:0x013c, B:31:0x0152, B:32:0x0154), top: B:37:0x0006 }] */
    /* JADX WARN: Removed duplicated region for block: B:25:0x00c6 A[Catch: Exception -> 0x0170, TryCatch #0 {Exception -> 0x0170, blocks: (B:5:0x0006, B:7:0x000c, B:9:0x0012, B:11:0x001e, B:12:0x0034, B:14:0x0045, B:16:0x004b, B:17:0x0053, B:19:0x0059, B:21:0x006b, B:22:0x008c, B:23:0x00c0, B:25:0x00c6, B:27:0x00dd, B:28:0x00ee, B:29:0x013c, B:31:0x0152, B:32:0x0154), top: B:37:0x0006 }] */
    /* JADX WARN: Removed duplicated region for block: B:31:0x0152 A[Catch: Exception -> 0x0170, TryCatch #0 {Exception -> 0x0170, blocks: (B:5:0x0006, B:7:0x000c, B:9:0x0012, B:11:0x001e, B:12:0x0034, B:14:0x0045, B:16:0x004b, B:17:0x0053, B:19:0x0059, B:21:0x006b, B:22:0x008c, B:23:0x00c0, B:25:0x00c6, B:27:0x00dd, B:28:0x00ee, B:29:0x013c, B:31:0x0152, B:32:0x0154), top: B:37:0x0006 }] */
    /*
        Code decompiled incorrectly, please refer to instructions dump.
        To view partially-correct code enable 'Show inconsistent code' option in preferences
    */
    public java.lang.String categoryContent(java.lang.String r7, java.lang.String r8, boolean r9, java.util.HashMap<java.lang.String, java.lang.String> r10) {
        /*
            Method dump skipped, instructions count: 374
            To view this dump change 'Code comments level' option to 'DEBUG'
        */
        throw new UnsupportedOperationException("Method not decompiled: com.github.catvod.spider.Bili.categoryContent(java.lang.String, java.lang.String, boolean, java.util.HashMap):java.lang.String");
    }

    public String detailContent(List<String> list) {
        JSONObject jSONObject;
        try {
            String str = list.get(0);
            String str2 = new JSONObject(Vf.h("https://api.bilibili.com/x/web-interface/archive/stat?bvid=" + str, Aq())).getJSONObject("data").getLong("aid") + "";
            JSONObject jSONObject2 = new JSONObject(Vf.h("https://api.bilibili.com/x/web-interface/view?aid=" + str2, Aq())).getJSONObject("data");
            JSONObject jSONObject3 = new JSONObject();
            jSONObject3.put("vod_id", str);
            jSONObject3.put("vod_name", jSONObject2.getString("title"));
            jSONObject3.put("vod_pic", jSONObject2.getString("pic"));
            jSONObject3.put("type_name", jSONObject2.getString("tname"));
            jSONObject3.put("vod_year", "");
            jSONObject3.put("vod_area", "");
            jSONObject3.put("vod_remarks", (jSONObject2.getLong("duration") / 60) + "分钟");
            jSONObject3.put("vod_actor", "");
            jSONObject3.put("vod_director", "");
            jSONObject3.put("vod_content", jSONObject2.getString("desc"));
            jSONObject3.put("vod_play_from", "B站");
            ArrayList arrayList = new ArrayList();
            JSONArray jSONArray = jSONObject2.getJSONArray("pages");
            for (int i = 0; i < jSONArray.length(); i++) {
                arrayList.add(jSONArray.getJSONObject(i).getString("part").replace("$", "_").replace("#", "_") + "$" + str2 + "+ " + jSONObject.getLong("cid"));
            }
            jSONObject3.put("vod_play_url", TextUtils.join("#", arrayList));
            JSONArray jSONArray2 = new JSONArray();
            jSONArray2.put(jSONObject3);
            JSONObject jSONObject4 = new JSONObject();
            jSONObject4.put("list", jSONArray2);
            return jSONObject4.toString();
        } catch (Exception e) {
            e.printStackTrace();
            return null;
        }
    }

    public String homeContent(boolean z) {
        JSONObject jSONObject = new JSONObject();
        try {
            jSONObject.put("class", this.Aq.getJSONArray("classes"));
            if (z) {
                jSONObject.put("filters", this.Aq.getJSONObject("filter"));
            }
        } catch (JSONException e) {
            SpiderDebug.log(e);
        }
        return jSONObject.toString();
    }

    public String homeVideoContent() {
        try {
            JSONArray jSONArray = new JSONArray();
            try {
                JSONArray jSONArray2 = new JSONObject(Vf.h("https://api.bilibili.com/x/web-interface/search/type?search_type=video&keyword=窗 白噪音", Aq())).getJSONObject("data").getJSONArray("result");
                for (int i = 0; i < jSONArray2.length(); i++) {
                    JSONObject jSONObject = jSONArray2.getJSONObject(i);
                    JSONObject jSONObject2 = new JSONObject();
                    String string = jSONObject.getString("pic");
                    if (string.startsWith("//")) {
                        string = "https:" + string;
                    }
                    jSONObject2.put("vod_id", jSONObject.getString("bvid"));
                    jSONObject2.put("vod_name", JD.ue(jSONObject.getString("title")).pE());
                    jSONObject2.put("vod_pic", string);
                    jSONObject2.put("vod_remarks", jSONObject.getString("duration").split(":")[0] + "分钟");
                    jSONArray.put(jSONObject2);
                }
            } catch (Exception unused) {
            }
            JSONObject jSONObject3 = new JSONObject();
            jSONObject3.put("list", jSONArray);
            return jSONObject3.toString();
        } catch (Throwable unused2) {
            return "";
        }
    }

    public void init(Context context, String str) {
        super.init(context, str);
        try {
            this.Aq = new JSONObject(Vf.h(str, Aq()));
        } catch (JSONException e) {
            e.printStackTrace();
        }
    }

    public String playerContent(String str, String str2, List<String> list) {
        try {
            String[] split = str2.split("\\+");
            String str3 = split[0];
            String str4 = split[1];
            JSONObject jSONObject = new JSONObject(Vf.h("https://api.bilibili.com/x/player/playurl?avid=" + str3 + "&cid= " + str4 + "&qn=120&fourk=1", Aq()));
            JSONObject jSONObject2 = new JSONObject();
            jSONObject2.put("parse", "0");
            jSONObject2.put("playUrl", "");
            jSONObject2.put("url", jSONObject.getJSONObject("data").getJSONArray("durl").getJSONObject(0).getString("url"));
            jSONObject2.put("header", "{\"Referer\":\"https://www.bilibili.com\",\"User-Agent\":\"Mozilla/5.0 (Macintosh; Intel Mac OS X 10_15_7) AppleWebKit/537.36 (KHTML, like Gecko) Chrome/100.0.4896.127 Safari/537.36\"}");
            jSONObject2.put("contentType", "video/x-flv");
            return jSONObject2.toString();
        } catch (Exception e) {
            e.printStackTrace();
            return null;
        }
    }

    public String searchContent(String str, boolean z) {
        try {
            JSONObject jSONObject = new JSONObject();
            JSONObject jSONObject2 = new JSONObject(Vf.h("https://api.bilibili.com/x/web-interface/search/type?search_type=video&keyword=" + URLEncoder.encode(str), Aq())).getJSONObject("data");
            JSONArray jSONArray = new JSONArray();
            JSONArray jSONArray2 = jSONObject2.getJSONArray("result");
            for (int i = 0; i < jSONArray2.length(); i++) {
                JSONObject jSONObject3 = jSONArray2.getJSONObject(i);
                JSONObject jSONObject4 = new JSONObject();
                String string = jSONObject3.getString("pic");
                if (string.startsWith("//")) {
                    string = "https:" + string;
                }
                jSONObject4.put("vod_id", jSONObject3.getString("bvid"));
                jSONObject4.put("vod_name", JD.ue(jSONObject3.getString("title")).pE());
                jSONObject4.put("vod_pic", string);
                jSONObject4.put("vod_remarks", jSONObject3.getString("duration").split(":")[0] + "分钟");
                jSONArray.put(jSONObject4);
            }
            jSONObject.put("list", jSONArray);
            return jSONObject.toString();
        } catch (Exception e) {
            SpiderDebug.log(e);
            return "";
        }
    }
}
