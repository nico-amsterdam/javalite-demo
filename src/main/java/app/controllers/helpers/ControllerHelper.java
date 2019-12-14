package app.controllers.helpers;

import java.util.ArrayList;
import java.util.List;
import java.util.HashMap;
import java.util.Map;

public final class ControllerHelper {

    private ControllerHelper() { };

    public final static List<String> getRowIndexes(Map<String, String> params1st, String prefix, String fieldName, boolean includeNonEmpty, boolean includeEmpty) {
        List<String> rowIndexes = new ArrayList<>();
        for (Map.Entry<String,String> entry : params1st.entrySet()) {
            String key = entry.getKey();
            String endsWith = '_' + fieldName;
            if (key.startsWith(prefix) && key.endsWith(endsWith)) {
                String idx = key.substring(prefix.length(), key.length() - endsWith.length());
                boolean isValueEmpty = entry.getValue() == null || entry.getValue().isEmpty();
                if ((includeNonEmpty && !isValueEmpty) || (includeEmpty && isValueEmpty)) {
                    rowIndexes.add(idx);
                } 
            }
        }
        return rowIndexes;
    }

    public final static Map<String, String> getParamMap(Map<String, String> params1st, String prefix, String rowIndex) {
        Map<String, String> paramMap = new HashMap<String, String>();
        String search = prefix + rowIndex + '_';
        for (Map.Entry<String, String> entry : params1st.entrySet()) {
            String key = entry.getKey();
            if (key.startsWith(search)) {
                String field = key.substring(search.length());
                paramMap.put(field, entry.getValue());
            }
        }
        return paramMap;
    }




}