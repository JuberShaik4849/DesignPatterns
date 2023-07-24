package com.jsonParser.project;

import java.io.FileReader;
import java.io.IOException;
import org.json.JSONArray;
import org.json.JSONObject;
import org.json.JSONTokener;

public class JsonParsertoSqlApplication {

    public static void main(String[] args) {
        try {
            // Read JSON file
            JSONTokener tokener = new JSONTokener(new FileReader("input.json"));
            JSONObject json = new JSONObject(tokener);

            // Generate SQL query
            String sqlQuery = generateSQLQuery(json);
            System.out.println(sqlQuery);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public static String generateSQLQuery(JSONObject json) {
        JSONArray columns = json.getJSONArray("columns");
        StringBuilder conditions = new StringBuilder();

        for (int i = 0; i < columns.length(); i++) {
            JSONObject column = columns.getJSONObject(i);
            String operator = column.getString("operator").toUpperCase();
            String fieldName = column.getString("fieldName");
            String fieldValue = column.getString("fieldValue");

            String condition;
            switch (operator) {
                case "IN":
                    condition = fieldName + " IN (" + fieldValue + ")";
                    break;
                case "LIKE":
                    condition = fieldName + " LIKE '" + fieldValue + "'";
                    break;
                case "EQUAL":
                    condition = fieldName + " = '" + fieldValue + "'";
                    break;
                case "LESS_THAN_EQUAL":
                    condition = fieldName + " <= '" + fieldValue + "'";
                    break;
                case "GREATER_THAN_EQUAL":
                    condition = fieldName + " >= '" + fieldValue + "'";
                    break;
                case "NOT_EQUAL":
                    condition = fieldName + " <> '" + fieldValue + "'";
                    break;
                case "BETWEEN":
                    String[] values = fieldValue.split(",");
                    condition = fieldName + " BETWEEN '" + values[0] + "' AND '" + values[1] + "'";
                    break;
                default:
                    condition = "";
            }

            conditions.append(condition).append(" AND ");
        }

        if (conditions.length() > 0) {
            conditions.delete(conditions.length() - 5, conditions.length()); // Remove the trailing " AND "
        }

        return "SELECT * FROM table_name WHERE " + conditions.toString();
    }
}