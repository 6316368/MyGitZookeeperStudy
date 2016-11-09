package com.zookeeper.crud;

import org.apache.zookeeper.*;

import java.io.IOException;
import java.util.List;

/**
 * Created by Administrator on 2016/11/3.
 * @function  zookeeper的实列化，基本的增删改查的操作
 */
public class SimpleZookeeper {
    //连接zookeeper的服务器的地址和端口
    private static  final String connectString="192.168.199.135:2181";
    //会话超时时间,设置为于默认时间一致
    private static  final int  SESSION_TIMEOUT=30000;
    //创建zookeeper的实列
    ZooKeeper  zk;
    Watcher wh = new Watcher() {
        public void process(WatchedEvent event)
        {
            System.out.println(event.getType()+"----"+event.getPath());
            try {
                zk.getChildren("/",true);
            } catch (KeeperException e) {
                e.printStackTrace();
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
        }
    };
    //初始化Zookeeper的实列
    private   void createZkInstance()throws IOException{
        zk = new ZooKeeper(this.connectString, SimpleZookeeper.SESSION_TIMEOUT, this.wh);
    }
    /**
     *创建一个zookeeper的节点
     */
    private   void  createZkNode() throws InterruptedException, KeeperException {
        System.out.println("/n1. 创建 ZooKeeper 节点 (znode ： zoo2, 数据：" +
                " myData2 ，权限： OPEN_ACL_UNSAFE ，节点类型： Persistent");
        String path=zk.create("/zoo2/zoo2child", "myData2".getBytes(), ZooDefs.Ids.OPEN_ACL_UNSAFE, CreateMode.PERSISTENT);
        System.out.println("path============="+path);
    }
    /**
     *判断某个数据节点是否创建成功
     */
    private  void  isCreateZkNodeSuccess()throws  InterruptedException,KeeperException{
        System.out.println("/n2. 查看是否创建成功： ");
        System.out.println(new String(zk.getData("/zoo2", false, null)));
    }
    /**
     *修改某一个Zk的数据节点
     */
    private  void updateZkNode()throws  InterruptedException,KeeperException{
        System.out.println("/n3. 修改节点数据 ");
        zk.setData("/zoo2", "shenlan211314".getBytes(), -1);

    }
    /**
     *判断修改某个zk数据节点是否成功
     */
    private  void  isUpdateZkNodeSuccess()throws  InterruptedException,KeeperException{
        System.out.println("/n4. 查看是否修改成功： ");
        System.out.println(new String(zk.getData("/zoo2", false, null)));

    }
    /**
     *删除zk的某个数据节点
     */
    private  void  deleteZkNode()throws  InterruptedException,KeeperException{
        System.out.println("/n5. 删除节点 ");
        zk.delete("/zoo2", -1);
    }
    /**
     *删除zk的某个数据节点
     */
    private  void  isDeleteZkNodeSuccess()throws  InterruptedException,KeeperException{
        System.out.println("/n6. 查看节点是否被删除： ");
        System.out.println(" 节点状态： [" + zk.exists("/zoo2", false) + "]");
    }
    /**
     *删除zk父节点下面所有的子节点的值
     */
    private  void listZkChilderNode() throws KeeperException, InterruptedException {
        List<String> children = zk.getChildren("/", true);
        for (String  child:children){
            System.out.println("child========="+child);
        }
        Thread.sleep(Long.MAX_VALUE);
    }
    /**
     *关闭zk的连接
     */
    private void ZKClose() throws InterruptedException
    {
        zk.close();
    }
    public static void main(String[] args) throws IOException, InterruptedException ,KeeperException{
        SimpleZookeeper dm = new SimpleZookeeper();
        dm.createZkInstance();
       // dm.createZkNode();
      dm.listZkChilderNode();
        dm.ZKClose();
    }
}
