package org.bit.crawler.gist.http.model;


public class Token_msg {
	int ret;
	Token msg = new Token();
	
		public Token_msg(int ret, Token msg) {
		super();
		this.ret = ret;
		this.msg = msg;
	}

	public Token_msg() {
		super();
		// TODO Auto-generated constructor stub
	}

	public int getRet() {
		return ret;
	}

	public void setRet(int ret) {
		this.ret = ret;
	}

	public Token getMsg() {
		return msg;
	}

	public void setMsg(Token msg) {
		this.msg = msg;
	}

	@Override
	public String toString() {
		return "Token_msg [ret=" + ret + ", msg=" + msg + "]";
	}

	public class Token{
		String access_token;

		public Token(String access_token) {
			super();
			this.access_token = access_token;
		}

		public Token() {
			super();
			// TODO Auto-generated constructor stub
		}

		public String getAccess_token() {
			return access_token;
		}

		public void setAccess_token(String access_token) {
			this.access_token = access_token;
		}

		@Override
		public String toString() {
			return "Token [access_token=" + access_token + "]";
		}
		
	}	

}
