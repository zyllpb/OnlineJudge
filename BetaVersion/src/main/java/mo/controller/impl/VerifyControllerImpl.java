package mo.controller.impl;

import mo.controller.VerifyController;
import mo.utils.RandomValidateCodeUtil;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;
import java.util.Map;

@Controller
public class VerifyControllerImpl implements VerifyController {//}, ErrorController {

    private static final Logger logger = LoggerFactory.getLogger(VerifyControllerImpl.class);

    @Override
    @RequestMapping(value = "/getVerify", method = RequestMethod.GET)
    public void getVerify(HttpServletRequest request, HttpServletResponse response) {
        try {
            response.setContentType("image/jpeg");
            response.setHeader("Pragma", "No-cashe");
            response.setHeader("Cashe-Control", "no-cashe");
            response.setDateHeader("Expire", 0);
            logger.info("更新验证码");
            RandomValidateCodeUtil randomValidateCodeUtil = new RandomValidateCodeUtil();
            randomValidateCodeUtil.getRandcode(request, response);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    @Override
    @ResponseBody
    @RequestMapping(value = "/checkVerify", method = RequestMethod.POST, headers = "Accept=application/json")
    public boolean checkVerify(@RequestBody Map<String, Object> data, HttpSession session) {
        String randomCode = (String) session.getAttribute(RandomValidateCodeUtil.RANDOMCODEKEY);
        String code = (String) data.get("code");
        if (randomCode == null) {
            session.removeAttribute(RandomValidateCodeUtil.RANDOMCODEKEY);
            return false;
        } else if (RandomValidateCodeUtil.checkVerifyCode(randomCode, code)) {
            logger.info("验证码[{}]正确[{}]", code, randomCode);
            return true;
        } else {
            session.removeAttribute(RandomValidateCodeUtil.RANDOMCODEKEY);
            return false;
        }
    }

    /*
    @Override
    @RequestMapping("/error")
    public String toError(@RequestParam(value = StringValue.error_msg, defaultValue = "") String msg, @RequestParam(value = StringValue.error_reason, defaultValue = "") String reason, ModelMap map) {
        switch (msg) {
            case StringValue.Exception_PermissionDenied: {
                logger.info("权限错误，跳转至404！");
                map.put("msg", msg);
                map.put("reason", reason);
                return "index/404";
            }
        }
        return "index/404";
    }

    @Override
    public String getErrorPath() {
        return "/error";
    }
    */
}
