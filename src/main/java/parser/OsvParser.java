/**
 * Name: OsvParser
 * Author: Felix Neutal
 * Description:
 */
package parser;

import DTO.Cdr;
import DTO.OsvCdr;
import org.apache.commons.lang3.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import schemas.Schema;
import schemas.SchemaItem;
import java.io.BufferedReader;
import java.io.FileReader;
import java.io.IOException;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.util.ArrayList;
import java.util.List;
import java.util.Properties;

public class OsvParser extends Parser<OsvCdr> {
    Logger logger = LoggerFactory.getLogger("osvParser");
    public OsvParser(Properties properties, OsvCdr c) {
        super(properties, c);
        super.setLogger(logger);
    }

    @Override
    protected List<OsvCdr> parseFile(String filepath) {
        List<OsvCdr> cdrList = new ArrayList<>();
        int startOffset = 9;
        //int endOffset = 19 ;
        int index = 1;
        int parsedLineCount = 0;
        OsvCdr cdr;
        try (BufferedReader reader = new BufferedReader(new FileReader(filepath))) {
            String line;
            while ((line = reader.readLine()) != null && !line.stripLeading().startsWith("CLOSE")) {
                if (index >= startOffset && !line.equals("")) {
                    cdr = new OsvCdr();
                    cdr = parseLine(line, cdr);
                    if (cdr != null) {
                        totalLines++;
                        parsedLineCount++;
                        cdrList.add(cdr);
                    }
                }
                index++;
            }
        } catch (IOException e) {
            e.printStackTrace();
            logger.error(e.getMessage());
        }
        //logger.info(filepath + " " + parsedLineCount + " lines parsed");
        return cdrList;
    }

    @Override
    public OsvCdr parseLine(String line, OsvCdr cdr) {
        Schema schema = getSchema();
        SchemaItem schemaItem;
        String methodName = "";
        Method method = null;
        String[] splittedLine = line.split(",", -1); //Trailing empty strings are therefore not included in the resulting array. Use -1 to combat that
        if (splittedLine.length > 1 && !splittedLine[1].equals("00000000")) {
            return null;
        }
        if (splittedLine.length != Integer.parseInt(properties.getProperty("CsvValuesCount"))) {
            logger.error("Broken line with length: " + splittedLine.length + " and tekst: " + line);
            return null;
        }
        try {
            while (schema.hasNext()) {
                schemaItem = (SchemaItem) schema.next();
                methodName = "set" + StringUtils.capitalize(schemaItem.getKey());
                if (containsMethod(methodName)) {
                    //System.out.println(schemaItem.getKey() + " == " +  schemaItem.getValue() + " == " +splittedLine[schemaItem.getValue() - 1]);
                    method = getMethod(methodName);
                    Object[] arg = {splittedLine[schemaItem.getValue() - 1].replaceAll("\"", "")};
                    method.invoke(cdr, arg);
                }
            }
        } catch (InvocationTargetException e) {
            logger.error(e.getMessage());
            e.printStackTrace();
        } catch (IllegalAccessException e) {
            logger.error(e.getMessage());
        }
        return cdr;
    }
}
