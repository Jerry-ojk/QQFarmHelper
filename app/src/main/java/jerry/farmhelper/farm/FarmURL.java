package jerry.farmhelper.farm;

public class FarmURL {

    public static final String url_main = "/cgi-bin/cgi_farm_index?mod=user&act=run&g_tk=";
    public static final String url_weed = "/cgi-bin/cgi_farm_opt?mod=farmlandstatus&act=clearWeed&g_tk=";
    public static final String url_spray = "/cgi-bin/cgi_farm_opt?mod=farmlandstatus&act=spraying&g_tk=";
    public static final String url_water = "/cgi-bin/cgi_farm_opt?mod=farmlandstatus&act=water&g_tk=";
    public static final String url_scarify = "/cgi-bin/cgi_farm_plant?mod=farmlandstatus&act=scarify&g_tk=";
    public static final String url_harvest = "/cgi-bin/cgi_farm_plant?mod=farmlandstatus&act=harvest&g_tk=";
    public static final String url_plant = "/cgi-bin/cgi_farm_plant?mod=farmlandstatus&act=planting&g_tk=";


    public static final String url_wish_main = "/cgi-bin/cgi_farm_wish_index?g_tk=";
    public static final String url_wish_start = "/cgi-bin/cgi_farm_wish_star?g_tk=";
    //{"ecode":0,"self":{"grow":3047,"lv":39,"next_lv":10000,"status":3,"w_num":7}}
    public static final String url_wish = "/cgi-bin/cgi_farm_wish_help?g_tk=";
    public static final String url_wish_harvest = "/cgi-bin/cgi_farm_wish_harvest?g_tk=";


    /**
     * key: 1534118400
     * ty: 2
     * farmTime: 1534128338
     * farmKey2: 65d7272ffaee955af2d7df02b5890960
     * uIdx: 1583709838
     * farmKey: fb9aa1666def3b7581a3ebed595d5400
     * uinY: 1583605494
     */
    public static final String url_magic = "/cgi-bin/query?act=2010001&g_tk=";
    public static final String url_magic_tower = "https://nc.qzone.qq.com/cgi-bin/query?act=2010030&g_tk=";
    public static final String url_magic_gift = "/cgi-bin/exchange?act=2010010&g_tk=";
    /**
     * uinY: 1583605494
     * uIdx: 1583709838
     * farmKey: 55e8a2a6bd7a9644182dfdc04b5490e4
     * farmTime: 1534128629
     * farmKey2: 7af63ab4768d83b85a23dcf341ebb819
     */
    public static final String url_magic_col = "/cgi-bin/query?act=2010022&g_tk=";
    /**
     * ownerId: 1583709838
     * farmTime: 1534136720
     * farmKey2: fdc0222bae1c957a908ef523c36c0961
     * uIdx: 1583709838
     * farmKey: be5b4bde85311b25a9fe02e3063044ce
     * uinY: 1583605494
     */


    public static final String url_hive = "/cgi-bin/cgi_farm_hive_index?g_tk=";
    /**
     * uinY: 1583605494
     * uIdx: 1583709838
     * farmKey: b5a5bce447f94d62bb557c9b15bfecc9
     * farmTime: 1534136721
     * farmKey2: 00f181caf9c0ec562213bea93ad3dc72
     */
    /**
     * 0
     * addHoney:0
     * addMoney:17500
     * addTool:1
     * code:1
     * ecode:0
     * pkg:[]
     * stamp:1534760221
     * zhuanpan_add:0
     */
    public static final String url_hive_col = "/cgi-bin/cgi_farm_hive_harvest?g_tk=";
    /**
     * farmTime: 1534136785
     * farmKey2: 9493e290a70ee9773732392f75a9b6c2
     * uIdx: 1583709838
     * farmKey: 52b409815bc4099d7bcb1ef40217fb11
     * free: 1(混合)
     * uinY: 1583605494
     */
    public static final String url_hive_seed = "/cgi-bin/cgi_farm_hive_restend?g_tk=";
    /**
     * uinY: 1583605494
     * uIdx: 1583709838
     * farmKey: 41479237153a0931400d6e4a491ce147
     * farmTime: 1534136791
     * farmKey2: c5a905e2edb4cc417539a8447fe5bef7
     */
    public static final String url_hive_work = "/cgi-bin/cgi_farm_hive_work?g_tk=";
    /**
     * farmKey2: 068f003a3d301a3805efd9e4ee5fcfad
     * uinY: 1583605494
     * farmKey: f8f6b52aa7d978e4ab036f1281f3fd60
     * place: 9
     * fName:
     * farmTime: 1534137482
     * tName:
     * ownerId: 1583709838
     * uIdx: 1583709838
     */

    public static final String url_sea = "/cgi-bin/query?act=2110001&g_tk=";
    /**
     * index: 3
     */
    public static final String url_sea_back = "/cgi-bin/exchange?act=2110009&cmd=end&g_tk=";
    /**
     * index: 1
     */
    public static final String url_sea_harvest = "/cgi-bin/exchange?act=2110009&cmd=draw&g_tk=";
    /**
     * areaid: 3
     */
    public static final String url_sea_go = "/cgi-bin/exchange?act=2110009&cmd=go&g_tk=";
}