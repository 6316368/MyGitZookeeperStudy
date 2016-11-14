package com.zookeeper.distributed.server;

import org.apache.zookeeper.*;

import java.io.IOException;

/**
 * Created by Administrator on 2016/11/3.
 * 分布式应用系统服务器上下线动态感知应用服务器端的情况程序代码
 */
public class DistributedServer {
    //连接zookeeper的服务器的地址和端口
    private static  final String connectString="192.168.199.135:2181";
    //会话超时时间,设置为于默认时间一致
    private static  final int  SESSION_TIMEOUT=30000;
    //创建zookeeper的实列
    private ZooKeeper zk=null;
    //服务器节点
    private static final String  parentNode="/servers";
    /**
     *链接到zk的server服务器
     */
    public  void  getConectZkserver() throws IOException {
        zk = new ZooKeeper(this.connectString, SESSION_TIMEOUT, new Watcher() {
            public void process(WatchedEvent event) {
                System.out.println(event.getType()+"----"+event.getPath());
                try {
                    zk.getChildren("/",true);
                } catch (KeeperException e) {
                    e.printStackTrace();
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }
            }
        });
    }
    /**
     *注册服务器
     **/
    public void  registerServer(String  HostName) throws KeeperException, InterruptedException {
        String s = new String(zk.getData(parentNode + "/server", false, null));
        if(s==null||""==s){
            zk.create(parentNode+"/server",HostName.getBytes(), ZooDefs.Ids.OPEN_ACL_UNSAFE,CreateMode.EPHEMERAL_SEQUENTIAL);
        }
        System.out.println(HostName+"is  online");

    }
    /**
     *业务功能
     **/
    public void  handleBussiness(String HostName) throws InterruptedException {
        System.out.println(HostName+"statr  working");
        Thread.sleep(Long.MAX_VALUE);
    }
    public static void main(String[] args) throws IOException, KeeperException, InterruptedException {
        DistributedServer   server=new DistributedServer();
        //获取Z看的链接
        server.getConectZkserver();
        //利用Zk链接注册服务器信息
        server.registerServer(args[0]);
        //启动业务服务
        server.handleBussiness(args[0]);

    }

}
