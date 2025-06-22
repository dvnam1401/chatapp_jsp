package com.chatapp.chatapp;

public class SimpleJsonParser {
    
    public static String getValue(String json, String key) {
        try {
            String searchPattern = "\"" + key + "\":";
            int startIndex = json.indexOf(searchPattern);
            
            if (startIndex == -1) {
                return null;
            }
            
            startIndex += searchPattern.length();
            
            // Skip whitespace
            while (startIndex < json.length() && Character.isWhitespace(json.charAt(startIndex))) {
                startIndex++;
            }
            
            if (startIndex >= json.length()) {
                return null;
            }
            
            // Check if value is string (starts with quote)
            if (json.charAt(startIndex) == '"') {
                startIndex++; // Skip opening quote
                int endIndex = startIndex;
                
                // Find ending quote, handle escaped quotes
                while (endIndex < json.length()) {
                    if (json.charAt(endIndex) == '"' && (endIndex == startIndex || json.charAt(endIndex - 1) != '\\')) {
                        break;
                    }
                    endIndex++;
                }
                
                if (endIndex >= json.length()) {
                    return null;
                }
                
                String value = json.substring(startIndex, endIndex);
                // Unescape the string
                return value.replace("\\\"", "\"").replace("\\\\", "\\").replace("\\n", "\n").replace("\\r", "\r");
            } else {
                // Number value
                int endIndex = startIndex;
                while (endIndex < json.length() && 
                       (Character.isDigit(json.charAt(endIndex)) || json.charAt(endIndex) == '.')) {
                    endIndex++;
                }
                return json.substring(startIndex, endIndex);
            }
        } catch (Exception e) {
            System.err.println("Error parsing JSON value for key: " + key + ", " + e.getMessage());
            return null;
        }
    }
    
    public static int getIntValue(String json, String key) {
        String value = getValue(json, key);
        if (value != null) {
            try {
                return Integer.parseInt(value.trim());
            } catch (NumberFormatException e) {
                System.err.println("Error parsing int value for key: " + key + ", " + e.getMessage());
            }
        }
        return 0;
    }
} 