package org.tohu.test.enums;


public enum BrowserType {
    
    /** Supported browser types */
    FIREFOX("firefoxproxy"), IEXPLORER("iexploreproxy"), SAFARI("*safari"), CHROME("*googlechrome");
    
    private BrowserType() {
        
    }

    private BrowserType(String browserName) {
        this.browserName = browserName;
    }
    
    private String browserName;
 
    public String getBrowserName() {
        return this.browserName;
    }
}
