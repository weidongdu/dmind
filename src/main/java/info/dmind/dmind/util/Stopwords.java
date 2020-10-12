package info.dmind.dmind.util;

import java.util.ArrayList;
import java.util.Arrays;

public class Stopwords {

    private static final String[] WORD_ARR = {
            "的", "一", "是", "在", "不", "了", "有", "和", "人", "这", " ", "　", "中", "大", "为", "上", "个", "国", "我"
            , "以", "要", "他", "时", "来", "用", "们", "生", "到", "作", "地", "于", "出", "就", "分", "对", "成", "会", "可"
            , "主", "发", "年", "动", "同", "工", "也", "能", "下", "过", "子", "说", "产", "种", "面", "而", "方", "后", "多"
            , "定", "行", "学", "法", "所", "民", "得", "经", "十", "三", "之", "进", "着", "等", "部", "度", "家", "电", "力"
            , "里", "如", "水", "化", "高", "自", "二", "理", "起", "小", "物", "现", "实", "加", "量", "都", "两", "体", "制"
            , "机", "当", "使", "点", "史", "雨", "婷", "去", "把", "性", "好", "应", "开", "它", "合", "还", "因", "由", "其"
            , "些", "然", "前", "外", "天", "政", "四", "日", "那", "社", "义", "事", "平", "形", "相", "全", "表", "间", "样"
            , "与", "关", "各", "重", "新", "线", "内", "数", "正", "心", "反", "你", "明", "看", "原", "又", "么", "利", "比"
            , "或", "但", "质", "气", "第", "向", "道", "命", "此", "变", "条", "只", "没", "结", "解", "问", "意", "建", "月"
            , "公", "无", "系", "军", "很", "情", "者", "最", "立", "代", "想", "已", "通", "并", "提", "直", "题", "党", "程"
            , "展", "五", "果", "料", "象", "员", "革", "位", "入", "常", "文", "总", "次", "品", "式", "活", "设", "及", "管"
            , "特", "件", "长", "求", "老", "头", "基", "资", "边", "流", "路", "级", "少", "图", "山", "统", "接", "知", "较"
            , "本", "将", "组", "见", "计", "别", "她", "手", "角", "期", "根", "论", "运", "农", "指", "几", "九", "区", "强"
            , "放", "决", "西", "被", "干", "做", "必", "战", "先", "回", "则", "任", "取", "据", "处", "队", "南", "给", "色"
            , "光", "门", "即", "保", "治", "北", "造", "百", "规", "热", "领", "七", "海", "口", "东", "导", "器", "压", "志"
            , "世", "金", "增", "争", "济", "阶", "油", "思", "术", "极", "交", "受", "联", "什", "认", "六", "共", "权", "收"
            , "证", "改", "清", "己", "美", "再", "采", "转", "更", "单", "风", "切", "打", "白", "教", "速", "花", "带", "安"
            , "场", "身", "车", "例", "真", "务", "具", "万", "每", "目", "至", "达", "走", "积", "示", "议", "声", "报", "斗"
            , "完", "类", "八", "离", "华", "名", "确", "才", "科", "张", "信", "马", "节", "话", "米", "整", "空", "元", "况"
            , "今", "集", "温", "传", "土", "许", "步", "群", "广", "石", "记", "需", "段", "研", "界", "拉", "林", "律", "叫"
            , "且", "究", "观", "越", "织", "装", "影", "算", "低", "持", "音", "众", "书", "布", "复", "容", "儿", "须", "际"
            , "商", "非", "验", "连", "断", "深", "难", "近", "矿", "千", "周", "委", "素", "技", "备", "半", "办", "青", "省"
            , "列", "习", "响", "约", "支", "般", "从", "感", "劳", "便", "团", "往", "酸", "历", "市", "克", "何", "除", "消"
            , "构", "府", "称", "太", "准", "精", "值", "号", "率", "族", "维", "划", "选", "标", "写", "存", "候", "毛", "亲"
            , "快", "效", "斯", "院", "查", "江", "型", "眼", "王", "按", "格", "养", "易", "置", "派", "层", "片", "始", "却"
            , "专", "状", "育", "厂", "京", "识", "适", "属", "圆", "包", "火", "住", "调", "满", "县", "局", "照", "参", "红"
            , "细", "引", "听", "该", "铁", "价", "严", "首", "底", "液", "官", "德", "随", "病", "苏", "失", "尔", "死", "讲"
            , "配", "女", "黄", "推", "显", "谈", "罪", "神", "艺", "呢", "席", "含", "企", "望", "密", "批", "营", "项", "防"
            , "举", "球", "英", "氧", "势", "告", "李", "台", "落", "木", "帮", "轮", "破", "亚", "师", "围", "注", "远", "字"
            , "材", "排", "供", "河", "态", "封", "另", "施", "减", "树", "溶", "怎", "止", "案", "言", "士", "均", "武", "固"
            , "叶", "鱼", "波", "视", "仅", "费", "紧", "爱", "左", "章", "早", "朝", "害", "续", "轻", "服", "试", "食", "充"
            , "兵", "源", "判", "护", "司", "足", "某", "练", "差", "致", "板", "田", "降", "黑", "犯", "负", "击", "范", "继"
            , "兴", "似", "余", "坚", "曲", "输", "修", "故", "城", "夫", "够", "送", "笑", "船", "占", "右", "财", "吃", "富"
            , "春", "职", "觉", "汉", "画", "功", "巴", "跟", "虽", "杂", "飞", "检", "吸", "助", "升", "阳", "互", "初", "创"
            , "抗", "考", "投", "坏", "策", "古", "径", "换", "未", "跑", "留", "钢", "曾", "端", "责", "站", "简", "述", "钱"
            , "副", "尽", "帝", "射", "草", "冲", "承", "独", "令", "限", "阿", "宣", "环", "双", "请", "超", "微", "让", "控"
            , "州", "良", "轴", "找", "否", "纪", "益", "依", "优", "顶", "础", "载", "倒", "房", "突", "坐", "粉", "敌", "略"
            , "客", "袁", "冷", "胜", "绝", "析", "块", "剂", "测", "丝", "协", "诉", "念", "陈", "仍", "罗", "盐", "友", "洋"
            , "错", "苦", "夜", "刑", "移", "频", "逐", "靠", "混", "母", "短", "皮", "终", "聚", "汽", "村", "云", "哪", "既"
            , "距", "卫", "停", "烈", "央", "察", "烧", "迅", "境", "若", "印", "洲", "刻", "括", "激", "孔", "搞", "甚", "室"
            , "待", "核", "校", "散", "侵", "吧", "甲", "游", "久", "菜", "味", "旧", "模", "湖", "货", "损", "预", "阻", "毫"
            , "普", "稳", "乙", "妈", "植", "息", "扩", "银", "语", "挥", "酒", "守", "拿", "序", "纸", "医", "缺", "业", "吗"
            , "针", "刘", "啊", "急", "唱", "误", "训", "愿", "审", "附", "获", "茶", "鲜", "粮", "斤", "孩", "脱", "硫", "肥"
            , "善", "龙", "演", "父", "渐", "血", "欢", "械", "掌", "歌", "沙", "著", "刚", "攻", "谓", "盾", "讨", "晚", "粒"
            , "乱", "燃", "矛", "乎", "杀", "药", "宁", "鲁", "贵", "钟", "煤", "读", "班", "伯", "香", "介", "迫", "句", "丰"
            , "培", "握", "兰", "担", "弦", "蛋", "沉", "假", "穿", "执", "答", "乐", "谁", "顺", "烟", "缩", "征", "脸", "喜"
            , "松", "脚", "困", "异", "免", "背", "星", "福", "买", "染", "井", "概", "慢", "怕", "磁", "倍", "祖", "皇", "促"
            , "静", "补", "评", "翻", "肉", "践", "尼", "衣", "宽", "扬", "棉", "希", "伤", "操", "垂", "秋", "宜", "氢", "套"
            , "笔", "督", "振", "架", "亮", "末", "宪", "庆", "编", "牛", "触", "映", "雷", "销", "诗", "座", "居", "抓", "裂"
            , "胞", "呼", "娘", "景", "威", "绿", "晶", "厚", "盟", "衡", "鸡", "孙", "延", "危", "胶", "屋", "乡", "临", "陆"
            , "顾", "掉", "呀", "灯", "岁", "措", "束", "耐", "剧", "玉", "赵", "跳", "哥", "季", "课", "凯", "胡", "额", "款"
            , "绍", "卷", "齐", "伟", "蒸", "殖", "永", "宗", "苗", "川", "妒", "岩", "弱", "零", "杨", "奏", "沿", "露", "杆"
            , "探", "滑", "镇", "饭", "浓", "航", "怀", "赶",

            "a", "b", "c", "d", "e", "f", "g", "h", "i", "j", "k", "l", "m", "n", "o", "p", "q", "r", "s", "t", "u", "v", "w", "x", "y", "z",
            "A", "B", "C", "D", "E", "F", "G", "H", "I", "J", "K", "L", "M", "N", "O", "P", "Q", "R", "S", "T", "U", "V", "W", "X", "Y", "Z",
            "0", "1", "2", "3", "4", "5", "6", "7", "8", "9"

            , "able", "about", "above", "according", "accordingly", "across", "actually", "after", "afterwards"
            , "again", "against", "ain't", "all", "allow", "allows", "almost", "alone", "along", "already", "also"
            , "although", "always", "am", "among", "amongst", "an", "and", "another", "any", "anybody"
            , "anyhow", "anyone", "anything", "anyway", "anyways", "anywhere", "apart", "appear", "appreciate"
            , "appropriate", "are", "aren't", "around", "as", "a's", "aside", "ask", "asking", "associated", "at"
            , "available", "away", "awfully"

            , "be", "became", "because", "become", "becomes", "becoming", "been", "before", "beforehand", "behind"
            , "being", "believe", "below", "beside", "besides", "best", "better", "between", "beyond", "both", "brief"
            , "but", "by"

            , "came", "can", "cannot", "cant", "can't", "cause", "causes", "certain", "certainly", "changes", "clearly"
            , "c'mon", "co", "com", "come", "comes", "concerning", "consequently", "consider", "considering", "contain"
            , "containing", "contains", "corresponding", "could", "couldn't", "course", "c's", "currently"

            , "definitely", "described", "despite", "did", "didn't", "different", "do", "does", "doesn't", "doing"
            , "done", "don't", "down", "downwards", "during"

            , "each", "edu", "eg", "eight", "either", "else", "elsewhere", "enough", "entirely", "especially", "et"
            , "etc", "even", "ever", "every", "everybody", "everyone", "everything", "everywhere", "ex", "exactly"
            , "example", "except"

            , "far", "few", "fifth", "first", "five", "followed", "following", "follows", "for", "former", "formerly"
            , "forth", "four", "from", "further", "furthermore"

            , "get", "gets", "getting", "given", "gives", "go", "goes", "going", "gone", "got", "gotten", "greetings"

            , "had", "hadn't", "happens", "hardly", "has", "hasn't", "have", "haven't", "having", "he", "hello", "help"
            , "hence", "her", "here", "hereafter", "hereby", "herein", "here's", "hereupon", "hers", "herself", "he's"
            , "hi", "him", "himself", "his", "hither", "hopefully", "how", "howbeit", "however"

            , "i'd", "ie", "if", "ignored", "i'll", "i'm", "immediate", "in", "inasmuch"
            , "inc", "indeed", "indicate", "indicated", "indicates", "inner", "insofar", "instead", "into", "inward"
            , "is", "isn't", "it", "it'd", "it'll", "its", "it's", "itself", "i've", "just"

            , "keep", "keeps", "kept", "know", "known", "knows"

            , "last", "lately", "later", "latter", "latterly", "least", "less", "lest", "let"
            , "let's", "like", "liked", "likely", "little", "look", "looking", "looks", "ltd"

            , "mainly", "many", "may", "maybe", "me", "mean", "meanwhile", "merely", "might", "more", "moreover", "most"
            , "mostly", "much", "must", "my", "myself"

            , "name", "namely", "nd", "near", "nearly", "necessary", "need", "needs", "neither"
            , "never", "nevertheless", "new", "next", "nine", "no", "nobody", "non", "none", "noone", "nor", "normally"
            , "not", "nothing", "novel", "now", "nowhere"

            , "obviously", "of", "off", "often", "oh", "ok", "okay", "old", "on", "once", "one", "ones", "only", "onto"
            , "or", "other", "others", "otherwise", "ought", "our", "ours", "ourselves", "out", "outside", "over"
            , "overall", "own"

            , "particular", "particularly", "per", "perhaps"
            , "placed", "please", "plus", "possible", "presumably", "probably", "provides", "que", "quite", "qv"


            , "rather", "rd", "re", "really", "reasonably", "regarding", "regardless", "regards", "relatively"
            , "respectively", "right"


            , "said", "same", "saw", "say", "saying", "says", "second", "secondly", "see", "seeing", "seem", "seemed"
            , "seeming", "seems", "seen", "self", "selves", "sensible", "sent", "serious", "seriously", "seven"
            , "several", "shall", "she", "should", "shouldn't", "since", "six", "so", "some", "somebody", "somehow"
            , "someone", "something", "sometime", "sometimes", "somewhat", "somewhere", "soon", "sorry", "specified"
            , "specify", "specifying", "still", "sub", "such", "sup", "sure"

            , "take", "taken", "tell", "tends", "th", "than", "thank", "thanks", "thanx", "that", "thats", "that's"
            , "the", "their", "theirs", "them", "themselves", "then", "thence", "there", "thereafter", "thereby"
            , "therefore", "therein", "theres", "there's", "thereupon", "these", "they", "they'd", "they'll", "they're"
            , "they've", "think", "third", "this", "thorough", "thoroughly", "those", "though", "three", "through"
            , "throughout", "thru", "thus", "to", "together", "too", "took", "toward", "towards", "tried", "tries"
            , "truly", "try", "trying", "t's", "twice", "two"

            , "un", "under", "unfortunately", "unless", "unlikely", "until", "unto", "up", "upon", "us", "use", "used"
            , "useful", "uses", "using", "usually"

            , "value", "various", "very", "via", "viz", "vs"

            , "where", "whereafter", "whereas", "whereby", "wherein", "where's", "whereupon", "wherever", "whether"
            , "which", "while", "whither", "who", "whoever", "whole", "whom", "who's", "whose", "why", "will", "willing"
            , "wish", "with", "within", "without", "wonder", "won't", "would", "wouldn't"

            , "yes", "yet", "you", "you'd", "you'll", "your", "you're", "yours", "yourself", "yourselves", "you've"

            , "zero", "zt", "ZT", "zz", "ZZ"

            , "你好", "嘛", "哎呀","吗"

            ,"", "`", "-", "=", "[", "]", "\\", ";", "'", ",", ".", "/"
            , "~", "!", "@", "#", "$", "%", "^", "&", "*", "(", ")", "_", "+", "{", "}", "|", ":", "\"", "<", ">", "?"
            , "·", "-", "=", "【", "】", "、", "；", "‘", "’", "，", "。", "、"
            , "~", "！", "@", "#", "￥", "%", "……", "&", "*", "（", "）", "——", "+", "{", "}", "|", "：", "“", "”", "《", "》", "？"

    };

    public static final ArrayList<String> STOP_WORD_LIST = new ArrayList<>(Arrays.asList(WORD_ARR)) ;
}
