package com.wz.tnews;

/**
 * @author wangzhan
 * @version 2018-01-04
 */

class PluginBean {
    public String label;
    public String pkgName;

    public PluginBean(String label, String pkgName) {
        this.label = label;
        this.pkgName = pkgName;
    }

    public String getPkgName() {
        return pkgName;
    }

    public String getLabel() {
        return label;
    }
}
