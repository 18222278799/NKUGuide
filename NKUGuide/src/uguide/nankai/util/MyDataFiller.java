package uguide.nankai.util;

import java.util.ArrayList;
import java.util.List;

import uguide.nankai.R;
import uguide.nankai.po.Scenery;

public abstract class MyDataFiller {
	public static List<Scenery> fillSceneryData(){
		List<Scenery> data=new ArrayList<Scenery>();
		data.add(new Scenery().set("东门", "位于南开区卫津路的南开大学东门", R.drawable.sc_nankaidongmen, 0).setLocation(39.1091100000,117.1854710000));
		data.add(new Scenery().set("南门", "位于复康路的南开大学南门", R.drawable.sc_nankainanmen, 0).setLocation(39.1066670000,117.1778890000));
		data.add(new Scenery().set("马蹄湖", "盛夏七月，是天津南开大学马蹄湖一年中最动人的时节。湖内盛开的荷花清馨淡雅、千姿百态，令人赏心悦目，吸引校内外众多摄影爱好者前来捕捉荷花的神韵", R.drawable.sc_matihu, 0).setLocation(39.1094180000,117.1820940000));
		data.add(new Scenery().set("新开湖", "位于南开大学理科图书馆前，宽广而平静，静谧耐人寻味，夜光下和周围建筑交相辉映。", R.drawable.sc_xinkaihu, 0).setLocation(39.1094390000,117.1805660000));
		data.add(new Scenery().set("周恩来总理纪念碑", "周恩来总理纪念碑，这是1979年南开大学60周年校庆之际在此建成的。碑座用花岗岩、碑身用汉白玉制成。在碑左上角镶嵌着周总理的铜制浮雕头像，正中镌刻着周总理的手书——“我是爱南开的”6个金色大字，碑背面镌刻的是由杨石先校长亲笔书写的碑文。", R.drawable.sc_zhouenlaizonglijinian, 0).setLocation(39.1096140000,117.1821500000));
		data.add(new Scenery().set("于方舟烈士纪念碑", "南开大学校园内新开湖北侧的苍松翠柏之间，坐落着于方舟烈士纪念碑。黑色大理石碑座的正面，镌刻着六届全国人大常委会委员长彭真手书的“于方舟烈士纪念碑”", R.drawable.sc_yufangzhou, 0).setLocation(39.1099780000,117.1804580000));
		data.add(new Scenery().set("周总理像", "位于南开大学南门矗立着庄严的周总理的雕像，下面刻着“我是爱南开的”五个字", R.drawable.sc_nankaizhouzongli, 0).setLocation(39.1071990000,117.1778540000));
		data.add(new Scenery().set("杨石先雕塑", "位于敬业广场边上，面朝南方有杨石先雕塑，大气庄重。", R.drawable.sc_yangshixian, 0).setLocation(39.1092150000,117.1740790000));
		data.add(new Scenery().set("校钟", "　南开大学曾有一口老校钟置于思源堂西侧。铜钟钟面铸有整部《金刚经》，重达一万三千余斤。1997年7月，在南开园被侵华日军炸毁60周年之际，为铭记历史，励志未来，南开大学重新铸造校钟。", R.drawable.sc_nankaixiaozhong, 0).setLocation(39.1086200000,117.1778090000));
		data.add(new Scenery().set("体育馆", "南开大学体育中心造型独特新颖，为钢网架结构，从空中俯瞰犹如一只巨型贝壳。南开大学体育中心建成后将集体育教学、体育比赛、大型演出等多功能为一身，可承接大型国际性体育赛事。", R.drawable.sc_nankaitiyu, 0).setLocation(39.1109370000,117.1786000000));
		data.add(new Scenery().set("理科图书馆", "南开大学图书馆老馆建成于1958年，很有历史沧桑感", R.drawable.sc_nankailitu, 0).setLocation(39.1100410000,117.1798570000));
		data.add(new Scenery().set("新图书馆", "新馆即“逸夫楼”建成于1990年", R.drawable.sc_nankaitushu, 0).setLocation(39.1087390000,117.1723570000));
		data.add(new Scenery().set("主楼", "南开大学创建于1919年，创办人是近代著名爱国教育家张伯苓和严修。南开大学主楼1963年建成，2003年又进行了历史上最大规模的装修和翻新。主楼的设计沿中轴对称，气势雄伟，线条明快。楼前广场开阔、大气，绿草茵茵，广场中间巍然挺立的周恩来总理雕像高大而伟岸。", R.drawable.sc_nankaizhulou, 0).setLocation(39.1075700000,117.1779010000));
		data.add(new Scenery().set("第二主教学楼", "二主楼位置在新开湖旁边，从八里台的东门进来的话要走五六分钟，总的来说，这是一个冬暖夏凉的自习好去处啊！", R.drawable.sc_nankaierzhu, 0).setLocation(39.1091450000,117.1777550000));
		data.add(new Scenery().set("图书馆", "南开大学津南校区图书馆于2015年建成，是津南校区的标志性建筑，规模浩大，气势雄伟", R.drawable.sc_nankaijinnantushu, 1).setLocation(38.9925640000,117.3534320000));
		data.add(new Scenery().set("体育馆", "南开大学津南校区体育馆于2015年12月正式启用，体育馆整体上由三部分构成，从外观看起来像一个“三步台阶”，南侧的部分高度最高，在功能上设计作为篮球馆和大型会场使用；中间高度次之的部分作为网球馆、羽毛球馆和综合训练馆使用，北侧的部分是游泳馆。", R.drawable.sc_jinnantiyu, 1).setLocation(38.9973880000,117.3541570000));
		data.add(new Scenery().set("学生活动中心", "南开大学津南校区学生活动中心外形像一个白色的贝壳，美观并且富有创新，为学生提供活动场所。", R.drawable.sc_jinnanxuesheng, 1).setLocation(38.9935670000,117.3567650000));
		data.add(new Scenery().set("体育场", "南开大学津南校区体育场为标准体育场，适合师生的体育锻炼活动", R.drawable.sc_jinnantiyuchang, 1).setLocation(38.9918070000,117.3452910000));
		return data;
	}
	
