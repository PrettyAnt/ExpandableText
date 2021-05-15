package com.example.expandabletext.model;

/**
 * @author chenyu
 * My personal blog  https://prettyant.github.io
 * <p>
 * Created on 12:00 AM  13/05/21
 * PackageName : com.example.expandabletext.model
 * describle :
 */
public class TestData {
    private String content;
    private boolean isExpland = false;

    public String getContent() {
        return content;
    }

    public void setContent(String content) {
        this.content = content;
    }

    public boolean isExpland() {
        return isExpland;
    }

    public void setExpland(boolean expland) {
        isExpland = expland;
    }
}
