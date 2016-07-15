package org.bit.crawler.gist.http.model;

public class Capture_msg {
	int ret;
	Capture msg = new Capture();
	
	
	public Capture_msg() {
		super();
		// TODO Auto-generated constructor stub
	}
	
	

	public Capture_msg(int ret, Capture msg) {
		super();
		this.ret = ret;
		this.msg = msg;
	}



	public int getRet() {
		return ret;
	}



	public void setRet(int ret) {
		this.ret = ret;
	}



	public Capture getMsg() {
		return msg;
	}



	public void setMsg(Capture msg) {
		this.msg = msg;
	}



	@Override
	public String toString() {
		return "Capture_msg [ret=" + ret + ", msg=" + msg + "]";
	}



	public class Capture{
		String capture_result;

		public Capture() {
			super();
			// TODO Auto-generated constructor stub
		}

		public Capture(String capture_result) {
			super();
			this.capture_result = capture_result;
		}

		public String getCapture_result() {
			return capture_result;
		}

		public void setCapture_result(String capture_result) {
			this.capture_result = capture_result;
		}

		@Override
		public String toString() {
			return "Capture [capture_result=" + capture_result + "]";
		}
		
	}
}
