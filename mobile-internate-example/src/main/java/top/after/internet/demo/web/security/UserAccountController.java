package top.after.internet.demo.web.security;

import javax.validation.Valid;
import javax.validation.constraints.NotBlank;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import io.swagger.annotations.ApiParam;
import top.after.internet.security.core.user.UserAccountServiceFacade;
import top.after.internet.security.sms.verify.VerificationCodeService;

@Valid
@RestController
@Api
public class UserAccountController {
    private static final String SMS_CODE_TYPE = "pw_reset";
    @Autowired
    private UserAccountServiceFacade userAccountServiceFacade;
    @Autowired
    private VerificationCodeService verificationCodeService;

    /**
     * 重置密码
     */
    @ApiOperation("重置密码")
    @PostMapping("/user/pw_reset")
    public void pwReset(@ApiParam(value = "验证码", required = true)
                        @NotBlank(message = "验证码不能为空")
                        @RequestParam("smsCode") String smsCode,
                        @ApiParam(value = "手机号", required = true)
                        @NotBlank(message = "手机号不能为空")
                        @RequestParam("phone") String phone,
                        @ApiParam(value = "series", required = true)
                        @NotBlank(message = "series不能为空")
                        @RequestParam("series") String series,
                        @ApiParam(value = "密码", required = true)
                        @NotBlank(message = "密码不能为空")
                        @RequestParam("newPassword") String newPassword){
        String saved = verificationCodeService.getVerificationCode(series,SMS_CODE_TYPE,phone);
        if(!smsCode.equals(saved)) {
            throw new RuntimeException("验证码错误");
        }
        verificationCodeService.disable(series);
        userAccountServiceFacade.resetPassword(phone,newPassword);
    }


}