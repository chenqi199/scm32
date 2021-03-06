package timeutils;

import IPModel.DatabaseMessage;
import IPModel.IPMessage;
import database.DataBaseDemo;
import htmlparse.URLFecter;
import ipfilter.IPUtils;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import static java.lang.System.out;

/**
 * 描述：
 * Created by  chen_q_i@163.com on 2017/9/26 : 10:59.
 *
 * @version : 1.0
 */
public class GetIpMain {
    public static void main(String[] args) throws ClassNotFoundException {


        List<String> Urls = new ArrayList<>();
        List<DatabaseMessage> databaseMessages = new ArrayList<>();
        List<IPMessage> list = new ArrayList<>();
        List<IPMessage> ipMessages = new ArrayList<>();
        String url = "http://www.xicidaili.com/nn/1";
        String IPAddress;
        String IPPort;
        int k, j;

//        //首先使用本机ip进行爬取
//        try {
//            list = URLFecter.urlParse(url, list);
//        } catch (IOException e) {
//            e.printStackTrace();
//        } catch (ClassNotFoundException e) {
//            e.printStackTrace();
//        }
//
//        //对得到的IP进行筛选，选取链接速度前100名的
////        list = IPFilter.Filter(list);
//        try {
//            list = IPUtils.IPIsable(list);
//            System.out.println("第一页的可用ip======================="+list.size()+"个。");
//        } catch (Exception e) {
//            e.printStackTrace();
//        }

        databaseMessages = DataBaseDemo.query();

        //构造种子Url
        for (int i = 1; i <= 27; i++) {
            Urls.add("http://www.xicidaili.com/nn/" + i);
        }

        //得到所需要的数据
//        for (k = 0, j = 0; j < Urls.size() && k<list.size(); k++) {
        for ( j = 1; j < Urls.size() ;j++) {
            url = Urls.get(j);

//            IPAddress = list.get(k).getIPAddress();
//            IPPort = list.get(k).getIPPort();
            //每次爬取前的大小
            int preIPMessSize = ipMessages.size();
            try {

                ipMessages = URLFecter.urlParse(url, databaseMessages.get(j).getIPAddress(),
                        databaseMessages.get(j).getIPPort(), ipMessages);
//                继续使用本机爬去ip
//                URLFecter.urlParse(url,ipMessages);
                //每次爬取后的大小
                int lastIPMessSize = ipMessages.size();
//                if(preIPMessSize != lastIPMessSize){
//                    j++;
//                }

                //对IP进行轮寻调用
//                if (k >= list.size()) {
//                    k = 0;
//                }
            } catch (ClassNotFoundException e) {
                e.printStackTrace();
            } catch (IOException e) {
                e.printStackTrace();
            }
        }

        //对得到的IP进行筛选，选取链接速度前100名的
//        ipMessages = IPFilter.Filter(ipMessages);

        //对ip进行测试，不可用的从数组中删除

        System.out.println("size===="+ipMessages.size());
        try {
            ipMessages = IPUtils.IPIsable(ipMessages);
        } catch (Exception e) {
            e.printStackTrace();
        }

//        for(IPMessage ipMessage : ipMessages){
////            out.println(ipMessage.getIPAddress());
////            out.println(ipMessage.getIPPort());
////            out.println(ipMessage.getServerAddress());
////            out.println(ipMessage.getIPType());
////            out.println(ipMessage.getIPSpeed());
//        }

        //将得到的IP存储在数据库中(每次先清空数据库)
        try {
//            DataBaseDemo.delete();
            DataBaseDemo.add(ipMessages);
        } catch (ClassNotFoundException e) {
            e.printStackTrace();
        }

        //从数据库中将IP取到
        try {
            databaseMessages = DataBaseDemo.query();
        } catch (ClassNotFoundException e) {
            e.printStackTrace();
        }

        for (DatabaseMessage databaseMessage: databaseMessages) {
            out.println(databaseMessage.getId()+"---------"+databaseMessage.getIPAddress()
                    +"---------"+databaseMessage.getIPPort()+"---------"+databaseMessage.getServerAddress()
                    +"---------"+databaseMessage.getIPType() +"---------"+databaseMessage.getIPSpeed());
//            out.println();
        }
    }

}
