package com.zookeeper.distributed.client;

import org.apache.zookeeper.KeeperException;
import org.apache.zookeeper.WatchedEvent;
import org.apache.zookeeper.Watcher;
import org.apache.zookeeper.ZooKeeper;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

/**
 * Created by Administrator on 2016/11/3.
 * 分布式应用系统服务器上下线动态感知应用客户端的情况程序代码
 */
public class DistributedClient {
    //连接zookeeper的服务器的地址和端口
    private static  final String connectString="192.168.199.135:2181";
    //会话超时时间,设置为于默认时间一致
    private static  final int  SESSION_TIMEOUT=30000;
    //创建zookeeper的实列
    private ZooKeeper zk=null;
    //服务器节点
    private static final String  parentNode="/servers";
    //得到服务器的列表信息,公业务系统的调用
    private  volatile  List<String>   serverList;
    /**
     *链接到zk的server服务器
     */
    public  void  getConectZkserver() throws IOException {
      zk=new ZooKeeper(this.connectString, SESSION_TIMEOUT, new Watcher() {
          public void process(WatchedEvent event) {
              try {
                  getServerList();
              } catch (KeeperException e) {
                  e.printStackTrace();
              } catch (InterruptedException e) {
                  e.printStackTrace();
              }
              System.out.println(event.getType()+"----"+event.getPath());
          }
      });
    }
    /**
     * 得到服务器列表的信息
     * @throws KeeperException
     * @throws InterruptedException
     */
    public  void getServerList() throws KeeperException, InterruptedException {
        List<String> children = zk.getChildren(parentNode, true);
        List<String> servers=new ArrayList<String>();
        for (String  child:children){
            byte[] data = zk.getData(parentNode + "/server", false, null);
            servers.add(new String(data));
            System.out.println("child========="+child);
        }
        //把servers赋给成员变量serverList,已提供给各业务系统调用
        serverList=servers;
        //打印服务器列表
        System.out.println(serverList.toString());
    }

    /**
     *业务功能
     **/
    public void  handleBussiness( ) throws InterruptedException {
        System.out.println("client statr  working");
        Thread.sleep(Long.MAX_VALUE);
    }
    public static void main(String[] args) throws IOException, KeeperException, InterruptedException {
        //获取zk链接
        DistributedClient  client=new DistributedClient();
        client.getConectZkserver();
       //获取serevr的字节点信息,从中获取服务器的信息
        client.getServerList();

        //业务线程启动
        client.handleBussiness();


    }

}
