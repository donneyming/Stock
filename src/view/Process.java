package view;

import java.text.SimpleDateFormat;
import java.util.Date;

import controll.MyTask;

public class Process {
	// 主函数
	public static void main(String[] args) {
		//84321009097678428
		//82311360359233
		
		//81891656927254
		//89531841181643
		System.out.println(System.nanoTime());
		MyTask r = new MyTask();
		// r.run();//这是方法调用，而不是开启一个线程
		Thread t = new Thread(r);// 调用了Thread(Runnable
									// target)方法。且父类对象变量指向子类对象。
		t.start();
		SimpleDateFormat df = null;
		Date d = new Date();
		boolean flag1 = false, flag2 = false;
		while (flag1 || flag2) {
			df = new SimpleDateFormat("HH:mm");// 设置日期格式
			String date = df.format(new Date());// new Date()为获取当前系统时间

			if (date.equals("14:50") & flag1) {
				// df = new SimpleDateFormat("yyyy-MM-dd");
				// String runTime = df.format(d) + " 10:30:00";
				try {
					MyTask r1 = new MyTask();
					// r.run();//这是方法调用，而不是开启一个线程
					Thread t1 = new Thread(r1);// 调用了Thread(Runnable
												// target)方法。且父类对象变量指向子类对象。
					t.start();
				} catch (Exception e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}

				flag1 = false;

			} else if (date.equals("14:55") & flag2) {
				// df = new SimpleDateFormat("yyyy-MM-dd");
				// String runTime = df.format(d) + " 10:30:00";
				try {
					MyTask r2 = new MyTask();
					// r.run();//这是方法调用，而不是开启一个线程
					Thread t2 = new Thread(r2);// 调用了Thread(Runnable
												// target)方法。且父类对象变量指向子类对象。
					t2.start();
				} catch (Exception e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}

				flag2 = false;

			} else {
				try {
					Thread.sleep(3000);
					System.out.println("wait for time");
				} catch (Exception e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}

			}
		}

	}
}
