package org.jeecg.modules.message.websocket;

import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import org.jeecg.common.api.vo.Result;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.alibaba.fastjson.JSONObject;

@Api(tags = "消息推送")
@RestController
@RequestMapping("kunze/webSocketApi")
public class TestController {

    @Autowired
    private WebSocket webSocket;

    @ApiOperation("群发消息")
    @PostMapping("/sendAll")
    public Result<String> sendAll(@RequestBody JSONObject jsonObject) {
        Result<String> result = new Result<String>();
        String message = jsonObject.getString("message");
        JSONObject obj = new JSONObject();
        obj.put("cmd", "topic");
        obj.put("msgId", "M0001");
        obj.put("msgTxt", message);
        webSocket.sendAllMessage(obj.toJSONString());
        result.setResult("群发！");
        return result;
    }

    @ApiOperation("单发消息")
    @PostMapping("/sendUser")
    public Result<String> sendUser(@RequestBody JSONObject jsonObject) {
        Result<String> result = new Result<String>();
        String userId = jsonObject.getString("userId");
        String message = jsonObject.getString("message");
        JSONObject obj = new JSONObject();
        obj.put("cmd", "user");
        obj.put("userId", userId);
        obj.put("msgId", "M0001");
        obj.put("msgTxt", message);
        webSocket.sendOneMessage(userId, obj.toJSONString());
        result.setResult("单发");
        return result;
    }

}