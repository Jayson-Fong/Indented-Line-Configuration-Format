/*
 * The MIT License
 *
 * Copyright 2020 Jayson Fong <fong.jayson@gmail.com>.
 *
 * Permission is hereby granted, free of charge, to any person obtaining a copy
 * of this software and associated documentation files (the "Software"), to deal
 * in the Software without restriction, including without limitation the rights
 * to use, copy, modify, merge, publish, distribute, sublicense, and/or sell
 * copies of the Software, and to permit persons to whom the Software is
 * furnished to do so, subject to the following conditions:
 *
 * The above copyright notice and this permission notice shall be included in
 * all copies or substantial portions of the Software.
 *
 * THE SOFTWARE IS PROVIDED "AS IS", WITHOUT WARRANTY OF ANY KIND, EXPRESS OR
 * IMPLIED, INCLUDING BUT NOT LIMITED TO THE WARRANTIES OF MERCHANTABILITY,
 * FITNESS FOR A PARTICULAR PURPOSE AND NONINFRINGEMENT. IN NO EVENT SHALL THE
 * AUTHORS OR COPYRIGHT HOLDERS BE LIABLE FOR ANY CLAIM, DAMAGES OR OTHER
 * LIABILITY, WHETHER IN AN ACTION OF CONTRACT, TORT OR OTHERWISE, ARISING FROM,
 * OUT OF OR IN CONNECTION WITH THE SOFTWARE OR THE USE OR OTHER DEALINGS IN
 * THE SOFTWARE.
 */
package me.jaysonfong.ilcf.reader;

import java.util.ArrayList;
import java.util.HashMap;
import me.jaysonfong.ilcf.exception.IndentationException;

/**
 * Indented Line Configuration Format line Processor
 * @author Jayson Fong <fong.jayson@gmail.com>
 */
public class ILCFProcessor {
    private final char ASSIGN_DELIM = '=';
    private final char COMMENT_DELIM = '#';
    private final char ARRLIST_CHAR = '-';
    private final char HASHMAP_CHAR = '*';
    private final String PREFIX_DELIM = "_";
    
    private final int PARTIAL_TRIM = 0x0;
    private final int FULL_TRIM = 0x1;
    
    private final HashMap<String, Object> variables = new HashMap();
    private final ArrayList<String> prefixes = new ArrayList();
    
    private boolean lineIsArrList = false;
    private boolean lineIsHashMap = false;
    private boolean isLabel = false;
    
    /**
     * ICLF Line Processor
     * Processes a line of a ILCF file.
     * If the line consists of only space characters, returns.
     * @param line A line of a ILCF file.
     * @throws me.jaysonfong.ilcf.exception.IndentationException
     */
    public void processLine(String line) throws IndentationException {
        String[] lines = trim(line);
        if (lines[FULL_TRIM].length() == 0x0) return;
        if (isListElement(lines[FULL_TRIM])) processListElement(lines);
        else processCascadeElement(lines);
        reset();
    }
    
    public HashMap<String, Object> getVariables() {
        return variables;
    }
    
    private void processListElement(String[] lines) {
        if (isEmptyListElement(lines[FULL_TRIM])) return;
        // TBC
    }
    
    private boolean isEmptyListElement(String fullTrimLine) {
        return fullTrimLine.length() < 0x2;
    }
    
    private String getContentsFromListElement(String fullTrimLine) {
        return fullTrimLine.substring(0x1);
    }
    
    private void processCascadeElement(String[] lines) throws IndentationException {
        int assignDelimIndex = getAssignDelimIndex(lines[FULL_TRIM]);
        String name = isLabel
                ? lines[FULL_TRIM]
                : getName(lines[FULL_TRIM], assignDelimIndex);
        String prefix = getPrefix(lines[PARTIAL_TRIM], name);
        if (!isLabel) {
            String value = getValue(lines[FULL_TRIM], assignDelimIndex);
            variables.put(prefix + name, value);
        }
    }
    
    private String getValue(String fullTrimLine, int assignDelimIndex) {
        int commentIndex = getCommentDelimLocation(fullTrimLine);
        if (commentIndex < 0x0) return fullTrimLine
                .substring(assignDelimIndex + 0x1).trim();
        return fullTrimLine.substring(assignDelimIndex + 0x1, commentIndex).trim();
    }

    private String getPrefix(String partialTrimLine, String name)
            throws IndentationException {
        int indentCount = getIndentCount(partialTrimLine);
        updateCascade(indentCount);
        String prefix = generatePrefix();
        prefixes.add(name);
        return prefix + (prefix.length() > 0 ? PREFIX_DELIM : "");
    }
    
    private String getPrefix(String partialTrimLine) throws IndentationException {
        int indentCount = getIndentCount(partialTrimLine);
        updateCascade(indentCount);
        String prefix = generatePrefix();
        return prefix;
    }
    
    private String generatePrefix() {
        return String.join(PREFIX_DELIM, prefixes);
    }
    
    private void updateCascade(int indentCount) throws IndentationException {
        if (indentCount > prefixes.size() + 0x1)
            throw new IndentationException("Too Many Indentations");
        while (!prefixes.isEmpty() && indentCount != prefixes.size())
            prefixes.remove(prefixes.size() - 0x1);
    }
    
    private String getName(String fullTrimLine, int assignDelimIndex) {
        String name = fullTrimLine.substring(0x0, assignDelimIndex);
        return name.trim();
    }
    
    private int getAssignDelimIndex(String fullTrimLine) {
        int assignDelimIndex = fullTrimLine.indexOf(ASSIGN_DELIM);
        if (assignDelimIndex == - 0x1) {
            assignDelimIndex = fullTrimLine.length();
            isLabel = true;
        }
        return assignDelimIndex;
    }
    
    private void reset() {
        lineIsArrList = lineIsHashMap = isLabel = false;
    }
    
    private String[] trim(String line) {
        return new String[]{
            line.replaceAll(" ++$", ""),
            line.trim()
        };
    }
    
    private boolean isListElement(String line) {
        char firstChar = line.charAt(0);
        if (firstChar == '-') return lineIsArrList = true;
        else if (firstChar == '*') return lineIsHashMap = true;
        return false;
    }
        
    private int getCommentDelimLocation(String fullTrimLine) {
        String line = fullTrimLine.replace("\\" + COMMENT_DELIM, "\\\\");
        return line.indexOf(COMMENT_DELIM);
    }
    
    private int getIndentCount(String line) {
        int indentCount = 0;
        while (line.length() > 0 && line.substring(0x0, 0x1).equals("\t")) {
            line = line.substring(0x1);
            ++indentCount;
        }
        return indentCount;
    }

}
