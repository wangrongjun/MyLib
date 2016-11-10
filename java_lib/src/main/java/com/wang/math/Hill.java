package com.wang.math;

import com.wang.java_util.TextUtil;
import com.wang.math.fraction.Fraction;
import com.wang.math.matrix.Matrix;
import com.wang.math.matrix.MatrixException;
import com.wang.math.matrix.MatrixUtil;

import java.util.ArrayList;
import java.util.List;

/**
 * by 王荣俊 on 2016/10/27.
 */
public class Hill {

    private int m;//连续的明文字母，也是加密密钥矩阵的维度。
    private Matrix<Fraction> keyMatrix;
    private Matrix<Fraction> reverseKeyMatrix;

    /**
     * @param keyMatrix 加密密钥矩阵，必须行等于列，det必须为1
     */
    public Hill(Matrix<Fraction> keyMatrix) throws Exception {
        if (keyMatrix == null || keyMatrix.getRow() != keyMatrix.getColumn()) {
            throw new Exception("Error: row != column");
        }
        this.keyMatrix = keyMatrix;
        if (keyMatrix.determinant().compare(new Fraction(1, 1)) == 0) {
            calculateReverseKeyMatrix();
        } else {
            throw new Exception("Error: det != 1");
        }
        m = keyMatrix.getRow();
    }

    public String encode(String text) throws Exception {

        text = text.replace(" ", "");//先去掉空格
        if (TextUtil.isEmpty(text)) {
            throw new Exception("Error: text is null or illegal");
        }

        while (text.length() % m != 0) {//如果分组时不能分成m个一组，补充字母x直到可以分组
            text += "x";
        }

        StringBuilder builder = new StringBuilder();
        for (int i = 0; i < text.length() / m; i++) {
            String textUnit = "";
            for (int j = 0; j < m; j++) {
                textUnit += text.charAt(i * m + j);
            }
            String s = encodeOrDecodeUnit(textUnit, true);
            builder.append(s);
        }
        return builder.toString();
    }

    public String decode(String text) throws Exception {
        text = text.replace(" ", "");//先去掉空格
        if (TextUtil.isEmpty(text) || text.length() % m != 0) {
            throw new Exception("Error: text is null or length%m != 0");
        }
        StringBuilder builder = new StringBuilder();
        for (int i = 0; i < text.length() / m; i++) {
            String textUnit = "";
            for (int j = 0; j < m; j++) {
                textUnit += text.charAt(i * m + j);
            }
            String s = encodeOrDecodeUnit(textUnit, false);
            System.out.println(textUnit + "  " + s);
            builder.append(s);
        }
        return builder.toString();
    }

    /**
     * 对长度为m的单位字符串进行加密/解密
     */
    public String encodeOrDecodeUnit(String unitText, boolean isEncode) throws Exception {

        if (unitText == null || unitText.length() != m) {
            throw new Exception("Error: length != m");
        }

        //如果unitText="pay" 那么textMatrix=(15, 0, 24)T
        List<Fraction> textMatrixList = new ArrayList<>();
        for (int i = 0; i < m; i++) {
            textMatrixList.add(new Fraction(toNumber(unitText.charAt(i)) % 26, 1));
        }
        Matrix<Fraction> textMatrix = new Matrix<>(textMatrixList, m, 1, IOperation.fractionIOperation);

        //如果unitText="pay" 那么resultMatrix=(11, 13, 18)，这几个数由keyMatrix和textMatrix决定。
        Matrix<Fraction> resultMatrix =
                MatrixUtil.multiply(isEncode ? keyMatrix : reverseKeyMatrix, textMatrix);

        String s = "";
        for (int i = 0; i < m; i++) {
//            s += toLetter(resultMatrix.get(i, 0) % 26);
            s += toLetter((int) (resultMatrix.get(i, 0).toLong() % 26));
        }
        return s;
    }

    /**
     * 计算解密用的逆矩阵
     */
    private void calculateReverseKeyMatrix() throws MatrixException {

        reverseKeyMatrix = MatrixUtil.reverse(keyMatrix);
        reverseKeyMatrix.iterator(new Matrix.Iterator<Fraction>() {
            @Override
            public void next(int i, int j, int index, Fraction element) {
                long son = element.getSon();
                if (!element.isPositive()) {
                    son = -son;
                    while (son < 0) {
                        son += 26;
                    }
                } else {
                    son = son % 26;
                }
                reverseKeyMatrix.set(i, j, new Fraction(son, 1));
            }
        });
    }

    private char toLetter(int number) {
        return (char) (number + 'a');
    }

    private int toNumber(char letter) {
        return letter - 'a';
    }

    public Matrix<Fraction> getReverseKeyMatrix() {
        return reverseKeyMatrix;
    }
}
