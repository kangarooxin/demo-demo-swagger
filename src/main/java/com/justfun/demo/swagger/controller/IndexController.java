package com.justfun.demo.swagger.controller;

import com.justfun.demo.swagger.annotation.RequestJsonBody;
import com.justfun.demo.swagger.annotation.RequestJsonParam;
import com.justfun.demo.swagger.model.UserInfo;
import com.justfun.demo.swagger.support.swagger.annotations.ApiJsonParam;
import com.justfun.demo.swagger.util.JsonUtils;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

/**
 * @author pangxin001
 */
@RestController
public class IndexController {

//    @RequestMapping(value = "/postJsonParam", method = RequestMethod.POST)
//    public ResponseEntity<UserInfo> postJsonParam(@ApiJsonParam(require = true, dataTypeClass = UserInfo.class) String content) {
//        UserInfo userInfo = JsonUtils.parse(content, UserInfo.class);
//        return new ResponseEntity<>(userInfo, HttpStatus.OK);
//    }

    @RequestMapping(value = "/postJsonBody", method = RequestMethod.POST)
    public ResponseEntity<UserInfo> postJsonBody(@ApiJsonParam(type = "body", require = true, dataTypeClass = UserInfo.class) @RequestBody String content) {
        UserInfo userInfo = JsonUtils.parse(content, UserInfo.class);
        return new ResponseEntity<>(userInfo, HttpStatus.OK);
    }

    @RequestMapping(value = "/postJsonList", method = RequestMethod.POST)
    public ResponseEntity<List<UserInfo>> postJsonList(@ApiJsonParam(type = "body", require = true, dataTypeClass = List.class, dataTypeParametersClass = {UserInfo.class}) @RequestBody String content) {
        List<UserInfo> userInfoList = JsonUtils.parseToList(content, UserInfo.class);
        return new ResponseEntity<>(userInfoList, HttpStatus.OK);
    }

//    @RequestMapping(value = "/postJsonParamAutoParse", method = RequestMethod.POST)
//    public ResponseEntity<UserInfo> postJsonParamAutoParse(@ApiJsonParam(require = true) @RequestJsonParam UserInfo userInfo) {
//        return new ResponseEntity<>(userInfo, HttpStatus.OK);
//    }

    @RequestMapping(value = "/postJsonBodyAutoParse", method = RequestMethod.POST)
    public ResponseEntity<UserInfo> postJsonBodyAutoParse(@ApiJsonParam(type = "body", require = true) @RequestJsonBody UserInfo userInfo) {
        return new ResponseEntity<>(userInfo, HttpStatus.OK);
    }

    @RequestMapping(value = "/postJsonListAutoParse", method = RequestMethod.POST)
    public ResponseEntity<List<UserInfo>> postJsonListAutoParse(@ApiJsonParam(type = "body", require = true) @RequestJsonBody List<UserInfo> userInfoList) {
        return new ResponseEntity<>(userInfoList, HttpStatus.OK);
    }

}
