package Utils;

import storage.tables.Row;

import java.util.List;

public class TableFormat {

    public static void createTable(List<Row> li)
    {
        String[] headers = {"Id", "Name", "Email"};
        int numColumns = headers.length;
        int[] colsWidth = new int[numColumns];//hardcoded for now rigid schema
        for(int i=0;i<colsWidth.length;i++)
        {
            colsWidth[i]=headers[i].length();
        }
        for(Row r1:li)
        {
            String s1 = r1.toString();
            int index=0;
            int count = 0;
            for(int i=0;i<s1.length();i++)
            {
                if(s1.charAt(i)=='[' || s1.charAt(i)==' ')
                {
                    continue;
                }
                if(s1.charAt(i)==',')
                {
                    colsWidth[index]=Math.max(colsWidth[index],count);
                    index++;
                    count=0;
                    continue;
                }
                count++;
            }
            colsWidth[index]=Math.max(colsWidth[index],count);
        }
        lineSeparator(colsWidth);
        printRow(headers, colsWidth);
        lineSeparator(colsWidth);
        for (Row row : li) {
            Object[] rawValues = row.values.toArray();
            printRow(rawValues, colsWidth);
        }
        lineSeparator(colsWidth);

    }
    public static void lineSeparator(int[] colWidths)
    {
        System.out.print("+");
        for(int i:colWidths)
        {
            for(int j=0;j<i+2;j++)
            {
                System.out.print("-");
            }
            System.out.print("+");
        }
        System.out.println();
    }

    public static void printRow(Object[] rowData,int[] colsWidths)
    {
        for(int i = 0; i < rowData.length; i++) {
            System.out.print("|");
            int val1 = colsWidths[i];
            String val = rowData[i].toString();
            System.out.print(val);
            for(int j=0;j<val1-val.length()+2;j++)
            {
                System.out.print(" ");
            }
        }
        System.out.print("|");
        System.out.println();

    }
}
