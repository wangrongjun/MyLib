package com.wang.db2;

import java.util.ArrayList;
import java.util.List;

/**
 * by wangrongjun on 2017/3/26.
 */
public class Where {

    /**
     * 查询条件的模式（相等，不相等，小于，小于等于，大于，大于等于，模糊查询）
     */
    public enum QueryMode {
        EQUAL,
        NOT_EQUAL,
        LESS,
        LESS_EQUAL,
        MORE,
        MORE_EQUAL,
        LIKE,
        NOT_LIKE
    }

    /**
     * 当前查询条件与下一个查询条件的逻辑（and,or）
     * 注意：若查询条件Equation为最后一个，则不存在下一个查询条件，也就没有所谓的逻辑，忽略该变量
     */
    public enum QueryLogic {
        AND,
        OR,
    }

    private List<Expression> expressionList;
    private InputValueFormatter formatter;

    public Where() {
        expressionList = new ArrayList<>();
        formatter = new InputValueFormatter.InputValueFormatterImpl();
    }

    public int size() {
        return expressionList.size();
    }

    public Where add(String name, String value, QueryMode mode, QueryLogic logic) {
        Expression expression = new Expression(name, value, mode, logic);
        expressionList.add(expression);
        return this;
    }

    public Where add(String name, String value, QueryMode mode) {
        Expression expression = new Expression(name, value, mode, QueryLogic.AND);
        expressionList.add(expression);
        return this;
    }

    public Where add(String name, String value, QueryLogic logic) {
        Expression expression = new Expression(name, value, QueryMode.EQUAL, logic);
        expressionList.add(expression);
        return this;
    }

    public Where add(String name, String value) {
        Expression expression = new Expression(name, value, QueryMode.EQUAL, QueryLogic.AND);
        expressionList.add(expression);
        return this;
    }

    public static Where build(String whereName, String whereValue) {
        return new Where().add(whereName, whereValue);
    }

    /**
     * @return username='wang' and password='123' or gender='1' or nickname like '%abc%'
     */
    @Override
    public String toString() {
        if (size() == 0) {
            return null;
        }
        String sql = "";
        for (int i = 0; i < size(); i++) {
            Expression expression = expressionList.get(i);
            // 1.字段名字
            sql += expression.name;
            // 2.查询模式
            switch (expression.queryMode) {
                case EQUAL:
                    sql += "=";
                    break;
                case NOT_EQUAL:
                    sql += "!=";
                    break;
                case LESS:
                    sql += "<";
                    break;
                case LESS_EQUAL:
                    sql += "<=";
                    break;
                case MORE:
                    sql += ">";
                    break;
                case MORE_EQUAL:
                    sql += ">=";
                    break;
                case LIKE:
                    sql += " like ";
                    break;
                case NOT_LIKE:
                    sql += " not like ";
                    break;
            }
            // 3.赋值
            sql += "'" + expression.value + "'";
            // 4.如果不是最后一个查询条件，添加查询逻辑
            if (i < size() - 1) {
                switch (expression.queryLogic) {
                    case AND:
                        sql += " and ";
                        break;
                    case OR:
                        sql += " or ";
                        break;
                }
            }
        }
        return sql;
    }

    private class Expression {
        public String name;
        public String value;
        public QueryMode queryMode;
        public QueryLogic queryLogic;

        Expression(String name, String value, QueryMode queryMode,
                   QueryLogic queryLogic) {
            this.name = name;
            this.value = formatter.format(value);
            this.queryMode = queryMode;
            this.queryLogic = queryLogic;
        }
    }

}
