package util;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.OutputStream;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.HashMap;
import java.util.List;

import jxl.Workbook;
import jxl.write.Label;
import jxl.write.WritableSheet;
import jxl.write.WritableWorkbook;
import jxl.write.WriteException;
import jxl.write.biff.RowsExceededException;

public class WriteExcel {
	static Log log = Log.getLogger();

	public static String createFileName() {
		SimpleDateFormat df = new SimpleDateFormat("yyyyMMddHHmm");// 设置日期格式
		String date = df.format(new Date());// new Date()为获取当前系统时间
		String filename = date + ".xls";
		return filename;// 确认流的输出文件
	}

	
	public static String createSummaryFileName(String dix) {
		SimpleDateFormat df = new SimpleDateFormat("yyyyMMddHHmm");// 设置日期格式
		String date = df.format(new Date());// new Date()为获取当前系统时间
		String filename = date + "summary-" +dix+ ".xls";
		return filename;// 确认流的输出文件
	}
	public static void writeExcelFile2(String filePath, String[] columnNames,
			String[][] cellData) {
		File f = new File(filePath);
		if (f.exists()) {
			System.out.print("文件存在");
		} else {
			System.out.print("文件不存在");
			try {
				f.createNewFile();
			} catch (IOException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}// 不存在则创建
		}
		OutputStream os = null;
		try {
			os = new FileOutputStream(f);
		} catch (FileNotFoundException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		// 创建工作薄
		WritableWorkbook workbook = null;
		try {
			workbook = Workbook.createWorkbook(os);
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		// 创建新的一页
		WritableSheet sheet = workbook.createSheet("First Sheet", 0);
		// 创建要显示的内容,创建一个单元格，第一个参数为列坐标，第二个参数为行坐标，第三个参数为内容
		try {

			for (int i = 0; i < columnNames.length; i++) {
				Label r = new Label(i, 0, columnNames[i]);
				sheet.addCell(r);
			}

			for (int i = 0; i < cellData.length; i++) {
				for (int j = 0; j < cellData[i].length; j++) {
					Label ri = new Label(j ,i+1,
							String.valueOf(cellData[i][j]));
					sheet.addCell(ri);

				}
			}
		} catch (WriteException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}

		// 把创建的内容写入到输出流中，并关闭输出流

		try {
			workbook.write();
			workbook.close();
			os.close();
		} catch (WriteException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}

	public static void writeExcelFile(String filePath, List<StockFund> list,boolean isjz) {
		File f = new File(filePath);
		if (f.exists()) {
			System.out.print("文件存在");
		} else {
			System.out.print("文件不存在");
			try {
				f.createNewFile();
			} catch (IOException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}// 不存在则创建
		}
		OutputStream os = null;
		try {
			os = new FileOutputStream(f);
		} catch (FileNotFoundException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		// 创建工作薄
		WritableWorkbook workbook = null;
		try {
			workbook = Workbook.createWorkbook(os);
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		// 创建新的一页
		WritableSheet sheet = workbook.createSheet("First Sheet", 0);
		// 创建要显示的内容,创建一个单元格，第一个参数为列坐标，第二个参数为行坐标，第三个参数为内容
		try {
			Label r1 = new Label(0, 0, "基金代码");
			sheet.addCell(r1);

			Label r2 = new Label(1, 0, "A份");
			sheet.addCell(r2);
			Label r3 = new Label(2, 0, "B份");
			sheet.addCell(r3);
			Label r5 = new Label(3, 0, "C份");
			sheet.addCell(r5);
			Label r4 = new Label(4, 0, "A当前时间");
			sheet.addCell(r4);
			Label r6 = new Label(5, 0, "A市值");
			sheet.addCell(r6);
			Label r41 = new Label(6, 0, "B当前时间");
			sheet.addCell(r41);
			Label r7 = new Label(7, 0, "B市值");
			sheet.addCell(r7);
			Label r42 = new Label(8, 0, "C当前时间");
			sheet.addCell(r42);
			if(isjz)
			{
			Label r8 = new Label(9, 0, "净值");
			sheet.addCell(r8);
			}
			else
			{
				Label r8 = new Label(9, 0, "网络值");
				sheet.addCell(r8);
			}
			Label r9 = new Label(10, 0, "溢价");
			sheet.addCell(r9);
			Label r10 = new Label(11, 0, "场内费用(0.05%)");
			sheet.addCell(r10);

			Label r11 = new Label(12, 0, "场外申购（1.2%）");
			sheet.addCell(r11);
			Label r12 = new Label(13, 0, "场外赎回(0.5%)");
			sheet.addCell(r12);
			Label r13 = new Label(14, 0, "拆/合");
			sheet.addCell(r13);
			Label r14 = new Label(15, 0, "收益");
			sheet.addCell(r14);

			/*
			 * Label r14 = new Label(15, 0, "IF"); sheet.addCell(r14); Label r15
			 * = new Label(16, 0, "IC"); sheet.addCell(r15); Label r16 = new
			 * Label(17, 0, "IH"); sheet.addCell(r16);
			 */

			for (int i = 0; i < list.size(); i++) {
				String code = "ww";
				// 从第一行开始
				try {
					Fund mfund = list.get(i).motherfund;
					if (mfund != null) {
						Label r20 = new Label(0, i + 1, mfund.getCode());
						sheet.addCell(r20);
					} else
						continue;
				} catch (Exception e) {
					e.printStackTrace();
					log.logger.error("WriteExcel->writeExcelFile " + code);
				}
				Label r201 = new Label(1, i + 1, list.get(i).afund.getNum()
						+ "");
				sheet.addCell(r201);
				Label r202 = new Label(2, i + 1, list.get(i).bfund.getNum()
						+ "");
				sheet.addCell(r202);
				Label r203 = new Label(3, i + 1,
						list.get(i).motherfund.getNum() + "");
				sheet.addCell(r203);
				Label r21 = new Label(4, i + 1,
						list.get(i).afund.getCreateTime());
				sheet.addCell(r21);
				Label r211 = new Label(5, i + 1,
						list.get(i).afund.getNetEquity() + "");
				sheet.addCell(r211);
				Label r22 = new Label(6, i + 1,
						list.get(i).bfund.getCreateTime());
				sheet.addCell(r22);
				Label r221 = new Label(7, i + 1,
						list.get(i).bfund.getNetEquity() + "");
				sheet.addCell(r221);
				if (list.get(i).motherfund.getCreateTime() != null) {
					Label r23 = new Label(8, i + 1, list
							.get(i).motherfund.getCreateTime());
					sheet.addCell(r23);
				}

				Label r231 = new Label(9, i + 1,
						list.get(i).motherfund.getNetEquity() + "");
				sheet.addCell(r231);

				double yijia = list.get(i).motherfund.getNetEquity()
						* list.get(i).motherfund.getNum()
						- list.get(i).afund.getNetEquity()
						* list.get(i).afund.getNum()
						- list.get(i).bfund.getNetEquity()
						* list.get(i).bfund.getNum();
				double changneifee = (list.get(i).afund.getNetEquity()
						* list.get(i).afund.getNum() + list.get(i).bfund
						.getNetEquity() * list.get(i).bfund.getNum()) * 0.0005;
				double changwaishenfee = list.get(i).motherfund.getNetEquity()
						* list.get(i).motherfund.getNum() * 0.012;
				double changwaishuhuifee = list.get(i).motherfund
						.getNetEquity()
						* list.get(i).motherfund.getNum()
						* 0.005;

				Label r24 = new Label(10, i + 1, String.valueOf(yijia + ""));
				sheet.addCell(r24);

				Label r241 = new Label(11, i + 1, String.valueOf(changneifee
						+ ""));
				sheet.addCell(r241);

				Label r242 = new Label(12, i + 1,
						String.valueOf(changwaishenfee));
				sheet.addCell(r242);

				Label r243 = new Label(13, i + 1,
						String.valueOf(changwaishuhuifee));
				sheet.addCell(r243);

				double shouyi = 0;
				if (yijia > 0) {
					shouyi = yijia - changneifee - changwaishenfee;
					if (shouyi > 0) {
						Label r244 = new Label(15, i + 1,
								String.valueOf(shouyi));
						sheet.addCell(r244);
					}
				} else {
					shouyi = 0 - yijia - changneifee - changwaishuhuifee;
					if (shouyi > 0) {
						Label r244 = new Label(15, i + 1,
								String.valueOf(shouyi));
						sheet.addCell(r244);
					}
				}
			}
		} catch (WriteException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}

		// 把创建的内容写入到输出流中，并关闭输出流

		try {
			workbook.write();
			workbook.close();
			os.close();
		} catch (WriteException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}

	}

	private static void createExcel(OutputStream os) throws WriteException,
			IOException {

	}
}
