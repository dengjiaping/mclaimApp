package com.sinosoftyingda.fastclaim.common.model;
/**
 * 升级接口请求类
 * @author haoyun 20130304
 *
 */
public class UpdateVersionRequest extends BasicRequest {
			private String version;//版本号

			public String getVersion() {
				return version;
			}

			public void setVersion(String version) {
				this.version = version;
			}

			public UpdateVersionRequest() {
				super();
			}

			public UpdateVersionRequest(String version) {
				super();
				this.version = version;
			}
}
