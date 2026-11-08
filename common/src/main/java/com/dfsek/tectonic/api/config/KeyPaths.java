package com.dfsek.tectonic.api.config;

import java.util.ArrayList;
import java.util.List;


/**
 * Helpers for dotted key paths, such as {@code a.b.c}.
 *
 * <p>Segments are separated by {@code .}. Inside a segment, {@code \.} is a literal dot and {@code \\} is a literal
 * backslash. Any other backslash sequence is invalid.
 */
public final class KeyPaths {
    private KeyPaths() {

    }

    /**
     * Escape a single path segment, so that it is read back as one segment by {@link #split(String)}.
     *
     * @param segment Raw segment.
     *
     * @return Segment with every {@code \} turned into {@code \\} and every {@code .} turned into {@code \.}.
     */
    public static String escape(String segment) {
        StringBuilder builder = new StringBuilder(segment.length() + 4);
        for(int i = 0; i < segment.length(); i++) {
            char c = segment.charAt(i);
            if(c == '.' || c == '\\') builder.append('\\');
            builder.append(c);
        }
        return builder.toString();
    }

    /**
     * Split a path into its unescaped segments. Trailing empty segments are dropped, as {@link String#split(String)}
     * does, so paths without escapes split exactly as they did before escapes existed.
     *
     * @param path Path to split.
     *
     * @return Unescaped segments.
     *
     * @throws IllegalArgumentException If the path contains an invalid escape sequence.
     */
    public static List<String> split(String path) {
        List<String> segments = new ArrayList<>();
        StringBuilder current = new StringBuilder();
        for(int i = 0; i < path.length(); i++) {
            char c = path.charAt(i);
            if(c == '\\') {
                if(i + 1 >= path.length()) {
                    throw new IllegalArgumentException("Invalid escape at end of key path \"" + path + "\"");
                }
                char next = path.charAt(++i);
                if(next != '.' && next != '\\') {
                    throw new IllegalArgumentException("Invalid escape \"\\" + next + "\" in key path \"" + path + "\"");
                }
                current.append(next);
            } else if(c == '.') {
                segments.add(current.toString());
                current.setLength(0);
            } else {
                current.append(c);
            }
        }
        segments.add(current.toString());
        int end = segments.size();
        while(end > 1 && segments.get(end - 1).isEmpty()) end--;
        if(end == 1 && segments.get(0).isEmpty() && !path.isEmpty()) end = 0;
        return segments.subList(0, end);
    }
}
