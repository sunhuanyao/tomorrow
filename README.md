# tomorrow
a local cache for indexes named tomorrow；

该项目致力于本地索引缓存的快速实现，方便解决资源瓶颈上的问题。

avl树的用法示例
```$xslt
public class test {
   public static List<Integer> t1 = Arrays.asList(5, 2, 8, 3, 1, 7, 9, 4, 6);
   public static void main(String[] args){
       AvlTreeFactory<Integer> avlTreeFactory = new AvlTreeFactory<Integer>() {
           @Override
           public int cmp(Integer v1, Integer v2) {
               return v1 - v2;
           }
       };
       for(int i = 0 ; i < t1.size(); ++ i){

           avlTreeFactory.add(t1.get(i));

       }
       avlTreeFactory.delete(1);
       System.out.println(avlTreeFactory.toString());
   }
}
```

B数的用法示例
```$xslt
BTreeFactory<Integer> bTreeFactory = new BTreeFactory<Integer>(3) {
    @Override
    public int cmp(Integer v1, Integer v2) {
        return v1 - v2;
    }
};
Integer[] a = {1, 3, 5, 7, 9, 11, 13, 15, 17};

for(int i = 0 ; i < a.length; ++ i){
    bTreeFactory.add(a[i]);
}
System.out.println(bTreeFactory);
```

# R树空间索引的实现

1.该R树主要针对点在二维矩形群中的一个快速查找。

# 加入线程工具

用法如下：

```$xslt

public class test {
    public void task1(){
        System.out.println("1");
    }
    public void task2(){
        System.out.println("2");
    }
    public void task3(){
        System.out.println("3");
    }
    public static void main(String[] args){
        ExecutorLocalService executorLocalService = new ExecutorLocalService(3);
        
        executorLocalService.doInvoke(test.class, "task");

    }
```

