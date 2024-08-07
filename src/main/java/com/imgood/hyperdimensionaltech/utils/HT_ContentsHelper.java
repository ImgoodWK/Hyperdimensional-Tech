package com.imgood.hyperdimensionaltech.utils;

import java.util.ArrayList;
import java.util.List;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

/**
 * @program: Hyperdimensional-Tech
 * @description: 一些类型校验或内容处理方法
 * @author: Imgood
 * @create: 2024-08-08 09:03
 **/
public class HT_ContentsHelper {
    /**
     * 检查字符串是否是有效的 6 位十六进制颜色代码
     *
     * @param color 字符串表示的颜色代码
     * @return 如果字符串是有效的十六进制颜色代码，则返回 true，否则返回 false
     */
    public static boolean isValidHexColor(String color) {
        return color != null && color.matches("^[0-9A-Fa-f]{6}$");
    }

    public static boolean isValidInteger(String text) {
        try {
            Integer.parseInt(text);
            return true;
        } catch (NumberFormatException e) {
            return false;
        }
    }

    public static boolean isValidDouble(String text) {
        try {
            Double.parseDouble(text);
            return true;
        } catch (NumberFormatException e) {
            return false;
        }
    }

    public static List<String> wrapText(String text, int lineLength) {
        List<String> lines = new ArrayList<>();
        int length = text.length();
        int startIndex = 0;

        while (startIndex < length) {
            int endIndex = Math.min(startIndex + lineLength, length);

            // 如果不是最后一行，且下一个字符不是空格，则向前查找最后一个空格
            if (endIndex < length && text.charAt(endIndex) != ' ') {
                while (endIndex > startIndex && text.charAt(endIndex - 1) != ' ') {
                    endIndex--;
                }
                // 如果没有找到空格，则强制在lineLength处截断
                if (endIndex == startIndex) {
                    endIndex = Math.min(startIndex + lineLength, length);
                }
            }

            // 添加当前行到结果列表
            lines.add(text.substring(startIndex, endIndex).trim());
            startIndex = endIndex;
        }

        return lines;
    }

    public static List<String> wrapText(List<String> texts, int lineLength) {
        List<String> wrappedTexts = new ArrayList<>();

        for (String text : texts) {
            List<String> lines = wrapTextHelper(text, lineLength);
            wrappedTexts.addAll(lines);
        }

        return wrappedTexts;
    }

    public static List<String> wrapTextHelper(String text, int maxWidth) {
        List<String> wrappedLines = new ArrayList<>();
        int startIndex = 0;

        while (startIndex < text.length()) {
            int endIndex = Math.min(startIndex + maxWidth, text.length());

            // 寻找最后一个空格的位置
            int tempEndIndex = endIndex;
            while (tempEndIndex > startIndex && text.charAt(tempEndIndex - 1) != ' ') {
                tempEndIndex--;
            }

            // 检查是否找到了空格或者已经到达了字符串的起始位置
            if (tempEndIndex <= startIndex) {
                // 如果没有找到空格，那么设置 tempEndIndex 为 startIndex
                // 这意味着当前子串中没有空格，直接添加整个子串
                wrappedLines.add(text.substring(startIndex, endIndex));
                startIndex = endIndex;
            } else {
                // 如果找到了空格，那么设置 tempEndIndex 为找到空格的位置 + 1
                // 这意味着当前子串以空格结束
                wrappedLines.add(text.substring(startIndex, tempEndIndex));
                startIndex = tempEndIndex;
            }
        }

        return wrappedLines;
    }

    // 图片扩展名的正则表达式
    private static final String IMAGE_EXTENSIONS_REGEX = "\\.(jpg|jpeg|png|gif|bmp|webp)$";

    // URL 的正则表达式
    private static final String URL_REGEX =
        "^(https?|ftp)://[^\s/$.?#].[^\s]*" + IMAGE_EXTENSIONS_REGEX + "$";

    private static final Pattern URL_PATTERN = Pattern.compile(URL_REGEX, Pattern.CASE_INSENSITIVE);

    /**
     * 判断一个字符串是否是下载图片的链接
     * @param url 要判断的字符串
     * @return 如果是下载图片的链接返回true，否则返回false
     */
    public static boolean isImageURL(String url) {
        if (url == null || url.isEmpty()) {
            return false;
        }
        Matcher matcher = URL_PATTERN.matcher(url);
        return matcher.matches();
    }

}
