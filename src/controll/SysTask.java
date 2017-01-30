package controll;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.concurrent.Delayed;
import java.util.concurrent.TimeUnit;

import util.Log;
import util.ThreadLocalDateUtil;
import util.TimeUtil;

public class SysTask implements Delayed {
	Log log = Log.getLogger();
	private long endTime;
	private String time;
	private static final long NANO_ORIGIN = System.nanoTime();

	final static long now() {
		return System.nanoTime() - NANO_ORIGIN;
	}

	public SysTask(String time) {
		String date = TimeUtil.getDateTime();
		String date2 = date + " " + time; // 将date2转成毫秒
		this.time = time;

		try {
			Date date3 = ThreadLocalDateUtil.parse(date2);
			long t1= date3.getTime();
			long t2  =System.currentTimeMillis();
			long timeout = t1-t2;


			endTime = TimeUnit.NANOSECONDS.convert(timeout,
					TimeUnit.MILLISECONDS) + System.nanoTime();
		} catch (ParseException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
			log.logger.error("SysTask->SysTask:" + e.getMessage());
		}// 毫秒
	}

	@Override
	public int compareTo(Delayed o) {
		if (o == this) // compare zero ONLY if same object
			return 0;
		SysTask jia = (SysTask) o;
		long result = endTime - jia.endTime;
		if (result < 0)
			return -1;
		else if (result > 0)
			return 1;
		else
			return 0;

	}

	@Override
	public long getDelay(TimeUnit unit) {
		// TODO Auto-generated method stub
		return unit.convert(endTime - System.nanoTime(), unit.NANOSECONDS);
		// return endTime-System.currentTimeMillis();
	}

}
