package schemas;

import java.io.BufferedReader;
import java.io.FileReader;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

public class AcmeSchema implements Schema{
    private List<SchemaItem> items;
    private int currentPos = 0;
    private String filepath;

    public AcmeSchema(String filepath) {
        this.filepath = filepath;
        items = new ArrayList<>();
        readData();
    }

    @Override
    public boolean hasNext() {
        return currentPos < items.size() - 1;
    }

    @Override
    public Object next() {
        SchemaItem item = items.get(currentPos++);
        return item;
    }

    private void readData() {
        try (BufferedReader reader = new BufferedReader(new FileReader(filepath))) {
            String line;
            SchemaItem item;
            while ((line = reader.readLine()) != null) {
                String[] pair = line.split(":");
                if (pair.length == 2) {
                    item = new SchemaItem();
                    item.setKey(pair[0].stripLeading().stripTrailing());
                    item.setValue(Integer.valueOf(pair[1].stripLeading().stripTrailing()));
                    items.add(item);
                } else {
                    //error message here
                }
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}
