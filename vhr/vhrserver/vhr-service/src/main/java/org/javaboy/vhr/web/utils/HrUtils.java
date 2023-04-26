package org.javaboy.vhr.web.utils;

import org.javaboy.vhr.web.model.Hr;
import org.springframework.security.core.context.SecurityContextHolder;

/**
 * Hr工具类
 */
public class HrUtils {
    public static Hr getCurrentHr() {
        return ((Hr) SecurityContextHolder.getContext().getAuthentication().getPrincipal());
    }
}
