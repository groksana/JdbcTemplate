package com.gromoks.jdbctemplate.util;

import java.util.LinkedList;
import java.util.List;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class NamedQueryParser {
    private static final Pattern PATTERN = Pattern.compile(":(\\w+)");

    public String getSubstituteNamedParameterSql(String originalSql) {
        String substitutedSql = originalSql;
        List<String> parameterNameList = getOrderedNamedParameters(originalSql);
        for (String parameterName : parameterNameList) {
            String namedParameter = ":" + parameterName;
            substitutedSql = substitutedSql.replace(namedParameter, "?");
        }
        return substitutedSql;
    }

    public List<String> getOrderedNamedParameters(String originalSql) {
        Matcher matcher = PATTERN.matcher(originalSql);
        List<String> parameterNameList = new LinkedList<>();

        while (matcher.find()) {
            parameterNameList.add(matcher.group(1));
        }
        return parameterNameList;
    }
}
