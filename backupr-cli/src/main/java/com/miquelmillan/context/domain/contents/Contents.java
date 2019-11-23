package com.miquelmillan.context.domain.contents;

import java.io.BufferedInputStream;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.InputStream;
import java.util.Objects;

public class Contents {
    private InputStream inputStream;

    public Contents(String path) throws FileNotFoundException {
        this.inputStream = new BufferedInputStream(new FileInputStream(path));
    }

    public Contents(InputStream inputStream) {
        this.inputStream = inputStream;
    }

    public InputStream getInputStream() {
        return inputStream;
    }

    @Override
    public String toString() {
        final StringBuilder sb = new StringBuilder("Contents{");
        sb.append("inputStream=").append(inputStream);
        sb.append('}');
        return sb.toString();
    }
}
