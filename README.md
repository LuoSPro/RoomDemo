# RoomDemo
这是用于学习使用Room框架而写的小Demo，边学边写


上个月学习了郭霖大神的LitePal框架，做了一个小Demo[LitePal小应用](https://www.jianshu.com/p/d1d5331e4e53)，但最近在学`Jetpack`，发现他里面有`Room`这个数据库框架，同时他结合了`LiveData`和`ViewModel`，于是我决定把之前的那个LitePal的小Demo用Room实现试一下，顺便熟悉一下`Room`的使用

#### 一、首先了解一下Room所需要的环境：
```
dependencies {
  def room_version = "2.2.2"

  implementation "androidx.room:room-runtime:$room_version"
  annotationProcessor "androidx.room:room-compiler:$room_version" // For Kotlin use kapt instead of annotationProcessor

  // optional - Kotlin Extensions and Coroutines support for Room
  implementation "androidx.room:room-ktx:$room_version"

  // optional - RxJava support for Room
  implementation "androidx.room:room-rxjava2:$room_version"

  // optional - Guava support for Room, including Optional and ListenableFuture
  implementation "androidx.room:room-guava:$room_version"

  // Test helpers
  testImplementation "androidx.room:room-testing:$room_version"
}
```
【注】这里可以根据自己所用的语言进行选择添加，里面分别是`Kotlin`、`RxJava`、`Guava`，如果用的是java，那么这三个依赖都可以不必添加
#### 二、了解搭建Room所需的类
Room由三个重要的组件组成:Database、Entity、DAO
##### （一） Entity
#### 1. `@Entity`:
这个类作为你的实体类，他是用来映射表结构的实体，在这个类中，你需要根据`@ColumnInfo(name = "xxx")`这个，来声明该字段存储在表中的**列名**，如果你声明这个，那么系统会将你的变量名默认作为你的列名
```
   @PrimaryKey(autoGenerate = true)
    private int id;
    //如果不设置ColumnInfo，系统默认你的变量名为ColumnInfo
    @ColumnInfo(name = "english_word")
    private String word;
    @ColumnInfo(name = "chinese_meaning")
    private String ChineseMeaning;
    @ColumnInfo(name = "chinese_invisible")
    private boolean chineseInvisible;

    //必须要写setter和getter，不然会报错
```
【注】每个属性都必须有自己的setter和getter，不然编译报错
**Entity中的每一个field都将被持久化到数据库，除非使用了@Ignore注解**
>@Entity注解包含的属性有：
tableName：设置表名字。默认是类的名字。
indices：设置索引。
inheritSuperIndices：父类的索引是否会自动被当前类继承。
primaryKeys：设置主键。
foreignKeys：设置外键。
#### 1.2对数据库的信息进行设置：
1. 表名设置
```
@Entity(tableName = "People")
public class People{
    
}
```
2. 设置主键：
```
@Entity(primaryKeys = {"id","identity"})//此为一个复合主键，也可以设置单个主键
public class People{

    public int id;
    public String identity;
}
```
【注】如果你希望Room给entity设置一个自增的字段，可以设置@PrimaryKey的autoGenerate属性。
3. 设置索引
数据库索引用于提高数据库表的数据访问速度的。数据库里面的索引有单列索引和组合索引。Room里面可以通过@Entity的indices属性来给表格添加索引。
```
@Entity(indices = {@Index("name"),@Index(value = {"sex", "age"})})
public class Person{
    @PrimaryKey
    public int id;
    
    @ColumnInfo(name = "name")
    public String name;

    @ColumnInfo(name = "sex")
    public String sex;

    @ColumnInfo(name = "age")
    public int age;
}
```
【注】当需要设置唯一索引时，需要通过的@Index的unique属性来设置，如`@Entity(indices = {@Index(value = {"sex", "age"},
        unique = true)})`
4. 设置外键：
目前还用不上，等以后来学
#### （二）DAO
#### 2.1  `@DAO`：
`(Data Access Objects)`，他是一个接口，用来声明操作数据的方法，如对数据库数据的增、删、改、查等操作
```
    @Insert
    void insertPerson(Person...people);

    @Delete
    void deletePerson(Person...people);

    @Update
    void updatePerson(Person...people);

    @Query("SELECT * FROM Person ORDER BY id DESC")
    LiveData<List<Person>> getAllPerson();

    @Query("SELECT * FROM Person WHERE ID =:id")
    Person queryPerson(int id);
```
每个方法前都需要声明这个操作是用来干嘛，对于查询的方法，还应该写明查询的`SQL语句`，对于条件查询，可以从方法中传递相应的参数到`SQL语句`中，格式是`: 变量`,如根据id查询数据`@Query("SELECT * FROM Person WHERE ID =:id")`
#### 2.2：Dao中的方法的注解
1. insert的注解
@Insert注解可以设置一个属性：
`onConflict：`
默认值是OnConflictStrategy.ABORT，表示当插入有冲突的时候的处理策略。OnConflictStrategy封装了Room解决冲突的相关策略：
       1. OnConflictStrategy.REPLACE：冲突策略是取代旧数据同时继续事务。
       2. OnConflictStrategy.ROLLBACK：冲突策略是回滚事务。
       3. OnConflictStrategy.ABORT：冲突策略是终止事务。
       4. OnConflictStrategy.FAIL：冲突策略是事务失败。
       5. OnConflictStrategy.IGNORE：冲突策略是忽略冲突。
2. update的注解
 @Update和@Insert一样也是可以设置onConflict来表明冲突的时候的解决办法
3. delete:
 @Delete也是可以设置onConflict来表明冲突的时候的解决办法。
4.  query查询 
 @Query注解是DAO类中使用的主要注释。它允许您对数据库执行读/写操作。@Query在编译的时候会验证准确性，所以如果查询出现问题在编译的时候就会报错。
```
@Dao
public interface PersonDao {

    //简单查询
    @Query("SELECT * FROM Person ORDER BY id DESC")
    LiveData<List<Person>> getAllPerson();

    //带参查询
    @Query("SELECT * FROM Person WHERE ID =:id")
    Person queryPerson(int id);

    //带多个参数的情况
    @Query("SELECT * FROM Person WHERE age BETWEEN :minAge AND :maxAge")
    LiveData<List<Person>> loadAllPersonBetweenAges(int minAge, int maxAge);
  
    //查询时传递一组参数
    @Query("SELECT * FROM Person WHERE name IN (:names)")
    List<Person> loadPersonFromNames(List<String> names);

    //Observable的查询
    //好处：查询到结果的时候，UI能够自动更新。Room为了实现这一效果，查询的返回值的类型为LiveData。
     @Query("SELECT * FROM Person WHERE name IN (:names)")
    LiveData<List<Person>> loadPersonFromNames(List<String> names);
}
``` 
#### （三）Database
```
@Database(entities = {Person.class},version = 2,exportSchema = true)
public abstract class PersonDatabase extends RoomDatabase {
    private static PersonDatabase INSTANCE;
    static synchronized PersonDatabase getDatabase(Context context){
        if (INSTANCE == null){
            INSTANCE = Room.databaseBuilder(context.getApplicationContext(),
                    PersonDatabase.class,"person_database")
                    .build();
        }
        return INSTANCE;
    }

    public abstract PersonDao getPersonDao();
}
```
1. Database对应的对象(RoomDatabase)必须添加@Database注解，@Database包含的属性：
`entities`：数据库相关的所有Entity实体类，他们会转化成数据库里面的表。
`version`：数据库版本。
`exportSchema`：默认true，也是建议传true，这样可以把Schema导出到一个文件夹里面。同时建议把这个文件夹上次到VCS。
2. 可以通过调用`Room.databaseBuilder()`或者`Room.inMemoryDatabaseBuilder()`获取实例,两种方式获取Database对象的区别:
`Room.databaseBuilder()`：生成Database对象，并且创建一个存在文件系统中的数据库。
`Room.inMemoryDatabaseBuilder()`：生成Database对象并且创建一个存在内存中的数据库。当应用退出的时候(应用进程关闭)数据库也消失。
3. 创建RoomDatabase实例的时候，RoomDatabase.Builder类里面主要方法的介绍：
```
    /**
     * 默认值是FrameworkSQLiteOpenHelperFactory，设置数据库的factory。
     * 比如我们想改变数据库的存储路径可以通过这个函数来实现
     */
    public RoomDatabase.Builder<T> openHelperFactory(@Nullable SupportSQLiteOpenHelper.Factory factory);

    /**
     * 设置数据库升级(迁移)的逻辑
     */
    public RoomDatabase.Builder<T> addMigrations(@NonNull Migration... migrations);

    /**
     * 设置是否允许在主线程做查询操作
     */
    public RoomDatabase.Builder<T> allowMainThreadQueries();

    /**
     * 设置数据库的日志模式
     */
    public RoomDatabase.Builder<T> setJournalMode(@NonNull JournalMode journalMode);

    /**
     * 设置迁移数据库如果发生错误，将会重新创建数据库，而不是发生崩溃
     */
    public RoomDatabase.Builder<T> fallbackToDestructiveMigration();

    /**
     * 设置从某个版本开始迁移数据库如果发生错误，将会重新创建数据库，而不是发生崩溃
     */
    public RoomDatabase.Builder<T> fallbackToDestructiveMigrationFrom(int... startVersions);
    /**
     * 监听数据库，创建和打开的操作
     */
    public RoomDatabase.Builder<T> addCallback(@NonNull RoomDatabase.Callback callback);
```
RoomDatabase除了必须添加@Database注解也可以添加@TypeConverter注解。用于提供一个把自定义类转化为一个Room能够持久化的已知类型的，比如我们想持久化日期的实例，我们可以用如下代码写一个TypeConverter去存储相等的Unix时间戳在数据库中。
```
public class Converters {

    @TypeConverter
    public static Date fromTimestamp(Long value) {
        return value == null ? null : new Date(value);
    }

    @TypeConverter
    public static Long dateToTimestamp(Date date) {
        return date == null ? null : date.getTime();
    }
}
```
#### （四）数据迁移
在数据库有变化的时候，我们任何时候都应该尽量提供Migrating。Migrating让我们可以自己去处理数据库从某个版本过渡到另一个版本的逻辑
```
@Database(entities = {Person.class},version = 2,exportSchema = false)
public abstract class PersonDatabase extends RoomDatabase {
    private static PersonDatabase INSTANCE;
    static synchronized PersonDatabase getDatabase(Context context){
        if (INSTANCE == null){
            INSTANCE = Room.databaseBuilder(context.getApplicationContext(),
                    PersonDatabase.class,"person_database")
                    .addMigrations(MIGRATION_1_2)//版本迁移，需要添加一个参数
                    .build();
        }
        return INSTANCE;
    }

    public abstract PersonDao getPersonDao();

    static final Migration MIGRATION_1_2 = new Migration(1,2) {
        @Override
        public void migrate(@NonNull SupportSQLiteDatabase database) {//偏底层，需要手写SQL语句
            database.execSQL("ALTER TABLE Person ADD COLUMN identity INTEGER NOT NULL DEFAULT 1");
        }
    };
}
```
### Room + ViewModel + Repository + AsyncTask
1. `ViewModel`
用于管理、保存界面的数据，但是不应该存在直接操作数据的方法
`jetpack`提供了`ViewModel`，也提供了一个继承自`ViewModel`的`AndroidViewModel`，两者的区别就是`AndroidViewModel`中有一个Context参数，方便得到Activity中的数据或者使用`SharedPreference`这种依赖`Context`的工具
在这个Demo中，我的`ViewModel`主要作为一个**中转站**，同时提供一个Context，方便提供给`Repository`创建数据库
2. `Repository`
用来做数据的处理，可以在数据库或者在云端的数据库取数据
这个Demo中，就是在`Repository`中对数据库进行操作(增、删、改、查)
在`Repository`中，使用LiveData用来保存数据，同时也要写相应的方法去对数据库进行处理，但这里，我使用的`AsyncTask`去对数据库进行处理，避免在消耗太多主线程的时间
>LiveData的特点：
1）采用观察者模式，数据发生改变，可以自动回调（比如更新UI）。
2）不需要手动处理生命周期，不会因为Activity的销毁重建而丢失数据。
3）不会出现内存泄漏。
4）不需要手动取消订阅，Activity在非活跃状态下（pause、stop、destroy之后）不会收到数据更新信息。

3. `AsyncTask`
使用AsyncTask是为了不在主线程中对数据进行操作，避免加载时间过程，影响主线程的执行,但其实还是调用之前在主线程中操作的那几个方法
如果要强制在主线程就行操作，需要在Database创建的时候，指明
```
.allowMainThreadQueries()
//这一句是运行对数据的操作可以在主线程中执行，当加入了人AsyncTask后，就不用使用这个方法了
```
`AsyncTask<Person,Void,Void>`
泛型里面三个内容
 第一个：实体类
 第二个：操作的进度
 第三个：操作的结果
```
    //插入
    static class InsertAsyncTask extends AsyncTask<Person,Void,Void>{
        private PersonDao mPersonDao;

        private InsertAsyncTask(PersonDao personDao) {
            mPersonDao = personDao;
        }

        //在后台上要做什么操作
        @Override
        protected Void doInBackground(Person... people) {
            mPersonDao.insertPerson(people);
            return null;
        }

        //任务完成后会呼叫
        @Override
        protected void onPostExecute(Void aVoid) {
            super.onPostExecute(aVoid);
        }

        //当进度更新的时候呼叫
        @Override
        protected void onProgressUpdate(Void... values) {
            super.onProgressUpdate(values);
        }

        //在后台任务执行之前呼叫
        @Override
        protected void onPreExecute() {
            super.onPreExecute();
        }
    }
```
这是插入操作，删、改等操作类似
【注】我的查找操作没有按照这种方式去处理——以为我不是很清楚怎么用，所以我是直接
```
LiveData<List<Person>> getAllPersonLive(){
        return allPersonLive;
    }
```
这样，将LiveData返回回去，再转化成List，对数据就行查询
下面是这个Demo的gitHub地址
