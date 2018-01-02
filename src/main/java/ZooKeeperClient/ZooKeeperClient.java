package ZooKeeperClient;

import SystemUtils.SystemUtils;
import org.apache.log4j.Logger;
import org.apache.zookeeper.*;
import org.apache.zookeeper.data.Stat;
import sun.java2d.loops.ProcessPath;

import java.io.IOException;
import java.util.List;
import java.util.concurrent.CountDownLatch;

import static SystemUtils.SystemUtils.getPID;

/**
 * Created by yon_b on 1/1/2018.
 */
public class ZooKeeperClient {
    // declare zookeeper instance to access ZooKeeper ensemble
    private ZooKeeper zoo;
    private static Logger log = Logger.getLogger(ZooKeeperClient.class.getName());

//    static void logDebugData(String data) {
//        log.debug("[" +String.valueOf(getPID()) + "]" + data);
//    }

    // Method to connect zookeeper ensemble.
    public static ZooKeeperClient connect(String host) throws IOException,InterruptedException {
        final CountDownLatch connectedSignal = new CountDownLatch(1);
        ZooKeeperClient zClient = new ZooKeeperClient();
        zClient.zoo = new ZooKeeper(host,5000,new Watcher() {
            public void process(WatchedEvent we) {
                if (we.getState() == Event.KeeperState.SyncConnected) {
                    connectedSignal.countDown();
                }
            }
        });
        connectedSignal.await();
        return zClient;
    }
    
    public void close() throws InterruptedException {
        zoo.close();
    }

    public void createNode(String path, byte[] data, CreateMode mode) throws
            KeeperException,InterruptedException {
        zoo.create(path, data, ZooDefs.Ids.OPEN_ACL_UNSAFE,
                mode);
    }

    public void delete(String path) throws KeeperException,InterruptedException {
        zoo.delete(path,zoo.exists(path,true).getVersion());
    }

    public boolean znodeExists(String path, Stat stat) throws //TODO: understand the watch parameter
            KeeperException,InterruptedException {
        Stat _stat = zoo.exists(path, true);
        if (stat != null) stat =_stat;
        return _stat != null;
    }

    public byte[] getData(String path, Watcher watcher, Stat stat) throws KeeperException, InterruptedException {
        return zoo.getData(path, watcher, stat);
    }

    public void update(String path, byte[] data) throws //TODO: understand the watch parameter
            KeeperException,InterruptedException {
        zoo.setData(path, data, zoo.exists(path,true).getVersion());
    }

    public List<String> getChildren(String path, Watcher watcher, Stat stat) throws KeeperException, InterruptedException {
        if (znodeExists(path, null)) return zoo.getChildren(path, watcher, stat);
        return null;
    }
}