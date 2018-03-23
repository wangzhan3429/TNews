package com.wz.tnews;

import cn.bmob.v3.BmobUser;
import cn.bmob.v3.datatype.BmobFile;

public class User extends BmobUser {
  
    private BmobFile file;
  
  
    public BmobFile getFile() {
        return file;
    }  
  
    public void setFile(BmobFile file) {
        this.file = file;
    }  
  
}  