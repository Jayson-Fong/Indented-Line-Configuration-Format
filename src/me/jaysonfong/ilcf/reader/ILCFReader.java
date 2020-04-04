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

import java.io.File;
import java.io.FileNotFoundException;
import java.util.HashMap;
import java.util.Scanner;
import me.jaysonfong.ilcf.exception.IndentationException;

/**
 * Indented Line Configuration Format Reader
 * @author Jayson Fong <fong.jayson@gmail.com>
 */
public class ILCFReader {    
    private final File file;
    private final ILCFProcessor processor = new ILCFProcessor();
        
    /**
     * Uses an existing File object.
     * @param file An Object of type File
     */
    public ILCFReader(File file) {
        this.file = file;
    }
    
    /**
     * Creates a new Object of type File using a given file path.
     * @param filePath Path to the configuration file readable by the user.
     */
    public ILCFReader(String filePath) {
        file = new File(filePath);
    }
    
    /**
     * Reads the file for parsing.
     * @throws FileNotFoundException
     * @throws IndentationException
     * @see ILCFProcessor
     */
    public void read() throws FileNotFoundException, IndentationException {
        try (Scanner fileInput = new Scanner(file)) {
            while (fileInput.hasNextLine())
                processor.processLine(fileInput.nextLine());
        }
    }
    
    public ILCFProcessor getProcessor() {
        return processor;
    }
    
    /**
     * Returns a HashMap of the variables parsed.
     * @return A HashMap containing the variables parsed.
     */
    public HashMap<String, Object> getVariables() {
        return processor.getVariables();
    }
    
    /**
     * Returns the value at the given identifier.
     * @param indentifier A reference to the data.
     * @return The value retrieved using the given identifier.
     */
    public Object get(String indentifier) {
        return processor.getVariables().get(indentifier);
    }
    
    /**
     * Returns the value at the given identifier after conversion to a string.
     * @param identifier A reference to the data.
     * @return The value retrieved converted to a String from given identifier.
     */
    public String getString(String identifier) {
        return get(identifier).toString();
    }
    
    /**
     * Returns the value at the given identifier after conversion to an integer.
     * @param identifier A reference to the data.
     * @return The value retrieved converted to a Integer from given identifier.
     */
    public Integer getInteger(String identifier) {
        return Integer.valueOf(getString(identifier));
    }
    
    /**
     * Returns the value at the given identifier after conversion to a Double.
     * @param identifier A reference to the data.
     * @return The value retrieved converted to a Double from given identifier.
     */
    public Double getDouble(String identifier) {
        return Double.valueOf(getString(identifier));
    }
    
    /**
     * Returns the value at the given identifier after conversion to a Long.
     * @param identifier A reference to the data.
     * @return The value retrieved converted to a Long from given identifier.
     */
    public Long getLong(String identifier) {
        return Long.valueOf(getString(identifier));
    }
    
    /**
     * Returns the value at the given identifier after conversion to a Float.
     * @param identifier A reference to the data.
     * @return The value retrieved converted to a Float from given identifier.
     */
    public Float getFloat(String identifier) {
        return Float.valueOf(getString(identifier));
    }
    
    /**
     * Returns the value at the given identifier after conversion to a Boolean.
     * @param identifier A reference to the data.
     * @return The value retrieved converted to a Boolean from given identifier.
     */
    public Boolean getBoolean(String identifier) {
        return Boolean.valueOf(getString(identifier));
    }
    
    /**
     * Returns the value at the given identifier after conversion to a Character.
     * @param identifier A reference to the data.
     * @return The value retrieved converted to a Character from given identifier.
     */
    public Character getCharacter(String identifier) {
        return getString(identifier).charAt(0);
    }
}
