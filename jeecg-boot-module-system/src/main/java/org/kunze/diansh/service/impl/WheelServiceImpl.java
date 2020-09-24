package org.kunze.diansh.service.impl;

import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.github.pagehelper.PageInfo;
import org.apache.commons.lang.StringUtils;
import org.apache.poi.ss.formula.functions.T;
import org.jeecg.common.api.vo.Result;
import org.jeecg.common.constant.CommonConstant;
import org.jeecg.common.util.BeansUtil;
import org.jeecg.common.util.CommonUtil;
import org.jeecg.common.util.DelFileUtils;
import org.kunze.diansh.entity.Wheel;
import org.kunze.diansh.mapper.WheelMapper;
import org.kunze.diansh.pojo.domain.wheel.WheelDO;
import org.kunze.diansh.pojo.param.wheel.UpdateWheelStatusParam;
import org.kunze.diansh.pojo.param.wheel.WheelParam;
import org.kunze.diansh.pojo.request.wheel.DeleteWheelRequest;
import org.kunze.diansh.pojo.request.wheel.HomeWheelRequest;
import org.kunze.diansh.pojo.request.wheel.QueryWheelRequest;
import org.kunze.diansh.pojo.request.wheel.SaveWheelRequest;
import org.kunze.diansh.pojo.request.wheel.UpdateWheelRequest;
import org.kunze.diansh.pojo.request.wheel.UpdateWheelStatusRequest;
import org.kunze.diansh.pojo.vo.wheel.HomeWheelVO;
import org.kunze.diansh.pojo.vo.wheel.WheelVO;
import org.kunze.diansh.service.IWheelService;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

import static com.github.pagehelper.page.PageMethod.startPage;

/**
 * 轮播图管理业务层
 *
 * @author 姜伟
 * @date 2020/9/17
 */
@Service
public class WheelServiceImpl extends ServiceImpl< WheelMapper, Wheel > implements IWheelService {
    /**
     * 起始页
     */
    private static final Integer PAGE_NO = 1;

    /**
     * 首页默认展示数
     */
    private static final Integer HOME_PAGE_SIZE = 5;

    /**
     * 轮播图启动
     */
    private static final String WHEEL_START_UP = "0";

    /**
     * 轮播图未启动
     */
    private static final String WHEEL_NOT_STARTED = "1";

    /**
     * 轮播图持久层
     */
    @Autowired
    private WheelMapper wheelMapper;

    @Override
    public Result< T > saveWheel(SaveWheelRequest saveWheelRequest) {
        WheelParam wheelParam = new WheelParam();
        BeanUtils.copyProperties(saveWheelRequest, wheelParam);
        wheelParam.setUpdateName(CommonUtil.getUserName());
        wheelParam.setWheelId(UUID.randomUUID().toString().replace("-", ""));
        int count = wheelMapper.saveWheel(wheelParam);
        Result< T > result = new Result<>();
        if (count > 0) {
            result.success(CommonConstant.ADD_SUCCESSFULLY);
        }
        else {
            result.error500(CommonConstant.ADD_FAILED);
        }
        return result;
    }

    @Override
    public Result< PageInfo< HomeWheelVO > > listWheelHome(HomeWheelRequest homeWheelRequest) {
        // 如果显示页为空，那么默认显示5张图片
        if (homeWheelRequest.getPageSize() == null) {
            homeWheelRequest.setPageSize(HOME_PAGE_SIZE);
        }
        startPage(PAGE_NO, homeWheelRequest.getPageSize());
        WheelParam wheelParam = new WheelParam();
        BeanUtils.copyProperties(homeWheelRequest, wheelParam);
        // 设置轮播图启动
        wheelParam.setIsFlag(WHEEL_START_UP);
        List< WheelDO > wheelDOList = wheelMapper.listWheel(wheelParam);
        Result< PageInfo< HomeWheelVO > > pageInfoResult = new Result<>();
        pageInfoResult.setResult(new PageInfo<>(BeansUtil.listCopy(wheelDOList, HomeWheelVO.class)));
        pageInfoResult.setSuccess(true);
        return pageInfoResult;
    }

    @Override
    public Result< T > updateWheel(UpdateWheelRequest updateWheelRequest) {
        WheelParam wheelParam = new WheelParam();
        BeanUtils.copyProperties(updateWheelRequest, wheelParam);
        wheelParam.setUpdateName(CommonUtil.getUserName());
        String oldImages = wheelMapper.queryImages(wheelParam.getWheelId());
        if (!StringUtils.isEmpty(oldImages)) {
            DelFileUtils.delFile(oldImages);
        }
        int count = wheelMapper.updateWheel(wheelParam);
        Result< T > result = new Result<>();
        if (count > 0) {
            result.success(CommonConstant.UPDATE_SUCCESSFULLY);
        }
        else {
            result.error500(CommonConstant.UPDATE_FAILED);
        }
        return result;
    }

    @Override
    public Result< T > delWheel(String wheelId) {
        List< String > list = new ArrayList<>(1);
        list.add(wheelId);
        int count = wheelMapper.batchDeleteWheel(list);
        Result< T > result = new Result<>();
        if (count > 0) {
            result.success(CommonConstant.DELETE_SUCCESSFULLY);
        }
        else {
            result.error500(CommonConstant.DELETE_FAILED);
        }
        return result;
    }

    @Override
    public Result< T > delWheels(DeleteWheelRequest deleteWheelRequest) {
        Result< T > result = new Result<>();
        int count = wheelMapper.batchDeleteWheel(CommonUtil.commaSeparatedStringList(deleteWheelRequest.getIds()));
        if (count > 0) {
            result.success(CommonConstant.MASS_DELETE_SUCCESSFULLY);
        }
        else {
            result.error500(CommonConstant.MASS_DELETE_FAILED);
        }

        return result;
    }

    @Override
    public Result< T > updateWheelIsFlag(UpdateWheelStatusRequest updateWheelStatusRequest) {
        UpdateWheelStatusParam updateWheelStatusParam = new UpdateWheelStatusParam();
        updateWheelStatusParam.setStatus(updateWheelStatusRequest.getIsFlag());
        updateWheelStatusParam.setUpdateName(CommonUtil.getUserName());
        updateWheelStatusParam.setWheelIdList(CommonUtil.commaSeparatedStringList(updateWheelStatusRequest.getIds()));
        int count = wheelMapper.updateIsFlag(updateWheelStatusParam);
        Result< T > result = new Result<>();
        if (count > 0) {
            if (WHEEL_NOT_STARTED.equals(updateWheelStatusRequest.getIsFlag())) {
                result.success("图片已关闭，将不在首页展示");
            }
            else {
                result.success("图片将展示在首页");
            }
        }
        return result;
    }

    @Override
    public Result< PageInfo< WheelVO > > listPageWheelBackstage(QueryWheelRequest queryWheelRequest) {
        startPage(queryWheelRequest.getPageNo(), queryWheelRequest.getPageSize());
        WheelParam wheelParam = new WheelParam();
        BeanUtils.copyProperties(queryWheelRequest, wheelParam);
        Result< PageInfo< WheelVO > > result = new Result<>();
        result.setSuccess(true);
        result.setResult(new PageInfo<>(BeansUtil.listCopy(wheelMapper.listWheelBackstage(wheelParam), WheelVO.class)));
        return result;
    }

    @Override
    public Result< List< String > > listShopByShopId(String id) {
        Result< List< String > > result = new Result<>();
        result.setSuccess(true);
        result.setResult(wheelMapper.listShopId(id));
        return result;
    }
}
