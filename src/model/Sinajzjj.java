package model;

import java.util.List;

public class Sinajzjj {
	class SinaData {
		private String fbrq;
		private String jjjz;
		private String ljjz;

		public void setFbrq(String fbrq) {
			this.fbrq = fbrq;
		}

		public String getFbrq() {
			return this.fbrq;
		}

		public void setJjjz(String jjjz) {
			this.jjjz = jjjz;
		}

		public String getJjjz() {
			return this.jjjz;
		}

		public void setLjjz(String ljjz) {
			this.ljjz = ljjz;
		}

		public String getLjjz() {
			return this.ljjz;
		}

	}

	class Status {
		private int code;

		public void setCode(int code) {
			this.code = code;
		}

		public int getCode() {
			return this.code;
		}

		class DataList {
			private List<SinaData> data;

			private String total_num;

			public void setData(List<SinaData> data) {
				this.data = data;
			}

			public List<SinaData> getData() {
				return this.data;
			}

			public void setTotal_num(String total_num) {
				this.total_num = total_num;
			}

			public String getTotal_num() {
				return this.total_num;
			}

		}

		class Result {
			private Status status;

			private DataList data;

			public void setStatus(Status status) {
				this.status = status;
			}

			public Status getStatus() {
				return this.status;
			}

			public void setData(DataList data) {
				this.data = data;
			}

			public DataList getData() {
				return this.data;
			}

		}

		private Result result;

		public void setResult(Result result) {
			this.result = result;
		}

		public Result getResult() {
			return this.result;
		}

	}
}
