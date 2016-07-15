package org.bit.crawler.gist.http.model;



public class ApiError extends Throwable {

    public int ret;
    public String msg;


    public ApiError() {
		super();
		// TODO Auto-generated constructor stub
	}

	public ApiError(int ret, String msg) {
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



	public String getMsg() {
		return msg;
	}



	public void setMsg(String msg) {
		this.msg = msg;
	}



	@Override
    public String toString() {
        return "ApiError{" +
                "ret=" + ret +
                ", msg='" + msg + '\'' +
                "} " + super.toString();
    }
}
