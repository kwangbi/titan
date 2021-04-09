package com.greece.titan.common.util;

import com.greece.titan.common.CommonConstants;
import eu.bitwalker.useragentutils.UserAgent;
import org.springframework.http.HttpHeaders;
import org.springframework.web.util.WebUtils;

import javax.servlet.http.HttpServletRequest;

public class UserAgentUtil {
    private static boolean saveUserAgentInSession = false;

    private static boolean isSettingNotYet = true;

    public static void setSaveUserAgentInSession() {
        if (isSettingNotYet) {
            UserAgentUtil.saveUserAgentInSession = true;
            markSettingEnd();
        }
    }

    public static void markSettingEnd() {
        isSettingNotYet = false;
    }

    /**
     * UserAgent 객체 리턴
     *
     * @param request HttpServletRequest
     * @return UserAgent
     */
    public static UserAgent getUserAgent(HttpServletRequest request) {
        if (request == null) {
            return null;
        }
        UserAgent ua = null;
        String userAgentString = request.getHeader(HttpHeaders.USER_AGENT);
        // session 에 등록 된 UserAgent 정보가 존재하는 지 확인
        if (saveUserAgentInSession) {
            if ((ua = (UserAgent) WebUtils.getSessionAttribute(request,
                    CommonConstants.SESSION_KEY_USERAGENT)) == null) {
                // 존재하지 않을 경우 Header를 분석 한 후 세션에 저장
                if (userAgentString != null) {
                    ua = UserAgent.parseUserAgentString(userAgentString);
                    request.getSession(true).setAttribute(CommonConstants.SESSION_KEY_USERAGENT, ua);
                }
            }
        } else {
            ua = UserAgent.parseUserAgentString(userAgentString);
        }
        if (ua == null) {
            ua = new UserAgent("");
        }
        return ua;
    }

}