package kvz.zsu.control;

import org.apache.poi.openxml4j.exceptions.InvalidFormatException;
import org.apache.poi.ss.usermodel.Sheet;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;
import org.junit.jupiter.api.Test;
import org.springframework.boot.test.context.SpringBootTest;

import java.io.File;
import java.io.IOException;
import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.Date;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

@SpringBootTest
class ControlApplicationTests {

    @Test
    void contextLoads() throws IOException, InvalidFormatException, ParseException {
        XSSFWorkbook workbook = new XSSFWorkbook(new File("C:\\Users\\Taras\\OneDrive\\Рабочий стол\\Додаток2.xlsx"));
        Sheet sheet = workbook.getSheetAt(0);
        String text = sheet.getRow(7).getCell(2).getStringCellValue();

        String date = "04.04.2021";
        String regex = "\\d{2}\\.\\d{2}\\.\\d{4}";

        Pattern pattern = Pattern.compile(regex);
        Matcher matcher = pattern.matcher(text);

        if (matcher.find()){
            System.out.println(matcher.group());
        }

        DateTimeFormatter dateFormat = DateTimeFormatter.ofPattern("dd.MM.yyyy");

        LocalDate localDate = LocalDate.parse(date, dateFormat);
        System.out.println(localDate.format(dateFormat));


    }

}
