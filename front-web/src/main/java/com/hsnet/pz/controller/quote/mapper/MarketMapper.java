package com.hsnet.pz.controller.quote.mapper;

/**
 * 市场类型 转换
 * @author chensz11448
 *@2014-7-16
 */
public class MarketMapper {

    // 沪深A股 1
    private static final short HSAG_MARKET = 0x1301;

    // 创业板 2
    private static final short CYB_MARKET = 0x120d;

    // 中小板 3
    private static final short ZXB_MARKET = 0x1206;

    // 上证指数 4
    private static final short SHZZS_MARKET = 0x1100;

    // 上证B股 5
    private static final short SHZBG_MARKET = 0x1102;

    // 上证债券 6
    private static final short SHZZQ_MARKET = 0x1103;

    // 上证基金 7
    private static final short SHZJJ_MARKET = 0x1104;

    // 上证ETF 8
    private static final short SHZETF_MARKET = 0x1109;

    // 上证其他 9
    private static final short SHZQT_MARKET = 0x110f;

    // 深证指数 10
    private static final short SZZS_MARKET = 0x1200;

    // 深证B股 11
    private static final short SZBG_MARKET = 0x1202;

    // 深证债券 12
    private static final short SZZQ_MARKET = 0x1203;

    // 深证基金 13
    private static final short SZJJ_MARKET = 0x1204;

    // LOF基金 14
    private static final short LOFJJ_MARKET = 0x1208;

    // 深证ETF 15
    private static final short SZETF_MARKET = 0x1209;

    // 深证其他 16
    private static final short SZQT_MARKET = 0x120f;

    // 市场类型转换
    public static short getMarketType(String marketType) {
        int mt = Integer.parseInt(marketType);
        switch (mt) {
            case 1:
                return HSAG_MARKET;
            case 2:
                return CYB_MARKET;
            case 3:
                return ZXB_MARKET;
            case 4:
                return SHZZS_MARKET;
            case 5:
                return SHZBG_MARKET;
            case 6:
                return SHZZQ_MARKET;
            case 7:
                return SHZJJ_MARKET;
            case 8:
                return SHZETF_MARKET;
            case 9:
                return SHZQT_MARKET;
            case 10:
                return SZZS_MARKET;
            case 11:
                return SZBG_MARKET;
            case 12:
                return SZZQ_MARKET;
            case 13:
                return SZJJ_MARKET;
            case 14:
                return LOFJJ_MARKET;
            case 15:
                return SZETF_MARKET;
            case 16:
                return SZQT_MARKET;
            default:
                return 0;
        }
    }
}
