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
