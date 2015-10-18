# MyLibrary

##引用方法
###Add Gradle dependency:
```
dependencies {
    compile 'starbuilder.github.io:spiderlibrary:1.5';
}

```


##1.資料庫
###使用annotation來同時建立物件和資料庫Table。
###簡單物件範例
```
import andy.spiderlibrary.db.Column;
import andy.spiderlibrary.db.Table;
//table 名稱 cuteanimal
@Table(tableName = "cuteanimal")
public class CuteAnimal {
     //欄位 1 欄位名稱 name,型態 text, 索引 0
     @Column(name = "name" , type = "text",index = 0)
     String name;
     //欄位 2 欄位名稱 sex ,型態 text, 索引 1
     @Column(name = "sex" , type = "text",index = 1)
     String sex;
}
```

###取得tableName 

```
import andy.spiderlibrary.db.Table;

 public  String getTableName(){
        return CuteAnimal.class.getAnnotation(Table.class).tableName();
 }
```
 
##2.Custom Log 

###顯示比較詳細Log紀錄並儲存成Log檔案
###在APP開啟時先執行初始化

```
import andy.spiderlibrary.utils.Log;
  /**
   * Log 記錄初始化
   * @param logName  log的儲存檔案名稱
   * @param logFilePath logFilePath/log的儲存檔案路徑
   * @param tag log顯示tag
   */
  Log.createLogger("amimal","AnimalAdoption","amimalLog");
```
###寫入Log
```
 Log.d("test debug");
 Log.e("test error");
 Log.i("test info"); 
```

