package org.javaboy.vhr.web.controller;

import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import org.javaboy.vhr.web.config.JwtTokenUtil;
import org.javaboy.vhr.common.api.VerificationCode;
import org.javaboy.vhr.common.api.RespBean;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;
import java.awt.image.BufferedImage;
import java.io.IOException;

/**
 * 登录处理
 */
@Api(tags = "登录注册管理")
@RestController
public class LoginController {
    @Value("${jwt.tokenHeader}")
    private String tokenHeader;
    @Value("${jwt.tokenHead}")
    private String tokenHead;

    @Autowired
    private JwtTokenUtil jwtTokenUtil;

    @GetMapping("/login")
    public RespBean login() {
        return RespBean.error("尚未登录，请登录!");
    }

    /**\
     * 这里面是将验证码放入到页面中
     * @param request
     * @param resp
     * @throws IOException
     */
    @GetMapping("/verifyCode")
    public void verifyCode(HttpServletRequest request, HttpServletResponse resp) throws IOException {
        VerificationCode code = new VerificationCode();
        BufferedImage image = code.getImage();
        String text = code.getText();
        HttpSession session = request.getSession(true);
        session.setAttribute("verify_code", text);
        VerificationCode.output(image,resp.getOutputStream());
    }

    /**
     * 这个是将token每次请求进行更新
     * @param request
     * @return
     */
    @ApiOperation(value = "刷新token")
    @GetMapping("/refreshToken")
    public RespBean refreshToken(HttpServletRequest request) {
        String token = request.getHeader(tokenHeader);
        String refreshToken = jwtTokenUtil.refreshToken(token);
        if (refreshToken == null) {
            return RespBean.error("token已经过期！");
        }
        refreshToken = tokenHead+refreshToken;
        RespBean ok = RespBean.ok("token成功刷新", refreshToken);
        return ok;
    }
}
