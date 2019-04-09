package example.tools;

import com.alibaba.fastjson.JSON;

/**
 * @author Created by rrz on 2018/6/20.
 * 用于 ajax 请求的响应工具类
 * @version V2.0
 */

public class ModelResult implements java.io.Serializable {

    private static final long serialVersionUID = 1L;

    private static final String DEFAULT_ERROR_CODE = "100";

    private static final String DEFAULT_ERROR_MSG = "执行失败";

    private static final String UN_LOGIN_ERROR_CODE = "401";

    private static final String UN_LOGIN_ERROR_MSG = "请先登录";

    private static final String UN_OPEN_ERROR_CODE = "402";

    private static final String UN_OPEN_ERROR_MSG = "请先实名认证";

    private static final String UN_PAY_PWD_ERROR_CODE = "403";

    private static final String UN_PAY_PWD__ERROR_MSG = "请先设置支付密码";

    private Boolean success = true;

    private String code = "200";

    private String msg = "执行成功";

    private Object result;

    public Boolean getSuccess() {
        return success;
    }

    public void setSuccess(Boolean success) {
        this.success = success;
    }

    public String getCode() {
        return code;
    }

    public void setCode(String code) {
        this.code = code;
    }

    public String getMsg() {
        return msg;
    }

    public void setMsg(String msg) {
        this.msg = msg;
    }

    public Object getResult() {
        return result;
    }

    public void setResult(Object result) {
        this.result = result;
    }

    public ModelResult() {

    }

    public ModelResult(Object result) {
        this.result = result;
    }

    public ModelResult(String msg) {
        this.msg = msg;
    }

    public ModelResult(String msg, Object result) {
        this.msg = msg;
        this.result = result;
    }

    public ModelResult(Boolean success, String code, String msg, Object result) {
        this.success = success;
        this.code = code;
        this.msg = msg;
        this.result = result;
    }

    /**
     * 接收正确结果
     *
     * @param result
     * @return
     */
    public static ModelResult newSuccess(Object result) {
        return new ModelResult(result);
    }

    /**
     * 接收正确结果
     *
     * @param msg
     * @return
     */
    public static ModelResult newSuccess(String msg) {
        return new ModelResult(msg);
    }

    /**
     * 接收成功消息 + 正确结果
     *
     * @param msg
     * @param result
     * @return
     */
    public static ModelResult newSuccess(String msg, Object result) {
        return new ModelResult(msg, result);
    }

    /**
     * 接收错误信息，使用默认的错误code
     *
     * @param msg
     * @return
     */
    public static ModelResult newError(String msg) {
        ModelResult modelResult = new ModelResult();
        modelResult.setSuccess(false);
        modelResult.setCode(DEFAULT_ERROR_CODE);
        modelResult.setMsg(msg);
        modelResult.setResult(null);
        return modelResult;
    }

    /**
     * 接收错误结果，使用默认的错误code 和 消息
     *
     * @param result
     * @return
     */
    public static ModelResult newError(Object result) {
        ModelResult modelResult = new ModelResult();
        modelResult.setSuccess(false);
        modelResult.setCode(DEFAULT_ERROR_CODE);
        modelResult.setMsg(DEFAULT_ERROR_MSG);
        modelResult.setResult(result);
        return modelResult;
    }

    /**
     * 用户未开户错误
     *
     * @return
     */
    public static ModelResult unOpenAccount() {
        ModelResult modelResult = new ModelResult();
        modelResult.setSuccess(false);
        modelResult.setCode(UN_OPEN_ERROR_CODE);
        modelResult.setMsg(UN_OPEN_ERROR_MSG);
        modelResult.setResult(null);
        return modelResult;
    }

    /**
     * 用户未登陆错误
     *
     * @return
     */
    public static ModelResult unLoginError() {
        ModelResult modelResult = new ModelResult();
        modelResult.setSuccess(false);
        modelResult.setCode(UN_LOGIN_ERROR_CODE);
        modelResult.setMsg(UN_LOGIN_ERROR_MSG);
        modelResult.setResult(null);
        return modelResult;
    }

    public static ModelResult unPayPwdError() {
        ModelResult modelResult = new ModelResult();
        modelResult.setSuccess(false);
        modelResult.setCode(UN_PAY_PWD_ERROR_CODE);
        modelResult.setMsg(UN_PAY_PWD__ERROR_MSG);
        modelResult.setResult(null);
        return modelResult;
    }

    /**
     * 接收错误code + 错误结果，使用默认的错误消息
     *
     * @param code
     * @param result
     * @return
     */
    public static ModelResult newError(String code, Object result) {
        ModelResult modelResult = new ModelResult();
        modelResult.setSuccess(false);
        modelResult.setCode(code);
        modelResult.setMsg(DEFAULT_ERROR_MSG);
        modelResult.setResult(result);
        return modelResult;
    }

    /**
     * 接收错误code + 错误消息 + 错误结果
     *
     * @param code
     * @param msg
     * @param result
     * @return
     */
    public static ModelResult newError(String code, String msg, Object result) {
        ModelResult modelResult = new ModelResult();
        modelResult.setSuccess(false);
        modelResult.setCode(code);
        modelResult.setMsg(msg);
        modelResult.setResult(result);
        return modelResult;
    }

    @Override
    public String toString() {
        return "ModelResult{" +
                "success=" + success +
                ", code='" + code + '\'' +
                ", msg='" + msg + '\'' +
                ", result=" + JSON.toJSONString(result, true) +
                '}';
    }
}
