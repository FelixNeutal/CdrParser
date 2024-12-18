/**
 * Name: AcmeParser
 * Author: Felix Neutal
 * Description: 
 */
package parser;

import DTO.AcmeCdr;
import DTO.Cdr;
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
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class AcmeParser extends Parser<AcmeCdr> {
    Logger logger = LoggerFactory.getLogger("acmeParser");


    public AcmeParser(Properties properties, AcmeCdr c) {
        super(properties, c);
        super.setLogger(logger);
    }

    protected List<AcmeCdr> parseFile(String filepath) {
        List<AcmeCdr> cdrList = new ArrayList<>();
        AcmeCdr cdr;
        int parsedLineCount = 0;
        try (BufferedReader reader = new BufferedReader(new FileReader(filepath))) {
            String line;
            while ((line = reader.readLine()) != null) {
                if (!line.trim().isEmpty() && line.startsWith("2")) {
                    totalLines++;
                    parsedLineCount++;
                    cdr = new AcmeCdr();
                    //System.out.println(tempCdr);
                    cdrList.add(parseLine(line, cdr));
                }
            }
        } catch (IOException e) {
            logger.error(e.toString());
            e.printStackTrace();
        }
        logger.info(filepath + " " + parsedLineCount + " lines parsed");
        return cdrList;
    }

    @Override
    public AcmeCdr parseLine(String line, AcmeCdr cdr) {
        Schema schema = getSchema();
        SchemaItem schemaItem;
        String methodName = "";
        Method method = null;
        String[] splittedLine = line.split(",", -1); //Trailing empty strings are therefore not included in the resulting array. Use -1 to combat that
        String error = "";
        try {
            while (schema.hasNext()) {
                schemaItem = (SchemaItem) schema.next();
                methodName = "set" + StringUtils.capitalize(schemaItem.getKey());

                if (containsMethod(methodName)) {
                    method = getMethod(methodName);
                    Object[] arg = {splittedLine[schemaItem.getValue() - 1].replaceAll("\"", "")};
                    method.invoke(cdr, arg);
                }
            }
        } catch (InvocationTargetException e) {
            logger.error(e.toString());
            e.printStackTrace();
        } catch (IllegalAccessException e) {
            logger.error(e.toString());
            throw new RuntimeException(e);
        }
        return cleanNumbers(cdr);
    }

    public AcmeCdr cleanNumbers(AcmeCdr cdr) {
        cdr.setCaller_number(cleanNumber(cdr.getCaller_number()));
        cdr.setCalled_number(cleanNumber(cdr.getCalled_number()));
        cdr.setP_asserted_id(cleanNumber(cdr.getP_asserted_id()));
        cdr.setEgress_routing_number(cleanNumber(cdr.getEgress_routing_number()));
        cdr.setRouting_number(cleanNumber(cdr.getRouting_number()));
        cdr.setSip_diversion(cleanNumber(cdr.getSip_diversion()));
        return cdr;
    }

    private String cleanNumber(String originalNumber) {
        if (originalNumber == null || originalNumber.isEmpty()) {
            return "";
        }
        String cleanedNumber = "";
        Pattern pattern = Pattern.compile("sip:(.*?)@");
        Matcher matcher = pattern.matcher(originalNumber);
        if (matcher.find()) {
            cleanedNumber = matcher.group(1);
        }
	if (cleanedNumber.length() > 30) {
            cleanedNumber = "TooLong";
        }
        return cleanedNumber;
    }
}
