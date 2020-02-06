# 该项目致力于本地索引缓存的快速实现，方便解决资源瓶颈上的问题。
--

## avl树的用法示例
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

## B树的用法示例
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

## 基于B+树的空间索引实现

该R树主要针对点在二维矩形群中的一个快速查找。具体用法已被封装成ApplicationRegionFactory
类。

## 线程

该工具面向需求：并发运行内部函数。

用法如下：

```

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

## 堆用法-定长堆

步骤描述：实现BaseEntity的子类即可。然后调用Heap。样例如下：

```
public class Apple extends BaseEntity {
    int val;
    String name;
    Apple(int val, String name){
        this.val = val;
        this.name = name;
    }

    public int compareTo(BaseEntity tmp){
        return this.val - ((Apple)tmp).val;
    }
}

Heap<Apple> heap = new Heap<>(9, true);
//添加节点
heap.add(t.new Apple(1, "1"));
heap.add(t.new Apple(2, "2"));
heap.add(t.new Apple(4, "4"));
heap.add(t.new Apple(6, "6"));
heap.add(t.new Apple(7, "7"));
heap.add(t.new Apple(8, "8"));
heap.add(t.new Apple(3, "3"));
heap.add(t.new Apple(5, "5"));
System.out.println(heap.toString());
System.out.println(heap.pop().val);
System.out.println(heap.pop().val);
```

## 空间索引工厂类

需求描述：给定经纬度点，找到相应的地理位置。
该方法有对应的Springboot的Starter用法。
详见：https://github.com/sunhuanyao/tomorrow-core-starter，
可快速实现于springboot项目。内部有默认的经纬度全国信息。

用法如下：
```
ApplicationRegionFactory applicationRegionFactory = new ApplicationRegionFactory("tomorrow_region.xml");
List<RegionInfo> regionInfos = applicationRegionFactory.getRegionInfo(new Point(116.318319,40.603389));
for(RegionInfo regionInfo: regionInfos){
    System.out.println(regionInfo.getName());
}
```