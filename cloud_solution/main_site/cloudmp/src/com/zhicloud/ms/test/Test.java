/**
 * Project Name:ms
 * File Name:Test.java
 * Package Name:com.zhicloud.ms.test
 * Date:Mar 22, 20155:59:04 AM
 * 
 *
 */

package com.zhicloud.ms.test;
 
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method; 
import java.util.Collections;
import java.util.Comparator;
import java.util.List;

import com.zhicloud.ms.balancer.BalancerHelper;

import net.sf.json.JSONObject;

/**
 * ClassName: Test 
 * date: Mar 22, 2015 5:59:04 AM 
 *
 * @author sean
 * @version 
 * @since JDK 1.7
 */
public class Test {

	/**
	 * main:(这里用一句话描述这个方法的作用).  
	 *
	 * @author sean
	 * @param args void
	 * @since JDK 1.7
	 */
	public static void main(String[] args) {
//      ArrayList<Student> list= new ArrayList<Student>();
//      list.add(new Student(1,"张三",5));
//      list.add(new Student(2,"李四",4));
//      list.add(new Student(4,"小明",2));
//      list.add(new Student(5,"小黑",1));
//      list.add(new Student(3,"王五",3));
//
//      ListSort<Student> listSort= new ListSort<Student>();
//      listSort.Sort(list, "getAge", "desc");
//      for(Student s:list){
//          System.out.println(s.getId()+s.getName()+s.getAge());
//      }
		int timeOutTimes = 0;
		while(true){
			JSONObject obj = new JSONObject();
			obj.put("ip", "172.18.21.90");
//			obj.put("ip", "172.16.2.111");
			obj.put("port", "50000");
			obj.put("session_id", "");
			try {
				JSONObject j = BalancerHelper.queryBalancerSummary(obj);
				if(j.get("status").equals("success")){
					System.out.println(j.get("message"));
				}else{
					System.out.println(j.get("message"));
				}
				
			} catch (Exception e) {
				//判断连接超时次数，每次超时后间隔一分钟再发请求，连续5次超时则放弃操作。
				if(timeOutTimes<=4){
					timeOutTimes++;
					System.out.println("连接超时，次数【"+timeOutTimes+"】");
					try {
						Thread.sleep(1000*5);
					} catch (InterruptedException e1) {
						e1.printStackTrace();
					}
				}else{
					System.out.println("连接失败");
					break;
				}
			}
		}
	}

}

class Student {
    public Student(int id, String name, int age) {
        super();
        this.id = id;
        this.name = name;
        this.age = age;
    }

    private int id;
    private String name;
    private int age;

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public int getAge() {
        return age;
    }

    public void setAge(int age) {
        this.age = age;
    }
}

class ListSort<E> {
    /**
     *
     * @param list 要排序的集合
     * @param method 要排序的实体的属性所对应的get方法
     * @param sort desc 为正序
     */
    public void Sort(List<E> list, final String method, final String sort) {
        // 用内部类实现排序
        Collections.sort(list, new Comparator<E>() {

            public int compare(E a, E b) {
                int ret = 0;
                try {
                    // 获取m1的方法名
                    Method m1 = a.getClass().getMethod(method, null);
                    // 获取m2的方法名
                    Method m2 = b.getClass().getMethod(method, null);

                    if (sort != null && "desc".equals(sort)) {

                        ret = m2.invoke(((E) b), null).toString()
                            .compareTo(m1.invoke(((E) a), null).toString());

                    } else {
                        // 正序排序
                        ret = m1.invoke(((E) a), null).toString()
                            .compareTo(m2.invoke(((E) b), null).toString());
                    }
                } catch (NoSuchMethodException ne) {
                    System.out.println(ne);
                } catch (IllegalArgumentException e) {
                    // TODO Auto-generated catch block
                    e.printStackTrace();
                } catch (IllegalAccessException e) {
                    // TODO Auto-generated catch block
                    e.printStackTrace();
                } catch (InvocationTargetException e) {
                    // TODO Auto-generated catch block
                    e.printStackTrace();
                }
                return ret;
            }
        });
    }
}



