package canal;

import com.alibaba.otter.canal.client.CanalConnector;
import com.alibaba.otter.canal.client.CanalConnectors;
import com.alibaba.otter.canal.protocol.CanalEntry.Entry;
import com.alibaba.otter.canal.protocol.CanalEntry.EntryType;
import com.alibaba.otter.canal.protocol.CanalEntry.RowChange;
import com.alibaba.otter.canal.protocol.Message;

import java.net.InetSocketAddress;
import java.util.List;

public class CanalClient1 {

    public static void main(String[] args) {
        new Thread(() -> initConnector("example")).start();
//        new Thread(() -> initConnector("example2")).start();
    }

    private static void initConnector(String destination) {
        CanalConnector connector = CanalConnectors.newSingleConnector(new InetSocketAddress("node01", 11111), destination, "root", "12345678");
        try {
            connector.connect();
            connector.subscribe(".*\\..*");
            connector.rollback();
            while (true) {
                Message message = connector.getWithoutAck(1000);
                if (message.getId() != -1 && message.getEntries().size() > 0) {
                    printEntry(message.getEntries());
                    System.out.println("=======");
                }
                connector.ack(message.getId());
            }
        } finally {
            connector.disconnect();
        }
    }

    private static void printEntry(List<Entry> entries) {
        for (Entry entry : entries) {
            if (entry.getEntryType() == EntryType.TRANSACTIONBEGIN || entry.getEntryType() == EntryType.TRANSACTIONEND) {
                continue;
            }
            try {
                RowChange rowChange = RowChange.parseFrom(entry.getStoreValue());
                System.out.println(rowChange.getSql());
            } catch (Exception e) {
                throw new RuntimeException("ERROR ## parser error, data:" + entry.toString(), e);
            }
        }
    }

}