	public static List<List<String>> fillServiceList(){
		List<List<String>> str_s_s=new ArrayList<List<String>>();
		//餐饮
		List<String> str_s0=new ArrayList<String>();
		str_s0.add("南开大学第一食堂");
		str_s0.add("南开大学第二食堂");
		str_s0.add("开大学学生第三食堂");
		str_s0.add("南开大学职工食堂");
		str_s0.add("南开大学西园食堂");
		str_s0.add("南开大学清真食堂");
		str_s_s.add(str_s0);
		//购物
		List<String> str_s1=new ArrayList<String>();
		str_s1.add("南开浴园便利超市");
		str_s1.add("新三角超市");
		str_s1.add("西南村菜市场");
		str_s_s.add(str_s1);
		//ATM
		List<String> str_s2=new ArrayList<String>();
		str_s2.add("南开大学第三食堂ATM机");
		str_s2.add("南开大学十二宿ATM机");
		str_s2.add("西南村交通银行");
		str_s2.add("南大北村交通银行");
		str_s_s.add(str_s2);
		//体育
		List<String> str_s3=new ArrayList<String>();
		str_s3.add("南开大学网球场");
		str_s3.add("南开大学游泳馆");
		str_s3.add("南开大学体育馆");
		str_s3.add("南开大学西区篮球场");
		str_s3.add("南开大学体育场");
		str_s_s.add(str_s3);
		//住宿
		List<String> str_s4=new ArrayList<String>();
		str_s4.add("南开大学五宿舍");
		str_s4.add("南开大学十二宿舍");
		str_s4.add("南开大学十三宿舍");
		str_s4.add("南开大学二十一宿");
		str_s_s.add(str_s4);
		//生活
		List<String> str_s5=new ArrayList<String>();
		str_s5.add("90咖啡厅（浴园附近）");
		str_s5.add("南开大学复印店");
		str_s5.add("南开大学校医院");
		str_s5.add("南开大学浴园");
		str_s5.add("南开大学新起点理发店");
		str_s5.add("南开大学收发室");
		str_s_s.add(str_s5);
		//学习
		List<String> str_s6=new ArrayList<String>();
		str_s6.add("南开大学主楼");
		str_s6.add("南开大学第二主教学楼");
		str_s6.add("敬业广场");
		str_s6.add("南开大学理科图书馆");
		str_s6.add("南开大学计算中心");
		str_s6.add("开大学外国语学院");
		str_s6.add("南开大学北村书店");
		str_s_s.add(str_s6);
		//物业
		List<String> str_s7=new ArrayList<String>();
		str_s7.add("南开大学收发室");
		str_s7.add("南开大学电教中心站");
		str_s7.add("南开大学数码小院");
		str_s7.add("南开大学保卫处");
		str_s_s.add(str_s7);
		return str_s_s;
	}
}
